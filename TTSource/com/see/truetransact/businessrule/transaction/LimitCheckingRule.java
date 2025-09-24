/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingRule.java
 *
 * Created on February 5, 2004, 11:00 AM
 */

package com.see.truetransact.businessrule.transaction;

import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.TTException;

/**
 *
 * @author  rahul, bala
 *
 * LimitCheckingRule Checks for the Followings:
 * a) Whats the Status of the Particular Account?
 * b) Computes Whether the Given amount can be withdrawn from the Particular Account.
 *
 */
public class LimitCheckingRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of InwardClearingRule */
    public LimitCheckingRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /** 
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO 
     */
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    /**
     * Method To get the Data from the Database to be Compared
     * The inputMap is passed from the DAO.
     */
    private void getTarget(HashMap inputMap) throws Exception{
        System.out.println("inputMap LimitCheckingRule : "+inputMap);
        double amount =  CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue();
        if(amount < 0 )
            amount = -1 * amount;
        
        /*
         * The Mapped Statement "getOABalance" is used to get the values of 
         * the Available, Clear, Shadow, Limit balances.
         */
        if (!((String)inputMap.get(TransactionDAOConstants.PROD_TYPE)).equalsIgnoreCase("SA")) {
        List list = (List) sqlMap.executeQueryForList("getOABalance", inputMap);
        HashMap resultMap = (HashMap)list.get(0);
        double flexiAmount = CommonUtil.convertObjToDouble(resultMap.get("FLEXI_AMT")).doubleValue();
        double flexiMinBal = CommonUtil.convertObjToDouble(resultMap.get("MIN_BAL2_FLEXI")).doubleValue();
        double availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BALANCE")).doubleValue();
        double shadowCredit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_CREDIT")).doubleValue();
        double Freeze = CommonUtil.convertObjToDouble(resultMap.get("FREEZE_AMT")).doubleValue();
        double lien = CommonUtil.convertObjToDouble(resultMap.get("LIEN_AMT")).doubleValue();
        double clearBalance = CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue();
        double shadowDebit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_DEBIT")).doubleValue();
//        availableBalance += shadowCredit; //- shadowDebit;
        double limit = CommonUtil.convertObjToDouble(resultMap.get("TOD_LIMIT")).doubleValue();
        String status = CommonUtil.convertObjToStr(resultMap.get("STATUS"));
        Date lastTransDt = (Date) resultMap.get("LAST_TRANS_DT");

            // Added the block by nithya on 16-03-2019 for KD 355 Savings Bank Interest - Available Balance Negative A/Cs persons Overdraft interest not taking
            if (inputMap.containsKey("INSTRUMENT1") && inputMap.get("INSTRUMENT1") != null && CommonUtil.convertObjToStr(inputMap.get("INSTRUMENT1")).equalsIgnoreCase("OD INTEREST")) {
                String acctNum = CommonUtil.convertObjToStr(inputMap.get("ACCOUNTNO"));
                System.out.println("acctNum.... " + acctNum);
                String prodId = CommonUtil.convertObjToStr(acctNum.substring(4, 7));
                HashMap todCheckMap = new HashMap();
                todCheckMap.put("PROD_ID", prodId);
                List todList = sqlMap.executeQueryForList("isTODSetForProduct", todCheckMap);
                if (todList != null && todList.size() > 0) {
                    todCheckMap = (HashMap) todList.get(0);
                    if (CommonUtil.convertObjToStr(todCheckMap.get("TEMP_OD_ALLOWED")).equals("Y")) {
                        System.out.println("SB OD Interest Processing..");
                        if(availableBalance < 0){
                            availableBalance = amount;                           
                        }
                    }
                }
            }
        
         if (status.equals("TOTAL_FREEZE")) {
                throw new ValidationRuleException(TransactionConstants.BOTH_CR_DT);
            }
        
        // Debit Transaction Checking
        if (((String)inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            if (status.equals("COMP_FREEZE")) {
                throw new ValidationRuleException(TransactionConstants.COMP_FREEZE);
            }else if(status.equals("DEBIT_FREEZE")){
                throw new ValidationRuleException(TransactionConstants.DEBIT_FREEZE);
            }else {
                if ((availableBalance + limit) < amount) {
                    throw new ValidationRuleException(TransactionConstants.INSUFFICIENT_BALANCE);
                }else if(lien>0.0 || Freeze>0.0){
                    if((clearBalance-(lien+Freeze))<amount){
                        throw new ValidationRuleException(TransactionConstants.INSUFFICIENT_CLEARBALANCE);
                    }
                }
            }
            if(inputMap.containsKey("ACT_CLOSING_MIN_BAL_CHECK_CASH_TRANSFER")){
                if (inputMap.containsKey("ACTUAL_AMT"))
                    amount =  CommonUtil.convertObjToDouble(inputMap.get("ACTUAL_AMT")).doubleValue();
                if(CommonUtil.convertObjToStr(inputMap.get("AUTHORIZE_STATUS")).length()>0)
                    amount=availableBalance-amount;
                else if(CommonUtil.convertObjToStr(inputMap.get("TO_STATUS")).equals("CREATED"))
                    amount= (availableBalance-shadowDebit)-amount;
                else if(CommonUtil.convertObjToStr(inputMap.get("TO_STATUS")).equals("MODIFIED")){
                    List transList = new ArrayList();
                    if(CommonUtil.convertObjToStr(inputMap.get("TRANSMODE")).equalsIgnoreCase("CASH")){
                        transList = (List) sqlMap.executeQueryForList("getOldAmountFromCash", inputMap);
                    }else if(CommonUtil.convertObjToStr(inputMap.get("TRANSMODE")).equalsIgnoreCase("TRANSFER")){
                        transList = (List) sqlMap.executeQueryForList("getOldAmountFromTransfer", inputMap);
                    }else{
                        transList = (List) sqlMap.executeQueryForList("getOldAmountFromClearing", inputMap);
                    }
                    if(transList!=null && transList.size()>0){
                        HashMap amtMap = new HashMap();
                        amtMap=(HashMap)transList.get(0);
                        double oldAmt= CommonUtil.convertObjToDouble(amtMap.get("AMOUNT")).doubleValue();
                        amount=availableBalance-((shadowDebit-oldAmt)+amount);
                    }
                }
                double amt = 0.0;
                amt=checkForMinAmount(inputMap);
                if (amount < amt) {
                    throw new ValidationRuleException(TransactionConstants.MIN_BALANCE);
                }
            }
        } else { // Credit Transaction Checking
              if (status.equals("CREDIT_FREEZE")) {
                throw new ValidationRuleException(TransactionConstants.CREDIT_FREEZE);
            }
            if (lastTransDt == null) {
                if (inputMap.containsKey("ACTUAL_AMT"))
                    amount =  CommonUtil.convertObjToDouble(inputMap.get("ACTUAL_AMT")).doubleValue();
                   double amt = 0.0;
//                HashMap minMap = (HashMap) sqlMap.executeQueryForObject("getMinBalance", inputMap);
//                double withoutChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITHOUT_CHQ")).doubleValue();
//                double withChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITH_CHQ")).doubleValue();
//                String chqBk = CommonUtil.convertObjToStr(minMap.get("CHQ_BOOK")).toUpperCase();
//
//                if (chqBk.equals("Y")) {
//                    amt =  withChq;
//                } else {
//                    amt = withoutChq;
//                }
          // commented by rishad after discussion with rajesh--creidt time minimum balance checking not required 22/04/2015
//                amt=checkForMinAmount(inputMap);
//                if (amount < amt) {
//                    throw new ValidationRuleException(TransactionConstants.MIN_BALANCE, String.valueOf(amt));
//                }
            }
        }
        //Added by sreekrishnan suspense debit validation
        }else { 
            if (((String)inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
                inputMap.put("ACT_NUM", CommonUtil.convertObjToStr(inputMap.get("ACCOUNTNO")));
                List suspenseList = (List) sqlMap.executeQueryForList("getBalanceSA", inputMap);
                if(suspenseList!=null && suspenseList.size()>0){
                    HashMap suspenseResultMap = (HashMap)suspenseList.get(0);
                    double suspenseClearBalance = CommonUtil.convertObjToDouble(suspenseResultMap.get("CLEAR_BALANCE")).doubleValue();
                    double suspenseTotalBalance = CommonUtil.convertObjToDouble(suspenseResultMap.get("TOTAL_BALANCE")).doubleValue();
                    inputMap.put("ACCT_NUM", CommonUtil.convertObjToStr(inputMap.get("ACCOUNTNO")));
                    List suspenseProdList = (List) sqlMap.executeQueryForList("getNegativeAmtCheckForSA", inputMap);
                    if (suspenseProdList != null && suspenseProdList.size() > 0) {
                        HashMap tempMap = (HashMap) suspenseProdList.get(0);
                        String negYn = CommonUtil.convertObjToStr(tempMap.get("NEG_AMT_YN"));
                        if ((negYn!=null && negYn.length()>0 && !negYn.equals("Y"))&& suspenseTotalBalance < amount) {
                            throw new ValidationRuleException(TransactionConstants.INSUFFICIENT_BALANCE);
                        }
                    }
                }
            }
        }
    }
    private double  checkForMinAmount(HashMap inputMap){
        double amt = 0.0;
        try{
                HashMap minMap = (HashMap) sqlMap.executeQueryForObject("getMinBalance", inputMap);
                double withoutChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITHOUT_CHQ")).doubleValue();
                double withChq = CommonUtil.convertObjToDouble(minMap.get("MIN_WITH_CHQ")).doubleValue();
                String chqBk = CommonUtil.convertObjToStr(minMap.get("CHQ_BOOK")).toUpperCase();
                System.out.println("Inside checkForMinAmount :");
                if (chqBk.equals("Y")) {
                    amt =  withChq;
                } else {
                    amt = withoutChq;
                }   
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return amt;
    }

    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTNO", "OA001001" );
        inputMap.put("AMOUNT", "5000");
        
        LimitCheckingRule iRule = new LimitCheckingRule();
        iRule.validate(inputMap);
    }
}

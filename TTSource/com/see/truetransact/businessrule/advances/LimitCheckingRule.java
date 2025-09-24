/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LimitCheckingRule.java
 *
 * Created on May 5, 2005, 12:54 PM
 */

package com.see.truetransact.businessrule.advances;

import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;
import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.advances.AdvancesConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.transferobject.operativeaccount.TodAllowedTO;
import com.see.truetransact.serverside.operativeaccount.TodAllowedDAO;


/**
 *
 * @author  152713
 */
public class LimitCheckingRule extends ValidationRule{
    private SqlMap sqlMap = null;
    /** Creates a new instance of LimitCheckingRule */
    public LimitCheckingRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    /**
     * Method To get the Data from the Database to be Compared
     * The inputMap is passed from the DAO.
     */
    private void getTarget(HashMap inputMap) throws Exception{
        System.out.println("###$$ LimitCheckingRule : inputMap : "+inputMap);
        double amount =  CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue();
        if (amount < 0)
            amount = -amount;
        
        /*
         * The Mapped Statement "getTLBalance" is used to get the values of 
         * the Available, Clear, Shadow, Limit balances.
         */
        List list = (List) sqlMap.executeQueryForList("getTLBalance", inputMap);
        List calDrawAmtlist = (List) sqlMap.executeQueryForList("getTotCalculatedDrawingAmountAD", inputMap);
        HashMap resultMap = new HashMap();
        double availableBalance = 0.0;
        double clearBalance =0.0;
        double shadowCredit = 0.0;
        double shadowDebit = 0.0;
        double limit = 0.0;
        double limitAmt = 0;
        double calDrawingAmt = 0.0;
        
        String status = "";
        if (list != null && list.size() > 0) {
            resultMap = (HashMap) list.get(0);
            if (resultMap != null && resultMap.size() > 0) {
                availableBalance = CommonUtil.convertObjToDouble(resultMap.get("AVAILABLE_BALANCE")).doubleValue();
                clearBalance = CommonUtil.convertObjToDouble(resultMap.get("CLEAR_BALANCE")).doubleValue();
                shadowCredit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_CREDIT")).doubleValue();
                shadowDebit = CommonUtil.convertObjToDouble(resultMap.get("SHADOW_DEBIT")).doubleValue();
                limit = CommonUtil.convertObjToDouble(resultMap.get("LIMIT")).doubleValue();
                limitAmt = availableBalance;
                calDrawingAmt = 0.0;

                status = CommonUtil.convertObjToStr(resultMap.get("STATUS"));
            }
            list = null;
            resultMap = null;
        }
        if (calDrawAmtlist.size() > 0) {
            resultMap = (HashMap) calDrawAmtlist.get(0);;
            calDrawingAmt = CommonUtil.convertObjToDouble(resultMap.get("LIMIT")).doubleValue();

            calDrawAmtlist = null;
            resultMap = null;
        }
        
        if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT)) {
            /* If the Limit amount in Facility details is less than 
             *   the Total calculated drawing amount then the 'amount' should be less than 
             *   the facility detail's limit amount 
             * else
             *   'amount' should be less than the Total calculated drawing amount
             */
            
            if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.DEBIT) &&  CommonUtil.convertObjToStr(inputMap.get("DEBIT_LOAN_TYPE")).equals("DP")) {
            if (limit < calDrawingAmt){
                limitAmt = calDrawingAmt + availableBalance;
            } 
//            else
//            {
//                limitAmt = limit + availableBalance;
//            }
            if (inputMap.get(TransactionDAOConstants.PROD_TYPE).equals("AD")) {
                limitAmt=availableBalance;
                System.out.println("status :: " + status);
                if(status.equalsIgnoreCase("CLOSED")){
                    limitAmt = amount;
                }
            } else {
                if(limit<availableBalance)
                    limitAmt=limit;
                else
                    limitAmt=availableBalance;
            }
           
            if (limitAmt < amount) {
                System.out.println("limitAmt : amount : " + limitAmt + ":" + amount);
                 if( ! (inputMap.containsKey(("DEBIT_LOAN_TYPE")) && CommonUtil.convertObjToStr(inputMap.get("DEBIT_LOAN_TYPE")).equals("CLEARING_BOUNCED")))
                throw new ValidationRuleException(AdvancesConstants.ADVANCES_EXCEED_LIMIT);
                 else if( ! inputMap.containsKey("DEBIT_LOAN_TYPE"))
                 throw new ValidationRuleException(AdvancesConstants.ADVANCES_EXCEED_LIMIT);
            }
            }else{
                // without available balance they want close the od account but transaction time intrest debit into od account it should allow the transaction and basing excess amt create TOD
                if (inputMap.get(TransactionDAOConstants.PROD_TYPE).equals("AD")) {
                limitAmt=availableBalance;
                if (limitAmt < amount){
                if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.AUTHORIZE_STATUS)).equals("AUTHORIZED")) {//CommonUtil.convertObjToStr(inputMap.get("ACCOUNT_CLOSING")).equals("ACCOUNT_CLOSING") &&
                HashMap data=new HashMap();
                data.put("TodAllowed",setTodTransaction(inputMap,limitAmt));
                data.put("AUTHORIZEMAP", getauthorizeMap(inputMap)); //For Authorization added 28 Apr 2005
                data.put("BRANCH_CODE", inputMap.get("BRANCH_CODE"));
                data.put("MODE","INSERT");             
                data.put("AUTO_POSTING_EXCESS_AMOUNT","AUTO_POSTING_EXCESS_AMOUNT"); // Added by nithya on 03-11-2017 for 0008180
                System.out.println("data####"+data);
                TodAllowedDAO todAllowedDAO=new TodAllowedDAO();
                todAllowedDAO.execute(data,true,true);
            
                }
                }
                }
            }
        }else if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
            
        }
        
    }
    
    
     public TodAllowedTO setTodTransaction(HashMap inputMap ,double limitAmt) {
       
        
        final TodAllowedTO objTodAllowedTO = new TodAllowedTO();
        try{
           objTodAllowedTO.setTrans_id("_");
            objTodAllowedTO.setAccountNumber(CommonUtil.convertObjToStr(inputMap.get("ACCOUNTNO")));
            //           objTodAllowedTO.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));
            java.util.Date FromDt = (Date)inputMap.get("TODAY_DT");
            objTodAllowedTO.setFromDate(FromDt);

            objTodAllowedTO.setToDate(FromDt);
            objTodAllowedTO.setPermittedDt(FromDt);
            objTodAllowedTO.setStatusBy("TTSYSTEM");
            objTodAllowedTO.setRemarks("Automatic TOD Closing Acc");
            objTodAllowedTO.setProductType(CommonUtil.convertObjToStr(inputMap.get("PROD_TYPE")));
            objTodAllowedTO.setProductId(CommonUtil.convertObjToStr(inputMap.get("PROD_ID")));
            objTodAllowedTO.setPermitedBy("sys");
            double todAmt =CommonUtil.convertObjToDouble(inputMap.get("ACTUAL_AMT")).doubleValue()-limitAmt;
            objTodAllowedTO.setTodAllowed(String.valueOf(todAmt));//CommonUtil.convertObjToStr(inputMap.get("ACTUAL_AMT")));
            objTodAllowedTO.setPermissionRefNo("Auto TOD");
            objTodAllowedTO.setBranchCode(CommonUtil.convertObjToStr(inputMap.get("BRANCH_CODE")));
            //           objTodAllowedTO.setAcctName(CommonUtil.convertObjToStr(getTxtAcctName()));

            //           objTodAllowedTO.setIntCalcDt(PermittedDt);
            objTodAllowedTO.setRepayPeriod(new Double((1)));
            objTodAllowedTO.setRepayPeriodDDMMYY(String.valueOf(1));
            objTodAllowedTO.setTypeOfTOD("SINGLE");
            objTodAllowedTO.setRepayDt(FromDt);
            System.out.println("objTodAllowedTO###"+objTodAllowedTO);
        }catch(Exception e){
         
            e.printStackTrace();
        }
        return objTodAllowedTO;
    }
     
     private HashMap getauthorizeMap(HashMap inputMap){
           ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", "CREATED");
            singleAuthorizeMap.put("TRANS_ID", "");
            singleAuthorizeMap.put("TOD_AMOUNT", inputMap.get("ACTUAL_AMT"));
            singleAuthorizeMap.put("ACT_NUM", inputMap.get("ACCOUNTNO"));
            singleAuthorizeMap.put("PROD_TYPE", inputMap.get("PROD_TYPE"));            
            arrList.add(singleAuthorizeMap);
            
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, "AUTHORIZED");
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
             System.out.println("authorizeMap###"+authorizeMap);
            return authorizeMap;
     }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}

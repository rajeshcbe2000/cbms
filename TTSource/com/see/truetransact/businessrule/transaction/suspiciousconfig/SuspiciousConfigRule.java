/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SuspiciousConfigRule.java
 *
 * Created on March 10, 2005, 4:42 PM
 */

package com.see.truetransact.businessrule.transaction.suspiciousconfig;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.transferobject.operativeaccount.AccountTO;
/**
 *
 * @author  152721
 */
public class SuspiciousConfigRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    
    /** Creates a new instance of SuspiciousConfigRule */
    public SuspiciousConfigRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE));

        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        ArrayList suspList = new ArrayList();
        String actNum = CommonUtil.convertObjToStr(inputMap.get("ACCOUNTNO"));
        if (!inputMap.containsKey("CONF_FOR")) {
            inputMap.put("CONF_FOR", CommonConstants.ACCOUNT);
        }
        
        suspList = (ArrayList)getSuspeciousData(inputMap);
        
        System.out.println ("Account suspList : " + suspList);
        int suspLen = suspList.size();
        
        if (suspList.size()==0) {
            inputMap.put("CONF_FOR", CommonConstants.CUSTOMER);
            inputMap.remove("ACCOUNTNO");
            suspList = (ArrayList)getSuspeciousData(inputMap);
            suspLen = suspList.size();
            System.out.println ("Customer suspList : " + suspList);
        }
        
        if (suspList.size()==0) {
            inputMap.put("CONF_FOR", CommonConstants.GENERAL);
            inputMap.remove("ACCOUNTNO");
            suspList = (ArrayList)getSuspeciousData(inputMap);
            suspLen = suspList.size();
            System.out.println ("General suspList : " + suspList);
        }
        inputMap.put("ACCOUNTNO", CommonUtil.convertObjToStr(actNum));
        HashMap suspMap = new HashMap();
        
        //__ If the data Exists... Varify it...
        if(suspLen > 0){
            suspMap = (HashMap)suspList.get(0);
            suspMap.putAll(inputMap);
            
            suspMap.put("CURRENT_DT",ServerUtil.getCurrentDate(super._branchCode));
            
            System.out.println("suspMap: " + suspMap);
            HashMap resultMap = new HashMap();
            String confFor = (String) inputMap.get("CONF_FOR");
            int rowCount = 0;
            double amount = 0;
            boolean isAccountValidated = false;
                      
            //__ To Validate the data at the Account Level...
            if(confFor.equalsIgnoreCase(CommonConstants.ACCOUNT)){
                suspMap.put("PROD_TYPE",inputMap.get("PROD_TYPE"));
                suspMap.put("ACT_NUM",inputMap.get("ACCOUNTNO"));
                
                resultMap = validateData(suspMap);
                
                rowCount = CommonUtil.convertObjToInt(resultMap.get("ROWS"));
                amount = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
            }
            
            //__ To Validate the data at the Account Level...
            if(confFor.equalsIgnoreCase(CommonConstants.CUSTOMER)){
                List lst = sqlMap.executeQueryForList("getSelectAccountTO", inputMap.get("ACCOUNTNO"));
                AccountTO actTO = new AccountTO();
                if (lst.size()>0)
                    actTO = (AccountTO)lst.get(0);
//                ArrayList custList = (ArrayList)getCustData(CommonUtil.convertObjToStr(inputMap.get("CUST_ID")));
                ArrayList custList = (ArrayList)getCustData(actTO.getCustId());                
                int length = custList.size();
                
                for(int i = 0; i< length; i++){
                    resultMap = (HashMap)custList.get(i);
                    
                    suspMap.put("PROD_TYPE", resultMap.get("PROD_TYPE"));
                    suspMap.put("ACT_NUM", resultMap.get("ACT_NUM"));
                    
                    resultMap = validateData(suspMap);
                    rowCount = rowCount + CommonUtil.convertObjToInt(resultMap.get("ROWS"));
                    amount = amount + CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
                }
                isAccountValidated = true;
            }
            
            //__ To Validate the data at the General Level...
            if(confFor.equalsIgnoreCase(CommonConstants.GENERAL)){
                suspMap.put("PROD_TYPE",inputMap.get("PROD_TYPE"));
                suspMap.put("ACT_NUM",inputMap.get("ACCOUNTNO"));
                
                System.out.println("suspMap: " + suspMap);
                
                resultMap = validateData(suspMap);
                
                rowCount = CommonUtil.convertObjToInt(resultMap.get("ROWS"));
                amount = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
            }
            
            System.out.println("resultMap in susp config :: " + resultMap);
            
            if(resultMap.containsKey("VALIDATION_REQUIRED") && resultMap.get("VALIDATION_REQUIRED") != null && resultMap.get("VALIDATION_REQUIRED").equals("Y")){
                isAccountValidated = true;
            }
            suspList = null;
            resultMap = null;
            
            System.out.println("rowCount: " + rowCount);
            System.out.println("amount: " + amount);
            
            //__ If the totalCount Exceeds the specified Limit, throw the wxception...
            if (isAccountValidated) {
            if(rowCount >= CommonUtil.convertObjToInt(suspMap.get("COUNT"))){
                throw new ValidationRuleException(TransactionConstants.COUNT_EXCEEDS);
            }
            
            //__ If the totalAmount Exceeds the specified Limit, throw the wxception...
            if(amount >= CommonUtil.convertObjToDouble(suspMap.get("WORTH")).doubleValue()){
                throw new ValidationRuleException(TransactionConstants.WORTH_EXCEEDS);
            }
        }
    }
    }
    
    //__ To get the number of transactions and Amount for the Particular Account No...
    private HashMap validateData(HashMap inputMap) throws Exception {
        HashMap dataMap = new HashMap();
        HashMap resultMap;
        int totalCount = 0;
        int cashCount = 0;
        int transCount = 0;
        int inwardCount = 0;
        int paySlipCount = 0;
        int exeFlag = 0;
        double totalAmt = 0;
        double cashAmt = 0;
        double transAmt = 0;
        double inwardAmt = 0;
        double paySlipAmt = 0;
        String validationRequired = "N";
        dataMap.put("PERIOD",inputMap.get("PERIOD"));
        dataMap.put("ACCOUNTNO",inputMap.get("ACT_NUM"));
        dataMap.put("CURRENT_DT",inputMap.get("CURRENT_DT"));
        dataMap.put("PROD_TYPE",inputMap.get("PROD_TYPE"));
        String actNum = CommonUtil.convertObjToStr(inputMap.get("ACT_NUM"));
        String transType = CommonUtil.convertObjToStr(inputMap.get("TRANSTYPE"));
        String transMode = CommonUtil.convertObjToStr(inputMap.get("TRANSMODE"));
        if((CommonUtil.convertObjToStr(inputMap.get("CONF_FOR")).equalsIgnoreCase("GENERAL"))){
            dataMap.put("PROD_ID", inputMap.get("PROD_ID"));
            dataMap.remove("ACCOUNTNO");
        }
        
        
        //__ Check for the Kind of Cash Transaction...
        if(transMode.equals("CASH") && transType.equals("CREDIT") && CommonUtil.convertObjToStr(inputMap.get("CR_CASH")).equalsIgnoreCase("Y")){
            dataMap.put("CREDIT","CREDIT");
            exeFlag = 1;
        }else{
            dataMap.put("CREDIT","");
        }
        if(transMode.equals("CASH") && transType.equals("DEBIT") && CommonUtil.convertObjToStr(inputMap.get("DR_CASH")).equalsIgnoreCase("Y")){
            dataMap.put("DEBIT","DEBIT");
            exeFlag = 1;
        }else{
            dataMap.put("DEBIT","");
        }
        if(exeFlag == 1){
            //__ get the Count for Cash Transaction...
            resultMap = getRowCounts("getCashTrasnCount", dataMap);
            cashCount = CommonUtil.convertObjToInt(resultMap.get("ROWS"));
            cashAmt = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
            validationRequired = "Y";
            exeFlag = 0;
        }
        
        //__ To Check for the Kind of Transfer Transaction...
        if(transMode.equals("TRANSFER") && transType.equals("CREDIT") && CommonUtil.convertObjToStr(inputMap.get("CR_TRANS")).equalsIgnoreCase("Y")){
            dataMap.put("CREDIT","CREDIT");
            exeFlag = 1;
        }else{
            dataMap.put("CREDIT","");
        }
        if(transMode.equals("TRANSFER") && transType.equals("DEBIT") && CommonUtil.convertObjToStr(inputMap.get("DR_TRANS")).equalsIgnoreCase("Y")){
            dataMap.put("DEBIT","DEBIT");
            exeFlag = 1;
        }else{
            dataMap.put("DEBIT","");
        }
        
        if(exeFlag == 1){
            //__ get the Count for Cash Transaction...
            resultMap = getRowCounts("getTransferTrasnCount", dataMap);
            transCount = CommonUtil.convertObjToInt(resultMap.get("ROWS"));
            transAmt = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
            validationRequired = "Y";
            exeFlag = 0;
        }
        
        if(inputMap.containsKey("DR_CLEARING")){
            //__ get the Count for InwardClearing Transaction...
            resultMap = getRowCounts("getInwardClearingCount", dataMap);
            inwardCount = CommonUtil.convertObjToInt(resultMap.get("ROWS"));
            inwardAmt = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
        }
        
        if(inputMap.containsKey("CR_CLEARING")){
            //__ get the Count for Pay Slip Transaction...
            resultMap = getRowCounts("getPaySlipCount", dataMap);
            paySlipCount = CommonUtil.convertObjToInt(resultMap.get("ROWS"));
            paySlipAmt = CommonUtil.convertObjToDouble(resultMap.get("AMOUNT")).doubleValue();
        }
        
        totalCount = cashCount + transCount + inwardCount + paySlipCount;
        totalAmt = cashAmt + transAmt + inwardAmt + paySlipAmt;
        
        inputMap = new HashMap();
        inputMap.put("ROWS",String.valueOf(totalCount));
        inputMap.put("AMOUNT",String.valueOf(totalAmt));
        inputMap.put("VALIDATION_REQUIRED",validationRequired);
        dataMap = null;
        resultMap = null;
        return inputMap;
    }
    
    //__ To get the Number of transactions in the Particular Table...
    private HashMap getRowCounts(String query, HashMap inputMap) throws Exception{
        List list = sqlMap.executeQueryForList(query,inputMap);
        inputMap = (HashMap)list.get(0);
        return inputMap;
    }
    
    //__ To get the Number of transactions in the Particular Table...
    private List getCustData(String custId) throws Exception{
        List list = sqlMap.executeQueryForList("getCustInfoData",custId);
        return list;
    }
    
    //__ To get the data from the Suspecious Config table...
    private List getSuspeciousData(HashMap inputMap) throws Exception{
        System.out.println("inputMap: " + inputMap);
        List list = sqlMap.executeQueryForList("getSuspeciousConfigData",inputMap);
        return list;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        HashMap inputMap = new HashMap();
        //inputMap.put("CONF_FOR",CommonConstants.GENERAL);
        
        //        inputMap.put("PERIOD","300");
        inputMap.put("PROD_TYPE","OA");
        inputMap.put("ACCOUNTNO","OA060886"); //  OA001011
//        inputMap.put("CUST_ID","C0001065"); //  C0001065  C0001025
        
//        inputMap.put("CURRENT_DT",ServerUtil.getCurrentDate());
//        inputMap.put("COUNT", String.valueOf(25));
//        inputMap.put("WORTH", String.valueOf(4000));
        
//        inputMap.put("CR_CASH","");
//        inputMap.put("DR_CASH","");
//        inputMap.put("CR_TRANS","");
//        inputMap.put("DR_TRANS","");
//        inputMap.put("CR_CLEARING","");
//        inputMap.put("DR_CLEARING","");
        try{
            SuspiciousConfigRule sRule = new SuspiciousConfigRule();
            sRule.getTarget(inputMap);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
}

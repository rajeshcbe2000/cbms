/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeInstrumentRule.java
 *
 * Created on February 9, 2004, 1:50 PM
 */

package com.see.truetransact.businessrule.transaction;

import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;

import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

/**
 *
 * @author  rahul, bala
 *
 * ChequeInstrumentRule Checks for the following:
 * a) if the Cheque belongs to the particular Account No.
 * b) Has it been Cleared Already?
 *
 */
public class ChequeInstrumentRule extends ValidationRule{
    private SqlMap sqlMap = null;
    private final String INSTRU_TYPE = "CHEQUE";
    private String errMsg;
    
    /** Creates a new instance of ChequeInstrumentRule */
    public ChequeInstrumentRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /* 
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO.
     */
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    /*
     * Method To get the Data from the Database to be Compared...
     * inputMap passed from the DAO...
     */
    private void getTarget(HashMap inputMap) throws Exception{
        boolean check;
        
        errMsg = "";
        errMsg += "\nA/c No. : "+inputMap.get(TransactionDAOConstants.ACCT_NO);
        errMsg += "\nInstrument : "+inputMap.get(TransactionDAOConstants.INSTRUMENT_1)+inputMap.get(TransactionDAOConstants.INSTRUMENT_2);
        
        /* The Mapped Statement "getChequeIssueNoRule" "getLooseLeafRule" and are
         * in InwardClearingMap.and are used to Know if the
         * Issued Cheque Is a valid Cheque Book/ Leaf No.
         */
        List chequeIssued = (List) sqlMap.executeQueryForList("getChequeIssueNoRule", inputMap);
        
        // IF The Cheque Is Taken from the Cheque Book and Its a valid One...
        if( chequeIssued!=null && chequeIssued.size() <= 0 ){
            /*
             * IF The Cheque Is Taken as the
             * Loose Lesf and Its a valid One...
             */
            chequeIssued = (List) sqlMap.executeQueryForList("getLooseLeafRule", inputMap);
        }
        
        if(chequeIssued!=null && chequeIssued.size() > 0 ) {            
            inward(inputMap);            
        } else {            
            throw new ValidationRuleException(TransactionConstants.INSTRUMENT_NOT_ISSUED, errMsg);
        }
    }
    
    /**
     * To Check is the Particular Cheque has Already been Cashed of Not.
     */
    private void inward(HashMap inputMap) throws Exception{
        /*
         * Mapped Statement is in InwardClearingMap...
         * To Know Whether the Cheque has been used Earlier or not...
         */
        
        // To Know if the Instrument is already Cleared. Checking with Inward Clearing        
        
//        List instrumentCleared = (List)sqlMap.executeQueryForList("getInstrumentNoRule",inputMap);
//        
//        if (instrumentCleared.size() <= 0) {
//            // To Know if the Instrument is already Encashed. Checking with Cash
//            instrumentCleared = (List)sqlMap.executeQueryForList("getCashInstrumentNoRule",inputMap);
//        }
//        
//        if ((instrumentCleared.size() <= 0) && inputMap.containsKey("TRANS_ID")) {
//            // Checking with Transfer
//            instrumentCleared = (List)sqlMap.executeQueryForList("getTransferNoRule",inputMap);            
//        }
    
        // To Know if the Instrument is already Cleared. Checking with Inward Clearing, Cash Transaction
        // and Transfer Transaction...
        inputMap.put("INSTRU_TYPE", INSTRU_TYPE);
        inputMap.put("TRANS_DT", inputMap.get(TransactionDAOConstants.TODAY_DT));
        inputMap.put("INITIATED_BRANCH", inputMap.get(TransactionDAOConstants.INITIATED_BRANCH));
        List instrumentCleared = (List)sqlMap.executeQueryForList("getInstrumentClearedRule",inputMap);
        
        //__ Instrument Cleared...
        if (instrumentCleared!=null && instrumentCleared.size() > 0) {
            HashMap clearMap = (HashMap)instrumentCleared.get(0);
            
            if(CommonUtil.convertObjToStr(clearMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                System.out.println("Instrument Cleared, Authorized...");
                throw new ValidationRuleException(TransactionConstants.INSTRUMENT_CLEARED, errMsg);
                
            }else{
                System.out.println("Instrument sent for Clearing, Pending for Authorization...");
                throw new ValidationRuleException(TransactionConstants.INSTRUMENT_CLEARED_NA, errMsg);
            }            
        } else {
            // To Know if the Instrument is stopped.            
            List stopRule = (List)sqlMap.executeQueryForList("getStopInstrumentNoRule",inputMap);            
            
            //__ Cheque sent to be Stopped...
            if (stopRule!=null && stopRule.size() > 0) {  
                HashMap stopMap = (HashMap)stopRule.get(0);
                if(CommonUtil.convertObjToStr(stopMap.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")){
                    if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                        System.out.println("Cheque Stopped, Authorized...");
                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_STOP_PAY, errMsg);

                    }else{
                        System.out.println("Cheque Stopped, Pending for Authorization...");
                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_STOP_PAY_NA, errMsg);
                    }
                }else{
                    if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("")){
                        System.out.println("Cheque Revoked, Pending for Authorized...");
                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_REVOK_NA, errMsg);
                    }else if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("REJECTED")){
                        System.out.println("Cheque Revoked, rejected...");
                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_STOP_PAY, errMsg);
                    }
                }
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTNO", "OA001003" );
        inputMap.put("INSTRUMENT1", "Vbs");
        inputMap.put("INSTRUMENT2", "10102");
        
        ChequeInstrumentRule cRule = new ChequeInstrumentRule();
        cRule.validate(inputMap);
        System.out.println ("Done");
    }
    
    
}

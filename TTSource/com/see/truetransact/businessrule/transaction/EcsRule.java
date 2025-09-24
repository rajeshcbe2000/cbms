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
public class EcsRule extends ValidationRule{
    private SqlMap sqlMap = null;
    private final String INSTRU_TYPE = "ECS";
    private String errMsg;
    
    /** Creates a new instance of ChequeInstrumentRule */
    public EcsRule() throws Exception {
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
         inward(inputMap);
    }
    
    /**
     * To Check is the Particular Ecs.
     */
    private void inward(HashMap inputMap) throws Exception{

            // To Know if the ECS is stopped.            
            List stopRule = (List)sqlMap.executeQueryForList("checkForEcsStopPayment",inputMap);            
            
            //__ Ecs sent to be Stopped...
            if (stopRule!=null && stopRule.size() > 0) {  
                HashMap stopMap = (HashMap)stopRule.get(0);
                if(CommonUtil.convertObjToStr(stopMap.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")){
                    if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                        System.out.println("Ecs Stopped, Authorized...");
                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_ECS_STOP_PAY, errMsg);

                    }else{
                        System.out.println("Ecs Stopped, Pending for Authorization...");
                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_ECS_STOP_PAY_NA, errMsg);
                    }
                }
//                    else{
//                    if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("")){
//                        System.out.println("Ecs Revoked, Pending for Authorized...");
//                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_REVOK_NA, errMsg);
//                    }else if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("REJECTED")){
//                        System.out.println("Ecs Revoked, rejected...");
//                        throw new ValidationRuleException(TransactionConstants.INSTRUMENT_STOP_PAY, errMsg);
//                    }
//                }
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
        
        EcsRule cRule = new EcsRule();
        cRule.validate(inputMap);
        System.out.println ("Done");
    }
    
    
}

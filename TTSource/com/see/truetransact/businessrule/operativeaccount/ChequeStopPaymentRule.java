/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChequeStopPaymentRule.java
 *
 * Created on May 18, 2004, 6:27 PM
 */

package com.see.truetransact.businessrule.operativeaccount;
import java.util.HashMap;
import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.commonutil.exceptionconstants.operativeaccount.OperativeAccountConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverutil.ServerConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
/**
 *
 * @author  rahul
 * ChequeStopPaymentRule Checks for the following:
 * a) if the Cheque(s) were issued ?
 * b) if the issued Cheque(s) are already cleared ?
 * c) if the Cheque(s) are already stopped ?
 *
 */
public class ChequeStopPaymentRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of ChequeStopPaymentRule */
    public ChequeStopPaymentRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    
    final String INSTRU_TYPE = "CHEQUE";
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
        int loop = 0;
        System.out.println("inputMap##"+inputMap);
         super._branchCode = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.BRANCH_CODE));

        inputMap.put("INSTRU_TYPE", INSTRU_TYPE);
        
        /* The Mapped Statement "getChequeIssueNoRule", "getDraftRule", "getStopInstrumentNoRule"
         * and are in InwardClearingMap.and are used to Know if the
         * Validity of the Cheque(s) to be stopped .
         */
        final int INSTRU1 = CommonUtil.convertObjToInt(inputMap.get("INSTRU1"));
        final int INSTRU2 = CommonUtil.convertObjToInt(inputMap.get("INSTRU2"));
        if(INSTRU2 !=0){
            loop = INSTRU2 - INSTRU1;
        }
        System.out.println("loop: "+loop);
        
        for (int i =0; i <= loop; i++){
            inputMap.put("INSTRUMENT2", String.valueOf(INSTRU1 + i) );
            System.out.println("inputMap: "+inputMap);
            /*
             * To Check, if the Cheque(s) were issued or not
             */
            List chequeIssued = (List) sqlMap.executeQueryForList("getChequeIssueNoRule", inputMap);
            if( !(chequeIssued.size() > 0) ){
                System.out.println("Cheque Not Issued");
                throw new ValidationRuleException(OperativeAccountConstants.CHEQUENOTISSUED);
            }else{
                System.out.println("Cheque Is Issued");
            }
            
            /*
             * To Check, if the Cheque(s) are already cleared or not.
             */
            
            //            List chequeCleared = (List) sqlMap.executeQueryForList("getDraftRule", inputMap);
//            inputMap.put("BRANCH_CODE",inputMap.get("BRANCH_ID"));
            inputMap.put("TRANS_DT", inputMap.get(TransactionDAOConstants.TODAY_DT));
            inputMap.put("INITIATED_BRANCH", inputMap.get(TransactionDAOConstants.BRANCH_CODE));
            List chequeCleared = (List) sqlMap.executeQueryForList("getInstrumentClearedRule", inputMap);
            if(chequeCleared!=null && chequeCleared.size() > 0){
                HashMap clearMap = (HashMap)chequeCleared.get(0);
                
                if(CommonUtil.convertObjToStr(clearMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                    System.out.println("Instrument Cleared, Authorized...");
                    throw new ValidationRuleException(OperativeAccountConstants.CHEQUECLEARED);
                    
                }else{
                    System.out.println("Instrument sent for Clearing, Pending for Authorization...");
                    throw new ValidationRuleException(OperativeAccountConstants.CHEQUECLEARED_NA);
                }
            }
            
            /*
             * To Check, if the Cheque(s) are already stopped or not.
             */
            List chequeStopped = (List) sqlMap.executeQueryForList("getStopInstrumentNoRule", inputMap);
            
            //__ Instrument already stopped...
            if( (chequeStopped.size() > 0) ){
                HashMap stopMap = (HashMap)chequeStopped.get(0);
                final String authStatus = CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS"));
//                stopMap = null;
                if(CommonUtil.convertObjToStr(stopMap.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")){ 
                    if(authStatus.equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                        System.out.println("Cheque Stopped, Authorized...");
                        throw new ValidationRuleException(OperativeAccountConstants.CHEQUESTOPPED);

                    }else{
                        System.out.println("Cheque Stopped, Pending for Authorization...");
                        throw new ValidationRuleException(OperativeAccountConstants.CHEQUESTOPPED_NA);
                    }
                }else{
                    if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("")){
                        System.out.println("Cheque Revoked, Pending for Authorized...");
                        throw new ValidationRuleException(OperativeAccountConstants.CHEQUEREVOK_NA);
                    }else if(CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("REJECTED")){
                        System.out.println("Cheque Revoked, rejected...");
                        throw new ValidationRuleException(OperativeAccountConstants.CHEQUESTOPPED);
                    }
                }
            }else{
                System.out.println("Cheque can Be Stopped");
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTNO", "OA060961" );
        inputMap.put("INSTRUMENT1", "C25A" );
        inputMap.put("INSTRU1", "250000176" );
        inputMap.put("INSTRU2", "0" );
        
        ChequeStopPaymentRule cRule = new ChequeStopPaymentRule();
        cRule.validate(inputMap);
    }
    
}

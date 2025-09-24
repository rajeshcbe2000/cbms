/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DraftInstrumentRule.java
 *
 * Created on February 10, 2004, 5:42 PM
 */

package com.see.truetransact.businessrule.transaction;

import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
/**
 *
 * @author  rahul, bala
 *
 * DraftInstrumentRule Checks for the following:
 * a) If the Draft is Actually issued or not?
 * b) if the issued draft have already been cleared or not?
 */
public class DraftInstrumentRule extends ValidationRule{
    private SqlMap sqlMap = null;
    private final String INSTRU_TYPE = "DD";
    private final Integer LIMIT = new Integer("180");
    
    /** Creates a new instance of DraftInstrumentRule */
    public DraftInstrumentRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /** To Validate the Condition depending on the Value Passed in the HashMap from the DAO.*/
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
     /*
      * Method To get the Data from the Database to be Compared...
      * inputMap passed from the DAO.
      */
    
    private void getTarget(HashMap inputMap) throws Exception{
        boolean check;
        
        /*
         * The Mapped Statement "getDraftIssueNoRule" is in InwardClearingMap; and is
         * used to Know if the Issued Draft Is a valid Draft or not.
         */
        List draftIssue = (List) sqlMap.executeQueryForList("getDraftIssueNoRule", inputMap);
        
        if(draftIssue.size() > 0){
            inward(inputMap);
        }else{
            throw new ValidationRuleException(TransactionConstants.DRAFT_NOT_ISSUED);
        }
    }
    
    /**
     *To Check is the Particular Draft has Already been Cashed of Not.
     */
    private void inward(HashMap inputMap) throws Exception{
        //__ if the DD is Aissued, Check if the Data is Still valid...
        inputMap.put("CURRENT_DT", inputMap.get(TransactionDAOConstants.DATE));
        inputMap.put("LIMIT_DAYS", LIMIT);
        inputMap.put("INSTRU_TYPE", INSTRU_TYPE);
        inputMap.put("TRANS_DT", inputMap.get(TransactionDAOConstants.TODAY_DT));
        inputMap.put("INITIATED_BRANCH", inputMap.get(TransactionDAOConstants.BRANCH_CODE));
        List dateList = sqlMap.executeQueryForList("getDraftDateData",inputMap);
        
        //__ if the DD Date is Still Valid...
        if(dateList.size() > 0){
            /*
             * Mapped Statement is in InwardClearingMap; and is used
             * to Know Whether the Cheque has been Cleared Earlier or not...
             */
            // Inward Clearing
            //            List draftData = (List)sqlMap.executeQueryForList("getDraftRule", inputMap);
            //
            //            if (draftData.size() <= 0) {
            //                // Cash Transaction
            //                draftData = (List)sqlMap.executeQueryForList("Cash.getDraftRule", inputMap);
            //            }
            //
            //            if ((draftData.size() <= 0) && inputMap.containsKey("TRANSID")) {
            //                // Transfer Transaction
            //                draftData = (List)sqlMap.executeQueryForList("getTransferDraftRule", inputMap);
            //            }
            
            
            List draftData = (List)sqlMap.executeQueryForList("getInstrumentClearedRule", inputMap);
            
            //__ Instrument Cleared...
            if(draftData!=null && draftData.size() > 0){
                HashMap clearMap = (HashMap)draftData.get(0);
                
                if(CommonUtil.convertObjToStr(clearMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)){
                    System.out.println("Draft Cleared, Authorized...");
                    throw new ValidationRuleException(TransactionConstants.DRAFT_CLEARED);
                    
                }else{
                    System.out.println("Draft sent for Clearing, Pending for Authorization...");
                    throw new ValidationRuleException(TransactionConstants.DRAFT_CLEARED_NA);
                }
            }else{
                // To Know if the Instrument is Cancled.
                List cancelRule = (List)sqlMap.executeQueryForList("getDraftCancelRule",inputMap);
                
                
                //__ If the Dreaft is Canceled...
                if (cancelRule.size() > 0) {
                    throw new ValidationRuleException(TransactionConstants.DRAFT_CANCEL);
                    
                }else{
                    // To Know if the Instrument is stopped.
                    List stopRule = (List)sqlMap.executeQueryForList("getDraftStopPaymentRule",inputMap);
                    
                    if(stopRule.size() > 0){
                        throw new ValidationRuleException(TransactionConstants.DRAFT_CANCEL);
                    }
                }
            }
        }else{
            //__ Throw Exception...
            throw new ValidationRuleException(TransactionConstants.DATE_EXPIRED);
        }
        
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("INSTRUMENT1", "23" );
        inputMap.put("INSTRUMENT2", "43" );
        //inputMap.put("CURRENT_DT", ServerUtil.getCurrentDate());
        inputMap.put("LIMIT_DAYS", new Integer("180") );
        
        System.out.println("inputMap : " + inputMap);
        
        try{
            DraftInstrumentRule dRule = new DraftInstrumentRule();
            dRule.validate(inputMap);
        }catch(Exception e){
            System.out.println("Error in main...");
            e.printStackTrace();
        }
        
    }
}

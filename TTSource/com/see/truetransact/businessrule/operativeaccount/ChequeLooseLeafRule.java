/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ChequeLooseLeafRule.java
 *
 * Created on May 18, 2004, 4:10 PM
 */

package com.see.truetransact.businessrule.operativeaccount;
import java.util.HashMap;
import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.commonutil.exceptionconstants.operativeaccount.OperativeAccountConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerConstants;

/**
 *
 * @author  rahul
 *
 * ChequeLooseLeafRule Checks for the following:
 * a) if the Particular Cheque is Issued Before or not.
 *
 */
public class ChequeLooseLeafRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of ChequeLooseLeafRule */
    public ChequeLooseLeafRule()  throws Exception {
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
        /* 
         * The Mapped Statement "getChequeLooseLeafRule" is
         * in ChequeBookMap; and is used to Know if the
         * Issued Cheque Is a valid Leaf  or not...
         */
        List looseLeafRule = (List)sqlMap.executeQueryForList("getChequeLooseLeafRule",inputMap);
        List chequeIssueRule = (List)sqlMap.executeQueryForList("getChequeIssuedEarlierRule",inputMap);
        
        if (looseLeafRule.size() > 0) {
            throw new ValidationRuleException(OperativeAccountConstants.CHEQUELOOSELEAF);
        } else if (chequeIssueRule != null && chequeIssueRule.size() > 0) {
            HashMap singMap = (HashMap) chequeIssueRule.get(0);
            if (singMap != null && singMap.size() > 0 && singMap.containsKey("ACT_NUM")) {
                String accNo = CommonUtil.convertObjToStr(singMap.get("ACT_NUM"));
                String accStatus = CommonUtil.convertObjToStr(singMap.get("ACT_STATUS_ID"));
                if (accStatus != null && accStatus.equals("CLOSED")) {
                    List usedChequeList = (List) sqlMap.executeQueryForList("getUsedChequeNumbers", singMap);
                    if (usedChequeList != null && usedChequeList.size() > 0) {
                        for (int i = 0; i < usedChequeList.size(); i++) {
                            HashMap map = (HashMap) usedChequeList.get(i);
                            String chqNo = CommonUtil.convertObjToStr(map.get("CHEQUE_NO"));
                            String currChqNo = CommonUtil.convertObjToStr(inputMap.get("INSTRUMENT2"));
                            if (currChqNo.equals(chqNo)) {
                                throw new ValidationRuleException(OperativeAccountConstants.CHEQUELOOSELEAF);
                            }
                        }
                    } 
                        // To Know if the Instrument is stopped.
                        inputMap.put(TransactionDAOConstants.ACCT_NO, accNo);
                        List stopRule = (List) sqlMap.executeQueryForList("getStopInstrumentNoRule", inputMap);
                        String errMsg = "Cheque Stopped ";
                        errMsg += "\nA/c No. : " + inputMap.get(TransactionDAOConstants.ACCT_NO);
                        errMsg += "\nInstrument : " + inputMap.get(TransactionDAOConstants.INSTRUMENT_1) + inputMap.get(TransactionDAOConstants.INSTRUMENT_2);
                        //__ Cheque sent to be Stopped...
                        if (stopRule != null && stopRule.size() > 0) {
                            HashMap stopMap = (HashMap) stopRule.get(0);
                            if (CommonUtil.convertObjToStr(stopMap.get("STOP_STATUS")).equalsIgnoreCase("STOPPED")) {
                                if (CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase(CommonConstants.STATUS_AUTHORIZED)) {
                                    System.out.println("Cheque Stopped, Authorized...");
                                    throw new ValidationRuleException(OperativeAccountConstants.CHEQUELOOSELEAF, errMsg);

                                } else {
                                    System.out.println("Cheque Stopped, Pending for Authorization...");
                                    throw new ValidationRuleException(OperativeAccountConstants.CHEQUELOOSELEAF, errMsg);
                                }
                            } else {
                                if (CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("")) {
                                    System.out.println("Cheque Revoked, Pending for Authorized...");
                                    throw new ValidationRuleException(OperativeAccountConstants.CHEQUELOOSELEAF, errMsg);
                                } else if (CommonUtil.convertObjToStr(stopMap.get("AUTHORIZE_STATUS")).equalsIgnoreCase("REJECTED")) {
                                    System.out.println("Cheque Revoked, rejected...");
                                    throw new ValidationRuleException(OperativeAccountConstants.CHEQUELOOSELEAF, errMsg);
                                }
                            }
                        }
                    
                } else {
                    throw new ValidationRuleException(OperativeAccountConstants.CHEQUELOOSELEAF);
                }
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("INSTRUMENT1", "ABC" );
        inputMap.put("INSTRUMENT2", "10001" );
        
        ChequeLooseLeafRule cRule = new ChequeLooseLeafRule();
        cRule.validate(inputMap);
    }
    
}

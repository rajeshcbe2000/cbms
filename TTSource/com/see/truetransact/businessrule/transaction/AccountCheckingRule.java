/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountCheckingRule.java
 *
 * Created on December 10, 2019, 11:00 AM
 */

package com.see.truetransact.businessrule.transaction;

import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author  Rishad M P
 *
 * AccountCheckingRule Checks for the Followings:
 * a) Whats the Status of the Particular Account?
 * 
 *
 */


public class AccountCheckingRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of InwardClearingRule */
    public AccountCheckingRule() throws Exception {
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
        System.out.println("inputMap AccountCheckingRule:" + inputMap);
        double crAmount = 0;
        double drAmount = 0;
        if ((!inputMap.containsKey("AUTHORIZE_STATUS") ||CommonUtil.convertObjToStr(inputMap.get("AUTHORIZE_STATUS")).equals(""))&&!(inputMap.containsKey("SCREEN_NAME") &&inputMap.get("SCREEN_NAME").equals("CHARGESUI"))&&(((String) inputMap.get(TransactionDAOConstants.PROD_TYPE)).equalsIgnoreCase("OA") || ((String) inputMap.get(TransactionDAOConstants.PROD_TYPE)).equalsIgnoreCase("TL")
                || ((String) inputMap.get(TransactionDAOConstants.PROD_TYPE)).equalsIgnoreCase("AD") || ((String) inputMap.get(TransactionDAOConstants.PROD_TYPE)).equalsIgnoreCase("TD"))) {
            double amount = CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue();
            if (amount < 0) {
                amount = -1 * amount;
            }
            if (inputMap.get("TRANSTYPE").equals(TransactionDAOConstants.CREDIT)) {
                crAmount = amount;
            }
            if (inputMap.get("TRANSTYPE").equals(TransactionDAOConstants.DEBIT)) {
                drAmount = amount;
            }
            inputMap.put("CRAMT", crAmount);
            inputMap.put("DRAMT", drAmount);
            List list = (List) sqlMap.executeQueryForList("getAccountValidate", inputMap);
            if (list != null && list.size() > 0) {
                HashMap resultMap = (HashMap) list.get(0);
                if (resultMap != null && resultMap.containsKey("VALDATE") && CommonUtil.convertObjToInt(resultMap.get("VALDATE")) == 0) {//for avoid function error
                    throw new ValidationRuleException(TransactionConstants.ACCOUNT_DATA_MISMATCH, CommonUtil.convertObjToStr(resultMap.get("MSG")));
                }
            }
        }
    }
 

    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        AccountCheckingRule iRule = new AccountCheckingRule();
        iRule.validate(inputMap);
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitCheckingRule.java
 *
 * Created on June 14, 2005, 16:00 AM
 */

package com.see.truetransact.businessrule.generalledger;

import java.util.HashMap;
import java.util.Date;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;

import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.commonutil.CommonConstants;
import javax.swing.JOptionPane;
/**
 *
 * @author  bala
 *
 * LimitCheckingRule Checks for GL Limit
 *
 */
public class GLLimitCheckingRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of InwardClearingRule */
    public GLLimitCheckingRule() throws Exception {
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
        double amount =  CommonUtil.convertObjToDouble(inputMap.get("AMOUNT")).doubleValue();
        System.out.println("getTargetinputMap "+inputMap);
        if(amount < 0 )
            amount = -1 * amount;

        Double limit = (Double) sqlMap.executeQueryForObject("getGLLimit", inputMap);
        System.out.println("GL Limit : " + limit + " : amount : " + String.valueOf(amount));
        
        if (limit != null) {
            if (amount > limit.doubleValue()) {
                //, String.valueOf(limit)
//                throw new ValidationRuleException(TransactionConstants.LIMIT_EXCEEDS);
            JOptionPane.showMessageDialog(null,"Amount Exceeding the limit...");
            }
        }
    }

    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("ACCOUNTNO", "OA001001" );
        inputMap.put("AMOUNT", "5000");
        inputMap.put("BRANCH_CODE", "BRAN");

        GLLimitCheckingRule iRule = new GLLimitCheckingRule();
        iRule.validate(inputMap);
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DeathMarkedRule1.java
 *
 * Created on March 12, 2005, 2:58 PM
 */

package com.see.truetransact.businessrule.transaction;
import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
/**
 *
 * @author  152699
 */
public class DeathMarkedRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    /** Creates a new instance of InwardClearingRule */
    public DeathMarkedRule() throws Exception {
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    /** 
     * To Validate the Condition depending on the Value Passed in the HashMap from the DAO 
     */
    /*
     * Steps :
     * 1. Based on A/c No. get Customer ID and check with Death marking
     * 2. Based on A/c No. get Joint Customer ID and check with Death Marking
     */
    public void validate(HashMap inputMap) throws Exception {
        String prodType = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.PROD_TYPE));
        String transType = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE));
        
        // Check Owner marked for Death in Debti transaction
        List actOwner = sqlMap.executeQueryForList("getDeathMarkedCustomer" + prodType , inputMap);
        if (actOwner.size() > 0 && transType.equals(TransactionDAOConstants.DEBIT) ) {
            throw new ValidationRuleException(TransactionConstants.DEATH_MARKED, (String) actOwner.get(0));
        }
        
        // Check for Joint Account customer death Marked.
        List jointAct = sqlMap.executeQueryForList ("getDeathMarkedCustomerJoint" + prodType , inputMap);
        if (jointAct.size() > 0) {
            throw new ValidationRuleException(TransactionConstants.JOINT_DEATH_MARKED, (String) jointAct.get(0));
        }
    }

    public static void main(String[] args) {
        try {
            HashMap inputMap = new HashMap();
            inputMap.put(TransactionDAOConstants.PROD_TYPE, "OA" );
            inputMap.put(TransactionDAOConstants.TRANS_TYPE, TransactionDAOConstants.DEBIT);
            inputMap.put("ACCOUNTNO", "OA001004" );
            inputMap.put("AMOUNT", "5000");

            DeathMarkedRule iRule = new DeathMarkedRule();
            iRule.validate(inputMap);
            System.out.println (inputMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChequeBookRule.java
 *
 * Created on May 5, 2004, 6:25 PM
 */

package com.see.truetransact.businessrule.operativeaccount;
import java.util.HashMap;
import java.util.List;
import com.ibatis.db.sqlmap.SqlMap;
import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverutil.ServerConstants;

import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;


/**
 *
 * @author  rahul
 * ChequeBookRule Checks for the following:
 * a) if the Cheque Books are allowed for the particular Account No.
 *
 */
public class ChequeBookRule extends ValidationRule{
    private SqlMap sqlMap = null;
    final String NO ="N";
    
    /** Creates a new instance of ChequeBookRule */
    public ChequeBookRule()  throws Exception {
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
        
        /* The Mapped Statement "getChequeIssueNoRule" "getLooseLeafRule" and are
         * in InwardClearingMap.and are used to Know if the
         * Issued Cheque Is a valid Cheque Book/ Leaf No.
         */
        
        String prodType = CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.PROD_TYPE));
         // To Know if the Cheque Book is allowed for the Particular Account No.
        List chequeBookRule = (List)sqlMap.executeQueryForList("getChequeBookRule"+prodType,inputMap);
        final HashMap chequeBookRuleMap = (HashMap)chequeBookRule.get(0);
         if(CommonUtil.convertObjToStr(chequeBookRuleMap.get("CHK_BOOK")).equalsIgnoreCase(NO)){
             throw new ValidationRuleException(TransactionConstants.INSTRUMENT_NOT_ALLOWED);
         }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        HashMap inputMap = new HashMap();
        inputMap.put("PROD_TYPE", "AD" );
        inputMap.put("ACCOUNTNO", "LA00000000001063" );
        
        ChequeBookRule cRule = new ChequeBookRule();
        cRule.validate(inputMap);
    }
    
}

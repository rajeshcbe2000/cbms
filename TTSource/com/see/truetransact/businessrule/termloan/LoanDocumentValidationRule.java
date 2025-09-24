/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanDocumentValidationRule.java
 *
 * Created on July 15, 2005, 11:18 AM
 */

package com.see.truetransact.businessrule.termloan;

import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.serverexception.ServiceLocatorException;
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;

/**
 *
 * @author  152713
 */
public class LoanDocumentValidationRule extends ValidationRule{
    private SqlMap sqlMap = null;
    
    private final String DOCUMENT_COMPLETE = "DOCUMENT_COMPLETE";
    private final String YES = "Y";
    
    /** Creates a new instance of LoanDocumentValidationRule */
    public LoanDocumentValidationRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(CommonConstants.BRANCH_ID));

        getTarget(inputMap);
    }
    
    private void getTarget(HashMap inputMap) throws Exception{
        List keyList = sqlMap.executeQueryForList("getDocumentStatusTL", inputMap);
        System.out.println ("LoanDocumentValidationRule : " + keyList);
        if (keyList.size() > 0){
            HashMap keyMap = (HashMap) keyList.get(0);
            String strDocStatus = CommonUtil.convertObjToStr(keyMap.get(DOCUMENT_COMPLETE));
            System.out.println("checkstatus"+strDocStatus);
            if (!strDocStatus.equals(YES)){
                throw new ValidationRuleException(TermLoanConstants.DOC_INCOMPLETE);
            }
        //}else{
            //throw new ValidationRuleException(TermLoanConstants.DOC_INCOMPLETE);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception{
        // TODO code application logic here
        HashMap map = new HashMap();
        map.put(TransactionDAOConstants.ACCT_NO, "LA00000000001065");
        LoanDocumentValidationRule rule = new LoanDocumentValidationRule();
        rule.validate(map);
    }
    
}

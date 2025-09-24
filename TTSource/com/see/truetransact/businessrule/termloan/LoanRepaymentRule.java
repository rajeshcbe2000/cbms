/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanRepaymentRule.java
 *
 * Created on March 29, 2005, 11:03 AM
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
import com.see.truetransact.serverside.transaction.common.TransactionDAOConstants;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;
/**
 *
 * @author  152713
 */
public class LoanRepaymentRule extends ValidationRule{
    private SqlMap sqlMap = null;
    /** Creates a new instance of LoanRepaymentRule */
    public LoanRepaymentRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        getTarget(inputMap);
    }
    
    /**
     * Method To get the Data from the Database to be Compared
     * The inputMap is passed from the DAO.
     */
    private void getTarget(HashMap inputMap) throws Exception{
        if(CommonUtil.convertObjToStr(inputMap.get(TransactionDAOConstants.TRANS_TYPE)).equalsIgnoreCase(TransactionDAOConstants.CREDIT)) {
            /*
             * The Mapped Statement "getLoansRepaymentScheduleCount" is used to get the no. of
             * Repayment Schedule using LOANS_REPAY_SCHEDULE
             */
            List list = (List) sqlMap.executeQueryForList("getLoansRepaymentScheduleCount", inputMap);
            HashMap resultMap = (HashMap)list.get(0);
            
            int scheduleCount = CommonUtil.convertObjToInt(resultMap.get("NO_SCHEDULE"));
            
            if (scheduleCount < 1){
                throw new ValidationRuleException(TermLoanConstants.NO_SCHEDULE);
            }
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}

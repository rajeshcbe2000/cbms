/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSecurityDetailsValidationRule.java
 *
 * Created on May 20, 2005, 12:32 PM
 */

package com.see.truetransact.businessrule.termloan.agritermloan;

import java.util.HashMap;
import java.util.List;

import com.ibatis.db.sqlmap.SqlMap;

import com.see.truetransact.businessrule.ValidationRule;
import com.see.truetransact.businessrule.validateexception.ValidationRuleException;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.serverutil.ServerUtil;
import com.see.truetransact.servicelocator.ServiceLocator;
import com.see.truetransact.serverexception.ServiceLocatorException;

/**
 *
 * @author  152713
 */
public class AgriSecurityDetailsValidationRule extends ValidationRule{
    private SqlMap sqlMap = null;
    private final String BEHAVES_LIKE = "BEHAVES_LIKE";
    private final String CC = "CC";
    private final String ELIGIBLE_LOAN_AMOUNT = "ELIGIBLE_LOAN_AMOUNT";
    private final String LOANS_AGAINST_DEPOSITS = "LOANS_AGAINST_DEPOSITS";
    private final String OD = "OD";
    
    /** Creates a new instance of SecurityDetailsValidationRule */
    public AgriSecurityDetailsValidationRule() throws Exception{
        final ServiceLocator locate = ServiceLocator.getInstance();
        sqlMap = (SqlMap) locate.getDAOSqlMap();
    }
    
    public void validate(HashMap inputMap) throws Exception {
        super._branchCode = CommonUtil.convertObjToStr(inputMap.get(CommonConstants.BRANCH_ID));

        getTarget(inputMap);
    }
    /**
     * Method To get the Data from the Database to be Compared
     * The inputMap is passed from the DAO.
     */
    private void getTarget(HashMap inputMap) throws Exception{
        String strBehavesLike = CommonUtil.convertObjToStr(inputMap.get(BEHAVES_LIKE));
        int limitAmount = CommonUtil.convertObjToInt(inputMap.get("LIMIT"));
        int eligible_Loan_Amt = 0;
        if (strBehavesLike.length() <= 0){
            throw new ValidationRuleException(TermLoanConstants.NOT_VALID_PROD_ID);
        }
        if (!strBehavesLike.equals(LOANS_AGAINST_DEPOSITS)){
            /*
             * The Mapped Statement "getLoanSecurity_Eligible_Loan_Amt" is used 
             * to get the eligible loan amount
             * using LOANS_SECURITY_DETAILS
             */
            List list = (List) sqlMap.executeQueryForList("getAgriLoanSecurity_Eligible_Loan_Amt", inputMap);
            if (list.size() > 0){
                HashMap resultMap = (HashMap)list.get(0);

                eligible_Loan_Amt = CommonUtil.convertObjToInt(resultMap.get(ELIGIBLE_LOAN_AMOUNT));
                                
                resultMap = null;
            }else{
                throw new ValidationRuleException(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS);
            }
            list = null;
        }else if (strBehavesLike.equals(LOANS_AGAINST_DEPOSITS)){
            /*
             * The Mapped Statement "getDepositLienAmount" is used to get the deposit Lien amount
             *  using DEPOSIT_LIEN
             */
//            List list = (List) sqlMap.executeQueryForList("getDepositLienAmount", inputMap);
//            if (list.size() > 0){
//                HashMap resultMap = (HashMap)list.get(0);
//
//                eligible_Loan_Amt = CommonUtil.convertObjToInt(resultMap.get(ELIGIBLE_LOAN_AMOUNT));
//                
//                resultMap = null;
//            }else{
//                throw new ValidationRuleException(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS);
//            }
//            list = null;
        }
        
        if (!(eligible_Loan_Amt >= limitAmount && eligible_Loan_Amt > 0)){
            throw new ValidationRuleException(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS);
        }
    }
        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}

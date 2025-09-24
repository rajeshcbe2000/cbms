/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanRuleHashMap.java
 *
 * Created on March 7, 2005, 3:01 PM
 */

package com.see.truetransact.clientutil.exceptionhashmap.termloan;

import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;

/**
 *
 * @author  Shanmugavel
 */
public class TermLoanRuleHashMap implements ExceptionHashMap{
    private HashMap exceptionMap;
    
    /** Creates a new instance of TermLoanRuleHashMap */
    public TermLoanRuleHashMap() {
        exceptionMap = new HashMap();
        
        // Limit Checking uses this
        exceptionMap.put(TermLoanConstants.EXCEED_LIMIT, "Available Balance is less than the input Amount");

        // Disbursement Checking 
        exceptionMap.put(TermLoanConstants.MULTIPLE_DISBURSEMENT, "Multiple Disbursement is not allowed for this Account");
        exceptionMap.put(TermLoanConstants.DISBURSEMENT_DATE_EXPIRED, "Disbursement is not allowed after Repayment Date");
        exceptionMap.put(TermLoanConstants.NO_SCHEDULE, "Repayment Schedule is not prepared");
        
        // Repayment Checking
        exceptionMap.put(TermLoanConstants.NO_INTEREST_DETAILS, "No interest details for this Account");
        
        // Product ID validation
        exceptionMap.put(TermLoanConstants.NOT_VALID_PROD_ID, "Invalid Product ID");
        
        // Security Details Validation
        exceptionMap.put(TermLoanConstants.INSUFFICIENT_SECURITY_DETAILS, "Insufficient Security Details");
    }
    
    public HashMap getExceptionHashMap() {
        return this.exceptionMap;
    }
    
}

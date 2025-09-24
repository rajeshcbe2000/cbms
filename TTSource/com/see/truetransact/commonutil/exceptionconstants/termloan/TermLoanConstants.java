/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TermLoanConstants.java
 *
 * Created on March 10, 2005, 10:57 AM
 */

package com.see.truetransact.commonutil.exceptionconstants.termloan;

/**
 *
 * @author  152713
 */
public interface TermLoanConstants {
    // Limit Checking rule uses this constants
    public final String EXCEED_LIMIT = "EL";
     public final String EXCEED_SUB_LIMIT = "ESL";
    
    // Disbursement Checking
    public final String DISBURSEMENT_DATE_EXPIRED = "DDE";
    public final String MULTIPLE_DISBURSEMENT = "MD";
    public final String NO_SCHEDULE = "NS";
    
    // Repayment Checking
    public final String NO_INTEREST_DETAILS = "NID";
    
    // Product ID validation
    public final String NOT_VALID_PROD_ID = "NVPID";
    
    // Security Details Validation
    public final String INSUFFICIENT_SECURITY_DETAILS = "ISD";
    
    // Document is not Completed
    public final String DOC_INCOMPLETE = "SUSPECIOUS_DIC";
    
    //loan Date is Future Date
    public final String FUTURE_DATE="FUTURE_DATE";
}


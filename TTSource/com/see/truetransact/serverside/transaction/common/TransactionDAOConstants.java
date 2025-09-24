/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransactionConstants.java
 *
 * Created on June 21, 2004, 2:14 PM
 */
package com.see.truetransact.serverside.transaction.common;

/**
 *
 * @author bala
 */
public interface TransactionDAOConstants {
    // Hash Map Keys

    public static final String ACCT_NO = "ACCOUNTNO";
    public static final String AMT = "AMOUNT";
    public static final String INSTRUMENT_1 = "INSTRUMENT1";
    public static final String INSTRUMENT_2 = "INSTRUMENT2";
    public static final String INSTRUMENT_TYPE = "INSTRUMENTTYPE";
    public static final String DATE = "DATE";
    public static final String TRANS_TYPE = "TRANSTYPE";
    public static final String TRANS_MODE = "TRANSMODE";
    public static final String UNCLEAR_AMT = "UNCLEAR_AMT";
    public static final String CHARGES = "CHARGES";
    // Other Constants
    public static final String AUTHORIZE_STATUS = "AUTHORIZE_STATUS";
    public static final String AUTHORIZE_BY = "AUTHORIZE_BY";
    public static final String TO_STATUS = "TO_STATUS";
    public static final String OLDAMT = "OLDAMOUNT";
    public static final String TRANS_ID = "TRANS_ID";
    public static final String BRANCH_CODE = "BRANCH_CODE";
    public static final String PROD_ID = "PRODUCT ID";
    public static final String PROD_TYPE = "PROD_TYPE";
    public static final String TODAY_DT = "TODAY_DT";
    public static final String INITIATED_BRANCH = "INITIATED_BRANCH";
    public static final String PARTICULARS = "PARTICULARS";
    public static final String BATCH_ID = "BATCH_ID";
    // Transaction Rule Map (which is in client side to show the messages)
    public static final String TRANS_RULE_MAP =
            "com.see.truetransact.clientutil.exceptionhashmap.transaction.TransactionRuleHashMap";
    // TermLoan Rule Map (which is in client side to show the messages)
    public static final String TERM_LOAN_RULE_MAP =
            "com.see.truetransact.clientutil.exceptionhashmap.termloan.TermLoanRuleHashMap";
    //Instrument Type
    public static final String DD = "DD";
    public static final String CHEQUE = "CHEQUE";
    public static final String WITHDRAWLSLIP = "WITHDRAW_SLIP";
    public static final String ECS = "ECS";
    //Transaction Type
    public static final String DEBIT = "DEBIT";
    public static final String CREDIT = "CREDIT";
    //Transaction Mode
    public static final String CASH = "CASH";
    public static final String TRANSFER = "TRANSFER";
    public static final String CLEARING = "CLEARING";
}

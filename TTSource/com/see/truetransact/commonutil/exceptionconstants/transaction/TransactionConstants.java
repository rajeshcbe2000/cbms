/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TransactionConstants.java
 *
 * Created on March 1, 2004, 10:54 AM
 */

package com.see.truetransact.commonutil.exceptionconstants.transaction;

/**
 *
 * @author  rahul
 */
public interface TransactionConstants {
    //__ Instrument Related Constants (Cheque)
    public final String INSTRUMENT_NOT_ISSUED = "INI"; 
    public final String INSTRUMENT_CLEARED = "IC"; 
    public final String INSTRUMENT_CLEARED_NA = "ICNA"; 
    public final String INSTRUMENT_STOP_PAY = "ISP"; 
    public final String INSTRUMENT_STOP_PAY_NA = "ISPNA";
    public final String INSTRUMENT_REVOK_NA = "ISRNA";
    public final String INSTRUMENT_NOT_ALLOWED = "INA";
    public final String INSTRUMENT_ECS_STOP_PAY = "ESP"; 
    public final String INSTRUMENT_ECS_STOP_PAY_NA = "ESPNA";
     

    //__ DateCheckingRule
    public final String DATE_EXPIRED = "DE";
    public final String DATE_FUTURE = "DF";
    public final String DATE_LT_ACCT_OPEN = "DLA";

    // Draft related constants
    public final String DRAFT_NOT_ISSUED = "DNI";
    public final String DRAFT_CLEARED = "DCR";
    public final String DRAFT_CLEARED_NA = "DCRNA";
    public final String DRAFT_CANCEL = "DC";
     public final String DRAFT_STOP = "DS";
    
    //__ Limit Checking rule uses this constants
    public final String COMP_FREEZE = "CF";
    public final String INSUFFICIENT_BALANCE = "IB";
    public final String INSUFFICIENT_CLEARBALANCE = "ICB";
    public final String CREDIT_FREEZE = "CRF";
    public final String DEBIT_FREEZE = "DBF";
    public final String BOTH_CR_DT = "CDF";
    
    public final String BATCH_TALLY = "BT";
    
    //__ Used in inward clearing
    public final String TALLY_COUNT = "TC";
    
    //__ Suspecious Config rule uses this constants
    public final String COUNT_EXCEEDS = "SUSPECIOUS_CE";
    public final String WORTH_EXCEEDS = "SUSPECIOUS_WE";
    
    //__ Death Marked Rule
    public final String JOINT_DEATH_MARKED = "JDM";
    public final String DEATH_MARKED = "DM";
    
    //__ Withdrawal Slip rule uses this constants
    public final String WITHDRAWAL_SLIP_NOT_ALLOWED = "WSNA";
    public final String AMOUNT_EXCEEDS = "AE";
    
    //__ Confirm Thanx Rule rule uses this constants
    public final String THNX_NOT_RECEIVED = "SUSPECIOUS_TNR";
    
    //__ Address Verification rule uses this constants
    public final String ADDR_NOT_VERIFIED = "SUSPECIOUS_ANV";
    
    //__ Introducer Verification rule uses this constants
    public final String INTRO_NOT_VERIFIED = "SUSPECIOUS_INV";

    //__ Min Balance check rule uses this constants
    public final String MIN_BALANCE  = "MIN";    
    
    // GL Limit Checking rule uses this constants
    public final String LIMIT_EXCEEDS  = "LE";   
    //Account validation of TL,AD,TD,SB
    public final String ACCOUNT_DATA_MISMATCH = "ADM";
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * TransactionRuleHashMap.java
 *
 * Created on March 1, 2004, 10:56 AM
 */

package com.see.truetransact.clientutil.exceptionhashmap.transaction;

import java.util.HashMap;

import com.see.truetransact.clientutil.exceptionhashmap.ExceptionHashMap;
import com.see.truetransact.commonutil.exceptionconstants.transaction.TransactionConstants;
import com.see.truetransact.commonutil.exceptionconstants.termloan.TermLoanConstants;
import com.see.truetransact.commonutil.exceptionconstants.advances.AdvancesConstants;
/**
 *
 * @author  rahul
 */
public class TransactionRuleHashMap implements ExceptionHashMap {
    private HashMap exceptionMap;
    
    /** Creates a new instance of TransactionRuleHashMap */
    public TransactionRuleHashMap() {
        exceptionMap = new HashMap();
        
        //__ Instrument Related Constants (Cheque)
        exceptionMap.put(TransactionConstants.INSTRUMENT_NOT_ISSUED, "This Instrument is not issued/valid.");
        exceptionMap.put(TransactionConstants.INSTRUMENT_CLEARED, "This Instrument is already cleared.");
        exceptionMap.put(TransactionConstants.INSTRUMENT_CLEARED_NA, "Instrument already passed and pending for Authorization" + "\n" +
                         "Verify the Instrument No");
        exceptionMap.put(TransactionConstants.INSTRUMENT_STOP_PAY, "Stop Payment applied for this Instrument.");
        exceptionMap.put(TransactionConstants.INSTRUMENT_STOP_PAY_NA, "Instrument Sent for Stop Payment" + "\n" +
                         "Pending for Authorization");
        exceptionMap.put(TransactionConstants.INSTRUMENT_NOT_ALLOWED, "Cheques are not allowed for this account.");
        exceptionMap.put(TransactionConstants.INSTRUMENT_REVOK_NA, "Instrument Revoked" + "\n" +
                         "Pending for Authorization");
         exceptionMap.put(TransactionConstants.INSTRUMENT_ECS_STOP_PAY, "Stop Payment applied for this Ecs.");
        exceptionMap.put(TransactionConstants.INSTRUMENT_ECS_STOP_PAY_NA, "Ecs Sent for Stop Payment" + "\n" +
                         "Pending for Authorization");
       
        //__ DateCheckingRule
        exceptionMap.put(TransactionConstants.DATE_EXPIRED, "Instrument is expired. It is not valid.");
        exceptionMap.put(TransactionConstants.DATE_FUTURE, "Post Dated Instrument .");
        exceptionMap.put(TransactionConstants.DATE_LT_ACCT_OPEN, "Instrument Date is lesser than Account Opening Date.");

        //__ Draft related constants
        exceptionMap.put(TransactionConstants.DRAFT_NOT_ISSUED, "Draft is not issued/valid.");
        exceptionMap.put(TransactionConstants.DRAFT_CLEARED, "Draft is already cleared.");
        exceptionMap.put(TransactionConstants.DRAFT_CLEARED_NA, "Draft Sent for Clearing" + "\n" +
                         "Pending for Authorization"); 
        exceptionMap.put(TransactionConstants.DRAFT_CANCEL, "Draft is cancelled.");
        exceptionMap.put(TransactionConstants.DRAFT_STOP, "Draft is Stopped.");
        
        
        //__ Limit Checking uses this
        exceptionMap.put(TransactionConstants.COMP_FREEZE, "Account is Freezed, cannot process the request");
        exceptionMap.put(TransactionConstants.INSUFFICIENT_BALANCE, "Available Balance is less than the input Amount");
        exceptionMap.put(TransactionConstants.INSUFFICIENT_CLEARBALANCE, "Withdraw Not Allowed, Account Is Going To Negative Balance");
        
        exceptionMap.put(TransactionConstants.CREDIT_FREEZE, "Credit Transaction is Freezed, cannot process the request");
        exceptionMap.put(TransactionConstants.DEBIT_FREEZE, "Debit Transaction is Freezed, cannot process the request");
        exceptionMap.put(TransactionConstants.BOTH_CR_DT, "Credit/Debit Transactions is Freezed, cannot process the request");

        exceptionMap.put(TransactionConstants.BATCH_TALLY, "Batch not Tallied.Cannot proceed with Authorization!!!");
        
        //__ Used in inward clearing
        exceptionMap.put(TransactionConstants.TALLY_COUNT, "Booked Instruments or Total Amount of Booked Instruments Cannot Exceed \n" +
                                                "Total No Of Instruments or Total Amount.");
        
        //__ Used in Scecious Config
        exceptionMap.put(TransactionConstants.WORTH_EXCEEDS, "<html><B>Suspecious Activity Tracking :</B> Worth of Total Transaction(s) Exceeds the Specified Limit</html>");
        exceptionMap.put(TransactionConstants.COUNT_EXCEEDS, "<html><B>Suspecious Activity Tracking :</B> Transaction Count Exceeds the Specified Limit</html>");
        
        // Used in Death Marking
        exceptionMap.put(TransactionConstants.DEATH_MARKED, "Owner of the Account is Death marked.");
        exceptionMap.put(TransactionConstants.JOINT_DEATH_MARKED, "Customer is death marked.");
        
        
        //__ Used in Withdrawal Slip
        exceptionMap.put(TransactionConstants.WITHDRAWAL_SLIP_NOT_ALLOWED, "Withdrawal Slip(s) are not allowed for the selected Account No.");
        exceptionMap.put(TransactionConstants.AMOUNT_EXCEEDS, "Withdrawal Amount Exceeds the Specified Limit");
        
        //__ Used in Confirm Thanx Rule
        exceptionMap.put(TransactionConstants.THNX_NOT_RECEIVED, "Confirmation for the Thanks Letter not received yet.");
        
        //__ Used in Address Verification Rule
        exceptionMap.put(TransactionConstants.ADDR_NOT_VERIFIED, "Address of the Customer not Verified yet.");
        
        //__ Used in Introducer Verification Rule
        exceptionMap.put(TransactionConstants.INTRO_NOT_VERIFIED, "Selected Customer cannot Introduce a new Customer.");

        //__ Used in Limit Checking Rule
        exceptionMap.put(TransactionConstants.MIN_BALANCE, "Account should have the Minimum Balance.");
        
        // Loan
        exceptionMap.put(TermLoanConstants.DOC_INCOMPLETE, "Document is incomplete for this Account No.");
        
        // Advances
        // Limit Checking uses this
        exceptionMap.put(TermLoanConstants.EXCEED_LIMIT, "Input Amount exceeds the limit");

        // Disbursement Checking 
        exceptionMap.put(TermLoanConstants.MULTIPLE_DISBURSEMENT, "Multiple Disbursement is not allowed for this Account");
        exceptionMap.put(TermLoanConstants.DISBURSEMENT_DATE_EXPIRED, "Disbursement is not allowed after Repayment Date");
        exceptionMap.put(TermLoanConstants.NO_SCHEDULE, "Repayment Schedule is not prepared");
        
        // Limit Checking uses this
        exceptionMap.put(AdvancesConstants.ADVANCES_EXCEED_LIMIT, "Available Balance/Drawing Power is less than the input Amount");
        
        // GL Limit Checking
        exceptionMap.put(TransactionConstants.LIMIT_EXCEEDS, "Input Amount exceeds the limit");
        //Dont allow the transaction for future date for termloan and advances
        exceptionMap.put(TermLoanConstants.FUTURE_DATE, "Loan Starting Date is in Future");
        
        //Dont allow the transaction for SUBLIMT  for termloan and advances
        exceptionMap.put(TermLoanConstants.EXCEED_SUB_LIMIT, "Loan Exceeding Sub Limit");     
        // For Account Rule Checking added by rishad 12/12/2019
        exceptionMap.put(TransactionConstants.ACCOUNT_DATA_MISMATCH, "Account Data Mismatch");
        
    }
    public HashMap getExceptionHashMap(){
        return this.exceptionMap;
    }
    
}

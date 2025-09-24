/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NomineeRB.java
 * 
 * Created on Fri Dec 24 10:13:55 IST 2004
 */
package com.see.truetransact.ui.transaction.common;

import java.util.ListResourceBundle;

public class TransDetailsRB extends ListResourceBundle {

    public TransDetailsRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"ProductCurrency", "Product Currency"},
        {"Branch", "Branch"},
        {"CustId", "CustId"} ,
        {"MemberNo", "MemberNo"} ,
        {"ModeofOperation", "Mode of Operation"},
        {"Constitution", "Constitution"},
        {"BalanceType", "Balance Type"},
        {"acctHeadType", "AcctHead Type"},
        {"Category", "Category"},
        {"OpeningDate", "Opening Date"},
        {"MaturityDate", "Maturity Date"},
        {"intPayFreq", "Payment Freq"},
        {"LastIntApplDt", "Last int Appl Date"},
        {"ExpiryDt", "Expiry Date"},
        {"Remarks", "Remarks"},
        {"Display", "Display"},
        {"Value", "Value"},
        {"AvailableBalance", "Available Balance"},
        {"AvailableSubsidy", "Available Subsidy"},
        {"WaivedPenal", "Total Waived Penal"},
        {"WaivedInterest", "Total Waived Interest"},
        {"penalWaivedDate", "Penal Waived Dt"},
        {"uptoRebatePaidDt", "Rebate PaidDt Upto"},
        {"paidRebateInterest", "Rebate Paid Interest"},
        {"ClearBalance", "Clear Balance"},
        {"UnclearBalance", "Unclear Balance"},
        {"TotalBalance", "Total Balance"},
        {"ShadowCreditTitle", "Shadow Credit :: Total Amount "},
        {"ShadowDebitTitle", "Shadow Debit :: Total Amount "},
        {"UnclearBalanceTitle", "Unclear Balance :: Total Amount "},
        {"Freeze", "Freeze Amount"},
        {"Lien", "Lien Amount"},
        {"LoanBalance", "Principal Amount"}, // Previous value is "Balance Loan Amount", changed by Rajesh
        {"DrawingPower", "Drawing Power Amount"},
        {"Limit", "Limit Amount"},
        {"Sub Limit", "Sub Limit"},
        {"ShadowCredit", "Shadow Credit"},
        {"ShadowDebit", "Shadow Debit"},
        {"CurrAmt", "Interest Due"},//Curr Month Int
        {"Currprince", "Principal Due"},//Curr Month Principle/
        {"penalAmt", "Penal Interest"},
        {"overDuePrince", "Other Charges Due"},
        {"overDueInt", "Over Due InterestAmt"},
        {"prevBalance", "Prev Balance Principal"},
        {"prevInterest", "prev Interest"},
        //deposits interest for cash Transaction 
        {"LastInt", "Last Int Appl Date"},
        {"InterestAmt", "Interest Amount"},
        {"Lien Amt", "Lien Amount"},
        {"POSTAGE CHARGES", "Postage Charges"},
        {"ADVERTISE CHARGES", "Advertise Charges"},
        {"NOTICE CHARGES", "Notice Charges"},
        {"MISCELLANEOUS CHARGES", "Miscellaneous Charges"},
        {"LEGAL CHARGES", "Legal Charges"},
        {"INSURANCE CHARGES", "Insurance Charges"},
        {"EXECUTION DECREE CHARGES", "Execution Decree Charges"},
        {"EA COST", "EA Cost"},
        {"EA EXPENCE", "EA Expence"},
        {"ARC_COST", "ARC Cost"},
        {"ARC EXPENCE", "ARC Expence"},
        {"EP_COST", "EP Cost"},
        {"EP EXPENCE", "EP Expence"},
        {"ARBITRARY CHARGES", "Arbitrary Charges"},
        {"RECOVERY CHARGES", "Recovery Charges"},
        {"MEASUREMENT CHARGES", "Measurement Charges"},
        {"KOLEFIELD EXPENSE", "KoleField Expense"},
        {"KOLEFIELD OPERATION", "KoleField Operation Charges"},
        {"Actual Delay", "Installments Pending"},
        {"Total Delayed Month", "Total No of Months Delay"},
        {"Paid Month", "Penalty Paid for no of months delay"},
        {"Paid Amount", "Penalty amount already paid for delayed Month"},
        {"Delayed Month", "Pending No of Months Delay"},
        {"Delayed Amount", "Pending Penalty for Delayed Months"},
        {"flexiDeposit", "Flexi Deposit Amount"},
        {"flexiLienDeposit", "Flexi Lien Amount"},
        {"todAmount", "Tod Amount"},
        {"todUtilized", "Tod Utilized"},
        {"Status", "Status"},
        {"Cust Status", "Cust Status"},
        {"Guardian Name", "Guardian Name"},
        {"Guardian Relationship", "Guardian Relationship"},
        {"Total Recievable", "Total Recievable"},
        {"Account Type", "Account Type"},
        {"Unused Cheque Count","Unused Cheque Count"},
        {"Loan Closing Amount", "Loan Closing Amount"},
        {"Total(IntDue+Penal)","Total(IntDue+Penal)"},
        {"OtherInterestAmt", "Other Bank Interest"},
        {"OtherPenalAmt", "Other Bank Penal"},
        {"OtherChargeAmt", "Other Bank Charge"},
        {"InterestPeriod", "Interest Period"}
    };
}

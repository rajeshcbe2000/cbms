/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingRB.java
 *
 * Created on Wed Jul 21 17:24:31 GMT+05:30 2004
 */

package com.see.truetransact.ui.transaction.clearing;

import java.util.ListResourceBundle;

public class InwardClearingRB extends ListResourceBundle {
    public InwardClearingRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnView", "View"},
        {"panClearingData", ""},
        {"btnSave", ""},
        {"lblCovtAmount", "Converted Amount"},
        {"btnCancel", ""},
        {"lblTransactionId_2", "null"},
        {"lblInstrumentTypeID", "Instrument Type"},
        {"lblOpenDate", "Opening Date"},
        {"btnAuthorize", ""},
        {"lblTotalAmt_2", "null"},
        {"lblMsg", ""},
        {"lblAccountNumberName", ""},
        {"lblModeOper", "Mode of Operation"},
        {"lblCurrency", "Currency"},
        {"lblInstrumentNumber", "Instrument Number"},
        {"panProdData", ""},
        {"lblProdCurrency", "Product Currency"},
        {"lblRemarks", "Remarks"},
        {"lblPayeeName", "Payee Name"},
        {"panProductData", ""},
        {"btnAccountNumber", ""},
        {"lblCategory", "Category"},
        {"btnException", ""},
        {"lblConstitution", "Constitution"},
        {"lblRemarks_2", ""},
        {"lblTotalBalance", "Total Balance"},
        {"lblAccountHeadProdId", ""},
        {"lblTransactionDate_2", "null"},
        {"lblBookedAmount_2", "null"},
        {"lblStatus", "                      "},
        {"lblAccountHeadDesc", ""},
        {"lblSpace4", "     "},
        {"lblSpace3", "     "},
        {"lblAvailBalance", "Available Balance"},
        {"lblSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"lblAccountNumber", "Account Number"},
        {"panAmounts", ""},
        {"lblTotalAmt", "Total Amount"},
        {"lblTotalInstrument", "Physical Count"},
        {"btnShadowCredit", ""},
        {"lblProductType", "Product Type"},
        {"btnPrint", ""},
        {"btnShadowDebit", ""},
        {"lblAmount", "Amount"},
        {"lblProdId", "Product Id"},
        {"lblTotalInstrument_2", "null"},
        {"btnDelete", ""},
        {"lblClearingType", "Clearing Type"},
        {"lblBookedInstrument", "Booked Instrument"},
        {"lblClearBalance", "Clear Balance"},
        {"btnNew", ""},
        {"lblBankCode", "Bank Code"},
        {"btnReject", ""},
        {"panAcctData", ""},
        {"lblClearingDate", "Clearing Date"},
        {"lblBookedInstrument_2", "null"},
        {"lblAccountHead", "Account Head"},
        {"lblScheduleNumber", "Schedule Number"},
        {"lblTransactionDate", "Transaction Date"},
        {"lblBookedAmount", "Booked Amount"},
        {"lblShadowDebit", "Shadow Debit"},
        {"lblProdCurrency_2", ""},
        {"lblShadowCredit", "Shadow Credit"},
        {"lblClearingDate_2", "null"},
        {"lblInstrumentDate", "Instrument Date"},
        {"btnEdit", ""},
        {"lblBranchCode", "Branch Code"},
        {"lblTransactionId", "Transaction Id"},
        {"EXCEPTION", "Exception"},
        {"BOUNCE", "Bounce"},
        {"TITLE", "Authorization/Bounce"},
        {"Note", "Note"},
        {"BOUNCEREASON", "Enter the Reason For the Bounce: "},
        {"BOUNCECLEARINGTYPE", "Enter the Clearing Type For the Bounce: "},
        {"B_C_TYPE", "Bounce Clearing Type:"},
        {"CANCEL", "Cancel"},
        {"ACTION_SELECTION", "Please select the Action to be performed."},
        {"ACTION_TITLE", "Action Selection"},
        {"POST", "Post"},
        {"OK", "Ok"},
        {"COUNTWARNING", "Service Count is Less Than Physical Count"},
        {"BOUNCETYPE", "No Data Available for the Bounce Clearing Type."},
        {"BOOKEDWARNING", "Booked Instrument cannot Exceeds the Total no. of Instruments."},
        {"TOTALINSTRU", "Booked Instruments Under the Selected Schedule No. Cannot Exceed Total Instruments."},
        {"TOTALAMT", "Booked Amount Under the Selected Schedule No. Cannot Exceed Total Amount."},
        {"REMARK"," remark"},        
        {"existScheduleNo","Inward Clearing Tally is not added for this Clearing Type"},
        {"CONTINUE", "Continue"}
        
    };
    
}

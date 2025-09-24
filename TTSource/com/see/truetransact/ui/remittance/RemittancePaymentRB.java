/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RemittancePaymentRB.java
 */
package com.see.truetransact.ui.remittance;

import java.util.ListResourceBundle;

public class RemittancePaymentRB extends ListResourceBundle {
    public RemittancePaymentRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"panInstrumentDetails", "Instrument Details"},
        {"btnClose", ""},
        {"btnSave", ""},
        {"lblRemitPayId", "Remit PayId"},
        {"btnCancel", ""},
        {"btnCreditHead", ""},
        {"lblDateIssue", "Date of Issue"},
        {"lblPayDate", "Payment Date"},
        {"lblSerialNumber", "Serial Number"},
        {"btnPrint", ""},
        {"btnDebitHead", ""},
        {"lblMsg", ""},
        {"lblCharges", "Charges"},
        {"lblServiceTax", "Service Tax"},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"lblAddress", "Address"},
        {"lblRemarks", "Remarks"},
        {"lblFavouring", "Favouring"},
        {"lblInstrumentType", "Product ID"},
        {"lblAccHeadBal", "Act. Head Balance"},
        {"lblAccHead", "Account Head"},
        {"lblInstrumentNumber", "Instrument Number"},
        {"lblPayStatus", "Pay Status"},
        {"lblIssueCode", "-"},
        {"panOtherDetails", "Other Details"},
        {"lblPayAmount", "Issue Amount"},
        {"lblPayableAmount", "Amount Payable"},
        {"lblStatus", "                      "},
        {"btnEdit", ""},
        {"lblIssue", "Issue Bank - Branch Code"},
        {"lblSpace3", " Status :"},
        {"lblSpace2", "     "},
        {"lblSpace1", "     "},
        {"TOCommandError", "TO Status Command is null"},
        {"cDialogOK", "OK"},
        { "warningMsg", "The Instrument is stale cannot be paid Unless revalidated" },
        { "paidMsg" , "The Instrument with this SerialNumber and InstrumentType is already Paid"},
        { "issuedMsg", "The Instruemnt with this SerialNumber and InstrumentType is not Issued."},
        {"printedNumberMsg", "Duplicate Issued On "}
  };

}

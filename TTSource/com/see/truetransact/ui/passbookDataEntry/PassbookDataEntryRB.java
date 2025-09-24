/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PassbookDataEntryRB.java
 * 
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.passbookDataEntry;

import java.util.ListResourceBundle;

public class PassbookDataEntryRB extends ListResourceBundle {

    public PassbookDataEntryRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnClose", ""},
        {"lblBankHead", "Bank Head"},
        {"lblBranchHead", "Branch Head"},
        {"lblStatus", "                      "},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblStatus", ""},
        {"lblSpace", ""},
        {"lblSpace2", ""},
        {"lblSpace3", ""},
        {"lblSpace1", ""},
        {"lblDate", "Transaction Date"},
        {"lblTransactionType", "Transaction Type"},
        {"rdoTransactionType_Debit", "Payment"},
        {"btnSave", ""},
        {"rdoTransactionType_Credit", "Receipt"},
        {"lblSpace", " Status :"},
        {"lblTransactionID", "Transaction ID"},
        {"lblInstrumentType", "Instrument Type"},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"lblInstrumentNo", "Instrument No."},
        {"lblInstrumentDate", "Instrument Date"},
        {"lblParticulars", "Particulars"},
        {"lblAmount", "Amount"},
        {"lblBalance", "Final Balance"},
        {"lblTotalAmount", "Balance"}
    };
}

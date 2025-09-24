/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFTransferRB.java
 * 
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.pftransfer;

import java.util.ListResourceBundle;

public class PFTransferRB extends ListResourceBundle {

    public PFTransferRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnSearchPfNo", ""},
        {"lblPfNo", "PF No."},
        {"lblStatus1", "                      "},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblBalance", "Balance"},
        {"lblAmount", "Amount"},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"txtPfNo", ""},
        {"txtBalance", ""},
        {"txtAmount", ""},
        {"rdoTransactionType_Debit", "Payment"},
        {"rdoTransactionType_Credit", "Receipt"},
        {"lblTransactionType", "Transaction Type"}
    };
}

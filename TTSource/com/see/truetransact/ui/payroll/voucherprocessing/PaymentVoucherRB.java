/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaymentVoucherRB.java
 * 
 * Created on Wed Feb 02 12:50:41 IST 2015
 */
package com.see.truetransact.ui.payroll.voucherprocessing;

import java.util.ListResourceBundle;

public class PaymentVoucherRB extends ListResourceBundle {

    public PaymentVoucherRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblApptDate", "Appointed Date"},
        {"lblName", "Name"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblStatus1", "                      "},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblNameValue", ""},
        {"btnPrint", ""},
        {"lblRemarks", "Remarks"},
        {"txtCurDate", "Current Date"},
        {"cboMonth", "Month"},
        {"cboYear", "Year"},};
}

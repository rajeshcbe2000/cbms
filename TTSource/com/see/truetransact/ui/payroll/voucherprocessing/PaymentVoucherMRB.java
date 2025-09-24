/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PaymentVoucherMRB.java
 * 
 * Created on Wed Feb 02 12:57:26 IST 2015
 */
package com.see.truetransact.ui.payroll.voucherprocessing;

import java.util.ListResourceBundle;

public class PaymentVoucherMRB extends ListResourceBundle {

    public PaymentVoucherMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtCurDate", "Current Date should not be empty!!!"},
        {"cboMonth", "Month should not be empty!!!"},
        {"cboYear", "Year should not be empty!!!"}
    };
}

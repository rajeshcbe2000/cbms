/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.indend.reserve;

import java.util.ListResourceBundle;

public class ReserveMRB extends ListResourceBundle {

    public ReserveMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtDepoID", "Depo ID should not be empty!!!"},
        {"tdtClosingDt", "Closing Date should be a proper value!!!"},
        {"txtClosingAmount", "Closing Amount should be a proper value!!!"},
        {"txtClosingPerLessAmt", "Closing Per Less Amount should be a proper value!!!"},
        {"cboStockType", "Close Type should be a proper value!!!"},
        {"cboClosingStockType", "Closing Stock Type should be a proper value!!!"},};
}

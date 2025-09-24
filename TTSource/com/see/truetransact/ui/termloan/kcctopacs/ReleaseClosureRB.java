/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SanctionMasterRB.java
 * 
 * Created on Fri Feb 15 13:08:56 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.ListResourceBundle;

public class ReleaseClosureRB extends ListResourceBundle {

    public ReleaseClosureRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"lblKCCProdId", "Product ID"},
        {"lblAccNo", "Account No."},
        {"lblAccNameValue", "Account Name"},
        {"lblFromReleaseNo", "From Release No."},
        {"lblToReleaseNo", "To Release No."},
        {"btnSearch", "Search"},};
}

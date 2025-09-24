/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SanctionMasterMRB.java
 * 
 * Created on Fri Feb 15 13:44:39 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.ListResourceBundle;

public class ReleaseClosureMRB extends ListResourceBundle {

    public ReleaseClosureMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"cboKCCProdId", "Prod Id should not be empty!!!"},
        {"txtAccNo", "Account Number should not be empty!!!"},
        {"txtFromReleaseNo", "From Release Number should not be empty!!!"},
        {"txtToReleaseNo", "To Release Number should not be empty!!!"},};
}

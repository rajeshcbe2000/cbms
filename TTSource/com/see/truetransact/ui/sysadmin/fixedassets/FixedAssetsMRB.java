/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 *
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.ListResourceBundle;
public class FixedAssetsMRB extends ListResourceBundle {
    public FixedAssetsMRB(){
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtProvision","Enter Fixed Asset Depreciation%"},
        {"txtAssetDesc", "Fixed Asset Description should be a proper value!!!"},
        {"txtAssetType", "Fixed Asset Type should be a proper value!!!"},
        {"txtpurchaseDebit","Purchase Debit account head should be proper value"},
        {"txtProvDebit", "Depreciation Debit head should be proper value"},
        {"txtSellAcHdID", "Selling  account head should be proper value"},
        {"txtNullifyingDebit","Excess Debit account head should be proper value"},
        {"txtNullifyingCredit", "Excess Credit account head should be proper value"},
        {"txtCurValRoundOff", "Enter Current Value Round Off"}
    };
}

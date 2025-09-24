/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.sysadmin.fixedassets;
import java.util.ListResourceBundle;
public class FixedAssetsRB extends ListResourceBundle {
    public FixedAssetsRB(){
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"lblAssetType","Fixed Asset Type"},
        {"lblAssetDesc","Fixed Asset Description"},
        {"lblProvision","Fixed Asset Depreciation %"},
        {"lblpurchaseDebit","Purchase Debit"},
        {"lblProvDebit","Depreciation Debit"},
        {"lblSellAcHdID","Selling Ac Hd Id"},
        {"lblNullifyingDebit", "Excess Debit"},
        {"lblNullifyingCredit", "Excess Credit"},
        {"lblDepre","Depreciation"},
        {"lblRoundOffType", "Round Off Type"},
        {"lblCurValRoundOff", "Current Value Round Off"}
    };
}

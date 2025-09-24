/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigMRB.java
 * 
 * Created on Mon Jan 31 15:42:25 IST 2005
 */

package com.see.truetransact.ui.trading.tradingpurchase;

import java.util.ListResourceBundle;

public class TradingPurchaseMRB extends ListResourceBundle {
    public TradingPurchaseMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtTdsId", "TdsId should not be empty!!!"},
        {"txtCutOfAmount", "CutOfAmount should not be empty!!!"},
        {"tdtEndDate", "EndDate should not be empty!!!"},
        {"txtPercentage", "Percentage should not be empty!!!"},
        {"tdtStartDate", "StartDate should not be empty!!!"},
        {"cboScope", "Scope should be a proper value!!!"},
         {"cboCustTypeVal", "CustomerType should be a proper value!!!"},
        {"txtTdsCreditAchdIdVal", "Tds Cr Account Head  not be empty!!!"},
        {"rdoCutOff_Yes", "IncludeCutOff should be selected!!!"}
        
   };
}

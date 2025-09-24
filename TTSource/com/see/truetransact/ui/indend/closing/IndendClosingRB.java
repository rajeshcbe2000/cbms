/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.indend.closing;
import java.util.ListResourceBundle;
public class IndendClosingRB extends ListResourceBundle {
    public IndendClosingRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblDepoID", "Depo ID"},
        {"lblAcctName", "Account Name"},
        {"lblStatusBy", "Status By"},
        {"lblClosingPerLessAmt", "Closing Per Less Amount"},
        {"lblClosingAmount", "Closing Amount"},
        {"lblClosingStockType", "Closing Stock Type"},
        {"lblStockType", "Stock Type"},
        {"lblClosingDt","Closing Date"},
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigMRB.java
 * 
 * Created on Thu Jan 20 16:39:25 IST 2005
 */

package com.see.truetransact.ui.batchprocess.yearendprocess;

import java.util.ListResourceBundle;

public class YearEndProcessMRB extends ListResourceBundle {
    public YearEndProcessMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtProfitAcHead", "Enter/Select Profit Account Head"},
        {"txtLossAcHead", "Enter/Select Loss Account Head"},
        {"txtProfitLoss","Shows Profit/Loss"}

   };

}

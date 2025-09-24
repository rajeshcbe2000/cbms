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

package com.see.truetransact.ui.locker.lockeroperation;

import java.util.ListResourceBundle;

public class LockerOperationMRB extends ListResourceBundle {
    public LockerOperationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtNoOfTokens", "Displays Number of Tokens Configured"},
        {"cboTokenType", "TokenType should be a proper value!!!"},
        {"txtSeriesNo", "SeriesNo should not be empty!!!"},
        {"txtEndingTokenNo", "EndingTokenNo should not be empty!!!"},
        {"txtStartingTokenNo", "StartingTokenNo should not be empty!!!"} ,
        {"txtNumber","Starting Token Number Should Be Less Than Ending Token Number"}

   };

}

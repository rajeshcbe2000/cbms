/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexExchangeMRB.java
 * 
 * Created on Wed May 05 12:41:14 IST 2004
 */

package com.see.truetransact.ui.forex;

import java.util.ListResourceBundle;

public class ForexExchangeMRB extends ListResourceBundle {
    public ForexExchangeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtMiddleRate", "MiddleRate should not be empty!!!"},
        {"tdtValidDate", "ValidDate should not be empty!!!"},
        {"cboHours", "Hours should be a proper value!!!"},
        {"cboTransCurrency", "TransCurrency should be a proper value!!!"},
        {"cboMinutes", "Minutes should be a proper value!!!"} 

   };

}

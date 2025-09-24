/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesMRB.java
 * 
 * Created on Thu Dec 23 12:39:04 IST 2004
 */

package com.see.truetransact.ui.common.charges;

import java.util.ListResourceBundle;

public class ChargesMRB extends ListResourceBundle {
    public ChargesMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboRateType", "RateType should be a proper value!!!"},
        {"txtFixRate", "FixRate should not be empty!!!"},
        {"cboChargeType", "ChargeType should be a proper value!!!"},
        {"txtFromAmt", "FromAmt should not be empty!!!"},
        {"txtRateVal", "RateVal should not be empty!!!"},
        {"txtForEvery", "ForEvery should not be empty!!!"},
        {"txtToAmt", "ToAmt should not be empty!!!"},
        {"txtPercent", "Percent should not be empty!!!"} 

   };

}

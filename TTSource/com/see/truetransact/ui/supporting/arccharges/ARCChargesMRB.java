/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ChargesMRB.java
 *
 * Created on Thu Dec 23 12:39:04 IST 2004
 */

package com.see.truetransact.ui.supporting.arccharges;

import java.util.ListResourceBundle;

public class ARCChargesMRB extends ListResourceBundle {
    public ARCChargesMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboChargeBase", "ChargeBase should be a proper value!!!"},
        {"cboRoundOffType", "RoundOffType should be a proper value!!!"},
        {"cboSchemeId", "SchemeId should be a proper value!!!"},
        {"txtToSlabAmount", "ToSlab Amount should not be empty!!!"},
        {"txtFlatCharges", "Flat Charges should not be empty!!!"},
        {"txtDivisibleBy", "DivisibleBy should not be empty!!!"},
        {"txtChargeId", "ChargeId should not be empty!!!"},
        {"txtChargeDesc", "Charge Description should not be empty!!!"},
        {"txtFromSlabAmount", "FromSlab Amount should not be empty!!!"},
        {"txtMinimumChrgAmt", "Minimum Charge Amount should not be empty!!!"},
        {"txtMaximumChrgAmt", "Maximum Charge Amount should not be empty!!!"},
        {"txtChargeRate", "Charge Rate should not be empty!!!"},
        {"txtAccGroupId", "A/c GroupId should not be empty!!!"}
    };
    
}

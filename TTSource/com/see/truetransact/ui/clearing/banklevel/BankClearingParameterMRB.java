/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterMRB.java
 *
 * Created on Tue Mar 16 17:57:56 PST 2004
 */

package com.see.truetransact.ui.clearing.banklevel;

import java.util.ListResourceBundle;

public class BankClearingParameterMRB extends ListResourceBundle {
    public BankClearingParameterMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtICReturnChargesHD", "ICReturnChargesHD should not be empty!!!"},
        {"txtICReturnCharges", "ICReturnCharges should not be empty!!!"},
        {"txtClearingSuspenseHD", "ClearingSuspenseHD should not be empty!!!"},
        {"txtOCInstrumentChargesHD", "Outward Clearing collect charges head should not be empty!!!"},
         {"txtOCInstrumentCharges", "Outward Clearing collect charges should not be empty!!!"},
        {"txtOCReturnCharges", "OCReturnCharges should not be empty!!!"},
        {"txtOCReturnChargesHD", "OCReturnChargesHD should not be empty!!!"},
        {"txtClearingHD", "ClearingHD should not be empty!!!"},
        {"txtClearingType", "ClearingType should not be empty!!!"},
        {"txtShortClaimAcHead", "ShortClaimAccountHead should not be empty!!!"},
        {"txtExcessClaimAcHead", "ExcessClaimAccountHead should not be empty!!!"},
        {"rdoCompleteDay_Yes", "CompleteDay should be selected!!!"},
        
    };
    
}

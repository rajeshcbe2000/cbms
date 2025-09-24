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

package com.see.truetransact.ui.clearing;

import java.util.ListResourceBundle;

public class ParameterMRB extends ListResourceBundle {
    public ParameterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtServiceBranchCode", "ServiceBranchCode should not be empty!!!"},
        {"txtLotSize", "LotSize should not be empty!!!"},
        {"txtICReturnChargesHD", "ICReturnChargesHD should not be empty!!!"},
        {"txtICReturnCharges", "ICReturnCharges should not be empty!!!"},
        {"txtClearingSuspenseHD", "ClearingSuspenseHD should not be empty!!!"},
        {"rdoHighValue_Yes", "HighValue should be selected!!!"},
        {"txtOCReturnCharges", "OCReturnCharges should not be empty!!!"},
        {"txtOCReturnChargesHD", "OCReturnChargesHD should not be empty!!!"},
        {"txtValueofHighValueCheque", "ValueofHighValueCheque should not be empty!!!"},
        {"txtClearingHD", "ClearingHD should not be empty!!!"},
        {"txtClearingFreq", "ClearingFreq should not be empty!!!"},
        {"cboClearingFreq", "ClearingFreq should not be empty!!!"},
        {"cboClearingType", "ClearingType should not be empty!!!"} 
   };

}

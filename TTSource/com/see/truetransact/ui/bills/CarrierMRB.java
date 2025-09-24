/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CarrierMRB.java
 * 
 * Created on Wed Jan 05 14:42:23 IST 2005
 */

package com.see.truetransact.ui.bills;

import java.util.ListResourceBundle;

public class CarrierMRB extends ListResourceBundle {
    public CarrierMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtCarrierCode", "CarrierCode should not be empty!!!"},
        {"txtAddress", "Address should not be empty!!!"},
        {"txtCarrierName", "CarrierName should not be empty!!!"},
        {"chkIsApproved", "IsApproved should be selected!!!"},
        {"cboCountry", "Country should be a proper value!!!"},
        {"cboCarrierType", "CarrierType should be a proper value!!!"},
        {"txtPincode", "Pincode should not be empty!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"cboCity", "City should be a proper value!!!"} 

   };

}

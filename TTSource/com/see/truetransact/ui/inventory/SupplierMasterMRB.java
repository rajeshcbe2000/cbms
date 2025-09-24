/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterMRB.java
 * 
 * Created on Fri Jun 10 15:40:25 IST 2011
 */

package com.see.truetransact.ui.inventory;

import java.util.ListResourceBundle;

public class SupplierMasterMRB extends ListResourceBundle {
    public SupplierMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtSupplierName", "SupplierName should not be empty!!!"},
        {"txtAddrRemarks", "AddrRemarks should not be empty!!!"},
        {"cboCountry", "Country should be a proper value!!!"},
        {"txtPincode", "Pincode should not be empty!!!"},
        {"txtStreet", "Street should not be empty!!!"},
        {"txtSupplierID", "SupplierID should not be empty!!!"},
        {"txtTinNo", "TinNo should not be empty!!!"},
        {"txtArea", "Area should not be empty!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"txtCST", "CST should not be empty!!!"},
        {"cboCity", "City should be a proper value!!!"} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankMRB.java
 * 
 * Created on Thu Dec 30 16:04:32 IST 2004
 */

package com.see.truetransact.ui.sysadmin.otherbank;

import java.util.ListResourceBundle;

public class OtherBankMRB extends ListResourceBundle {
    public OtherBankMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtBranchName", "Name should not be empty!!!"},
        {"txtOtherBranchCode", "OtherBranchCode should not be empty!!!"},
        {"txtBankShortName", "BankShortName should not be empty!!!"},
        {"txtBankName", "BankName should not be empty!!!"},
        {"txtAddress", "Address should not be empty!!!"},
        {"txtMICR", "MICR should not be empty!!!"},
        {"cboCountry", "Country should be a proper value!!!"},
        {"txtBankCode", "BankCode should not be empty!!!"},
        {"txtPincode", "Pincode should not be empty!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"txtOtherBranchShortName", "OtherBranchShortName should not be empty!!!"},
        {"cboCity", "City should be a proper value!!!"} 

   };

}

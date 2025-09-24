/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankRB.java
 * 
 * Created on Thu Dec 30 14:39:01 IST 2004
 */

package com.see.truetransact.ui.sysadmin.otherbank;

import java.util.ListResourceBundle;

public class OtherBankRB extends ListResourceBundle {
    public OtherBankRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblBankName", "Bank Name"},
        {"btnClose", ""},
        {"lblCity", "City"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblPincode", "Pincode"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"btnOtherBankSave", "Save"},
        {"lblOtherBranchShortName", "Branch Short Name"},
        {"lblProductType", "Product Type"},
        {"lblProdId", "Product Id"},
        {"btnOtherBankNew", "New"},
        {"lblSpace1", " Status :"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblHVCClearing","High Value Clearing"},
        {"lblBankType","Designated Branch"},
        {"lblPhoneNo","PboneNo"},
        {"btnOtherBankDelete", "Delete"},
        {"lblBranchName", "Branch Name"},
        {"lblOtherBranchCode", "Branch Code"},
        {"lblCountry", "Country"},
        {"btnReject", ""},
        {"panOtherBank", "Other Bank"},
        {"btnEdit", ""},
        {"lblBankShortName", "Bank Short Name"},
        {"btnNew", ""},
        {"lblBankCode", "Bank Code"},
        {"btnCancel", ""},
        {"lblAddress", "Address"},
        {"lblMICR", "MICR"},
        {"lblState", "State"},
        {"panOtherBankBranch", "Other Bank Branch"},
        {"btnPrint", ""},
        
        {"tblColumn1", "SI No"},
        {"tblColumn2", "Prod Type"},
        {"tblColumn3", "Branch Code"},
        {"tblColumn4", "Branch Short Name"},
        {"radio_hvc_yes","Y"},
        {"radio_hvc_no","N"},
        {"radio_db_yes","Y"},
        {"radio_db_no","N"},
        
        {"BankCodeCount", "This Bank Code Already Exist !!!"}

   };

}

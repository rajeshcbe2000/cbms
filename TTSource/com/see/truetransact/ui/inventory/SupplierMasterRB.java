/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterRB.java
 * 
 * Created on Fri Jun 10 13:48:57 IST 2011
 */

package com.see.truetransact.ui.inventory;

import java.util.ListResourceBundle;

public class SupplierMasterRB extends ListResourceBundle {
    public SupplierMasterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblStreet", "House No./Street"},
        {"lblCity", "City"},
        {"lblTinNo", "Tin No."},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblPincode", "Pincode"},
        {"lblSupplierName", "Supplier Name"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblCST", "CST No."},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnView", ""},
        {"lblArea", "Area"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblCountry", "Country"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblSupplierID", "Supplier ID"},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblState", "State"},
        {"lblAddrRemarks", "Remarks"},
        {"btnPrint", ""} 

   };

}

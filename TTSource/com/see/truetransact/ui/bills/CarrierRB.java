/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CarrierRB.java
 *
 * Created on Wed Jan 05 14:36:49 IST 2005
 */

package com.see.truetransact.ui.bills;

import java.util.ListResourceBundle;

public class CarrierRB extends ListResourceBundle {
    public CarrierRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"lblCity", "City"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblIsApproved", "Is Approved"},
        {"lblPincode", "Pincode"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblCarrierName", "Name"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblCountry", "Country"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblCarrierType", "Carrier Type"},
        {"lblCarrierCode", "Carrier Code"},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblAddress", "Address"},
        {"lblState", "State"},
        {"chkIsApproved", ""},
        {"btnPrint", ""},
        {"TOCommandError", ""},
        {"CarrierCodeCount", "This Carrier Code Already Exist !!!"}
        
    };
    
}

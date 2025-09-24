/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexDenominationMasterRB.java
 * 
 * Created on Thu Jan 27 12:05:49 IST 2005
 */

package com.see.truetransact.ui.sysadmin.denomination;

import java.util.ListResourceBundle;

public class ForexDenominationMasterRB extends ListResourceBundle {
    public ForexDenominationMasterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblType", "Type"},
        {"btnReject", ""},
        {"lblDenominationName", "Denomination Name"},
//        {"btnEdit", ""},
        {"btnAuthorize", ""},
        {"lblCurrency", "Currency"},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblDenominationValue", "Denomination Value"},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        
        {"TOCommandError", ""},
        {"warning", "This Record Already Exist !!! "} 

   };

}

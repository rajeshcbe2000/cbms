/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LookupMasterRB.java
 */

package com.see.truetransact.ui.sysadmin.lookup;
import java.util.ListResourceBundle;
public class LookupMasterRB extends ListResourceBundle {
    public LookupMasterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"panMain", ""},
        {"btnPrint", ""},
        {"lblMsg", ""},
        {"lblLookupID", "Lookup ID"},
        {"btnLookupMasterTabDelete", "Delete"},
        {"lblStatus", "                      "},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnLookupMasterTabAdd", "Save"},
        {"btnLookupMasterTabNew", "New"},
        {"lblLookupRef", "Lookup Ref ID"},
        {"lblSpace3", " Status :"},
        {"lblSpace2", "     "},
        {"lblSpace1", "     "},
        {"lblLookupDesc", "Description"},
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        
        {"TOCommandError", ""},
        {"REFIDWARNING", "Please Select the Record to be Authorized/Rejected."},
        {"existanceWarning", "The Lookup Master Reference ID Already exist. Do you want to modify it ?"},
        

   };

}

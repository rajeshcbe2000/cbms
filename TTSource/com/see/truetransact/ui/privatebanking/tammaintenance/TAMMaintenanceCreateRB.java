/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TAMMaintenanceCreateRB.java
 * 
 * Created on Mon Jul 12 15:46:21 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.tammaintenance;

import java.util.ListResourceBundle;

public class TAMMaintenanceCreateRB extends ListResourceBundle {
    public TAMMaintenanceCreateRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblTAMDefaultType", "TAM Default Type"},
        {"btnAuthorize", ""},
        {"lblTAMStatus", "Status"},
        {"lblMsg", ""},
        {"btnException", ""},
        {"rdoTAMDefaultType_No", "No"},
        {"lblSpace4", "     "},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", " Status :"},
        {"lblStatus", "                      "},
        {"panTAMMaintenanceCreat", ""},
        {"lblSpace1", "     "},
        {"lblTAMOrderType", "TAM Order Type"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblAssetSubclassID", "Asset Subclass ID"},
        {"btnNew", ""},
        {"rdoTAMDefaultType_Yes", "Yes"},
        {"lblAssetClassID", "Asset Class ID"},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"TOCommandError", "TO Status Command is null"}
   };

}

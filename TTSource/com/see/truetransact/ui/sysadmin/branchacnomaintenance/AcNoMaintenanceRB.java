/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcNoMaintenanceRB.java
 * 
 * Created on February 18, 2009, 01:40 PM
 *
 * AUTHOR : RAJESH.S
 */

package com.see.truetransact.ui.sysadmin.branchacnomaintenance;

import java.util.ListResourceBundle;

public class AcNoMaintenanceRB extends ListResourceBundle {
    public AcNoMaintenanceRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        
        {"lblBranches", "Branch Name"},
        {"lblProdId", "Product Id"},
        {"lblLastAcNo", "Last Account No."},
        {"lblNextAcNo", "Next Account No."},
        {"lblLastAcNoDisplay", ""},
        {"lblNextAcNoDisplay", ""},
        {"btnClose", ""},
        {"panLeft", ""},
        {"lblMsg", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", " Status :"},
        {"lblStatus", "                      "},
        {"lblSpace1", "     "},
        {"btnDelete", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnPrint", ""},

        {"tblColumn1", "Product Id"},
        {"tblColumn2", "Product Description"},
        {"tblColumn3", "Last Ac No"},
        {"tblColumn4", "Next Ac No"},
        {"tblColumn5", "Group No"},
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        
        {"cDialogOk", "Ok"},
        
        {"TOCommandError", ""},
        
        {"existanceWarning", "The Name and Remarks for this Holiday Date Already exist. Do you want to modify it ?"},
        {"comboEmptyWarning", "Atleast one combo has to be filled"},
        {"comboUniqueWarning", "This data should be unique"},
        {"deleteWarning","Are you sure you want to Delete?"},
        {"todayOrPriorDateWarning","You cannot set Today or prior date as Holiday!!!" }

   };

}

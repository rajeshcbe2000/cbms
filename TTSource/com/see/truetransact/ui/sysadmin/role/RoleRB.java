/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RoleRB.java
 *
 * Created on Wed May 11 14:36:57 IST 2005
 */

package com.see.truetransact.ui.sysadmin.role;

import java.util.ListResourceBundle;

public class RoleRB extends ListResourceBundle {
    public RoleRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"panLevelDetailsForeign", "Level Details"},
        {"lblClearingDebit", "Clearing Debit"},
        {"btnClose", ""},
        {"lblRoleID", "Role ID"},
        {"lblTransDebitForeign", "Trans Debit"},
        {"lblRoleDesc", "Role Description"},
        {"lblGroupID", "Group ID"},
        {"btnSaveLevel", "Save"},
        {"btnAuthorize", ""},
        {"lblCashDebit", "Cash Debit"},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblTransCreditForeign", "Trans Credit"},
        {"lblLevelNameForeign", "Level Name"},
        {"lblTransCredit", "Trans Credit"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnLevelIDForeign", ""},
        {"lblSpace3", "     "},
        {"chkAccAllBran", ""},
        {"lblStatus", "                      "},
        {"lblGroupDesc", "Group Description"},
        {"btnDelete", ""},
        {"btnDeleteLevel", "Delete"},
        {"lblLevelIDForeign", "Level ID"},
        {"lblSpace", " Status :"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblLevelDesc", "Level Description"},
        {"lblClearingDebitForeign", "Clearing Debit"},
        {"lblTransDebit", "Trans Debit"},
        {"btnLevelID", ""},
        {"lblLevelName", "Level Name"},
        {"btnNewLevel", "New"},
        {"btnCancel", ""},
        {"lblClearingCredit", "Clearing Credit"},
        {"lblClearingCreditForeign", "Clearing Credit"},
        {"lblCashDebitForeign", "Cash Debit"},
        {"lblCashCreditForeign", "Cash Credit"},
        {"lblCashCredit", "Cash Credit"},
        {"panLevelDetails", "Level Details"},
        {"lblLevelID", "Level ID"},
        {"btnPrint", ""},
        {"lblAccAllBran", "Access All Branch"},
        {"lblLevelDescForeign", "Level Description"} ,
        
        {"tblColumn1", "Role"},
        {"tblColumn2", "Desc."},
        {"tblColumn3", "Hierarchy"},
        {"tblColumn4", "Home ID"},
        {"tblColumn5", "Foreign ID"},
        {"tblColumn6", "All"},
        {"tblColumn7", "Same Hierarchy"},
        {"tblColumn8", "Authorize Status"},
        
        {"RECORDEXIST", "Record already exists"},
        {"ROLEIDWARNING", "Please Select the Record to be Authorized/Rejected."},
        {"lblRoleHierarchy", "Role Hierarchy"},
        {"lblHierarchyAllowed", "Same Hierarchy Allowed"},
        
    };
    
}

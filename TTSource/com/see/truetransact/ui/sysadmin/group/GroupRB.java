/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * 
 *GroupRB.java
 *
 */

package com.see.truetransact.ui.sysadmin.group;

import java.util.ListResourceBundle;

/**
 *
 * @author  Pinky
 */

public class GroupRB extends ListResourceBundle {
   
    public GroupRB(){
    }    
    public Object[][] getContents() {
        return contents;
    }    
    static final String[][] contents = {
        {"btnExclude", "Exclude"},
        {"btnClose", ""},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblGrantScreen", "Granted Screens"},
        {"btnPrint", ""},
        {"lblSpace", " Status :"},
        {"btnInclude", "Include"},
        {"panGrpAccess", "Group Access"},
        {"lblMsg", ""},
        {"lblGrpID", "Group ID"},
        {"lblStatus", "                      "},
        {"btnDelete", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"lblSpace3", "     "},
        {"lblSpace2", "     "},
        {"lblAvaScreen", "Available Screens"},
        {"lblGrpDesc", "Group Description"},
        {"lblBranch", "Branch"},
        {"lblBranchGroup", "Branch Group"},
        {"chkNewAllowed", "New"},
        {"chkEditAllowed", "Edit"},
        {"chkDeleteAllowed", "Delete"},
        {"chkAuthRejAllowed", "Authorize"},
        {"chkExceptionAllowed", "Exception"},
        {"chkPrintAllowed", "Print"},
        {"chkInterbranchAllowed", "Interbranch Allowed"},
        
        {"noGrantScreen", "There should be atleast one granted screen"}      
    };    
}

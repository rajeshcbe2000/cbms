/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupRB.java
 * 
 * Created on Thu Aug 25 16:35:06 IST 2005
 */

package com.see.truetransact.ui.sysadmin.branchgroup;

import java.util.ListResourceBundle;

public class BranchGroupRB extends ListResourceBundle {
    public BranchGroupRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblSpace", " Status :"},
        {"panBranchGroup", "Branch Grouping"},
        {"btnInclude", "Include"},
        {"btnEdit", ""},
        {"lblAvailBranch", "Available Branches"},
        {"lblMsg", ""},
        {"btnNew", ""},
        {"btnExclude", "Exclude"},
        {"lblSpace2", "     "},
        {"lblGroupId", "Group Id"},
        {"btnSave", ""},
        {"lblGrantedBranch", "Granted Branches"},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblGroupName", "Group Name"},
        {"btnPrint", ""} 

   };

}

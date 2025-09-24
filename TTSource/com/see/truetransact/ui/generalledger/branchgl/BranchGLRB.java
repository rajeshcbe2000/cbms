/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGLRB.java
 * 
 * Created on Mon Dec 27 16:35:06 IST 2004
 */

package com.see.truetransact.ui.generalledger.branchgl;

import java.util.ListResourceBundle;

public class BranchGLRB extends ListResourceBundle {
    public BranchGLRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblSpace", " Status :"},
        {"panGLAccess", "GL Access"},
        {"btnInclude", "Include"},
        {"btnEdit", ""},
        {"lblAvaGL", "Available GL Heads"},
        {"lblMsg", ""},
        {"btnNew", ""},
        {"btnExclude", "Exclude"},
        {"lblSpace2", "     "},
        {"lblGroupId", "Group Id"},
        {"btnSave", ""},
        {"lblGrantGL", "Granted GL Heads"},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblGroupName", "Group Name"},
        {"btnPrint", ""} 

   };

}

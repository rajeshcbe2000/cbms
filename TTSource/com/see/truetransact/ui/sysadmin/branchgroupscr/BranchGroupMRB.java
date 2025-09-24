/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * 
 *BranchGroupMRB.java
 *
 */

package com.see.truetransact.ui.sysadmin.branchgroupscr;

import java.util.ListResourceBundle;

/**
 *
 * @author  Pinky
 */

public class BranchGroupMRB extends ListResourceBundle {
    public BranchGroupMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtGrpDesc", "GrpDesc should not be empty!!!"},
        {"txtGrpID", "GrpID is not editable!"},
        {"treAvaiScreen", "Tree of available Screen"},
        {"treGrantScreen", "Tree of granted Screen"},
    };
}

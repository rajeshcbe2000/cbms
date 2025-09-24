/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * 
 *GroupMRB.java
 *
 */

package com.see.truetransact.ui.sysadmin.group;

import java.util.ListResourceBundle;

/**
 *
 * @author  Pinky
 */

public class GroupMRB extends ListResourceBundle {
    public GroupMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtGrpDesc", "GrpDesc should not be empty!!!"},
        {"txtGrpID", "GrpID is not editable!"},
        {"treAvaiScreen", "Tree of available Screen"},
        {"treGrantScreen", "Tree of granted Screen"},
        
        {"txtBranchGroup", "Branch Group is not editable!"},
        {"chkNewAllowed", "Is New allowed?"},
        {"chkEditAllowed", "Is Edit allowed?"},
        {"chkDeleteAllowed", "Is Delete allowed?"},
        {"chkAuthRejAllowed", "Is Authorize allowed?"},
        {"chkExceptionAllowed", "Is Exception allowed?"},
        {"chkPrintAllowed", "Is Print allowed?"},
        {"chkInterbranchAllowed", "Is Interbranch Transactions allowed?"},
    };
}

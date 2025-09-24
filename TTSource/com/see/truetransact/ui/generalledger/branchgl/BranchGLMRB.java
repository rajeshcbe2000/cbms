/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGLMRB.java
 * 
 * Created on Mon Dec 27 17:06:59 IST 2004
 */

package com.see.truetransact.ui.generalledger.branchgl;

import java.util.ListResourceBundle;

public class BranchGLMRB extends ListResourceBundle {
    public BranchGLMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtGroupId", "GroupId should not be empty!!!"},
        {"txtGroupName", "GroupName should not be empty!!!"},
        {"lstAvailGL", "AvailGL should be a proper value!!!"},
        {"lstGrantGL", "GrantGL should be a proper value!!!"} 

   };

}

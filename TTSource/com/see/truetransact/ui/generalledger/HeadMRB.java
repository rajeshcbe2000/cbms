/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * HeadMRB.java
 */

package com.see.truetransact.ui.generalledger;
import java.util.ListResourceBundle;
public class HeadMRB extends ListResourceBundle {
    public HeadMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboAccountType", "Account Type should be a proper value!!!"} ,
        {"txtMajorHeadCode1", "Major Head Code should not be empty!!!"},
        {"txtMajorHeadCode2", "Major Head Code should not be empty!!!"},
        {"txtMajorHeadDesc", "Major Head Desc should not be empty!!!"},
        {"txtSubHeadCode", "SubHead Code should not be empty!!!"},
        {"txtSubHeadDesc", "SubHead Desc should not be empty!!!"},
        {"cboFinalActType", "Enter Final Account Type!!!"},
        {"cboSubAccountType", "Enter Sub Account Type!!!"},
        {"chkGLHeadConsolidated", "GL Heads to be Consolidated should be selected!!!"}
   };

}

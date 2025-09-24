/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitMRB.java
 * 
 * Created on Wed Jun 08 11:55:05 GMT+05:30 2005
 */

package com.see.truetransact.ui.rbi;

import java.util.ListResourceBundle;

public class GLLimitMRB extends ListResourceBundle {
    public GLLimitMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtLimit", "Limit should not be empty!!!"},
        {"txtAccountHead", "AccountHead should not be empty!!!"},
        {"txtGLGroup", "GLGroup should not be empty!!!"},
        {"txtAnnualLimit", "Enter the Annual Limit for the selected Account Head"},
        {"txtOverDraw", "Enter the Percentage of OverDraw for the Selected Account Head"},
        {"chkInterBranchTransAllowed", "Inter Branch Transaction Allowed should be selected!!!"},

   };

}

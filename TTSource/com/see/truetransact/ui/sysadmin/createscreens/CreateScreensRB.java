/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CreateScreensRB.java
 */

package com.see.truetransact.ui.sysadmin.createscreens;
import java.util.ListResourceBundle;
public class CreateScreensRB extends ListResourceBundle {
    public CreateScreensRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"btnEdit", ""},
        {"lblScreenName", "Screen Name"},
        {"lblSpace1", " Status :"},
        {"lblStatus1", "                      "},
        {"btnPrint", ""},
        {"lblMsg", ""},
        {"btnNew", ""}, 
        {"WARNING", "Select The Screen Name First"}

   };

}

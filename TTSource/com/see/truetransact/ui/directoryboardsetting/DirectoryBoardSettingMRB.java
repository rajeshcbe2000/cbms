/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.directoryboardsetting;

import java.util.ListResourceBundle;

public class DirectoryBoardSettingMRB extends ListResourceBundle {
    public DirectoryBoardSettingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
       
            {"txtMemno", "Membership no should be filled!!!"},
        {"txtDesig", "Designation should be filled!!!"},
        {"txtPriority", "Priority should be filled!!!"}
        
      
        
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LookupMasterMRB.java
 */

package com.see.truetransact.ui.sysadmin.lookup;
import java.util.ListResourceBundle;
public class LookupMasterMRB extends ListResourceBundle {
    public LookupMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtLookupDesc", "LookupDesc should not be empty!!!"},
        {"txtLookupRef", "LookupRef should not be empty!!!"},
        {"txtLookupID", "LookupID should not be empty!!!"} 

   };

}

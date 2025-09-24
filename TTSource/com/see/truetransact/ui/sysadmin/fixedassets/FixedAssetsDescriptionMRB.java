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

package com.see.truetransact.ui.sysadmin.fixedassets;
import java.util.ListResourceBundle;

public class FixedAssetsDescriptionMRB extends ListResourceBundle {
    public FixedAssetsDescriptionMRB(){
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"cboAssetType","Asset Type should be a Proper Value!!!!"},
        {"txtAssetSubType","Enter Asset_Sub_Type"}
    };
}

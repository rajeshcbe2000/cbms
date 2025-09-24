/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.sysadmin.fixedassets;
import java.util.ListResourceBundle;
public class FixedAssetsDescriptionRB extends ListResourceBundle {
    public FixedAssetsDescriptionRB(){
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"lblAssetType", "Asset Type"},
        {"lblFaDescID","FA Desc ID"},
        {"lblAssetSubType","Sub Type"}
    };
}

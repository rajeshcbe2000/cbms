/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TAMMaintenanceCreateMRB.java
 * 
 * Created on Mon Jul 12 15:54:05 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.tammaintenance;

import java.util.ListResourceBundle;

public class TAMMaintenanceCreateMRB extends ListResourceBundle {
    public TAMMaintenanceCreateMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboTAMStatus", "TAM Status should be a proper value!!!"},
        {"rdoTAMDefaultType_Yes", "TAM Default Type should be selected!!!"},
        {"cboAssetClassID", "Asset Class ID should be a proper value!!!"},
        {"cboTAMOrderType", "TAM Order Type should be a proper value!!!"},
        {"cboAssetSubclassID", "Asset Subclass ID should be a proper value!!!"} 

   };

}

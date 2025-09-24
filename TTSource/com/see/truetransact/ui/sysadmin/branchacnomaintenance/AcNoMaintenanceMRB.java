/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcNoMaintenanceMRB.java
 * 
 * Created on February 18, 2009, 01:40 PM
 *
 * AUTHOR : RAJESH.S
 */

package com.see.truetransact.ui.sysadmin.branchacnomaintenance;

import java.util.ListResourceBundle;

public class AcNoMaintenanceMRB extends ListResourceBundle {
    public AcNoMaintenanceMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboBranches", "Branch should be a proper value!!!"},
        {"cboProdId", "Product Id should be a proper value!!!"},
        {"txtLastAcNo", "Last Account No should not be empty!!!"},
   };

}

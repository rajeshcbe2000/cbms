/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductMRB.java
 *
 * Created on Thu Jun 24 18:03:43 GMT+05:30 2004
 */

package com.see.truetransact.ui.transaction.editMigratedData;

import java.util.ListResourceBundle;

public class EditMigratedDataMRB extends ListResourceBundle {
    public EditMigratedDataMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtCustomerIdCr", "Act/No should not be empty!!!"},
        {"cboProdId", "Product ID should be a proper value!!!"},
        {"cboProdType", "Product Type should be a proper value!!!"},
    };
}

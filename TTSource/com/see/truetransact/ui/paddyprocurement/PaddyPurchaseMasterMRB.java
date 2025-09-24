/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterMRB.java
 * 
 * Created on Fri Jun 10 15:40:25 IST 2011
 */

package com.see.truetransact.ui.paddyprocurement;

import java.util.ListResourceBundle;

public class PaddyPurchaseMasterMRB extends ListResourceBundle {
    public PaddyPurchaseMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtProductCode", "Product Code should not be empty!!!"},
        {"txtProductDesc", "Product Description should not be empty!!!"},
        {"txtRatePerKg", "Rate Per Unit should not be empty!!!"},
        {"txtKiloGram", "Kilo Gram should not be empty!!!"},
        {"txtAmount", "Amount should not be empty!!!"},
        {"txtlAcreage", "Acreage should not be empty!!!"},
        {"txtBags", "bags should not be empty!!!"},
        {"txtCnDNo", "C and D number should not be empty!!!"},
        {"txtAddress", "Address should not be empty!!!"},
        {"txtName", "Name should not be empty!!!"},
        {"txtLocalityCode", "Locality Code should not be empty!!!"},
        {"txtLocalityName", "Locality Name should not be empty!!!"},
        {"txtTotalAmount", "Total Amountshould not be empty!!!"}
   };

}

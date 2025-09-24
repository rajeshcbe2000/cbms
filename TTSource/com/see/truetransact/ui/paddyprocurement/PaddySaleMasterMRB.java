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

public class PaddySaleMasterMRB extends ListResourceBundle {
    public PaddySaleMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtBillNo", "Bill Number should not be empty!!!"},
        {"txtName", "Name should not be empty!!!"},
        {"txtProductCode", "Product Code should not be empty!!!"},
        {"txtProductDesc", "Product Description should not be empty!!!"},
        {"txtRatePerKg", "Rate Per Unit should not be empty!!!"},
        {"txtKiloGram", "Kilogram should not be empty!!!"},
        {"txtAmount", "Amount should not be empty!!!"},
        {"txtlAcreage", "Acreage should should not be empty!!!"},
        {"txtBags", "Bags should not be empty!!!"},
        {"tdtBillDate", "Bill gate should not be empty!!!"},
        {"txtAddress", "Address should not be empty!!!"},
        {"tdtSaleDate", "sale date should not be empty!!!"},
        {"txtTotalAmount", "Total Amount should not be empty!!!"}
   };

}

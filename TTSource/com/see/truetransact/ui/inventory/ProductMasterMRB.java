/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProductMasterMRB.java
 * 
 * Created on Mon Jun 20 16:50:36 GMT+05:30 2011
 */

package com.see.truetransact.ui.inventory;

import java.util.ListResourceBundle;

public class ProductMasterMRB extends ListResourceBundle {
    public ProductMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtSalesReturnAcHd", "SalesReturnAcHd should not be empty!!!"},
        {"txtProductCode", "ProductCode should not be empty!!!"},
        {"txtQty", "Qty should not be empty!!!"},
        {"txtTaxAcHd", "TaxAcHd should not be empty!!!"},
        {"txtReorderLevel", "ReorderLevel should not be empty!!!"},
        {"txtSellingPrice", "SellingPrice should not be empty!!!"},
        {"txtSalesAcHd", "SalesAcHd should not be empty!!!"},
        {"txtPurchasePrice", "PurchasePrice should not be empty!!!"},
        {"txtPurchaseReturnAcHd", "PurchaseReturnAcHd should not be empty!!!"},
        {"txtPurchaseAcHd", "PurchaseAcHd should not be empty!!!"},
        {"txtProductDesc", "ProductDesc should not be empty!!!"},
        {"cboUnit", "Unit should be a proper value!!!"} 

   };

}

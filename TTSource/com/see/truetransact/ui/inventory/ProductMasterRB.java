/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ProductMasterRB.java
 * 
 * Created on Mon Jun 20 16:18:49 GMT+05:30 2011
 */

package com.see.truetransact.ui.inventory;

import java.util.ListResourceBundle;

public class ProductMasterRB extends ListResourceBundle {
    public ProductMasterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSalesAcHd", "Sales Account Head"},
        {"btnClose", ""},
        {"lblProductDesc", "Product Description"},
        {"lblQty", "Quantity in store"},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblProductCode", "Product Code"},
        {"lblPurchaseAcHd", "Purchase Account Head"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"btnTaxAcHd", ""},
        {"btnPurchaseAcHd", ""},
        {"lblReorderLevel", "Reorder level"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnView", ""},
        {"lblSellingPrice", "Selling Price/Unit"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblUnit", "Units in"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblTaxAcHd", "Tax Account Head"},
        {"btnSalesAcHd", ""},
        {"btnNew", ""},
        {"lblPurchasePrice", "Purchase Price/Unit"},
        {"btnCancel", ""},
        {"btnPrint", ""} 

   };

}

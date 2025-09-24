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

package com.see.truetransact.ui.suspenseaccount;

import java.util.ListResourceBundle;

public class SuspenseAccountMasterRB extends ListResourceBundle {
    public SuspenseAccountMasterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSalesAcHd", "Sales Account Head"},
        {"lblSalesReturnAcHd","Sales Return Account Head"},
        {"lblItemCode", "Item Code"},
        {"lblItemDesc", "Description"},
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
        {"lblOrderLevel", "Order level"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnView", ""},
        {"lblPurchaseReturnAcHd","Purchase Return Account Head"},
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

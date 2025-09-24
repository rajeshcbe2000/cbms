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

package com.see.truetransact.ui.suspenseaccount;

import java.util.ListResourceBundle;

public class SuspenseAccountMasterMRB extends ListResourceBundle {
    public SuspenseAccountMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtItemCode", "Item Code should not be empty!!!"},
        {"txtItemDesc", "Description should not be empty!!!"},
        {"txtPurchasePrice", "Purchase Price should not be empty!!!"},
        {"txtSellingPrice", "Selling Price should not be empty!!!"},
        {"txtQty", "Quantity should not be empty!!!"},
        {"txtOrderLevel", "Order Level should not be empty!!!"},
        {"txtPurchaseAcHd", "Purchase Act Head should be a proper value!!!"},
        {"txtSalesAcHd", "Sales Act head should be a proper value!!!"},
        {"txtSalesReturnAcHd", "Sales Returns Head should be a proper value!!!"},
        {"txtTaxAcHd", "Tax Act Head should be a proper value!!!"},
        {"cboUnit", "Unit should be a proper value!!!"},
        {"txtPurchaseReturnAcHd", "Purchase Return Head should be a proper value!!!"},
        {"cboSuspenseProdID", "Prod id a proper value!!!"},
        {"tdtSuspenseOpenDate", "Opening date should be a proper value!!!"},
        {"txtMemberNumber", "MemberNo should be a proper value!!!"},
        {"txtCustomerId", "CustomerId should be a proper value!!!"},


   };

}

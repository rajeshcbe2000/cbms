/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigRB.java
 *
 * Created on Thu Jan 20 15:41:51 IST 2005
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.ListResourceBundle;

public class FixedAssetsIndividualRB extends ListResourceBundle {
    public FixedAssetsIndividualRB(){
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"lblBranchId","Branch Code"},
        {"lblTrainingID", "Asset Id"},
        {"lblAssetType", "Asset Type"},
        {"lblAssetDesc", "Asset Description"},
        {"lblQuantity", "Quantity"},
        {"lblOrdered", "Ordered Date"},
        {"lblPurchased","Purchased Date"},
        {"lblComp","Company Name"},
        {"lblWarranty","Warranty"},
        {"lblFaceVal","Face Value"},
        {"lblCurrValue","Current Value"},
        {"lblAssetNum","Asset Number"},
        {"lblInvoiceNo","Invoice No"},
        {"lblSlNo","Serial Number"},
        {"lblFloor", "Floor"},
        {"lblDepart","Department"},
        {"lblWarrantyExpired","Warranty Expiry Date"}
    };
}

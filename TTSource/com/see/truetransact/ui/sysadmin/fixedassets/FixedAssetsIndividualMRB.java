/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 *
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.ListResourceBundle;

public class FixedAssetsIndividualMRB extends ListResourceBundle {
    public FixedAssetsIndividualMRB(){
        
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"cboBranchId","Branch Id Should Not be Empty"},
        {"cboAssetType","Asset Type Should be a Proper Value"},
        {"cboAssetDesc","Asset Description Should be a Proper Value"},
        {"txtInvoiceNo","Enter Invoice Number"},
        {"txtQuantity","Enter Quantity"},
        {"tdtOrdered","Ordered Date Should Not be Empty"},
        {"tdtPurchased","Purchased Date Should Not be Empty"},
        {"txtComp","Enter Company Name"},
        {"txtWarranty","Enter Warranty"},
        {"txtFaceVal","Enter Face Value"},
        {"txtCurrValue","Enter Current Value"},
        {"txtSlNo","Enter Serial Number"},
        {"txtAssetNum","Enter Asset Number"},
        {"cboWarrPack","Warranty Time Should Not be Empty"},
        {"txtFloor", "Enter Floor Number"},
        {"cboDepart", "Department Should be a Proper Value"}
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * FixedAssetsTransMRB.java
 *
 * Created on Tue Jan 18 16:23:09 IST 2011
 */

package com.see.truetransact.ui.sysadmin.fixedassets;

import java.util.ListResourceBundle;

public class FixedAssetsTransMRB extends ListResourceBundle {
    public FixedAssetsTransMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtFromAssetId", "FromAssetId should not be empty!!!\n"},
        {"txtFaceVal", "FaceVal should not be empty!!!\n"},
        {"txtAssetIdMove", "AssetIdMove should not be empty!!!\n"},
        {"cboProductType", "ProductType should be a proper value!!!\n"},
        {"cboSubType", "SubType should be a proper value!!!\n"},
        {"cboProductTypeSale", "ProductTypeSale should be a proper value!!!\n"},
        {"txtDepart", "Depart should not be empty!!!\n"},
        {"txtCurrValue", "CurrValue should not be empty!!!\n"},
        {"txtSlNo", "SlNo should not be empty!!!\n"},
        {"txtCurrentValueSale", "CurrentValueSale should not be empty!!!\n"},
        {"txtFaceValueSale", "FaceValueSale should not be empty!!!\n"},
        {"txtFloorBreak", "FloorBreak should not be empty!!!\n"},
        {"txtBranchIdBreak", "BranchIdBreak should not be empty!!!\n"},
        {"txtDepartBreak", "DepartBreak should not be empty!!!\n"},
        
        {"cboBranchIdMove", "BranchIdMove should be a proper value!!!\n"},
        {"cboDepartMove", "DepartMove should be a proper value!!!\n"},
        {"txtSlNoBreak", "SlNoBreak should not be empty!!!\n"},
        {"txtAssetIdSale", "AssetIdSale should not be empty!!!\n"},
        {"txtFloorMove", "FloorMove should not be empty!!!\n"},
        {"txtCurrValueBreak", "CurrValueBreak should not be empty!!!\n"},
        {"txtBranchId", "BranchId should not be empty!!!\n"},
        {"txtPurchasedDate", "PurchasedDate should not be empty!!!\n"},
        {"txtAssetIdBreak", "AssetIdBreak should not be empty!!!\n"},
        {"cboSubTypeSale", "SubTypeSale should be a proper value!!!\n"},
        {"tdtDepDate", "DepDate should not be empty!!!\n"},
        {"txtFaceValBreak", "FaceValBreak should not be empty!!!\n"},
        {"txtBreakageRegion", "BreakageRegion should not be empty!!!\n"},
        {"txtFloor", "Floor should not be empty!!!\n"},
        {"txtToAssetId", "ToAssetId should not be empty!!!\n"}
        
    };
    
}

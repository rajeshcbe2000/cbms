/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterRB.java
 *
 * Created on Fri Jun 10 13:48:57 IST 2011
 */

package com.see.truetransact.ui.paddyprocurement;

import java.util.ListResourceBundle;

public class PaddyPurchaseMasterRB extends ListResourceBundle {
    public PaddyPurchaseMasterRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"deleteWarningMsg", "Are you sure you want to delete the selected row?"},
        {"cDialogYes", "Yes"},
        {"CDialogNo", "No"},
        {"btnClose", ""},
        {"lblStreet", "House No./Street"},
        {"lblCity", "City"},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblPincode", "Pincode"},
        {"lblLocalityName", "Locality Name"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnView", ""},
        {"lblArea", "Area"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblCountry", "Country"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblLocalityCode", "Locality Code"},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblState", "State"},
        {"btnPrint", ""},
        {"NoRecords", "There are no transaction records."},
        {"saveInTxDetailsTable","Save The Current Transaction Details in the Table!!!"},
        {"lblCnDNo", "C&D No."},
        {"lblName", "Name"},
        {"lblBillDate", "Date"},
        {"lblPurchaseDate","Purchase Date"},
        {"lblAddress", "Address"},
        {"lblLocalityName", "Locality Name"},
        {"lblProductCode", "Product Code"},
        {"lblProductDesc", "Product Description"},
        {"lblRatePerKg", "Rate per Unit"},
        {"lblKiloGram", "Kilo Gram"},
        {"lblBags", "Bags"},
        {"lblTotalAmount", "Total Amount"},
        {"lblAcreage", "Acreage"},
        {"lblAmount", "Amount"}
        
    };
    
}

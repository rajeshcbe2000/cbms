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

public class PaddySaleMasterRB extends ListResourceBundle {
    public PaddySaleMasterRB(){
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
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnView", ""},
        {"lblArea", "Area"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"NoRecords", "There are no transaction records."},
        {"saveInTxDetailsTable","Save The Current Transaction Details in the Table!!!"},
        {"lblBillNo", "Bill No."},
        {"lblBillDate", "Date"},
        {"lblName", "Name"},
        {"lblAddress", "Address"},
        {"lblProductCode", "Product Code"},
        {"lblProductDesc", "Product Description"},
        {"lblRatePerKg", "Rate Per Unit"},
        {"lblKiloGram", "Kilo Gram"},
        {"lblBags", "Bags"},
        {"lblAmount", "Amount"},
        {"lblSaleDate", "Sale Date"},
        {"lblAcreage", "Acreage"},
        {"lblTotalAmount", "Total Amount"}
        
    };
    
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.indend.suppliermaster;
import com.see.truetransact.ui.inwardregister.*;
import java.util.ListResourceBundle;
public class SupplierRB extends ListResourceBundle {
    public SupplierRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSupplierNo", "Supplier No"},
       // {"lblInwardNo", "Inward No"},
        {"lblAddress", "Address"},
        {"lblTinNo", "Tin No"},
        {"lblName", "Name"}
      //  {"lblDetails", "Details"},
       // {"lblRemarks", "Remarks"}
    };
}

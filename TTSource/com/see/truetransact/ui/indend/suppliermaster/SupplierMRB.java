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

package com.see.truetransact.ui.indend.suppliermaster;

import com.see.truetransact.ui.inwardregister.*;
import java.util.ListResourceBundle;

public class SupplierMRB extends ListResourceBundle {
    public SupplierMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
      //  {"tdtDate", "Inward Date should be a proper value!!!"},
        {"txtSupplierNo", "Supplier No should be a proper value!!!"},
        {"txtName", "Name should be a proper value!!!"},
        {"txtTinNo", "TIN No  should be a proper value!!!"},
     //   {"txtAddress", "Action Taken should be a proper value!!!"},
       // {"txaDetails", "Details should not be empty!!!"},
        {"txaAddress", "Address should not be empty!!!"}
        
   };

}

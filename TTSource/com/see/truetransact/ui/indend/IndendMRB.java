/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigMRB.java
 * 
 * Created on Thu Jan 20 16:39:25 IST 2005
 */

package com.see.truetransact.ui.indend;

import java.util.ListResourceBundle;

public class IndendMRB extends ListResourceBundle {
    public IndendMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboDepoId", "Depo Id should not be empty!!!\n"},
        {"tdtDateIndand", "Indend Date should not be empty!!!\n"},
        {"cboTransType", "Trans Type should not be empty!!!\n"},
        {"cboSupplier", "Supplier Name should not be empty!!!\n"},
        {"cboType", "Type should be a proper value!!!\n"},
        {"txtPurAmount", "Purchase should not be empty!!!\n"} ,
        {"txtAmount","Amount should not be empty!!!\n"}
       
                 
   };

}

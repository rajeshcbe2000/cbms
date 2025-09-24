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

public class StoreMRB extends ListResourceBundle {
    public StoreMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtStoreName", "Store name should not be empty!!!\n"},
       {"txtServiceTaxNumber", "Enter number!!!\n"},
        {"txtVatTaxNumber", "Enter number!!!\n"},
        {"txtServiceTax", "Service Tax should not be empty!!!\n"},
        {"txtVatTax", "Vat should not be empty!!!\n"}
       
       
                 
   };

}

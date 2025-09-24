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

public class SaleMRB extends ListResourceBundle {
    public SaleMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtName", "Sales Man name should not be empty!!!\n"},
        {"txaArea", "Address should not be empty!!!\n"},
        {"txtSecAmt", "Security amount should not be empty!!!\n"},
        {"txaRemarks", "Remarks should be a proper value!!!\n"},
        
                 
   };

}

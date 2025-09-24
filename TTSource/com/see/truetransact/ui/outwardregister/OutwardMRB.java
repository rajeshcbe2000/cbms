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

package com.see.truetransact.ui.outwardregister;

import java.util.ListResourceBundle;

public class OutwardMRB extends ListResourceBundle {
    public OutwardMRB(){
    }

    protected Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txaDetails", "Sent Details should contain proper value!!!"},
        {"tdtDate", "Date should be a proper value!!!"},
        {"txtReferenceNo", "Reference Number should be a proper value!!!"},
        {"txaRemarks", "Remarks should contain proper value!!!"},
        {"txtOutwardNo","Outward Number should contain proper value"},
            {"cmbMessenger","Messenger should be proper value"},
            {"txaAddress","Address should contain proper value"}
//        {"rdoAcc_Yes", "Accumaltion should not be empty!!!"},
//        {"cboTransBran", "Transferred  Branch should not be empty!!!"}
//        
   };

}

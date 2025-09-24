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
package com.see.truetransact.ui.transaction.multipleCash;

import java.util.ListResourceBundle;

public class MultipleCashTransactionMRB extends ListResourceBundle {
    public MultipleCashTransactionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtDateofComplaint", "Date  should be a proper value!!!"},
        {"txaNameAddress", "Name and Address should be a proper value!!!"},
        {"txaComments", "Comments should be a proper value!!!"}
     
        
   };

}

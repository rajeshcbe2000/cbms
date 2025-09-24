/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositClosingMRB.java
 * 
 * Created on Thu May 20 13:00:33 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.closing;

import java.util.ListResourceBundle;

public class DepositClosingMRB extends ListResourceBundle {
    public DepositClosingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtDepositNo", "DepositNo should not be empty!!!"},
        {"cboProductId", "ProductId should be a proper value!!!"} 

   };

}

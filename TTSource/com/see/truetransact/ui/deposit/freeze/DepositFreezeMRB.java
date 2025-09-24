/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositFreezeMRB.java
 * 
 * Created on Fri Jun 04 11:50:43 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.freeze;

import java.util.ListResourceBundle;

public class DepositFreezeMRB extends ListResourceBundle {
    public DepositFreezeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtDepositNo", "DepositNo should not be empty!!!"},
        {"cboSubDepositNo", "SubDepositNo should be a proper value!!!"},
        {"txtFreezeAmount", "FreezeAmount should not be empty!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"tdtFreezeDate", "FreezeDate should not be empty!!!"},
        {"cboFreezeType", "FreezeType should be a proper value!!!"},
        {"txtRemark", "Remark should not be empty!!!"} 

   };

}

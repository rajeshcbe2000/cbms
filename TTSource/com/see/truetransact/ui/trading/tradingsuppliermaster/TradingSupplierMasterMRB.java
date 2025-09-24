/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigMRB.java
 * 
 * Created on Mon Jan 31 15:42:25 IST 2005
 */

package com.see.truetransact.ui.trading.tradingsuppliermaster;

import java.util.ListResourceBundle;

public class TradingSupplierMasterMRB extends ListResourceBundle {
    public TradingSupplierMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtSupplierID", "TdsId should not be empty!!!"},
        {"txtName","Name Should not be empty"},
        {"txtAddress","Address should not be empty!!!"},
        {"txtSundryCreditors","SundryCreditors should not be empty!!!"},
        {"txtPurchase","Purchase should not be empty!!!"},
        {"tdtdDate","Date should not be empty!!!"},
        {"txtPhone","Phone should not be empty"},
        {"txtCSTNO","CSTNO should not be empty"},
        {"txtKGSTNO","KGSTNO should not be empty"},
        {"txtTinNo","TinNo should not be empty"}
            
        
   };
}

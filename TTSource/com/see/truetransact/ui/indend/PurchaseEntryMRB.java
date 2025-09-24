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

public class PurchaseEntryMRB extends ListResourceBundle {
    public PurchaseEntryMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtPurAmount", "Purchase Amount should not be empty!!!\n"},
        {"txtPurComm", "Purchase Commission should not be empty!!!\n"},
        {"txtPurchaseRet", "Purchase Return should not be empty!!!\n"},
        {"txtSundry", "Sundry Creditors should be a proper value!!!\n"},
        {"txtInvestAcHead", "Investment Ac Head should not be empty!!!\n"} ,
         
                 
   };

}

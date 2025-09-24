/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryDetailsMRB.java
 * 
 * Created on Tue Aug 24 10:48:48 IST 2004
 */

package com.see.truetransact.ui.supporting.inventory;

import java.util.ListResourceBundle;

public class InventoryDetailsMRB extends ListResourceBundle {
    public InventoryDetailsMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtBookTo", "BookTo should not be empty!!!"},
        {"txtChequeFrom", "ChequeFrom should not be empty!!!"},
        {"txtBookQuantity", "BookQuantity should not be empty!!!"},
        {"txtBookFrom", "BookFrom should not be empty!!!"},
        {"txtItemID", "ItemID should not be empty!!!"},
        {"txtChequeTo", "ChequeTo should not be empty!!!"},
        {"txtInstrumentPrefix", "InstrumentPrefix should not be empty!!!"},
        {"cboTransType", "TransType should be a proper value!!!"} 

   };

}

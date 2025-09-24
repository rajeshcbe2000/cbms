/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMasterMRB.java
 * 
 * Created on Fri Aug 20 14:17:38 IST 2004
 */

package com.see.truetransact.ui.supporting.inventory;

import java.util.ListResourceBundle;

public class InventoryMasterMRB extends ListResourceBundle {
    public InventoryMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboUsage", "Usage should be a proper value!!!"},
        {"txtReOrderLevel", "ReOrderLevel should not be empty!!!"},
        //{"txtInstrumentPrefix", "lblInstrumentPrefix should not be empty!!!"},
        {"txtDangerLevel", "DangerLevel should not be empty!!!"},
        {"txtLeavesPerBook", "LeavesPerBook should not be empty!!!"},
        {"cboItemType", "ItemType should be a proper value!!!"} 

   };

}

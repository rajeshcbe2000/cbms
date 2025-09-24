/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMasterRB.java
 * 
 * Created on Wed Sep 01 13:06:27 IST 2004
 */

package com.see.truetransact.ui.supporting.inventory;

import java.util.ListResourceBundle;

public class InventoryMasterRB extends ListResourceBundle {
    public InventoryMasterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblTransaInDesc", ""},
        {"btnClose", ""},
        {"lblItemType", "Item Type"},
        {"lblInstrumentPrefix", "Instrument Prefix No."},
        {"lblInventoryIDDesc", ""},
        {"btnAuthorize", ""},
        {"lblStatus1", "                      "},
        {"btnException", ""},
        {"lblTotalBooksDesc", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblUsage", "Usage"},
        {"lblTransaOutDesc", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblReOrderLevel", "ReOrder Level"},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblTransaOut", "Transaction Out"},
        {"lblDangerLevel", "Danger Level"},
        {"btnDelete", ""},
        {"lblTotalLeavesDesc", ""},
        {"lblTransaIn", "Transaction In"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblTotalBooks", "Total No. Of Books"},
        {"lblLeavesPerBook", "No. Of Leaves Per Book"},
        {"panAuthorization", "Pending For Authorization"}, 
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblInventoryID", "Inventory ID"},
        {"btnPrint", ""},
        {"lblTotalLeaves", "Total No. Of Leaves"}, 
        
        {"REORDER_WARNING", "Reorder Level Should be Greater than Danger Level..."}

   };

}

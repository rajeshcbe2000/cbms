/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMovementMRB.java
 * 
 * Created on Tue Jan 25 12:52:38 IST 2005
 */

package com.see.truetransact.ui.supporting.InventoryMovement;

import java.util.ListResourceBundle;

public class InventoryMovementMRB extends ListResourceBundle {
    public InventoryMovementMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtReason", "Reason should not be empty!!!"},
        {"txtEndVariableNo", "End Variable No should not be empty!!!"},
        {"txtStartNo2", "Start DD No2 should not be empty!!!"},
        {"txtEndNo1", "End DD No1 should not be empty!!!"},
        {"txtStartNo1", "Start DD No1 should not be empty!!!"},
        {"rdoDDLeaf_Single", "DD Leaf should be selected!!!"},
        {"rdoDDLeaf_Bulk", "DD Leaf should be selected!!!"},
        {"txtStartVariableNo", "Start Variable No should not be empty!!!"},
        {"cboProdId", "Product Id should be a proper value!!!"},
        {"txtEndNo2", "End DD No2 should not be empty!!!"} ,
        {"cboReason", "Reason should be a proper value!!!"},
        {"tdtDate", "Date should be a proper value!!!"}

   };

}

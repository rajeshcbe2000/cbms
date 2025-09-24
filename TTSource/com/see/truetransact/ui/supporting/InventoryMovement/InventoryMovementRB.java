/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitStopPaymeRB.java
 * 
 * Created on Tue Jan 25 09:21:16 IST 2005
 */

package com.see.truetransact.ui.supporting.InventoryMovement;

import java.util.ListResourceBundle;

public class InventoryMovementRB extends ListResourceBundle {
    public InventoryMovementRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoDDLeaf_Bulk", "Bulk"},
        {"btnClose", ""},
        {"btnAuthorize", ""},
        {"lblStatus1", "                      "},
        {"lblStartVariableNo", "Starting Variable No"},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblRemarks", "Remarks"},
        {"lblPayeeName", "Payee Name"},
        {"lblMissingDateValue", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblReason", "Reason"},
        {"lblSpace3", "     "},
        {"lblPayeeNameValue", ""},
        {"lblMissingDate", " Date"},
        {"lblSpace1", " Status :"},
        {"rdoDDLeaf_Single", "Single"},
        {"lblAmount", "DD Amount"},
        {"btnPaymentRevoke", "Payment Revoke"},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblStartNo1", "Starting  No"},
        {"lblDDLeaf", "Leaf"},
        {"btnNew", ""},
        {"lblEndNo", "Ending  No"},
        {"lblProdId", "Product ID"},
        {"panRemitStop", ""},
        {"btnCancel", ""},
        {"lblAmountValue", ""},
        {"btnPrint", ""} ,
        
        {"btnDDEndNo", ""} ,
        {"btnDDStartNo", ""} ,
        
        {"lblMissingId", "Missing ID"} ,
        {"lblMissingIdValue", ""} ,
        {"lblDDStopDate", "DD Stop Date"} ,
        {"lblDDStopDateValue", ""} ,
        
        {"END_DD_WARNING", "Enter the Starting  No first!!!"}, 
        {"DATEWARNING", "Date Cannot Be post Date!!!!"}
   };

}

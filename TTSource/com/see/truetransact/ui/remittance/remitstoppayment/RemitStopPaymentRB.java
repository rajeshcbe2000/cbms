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

package com.see.truetransact.ui.remittance.remitstoppayment;

import java.util.ListResourceBundle;

public class RemitStopPaymentRB extends ListResourceBundle {
    public RemitStopPaymentRB(){
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
        {"lblReason", "Reasons for Stop Payment"},
        {"lblRevokeReason", "Reason To Revoke Stop Payment"},
        {"lblPayeeName", "Payee Name"},
        {"lblDDDateValue", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblEndVariableNo", "Ending Variable No"},
        {"lblSpace3", "     "},
        {"lblPayeeNameValue", ""},
        {"lblDDDate", "DD/PO Date"},
        {"lblSpace1", " Status :"},
        {"rdoDDLeaf_Single", "Single"},
        {"lblAmount", "DD/PO Amount"},
        {"btnPaymentRevoke", "Stop Payment Revoke"},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblStartDDNo1", "Starting DD/PO No"},
        {"lblDDLeaf", "DD/PO Leaf"},
        {"btnNew", ""},
        {"lblEndDDNo", "Ending DD/PO No"},
        {"lblProdId", "Product ID"},
        {"panRemitStop", ""},
        {"btnCancel", ""},
        {"lblAmountValue", ""},
        {"btnPrint", ""} ,
        
        {"btnDDEndNo", ""} ,
        {"btnDDStartNo", ""} ,
        
        {"lblStopId", "DD/PO Stop ID"} ,
        {"lblStopIdValue", ""} ,
        {"lblDDStopDate", "DD/PO Stop Date"} ,
        {"lblDDRevokeDate", "DD/PO Revoke Date"} ,
        {"lblDDStopDateValue", ""} ,
        {"lblRevokeDateVal", ""} ,
        {"END_DD_WARNING", "Enter the Starting DD/PO No first!!!"},
        {"REVOKE_REMARKS", "Revoke remarks"}
   };

}

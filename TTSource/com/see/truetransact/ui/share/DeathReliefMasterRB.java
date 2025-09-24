/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterRB.java
 * 
 * Created on Fri Aug 05 12:52:47 GMT+05:30 2011
 */

package com.see.truetransact.ui.share;

import java.util.ListResourceBundle;

public class DeathReliefMasterRB extends ListResourceBundle {
    public DeathReliefMasterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDrfDetailsNew", "New"},
         {"deleteWarningMsg", "Are you sure you want to delete the selected row?"},
        {"cDialogYes", "Yes"},
        {"CDialogNo", "No"},
        {"btnClose", ""},
        {"lblDrfName", "Name"},
        {"panDrfProductDesc", "Product Description"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"lblDrfFromDt", "Installment Date"},
        {"lblPaymentHeadName", "Payment Head "},
        {"lblProductId", "Product ID"},
        {"lblSpace2", "     "},
        {"btnActHeadName", ""},
        {"btnSave", ""},
        {"btnView", ""},
        {"panDrfDetails", "Product Details"},
        {"lblSpace3", "     "},
        {"lblAccountHeadType", "(Liability GL)"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnDrfDetailsSave", "Save"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblPaymentHeadType", "(Expenditure GL)"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblPaymentAmount", "Payment Amount"},
        {"btnNew", ""},
        {"lblDrfAmount", "Amount"},
        {"btnPaymentHeadName", ""},
        {"lblActHeadName", "Account Head"},
        {"btnCancel", ""},
        {"lblSpace6", "     "},
        {"btnDrfDetailsDelete", "Delete"},
        {"btnPrint", ""} 

   };

}

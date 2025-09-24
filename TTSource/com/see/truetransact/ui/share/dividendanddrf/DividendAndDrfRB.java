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

package com.see.truetransact.ui.share.dividendanddrf;

import java.util.ListResourceBundle;

public class DividendAndDrfRB extends ListResourceBundle {
    public DividendAndDrfRB(){
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
        {"lblDrfTransAddress", "Address"},
        {"lblDrfTransAmount", "Amount"},
        {"lblSpace2", "     "},
        {"btnActHeadName", ""},
        {"btnSave", ""},
        {"btnView", ""},
        {"panDrfTransDetails", "DRF Transaction"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnDrfDetailsSave", "Save"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblDrfTransName", "Name"},
        {"lblDrfTransProdID","Product ID"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblDueAmtPayment", "Due Amount Payment"},
        {"btnNew", ""},
        {"lblDrfTransMemberNo", "Member No"},
        {"btnTransactionSave", "Save"},
        {"btnTransactionNew", "New"},
        {"btnTransactionDelete", "Delete"},
        {"lblActHeadName", "Account Head"},
        {"btnCancel", ""},
        {"lblSpace6", "     "},
        {"btnDrfDetailsDelete", "Delete"},
        {"btnPrint", ""},
        {"NoRecords", "There are no transaction records."},
        {"saveInTxDetailsTable","Save The Current Transaction Details in the Table!!!"},
        {"saveAcctDet", "Save the Share Account Details before saving the Share Account!!!"}
        

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class TodAllowedMRB extends ListResourceBundle {
    public TodAllowedMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAccountNumber", "AccountNumber should not be empty!!!"},
        {"txtTODAllowed", "TODAllowed should not be empty!!!"},
        {"dtdFromDate", "From Date Should Not Be Empty!!!!"},
        {"dtdToDate", "TO Date Should Not Be Empty!!!!"},
        {"dtdPermittedDt", "Permitted Date Should Not Be Empty!!!!"},
        {"txtTODAllowed", "Amount should not be empty!!!"} ,
        {"txtRemarks", "Remarks should not be empty!!!"} ,
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"cboProdType", "ProdType should be a proper value!!!"},
        {"rdoSingleTransaction", "TODType should be selected!!!"},
        {"cboPermitedBy", "PermitedBy should be a proper value!!!"},
        {"txtPermissionRefNo", "Permission Ref Number should not be empty!!!"} ,
        {"txtRepayedWithin", "Enter the period within which TOD is to be repaid!!!"}
   };
}

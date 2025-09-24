/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class TodAllowedRB extends ListResourceBundle {
    public TodAllowedRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblAccountNumber", "Account Number"},
        {"lblAcctName", "Account Name"},
        {"lblTODAllowed", "TOD Allowed"},
        {"lblFromDate", "From Date"},
        {"lblToDate", "To Date"},
        {"lblStatusBy", "Status By"},
        {"lblRemarks", "Remarks"},
        {"rdoSingleTransaction", "SingleTransaction"},
        {"lblTODAllowed", "TOD Amount"},
        {"lblPermitedBy", "PermitedBy"},
        {"lblPermissionRefNo", "Permission Reference No"},
        {"lblPermittedDt", "PermittedDt"},
        {"rdoRunningLimit", "RunningLimit"},
        {"lblProd", "Product ID"},
        {"lblTypeOfTOD", "Type Of TOD"},
        {"lblTransactionID", "TransactionID"},
        {"lblProdType", "ProdType"},
        {"lblCreatedDt","Created Date"},
        {"DIFFTYPEOFTOD","test"},
        {"lblrepayedDate","Repay Date"},
        {"lblrepayedWithin","To be Repayed Within"}
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueRevalidateRB.java
 * 
 * Created on Mon Aug 23 15:02:26 PDT 2004
 */

package com.see.truetransact.ui.remittance.revalidate;

import java.util.ListResourceBundle;

public class RemitIssueRevalidateRB extends ListResourceBundle {
    public RemitIssueRevalidateRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblLapsedPeriod", "Amount"},
        {"lblRevalidateRemarks", "Remarks"},
        {"lblRevalidationDate", "Revalidation Date"},
        {"lblBranchCode", "Product ID"},
        {"lblMsg", ""},
        {"lblVariableNo", "Category"},
        {"btnOk", "Ok"},
        {"lblDraweeBank", "Drawee Bank"},
        {"btnCancel", "Cancel"},
        {"lblDisplayLapsedPeriod", "Test"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblTransactionType", "Transaction Type"},
        {"lblRevalidationCharge", "Revalidation Charge"},
        {"lblDisplayForDraweeBank", ""},
        {"lblDisplayForVariableNo", ""},
        {"lblDisplayForRevalidationDate", "8/23/2004"},
        {"lblDisplayForBranchCode", ""},
        {"lblDOExpiring", "Date Of Expiring"} 

   };

}

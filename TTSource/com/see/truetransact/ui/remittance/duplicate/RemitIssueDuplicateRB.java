/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueDuplicateRB.java
 * 
 * Created on Mon Aug 23 15:28:13 PDT 2004
 */

package com.see.truetransact.ui.remittance.duplicate;

import java.util.ListResourceBundle;

public class RemitIssueDuplicateRB extends ListResourceBundle {
    public RemitIssueDuplicateRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblLapsedPeriod", "Amount"},
        {"lblBranchCode", "Product ID"},
        {"lblMsg", ""},
        {"lblVariableNo", "Category"},
        {"btnOk", "Ok"},
        {"lblDraweeBank", "Drawee Bank"},
        {"btnCancel", "Cancel"},
        {"lblDisplayForDuplicationDate", "8/23/2004"},
        {"lblDisplayLapsedPeriod", "Test"},
        {"lblStatus", "                      "},
        {"lblDuplicationDate", "Duplication Date"},
        {"lblSpace1", " Status :"},
        {"lblTransactionType", "Transaction Type"},
        {"lblRevalidationCharge", "Duplication Charge"},
        {"lblServiceTax", "Service Tax"},
        {"lblDuplicateRemarks", "Remarks"},
        {"lblDisplayForDraweeBank", ""},
        {"lblDisplayForVariableNo", ""},
        {"lblDisplayForBranchCode", ""} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueCancellationRB.java
 * 
 * Created on Thu Jun 10 16:15:49 PDT 2004
 */

package com.see.truetransact.ui.remittance.cancellation;

import java.util.ListResourceBundle;

public class RemitIssueCancellationRB extends ListResourceBundle {
    public RemitIssueCancellationRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblVariableNo", "Variable No"},
        {"btnOk", "Ok"},
        {"lblDraweeBank", "Drawee Bank"},
        {"lblCancellationCharge", "Cancellation Charge"},
        {"btnCancel", "Cancel"},
        {"lblStatus", "                      "},
        {"lblDisplayForCancellationDate", "Test"},
        {"lblSpace1", " Status :"},
        {"lblCancellationRemarks", "Remarks"},
        {"lblBranchCode", "Branch Code"},
        {"lblMsg", ""},
        {"lblCancellationDate", "Cancellation Date"},
        {"lblDisplayForVariableNo", "Test"},
        {"lblDisplayForDraweeBank", "Test"},
        {"lblDisplayForBranchCode", "Test"} 

   };

}

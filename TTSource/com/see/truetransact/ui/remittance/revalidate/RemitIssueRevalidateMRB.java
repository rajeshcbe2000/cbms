/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueRevalidateMRB.java
 * 
 * Created on Mon Jun 07 12:38:15 PDT 2004
 */

package com.see.truetransact.ui.remittance.revalidate;

import java.util.ListResourceBundle;

public class RemitIssueRevalidateMRB extends ListResourceBundle {
    public RemitIssueRevalidateMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboTransactionType", "TransactionType should be a proper value!!!"},
        {"txtRevalidateRemarks", "RevalidateRemarks should not be empty!!!"},
        {"txtRevalidationCharge", "RevalidationCharge should not be empty!!!"},
        {"tdtDOExpiring", "DOExpiring should not be empty!!!"} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueDuplicateMRB.java
 * 
 * Created on Mon Jun 07 12:40:23 PDT 2004
 */

package com.see.truetransact.ui.locker.lockeroperation.pwdchange;

import java.util.ListResourceBundle;

public class PwdChangeMRB extends ListResourceBundle {
    public PwdChangeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboTransactionType", "TransactionType should be a proper value!!!"},
        {"txtDuplicateRemarks", "DuplicateRemarks should not be empty!!!"},
        {"txtDuplicationCharge", "DuplicationCharge should not be empty!!!"},
        {"txtServiceTax", "ServiceTax should not be empty!!!"}

   };

}

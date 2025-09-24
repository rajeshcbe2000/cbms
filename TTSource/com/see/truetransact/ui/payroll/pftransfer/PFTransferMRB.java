/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFTransferMRB.java
 * 
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.pftransfer;

import java.util.ListResourceBundle;

public class PFTransferMRB extends ListResourceBundle {

    public PFTransferMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtPfNo", "PF Account No. should not be empty!!!"},
        {"txtBalance", "PF Balance should not be empty!!!"},
        {"txtAmount", "PF Amount should not be empty!!!"},
        {"rdoTransactionType_Debit", "Please select Payment!!!"},
        {"rdoTransactionType_Credit", "Please select Receipt!!!"}
     };
}

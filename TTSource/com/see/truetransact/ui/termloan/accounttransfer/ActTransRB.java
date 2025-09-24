/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ActTransRB.java
 *
 * Created on December 23, 2004, 4:54 PM
 */

package com.see.truetransact.ui.termloan.accounttransfer;

import java.util.ListResourceBundle;
/**
 *
 * @author  152713
 */
public class ActTransRB extends ListResourceBundle {
    
    /** Creates a new instance of PowerOfAttorneyRB */
    public ActTransRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblBankName", "Bank Name"},
        {"lblBranchName", "Branch Name"},
        {"lblRefNo", "Taken Over Ref No."},
        {"lblAmt", "Taken Over Amt"},
        {"lblSecDepRec", "Security documents recieved"},
        {"lblPoDdNo", "PO/DD No."},
        {"lblPoDdDate", "PO/DD Date"},
        {"lblPoDdAmt", "PO/DD Amt"},
        {"lblRemarks", "Remarks"}
    };
}

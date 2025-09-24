/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RTGSOtherBankBranchMRB.java
 */
package com.see.truetransact.ui.product.remittance;

import java.util.ListResourceBundle;

public class RTGSOtherBankBranchMRB extends ListResourceBundle {

    public RTGSOtherBankBranchMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtRepayScheduleMode", "Repayment Schedule Mode."},
        {"txtAcct_Name", "Enter the Account Name."},
        {"tdtExecuteDate_DOC", "Enter the Document Executed Date."},
        {"tdtExpiryDate_DOC", "Enter the Document Expiry Date."},
        {"tdtDisbursement_Dt", "Enter the Loan Disbursement Date."},
        {"txtEligibleLoan", "Enter the Loan Eligible against Security."},
        {"cboIntGetFrom", "Enter the Interest get from field."},
        {"cboAddrType_PoA", "Enter the Address Type of PoA Holder."}
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DepositLoanMRB.java
 */

package com.see.truetransact.ui.termloan.depositLoan;

import java.util.ListResourceBundle;
public class DepositLoanMRB extends ListResourceBundle {
    public DepositLoanMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboProductId", "Select Product Id"},
        {"txtDepositNo", "Enter the Deposit Account Number."},
        {"cboCategory", "Select Category"},
        {"cboSanctionMode", "Select Sanction Mode"},
        {"cboSanctionBy", "Select Loan Sanction authority"},
        {"txtLoanAmt", "Enter the Loan Eligible Amount"},
        {"tdtAccountOpenDate", "choose account Open Date"},
        {"cboAccStatus", "Select Loan Account Status."},
        {"tdtRepaymentDt", "Choose Loan Repayment Dt"},
        {"txtInter", "Enter Interest Rate"},
        

   };

}

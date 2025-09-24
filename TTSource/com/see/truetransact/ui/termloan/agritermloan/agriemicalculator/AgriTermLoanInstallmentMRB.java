/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallmentMRB.java
 * 
 * Created on Tue Jan 25 16:01:16 IST 2005
 */

package com.see.truetransact.ui.termloan.agritermloan.agriemicalculator;

import java.util.ListResourceBundle;

public class AgriTermLoanInstallmentMRB extends ListResourceBundle {
    public AgriTermLoanInstallmentMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtLoanAmt", "Enter the Loan amount."},
        {"txtInterestRate", "Enter the Interest Rate."},
        {"txtBalance", "Enter the Balance amount."},
        {"txtInterest", "Enter the Interest amount."},
        {"txtPrincipalAmt", "Enter the Principal Amount."},
        {"txtTotal", "Enter the Total amount."},
        {"cboFrequency", "Enter the Frequency."},
        {"txtNoOfInstall", "Enter the Number of Installments."},
        {"tdtInstallmentDate", "Enter the Installment Date."}, 
        {"cboRepaymentType","Enter the Repaymenttype"},
        {"cboRoundingType","Enter the Rounding Type"}
        //{"rdoUniformPrincipalEMI","Select the EMI Type"},
        //{"rdoUniformEMI","Select the EMI Type"}
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.termloan.guarantee;

import java.util.ListResourceBundle;

public class GuaranteeMasterMRB extends ListResourceBundle {
    public GuaranteeMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboPli", "PLI should be a proper value!!!"},
        {"txtCustId", "Customer ID should be a proper value!!!"},
        {"txtSanctionNo", "Loan Sanction No should be a proper value!!!"},
        {"tdtSanctionDt", "Sanction Date should be a proper value!!!"},
        {"txtLoanNo", "Loan No should be a proper value!!!"},
        {"tdtLoanDt", "Loan Date should be a proper value!!!"},
        {"txtLoanAmount", "Loan Amount should be a proper value!!!"},
        {"txtNoOfInstallments", "No Of Installments should be a proper value!!!"},
        {"cboRepaymentFrequency", "Repayment Frequency should be a proper value!!!"},
        {"txtInterestRate", "Interest Rate should be a proper value!!!"},
        {"tdtGuaranteeDt", "Guarantee Date should be a proper value!!!"},
        {"txtGuaranteeSanctionNo", "Guarantee Sanction No should be a proper value!!!"},
        {"txtGuaranteeAmount", "Guarantee Amount should be a proper value!!!"},
        {"txtGuaranteeFee", "Guarantee Fee should be a proper value!!!"},
        {"txtGuaranteeFeePer", "Guarantee Fee % should be a proper value!!!"}
    
   };

}

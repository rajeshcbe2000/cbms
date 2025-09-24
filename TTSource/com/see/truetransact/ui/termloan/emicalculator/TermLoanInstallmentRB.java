/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallmentRB.java
 * 
 * Created on Tue Jan 25 15:52:43 IST 2005
 */

package com.see.truetransact.ui.termloan.emicalculator;

import java.util.ListResourceBundle;

public class TermLoanInstallmentRB extends ListResourceBundle {
    public TermLoanInstallmentRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblPrincipalAmt", "Principal"},
        {"lblInstallmentDate", "Installment Date"},
        {"lblLoanAmt", "Loan Amount"},
        {"btnDelete", "Delete"},
        {"btnSave", "Save"},
        {"btnSubmit", "Submit"},
        {"lblInterest", "Interest"},
        {"lblNoOfInstall", "No of Installments"},
        {"lblInterestRate", "Interest Rate"},
        {"lblFrequency", "Frequency"},
       {"lblroundOfType","Round Off Type"},
       {"lblRepaymentType","RepaymentType"},
     //  {"rdoUnfiromEMI","UnfiromEMI"},
      // {"rdoUniformPrincipalEMI","UniformPrincipalEMI"},
        {"txtInterestRate", ""},
        {"lblBalance", "Balance"},
        {"lblAcctNo_Disp", ""},
        {"lblTotal", "Total"},
        {"cboFrequency","Frequency"},
        {"cboRepaymentType","RepaymentType"},
        {"cboroundOfType","roundOfType"},
        {"btnNew", "New"},
        {"btnOk", "Ok"},
        {"panInstallmentFields", "Installment Details"},
        {"lblAcctNo", "Account Number"},
        {"screenTitle", "EMI Calculator."},
        {"limitWarning", "Repayment Amount is less than Loan Amount"},
        {"keyWarning", "Please enter following values : "},
        
        {"tblColumnInstallNo", "Installment No."},
        {"tblColumnInstallDate", "Date"},
        {"tblColumnInstallPrincipal", "Principal"},
        {"tblColumnInstallInterestRate", "RoI"},
        {"tblColumnInstallInterest", "Interest Amount"},
        {"tblColumnInstallTotal", "Total"},
        {"tblColumnInstallBalance", "Balance"},
        {"tblColumnInstallActiveStatus", "Active Status"}
   };

}

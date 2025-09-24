/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.termloan.loanapplicationregister;
import java.util.ListResourceBundle;
public class LoanApplicationRB extends ListResourceBundle {
    public LoanApplicationRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblCustId", "Cust Id"},
        {"lblSchemName", "Scheme Name"},
        {"lblLoanAmt", "Loan Amt"},
       // {"lblPurposeofVisit", "Purpose of Visit"},
      //  {"lblCommentsLeft", "Comments Left"},
        {"REMARK_CASH_TRANS","Cash Transaction Token No"},
        {"REMARK_TRANSFER_TRANS","Transfer Transaction Operative Account No"},
                        //for security
        {"panSecurityDetails", "Security Details"},
        {"tblcolumnSalaryslno","SL_No"},
        {"tblcolumnSalaryCertificate","Certificate"},
        {"tblcolumnSalaryMemberNo","Member No"},
        {"tblcolumnSalaryMemberName","Name"},
        {"tblcolumnSalaryContactNo","Contact No"},
        {"tblcolumnSalaryNetworth","Networth"}
        //end
    };
}

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

package com.see.truetransact.ui.termloan.loanapplicationregister;

import java.util.ListResourceBundle;

public class LoanApplicationMRB extends ListResourceBundle {
    public LoanApplicationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtCustId", "Customer Id should be filled!!!"},
        {"cboSchemName", "Scheme Name should be filled!!!"},
        {"txtLoanAmt", "Loan Amount  should be filled!!!"}
      //  {"tdtDoj", "Date Of Joining should be a proper value!!!"},
      //  {"tdtLastDay", "Last Working Date should be a proper value!!!"},
      //  {"rdoAcc_Yes", "Accumaltion should not be empty!!!"},
      //  {"cboTransBran", "Transferred  Branch should not be empty!!!"}
        
   };

}

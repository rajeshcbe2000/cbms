/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositLienMRB.java
 * 
 * Created on Wed Jun 02 10:45:38 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.lien;

import java.util.ListResourceBundle;

public class DepositLienMRB extends ListResourceBundle {
    public DepositLienMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboSubDepositNo", "SubDepositNo should be a proper value!!!"},
        {"txtDepositNo", "DepositNo should not be empty!!!"},
        {"txtLienAmount", "LienAmount should not be empty!!!"},
        {"txtLienActNum", "LienActNum should not be empty!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"tdtLienDate", "LienDate should not be empty!!!"},
        {"cboLienProductID", "LienProductID should be a proper value!!!"},
        {"cboCreditType", "CreditType should be a proper value!!!"},
        {"txtRemark", "Remark should not be empty!!!"},
         {"txtLoanOtherSocietyLienAmount", "LienAmount should not be empty!!!"},
        {"txtLoanOtherSocietyLienAcNo", "LienActNum should not be empty!!!"},
        {"cboLienLoanType", "LoanType should be a proper value!!!"},
        {"tdtLoanOtherSocietyLienDate", "LienDate should not be empty!!!"},
        {"txtLoanOtherSocietyLienCustName", "CustomerName should not be empty!!!"},
       
        {"txtLoanOtherSocietyRemark", "Remark should not be empty!!!"}
        
    };
    
}

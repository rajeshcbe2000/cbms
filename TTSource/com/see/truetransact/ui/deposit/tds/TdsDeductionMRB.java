/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TdsDeductionMRB.java
 */

package com.see.truetransact.ui.deposit.tds;
import java.util.ListResourceBundle;
public class TdsDeductionMRB extends ListResourceBundle {
    public TdsDeductionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtDepositAmount", "Deposit Amount should not be empty!!!"},
        {"txtDepositNo", "Deposit Number should not be empty!!!"},
        {"dateMaturityDate", "Maturity Date should not be empty!!!"},
        {"dateTDSStartDate", "TDS Start Date should not be empty!!!"},
        {"txtTdsAmount", "TDS Amount should not be empty!!!"},
        {"txtInterestPayable", "Interest Payable should not be empty!!!"},
        {"cboDepositSubNo", "Deposit Sub Number should be a proper value!!!"},
        {"cboProductID", "Product ID should be a proper value!!!"},
        {"cboCollectionType", "Collection Type should be a proper value!!!"},
        {"txtInterestPaid", "Interest Paid should not be empty!!!"},
        {"txtDebitAccHead", "Debit Account Head should not be empty!!!"},
        {"dateTDSEndDate", "TDS End Date should not be empty!!!"},
        {"txtInterestAccrued", "Interest Accured should not be empty!!!"},
        {"dateDepositDate", "Deposit Date should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtDebitAccNum", "Debit Account Number should not be empty!!!"} 

   };

}

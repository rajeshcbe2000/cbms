/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TdsDeductionRB.java
 */
package com.see.truetransact.ui.deposit.tds;
import java.util.ListResourceBundle;
public class TdsDeductionRB extends ListResourceBundle {
    public TdsDeductionRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblTDSInterestTillDt", "Name"},
        {"btnClose", ""},
        {"lblTDSEndDate", "TDS End Date"},
        {"lblDebitAccNum", "Debit Account Number"},
        {"lblInterestPayablePercentage", "%"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblMaturityDate", "Maturity Date"},
        {"lblProductID", "Product ID"},
        {"lblTDSTillDt", "Name"},
        {"btnDebitAccHead", ""},
        {"btnPrint", ""},
        {"panProductID", ""},
        {"lblInterestPaid", "Interest Paid"},
        {"lblMsg", ""},
        {"lblDepositDate", "Deposit Date"},
        {"lblDepositNo", "Deposit No."},
        {"btnDepositNo", ""},
        {"btnDelete", ""},
        {"lblInterestPaidPercentage", "%"},
        {"lblTdsAmount", "TDS Amount"},
        {"btnNew", ""},
        {"lblInterestAccrued", "Interest Accrued"},
        {"lblRemarks", "Remarks"},
        {"lblTDSInterestTillDate", ""},
        {"lblAccountHead", ""},
        {"lblInterestAccruedPercentage", "%"},
        {"btnDebitAccNum", ""},
        {"lblDepositAmount", "Deposit Amount"},
        {"lblTDSTillDate", "Name"},
        {"lblCollectionType", "Debit Prod Type"},
        {"lblDebitAccHead", "Debit Account Head"},
        {"lblStatus", "                      "},
        {"btnEdit", ""},
        {"lblDepositSubNo", "Debit Prod ID."},
        {"lblSpace3", " Status :"},
        {"lblTDSStartDate", "TDS Start Date"},
        {"lblSpace2", "     "},
        {"lblSpace1", "     "},
        {"cLabel1", "Account Head"},
        {"lblInterestPayable", "Interest Payable"},
        {"TOCommandError", "TO Status Command is null"}  

   };

}

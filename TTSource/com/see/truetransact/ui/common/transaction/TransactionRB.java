/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransactionRB.java
 * 
 * Created on Wed Jan 19 16:01:00 IST 2005
 */

package com.see.truetransact.ui.common.transaction;

import java.util.ListResourceBundle;

public class TransactionRB extends ListResourceBundle {
    public TransactionRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblTransactionAmt", "Trans Amt"},
        {"btnNewTxDetails", "New"},
        {"lblTransType", "Trans Type"},
        {"btnDeleteTxDetails", "Delete"},
        {"lblTotalTransactionAmt", "Total Amout"},
        {"lblDebitAccNo", "Account No."},
        {"btnTransProductId", ""},
        {"lblApplicantsName", "Applicant's Name"},
        {"lblChequeDate", "Inst. Date"},
        {"lblProductType", "Prod Type"},
        {"lblTotalTransfer", "Total Transfer"},
        {"lblChequeNo", "Inst. No."},
        {"panTransDetails", "Transaction Details"},
        {"lblTransProductId", "Prod Id"},
        {"btnSaveTxDetails", "Save"},
        {"lblTotalCash", "Total Cash"},
        {"lblInstrumentType", "Inst. Type"},
        {"lblCustomerName", "Customer Name"},
        {"tally", "The transaction amounts are not tallied"},
        {"noRecords","There are No Records to Save!!!"},
        {"saveInTxDetailsTable","Save The Current Transaction Details in the Table!!!"},
        {"transactionAmt","Transaction Amount should be greater than zero"},
        {"tblTransDetailsColumn2","Trans Id"},
        {"tblTransDetailsColumn3","Trans Type"},
        {"tblTransDetailsColumn4","Trans Amt"},
        {"NoRecords","There are No Records to Save!!!"},
        {"saveInTxDetailsTable","Save The Current Transaction Details in the Table!!!"},
        {"tokenMsg", "Token is invalid or not issued for you. Please verify."},
        {"particulars", "Particulars Should not be blank !!! "}    
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransactionMRB.java
 * 
 * Created on Wed Jan 19 16:14:16 IST 2005
 */

package com.see.truetransact.ui.common.transaction;

import java.util.ListResourceBundle;

public class TransactionMRB extends ListResourceBundle {
    public TransactionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtTotalCash", "TotalCash should not be empty!!!"},
        {"txtTotalTransfer", "TotalTransfer should not be empty!!!"},
        {"txtChequeNo", "ChequeNo should not be empty!!!"},
        {"txtChequeNo2", "ChequeNo2 should not be empty!!!"},
        {"txtTotalTransactionAmt", "TotalTransactionAmt should not be empty!!!"},
        {"txtDebitAccNo", "AccNo should not be empty!!!"},
        {"txtApplicantsName", "ApplicantsName should not be empty!!!"},
        {"txtTransProductId", "TransProductId should not be empty!!!"},
        {"tdtChequeDate", "ChequeDate should not be empty!!!"},
        {"cboProductType", "ProductType should be a proper value!!!"},
        {"txtTransactionAmt", "TransactionAmt should not be empty!!!"},
        {"cboInstrumentType", "Instrument Type should be a proper value!!!"}, 
        {"cboTransType", "TransType should be a proper value!!!"},
        {"txtTokenNo","Token No should be a proper value!!!"},
        {"txtParticulars","Particulars should not be empty!!!"}

   };

}

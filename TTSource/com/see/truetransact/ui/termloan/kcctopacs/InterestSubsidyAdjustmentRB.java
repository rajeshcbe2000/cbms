/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestSubsidyAdjustmentRB.java
 * 
 * Created on Mon Jul 08 13:46:22 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.ListResourceBundle;

public class InterestSubsidyAdjustmentRB extends ListResourceBundle {

    public InterestSubsidyAdjustmentRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"null", "A/c wise Subsidy Due Details"},
        {"btnSaveTxDetails", "Save"},
        {"btnDebitAccNo", ""},
        {"btnDeleteTxDetails", "Delete"},
        {"lblTransProductId", "Prod Id"},
        {"btnNewTxDetails", "New"},
        {"lblChequeDate", "Inst. Date *"},
        {"lblInstrumentType", "Inst. Type *"},
        {"lblApplicantsName", "Applicant's Name *"},
        {"txtTotalTransactionAmt", ""},
        {"rdoGender_Male", "Interest Subsidy"},
        {"lblTransType", "Trans Type *"},
        {"lblChequeNo", "Inst. No."},
        {"rdoGender_Female", "Principal Subsidy"},
        {"panTransDetails", "Transaction Details"},
        {"btnTransProductId", ""},
        {"lblTotalTransactionAmt", "Total Amout"},
        {"lblTransactionAmt", "Trans Amt *"},
        {"lblDebitAccNo", "Account No."},
        {"lblProductType", "Prod Type"}
    };
}

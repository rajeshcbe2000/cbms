/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSCommencementRB.java
 * 
 * Created on Thu Jun 16 12:19:30 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdsconmmencement;

import java.util.ListResourceBundle;

public class MDSCommencementRB extends ListResourceBundle {
    public MDSCommencementRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblInstAmt", "Installment Amount"},
        {"btnClose", ""},
        {"btnDeleteTxDetails", "Delete"},
        {"txtTotalTransactionAmt", ""},
        {"lblDebitAccNo", "Account No."},
        {"panTransDetails", "Transaction Details"},
        {"lblTransProductId", "Prod Id"},
        {"lblInstrumentType", "Inst. Type *"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"btnSaveTxDetails", "Save"},
        {"lblSpace4", "     "},
        {"btnSave", ""},
        {"btnTransProductId", ""},
        {"lblSpace3", "     "},
        {"lblChequeDate", "Inst. Date *"},
        {"lblStatus", "                      "},
        {"btnView", ""},
        {"lblTransactionAmt", "Trans Amt *"},
        {"lblSchemeName", "MDS Scheme Name"},
        {"lblSpace5", "     "},
        {"lblCustomerName", "Customer Name"},
        {"btnDelete", ""},
        {"btnNewTxDetails", "New"},
        {"lblSpace", " Status :"},
        {"lblTotAmt", "Total Amount"},
        {"btnReject", ""},
        {"lblApplicantsName", "Applicant's Name *"},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"lblTotalTransactionAmt", "Total Amout"},
        {"btnCancel", ""},
        {"lblStartDt", "MDS Start Date"},
        {"btnSchemeName", ""},
        {"lblChequeNo", "Inst. No."},
        {"lblSpace6", "     "},
        {"btnPrint", ""},
        {"btnDebitAccNo", ""},
        {"lblTransType", "Trans Type *"},
        {"lblProductType", "Prod Type"} 

   };

}

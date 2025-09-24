/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueMRB.java
 * 
 * Created on Fri Nov 05 11:57:38 PST 2004
 */

package com.see.truetransact.ui.remittance.rtgs;

import java.util.ListResourceBundle;

public class RtgsRemittanceMRB extends ListResourceBundle {
    public RtgsRemittanceMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtTotalTransfer", "TotalTransfer should not be empty!!!"},
        {"txtTotalTransactionAmt", "TotalTransactionAmt should not be empty!!!"},
        {"txtDebitAccNo", "DebitAccNo should not be empty!!!"},
        {"cboCrossing", "Crossing should be a proper value!!!"},
        {"cboProductId", "ProductId should be a proper value!!!"},
        {"tdtChequeDate", "ChequeDate should not be empty!!!"},
        {"cboTransType", "TransType should be a proper value!!!"},
        {"txtAmt", "Amt should not be empty!!!"},
        {"cboCity", "City should be a proper value!!!"},
        {"txtTotalCash", "TotalCash should not be empty!!!"},
        {"txtOtherCharges", "OtherCharges should not be empty!!!"},
        {"txtExchange", "Exchange Collected should not be empty!!!"},
        {"txtExchange1", "Exchange Calculated should not be empty!!!"},
        {"txtApplicantsName", "ApplicantsName should not be empty!!!"},
        {"cboCategory", "Category should be a proper value!!!"},
        {"txtPayeeAccHead", "PayeeAccHead should not be empty!!!"},
        {"cboTransmissionType", "TransmissionType should be a proper value!!!"},
        {"txtTotalInstruments", "TotalInstruments should not be empty!!!"},
        {"cboProductType", "ProductType should be a proper value!!!"},
        {"cboPayeeProdType", "Payee Product Type should be a proper value!!!"},
        {"cboPayeeProductId", "Payee Product Id should be a proper value!!!"},
        {"txtTransProductId", "ProductId should not be empty!!!"},
        {"txtPayeeAccNo", "PayeeAccNo should not be empty!!!"},
        {"txtPostage", "Postage should not be empty!!!"},
        {"txtVariableNo", "VariableNo should not be empty!!!"},
        {"txtPANGIRNo", "PANGIRNo should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"cboBranchCode", "BranchCode should be a proper value!!!"},
        {"txtTransactionAmt", "TransactionAmt should not be empty!!!"},
        {"txtFavouring", "Favouring should not be empty!!!"},
        {"txtInstrumentNo2", "InstrumentNo2 should not be empty!!!"},
        {"txtTotalamt", "Totalamt should not be empty!!!"},
        {"txtChequeNo", "ChequeNo should not be empty!!!"},
        {"cboDraweeBank", "DraweeBank should be a proper value!!!"},
        {"txtTotalAmt", "TotalAmt should not be empty!!!"},
        {"txtInstrumentNo1", "InstrumentNo1 should not be empty!!!"},
        {"txtRevalidationCharge", "Revalidation Charge should not be empty!!!"},
        {"txtDuplicationCharge", "Duplication Charge should not be empty!!!"},
        {"txtDupServTax", "DupServTax Charge should not be empty!!!"},
        {"txtRevServTax", "RevServTax Charge should not be empty!!!"}
   };

}

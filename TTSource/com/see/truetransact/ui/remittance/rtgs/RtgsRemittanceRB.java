/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueRB.java
 * 
 * Created on Fri Nov 05 11:35:20 PST 2004
 */

package com.see.truetransact.ui.remittance.rtgs;

import java.util.ListResourceBundle;

public class RtgsRemittanceRB extends ListResourceBundle {
    public RtgsRemittanceRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblTransAmt", "Transaction Amount"},
        {"btnClose", ""},
        {"btnDeleteTxDetails", "Delete"},
        {"panTransDetails", "Transaction Details"},
        {"btnSaveIssue", "Save"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblTotalCash", "Total Cash"},
        {"lblOtherCharges", "Service Tax"},
        {"btnDeleteIssue", "Delete"},
        {"panIssueDetails", "Issue Details"},
        {"lblInstrumentHIphen", "-"},
        {"lblPayeeAccNo", "Payee Act. No."},
        {"lblPayeeAccHead", "Payee Act. Head"},
        {"lblPayeeProdType", "Payee Prod. Type"},
        {"lblPayeeProdId", "Payee Prod. Id"},
        {"btnRevalidate", "R"},
        {"lblSpace3", "     "},
        {"lblChequeDate", "Cheque Date"},
        {"lblSpace1", " Status :"},
        {"lblExchange", "Exchange Collected"},
        {"lblExchange1", "Exchange Calculated"},
        {"lblBatchId", "Batch Id"},
        {"btnNewTxDetails", "New"},
        {"btnPayeeAccHead", ""},
        {"lblTotalamount", "Ttl. Amt"},
        {"lblApplicantsName", "Applicants Name"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblCategory", "Category"},
        {"lblVariableNo", "Variable No."},
        {"lblCrossing", "Crossing"},
        {"lblTransmissionType", "Transmission Type"},
        {"lblAmt", "Amount"},
        {"btnPrint", ""},
        {"lblAccHead", "Account Head"},
        {"lblAccHeadBal", "Act. Head Balance"},
        {"lblTotalAmt", "Total Amount"},
        {"lblDebitAccNo", "Debit Account No."},
        {"lblCity", "City"},
        {"lblDisplayBatchId", "test"},
        {"lblFavouring", "Favouring"},
        {"btnDebitAccHead", ""},
        {"lblBranchCode", "Branch Code"},
        {"btnException", ""},
        {"btnSaveTxDetails", "Save"},
        {"lblProductId", "Product Id"},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"lbSpace2", "     "},
        {"lblProductType", "Product Type"},
        {"lblTransProductId", "Product Id"},
        {"btnTransProductId", ""},
        {"lblPostage", "Postage"},
        {"btnDuplicate", "D"},
        {"lblRemarks", "Remarks"},
        {"lblInstrumentNo", "Instrument No."},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblTotalTransfer", "Total Transfer"},
        {"lblPANGIRNo", "PAN / GIR No."},
        {"btnNew", ""},
        {"lbltransactionType", "Transaction Type"},
        {"btnNewIssue", "New"},
        {"lblDraweeBank", "Drawee Bank"},
        {"btnCancel", ""},
        {"lblTotalTransactionAmt", "Total Amout"},
        {"lblChequeNo", "Cheque No."},
        {"lblTotalInstruments", "Inst. Nos"},
        
        {"lblRevalidationCharge", "Revalidation Charges"},
        {"lblDuplicationCharge", "Duplicate Issue Charges"},
        {"lblDupServTax", "Dup Service Tax"},
        {"lblRevServTax", "Rev Service Tax"},
        {"TOCommandError", ""},
        {"tblIssueColumn1","Sl No"},
        {"tblIssueColumn2","Product Id"},
        {"tblIssueColumn3","Account No"},
        {"tblIssueColumn4","Total Amt"},    
        
        
        {"tblTransDetailsColumn2","Trans Id"},
        {"tblTransDetailsColumn3","Trans Type"},
        {"tblTransDetailsColumn4","Trans Amt"},
        
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogOK", "OK"},
        {"WarningMessage","This Record Already exist. Do you want to change ?"},
        {"NoRecords","There are No Records to Save!!!"},
        {"titleForRevalidation","Revalidation"},
        {"titleForDuplication","Duplication"},
        {"saveInIssueTable","Save The Current Issue Details in the Table!!!"},
        {"saveInTxDetailsTable","Save The Current Transaction Details in the Table!!!"},
        {"saveBoth","Save Both Details in the Table!!!"},
        {"EnterTxDetails","Enter The Transaction Details!!!"},
        {"EnterIssueDetails","Enter The Issue Details!!!"},
        {"tally","The Total Amounts in Both Details Should Be Tallied!!!"}, 
        {"payOrder","PayOrder Payable at Issuing Branch Only!!!"},
        {"illegalChars","Enter Alpha or Numeric values only!!!"},
        { "warningMsg", "The Instrument is stale cannot be Duplicated Unless Revalidated" },
        {"InvalidPan","\nThe PAN number entered is invalid.\n Format required is AAAAA1111A"},
        {"panGirNo","For Cash Type Of Transaction PAN/GIR NO Is Needed If Amount Exceeds The Limit!!!"},
        {"DD","Payable by a Branch Other than Issuing Branch Only!!!"} 

   };

}

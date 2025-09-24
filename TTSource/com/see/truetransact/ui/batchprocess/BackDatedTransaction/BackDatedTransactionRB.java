/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * BackDatedTransactionRB.java
 *
 * Created on March 12, 2014, 3:43 PM 2014  
 */
package com.see.truetransact.ui.batchprocess.BackDatedTransaction;

import java.util.ListResourceBundle;

/**
 * @author Suresh R
 *
 */
public class BackDatedTransactionRB extends ListResourceBundle {

    public BackDatedTransactionRB() {
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblTransDt", "Transaction Date"},
        {"lblTransDtValue", ""},
        {"lblValueDt", "Value Date"},
        {"lblCategoryValue", ""},
        {"btnClose", ""},
        {"lblTtlDrAmount", "Total Debit Amount"},
        {"lblOpMode", "Mode of Operation"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblBatchIDValue", ""},
        {"panAccInfo", "Account Info"},
        {"lblAvailBalanceValue", ""},
        {"btnAuthorize", ""},
        {"lblShadowCrValue", ""},
        {"lblTtlBalanceValue", ""},
        {"lblSep3", ""},
        {"lblTransactionID", "Transaction ID"},
        {"lblInstrumentNo", "Instrument No."},
        {"lblRemarks", "Remarks"},
        {"lblCurCaption", "All amounts are in Rupees."},
        {"panTransInfo", "Transaction Info"},
        {"lblTtlDrAmountValue", "0.0"},
        {"lblTtlCrAmountValue", "0.0"},
        {"lblCategory", "Category"},
        {"btnException", ""},
        {"btnAccountNo", ""},
        {"lblConstitution", "Constitution"},
        {"lblTransactionIDValue", ""},
        {"lblClrBalance", "Clear Balance"},
        {"lblStatus", "Status:"},
        {"lblTtlDrInstr", "Total Debit Instruments"},
        {"lblAvailBalance", "Available Balance"},
        {"lblOpeningDateValue", ""},
        {"lblBatchID", "Batch ID"},
        {"panTransDetail", "Transaction Details"},
        {"lblAccountNo", "Account No."},
        {"btnShadowCredit", ""},
        {"lblProductID", "Account Product"},
        {"btnShadowDebit", ""},
        {"btnReport", ""},
        {"lblAmount", "Amount"},
        {"lblShadowDrValue", ""},
        {"lblTtlCrInstrValue", "0"},
        {"lblClrBalanceValue", ""},
        {"lblParticulars", "Particulars"},
        {"lblNarration", "Mem Name/Narration"},
        {"btnDelete", ""},
        {"lblTtlBalance", "Total Balance"},
        {"btnAdd", ""},
        {"lblMsg", ""},
        {"lblShadowDr", "Shadow Debit"},
        {"lblConstitutionValue", ""},
        {"lblTtlCrAmount", "Total Credit Amount"},
        {"lblAccountHeadValue", ""},
        {"lblInstrumentType", "Instrument Type"},
        {"lblAccountHead", "Account Head"},
        {"lblOpModeValue", ""},
        {"lblRemarksValue", ""},
        {"lblOpeningDate", "Opening Date"},
        {"btnRejection", ""},
        {"lblInstrumentDate", "Instrument Dt"},
        {"lblStatusValue", ""},
        {"lblTtlDrInstrValue", "0"},
        {"lblTtlCrInstr", "Total Credit Instruments"},
        {"btnEdit", ""},
        {"lblShadowCr", "Shadow Credit"},
        {"lblTokenNo", "Token No."},
        {"rdoBulkTransaction_Yes", "Yes"},
        {"rdoBulkTransaction_No", "No"},
        {"lblBulkTransaction", "BulkTransaction UpLoad"},
        {"WARNING_INSTRUMENT_DATE1", "Instrument Date cannot be Post Date!!!"},
        {"WARNING_AVLB", "Account cannot be DEBITED (Insufficient Fund)!!!"},
        {"WARNING_AMOUNT", "Amount is greater than available Balance!!!"},
        {"WARNING_SHADOWDEBIT", "Shadow Debit is greater than available Balance!!!"},
        {"WARNING_INVALIDAMT", "Invalid Amount!!!"},
        {"TITLE_TRANSFERTYPE", "Transaction Type: "},
        {"TITLE", "Transfer Transactions"},
        {"REMARK_TITLE_AUTH", "Authorize Remark"},
        {"REMARK_TITLE_REJE", "Rejection Remark"},
        {"REMARK_TITLE_EXCE", "Exception Remark"},
        {"TABLE_SWDEBIT_TITLE", "Shadow Debit :: Total Amount "},
        {"TABLE_SWCREDIT_TITLE", "Shadow Credit :: Total Amount "},
        {"BATCH_TALLY", "Batch is not Tallied. It Cannot proceed with Authorization!!!"},
        {"NOT_VERIFIED", "All rows have not been verified."},
        {"COUNT_BATCH_TALLY", "Debit & Credit are more than one entry. Put it in different Batch."},
        {"NO_ROWS", "Enter & Save the Transfer Data."},
        {"lblAuthBy", "Authorize By"},
        {"SAVE_BATCH_TALLY", "Batch is not Tallied."}

    };
}

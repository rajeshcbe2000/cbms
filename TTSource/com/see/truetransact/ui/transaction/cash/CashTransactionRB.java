/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CashTransactionRB.java
 */

package com.see.truetransact.ui.transaction.cash;
import java.util.ListResourceBundle;
public class CashTransactionRB extends ListResourceBundle {
    public CashTransactionRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblShadowCreditDesc", ""},
        {"lblTokenNo", "Token No."},
        {"lblAvailBalance", "Available Balance"},
        {"lblClearBalance", "Clear Balance"},
        {"lblShadowCredit", "Shadow Credit"},
        {"lblInstrumentType", "Instrument Type"},
        {"lblMsg", ""},
        {"panTransaction", ""},
        {"lblInitiatorChannel", "Initiator Channel Type"},
        {"lblTotalBalance", "Total Balance"},
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblTransactionID", "Transaction ID"},
        {"lblConstitution", "Constitution"},
        {"lblSpace1", " Status :"},
        {"lblShadowDebitDesc", ""},
        {"lblAccName", ""},
        {"lblInputAmt", "Currency Amount"},
        {"lblModeOperationDesc", ""},
        {"lblTransactionDate", "Transaction Date"},
        {"lblConstitutionDesc", ""},
        {"btnEdit", ""},
        {"lblTransactionDateDesc", ""},
        {"lblCategory", "Category"},
        {"lblRemarksDesc", ""},
        {"lblAccHdId", " DDS "},
        {"lblAccHdDesc", ""},
        {"lblAccHd", "Account Head"},
        {"rdoTransactionType_Debit", "Payment"},
        {"lblCurrencyType", ""},
        {"lblAccNo", "Account No."},
        {"btnPrint", ""},
        {"rdoTransactionType_Credit", "Receipt"},
        {"lblTransactionType", "Transaction Type"},
        {"lblAvailBalanceDesc", ""},
        {"lblOpeningDateDesc", ""},
        {"lblInstrumentDate", "Instrument Date"},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnAccNo", ""},
        {"lblShadowDebit", "Shadow Debit"},
        {"lblAmount", "Amount"},
        {"lblInstrumentNo", "Instrument No."},
        {"lblRemarks", "Remarks"},
        {"btnDelete", ""},
        {"lblOpeningDate", "Opening Date"},
        {"lblTotalBalanceDesc", ""},
        {"lblClearBalanceDesc", ""},
        {"lblModeOperation", "Mode of Operation"},
        {"btnNew", ""},
        {"lblProdId", "Product Id"},
        {"lblProdType", "Product Type"},
        {"lblCategoryDesc", ""},
        {"panData", ""},
        {"pannstrumentNo", ""},
        {"lblInputCurrency", "Currency"},
        {"lblParticulars", "Particulars"},
        {"lblNarration", "Member Name/Narration"},
        {"btnCancel", ""},
        {"panLableValues", ""},
        {"lblInitiatorID", "Initiator ID"},
        {"lblTransactionIDDesc", ""},
        {"lblInitiatorIDDesc", ""} ,
       {"lblAuthBy", "AuthorizeBy"},
        {"tokenMsg", "Token is invalid or not issued for you. Please verify."} ,
        {"lblPanNo","PAN NO"},
        {"REMARK_TITLE_AUTH","Authorize Remark"},
        {"REMARK_TITLE_REJE","Rejection Remark"},
        {"REMARK_TITLE_EXCE","Exception Remark"},
        {"btnVer",""}
   };

}

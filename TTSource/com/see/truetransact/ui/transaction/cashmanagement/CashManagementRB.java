/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashManagementRB.java
 * 
 * Created on Fri Jan 28 18:03:05 IST 2005
 */

package com.see.truetransact.ui.transaction.cashmanagement;

import java.util.ListResourceBundle;

public class CashManagementRB extends ListResourceBundle {
    public CashManagementRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblReceivingCashierId", "Receiving Cashier ID"},
        {"btnClose", ""},
        {"lblTransactionType", "Transaction Type"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"rdoVaultCash_No", "To"},
        {"lblCashierName", "Receiving Cashier Name"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnIssueCashierID", ""},
        {"lblDisplayIssueCashierName", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"rdoTranscationType_Receipt", "Receipt"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblDate", "Date"},
        {"lblVaultCash", "Vault Cash"},
        {"lblCashBoxBalance", "Cash Box Balance"},
        {"lblDisplayDate", ""},
        {"btnNew", ""},
        {"lblIssueCashierID", "Issue Cashier ID"},
        {"btnCancel", ""},        
        {"lblDisplayCashierName", ""},
        {"rdoTranscationType_Payment", "Payment"},
        {"btnPrint", ""},
        {"lblIssueCashierName", "Issue Cashier Name"},
        {"btnReceivingCashierID", ""},
        {"rdoVaultCash_Yes", "From"},
        {"lblTransBalance", "Transaction Balance"},
        {"lblReceiptBalance", "Receipt Balance"},
        {"lblPaymentBalance", "Payment Balance"},
        {"lblPaymentDenomination", "Payment Denomination"},
        {"lblReceiptDenomination", "Receipt Denomination"},
        {"warningDenomination", "Denominations Should not be empty !!!"}

   };

}

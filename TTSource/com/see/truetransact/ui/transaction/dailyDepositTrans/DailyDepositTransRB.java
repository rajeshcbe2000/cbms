/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DailyDepositTransRB.java
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;
import java.util.ListResourceBundle;
public class DailyDepositTransRB extends ListResourceBundle {
    public DailyDepositTransRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblShadowCredit", "Shadow Credit"},
        {"panTransaction", ""},
        {"lblInitiatorChannel", "Initiator Channel Type"},
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblTransactionID", "Transaction ID"},
        {"lblSpace1", " Status :"},
        {"lblShadowDebitDesc", ""},
        {"lblAccName", ""},
        {"lblInputAmt", "Currency Amount"},
        {"lblTransactionDate", "Transaction Date"},
        {"btnEdit", ""},
        {"lblProdId","Product Id"},
        {"lblProd_type","Prod Type"},
        {"lblProductId"," DAILY DEPOSIT SCHEME "},
        {"lblAgentType","Agent Id"},
        {"lblBalanceAmount","BalanceAmount"},
        {"lblBalance",""},
        {"lblAccHdId", " DDS "},
        {"lblAccHd", "Account Head"},
        {"lblCurrencyType", ""},
        {"lblAccNo", "Account No."},
        {"btnPrint", ""},
        {"rdoTransactionType_Credit", "Receipt"},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnAccNo", ""},
        {"lblShadowDebit", "Shadow Debit"},
        {"lblAmount", "Amount"},
        {"lblInstrumentNo", "Instrument No."},
        {"lblRemarks", "Remarks"},
        {"btnDelete", ""},
        {"lblOpeningDate", "Opening Date"},
        {"btnNew", ""},
        {"lblCategoryDesc", ""},
        {"panData", ""},
        {"pannstrumentNo", ""},
        {"btnCancel", ""},
        {"panLableValues", ""},
        {"lblInitiatorID", "Initiator ID"},
        {"lblTransactionIDDesc", ""},
        {"lblInitiatorIDDesc", ""} ,
        {"lblCustNm", "Customer Name"} ,
        {"lblInstrumentDate", "Collection Date"} ,
        {"lblAgNm", "Agent Name"} ,
        {"lblTotalAmt", "TotalAmt"} ,
        {"AlreadyAdd", "Already This Account is Added in The list!!!"},
        {"lblTransactionType", "TransactionType"}, 
        {"NOT_VERIFIED","All rows have not been verified."}
   };
}

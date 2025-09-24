/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CashTransactionMRB.java
 */
package com.see.truetransact.ui.transaction.cash;
import java.util.ListResourceBundle;
public class CashTransactionMRB extends ListResourceBundle {
    public CashTransactionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAmount", "Amount should not be empty or zero!!!"},
        {"tdtInstrumentDate", "InstrumentDate should not be empty!!!"},
        {"txtParticulars", "Particulars should not be empty!!!"},
        {"txtNarration", "Member Name/Narration should not be empty!!!"},
        {"txtTokenNo", "TokenNo should not be empty!!!"},
        {"txtInstrumentNo1", "InstrumentNo should not be empty!!!"},
        {"txtInstrumentNo2", "InstrumentNo should not be empty!!!"},
        {"cboInputCurrency", "InputCurrency should be a proper value!!!"},
        {"rdoTransactionType_Debit", "TransactionType should be selected!!!"},
        {"cboInstrumentType", "InstrumentType should be a proper value!!!"},
        {"txtInputAmt", "InputAmt should not be empty!!!"},
        {"cboProdId", "ProdId should be a proper value!!!"},
        {"cboProdType", "ProdType should be a proper value!!!"},
        {"txtAccNo", "AccNo should be a proper value!!!"},
        {"txtAccHdId", "AccHdId should be a proper value!!!"},
        {"txtInitiatorChannel", "InitiatorChannel should be a proper value!!!"},
        {"txtPanNo", "Pan Number should not be empty!!!"},
        

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashManagementMRB.java
 * 
 * Created on Sat Jan 29 10:32:21 IST 2005
 */

package com.see.truetransact.ui.transaction.cashmanagement;

import java.util.ListResourceBundle;

public class CashManagementMRB extends ListResourceBundle {
    public CashManagementMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoTranscationType_Receipt", "TranscationType should be selected!!!"},
        {"txtReceivingCashierID", "ReceivingCashierID should not be empty!!!"},
        {"txtIssueCashierID", "IssueCashierID should not be empty!!!"},
        {"txtCashBoxBalance", "CashBoxBalance should not be empty!!!"},
        {"txtTransBalance", "Transaction Balance should not be empty!!!"},
        {"rdoVaultCash_Yes", "VaultCash should be selected!!!"} 

   };

}

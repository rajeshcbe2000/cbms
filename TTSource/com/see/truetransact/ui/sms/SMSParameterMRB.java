/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterMRB.java
 * 
 * Created on Thu May 03 12:22:30 IST 2012
 */

package com.see.truetransact.ui.sms;

import java.util.ListResourceBundle;

public class SMSParameterMRB extends ListResourceBundle {
    public SMSParameterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"chkCreditClearing", "CreditClearing should be selected!!!"},
        {"txtCreditClearingAmt", "CreditClearingAmt should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"cboProdType", "ProdType should be a proper value!!!"},
        {"chkDebitCash", "DebitCash should be selected!!!"},
        {"chkCreditTransfer", "CreditTransfer should be selected!!!"},
        {"txtDebitTransferAmt", "DebitTransferAmt should not be empty!!!"},
        {"chkDebitClearing", "DebitClearing should be selected!!!"},
        {"txtDebitClearingAmt", "DebitClearingAmt should not be empty!!!"},
        {"txtCreditCashAmt", "CreditCashAmt should not be empty!!!"},
        {"chkCreditCash", "CreditCash should be selected!!!"},
        {"txtCreditTransferAmt", "CreditTransferAmt should not be empty!!!"},
        {"cboProdId", "ProdId should be a proper value!!!"},
        {"chkDebitTransfer", "DebitTransfer should be selected!!!"},
        {"txtDebitCashAmt", "DebitCashAmt should not be empty!!!"} 

   };

}

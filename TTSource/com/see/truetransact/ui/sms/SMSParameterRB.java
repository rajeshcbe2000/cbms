/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterRB.java
 * 
 * Created on Mon Apr 30 17:06:48 IST 2012
 */

package com.see.truetransact.ui.sms;

import java.util.ListResourceBundle;

public class SMSParameterRB extends ListResourceBundle {
    public SMSParameterRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"chkDebitClearing", ""},
        {"lblTransfer", "Transfer"},
        {"lblCredit", "Credit"},
        {"null", "Minimum Amount Limit for Sending Alerts"},
        {"chkDebitCash", ""},
        {"chkCreditTransfer", ""},
        {"chkCreditCash", ""},
        {"chkDebitTransfer", ""},
        {"lblDebit", "Debit"},
        {"lblCash", "Cash"},
        {"lblClearing", "Clearing"} 

   };

}

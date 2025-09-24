/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SuspiciousConfigMRB.java
 * 
 * Created on Thu Mar 10 15:57:19 IST 2005
 */

package com.see.truetransact.ui.sysadmin.suspiciousconfig;

import java.util.ListResourceBundle;

public class SuspiciousConfigMRB extends ListResourceBundle {
    public SuspiciousConfigMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAccNo", "AccNo should not be empty!!!"},
        {"chkCreditClearing", "CreditClearing should be selected!!!"},
        {"txtWorthExceeds", "WorthExceeds should not be empty!!!"},
        {"txtCountExceeds", "CountExceeds should not be empty!!!"},
        {"chkDebitClearing", "DebitClearing should be selected!!!"},
        {"cboConfigurationFor", "ConfigurationFor should be a proper value!!!"},
        {"cboTransactionType", "TransactionType should be a proper value!!!"},
        {"cboProdType", "ProdType should be a proper value!!!"},
        {"chkCreditTransfer", "CreditTransfer should be selected!!!"},
        {"chkDebitCash", "DebitCash should be selected!!!"},
        {"chkCreditCash", "CreditCash should be selected!!!"},
        {"txtPeriod", "Period should not be empty!!!"},
        {"txtCustNo", "CustNo should not be empty!!!"},
        {"chkDebitTransfer", "DebitTransfer should be selected!!!"},
        {"cboProdId", "ProdId should be a proper value!!!"},
        {"cboPeriod", "Period should be a proper value!!!"} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SuspiciousConfigRB.java
 * 
 * Created on Sat Mar 12 11:59:02 IST 2005
 */

package com.see.truetransact.ui.sysadmin.suspiciousconfig;

import java.util.ListResourceBundle;

public class SuspiciousConfigRB extends ListResourceBundle {
    public SuspiciousConfigRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"chkCreditClearing", ""},
        {"btnClose", ""},
        {"lblCredit", "Credit"},
        {"btnCustNo", ""},
        {"chkCreditTransfer", ""},
        {"chkDebitCash", ""},
        {"lblWorthExceeds", "Worth Exceeds"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"panTransaction", "Transaction"},
        {"chkDebitClearing", ""},
        {"btnSuspiciousNew", "New"},
        {"lblSpace2", "     "},
        {"lblCountExceeds", "Count Exceeds"},
        {"btnSave", ""},
        {"btnSuspiciousSave", "Save"},
        {"lblProdType", "Product Type"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"chkCreditCash", ""},
        {"btnAccNo", ""},
        {"lblPeriod", "Period"},
        {"lblCash", "Cash"},
        {"lblClearing", "Clearing"},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblCustNo", "Customer No"},
        {"panConfigurationFor", "Configuration For"},
        {"lblConfigurationFor", "Configuration For"},
        {"lblDebit", "Debit"},
        {"btnNew", ""},
        {"lblProdId", "Product Id"},
        {"lblTransfer", "Transfer"},
        {"btnCancel", ""},
        {"lblAccNo", "Account No"},
        {"chkDebitTransfer", ""},
        {"btnPrint", ""},
        {"btnSuspiciousDelete", "Delete"},
        
        {"tblColumn1", "SI No"},
        {"tblColumn2", "Config"},
        {"tblColumn3", "Count"},
        {"tblColumn4", "Worth"}

   };

}

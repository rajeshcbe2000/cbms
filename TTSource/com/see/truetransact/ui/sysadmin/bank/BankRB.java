/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BankRB.java
 */

package com.see.truetransact.ui.sysadmin.bank;
import java.util.ListResourceBundle;
public class BankRB extends ListResourceBundle {
    public BankRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblBankName", "Bank Name"},
        {"btnClose", ""},
        {"rdoB2B_No", "No"},
        {"rdoT2T_Yes", "Yes"},
        {"lblMsg", ""},
        {"lblMins", "mins"},
        {"rdoT2T_No", "No"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblT2T", "Teller To Teller Allowed"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblDataIP", "Data Center IP Address"},
        {"lblCashLimit", "Cash Limit"},
        {"btnDelete", ""},
        {"lblSiteIP", "Site IP Address"},
        {"lblWebsite", " Website Address"},
        {"panMainBankUI", ""},
        {"lblHours", "hrs"},
        {"rdoB2B_Yes", "Yes"},
        {"lblSpace", " Status :"},
        {"btnEdit", ""},
        {"btnException", ""},
        {"btnAuthorize", ""},
        {"btnReject", ""},
        {"btnNew", ""},
        {"lblDayEndProcessTime", "Day End Process Time"},
        {"lblTranPosting", "Transactions Posting"},
        {"lblConversion", "Currency Conversion"},
        {"lblBaseCurrency", "Base Currency"},
        {"lblBankCode", "Bank Code"},
        {"btnCancel", ""},
        {"lblB2B", "Branch To Branch Allowed"},
        {"panTransactions", "User Defined Transactions"},
        {"btnPrint", ""},
        {"lblBankOpeningDate", "Bank Opening Date"},
        {"lblSupportEmail", "Support Email"}
        
        
    };
    
}

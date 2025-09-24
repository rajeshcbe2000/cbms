/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BankMRB.java
 */

package com.see.truetransact.ui.sysadmin.bank;
import java.util.ListResourceBundle;
public class BankMRB extends ListResourceBundle {
    public BankMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtSiteIP", "Enter the Site IP"},
        {"txtCashLimit", "CashLimit should not be empty!!!"},
        {"cboMins", "Mins should be a proper value!!!"},
        {"rdoB2B_Yes", "B2B should be selected!!!"},
        {"txtBankName", "BankName should not be empty!!!"},
        {"rdoT2T_Yes", "T2T should be selected!!!"},
        {"cboHours", "Hours should be a proper value!!!"},
        {"cboTranPosting", "TranPosting should be a proper value!!!"},
        {"cboConversion", "Conversion should be a proper value!!!"},
        {"cboBaseCurrency", "BaseCurrency should be a proper value!!!"},
        {"txtDataIP", "Enter the Data Center IP"},
        {"txtBankCode", "BankCode should not be empty!!!"},
        {"txtWebsite", "Enter the Website"},
        {"tdtBankOpeningDate", "Enter the Bank Opening Date"},
        {"txtSupportEmail", "Enter the Support Mail ID"}
        

   };

}

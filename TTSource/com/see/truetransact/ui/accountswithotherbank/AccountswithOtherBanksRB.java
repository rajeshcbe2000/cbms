/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductRB.java
 *
 * Created on Mon Apr 11 12:08:48 IST 2005
 */

package com.see.truetransact.ui.accountswithotherbank;

import java.util.ListResourceBundle;

public class AccountswithOtherBanksRB extends ListResourceBundle {
    public AccountswithOtherBanksRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnTabSave", "Save"},
        {"btnTabDelete", "Delete"},        
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},        
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},       
        {"btnReject", ""},
        {"btnEdit", ""},
        {"null", "Account Details"},
        {"btnTabNew", "New"},      
        {"btnPrint", ""},       
        {"btnException", ""},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
              
        {"lblInvestmentID", "Product ID"},
        {"lblInvestmentName", "A/c Description"},
        {"lblIssueDt", "Issue Date"},
        {"lblInvestmentPeriod", "Investment Period"},
        {"lblInvestmentPeriod_Years", "Years"},
        {"lblInvestmentPeriod_Months", "Months"},
        {"lblInvestmentPeriod_Days", "Days"},
        {"lblInvestmentBehaves", "Account Type"},
        {"lblMaturityDate", "Maturity Date"},
        {"lblFaceValue", "Face Value"},
        {"lblInterestPaymentFrequency", "Int Payment Frequency"},
        {"lblCouponRate", "CouponRate/Interest Rate"},
        {"lblSLR", "SLR"} ,
        {"lblPutOption", "Put Option"},
        {"lblCallOption", "Call Option"},
        {"lblLstIntApplDt", "Last interest paid Date"},
        {"lblSetupOption", "Setup Option"}      
        
    };
    
}

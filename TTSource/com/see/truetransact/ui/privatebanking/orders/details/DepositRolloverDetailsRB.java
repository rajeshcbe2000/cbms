/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositRolloverDetailsRB.java
 * 
 * Created on Wed Jul 07 15:44:03 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.orders.details;

import java.util.ListResourceBundle;

public class DepositRolloverDetailsRB extends ListResourceBundle {
    public DepositRolloverDetailsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblEntitlementGroup", "Entitlement Group"},
        {"lblSpread", "Spread"},
        {"btnAuthorize", ""},
        {"lblCurrency", "Currency"},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"rdoPhoneOrder_No", "No"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblInterestEarned", "Interest Earned"},
        {"btnStartDate", ""},
        {"lblSpace3", " Status :"},
        {"lblStatus", "                      "},
        {"lblRolloverAmount", "Rollover Amount"},
        {"lblSpace1", "     "},
        {"lblDepositReferenceNumber", "Deposit Reference No."},
        {"panEnterRolloverDetails", "Enter Rollover Details"},
        {"panDepositRollover", "Deposit Rollover"},
        {"lblStartDate", "Start Date"},
        {"lblRollover", "Rollover"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblAssetSubClass", "Asset Sub – Class"},
        {"lblPortfolioLocation", "Portfolio Location"},
        {"btnReject", ""},
        {"lblTenor", "Tenor"},
        {"lblTenorDays", "Days"},
        {"btnEdit", ""},
        {"btnEntitlementGroup", ""},
        {"lblPhoneOrder", "Phone Order"},
        {"btnNew", ""},
        {"lblMaturityDate", "Maturity Date"},
        {"lblRolloverType", "Rollover Type"},
        {"rdoPhoneOrder_Yes", "Yes"},
        {"lblAccount", "Account"},
        {"btnCancel", ""},
        {"lblCSPMemoAvailableBalance", "CSP Memo Avail Bal."},
        {"btnPrint", ""},
        {"lblPrincipal", "Principal"},
        
        {"TOCommandError", "TO Status Command is null"}
   };

}

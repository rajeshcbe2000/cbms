/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositRolloverDetailsMRB.java
 * 
 * Created on Wed Jul 07 15:52:13 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.orders.details;

import java.util.ListResourceBundle;

public class DepositRolloverDetailsMRB extends ListResourceBundle {
    public DepositRolloverDetailsMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAccount", "Account should not be empty!!!"},
        {"dateMaturityDate", "Maturity Date should not be empty!!!"},
        {"txtDepositReferenceNumber", "Deposit Reference Number should not be empty!!!"},
        {"dateStartDate", "Start Date should not be empty!!!"},
        {"txtPrincipal", "Principal should not be empty!!!"},
        {"cboRolloverType", "Rollover Type should be a proper value!!!"},
        {"txtCSPMemoAvailableBalance", "CSP Memo Available Balance should not be empty!!!"},
        {"rdoPhoneOrder_Yes", "Phone Order should be selected!!!"},
        {"cboCurrency", "Currency should be a proper value!!!"},
        {"txtTenorDays", "Tenor Days should not be empty!!!"},
        {"txtSpread", "Spread should not be empty!!!"},
        {"txtPortfolioLocation", "Portfolio Location should not be empty!!!"},
        {"txtInterestEarned", "Interest Earned should not be empty!!!"},
        {"cboTenor", "Tenor should be a proper value!!!"},
        {"txtAssetSubClass", "Asset Sub - Class should not be empty!!!"},
        {"txtEntitlementGroup", "Entitlement Group should not be empty!!!"},
        {"txtRollover", "Rollover should not be empty!!!"},
        {"txtRolloverAmount", "Rollover Amount should not be empty!!!"} 

   };

}

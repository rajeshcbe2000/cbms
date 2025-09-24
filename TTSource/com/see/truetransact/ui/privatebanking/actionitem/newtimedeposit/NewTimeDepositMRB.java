/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NewTimeDepositMRB.java
 *
 * Created on Mon Jul 12 16:52:30 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit;

import java.util.ListResourceBundle;

public class NewTimeDepositMRB extends ListResourceBundle {
    public NewTimeDepositMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtSettlementAssetSubClass", "SettlementAssetSubClass should not be empty!!!"},
        {"txtTraderDealerInst", "TraderDealerInst should not be empty!!!"},
        {"txtSettlementAccount", "SettlementAccount should not be empty!!!"},
        {"txtPortfolioAssetSubClass", "PortfolioAssetSubClass should not be empty!!!"},
        {"txtAccount", "Account should not be empty!!!"},
        {"txtPrincipalAccount", "Account should not be empty!!!"},
        {"txtTenor1", "Tenor should not be empty!!!"},
        {"txtTenor2", "Tenor should not be empty!!!"},
        {"tdtMaturityDate", "MaturityDate should not be empty!!!"},
        {"txtAutorollInd", "AutorollInd should not be empty!!!"},
        {"txtSpread", "Spread should not be empty!!!"},
        {"txtClientRate", "ClientRate should not be empty!!!"},
        {"tdtExecutionDate", "ExecutionDate should not be empty!!!"},
        {"txtPortfolioAccount", "PortfolioAccount should not be empty!!!"},
        {"cboProductType", "ProductType should be a proper value!!!"},
        {"txtEntitlementGroup", "EntitlementGroup should not be empty!!!"},
        {"txtBankOfficeInstruction", "BankOfficeInstruction should not be empty!!!"},
        {"txtMember", "Member should not be empty!!!"},
        {"txtCreditNotes", "CreditNotes should not be empty!!!"},
        {"tdtStartDate", "StartDate should not be empty!!!"},
        {"txtClientAdvices", "ClientAdvices should not be empty!!!"},
        {"rdoPhoneOrder_Yes", "PhoneOrder should be selected!!!"},
        {"cboSettlementType", "SettlementType should be a proper value!!!"},
        {"txtOrderAmount", "OrderAmount should not be empty!!!"},
        {"txtPortfolioLocation", "PortfolioLocation should not be empty!!!"},
        {"txtAssetSubClass", "AssetSubClass should not be empty!!!"},
        {"txtPrincipalAssetSubClass", "PrincipalAssetSubClass should not be empty!!!"}
        
    };
    
}

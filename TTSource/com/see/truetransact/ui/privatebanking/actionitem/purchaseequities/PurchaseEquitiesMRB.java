/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PurchaseEquitiesMRB.java
 * 
 * Created on Thu Jul 08 10:44:37 GMT+05:30 2004
 */

package com.see.truetransact.ui.privatebanking.actionitem.purchaseequities;

import java.util.ListResourceBundle;

public class PurchaseEquitiesMRB extends ListResourceBundle {
    public PurchaseEquitiesMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtLotSize", "LotSize should not be empty!!!"},
        {"txtSettlementAssetSubClass", "SettlementAssetSubClass should not be empty!!!"},
        {"tdtTillDate", "TillDate should not be empty!!!"},
        {"txtTraderDealerInst", "TraderDealerInst should not be empty!!!"},
        {"cboCommType", "CommType should be a proper value!!!"},
        {"txtApproxAmount", "ApproxAmount should not be empty!!!"},
        {"txtSettlementAccount", "SettlementAccount should not be empty!!!"},
        {"tdtSettlementDate", "SettlementDate should not be empty!!!"},
        {"cboMinAmount", "MinAmount should be a proper value!!!"},
        {"rdoEDTSEligible_Yes", "EDTSEligible should be selected!!!"},
        {"txtUnits", "Units should not be empty!!!"},
        {"txtDealerName", "DealerName should not be empty!!!"},
        {"cboCurrency", "Currency should be a proper value!!!"},
        {"txtPortfolioAssetSubClass", "PortfolioAssetSubClass should not be empty!!!"},
        {"txtExchange", "Exchange should not be empty!!!"},
        {"txtLodgementFee", "LodgementFee should not be empty!!!"},
        {"tdtExecutionDate", "ExecutionDate should not be empty!!!"},
        {"txtPortfolioAccount", "PortfolioAccount should not be empty!!!"},
        {"txtCommRate", "CommRate should not be empty!!!"},
        {"txtEntitlementGroup", "EntitlementGroup should not be empty!!!"},
        {"txtBankOfficeInstruction", "BankOfficeInstruction should not be empty!!!"},
        {"rdoProcessthruEdts_Yes", "ProcessthruEdts should be selected!!!"},
        {"txtMember", "Member should not be empty!!!"},
        {"txtSMIInfo", "SMIInfo should not be empty!!!"},
        {"txtCreditNotes", "CreditNotes should not be empty!!!"},
        {"txtMinCommAmount", "MinCommAmount should not be empty!!!"},
        {"cboOrderSubType", "OrderSubType should be a proper value!!!"},
        {"cboLodgementFee", "LodgementFee should be a proper value!!!"},
        {"txtClientAdvices", "ClientAdvices should not be empty!!!"},
        {"cboCommission", "Commission should be a proper value!!!"},
        {"rdoPhoneOrder_Yes", "PhoneOrder should be selected!!!"},
        {"txtPrice", "Price should not be empty!!!"},
        {"txtCommission", "Commission should not be empty!!!"},
        {"txtPortfolioLocation", "PortfolioLocation should not be empty!!!"},
        {"cboOrderType", "OrderType should be a proper value!!!"} 

   };

}

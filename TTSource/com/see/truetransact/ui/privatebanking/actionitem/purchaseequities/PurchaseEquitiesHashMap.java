/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * PurchaseEquitiesHashMap.java
 */
package com.see.truetransact.ui.privatebanking.actionitem.purchaseequities;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class PurchaseEquitiesHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public PurchaseEquitiesHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLotSize", new Boolean(true));
        mandatoryMap.put("txtSettlementAssetSubClass", new Boolean(true));
        mandatoryMap.put("tdtTillDate", new Boolean(true));
        mandatoryMap.put("txtTraderDealerInst", new Boolean(true));
        mandatoryMap.put("cboCommType", new Boolean(true));
        mandatoryMap.put("txtSettlementAccount", new Boolean(true));
        mandatoryMap.put("txtApproxAmount", new Boolean(true));
        mandatoryMap.put("tdtSettlementDate", new Boolean(true));
        mandatoryMap.put("rdoEDTSEligible_Yes", new Boolean(true));
        mandatoryMap.put("cboMinAmount", new Boolean(true));
        mandatoryMap.put("txtUnits", new Boolean(true));
        mandatoryMap.put("txtDealerName", new Boolean(true));
        mandatoryMap.put("txtPortfolioAssetSubClass", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("txtExchange", new Boolean(true));
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("txtLodgementFee", new Boolean(true));
        mandatoryMap.put("txtPortfolioAccount", new Boolean(true));
        mandatoryMap.put("txtCommRate", new Boolean(true));
        mandatoryMap.put("txtEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtBankOfficeInstruction", new Boolean(true));
        mandatoryMap.put("rdoProcessthruEdts_Yes", new Boolean(true));
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtSMIInfo", new Boolean(true));
        mandatoryMap.put("txtCreditNotes", new Boolean(true));
        mandatoryMap.put("txtMinCommAmount", new Boolean(true));
        mandatoryMap.put("cboOrderSubType", new Boolean(true));
        mandatoryMap.put("cboLodgementFee", new Boolean(true));
        mandatoryMap.put("txtClientAdvices", new Boolean(true));
        mandatoryMap.put("cboCommission", new Boolean(true));
        mandatoryMap.put("rdoPhoneOrder_Yes", new Boolean(true));
        mandatoryMap.put("txtPrice", new Boolean(true));
        mandatoryMap.put("txtCommission", new Boolean(true));
        mandatoryMap.put("txtPortfolioLocation", new Boolean(true));
        mandatoryMap.put("cboOrderType", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

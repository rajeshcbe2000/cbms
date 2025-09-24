/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NewTimeDepositHashMap.java
 */
package com.see.truetransact.ui.privatebanking.actionitem.newtimedeposit;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class NewTimeDepositHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public NewTimeDepositHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSettlementAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtTraderDealerInst", new Boolean(true));
        mandatoryMap.put("txtSettlementAccount", new Boolean(true));
        mandatoryMap.put("txtPortfolioAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtTenor1", new Boolean(true));
        mandatoryMap.put("txtTenor2", new Boolean(true));
        mandatoryMap.put("txtAutorollInd", new Boolean(true));
        mandatoryMap.put("tdtMaturityDate", new Boolean(true));
        mandatoryMap.put("txtClientRate", new Boolean(true));
        mandatoryMap.put("txtSpread", new Boolean(true));
        mandatoryMap.put("txtPortfolioAccount", new Boolean(true));
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("txtEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtPrincipalAccount", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("txtBankOfficeInstruction", new Boolean(true));
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtCreditNotes", new Boolean(true));
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("txtAccount", new Boolean(true));
        mandatoryMap.put("txtClientAdvices", new Boolean(true));
        mandatoryMap.put("rdoPhoneOrder_Yes", new Boolean(true));
        mandatoryMap.put("cboSettlementType", new Boolean(true));
        mandatoryMap.put("txtOrderAmount", new Boolean(true));
        mandatoryMap.put("txtPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtPrincipalAssetSubClass", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

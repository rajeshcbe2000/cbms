/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FTInternalHashMap.java
 */

package com.see.truetransact.ui.privatebanking.actionitem.ftinternal;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class FTInternalHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public FTInternalHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtCreditAmount", new Boolean(true));
        mandatoryMap.put("txtDebitAmount", new Boolean(true));
        mandatoryMap.put("txtTraderDealerInst", new Boolean(true));
        mandatoryMap.put("txtCreditNotes", new Boolean(true));
        mandatoryMap.put("txtDebtiPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtDebitAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtDebitAccount", new Boolean(true));
        mandatoryMap.put("txtDebitEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtCreditPortfolioLocation", new Boolean(true));
        mandatoryMap.put("tdtExecutionDate", new Boolean(true));
        mandatoryMap.put("txtCreditAccount", new Boolean(true));
        mandatoryMap.put("txtCreditEntitlementGroup", new Boolean(true));
        mandatoryMap.put("tdtValueDate", new Boolean(true));
        mandatoryMap.put("txtBankOfficeInstruction", new Boolean(true));
        mandatoryMap.put("txtClientAdvices", new Boolean(true));
        mandatoryMap.put("txtCreditAssetSubClass", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

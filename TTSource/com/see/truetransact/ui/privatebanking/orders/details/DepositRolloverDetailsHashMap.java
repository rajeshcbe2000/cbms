/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DepositRolloverDetailsHashMap.java
 */

package com.see.truetransact.ui.privatebanking.orders.details;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DepositRolloverDetailsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public DepositRolloverDetailsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAccount", new Boolean(true));
        mandatoryMap.put("dateMaturityDate", new Boolean(true));
        mandatoryMap.put("txtDepositReferenceNumber", new Boolean(true));
        mandatoryMap.put("dateStartDate", new Boolean(true));
        mandatoryMap.put("txtPrincipal", new Boolean(true));
        mandatoryMap.put("cboRolloverType", new Boolean(true));
        mandatoryMap.put("txtCSPMemoAvailableBalance", new Boolean(true));
        mandatoryMap.put("rdoPhoneOrder_Yes", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(true));
        mandatoryMap.put("txtTenorDays", new Boolean(true));
        mandatoryMap.put("txtSpread", new Boolean(true));
        mandatoryMap.put("txtPortfolioLocation", new Boolean(true));
        mandatoryMap.put("txtInterestEarned", new Boolean(true));
        mandatoryMap.put("cboTenor", new Boolean(true));
        mandatoryMap.put("txtAssetSubClass", new Boolean(true));
        mandatoryMap.put("txtEntitlementGroup", new Boolean(true));
        mandatoryMap.put("txtRollover", new Boolean(true));
        mandatoryMap.put("txtRolloverAmount", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

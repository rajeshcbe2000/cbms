/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BankClearingParameterHashMap.java
 */
package com.see.truetransact.ui.clearing.banklevel;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class BankClearingParameterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public BankClearingParameterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtICReturnCharges", new Boolean(true));
        mandatoryMap.put("txtICReturnChargesHD", new Boolean(true));
        mandatoryMap.put("txtClearingSuspenseHD", new Boolean(true));
        mandatoryMap.put("txtOCReturnCharges", new Boolean(true));
        mandatoryMap.put("txtOCReturnChargesHD", new Boolean(true));
        mandatoryMap.put("txtClearingHD", new Boolean(true));
        mandatoryMap.put("txtClearingType", new Boolean(true));
        mandatoryMap.put("txtShortClaimAcHead", new Boolean(true));
        mandatoryMap.put("txtExcessClaimAcHead", new Boolean(true));
        mandatoryMap.put("rdoCompleteDay_Yes", new Boolean(true));
       
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

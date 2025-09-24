/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ParameterHashMap.java
 */
package com.see.truetransact.ui.clearing;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class ParameterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public ParameterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtServiceBranchCode", new Boolean(true));
        mandatoryMap.put("txtLotSize", new Boolean(true));
        mandatoryMap.put("txtICReturnCharges", new Boolean(true));
        mandatoryMap.put("txtICReturnChargesHD", new Boolean(true));
        mandatoryMap.put("txtClearingSuspenseHD", new Boolean(true));
        mandatoryMap.put("txtOCReturnCharges", new Boolean(true));
        mandatoryMap.put("rdoCompleteDay_Yes", new Boolean(true));
        mandatoryMap.put("txtOCReturnChargesHD", new Boolean(true));
        mandatoryMap.put("txtClearingHD", new Boolean(true));
        mandatoryMap.put("txtValueofHighValueCheque", new Boolean(false));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("cboClearingFreq", new Boolean(true));
        mandatoryMap.put("txtClearingFreq", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

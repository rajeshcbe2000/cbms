/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InwardClearingTallyHashMap.java
 */
package com.see.truetransact.ui.clearing.tally;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class InwardClearingTallyHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public InwardClearingTallyHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtServiceInstruments", new Boolean(true));
        mandatoryMap.put("txtServiceAmount", new Boolean(true));
        mandatoryMap.put("txtPhysicalAmount", new Boolean(true));
        mandatoryMap.put("txtPhysicalInstruments", new Boolean(true));
        mandatoryMap.put("tdtClearingDate", new Boolean(true));
        mandatoryMap.put("txtDifferenveInstrument", new Boolean(false));
        mandatoryMap.put("txtScheduleNo", new Boolean(false));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("cboCurrencyBID", new Boolean(true));
        mandatoryMap.put("cboCurrencyPC", new Boolean(true));
        mandatoryMap.put("txtDifferenceAmount", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

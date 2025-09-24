/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * OutwardClearingTallyHashMap.java
 */
package com.see.truetransact.ui.clearing.outwardtally;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class OutwardClearingTallyHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public OutwardClearingTallyHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtClosingDate", new Boolean(false));
        mandatoryMap.put("txtSBAmount", new Boolean(false));
        mandatoryMap.put("txtDAmount", new Boolean(false));
        mandatoryMap.put("tdtClearingDate", new Boolean(true));
        mandatoryMap.put("txtSBNumberofInstruments", new Boolean(false));
        mandatoryMap.put("txtDNumberofInstruments", new Boolean(false));
        mandatoryMap.put("txtScheduleNo", new Boolean(false));
        mandatoryMap.put("cboClearingType", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

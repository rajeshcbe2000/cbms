/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BouncingInstrumentwiseHashMap.java
 */
package com.see.truetransact.ui.clearing.bouncing;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class BouncingInstrumentwiseHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public BouncingInstrumentwiseHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtInwardScheduleNo", new Boolean(true));
//        mandatoryMap.put("cboReasonforBouncing", new Boolean(true));
        mandatoryMap.put("txtReasonforBouncing", new Boolean(true));
        mandatoryMap.put("cboBouncingType", new Boolean(true));
        mandatoryMap.put("chkPresentAgain", new Boolean(false));
        mandatoryMap.put("dateClearingDate", new Boolean(true));
        mandatoryMap.put("txtClearingSerialNo", new Boolean(true));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

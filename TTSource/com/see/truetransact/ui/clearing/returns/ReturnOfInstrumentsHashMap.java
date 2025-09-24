/*
 *Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsHashMap.java
 *
 * Created on April 5, 2004, 3:59 PM
 */
package com.see.truetransact.ui.clearing.returns;

/**
 *
 * @author  Ashok
 */

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class ReturnOfInstrumentsHashMap implements UIMandatoryHashMap {
    
    private HashMap mandatoryMap;
    
    public ReturnOfInstrumentsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("chkPresentAgain", new Boolean(false));
        mandatoryMap.put("cboReturnType", new Boolean(true));
        mandatoryMap.put("tdtClearingDate", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("txtBatchId", new Boolean(true));
        mandatoryMap.put("cboClearingType", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
    
}

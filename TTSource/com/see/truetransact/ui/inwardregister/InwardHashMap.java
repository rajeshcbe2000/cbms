/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InwardHashMap.java
 */

package com.see.truetransact.ui.inwardregister;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class InwardHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public InwardHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
        mandatoryMap.put("txtSubmittedBy", new Boolean(true));
        mandatoryMap.put("txtActionTaken", new Boolean(true));
        mandatoryMap.put("txaDetails", new Boolean(false));
        // mandatoryMap.put("tdtLastDay", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

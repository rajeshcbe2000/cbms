/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * OutwardHashMap.java
 */

package com.see.truetransact.ui.outwardregister;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class OutwardHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public OutwardHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txaDetails", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(false));
        mandatoryMap.put("txaRemarks", new Boolean(false));
        mandatoryMap.put("txtOutwardNo", new Boolean(false));
        mandatoryMap.put("cmbMessenger", new Boolean(false));
        mandatoryMap.put("txaAddress", new Boolean(false));
        //mandatoryMap.put("tdtLastDay", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

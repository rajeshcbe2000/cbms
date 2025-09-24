/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * GeneralBodyDetailsHashMap.java
 */

package com.see.truetransact.ui.generalbodydetails;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class GeneralBodyDetailsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public GeneralBodyDetailsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtVenu", new Boolean(true));
        mandatoryMap.put("txtTotalAttandance", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

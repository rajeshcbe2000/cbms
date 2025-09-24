/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigHashMap.java
 *
 * Created on Mon Jan 31 15:41:47 IST 2005
 */

package com.see.truetransact.ui.trading.damage;

import java.util.HashMap;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DamageHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public DamageHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTdsId", new Boolean(false));
        mandatoryMap.put("txtCutOfAmount", new Boolean(true));
        mandatoryMap.put("tdtEndDate", new Boolean(true));
        mandatoryMap.put("txtPercentage", new Boolean(true));
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("cboScope", new Boolean(true));
        mandatoryMap.put("rdoCutOff_Yes", new Boolean(true));
        mandatoryMap.put("txtTdsCreditAchdIdVal", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

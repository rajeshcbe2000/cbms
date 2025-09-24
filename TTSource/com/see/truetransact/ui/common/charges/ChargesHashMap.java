/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesHashMap.java
 * 
 * Created on Thu Dec 23 12:38:39 IST 2004
 */

package com.see.truetransact.ui.common.charges;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ChargesHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ChargesHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtFixRate", new Boolean(false));
        mandatoryMap.put("cboRateType", new Boolean(false));
        mandatoryMap.put("cboChargeType", new Boolean(false));
        mandatoryMap.put("txtFromAmt", new Boolean(false));
        mandatoryMap.put("txtRateVal", new Boolean(false));
        mandatoryMap.put("txtForEvery", new Boolean(false));
        mandatoryMap.put("txtToAmt", new Boolean(false));
        mandatoryMap.put("txtPercent", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexDenominationMasterHashMap.java
 * 
 * Created on Thu Jan 27 12:11:54 IST 2005
 */

package com.see.truetransact.ui.sysadmin.denomination;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ForexDenominationMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ForexDenominationMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCurrency", new Boolean(false));
        mandatoryMap.put("cboDenominationType", new Boolean(true));
        mandatoryMap.put("txtDenominationName", new Boolean(true));
        mandatoryMap.put("txtDenominationValue", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

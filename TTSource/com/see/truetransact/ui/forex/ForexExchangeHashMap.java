/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexExchangeHashMap.java
 * 
 * Created on Wed May 05 12:40:21 IST 2004
 */

package com.see.truetransact.ui.forex;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ForexExchangeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ForexExchangeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMiddleRate", new Boolean(true));
        mandatoryMap.put("tdtValidDate", new Boolean(true));
        mandatoryMap.put("cboHours", new Boolean(true));
        mandatoryMap.put("cboTransCurrency", new Boolean(true));
        mandatoryMap.put("cboMinutes", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

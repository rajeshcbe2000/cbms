/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GoldConfigurationMRB.java
 * 
 * Created on Wed Feb 02 12:56:47 IST 2005
 */

package com.see.truetransact.ui.termloan.goldLoanConfiguration;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class GoldConfigurationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public GoldConfigurationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDate", new Boolean(false));
        mandatoryMap.put("cboPurityOfGold", new Boolean(true));
        mandatoryMap.put("txtPerGramRate", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * stateTalukHashMap.java
 * 
 * Created on 19-05-2009 IST 
 */

package com.see.truetransact.ui.sysadmin.stateTalukMaster;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class StateTalukHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public StateTalukHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtStateName", new Boolean(true));
        mandatoryMap.put("txtStateCode", new Boolean(true));
        mandatoryMap.put("txtTalukName", new Boolean(true));
        mandatoryMap.put("txtTalukCode", new Boolean(true));
        mandatoryMap.put("txtDistrictCode", new Boolean(true));
        mandatoryMap.put("txtDistrictName", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigHashMap.java
 * 
 * Created on Thu Jan 20 16:38:23 IST 2005
 */

package com.see.truetransact.ui.locker.lockerconfig;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class LockerConfigHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public LockerConfigHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProdId", new Boolean(false));
        mandatoryMap.put("txtFromLocNo", new Boolean(false));
        mandatoryMap.put("txtToLocNo", new Boolean(false));
        mandatoryMap.put("txtNoOfLockers", new Boolean(false));
        mandatoryMap.put("txtMasterKey", new Boolean(false));
         mandatoryMap.put("txtLockerKey", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

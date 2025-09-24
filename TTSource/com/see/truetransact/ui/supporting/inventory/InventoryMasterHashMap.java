/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryMasterHashMap.java
 * 
 * Created on Fri Aug 20 14:16:41 IST 2004
 */

package com.see.truetransact.ui.supporting.inventory;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class InventoryMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public InventoryMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboUsage", new Boolean(true));
        mandatoryMap.put("txtReOrderLevel", new Boolean(false));
        //mandatoryMap.put("txtInstrumentPrefix", new Boolean(true));
        mandatoryMap.put("txtDangerLevel", new Boolean(false));
        mandatoryMap.put("cboItemType", new Boolean(true));
        mandatoryMap.put("txtLeavesPerBook", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

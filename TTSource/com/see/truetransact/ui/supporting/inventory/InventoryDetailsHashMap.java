/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryDetailsHashMap.java
 * 
 * Created on Tue Aug 24 10:42:12 IST 2004
 */

package com.see.truetransact.ui.supporting.inventory;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class InventoryDetailsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public InventoryDetailsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBookTo", new Boolean(false));
        mandatoryMap.put("txtChequeFrom", new Boolean(true));
        mandatoryMap.put("txtBookQuantity", new Boolean(true));
        mandatoryMap.put("txtBookFrom", new Boolean(false));
        mandatoryMap.put("txtItemID", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(false));
        mandatoryMap.put("txtChequeTo", new Boolean(false));
        mandatoryMap.put("txtInstrumentPrefix", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

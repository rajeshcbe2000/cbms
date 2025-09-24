/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositClosingHashMap.java
 * 
 * Created on Thu May 20 12:57:57 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.closing;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DepositClosingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public DepositClosingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDepositNo", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueDuplicateHashMap.java
 * 
 * Created on Mon Jun 07 12:36:04 PDT 2004
 */

package com.see.truetransact.ui.locker.lockeroperation.pwdchange;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class PwdChangeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public PwdChangeHashMap(){
        mandatoryMap = new HashMap();        
        mandatoryMap.put("txtDuplicateRemarks", new Boolean(true));
        mandatoryMap.put("txtDuplicationCharge", new Boolean(true));
        mandatoryMap.put("cboTransactionType", new Boolean(false));
        mandatoryMap.put("txtServiceTax", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

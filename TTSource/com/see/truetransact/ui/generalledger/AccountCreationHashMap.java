/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountCreationHashMap.java
 */

package com.see.truetransact.ui.generalledger;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class AccountCreationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public AccountCreationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboSubHead", new Boolean(true));
        mandatoryMap.put("txtAccountHeadCode", new Boolean(true));
        mandatoryMap.put("cboMajorHead", new Boolean(true));
        mandatoryMap.put("txtAccountHeadDesc", new Boolean(true));
        mandatoryMap.put("txtAccountHead", new Boolean(true));
        mandatoryMap.put("cboAccountType", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

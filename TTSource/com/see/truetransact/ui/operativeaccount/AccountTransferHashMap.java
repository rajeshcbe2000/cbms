/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountTransferHashMap.java
 */

package com.see.truetransact.ui.operativeaccount;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class AccountTransferHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public AccountTransferHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductId", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

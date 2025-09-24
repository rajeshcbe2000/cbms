/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BlockedListHashMap.java
 * 
 * Created on Wed Feb 09 14:53:49 IST 2005
 */

package com.see.truetransact.ui.sysadmin.blockedlist;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class BlockedListHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public BlockedListHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboFraudStatus", new Boolean(true));
        mandatoryMap.put("txaBusinessAddress", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(false));
        mandatoryMap.put("txtFraudClassifcation", new Boolean(false));
        mandatoryMap.put("txtBlockedName", new Boolean(true));
        mandatoryMap.put("cboFraudClassification", new Boolean(true));
        mandatoryMap.put("txtBlockedListId", new Boolean(false));
        mandatoryMap.put("cboCustomerType", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

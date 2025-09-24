/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LookupMasterHashMap.java
 */

package com.see.truetransact.ui.sysadmin.lookup;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LookupMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LookupMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLookupDesc", new Boolean(true));
        mandatoryMap.put("txtLookupRef", new Boolean(true));
        mandatoryMap.put("txtLookupID", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

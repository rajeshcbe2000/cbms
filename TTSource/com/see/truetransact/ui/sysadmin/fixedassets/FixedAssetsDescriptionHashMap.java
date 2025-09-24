/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * FixedAssetsDescriptionHashMap.java
 */
package com.see.truetransact.ui.sysadmin.fixedassets;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class FixedAssetsDescriptionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public FixedAssetsDescriptionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAssetType", new Boolean(true));
        mandatoryMap.put("txtAssetSubType", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

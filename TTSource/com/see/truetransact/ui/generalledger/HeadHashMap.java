/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * HeadHashMap.java
 */

package com.see.truetransact.ui.generalledger;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class HeadHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public HeadHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAccountType", new Boolean(true));
        mandatoryMap.put("txtMajorHeadCode1", new Boolean(true));
        mandatoryMap.put("txtMajorHeadCode2", new Boolean(true));
        mandatoryMap.put("txtMajorHeadDesc", new Boolean(false));
        mandatoryMap.put("txtSubHeadCode", new Boolean(true));
        mandatoryMap.put("txtSubHeadDesc", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

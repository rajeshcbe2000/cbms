/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MDSCommencementHashMap.java
 */

package com.see.truetransact.ui.mdsapplication.mdsconmmencement;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MDSCommencementHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MDSCommencementHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtStartDt", new Boolean(true));
        mandatoryMap.put("txtInstAmt", new Boolean(true));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtTotAmt", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

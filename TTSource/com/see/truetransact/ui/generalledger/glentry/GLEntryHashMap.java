/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLEntryHashMap.java
 * 
 * Created on Tue Jan 04 11:01:02 IST 2005
 */

package com.see.truetransact.ui.generalledger.glentry;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class GLEntryHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public GLEntryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAcHead", new Boolean(true));
        mandatoryMap.put("cboAccountHeadStatus", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

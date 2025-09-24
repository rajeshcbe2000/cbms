/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGLHashMap.java
 * 
 * Created on Mon Dec 27 17:05:17 IST 2004
 */

package com.see.truetransact.ui.generalledger.branchgl;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class BranchGLHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public BranchGLHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupId", new Boolean(false));
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("lstAvailGL", new Boolean(false));
        mandatoryMap.put("lstGrantGL", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

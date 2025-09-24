/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * 
 *BranchGroupHashMap.java
 *
 */

package com.see.truetransact.ui.sysadmin.branchgroupscr;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

/**
 *
 * @author  Pinky
 */

public class BranchGroupHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public BranchGroupHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("treGrantScreen", new Boolean(false));
        mandatoryMap.put("txtGrpDesc", new Boolean(true));
        mandatoryMap.put("treAvaiScreen", new Boolean(false));
        mandatoryMap.put("txtGrpID", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

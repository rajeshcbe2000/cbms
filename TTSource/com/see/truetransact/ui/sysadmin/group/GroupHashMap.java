/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 * 
 *GroupHashMap.java
 *
 */

package com.see.truetransact.ui.sysadmin.group;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

/**
 *
 * @author  Pinky
 */

public class GroupHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public GroupHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("treGrantScreen", new Boolean(false));
        mandatoryMap.put("txtGrpDesc", new Boolean(true));
        mandatoryMap.put("treAvaiScreen", new Boolean(false));
        mandatoryMap.put("txtGrpID", new Boolean(false));
        mandatoryMap.put("txtBranchGroup", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

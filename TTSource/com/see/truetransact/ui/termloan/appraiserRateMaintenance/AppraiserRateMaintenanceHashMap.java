/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupHashMap.java
 * 
 * Created on Thu Aug 25 11:05:17 IST 2005
 */

package com.see.truetransact.ui.termloan.appraiserRateMaintenance;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class AppraiserRateMaintenanceHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public AppraiserRateMaintenanceHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupId", new Boolean(false));
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("lstAvailBranch", new Boolean(false));
        mandatoryMap.put("lstGrantedBranch", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

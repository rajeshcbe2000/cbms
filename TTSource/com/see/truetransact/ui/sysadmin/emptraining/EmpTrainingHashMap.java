/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * EmpTrainingHashMap.java
 */

package com.see.truetransact.ui.sysadmin.emptraining;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class EmpTrainingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public EmpTrainingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTrainDest", new Boolean(true));
        mandatoryMap.put("txtLocation", new Boolean(true));
        mandatoryMap.put("txtCondTeam", new Boolean(true));
        mandatoryMap.put("txtSize", new Boolean(true));
        mandatoryMap.put("txtNoOfTrainees", new Boolean(true));
        mandatoryMap.put("tdtFrom", new Boolean(true));
        mandatoryMap.put("tdtTo", new Boolean(true));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

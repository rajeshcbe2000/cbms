/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AcNoMaintenanceHashMap.java
 *
 * Created on February 18, 2009, 01:40 PM
 *
 * AUTHOR : RAJESH.S
 */

package com.see.truetransact.ui.sysadmin.branchacnomaintenance;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class AcNoMaintenanceHashMap implements UIMandatoryHashMap {
    
    private HashMap mandatoryMap;
    
    public AcNoMaintenanceHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboBranches", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("txtLastAcNo", new Boolean(true));
        mandatoryMap.put("txtNextAcNo", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
    
}

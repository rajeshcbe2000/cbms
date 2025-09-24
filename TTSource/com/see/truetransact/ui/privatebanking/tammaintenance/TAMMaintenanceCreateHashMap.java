/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * TAMMaintenanceCreateHashMap.java
 */

package com.see.truetransact.ui.privatebanking.tammaintenance;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class TAMMaintenanceCreateHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public TAMMaintenanceCreateHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboTAMStatus", new Boolean(true));
        mandatoryMap.put("rdoTAMDefaultType_Yes", new Boolean(true));
        mandatoryMap.put("cboAssetClassID", new Boolean(true));
        mandatoryMap.put("cboTAMOrderType", new Boolean(true));
        mandatoryMap.put("cboAssetSubclassID", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

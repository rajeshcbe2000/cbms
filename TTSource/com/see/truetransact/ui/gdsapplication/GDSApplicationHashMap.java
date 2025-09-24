/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * GDSApplicationHashMap.java
 */
package com.see.truetransact.ui.gdsapplication;
import com.see.truetransact.ui.mdsapplication.*;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class GDSApplicationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public GDSApplicationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupName", new Boolean(true));
//        mandatoryMap.put("txtMembershipNo", new Boolean(true));
        mandatoryMap.put("txtMembershipName", new Boolean(true));
        mandatoryMap.put("txtHouseStNo", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
//        mandatoryMap.put("cboCity", new Boolean(true));
//        mandatoryMap.put("cboState", new Boolean(true));
//        mandatoryMap.put("txtpin", new Boolean(true));
//        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtApplnNo", new Boolean(true));
        mandatoryMap.put("tdtApplnDate", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

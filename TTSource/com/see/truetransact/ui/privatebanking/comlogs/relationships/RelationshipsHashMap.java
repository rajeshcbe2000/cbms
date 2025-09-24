/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RelationshipsHashMap.java
 */

package com.see.truetransact.ui.privatebanking.comlogs.relationships;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class RelationshipsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public RelationshipsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("txtBankerName", new Boolean(true));
        mandatoryMap.put("tdtContactDate", new Boolean(true));
        mandatoryMap.put("txtLeadRSO", new Boolean(true));
        mandatoryMap.put("cboSource", new Boolean(true));
        mandatoryMap.put("cboSubType", new Boolean(true));
        mandatoryMap.put("cboType", new Boolean(true));
        mandatoryMap.put("txtSourceReference", new Boolean(true));
        mandatoryMap.put("txtInitiatedBy", new Boolean(true));
        mandatoryMap.put("txaContactDescription", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

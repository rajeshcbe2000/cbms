/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountDeathMarkingHashMap.java
 */

package com.see.truetransact.ui.operativeaccount.deathmarking;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class AccountDeathMarkingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public AccountDeathMarkingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("tdtReportedOn", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("tdtDtOfDeath", new Boolean(true));
        mandatoryMap.put("txtReportedBy", new Boolean(true));
        mandatoryMap.put("cboRelationship", new Boolean(true));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

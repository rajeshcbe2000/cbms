/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DepositRolloverHashMap.java
 */

package com.see.truetransact.ui.privatebanking.orders;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DepositRolloverHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public DepositRolloverHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboOrderType", new Boolean(true));
        mandatoryMap.put("dateContactDate", new Boolean(true));
        mandatoryMap.put("txtDescription", new Boolean(true));
        mandatoryMap.put("txtMember", new Boolean(true));
        mandatoryMap.put("cboSrcDocDetails", new Boolean(true));
        mandatoryMap.put("cboInstructionFrom", new Boolean(true));
        mandatoryMap.put("cboContactMode", new Boolean(true));
        mandatoryMap.put("cboSolicited", new Boolean(true));
        mandatoryMap.put("cboContactTimeHours", new Boolean(true));
        mandatoryMap.put("txtPhoneExtnum", new Boolean(true));
        mandatoryMap.put("txtAuthSrcDoc", new Boolean(true));
        mandatoryMap.put("cboContactTimeMinutes", new Boolean(true));
        mandatoryMap.put("cboClientContact", new Boolean(true));
        mandatoryMap.put("cboRelationship", new Boolean(false));
        mandatoryMap.put("dateSrcDocDate", new Boolean(true));
        mandatoryMap.put("cboViewInfoDoc", new Boolean(true));
        
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
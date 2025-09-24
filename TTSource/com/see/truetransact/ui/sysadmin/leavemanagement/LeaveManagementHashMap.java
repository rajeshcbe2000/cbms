/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LeaveManagementHashMap.java
 */
package com.see.truetransact.ui.sysadmin.leavemanagement;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LeaveManagementHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LeaveManagementHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTypeOfLeave", new Boolean(true));
        mandatoryMap.put("txtDesc", new Boolean(true));
        mandatoryMap.put("cboCreditType", new Boolean(true));
        mandatoryMap.put("rdoIntroReq_Yes1", new Boolean(true));
        mandatoryMap.put("rdoAcc_Yes", new Boolean(true));
        mandatoryMap.put("cboParForLeave", new Boolean(true));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

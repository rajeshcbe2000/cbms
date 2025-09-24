/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LeaveSanctionHashMap.java
 */
package com.see.truetransact.ui.sysadmin.leavemanagement;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LeaveSanctionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LeaveSanctionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSanEmpID", new Boolean(true));
        mandatoryMap.put("cboProcessType", new Boolean(true));
        mandatoryMap.put("tdtLvAplDt", new Boolean(true));
        mandatoryMap.put("tdtReqFrom", new Boolean(true));
        mandatoryMap.put("tdtReqTo", new Boolean(true));
        mandatoryMap.put("txtNoOfDays", new Boolean(true));
        mandatoryMap.put("txtLeavePurpose", new Boolean(false));
        mandatoryMap.put("txtSanRefNo", new Boolean(false));
        mandatoryMap.put("tdtSanDate", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

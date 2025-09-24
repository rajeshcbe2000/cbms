/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * EmpTransferHashMap.java
 */

package com.see.truetransact.ui.sysadmin.emptransfer;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class EmpTransferHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public EmpTransferHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboRoleCurrBran", new Boolean(true));
        mandatoryMap.put("cboRoleTransBran", new Boolean(true));
        mandatoryMap.put("txtEmpID", new Boolean(true));
        mandatoryMap.put("cboTransBran", new Boolean(true));
        mandatoryMap.put("tdtDoj", new Boolean(false));
        mandatoryMap.put("tdtLastDay", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

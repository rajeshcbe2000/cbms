/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.sysadmin.terminal;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class TerminalHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public TerminalHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTerminalId", new Boolean(true));
        mandatoryMap.put("txtBranchCode", new Boolean(true));
        mandatoryMap.put("txtIPAddress", new Boolean(true));
        mandatoryMap.put("txtMachineName", new Boolean(true));
        mandatoryMap.put("txtTerminalDescription", new Boolean(true));
        mandatoryMap.put("txtTerminalName", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

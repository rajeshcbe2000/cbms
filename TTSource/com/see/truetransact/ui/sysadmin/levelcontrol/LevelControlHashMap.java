/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LevelControlHashMap.java
 */
package com.see.truetransact.ui.sysadmin.levelcontrol;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LevelControlHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LevelControlHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLevelID", new Boolean(false));
        mandatoryMap.put("txtDescription", new Boolean(true));
        mandatoryMap.put("txtClearingCredit", new Boolean(true));
        mandatoryMap.put("txtClearingDebit", new Boolean(true));
        mandatoryMap.put("txtCashDebit", new Boolean(true));
        mandatoryMap.put("txtTransferDebit", new Boolean(true));
        mandatoryMap.put("txtCashCredit", new Boolean(true));
        mandatoryMap.put("txtTransferCredit", new Boolean(true));
        mandatoryMap.put("chkSingleWindow", new Boolean(false));
        mandatoryMap.put("cboName", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

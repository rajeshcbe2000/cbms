/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ConfigurationHashMap.java
 * 
 * Created on Fri Feb 11 14:15:44 IST 2005
 */

package com.see.truetransact.ui.sysadmin.config;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ConfigurationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ConfigurationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNo", new Boolean(false));
        mandatoryMap.put("txtMaxLength", new Boolean(false));
        mandatoryMap.put("chkPwdNeverExpire", new Boolean(false));
        mandatoryMap.put("txtAttempts", new Boolean(false));
        mandatoryMap.put("txtSplChar", new Boolean(false));
        mandatoryMap.put("chkCantChangePwd", new Boolean(false));
        mandatoryMap.put("chkAccLocked", new Boolean(false));
        mandatoryMap.put("chkFirstLogin", new Boolean(false));
        mandatoryMap.put("txtMinLength", new Boolean(false));
        mandatoryMap.put("txtDays", new Boolean(false));
        mandatoryMap.put("txtPwds", new Boolean(false));
        mandatoryMap.put("txtUpperCase", new Boolean(false));
        mandatoryMap.put("txtMinorAge", new Boolean(false));
        mandatoryMap.put("txtRetireAge", new Boolean(false));
        mandatoryMap.put("txtCashActHead", new Boolean(false));
        mandatoryMap.put("txtIBRActHead", new Boolean(false));
        mandatoryMap.put("txtPanAmount", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

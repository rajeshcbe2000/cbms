/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChangePasswordHashMap.java
 *
 * Created on Thu Sep 30 16:58:45 GMT+05:30 2004
 */

package com.see.truetransact.ui.login.newpasswd;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ChangePasswordHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public ChangePasswordHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("pwdNewPasswd", new Boolean(true));
        mandatoryMap.put("pwdOldPasswd", new Boolean(true));
        mandatoryMap.put("pwdConfirmPasswd", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
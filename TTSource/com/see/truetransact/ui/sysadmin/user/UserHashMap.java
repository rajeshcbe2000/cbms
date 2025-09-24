/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserHashMap.java
 * 
 * Created on Wed Feb 09 11:37:02 IST 2005
 */

package com.see.truetransact.ui.sysadmin.user;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class UserHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public UserHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtReasonForSuspend", new Boolean(true));
        mandatoryMap.put("txtUserId", new Boolean(true));
        mandatoryMap.put("txtEmailId", new Boolean(false));
        mandatoryMap.put("chkSuspend", new Boolean(false));
        mandatoryMap.put("chkUserSuspend", new Boolean(false));
        mandatoryMap.put("tdtSuspendFrom", new Boolean(false));
        mandatoryMap.put("cboRoleID", new Boolean(true));
        mandatoryMap.put("cboForeignGroupId", new Boolean(false));
        mandatoryMap.put("cboForeignBranchGroup", new Boolean(false));
        mandatoryMap.put("txtTerminalId", new Boolean(false));
        mandatoryMap.put("tdtAccessFromDate", new Boolean(false));
        mandatoryMap.put("txtTerminalName", new Boolean(false));
        mandatoryMap.put("pwdConfirmPassword", new Boolean(true));
        mandatoryMap.put("chkSmartCard", new Boolean(false));
        mandatoryMap.put("cboGroupID", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtIPAddress", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("pwdPassword", new Boolean(true));
        mandatoryMap.put("tdtAccessToDate", new Boolean(false));
        mandatoryMap.put("txtTerminalDescription", new Boolean(false));
        mandatoryMap.put("tdtActivateUser", new Boolean(true));
        mandatoryMap.put("txtBranchID", new Boolean(true));
        mandatoryMap.put("tdtSuspendTo", new Boolean(false));
        mandatoryMap.put("txtCustomerId", new Boolean(true));
        mandatoryMap.put("txtMachineName", new Boolean(false));
        mandatoryMap.put("tdtUserIdExpiresOn", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

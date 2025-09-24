/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RoleHashMap.java
 * 
 * Created on Wed May 11 14:42:03 IST 2005
 */

package com.see.truetransact.ui.sysadmin.role;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class RoleHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public RoleHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupID", new Boolean(false));
        mandatoryMap.put("txtLevelID", new Boolean(true));
        mandatoryMap.put("txtClearingDebit", new Boolean(false));
        mandatoryMap.put("txtCashDebit", new Boolean(false));
        mandatoryMap.put("txtTransCredit", new Boolean(false));
        mandatoryMap.put("cboRoleId", new Boolean(true));
        mandatoryMap.put("txtLevelName", new Boolean(false));
        mandatoryMap.put("txtCashCredit", new Boolean(false));
        mandatoryMap.put("chkAccAllBran", new Boolean(false));
        mandatoryMap.put("txtGroupDesc", new Boolean(false));
        mandatoryMap.put("txtRoleDesc", new Boolean(true));
        mandatoryMap.put("txtLevelDesc", new Boolean(false));
        mandatoryMap.put("txtClearingCredit", new Boolean(false));
        mandatoryMap.put("txtTransDebit", new Boolean(false));
        
        mandatoryMap.put("txtLevelNameForeign", new Boolean(false));
        mandatoryMap.put("txtLevelDescForeign", new Boolean(false));
        mandatoryMap.put("txtCashDebitForeign", new Boolean(false));
        mandatoryMap.put("txtCashCreditForeign", new Boolean(false));
        mandatoryMap.put("txtClearingCreditForeign", new Boolean(false));
        mandatoryMap.put("txtClearingDebitForeign", new Boolean(false));
        mandatoryMap.put("txtTransCreditForeign", new Boolean(false));
        mandatoryMap.put("txtTransDebitForeign", new Boolean(false));
        mandatoryMap.put("txtLevelIDForeign", new Boolean(false));
        mandatoryMap.put("cboRoleHierarchy", new Boolean(true));
        mandatoryMap.put("chkHierarchyAllowed", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

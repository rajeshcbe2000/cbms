/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankHashMap.java
 * 
 * Created on Thu Dec 30 15:42:56 IST 2004
 */

package com.see.truetransact.ui.sysadmin.otherbank;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class OtherBankHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public OtherBankHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtOtherBranchCode", new Boolean(true));
        mandatoryMap.put("txtBankShortName", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(true));
        mandatoryMap.put("txtMICR", new Boolean(true));
        mandatoryMap.put("txtBankName", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
        mandatoryMap.put("txtBankCode", new Boolean(true));
        mandatoryMap.put("txtOtherBranchShortName", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

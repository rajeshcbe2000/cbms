/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositFreezeHashMap.java
 * 
 * Created on Fri Jun 04 11:50:09 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.freeze;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DepositFreezeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public DepositFreezeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtFreezeAmount", new Boolean(true));
        mandatoryMap.put("cboSubDepositNo", new Boolean(true));
        mandatoryMap.put("txtDepositNo", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("tdtFreezeDate", new Boolean(true));
        mandatoryMap.put("cboFreezeType", new Boolean(true));
        mandatoryMap.put("txtRemark", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

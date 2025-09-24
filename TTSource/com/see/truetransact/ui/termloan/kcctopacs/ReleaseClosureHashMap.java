/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SanctionMasterHashMap.java
 * 
 * Created on Fri Feb 15 13:40:48 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ReleaseClosureHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public ReleaseClosureHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtFromReleaseNo", new Boolean(true));
        mandatoryMap.put("txtToReleaseNo", new Boolean(true));
        mandatoryMap.put("cboKCCProdId", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SupplierMasterHashMap.java
 * 
 * Created on Fri Jun 10 15:39:15 IST 2011
 */

package com.see.truetransact.ui.inventory;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class SupplierMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public SupplierMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSupplierName", new Boolean(true));
        mandatoryMap.put("txtAddrRemarks", new Boolean(false));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(false));
        mandatoryMap.put("txtSupplierID", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("txtTinNo", new Boolean(false));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("txtCST", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

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

package com.see.truetransact.ui.paddyprocurement;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class PaddyLocalityMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public PaddyLocalityMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLocalityName", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(false));
        mandatoryMap.put("txtPincode", new Boolean(false));
        mandatoryMap.put("txtLocalityCode", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(false));
        mandatoryMap.put("txtArea", new Boolean(false));
        mandatoryMap.put("cboState", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

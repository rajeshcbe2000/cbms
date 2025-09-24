/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CarrierHashMap.java
 *
 * Created on Wed Jan 05 14:40:56 IST 2005
 */

package com.see.truetransact.ui.bills;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class CarrierHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public CarrierHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCarrierCode", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(true));
        mandatoryMap.put("txtCarrierName", new Boolean(true));
        mandatoryMap.put("chkIsApproved", new Boolean(false));
        mandatoryMap.put("cboCarrierType", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("txtPincode", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

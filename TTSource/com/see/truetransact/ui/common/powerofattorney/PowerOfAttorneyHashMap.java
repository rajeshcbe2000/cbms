/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PowerOfAttorneyHashMap.java
 *
 * Created on December 23, 2004, 5:34 PM
 */

package com.see.truetransact.ui.common.powerofattorney;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  152713
 */
public class PowerOfAttorneyHashMap implements UIMandatoryHashMap{
    private HashMap mandatoryMap;
    /** Creates a new instance of PowerOfAttorneyHashMap */
    public PowerOfAttorneyHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAddrType_PoA", new Boolean(false));
        mandatoryMap.put("txtPoANo", new Boolean(false));
        mandatoryMap.put("txtPoaHolderName", new Boolean(true));
        mandatoryMap.put("cboPoACust", new Boolean(true));
        mandatoryMap.put("tdtPeriodTo_PowerAttroney", new Boolean(false));  // true changed as false by Rajesh.
        mandatoryMap.put("tdtPeriodFrom_PowerAttroney", new Boolean(true));
        mandatoryMap.put("txtPin_PowerAttroney", new Boolean(false));
        mandatoryMap.put("txtState_PowerAttroney", new Boolean(false));
        mandatoryMap.put("cboCity_PowerAttroney", new Boolean(false));
        mandatoryMap.put("txtStreet_PowerAttroney", new Boolean(false));
        mandatoryMap.put("cboCountry_PowerAttroney", new Boolean(false));
        mandatoryMap.put("txtRemark_PowerAttroney", new Boolean(false));
        mandatoryMap.put("txtArea_PowerAttroney", new Boolean(false));
        mandatoryMap.put("txtPhone_PowerAttroney", new Boolean(false));
        mandatoryMap.put("txtCustID_PoA", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
    
}

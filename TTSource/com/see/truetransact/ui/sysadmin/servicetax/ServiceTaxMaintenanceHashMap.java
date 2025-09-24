/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ServiceTaxMaintenanceHashMap.java
 * 
 * Created on Mon Jan 17 17:34:24 IST 2005
 */

package com.see.truetransact.ui.sysadmin.servicetax;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ServiceTaxMaintenanceHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ServiceTaxMaintenanceHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtFromPeriod", new Boolean(false));
        mandatoryMap.put("cboFromPeriod", new Boolean(false));
        mandatoryMap.put("txtToPeriod", new Boolean(false));
        mandatoryMap.put("cboToPeriod", new Boolean(false));
        mandatoryMap.put("txtRateInterest", new Boolean(true));
        mandatoryMap.put("txtPenalInterest", new Boolean(false));
        mandatoryMap.put("cboFromAmount", new Boolean(false));
        mandatoryMap.put("cboToAmount", new Boolean(false));
        mandatoryMap.put("tdtToDate", new Boolean(false));
        mandatoryMap.put("txtAgainstInterest", new Boolean(false));
        mandatoryMap.put("txtLimitAmt", new Boolean(false));
        mandatoryMap.put("txtInterExpiry", new Boolean(false));
        mandatoryMap.put("txtPLR", new Boolean(false));
        mandatoryMap.put("txtFloatingRate", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

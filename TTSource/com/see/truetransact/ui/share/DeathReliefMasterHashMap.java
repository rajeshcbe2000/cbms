/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterHashMap.java
 * 
 * Created on Fri Aug 05 13:51:33 GMT+05:30 2011
 */

package com.see.truetransact.ui.share;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DeathReliefMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public DeathReliefMasterHashMap()
    {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductId", new Boolean(true));
        mandatoryMap.put("txtPaymentAmount", new Boolean(true));
        mandatoryMap.put("txtPaymentHeadName", new Boolean(true));
        mandatoryMap.put("txtDrfName", new Boolean(true));
        mandatoryMap.put("txtActHeadName", new Boolean(true));
        mandatoryMap.put("txtDrfAmount", new Boolean(true));
        mandatoryMap.put("tdtDrfFromDt", new Boolean(true));
        mandatoryMap.put("tdtDrfToDt", new Boolean(true));
    mandatoryMap.put("txtInterestRate",new Boolean(true));
 //  mandatoryMap.put("txtInterestRate",new Boolean(true));
    mandatoryMap.put("cboCalculationFrequency",new Boolean(true));
    mandatoryMap.put("cboCalclulationCriteria",new Boolean(true));
    mandatoryMap.put("cboProductFrequency",new Boolean(true));
    mandatoryMap.put("txtDebitHead",new Boolean(true));
    mandatoryMap.put("tdtCalculatedDt",new Boolean(true));
     mandatoryMap.put("tdtFromDt",new Boolean(true));
     mandatoryMap.put("txtToDt",new Boolean(true));
    
    mandatoryMap.put("txtInterestRate",new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

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

public class DrfTransactionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public DrfTransactionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDrfTransMemberNo", new Boolean(true));
        mandatoryMap.put("txtDrfTransName", new Boolean(false));
        mandatoryMap.put("txtDrfTransAmount", new Boolean(true));
        mandatoryMap.put("cboDrfTransProdID",new Boolean(true));
         mandatoryMap.put("cboCalculationFrequency",new Boolean(true));
         mandatoryMap.put("cboCalclulationCriteria",new Boolean(true));
         mandatoryMap.put("cboProductFrequency",new Boolean(true));
         mandatoryMap.put("txtDebitHead",new Boolean(true));
         mandatoryMap.put("txtResolutionNo",new Boolean(false));
         mandatoryMap.put("tdtResolutionDate", new Boolean(false));
         //mandatoryMap.put("cboDrfTransProdID",new Boolean(true));
        
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSExemptionHashMap.java
 *
 * Created on Tue Feb 01 17:49:36 IST 2005
 */

package com.see.truetransact.ui.tds.tdsexemption;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class TDSExemptionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public TDSExemptionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerId", new Boolean(true));
        mandatoryMap.put("tdtSubmitDate", new Boolean(true));
        mandatoryMap.put("tdtEndDate", new Boolean(true));
        mandatoryMap.put("txtRefNo", new Boolean(true));
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("txtExemptId", new Boolean(false));
        mandatoryMap.put("txtPanNo", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

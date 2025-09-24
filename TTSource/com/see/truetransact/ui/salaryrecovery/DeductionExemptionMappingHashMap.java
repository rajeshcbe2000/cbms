/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DeductionExemptionMappingHashMap.java
 */

package com.see.truetransact.ui.salaryrecovery;
import java.util.HashMap;
import java.util.LinkedHashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DeductionExemptionMappingHashMap implements UIMandatoryHashMap {
    private LinkedHashMap mandatoryMap;
    public DeductionExemptionMappingHashMap(){
        mandatoryMap = new LinkedHashMap();
        mandatoryMap.put("cboProdId", new Boolean(false));
        mandatoryMap.put("txtEmployerRefNo", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(false));
        mandatoryMap.put("txtAccNo", new Boolean(false));
        mandatoryMap.put("cboExemptionMode",new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

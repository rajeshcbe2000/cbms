/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeductionHashMap.java
 * 
 * Created on Wed Jun 02 10:44:04 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DeductionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public DeductionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboSalaryStructureProdId", new Boolean(true));
        mandatoryMap.put("lblSalaryStructureFromDateValue", new Boolean(true));
//        mandatoryMap.put("lblSalaryStructureToDateValue", new Boolean(false));
        mandatoryMap.put("txtSalaryStructureStartingAmt", new Boolean(true));
        mandatoryMap.put("txtSalaryStructureAmtValue", new Boolean(true));
        mandatoryMap.put("txtSalaryStructureIncYearValue", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

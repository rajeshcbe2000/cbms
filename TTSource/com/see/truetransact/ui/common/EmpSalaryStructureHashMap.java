/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * EmpSalaryStructureHashMap.java
 * 
 * Created on Sat Feb 26 14:09:56 GMT+05:30 2011
 */

package com.see.truetransact.ui.common;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class EmpSalaryStructureHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public EmpSalaryStructureHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtSalFromDate", new Boolean(true));
        mandatoryMap.put("rdoBasedOnBasic_Yes", new Boolean(true));
        mandatoryMap.put("txtAllowanceID", new Boolean(true));
        mandatoryMap.put("rdoPercentOrFixed_Percent", new Boolean(true));
        mandatoryMap.put("tdtSalToDate", new Boolean(false));
        mandatoryMap.put("txtFromAmount", new Boolean(true));
        mandatoryMap.put("txtAllowanceAmount", new Boolean(true));
        mandatoryMap.put("rdoEarnOrDed_Earning", new Boolean(true));
        mandatoryMap.put("txtMaxAmount", new Boolean(false));
        mandatoryMap.put("txtAllowanceType", new Boolean(true));
        mandatoryMap.put("txtToAmount", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

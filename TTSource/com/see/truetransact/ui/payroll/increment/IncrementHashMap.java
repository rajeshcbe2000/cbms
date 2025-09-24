/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IncrementHashMap.java
 * 
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.ui.payroll.increment;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class IncrementHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public IncrementHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEmpId", new Boolean(true));
        mandatoryMap.put("txtPresBasicSalary", new Boolean(true));
        mandatoryMap.put("txtLastIncrDate", new Boolean(true));
        mandatoryMap.put("txtEmpName", new Boolean(true));
        mandatoryMap.put("txtDesig", new Boolean(true));
        mandatoryMap.put("txtNextIncrDate", new Boolean(true));
        mandatoryMap.put("txtNumberIncr", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

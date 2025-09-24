/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureHashMap.java
 * 
 * Created on Tue Aug 12 2014
 * 
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.salaryStructure;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class SalaryStructureHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public SalaryStructureHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboDesignation", new Boolean(true));
        mandatoryMap.put("txtScaleId", new Boolean(true));
        mandatoryMap.put("txtVersionNo", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("txtStartingAmount", new Boolean(true));
        mandatoryMap.put("txtEndingAmount", new Boolean(true));
        mandatoryMap.put("txtIncrementAmount", new Boolean(true));
        mandatoryMap.put("txtNoOfIncrements", new Boolean(true));
        mandatoryMap.put("cboIncrementFrequency", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

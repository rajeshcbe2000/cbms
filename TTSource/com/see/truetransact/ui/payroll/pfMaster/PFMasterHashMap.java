/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFMasterHashMap.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.pfMaster;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class PFMasterHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public PFMasterHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEmployeeId", new Boolean(true));
        mandatoryMap.put("txtPfAccountNo", new Boolean(true));
        mandatoryMap.put("tdtPfDate", new Boolean(true));
        mandatoryMap.put("tdtPfOpeningDate", new Boolean(true));
        mandatoryMap.put("txtOpeningBalance", new Boolean(true));
        mandatoryMap.put("txtPfRateOfInterest", new Boolean(true));
        mandatoryMap.put("tdtLastInterestDate", new Boolean(true));
        mandatoryMap.put("txtPfNomineeName", new Boolean(true));
        mandatoryMap.put("txtPfNomineeRelation", new Boolean(true));
        mandatoryMap.put("txtEmployerContribution", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

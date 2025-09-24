/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * NomineeHashMap.java
 * 
 * Created on Fri Dec 24 10:30:25 IST 2004
 */

package com.see.truetransact.ui.common.nominee;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class NomineeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public NomineeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboNomineeStatus", new Boolean(true));
        mandatoryMap.put("cboGCountry", new Boolean(false));
        mandatoryMap.put("tdtMinorDOBNO", new Boolean(true));
        mandatoryMap.put("txtNomineeNameNO", new Boolean(true));
        mandatoryMap.put("txtGPinCode", new Boolean(false));
        mandatoryMap.put("txtGuardianPhoneNO", new Boolean(false));
        mandatoryMap.put("txtGArea", new Boolean(false));
        mandatoryMap.put("txtTotalShareNO", new Boolean(false));
        mandatoryMap.put("txtNomineeACodeNO", new Boolean(false));
        mandatoryMap.put("cboNCountry", new Boolean(false));
        mandatoryMap.put("txtGuardianNameNO", new Boolean(true));
        mandatoryMap.put("txtNStreet", new Boolean(false));
        mandatoryMap.put("cboGState", new Boolean(false));
        mandatoryMap.put("cboNomineeRelationNO", new Boolean(true));
        mandatoryMap.put("txtNArea", new Boolean(false));
        mandatoryMap.put("txtGuardianACodeNO", new Boolean(false));
        mandatoryMap.put("cboNState", new Boolean(false));
        mandatoryMap.put("txtMinNominees", new Boolean(false));
        mandatoryMap.put("cboRelationNO", new Boolean(true));
        mandatoryMap.put("txtGStreet", new Boolean(false));
        mandatoryMap.put("txtNomineePhoneNO", new Boolean(false));
        mandatoryMap.put("txtNPinCode", new Boolean(false));
        mandatoryMap.put("txtNomineeShareNO", new Boolean(true));
        mandatoryMap.put("cboNCity", new Boolean(false));
        mandatoryMap.put("cboGCity", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

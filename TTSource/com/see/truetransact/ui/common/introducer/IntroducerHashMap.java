/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IntroducerHashMap.java
 *
 * Created on Wed Dec 29 10:33:15 IST 2004
 */

package com.see.truetransact.ui.common.introducer;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class IntroducerHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public IntroducerHashMap(){
        mandatoryMap = new HashMap();
        
        mandatoryMap.put("txtAcctNo", new Boolean(true));
        
        mandatoryMap.put("cboDocTypeDD", new Boolean(false));
        mandatoryMap.put("txtDocNoDD", new Boolean(false));
        mandatoryMap.put("txtIssuedByDD", new Boolean(false));
        mandatoryMap.put("tdtIssuedDateDD", new Boolean(true));
        mandatoryMap.put("tdtExpiryDateDD", new Boolean(false));
        
        mandatoryMap.put("txtBankOB", new Boolean(true));
        mandatoryMap.put("txtBranchOB", new Boolean(false));
        mandatoryMap.put("txtAcctNoOB", new Boolean(false));
        mandatoryMap.put("txtNameOB", new Boolean(true));
        
        mandatoryMap.put("txtIntroName", new Boolean(true));
        mandatoryMap.put("txtDesig", new Boolean(false));
        mandatoryMap.put("txtACode", new Boolean(false));
        mandatoryMap.put("txtPhone", new Boolean(false));
        mandatoryMap.put("txtStreet", new Boolean(false));
        mandatoryMap.put("txtArea", new Boolean(false));
        mandatoryMap.put("cboCountry", new Boolean(false));
        mandatoryMap.put("cboState", new Boolean(false));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("txtPinCode", new Boolean(true));
        
        mandatoryMap.put("cboIdentityTypeID", new Boolean(true));
        mandatoryMap.put("txtIssuedAuthID", new Boolean(true));
        mandatoryMap.put("txtDocID", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

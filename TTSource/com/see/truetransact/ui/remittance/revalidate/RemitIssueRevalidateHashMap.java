/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueRevalidateHashMap.java
 * 
 * Created on Mon Jun 07 12:34:26 PDT 2004
 */

package com.see.truetransact.ui.remittance.revalidate;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class RemitIssueRevalidateHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public RemitIssueRevalidateHashMap(){
        mandatoryMap = new HashMap();        
        mandatoryMap.put("txtRevalidationCharge", new Boolean(true));
        mandatoryMap.put("txtRevalidateRemarks", new Boolean(true));
        mandatoryMap.put("tdtDOExpiring", new Boolean(false));
        mandatoryMap.put("cboTransactionType", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

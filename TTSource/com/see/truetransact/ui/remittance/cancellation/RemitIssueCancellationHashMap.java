/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueCancellationHashMap.java
 * 
 * Created on Thu Jun 10 16:52:53 PDT 2004
 */

package com.see.truetransact.ui.remittance.cancellation;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class RemitIssueCancellationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public RemitIssueCancellationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCancellationCharge", new Boolean(true));
        mandatoryMap.put("txtCancellationRemarks", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

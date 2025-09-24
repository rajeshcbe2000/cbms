/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitStopPaymentHashMap.java
 * 
 * Created on Tue Jan 25 12:55:03 IST 2005
 */

package com.see.truetransact.ui.remittance.remitstoppayment;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class RemitStopPaymentHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public RemitStopPaymentHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtReason", new Boolean(true));
        mandatoryMap.put("txtEndVariableNo", new Boolean(false));
        mandatoryMap.put("txtStartDDNo2", new Boolean(true));
        mandatoryMap.put("txtEndDDNo1", new Boolean(true));
        mandatoryMap.put("rdoDDLeaf_Single", new Boolean(false));
        mandatoryMap.put("txtStartDDNo1", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("txtStartVariableNo", new Boolean(false));
        mandatoryMap.put("txtEndDDNo2", new Boolean(true));
//        mandatoryMap.put("txtRevokeReason", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

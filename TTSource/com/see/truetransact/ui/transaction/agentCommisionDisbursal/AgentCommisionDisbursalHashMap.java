/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentHashMap.java
 * 
 * Created on Wed Feb 02 12:56:47 IST 2005
 */

package com.see.truetransact.ui.transaction.agentCommisionDisbursal;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class AgentCommisionDisbursalHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public AgentCommisionDisbursalHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCommision", new Boolean(false));
        mandatoryMap.put("txtCommisionDuringThePeriod", new Boolean(true));
        mandatoryMap.put("txCommisionForThePeriod", new Boolean(false));
        mandatoryMap.put("cboAgentId", new Boolean(true));
//        mandatoryMap.put("txtDeposit No", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

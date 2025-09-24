/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentHashMap.java
 * 
 * Created on Wed Feb 02 12:56:47 IST 2005
 */

package com.see.truetransact.ui.agent;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class AgentHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public AgentHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtAgentID", new Boolean(true));
        mandatoryMap.put("tdtApptDate", new Boolean(true));
        mandatoryMap.put("txtOperativeAccNo", new Boolean(true));
//        mandatoryMap.put("txtDeposit No", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("txtCollSuspACNum", new Boolean(true));
        mandatoryMap.put("txtCommisionCreditedTo", new Boolean(true));
        mandatoryMap.put("cboCreditProductType", new Boolean(true));
        mandatoryMap.put("txtDepositCreditedTo", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

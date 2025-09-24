/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DailyDepositTransHashMap.java
 */
package com.see.truetransact.ui.transaction.dailyDepositTrans;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DailyDepositTransHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public DailyDepositTransHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoTransactionType_Credit", new Boolean(false));
        mandatoryMap.put("cboAgentType", new Boolean(true));
        mandatoryMap.put("txtAccHdId", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtInitiatorChannel", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

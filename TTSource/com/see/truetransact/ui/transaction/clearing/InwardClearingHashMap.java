/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InwardClearingHashMap.java
 * 
 * Created on Tue May 04 16:14:50 IST 2004
 */

package com.see.truetransact.ui.transaction.clearing;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class InwardClearingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public InwardClearingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(false));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(false));
        mandatoryMap.put("txtPayeeName", new Boolean(false));
        mandatoryMap.put("cboInstrumentTypeID", new Boolean(true));
        mandatoryMap.put("txtBranchCodeID", new Boolean(false));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtBankCodeID", new Boolean(false));
        mandatoryMap.put("txtScheduleNumber", new Boolean(false));
        mandatoryMap.put("txtCovtAmount", new Boolean(false));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

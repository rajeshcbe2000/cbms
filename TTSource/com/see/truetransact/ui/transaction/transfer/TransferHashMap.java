/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TransferHashMap.java
 * 
 * Created on Wed May 12 18:34:23 GMT+05:30 2004
 */

package com.see.truetransact.ui.transaction.transfer;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class TransferHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public TransferHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("txtAccountNo", new Boolean(false));
        mandatoryMap.put("cboProductID", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(true));
       // mandatoryMap.put("txtTokenNo", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(false));
        mandatoryMap.put("tdtInstrumentDate", new Boolean(false));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(false));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("cboCurrency", new Boolean(false));
        mandatoryMap.put("cboInstrumentType", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

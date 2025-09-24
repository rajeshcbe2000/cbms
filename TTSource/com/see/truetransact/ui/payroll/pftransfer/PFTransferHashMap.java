/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFTransferHashMap.java
 * 
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.pftransfer;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class PFTransferHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public PFTransferHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtPfNo", new Boolean(true));
        mandatoryMap.put("txtBalance", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("rdoReceipt", new Boolean(true));
        mandatoryMap.put("rdoPayment", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

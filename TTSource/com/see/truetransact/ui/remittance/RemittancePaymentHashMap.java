/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RemittancePaymentHashMap.java
 */

package com.see.truetransact.ui.remittance;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class RemittancePaymentHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public RemittancePaymentHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSerialNumber", new Boolean(true));
        mandatoryMap.put("txtDebitNo", new Boolean(false));
        mandatoryMap.put("txtDebitHead", new Boolean(false));
        mandatoryMap.put("cboPayStatus", new Boolean(true));
        mandatoryMap.put("txtPrintedNo", new Boolean(true));
        mandatoryMap.put("txtCreditNo", new Boolean(false));
        mandatoryMap.put("txtCreditHead", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtPayAmount", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(false));
        mandatoryMap.put("txtCharges", new Boolean(false));
        mandatoryMap.put("txtNumber1", new Boolean(false));
        mandatoryMap.put("txtNumber2", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}


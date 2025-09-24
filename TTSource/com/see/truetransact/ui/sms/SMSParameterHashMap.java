/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SMSParameterHashMap.java
 * 
 * Created on Thu May 03 12:20:26 IST 2012
 */

package com.see.truetransact.ui.sms;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class SMSParameterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public SMSParameterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("chkCreditClearing", new Boolean(false));
        mandatoryMap.put("txtCreditClearingAmt", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("chkDebitCash", new Boolean(false));
        mandatoryMap.put("chkCreditTransfer", new Boolean(false));
        mandatoryMap.put("txtDebitTransferAmt", new Boolean(false));
        mandatoryMap.put("chkDebitClearing", new Boolean(false));
        mandatoryMap.put("txtDebitClearingAmt", new Boolean(false));
        mandatoryMap.put("txtCreditCashAmt", new Boolean(false));
        mandatoryMap.put("chkCreditCash", new Boolean(false));
        mandatoryMap.put("txtCreditTransferAmt", new Boolean(false));
        mandatoryMap.put("chkDebitTransfer", new Boolean(false));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("txtDebitCashAmt", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

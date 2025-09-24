/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SuspiciousConfigHashMap.java
 * 
 * Created on Sat Jan 08 14:53:27 IST 2005
 */

package com.see.truetransact.ui.sysadmin.suspiciousconfig;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class SuspiciousConfigHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public SuspiciousConfigHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAccNo", new Boolean(false));
        mandatoryMap.put("txtCountExceeds", new Boolean(true));
        mandatoryMap.put("txtWorthExceeds", new Boolean(true));
        mandatoryMap.put("cboConfigurationFor", new Boolean(true));
        mandatoryMap.put("cboTransactionType", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(false));
        mandatoryMap.put("txtCustNo", new Boolean(false));
        mandatoryMap.put("txtPeriod", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(false));
        mandatoryMap.put("cboPeriod", new Boolean(true));
        mandatoryMap.put("chkDebitClearing", new Boolean(false));
        mandatoryMap.put("chkDebitTransfer", new Boolean(false));
        mandatoryMap.put("chkDebitCash", new Boolean(false));
        mandatoryMap.put("chkCreditClearing", new Boolean(false));
        mandatoryMap.put("chkCreditTransfer", new Boolean(false));
        mandatoryMap.put("chkCreditCash", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

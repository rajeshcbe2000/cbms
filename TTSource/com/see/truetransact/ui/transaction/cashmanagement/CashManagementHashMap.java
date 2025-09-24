/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CashManagementHashMap.java
 * 
 * Created on Sat Jan 29 10:30:13 IST 2005
 */

package com.see.truetransact.ui.transaction.cashmanagement;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class CashManagementHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public CashManagementHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoTranscationType_Receipt", new Boolean(false));
        mandatoryMap.put("txtReceivingCashierID", new Boolean(false));
        mandatoryMap.put("txtIssueCashierID", new Boolean(false));
        mandatoryMap.put("txtCashBoxBalance", new Boolean(false));
        mandatoryMap.put("txtTransBalance", new Boolean(false));
        mandatoryMap.put("rdoVaultCash_Yes", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class AccountClosingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public AccountClosingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNoOfUnusedChequeLeafs", new Boolean(true));
        mandatoryMap.put("txtAccountClosingCharges", new Boolean(true));
        mandatoryMap.put("txtChargeDetails", new Boolean(true));
        mandatoryMap.put("txtPayableBalance", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtInterestPayable", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

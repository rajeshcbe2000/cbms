/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * TransactionHashMap.java
 * 
 * Created on Wed Jan 19 16:13:29 IST 2005
 */

package com.see.truetransact.ui.common.transaction;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class TransactionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public TransactionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTotalCash", new Boolean(false));
        mandatoryMap.put("txtTotalTransfer", new Boolean(false));
        mandatoryMap.put("txtChequeNo", new Boolean(false));
        mandatoryMap.put("txtChequeNo2", new Boolean(false));
        mandatoryMap.put("txtApplicantsName", new Boolean(true));
        mandatoryMap.put("txtDebitAccNo", new Boolean(false));
        mandatoryMap.put("txtTotalTransactionAmt", new Boolean(false));
        mandatoryMap.put("txtTransProductId", new Boolean(false));
        mandatoryMap.put("tdtChequeDate", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtTransactionAmt", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(false));        
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AuditEntryHashMap.java
 */

package com.see.truetransact.ui.transaction.auditEntry;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class AuditEntryHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public AuditEntryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtInstrumentDate", new Boolean(false));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(false));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(false));
        mandatoryMap.put("txtTokenNo", new Boolean(false));
        mandatoryMap.put("txtParticulars", new Boolean(false));
        mandatoryMap.put("cboInputCurrency", new Boolean(false));
        mandatoryMap.put("rdoTransactionType_Debit", new Boolean(false));
        mandatoryMap.put("cboInstrumentType", new Boolean(false));
        mandatoryMap.put("cboProdId", new Boolean(false));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("txtAccHdId", new Boolean(true));
        mandatoryMap.put("txtInputAmt", new Boolean(false));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAccNo", new Boolean(false));
        mandatoryMap.put("txtInitiatorChannel", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

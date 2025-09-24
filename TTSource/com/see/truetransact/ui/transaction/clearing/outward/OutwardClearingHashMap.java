/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * OutwardClearingHashMap.java
 */
package com.see.truetransact.ui.transaction.clearing.outward;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class OutwardClearingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public OutwardClearingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAmountID", new Boolean(true));
        mandatoryMap.put("txtAmountPISD", new Boolean(true));
        mandatoryMap.put("txtConvAmt", new Boolean(true));
        mandatoryMap.put("cboClearingTypeID", new Boolean(true));
        mandatoryMap.put("cboInstrumentTypeID", new Boolean(true));
        mandatoryMap.put("cboInstrDetailsCurrency", new Boolean(false));
        mandatoryMap.put("txtDrawerAccNoID", new Boolean(false));
        mandatoryMap.put("txtDrawerNameID", new Boolean(false));
        mandatoryMap.put("txtBranchCodeID", new Boolean(true));
        mandatoryMap.put("txtBankCodeID", new Boolean(true));
        mandatoryMap.put("cboProdIdPISD", new Boolean(true));
        mandatoryMap.put("dtdInstrumentDtID", new Boolean(true));
        mandatoryMap.put("txtRemarksPISD", new Boolean(false));
        mandatoryMap.put("txtBatchIdOC", new Boolean(true));
        mandatoryMap.put("txtAccNoPISD", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1ID", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2ID", new Boolean(true));
        mandatoryMap.put("txtRemarksID", new Boolean(false));
        mandatoryMap.put("txtPayeeNameID", new Boolean(false));
        mandatoryMap.put("cboScheduleNo",new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

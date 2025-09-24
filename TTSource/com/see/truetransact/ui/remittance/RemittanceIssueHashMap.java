/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceIssueHashMap.java
 * 
 * Created on Fri Nov 05 11:55:23 PST 2004
 */

package com.see.truetransact.ui.remittance;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class RemittanceIssueHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public RemittanceIssueHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTotalTransfer", new Boolean(true));
        mandatoryMap.put("txtDebitAccNo", new Boolean(false));
        mandatoryMap.put("txtTotalTransactionAmt", new Boolean(true));
        mandatoryMap.put("cboCrossing", new Boolean(false));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("tdtChequeDate", new Boolean(false));
        mandatoryMap.put("txtAmt", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("txtTotalCash", new Boolean(true));
        mandatoryMap.put("txtOtherCharges", new Boolean(false));
        mandatoryMap.put("txtExchange", new Boolean(true));
        mandatoryMap.put("txtApplicantsName", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("txtPayeeAccHead", new Boolean(false));
        mandatoryMap.put("txtTotalInstruments", new Boolean(false));
        mandatoryMap.put("cboTransmissionType", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("txtTransProductId", new Boolean(false));
        mandatoryMap.put("txtPayeeAccNo", new Boolean(false));
        mandatoryMap.put("txtPostage", new Boolean(false));
        mandatoryMap.put("txtVariableNo", new Boolean(false));
        mandatoryMap.put("txtPANGIRNo", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboBranchCode", new Boolean(true));
        mandatoryMap.put("txtTransactionAmt", new Boolean(true));
        mandatoryMap.put("txtFavouring", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo2", new Boolean(true));
        mandatoryMap.put("txtTotalamt", new Boolean(false));
        mandatoryMap.put("txtChequeNo", new Boolean(false));
        mandatoryMap.put("txtTotalAmt", new Boolean(false));
        mandatoryMap.put("cboDraweeBank", new Boolean(true));
        mandatoryMap.put("txtInstrumentNo1", new Boolean(true));
        mandatoryMap.put("txtRevalidationCharge", new Boolean(false));
        mandatoryMap.put("txtDuplicationCharge", new Boolean(false));
        mandatoryMap.put("cboPayeeProdType", new Boolean(false));
        mandatoryMap.put("cboPayeeProdId", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

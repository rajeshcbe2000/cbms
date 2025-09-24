/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CurrencyExchangeHashMap.java
 */

package com.see.truetransact.ui.forex;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class CurrencyExchangeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public CurrencyExchangeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustRemarks", new Boolean(false));
        mandatoryMap.put("txtTouristName", new Boolean(false));
        mandatoryMap.put("txtTransAmount", new Boolean(true));
        mandatoryMap.put("txtAcctNo", new Boolean(false));
        mandatoryMap.put("txtDiaCrossCcyRate", new Boolean(false));
        mandatoryMap.put("txtBranchCode", new Boolean(false));
        mandatoryMap.put("txtTouristBankDetails", new Boolean(false));
        mandatoryMap.put("txtExchangeRate", new Boolean(true));
        mandatoryMap.put("txtTouristPassportNo", new Boolean(false));
        mandatoryMap.put("rdoTransType_Deposit", new Boolean(true));
        mandatoryMap.put("cboTransCurrency", new Boolean(true));
        mandatoryMap.put("txtCustGroup", new Boolean(false));
        mandatoryMap.put("txtTouristRemarks", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtTotalAmount", new Boolean(false));
        mandatoryMap.put("txtTouristInstrumentNo", new Boolean(false));
        mandatoryMap.put("txtDiaTotAmt", new Boolean(false));
        mandatoryMap.put("txtDiaEquiAmt", new Boolean(false));
        mandatoryMap.put("txtType", new Boolean(false));
        mandatoryMap.put("txtName", new Boolean(false));
        mandatoryMap.put("txtDiaComm", new Boolean(false));
        mandatoryMap.put("cboConvCurrency", new Boolean(true));
        mandatoryMap.put("txtCommission", new Boolean(true));
        mandatoryMap.put("txtDiaTransAmt", new Boolean(false));
        mandatoryMap.put("txtValueDate", new Boolean(false));
        mandatoryMap.put("txtTransId", new Boolean(false));
        mandatoryMap.put("cboCustType", new Boolean(true));
        mandatoryMap.put("tdtTouristInstrumentDt", new Boolean(false));
        mandatoryMap.put("txtFromBranch", new Boolean(true));
        mandatoryMap.put("txtImpTrID", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

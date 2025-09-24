/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ExchangeRateHashMap.java
 * 
 * Created on Tue May 04 16:41:33 GMT+05:30 2004
 */

package com.see.truetransact.ui.forex;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ExchangeRateHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ExchangeRateHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBillBuyingSlab", new Boolean(true));
        mandatoryMap.put("txtDDBuying", new Boolean(true));
        mandatoryMap.put("txtDDBuyingComm", new Boolean(true));
        mandatoryMap.put("txtTTBuying", new Boolean(true));
        mandatoryMap.put("txtCurrBuyingSlab", new Boolean(true));
        mandatoryMap.put("txtDDSelling", new Boolean(true));
        mandatoryMap.put("txtNotionalRate", new Boolean(false));
        mandatoryMap.put("txtTCBuying", new Boolean(true));
        mandatoryMap.put("txtDDBuyingSlab", new Boolean(true));
        mandatoryMap.put("txtBillBuyingComm", new Boolean(true));
        mandatoryMap.put("txtTTSelling", new Boolean(true));
        mandatoryMap.put("txtTCBuyingSlab", new Boolean(true));
        mandatoryMap.put("cboTransCurrency", new Boolean(false));
        mandatoryMap.put("txtCurrSelling", new Boolean(true));
        mandatoryMap.put("txtBillBuying", new Boolean(true));
        mandatoryMap.put("txtBillSelling", new Boolean(true));
        mandatoryMap.put("txtTTBuyingComm", new Boolean(true));
        mandatoryMap.put("txtTCSelling", new Boolean(true));
        mandatoryMap.put("cboBuySellType", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtCurrBuying", new Boolean(true));
        mandatoryMap.put("txtExchangeId", new Boolean(false));
        mandatoryMap.put("cboConversionCurrency", new Boolean(false));
        mandatoryMap.put("txtTTBuyingSlab", new Boolean(true));
        mandatoryMap.put("txtTCBuyingComm", new Boolean(true));
        mandatoryMap.put("cboCustomerType", new Boolean(false));
        mandatoryMap.put("chkPreferred", new Boolean(false));
        mandatoryMap.put("txtCurrBuyingComm", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

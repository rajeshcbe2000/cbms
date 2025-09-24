/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigHashMap.java
 *
 * Created on Mon Jan 31 15:41:47 IST 2005
 */

package com.see.truetransact.ui.trading.tradingsuppliermaster;

import java.util.HashMap;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class TradingSupplierMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public TradingSupplierMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTdsId", new Boolean(false));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtAddress", new Boolean(true));
        mandatoryMap.put("txtSundryCreditors", new Boolean(true));
        mandatoryMap.put("txtPurchase", new Boolean(true));
        mandatoryMap.put("tdtdDate", new Boolean(true));
        mandatoryMap.put("txtPhone", new Boolean(true));
        mandatoryMap.put("txtCSTNO", new Boolean(true));
        mandatoryMap.put("txtKGSTNO", new Boolean(true));
        mandatoryMap.put("txtTinNo", new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

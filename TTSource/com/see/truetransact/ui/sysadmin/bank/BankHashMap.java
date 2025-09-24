/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * BankHashMap.java
 */

package com.see.truetransact.ui.sysadmin.bank;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class BankHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public BankHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtBankCode", new Boolean(true));
        mandatoryMap.put("txtBankName", new Boolean(true));
        mandatoryMap.put("txtWebsite", new Boolean(true));
        mandatoryMap.put("txtSiteIP", new Boolean(true));
        mandatoryMap.put("txtDataIP", new Boolean(true));
        mandatoryMap.put("txtCashLimit", new Boolean(false));
        mandatoryMap.put("cboConversion", new Boolean(false));
        mandatoryMap.put("cboTranPosting", new Boolean(false));
        mandatoryMap.put("rdoT2T_Yes", new Boolean(false));
        mandatoryMap.put("rdoB2B_Yes", new Boolean(false));
        mandatoryMap.put("cboHours", new Boolean(true));
        mandatoryMap.put("cboMins", new Boolean(true));
        mandatoryMap.put("cboBaseCurrency", new Boolean(false));
        mandatoryMap.put("txtSupportEmail", new Boolean(true));
        mandatoryMap.put("tdtBankOpeningDate", new Boolean(true));
        
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

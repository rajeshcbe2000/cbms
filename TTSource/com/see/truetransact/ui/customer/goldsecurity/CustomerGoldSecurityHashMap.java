/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SecurityInsuranceHashMap.java
 * 
 * Created on Wed Jan 12 18:32:40 IST 2005
 */

package com.see.truetransact.ui.customer.goldsecurity;

import com.see.truetransact.ui.customer.security.*;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class CustomerGoldSecurityHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public CustomerGoldSecurityHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboInsuranceNo", new Boolean(false));
        mandatoryMap.put("txtSecurityValue", new Boolean(true));
        mandatoryMap.put("txtWeight", new Boolean(false));
        mandatoryMap.put("txtPolicyAmt", new Boolean(true));
        mandatoryMap.put("tdtPolicyDate", new Boolean(true));
        mandatoryMap.put("txtRemark_Insurance", new Boolean(false));
        mandatoryMap.put("txtTotalSecurity_Value", new Boolean(false));
        mandatoryMap.put("txtAvalSecVal", new Boolean(true));
        mandatoryMap.put("txtPremiumAmt", new Boolean(true));
        mandatoryMap.put("txtPolicyNumber", new Boolean(true));
        mandatoryMap.put("txtParticulars", new Boolean(true));
        mandatoryMap.put("cboSecurityCate", new Boolean(true));
        mandatoryMap.put("tdtAson", new Boolean(true));
        mandatoryMap.put("chkSelCommodityItem", new Boolean(false));
        mandatoryMap.put("cboForMillIndus", new Boolean(true));
        mandatoryMap.put("cboSecurityNo_Insurance", new Boolean(false));
        mandatoryMap.put("txtSecurityNo", new Boolean(false));
        mandatoryMap.put("tdtDateCharge", new Boolean(true));
        mandatoryMap.put("cboStockStateFreq", new Boolean(true));
        mandatoryMap.put("rdoSecurityType_Primary", new Boolean(true));
        mandatoryMap.put("txtInsureCompany", new Boolean(true));
        mandatoryMap.put("tdtDateInspection", new Boolean(true));
        mandatoryMap.put("tdtExpityDate", new Boolean(true));
        mandatoryMap.put("cboNatureCharge", new Boolean(true));
        mandatoryMap.put("cboNatureRisk", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

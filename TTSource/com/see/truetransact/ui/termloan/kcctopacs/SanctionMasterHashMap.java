/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SanctionMasterHashMap.java
 * 
 * Created on Fri Feb 15 13:40:48 IST 2013
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class SanctionMasterHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public SanctionMasterHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMisc1", new Boolean(false));
        mandatoryMap.put("txtTotalShareAmount", new Boolean(false));
        mandatoryMap.put("txtOralLessees", new Boolean(false));
        mandatoryMap.put("txtMisc2", new Boolean(false));
        mandatoryMap.put("txtFinancialYearEnd", new Boolean(true));
        mandatoryMap.put("txtTenantFarmers", new Boolean(false));
        mandatoryMap.put("txtSmallFarmers", new Boolean(false));
        mandatoryMap.put("txtKCCAccNo", new Boolean(true));
        mandatoryMap.put("txtOthers", new Boolean(false));
        mandatoryMap.put("txtCustID", new Boolean(true));
        mandatoryMap.put("txtOthersMain", new Boolean(false));
        mandatoryMap.put("cboFromAmount", new Boolean(true));
        mandatoryMap.put("txtMarginalFarmers", new Boolean(false));
        mandatoryMap.put("txtTotMembers", new Boolean(true));
        mandatoryMap.put("txtWomen", new Boolean(false));
        mandatoryMap.put("tdtExpiryDt", new Boolean(true));
        mandatoryMap.put("cboToAmount", new Boolean(true));
        mandatoryMap.put("txtSC", new Boolean(false));
        mandatoryMap.put("txtSanctionAmt", new Boolean(true));
        mandatoryMap.put("cboKCCProdId", new Boolean(true));
        mandatoryMap.put("txtNoOfMembers", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtCAAccNo", new Boolean(true));
        mandatoryMap.put("txtTotalNoOfShare", new Boolean(false));
        mandatoryMap.put("cboCAProdType", new Boolean(true));
        mandatoryMap.put("cboCAProdId", new Boolean(true));
        mandatoryMap.put("txtEditTermLoanNo", new Boolean(false));
        mandatoryMap.put("txtST", new Boolean(false));
        mandatoryMap.put("txtMinorityCommunity", new Boolean(false));
        mandatoryMap.put("tdtSanctionDt", new Boolean(true));
        mandatoryMap.put("cboKCCProdType", new Boolean(true));
        mandatoryMap.put("txtSubLimitAmt", new Boolean(true));
        mandatoryMap.put("txtFinancialYear", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("cboSubCategory", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtSlabNoOfMembers", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

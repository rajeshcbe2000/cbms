/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarningsDeductionHashMap.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.earningsDeductionGlobal;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class EarningsDeductionHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public EarningsDeductionHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboModuleType", new Boolean(true));
        mandatoryMap.put("txtDescription", new Boolean(true));
        mandatoryMap.put("cboCalculationType", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("chkActive", new Boolean(false));
        mandatoryMap.put("chkExcludeFromTax", new Boolean(false));
        mandatoryMap.put("chkPaymentVoucherRequired", new Boolean(false));
        mandatoryMap.put("chkIndividualRequired", new Boolean(false));
        mandatoryMap.put("chkOnlyForContra", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("txtAccountHead", new Boolean(false));
        mandatoryMap.put("cboAccountType", new Boolean(false));
        mandatoryMap.put("cboCalcModType", new Boolean(false));
        mandatoryMap.put("txtMinAmt", new Boolean(false));
        mandatoryMap.put("txtMaxAmt", new Boolean(false));
        mandatoryMap.put("txtPercentage", new Boolean(false));
        mandatoryMap.put("chkIncludePersonalPay", new Boolean(false));
        mandatoryMap.put("tdtFromDate", new Boolean(false));
        mandatoryMap.put("chkGl", new Boolean(false));
        mandatoryMap.put("cboProdType", new Boolean(false));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

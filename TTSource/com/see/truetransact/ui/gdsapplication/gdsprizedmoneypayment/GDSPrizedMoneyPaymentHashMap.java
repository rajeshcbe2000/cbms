/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * GDSPrizedMoneyPaymentHashMap.java
 */
package com.see.truetransact.ui.gdsapplication.gdsprizedmoneypayment;
import com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.*;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class GDSPrizedMoneyPaymentHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public GDSPrizedMoneyPaymentHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtDivisionNo", new Boolean(false));
        mandatoryMap.put("txtTotalInst", new Boolean(false));
        mandatoryMap.put("txtNoOfInstPaid", new Boolean(false));
        mandatoryMap.put("txtCommisionAmt", new Boolean(false));
        mandatoryMap.put("txtBonusAmt", new Boolean(false));
        mandatoryMap.put("txtPrizedInstNo", new Boolean(false));
        mandatoryMap.put("txtOverDueAmt", new Boolean(false));
        mandatoryMap.put("txtNetAmt", new Boolean(false));
        mandatoryMap.put("txtAribitrationAmt", new Boolean(false));
        mandatoryMap.put("txtDiscountAmt", new Boolean(false));
        mandatoryMap.put("txtPrizedAmt", new Boolean(false));
        mandatoryMap.put("txtNoticeAmt", new Boolean(false));
        mandatoryMap.put("txtChargeAmount", new Boolean(false));
        mandatoryMap.put("txtOverdueInst", new Boolean(false));
        mandatoryMap.put("tdtDrawDate", new Boolean(false));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtChittalNo1", new Boolean(false));
        mandatoryMap.put("txtApplicantsName", new Boolean(false));
        mandatoryMap.put("cboTransType", new Boolean(false));
        mandatoryMap.put("txtTransactionAmt", new Boolean(false));
        mandatoryMap.put("txtTransProductId", new Boolean(false));
        mandatoryMap.put("cboProductType", new Boolean(false));
        mandatoryMap.put("txtDebitAccNo", new Boolean(false));
        mandatoryMap.put("tdtChequeDate", new Boolean(false));
        mandatoryMap.put("txtChequeNo", new Boolean(false));
        mandatoryMap.put("txtChequeNo2", new Boolean(false));
        mandatoryMap.put("cboInstrumentType", new Boolean(false));
        mandatoryMap.put("txtTokenNo", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

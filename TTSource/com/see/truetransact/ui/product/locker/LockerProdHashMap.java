/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsHashMap.java
 * 
 * Created on Sat Feb 05 14:31:44 IST 2005
 */

package com.see.truetransact.ui.product.locker;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class LockerProdHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public LockerProdHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductId", new Boolean(true));
        mandatoryMap.put("txtProdDesc", new Boolean(true));
        mandatoryMap.put("txtLength", new Boolean(true));
        mandatoryMap.put("txtBreth", new Boolean(true));
        mandatoryMap.put("txtHeight", new Boolean(true));
        mandatoryMap.put("cboMetric", new Boolean(true));
        mandatoryMap.put("txtLockerRentAcHd", new Boolean(true));
        mandatoryMap.put("rdoPenalToBeCollected", new Boolean(true));
        mandatoryMap.put("txtDefaultPostage", new Boolean(false));
        mandatoryMap.put("rdoPostDtdCheqAllowed_Yes", new Boolean(false));
        mandatoryMap.put("txtDiscountRateBills", new Boolean(false));
        mandatoryMap.put("txtTelChargesHead1", new Boolean(false));
        mandatoryMap.put("txtOverdueRateBills", new Boolean(false));
        mandatoryMap.put("txtCleanBills", new Boolean(false));
        mandatoryMap.put("txtRateForCBP", new Boolean(false));
        mandatoryMap.put("txtMarginAccountHead", new Boolean(false));
        mandatoryMap.put("txtAtParLimit", new Boolean(false));
        mandatoryMap.put("cboOperatesLike", new Boolean(false));
        mandatoryMap.put("RegType", new Boolean(true));
        mandatoryMap.put("SubRegType", new Boolean(true));
        mandatoryMap.put("txtIBRAccountHead", new Boolean(false));
        mandatoryMap.put("cboBaseCurrency", new Boolean(false));
        mandatoryMap.put("txtInterestAccountHead", new Boolean(false));
        mandatoryMap.put("txtGLAccountHead", new Boolean(false));
        mandatoryMap.put("txtBillsRealisedHead", new Boolean(false));
        mandatoryMap.put("txtDDAccountHead", new Boolean(false));
        mandatoryMap.put("txtPostageAccountHead", new Boolean(false));
        mandatoryMap.put("txtTransitPeriod", new Boolean(false));
        mandatoryMap.put("cboTransitPeriod", new Boolean(false));
        mandatoryMap.put("txtCommissionAccountHead", new Boolean(false));
        mandatoryMap.put("rdoContraAccountHead_Yes", new Boolean(false));
        mandatoryMap.put("txtContractAccountHead", new Boolean(false));
        mandatoryMap.put("txtChargesAccountHead", new Boolean(false));
        mandatoryMap.put("txtOthersHead", new Boolean(false));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
//        mandatoryMap.put("cboChargeType", new Boolean(true));
        mandatoryMap.put("cboCustCategory", new Boolean(true));
        mandatoryMap.put("txtFromSlab", new Boolean(true));
        mandatoryMap.put("txtToSlab", new Boolean(true));
        mandatoryMap.put("txtCommision", new Boolean(false));
        mandatoryMap.put("txtServiceTax", new Boolean(false));
        
        
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}
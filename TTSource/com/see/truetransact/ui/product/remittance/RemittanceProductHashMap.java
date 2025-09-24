/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RemittanceProductHashMap.java
 */
package com.see.truetransact.ui.product.remittance;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class RemittanceProductHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public RemittanceProductHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductIdGR", new Boolean(true));
        mandatoryMap.put("txtProductDescGR", new Boolean(true));
        mandatoryMap.put("cboProdCurrency", new Boolean(false));
        mandatoryMap.put("txtIssueHeadGR", new Boolean(true));
        mandatoryMap.put("txtExchangeHeadGR", new Boolean(false));
        mandatoryMap.put("txtTCHeadGR", new Boolean(false));
        mandatoryMap.put("txtRCHeadGR", new Boolean(false));
        mandatoryMap.put("txtOCHeadGR", new Boolean(false));
        mandatoryMap.put("txtPayableHeadGR", new Boolean(true));
        mandatoryMap.put("txtPostageHeadGR", new Boolean(false));
        mandatoryMap.put("txtDCHeadGR", new Boolean(false));
        mandatoryMap.put("txtCCHeadGR", new Boolean(false));
        mandatoryMap.put("rdoIsLapsedGR_Yes", new Boolean(true));
        mandatoryMap.put("txtLapsedHeadGR", new Boolean(false));
        mandatoryMap.put("txtLapsedPeriodGR", new Boolean(false));
        mandatoryMap.put("cboLapsedPeriodGR", new Boolean(false));
        mandatoryMap.put("rdoEFTProductGR_Yes", new Boolean(true));
        mandatoryMap.put("cboPayableBranchGR", new Boolean(true));
        mandatoryMap.put("rdoPrintServicesGR_Yes", new Boolean(true));
        mandatoryMap.put("rdoSeriesGR_Yes", new Boolean(false));
        mandatoryMap.put("txtPerfix", new Boolean(false));
        mandatoryMap.put("txtSuffix", new Boolean(false));
        mandatoryMap.put("txtCashLimitGR", new Boolean(false));
        mandatoryMap.put("txtRemarksGR", new Boolean(false));
        mandatoryMap.put("txtBankCode", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtBranchCodeBR", new Boolean(true));
        mandatoryMap.put("cboRttTypeBR", new Boolean(true));
        mandatoryMap.put("txtRttLimitBR", new Boolean(true));
        mandatoryMap.put("txtIVNoRR", new Boolean(true));
        mandatoryMap.put("txtOVNoRR", new Boolean(true));
        mandatoryMap.put("txtMinAmtRR", new Boolean(false));
        mandatoryMap.put("txtMaxAmtRR", new Boolean(false));
        mandatoryMap.put("cboChargeType", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("txtAmtRangeFrom", new Boolean(true));
        mandatoryMap.put("txtAmtRangeTo", new Boolean(true));
        mandatoryMap.put("txtCharges", new Boolean(false));
        mandatoryMap.put("cboBehavesLike", new Boolean(true));
        mandatoryMap.put("txtPercentage", new Boolean(false));
        mandatoryMap.put("txtServiceTax", new Boolean(false));
        mandatoryMap.put("txtRateVal", new Boolean(false));
        mandatoryMap.put("txtForEvery", new Boolean(false));
        mandatoryMap.put("cboRateType", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

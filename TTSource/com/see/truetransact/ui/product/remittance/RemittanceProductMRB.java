/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductMRB.java
 *
 * Created on Thu Jun 24 18:03:43 GMT+05:30 2004
 */

package com.see.truetransact.ui.product.remittance;

import java.util.ListResourceBundle;

public class RemittanceProductMRB extends ListResourceBundle {
    public RemittanceProductMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtBranchCodeBR", "BranchCodeBR should not be empty!!!"},
        {"txtOVNoRR", "OVNoRR should not be empty!!!"},
        {"txtRttLimitBR", "RttLimitBR should not be empty!!!"},
        {"txtAmtRangeFrom", "AmtRangeFrom should be a proper value!!!"},
        {"txtOCHeadGR", "OCHeadGR should not be empty!!!"},
        {"txtSuffix", "Suffix should not be empty!!!"},
        {"txtBankCode", "BankCode should not be empty!!!"},
        {"txtExchangeHeadGR", "ExchangeHeadGR should not be empty!!!"},
        {"txtMinAmtRR", "MinAmtRR should not be empty!!!"},
        {"txtTCHeadGR", "TCHeadGR should not be empty!!!"},
        {"txtCashLimitGR", "CashLimitGR should not be empty!!!"},
        {"txtMaximumAmount", "Maximum Amount should not be empty!!!"},
        {"txtMinimumAmount", "Minimum Amount should not be empty!!!"},
        {"txtLapsedHeadGR", "LapsedHeadGR should not be empty!!!"},
        {"rdoEFTProductGR_Yes", "EFTProductGR should be selected!!!"},
        {"txtPayableHeadGR", "PayableHeadGR should not be empty!!!"},
        {"cboCategory", "Category should be a proper value!!!"},
        {"txtLapsedPeriodGR", "LapsedPeriodGR should not be empty!!!"},
        {"txtDCHeadGR", "DCHeadGR should not be empty!!!"},
        {"cboProdCurrency", "ProdCurrency should be a proper value!!!"},
        {"txtProductIdGR", "ProductIdGR should not be empty!!!"},
        {"txtMaxAmtRR", "MaxAmtRR should not be empty!!!"},
        {"rdoPrintServicesGR_Yes", "PrintServicesGR should be selected!!!"},
        {"txtBranchName", "BranchName should not be empty!!!"},
        {"txtProductDescGR", "ProductDescGR should not be empty!!!"},
        {"cboPayableBranchGR", "PayableBranchGR should be a proper value!!!"},
        {"txtRCHeadGR", "RCHeadGR should not be empty!!!"},
        {"txtIVNoRR", "IVNoRR should not be empty!!!"},
        {"txtCharges", "Charges should not be empty!!!"},
        {"cboLapsedPeriodGR", "LapsedPeriodGR should be a proper value!!!"},
        {"txtIssueHeadGR", "IssueHeadGR should not be empty!!!"},
        {"cboChargeType", "ChargeType should be a proper value!!!"},
        {"txtRemarksGR", "RemarksGR should not be empty!!!"},
        {"txtPostageHeadGR", "PostageHeadGR should not be empty!!!"},
        {"rdoSeriesGR_Yes", "SeriesGR should be selected!!!"},
        {"rdoIsLapsedGR_Yes", "IsLapsedGR should be selected!!!"},
        {"txtAmtRangeTo", "AmtRangeTo should be a proper value!!!"},
        {"cboBehavesLike", "BehavesLike should be a proper value!!!"},
        {"cboRttTypeBR", "RttTypeBR should be a proper value!!!"},
        {"txtPerfix", "Perfix should not be empty!!!"},
        {"txtCCHeadGR", "CCHeadGR should not be empty!!!"},
        {"txtRTGSSuspenseHead", "RTGS Suspense Head should not be empty!!!"},
        {"cboRateType", "RateType should be a proper value!!!"},
        {"txtRateVal", "RateVal should not be empty!!!"},
        {"txtForEvery", "ForEvery should not be empty!!!"},
        {"txtPercentage", "Percent should not be empty!!!"},
        {"txtServiceTax", "ServiceTax should not be empty!!!"},
        {"txtMinimumAmt", "Minimum Amount should not be empty!!!"},
        {"txtMaxAmt", "Maximum Amount should not be empty!!!"}
    };
}

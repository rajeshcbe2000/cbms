/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsMRB.java
 * 
 * Created on Sat Feb 05 14:34:36 IST 2005
 */

package com.see.truetransact.ui.product.bills;

import java.util.ListResourceBundle;

public class BillsMRB extends ListResourceBundle {
    public BillsMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtProductId", "ProductId should not be empty!!!"},
        {"rdoPostDtdCheqAllowed_Yes", "PostDtdCheqAllowed should be selected!!!"},
        {"txtDefaultPostage", "DefaultPostage should not be empty!!!"},
        {"lblRateForDelay", "RateForDelay should not be empty!!!"},
        {"txtDiscountRateBills", "DiscountRateBills should not be empty!!!"},
        {"txtTelChargesHead1", "TelChargesHead1 should not be empty!!!"},
        {"txtOverdueRateBills", "OverdueRateBills should not be empty!!!"},
        {"txtCleanBills", "CleanBills should not be empty!!!"},
        {"txtRateForCBP", "RateForCBP should not be empty!!!"},
        {"txtMarginAccountHead", "MarginAccountHead should not be empty!!!"},
        {"txtAtParLimit", "AtParLimit should not be empty!!!"},
        {"cboOperatesLike", "OperatesLike should be a proper value!!!"},
        {"cboRegType", "RegType should be a proper value!!!"},
        {"cboSubRegType", "SubRegType should be a proper value!!!"},
        {"txtIBRAccountHead", "IBRAccountHead should not be empty!!!"},
        {"txtServiceTaxHd", "ServiceTaxHd should not be empty!!!"},
        {"cboBaseCurrency", "BaseCurrency should be a proper value!!!"},
        {"txtInterestAccountHead", "InterestAccountHead should not be empty!!!"},
        {"txtBillsRealisedHead", "BillsRealisedHead should not be empty!!!"},
        {"txtGLAccountHead", "GLAccountHead should not be empty!!!"},
        {"txtDDAccountHead", "DDAccountHead should not be empty!!!"},
        {"txtPostageAccountHead", "PostageAccountHead should not be empty!!!"},
        {"txtTransitPeriod", "TransitPeriod should not be empty!!!"},
        {"cboTransitPeriod", "TransitPeriod should be a proper value!!!"},
        {"txtCommissionAccountHead", "CommissionAccountHead should not be empty!!!"},
        {"rdoContraAccountHead_Yes", "ContraAccountHead should be selected!!!"},
        {"cRadio_ICC_Yes", "Interest during Lodgement should be selected"},
        {"txtProdDesc", "ProdDesc should not be empty!!!"},
        {"txtContractCrAccountHead", "ContraAccount Cr Head should not be empty!!!"},
        {"txtContractDrAccountHead", "ContraAccount Dr Head should not be empty!!!"},
        {"txtOthersHead", "OthersHead should not be empty!!!"},
        {"txtChargesAccountHead", "ChargesAccountHead should not be empty!!!"},
        {"txtTelChargesHead", "TelephoneChargesHead should not be empty!!!"},
        {"cboInstrumentType", "InstrumentType should be a proper value!!!"},
        {"cboChargeType", "ChargeType should be a proper value!!!"},
        {"cboCustCategory", "Customer Category should be a proper value!!!"},
        {"txtFromSlab", "From Slab should be a proper value!!!"},
        {"txtToSlab", "To Slab should be a proper value!!!"},
        {"txtMinimum", "Minimum should be a proper value!!!"},
        {"txtMaximum", "Maximum should be a proper value!!!"},
        {"txtCommision", "Commision should be a proper value!!!"},
        {"txtServiceTax", "ServiceTax should be a proper value!!!"},
        {"cboIntDays", "IntDays should be a proper value!!!"},
        {"txtIntDays", "IntDays should be a proper value!!!"},
        {"txtDrBkChargeAccountHead","DebitBkChargeAccountHead should not be empty!!!"},
        {"txtMisBkChargeAccountHead","MisBkChargeAccountHead should not be empty!!!"}

   };

}
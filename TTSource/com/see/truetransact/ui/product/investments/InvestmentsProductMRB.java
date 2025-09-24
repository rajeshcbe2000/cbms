/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.product.investments;

import java.util.ListResourceBundle;

public class InvestmentsProductMRB extends ListResourceBundle {
    public InvestmentsProductMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboInvestmentBehaves", "Investment Behaves should be a proper value!!!"},
        {"txtProductID", "Product ID should be a proper value!!!"},
        {"txtDesc", "Product Desc should be a proper value!!!"},
        {"txtInvestmentAcHead", "Investment A/c Head should be a proper value!!!"},
        {"txtIntReceivedAcHead", "Interest Received A/c Head should be a proper value!!!"},
        {"txtIntPaidAcHead", "Interest Paid A/c Head should be a proper value!!!"},
        {"txtIntPaidAcHead", "Interest Paid A/c Head should be a proper value!!!"},
        {"txtPremiumPaidAcHead", "Premium Paid A/c Head should be a proper value!!!"},
        {"txtPremiumDepreciationAcHead", "Premium Depreciation A/c Head should be a proper value!!!"},
        {"txtBrokerCommissionAcHead", "Broker Commission A/c Head should be a proper value!!!"},
        {"txtDividentReceivedAcHead", "Divident Received A/c Head should be a proper value!!!"},
        {"txtChargeAcHead", "Charge Paid A/c Head should be a proper value!!!"},
        {"txtInterestReceivableAcHead", "InterestReceivable A/c Head should be a proper value!!!"},
        {"txtServiceTaxAcHead", "Service Tax A/c Head should be a proper value!!!"},
        {"txtServiceTaxAcHead", "Service Tax A/c Head should be a proper value!!!"},
        {"txtTransServiceTaxAcHead", "Transaction Service Tax A/c Head should be a proper value!!!"},
        {"txtDividentPaidAcHead", "Dividend Paid A/c Head should be a proper value!!!"},
        {"txtTDSAcHead", "TDS A/c Head should be a proper value!!!"}
   };

}

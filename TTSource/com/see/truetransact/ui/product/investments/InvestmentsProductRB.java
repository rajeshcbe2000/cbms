/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductRB.java
 *
 * Created on Mon Apr 11 12:08:48 IST 2005
 */

package com.see.truetransact.ui.product.investments;

import java.util.ListResourceBundle;

public class InvestmentsProductRB extends ListResourceBundle {
    public InvestmentsProductRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnTabSave", "Save"},
        {"btnTabDelete", "Delete"},        
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},        
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},       
        {"btnReject", ""},
        {"btnEdit", ""},
        {"null", "Account Details"},
        {"btnTabNew", "New"},      
        {"btnPrint", ""},       
        {"btnException", ""},
        {"btnSave", ""},
        {"lblStatus", "                      "},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnInvestmentAcHead", " "},
        {"btnIntReceivedAcHead", " "},
        {"btnIntPaidAcHead", " "},
        {"btnPremiumPaidAcHead", " "},
        {"btnPremiumDepreciationAcHead", " "},
        {"btnBrokerCommissionAcHead", " "},
        {"btnDividentReceivedAcHead", " "},
        {"btnServiceTaxAcHead", " "},
        {"btnTransServiceTaxAcHead", " "},
        {"lblInvestmentAcHead", "Investment A/c Head"},
        {"lblIntReceivedAcHead", "Interest Received A/c Head"},
        {"lblIntPaidAcHead", "Interest Paid A/c Head"},
        {"lblPremiumPaidAcHead", "Premium Paid A/c Head"},
        {"lblPremiumDepreciationAcHead", "Premium Depreciation Paid A/c Head"},
        {"lblBrokerCommissionAcHead", "Broker Commission Paid A/c Head"},
        {"lblDividentReceivedAcHead", "Dividend Received  A/c Head"},
        {"lblChargeAcHead", "Charge Paid A/c Head"},
        {"lblInterestReceivableAcHead", "Interest Receivable A/c Head"},
        {"lblServiceTaxAcHead", "Service Tax Paid A/c Head"},
        {"lblTransServiceTaxAcHead", "Dividend Paid A/c Head"},
        {"lblInvestmentBehaves", "Investment Type"},
        {"lblProductID", "Product ID"},
        {"lblPremiumReceivedAcHead", "Premium Received A/c Head"},
        {"lblDesc", "Product Desc"},
        {"lblTDSAcHead", "TDS A/c Head"}
    };
    
}

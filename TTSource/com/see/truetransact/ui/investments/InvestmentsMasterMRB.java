/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.investments;

import java.util.ListResourceBundle;

public class InvestmentsMasterMRB extends ListResourceBundle {
    public InvestmentsMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboInvestmentBehaves", "Investment Behaves should be a proper value!!!"},
        {"txtInvestmentID", "Investment ID should be a proper value!!!"},
        {"txtInvestmentName", "InvestmentName should be a proper value!!!"},
        {"tdtlIssueDt", "tdtlIssueDt should be a proper value!!!"},
        {"txtFaceValue", "FaceValue should be a proper value!!!"},
        {"txtInvestmentPeriod_Years", "InvestmentPeriod_Years should be a proper value!!!"},
        {"txtInvestmentPeriod_Months", "InvestmentPeriod_Months should be a proper value!!!"},
        {"txtInvestmentPeriod_Days", "InvestmentPeriod_Days should be a proper value!!!"},
        {"tdtMaturityDate", "MaturityDate should be a proper value!!!"},
        {"cboInterestPaymentFrequency", "InterestPaymentFrequencyshould be a proper value!!!"},
        {"txtCouponRate", "CouponRate should be a proper value!!!"},
        {"txtServiceTaxAcHead", "Service Tax A/c Head should be a proper value!!!"},
        {"txtServiceTaxAcHead", "Service Tax A/c Head should be a proper value!!!"},
        {"txtTransServiceTaxAcHead", "Transaction Service Tax A/c Head should be a proper value!!!"}
    
   };

}

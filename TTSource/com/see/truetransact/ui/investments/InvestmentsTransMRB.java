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

public class InvestmentsTransMRB extends ListResourceBundle {
    public InvestmentsTransMRB(){
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
        {"tdtPurchaseDt", "Purchase Dt should be a proper value!!!"},
        {"txtBrokerName", " Broker Name should be a proper value!!!"},
        {"txtPurchaseRate", "Purchase Rate should be a proper value!!!"},
        {"txtNoOfUnits", " No Of Units should be a proper value!!!"},
        {"txtInvestmentAmount", "Investment Amount should be a proper value!!!"},
        {"txtNarration", "Narration should be a proper value!!!"},
        {"txtPrematureROI", "Premature ROI should be a proper value!!!"},
        {"txtPrematureIntAmt", "Premature Interest Amount should be a proper value!!!"},
        {"txtPremiumAmount", " Premium Amount should be a proper value!!!"},
        {"txtTotalInvestmentAmount", "Total Investment Amount should be a proper value!!!"},
        {"txtBrokenPeriodIntAmount", "Broken Period Int Amount should be a proper value!!!"},
        {"txtBrokerCommission", "Broker Commission should be a proper value!!!"}
    
   };

}

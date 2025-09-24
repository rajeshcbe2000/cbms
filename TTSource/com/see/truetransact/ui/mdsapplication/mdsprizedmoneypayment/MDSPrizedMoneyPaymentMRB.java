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

package com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment;

import java.util.ListResourceBundle;

public class MDSPrizedMoneyPaymentMRB extends ListResourceBundle {
    public MDSPrizedMoneyPaymentMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtSchemeName", "SchemeName should not be empty!!!"},
        {"txtDivisionNo", "DivisionNo should not be empty!!!"},
        {"txtTotalInst", "TotalInstallment should not be empty!!!"},
        {"txtNoOfInstPaid", "NoOfInstPaid should not be empty!!!"},
        {"txtCommisionAmt", "CommisionAmount should not be empty!!!"},
        {"txtBonusAmt", "BonusAmount should not be empty!!!"},
        {"txtPenalAmt", "Penal Amount should not be empty!!!"},
        {"txtBonusRecovered", "Bonus Recovered Amount should not be empty!!!"},
        {"txtPrizedInstNo", "PrizedInstNo should not be empty!!!"},
        {"txtOverDueAmt", "OverDueAmount should not be empty!!!"},
        {"txtNetAmt", "NetAmount should not be empty!!!"},
        {"txtAribitrationAmt", "AribitrationAmount should not be empty!!!"},
        {"txtDiscountAmt", "DiscountAmount should not be empty!!!"},
        {"txtPrizedAmt", "PrizedAmount should not be empty!!!"},
        {"txtNoticeAmt", "NoticeAmount should not be empty!!!"},
        {"txtChargeAmount", "ChargeAmount should not be empty!!!"},
        {"txtOverdueInst", "OverdueInst should not be empty!!!"},
        {"tdtDrawDate", "DrawDate should not be empty!!!"},
        {"txtChittalNo", "ChittalNo should not be empty!!!"},
        {"txtMemberNo", "ChittalNo should not be empty!!!"}
    };
}

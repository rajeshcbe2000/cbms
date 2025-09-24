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

package com.see.truetransact.ui.mdsapplication.mdsmemberreceiptentry;

import java.util.ListResourceBundle;

public class MDSMemberReceiptEntryMRB extends ListResourceBundle {
    public MDSMemberReceiptEntryMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtAribitrationAmt", "AribitrationAmt should not be empty!!!"},
        {"txtTotBonusAmt", "TotBonusAmt should not be empty!!!"},
        {"txtTotPenalAmt", "TotPenalAmt should not be empty!!!"},
        {"txtTotInstAmt", "TotInstAmt should not be empty!!!"},
        {"txtAmtPaid", "AmtPaid should not be empty!!!"},
        {"txtNoticeAmt", "NoticeAmt should not be empty!!!"},
        {"txtTotInterest", "TotInterest should not be empty!!!"},
        {"txtTotDiscountAmt", "TotDiscountAmt should not be empty!!!"},
        {"txtMembershipNo", "MembershipNo should not be empty!!!"},
        {"txtEarlierMemNo", "EarlierMemNo should not be empty!!!"},
        {"txtChangedInstNo", "ChangedInstNo should not be empty!!!"},
        {"txtEarlierMemName", "EarlierMemName should not be empty!!!"},
        {"tdtChangedDt", "ChangedDt should not be empty!!!"},
        {"txtPaidAmount", "PaidAmount should not be empty!!!"},
        {"txtPaidInst", "PaidInst should not be empty!!!"},
        {"tdtPaidDt", "PaidDt should not be empty!!!"}
    };
}

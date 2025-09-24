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

package com.see.truetransact.ui.mdsapplication.mdsclosedreceipt;

import java.util.ListResourceBundle;

public class MDSClosedReciptMRB extends ListResourceBundle {
    public MDSClosedReciptMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtNetAmtPaid", "NetAmountPaid should not be empty!!!"},
        {"txtInterest", "Interest should not be empty!!!"},
        {"txtDiscountAmt", "DiscountAmount should not be empty!!!"},
        {"txtBonusAmt", "BonusAmount should not be empty!!!"},
        {"txtPenalAmtPayable", "PenalAmountPayable should not be empty!!!"},
        {"txtInstPayable", "InstPayable should not be empty!!!"},
        {"txtNoOfInstToPaay", "NoOfInstToPay should not be empty!!!"},
        {"txtTotalInstAmt", "TotalInstAmount should not be empty!!!"},
        {"txtPendingInst", "PendingInstallments should not be empty!!!"},
        {"txtInstAmt", "InstAmount should not be empty!!!"},
        {"txtCurrentInstNo", "CurrentInstNo should not be empty!!!"},
        {"txtNoOfInst", "NoOfInst should not be empty!!!"},
        {"txtSubNo", "SubNo should not be empty!!!"},
        {"tdtChitStartDt", "ChitStartDt should not be empty!!!"},
        {"txtBonusAmtAvail", "BonusAmountAvailable should not be empty!!!"},
        {"txtDivisionNo", "DivisionNo should not be empty!!!"},
        {"txtSchemeName", "Scheme Name should not be empty!!!"},
        {"txtChittalNo", "Chittal No should not be empty!!!"},
        {"txtNoticeAmt", "NoticeAmount should not be empty!!!"},
        {"txtAribitrationAmt", "AribitrationAmount should not be empty!!!"},
        {"txtEarlierMember", "EarlierMember should not be empty!!!"},
        {"txtChangedInst", "ChangedInst should not be empty!!!"},
        {"txtEarlierMemberName", "EarlierMemberName should not be empty!!!"},
        {"tdtChangedDate", "ChangedDate should not be empty!!!"},
        {"txtPaidAmt", "PaidAmount should not be empty!!!"},
        {"txtPaidInst", "PaidInst should not be empty!!!"},
        {"tdtPaidDate", "PaidDate should not be empty!!!"}
    };
}

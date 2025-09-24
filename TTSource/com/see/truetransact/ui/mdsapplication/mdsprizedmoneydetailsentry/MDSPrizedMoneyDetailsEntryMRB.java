/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyDetailsEntryMRB.java
 * 
 * Created on Wed Jun 08 17:29:46 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdsprizedmoneydetailsentry;

import java.util.ListResourceBundle;

public class MDSPrizedMoneyDetailsEntryMRB extends ListResourceBundle {
    public MDSPrizedMoneyDetailsEntryMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtDivisionNo", "DivisionNo should not be empty!!!"},
        {"txtInstallmentNo", "InstallmentNo should not be empty!!!"},
        {"txtSchemeName", "SchemeName should not be empty!!!"},
        {"tdtDrawOrAuctionDt", "DrawOrAuctionDt should not be empty!!!"},
        {"txtPrizedAmount", "PrizedAmount should not be empty!!!"},
        {"txtTotalBonusAmount", "TotalBonusAmount should not be empty!!!"},
        {"txtNextBonusAmount", "NextBonusAmount should not be empty!!!"},
        {"txtCommisionAmount", "CommisionAmount should not be empty!!!"},
        {"txtTotalDiscount", "TotalDiscount should not be empty!!!"},
        {"txtNetAmountPayable", "NetAmountPayable should not be empty!!!"},
        {"txtApplicationNo", "ApplicationNo should not be empty!!!"},
        {"txtMemberClass", "MemberClass should not be empty!!!"},
        {"txtMemberNo", "MemberNo should not be empty!!!"},
        {"txtInstallmentsDue", "InstallmentsDue should not be empty!!!"},
        {"txtInstallmentsAmountPaid", "InstallmentsAmountPaid should not be empty!!!"},
        {"txtInstallmentsPaid", "InstallmentsPaid should not be empty!!!"},
        {"tdtInstallmentDate", "InstallmentDate should not be empty!!!"},
   };

}

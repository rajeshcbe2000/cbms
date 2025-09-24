/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareMRB.java
 * 
 * Created on Mon Dec 27 14:17:36 IST 2004
 */

package com.see.truetransact.ui.share;

import java.util.ListResourceBundle;

public class ShareMRB extends ListResourceBundle {
    public ShareMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtShareDetShareAcctNo", "Share Account No. should not be empty!!!"},
        {"txtDupIdCard", "Duplicate Id Card should not be empty!!!"},
       // {"txtShareAcctNo", "Share Account No. should not be empty!!!"},
        {"txtRelativeDetails", "Relative Details should not be empty!!!"},
        {"txtCustId", "Customer Id should not be empty!!!"},
        {"tdtNotEligiblePeriod", "Not-Eligible-Period should not be empty!!!"},
        {"txtResolutionNo", "Resolution No. should not be empty!!!"},
        {"txtResolutionNo1", "Resolution No. should not be empty!!!\n"},
        {"txtConnGrpDet", "Connected Group Details should not be empty!!!"},
        {"txtMemFee", "Member Fee should not be empty!!!"},
        {"tdtIssId", "Issue Id should not be empty!!!"},
        {"txtShareFee", "Share Fee should not be empty!!!"},
        {"txtShareDetNoOfShares", "No.of shares should not be empty!!!"},
        {"txtApplFee", "Application Fee should not be empty!!!"},
        {"tdtShareDetIssShareCert", "Resolution Date should not be empty!!!\n"},
        {"txtShareDetShareNoTo", "Share No. To should not be empty!!!"},
        {"cboAcctStatus", "Account Status should be a proper value!!!"},
        {"cboConstitution", "Constitution should be a proper value!!!"},
        {"cboCommAddrType", "Communication Address Type should be a proper value!!!"},
        {"cboShareType", "Share Type should be a proper value!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtDirRelDet", "Director Relative Details should not be empty!!!"},
        {"txtShareAmt", "Share Amount should not be empty!!!"},
        {"txtPropertyDetails", "Property Details should not be empty!!!"},
        {"txtShareDetShareNoFrom", "Share No. From should not be empty!!!"},
        {"txtWelFund", "Welfare Fund should not be empty!!!"},
        {"chkNotEligibleStatus", "Not-Eligible-Status should be selected!!!"} 

   };

}

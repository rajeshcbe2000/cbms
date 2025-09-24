/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterMRB.java
 * 
 * Created on Fri Aug 05 13:53:36 GMT+05:30 2011
 */

package com.see.truetransact.ui.share;

import java.util.ListResourceBundle;

    
    
public class ShareDividendPaymentMRB extends ListResourceBundle {
    public ShareDividendPaymentMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtDrfTransMemberNo", "Member no should not be empty!!!"},
        {"txtDrfTransName", "Name of the member!!!"},
        {"chkDueAmtPayment", "Check if paying due amount!!!"},
        {"txtDrfTransAmount", "Amount should not be empty!!!"},
        {"cboDrfTransProdID","Product ID cannot be empty!!"}

   };

}

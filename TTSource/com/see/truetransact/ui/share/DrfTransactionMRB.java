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



public class DrfTransactionMRB extends ListResourceBundle {
    public DrfTransactionMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboDrfTransProdID","Product ID cannot be empty!!"},
        {"txtDrfTransMemberNo", "Member no should not be empty!!!"},
        {"txtDrfTransName", "Name of the member!!!"},
        {"chkDueAmtPayment", "Check if paying due amount!!!"},
        {"lblDrfTransAddressCont", "Address should not be empty!!!"},
        {"txtDrfTransAmount", "Amount should not be empty!!!"},
         {"txtResolutionNo","Resolution No should not be empty!!!"},
        {"tdtResolutionDate","Resolution Date should not be empty!!!"}
    };
    
}

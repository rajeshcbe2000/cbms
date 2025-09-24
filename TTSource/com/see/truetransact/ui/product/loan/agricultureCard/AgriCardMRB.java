/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OtherBankMRB.java
 * 
 * Created on Thu Dec 30 16:04:32 IST 2004
 */

package com.see.truetransact.ui.product.loan.agricultureCard;

import java.util.ListResourceBundle;

public class AgriCardMRB extends ListResourceBundle {
    public AgriCardMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboAgriCardType", "Card Type should be a proper value!!!!!!"},
        {"txtNoOfYears", "Number should not be empty!!!"},
        {"cboAgriCardValidity", "Card Validity should be a proper value!!!"},
        {"cboProdType", "Prod Type should be a proper value!!!!!!"},
        {"cboProdId", "Prod Id should be a proper value!!!!!!"},
        {"cRadio_SB_Yes", "SB_Yes should be selected!!!"},
        {"cRadio_SB_No", "SB_No should be selected!!!"},
        {"txtPincode", "Pincode should not be empty!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"txtOtherBranchShortName", "OtherBranchShortName should not be empty!!!"},
        {"cboCity", "City should be a proper value!!!"} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IntroducerMRB.java
 * 
 * Created on Wed Dec 29 10:33:45 IST 2004
 */

package com.see.truetransact.ui.common.introducer;

import java.util.ListResourceBundle;

public class IntroducerMRB extends ListResourceBundle {
    public IntroducerMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtIssuedDateDD", "IssuedDate should not be empty!!!"},
        {"txtPinCode", "PinCode should not be empty!!!"},
        {"txtDocNoDD", "Document No. should not be empty!!!"},
        {"tdtExpiryDateDD", "ExpiryDate should not be empty!!!"},
        {"cboCountry", "Country should be a proper value!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"txtAcctNo", "Account No. should not be empty!!!"},
        {"cboDocTypeDD", "Document Type should be a proper value!!!"},
        {"txtBankOB", "Bank Name should not be empty!!!"},
        {"txtDocID", "Document ID should not be empty!!!"},
        {"txtNameOB", "Branch Name should not be empty!!!"},
        {"cboCity", "City should be a proper value!!!"},
        {"txtAcctNoOB", "Account No. should not be empty!!!"},
        {"txtACode", "Area Code should not be empty!!!"},
//        {"txtDesig", "Designation should not be empty!!!"},
        {"cboIdentityTypeID", "IdentityType should be a proper value!!!"},
        {"txtPhone", "Phone should not be empty!!!"},
        {"txtIntroName", "Introducer Name should not be empty!!!"},
        {"txtIssuedAuthID", "Issuing Authority should not be empty!!!"},
        {"txtStreet", "Street should not be empty!!!"},
        {"txtBranchOB", "Branch Name should not be empty!!!"},
        {"txtArea", "Area should not be empty!!!"},
        {"txtIssuedByDD", "IssuedBy should not be empty!!!"} 

   };

}

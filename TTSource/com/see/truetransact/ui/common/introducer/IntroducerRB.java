/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * IntroducerRB.java
 * 
 * Created on Wed Dec 29 10:26:51 IST 2004
 */

package com.see.truetransact.ui.common.introducer;

import java.util.ListResourceBundle;

public class IntroducerRB extends ListResourceBundle {
    public IntroducerRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblIdentityTypeID", "Identity Type"},
        {"panIntroducerDetails", "Introducer Details"},
        {"lblStreet", "Street"},
        {"lblIntroName", "Introducer's Name"},
        {"lblIntroducerTypeValue", ""},
        {"lblCity", "City"},
        {"lblBranchCodeValue", ""},
        {"lblDocTypeDD", "Document Type"},
        {"lblBranchCode", "Branch Code"},
        {"lblDocID", "Document ID"},
        {"lblIssuedByDD", "Issued By"},
        {"panBank", "Other Bank"},
        {"lblAcctNoOB", "Account No."},
        {"lblIssuedByID", "Issuing Authority"},
        {"panOthers", "Others"},
        {"lblExpiryDateDD", "Expiry Date"},
        {"lblCustValue", ""},
        {"btnAcctNo", ""},
        {"lblArea", "Area"},
        {"lblIssuedDateDD", "Issue Date"},
        {"lblPinCode", "Pin Code"},
        {"panDocDetails", "Document Details"},
        {"lblDesig", "Profession"},
        {"lblBranch", "Branch Name"},
        {"lblDocNoDD", "Document No."},
        {"lblAcctValue", ""},
        {"lblName", "Name"},
        {"lblCountry", "Country"},
        {"lblBranchValue", ""},
        {"panSelfCustomer", "Self or Existing Customer"},
        {"lblPhone", "Phone"},
        {"lblBranchOB", "Branch Code"},
        {"lblProdId", "Product ID"},
        {"lblIntroducerType", "Type of Introduction:"},
        {"lblState", "State"},
        {"lblNameOB", "Branch Name"},
        {"lblNameValue", ""},
        {"panIdentity", "Identity Info."},
//        {"lblCust", "Customer ID"},
        {"lblBankOB", "Bank Name"},
        {"lblAcctHd", "Account Head"},
        {"lblAcctNo", "Customer ID"} ,
        {"lblActNum", "Act Number"}

   };

}

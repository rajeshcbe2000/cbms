/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CorporateCustomerMRB.java
 * 
 * Created on Wed Mar 24 16:28:55 GMT+05:30 2004
 */

package com.see.truetransact.ui.customer;

import java.util.ListResourceBundle;

public class SHGCustomerMRB extends ListResourceBundle {
    public SHGCustomerMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtCustomerID", "CustomerID should not be empty!!!"},
        {"cboTitle", "Title should be a proper value!!!"},
        {"txtWebSite", "WebSite should not be empty!!!"},
        {"cboCountry", "Country should be a proper value!!!"},
        {"txtAuthCustId", "AuthCustId should not be empty!!!"},
        {"cboCity", "City should be a proper value!!!"},
        {"cboAddressType", "AddressType should be a proper value!!!"},
        {"rdoGender_Male", "Gender should be selected!!!"},
        {"txtTransPwd", "Transaction Password should not be empty!!!"},
        {"txtCustUserid", "Userid should not be empty!!!"},
        {"txtNetWorth", "NetWorth should not be empty!!!"},
        {"txtCustPwd", "Password should not be empty!!!"},
        {"txtArea", "Area should not be empty!!!"},
        {"txtAreaCode", "AreaCode should not be empty!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"cboPhoneType", "PhoneType should be a proper value!!!"},
        {"txtFirstName", "FirstName should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"cboRelationManager", "RelationManager should be a proper value!!!"},
        {"txtLastName", "LastName should not be empty!!!"},
        {"txtEmailID", "EmailID should not be empty!!!"},
        {"txtPhoneNumber", "PhoneNumber should not be empty!!!"},
        {"txtMiddleName", "MiddleName should not be empty!!!"},
        {"cboPrefCommunication", "Preferred Communication should be a proper value!!!"},
        {"txtPincode", "Pincode should not be empty!!!"},
        {"txtStreet", "Street should not be empty!!!"},
        {"cboCustomerType", "CustomerType should be a proper value!!!"},
        {"txtCompany", "Company should not be empty!!!"},
        {"cboBusNature", "Select the Business Nature"},
        {"tdtDtEstablished", "Enter the Date of Establishment"},
        {"txtRegNumber", "Enter the Register Number of the Company"},
        {"txtCEO", "Enter the Name of the Chief Executive Officer"},
        {"cboIntroType", "Select the Introducer Type"},
        {"tdtNetWorthAsOn", "NetWorthAsOnDate should not be empty!!!"},
        {"txtPanNumber", "TinNumber should  be a proper value!!!"},
        {"chkAddrVerified", "Address Verified should be Selected!!!"},
        {"cboMembershipClass", "MembershipClass should be a proper value!!!"},
        {"cboAddrProof", "Address Proof should be a proper value!!!"},
        {"cboIdenProof", "Identity Proof should be a proper value!!!"},
        {"rdoITDec_Pan","Pan/Form Details Should Be Entered!!!!!"}
        
   };

}

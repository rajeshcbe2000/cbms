/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchManagementMRB.java
 *
 * Created on Tue Apr 13 14:11:07 PDT 2004
 */

package com.see.truetransact.ui.sysadmin.branch;

import java.util.ListResourceBundle;

public class BranchManagementMRB extends ListResourceBundle {
    public BranchManagementMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtIPDataBC", "IPDataBC should not be empty!!!"},
        {"txtPinCode", "PinCode should not be empty!!!"},
        {"cboFromHrs", "FromHrs should be a proper value!!!"},
        {"txtBranchShortName", "BranchShortName should not be empty!!!"},
        {"txtRegionalOffice", "RegionalOffice should not be empty!!!"},
        {"cboCountry", "Country should be a proper value!!!"},
        {"rdoBalanceLimitBP_Yes", "BalanceLimitBP should be selected!!!"},
        {"cboCity", "City should be a proper value!!!"},
        {"txtBranchCode", "BranchCode should not be empty!!!"},
        {"txtUserIDDataBC", "UserIDDataBC should not be empty!!!"},
        {"txtDriverDataBC", "DriverDataBC should not be empty!!!"},
        {"cboBranchGroup", "BranchGroup should be a proper value!!!"},
        {"cboBranchGLGroup", "BranchGLGroup should be a proper value!!!"},
        {"cboToMin", "ToMin should be a proper value!!!"},
        {"cboContactType", "ContactType should be a proper value!!!"},
        {"txtAvgCashStockBP", "AvgCashStockBP should not be empty!!!"},
        {"cboFromMin", "FromMin should be a proper value!!!"},
        {"txtPortAppBC", "PortAppBC should not be empty!!!"},
        {"txtArea", "Area should not be empty!!!"},
        {"txtAreaCode", "AreaCode should not be empty!!!"},
        {"txtMICRCode", "MICRCode should not be empty!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"txtBranchName", "BranchName should not be empty!!!"},
        {"txtMaxCashStockBP", "MaxCashStockBP should not be empty!!!"},
        {"txtIPAppBC", "IPAppBC should not be empty!!!"},
        {"tdtOpeningDate", "OpeningDate should not be empty!!!"},
        {"txtPasswordDataBC", "PasswordDataBC should not be empty!!!"},
        {"cboToHrs", "ToHrs should be a proper value!!!"},
        {"txtContactNo", "ContactNo should not be empty!!!"},
        {"txtPortDataBC", "PortDataBC should not be empty!!!"},
        {"txtDBNameDataBC", "DBNameDataBC should not be empty!!!"},
        {"txtURLDataBC", "URLDataBC should not be empty!!!"},
        {"txtStreet", "Street should not be empty!!!"},
        {"txtBSRCode", "BSRCode should not be empty!!!"},
        {"txtBranchManagerID", "BranchManagerID should not be empty!!!"}
        
    };
    
}

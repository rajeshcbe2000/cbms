/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchManagementRB.java
 * 
 * Created on Tue Apr 13 13:01:33 PDT 2004
 */

package com.see.truetransact.ui.sysadmin.branch;

import java.util.ListResourceBundle;

public class BranchManagementRB extends ListResourceBundle {
    public BranchManagementRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblMsg", ""},
        {"lblMaxCashStockBP", "Maximum Cash Stock"},
        {"lblSpace2", "     "},
        {"panContactDetails", "Contact Details"},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblAreaCode", "Area Code"},
        {"lblWorkingFrom", "From"},
        {"lblCountry", "Country"},
        {"lblDBNameDataBC", "Database Name"},
        {"btnEdit", ""},
        {"lblMICRCode", "MICR Code"},
        {"lblDriverDataBC", "Database Driver"},
        {"lblWorkingMin", "Min"},
        {"lblPortAppBC", "Port"},
        {"lblIPAppBC", "IP Address"},
        {"lblContactType", "Contact Type"},
        {"btnBranchManagerID", ""},
        {"lblBranchShortName", "Branch Short Name"},
        {"panContactList", "List of Phone No's"},
        {"lblAvgCashStockBP", "Average Cash Stock"},
        {"btnPrint", ""},
        {"lblIPDataBC", "IP Address"},
        {"panDataBC", "Data Server"},
        {"lblWorkingTo", "To"},
        {"lblStreet", "Street"},
        {"lblCity", "City"},
        {"lblBalanceLimitBP", "Check Balance Limit"},
        {"lblBranchCode", "Branch Code"},
        {"lblWorkingHrs", "Hrs"},
        {"rdoBalanceLimitBP_Yes", "Yes"},
        {"rdoBalanceLimitBP_No", "No"},
        {"btnSave", ""},
        {"btnPhoneDelete", ""},
        {"lblStatus", "                      "},
        {"lblContactNo", "Contact No"},
        {"lblPasswordDataBC", "Password"},
        {"lblArea", "Area"},
        {"lblPinCode", "Pin Code"},
        {"lblBranchManagerID", "Branch Manager ID"},
        {"btnContactNoAdd", ""},
        {"panWorkingTime", "Working Time"},
        {"btnDelete", ""},
        {"lblOpeningDate", "Opening Date"},
        {"lblRegionalOffice", "Regional Office"},
        {"lblURLDataBC", "Database URL"},
        {"lblPortDataBC", "Port"},
        {"panAddress", ""},
        {"btnNew", ""},
        {"btnRegionalOffice", ""},
        {"panAppBC", "Application"},
        {"btnCancel", ""},
        {"lblState", "State"},
        {"lblBranchGroup", "Branch Screen Group"},
        {"btnPhoneNew", ""},
        {"lblUserIDDataBC", "User ID"},
        {"lblBranchName", "Branch Name"},
        {"lblBranchManagerName", "Branch Manager Name"},
        {"lblRegionalOfficerName", "Regional Officer Name"},
        {"lblBSRCode", "BSR Code"},
        {"tblColumn1", "S.N."},
        {"tblColumn2", "Contact Type"},
        {"tblColumn3", "Contact No"},
        {"lblBranchGLGroup", "Branch GLGroup"},
        {"WarningMessage","The StartingTime Should be Less than EndingTime\n."},
        {"WarningBranchCode","This Branch Code Already Exist !"},
        {"WarningOpeningDate","Bank Opening Date is not entered"},
        {"ValidateContactTypeAndNo","Contact Type And Contact No Should Not Be Duplicated !!!"},
        {"cDialogOK","Ok"}

   };

}

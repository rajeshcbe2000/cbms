/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserRB.java
 * 
 * Created on Wed Feb 09 11:28:26 IST 2005
 */

package com.see.truetransact.ui.sysadmin.user;

import java.util.ListResourceBundle;

public class UserRB extends ListResourceBundle {
    public UserRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblRoleID", "Role ID"},
        {"lblForeignGroupId", "Foreign Group ID"},
        {"lblForeignBranchGroup", "Foreign Branch Group"},
        {"lblTerminalId", "Terminal Id"},
        {"lblCustomerId", "Customer Id"},
        {"btnAdd", "Add"},
        {"lblUserId", "User Id"},
        {"lblCustomerName", "Customer Name"},
        {"lblGroupID", "Group ID"},
        {"btnAuthorize", ""},
        {"lblDisplayCustName", ""},
        {"lblMsg", ""},
        {"lblSpace2", "     "},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblSmartCard", "Smart Card or Bio-metric login with a pin"},
        {"lblMachineName", "Machine Name"},
        {"chkSmartCard", ""},
        {"lblLastLoginDate", "Last Login Date"},
        {"btnReject", ""},
        {"chkAppraiserAllowed",""},
        {"btnEdit", ""},
        {"btnRemove", "Remove"},
        {"lblEmailId", "Email Id"},
        {"lblSuspendFrom", "From"},
        {"lblDisplayLastLoginDate", ""},
        {"btnPrint", ""},
        {"lblSuspendTo", "To"},
        {"lblAccessToDate", "Access To Date"},
        {"chkSuspend", ""},
        {"chkUserSuspend", ""},
        {"lblConfirmPassword", "Confirm Password"},
        {"lblReasonForSuspend", "Reason For Suspension"},
        {"lblUserIdExpiresOn", "User Id Expires on"},
        {"btnException", ""},
        {"lblTerminalDescription", "Terminal Description"},
        {"lblUserSuspend", "User can suspend his/her account temporarily"},
        {"panSettings", "Settings"},
        {"lblBranchID", "Branch ID"},
        {"btnCustomerID", ""},
        {"btnSave", ""},
        {"lblAccessFromDate", "Access From Date"},
        {"btnTerminalID", ""},
        {"lblStatus", "                      "},
        {"lblRemarks", "Remarks"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblIpAddress", "IP Address"},
        {"lblSuspend", "Suspend user temporarily"},
        {"lblTerminalName", "Terminal Name"},
        {"lblPassword", "Password"},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblBranchName", "Branch Name"},
        {"lblActivateUser", "User account Activate on"},
        {"warningForSuspend","Reason For Suspend, From And To Date Should Not be Empty !!!"},        
        {"SuspendFromDate","Reason For Suspend From Date Should be Greater than User Account Activated Date !!!"},
        {"SuspendTo","Reason For Suspend To Date Should be Greater than Suspend From Date !!!"},
        {"AccessTo","Access To Date Should be Greater than Access From Date !!!"},
        {"UserIdExpired","User Id Expired Date Should be Greater than User Account Activated Date !!!"},
        {"SuspendToDate","Reason For Suspend To Date Should be Less than User Id Expires Date !!!"},
        {"AccessFromDate","Access From Date Should be Greater than User Account Activated Date !!!"},
        {"AccessToDate","Access To Date Should be Less than User Id Expires Date !!!"},
        {"UserIdUniqueness","This User Id Already Exist !!!"},
        {"booleanYes","Y"},
        {"booleanNo","N"},
        {"userActivationDateValidation", "User activation date should not be before Branch activation date!!!"}

   };

}

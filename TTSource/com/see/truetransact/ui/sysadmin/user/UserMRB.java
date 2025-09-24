/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * UserMRB.java
 * 
 * Created on Wed Feb 09 11:43:21 IST 2005
 */

package com.see.truetransact.ui.sysadmin.user;

import java.util.ListResourceBundle;

public class UserMRB extends ListResourceBundle {
    public UserMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtReasonForSuspend", "ReasonForSuspend should not be empty!!!"},
        {"txtUserId", "UserId should not be empty!!!"},
        {"chkSuspend", "Suspend should be selected!!!"},
        {"chkUserSuspend", "UserSuspend should be selected!!!"},
        {"tdtSuspendFrom", "SuspendFrom should not be empty!!!"},
        {"cboRoleID", "RoleID should be a proper value!!!"},
        {"cboForeignGroupId", "Foreign Group ID should be a proper value!!!"},
        {"cboForeignBranchGroup", "Foreign Branch Group should be a proper value!!!"},
        {"txtTerminalId", "TerminalId should not be empty!!!"},
        {"pwdConfirmPassword", "ConfirmPassword should not be empty!!!"},
        {"txtTerminalName", "TerminalName should not be empty!!!"},
        {"tdtAccessFromDate", "AccessFromDate should not be empty!!!"},
        {"chkSmartCard", "SmartCard should be selected!!!"},
        {"chkAppraiserAllowed", "Appraiser should be selected!!!"},
        {"cboGroupID", "GroupID should be a proper value!!!"},
        {"txtBranchName", "BranchName should not be empty!!!"},
        {"txtIPAddress", "IPAddress should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"pwdPassword", "Password should not be empty!!!"},
        {"tdtAccessToDate", "AccessToDate should not be empty!!!"},
        {"txtTerminalDescription", "TerminalDescription should not be empty!!!"},
        {"tdtActivateUser", "ActivateUser should not be empty!!!"},
        {"txtBranchID", "BranchID should not be empty!!!"},
        {"tdtSuspendTo", "SuspendTo should not be empty!!!"},
        {"txtCustomerId", "CustomerId should not be empty!!!"},
        {"txtMachineName", "MachineName should not be empty!!!"},
        {"tdtUserIdExpiresOn", "UserIdExpiresOn should not be empty!!!"} 

   };

}

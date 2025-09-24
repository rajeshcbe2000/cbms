/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.sysadmin.leavemanagement;

import java.util.ListResourceBundle;

public class LeaveSanctionMRB extends ListResourceBundle {
    public LeaveSanctionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboProcessType", "Process type Should Not Be Empty!!!"},
        {"tdtLvAplDt", "Leave Application Date should be a proper value!!!"},
        {"tdtReqFrom", "Leave Request From Date should be a proper value!!!"},
        {"tdtReqTo", "Leave Request To Date should be a proper value!!!"},
        {"txtNoOfDays", "No Of Days Should Not Be Empty!!!"},
        {"txtLeavePurpose", "Leave Purpose should not be empty!!!"},
        {"txtSanRefNo", "Sanction Number should not be empty!!!"},
        {"tdtSanDate", "Sanction Date should not be empty!!!"},
        {"txtSanEmpID","Employee ID should Not Be Empty"}
   };

}

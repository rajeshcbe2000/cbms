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

public class LeaveManagementMRB extends ListResourceBundle {
    public LeaveManagementMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboParForLeave", "Parameters For Leave Crediting should be a proper value!!!"},
        {"txtTypeOfLeave", "Leave Type should be a proper value!!!"},
        {"txtDesc", "Descriptor should be a proper value!!!"},
        {"cboCreditType", "LEave Credit Type should be a proper value!!!"},
        {"rdoIntroReq_Yes1", "Leave Lapses should be a proper value!!!"},
        {"rdoAcc_Yes", "Accumaltion should not be empty!!!"}
        
   };

}

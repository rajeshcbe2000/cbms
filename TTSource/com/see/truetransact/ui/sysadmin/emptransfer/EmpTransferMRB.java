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

package com.see.truetransact.ui.sysadmin.emptransfer;

import java.util.ListResourceBundle;

public class EmpTransferMRB extends ListResourceBundle {
    public EmpTransferMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboRoleCurrBran", "Role Of Curr Branch should be a proper value!!!"},
        {"cboRoleTransBran", "Role Of Tranfer Branch should be a proper value!!!"},
        {"txtEmpID", "Employee ID  should be a proper value!!!"},
        {"tdtDoj", "Date Of Joining should be a proper value!!!"},
        {"tdtLastDay", "Last Working Date should be a proper value!!!"},
        {"rdoAcc_Yes", "Accumaltion should not be empty!!!"},
        {"cboTransBran", "Transferred  Branch should not be empty!!!"}
        
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CustDetailsRB.java
 * 
 * Created on Fri Dec 24 10:13:55 IST 2004
 */

package com.see.truetransact.ui.payroll.payMaster;

import java.util.ListResourceBundle;

public class EmployeeDetailsRB extends ListResourceBundle {
    public EmployeeDetailsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"employeeId", "Employee Id"} ,
        {"employeeName", "Employee Name"} ,
        {"employeeAddress", "Address"} ,
        {"employeePlace", "Place"} ,
        {"employeeCity", "City"} ,
        {"employeeDesignation", "Designation"},
        {"employeePhNo", "Contact No."},
        {"employeeGender", "Gender"},  
        {"employeeEmail", "Email ID"},  
        {"employeeDtOfJoin", "Date of Join"} 
            
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * IncrementMRB.java
 * 
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.ui.payroll.increment;

import java.util.ListResourceBundle;

public class IncrementMRB extends ListResourceBundle {

    public IncrementMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtEmpId", "Employee Id should not be empty!!!"},
        {"txtPresBasicSalary", "Present Basic Salary should not be empty!!!"},
        {"txtLastIncrDate", "Last Increment Date should not be empty!!!"},
        {"cboDesig", "New Designation should not be empty!!!"},
        {"txtNewBasicSalary", "New Basic Salary should not be empty!!!"},
        {"txtEmpName", "Employee Name should not be empty!!!"},
        {"txtDesig", "Designation should not be empty!!!"},
        {"txtNextIncrDate", "Next increment Date should not be empty!!!"},
        {"txtNumberIncr", "Number of increments should not be empty!!!"}
    };
}

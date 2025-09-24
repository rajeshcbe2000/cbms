/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentRB.java
 * 
 * Created on Fri Nov 14 10:00:00 IST 2014
 */
package com.see.truetransact.ui.payroll.increment;
import java.util.ListResourceBundle;

public class IncrementRB extends ListResourceBundle {

    public IncrementRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtNewBasicSalary", ""},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblName", "Name"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblStatus1", ""},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"btnNew", ""},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"lblRemarks", "Remarks"},
        {"txtEmpId", "Employee ID"},
        {"txtEmpName", "Employee Name"},
        {"txtPresBasicSalary", "Present Basic Salary"},
        {"txtLastIncrDate", "Last Increment Date"},
        {"cboDesig", "Designation should not be empty"},
        {"txtNewBasicSalary", "New Basic Salary should not be empty"},
        {"txtDesig", "Designation"},
        {"txtNextIncrDate", "Next Increment Date"},
        {"txtNumberIncr", "Number of Increments"},
        {"tdtNewIncrDate", "New increment Date"},};
}

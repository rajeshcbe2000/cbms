/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveDetailsMRB.java
 * 
 */
package com.see.truetransact.ui.payroll.leaveDetails;

/**
 *
 * @author anjuanand
 */
import java.util.ListResourceBundle;

public class LeaveDetailsMRB extends ListResourceBundle {

    public LeaveDetailsMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtEmployeeId", "Employee Id should not be empty!!!"},
        {"cboLeaveDescription", "Please select Leave Description!!!"},
        {"tdtLeaveDate", "Please enter Leave Date!!!"},
        {"txtLeaveRemarks", "Remarks should not be empty!!!"}
    };
}

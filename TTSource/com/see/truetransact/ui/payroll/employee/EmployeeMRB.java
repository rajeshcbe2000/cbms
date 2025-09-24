/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * EmployeeMRB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.employee;

/**
 *
 * @author anjuanand
 */
import java.util.ListResourceBundle;

public class EmployeeMRB extends ListResourceBundle {

    public EmployeeMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"cboTitle", "Title should be a proper value!!!"},
        {"txtHomePhone", "HomePhone should not be empty!!!"},
        {"txtPinCode", "PinCode should not be empty!!!"},
        {"txaEducation", "Education should not be empty!!!"},
        {"cboCountry", "Country should be a proper value!!!"},
        {"txtAlternateEmail", "AlternateEmail should not be empty!!!"},
        {"cboMartialStatus", "MartialStatus should be selected!!!"},
        {"cboEmployeeType", "EmployeeType should be a proper value!!!"},
        {"tdtLeavingDate", "LeavingDate should not be empty!!!"},
        {"txaResponsibility", "Responsibility should not be empty!!!"},
        {"cboCity", "City should be a proper value!!!"},
        {"txtPanNo", "PanNo should not be empty!!!"},
        {"rdoGender_Male", "Gender should be selected!!!"},
        {"cboBranch", "BranchCode should not be empty!!!"},
        {"cboManager", "Manager should be a proper value!!!"},
        {"txtSsnNo", "SsnNo should not be empty!!!"},
        {"tdtBirthDate", "BirthDate should not be empty!!!"},
        {"txaPerformance", "Performance should not be empty!!!"},
        {"cboDepartment", "Department should be a proper value!!!"},
        {"txtArea", "Area should not be empty!!!"},
        {"txtPassPortNo", "PassPortNo should not be empty!!!"},
        {"txaExperience", "Experience should not be empty!!!"},
        {"txtLastName", "LastName should not be empty!!!"},
        {"tdtJoiningDate", "JoiningDate should not be empty!!!"},
        {"cboState", "State should not be empty!!!"},
        {"cboJobTitle", "JobTitle should be a proper value!!!"},
        {"tdtWeddindDate", "WeddindDate should not be empty!!!"},
        {"txtCellular", "Cellular should not be empty!!!"},
        {"txaSkills", "Skills should not be empty!!!"},
        {"txtStreet", "Street should not be empty!!!"},
        {"txaComments", "Comments should not be empty!!!"},
        {"txtPresentBasicSalary", "Present Basic Salary should not be empty!!!"},
        {"cboDesignation", "Select Designation!!!"},
        {"txtScaleId", "Scale Id should not be empty!!!"},
        {"tdtEffectiveDate", "Select Effective Date!!!"},
        {"tdtLastIncrementDate", "Select Last Increment Date!!!"},
        {"tdtNextIncrementDate", "Select Next Increment Date!!!"},
        {"tdtDateOfJoin", "Select Date of Join!!!"},
        {"tdtProbationEndDate", "Select Probation End Date!!!"},
        {"tdtDateOfRetirement", "Select Date of Retirement!!!"},
        {"cboStatusOfEmp", "Select Status of Employee!!!"},
        {"txtCustomerId", "Customer Id should not be empty!!!"},
        {"txtIncrementCount", "Select Increment Count!!!"},
        {"txtPensionCodeNo", "Pension Code No should not be empty!!!"},
        {"txtWfNumber", "Wf Number should not be empty!!!"},
        {"cboManager", "Please select Manager!!!"}
    };
}

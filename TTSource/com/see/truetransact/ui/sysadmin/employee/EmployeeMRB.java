/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * EmployeeMRB.java
 */

package com.see.truetransact.ui.sysadmin.employee;
import java.util.ListResourceBundle;
public class EmployeeMRB extends ListResourceBundle {
    public EmployeeMRB(){
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
        {"txtBranchCode", "BranchCode should not be empty!!!"},
        {"cboManager", "Manager should be a proper value!!!"},
        {"txtSsnNo", "SsnNo should not be empty!!!"},
        {"tdtBirthDate", "BirthDate should not be empty!!!"},
        {"txtOfficePhone", "OfficePhone should not be empty!!!"},
        {"txaPerformance", "Performance should not be empty!!!"},
        {"cboDepartment", "Department should be a proper value!!!"},
        {"txtArea", "Area should not be empty!!!"},
        {"txtFirstName", "FirstName should not be empty!!!"},
        {"txtPassPortNo", "PassPortNo should not be empty!!!"},
        {"txaExperience", "Experience should not be empty!!!"},
        {"txtLastName", "LastName should not be empty!!!"},
        {"tdtJoiningDate", "JoiningDate should not be empty!!!"},
        {"cboState", "State should not be empty!!!"},
        {"cboJobTitle", "JobTitle should be a proper value!!!"},
        {"tdtWeddindDate", "WeddindDate should not be empty!!!"},
        {"txtCellular", "Cellular should not be empty!!!"},
        {"txtEmployeeId", "EmployeeId should not be empty!!!"},
        {"txaSkills", "Skills should not be empty!!!"},
        {"txtStreet", "Street should not be empty!!!"},
        {"txaComments", "Comments should not be empty!!!"},
        {"txtOfficialEmail", "OfficialEmail should not be empty!!!"} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFMasterMRB.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.pfMaster;

import java.util.ListResourceBundle;

public class PFMasterMRB extends ListResourceBundle {

    public PFMasterMRB() {
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtEmployeeId", "Employee Id should not be empty!!!"},
        {"txtPfAccountNo", "PF Account No. should not be empty!!!"},
        {"tdtPfDate", "PF Date should not be empty!!!"},
        {"tdtPfOpeningDate", "PF Opening Date should not be empty!!!"},
        {"txtOpeningBalance", "PF opening Balance should not be empty!!!"},
        {"txtPfRateOfInterest", "PF Rate of Interest should not be empty!!!"},
        {"tdtLastInterestDate", "Last Interest Date should not be empty!!!"},
        {"txtPfNomineeName", "PF Nominee Name should not be empty!!!"},
        {"txtPfNomineeRelation", "Nominee Relation should not be empty!!!"},
        {"txtEmployerContribution", "Employer Contribution should not be empty!!!"},};
}

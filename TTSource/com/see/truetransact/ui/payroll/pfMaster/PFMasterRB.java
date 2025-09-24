/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PFMasterRB.java
 * 
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.pfMaster;

import java.util.ListResourceBundle;

public class PFMasterRB extends ListResourceBundle {

    public PFMasterRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnEmployeeId", ""},
        {"lblEmployeeId", "Employee ID"},
        {"lblStatus", "                      "},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblStatus", ""},
        {"lblSpace", ""},
        {"lblPfAccountNo", "PF Account No"},
        {"lblPfDate", "PF Date"},
        {"lblPfOpeningDate", "PF Opening Date"},
        {"btnSave", ""},
        {"lblOpeningBalance", "Opening Balance"},
        {"lblSpace", " Status :"},
        {"lblPfRateOfInterest", "PF Rate of Interest"},
        {"lblLastInterestDate", "Last Interest Date"},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"lblPfNomineeName", "PF Nominee Name"},
        {"lblPfNomineeRelation", "PF Nominee Relation"},
        {"lblEmployerContribution", "Employer Contribution"}
    };
}

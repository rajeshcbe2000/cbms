/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveDetailsRB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.leaveDetails;

/*
 * @author anjuanand
 *
 */
import java.util.ListResourceBundle;

public class LeaveDetailsRB extends ListResourceBundle {

    public LeaveDetailsRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnDelete", ""},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"lblSpace1", " Status :"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnTabNew", "New"},
        {"btnPrint", ""},
        {"btnException", ""},
        {"btnSave", ""},
        {"lblStatus", " "},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblEmployeeId", "Employee Id"},
        {"lblLeaveDescription", "Leave Description"},
        {"lblLeaveDate", "Leave Date"},
        {"lblLeaveRemarks", "Remarks"}
    };
}

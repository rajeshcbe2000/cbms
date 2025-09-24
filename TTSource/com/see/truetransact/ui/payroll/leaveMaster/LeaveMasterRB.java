/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveMasterRB.java
 *
 * 
 */
package com.see.truetransact.ui.payroll.leaveMaster;

/*
 * @author anjuanand
 *
 */
import java.util.ListResourceBundle;

public class LeaveMasterRB extends ListResourceBundle {

    public LeaveMasterRB() {
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
        {"lblLeaveId", "Leave Id"},
        {"lblLeaveDescription", "Leave Description"},
        {"lblLeaveDedPerDay", "Deduction"},
        {"lblDeduction", "Deduction"},
        {"rdoDeduction_Yes", "Yes"},
        {"rdoDeduction_No", "No"},
        {"rdoHalfPay", "Half-Pay"},
        {"rdoFullPay", "Full-Pay"}
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadConsolidationRB.java
 * 
 * Created on Wed Feb 02 12:50:41 IST 2015
 */
package com.see.truetransact.ui.payroll.voucherprocessing;

import com.see.truetransact.ui.agent.*;

import java.util.ListResourceBundle;

public class HeadConsolidationRB extends ListResourceBundle {

    public HeadConsolidationRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblName", "Name"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblStatus1", "                      "},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"lblAgentID", "Agent ID"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblNameValue", ""},
        {"btnPrint", ""},
        {"lblRemarks", "Remarks"},
        {"txtMapHead", "Map Head"},
        {"cboHeads", "Mapping Heads"}
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductRB.java
 *
 * Created on Mon Apr 11 12:08:48 IST 2005
 */

package com.see.truetransact.ui.sysadmin.leavemanagement;

import java.util.ListResourceBundle;

public class LeaveManagementRB extends ListResourceBundle {
    public LeaveManagementRB(){
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
        {"btnShareAccount", ""},
        {"lblStatus", " "},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblTypeOfLeave", "Type Of Leave"},
        {"lblDesc", "Descriptor"},
        {"lblLeaveLapses", "Leave Lapses"},
        {"lblCarryOver", "Carry Over For How Many Years"},
        {"lblAccAllowed", "Accumalation Allowed"},
        {"lblParForLeave", "Parameters For Leave Crediting"},
        {"lblProRatelbl2", "Days Worked"},
        {"lblProRatelbl1", "For Every"},
        {"lblProRatelbl3", "Day/s Credited"},
        {"lblMaxLeaves", "Max Number Of Leaves Credited"},
        {"lblEncash", "Leave Encashment Allowed"},
        {"lblMaxEncash", "Max Encashment Allowed"},
        {"lblCreditType", "To Be Credited"},
        {"lblFixedLeave", "Leave For"}
        
    };
    
}

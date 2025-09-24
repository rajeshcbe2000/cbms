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

public class LeaveSanctionRB extends ListResourceBundle {
    public LeaveSanctionRB(){
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
        {"tblColumn1","Sl.No"},
        {"tblColumn2","LeaveType"},
        {"tblColumn3","Days"},
        {"tblColumn4","Req From"},
        {"tblColumn5","Req To"},
        {"tblColumn6","Pay Type"},
        
        {"lblSanEmpID", "Emp ID"},
        {"lblLeaveBalanceAsOn", "Balance As On Date"},
        {"lblLeaveLastCrDt", "Last Credited Date"},
        {"lblLeaveNextCrDt1", "Next Credit Date"},
        {"btnEnq", "Enquiry"},
        {"lblLeaveType9", "Process Type"},
        {"lblPFNumber7", "Old Sanction No"},
        {"lblLoanSanctionDate9", "Leave Appl Date"},
        {"lblDateOfJoin10", "Leave Req From"},
        {"lblDateOfJoin9", "Leave Req To"},
        {"lblPFNumber6", "No Of Days"},
        {"lblLoanRateofInterest23", "Purpose Of Leave"},
        {"lblPFNumber5", "Sanction Ref No"},
        {"lblReportedOn3", "Sanction Date"},
        {"lblEnqLeaveType","Leave Type"},
        {"btnRejAppl","Reject Application"},
        {"lblTabDays","No Of Days"},
        {"lblTabLeaveType","Leave Type"}
        
        
    };
    
}

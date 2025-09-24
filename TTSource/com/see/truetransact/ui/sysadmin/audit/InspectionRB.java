/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InspectionRB.java
 *
 * Created on Wed Jun 09 10:54:35 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.audit;

import java.util.ListResourceBundle;

public class InspectionRB extends ListResourceBundle {
    public InspectionRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnTabSave", "Save"},
        {"lblValueRegion", ""},
        {"lblWorkingHours", " Working Hours"},
        {"btnAuthorize", ""},
        {"lblBranchCode", "Branch Code"},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblWeeklyHoliday", "Weekly Holiday"},
        {"lblSpace4", "     "},
        {"lblClassification", "Classification"},
        {"lblInspCommOn", "Inspection Commenced On"},
        {"lblActualPosition", "Actual Position"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"btnBranchCode", ""},
        {"lblSpace1", " Status :"},
        {"lblInspectingOfficials", "Inspecting Officials"},
        {"btnDelete", ""},
        {"btnTabDelete", "Delete"},
        {"lblOpeningDate", "Opening Date"},
        {"lblValueName", ""},
        {"lblInspConcludedOn", "Inspection Concluded On"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblBranchRating", "Branch Rating"},
        {"lblCategory", "Category"},
        {"lblValueOpeningDate", ""},
        {"btnTabNew", "New"},
        {"btnNew", ""},
        {"lblPositionAsOn", "Position As On"},
        {"lblManDays", "No. Of ManDays"},
        {"btnCancel", ""},
        {"lblRegion", "Region"},
        {"lblBranchName", "Branch Name"},
        {"btnPrint", ""},
        {"lblStaffPosition", "Staff Position"},
        {"lblValueWorkingHours", ""},
        {"lblJobCategory", "Job Category"},
        {"lblOtherInfo", "Other Information"},
        {"lblbranchCode", "Branch Code"},
        {"lblValueBranchCode", ""},
        {"lblbranchName", "Branch Name"},
         {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        
        {"TOCommandError", ""},
        
        {"existanceWarning", " JobCategory Selected Already exists. Do you want to modify it ?"},
        {"lblValueBranchName", ""},
        {"lblAuditId", "Audit Id"},
        {"lblValueAuditId", ""},
        {"columnHeading1","Inspection Duration"},
        {"columnHeading2","Total Deposits"},
        {"columnHeading3", "Total Advances"},
        {"columnHeading4", "Priority Sector Advances"}
        
    };
}

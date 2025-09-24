/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InspectionMRB.java
 * 
 * Created on Wed Jun 09 11:36:08 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.audit;

import java.util.ListResourceBundle;

public class InspectionMRB extends ListResourceBundle {
    public InspectionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtActualPosition", "ActualPosition should not be empty!!!"},
        {"txtStaffPosition", "StaffPosition should not be empty!!!"},
        {"txtBranchCode", "BranchCode should not be empty!!!"},
        {"tdtInspectionConcludedOn", "InspectionConcludedOn should not be empty!!!"},
        {"txaOtherInformation", "OtherInformation should not be empty!!!"},
        {"txtNumberOfManDays", "NumberOfManDays should not be empty!!!"},
        {"cboCategory", "Category should be a proper value!!!"},
        {"tdtInspectionCommencedOn", "InspectionCommecedOn should not be empty!!!"},
        {"cboClassification", "Classification should be a proper value!!!"},
        {"cboJobCategory", "JobCategory should be a proper value!!!"},
        {"cboBranchRating", "BranchRating should be a proper value!!!"},
        {"cboWeeklyHoliday", "WeeklyHoliday should be a proper value!!!"},
        {"txaInspectingOfficials", "InspectingOfficials not be empty!!!"},
        {"tdtPositionAsOn", "PositionAsOn should not be empty!!!"} 

   };

}

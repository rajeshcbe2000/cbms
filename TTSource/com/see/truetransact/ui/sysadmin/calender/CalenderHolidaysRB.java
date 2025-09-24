/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CalenderHolidaysRB.java
 * 
 * Created on Fri Jun 11 10:41:05 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.calender;

import java.util.ListResourceBundle;

public class CalenderHolidaysRB extends ListResourceBundle {
    public CalenderHolidaysRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"panLeft", ""},
        {"btnYearDec", ""},
        {"rdoWeeklyOff_No", "No"},
        {"lblMsg", ""},
        {"lblWeeklyOff1", "Weekly Off 1"},
        {"btnYearInc", ""},
        {"lblHalfDay1", "Half Day 1"},
        {"lblSpace2", "     "},
        {"rdoWeeklyOff_Yes", "Yes"},
        {"btnSave", ""},
        {"lblYear", "Year"},
        {"panWeek", "Weekly Off"},
        {"lblSpace3", " Status :"},
        {"lblWeeklyOff", "Weekly Off"},
        {"lblStatus", "                      "},
        {"lblSpace1", "     "},
        {"lblRemarks", "Remarks"},
        {"lblMonth", "Month"},
        {"btnDelete", ""},
        {"panHoliday", "Holiday List"},
        {"lblWeeklyOff2", "Weekly Off 2"},
        {"btnEdit", ""},
        {"lblHalfDay2", "Half Day 2"},
        {"lblDate", "Date"},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblHoliday", "Holiday Name"},
        {"btnPrint", ""},
        
       {"btnYearInc", ""},
        {"btnYearDec", ""},
        
        {"lblHolidayDate", "Date"},
        {"lblHolidayName", "Name"},
        {"lblHolidayRemarks", "Remarks"},
        
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        
        {"cDialogOk", "Ok"},
        
        {"TOCommandError", ""},
        
        {"existanceWarning", "The Name and Remarks for this Holiday Date Already exist. Do you want to modify it ?"},
        {"comboEmptyWarning", "Atleast one combo has to be filled"},
        {"comboUniqueWarning", "This data should be unique"},
        {"deleteWarning","Are you sure you want to Delete?"},
        {"todayOrPriorDateWarning","You cannot set Today or prior date as Holiday!!!" }

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * CalenderHolidaysMRB.java
 * 
 * Created on Tue Jun 08 17:30:06 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.calender;

import java.util.ListResourceBundle;

public class CalenderHolidaysMRB extends ListResourceBundle {
    public CalenderHolidaysMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoWeeklyOff_Yes", "Weekly Off should be selected!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"cboWeeklyOff2", "Weekly Off 2 should be a proper value!!!"},
        {"cboHalfDay2", "Half Day 2 should be a proper value!!!"},
        {"txtHolidayName", "Holiday Name should not be empty!!!"},
        {"txtYear", "Year should not be empty!!!"},
        {"cboWeeklyOff1", "Weekly Off 1 should be a proper value!!!"},
        {"cboMonth", "Month should be a proper value!!!"},
        {"cboDate", "Date should be a proper value!!!"},
        {"cboHalfDay1", "Half Day 1 should be a proper value!!!"} 
   };

}

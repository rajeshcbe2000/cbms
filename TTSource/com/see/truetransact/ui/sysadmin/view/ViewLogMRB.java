/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ViewLogMRB.java
 * 
 * Created on Mon Apr 19 11:38:01 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.view;

import java.util.ListResourceBundle;

public class ViewLogMRB extends ListResourceBundle {
    public ViewLogMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"dateFromDate", "dateToDate should be a proper value!!!"},
        {"cboActivity", "Activity should be a proper value!!!"},
        {"cboScreen", "Screen should be a proper value!!!"},
        {"txtLatestEntries", "LatestEntries should not be empty!!!"},
        {"cboFindbyHistory", "FindbyHistory should be a proper value!!!"},
        {"cboIPAddress", "IPAddress should be a proper value!!!"},
        {"cboUserID", "UserID should be a proper value!!!"},
        {"cboBranchID", "BranchID should be a proper value!!!"},
        {"cboModule", "Module should be a proper value!!!"},
        {"dateToDate", "dateToDate should be a proper value!!!"} 

   };

}

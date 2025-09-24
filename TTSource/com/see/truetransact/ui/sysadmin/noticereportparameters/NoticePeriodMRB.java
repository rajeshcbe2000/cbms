/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NoticePeriodMRB.java
 */

package com.see.truetransact.ui.sysadmin.noticereportparameters;
import java.util.ListResourceBundle;
public class NoticePeriodMRB extends ListResourceBundle {
    public NoticePeriodMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboGrDet", "GuarantorDetails should not be empty!!!"},
        {"cboLan", "Language should not be empty!!!"},
        {"txtReportName", "Report Name should not be empty!!!"} ,
        {"jTextPaneData", "Data should not be empty!!!"},
        {"txtRepotHeading", "Report Heading should not be empty!!!"} 
        

   };

}

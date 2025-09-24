/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

import java.util.ListResourceBundle;

public class ServiceWiseLoanIssueConfigurationMRB extends ListResourceBundle {
    public ServiceWiseLoanIssueConfigurationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtEffectFrom", "Please select Effect From!!!"},
        {"txtPastServicePeriod", "Please select Past Service Period!!!"},
        {"txtFromAmt", "Please select From Amount!!!"},
        {"txtToAmt","Please select To Amount!!!"},
        {"txtNoOfSuretiesRequired","Please select Number of Sureties Required!!!"}
      //  {"txtTotalAttandance", "TotalAttendance  should be a proper value!!!"}
        
           };

}

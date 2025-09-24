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

public class ServiceWiseLoanAmountMRB extends ListResourceBundle {
    public ServiceWiseLoanAmountMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtFromServicePeriod", "From Service Period should be a proper value!!!"},
        {"txtToServicePeriod", "To Service Period should be a proper value!!!"},
        {"txtMaximumLoanAmount", "Maximum Loan Amount should be a proper value!!!"},
        {"txtMinimumLoanAmount", "Minimum Loan Amount should be a proper value!!!"},
        {"tdtFromDate", "Effect From Date should be a proper value!!!"}
      //  {"txtTotalAttandance", "TotalAttendance  should be a proper value!!!"}
        
           };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureMRB.java
 * 
 * Created on Tue Aug 12 2014
 * 
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.salaryStructure;

import java.util.ListResourceBundle;

public class SalaryStructureMRB extends ListResourceBundle {

    public SalaryStructureMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"cboDesignation", "Please select Designation !!!"},
        {"tdtFromDate", "Enter From Date !!!"},
        {"txtStartingAmount", "Starting Amount should be a proper value !!!"},
        {"txtEndingAmount", "Ending Amount should be a proper value !!!"},
        {"txtIncrementAmount", "Increment Amount should be a proper value !!!"},
        {"txtNoOfIncrements", "No of Increments should be a proper value !!!"},
        {"cboIncrementFrequency", "Please select Increment Frequency!!!"}
    };
}

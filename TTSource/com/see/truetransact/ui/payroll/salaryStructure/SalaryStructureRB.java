/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SalaryStructureRB.java
 * 
 * Created on Tue Aug 12 2014
 * 
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.salaryStructure;

import java.util.ListResourceBundle;

public class SalaryStructureRB extends ListResourceBundle {

    public SalaryStructureRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"btnAuthorize", ""},
        {"btnCancel", ""},
        {"btnClose", ""},
        {"btnDelete", ""},
        {"btnEdit", ""},
        {"btnException", ""},
        {"btnNew", ""},
        {"btnPrint", ""},
        {"btnReject", ""},
        {"btnSave", ""},
        {"lblDesignation", "Designation"},
        {"lblScaleId", "Scale Id"},
        {"lblVersionNo", "Version No"},
        {"lblFromDate", "From Date"},
        {"lblToDate", "To Date"},
        {"lblStartingAmount", "Scale Starting Basic Amount"},
        {"lblEndingAmount", "Scale Ending Basic Amount"},
        {"lblIncrementAmount", "Increment Amount(NI)"},
        {"lblNoOfIncrements", "No.of Increments"},
        {"lblIncrementFrequency", "Increment Frequency"},
        {"lblStagnationIncrement", "Stagnation Increment"},
        {"lblSalaryStructureStagnationIncrement", ""},
        {"lblTotalNoOfStagnationIncrements", "Total No of Stagnation Increments"},
        {"lblStagIncrAmount", "Increment Amount(SI)"},
        {"lblStagNoOfIncrements", "No.of Increments"},
        {"lblIncrementPeriod", "Increment Period"},
        {"rdoStagnationIncrement_Yes", "Yes"},
        {"rdoStagnationIncrement_No", "No"},
        {"lblSalaryStructureStagnationIncrement", "Stagnation Increment"},
        {"lblTotalNoOfStagnationIncrements", "Total No of Stagnation Increments"},
        {"lblStagIncrAmount", "Increment Amount(SI)"},
        {"lblStagNoOfIncrements", "No.of Increments"},
        {"lblIncrementPeriod", "Increment Period"}
    };
}

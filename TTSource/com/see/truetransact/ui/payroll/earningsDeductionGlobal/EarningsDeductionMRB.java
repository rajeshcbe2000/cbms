/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EarningsDeductionMRB.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.earningsDeductionGlobal;

import java.util.ListResourceBundle;

public class EarningsDeductionMRB extends ListResourceBundle {

    public EarningsDeductionMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"rdoEarnings", "Please select Earnings!!!"},
        {"rdoDeduction", "Please select Deduction!!!"},
        {"rdoContra", "Please select Contra!!!"},
        {"cboModuleType", "Please select Module Type!!!"},
        {"txtDescription", "Please enter Description!!!"},
        {"cboCalculationType", "Please select Calculation Type!!!"},
        {"txtAmount", "Please enter Amount!!!"},
        {"txtAccountNo", "Please enter Account Head!!!"},
        {"cboAccountType", "Please select Account Type!!!"},
        {"cboProductType", "Please select Product Type!!!"},
        {"cboCalcModType", "Please select Calculation Module Type!!!"},
        {"txtPercentage", "Please enter Percentage!!!"},
        {"txtMinAmt", "Please enter Minimum Amount!!!"},
        {"txtMaxAmt", "Please enter Maximum Amount!!!"},
        {"tdtFromDate", "Please enter From Date!!!"},
        {"chkGl", "Please select whether GL or not!!!"},
        {"cboProdType", "Please select Product Type!!!"}
    };
}

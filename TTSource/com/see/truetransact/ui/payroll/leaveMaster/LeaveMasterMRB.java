/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LeaveMasterMRB.java
 * 
 */
package com.see.truetransact.ui.payroll.leaveMaster;

/**
 *
 * @author anjuanand
 */
import java.util.ListResourceBundle;

public class LeaveMasterMRB extends ListResourceBundle {

    public LeaveMasterMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboLeaveDescription", "Please select Leave Description!!!"},
        {"rdoDeduction_Yes", "Please select if Deduction is Yes!!!"},
        {"rdoDeduction_No", "Please select if Deduction is No!!!"},
        {"rdoHalfPay", "Please select if Half Pay!!!"},
        {"rdoFullPay", "Please select if Full Pay!!!"}
    };
}

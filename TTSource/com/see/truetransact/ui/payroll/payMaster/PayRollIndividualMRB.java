/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * PayRollIndividualMRB.java
 */
package com.see.truetransact.ui.payroll.payMaster;

import java.util.ListResourceBundle;
public class PayRollIndividualMRB extends ListResourceBundle {
    public PayRollIndividualMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoYes_Executed_DOC", ""},
        {"rdoNo_Executed_DOC", ""},
        {"rdoYes_Mandatory_DOC", ""},
        {"rdoNo_Mandatory_DOC", ""},
        {"txtEmployeeID", "Select Any Employee.."},
        {"cboPayCodes", "Select Any Pay Codes.."},
        {"txtAmount", "Enter the Amount."},
        {"tdtFromDt", "Enter the  Recovery Date."},
        {"txtRecoveryMonth", "Enter the Recovery Month."},
    };
}

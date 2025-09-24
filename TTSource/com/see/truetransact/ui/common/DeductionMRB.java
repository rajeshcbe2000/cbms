/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeductionMRB.java
 * 
 * Created on Wed Jun 02 10:45:38 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class DeductionMRB extends ListResourceBundle {
    public DeductionMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {    
        {"txtEmployeeId", "Employee id should be a proper value!!!"},
        {"cboDeductionTypeValue", "Deduction type should be a proper value!!!"},
        {"txtFromDateMMValue", "From date month should be a proper value!!!"},
        {"txtFromDateYYYYValue", "From date year should be a proper value!!!"},
        {"txtToDateMMValue", "To date month should be a proper value!!!"},
        {"txtToDateYYYYValue", "To date year should be a proper value!!!"},
        {"txtPremiumAmtValue", "Amount should be a proper value!!!"},
        {"txtLossOfPayDays","Loss of pay shud be a proper value!!!"},
        {"txtCreditingACNo", "Crediting account head should be a proper value!!!"},                
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * EmpSalaryStructureMRB.java
 * 
 * Created on Sat Feb 26 14:11:19 GMT+05:30 2011
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class EmpSalaryStructureMRB extends ListResourceBundle {
    public EmpSalaryStructureMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoBasedOnBasic_Yes", "BasedOnBasic should be selected!!!"},
        {"tdtSalFromDate", "SalFromDate should not be empty!!!"},
        {"txtAllowanceID", "AllowanceID should not be empty!!!"},
        {"tdtSalToDate", "SalToDate should not be empty!!!"},
        {"rdoPercentOrFixed_Percent", "PercentOrFixed should be selected!!!"},
        {"txtFromAmount", "FromAmount should not be empty!!!"},
        {"txtAllowanceAmount", "AllowanceAmount should not be empty!!!"},
        {"rdoEarnOrDed_Earning", "EarnOrDed should be selected!!!"},
        {"txtMaxAmount", "MaxAmount should not be empty!!!"},
        {"txtAllowanceType", "AllowanceType should not be empty!!!"},
        {"txtToAmount", "ToAmount should not be empty!!!"} 

   };

}

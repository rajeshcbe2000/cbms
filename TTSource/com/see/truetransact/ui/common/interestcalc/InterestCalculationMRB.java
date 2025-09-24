/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * InterestCalculationMRB.java
 * 
 * Created on Wed Mar 24 10:17:52 GMT+05:30 2004
 */

package com.see.truetransact.ui.common.interestcalc;

import java.util.ListResourceBundle;

public class InterestCalculationMRB extends ListResourceBundle {
    public InterestCalculationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtPrincipal", "Principal should not be empty!!!"},
        {"tdtFromDate", "FromDate should not be empty!!!"},
        {"cboCompounded", "Compounded should be a proper value!!!"},

        //added Deposits
        {"cboDepositsCompounded", "Deposits Compounded should be a proper value!!!"},
        
        {"rdoReport_Summary", "Report should be selected!!!"},
        {"cboAccountType", "AccountType should be a proper value!!!"},
        {"rdoInterestOption_Simple", "InterestOption should be selected!!!"},
        {"txtPenalRate", "PenalRate should not be empty!!!"},
        {"txtInterestCreditHead", "InterestCreditHead should not be empty!!!"},
        {"txtInterestDebitHead", "InterestDebitHead should not be empty!!!"},
        {"cboFloatPrecision", "FloatPrecision should be a proper value!!!"},
        {"txtRateofInterest", "RateofInterest should not be empty!!!"},
        {"cboMonth", "Month should be a proper value!!!"},
        {"cboRoundingInterest", "RoundingInterest should be a proper value!!!"},
        {"tdtToDate", "ToDate should not be empty!!!"},
        {"cboYear", "Year should be a proper value!!!"},
        {"cboRoundingPrincipal", "RoundingPrincipal should be a proper value!!!"},
        {"txtGracePeriod", "GracePeriod should not be empty!!!"},
        {"cboGracePeriod", "GracePeriod should be a proper value!!!"},
        {"txtDays", "Days should not be empty!!!"},
        {"txtYears", "Years should not be empty!!!"},
        {"txtMonths", "Months should not be empty!!!"},
        {"rdoPeriodOption_Duration", "PeriodOption should be selected!!!"} 
   };

}

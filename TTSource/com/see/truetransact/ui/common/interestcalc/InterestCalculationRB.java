/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * InterestCalculationRB.java
 * 
 * Created on Wed Mar 24 16:42:19 GMT+05:30 2004
 */

package com.see.truetransact.ui.common.interestcalc;

import java.util.ListResourceBundle;

public class InterestCalculationRB extends ListResourceBundle {
    public InterestCalculationRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"btnSave", ""},
        {"lblTotalInterest", "Total Interest"},
        {"btnCancel", ""},
        {"rdoReport_Details", "Details"},
        {"lblAccountTo", "To Account"},
        {"rdoInterestOption_Fixed", "Fixed"},
        {"lblAccountType", "Account Type"},
        //added Deposits
        {"lblDepositsInterest","DepositsInterest"},
        {"lblDepositsInstallment","DepositsInstallment"},
        {"lblSpace", " Status :"},
        {"lblPenalRate", "Penal Rate"},
        {"lblMsg", ""},
        {"lblMonth", "Month"},
        {"lblDuration", "Duration"},
        {"rdoInterestOption_Floating", "Floating"},
        {"lblPercentageOfRate", "%"},
        {"btnInterestDebitHead", ""},
        {"lblRoundingPrincipal", "Principal Rounding off Factor"},
        {"lblAccountFrom", "From Account"},
        {"lblCompounded", "Interest Comp Freq"},
        {"rdoPeriodOption_Date", "Date"},
        {"rdoReport_Summary", "Summary"},
        {"lblFloatPrecision", "Float Precision"},
        {"lblRateofInterest", "Rate of Interest"},
        {"rdoInterestOption_Simple", "Simple"},
        {"lblFromDate", "From"},
        {"lblInterestDebitHead", "Interest Debit Head"},
        {"lblMonths", "Months"},
        {"lblStatus", "                      "},
        {"lblSpace3", "     "},
        {"lblSpace2", "     "},
        {"rdoPeriodOption_Duration", "Duration"},
        {"lblYears", "Years"},
        {"btnInterestCreditHead", ""},
        {"panPeriod", "Period"},
        {"lblRoundingInterest", "Interest Rounding off Factor"},
        {"lblProductID", "Product ID"},
        {"btnPrint", ""},
        {"lblDays", "Days"},
        {"lblPenalRatePercentage", "%"},
        {"btnDelete", ""},
        {"btnNew", ""},
        {"lblToDate", "To"},
        {"panRateOfInterest", "Rate of Interest"},
        {"lblPeriodOpt", "Duration/Date"},
        {"lblInterestCreditHead", "Interest Credit Head"},
        {"lblPrincipal", "Principal Amount"},
        {"lblInterestOption", "Interest Option"},
        {"lblYear", "Year"},
        {"rdoInterestOption_Compound", "Compound"},
        {"lblReport", "Report"},
        {"btnEdit", ""},
        {"lblGracePeriod", "Grace Period"} ,
        {"TBL_DEP_SUB_NO_COLUMN_1","Payment Date"},
        {"TBL_DEP_SUB_NO_COLUMN_2","Period"}

   };

}

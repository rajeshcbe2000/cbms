/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * SalaryStructureRB.java
 * 
 * Created on Wed Jun 02 10:35:02 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class MisecllaniousDeductionRB extends ListResourceBundle {
    public MisecllaniousDeductionRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblDepositAmt","Deposit Amount"},
        {"lblDepositAmtValue",""},
        {"lblFreezeSum","Sum of Authorized Freezes"},
        {"lblFreezeSumValue",""},
        {"lblShadowLien","Shadow Lien"},
        {"lblShadowLienValue",""},
        {"lblDepositLienDesc", ""},
        {"UNLIEN_REMARK","UnLien Remark"},
        {"btnAuthorize",""},
        {"btnCancel",""},
        {"btnClose",""},
        {"btnDelete",""},
        {"btnEdit",""},
        {"btnException",""},
        {"btnNew",""},
        {"btnPrint",""},
        {"btnReject",""},
        {"btnSave",""},
        
        {"lblHaltingSLNO", "SL No"},
        {"lblHaltingDesignation","Grade"},
        {"lblHaltingFromDate","From Date"},
        {"lblHaltingToDate","To Date"},
        {"lblHaltingllowanceType","Allowance Type"},
        {"lblHaltingParameterBasedon","Parameter Based on"},
        {"lblHaltingSubParameter","Sub Parameter"},
        {"lblHaltingFixedAmt","Amount"},
        {"lblHaltingPercentage","Percentage"},
        {"lblHaltingMaximumOf","Maximum of"},
        
        {"lblMDSLNO","SL No"},
        {"lblMisecllaniousDeduction","Designation"},
        {"lblMDDeductionType","Deduction Type"},
        {"lblMDFromDate","From Date"},
        {"lblMDToDate","To Date"},
        {"lblMDPercentage","% of amount to Deduct"},
        {"lblMDMaximumOf","Maximum Amount"},
        {"lblMDEligibleAllowances","Eligible Allowances"},
        {"lblMDEligiblePercentage","% of amount eligible"},
        
        {"lblGratuitySLNO","SL No"},
        {"lblGratuityCityType","Gratuity Rule 1 :"},
        {"lblGratuityDesignation","Designation"},
        {"lblGratuityFromDate","From Date"},
        {"lblGratuityToDate","To Date"},
        {"lblGratuityUpto","Up to"},
        {"lblGratuityYearofService","Years of Service for each"},        
        {"lblGratuityCompleted","completed year of"},        
        {"lblGratuityMonthPay","Month/s pay"},
        {"lblGratuityMaximumOf","With a maximum of"},
        {"lblGratuityMonths","AND"},
        {"lblGratuityUptoMonths","Months pay"},
        {"lblGratuityBeyond","Beyond"},
        {"lblYearOfService","Years of Service for each"},
        {"lblCompletedYearOfService","completed year of Service"},
        {"lblGratuityBeyondMonthPay","Month/s pay"},
        {"lblGratuityMaximumAmtBeyond","Maximum Gratuity amount Payable"}
    };
    
}

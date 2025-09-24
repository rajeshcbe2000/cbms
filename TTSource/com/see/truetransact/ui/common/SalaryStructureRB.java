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

public class SalaryStructureRB extends ListResourceBundle {
    public SalaryStructureRB(){
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
        
        {"lblSalaryStructureSLNO", "SL No"},
        {"lblSalaryStructureProdId","Grade"},
        {"lblSalaryStructureFromDate","From Date"},
        {"lblSalaryStructureToDate","To Date"},
        {"lblSalaryStructureStartingAmt","Scale Starting Basic Amount"},
        {"lblSalaryStructureEndingAmt","Scale Ending Basic Amount"},
        {"lblSalaryStructureAmt","Increment Amount"},
        {"lblSalaryStructureIncYear","No.of Increments"},
        {"lblStagnationIncrement","Stagnation Increment"},
        {"lblSalaryStructureTotNoInc","Total No of Stagnation Increments"},
        {"lblSalaryStructureStagnationAmt","Stagnation Increment Amount(SI)"},
        {"lblSalaryStructureNoOfStagnation","No.of Increments"},
        {"lblSalaryStructureStagnationOnceIn","Once in"},
        
        {"lblCCAllowanceSLNO","SL No"},
        {"lblCCAllowanceCityType","City Type"},
        {"lblCCAllowance","Grade"},
        {"lblCCAllowanceFromDate","From Date"},
        {"lblCCAllowanceToDate","To Date"},
        {"lblCCAllowanceStartingAmt","CCA %"},
        {"lblCCAllowanceStartingAmt2","CCA Fixed Amt"},
        {"lblCCAllowanceEndingAmt","Max CCA"},
        {"lblFromAmount","From Amount"},
        {"lblToAmount","To Amount"},
        {"lblPercentOrFixed","Allowance In"},
        
        {"lblHRAllowanceSLNO","SL No"},
        {"lblHRAllowanceCityType","City Type"},
        {"lblHRAllowanceDesignation","Grade"},
        {"lblHRAllowanceFromDate","From Date"},
        {"lblHRAllowanceToDate","To Date"},
        {"lblHRAllowanceStartingAmt","HRA %"},
        {"lblHRAllowanceEndingAmt","Max HRA"},
        {"lblHRAPayable","Quarters Provided whether HRA Payable"},
        
        {"lblDASLNO","SL No"},
        {"lblDADesignation","Grade"},
        {"lblDAFromDate","From Date"},
        {"lblDAToDate","To Date"},
        {"lblDANoOfPointsPerSlab","No. of Points Per Slab"},
        {"lblDAIndex","Index"},
        {"lblDATotalNoofSlab","Percentage Per Slab"},
        {"lblTotalDaPer","Total No.of Slab"},
        {"lblDAPercentagePerSlab","Total DA Percentage"},

        {"lblTravellingAllowanceSLNO","SL No"},
        {"lblTravellingAllowanceDesg","Grade"},
        {"lblTAFromDate","From Date"},
        {"lblTAToDate","To Date"},
        {"lblFixedConveyance","Fixed Conveyance"},
        {"lblPetrolAllowance","Petrol Allowance"},
        {"lblBasicAmtUpto","Basic Amount Up to"},
        {"lblConveyancePerMonth","Conveyance Per Month"},
        {"lblBasicAmtBeyond","Basic Amount Beyond"},
        {"lblConveyanceAmt","Conveyance Amount"},
        {"lblNoOflitres","No of litres of petrol per month"},
        {"lblPricePerlitre","Price Per litre"},
        {"lblTotalConveyanceAmt","Total Conveyance Amount"},

        {"lblMASLNO","SL No"},
        {"lblMAidDesg","Grade"},
        {"lblMAFromDate","From Date"},
        {"lblMAToDate","To Date"},
        {"lblMAAmt","Medical Aid per annum"},
        
        {"lblOASLNO","SL No"},
        {"lblOADesignation","Grade"},
        {"lblOAFromDate","From Date"},
        {"lblOAToDate","To Date"},
        {"lblOAllowanceType","Allowance Type"},
        {"lblOAParameterBasedon","Parameter Based on"},
        {"lblOASubParameter","Sub Parameter"},
        {"lblOAFixed","Fixed"},
        {"lblOAPecentage","Percentage"},
        {"lblOAFixedAmt","Fixed Amount"},
        {"lblOAPercentage","Percentage"},
        {"lblOAMaximumOf","Maximum of"}
        
//        {"LIENSUM_WARNING","Lien Amount is more than available balance"},
//        {"LIENDATE_WARNING","Improper Lien Date. Lien Date cannot be before"},
//        {"AUTHORIZE_WARNING","Insufficient fund to fulfill your request"},
//        {"DEPOSIT_DATE","Deposit Date : "},
//        {"AOD_DATE","Lien Account Opening Date : "},
//        {"ZEROAMOUNT_WARNING","Not a valid amount"}
    };
    
}

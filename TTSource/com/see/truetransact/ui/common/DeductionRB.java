/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeductionRB.java
 * 
 * Created on Wed Jun 02 10:35:02 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class DeductionRB extends ListResourceBundle {
    public DeductionRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"deleteWarningMsg", "Are you sure you want to delete the earning selected?"},
        {"cDialogYes", "Yes"},
        {"CDialogNo", "No"},
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
        
        {"lblDeductionSLNO", "SL no"},
        {"lblEmployeeId", "Employee id"},
        {"lblEmployeeName", "Employee name"},
        {"lblDesignation", "Designation"},
        {"lblEmployeeBranch", "Employee branch"},
        {"lblDeductionType", "Deduction type"},
        {"lblAllowanceType", "Deduction towards"},
        {"lblDeductionTypeFromDate", "From date"},
        {"lblDeductionTypeToDate", "To date"},
        {"lblPremiumAmt", "Amount to be deducted"},
        {"lblCreditingACNo", "Deducting account head"},
        {"lblFromDateFormatValue", "MM      -     YYYY"},
        {"lblToDateFormatValue", "MM      -     YYYY"},
        {"lblLossOfPayDays","No Of Days:"},
        {"lblCreditDesigValue","Grade"}
        
                
//        {"LIENSUM_WARNING","Lien Amount is more than available balance"},
//        {"LIENDATE_WARNING","Improper Lien Date. Lien Date cannot be before"},
//        {"AUTHORIZE_WARNING","Insufficient fund to fulfill your request"},
//        {"DEPOSIT_DATE","Deposit Date : "},
//        {"AOD_DATE","Lien Account Opening Date : "},
//        {"ZEROAMOUNT_WARNING","Not a valid amount"}
    };
    
}

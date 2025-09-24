/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanChargesRB.java
 * 
 * Created on Mon Aug 29 13:15:20 IST 2011
 */

package com.see.truetransact.ui.supporting.arccharges;

import java.util.ListResourceBundle;

public class ARCChargesRB extends ListResourceBundle {
    public ARCChargesRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblChargeDesc", "Charge Description"},
        {"lblFromSlabAmount", "From Slab Amount"},
        {"rdoDeductionAccClose_no", "Account Closing Time"},
        {"btnClose", ""},
        {"btnAuthorize", ""},
        {"lblAccGroupId", " Account Group ID"},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", " Status :"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblChargeBase", "Charge Base"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"rdoMandatory_yes", "Mandatory"},
        {"lbDivisibleBy", "Divisible By"},
        {"lblMaximumChrgAmt", "Maximum Charge Amount"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnAccGroupId", ""},
        {"lblRoundOffType", "Round Off Type"},
        {"lblFlatCharges", "Flat Charges"},
        {"btnNew", ""},
        {"lblToSlabAmount", "To Slab Amount"},
        {"lblSchemeId", "Scheme Id"},
        {"lblChargeId", "Charge Id"},
        {"btnCancel", ""},
        {"lblSpace6", "     "},
        {"rdoMandatory_no", "Not Mandatory"},
        {"lblChargeRate", "Charge Rate(%)"},
        {"lblMinimumChrgAmt", "Minimum Charge Amount"},
        {"lblDeductionOccu", "Deduction Occurrence"},
        {"btnPrint", ""},
        {"rdoDeductionAccOpen_yes", "Account Opening Time"} 

   };

}

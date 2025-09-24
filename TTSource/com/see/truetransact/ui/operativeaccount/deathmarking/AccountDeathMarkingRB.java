/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeathMarkingRB.java
 *
 * Created on Thu Jun 03 12:44:43 GMT+05:30 2004
 */

package com.see.truetransact.ui.operativeaccount.deathmarking;

import java.util.ListResourceBundle;

public class AccountDeathMarkingRB extends ListResourceBundle {
    public AccountDeathMarkingRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"lblAccountHead", "AccountHead"},
        {"lblShadowCredit", "Shadow Credit"},
        {"lblNomineeAvailable", "Nominee Available"},
        {"lblReportedOn", "Reported On"},
        {"lblAvailableBalance", "Available Balance"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"lblRelationship", "Relationship"},
        {"lblClearingBalance", "Clearing Balance"},
        {"lblProductId", "Product Id"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblValueBalance", ""},
        {"lblValueCustName", ""},
        {"lblSpace3", "     "},
        {"lblSettlementMode", "Settlement Mode"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnAccountNumber", ""},
        {"lblShadowDebit", "Shadow Credit"},
        {"lblValueNominee", ""},
        {"lblRemarks", "Remarks"},
        {"lblValueClearing", ""},
        {"btnDelete", ""},
        {"lblCustomerName", "CustomerName"},
        {"lblDateOfDeath", "Date of Death"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblHdValue", ""},
        {"btnNew", ""},
        {"lblValueCredit", ""},
        {"btnCancel", ""},
        {"cDialogOK", "OK"},
        {"reportedMsg", "ReportedOn Date should be after the DateofDeath"},
        {"currentDateMsg", "Date entered should be lessthan or equal to the CurrentDate"},
        {"lblValueDebit", ""},
        {"lblReportedBy", "Reported By"},
        {"btnPrint", ""},
        {"lblValueSettlement", ""},
        {"lblAccountNumber", "Account Number"},
        {"lblReferenceNo", "Reference No."}
        
    };
    
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathMarkingRB.java
 *
 * Created on Wed May 26 17:17:56 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.deathmarking;

import java.util.ListResourceBundle;

public class DeathMarkingRB extends ListResourceBundle {
    public DeathMarkingRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"btnNew", ""},
        {"btnDelete", ""},
        {"btnCustomerId", ""},
        {"lblDateOfDeath", "Date of Death"},
        {"btnEdit", ""},
        {"lblDate", "Date"},
        {"lblReportedOn", "Reported On"},
        {"lblMsg", ""},
        {"lblHdValue", ""},
        {"lblRelationship", "Relationship"},
        {"lblCustomerName", "CustomerName"},
        {"lblSpace2", "     "},
        {"lblSpace4", "     "},
        {"btnSave", ""},
        {"btnAuthorize", ""},
        {"btnReject", ""},
        {"btnException", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblReportedBy", "Reported By"},
        {"cDialogOK", "OK"},
        {"reportedMsg", "ReportedOn Date should be after the DateofDeath"},
        {"currentDateMsg", "Date entered should be lessthan or equal to the CurrentDate"},
        {"btnPrint", ""},
        {"lblCustomerId", "Customer Id"},
        {"lblReferenceNo", "Reference No."},
        {"lblRemarks", "Remarks"},
        {"lblDepositNo", "Deposit No."},
        {"lblTotalBalance", "Total Balance"},
        {"lblCreateDate", "Create Dt."},
        {"lblMaturityDate", "Maturity Dt."},
        {"lblRateOfInterest", "Interest"},
        {"lblAvailabaleBalance", "Available Balance"},
        {"lblSettlementMode", "Settlement"},
        {"lblNominee", "Nominee"}
        
    };
    
}

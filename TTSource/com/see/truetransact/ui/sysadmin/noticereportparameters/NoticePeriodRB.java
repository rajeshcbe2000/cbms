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

package com.see.truetransact.ui.sysadmin.noticereportparameters;

import java.util.ListResourceBundle;

public class NoticePeriodRB extends ListResourceBundle {
    public NoticePeriodRB(){
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
        {"lblCustomerName", "ReportName"},
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
        {"reportedMsg", "Reported On Date should be greater than the DateofDeath"},
        {"currentDateMsg", "Reported On Date selected should be greater than the Current Date "},
        {"createdDateMsg", "Date of Death selected should be greater than the Selected Customer Created Date "},
        {"btnPrint", ""},
        {"lblCustomerId", "Customer Id"},
        {"lblReferenceNo", "Reference No."},
        {"lblRemarks", "Remarks"},
        {"lblProductType", "Product Type"},
        {"lblAccountNo", "Account No."},
        {"lblCreateDate", "Create Dt."},
        {"lblMaturityDate", "Maturity Dt."},
        {"lblRateOfInterest", "Interest"},
        {"lblAvailabaleBalance", "Available Balance"},
        {"lblSettlementMode", "Settlement"},
        {"lblNominee", "Nominee"},
        {"lblReportID", "Report ID"},
        {"confirmMsg", "Are you sure you want to Deathmark the selected customer?"},
        
    };
    
}

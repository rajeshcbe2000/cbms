/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductMRB.java
 *
 * Created on Thu Jun 24 18:03:43 GMT+05:30 2004
 */

package com.see.truetransact.ui.mdsapplication;

import java.util.ListResourceBundle;

public class MDSApplicationMRB extends ListResourceBundle {
    public MDSApplicationMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtDivisionNo", "Division No should not be empty!!!"},
      //{"txtMembershipNo", "MembershipNo should not be empty!!!"},
        {"txtSchemeName", "MDS Scheme Name should not be empty!!!"},
        {"txtChittalNo", "ChittalNo should not be empty!!!"},
        {"tdtChitStartDt", "ChitStartDt should not be empty!!!"},
        {"tdtChitEndDt", "ChitEndDt should not be empty!!!"},
        {"txtInstAmt", "InstAmt should not be empty!!!"},
        {"txtSubNo", "Sub No should not be empty!!!"},
        {"txtApplnNo", "Application No should not be empty!!!"},
        {"tdtApplnDate", "Application Date should not be empty!!!"},
      //{"txtMembershipName", "Member Name should not be empty!!!"},
        {"txtHouseStNo", "House/ Street No should not be empty!!!"},
        {"txtpin", "Pin should not be empty!!!"},
        {"txtArea", "Area should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"cboState", "State should be a proper value!!!"},
        {"cboCity", "City should be a proper value!!!"},
    };
}

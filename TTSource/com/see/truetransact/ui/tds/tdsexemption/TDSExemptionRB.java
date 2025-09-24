/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSExemptionRB.java
 *
 * Created on Tue Feb 01 17:29:23 IST 2005
 */

package com.see.truetransact.ui.tds.tdsexemption;

import java.util.ListResourceBundle;

public class TDSExemptionRB extends ListResourceBundle {
    public TDSExemptionRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnAuthorize", ""},
        {"lblSubmitDate", "Submit Date"},
        {"lblMsg", ""},
        {"btnException", ""},
        {"btnNew", ""},
        {"lblEndDate", "Period To"},
        {"lblRefNo", " Certificate Reference No."},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblCustomerId", "Customer Id"},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"btnCustomerId", ""},
        {"btnPrint", ""},
        {"lblStartDate", "Period From"},
        {"lblExemptId", "Exemption Id"},
        {"lblCustomerName", "Customer Name"},
        {"lblPanNo", "PAN Number"},
        {"lblRemarks", "Remarks"},
        {"lblCustomerNameValue", ""},
    };
    
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSChangeofMemberRB.java
 * 
 * Created on Sun May 29 12:03:57 IST 2011
 */

package com.see.truetransact.ui.gdsapplication.gdschangeofmember;

import com.see.truetransact.ui.mdsapplication.mdschangeofmember.*;
import java.util.ListResourceBundle;

public class GDSChangeofMemberRB extends ListResourceBundle {
    public GDSChangeofMemberRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblEffetiveDt", "Change Effective Date"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"lblMunnal", "Whether New Member a Munnal"},
        {"btnCopy", ""},
        {"btnSave", ""},
        {"lblExistingMemberName", "Existing Member Name"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"rdoManualAllowed_no", "No"},
        {"btnView", ""},
        {"lblExistingMemName", "                                          "},
        {"btnExistingNo", ""},
        {"lblSchemeName", "MDS Scheme Name"},
        {"lblRemarks", "Remarks"},
        {"lblNewMemberNo", "New Member No"},
        {"lblSpace5", "     "},
        {"rdoManualAllowed_yes", "Yes"},
        {"btnDelete", ""},
//        {"lblChitNo", "Chit no"},
        {"btnSchemeNameOpen", ""},
        {"lblNewMemberName", "New Member Name"},
        {"lblTotalAmount", "Total Amount Paid Till Date"},
        {"lblSpace", " Status :"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblDivisionNo", "Division No"},
        {"btnNew", ""},
        {"lblInstallmentNo", "Installment No"},
        {"btnCancel", ""},
        {"lblSpace6", "     "},
        {"lblExistingNo", "Chittal No"},
        {"btnNewMemberNo", ""},
        {"btnPrint", ""} 

   };

}

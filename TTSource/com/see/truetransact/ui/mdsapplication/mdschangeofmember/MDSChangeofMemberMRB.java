/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSChangeofMemberMRB.java
 * 
 * Created on Tue May 31 15:18:42 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdschangeofmember;

import java.util.ListResourceBundle;

public class MDSChangeofMemberMRB extends ListResourceBundle {
    public MDSChangeofMemberMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtSchemeName", "MDS SchemeName should not be empty!!!"},
        {"txtChittalNo", "Chittal No should not be empty!!!"},
        {"btnSchemeNameOpen", "SchemeName should not be empty!!!"},
        {"txtDivisionNo", "DivisionNo should not be empty!!!"},
        {"txtInstallmentNo", "InstallmentNo should not be empty!!!"},
        {"txtTotalAmount", "TotalAmount should not be empty!!!"},
        {"rdoManualAllowed_yes", "Munnal should be selected!!!"},
        {"rdoManualAllowed_no", "Munnal should be selected!!!"},
        {"btnNewMemberNo", "New Member No should not be empty!!!"},
        {"tdtEffetiveDt", "Change Effective Date should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"}
   };

}

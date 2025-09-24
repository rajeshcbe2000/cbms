/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GoldConfigurationMRB.java
 * 
 * Created on Wed Feb 02 12:50:41 IST 2005
 */

package com.see.truetransact.ui.termloan.goldLoanConfiguration;

import java.util.ListResourceBundle;

public class GoldConfigurationRB extends ListResourceBundle {
    public GoldConfigurationRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblName", "Name"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblStatus1", "                      "},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblNameValue", ""},
        {"btnPrint", ""},
        {"lblDate", "From Date"},
        {"lblPurityOfGold","Purity of Gold"},
        {"lblPerGramRate","Per Gram Rate"},
        {"tblColumn1", "Set No"},
        {"tblColumn2", "From Date"},
        {"tblColumn3", "To Date"},
        {"tblColumn4", "Purity of Gold"},
        {"tblColumn5", "PerGram Rate"},
        {"tblColumn6", "Authorize Status"}
        
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSConfigRB.java
 * 
 * Created on Thu Feb 10 12:53:41 IST 2005
 */

package com.see.truetransact.ui.tds.tdsconfig;

import java.util.ListResourceBundle;

public class TDSConfigRB extends ListResourceBundle {
    public TDSConfigRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoCutOff_No", "No"},
        {"btnClose", ""},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblCutOff", "Include Cut off"},

        {"lblCutOff", "Include Cut Off Amt"},
        {"lblTdsId", "TDS Id"},
        {"lblEndDate", "Financial Year End Date"},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lbSpace2", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblStartDate", "Financial Year Start Date"},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"lblCutOfAmount", "Cut off Amount"},
        {"lblPercentage", "Percentage"},
        {"lblScope", "Scope"},
        {"btnCancel", ""},
        {"rdoCutOff_Yes", "Yes"},
        {"lblTdsCreditAchdId", "Tds Cr A/c Head Id"},
        {"btnPrint", ""} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * stateTalukRB.java
 * 
 * Created on 19-05-2009 
 */

package com.see.truetransact.ui.sysadmin.stateTalukMaster;

import java.util.ListResourceBundle;

public class StateTalukRB extends ListResourceBundle {
    public StateTalukRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
       
        {"lblStateCode", "State Code"},
        {"lblStateName", "State Name"},
        {"lblTalukName", "Taluk Name"},
        {"lblTalukCode","Taluk Code"},
        {"lblDistrictName","District Name"},
        {"lblDistrictCode","District Code"},
        {"btnDisDelete", "Delete"},
        {"btnDisNew", "New"},
        {"btnDisSave", "Save"},
        {"btnDistrictSave", "Save"},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"btnClose", ""},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"btnEdit", ""},
        {"btnCancel", ""},
        {"btnNew",""},
        {"tblColumn1", "Sl No"},
        {"tblColumn2", "District Code"},
        {"tblColumn3", "District Name"},
        {"tblColumn4", "Taluk Code"},
        {"tblColumn5", "Taluk Name"},
        {"tblColumn6", "SL.No"},
        {"tblColumn7", "State Code"},
        {"tblColumn8", "District Code"},
        {"tblColumn9", "District Name"},
        {"tblColumn10", "Status"},
        {"tblColumn11", "Status"},
        {"tblColumn12", "AuthStatus"},
        {"tblColumn13", "Verified"},
        {"tblColumn14", "AuthStatus"},
        {"tblColumn15", "Verified"},
        {"btnClear", "New"},
        {"StateCodeCount", "This State Code Already Exist !!!"}

   };

}

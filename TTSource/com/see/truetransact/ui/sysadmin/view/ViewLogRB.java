/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ViewLogRB.java
 * 
 * Created on Mon Apr 19 11:12:57 GMT+05:30 2004
 */

package com.see.truetransact.ui.sysadmin.view;

import java.util.ListResourceBundle;

public class ViewLogRB extends ListResourceBundle {
    public ViewLogRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        
        {"lblFromDate", "From Date"},
        {"rdoAll", "All"},
        {"lblLatestEntries", "No. of Latest Entries"},
        {"lblColorAll", ""},
        
        {"lblToDate", "To Date"},
        {"lblFindbyHistory", "Find by History"},
        {"rdoNew", "New"},
        
        
        {"lblColorNew", ""},
        {"lblColorDelete", ""},
        {"rdoDelete", "Delete"},
        {"lblColorEdit", ""},
        {"btnView", "View"},
        {"rdoEdit", "Edit"},
        {"panMain", ""},
        {"lblMsg", ""},
        {"lblSpace3", " Status :"},
        {"lblStatus", "                      "},
        {"lblUserID", "Logged on User ID"},
        {"btnClear","Clear Fields"},
        
        {"btnOk","OK"},
        {"lblDate", "Date"},
        {"lblModule", "Module"},
        {"lblScreen", "Screen"},
        {"lblIPAddress", "IP Address"},
        
        {"lblUserid", "User ID"},
        {"lblActivity", "Activity"},
        {"lblBranchID", "Branch ID"},
        {"lblPrimaryKey", "Primary Key"},
        {"lblData", "Data"},
        {"lblRemarks", "Remarks"},
        {"lblMod", ""},
        
        
        {"lblBrnID", ""},        
        {"lblUsrID", ""},
        {"lblAct", ""},
        {"lblPrimKey", ""},
        {"lblDt", ""},
        {"lblMod", ""},
        {"lblScr", ""},
        {"lblIPAddr", ""},
        
        {"mnuTableOutput", "Table Output"},
        {"mitMultipleIP", "Multiple IP Address"},
        {"mitMultipleUsers", "Multiple Users"},
        
        
        
        {"cDialogOk", "Ok"},
        {"dateWarning","From date greater than To date !!! "},
        {"existanceWarning","No relevant data !!! "}
   };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AccountCreationRB.java
 * 
 * Created on Wed May 19 11:01:01 GMT+05:30 2004
 */

package com.see.truetransact.ui.generalledger;

import java.util.ListResourceBundle;

public class AccountCreationRB extends ListResourceBundle {
    public AccountCreationRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"lblAccountType", "Account Type"},
        {"btnClose", ""},
        {"lblAccountHeadCode", "Account Head Code"},
        {"lblAccountHead", "Account Head"},
        {"lblAccountHeadDesc", "Description"},
        {"lblReceiveDayBookDetail", "Receipt Detail in Day Book"},
        {"lblPayDayBookDetail", "Payment Detail in Day Book"},
        {"lblAccountHeadOrder", "Account Head Order"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnAuthorize", ""},
        {"lblSubHead", "Sub Head"},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblMajorHead", "Major Head"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        {"lblSpace5", "     "},
        
        /** UNGENERATED CODE  ***/
        
        {"TOCommandError", "TO Status Command is null"},
        {"treeHeading","Account Head"},
        {"acHdExistance","Active Account(s) exist for this Account Head!!!"},
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitRB.java
 * 
 * Created on Wed Jun 08 10:50:57 GMT+05:30 2005
 */

package com.see.truetransact.ui.generalledger.gllimit;

import java.util.ListResourceBundle;

public class GLLimitRB extends ListResourceBundle {
    public GLLimitRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"lblGLGroup", "GL Group"},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblAccountHead", "Account Head"},
        {"lblSpace", " Status :"},
        {"btnReject", ""},
        {"btnAdd", "Save"},
        {"btnEdit", ""},
        {"lblLimit", "Amount Limit"},
        {"lblAnnualLimit", "Annual Limit"},
        {"lblOverDraw", "OverDraw Percentage"},
        {"lblInterBranchTransAllowed", "Inter Branch Transaction Allowed"},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblSpace6", "     "},
        {"lblStatus", "                      "},
        {"btnstp", ""},
        {"btnPrint", ""},
        {"tblColumn0", "SL.No"},
        {"tblColumn1", "Act. Head"},
        {"tblColumn2", "Amt.Limit"},
        {"tblColumn3", "Ann.Limit"},
        {"tblColumn4", "Over Draw %" },
        {"tblColumn5", "From Period" },
        {"tblColumn6", "To Period"},
        {"tblColumn7", "Status"},
        {"tblColumn8", "Branch Trans."},

   

   };

}

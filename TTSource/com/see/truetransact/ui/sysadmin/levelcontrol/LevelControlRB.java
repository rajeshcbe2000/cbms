/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LevelControlRB.java
 */
package com.see.truetransact.ui.sysadmin.levelcontrol;
import java.util.ListResourceBundle;
public class LevelControlRB extends ListResourceBundle {
    public LevelControlRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblName", "Name"},
        {"lblCredit", "Credit"},
        {"btnEdit", ""},
        {"lblMsg", ""},
        {"lblDebit", "Debit"},
        {"btnNew", ""},
        {"panData", "Maximum Limit"},
        {"lblSpace2", "     "},
        {"lblTransfer", "Transfer"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblLevelID", "Level ID"},
        {"lblDescription", "Description"},
        {"btnPrint", ""},
        {"lblCash", "Cash"},
        {"lblClearing", "Clearing"},
        {"lblSingleWindow", "Single Window"},
        {"cDialogOK", "OK"},
        {"dataExistsMsg", "Level ID for Amount(s) entered, Already Exists"}

   };

}

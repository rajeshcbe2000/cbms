/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigRB.java
 * 
 * Created on Thu Jan 20 15:41:51 IST 2005
 */

package com.see.truetransact.ui.locker.lockersurrender;

import java.util.ListResourceBundle;

public class LockerSurrenderRB extends ListResourceBundle {
    public LockerSurrenderRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblNoOfTokens", "No. Of Tokens Configured"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblSeriesNo", "Series No."},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblEndingTokenNo", "Ending Token No."},
        {"btnNew", ""},
        {"lblStartingTokenNo", "Starting Token No."},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"lblTokenType", "Token Type"},
        {"btnPrint", ""} ,
        {"lblTokenConfigId", "Token Config Id"},
        {"cDialogOK", "OK"},
        {"existingMsg", "TokenType with this Series Already Exists"},
        {"tokenTypeExistingMsg", "TokenType Selected Already Exists"},
        {"tblHeading1", "custid"},
        {"tblHeading2", "name"},
        {"tblHeading3", "password"},
        {"tokenNoMsg", "Token Number Entered should be greater than Zero"},
        {"LOCNOWARNING", "Locker No not selected"},
        {"TRANSAMTWARNING", "Total Amount and Transaction Amount not tallied..."},
        {"NOTRANSWARNING", "There is no transaction details"},
        {"RADIOBITTONSWARNING", "select Renew or Surrender or BreakOpen or RentCollection"},

   };

}

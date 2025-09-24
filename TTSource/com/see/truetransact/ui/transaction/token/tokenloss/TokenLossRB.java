/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenLossRB.java
 *
 * Created on Tue Jan 25 16:46:32 IST 2005
 */

package com.see.truetransact.ui.transaction.token.tokenloss;

import java.util.ListResourceBundle;

public class TokenLossRB extends ListResourceBundle {
    public TokenLossRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblTokenRecovered", "Token Recovered"},
        {"btnClose", ""},
        {"lblDateOfLoss", "Date of Loss"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblSeriesNo", "Series No."},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblRecoveredDate", "Recovered Date"},
        {"btnNew", ""},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblTokenType", "Token Type"},
        {"chkTokenRecovered", ""},
        {"lblStatus", "                      "},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        {"lblRemarks", "Remarks"},
        {"lblTokenLossId", "Token Loss Id"},
        {"lblTokenNo", "Token No."},
        {"existingMsg", "Token Number Entered Already Exists"},
        {"issuedMsg", "Token Number Entered is not Issued"},
        {"WARNING", "Date Cannot Be A Post Date"},
        {"cDialogOK", "OK"},
    };
    
}

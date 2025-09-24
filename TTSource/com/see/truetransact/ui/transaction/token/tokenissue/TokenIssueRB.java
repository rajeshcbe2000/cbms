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

package com.see.truetransact.ui.transaction.token.tokenissue;

import java.util.ListResourceBundle;

public class TokenIssueRB extends ListResourceBundle {
    public TokenIssueRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblNoOfTokens", "No. Of Tokens Issued"},
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
        {"btnUserId", ""},
        {"lblTokenIssueId", "Token Issue Id"},
        {"cDialogOK", "OK"},
        {"nonExistingMsg", "TokenType with this Series is Not Available"},
        {"lblReceiverId", "Receiver's Id"},
        {"existingMsg", "TokenType with this Series is Already Issued"},
        {"lblTokenNo", "Token No."},
        {"lblTokenStatus", "Token Status"},
        {"lblLostDate", "Lost Date"},
        {"tokenNoMsg", "Token Number Entered should be greater than Zero"}
        
    };
    
}

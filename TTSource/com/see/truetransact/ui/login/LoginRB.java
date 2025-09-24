/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.login;
import java.util.ListResourceBundle;
public class LoginRB extends ListResourceBundle {
    public LoginRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblUserID", "User ID"},
        {"lblPassword", "Password"},
        {"lblDate", "Date"},
        {"lblBranch", "Bank & Branch"},
        {"btnLogon", "Log on"},
        {"btnCancel", "Cancel"}
   };

}

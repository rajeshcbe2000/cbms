/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChangePasswordRB.java
 *
 * Created on Thu Sep 30 16:53:01 GMT+05:30 2004
 */

package com.see.truetransact.ui.login.newpasswd;

import java.util.ListResourceBundle;

public class ChangePasswordRB extends ListResourceBundle {
    public ChangePasswordRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnOk", "Ok"},
        {"lblUserName", ""},
        {"btnCancel", "Cancel"},
        {"lblUsrNm", "User Name"},
        {"lblNewPasswd", "New Password"},
        {"lblConfirmPasswd", "Confirmation Password"},
        {"lblOldPasswd", "Old Password"},
        {"panMain", ""},
        
        {"cDialogOk", "Ok"},
        {"passwordUpdated", "Your password is changed Sucessfully!!!"},
        {"pwdHistoryMissmatch", "Password history error... Please enter a new password !!!"},
        
        {"passwordError", "There was an Error in the database... Please try later  !!!"},
        {"oldPasswordMissmatch", "Old Password missmatch... Please enter the correct Password!!!"},
        {"newPasswordMissmatch", "New password missmatch... Please confirm the Password!!!"}        
    };
}
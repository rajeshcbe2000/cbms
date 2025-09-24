/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChangePasswordMRB.java
 *
 * Created on Thu Sep 30 17:00:13 GMT+05:30 2004
 */

package com.see.truetransact.ui.login.newpasswd;

import java.util.ListResourceBundle;

public class ChangePasswordMRB extends ListResourceBundle {
    public ChangePasswordMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"pwdNewPasswd", "NewPasswd should not be empty!!!"},
        {"pwdOldPasswd", "OldPasswd should not be empty!!!"},
        {"pwdConfirmPasswd", "ConfirmPasswd should not be empty!!!"}        
    };
    
}

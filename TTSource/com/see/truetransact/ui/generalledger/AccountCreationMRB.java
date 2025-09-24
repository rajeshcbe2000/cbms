/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountCreationMRB.java
 */

package com.see.truetransact.ui.generalledger;
import java.util.ListResourceBundle;
public class AccountCreationMRB extends ListResourceBundle {
    public AccountCreationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboSubHead", "SubHead should be a proper value!!!"},
        {"txtCallingCode", "CallingCode should not be empty!!!"},
        {"cboMajorHead", "MajorHead should be a proper value!!!"},
        {"txtAccountHeadDesc", "AccountHeadDesc should not be empty!!!"},
        {"txtAccountHead", "AccountHead should not be empty!!!"},
        {"txtAccountHeadOrder", "AccountHeadOrder should not be empty!!!"},
        {"cboAccountType", "AccountType should be a proper value!!!"} 

   };

}

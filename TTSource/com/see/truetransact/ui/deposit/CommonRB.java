/*
 * CommonRB.java
 *
 * Created on December 27, 2004, 4:53 PM
 */

package com.see.truetransact.ui.deposit;

/**
 *
 * @author  K.R.Jayakrishnan
 */
/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermDepositRB.java
 *
 * Created on Wed Aug 18 15:18:50 GMT+05:30 2004
 */


import java.util.ListResourceBundle;

public class CommonRB extends ListResourceBundle {
    public CommonRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"dialogYes", "Yes"},
        {"dialogNo", "No"},
        {"dialogCancel", "Cancel"},
        {"dialogOk", "OK"},
        {"warningMessage", "This Record Already exist. Do you want to replace it?"},
        {"warningMessageOK", "This Record Already exists"},
        {"dialogForJointAccntHolder", "This will reset the Joint Account Holders data. Are you sure to reset it?"},
        {"selectMainAccntHolder", "Select Main Account Holder before selecting Joint Account Holders"},
        {"tblJntAccntColumn1", "Name"},
        {"tblJntAccntColumn2", "Cust.Id"},
        {"tblJntAccntColumn3", "Type"},
        {"tblJntAccntColumn4", "Main / Joint"}
    };
    
}

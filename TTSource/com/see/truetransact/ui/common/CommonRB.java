/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AccountCreationRB.java
 * 
 * Created on Wed May 19 11:01:01 GMT+05:30 2004
 */

package com.see.truetransact.ui.common;

import java.util.ListResourceBundle;

public class CommonRB extends ListResourceBundle {
    public CommonRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cDialogYes","Yes"},  
        {"cDialogNo","No"},
        {"cDialogOK","OK"},
        {"deleteWarning","Are you sure you want to Delete?"},
        {"closeWarning","Are you sure you want to close the window?"},
        {"YES","Y"},
        {"NO","N"},
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CreateScreensMRB.java
 */

package com.see.truetransact.ui.sysadmin.createscreens;
import java.util.ListResourceBundle;
public class CreateScreensMRB extends ListResourceBundle {
    public CreateScreensMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboScreenName", "Screen Name should be a proper value!!!"},
        {"txt", "Text value should not be empty!!!"}
   };

}


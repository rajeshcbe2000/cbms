/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceMRB.java
 *
 * Created on January 7, 2005, 3:03 PM
 */

package com.see.truetransact.ui.sysadmin.view;

import java.util.ListResourceBundle;
/**
 *
 * @author  152713
 */
public class ViewLogImportanceMRB extends ListResourceBundle {
    
    /** Creates a new instance of ViewLogImportanceMRB */
    public ViewLogImportanceMRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboActivity", "Activity should be a proper value!!!"},
        {"cboScreen", "Screen should be a proper value!!!"},
        {"cboImportance", "Importance should not be empty!!!"},
        {"cboModule", "Module should be a proper value!!!"}

   };
}

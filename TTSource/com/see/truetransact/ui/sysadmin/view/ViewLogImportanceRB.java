/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceRB.java
 *
 * Created on January 7, 2005, 3:02 PM
 */

package com.see.truetransact.ui.sysadmin.view;

import java.util.ListResourceBundle;
/**
 *
 * @author  152713
 */
public class ViewLogImportanceRB extends ListResourceBundle {
    
    /** Creates a new instance of ViewLogImportanceRB */
    public ViewLogImportanceRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        
        {"lblSpace1", "Status"},
        {"lblStatus", "      "},
        {"lblMsg", "                      "},
        {"lblModule", "Module"},
        {"lblScreen", "Screen"},
        {"lblActivity", "Activity"},
        
        {"lblImportance", "Importance"},
        {"btnNew_Importance","New"},
        {"btnSave_Importance","Save"},
        {"btnDelete_Importance","Delete"},
        
        {"tblColumn1", "IMP No."},
        
        {"cDialogOk", "Ok"}
   };
}

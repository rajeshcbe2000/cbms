/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * HeadRB.java
 */

package com.see.truetransact.ui.generalledger;
import java.util.ListResourceBundle;
public class HeadRB extends ListResourceBundle {
    public HeadRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnSubHeadNew", "New"},
        {"lblAccountType", "Account Type"},
        {"btnClose", ""},
        {"btnSubHeadSave", "Save"},
        {"btnEdit", ""},
        {"lblMajorHeadCode", "Major Head Code"},
        {"lblSubHeadCode", "Sub Head Code"},
        {"panMajorHead", "Major Head"},
        {"lblMsg", ""},
        {"lblMajorHeadDesc", "Major Head Description"},
        {"lblGLHeadConsolidated", "GL Heads to be Consolidated"},
        {"btnNew", ""},
        {"panSubHead", "Sub Head"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnSubHeadDelete", "Delete"},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblSubHeadDesc", "Sub Head Description"},
        {"btnPrint", ""},
        
        /**UNGENERATED CODE**/
        {"tblColumn1","Sub Head Code"},
        {"tblColumn2","Sub Head Description"},
        
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},   
        {"cDialogOK","OK"},  
        {"cDialogCancel","Cancel"},
        {"deleteWarning","Are you sure you want to Delete?"},
        
        {"existanceWarning", "The Description for this SubHead Already exist. Do you want to modify the description?"},
        {"mjrHdExistance", "The Major Head already exists"},
        {"mjrHdZero", "Major Head Code should not be Zero!!!"},
        {"subHdZero", "Sub Head Code should not be Zero!!!"},
        {"acHdExistance","Sub Head can't be deleted as it has active account head(s)"},
        {"subAcHdExistance","Major Head can't be deleted as it has active Sub Account head(s)"},
   };

}

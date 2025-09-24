/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.inwardregister;
import java.util.ListResourceBundle;
public class InwardRB extends ListResourceBundle {
    public InwardRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblInward", "Inward Date"},
        {"lblInwardNo", "Inward No"},
        {"lblReferenceNo", "Reference No"},
        {"lblSubmittedBy", "Submitted By"},
        {"lblActionTaken", "Action Taken"},
        {"lblDetails", "Details"},
        {"lblRemarks", "Remarks"}
    };
}

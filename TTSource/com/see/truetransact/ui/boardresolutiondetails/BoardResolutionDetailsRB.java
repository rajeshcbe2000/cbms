/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.boardresolutiondetails;
import java.util.ListResourceBundle;
public class BoardResolutionDetailsRB extends ListResourceBundle {
    public BoardResolutionDetailsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblOutwardDate", "Outward Date"},
//        {"lblOutwardNo", "Outward Number"},
        {"lblReferenceNo", "Reference Number"},
        {"lblRemarks", "Remarks"}
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * AddressPanelRB.java
 *
 * Created on December 18, 2003, 1:12 PM
 */

package com.see.truetransact.ui.common.viewsearch;

/**
 *
 * @author  Bala
 */
public class ViewSearchRB extends java.util.ListResourceBundle {

    /** Creates a new instance of ViewAllRB */
    public ViewSearchRB() {
    }

    protected Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSearch","Search"},
        {"btnSearch","Find"},
        {"chkCase","Match Case"},
        {"btnOk","OK"},
        {"SelectRow","Please Select a Row"},
        {"SelectRowHeading","Info"},
        {"btnCancel","Cancel"}
    };
}

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

package com.see.truetransact.ui.common.viewall;

/**
 *
 * @author  Bala
 */
public class ViewAllRB extends java.util.ListResourceBundle {

    /** Creates a new instance of ViewAllRB */
    public ViewAllRB() {
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

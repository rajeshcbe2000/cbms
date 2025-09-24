/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 **
 *
 * ResolutionStatusRB.java
 *
 * Created on December 18, 2003, 1:12 PM
 */

package com.see.truetransact.ui.common.resolutionstatus;

/**
 *
 * @author  Bala
 */
public class ResolutionStatusRB extends java.util.ListResourceBundle {

    /** Creates a new instance of ViewAllRB */
    public ResolutionStatusRB() {
    }

    protected Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSearch","Search"},
        {"btnSearch","Find"},
        {"chkCase","Match Case"},
        {"btnResolution","Send to Resolution"},
        {"SelectRow","Please Select a Row"},
        {"SelectRowHeading","Info"},
        {"btnCancel","Close"}
    };
}

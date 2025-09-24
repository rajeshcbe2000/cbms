/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * AuthorizeRB.java
 *
 * Created on December 18, 2003, 1:12 PM
 */

package com.see.truetransact.ui.termloan.loansubsidy;

/**
 *
 * @author  Bala
 */
public class LoanSubsidyRB extends java.util.ListResourceBundle {

    /** Creates a new instance of ViewAllRB */
    public LoanSubsidyRB() {
    }

    protected Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSearch","Search"},
        {"btnSearch","Find"},
        {"chkCase","Match Case"},
        {"btnAuthorize","Authorize"},
        {"SelectRow","Please Select a Row"},
        {"SelectRowHeading","Info"},
        {"btnCancel","Close"},
        {"ACCOUNTWARNING","To Account NO should be Greater than From Account Number"}
        
    };
}

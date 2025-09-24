/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * TransferRB.java
 *
 * Created on August 7, 2003, 3:57 PM
 */

package com.see.truetransact.ui.transaction.transfer;

import java.util.ListResourceBundle;

/**
 *
 * @author  Pranav
 */
public class ActionPopupRB extends ListResourceBundle {
    
    /** Creates a new instance of TransferRB */
    public ActionPopupRB() {
    }
    
    protected Object[][] getContents() {
        
        return contents;
    }
    
    public String[] getMasterHeader() {
        return new String[]{"Select", "Batch ID", "Instrument Count (Cr)", "Amount (Cr)",
                            "Instrument Count (Cr)", "Amount (Cr)",
                            "Batch Status"};
    }
    
    public String[] getDetailHeader() {
        return new String[]{"Select", "Transaction ID", "Account Head", "Account No.",
                            "Account Holder Name", "Type",
                            "Amount", "User ID"};
    }

    static final String[][] contents = {
        {"panMaster","Master View"},
        {"chkMasterSelect","Select All"},
        {"panDetail","Detailed View"},
        {"chkDetailSelect","Select All"},
        {"btnOK","OK"},
        {"btnClose","Close"}
    };
}

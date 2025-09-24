/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 */
package com.see.truetransact.ui.sysadmin.terminal;
import java.util.ListResourceBundle;
public class TerminalMRB extends ListResourceBundle {
    public TerminalMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtTerminalId", "TerminalId should not be empty!!!"},
//        {"txtBranchCode", "BranchCode should not be empty!!!"},
        {"txtIPAddress", "IPAddress should not be empty!!!"},
        {"txtTerminalDescription", "TerminalDescription should not be empty!!!"},
        {"txtMachineName", "MachineName should not be empty!!!"},
        {"txtTerminalName", "TerminalName should not be empty!!!"},
         {"cboBranchCode", "BranchCode should be a proper value!!!"},
        
    };
    
}

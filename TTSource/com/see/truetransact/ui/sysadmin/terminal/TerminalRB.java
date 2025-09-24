/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 */
package com.see.truetransact.ui.sysadmin.terminal;
import java.util.ListResourceBundle;
public class TerminalRB extends ListResourceBundle {
    public TerminalRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnTerminalMasterTabDelete", "Delete"},
        {"btnTerminalMasterTabSave", "Save"},
        {"lblIpAddress", "IP Address"},
        {"btnClose", ""},
        {"btnEdit", ""},
        {"lblTerminalName", "Terminal Name"},
        {"lblBranchCode", "Branch Code"},
        {"lblTerminalId", "Terminal Id"},
        {"lblMsg", ""},
        {"btnTerminalMasterTabNew", "New"},
        {"lblTerminalDescription", "Terminal Description"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblMachineName", "Machine Name"},
        {"btnPrint", ""},
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        
        {"TOCommandError", ""},
        
        {"existanceWarning", "This Record Already Exists. Do you want to modify it ?"},
        
        {"DataEntryMsg", "Insert New Data in the Table"},
        
    };
    
}

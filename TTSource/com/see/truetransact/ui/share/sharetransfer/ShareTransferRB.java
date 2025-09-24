/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareTransferRB.java
 * 
 * Created on Thu Feb 03 15:30:39 IST 2005
 */

package com.see.truetransact.ui.share.sharetransfer;

import java.util.ListResourceBundle;

public class ShareTransferRB extends ListResourceBundle {
    public ShareTransferRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"btnAcctTo", ""},
        {"lblAcctTo", "Transfer To A/C No"},
        {"lblStatus1", "                      "},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblSpace4", "     "},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblRemarks", "Remarks"},
        {"lblShareTo", "Share No To"},
        {"btnDelete", ""},
        {"btnAcctFrom", ""},
        {"lblShareFrom", "Share No From"},
        {"lblTransNoValue", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnNew", ""},
        {"lblTransNo", "Transfer Id"},
        {"btnCancel", ""},
        {"lblAcctFrom", "Transfer From A/C No"},
        {"lblNameFrom", ""} ,
        {"lblNameTo", ""} ,
        {"btnPrint", ""} ,
        
        {"tblColumn1", "Serial No"},
        {"tblColumn2", "Share No From"},
        {"tblColumn3", "Share No To"},
        {"tblColumn4", "No Of Shares"},
        
        {"SHAREWARNING", "ShareNo To Should be greater than ShareNo From"} ,
        {"SHARENOWARNING", "The Specified range of Shares is Invalid, Please enter Again"} 

   };

}

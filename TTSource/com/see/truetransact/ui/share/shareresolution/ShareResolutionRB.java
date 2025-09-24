/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareResolutionRB.java
 * 
 * Created on Thu Apr 28 11:38:54 IST 2005
 */

package com.see.truetransact.ui.share.shareresolution;

import java.util.ListResourceBundle;

public class ShareResolutionRB extends ListResourceBundle {
    public ShareResolutionRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"lblResolutionNo", "Resolution No."},
        {"btnClose", ""},
        {"btnAccept", "Accept"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"btnAuthorize", ""},
        {"btnDeffered", "Deffered"},
        {"lblMsg", ""},
        {"btnNew", ""},
        {"btnClose1", "Close"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"btnException", ""},
        {"lblSpace3", "     "},
        {"chkSelectAll", "Select All"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"btnReject1", "Reject"},
        {"btnPrint", ""},
        {"lblResolutionDt", "Resolution Date"},
        {"txtResolutionNo", "Resolution No."},
        {"tdtResolutionDt", "Resolution Date"}

   };

}

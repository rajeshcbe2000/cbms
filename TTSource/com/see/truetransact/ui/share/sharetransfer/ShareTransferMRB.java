/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareTransferMRB.java
 * 
 * Created on Thu Feb 03 15:34:47 IST 2005
 */

package com.see.truetransact.ui.share.sharetransfer;

import java.util.ListResourceBundle;

public class ShareTransferMRB extends ListResourceBundle {
    public ShareTransferMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAcctFrom", "AcctFrom should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtShareTo", "ShareTo should not be empty!!!"},
        {"txtShareFrom", "ShareFrom should not be empty!!!"},
        {"txtAcctTo", "AcctTo should not be empty!!!"} 

   };

}

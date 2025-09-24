/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLEntryMRB.java
 * 
 * Created on Tue Jan 04 11:01:37 IST 2005
 */

package com.see.truetransact.ui.generalledger.glentry;

import java.util.ListResourceBundle;

public class GLEntryMRB extends ListResourceBundle {
    public GLEntryMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAmount", "Amount should not be empty!!!"},
        {"txtAcHead", "AcHead should not be empty!!!"}, 
        {"cboAccountHeadStatus", "Select a AccountHead Status"}

   };

}

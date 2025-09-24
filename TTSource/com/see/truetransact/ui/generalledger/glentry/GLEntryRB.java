/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLEntryRB.java
 *
 * Created on Tue Jan 04 10:18:42 IST 2005
 */

package com.see.truetransact.ui.generalledger.glentry;

import java.util.ListResourceBundle;

public class GLEntryRB extends ListResourceBundle {
    public GLEntryRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnAcHead", ""},
        {"lblAcDesc", "Account Head Description"},
        {"btnSave", ""},
        {"lblDescription", ""},
        {"lblAcHead", "Account Head"},
        {"lblAmount", "Amount"},
        {"lblGLBalance", "GL Balance"},
        {"lblGLBalanceValue", ""},
        {"lblAccountHeadStatus", "Account Head Status"},
        
    };
    
}

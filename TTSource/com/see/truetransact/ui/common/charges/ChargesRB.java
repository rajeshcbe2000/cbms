/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ChargesRB.java
 * 
 * Created on Thu Dec 23 12:29:27 IST 2004
 */

package com.see.truetransact.ui.common.charges;

import java.util.ListResourceBundle;

public class ChargesRB extends ListResourceBundle {
    public ChargesRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblFixedRate", "Fixed Rate"},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"btnNew", ""},
        {"lblForEvery", "For every"},
        {"lblChargeType", "Types of Charges"},
        {"lblPercentage", "Percentage"},
        {"lblSpace2", "     "},
        {"lblFrmAmt", "From Amount"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"btnPrint", ""},
        {"lblToAmt", "To Amount"} 

   };

}

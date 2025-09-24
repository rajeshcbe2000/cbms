/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigMRB.java
 * 
 * Created on Thu Jan 20 16:39:25 IST 2005
 */

package com.see.truetransact.ui.borrowings;

import java.util.ListResourceBundle;

public class DisbursalMRB extends ListResourceBundle {
    public DisbursalMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtBorrowingRefNo", "BorrowingRefNo should not be empty!!!"}
                 
   };

}

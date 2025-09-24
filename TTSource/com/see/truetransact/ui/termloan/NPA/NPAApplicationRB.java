/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallmentRB.java
 * 
 * Created on Tue Jan 25 15:52:43 IST 2005
 */

package com.see.truetransact.ui.termloan.NPA;

import java.util.ListResourceBundle;

public class NPAApplicationRB extends ListResourceBundle {
    public NPAApplicationRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblBranches","Branches"},
        {"lblProductID", "ProductID"},
        {"btnOk", "Ok"}
   };

}

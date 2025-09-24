/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LienMarkingMRB.java
 */

package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class LienMarkingMRB extends ListResourceBundle {
    public LienMarkingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtLienAmount", "LienAmount should not be empty!!!"},
        {"cboLienProduct", "LienProduct should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"txtLienAccountNumber", "LienAccountNumber should not be empty!!!"},
        {"tdtLienDate", "LienDate should not be empty!!!"},
        {"txtAccountNumber", "AccountNumber should not be empty!!!"} 

   };

}

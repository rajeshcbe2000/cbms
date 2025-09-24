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

public class RepaymentIntMRB extends ListResourceBundle {
    public RepaymentIntMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtBorrowingRefNo", "BorrowingRefNo should not be empty!!!"},
      {"txtPrinRepaid", "Principal repaid should not be empty!!!"},  
        {"tdtIntPaid", "Interest Paid Date should not be empty!!!"},
        {"txtPrinRepaid", "Principal Repaid should not be empty!!!"},
        {"txtIntRepaid", "Interest Repaid should not be empty!!!"},
        {"txtPenalRepaid", "Penal Repaid should not be empty!!!"},
        {"txtChargesRepaid", "Charges Repaid should not be empty!!!"},
        {"txtPrinRepaidNo", "Principal Repaid should allow only number!!!"},
        {"txtIntRepaidNo", "Interest Repaid should allow only number!!!"},
        {"txtPenalRepaidNo", "Penal Repaid should allow only number!!!"},
        {"txtChargesRepaidNo", "Charges Repaid should allow only number!!!"}
            
            
            
   };

}

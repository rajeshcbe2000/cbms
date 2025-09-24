/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class FreezeMRB extends ListResourceBundle {
    public FreezeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAmount", "Amount should not be empty!!!"},
        {"tdtDate", "FreezeDate should not be empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"rdoCreditDebit_Credit", "CreditDebit should be selected!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"cboType", "Type should be a proper value!!!"},
        {"txtAccountNumber", "AccountNumber should not be empty!!!"} 

   };

}

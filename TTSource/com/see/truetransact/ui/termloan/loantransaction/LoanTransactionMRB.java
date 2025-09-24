/*
 * Copyright 2014 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..
 * 
 */

package com.see.truetransact.ui.termloan.loantransaction;
import java.util.ListResourceBundle;

public class LoanTransactionMRB extends ListResourceBundle {
    public LoanTransactionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtFutureDate", "Date should not be empty!!!"},
        {"txtPayableAmount", "PayableAmount should not be empty!!!"},
        {"cboProductID", "ProductID should be a proper value!!!"},
        {"txtAccountNumber", "AccountNumber should not be empty!!!"},  
         {"cboProdType", "ProdType should be a proper value!!!"},
   };

}

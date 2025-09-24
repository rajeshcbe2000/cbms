/*
 * Copyright 2013 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd...
 * 
 */
package com.see.truetransact.ui.termloan.loantransaction;
import java.util.ListResourceBundle;

public class LoanTransactionRB extends ListResourceBundle {
    public LoanTransactionRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblCustomerName", "Customer Name"},
        {"lblProductID", "Product ID"},
        {"lblAccountNumber", "Account Number"},
        {"btnAccountNumber", ""},
        {"lblCustomerNameDisplay", "null"},
        {"freezeAmount", "Account cannot be closed as Freeze exists."},
        {"lienAmount", "Account cannot be closed as Lien exists."},
        {"shadowCrDr", "Shadow Credit and Debit should be zero for closing the Account."},
        {"UnclearBalance","Account Cannot Be Closed As UnclearBalance Exists"},
   };

}

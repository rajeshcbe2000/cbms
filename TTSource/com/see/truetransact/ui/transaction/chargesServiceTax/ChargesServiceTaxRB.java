/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.transaction.chargesServiceTax;
import java.util.ListResourceBundle;
public class ChargesServiceTaxRB extends ListResourceBundle {
    public ChargesServiceTaxRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblCustomerName", "Customer Name"},
        {"lblProductID", "Product ID"},
        {"lblProductType", "Product Type"},
        {"lblAccountHeadDisplay", " "},
        {"lblAccountNumber", "Account Number"},
        {"lblChargeDetails", "Particulars"},
        {"lblAccountHead", "Account Head"},
        {"lblTotalAmt", "Total Amount"},
        {"lblAccountHeadDesc", " "},
        {"lblAmount", "Amount"},
        {"lblServicTaxAmt", "Service Tax Amount"},
        {"btnAccountNumber", ""},
        {"lblCustomerNameDisplay", " "},
        {"DebitBalance","Debit Balance account can not be closed"},
        {"TOCommandError", "TO Status Command is null"},
        {"UnclearBalance","Account Cannot Be Closed As UnclearBalance Exists"}
   };
}

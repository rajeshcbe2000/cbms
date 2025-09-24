/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.ListResourceBundle;
public class AccountClosingRB extends ListResourceBundle {
    public AccountClosingRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblCustomerName", "Customer Name"},
        {"lblProductID", "Product ID"},
        {"lblAccountHeadDisplay", "null"},
        {"lblAccountNumber", "Account Number"},
        {"lblChargeDetails", "Misc Service Charges"},
        {"lblAccountHead", "Account Head"},
        {"lblPayableCharges", "Payable Balance"},
        {"lblReceivableCharges", "Receivable Balance"},
        {"lblBalanceDisplay", "null"},
        {"lblBalance", "Balance"},
        {"lblInterestPayable", "Interest Payable"},
        {"lblInterestReceivable", "Interest/charges Receivable"},
        {"lblAccountHeadDescription", "null"},
        {"lblNoOfUnusedChequeLeafs", "No of Unused Cheque Leaves"},
        {"lblAccountClosingCharges", "Account Closing Charges"},
        {"btnAccountNumber", ""},
        {"lblCustomerNameDisplay", "null"},
        {"DebitBalance","Debit Balance account can not be closed"},
        {"TOCommandError", "TO Status Command is null"},
        {"freezeAmount", "Account cannot be closed as Freeze exists."},
        {"lienAmount", "Account cannot be closed as Lien exists."},
        {"shadowCrDr", "Shadow Credit and Debit should be zero for closing the Account."},
        {"UnclearBalance","Account Cannot Be Closed As UnclearBalance Exists"},
        {"TodAmount", "Account cannot be closed as TOD exists."},
        {"FlexiAmount", "Account cannot be closed as Flexi Deposit exists."},

   };

}

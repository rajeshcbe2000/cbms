/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InventoryDetailsRB.java
 * 
 * Created on Tue Aug 24 10:35:33 IST 2004
 */

package com.see.truetransact.ui.supporting.inventory;

import java.util.ListResourceBundle;

public class InventoryDetailsRB extends ListResourceBundle {
    public InventoryDetailsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblChequeTo", "To"},
        {"lblTranIDDesc", ""},
        {"lblItemSubTypeDesc", ""},
        {"lblAvailableBooksDesc", ""},
        {"lblLeavesNODesc", ""},
        {"btnAuthorize", ""},
        {"lblStatus1", "                      "},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"lblBookFrom", "From"},
        {"lblTransDate", "Transaction Date"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblBookTo", "To"},
        {"lblSpace3", "     "},
        {"lblAccountNo", "Account No."},
        {"lblProdType", "Product Type"},
        {"lblChequeFrom", "From"},
        {"lblSpace1", " Status :"},
        {"lblTransDateDesc", ""},
        {"btnDelete", ""},
        {"lblBookSeries", "Book Series"},
        {"btnReject", ""},
        {"lblProdTypeDesc", "Account No."},
        {"btnEdit", ""},
        {"lblItemID", "Item ID"},
        {"lblTranID", "Transaction ID"},
        {"lblItemSubType", "Item SubType"},
        {"lblLeavesNO", "No. of Leaves"},
        {"lblAvailableBooks", "Available Books"},
        {"lblInstrumentPrefix", "Instrument Prefix"},
        {"lblAccountNoDesc", ""},
        {"btnItemID", ""},
        {"btnItemLst", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblChequeNo", "Instrument No."},
        {"lblBookQuantity", "Book Quantity"},
        {"btnPrint", ""},
        {"lblTransType", "Transaction Type"},
        
        {"ISTRU_WARNING", "The Entered Instrument Prefix and Instrument Series is already Entered."},
        {"CHEQUENOWARNING", "To Cheque No. Should Be Greater Than The From Cheque No."} 

   };

}

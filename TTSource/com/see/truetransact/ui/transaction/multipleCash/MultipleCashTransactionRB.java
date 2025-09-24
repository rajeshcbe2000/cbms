/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.transaction.multipleCash;
import java.util.ListResourceBundle;
public class MultipleCashTransactionRB extends ListResourceBundle {
    public MultipleCashTransactionRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblDateofComplaint", "Date of Complaint"},
        {"lblCustId", "Customer Id"},
        {"lblNameAddress", "Name and Address"},
        {"lblComments", "Comments Left"}
    };
}

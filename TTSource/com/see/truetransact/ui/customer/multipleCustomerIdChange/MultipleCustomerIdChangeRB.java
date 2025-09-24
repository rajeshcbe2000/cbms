/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.customer.multipleCustomerIdChange;
import java.util.ListResourceBundle;
public class MultipleCustomerIdChangeRB extends ListResourceBundle {
    public MultipleCustomerIdChangeRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblAccountNumber", "Account Number"},
        {"lblAcctName", "Account Name"},
        {"lblStatusBy", "Status By"},
        {"lblNewCustId", "New Customer ID"},
        {"lblOldCustID", "Existing Customer ID"},
        {"lblProductId", "Product ID"},
        {"lblProdType", "ProdType"},
        {"lblCreatedDt","Created Date"},
    };
}

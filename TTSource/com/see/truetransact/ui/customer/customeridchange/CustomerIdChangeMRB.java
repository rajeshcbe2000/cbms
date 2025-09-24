/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.customer.customeridchange;
import java.util.ListResourceBundle;
public class CustomerIdChangeMRB extends ListResourceBundle {
    public CustomerIdChangeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtAccountNumber", "Account Number should not be empty!!!"},
        {"cboProductId", "Product ID should be a proper value!!!"},
        {"cboProdType", "Product Type should be a proper value!!!"},
        {"txtOldCustID", "Customer Old No should be a proper value!!!"},
        {"txtNewCustId", "Customer New No should be a proper value!!!"},
   };
}

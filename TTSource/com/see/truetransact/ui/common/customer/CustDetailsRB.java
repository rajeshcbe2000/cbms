/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * CustDetailsRB.java
 * 
 * Created on Fri Dec 24 10:13:55 IST 2004
 */

package com.see.truetransact.ui.common.customer;

import java.util.ListResourceBundle;

public class CustDetailsRB extends ListResourceBundle {
    public CustDetailsRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"custId", "Customer Id"} ,
        {"custName", "Customer Name"} ,
        {"custStreet", "Street"} ,
        {"custArea", "Area"} ,
        {"custCity", "City"} ,
        {"custPinCode", "Pin Code"} ,
        {"custCountry", "Country"} ,
        {"custState", "State"},
        {"dob", "Date of Birth"}
   };

}

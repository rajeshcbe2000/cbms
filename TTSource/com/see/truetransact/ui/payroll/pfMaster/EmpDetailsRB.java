/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * EmpDetailsRB.java
 * 
 * 
 * @author anjuanand
 */
package com.see.truetransact.ui.payroll.pfMaster;

import java.util.ListResourceBundle;

public class EmpDetailsRB extends ListResourceBundle {

    public EmpDetailsRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"custId", "Customer Id"},
        {"custName", "Customer Name"},
        {"empStreet", "Street"},
        {"empArea", "Area"},
        {"empCity", "City"},
        {"empPinCode", "Pin Code"},
        {"empCountry", "Country"},
        {"empState", "State"},
        {"dob", "Date of Birth"}
    };
}

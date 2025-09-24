/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

import java.util.ListResourceBundle;

public class PersonalSuretyConfigurationMRB extends ListResourceBundle {
    public PersonalSuretyConfigurationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtMaxSurety", "Maximum Number of Surety Applicable  should be a proper value!!!"},
        {"txtCloseBefore", "Need to close the Loan before month should be a proper value!!!"},
        {"txtMaxLoanSurety", "Maximum Number of Loan Applicable For Surety should be a proper value!!!"}
    };
}

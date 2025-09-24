/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigMRB.java
 * 
 * Created on Thu Jan 20 16:39:25 IST 2005
 */

package com.see.truetransact.ui.servicetax.servicetaxSettings;

import java.util.ListResourceBundle;

public class ServiceTaxSettingsMRB extends ListResourceBundle {
    public ServiceTaxSettingsMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
       // {"txtNoOfTokens", "Displays Number of Tokens Configured"},
        {"txtwefDate", "Date should not be empty!!!"},
        {"txtTaxRate", "Tax Rate should not be empty!!!"},
        {"txtEduCess", "Education Cess should not be empty!!!"},
        {"txthigherCess", "Secondary and  higher education cess should not be empty!!!"},
        {"txttaxHeadId", "Service Tax Head is not selected!!!"} 
   };

}

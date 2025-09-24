/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexExchangeRB.java
 * 
 * Created on Wed May 05 12:31:08 IST 2004
 */

package com.see.truetransact.ui.forex;

import java.util.ListResourceBundle;

public class ForexExchangeRB extends ListResourceBundle {
    public ForexExchangeRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblMinutes", "mins"},
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblBaseCurrencyDesc", ""},
        {"lblExchangeIDDesc", "null"},
        {"lblHours", "hrs"},
        {"btnEdit", ""},
        {"lblMiddleRate", "Middle Rate"},
        {"lblStatus1", "                      "},
        {"lblTransCurrency", "Transaction Currency"},
        {"lblMsg", ""},
        {"btnNew", ""},
        {"lblValueTime", "Value Time"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblValidDate", "Value Date"},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblBaseCurrency", "Base Currency"},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        {"lblExchangeID", "Exchange ID"}, 
        
        {"tblColumn1", "Value Date"},
        {"tblColumn2", "Base Currency"},
        {"tblColumn3", "Transaction Currency"},
        {"tblColumn4", "Middle Rate"}

   };

}

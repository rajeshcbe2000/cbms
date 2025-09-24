/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ForexDenominationMasterMRB.java
 * 
 * Created on Thu Jan 27 12:13:43 IST 2005
 */

package com.see.truetransact.ui.sysadmin.denomination;

import java.util.ListResourceBundle;

public class ForexDenominationMasterMRB extends ListResourceBundle {
    public ForexDenominationMasterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboCurrency", "Currency should be a proper value!!!"},
        {"cboDenominationType", "Type should be a proper value!!!"},
        {"txtDenominationName", "DenominationName should not be empty!!!"},
        {"txtDenominationValue", "DenominationValue should not be empty!!!"} 

   };

}

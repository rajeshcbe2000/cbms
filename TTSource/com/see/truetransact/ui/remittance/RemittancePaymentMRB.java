/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RemittancePaymentMRB.java
 */

package com.see.truetransact.ui.remittance;

import java.util.ListResourceBundle;

public class RemittancePaymentMRB extends ListResourceBundle {
    public RemittancePaymentMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtSerialNumber", "Enter the variable No. of the Instrument"},
        {"cboPayStatus", "Select the Mode of Payment/Cancellation"},
        {"txtAddress", "Enter the Address of the Payee"},
        {"txtRemarks", "Enter the Remarks"},
        {"txtCharges", "Enter the Charges"},
        {"txtServiceTax", "Enter the ServiceTax"},
        {"cboInstrumentType", "Enter the Type of Instrument"},
        {"txtPayAmount", "Enter the amount of Instrument to be paid"},
        {"txtNumber1", "Enter the Instrument Number"},
        {"txtNumber2", "Enter the Instrument Number"}
   };

}

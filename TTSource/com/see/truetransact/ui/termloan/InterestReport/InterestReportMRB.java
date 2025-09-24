/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InterestReportMRB.java
 */

package com.see.truetransact.ui.termloan.InterestReport;
import java.util.ListResourceBundle;
public class InterestReportMRB extends ListResourceBundle {
    public InterestReportMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboProductType","Select Product Type"},
        {"cboProductID" , "Select Product Id"},
        {"txtAccountNumber","Enter the AccountNumber."},
        {"cboChargesType","Select Charges Type"},
        {"tdtChargesDate","Select Charges Date"},
        {"txtChargesAmount","Enter the  Charges Amount"}
   };

}

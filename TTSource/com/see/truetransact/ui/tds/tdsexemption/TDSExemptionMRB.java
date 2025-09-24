/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TDSExemptionMRB.java
 * 
 * Created on Tue Feb 01 17:50:13 IST 2005
 */

package com.see.truetransact.ui.tds.tdsexemption;

import java.util.ListResourceBundle;

public class TDSExemptionMRB extends ListResourceBundle {
    public TDSExemptionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtCustomerId", "CustomerId should not be empty!!!"},
        {"tdtSubmitDate", "Submit Date should not be empty!!!"},
        {"tdtEndDate", "Period To should not be empty!!!"},
        {"txtRefNo", "RefNo should not be empty!!!"},
        {"tdtStartDate", "Period To should not be empty!!!"},
        {"txtExemptId", "ExemptId should not be empty!!!"}, 
        {"txtPanNo", "PAN Number should not be empty!!!"}, 
        {"txtRemarks", "Remarks should not be empty!!!"} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.product.loan.loaneligibilitymaintenance;
import java.util.ListResourceBundle;
public class LoanEligibilityMaintenanceMRB extends ListResourceBundle {
    public LoanEligibilityMaintenanceMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        
        {"cboCropType", "Crop Type should not be empty!!!"},
        {"txtEligibileAmount", "EligibileAmount should not be empty!!!"},
        {"tdtFromDate", "FromDate should not be empty!!!"},
        {"tdtToDate", "ToDate should be selected!!!"}
      
      
   };

}

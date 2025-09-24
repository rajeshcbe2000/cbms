/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.product.groupmdsdeposit;
import com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.*;
import java.util.ListResourceBundle;
public class GroupMDSDepositMRB extends ListResourceBundle {
    public GroupMDSDepositMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        
        {"txtGroupName", "Group Name should not be empty!!!"},
        {"txtCount", "Scheme Count should not be empty!!!"},
        {"cbmProductType", "Product Type should not be empty!!!"},
        {"cbmInterestAmount", "Interest Amount Type should be selected!!!"},
        {"txtInterestAmount", "Interest Amount should not be empty!!!"},
        {"cbmPenalCalculation", "Penal Calculation Type should be selected!!!"},
        {"txtPenalCalculation", "Penal Calculation should not be empty!!!"},
        {"cbmInterestRecovery", "Interest Recovery type should be selected!!!"},
        {"txtInterestRecovery", "Interest Recovery should not be empty!!!"},
        {"tdtStartDate", "Start Date should not be empty!!!"},
        {"tdtEndDate", "End Date should not be empty!!!"}
      
      
   };

}

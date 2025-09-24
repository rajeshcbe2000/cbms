/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.product.groupmdsdeposit;
import com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.*;
import java.util.ListResourceBundle;
public class GroupMDSDepositRB extends ListResourceBundle {
    public GroupMDSDepositRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"rdoMds", "MDS"},
        {"rdoDeposit", "Deposit"},
        {"txtGroupName", "Group Name"},
        {"txtCount", "No of schemes"},
        {"cbmProductType", "Product Type"},
        {"cbmInterestAmount", "Interest Amount Type"},
        {"txtInterestAmount", "Interest Amount"},
        {"cbmPenalCalculation", "Penal Calculation Type"},
        {"txtPenalCalculation", "Penal Calculation"},
        {"cbmInterestRecovery", "Interest Recovery Type"},
        {"txtInterestRecovery", "Interest Recovery"},
        {"tdtStartDate", "Start Date"},
        {"tdtEndDate", "End Date"}
   };

}

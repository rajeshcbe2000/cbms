/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentMRB.java
 * 
 * Created on Wed Feb 02 12:57:26 IST 2005
 */

package com.see.truetransact.ui.termloan.appraiserCommision;

import java.util.ListResourceBundle;

public class AppraiserCommisionMRB extends ListResourceBundle {
    public AppraiserCommisionMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtAgentID", "AgentID should not be empty!!!"},
        {"tdtApptDate", "ApptDate should not be empty!!!"}, 
        {"txtOperativeAccNo", "Operative Acc No should not be empty!!!"},
        {"txtDepositCreditedTo", "Deposit No should not be empty!!!"},
        {"cboProductType", "Collection ProductType should not be empty!!!"},
        {"txtCollSuspACNum", "CollSuspACNum should not be empty!!!"}


        

   };

}

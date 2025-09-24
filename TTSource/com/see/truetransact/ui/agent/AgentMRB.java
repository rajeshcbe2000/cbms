/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentMRB.java
 * 
 * Created on Wed Feb 02 12:57:26 IST 2005
 */

package com.see.truetransact.ui.agent;

import java.util.ListResourceBundle;

public class AgentMRB extends ListResourceBundle {
    public AgentMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtAgentID", "Agent ID should not be empty!!!"},
        {"tdtApptDate", "Appointed Date should not be empty!!!"}, 
        {"txtOperativeAccNo", "Operative Account Number should not be empty!!!"},
        {"txtDepositCreditedTo", "Security Deposit No should not be empty!!!"},
        {"cboProductType", "Collection Product Type should not be empty!!!"},
        {"txtCollSuspACNum", "Collection Suspense Account Number should not be empty!!!"},
        {"txtCommisionCreditedTo", "Commision Credited To should not be empty!!!"}

   };

}

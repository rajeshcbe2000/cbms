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

package com.see.truetransact.ui.transaction.agentCommisionDisbursal;

import java.util.ListResourceBundle;

public class AgentCommisionDisbursalMRB extends ListResourceBundle {
    public AgentCommisionDisbursalMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
//        {"txtCommision", "Commision should not be empty!!!"},
//        {"txtCommisionDuringThePeriod", "Commision During The Period should not be empty!!!"},
//        {"txtCommisionForThePeriod", "Commision For The Period should not be empty!!!"},
        {"cboAgentId", "Agent Id should not be empty!!!"}

   };

}

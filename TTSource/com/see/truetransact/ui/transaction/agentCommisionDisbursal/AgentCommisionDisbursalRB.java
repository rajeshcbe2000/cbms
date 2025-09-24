/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * AgentRB.java
 * 
 * Created on Wed Feb 02 12:50:41 IST 2005
 */

package com.see.truetransact.ui.transaction.agentCommisionDisbursal;

import java.util.ListResourceBundle;

public class AgentCommisionDisbursalRB extends ListResourceBundle {
    public AgentCommisionDisbursalRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblName", "Name"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblStatus1", "                      "},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"lblMsg", ""},
        {"lblSpace4", "     "},
        {"btnNew", ""},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"btnPrint", ""},
        {"lblAgentID", "Agent Id"},        
        {"lblCommision", "Commision"},        
        {"lblAgentName","Agent Name"},
        {"lblNameForAgent","" },
        {"lblFromDate","From Date"},
        {"lblToDate","To Date"},
        {"lblCommision","Collections During The Period"},
        {"lblCollectionsDuringThePeriod",""},
        {"lblCommisionForThePeriod","Commision For The Period"},
        {"tdtToDate", " "},
        {"tdtFromDate", " "},
        {"txtCommision", " "},
        {"txtCommisionDuringThePeriod", " "},
        {"txCommisionForThePeriod",""},
        {"lblTDS","TDS"},
        {"lblComtoOA","Commission TO Oprative A/C"},
        {"lblCommTD","Commission TO Deposit A/C"},
        {"btnPayDetails"," Pay Details"}

   };

}

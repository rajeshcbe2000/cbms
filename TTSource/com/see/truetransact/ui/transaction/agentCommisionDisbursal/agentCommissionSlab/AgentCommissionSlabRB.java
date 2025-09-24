/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * SHGRB.java
 * 
 * Created on Sat Oct 15 11:50:15 IST 2011
 */

package com.see.truetransact.ui.transaction.agentCommisionDisbursal.agentCommissionSlab;

import com.see.truetransact.ui.termloan.groupLoan.*;
import com.see.truetransact.ui.termloan.SHG.*;
import java.util.ListResourceBundle;

public class AgentCommissionSlabRB extends ListResourceBundle {
    public AgentCommissionSlabRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblStreet", "Street"},
        {"lblStreetVal", ""},
        {"lblCity", "City"},
        {"lblGroupLoan", "Group Loan Number"},
        {"lblCCNo", "Credit Card No "},
        {"txtCCNo", ""},
        {"txtLimitAmt", ""},
        {"btnAuthorize", ""},
        {"lblMsg", ""},
        {"btnException", ""},
        {"lblAreaVal", ""},
        {"lblSpace4", "     "},
        {"btnSHGSave", ""},
        {"lblMemberNo", "Member No"},
        {"btnSave", ""},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"btnView", ""},
        {"lblArea", "Area"},
        {"lblCityVal", ""},
        {"lblSpace5", "     "},
        {"btnDelete", ""},
        {"btnMemberNo", ""},
        {"lblSpace", " Status :"},
        {"btnReject", ""},
        {"btnEdit", ""},
        {"lblMemberNameVal", ""},
        {"lblStateVal", ""},
        {"lblMemberName", "Member Name"},
        {"btnSHGNew", ""},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblState", "State"},
        {"lblSpace6", "     "},
        {"btnSHGDelete", ""},
        {"btnPrint", ""} 
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * AgentRB.java
 * 
 * Created on Wed Feb 02 12:50:41 IST 2005
 */

package com.see.truetransact.ui.agent;

import java.util.ListResourceBundle;

public class AgentRB extends ListResourceBundle {
    public AgentRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnDelete", ""},
        {"btnClose", ""},
        {"lblApptDate", "Appointed Date"},
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
        {"lblAgentID", "Agent ID"},
        {"btnSave", ""},
        {"btnCancel", ""},
        {"lblSpace3", "     "},
        {"lblSpace1", " Status :"},
        {"lblNameValue", ""},
        {"btnAgentID", ""},
        {"btnPrint", ""},
        {"lblRemarks", "Remarks"} ,
        {"lblCommisionCreditedTo","Commision Credited To"},
        {"lblDepositCreditedTo","Deposit Credited To"},
        {"lblProdIdlName",""},
        {"lblDepositName",""},
        {"btnCommisionCreditedTo",""},
        {"btnDepositCreditedTo",""},
        {"txtCommisionCreditedTo",""},
        {"txtDepositCreditedTo",""},
        {"tblColumn1", "Deposit No"},
        {"tblColumn2", "Customer Name"},
        {"tblColumn3", "Date of Deposit"},
        {"btnCollSuspACNum", ""},
        {"lblCollSuspACnum", "Collection SuspAc"},
        {"lblCollSuspProdID", "Collection SuspProdID"},
        {"lblCollSuspProdtype", "Collection SuspProdType"},
        {"lblCustName", "A/C Holder Name"},
        {"lblCustNameVal", ""},
        {"lblLstComPaiddtVal", ""},
        {"lblLstComPaidDt", "Last Commision Paid Date"}, 
       
        {"tblColumn4", "Deposit Amount"},
            
        {"tblColumnLeave1", "SLNO"},
        {"tblColumnLeave2", "Agent Id"},
        {"tblColumnLeave3", "Agent Name"},
        {"tblColumnLeave4", "Agent Id"},
        {"tblColumnLeave5", "Agent Name"},
        {"tblColumnLeave6", "From Date"},
        {"tblColumnLeave7", "To Date"}

   };

}

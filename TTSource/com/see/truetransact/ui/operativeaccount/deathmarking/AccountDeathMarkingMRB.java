/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * DeathMarkingMRB.java
 * 
 * Created on Thu Jun 03 14:21:25 GMT+05:30 2004
 */

package com.see.truetransact.ui.operativeaccount.deathmarking;

import java.util.ListResourceBundle;

public class AccountDeathMarkingMRB extends ListResourceBundle {
    public AccountDeathMarkingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"tdtReportedOn", "ReportedOn should not be empty!!!"},
        {"txtAccountNumber", "AccountNumber should not be empty!!!"},
        {"tdtDtOfDeath", "DateOfDeath should not be empty!!!"},
        {"cboProductId", "ProductId should be a proper value!!!"},
        {"txtReportedBy", "ReportedBy should not be empty!!!"},
        {"cboRelationship", "Relationship should be a proper value!!!"},
        {"txtReferenceNo", "ReferenceNo should not be empty!!!"} 

   };

}

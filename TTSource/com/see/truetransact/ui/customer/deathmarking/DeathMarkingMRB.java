/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathMarkingMRB.java
 * 
 * Created on Wed May 26 17:47:00 GMT+05:30 2004
 */

package com.see.truetransact.ui.customer.deathmarking;

import java.util.ListResourceBundle;

public class DeathMarkingMRB extends ListResourceBundle {
    public DeathMarkingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtDtOfDeath", "Enter the Date of Death"},
        {"cboRelationship", "RelationShip should not empty!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"txtCustomerId", "CustomerId should not be empty!!!"},
        {"txtReportedBy", "ReportedBy should not be empty!!!"},
        {"tdtReportedOn", "Enter the Reported Date"},
        {"txtReferenceNo", "ReferenceNo should not be empty!!!"} 

   };

}

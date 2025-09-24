/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * InterestMaintenanceMRB.java
 * 
 * Created on Mon Jan 17 18:45:15 IST 2005
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.ListResourceBundle;

public class InterestSubsidyRateMaintenanceMRB extends ListResourceBundle {

    public InterestSubsidyRateMaintenanceMRB() {
    }

    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"txtPLR", "PLR should not be empty!!!"},
        {"tdtDate", "Date should not be empty!!!"},
        {"txtLimitAmt", "LimitAmt should not be empty!!!"},
        {"txtPenalInterest", "PenalInterest should not be empty!!!"},
        {"txtInterExpiry", "InterExpiry should not be empty!!!"},
        {"txtFloatingRate", "FloatingRate should not be empty!!!"},
        {"cboProdType", "ProdType should be a proper value!!!"},
        {"cboToPeriod", "ToPeriod should be a proper value!!!"},
        {"cboInstitution", "Institution should be a proper value!!!"},
        {"cboToAmount", "ToAmount should be a proper value!!!"},
        {"txtFromPeriod", "FromPeriod should not be empty!!!"},
        {"txtToPeriod", "ToPeriod should not be empty!!!"},
        {"txtAgainstInterest", "AgainstInterest should not be empty!!!"},
        {"txtRateInterest", "RateInterest should not be empty!!!"},
        {"txtGroupName", "GroupName should not be empty!!!"},
        //        {"tdtToDate", "ToDate should not be empty!!!"},
        {"cboFromPeriod", "FromPeriod should be a proper value!!!"}
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PowerOfAttorneyMRB.java
 *
 * Created on December 23, 2004, 5:35 PM
 */

package com.see.truetransact.ui.common.powerofattorney;

import java.util.ListResourceBundle;

/**
 *
 * @author  152713
 */
public class PowerOfAttorneyMRB extends ListResourceBundle {
    
    /** Creates a new instance of PowerOfAttorneyMRB */
    public PowerOfAttorneyMRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboAddrType_PoA", "Enter the Address Type of PoA Holder."},
        {"txtPoANo", " "},
        {"txtPoaHolderName", "Enter the Name of the Power of Attorney Holder"},
        {"cboPoACust", "On Behalf of"},
        {"tdtPeriodTo_PowerAttroney", "Enter the date up to which the PoA is being granted."},
        {"tdtPeriodFrom_PowerAttroney", "Enter the date from which the PoA is being granted."},
        {"txtPin_PowerAttroney", "Enter the PIN Code."},
        {"cboState_PowerAttroney", "Choose a  State"},
        {"cboCity_PowerAttroney", "Choose a  City"},
        {"txtStreet_PowerAttroney", "Enter the Address type of the PoA Holder."},
        {"cboCountry_PowerAttroney", "Choose a  Country"},
        {"txtRemark_PowerAttroney", ""},
        {"txtArea_PowerAttroney", "Enter the Address type of the PoA Holder."},
        {"txtPhone_PowerAttroney", "Enter the Phone Number."},
        {"txtCustID_PoA", "Enter the Customer ID."}
    };
}

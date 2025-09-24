/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ActTransMRB.java
 *
 * Created on December 23, 2004, 5:35 PM
 */

package com.see.truetransact.ui.termloan.accounttransfer;

import java.util.ListResourceBundle;

/**
 *
 * @author  152713
 */
public class ActTransMRB extends ListResourceBundle {
    
    /** Creates a new instance of PowerOfAttorneyMRB */
    public ActTransMRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboBankName", "Enter the Address Type of PoA Holder."},
        {"cboBranchName", " "},
        {"txtRefNo", "Enter the Name of the Power of Attorney Holder"},
        {"txtAmt", "On Behalf of"},
        {"txtSecDepRec", "Enter the date up to which the PoA is being granted."},
        {"txtPoDdNo", "Enter the date from which the PoA is being granted."},
        {"tdtPoDdDate", "Enter the PIN Code."},
        {"txtPoDdAmt", "Choose a  State"},
        {"txtRemarks", "Choose a  City"},
//        {"cboBounReason", "Enter the Address type of the PoA Holder."},
//        {"txtRemarks", "Choose a  Country"},
//        {"txtRemark_PowerAttroney", ""},
//        {"txtArea_PowerAttroney", "Enter the Address type of the PoA Holder."},
//        {"txtPhone_PowerAttroney", "Enter the Phone Number."},
//        {"txtCustID_PoA", "Enter the Customer ID."}
    };
}

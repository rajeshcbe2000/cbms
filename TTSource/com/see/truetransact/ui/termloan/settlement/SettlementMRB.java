/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SettlementMRB.java
 *
 * Created on December 23, 2004, 5:35 PM
 */

package com.see.truetransact.ui.termloan.settlement;

import java.util.ListResourceBundle;

/**
 *
 * @author  152713
 */
public class SettlementMRB extends ListResourceBundle {
    
    /** Creates a new instance of PowerOfAttorneyMRB */
    public SettlementMRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboBankName", "Enter the Address Type of PoA Holder."},
        {"cboBranchName", " "},
        {"txtActNo", "Enter the Name of the Power of Attorney Holder"},
        {"txtFromChqNo", "On Behalf of"},
        {"txtToChqNo", "Enter the date up to which the PoA is being granted."},
        {"txtQty", "Enter the date from which the PoA is being granted."},
        {"tdtChqDate", "Enter the PIN Code."},
        {"txtChqAmt", "Choose a  State"},
        {"tdtClearingDt", "Choose a  City"},
        {"cboBounReason", "Enter the Address type of the PoA Holder."},
        {"txtRemarks", "Choose a  Country"},
//        {"txtRemark_PowerAttroney", ""},
//        {"txtArea_PowerAttroney", "Enter the Address type of the PoA Holder."},
//        {"txtPhone_PowerAttroney", "Enter the Phone Number."},
//        {"txtCustID_PoA", "Enter the Customer ID."}
    };
}

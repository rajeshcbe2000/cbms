/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizedSignatoryMRB.java
 *
 * Created on December 23, 2004, 11:29 AM
 */

package com.see.truetransact.ui.common.authorizedsignatory;
import java.util.ListResourceBundle;
/**
 *
 * @author  152713
 */
public class AuthorizedSignatoryMRB extends ListResourceBundle {
    
    /** Creates a new instance of AuthorizedSignatoryMRB */
    public AuthorizedSignatoryMRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"txtCustomerID", "Enter the Customer ID"},
        {"txtPager_AuthorizedSignatory", "Enter the Pager number."},
        {"txtHomeFax_AuthorizedSignatory", "Enter the Home Fax number."},
        {"cboState_AuthorizedSignatory", "Choose a  State"},
        {"txtName_AuthorizedSignatory", "Enter the Name of the Authorized Signatory"},
        {"cboAddrCommunication_AuthorizedSignatory", "Enter the Address type of the Authorized Signatory"},
        {"txtAreaCode_AuthorizedSignatory", "Enter the Area code"},
        {"txtArea_AuthorizedSignatory", "Enter the Address type of the Authorized Signatory"},
        {"txtMobile_AuthorizedSignatory", "Enter the Mobile number."},
        {"txtPin_AuthorizedSignatory", "Enter the PIN Code"},
        {"txtBusinessFax_AuthorizedSignatory", "Enter the Business  Fax."},
        {"txtBusinessPhone_AuthorizedSignatory", "Enter the Business  Phone."},
        {"txtDesig_AuthorizedSignatory", "Enter the Designation of the Authorized Signatory"},
        {"cboCity_AuthorizedSignatory", "Choose a  City"},
        {"txtStreet_AuthorizedSignatory", "Enter the Address type of the Authorized Signatory"},
        {"cboCountry_AuthorizedSignatory", "Choose a  Country"},
        {"txtEmailId_AuthorizedSignatory", "Enter the Email ID."},
        {"txtHomePhone_AuthorizedSignatory", "Enter the Home Phone."},
        {"txtLimits", "Enter the Limit"},
        {"txtFromAmount", "Enter the From Amount."},
        {"txtToAmount", "Enter the To Amount."},
        {"txtInstruction", "Enter the Instruction"},
        {"txtNumberAuthSignatory", "Enter the number of authorized signatories for this account"}
    };
}

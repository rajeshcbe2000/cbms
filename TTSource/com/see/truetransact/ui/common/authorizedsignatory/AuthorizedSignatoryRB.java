/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizedSignatoryRB.java
 *
 * Created on December 23, 2004, 11:29 AM
 */

package com.see.truetransact.ui.common.authorizedsignatory;
import java.util.ListResourceBundle;
/**
 *
 * @author  152713
 */
public class AuthorizedSignatoryRB extends ListResourceBundle {
    
    /** Creates a new instance of AuthorizedSignatoryRB */
    public AuthorizedSignatoryRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblBusinessPhone_AuthorizedSignatory", "Business Phone"},
        {"lblState_AuthorizedSignatory", "State"},
        {"lblAddrCommunication_AuthorizedSignatory", "Communication Address"},
        {"lblArea_AuthorizedSignatory", "Area"},
        {"lblAreaCode_AuthorizedSignatory", "Area Code"},
        {"lblHomePhone_AuthorizedSignatory", "Home Phone"},
        {"lblEmailId_AuthorizedSignatory", "Email ID"},
        {"lblMobile_AuthorizedSignatory", "Mobile"},
        {"lblHomeFax_AuthorizedSignatory", "Home Fax"},
        {"lblPin_AuthorizedSignatory", "PIN Code"},
        {"lblName_AuthorizedSignatory", "Name"},
        {"lblPager_AuthorizedSignatory", "Pager"},
        {"lblBusinessFax_AuthorizedSignatory", "Business Fax"},
        {"lblStreet_AuthorizedSignatory", "Street"},
        {"lblCountry_AuthorizedSignatory", "Country"},
        {"lblDesig_AuthorizedSignatory", "Designation"},
        {"lblCity_AuthorizedSignatory", "City"},
        {"btnAuthorizedSave", "Save"},
        {"btnAuthorizedDelete", "Delete"},
        {"btnAuthorizedNew", "New"},
        {"lblNumberAuthSignatory", "Number of Authorized Signatory"},
        {"btnCustomerID", ""},
        {"lblCustomerID", "Customer ID"},
        {"lblLimits", "Limit"},
        {"panAuthorizedSignatory", "Authorized Signatory"},
        
        {"panInstruction", "Instruction"},
        {"btnInstructionSave", "Save"},
        {"btnInstructionDelete", "Delete"},
        {"btnInstructionNew", "New"},
        {"lblFromAmount", "From Amount"},
        {"lblToAmount", "To Amount"},
        {"lblInstruction", "Instruction"},
        
        {"tblColumnAuth1", "AS No."},
        {"tblColumnAuth2", "Customer ID"},
        {"tblColumnAuth3", "Name"},
        {"tblColumnAuth4", "Limits"},
        
        {"tblColumnAuthInstructionSlNo", "Instruction No."},
        {"tblColumnAuthInstructionFromAmt", "From Amount"},
        {"tblColumnAuthInstructionToAmt", "To Amount"},
        {"tblColumnAuthInstruction", "Instruction"},
        
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        {"cDialogOk", "Ok"},
        
        {"limitLessThanOrEqualToZeroWarning", "Limit must be greater than Zero"},
        {"acctLevelCustWarning", "This Customer already exists in Account Level"},
        {"existenceAuthSignTableWarning", "Authorized Signatory Table should contain "}
    };
}

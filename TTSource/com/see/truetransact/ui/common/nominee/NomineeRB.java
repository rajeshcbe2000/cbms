/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * NomineeRB.java
 * 
 * Created on Fri Dec 24 10:13:55 IST 2004
 */

package com.see.truetransact.ui.common.nominee;

import java.util.ListResourceBundle;

public class NomineeRB extends ListResourceBundle {
    public NomineeRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblNState", "State"},
        {"lblNStreet", "Street"},
        {"lblNPinCode", "Pincode"},
        {"lblNomineeShareNO", "Share (%)"},
        {"panNomineeDetails", "Nominee Details"},
        {"lblGStreet", "Street"},
        {"lblGCity", "City"},
        {"rdoStatus_MajorNO", "Major"},
        {"lblTotalShareNO", "Total Share (%)"},
        {"lblMinorDOBNO", "Nominee DOB"},
        {"btnAddNO", "Save"},
        {"lblNArea", "Area"},
        {"lblGState", "State"},
        {"lblGuardianPhoneNO", "Phone No"},
        {"lblGCountry", "Country"},
        {"lblNCountry", "Country"},
        {"btnNewNO", "New"},
        {"lblGArea", "Area"},
        {"panNomineeList", "List of Nominees"},
        {"lblRelationNO", "Relationship"},
        {"lblGuardianNameNO", "Guardian's Name"},
        {"lblGPinCode", "Pincode"},
        {"btnRemoveNO", "Delete"},
        {"lblNomineeStatusNO", "Status"},
        {"lblNomineeRelationNO", "Relationship"},
        {"lblNomineeNameNO", "Name"},
        {"lblMinNominees", "Min Nominees Reqd."},
        {"panGuardianDetails", "In case of Minor"},
        {"lblNCity", "City"},
        {"lblNomineePhoneNO", "Phone No"} ,
        {"lblMaxNominee", "Max Nominees Allowed"} ,
        {"lblCustNo", ""} ,
        {"lblCustomer", "Customer ID"} ,
        
        {"tabNominee1", "ID"} ,
        {"tabNominee2", "Name"} ,
        {"tabNominee3", "Relation"},
        
        {"title", "Nominee"},
        
        {"StatusWarning", "Status should be not empty"} ,
//      {"MoreWarning", "Total Share of the Nominee(s) cannot be more than 100%"} ,
        {"MoreWarning", "Cannot add more Nominee"} ,
        {"MoreRowWarning", "Total Number of the Nominee(s) cannot be more than Max Nominee Allowed"} ,
        {"NoNominee", "Number of Nominee(s) cannot be less than the Minimum No. of Nominee(s)"} ,
        {"LessWarning", "Total Share of the nominee(s) cannot be less than 100%"} 
   };

}

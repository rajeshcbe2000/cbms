/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * NomineeMRB.java
 * 
 * Created on Fri Dec 24 10:31:12 IST 2004
 */

package com.see.truetransact.ui.common.nominee;

import java.util.ListResourceBundle;

public class NomineeMRB extends ListResourceBundle {
    public NomineeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboGCountry", "Country should be a proper value!!!"},
        {"tdtMinorDOBNO", "Nominee DOB should be a proper value!!!"},
        {"txtNomineeNameNO", "Nominee Name should not be empty!!!"},
        {"txtGuardianPhoneNO", "Guardian Phone number should not be empty!!!"},
        {"txtGPinCode", "Guardian PinCode should not be empty!!!"},
        {"txtGArea", "Guardian Area should not be empty!!!"},
        {"txtTotalShareNO", "Total ShareNo. should not be empty!!!"},
        {"txtNomineeACodeNO", "Nominee Area Code should not be empty!!!"},
        {"txtGuardianNameNO", "Guardian Name should not be empty!!!"},
        {"cboNCountry", "Nominee's Country should be a proper value!!!"},
        {"txtNStreet", "Nominee's Street should not be empty!!!"},
        {"cboNomineeRelationNO", "Nominee Relation should be a proper value!!!"},
        {"cboGState", "Guardian State should be a proper value!!!"},
        {"txtNArea", "Nominee Area should not be empty!!!"},
        {"txtGuardianACodeNO", "Guardian Area Code should not be empty!!!"},
        {"cboNState", "NState should be a proper value!!!"},
        {"txtMinNominees", "Minimum No.of Nominees should not be empty!!!"},
        {"cboRelationNO", "Relation should be a proper value!!!"},
        {"txtGStreet", "Guardian Street should not be empty!!!"},
        {"txtNomineePhoneNO", "Nominee Phone number should not be empty!!!"},
        {"cboNomineeStatus", "Nominee Status should be selected!!!"},
        {"txtNPinCode", "Nominee PinCode should not be empty!!!"},
        {"txtNomineeShareNO", "Nominee Share should not be empty!!!"},
        {"cboNCity", "Nominee City should be a proper value!!!"},
        {"cboGCity", "Guardian City should be a proper value!!!"} 

   };

}

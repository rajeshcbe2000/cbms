/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PowerOfAttorneyRB.java
 *
 * Created on December 23, 2004, 4:54 PM
 */

package com.see.truetransact.ui.common.powerofattorney;

import java.util.ListResourceBundle;
/**
 *
 * @author  152713
 */
public class PowerOfAttorneyRB extends ListResourceBundle {
    
    /** Creates a new instance of PowerOfAttorneyRB */
    public PowerOfAttorneyRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblPoANo", "PoA Number"},
        {"lblAddrType_PoA", "Address Type"},
        {"lblPoaHolderName", "PoA Holder's Name"},
        {"lblPoACust", "On Behalf of"},
        {"lblPin_PowerAttroney", "PIN Code"},
        {"lblRemark_PowerAttroney", "Remarks"},
        {"lblState_PowerAttroney", "State"},
        {"lblPeriodFrom_PowerAttroney", "Period From"},
        {"lblCity_PowerAttroney", "City"},
        {"lblArea_PowerAttroney", "Area"},
        {"lblPeriodTo_PowerAttroney", "Period To"},
        {"lblCountry_PowerAttroney", "Country"},
        {"lblPhone_PowerAttroney", "Phone"},
        {"lblStreet_PowerAttroney", "Street"},
        {"lblCustID_PoA", "Customer ID"},
        {"btnNew_PoA", "New"},
        {"btnDelete_PoA", "Delete"},
        {"btnSave_PoA", "Save"},
        
        {"tblColumnPoA1", "PoA No."},
        {"tblColumnPoA2", "PoA Holder"},
        {"tblColumnPoA3", "Period From"},
        {"tblColumnPoA4", "Period To"},
        
        {"cDialogYes", "Yes"},
        {"cDialogNo", "No"},
        {"cDialogCancel", "Cancel"},
        {"cDialogOk", "Ok"},
        
        {"onBehalfOfSameCustWarn", "Same customer cannot give Power of Attorney"},
        {"onBehalfOfCombinationExistWarn", " already gave Power of Attorney to "},
        {"existanceCustomerWarningPoA", "This Customer has entry in Power of Attorney."}
        
    };
}

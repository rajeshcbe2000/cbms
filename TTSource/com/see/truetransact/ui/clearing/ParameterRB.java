/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterRB.java
 *
 * Created on Tue Mar 16 17:45:49 PST 2004
 */

package com.see.truetransact.ui.clearing;

import java.util.ListResourceBundle;

public class ParameterRB extends ListResourceBundle {
    public ParameterRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"lblClearingHD", "Clearing Head"},
        {"rdoHighValue_Yes", "Yes"},
        {"btnICReturnChargesHD", ""},
        {"lblOCReturnChargesHD", "Outward Clearing Return Charges Head"},
        {"btnSave", ""},
        {"lblValueofHighValueCheque", "Value of High Value Cheque"},
        {"lblClearingType", "Clearing Type"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblClearingSuspenseHD", "Clearing Suspense Head"},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"lblMsg", ""},
        {"btnClearingSuspenseHD", ""},
        {"lblHighValueApplicability", "High Value Applicability"},
        {"btnDelete", ""},
        {"lblOCReturnCharges", "Outward Clearing Return Charges"},
        {"lblICReturnCharges", "Inward Clearing Return Charges"},
        {"btnEdit", ""},
        {"lblServiceBranchCode", "Service Branch Code"},
        {"btnClearingHD", ""},
        {"btnNew", ""},
        {"btnOCReturnChargesHD", ""},
        {"btnCancel", ""},
        {"rdoHighValue_No", "No"},
        {"btnPrint", ""},
        {"lblICReturnChargesHD", "Inward Clearing Return Charges Head"},
        {"lblLotSize", "Lot Size MICR Clearing"},
        {"lblClearingFrequency", "Clearing Frequency"},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"btnReject", ""},
        
        {"cDialogOK", "OK"},
        
        {"WarningMessage","This Record Already exist."}
        
    };
    
}

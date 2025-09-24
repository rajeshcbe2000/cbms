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

package com.see.truetransact.ui.clearing.banklevel;

import java.util.ListResourceBundle;

public class BankClearingParameterRB extends ListResourceBundle {
    public BankClearingParameterRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"btnClose", ""},
        {"lblClearingHD", "Clearing Head"},
        {"btnICReturnChargesHD", ""},
        {"lblOCReturnChargesHD", "Outward Clearing Return Charges Head"},
        {"btnSave", ""},
        {"lblClearingType", "Clearing Type"},
        {"lblSpace3", "     "},
        {"lblStatus", "                      "},
        {"lblClearingSuspenseHD", "Clearing Suspense Head"},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"lblMsg", ""},
        {"btnClearingSuspenseHD", ""},
        {"btnDelete", ""},
        {"lblOCReturnCharges", "Outward Clearing Return Charges"},
        {"lblICReturnCharges", "Inward Clearing Return Charges"},
        {"btnEdit", ""},
        {"btnClearingHD", ""},
        {"btnNew", ""},
        {"btnOCReturnChargesHD", ""},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"lblICReturnChargesHD", "Inward Clearing Return Charges Head"},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"btnReject", ""},
        {"rdoCompleteDay_No", "No"},
        {"rdoCompleteDay_Yes", "Yes"},
        {"lblCompleteDayEWCOPosting", "Complete Day End Without CO Posting"},
        {"cDialogOK", "OK"},
        
        {"WarningMessage","This Record Already exist."},
        
        {"lblShortClaimAcHead", "Short Claim Account Head"},
        {"lblExcessClaimAcHead", "Excess Claim Account Head"},
        {"lblOCInstrumentCharges", "Outward Clearing Isntrument Collection Charges"},
         {"lblOCInstrumentChargesHD", "Outward Clearing Instrument Collevtion Charges Head"},
        
    };
    
}

/*
 *Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsRB.java
 *
 * Created on April 5, 2004, 3:59 PM
 */

package com.see.truetransact.ui.clearing.returns;

/**
 *
 * @author  Ashok
 */

import java.util.ListResourceBundle;
public class ReturnOfInstrumentsRB extends ListResourceBundle {
    
    public ReturnOfInstrumentsRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"chkPresentAgain", ""},
        {"lblPresentAgain", "Present Again"},
        {"lblScInstBelongs", "Schedule to which Instrument Belongs"},
        {"lblInstrumentAmount", "Instrument Amount"},
        {"btnClose", ""},
        {"btnAuthorize", ""},
        {"btnException", ""},
        {"btnReject", ""},
        {"lblInstrumentDate", "Instrument Date"},
        {"lblMsg", ""},
        
        {"lblClearingSerialNo", "Instrument No"},
        {"lblSpace2", "     "},
        
        {"btnSave", ""},
        {"lblClearingType", "Clearing Type"},
        {"lblSpace3", "     "},
        {"lblSpace4", "     "},
        {"lblStatus", "                      "},
        
        {"lblSpace1", " Status :"},
        {"lblClearingDate", "Clearing Date"},
        
        {"btnDelete", ""},
        {"lblReturnType", "Return Type"},
        
        {"lblSchInstBelongs", ""},
        {"btnEdit", ""},
        
        {"btnNew", ""},
        {"lblBkPresented", "Bank to which Presented"},
        {"lblInstDate", ""},
        {"lblBrachPres", ""},
        {"lblBrPresented", "Branch to which Presented"},
        {"lblBankPres", ""},
        {"lblReturnId", " "},
        {"lblRetId", ""},
        {"lblBatchId", "Batch Id"},
        {"btnCancel", ""},
        {"btnPrint", ""},
        {"lblInstAmount", ""},
        {"lblReturnId", "Return Id"},
        {"lblRetId", ""},
        {"btnBatchNO", ""},
        
        {"INSTRUMENT_WARNING", "Please enter the complete Instrument No."},
        {"INST_WARNING", "The Instrument No Entered is not valid, Please Enter it Again."}
    };
    
}

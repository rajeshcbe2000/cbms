/*
 *Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReturnOfInstrumentsMRB.java
 *
 * Created on April 5, 2004, 3:59 PM
 */

package com.see.truetransact.ui.clearing.returns;

/**
 *
 * @author  Ashok
 */

import java.util.ListResourceBundle;

public class ReturnOfInstrumentsMRB extends ListResourceBundle {
    
    public ReturnOfInstrumentsMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"chkPresentAgain", "PresentAgain should be selected!!!"},
        {"cboReturnType", "ReturnType should be a proper value!!!"},
        {"tdtClearingDate", "ClearingDate should not be empty!!!"},
        {"txtInstrumentNo1", "InstrumentNo should not be empty!!!"},
        {"txtInstrumentNo2", "InstrumentNo should not be empty!!!"},
        {"txtBatchId", "BatchId should not be empty!!!"},
        {"cboClearingType", "ClearingType should be a proper value!!!"}
        
    };
    
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingTallyMRB.java
 * 
 * Created on Tue Mar 30 12:11:39 PST 2004
 */

package com.see.truetransact.ui.clearing.outwardtally;

import java.util.ListResourceBundle;

public class OutwardClearingTallyMRB extends ListResourceBundle {
    public OutwardClearingTallyMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtClosingDate", "ClosingDate should not be empty!!!"},
        {"txtSBAmount", "SBAmount should not be empty!!!"},
        {"txtDAmount", "DAmount should not be empty!!!"},
        {"txtSBNumberofInstruments", "SBNumberofInstruments should not be empty!!!"},
        {"tdtClearingDate", "ClearingDate should not be empty!!!"},
        {"txtDNumberofInstruments", "DNumberofInstruments should not be empty!!!"},
        {"txtScheduleNo", "ScheduleNo should not be empty!!!"},
        {"cboClearingType", "ClearingType should be a proper value!!!"} 

   };

}

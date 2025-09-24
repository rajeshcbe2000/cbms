/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InwardClearingTallyMRB.java
 */
package com.see.truetransact.ui.clearing.tally;
import java.util.ListResourceBundle;
public class InwardClearingTallyMRB extends ListResourceBundle {
    public InwardClearingTallyMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtServiceInstruments", "ServiceInstruments should not be empty!!!"},
        {"txtServiceAmount", "ServiceAmount should not be empty!!!"},
        {"txtPhysicalAmount", "PhysicalAmount should not be empty!!!"},
        {"txtPhysicalInstruments", "PhysicalInstruments should not be empty!!!"},
        {"tdtClearingDate", "ClearingDate should not be empty!!!"},
        {"txtDifferenveInstrument", "DifferenveInstrument should not be empty!!!"},
        {"txtScheduleNo", "ScheduleNo should not be empty!!!"},
        {"txtDifferenceAmount", "DifferenceAmount should not be empty!!!"},
        {"cboClearingType", "ClearingType should be a proper value!!!"},
        {"cboCurrencyBID", "Currency should be a proper value!!!"} ,
        {"cboCurrencyPC", "Currency should be a proper value!!!"} 

   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * OutwardClearingTallyRB.java
 * 
 * Created on Tue Mar 23 18:03:18 PST 2004
 */

package com.see.truetransact.ui.clearing.outwardtally;

import java.util.ListResourceBundle;

public class OutwardClearingTallyRB extends ListResourceBundle {
    public OutwardClearingTallyRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"btnClose", ""},
        {"lblBookedAmount", ""},
        {"lblInwardReturns", "Inward Returns"},
        {"lblDisplayCurrentClearingDate", ""},
        {"lblMsg", ""},
        {"lblCurrentClearingDate", "Current Clearing Date"},
        {"lblTotal", "Total"},
        {"lblSBNumberofInstruments", "Number of Instruments"},
        {"panServiceBranch", "As Per Service Branch"},
        {"lblNoofInstruments", "No of Instruments"},
        {"lblScheduleNo", "Schedule Number"},
        {"btnSave", ""},
        {"lblClearingType", "ClearingType"},
        {"lblSpace3", "     "},
        {"panSystem", "As Per System"},
        {"lblStatus", "                      "},
        {"lbSpace2", "     "},
        {"lblSpace1", " Status :"},
        {"lblClearingDate", "Clearing Date"},
        {"lblInstrumentsBooked", "Instruments Booked"},
        {"btnDelete", ""},
        {"lblInwardAmount", ""},
        {"lblClosingDate", "Closing Date"},
        {"lblSAmount", "Amount"},
        {"lblTotalAmount", ""},
        {"btnEdit", ""},
        {"lblDNumberofInstruments", "Number of Instruments"},
        {"lblSBAmount", "Amount"},
        {"btnNew", ""},
        {"panDiffSystemServiceBranch", "Differences Between System and Service Branch"},
        {"lblDAmount", "Amount"},
        {"btnCancel", ""},
        {"lblInwardInstruments", ""},
        {"lblTotalInstruments", ""},
        {"btnPrint", ""},
        {"lblBookedInstruments", ""},        
        {"WarningMessage","Pls. Close existing clearing to open a new one.\n"},        
        {"WarningForCloser","Do you want to close the schedule?"},
        {"WarningCloseDt","Close Date is not filled."},
        {"WarningTally","Outward Tally is not created."},
        {"WarningForDate","Closing Date Should be Greater than or Equal to Clearing Date !!!\n"},
        {"WarningFutureDate","Closing Date Should be less than or Equal to Current Date !!!\n"},
        {"checkPending", "Clearing instrument is pending for Authorization."}
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InwardClearingTallyRB.java
 */
package com.see.truetransact.ui.clearing.tally;
import java.util.ListResourceBundle;
public class InwardClearingTallyRB extends ListResourceBundle {
    public InwardClearingTallyRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblSystemBooked", "Instruments Booked"},
        {"btnClose", ""},
        {"lblBookedAmount", ""},
        {"lblDifferenveInstrument", "Number of Instruments"},
        {"lblSystemReturned", "Outward Returns"},
        {"lblMsg", ""},
        {"panServiceBranch", "Service Branch"},
        {"lblScheduleNo", "Schedule Number"},
        {"lblSpace2", "     "},
        {"btnSave", ""},
        {"lblDifferenceAmount", "Amount"},
        {"lblPhysicalInstruments", "Number of Instruments"},
        {"lblClearingType", "Clearing Type"},
        {"lblSpace3", "     "},
        {"lblReturnAmount", ""},
        {"panSystem", "System"},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"},
        {"lblReturnInstrument", ""},
        {"lblClearingDate", "Clearing Date"},
        {"panPhysicalCount", "Physical Count"},
        {"btnDelete", ""},
        {"panDifference", "Difference Between System and Service Branch"},
        {"lblSystemAmount", "Amount"},
        {"lblSystemInstruments", "No of Instruments"},
        {"btnEdit", ""},
        {"lblBookedInstrument", ""},
        {"lblServiceInstruments", "Number of Instruments"},
        {"btnNew", ""},
        {"btnCancel", ""},
        {"lblServiceAmount", "Amount"},
        {"btnPrint", ""},
        {"lblPhysicalAmount", "Amount"},
        
        {"cDialogOk", "Ok"},
        {"dateWarning","Schedule Number already present !!! "},
        
        //UNGENERATED CODE
        {"tblInstCol1","Currency"},
        {"tblInstCol2","Inwards (S)"},
        {"tblInstCol3","Inward Amt (S)"},
        {"tblInstCol4","OutReturns (S)"},
        {"tblInstCol5","OutReturn Amt (S)"},
        {"tblInstCol6","Insts-Service"},
        {"tblInstCol7","Amt-Service"},
        {"tblInstCol8","Insts-(P)"},
        {"tblInstCol9","Amt-(P)"},
        
        {"tblDiffCol1","Currency"},
        {"tblDiffCol2","No. of Instruments"},
        {"tblDiffCol3","Instruments Amount"},
        
        {"checkClearingTypeExist", "Same Clearing Type is already exist. Close the schedule and open a new."},
        {"checkCreated", "Inward Tally is not created."},
        {"WarningForCloser","Do you want to close the schedule?"},
        {"checkTally","Batch is not  tally"},
        {"checkPending", "Clearing instrument is pending for Authorization."}
   };
}

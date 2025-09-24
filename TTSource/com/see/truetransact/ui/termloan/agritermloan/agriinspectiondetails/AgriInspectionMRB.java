/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriInspectionMRB.java
 *
 * Created on May 13, 2009, 5:28 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails;
import java.util.ListResourceBundle;
/**
 *
 * @author  Administrator
 */
public class AgriInspectionMRB extends ListResourceBundle{
    
    /** Creates a new instance of AgriSubLimitMRB */
    public AgriInspectionMRB() {
    }
    
    protected Object[][] getContents() {
        return  contents;
    }
     static final String[][] contents = {
          {"txtSubLimitAmt", "Enter Limit Amount"},
          {"tdtSubLimitFromDt", "Enter Limit FromDate"},
          {"tdtSubLimitToDt", "Enter Limit ToDate"},
          {"cboTypeOfInspection", "Select Type of Inspection"},
          {"tdtDateOfInspection", "Enter Date of Inspection"},
          {"cboInspectionDetails", "Choose Inspection Details"},
          {"txtInspectBy", "Enter Inspect by"},
          {"textAreaInspectObservation", "Enter observation on Inspection"}
        
          
     };
}

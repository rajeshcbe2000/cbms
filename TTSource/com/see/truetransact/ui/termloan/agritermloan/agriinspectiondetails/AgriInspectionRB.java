/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriInspectionRB.java
 *
 * Created on April 28, 2009, 4:13 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agriinspectiondetails;
import java.util.ListResourceBundle;
/**
 *
 * @author  Administrator
 */
public class AgriInspectionRB extends ListResourceBundle {
    
    /** Creates a new instance of AgriSubLimitRB */
    public AgriInspectionRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"lblSubLimitAmt","Sub Limit Amt"},
        {"lblSubLimitFromDt","Sub Limit From Date"},
        {"lblSubLimitToDt","Sub Limit To Date"},
        {"lblTypeOfInspection","Type Of Inspection"},
        {"lblDateOfInspection","Date Of Inspection"},
        {"lblInspectionDetails","Inspection details"},
        {"lblInspectPosition","Designation"},
        {"lblInspectPositions","Inspect positions"},
        {"lblInspectBy","Inspected By"},
        {"lblInsepectionObservation","Inspection Observation"},
        {"btnInspectionNew","New"},
        {"btnInspectionSave","Save"},
        {"btnInspectionDelete","Delete"},
        {"btnsubLimitNew","New"},
        {"btnSubLimitSave","Save"},
        {"btnSubLimitDelete","Delete"},
        {"colum0","Slno"},
        {"colum1","Sub Limit Amt"},
        {"colum2","Start dt"},
        {"colum3","End Dt"},
        {"colum4","Inspection Type"},
        {"colum5","Inspection Date"},
        {"colum6","Inspection Details"},
        {"colum7","Inspect By"},
         {"colum8","Slno"}
        
    };
}

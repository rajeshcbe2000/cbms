/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubLimitRB.java
 *
 * Created on April 28, 2009, 4:13 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agrinewdetails;
import java.util.ListResourceBundle;
/**
 *
 * @author  Administrator
 */
public class AgriSubLimitRB extends ListResourceBundle {
    
    /** Creates a new instance of AgriSubLimitRB */
    public AgriSubLimitRB() {
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
        {"lblHectare","Acres/Hectare"},
        {"lblNumberCount","Number"},
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
        {"colum4","Purpose"},
        {"colum5","Type"},
        {"colum6","Hectare"},
        {"colum7","MainSlno"},
         {"colum8","Slno"}
        
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanDisbursementRB.java
 *
 * Created on April 28, 2009, 4:13 PM
 */

package com.see.truetransact.ui.termloan.loandisbursement;
import java.util.ListResourceBundle;
/**
 *
 * @author  Administrator
 */
public class LoanDisbursementRB extends ListResourceBundle {
    
    /** Creates a new instance of LoanDisbursementRB */
    public LoanDisbursementRB() {
    }
    
    protected Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        



        {"lblDisbursementDt","Disburst Dt"},
        {"lblDisbursementAmt","Disburst Amt"},
        {"lblDisbursementStage","Disburst Stage"},
        {"lblRemarks","Remarks"},
        {"rdoDisbursementAmt","% Loan Amt"},
        
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
        {"colum1","Disburst Dt"},
        {"colum2","Disburst Stage"},
        {"colum3","Amt Rs"},
        {"colum4","Amt %"},
        {"colum5","Type"},
        {"colum6","Hectare"},
        {"colum7","MainSlno"},
         {"colum8","Slno"}
        
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GhanCustomerRB.java
 *
 * Created on April 11, 2012, 4:43 PM
 */

package com.see.truetransact.ui.customer.gahan;
import java.util.ListResourceBundle;

/**
 *
 * @author  admin
 */
public class GahanCustomerRB extends ListResourceBundle{
    
    /** Creates a new instance of GhanCustomerRB */
    public GahanCustomerRB() {
        
        
    }
    
    
    static String content[][] ={
        
        
        {"lblOwnerMemberNo","Customer ID/ Member No"},
        {"lblOwnerMemberNumber","Owner Member/Customer ID"},
        {"lblOwnerMemberName","Member Name"},
        {"lblDocumentNo","Document No"},
        {"lblDocumentType","Document Type"},
        {"lblDocumentDt","Document Date"},
        {"lblRegisteredOffice","Registered Office"},
        {"lblPledge","Pledge Type"},
        {"lblPledgeDate","Pledge Date"},
        {"lblPledgeNo","Pledge No"},
        {"lblPledgeAmount","Pledge Amount"},
        {"lblVillage","Village"},
        {"lblSurveryNo","Survery No"},
        {"lblTotalArea","Total Area(In Cents)"},
        {"lblNature","Nature"},
        {"lblRight","Right"},
        {"lblGahanExpDt","Gahan Expiry Date"},
        {"lblGahanReleasedDate","Gahan Released"},
        {"lblGahanReleasedExpiryDate","Gahan Released Date"},
        {"tblColumnBorrowerName", "Name"},
        {"lblHouseName","House Name"},
        {"lblPlace","Place"},
        {"sno","SNo"},
        {"lblCity","City"},
        {"lblARS","ARS"},
        {"lbkPinCode","Pin Code"},
        {"tblColumnBorrowerCustID", "Customer ID"},
        {"tblColumnBorrowerType", "Type"},
        {"tblColumnBorrowerMain/Joint", "Main/Joint"},
        {"lblRemarks","Remarks"},
        {"tblCollumnLoanAccoountNo","Loan A/C No"},
        {"tblCollumnProductId","Product Id"},
        {"tblCollumnCustomerName","Customer Name"},
        {"tblCollumnLoanAmount","Loan Amount"},
        {"tblCollumnExpiryDate","Expiry Date"},
        {"tblCollumnOutStandingBalance","Out Standing Balance"},
        {"lblAvailSecurityValue","Available Security Value"},
         {"lblMemberType","Member Type"},
         {"lblConstitution","Constitution"},
         {"tblCollumnPledgeAmount","Pledge Amount"},
         {"slNo","Sl No"},
          {"lblResurvey","Resurvey No"},
          {"lblOwnerNo","Owner No"},
          {"lblOwnerNo2","Owner No2"}
         
//         {"documentNo","Documnet No"},
//         {"documentType","Document Type"},
//         {"documentDate","Documnet Date"},
//         {"registeredOffice","Registered Office"}
        
        
    };
    
    
    
    protected Object[][] getContents() {
        return content;
    }
    
}

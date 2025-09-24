/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GahanCustomerMRB.java
 *
 * Created on April 11, 2012, 4:43 PM
 */

package com.see.truetransact.ui.customer.gahan;
import java.util.ListResourceBundle;

/**
 *
 * @author  admin
 */
public class GahanCustomerMRB extends ListResourceBundle{
    
    /** Creates a new instance of GhanCustomerRB */
    public GahanCustomerMRB() {
        
        
    }
    
    
     static String content[][] ={
        {"txtDocumentNo", "Enter the Document Number"},
        {"txtDocumentType", "Enter the Document Type"},
        {"tdtDocumentDt", "Choose the Document Date"},
        {"txtRegisteredOffice", "Enter the Registered Office"},
        {"tdtPledgeDate", "Choose the Pledge Date"},
        {"txtPledgeNo", "Enter the  Pledge No"},
        {"txtPledgeAmount", "Enter the Pledge Amount"},
        {"cboVillage", "Enter the Village"},
        {"txtSurveryNo", "Enter the Survey Number"},
        {"txtTotalArea", "Enter the Total Area"},
        {"txtRemarks", "Enter the Remarks "},
        {"tdtGahanExpDt","Enter the ExpiryDate"}
    };
    
    
    
    protected Object[][] getContents() {
         return content;
    }
    
}

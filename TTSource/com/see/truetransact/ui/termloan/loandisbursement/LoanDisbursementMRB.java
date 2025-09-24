/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanDisbursementMRB.java
 *
 * Created on May 13, 2009, 5:28 PM
 */

package com.see.truetransact.ui.termloan.loandisbursement;
import java.util.ListResourceBundle;
/**
 *
 * @author  Administrator
 */
public class LoanDisbursementMRB extends ListResourceBundle{
    
    /** Creates a new instance of LoanDisbursementMRB */
    public LoanDisbursementMRB() {
    }
    
    protected Object[][] getContents() {
        return  contents;
    }
     static final String[][] contents = {
          {"txtSubLimitAmt", "Enter Limit Amount"},
          {"tdtSubLimitFromDt", "Enter Limit FromDate"},
          {"tdtSubLimitToDt", "Enter Limit ToDate"},
          {"cboPurpose", "Select Purpose"},
          {"cboType", "Enter Type"},
          {"txtHectare", "Choose Hectare Details"},
          {"txtSurveyNo", "Enter Survey No"}

     };
}

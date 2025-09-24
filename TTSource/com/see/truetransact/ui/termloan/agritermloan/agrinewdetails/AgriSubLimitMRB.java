/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AgriSubLimitMRB.java
 *
 * Created on May 13, 2009, 5:28 PM
 */

package com.see.truetransact.ui.termloan.agritermloan.agrinewdetails;
import java.util.ListResourceBundle;
/**
 *
 * @author  Administrator
 */
public class AgriSubLimitMRB extends ListResourceBundle{
    
    /** Creates a new instance of AgriSubLimitMRB */
    public AgriSubLimitMRB() {
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

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.supporting.depositperiodwisesetting;

import java.util.ListResourceBundle;

public class DepositPeriodwiseSettingMRB extends ListResourceBundle {
    public DepositPeriodwiseSettingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
       
        {"txtPeriodName", "Period Name should be a proper value!!!"},
        {"txtPeriodFrom", "Period From should be a proper value!!!"},
        {"txtPeriodTo", "Period To should be a proper value!!!"},
        {"txtPriority", "Priority should be a proper value!!!"},
        
        
        {"txtamountrange", "Amount range should be a proper value!!!"},
        {"txtfromamount", "From Amount should be a proper value!!!"},
        {"txttoamount", "To Amount  should be a proper value!!!"},
        
         {"txtamountrange1", "Amount range should be a proper value!!!"},
        {"txtfromamount1", "From Amount should be a proper value!!!"},
        {"txttoamount1", "To Amount  should be a proper value!!!"},
        
        {"txtPriority2", "Priority  should be a proper value!!!"},
        {"txtdesc", "Description  should be a proper value!!!"},
         {"txtPeriodFrom2", "Period should be a proper value!!!"},
         {"txtPeriodTo2", "Period should be a proper value!!!"},
         
         {"txtdoubtfrom", "Personal Doubtfull from  should be a proper value!!!"},
         {"txtdoubtto", "Personal Doubtfull to should be a proper value!!!"},
         {"txtbadfrom", "Personal Bad from should be a proper value!!!"},
         {"txtbadto", "Personal Bad to should be a proper value!!!"},
         {"txtdocdoubtfrom", "Document Doubtfull from should be a proper value!!!"},
         {"txtdocdoubtto", "Document Doubtfull to should be a proper value!!!"},
         {"txtdocbadfrom", "Document Bad from  should be a proper value!!!"},
         {"txtdocbadto", "Document Bad to should be a proper value!!!"},
         {"txtdoubtnarra", "Personal Doubtfull narration should be a proper value!!!"},
         {"txtbadnarra", "Personal Bad narration   should be a proper value!!!"},
         {"txtdocdoubtnara", "Document Doubtfull narration should be a proper value!!!"},
         {"txtdocbadnara", "Document Bad narration should be a proper value!!!"},
         {"cboPeriodType", "should be a proper value!!!"}
    
    };
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigRB.java
 *
 * Created on Thu Jan 20 15:41:51 IST 2005
 */

package com.see.truetransact.ui.termloan.personalSuretyConfiguration;

import java.util.ListResourceBundle;

public class PersonalSuretyConfigurationRB extends ListResourceBundle {
    public PersonalSuretyConfigurationRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
          {"lblDate", "Date Of Meeting"},
        {"lblVenu", "Meeting Venue"},
        {"lblTotalAttendance", "Total Number of participents"},
        {"lblRemarks", "Remarks"},
                
    };
    
}

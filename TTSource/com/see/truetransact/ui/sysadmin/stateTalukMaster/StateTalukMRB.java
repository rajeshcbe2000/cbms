/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * stateTalukMRB.java
 * 
 * Created on 19-05-2009 IST 
 */

package com.see.truetransact.ui.sysadmin.stateTalukMaster;

import java.util.ListResourceBundle;

public class StateTalukMRB extends ListResourceBundle {
    public StateTalukMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtStateName", "State Name should not be empty!!!"},
        {"txtStateCode", "State Code should not be empty!!!"},
        {"txtTalukName", "Taluk Name should not be empty!!!"},
        {"txtTalukCode", "Taluk Code should not be empty!!!"},
        {"txtDistrictName", "District Name should not be empty!!!"},
        {"txtDistrictCode", " District Code should not be empty!!!"}

   };

}

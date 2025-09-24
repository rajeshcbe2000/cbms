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

package com.see.truetransact.ui.sysadmin.emptraining;

import java.util.ListResourceBundle;

public class EmpTrainingMRB extends ListResourceBundle {
    public EmpTrainingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboTrainDest", "Training Destination should be a proper value!!!"},
        {"txtLocation", "Location should be a proper value!!!"},
        {"txtCondTeam", "Conducting Team  should be a proper value!!!"},
        {"txtSize", "Team Size should be a proper value!!!"},
        {"txtNoOfTrainees", "No Of Trainees should be a proper value!!!"},
        {"tdtFrom", "From Date should not be empty!!!"},
        {"tdtTo", "To Date  should not be empty!!!"}
        
   };

}

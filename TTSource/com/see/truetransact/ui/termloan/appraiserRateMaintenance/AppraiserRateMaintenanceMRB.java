/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved..
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BranchGroupMRB.java
 * 
 * Created on Thu Aug 25 17:06:59 IST 2005
 */

package com.see.truetransact.ui.termloan.appraiserRateMaintenance;

import java.util.ListResourceBundle;

public class AppraiserRateMaintenanceMRB extends ListResourceBundle {
    public AppraiserRateMaintenanceMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtGroupId", "GroupId should not be empty!!!"},
        {"txtGroupName", "GroupName should not be empty!!!"},
        {"lstAvailBranch", "AvailGL should be a proper value!!!"},
        {"lstGrantedBranch", "GrantGL should be a proper value!!!"} 

   };

}

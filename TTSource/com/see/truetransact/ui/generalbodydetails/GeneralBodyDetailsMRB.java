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

package com.see.truetransact.ui.generalbodydetails;

import java.util.ListResourceBundle;

public class GeneralBodyDetailsMRB extends ListResourceBundle {
    public GeneralBodyDetailsMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtDate", "Date should be a proper value!!!"},
        {"txtVenu", "Venue should be a proper value!!!"},
        {"txtTotalAttandance", "TotalAttendance  should be a proper value!!!"}
        
           };

}

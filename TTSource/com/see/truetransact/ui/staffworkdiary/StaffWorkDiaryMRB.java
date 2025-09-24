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

package com.see.truetransact.ui.staffworkdiary;

import java.util.ListResourceBundle;

public class StaffWorkDiaryMRB extends ListResourceBundle {
    public StaffWorkDiaryMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtDate", "Date should be a proper value!!!"},
        {"txtStaffID", "Staff ID should be a proper value!!!"},
        {"txtLoginout", "Login/Logout Details  should be a proper value!!!"},
        {"txtTransSummry", "Transaction Summary  should be a proper value!!!"}
        
           };

}

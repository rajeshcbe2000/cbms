/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.customer;
import java.util.ListResourceBundle;
public class CheckCustomerIdRB extends ListResourceBundle {
    public CheckCustomerIdRB(){
    }
    public Object[][] getContents() {
        return contents;
    }
    static final String[][] contents = {
        {"screenTitle", "Check Customer_ID Detail"},
        {"lblCustomerName", "Customer Name"},
        {"lblCareOfName", "Care Of Name"},
        {"lblUniqueIdNo","Aadhaar No"},
        {"lblUniqueId","Unique Id"},
        {"lblEmployeeNo","Employee No"},
        {"lblPanNO","PAN No"},
        {"lblPassPortNo","Pass Port No"},
        {"lblDtOfBirth","Date Of Birth"},
        {"btnSearch","Search"},
        {"btnClose","Close"},
        {"btnClear","Clear"}
    };
}

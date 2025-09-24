/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigMRB.java
 * 
 * Created on Thu Jan 20 16:39:25 IST 2005
 */

package com.see.truetransact.ui.locker.lockerconfig;

import java.util.ListResourceBundle;

public class LockerConfigMRB extends ListResourceBundle {
    public LockerConfigMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtProdId", "ProdId should be a proper value!!!"},
        {"txtFromLocNo", "FromLocNo should be a proper value!!!"},
        {"txtToLocNo", "ToLocNo should not be empty!!!"},
        {"txtNoOfLockers", "NoOfLockers should not be empty!!!"},
        {"txtMasterKey", "MasterKey should not be empty!!!"} ,
        {"txtLockerKey","LockerKey Token Number Should Be Less Than Ending Token Number"}

   };

}

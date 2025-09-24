/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterMRB.java
 *
 * Created on Fri Aug 05 13:53:36 GMT+05:30 2011
 */

package com.see.truetransact.ui.share;

import java.util.ListResourceBundle;



public class DrfRecoveryMRB extends ListResourceBundle {
    public DrfRecoveryMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"cboDrfTransProdID","Product ID cannot be empty!!\n"},
        {"txtDrfTransMemberNo", "Member no should not be empty!!\n"},
        {"txtDrfTransName","Name should not be empty!!\n"},
        {"lblDrfTransAddressCont","Address should not be empty!!\n"},
        {"txtDrfTransAmount","Amount should not be empty!!\n"},
        {"tblDrfTransaction","Please select atleast one record from table"}
    };
    
}

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

package com.see.truetransact.ui.roomrent;

import java.util.ListResourceBundle;

public class RentMRB extends ListResourceBundle {
    public RentMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtBuildingNo", "Building Number should not be empty!!!\n"},
        {"txtaBuildingDes", "Building Description should not be empty!!!\n"},
        {"txtRentAccHead", "Rent Account head should not be empty!!!\n"},
        {"txtPenelAccHead", "Penel Account head should be a proper value!!!\n"},
        {"txtNoticeAccHead", "Notice Account head should be a proper value!!!\n"},
        {"txtLegalAccHead", "Legal Account head should be a proper value!!!\n"},
        {"txtArbAccHead", "Arbitration Account head should be a proper value!!!\n"},
        {"txtCourtGrpHead", "Court Account head should be a proper value!!!\n"},
        {"txtExeGrpHead", "Execution Account head should be a proper value!!!\n"},
        {"cboStatus", "Status should be a proper value!!!\n"},
        {"splCharsCheck", "Special Characters are not allowed in building number!!!\n"},
        {"txtAdvHead", "Advance Account head should be a proper value!!!\n"}
   };

}

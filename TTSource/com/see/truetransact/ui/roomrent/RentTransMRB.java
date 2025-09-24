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

public class RentTransMRB extends ListResourceBundle {
    public RentTransMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboBuildingNo", "Building No should not be empty!!!\n"},
        {"cboRoomNo", "Room Number should not be empty!!!\n"},
        {"tdtRentDate", "Rent date should not be empty!!!\n"},
        {"txtRentAmt", "Rent Amount should not be empty!!!\n"} ,
        {"txtPenelAmt", "Penel amount should not be empty!!!\n"} ,
        {"txtNoticeAmt", "Notice amount should not be empty!!!\n"} ,
        {"txtLegalAmt", "Legal amount should not be empty!!!\n"} ,
        {"txtArbAmt", "Arbitary amount should not be empty!!!\n"} ,
        {"txtCourtAmt", "Court amount should not be empty!!!\n"} ,
        {"txtExeAmt", "Execution amount should not be empty!!!"} ,
   };

}

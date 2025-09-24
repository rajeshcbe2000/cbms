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

public class RentRegisterMRB extends ListResourceBundle {
    public RentRegisterMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboBuildingNo", "Building No should not be empty!!!\n"},
        {"txtBuildingNo", "Building No should not be empty!!!\n"},
        {"txtRoomNo", "Room Number should not be empty!!!\n"},
        {"txtAppNo", "Application No should not be empty!!!\n"},
        {"tdtApplnDate", "Application Date should be a proper value!!!\n"},
        {"txtName", "Name should not be empty!!!\n"} ,
        {"txtHouseName","House Name should not be empty!!!\n"},
         {"txtPlace", "Place should not be empty!!!\n"} ,
        {"txtCity", "City should not be empty!!!\n"} ,
        {"txtPhNo", "Phone Number should not be empty!!!\n"} ,
        {"txtMobNo", "Mobile Number should not be empty!!!\n"} ,
        {"txtEmailId", "Email ID should not be empty!!!\n"} ,
        {"txtGuardian", "Guardian should not be empty!!!\n"} ,
        {"txtNominee", "Nominee should not be empty!!!\n"} ,
        {"tdtOccDate", "Occupied date should not be empty!!!\n"} ,
        {"tdtCommDate", "Committee date should not be empty!!!\n"} ,
        {"txtRecommBy", "Recommended by should not be empty!!!\n"} ,
        {"txtAgreNo", "Agreement No should not be empty!!!\n"} ,
        {"tdtAgrDate", "Agreement date should not be empty!!!\n"} ,
        {"txtRentAmt", "Rent Amount should not be empty!!!\n"} ,
        {"cboRentDate", "Rent Date should not be empty!!!\n"} ,
        {"cboPenalGrPeriod", "Penel Grace period should not be empty!!\n!"} ,
        {"txtAdvAmt", "Advance Amount should not be empty!!!\n"} ,
        {"txtaAdvDetails", "Advance Account details should not be empty!!!\n"} ,
        {"txtPhNoNum", "Phone Number allowed Numbers only!!!\n"} ,
        {"txtMobNoNum", "Mobile Number allowed Numbers only!!!\n"},
        {"validEmail", "Enter valid email address!!!"} ,
        {"splCharsWCheck1", "Special Characters are not allowed in Application No!!!\n"},
        {"splCharsWCheck2", "Special Characters are not allowed in Name!!!\n"},
        {"splCharsWCheck3", "Special Characters are not allowed in House Name!!!\n"},
        {"splCharsWCheck4", "Special Characters are not allowed in Place!!!\n"},
        {"splCharsWCheck5", "Special Characters are not allowed in City!!!\n"}
   
   };
}

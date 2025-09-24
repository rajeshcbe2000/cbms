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

package com.see.truetransact.ui.borrowings;

import java.util.ListResourceBundle;

public class NewBorrowingMRB extends ListResourceBundle {
    public NewBorrowingMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboAgency", "Agency should be a proper value!!!\n"},
         {"txtAmount", "Amount/Disbursal amount should not be empty!!!\n"},
        {"txtBorrowingRefNo", "BorrowingRefNo should not be empty!!!\n"},
        {"txtBorrowingNo", "BorrowingNo should not be empty!!!\n"},
        {"cboType", "Type should be a proper value!!!\n"},
        {"txtaDescription", "Description should not be empty!!!\n"} ,
        {"tdtDateSanctioned","Date Sanctioned should not be empty!!!\n"},
        {"txtAmtSanctioned","Amount Sanctioned should not be empty!!!\n"},
        {"txtAmtBorrowed","Amount Borrowed should not be empty!!!\n"},
        {"txtRateInterest","Rate of Interest should not be empty!!!\n"},
        {"txtnoofInstall","No of installments should not be empty!!!\n"},
        {"cboPrinRepFrq","Principal Repayment Freqency should be a proper value!!!\n"},
        {"cboIntRepFrq","Interest Repayment Freqency should be a proper value!!!\n"},
        {"txtMorotorium","Morotorium should not be empty!!!\n"},
        {"txaSecurityDet","Security Details should not be empty!!!\n"},
        {"txtprinGrpHead","{Principal Group Head should not be empty!!!\n"},
        {"txtintGrpHead","{Interest Group Head should not be empty!!!\n"},
        {"txtpenalGrpHead","{Penal Group Head should not be empty!!!\n"},
        {"txtchargeGrpHead","{Charges Group Head should not be empty!!!\n"},
        {"txtAmtSanctionedNo","{Amount Sanctioned allowed Numbers only!!!\n"},
        {"txtAmtBorrowedNo","{Amount Borrowed allowed Numbers only!!!\n"},
        {"txtRateInterestNo","{Rate Interest allowed Numbers only!!!\n"},
        {"txtnoofInstallNo","{No of installments allowed Numbers only!!!\n"}
                 
   };

}

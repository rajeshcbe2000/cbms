/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.investments;

import java.util.ListResourceBundle;

public class CallMoneyMRB extends ListResourceBundle {
    public CallMoneyMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboCallMoneyInstituation", "CallMoney Instituation should be a proper value!!!"},
        {"cboCommunication", "Communication Mode should be a proper value!!!"},
        {"tdtCallMoneyDate", "Call Money Date should be a proper value!!!"},
        {"txtNoOfDays", "No Of Days should be a proper value!!!"},
        {"txtInterestRate", "Interest Rate should be a proper value!!!"},
        {"txtCallMoneyAmount", "CallMoney Amount should be a proper value!!!"},
        {"txtParticulars", " Particulars should be a proper value!!!"},
        {"txtInterestAmount", "Interest Amount should be a proper value!!!"},
        {"txtNoOfDaysExtension", "No Of DaysExtension should be a proper value!!!"},
        {"txtExtensionInterestRate", "Extension Interest Rate should be a proper value!!!"}
   };
}

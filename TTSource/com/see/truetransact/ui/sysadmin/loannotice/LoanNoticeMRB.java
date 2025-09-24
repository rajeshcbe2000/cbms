/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LoanNoticeMRB.java
 */
package com.see.truetransact.ui.sysadmin.loannotice;
import java.util.ListResourceBundle;
public class LoanNoticeMRB extends ListResourceBundle {
    public LoanNoticeMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboNoticeType", "Notice Type should not be empty!!!"},
        {"cboProdType", "Product Type should not be empty!!!"},
        {"cboProdId", "Product ID should not be empty!!!"} ,
        {"tdtDayEndDt", "Day End Date should not be empty!!!"} ,
        {"tdtFromInstDate", "From Installment Date should not be empty!!!"}, 
        {"tdtToInstDate", "To Installment Date should not be empty!!!"} 

   };

}

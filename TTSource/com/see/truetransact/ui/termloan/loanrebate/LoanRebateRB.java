/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * AuthorizeRB.java
 *
 * Created on December 18, 2003, 1:12 PM
 */

package com.see.truetransact.ui.termloan.loanrebate;

/**
 *
 * @author  Bala
 */
public class LoanRebateRB extends java.util.ListResourceBundle {

    /** Creates a new instance of ViewAllRB */
    public LoanRebateRB() {
    }

    protected Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblProdType","Product Type"},
        {"lblProdId","Product Id"},
        {"lblFromAccount","From Account"},
        {"lblToAccount","To Account"},
        {"lblRebateDate","RebateUpto"},
        {"btnProcess","Search"},
       
    };
}

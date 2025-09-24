/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * RecoveryParametersRB.java
 */

package com.see.truetransact.ui.salaryrecovery;
import java.util.ListResourceBundle;
public class RecoveryParametersRB extends ListResourceBundle {
    public RecoveryParametersRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"lblFirstDay","In the Current month Recovery List include Loans Sanctioned between  1st   and  "},
        {"lblFromDate","day of the month "},
        {"lblLastDay","In the Next Month Recovery List include Loans  Sanctioned  between  "},
        {"lblToDate","and   last day of the month "},
        {"btnSave","Save"}
    };
    
}

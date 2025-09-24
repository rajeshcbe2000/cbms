/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.courtexpensesetting;
import java.util.ListResourceBundle;
public class CourtExpenseSettingRB extends ListResourceBundle {
    public CourtExpenseSettingRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblFromAmt", "lblFromAmt"},
        {"lblToAmt", "lblToAmt"},
        {"lblPercentage", "lblPercentage"}
     
    };
}

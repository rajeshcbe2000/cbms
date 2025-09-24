/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigRB.java
 * 
 * Created on Thu Jan 20 15:41:51 IST 2005
 */

package com.see.truetransact.ui.batchprocess.yearendprocess;

import java.util.ListResourceBundle;

public class YearEndProcessRB extends ListResourceBundle {
    public YearEndProcessRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblProfitAcHead", "Profit/Loss A/c Head"},
        {"lblLossAcHead", "Loss A/c Head"},
        {"lblProfitLoss", "Profit / Loss"},
        {"btnProcess", "Process"},
        {"lblIncomeHeads", "Inc Heads"},
        {"lblIncomeHeadDescription", "Income Head Description"},
        {"lblIncomeBalance", "Balance"},
        {"lblExpenditureHeads", "Exp Heads"},
        {"lblExpenditureHeadDescription", "Expenditure Head Description"},
        {"lblExpenditureBalance", "Balance"},

        {"lblMsg", ""},
        {"lblStatus", "                      "},
        {"lblSpace1", " Status :"}
   };

}

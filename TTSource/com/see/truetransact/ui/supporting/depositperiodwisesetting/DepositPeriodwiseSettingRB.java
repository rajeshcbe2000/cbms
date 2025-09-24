/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.supporting.depositperiodwisesetting;
import java.util.ListResourceBundle;
public class DepositPeriodwiseSettingRB extends ListResourceBundle {
    public DepositPeriodwiseSettingRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblPeriodName", "Period Name"},
        {"lblPeriodType", "Period Type"},
        {"lblPeriodFrom", "Period From"},
        {"lblPeriodTo", "Period To"},
        {"lblPriority", "Priority"},
         {"lblamountrange", "Amount Range"},
         {"lblfromamount", "From Amount"},
          {"lbltoamount", "To Amount"},
          {"lblpriority", "Priority"},
          
          {"lblamountrange1", "Amount Range"},
         {"lblfromamount1", "From Amount"},
          {"lbltoamount1", "To Amount"},
          {"lblpriority1", "Priority"},
          
          {"lblPriority2","Priority"},
          {"lbldesc","Description"},
          {"lblPeriodType3","From Period Type"},
          {"lblPeriodFrom2","Period From"},
          {"lblPeriodType4","To Period Type"},
          {"lblPeriodTo2","Period To"},
          
           {"cLabel1","Personal Doubtfull From"},
            {"cLabel2","Personal Doubtfull To"},
             {"cLabel3","Personal Bad From"},
              {"cLabel4","Personal Bad To "},
               {"cLabel5","Document Doubtfull From"},
                {"cLabel6","Document Doubtfull To"},
                 {"cLabel7","Document Bad From"},
                  {"cLabel8","Document Bad To"},
                   {"cLabel9","Personal Doubtfull Narration"},
                    {"cLabel10","Personal Bad Narration"},
                     {"cLabel11","Document Doubtfull Narration"},
                      {"cLabel12","Document Bad Narration"}
               
    };
}

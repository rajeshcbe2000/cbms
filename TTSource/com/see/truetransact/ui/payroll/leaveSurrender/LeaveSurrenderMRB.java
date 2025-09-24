/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * Author   : Chithra
 * Location : Thrissur
 * Date of Completion : 1-08-2015
 */
package com.see.truetransact.ui.payroll.leaveSurrender;

import com.see.truetransact.ui.directorboardmeeting.*;
import java.util.ListResourceBundle;

public class LeaveSurrenderMRB extends ListResourceBundle {
    public LeaveSurrenderMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"cboBoardMember", "Board Member should be filled!!!\n"},
        {"tdtMeetingDate", "Date of Meeting should be filled!!!\n"},
        {"txtSittingFeeAmount", "Sitting Fee Amount should be filled!!!\n"},
        {"tdtPaidDate", "Paid Date should be filled!!!\n"}
      
        
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductMRB.java
 * 
 * Created on Mon Apr 11 12:14:57 IST 2005
 */

package com.see.truetransact.ui.kolefieldsoperations;

import com.see.truetransact.ui.directorboardmeeting.*;
import java.util.ListResourceBundle;

public class KoleFieldsExpensesMRB extends ListResourceBundle {
    public KoleFieldsExpensesMRB(){
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

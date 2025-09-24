/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.directorboardmeeting;
import java.util.ListResourceBundle;
public class DirectorBoardRB extends ListResourceBundle {
    public DirectorBoardRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblBoardMember", "Board Member"},
        {"lblMeetingDate", "Meeting Date"},
        {"lblSittingFeeAmount", "Sitting Fee Amount"},
        {"lblPaidDate", "Paid Date"}
      //  {"rdoApplType", "Appl Type"},
       // {"rdoApplType1", "Appl Type1"}

    };
}

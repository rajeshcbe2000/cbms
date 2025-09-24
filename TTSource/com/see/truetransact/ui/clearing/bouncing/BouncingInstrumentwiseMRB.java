/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BouncingInstrumentwiseMRB.java
 * 
 * Created on Wed Apr 07 12:38:12 GMT+05:30 2004
 */

package com.see.truetransact.ui.clearing.bouncing;

import java.util.ListResourceBundle;

public class BouncingInstrumentwiseMRB extends ListResourceBundle {
    public BouncingInstrumentwiseMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtInwardScheduleNo", "InwardScheduleNo should not be empty!!!"},
//        {"cboReasonforBouncing", "ReasonforBouncing should be a proper value!!!"},
        {"txtReasonforBouncing", "ReasonforBouncing should not be empty!!!"},
        {"dateClearingDate", "ClearingDate should not be empty!!!"},
        {"chkPresentAgain", "PresentAgain should be selected!!!"},
        {"cboBouncingType", "BouncingType should be a proper value!!!"},
        {"txtClearingSerialNo", "ClearingSerialNo should not be empty!!!"},
        {"cboClearingType", "ClearingType should be a proper value!!!"}
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemitIssueCancellationMRB.java
 * 
 * Created on Thu Jun 10 16:54:03 PDT 2004
 */

package com.see.truetransact.ui.remittance.cancellation;

import java.util.ListResourceBundle;

public class RemitIssueCancellationMRB extends ListResourceBundle {
    public RemitIssueCancellationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtCancellationCharge", "CancellationCharge should not be empty!!!"},
        {"txtCancellationRemarks", "CancellationRemarks should not be empty!!!"} 

   };

}

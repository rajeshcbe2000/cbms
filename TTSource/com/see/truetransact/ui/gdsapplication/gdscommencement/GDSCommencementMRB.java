/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * RemittanceProductMRB.java
 *
 * Created on Thu Jun 24 18:03:43 GMT+05:30 2004
 */

package com.see.truetransact.ui.gdsapplication.gdscommencement;

import com.see.truetransact.ui.mdsapplication.mdsconmmencement.*;
import java.util.ListResourceBundle;

public class GDSCommencementMRB extends ListResourceBundle {
    public GDSCommencementMRB(){
    }
    
    public Object[][] getContents() {
        return contents;
    }
    
    static final String[][] contents = {
        {"tdtStartDt", "StartDt should not be empty!!!"},
        {"txtInstAmt", "InstallmentAmount should not be empty!!!"},
        {"txtSchemeName", "SchemeName should not be empty!!!"},
        {"txtTotAmt", "TotalAmount should not be empty!!!"}
    };
}

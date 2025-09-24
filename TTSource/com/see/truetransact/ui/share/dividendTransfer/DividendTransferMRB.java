/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareMRB.java
 * 
 * Created on Mon Dec 27 14:17:36 IST 2004
 */

package com.see.truetransact.ui.share.dividendTransfer;
import com.see.truetransact.ui.share.shareresolution.*;

import java.util.ListResourceBundle;

public class DividendTransferMRB extends ListResourceBundle {
    public DividendTransferMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtResolutionNo", "Resolution No. should not be empty!!!"},
        {"tdtResolutionDt", "Resolution Date should not be empty!!!"}
   };

}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSLetterGenerationRB.java
 * 
 * Created on Wed Jun 08 17:18:22 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdslettergeneration;

import java.util.ListResourceBundle;

public class MDSLetterGenerationRB extends ListResourceBundle {
    public MDSLetterGenerationRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"lblLettorNo", "Letter No"},
        {"lblSchemeName", "MDS Scheme Name"},
        {"lblChittalNo", "Chittal No"},
        {"lblAuctionAmount", "Auction Amount"},
        {"lblFromDt", "From Date"},
        {"lblToDt", "To Date"}
   };

}

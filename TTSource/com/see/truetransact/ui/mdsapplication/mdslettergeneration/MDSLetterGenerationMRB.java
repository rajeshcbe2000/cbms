/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyDetailsEntryMRB.java
 * 
 * Created on Wed Jun 08 17:29:46 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdslettergeneration;

import java.util.ListResourceBundle;

public class MDSLetterGenerationMRB extends ListResourceBundle {
    public MDSLetterGenerationMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtSchemeName", "SchemeName should not be empty!!!"},
        {"txtChittalNo", "ChittalNo should not be empty!!!"},
        {"txtAuctionAmount", "AuctionAmount should not be empty!!!"},
        {"tdtFromDt", "FromDt should not be empty!!!"},
        {"tdtToDt", "ToDt should not be empty!!!"},
   };

}

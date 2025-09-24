/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigMRB.java
 * 
 * Created on Thu Jan 20 16:39:25 IST 2005
 */

package com.see.truetransact.ui.transaction.token.tokenissue;

import java.util.ListResourceBundle;

public class TokenIssueMRB extends ListResourceBundle {
    public TokenIssueMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"txtNoOfTokens", "Enter the Number of Tokens Configured"},
        {"cboTokenType", "TokenType should be a proper value!!!"},
        {"cboSeriesNo", "SeriesNo should not be empty!!!"},
        {"txtEndingTokenNo", "EndingTokenNo should not be empty!!!"},
        {"txtStartingTokenNo", "StartingTokenNo should not be empty!!!"},
        {"txtReceiverId", "Enter the Receiver Id"}

   };

}

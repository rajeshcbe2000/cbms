/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenLossMRB.java
 * 
 * Created on Tue Jan 25 16:54:21 IST 2005
 */

package com.see.truetransact.ui.transaction.token.tokenloss;

import java.util.ListResourceBundle;

public class TokenLossMRB extends ListResourceBundle {
    public TokenLossMRB(){
    }

    public Object[][] getContents() {
        return contents;
    }

    static final String[][] contents = {
        {"tdtDateOfLoss", "DateOfLoss should not be empty!!!"},
        {"cboTokenType", "TokenType should be a proper value!!!"},
        {"txtRemarks", "Remarks should not be empty!!!"},
        {"cboSeriesNo", "SeriesNo should not be empty!!!"},
        {"chkTokenRecovered", "TokenRecovered should be selected!!!"},
        {"tdtRecoveredDate", "RecoveredDate should not be empty!!!"},
        {"txtTokenLossId", "TokenLoss Id should not be empty!!!"},
        {"txtTokenNo", "TokenNumber should not be empty!!!"},
   };

}

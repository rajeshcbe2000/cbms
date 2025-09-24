/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenLossHashMap.java
 * 
 * Created on Tue Jan 25 16:53:30 IST 2005
 */

package com.see.truetransact.ui.transaction.token.tokenloss;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class TokenLossHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public TokenLossHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTokenLossId", new Boolean(false));
        mandatoryMap.put("txtTokenNo", new Boolean(true));
        mandatoryMap.put("cboTokenType", new Boolean(true));
        mandatoryMap.put("tdtDateOfLoss", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboSeriesNo", new Boolean(true));
        mandatoryMap.put("chkTokenRecovered", new Boolean(false));
        mandatoryMap.put("tdtRecoveredDate", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

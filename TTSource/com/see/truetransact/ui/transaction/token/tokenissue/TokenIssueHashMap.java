/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TokenConfigHashMap.java
 *
 * Created on Thu Jan 20 16:38:23 IST 2005
 */

package com.see.truetransact.ui.transaction.token.tokenissue;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class TokenIssueHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public TokenIssueHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNoOfTokens", new Boolean(true));
        mandatoryMap.put("cboTokenType", new Boolean(true));
        mandatoryMap.put("cboSeriesNo", new Boolean(false));
        mandatoryMap.put("txtEndingTokenNo", new Boolean(true));
        mandatoryMap.put("txtStartingTokenNo", new Boolean(true));
        mandatoryMap.put("txtReceiverId", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyDetailsEntryHashMap.java
 * 
 * Created on Wed Jun 08 17:29:28 IST 2011
 */

package com.see.truetransact.ui.mdsapplication.mdslettergeneration;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class MDSLetterGenerationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public MDSLetterGenerationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtAuctionAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDt", new Boolean(true));
        mandatoryMap.put("tdtToDt", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

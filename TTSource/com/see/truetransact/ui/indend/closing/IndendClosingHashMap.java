/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.indend.closing;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class IndendClosingHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public IndendClosingHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDepoID", new Boolean(true));
        mandatoryMap.put("tdtClosingDt", new Boolean(true));
        mandatoryMap.put("txtClosingAmount", new Boolean(true));
        mandatoryMap.put("txtClosingPerLessAmt", new Boolean(true));
        mandatoryMap.put("cboStockType", new Boolean(true));
        mandatoryMap.put("cboClosingStockType", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

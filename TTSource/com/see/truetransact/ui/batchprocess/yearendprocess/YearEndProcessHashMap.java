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

package com.see.truetransact.ui.batchprocess.yearendprocess;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class YearEndProcessHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public YearEndProcessHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProfitAcHead", new Boolean(true));
        mandatoryMap.put("txtLossAcHead", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

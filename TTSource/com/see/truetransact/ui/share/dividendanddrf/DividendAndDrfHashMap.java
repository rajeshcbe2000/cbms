/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DeathReliefMasterHashMap.java
 * 
 * Created on Fri Aug 05 13:51:33 GMT+05:30 2011
 */

package com.see.truetransact.ui.share.dividendanddrf;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DividendAndDrfHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public DividendAndDrfHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDrfTransMemberNo", new Boolean(true));
        mandatoryMap.put("txtDrfTransName", new Boolean(false));
        mandatoryMap.put("txtDrfTransAmount", new Boolean(true));
        mandatoryMap.put("cboDrfTransProdID",new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

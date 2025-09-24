/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 *
 * LoanChargesHashMap.java
 * 
 * Created on Tue Aug 30 15:17:45 IST 2011
 */

package com.see.truetransact.ui.common.charges;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class LoanChargesHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public LoanChargesHashMap(){
        mandatoryMap = new HashMap();
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

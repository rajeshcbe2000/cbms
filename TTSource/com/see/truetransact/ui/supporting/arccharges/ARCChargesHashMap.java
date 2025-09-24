/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanChargesHashMap.java
 * 
 * Created on Tue Aug 30 15:17:45 IST 2011
 */

package com.see.truetransact.ui.supporting.arccharges;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ARCChargesHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ARCChargesHashMap(){
        mandatoryMap = new HashMap();
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

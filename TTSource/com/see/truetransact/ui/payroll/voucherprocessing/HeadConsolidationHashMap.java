/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * HeadConsolidationHashMap.java
 * 
 * Created on Wed Feb 02 12:56:47 IST 2015
 */
package com.see.truetransact.ui.payroll.voucherprocessing;


import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class HeadConsolidationHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public HeadConsolidationHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMapHead", new Boolean(true));
        mandatoryMap.put("cboHeads", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

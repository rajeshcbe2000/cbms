/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.transaction.chargesServiceTax;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class ChargesServiceTaxHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public ChargesServiceTaxHashMap(){
        mandatoryMap = new HashMap();        
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtAccountHead", new Boolean(true));
        mandatoryMap.put("txtChargeDetails", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

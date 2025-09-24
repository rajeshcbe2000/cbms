/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LienMarkingHashMap.java
 */

package com.see.truetransact.ui.operativeaccount;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LienMarkingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LienMarkingHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLienAmount", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboLienProduct", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("tdtLienDate", new Boolean(true));
        mandatoryMap.put("txtLienAccountNumber", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * SupplierHashMap.java
 */
package com.see.truetransact.ui.indend.suppliermaster;
//import com.see.truetransact.ui.indend.suppliermaster.*;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class SupplierHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public SupplierHashMap(){
        mandatoryMap = new HashMap();
       // mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtTinNo", new Boolean(true));
     //   mandatoryMap.put("txtActionTaken", new Boolean(true));
        mandatoryMap.put("txaAddress", new Boolean(true));
        // mandatoryMap.put("tdtLastDay", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

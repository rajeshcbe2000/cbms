/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * EditMigratedDataHashMap.java
 */
package com.see.truetransact.ui.transaction.editMigratedData;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class EditMigratedDataHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public EditMigratedDataHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustomerIdCr", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

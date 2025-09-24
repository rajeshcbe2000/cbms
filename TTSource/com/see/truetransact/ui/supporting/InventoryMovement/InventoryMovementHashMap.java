/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * InventoryMovementHashMap.java
 */
package com.see.truetransact.ui.supporting.InventoryMovement;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class InventoryMovementHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public InventoryMovementHashMap(){
        mandatoryMap = new HashMap();
       mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("rdoDDLeaf_Single", new Boolean(true));
        mandatoryMap.put("txtStartNo1", new Boolean(true));
        mandatoryMap.put("txtStartNo2", new Boolean(true));
        mandatoryMap.put("txtEndNo1", new Boolean(true));
        mandatoryMap.put("txtEndNo2", new Boolean(true));
        mandatoryMap.put("txtReason", new Boolean(true));
        mandatoryMap.put("cboReason", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

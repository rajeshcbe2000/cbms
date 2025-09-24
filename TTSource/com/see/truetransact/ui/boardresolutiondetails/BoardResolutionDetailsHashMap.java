/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * BoardResolutionDetailsHashMap.java
 *
 * Created on September 12, 2011, 10:33 AM
 */
package com.see.truetransact.ui.boardresolutiondetails;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class BoardResolutionDetailsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public BoardResolutionDetailsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txaDetails", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtReferenceNo", new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(false));
        mandatoryMap.put("txaRemarks", new Boolean(false));
    //    mandatoryMap.put("txtOutwardNo", new Boolean(false));
        //mandatoryMap.put("tdtLastDay", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

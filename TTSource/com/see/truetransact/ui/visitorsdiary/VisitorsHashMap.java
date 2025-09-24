/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * VisitorsHashMap.java
 */

package com.see.truetransact.ui.visitorsdiary;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class VisitorsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public VisitorsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txaNameAddress", new Boolean(true));
        mandatoryMap.put("txaPurposeofVisit", new Boolean(true));
        mandatoryMap.put("txaCommentsLeft", new Boolean(true));
        mandatoryMap.put("tdtDateofVisit", new Boolean(true));
        mandatoryMap.put("txaInstiNameAddress", new Boolean(true));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

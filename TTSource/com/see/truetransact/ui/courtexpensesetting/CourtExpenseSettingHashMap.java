/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CourtExpenseSettingHashMap.java
 */

package com.see.truetransact.ui.courtexpensesetting;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class CourtExpenseSettingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public CourtExpenseSettingHashMap(){
        mandatoryMap = new HashMap();
        
        mandatoryMap.put("txtfromamt", new Boolean(true));
         mandatoryMap.put("txttoamt", new Boolean(true));
          mandatoryMap.put("txtpercentage", new Boolean(true));
        

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

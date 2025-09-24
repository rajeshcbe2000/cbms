/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * StaffWorkDiaryHashMap.java
 */
package com.see.truetransact.ui.staffworkdiary;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class StaffWorkDiaryHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public StaffWorkDiaryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtStaffID", new Boolean(true));
        mandatoryMap.put("txtLoginout", new Boolean(true));
        mandatoryMap.put("txtTransSummry",new Boolean(true));
        mandatoryMap.put("txaRemarks", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

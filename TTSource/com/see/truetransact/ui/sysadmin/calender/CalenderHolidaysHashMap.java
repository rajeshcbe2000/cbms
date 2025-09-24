/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * CalenderHolidaysHashMap.java
 */

package com.see.truetransact.ui.sysadmin.calender;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class CalenderHolidaysHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public CalenderHolidaysHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("rdoWeeklyOff_Yes", new Boolean(true));
        mandatoryMap.put("cboWeeklyOff2", new Boolean(false));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("cboHalfDay2", new Boolean(false));
        mandatoryMap.put("txtHolidayName", new Boolean(false));
        mandatoryMap.put("txtYear", new Boolean(false));
        mandatoryMap.put("cboWeeklyOff1", new Boolean(false));
        mandatoryMap.put("cboMonth", new Boolean(false));
        mandatoryMap.put("cboHalfDay1", new Boolean(false));
        mandatoryMap.put("cboDate", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

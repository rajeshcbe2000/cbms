/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * NoticePeriodHashMap.java
 */

package com.see.truetransact.ui.sysadmin.noticereportparameters;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class NoticePeriodHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public NoticePeriodHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboGrDet", new Boolean(true));
        mandatoryMap.put("cboLan", new Boolean(true));
        mandatoryMap.put("txtReportName", new Boolean(true));
        mandatoryMap.put("jTextPaneData", new Boolean(true));
        mandatoryMap.put("txtRepotHeading", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

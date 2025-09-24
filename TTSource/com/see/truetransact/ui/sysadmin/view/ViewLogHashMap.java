/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ViewLogHashMap.java
 */

package com.see.truetransact.ui.sysadmin.view;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class ViewLogHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public ViewLogHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboActivity", new Boolean(true));
        mandatoryMap.put("dateFromDate", new Boolean(true));
        mandatoryMap.put("cboScreen", new Boolean(true));
        mandatoryMap.put("cboFindbyHistory", new Boolean(true));
        mandatoryMap.put("txtLatestEntries", new Boolean(true));
        mandatoryMap.put("cboIPAddress", new Boolean(true));
        mandatoryMap.put("cboUserID", new Boolean(true));
        mandatoryMap.put("cboBranchID", new Boolean(true));
        mandatoryMap.put("cboModule", new Boolean(true));
        mandatoryMap.put("dateToDate", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

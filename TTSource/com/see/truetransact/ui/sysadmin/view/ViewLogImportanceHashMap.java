/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ViewLogImportanceHashMap.java
 *
 * Created on January 7, 2005, 3:03 PM
 */

package com.see.truetransact.ui.sysadmin.view;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  152713
 */
public class ViewLogImportanceHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    /** Creates a new instance of ViewLogImportanceHashMap */
    public ViewLogImportanceHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboImportance", new Boolean(true));
        mandatoryMap.put("cboScreen", new Boolean(true));
        mandatoryMap.put("cboModule", new Boolean(true));
        mandatoryMap.put("cboActivity", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
    
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * GLLimitHashMap.java
 *
 * Created on Wed Jun 08 11:54:25 GMT+05:30 2005
 */

package com.see.truetransact.ui.rbi;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class GLLimitHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public GLLimitHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLimit", new Boolean(true));
        mandatoryMap.put("txtGLGroup", new Boolean(false));
        mandatoryMap.put("txtAccountHead", new Boolean(true));
        mandatoryMap.put("txtAnnualLimit", new Boolean(false));
        mandatoryMap.put("txtOverDraw", new Boolean(false));
        mandatoryMap.put("chkInterBranchTransAllowed", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

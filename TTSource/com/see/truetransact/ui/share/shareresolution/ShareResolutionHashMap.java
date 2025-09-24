/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ShareResolutionHashMap.java
 *
 * Created on April 28, 2005, 12:11 PM
 */

package com.see.truetransact.ui.share.shareresolution;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  152707
 */
public class ShareResolutionHashMap implements UIMandatoryHashMap{
    
    private HashMap mandatoryMap;
    
    /** Creates a new instance of ShareResolutionHashMap */
    public ShareResolutionHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtResolutionNo", new Boolean(true));
        mandatoryMap.put("tdtResolutionDt", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
    
}






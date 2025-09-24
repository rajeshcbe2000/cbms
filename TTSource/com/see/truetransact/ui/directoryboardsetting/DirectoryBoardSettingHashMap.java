/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * DirectoryBoardSettingHashMap.java
 */
package com.see.truetransact.ui.directoryboardsetting;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class DirectoryBoardSettingHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public DirectoryBoardSettingHashMap(){
        mandatoryMap = new HashMap();
        
        mandatoryMap.put("txtMemno", new Boolean(true));
         mandatoryMap.put("txtDesig", new Boolean(true));
          mandatoryMap.put("txtPriority", new Boolean(true));
        

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

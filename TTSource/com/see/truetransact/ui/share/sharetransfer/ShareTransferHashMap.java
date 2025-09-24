/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareTransferHashMap.java
 * 
 * Created on Thu Feb 03 15:34:13 IST 2005
 */

package com.see.truetransact.ui.share.sharetransfer;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ShareTransferHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ShareTransferHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAcctFrom", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtShareTo", new Boolean(true));
        mandatoryMap.put("txtShareFrom", new Boolean(true));
        mandatoryMap.put("txtAcctTo", new Boolean(true));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * PassbookDataEntryHashMap.java
 * 
 /**
 *
 * @author anjuanand
 */
package com.see.truetransact.ui.passbookDataEntry;

import com.see.truetransact.uimandatory.UIMandatoryHashMap;
import java.util.HashMap;

public class PassbookDataEntryHashMap implements UIMandatoryHashMap {

    private HashMap mandatoryMap;

    public PassbookDataEntryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboBankHead", new Boolean(true));
        mandatoryMap.put("cboBranchHead", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
    }

    public HashMap getMandatoryHashMap() {
        return this.mandatoryMap;
    }
}

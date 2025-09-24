/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LoanNoticeHashMap.java
 */
package com.see.truetransact.ui.sysadmin.loannotice;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LoanNoticeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LoanNoticeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboNoticeType", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("tdtFromInstDate", new Boolean(true));
        mandatoryMap.put("tdtToInstDate", new Boolean(true));
        mandatoryMap.put("tdtDayEndDt", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

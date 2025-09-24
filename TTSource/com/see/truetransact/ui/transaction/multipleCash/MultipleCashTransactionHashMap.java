/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MultipleCashTransactionHashMap.java
 */
package com.see.truetransact.ui.transaction.multipleCash;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MultipleCashTransactionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MultipleCashTransactionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("tdtDateofComplaint", new Boolean(true));
        mandatoryMap.put("txaNameAddress", new Boolean(true));
        mandatoryMap.put("txtEmployeeId", new Boolean(false));
        mandatoryMap.put("txaComments", new Boolean(true));
       

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

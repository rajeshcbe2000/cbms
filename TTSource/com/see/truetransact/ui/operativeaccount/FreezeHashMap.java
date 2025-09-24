/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class FreezeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public FreezeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("rdoCreditDebit_Credit", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("cboType", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

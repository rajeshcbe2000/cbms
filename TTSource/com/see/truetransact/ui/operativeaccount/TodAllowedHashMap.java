/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 **
 */
package com.see.truetransact.ui.operativeaccount;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class TodAllowedHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public TodAllowedHashMap(){
        mandatoryMap = new HashMap();
//        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtTODAllowed", new Boolean(true));
        mandatoryMap.put("dtdToDate", new Boolean(true));
        mandatoryMap.put("dtdFromDate", new Boolean(true));
        mandatoryMap.put("cboPermitedBy", new Boolean(true));
        mandatoryMap.put("dtdPermittedDt", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtRepayedWithin", new Boolean(true));
        mandatoryMap.put("cboRepayedWithin", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

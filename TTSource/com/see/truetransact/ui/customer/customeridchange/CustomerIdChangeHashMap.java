/* Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 */
package com.see.truetransact.ui.customer.customeridchange;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class CustomerIdChangeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public CustomerIdChangeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtOldCustID", new Boolean(true));
        mandatoryMap.put("txtNewCustId", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

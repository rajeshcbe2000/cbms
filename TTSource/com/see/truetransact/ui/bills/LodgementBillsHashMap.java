/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LodgementBillsHashMap.java
 */
package com.see.truetransact.ui.bills;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LodgementBillsHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LodgementBillsHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtMargin", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsStreet", new Boolean(true));
        mandatoryMap.put("txtCommission", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("cboDraweeDetailsCity", new Boolean(true));
        mandatoryMap.put("txtBorrowerNum", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsPincode", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsOthers", new Boolean(true));
        mandatoryMap.put("txtPSBROtherBanks", new Boolean(true));
        mandatoryMap.put("cboTypeOfBill", new Boolean(true));
        mandatoryMap.put("cboDispatchDetailsCity", new Boolean(true));
        mandatoryMap.put("cboDispatchDetailsCountry", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsArea", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsOthers", new Boolean(true));
        mandatoryMap.put("cboDispatchDetailsState", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsName", new Boolean(true));
        mandatoryMap.put("cboBankDetails", new Boolean(true));
        mandatoryMap.put("txtDraweeDetailsStreet", new Boolean(true));
        mandatoryMap.put("txtDiscount", new Boolean(true));
        mandatoryMap.put("txtPostage", new Boolean(true));
        mandatoryMap.put("txtInstrumentDetails", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsPincode", new Boolean(true));
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("cboDraweeDetailsCountry", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsArea", new Boolean(true));
        mandatoryMap.put("txtDispatchDetailsName", new Boolean(true));
        mandatoryMap.put("cboDraweeDetailsState", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

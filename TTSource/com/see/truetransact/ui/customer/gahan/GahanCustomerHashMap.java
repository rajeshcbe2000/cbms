/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GahanCustomerHashMap.java
 *
 * Created on April 24, 2012, 3:29 PM
 */

package com.see.truetransact.ui.customer.gahan;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  admin
 */
public class GahanCustomerHashMap implements UIMandatoryHashMap {
    
    /** Creates a new instance of GahanCustomerHashMap */
     private HashMap mandatoryMap;
    public GahanCustomerHashMap() {
        
       mandatoryMap = new HashMap();
        mandatoryMap.put("txtDocumentNo",new Boolean(false));
        mandatoryMap.put("txtDocumentType",new Boolean(false));
        mandatoryMap.put("tdtDocumentDt",new Boolean(false));
        mandatoryMap.put("txtRegisteredOffice",new Boolean(false));
        mandatoryMap.put("tdtPledgeDate",new Boolean(true));
        mandatoryMap.put("txtPledgeNo",new Boolean(true));
        mandatoryMap.put("txtPledgeAmount",new Boolean(true));
        mandatoryMap.put("txtVillage",new Boolean(true));
        mandatoryMap.put("txtSurveryNo",new Boolean(true));
        mandatoryMap.put("tdtGahanExpDt",new Boolean(false)); 
    }
    
    public HashMap getMandatoryHashMap() {
        
        return mandatoryMap;
    }
    
}

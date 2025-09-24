/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LoanEligibilityMaintenanceHashMap.java
 *
 * Created on 1 February, 2013, 3:08 PM
 */

package com.see.truetransact.ui.product.loan.loaneligibilitymaintenance;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  Admin
 */
public class LoanEligibilityMaintenanceHashMap  implements UIMandatoryHashMap{
    
    private HashMap mandatoryMap;

    public LoanEligibilityMaintenanceHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCropType", new Boolean(true));
        mandatoryMap.put("txtEligibileAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap() {
         return this.mandatoryMap;
    }
    
}

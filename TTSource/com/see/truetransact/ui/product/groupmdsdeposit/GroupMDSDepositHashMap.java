/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GroupMDSDepositHashMap.java
 *
 * Created on 1 February, 2013, 3:08 PM
 */

package com.see.truetransact.ui.product.groupmdsdeposit;

import com.see.truetransact.ui.product.loan.loaneligibilitymaintenance.*;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
/**
 *
 * @author  Admin
 */
public class GroupMDSDepositHashMap  implements UIMandatoryHashMap{
    
    private HashMap mandatoryMap;

    public GroupMDSDepositHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboCropType", new Boolean(true));
        mandatoryMap.put("txtEligibileAmount", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("txtGroupName", new Boolean(true));
        mandatoryMap.put("txtCount", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboInterestAmount", new Boolean(true));
        mandatoryMap.put("txtInterestAmount", new Boolean(true));
        mandatoryMap.put("cboPenalCalculation", new Boolean(true));
        mandatoryMap.put("txtPenalCalculation", new Boolean(true));
        mandatoryMap.put("cboInterestRecovery", new Boolean(true));
        mandatoryMap.put("txtInterestRecovery", new Boolean(true));
        mandatoryMap.put("cboInterestRecovery", new Boolean(true));
        mandatoryMap.put("txtInterestRecovery", new Boolean(true));
        mandatoryMap.put("cboPrematureIntRecType", new Boolean(true));
        mandatoryMap.put("txtPrematureIntRecAmt", new Boolean(true));
        mandatoryMap.put("tdtStartDate", new Boolean(true));
        mandatoryMap.put("tdtEndDate", new Boolean(true));
        
        
    }
    
    public HashMap getMandatoryHashMap() {
         return this.mandatoryMap;
    }
    
}

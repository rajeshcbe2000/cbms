/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AccountMaintenanceHashMap.java
 */
package com.see.truetransact.ui.generalledger;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class AccountMaintenanceHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public AccountMaintenanceHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("chkCreditClearing", new Boolean(false));
        mandatoryMap.put("chkDebitClearing", new Boolean(false));
        mandatoryMap.put("cboContraHead", new Boolean(false));
        mandatoryMap.put("cboTransactionPosting", new Boolean(true));
        mandatoryMap.put("cboPostingMode", new Boolean(true));
        mandatoryMap.put("chkReconcilliationAllowed", new Boolean(false));
        mandatoryMap.put("txtBalanceInGL", new Boolean(false));
        mandatoryMap.put("chkCreditTransfer", new Boolean(false));
        mandatoryMap.put("chkDebitCash", new Boolean(false));
        mandatoryMap.put("txtAccountHead", new Boolean(true));
        mandatoryMap.put("rdoStatus_Implemented", new Boolean(true));
        mandatoryMap.put("chkCreditCash", new Boolean(false));
        mandatoryMap.put("cboGLBalanceType", new Boolean(true));
        mandatoryMap.put("chkDebitTransfer", new Boolean(false));
        mandatoryMap.put("rdoFloatAccount_Yes", new Boolean(true));
        mandatoryMap.put("rdoNegValue_Yes", new Boolean(false));
        mandatoryMap.put("chkHdOfficeAc", new Boolean(false));
        mandatoryMap.put("txtReconcillationAcHd", new Boolean(false));
        mandatoryMap.put("chkBalCheck", new Boolean(false));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

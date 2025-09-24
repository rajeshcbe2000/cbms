/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanInstallmentHashMap.java
 * 
 * Created on Tue Jan 25 16:00:11 IST 2005
 */

package com.see.truetransact.ui.termloan.agritermloan.agriemicalculator;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class AgriTermLoanInstallmentHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public AgriTermLoanInstallmentHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLoanAmt", new Boolean(true));
        mandatoryMap.put("txtBalance", new Boolean(true));
        mandatoryMap.put("txtInterest", new Boolean(true));
        mandatoryMap.put("txtPrincipalAmt", new Boolean(true));
        mandatoryMap.put("txtTotal", new Boolean(true));
        mandatoryMap.put("tdtInstallmentDate", new Boolean(true));
        mandatoryMap.put("txtInterestRate", new Boolean(true));
        mandatoryMap.put("cboFrequency", new Boolean(false));
        mandatoryMap.put("txtNoOfInstall", new Boolean(true));
        mandatoryMap.put("cboroundOfType", new Boolean(false));
        mandatoryMap.put("rdoUniformPrincipalEMI",new Boolean(false));
        mandatoryMap.put("rdoUniformEMI",new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * LoanApplicationHashMap.java
 */

package com.see.truetransact.ui.termloan.loanapplicationregister;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class LoanApplicationHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LoanApplicationHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtCustId", new Boolean(true));
        mandatoryMap.put("cboSchemName", new Boolean(true));
        mandatoryMap.put("txtLoanAmt", new Boolean(true));
    //    mandatoryMap.put("cboTransBran", new Boolean(true));
   //     mandatoryMap.put("tdtDoj", new Boolean(false));
   //     mandatoryMap.put("tdtLastDay", new Boolean(false));

    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

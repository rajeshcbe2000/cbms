/*
 * Copyright 2013 Fincuro Solutions (P) Ltd. All rights reserved.
 *
 * This software is the proprietary information of Fincuro Solutions (P) Ltd..
 * 
 */
package com.see.truetransact.ui.termloan.loantransaction;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class LoanTransactionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public LoanTransactionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtAccountNumber", new Boolean(true));
        mandatoryMap.put("txtPayableAmount", new Boolean(true));
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("tdtFutureDate", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

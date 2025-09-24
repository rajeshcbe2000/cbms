/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositLienHashMap.java
 * 
 * Created on Wed Jun 02 10:44:04 GMT+05:30 2004
 */

package com.see.truetransact.ui.deposit.lien;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class DepositLienHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public DepositLienHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtDepositNo", new Boolean(true));
        mandatoryMap.put("cboSubDepositNo", new Boolean(true));
        mandatoryMap.put("txtLienAmount", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtLienActNum", new Boolean(true));
        mandatoryMap.put("tdtLienDate", new Boolean(true));
        mandatoryMap.put("cboLienProductID", new Boolean(true));
        mandatoryMap.put("cboCreditType", new Boolean(true));
        mandatoryMap.put("txtRemark", new Boolean(false));
        mandatoryMap.put("txtLoanOtherSocietyLienAmount", new Boolean(true));
        mandatoryMap.put("cboLienLoanType", new Boolean(true));
        mandatoryMap.put("txtLoanOtherSocietyLienAcNo", new Boolean(true));
        mandatoryMap.put("tdtLoanOtherSocietyLienDate", new Boolean(true));
        mandatoryMap.put("txtLoanOtherSocietyLienCustName", new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

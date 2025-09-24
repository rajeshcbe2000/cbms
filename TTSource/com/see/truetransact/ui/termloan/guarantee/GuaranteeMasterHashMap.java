/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareProductHashMap.java
 * 
 * Created on Mon Apr 11 12:14:43 IST 2005
 */

package com.see.truetransact.ui.termloan.guarantee;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class GuaranteeMasterHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public GuaranteeMasterHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboPli", new Boolean(true));
        mandatoryMap.put("txtCustId", new Boolean(true));
        mandatoryMap.put("txtSanctionNo", new Boolean(true));
        mandatoryMap.put("tdtSanctionDt", new Boolean(true));
        mandatoryMap.put("txtLoanNo", new Boolean(true));
        mandatoryMap.put("tdtLoanDt", new Boolean(true));
        mandatoryMap.put("txtLoanAmount", new Boolean(true));
        mandatoryMap.put("txtNoOfInstallments", new Boolean(true));
        mandatoryMap.put("cboRepaymentFrequency", new Boolean(true));
        mandatoryMap.put("txtInterestRate", new Boolean(true));
        mandatoryMap.put("tdtGuaranteeDt", new Boolean(true));
        mandatoryMap.put("txtGuaranteeSanctionNo", new Boolean(true));
        mandatoryMap.put("txtGuaranteeAmount", new Boolean(true));
        mandatoryMap.put("txtGuaranteeFeePer", new Boolean(true));
        mandatoryMap.put("txtGuaranteeFee", new Boolean(true));
//        mandatoryMap.put("txtCouponRate", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

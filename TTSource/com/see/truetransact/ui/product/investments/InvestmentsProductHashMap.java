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

package com.see.truetransact.ui.product.investments;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class InvestmentsProductHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public InvestmentsProductHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboAdditionalShareRefund", new Boolean(false));
        mandatoryMap.put("cboUnclaimedDivPeriod", new Boolean(false));
        mandatoryMap.put("cboShareType", new Boolean(true));
        mandatoryMap.put("cboDivCalcFrequency", new Boolean(false));
        mandatoryMap.put("txtMaxShareHolding", new Boolean(true));
        mandatoryMap.put("cboNomineePeriod", new Boolean(false));
        mandatoryMap.put("txtAdmissionFee", new Boolean(true));
        mandatoryMap.put("tdtDueDate", new Boolean(false));
        mandatoryMap.put("txtSurityLimit", new Boolean(false));
        mandatoryMap.put("txtAdditionalShareRefund", new Boolean(false));
        mandatoryMap.put("txtDividentPercentage", new Boolean(false));
        mandatoryMap.put("txtNomineeaAllowed", new Boolean(true));
        mandatoryMap.put("cboRefundPeriod", new Boolean(false));
        mandatoryMap.put("txtMaxLoanLimit", new Boolean(false));
        mandatoryMap.put("txtNomineePeriod", new Boolean(false));
        mandatoryMap.put("cboLoanType", new Boolean(false));
        mandatoryMap.put("tdtCalculatedDate", new Boolean(false));
        mandatoryMap.put("cboDivAppFrequency", new Boolean(false));
        mandatoryMap.put("txtApplicationFee", new Boolean(true));
        mandatoryMap.put("txtShareFee", new Boolean(true));
        mandatoryMap.put("txtSubscribedCapital", new Boolean(true));
        mandatoryMap.put("txtRefundPeriod", new Boolean(false));
        mandatoryMap.put("txtFaceValue", new Boolean(true));
        mandatoryMap.put("txtShareSuspAccount", new Boolean(true));
        mandatoryMap.put("txtLoanAvailingLimit", new Boolean(false));
        mandatoryMap.put("txtPaidupCapital", new Boolean(true));
        mandatoryMap.put("chkReqActHolder", new Boolean(false));
        mandatoryMap.put("txtSecuredAdvances", new Boolean(false));
        mandatoryMap.put("txtRefundinaYear", new Boolean(false));
        mandatoryMap.put("txtUnclaimedDivPeriod", new Boolean(false));
        mandatoryMap.put("txtUnsecuredAdvances", new Boolean(false));
        mandatoryMap.put("txtIssuedCapital", new Boolean(true));
        mandatoryMap.put("txtShareAccount", new Boolean(true));
        mandatoryMap.put("txtMemFeeAcct", new Boolean(true));
        mandatoryMap.put("txtApplFeeAcct", new Boolean(true));
        mandatoryMap.put("txtShareFeeAcct", new Boolean(true));


    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

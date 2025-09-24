/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * StandingInstructionHashMap.java
 *
 * Created on Thu Nov 04 12:09:42 GMT+05:30 2004
 */

package com.see.truetransact.ui.supporting.standinginstruction;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class StandingInstructionHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    
    public StandingInstructionHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("dtdEndDtSI", new Boolean(true));
        mandatoryMap.put("cboMoRSI", new Boolean(false));
        mandatoryMap.put("txtGraceDaysSI", new Boolean(false));
        mandatoryMap.put("txtForwardCount", new Boolean(false));
        mandatoryMap.put("txtCommChargesSI", new Boolean(false));
        mandatoryMap.put("cboSIType", new Boolean(true));
        mandatoryMap.put("txtMinBalSI", new Boolean(false));
        mandatoryMap.put("cboWeekDay", new Boolean(false));
        mandatoryMap.put("txtMultiplieSI", new Boolean(false));
        mandatoryMap.put("rdoHolidayExecution_Yes", new Boolean(false));
        mandatoryMap.put("rdoSIAutoPosting_Yes", new Boolean(false));
        mandatoryMap.put("cboProdIDCSI", new Boolean(false));
        mandatoryMap.put("txtRettCommChargesSI", new Boolean(false));
        mandatoryMap.put("cboProdIDDSI", new Boolean(false));
        mandatoryMap.put("txtAccNoDSI", new Boolean(false));
        mandatoryMap.put("txtExecutionCharges", new Boolean(false));
        mandatoryMap.put("txtAcceptanceCharges", new Boolean(false));
        mandatoryMap.put("cboWeek", new Boolean(false));
        mandatoryMap.put("txtAmountCSI", new Boolean(false));
        mandatoryMap.put("cboExecConfig", new Boolean(true));
        mandatoryMap.put("txtParticularsCSI", new Boolean(false));
        mandatoryMap.put("rdoRettCommSI_Yes", new Boolean(false));
        mandatoryMap.put("cboFrequencySI", new Boolean(false));
        mandatoryMap.put("txtBeneficiarySI", new Boolean(false));
        mandatoryMap.put("rdoCommSI_Yes", new Boolean(false));
        mandatoryMap.put("txtParticularsDSI", new Boolean(false));
        mandatoryMap.put("dtdStartDtSI", new Boolean(true));
        mandatoryMap.put("txtAmountDSI", new Boolean(false));
        mandatoryMap.put("cboSpecificDate", new Boolean(false));
        mandatoryMap.put("txtSINo", new Boolean(true));
        mandatoryMap.put("txtAccNoCSI", new Boolean(false));
        mandatoryMap.put("txtFailureCharges", new Boolean(false));
        mandatoryMap.put("cboProductTypeDSI", new Boolean(true));
        mandatoryMap.put("cboProductTypeCSI", new Boolean(true));
        mandatoryMap.put("cboExecutionDay", new Boolean(false));
        mandatoryMap.put("txtAccHeadValueCSI", new Boolean(false));
        mandatoryMap.put("txtAccHeadValueDSI", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

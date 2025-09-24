/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ShareHashMap.java
 * 
 * Created on Fri Dec 24 18:24:09 IST 2004
 */

package com.see.truetransact.ui.share;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class ShareHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public ShareHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtShareDetShareAcctNo", new Boolean(true));
        mandatoryMap.put("txtDupIdCard", new Boolean(true));
        mandatoryMap.put("txtShareAcctNo", new Boolean(false));
        mandatoryMap.put("txtRelativeDetails", new Boolean(false));
        mandatoryMap.put("txtCustId", new Boolean(true));
        mandatoryMap.put("tdtNotEligiblePeriod", new Boolean(false));
        mandatoryMap.put("txtResolutionNo", new Boolean(false));
        mandatoryMap.put("txtConnGrpDet", new Boolean(false));
        mandatoryMap.put("txtMemFee", new Boolean(true));
        mandatoryMap.put("tdtIssId", new Boolean(false));
        mandatoryMap.put("txtShareFee", new Boolean(true));
        mandatoryMap.put("txtShareDetNoOfShares", new Boolean(true));
        mandatoryMap.put("txtApplFee", new Boolean(true));
        mandatoryMap.put("tdtShareDetIssShareCert", new Boolean(false));
        mandatoryMap.put("txtShareDetShareNoTo", new Boolean(false));
        mandatoryMap.put("cboAcctStatus", new Boolean(true));
        mandatoryMap.put("cboConstitution", new Boolean(true));
        mandatoryMap.put("cboCommAddrType", new Boolean(true));
        mandatoryMap.put("cboShareType", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("IntroducerType", new Boolean(true));
        mandatoryMap.put("txtDirRelDet", new Boolean(false));
        mandatoryMap.put("txtShareAmt", new Boolean(true));
        mandatoryMap.put("txtPropertyDetails", new Boolean(false));
        mandatoryMap.put("txtShareDetShareNoFrom", new Boolean(false));
        mandatoryMap.put("txtWelFund", new Boolean(true));
        mandatoryMap.put("chkNotEligibleStatus", new Boolean(false));
        mandatoryMap.put("txtResolutionNo1", new Boolean(false));
        mandatoryMap.put("cboMemDivPayMode",new Boolean(true));
        
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * MDSPrizedMoneyDetailsEntryHashMap.java
 * 
 * Created on Wed Jun 08 17:29:28 IST 2011
 */

package com.see.truetransact.ui.gdsapplication.gdsprizedmoneydetailsentry;

import com.see.truetransact.ui.mdsapplication.mdsprizedmoneydetailsentry.*;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class GDSPrizedMoneyDetailsEntryHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public GDSPrizedMoneyDetailsEntryHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtTotalBonusAmount", new Boolean(false));
        mandatoryMap.put("txtDivisionNo", new Boolean(false));
        mandatoryMap.put("txtInstallmentsPaid", new Boolean(false));
        mandatoryMap.put("txtCommisionAmount", new Boolean(false));
        mandatoryMap.put("txtInstallmentsDue", new Boolean(false));
        mandatoryMap.put("tdtInstallmentDate", new Boolean(false));
        mandatoryMap.put("txtInstallmentsAmountPaid", new Boolean(false));
        mandatoryMap.put("txtSchemeName", new Boolean(false));
        mandatoryMap.put("txtMemberNo", new Boolean(false));
        mandatoryMap.put("txtNextBonusAmount", new Boolean(false));
        mandatoryMap.put("txtInstallmentNo", new Boolean(false));
        mandatoryMap.put("txtTotalDiscount", new Boolean(false));
        mandatoryMap.put("txtNetAmountPayable", new Boolean(false));
        mandatoryMap.put("txtApplicationNo", new Boolean(false));
        mandatoryMap.put("txtMemberClass", new Boolean(false));
        mandatoryMap.put("txtPrizedAmount", new Boolean(false));
        mandatoryMap.put("tdtDrawOrAuctionDt", new Boolean(false));
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * ChequeBookHashMap.java
 */
package com.see.truetransact.ui.supporting.chequebook;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class ChequeBookHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public ChequeBookHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboNoOfLeaves", new Boolean(true));
        mandatoryMap.put("txtSeriesFrom", new Boolean(false));
        mandatoryMap.put("txtEndingCheque", new Boolean(true));
        mandatoryMap.put("txtEndingchequeNo", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("tdtChequeDate", new Boolean(false));
        mandatoryMap.put("rdoLeaf_single", new Boolean(false));
        mandatoryMap.put("txtStopPaymentCharges", new Boolean(true));
        mandatoryMap.put("txtEndingChequeNo", new Boolean(false));
        mandatoryMap.put("txtNamesOfAccount", new Boolean(false));
        mandatoryMap.put("txtPayeeName", new Boolean(false));
        mandatoryMap.put("cboProdId", new Boolean(true));
        mandatoryMap.put("cboMethodOfDelivery", new Boolean(false));
        mandatoryMap.put("txtChequeAmt", new Boolean(false));
        mandatoryMap.put("txtAccNo", new Boolean(true));
        mandatoryMap.put("txtChargesCollected", new Boolean(true));
        mandatoryMap.put("txtStartingCheque", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(false));
        mandatoryMap.put("txtRemark", new Boolean(false));
        mandatoryMap.put("txtLeafNo2", new Boolean(true));
        mandatoryMap.put("cboProductID", new Boolean(true));
        mandatoryMap.put("txtNoOfChequeBooks", new Boolean(true));
        mandatoryMap.put("txtLeafNo1", new Boolean(true));
        mandatoryMap.put("cboReasonStopPayment", new Boolean(true));
        mandatoryMap.put("txtStartingChequeNo", new Boolean(true));
        mandatoryMap.put("txtAccountNo", new Boolean(true));
        mandatoryMap.put("txtStartCheque", new Boolean(true));
        mandatoryMap.put("txtEndCheque", new Boolean(false));
        mandatoryMap.put("txtSeriesNoTo", new Boolean(false));
        mandatoryMap.put("txtStartchequeNo", new Boolean(true));
        mandatoryMap.put("txtAccounNumber", new Boolean(false));
        mandatoryMap.put("cboUsage", new Boolean(true));
        
        mandatoryMap.put("cboProdType", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("cboProduct_Type", new Boolean(true));
        
        mandatoryMap.put("cboEcsProductType", new Boolean(true));
        mandatoryMap.put("cboEcsProductId", new Boolean(true));
        mandatoryMap.put("txtEcsAccNo", new Boolean(true));
        mandatoryMap.put("txtEndEcs1", new Boolean(true));
        mandatoryMap.put("txtEndEcs2", new Boolean(true));
        mandatoryMap.put("cboEcsReasonStopPayment", new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

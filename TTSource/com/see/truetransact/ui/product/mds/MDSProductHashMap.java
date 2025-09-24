/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MDSProductHashMap.java
 */
package com.see.truetransact.ui.product.mds;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MDSProductHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MDSProductHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductId",  new Boolean(true));
        mandatoryMap.put("txtAuctionMaxAmt", new Boolean(true));
        mandatoryMap.put("txtAuctionMinAmt", new Boolean(true));
        mandatoryMap.put("txtCommisionRate", new Boolean(false));
        mandatoryMap.put("txtDiscountRate", new Boolean(false));
        mandatoryMap.put("txtPenalIntRate", new Boolean(false));
        mandatoryMap.put("txtPenalPrizedRate", new Boolean(false));
        mandatoryMap.put("txtLoanIntRate", new Boolean(false));
        mandatoryMap.put("txtBonusGracePeriod", new Boolean(false));
        mandatoryMap.put("txtBonusPrizedPreriod", new Boolean(false));
        mandatoryMap.put("txtPenalPeriod", new Boolean(true));
        mandatoryMap.put("txtPenalPrizedPeriod", new Boolean(true));
        mandatoryMap.put("txtDiscountPeriod", new Boolean(false));
        mandatoryMap.put("txtDiscountPrizedPeriod", new Boolean(false));
        mandatoryMap.put("txtMargin", new Boolean(false));
        mandatoryMap.put("txtMinLoanAmt",  new Boolean(false));
        mandatoryMap.put("txtMaxLoanAmt",  new Boolean(false));
        mandatoryMap.put("cboCommisionRate",  new Boolean(false));
        mandatoryMap.put("cboDiscountRate",  new Boolean(false));
        mandatoryMap.put("cboPenalIntRate",  new Boolean(false));
        mandatoryMap.put("cboPenalPrizedRate",  new Boolean(false));
        mandatoryMap.put("cboLoanIntRate",  new Boolean(false));
        mandatoryMap.put("cboBonusGracePeriod",  new Boolean(false));
        mandatoryMap.put("cboBonusPrizedPreriod",  new Boolean(false));
        mandatoryMap.put("cboPenalPeriod",  new Boolean(false));
        mandatoryMap.put("cboPenalPrizedPeriod",  new Boolean(false));
        mandatoryMap.put("cboPenalCalc",  new Boolean(true));
        mandatoryMap.put("cboDiscountPeriod",  new Boolean(false));
        mandatoryMap.put("cboDiscountPrizedPeriod",  new Boolean(false));
//        mandatoryMap.put("lblThalayalBonusHead1", new Boolean(true));
//        mandatoryMap.put("txtReceiptHead",  new Boolean(true));
//        mandatoryMap.put("txtPaymentHead",  new Boolean(true));
//        mandatoryMap.put("txtSuspenseHead",  new Boolean(true));
//        mandatoryMap.put("txtMiscellaneousHead",  new Boolean(true));
//        mandatoryMap.put("txtCommisionHead",  new Boolean(true));
//        mandatoryMap.put("txtBonusPayableHead",  new Boolean(true));
//        mandatoryMap.put("txtBonusReceivableHead",  new Boolean(true));
//        mandatoryMap.put("txtPenalHead",  new Boolean(true));
//        mandatoryMap.put("txtThalayalReceiptsHead",  new Boolean(true));
//        mandatoryMap.put("txtThalayalBonusHead",  new Boolean(true));
//        mandatoryMap.put("txtMunnalBonusHead",  new Boolean(true));
//        mandatoryMap.put("txtMunnalReceiptsHead",  new Boolean(true));
//        mandatoryMap.put("txtBankingHead",  new Boolean(true));
//        mandatoryMap.put("txtNoticeChargesHead",  new Boolean(true));
//        mandatoryMap.put("txtCaseExpensesHead",  new Boolean(true));
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

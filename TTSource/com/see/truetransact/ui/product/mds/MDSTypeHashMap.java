/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * MDSTypeHashMap.java
 */
package com.see.truetransact.ui.product.mds;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class MDSTypeHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public MDSTypeHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNoofDivision", new Boolean(false));
        mandatoryMap.put("txtNoofMemberPer", new Boolean(false));
        mandatoryMap.put("txtNoofAuctions", new Boolean(false));
        mandatoryMap.put("txtNoofInst", new Boolean(false));
        mandatoryMap.put("txtNoofDraws", new Boolean(false));
        mandatoryMap.put("txtInstAmt", new Boolean(false));
        mandatoryMap.put("txtNoofMemberScheme", new Boolean(false));
        mandatoryMap.put("txtTotAmtPerDivision", new Boolean(false));
        mandatoryMap.put("tdtSchemeStDt", new Boolean(true));
        mandatoryMap.put("tdtSchemeEndDt", new Boolean(true));
        mandatoryMap.put("cboInstFreq", new Boolean(true));
        mandatoryMap.put("txtAcctHead", new Boolean(true));
        mandatoryMap.put("cboProductId", new Boolean(true));
        mandatoryMap.put("txtResolutionNo", new Boolean(true));
        mandatoryMap.put("tdtlResolutionDate", new Boolean(true));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtNoofCoChittals", new Boolean(false));
        mandatoryMap.put("txtNoofCoInstallments", new Boolean(false));
        mandatoryMap.put("txtMaxNoofMemberCoChittals", new Boolean(false));
        mandatoryMap.put("txtCoChittalInstAmount", new Boolean(false));
        mandatoryMap.put("txtTotAmtUnderScheme", new Boolean(true));
        mandatoryMap.put("chkApplicable2", new Boolean(false));
        mandatoryMap.put("chkApplicable4", new Boolean(false));
        mandatoryMap.put("chkApplicable1", new Boolean(false));
        mandatoryMap.put("chkApplicable3", new Boolean(false));
        mandatoryMap.put("txtInstallments", new Boolean(true));
        mandatoryMap.put("txtDay", new Boolean(true));
        mandatoryMap.put("cboInstallmentDay", new Boolean(false));
        mandatoryMap.put("cboAuctionDay", new Boolean(false));
        mandatoryMap.put("tdtInstallmentDt", new Boolean(false));
        mandatoryMap.put("txtAmount", new Boolean(false));
        mandatoryMap.put("txtNoticeChargeAmt", new Boolean(false));
        mandatoryMap.put("txtPostageChargeAmt", new Boolean(false));
        mandatoryMap.put("txtBonus", new Boolean(false));
        mandatoryMap.put("txtPaymentAmount", new Boolean(false));
        mandatoryMap.put("txtChittalNumberPattern", new Boolean(true));
        mandatoryMap.put("txtNextChittalNumber", new Boolean(true));
   /*     mandatoryMap.put("txtReceiptHead",  new Boolean(true));
        mandatoryMap.put("txtPaymentHead",  new Boolean(true));
        mandatoryMap.put("txtSuspenseHead",  new Boolean(false));
        mandatoryMap.put("txtMiscellaneousHead",  new Boolean(true));
        mandatoryMap.put("txtCommisionHead",  new Boolean(true));
        mandatoryMap.put("txtBonusPayableHead",  new Boolean(true));
        mandatoryMap.put("txtBonusReceivableHead",  new Boolean(true));
        mandatoryMap.put("txtPenalHead",  new Boolean(true));
        mandatoryMap.put("txtThalayalReceiptsHead",  new Boolean(true));
        mandatoryMap.put("txtThalayalBonusHead",  new Boolean(true));
        mandatoryMap.put("txtMunnalBonusHead",  new Boolean(true));
        mandatoryMap.put("txtMunnalReceiptsHead",  new Boolean(true));
        mandatoryMap.put("txtBankingHead",  new Boolean(true));
        mandatoryMap.put("txtNoticeChargesHead",  new Boolean(true));
        mandatoryMap.put("txtChargeHead",  new Boolean(true));
        mandatoryMap.put("txtCaseExpensesHead",  new Boolean(true));
        mandatoryMap.put("txtDiscountHead",  new Boolean(true));
        mandatoryMap.put("txtMDSPayableHead",  new Boolean(true));
        mandatoryMap.put("txtStampAdvanceHead",  new Boolean(true));
        mandatoryMap.put("txtARCCostHead",  new Boolean(true));
        mandatoryMap.put("txtARCExpenseHead",  new Boolean(true));
        mandatoryMap.put("txtEACostHead",  new Boolean(true));
        mandatoryMap.put("txtEAExpenseHead",  new Boolean(true));
        mandatoryMap.put("txtEPCostHead",  new Boolean(true));
        mandatoryMap.put("txtEPExpenseHead",  new Boolean(true));
        mandatoryMap.put("txtPostageHead",  new Boolean(true));
        mandatoryMap.put("txtMDSReceivableHead",  new Boolean(true));
        mandatoryMap.put("txtSundryReceiptHead",  new Boolean(true));
        mandatoryMap.put("txtSundryPaymentHead",  new Boolean(true));*/
    }
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

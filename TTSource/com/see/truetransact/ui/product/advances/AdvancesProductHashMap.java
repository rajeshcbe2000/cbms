/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 * AdvancesProductHashMap.java
 */
package com.see.truetransact.ui.product.advances;
import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;
public class AdvancesProductHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;
    public AdvancesProductHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtExOLHAH", new Boolean(false));
        mandatoryMap.put("cboProdCurrency", new Boolean(false));
        mandatoryMap.put("rdoUAICOthersIR_Yes", new Boolean(false));
        mandatoryMap.put("rdoCreditCompdInterestPayable_Yes", new Boolean(false));
        mandatoryMap.put("cboChargeOnDocFCharges", new Boolean(false));
        mandatoryMap.put("cboCollectChargeFCharges", new Boolean(false));
        mandatoryMap.put("rdoDIAUEAccount_Yes", new Boolean(false));
        mandatoryMap.put("cboDIRoundOffIR", new Boolean(false));
        mandatoryMap.put("rdoEOLOthersIR_Yes", new Boolean(false));
        mandatoryMap.put("txtProductIdAccount", new Boolean(true));
        mandatoryMap.put("txtMaxAmountOnWS", new Boolean(false));
        mandatoryMap.put("cboAmountCROC", new Boolean(false));
        mandatoryMap.put("rdoACAAccount_Yes", new Boolean(false));
        mandatoryMap.put("txtRatePLRIR", new Boolean(false));
        mandatoryMap.put("txtMaxDIAmtIR", new Boolean(false));
        mandatoryMap.put("txtCRCoutwardAH", new Boolean(false));
        mandatoryMap.put("tdtLastADIP", new Boolean(false));
        mandatoryMap.put("tdtCLStartAccount", new Boolean(false));
        mandatoryMap.put("rdoIsProcessingCharges_Yes", new Boolean(false));
        mandatoryMap.put("txtAddIntRateIP", new Boolean(false));
        mandatoryMap.put("txtLastAccNoAccount", new Boolean(false));
        mandatoryMap.put("txtFolioEntriesFCharges", new Boolean(false));
        mandatoryMap.put("txtCICAH", new Boolean(false));
        mandatoryMap.put("cboCLPeriod", new Boolean(false));
        mandatoryMap.put("cboProcessingCharges", new Boolean(false));
        mandatoryMap.put("rdoChargedDIIR_Yes", new Boolean(false));
        mandatoryMap.put("rdoBranchBankingSI_Yes", new Boolean(false));
        mandatoryMap.put("tdtLastAppliedFCharges", new Boolean(false));
        mandatoryMap.put("rdoDebitCompoundIR_Yes", new Boolean(false));
        mandatoryMap.put("txtMiscServiceCharges", new Boolean(false));
        mandatoryMap.put("txtDIAH", new Boolean(false));
        mandatoryMap.put("txtRateSI", new Boolean(false));
        mandatoryMap.put("txtMinDIRateIR", new Boolean(false));
        mandatoryMap.put("rdoTokanAccount_Yes", new Boolean(false));
        mandatoryMap.put("rdoCreditIntInterestPayable_Yes", new Boolean(false));
        mandatoryMap.put("cboCIROIP", new Boolean(false));
        mandatoryMap.put("cboDICalculationFIR", new Boolean(false));
        mandatoryMap.put("rdoIsApplicableFCharges_Yes", new Boolean(false));
        mandatoryMap.put("rdoNewAccountPLRIR_Yes", new Boolean(false));
        mandatoryMap.put("cboCreditInterestAFIP", new Boolean(false));
        mandatoryMap.put("txtPIAH", new Boolean(false));
        mandatoryMap.put("txtStopPaymentCharges", new Boolean(false));
        mandatoryMap.put("tdtInterestCDDebitIR", new Boolean(false));
        mandatoryMap.put("rdoIsCommitmentCharges_Yes", new Boolean(false));
        mandatoryMap.put("txtStatementCharges", new Boolean(false));
        mandatoryMap.put("txtProductDescAccount", new Boolean(true));
        mandatoryMap.put("rdoMobileBankingClientSI_Yes", new Boolean(false));
        mandatoryMap.put("txtAgCIAH", new Boolean(false));
        mandatoryMap.put("rdoLimitEIOthersIR_Yes", new Boolean(false));
        mandatoryMap.put("rdoSAOAccount_Yes", new Boolean(false));
        mandatoryMap.put("txtChequebookCharges", new Boolean(false));
        mandatoryMap.put("txtCIAH", new Boolean(false));
        mandatoryMap.put("rdoLDAccount_Yes", new Boolean(false));
        mandatoryMap.put("txtSCAH", new Boolean(false));
        mandatoryMap.put("txtNumberpatternAccount", new Boolean(false));
        mandatoryMap.put("txtMSCAH", new Boolean(false));
        mandatoryMap.put("txtRateFCharges", new Boolean(false));
        mandatoryMap.put("rdoIsStopPaymentCharges_Yes", new Boolean(false));
        mandatoryMap.put("txtSPCAH", new Boolean(false));
        mandatoryMap.put("rdoDebitCardSI_Yes", new Boolean(false));
        mandatoryMap.put("cboCommitmentCharges", new Boolean(false));
        mandatoryMap.put("txtManagerDistAccount", new Boolean(false));
        mandatoryMap.put("rdoIsApplicablePLRIR_Yes", new Boolean(false));
        mandatoryMap.put("rdoODALAccount_Yes", new Boolean(false));
        mandatoryMap.put("txtAccountClosingCharges", new Boolean(false));
        mandatoryMap.put("txtMaxDIRateIR", new Boolean(false));
        mandatoryMap.put("txtCLPeriodAccount", new Boolean(false));
        mandatoryMap.put("cboProdFrequencyIP", new Boolean(false));
        mandatoryMap.put("cboDPRoundOffIR", new Boolean(false));
        mandatoryMap.put("txtPenalIROthersIR", new Boolean(false));
        mandatoryMap.put("cboAssetsSI", new Boolean(false));
        mandatoryMap.put("rdoExistingAccountPLRIR_Yes", new Boolean(false));
        mandatoryMap.put("txtCreditInterestRateIP", new Boolean(false));
        mandatoryMap.put("rdoAdditionalIntInterestPayable_Yes", new Boolean(false));
        mandatoryMap.put("rdoCIUEAccount_Yes", new Boolean(false));
        mandatoryMap.put("rdoCreditCardSI_Yes", new Boolean(false));
        mandatoryMap.put("rdoIsChequebookCharges_Yes", new Boolean(false));
        mandatoryMap.put("tdtAppliedFromPLRIR", new Boolean(false));
        mandatoryMap.put("tdtLastCDIP", new Boolean(false));
        mandatoryMap.put("txtACCAH", new Boolean(false));
        mandatoryMap.put("cboDIApplicationFIR", new Boolean(false));
        mandatoryMap.put("cboCreditInterestCompdFIP", new Boolean(false));
        mandatoryMap.put("cboStmtFrequency", new Boolean(true));
        mandatoryMap.put("tdtDueDateFCharges", new Boolean(false));
        mandatoryMap.put("tdtInterestADDebitIR", new Boolean(false));
        mandatoryMap.put("rdoIsStatementCharges_Yes", new Boolean(false));
        mandatoryMap.put("cboDICompoundFIR", new Boolean(false));
        mandatoryMap.put("cboCreditInterestCFIP", new Boolean(false));
        mandatoryMap.put("cboCalcCtriteriaIP", new Boolean(false));
        mandatoryMap.put("txtFreeCLAccount", new Boolean(true));
        mandatoryMap.put("rdoPenalOthersIR_Yes", new Boolean(false));
        mandatoryMap.put("cboAmountCRIC", new Boolean(false));
        mandatoryMap.put("cboProductFOthersIR", new Boolean(false));
        mandatoryMap.put("cboCPROIP", new Boolean(false));
        mandatoryMap.put("cboIRFrequencyFCharges", new Boolean(false));
        mandatoryMap.put("cboChargeOnTransactionFCharges", new Boolean(false));
        mandatoryMap.put("tdtAccountSFPLRIR", new Boolean(false));
        mandatoryMap.put("txtProcessingCharges", new Boolean(false));
        mandatoryMap.put("txtEIAH", new Boolean(false));
        mandatoryMap.put("rdoWSAccount_Yes", new Boolean(false));
        mandatoryMap.put("cboBehavesLike", new Boolean(false));
        mandatoryMap.put("txtFCAH", new Boolean(false));
        mandatoryMap.put("rdoATMCardSI_Yes", new Boolean(false));
        mandatoryMap.put("txtRateCRIC", new Boolean(false));
        mandatoryMap.put("txtCRCInwardAH", new Boolean(false));
        mandatoryMap.put("cboCAFrequencyFCharges", new Boolean(false));
        mandatoryMap.put("txtMinDIAmtIR", new Boolean(false));
        mandatoryMap.put("txtCommitmentCharges", new Boolean(false));
        mandatoryMap.put("txtRateCROC", new Boolean(false));
        mandatoryMap.put("txtAccountHeadAccount", new Boolean(false));
    }
    
    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

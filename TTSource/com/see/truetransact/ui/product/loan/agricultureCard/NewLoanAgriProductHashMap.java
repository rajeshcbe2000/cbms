/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * NewLoanProductHashMap.java
 * 
 * Created on Fri Apr 29 16:27:33 IST 2005
 */

package com.see.truetransact.ui.product.loan.agricultureCard;

import java.util.HashMap;
import com.see.truetransact.uimandatory.UIMandatoryHashMap;

public class NewLoanAgriProductHashMap implements UIMandatoryHashMap {
    private HashMap mandatoryMap;

    public NewLoanAgriProductHashMap(){
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboMinPeriod", new Boolean(false));
        mandatoryMap.put("txtCharge_Limit3", new Boolean(false));
        mandatoryMap.put("rdoProcessCharges_Yes", new Boolean(false));
        mandatoryMap.put("cboPeriodTransLossAssets", new Boolean(false));
        mandatoryMap.put("txtMiscServCharges", new Boolean(false));
        mandatoryMap.put("rdoMobileBanlingClient_Yes", new Boolean(false));
        mandatoryMap.put("txtProvisionLossAssets", new Boolean(false));
        mandatoryMap.put("rdoIsDebitInterUnderClearing_Yes", new Boolean(false));
        mandatoryMap.put("txtPLRRate", new Boolean(false));
        mandatoryMap.put("txtLastAccNum", new Boolean(false));
        mandatoryMap.put("txtAccHead", new Boolean(true));
        mandatoryMap.put("txtMaxAmtLoan", new Boolean(true));
        mandatoryMap.put("cboLoanPeriodMul", new Boolean(false));
        mandatoryMap.put("cboDebitInterComFreq", new Boolean(false));
        mandatoryMap.put("cboDebInterCalcFreq", new Boolean(false));
        mandatoryMap.put("txtExposureLimit_Policy2", new Boolean(false));
        mandatoryMap.put("tdtNextFolioDDate", new Boolean(false));
        mandatoryMap.put("cboPeriodTransDoubtfulAssets1", new Boolean(false));
        mandatoryMap.put("cboPeriodTransDoubtfulAssets2", new Boolean(false));
        mandatoryMap.put("cboPeriodTransDoubtfulAssets3", new Boolean(false));
        mandatoryMap.put("txtCharge_Limit2", new Boolean(false));
        mandatoryMap.put("rdoSubsidy_Yes", new Boolean(false));
        mandatoryMap.put("rdoPLRRateAppl_Yes", new Boolean(false));
        mandatoryMap.put("txtPeriodAfterWhichTransNPerformingAssets", new Boolean(false));
        mandatoryMap.put("cboMinPeriodsArrears", new Boolean(false));
        mandatoryMap.put("rdoIsStaffAccOpened_Yes", new Boolean(false));
        mandatoryMap.put("rdoStatCharges_Yes", new Boolean(false));
        mandatoryMap.put("txtProductDesc", new Boolean(true));
        mandatoryMap.put("txtMinPeriod", new Boolean(false));
        mandatoryMap.put("cboIncompleteFolioRoundOffFreq", new Boolean(false));
        mandatoryMap.put("rdoIsAnyBranBankingAllowed_Yes", new Boolean(false));
        mandatoryMap.put("txtNoEntriesPerFolio", new Boolean(false));
        mandatoryMap.put("txtStatementCharges", new Boolean(false));
        mandatoryMap.put("txtMinInterDebited", new Boolean(true));
        mandatoryMap.put("rdoPLRApplNewAcc_Yes", new Boolean(false));
        mandatoryMap.put("tdtPlrApplAccSancForm", new Boolean(false));
        mandatoryMap.put("cboMaxPeriod", new Boolean(false));
        mandatoryMap.put("txtNumPatternFollowed", new Boolean(false));
        mandatoryMap.put("txtAccCreditInter", new Boolean(false));
        mandatoryMap.put("rdoToChargeOn_Man", new Boolean(false));
        mandatoryMap.put("tdtLastInterAppDate", new Boolean(false));
        mandatoryMap.put("cboProductFreq", new Boolean(true));
        mandatoryMap.put("txtStatChargesRate", new Boolean(false));
        mandatoryMap.put("txtPeriodTranSStanAssets", new Boolean(false));
        mandatoryMap.put("txtAccClosingCharges", new Boolean(true));
        mandatoryMap.put("cboDocumentType", new Boolean(false));
        mandatoryMap.put("rdoFolioChargesAppl_Yes", new Boolean(false));
        mandatoryMap.put("txtDocumentDesc", new Boolean(false));
        mandatoryMap.put("txtExposureLimit_Policy", new Boolean(false));
        mandatoryMap.put("tdtLastFolioChargesAppl", new Boolean(false));
        mandatoryMap.put("txtMaxDebitInterAmt", new Boolean(false));
        mandatoryMap.put("txtCheReturnChargest_Out", new Boolean(false));
        mandatoryMap.put("txtPenalInterRate", new Boolean(false));
        mandatoryMap.put("cboFolioChargesAppFreq", new Boolean(false));
        mandatoryMap.put("txtRateAmt", new Boolean(false));
        mandatoryMap.put("rdoATMcardIssued_Yes", new Boolean(false));
        mandatoryMap.put("cboToCollectFolioCharges", new Boolean(false));
        mandatoryMap.put("txtMinDebitInterAmt", new Boolean(false));
        mandatoryMap.put("txtDocumentNo", new Boolean(false));
        mandatoryMap.put("txtAccClosCharges", new Boolean(false));
        mandatoryMap.put("cboProdCurrency", new Boolean(false));
        mandatoryMap.put("txtMinAmtLoan", new Boolean(true));
        mandatoryMap.put("txtExposureLimit_Prud2", new Boolean(false));
        mandatoryMap.put("cboOperatesLike", new Boolean(true));
        mandatoryMap.put("txtMiscSerCharges", new Boolean(true));
        mandatoryMap.put("cboCharge_Limit2", new Boolean(false));
        mandatoryMap.put("rdoPenalAppl_Yes", new Boolean(false));
        mandatoryMap.put("cboDebitInterRoundOff", new Boolean(false));
        mandatoryMap.put("rdoldebitInterCharged_Yes", new Boolean(false));
        mandatoryMap.put("txtAccDebitInter", new Boolean(false));
        mandatoryMap.put("txtExposureLimit_Prud", new Boolean(false));
        mandatoryMap.put("cboDebitInterAppFreq", new Boolean(false));
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("txtProvisionStandardAssetss", new Boolean(false));
        mandatoryMap.put("rdoPLRApplExistingAcc_Yes", new Boolean(false));
        mandatoryMap.put("txtPenalInter", new Boolean(false));
        mandatoryMap.put("tdtPLRAppForm", new Boolean(false));
        mandatoryMap.put("rdoCreditCardIssued_Yes", new Boolean(false));
        mandatoryMap.put("cboPeriodTranSStanAssets", new Boolean(false));
        mandatoryMap.put("txtMaxPeriod", new Boolean(false));
        mandatoryMap.put("cboPeriodAfterWhichTransNPerformingAssets", new Boolean(false));
        mandatoryMap.put("rdoLimitExpiryInter_Yes", new Boolean(false));
        mandatoryMap.put("txtMinDebitInterRate", new Boolean(false));
        mandatoryMap.put("txtNumPatternFollowedSuffix", new Boolean(false));
        mandatoryMap.put("txtCheReturnChargest_In", new Boolean(false));
        mandatoryMap.put("txtExpiryInter", new Boolean(false));
        mandatoryMap.put("txtProvisionsStanAssets", new Boolean(false));
        mandatoryMap.put("txtRatePerFolio", new Boolean(false));
        mandatoryMap.put("tdtLastInterCalcDate", new Boolean(false));
        mandatoryMap.put("txtProcessingCharges", new Boolean(false));
        mandatoryMap.put("txtCommitCharges", new Boolean(false));
        mandatoryMap.put("cbocalcType", new Boolean(true));
        mandatoryMap.put("cboCharge_Limit3", new Boolean(false));
        mandatoryMap.put("txtProvisionDoubtfulAssets1", new Boolean(false));
        mandatoryMap.put("txtProvisionDoubtfulAssets2", new Boolean(false));
        mandatoryMap.put("txtProvisionDoubtfulAssets3", new Boolean(false));
        mandatoryMap.put("txtApplicableInter", new Boolean(true));
        mandatoryMap.put("rdoCommitmentCharge_Yes", new Boolean(false));
        mandatoryMap.put("txtMaxDebitInterRate", new Boolean(false));
        mandatoryMap.put("txtRangeTo", new Boolean(false));
        mandatoryMap.put("txtRangeFrom", new Boolean(false));
        mandatoryMap.put("txtPeriodTransLossAssets", new Boolean(false));
        mandatoryMap.put("cboDebitProdRoundOff", new Boolean(false));
        mandatoryMap.put("txtPeriodTransDoubtfulAssets1", new Boolean(false));
        mandatoryMap.put("txtPeriodTransDoubtfulAssets2", new Boolean(false));
        mandatoryMap.put("txtPeriodTransDoubtfulAssets3", new Boolean(false));
        mandatoryMap.put("txtMinPeriodsArrears", new Boolean(false));
        mandatoryMap.put("txtFolioChargesAcc", new Boolean(false));
        mandatoryMap.put("rdoIsLimitDefnAllowed_Yes", new Boolean(false));
        
        mandatoryMap.put("cboCommodityCode", new Boolean(false));
        mandatoryMap.put("cboGuaranteeCoverCode", new Boolean(false));
        mandatoryMap.put("cboSectorCode", new Boolean(false));
        mandatoryMap.put("cboHealthCode", new Boolean(false));
        mandatoryMap.put("cboTypeFacility", new Boolean(false));
        mandatoryMap.put("cboPurposeCode", new Boolean(false));
        mandatoryMap.put("cboIndusCode", new Boolean(false));
        mandatoryMap.put("cbo20Code", new Boolean(false));
        mandatoryMap.put("cboRefinancingInsti", new Boolean(false));
        mandatoryMap.put("cboGovtSchemeCode", new Boolean(false));
        mandatoryMap.put("cboSecurityDeails", new Boolean(false));
        mandatoryMap.put("chkDirectFinance", new Boolean(false));
        mandatoryMap.put("chkECGC", new Boolean(false));
        mandatoryMap.put("chkQIS", new Boolean(false));
        mandatoryMap.put("cboNoticeType",new Boolean(false));
        mandatoryMap.put("cboIssueAfter",new Boolean(false));
        mandatoryMap.put("txtNoticeChargeAmt",new Boolean(false));
        mandatoryMap.put("txtPostageChargeAmt",new Boolean(false));
        mandatoryMap.put("btnNotice_Charge_New",new Boolean(false));
        mandatoryMap.put("btnNotice_Charge_Save",new Boolean(false));
        mandatoryMap.put("btnNotice_Charge_Delete",new Boolean(false));
        mandatoryMap.put("txtNoticeCharges", new Boolean(false));
        mandatoryMap.put("txtPostageCharges",new Boolean(false));
        
    }

    public HashMap getMandatoryHashMap(){
        return this.mandatoryMap;
    }
}

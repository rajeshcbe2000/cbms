/*
 *
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductUI.java
 *
 * Created on November 20, 2003, 11:45 AM
 */
package com.see.truetransact.ui.product.loan;

import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.clientproxy.ProxyParameters;
import java.util.List;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;

import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.ui.product.advances.AdvancesProductUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author rahul @modified : Sunil Added Edit Locking - 08-07-2005
 */
public class NewLoanProductUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    private HashMap mandatoryMap;
    NewLoanProductOB observable;
    //    final NewLoanProductRB resourceBundle = new NewLoanProductRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.NewLoanProductRB", ProxyParameters.LANGUAGE);
    final NewLoanProductMRB objMandatoryRB = new NewLoanProductMRB();
    int viewType = -1;
    final int AUTHORIZE = 100, EDIT = 0, COPY = 23, DELETE = 1, ACCHEAD = 2, ACCCLOSCHARGES = 3, MISCSERVCHARGES = 4, STATEMENTCHARGES = 5, ACCDEBITINTER = 6;
    final int PENALINTER = 7, ACCCREDITINTER = 8, EXPIRYINTER = 9, CHERETURNCHARGES_OUT = 10, CHERETURNCHARGES_IN = 11;
    final int FOLIOCHARGESACC = 12, COMMITCHARGES = 13, PROCESSINGCHARGES = 14, INTERRECEIVABLEGLACCHEAD = 15, NOTICECHARGES = 16, POSTAGECHARGES = 17, VIEW = 350, DEBIT_DISCOUNT_ACHD = 31;
    final int INTPAYABLEACCHEAD = 18, LEGALCHARGES = 19, ARBITRARYCHARGES = 20, INSURANCECHARGES = 21, EXECUTION_DECREE_CHARGES = 22, OTHERCHARGES = 40;
    final int ARCCOSTHEAD = 25, ARCEXPENSEHEAD = 26, EACOSTHEAD = 27, EAEXPENSEHEAD = 28, EPCOSTHEAD = 29, EPEXPENSETHEAD = 30, REBATEACCHEAD = 36, STAMPADVANCESHEAD = 37, ADVERTISEMENTHEAD = 38, ARCEPSUSPENCEACHD = 39, NOTICEADVANCESHEAD = 41;
    final int PENALWAIVEOFF = 42, PRINCIPALWAIVEOFF = 45, NOTICEWAIVEOFF = 46, SUSPENSEPRODUCTCREDIT = 47, SUSPENSEPRODUCTDEBIT = 48;
    final int ARCWAIVEOFF = 49, MISWAIVEOFF = 50, INSUWAIVEOFF = 51, LEGALWAIVEOFF = 52, DECREEWAIVEOFF = 53, ARBITARYWAIVEOFF = 54, ADVERWAIVEOFF = 55, EPWAIVEOFF = 56, POSTAGEWAIVEOFF = 57;
    final int OVERDUEINTWAIVEOFF = 60, OVERDUEINT = 61;
    final int RECOVERYWAIVEOFF = 66, RECOVERYCHARGE = 67, MEASUREMENTWAIVEOFF = 68, MEASUREMENTCHARGE = 69;
    final int KOLEFIELDOPERATIONWAIVEOFF = 70, KOLEFIELDOPERATION = 71, KOLEFIELDEXPENSEWAIVEOFF = 72, KOLEFIELDEXPENSE = 73;
    final String ABSOLUTE = "Absolute";
    final String PERCENT = "Percent";
    final int ACCOUNTHEADINDEX = 3;
    private boolean tableDocumentsClicked = false;
    private boolean tableNoticeChargeClicked = false;
    private boolean isFilled = false;
    CButtonGroup rdoPrincipalInterestWaiverGroup;
    //Logger
    private final static Logger log = Logger.getLogger(NewLoanProductUI.class);
    private Date currDt = null;

    /**
     * Creates new form LoanProductUI
     */
    public NewLoanProductUI() {
        initComponents();
        initPanAdditionalDetails();
        rdoIsInterestFirst = new com.see.truetransact.uicomponent.CButtonGroup();
        initSetup();
    }

    private void initSetup() {
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setMaxLenths();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();  //__ To enable and disable the main buttons and the menu items...
        enableDisableButtons();    //__ To enable and disable the buttons in Account and Account-head
        enableDisableCharge_SaveDelete();  //__ To enable and disable the fields in the table...
        btnCharge_New.setEnabled(false);   //__ to disable the New button in table
        enableDisableDocuments_NewSaveDelete(false);  //__ Disable New Save Delete Buttons in Documents
        observable.resetStatus();          //__ To reset the status
        enableDisbleNotice_NewSaveDeleter(false); // disable the button for notice charge
        panGroupLoan.setVisible(false);
        setInvisible();
        setHelpMessage();
        tableDocumentsClicked = false;
        enableDisableTableDocuments(true);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panLoanProduct);
        //__ To reset the value of the visited tabs...
        tabLoanProduct.resetVisits();
        enableDisableAppropriateTrans(false);
//        panLoanAgainstDeposit.setVisible(false);//demo purpose
        panGldRenewal.setVisible(false);//only enable  for gold loan

        if (CommonConstants.SAL_REC_MODULE.equals("Y")) {
            lblSalaryRecovery.setVisible(true);
            panSalRec.setVisible(true);
            rdoSalRec_Yes.setSelected(true);
        } else {
            lblSalaryRecovery.setVisible(false);
            panSalRec.setVisible(false);
        }
        btnCreateHead.setEnabled(false);
        rdoOverDueWaiverAllowed_No.setSelected(true);
        rdoOverDueWaiverAllowed_Yes.setSelected(true);
        txtRebateLoanIntPercent.setEnabled(false);//10-01-2020
    }

    private void setObservable() {// Creates the instance of OB
        //System.out.println("Inside setObservable");
        observable = NewLoanProductOB.getInstance();
        observable.addObserver(this);
    }

    /**
     * To enable disable table Documents
     */
    private void enableDisableTableDocuments(boolean flag) {
        tblDocuments.setEnabled(flag);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        txtFixedMargin.setName("txtFixedMargin");
        chkFixed.setName("chkFixed");
        btnIntPayableAccount.setName("btnIntPayableAccount");
        btnAccClosCharges.setName("btnAccClosCharges");
        btnAccCreditInter.setName("btnAccCreditInter");
        btnAccDebitInter.setName("btnAccDebitInter");
        btnAccHead.setName("btnAccHead");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnCharge_Delete.setName("btnCharge_Delete");
        btnCharge_New.setName("btnCharge_New");
        btnCharge_Save.setName("btnCharge_Save");
        btnCheReturnChargest_In.setName("btnCheReturnChargest_In");
        btnCheReturnChargest_Out.setName("btnCheReturnChargest_Out");
        btnClose.setName("btnClose");
        btnCommitCharges.setName("btnCommitCharges");
        btnDelete.setName("btnDelete");
        btnPenalWaiveoffHead.setName("btnPenalWaiveoffHead");
        btnPrincipleWaiveoffHead.setName("btnPrincipleWaiveoffHead");
        btnDocumentDelete.setName("btnDocumentDelete");
        btnDocumentNew.setName("btnDocumentNew");
        btnDocumentSave.setName("btnDocumentSave");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnExpiryInter.setName("btnExpiryInter");
        btnFolioChargesAcc.setName("btnFolioChargesAcc");
        btnMiscServCharges.setName("btnMiscServCharges");
        btnNew.setName("btnNew");
        btnPenalInter.setName("btnPenalInter");
        btnPrint.setName("btnPrint");
        btnProcessingCharges.setName("btnProcessingCharges");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnStatementCharges.setName("btnStatementCharges");
        btnNotice_Charge_New.setName("btnNotice_Charge_New");
        btnNotice_Charge_Save.setName("btnNotice_Charge_Save");
        btnNotice_Charge_Delete.setName("btnNotice_Charge_Delete");
        cbo20Code.setName("cbo20Code");
        cboCharge_Limit2.setName("cboCharge_Limit2");
        cboCharge_Limit3.setName("cboCharge_Limit3");
        cboCommodityCode.setName("cboCommodityCode");
        cboDebInterCalcFreq.setName("cboDebInterCalcFreq");
        cboDebitInterAppFreq.setName("cboDebitInterAppFreq");
        cboDebitInterComFreq.setName("cboDebitInterComFreq");
        cboDebitInterRoundOff.setName("cboDebitInterRoundOff");
        lblInterestRepaymentFreq.setName("lblInterestRepaymentFreq");
        cboInterestRepaymentFreq.setName("cboInterestRepaymentFreq");
        cboDebitProdRoundOff.setName("cboDebitProdRoundOff");
        cboDocumentType.setName("cboDocumentType");
        cboFolioChargesAppFreq.setName("cboFolioChargesAppFreq");
        cboGovtSchemeCode.setName("cboGovtSchemeCode");
        cboGuaranteeCoverCode.setName("cboGuaranteeCoverCode");
        cboHealthCode.setName("cboHealthCode");
        cboIncompleteFolioRoundOffFreq.setName("cboIncompleteFolioRoundOffFreq");
        cboIndusCode.setName("cboIndusCode");
        cboLoanPeriodMul.setName("cboLoanPeriodMul");
        cboMaxPeriod.setName("cboMaxPeriod");
        cboMinPeriod.setName("cboMinPeriod");
        cboMinPeriodsArrears.setName("cboMinPeriodsArrears");
        cboOperatesLike.setName("cboOperatesLike");
        cboProdCategory.setName("cboProdCategory");
        cboPeriodAfterWhichTransNPerformingAssets.setName("cboPeriodAfterWhichTransNPerformingAssets");
        cboPeriodTranSStanAssets.setName("cboPeriodTranSStanAssets");
        cboPeriodTransDoubtfulAssets1.setName("cboPeriodTransDoubtfulAssets1");
        cboPeriodTransDoubtfulAssets2.setName("cboPeriodTransDoubtfulAssets2");
        cboPeriodTransDoubtfulAssets3.setName("cboPeriodTransDoubtfulAssets3");
        cboPeriodTransLossAssets.setName("cboPeriodTransLossAssets");
        cboProdCurrency.setName("cboProdCurrency");
        cboProductFreq.setName("cboProductFreq");
        cboPurposeCode.setName("cboPurposeCode");
        cboRefinancingInsti.setName("cboRefinancingInsti");
        cboSectorCode.setName("cboSectorCode");
        cboSecurityDeails.setName("cboSecurityDeails");
        cboToCollectFolioCharges.setName("cboToCollectFolioCharges");
        cboTypeFacility.setName("cboTypeFacility");
        cbocalcType.setName("cbocalcType");
        chkDirectFinance.setName("chkDirectFinance");
        chkECGC.setName("chkECGC");
        chkQIS.setName("chkQIS");
        cboNoticeType.setName("cboNoticeType");
        cboIssueAfter.setName("cboIssueAfter");
        cboPeriodFreq.setName("cboPeriodFreq");
        cboPeriodFreq1.setName("cboPeriodFreq1");
        cboPrematureIntCalAmt.setName("cboPrematureIntCalAmt");
        cboDepositRoundOff.setName("cboDepositRoundOff");
        lblDepositRoundOff.setName("lblDepositRoundOff");
        lblPrematureCloserMinCalcPeriod.setName("lblPrematureCloserMinCalcPeriod");
        lblPrematureIntCalcAmt.setName("lblPrematureIntCalcAmt");
        lblBillbyBill.setName("lblBillbyBill");
        lblNoticeType.setName("lblNoticeType");
        lblIssueAfter.setName("lblIssueAfter");
        lblNoticeChargeAmt.setName("lblNoticeChargeAmt");
        lblPostageAmt.setName("lblPostageAmt");
        lblEligibleDepositAmtForLoan.setName("lblEligibleDepositAmtForLoan");
        lblPenalWaiveoffHead.setName("lblPenalWaiveoffHead");
        lblPrincipleWaiveoffHead.setName("lblPrincipleWaiveoffHead");
        lbStatementCharges.setName("lbStatementCharges");
        lbl20Code.setName("lbl20Code");
        lblATMcardIssued.setName("lblATMcardIssued");
        lblAccClosCharges.setName("lblAccClosCharges");
        lblIntPayableAccount.setName("lblIntPayableAccount");
        lblAccClosingCharges.setName("lblAccClosingCharges");
        lblAccCreditInter.setName("lblAccCreditInter");
        lblAccDebitInter.setName("lblAccDebitInter");
        lblAccHead.setName("lblAccHead");
        lblApplicableInter.setName("lblApplicableInter");
        lblApplicableInterPer.setName("lblApplicableInterPer");
        lblCharge_1.setName("lblCharge_1");
        lblCharge_2.setName("lblCharge_2");
        lblCheReturnChargest_In.setName("lblCheReturnChargest_In");
        lblCheReturnChargest_Out.setName("lblCheReturnChargest_Out");
        lblCommitCharges.setName("lblCommitCharges");
        lblCommitmentCharge.setName("lblCommitmentCharge");
        lblCommodityCode.setName("lblCommodityCode");
        lblCreditCardIssued.setName("lblCreditCardIssued");
        lblDebInterCalcFreq.setName("lblDebInterCalcFreq");
        lblDebitInterAppFreq.setName("lblDebitInterAppFreq");
        lblDebitInterComFreq.setName("lblDebitInterComFreq");
        lblDebitInterRoundOff.setName("lblDebitInterRoundOff");
        lblDebitProdRoundOff.setName("lblDebitProdRoundOff");
        lblDirectFinance.setName("lblDirectFinance");
        lblDocumentDesc.setName("lblDocumentDesc");
        lblDocumentNo.setName("lblDocumentNo");
        lblDocumentType.setName("lblDocumentType");
        lblECGC.setName("lblECGC");
        lblExpiryInter.setName("lblExpiryInter");
        lblExposureLimit_Policy.setName("lblExposureLimit_Policy");
        lblExposureLimit_Policy_Per.setName("lblExposureLimit_Policy_Per");
        lblExposureLimit_Prud.setName("lblExposureLimit_Prud");
        lblExposureLimit_Prud_Per.setName("lblExposureLimit_Prud_Per");
        lblFolioChargesAcc.setName("lblFolioChargesAcc");
        lblFolioChargesAppFreq.setName("lblFolioChargesAppFreq");
        lblFolioChargesAppl.setName("lblFolioChargesAppl");
        lblGovtSchemeCode.setName("lblGovtSchemeCode");
        lblGuaranteeCoverCode.setName("lblGuaranteeCoverCode");
        lblHealthCode.setName("lblHealthCode");
        lblIncompleteFolioRoundOffFreq.setName("lblIncompleteFolioRoundOffFreq");
        lblIndusCode.setName("lblIndusCode");
        lblIsAnyBranBankingAllowed.setName("lblIsAnyBranBankingAllowed");
        lblIsDebitInterUnderClearing.setName("lblIsDebitInterUnderClearing");
        lblIsInterestFirst.setName("lblIsInterestFirst");
        lblIsLimitDefnAllowed.setName("lblIsLimitDefnAllowed");
        lblIsStaffAccOpened.setName("lblIsStaffAccOpened");
        lblLastAccNum.setName("lblLastAccNum");
        lblLastFolioChargesAppl.setName("lblLastFolioChargesAppl");
        lblLastInterAppDate.setName("lblLastInterAppDate");
        lblLastInterCalcDate.setName("lblLastInterCalcDate");
        lblLimit.setName("lblLimit");
        lblLimitExpiryInter.setName("lblLimitExpiryInter");
        lblLimits.setName("lblLimits");
        lblLoanPeriodMul.setName("lblLoanPeriodMul");
        lblMinAmtLoan.setName("lblMinAmtLoan");
        lblMaxDebitInterAmt.setName("lblMaxDebitInterAmt");
        lblMaxDebitInterRate.setName("lblMaxDebitInterRate");
        lblMaxDebitInterRate_Per.setName("lblMaxDebitInterRate_Per");
        lblMaxPeriod.setName("lblMaxPeriod");
//        lblPrincipalWaiverAllowed1.setName("lblMinAmtLoan");
        lblMinDebitInterAmt.setName("lblMinDebitInterAmt");
        lblMinDebitInterRate.setName("lblMinDebitInterRate");
        lblMinDebitInterRate_Per.setName("lblMinDebitInterRate_Per");
        lblMinInterDebited.setName("lblMinInterDebited");
        lblMinPeriod.setName("lblMinPeriod");
        lblMinPeriodsArrears.setName("lblMinPeriodsArrears");
        lblMiscSerCharges.setName("lblMiscSerCharges");
        lblMiscServCharges.setName("lblMiscServCharges");
        lblMobileBanlingClient.setName("lblMobileBanlingClient");
        lblMsg.setName("lblMsg");
        lblNextFolioDDate.setName("lblNextFolioDDate");
        lblNoEntriesPerFolio.setName("lblNoEntriesPerFolio");
        lblNumPatternFollowed.setName("lblNumPatternFollowed");
        lblProdCategory.setName("lblProdCategory");
        lblPLRAppForm.setName("lblPLRAppForm");
        lblPLRApplExistingAcc.setName("lblPLRApplExistingAcc");
        lblPLRApplNewAcc.setName("lblPLRApplNewAcc");
        lblPLRRate.setName("lblPLRRate");
        lblPLRRateAppl.setName("lblPLRRateAppl");
        lblPLRRate_Per.setName("lblPLRRate_Per");
        lblPenalAppl.setName("lblPenalAppl");
        lblPenalInter.setName("lblPenalInter");
        lblPenalInterRate.setName("lblPenalInterRate");
        lblPenalInterRate_Per.setName("lblPenalInterRate_Per");
        lblPeriodAfterWhichTransNPerformingAssets.setName("lblPeriodAfterWhichTransNPerformingAssets");
        lblPeriodTranSStanAssets.setName("lblPeriodTranSStanAssets");
        lblPeriodTransDoubtfulAssets1.setName("lblPeriodTransDoubtfulAssets1");
        lblPeriodTransDoubtfulAssets2.setName("lblPeriodTransDoubtfulAssets2");
        lblPeriodTransDoubtfulAssets3.setName("lblPeriodTransDoubtfulAssets3");
        lblPeriodTransLossAssets.setName("lblPeriodTransLossAssets");
        lblPlrApplAccSancForm.setName("lblPlrApplAccSancForm");
        lblProcessCharges.setName("lblProcessCharges");
        lblProcessingCharges.setName("lblProcessingCharges");
        lblProdCurrency.setName("lblProdCurrency");
        lblProductDesc.setName("lblProductDesc");
        lblProductFreq.setName("lblProductFreq");
        lblProductID.setName("lblProductID");
        lblProvisionDoubtfulAssets1.setName("lblProvisionDoubtfulAssets1");
        lblProvisionDoubtfulAssets2.setName("lblProvisionDoubtfulAssets2");
        lblProvisionDoubtfulAssets3.setName("lblProvisionDoubtfulAssets3");
        lblProvisionDoubtfulAssetsPer1.setName("lblProvisionDoubtfulAssetsPer1");
        lblProvisionDoubtfulAssetsPer2.setName("lblProvisionDoubtfulAssetsPer2");
        lblProvisionDoubtfulAssetsPer3.setName("lblProvisionDoubtfulAssetsPer3");
        lblProvisionLossAssets.setName("lblProvisionLossAssets");
        lblProvisionLossAssetsPer.setName("lblProvisionLossAssetsPer");
        lblProvisionStandardAssetss.setName("lblProvisionStandardAssetss");
        lblProvisionStandardAssetssPer.setName("lblProvisionStandardAssetssPer");
        lblProvisionsStanAssets.setName("lblProvisionsStanAssets");
        lblProvisionsStanAssetsPer.setName("lblProvisionsStanAssetsPer");
        lblPurposeCode.setName("lblPurposeCode");
        lblQIS.setName("lblQIS");
        lblRangeFrom.setName("lblRangeFrom");
        lblRangeTo.setName("lblRangeTo");
        lblRateAmt.setName("lblRateAmt");
        lblRatePerFolio.setName("lblRatePerFolio");
        lblRefinancingInsti.setName("lblRefinancingInsti");
        lblSectorCode.setName("lblSectorCode");
        lblSecurityDeails.setName("lblSecurityDeails");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblSpaces.setName("lblSpaces");
        lblStatCharges.setName("lblStatCharges");
        lblStatChargesRate.setName("lblStatChargesRate");
        lblStatus1.setName("lblStatus1");
        lblSubsidy.setName("lblSubsidy");
        lblSunbsidy.setName("lblSunbsidy");
        lblToChargeOn.setName("lblToChargeOn");
        lblToCollectFolioCharges.setName("lblToCollectFolioCharges");
        lblTypeFacility.setName("lblTypeFacility");
        lblcalcType.setName("lblcalcType");
        lbldebitInterCharged.setName("lbldebitInterCharged");
        lblNoticeCharges.setName("lblNoticeCharges");
        lblPostageCharges.setName("lblPostageCharges");
        lblPenalDue.setName("lblPenalDue");
        lblcalendar.setName("lblcalendar");
        lblasAndWhenCustomer.setName("lblasAndWhenCustomer");
        lblLegalCharges.setName("lblLegalCharges");
        lblArbitraryCharges.setName("lblArbitraryCharges");
        lblInsuranceCharges.setName("lblInsuranceCharges");
        lblExecutionDecreeCharges.setName("lblExecutionDecreeCharges");
//        lbl_av_Balance.setName("lbl_av_Balance");
//        lblNoOfFreeFolio.setName("lblNoOfFreeFolio");

        mbrLoanProduct.setName("mbrLoanProduct");
        panPreMatIntCalcPeriod.setName("panPreMatIntCalcPeriod");
        panAccCharges.setName("panAccCharges");
        panAccount.setName("panAccount");
        panAccountHead.setName("panAccountHead");
        panApplicableInter.setName("panApplicableInter");
        panButton.setName("panButton");
        panButtons.setName("panButtons");
        panCharge.setName("panCharge");
        panNoticeCharges_Table.setName("panNoticeCharges_Table");
        panNoticeButton.setName("panNoticeButton");
        panCharge_1.setName("panCharge_1");
        panCharge_2.setName("panCharge_2");
        panCharge_ProcCommit.setName("panCharge_ProcCommit");
        panCharges.setName("panCharges");
        panCharges_Table.setName("panCharges_Table");
        panChequeReturnCharges.setName("panChequeReturnCharges");
        panNoticecharge_Amt.setName("panNoticecharge_Amt");
        panClassDetails_Details.setName("panClassDetails_Details");
        panCode.setName("panCode");
        panCode2.setName("panCode2");
        panCommitCharges.setName("panCommitCharges");
        panCommitmentCharge.setName("panCommitmentCharge");
        panDocumentFields.setName("panDocumentFields");
        panDocumentTable.setName("panDocumentTable");
        panDocuments.setName("panDocuments");
        panExposureLimit_Policy.setName("panExposureLimit_Policy");
        panExposureLimit_Prud.setName("panExposureLimit_Prud");
        panFolio.setName("panFolio");
        panFolioChargesAppl.setName("panFolioChargesAppl");
        panFolio_Date.setName("panFolio_Date");
        panFolio_Freq.setName("panFolio_Freq");
        panInterCalculation.setName("panInterCalculation");
        panInterReceivable.setName("panInterReceivable");
        panInterReceivable_Debit.setName("panInterReceivable_Debit");
        panInterReceivable_PLR.setName("panInterReceivable_PLR");
        panIsDebitInterUnderClearing.setName("panIsDebitInterUnderClearing");
        panIsLimitDefnAllowed.setName("panIsLimitDefnAllowed");
        panIsStaffAccOpened.setName("panIsStaffAccOpened");
        panLimitExpiryInter.setName("panLimitExpiryInter");
        panLoanProduct.setName("panLoanProduct");
        panMaxDebitInterRate.setName("panMaxDebitInterRate");
        panMaxPeriod.setName("panMaxPeriod");
        panNoticeCharges.setName("panNoticeCharges");
        panMinDebitInterRate.setName("panMinDebitInterRate");
        panMinInterDebited.setName("panMinInterDebited");
        panMinPeriod.setName("panMinPeriod");
        panMinPeriodsArrears.setName("panMinPeriodsArrears");
        panNonPerAssets.setName("panNonPerAssets");
        panPLRApplExistingAcc.setName("panPLRApplExistingAcc");
        panPLRApplNewAcc.setName("panPLRApplNewAcc");
        panPLRRate.setName("panPLRRate");
        panPLRRateAppl.setName("panPLRRateAppl");
        panPenalAppl.setName("panPenalAppl");
        panPenalInterRate.setName("panPenalInterRate");
        panPeriodAfterWhichTransNPerformingAssets.setName("panPeriodAfterWhichTransNPerformingAssets");
        panPeriodTranSStanAssets.setName("panPeriodTranSStanAssets");
        panPeriodTransDoubtfulAssets1.setName("panPeriodTransDoubtfulAssets1");
        panPeriodTransDoubtfulAssets2.setName("panPeriodTransDoubtfulAssets2");
        panPeriodTransDoubtfulAssets3.setName("panPeriodTransDoubtfulAssets3");
        panPeriodTransLossAssets.setName("panPeriodTransLossAssets");
        panProcessCharge.setName("panProcessCharge");
        panProcessCharges.setName("panProcessCharges");
        panSpeItems.setName("panSpeItems");
        panSpecial_NonPerfoormingAssets.setName("panSpecial_NonPerfoormingAssets");
        panStatCharges.setName("panStatCharges");
        panStateCharges.setName("panStateCharges");
        panStatus.setName("panStatus");
//        panSubsidy.setName("panSubsidy");
        panToChargeOn.setName("panToChargeOn");
        pancharge_Amt.setName("pancharge_Amt");
        panldebitInterCharged.setName("panldebitInterCharged");
        panToChargeType.setName("panToChargeType");
        panasAndWhenCustomer.setName("panasAndWhenCustomer");
        panCalendarFrequency.setName("panCalendarFrequency");
        panBillbyBill.setName("panBillbyBill");
        panPenalAppl_Due.setName("panPenalAppl_Due");
        panLoanAgainstDeposit.setName("panLoanAgainstDeposit");
        panChargeTypes.setName("panChargeTypes");

        panAppropriateTransaction.setName("panAppropriateTransaction");
        lstAvailableTypeTransaction.setName("lstAvailableTypeTransaction");
        lstSelectedPriorityTransaction.setName("lstSelectedPriorityTransaction");
        btnTransactionRemove.setName("btnTransactionRemove");
        btnTransactionAdd.setName("btnTransactionAdd");
        lblAvailableTransaction.setName("lblAvailableTransaction");
        lblSelectedTransaction.setName("lblSelectedTransaction");

        rdoATMcardIssued_No.setName("rdoATMcardIssued_No");
        rdoATMcardIssued_Yes.setName("rdoATMcardIssued_Yes");
        rdoCommitmentCharge_No.setName("rdoCommitmentCharge_No");
        rdoCommitmentCharge_Yes.setName("rdoCommitmentCharge_Yes");
        rdoCreditCardIssued_No.setName("rdoCreditCardIssued_No");
        rdoCreditCardIssued_Yes.setName("rdoCreditCardIssued_Yes");
        rdoFolioChargesAppl_No.setName("rdoFolioChargesAppl_No");
        rdoFolioChargesAppl_Yes.setName("rdoFolioChargesAppl_Yes");
        rdoToChargeType_Credit.setName("rdoToChargeType_Credit");
        rdoToChargeType_Debit.setName("rdoToChargeType_Debit");
        rdoToChargeType_Both.setName("rdoToChargeType_Both");
        rdoIsAnyBranBankingAllowed_No.setName("rdoIsAnyBranBankingAllowed_No");
        rdoIsAnyBranBankingAllowed_Yes.setName("rdoIsAnyBranBankingAllowed_Yes");
        rdoIsDebitInterUnderClearing_No.setName("rdoIsDebitInterUnderClearing_No");
        rdoIsDebitInterUnderClearing_Yes.setName("rdoIsDebitInterUnderClearing_Yes");
        rdoIsLimitDefnAllowed_No.setName("rdoIsLimitDefnAllowed_No");
        rdoIsLimitDefnAllowed_Yes.setName("rdoIsLimitDefnAllowed_Yes");
        rdoIsInterestFirst_No.setName("rdoIsInterestFirst_No");
        rdoIsInterestFirst_Yes.setName("rdoIsInterestFirst_Yes");
        rdoIsCreditAllowed_Yes.setName("rdoIsCreditAllowed_Yes");
        rdoIsCreditAllowed_No.setName("rdoIsCreditAllowed_No");
        rdoIsStaffAccOpened_No.setName("rdoIsStaffAccOpened_No");
        rdoIsStaffAccOpened_Yes.setName("rdoIsStaffAccOpened_Yes");
        rdoLimitExpiryInter_No.setName("rdoLimitExpiryInter_No");
        rdoLimitExpiryInter_Yes.setName("rdoLimitExpiryInter_Yes");
        rdoMobileBanlingClient_No.setName("rdoMobileBanlingClient_No");
        rdoMobileBanlingClient_Yes.setName("rdoMobileBanlingClient_Yes");
        rdoPLRApplExistingAcc_No.setName("rdoPLRApplExistingAcc_No");
        rdoPLRApplExistingAcc_Yes.setName("rdoPLRApplExistingAcc_Yes");
        rdoPLRApplNewAcc_No.setName("rdoPLRApplNewAcc_No");
        rdoPLRApplNewAcc_Yes.setName("rdoPLRApplNewAcc_Yes");
        rdoPLRRateAppl_No.setName("rdoPLRRateAppl_No");
        rdoPLRRateAppl_Yes.setName("rdoPLRRateAppl_Yes");
        rdoPenalAppl_No.setName("rdoPenalAppl_No");
        rdoPenalAppl_Yes.setName("rdoPenalAppl_Yes");
        rdoProcessCharges_No.setName("rdoProcessCharges_No");
        rdoProcessCharges_Yes.setName("rdoProcessCharges_Yes");
        rdoStatCharges_No.setName("rdoStatCharges_No");
        rdoStatCharges_Yes.setName("rdoStatCharges_Yes");
        rdoSubsidy_No.setName("rdoSubsidy_No");
        rdoSubsidy_Yes.setName("rdoSubsidy_Yes");
        rdoToChargeOn_Both.setName("rdoToChargeOn_Both");
        rdoToChargeOn_Man.setName("rdoToChargeOn_Man");
        rdoToChargeOn_Sys.setName("rdoToChargeOn_Sys");
        rdoldebitInterCharged_No.setName("rdoldebitInterCharged_No");
        rdoldebitInterCharged_Yes.setName("rdoldebitInterCharged_Yes");
        rdoasAndWhenCustomer_Yes.setName("rdoasAndWhenCustomer_Yes");
        rdoasAndWhenCustomer_No.setName("rdoasAndWhenCustomer_No");
        rdocalendarFrequency_Yes.setName("rdocalendarFrequency_Yes");
        rdocalendarFrequency_No.setName("rdocalendarFrequency_No");
        rdobill_Yes.setName("rdobill_Yes");
        rdobill_No.setName("rdobill_No");
        sptAcc.setName("sptAcc");
        sptCharges_Vert.setName("sptCharges_Vert");
        sptCharges_Vert2.setName("sptCharges_Vert2");
        sptCharges_Vert3.setName("sptCharges_Vert3");
        sptClassification_vertical.setName("sptClassification_vertical");
        sptInterReceivable.setName("sptInterReceivable");
        srpCharges.setName("srpCharges");
        srpDocuments.setName("srpDocuments");
        tabLoanProduct.setName("tabLoanProduct");
        tblCharges.setName("tblCharges");
        tblDocuments.setName("tblDocuments");
        tblNoticeCharges.setName("tblNoticeCharges");
        tdtLastFolioChargesAppl.setName("tdtLastFolioChargesAppl");
        tdtLastInterAppDate.setName("tdtLastInterAppDate");
        tdtLastInterCalcDate.setName("tdtLastInterCalcDate");
        tdtNextFolioDDate.setName("tdtNextFolioDDate");
        tdtPLRAppForm.setName("tdtPLRAppForm");
        tdtPlrApplAccSancForm.setName("tdtPlrApplAccSancForm");
        txtAccClosCharges.setName("txtAccClosCharges");
        txtArcCostAccount.setName("txtArcCostAccount");
        txtArcExpenseAccount.setName("txtArcExpenseAccount");
        txtEaCostAccount.setName("txtEaCostAccount");
        txtPenalWaiveoffHead.setName("txtPenalWaiveoffHead");
        txtPrincipleWaiveoffHead.setName("txtPrincipleWaiveoffHead");
        txtEaExpenseAccount.setName("txtEaExpenseAccount");
        txtEpCostAccount.setName("txtEpCostAccount");
        txtEpExpenseAccount.setName("txtEpExpenseAccount");
        lblArcCostAccount.setName("lblArcCostAccount");
        lblArcExpenseAccount.setName("lblArcExpenseAccount");
        lblEaCostAccount.setName("lblEaCostAccount");
        lblEaExpenseAccount.setName("lblEaExpenseAccount");
        lblEpCostAccount.setName("lblEpCostAccount");
        lblEpExpenseAccount.setName("lblEpExpenseAccount");
        panCaseHead.setName("panCaseHead");
        txtIntPayableAccount.setName("txtIntPayableAccount");
        txtAccClosingCharges.setName("txtAccClosingCharges");
        txtAccCreditInter.setName("txtAccCreditInter");
        txtAccDebitInter.setName("txtAccDebitInter");
        txtAccHead.setName("txtAccHead");
        txtApplicableInter.setName("txtApplicableInter");
        txtCharge_Limit2.setName("txtCharge_Limit2");
        txtCharge_Limit3.setName("txtCharge_Limit3");
        txtCheReturnChargest_In.setName("txtCheReturnChargest_In");
        txtCheReturnChargest_Out.setName("txtCheReturnChargest_Out");
        txtCommitCharges.setName("txtCommitCharges");
        txtDocumentDesc.setName("txtDocumentDesc");
        txtDocumentNo.setName("txtDocumentNo");
        txtExpiryInter.setName("txtExpiryInter");
//        txtAvBalance.setName("txtAvBalance");
//        txtNoOfFreeFolio.setName("txtNoOfFreeFolio");
        txtExposureLimit_Policy.setName("txtExposureLimit_Policy");
        txtExposureLimit_Policy2.setName("txtExposureLimit_Policy2");
        txtExposureLimit_Prud.setName("txtExposureLimit_Prud");
        txtExposureLimit_Prud2.setName("txtExposureLimit_Prud2");
        txtFolioChargesAcc.setName("txtFolioChargesAcc");
        txtLastAccNum.setName("txtLastAccNum");
        txtMaxAmtLoan.setName("txtMaxAmtLoan");
        txtMaxDebitInterAmt.setName("txtMaxDebitInterAmt");
        txtMaxDebitInterRate.setName("txtMaxDebitInterRate");
        txtMaxPeriod.setName("txtMaxPeriod");
        txtMinAmtLoan.setName("txtMinAmtLoan");
        txtMinDebitInterAmt.setName("txtMinDebitInterAmt");
        txtMinDebitInterRate.setName("txtMinDebitInterRate");
        txtMinInterDebited.setName("txtMinInterDebited");
        txtMinPeriod.setName("txtMinPeriod");
        txtMinPeriodsArrears.setName("txtMinPeriodsArrears");
        txtMiscSerCharges.setName("txtMiscSerCharges");
        txtMiscServCharges.setName("txtMiscServCharges");
        txtNoEntriesPerFolio.setName("txtNoEntriesPerFolio");
        txtNumPatternFollowed.setName("txtNumPatternFollowed");
        txtNumPatternFollowedSuffix.setName("txtNumPatternFollowedSuffix");
        txtPLRRate.setName("txtPLRRate");
        txtPenalInter.setName("txtPenalInter");
        txtPenalInterRate.setName("txtPenalInterRate");
        txtPeriodAfterWhichTransNPerformingAssets.setName("txtPeriodAfterWhichTransNPerformingAssets");
        txtPeriodTranSStanAssets.setName("txtPeriodTranSStanAssets");
        txtPeriodTransDoubtfulAssets1.setName("txtPeriodTransDoubtfulAssets1");
        txtPeriodTransDoubtfulAssets2.setName("txtPeriodTransDoubtfulAssets2");
        txtPeriodTransDoubtfulAssets3.setName("txtPeriodTransDoubtfulAssets3");
        txtPeriodTransLossAssets.setName("txtPeriodTransLossAssets");
        txtProcessingCharges.setName("txtProcessingCharges");
        txtProductDesc.setName("txtProductDesc");
        txtProductID.setName("txtProductID");
        txtProvisionDoubtfulAssets1.setName("txtProvisionDoubtfulAssets1");
        txtProvisionDoubtfulAssets2.setName("txtProvisionDoubtfulAssets2");
        txtProvisionDoubtfulAssets3.setName("txtProvisionDoubtfulAssets3");
        txtProvisionLossAssets.setName("txtProvisionLossAssets");
        txtProvisionStandardAssetss.setName("txtProvisionStandardAssetss");
        txtProvisionsStanAssets.setName("txtProvisionsStanAssets");
        txtRangeFrom.setName("txtRangeFrom");
        txtRangeTo.setName("txtRangeTo");
        txtRateAmt.setName("txtRateAmt");
        txtRatePerFolio.setName("txtRatePerFolio");
        txtStatChargesRate.setName("txtStatChargesRate");
        txtStatementCharges.setName("txtStatementCharges");
        txtNoticeChargeAmt.setName("txtNoticeChargeAmt");
        txtPostageAmt.setName("txtPostageAmt");
        txtNoticeCharges.setName("txtNoticeCharges");
        txtPostageCharges.setName("txtPostageCharges");

        txtExecutionDecreeCharges.setName("txtExecutionDecreeCharges");
        txtArbitraryCharges.setName("txtArbitraryCharges");
        txtInsuranceCharges.setName("txtInsuranceCharges");
        txtLegalCharges.setName("txtLegalCharges");
        txtPreMatIntCalctPeriod.setName("txtPreMatIntCalctPeriod");
        txtPreMatIntCalctPeriod1.setName("txtPreMatIntCalctPeriod1");
        txtElgLoanAmt.setName("txtElgLoanAmt");
        txtDepAmtMaturing.setName("txtDepAmtMaturing");

        //ltd interest upto date or maturituy date
        panLTDInterest.setName("panLTDInterest");
        rdoMaturity_Yes.setName("rdoMaturity_Yes");
        rdoCurrDt_Yes.setName("rdoCurrDt_Yes");
        lblCollectIntTill.setName("lblCollectIntTill");
        //Telk
        lblCollectIntTillLoanClosureMenu.setName("lblCollectIntTillLoanClosureMenu");
        lblCollectIntTillDepositClosureMenu.setName("lblCollectIntTillDepositClosureMenu");
        lblCollectIntTillDepositClosure.setName("lblCollectIntTillDepositClosure");
        rdoMaturity_Deposit_closure_Maturity_Yes.setName("rdoMaturity_Deposit_closure_Maturity_Yes");
        rdoMaturity_Deposit_closure_Curr_Yes.setName("rdoMaturity_Deposit_closure_Curr_Yes");

        lblGracePeriodPenalInterest.setName("lblGracePeriodPenalInterest");
        txtGracePeriodPenalInterest.setName("txtGracePeriodPenalInterest");

        //subsidy wiveroff interestrebate

        lblSubsidyInterestCalculatedOn.setName("lblSubsidyInterestCalculatedOn");
        rdoLoanBalance_Yes.setName("rdoLoanBalance_Yes");
        rdoSubsidyAdjustLoanBalance_Yes.setName("rdoSubsidyAdjustLoanBalance_Yes");
        rdoSubsidyReceivedDate.setName("rdoSubsidyReceivedDate");
        rdoLoanOpenDate.setName("rdoLoanOpenDate");
        panInterestRebate.setName("panInterestRebate");
        lblInterestRebateAllowed.setName("lblInterestRebateAllowed");
        rdoInterestRebateAllowed_Yes.setName("rdoInterestRebateAllowed_Yes");
        rdoInterestRebateAllowed_No.setName("rdoInterestRebateAllowed_No");
        lblInterestRebatePercentage.setName("lblInterestRebatePercentage");
        txtInterestRebatePercentage.setName("txtInterestRebatePercentage");
        lblInterestRebateCalculation.setName("lblInterestRebateCalculation");
        cboInterestRebateCalculation.setName("cboInterestRebateCalculation");
        lblRebatePeriod.setName("lblRebatePeriod");
        cboRebatePeriod.setName("cboRebatePeriod");
        lblFinancialYearstartingfrom.setName("lblFinancialYearstartingfrom");
        lblPenalInterestWaiverAllowed.setName("lblPenalInterestWaiverAllowed");
        rdoPenalInterestWaiverAllowed_Yes.setName("rdoPenalInterestWaiverAllowed_Yes");
        rdoPrincipalWaiverAllowed_Yes.setName("rdoPrincipalWaiverAllowed_Yes");
        rdoNoticeWaiverAllowed_Yes.setName("rdoNoticeWaiverAllowed_Yes");
        rdoNoticeWaiverAllowed_No.setName("rdoNoticeWaiverAllowed_No");
        panWaivedOff.setName("panWaivedOff");
        rdoPrincipalWaiverAllowed_No.setName("rdoPrincipalWaiverAllowed_No");
        rdoPenalInterestWaiverAllowed_Yes.setName("rdoPenalInterestWaiverAllowed_Yes");
        rdoPenalInterestWaiverAllowed_No.setName("rdoPenalInterestWaiverAllowed_No");
        lblInterestWaiverAllowed.setName("lblInterestWaiverAllowed");
        rdoInterestWaiverAllowed_Yes.setName("rdoInterestWaiverAllowed_Yes");
        rdoInterestWaiverAllowed_No.setName("rdoInterestWaiverAllowed_No");
        lblDebitIntDiscount.setName("lblDebitIntDiscount");
        txtDebitIntDiscount.setName("txtDebitIntDiscount");
        btnDebitIntDiscount.setName("btnDebitIntDiscount");
        lblStampAdvancesHead.setName("lblStampAdvancesHead");
        txtStampAdvancesHead.setName("txtStampAdvancesHead");
        btnStampAdvancesHead.setName("btnStampAdvancesHead");
        txtNoticeAdvancesHead.setName("txtNoticeAdvancesHead");
        btnNoticeAdvancesHead.setName("btnNoticeAdvancesHead");
        lblCreditStampAdvance.setName("lblCreditStampAdvance");
        chkCreditStampAdvance.setName("chkCreditStampAdvance");
        chkCreditNoticeAdvance.setName("chkCreditNoticeAdvance");
        lblAdvertisementHead.setName("lblAdvertisementHead");
        txtAdvertisementHead.setName("txtAdvertisementHead");
        btnAdvertisementHead.setName("btnAdvertisementHead");
        lblARCEPSuspenceHead.setName("lblARCEPSuspenceHead");
        txtARCEPSuspenceHead.setName("txtARCEPSuspenceHead");
        btnARCEPSuspenceHead.setName("btnARCEPSuspenceHead");
        lblInterestDueKeptRecivable.setName("lblInterestDueKeptRecivable");
        rdoInterestDueKeptReceivable_Yes.setName("rdoInterestDueKeptReceivable_Yes");
        rdoInterestDueKeptReceivable_No.setName("rdoInterestDueKeptRecivable_No");
        panInterestDueKeptRecivable.setName("panInterestDueKeptRecivable");

        btnTransactionAdd_OTS.setName("btnTransactionAdd_OTS");
        btnTransactionRemove_OTS.setName("btnTransactionRemove_OTS");
        lstSelectedPriorityTransaction_OTS.setName("lstSelectedPriorityTransaction_OTS");
        lstAvailableTypeTransaction_OTS.setName("lstAvailableTypeTransaction_OTS");


        lblInsuranceCharge.setName("lblInsuranceCharge");
        rdoinsuranceCharge_Yes.setName("rdoinsuranceCharge_Yes");
        rdoinsuranceCharge_No.setName("rdoinsuranceCharge_No");
        panInsuranceChargeApplicable.setName("panInsuranceChargeApplicable");
        lblMarketRateAsOn.setName("lblMarketRateAsOn");
        rdosanctionDate_Yes.setName("rdosanctionDate_Yes");
        rdosanctionDate_No.setName("rdosanctionDate_No");
        lblInsuraceRate.setName("lblInsuraceRate");
        txtInsuraceRate.setName("txtInsuraceRate");

        panGldRenewal.setName("panGldRenewal");
        lblGldRenewOldAmt.setName("lblGldRenewOldAmt");
        chkGldRenewOldAmt.setName("chkGldRenewOldAmt");
        lblGldRenewCash.setName("lblGldRenewCash");
        chkGldRenewCash.setName("chkGldRenewCash");
        lblGldRenewMarketRate.setName("lblGldRenewMarketRate");
        chkGldRenewMarketRate.setName("chkGldRenewMarketRate");
        lblCollectIntOnMaturity.setName("lblCollectIntOnMaturity");
        chkInterestOnMaturity.setName("chkInterestOnMaturity");
        lblFrequencyInDays.setName("lblFrequencyInDays");
        txtFrequencyInDays.setName("txtFrequencyInDays");
        lblSalaryRecovery.setName("lblSalaryRecovery");
        panSalRec.setName("panSalRec");
        rdoSalRec_Yes.setName("rdoSalRec_Yes");
        rdoSalRec_No.setName("rdoSalRec_No");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        //        final NewLoanProductRB resourceBundle = new NewLoanProductRB();

        lblPLRApplNewAcc.setText(resourceBundle.getString("lblPLRApplNewAcc"));
        lblLoanPeriodMul.setText(resourceBundle.getString("lblLoanPeriodMul"));
        lblOperatesLike.setText(resourceBundle.getString("lblOperatesLike"));
        lblProdCategory.setText(resourceBundle.getString("lblProdCategory"));
        lblQIS.setText(resourceBundle.getString("lblQIS"));
        rdoMobileBanlingClient_Yes.setText(resourceBundle.getString("rdoMobileBanlingClient_Yes"));
        btnStatementCharges.setText(resourceBundle.getString("btnStatementCharges"));
        btnAccClosCharges.setText(resourceBundle.getString("btnAccClosCharges"));
        btnIntPayableAccount.setText(resourceBundle.getString("btnIntPayableAccount"));
        lblDocumentType.setText(resourceBundle.getString("lblDocumentType"));
        rdoPLRRateAppl_No.setText(resourceBundle.getString("rdoPLRRateAppl_No"));
        lblLastInterAppDate.setText(resourceBundle.getString("lblLastInterAppDate"));
        ((javax.swing.border.TitledBorder) panFolio.getBorder()).setTitle(resourceBundle.getString("panFolio"));
        lblRefinancingInsti.setText(resourceBundle.getString("lblRefinancingInsti"));
        lblMaxPeriod.setText(resourceBundle.getString("lblMaxPeriod"));
        rdoStatCharges_No.setText(resourceBundle.getString("rdoStatCharges_No"));
        btnCharge_New.setText(resourceBundle.getString("btnCharge_New"));
        lblPLRRate_Per.setText(resourceBundle.getString("lblPLRRate_Per"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblIsStaffAccOpened.setText(resourceBundle.getString("lblIsStaffAccOpened"));
        btnExpiryInter.setText(resourceBundle.getString("btnExpiryInter"));
        lblPLRRateAppl.setText(resourceBundle.getString("lblPLRRateAppl"));
        btnCheReturnChargest_In.setText(resourceBundle.getString("btnCheReturnChargest_In"));
        lblRatePerFolio.setText(resourceBundle.getString("lblRatePerFolio"));
        ((javax.swing.border.TitledBorder) panSpeItems.getBorder()).setTitle(resourceBundle.getString("panSpeItems"));
        rdoATMcardIssued_No.setText(resourceBundle.getString("rdoATMcardIssued_No"));
        btnAccHead.setText(resourceBundle.getString("btnAccHead"));
        lblLimitExpiryInter.setText(resourceBundle.getString("lblLimitExpiryInter"));
        lblDocumentDesc.setText(resourceBundle.getString("lblDocumentDesc"));
        lblSecurityDeails.setText(resourceBundle.getString("lblSecurityDeails"));
        lblPenalInter.setText(resourceBundle.getString("lblPenalInter"));
        lblSunbsidy.setText(resourceBundle.getString("lblSunbsidy"));
        lblProductDesc.setText(resourceBundle.getString("lblProductDesc"));
        lblPLRAppForm.setText(resourceBundle.getString("lblPLRAppForm"));
        rdoIsStaffAccOpened_Yes.setText(resourceBundle.getString("rdoIsStaffAccOpened_Yes"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblIsAnyBranBankingAllowed.setText(resourceBundle.getString("lblIsAnyBranBankingAllowed"));
        lblAccDebitInter.setText(resourceBundle.getString("lblAccDebitInter"));
        lblMiscSerCharges.setText(resourceBundle.getString("lblMiscSerCharges"));
        lblNumPatternFollowed.setText(resourceBundle.getString("lblNumPatternFollowed"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblChargeAppliedType.setText(resourceBundle.getString("lblChargeAppliedType"));
        lblMinPeriod.setText(resourceBundle.getString("lblMinPeriod"));
        rdoIsAnyBranBankingAllowed_Yes.setText(resourceBundle.getString("rdoIsAnyBranBankingAllowed_Yes"));
        lblPLRRate.setText(resourceBundle.getString("lblPLRRate"));
        lblSectorCode.setText(resourceBundle.getString("lblSectorCode"));
        lblCharge_1.setText(resourceBundle.getString("lblCharge_1"));
        btnPenalInter.setText(resourceBundle.getString("btnPenalInter"));
        lblToCollectFolioCharges.setText(resourceBundle.getString("lblToCollectFolioCharges"));
        rdoToChargeOn_Man.setText(resourceBundle.getString("rdoToChargeOn_Man"));
        lblPenalInterRate_Per.setText(resourceBundle.getString("lblPenalInterRate_Per"));
        rdoIsDebitInterUnderClearing_No.setText(resourceBundle.getString("rdoIsDebitInterUnderClearing_No"));
        lblIncompleteFolioRoundOffFreq.setText(resourceBundle.getString("lblIncompleteFolioRoundOffFreq"));
        lblStatCharges.setText(resourceBundle.getString("lblStatCharges"));
        lblCommodityCode.setText(resourceBundle.getString("lblCommodityCode"));
        lblIsDebitInterUnderClearing.setText(resourceBundle.getString("lblIsDebitInterUnderClearing"));
        ((javax.swing.border.TitledBorder) panChequeReturnCharges.getBorder()).setTitle(resourceBundle.getString("panChequeReturnCharges"));
        rdoFolioChargesAppl_Yes.setText(resourceBundle.getString("rdoFolioChargesAppl_Yes"));
        lblApplicableInter.setText(resourceBundle.getString("lblApplicableInter"));
        lblATMcardIssued.setText(resourceBundle.getString("lblATMcardIssued"));
        lblExpiryInter.setText(resourceBundle.getString("lblExpiryInter"));
        lblECGC.setText(resourceBundle.getString("lblECGC"));
        lblPLRApplExistingAcc.setText(resourceBundle.getString("lblPLRApplExistingAcc"));
        btnCheReturnChargest_Out.setText(resourceBundle.getString("btnCheReturnChargest_Out"));
        lblLimit.setText(resourceBundle.getString("lblLimit"));
        btnFolioChargesAcc.setText(resourceBundle.getString("btnFolioChargesAcc"));
        lblAccClosCharges.setText(resourceBundle.getString("lblAccClosCharges"));
        lblArcCostAccount.setText(resourceBundle.getString("lblArcCostAccount"));
        lblArcExpenseAccount.setText(resourceBundle.getString("lblArcExpenseAccount"));
        lblEaCostAccount.setText(resourceBundle.getString("lblEaCostAccount"));
        lblEaExpenseAccount.setText(resourceBundle.getString("lblEaExpenseAccount"));
        lblEpCostAccount.setText(resourceBundle.getString("lblEpCostAccount"));
        lblEpExpenseAccount.setText(resourceBundle.getString("lblEpExpenseAccount"));
        lblIntPayableAccount.setText(resourceBundle.getString("lblIntPayableAccount"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblDebInterCalcFreq.setText(resourceBundle.getString("lblDebInterCalcFreq"));
        rdoldebitInterCharged_No.setText(resourceBundle.getString("rdoldebitInterCharged_No"));
        lbldebitInterCharged.setText(resourceBundle.getString("lbldebitInterCharged"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblHealthCode.setText(resourceBundle.getString("lblHealthCode"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblProvisionDoubtfulAssetsPer1.setText(resourceBundle.getString("lblProvisionDoubtfulAssetsPer1"));
        lblProvisionDoubtfulAssetsPer2.setText(resourceBundle.getString("lblProvisionDoubtfulAssetsPer2"));
        lblProvisionDoubtfulAssetsPer3.setText(resourceBundle.getString("lblProvisionDoubtfulAssetsPer3"));
        rdoIsAnyBranBankingAllowed_No.setText(resourceBundle.getString("rdoIsAnyBranBankingAllowed_No"));
        lblProvisionLossAssetsPer.setText(resourceBundle.getString("lblProvisionLossAssetsPer"));
        rdoToChargeOn_Sys.setText(resourceBundle.getString("rdoToChargeOn_Sys"));
        lblProvisionLossAssets.setText(resourceBundle.getString("lblProvisionLossAssets"));
        rdoCreditCardIssued_No.setText(resourceBundle.getString("rdoCreditCardIssued_No"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        rdoMobileBanlingClient_No.setText(resourceBundle.getString("rdoMobileBanlingClient_No"));
        lbStatementCharges.setText(resourceBundle.getString("lbStatementCharges"));
        lblPeriodTranSStanAssets.setText(resourceBundle.getString("lblPeriodTranSStanAssets"));
        lblCharge_2.setText(resourceBundle.getString("lblCharge_2"));
        lblIndusCode.setText(resourceBundle.getString("lblIndusCode"));
        lblcalcType.setText(resourceBundle.getString("lblcalcType"));
        btnProcessingCharges.setText(resourceBundle.getString("btnProcessingCharges"));
        lblToChargeOn.setText(resourceBundle.getString("lblToChargeOn"));
        rdoPLRApplExistingAcc_Yes.setText(resourceBundle.getString("rdoPLRApplExistingAcc_Yes"));
        rdoToChargeType_Credit.setText(resourceBundle.getString("rdoToChargeType_Credit"));
        rdoToChargeType_Debit.setText(resourceBundle.getString("rdoToChargeType_Debit"));
        rdoToChargeType_Both.setText(resourceBundle.getString("rdoToChargeType_Both"));
        lblAccCreditInter.setText(resourceBundle.getString("lblAccCreditInter"));
        lblMinPeriodsArrears.setText(resourceBundle.getString("lblMinPeriodsArrears"));
        rdoIsLimitDefnAllowed_No.setText(resourceBundle.getString("rdoIsLimitDefnAllowed_No"));
        rdoIsInterestFirst_No.setText(resourceBundle.getString("rdoIsInterestFirst_No"));
        lblPeriodTransDoubtfulAssets1.setText(resourceBundle.getString("lblPeriodTransDoubtfulAssets1"));
        lblPeriodTransDoubtfulAssets2.setText(resourceBundle.getString("lblPeriodTransDoubtfulAssets2"));
        lblPeriodTransDoubtfulAssets3.setText(resourceBundle.getString("lblPeriodTransDoubtfulAssets3"));
        lblLastAccNum.setText(resourceBundle.getString("lblLastAccNum"));
        rdoLimitExpiryInter_Yes.setText(resourceBundle.getString("rdoLimitExpiryInter_Yes"));
        rdoProcessCharges_No.setText(resourceBundle.getString("rdoProcessCharges_No"));
        lblMaxDebitInterRate.setText(resourceBundle.getString("lblMaxDebitInterRate"));
        lblCommitCharges.setText(resourceBundle.getString("lblCommitCharges"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblExposureLimit_Policy.setText(resourceBundle.getString("lblExposureLimit_Policy"));
        rdoSubsidy_No.setText(resourceBundle.getString("rdoSubsidy_No"));
        rdoCommitmentCharge_Yes.setText(resourceBundle.getString("rdoCommitmentCharge_Yes"));
        lblPurposeCode.setText(resourceBundle.getString("lblPurposeCode"));
        lblCommitmentCharge.setText(resourceBundle.getString("lblCommitmentCharge"));
        rdoIsLimitDefnAllowed_Yes.setText(resourceBundle.getString("rdoIsLimitDefnAllowed_Yes"));
        rdoIsInterestFirst_Yes.setText(resourceBundle.getString("rdoIsInterestFirst_Yes"));
        lblLimits.setText(resourceBundle.getString("lblLimits"));
        rdoToChargeOn_Both.setText(resourceBundle.getString("rdoToChargeOn_Both"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        rdoProcessCharges_Yes.setText(resourceBundle.getString("rdoProcessCharges_Yes"));
        lblFolioChargesAppFreq.setText(resourceBundle.getString("lblFolioChargesAppFreq"));
        btnMiscServCharges.setText(resourceBundle.getString("btnMiscServCharges"));
        lblLastInterCalcDate.setText(resourceBundle.getString("lblLastInterCalcDate"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblProdCurrency.setText(resourceBundle.getString("lblProdCurrency"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSubsidy.setText(resourceBundle.getString("lblSubsidy"));
        rdoIsDebitInterUnderClearing_Yes.setText(resourceBundle.getString("rdoIsDebitInterUnderClearing_Yes"));
        lblFolioChargesAcc.setText(resourceBundle.getString("lblFolioChargesAcc"));
        lblExposureLimit_Policy_Per.setText(resourceBundle.getString("lblExposureLimit_Policy_Per"));
        lblProvisionsStanAssetsPer.setText(resourceBundle.getString("lblProvisionsStanAssetsPer"));
        rdoPenalAppl_No.setText(resourceBundle.getString("rdoPenalAppl_No"));
        lblPenalInterRate.setText(resourceBundle.getString("lblPenalInterRate"));
        lblProvisionsStanAssets.setText(resourceBundle.getString("lblProvisionsStanAssets"));
        lblSpaces.setText(resourceBundle.getString("lblSpaces"));
        lblProcessCharges.setText(resourceBundle.getString("lblProcessCharges"));
        rdoFolioChargesAppl_No.setText(resourceBundle.getString("rdoFolioChargesAppl_No"));
        lblNoEntriesPerFolio.setText(resourceBundle.getString("lblNoEntriesPerFolio"));
        lblMobileBanlingClient.setText(resourceBundle.getString("lblMobileBanlingClient"));
        btnCharge_Delete.setText(resourceBundle.getString("btnCharge_Delete"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblProductFreq.setText(resourceBundle.getString("lblProductFreq"));
        lblAccHead.setText(resourceBundle.getString("lblAccHead"));
        rdoSubsidy_Yes.setText(resourceBundle.getString("rdoSubsidy_Yes"));
        lblRateAmt.setText(resourceBundle.getString("lblRateAmt"));
        lblMaxDebitInterAmt.setText(resourceBundle.getString("lblMaxDebitInterAmt"));
        rdoPLRRateAppl_Yes.setText(resourceBundle.getString("rdoPLRRateAppl_Yes"));
        lblDebitProdRoundOff.setText(resourceBundle.getString("lblDebitProdRoundOff"));
        lblDebitInterAppFreq.setText(resourceBundle.getString("lblDebitInterAppFreq"));
        rdoStatCharges_Yes.setText(resourceBundle.getString("rdoStatCharges_Yes"));
        rdoLimitExpiryInter_No.setText(resourceBundle.getString("rdoLimitExpiryInter_No"));
        lblMinDebitInterRate_Per.setText(resourceBundle.getString("lblMinDebitInterRate_Per"));
        lblCreditCardIssued.setText(resourceBundle.getString("lblCreditCardIssued"));
        rdoPLRApplNewAcc_Yes.setText(resourceBundle.getString("rdoPLRApplNewAcc_Yes"));
        lblPeriodTransLossAssets.setText(resourceBundle.getString("lblPeriodTransLossAssets"));
        lblMiscServCharges.setText(resourceBundle.getString("lblMiscServCharges"));
        lblMaxDebitInterRate_Per.setText(resourceBundle.getString("lblMaxDebitInterRate_Per"));
        lblExposureLimit_Prud_Per.setText(resourceBundle.getString("lblExposureLimit_Prud_Per"));
        btnAccCreditInter.setText(resourceBundle.getString("btnAccCreditInter"));
        lblPenalAppl.setText(resourceBundle.getString("lblPenalAppl"));
        lblRangeTo.setText(resourceBundle.getString("lblRangeTo"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblRangeFrom.setText(resourceBundle.getString("lblRangeFrom"));
        rdoPLRApplExistingAcc_No.setText(resourceBundle.getString("rdoPLRApplExistingAcc_No"));
        lblAccClosingCharges.setText(resourceBundle.getString("lblAccClosingCharges"));
        lblDirectFinance.setText(resourceBundle.getString("lblDirectFinance"));
        btnDocumentDelete.setText(resourceBundle.getString("btnDocumentDelete"));
        lblApplicableInterPer.setText(resourceBundle.getString("lblApplicableInterPer"));
        btnDocumentNew.setText(resourceBundle.getString("btnDocumentNew"));
        ((javax.swing.border.TitledBorder) panNonPerAssets.getBorder()).setTitle(resourceBundle.getString("panNonPerAssets"));
//        lblPrincipalWaiverAllowed1.setText(resourceBundle.getString("lblMinAmtLoan"));
        lblProvisionDoubtfulAssets1.setText(resourceBundle.getString("lblProvisionDoubtfulAssets1"));
        lblProvisionDoubtfulAssets2.setText(resourceBundle.getString("lblProvisionDoubtfulAssets2"));
        lblProvisionDoubtfulAssets3.setText(resourceBundle.getString("lblProvisionDoubtfulAssets3"));
        rdoATMcardIssued_Yes.setText(resourceBundle.getString("rdoATMcardIssued_Yes"));
        lblTypeFacility.setText(resourceBundle.getString("lblTypeFacility"));
        lblCheReturnChargest_In.setText(resourceBundle.getString("lblCheReturnChargest_In"));
        rdoCommitmentCharge_No.setText(resourceBundle.getString("rdoCommitmentCharge_No"));
        lblExposureLimit_Prud.setText(resourceBundle.getString("lblExposureLimit_Prud"));
        lblProcessingCharges.setText(resourceBundle.getString("lblProcessingCharges"));
        lblCheReturnChargest_Out.setText(resourceBundle.getString("lblCheReturnChargest_Out"));
        lblMinDebitInterRate.setText(resourceBundle.getString("lblMinDebitInterRate"));
        lblNextFolioDDate.setText(resourceBundle.getString("lblNextFolioDDate"));
        lbl20Code.setText(resourceBundle.getString("lbl20Code"));
        lblMinInterDebited.setText(resourceBundle.getString("lblMinInterDebited"));
        rdoPenalAppl_Yes.setText(resourceBundle.getString("rdoPenalAppl_Yes"));
        lblDebitInterComFreq.setText(resourceBundle.getString("lblDebitInterComFreq"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        rdoldebitInterCharged_Yes.setText(resourceBundle.getString("rdoldebitInterCharged_Yes"));
        lblDocumentNo.setText(resourceBundle.getString("lblDocumentNo"));
        btnAccDebitInter.setText(resourceBundle.getString("btnAccDebitInter"));
        lblDebitInterRoundOff.setText(resourceBundle.getString("lblDebitInterRoundOff"));
        lblStatChargesRate.setText(resourceBundle.getString("lblStatChargesRate"));
        lblGuaranteeCoverCode.setText(resourceBundle.getString("lblGuaranteeCoverCode"));
        lblProvisionStandardAssetss.setText(resourceBundle.getString("lblProvisionStandardAssetss"));
        lblLastFolioChargesAppl.setText(resourceBundle.getString("lblLastFolioChargesAppl"));
        lblFolioChargesAppl.setText(resourceBundle.getString("lblFolioChargesAppl"));
        lblPlrApplAccSancForm.setText(resourceBundle.getString("lblPlrApplAccSancForm"));
        lblMinDebitInterAmt.setText(resourceBundle.getString("lblMinDebitInterAmt"));
        rdoCreditCardIssued_Yes.setText(resourceBundle.getString("rdoCreditCardIssued_Yes"));
        btnCommitCharges.setText(resourceBundle.getString("btnCommitCharges"));
        btnDocumentSave.setText(resourceBundle.getString("btnDocumentSave"));
        rdoPLRApplNewAcc_No.setText(resourceBundle.getString("rdoPLRApplNewAcc_No"));
        rdoIsStaffAccOpened_No.setText(resourceBundle.getString("rdoIsStaffAccOpened_No"));
        lblMinAmtLoan.setText(resourceBundle.getString("lblMinAmtLoan"));
        lblPeriodAfterWhichTransNPerformingAssets.setText(resourceBundle.getString("lblPeriodAfterWhichTransNPerformingAssets"));
        lblGovtSchemeCode.setText(resourceBundle.getString("lblGovtSchemeCode"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblProvisionStandardAssetssPer.setText(resourceBundle.getString("lblProvisionStandardAssetssPer"));
        btnCharge_Save.setText(resourceBundle.getString("btnCharge_Save"));
        lblIsInterestFirst.setText(resourceBundle.getString("lblIsInterestFirst"));
        lblIsLimitDefnAllowed.setText(resourceBundle.getString("lblIsLimitDefnAllowed"));
        lblNoticeType.setText(resourceBundle.getString("lblNoticeType"));
        lblIssueAfter.setText(resourceBundle.getString("lblIssueAfter"));
        lblNoticeChargeAmt.setText(resourceBundle.getString("lblNoticeChargeAmt"));
        lblPostageAmt.setText(resourceBundle.getString("lblPostageAmt"));
        btnNotice_Charge_New.setText(resourceBundle.getString("btnNotice_Charge_New"));
        btnNotice_Charge_Save.setText(resourceBundle.getString("btnNotice_Charge_Save"));
        btnNotice_Charge_Delete.setText(resourceBundle.getString("btnNotice_Charge_Delete"));
        lblNoticeCharges.setText(resourceBundle.getString("lblNoticeCharges"));
        lblPostageCharges.setText(resourceBundle.getString("lblPostageCharges"));
        lblPenalDue.setText(resourceBundle.getString("lblPenalDue"));
        lblPrematureCloserMinCalcPeriod.setText(resourceBundle.getString("lblPrematureCloserMinCalcPeriod"));
        lblPrematureIntCalcAmt.setText(resourceBundle.getString("lblPrematureIntCalcAmt"));
        lblBillbyBill.setText(resourceBundle.getString("lblBillbyBill"));
        lblasAndWhenCustomer.setText(resourceBundle.getString("lblasAndWhenCustomer"));
        lblcalendar.setText(resourceBundle.getString("lblcalendar"));
        lblEligibleDepositAmtForLoan.setText(resourceBundle.getString("lblEligibleDepositAmtForLoan"));
        lblReviewPeriodLoan.setText(resourceBundle.getString("lblReviewPeriodLoan"));
        lblDepositRoundOff.setText(resourceBundle.getString("lblDepositRoundOff"));
        rdoMaturity_Yes.setText(resourceBundle.getString("rdoMaturity_Yes"));
        rdoCurrDt_Yes.setText(resourceBundle.getString("rdoCurrDt_Yes"));
        lblAvailableTransaction.setText(resourceBundle.getString("lblAvailableTransaction"));
        lblSelectedTransaction.setText(resourceBundle.getString("lblSelectedTransaction"));
        lblCollectIntTill.setText(resourceBundle.getString("lblCollectIntTill"));
        lblCreditStampAdvance.setText(resourceBundle.getString("lblCreditStampAdvance"));

        //Telk

        lblCollectIntTillLoanClosureMenu.setText(resourceBundle.getString("lblCollectIntTillLoanClosureMenu"));
        lblCollectIntTillDepositClosureMenu.setText(resourceBundle.getString("lblCollectIntTillDepositClosureMenu"));
        lblCollectIntTillDepositClosure.setText(resourceBundle.getString("lblCollectIntTillDepositClosure"));
        rdoMaturity_Deposit_closure_Maturity_Yes.setText(resourceBundle.getString("rdoMaturity_Yes"));
        rdoMaturity_Deposit_closure_Curr_Yes.setText(resourceBundle.getString("rdoCurrDt_Yes"));
        lblGracePeriodPenalInterest.setText(resourceBundle.getString("lblGracePeriodPenalInterest"));



//        lbl_av_Balance.setText(resourceBundle.getString("lbl_av_Balance"));
//        lblNoOfFreeFolio.setText(resourceBundle.getString("lblNoOfFreeFolio"));


        //subsidy wiveroff interestrebate



        lblSubsidyInterestCalculatedOn.setText(resourceBundle.getString("lblSubsidyInterestCalculatedOn"));
        rdoLoanBalance_Yes.setText(resourceBundle.getString("rdoLoanBalance_Yes"));
        rdoSubsidyAdjustLoanBalance_Yes.setText(resourceBundle.getString("rdoSubsidyAdjustLoanBalance_Yes"));


        rdoSubsidyReceivedDate.setText(resourceBundle.getString("rdoSubsidyReceivedDate"));
        rdoLoanOpenDate.setText(resourceBundle.getString("rdoLoanOpenDate"));



//        panInterestRebate.setText(resourceBundle.getString("lblGracePeriodPenalInterest"));


        lblInterestRebateAllowed.setText(resourceBundle.getString("lblInterestRebateAllowed"));
        rdoInterestRebateAllowed_Yes.setText(resourceBundle.getString("rdoPenalAppl_Yes"));
        rdoInterestRebateAllowed_No.setText(resourceBundle.getString("rdoPenalAppl_No"));

        lblInterestRebatePercentage.setText(resourceBundle.getString("lblInterestRebatePercentage"));
//        txtInterestRebatePercentage.setName("txtInterestRebatePercentage");
        lblInterestRebateCalculation.setText(resourceBundle.getString("lblInterestRebateCalculation"));
//        cboInterestRebateCalculation.setName("cboInterestRebateCalculation");
        lblRebatePeriod.setText(resourceBundle.getString("lblRebatePeriod"));
//        cboRebatePeriod.setName("cboRebatePeriod");
        lblFinancialYearstartingfrom.setText(resourceBundle.getString("lblFinancialYearstartingfrom"));
//        tdtFinancialYearstartingfrom.setName("tdtFinancialYearstartingfrom");
        lblPenalInterestWaiverAllowed.setText(resourceBundle.getString("lblPenalInterestWaiverAllowed"));
//        rdoPenalInterestWaiverAllowed_Yes.setText(resourceBundle.getString("rdoPenalAppl_Yes"));
//        panWaivedOff.setName("panWaivedOff");
        rdoPenalInterestWaiverAllowed_Yes.setText(resourceBundle.getString("rdoPenalAppl_Yes"));
        rdoPenalInterestWaiverAllowed_No.setText(resourceBundle.getString("rdoPenalAppl_No"));

        lblInterestWaiverAllowed.setText(resourceBundle.getString("lblInterestWaiverAllowed"));
        rdoInterestWaiverAllowed_Yes.setText(resourceBundle.getString("rdoPenalAppl_Yes"));
        rdoInterestWaiverAllowed_No.setText(resourceBundle.getString("rdoPenalAppl_No"));
        lblDebitIntDiscount.setText(resourceBundle.getString("lblDebitIntDiscount"));
        lblARCEPSuspenceHead.setText(resourceBundle.getString("lblARCEPSuspenceHead"));

        //parameter create for whether kept in interst recvivable or not

        lblInterestDueKeptRecivable.setText(resourceBundle.getString("lblInterestDueKeptRecivable"));
        rdoInterestDueKeptReceivable_Yes.setText(resourceBundle.getString("rdoPenalAppl_Yes"));
        rdoInterestDueKeptReceivable_No.setText(resourceBundle.getString("rdoPenalAppl_No"));
        //insurance charges

        lblInsuranceCharge.setText(resourceBundle.getString("lblInsuranceCharge"));
        rdoinsuranceCharge_Yes.setText(resourceBundle.getString("rdoPenalAppl_Yes"));
        rdoinsuranceCharge_No.setText(resourceBundle.getString("rdoPenalAppl_No"));
        lblMarketRateAsOn.setText(resourceBundle.getString("lblMarketRateAsOn"));
        rdosanctionDate_Yes.setText(resourceBundle.getString("rdosanctionDate_Yes"));
        rdosanctionDate_No.setText(resourceBundle.getString("rdosanctionDate_No"));
        lblInsuraceRate.setText(resourceBundle.getString("lblInsuraceRate"));


    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("txtProductDesc", new Boolean(true));
        mandatoryMap.put("cboOperatesLike", new Boolean(true));
        mandatoryMap.put("cboProdCategory", new Boolean(true));
        mandatoryMap.put("txtNumPatternFollowed", new Boolean(true));
        mandatoryMap.put("txtLastAccNum", new Boolean(true));
        mandatoryMap.put("rdoIsLimitDefnAllowed_Yes", new Boolean(true));
        mandatoryMap.put("rdoIsStaffAccOpened_Yes", new Boolean(true));
        mandatoryMap.put("rdoIsDebitInterUnderClearing_Yes", new Boolean(true));
        mandatoryMap.put("txtAccHead", new Boolean(true));
        //		mandatoryMap.put("cboProdCurrency", new Boolean(true));
        mandatoryMap.put("txtNumPatternFollowedSuffix", new Boolean(true));
        mandatoryMap.put("rdoldebitInterCharged_Yes", new Boolean(true));
        mandatoryMap.put("txtMinDebitInterRate", new Boolean(true));
        mandatoryMap.put("txtMaxDebitInterRate", new Boolean(true));
        mandatoryMap.put("txtMinDebitInterAmt", new Boolean(true));
        mandatoryMap.put("txtMaxDebitInterAmt", new Boolean(true));
        mandatoryMap.put("cboDebInterCalcFreq", new Boolean(true));
        mandatoryMap.put("cboDebitInterAppFreq", new Boolean(true));
        mandatoryMap.put("cboDebitInterComFreq", new Boolean(true));
        mandatoryMap.put("cboDebitProdRoundOff", new Boolean(true));
        mandatoryMap.put("cboDebitInterRoundOff", new Boolean(true));
        mandatoryMap.put("tdtLastInterCalcDate", new Boolean(true));
        mandatoryMap.put("tdtLastInterAppDate", new Boolean(true));
        mandatoryMap.put("rdoPLRRateAppl_Yes", new Boolean(true));
        mandatoryMap.put("txtPLRRate", new Boolean(true));
        mandatoryMap.put("tdtPLRAppForm", new Boolean(true));
        mandatoryMap.put("rdoPLRApplNewAcc_Yes", new Boolean(true));
        mandatoryMap.put("rdoPLRApplExistingAcc_Yes", new Boolean(true));
        mandatoryMap.put("tdtPlrApplAccSancForm", new Boolean(true));
        mandatoryMap.put("rdoPenalAppl_Yes", new Boolean(true));
        mandatoryMap.put("rdoLimitExpiryInter_Yes", new Boolean(true));
        mandatoryMap.put("txtPenalInterRate", new Boolean(true));
        mandatoryMap.put("txtExposureLimit_Prud", new Boolean(true));
        mandatoryMap.put("txtExposureLimit_Prud2", new Boolean(true));
        mandatoryMap.put("txtExposureLimit_Policy", new Boolean(true));
        mandatoryMap.put("txtExposureLimit_Policy2", new Boolean(true));
        mandatoryMap.put("cboProductFreq", new Boolean(true));
        mandatoryMap.put("txtAccClosingCharges", new Boolean(true));
        mandatoryMap.put("txtMiscSerCharges", new Boolean(true));
        mandatoryMap.put("rdoStatCharges_Yes", new Boolean(true));
        mandatoryMap.put("txtStatChargesRate", new Boolean(true));
        mandatoryMap.put("txtRateAmt", new Boolean(true));
        mandatoryMap.put("txtRangeFrom", new Boolean(true));
        mandatoryMap.put("txtRangeTo", new Boolean(true));
        mandatoryMap.put("rdoFolioChargesAppl_Yes", new Boolean(true));
        mandatoryMap.put("rdoToChargeType_Credit", new Boolean(true));
        mandatoryMap.put("rdoToChargeType_Debit", new Boolean(true));
        mandatoryMap.put("rdoToChargeType_Both", new Boolean(true));
        mandatoryMap.put("tdtLastFolioChargesAppl", new Boolean(true));
        mandatoryMap.put("txtNoEntriesPerFolio", new Boolean(true));
        mandatoryMap.put("tdtNextFolioDDate", new Boolean(true));
        mandatoryMap.put("cboFolioChargesAppFreq", new Boolean(true));
        mandatoryMap.put("cboToCollectFolioCharges", new Boolean(true));
        mandatoryMap.put("rdoToChargeOn_Man", new Boolean(true));
        mandatoryMap.put("cboIncompleteFolioRoundOffFreq", new Boolean(true));
        mandatoryMap.put("txtRatePerFolio", new Boolean(true));
        mandatoryMap.put("rdoProcessCharges_Yes", new Boolean(true));
        mandatoryMap.put("txtCharge_Limit2", new Boolean(true));
        mandatoryMap.put("cboCharge_Limit2", new Boolean(true));
        mandatoryMap.put("rdoCommitmentCharge_Yes", new Boolean(true));
        mandatoryMap.put("txtCharge_Limit3", new Boolean(true));
        mandatoryMap.put("cboCharge_Limit3", new Boolean(true));
        mandatoryMap.put("txtAccClosCharges", new Boolean(true));
        mandatoryMap.put("txtArcCostAccount", new Boolean(true));
        mandatoryMap.put("txtArcExpenseAccount", new Boolean(true));
        mandatoryMap.put("txtEaCostAccount", new Boolean(true));
        mandatoryMap.put("txtEaExpenseAccount", new Boolean(true));
        mandatoryMap.put("txtEpCostAccount", new Boolean(true));
        mandatoryMap.put("txtEpExpenseAccount", new Boolean(true));
        mandatoryMap.put("txtIntPayableAccount", new Boolean(true));
        mandatoryMap.put("txtMiscServCharges", new Boolean(true));
        mandatoryMap.put("txtStatementCharges", new Boolean(true));
        mandatoryMap.put("txtAccDebitInter", new Boolean(true));
        mandatoryMap.put("txtPenalInter", new Boolean(true));
        mandatoryMap.put("txtAccCreditInter", new Boolean(true));
        mandatoryMap.put("txtExpiryInter", new Boolean(true));
        mandatoryMap.put("txtCheReturnChargest_Out", new Boolean(true));
        mandatoryMap.put("txtCheReturnChargest_In", new Boolean(true));
        mandatoryMap.put("txtFolioChargesAcc", new Boolean(true));
        mandatoryMap.put("txtCommitCharges", new Boolean(true));
        mandatoryMap.put("txtProcessingCharges", new Boolean(true));
        mandatoryMap.put("rdoATMcardIssued_Yes", new Boolean(true));
        mandatoryMap.put("rdoCreditCardIssued_Yes", new Boolean(true));
        mandatoryMap.put("rdoMobileBanlingClient_Yes", new Boolean(true));
        mandatoryMap.put("rdoIsAnyBranBankingAllowed_Yes", new Boolean(true));
        mandatoryMap.put("txtAvBalance", new Boolean(true));
        mandatoryMap.put("txtNoOfFreeFolio", new Boolean(true));
        mandatoryMap.put("txtMinPeriodsArrears", new Boolean(true));
        mandatoryMap.put("cboMinPeriodsArrears", new Boolean(true));
        mandatoryMap.put("txtPeriodTranSStanAssets", new Boolean(true));
        mandatoryMap.put("cboPeriodTranSStanAssets", new Boolean(true));
        mandatoryMap.put("txtPeriodTransDoubtfulAssets1", new Boolean(true));
        mandatoryMap.put("txtPeriodTransDoubtfulAssets2", new Boolean(true));
        mandatoryMap.put("txtPeriodTransDoubtfulAssets3", new Boolean(true));
        mandatoryMap.put("cboPeriodTransDoubtfulAssets1", new Boolean(true));
        mandatoryMap.put("cboPeriodTransDoubtfulAssets2", new Boolean(true));
        mandatoryMap.put("cboPeriodTransDoubtfulAssets3", new Boolean(true));
        mandatoryMap.put("txtPeriodTransLossAssets", new Boolean(true));
        mandatoryMap.put("cboPeriodTransLossAssets", new Boolean(true));
        mandatoryMap.put("txtPeriodAfterWhichTransNPerformingAssets", new Boolean(true));
        mandatoryMap.put("cboPeriodAfterWhichTransNPerformingAssets", new Boolean(true));
        mandatoryMap.put("txtProvisionsStanAssets", new Boolean(true));
        mandatoryMap.put("txtProvisionDoubtfulAssets1", new Boolean(true));
        mandatoryMap.put("txtProvisionDoubtfulAssets2", new Boolean(true));
        mandatoryMap.put("txtProvisionDoubtfulAssets3", new Boolean(true));
        mandatoryMap.put("txtProvisionStandardAssetss", new Boolean(true));
        mandatoryMap.put("txtProvisionLossAssets", new Boolean(true));
        mandatoryMap.put("cbocalcType", new Boolean(true));
        mandatoryMap.put("txtMinPeriod", new Boolean(true));
        mandatoryMap.put("cboMinPeriod", new Boolean(true));
        mandatoryMap.put("txtMaxPeriod", new Boolean(true));
        mandatoryMap.put("cboMaxPeriod", new Boolean(true));
        mandatoryMap.put("txtMinAmtLoan", new Boolean(true));
        mandatoryMap.put("txtMaxAmtLoan", new Boolean(true));
        mandatoryMap.put("txtApplicableInter", new Boolean(true));
        mandatoryMap.put("txtMinInterDebited", new Boolean(true));
        mandatoryMap.put("rdoSubsidy_Yes", new Boolean(true));
        mandatoryMap.put("cboLoanPeriodMul", new Boolean(true));
        mandatoryMap.put("cboDocumentType", new Boolean(true));
        mandatoryMap.put("txtDocumentNo", new Boolean(true));
        mandatoryMap.put("txtDocumentDesc", new Boolean(true));
        mandatoryMap.put("cboCommodityCode", new Boolean(true));
        mandatoryMap.put("cboGuaranteeCoverCode", new Boolean(true));
        mandatoryMap.put("cboSectorCode", new Boolean(true));
        mandatoryMap.put("cboHealthCode", new Boolean(true));
        mandatoryMap.put("cboTypeFacility", new Boolean(true));
        mandatoryMap.put("cboPurposeCode", new Boolean(true));
        mandatoryMap.put("cboIndusCode", new Boolean(true));
        mandatoryMap.put("cbo20Code", new Boolean(true));
        mandatoryMap.put("cboRefinancingInsti", new Boolean(true));
        mandatoryMap.put("cboGovtSchemeCode", new Boolean(true));
        mandatoryMap.put("cboSecurityDeails", new Boolean(true));
        mandatoryMap.put("chkDirectFinance", new Boolean(true));
        mandatoryMap.put("chkECGC", new Boolean(true));
        mandatoryMap.put("chkQIS", new Boolean(true));

        mandatoryMap.put("cboNoticeType", new Boolean(true));
        mandatoryMap.put("cboIssueAfter", new Boolean(true));
        mandatoryMap.put("txtNoticeChargeAmt", new Boolean(true));
        mandatoryMap.put("txtPostageAmt", new Boolean(true));
        mandatoryMap.put("rdoIsInterestFirst_Yes", new Boolean(true));

//        mandatoryMap.put("txtInterestRebatePercentage", new Boolean(true));
//        mandatoryMap.put("cboInterestRebateCalculation", new Boolean(true));
//        mandatoryMap.put("cboRebatePeriod", new Boolean(true));
//        mandatoryMap.put("txtFinYearStartingFromDD", new Boolean(true));
//        mandatoryMap.put("txtFinYearStartingFromMM", new Boolean(true));

    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        //LOANS_PRODUCT
        txtProductID.setMaxLength(8);
        txtProductDesc.setMaxLength(128);
        txtAccHead.setMaxLength(16);
        //LOANS_PROD_ACPARAM
        txtNumPatternFollowed.setMaxLength(16);
//        txtNumPatternFollowed.setValidation(new NumericValidation());

        txtLastAccNum.setMaxLength(16);
        txtLastAccNum.setValidation(new NumericValidation());
        txtInterestRebatePercentage.setMaxLength(5);
        txtFinYearStartingFromDD.setValidation(new NumericValidation());
        txtFinYearStartingFromDD.setMaxLength(2);
        txtFinYearStartingFromMM.setValidation(new NumericValidation());
        txtFinYearStartingFromMM.setMaxLength(2);

        txtInterestRebatePercentage.setValidation(new PercentageValidation());
        txtGracePeriodPenalInterest.setMaxLength(3);
        txtGracePeriodPenalInterest.setValidation(new NumericValidation());
        txtNumPatternFollowedSuffix.setMaxLength(10);
        txtNumPatternFollowedSuffix.setValidation(new NumericValidation());
        //LOANS_PROD_INTREC
        txtMinDebitInterRate.setValidation(new PercentageValidation());
        txtInsuraceRate.setValidation(new PercentageValidation());
        //        txtMaxDebitInterRate.setMaxLength(3);
        txtMaxDebitInterRate.setValidation(new PercentageValidation());

        //        txtMinDebitInterAmt.setMaxLength(16);
        txtMinDebitInterAmt.setValidation(new CurrencyValidation(14, 2));
        txtPreMatIntCalctPeriod.setValidation(new NumericValidation());
        txtPreMatIntCalctPeriod1.setValidation(new NumericValidation());
        //        txtMaxDebitInterAmt.setMaxLength(16);
        txtMaxDebitInterAmt.setValidation(new CurrencyValidation(14, 2));

        //        txtPLRRate.setMaxLength(3);
        txtPLRRate.setValidation(new PercentageValidation());

        //        txtPenalInterRate.setMaxLength(3);
        txtPenalInterRate.setValidation(new PercentageValidation());

        //        txtExposureLimit_Prud.setMaxLength(16);
        txtExposureLimit_Prud.setValidation(new NumericValidation(14, 2));
        txtElgLoanAmt.setValidation(new PercentageValidation());
        txtDepAmtMaturing.setValidation(new PercentageValidation());

        //        txtExposureLimit_Policy.setMaxLength(16);
        txtExposureLimit_Policy.setValidation(new NumericValidation(14, 2));

        //        txtExposureLimit_Prud2.setMaxLength(3);
        txtExposureLimit_Prud2.setValidation(new PercentageValidation());

        //        txtExposureLimit_Policy2.setMaxLength(3);
        txtExposureLimit_Policy2.setValidation(new PercentageValidation());

        //LOANS_PROD_CHARGES
        //        txtAccClosingCharges.setMaxLength(16);
        txtAccClosingCharges.setValidation(new CurrencyValidation(14, 2));

        //        txtMiscSerCharges.setMaxLength(16);
        txtMiscSerCharges.setValidation(new CurrencyValidation(14, 2));

        //        txtStatChargesRate.setMaxLength(16);
        txtStatChargesRate.setValidation(new CurrencyValidation(14, 2));


        txtNoEntriesPerFolio.setMaxLength(5);
        txtNoEntriesPerFolio.setValidation(new NumericValidation());

        //        txtRatePerFolio.setMaxLength(16);
        txtRatePerFolio.setValidation(new CurrencyValidation(14, 2));
        //        txtcharge_Limit1.setMaxLength(3);
        //        txtcharge_Limit1.setValidation(new NumericValidation());
        //
        txtCharge_Limit2.setMaxLength(16);
        txtCharge_Limit2.setValidation(new NumericValidation());
        //
        txtCharge_Limit3.setMaxLength(3);
        txtCharge_Limit3.setValidation(new NumericValidation());
        //
        //        txtCharge_Limit4.setMaxLength(16);
        //        txtCharge_Limit4.setValidation(new NumericValidation(14,2));

        //LOANS_PROD_CHOCHRG
        //        txtRateAmt.setMaxLength(17);
        txtRateAmt.setValidation(new PercentageValidation());

        //        txtRangeFrom.setMaxLength(17);
        txtRangeFrom.setValidation(new CurrencyValidation(14, 2));

        //        txtRangeTo.setMaxLength(17);
        txtRangeTo.setValidation(new CurrencyValidation(14, 2));

        //LOANS_PROD_SPECIALITEM
        //LOANS_PROD_ACHD
        txtIntPayableAccount.setMaxLength(16);
        txtAccClosCharges.setMaxLength(16);
        txtMiscServCharges.setMaxLength(16);
        txtStatementCharges.setMaxLength(16);
        txtAccDebitInter.setMaxLength(16);
        txtPenalInter.setMaxLength(16);
        txtAccCreditInter.setMaxLength(16);
        txtExpiryInter.setMaxLength(16);
        txtCheReturnChargest_Out.setMaxLength(16);
        txtCheReturnChargest_In.setMaxLength(16);
        txtFolioChargesAcc.setMaxLength(16);
        txtCommitCharges.setMaxLength(16);
        txtProcessingCharges.setMaxLength(16);
        txtArcCostAccount.setMaxLength(16);
        txtArcExpenseAccount.setMaxLength(16);
        txtEaCostAccount.setMaxLength(16);
        txtEaExpenseAccount.setMaxLength(16);
        txtEpCostAccount.setMaxLength(16);
        txtEpExpenseAccount.setMaxLength(16);
        txtRebateInterest.setMaxLength(16);
        txtDebitIntDiscount.setMaxLength(16);
        txtStampAdvancesHead.setMaxLength(16);
        txtNoticeAdvancesHead.setMaxLength(16);
        txtAdvertisementHead.setMaxLength(16);
        txtARCEPSuspenceHead.setMaxLength(16);

        //LOANS_PROD_NONPERASSET
        txtMinPeriodsArrears.setMaxLength(5);// 4
        txtMinPeriodsArrears.setValidation(new NumericValidation());

        txtPeriodTranSStanAssets.setMaxLength(5);// 4
        txtPeriodTranSStanAssets.setValidation(new NumericValidation());

        txtPeriodTransDoubtfulAssets1.setMaxLength(5);// 4
        txtPeriodTransDoubtfulAssets1.setValidation(new NumericValidation());

        txtPeriodTransDoubtfulAssets2.setMaxLength(5);// 4
        txtPeriodTransDoubtfulAssets2.setValidation(new NumericValidation());

        txtPeriodTransDoubtfulAssets3.setMaxLength(5);// 4
        txtPeriodTransDoubtfulAssets3.setValidation(new NumericValidation());

        txtPeriodTransLossAssets.setMaxLength(5);// 4
        txtPeriodTransLossAssets.setValidation(new NumericValidation());

        txtPeriodAfterWhichTransNPerformingAssets.setMaxLength(5); // 4
        txtPeriodAfterWhichTransNPerformingAssets.setValidation(new NumericValidation());

        //        txtProvisionsStanAssets.setMaxLength(4);
        txtProvisionsStanAssets.setValidation(new PercentageValidation());

        //        txtProvisionDoubtfulAssets.setMaxLength(4);
        txtProvisionDoubtfulAssets1.setValidation(new PercentageValidation());
        txtProvisionDoubtfulAssets2.setValidation(new PercentageValidation());
        txtProvisionDoubtfulAssets3.setValidation(new PercentageValidation());

        //        txtProvisionStandardAssetss.setMaxLength(4);
        txtProvisionStandardAssetss.setValidation(new PercentageValidation());
        txtReviewPeriod.setValidation(new PercentageValidation());
        txtDepAmtLoanMaturingPeriod.setValidation(new PercentageValidation());
        //        txtProvisionLossAssets.setMaxLength(4);
        txtProvisionLossAssets.setValidation(new PercentageValidation());
        //LOANS_PROD_INTCALC
        txtMinPeriod.setMaxLength(5); // 4
        txtMinPeriod.setValidation(new NumericValidation());

        txtMaxPeriod.setMaxLength(5); // 4
        txtMaxPeriod.setValidation(new NumericValidation());

        //        txtMinAmtLoan.setMaxLength(17);
        txtMinAmtLoan.setValidation(new CurrencyValidation(14, 2));

        //        txtMaxAmtLoan.setMaxLength(17);
        txtMaxAmtLoan.setValidation(new CurrencyValidation(14, 2));

        //        txtApplicableInter.setMaxLength(3);
        txtApplicableInter.setValidation(new PercentageValidation());

        //        txtMinInterDebited.setMaxLength(3);
        txtMinInterDebited.setValidation(new PercentageValidation());
        // LOANS_PROD_DOC
        txtDocumentNo.setMaxLength(32);
        txtDocumentDesc.setMaxLength(128);
        //for notice charge
        txtNoticeChargeAmt.setMaxLength(16);
        txtNoticeChargeAmt.setValidation(new NumericValidation());
        txtPostageAmt.setMaxLength(16);
        txtPostageAmt.setValidation(new NumericValidation());
        txtEpExpenseAccount.setAllowAll(true);
        txtRebateInterest.setAllowAll(true);
        txtStampAdvancesHead.setAllowAll(true);
        txtNoticeAdvancesHead.setAllowAll(true);
        txtAdvertisementHead.setAllowAll(true);
        txtARCEPSuspenceHead.setAllowAll(true);
        txtDebitIntDiscount.setAllowAll(true);
        txtEpCostAccount.setAllowAll(true);
        txtEaExpenseAccount.setAllowAll(true);
        txtEaCostAccount.setAllowAll(true);
        txtArcExpenseAccount.setAllowAll(true);
        txtArcCostAccount.setAllowAll(true);
        txtExecutionDecreeCharges.setAllowAll(true);
        txtInsuranceCharges.setAllowAll(true);
        txtArbitraryCharges.setAllowAll(true);
        txtLegalCharges.setAllowAll(true);
        txtIntPayableAccount.setAllowAll(true);
        txtPostageCharges.setAllowAll(true);
        txtNoticeCharges.setAllowAll(true);
        txtProcessingCharges.setAllowAll(true);
        txtCommitCharges.setAllowAll(true);
        txtFolioChargesAcc.setAllowAll(true);
        txtCheReturnChargest_In.setAllowAll(true);
        txtCheReturnChargest_Out.setAllowAll(true);
        txtExpiryInter.setAllowAll(true);
        txtAccCreditInter.setAllowAll(true);
        txtPenalInter.setAllowAll(true);
        txtAccDebitInter.setAllowAll(true);
        txtStatementCharges.setAllowAll(true);
        txtMiscServCharges.setAllowAll(true);
        txtAccClosCharges.setAllowAll(true);
        txtAccHead.setAllowAll(true);
        txtDebitIntDiscount.setAllowAll(true);
    }

    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //---Account---
        rdoIsLimitDefnAllowed.remove(rdoIsLimitDefnAllowed_No);
        rdoIsLimitDefnAllowed.remove(rdoIsLimitDefnAllowed_Yes);

        rdoIsInterestFirst.remove(rdoIsInterestFirst_No);
        rdoIsInterestFirst.remove(rdoIsInterestFirst_Yes);

        rdoIsStaffAccOpened.remove(rdoIsStaffAccOpened_No);
        rdoIsStaffAccOpened.remove(rdoIsStaffAccOpened_Yes);

        rdoIsDebitInterUnderClearing.remove(rdoIsDebitInterUnderClearing_No);
        rdoIsDebitInterUnderClearing.remove(rdoIsDebitInterUnderClearing_Yes);
        //---Interest Receivable---
        rdoldebitInterCharged.remove(rdoldebitInterCharged_No);
        rdoldebitInterCharged.remove(rdoldebitInterCharged_Yes);

        rdoPLRRateAppl.remove(rdoPLRRateAppl_No);
        rdoPLRRateAppl.remove(rdoPLRRateAppl_Yes);

        rdoPLRApplNewAcc.remove(rdoPLRApplNewAcc_No);
        rdoPLRApplNewAcc.remove(rdoPLRApplNewAcc_Yes);

        rdoPLRApplExistingAcc.remove(rdoPLRApplExistingAcc_No);

        rdoPLRApplExistingAcc.remove(rdoPLRApplExistingAcc_Yes);

        rdoPenalAppl.remove(rdoPenalAppl_No);
        rdoPenalAppl.remove(rdoPenalAppl_Yes);

        rdoLimitExpiryInter.remove(rdoLimitExpiryInter_No);
        rdoLimitExpiryInter.remove(rdoLimitExpiryInter_Yes);
        //---Charges---
        rdoStatCharges.remove(rdoStatCharges_No);
        rdoStatCharges.remove(rdoStatCharges_Yes);

        rdoFolioChargesAppl.remove(rdoFolioChargesAppl_No);
        rdoFolioChargesAppl.remove(rdoFolioChargesAppl_Yes);

        rdoToChargeOn.remove(rdoToChargeOn_Both);
        rdoToChargeOn.remove(rdoToChargeOn_Man);
        rdoToChargeOn.remove(rdoToChargeOn_Sys);

        rdoLimitExpiryInter.remove(rdoLimitExpiryInter_No);
        rdoLimitExpiryInter.remove(rdoLimitExpiryInter_Yes);

        rdoLimitExpiryInter.remove(rdoLimitExpiryInter_No);
        rdoLimitExpiryInter.remove(rdoLimitExpiryInter_Yes);
        //---Special Items---
        rdoATMcardIssued.remove(rdoATMcardIssued_No);
        rdoATMcardIssued.remove(rdoATMcardIssued_Yes);

        rdoCreditCardIssued.remove(rdoCreditCardIssued_No);
        rdoCreditCardIssued.remove(rdoCreditCardIssued_Yes);

        rdoMobileBanlingClient.remove(rdoMobileBanlingClient_No);
        rdoMobileBanlingClient.remove(rdoMobileBanlingClient_Yes);

        rdoIsAnyBranBankingAllowed.remove(rdoIsAnyBranBankingAllowed_No);
        rdoIsAnyBranBankingAllowed.remove(rdoIsAnyBranBankingAllowed_Yes);

        rdoCommitmentCharge.remove(rdoCommitmentCharge_No);
        rdoCommitmentCharge.remove(rdoCommitmentCharge_Yes);

        rdoProcessCharges.remove(rdoProcessCharges_No);
        rdoProcessCharges.remove(rdoProcessCharges_Yes);
        //---Account Head---
        //---Non Performing Account---
        //---Interest Calculation---
        rdoSubsidy.remove(rdoSubsidy_No);
        rdoSubsidy.remove(rdoSubsidy_Yes);

        rdoLTDinterestGroup.remove(rdoMaturity_Yes);
        rdoLTDinterestGroup.remove(rdoCurrDt_Yes);


        rdoMaturityDepositClosureGroup.remove(rdoMaturity_Deposit_closure_Maturity_Yes);
        rdoMaturityDepositClosureGroup.remove(rdoMaturity_Deposit_closure_Curr_Yes);

        rdoSubsidy.remove(rdoSubsidy_Yes);
        rdoSubsidy.remove(rdoSubsidy_No);

        rdosubsidyInterestCalculateGroup.remove(rdoLoanBalance_Yes);
        rdosubsidyInterestCalculateGroup.remove(rdoSubsidyAdjustLoanBalance_Yes);

        rdoFromSubsidyReceivedDateGroup.remove(rdoSubsidyReceivedDate);
        rdoFromSubsidyReceivedDateGroup.remove(rdoLoanOpenDate);

        rdoInterestRebateAllowedGroup.remove(rdoInterestRebateAllowed_Yes);
        rdoInterestRebateAllowedGroup.remove(rdoInterestRebateAllowed_No);

        rdoPenalInterestWaiverGroup.remove(rdoPenalInterestWaiverAllowed_Yes);
        rdoPenalInterestWaiverGroup.remove(rdoPenalInterestWaiverAllowed_No);

        rdoRecoveryWaiverGroup.remove(rdoRecoveryWaiverAllowed_Yes);
        rdoRecoveryWaiverGroup.remove(rdoRecoveryWaiverAllowed_No);
        
        rdoMeasurementWaiverGroup.remove(rdoMeasurementWaiverAllowed_Yes);
        rdoMeasurementWaiverGroup.remove(rdoMeasurementWaiverAllowed_No);
        
        rdoKoleFieldOperationWaiverGroup.remove(rdoKoleFieldOperationWaiverAllowed_Yes);
        rdoKoleFieldOperationWaiverGroup.remove(rdoKoleFieldOperationWaiverAllowed_No);
        
        rdoKoleFieldExpenseWaiverGroup.remove(rdoKoleFieldExpenseWaiverAllowed_Yes);
        rdoKoleFieldExpenseWaiverGroup.remove(rdoKoleFieldExpenseWaiverAllowed_No);

//        rdoPrincipalInterestWaiverGroup.remove(rdoPrincipalWaiverAllowed_No);
//         rdoPrincipalInterestWaiverGroup.remove(rdoPrincipalWaiverAllowed_Yes);

        rdoInterestWaiverGroup.remove(rdoInterestWaiverAllowed_Yes);
        rdoInterestWaiverGroup.remove(rdoInterestWaiverAllowed_No);

        InterestDueKeptRecivableGroup.remove(rdoInterestDueKeptReceivable_Yes);
        InterestDueKeptRecivableGroup.remove(rdoInterestDueKeptReceivable_No);


        rdoInsuranceApplicableGroup.remove(rdoinsuranceCharge_Yes);
        rdoInsuranceApplicableGroup.remove(rdoinsuranceCharge_No);
        rdoApplyMarketRateGroup.remove(rdosanctionDate_Yes);
        rdoApplyMarketRateGroup.remove(rdosanctionDate_No);


        rdoIsAuctionAmt.remove(rdoIsAuctionAmt_No);
        rdoIsAuctionAmt.remove(rdoIsAuctionAmt_Yes);

        rdoDisbAftMoraPeriod.remove(rdoIsDisbAftMora_No);
        rdoDisbAftMoraPeriod.remove(rdoIsDisbAftMora_Yes);

        rdoSalRec.remove(rdoSalRec_Yes);
        rdoSalRec.remove(rdoSalRec_No);
        
        rdoInterestButtonGroup.remove(rdoIsInterestdue_YES);
        rdoInterestButtonGroup.remove(rdoIsInterestDue_No);
        //-------------------------------------------------------------
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    private String getAccHdDesc(String achdid) {
        // System.out.println("achdidachdidachdidachdidachdid"+achdid);  
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", achdid);
        List accdes = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        //String acchddes= accdes.get(0).toString();
        // System.out.println("acchddesacchddesacchddesacchddes"+acchddes);  
        if (!accdes.isEmpty()) {
            HashMap mapd1 = new HashMap();
            mapd1 = (HashMap) accdes.get(0);
            String acchddes = mapd1.get("AC_HD_DESC").toString();
            //System.out.println("acchddesacchddesacchddesacchddes"+acchddes); 
            return acchddes;
        } else {
            return "";
        }
    }

    @Override
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        if (observable.getChkExcludeTOD() != null) {
            if (observable.getChkExcludeTOD().equals("Y")) {
                chkExcludeTOD.setSelected(true);
            } else {
                chkExcludeTOD.setSelected(false);
            }
        }
        if (observable.getChkEMIInSimpleIntrst() != null) {
            if (observable.getChkEMIInSimpleIntrst().equals("Y")) {
                chkEmiInSimpleInterst.setSelected(true);
            } else {
                chkEmiInSimpleInterst.setSelected(false);
            }
        }
        if (observable.getChkGroupLoan() != null) {
            if (observable.getChkGroupLoan().equals("Y")) {
                chkGroupLoan.setSelected(true);
            } else {
                chkGroupLoan.setSelected(false);
            }
        }
        if (observable.getChkFixed() != null) {
            if (observable.getChkFixed().equals("Y")) {
                chkFixed.setSelected(true);
            } else {
                chkFixed.setSelected(false);
            }
        }
        if (observable.getChkAuctionAllowed() != null) {
            if (observable.getChkAuctionAllowed().equals("Y")) {
                chkGoldAuctionAllowed.setSelected(true);
            } else {
                chkGoldAuctionAllowed.setSelected(false);
            }
        }
        if (observable.getChkShareLink() != null) {
            if (observable.getChkShareLink().equals("Y")) {
                chkShareLink.setSelected(true);
            } else {
                chkShareLink.setSelected(false);
            }
        }
        if (observable.getChkExcludeScSt() != null) {
            if (observable.getChkExcludeScSt().equals("Y")) {
                chkExcludeScSt.setSelected(true);
            } else {
                chkExcludeScSt.setSelected(false);
            }
        }
        txtSuspenseCreditProductID.setText(observable.getTxtSuspenseCreditAchd());
        txtSuspenseDebitProductId.setText(observable.getTxtSuspenseDebitAchd());
        txtProductID.setText(observable.getTxtProductID());
        txtMAxCashPayment.setText(CommonUtil.convertObjToStr(observable.getTxtMaxCashDisb()));
        txtFixedMargin.setText(CommonUtil.convertObjToStr(observable.getTxtFixedMargin()));
        txtProductDesc.setText(observable.getTxtProductDesc());
        cboOperatesLike.setSelectedItem(observable.getCboOperatesLike());
        // Addedd by nithya
        cboRepaymentType.setSelectedItem(observable.getCboRepaymentType());
        cboRepaymentFreq.setSelectedItem(observable.getCboRepaymentFreq());
        // End
        System.out.println("holiday---->" + observable.getCboIfHoliday());
        cboholidayInt.setSelectedItem(observable.getCboIfHoliday());
        // cboProdCategory.setSelectedItem(observable.getCboProdCategory());
        txtNumPatternFollowed.setText(observable.getTxtNumPatternFollowed());
        txtNumPatternFollowedSuffix.setText(observable.getTxtNumPatternFollowedSuffix());
        txtLastAccNum.setText(observable.getTxtLastAccNum());

        rdoIsDisbAftMora_Yes.setSelected(observable.isRdoDisbAftMoraPerd_Yes());
        rdoIsDisbAftMora_No.setSelected(observable.isRdoDisbAftMoraPerd_No());
        rdoIsLimitDefnAllowed_Yes.setSelected(observable.getRdoIsLimitDefnAllowed_Yes());
        rdoIsLimitDefnAllowed_No.setSelected(observable.getRdoIsLimitDefnAllowed_No());
        rdoIsInterestFirst_No.setSelected(observable.isRdoIsInterestFirst_No());
        if (rdoIsInterestFirst_No.isSelected()) {
            disableFrequencyText();
        }
        rdoIsInterestFirst_Yes.setSelected(observable.isRdoIsInterestFirst_Yes());
        rdoIsInterestdue_YES.setSelected(observable.isRdoIsInterestDue_Yes());
        rdoIsInterestDue_No.setSelected(observable.isRdoIsInterestDue_No());
        String prodCategory = "";
        prodCategory = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdCategory.getModel()).getKeyForSelected());
        if (rdoIsInterestFirst_Yes.isSelected() && prodCategory.equals("DAILY_LOAN")) {
            lblFrequencyInDays.setVisible(true);
            txtFrequencyInDays.setVisible(true);
            txtFrequencyInDays.setAllowNumber(true);
            txtFrequencyInDays.setText(CommonUtil.convertObjToStr(observable.getTxtFrequencyInDays()));
        } else {
            disableFrequencyText();
        }
        rdoIsStaffAccOpened_Yes.setSelected(observable.getRdoIsStaffAccOpened_Yes());
        rdoIsStaffAccOpened_No.setSelected(observable.getRdoIsStaffAccOpened_No());
        rdoIsDebitInterUnderClearing_Yes.setSelected(observable.getRdoIsDebitInterUnderClearing_Yes());
        rdoIsDebitInterUnderClearing_No.setSelected(observable.getRdoIsDebitInterUnderClearing_No());
        txtAccHead.setText(observable.getTxtAccHead());
        if (!txtAccHead.getText().equals("")) {
            txtAccHeadDesc.setText(getAccHdDesc(observable.getTxtAccHead()));
            txtAccHeadDesc.setEnabled(false);
        }
        rdoldebitInterCharged_Yes.setSelected(observable.getRdoldebitInterCharged_Yes());
        rdoldebitInterCharged_No.setSelected(observable.getRdoldebitInterCharged_No());
        rdoldebitInterCharged_NoActionPerformed(null);

        rdoIsCreditAllowed_Yes.setSelected(observable.isRdoIsCreditAllowed_Yes());
        rdoIsCreditAllowed_No.setSelected(observable.isRdoIsCreditAllowed_No());
        //want edit

        txtMinDebitInterRate.setText(observable.getTxtMinDebitInterRate());
        txtMaxDebitInterRate.setText(observable.getTxtMaxDebitInterRate());
        txtMinDebitInterAmt.setText(observable.getTxtMinDebitInterAmt());
        txtMaxDebitInterAmt.setText(observable.getTxtMaxDebitInterAmt());
        cboDebInterCalcFreq.setSelectedItem(observable.getCboDebInterCalcFreq());
        cboDebitInterAppFreq.setSelectedItem(observable.getCboDebitInterAppFreq());
        cboDebitInterComFreq.setSelectedItem(observable.getCboDebitInterComFreq());
        cboDebitProdRoundOff.setSelectedItem(observable.getCboDebitProdRoundOff());
        cboDebitInterRoundOff.setSelectedItem(observable.getCboDebitInterRoundOff());
        tdtLastInterCalcDate.setDateValue(observable.getTdtLastInterCalcDate());
        tdtLastInterAppDate.setDateValue(observable.getTdtLastInterAppDate());
        rdoPLRRateAppl_Yes.setSelected(observable.getRdoPLRRateAppl_Yes());
        rdoPLRRateAppl_No.setSelected(observable.getRdoPLRRateAppl_No());
        rdoPLRRateAppl_NoActionPerformed(null);

        txtPLRRate.setText(observable.getTxtPLRRate());
        tdtPLRAppForm.setDateValue(observable.getTdtPLRAppForm());
        rdoPLRApplNewAcc_Yes.setSelected(observable.getRdoPLRApplNewAcc_Yes());
        rdoPLRApplNewAcc_No.setSelected(observable.getRdoPLRApplNewAcc_No());
        rdoPLRApplExistingAcc_Yes.setSelected(observable.getRdoPLRApplExistingAcc_Yes());
        rdoPLRApplExistingAcc_No.setSelected(observable.getRdoPLRApplExistingAcc_No());
        tdtPlrApplAccSancForm.setDateValue(observable.getTdtPlrApplAccSancForm());
        rdoPenalAppl_Yes.setSelected(observable.getRdoPenalAppl_Yes());
        rdoPenalAppl_No.setSelected(observable.getRdoPenalAppl_No());
        rdoasAndWhenCustomer_Yes.setSelected(observable.isRdoasAndWhenCustomer_Yes());
        rdoasAndWhenCustomer_No.setSelected(observable.isRdoasAndWhenCustomer_No());
        rdocalendarFrequency_Yes.setSelected(observable.isRdocalendarFrequency_Yes());
        rdocalendarFrequency_No.setSelected(observable.isRdocalendarFrequency_No());
        rdobill_Yes.setSelected(observable.isRdobill_Yes());
        rdobill_No.setSelected(observable.isRdobill_NO());
        chkprincipalDue.setSelected(observable.isChkprincipalDue());
        chkInterestDue.setSelected(observable.isChkInterestDue());
        rdoPenalAppl_NoActionPerformed(null);

        rdoLimitExpiryInter_Yes.setSelected(observable.getRdoLimitExpiryInter_Yes());
        rdoLimitExpiryInter_No.setSelected(observable.getRdoLimitExpiryInter_No());
        cboPeriodFreq.setSelectedItem(observable.getCboPeriodFreq());
        cboPeriodFreq1.setSelectedItem(observable.getCboPrematureIntCalcFreq());
        cboPrematureIntCalAmt.setSelectedItem(observable.getCboPrematureIntCalAmt());
        rdoLimitExpiryInter_NoActionPerformed(null);

        txtPenalInterRate.setText(observable.getTxtPenalInterRate());
        txtExposureLimit_Prud.setText(observable.getTxtExposureLimit_Prud());
        txtExposureLimit_Prud2.setText(observable.getTxtExposureLimit_Prud2());
        txtExposureLimit_Policy.setText(observable.getTxtExposureLimit_Policy());
        txtExposureLimit_Policy2.setText(observable.getTxtExposureLimit_Policy2());
        cboProductFreq.setSelectedItem(observable.getCboProductFreq());
        txtAccClosingCharges.setText(observable.getTxtAccClosingCharges());
        txtMiscSerCharges.setText(observable.getTxtMiscSerCharges());
        rdoStatCharges_Yes.setSelected(observable.getRdoStatCharges_Yes());
        rdoStatCharges_No.setSelected(observable.getRdoStatCharges_No());
        rdoStatCharges_NoActionPerformed(null);

        txtStatChargesRate.setText(observable.getTxtStatChargesRate());

        //        cboProdCurrency.setSelectedItem(observable.getCboProdCurrency());
        txtRateAmt.setText(observable.getTxtRateAmt());
        txtRangeFrom.setText(observable.getTxtRangeFrom());
        txtRangeTo.setText(observable.getTxtRangeTo());
        rdoFolioChargesAppl_Yes.setSelected(observable.getRdoFolioChargesAppl_Yes());
        rdoFolioChargesAppl_No.setSelected(observable.getRdoFolioChargesAppl_No());
        rdoFolioChargesAppl_NoActionPerformed(null);

        tdtLastFolioChargesAppl.setDateValue(observable.getTdtLastFolioChargesAppl());
        txtNoEntriesPerFolio.setText(observable.getTxtNoEntriesPerFolio());
        tdtNextFolioDDate.setDateValue(observable.getTdtNextFolioDDate());
        txtRatePerFolio.setText(observable.getTxtRatePerFolio());
        cboFolioChargesAppFreq.setSelectedItem(observable.getCboFolioChargesAppFreq());
        cboToCollectFolioCharges.setSelectedItem(observable.getCboToCollectFolioCharges());
        rdoToChargeOn_Man.setSelected(observable.getRdoToChargeOn_Man());
        rdoToChargeOn_Sys.setSelected(observable.getRdoToChargeOn_Sys());
        rdoToChargeOn_Both.setSelected(observable.getRdoToChargeOn_Both());
        rdoToChargeType_Credit.setSelected(observable.isRdoToChargeType_Credit());
        rdoToChargeType_Debit.setSelected(observable.isRdoToChargeType_Debit());
        rdoToChargeType_Both.setSelected(observable.isRdoToChargeType_Both());
        cboIncompleteFolioRoundOffFreq.setSelectedItem(observable.getCboIncompleteFolioRoundOffFreq());
        rdoProcessCharges_Yes.setSelected(observable.getRdoProcessCharges_Yes());
        rdoProcessCharges_No.setSelected(observable.getRdoProcessCharges_No());
        rdoProcessCharges_NoActionPerformed(null);
        cboCharge_Limit2.setSelectedItem(observable.getCboCharge_Limit2());
        txtCharge_Limit2.setText(observable.getTxtCharge_Limit2());
        rdoCommitmentCharge_Yes.setSelected(observable.getRdoCommitmentCharge_Yes());
        rdoCommitmentCharge_No.setSelected(observable.getRdoCommitmentCharge_No());
        rdoCommitmentCharge_NoActionPerformed(null);


        cboCharge_Limit3.setSelectedItem(observable.getCboCharge_Limit3());
        txtCharge_Limit3.setText(observable.getTxtCharge_Limit3());
        txtAccClosCharges.setText(observable.getTxtAccClosCharges());
        if (!txtAccClosCharges.getText().equals("")) {
            btnAccClosCharges.setToolTipText(getAccHdDesc(observable.getTxtAccClosCharges()));
        }
        txtArcCostAccount.setText(observable.getTxtArcCostAccount());
        if (!txtArcCostAccount.getText().equals("")) {
            btnArcCostAccount.setToolTipText(getAccHdDesc(observable.getTxtArcCostAccount()));
        }
        txtArcExpenseAccount.setText(observable.getTxtArcExpenseAccount());
        if (!txtArcExpenseAccount.getText().equals("")) {
            btnArcExpenseAccount.setToolTipText(getAccHdDesc(observable.getTxtArcExpenseAccount()));
        }
        txtEaCostAccount.setText(observable.getTxtEaCostAccount());
        if (!txtEaCostAccount.getText().equals("")) {
            btnEaCostAccount.setToolTipText(getAccHdDesc(observable.getTxtEaCostAccount()));
        }
        txtEaExpenseAccount.setText(observable.getTxtEaExpenseAccount());
        if (!txtEaExpenseAccount.getText().equals("")) {
            btnEaExpenseAccount.setToolTipText(getAccHdDesc(observable.getTxtEaExpenseAccount()));
        }
        txtEpCostAccount.setText(observable.getTxtEpCostAccount());
        if (!txtEpCostAccount.getText().equals("")) {
            btnEpCostAccount.setToolTipText(getAccHdDesc(observable.getTxtEpCostAccount()));
        }
        txtEpExpenseAccount.setText(observable.getTxtEpExpenseAccount());
        if (!txtEpExpenseAccount.getText().equals("")) {
            btnEpExpenseAccount.setToolTipText(getAccHdDesc(observable.getTxtEpExpenseAccount()));
        }
        txtDebitIntDiscount.setText(observable.getTxtDebitIntDiscount());
        if (!txtDebitIntDiscount.getText().equals("")) {
            btnDebitIntDiscount.setToolTipText(getAccHdDesc(observable.getTxtDebitIntDiscount()));
        }
        txtOtherCharges.setText(observable.getTxtOtherCharges());
        if (!txtOtherCharges.getText().equals("")) {
            btnOtherCharges.setToolTipText(getAccHdDesc(observable.getTxtOtherCharges()));
        }
        //btnDebitIntDiscount.setToolTipText(accountHeadDesc);
        txtRebateInterest.setText(observable.getTxtRebateInterest());
        if (!txtRebateInterest.getText().equals("")) {
            btnRebateInterest.setToolTipText(getAccHdDesc(observable.getTxtRebateInterest()));
        }
        txtIntPayableAccount.setText(observable.getTxtIntPayableAccount());
        if (!txtIntPayableAccount.getText().equals("")) {
            btnIntPayableAccount.setToolTipText(getAccHdDesc(observable.getTxtIntPayableAccount()));
        }
        txtMiscServCharges.setText(observable.getTxtMiscServCharges());
        if (!txtMiscServCharges.getText().equals("")) {
            btnMiscServCharges.setToolTipText(getAccHdDesc(observable.getTxtMiscServCharges()));
        }
        txtStatementCharges.setText(observable.getTxtStatementCharges());
        if (!txtStatementCharges.getText().equals("")) {
            System.out.println("getAccHdDesc(observable.getTxtStatementCharges())" + getAccHdDesc(observable.getTxtStatementCharges()));
            btnStatementCharges.setToolTipText(getAccHdDesc(observable.getTxtStatementCharges()));
        }
        txtAccDebitInter.setText(observable.getTxtAccDebitInter());
        if (!txtAccDebitInter.getText().equals("")) {
            btnAccDebitInter.setToolTipText(getAccHdDesc(observable.getTxtAccDebitInter()));
        }
        txtPenalInter.setText(observable.getTxtPenalInter());
        if (!txtPenalInter.getText().equals("")) {
            btnPenalInter.setToolTipText(getAccHdDesc(observable.getTxtPenalInter()));
        }
        txtAccCreditInter.setText(observable.getTxtAccCreditInter());
        if (!txtAccCreditInter.getText().equals("")) {
            btnAccCreditInter.setToolTipText(getAccHdDesc(observable.getTxtAccCreditInter()));
        }
        txtExpiryInter.setText(observable.getTxtExpiryInter());
        if (!txtExpiryInter.getText().equals("")) {
            btnExpiryInter.setToolTipText(getAccHdDesc(observable.getTxtExpiryInter()));
        }
        txtCheReturnChargest_Out.setText(observable.getTxtCheReturnChargest_Out());
        if (!txtCheReturnChargest_Out.getText().equals("")) {
            btnCheReturnChargest_Out.setToolTipText(getAccHdDesc(observable.getTxtCheReturnChargest_Out()));
        }
        txtCheReturnChargest_In.setText(observable.getTxtCheReturnChargest_In());
        if (!txtCheReturnChargest_Out.getText().equals("")) {
            btnCheReturnChargest_In.setToolTipText(getAccHdDesc(observable.getTxtCheReturnChargest_In()));
        }
        txtFolioChargesAcc.setText(observable.getTxtFolioChargesAcc());
        if (!txtFolioChargesAcc.getText().equals("")) {
            btnFolioChargesAcc.setToolTipText(getAccHdDesc(observable.getTxtFolioChargesAcc()));
        }
        txtCommitCharges.setText(observable.getTxtCommitCharges());
        if (!txtCommitCharges.getText().equals("")) {
            btnCommitCharges.setToolTipText(getAccHdDesc(observable.getTxtCommitCharges()));
        }
        txtProcessingCharges.setText(observable.getTxtProcessingCharges());
        if (!txtProcessingCharges.getText().equals("")) {
            btnProcessingCharges.setToolTipText(getAccHdDesc(observable.getTxtProcessingCharges()));
        }
        rdoATMcardIssued_Yes.setSelected(observable.getRdoATMcardIssued_Yes());
        rdoATMcardIssued_No.setSelected(observable.getRdoATMcardIssued_No());
        rdoCreditCardIssued_Yes.setSelected(observable.getRdoCreditCardIssued_Yes());
        rdoCreditCardIssued_No.setSelected(observable.getRdoCreditCardIssued_No());
        rdoMobileBanlingClient_Yes.setSelected(observable.getRdoMobileBanlingClient_Yes());
        rdoMobileBanlingClient_No.setSelected(observable.getRdoMobileBanlingClient_No());
        rdoIsAnyBranBankingAllowed_Yes.setSelected(observable.getRdoIsAnyBranBankingAllowed_Yes());
        rdoIsAnyBranBankingAllowed_No.setSelected(observable.getRdoIsAnyBranBankingAllowed_No());
        txtMinPeriodsArrears.setText(observable.getTxtMinPeriodsArrears());
        cboMinPeriodsArrears.setSelectedItem(observable.getCboMinPeriodsArrears());
        txtPeriodTranSStanAssets.setText(observable.getTxtPeriodTranSStanAssets());
        cboPeriodTranSStanAssets.setSelectedItem(observable.getCboPeriodTranSStanAssets());
        txtPeriodTransDoubtfulAssets1.setText(observable.getTxtPeriodTransDoubtfulAssets1());
        txtPeriodTransDoubtfulAssets2.setText(observable.getTxtPeriodTransDoubtfulAssets2());
        txtPeriodTransDoubtfulAssets3.setText(observable.getTxtPeriodTransDoubtfulAssets3());
        cboPeriodTransDoubtfulAssets1.setSelectedItem(observable.getCboPeriodTransDoubtfulAssets1());
        cboPeriodTransDoubtfulAssets2.setSelectedItem(observable.getCboPeriodTransDoubtfulAssets2());
        cboPeriodTransDoubtfulAssets3.setSelectedItem(observable.getCboPeriodTransDoubtfulAssets3());
        txtPeriodTransLossAssets.setText(observable.getTxtPeriodTransLossAssets());
        cboPeriodTransLossAssets.setSelectedItem(observable.getCboPeriodTransLossAssets());
        txtPeriodAfterWhichTransNPerformingAssets.setText(observable.getTxtPeriodAfterWhichTransNPerformingAssets());
        cboPeriodAfterWhichTransNPerformingAssets.setSelectedItem(observable.getCboPeriodAfterWhichTransNPerformingAssets());
        txtProvisionsStanAssets.setText(observable.getTxtProvisionsStanAssets());
        txtProvisionDoubtfulAssets1.setText(observable.getTxtProvisionDoubtfulAssets1());
        txtProvisionDoubtfulAssets2.setText(observable.getTxtProvisionDoubtfulAssets2());
        txtProvisionDoubtfulAssets3.setText(observable.getTxtProvisionDoubtfulAssets3());
        txtProvisionStandardAssetss.setText(observable.getTxtProvisionStandardAssetss());
        txtProvisionLossAssets.setText(observable.getTxtProvisionLossAssets());
        cbocalcType.setSelectedItem(observable.getCbocalcType());
        txtMinPeriod.setText(observable.getTxtMinPeriod());
        cboMinPeriod.setSelectedItem(observable.getCboMinPeriod());
        txtMaxPeriod.setText(observable.getTxtMaxPeriod());
        cboMaxPeriod.setSelectedItem(observable.getCboMaxPeriod());
        txtMinAmtLoan.setText(observable.getTxtMinAmtLoan());
        txtMaxAmtLoan.setText(observable.getTxtMaxAmtLoan());
        txtApplicableInter.setText(observable.getTxtApplicableInter());
        txtElgLoanAmt.setText(observable.getTxtElgLoanAmt());
        txtDepAmtMaturing.setText(observable.getTxtDepAmtMaturing());
        txtReviewPeriod.setText(observable.getTxtReviewPeriod());
        txtDepAmtLoanMaturingPeriod.setText(observable.getTxtDepAmtLoanMaturingPeriod());
        cboReviewPeriod.setSelectedItem(observable.getCboReviewPeriod());
        cboDepAmtLoanMaturing.setSelectedItem(observable.getCboDepAmtLoanMaturingPeriod());
        cboDepositRoundOff.setSelectedItem(observable.getCboDepositRoundOff());
        /*
         * for Notice charge
         */

        txtPostageAmt.setText(observable.getTxtPostageChargeAmt());
        txtNoticeChargeAmt.setText(observable.getTxtNoticeChargeAmt());
        cboIssueAfter.setSelectedItem(observable.getCboIssueAfter());
        cboNoticeType.setSelectedItem(observable.getCboNoticeType());

        /*
         * add noticechargeacchead
         */

        txtPostageCharges.setText(observable.getTxtPostageCharges());
        if (!txtPostageCharges.getText().equals("")) {
            btnPostageCharges.setToolTipText(getAccHdDesc(observable.getTxtPostageCharges()));
        }
        txtNoticeCharges.setText(observable.getTxtNoticeCharges());
        if (!txtNoticeCharges.getText().equals("")) {
            btnNoticeCharges.setToolTipText(getAccHdDesc(observable.getTxtNoticeCharges()));
        }
        txtMinInterDebited.setText(observable.getTxtMinInterDebited());
        rdoSubsidy_Yes.setSelected(observable.getRdoSubsidy_Yes());
        rdoSubsidy_No.setSelected(observable.getRdoSubsidy_No());
        cboLoanPeriodMul.setSelectedItem(observable.getCboLoanPeriodMul());
        /*
         * charge types
         */
        txtLegalCharges.setText(observable.getTxtLegalCharges());
        if (!txtLegalCharges.getText().equals("")) {
            btnLegalCharges.setToolTipText(getAccHdDesc(observable.getTxtLegalCharges()));
        }
        txtPreMatIntCalctPeriod.setText(observable.getTxtPreMatIntCalctPeriod());
        txtPreMatIntCalctPeriod1.setText(observable.getTxtPreMatIntCollectPeriod());
        txtArbitraryCharges.setText(observable.getTxtArbitraryCharges());
        if (!txtArbitraryCharges.getText().equals("")) {
            btnArbitraryCharges.setToolTipText(getAccHdDesc(observable.getTxtArbitraryCharges()));
        }
        txtInsuranceCharges.setText(observable.getTxtInsuranceCharges());
        if (!txtInsuranceCharges.getText().equals("")) {
            btnInsuranceCharges.setToolTipText(getAccHdDesc(observable.getTxtInsuranceCharges()));
        }
        txtExecutionDecreeCharges.setText(observable.getTxtExecutionDecreeCharges());
        if (!txtExecutionDecreeCharges.getText().equals("")) {
            btnExecutionDecreeCharges.setToolTipText(getAccHdDesc(observable.getTxtExecutionDecreeCharges()));
        }
        log.info("observable.getTblCharges()" + observable.getTblCharges());
        tblCharges.setModel(observable.getTblCharges());
        lblStatus1.setText(observable.getLblStatus());
        rdoMaturity_Yes.setSelected(observable.isRdoMaturity_Yes());
        rdoCurrDt_Yes.setSelected(observable.isRdoCurrDt_Yes());

        rdoMaturity_Deposit_closure_Maturity_Yes.setSelected(observable.isRdoMaturity_Deposit_closure_Yes());
        rdoMaturity_Deposit_closure_Curr_Yes.setSelected(observable.isRdoMaturity_Deposit_closure_Upto_Curr_Yes());
        txtGracePeriodPenalInterest.setText(observable.getTxtGracePeriodPenalInterest());
//        ((CList)lstAvailableDeposits.getModel()).setListData(
        lstAvailableDeposits.setModel(observable.getLstAvailableDeposits());
        lstSelectedDeposits.setModel(observable.getLstSelectedDeposits());
        lstAvailableTypeTransaction.setModel(observable.getLstAvailableTransaction());
        lstSelectedPriorityTransaction.setModel(observable.getLstSelectedTransaction());

        lstAvailableTypeTransaction_OTS.setModel(observable.getLstAvailableTransaction_OTS());
        lstSelectedPriorityTransaction_OTS.setModel(observable.getLstSelectedTransaction_OTS());
        //subsidy rebate
        rdoSubsidy_Yes.setSelected(observable.isRdoSubsidy_Yes());
        rdoSubsidy_No.setSelected(observable.getRdoSubsidy_No());

        rdoLoanBalance_Yes.setSelected(observable.isRdoLoanBalance_Yes());
        rdoSubsidyAdjustLoanBalance_Yes.setSelected(observable.isRdoSubsidyAdjustLoanBalance_Yes());
        rdoSubsidyReceivedDate.setSelected(observable.isRdoSubsidyReceivedDate());
        rdoLoanOpenDate.setSelected(observable.isRdoLoanOpenDate());
        rdoInterestRebateAllowed_Yes.setSelected(observable.isRdoInterestRebateAllowed_Yes());
        rdoInterestRebateAllowed_No.setSelected(observable.isRdoInterestRebateAllowed_No());
        chkRebtSpl.setSelected(observable.isChkRebtSpl());


        txtInterestRebatePercentage.setText(observable.getTxtInterestRebatePercentage());
        txtFinYearStartingFromDD.setText(observable.getTxtFinYearStartingFromDD());
        txtFinYearStartingFromMM.setText(observable.getTxtFinYearStartingFromMM());
        rdoPenalInterestWaiverAllowed_Yes.setSelected(observable.isRdoPenalInterestWaiverAllowed_Yes());
        rdoPenalInterestWaiverAllowed_No.setSelected(observable.isRdoPenalInterestWaiverAllowed_No());
        rdoIsDebitAllowedForDue_Yes.setSelected(observable.isRdoDebitAllowedForCustomer_Yes());
        rdoIsDebitAllowedForDue_No.setSelected(observable.isRdoDebitAllowedForCustomer_No());
        rdoPrincipalWaiverAllowed_No.setSelected(observable.isRdoPrincipalWaiverAllowed_No());
        rdoPrincipalWaiverAllowed_Yes.setSelected(observable.isRdoPrincipalWaiverAllowed_Yes());
        rdoNoticeWaiverAllowed_Yes.setSelected(observable.isRdoNoticeWaiverAllowed_Yes());
        rdoNoticeWaiverAllowed_No.setSelected(observable.isRdoNoticeWaiverAllowed_No());
        rdoInterestWaiverAllowed_Yes.setSelected(observable.isRdoInterestWaiverAllowed_Yes());
        rdoInterestWaiverAllowed_No.setSelected(observable.isRdoInterestWaiverAllowed_No());
        rdoRecoveryWaiverAllowed_Yes.setSelected(observable.isRdoRecoveryWaiverAllowed_Yes());
        rdoRecoveryWaiverAllowed_No.setSelected(observable.isRdoRecoveryWaiverAllowed_No());
        rdoMeasurementWaiverAllowed_Yes.setSelected(observable.isRdoMeasurementWaiverAllowed_Yes());
        rdoMeasurementWaiverAllowed_No.setSelected(observable.isRdoMeasurementWaiverAllowed_No());
        txtRecoveryChargeHead.setText(observable.getTxtRecoveryChargeHead());
        txtRecoveryWaiverHead.setText(observable.getTxtRecoveryWaiverHead());
        txtMeasurementChargeHead.setText(observable.getTxtMeasurementChargeHead());
        txtMeasurementWaiverHead.setText(observable.getTxtMeasurementWaiverHead());
        
        // Kole field operation & expense
        rdoKoleFieldExpenseWaiverAllowed_Yes.setSelected(observable.isRdoKoleFieldExpenseWaiverAllowed_Yes());
        rdoKoleFieldExpenseWaiverAllowed_No.setSelected(observable.isRdoKoleFieldExpenseWaiverAllowed_No());
        rdoKoleFieldOperationWaiverAllowed_Yes.setSelected(observable.isRdoKoleFieldOperationWaiverAllowed_Yes());
        rdoKoleFieldOperationWaiverAllowed_No.setSelected(observable.isRdoKoleFieldOperationWaiverAllowed_No());
        
        txtKoleFieldExpenseHead.setText(observable.getTxtKoleFieldExpenseHead());
        txtKoleFieldExpenseWaiverHead.setText(observable.getTxtKoleFieldExpenseWaiverHead());
        txtKoleFieldOperationHead.setText(observable.getTxtKoleFieldOperationHead());
        txtKoleFieldOperationWaiverHead.setText(observable.getTxtKoleFieldOperationWaiverHead());
        
        // End
        
        
        // end
        //added by rishad 24/04/2015
        rdoArbitaryWaiverAllowed_Yes.setSelected(observable.isRdoArbitraryWaiverAllowed_Yes());
        rdoArbitaryWaiverAllowed_No.setSelected(observable.isRdoArbitraryWaiverAllowed_No());
        rdoAdvertiseWaiverAllowed_Yes.setSelected(observable.isRdoAdvertiseWaiverAllowed_Yes());
        rdoAdvertiseWaiverAllowed_No.setSelected(observable.isRdoAdvertiseWaiverAllowed_No());
        rdoLegalWaiverAllowed_Yes.setSelected(observable.isRdoLeagalWaiverAllowed_Yes());
        rdoLegalWaiverAllowed_No.setSelected(observable.isRdoLeagalWaiverAllowed_No());
        rdoInsurenceWaiverAllowed_Yes.setSelected(observable.isRdoInsurenceWaiverAllowed_Yes());
        rdoInsurenceWaiverAllowed_No.setSelected(observable.isRdoInsurenceWaiverAllowed_No());
        rdoArcWaiverAllowed_Yes.setSelected(observable.isRdoArcWaiverAllowed_Yes());
        rdoArcWaiverAllowed_No.setSelected(observable.isRdoArcWaiverAllowed_No());
        rdoPostageWaiverAllowed_Yes.setSelected(observable.isRdoPostageWaiverAllowed_Yes());
        rdoPostageWaiverAllowed_No.setSelected(observable.isRdoPostageWaiverAllowed_No());
        rdoEpWaiverAllowed_Yes.setSelected(observable.isRdoEpWaiverAllowed_Yes());
        rdoEpWaiverAllowed_No.setSelected(observable.isRdoEpWaiverAllowed_No());
        rdoDecreeWaiverAllowed_Yes.setSelected(observable.isRdoDecreeWaiverAllowed_Yes());
        rdoDecreeWaiverAllowed_No.setSelected(observable.isRdoDecreeWaiverAllowed_No());
        rdoMiscellaneousWaiverAllowed_Yes.setSelected(observable.isRdoMiscellaneousWaiverAllowed_Yes());
        rdoMiscellaneousWaiverAllowed_No.setSelected(observable.isRdoMiscellaneousWaiverAllowed_No());
        //for waive head rishad
        txtArcWaiverHead.setText(observable.getTxtArcWaiveoffHead());
        txtAdvertiseWaiverHead.setText(observable.getTxtAdvertiseWaiveoffHead());
        txtLegalWaiverHead.setText(observable.getTxtLegalWaiveoffHead());
        txtPostageWaiverHead.setText(observable.getTxtPostageWaiveoffHead());
        txtInsurenceWaiverHead.setText(observable.getTxtInsurenceWaiveoffHead());
        txtMiscellaneousWaiverHead.setText(observable.getTxtMiscellaneousWaiveoffHead());
        txtDecreeWaiverHead.setText(observable.getTxtDecreeWaiveoffHead());
        txtEpWaiverHead.setText(observable.getTxtEpWaiveoffHead());
        txtArbitaryWaiveHead.setText(observable.getTxtArbitraryWaiveoffHead());
        txtPrincipleWaiveoffHead.setText(observable.getTxtPrincipleWaiveoffHead());
        txtPenalWaiveoffHead.setText(observable.getTxtPenalWaiveoffHead());
        txtNoticeChargeDebitHead.setText(observable.getTxtNoticeChargeDebitHead());

        rdoInterestDueKeptReceivable_Yes.setSelected(observable.isRdoInterestDueKeptReceivable_Yes());
        rdoInterestDueKeptReceivable_No.setSelected(observable.isRdoInterestDueKeptReceivable_No());
        txtStampAdvancesHead.setText(observable.getTxtStampAdvancesHead());
        if (!txtStampAdvancesHead.getText().equals("")) {
            btnStampAdvancesHead.setToolTipText(getAccHdDesc(observable.getTxtStampAdvancesHead()));
        }
        txtNoticeAdvancesHead.setText(observable.getTxtNoticeAdvancesHead());
        if (!txtNoticeAdvancesHead.getText().equals("")) {
            btnNoticeAdvancesHead.setToolTipText(getAccHdDesc(observable.getTxtNoticeAdvancesHead()));
        }
        txtAdvertisementHead.setText(observable.getTxtAdvertisementHead());
        if (!txtAdvertisementHead.getText().equals("")) {
            btnAdvertisementHead.setToolTipText(getAccHdDesc(observable.getTxtAdvertisementHead()));
        }
        chkCreditStampAdvance.setSelected(observable.isChkCreditStampAdvance());
        chkGoldAuctionAllowed.setSelected(observable.isChkAuctionAllowd());
        chkCreditNoticeAdvance.setSelected(observable.isChkCreditNoticeAdvance());
        txtARCEPSuspenceHead.setText(observable.getTxtARCEPSuspenceHead());
        if (!txtARCEPSuspenceHead.getText().equals("")) {
            btnARCEPSuspenceHead.setToolTipText(getAccHdDesc(observable.getTxtARCEPSuspenceHead()));
        }

        rdoinsuranceCharge_Yes.setSelected(observable.isRdoinsuranceCharge_Yes());
        rdoinsuranceCharge_No.setSelected(observable.isRdoinsuranceCharge_No());
        rdosanctionDate_Yes.setSelected(observable.isRdosanctionDate_Yes());
        rdosanctionDate_No.setSelected(observable.isRdosanctionDate_No());
        txtInsuraceRate.setText(observable.getTxtInsuraceRate());
        rdoIsAuctionAmt_Yes.setSelected(observable.isRdoAuctionAmt_Yes());
        rdoIsAuctionAmt_No.setSelected(observable.isRdoAuctionAmt_No());
        rdoIsDebitAllowedForDue_Yes.setSelected(observable.isRdoDebitAllowedForCustomer_Yes());
        rdoIsDebitAllowedForDue_No.setSelected(observable.isRdoDebitAllowedForCustomer_No());
        cboIntPaymntDay.setSelectedItem(observable.getCboIntCalcDay());
        cboIntPaymntMonth.setSelectedItem(observable.getCboIntCalcMonth());
        cboholidayInt.setSelectedItem(observable.getCboIfHoliday());
        chkInterestOnMaturity.setSelected(observable.isChkInterestOnMaturity());
        ///gold loan renewal  
        System.out.println("isChkGldRenewOldAmt" + observable.isChkGldRenewOldAmt() + "_isChkGldRenewCash" + observable.isChkGldRenewCash() + "_isChkGldRenewMarketRate" + observable.isChkGldRenewMarketRate());
        chkGldRenewOldAmt.setSelected(observable.isChkGldRenewOldAmt());
        chkGldRenewCash.setSelected(observable.isChkGldRenewCash());
        chkGldRenewMarketRate.setSelected(observable.isChkGldRenewMarketRate());
        chkGldRnwOldIntRate.setSelected(observable.isChkGldRnwNwIntRate());

        if (observable.isChkSalRec()) {
            rdoSalRec_Yes.setSelected(true);
            rdoSalRec_No.setSelected(false);
        } else {
            rdoSalRec_No.setSelected(true);
            rdoSalRec_Yes.setSelected(false);
        }
        
        // Added by nithya on 15-11-2017 for 7867
        if(observable.getChkIsInterestPeriodWise().equalsIgnoreCase("Y")){
            chkIsInterestPeriodWise.setSelected(true);
        }else{
            chkIsInterestPeriodWise.setSelected(false);
        }

        updateDocuments();
        updateClassification();
        tblNoticeCharges.setModel(observable.getTblNoticeCharge());

        addRadioButtons();
        rdoInterestRebateAllowed_YesActionPerformed(null);
        rdoSubsidy_YesActionPerformed(null);
        
        if(observable.getChkIsOverdueInt().equalsIgnoreCase("Y")){
            chkIsOverdueInt.setSelected(true);
        }else{
            chkIsOverdueInt.setSelected(false);
        }        
        if(observable.isChkEmiPenal()){
            chkEmiPenal.setSelected(true);
        }else{
            chkEmiPenal.setSelected(false);
        }
        if(observable.isRdoPenalCalcDays()){
            rdoPenalCalcDays.setSelected(true);
        }else{
            rdoPenalCalcDays.setSelected(false);
        }
        if(observable.isRdoPenalCalcMonths()){
            rdoPenalCalcMonths.setSelected(true);
        }else{
            rdoPenalCalcMonths.setSelected(false);
        }
        
        txtOverdueIntHead.setText(observable.getTxtOverdueIntHead());
        txtOverDueWaiverHead.setText(observable.getTxtOverDueWaiverHead());
        
        if(observable.isRdoOverDueWaiverAllowed_Yes()){
            rdoOverDueWaiverAllowed_Yes.setSelected(true);
        }else{
            rdoOverDueWaiverAllowed_Yes.setSelected(false);
        }
        
        if(observable.isRdoOverDueWaiverAllowed_No()){
            rdoOverDueWaiverAllowed_No.setSelected(true);
        }else{
            rdoOverDueWaiverAllowed_No.setSelected(false);
        }
        // Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
        if(observable.getChkPrematureIntCalc().equalsIgnoreCase("Y")){
            chkPrematureIntCalc.setSelected(true);
        }else{
            chkPrematureIntCalc.setSelected(false);
        } 
        // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE   
        if(observable.getChkIntCalcFromSanctionDt().equalsIgnoreCase("Y")){
            chkIntCalcFromSanctionDt.setSelected(true);
        }else{
            chkIntCalcFromSanctionDt.setSelected(false);
        } 
        txtGracePeriodForOverDueInt.setText(observable.getTxtGracePeriodForOverDueInt());
        if(observable.getChkGoldStockPhoto().equalsIgnoreCase("Y")){// Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            chkGoldStockPhoto.setSelected(true);
        }else{
            chkGoldStockPhoto.setSelected(false);
        }
        if(observable.getChkBlockIFLimitExceed().equalsIgnoreCase("Y")){ // Added by nithya on 16-11-2019 for KD-729
            chkBlockIFLimitExceed.setSelected(true);
        }else{
            chkBlockIFLimitExceed.setSelected(false);
        }
        txtRebateLoanIntPercent.setText(observable.getTxtRebateLoanIntPercent()); // Added by nithya on 11-01-2020 for KD-1234
    }

    /**
     * Enable Disable New Save Delete Buttons of Documents Table When Save or
     * Delete is invoked
     */
    private void enableDisableDocuments_SaveDelete() {
        btnDocumentNew.setEnabled(true);
        btnDocumentSave.setEnabled(false);
        btnDocumentDelete.setEnabled(false);
    }

    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "ASSET_ID"); //Here u have to pass assetid or something else
            List list = null;
//            sqlMap.executeUpdate("updateIDGenerated", where);  // This update statement just updates curr_value=curr_value+1
            list = (List) ClientUtil.executeQuery(mapName, where);  // This will get u the updated curr_value, prefix and length
            //sqlMap.commitTransaction();

            if (list.size() > 0) {
                hash = (HashMap) list.get(0);
                String strPrefix = "", strLen = "";

                // Prefix for the ID.
                if (hash.containsKey("PREFIX")) {
                    strPrefix = (String) hash.get("PREFIX");
                    if (strPrefix == null || strPrefix.trim().length() == 0) {
                        strPrefix = "";
                    }
                }

                // Maximum Length for the ID
                int len = 10;
                if (hash.containsKey("ID_LENGTH")) {
                    strLen = String.valueOf(hash.get("ID_LENGTH"));
                    if (strLen == null || strLen.trim().length() == 0) {
                        len = 10;
                    } else {
                        len = Integer.parseInt(strLen.trim());
                    }
                }

                int numFrom = strPrefix.trim().length();

                String newID = String.valueOf(hash.get("CURR_VALUE"));
                System.out.println("newID" + newID);
                long d = (long) Double.parseDouble(newID) + 1;
                System.out.println("d.." + d);
                newID = "";
                newID = "" + d;
                // Number Part of the String and incrementing 1 (ex. only 00085 from OGGOT00085)
                result = new HashMap();
                String genID = strPrefix.toUpperCase() + CommonUtil.lpad(newID, len - numFrom, '0');
                result.put(CommonConstants.DATA, genID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private void enableDisableSubsidyDetails(boolean flag) {
        panSubsidyInterestCalculate.setEnabled(flag);

    }

    private void enableDisablInterestRebateDetails(boolean flag) {
        txtInterestRebatePercentage.setEnabled(flag);
        cboInterestRebateCalculation.setEnabled(flag);
        cboRebatePeriod.setEnabled(flag);
        txtFinYearStartingFromDD.setEnabled(flag);
        txtFinYearStartingFromMM.setEnabled(flag);
        txtRebateLoanIntPercent.setEnabled(flag);//10-01-2020 for KD-1234
    }

    /**
     * EnableDisable New Save Delete Buttons of Documents Table When New is
     * pressed
     */
    private void enableDisableDocuments_NewSaveDelete(boolean flag) {
        btnDocumentNew.setEnabled(flag);
        btnDocumentSave.setEnabled(flag);
        btnDocumentDelete.setEnabled(flag);
    }

    private void enableDisbleNotice_NewSaveDeleter(boolean flag) {
        btnNotice_Charge_New.setEnabled(flag);
        btnNotice_Charge_Save.setEnabled(flag);
        btnNotice_Charge_Delete.setEnabled(flag);
    }

    /**
     * update the document details
     */
    private void updateDocuments() {
        cboDocumentType.setSelectedItem(observable.getCboDocumentType());
        txtDocumentNo.setText(observable.getTxtDocumentNo());
        txtDocumentDesc.setText(observable.getTxtDocumentDesc());
        tblDocuments.setModel(observable.getTblDocuments());
    }

    private void updateClassification() {
        cboCommodityCode.setSelectedItem(observable.getCboCommodityCode());
        cboGuaranteeCoverCode.setSelectedItem(observable.getCboGuaranteeCoverCode());
        cboSectorCode.setSelectedItem(observable.getCboSectorCode());
        cboHealthCode.setSelectedItem(observable.getCboHealthCode());
        cboTypeFacility.setSelectedItem(observable.getCboTypeFacility());
        cboPurposeCode.setSelectedItem(observable.getCboPurposeCode());
        cboIndusCode.setSelectedItem(observable.getCboIndusCode());
        cbo20Code.setSelectedItem(observable.getCbo20Code());
        cboRefinancingInsti.setSelectedItem(observable.getCboRefinancingInsti());
        cboSecurityDeails.setSelectedItem(observable.getCboSecurityDeails());
        cboGovtSchemeCode.setSelectedItem(observable.getCboGovtSchemeCode());
        chkDirectFinance.setSelected(observable.getChkDirectFinance());
        chkECGC.setSelected(observable.getChkECGC());
        chkQIS.setSelected(observable.getChkQIS());
    }

    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //---Account---

        rdoIsInterestFirst = new CButtonGroup();
        rdoIsInterestFirst.add(rdoIsInterestFirst_No);
        rdoIsInterestFirst.add(rdoIsInterestFirst_Yes);

        rdoIsLimitDefnAllowed = new CButtonGroup();
        rdoIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_No);
        rdoIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_Yes);

        rdoIsCreditAllowed = new CButtonGroup();
        rdoIsCreditAllowed.add(rdoIsCreditAllowed_No);
        rdoIsCreditAllowed.add(rdoIsCreditAllowed_Yes);

        rdoIsStaffAccOpened = new CButtonGroup();
        rdoIsStaffAccOpened.add(rdoIsStaffAccOpened_No);
        rdoIsStaffAccOpened.add(rdoIsStaffAccOpened_Yes);

        rdoIsDebitInterUnderClearing = new CButtonGroup();
        rdoIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_No);
        rdoIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_Yes);

        //---Interest Receivable---
        rdoldebitInterCharged = new CButtonGroup();
        rdoldebitInterCharged.add(rdoldebitInterCharged_No);
        rdoldebitInterCharged.add(rdoldebitInterCharged_Yes);

        rdoPLRRateAppl = new CButtonGroup();
        rdoPLRRateAppl.add(rdoPLRRateAppl_No);
        rdoPLRRateAppl.add(rdoPLRRateAppl_Yes);

        rdoPLRApplNewAcc = new CButtonGroup();
        rdoPLRApplNewAcc.add(rdoPLRApplNewAcc_No);
        rdoPLRApplNewAcc.add(rdoPLRApplNewAcc_Yes);

        rdoPLRApplExistingAcc = new CButtonGroup();
        rdoPLRApplExistingAcc.add(rdoPLRApplExistingAcc_No);
        rdoPLRApplExistingAcc.add(rdoPLRApplExistingAcc_Yes);

        rdoPenalAppl = new CButtonGroup();
        rdoPenalAppl.add(rdoPenalAppl_No);
        rdoPenalAppl.add(rdoPenalAppl_Yes);

        rdoLimitExpiryInter = new CButtonGroup();
        rdoLimitExpiryInter.add(rdoLimitExpiryInter_No);
        rdoLimitExpiryInter.add(rdoLimitExpiryInter_Yes);
        //---Charges---
        rdoStatCharges = new CButtonGroup();
        rdoStatCharges.add(rdoStatCharges_No);
        rdoStatCharges.add(rdoStatCharges_Yes);

        rdoFolioChargesAppl = new CButtonGroup();
        rdoFolioChargesAppl.add(rdoFolioChargesAppl_No);
        rdoFolioChargesAppl.add(rdoFolioChargesAppl_Yes);

        rdoToChargeOn = new CButtonGroup();
        rdoToChargeOn.add(rdoToChargeOn_Both);
        rdoToChargeOn.add(rdoToChargeOn_Man);
        rdoToChargeOn.add(rdoToChargeOn_Sys);

        rdoLimitExpiryInter = new CButtonGroup();
        rdoLimitExpiryInter.add(rdoLimitExpiryInter_No);
        rdoLimitExpiryInter.add(rdoLimitExpiryInter_Yes);

        rdoLimitExpiryInter = new CButtonGroup();
        rdoLimitExpiryInter.add(rdoLimitExpiryInter_No);
        rdoLimitExpiryInter.add(rdoLimitExpiryInter_Yes);
        //---Special Items---
        rdoATMcardIssued = new CButtonGroup();
        rdoATMcardIssued.add(rdoATMcardIssued_No);
        rdoATMcardIssued.add(rdoATMcardIssued_Yes);

        rdoCreditCardIssued = new CButtonGroup();
        rdoCreditCardIssued.add(rdoCreditCardIssued_No);
        rdoCreditCardIssued.add(rdoCreditCardIssued_Yes);

        rdoMobileBanlingClient = new CButtonGroup();
        rdoMobileBanlingClient.add(rdoMobileBanlingClient_No);
        rdoMobileBanlingClient.add(rdoMobileBanlingClient_Yes);

        rdoIsAnyBranBankingAllowed = new CButtonGroup();
        rdoIsAnyBranBankingAllowed.add(rdoIsAnyBranBankingAllowed_No);
        rdoIsAnyBranBankingAllowed.add(rdoIsAnyBranBankingAllowed_Yes);

        rdoCommitmentCharge = new CButtonGroup();
        rdoCommitmentCharge.add(rdoCommitmentCharge_No);
        rdoCommitmentCharge.add(rdoCommitmentCharge_Yes);

        rdoProcessCharges = new CButtonGroup();
        rdoProcessCharges.add(rdoProcessCharges_No);
        rdoProcessCharges.add(rdoProcessCharges_Yes);
        //---Account Head---
        //---Non Performing Account---
        //---Interest Calculation---
        rdoSubsidy = new CButtonGroup();
        rdoSubsidy.add(rdoSubsidy_No);
        rdoSubsidy.add(rdoSubsidy_Yes);

        rdoLTDinterestGroup = new CButtonGroup();
        rdoLTDinterestGroup.add(rdoMaturity_Yes);
        rdoLTDinterestGroup.add(rdoCurrDt_Yes);

        rdoMaturityDepositClosureGroup = new CButtonGroup();
        rdoMaturityDepositClosureGroup.add(rdoMaturity_Deposit_closure_Maturity_Yes);
        rdoMaturityDepositClosureGroup.add(rdoMaturity_Deposit_closure_Curr_Yes);

        rdoSubsidy = new CButtonGroup();
        rdoSubsidy.add(rdoSubsidy_Yes);
        rdoSubsidy.add(rdoSubsidy_No);

        rdosubsidyInterestCalculateGroup = new CButtonGroup();
        rdosubsidyInterestCalculateGroup.add(rdoLoanBalance_Yes);
        rdosubsidyInterestCalculateGroup.add(rdoSubsidyAdjustLoanBalance_Yes);

        rdoFromSubsidyReceivedDateGroup = new CButtonGroup();
        rdoFromSubsidyReceivedDateGroup.add(rdoSubsidyReceivedDate);
        rdoFromSubsidyReceivedDateGroup.add(rdoLoanOpenDate);

        rdoInterestRebateAllowedGroup = new CButtonGroup();
        rdoInterestRebateAllowedGroup.add(rdoInterestRebateAllowed_Yes);
        rdoInterestRebateAllowedGroup.add(rdoInterestRebateAllowed_No);

        rdoPenalInterestWaiverGroup = new CButtonGroup();
        rdoPenalInterestWaiverGroup.add(rdoPenalInterestWaiverAllowed_Yes);
        rdoPenalInterestWaiverGroup.add(rdoPenalInterestWaiverAllowed_No);

        rdoPrincipalInterestWaiverGroup = new CButtonGroup();
        rdoPrincipalInterestWaiverGroup.add(rdoPrincipalWaiverAllowed_Yes);
        rdoPrincipalInterestWaiverGroup.add(rdoPrincipalWaiverAllowed_No);

        rdoRecoveryWaiverGroup = new CButtonGroup();
        rdoRecoveryWaiverGroup.add(rdoRecoveryWaiverAllowed_Yes);
        rdoRecoveryWaiverGroup.add(rdoRecoveryWaiverAllowed_No);
        
        rdoMeasurementWaiverGroup = new CButtonGroup();
        rdoMeasurementWaiverGroup.add(rdoMeasurementWaiverAllowed_Yes);
        rdoMeasurementWaiverGroup.add(rdoMeasurementWaiverAllowed_No);
        
        
        rdoKoleFieldOperationWaiverGroup = new CButtonGroup();
        rdoKoleFieldOperationWaiverGroup.add(rdoKoleFieldOperationWaiverAllowed_Yes);
        rdoKoleFieldOperationWaiverGroup.add(rdoKoleFieldOperationWaiverAllowed_No);
        
        rdoKoleFieldExpenseWaiverGroup = new CButtonGroup();
        rdoKoleFieldExpenseWaiverGroup.add(rdoKoleFieldExpenseWaiverAllowed_Yes);
        rdoKoleFieldExpenseWaiverGroup.add(rdoKoleFieldExpenseWaiverAllowed_No);

        rdoInterestWaiverGroup = new CButtonGroup();
        rdoInterestWaiverGroup.add(rdoInterestWaiverAllowed_Yes);
        rdoInterestWaiverGroup.add(rdoInterestWaiverAllowed_No);

        InterestDueKeptRecivableGroup = new CButtonGroup();
        InterestDueKeptRecivableGroup.add(rdoInterestDueKeptReceivable_Yes);
        InterestDueKeptRecivableGroup.add(rdoInterestDueKeptReceivable_No);

        rdoApplyMarketRateGroup = new CButtonGroup();
        rdoApplyMarketRateGroup.add(rdosanctionDate_Yes);
        rdoApplyMarketRateGroup.add(rdosanctionDate_No);


        rdoInsuranceApplicableGroup = new CButtonGroup();
        rdoInsuranceApplicableGroup.add(rdoinsuranceCharge_Yes);
        rdoInsuranceApplicableGroup.add(rdoinsuranceCharge_No);

        rdoIsAuctionAmt = new CButtonGroup();
        rdoIsAuctionAmt.add(rdoIsAuctionAmt_No);
        rdoIsAuctionAmt.add(rdoIsAuctionAmt_Yes);

        rdoDisbAftMoraPeriod = new CButtonGroup();
        rdoDisbAftMoraPeriod.add(rdoIsDisbAftMora_No);
        rdoDisbAftMoraPeriod.add(rdoIsDisbAftMora_Yes);

        rdoSalRec = new CButtonGroup();
        rdoSalRec.add(rdoSalRec_Yes);
        rdoSalRec.add(rdoSalRec_No);
        
        
        rdoInterestButtonGroup=new CButtonGroup();
        rdoInterestButtonGroup.add(rdoIsInterestdue_YES);
        rdoInterestButtonGroup.add(rdoIsInterestDue_No);


        //-------------------------------------------------------------
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        if (chkExcludeTOD.isSelected()) {
            observable.setChkExcludeTOD("Y");
        } else {
            observable.setChkExcludeTOD("N");
        }
        if (chkGoldAuctionAllowed.isSelected()) {
            observable.setChkAuctionAllowed("Y");
        } else {
            observable.setChkAuctionAllowed("N");
        }
        if (chkEmiInSimpleInterst.isSelected()) {
            observable.setChkEMIInSimpleIntrst("Y");
        } else {
            observable.setChkEMIInSimpleIntrst("N");
        }
        if (chkGroupLoan.isSelected()) {
            observable.setChkGroupLoan("Y");
        } else {
            observable.setChkGroupLoan("N");
        }
        if (chkShareLink.isSelected()) {
            observable.setChkShareLink("Y");
        } else {
            observable.setChkShareLink("N");
        }
        if (chkExcludeScSt.isSelected()) {
            observable.setChkExcludeScSt("Y");
        } else {
            observable.setChkExcludeScSt("N");
        }
        if (chkFixed.isSelected()) {
            observable.setChkFixed("Y");
        } else {
            observable.setChkFixed("N");
        }
        if (rdoIsDebitAllowedForDue_Yes.isSelected()) {
            observable.setRdoDebitAllowedForCustomer_Yes(true);
        } else {
            observable.setRdoDebitAllowedForCustomer_Yes(false);
        }
        observable.setRdoPrincipalWaiverAllowed_Yes(rdoPrincipalWaiverAllowed_Yes.isSelected());
        observable.setRdoPrincipalWaiverAllowed_No(rdoPrincipalWaiverAllowed_No.isSelected());

        observable.setRdoDebitAllowedForCustomer_No(rdoIsDebitAllowedForDue_Yes.isSelected());
        observable.setRdoDebitAllowedForCustomer_No(rdoIsDebitAllowedForDue_No.isSelected());
        observable.setCboIntCalcDay(CommonUtil.convertObjToStr(cboIntPaymntDay.getSelectedItem()));
        observable.setCboIntCalcMonth(CommonUtil.convertObjToStr(cboIntPaymntMonth.getSelectedItem()));
        observable.setCboIfHoliday(CommonUtil.convertObjToStr(cboholidayInt.getSelectedItem()));
        observable.setRdoNoticeWaiverAllowed_Yes(rdoNoticeWaiverAllowed_Yes.isSelected());
        observable.setRdoNoticeWaiverAllowed_No(rdoNoticeWaiverAllowed_No.isSelected());
        observable.setTxtSuspenseCreditAchd(txtSuspenseCreditProductID.getText());
        observable.setTxtSuspenseDebitAchd(txtSuspenseDebitProductId.getText());
        observable.setTxtProductID(txtProductID.getText());
        observable.setTxtFixedMargin(CommonUtil.convertObjToDouble(txtFixedMargin.getText()));
        observable.setTxtProductDesc(txtProductDesc.getText());

        observable.setTxtNumPatternFollowed(txtNumPatternFollowed.getText());
        observable.setTxtNumPatternFollowedSuffix(txtNumPatternFollowedSuffix.getText());
        observable.setTxtLastAccNum(txtLastAccNum.getText());
        observable.setTxtPenalWaiveoffHead(txtPenalWaiveoffHead.getText());

        observable.setTxtPrincipleWaiveoffHead(txtPrincipleWaiveoffHead.getText());
        observable.setTxtNoticeChargeDebitHead(txtNoticeChargeDebitHead.getText());
        observable.setRdoIsLimitDefnAllowed_Yes(rdoIsLimitDefnAllowed_Yes.isSelected());
        observable.setRdoIsLimitDefnAllowed_No(rdoIsLimitDefnAllowed_No.isSelected());
        observable.setRdoIsInterestFirst_No(rdoIsInterestFirst_No.isSelected());
        observable.setRdoIsInterestFirst_Yes(rdoIsInterestFirst_Yes.isSelected());
        observable.setRdoIsInterestDue_Yes(rdoIsInterestdue_YES.isSelected());
        observable.setRdoIsInterestDue_No(rdoIsInterestDue_No.isSelected());
        observable.setTxtFrequencyInDays(CommonUtil.convertObjToInt(txtFrequencyInDays.getText()));

        observable.setRdoIsCreditAllowed_Yes(rdoIsCreditAllowed_Yes.isSelected());
        observable.setRdoIsCreditAllowed_No(rdoIsCreditAllowed_No.isSelected());

        observable.setRdoIsStaffAccOpened_Yes(rdoIsStaffAccOpened_Yes.isSelected());
        observable.setRdoIsStaffAccOpened_No(rdoIsStaffAccOpened_No.isSelected());
        observable.setRdoIsDebitInterUnderClearing_Yes(rdoIsDebitInterUnderClearing_Yes.isSelected());
        observable.setRdoIsDebitInterUnderClearing_No(rdoIsDebitInterUnderClearing_No.isSelected());
        observable.setRdoAuctionAmt_Yes(rdoIsAuctionAmt_Yes.isSelected());
        observable.setRdoAuctionAmt_No(rdoIsAuctionAmt_No.isSelected());
        observable.setRdoDisbAftMoraPerd_Yes(rdoIsDisbAftMora_Yes.isSelected());
        observable.setRdoDisbAftMoraPerd_No(rdoIsDisbAftMora_No.isSelected());
        observable.setTxtAccHead(txtAccHead.getText());
        observable.setRdoldebitInterCharged_Yes(rdoldebitInterCharged_Yes.isSelected());
        observable.setRdoldebitInterCharged_No(rdoldebitInterCharged_No.isSelected());
        observable.setTxtMinDebitInterRate(txtMinDebitInterRate.getText());
        observable.setTxtMaxDebitInterRate(txtMaxDebitInterRate.getText());
        observable.setTxtMinDebitInterAmt(txtMinDebitInterAmt.getText());
        observable.setTxtMaxDebitInterAmt(txtMaxDebitInterAmt.getText());
        observable.setCboDebInterCalcFreq((String) cboDebInterCalcFreq.getSelectedItem());
        observable.setCboDebitInterAppFreq((String) cboDebitInterAppFreq.getSelectedItem());
        observable.setCboDebitInterComFreq((String) cboDebitInterComFreq.getSelectedItem());
        observable.setCboDebitProdRoundOff((String) cboDebitProdRoundOff.getSelectedItem());
        observable.setCboDebitInterRoundOff((String) cboDebitInterRoundOff.getSelectedItem());
        observable.setTdtLastInterCalcDate(tdtLastInterCalcDate.getDateValue());
        observable.setTdtLastInterAppDate(tdtLastInterAppDate.getDateValue());
        observable.setRdoPLRRateAppl_Yes(rdoPLRRateAppl_Yes.isSelected());
        observable.setRdoPLRRateAppl_No(rdoPLRRateAppl_No.isSelected());
        observable.setTxtPLRRate(txtPLRRate.getText());
        observable.setTxtMaxCashDisb(CommonUtil.convertObjToDouble(txtMAxCashPayment.getText()));
        observable.setTdtPLRAppForm(tdtPLRAppForm.getDateValue());
        observable.setRdoPLRApplNewAcc_Yes(rdoPLRApplNewAcc_Yes.isSelected());
        observable.setRdoPLRApplNewAcc_No(rdoPLRApplNewAcc_No.isSelected());
        observable.setRdoPLRApplExistingAcc_Yes(rdoPLRApplExistingAcc_Yes.isSelected());
        observable.setRdoPLRApplExistingAcc_No(rdoPLRApplExistingAcc_No.isSelected());
        observable.setTdtPlrApplAccSancForm(tdtPlrApplAccSancForm.getDateValue());
        observable.setRdoPenalAppl_Yes(rdoPenalAppl_Yes.isSelected());
        observable.setRdoPenalAppl_No(rdoPenalAppl_No.isSelected());
        observable.setRdoLimitExpiryInter_Yes(rdoLimitExpiryInter_Yes.isSelected());
        observable.setRdoLimitExpiryInter_No(rdoLimitExpiryInter_No.isSelected());
        observable.setTxtPenalInterRate(txtPenalInterRate.getText());
        observable.setTxtExposureLimit_Prud(txtExposureLimit_Prud.getText());
        observable.setTxtExposureLimit_Prud2(txtExposureLimit_Prud2.getText());
        observable.setTxtExposureLimit_Policy(txtExposureLimit_Policy.getText());
        observable.setTxtExposureLimit_Policy2(txtExposureLimit_Policy2.getText());
        observable.setCboProductFreq((String) cboProductFreq.getSelectedItem());
        observable.setTxtAccClosingCharges(txtAccClosingCharges.getText());
        observable.setTxtMiscSerCharges(txtMiscSerCharges.getText());
        observable.setRdoStatCharges_Yes(rdoStatCharges_Yes.isSelected());
        observable.setRdoStatCharges_No(rdoStatCharges_No.isSelected());
        observable.setRdoasAndWhenCustomer_Yes(rdoasAndWhenCustomer_Yes.isSelected());
        observable.setRdoasAndWhenCustomer_No(rdoasAndWhenCustomer_No.isSelected());
        observable.setChkprincipalDue(chkprincipalDue.isSelected());
        observable.setChkInterestDue(chkInterestDue.isSelected());
        //        observable.setCboProdCurrency((String) cboProdCurrency.getSelectedItem());
        observable.setTxtRateAmt(txtRateAmt.getText());

        observable.setTxtRangeFrom(txtRangeFrom.getText());
        observable.setTxtRangeTo(txtRangeTo.getText());

        observable.setRdoFolioChargesAppl_Yes(rdoFolioChargesAppl_Yes.isSelected());
        observable.setRdoFolioChargesAppl_No(rdoFolioChargesAppl_No.isSelected());
        observable.setTdtLastFolioChargesAppl(tdtLastFolioChargesAppl.getDateValue());
        observable.setTxtNoEntriesPerFolio(txtNoEntriesPerFolio.getText());
        observable.setTdtNextFolioDDate(tdtNextFolioDDate.getDateValue());
        observable.setTxtRatePerFolio(txtRatePerFolio.getText());
        observable.setCboFolioChargesAppFreq((String) cboFolioChargesAppFreq.getSelectedItem());
        observable.setCboPeriodFreq((String) cboPeriodFreq.getSelectedItem());
        observable.setCboPrematureIntCalcFreq((String) cboPeriodFreq1.getSelectedItem());
        observable.setCboPrematureIntCalAmt((String) cboPrematureIntCalAmt.getSelectedItem());
        observable.setCboToCollectFolioCharges((String) cboToCollectFolioCharges.getSelectedItem());
        observable.setRdoToChargeOn_Man(rdoToChargeOn_Man.isSelected());
        observable.setRdoToChargeOn_Sys(rdoToChargeOn_Sys.isSelected());
        observable.setRdoToChargeOn_Both(rdoToChargeOn_Both.isSelected());
        observable.setRdoToChargeType_Credit(rdoToChargeType_Credit.isSelected());
        observable.setRdoToChargeType_Debit(rdoToChargeType_Debit.isSelected());
        observable.setRdoToChargeType_Both(rdoToChargeType_Both.isSelected());
        observable.setCboIncompleteFolioRoundOffFreq((String) cboIncompleteFolioRoundOffFreq.getSelectedItem());
        observable.setRdoProcessCharges_Yes(rdoProcessCharges_Yes.isSelected());
        observable.setRdoProcessCharges_No(rdoProcessCharges_No.isSelected());
        observable.setTxtCharge_Limit2(txtCharge_Limit2.getText());
        observable.setCboCharge_Limit2((String) cboCharge_Limit2.getSelectedItem());
        observable.setRdoCommitmentCharge_Yes(rdoCommitmentCharge_Yes.isSelected());
        observable.setRdoCommitmentCharge_No(rdoCommitmentCharge_No.isSelected());
        observable.setTxtCharge_Limit3(txtCharge_Limit3.getText());
        observable.setCboCharge_Limit3((String) cboCharge_Limit3.getSelectedItem());
        observable.setTxtAccClosCharges(txtAccClosCharges.getText());
        observable.setTxtArcCostAccount(txtArcCostAccount.getText());
        observable.setTxtArcExpenseAccount(txtArcExpenseAccount.getText());
        observable.setTxtEaCostAccount(txtEaCostAccount.getText());
        observable.setTxtEaExpenseAccount(txtEaExpenseAccount.getText());
        observable.setTxtEpCostAccount(txtEpCostAccount.getText());
        observable.setTxtEpExpenseAccount(txtEpExpenseAccount.getText());
        observable.setTxtDebitIntDiscount(txtDebitIntDiscount.getText());
        observable.setTxtRebateInterest(txtRebateInterest.getText());
        observable.setTxtIntPayableAccount(txtIntPayableAccount.getText());
        observable.setTxtMiscServCharges(txtMiscServCharges.getText());
        observable.setTxtStatementCharges(txtStatementCharges.getText());
        observable.setTxtAccDebitInter(txtAccDebitInter.getText());
        observable.setTxtPenalInter(txtPenalInter.getText());
        observable.setTxtAccCreditInter(txtAccCreditInter.getText());
        observable.setTxtExpiryInter(txtExpiryInter.getText());
        observable.setTxtCheReturnChargest_Out(txtCheReturnChargest_Out.getText());
        observable.setTxtCheReturnChargest_In(txtCheReturnChargest_In.getText());
        observable.setTxtFolioChargesAcc(txtFolioChargesAcc.getText());
        observable.setTxtCommitCharges(txtCommitCharges.getText());
        observable.setTxtProcessingCharges(txtProcessingCharges.getText());
        observable.setRdoATMcardIssued_Yes(rdoATMcardIssued_Yes.isSelected());
        observable.setRdoATMcardIssued_No(rdoATMcardIssued_No.isSelected());
        observable.setRdoCreditCardIssued_Yes(rdoCreditCardIssued_Yes.isSelected());
        observable.setRdoCreditCardIssued_No(rdoCreditCardIssued_No.isSelected());
        observable.setRdoMobileBanlingClient_Yes(rdoMobileBanlingClient_Yes.isSelected());
        observable.setRdoMobileBanlingClient_No(rdoMobileBanlingClient_No.isSelected());
        observable.setRdoIsAnyBranBankingAllowed_Yes(rdoIsAnyBranBankingAllowed_Yes.isSelected());
        observable.setRdoIsAnyBranBankingAllowed_No(rdoIsAnyBranBankingAllowed_No.isSelected());
        observable.setTxtMinPeriodsArrears(txtMinPeriodsArrears.getText());
        observable.setCboMinPeriodsArrears((String) cboMinPeriodsArrears.getSelectedItem());
        observable.setTxtPeriodTranSStanAssets(txtPeriodTranSStanAssets.getText());
        observable.setCboPeriodTranSStanAssets((String) cboPeriodTranSStanAssets.getSelectedItem());
        observable.setTxtPeriodTransDoubtfulAssets1(txtPeriodTransDoubtfulAssets1.getText());
        observable.setTxtPeriodTransDoubtfulAssets2(txtPeriodTransDoubtfulAssets2.getText());
        observable.setTxtPeriodTransDoubtfulAssets3(txtPeriodTransDoubtfulAssets3.getText());
        observable.setCboPeriodTransDoubtfulAssets1((String) cboPeriodTransDoubtfulAssets1.getSelectedItem());
        observable.setCboPeriodTransDoubtfulAssets2((String) cboPeriodTransDoubtfulAssets2.getSelectedItem());
        observable.setCboPeriodTransDoubtfulAssets3((String) cboPeriodTransDoubtfulAssets3.getSelectedItem());
        observable.setTxtPeriodTransLossAssets(txtPeriodTransLossAssets.getText());
        observable.setCboPeriodTransLossAssets((String) cboPeriodTransLossAssets.getSelectedItem());
        observable.setTxtPeriodAfterWhichTransNPerformingAssets(txtPeriodAfterWhichTransNPerformingAssets.getText());
        observable.setCboPeriodAfterWhichTransNPerformingAssets((String) cboPeriodAfterWhichTransNPerformingAssets.getSelectedItem());
        observable.setTxtProvisionsStanAssets(txtProvisionsStanAssets.getText());
        observable.setTxtProvisionDoubtfulAssets1(txtProvisionDoubtfulAssets1.getText());
        observable.setTxtProvisionDoubtfulAssets2(txtProvisionDoubtfulAssets2.getText());
        observable.setTxtProvisionDoubtfulAssets3(txtProvisionDoubtfulAssets3.getText());
        observable.setTxtProvisionStandardAssetss(txtProvisionStandardAssetss.getText());
        observable.setTxtProvisionLossAssets(txtProvisionLossAssets.getText());
        observable.setCbocalcType((String) cbocalcType.getSelectedItem());
        observable.setTxtMinPeriod(txtMinPeriod.getText());
        observable.setCboMinPeriod((String) cboMinPeriod.getSelectedItem());
        observable.setTxtMaxPeriod(txtMaxPeriod.getText());
        observable.setCboMaxPeriod((String) cboMaxPeriod.getSelectedItem());
        observable.setTxtMinAmtLoan(txtMinAmtLoan.getText());
        observable.setTxtMaxAmtLoan(txtMaxAmtLoan.getText());
        observable.setTxtApplicableInter(txtApplicableInter.getText());
        observable.setTxtMinInterDebited(txtMinInterDebited.getText());
        observable.setTxtLegalCharges(txtLegalCharges.getText());
        observable.setTxtPreMatIntCalctPeriod(txtPreMatIntCalctPeriod.getText());
        observable.setTxtPreMatIntCollectPeriod(txtPreMatIntCalctPeriod1.getText());
        observable.setTxtArbitraryCharges(txtArbitraryCharges.getText());
        observable.setTxtInsuranceCharges(txtInsuranceCharges.getText());
        observable.setTxtExecutionDecreeCharges(txtExecutionDecreeCharges.getText());
        observable.setRdoSubsidy_Yes(rdoSubsidy_Yes.isSelected());
        observable.setRdoSubsidy_No(rdoSubsidy_No.isSelected());
        observable.setCboLoanPeriodMul((String) cboLoanPeriodMul.getSelectedItem());

        observable.setCboReviewPeriod((String) cboReviewPeriod.getSelectedItem());
        observable.setCboDepAmtLoanMaturingPeriod((String) cboDepAmtLoanMaturing.getSelectedItem());
        observable.setTxtReviewPeriod(txtReviewPeriod.getText());
        observable.setTxtDepAmtLoanMaturingPeriod(txtDepAmtLoanMaturingPeriod.getText());
        observable.setTxtElgLoanAmt(txtElgLoanAmt.getText());
        observable.setTxtDepAmtMaturing(txtDepAmtMaturing.getText());
        observable.setTblCharges((com.see.truetransact.clientutil.EnhancedTableModel) tblCharges.getModel());
        observable.setCboDocumentType((String) cboDocumentType.getSelectedItem());
        observable.setTxtDocumentDesc(txtDocumentDesc.getText());
        observable.setTxtDocumentNo(txtDocumentNo.getText());
        observable.setTblDocuments((com.see.truetransact.clientutil.EnhancedTableModel) tblDocuments.getModel());
        observable.setCboDepositRoundOff((String) cboDepositRoundOff.getSelectedItem());


        observable.setCboCommodityCode((String) cboCommodityCode.getSelectedItem());
        observable.setCboGuaranteeCoverCode((String) cboGuaranteeCoverCode.getSelectedItem());
        observable.setCboSectorCode((String) cboSectorCode.getSelectedItem());
        observable.setCboHealthCode((String) cboHealthCode.getSelectedItem());
        observable.setCboTypeFacility((String) cboTypeFacility.getSelectedItem());
        observable.setCboPurposeCode((String) cboPurposeCode.getSelectedItem());
        observable.setCboIndusCode((String) cboIndusCode.getSelectedItem());
        observable.setCbo20Code((String) cbo20Code.getSelectedItem());
        observable.setCboRefinancingInsti((String) cboRefinancingInsti.getSelectedItem());
        observable.setCboSecurityDeails((String) cboSecurityDeails.getSelectedItem());
        observable.setCboGovtSchemeCode((String) cboGovtSchemeCode.getSelectedItem());
        observable.setChkDirectFinance(chkDirectFinance.isSelected());
        observable.setChkECGC(chkECGC.isSelected());
        observable.setChkQIS(chkQIS.isSelected());

        /*
         * for Notice Charge
         */
        observable.setCboIssueAfter((String) cboIssueAfter.getSelectedItem());
        observable.setCboNoticeType((String) cboNoticeType.getSelectedItem());
        observable.setTxtNoticeChargeAmt(txtNoticeChargeAmt.getText());
        observable.setTxtPostageChargeAmt(txtPostageAmt.getText());
        observable.setTxtPostageCharges(txtPostageCharges.getText());
        observable.setTxtNoticeCharges(txtNoticeCharges.getText());
        observable.setRdobill_Yes(rdobill_Yes.isSelected());
        observable.setRdobill_NO(rdobill_No.isSelected());
        observable.setRdocalendarFrequency_Yes(rdocalendarFrequency_Yes.isSelected());
        observable.setRdocalendarFrequency_No(rdocalendarFrequency_No.isSelected());

        observable.setRdoMaturity_Yes(rdoMaturity_Yes.isSelected());
        observable.setRdoCurrDt_Yes(rdoCurrDt_Yes.isSelected());

        observable.setRdoMaturity_Deposit_closure_Yes(rdoMaturity_Deposit_closure_Maturity_Yes.isSelected());
        observable.setRdoMaturity_Deposit_closure_Upto_Curr_Yes(rdoMaturity_Deposit_closure_Curr_Yes.isSelected());
        observable.setTxtGracePeriodPenalInterest(txtGracePeriodPenalInterest.getText());

        //subsidy
        observable.setRdoLoanBalance_Yes(rdoLoanBalance_Yes.isSelected());
        observable.setRdoSubsidyAdjustLoanBalance_Yes(rdoSubsidyAdjustLoanBalance_Yes.isSelected());
        observable.setRdoSubsidyReceivedDate(rdoSubsidyReceivedDate.isSelected());
        observable.setRdoLoanOpenDate(rdoLoanOpenDate.isSelected());
        observable.setRdoLoanOpenDate(rdoLoanOpenDate.isSelected());
        observable.setRdoInterestRebateAllowed_Yes(rdoInterestRebateAllowed_Yes.isSelected());
        observable.setRdoInterestRebateAllowed_No(rdoInterestRebateAllowed_No.isSelected());
        observable.setTxtInterestRebatePercentage(txtInterestRebatePercentage.getText());
        observable.setTxtFinYearStartingFromDD(txtFinYearStartingFromDD.getText());
        observable.setTxtFinYearStartingFromMM(txtFinYearStartingFromMM.getText());
        observable.setChkRebtSpl(chkRebtSpl.isSelected());

        observable.setRdoPenalInterestWaiverAllowed_Yes(rdoPenalInterestWaiverAllowed_Yes.isSelected());
        observable.setRdoPenalInterestWaiverAllowed_No(rdoPenalInterestWaiverAllowed_No.isSelected());
        observable.setRdoInterestWaiverAllowed_No(rdoInterestWaiverAllowed_No.isSelected());
        observable.setRdoInterestWaiverAllowed_Yes(rdoInterestWaiverAllowed_Yes.isSelected());
        
        observable.setRdoRecoveryWaiverAllowed_Yes(rdoRecoveryWaiverAllowed_Yes.isSelected());
        observable.setRdoRecoveryWaiverAllowed_No(rdoRecoveryWaiverAllowed_No.isSelected());
        
        observable.setRdoMeasurementWaiverAllowed_Yes(rdoMeasurementWaiverAllowed_Yes.isSelected());
        observable.setRdoMeasurementWaiverAllowed_No(rdoMeasurementWaiverAllowed_No.isSelected());
        
        observable.setTxtRecoveryChargeHead(txtRecoveryChargeHead.getText());
        observable.setTxtRecoveryWaiverHead(txtRecoveryWaiverHead.getText());
        observable.setTxtMeasurementChargeHead(txtMeasurementChargeHead.getText());
        observable.setTxtMeasurementWaiverHead(txtMeasurementWaiverHead.getText());
        
        
        observable.setRdoKoleFieldExpenseWaiverAllowed_Yes(rdoKoleFieldExpenseWaiverAllowed_Yes.isSelected());
        observable.setRdoKoleFieldExpenseWaiverAllowed_No(rdoKoleFieldExpenseWaiverAllowed_No.isSelected());
        
        observable.setRdoKoleFieldOperationWaiverAllowed_Yes(rdoKoleFieldOperationWaiverAllowed_Yes.isSelected());
        observable.setRdoKoleFieldOperationWaiverAllowed_No(rdoKoleFieldOperationWaiverAllowed_No.isSelected());
        
        observable.setTxtKoleFieldExpenseHead(txtKoleFieldExpenseHead.getText());
        observable.setTxtKoleFieldExpenseWaiverHead(txtKoleFieldExpenseWaiverHead.getText());
        observable.setTxtKoleFieldOperationHead(txtKoleFieldOperationHead.getText());
        observable.setTxtKoleFieldOperationWaiverHead(txtKoleFieldOperationWaiverHead.getText());
        
        //added by 24/04/2015
        observable.setRdoArcWaiverAllowed_Yes(rdoArcWaiverAllowed_Yes.isSelected());
        observable.setRdoArcWaiverAllowed_No(rdoArcWaiverAllowed_No.isSelected());
        observable.setRdoArbitraryWaiverAllowed_Yes(rdoArbitaryWaiverAllowed_Yes.isSelected());
        observable.setRdoArbitraryWaiverAllowed_No(rdoArbitaryWaiverAllowed_No.isSelected());
        observable.setRdoAdvertiseWaiverAllowed_Yes(rdoAdvertiseWaiverAllowed_Yes.isSelected());
        observable.setRdoAdvertiseWaiverAllowed_No(rdoAdvertiseWaiverAllowed_No.isSelected());
        observable.setRdoMiscellaneousWaiverAllowed_Yes(rdoMiscellaneousWaiverAllowed_Yes.isSelected());
        observable.setRdoMiscellaneousWaiverAllowed_No(rdoMiscellaneousWaiverAllowed_No.isSelected());
        observable.setRdoLeagalWaiverAllowed_Yes(rdoLegalWaiverAllowed_Yes.isSelected());
        observable.setRdoLeagalWaiverAllowed_No(rdoLegalWaiverAllowed_No.isSelected());
        observable.setRdoDecreeWaiverAllowed_Yes(rdoDecreeWaiverAllowed_Yes.isSelected());
        observable.setRdoDecreeWaiverAllowed_No(rdoDecreeWaiverAllowed_No.isSelected());
        observable.setRdoInsurenceWaiverAllowed_Yes(rdoInsurenceWaiverAllowed_Yes.isSelected());
        observable.setRdoInsurenceWaiverAllowed_No(rdoInsurenceWaiverAllowed_No.isSelected());
        observable.setRdoEpWaiverAllowed_Yes(rdoEpWaiverAllowed_Yes.isSelected());
        observable.setRdoEpWaiverAllowed_No(rdoEpWaiverAllowed_No.isSelected());
        observable.setRdoPostageWaiverAllowed_Yes(rdoPostageWaiverAllowed_Yes.isSelected());
        observable.setRdoPostageWaiverAllowed_No(rdoPostageWaiverAllowed_No.isSelected());
        //for seting waiver achd rishad
        observable.setTxtArcWaiveoffHead(txtArcWaiverHead.getText());
        observable.setTxtInsurenceWaiveoffHead(txtInsurenceWaiverHead.getText());
        observable.setTxtLegalWaiveoffHead(txtLegalWaiverHead.getText());
        observable.setTxtArbitraryWaiveoffHead(txtArbitaryWaiveHead.getText());
        observable.setTxtAdvertiseWaiveoffHead(txtAdvertiseWaiverHead.getText());
        observable.setTxtMiscellaneousWaiveoffHead(txtMiscellaneousWaiverHead.getText());
        observable.setTxtDecreeWaiveoffHead(txtDecreeWaiverHead.getText());
        observable.setTxtEpWaiveoffHead(txtEpWaiverHead.getText());
        observable.setTxtPostageWaiveoffHead(txtPostageWaiverHead.getText());

        observable.setChkCreditStampAdvance(chkCreditStampAdvance.isSelected());
        observable.setChkAuctionAllowd(chkGoldAuctionAllowed.isSelected());
        observable.setChkCreditNoticeAdvance(chkCreditNoticeAdvance.isSelected());
        observable.setTxtStampAdvancesHead(txtStampAdvancesHead.getText());
        observable.setTxtNoticeAdvancesHead(txtNoticeAdvancesHead.getText());
        observable.setTxtAdvertisementHead(txtAdvertisementHead.getText());
        observable.setTxtARCEPSuspenceHead(txtARCEPSuspenceHead.getText());
        observable.setRdoInterestDueKeptReceivable_Yes(rdoInterestDueKeptReceivable_Yes.isSelected());
        observable.setRdoInterestDueKeptReceivable_No(rdoInterestDueKeptReceivable_No.isSelected());

        observable.setRdoinsuranceCharge_Yes(rdoinsuranceCharge_Yes.isSelected());
        observable.setRdoinsuranceCharge_No(rdoinsuranceCharge_No.isSelected());
        observable.setRdosanctionDate_Yes(rdosanctionDate_Yes.isSelected());
        observable.setRdosanctionDate_No(rdosanctionDate_No.isSelected());
        observable.setTxtInsuraceRate(txtInsuraceRate.getText());


        //for gold loan renewal case
        observable.setChkGldRenewOldAmt(chkGldRenewOldAmt.isSelected());
        observable.setChkGldRenewCash(chkGldRenewCash.isSelected());
        observable.setChkGldRenewMarketRate(chkGldRenewMarketRate.isSelected());
        observable.setChkGldRnwNwIntRate(chkGldRnwOldIntRate.isSelected());
        observable.setTxtOtherCharges(txtOtherCharges.getText());
        observable.setChkInterestOnMaturity(chkInterestOnMaturity.isSelected());

        if (rdoSalRec_Yes.isSelected()) {
            observable.setChkSalRec(true);
        } else {
            observable.setChkSalRec(false);
        }


        //-- Added by nithya 
        observable.setCboRepaymentType((String) cboRepaymentType.getSelectedItem());
        System.out.println("NewLoanProductUI :: UpdateOBFields :: " + observable.getCboRepaymentType());
        observable.setCboRepaymentFreq((String) cboRepaymentFreq.getSelectedItem());
        //-- End
        // Added by nithya on 15-11-2017 for 0007867: Gold Loan Interest rate needed to set as slab wise [ Product level parameter added ]
        if(chkIsInterestPeriodWise.isSelected()){
            observable.setChkIsInterestPeriodWise("Y");
        }else{
            observable.setChkIsInterestPeriodWise("N");
        }
        //End
        
        if(chkIsOverdueInt.isSelected()){
            observable.setChkIsOverdueInt("Y");
        }else{
            observable.setChkIsOverdueInt("N");
        }        
        observable.setRdoPenalCalcDays(rdoPenalCalcDays.isSelected());
        observable.setRdoPenalCalcMonths(rdoPenalCalcMonths.isSelected());
        observable.setChkEmiPenal(chkEmiPenal.isSelected());       
        observable.setRdoOverDueWaiverAllowed_Yes(rdoOverDueWaiverAllowed_Yes.isSelected());
        observable.setRdoOverDueWaiverAllowed_No(rdoOverDueWaiverAllowed_No.isSelected());
        observable.setTxtOverDueWaiverHead(txtOverDueWaiverHead.getText());
        observable.setTxtOverdueIntHead(txtOverdueIntHead.getText());    
        // Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
        if(chkPrematureIntCalc.isSelected()){
            observable.setChkPrematureIntCalc("Y");
        }else{
            observable.setChkPrematureIntCalc("N");
        }
        // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE
        if(chkIntCalcFromSanctionDt.isSelected()){
            observable.setChkIntCalcFromSanctionDt("Y");
        }else{
            observable.setChkIntCalcFromSanctionDt("N");
        }
        observable.setTxtGracePeriodForOverDueInt(txtGracePeriodForOverDueInt.getText());
        // End
        
        if(chkGoldStockPhoto.isSelected()){ // Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            observable.setChkGoldStockPhoto("Y");
        }else{
            observable.setChkGoldStockPhoto("N");
        }
        
        if(chkBlockIFLimitExceed.isSelected()){ // Added by nithya on 16-11-2019 for KD-729
            observable.setChkBlockIFLimitExceed("Y");
        }else{
            observable.setChkBlockIFLimitExceed("N");
        }
        
        if(chkRebtSpl.isSelected()){ // Added by nithya on 11-01-2020 for KD-1234
            if(txtRebateLoanIntPercent.getText().length() == 0){
                observable.setTxtRebateLoanIntPercent("1.0");
            }else{
                observable.setTxtRebateLoanIntPercent(txtRebateLoanIntPercent.getText());
            }
        }else{
            observable.setTxtRebateLoanIntPercent("1.0");
        }
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        NewLoanProductMRB objMandatoryRB = new NewLoanProductMRB();
        txtProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductID"));
        txtProductDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductDesc"));
        cboProdCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdCategory"));
        txtNumPatternFollowed.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumPatternFollowed"));
        txtLastAccNum.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastAccNum"));
        rdoIsLimitDefnAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsLimitDefnAllowed_Yes"));
        rdoIsStaffAccOpened_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsStaffAccOpened_Yes"));
        rdoIsDebitInterUnderClearing_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsDebitInterUnderClearing_Yes"));
        txtAccHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccHead"));
        //		cboProdCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdCurrency"));
        txtNumPatternFollowedSuffix.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumPatternFollowedSuffix"));
        rdoldebitInterCharged_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoldebitInterCharged_Yes"));
        txtMinDebitInterRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDebitInterRate"));
        txtMaxDebitInterRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDebitInterRate"));
        txtMinDebitInterAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDebitInterAmt"));
        txtMaxDebitInterAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDebitInterAmt"));
        cboDebInterCalcFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebInterCalcFreq"));
        cboDebitInterAppFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitInterAppFreq"));
        cboDebitInterComFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitInterComFreq"));
        cboDebitProdRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitProdRoundOff"));
        cboDebitInterRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitInterRoundOff"));
        cboInterestRepaymentFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInterestRepaymentFreq"));
        tdtLastInterCalcDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastInterCalcDate"));
        tdtLastInterAppDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastInterAppDate"));
        rdoPLRRateAppl_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPLRRateAppl_Yes"));
        txtPLRRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPLRRate"));
        tdtPLRAppForm.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPLRAppForm"));
        rdoPLRApplNewAcc_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPLRApplNewAcc_Yes"));
        rdoPLRApplExistingAcc_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPLRApplExistingAcc_Yes"));
        tdtPlrApplAccSancForm.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPlrApplAccSancForm"));
        rdoPenalAppl_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPenalAppl_Yes"));
        rdoLimitExpiryInter_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLimitExpiryInter_Yes"));
        txtPenalInterRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalInterRate"));
        txtExposureLimit_Prud.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExposureLimit_Prud"));
        txtExposureLimit_Prud2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExposureLimit_Prud2"));
        txtExposureLimit_Policy.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExposureLimit_Policy"));
        txtExposureLimit_Policy2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExposureLimit_Policy2"));
        cboProductFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductFreq"));
        txtAccClosingCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccClosingCharges"));
        txtMiscSerCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiscSerCharges"));
        rdoStatCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoStatCharges_Yes"));
        txtStatChargesRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStatChargesRate"));
        txtRateAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateAmt"));
        txtRangeFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRangeFrom"));
        txtRangeTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRangeTo"));
        rdoFolioChargesAppl_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoFolioChargesAppl_Yes"));
        tdtLastFolioChargesAppl.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastFolioChargesAppl"));
        txtNoEntriesPerFolio.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoEntriesPerFolio"));
        tdtNextFolioDDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNextFolioDDate"));
        cboFolioChargesAppFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFolioChargesAppFreq"));
        cboToCollectFolioCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToCollectFolioCharges"));
        rdoToChargeOn_Man.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoToChargeOn_Man"));
        cboIncompleteFolioRoundOffFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIncompleteFolioRoundOffFreq"));
        txtRatePerFolio.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRatePerFolio"));
        rdoProcessCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoProcessCharges_Yes"));
        txtCharge_Limit2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCharge_Limit2"));
        cboCharge_Limit2.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCharge_Limit2"));
        rdoCommitmentCharge_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCommitmentCharge_Yes"));
        txtCharge_Limit3.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCharge_Limit3"));
        cboCharge_Limit3.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCharge_Limit3"));
        txtAccClosCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccClosCharges"));
        txtArcCostAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArcCostAccount"));
        txtArcExpenseAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArcExpenseAccount"));
        txtEaCostAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEaCostAccount"));
        txtEaExpenseAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEaExpenseAccount"));
        txtEpCostAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEpCostAccount"));
        txtEpExpenseAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEpExpenseAccount"));
        txtIntPayableAccount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntPayableAccount"));
        txtMiscServCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiscServCharges"));
        txtStatementCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStatementCharges"));
        txtAccDebitInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccDebitInter"));
        txtPenalInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalInter"));
        txtAccCreditInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAccCreditInter"));
        txtExpiryInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExpiryInter"));
        txtCheReturnChargest_Out.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCheReturnChargest_Out"));
        txtCheReturnChargest_In.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCheReturnChargest_In"));
        txtFolioChargesAcc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFolioChargesAcc"));
        txtCommitCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommitCharges"));
        txtProcessingCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProcessingCharges"));
        rdoATMcardIssued_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoATMcardIssued_Yes"));
        rdoCreditCardIssued_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCreditCardIssued_Yes"));
        rdoMobileBanlingClient_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMobileBanlingClient_Yes"));
        rdoIsAnyBranBankingAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIsAnyBranBankingAllowed_Yes"));
        txtMinPeriodsArrears.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinPeriodsArrears"));
        cboMinPeriodsArrears.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinPeriodsArrears"));
        txtPeriodTranSStanAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriodTranSStanAssets"));
        cboPeriodTranSStanAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriodTranSStanAssets"));
        txtPeriodTransDoubtfulAssets1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriodTransDoubtfulAssets1"));
        txtPeriodTransDoubtfulAssets2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriodTransDoubtfulAssets2"));
        txtPeriodTransDoubtfulAssets3.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriodTransDoubtfulAssets3"));
        cboPeriodTransDoubtfulAssets1.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriodTransDoubtfulAssets1"));
        cboPeriodTransDoubtfulAssets2.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriodTransDoubtfulAssets2"));
        cboPeriodTransDoubtfulAssets3.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriodTransDoubtfulAssets3"));
        txtPeriodTransLossAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriodTransLossAssets"));
        cboPeriodTransLossAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriodTransLossAssets"));
        txtPeriodAfterWhichTransNPerformingAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriodAfterWhichTransNPerformingAssets"));
        cboPeriodAfterWhichTransNPerformingAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriodAfterWhichTransNPerformingAssets"));
        txtProvisionsStanAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvisionsStanAssets"));
        txtProvisionDoubtfulAssets1.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvisionDoubtfulAssets1"));
        txtProvisionDoubtfulAssets2.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvisionDoubtfulAssets2"));
        txtProvisionDoubtfulAssets3.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvisionDoubtfulAssets3"));
        txtProvisionStandardAssetss.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvisionStandardAssetss"));
        txtProvisionLossAssets.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProvisionLossAssets"));
        cbocalcType.setHelpMessage(lblMsg, objMandatoryRB.getString("cbocalcType"));
        txtMinPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinPeriod"));
        cboMinPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinPeriod"));
        txtMaxPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxPeriod"));
        cboMaxPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMaxPeriod"));
        txtMinAmtLoan.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinAmtLoan"));
        txtMaxAmtLoan.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxAmtLoan"));
        txtApplicableInter.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplicableInter"));
        txtMinInterDebited.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinInterDebited"));
        rdoSubsidy_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSubsidy_Yes"));
        cboLoanPeriodMul.setHelpMessage(lblMsg, objMandatoryRB.getString("cboLoanPeriodMul"));
        cboDocumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDocumentType"));
        txtDocumentNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDocumentNo"));
        txtDocumentDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDocumentDesc"));
        cboCommodityCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCommodityCode"));
        cboGuaranteeCoverCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGuaranteeCoverCode"));
        cboSectorCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSectorCode"));
        cboHealthCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboHealthCode"));
        cboTypeFacility.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTypeFacility"));
        cboPurposeCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPurposeCode"));
        cboIndusCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIndusCode"));
        cboRefinancingInsti.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRefinancingInsti"));
        cboGovtSchemeCode.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGovtSchemeCode"));
        chkDirectFinance.setHelpMessage(lblMsg, objMandatoryRB.getString("chkDirectFinance"));
        chkECGC.setHelpMessage(lblMsg, objMandatoryRB.getString("chkECGC"));
        chkQIS.setHelpMessage(lblMsg, objMandatoryRB.getString("chkQIS"));
        cbo20Code.setHelpMessage(lblMsg, objMandatoryRB.getString("cbo20Code"));
        cboSecurityDeails.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSecurityDeails"));
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());

    }

    private void setInterestDueKeptReceivableVisible(boolean flag) {
        lblInterestDueKeptRecivable.setVisible(flag);
        rdoInterestDueKeptReceivable_Yes.setVisible(flag);
        rdoInterestDueKeptReceivable_No.setVisible(flag);
        panInterestDueKeptRecivable.setVisible(flag);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoIsLimitDefnAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsStaffAccOpened = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsDebitInterUnderClearing = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoldebitInterCharged = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPLRRateAppl = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPLRApplNewAcc = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPLRApplExistingAcc = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPenalAppl = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLimitExpiryInter = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoFolioChargesAppl = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoToChargeOn = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoATMcardIssued = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCreditCardIssued = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMobileBanlingClient = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsAnyBranBankingAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidy = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoProcessCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCommitmentCharge = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoFolioChargeType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoasAndWhenCustomer = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCalendarFrequency = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoBillByBill = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLTDinterestGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMaturityDepositClosureGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdosubsidyInterestCalculateGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoFromSubsidyReceivedDateGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterestRebateAllowedGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPenalInterestWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterestWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        InterestDueKeptRecivableGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInsuranceApplicableGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoApplyMarketRateGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsAuctionAmt = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsCreditAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDisbAftMoraPeriod = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSalRec = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoArbitaryWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        PostageWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        AdvertiseWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        Miscellaneouswaivergroup = new com.see.truetransact.uicomponent.CButtonGroup();
        DecreeWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        EpWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        ArcWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        InsurenceWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        LegalWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterestButtonGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panLoanProduct = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabLoanProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panAccount = new com.see.truetransact.uicomponent.CPanel();
        sptAcc = new com.see.truetransact.uicomponent.CSeparator();
        panIsAuctionAmt2 = new com.see.truetransact.uicomponent.CPanel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        panGldRenewal = new com.see.truetransact.uicomponent.CPanel();
        lblGldRenewOldAmt = new com.see.truetransact.uicomponent.CLabel();
        lblGldRenewCash = new com.see.truetransact.uicomponent.CLabel();
        lblGldRenewMarketRate = new com.see.truetransact.uicomponent.CLabel();
        chkGldRenewOldAmt = new com.see.truetransact.uicomponent.CCheckBox();
        chkGldRenewCash = new com.see.truetransact.uicomponent.CCheckBox();
        chkGldRenewMarketRate = new com.see.truetransact.uicomponent.CCheckBox();
        lblGldRnwOldIntRate = new com.see.truetransact.uicomponent.CLabel();
        chkGldRnwOldIntRate = new com.see.truetransact.uicomponent.CCheckBox();
        panProdExtraFeatures = new com.see.truetransact.uicomponent.CPanel();
        chkExcludeScSt = new com.see.truetransact.uicomponent.CCheckBox();
        chkShareLink = new com.see.truetransact.uicomponent.CCheckBox();
        chkExcludeTOD = new com.see.truetransact.uicomponent.CCheckBox();
        panProdCatg = new com.see.truetransact.uicomponent.CPanel();
        lblOperatesLike = new com.see.truetransact.uicomponent.CLabel();
        cboOperatesLike = new com.see.truetransact.uicomponent.CComboBox();
        lblProdCategory = new com.see.truetransact.uicomponent.CLabel();
        cboProdCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblProdCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboProdCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblNumPatternFollowed = new com.see.truetransact.uicomponent.CLabel();
        txtNumPatternFollowed = new com.see.truetransact.uicomponent.CTextField();
        txtNumPatternFollowedSuffix = new com.see.truetransact.uicomponent.CTextField();
        lblLastAccNum = new com.see.truetransact.uicomponent.CLabel();
        txtLastAccNum = new com.see.truetransact.uicomponent.CTextField();
        panProdSettings = new com.see.truetransact.uicomponent.CPanel();
        lblIsInterestFirst = new com.see.truetransact.uicomponent.CLabel();
        panIsLimitDefnAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoIsLimitDefnAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsLimitDefnAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIsStaffAccOpened = new com.see.truetransact.uicomponent.CLabel();
        panIsStaffAccOpened = new com.see.truetransact.uicomponent.CPanel();
        rdoIsStaffAccOpened_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsStaffAccOpened_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIsDebitInterUnderClearing = new com.see.truetransact.uicomponent.CLabel();
        lblIsAuctionAmt = new com.see.truetransact.uicomponent.CLabel();
        panIsAuctionAmt = new com.see.truetransact.uicomponent.CPanel();
        rdoIsAuctionAmt_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsAuctionAmt_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIsDisbAftMoraPerd = new com.see.truetransact.uicomponent.CLabel();
        panDIsbAftMoraPeriod = new com.see.truetransact.uicomponent.CPanel();
        rdoIsDisbAftMora_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsDisbAftMora_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCollectIntOnMaturity = new com.see.truetransact.uicomponent.CLabel();
        chkInterestOnMaturity = new com.see.truetransact.uicomponent.CCheckBox();
        panIsDebitInterUnderClearing = new com.see.truetransact.uicomponent.CPanel();
        rdoIsDebitInterUnderClearing_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsDebitInterUnderClearing_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMAxCashPayment = new com.see.truetransact.uicomponent.CLabel();
        txtMAxCashPayment = new com.see.truetransact.uicomponent.CTextField();
        lblEMIInSimpleInterest = new com.see.truetransact.uicomponent.CLabel();
        chkEmiInSimpleInterst = new com.see.truetransact.uicomponent.CCheckBox();
        lblIsLimitDefnAllowed = new com.see.truetransact.uicomponent.CLabel();
        panIsInterestFirst = new com.see.truetransact.uicomponent.CPanel();
        rdoIsInterestFirst_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsInterestFirst_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblFrequencyInDays = new com.see.truetransact.uicomponent.CLabel();
        txtFrequencyInDays = new com.see.truetransact.uicomponent.CTextField();
        lblSalaryRecovery = new com.see.truetransact.uicomponent.CLabel();
        panSalRec = new com.see.truetransact.uicomponent.CPanel();
        rdoSalRec_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSalRec_No = new com.see.truetransact.uicomponent.CRadioButton();
        cLabel24 = new com.see.truetransact.uicomponent.CLabel();
        rdoIsInterestdue_YES = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsInterestDue_No = new com.see.truetransact.uicomponent.CRadioButton();
        chkIsInterestPeriodWise = new com.see.truetransact.uicomponent.CCheckBox();
        panProdDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        lblProductDesc = new com.see.truetransact.uicomponent.CLabel();
        txtProductDesc = new com.see.truetransact.uicomponent.CTextField();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        txtAccHead = new com.see.truetransact.uicomponent.CTextField();
        txtAccHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        btnAccHead = new com.see.truetransact.uicomponent.CButton();
        panGroupLoan = new com.see.truetransact.uicomponent.CPanel();
        chkGroupLoan = new com.see.truetransact.uicomponent.CCheckBox();
        chkGoldStockPhoto = new com.see.truetransact.uicomponent.CCheckBox();
        chkBlockIFLimitExceed = new com.see.truetransact.uicomponent.CCheckBox();
        panInterReceivable = new com.see.truetransact.uicomponent.CPanel();
        panInterReceivable_Debit = new com.see.truetransact.uicomponent.CPanel();
        lbldebitInterCharged = new com.see.truetransact.uicomponent.CLabel();
        panldebitInterCharged = new com.see.truetransact.uicomponent.CPanel();
        rdoldebitInterCharged_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoldebitInterCharged_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMinDebitInterRate = new com.see.truetransact.uicomponent.CLabel();
        panMinDebitInterRate = new com.see.truetransact.uicomponent.CPanel();
        txtMinDebitInterRate = new com.see.truetransact.uicomponent.CTextField();
        lblMinDebitInterRate_Per = new com.see.truetransact.uicomponent.CLabel();
        lblMaxDebitInterRate = new com.see.truetransact.uicomponent.CLabel();
        panMaxDebitInterRate = new com.see.truetransact.uicomponent.CPanel();
        txtMaxDebitInterRate = new com.see.truetransact.uicomponent.CTextField();
        lblMaxDebitInterRate_Per = new com.see.truetransact.uicomponent.CLabel();
        lblMinDebitInterAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMinDebitInterAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMaxDebitInterAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaxDebitInterAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDebInterCalcFreq = new com.see.truetransact.uicomponent.CLabel();
        cboDebInterCalcFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitInterAppFreq = new com.see.truetransact.uicomponent.CLabel();
        cboDebitInterAppFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitInterComFreq = new com.see.truetransact.uicomponent.CLabel();
        cboDebitInterComFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitProdRoundOff = new com.see.truetransact.uicomponent.CLabel();
        cboDebitProdRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitInterRoundOff = new com.see.truetransact.uicomponent.CLabel();
        cboDebitInterRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        lblLastInterCalcDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLastInterCalcDate = new com.see.truetransact.uicomponent.CDateField();
        lblLastInterAppDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLastInterAppDate = new com.see.truetransact.uicomponent.CDateField();
        lblasAndWhenCustomer = new com.see.truetransact.uicomponent.CLabel();
        lblcalendar = new com.see.truetransact.uicomponent.CLabel();
        panasAndWhenCustomer = new com.see.truetransact.uicomponent.CPanel();
        rdoasAndWhenCustomer_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoasAndWhenCustomer_No = new com.see.truetransact.uicomponent.CRadioButton();
        panCalendarFrequency = new com.see.truetransact.uicomponent.CPanel();
        rdocalendarFrequency_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdocalendarFrequency_No = new com.see.truetransact.uicomponent.CRadioButton();
        panBillbyBill = new com.see.truetransact.uicomponent.CPanel();
        rdobill_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdobill_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblBillbyBill = new com.see.truetransact.uicomponent.CLabel();
        panLTDInterest = new com.see.truetransact.uicomponent.CPanel();
        lblCollectIntTill = new com.see.truetransact.uicomponent.CLabel();
        rdoMaturity_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCurrDt_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblCollectIntTillLoanClosureMenu = new com.see.truetransact.uicomponent.CLabel();
        lblCollectIntTillDepositClosureMenu = new com.see.truetransact.uicomponent.CLabel();
        panLTDInterest1 = new com.see.truetransact.uicomponent.CPanel();
        lblCollectIntTillDepositClosure = new com.see.truetransact.uicomponent.CLabel();
        rdoMaturity_Deposit_closure_Maturity_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMaturity_Deposit_closure_Curr_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblInterestRepaymentFreq = new com.see.truetransact.uicomponent.CLabel();
        cboInterestRepaymentFreq = new com.see.truetransact.uicomponent.CComboBox();
        chkIsOverdueInt = new com.see.truetransact.uicomponent.CCheckBox();
        chkPrematureIntCalc = new com.see.truetransact.uicomponent.CCheckBox();
        panInterReceivable_PLR = new com.see.truetransact.uicomponent.CPanel();
        lblPLRRateAppl = new com.see.truetransact.uicomponent.CLabel();
        panPLRRateAppl = new com.see.truetransact.uicomponent.CPanel();
        rdoPLRRateAppl_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPLRRateAppl_No = new com.see.truetransact.uicomponent.CRadioButton();
        panPLRRate = new com.see.truetransact.uicomponent.CPanel();
        txtPLRRate = new com.see.truetransact.uicomponent.CTextField();
        lblPLRRate_Per = new com.see.truetransact.uicomponent.CLabel();
        lblPLRRate = new com.see.truetransact.uicomponent.CLabel();
        lblPLRAppForm = new com.see.truetransact.uicomponent.CLabel();
        tdtPLRAppForm = new com.see.truetransact.uicomponent.CDateField();
        lblPLRApplNewAcc = new com.see.truetransact.uicomponent.CLabel();
        panPLRApplNewAcc = new com.see.truetransact.uicomponent.CPanel();
        rdoPLRApplNewAcc_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPLRApplNewAcc_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPLRApplExistingAcc = new com.see.truetransact.uicomponent.CLabel();
        panPLRApplExistingAcc = new com.see.truetransact.uicomponent.CPanel();
        rdoPLRApplExistingAcc_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPLRApplExistingAcc_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPlrApplAccSancForm = new com.see.truetransact.uicomponent.CLabel();
        tdtPlrApplAccSancForm = new com.see.truetransact.uicomponent.CDateField();
        lblPenalAppl = new com.see.truetransact.uicomponent.CLabel();
        panPenalAppl = new com.see.truetransact.uicomponent.CPanel();
        rdoPenalAppl_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenalAppl_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblLimitExpiryInter = new com.see.truetransact.uicomponent.CLabel();
        panLimitExpiryInter = new com.see.truetransact.uicomponent.CPanel();
        rdoLimitExpiryInter_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLimitExpiryInter_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPenalInterRate = new com.see.truetransact.uicomponent.CLabel();
        panPenalInterRate = new com.see.truetransact.uicomponent.CPanel();
        txtPenalInterRate = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInterRate_Per = new com.see.truetransact.uicomponent.CLabel();
        lblExposureLimit_Prud = new com.see.truetransact.uicomponent.CLabel();
        panExposureLimit_Prud = new com.see.truetransact.uicomponent.CPanel();
        txtExposureLimit_Prud = new com.see.truetransact.uicomponent.CTextField();
        txtExposureLimit_Prud2 = new com.see.truetransact.uicomponent.CTextField();
        lblExposureLimit_Prud_Per = new com.see.truetransact.uicomponent.CLabel();
        lblExposureLimit_Policy = new com.see.truetransact.uicomponent.CLabel();
        panExposureLimit_Policy = new com.see.truetransact.uicomponent.CPanel();
        txtExposureLimit_Policy = new com.see.truetransact.uicomponent.CTextField();
        txtExposureLimit_Policy2 = new com.see.truetransact.uicomponent.CTextField();
        lblExposureLimit_Policy_Per = new com.see.truetransact.uicomponent.CLabel();
        lblProductFreq = new com.see.truetransact.uicomponent.CLabel();
        cboProductFreq = new com.see.truetransact.uicomponent.CComboBox();
        panPenalAppl_Due = new com.see.truetransact.uicomponent.CPanel();
        chkprincipalDue = new com.see.truetransact.uicomponent.CCheckBox();
        chkInterestDue = new com.see.truetransact.uicomponent.CCheckBox();
        chkEmiPenal = new com.see.truetransact.uicomponent.CCheckBox();
        lblPenalDue = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureCloserMinCalcPeriod = new com.see.truetransact.uicomponent.CLabel();
        cboPrematureIntCalAmt = new com.see.truetransact.uicomponent.CComboBox();
        lblPrematureIntCalcAmt = new com.see.truetransact.uicomponent.CLabel();
        panPreMatIntCalcPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtPreMatIntCalctPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblPrematureCloserMinIntCalcPeriod1 = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureCloserMinIntCalcPeriod2 = new com.see.truetransact.uicomponent.CLabel();
        panPreMatIntCalcPeriod1 = new com.see.truetransact.uicomponent.CPanel();
        txtPreMatIntCalctPeriod1 = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodFreq1 = new com.see.truetransact.uicomponent.CComboBox();
        lblGracePeriodPenalInterest = new com.see.truetransact.uicomponent.CLabel();
        txtGracePeriodPenalInterest = new com.see.truetransact.uicomponent.CTextField();
        lblInterestDueKeptRecivable = new com.see.truetransact.uicomponent.CLabel();
        panInterestDueKeptRecivable = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestDueKeptReceivable_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestDueKeptReceivable_No = new com.see.truetransact.uicomponent.CRadioButton();
        panPenalInterRate1 = new com.see.truetransact.uicomponent.CPanel();
        txtInsuraceRate = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInterRate_Per1 = new com.see.truetransact.uicomponent.CLabel();
        lblInsuraceRate = new com.see.truetransact.uicomponent.CLabel();
        panInterestDueKeptRecivable1 = new com.see.truetransact.uicomponent.CPanel();
        rdosanctionDate_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdosanctionDate_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMarketRateAsOn = new com.see.truetransact.uicomponent.CLabel();
        lblInsuranceCharge = new com.see.truetransact.uicomponent.CLabel();
        panInsuranceChargeApplicable = new com.see.truetransact.uicomponent.CPanel();
        rdoinsuranceCharge_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoinsuranceCharge_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPenalCalcBasedOn = new com.see.truetransact.uicomponent.CLabel();
        panEmiCalc = new com.see.truetransact.uicomponent.CPanel();
        rdoPenalCalcDays = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenalCalcMonths = new com.see.truetransact.uicomponent.CRadioButton();
        sptInterReceivable = new com.see.truetransact.uicomponent.CSeparator();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        panCharge = new com.see.truetransact.uicomponent.CPanel();
        panAccCharges = new com.see.truetransact.uicomponent.CPanel();
        lblAccClosingCharges = new com.see.truetransact.uicomponent.CLabel();
        txtAccClosingCharges = new com.see.truetransact.uicomponent.CTextField();
        lblMiscSerCharges = new com.see.truetransact.uicomponent.CLabel();
        txtMiscSerCharges = new com.see.truetransact.uicomponent.CTextField();
        sptCharges_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panStateCharges = new com.see.truetransact.uicomponent.CPanel();
        lblStatCharges = new com.see.truetransact.uicomponent.CLabel();
        panStatCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoStatCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStatCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblStatChargesRate = new com.see.truetransact.uicomponent.CLabel();
        txtStatChargesRate = new com.see.truetransact.uicomponent.CTextField();
        panChequeReturnCharges = new com.see.truetransact.uicomponent.CPanel();
        pancharge_Amt = new com.see.truetransact.uicomponent.CPanel();
        lblRangeFrom = new com.see.truetransact.uicomponent.CLabel();
        lblRateAmt = new com.see.truetransact.uicomponent.CLabel();
        txtRateAmt = new com.see.truetransact.uicomponent.CTextField();
        panButton = new com.see.truetransact.uicomponent.CPanel();
        btnCharge_New = new com.see.truetransact.uicomponent.CButton();
        btnCharge_Save = new com.see.truetransact.uicomponent.CButton();
        btnCharge_Delete = new com.see.truetransact.uicomponent.CButton();
        lblRangeTo = new com.see.truetransact.uicomponent.CLabel();
        txtRangeFrom = new com.see.truetransact.uicomponent.CTextField();
        txtRangeTo = new com.see.truetransact.uicomponent.CTextField();
        panCharges_Table = new com.see.truetransact.uicomponent.CPanel();
        srpCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblCharges = new com.see.truetransact.uicomponent.CTable();
        panFolio = new com.see.truetransact.uicomponent.CPanel();
        panFolio_Date = new com.see.truetransact.uicomponent.CPanel();
        lblFolioChargesAppl = new com.see.truetransact.uicomponent.CLabel();
        panFolioChargesAppl = new com.see.truetransact.uicomponent.CPanel();
        rdoFolioChargesAppl_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFolioChargesAppl_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblLastFolioChargesAppl = new com.see.truetransact.uicomponent.CLabel();
        tdtLastFolioChargesAppl = new com.see.truetransact.uicomponent.CDateField();
        lblNoEntriesPerFolio = new com.see.truetransact.uicomponent.CLabel();
        txtNoEntriesPerFolio = new com.see.truetransact.uicomponent.CTextField();
        lblNextFolioDDate = new com.see.truetransact.uicomponent.CLabel();
        tdtNextFolioDDate = new com.see.truetransact.uicomponent.CDateField();
        cboFolioChargesAppFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblFolioChargesAppFreq = new com.see.truetransact.uicomponent.CLabel();
        sptCharges_Vert2 = new com.see.truetransact.uicomponent.CSeparator();
        panFolio_Freq = new com.see.truetransact.uicomponent.CPanel();
        lblToCollectFolioCharges = new com.see.truetransact.uicomponent.CLabel();
        cboToCollectFolioCharges = new com.see.truetransact.uicomponent.CComboBox();
        lblToChargeOn = new com.see.truetransact.uicomponent.CLabel();
        panToChargeOn = new com.see.truetransact.uicomponent.CPanel();
        rdoToChargeOn_Man = new com.see.truetransact.uicomponent.CRadioButton();
        rdoToChargeOn_Sys = new com.see.truetransact.uicomponent.CRadioButton();
        rdoToChargeOn_Both = new com.see.truetransact.uicomponent.CRadioButton();
        lblIncompleteFolioRoundOffFreq = new com.see.truetransact.uicomponent.CLabel();
        cboIncompleteFolioRoundOffFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblRatePerFolio = new com.see.truetransact.uicomponent.CLabel();
        txtRatePerFolio = new com.see.truetransact.uicomponent.CTextField();
        panToChargeType = new com.see.truetransact.uicomponent.CPanel();
        rdoToChargeType_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoToChargeType_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoToChargeType_Both = new com.see.truetransact.uicomponent.CRadioButton();
        lblChargeAppliedType = new com.see.truetransact.uicomponent.CLabel();
        panCharge_ProcCommit = new com.see.truetransact.uicomponent.CPanel();
        panProcessCharge = new com.see.truetransact.uicomponent.CPanel();
        lblProcessCharges = new com.see.truetransact.uicomponent.CLabel();
        panProcessCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoProcessCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoProcessCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCharge_1 = new com.see.truetransact.uicomponent.CLabel();
        panCharge_1 = new com.see.truetransact.uicomponent.CPanel();
        lblLimit = new com.see.truetransact.uicomponent.CLabel();
        txtCharge_Limit2 = new com.see.truetransact.uicomponent.CTextField();
        cboCharge_Limit2 = new com.see.truetransact.uicomponent.CComboBox();
        sptCharges_Vert3 = new com.see.truetransact.uicomponent.CSeparator();
        panCommitCharges = new com.see.truetransact.uicomponent.CPanel();
        lblCommitmentCharge = new com.see.truetransact.uicomponent.CLabel();
        panCommitmentCharge = new com.see.truetransact.uicomponent.CPanel();
        rdoCommitmentCharge_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCommitmentCharge_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCharge_2 = new com.see.truetransact.uicomponent.CLabel();
        panCharge_2 = new com.see.truetransact.uicomponent.CPanel();
        lblLimits = new com.see.truetransact.uicomponent.CLabel();
        txtCharge_Limit3 = new com.see.truetransact.uicomponent.CTextField();
        cboCharge_Limit3 = new com.see.truetransact.uicomponent.CComboBox();
        panNoticeCharges = new com.see.truetransact.uicomponent.CPanel();
        panNoticecharge_Amt = new com.see.truetransact.uicomponent.CPanel();
        lblNoticeType = new com.see.truetransact.uicomponent.CLabel();
        lblNoticeChargeAmt = new com.see.truetransact.uicomponent.CLabel();
        panNoticeButton = new com.see.truetransact.uicomponent.CPanel();
        btnNotice_Charge_New = new com.see.truetransact.uicomponent.CButton();
        btnNotice_Charge_Save = new com.see.truetransact.uicomponent.CButton();
        btnNotice_Charge_Delete = new com.see.truetransact.uicomponent.CButton();
        lblIssueAfter = new com.see.truetransact.uicomponent.CLabel();
        cboNoticeType = new com.see.truetransact.uicomponent.CComboBox();
        cboIssueAfter = new com.see.truetransact.uicomponent.CComboBox();
        lblPostageAmt = new com.see.truetransact.uicomponent.CLabel();
        txtNoticeChargeAmt = new com.see.truetransact.uicomponent.CTextField();
        txtPostageAmt = new com.see.truetransact.uicomponent.CTextField();
        panNoticeCharges_Table = new com.see.truetransact.uicomponent.CPanel();
        srpNoticeCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblNoticeCharges = new com.see.truetransact.uicomponent.CTable();
        panSpecial_NonPerfoormingAssets = new com.see.truetransact.uicomponent.CPanel();
        panSpeItems = new com.see.truetransact.uicomponent.CPanel();
        lblATMcardIssued = new com.see.truetransact.uicomponent.CLabel();
        rdoATMcardIssued_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoATMcardIssued_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCreditCardIssued = new com.see.truetransact.uicomponent.CLabel();
        rdoCreditCardIssued_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCreditCardIssued_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMobileBanlingClient = new com.see.truetransact.uicomponent.CLabel();
        rdoMobileBanlingClient_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMobileBanlingClient_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIsAnyBranBankingAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoIsAnyBranBankingAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsAnyBranBankingAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        panNonPerAssets = new com.see.truetransact.uicomponent.CPanel();
        lblMinPeriodsArrears = new com.see.truetransact.uicomponent.CLabel();
        panMinPeriodsArrears = new com.see.truetransact.uicomponent.CPanel();
        txtMinPeriodsArrears = new com.see.truetransact.uicomponent.CTextField();
        cboMinPeriodsArrears = new com.see.truetransact.uicomponent.CComboBox();
        lblPeriodTranSStanAssets = new com.see.truetransact.uicomponent.CLabel();
        panPeriodTranSStanAssets = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodTranSStanAssets = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodTranSStanAssets = new com.see.truetransact.uicomponent.CComboBox();
        lblProvisionsStanAssets = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodTransDoubtfulAssets1 = new com.see.truetransact.uicomponent.CLabel();
        panPeriodTransDoubtfulAssets1 = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodTransDoubtfulAssets1 = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodTransDoubtfulAssets1 = new com.see.truetransact.uicomponent.CComboBox();
        lblProvisionDoubtfulAssets1 = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodTransLossAssets = new com.see.truetransact.uicomponent.CLabel();
        panPeriodTransLossAssets = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodTransLossAssets = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodTransLossAssets = new com.see.truetransact.uicomponent.CComboBox();
        lblPeriodAfterWhichTransNPerformingAssets = new com.see.truetransact.uicomponent.CLabel();
        panPeriodAfterWhichTransNPerformingAssets = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodAfterWhichTransNPerformingAssets = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodAfterWhichTransNPerformingAssets = new com.see.truetransact.uicomponent.CComboBox();
        txtProvisionsStanAssets = new com.see.truetransact.uicomponent.CTextField();
        lblProvisionsStanAssetsPer = new com.see.truetransact.uicomponent.CLabel();
        txtProvisionDoubtfulAssets1 = new com.see.truetransact.uicomponent.CTextField();
        lblProvisionDoubtfulAssetsPer1 = new com.see.truetransact.uicomponent.CLabel();
        lblProvisionStandardAssetss = new com.see.truetransact.uicomponent.CLabel();
        txtProvisionStandardAssetss = new com.see.truetransact.uicomponent.CTextField();
        lblProvisionStandardAssetssPer = new com.see.truetransact.uicomponent.CLabel();
        lblProvisionLossAssets = new com.see.truetransact.uicomponent.CLabel();
        txtProvisionLossAssets = new com.see.truetransact.uicomponent.CTextField();
        lblProvisionLossAssetsPer = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodTransDoubtfulAssets2 = new com.see.truetransact.uicomponent.CLabel();
        panPeriodTransDoubtfulAssets2 = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodTransDoubtfulAssets2 = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodTransDoubtfulAssets2 = new com.see.truetransact.uicomponent.CComboBox();
        lblProvisionDoubtfulAssets2 = new com.see.truetransact.uicomponent.CLabel();
        txtProvisionDoubtfulAssets2 = new com.see.truetransact.uicomponent.CTextField();
        lblProvisionDoubtfulAssetsPer2 = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodTransDoubtfulAssets3 = new com.see.truetransact.uicomponent.CLabel();
        panPeriodTransDoubtfulAssets3 = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodTransDoubtfulAssets3 = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodTransDoubtfulAssets3 = new com.see.truetransact.uicomponent.CComboBox();
        lblProvisionDoubtfulAssets3 = new com.see.truetransact.uicomponent.CLabel();
        txtProvisionDoubtfulAssets3 = new com.see.truetransact.uicomponent.CTextField();
        lblProvisionDoubtfulAssetsPer3 = new com.see.truetransact.uicomponent.CLabel();
        panInterCalculation = new com.see.truetransact.uicomponent.CPanel();
        lblcalcType = new com.see.truetransact.uicomponent.CLabel();
        cbocalcType = new com.see.truetransact.uicomponent.CComboBox();
        lblMinPeriod = new com.see.truetransact.uicomponent.CLabel();
        panMinPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtMinPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboMinPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblMaxPeriod = new com.see.truetransact.uicomponent.CLabel();
        panMaxPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtMaxPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboMaxPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblFixedMargin = new com.see.truetransact.uicomponent.CLabel();
        txtMinAmtLoan = new com.see.truetransact.uicomponent.CTextField();
        lblMinAmtLoan = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmtLoan = new com.see.truetransact.uicomponent.CTextField();
        lblApplicableInter = new com.see.truetransact.uicomponent.CLabel();
        panApplicableInter = new com.see.truetransact.uicomponent.CPanel();
        txtApplicableInter = new com.see.truetransact.uicomponent.CTextField();
        lblApplicableInterPer = new com.see.truetransact.uicomponent.CLabel();
        lblMinInterDebited = new com.see.truetransact.uicomponent.CLabel();
        panMinInterDebited = new com.see.truetransact.uicomponent.CPanel();
        txtMinInterDebited = new com.see.truetransact.uicomponent.CTextField();
        lblSunbsidy = new com.see.truetransact.uicomponent.CLabel();
        lblLoanPeriodMul = new com.see.truetransact.uicomponent.CLabel();
        cboLoanPeriodMul = new com.see.truetransact.uicomponent.CComboBox();
        panLoanAgainstDeposit = new com.see.truetransact.uicomponent.CPanel();
        lblLoanPeriodMul1 = new com.see.truetransact.uicomponent.CLabel();
        scrollPanList = new com.see.truetransact.uicomponent.CScrollPane();
        lstAvailableDeposits = new com.see.truetransact.uicomponent.CList();
        scrollPanAdd = new com.see.truetransact.uicomponent.CScrollPane();
        lstSelectedDeposits = new com.see.truetransact.uicomponent.CList();
        btnLTDRemove = new com.see.truetransact.uicomponent.CButton();
        btnLTDAdd = new com.see.truetransact.uicomponent.CButton();
        lblLoanPeriodMul2 = new com.see.truetransact.uicomponent.CLabel();
        lblEligibleDepositAmtForLoan = new com.see.truetransact.uicomponent.CLabel();
        panBankSharePremium1 = new com.see.truetransact.uicomponent.CPanel();
        txtElgLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        lblSunbsidy2 = new com.see.truetransact.uicomponent.CLabel();
        lblReviewPeriodLoan = new com.see.truetransact.uicomponent.CLabel();
        panMinPeriod1 = new com.see.truetransact.uicomponent.CPanel();
        txtReviewPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboReviewPeriod = new com.see.truetransact.uicomponent.CComboBox();
        cboDepositRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositRoundOff = new com.see.truetransact.uicomponent.CLabel();
        lblEligibleDepositAmtForLoanMaturing = new com.see.truetransact.uicomponent.CLabel();
        panMinInterDebited1 = new com.see.truetransact.uicomponent.CPanel();
        txtDepAmtMaturing = new com.see.truetransact.uicomponent.CTextField();
        lblSunbsidy1 = new com.see.truetransact.uicomponent.CLabel();
        panMinPeriod2 = new com.see.truetransact.uicomponent.CPanel();
        txtDepAmtLoanMaturingPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboDepAmtLoanMaturing = new com.see.truetransact.uicomponent.CComboBox();
        chkFixed = new com.see.truetransact.uicomponent.CCheckBox();
        lblMaxAmtLoan = new com.see.truetransact.uicomponent.CLabel();
        txtFixedMargin = new com.see.truetransact.uicomponent.CTextField();
        cLabel22 = new com.see.truetransact.uicomponent.CLabel();
        cboRepaymentType = new com.see.truetransact.uicomponent.CComboBox();
        cLabel23 = new com.see.truetransact.uicomponent.CLabel();
        cboRepaymentFreq = new com.see.truetransact.uicomponent.CComboBox();
        chkIntCalcFromSanctionDt = new com.see.truetransact.uicomponent.CCheckBox();
        cLabel25 = new com.see.truetransact.uicomponent.CLabel();
        txtGracePeriodForOverDueInt = new com.see.truetransact.uicomponent.CTextField();
        panDocuments = new com.see.truetransact.uicomponent.CPanel();
        panDocumentFields = new com.see.truetransact.uicomponent.CPanel();
        lblDocumentType = new com.see.truetransact.uicomponent.CLabel();
        cboDocumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblDocumentNo = new com.see.truetransact.uicomponent.CLabel();
        lblDocumentDesc = new com.see.truetransact.uicomponent.CLabel();
        txtDocumentNo = new com.see.truetransact.uicomponent.CTextField();
        txtDocumentDesc = new com.see.truetransact.uicomponent.CTextField();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnDocumentNew = new com.see.truetransact.uicomponent.CButton();
        btnDocumentSave = new com.see.truetransact.uicomponent.CButton();
        btnDocumentDelete = new com.see.truetransact.uicomponent.CButton();
        panDocumentTable = new com.see.truetransact.uicomponent.CPanel();
        srpDocuments = new com.see.truetransact.uicomponent.CScrollPane();
        tblDocuments = new com.see.truetransact.uicomponent.CTable();
        panAppropriateTransaction = new com.see.truetransact.uicomponent.CPanel();
        lblSelectedTransaction = new com.see.truetransact.uicomponent.CLabel();
        scrollPanList1 = new com.see.truetransact.uicomponent.CScrollPane();
        lstAvailableTypeTransaction = new com.see.truetransact.uicomponent.CList();
        scrollPanAdd1 = new com.see.truetransact.uicomponent.CScrollPane();
        lstSelectedPriorityTransaction = new com.see.truetransact.uicomponent.CList();
        btnTransactionRemove = new com.see.truetransact.uicomponent.CButton();
        btnTransactionAdd = new com.see.truetransact.uicomponent.CButton();
        lblAvailableTransaction = new com.see.truetransact.uicomponent.CLabel();
        panCreditPenalFirst1 = new com.see.truetransact.uicomponent.CPanel();
        lblWhetherCredit = new com.see.truetransact.uicomponent.CLabel();
        rdoIsCreditAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsCreditAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        panAppropriateRepaymentOTS = new com.see.truetransact.uicomponent.CPanel();
        lblSelectedTransaction1 = new com.see.truetransact.uicomponent.CLabel();
        scrollPanList2 = new com.see.truetransact.uicomponent.CScrollPane();
        lstAvailableTypeTransaction_OTS = new com.see.truetransact.uicomponent.CList();
        scrollPanAdd2 = new com.see.truetransact.uicomponent.CScrollPane();
        lstSelectedPriorityTransaction_OTS = new com.see.truetransact.uicomponent.CList();
        btnTransactionRemove_OTS = new com.see.truetransact.uicomponent.CButton();
        btnTransactionAdd_OTS = new com.see.truetransact.uicomponent.CButton();
        lblAvailableTransaction1 = new com.see.truetransact.uicomponent.CLabel();
        panClassDetails_Details = new com.see.truetransact.uicomponent.CPanel();
        panCode = new com.see.truetransact.uicomponent.CPanel();
        lblCommodityCode = new com.see.truetransact.uicomponent.CLabel();
        cboCommodityCode = new com.see.truetransact.uicomponent.CComboBox();
        lblGuaranteeCoverCode = new com.see.truetransact.uicomponent.CLabel();
        cboGuaranteeCoverCode = new com.see.truetransact.uicomponent.CComboBox();
        lblSectorCode = new com.see.truetransact.uicomponent.CLabel();
        cboSectorCode = new com.see.truetransact.uicomponent.CComboBox();
        lblHealthCode = new com.see.truetransact.uicomponent.CLabel();
        cboHealthCode = new com.see.truetransact.uicomponent.CComboBox();
        lblTypeFacility = new com.see.truetransact.uicomponent.CLabel();
        cboTypeFacility = new com.see.truetransact.uicomponent.CComboBox();
        lblPurposeCode = new com.see.truetransact.uicomponent.CLabel();
        cboPurposeCode = new com.see.truetransact.uicomponent.CComboBox();
        lblIndusCode = new com.see.truetransact.uicomponent.CLabel();
        cboIndusCode = new com.see.truetransact.uicomponent.CComboBox();
        sptClassification_vertical = new com.see.truetransact.uicomponent.CSeparator();
        panCode2 = new com.see.truetransact.uicomponent.CPanel();
        lbl20Code = new com.see.truetransact.uicomponent.CLabel();
        lblRefinancingInsti = new com.see.truetransact.uicomponent.CLabel();
        cboRefinancingInsti = new com.see.truetransact.uicomponent.CComboBox();
        lblGovtSchemeCode = new com.see.truetransact.uicomponent.CLabel();
        cboGovtSchemeCode = new com.see.truetransact.uicomponent.CComboBox();
        lblDirectFinance = new com.see.truetransact.uicomponent.CLabel();
        chkDirectFinance = new com.see.truetransact.uicomponent.CCheckBox();
        lblECGC = new com.see.truetransact.uicomponent.CLabel();
        chkECGC = new com.see.truetransact.uicomponent.CCheckBox();
        lblQIS = new com.see.truetransact.uicomponent.CLabel();
        chkQIS = new com.see.truetransact.uicomponent.CCheckBox();
        cbo20Code = new com.see.truetransact.uicomponent.CComboBox();
        lblSecurityDeails = new com.see.truetransact.uicomponent.CLabel();
        cboSecurityDeails = new com.see.truetransact.uicomponent.CComboBox();
        panSpecial_NonPerfoormingAssets1 = new com.see.truetransact.uicomponent.CPanel();
        panSpeItems1 = new com.see.truetransact.uicomponent.CPanel();
        lblSubsidy = new com.see.truetransact.uicomponent.CLabel();
        rdoSubsidy_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSubsidy_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSubsidyInterestCalculatedOn = new com.see.truetransact.uicomponent.CLabel();
        panSubsidyInterestCalculate = new com.see.truetransact.uicomponent.CPanel();
        rdoLoanBalance_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSubsidyAdjustLoanBalance_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        panSubsidyReceived = new com.see.truetransact.uicomponent.CPanel();
        rdoSubsidyReceivedDate = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLoanOpenDate = new com.see.truetransact.uicomponent.CRadioButton();
        panWaivedOff = new com.see.truetransact.uicomponent.CPanel();
        lblPenalInterestWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoPenalInterestWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenalInterestWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblInterestWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoInterestWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPrincipalWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoPrincipalWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPrincipalWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblNoticeWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoNoticeWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNoticeWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        cLabel8 = new com.see.truetransact.uicomponent.CLabel();
        cLabel9 = new com.see.truetransact.uicomponent.CLabel();
        cLabel10 = new com.see.truetransact.uicomponent.CLabel();
        cLabel11 = new com.see.truetransact.uicomponent.CLabel();
        cLabel12 = new com.see.truetransact.uicomponent.CLabel();
        rdoArbitaryWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoArbitaryWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPostageWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPostageWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdvertiseWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdvertiseWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMiscellaneousWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMiscellaneousWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDecreeWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDecreeWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEpWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEpWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoArcWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoArcWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInsurenceWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInsurenceWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLegalWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLegalWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        panInterestRebate = new com.see.truetransact.uicomponent.CPanel();
        lblInterestRebateAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoInterestRebateAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestRebateAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblInterestRebatePercentage = new com.see.truetransact.uicomponent.CLabel();
        lblInterestRebateCalculation = new com.see.truetransact.uicomponent.CLabel();
        lblRebatePeriod = new com.see.truetransact.uicomponent.CLabel();
        txtInterestRebatePercentage = new com.see.truetransact.uicomponent.CTextField();
        cboInterestRebateCalculation = new com.see.truetransact.uicomponent.CComboBox();
        cboRebatePeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblFinancialYearstartingfrom = new com.see.truetransact.uicomponent.CLabel();
        cPanel3 = new com.see.truetransact.uicomponent.CPanel();
        txtFinYearStartingFromDD = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtFinYearStartingFromMM = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblRebateSpl = new com.see.truetransact.uicomponent.CLabel();
        chkRebtSpl = new com.see.truetransact.uicomponent.CCheckBox();
        txtRebateLoanIntPercent = new com.see.truetransact.uicomponent.CTextField();
        PanWaiveHead = new com.see.truetransact.uicomponent.CPanel();
        cLabel13 = new com.see.truetransact.uicomponent.CLabel();
        txtArbitaryWaiveHead = new com.see.truetransact.uicomponent.CTextField();
        btnArbitaryHead = new com.see.truetransact.uicomponent.CButton();
        cLabel14 = new com.see.truetransact.uicomponent.CLabel();
        txtPostageWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnPostageWaiveHead = new com.see.truetransact.uicomponent.CButton();
        cLabel15 = new com.see.truetransact.uicomponent.CLabel();
        txtAdvertiseWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnAdvertiseWaiveHead = new com.see.truetransact.uicomponent.CButton();
        cLabel16 = new com.see.truetransact.uicomponent.CLabel();
        cLabel17 = new com.see.truetransact.uicomponent.CLabel();
        cLabel18 = new com.see.truetransact.uicomponent.CLabel();
        cLabel19 = new com.see.truetransact.uicomponent.CLabel();
        cLabel20 = new com.see.truetransact.uicomponent.CLabel();
        cLabel21 = new com.see.truetransact.uicomponent.CLabel();
        txtMiscellaneousWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnMiscellaneousWaiveHead = new com.see.truetransact.uicomponent.CButton();
        txtDecreeWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnDecreeWaiveHead = new com.see.truetransact.uicomponent.CButton();
        txtEpWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnEpWaiveHead = new com.see.truetransact.uicomponent.CButton();
        txtArcWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnArcWaiveHead = new com.see.truetransact.uicomponent.CButton();
        txtInsurenceWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnInsurenceWaiveHead = new com.see.truetransact.uicomponent.CButton();
        txtLegalWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnLeagalWaiveHead = new com.see.truetransact.uicomponent.CButton();
        cLabel26 = new com.see.truetransact.uicomponent.CLabel();
        txtOverDueWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnOverDueWaiverHead = new com.see.truetransact.uicomponent.CButton();
        cLabel28 = new com.see.truetransact.uicomponent.CLabel();
        rdoOverDueWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOverDueWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        panChargeTypes = new com.see.truetransact.uicomponent.CPanel();
        lblLegalCharges = new com.see.truetransact.uicomponent.CLabel();
        txtLegalCharges = new com.see.truetransact.uicomponent.CTextField();
        btnLegalCharges = new com.see.truetransact.uicomponent.CButton();
        lblPostageCharges = new com.see.truetransact.uicomponent.CLabel();
        txtPostageCharges = new com.see.truetransact.uicomponent.CTextField();
        btnPostageCharges = new com.see.truetransact.uicomponent.CButton();
        lblMiscServCharges = new com.see.truetransact.uicomponent.CLabel();
        txtMiscServCharges = new com.see.truetransact.uicomponent.CTextField();
        btnMiscServCharges = new com.see.truetransact.uicomponent.CButton();
        lblArbitraryCharges = new com.see.truetransact.uicomponent.CLabel();
        txtArbitraryCharges = new com.see.truetransact.uicomponent.CTextField();
        btnArbitraryCharges = new com.see.truetransact.uicomponent.CButton();
        lblInsuranceCharges = new com.see.truetransact.uicomponent.CLabel();
        txtInsuranceCharges = new com.see.truetransact.uicomponent.CTextField();
        btnInsuranceCharges = new com.see.truetransact.uicomponent.CButton();
        lblExecutionDecreeCharges = new com.see.truetransact.uicomponent.CLabel();
        txtExecutionDecreeCharges = new com.see.truetransact.uicomponent.CTextField();
        btnExecutionDecreeCharges = new com.see.truetransact.uicomponent.CButton();
        lblPenalWaiveoffHead = new com.see.truetransact.uicomponent.CLabel();
        txtPenalWaiveoffHead = new com.see.truetransact.uicomponent.CTextField();
        btnPenalWaiveoffHead = new com.see.truetransact.uicomponent.CButton();
        lblPrincipleWaiveoffHead = new com.see.truetransact.uicomponent.CLabel();
        txtPrincipleWaiveoffHead = new com.see.truetransact.uicomponent.CTextField();
        btnPrincipleWaiveoffHead = new com.see.truetransact.uicomponent.CButton();
        lblNOticeChargeDebitHead = new com.see.truetransact.uicomponent.CLabel();
        txtNoticeChargeDebitHead = new com.see.truetransact.uicomponent.CTextField();
        btnNoticeChargeDebitHd = new com.see.truetransact.uicomponent.CButton();
        lblDebitIntDiscount = new com.see.truetransact.uicomponent.CLabel();
        txtDebitIntDiscount = new com.see.truetransact.uicomponent.CTextField();
        btnDebitIntDiscount = new com.see.truetransact.uicomponent.CButton();
        panAccountHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        lblAccClosCharges = new com.see.truetransact.uicomponent.CLabel();
        txtAccClosCharges = new com.see.truetransact.uicomponent.CTextField();
        lbStatementCharges = new com.see.truetransact.uicomponent.CLabel();
        txtStatementCharges = new com.see.truetransact.uicomponent.CTextField();
        lblAccDebitInter = new com.see.truetransact.uicomponent.CLabel();
        txtAccDebitInter = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInter = new com.see.truetransact.uicomponent.CLabel();
        txtPenalInter = new com.see.truetransact.uicomponent.CTextField();
        lblAccCreditInter = new com.see.truetransact.uicomponent.CLabel();
        txtAccCreditInter = new com.see.truetransact.uicomponent.CTextField();
        lblExpiryInter = new com.see.truetransact.uicomponent.CLabel();
        txtExpiryInter = new com.see.truetransact.uicomponent.CTextField();
        lblCheReturnChargest_Out = new com.see.truetransact.uicomponent.CLabel();
        txtCheReturnChargest_Out = new com.see.truetransact.uicomponent.CTextField();
        lblCheReturnChargest_In = new com.see.truetransact.uicomponent.CLabel();
        txtCheReturnChargest_In = new com.see.truetransact.uicomponent.CTextField();
        lblFolioChargesAcc = new com.see.truetransact.uicomponent.CLabel();
        txtFolioChargesAcc = new com.see.truetransact.uicomponent.CTextField();
        lblCommitCharges = new com.see.truetransact.uicomponent.CLabel();
        txtCommitCharges = new com.see.truetransact.uicomponent.CTextField();
        lblProcessingCharges = new com.see.truetransact.uicomponent.CLabel();
        txtProcessingCharges = new com.see.truetransact.uicomponent.CTextField();
        btnAccClosCharges = new com.see.truetransact.uicomponent.CButton();
        btnStatementCharges = new com.see.truetransact.uicomponent.CButton();
        btnAccDebitInter = new com.see.truetransact.uicomponent.CButton();
        btnPenalInter = new com.see.truetransact.uicomponent.CButton();
        btnAccCreditInter = new com.see.truetransact.uicomponent.CButton();
        btnExpiryInter = new com.see.truetransact.uicomponent.CButton();
        btnCheReturnChargest_Out = new com.see.truetransact.uicomponent.CButton();
        btnCheReturnChargest_In = new com.see.truetransact.uicomponent.CButton();
        btnFolioChargesAcc = new com.see.truetransact.uicomponent.CButton();
        btnCommitCharges = new com.see.truetransact.uicomponent.CButton();
        btnProcessingCharges = new com.see.truetransact.uicomponent.CButton();
        lblNoticeCharges = new com.see.truetransact.uicomponent.CLabel();
        txtNoticeCharges = new com.see.truetransact.uicomponent.CTextField();
        btnNoticeCharges = new com.see.truetransact.uicomponent.CButton();
        lblIntPayableAccount = new com.see.truetransact.uicomponent.CLabel();
        txtIntPayableAccount = new com.see.truetransact.uicomponent.CTextField();
        btnIntPayableAccount = new com.see.truetransact.uicomponent.CButton();
        panCaseHead = new com.see.truetransact.uicomponent.CPanel();
        lblArcExpenseAccount = new com.see.truetransact.uicomponent.CLabel();
        txtArcExpenseAccount = new com.see.truetransact.uicomponent.CTextField();
        lblEaCostAccount = new com.see.truetransact.uicomponent.CLabel();
        txtEaCostAccount = new com.see.truetransact.uicomponent.CTextField();
        lblEaExpenseAccount = new com.see.truetransact.uicomponent.CLabel();
        txtEaExpenseAccount = new com.see.truetransact.uicomponent.CTextField();
        lblEpCostAccount = new com.see.truetransact.uicomponent.CLabel();
        txtEpCostAccount = new com.see.truetransact.uicomponent.CTextField();
        lblEpExpenseAccount = new com.see.truetransact.uicomponent.CLabel();
        txtEpExpenseAccount = new com.see.truetransact.uicomponent.CTextField();
        btnArcExpenseAccount = new com.see.truetransact.uicomponent.CButton();
        btnEaCostAccount = new com.see.truetransact.uicomponent.CButton();
        btnEaExpenseAccount = new com.see.truetransact.uicomponent.CButton();
        btnEpCostAccount = new com.see.truetransact.uicomponent.CButton();
        btnEpExpenseAccount = new com.see.truetransact.uicomponent.CButton();
        lblArcCostAccount = new com.see.truetransact.uicomponent.CLabel();
        txtArcCostAccount = new com.see.truetransact.uicomponent.CTextField();
        btnArcCostAccount = new com.see.truetransact.uicomponent.CButton();
        lblEpExpenseAccount1 = new com.see.truetransact.uicomponent.CLabel();
        txtRebateInterest = new com.see.truetransact.uicomponent.CTextField();
        btnRebateInterest = new com.see.truetransact.uicomponent.CButton();
        lblStampAdvancesHead = new com.see.truetransact.uicomponent.CLabel();
        txtStampAdvancesHead = new com.see.truetransact.uicomponent.CTextField();
        btnStampAdvancesHead = new com.see.truetransact.uicomponent.CButton();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        lblCreditStampAdvance = new com.see.truetransact.uicomponent.CLabel();
        chkCreditStampAdvance = new com.see.truetransact.uicomponent.CCheckBox();
        lblAdvertisementHead = new com.see.truetransact.uicomponent.CLabel();
        txtAdvertisementHead = new com.see.truetransact.uicomponent.CTextField();
        btnAdvertisementHead = new com.see.truetransact.uicomponent.CButton();
        lblARCEPSuspenceHead = new com.see.truetransact.uicomponent.CLabel();
        txtARCEPSuspenceHead = new com.see.truetransact.uicomponent.CTextField();
        btnARCEPSuspenceHead = new com.see.truetransact.uicomponent.CButton();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        txtOtherCharges = new com.see.truetransact.uicomponent.CTextField();
        btnOtherCharges = new com.see.truetransact.uicomponent.CButton();
        lblNoticeStampAdvance = new com.see.truetransact.uicomponent.CLabel();
        chkCreditNoticeAdvance = new com.see.truetransact.uicomponent.CCheckBox();
        lblNoticeAdvancesHead = new com.see.truetransact.uicomponent.CLabel();
        txtNoticeAdvancesHead = new com.see.truetransact.uicomponent.CTextField();
        btnNoticeAdvancesHead = new com.see.truetransact.uicomponent.CButton();
        cLabel27 = new com.see.truetransact.uicomponent.CLabel();
        txtOverdueIntHead = new com.see.truetransact.uicomponent.CTextField();
        btnOverdueIntHead = new com.see.truetransact.uicomponent.CButton();
        panSuspenseProducts = new com.see.truetransact.uicomponent.CPanel();
        lblSuspenseCreditAchd = new com.see.truetransact.uicomponent.CLabel();
        txtSuspenseCreditProductID = new com.see.truetransact.uicomponent.CTextField();
        btnSuspenseCreditAchd = new com.see.truetransact.uicomponent.CButton();
        lblSuspenseDebitAchd = new com.see.truetransact.uicomponent.CLabel();
        txtSuspenseDebitProductId = new com.see.truetransact.uicomponent.CTextField();
        btnSuspenseDebitAchd = new com.see.truetransact.uicomponent.CButton();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        lblCreditStampAdvance1 = new com.see.truetransact.uicomponent.CLabel();
        chkGoldAuctionAllowed = new com.see.truetransact.uicomponent.CCheckBox();
        btnCreateHead = new com.see.truetransact.uicomponent.CButton();
        PanGroupProduct = new com.see.truetransact.uicomponent.CPanel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblIntrestCalcMonth = new com.see.truetransact.uicomponent.CLabel();
        cboIntPaymntMonth = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitAllowedForDueCustomer = new com.see.truetransact.uicomponent.CLabel();
        cboIntPaymntDay = new com.see.truetransact.uicomponent.CComboBox();
        lblIntrestCalcDay = new com.see.truetransact.uicomponent.CLabel();
        lblIntrestCalcDay2 = new com.see.truetransact.uicomponent.CLabel();
        panIsLimitDefnAllowed1 = new com.see.truetransact.uicomponent.CPanel();
        rdoIsDebitAllowedForDue_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsDebitAllowedForDue_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblHolidayInst = new com.see.truetransact.uicomponent.CLabel();
        cboholidayInt = new com.see.truetransact.uicomponent.CComboBox();
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrLoanProduct = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(1008, 690));
        setPreferredSize(new java.awt.Dimension(1008, 690));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panLoanProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLoanProduct.setMinimumSize(new java.awt.Dimension(844, 680));
        panLoanProduct.setPreferredSize(new java.awt.Dimension(844, 680));
        panLoanProduct.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 15));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panLoanProduct.add(lblSpaces, gridBagConstraints);

        tabLoanProduct.setMinimumSize(new java.awt.Dimension(988, 680));
        tabLoanProduct.setPreferredSize(new java.awt.Dimension(988, 680));
        tabLoanProduct.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabLoanProductStateChanged(evt);
            }
        });

        panAccount.setMinimumSize(new java.awt.Dimension(278, 300));
        panAccount.setPreferredSize(new java.awt.Dimension(1100, 1100));
        panAccount.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccount.add(sptAcc, gridBagConstraints);

        panIsAuctionAmt2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccount.add(panIsAuctionAmt2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAccount.add(cPanel2, gridBagConstraints);

        panGldRenewal.setMinimumSize(new java.awt.Dimension(300, 150));
        panGldRenewal.setPreferredSize(new java.awt.Dimension(300, 150));
        panGldRenewal.setLayout(new java.awt.GridBagLayout());

        lblGldRenewOldAmt.setText("Renew with Old Loan Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblGldRenewOldAmt, gridBagConstraints);

        lblGldRenewCash.setText("Renew Transaction through Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblGldRenewCash, gridBagConstraints);

        lblGldRenewMarketRate.setText("Renew with new Market Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblGldRenewMarketRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkGldRenewOldAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkGldRenewCash, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkGldRenewMarketRate, gridBagConstraints);

        lblGldRnwOldIntRate.setText("Renew with New Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(lblGldRnwOldIntRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGldRenewal.add(chkGldRnwOldIntRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 10, 0, 0);
        panAccount.add(panGldRenewal, gridBagConstraints);

        panProdExtraFeatures.setMinimumSize(new java.awt.Dimension(350, 80));
        panProdExtraFeatures.setPreferredSize(new java.awt.Dimension(350, 100));
        panProdExtraFeatures.setLayout(new java.awt.GridBagLayout());

        chkExcludeScSt.setText("Exclude SC/ST in Share Linking");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panProdExtraFeatures.add(chkExcludeScSt, gridBagConstraints);

        chkShareLink.setText("Share Linking Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panProdExtraFeatures.add(chkShareLink, gridBagConstraints);

        chkExcludeTOD.setText("Close Account For TOD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panProdExtraFeatures.add(chkExcludeTOD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = -10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 22, 60, 0);
        panAccount.add(panProdExtraFeatures, gridBagConstraints);

        panProdCatg.setMinimumSize(new java.awt.Dimension(300, 150));
        panProdCatg.setLayout(new java.awt.GridBagLayout());

        lblOperatesLike.setText("Operates Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 10, 0, 0);
        panProdCatg.add(lblOperatesLike, gridBagConstraints);

        cboOperatesLike.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOperatesLike.setPopupWidth(250);
        cboOperatesLike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboOperatesLikeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 4, 0, 0);
        panProdCatg.add(cboOperatesLike, gridBagConstraints);

        lblProdCategory.setText("Category of Product");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        panProdCatg.add(lblProdCategory, gridBagConstraints);

        cboProdCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdCategory.setPopupWidth(250);
        cboProdCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdCategoryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 0);
        panProdCatg.add(cboProdCategory, gridBagConstraints);

        lblProdCurrency.setText("Product Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        panProdCatg.add(lblProdCurrency, gridBagConstraints);

        cboProdCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 0);
        panProdCatg.add(cboProdCurrency, gridBagConstraints);

        lblNumPatternFollowed.setText("Numbering Pattern to be Followed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 0);
        panProdCatg.add(lblNumPatternFollowed, gridBagConstraints);

        txtNumPatternFollowed.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowed.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 0, 0);
        panProdCatg.add(txtNumPatternFollowed, gridBagConstraints);

        txtNumPatternFollowedSuffix.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowedSuffix.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 0, 60);
        panProdCatg.add(txtNumPatternFollowedSuffix, gridBagConstraints);

        lblLastAccNum.setText("Next Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 10, 9, 0);
        panProdCatg.add(lblLastAccNum, gridBagConstraints);

        txtLastAccNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 9, 0);
        panProdCatg.add(txtLastAccNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 130;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 22, 0, 0);
        panAccount.add(panProdCatg, gridBagConstraints);

        panProdSettings.setMinimumSize(new java.awt.Dimension(350, 220));
        panProdSettings.setPreferredSize(new java.awt.Dimension(700, 700));
        panProdSettings.setLayout(new java.awt.GridBagLayout());

        lblIsInterestFirst.setText("Interest Retrieved while disbursement?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 28, 0, 0);
        panProdSettings.add(lblIsInterestFirst, gridBagConstraints);

        panIsLimitDefnAllowed.setLayout(new java.awt.GridBagLayout());

        rdoIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_Yes);
        rdoIsLimitDefnAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_Yes, gridBagConstraints);

        rdoIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_No);
        rdoIsLimitDefnAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 14, 0, 0);
        panProdSettings.add(panIsLimitDefnAllowed, gridBagConstraints);

        lblIsStaffAccOpened.setText("Is Staff Account Opened?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblIsStaffAccOpened, gridBagConstraints);

        panIsStaffAccOpened.setLayout(new java.awt.GridBagLayout());

        rdoIsStaffAccOpened.add(rdoIsStaffAccOpened_Yes);
        rdoIsStaffAccOpened_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsStaffAccOpened.add(rdoIsStaffAccOpened_Yes, gridBagConstraints);

        rdoIsStaffAccOpened.add(rdoIsStaffAccOpened_No);
        rdoIsStaffAccOpened_No.setText("No");
        rdoIsStaffAccOpened_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsStaffAccOpened_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsStaffAccOpened.add(rdoIsStaffAccOpened_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 0);
        panProdSettings.add(panIsStaffAccOpened, gridBagConstraints);

        lblIsDebitInterUnderClearing.setText("Is Debit Interest on Under Clearing Applicable?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblIsDebitInterUnderClearing, gridBagConstraints);

        lblIsAuctionAmt.setText("Is Auction Amount  Allowed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblIsAuctionAmt, gridBagConstraints);

        panIsAuctionAmt.setLayout(new java.awt.GridBagLayout());

        rdoIsDebitInterUnderClearing.add(rdoIsAuctionAmt_Yes);
        rdoIsAuctionAmt_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsAuctionAmt.add(rdoIsAuctionAmt_Yes, gridBagConstraints);

        rdoIsDebitInterUnderClearing.add(rdoIsAuctionAmt_No);
        rdoIsAuctionAmt_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsAuctionAmt.add(rdoIsAuctionAmt_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 0);
        panProdSettings.add(panIsAuctionAmt, gridBagConstraints);

        lblIsDisbAftMoraPerd.setText("Is Disbersment allowed after Moratorium?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblIsDisbAftMoraPerd, gridBagConstraints);

        panDIsbAftMoraPeriod.setLayout(new java.awt.GridBagLayout());

        rdoDisbAftMoraPeriod.add(rdoIsDisbAftMora_Yes);
        rdoIsDisbAftMora_Yes.setText("Yes");
        rdoIsDisbAftMora_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsDisbAftMora_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panDIsbAftMoraPeriod.add(rdoIsDisbAftMora_Yes, gridBagConstraints);

        rdoDisbAftMoraPeriod.add(rdoIsDisbAftMora_No);
        rdoIsDisbAftMora_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panDIsbAftMoraPeriod.add(rdoIsDisbAftMora_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 0);
        panProdSettings.add(panDIsbAftMoraPeriod, gridBagConstraints);

        lblCollectIntOnMaturity.setText("Loan Closing Menu Collect Int On Maturity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblCollectIntOnMaturity, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 4);
        panProdSettings.add(chkInterestOnMaturity, gridBagConstraints);

        panIsDebitInterUnderClearing.setMinimumSize(new java.awt.Dimension(88, 27));
        panIsDebitInterUnderClearing.setPreferredSize(new java.awt.Dimension(88, 27));
        panIsDebitInterUnderClearing.setLayout(new java.awt.GridBagLayout());

        rdoIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_Yes);
        rdoIsDebitInterUnderClearing_Yes.setText("Yes");
        rdoIsDebitInterUnderClearing_Yes.setMaximumSize(new java.awt.Dimension(54, 27));
        rdoIsDebitInterUnderClearing_Yes.setMinimumSize(new java.awt.Dimension(54, 27));
        rdoIsDebitInterUnderClearing_Yes.setPreferredSize(new java.awt.Dimension(54, 27));
        panIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_Yes, new java.awt.GridBagConstraints());

        rdoIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_No);
        rdoIsDebitInterUnderClearing_No.setText("No");
        rdoIsDebitInterUnderClearing_No.setMaximumSize(new java.awt.Dimension(44, 27));
        rdoIsDebitInterUnderClearing_No.setMinimumSize(new java.awt.Dimension(44, 27));
        rdoIsDebitInterUnderClearing_No.setPreferredSize(new java.awt.Dimension(44, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 3;
        panIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 5);
        panProdSettings.add(panIsDebitInterUnderClearing, gridBagConstraints);

        lblMAxCashPayment.setText("Cash Limit For Loan Disbursement ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblMAxCashPayment, gridBagConstraints);

        txtMAxCashPayment.setAllowAll(true);
        txtMAxCashPayment.setAllowNumber(true);
        txtMAxCashPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMAxCashPayment.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMAxCashPaymentFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 4);
        panProdSettings.add(txtMAxCashPayment, gridBagConstraints);

        lblEMIInSimpleInterest.setText("EMI in simple interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 119;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblEMIInSimpleInterest, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 16, 0, 4);
        panProdSettings.add(chkEmiInSimpleInterst, gridBagConstraints);

        lblIsLimitDefnAllowed.setText("Is Limit Definition Allowed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblIsLimitDefnAllowed, gridBagConstraints);

        panIsInterestFirst.setLayout(new java.awt.GridBagLayout());

        rdoDisbAftMoraPeriod.add(rdoIsInterestFirst_Yes);
        rdoIsInterestFirst_Yes.setText("Yes");
        rdoIsInterestFirst_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsInterestFirst_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsInterestFirst.add(rdoIsInterestFirst_Yes, gridBagConstraints);

        rdoDisbAftMoraPeriod.add(rdoIsInterestFirst_No);
        rdoIsInterestFirst_No.setText("No");
        rdoIsInterestFirst_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsInterestFirst_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsInterestFirst.add(rdoIsInterestFirst_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 0);
        panProdSettings.add(panIsInterestFirst, gridBagConstraints);

        lblFrequencyInDays.setText("Frequency in Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblFrequencyInDays, gridBagConstraints);

        txtFrequencyInDays.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 0, 4);
        panProdSettings.add(txtFrequencyInDays, gridBagConstraints);

        lblSalaryRecovery.setText("Salary Recovery");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(lblSalaryRecovery, gridBagConstraints);

        panSalRec.setLayout(new java.awt.GridBagLayout());

        rdoSalRec.add(rdoSalRec_Yes);
        rdoSalRec_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panSalRec.add(rdoSalRec_Yes, gridBagConstraints);

        rdoSalRec.add(rdoSalRec_No);
        rdoSalRec_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panSalRec.add(rdoSalRec_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 14, 0, 0);
        panProdSettings.add(panSalRec, gridBagConstraints);

        cLabel24.setText("Is Interest Retrieved While Loan Due");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 28, 0, 0);
        panProdSettings.add(cLabel24, gridBagConstraints);

        rdoInterestButtonGroup.add(rdoIsInterestdue_YES);
        rdoIsInterestdue_YES.setText("Yes");
        rdoIsInterestdue_YES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsInterestdue_YESActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 14, 0, 0);
        panProdSettings.add(rdoIsInterestdue_YES, gridBagConstraints);

        rdoInterestButtonGroup.add(rdoIsInterestDue_No);
        rdoIsInterestDue_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        panProdSettings.add(rdoIsInterestDue_No, gridBagConstraints);

        chkIsInterestPeriodWise.setText("Is Slab Wise Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        panProdSettings.add(chkIsInterestPeriodWise, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 140;
        gridBagConstraints.ipady = 125;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 21);
        panAccount.add(panProdSettings, gridBagConstraints);

        panProdDetails.setMinimumSize(new java.awt.Dimension(300, 60));
        panProdDetails.setPreferredSize(new java.awt.Dimension(700, 300));
        panProdDetails.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 53;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panProdDetails.add(lblProductID, gridBagConstraints);

        txtProductID.setAllowAll(true);
        txtProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panProdDetails.add(txtProductID, gridBagConstraints);

        lblProductDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 0, 0);
        panProdDetails.add(lblProductDesc, gridBagConstraints);

        txtProductDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductDescActionPerformed(evt);
            }
        });
        txtProductDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtProductDescFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductDescFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panProdDetails.add(txtProductDesc, gridBagConstraints);

        lblAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 40, 0, 0);
        panProdDetails.add(lblAccHead, gridBagConstraints);

        txtAccHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProdDetails.add(txtAccHead, gridBagConstraints);

        txtAccHeadDesc.setMinimumSize(new java.awt.Dimension(170, 21));
        txtAccHeadDesc.setPreferredSize(new java.awt.Dimension(170, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 0, 45);
        panProdDetails.add(txtAccHeadDesc, gridBagConstraints);

        btnAccHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHead.setToolTipText("Search");
        btnAccHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 0);
        panProdDetails.add(btnAccHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 354;
        gridBagConstraints.ipady = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 35, 0);
        panAccount.add(panProdDetails, gridBagConstraints);

        panGroupLoan.setMinimumSize(new java.awt.Dimension(300, 150));
        panGroupLoan.setPreferredSize(new java.awt.Dimension(300, 150));
        panGroupLoan.setLayout(new java.awt.GridBagLayout());

        chkGroupLoan.setText("Group Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panGroupLoan.add(chkGroupLoan, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = -120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 31, 0, 0);
        panAccount.add(panGroupLoan, gridBagConstraints);

        chkGoldStockPhoto.setText("Gold Loan Stock Photo to be stored");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 36, 0, 0);
        panAccount.add(chkGoldStockPhoto, gridBagConstraints);

        chkBlockIFLimitExceed.setText("Block interest posting id OD accounts exceeds limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panAccount.add(chkBlockIFLimitExceed, gridBagConstraints);

        tabLoanProduct.addTab("Account", panAccount);

        panInterReceivable.setMinimumSize(new java.awt.Dimension(1014, 700));
        panInterReceivable.setPreferredSize(new java.awt.Dimension(1016, 700));
        panInterReceivable.setLayout(new java.awt.GridBagLayout());

        panInterReceivable_Debit.setMinimumSize(new java.awt.Dimension(420, 680));
        panInterReceivable_Debit.setPreferredSize(new java.awt.Dimension(360, 700));
        panInterReceivable_Debit.setLayout(new java.awt.GridBagLayout());

        lbldebitInterCharged.setText("Debit Interest to be Charged");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterReceivable_Debit.add(lbldebitInterCharged, gridBagConstraints);

        panldebitInterCharged.setLayout(new java.awt.GridBagLayout());

        rdoldebitInterCharged.add(rdoldebitInterCharged_Yes);
        rdoldebitInterCharged_Yes.setText("Yes");
        rdoldebitInterCharged_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoldebitInterCharged_YesActionPerformed(evt);
            }
        });
        panldebitInterCharged.add(rdoldebitInterCharged_Yes, new java.awt.GridBagConstraints());

        rdoldebitInterCharged.add(rdoldebitInterCharged_No);
        rdoldebitInterCharged_No.setText("No");
        rdoldebitInterCharged_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoldebitInterCharged_NoActionPerformed(evt);
            }
        });
        panldebitInterCharged.add(rdoldebitInterCharged_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panInterReceivable_Debit.add(panldebitInterCharged, gridBagConstraints);

        lblMinDebitInterRate.setText("Minimum Debit Interest rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblMinDebitInterRate, gridBagConstraints);

        panMinDebitInterRate.setLayout(new java.awt.GridBagLayout());

        txtMinDebitInterRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinDebitInterRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinDebitInterRate.add(txtMinDebitInterRate, new java.awt.GridBagConstraints());

        lblMinDebitInterRate_Per.setText("%");
        panMinDebitInterRate.add(lblMinDebitInterRate_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInterReceivable_Debit.add(panMinDebitInterRate, gridBagConstraints);

        lblMaxDebitInterRate.setText("Maximum Debit Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblMaxDebitInterRate, gridBagConstraints);

        panMaxDebitInterRate.setLayout(new java.awt.GridBagLayout());

        txtMaxDebitInterRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxDebitInterRate.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMaxDebitInterRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDebitInterRateFocusLost(evt);
            }
        });
        panMaxDebitInterRate.add(txtMaxDebitInterRate, new java.awt.GridBagConstraints());

        lblMaxDebitInterRate_Per.setText("%");
        panMaxDebitInterRate.add(lblMaxDebitInterRate_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInterReceivable_Debit.add(panMaxDebitInterRate, gridBagConstraints);

        lblMinDebitInterAmt.setText("Minimum Debit Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblMinDebitInterAmt, gridBagConstraints);

        txtMinDebitInterAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInterReceivable_Debit.add(txtMinDebitInterAmt, gridBagConstraints);

        lblMaxDebitInterAmt.setText("Maximum Debit Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblMaxDebitInterAmt, gridBagConstraints);

        txtMaxDebitInterAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxDebitInterAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDebitInterAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panInterReceivable_Debit.add(txtMaxDebitInterAmt, gridBagConstraints);

        lblDebInterCalcFreq.setText("Debit Interest Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblDebInterCalcFreq, gridBagConstraints);

        cboDebInterCalcFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebInterCalcFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 2);
        panInterReceivable_Debit.add(cboDebInterCalcFreq, gridBagConstraints);

        lblDebitInterAppFreq.setText("Debit Interest Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblDebitInterAppFreq, gridBagConstraints);

        cboDebitInterAppFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitInterAppFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panInterReceivable_Debit.add(cboDebitInterAppFreq, gridBagConstraints);

        lblDebitInterComFreq.setText("Debit Interest Compound Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblDebitInterComFreq, gridBagConstraints);

        cboDebitInterComFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitInterComFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panInterReceivable_Debit.add(cboDebitInterComFreq, gridBagConstraints);

        lblDebitProdRoundOff.setText("Debit Product Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblDebitProdRoundOff, gridBagConstraints);

        cboDebitProdRoundOff.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitProdRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panInterReceivable_Debit.add(cboDebitProdRoundOff, gridBagConstraints);

        lblDebitInterRoundOff.setText("Debit Interest Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblDebitInterRoundOff, gridBagConstraints);

        cboDebitInterRoundOff.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitInterRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panInterReceivable_Debit.add(cboDebitInterRoundOff, gridBagConstraints);

        lblLastInterCalcDate.setText("Last Interest Calculated Date - Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblLastInterCalcDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panInterReceivable_Debit.add(tdtLastInterCalcDate, gridBagConstraints);

        lblLastInterAppDate.setText("Last Interest Application Date - Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblLastInterAppDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
        panInterReceivable_Debit.add(tdtLastInterAppDate, gridBagConstraints);

        lblasAndWhenCustomer.setText("AsAndWhenCustomerComes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblasAndWhenCustomer, gridBagConstraints);

        lblcalendar.setText("CalendarFrequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblcalendar, gridBagConstraints);

        panasAndWhenCustomer.setLayout(new java.awt.GridBagLayout());

        rdoasAndWhenCustomer.add(rdoasAndWhenCustomer_Yes);
        rdoasAndWhenCustomer_Yes.setText("Yes");
        rdoasAndWhenCustomer_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoasAndWhenCustomer_YesActionPerformed(evt);
            }
        });
        panasAndWhenCustomer.add(rdoasAndWhenCustomer_Yes, new java.awt.GridBagConstraints());

        rdoasAndWhenCustomer.add(rdoasAndWhenCustomer_No);
        rdoasAndWhenCustomer_No.setText("No");
        rdoasAndWhenCustomer_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoasAndWhenCustomer_NoActionPerformed(evt);
            }
        });
        panasAndWhenCustomer.add(rdoasAndWhenCustomer_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panInterReceivable_Debit.add(panasAndWhenCustomer, gridBagConstraints);

        panCalendarFrequency.setLayout(new java.awt.GridBagLayout());

        rdoCalendarFrequency.add(rdocalendarFrequency_Yes);
        rdocalendarFrequency_Yes.setText("Yes");
        rdocalendarFrequency_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdocalendarFrequency_YesActionPerformed(evt);
            }
        });
        panCalendarFrequency.add(rdocalendarFrequency_Yes, new java.awt.GridBagConstraints());

        rdoCalendarFrequency.add(rdocalendarFrequency_No);
        rdocalendarFrequency_No.setText("No");
        rdocalendarFrequency_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdocalendarFrequency_NoActionPerformed(evt);
            }
        });
        panCalendarFrequency.add(rdocalendarFrequency_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panInterReceivable_Debit.add(panCalendarFrequency, gridBagConstraints);

        panBillbyBill.setLayout(new java.awt.GridBagLayout());

        rdoBillByBill.add(rdobill_Yes);
        rdobill_Yes.setText("Yes");
        rdobill_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdobill_YesActionPerformed(evt);
            }
        });
        panBillbyBill.add(rdobill_Yes, new java.awt.GridBagConstraints());

        rdoBillByBill.add(rdobill_No);
        rdobill_No.setText("No");
        rdobill_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdobill_NoActionPerformed(evt);
            }
        });
        panBillbyBill.add(rdobill_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panInterReceivable_Debit.add(panBillbyBill, gridBagConstraints);

        lblBillbyBill.setText("Bill by Bill");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblBillbyBill, gridBagConstraints);

        panLTDInterest.setMinimumSize(new java.awt.Dimension(280, 27));
        panLTDInterest.setPreferredSize(new java.awt.Dimension(280, 27));
        panLTDInterest.setLayout(new java.awt.GridBagLayout());

        lblCollectIntTill.setText("Collect Interest Till");
        lblCollectIntTill.setMaximumSize(new java.awt.Dimension(110, 18));
        lblCollectIntTill.setMinimumSize(new java.awt.Dimension(110, 18));
        lblCollectIntTill.setPreferredSize(new java.awt.Dimension(110, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLTDInterest.add(lblCollectIntTill, gridBagConstraints);

        rdoLTDinterestGroup.add(rdoMaturity_Yes);
        rdoMaturity_Yes.setText("Maturity Dt");
        panLTDInterest.add(rdoMaturity_Yes, new java.awt.GridBagConstraints());

        rdoLTDinterestGroup.add(rdoCurrDt_Yes);
        rdoCurrDt_Yes.setText("Curr Dt");
        rdoCurrDt_Yes.setMaximumSize(new java.awt.Dimension(80, 27));
        rdoCurrDt_Yes.setMinimumSize(new java.awt.Dimension(80, 27));
        rdoCurrDt_Yes.setPreferredSize(new java.awt.Dimension(97, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panLTDInterest.add(rdoCurrDt_Yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(panLTDInterest, gridBagConstraints);

        lblCollectIntTillLoanClosureMenu.setText("Loan closed through Loan closure Menu ");
        lblCollectIntTillLoanClosureMenu.setMaximumSize(new java.awt.Dimension(310, 18));
        lblCollectIntTillLoanClosureMenu.setMinimumSize(new java.awt.Dimension(310, 18));
        lblCollectIntTillLoanClosureMenu.setPreferredSize(new java.awt.Dimension(310, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblCollectIntTillLoanClosureMenu, gridBagConstraints);

        lblCollectIntTillDepositClosureMenu.setText("Loan closed through Deposit Closure menu");
        lblCollectIntTillDepositClosureMenu.setMaximumSize(new java.awt.Dimension(310, 18));
        lblCollectIntTillDepositClosureMenu.setMinimumSize(new java.awt.Dimension(310, 18));
        lblCollectIntTillDepositClosureMenu.setPreferredSize(new java.awt.Dimension(310, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblCollectIntTillDepositClosureMenu, gridBagConstraints);

        panLTDInterest1.setMinimumSize(new java.awt.Dimension(300, 27));
        panLTDInterest1.setPreferredSize(new java.awt.Dimension(300, 27));
        panLTDInterest1.setLayout(new java.awt.GridBagLayout());

        lblCollectIntTillDepositClosure.setText("Collect Interest Till");
        lblCollectIntTillDepositClosure.setMaximumSize(new java.awt.Dimension(110, 18));
        lblCollectIntTillDepositClosure.setMinimumSize(new java.awt.Dimension(110, 18));
        lblCollectIntTillDepositClosure.setPreferredSize(new java.awt.Dimension(110, 18));
        panLTDInterest1.add(lblCollectIntTillDepositClosure, new java.awt.GridBagConstraints());

        rdoMaturityDepositClosureGroup.add(rdoMaturity_Deposit_closure_Maturity_Yes);
        rdoMaturity_Deposit_closure_Maturity_Yes.setText("Maturity Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panLTDInterest1.add(rdoMaturity_Deposit_closure_Maturity_Yes, gridBagConstraints);

        rdoMaturityDepositClosureGroup.add(rdoMaturity_Deposit_closure_Curr_Yes);
        rdoMaturity_Deposit_closure_Curr_Yes.setText("Curr Dt");
        rdoMaturity_Deposit_closure_Curr_Yes.setMaximumSize(new java.awt.Dimension(80, 27));
        rdoMaturity_Deposit_closure_Curr_Yes.setMinimumSize(new java.awt.Dimension(80, 27));
        rdoMaturity_Deposit_closure_Curr_Yes.setPreferredSize(new java.awt.Dimension(97, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panLTDInterest1.add(rdoMaturity_Deposit_closure_Curr_Yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 20, 1, 1);
        panInterReceivable_Debit.add(panLTDInterest1, gridBagConstraints);

        lblInterestRepaymentFreq.setText("Interest Repayment Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_Debit.add(lblInterestRepaymentFreq, gridBagConstraints);

        cboInterestRepaymentFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboInterestRepaymentFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 2, 2);
        panInterReceivable_Debit.add(cboInterestRepaymentFreq, gridBagConstraints);

        chkIsOverdueInt.setText("Over Due Interest to be charged for EMI");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        panInterReceivable_Debit.add(chkIsOverdueInt, gridBagConstraints);

        chkPrematureIntCalc.setText(" Premature Closure Int Calculation Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panInterReceivable_Debit.add(chkPrematureIntCalc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable.add(panInterReceivable_Debit, gridBagConstraints);

        panInterReceivable_PLR.setMaximumSize(new java.awt.Dimension(630, 600));
        panInterReceivable_PLR.setMinimumSize(new java.awt.Dimension(630, 680));
        panInterReceivable_PLR.setPreferredSize(new java.awt.Dimension(630, 700));
        panInterReceivable_PLR.setLayout(new java.awt.GridBagLayout());

        lblPLRRateAppl.setText("PLR Rate Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterReceivable_PLR.add(lblPLRRateAppl, gridBagConstraints);

        panPLRRateAppl.setLayout(new java.awt.GridBagLayout());

        rdoPLRRateAppl.add(rdoPLRRateAppl_Yes);
        rdoPLRRateAppl_Yes.setText("Yes");
        rdoPLRRateAppl_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPLRRateAppl_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPLRRateAppl.add(rdoPLRRateAppl_Yes, gridBagConstraints);

        rdoPLRRateAppl.add(rdoPLRRateAppl_No);
        rdoPLRRateAppl_No.setText("No");
        rdoPLRRateAppl_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPLRRateAppl_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPLRRateAppl.add(rdoPLRRateAppl_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panInterReceivable_PLR.add(panPLRRateAppl, gridBagConstraints);

        panPLRRate.setLayout(new java.awt.GridBagLayout());

        txtPLRRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPLRRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panPLRRate.add(txtPLRRate, new java.awt.GridBagConstraints());

        lblPLRRate_Per.setText("%");
        panPLRRate.add(lblPLRRate_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInterReceivable_PLR.add(panPLRRate, gridBagConstraints);

        lblPLRRate.setText("PLR Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterReceivable_PLR.add(lblPLRRate, gridBagConstraints);

        lblPLRAppForm.setText("PLR Applicable From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterReceivable_PLR.add(lblPLRAppForm, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInterReceivable_PLR.add(tdtPLRAppForm, gridBagConstraints);

        lblPLRApplNewAcc.setText("PLR Applicable for New Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPLRApplNewAcc, gridBagConstraints);

        panPLRApplNewAcc.setMinimumSize(new java.awt.Dimension(100, 21));
        panPLRApplNewAcc.setPreferredSize(new java.awt.Dimension(100, 21));
        panPLRApplNewAcc.setLayout(new java.awt.GridBagLayout());

        rdoPLRApplNewAcc.add(rdoPLRApplNewAcc_Yes);
        rdoPLRApplNewAcc_Yes.setText("Yes");
        rdoPLRApplNewAcc_Yes.setMaximumSize(new java.awt.Dimension(50, 27));
        rdoPLRApplNewAcc_Yes.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoPLRApplNewAcc_Yes.setPreferredSize(new java.awt.Dimension(50, 21));
        panPLRApplNewAcc.add(rdoPLRApplNewAcc_Yes, new java.awt.GridBagConstraints());

        rdoPLRApplNewAcc.add(rdoPLRApplNewAcc_No);
        rdoPLRApplNewAcc_No.setText("No");
        rdoPLRApplNewAcc_No.setMinimumSize(new java.awt.Dimension(45, 21));
        rdoPLRApplNewAcc_No.setPreferredSize(new java.awt.Dimension(45, 21));
        panPLRApplNewAcc.add(rdoPLRApplNewAcc_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panInterReceivable_PLR.add(panPLRApplNewAcc, gridBagConstraints);

        lblPLRApplExistingAcc.setText("PLR Applicable for Existing Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPLRApplExistingAcc, gridBagConstraints);

        panPLRApplExistingAcc.setDoubleBuffered(false);
        panPLRApplExistingAcc.setMinimumSize(new java.awt.Dimension(100, 21));
        panPLRApplExistingAcc.setPreferredSize(new java.awt.Dimension(100, 21));
        panPLRApplExistingAcc.setLayout(new java.awt.GridBagLayout());

        rdoPLRApplExistingAcc.add(rdoPLRApplExistingAcc_Yes);
        rdoPLRApplExistingAcc_Yes.setText("Yes");
        rdoPLRApplExistingAcc_Yes.setMaximumSize(new java.awt.Dimension(51, 21));
        rdoPLRApplExistingAcc_Yes.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoPLRApplExistingAcc_Yes.setPreferredSize(new java.awt.Dimension(51, 21));
        panPLRApplExistingAcc.add(rdoPLRApplExistingAcc_Yes, new java.awt.GridBagConstraints());

        rdoPLRApplExistingAcc.add(rdoPLRApplExistingAcc_No);
        rdoPLRApplExistingAcc_No.setText("No");
        rdoPLRApplExistingAcc_No.setMaximumSize(new java.awt.Dimension(51, 27));
        rdoPLRApplExistingAcc_No.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoPLRApplExistingAcc_No.setPreferredSize(new java.awt.Dimension(51, 21));
        panPLRApplExistingAcc.add(rdoPLRApplExistingAcc_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panInterReceivable_PLR.add(panPLRApplExistingAcc, gridBagConstraints);

        lblPlrApplAccSancForm.setText("PLR Applicable For Accounts Sanctioned From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPlrApplAccSancForm, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInterReceivable_PLR.add(tdtPlrApplAccSancForm, gridBagConstraints);

        lblPenalAppl.setText("Penal Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPenalAppl, gridBagConstraints);

        panPenalAppl.setMinimumSize(new java.awt.Dimension(100, 21));
        panPenalAppl.setPreferredSize(new java.awt.Dimension(100, 21));
        panPenalAppl.setLayout(new java.awt.GridBagLayout());

        rdoPenalAppl.add(rdoPenalAppl_Yes);
        rdoPenalAppl_Yes.setText("Yes");
        rdoPenalAppl_Yes.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoPenalAppl_Yes.setPreferredSize(new java.awt.Dimension(51, 21));
        rdoPenalAppl_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalAppl_YesActionPerformed(evt);
            }
        });
        panPenalAppl.add(rdoPenalAppl_Yes, new java.awt.GridBagConstraints());

        rdoPenalAppl.add(rdoPenalAppl_No);
        rdoPenalAppl_No.setText("No");
        rdoPenalAppl_No.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoPenalAppl_No.setPreferredSize(new java.awt.Dimension(51, 21));
        rdoPenalAppl_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalAppl_NoActionPerformed(evt);
            }
        });
        panPenalAppl.add(rdoPenalAppl_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panInterReceivable_PLR.add(panPenalAppl, gridBagConstraints);

        lblLimitExpiryInter.setText("Limit Expiry Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblLimitExpiryInter, gridBagConstraints);

        panLimitExpiryInter.setMinimumSize(new java.awt.Dimension(100, 21));
        panLimitExpiryInter.setPreferredSize(new java.awt.Dimension(100, 21));
        panLimitExpiryInter.setLayout(new java.awt.GridBagLayout());

        rdoLimitExpiryInter.add(rdoLimitExpiryInter_Yes);
        rdoLimitExpiryInter_Yes.setText("Yes");
        rdoLimitExpiryInter_Yes.setMaximumSize(new java.awt.Dimension(50, 21));
        rdoLimitExpiryInter_Yes.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoLimitExpiryInter_Yes.setPreferredSize(new java.awt.Dimension(50, 21));
        rdoLimitExpiryInter_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLimitExpiryInter_YesActionPerformed(evt);
            }
        });
        panLimitExpiryInter.add(rdoLimitExpiryInter_Yes, new java.awt.GridBagConstraints());

        rdoLimitExpiryInter.add(rdoLimitExpiryInter_No);
        rdoLimitExpiryInter_No.setText("No");
        rdoLimitExpiryInter_No.setMaximumSize(new java.awt.Dimension(50, 21));
        rdoLimitExpiryInter_No.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoLimitExpiryInter_No.setPreferredSize(new java.awt.Dimension(50, 21));
        rdoLimitExpiryInter_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLimitExpiryInter_NoActionPerformed(evt);
            }
        });
        panLimitExpiryInter.add(rdoLimitExpiryInter_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 1);
        panInterReceivable_PLR.add(panLimitExpiryInter, gridBagConstraints);

        lblPenalInterRate.setText("Penal Interest Rate For Debit Balance Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPenalInterRate, gridBagConstraints);

        panPenalInterRate.setLayout(new java.awt.GridBagLayout());

        txtPenalInterRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPenalInterRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panPenalInterRate.add(txtPenalInterRate, new java.awt.GridBagConstraints());

        lblPenalInterRate_Per.setText("%");
        panPenalInterRate.add(lblPenalInterRate_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInterReceivable_PLR.add(panPenalInterRate, gridBagConstraints);

        lblExposureLimit_Prud.setText("Exposure Limit (Prudential Norms)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblExposureLimit_Prud, gridBagConstraints);

        panExposureLimit_Prud.setLayout(new java.awt.GridBagLayout());

        txtExposureLimit_Prud.setMinimumSize(new java.awt.Dimension(80, 21));
        txtExposureLimit_Prud.setPreferredSize(new java.awt.Dimension(80, 21));
        panExposureLimit_Prud.add(txtExposureLimit_Prud, new java.awt.GridBagConstraints());

        txtExposureLimit_Prud2.setMinimumSize(new java.awt.Dimension(50, 21));
        txtExposureLimit_Prud2.setPreferredSize(new java.awt.Dimension(50, 21));
        panExposureLimit_Prud.add(txtExposureLimit_Prud2, new java.awt.GridBagConstraints());

        lblExposureLimit_Prud_Per.setText("%");
        panExposureLimit_Prud.add(lblExposureLimit_Prud_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInterReceivable_PLR.add(panExposureLimit_Prud, gridBagConstraints);

        lblExposureLimit_Policy.setText("Exposure Limit (Policy Norms)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblExposureLimit_Policy, gridBagConstraints);

        panExposureLimit_Policy.setLayout(new java.awt.GridBagLayout());

        txtExposureLimit_Policy.setMinimumSize(new java.awt.Dimension(80, 21));
        txtExposureLimit_Policy.setPreferredSize(new java.awt.Dimension(80, 21));
        panExposureLimit_Policy.add(txtExposureLimit_Policy, new java.awt.GridBagConstraints());

        txtExposureLimit_Policy2.setMinimumSize(new java.awt.Dimension(50, 21));
        txtExposureLimit_Policy2.setPreferredSize(new java.awt.Dimension(50, 21));
        panExposureLimit_Policy.add(txtExposureLimit_Policy2, new java.awt.GridBagConstraints());

        lblExposureLimit_Policy_Per.setText("%");
        lblExposureLimit_Policy_Per.setMinimumSize(new java.awt.Dimension(50, 21));
        lblExposureLimit_Policy_Per.setPreferredSize(new java.awt.Dimension(50, 21));
        panExposureLimit_Policy.add(lblExposureLimit_Policy_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInterReceivable_PLR.add(panExposureLimit_Policy, gridBagConstraints);

        lblProductFreq.setText("Product Accumulation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterReceivable_PLR.add(lblProductFreq, gridBagConstraints);

        cboProductFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProductFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInterReceivable_PLR.add(cboProductFreq, gridBagConstraints);

        panPenalAppl_Due.setMaximumSize(new java.awt.Dimension(150, 21));
        panPenalAppl_Due.setMinimumSize(new java.awt.Dimension(170, 21));
        panPenalAppl_Due.setPreferredSize(new java.awt.Dimension(210, 100));
        panPenalAppl_Due.setLayout(new java.awt.GridBagLayout());

        chkprincipalDue.setText("PrincipalDue");
        chkprincipalDue.setMaximumSize(new java.awt.Dimension(105, 27));
        chkprincipalDue.setMinimumSize(new java.awt.Dimension(100, 27));
        chkprincipalDue.setPreferredSize(new java.awt.Dimension(105, 27));
        chkprincipalDue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkprincipalDueActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPenalAppl_Due.add(chkprincipalDue, gridBagConstraints);

        chkInterestDue.setText("Int Due");
        chkInterestDue.setMaximumSize(new java.awt.Dimension(85, 27));
        chkInterestDue.setMinimumSize(new java.awt.Dimension(70, 27));
        chkInterestDue.setPreferredSize(new java.awt.Dimension(85, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 0);
        panPenalAppl_Due.add(chkInterestDue, gridBagConstraints);

        chkEmiPenal.setText("EMI");
        chkEmiPenal.setToolTipText("EMI Amount");
        chkEmiPenal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkEmiPenalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 15);
        panPenalAppl_Due.add(chkEmiPenal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(panPenalAppl_Due, gridBagConstraints);

        lblPenalDue.setText("Penal Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPenalDue, gridBagConstraints);

        lblPrematureCloserMinCalcPeriod.setText("A/c deemed Prepaid if closed before");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPrematureCloserMinCalcPeriod, gridBagConstraints);

        cboPrematureIntCalAmt.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPrematureIntCalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPrematureIntCalAmt.setPopupWidth(100);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInterReceivable_PLR.add(cboPrematureIntCalAmt, gridBagConstraints);

        lblPrematureIntCalcAmt.setText("Premature Closer Int Calc Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblPrematureIntCalcAmt, gridBagConstraints);

        panPreMatIntCalcPeriod.setLayout(new java.awt.GridBagLayout());

        txtPreMatIntCalctPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPreMatIntCalctPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panPreMatIntCalcPeriod.add(txtPreMatIntCalctPeriod, gridBagConstraints);

        cboPeriodFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPreMatIntCalcPeriod.add(cboPeriodFreq, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInterReceivable_PLR.add(panPreMatIntCalcPeriod, gridBagConstraints);

        lblPrematureCloserMinIntCalcPeriod1.setText("In case of Pre Closer Min Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 1);
        panInterReceivable_PLR.add(lblPrematureCloserMinIntCalcPeriod1, gridBagConstraints);

        lblPrematureCloserMinIntCalcPeriod2.setText("Interest to be collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panInterReceivable_PLR.add(lblPrematureCloserMinIntCalcPeriod2, gridBagConstraints);

        panPreMatIntCalcPeriod1.setLayout(new java.awt.GridBagLayout());

        txtPreMatIntCalctPeriod1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPreMatIntCalctPeriod1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panPreMatIntCalcPeriod1.add(txtPreMatIntCalctPeriod1, gridBagConstraints);

        cboPeriodFreq1.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPreMatIntCalcPeriod1.add(cboPeriodFreq1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 1, 4);
        panInterReceivable_PLR.add(panPreMatIntCalcPeriod1, gridBagConstraints);

        lblGracePeriodPenalInterest.setText("Grace Period for Penal Interest In Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblGracePeriodPenalInterest, gridBagConstraints);

        txtGracePeriodPenalInterest.setMinimumSize(new java.awt.Dimension(50, 21));
        txtGracePeriodPenalInterest.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 5, 1, 1);
        panInterReceivable_PLR.add(txtGracePeriodPenalInterest, gridBagConstraints);

        lblInterestDueKeptRecivable.setText("Interest Due to be kept in Interest Recivable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblInterestDueKeptRecivable, gridBagConstraints);

        panInterestDueKeptRecivable.setMinimumSize(new java.awt.Dimension(100, 21));
        panInterestDueKeptRecivable.setPreferredSize(new java.awt.Dimension(100, 21));
        panInterestDueKeptRecivable.setLayout(new java.awt.GridBagLayout());

        InterestDueKeptRecivableGroup.add(rdoInterestDueKeptReceivable_Yes);
        rdoInterestDueKeptReceivable_Yes.setText("Yes");
        rdoInterestDueKeptReceivable_Yes.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoInterestDueKeptReceivable_Yes.setPreferredSize(new java.awt.Dimension(51, 21));
        panInterestDueKeptRecivable.add(rdoInterestDueKeptReceivable_Yes, new java.awt.GridBagConstraints());

        InterestDueKeptRecivableGroup.add(rdoInterestDueKeptReceivable_No);
        rdoInterestDueKeptReceivable_No.setText("No");
        rdoInterestDueKeptReceivable_No.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoInterestDueKeptReceivable_No.setPreferredSize(new java.awt.Dimension(51, 21));
        panInterestDueKeptRecivable.add(rdoInterestDueKeptReceivable_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(panInterestDueKeptRecivable, gridBagConstraints);

        panPenalInterRate1.setLayout(new java.awt.GridBagLayout());

        txtInsuraceRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInsuraceRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panPenalInterRate1.add(txtInsuraceRate, new java.awt.GridBagConstraints());

        lblPenalInterRate_Per1.setText("%");
        panPenalInterRate1.add(lblPenalInterRate_Per1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        panInterReceivable_PLR.add(panPenalInterRate1, gridBagConstraints);

        lblInsuraceRate.setText("Insurance Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblInsuraceRate, gridBagConstraints);

        panInterestDueKeptRecivable1.setMinimumSize(new java.awt.Dimension(210, 21));
        panInterestDueKeptRecivable1.setPreferredSize(new java.awt.Dimension(240, 21));
        panInterestDueKeptRecivable1.setLayout(new java.awt.GridBagLayout());

        InterestDueKeptRecivableGroup.add(rdosanctionDate_Yes);
        rdosanctionDate_Yes.setText("Sanction Date");
        rdosanctionDate_Yes.setMaximumSize(new java.awt.Dimension(110, 21));
        rdosanctionDate_Yes.setMinimumSize(new java.awt.Dimension(110, 21));
        rdosanctionDate_Yes.setPreferredSize(new java.awt.Dimension(110, 21));
        panInterestDueKeptRecivable1.add(rdosanctionDate_Yes, new java.awt.GridBagConstraints());

        InterestDueKeptRecivableGroup.add(rdosanctionDate_No);
        rdosanctionDate_No.setText("Closing Date");
        rdosanctionDate_No.setMaximumSize(new java.awt.Dimension(100, 21));
        rdosanctionDate_No.setMinimumSize(new java.awt.Dimension(100, 21));
        rdosanctionDate_No.setPreferredSize(new java.awt.Dimension(100, 21));
        panInterestDueKeptRecivable1.add(rdosanctionDate_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 1);
        panInterReceivable_PLR.add(panInterestDueKeptRecivable1, gridBagConstraints);

        lblMarketRateAsOn.setText("Apply Market Rate As On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblMarketRateAsOn, gridBagConstraints);

        lblInsuranceCharge.setText("Insurance Charge Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(lblInsuranceCharge, gridBagConstraints);

        panInsuranceChargeApplicable.setMinimumSize(new java.awt.Dimension(100, 21));
        panInsuranceChargeApplicable.setPreferredSize(new java.awt.Dimension(100, 21));
        panInsuranceChargeApplicable.setLayout(new java.awt.GridBagLayout());

        InterestDueKeptRecivableGroup.add(rdoinsuranceCharge_Yes);
        rdoinsuranceCharge_Yes.setText("Yes");
        rdoinsuranceCharge_Yes.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoinsuranceCharge_Yes.setPreferredSize(new java.awt.Dimension(51, 21));
        rdoinsuranceCharge_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoinsuranceCharge_YesActionPerformed(evt);
            }
        });
        panInsuranceChargeApplicable.add(rdoinsuranceCharge_Yes, new java.awt.GridBagConstraints());

        InterestDueKeptRecivableGroup.add(rdoinsuranceCharge_No);
        rdoinsuranceCharge_No.setText("No");
        rdoinsuranceCharge_No.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoinsuranceCharge_No.setPreferredSize(new java.awt.Dimension(51, 21));
        rdoinsuranceCharge_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoinsuranceCharge_NoActionPerformed(evt);
            }
        });
        panInsuranceChargeApplicable.add(rdoinsuranceCharge_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInterReceivable_PLR.add(panInsuranceChargeApplicable, gridBagConstraints);

        lblPenalCalcBasedOn.setText("EMI Penal Calculation Based on ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        panInterReceivable_PLR.add(lblPenalCalcBasedOn, gridBagConstraints);

        panEmiCalc.setLayout(new java.awt.GridBagLayout());

        rdoPenalCalcDays.setText("Days");
        rdoPenalCalcDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalCalcDaysActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panEmiCalc.add(rdoPenalCalcDays, gridBagConstraints);

        rdoPenalCalcMonths.setText("Months");
        rdoPenalCalcMonths.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalCalcMonthsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panEmiCalc.add(rdoPenalCalcMonths, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        panInterReceivable_PLR.add(panEmiCalc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable.add(panInterReceivable_PLR, gridBagConstraints);

        sptInterReceivable.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable.add(sptInterReceivable, gridBagConstraints);

        tabLoanProduct.addTab("Interest Receivable", panInterReceivable);

        panCharges.setMinimumSize(new java.awt.Dimension(429, 450));
        panCharges.setPreferredSize(new java.awt.Dimension(700, 700));
        panCharges.setLayout(new java.awt.GridBagLayout());

        panCharge.setLayout(new java.awt.GridBagLayout());

        panAccCharges.setLayout(new java.awt.GridBagLayout());

        lblAccClosingCharges.setText("Account Closing Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 5);
        panAccCharges.add(lblAccClosingCharges, gridBagConstraints);

        txtAccClosingCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccClosingCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccClosingChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccCharges.add(txtAccClosingCharges, gridBagConstraints);

        lblMiscSerCharges.setText("Misc Service Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 5);
        panAccCharges.add(lblMiscSerCharges, gridBagConstraints);

        txtMiscSerCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMiscSerCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMiscSerChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccCharges.add(txtMiscSerCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 76, 4, 4);
        panCharge.add(panAccCharges, gridBagConstraints);

        sptCharges_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharge.add(sptCharges_Vert, gridBagConstraints);

        panStateCharges.setLayout(new java.awt.GridBagLayout());

        lblStatCharges.setText("Statement Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 5);
        panStateCharges.add(lblStatCharges, gridBagConstraints);

        panStatCharges.setMaximumSize(new java.awt.Dimension(130, 27));
        panStatCharges.setMinimumSize(new java.awt.Dimension(130, 27));
        panStatCharges.setPreferredSize(new java.awt.Dimension(130, 27));
        panStatCharges.setLayout(new java.awt.GridBagLayout());

        rdoStatCharges.add(rdoStatCharges_Yes);
        rdoStatCharges_Yes.setText("Yes");
        rdoStatCharges_Yes.setMaximumSize(new java.awt.Dimension(60, 21));
        rdoStatCharges_Yes.setMinimumSize(new java.awt.Dimension(60, 21));
        rdoStatCharges_Yes.setPreferredSize(new java.awt.Dimension(60, 21));
        rdoStatCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStatCharges_YesActionPerformed(evt);
            }
        });
        panStatCharges.add(rdoStatCharges_Yes, new java.awt.GridBagConstraints());

        rdoStatCharges.add(rdoStatCharges_No);
        rdoStatCharges_No.setText("No");
        rdoStatCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStatCharges_NoActionPerformed(evt);
            }
        });
        panStatCharges.add(rdoStatCharges_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panStateCharges.add(panStatCharges, gridBagConstraints);

        lblStatChargesRate.setText("Statement Charges Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panStateCharges.add(lblStatChargesRate, gridBagConstraints);

        txtStatChargesRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 4, 4);
        panStateCharges.add(txtStatChargesRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 57, 4, 4);
        panCharge.add(panStateCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 6);
        panCharges.add(panCharge, gridBagConstraints);

        panChequeReturnCharges.setBorder(javax.swing.BorderFactory.createTitledBorder("Cheque Return / Outward Charges"));
        panChequeReturnCharges.setMinimumSize(new java.awt.Dimension(650, 125));
        panChequeReturnCharges.setPreferredSize(new java.awt.Dimension(650, 140));
        panChequeReturnCharges.setLayout(new java.awt.GridBagLayout());

        pancharge_Amt.setLayout(new java.awt.GridBagLayout());

        lblRangeFrom.setText("Range From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        pancharge_Amt.add(lblRangeFrom, gridBagConstraints);

        lblRateAmt.setText("Rate of Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        pancharge_Amt.add(lblRateAmt, gridBagConstraints);

        txtRateAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        pancharge_Amt.add(txtRateAmt, gridBagConstraints);

        panButton.setLayout(new java.awt.GridBagLayout());

        btnCharge_New.setText("New");
        btnCharge_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCharge_NewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnCharge_New, gridBagConstraints);

        btnCharge_Save.setText("Save");
        btnCharge_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCharge_SaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnCharge_Save, gridBagConstraints);

        btnCharge_Delete.setText("Delete");
        btnCharge_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCharge_DeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton.add(btnCharge_Delete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        pancharge_Amt.add(panButton, gridBagConstraints);

        lblRangeTo.setText("Range To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        pancharge_Amt.add(lblRangeTo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        pancharge_Amt.add(txtRangeFrom, gridBagConstraints);

        txtRangeTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRangeToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        pancharge_Amt.add(txtRangeTo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 60, 4, 4);
        panChequeReturnCharges.add(pancharge_Amt, gridBagConstraints);

        panCharges_Table.setLayout(new java.awt.GridBagLayout());

        srpCharges.setMinimumSize(new java.awt.Dimension(250, 105));
        srpCharges.setPreferredSize(new java.awt.Dimension(250, 105));

        tblCharges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Range From", "Range To", "Rate of Amount"
            }
        ));
        tblCharges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblChargesMousePressed(evt);
            }
        });
        srpCharges.setViewportView(tblCharges);

        panCharges_Table.add(srpCharges, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 70, 4, 4);
        panChequeReturnCharges.add(panCharges_Table, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 80, 4, 4);
        panCharges.add(panChequeReturnCharges, gridBagConstraints);

        panFolio.setBorder(javax.swing.BorderFactory.createTitledBorder("Folio"));
        panFolio.setMinimumSize(new java.awt.Dimension(830, 152));
        panFolio.setPreferredSize(new java.awt.Dimension(900, 191));
        panFolio.setLayout(new java.awt.GridBagLayout());

        panFolio_Date.setMinimumSize(new java.awt.Dimension(327, 127));
        panFolio_Date.setLayout(new java.awt.GridBagLayout());

        lblFolioChargesAppl.setText("Charges Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panFolio_Date.add(lblFolioChargesAppl, gridBagConstraints);

        panFolioChargesAppl.setMaximumSize(new java.awt.Dimension(130, 27));
        panFolioChargesAppl.setMinimumSize(new java.awt.Dimension(100, 27));
        panFolioChargesAppl.setPreferredSize(new java.awt.Dimension(130, 27));
        panFolioChargesAppl.setLayout(new java.awt.GridBagLayout());

        rdoFolioChargesAppl.add(rdoFolioChargesAppl_Yes);
        rdoFolioChargesAppl_Yes.setText("Yes");
        rdoFolioChargesAppl_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFolioChargesAppl_YesActionPerformed(evt);
            }
        });
        panFolioChargesAppl.add(rdoFolioChargesAppl_Yes, new java.awt.GridBagConstraints());

        rdoFolioChargesAppl.add(rdoFolioChargesAppl_No);
        rdoFolioChargesAppl_No.setText("No");
        rdoFolioChargesAppl_No.setMinimumSize(new java.awt.Dimension(45, 27));
        rdoFolioChargesAppl_No.setPreferredSize(new java.awt.Dimension(45, 27));
        rdoFolioChargesAppl_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFolioChargesAppl_NoActionPerformed(evt);
            }
        });
        panFolioChargesAppl.add(rdoFolioChargesAppl_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panFolio_Date.add(panFolioChargesAppl, gridBagConstraints);

        lblLastFolioChargesAppl.setText("Last Charges Applied On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Date.add(lblLastFolioChargesAppl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 4, 4);
        panFolio_Date.add(tdtLastFolioChargesAppl, gridBagConstraints);

        lblNoEntriesPerFolio.setText("No of Entries per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Date.add(lblNoEntriesPerFolio, gridBagConstraints);

        txtNoEntriesPerFolio.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 4, 4);
        panFolio_Date.add(txtNoEntriesPerFolio, gridBagConstraints);

        lblNextFolioDDate.setText("Next Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Date.add(lblNextFolioDDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 4, 4);
        panFolio_Date.add(tdtNextFolioDDate, gridBagConstraints);

        cboFolioChargesAppFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboFolioChargesAppFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFolioChargesAppFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFolioChargesAppFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 7, 4, 4);
        panFolio_Date.add(cboFolioChargesAppFreq, gridBagConstraints);

        lblFolioChargesAppFreq.setText("Charges Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Date.add(lblFolioChargesAppFreq, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 50, 4, 4);
        panFolio.add(panFolio_Date, gridBagConstraints);

        sptCharges_Vert2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio.add(sptCharges_Vert2, gridBagConstraints);

        panFolio_Freq.setMinimumSize(new java.awt.Dimension(420, 130));
        panFolio_Freq.setLayout(new java.awt.GridBagLayout());

        lblToCollectFolioCharges.setText("To Collect Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panFolio_Freq.add(lblToCollectFolioCharges, gridBagConstraints);

        cboToCollectFolioCharges.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboToCollectFolioCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        cboToCollectFolioCharges.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panFolio_Freq.add(cboToCollectFolioCharges, gridBagConstraints);

        lblToChargeOn.setText("To Charge On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Freq.add(lblToChargeOn, gridBagConstraints);

        panToChargeOn.setLayout(new java.awt.GridBagLayout());

        rdoToChargeOn.add(rdoToChargeOn_Man);
        rdoToChargeOn_Man.setText("Manual");
        panToChargeOn.add(rdoToChargeOn_Man, new java.awt.GridBagConstraints());

        rdoToChargeOn.add(rdoToChargeOn_Sys);
        rdoToChargeOn_Sys.setText("System");
        panToChargeOn.add(rdoToChargeOn_Sys, new java.awt.GridBagConstraints());

        rdoToChargeOn.add(rdoToChargeOn_Both);
        rdoToChargeOn_Both.setText("Both");
        panToChargeOn.add(rdoToChargeOn_Both, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 4, 4);
        panFolio_Freq.add(panToChargeOn, gridBagConstraints);

        lblIncompleteFolioRoundOffFreq.setText("Incomplete Rounding Off frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Freq.add(lblIncompleteFolioRoundOffFreq, gridBagConstraints);

        cboIncompleteFolioRoundOffFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboIncompleteFolioRoundOffFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Freq.add(cboIncompleteFolioRoundOffFreq, gridBagConstraints);

        lblRatePerFolio.setText("Rate per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFolio_Freq.add(lblRatePerFolio, gridBagConstraints);

        txtRatePerFolio.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panFolio_Freq.add(txtRatePerFolio, gridBagConstraints);

        panToChargeType.setLayout(new java.awt.GridBagLayout());

        rdoFolioChargeType.add(rdoToChargeType_Credit);
        rdoToChargeType_Credit.setText("Credit");
        panToChargeType.add(rdoToChargeType_Credit, new java.awt.GridBagConstraints());

        rdoFolioChargeType.add(rdoToChargeType_Debit);
        rdoToChargeType_Debit.setText("Debit");
        panToChargeType.add(rdoToChargeType_Debit, new java.awt.GridBagConstraints());

        rdoFolioChargeType.add(rdoToChargeType_Both);
        rdoToChargeType_Both.setText("Both");
        panToChargeType.add(rdoToChargeType_Both, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 4, 4);
        panFolio_Freq.add(panToChargeType, gridBagConstraints);

        lblChargeAppliedType.setText("To Charge AppliedType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolio_Freq.add(lblChargeAppliedType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio.add(panFolio_Freq, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 19, 4, 4);
        panCharges.add(panFolio, gridBagConstraints);

        panCharge_ProcCommit.setMinimumSize(new java.awt.Dimension(800, 75));
        panCharge_ProcCommit.setPreferredSize(new java.awt.Dimension(650, 95));
        panCharge_ProcCommit.setLayout(new java.awt.GridBagLayout());

        panProcessCharge.setMinimumSize(new java.awt.Dimension(380, 59));
        panProcessCharge.setPreferredSize(new java.awt.Dimension(400, 59));
        panProcessCharge.setLayout(new java.awt.GridBagLayout());

        lblProcessCharges.setText("Processing Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcessCharge.add(lblProcessCharges, gridBagConstraints);

        panProcessCharges.setMaximumSize(new java.awt.Dimension(100, 21));
        panProcessCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        panProcessCharges.setPreferredSize(new java.awt.Dimension(100, 21));
        panProcessCharges.setLayout(new java.awt.GridBagLayout());

        rdoProcessCharges.add(rdoProcessCharges_Yes);
        rdoProcessCharges_Yes.setText("Yes");
        rdoProcessCharges_Yes.setMaximumSize(new java.awt.Dimension(50, 21));
        rdoProcessCharges_Yes.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoProcessCharges_Yes.setPreferredSize(new java.awt.Dimension(50, 21));
        rdoProcessCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoProcessCharges_YesActionPerformed(evt);
            }
        });
        panProcessCharges.add(rdoProcessCharges_Yes, new java.awt.GridBagConstraints());

        rdoProcessCharges.add(rdoProcessCharges_No);
        rdoProcessCharges_No.setText("No");
        rdoProcessCharges_No.setMaximumSize(new java.awt.Dimension(50, 21));
        rdoProcessCharges_No.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoProcessCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoProcessCharges_NoActionPerformed(evt);
            }
        });
        panProcessCharges.add(rdoProcessCharges_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcessCharge.add(panProcessCharges, gridBagConstraints);

        lblCharge_1.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcessCharge.add(lblCharge_1, gridBagConstraints);

        panCharge_1.setLayout(new java.awt.GridBagLayout());

        lblLimit.setText("  Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCharge_1.add(lblLimit, gridBagConstraints);

        txtCharge_Limit2.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCharge_1.add(txtCharge_Limit2, gridBagConstraints);

        cboCharge_Limit2.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCharge_Limit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCharge_Limit2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCharge_1.add(cboCharge_Limit2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 4, 4);
        panProcessCharge.add(panCharge_1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 23, 4, 4);
        panCharge_ProcCommit.add(panProcessCharge, gridBagConstraints);

        sptCharges_Vert3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharge_ProcCommit.add(sptCharges_Vert3, gridBagConstraints);

        panCommitCharges.setMinimumSize(new java.awt.Dimension(410, 85));
        panCommitCharges.setPreferredSize(new java.awt.Dimension(410, 85));
        panCommitCharges.setLayout(new java.awt.GridBagLayout());

        lblCommitmentCharge.setText("Commitment Charges");
        lblCommitmentCharge.setMinimumSize(new java.awt.Dimension(118, 18));
        lblCommitmentCharge.setPreferredSize(new java.awt.Dimension(118, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCommitCharges.add(lblCommitmentCharge, gridBagConstraints);

        panCommitmentCharge.setMaximumSize(new java.awt.Dimension(130, 27));
        panCommitmentCharge.setMinimumSize(new java.awt.Dimension(100, 27));
        panCommitmentCharge.setPreferredSize(new java.awt.Dimension(100, 27));
        panCommitmentCharge.setLayout(new java.awt.GridBagLayout());

        rdoCommitmentCharge.add(rdoCommitmentCharge_Yes);
        rdoCommitmentCharge_Yes.setText("Yes");
        rdoCommitmentCharge_Yes.setMaximumSize(new java.awt.Dimension(60, 21));
        rdoCommitmentCharge_Yes.setMinimumSize(new java.awt.Dimension(60, 21));
        rdoCommitmentCharge_Yes.setPreferredSize(new java.awt.Dimension(60, 21));
        rdoCommitmentCharge_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCommitmentCharge_YesActionPerformed(evt);
            }
        });
        panCommitmentCharge.add(rdoCommitmentCharge_Yes, new java.awt.GridBagConstraints());

        rdoCommitmentCharge.add(rdoCommitmentCharge_No);
        rdoCommitmentCharge_No.setText("No");
        rdoCommitmentCharge_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCommitmentCharge_NoActionPerformed(evt);
            }
        });
        panCommitmentCharge.add(rdoCommitmentCharge_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCommitCharges.add(panCommitmentCharge, gridBagConstraints);

        lblCharge_2.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 10);
        panCommitCharges.add(lblCharge_2, gridBagConstraints);

        panCharge_2.setLayout(new java.awt.GridBagLayout());

        lblLimits.setText("  Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCharge_2.add(lblLimits, gridBagConstraints);

        txtCharge_Limit3.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCharge_2.add(txtCharge_Limit3, gridBagConstraints);

        cboCharge_Limit3.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCharge_Limit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCharge_Limit3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCharge_2.add(cboCharge_Limit3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 4, 4);
        panCommitCharges.add(panCharge_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharge_ProcCommit.add(panCommitCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 16, 4, 4);
        panCharges.add(panCharge_ProcCommit, gridBagConstraints);

        panNoticeCharges.setBorder(javax.swing.BorderFactory.createTitledBorder("Notice Charges"));
        panNoticeCharges.setMinimumSize(new java.awt.Dimension(650, 150));
        panNoticeCharges.setPreferredSize(new java.awt.Dimension(650, 140));
        panNoticeCharges.setLayout(new java.awt.GridBagLayout());

        panNoticecharge_Amt.setLayout(new java.awt.GridBagLayout());

        lblNoticeType.setText("NotiiceType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNoticecharge_Amt.add(lblNoticeType, gridBagConstraints);

        lblNoticeChargeAmt.setText("NoticeChargeAmt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNoticecharge_Amt.add(lblNoticeChargeAmt, gridBagConstraints);

        panNoticeButton.setLayout(new java.awt.GridBagLayout());

        btnNotice_Charge_New.setText("New");
        btnNotice_Charge_New.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotice_Charge_NewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNoticeButton.add(btnNotice_Charge_New, gridBagConstraints);

        btnNotice_Charge_Save.setText("Save");
        btnNotice_Charge_Save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotice_Charge_SaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNoticeButton.add(btnNotice_Charge_Save, gridBagConstraints);

        btnNotice_Charge_Delete.setText("Delete");
        btnNotice_Charge_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNotice_Charge_DeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNoticeButton.add(btnNotice_Charge_Delete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panNoticecharge_Amt.add(panNoticeButton, gridBagConstraints);

        lblIssueAfter.setText("IssueAfter");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNoticecharge_Amt.add(lblIssueAfter, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panNoticecharge_Amt.add(cboNoticeType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panNoticecharge_Amt.add(cboIssueAfter, gridBagConstraints);

        lblPostageAmt.setText("Stamp Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNoticecharge_Amt.add(lblPostageAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panNoticecharge_Amt.add(txtNoticeChargeAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panNoticecharge_Amt.add(txtPostageAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 4, 4);
        panNoticeCharges.add(panNoticecharge_Amt, gridBagConstraints);

        panNoticeCharges_Table.setLayout(new java.awt.GridBagLayout());

        srpNoticeCharges.setMinimumSize(new java.awt.Dimension(250, 105));
        srpNoticeCharges.setPreferredSize(new java.awt.Dimension(250, 105));

        tblNoticeCharges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NoticeType", "IssueAfter", "NoticeChargeAmt", "Postage_Amt"
            }
        ));
        tblNoticeCharges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblNoticeChargesMousePressed(evt);
            }
        });
        srpNoticeCharges.setViewportView(tblNoticeCharges);

        panNoticeCharges_Table.add(srpNoticeCharges, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 70, 4, 4);
        panNoticeCharges.add(panNoticeCharges_Table, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 80, 4, 4);
        panCharges.add(panNoticeCharges, gridBagConstraints);
        panNoticeCharges.getAccessibleContext().setAccessibleParent(panNoticeCharges);

        tabLoanProduct.addTab("Charges", panCharges);

        panSpecial_NonPerfoormingAssets.setLayout(new java.awt.GridBagLayout());

        panSpeItems.setBorder(javax.swing.BorderFactory.createTitledBorder("Special Items"));
        panSpeItems.setMinimumSize(new java.awt.Dimension(469, 159));
        panSpeItems.setPreferredSize(new java.awt.Dimension(469, 159));
        panSpeItems.setLayout(new java.awt.GridBagLayout());

        lblATMcardIssued.setText("ATM Card Issued?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panSpeItems.add(lblATMcardIssued, gridBagConstraints);

        rdoATMcardIssued.add(rdoATMcardIssued_Yes);
        rdoATMcardIssued_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panSpeItems.add(rdoATMcardIssued_Yes, gridBagConstraints);

        rdoATMcardIssued.add(rdoATMcardIssued_No);
        rdoATMcardIssued_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panSpeItems.add(rdoATMcardIssued_No, gridBagConstraints);

        lblCreditCardIssued.setText("Credit Card Issued?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(lblCreditCardIssued, gridBagConstraints);

        rdoCreditCardIssued.add(rdoCreditCardIssued_Yes);
        rdoCreditCardIssued_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(rdoCreditCardIssued_Yes, gridBagConstraints);

        rdoCreditCardIssued.add(rdoCreditCardIssued_No);
        rdoCreditCardIssued_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(rdoCreditCardIssued_No, gridBagConstraints);

        lblMobileBanlingClient.setText("Mobile Banking Client? ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(lblMobileBanlingClient, gridBagConstraints);

        rdoMobileBanlingClient.add(rdoMobileBanlingClient_Yes);
        rdoMobileBanlingClient_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(rdoMobileBanlingClient_Yes, gridBagConstraints);

        rdoMobileBanlingClient.add(rdoMobileBanlingClient_No);
        rdoMobileBanlingClient_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(rdoMobileBanlingClient_No, gridBagConstraints);

        lblIsAnyBranBankingAllowed.setText("Is Any Branch Banking Allowed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(lblIsAnyBranBankingAllowed, gridBagConstraints);

        rdoIsAnyBranBankingAllowed.add(rdoIsAnyBranBankingAllowed_Yes);
        rdoIsAnyBranBankingAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(rdoIsAnyBranBankingAllowed_Yes, gridBagConstraints);

        rdoIsAnyBranBankingAllowed.add(rdoIsAnyBranBankingAllowed_No);
        rdoIsAnyBranBankingAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSpeItems.add(rdoIsAnyBranBankingAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecial_NonPerfoormingAssets.add(panSpeItems, gridBagConstraints);

        panNonPerAssets.setBorder(javax.swing.BorderFactory.createTitledBorder("Non-Performing Assets"));
        panNonPerAssets.setLayout(new java.awt.GridBagLayout());

        lblMinPeriodsArrears.setText("Minimum Periods of Arrears");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblMinPeriodsArrears, gridBagConstraints);

        panMinPeriodsArrears.setLayout(new java.awt.GridBagLayout());

        txtMinPeriodsArrears.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinPeriodsArrears.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinPeriodsArrears.add(txtMinPeriodsArrears, new java.awt.GridBagConstraints());

        cboMinPeriodsArrears.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboMinPeriodsArrears.setMinimumSize(new java.awt.Dimension(88, 21));
        cboMinPeriodsArrears.setPreferredSize(new java.awt.Dimension(88, 21));
        cboMinPeriodsArrears.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMinPeriodsArrearsActionPerformed(evt);
            }
        });
        panMinPeriodsArrears.add(cboMinPeriodsArrears, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(panMinPeriodsArrears, gridBagConstraints);

        lblPeriodTranSStanAssets.setText("Period after which to be Transfered to Sub-Standard Assets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblPeriodTranSStanAssets, gridBagConstraints);

        panPeriodTranSStanAssets.setLayout(new java.awt.GridBagLayout());

        txtPeriodTranSStanAssets.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriodTranSStanAssets.setPreferredSize(new java.awt.Dimension(50, 21));
        panPeriodTranSStanAssets.add(txtPeriodTranSStanAssets, new java.awt.GridBagConstraints());

        cboPeriodTranSStanAssets.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPeriodTranSStanAssets.setMinimumSize(new java.awt.Dimension(88, 21));
        cboPeriodTranSStanAssets.setPreferredSize(new java.awt.Dimension(88, 21));
        cboPeriodTranSStanAssets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeriodTranSStanAssetsActionPerformed(evt);
            }
        });
        panPeriodTranSStanAssets.add(cboPeriodTranSStanAssets, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(panPeriodTranSStanAssets, gridBagConstraints);

        lblProvisionsStanAssets.setText("Provision for Sub-Standard Assets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblProvisionsStanAssets, gridBagConstraints);

        lblPeriodTransDoubtfulAssets1.setText("Period after which to be Transfered to Doubtful Assets 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblPeriodTransDoubtfulAssets1, gridBagConstraints);

        panPeriodTransDoubtfulAssets1.setLayout(new java.awt.GridBagLayout());

        txtPeriodTransDoubtfulAssets1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriodTransDoubtfulAssets1.setPreferredSize(new java.awt.Dimension(50, 21));
        panPeriodTransDoubtfulAssets1.add(txtPeriodTransDoubtfulAssets1, new java.awt.GridBagConstraints());

        cboPeriodTransDoubtfulAssets1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPeriodTransDoubtfulAssets1.setMinimumSize(new java.awt.Dimension(88, 21));
        cboPeriodTransDoubtfulAssets1.setPreferredSize(new java.awt.Dimension(88, 21));
        cboPeriodTransDoubtfulAssets1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeriodTransDoubtfulAssets1ActionPerformed(evt);
            }
        });
        panPeriodTransDoubtfulAssets1.add(cboPeriodTransDoubtfulAssets1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(panPeriodTransDoubtfulAssets1, gridBagConstraints);

        lblProvisionDoubtfulAssets1.setText("Provision for Douibtful Assets 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblProvisionDoubtfulAssets1, gridBagConstraints);

        lblPeriodTransLossAssets.setText("Period after which to be Transfered to Loss Assets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblPeriodTransLossAssets, gridBagConstraints);

        panPeriodTransLossAssets.setLayout(new java.awt.GridBagLayout());

        txtPeriodTransLossAssets.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriodTransLossAssets.setPreferredSize(new java.awt.Dimension(50, 21));
        panPeriodTransLossAssets.add(txtPeriodTransLossAssets, new java.awt.GridBagConstraints());

        cboPeriodTransLossAssets.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPeriodTransLossAssets.setMinimumSize(new java.awt.Dimension(88, 21));
        cboPeriodTransLossAssets.setPreferredSize(new java.awt.Dimension(88, 21));
        cboPeriodTransLossAssets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeriodTransLossAssetsActionPerformed(evt);
            }
        });
        panPeriodTransLossAssets.add(cboPeriodTransLossAssets, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(panPeriodTransLossAssets, gridBagConstraints);

        lblPeriodAfterWhichTransNPerformingAssets.setText("Period After Which to be Transfered to Non-Performing Assets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 2);
        panNonPerAssets.add(lblPeriodAfterWhichTransNPerformingAssets, gridBagConstraints);

        panPeriodAfterWhichTransNPerformingAssets.setLayout(new java.awt.GridBagLayout());

        txtPeriodAfterWhichTransNPerformingAssets.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriodAfterWhichTransNPerformingAssets.setPreferredSize(new java.awt.Dimension(50, 21));
        panPeriodAfterWhichTransNPerformingAssets.add(txtPeriodAfterWhichTransNPerformingAssets, new java.awt.GridBagConstraints());

        cboPeriodAfterWhichTransNPerformingAssets.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPeriodAfterWhichTransNPerformingAssets.setMinimumSize(new java.awt.Dimension(88, 21));
        cboPeriodAfterWhichTransNPerformingAssets.setPreferredSize(new java.awt.Dimension(88, 21));
        cboPeriodAfterWhichTransNPerformingAssets.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeriodAfterWhichTransNPerformingAssetsActionPerformed(evt);
            }
        });
        panPeriodAfterWhichTransNPerformingAssets.add(cboPeriodAfterWhichTransNPerformingAssets, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 2);
        panNonPerAssets.add(panPeriodAfterWhichTransNPerformingAssets, gridBagConstraints);

        txtProvisionsStanAssets.setMinimumSize(new java.awt.Dimension(50, 21));
        txtProvisionsStanAssets.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(txtProvisionsStanAssets, gridBagConstraints);

        lblProvisionsStanAssetsPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNonPerAssets.add(lblProvisionsStanAssetsPer, gridBagConstraints);

        txtProvisionDoubtfulAssets1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtProvisionDoubtfulAssets1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(txtProvisionDoubtfulAssets1, gridBagConstraints);

        lblProvisionDoubtfulAssetsPer1.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panNonPerAssets.add(lblProvisionDoubtfulAssetsPer1, gridBagConstraints);

        lblProvisionStandardAssetss.setText("Provision for Standard Assets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblProvisionStandardAssetss, gridBagConstraints);

        txtProvisionStandardAssetss.setMinimumSize(new java.awt.Dimension(50, 21));
        txtProvisionStandardAssetss.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(txtProvisionStandardAssetss, gridBagConstraints);

        lblProvisionStandardAssetssPer.setText("%");
        lblProvisionStandardAssetssPer.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNonPerAssets.add(lblProvisionStandardAssetssPer, gridBagConstraints);

        lblProvisionLossAssets.setText("Provision for Loss Assets");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblProvisionLossAssets, gridBagConstraints);

        txtProvisionLossAssets.setMinimumSize(new java.awt.Dimension(50, 21));
        txtProvisionLossAssets.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(txtProvisionLossAssets, gridBagConstraints);

        lblProvisionLossAssetsPer.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNonPerAssets.add(lblProvisionLossAssetsPer, gridBagConstraints);

        lblPeriodTransDoubtfulAssets2.setText("Period after which to be Transfered to Doubtful Assets 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblPeriodTransDoubtfulAssets2, gridBagConstraints);

        panPeriodTransDoubtfulAssets2.setLayout(new java.awt.GridBagLayout());

        txtPeriodTransDoubtfulAssets2.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriodTransDoubtfulAssets2.setPreferredSize(new java.awt.Dimension(50, 21));
        panPeriodTransDoubtfulAssets2.add(txtPeriodTransDoubtfulAssets2, new java.awt.GridBagConstraints());

        cboPeriodTransDoubtfulAssets2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPeriodTransDoubtfulAssets2.setMinimumSize(new java.awt.Dimension(88, 21));
        cboPeriodTransDoubtfulAssets2.setPreferredSize(new java.awt.Dimension(88, 21));
        cboPeriodTransDoubtfulAssets2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeriodTransDoubtfulAssets2ActionPerformed(evt);
            }
        });
        panPeriodTransDoubtfulAssets2.add(cboPeriodTransDoubtfulAssets2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(panPeriodTransDoubtfulAssets2, gridBagConstraints);

        lblProvisionDoubtfulAssets2.setText("Provision for Douibtful Assets 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblProvisionDoubtfulAssets2, gridBagConstraints);

        txtProvisionDoubtfulAssets2.setMinimumSize(new java.awt.Dimension(50, 21));
        txtProvisionDoubtfulAssets2.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(txtProvisionDoubtfulAssets2, gridBagConstraints);

        lblProvisionDoubtfulAssetsPer2.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panNonPerAssets.add(lblProvisionDoubtfulAssetsPer2, gridBagConstraints);

        lblPeriodTransDoubtfulAssets3.setText("Period after which to be Transfered to Doubtful Assets 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblPeriodTransDoubtfulAssets3, gridBagConstraints);

        panPeriodTransDoubtfulAssets3.setLayout(new java.awt.GridBagLayout());

        txtPeriodTransDoubtfulAssets3.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriodTransDoubtfulAssets3.setPreferredSize(new java.awt.Dimension(50, 21));
        panPeriodTransDoubtfulAssets3.add(txtPeriodTransDoubtfulAssets3, new java.awt.GridBagConstraints());

        cboPeriodTransDoubtfulAssets3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPeriodTransDoubtfulAssets3.setMinimumSize(new java.awt.Dimension(88, 21));
        cboPeriodTransDoubtfulAssets3.setPreferredSize(new java.awt.Dimension(88, 21));
        cboPeriodTransDoubtfulAssets3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPeriodTransDoubtfulAssets3ActionPerformed(evt);
            }
        });
        panPeriodTransDoubtfulAssets3.add(cboPeriodTransDoubtfulAssets3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(panPeriodTransDoubtfulAssets3, gridBagConstraints);

        lblProvisionDoubtfulAssets3.setText("Provision for Douibtful Assets 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(lblProvisionDoubtfulAssets3, gridBagConstraints);

        txtProvisionDoubtfulAssets3.setMinimumSize(new java.awt.Dimension(50, 21));
        txtProvisionDoubtfulAssets3.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panNonPerAssets.add(txtProvisionDoubtfulAssets3, gridBagConstraints);

        lblProvisionDoubtfulAssetsPer3.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panNonPerAssets.add(lblProvisionDoubtfulAssetsPer3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecial_NonPerfoormingAssets.add(panNonPerAssets, gridBagConstraints);

        tabLoanProduct.addTab("Special Asset/ Non-Performance Assets", panSpecial_NonPerfoormingAssets);

        panInterCalculation.setLayout(new java.awt.GridBagLayout());

        lblcalcType.setText("Calculation Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblcalcType, gridBagConstraints);

        cbocalcType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cbocalcType.setMinimumSize(new java.awt.Dimension(100, 21));
        cbocalcType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbocalcTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(cbocalcType, gridBagConstraints);

        lblMinPeriod.setText("Minimum Term of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblMinPeriod, gridBagConstraints);

        panMinPeriod.setLayout(new java.awt.GridBagLayout());

        txtMinPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinPeriod.add(txtMinPeriod, new java.awt.GridBagConstraints());

        cboMinPeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboMinPeriod.setMinimumSize(new java.awt.Dimension(88, 21));
        cboMinPeriod.setPreferredSize(new java.awt.Dimension(88, 21));
        cboMinPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMinPeriodActionPerformed(evt);
            }
        });
        panMinPeriod.add(cboMinPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(panMinPeriod, gridBagConstraints);

        lblMaxPeriod.setText("Maximum Term of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblMaxPeriod, gridBagConstraints);

        panMaxPeriod.setLayout(new java.awt.GridBagLayout());

        txtMaxPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        panMaxPeriod.add(txtMaxPeriod, new java.awt.GridBagConstraints());

        cboMaxPeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboMaxPeriod.setMinimumSize(new java.awt.Dimension(88, 21));
        cboMaxPeriod.setPreferredSize(new java.awt.Dimension(88, 21));
        cboMaxPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMaxPeriodActionPerformed(evt);
            }
        });
        panMaxPeriod.add(cboMaxPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(panMaxPeriod, gridBagConstraints);

        lblFixedMargin.setText("Fixed Margin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblFixedMargin, gridBagConstraints);

        txtMinAmtLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(txtMinAmtLoan, gridBagConstraints);

        lblMinAmtLoan.setText("Minimum Amount of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblMinAmtLoan, gridBagConstraints);

        txtMaxAmtLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxAmtLoan.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxAmtLoanFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 21);
        panInterCalculation.add(txtMaxAmtLoan, gridBagConstraints);

        lblApplicableInter.setText("Applicable Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblApplicableInter, gridBagConstraints);

        panApplicableInter.setLayout(new java.awt.GridBagLayout());

        txtApplicableInter.setMinimumSize(new java.awt.Dimension(50, 21));
        txtApplicableInter.setPreferredSize(new java.awt.Dimension(50, 21));
        txtApplicableInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApplicableInterFocusLost(evt);
            }
        });
        panApplicableInter.add(txtApplicableInter, new java.awt.GridBagConstraints());

        lblApplicableInterPer.setText("%");
        panApplicableInter.add(lblApplicableInterPer, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(panApplicableInter, gridBagConstraints);

        lblMinInterDebited.setText("Minimum Interest to be Debited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblMinInterDebited, gridBagConstraints);

        panMinInterDebited.setLayout(new java.awt.GridBagLayout());

        txtMinInterDebited.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinInterDebited.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinInterDebited.add(txtMinInterDebited, new java.awt.GridBagConstraints());

        lblSunbsidy.setText("%");
        panMinInterDebited.add(lblSunbsidy, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(panMinInterDebited, gridBagConstraints);

        lblLoanPeriodMul.setText("Loan Periods in Multiples of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblLoanPeriodMul, gridBagConstraints);

        cboLoanPeriodMul.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboLoanPeriodMul.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(cboLoanPeriodMul, gridBagConstraints);

        panLoanAgainstDeposit.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit Products  to be linked"));
        panLoanAgainstDeposit.setMaximumSize(new java.awt.Dimension(550, 140));
        panLoanAgainstDeposit.setMinimumSize(new java.awt.Dimension(550, 140));
        panLoanAgainstDeposit.setPreferredSize(new java.awt.Dimension(580, 140));
        panLoanAgainstDeposit.setLayout(new java.awt.GridBagLayout());

        lblLoanPeriodMul1.setText("Selected Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAgainstDeposit.add(lblLoanPeriodMul1, gridBagConstraints);

        scrollPanList.setMaximumSize(new java.awt.Dimension(200, 100));
        scrollPanList.setMinimumSize(new java.awt.Dimension(200, 100));
        scrollPanList.setPreferredSize(new java.awt.Dimension(200, 100));

        lstAvailableDeposits.setMaximumSize(new java.awt.Dimension(200, 100));
        lstAvailableDeposits.setMinimumSize(new java.awt.Dimension(200, 100));
        lstAvailableDeposits.setPreferredSize(new java.awt.Dimension(200, 100));
        scrollPanList.setViewportView(lstAvailableDeposits);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panLoanAgainstDeposit.add(scrollPanList, gridBagConstraints);

        scrollPanAdd.setMaximumSize(new java.awt.Dimension(200, 100));
        scrollPanAdd.setMinimumSize(new java.awt.Dimension(200, 100));
        scrollPanAdd.setPreferredSize(new java.awt.Dimension(200, 100));

        lstSelectedDeposits.setMaximumSize(new java.awt.Dimension(200, 100));
        lstSelectedDeposits.setMinimumSize(new java.awt.Dimension(200, 100));
        lstSelectedDeposits.setPreferredSize(new java.awt.Dimension(200, 100));
        scrollPanAdd.setViewportView(lstSelectedDeposits);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panLoanAgainstDeposit.add(scrollPanAdd, gridBagConstraints);

        btnLTDRemove.setText("Remove");
        btnLTDRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLTDRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panLoanAgainstDeposit.add(btnLTDRemove, gridBagConstraints);

        btnLTDAdd.setText("Add");
        btnLTDAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLTDAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panLoanAgainstDeposit.add(btnLTDAdd, gridBagConstraints);

        lblLoanPeriodMul2.setText("Available Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanAgainstDeposit.add(lblLoanPeriodMul2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterCalculation.add(panLoanAgainstDeposit, gridBagConstraints);

        lblEligibleDepositAmtForLoan.setText("Eligible Deposit Amt For Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panInterCalculation.add(lblEligibleDepositAmtForLoan, gridBagConstraints);

        panBankSharePremium1.setLayout(new java.awt.GridBagLayout());

        txtElgLoanAmt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtElgLoanAmt.setPreferredSize(new java.awt.Dimension(50, 21));
        panBankSharePremium1.add(txtElgLoanAmt, new java.awt.GridBagConstraints());

        lblSunbsidy2.setText("%");
        panBankSharePremium1.add(lblSunbsidy2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 4);
        panInterCalculation.add(panBankSharePremium1, gridBagConstraints);

        lblReviewPeriodLoan.setText("Review Period Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panInterCalculation.add(lblReviewPeriodLoan, gridBagConstraints);

        panMinPeriod1.setLayout(new java.awt.GridBagLayout());

        txtReviewPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtReviewPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinPeriod1.add(txtReviewPeriod, new java.awt.GridBagConstraints());

        cboReviewPeriod.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboReviewPeriod.setMinimumSize(new java.awt.Dimension(88, 21));
        cboReviewPeriod.setPreferredSize(new java.awt.Dimension(88, 21));
        panMinPeriod1.add(cboReviewPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panInterCalculation.add(panMinPeriod1, gridBagConstraints);

        cboDepositRoundOff.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDepositRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 6, 4, 4);
        panInterCalculation.add(cboDepositRoundOff, gridBagConstraints);

        lblDepositRoundOff.setText("Eligible Deposit Rounde Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panInterCalculation.add(lblDepositRoundOff, gridBagConstraints);

        lblEligibleDepositAmtForLoanMaturing.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEligibleDepositAmtForLoanMaturing.setText(" Of Eligible Dep Amt For Loan Where Dep Maturing Within Next");
        lblEligibleDepositAmtForLoanMaturing.setMaximumSize(new java.awt.Dimension(290, 18));
        lblEligibleDepositAmtForLoanMaturing.setMinimumSize(new java.awt.Dimension(290, 18));
        lblEligibleDepositAmtForLoanMaturing.setPreferredSize(new java.awt.Dimension(290, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterCalculation.add(lblEligibleDepositAmtForLoanMaturing, gridBagConstraints);

        panMinInterDebited1.setMaximumSize(new java.awt.Dimension(55, 21));
        panMinInterDebited1.setMinimumSize(new java.awt.Dimension(55, 21));
        panMinInterDebited1.setPreferredSize(new java.awt.Dimension(55, 21));
        panMinInterDebited1.setLayout(new java.awt.GridBagLayout());

        txtDepAmtMaturing.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDepAmtMaturing.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinInterDebited1.add(txtDepAmtMaturing, new java.awt.GridBagConstraints());

        lblSunbsidy1.setText("%");
        panMinInterDebited1.add(lblSunbsidy1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(panMinInterDebited1, gridBagConstraints);

        panMinPeriod2.setLayout(new java.awt.GridBagLayout());

        txtDepAmtLoanMaturingPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDepAmtLoanMaturingPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinPeriod2.add(txtDepAmtLoanMaturingPeriod, new java.awt.GridBagConstraints());

        cboDepAmtLoanMaturing.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDepAmtLoanMaturing.setMinimumSize(new java.awt.Dimension(88, 21));
        cboDepAmtLoanMaturing.setPreferredSize(new java.awt.Dimension(88, 21));
        panMinPeriod2.add(cboDepAmtLoanMaturing, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panInterCalculation.add(panMinPeriod2, gridBagConstraints);

        chkFixed.setText("Fixed ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        panInterCalculation.add(chkFixed, gridBagConstraints);

        lblMaxAmtLoan.setText("Maximum Amount of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterCalculation.add(lblMaxAmtLoan, gridBagConstraints);

        txtFixedMargin.setAllowAll(true);
        txtFixedMargin.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFixedMargin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFixedMarginFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterCalculation.add(txtFixedMargin, gridBagConstraints);

        cLabel22.setText("Repayment Type");
        cLabel22.setName("lblRepaymentType"); // NOI18N
        panInterCalculation.add(cLabel22, new java.awt.GridBagConstraints());

        cboRepaymentType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRepaymentType.setName("cboRepaymentType"); // NOI18N
        cboRepaymentType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepaymentTypeActionPerformed(evt);
            }
        });
        panInterCalculation.add(cboRepaymentType, new java.awt.GridBagConstraints());

        cLabel23.setText("Repayment Frequency");
        cLabel23.setName("lblRepaymentFreq"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panInterCalculation.add(cLabel23, gridBagConstraints);

        cboRepaymentFreq.setName("cboRepaymentFreq"); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panInterCalculation.add(cboRepaymentFreq, gridBagConstraints);

        chkIntCalcFromSanctionDt.setText("Calc OverDue Int For Gold Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panInterCalculation.add(chkIntCalcFromSanctionDt, gridBagConstraints);

        cLabel25.setText("Grace Period if Due");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panInterCalculation.add(cLabel25, gridBagConstraints);

        txtGracePeriodForOverDueInt.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        panInterCalculation.add(txtGracePeriodForOverDueInt, gridBagConstraints);

        tabLoanProduct.addTab("Other Terms", panInterCalculation);

        panDocuments.setLayout(new java.awt.GridBagLayout());

        panDocumentFields.setMinimumSize(new java.awt.Dimension(244, 170));
        panDocumentFields.setPreferredSize(new java.awt.Dimension(244, 120));
        panDocumentFields.setLayout(new java.awt.GridBagLayout());

        lblDocumentType.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentFields.add(lblDocumentType, gridBagConstraints);

        cboDocumentType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboDocumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentFields.add(cboDocumentType, gridBagConstraints);

        lblDocumentNo.setText("Document No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentFields.add(lblDocumentNo, gridBagConstraints);

        lblDocumentDesc.setText("Document Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentFields.add(lblDocumentDesc, gridBagConstraints);

        txtDocumentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentFields.add(txtDocumentNo, gridBagConstraints);

        txtDocumentDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentFields.add(txtDocumentDesc, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnDocumentNew.setText("New");
        btnDocumentNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnDocumentNew, gridBagConstraints);

        btnDocumentSave.setText("Save");
        btnDocumentSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnDocumentSave, gridBagConstraints);

        btnDocumentDelete.setText("Delete");
        btnDocumentDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDocumentDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnDocumentDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        panDocumentFields.add(panButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 50, 5, 0);
        panDocuments.add(panDocumentFields, gridBagConstraints);

        panDocumentTable.setMaximumSize(new java.awt.Dimension(340, 120));
        panDocumentTable.setMinimumSize(new java.awt.Dimension(340, 120));
        panDocumentTable.setPreferredSize(new java.awt.Dimension(340, 120));
        panDocumentTable.setLayout(new java.awt.GridBagLayout());

        srpDocuments.setMinimumSize(new java.awt.Dimension(250, 114));
        srpDocuments.setPreferredSize(new java.awt.Dimension(250, 114));

        tblDocuments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "SI No", "Document Type", "Document No"
            }
        ));
        tblDocuments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDocumentsMousePressed(evt);
            }
        });
        srpDocuments.setViewportView(tblDocuments);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panDocumentTable.add(srpDocuments, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 12, 0, 61);
        panDocuments.add(panDocumentTable, gridBagConstraints);

        panAppropriateTransaction.setBorder(javax.swing.BorderFactory.createTitledBorder("Appropriation Of Repayment"));
        panAppropriateTransaction.setMaximumSize(new java.awt.Dimension(660, 160));
        panAppropriateTransaction.setMinimumSize(new java.awt.Dimension(660, 160));
        panAppropriateTransaction.setPreferredSize(new java.awt.Dimension(660, 200));
        panAppropriateTransaction.setLayout(new java.awt.GridBagLayout());

        lblSelectedTransaction.setText("Selected Hierarchy of Adjustment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppropriateTransaction.add(lblSelectedTransaction, gridBagConstraints);

        scrollPanList1.setMaximumSize(new java.awt.Dimension(200, 100));
        scrollPanList1.setMinimumSize(new java.awt.Dimension(200, 100));
        scrollPanList1.setPreferredSize(new java.awt.Dimension(200, 100));

        lstAvailableTypeTransaction.setMaximumSize(new java.awt.Dimension(200, 100));
        lstAvailableTypeTransaction.setMinimumSize(new java.awt.Dimension(200, 100));
        lstAvailableTypeTransaction.setPreferredSize(new java.awt.Dimension(200, 100));
        scrollPanList1.setViewportView(lstAvailableTypeTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAppropriateTransaction.add(scrollPanList1, gridBagConstraints);

        scrollPanAdd1.setMaximumSize(new java.awt.Dimension(200, 100));
        scrollPanAdd1.setMinimumSize(new java.awt.Dimension(200, 100));
        scrollPanAdd1.setPreferredSize(new java.awt.Dimension(200, 100));

        lstSelectedPriorityTransaction.setMaximumSize(new java.awt.Dimension(200, 100));
        lstSelectedPriorityTransaction.setMinimumSize(new java.awt.Dimension(200, 100));
        lstSelectedPriorityTransaction.setPreferredSize(new java.awt.Dimension(200, 100));
        scrollPanAdd1.setViewportView(lstSelectedPriorityTransaction);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAppropriateTransaction.add(scrollPanAdd1, gridBagConstraints);

        btnTransactionRemove.setText("Remove");
        btnTransactionRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionRemoveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAppropriateTransaction.add(btnTransactionRemove, gridBagConstraints);

        btnTransactionAdd.setText("Add");
        btnTransactionAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(3, 5, 3, 5);
        panAppropriateTransaction.add(btnTransactionAdd, gridBagConstraints);

        lblAvailableTransaction.setText("Hierarchy of  Adjustment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppropriateTransaction.add(lblAvailableTransaction, gridBagConstraints);

        panCreditPenalFirst1.setMaximumSize(new java.awt.Dimension(424, 27));
        panCreditPenalFirst1.setMinimumSize(new java.awt.Dimension(624, 27));
        panCreditPenalFirst1.setPreferredSize(new java.awt.Dimension(624, 27));
        panCreditPenalFirst1.setLayout(new java.awt.GridBagLayout());

        lblWhetherCredit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblWhetherCredit.setText("Whether credit principle first");
        lblWhetherCredit.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        lblWhetherCredit.setMaximumSize(new java.awt.Dimension(224, 21));
        lblWhetherCredit.setMinimumSize(new java.awt.Dimension(224, 21));
        lblWhetherCredit.setPreferredSize(new java.awt.Dimension(224, 21));
        panCreditPenalFirst1.add(lblWhetherCredit, new java.awt.GridBagConstraints());

        rdoProcessCharges.add(rdoIsCreditAllowed_Yes);
        rdoIsCreditAllowed_Yes.setText("Yes");
        rdoIsCreditAllowed_Yes.setMaximumSize(new java.awt.Dimension(50, 21));
        rdoIsCreditAllowed_Yes.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoIsCreditAllowed_Yes.setPreferredSize(new java.awt.Dimension(50, 21));
        rdoIsCreditAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsCreditAllowed_YesActionPerformed(evt);
            }
        });
        panCreditPenalFirst1.add(rdoIsCreditAllowed_Yes, new java.awt.GridBagConstraints());

        rdoProcessCharges.add(rdoIsCreditAllowed_No);
        rdoIsCreditAllowed_No.setText("No");
        rdoIsCreditAllowed_No.setMaximumSize(new java.awt.Dimension(50, 21));
        rdoIsCreditAllowed_No.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoIsCreditAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsCreditAllowed_NoActionPerformed(evt);
            }
        });
        panCreditPenalFirst1.add(rdoIsCreditAllowed_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAppropriateTransaction.add(panCreditPenalFirst1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 50, 0, 0);
        panDocuments.add(panAppropriateTransaction, gridBagConstraints);

        panAppropriateRepaymentOTS.setBorder(javax.swing.BorderFactory.createTitledBorder("Appropriation of  Repayment  for OTS"));
        panAppropriateRepaymentOTS.setMaximumSize(new java.awt.Dimension(560, 160));
        panAppropriateRepaymentOTS.setMinimumSize(new java.awt.Dimension(660, 160));
        panAppropriateRepaymentOTS.setPreferredSize(new java.awt.Dimension(660, 160));
        panAppropriateRepaymentOTS.setLayout(new java.awt.GridBagLayout());

        lblSelectedTransaction1.setText("Selected Hierarchy of Adjustment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppropriateRepaymentOTS.add(lblSelectedTransaction1, gridBagConstraints);

        scrollPanList2.setMaximumSize(new java.awt.Dimension(200, 100));
        scrollPanList2.setMinimumSize(new java.awt.Dimension(200, 100));
        scrollPanList2.setPreferredSize(new java.awt.Dimension(200, 100));

        lstAvailableTypeTransaction_OTS.setMaximumSize(new java.awt.Dimension(200, 100));
        lstAvailableTypeTransaction_OTS.setMinimumSize(new java.awt.Dimension(200, 100));
        lstAvailableTypeTransaction_OTS.setPreferredSize(new java.awt.Dimension(200, 100));
        scrollPanList2.setViewportView(lstAvailableTypeTransaction_OTS);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAppropriateRepaymentOTS.add(scrollPanList2, gridBagConstraints);

        scrollPanAdd2.setMaximumSize(new java.awt.Dimension(200, 100));
        scrollPanAdd2.setMinimumSize(new java.awt.Dimension(200, 100));
        scrollPanAdd2.setPreferredSize(new java.awt.Dimension(200, 100));

        lstSelectedPriorityTransaction_OTS.setMaximumSize(new java.awt.Dimension(200, 100));
        lstSelectedPriorityTransaction_OTS.setMinimumSize(new java.awt.Dimension(200, 100));
        lstSelectedPriorityTransaction_OTS.setPreferredSize(new java.awt.Dimension(200, 100));
        scrollPanAdd2.setViewportView(lstSelectedPriorityTransaction_OTS);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAppropriateRepaymentOTS.add(scrollPanAdd2, gridBagConstraints);

        btnTransactionRemove_OTS.setText("Remove");
        btnTransactionRemove_OTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionRemove_OTSActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panAppropriateRepaymentOTS.add(btnTransactionRemove_OTS, gridBagConstraints);

        btnTransactionAdd_OTS.setText("Add");
        btnTransactionAdd_OTS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransactionAdd_OTSActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panAppropriateRepaymentOTS.add(btnTransactionAdd_OTS, gridBagConstraints);

        lblAvailableTransaction1.setText("Hierarchy of  Adjustment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAppropriateRepaymentOTS.add(lblAvailableTransaction1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 50, 4, 0);
        panDocuments.add(panAppropriateRepaymentOTS, gridBagConstraints);

        tabLoanProduct.addTab("Documents/Hierarchy Of Repayment", panDocuments);

        panClassDetails_Details.setLayout(new java.awt.GridBagLayout());

        panCode.setLayout(new java.awt.GridBagLayout());

        lblCommodityCode.setText("Commodity Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblCommodityCode, gridBagConstraints);

        cboCommodityCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCommodityCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboCommodityCode, gridBagConstraints);

        lblGuaranteeCoverCode.setText("Guarantee Cover Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblGuaranteeCoverCode, gridBagConstraints);

        cboGuaranteeCoverCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboGuaranteeCoverCode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboGuaranteeCoverCode.setPopupWidth(115);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboGuaranteeCoverCode, gridBagConstraints);

        lblSectorCode.setText("Sector Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblSectorCode, gridBagConstraints);

        cboSectorCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSectorCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboSectorCode, gridBagConstraints);

        lblHealthCode.setText("Health Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblHealthCode, gridBagConstraints);

        cboHealthCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboHealthCode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboHealthCode.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboHealthCode, gridBagConstraints);

        lblTypeFacility.setText("Type of Facility");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblTypeFacility, gridBagConstraints);

        cboTypeFacility.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboTypeFacility.setMinimumSize(new java.awt.Dimension(100, 21));
        cboTypeFacility.setPopupWidth(300);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboTypeFacility, gridBagConstraints);

        lblPurposeCode.setText("Purpose Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblPurposeCode, gridBagConstraints);

        cboPurposeCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPurposeCode.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPurposeCode.setPopupWidth(210);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboPurposeCode, gridBagConstraints);

        lblIndusCode.setText("Industry Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(lblIndusCode, gridBagConstraints);

        cboIndusCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboIndusCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode.add(cboIndusCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails_Details.add(panCode, gridBagConstraints);

        sptClassification_vertical.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails_Details.add(sptClassification_vertical, gridBagConstraints);

        panCode2.setLayout(new java.awt.GridBagLayout());

        lbl20Code.setText("20 Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lbl20Code, gridBagConstraints);

        lblRefinancingInsti.setText("Refinancing Institution");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblRefinancingInsti, gridBagConstraints);

        cboRefinancingInsti.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboRefinancingInsti.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(cboRefinancingInsti, gridBagConstraints);

        lblGovtSchemeCode.setText("Govt. Scheme Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblGovtSchemeCode, gridBagConstraints);

        cboGovtSchemeCode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboGovtSchemeCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(cboGovtSchemeCode, gridBagConstraints);

        lblDirectFinance.setText("Direct Finance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblDirectFinance, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCode2.add(chkDirectFinance, gridBagConstraints);

        lblECGC.setText("ECGC");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblECGC, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCode2.add(chkECGC, gridBagConstraints);

        lblQIS.setText("QIS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblQIS, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panCode2.add(chkQIS, gridBagConstraints);

        cbo20Code.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cbo20Code.setMinimumSize(new java.awt.Dimension(100, 21));
        cbo20Code.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(cbo20Code, gridBagConstraints);

        lblSecurityDeails.setText("Security Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(lblSecurityDeails, gridBagConstraints);

        cboSecurityDeails.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCode2.add(cboSecurityDeails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails_Details.add(panCode2, gridBagConstraints);

        tabLoanProduct.addTab("Classification", panClassDetails_Details);

        panSpecial_NonPerfoormingAssets1.setLayout(new java.awt.GridBagLayout());

        panSpeItems1.setBorder(javax.swing.BorderFactory.createTitledBorder("Subsidy"));
        panSpeItems1.setMinimumSize(new java.awt.Dimension(555, 159));
        panSpeItems1.setPreferredSize(new java.awt.Dimension(555, 159));
        panSpeItems1.setLayout(new java.awt.GridBagLayout());

        lblSubsidy.setText("Subsidy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 19, 2, 4);
        panSpeItems1.add(lblSubsidy, gridBagConstraints);

        rdoSubsidy.add(rdoSubsidy_Yes);
        rdoSubsidy_Yes.setText("Yes");
        rdoSubsidy_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSubsidy_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 2, 4);
        panSpeItems1.add(rdoSubsidy_Yes, gridBagConstraints);

        rdoATMcardIssued.add(rdoSubsidy_No);
        rdoSubsidy_No.setText("No");
        rdoSubsidy_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSubsidy_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panSpeItems1.add(rdoSubsidy_No, gridBagConstraints);

        lblSubsidyInterestCalculatedOn.setText("Interest to be calculated on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 7, 2, 4);
        panSpeItems1.add(lblSubsidyInterestCalculatedOn, gridBagConstraints);

        panSubsidyInterestCalculate.setMinimumSize(new java.awt.Dimension(320, 21));
        panSubsidyInterestCalculate.setPreferredSize(new java.awt.Dimension(320, 21));
        panSubsidyInterestCalculate.setLayout(new java.awt.GridBagLayout());

        rdosubsidyInterestCalculateGroup.add(rdoLoanBalance_Yes);
        rdoLoanBalance_Yes.setText("Loan Balance");
        rdoLoanBalance_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLoanBalance_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSubsidyInterestCalculate.add(rdoLoanBalance_Yes, gridBagConstraints);

        rdosubsidyInterestCalculateGroup.add(rdoSubsidyAdjustLoanBalance_Yes);
        rdoSubsidyAdjustLoanBalance_Yes.setText("Subsidy adjusted loan balance");
        rdoSubsidyAdjustLoanBalance_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSubsidyAdjustLoanBalance_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSubsidyInterestCalculate.add(rdoSubsidyAdjustLoanBalance_Yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panSpeItems1.add(panSubsidyInterestCalculate, gridBagConstraints);

        panSubsidyReceived.setMinimumSize(new java.awt.Dimension(360, 21));
        panSubsidyReceived.setPreferredSize(new java.awt.Dimension(360, 21));
        panSubsidyReceived.setLayout(new java.awt.GridBagLayout());

        rdoFromSubsidyReceivedDateGroup.add(rdoSubsidyReceivedDate);
        rdoSubsidyReceivedDate.setText("From Subsidy received date");
        rdoSubsidyReceivedDate.setMaximumSize(new java.awt.Dimension(240, 27));
        rdoSubsidyReceivedDate.setMinimumSize(new java.awt.Dimension(240, 27));
        rdoSubsidyReceivedDate.setPreferredSize(new java.awt.Dimension(237, 27));
        rdoSubsidyReceivedDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSubsidyReceivedDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSubsidyReceived.add(rdoSubsidyReceivedDate, gridBagConstraints);

        rdoFromSubsidyReceivedDateGroup.add(rdoLoanOpenDate);
        rdoLoanOpenDate.setText("From Loan open date");
        rdoLoanOpenDate.setMaximumSize(new java.awt.Dimension(199, 27));
        rdoLoanOpenDate.setMinimumSize(new java.awt.Dimension(199, 27));
        rdoLoanOpenDate.setPreferredSize(new java.awt.Dimension(199, 27));
        rdoLoanOpenDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLoanOpenDateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panSubsidyReceived.add(rdoLoanOpenDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panSpeItems1.add(panSubsidyReceived, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 14, 0, 0);
        panSpecial_NonPerfoormingAssets1.add(panSpeItems1, gridBagConstraints);

        panWaivedOff.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest WaivedOff"));
        panWaivedOff.setMinimumSize(new java.awt.Dimension(375, 405));
        panWaivedOff.setPreferredSize(new java.awt.Dimension(375, 450));
        panWaivedOff.setLayout(new java.awt.GridBagLayout());

        lblPenalInterestWaiverAllowed.setText("Penal Interest Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(lblPenalInterestWaiverAllowed, gridBagConstraints);

        rdoPenalInterestWaiverGroup.add(rdoPenalInterestWaiverAllowed_Yes);
        rdoPenalInterestWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoPenalInterestWaiverAllowed_Yes, gridBagConstraints);

        rdoPenalInterestWaiverGroup.add(rdoPenalInterestWaiverAllowed_No);
        rdoPenalInterestWaiverAllowed_No.setText("No");
        rdoPenalInterestWaiverAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalInterestWaiverAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoPenalInterestWaiverAllowed_No, gridBagConstraints);

        lblInterestWaiverAllowed.setText("Interest Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panWaivedOff.add(lblInterestWaiverAllowed, gridBagConstraints);

        rdoInterestWaiverGroup.add(rdoInterestWaiverAllowed_Yes);
        rdoInterestWaiverAllowed_Yes.setText("Yes");
        rdoInterestWaiverAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestWaiverAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoInterestWaiverAllowed_Yes, gridBagConstraints);

        rdoInterestWaiverGroup.add(rdoInterestWaiverAllowed_No);
        rdoInterestWaiverAllowed_No.setText("No");
        rdoInterestWaiverAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestWaiverAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoInterestWaiverAllowed_No, gridBagConstraints);

        lblPrincipalWaiverAllowed.setText("Principal Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(lblPrincipalWaiverAllowed, gridBagConstraints);

        rdoInterestWaiverGroup.add(rdoPrincipalWaiverAllowed_Yes);
        rdoPrincipalWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoPrincipalWaiverAllowed_Yes, gridBagConstraints);

        rdoInterestWaiverGroup.add(rdoPrincipalWaiverAllowed_No);
        rdoPrincipalWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoPrincipalWaiverAllowed_No, gridBagConstraints);

        lblNoticeWaiverAllowed.setText("Notice Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(lblNoticeWaiverAllowed, gridBagConstraints);

        rdoInterestWaiverGroup.add(rdoNoticeWaiverAllowed_Yes);
        rdoNoticeWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoNoticeWaiverAllowed_Yes, gridBagConstraints);

        rdoInterestWaiverGroup.add(rdoNoticeWaiverAllowed_No);
        rdoNoticeWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panWaivedOff.add(rdoNoticeWaiverAllowed_No, gridBagConstraints);

        cLabel4.setText("Arbitrary Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panWaivedOff.add(cLabel4, gridBagConstraints);

        cLabel5.setText("Postage Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        panWaivedOff.add(cLabel5, gridBagConstraints);

        cLabel6.setText("Advertise Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panWaivedOff.add(cLabel6, gridBagConstraints);

        cLabel7.setText("Miscellaneous Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        panWaivedOff.add(cLabel7, gridBagConstraints);

        cLabel8.setText("Decree Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        panWaivedOff.add(cLabel8, gridBagConstraints);

        cLabel9.setText("Ep Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        panWaivedOff.add(cLabel9, gridBagConstraints);

        cLabel10.setText("Arc Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        panWaivedOff.add(cLabel10, gridBagConstraints);

        cLabel11.setText("Insurence Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        panWaivedOff.add(cLabel11, gridBagConstraints);

        cLabel12.setText("Legal Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        panWaivedOff.add(cLabel12, gridBagConstraints);

        rdoArbitaryWaiverGroup.add(rdoArbitaryWaiverAllowed_Yes);
        rdoArbitaryWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        panWaivedOff.add(rdoArbitaryWaiverAllowed_Yes, gridBagConstraints);

        rdoArbitaryWaiverGroup.add(rdoArbitaryWaiverAllowed_No);
        rdoArbitaryWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panWaivedOff.add(rdoArbitaryWaiverAllowed_No, gridBagConstraints);

        PostageWaiverGroup.add(rdoPostageWaiverAllowed_Yes);
        rdoPostageWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        panWaivedOff.add(rdoPostageWaiverAllowed_Yes, gridBagConstraints);

        PostageWaiverGroup.add(rdoPostageWaiverAllowed_No);
        rdoPostageWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panWaivedOff.add(rdoPostageWaiverAllowed_No, gridBagConstraints);

        AdvertiseWaiverGroup.add(rdoAdvertiseWaiverAllowed_Yes);
        rdoAdvertiseWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        panWaivedOff.add(rdoAdvertiseWaiverAllowed_Yes, gridBagConstraints);

        AdvertiseWaiverGroup.add(rdoAdvertiseWaiverAllowed_No);
        rdoAdvertiseWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        panWaivedOff.add(rdoAdvertiseWaiverAllowed_No, gridBagConstraints);

        Miscellaneouswaivergroup.add(rdoMiscellaneousWaiverAllowed_Yes);
        rdoMiscellaneousWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        panWaivedOff.add(rdoMiscellaneousWaiverAllowed_Yes, gridBagConstraints);

        Miscellaneouswaivergroup.add(rdoMiscellaneousWaiverAllowed_No);
        rdoMiscellaneousWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        panWaivedOff.add(rdoMiscellaneousWaiverAllowed_No, gridBagConstraints);

        DecreeWaiverGroup.add(rdoDecreeWaiverAllowed_Yes);
        rdoDecreeWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        panWaivedOff.add(rdoDecreeWaiverAllowed_Yes, gridBagConstraints);

        DecreeWaiverGroup.add(rdoDecreeWaiverAllowed_No);
        rdoDecreeWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        panWaivedOff.add(rdoDecreeWaiverAllowed_No, gridBagConstraints);

        EpWaiverGroup.add(rdoEpWaiverAllowed_Yes);
        rdoEpWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        panWaivedOff.add(rdoEpWaiverAllowed_Yes, gridBagConstraints);

        EpWaiverGroup.add(rdoEpWaiverAllowed_No);
        rdoEpWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        panWaivedOff.add(rdoEpWaiverAllowed_No, gridBagConstraints);

        ArcWaiverGroup.add(rdoArcWaiverAllowed_Yes);
        rdoArcWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        panWaivedOff.add(rdoArcWaiverAllowed_Yes, gridBagConstraints);

        ArcWaiverGroup.add(rdoArcWaiverAllowed_No);
        rdoArcWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        panWaivedOff.add(rdoArcWaiverAllowed_No, gridBagConstraints);

        InsurenceWaiverGroup.add(rdoInsurenceWaiverAllowed_Yes);
        rdoInsurenceWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        panWaivedOff.add(rdoInsurenceWaiverAllowed_Yes, gridBagConstraints);

        InsurenceWaiverGroup.add(rdoInsurenceWaiverAllowed_No);
        rdoInsurenceWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        panWaivedOff.add(rdoInsurenceWaiverAllowed_No, gridBagConstraints);

        LegalWaiverGroup.add(rdoLegalWaiverAllowed_Yes);
        rdoLegalWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        panWaivedOff.add(rdoLegalWaiverAllowed_Yes, gridBagConstraints);

        rdoLegalWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        panWaivedOff.add(rdoLegalWaiverAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = -105;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 104, 0, 0);
        panSpecial_NonPerfoormingAssets1.add(panWaivedOff, gridBagConstraints);

        panInterestRebate.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Rebate"));
        panInterestRebate.setMinimumSize(new java.awt.Dimension(400, 159));
        panInterestRebate.setPreferredSize(new java.awt.Dimension(400, 159));
        panInterestRebate.setLayout(new java.awt.GridBagLayout());

        lblInterestRebateAllowed.setText("Interest Rebate Allowed  ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInterestRebate.add(lblInterestRebateAllowed, gridBagConstraints);

        rdoInterestRebateAllowedGroup.add(rdoInterestRebateAllowed_Yes);
        rdoInterestRebateAllowed_Yes.setText("Yes");
        rdoInterestRebateAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestRebateAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInterestRebate.add(rdoInterestRebateAllowed_Yes, gridBagConstraints);

        rdoInterestRebateAllowedGroup.add(rdoInterestRebateAllowed_No);
        rdoInterestRebateAllowed_No.setText("No");
        rdoInterestRebateAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestRebateAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panInterestRebate.add(rdoInterestRebateAllowed_No, gridBagConstraints);

        lblInterestRebatePercentage.setText("Interest Rebate Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInterestRebate.add(lblInterestRebatePercentage, gridBagConstraints);

        lblInterestRebateCalculation.setText("Interest Rebate Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInterestRebate.add(lblInterestRebateCalculation, gridBagConstraints);

        lblRebatePeriod.setText("Rebate Period ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInterestRebate.add(lblRebatePeriod, gridBagConstraints);

        txtInterestRebatePercentage.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        panInterestRebate.add(txtInterestRebatePercentage, gridBagConstraints);

        cboInterestRebateCalculation.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        panInterestRebate.add(cboInterestRebateCalculation, gridBagConstraints);

        cboRebatePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRebatePeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRebatePeriodActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        panInterestRebate.add(cboRebatePeriod, gridBagConstraints);

        lblFinancialYearstartingfrom.setText("Financial Year starting from");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInterestRebate.add(lblFinancialYearstartingfrom, gridBagConstraints);

        cPanel3.setMinimumSize(new java.awt.Dimension(120, 21));
        cPanel3.setPreferredSize(new java.awt.Dimension(120, 21));
        cPanel3.setLayout(new java.awt.GridBagLayout());

        txtFinYearStartingFromDD.setMinimumSize(new java.awt.Dimension(25, 21));
        txtFinYearStartingFromDD.setPreferredSize(new java.awt.Dimension(25, 21));
        cPanel3.add(txtFinYearStartingFromDD, new java.awt.GridBagConstraints());

        cLabel1.setText("MM");
        cLabel1.setMaximumSize(new java.awt.Dimension(25, 21));
        cLabel1.setMinimumSize(new java.awt.Dimension(25, 21));
        cLabel1.setPreferredSize(new java.awt.Dimension(25, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        cPanel3.add(cLabel1, gridBagConstraints);

        txtFinYearStartingFromMM.setMinimumSize(new java.awt.Dimension(25, 21));
        txtFinYearStartingFromMM.setPreferredSize(new java.awt.Dimension(25, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        cPanel3.add(txtFinYearStartingFromMM, gridBagConstraints);

        cLabel2.setText("DD");
        cLabel2.setMaximumSize(new java.awt.Dimension(25, 21));
        cLabel2.setMinimumSize(new java.awt.Dimension(25, 21));
        cLabel2.setPreferredSize(new java.awt.Dimension(25, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        cPanel3.add(cLabel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        panInterestRebate.add(cPanel3, gridBagConstraints);

        lblRebateSpl.setText("Based on Loan Int.Rate & Enter percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panInterestRebate.add(lblRebateSpl, gridBagConstraints);

        chkRebtSpl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRebtSplActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panInterestRebate.add(chkRebtSpl, gridBagConstraints);

        txtRebateLoanIntPercent.setToolTipText("Loan Interest Percentage for Rebate Calculation");
        txtRebateLoanIntPercent.setAllowNumber(true);
        txtRebateLoanIntPercent.setMinimumSize(new java.awt.Dimension(60, 21));
        txtRebateLoanIntPercent.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panInterestRebate.add(txtRebateLoanIntPercent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 4, 0, 10);
        panSpecial_NonPerfoormingAssets1.add(panInterestRebate, gridBagConstraints);
        panInterestRebate.getAccessibleContext().setAccessibleName("Interest Rebeat");

        PanWaiveHead.setBorder(javax.swing.BorderFactory.createTitledBorder("WaiveHead"));
        PanWaiveHead.setMinimumSize(new java.awt.Dimension(400, 400));
        PanWaiveHead.setPreferredSize(new java.awt.Dimension(400, 400));
        PanWaiveHead.setLayout(new java.awt.GridBagLayout());

        cLabel13.setText("Arbitary Waive Head");
        PanWaiveHead.add(cLabel13, new java.awt.GridBagConstraints());

        txtArbitaryWaiveHead.setMinimumSize(new java.awt.Dimension(100, 21));
        PanWaiveHead.add(txtArbitaryWaiveHead, new java.awt.GridBagConstraints());

        btnArbitaryHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnArbitaryHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnArbitaryHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnArbitaryHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArbitaryHeadActionPerformed(evt);
            }
        });
        PanWaiveHead.add(btnArbitaryHead, new java.awt.GridBagConstraints());

        cLabel14.setText("Postage Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        PanWaiveHead.add(cLabel14, gridBagConstraints);

        txtPostageWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        PanWaiveHead.add(txtPostageWaiverHead, gridBagConstraints);

        btnPostageWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostageWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnPostageWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnPostageWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnPostageWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostageWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        PanWaiveHead.add(btnPostageWaiveHead, gridBagConstraints);

        cLabel15.setText("Advertise Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        PanWaiveHead.add(cLabel15, gridBagConstraints);

        txtAdvertiseWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        PanWaiveHead.add(txtAdvertiseWaiverHead, gridBagConstraints);

        btnAdvertiseWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAdvertiseWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnAdvertiseWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnAdvertiseWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnAdvertiseWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvertiseWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        PanWaiveHead.add(btnAdvertiseWaiveHead, gridBagConstraints);

        cLabel16.setText("Miscellaneous Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        PanWaiveHead.add(cLabel16, gridBagConstraints);

        cLabel17.setText("Decree Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        PanWaiveHead.add(cLabel17, gridBagConstraints);

        cLabel18.setText("Ep Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        PanWaiveHead.add(cLabel18, gridBagConstraints);

        cLabel19.setText("Arc Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        PanWaiveHead.add(cLabel19, gridBagConstraints);

        cLabel20.setText("Insurence Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        PanWaiveHead.add(cLabel20, gridBagConstraints);

        cLabel21.setText("Leagal Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        PanWaiveHead.add(cLabel21, gridBagConstraints);

        txtMiscellaneousWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        PanWaiveHead.add(txtMiscellaneousWaiverHead, gridBagConstraints);

        btnMiscellaneousWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMiscellaneousWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMiscellaneousWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMiscellaneousWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMiscellaneousWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiscellaneousWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        PanWaiveHead.add(btnMiscellaneousWaiveHead, gridBagConstraints);

        txtDecreeWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        PanWaiveHead.add(txtDecreeWaiverHead, gridBagConstraints);

        btnDecreeWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDecreeWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnDecreeWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnDecreeWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDecreeWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecreeWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        PanWaiveHead.add(btnDecreeWaiveHead, gridBagConstraints);

        txtEpWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        PanWaiveHead.add(txtEpWaiverHead, gridBagConstraints);

        btnEpWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEpWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnEpWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnEpWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnEpWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEpWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        PanWaiveHead.add(btnEpWaiveHead, gridBagConstraints);

        txtArcWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        PanWaiveHead.add(txtArcWaiverHead, gridBagConstraints);

        btnArcWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnArcWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnArcWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnArcWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnArcWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArcWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        PanWaiveHead.add(btnArcWaiveHead, gridBagConstraints);

        txtInsurenceWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        PanWaiveHead.add(txtInsurenceWaiverHead, gridBagConstraints);

        btnInsurenceWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInsurenceWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnInsurenceWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnInsurenceWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnInsurenceWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsurenceWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        PanWaiveHead.add(btnInsurenceWaiveHead, gridBagConstraints);

        txtLegalWaiverHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        PanWaiveHead.add(txtLegalWaiverHead, gridBagConstraints);

        btnLeagalWaiveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLeagalWaiveHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnLeagalWaiveHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnLeagalWaiveHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnLeagalWaiveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLeagalWaiveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        PanWaiveHead.add(btnLeagalWaiveHead, gridBagConstraints);

        cLabel26.setText("Overdue Interest Waive Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        PanWaiveHead.add(cLabel26, gridBagConstraints);

        txtOverDueWaiverHead.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        PanWaiveHead.add(txtOverDueWaiverHead, gridBagConstraints);

        btnOverDueWaiverHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOverDueWaiverHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnOverDueWaiverHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnOverDueWaiverHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnOverDueWaiverHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverDueWaiverHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        PanWaiveHead.add(btnOverDueWaiverHead, gridBagConstraints);

        cLabel28.setText("Overdue Interest Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        PanWaiveHead.add(cLabel28, gridBagConstraints);

        rdoOverDueWaiverAllowed_Yes.setText("Yes");
        rdoOverDueWaiverAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOverDueWaiverAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        PanWaiveHead.add(rdoOverDueWaiverAllowed_Yes, gridBagConstraints);

        rdoOverDueWaiverAllowed_No.setText("No");
        rdoOverDueWaiverAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOverDueWaiverAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        PanWaiveHead.add(rdoOverDueWaiverAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 47, 10);
        panSpecial_NonPerfoormingAssets1.add(PanWaiveHead, gridBagConstraints);

        tabLoanProduct.addTab("Subsidy/ Interest Rebeat/WaiveOff", panSpecial_NonPerfoormingAssets1);

        panAccountHead.setPreferredSize(new java.awt.Dimension(844, 628));
        panAccountHead.setLayout(new java.awt.GridBagLayout());

        panChargeTypes.setMinimumSize(new java.awt.Dimension(570, 130));
        panChargeTypes.setPreferredSize(new java.awt.Dimension(570, 130));
        panChargeTypes.setLayout(new java.awt.GridBagLayout());

        lblLegalCharges.setText("Legal Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblLegalCharges, gridBagConstraints);

        txtLegalCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLegalCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLegalChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        panChargeTypes.add(txtLegalCharges, gridBagConstraints);

        btnLegalCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLegalCharges.setToolTipText("Search");
        btnLegalCharges.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLegalCharges.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLegalCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLegalCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLegalChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        panChargeTypes.add(btnLegalCharges, gridBagConstraints);

        lblPostageCharges.setText("Postage Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblPostageCharges, gridBagConstraints);

        txtPostageCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPostageCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostageChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panChargeTypes.add(txtPostageCharges, gridBagConstraints);

        btnPostageCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostageCharges.setToolTipText("Search");
        btnPostageCharges.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPostageCharges.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPostageCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPostageCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostageChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panChargeTypes.add(btnPostageCharges, gridBagConstraints);

        lblMiscServCharges.setText("Miscellaneous Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblMiscServCharges, gridBagConstraints);

        txtMiscServCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMiscServCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMiscServChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panChargeTypes.add(txtMiscServCharges, gridBagConstraints);

        btnMiscServCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMiscServCharges.setToolTipText("Search");
        btnMiscServCharges.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMiscServCharges.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMiscServCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMiscServCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiscServChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        panChargeTypes.add(btnMiscServCharges, gridBagConstraints);

        lblArbitraryCharges.setText("Arbitrary Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblArbitraryCharges, gridBagConstraints);

        txtArbitraryCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtArbitraryCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtArbitraryChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panChargeTypes.add(txtArbitraryCharges, gridBagConstraints);

        btnArbitraryCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnArbitraryCharges.setToolTipText("Search");
        btnArbitraryCharges.setMaximumSize(new java.awt.Dimension(21, 21));
        btnArbitraryCharges.setMinimumSize(new java.awt.Dimension(21, 21));
        btnArbitraryCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnArbitraryCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArbitraryChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        panChargeTypes.add(btnArbitraryCharges, gridBagConstraints);

        lblInsuranceCharges.setText("Insurance Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblInsuranceCharges, gridBagConstraints);

        txtInsuranceCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInsuranceCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInsuranceChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panChargeTypes.add(txtInsuranceCharges, gridBagConstraints);

        btnInsuranceCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInsuranceCharges.setToolTipText("Search");
        btnInsuranceCharges.setMaximumSize(new java.awt.Dimension(21, 21));
        btnInsuranceCharges.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInsuranceCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInsuranceCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsuranceChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panChargeTypes.add(btnInsuranceCharges, gridBagConstraints);

        lblExecutionDecreeCharges.setText("Execution Decree Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblExecutionDecreeCharges, gridBagConstraints);

        txtExecutionDecreeCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExecutionDecreeCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExecutionDecreeChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panChargeTypes.add(txtExecutionDecreeCharges, gridBagConstraints);

        btnExecutionDecreeCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExecutionDecreeCharges.setToolTipText("Search");
        btnExecutionDecreeCharges.setMaximumSize(new java.awt.Dimension(21, 21));
        btnExecutionDecreeCharges.setMinimumSize(new java.awt.Dimension(21, 21));
        btnExecutionDecreeCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExecutionDecreeCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecutionDecreeChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        panChargeTypes.add(btnExecutionDecreeCharges, gridBagConstraints);

        lblPenalWaiveoffHead.setText("Penal Waiveoff HD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblPenalWaiveoffHead, gridBagConstraints);

        txtPenalWaiveoffHead.setAllowAll(true);
        txtPenalWaiveoffHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalWaiveoffHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalWaiveoffHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        panChargeTypes.add(txtPenalWaiveoffHead, gridBagConstraints);

        btnPenalWaiveoffHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPenalWaiveoffHead.setToolTipText("Search");
        btnPenalWaiveoffHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPenalWaiveoffHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPenalWaiveoffHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPenalWaiveoffHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenalWaiveoffHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        panChargeTypes.add(btnPenalWaiveoffHead, gridBagConstraints);

        lblPrincipleWaiveoffHead.setText("Principle Waiveoff HD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblPrincipleWaiveoffHead, gridBagConstraints);

        txtPrincipleWaiveoffHead.setAllowAll(true);
        txtPrincipleWaiveoffHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPrincipleWaiveoffHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrincipleWaiveoffHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        panChargeTypes.add(txtPrincipleWaiveoffHead, gridBagConstraints);

        btnPrincipleWaiveoffHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPrincipleWaiveoffHead.setToolTipText("Search");
        btnPrincipleWaiveoffHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPrincipleWaiveoffHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPrincipleWaiveoffHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPrincipleWaiveoffHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrincipleWaiveoffHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        panChargeTypes.add(btnPrincipleWaiveoffHead, gridBagConstraints);

        lblNOticeChargeDebitHead.setText("Notice Chg Waiveoff HD ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panChargeTypes.add(lblNOticeChargeDebitHead, gridBagConstraints);

        txtNoticeChargeDebitHead.setAllowAll(true);
        txtNoticeChargeDebitHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoticeChargeDebitHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeChargeDebitHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        panChargeTypes.add(txtNoticeChargeDebitHead, gridBagConstraints);

        btnNoticeChargeDebitHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNoticeChargeDebitHd.setToolTipText("Search");
        btnNoticeChargeDebitHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnNoticeChargeDebitHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnNoticeChargeDebitHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNoticeChargeDebitHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoticeChargeDebitHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        panChargeTypes.add(btnNoticeChargeDebitHd, gridBagConstraints);

        lblDebitIntDiscount.setText("Interest Waiveoff HD");
        lblDebitIntDiscount.setMaximumSize(new java.awt.Dimension(120, 18));
        lblDebitIntDiscount.setMinimumSize(new java.awt.Dimension(120, 18));
        lblDebitIntDiscount.setPreferredSize(new java.awt.Dimension(120, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panChargeTypes.add(lblDebitIntDiscount, gridBagConstraints);

        txtDebitIntDiscount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitIntDiscount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebitIntDiscountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChargeTypes.add(txtDebitIntDiscount, gridBagConstraints);

        btnDebitIntDiscount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitIntDiscount.setToolTipText("Search");
        btnDebitIntDiscount.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDebitIntDiscount.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDebitIntDiscount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitIntDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitIntDiscountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChargeTypes.add(btnDebitIntDiscount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panAccountHead.add(panChargeTypes, gridBagConstraints);

        panAccountHeadDetails.setMinimumSize(new java.awt.Dimension(430, 300));
        panAccountHeadDetails.setPreferredSize(new java.awt.Dimension(430, 300));
        panAccountHeadDetails.setLayout(new java.awt.GridBagLayout());

        lblAccClosCharges.setText("Account Closing Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblAccClosCharges, gridBagConstraints);

        txtAccClosCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccClosCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccClosChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtAccClosCharges, gridBagConstraints);

        lbStatementCharges.setText("Statement Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lbStatementCharges, gridBagConstraints);

        txtStatementCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStatementCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStatementChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtStatementCharges, gridBagConstraints);

        lblAccDebitInter.setText("Account for Debit Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblAccDebitInter, gridBagConstraints);

        txtAccDebitInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccDebitInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccDebitInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtAccDebitInter, gridBagConstraints);

        lblPenalInter.setText("Penal Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblPenalInter, gridBagConstraints);

        txtPenalInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtPenalInter, gridBagConstraints);

        lblAccCreditInter.setText("Account for Credit Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblAccCreditInter, gridBagConstraints);

        txtAccCreditInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccCreditInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccCreditInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtAccCreditInter, gridBagConstraints);

        lblExpiryInter.setText("Expiry Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblExpiryInter, gridBagConstraints);

        txtExpiryInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExpiryInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExpiryInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtExpiryInter, gridBagConstraints);

        lblCheReturnChargest_Out.setText("Cheque Return Charges (Outward) - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblCheReturnChargest_Out, gridBagConstraints);

        txtCheReturnChargest_Out.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCheReturnChargest_Out.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCheReturnChargest_OutFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtCheReturnChargest_Out, gridBagConstraints);

        lblCheReturnChargest_In.setText("Cheque Return Charges (Inward) - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblCheReturnChargest_In, gridBagConstraints);

        txtCheReturnChargest_In.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCheReturnChargest_In.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCheReturnChargest_InFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtCheReturnChargest_In, gridBagConstraints);

        lblFolioChargesAcc.setText("Folio Charges Account - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblFolioChargesAcc, gridBagConstraints);

        txtFolioChargesAcc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFolioChargesAcc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFolioChargesAccFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtFolioChargesAcc, gridBagConstraints);

        lblCommitCharges.setText("Commitment Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblCommitCharges, gridBagConstraints);

        txtCommitCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommitCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommitChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtCommitCharges, gridBagConstraints);

        lblProcessingCharges.setText("Processing Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblProcessingCharges, gridBagConstraints);

        txtProcessingCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProcessingCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProcessingChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtProcessingCharges, gridBagConstraints);

        btnAccClosCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccClosCharges.setToolTipText("Search");
        btnAccClosCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccClosCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccClosChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnAccClosCharges, gridBagConstraints);

        btnStatementCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStatementCharges.setToolTipText("Search");
        btnStatementCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStatementCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatementChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnStatementCharges, gridBagConstraints);

        btnAccDebitInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccDebitInter.setToolTipText("Search");
        btnAccDebitInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccDebitInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccDebitInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnAccDebitInter, gridBagConstraints);

        btnPenalInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPenalInter.setToolTipText("Search");
        btnPenalInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPenalInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenalInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnPenalInter, gridBagConstraints);

        btnAccCreditInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccCreditInter.setToolTipText("Search");
        btnAccCreditInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccCreditInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccCreditInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnAccCreditInter, gridBagConstraints);

        btnExpiryInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExpiryInter.setToolTipText("Search");
        btnExpiryInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExpiryInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpiryInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnExpiryInter, gridBagConstraints);

        btnCheReturnChargest_Out.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCheReturnChargest_Out.setToolTipText("Search");
        btnCheReturnChargest_Out.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCheReturnChargest_Out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheReturnChargest_OutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnCheReturnChargest_Out, gridBagConstraints);

        btnCheReturnChargest_In.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCheReturnChargest_In.setToolTipText("Search");
        btnCheReturnChargest_In.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCheReturnChargest_In.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheReturnChargest_InActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnCheReturnChargest_In, gridBagConstraints);

        btnFolioChargesAcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFolioChargesAcc.setToolTipText("Search");
        btnFolioChargesAcc.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFolioChargesAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolioChargesAccActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnFolioChargesAcc, gridBagConstraints);

        btnCommitCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommitCharges.setToolTipText("Search");
        btnCommitCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommitCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommitChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnCommitCharges, gridBagConstraints);

        btnProcessingCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProcessingCharges.setToolTipText("Search");
        btnProcessingCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProcessingCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessingChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnProcessingCharges, gridBagConstraints);

        lblNoticeCharges.setText("Notice Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblNoticeCharges, gridBagConstraints);

        txtNoticeCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoticeCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtNoticeCharges, gridBagConstraints);

        btnNoticeCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNoticeCharges.setToolTipText("Search");
        btnNoticeCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNoticeCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoticeChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnNoticeCharges, gridBagConstraints);

        lblIntPayableAccount.setText("Credit Int Payable Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAccountHeadDetails.add(lblIntPayableAccount, gridBagConstraints);

        txtIntPayableAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntPayableAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntPayableAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(txtIntPayableAccount, gridBagConstraints);

        btnIntPayableAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntPayableAccount.setToolTipText("Search");
        btnIntPayableAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntPayableAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntPayableAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccountHeadDetails.add(btnIntPayableAccount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panAccountHead.add(panAccountHeadDetails, gridBagConstraints);

        panCaseHead.setMinimumSize(new java.awt.Dimension(320, 350));
        panCaseHead.setPreferredSize(new java.awt.Dimension(320, 350));
        panCaseHead.setLayout(new java.awt.GridBagLayout());

        lblArcExpenseAccount.setText("ARC Expense");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblArcExpenseAccount, gridBagConstraints);

        txtArcExpenseAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtArcExpenseAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtArcExpenseAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtArcExpenseAccount, gridBagConstraints);

        lblEaCostAccount.setText("EA Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblEaCostAccount, gridBagConstraints);

        txtEaCostAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEaCostAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEaCostAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtEaCostAccount, gridBagConstraints);

        lblEaExpenseAccount.setText("EA Expense");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblEaExpenseAccount, gridBagConstraints);

        txtEaExpenseAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEaExpenseAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEaExpenseAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtEaExpenseAccount, gridBagConstraints);

        lblEpCostAccount.setText("EP Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCaseHead.add(lblEpCostAccount, gridBagConstraints);

        txtEpCostAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEpCostAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEpCostAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtEpCostAccount, gridBagConstraints);

        lblEpExpenseAccount.setText("EP Expense");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblEpExpenseAccount, gridBagConstraints);

        txtEpExpenseAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtEpExpenseAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEpExpenseAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtEpExpenseAccount, gridBagConstraints);

        btnArcExpenseAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnArcExpenseAccount.setToolTipText("Search");
        btnArcExpenseAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnArcExpenseAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArcExpenseAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnArcExpenseAccount, gridBagConstraints);

        btnEaCostAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEaCostAccount.setToolTipText("Search");
        btnEaCostAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEaCostAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEaCostAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnEaCostAccount, gridBagConstraints);

        btnEaExpenseAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEaExpenseAccount.setToolTipText("Search");
        btnEaExpenseAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEaExpenseAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEaExpenseAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnEaExpenseAccount, gridBagConstraints);

        btnEpCostAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEpCostAccount.setToolTipText("Search");
        btnEpCostAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEpCostAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEpCostAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnEpCostAccount, gridBagConstraints);

        btnEpExpenseAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnEpExpenseAccount.setToolTipText("Search");
        btnEpExpenseAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnEpExpenseAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEpExpenseAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnEpExpenseAccount, gridBagConstraints);

        lblArcCostAccount.setText("ARC Cost");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblArcCostAccount, gridBagConstraints);

        txtArcCostAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtArcCostAccount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtArcCostAccountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtArcCostAccount, gridBagConstraints);

        btnArcCostAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnArcCostAccount.setToolTipText("Search");
        btnArcCostAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnArcCostAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArcCostAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnArcCostAccount, gridBagConstraints);

        lblEpExpenseAccount1.setText("Rebate Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblEpExpenseAccount1, gridBagConstraints);

        txtRebateInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRebateInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRebateInterestActionPerformed(evt);
            }
        });
        txtRebateInterest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRebateInterestFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtRebateInterest, gridBagConstraints);

        btnRebateInterest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRebateInterest.setToolTipText("Search");
        btnRebateInterest.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRebateInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRebateInterestActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnRebateInterest, gridBagConstraints);

        lblStampAdvancesHead.setText("Stamp Advances Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblStampAdvancesHead, gridBagConstraints);

        txtStampAdvancesHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStampAdvancesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStampAdvancesHeadActionPerformed(evt);
            }
        });
        txtStampAdvancesHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStampAdvancesHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtStampAdvancesHead, gridBagConstraints);

        btnStampAdvancesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStampAdvancesHead.setToolTipText("Save");
        btnStampAdvancesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStampAdvancesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStampAdvancesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnStampAdvancesHead, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(200, 18));
        cPanel1.setPreferredSize(new java.awt.Dimension(200, 18));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        lblCreditStampAdvance.setText("Credit Stamp Advance");
        lblCreditStampAdvance.setMaximumSize(new java.awt.Dimension(145, 18));
        lblCreditStampAdvance.setMinimumSize(new java.awt.Dimension(145, 18));
        lblCreditStampAdvance.setPreferredSize(new java.awt.Dimension(145, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(lblCreditStampAdvance, gridBagConstraints);

        chkCreditStampAdvance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCreditStampAdvanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel1.add(chkCreditStampAdvance, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(cPanel1, gridBagConstraints);

        lblAdvertisementHead.setText("Advertisement Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblAdvertisementHead, gridBagConstraints);

        txtAdvertisementHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAdvertisementHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAdvertisementHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtAdvertisementHead, gridBagConstraints);

        btnAdvertisementHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAdvertisementHead.setToolTipText("Save");
        btnAdvertisementHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAdvertisementHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdvertisementHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnAdvertisementHead, gridBagConstraints);

        lblARCEPSuspenceHead.setText("ARC,EP,EA,SuspenceHead");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblARCEPSuspenceHead, gridBagConstraints);

        txtARCEPSuspenceHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtARCEPSuspenceHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtARCEPSuspenceHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtARCEPSuspenceHead, gridBagConstraints);

        btnARCEPSuspenceHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnARCEPSuspenceHead.setToolTipText("Save");
        btnARCEPSuspenceHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnARCEPSuspenceHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnARCEPSuspenceHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnARCEPSuspenceHead, gridBagConstraints);

        cLabel3.setText("Other Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(cLabel3, gridBagConstraints);

        txtOtherCharges.setAllowAll(true);
        txtOtherCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtOtherCharges, gridBagConstraints);

        btnOtherCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOtherCharges.setToolTipText("Search");
        btnOtherCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOtherCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOtherChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        panCaseHead.add(btnOtherCharges, gridBagConstraints);

        lblNoticeStampAdvance.setText("Credit Notice Advance");
        lblNoticeStampAdvance.setMaximumSize(new java.awt.Dimension(140, 18));
        lblNoticeStampAdvance.setMinimumSize(new java.awt.Dimension(140, 18));
        lblNoticeStampAdvance.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblNoticeStampAdvance, gridBagConstraints);

        chkCreditNoticeAdvance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCreditNoticeAdvanceActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(chkCreditNoticeAdvance, gridBagConstraints);

        lblNoticeAdvancesHead.setText("Notice Advances Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panCaseHead.add(lblNoticeAdvancesHead, gridBagConstraints);

        txtNoticeAdvancesHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoticeAdvancesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoticeAdvancesHeadActionPerformed(evt);
            }
        });
        txtNoticeAdvancesHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeAdvancesHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(txtNoticeAdvancesHead, gridBagConstraints);

        btnNoticeAdvancesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNoticeAdvancesHead.setToolTipText("Save");
        btnNoticeAdvancesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNoticeAdvancesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoticeAdvancesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCaseHead.add(btnNoticeAdvancesHead, gridBagConstraints);

        cLabel27.setText("Overdue Interest Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        panCaseHead.add(cLabel27, gridBagConstraints);

        txtOverdueIntHead.setAllowAll(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        panCaseHead.add(txtOverdueIntHead, gridBagConstraints);

        btnOverdueIntHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOverdueIntHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnOverdueIntHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnOverdueIntHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnOverdueIntHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOverdueIntHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        panCaseHead.add(btnOverdueIntHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        panAccountHead.add(panCaseHead, gridBagConstraints);

        panSuspenseProducts.setMinimumSize(new java.awt.Dimension(300, 130));
        panSuspenseProducts.setPreferredSize(new java.awt.Dimension(300, 130));
        panSuspenseProducts.setLayout(new java.awt.GridBagLayout());

        lblSuspenseCreditAchd.setText("Suspense Credit Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        panSuspenseProducts.add(lblSuspenseCreditAchd, gridBagConstraints);

        txtSuspenseCreditProductID.setAllowAll(true);
        txtSuspenseCreditProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSuspenseCreditProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuspenseCreditProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        panSuspenseProducts.add(txtSuspenseCreditProductID, gridBagConstraints);

        btnSuspenseCreditAchd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSuspenseCreditAchd.setToolTipText("Search");
        btnSuspenseCreditAchd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSuspenseCreditAchd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspenseCreditAchdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        panSuspenseProducts.add(btnSuspenseCreditAchd, gridBagConstraints);

        lblSuspenseDebitAchd.setText("Suspense Debit Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        panSuspenseProducts.add(lblSuspenseDebitAchd, gridBagConstraints);

        txtSuspenseDebitProductId.setAllowAll(true);
        txtSuspenseDebitProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSuspenseDebitProductId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSuspenseDebitProductIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        panSuspenseProducts.add(txtSuspenseDebitProductId, gridBagConstraints);

        btnSuspenseDebitAchd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSuspenseDebitAchd.setToolTipText("Search");
        btnSuspenseDebitAchd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSuspenseDebitAchd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspenseDebitAchdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        panSuspenseProducts.add(btnSuspenseDebitAchd, gridBagConstraints);

        cPanel4.setMinimumSize(new java.awt.Dimension(260, 20));
        cPanel4.setPreferredSize(new java.awt.Dimension(260, 20));
        cPanel4.setLayout(new java.awt.GridBagLayout());

        lblCreditStampAdvance1.setText("Loan Closing(Gold Auction Allowed?)");
        lblCreditStampAdvance1.setMaximumSize(new java.awt.Dimension(230, 18));
        lblCreditStampAdvance1.setMinimumSize(new java.awt.Dimension(230, 18));
        lblCreditStampAdvance1.setPreferredSize(new java.awt.Dimension(230, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel4.add(lblCreditStampAdvance1, gridBagConstraints);

        chkGoldAuctionAllowed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkGoldAuctionAllowedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        cPanel4.add(chkGoldAuctionAllowed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panSuspenseProducts.add(cPanel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panAccountHead.add(panSuspenseProducts, gridBagConstraints);

        btnCreateHead.setText("Create Account Head");
        btnCreateHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAccountHead.add(btnCreateHead, gridBagConstraints);

        tabLoanProduct.addTab("Account Head", panAccountHead);

        PanGroupProduct.setLayout(new java.awt.GridBagLayout());

        panProductDetails.setMaximumSize(new java.awt.Dimension(500, 200));
        panProductDetails.setMinimumSize(new java.awt.Dimension(500, 200));
        panProductDetails.setPreferredSize(new java.awt.Dimension(500, 200));
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        lblIntrestCalcMonth.setText("Month");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(89, 10, 0, 40);
        panProductDetails.add(lblIntrestCalcMonth, gridBagConstraints);

        cboIntPaymntMonth.setMinimumSize(new java.awt.Dimension(50, 21));
        cboIntPaymntMonth.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(89, 0, 0, 0);
        panProductDetails.add(cboIntPaymntMonth, gridBagConstraints);

        lblDebitAllowedForDueCustomer.setText("Debit Allowed For Due Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 11;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 160, 0, 0);
        panProductDetails.add(lblDebitAllowedForDueCustomer, gridBagConstraints);

        cboIntPaymntDay.setMinimumSize(new java.awt.Dimension(50, 21));
        cboIntPaymntDay.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(90, 20, 0, 0);
        panProductDetails.add(cboIntPaymntDay, gridBagConstraints);

        lblIntrestCalcDay.setText("Day");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(90, 10, 0, 0);
        panProductDetails.add(lblIntrestCalcDay, gridBagConstraints);

        lblIntrestCalcDay2.setText("Interest Calculating after");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(90, 200, 0, 0);
        panProductDetails.add(lblIntrestCalcDay2, gridBagConstraints);

        panIsLimitDefnAllowed1.setLayout(new java.awt.GridBagLayout());

        rdoIsLimitDefnAllowed.add(rdoIsDebitAllowedForDue_Yes);
        rdoIsDebitAllowedForDue_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsLimitDefnAllowed1.add(rdoIsDebitAllowedForDue_Yes, gridBagConstraints);

        rdoIsLimitDefnAllowed.add(rdoIsDebitAllowedForDue_No);
        rdoIsDebitAllowedForDue_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsLimitDefnAllowed1.add(rdoIsDebitAllowedForDue_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 16, 0, 0);
        panProductDetails.add(panIsLimitDefnAllowed1, gridBagConstraints);

        lblHolidayInst.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHolidayInst.setText("If installment Date is a holiday, insterest to be calculated ");
        lblHolidayInst.setMinimumSize(new java.awt.Dimension(340, 18));
        lblHolidayInst.setPreferredSize(new java.awt.Dimension(340, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panProductDetails.add(lblHolidayInst, gridBagConstraints);

        cboholidayInt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 20, 20, 0);
        panProductDetails.add(cboholidayInt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 170;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(216, 171, 217, 142);
        PanGroupProduct.add(panProductDetails, gridBagConstraints);

        tabLoanProduct.addTab("Group Loan Product", PanGroupProduct);

        panLoanProduct.add(tabLoanProduct, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(panLoanProduct, gridBagConstraints);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnView);

        lblSpace6.setText("     ");
        tbrLoantProduct.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCopy);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace5.setText("     ");
        tbrLoantProduct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(tbrLoantProduct, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mitNew.setMnemonic('N');
        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        mitEdit.setMnemonic('E');
        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptProcess);

        mitSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mitSave.setMnemonic('S');
        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitPrint.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        mitPrint.setMnemonic('P');
        mitPrint.setText("Print");
        mitPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitPrintActionPerformed(evt);
            }
        });
        mnuProcess.add(mitPrint);
        mnuProcess.add(sptPrint);

        mitClose.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        mitClose.setMnemonic('l');
        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrLoanProduct.add(mnuProcess);

        setJMenuBar(mbrLoanProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoinsuranceCharge_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoinsuranceCharge_YesActionPerformed
        // TODO add your handling code here:
        enableDisbleInsurance(true);
    }//GEN-LAST:event_rdoinsuranceCharge_YesActionPerformed

    private void rdoinsuranceCharge_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoinsuranceCharge_NoActionPerformed
        // TODO add your handling code here:
        enableDisbleInsurance(false);
    }//GEN-LAST:event_rdoinsuranceCharge_NoActionPerformed

    private void enableDisbleInsurance(boolean flag) {
        rdosanctionDate_Yes.setEnabled(flag);
        rdosanctionDate_No.setEnabled(flag);
        txtInsuraceRate.setEnabled(flag);
    }
    private void txtExecutionDecreeChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExecutionDecreeChargesFocusLost
        // TODO add your handling code here:
        if (!txtExecutionDecreeCharges.getText().equals("")) {
            btnExecutionDecreeCharges.setToolTipText(getAccHdDesc(txtExecutionDecreeCharges.getText()));
        }
    }//GEN-LAST:event_txtExecutionDecreeChargesFocusLost

    private void txtInsuranceChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInsuranceChargesFocusLost
        // TODO add your handling code here:
        if (!txtInsuranceCharges.getText().equals("")) {
            btnInsuranceCharges.setToolTipText(getAccHdDesc(txtInsuranceCharges.getText()));
        }
    }//GEN-LAST:event_txtInsuranceChargesFocusLost

    private void txtArbitraryChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtArbitraryChargesFocusLost
        // TODO add your handling code here:
        if (!txtArbitraryCharges.getText().equals("")) {
            btnArbitraryCharges.setToolTipText(getAccHdDesc(txtArbitraryCharges.getText()));
        }
    }//GEN-LAST:event_txtArbitraryChargesFocusLost

    private void txtARCEPSuspenceHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtARCEPSuspenceHeadFocusLost
        // TODO add your handling code here:
        if (!txtARCEPSuspenceHead.getText().equals("")) {
            btnARCEPSuspenceHead.setToolTipText(getAccHdDesc(txtARCEPSuspenceHead.getText()));
        }
    }//GEN-LAST:event_txtARCEPSuspenceHeadFocusLost

    private void txtRebateInterestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRebateInterestFocusLost
        // TODO add your handling code here:
        if (!txtRebateInterest.getText().equals("")) {
            btnRebateInterest.setToolTipText(getAccHdDesc(txtRebateInterest.getText()));
        }
    }//GEN-LAST:event_txtRebateInterestFocusLost

    private void txtLegalChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLegalChargesFocusLost
        // TODO add your handling code here:
        if (!txtLegalCharges.getText().equals("")) {
            btnLegalCharges.setToolTipText(getAccHdDesc(txtLegalCharges.getText()));
        }
    }//GEN-LAST:event_txtLegalChargesFocusLost

    private void txtPostageChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostageChargesFocusLost
        // TODO add your handling code here:
        if (!txtPostageCharges.getText().equals("")) {
            btnPostageCharges.setToolTipText(getAccHdDesc(txtPostageCharges.getText()));
        }
    }//GEN-LAST:event_txtPostageChargesFocusLost

    private void txtDebitIntDiscountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitIntDiscountFocusLost
        // TODO add your handling code here:
        if (!txtDebitIntDiscount.getText().equals("")) {
            btnDebitIntDiscount.setToolTipText(getAccHdDesc(txtDebitIntDiscount.getText()));
        }
    }//GEN-LAST:event_txtDebitIntDiscountFocusLost

    private void txtNoticeChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeChargesFocusLost
        // TODO add your handling code here:
        if (!txtNoticeCharges.getText().equals("")) {
            btnNoticeCharges.setToolTipText(getAccHdDesc(txtNoticeCharges.getText()));
        }
    }//GEN-LAST:event_txtNoticeChargesFocusLost

    private void txtIntPayableAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntPayableAccountFocusLost
        // TODO add your handling code here:
        if (!txtIntPayableAccount.getText().equals("")) {
            btnIntPayableAccount.setToolTipText(getAccHdDesc(txtIntPayableAccount.getText()));
        }
    }//GEN-LAST:event_txtIntPayableAccountFocusLost

    private void btnTransactionRemove_OTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionRemove_OTSActionPerformed
        // TODO add your handling code here:3
        int selected = lstSelectedPriorityTransaction_OTS.getSelectedIndex();
        if (selected == -1) {
            ClientUtil.showMessageWindow("First Select Transaction From Selected Priority Transaction");
            return;
        }
        observable.removeTargetTransactionList_OTS(selected);
    }//GEN-LAST:event_btnTransactionRemove_OTSActionPerformed

    private void btnTransactionAdd_OTSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionAdd_OTSActionPerformed
        // TODO add your handling code here:

        int isSelected = lstAvailableTypeTransaction_OTS.getSelectedIndex();
        if (isSelected == -1) {
            ClientUtil.showMessageWindow("First Select Transaction From Available Transaction");
            return;
        }

        observable.addTargetTransactionList_OTS(isSelected);
//        lstSelectedDeposits.addE
        lstAvailableTypeTransaction_OTS.setSelectedValue("obj", false);
    }//GEN-LAST:event_btnTransactionAdd_OTSActionPerformed

    private void cboProdCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdCategoryActionPerformed
        // TODO add your handling code here:
        String prodCategory = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdCategory.getModel()).getKeyForSelected());
        if (prodCategory.length() > 0 && prodCategory.equals("DAILY_LOAN")) {
            lblcalcType.setText(resourceBundle.getString("lblDailyProduct"));
        } else {
            lblcalcType.setText(resourceBundle.getString("lblcalcType"));
            disableFrequencyText();
        }
        if (CommonUtil.convertObjToStr(cboProdCategory.getSelectedItem()).equals("Gold Loan")) {
            panGldRenewal.setVisible(true);
        } else {
            panGldRenewal.setVisible(false);
        }
    }//GEN-LAST:event_cboProdCategoryActionPerformed

    private void btnARCEPSuspenceHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnARCEPSuspenceHeadActionPerformed
        // TODO add your handling code here:
        popUp(ARCEPSUSPENCEACHD);
    }//GEN-LAST:event_btnARCEPSuspenceHeadActionPerformed

    private void btnAdvertisementHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdvertisementHeadActionPerformed
        // TODO add your handling code here:
        popUp(ADVERTISEMENTHEAD);
    }//GEN-LAST:event_btnAdvertisementHeadActionPerformed

    private void txtAdvertisementHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAdvertisementHeadFocusLost
        // TODO add your handling code here:
        if (!(txtAdvertisementHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAdvertisementHead, "TermLoan.getSelectAcctHeadTOList");
            btnAdvertisementHead.setToolTipText(getAccHdDesc(txtAdvertisementHead.getText()));
        }
    }//GEN-LAST:event_txtAdvertisementHeadFocusLost

    private void txtStampAdvancesHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStampAdvancesHeadFocusLost
        // TODO add your handling code here:
        if (!(txtStampAdvancesHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtStampAdvancesHead, "TermLoan.getSelectAcctHeadTOList");
            btnStampAdvancesHead.setToolTipText(getAccHdDesc(txtStampAdvancesHead.getText()));
        }
    }//GEN-LAST:event_txtStampAdvancesHeadFocusLost

    private void txtStampAdvancesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStampAdvancesHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStampAdvancesHeadActionPerformed

    private void chkCreditStampAdvanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditStampAdvanceActionPerformed
        // TODO add your handling code here:
        if (chkCreditStampAdvance.isSelected()) {
            setEnableStamp(true);
        } else {
            setEnableStamp(false);
            txtStampAdvancesHead.setText("");
        }
    }//GEN-LAST:event_chkCreditStampAdvanceActionPerformed
    private void setEnableStamp(boolean flag) {
        txtStampAdvancesHead.setEnabled(flag);
        btnStampAdvancesHead.setEnabled(flag);
    }
    private void btnStampAdvancesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStampAdvancesHeadActionPerformed
        // TODO add your handling code here:
        popUp(STAMPADVANCESHEAD);
    }//GEN-LAST:event_btnStampAdvancesHeadActionPerformed

    private void btnRebateInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRebateInterestActionPerformed
        // TODO add your handling code here:

        popUp(REBATEACCHEAD);
    }//GEN-LAST:event_btnRebateInterestActionPerformed

    private void txtRebateInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRebateInterestActionPerformed
        // TODO add your handling code here:
        if (!(txtRebateInterest.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtRebateInterest, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtRebateInterestActionPerformed

    private void cboRebatePeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRebatePeriodActionPerformed
        // TODO add your handling code here:
        if (!(viewType == AUTHORIZE)) {
            String rebatePeriod = CommonUtil.convertObjToStr(((ComboBoxModel) cboRebatePeriod.getModel()).getKeyForSelected());
            if (rebatePeriod.length() > 0) {
                if (rebatePeriod.equals("Financial year")) {
                    ClientUtil.enableDisable(cPanel3, true);
                } else {
                    ClientUtil.enableDisable(cPanel3, false);
                    txtFinYearStartingFromDD.setText("");
                    txtFinYearStartingFromMM.setText("");
                }
            }
        }
    }//GEN-LAST:event_cboRebatePeriodActionPerformed

    private void btnDebitIntDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitIntDiscountActionPerformed
        // TODO add your handling code here:

        popUp(DEBIT_DISCOUNT_ACHD);
    }//GEN-LAST:event_btnDebitIntDiscountActionPerformed

    private void rdoInterestRebateAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestRebateAllowed_NoActionPerformed
        // TODO add your handling code here:
        if (rdoInterestRebateAllowed_No.isSelected()) {
            enableDisablInterestRebateDetails(false);
            txtInterestRebatePercentage.setText("");
            ((ComboBoxModel) cboInterestRebateCalculation.getModel()).setKeyForSelected("");
            ((ComboBoxModel) cboRebatePeriod.getModel()).setKeyForSelected("");
            txtFinYearStartingFromDD.setText("");
            txtFinYearStartingFromMM.setText("");
            txtRebateLoanIntPercent.setText("");// Added by nithya on 11-01-2020 for KD-1234
        }
    }//GEN-LAST:event_rdoInterestRebateAllowed_NoActionPerformed

    private void rdoInterestRebateAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestRebateAllowed_YesActionPerformed
        // TODO add your handling code here:
        if (rdoInterestRebateAllowed_Yes.isSelected()) {
            enableDisablInterestRebateDetails(true);
        }
    }//GEN-LAST:event_rdoInterestRebateAllowed_YesActionPerformed

    private void rdoLoanOpenDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLoanOpenDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoLoanOpenDateActionPerformed

    private void rdoSubsidyReceivedDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSubsidyReceivedDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoSubsidyReceivedDateActionPerformed

    private void rdoSubsidyAdjustLoanBalance_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSubsidyAdjustLoanBalance_YesActionPerformed
        // TODO add your handling code here:

        enableSubsidyfromloanOpenDate(true);
    }//GEN-LAST:event_rdoSubsidyAdjustLoanBalance_YesActionPerformed

    private void rdoLoanBalance_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLoanBalance_YesActionPerformed
        // TODO add your handling code here:
        enableSubsidyfromloanOpenDate(false);
        fromloanOpenDate(false);

    }//GEN-LAST:event_rdoLoanBalance_YesActionPerformed

    private void fromloanOpenDate(boolean flag) {

        rdoFromSubsidyReceivedDateGroup.remove(rdoSubsidyReceivedDate);
        rdoFromSubsidyReceivedDateGroup.remove(rdoLoanOpenDate);
        rdoSubsidyReceivedDate.setSelected(flag);
        rdoLoanOpenDate.setSelected(flag);
        rdoFromSubsidyReceivedDateGroup = new CButtonGroup();
        rdoFromSubsidyReceivedDateGroup.add(rdoSubsidyReceivedDate);
        rdoFromSubsidyReceivedDateGroup.add(rdoLoanOpenDate);
    }

    private void enableSubsidyfromloanOpenDate(boolean flag) {
        rdoSubsidyReceivedDate.setEnabled(flag);
        rdoLoanOpenDate.setEnabled(flag);
    }

    private void enableSubsidyloanBalance(boolean flag) {
        rdoLoanBalance_Yes.setEnabled(flag);
        rdoSubsidyAdjustLoanBalance_Yes.setEnabled(flag);
        rdoSubsidyAdjustLoanBalance_Yes.setSelected(false);
        rdoLoanBalance_Yes.setSelected(false);
    }

    private void rdoSubsidy_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSubsidy_NoActionPerformed
        // TODO add your handling code here:
        rdosubsidyInterestCalculateGroup.remove(rdoLoanBalance_Yes);
        rdosubsidyInterestCalculateGroup.remove(rdoSubsidyAdjustLoanBalance_Yes);
        enableDisableSubsidyDetails(false);
        enableSubsidyloanBalance(false);
        rdosubsidyInterestCalculateGroup = new CButtonGroup();
        rdosubsidyInterestCalculateGroup.add(rdoLoanBalance_Yes);
        rdosubsidyInterestCalculateGroup.add(rdoSubsidyAdjustLoanBalance_Yes);

        enableSubsidyfromloanOpenDate(false);
        fromloanOpenDate(false);

    }//GEN-LAST:event_rdoSubsidy_NoActionPerformed

    private void rdoSubsidy_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSubsidy_YesActionPerformed
        // TODO add your handling code here:
        if (rdoSubsidy_Yes.isSelected()) {
            enableDisableSubsidyDetails(true);
            enableSubsidyloanBalance(true);
        }
//        enableSubsidyfromloanOpenDate(false);
//        enableSubsidyloanBalance(false);

    }//GEN-LAST:event_rdoSubsidy_YesActionPerformed

    private void btnTransactionRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionRemoveActionPerformed
        // TODO add your handling code here:
        int selected = lstSelectedPriorityTransaction.getSelectedIndex();
        if (selected == -1) {
            ClientUtil.showMessageWindow("First Select Transaction From Selected Priority Transaction");
            return;
        }
        observable.removeTargetTransactionList(selected);

    }//GEN-LAST:event_btnTransactionRemoveActionPerformed

    private void btnTransactionAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransactionAddActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        int isSelected = lstAvailableTypeTransaction.getSelectedIndex();
        if (isSelected == -1) {
            ClientUtil.showMessageWindow("First Select Transaction From Available Transaction");
            return;
        }

        observable.addTargetTransactionList(isSelected);
//        lstSelectedDeposits.addE
        lstAvailableTypeTransaction.setSelectedValue("obj", false);

    }//GEN-LAST:event_btnTransactionAddActionPerformed

    private void txtEpExpenseAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEpExpenseAccountFocusLost
        // TODO add your handling code here:
        if (!(txtEpExpenseAccount.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtEpExpenseAccount, "TermLoan.getSelectAcctHeadTOList");
            btnEpExpenseAccount.setToolTipText(getAccHdDesc(txtEpExpenseAccount.getText()));
        }
    }//GEN-LAST:event_txtEpExpenseAccountFocusLost

    private void txtEpCostAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEpCostAccountFocusLost
        // TODO add your handling code here:
        if (!(txtEpCostAccount.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtEpCostAccount, "TermLoan.getSelectAcctHeadTOList");
            btnEpCostAccount.setToolTipText(getAccHdDesc(txtEpCostAccount.getText()));
        }
    }//GEN-LAST:event_txtEpCostAccountFocusLost

    private void txtEaExpenseAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEaExpenseAccountFocusLost
        // TODO add your handling code here:
        if (!(txtEaExpenseAccount.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtEaExpenseAccount, "TermLoan.getSelectAcctHeadTOList");
            btnEaExpenseAccount.setToolTipText(getAccHdDesc(txtEaExpenseAccount.getText()));
        }
    }//GEN-LAST:event_txtEaExpenseAccountFocusLost

    private void txtEaCostAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEaCostAccountFocusLost
        // TODO add your handling code here:
        if (!(txtEaCostAccount.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtEaCostAccount, "TermLoan.getSelectAcctHeadTOList");
            btnEaCostAccount.setToolTipText(getAccHdDesc(txtEaCostAccount.getText()));
        }
    }//GEN-LAST:event_txtEaCostAccountFocusLost

    private void txtArcExpenseAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtArcExpenseAccountFocusLost
        // TODO add your handling code here:
        if (!(txtArcExpenseAccount.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtArcExpenseAccount, "TermLoan.getSelectAcctHeadTOList");
            btnArcExpenseAccount.setToolTipText(getAccHdDesc(txtArcExpenseAccount.getText()));
        }
    }//GEN-LAST:event_txtArcExpenseAccountFocusLost

    private void txtArcCostAccountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtArcCostAccountFocusLost
        // TODO add your handling code here:
        if (!(txtArcCostAccount.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtArcCostAccount, "TermLoan.getSelectAcctHeadTOList");
            btnArcCostAccount.setToolTipText(getAccHdDesc(txtArcCostAccount.getText()));
        }
    }//GEN-LAST:event_txtArcCostAccountFocusLost

    private void btnEpExpenseAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEpExpenseAccountActionPerformed
        // TODO add your handling code here:
        popUp(EPEXPENSETHEAD);
    }//GEN-LAST:event_btnEpExpenseAccountActionPerformed

    private void btnEpCostAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEpCostAccountActionPerformed
        // TODO add your handling code here:
        popUp(EPCOSTHEAD);
    }//GEN-LAST:event_btnEpCostAccountActionPerformed

    private void btnEaExpenseAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEaExpenseAccountActionPerformed
        // TODO add your handling code here:
        popUp(EAEXPENSEHEAD);
    }//GEN-LAST:event_btnEaExpenseAccountActionPerformed

    private void btnEaCostAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEaCostAccountActionPerformed
        // TODO add your handling code here:
        popUp(EACOSTHEAD);
    }//GEN-LAST:event_btnEaCostAccountActionPerformed

    private void btnArcExpenseAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArcExpenseAccountActionPerformed
        // TODO add your handling code here:
        popUp(ARCEXPENSEHEAD);
    }//GEN-LAST:event_btnArcExpenseAccountActionPerformed

    private void btnArcCostAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArcCostAccountActionPerformed
        // TODO add your handling code here:
        popUp(ARCCOSTHEAD);
    }//GEN-LAST:event_btnArcCostAccountActionPerformed

    private void btnLTDRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLTDRemoveActionPerformed
        // TODO add your handling code here:
        int selected = lstSelectedDeposits.getSelectedIndex();
        if (selected == -1) {
            ClientUtil.showMessageWindow("First Select Deposit From Selected Deposit");
            return;
        }
        observable.removeTargetList(selected);

    }//GEN-LAST:event_btnLTDRemoveActionPerformed

    private void btnLTDAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLTDAddActionPerformed
        // TODO add your handling code here:
        int isSelected = lstAvailableDeposits.getSelectedIndex();
        if (isSelected == -1) {
            ClientUtil.showMessageWindow("First Select Deposit From Available Deposit");
            return;
        }

        observable.addTargetList(isSelected);
//        lstSelectedDeposits.addE
        lstAvailableDeposits.setSelectedValue("obj", false);
//       lstAvailableDeposits.setL
    }//GEN-LAST:event_btnLTDAddActionPerformed

    private void cboOperatesLikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboOperatesLikeActionPerformed
        // TODO add your handling code here:
        String behaves_like = CommonUtil.convertObjToStr(((com.see.truetransact.clientutil.ComboBoxModel) cboOperatesLike.getModel()).getKeyForSelected());
        if (behaves_like.equals("OD")) {
            setInterestDueKeptReceivableVisible(true);
            panGroupLoan.setVisible(true);

        } else {
            setInterestDueKeptReceivableVisible(false);
            panGroupLoan.setVisible(false);
        }
        if (behaves_like.equals("LOANS_AGAINST_DEPOSITS")) {
            ClientUtil.enableDisable(panLoanAgainstDeposit, true);
            btnLTDAdd.setEnabled(true);
            btnLTDRemove.setEnabled(true);
            ClientUtil.enableDisable(panLTDInterest, true);
            //            panLTDInterest.setVisible(true);
            setVisibleUptoDepositMaturityorCurrDt(true);
            txtDepAmtMaturing.setEnabled(true);
            txtDepAmtLoanMaturingPeriod.setEnabled(true);
            cboDepAmtLoanMaturing.setEnabled(true);

        } else {
            ClientUtil.enableDisable(panLoanAgainstDeposit, false);
            btnLTDAdd.setEnabled(false);
            btnLTDRemove.setEnabled(false);
//             ClientUtil.enableDisable(panLTDInterest,false);
            //              panLTDInterest.setVisible(false);
            setVisibleUptoDepositMaturityorCurrDt(false);
            txtDepAmtMaturing.setEnabled(false);
            txtDepAmtLoanMaturingPeriod.setEnabled(false);
            cboDepAmtLoanMaturing.setEnabled(false);

        }
    }//GEN-LAST:event_cboOperatesLikeActionPerformed

    private void setVisibleUptoDepositMaturityorCurrDt(boolean flag) {
        panLTDInterest.setVisible(flag);
        panLTDInterest1.setVisible(flag);
        lblCollectIntTillLoanClosureMenu.setVisible(flag);
        lblCollectIntTillDepositClosureMenu.setVisible(flag);
    }
    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // TODO add your handling code here:
        observable.resetTable();
        observable.setActionType(ClientConstants.ACTIONTYPE_COPY);
        //        enableDisableTextBox();
        // to enable the Table enabled
        tblCharges.setEnabled(true);
        enableDisableTableDocuments(true);
        popUp(COPY);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        HashMap idMap = generateID();
        if (idMap != null && idMap.size() > 0) {
            txtProductID.setText((String) idMap.get(CommonConstants.DATA));
        }
    }//GEN-LAST:event_btnCopyActionPerformed

    private void rdobill_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdobill_NoActionPerformed
        // TODO add your handling code here:
        rdoasAndWhenCustomer_Yes.setSelected(true);
        rdocalendarFrequency_No.setSelected(true);
    }//GEN-LAST:event_rdobill_NoActionPerformed

    private void rdobill_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdobill_YesActionPerformed
        // TODO add your handling code here:
//        rdobill_Yes.setSelected(true);
        rdoasAndWhenCustomer_No.setSelected(true);
        rdocalendarFrequency_No.setSelected(true);

    }//GEN-LAST:event_rdobill_YesActionPerformed

    private void btnExecutionDecreeChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecutionDecreeChargesActionPerformed
        // TODO add your handling code here:
        popUp(EXECUTION_DECREE_CHARGES);
    }//GEN-LAST:event_btnExecutionDecreeChargesActionPerformed

    private void btnInsuranceChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsuranceChargesActionPerformed
        // TODO add your handling code here:
        popUp(INSURANCECHARGES);
    }//GEN-LAST:event_btnInsuranceChargesActionPerformed

    private void btnArbitraryChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArbitraryChargesActionPerformed
        // TODO add your handling code here:
        popUp(ARBITRARYCHARGES);
    }//GEN-LAST:event_btnArbitraryChargesActionPerformed

    private void btnLegalChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLegalChargesActionPerformed
        // TODO add your handling code here:
        popUp(LEGALCHARGES);
    }//GEN-LAST:event_btnLegalChargesActionPerformed

    private void chkprincipalDueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkprincipalDueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkprincipalDueActionPerformed

    private void setInterestCalcMethod() {
//        if (rdoasAndWhenCustomer_Yes.isSelected()) {
//            rdocalendarFrequency_No.setSelected(true);
//            if (rdocalendarFrequency_Yes.isSelected()) {
//                 rdoasAndWhenCustomer_No.setSelected(true);
//            } else {
//                 rdoasAndWhenCustomer_Yes.setSelected(true);
//            }
//        } else {
//            rdocalendarFrequency_Yes.setSelected(true);
//            if (rdocalendarFrequency_Yes.isSelected()) {
//                 rdoasAndWhenCustomer_No.setSelected(true);
//            } else {
//                 rdoasAndWhenCustomer_Yes.setSelected(true);
//            }
//        }


        if (rdoPenalAppl_Yes.isSelected()) {
            if (rdoasAndWhenCustomer_Yes.isSelected()) {
                chkInterestDue.setEnabled(true);//false
                chkInterestDue.setSelected(false);
                chkprincipalDue.setEnabled(true);
            } else {
                chkInterestDue.setEnabled(true);
                chkprincipalDue.setEnabled(true);
            }
        } else {
            chkInterestDue.setEnabled(false);
            chkInterestDue.setSelected(false);
            chkprincipalDue.setEnabled(false);
            chkprincipalDue.setSelected(false);
        }
//        if(rdoPenalAppl_No.isSelected()){
//            chkInterestDue.setEnabled(false);
//            chkInterestDue.setSelected(false);
//            chkprincipalDue.setEnabled(false);
//            chkprincipalDue.setSelected(false);
//        } else {
//            chkInterestDue.setEnabled(true);
//            chkprincipalDue.setEnabled(true);
//        }

    }

    private void rdocalendarFrequency_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdocalendarFrequency_NoActionPerformed
        // TODO add your handling code here:
        rdoasAndWhenCustomer_Yes.setSelected(true);
        rdobill_No.setSelected(true);
        setInterestCalcMethod();
//        rdoasAndWhenCustomer_YesActionPerformed(evt);
    }//GEN-LAST:event_rdocalendarFrequency_NoActionPerformed

    private void rdoasAndWhenCustomer_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoasAndWhenCustomer_NoActionPerformed
        // TODO add your handling code here:
        rdocalendarFrequency_Yes.setSelected(true);
        rdobill_No.setSelected(true);
        setInterestCalcMethod();
//        rdocalendarFrequency_YesActionPerformed(evt);
    }//GEN-LAST:event_rdoasAndWhenCustomer_NoActionPerformed

    private void rdoasAndWhenCustomer_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoasAndWhenCustomer_YesActionPerformed
        // TODO add your handling code here:
        rdocalendarFrequency_No.setSelected(true);
        rdobill_No.setSelected(true);
        setInterestCalcMethod();
//        rdocalendarFrequency_NoActionPerformed(evt);
    }//GEN-LAST:event_rdoasAndWhenCustomer_YesActionPerformed

    private void rdocalendarFrequency_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdocalendarFrequency_YesActionPerformed
        // TODO add your handling code here:
        rdoasAndWhenCustomer_No.setSelected(true);
        rdobill_No.setSelected(true);
        setInterestCalcMethod();
//         rdoasAndWhenCustomer_NoActionPerformed(evt);
    }//GEN-LAST:event_rdocalendarFrequency_YesActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus1.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void btnIntPayableAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntPayableAccountActionPerformed
        // TODO add your handling code here:
        popUp(INTPAYABLEACCHEAD);
    }//GEN-LAST:event_btnIntPayableAccountActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        HashMap reportParamMap = new HashMap();
        com.see.truetransact.clientutil.ttrintegration.LinkReport.getReports(getScreenID(), reportParamMap);
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnNotice_Charge_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_DeleteActionPerformed
        // TODO add your handling code here:
        if (tblNoticeCharges.getSelectedRow() >= 0) {
            int index = tblNoticeCharges.getSelectedRow();
//            index+=1;
            observable.deleteNoticeCharge(index);
            observable.resetNoticeChargeValues();
            tableNoticeChargeClicked = false;
            ClientUtil.enableDisable(panNoticeCharges, false);
            enableDisableNoticeCharge_SaveDelete(true);
        }

    }//GEN-LAST:event_btnNotice_Charge_DeleteActionPerformed

    private void btnPostageChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostageChargesActionPerformed
        // TODO add your handling code here:
        popUp(POSTAGECHARGES);
    }//GEN-LAST:event_btnPostageChargesActionPerformed

    private void btnNoticeChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoticeChargesActionPerformed
        // TODO add your handling code here:
        popUp(NOTICECHARGES);
    }//GEN-LAST:event_btnNoticeChargesActionPerformed

    private void tblNoticeChargesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNoticeChargesMousePressed
        // TODO add your handling code here:
        tableNoticeChargeClicked = true;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            ClientUtil.enableDisable(panNoticecharge_Amt, true);
            enableDisbleNotice_NewSaveDeleter(true);
        } else {
            ClientUtil.enableDisable(panNoticecharge_Amt, false);
            enableDisbleNotice_NewSaveDeleter(false);
        }
        observable.populateNoticeCharge(tblNoticeCharges.getSelectedRow());


//        tableNoticeChargeClicked=false;
//        observable.saveNoticeCharge(tableNoticeChargeClicked,tblNoticeCharges.getSelectedRow());
        //  total notice charge pan panNoticeCharges
    }//GEN-LAST:event_tblNoticeChargesMousePressed

    private void btnNotice_Charge_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_SaveActionPerformed
        // TODO add your handling code here:

        StringBuffer mandatoryMessage = new StringBuffer();
        mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panNoticeCharges).toString());//panNoticecharge_Amt
        if (mandatoryMessage.length() > 0) {
            ClientUtil.displayAlert(mandatoryMessage.toString());
            return;
        }
        ClientUtil.enableDisable(panNoticeCharges, false);
        enableDisableNoticeCharge_SaveDelete(true);
        updateOBFields();
        observable.saveNoticeCharge(tableNoticeChargeClicked, tblNoticeCharges.getSelectedRow());
        updateNoticeCharge();
        observable.resetNoticeChargeValues();
        updateNoticeCharge();
        tableNoticeChargeClicked = false;

    }//GEN-LAST:event_btnNotice_Charge_SaveActionPerformed
    private void btnCheck() {
        btnCancel.setEnabled(true);
        btnSave.setEnabled(false);
        btnNew.setEnabled(false);
        btnDelete.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        btnEdit.setEnabled(false);
    }

    private void resetValues() {
        txtPostageAmt.setText("");
        txtNoticeChargeAmt.setText("");

    }

    private void updateNoticeCharge() {
        cboNoticeType.setSelectedItem(observable.getCboNoticeType());
        cboIssueAfter.setSelectedItem(observable.getCboIssueAfter());
        txtNoticeChargeAmt.setText((String) observable.getTxtNoticeChargeAmt());
        txtPostageAmt.setText((String) observable.getTxtPostageChargeAmt());
        tblNoticeCharges.setModel(observable.getTblNoticeCharge());


    }
    private void btnNotice_Charge_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_NewActionPerformed
        ClientUtil.enableDisable(panNoticeCharges, true);
        enableDisableNoticeCharge_SaveDelete(false);


    }//GEN-LAST:event_btnNotice_Charge_NewActionPerformed

    private void cboPeriodTransDoubtfulAssets3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransDoubtfulAssets3ActionPerformed
        // TODO add your handling code here:
        if (cboPeriodTransDoubtfulAssets3.getSelectedIndex() < 1) {
            txtPeriodTransDoubtfulAssets3.setText("");
        }
    }//GEN-LAST:event_cboPeriodTransDoubtfulAssets3ActionPerformed

    private void cboPeriodTransDoubtfulAssets2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransDoubtfulAssets2ActionPerformed
        // TODO add your handling code here:
        if (cboPeriodTransDoubtfulAssets2.getSelectedIndex() < 1) {
            txtPeriodTransDoubtfulAssets2.setText("");
        }
    }//GEN-LAST:event_cboPeriodTransDoubtfulAssets2ActionPerformed

    private void btnDocumentDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocumentDeleteActionPerformed
        // TODO add your handling code here:
        if (tblDocuments.getSelectedRow() >= 0) {
            observable.deleteDocuments(tblDocuments.getSelectedRow());
            enableDisableDocuments_SaveDelete();
            tableDocumentsClicked = false;
            observable.resetDocumentsTextFields();
            enableDisablePanDocumentsFields(false);
            whenEditIsPressed(false);
            updateDocuments();
        }
    }//GEN-LAST:event_btnDocumentDeleteActionPerformed
    /**
     * Enable Disable Document Fields
     */
    public void enableDisablePanDocumentsFields(boolean flag) {
        ClientUtil.enableDisable(panDocumentFields, flag);

    }
    private void btnDocumentSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocumentSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDocumentFields);
        boolean duplicate = observable.checkDocumentType(txtProductID.getText(), CommonUtil.convertObjToStr((((ComboBoxModel) (cboDocumentType).getModel())).getKeyForSelected()), txtDocumentNo.getText(), tableDocumentsClicked, tblDocuments.getSelectedRow());
        if (duplicate) {
            // This is to check whether the product id, document type, document No is duplicated or not
            mandatoryMessage += resourceBundle.getString("DocTypeDocNoWarning");
        }
        if (observable.checkMandatoryForDocuments(CommonUtil.convertObjToStr(cboDocumentType.getSelectedItem()), CommonUtil.convertObjToStr(txtDocumentNo.getText()))) {
            // This is mandatory check for documents fields
            StringBuffer strBuf = new StringBuffer();
            strBuf.append(CommonUtil.convertObjToStr(objMandatoryRB.getString("cboDocumentType")));
            strBuf.append("\n");
            strBuf.append(CommonUtil.convertObjToStr(objMandatoryRB.getString("txtDocumentNo")));
            strBuf.append("\n");
            mandatoryMessage += CommonUtil.convertObjToStr(strBuf);
            strBuf = null;

        }

        if (mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            updateOBFields();
            observable.saveDocuments(tableDocumentsClicked, tblDocuments.getSelectedRow());
            enableDisablePanDocumentsFields(false);
            enableDisableDocuments_SaveDelete();
            whenEditIsPressed(false);

            observable.resetDocumentsTextFields();
            updateDocuments();
        }
    }//GEN-LAST:event_btnDocumentSaveActionPerformed
    /**
     * when edit button is pressed enable disable these fields
     */
    private void whenEditIsPressed(boolean flag) {
        cboDocumentType.setEnabled(flag);
        txtDocumentNo.setEditable(flag);
        txtDocumentNo.setEnabled(flag);
    }
    private void btnDocumentNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocumentNewActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.resetDocumentsTextFields();
        updateDocuments();
        tableDocumentsClicked = false;
        enableDisableDocuments_NewSaveDelete(true);
        enableDisablePanDocumentsFields(true);
        whenEditIsPressed(true);


    }//GEN-LAST:event_btnDocumentNewActionPerformed

    private void tblDocumentsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDocumentsMousePressed
        // TODO add your handling code here:
        if (tblDocuments.getSelectedRow() >= 0 && observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
//            if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){

//            }

            tableDocumentsClicked = true;// table row is clicked
            enableDisableTableDocuments(true);
            enableDisableDocuments_NewSaveDelete(true);// enable New Save Delete buttons
            enableDisablePanDocumentsFields(true);
            if (observable.populateDocuments(tblDocuments.getSelectedRow())) {
                //                enableDisablePanDocumentsFields(true);
                whenEditIsPressed(true);
            } else {
                //                enableDisablePanDocumentsFields(true);
                whenEditIsPressed(false);
                if (viewType == AUTHORIZE) {
                    btnDocumentNew.setEnabled(false);
                    btnDocumentDelete.setEnabled(false);
                    btnDocumentSave.setEnabled(false);
                    txtDocumentDesc.setEnabled(false);
                    ClientUtil.enableDisable(this, false);
                }
            }
        }
    }//GEN-LAST:event_tblDocumentsMousePressed

    private void tabLoanProductStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabLoanProductStateChanged
        // TODO add your handling code here:
        int index = tabLoanProduct.getSelectedIndex();
        if (index == ACCOUNTHEADINDEX) {
            if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                    || (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {

                int rows = observable.getRowCount();
                if (rows < 1) {
                    btnCheReturnChargest_Out.setEnabled(false);
                    txtCheReturnChargest_Out.setText("");
                    txtCheReturnChargest_Out.setEditable(false);
                    txtCheReturnChargest_Out.setEnabled(false);
//                    btnCheReturnChargest_Out.setEnabled(false);
                } else {
                    txtCheReturnChargest_Out.setEditable(true);
                    txtCheReturnChargest_Out.setEnabled(true);
                    btnCheReturnChargest_Out.setEnabled(true);
                }
            }
        }
    }//GEN-LAST:event_tabLoanProductStateChanged

    private void txtApplicableInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApplicableInterFocusLost
        // TODO add your handling code here:txtMinDebitInterRate
        String message = txtApplicableInterRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtApplicableInterFocusLost
    private String txtApplicableInterRule() {
        String message = "";

        if (!(txtApplicableInter.getText().equalsIgnoreCase("")
                || txtMaxDebitInterRate.getText().equalsIgnoreCase("")
                || txtMinDebitInterRate.getText().equalsIgnoreCase(""))) {

            if (!(txtMaxDebitInterRate.getText().equalsIgnoreCase("0")
                    || txtMinDebitInterRate.getText().equalsIgnoreCase("0"))) {
                double appInt = Double.parseDouble(txtApplicableInter.getText());
                double maxInt = Double.parseDouble(txtMaxDebitInterRate.getText());
                double minInt = Double.parseDouble(txtMinDebitInterRate.getText());

                if ((maxInt < appInt) || (appInt < minInt)) {
                    message = resourceBundle.getString("ApplicableLoanWarning");
                }
            }

        }
        return message;
    }
    private void txtMaxAmtLoanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxAmtLoanFocusLost
        // TODO add your handling code here:
        String message = txtMaxAmtLoanRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxAmtLoanFocusLost
    private String txtMaxAmtLoanRule() {
        String message = "";

        if (!(txtMaxAmtLoan.getText().equalsIgnoreCase("")
                || txtMinAmtLoan.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtMinAmtLoan.getText()) > Double.parseDouble(txtMaxAmtLoan.getText())) {
                message = resourceBundle.getString("MaxLoanWarning");
            }
        }
        return message;
    }
    private void txtMiscSerChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscSerChargesFocusLost
        // TODO add your handling code here:
        if (!(txtMiscSerCharges.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtMiscSerCharges.getText()) == 0) {
                txtMiscServCharges.setText("");
                txtMiscServCharges.setEditable(false);
                txtMiscServCharges.setEnabled(false);
                btnMiscServCharges.setEnabled(false);
            } else {
                txtMiscServCharges.setEditable(true);
                txtMiscServCharges.setEnabled(true);
                btnMiscServCharges.setEnabled(true);
            }
        } else {
            txtMiscServCharges.setText("");
            txtMiscServCharges.setEditable(false);
            txtMiscServCharges.setEnabled(false);
            btnMiscServCharges.setEnabled(false);
        }
    }//GEN-LAST:event_txtMiscSerChargesFocusLost

    private void txtAccClosingChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccClosingChargesFocusLost
        // TODO add your handling code here:
        if (!(txtAccClosingCharges.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtAccClosingCharges.getText()) == 0) {
                txtAccClosCharges.setText("");
                txtAccClosCharges.setEditable(false);
                txtAccClosCharges.setEnabled(false);
                btnAccClosCharges.setEnabled(false);
            } else {
                txtAccClosCharges.setEditable(true);
                txtAccClosCharges.setEnabled(true);
                btnAccClosCharges.setEnabled(true);
            }
        } else {
            txtAccClosCharges.setText("");
            txtAccClosCharges.setEditable(false);
            txtAccClosCharges.setEnabled(false);
            btnAccClosCharges.setEnabled(false);
        }
    }//GEN-LAST:event_txtAccClosingChargesFocusLost

    private void cboFolioChargesAppFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFolioChargesAppFreqActionPerformed
        // TODO add your handling code here:
        if ((String) cboFolioChargesAppFreq.getSelectedItem() != null && ((String) cboFolioChargesAppFreq.getSelectedItem()).length() > 0 && !((String) cboFolioChargesAppFreq.getSelectedItem()).equals("")) {
            int period = CommonUtil.convertObjToInt(((ComboBoxModel) cboFolioChargesAppFreq.getModel()).getKeyForSelected());
            computeStartProdCalc(period);
        }
    }//GEN-LAST:event_cboFolioChargesAppFreqActionPerformed
    private void computeStartProdCalc(int days) {
        Date workingDate = DateUtil.getDateMMDDYYYY(tdtLastFolioChargesAppl.getDateValue());
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(workingDate);
        calendar.add(calendar.DATE, days);

        Date nextDate = calendar.getTime();
        tdtNextFolioDDate.setDateValue(DateUtil.getStringDate(nextDate));
    }
    private void txtRangeToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRangeToFocusLost
        // TODO add your handling code here:
        String message = txtRangeToRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtRangeToFocusLost
    private String txtRangeToRule() {
        String message = "";

        if (!(txtRangeFrom.getText().equalsIgnoreCase("")
                || txtRangeTo.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtRangeFrom.getText()) >= Double.parseDouble(txtRangeTo.getText())) {
                message = resourceBundle.getString("RangeWarning");
            }
        }
        return message;
    }
    private void txtMaxDebitInterAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDebitInterAmtFocusLost
        // TODO add your handling code here:
        String message = txtMaxDebitInterAmtRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxDebitInterAmtFocusLost
    private String txtMaxDebitInterAmtRule() {
        String message = "";

        if (!(txtMinDebitInterAmt.getText().equalsIgnoreCase("")
                || txtMaxDebitInterAmt.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtMinDebitInterAmt.getText()) > Double.parseDouble(txtMaxDebitInterAmt.getText())) {
                message = resourceBundle.getString("AmountWarning");
            }
        }
        return message;
    }
    private void txtMaxDebitInterRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDebitInterRateFocusLost
        // TODO add your handling code here:
        String message = txtMaxDebitInterRateRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxDebitInterRateFocusLost
    private String txtMaxDebitInterRateRule() {
        String message = "";

        if (!(txtMinDebitInterRate.getText().equalsIgnoreCase("")
                || txtMaxDebitInterRate.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtMinDebitInterRate.getText()) > Double.parseDouble(txtMaxDebitInterRate.getText())) {
                message = resourceBundle.getString("RateWarning");
            }
        }
        return message;
    }
    private void txtProcessingChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProcessingChargesFocusLost
        // TODO add your handling code here:
        if (!(txtProcessingCharges.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtProcessingCharges, "TermLoan.getSelectAcctHeadTOList");
            btnProcessingCharges.setToolTipText(getAccHdDesc(txtProcessingCharges.getText()));
        }
    }//GEN-LAST:event_txtProcessingChargesFocusLost

    private void txtCommitChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommitChargesFocusLost
        // TODO add your handling code here:
        if (!(txtCommitCharges.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtCommitCharges, "TermLoan.getSelectAcctHeadTOList");
            btnCommitCharges.setToolTipText(getAccHdDesc(txtCommitCharges.getText()));
        }
    }//GEN-LAST:event_txtCommitChargesFocusLost

    private void txtFolioChargesAccFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFolioChargesAccFocusLost
        // TODO add your handling code here:
        if (!(txtFolioChargesAcc.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtFolioChargesAcc, "TermLoan.getSelectAcctHeadTOList");
            btnFolioChargesAcc.setToolTipText(getAccHdDesc(txtFolioChargesAcc.getText()));
        }
    }//GEN-LAST:event_txtFolioChargesAccFocusLost

    private void txtCheReturnChargest_InFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCheReturnChargest_InFocusLost
        // TODO add your handling code here:
        if (!(txtCheReturnChargest_In.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtCheReturnChargest_In, "TermLoan.getSelectAcctHeadTOList");
            btnCheReturnChargest_In.setToolTipText(getAccHdDesc(txtCheReturnChargest_In.getText()));
        }
    }//GEN-LAST:event_txtCheReturnChargest_InFocusLost

    private void txtCheReturnChargest_OutFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCheReturnChargest_OutFocusLost
        // TODO add your handling code here:
        if (!(txtCheReturnChargest_Out.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtCheReturnChargest_Out, "TermLoan.getSelectAcctHeadTOList");
            btnCheReturnChargest_Out.setToolTipText(getAccHdDesc(txtCheReturnChargest_Out.getText()));
        }
    }//GEN-LAST:event_txtCheReturnChargest_OutFocusLost

    private void txtExpiryInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExpiryInterFocusLost
        // TODO add your handling code here:
        if (!(txtExpiryInter.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtExpiryInter, "TermLoan.getSelectAcctHeadTOList");
            btnExpiryInter.setToolTipText(getAccHdDesc(txtExpiryInter.getText()));
        }
    }//GEN-LAST:event_txtExpiryInterFocusLost

    private void txtAccCreditInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccCreditInterFocusLost
        // TODO add your handling code here:
        if (!(txtAccCreditInter.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAccCreditInter, "TermLoan.getSelectAcctHeadTOList");
            btnAccCreditInter.setToolTipText(getAccHdDesc(txtAccCreditInter.getText()));
        }
    }//GEN-LAST:event_txtAccCreditInterFocusLost

    private void txtPenalInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalInterFocusLost
        // TODO add your handling code here:
        if (!(txtPenalInter.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtPenalInter, "TermLoan.getSelectAcctHeadTOList");
            btnPenalInter.setToolTipText(getAccHdDesc(txtPenalInter.getText()));
        }
    }//GEN-LAST:event_txtPenalInterFocusLost

    private void txtAccDebitInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccDebitInterFocusLost
        // TODO add your handling code here:
        if (!(txtAccDebitInter.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAccDebitInter, "TermLoan.getSelectAcctHeadTOList");
            btnAccDebitInter.setToolTipText(getAccHdDesc(txtAccDebitInter.getText()));
        }
    }//GEN-LAST:event_txtAccDebitInterFocusLost

    private void txtStatementChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStatementChargesFocusLost
        // TODO add your handling code here:
        if (!(txtStatementCharges.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtStatementCharges, "TermLoan.getSelectAcctHeadTOList");
            btnStatementCharges.setToolTipText(getAccHdDesc(txtStatementCharges.getText()));
        }
    }//GEN-LAST:event_txtStatementChargesFocusLost

    private void txtMiscServChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscServChargesFocusLost
        // TODO add your handling code here:
        if (!(txtMiscServCharges.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtMiscServCharges, "TermLoan.getSelectAcctHeadTOList");
            btnMiscServCharges.setToolTipText(getAccHdDesc(txtMiscServCharges.getText()));
        }
    }//GEN-LAST:event_txtMiscServChargesFocusLost

    private void txtAccClosChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccClosChargesFocusLost
        // TODO add your handling code here:
        if (!(txtAccClosCharges.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAccClosCharges, "TermLoan.getSelectAcctHeadTOList");
            btnAccClosCharges.setToolTipText(getAccHdDesc(txtAccClosCharges.getText()));
        }
    }//GEN-LAST:event_txtAccClosChargesFocusLost

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        //txtProductID
        
        // Added by nithya on 19/05/2016        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            boolean verify = observable.verifyProdId(txtProductID.getText());
            if (verify) {
                final NewLoanProductRB resourceBundle = new NewLoanProductRB();
                displayAlert(resourceBundle.getString("prodIdWarning"));
            } 
//            else {
//                HashMap whereMap = new HashMap();
//                whereMap.put("PROD_ID", txtProductID.getText());
//                List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
//                System.out.println("getSBODBorrowerEligAmt : " + lst);
//                if (lst != null && lst.size() > 0) {
//                    HashMap existingProdIdMap = (HashMap) lst.get(0);
//                    if (existingProdIdMap.containsKey("PROD_ID")) {
//                        ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change");
//                        txtProductID.setText("");
//                    }
//                }
//            }
        }
    }//GEN-LAST:event_txtProductIDFocusLost

    private void txtAccHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccHeadFocusLost
        // TODO add your handling code here:
        if (!(txtAccHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAccHead, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtAccHeadFocusLost

    private void cboMaxPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaxPeriodActionPerformed
        // Add your handling code here:
        if (cboMaxPeriod.getSelectedIndex() < 1) {
            txtMaxPeriod.setText("");
        } else {
            String message = cboMaxPeriodRule();
            if (message.length() > 0) {
                displayAlert(message);
            }
        }
    }//GEN-LAST:event_cboMaxPeriodActionPerformed
    private String cboMaxPeriodRule() {
        String message = "";
        if (!(txtMaxPeriod.getText().equalsIgnoreCase("")
                || txtMinPeriod.getText().equalsIgnoreCase(""))) {
            try {
                double maxPeriod = observable.setCombo(CommonUtil.convertObjToStr(((ComboBoxModel) cboMaxPeriod.getModel()).getKeyForSelected()));
                double minPeriod = observable.setCombo(CommonUtil.convertObjToStr(((ComboBoxModel) cboMinPeriod.getModel()).getKeyForSelected()));
                if ((Double.parseDouble(txtMaxPeriod.getText()) * maxPeriod) < (Double.parseDouble(txtMinPeriod.getText()) * minPeriod)) {
                    message = resourceBundle.getString("PeriodWarning");
                }
            } catch (Exception e) {
                System.out.println("Error in cboMaxPeriodRule");
            }
        }
        return message;
    }
    private void cboMinPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMinPeriodActionPerformed
        // Add your handling code here:
        if (cboMinPeriod.getSelectedIndex() < 1) {
            txtMinPeriod.setText("");
        }
    }//GEN-LAST:event_cboMinPeriodActionPerformed

    private void cboPeriodAfterWhichTransNPerformingAssetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodAfterWhichTransNPerformingAssetsActionPerformed
        // Add your handling code here:
        if (cboPeriodAfterWhichTransNPerformingAssets.getSelectedIndex() < 1) {
            txtPeriodAfterWhichTransNPerformingAssets.setText("");
        }
    }//GEN-LAST:event_cboPeriodAfterWhichTransNPerformingAssetsActionPerformed

    private void cboPeriodTransLossAssetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransLossAssetsActionPerformed
        // Add your handling code here:
        if (cboPeriodTransLossAssets.getSelectedIndex() < 1) {
            txtPeriodTransLossAssets.setText("");
        }
    }//GEN-LAST:event_cboPeriodTransLossAssetsActionPerformed

    private void cboPeriodTransDoubtfulAssets1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransDoubtfulAssets1ActionPerformed
        // Add your handling code here:
        if (cboPeriodTransDoubtfulAssets1.getSelectedIndex() < 1) {
            txtPeriodTransDoubtfulAssets1.setText("");
        }
    }//GEN-LAST:event_cboPeriodTransDoubtfulAssets1ActionPerformed

    private void cboPeriodTranSStanAssetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTranSStanAssetsActionPerformed
        // Add your handling code here:
        if (cboPeriodTranSStanAssets.getSelectedIndex() < 1) {
            txtPeriodTranSStanAssets.setText("");
        }
    }//GEN-LAST:event_cboPeriodTranSStanAssetsActionPerformed

    private void cboMinPeriodsArrearsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMinPeriodsArrearsActionPerformed
        // Add your handling code here:
        if (cboMinPeriodsArrears.getSelectedIndex() < 1) {
            txtMinPeriodsArrears.setText("");
        }
    }//GEN-LAST:event_cboMinPeriodsArrearsActionPerformed

    private void cboCharge_Limit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCharge_Limit3ActionPerformed
        // Add your handling code here:
        observable.setCboCharge_Limit3((String) cboCharge_Limit3.getSelectedItem());
        if (observable.getCboCharge_Limit3().length() > 0) {
            txtCharge_Limit3.setText("");
            txtCharge_Limit3.setEditable(true);
            txtCharge_Limit3.setEnabled(true);
            if ((observable.getCboCharge_Limit3()).equalsIgnoreCase(ABSOLUTE)) {
                txtCharge_Limit3.setMaxLength(17);
                txtCharge_Limit3.setValidation(new NumericValidation(14, 2));
            } else {
                txtCharge_Limit3.setMaxLength(6);
                txtCharge_Limit3.setValidation(new NumericValidation(3, 2));
            }
        }
    }//GEN-LAST:event_cboCharge_Limit3ActionPerformed

    private void cboCharge_Limit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCharge_Limit2ActionPerformed
        // Add your handling code here:
        observable.setCboCharge_Limit2((String) cboCharge_Limit2.getSelectedItem());
        if (observable.getCboCharge_Limit2().length() > 0) {
            txtCharge_Limit2.setText("");
            txtCharge_Limit2.setEditable(true);
            txtCharge_Limit2.setEnabled(true);
            if ((observable.getCboCharge_Limit2()).equalsIgnoreCase(ABSOLUTE)) {
                txtCharge_Limit2.setMaxLength(17);
                txtCharge_Limit2.setValidation(new NumericValidation(14, 2));
            } else {
                txtCharge_Limit2.setMaxLength(6);
                txtCharge_Limit2.setValidation(new NumericValidation(3, 2));
            }
        }
    }//GEN-LAST:event_cboCharge_Limit2ActionPerformed
                                            private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
                                                // Add your handling code here:
                                                authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
                                                                                                                                                                                                                                                                        private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
                                                                                                                                                                                                                                                                            // Add your handling code here:
                                                                                                                                                                                                                                                                            authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            // Add your handling code here:
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE) {
            viewType = AUTHORIZE;
            //__ To Save the data in the Internal Frame...
            setModified(true);

            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getLoanProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLoanProduct");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            rdoFolioChargesAppl_Yes.setEnabled(false);
            rdoFolioChargesAppl_No.setEnabled(false);
            btnDocumentNew.setEnabled(false);
            btnDocumentDelete.setEnabled(false);
            btnDocumentSave.setEnabled(false);
            ClientUtil.enableDisable(this, false);
            ClientUtil.enableDisable(panAppropriateTransaction, false);
            enableDisableAppropriateTrans(false);
            //__ If there's no data to be Authorized, call Cancel action...
            if (!isModified()) {
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
            if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)) {
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            }
            if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
                observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
            }


        } else if (viewType == AUTHORIZE && isFilled) {
            String warningMessage = tabLoanProduct.isAllTabsVisited();
            if (warningMessage.length() > 0) {
                displayAlert(warningMessage);

            } else {
                //__ To reset the value of the visited tabs...
                tabLoanProduct.resetVisits();

                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("PROD_ID", txtProductID.getText());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                final String BehavesLike = CommonUtil.convertObjToStr(((ComboBoxModel) cboOperatesLike.getModel()).getKeyForSelected());
                final int actionType = observable.getActionType();

                HashMap dataMap = new HashMap();
                dataMap.put("ACTION_TYPE", String.valueOf(actionType));
                dataMap.put("PROD_ID", CommonUtil.convertObjToStr(txtProductID.getText()));
                dataMap.put("PROD_DESC", CommonUtil.convertObjToStr(txtProductDesc.getText()));
//            dataMap.put("AGRI","AGRI");

                if (BehavesLike.equalsIgnoreCase("OD") || BehavesLike.equalsIgnoreCase("CC")) {
                    String[] options = {resourceBundle.getString("cDialogYes"), resourceBundle.getString("cDialogNo")};
                    int option = COptionPane.showOptionDialog(null, resourceBundle.getString("OD_CCALERT"), CommonConstants.INFORMATIONTITLE,
                            COptionPane.YES_NO_OPTION, COptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                    System.out.println("option: " + option);
                    if (option == 0) {    //__ Yes is selected
                        TrueTransactMain.showScreen(new AdvancesProductUI(dataMap));
                    }
                }
                ClientUtil.execute("authorizeLoanProduct", singleAuthorizeMap);
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(txtProductID.getText());
                viewType = -1;
                btnCancelActionPerformed(null);
            }
        }
    }

    private void enableDisableAppropriateTrans(boolean flag) {
        btnTransactionAdd.setEnabled(flag);
        btnTransactionRemove.setEnabled(flag);
    }
    private void rdoCommitmentCharge_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCommitmentCharge_NoActionPerformed
        // Add your handling code here:
        if (rdoCommitmentCharge_No.isSelected()) {
            ClientUtil.clearAll(panCommitCharges);
            ClientUtil.enableDisable(panCommitCharges, false);
            rdoCommitmentCharge_No.setSelected(true);
            rdoCommitmentCharge_No.setEnabled(true);
            rdoCommitmentCharge_Yes.setEnabled(true);

            btnCommitCharges.setEnabled(false);
            txtCommitCharges.setText("");
            txtCommitCharges.setEnabled(false);
            txtCommitCharges.setEditable(false);
        }
    }//GEN-LAST:event_rdoCommitmentCharge_NoActionPerformed

    private void rdoCommitmentCharge_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCommitmentCharge_YesActionPerformed
        // Add your handling code here:
        ClientUtil.clearAll(panCommitCharges);
        ClientUtil.enableDisable(panCommitCharges, true);

        btnCommitCharges.setEnabled(true);
        txtCommitCharges.setEnabled(true);
        txtCommitCharges.setEditable(true);
    }//GEN-LAST:event_rdoCommitmentCharge_YesActionPerformed

    private void rdoProcessCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoProcessCharges_NoActionPerformed
        // Add your handling code here:
        if (rdoProcessCharges_No.isSelected()) {
            ClientUtil.clearAll(panProcessCharge);
            ClientUtil.enableDisable(panProcessCharge, false);
            rdoProcessCharges_No.setSelected(true);
            rdoProcessCharges_No.setEnabled(true);
            rdoProcessCharges_Yes.setEnabled(true);

            btnProcessingCharges.setEnabled(false);
            txtProcessingCharges.setText("");
            txtProcessingCharges.setEnabled(false);
            txtProcessingCharges.setEditable(false);
        }
    }//GEN-LAST:event_rdoProcessCharges_NoActionPerformed

    private void rdoProcessCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoProcessCharges_YesActionPerformed
        // Add your handling code here:
        ClientUtil.clearAll(panProcessCharge);
        ClientUtil.enableDisable(panProcessCharge, true);

        btnProcessingCharges.setEnabled(true);
        txtProcessingCharges.setEnabled(true);
        txtProcessingCharges.setEditable(true);
    }//GEN-LAST:event_rdoProcessCharges_YesActionPerformed

    private void rdoFolioChargesAppl_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFolioChargesAppl_NoActionPerformed
        // Add your handling code here:
        if (rdoFolioChargesAppl_No.isSelected()) {
            ClientUtil.clearAll(panFolio);
            ClientUtil.enableDisable(panFolio, false);
            rdoFolioChargesAppl_No.setSelected(true);
            rdoFolioChargesAppl_No.setEnabled(true);
            rdoFolioChargesAppl_Yes.setEnabled(true);

            btnFolioChargesAcc.setEnabled(false);
            txtFolioChargesAcc.setText("");
            txtFolioChargesAcc.setEnabled(false);
            txtFolioChargesAcc.setEditable(false);
            btnFolioChargesAcc.setEnabled(false);
        }
    }//GEN-LAST:event_rdoFolioChargesAppl_NoActionPerformed

    private void rdoFolioChargesAppl_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFolioChargesAppl_YesActionPerformed
        // Add your handling code here:
        ClientUtil.clearAll(panFolio);
        ClientUtil.enableDisable(panFolio, true);
        tdtNextFolioDDate.setEnabled(false);

        btnFolioChargesAcc.setEnabled(true);
        txtFolioChargesAcc.setEnabled(true);
        txtFolioChargesAcc.setEditable(true);
    }//GEN-LAST:event_rdoFolioChargesAppl_YesActionPerformed

    private void rdoStatCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStatCharges_NoActionPerformed
        // Add your handling code here:
        if (rdoStatCharges_No.isSelected()) {
            txtStatChargesRate.setText("");
            txtStatChargesRate.setEnabled(false);
            txtStatChargesRate.setEditable(false);

//            btnStatementCharges.setEnabled(false);
            txtStatementCharges.setText("");
            txtStatementCharges.setEnabled(false);
            txtStatementCharges.setEditable(false);
            btnStatementCharges.setEnabled(false);
        }
    }//GEN-LAST:event_rdoStatCharges_NoActionPerformed

    private void rdoLimitExpiryInter_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLimitExpiryInter_NoActionPerformed
        // Add your handling code here:
        if (rdoLimitExpiryInter_No.isSelected()) {
            txtExposureLimit_Prud.setText("");
            txtExposureLimit_Prud.setEnabled(false);
            txtExposureLimit_Prud.setEditable(false);
            txtExposureLimit_Prud2.setText("");
            txtExposureLimit_Prud2.setEnabled(false);
            txtExposureLimit_Prud2.setEditable(false);
            txtExposureLimit_Policy.setText("");
            txtExposureLimit_Policy.setEnabled(false);
            txtExposureLimit_Policy.setEditable(false);
            txtExposureLimit_Policy2.setText("");
            txtExposureLimit_Policy2.setEnabled(false);
            txtExposureLimit_Policy2.setEditable(false);
        }
    }//GEN-LAST:event_rdoLimitExpiryInter_NoActionPerformed

    private void rdoLimitExpiryInter_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLimitExpiryInter_YesActionPerformed
        // Add your handling code here:
        txtExposureLimit_Prud.setEnabled(true);
        txtExposureLimit_Prud.setEditable(true);
        txtExposureLimit_Prud2.setEnabled(true);
        txtExposureLimit_Prud2.setEditable(true);
        txtExposureLimit_Policy.setEnabled(true);
        txtExposureLimit_Policy.setEditable(true);
        txtExposureLimit_Policy2.setEnabled(true);
        txtExposureLimit_Policy2.setEditable(true);
    }//GEN-LAST:event_rdoLimitExpiryInter_YesActionPerformed

    private void rdoPenalAppl_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalAppl_NoActionPerformed
        // Add your handling code here:
        if (rdoPenalAppl_No.isSelected()) {
            txtPenalInterRate.setText("");
            txtPenalInterRate.setEnabled(false);
            txtPenalInterRate.setEditable(false);

//            chkprincipalDue.setEnabled(false);
//            chkInterestDue.setEnabled(false);
//            chkprincipalDue.setSelected(false);
//            chkInterestDue.setSelected(false);

            btnPenalInter.setEnabled(false);
            txtPenalInter.setText("");
            txtPenalInter.setEnabled(false);
            txtPenalInter.setEditable(false);
        }
        setInterestCalcMethod();

    }//GEN-LAST:event_rdoPenalAppl_NoActionPerformed

    private void rdoPenalAppl_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalAppl_YesActionPerformed
        // Add your handling code here:
        txtPenalInterRate.setEnabled(true);
        txtPenalInterRate.setEditable(true);

//        chkprincipalDue.setEnabled(true);
//        chkInterestDue.setEnabled(true);

        btnPenalInter.setEnabled(true);
        txtPenalInter.setEnabled(true);
        txtPenalInter.setEditable(true);
        setInterestCalcMethod();

    }//GEN-LAST:event_rdoPenalAppl_YesActionPerformed

    private void rdoPLRRateAppl_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPLRRateAppl_NoActionPerformed
        // Add your handling code here:
        if (rdoPLRRateAppl_No.isSelected()) {
            txtPLRRate.setText("");
            txtPLRRate.setEnabled(false);
            txtPLRRate.setEditable(false);

            tdtPLRAppForm.setDateValue(null);
            tdtPLRAppForm.setEnabled(false);

            ClientUtil.clearAll(panPLRApplNewAcc);
            ClientUtil.enableDisable(panPLRApplNewAcc, false);
            /*
             * Remove, set the Value and Add the RadioButton Group...
             */
            rdoPLRApplNewAcc.remove(rdoPLRApplNewAcc_Yes);
            rdoPLRApplNewAcc.remove(rdoPLRApplNewAcc_No);
            rdoPLRApplNewAcc_Yes.setSelected(false);
            rdoPLRApplNewAcc_No.setSelected(false);
            rdoPLRApplNewAcc = new CButtonGroup();
            rdoPLRApplNewAcc.add(rdoPLRApplNewAcc_Yes);
            rdoPLRApplNewAcc.add(rdoPLRApplNewAcc_No);

            ClientUtil.clearAll(panPLRApplExistingAcc);
            ClientUtil.enableDisable(panPLRApplExistingAcc, false);
            /*
             * Remove and add the RadioButton Group...
             */
            rdoPLRApplExistingAcc.remove(rdoPLRApplExistingAcc_Yes);
            rdoPLRApplExistingAcc.remove(rdoPLRApplExistingAcc_No);
            rdoPLRApplExistingAcc_Yes.setSelected(false);
            rdoPLRApplExistingAcc_No.setSelected(false);
            rdoPLRApplExistingAcc = new CButtonGroup();
            rdoPLRApplExistingAcc.add(rdoPLRApplExistingAcc_Yes);
            rdoPLRApplExistingAcc.add(rdoPLRApplExistingAcc_No);

            tdtPlrApplAccSancForm.setDateValue(null);
            tdtPlrApplAccSancForm.setEnabled(false);
        }
    }//GEN-LAST:event_rdoPLRRateAppl_NoActionPerformed

    private void rdoPLRRateAppl_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPLRRateAppl_YesActionPerformed
        // Add your handling code here:
        txtPLRRate.setEnabled(true);
        txtPLRRate.setEditable(true);
        tdtPLRAppForm.setEnabled(true);

        ClientUtil.clearAll(panPLRApplNewAcc);
        ClientUtil.enableDisable(panPLRApplNewAcc, true);

        ClientUtil.clearAll(panPLRApplExistingAcc);
        ClientUtil.enableDisable(panPLRApplExistingAcc, true);

        tdtPlrApplAccSancForm.setEnabled(true);
    }//GEN-LAST:event_rdoPLRRateAppl_YesActionPerformed

    private void rdoldebitInterCharged_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoldebitInterCharged_NoActionPerformed
        // Add your handling code here:
        if (rdoldebitInterCharged_No.isSelected()) {
            ClientUtil.clearAll(panInterReceivable_Debit);
            ClientUtil.enableDisable(panInterReceivable_Debit, false);
            rdoldebitInterCharged_No.setSelected(true);
            rdoldebitInterCharged_No.setEnabled(true);
            rdoldebitInterCharged_Yes.setEnabled(true);

            txtAccDebitInter.setEditable(false);
            txtAccDebitInter.setEnabled(false);
            txtAccDebitInter.setText("");
            btnAccDebitInter.setEnabled(false);
        }
    }//GEN-LAST:event_rdoldebitInterCharged_NoActionPerformed

    private void rdoldebitInterCharged_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoldebitInterCharged_YesActionPerformed
        // Add your handling code here:
        ClientUtil.clearAll(panInterReceivable_Debit);
        ClientUtil.enableDisable(panInterReceivable_Debit, true);
        txtAccDebitInter.setEditable(true);
        txtAccDebitInter.setEnabled(true);
        btnAccDebitInter.setEnabled(true);
    }//GEN-LAST:event_rdoldebitInterCharged_YesActionPerformed
    // to Reset the text fields, combo boxes, etc which were enabled depending on the Value of Radio
    // buttons...

    private void txtReset() {
        ClientUtil.clearAll(panInterReceivable_Debit);
        ClientUtil.enableDisable(panInterReceivable_Debit, true);
        txtPLRRate.setEnabled(true);
        txtPLRRate.setEditable(true);
        txtPenalInterRate.setEnabled(true);
        txtPenalInterRate.setEditable(true);
        txtExposureLimit_Prud.setEnabled(true);
        txtExposureLimit_Prud.setEditable(true);
        txtExposureLimit_Prud2.setEnabled(true);
        txtExposureLimit_Prud2.setEditable(true);
        txtExposureLimit_Policy.setEnabled(true);
        txtExposureLimit_Policy.setEditable(true);
        txtExposureLimit_Policy2.setEnabled(true);
        txtExposureLimit_Policy2.setEditable(true);
        txtStatChargesRate.setEnabled(true);
        txtStatChargesRate.setEditable(true);
        ClientUtil.clearAll(panFolio);
        ClientUtil.enableDisable(panFolio, true);
        ClientUtil.clearAll(panProcessCharge);
        ClientUtil.enableDisable(panProcessCharge, true);
        ClientUtil.clearAll(panCommitCharges);
        ClientUtil.enableDisable(panCommitCharges, true);

        tdtPLRAppForm.setEnabled(true);
        ClientUtil.clearAll(panPLRApplNewAcc);
        ClientUtil.enableDisable(panPLRApplNewAcc, true);
        ClientUtil.clearAll(panPLRApplExistingAcc);
        ClientUtil.enableDisable(panPLRApplExistingAcc, true);
        tdtPlrApplAccSancForm.setEnabled(true);
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnProcessingChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessingChargesActionPerformed
        // Add your handling code here:
        popUp(PROCESSINGCHARGES);
    }//GEN-LAST:event_btnProcessingChargesActionPerformed

    private void btnCommitChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommitChargesActionPerformed
        // Add your handling code here:
        popUp(COMMITCHARGES);
    }//GEN-LAST:event_btnCommitChargesActionPerformed

    private void btnFolioChargesAccActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolioChargesAccActionPerformed
        // Add your handling code here:
        popUp(FOLIOCHARGESACC);
    }//GEN-LAST:event_btnFolioChargesAccActionPerformed

    private void btnCheReturnChargest_InActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheReturnChargest_InActionPerformed
        // Add your handling code here:
        popUp(CHERETURNCHARGES_IN);
    }//GEN-LAST:event_btnCheReturnChargest_InActionPerformed

    private void btnCheReturnChargest_OutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheReturnChargest_OutActionPerformed
        // Add your handling code here:
        popUp(CHERETURNCHARGES_OUT);
    }//GEN-LAST:event_btnCheReturnChargest_OutActionPerformed

    private void btnExpiryInterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExpiryInterActionPerformed
        // Add your handling code here:
        popUp(EXPIRYINTER);
    }//GEN-LAST:event_btnExpiryInterActionPerformed

    private void btnAccCreditInterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccCreditInterActionPerformed
        // Add your handling code here:
        popUp(ACCCREDITINTER);
    }//GEN-LAST:event_btnAccCreditInterActionPerformed

    private void btnPenalInterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenalInterActionPerformed
        // Add your handling code here:
        popUp(PENALINTER);
    }//GEN-LAST:event_btnPenalInterActionPerformed

    private void btnAccDebitInterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccDebitInterActionPerformed
        // Add your handling code here:
        popUp(ACCDEBITINTER);
    }//GEN-LAST:event_btnAccDebitInterActionPerformed

    private void btnStatementChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatementChargesActionPerformed
        // Add your handling code here:
        popUp(STATEMENTCHARGES);
    }//GEN-LAST:event_btnStatementChargesActionPerformed

    private void btnMiscServChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMiscServChargesActionPerformed
        // Add your handling code here:
        popUp(MISCSERVCHARGES);
    }//GEN-LAST:event_btnMiscServChargesActionPerformed

    private void btnAccClosChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccClosChargesActionPerformed
        // Add your handling code here:
        popUp(ACCCLOSCHARGES);
    }//GEN-LAST:event_btnAccClosChargesActionPerformed
    public void clearDesc() {
        txtAccHeadDesc.setText("");
        txtPenalWaiveoffHead.setText("");
        txtPrincipleWaiveoffHead.setText("");
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        //__ To reset the value of the visited tabs...
        clearDesc();
        tabLoanProduct.resetVisits();

        super.removeEditLock(txtProductID.getText());
        observable.resetForm();
        observable.resetTable();
        txtAccHeadDesc.setText("");
        txtMAxCashPayment.setText("");
        ClientUtil.enableDisable(panChequeReturnCharges, false);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        btnCharge_New.setEnabled(false);
        enableDisableDocuments_NewSaveDelete(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        disableButtons();
        enableDisableTextBox();
        observable.setStatus();

        isFilled = false;
        viewType = -1;

        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnCopy.setEnabled(true);
        tableNoticeChargeClicked = false;
        chkExcludeTOD.setSelected(false);
        chkShareLink.setSelected(false);
        chkExcludeScSt.setSelected(false);
        chkFixed.setSelected(false);
        txtFixedMargin.setText("");
        disableFrequencyText();
        btnCreateHead.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    // To disable the folder buttons in Account and Account Head
    // Disabled  when Delete or Cancel is done

    private void disableButtons() {
        btnAccHead.setEnabled(false);
        btnIntPayableAccount.setEnabled(false);
        btnAccClosCharges.setEnabled(false);
        btnMiscServCharges.setEnabled(false);
        btnStatementCharges.setEnabled(false);
        btnAccDebitInter.setEnabled(false);
        btnPenalInter.setEnabled(false);
        btnAccCreditInter.setEnabled(false);
        btnExpiryInter.setEnabled(false);
        btnCheReturnChargest_Out.setEnabled(false);
        btnCheReturnChargest_In.setEnabled(false);
        btnFolioChargesAcc.setEnabled(false);
        btnCommitCharges.setEnabled(false);
        btnProcessingCharges.setEnabled(false);
        btnNoticeCharges.setEnabled(false);
        btnDebitIntDiscount.setEnabled(false);
        btnPostageCharges.setEnabled(false);
        btnNoticeChargeDebitHd.setEnabled(false);
        btnSuspenseCreditAchd.setEnabled(false);
        btnPrincipleWaiveoffHead.setEnabled(false);
        btnStatementCharges.setEnabled(false);
        btnProcessingCharges.setEnabled(false);
        btnSuspenseDebitAchd.setEnabled(false);
        btnLegalCharges.setEnabled(false);
        btnInsuranceCharges.setEnabled(false);
        btnExecutionDecreeCharges.setEnabled(false);
        btnArbitraryCharges.setEnabled(false);
        btnArcCostAccount.setEnabled(false);
        btnArcExpenseAccount.setEnabled(false);
        btnEaCostAccount.setEnabled(false);
        btnEaExpenseAccount.setEnabled(false);
        btnEpCostAccount.setEnabled(false);
        btnEpExpenseAccount.setEnabled(false);
        btnRebateInterest.setEnabled(false);
        btnStampAdvancesHead.setEnabled(false);
        btnNoticeAdvancesHead.setEnabled(false);
        btnAdvertisementHead.setEnabled(false);
        btnARCEPSuspenceHead.setEnabled(false);
        btnOtherCharges.setEnabled(false);
        btnPenalWaiveoffHead.setEnabled(false);
        btnOverDueWaiverHead.setEnabled(false);
        btnOverdueIntHead.setEnabled(false);

    }

    private void enableDisableButtons() {
        btnAccHead.setEnabled(!btnAccHead.isEnabled());
        btnIntPayableAccount.setEnabled(!btnIntPayableAccount.isEnabled());
        btnAccClosCharges.setEnabled(!btnAccClosCharges.isEnabled());
        btnMiscServCharges.setEnabled(!btnMiscServCharges.isEnabled());
        btnStatementCharges.setEnabled(!btnStatementCharges.isEnabled());
        btnAccDebitInter.setEnabled(!btnAccDebitInter.isEnabled());
        btnPenalInter.setEnabled(!btnPenalInter.isEnabled());
        btnAccCreditInter.setEnabled(!btnAccCreditInter.isEnabled());
        btnExpiryInter.setEnabled(!btnExpiryInter.isEnabled());
        btnCheReturnChargest_Out.setEnabled(!btnCheReturnChargest_Out.isEnabled());
        btnCheReturnChargest_In.setEnabled(!btnCheReturnChargest_In.isEnabled());
        btnFolioChargesAcc.setEnabled(!btnFolioChargesAcc.isEnabled());
        btnCommitCharges.setEnabled(!btnCommitCharges.isEnabled());
        btnProcessingCharges.setEnabled(!btnProcessingCharges.isEnabled());
        btnNoticeCharges.setEnabled(!btnNoticeCharges.isEnabled());
        btnDebitIntDiscount.setEnabled(!btnDebitIntDiscount.isEnabled());
        btnPostageCharges.setEnabled(!btnPostageCharges.isEnabled());
        btnSuspenseDebitAchd.setEnabled(!btnSuspenseDebitAchd.isEnabled());
        btnSuspenseCreditAchd.setEnabled(!btnSuspenseCreditAchd.isEnabled());
        btnNoticeChargeDebitHd.setEnabled(!btnNoticeChargeDebitHd.isEnabled());
        btnPrincipleWaiveoffHead.setEnabled(!btnPrincipleWaiveoffHead.isEnabled());
        btnLegalCharges.setEnabled(!btnLegalCharges.isEnabled());
        btnInsuranceCharges.setEnabled(!btnInsuranceCharges.isEnabled());
        btnExecutionDecreeCharges.setEnabled(!btnExecutionDecreeCharges.isEnabled());
        btnArbitraryCharges.setEnabled(!btnArbitraryCharges.isEnabled());
        btnArcCostAccount.setEnabled(!btnArcCostAccount.isEnabled());
        btnArcExpenseAccount.setEnabled(!btnArcExpenseAccount.isEnabled());
        btnEaCostAccount.setEnabled(!btnEaCostAccount.isEnabled());
        btnEaExpenseAccount.setEnabled(!btnEaExpenseAccount.isEnabled());
        btnEpCostAccount.setEnabled(!btnEpCostAccount.isEnabled());
        btnEpExpenseAccount.setEnabled(!btnEpExpenseAccount.isEnabled());
        btnRebateInterest.setEnabled(!btnRebateInterest.isEnabled());
        btnStampAdvancesHead.setEnabled(!btnStampAdvancesHead.isEnabled());
        btnNoticeAdvancesHead.setEnabled(!btnNoticeAdvancesHead.isEnabled());
        btnAdvertisementHead.setEnabled(!btnAdvertisementHead.isEnabled());
        btnARCEPSuspenceHead.setEnabled(!btnARCEPSuspenceHead.isEnabled());
        btnOtherCharges.setEnabled(!btnOtherCharges.isEnabled());
        btnPenalWaiveoffHead.setEnabled(!btnPenalWaiveoffHead.isEnabled());
    }
    // To Make the Text Boxes associated with the folder buttons non Editable

    private void enableDisableTextBox() {
        //        txtAccHead.setEnabled(false);
        txtAccClosCharges.setEnabled(!txtAccClosCharges.isEnabled());
        txtMiscServCharges.setEnabled(!txtMiscServCharges.isEnabled());
        txtStatementCharges.setEnabled(!txtStatementCharges.isEnabled());
        txtAccDebitInter.setEnabled(!txtAccDebitInter.isEnabled());
        txtPenalInter.setEnabled(!txtPenalInter.isEnabled());
        txtAccCreditInter.setEnabled(!txtAccCreditInter.isEnabled());
        txtExpiryInter.setEnabled(!txtExpiryInter.isEnabled());
        txtCheReturnChargest_Out.setEnabled(!txtCheReturnChargest_Out.isEnabled());
        txtCheReturnChargest_In.setEnabled(!txtCheReturnChargest_In.isEnabled());
        txtFolioChargesAcc.setEnabled(!txtFolioChargesAcc.isEnabled());
        txtCommitCharges.setEnabled(!txtCommitCharges.isEnabled());
        txtProcessingCharges.setEnabled(!txtProcessingCharges.isEnabled());
        txtAccClosCharges.setEditable(!txtAccClosCharges.isEditable());
        txtMiscServCharges.setEditable(!txtMiscServCharges.isEditable());
        txtStatementCharges.setEditable(!txtStatementCharges.isEditable());
        txtAccDebitInter.setEditable(!txtAccDebitInter.isEditable());
        txtPenalInter.setEditable(!txtPenalInter.isEditable());
        txtAccCreditInter.setEditable(!txtAccCreditInter.isEditable());
        txtExpiryInter.setEditable(!txtExpiryInter.isEditable());
        txtCheReturnChargest_Out.setEditable(!txtCheReturnChargest_Out.isEditable());
        txtCheReturnChargest_In.setEditable(!txtCheReturnChargest_In.isEditable());
        txtFolioChargesAcc.setEditable(!txtFolioChargesAcc.isEditable());
        txtCommitCharges.setEditable(!txtCommitCharges.isEditable());
        txtProcessingCharges.setEditable(!txtProcessingCharges.isEditable());
        txtArcCostAccount.setEnabled(!txtArcCostAccount.isEnabled());
        txtArcExpenseAccount.setEnabled(!txtArcExpenseAccount.isEnabled());
        txtEaCostAccount.setEnabled(!txtEaCostAccount.isEnabled());
        txtEaExpenseAccount.setEnabled(!txtEaExpenseAccount.isEnabled());
        txtEpCostAccount.setEnabled(!txtEpCostAccount.isEnabled());
        txtEpExpenseAccount.setEnabled(!txtEpExpenseAccount.isEnabled());
        txtRebateInterest.setEnabled(!txtRebateInterest.isEnabled());
        txtStampAdvancesHead.setEnabled(!txtStampAdvancesHead.isEnabled());
        txtNoticeAdvancesHead.setEnabled(!txtNoticeAdvancesHead.isEnabled());
        txtDebitIntDiscount.setEnabled(!txtDebitIntDiscount.isEnabled());
        txtArcCostAccount.setEditable(!txtArcCostAccount.isEditable());
        txtArcExpenseAccount.setEditable(!txtArcExpenseAccount.isEditable());
        txtEaCostAccount.setEditable(!txtEaCostAccount.isEditable());
        txtEaExpenseAccount.setEditable(!txtEaExpenseAccount.isEditable());
        txtEpCostAccount.setEditable(!txtEpCostAccount.isEditable());
        txtEpExpenseAccount.setEditable(!txtEpExpenseAccount.isEditable());
        txtRebateInterest.setEditable(!txtRebateInterest.isEnabled());
        txtStampAdvancesHead.setEditable(!txtStampAdvancesHead.isEnabled());
        txtNoticeAdvancesHead.setEditable(!txtNoticeAdvancesHead.isEnabled());
        txtDebitIntDiscount.setEditable(!txtDebitIntDiscount.isEnabled());
        txtOverDueWaiverHead.setEditable(!txtOverDueWaiverHead.isEnabled());
        txtOverdueIntHead.setEditable(!txtOverdueIntHead.isEnabled());
               
    }

    private void enableDisableTextBox(boolean value) {
        //        txtAccHead.setEnabled(false);
        txtAccClosCharges.setEnabled(value);
        txtMiscServCharges.setEnabled(value);
        txtStatementCharges.setEnabled(value);
        txtAccDebitInter.setEnabled(value);
        txtPenalInter.setEnabled(value);
        txtAccCreditInter.setEnabled(value);
        txtExpiryInter.setEnabled(value);
        txtCheReturnChargest_Out.setEnabled(value);
        txtCheReturnChargest_In.setEnabled(value);
        txtFolioChargesAcc.setEnabled(value);
        txtCommitCharges.setEnabled(value);
        txtProcessingCharges.setEnabled(value);
        txtAccClosCharges.setEditable(value);
        txtMiscServCharges.setEditable(value);
        txtStatementCharges.setEditable(value);
        txtAccDebitInter.setEditable(value);
        txtPenalInter.setEditable(value);
        txtAccCreditInter.setEditable(value);
        txtExpiryInter.setEditable(value);
        txtCheReturnChargest_Out.setEditable(value);
        txtCheReturnChargest_In.setEditable(value);
        txtFolioChargesAcc.setEditable(value);
        txtCommitCharges.setEditable(value);
        txtProcessingCharges.setEditable(value);
        txtArcCostAccount.setEnabled(value);
        txtArcExpenseAccount.setEnabled(value);
        txtEaCostAccount.setEnabled(value);
        txtEaExpenseAccount.setEnabled(value);
        txtEpCostAccount.setEnabled(value);
        txtEpExpenseAccount.setEnabled(value);
        txtRebateInterest.setEnabled(value);
        txtStampAdvancesHead.setEnabled(value);
        txtNoticeAdvancesHead.setEnabled(value);
        txtDebitIntDiscount.setEnabled(value);
        txtOverDueWaiverHead.setEnabled(value);
        txtOverdueIntHead.setEnabled(value);

    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();
        observable.resetTable();
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        //        enableDisableTextBox();
        // to enable the Table enabled
        tblCharges.setEnabled(true);
        enableDisableTableDocuments(true);
        popUp(EDIT);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        enableDisableAppropriateTrans(true);
        enableSubsidyfromloanOpenDate(false);
        enableSubsidyloanBalance(false);
        enableDisablInterestRebateDetails(false);

    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if (field == EDIT || field == DELETE || field == VIEW || field == COPY) {//Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewLoanProduct");

        } else if (field == SUSPENSEPRODUCTCREDIT || field == SUSPENSEPRODUCTDEBIT) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectSuspenseProductTOList");
        } else {
            updateOBFields();
            viewMap.put(CommonConstants.MAP_NAME, "TermLoan.getSelectAcctHeadTOList");

        }
        new ViewAll(this, viewMap).show();
    }
    // Called Automatically when viewAll() is Called...

    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        //final String accountHead="AC_HD_ID";
        final String accountHead = (String) hash.get("AC_HD_ID");
        final String accountHeadDesc = (String) hash.get("AC_HD_DESC");
        if (viewType != -1) {
            if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW || viewType == COPY) {
                isFilled = true;
                hash.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                int prodid = CommonUtil.convertObjToInt(hash.get("PROD_ID"));

                observable.populateData(hash);
                enableDisableNoticeCharge_SaveDelete(true);
                if (CommonUtil.convertObjToStr(cboProdCategory.getSelectedItem()).equals("Gold Loan")) {
                    lblEligibleDepositAmtForLoan.setText(resourceBundle.getString("goldMargin"));
                    lblDepositRoundOff.setText(resourceBundle.getString("goldRounding"));
                    String fixed = (String) hash.get("FIXED_RATE_GOLD");
                    txtFixedMargin.setText(fixed);

                } else {
                    lblEligibleDepositAmtForLoan.setText(resourceBundle.getString("lblEligibleDepositAmtForLoan"));
                    lblDepositRoundOff.setText(resourceBundle.getString("lblDepositRoundOff"));
                }

                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                    ClientUtil.enableDisable(this, false);
                    setInvisible();
                } else {
                    ClientUtil.enableDisable(this, true);
                    ClientUtil.enableDisable(panNoticecharge_Amt, false);
                    enableDisableButtons();
                    enableDisableCharge_SaveDelete();
                    enableDisableDocuments_SaveDelete();
                    enableDisablePanDocumentsFields(false);
                    //                    enableDisableTextBox();
                    setInvisible();
                    setDisplay();
                    if (!(tdtLastInterCalcDate.getDateValue()).equalsIgnoreCase("")) {
                        tdtLastInterCalcDate.setEnabled(false);
                    }
                    if (!(tdtLastInterAppDate.getDateValue()).equalsIgnoreCase("")) {
                        tdtLastInterAppDate.setEnabled(false);
                    }

                    txtProductID.setEnabled(false);
                    cboOperatesLike.setEnabled(false);
                }


                observable.setStatus();
                setButtonEnableDisable();
                if (viewType == COPY) {
                    txtProductID.setEnabled(true);
                    txtProductID.setEditable(true);
                    cboOperatesLike.setEnabled(true);
                    cboOperatesLike.setEditable(true);
                    txtLastAccNum.setText("");
                    btnCopy.setEnabled(false);
                }
                btnCopy.setEnabled(false);
                //popUp(PENALWAIVEOFF);
            } else if (viewType == ACCHEAD) {
                txtAccHead.setText(accountHead);
                txtAccHeadDesc.setText((accountHeadDesc));
                txtAccHeadDesc.setEnabled(false);

            } else if (viewType == SUSPENSEPRODUCTCREDIT) {
                txtSuspenseCreditProductID.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            } else if (viewType == SUSPENSEPRODUCTDEBIT) {
                txtSuspenseDebitProductId.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            } else if (viewType == PENALWAIVEOFF) {
                txtPenalWaiveoffHead.setText(accountHead);
                txtAccHeadDesc.setText((accountHeadDesc));
                txtAccHeadDesc.setEnabled(false);
            } else if (viewType == PRINCIPALWAIVEOFF) {
                txtPrincipleWaiveoffHead.setText(accountHead);
                txtAccHeadDesc.setText((accountHeadDesc));
                txtAccHeadDesc.setEnabled(false);
            } else if (viewType == ACCCLOSCHARGES) {
                txtAccClosCharges.setText(accountHead);
                btnAccClosCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == NOTICEWAIVEOFF) {
                txtNoticeChargeDebitHead.setText(accountHead);
                btnNoticeChargeDebitHd.setToolTipText(accountHeadDesc);
            } else if (viewType == MISCSERVCHARGES) {
                txtMiscServCharges.setText(accountHead);
                btnMiscServCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == STATEMENTCHARGES) {
                txtStatementCharges.setText(accountHead);
                btnStatementCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == ACCDEBITINTER) {
                txtAccDebitInter.setText(accountHead);
                btnAccDebitInter.setToolTipText(accountHeadDesc);
            } else if (viewType == PENALINTER) {
                txtPenalInter.setText(accountHead);
                btnPenalInter.setToolTipText(accountHeadDesc);
            } else if (viewType == ACCCREDITINTER) {
                txtAccCreditInter.setText(accountHead);
                btnAccCreditInter.setToolTipText(accountHeadDesc);
            } else if (viewType == EXPIRYINTER) {
                txtExpiryInter.setText(accountHead);
                btnExpiryInter.setToolTipText(accountHeadDesc);
            } else if (viewType == CHERETURNCHARGES_OUT) {
                txtCheReturnChargest_Out.setText(accountHead);
                btnCheReturnChargest_Out.setToolTipText(accountHeadDesc);
            } else if (viewType == CHERETURNCHARGES_IN) {
                txtCheReturnChargest_In.setText(accountHead);
                btnCheReturnChargest_In.setToolTipText(accountHeadDesc);
            } else if (viewType == FOLIOCHARGESACC) {
                txtFolioChargesAcc.setText(accountHead);
                btnFolioChargesAcc.setToolTipText(accountHeadDesc);
            } else if (viewType == COMMITCHARGES) {
                txtCommitCharges.setText(accountHead);
                btnCommitCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == PROCESSINGCHARGES) {
                txtProcessingCharges.setText(accountHead);
                btnProcessingCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == NOTICECHARGES) {
                txtNoticeCharges.setText(accountHead);
                btnNoticeCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == POSTAGECHARGES) {
                txtPostageCharges.setText(accountHead);
                btnPostageCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == INTPAYABLEACCHEAD) {
                txtIntPayableAccount.setText(accountHead);
                btnIntPayableAccount.setToolTipText(accountHeadDesc);
            } else if (viewType == LEGALCHARGES) {
                txtLegalCharges.setText(accountHead);
                btnLegalCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == ARBITRARYCHARGES) {
                txtArbitraryCharges.setText(accountHead);
                btnArbitraryCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == OTHERCHARGES) {
                txtOtherCharges.setText(accountHead);
                btnArbitraryCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == INSURANCECHARGES) {
                txtInsuranceCharges.setText(accountHead);
                btnInsuranceCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == EXECUTION_DECREE_CHARGES) {
                txtExecutionDecreeCharges.setText(accountHead);
                btnExecutionDecreeCharges.setToolTipText(accountHeadDesc);
            } else if (viewType == ARCCOSTHEAD) {
                txtArcCostAccount.setText(accountHead);
                btnArcCostAccount.setToolTipText(accountHeadDesc);
            } else if (viewType == ARCEXPENSEHEAD) {
                txtArcExpenseAccount.setText(accountHead);
                btnArcExpenseAccount.setToolTipText(accountHeadDesc);
            } else if (viewType == EACOSTHEAD) {
                txtEaCostAccount.setText(accountHead);
                btnEaCostAccount.setToolTipText(accountHeadDesc);
            } else if (viewType == EAEXPENSEHEAD) {
                txtEaExpenseAccount.setText(accountHead);
                btnEaExpenseAccount.setToolTipText(accountHeadDesc);
            } else if (viewType == EPCOSTHEAD) {
                txtEpCostAccount.setText(accountHead);
                btnEpCostAccount.setToolTipText(accountHeadDesc);
            } else if (viewType == EPEXPENSETHEAD) {
                txtEpExpenseAccount.setText(accountHead);
                btnEpExpenseAccount.setToolTipText(accountHeadDesc);
            } else if (viewType == DEBIT_DISCOUNT_ACHD) {
                txtDebitIntDiscount.setText(accountHead);
                btnDebitIntDiscount.setToolTipText(accountHeadDesc);
            } else if (viewType == REBATEACCHEAD) {
                txtRebateInterest.setText(accountHead);
                btnRebateInterest.setToolTipText(accountHeadDesc);
            } else if (viewType == STAMPADVANCESHEAD) {
                txtStampAdvancesHead.setText(accountHead);

            } else if (viewType == ADVERTISEMENTHEAD) {
                txtAdvertisementHead.setText(accountHead);
            } else if (viewType == ARCEPSUSPENCEACHD) {
                txtARCEPSuspenceHead.setText(accountHead);
            } else if (viewType == NOTICEADVANCESHEAD) {
                System.out.println("accountHead>>>" + accountHead);
                txtNoticeAdvancesHead.setText(accountHead);
            } else if (viewType == ARCWAIVEOFF) {
                txtArcWaiverHead.setText(accountHead);
            } else if (viewType == LEGALWAIVEOFF) {
                txtLegalWaiverHead.setText(accountHead);
            } else if (viewType == ARBITARYWAIVEOFF) {
                txtArbitaryWaiveHead.setText(accountHead);
            } else if (viewType == ADVERWAIVEOFF) {
                txtAdvertiseWaiverHead.setText(accountHead);
            } else if (viewType == MISWAIVEOFF) {
                txtMiscellaneousWaiverHead.setText(accountHead);
            } else if (viewType == INSUWAIVEOFF) {
                txtInsurenceWaiverHead.setText(accountHead);
            } else if (viewType == DECREEWAIVEOFF) {
                txtDecreeWaiverHead.setText(accountHead);
            } else if (viewType == EPWAIVEOFF) {
                txtEpWaiverHead.setText(accountHead);
            } else if (viewType == POSTAGEWAIVEOFF) {
                txtPostageWaiverHead.setText(accountHead);
            } else if (viewType == OVERDUEINT) {
                txtOverdueIntHead.setText(accountHead);
            } else if (viewType == OVERDUEINTWAIVEOFF) {
                txtOverDueWaiverHead.setText(accountHead);
            } else if (viewType == RECOVERYWAIVEOFF) {
                txtRecoveryWaiverHead.setText(accountHead);
            } else if (viewType == RECOVERYCHARGE) {
                txtRecoveryChargeHead.setText(accountHead);
            } else if (viewType == MEASUREMENTWAIVEOFF) {
                txtMeasurementWaiverHead.setText(accountHead);
            } else if (viewType == MEASUREMENTCHARGE) {
                txtMeasurementChargeHead.setText(accountHead);
            } else if (viewType == KOLEFIELDOPERATIONWAIVEOFF) {
                txtKoleFieldOperationWaiverHead.setText(accountHead);
            } else if (viewType == KOLEFIELDOPERATION) {
                txtKoleFieldOperationHead.setText(accountHead);
            } else if (viewType == KOLEFIELDEXPENSEWAIVEOFF) {
                txtKoleFieldExpenseWaiverHead.setText(accountHead);
            } else if (viewType == KOLEFIELDEXPENSE) {
                txtKoleFieldExpenseHead.setText(accountHead);
            }

        }

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();//Reset all the fields in UI
        observable.resetTable();
        enableDisableCharge_SaveDelete();
        btnCharge_New.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        enableDisableTextBox();
        popUp(DELETE);
        // To Disable the Table
        tblCharges.setEnabled(false);
        enableDisableTableDocuments(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblChargesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChargesMousePressed
        // To take the Data from the Table and put it into the combo box and Text fields...
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
            caseNotDelete();
        }
    }//GEN-LAST:event_tblChargesMousePressed
    // Fills the Combo box and text field with the data in table... when
    // some row is selected...

    private void caseNotDelete() {
        updateOBFields();
        observable.populateChargesTab(tblCharges.getSelectedRow());
        ClientUtil.enableDisable(panChequeReturnCharges, true);

        txtRangeFrom.setEnabled(true);
        txtRangeFrom.setEditable(true);

        txtRangeTo.setEnabled(true);
        txtRangeTo.setEditable(true);

        txtRateAmt.setEnabled(true);
        txtRateAmt.setEditable(true);

        btnCharge_New.setEnabled(true);
        btnCharge_Save.setEnabled(true);
        btnCharge_Delete.setEnabled(true);

    }
    private void btnCharge_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCharge_DeleteActionPerformed
        // To delete the entries in the Table
        updateOBFields();
        observable.deleteChargesTab();
        ClientUtil.enableDisable(panChequeReturnCharges, false);
        enableDisableCharge_SaveDelete();
        observable.resetTab();
    }//GEN-LAST:event_btnCharge_DeleteActionPerformed

    private void btnCharge_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCharge_SaveActionPerformed
        //To enter the data into the Table from the Combo box and The Text Fields...
        //local variable on the basis of what table buttons are
        // Enabled or disabled...
        int result = 0;
        updateOBFields();
        if (txtRangeToRule().length() > 0) {
            displayAlert(txtRangeToRule());
        } else {
            result = observable.addChargesTab();
            ClientUtil.enableDisable(panChequeReturnCharges, false);
            enableDisableCharge_SaveDelete();
            if (result == 2) {
                enableDisableCharge_New();
            }
        }
    }//GEN-LAST:event_btnCharge_SaveActionPerformed
    // To Enable or Disable the fields of the Table in Charges...
    // When Save(associated with the table only) is Pressed...

    private void enableDisableCharge_SaveDelete() {
        txtRangeFrom.setText("");
        txtRangeFrom.setEnabled(false);
        txtRangeFrom.setEditable(false);

        txtRangeTo.setText("");
        txtRangeTo.setEnabled(false);
        txtRangeTo.setEditable(false);

        txtRateAmt.setText("");
        txtRateAmt.setEnabled(false);
        txtRateAmt.setEditable(false);

        btnCharge_New.setEnabled(true);
        btnCharge_Save.setEnabled(false);
        btnCharge_Delete.setEnabled(false);
    }

    // To Enable or Disable the fields of the Table in Charges...
    // when New(associated with the table only) is Pressed...
    private void enableDisableCharge_New() {
        txtRangeFrom.setEnabled(true);
        txtRangeFrom.setEditable(true);

        txtRangeTo.setEnabled(true);
        txtRangeTo.setEditable(true);

        txtRateAmt.setEnabled(true);
        txtRateAmt.setEditable(true);

        btnCharge_New.setEnabled(true);
        btnCharge_Save.setEnabled(true);
        btnCharge_Delete.setEnabled(true);
    }
    private void btnCharge_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCharge_NewActionPerformed
        // Add your handling code here:
        updateOBFields();
        observable.resetTab();
        ClientUtil.enableDisable(panChequeReturnCharges, true);
        enableDisableCharge_New();
    }//GEN-LAST:event_btnCharge_NewActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        savePerformed();
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        txtPenalWaiveoffHead.setText("");
        txtSuspenseCreditProductID.setText("");
        txtSuspenseDebitProductId.setText("");
        txtPrincipleWaiveoffHead.setText("");
        txtFixedMargin.setText("");
        //  txtMAxCashPayment.setText("");
        chkFixed.setSelected(false);
        btnNoticeChargeDebitHd.setEnabled(false);
        disableFrequencyText();
        clearDesc();
    }//GEN-LAST:event_btnSaveActionPerformed
    // Does the necessary functions when Save Button is Pressed...

    private void savePerformed() {
        updateOBFields();
        if (!(viewType == AUTHORIZE)) {
            String rebatePeriod = CommonUtil.convertObjToStr(((ComboBoxModel) cboRebatePeriod.getModel()).getKeyForSelected());
            if (rebatePeriod.length() > 0 && rdoInterestRebateAllowed_Yes.isSelected()) {
                if (rebatePeriod.equals("Financial year")) {
                    if (CommonUtil.convertObjToStr(txtFinYearStartingFromDD.getText()).equals("") || CommonUtil.convertObjToStr(txtFinYearStartingFromMM.getText()).equals("")) {
                        ClientUtil.displayAlert("Please Enter financial period DD MM ");
                        return;
                    }
                }
            }
        }
        if (chkCreditStampAdvance.isSelected() && CommonUtil.convertObjToStr(txtStampAdvancesHead.getText()).length() == 0) {
            ClientUtil.showMessageWindow("Please Enter Stamp Advances Account Head");
            return;
        }
        if (observable.isPricipalCreditToFirst()) {
            return;
        }
        if (chkCreditNoticeAdvance.isSelected() && CommonUtil.convertObjToStr(txtNoticeAdvancesHead.getText()).length() == 0) {
            ClientUtil.showMessageWindow("Please Enter Notice Advances Account Head");
            return;
        }
        if (CommonUtil.convertObjToInt(txtDepAmtMaturing.getText()) > 0 && (CommonUtil.convertObjToInt(txtDepAmtLoanMaturingPeriod.getText()) == 0) || cboDepAmtLoanMaturing.getSelectedItem() == null) {
            ClientUtil.displayAlert("In Other Terms Tab Please Enter Value for label 'Deposit maturing within next'");
            txtDepAmtLoanMaturingPeriod.requestFocus();
            return;
        }
        if (CommonUtil.convertObjToInt(txtDepAmtMaturing.getText()) == 0 && ((CommonUtil.convertObjToInt(txtDepAmtLoanMaturingPeriod.getText()) > 0) || !CommonUtil.convertObjToStr(cboDepAmtLoanMaturing.getSelectedItem()).equals(""))) {
            ClientUtil.displayAlert("In Other Terms Tab Please Enter percentage for Deposit amount where Deposit maturing within next");
            txtDepAmtMaturing.requestDefaultFocus();
            txtDepAmtMaturing.requestFocus();
            return;
        }
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        StringBuffer strBAlert = new StringBuffer();
        StringBuffer mandatoryMessage = new StringBuffer();
        mandatoryMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panLoanProduct).toString());
        if (rdoInterestRebateAllowed_Yes.isSelected()) {
//        String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panInterestRebate);
            String mandatoryMessage1 = validateRebateDetails();
            if (mandatoryMessage1 != null && mandatoryMessage1.length() > 0) {
                ClientUtil.displayAlert(mandatoryMessage1.toString());
                return;
            }

        }
        final String BehavesLike = CommonUtil.convertObjToStr(((ComboBoxModel) cboOperatesLike.getModel()).getKeyForSelected());
        if (BehavesLike.equals("OD")) {
            if (!(rdoInterestDueKeptReceivable_Yes.isSelected()
                    || rdoInterestDueKeptReceivable_No.isSelected())) {
                ClientUtil.showMessageWindow("Please Select Interest Due Keept it Recivable or Not!!! ");
                return;
            }
        }
        if (rdoSubsidy_Yes.isSelected()) {
            if (!(rdoLoanBalance_Yes.isSelected() || rdoSubsidyAdjustLoanBalance_Yes.isSelected())) {
                ClientUtil.displayAlert("Please Select Subsidy LoanBalance or Subsidy adjusted loan balance ");
                return;
            }
        }
        if (rdoSubsidyAdjustLoanBalance_Yes.isSelected()) {
            if (!(rdoSubsidyReceivedDate.isSelected() || rdoLoanOpenDate.isSelected())) {
                ClientUtil.displayAlert("Please Select Subsidy Received Date or Loan Open Date ");
                return;
            }
        }
        if (rdoinsuranceCharge_Yes.isSelected()) {
            StringBuffer buffer = new StringBuffer();
            if (!(rdosanctionDate_Yes.isSelected() || rdosanctionDate_No.isSelected())) {
                buffer.append("Please Select Insurance Market Rate AsOn" + "\n");
            }
            if (CommonUtil.convertObjToStr(txtInsuraceRate.getText()).length() == 0) {
                buffer.append("Please Select Insurance Interest Rate" + "\n");
            }
            if (buffer.length() > 0) {
                ClientUtil.showMessageWindow(buffer.toString());
                return;
            }
        }
        String mandatoryMessagess = "";
        if (rdoInterestRebateAllowed_No.isSelected()) {
            System.out.println("before replace" + mandatoryMessage.toString());
            boolean exclamation = false;
            int val = 0;
            for (int i = 0; i < mandatoryMessage.length(); i++) {
                val = mandatoryMessage.charAt(i);
                if (exclamation) {
                    System.out.println("Character value is:" + val);
                    exclamation = false;
                }
                if (mandatoryMessage.charAt(i) == '!') {
                    exclamation = true;
                }
            }
//            String newline = System.getProperty("line.separator");
//            mandatoryMessagess=(String)mandatoryMessage.toString().replaceAll("RebatePeriod should Select  proper value!"+newline+"InterestRebateCalculation should Select  proper value!"+newline,"");
//            System.out.println("After replace"+mandatoryMessage.toString() +"     "+mandatoryMessagess);
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessagess.length() > 0) {
            strBAlert.append(mandatoryMessage + "\n");
        }
        if (txtMaxDebitInterRateRule().length() > 0) {
            strBAlert.append(txtMaxDebitInterRateRule() + "\n");
        }
        if (txtMaxDebitInterAmtRule().length() > 0) {
            strBAlert.append(txtMaxDebitInterAmtRule() + "\n");
        }
        if (cboMaxPeriodRule().length() > 0) {
            strBAlert.append(cboMaxPeriodRule() + "\n");
        }
        if (txtMaxAmtLoanRule().length() > 0) {
            strBAlert.append(txtMaxAmtLoanRule() + "\n");
        }
        if (txtApplicableInterRule().length() > 0) {
            strBAlert.append(txtApplicableInterRule() + "\n");
        }
        if (checkPenalDue().length() > 0) {
            strBAlert.append(checkPenalDue() + "\n");
        }
        
        if(checkEmIPenalMethod().length() > 0){// Added by nithya on 22-03-2018 for 0008470: Property Mortgage Loan Ledger modification as per attachment
            strBAlert.append(checkEmIPenalMethod() + "\n");
        }
        
        if (txtNumPatternFollowed.getText().length() <= 0) {
            strBAlert.append(resourceBundle.getString("PREFIXWARNING"));
        }
        if (txtNumPatternFollowedSuffix.getText().length() <= 0) {
            strBAlert.append(resourceBundle.getString("SUFFIXWARNING") + "\n");
        }
        if (cboOperatesLike.getSelectedItem().equals("") || cboOperatesLike.getSelectedItem().toString() == "") {
            strBAlert.append(resourceBundle.getString("lblOperatesLike") + "\n");
        }
        if (lstAvailableTypeTransaction.getModel().getSize() > 0) {
            for (int i = 0; i < lstAvailableTypeTransaction.getModel().getSize(); i++) {
                strBAlert.append(lstAvailableTypeTransaction.getModel().getElementAt(i) + "\n");
            }
            strBAlert.append("\n" + "Should be Set as Appropriate Transaction");
        }


        //__ Testing the Length of the Combination for the Days...
        String str = "";

        str = periodLengthValidation(txtMinPeriodsArrears, cboMinPeriodsArrears);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtPeriodTranSStanAssets, cboPeriodTranSStanAssets);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtPeriodTransDoubtfulAssets1, cboPeriodTransDoubtfulAssets1);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtPeriodTransDoubtfulAssets2, cboPeriodTransDoubtfulAssets2);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtPeriodTransDoubtfulAssets3, cboPeriodTransDoubtfulAssets3);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtPeriodTransLossAssets, cboPeriodTransLossAssets);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtPeriodAfterWhichTransNPerformingAssets, cboPeriodAfterWhichTransNPerformingAssets);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtMinPeriod, cboMinPeriod);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }

        str = periodLengthValidation(txtMaxPeriod, cboMaxPeriod);
        if (str.length() > 0) {
            strBAlert.append(str + "\n");
            str = "";
        }
//        boolean verify = false;
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_COPY) {
            boolean verify = observable.verifyProdId(txtProductID.getText());
            if (verify) {
                final NewLoanProductRB resourceBundle = new NewLoanProductRB();
//                displayAlert(resourceBundle.getString("prodIdWarning"));
                strBAlert.append(resourceBundle.getString("prodIdWarning"));
            }
        }

        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0) {
            displayAlert(strBAlert.toString());
        } else {

            //__ If the Behaves like is OD or CC, Display an alert to enter the Data in the Advances...
//            final String BehavesLike = CommonUtil.convertObjToStr(((ComboBoxModel) cboOperatesLike.getModel()).getKeyForSelected());
            final int actionType = observable.getActionType();

            HashMap dataMap = new HashMap();
            dataMap.put("ACTION_TYPE", String.valueOf(actionType));
            dataMap.put("PROD_ID", CommonUtil.convertObjToStr(txtProductID.getText()));
            dataMap.put("PROD_DESC", CommonUtil.convertObjToStr(txtProductDesc.getText()));
//            dataMap.put("AGRI","AGRI");
            System.out.println("BehavesLike: " + BehavesLike);

            observable.doAction();

            //__ if the Action is not Falied, Reset the fields...
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
//                super.removeEditLock(txtProductID.getText());
                observable.setResultStatus();
                observable.resetTable();

                ClientUtil.enableDisable(this, false);
                //disableButtons();
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE) {
                    enableDisableButtons();
                    disableButtons();
                }
                btnCharge_New.setEnabled(false);
                enableDisableDocuments_NewSaveDelete(false);
                observable.resetForm();
                enableDisableTextBox();
                setButtonEnableDisable();
                enableDisableButtons();

                //__ Make the Screen Closable..
                setModified(false);

                //__ To Call the Advances Screen..
                //                if((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                //                && (BehavesLike.equalsIgnoreCase("OD") || BehavesLike.equalsIgnoreCase("CC"))){
                //                    displayAlert(resourceBundle.getString("OD_CCALERT"));
                //                }

                if ((actionType != ClientConstants.ACTIONTYPE_DELETE) && (BehavesLike.equalsIgnoreCase("OD") || BehavesLike.equalsIgnoreCase("CC"))) {
                    String[] options = {resourceBundle.getString("cDialogYes"), resourceBundle.getString("cDialogNo")};
                    int option = COptionPane.showOptionDialog(null, resourceBundle.getString("OD_CCALERT"), CommonConstants.INFORMATIONTITLE,
                            COptionPane.YES_NO_OPTION, COptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                    System.out.println("option: " + option);
                    if (option == 0) {    //__ Yes is selected
                        TrueTransactMain.showScreen(new AdvancesProductUI(dataMap));
                    }
                }
            }
        }
    }

    private String validateRebateDetails() {
        StringBuffer buffer = new StringBuffer();
        if (CommonUtil.convertObjToStr(txtInterestRebatePercentage.getText()).length() == 0) {
            buffer.append(CommonUtil.convertObjToStr(objMandatoryRB.getString("txtInterestRebatePercentage")) + "\n");
        }

        if (CommonUtil.convertObjToStr(cboInterestRebateCalculation.getSelectedItem()).length() == 0) {
            buffer.append(CommonUtil.convertObjToStr(objMandatoryRB.getString("cboInterestRebateCalculation")) + "\n");
        }

        if (CommonUtil.convertObjToStr(cboRebatePeriod.getSelectedItem()).length() == 0) {
            buffer.append(CommonUtil.convertObjToStr(objMandatoryRB.getString("cboRebatePeriod")) + "\n");
        }
        return buffer.toString();

    }

    private String checkPenalDue() {
        if (rdoPenalAppl_Yes.isSelected()) {
            if (!(chkprincipalDue.isSelected() || chkInterestDue.isSelected() || chkEmiPenal.isSelected())) {
                return "Select Penal applicable on Principal or Interest or EMI Amount";
            }
        }
        return "";
    }
    
    private String checkEmIPenalMethod() {// Added by nithya on 22-03-2018 for 0008470: Property Mortgage Loan Ledger modification as per attachment
        if (rdoPenalAppl_Yes.isSelected() && chkEmiPenal.isSelected()) {
            if (!(rdoPenalCalcDays.isSelected() || rdoPenalCalcMonths.isSelected())) {
                return "Select EMI Penal Calculation Type(Days/Months)";
            }
        }
        return "";
    }

    private String periodLengthValidation(CTextField txtField, CComboBox comboField) {
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)) {
            message = objMandatoryRB.getString(txtField.getName());
        }
        return message;
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    /*
     * To Disable those fields which are to be displayed only.
     */
    private void setDisplay() {
        txtLastAccNum.setEditable(false);
        //        tdtLastInterCalcDate.setEnabled(false);
        //        tdtLastInterAppDate.setEnabled(false);
        tdtNextFolioDDate.setEnabled(false);
    }

    /*
     * To set the fields in the Non-Per-UI as Invisible...
     */
    private void setInvisible() {
        panChequeReturnCharges.setVisible(false);

        cboProdCurrency.setVisible(false);
        lblProdCurrency.setVisible(false);

        lblMinPeriodsArrears.setVisible(false);
        panMinPeriodsArrears.setVisible(false);

        lblPeriodAfterWhichTransNPerformingAssets.setVisible(false);
        panPeriodAfterWhichTransNPerformingAssets.setVisible(false);

        /*
         * visible false only for kerala co operative bank
         */

        //    dont delete  it may use in  future
        //        lblPLRRateAppl.setVisible(false);
        //        panPLRRateAppl.setVisible(false);
        //        lblPLRRate.setVisible(false);
        //        panPLRRate.setVisible(false);
        //        lblPLRAppForm.setVisible(false);
        //        tdtPLRAppForm.setVisible(false);
        //        lblPLRApplNewAcc.setVisible(false);
        //        panPLRApplNewAcc.setVisible(false);
        //        lblPLRApplExistingAcc.setVisible(false);
        //        panPLRApplExistingAcc.setVisible(false);
        //        lblPlrApplAccSancForm.setVisible(false);
        //        tdtPlrApplAccSancForm.setVisible(false);
        //        lblLastInterCalcDate.setVisible(false);
        //        tdtLastInterCalcDate.setVisible(false);
        //        tdtLastInterAppDate.setVisible(false);
        //        lblLastInterAppDate.setVisible(false);
        //        lblMinDebitInterRate.setVisible(false);
        //        panMinDebitInterRate.setVisible(false);
        //        lblMaxDebitInterRate.setVisible(false);
        //        panMaxDebitInterRate.setVisible(false);
        //        panIsLimitDefnAllowed.setVisible(false);
        //        lblIsLimitDefnAllowed.setVisible(false);
        //        lblIsDebitInterUnderClearing.setVisible(false);
        //        panIsDebitInterUnderClearing.setVisible(false);
        //        lblCheReturnChargest_Out.setVisible(false);
        //        txtCheReturnChargest_Out.setVisible(false);
        //        btnCheReturnChargest_Out.setVisible(false);
        //        lblCheReturnChargest_In.setVisible(false);
        //        txtCheReturnChargest_In.setVisible(false);
        //        btnCheReturnChargest_In.setVisible(false);


    }

    private void enableDisableNoticeCharge_SaveDelete(boolean flag) {

        btnNotice_Charge_Delete.setEnabled(!flag);
        btnNotice_Charge_Save.setEnabled(!flag);
        btnNotice_Charge_New.setEnabled(flag);
    }

    // If the Debit interest to be charged is false... Disable fields...
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        btnPrincipleWaiveoffHead.setSelected(true);
        observable.resetForm();// reset all the fields in the Screen...
        observable.resetTable();// reset the table in Charges...
        observable.setPriorityTransDetails();
        txtReset();//reset the fields associated with Radio buttons...
        txtAccHeadDesc.setText("");
        ClientUtil.enableDisable(this, true);// Enables the panel...
        enableDisablePanDocumentsFields(false);// Disable Fields in the Documents
        enableDisableTableDocuments(true);// Enable table Documents
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        // enableButtons();// Enables the Folder buttons
        enableDisableButtons();
        enableDisableTextBox(true);
//        ClientUtil.enableDisable(panAccountHead,true);
        //        enableDisableTextBox();// Disable the Text fields associated with folder buttons...
        enableDisableCharge_SaveDelete();// Disable the Table in Charges...
        enableDisableDocuments_SaveDelete();
        ClientUtil.enableDisable(panNoticeCharges, false);
        enableDisableNoticeCharge_SaveDelete(true);
        setDisplay();
        panGroupLoan.setVisible(true);// To set some fields as Disable...
        setButtonEnableDisable();
        setInvisible();
        // To enable the Table
        tblCharges.setEnabled(true);
        observable.setStatus();

        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCopy.setEnabled(false);
        enableDisableSubsidyDetails(false);
        enableSubsidyfromloanOpenDate(false);
        enableSubsidyloanBalance(false);
        enableDisablInterestRebateDetails(false);
        enableDisableAppropriateTrans(true);
        chkExcludeTOD.setSelected(false);
        chkEmiInSimpleInterst.setSelected(false);
        chkGroupLoan.setSelected(false);
        chkShareLink.setSelected(false);
        chkExcludeScSt.setSelected(false);
        //id generation
        btnCommitCharges.setSelected(true);
        btnPenalWaiveoffHead.setEnabled(true);
        btnFolioChargesAcc.setSelected(true);
        HashMap idMap = generateID();
        if (idMap != null && idMap.size() > 0) {
            txtProductID.setText((String) idMap.get(CommonConstants.DATA));
        }
        btnPrincipleWaiveoffHead.setSelected(true);

        if (CommonConstants.SAL_REC_MODULE.equals("Y")) {
            lblSalaryRecovery.setVisible(true);
            panSalRec.setVisible(true);
            rdoSalRec_Yes.setSelected(true);
        } else {
            lblSalaryRecovery.setVisible(false);
            panSalRec.setVisible(false);
        }
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void rdoStatCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStatCharges_YesActionPerformed
        // Add your handling code here:
        txtStatChargesRate.setEnabled(true);
        txtStatChargesRate.setEditable(true);

        txtStatementCharges.setEditable(true);
        txtStatementCharges.setEnabled(true);
        btnStatementCharges.setEnabled(true);
    }//GEN-LAST:event_rdoStatCharges_YesActionPerformed

    private void btnOtherChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOtherChargesActionPerformed
        // TODO add your handling code here:
        popUp(OTHERCHARGES);
    }//GEN-LAST:event_btnOtherChargesActionPerformed

    private void chkCreditNoticeAdvanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCreditNoticeAdvanceActionPerformed
        // TODO add your handling code here:
        if (chkCreditNoticeAdvance.isSelected()) {
            setEnableNotice(true);
        } else {
            setEnableNotice(false);
            txtNoticeAdvancesHead.setText("");
        }
    }//GEN-LAST:event_chkCreditNoticeAdvanceActionPerformed

    private void setEnableNotice(boolean flag) {
        txtNoticeAdvancesHead.setEnabled(flag);
        btnNoticeAdvancesHead.setEnabled(flag);
    }

    private void txtNoticeAdvancesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNoticeAdvancesHeadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNoticeAdvancesHeadActionPerformed

    private void txtNoticeAdvancesHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeAdvancesHeadFocusLost
        // TODO add your handling code here:
        if (!(txtNoticeAdvancesHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtNoticeAdvancesHead, "TermLoan.getSelectAcctHeadTOList");
            btnNoticeAdvancesHead.setToolTipText(getAccHdDesc(txtNoticeAdvancesHead.getText()));
        }
    }//GEN-LAST:event_txtNoticeAdvancesHeadFocusLost

    private void btnNoticeAdvancesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoticeAdvancesHeadActionPerformed
        // TODO add your handling code here:
        popUp(NOTICEADVANCESHEAD);
    }//GEN-LAST:event_btnNoticeAdvancesHeadActionPerformed

private void txtPenalWaiveoffHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalWaiveoffHeadFocusLost
// TODO add your handling code here:
    if (!(txtPenalWaiveoffHead.getText().equalsIgnoreCase(""))) {
        observable.verifyAcctHead(txtPenalWaiveoffHead, "TermLoan.getSelectAcctHeadTOList");
        btnPenalWaiveoffHead.setToolTipText(getAccHdDesc(txtPenalWaiveoffHead.getText()));
    }
}//GEN-LAST:event_txtPenalWaiveoffHeadFocusLost

private void btnPenalWaiveoffHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenalWaiveoffHeadActionPerformed
// TODO add your handling code here:
    popUp(PENALWAIVEOFF);
}//GEN-LAST:event_btnPenalWaiveoffHeadActionPerformed

private void txtPrincipleWaiveoffHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrincipleWaiveoffHeadFocusLost
    if (!(txtPrincipleWaiveoffHead.getText().equalsIgnoreCase(""))) {
        observable.verifyAcctHead(txtPrincipleWaiveoffHead, "TermLoan.getSelectAcctHeadTOList");
        btnPrincipleWaiveoffHead.setToolTipText(getAccHdDesc(txtPrincipleWaiveoffHead.getText()));
    }
}//GEN-LAST:event_txtPrincipleWaiveoffHeadFocusLost

private void btnPrincipleWaiveoffHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrincipleWaiveoffHeadActionPerformed
    popUp(PRINCIPALWAIVEOFF);
}//GEN-LAST:event_btnPrincipleWaiveoffHeadActionPerformed

private void rdoInterestWaiverAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestWaiverAllowed_YesActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_rdoInterestWaiverAllowed_YesActionPerformed

private void rdoInterestWaiverAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestWaiverAllowed_NoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_rdoInterestWaiverAllowed_NoActionPerformed

private void rdoPenalInterestWaiverAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalInterestWaiverAllowed_NoActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_rdoPenalInterestWaiverAllowed_NoActionPerformed

private void txtFixedMarginFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFixedMarginFocusLost
}//GEN-LAST:event_txtFixedMarginFocusLost

private void txtNoticeChargeDebitHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeChargeDebitHeadFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtNoticeChargeDebitHeadFocusLost

private void btnNoticeChargeDebitHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoticeChargeDebitHdActionPerformed
// TODO add your handling code here:
    popUp(NOTICEWAIVEOFF);
}//GEN-LAST:event_btnNoticeChargeDebitHdActionPerformed

    private void rdoIsCreditAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsCreditAllowed_YesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoIsCreditAllowed_YesActionPerformed

    private void rdoIsCreditAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsCreditAllowed_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoIsCreditAllowed_NoActionPerformed

    private void rdoIsDisbAftMora_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsDisbAftMora_YesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoIsDisbAftMora_YesActionPerformed

private void txtMAxCashPaymentFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMAxCashPaymentFocusLost
// TODO add your handling code here:
}//GEN-LAST:event_txtMAxCashPaymentFocusLost

private void btnAccHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHeadActionPerformed
    popUp(ACCHEAD);
}//GEN-LAST:event_btnAccHeadActionPerformed

    private void txtSuspenseCreditProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuspenseCreditProductIDFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSuspenseCreditProductIDFocusLost

    private void btnSuspenseCreditAchdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspenseCreditAchdActionPerformed
        // TODO add your handling code here:
        popUp(SUSPENSEPRODUCTCREDIT);
    }//GEN-LAST:event_btnSuspenseCreditAchdActionPerformed

    private void txtSuspenseDebitProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSuspenseDebitProductIdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSuspenseDebitProductIdFocusLost

    private void btnSuspenseDebitAchdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspenseDebitAchdActionPerformed
        // TODO add your handling code here:
        popUp(SUSPENSEPRODUCTDEBIT);
    }//GEN-LAST:event_btnSuspenseDebitAchdActionPerformed

    private void chkGoldAuctionAllowedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkGoldAuctionAllowedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkGoldAuctionAllowedActionPerformed

    private void rdoIsInterestFirst_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsInterestFirst_YesActionPerformed
        // TODO add your handling code here:
        String prodCategory = "";
        prodCategory = CommonUtil.convertObjToStr(((ComboBoxModel) cboProdCategory.getModel()).getKeyForSelected());
        if (prodCategory.equals("DAILY_LOAN")) {
            lblFrequencyInDays.setVisible(true);
            txtFrequencyInDays.setVisible(true);
            txtFrequencyInDays.setAllowNumber(true);
        } else {
            disableFrequencyText();
        }
    }//GEN-LAST:event_rdoIsInterestFirst_YesActionPerformed

    private void rdoIsStaffAccOpened_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsStaffAccOpened_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoIsStaffAccOpened_NoActionPerformed

    private void rdoIsInterestFirst_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsInterestFirst_NoActionPerformed
        // TODO add your handling code here:
        disableFrequencyText();
    }//GEN-LAST:event_rdoIsInterestFirst_NoActionPerformed

    private void btnArbitaryHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArbitaryHeadActionPerformed
        popUp(ARBITARYWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnArbitaryHeadActionPerformed

    private void btnPostageWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostageWaiveHeadActionPerformed
        popUp(POSTAGEWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPostageWaiveHeadActionPerformed

    private void btnAdvertiseWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdvertiseWaiveHeadActionPerformed

        popUp(ADVERWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAdvertiseWaiveHeadActionPerformed

    private void btnMiscellaneousWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMiscellaneousWaiveHeadActionPerformed
        popUp(MISWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMiscellaneousWaiveHeadActionPerformed

    private void btnDecreeWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecreeWaiveHeadActionPerformed
        popUp(DECREEWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDecreeWaiveHeadActionPerformed

    private void btnEpWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEpWaiveHeadActionPerformed
        popUp(EPWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEpWaiveHeadActionPerformed

    private void btnInsurenceWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsurenceWaiveHeadActionPerformed

        popUp(INSUWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnInsurenceWaiveHeadActionPerformed

    private void btnLeagalWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLeagalWaiveHeadActionPerformed
        popUp(LEGALWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLeagalWaiveHeadActionPerformed

    private void btnArcWaiveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArcWaiveHeadActionPerformed
        popUp(ARCWAIVEOFF);
        // TODO add your handling code here:
    }//GEN-LAST:event_btnArcWaiveHeadActionPerformed

    private void cbocalcTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbocalcTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbocalcTypeActionPerformed
    // Added by nithya

    private String getRepaymentFrequency(String repaymentType) {
        System.out.println("Inside getRepaymentFrequency ");
        String repaymentFrequency = null;
        if (repaymentType.equalsIgnoreCase("EMI")) {
            repaymentFrequency = "Monthly";
        } else if (repaymentType.equalsIgnoreCase("EYI")) {
            repaymentFrequency = "Yearly";
        } else if (repaymentType.equalsIgnoreCase("EQI")) {
            repaymentFrequency = "Quaterly";
        } else if (repaymentType.equalsIgnoreCase("EHI")) {
            repaymentFrequency = "Half Yearly";
        } else {
            repaymentFrequency = "Monthly";
        }
        return repaymentFrequency;
    }

    private void cboRepaymentTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepaymentTypeActionPerformed
        
        System.out.println("Inside cboRepaymentTypeActionPerformed ");
        String repaymentType = cboRepaymentType.getSelectedItem().toString();
        System.out.println("cboRepaymentTypeActionPerformed :: repaymentType :" + repaymentType);
        String repaymentFrequency = getRepaymentFrequency(repaymentType);
        cboRepaymentFreq.setSelectedItem(repaymentFrequency);
        
//        System.out.println("cboRepaymentTypeActionPerformed :: repaymentFrequency :" + repaymentFrequency);
//        int repaymentFrequencyCount = cboRepaymentFreq.getItemCount();
//        System.out.println("cboRepaymentTypeActionPerformed :: repaymentFrequencyCount :" + repaymentFrequencyCount);
//        boolean foundFreq = false;
//        for (int i = 0; i < repaymentFrequencyCount; i++) {
//            if (repaymentFrequency.equalsIgnoreCase(cboRepaymentFreq.getItemAt(i).toString())) {
//                cboRepaymentFreq.setSelectedIndex(i);
//                foundFreq = true;
//                break;
//            }
//        }
//        if (foundFreq == false) {
//            System.out.println("not found");
//            cboRepaymentFreq.setSelectedIndex(2);
//        }
    }//GEN-LAST:event_cboRepaymentTypeActionPerformed

private void btnCreateHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateHeadActionPerformed
// TODO add your handling code here:
    if(txtProductDesc.getText()!=null && txtProductDesc.getText().length()>0){
        btnCreateHead.setEnabled(false);
        HashMap headMap = new HashMap();
        headMap.put("CREATE_HEAD", txtProductDesc.getText());
        headMap.put("MODUL", "L");
            try {
                observable.createHead(headMap);
            } catch (Exception ex) {
                ex.printStackTrace();
                java.util.logging.Logger.getLogger(NewLoanProductUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(observable.getActionType()==ClientConstants.ACTIONTYPE_COPY){              
                java.awt.Component[] children = panChargeTypes.getComponents();
                java.awt.Component[] children1 = panAccountHeadDetails.getComponents();
                java.awt.Component[] children2 = panCaseHead.getComponents();                
                java.awt.Component[] children3 = panSuspenseProducts.getComponents();
                
                List createdHeads = ClientUtil.executeQuery("getSelectCreatedHeads", headMap);
                if (createdHeads != null && createdHeads.size() > 0) {
                    for(int i=0;i<createdHeads.size();i++){
                        headMap = (HashMap) createdHeads.get(i);
                        for (int j = 0; j < children.length; j++) {
                            if ((children[j] != null)) {
                                if (children[j] instanceof javax.swing.JTextField) {
                                    if(((javax.swing.JTextField) children[j]).getText()!=null && ((javax.swing.JTextField) children[j]).getText().length()>0){                                    
                                    if(((javax.swing.JTextField) children[j]).getText().substring(0, 4).equalsIgnoreCase(CommonUtil.convertObjToStr(headMap.get("MJR_AC_HD_ID")))){
                                       ((javax.swing.JTextField) children[j]).setText(CommonUtil.convertObjToStr(headMap.get("AC_HD_CODE")));
                                    }
                                }
                                }
                            }
                        } 
                        for (int j = 0; j < children1.length; j++) {
                            if ((children1[j] != null)) {
                                if (children1[j] instanceof javax.swing.JTextField) {
                                    if(((javax.swing.JTextField) children1[j]).getText()!=null && ((javax.swing.JTextField) children1[j]).getText().length()>0){                                    
                                    if(((javax.swing.JTextField) children1[j]).getText().substring(0, 4).equalsIgnoreCase(CommonUtil.convertObjToStr(headMap.get("MJR_AC_HD_ID")))){
                                       ((javax.swing.JTextField) children1[j]).setText(CommonUtil.convertObjToStr(headMap.get("AC_HD_CODE")));
                                    }
                                }
                                }
                            }
                        } 
                        for (int j = 0; j < children2.length; j++) {
                            if ((children2[j] != null)) {
                                if (children2[j] instanceof javax.swing.JTextField) {
                                    if(((javax.swing.JTextField) children2[j]).getText()!=null && ((javax.swing.JTextField) children2[j]).getText().length()>0){                                    
                                    if(((javax.swing.JTextField) children2[j]).getText().substring(0, 4).equalsIgnoreCase(CommonUtil.convertObjToStr(headMap.get("MJR_AC_HD_ID")))){
                                       ((javax.swing.JTextField) children2[j]).setText(CommonUtil.convertObjToStr(headMap.get("AC_HD_CODE")));
                                    }
                                }
                                }
                            }
                        } 
                        for (int j = 0; j < children3.length; j++) {
                            if ((children3[j] != null)) {
                                if (children3[j] instanceof javax.swing.JTextField) {
                                    if(((javax.swing.JTextField) children3[j]).getText()!=null && ((javax.swing.JTextField) children3[j]).getText().length()>0){                                    
                                    if(((javax.swing.JTextField) children3[j]).getText().substring(0, 4).equalsIgnoreCase(CommonUtil.convertObjToStr(headMap.get("MJR_AC_HD_ID")))){
                                       ((javax.swing.JTextField) children3[j]).setText(CommonUtil.convertObjToStr(headMap.get("AC_HD_CODE")));
                                    }
                                }
                                }
                            }
                        } 
                    }
            }
            }
    }else{
        ClientUtil.showMessageWindow("Please fill Product Description!!!");
    }
}//GEN-LAST:event_btnCreateHeadActionPerformed

private void txtProductDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductDescActionPerformed
// TODO add your handling code here:
     if(txtProductDesc.getText()!=null && txtProductDesc.getText().length()>0)
        btnCreateHead.setEnabled(true);
}//GEN-LAST:event_txtProductDescActionPerformed

private void txtProductDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductDescFocusLost
// TODO add your handling code here:
    if(txtProductDesc.getText()!=null && txtProductDesc.getText().length()>0)
        btnCreateHead.setEnabled(true);
}//GEN-LAST:event_txtProductDescFocusLost

    private void rdoIsInterestdue_YESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsInterestdue_YESActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoIsInterestdue_YESActionPerformed

    private void txtProductDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductDescFocusGained
        // TODO add your handling code here:
        // Added by nithya : w.r.t. feedback after testing
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT) {
            HashMap whereMap = new HashMap();
            whereMap.put("PROD_ID", txtProductID.getText());
            List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
            System.out.println("getSBODBorrowerEligAmt : " + lst);
            if (lst != null && lst.size() > 0) {
                HashMap existingProdIdMap = (HashMap) lst.get(0);
                if (existingProdIdMap.containsKey("PROD_ID")) {
                    ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change Product ID first");
                    txtProductID.setText("");
                }
            }
        }
    }//GEN-LAST:event_txtProductDescFocusGained

    private void chkEmiPenalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEmiPenalActionPerformed
        // TODO add your handling code here:
        // Added by nithya on 22-03-2018 for 0008470: Property Mortgage Loan Ledger modification as per attachment
        if(chkEmiPenal.isSelected()){
            chkprincipalDue.setSelected(false);
            chkInterestDue.setSelected(false);
        }
        
    }//GEN-LAST:event_chkEmiPenalActionPerformed

    private void btnOverdueIntHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverdueIntHeadActionPerformed
        // TODO add your handling code here:
        popUp(OVERDUEINT);
    }//GEN-LAST:event_btnOverdueIntHeadActionPerformed

    private void btnOverDueWaiverHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOverDueWaiverHeadActionPerformed
        // TODO add your handling code here:
        popUp(OVERDUEINTWAIVEOFF);
    }//GEN-LAST:event_btnOverDueWaiverHeadActionPerformed

    private void rdoPenalCalcDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalCalcDaysActionPerformed
        // TODO add your handling code here:
        if(rdoPenalCalcDays.isSelected()){
            rdoPenalCalcMonths.setSelected(false);
        }
    }//GEN-LAST:event_rdoPenalCalcDaysActionPerformed

    private void rdoPenalCalcMonthsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalCalcMonthsActionPerformed
        // TODO add your handling code here:
         if(rdoPenalCalcMonths.isSelected()){
            rdoPenalCalcDays.setSelected(false);
        }
    }//GEN-LAST:event_rdoPenalCalcMonthsActionPerformed

    private void rdoOverDueWaiverAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOverDueWaiverAllowed_YesActionPerformed
        // TODO add your handling code here:
        if(rdoOverDueWaiverAllowed_Yes.isSelected()){
            rdoOverDueWaiverAllowed_No.setSelected(false);
        }
    }//GEN-LAST:event_rdoOverDueWaiverAllowed_YesActionPerformed

    private void rdoOverDueWaiverAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOverDueWaiverAllowed_NoActionPerformed
        // TODO add your handling code here:
         if(rdoOverDueWaiverAllowed_No.isSelected()){
            rdoOverDueWaiverAllowed_Yes.setSelected(false);
        }
    }//GEN-LAST:event_rdoOverDueWaiverAllowed_NoActionPerformed

    private void chkRebtSplActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRebtSplActionPerformed
        // TODO add your handling code here:
        if(chkRebtSpl.isSelected()){
            txtRebateLoanIntPercent.setEnabled(true);
            ClientUtil.showMessageWindow("Enter the percentage of loan interest \n with which the rebate to be calculated in the below text box");
            txtRebateLoanIntPercent.setText("");            
        }else{
            txtRebateLoanIntPercent.setEnabled(false);
        }
    }//GEN-LAST:event_chkRebtSplActionPerformed
    // -- End

    private void disableFrequencyText() {
        lblFrequencyInDays.setVisible(false);
        txtFrequencyInDays.setVisible(false);
        txtFrequencyInDays.setText("");
        observable.setTxtFrequencyInDays(0);
    }

    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboOperatesLike.setModel(observable.getCbmOperatesLike());
        cboProdCategory.setModel(observable.getCbmProdCategory());
        cboDebInterCalcFreq.setModel(observable.getCbmDebInterCalcFreq());
        cboDebitInterAppFreq.setModel(observable.getCbmDebitInterAppFreq());
        cboDebitInterComFreq.setModel(observable.getCbmDebitInterComFreq());
        cboDebitProdRoundOff.setModel(observable.getCbmDebitProdRoundOff());
        cboDebitInterRoundOff.setModel(observable.getCbmDebitInterRoundOff());
        cboInterestRepaymentFreq.setModel(observable.getCbmInterestRepaymentFreq());
        cboProductFreq.setModel(observable.getCbmProductFreq());
        //        cboProdCurrency.setModel(observable.getCbmProdCurrency());
        cboFolioChargesAppFreq.setModel(observable.getCbmFolioChargesAppFreq());
        cboToCollectFolioCharges.setModel(observable.getCbmToCollectFolioCharges());
        cboIncompleteFolioRoundOffFreq.setModel(observable.getCbmIncompleteFolioRoundOffFreq());
        cboMinPeriodsArrears.setModel(observable.getCbmMinPeriodsArrears());
        cboPeriodTranSStanAssets.setModel(observable.getCbmPeriodTranSStanAssets());
        cboPeriodTransDoubtfulAssets1.setModel(observable.getCbmPeriodTransDoubtfulAssets1());
        cboPeriodTransDoubtfulAssets2.setModel(observable.getCbmPeriodTransDoubtfulAssets2());
        cboPeriodTransDoubtfulAssets3.setModel(observable.getCbmPeriodTransDoubtfulAssets3());
        cboPeriodTransLossAssets.setModel(observable.getCbmPeriodTransLossAssets());
        cboPeriodAfterWhichTransNPerformingAssets.setModel(observable.getCbmPeriodAfterWhichTransNPerformingAssets());
        cbocalcType.setModel(observable.getCbmcalcType());
        cboMinPeriod.setModel(observable.getCbmMinPeriod());
        cboMaxPeriod.setModel(observable.getCbmMaxPeriod());
        cboLoanPeriodMul.setModel(observable.getCbmLoanPeriodMul());


        cboCharge_Limit2.setModel(observable.getCbmCharge_Limit2());
        cboCharge_Limit3.setModel(observable.getCbmCharge_Limit3());
        cboDocumentType.setModel(observable.getCbmDocumentType());

        cboIntPaymntDay.setModel(observable.getCbmIntCalcDay());
        cboIntPaymntMonth.setModel(observable.getCbmIntCalcMonth());
        cboholidayInt.setModel(observable.getCbmProdHoliday());


        cboCommodityCode.setModel(observable.getCbmCommodityCode());
        cboGuaranteeCoverCode.setModel(observable.getCbmGuaranteeCoverCode());
        cboSectorCode.setModel(observable.getCbmSectorCode());
        cboHealthCode.setModel(observable.getCbmHealthCode());
        cboTypeFacility.setModel(observable.getCbmTypeFacility());
        cboPurposeCode.setModel(observable.getCbmPurposeCode());
        cboIndusCode.setModel(observable.getCbmIndusCode());
        cbo20Code.setModel(observable.getCbm20Code());
        cboRefinancingInsti.setModel(observable.getCbmRefinancingInsti());
        cboSecurityDeails.setModel(observable.getCbmSecurityDeails());
        cboGovtSchemeCode.setModel(observable.getCbmGovtSchemeCode());
        cboNoticeType.setModel(observable.getCbmNoticeType());
        cboIssueAfter.setModel(observable.getCbmIssueAfter());
        cboPeriodFreq.setModel(observable.getCbmPeridFreq());
        cboPeriodFreq1.setModel(observable.getCbmPrematureIntCalcFreq());
        cboPrematureIntCalAmt.setModel(observable.getCbmPrematureIntCalAmt());
        cboReviewPeriod.setModel(observable.getCbmReviewPeriod());
        cboDepAmtLoanMaturing.setModel(observable.getCbmDepAmtMaturingPeriod());
        cboDepositRoundOff.setModel(observable.getCbmDepositRoundOff());

        cboInterestRebateCalculation.setModel(observable.getCbmInterestRebateCalculation());
        cboRebatePeriod.setModel(observable.getCbmRebatePeriod());

        //-- Added by nithya
        cboRepaymentType.setModel(observable.getCbmRepaymentType());
        cboRepaymentFreq.setModel(observable.getCbmRepaymentFreq());
        //-- End
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButtonGroup AdvertiseWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup ArcWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup DecreeWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup EpWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup InsurenceWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup InterestDueKeptRecivableGroup;
    private com.see.truetransact.uicomponent.CButtonGroup LegalWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup Miscellaneouswaivergroup;
    private com.see.truetransact.uicomponent.CPanel PanGroupProduct;
    private com.see.truetransact.uicomponent.CPanel PanWaiveHead;
    private com.see.truetransact.uicomponent.CButtonGroup PostageWaiverGroup;
    private com.see.truetransact.uicomponent.CButton btnARCEPSuspenceHead;
    private com.see.truetransact.uicomponent.CButton btnAccClosCharges;
    private com.see.truetransact.uicomponent.CButton btnAccCreditInter;
    private com.see.truetransact.uicomponent.CButton btnAccDebitInter;
    private com.see.truetransact.uicomponent.CButton btnAccHead;
    private com.see.truetransact.uicomponent.CButton btnAdvertiseWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnAdvertisementHead;
    private com.see.truetransact.uicomponent.CButton btnArbitaryHead;
    private com.see.truetransact.uicomponent.CButton btnArbitraryCharges;
    private com.see.truetransact.uicomponent.CButton btnArcCostAccount;
    private com.see.truetransact.uicomponent.CButton btnArcExpenseAccount;
    private com.see.truetransact.uicomponent.CButton btnArcWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCharge_Delete;
    private com.see.truetransact.uicomponent.CButton btnCharge_New;
    private com.see.truetransact.uicomponent.CButton btnCharge_Save;
    private com.see.truetransact.uicomponent.CButton btnCheReturnChargest_In;
    private com.see.truetransact.uicomponent.CButton btnCheReturnChargest_Out;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCommitCharges;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnCreateHead;
    private com.see.truetransact.uicomponent.CButton btnDebitIntDiscount;
    private com.see.truetransact.uicomponent.CButton btnDecreeWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDocumentDelete;
    private com.see.truetransact.uicomponent.CButton btnDocumentNew;
    private com.see.truetransact.uicomponent.CButton btnDocumentSave;
    private com.see.truetransact.uicomponent.CButton btnEaCostAccount;
    private com.see.truetransact.uicomponent.CButton btnEaExpenseAccount;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnEpCostAccount;
    private com.see.truetransact.uicomponent.CButton btnEpExpenseAccount;
    private com.see.truetransact.uicomponent.CButton btnEpWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExecutionDecreeCharges;
    private com.see.truetransact.uicomponent.CButton btnExpiryInter;
    private com.see.truetransact.uicomponent.CButton btnFolioChargesAcc;
    private com.see.truetransact.uicomponent.CButton btnInsuranceCharges;
    private com.see.truetransact.uicomponent.CButton btnInsurenceWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnIntPayableAccount;
    private com.see.truetransact.uicomponent.CButton btnLTDAdd;
    private com.see.truetransact.uicomponent.CButton btnLTDRemove;
    private com.see.truetransact.uicomponent.CButton btnLeagalWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnLegalCharges;
    private com.see.truetransact.uicomponent.CButton btnMiscServCharges;
    private com.see.truetransact.uicomponent.CButton btnMiscellaneousWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNoticeAdvancesHead;
    private com.see.truetransact.uicomponent.CButton btnNoticeChargeDebitHd;
    private com.see.truetransact.uicomponent.CButton btnNoticeCharges;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_Delete;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_New;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_Save;
    private com.see.truetransact.uicomponent.CButton btnOtherCharges;
    private com.see.truetransact.uicomponent.CButton btnOverDueWaiverHead;
    private com.see.truetransact.uicomponent.CButton btnOverdueIntHead;
    private com.see.truetransact.uicomponent.CButton btnPenalInter;
    private com.see.truetransact.uicomponent.CButton btnPenalWaiveoffHead;
    private com.see.truetransact.uicomponent.CButton btnPostageCharges;
    private com.see.truetransact.uicomponent.CButton btnPostageWaiveHead;
    private com.see.truetransact.uicomponent.CButton btnPrincipleWaiveoffHead;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcessingCharges;
    private com.see.truetransact.uicomponent.CButton btnRebateInterest;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnStampAdvancesHead;
    private com.see.truetransact.uicomponent.CButton btnStatementCharges;
    private com.see.truetransact.uicomponent.CButton btnSuspenseCreditAchd;
    private com.see.truetransact.uicomponent.CButton btnSuspenseDebitAchd;
    private com.see.truetransact.uicomponent.CButton btnTransactionAdd;
    private com.see.truetransact.uicomponent.CButton btnTransactionAdd_OTS;
    private com.see.truetransact.uicomponent.CButton btnTransactionRemove;
    private com.see.truetransact.uicomponent.CButton btnTransactionRemove_OTS;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel10;
    private com.see.truetransact.uicomponent.CLabel cLabel11;
    private com.see.truetransact.uicomponent.CLabel cLabel12;
    private com.see.truetransact.uicomponent.CLabel cLabel13;
    private com.see.truetransact.uicomponent.CLabel cLabel14;
    private com.see.truetransact.uicomponent.CLabel cLabel15;
    private com.see.truetransact.uicomponent.CLabel cLabel16;
    private com.see.truetransact.uicomponent.CLabel cLabel17;
    private com.see.truetransact.uicomponent.CLabel cLabel18;
    private com.see.truetransact.uicomponent.CLabel cLabel19;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel20;
    private com.see.truetransact.uicomponent.CLabel cLabel21;
    private com.see.truetransact.uicomponent.CLabel cLabel22;
    private com.see.truetransact.uicomponent.CLabel cLabel23;
    private com.see.truetransact.uicomponent.CLabel cLabel24;
    private com.see.truetransact.uicomponent.CLabel cLabel25;
    private com.see.truetransact.uicomponent.CLabel cLabel26;
    private com.see.truetransact.uicomponent.CLabel cLabel27;
    private com.see.truetransact.uicomponent.CLabel cLabel28;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CLabel cLabel8;
    private com.see.truetransact.uicomponent.CLabel cLabel9;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel3;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CComboBox cbo20Code;
    private com.see.truetransact.uicomponent.CComboBox cboCharge_Limit2;
    private com.see.truetransact.uicomponent.CComboBox cboCharge_Limit3;
    private com.see.truetransact.uicomponent.CComboBox cboCommodityCode;
    private com.see.truetransact.uicomponent.CComboBox cboDebInterCalcFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitInterAppFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitInterComFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitInterRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboDebitProdRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboDepAmtLoanMaturing;
    private com.see.truetransact.uicomponent.CComboBox cboDepositRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboDocumentType;
    private com.see.truetransact.uicomponent.CComboBox cboFolioChargesAppFreq;
    private com.see.truetransact.uicomponent.CComboBox cboGovtSchemeCode;
    private com.see.truetransact.uicomponent.CComboBox cboGuaranteeCoverCode;
    private com.see.truetransact.uicomponent.CComboBox cboHealthCode;
    private com.see.truetransact.uicomponent.CComboBox cboIncompleteFolioRoundOffFreq;
    private com.see.truetransact.uicomponent.CComboBox cboIndusCode;
    private com.see.truetransact.uicomponent.CComboBox cboIntPaymntDay;
    private com.see.truetransact.uicomponent.CComboBox cboIntPaymntMonth;
    private com.see.truetransact.uicomponent.CComboBox cboInterestRebateCalculation;
    private com.see.truetransact.uicomponent.CComboBox cboInterestRepaymentFreq;
    private com.see.truetransact.uicomponent.CComboBox cboIssueAfter;
    private com.see.truetransact.uicomponent.CComboBox cboLoanPeriodMul;
    private com.see.truetransact.uicomponent.CComboBox cboMaxPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboMinPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboMinPeriodsArrears;
    private com.see.truetransact.uicomponent.CComboBox cboNoticeType;
    private com.see.truetransact.uicomponent.CComboBox cboOperatesLike;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodFreq;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodFreq1;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CComboBox cboPrematureIntCalAmt;
    private com.see.truetransact.uicomponent.CComboBox cboProdCategory;
    private com.see.truetransact.uicomponent.CComboBox cboProdCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboProductFreq;
    private com.see.truetransact.uicomponent.CComboBox cboPurposeCode;
    private com.see.truetransact.uicomponent.CComboBox cboRebatePeriod;
    private com.see.truetransact.uicomponent.CComboBox cboRefinancingInsti;
    private com.see.truetransact.uicomponent.CComboBox cboRepaymentFreq;
    private com.see.truetransact.uicomponent.CComboBox cboRepaymentType;
    private com.see.truetransact.uicomponent.CComboBox cboReviewPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboSectorCode;
    private com.see.truetransact.uicomponent.CComboBox cboSecurityDeails;
    private com.see.truetransact.uicomponent.CComboBox cboToCollectFolioCharges;
    private com.see.truetransact.uicomponent.CComboBox cboTypeFacility;
    private com.see.truetransact.uicomponent.CComboBox cbocalcType;
    private com.see.truetransact.uicomponent.CComboBox cboholidayInt;
    private com.see.truetransact.uicomponent.CCheckBox chkBlockIFLimitExceed;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditNoticeAdvance;
    private com.see.truetransact.uicomponent.CCheckBox chkCreditStampAdvance;
    private com.see.truetransact.uicomponent.CCheckBox chkDirectFinance;
    private com.see.truetransact.uicomponent.CCheckBox chkECGC;
    private com.see.truetransact.uicomponent.CCheckBox chkEmiInSimpleInterst;
    private com.see.truetransact.uicomponent.CCheckBox chkEmiPenal;
    private com.see.truetransact.uicomponent.CCheckBox chkExcludeScSt;
    private com.see.truetransact.uicomponent.CCheckBox chkExcludeTOD;
    private com.see.truetransact.uicomponent.CCheckBox chkFixed;
    private com.see.truetransact.uicomponent.CCheckBox chkGldRenewCash;
    private com.see.truetransact.uicomponent.CCheckBox chkGldRenewMarketRate;
    private com.see.truetransact.uicomponent.CCheckBox chkGldRenewOldAmt;
    private com.see.truetransact.uicomponent.CCheckBox chkGldRnwOldIntRate;
    private com.see.truetransact.uicomponent.CCheckBox chkGoldAuctionAllowed;
    private com.see.truetransact.uicomponent.CCheckBox chkGoldStockPhoto;
    private com.see.truetransact.uicomponent.CCheckBox chkGroupLoan;
    private com.see.truetransact.uicomponent.CCheckBox chkIntCalcFromSanctionDt;
    private com.see.truetransact.uicomponent.CCheckBox chkInterestDue;
    private com.see.truetransact.uicomponent.CCheckBox chkInterestOnMaturity;
    private com.see.truetransact.uicomponent.CCheckBox chkIsInterestPeriodWise;
    private com.see.truetransact.uicomponent.CCheckBox chkIsOverdueInt;
    private com.see.truetransact.uicomponent.CCheckBox chkPrematureIntCalc;
    private com.see.truetransact.uicomponent.CCheckBox chkQIS;
    private com.see.truetransact.uicomponent.CCheckBox chkRebtSpl;
    private com.see.truetransact.uicomponent.CCheckBox chkShareLink;
    private com.see.truetransact.uicomponent.CCheckBox chkprincipalDue;
    private com.see.truetransact.uicomponent.CLabel lbStatementCharges;
    private com.see.truetransact.uicomponent.CLabel lbl20Code;
    private com.see.truetransact.uicomponent.CLabel lblARCEPSuspenceHead;
    private com.see.truetransact.uicomponent.CLabel lblATMcardIssued;
    private com.see.truetransact.uicomponent.CLabel lblAccClosCharges;
    private com.see.truetransact.uicomponent.CLabel lblAccClosingCharges;
    private com.see.truetransact.uicomponent.CLabel lblAccCreditInter;
    private com.see.truetransact.uicomponent.CLabel lblAccDebitInter;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblAdvertisementHead;
    private com.see.truetransact.uicomponent.CLabel lblApplicableInter;
    private com.see.truetransact.uicomponent.CLabel lblApplicableInterPer;
    private com.see.truetransact.uicomponent.CLabel lblArbitraryCharges;
    private com.see.truetransact.uicomponent.CLabel lblArcCostAccount;
    private com.see.truetransact.uicomponent.CLabel lblArcExpenseAccount;
    private com.see.truetransact.uicomponent.CLabel lblAvailableTransaction;
    private com.see.truetransact.uicomponent.CLabel lblAvailableTransaction1;
    private com.see.truetransact.uicomponent.CLabel lblBillbyBill;
    private com.see.truetransact.uicomponent.CLabel lblChargeAppliedType;
    private com.see.truetransact.uicomponent.CLabel lblCharge_1;
    private com.see.truetransact.uicomponent.CLabel lblCharge_2;
    private com.see.truetransact.uicomponent.CLabel lblCheReturnChargest_In;
    private com.see.truetransact.uicomponent.CLabel lblCheReturnChargest_Out;
    private com.see.truetransact.uicomponent.CLabel lblCollectIntOnMaturity;
    private com.see.truetransact.uicomponent.CLabel lblCollectIntTill;
    private com.see.truetransact.uicomponent.CLabel lblCollectIntTillDepositClosure;
    private com.see.truetransact.uicomponent.CLabel lblCollectIntTillDepositClosureMenu;
    private com.see.truetransact.uicomponent.CLabel lblCollectIntTillLoanClosureMenu;
    private com.see.truetransact.uicomponent.CLabel lblCommitCharges;
    private com.see.truetransact.uicomponent.CLabel lblCommitmentCharge;
    private com.see.truetransact.uicomponent.CLabel lblCommodityCode;
    private com.see.truetransact.uicomponent.CLabel lblCreditCardIssued;
    private com.see.truetransact.uicomponent.CLabel lblCreditStampAdvance;
    private com.see.truetransact.uicomponent.CLabel lblCreditStampAdvance1;
    private com.see.truetransact.uicomponent.CLabel lblDebInterCalcFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitAllowedForDueCustomer;
    private com.see.truetransact.uicomponent.CLabel lblDebitIntDiscount;
    private com.see.truetransact.uicomponent.CLabel lblDebitInterAppFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitInterComFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitInterRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDebitProdRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDepositRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDirectFinance;
    private com.see.truetransact.uicomponent.CLabel lblDocumentDesc;
    private com.see.truetransact.uicomponent.CLabel lblDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblDocumentType;
    private com.see.truetransact.uicomponent.CLabel lblECGC;
    private com.see.truetransact.uicomponent.CLabel lblEMIInSimpleInterest;
    private com.see.truetransact.uicomponent.CLabel lblEaCostAccount;
    private com.see.truetransact.uicomponent.CLabel lblEaExpenseAccount;
    private com.see.truetransact.uicomponent.CLabel lblEligibleDepositAmtForLoan;
    private com.see.truetransact.uicomponent.CLabel lblEligibleDepositAmtForLoanMaturing;
    private com.see.truetransact.uicomponent.CLabel lblEpCostAccount;
    private com.see.truetransact.uicomponent.CLabel lblEpExpenseAccount;
    private com.see.truetransact.uicomponent.CLabel lblEpExpenseAccount1;
    private com.see.truetransact.uicomponent.CLabel lblExecutionDecreeCharges;
    private com.see.truetransact.uicomponent.CLabel lblExpiryInter;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Policy;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Policy_Per;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Prud;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Prud_Per;
    private com.see.truetransact.uicomponent.CLabel lblFinancialYearstartingfrom;
    private com.see.truetransact.uicomponent.CLabel lblFixedMargin;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargesAcc;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargesAppFreq;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargesAppl;
    private com.see.truetransact.uicomponent.CLabel lblFrequencyInDays;
    private com.see.truetransact.uicomponent.CLabel lblGldRenewCash;
    private com.see.truetransact.uicomponent.CLabel lblGldRenewMarketRate;
    private com.see.truetransact.uicomponent.CLabel lblGldRenewOldAmt;
    private com.see.truetransact.uicomponent.CLabel lblGldRnwOldIntRate;
    private com.see.truetransact.uicomponent.CLabel lblGovtSchemeCode;
    private com.see.truetransact.uicomponent.CLabel lblGracePeriodPenalInterest;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeCoverCode;
    private com.see.truetransact.uicomponent.CLabel lblHealthCode;
    private com.see.truetransact.uicomponent.CLabel lblHolidayInst;
    private com.see.truetransact.uicomponent.CLabel lblIncompleteFolioRoundOffFreq;
    private com.see.truetransact.uicomponent.CLabel lblIndusCode;
    private com.see.truetransact.uicomponent.CLabel lblInsuraceRate;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceCharge;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceCharges;
    private com.see.truetransact.uicomponent.CLabel lblIntPayableAccount;
    private com.see.truetransact.uicomponent.CLabel lblInterestDueKeptRecivable;
    private com.see.truetransact.uicomponent.CLabel lblInterestRebateAllowed;
    private com.see.truetransact.uicomponent.CLabel lblInterestRebateCalculation;
    private com.see.truetransact.uicomponent.CLabel lblInterestRebatePercentage;
    private com.see.truetransact.uicomponent.CLabel lblInterestRepaymentFreq;
    private com.see.truetransact.uicomponent.CLabel lblInterestWaiverAllowed;
    private com.see.truetransact.uicomponent.CLabel lblIntrestCalcDay;
    private com.see.truetransact.uicomponent.CLabel lblIntrestCalcDay2;
    private com.see.truetransact.uicomponent.CLabel lblIntrestCalcMonth;
    private com.see.truetransact.uicomponent.CLabel lblIsAnyBranBankingAllowed;
    private com.see.truetransact.uicomponent.CLabel lblIsAuctionAmt;
    private com.see.truetransact.uicomponent.CLabel lblIsDebitInterUnderClearing;
    private com.see.truetransact.uicomponent.CLabel lblIsDisbAftMoraPerd;
    private com.see.truetransact.uicomponent.CLabel lblIsInterestFirst;
    private com.see.truetransact.uicomponent.CLabel lblIsLimitDefnAllowed;
    private com.see.truetransact.uicomponent.CLabel lblIsStaffAccOpened;
    private com.see.truetransact.uicomponent.CLabel lblIssueAfter;
    private com.see.truetransact.uicomponent.CLabel lblLastAccNum;
    private com.see.truetransact.uicomponent.CLabel lblLastFolioChargesAppl;
    private com.see.truetransact.uicomponent.CLabel lblLastInterAppDate;
    private com.see.truetransact.uicomponent.CLabel lblLastInterCalcDate;
    private com.see.truetransact.uicomponent.CLabel lblLegalCharges;
    private com.see.truetransact.uicomponent.CLabel lblLimit;
    private com.see.truetransact.uicomponent.CLabel lblLimitExpiryInter;
    private com.see.truetransact.uicomponent.CLabel lblLimits;
    private com.see.truetransact.uicomponent.CLabel lblLoanPeriodMul;
    private com.see.truetransact.uicomponent.CLabel lblLoanPeriodMul1;
    private com.see.truetransact.uicomponent.CLabel lblLoanPeriodMul2;
    private com.see.truetransact.uicomponent.CLabel lblMAxCashPayment;
    private com.see.truetransact.uicomponent.CLabel lblMarketRateAsOn;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmtLoan;
    private com.see.truetransact.uicomponent.CLabel lblMaxDebitInterAmt;
    private com.see.truetransact.uicomponent.CLabel lblMaxDebitInterRate;
    private com.see.truetransact.uicomponent.CLabel lblMaxDebitInterRate_Per;
    private com.see.truetransact.uicomponent.CLabel lblMaxPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMinAmtLoan;
    private com.see.truetransact.uicomponent.CLabel lblMinDebitInterAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinDebitInterRate;
    private com.see.truetransact.uicomponent.CLabel lblMinDebitInterRate_Per;
    private com.see.truetransact.uicomponent.CLabel lblMinInterDebited;
    private com.see.truetransact.uicomponent.CLabel lblMinPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMinPeriodsArrears;
    private com.see.truetransact.uicomponent.CLabel lblMiscSerCharges;
    private com.see.truetransact.uicomponent.CLabel lblMiscServCharges;
    private com.see.truetransact.uicomponent.CLabel lblMobileBanlingClient;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNOticeChargeDebitHead;
    private com.see.truetransact.uicomponent.CLabel lblNextFolioDDate;
    private com.see.truetransact.uicomponent.CLabel lblNoEntriesPerFolio;
    private com.see.truetransact.uicomponent.CLabel lblNoticeAdvancesHead;
    private com.see.truetransact.uicomponent.CLabel lblNoticeChargeAmt;
    private com.see.truetransact.uicomponent.CLabel lblNoticeCharges;
    private com.see.truetransact.uicomponent.CLabel lblNoticeStampAdvance;
    private com.see.truetransact.uicomponent.CLabel lblNoticeType;
    private com.see.truetransact.uicomponent.CLabel lblNoticeWaiverAllowed;
    private com.see.truetransact.uicomponent.CLabel lblNumPatternFollowed;
    private com.see.truetransact.uicomponent.CLabel lblOperatesLike;
    private com.see.truetransact.uicomponent.CLabel lblPLRAppForm;
    private com.see.truetransact.uicomponent.CLabel lblPLRApplExistingAcc;
    private com.see.truetransact.uicomponent.CLabel lblPLRApplNewAcc;
    private com.see.truetransact.uicomponent.CLabel lblPLRRate;
    private com.see.truetransact.uicomponent.CLabel lblPLRRateAppl;
    private com.see.truetransact.uicomponent.CLabel lblPLRRate_Per;
    private com.see.truetransact.uicomponent.CLabel lblPenalAppl;
    private com.see.truetransact.uicomponent.CLabel lblPenalCalcBasedOn;
    private com.see.truetransact.uicomponent.CLabel lblPenalDue;
    private com.see.truetransact.uicomponent.CLabel lblPenalInter;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterRate;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterRate_Per;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterRate_Per1;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestWaiverAllowed;
    private com.see.truetransact.uicomponent.CLabel lblPenalWaiveoffHead;
    private com.see.truetransact.uicomponent.CLabel lblPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CLabel lblPlrApplAccSancForm;
    private com.see.truetransact.uicomponent.CLabel lblPostageAmt;
    private com.see.truetransact.uicomponent.CLabel lblPostageCharges;
    private com.see.truetransact.uicomponent.CLabel lblPrematureCloserMinCalcPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPrematureCloserMinIntCalcPeriod1;
    private com.see.truetransact.uicomponent.CLabel lblPrematureCloserMinIntCalcPeriod2;
    private com.see.truetransact.uicomponent.CLabel lblPrematureIntCalcAmt;
    private com.see.truetransact.uicomponent.CLabel lblPrincipalWaiverAllowed;
    private com.see.truetransact.uicomponent.CLabel lblPrincipleWaiveoffHead;
    private com.see.truetransact.uicomponent.CLabel lblProcessCharges;
    private com.see.truetransact.uicomponent.CLabel lblProcessingCharges;
    private com.see.truetransact.uicomponent.CLabel lblProdCategory;
    private com.see.truetransact.uicomponent.CLabel lblProdCurrency;
    private com.see.truetransact.uicomponent.CLabel lblProductDesc;
    private com.see.truetransact.uicomponent.CLabel lblProductFreq;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProvisionDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CLabel lblProvisionDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CLabel lblProvisionDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CLabel lblProvisionDoubtfulAssetsPer1;
    private com.see.truetransact.uicomponent.CLabel lblProvisionDoubtfulAssetsPer2;
    private com.see.truetransact.uicomponent.CLabel lblProvisionDoubtfulAssetsPer3;
    private com.see.truetransact.uicomponent.CLabel lblProvisionLossAssets;
    private com.see.truetransact.uicomponent.CLabel lblProvisionLossAssetsPer;
    private com.see.truetransact.uicomponent.CLabel lblProvisionStandardAssetss;
    private com.see.truetransact.uicomponent.CLabel lblProvisionStandardAssetssPer;
    private com.see.truetransact.uicomponent.CLabel lblProvisionsStanAssets;
    private com.see.truetransact.uicomponent.CLabel lblProvisionsStanAssetsPer;
    private com.see.truetransact.uicomponent.CLabel lblPurposeCode;
    private com.see.truetransact.uicomponent.CLabel lblQIS;
    private com.see.truetransact.uicomponent.CLabel lblRangeFrom;
    private com.see.truetransact.uicomponent.CLabel lblRangeTo;
    private com.see.truetransact.uicomponent.CLabel lblRateAmt;
    private com.see.truetransact.uicomponent.CLabel lblRatePerFolio;
    private com.see.truetransact.uicomponent.CLabel lblRebatePeriod;
    private com.see.truetransact.uicomponent.CLabel lblRebateSpl;
    private com.see.truetransact.uicomponent.CLabel lblRefinancingInsti;
    private com.see.truetransact.uicomponent.CLabel lblReviewPeriodLoan;
    private com.see.truetransact.uicomponent.CLabel lblSalaryRecovery;
    private com.see.truetransact.uicomponent.CLabel lblSectorCode;
    private com.see.truetransact.uicomponent.CLabel lblSecurityDeails;
    private com.see.truetransact.uicomponent.CLabel lblSelectedTransaction;
    private com.see.truetransact.uicomponent.CLabel lblSelectedTransaction1;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblStampAdvancesHead;
    private com.see.truetransact.uicomponent.CLabel lblStatCharges;
    private com.see.truetransact.uicomponent.CLabel lblStatChargesRate;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblSubsidy;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyInterestCalculatedOn;
    private com.see.truetransact.uicomponent.CLabel lblSunbsidy;
    private com.see.truetransact.uicomponent.CLabel lblSunbsidy1;
    private com.see.truetransact.uicomponent.CLabel lblSunbsidy2;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseCreditAchd;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseDebitAchd;
    private com.see.truetransact.uicomponent.CLabel lblToChargeOn;
    private com.see.truetransact.uicomponent.CLabel lblToCollectFolioCharges;
    private com.see.truetransact.uicomponent.CLabel lblTypeFacility;
    private com.see.truetransact.uicomponent.CLabel lblWhetherCredit;
    private com.see.truetransact.uicomponent.CLabel lblasAndWhenCustomer;
    private com.see.truetransact.uicomponent.CLabel lblcalcType;
    private com.see.truetransact.uicomponent.CLabel lblcalendar;
    private com.see.truetransact.uicomponent.CLabel lbldebitInterCharged;
    private com.see.truetransact.uicomponent.CList lstAvailableDeposits;
    private com.see.truetransact.uicomponent.CList lstAvailableTypeTransaction;
    private com.see.truetransact.uicomponent.CList lstAvailableTypeTransaction_OTS;
    private com.see.truetransact.uicomponent.CList lstSelectedDeposits;
    private com.see.truetransact.uicomponent.CList lstSelectedPriorityTransaction;
    private com.see.truetransact.uicomponent.CList lstSelectedPriorityTransaction_OTS;
    private com.see.truetransact.uicomponent.CMenuBar mbrLoanProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAccCharges;
    private com.see.truetransact.uicomponent.CPanel panAccount;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAccountHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panApplicableInter;
    private com.see.truetransact.uicomponent.CPanel panAppropriateRepaymentOTS;
    private com.see.truetransact.uicomponent.CPanel panAppropriateTransaction;
    private com.see.truetransact.uicomponent.CPanel panBankSharePremium1;
    private com.see.truetransact.uicomponent.CPanel panBillbyBill;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCalendarFrequency;
    private com.see.truetransact.uicomponent.CPanel panCaseHead;
    private com.see.truetransact.uicomponent.CPanel panCharge;
    private com.see.truetransact.uicomponent.CPanel panChargeTypes;
    private com.see.truetransact.uicomponent.CPanel panCharge_1;
    private com.see.truetransact.uicomponent.CPanel panCharge_2;
    private com.see.truetransact.uicomponent.CPanel panCharge_ProcCommit;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panCharges_Table;
    private com.see.truetransact.uicomponent.CPanel panChequeReturnCharges;
    private com.see.truetransact.uicomponent.CPanel panClassDetails_Details;
    private com.see.truetransact.uicomponent.CPanel panCode;
    private com.see.truetransact.uicomponent.CPanel panCode2;
    private com.see.truetransact.uicomponent.CPanel panCommitCharges;
    private com.see.truetransact.uicomponent.CPanel panCommitmentCharge;
    private com.see.truetransact.uicomponent.CPanel panCreditPenalFirst1;
    private com.see.truetransact.uicomponent.CPanel panDIsbAftMoraPeriod;
    private com.see.truetransact.uicomponent.CPanel panDocumentFields;
    private com.see.truetransact.uicomponent.CPanel panDocumentTable;
    private com.see.truetransact.uicomponent.CPanel panDocuments;
    private com.see.truetransact.uicomponent.CPanel panEmiCalc;
    private com.see.truetransact.uicomponent.CPanel panExposureLimit_Policy;
    private com.see.truetransact.uicomponent.CPanel panExposureLimit_Prud;
    private com.see.truetransact.uicomponent.CPanel panFolio;
    private com.see.truetransact.uicomponent.CPanel panFolioChargesAppl;
    private com.see.truetransact.uicomponent.CPanel panFolio_Date;
    private com.see.truetransact.uicomponent.CPanel panFolio_Freq;
    private com.see.truetransact.uicomponent.CPanel panGldRenewal;
    private com.see.truetransact.uicomponent.CPanel panGroupLoan;
    private com.see.truetransact.uicomponent.CPanel panInsuranceChargeApplicable;
    private com.see.truetransact.uicomponent.CPanel panInterCalculation;
    private com.see.truetransact.uicomponent.CPanel panInterReceivable;
    private com.see.truetransact.uicomponent.CPanel panInterReceivable_Debit;
    private com.see.truetransact.uicomponent.CPanel panInterReceivable_PLR;
    private com.see.truetransact.uicomponent.CPanel panInterestDueKeptRecivable;
    private com.see.truetransact.uicomponent.CPanel panInterestDueKeptRecivable1;
    private com.see.truetransact.uicomponent.CPanel panInterestRebate;
    private com.see.truetransact.uicomponent.CPanel panIsAuctionAmt;
    private com.see.truetransact.uicomponent.CPanel panIsAuctionAmt2;
    private com.see.truetransact.uicomponent.CPanel panIsDebitInterUnderClearing;
    private com.see.truetransact.uicomponent.CPanel panIsInterestFirst;
    private com.see.truetransact.uicomponent.CPanel panIsLimitDefnAllowed;
    private com.see.truetransact.uicomponent.CPanel panIsLimitDefnAllowed1;
    private com.see.truetransact.uicomponent.CPanel panIsStaffAccOpened;
    private com.see.truetransact.uicomponent.CPanel panLTDInterest;
    private com.see.truetransact.uicomponent.CPanel panLTDInterest1;
    private com.see.truetransact.uicomponent.CPanel panLimitExpiryInter;
    private com.see.truetransact.uicomponent.CPanel panLoanAgainstDeposit;
    private com.see.truetransact.uicomponent.CPanel panLoanProduct;
    private com.see.truetransact.uicomponent.CPanel panMaxDebitInterRate;
    private com.see.truetransact.uicomponent.CPanel panMaxPeriod;
    private com.see.truetransact.uicomponent.CPanel panMinDebitInterRate;
    private com.see.truetransact.uicomponent.CPanel panMinInterDebited;
    private com.see.truetransact.uicomponent.CPanel panMinInterDebited1;
    private com.see.truetransact.uicomponent.CPanel panMinPeriod;
    private com.see.truetransact.uicomponent.CPanel panMinPeriod1;
    private com.see.truetransact.uicomponent.CPanel panMinPeriod2;
    private com.see.truetransact.uicomponent.CPanel panMinPeriodsArrears;
    private com.see.truetransact.uicomponent.CPanel panNonPerAssets;
    private com.see.truetransact.uicomponent.CPanel panNoticeButton;
    private com.see.truetransact.uicomponent.CPanel panNoticeCharges;
    private com.see.truetransact.uicomponent.CPanel panNoticeCharges_Table;
    private com.see.truetransact.uicomponent.CPanel panNoticecharge_Amt;
    private com.see.truetransact.uicomponent.CPanel panPLRApplExistingAcc;
    private com.see.truetransact.uicomponent.CPanel panPLRApplNewAcc;
    private com.see.truetransact.uicomponent.CPanel panPLRRate;
    private com.see.truetransact.uicomponent.CPanel panPLRRateAppl;
    private com.see.truetransact.uicomponent.CPanel panPenalAppl;
    private com.see.truetransact.uicomponent.CPanel panPenalAppl_Due;
    private com.see.truetransact.uicomponent.CPanel panPenalInterRate;
    private com.see.truetransact.uicomponent.CPanel panPenalInterRate1;
    private com.see.truetransact.uicomponent.CPanel panPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CPanel panPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CPanel panPreMatIntCalcPeriod;
    private com.see.truetransact.uicomponent.CPanel panPreMatIntCalcPeriod1;
    private com.see.truetransact.uicomponent.CPanel panProcessCharge;
    private com.see.truetransact.uicomponent.CPanel panProcessCharges;
    private com.see.truetransact.uicomponent.CPanel panProdCatg;
    private com.see.truetransact.uicomponent.CPanel panProdDetails;
    private com.see.truetransact.uicomponent.CPanel panProdExtraFeatures;
    private com.see.truetransact.uicomponent.CPanel panProdSettings;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panSalRec;
    private com.see.truetransact.uicomponent.CPanel panSpeItems;
    private com.see.truetransact.uicomponent.CPanel panSpeItems1;
    private com.see.truetransact.uicomponent.CPanel panSpecial_NonPerfoormingAssets;
    private com.see.truetransact.uicomponent.CPanel panSpecial_NonPerfoormingAssets1;
    private com.see.truetransact.uicomponent.CPanel panStatCharges;
    private com.see.truetransact.uicomponent.CPanel panStateCharges;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubsidyInterestCalculate;
    private com.see.truetransact.uicomponent.CPanel panSubsidyReceived;
    private com.see.truetransact.uicomponent.CPanel panSuspenseProducts;
    private com.see.truetransact.uicomponent.CPanel panToChargeOn;
    private com.see.truetransact.uicomponent.CPanel panToChargeType;
    private com.see.truetransact.uicomponent.CPanel panWaivedOff;
    private com.see.truetransact.uicomponent.CPanel panasAndWhenCustomer;
    private com.see.truetransact.uicomponent.CPanel pancharge_Amt;
    private com.see.truetransact.uicomponent.CPanel panldebitInterCharged;
    private com.see.truetransact.uicomponent.CButtonGroup rdoATMcardIssued;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMcardIssued_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMcardIssued_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdvertiseWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdvertiseWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoApplyMarketRateGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoArbitaryWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoArbitaryWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoArbitaryWaiverGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoArcWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoArcWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoBillByBill;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCalendarFrequency;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCommitmentCharge;
    private com.see.truetransact.uicomponent.CRadioButton rdoCommitmentCharge_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCommitmentCharge_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditCardIssued;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCardIssued_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCardIssued_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoCurrDt_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoDecreeWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDecreeWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDisbAftMoraPeriod;
    private com.see.truetransact.uicomponent.CRadioButton rdoEpWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoEpWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFolioChargeType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFolioChargesAppl;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioChargesAppl_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioChargesAppl_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFromSubsidyReceivedDateGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInsuranceApplicableGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoInsurenceWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInsurenceWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterestButtonGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestDueKeptReceivable_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestDueKeptReceivable_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterestRebateAllowedGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestRebateAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestRebateAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterestWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsAnyBranBankingAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsAnyBranBankingAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsAnyBranBankingAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsAuctionAmt;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsAuctionAmt_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsAuctionAmt_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsCreditAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsCreditAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsCreditAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDebitAllowedForDue_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDebitAllowedForDue_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsDebitInterUnderClearing;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDebitInterUnderClearing_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDebitInterUnderClearing_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDisbAftMora_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDisbAftMora_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsInterestDue_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsInterestFirst_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsInterestFirst_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsInterestdue_YES;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsLimitDefnAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsLimitDefnAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsLimitDefnAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsStaffAccOpened;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStaffAccOpened_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStaffAccOpened_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLTDinterestGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoLegalWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLegalWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLimitExpiryInter;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitExpiryInter_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitExpiryInter_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoLoanBalance_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoLoanOpenDate;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMaturityDepositClosureGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaturity_Deposit_closure_Curr_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaturity_Deposit_closure_Maturity_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoMaturity_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoMiscellaneousWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoMiscellaneousWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMobileBanlingClient;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobileBanlingClient_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobileBanlingClient_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoNoticeWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoNoticeWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoOverDueWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoOverDueWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPLRApplExistingAcc;
    private com.see.truetransact.uicomponent.CRadioButton rdoPLRApplExistingAcc_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPLRApplExistingAcc_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPLRApplNewAcc;
    private com.see.truetransact.uicomponent.CRadioButton rdoPLRApplNewAcc_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPLRApplNewAcc_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPLRRateAppl;
    private com.see.truetransact.uicomponent.CRadioButton rdoPLRRateAppl_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPLRRateAppl_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPenalAppl;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalAppl_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalAppl_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalCalcDays;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalCalcMonths;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalInterestWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalInterestWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPenalInterestWaiverGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoPostageWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPostageWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrincipalWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrincipalWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoProcessCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSalRec;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalRec_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSalRec_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoStatCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStatCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
    private com.see.truetransact.uicomponent.CRadioButton rdoSubsidyAdjustLoanBalance_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSubsidyReceivedDate;
    private com.see.truetransact.uicomponent.CRadioButton rdoSubsidy_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSubsidy_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoToChargeOn;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeOn_Both;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeOn_Man;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeOn_Sys;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeType_Both;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeType_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeType_Debit;
    private com.see.truetransact.uicomponent.CButtonGroup rdoasAndWhenCustomer;
    private com.see.truetransact.uicomponent.CRadioButton rdoasAndWhenCustomer_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoasAndWhenCustomer_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdobill_No;
    private com.see.truetransact.uicomponent.CRadioButton rdobill_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdocalendarFrequency_No;
    private com.see.truetransact.uicomponent.CRadioButton rdocalendarFrequency_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoinsuranceCharge_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoinsuranceCharge_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoldebitInterCharged;
    private com.see.truetransact.uicomponent.CRadioButton rdoldebitInterCharged_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoldebitInterCharged_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdosanctionDate_No;
    private com.see.truetransact.uicomponent.CRadioButton rdosanctionDate_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdosubsidyInterestCalculateGroup;
    private com.see.truetransact.uicomponent.CScrollPane scrollPanAdd;
    private com.see.truetransact.uicomponent.CScrollPane scrollPanAdd1;
    private com.see.truetransact.uicomponent.CScrollPane scrollPanAdd2;
    private com.see.truetransact.uicomponent.CScrollPane scrollPanList;
    private com.see.truetransact.uicomponent.CScrollPane scrollPanList1;
    private com.see.truetransact.uicomponent.CScrollPane scrollPanList2;
    private com.see.truetransact.uicomponent.CSeparator sptAcc;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptCharges_Vert;
    private com.see.truetransact.uicomponent.CSeparator sptCharges_Vert2;
    private com.see.truetransact.uicomponent.CSeparator sptCharges_Vert3;
    private com.see.truetransact.uicomponent.CSeparator sptClassification_vertical;
    private com.see.truetransact.uicomponent.CSeparator sptInterReceivable;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpCharges;
    private com.see.truetransact.uicomponent.CScrollPane srpDocuments;
    private com.see.truetransact.uicomponent.CScrollPane srpNoticeCharges;
    private com.see.truetransact.uicomponent.CTabbedPane tabLoanProduct;
    private com.see.truetransact.uicomponent.CTable tblCharges;
    private com.see.truetransact.uicomponent.CTable tblDocuments;
    private com.see.truetransact.uicomponent.CTable tblNoticeCharges;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtLastFolioChargesAppl;
    private com.see.truetransact.uicomponent.CDateField tdtLastInterAppDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastInterCalcDate;
    private com.see.truetransact.uicomponent.CDateField tdtNextFolioDDate;
    private com.see.truetransact.uicomponent.CDateField tdtPLRAppForm;
    private com.see.truetransact.uicomponent.CDateField tdtPlrApplAccSancForm;
    private com.see.truetransact.uicomponent.CTextField txtARCEPSuspenceHead;
    private com.see.truetransact.uicomponent.CTextField txtAccClosCharges;
    private com.see.truetransact.uicomponent.CTextField txtAccClosingCharges;
    private com.see.truetransact.uicomponent.CTextField txtAccCreditInter;
    private com.see.truetransact.uicomponent.CTextField txtAccDebitInter;
    private com.see.truetransact.uicomponent.CTextField txtAccHead;
    private com.see.truetransact.uicomponent.CTextField txtAccHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtAdvertiseWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtAdvertisementHead;
    private com.see.truetransact.uicomponent.CTextField txtApplicableInter;
    private com.see.truetransact.uicomponent.CTextField txtArbitaryWaiveHead;
    private com.see.truetransact.uicomponent.CTextField txtArbitraryCharges;
    private com.see.truetransact.uicomponent.CTextField txtArcCostAccount;
    private com.see.truetransact.uicomponent.CTextField txtArcExpenseAccount;
    private com.see.truetransact.uicomponent.CTextField txtArcWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtCharge_Limit2;
    private com.see.truetransact.uicomponent.CTextField txtCharge_Limit3;
    private com.see.truetransact.uicomponent.CTextField txtCheReturnChargest_In;
    private com.see.truetransact.uicomponent.CTextField txtCheReturnChargest_Out;
    private com.see.truetransact.uicomponent.CTextField txtCommitCharges;
    private com.see.truetransact.uicomponent.CTextField txtDebitIntDiscount;
    private com.see.truetransact.uicomponent.CTextField txtDecreeWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtDepAmtLoanMaturingPeriod;
    private com.see.truetransact.uicomponent.CTextField txtDepAmtMaturing;
    private com.see.truetransact.uicomponent.CTextField txtDocumentDesc;
    private com.see.truetransact.uicomponent.CTextField txtDocumentNo;
    private com.see.truetransact.uicomponent.CTextField txtEaCostAccount;
    private com.see.truetransact.uicomponent.CTextField txtEaExpenseAccount;
    private com.see.truetransact.uicomponent.CTextField txtElgLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtEpCostAccount;
    private com.see.truetransact.uicomponent.CTextField txtEpExpenseAccount;
    private com.see.truetransact.uicomponent.CTextField txtEpWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtExecutionDecreeCharges;
    private com.see.truetransact.uicomponent.CTextField txtExpiryInter;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Policy;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Policy2;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Prud;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Prud2;
    private com.see.truetransact.uicomponent.CTextField txtFinYearStartingFromDD;
    private com.see.truetransact.uicomponent.CTextField txtFinYearStartingFromMM;
    private com.see.truetransact.uicomponent.CTextField txtFixedMargin;
    private com.see.truetransact.uicomponent.CTextField txtFolioChargesAcc;
    private com.see.truetransact.uicomponent.CTextField txtFrequencyInDays;
    private com.see.truetransact.uicomponent.CTextField txtGracePeriodForOverDueInt;
    private com.see.truetransact.uicomponent.CTextField txtGracePeriodPenalInterest;
    private com.see.truetransact.uicomponent.CTextField txtInsuraceRate;
    private com.see.truetransact.uicomponent.CTextField txtInsuranceCharges;
    private com.see.truetransact.uicomponent.CTextField txtInsurenceWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtIntPayableAccount;
    private com.see.truetransact.uicomponent.CTextField txtInterestRebatePercentage;
    private com.see.truetransact.uicomponent.CTextField txtLastAccNum;
    private com.see.truetransact.uicomponent.CTextField txtLegalCharges;
    private com.see.truetransact.uicomponent.CTextField txtLegalWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtMAxCashPayment;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmtLoan;
    private com.see.truetransact.uicomponent.CTextField txtMaxDebitInterAmt;
    private com.see.truetransact.uicomponent.CTextField txtMaxDebitInterRate;
    private com.see.truetransact.uicomponent.CTextField txtMaxPeriod;
    private com.see.truetransact.uicomponent.CTextField txtMinAmtLoan;
    private com.see.truetransact.uicomponent.CTextField txtMinDebitInterAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinDebitInterRate;
    private com.see.truetransact.uicomponent.CTextField txtMinInterDebited;
    private com.see.truetransact.uicomponent.CTextField txtMinPeriod;
    private com.see.truetransact.uicomponent.CTextField txtMinPeriodsArrears;
    private com.see.truetransact.uicomponent.CTextField txtMiscSerCharges;
    private com.see.truetransact.uicomponent.CTextField txtMiscServCharges;
    private com.see.truetransact.uicomponent.CTextField txtMiscellaneousWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtNoEntriesPerFolio;
    private com.see.truetransact.uicomponent.CTextField txtNoticeAdvancesHead;
    private com.see.truetransact.uicomponent.CTextField txtNoticeChargeAmt;
    private com.see.truetransact.uicomponent.CTextField txtNoticeChargeDebitHead;
    private com.see.truetransact.uicomponent.CTextField txtNoticeCharges;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowed;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowedSuffix;
    private com.see.truetransact.uicomponent.CTextField txtOtherCharges;
    private com.see.truetransact.uicomponent.CTextField txtOverDueWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtOverdueIntHead;
    private com.see.truetransact.uicomponent.CTextField txtPLRRate;
    private com.see.truetransact.uicomponent.CTextField txtPenalInter;
    private com.see.truetransact.uicomponent.CTextField txtPenalInterRate;
    private com.see.truetransact.uicomponent.CTextField txtPenalWaiveoffHead;
    private com.see.truetransact.uicomponent.CTextField txtPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CTextField txtPostageAmt;
    private com.see.truetransact.uicomponent.CTextField txtPostageCharges;
    private com.see.truetransact.uicomponent.CTextField txtPostageWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtPreMatIntCalctPeriod;
    private com.see.truetransact.uicomponent.CTextField txtPreMatIntCalctPeriod1;
    private com.see.truetransact.uicomponent.CTextField txtPrincipleWaiveoffHead;
    private com.see.truetransact.uicomponent.CTextField txtProcessingCharges;
    private com.see.truetransact.uicomponent.CTextField txtProductDesc;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtProvisionDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CTextField txtProvisionDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CTextField txtProvisionDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CTextField txtProvisionLossAssets;
    private com.see.truetransact.uicomponent.CTextField txtProvisionStandardAssetss;
    private com.see.truetransact.uicomponent.CTextField txtProvisionsStanAssets;
    private com.see.truetransact.uicomponent.CTextField txtRangeFrom;
    private com.see.truetransact.uicomponent.CTextField txtRangeTo;
    private com.see.truetransact.uicomponent.CTextField txtRateAmt;
    private com.see.truetransact.uicomponent.CTextField txtRatePerFolio;
    private com.see.truetransact.uicomponent.CTextField txtRebateInterest;
    private com.see.truetransact.uicomponent.CTextField txtRebateLoanIntPercent;
    private com.see.truetransact.uicomponent.CTextField txtReviewPeriod;
    private com.see.truetransact.uicomponent.CTextField txtStampAdvancesHead;
    private com.see.truetransact.uicomponent.CTextField txtStatChargesRate;
    private com.see.truetransact.uicomponent.CTextField txtStatementCharges;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseCreditProductID;
    private com.see.truetransact.uicomponent.CTextField txtSuspenseDebitProductId;
    // End of variables declaration//GEN-END:variables
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsInterestFirst;
    
    private com.see.truetransact.uicomponent.CButtonGroup rdoRecoveryWaiverGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMeasurementWaiverGroup;
    private com.see.truetransact.uicomponent.CPanel panAdditionalDetails;
    private com.see.truetransact.uicomponent.CPanel panWaiverAllowed;
    private com.see.truetransact.uicomponent.CLabel lblRecoveryWaiverAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoRecoveryWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoRecoveryWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CLabel lblRecoveryWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtRecoveryWaiverHead;
    private com.see.truetransact.uicomponent.CButton btnRecoveryWaiverHead;
    private com.see.truetransact.uicomponent.CLabel lblMeasurementWaiverAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoMeasurementWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoMeasurementWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CLabel lblMeasurementWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtMeasurementWaiverHead;
    private com.see.truetransact.uicomponent.CButton btnMeasurementWaiverHead;
    private com.see.truetransact.uicomponent.CLabel lblRecoveryChargeHead;
    private com.see.truetransact.uicomponent.CTextField txtRecoveryChargeHead;
    private com.see.truetransact.uicomponent.CButton btnRecoveryChargeHead;
    private com.see.truetransact.uicomponent.CLabel lblMeasurementChargeHead;
    private com.see.truetransact.uicomponent.CTextField txtMeasurementChargeHead;
    private com.see.truetransact.uicomponent.CButton btnMeasurementChargeHead;
    
    private com.see.truetransact.uicomponent.CLabel lblKoleFieldOperationWaiverAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoKoleFieldOperationWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoKoleFieldOperationWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CLabel lblKoleFieldOperationWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtKoleFieldOperationWaiverHead;
    private com.see.truetransact.uicomponent.CButton btnKoleFieldOperationWaiverHead;
    private com.see.truetransact.uicomponent.CLabel lblKoleFieldOperationHead;
    private com.see.truetransact.uicomponent.CTextField txtKoleFieldOperationHead;
    private com.see.truetransact.uicomponent.CButton btnKoleFieldOperationHead;
    private com.see.truetransact.uicomponent.CButtonGroup rdoKoleFieldOperationWaiverGroup;
    
    
    
    private com.see.truetransact.uicomponent.CLabel lblKoleFieldExpenseWaiverAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoKoleFieldExpenseWaiverAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoKoleFieldExpenseWaiverAllowed_No;
    private com.see.truetransact.uicomponent.CLabel lblKoleFieldExpenseWaiverHead;
    private com.see.truetransact.uicomponent.CTextField txtKoleFieldExpenseWaiverHead;
    private com.see.truetransact.uicomponent.CButton btnKoleFieldExpenseWaiverHead;
    private com.see.truetransact.uicomponent.CLabel lblKoleFieldExpenseHead;
    private com.see.truetransact.uicomponent.CTextField txtKoleFieldExpenseHead;
    private com.see.truetransact.uicomponent.CButton btnKoleFieldExpenseHead;
    private com.see.truetransact.uicomponent.CButtonGroup rdoKoleFieldExpenseWaiverGroup;
    

     public void initPanAdditionalDetails() {        

        java.awt.GridBagConstraints gridBagConstraints = null;
        gridBagConstraints = new java.awt.GridBagConstraints();      
         
        panAdditionalDetails = new com.see.truetransact.uicomponent.CPanel();
        panWaiverAllowed =  new com.see.truetransact.uicomponent.CPanel();
        lblRecoveryWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoRecoveryWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRecoveryWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblRecoveryWaiverHead = new com.see.truetransact.uicomponent.CLabel();
        txtRecoveryWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnRecoveryWaiverHead = new com.see.truetransact.uicomponent.CButton();
        lblMeasurementWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoMeasurementWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMeasurementWaiverAllowed_No  = new com.see.truetransact.uicomponent.CRadioButton();  
        lblMeasurementWaiverHead = new com.see.truetransact.uicomponent.CLabel();
        txtMeasurementWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnMeasurementWaiverHead = new com.see.truetransact.uicomponent.CButton();
        rdoRecoveryWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMeasurementWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        
        lblRecoveryChargeHead = new com.see.truetransact.uicomponent.CLabel();
        txtRecoveryChargeHead = new com.see.truetransact.uicomponent.CTextField();
        btnRecoveryChargeHead = new com.see.truetransact.uicomponent.CButton();
        
        lblMeasurementChargeHead = new com.see.truetransact.uicomponent.CLabel();
        txtMeasurementChargeHead = new com.see.truetransact.uicomponent.CTextField();
        btnMeasurementChargeHead = new com.see.truetransact.uicomponent.CButton();
        
        
        lblKoleFieldOperationWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoKoleFieldOperationWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoKoleFieldOperationWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblKoleFieldOperationWaiverHead = new com.see.truetransact.uicomponent.CLabel();
        txtKoleFieldOperationWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnKoleFieldOperationWaiverHead = new com.see.truetransact.uicomponent.CButton();
        lblKoleFieldOperationHead = new com.see.truetransact.uicomponent.CLabel();
        txtKoleFieldOperationHead = new com.see.truetransact.uicomponent.CTextField();
        btnKoleFieldOperationHead = new com.see.truetransact.uicomponent.CButton();        
        rdoKoleFieldOperationWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        
        lblKoleFieldExpenseWaiverAllowed = new com.see.truetransact.uicomponent.CLabel();
        rdoKoleFieldExpenseWaiverAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoKoleFieldExpenseWaiverAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblKoleFieldExpenseWaiverHead = new com.see.truetransact.uicomponent.CLabel();
        txtKoleFieldExpenseWaiverHead = new com.see.truetransact.uicomponent.CTextField();
        btnKoleFieldExpenseWaiverHead = new com.see.truetransact.uicomponent.CButton();
        lblKoleFieldExpenseHead = new com.see.truetransact.uicomponent.CLabel();
        txtKoleFieldExpenseHead = new com.see.truetransact.uicomponent.CTextField();
        btnKoleFieldExpenseHead = new com.see.truetransact.uicomponent.CButton();        
        rdoKoleFieldExpenseWaiverGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        
       
        panWaiverAllowed.setPreferredSize(new java.awt.Dimension(356, 600));
        panWaiverAllowed.setLayout(new java.awt.GridBagLayout());
        
        
        lblRecoveryWaiverAllowed.setText("Recovery Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblRecoveryWaiverAllowed, gridBagConstraints);
        
        rdoRecoveryWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        rdoRecoveryWaiverAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRecoveryWaiverAllowed_YesActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(rdoRecoveryWaiverAllowed_Yes, gridBagConstraints);
        
        rdoRecoveryWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        rdoRecoveryWaiverAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRecoveryWaiverAllowed_NoActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(rdoRecoveryWaiverAllowed_No, gridBagConstraints);
        

        lblRecoveryWaiverHead.setText("Recovery Waiver Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblRecoveryWaiverHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtRecoveryWaiverHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtRecoveryWaiverHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtRecoveryWaiverHead, gridBagConstraints);
        
        
        btnRecoveryWaiverHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnRecoveryWaiverHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnRecoveryWaiverHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRecoveryWaiverHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnRecoveryWaiverHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecoveryWaiverHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnRecoveryWaiverHead, gridBagConstraints);
        
        lblMeasurementWaiverAllowed.setText("Measurement Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblMeasurementWaiverAllowed, gridBagConstraints);
        
        rdoMeasurementWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
         rdoMeasurementWaiverAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMeasurementWaiverAllowed_YesActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(rdoMeasurementWaiverAllowed_Yes, gridBagConstraints);
        
        rdoMeasurementWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        rdoMeasurementWaiverAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMeasurementWaiverAllowed_NoActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(rdoMeasurementWaiverAllowed_No, gridBagConstraints);
        
        lblMeasurementWaiverHead.setText("Measurement Waiver Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblMeasurementWaiverHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtMeasurementWaiverHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtMeasurementWaiverHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtMeasurementWaiverHead, gridBagConstraints);
        
        
        btnMeasurementWaiverHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnMeasurementWaiverHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMeasurementWaiverHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMeasurementWaiverHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnMeasurementWaiverHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMeasurementWaiverHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnMeasurementWaiverHead, gridBagConstraints);
        
        //recovery charge head
        lblRecoveryChargeHead.setText("Recovery Charge Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblRecoveryChargeHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtRecoveryChargeHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtRecoveryChargeHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtRecoveryChargeHead, gridBagConstraints);
        
        
        btnRecoveryChargeHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnRecoveryChargeHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnRecoveryChargeHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRecoveryChargeHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnRecoveryChargeHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecoveryChargeHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnRecoveryChargeHead, gridBagConstraints);
        
        //end
        
        // Measurement charge head
         lblMeasurementChargeHead.setText("Measurement Charge Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblMeasurementChargeHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtMeasurementChargeHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtMeasurementChargeHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtMeasurementChargeHead, gridBagConstraints);
        
        
        btnMeasurementChargeHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnMeasurementChargeHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMeasurementChargeHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMeasurementChargeHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnMeasurementChargeHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMeasurementChargeHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnMeasurementChargeHead, gridBagConstraints);
        // End
        
        
        // For Kole field expenses
        lblKoleFieldOperationWaiverAllowed.setText("Kole Field Operation Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblKoleFieldOperationWaiverAllowed, gridBagConstraints);
        
        rdoKoleFieldOperationWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panWaiverAllowed.add(rdoKoleFieldOperationWaiverAllowed_Yes, gridBagConstraints);
        
        rdoKoleFieldOperationWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panWaiverAllowed.add(rdoKoleFieldOperationWaiverAllowed_No, gridBagConstraints);
        
        lblKoleFieldOperationWaiverHead.setText("Kole Field Operation Waiver Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblKoleFieldOperationWaiverHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtKoleFieldOperationWaiverHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtKoleFieldOperationWaiverHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtKoleFieldOperationWaiverHead, gridBagConstraints);
        
        
        btnKoleFieldOperationWaiverHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnKoleFieldOperationWaiverHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldOperationWaiverHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldOperationWaiverHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnKoleFieldOperationWaiverHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKoleFieldOperationWaiverHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnKoleFieldOperationWaiverHead, gridBagConstraints);
        
        lblKoleFieldOperationHead.setText("Kole Field Operation Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblKoleFieldOperationHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtKoleFieldOperationHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtKoleFieldOperationHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtKoleFieldOperationHead, gridBagConstraints);    
        
        btnKoleFieldOperationHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnKoleFieldOperationHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldOperationHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldOperationHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnKoleFieldOperationHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKoleFieldOperationHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnKoleFieldOperationHead, gridBagConstraints);
        
        
        lblKoleFieldExpenseWaiverAllowed.setText("Kole Field Expense Waiver Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblKoleFieldExpenseWaiverAllowed, gridBagConstraints);
        
        rdoKoleFieldExpenseWaiverAllowed_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panWaiverAllowed.add(rdoKoleFieldExpenseWaiverAllowed_Yes, gridBagConstraints);
        
        rdoKoleFieldExpenseWaiverAllowed_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panWaiverAllowed.add(rdoKoleFieldExpenseWaiverAllowed_No, gridBagConstraints);
        
        lblKoleFieldExpenseWaiverHead.setText("Kole Field Expense Waiver Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblKoleFieldExpenseWaiverHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtKoleFieldExpenseWaiverHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtKoleFieldExpenseWaiverHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtKoleFieldExpenseWaiverHead, gridBagConstraints);
        
        
        btnKoleFieldExpenseWaiverHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnKoleFieldExpenseWaiverHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldExpenseWaiverHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldExpenseWaiverHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnKoleFieldExpenseWaiverHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKoleFieldExpenseWaiverHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnKoleFieldExpenseWaiverHead, gridBagConstraints);
        
        lblKoleFieldExpenseHead.setText("Kole Field Expense Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panWaiverAllowed.add(lblKoleFieldExpenseHead, gridBagConstraints);
        
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridheight = 2;
        //gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);       
        txtKoleFieldExpenseHead.setMinimumSize(new java.awt.Dimension(120, 21));
        txtKoleFieldExpenseHead.setPreferredSize(new java.awt.Dimension(120, 21));
        panWaiverAllowed.add(txtKoleFieldExpenseHead, gridBagConstraints);  


        btnKoleFieldExpenseHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnKoleFieldExpenseHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldExpenseHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnKoleFieldExpenseHead.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        btnKoleFieldExpenseHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKoleFieldExpenseHeadActionPerformed(evt);
            }
        });
        panWaiverAllowed.add(btnKoleFieldExpenseHead, gridBagConstraints);
                
        
        //End
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 0;
        gridBagConstraints.ipady = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 0);
        //panAdditionalDetails.setMinimumSize(new java.awt.Dimension(800, 800));
        //panAdditionalDetails.setPreferredSize(new java.awt.Dimension(800, 800));
        panAdditionalDetails.add(panWaiverAllowed, gridBagConstraints);
        
        tabLoanProduct.add("Additional Details",panAdditionalDetails);
        
    }
    
    private void rdoRecoveryWaiverAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {
//        if(rdoRecoveryWaiverAllowed_Yes.isSelected()){
//            rdoRecoveryWaiverAllowed_No.setSelected(false);
//        }else{
//            rdoRecoveryWaiverAllowed_No.setSelected(true);
//        }
    }
    
    private void rdoRecoveryWaiverAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {
//        if(rdoRecoveryWaiverAllowed_No.isSelected()){
//            rdoRecoveryWaiverAllowed_Yes.setSelected(false);
//        }else{
//            rdoRecoveryWaiverAllowed_Yes.setSelected(true);
//        }
    }
    
    private void rdoMeasurementWaiverAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {
//        if(rdoMeasurementWaiverAllowed_Yes.isSelected()){
//            rdoMeasurementWaiverAllowed_No.setSelected(false);
//        }else{
//            rdoMeasurementWaiverAllowed_No.setEnabled(true);
//        }
    }
    
    private void rdoMeasurementWaiverAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {
//        if(rdoMeasurementWaiverAllowed_No.isSelected()){
//            rdoMeasurementWaiverAllowed_Yes.setSelected(false);
//        }else{
//            rdoMeasurementWaiverAllowed_Yes.setSelected(true);
//        }
    }
    
    private void btnRecoveryWaiverHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(RECOVERYWAIVEOFF);
    }
    
     private void btnMeasurementWaiverHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(MEASUREMENTWAIVEOFF);
    }
     
    private void btnKoleFieldOperationWaiverHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(KOLEFIELDOPERATIONWAIVEOFF);
    }
    
    private void btnKoleFieldOperationHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(KOLEFIELDOPERATION);
    }
     
    private void btnKoleFieldExpenseWaiverHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(KOLEFIELDEXPENSEWAIVEOFF);
    }
    
     private void btnKoleFieldExpenseHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(KOLEFIELDEXPENSE);
    } 
     
     private void btnRecoveryChargeHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(RECOVERYCHARGE);
         System.out.println("executing here recovery charge head");
    }
    
     private void btnMeasurementChargeHeadActionPerformed(java.awt.event.ActionEvent evt) {
        popUp(MEASUREMENTCHARGE);
    } 
     
    

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        NewLoanProductUI gui = new NewLoanProductUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}

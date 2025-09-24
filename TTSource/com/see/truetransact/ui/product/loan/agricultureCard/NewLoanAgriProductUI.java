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

package com.see.truetransact.ui.product.loan.agricultureCard;
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
import org.apache.log4j.Logger;
import java.util.Date;
/**
 *
 * @author  rahul
 *  @modified : Sunil
 *      Added Edit Locking - 08-07-2005
 */
public class NewLoanAgriProductUI extends CInternalFrame implements java.util.Observer, UIMandatoryField{
    private HashMap mandatoryMap;
    NewLoanAgriProductOB observable;
    //    final NewLoanProductRB resourceBundle = new NewLoanProductRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.agricultureCard.NewLoanAgriProductRB", ProxyParameters.LANGUAGE);
    
    final NewLoanAgriProductMRB objMandatoryRB = new NewLoanAgriProductMRB();
    
    int viewType=-1;
    final int AUTHORIZE=100, EDIT=0,DELETE=1,ACCHEAD=2,ACCCLOSCHARGES=3,MISCSERVCHARGES=4,STATEMENTCHARGES=5,ACCDEBITINTER=6;
    final int PENALINTER=7,ACCCREDITINTER=8,EXPIRYINTER=9,CHERETURNCHARGES_OUT=10,CHERETURNCHARGES_IN=11;
    final int FOLIOCHARGESACC=12,COMMITCHARGES=13,PROCESSINGCHARGES=14,INTERRECEIVABLEGLACCHEAD=15,NOTICECHARGES=16,POSTAGECHARGES=17,VIEW = 350;
    final int INTPAYABLEACCHEAD=18, LEGALCHARGES=19, ARBITRARYCHARGES=20, INSURANCECHARGES=21, EXECUTION_DECREE_CHARGES=22,SUBSIDY=23,INSURANCEPREMIUMDEBIT=24;
    final String ABSOLUTE = "Absolute";
    final String PERCENT = "Percent";
    final int ACCOUNTHEADINDEX = 3;
    private boolean tableDocumentsClicked = false;
    private boolean tableNoticeChargeClicked=false;
    private boolean tabInsusranceClicked=false;
    private boolean isFilled = false;
    private Date currDt = null;
    //Logger
    private final static Logger log = Logger.getLogger(NewLoanAgriProductUI.class);
    
    
    /** Creates new form LoanProductUI */
    public NewLoanAgriProductUI() {
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
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
        
        setInvisible();
        setHelpMessage();
        tableDocumentsClicked = false;
        enableDisableTableDocuments(true);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLoanProduct);
        //__ To reset the value of the visited tabs...
        tabLoanProduct.resetVisits();
//        
//            lblSubsidyFromDt.setVisible(false);
//            tdtSubsidyFromDt.setVisible(false);
//            lblSubsidyToDate.setVisible(false);
//            tdtSubsidyToDate.setVisible(false);
//            lblSubsidyAmt.setVisible(false);
//            txtSubsidyAmt.setVisible(false);
    }
    
    private void setObservable() {// Creates the instance of OB
        observable = NewLoanAgriProductOB.getInstance();
        observable.addObserver(this);
    }
    /**
     * To enable disable table Documents
     */
    private void enableDisableTableDocuments(boolean flag){
        tblDocuments.setEnabled(flag);
    }
    
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
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
        
        //insurance Details
        
        btnInsuranceNew.setName("btnInsuranceNew");
        btnInsuranceSave.setName("btnInsuranceSave");
        btnInsuranceDelete.setName("btnInsuranceDelete");
        
        lblInsurancePremiumDebit.setName("lblInsurancePremiumDebit");
        txtInsurancePremiumDebit.setName("txtInsurancePremiumDebit");
        btnInsurancePremiumDebit.setName("btnInsurancePremiumDebit");
        lblInsuranceAmt.setName("lblInsuranceAmt");
        txtInsuranceAmt.setName("txtInsuranceAmt");

        cbo20Code.setName("cbo20Code");
        cboCharge_Limit2.setName("cboCharge_Limit2");
        cboCharge_Limit3.setName("cboCharge_Limit3");
        cboCommodityCode.setName("cboCommodityCode");
        cboDebInterCalcFreq.setName("cboDebInterCalcFreq");
        cboDebitInterAppFreq.setName("cboDebitInterAppFreq");
        cboDebitInterComFreq.setName("cboDebitInterComFreq");
        cboDebitInterRoundOff.setName("cboDebitInterRoundOff");
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
        cboReviewPeriod.setName("cboReviewPeriod");
        //insurance details
        cboInsuranceType.setName("cboInsuranceType");
        cboInsuranceUnderScheme.setName("cboInsuranceUnderScheme");
        lblNoticeType.setName("lblNoticeType");
        lblIssueAfter.setName("lblIssueAfter");
        lblNoticeChargeAmt.setName("lblNoticeChargeAmt");
        lblPostageAmt.setName("lblPostageAmt");
        
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
        lblMaxAmtLoan.setName("lblMaxAmtLoan");
        lblMaxDebitInterAmt.setName("lblMaxDebitInterAmt");
        lblMaxDebitInterRate.setName("lblMaxDebitInterRate");
        lblMaxDebitInterRate_Per.setName("lblMaxDebitInterRate_Per");
        lblMaxPeriod.setName("lblMaxPeriod");
        lblMinAmtLoan.setName("lblMinAmtLoan");
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
        lblOperatesLike.setName("lblOperatesLike");
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
        lblPenalApplicableAmt.setName("lblPenalApplicableAmt");
        lblMaxAmtCashTrans.setName("lblMaxAmtCashTrans");
        lblSubsidyAmt.setName("lblSubsidyAmt");
        lblSubsidyFromDt.setName("lblSubsidyFromDt");
        lblSubsidyToDate.setName("lblSubsidyToDate");
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
//        lblSubsidyAmt
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
        lblReviewPeriodLoan.setName("lblReviewPeriodLoan");
        //insurance  details
        
        lblInsuranceType.setName("lblInsuranceType");
        lblInsuranceUnderSchume.setName("lblInsuranceUnderSchume");
        lblBankSharePremium.setName("lblBankSharePremium");
        lblCustomerSharePremium.setName("lblCustomerSharePremium");
//        lbl_av_Balance.setName("lbl_av_Balance");
//        lblNoOfFreeFolio.setName("lblNoOfFreeFolio");
        
        mbrLoanProduct.setName("mbrLoanProduct");
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
        panPenalApplicableAmt.setName("panPenalApplicableAmt");
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
        panSubsidy.setName("panSubsidy");
        panToChargeOn.setName("panToChargeOn");
        pancharge_Amt.setName("pancharge_Amt");
        panldebitInterCharged.setName("panldebitInterCharged");
        panToChargeType.setName("panToChargeType");
        panasAndWhenCustomer.setName("panasAndWhenCustomer");
        panCalendarFrequency.setName("panCalendarFrequency");
        panPenalAppl_Due.setName("panPenalAppl_Due");
        
        //insurance details
        panInsuranceTabDetails.setName("panInsuranceTabDetails");
        srpInsuranceDetails.setName("srpInsuranceDetails");
        tblInsuranceDetails.setName("tblInsuranceDetails");
        panInsuranceDetails.setName("panInsuranceDetails");
        panInsuranceTabComDetails.setName("panInsuranceTabComDetails");
        panInsuranceButton.setName("panInsuranceButton");
        panOtherItem.setName("panOtherItem");
//        panChargeTypes.setName("panChargeTypes");
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
        txtPenalApplicableAmt.setName("txtPenalApplicableAmt");
        txtMaxAmtCashTrans.setName("txtMaxAmtCashTrans");
        txtSubsidyAmt.setName("txtSubsidyAmt");
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
        txtPostageChargeAmt.setName("txtPostageChargeAmt");
        txtNoticeCharges.setName("txtNoticeCharges");
        txtPostageCharges.setName("txtPostageCharges");
        txtExecutionDecreeCharges.setName("txtExecutionDecreeCharges");
        txtArbitraryCharges.setName("txtArbitraryCharges");
        txtInsuranceCharges.setName("txtInsuranceCharges");
        txtLegalCharges.setName("txtLegalCharges");
        //insurance details
        txtBankSharePremium.setName("txtBankSharePremium");
        txtCustomerSharePremium.setName("txtCustomerSharePremium");
        txtReviewPeriod.setName("txtReviewPeriod");

        
        
    }
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        //        final NewLoanProductRB resourceBundle = new NewLoanProductRB();
        
        lblPLRApplNewAcc.setText(resourceBundle.getString("lblPLRApplNewAcc"));
        lblLoanPeriodMul.setText(resourceBundle.getString("lblLoanPeriodMul"));
        lblOperatesLike.setText(resourceBundle.getString("lblOperatesLike"));
        lblQIS.setText(resourceBundle.getString("lblQIS"));
        rdoMobileBanlingClient_Yes.setText(resourceBundle.getString("rdoMobileBanlingClient_Yes"));
        btnStatementCharges.setText(resourceBundle.getString("btnStatementCharges"));
        btnAccClosCharges.setText(resourceBundle.getString("btnAccClosCharges"));
        btnIntPayableAccount.setText(resourceBundle.getString("btnIntPayableAccount"));
        lblDocumentType.setText(resourceBundle.getString("lblDocumentType"));
        rdoPLRRateAppl_No.setText(resourceBundle.getString("rdoPLRRateAppl_No"));
        lblLastInterAppDate.setText(resourceBundle.getString("lblLastInterAppDate"));
        ((javax.swing.border.TitledBorder)panFolio.getBorder()).setTitle(resourceBundle.getString("panFolio"));
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
        ((javax.swing.border.TitledBorder)panSpeItems.getBorder()).setTitle(resourceBundle.getString("panSpeItems"));
        rdoATMcardIssued_No.setText(resourceBundle.getString("rdoATMcardIssued_No"));
        btnAccHead.setText(resourceBundle.getString("btnAccHead"));
        lblLimitExpiryInter.setText(resourceBundle.getString("lblLimitExpiryInter"));
        lblDocumentDesc.setText(resourceBundle.getString("lblDocumentDesc"));
        lblSecurityDeails.setText(resourceBundle.getString("lblSecurityDeails"));
        lblPenalInter.setText(resourceBundle.getString("lblPenalInter"));
        lblSunbsidy.setText(resourceBundle.getString("lblSunbsidy"));
        lblProductDesc.setText(resourceBundle.getString("lblProductDesc"));
        lblChargeAppliedType.setText(resourceBundle.getString("lblChargeAppliedType"));
        lblPLRAppForm.setText(resourceBundle.getString("lblPLRAppForm"));
        rdoIsStaffAccOpened_Yes.setText(resourceBundle.getString("rdoIsStaffAccOpened_Yes"));
        lblStatus1.setText(resourceBundle.getString("lblStatus1"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblIsAnyBranBankingAllowed.setText(resourceBundle.getString("lblIsAnyBranBankingAllowed"));
        lblAccDebitInter.setText(resourceBundle.getString("lblAccDebitInter"));
        lblMiscSerCharges.setText(resourceBundle.getString("lblMiscSerCharges"));
        lblNumPatternFollowed.setText(resourceBundle.getString("lblNumPatternFollowed"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblMinPeriod.setText(resourceBundle.getString("lblMinPeriod"));
        rdoIsAnyBranBankingAllowed_Yes.setText(resourceBundle.getString("rdoIsAnyBranBankingAllowed_Yes"));
        lblPLRRate.setText(resourceBundle.getString("lblPLRRate"));
        lblSectorCode.setText(resourceBundle.getString("lblSectorCode"));
        lblCharge_1.setText(resourceBundle.getString("lblCharge_1"));
        btnPenalInter.setText(resourceBundle.getString("btnPenalInter"));
        lblToCollectFolioCharges.setText(resourceBundle.getString("lblToCollectFolioCharges"));
        rdoToChargeOn_Man.setText(resourceBundle.getString("rdoToChargeOn_Man"));
        lblPenalInterRate_Per.setText(resourceBundle.getString("lblPenalInterRate_Per"));
        lblPenalApplicableAmt.setText(resourceBundle.getString("lblPenalApplicableAmt"));
        lblSubsidyAccount.setText(resourceBundle.getString("lblSubsidyAccount"));
        lblMaxAmtCashTrans.setText(resourceBundle.getString("lblMaxAmtCashTrans"));
        lblSubsidyAmt.setText(resourceBundle.getString("lblSubsidyAmt"));
        lblSubsidyFromDt.setText(resourceBundle.getString("lblSubsidyFromDt"));
        lblSubsidyToDate.setText(resourceBundle.getString("lblSubsidyToDate"));
        rdoIsDebitInterUnderClearing_No.setText(resourceBundle.getString("rdoIsDebitInterUnderClearing_No"));
        lblIncompleteFolioRoundOffFreq.setText(resourceBundle.getString("lblIncompleteFolioRoundOffFreq"));
        lblStatCharges.setText(resourceBundle.getString("lblStatCharges"));
        lblCommodityCode.setText(resourceBundle.getString("lblCommodityCode"));
        lblIsDebitInterUnderClearing.setText(resourceBundle.getString("lblIsDebitInterUnderClearing"));
        ((javax.swing.border.TitledBorder)panChequeReturnCharges.getBorder()).setTitle(resourceBundle.getString("panChequeReturnCharges"));
        rdoFolioChargesAppl_Yes.setText(resourceBundle.getString("rdoFolioChargesAppl_Yes"));
        lblApplicableInter.setText(resourceBundle.getString("lblApplicableInter"));
        lblATMcardIssued.setText(resourceBundle.getString("lblATMcardIssued"));
        lblExpiryInter.setText(resourceBundle.getString("lblExpiryInter"));
        lblECGC.setText(resourceBundle.getString("lblECGC"));
        lblPLRApplExistingAcc.setText(resourceBundle.getString("lblPLRApplExistingAcc"));
        btnCheReturnChargest_Out.setText(resourceBundle.getString("btnCheReturnChargest_Out"));
        lblLimit.setText(resourceBundle.getString("lblLimit"));
        btnFolioChargesAcc.setText(resourceBundle.getString("btnFolioChargesAcc"));
//        btnInsurancePremiumDebit.setText(resourceBundle.getString("btnInsurancePremiumDebit"));
        lblAccClosCharges.setText(resourceBundle.getString("lblAccClosCharges"));
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
        ((javax.swing.border.TitledBorder)panNonPerAssets.getBorder()).setTitle(resourceBundle.getString("panNonPerAssets"));
        lblMinAmtLoan.setText(resourceBundle.getString("lblMinAmtLoan"));
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
        lblMaxAmtLoan.setText(resourceBundle.getString("lblMaxAmtLoan"));
        lblPeriodAfterWhichTransNPerformingAssets.setText(resourceBundle.getString("lblPeriodAfterWhichTransNPerformingAssets"));
        lblGovtSchemeCode.setText(resourceBundle.getString("lblGovtSchemeCode"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblProvisionStandardAssetssPer.setText(resourceBundle.getString("lblProvisionStandardAssetssPer"));
        btnCharge_Save.setText(resourceBundle.getString("btnCharge_Save"));
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
        
        lblInsuranceType.setText(resourceBundle.getString("lblInsuranceType"));
        lblInsuranceUnderSchume.setText(resourceBundle.getString("lblInsuranceUnderSchume"));
        lblBankSharePremium.setText(resourceBundle.getString("lblBankSharePremium"));
        lblCustomerSharePremium.setText(resourceBundle.getString("lblCustomerSharePremium"));
        lblInsuranceAmt.setText(resourceBundle.getString("lblInsuranceAmt"));
        btnInsuranceNew.setText(resourceBundle.getString("btnInsuranceNew"));
        btnInsuranceSave.setText(resourceBundle.getString("btnInsuranceSave"));
        btnInsuranceDelete.setText(resourceBundle.getString("btnInsuranceDelete"));
        
        lblReviewPeriodLoan.setText(resourceBundle.getString("lblReviewPeriodLoan"));
        lblInsurancePremiumDebit.setText(resourceBundle.getString("lblInsurancePremiumDebit"));
//        lbl_av_Balance.setText(resourceBundle.getString("lbl_av_Balance"));
//        lblNoOfFreeFolio.setText(resourceBundle.getString("lblNoOfFreeFolio"));
        
    }
    
    
   /* Auto Generated Method - setMandatoryHashMap()
    
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
    
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("txtProductDesc", new Boolean(true));
        mandatoryMap.put("cboOperatesLike", new Boolean(true));
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
        mandatoryMap.put("rdoToChargeType_Credit",new Boolean(true));
        mandatoryMap.put("rdoToChargeType_Debit",new Boolean(true));
        mandatoryMap.put("rdoToChargeType_Both",new Boolean(true));
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
        mandatoryMap.put("txtIntPayableAccount",new Boolean(true));
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
        mandatoryMap.put("txtAvBalance",new Boolean(true));
        mandatoryMap.put("txtNoOfFreeFolio",new Boolean(true));
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
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
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
        
        txtNumPatternFollowedSuffix.setMaxLength(10);
        txtNumPatternFollowedSuffix.setValidation(new NumericValidation());
        //LOANS_PROD_INTREC
        txtMinDebitInterRate.setValidation(new PercentageValidation());
        txtMaxAmtCashTrans.setValidation(new CurrencyValidation());
        
        //        txtMaxDebitInterRate.setMaxLength(3);
        txtMaxDebitInterRate.setValidation(new PercentageValidation());
        
        //        txtMinDebitInterAmt.setMaxLength(16);
        txtMinDebitInterAmt.setValidation(new CurrencyValidation(14,2));
        
        //        txtMaxDebitInterAmt.setMaxLength(16);
        txtMaxDebitInterAmt.setValidation(new CurrencyValidation(14,2));
        
        //        txtPLRRate.setMaxLength(3);
        txtPLRRate.setValidation(new PercentageValidation());
        
        //        txtPenalInterRate.setMaxLength(3);
        txtPenalInterRate.setValidation(new PercentageValidation());
        txtInsuranceAmt.setValidation(new CurrencyValidation());
        //        txtExposureLimit_Prud.setMaxLength(16);
        txtExposureLimit_Prud.setValidation(new NumericValidation(14,2));
        
        //        txtExposureLimit_Policy.setMaxLength(16);
        txtExposureLimit_Policy.setValidation(new NumericValidation(14,2));
        txtPenalApplicableAmt.setValidation(new CurrencyValidation());
        //        txtExposureLimit_Prud2.setMaxLength(3);
        txtExposureLimit_Prud2.setValidation(new PercentageValidation());
        
        //        txtExposureLimit_Policy2.setMaxLength(3);
        txtExposureLimit_Policy2.setValidation(new PercentageValidation());
        
        //LOANS_PROD_CHARGES
        //        txtAccClosingCharges.setMaxLength(16);
        txtAccClosingCharges.setValidation(new CurrencyValidation(14,2));
        
        //        txtMiscSerCharges.setMaxLength(16);
        txtMiscSerCharges.setValidation(new CurrencyValidation(14,2));
        
        //        txtStatChargesRate.setMaxLength(16);
        txtStatChargesRate.setValidation(new CurrencyValidation(14,2));
        
        txtNoEntriesPerFolio.setMaxLength(5);
        txtNoEntriesPerFolio.setValidation(new NumericValidation());
        
        //        txtRatePerFolio.setMaxLength(16);
        txtRatePerFolio.setValidation(new CurrencyValidation(14,2));
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
        txtRangeFrom.setValidation(new CurrencyValidation(14,2));
        
        //        txtRangeTo.setMaxLength(17);
        txtRangeTo.setValidation(new CurrencyValidation(14,2));
        
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
        
        //        txtProvisionLossAssets.setMaxLength(4);
        txtProvisionLossAssets.setValidation(new PercentageValidation());
        //LOANS_PROD_INTCALC
        txtMinPeriod.setMaxLength(5); // 4
        txtMinPeriod.setValidation(new NumericValidation());
        
        txtMaxPeriod.setMaxLength(5); // 4
        txtMaxPeriod.setValidation(new NumericValidation());
        
        //        txtMinAmtLoan.setMaxLength(17);
        txtMinAmtLoan.setValidation(new CurrencyValidation(14,2));
        
        //        txtMaxAmtLoan.setMaxLength(17);
        txtMaxAmtLoan.setValidation(new CurrencyValidation(14,2));
        
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
        txtPostageChargeAmt.setMaxLength(16);
        txtPostageChargeAmt.setValidation(new NumericValidation());
        txtBankSharePremium.setValidation(new PercentageValidation());
        txtReviewPeriod.setValidation(new PercentageValidation());
        txtCustomerSharePremium.setValidation(new PercentageValidation());
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //---Account---
        rdoIsLimitDefnAllowed.remove(rdoIsLimitDefnAllowed_No);
        rdoIsLimitDefnAllowed.remove(rdoIsLimitDefnAllowed_Yes);
        
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
        //-------------------------------------------------------------
    }
    
    
    
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
         removeRadioButtons();
        
        txtProductID.setText(observable.getTxtProductID());
        txtProductDesc.setText(observable.getTxtProductDesc());
        cboOperatesLike.setSelectedItem(observable.getCboOperatesLike());
        txtNumPatternFollowed.setText(observable.getTxtNumPatternFollowed());
        txtNumPatternFollowedSuffix.setText(observable.getTxtNumPatternFollowedSuffix());
        txtLastAccNum.setText(observable.getTxtLastAccNum());
        rdoIsLimitDefnAllowed_Yes.setSelected(observable.getRdoIsLimitDefnAllowed_Yes());
        rdoIsLimitDefnAllowed_No.setSelected(observable.getRdoIsLimitDefnAllowed_No());
        rdoIsStaffAccOpened_Yes.setSelected(observable.getRdoIsStaffAccOpened_Yes());
        rdoIsStaffAccOpened_No.setSelected(observable.getRdoIsStaffAccOpened_No());
        rdoIsDebitInterUnderClearing_Yes.setSelected(observable.getRdoIsDebitInterUnderClearing_Yes());
        rdoIsDebitInterUnderClearing_No.setSelected(observable.getRdoIsDebitInterUnderClearing_No());
        txtAccHead.setText(observable.getTxtAccHead());
        rdoldebitInterCharged_Yes.setSelected(observable.getRdoldebitInterCharged_Yes());
        rdoldebitInterCharged_No.setSelected(observable.getRdoldebitInterCharged_No());
        rdoldebitInterCharged_NoActionPerformed(null);
        
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
        chkprincipalDue.setSelected(observable.isChkprincipalDue());
        chkInterestDue.setSelected(observable.isChkInterestDue());
        rdoPenalAppl_NoActionPerformed(null);
        
        rdoLimitExpiryInter_Yes.setSelected(observable.getRdoLimitExpiryInter_Yes());
        rdoLimitExpiryInter_No.setSelected(observable.getRdoLimitExpiryInter_No());
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
//        txtElgLoanAmt.setText(observable.getTxtElgLoanAmt());
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
        txtIntPayableAccount.setText(observable.getTxtIntPayableAccount());
        txtMiscServCharges.setText(observable.getTxtMiscServCharges());
        txtStatementCharges.setText(observable.getTxtStatementCharges());
        txtAccDebitInter.setText(observable.getTxtAccDebitInter());
        txtPenalInter.setText(observable.getTxtPenalInter());
        txtAccCreditInter.setText(observable.getTxtAccCreditInter());
        txtExpiryInter.setText(observable.getTxtExpiryInter());
        txtCheReturnChargest_Out.setText(observable.getTxtCheReturnChargest_Out());
        txtCheReturnChargest_In.setText(observable.getTxtCheReturnChargest_In());
        txtFolioChargesAcc.setText(observable.getTxtFolioChargesAcc());
        txtCommitCharges.setText(observable.getTxtCommitCharges());
        txtProcessingCharges.setText(observable.getTxtProcessingCharges());
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
        txtReviewPeriod.setText(observable.getTxtReviewPeriod());
        cboReviewPeriod.setSelectedItem(observable.getCboReviewPeriod());
        /*   for Notice charge  */
        txtPostageChargeAmt.setText(observable.getTxtPostageChargeAmt());
        txtNoticeChargeAmt.setText(observable.getTxtNoticeChargeAmt());
        cboIssueAfter.setSelectedItem(observable.getCboIssueAfter());
        cboNoticeType.setSelectedItem(observable.getCboNoticeType());
        
        /* add noticechargeacchead  */
        
        txtPostageCharges.setText(observable.getTxtPostageCharges());
        txtNoticeCharges.setText(observable.getTxtNoticeCharges());
        txtMinInterDebited.setText(observable.getTxtMinInterDebited());
        rdoSubsidy_Yes.setSelected(observable.getRdoSubsidy_Yes());
        rdoSubsidy_No.setSelected(observable.getRdoSubsidy_No());
        cboLoanPeriodMul.setSelectedItem(observable.getCboLoanPeriodMul());
        /*  charge types*/
        txtLegalCharges.setText(observable.getTxtLegalCharges());
        txtArbitraryCharges.setText(observable.getTxtArbitraryCharges());
        txtInsuranceCharges.setText(observable.getTxtInsuranceCharges());
        txtExecutionDecreeCharges.setText(observable.getTxtExecutionDecreeCharges());
        log.info("observable.getTblCharges()"+observable.getTblCharges());
        tblCharges.setModel(observable.getTblCharges());
        tblInsuranceDetails.setModel(observable.getTblInsurance());
        lblStatus1.setText(observable.getLblStatus());
        txtPenalApplicableAmt.setText(observable.getTxtPenalApplicableAmt());
        updateDocuments();
        updateClassification();
        tblNoticeCharges.setModel(observable.getTblNoticeCharge());
        
        /* Insurance Details  */
//        ((com.see.truetransact.clientutil.ComboBoxModel)cboInsuranceType.getModel()).setKeyForSelected(observable.getCboInsuranceType ());
//        ((com.see.truetransact.clientutil.ComboBoxModel)cboInsuranceUnderScheme.getModel()).setKeyForSelected(observable.getCboInsuranceUnderScheme());
        cboInsuranceType.setSelectedItem(observable.getCboInsuranceType());
        cboInsuranceUnderScheme.setSelectedItem(observable.getCboInsuranceUnderScheme());
        txtBankSharePremium.setText(observable.getTxtBankSharePremium());
        txtCustomerSharePremium.setText(observable.getTxtCustomerSharePremium());
        txtInsuranceAmt.setText(observable.getTxtInsuranceAmt());
        addRadioButtons();
 
    }
    /**
     * Enable Disable New Save Delete Buttons of Documents Table
     * When Save or Delete is invoked
     */
    private void enableDisableDocuments_SaveDelete() {
        btnDocumentNew.setEnabled(true);
        btnDocumentSave.setEnabled(false);
        btnDocumentDelete.setEnabled(false);
    }
    /**
     * EnableDisable New Save Delete Buttons of Documents Table
     * When New is pressed
     */
    private void enableDisableDocuments_NewSaveDelete(boolean flag) {
        btnDocumentNew.setEnabled(flag);
        btnDocumentSave.setEnabled(flag);
        btnDocumentDelete.setEnabled(flag);
    }
    private void enableDisbleNotice_NewSaveDeleter(boolean flag){
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
        rdoIsLimitDefnAllowed = new CButtonGroup();
        rdoIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_No);
        rdoIsLimitDefnAllowed.add(rdoIsLimitDefnAllowed_Yes);
        
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
        //-------------------------------------------------------------
    }
    
    /* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        
        observable.setTxtProductID(txtProductID.getText());
        observable.setTxtProductDesc(txtProductDesc.getText());
        observable.setCboOperatesLike((String) cboOperatesLike.getSelectedItem());
        observable.setTxtNumPatternFollowed(txtNumPatternFollowed.getText());
        observable.setTxtNumPatternFollowedSuffix(txtNumPatternFollowedSuffix.getText());
        observable.setTxtLastAccNum(txtLastAccNum.getText());
        observable.setRdoIsLimitDefnAllowed_Yes(rdoIsLimitDefnAllowed_Yes.isSelected());
        observable.setRdoIsLimitDefnAllowed_No(rdoIsLimitDefnAllowed_No.isSelected());
        observable.setRdoIsStaffAccOpened_Yes(rdoIsStaffAccOpened_Yes.isSelected());
        observable.setRdoIsStaffAccOpened_No(rdoIsStaffAccOpened_No.isSelected());
        observable.setRdoIsDebitInterUnderClearing_Yes(rdoIsDebitInterUnderClearing_Yes.isSelected());
        observable.setRdoIsDebitInterUnderClearing_No(rdoIsDebitInterUnderClearing_No.isSelected());
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
        observable.setRdocalendarFrequency_Yes(rdocalendarFrequency_Yes.isSelected());
        observable.setRdocalendarFrequency_No(rdocalendarFrequency_No.isSelected());
        observable.setChkprincipalDue(chkprincipalDue.isSelected());
        observable.setChkInterestDue(chkInterestDue.isSelected());
        observable.setTxtPenalApplicableAmt(txtPenalApplicableAmt.getText());

//        observable.setTxtElgLoanAmt(txtElgLoanAmt.getText());
        observable.setCboReviewPeriod(CommonUtil.convertObjToStr(cboReviewPeriod.getSelectedItem()));
        observable.setTxtReviewPeriod(txtReviewPeriod.getText());
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
        observable.setTxtInsurancePremiumDebit(txtInsurancePremiumDebit.getText());
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
        observable.setTxtArbitraryCharges(txtArbitraryCharges.getText());
        observable.setTxtInsuranceCharges(txtInsuranceCharges.getText());
        observable.setTxtExecutionDecreeCharges(txtExecutionDecreeCharges.getText());
        observable.setRdoSubsidy_Yes(rdoSubsidy_Yes.isSelected());
        observable.setRdoSubsidy_No(rdoSubsidy_No.isSelected());
        observable.setCboLoanPeriodMul((String) cboLoanPeriodMul.getSelectedItem());
        
        
        
        observable.setTblCharges((com.see.truetransact.clientutil.EnhancedTableModel)tblCharges.getModel());
        observable.setCboDocumentType((String) cboDocumentType.getSelectedItem());
        observable.setTxtDocumentDesc(txtDocumentDesc.getText());
        observable.setTxtDocumentNo(txtDocumentNo.getText());
        observable.setTblDocuments((com.see.truetransact.clientutil.EnhancedTableModel)tblDocuments.getModel());
        
        
        
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
        
        /*   for Notice Charge   */
        observable.setCboIssueAfter((String)cboIssueAfter.getSelectedItem());
        observable.setCboNoticeType((String) cboNoticeType.getSelectedItem());
        observable.setTxtNoticeChargeAmt(txtNoticeChargeAmt.getText());
        observable.setTxtPostageChargeAmt(txtPostageChargeAmt.getText());
        observable.setTxtPostageCharges(txtPostageCharges.getText());
        observable.setTxtNoticeCharges(txtNoticeCharges.getText());
        /* Insurance Details  */
        observable.setCboInsuranceType(CommonUtil.convertObjToStr(cboInsuranceType.getSelectedItem()));
        observable.setCboInsuranceUnderScheme(CommonUtil.convertObjToStr(cboInsuranceUnderScheme.getSelectedItem()));
        observable.setTxtBankSharePremium(txtBankSharePremium.getText());
        observable.setTxtCustomerSharePremium(txtCustomerSharePremium.getText());
        observable.setTxtInsuranceAmt(txtInsuranceAmt.getText());
        
    }
    
    
    
    
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        NewLoanAgriProductMRB objMandatoryRB = new NewLoanAgriProductMRB();
        txtProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductID"));
        txtProductDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductDesc"));
        cboOperatesLike.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOperatesLike"));
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
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
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
        panLoanProduct = new com.see.truetransact.uicomponent.CPanel();
        lblSpaces = new com.see.truetransact.uicomponent.CLabel();
        tabLoanProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panAccount = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        lblProductDesc = new com.see.truetransact.uicomponent.CLabel();
        lblAccHead = new com.see.truetransact.uicomponent.CLabel();
        lblOperatesLike = new com.see.truetransact.uicomponent.CLabel();
        lblNumPatternFollowed = new com.see.truetransact.uicomponent.CLabel();
        lblLastAccNum = new com.see.truetransact.uicomponent.CLabel();
        lblIsLimitDefnAllowed = new com.see.truetransact.uicomponent.CLabel();
        lblIsStaffAccOpened = new com.see.truetransact.uicomponent.CLabel();
        lblIsDebitInterUnderClearing = new com.see.truetransact.uicomponent.CLabel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        txtProductDesc = new com.see.truetransact.uicomponent.CTextField();
        cboOperatesLike = new com.see.truetransact.uicomponent.CComboBox();
        txtNumPatternFollowed = new com.see.truetransact.uicomponent.CTextField();
        txtLastAccNum = new com.see.truetransact.uicomponent.CTextField();
        sptAcc = new com.see.truetransact.uicomponent.CSeparator();
        panIsLimitDefnAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoIsLimitDefnAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsLimitDefnAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        panIsStaffAccOpened = new com.see.truetransact.uicomponent.CPanel();
        rdoIsStaffAccOpened_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsStaffAccOpened_No = new com.see.truetransact.uicomponent.CRadioButton();
        panIsDebitInterUnderClearing = new com.see.truetransact.uicomponent.CPanel();
        rdoIsDebitInterUnderClearing_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsDebitInterUnderClearing_No = new com.see.truetransact.uicomponent.CRadioButton();
        btnAccHead = new com.see.truetransact.uicomponent.CButton();
        txtAccHead = new com.see.truetransact.uicomponent.CTextField();
        lblProdCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboProdCurrency = new com.see.truetransact.uicomponent.CComboBox();
        txtNumPatternFollowedSuffix = new com.see.truetransact.uicomponent.CTextField();
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
        lblPenalApplicableAmt = new com.see.truetransact.uicomponent.CLabel();
        panPenalApplicableAmt = new com.see.truetransact.uicomponent.CPanel();
        txtPenalApplicableAmt = new com.see.truetransact.uicomponent.CTextField();
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
        lblPenalDue = new com.see.truetransact.uicomponent.CLabel();
        lblPenalInterRate = new com.see.truetransact.uicomponent.CLabel();
        panPenalInterRate = new com.see.truetransact.uicomponent.CPanel();
        txtPenalInterRate = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInterRate_Per = new com.see.truetransact.uicomponent.CLabel();
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
        txtPostageChargeAmt = new com.see.truetransact.uicomponent.CTextField();
        panNoticeCharges_Table = new com.see.truetransact.uicomponent.CPanel();
        srpNoticeCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblNoticeCharges = new com.see.truetransact.uicomponent.CTable();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
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
        lblSubsidyAccount = new com.see.truetransact.uicomponent.CLabel();
        txtSubsidyAccount = new com.see.truetransact.uicomponent.CTextField();
        btnSubsidyAccount = new com.see.truetransact.uicomponent.CButton();
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
        lblInsurancePremiumDebit = new com.see.truetransact.uicomponent.CLabel();
        txtInsurancePremiumDebit = new com.see.truetransact.uicomponent.CTextField();
        btnInsurancePremiumDebit = new com.see.truetransact.uicomponent.CButton();
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
        panOtherItem = new com.see.truetransact.uicomponent.CPanel();
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
        lblMinAmtLoan = new com.see.truetransact.uicomponent.CLabel();
        txtMinAmtLoan = new com.see.truetransact.uicomponent.CTextField();
        lblMaxAmtLoan = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmtLoan = new com.see.truetransact.uicomponent.CTextField();
        lblApplicableInter = new com.see.truetransact.uicomponent.CLabel();
        panApplicableInter = new com.see.truetransact.uicomponent.CPanel();
        txtApplicableInter = new com.see.truetransact.uicomponent.CTextField();
        lblApplicableInterPer = new com.see.truetransact.uicomponent.CLabel();
        lblMinInterDebited = new com.see.truetransact.uicomponent.CLabel();
        panMinInterDebited = new com.see.truetransact.uicomponent.CPanel();
        txtMinInterDebited = new com.see.truetransact.uicomponent.CTextField();
        lblSunbsidy = new com.see.truetransact.uicomponent.CLabel();
        lblSubsidy = new com.see.truetransact.uicomponent.CLabel();
        panSubsidy = new com.see.truetransact.uicomponent.CPanel();
        rdoSubsidy_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSubsidy_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblLoanPeriodMul = new com.see.truetransact.uicomponent.CLabel();
        cboLoanPeriodMul = new com.see.truetransact.uicomponent.CComboBox();
        lblSubsidyAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSubsidyAmt = new com.see.truetransact.uicomponent.CTextField();
        lblSubsidyToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSubsidyToDate = new com.see.truetransact.uicomponent.CDateField();
        lblSubsidyFromDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSubsidyFromDt = new com.see.truetransact.uicomponent.CDateField();
        lblMaxAmtCashTrans = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmtCashTrans = new com.see.truetransact.uicomponent.CTextField();
        lblReviewPeriodLoan = new com.see.truetransact.uicomponent.CLabel();
        panMinPeriod1 = new com.see.truetransact.uicomponent.CPanel();
        txtReviewPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboReviewPeriod = new com.see.truetransact.uicomponent.CComboBox();
        panInsuranceTabComDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsuranceDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInsuranceType = new com.see.truetransact.uicomponent.CLabel();
        lblInsuranceUnderSchume = new com.see.truetransact.uicomponent.CLabel();
        lblBankSharePremium = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerSharePremium = new com.see.truetransact.uicomponent.CLabel();
        cboInsuranceType = new com.see.truetransact.uicomponent.CComboBox();
        cboInsuranceUnderScheme = new com.see.truetransact.uicomponent.CComboBox();
        panInsuranceButton = new com.see.truetransact.uicomponent.CPanel();
        btnInsuranceNew = new com.see.truetransact.uicomponent.CButton();
        btnInsuranceSave = new com.see.truetransact.uicomponent.CButton();
        btnInsuranceDelete = new com.see.truetransact.uicomponent.CButton();
        panBankSharePremium = new com.see.truetransact.uicomponent.CPanel();
        txtBankSharePremium = new com.see.truetransact.uicomponent.CTextField();
        lblSunbsidy1 = new com.see.truetransact.uicomponent.CLabel();
        panCustomerSharePremium = new com.see.truetransact.uicomponent.CPanel();
        txtCustomerSharePremium = new com.see.truetransact.uicomponent.CTextField();
        lblApplicableInterPer1 = new com.see.truetransact.uicomponent.CLabel();
        txtInsuranceAmt = new com.see.truetransact.uicomponent.CTextField();
        lblInsuranceAmt = new com.see.truetransact.uicomponent.CLabel();
        panInsuranceTabDetails = new com.see.truetransact.uicomponent.CPanel();
        srpInsuranceDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblInsuranceDetails = new com.see.truetransact.uicomponent.CTable();
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
        tbrLoantProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(900, 675));
        setPreferredSize(new java.awt.Dimension(900, 675));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panLoanProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLoanProduct.setMinimumSize(new java.awt.Dimension(756, 520));
        panLoanProduct.setPreferredSize(new java.awt.Dimension(756, 520));
        panLoanProduct.setLayout(new java.awt.GridBagLayout());

        lblSpaces.setMinimumSize(new java.awt.Dimension(3, 15));
        lblSpaces.setPreferredSize(new java.awt.Dimension(3, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panLoanProduct.add(lblSpaces, gridBagConstraints);

        tabLoanProduct.setMinimumSize(new java.awt.Dimension(833, 600));
        tabLoanProduct.setPreferredSize(new java.awt.Dimension(744, 600));
        tabLoanProduct.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabLoanProductStateChanged(evt);
            }
        });

        panAccount.setMinimumSize(new java.awt.Dimension(278, 216));
        panAccount.setPreferredSize(new java.awt.Dimension(278, 218));
        panAccount.setLayout(new java.awt.GridBagLayout());

        lblProductID.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblProductID, gridBagConstraints);

        lblProductDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblProductDesc, gridBagConstraints);

        lblAccHead.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblAccHead, gridBagConstraints);

        lblOperatesLike.setText("Operates Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblOperatesLike, gridBagConstraints);

        lblNumPatternFollowed.setText("Numbering Pattern to be Followed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblNumPatternFollowed, gridBagConstraints);

        lblLastAccNum.setText("Next Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblLastAccNum, gridBagConstraints);

        lblIsLimitDefnAllowed.setText("Is Limit Definition Allowed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblIsLimitDefnAllowed, gridBagConstraints);

        lblIsStaffAccOpened.setText("Is Staff Account Opened?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblIsStaffAccOpened, gridBagConstraints);

        lblIsDebitInterUnderClearing.setText("Is Debit Interest on Under Clearing Applicable?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblIsDebitInterUnderClearing, gridBagConstraints);

        txtProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(txtProductID, gridBagConstraints);

        txtProductDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(txtProductDesc, gridBagConstraints);

        cboOperatesLike.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOperatesLike.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(cboOperatesLike, gridBagConstraints);

        txtNumPatternFollowed.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowed.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccount.add(txtNumPatternFollowed, gridBagConstraints);

        txtLastAccNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(txtLastAccNum, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(sptAcc, gridBagConstraints);

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
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panIsLimitDefnAllowed, gridBagConstraints);

        panIsStaffAccOpened.setLayout(new java.awt.GridBagLayout());

        rdoIsStaffAccOpened.add(rdoIsStaffAccOpened_Yes);
        rdoIsStaffAccOpened_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsStaffAccOpened.add(rdoIsStaffAccOpened_Yes, gridBagConstraints);

        rdoIsStaffAccOpened.add(rdoIsStaffAccOpened_No);
        rdoIsStaffAccOpened_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsStaffAccOpened.add(rdoIsStaffAccOpened_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panIsStaffAccOpened, gridBagConstraints);

        panIsDebitInterUnderClearing.setLayout(new java.awt.GridBagLayout());

        rdoIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_Yes);
        rdoIsDebitInterUnderClearing_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_Yes, gridBagConstraints);

        rdoIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_No);
        rdoIsDebitInterUnderClearing_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIsDebitInterUnderClearing.add(rdoIsDebitInterUnderClearing_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(panIsDebitInterUnderClearing, gridBagConstraints);

        btnAccHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccHead.setToolTipText("Save");
        btnAccHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccount.add(btnAccHead, gridBagConstraints);

        txtAccHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(txtAccHead, gridBagConstraints);

        lblProdCurrency.setText("Product Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(lblProdCurrency, gridBagConstraints);

        cboProdCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccount.add(cboProdCurrency, gridBagConstraints);

        txtNumPatternFollowedSuffix.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowedSuffix.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 0);
        panAccount.add(txtNumPatternFollowedSuffix, gridBagConstraints);

        tabLoanProduct.addTab("Account", panAccount);

        panInterReceivable.setLayout(new java.awt.GridBagLayout());

        panInterReceivable_Debit.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInterReceivable_Debit.setMinimumSize(new java.awt.Dimension(332, 436));
        panInterReceivable_Debit.setPreferredSize(new java.awt.Dimension(332, 436));
        panInterReceivable_Debit.setLayout(new java.awt.GridBagLayout());

        lbldebitInterCharged.setText("Debit Interest to be Charged");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(panldebitInterCharged, gridBagConstraints);

        lblMinDebitInterRate.setText("Minimum Debit Interest rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblMinDebitInterRate, gridBagConstraints);

        panMinDebitInterRate.setLayout(new java.awt.GridBagLayout());

        txtMinDebitInterRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinDebitInterRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinDebitInterRate.add(txtMinDebitInterRate, new java.awt.GridBagConstraints());

        lblMinDebitInterRate_Per.setText("%");
        panMinDebitInterRate.add(lblMinDebitInterRate_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(panMinDebitInterRate, gridBagConstraints);

        lblMaxDebitInterRate.setText("Maximum Debit Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(panMaxDebitInterRate, gridBagConstraints);

        lblMinDebitInterAmt.setText("Minimum Debit Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblMinDebitInterAmt, gridBagConstraints);

        txtMinDebitInterAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(txtMinDebitInterAmt, gridBagConstraints);

        lblMaxDebitInterAmt.setText("Maximum Debit Interest Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblMaxDebitInterAmt, gridBagConstraints);

        txtMaxDebitInterAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxDebitInterAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDebitInterAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(txtMaxDebitInterAmt, gridBagConstraints);

        lblDebInterCalcFreq.setText("Debit Interest Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblDebInterCalcFreq, gridBagConstraints);

        cboDebInterCalcFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebInterCalcFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(cboDebInterCalcFreq, gridBagConstraints);

        lblDebitInterAppFreq.setText("Debit Interest Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblDebitInterAppFreq, gridBagConstraints);

        cboDebitInterAppFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitInterAppFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(cboDebitInterAppFreq, gridBagConstraints);

        lblDebitInterComFreq.setText("Debit Interest Compound Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblDebitInterComFreq, gridBagConstraints);

        cboDebitInterComFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitInterComFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(cboDebitInterComFreq, gridBagConstraints);

        lblDebitProdRoundOff.setText("Debit Product Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblDebitProdRoundOff, gridBagConstraints);

        cboDebitProdRoundOff.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitProdRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(cboDebitProdRoundOff, gridBagConstraints);

        lblDebitInterRoundOff.setText("Debit Interest Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblDebitInterRoundOff, gridBagConstraints);

        cboDebitInterRoundOff.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboDebitInterRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(cboDebitInterRoundOff, gridBagConstraints);

        lblLastInterCalcDate.setText("Last Interest Calculated Date - Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblLastInterCalcDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(tdtLastInterCalcDate, gridBagConstraints);

        lblLastInterAppDate.setText("Last Interest Application Date - Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblLastInterAppDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(tdtLastInterAppDate, gridBagConstraints);

        lblasAndWhenCustomer.setText("AsAndWhenCustomerComes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(lblasAndWhenCustomer, gridBagConstraints);

        lblcalendar.setText("CalendarFrequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_Debit.add(panCalendarFrequency, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable.add(panInterReceivable_Debit, gridBagConstraints);

        panInterReceivable_PLR.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInterReceivable_PLR.setMaximumSize(new java.awt.Dimension(474, 436));
        panInterReceivable_PLR.setMinimumSize(new java.awt.Dimension(474, 436));
        panInterReceivable_PLR.setPreferredSize(new java.awt.Dimension(474, 436));
        panInterReceivable_PLR.setLayout(new java.awt.GridBagLayout());

        lblPLRRateAppl.setText("PLR Rate Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panPLRRate, gridBagConstraints);

        lblPLRRate.setText("PLR Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(lblPLRRate, gridBagConstraints);

        lblPLRAppForm.setText("PLR Applicable From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(lblPLRAppForm, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(tdtPLRAppForm, gridBagConstraints);

        lblPLRApplNewAcc.setText("PLR Applicable for New Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panPLRApplNewAcc, gridBagConstraints);

        lblPLRApplExistingAcc.setText("PLR Applicable for Existing Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panPLRApplExistingAcc, gridBagConstraints);

        lblPlrApplAccSancForm.setText("PLR Applicable For Accounts Sanctioned From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(lblPlrApplAccSancForm, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(tdtPlrApplAccSancForm, gridBagConstraints);

        lblPenalAppl.setText("Penal Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panPenalAppl, gridBagConstraints);

        lblLimitExpiryInter.setText("Limit Expiry Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panLimitExpiryInter, gridBagConstraints);

        lblPenalApplicableAmt.setText("Penal Interest Applicable Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(lblPenalApplicableAmt, gridBagConstraints);

        panPenalApplicableAmt.setLayout(new java.awt.GridBagLayout());

        txtPenalApplicableAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPenalApplicableAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        panPenalApplicableAmt.add(txtPenalApplicableAmt, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panPenalApplicableAmt, gridBagConstraints);

        lblExposureLimit_Prud.setText("Exposure Limit (Prudential Norms)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panExposureLimit_Prud, gridBagConstraints);

        lblExposureLimit_Policy.setText("Exposure Limit (Policy Norms)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panExposureLimit_Policy, gridBagConstraints);

        lblProductFreq.setText("Product Accumulation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(lblProductFreq, gridBagConstraints);

        cboProductFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboProductFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(cboProductFreq, gridBagConstraints);

        panPenalAppl_Due.setMinimumSize(new java.awt.Dimension(100, 21));
        panPenalAppl_Due.setPreferredSize(new java.awt.Dimension(100, 21));
        panPenalAppl_Due.setLayout(new java.awt.GridBagLayout());

        chkprincipalDue.setText("PrincipalDue");
        chkprincipalDue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkprincipalDueActionPerformed(evt);
            }
        });
        panPenalAppl_Due.add(chkprincipalDue, new java.awt.GridBagConstraints());

        chkInterestDue.setText("InterestDue");
        panPenalAppl_Due.add(chkInterestDue, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panPenalAppl_Due, gridBagConstraints);

        lblPenalDue.setText("Penal Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(lblPenalDue, gridBagConstraints);

        lblPenalInterRate.setText("Penal Interest Rate For Debit Balance Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(lblPenalInterRate, gridBagConstraints);

        panPenalInterRate.setLayout(new java.awt.GridBagLayout());

        txtPenalInterRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPenalInterRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panPenalInterRate.add(txtPenalInterRate, new java.awt.GridBagConstraints());

        lblPenalInterRate_Per.setText("%");
        panPenalInterRate.add(lblPenalInterRate_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable_PLR.add(panPenalInterRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable.add(panInterReceivable_PLR, gridBagConstraints);

        sptInterReceivable.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterReceivable.add(sptInterReceivable, gridBagConstraints);

        tabLoanProduct.addTab("Interest Receivable", panInterReceivable);

        panCharges.setMinimumSize(new java.awt.Dimension(429, 450));
        panCharges.setPreferredSize(new java.awt.Dimension(429, 450));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        panChequeReturnCharges.setMinimumSize(new java.awt.Dimension(650, 140));
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
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panChequeReturnCharges, gridBagConstraints);

        panFolio.setBorder(javax.swing.BorderFactory.createTitledBorder("Folio"));
        panFolio.setLayout(new java.awt.GridBagLayout());

        panFolio_Date.setLayout(new java.awt.GridBagLayout());

        lblFolioChargesAppl.setText("Charges Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(lblFolioChargesAppl, gridBagConstraints);

        panFolioChargesAppl.setMaximumSize(new java.awt.Dimension(130, 27));
        panFolioChargesAppl.setMinimumSize(new java.awt.Dimension(130, 27));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(panFolioChargesAppl, gridBagConstraints);

        lblLastFolioChargesAppl.setText("Last Charges Applied On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(lblLastFolioChargesAppl, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(tdtLastFolioChargesAppl, gridBagConstraints);

        lblNoEntriesPerFolio.setText("No of Entries per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(lblNoEntriesPerFolio, gridBagConstraints);

        txtNoEntriesPerFolio.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(txtNoEntriesPerFolio, gridBagConstraints);

        lblNextFolioDDate.setText("Next Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(lblNextFolioDDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(cboFolioChargesAppFreq, gridBagConstraints);

        lblFolioChargesAppFreq.setText("Charges Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Date.add(lblFolioChargesAppFreq, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 4, 4);
        panFolio.add(panFolio_Date, gridBagConstraints);

        sptCharges_Vert2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio.add(sptCharges_Vert2, gridBagConstraints);

        panFolio_Freq.setLayout(new java.awt.GridBagLayout());

        lblToCollectFolioCharges.setText("To Collect Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(lblToCollectFolioCharges, gridBagConstraints);

        cboToCollectFolioCharges.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboToCollectFolioCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        cboToCollectFolioCharges.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(cboToCollectFolioCharges, gridBagConstraints);

        lblToChargeOn.setText("To Charge On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(panToChargeOn, gridBagConstraints);

        lblIncompleteFolioRoundOffFreq.setText("Incomplete Rounding Off frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(lblIncompleteFolioRoundOffFreq, gridBagConstraints);

        cboIncompleteFolioRoundOffFreq.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboIncompleteFolioRoundOffFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(cboIncompleteFolioRoundOffFreq, gridBagConstraints);

        lblRatePerFolio.setText("Rate per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(lblRatePerFolio, gridBagConstraints);

        txtRatePerFolio.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(panToChargeType, gridBagConstraints);

        lblChargeAppliedType.setText("To Charge AppliedType");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio_Freq.add(lblChargeAppliedType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolio.add(panFolio_Freq, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panFolio, gridBagConstraints);

        panCharge_ProcCommit.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProcessCharge.add(panCharge_1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharge_ProcCommit.add(panProcessCharge, gridBagConstraints);

        sptCharges_Vert3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharge_ProcCommit.add(sptCharges_Vert3, gridBagConstraints);

        panCommitCharges.setLayout(new java.awt.GridBagLayout());

        lblCommitmentCharge.setText("Commitment Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCommitCharges.add(lblCommitmentCharge, gridBagConstraints);

        panCommitmentCharge.setMaximumSize(new java.awt.Dimension(130, 27));
        panCommitmentCharge.setMinimumSize(new java.awt.Dimension(130, 27));
        panCommitmentCharge.setPreferredSize(new java.awt.Dimension(130, 27));
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCommitCharges.add(panCommitmentCharge, gridBagConstraints);

        lblCharge_2.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCommitCharges.add(panCharge_2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharge_ProcCommit.add(panCommitCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panCharge_ProcCommit, gridBagConstraints);

        panNoticeCharges.setBorder(javax.swing.BorderFactory.createTitledBorder("NoticeCharges"));
        panNoticeCharges.setMinimumSize(new java.awt.Dimension(650, 140));
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

        lblPostageAmt.setText("PostageAmt");
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
        panNoticecharge_Amt.add(txtPostageChargeAmt, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panNoticeCharges, gridBagConstraints);
        panNoticeCharges.getAccessibleContext().setAccessibleParent(panNoticeCharges);

        tabLoanProduct.addTab("Charges", panCharges);

        panAccountHead.setMinimumSize(new java.awt.Dimension(650, 500));
        panAccountHead.setPreferredSize(new java.awt.Dimension(650, 500));
        panAccountHead.setLayout(new java.awt.GridBagLayout());

        lblAccClosCharges.setText("Account Closing Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblAccClosCharges, gridBagConstraints);

        txtAccClosCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccClosCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccClosChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtAccClosCharges, gridBagConstraints);

        lbStatementCharges.setText("Statement Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lbStatementCharges, gridBagConstraints);

        txtStatementCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStatementCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStatementChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtStatementCharges, gridBagConstraints);

        lblAccDebitInter.setText("Account for Debit Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblAccDebitInter, gridBagConstraints);

        txtAccDebitInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccDebitInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccDebitInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtAccDebitInter, gridBagConstraints);

        lblPenalInter.setText("Penal Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPenalInter, gridBagConstraints);

        txtPenalInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtPenalInter, gridBagConstraints);

        lblAccCreditInter.setText("Account for Credit Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblAccCreditInter, gridBagConstraints);

        txtAccCreditInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAccCreditInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAccCreditInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtAccCreditInter, gridBagConstraints);

        lblExpiryInter.setText("Expiry Interest - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblExpiryInter, gridBagConstraints);

        txtExpiryInter.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExpiryInter.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExpiryInterFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtExpiryInter, gridBagConstraints);

        lblCheReturnChargest_Out.setText("Cheque Ret Charges (Outward) - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCheReturnChargest_Out, gridBagConstraints);

        txtCheReturnChargest_Out.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCheReturnChargest_Out.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCheReturnChargest_OutFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtCheReturnChargest_Out, gridBagConstraints);

        lblCheReturnChargest_In.setText("Cheque Ret Charges (Inward) - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCheReturnChargest_In, gridBagConstraints);

        txtCheReturnChargest_In.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCheReturnChargest_In.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCheReturnChargest_InFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtCheReturnChargest_In, gridBagConstraints);

        lblFolioChargesAcc.setText("Folio Charges Account - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblFolioChargesAcc, gridBagConstraints);

        txtFolioChargesAcc.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFolioChargesAcc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFolioChargesAccFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtFolioChargesAcc, gridBagConstraints);

        lblCommitCharges.setText("Commitment Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCommitCharges, gridBagConstraints);

        txtCommitCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommitCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommitChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtCommitCharges, gridBagConstraints);

        lblProcessingCharges.setText("Processing Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblProcessingCharges, gridBagConstraints);

        txtProcessingCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProcessingCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProcessingChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtProcessingCharges, gridBagConstraints);

        btnAccClosCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccClosCharges.setToolTipText("Save");
        btnAccClosCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccClosCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccClosChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnAccClosCharges, gridBagConstraints);

        btnStatementCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStatementCharges.setToolTipText("Save");
        btnStatementCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStatementCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatementChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnStatementCharges, gridBagConstraints);

        btnAccDebitInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccDebitInter.setToolTipText("Save");
        btnAccDebitInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccDebitInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccDebitInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnAccDebitInter, gridBagConstraints);

        btnPenalInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPenalInter.setToolTipText("Save");
        btnPenalInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPenalInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenalInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnPenalInter, gridBagConstraints);

        btnAccCreditInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccCreditInter.setToolTipText("Save");
        btnAccCreditInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccCreditInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccCreditInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnAccCreditInter, gridBagConstraints);

        btnExpiryInter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExpiryInter.setToolTipText("Save");
        btnExpiryInter.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExpiryInter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExpiryInterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnExpiryInter, gridBagConstraints);

        btnCheReturnChargest_Out.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCheReturnChargest_Out.setToolTipText("Save");
        btnCheReturnChargest_Out.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCheReturnChargest_Out.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheReturnChargest_OutActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnCheReturnChargest_Out, gridBagConstraints);

        btnCheReturnChargest_In.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCheReturnChargest_In.setToolTipText("Save");
        btnCheReturnChargest_In.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCheReturnChargest_In.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheReturnChargest_InActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnCheReturnChargest_In, gridBagConstraints);

        btnFolioChargesAcc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFolioChargesAcc.setToolTipText("Save");
        btnFolioChargesAcc.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFolioChargesAcc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolioChargesAccActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnFolioChargesAcc, gridBagConstraints);

        btnCommitCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommitCharges.setToolTipText("Save");
        btnCommitCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommitCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommitChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnCommitCharges, gridBagConstraints);

        btnProcessingCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnProcessingCharges.setToolTipText("Save");
        btnProcessingCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnProcessingCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessingChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnProcessingCharges, gridBagConstraints);

        lblNoticeCharges.setText("Notice Charges - P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblNoticeCharges, gridBagConstraints);

        txtNoticeCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtNoticeCharges, gridBagConstraints);

        btnNoticeCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNoticeCharges.setToolTipText("Save");
        btnNoticeCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNoticeCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoticeChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnNoticeCharges, gridBagConstraints);

        lblIntPayableAccount.setText("Credit Int Payable Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblIntPayableAccount, gridBagConstraints);

        txtIntPayableAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtIntPayableAccount, gridBagConstraints);

        btnIntPayableAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntPayableAccount.setToolTipText("Save");
        btnIntPayableAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntPayableAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntPayableAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnIntPayableAccount, gridBagConstraints);

        lblSubsidyAccount.setText("Subsidy Amt Debit Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblSubsidyAccount, gridBagConstraints);

        txtSubsidyAccount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtSubsidyAccount, gridBagConstraints);

        btnSubsidyAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSubsidyAccount.setToolTipText("Save");
        btnSubsidyAccount.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSubsidyAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSubsidyAccountActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnSubsidyAccount, gridBagConstraints);

        lblLegalCharges.setText("Legal Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblLegalCharges, gridBagConstraints);

        txtLegalCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtLegalCharges, gridBagConstraints);

        btnLegalCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLegalCharges.setToolTipText("Save");
        btnLegalCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLegalCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLegalChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        panAccountHead.add(btnLegalCharges, gridBagConstraints);

        lblPostageCharges.setText("Postage Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPostageCharges, gridBagConstraints);

        txtPostageCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtPostageCharges, gridBagConstraints);

        btnPostageCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostageCharges.setToolTipText("Save");
        btnPostageCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPostageCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostageChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        panAccountHead.add(btnPostageCharges, gridBagConstraints);

        lblMiscServCharges.setText("Miscellaneous Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMiscServCharges, gridBagConstraints);

        txtMiscServCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMiscServCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMiscServChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtMiscServCharges, gridBagConstraints);

        btnMiscServCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMiscServCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMiscServCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiscServChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        panAccountHead.add(btnMiscServCharges, gridBagConstraints);

        lblArbitraryCharges.setText("Arbitrary Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblArbitraryCharges, gridBagConstraints);

        txtArbitraryCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtArbitraryCharges, gridBagConstraints);

        btnArbitraryCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnArbitraryCharges.setToolTipText("Save");
        btnArbitraryCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnArbitraryCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnArbitraryChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        panAccountHead.add(btnArbitraryCharges, gridBagConstraints);

        lblInsuranceCharges.setText("Insurance Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblInsuranceCharges, gridBagConstraints);

        txtInsuranceCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtInsuranceCharges, gridBagConstraints);

        btnInsuranceCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInsuranceCharges.setToolTipText("Save");
        btnInsuranceCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInsuranceCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsuranceChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        panAccountHead.add(btnInsuranceCharges, gridBagConstraints);

        lblExecutionDecreeCharges.setText("Execution Decree Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblExecutionDecreeCharges, gridBagConstraints);

        txtExecutionDecreeCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtExecutionDecreeCharges, gridBagConstraints);

        btnExecutionDecreeCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExecutionDecreeCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExecutionDecreeCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecutionDecreeChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        panAccountHead.add(btnExecutionDecreeCharges, gridBagConstraints);

        lblInsurancePremiumDebit.setText("Insurance Premium Debit -P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblInsurancePremiumDebit, gridBagConstraints);

        txtInsurancePremiumDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(txtInsurancePremiumDebit, gridBagConstraints);

        btnInsurancePremiumDebit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInsurancePremiumDebit.setToolTipText("Save");
        btnInsurancePremiumDebit.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInsurancePremiumDebit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsurancePremiumDebitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAccountHead.add(btnInsurancePremiumDebit, gridBagConstraints);

        tabLoanProduct.addTab("Account Head", panAccountHead);

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

        panOtherItem.setMaximumSize(new java.awt.Dimension(710, 280));
        panOtherItem.setMinimumSize(new java.awt.Dimension(710, 280));
        panOtherItem.setPreferredSize(new java.awt.Dimension(710, 280));
        panOtherItem.setLayout(new java.awt.GridBagLayout());

        lblcalcType.setText("Calculation Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblcalcType, gridBagConstraints);

        cbocalcType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cbocalcType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(cbocalcType, gridBagConstraints);

        lblMinPeriod.setText("Minimum Term of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblMinPeriod, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(panMinPeriod, gridBagConstraints);

        lblMaxPeriod.setText("Maximum Term of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblMaxPeriod, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(panMaxPeriod, gridBagConstraints);

        lblMinAmtLoan.setText("Minimum Amount of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblMinAmtLoan, gridBagConstraints);

        txtMinAmtLoan.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(txtMinAmtLoan, gridBagConstraints);

        lblMaxAmtLoan.setText("Maximum Amount of Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblMaxAmtLoan, gridBagConstraints);

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
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(txtMaxAmtLoan, gridBagConstraints);

        lblApplicableInter.setText("Applicable Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblApplicableInter, gridBagConstraints);

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
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(panApplicableInter, gridBagConstraints);

        lblMinInterDebited.setText("Minimum Interest to be Debited");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblMinInterDebited, gridBagConstraints);

        panMinInterDebited.setLayout(new java.awt.GridBagLayout());

        txtMinInterDebited.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinInterDebited.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinInterDebited.add(txtMinInterDebited, new java.awt.GridBagConstraints());

        lblSunbsidy.setText("%");
        panMinInterDebited.add(lblSunbsidy, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(panMinInterDebited, gridBagConstraints);

        lblSubsidy.setText("Subsidy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherItem.add(lblSubsidy, gridBagConstraints);

        panSubsidy.setLayout(new java.awt.GridBagLayout());

        rdoSubsidy.add(rdoSubsidy_Yes);
        rdoSubsidy_Yes.setText("Yes");
        panSubsidy.add(rdoSubsidy_Yes, new java.awt.GridBagConstraints());

        rdoSubsidy.add(rdoSubsidy_No);
        rdoSubsidy_No.setText("No");
        panSubsidy.add(rdoSubsidy_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(panSubsidy, gridBagConstraints);

        lblLoanPeriodMul.setText("Loan Periods in Multiples of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblLoanPeriodMul, gridBagConstraints);

        cboLoanPeriodMul.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboLoanPeriodMul.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(cboLoanPeriodMul, gridBagConstraints);

        lblSubsidyAmt.setText("Goverment Sponcered Subsidy Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblSubsidyAmt, gridBagConstraints);

        txtSubsidyAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(txtSubsidyAmt, gridBagConstraints);

        lblSubsidyToDate.setText("Subsidy Applied To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblSubsidyToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(tdtSubsidyToDate, gridBagConstraints);

        lblSubsidyFromDt.setText("Subsidy Applied From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblSubsidyFromDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(tdtSubsidyFromDt, gridBagConstraints);

        lblMaxAmtCashTrans.setText("Maximum Amount of Cash Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblMaxAmtCashTrans, gridBagConstraints);

        txtMaxAmtCashTrans.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(txtMaxAmtCashTrans, gridBagConstraints);

        lblReviewPeriodLoan.setText("Review Period Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(lblReviewPeriodLoan, gridBagConstraints);

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
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panOtherItem.add(panMinPeriod1, gridBagConstraints);

        panInterCalculation.add(panOtherItem, new java.awt.GridBagConstraints());

        panInsuranceTabComDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Insurance Details", 1, 2));
        panInsuranceTabComDetails.setMaximumSize(new java.awt.Dimension(700, 215));
        panInsuranceTabComDetails.setMinimumSize(new java.awt.Dimension(700, 215));
        panInsuranceTabComDetails.setPreferredSize(new java.awt.Dimension(700, 215));
        panInsuranceTabComDetails.setLayout(new java.awt.GridBagLayout());

        panInsuranceDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInsuranceDetails.setMaximumSize(new java.awt.Dimension(345, 190));
        panInsuranceDetails.setMinimumSize(new java.awt.Dimension(345, 190));
        panInsuranceDetails.setPreferredSize(new java.awt.Dimension(345, 190));
        panInsuranceDetails.setLayout(new java.awt.GridBagLayout());

        lblInsuranceType.setText("Insurance Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panInsuranceDetails.add(lblInsuranceType, gridBagConstraints);

        lblInsuranceUnderSchume.setText("Insurance Under Scheme");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panInsuranceDetails.add(lblInsuranceUnderSchume, gridBagConstraints);

        lblBankSharePremium.setText("Bank Send Premium");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panInsuranceDetails.add(lblBankSharePremium, gridBagConstraints);

        lblCustomerSharePremium.setText("Customer Share Premium");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panInsuranceDetails.add(lblCustomerSharePremium, gridBagConstraints);

        cboInsuranceType.setMaximumSize(new java.awt.Dimension(100, 21));
        cboInsuranceType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 3, 0);
        panInsuranceDetails.add(cboInsuranceType, gridBagConstraints);

        cboInsuranceUnderScheme.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboInsuranceUnderSchemeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 3, 0);
        panInsuranceDetails.add(cboInsuranceUnderScheme, gridBagConstraints);

        panInsuranceButton.setMaximumSize(new java.awt.Dimension(240, 35));
        panInsuranceButton.setMinimumSize(new java.awt.Dimension(240, 35));
        panInsuranceButton.setPreferredSize(new java.awt.Dimension(240, 35));
        panInsuranceButton.setLayout(new java.awt.GridBagLayout());

        btnInsuranceNew.setText("New");
        btnInsuranceNew.setMaximumSize(new java.awt.Dimension(71, 27));
        btnInsuranceNew.setMinimumSize(new java.awt.Dimension(71, 27));
        btnInsuranceNew.setPreferredSize(new java.awt.Dimension(71, 27));
        btnInsuranceNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsuranceNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 6);
        panInsuranceButton.add(btnInsuranceNew, gridBagConstraints);

        btnInsuranceSave.setText("Save");
        btnInsuranceSave.setMaximumSize(new java.awt.Dimension(71, 27));
        btnInsuranceSave.setMinimumSize(new java.awt.Dimension(71, 27));
        btnInsuranceSave.setPreferredSize(new java.awt.Dimension(71, 27));
        btnInsuranceSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsuranceSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panInsuranceButton.add(btnInsuranceSave, gridBagConstraints);

        btnInsuranceDelete.setText("Delete");
        btnInsuranceDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsuranceDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        panInsuranceButton.add(btnInsuranceDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        panInsuranceDetails.add(panInsuranceButton, gridBagConstraints);

        panBankSharePremium.setLayout(new java.awt.GridBagLayout());

        txtBankSharePremium.setMinimumSize(new java.awt.Dimension(50, 21));
        txtBankSharePremium.setPreferredSize(new java.awt.Dimension(50, 21));
        txtBankSharePremium.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankSharePremiumFocusLost(evt);
            }
        });
        panBankSharePremium.add(txtBankSharePremium, new java.awt.GridBagConstraints());

        lblSunbsidy1.setText("%");
        panBankSharePremium.add(lblSunbsidy1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 4);
        panInsuranceDetails.add(panBankSharePremium, gridBagConstraints);

        panCustomerSharePremium.setLayout(new java.awt.GridBagLayout());

        txtCustomerSharePremium.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCustomerSharePremium.setPreferredSize(new java.awt.Dimension(50, 21));
        panCustomerSharePremium.add(txtCustomerSharePremium, new java.awt.GridBagConstraints());

        lblApplicableInterPer1.setText("%");
        panCustomerSharePremium.add(lblApplicableInterPer1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 10, 3, 4);
        panInsuranceDetails.add(panCustomerSharePremium, gridBagConstraints);

        txtInsuranceAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panInsuranceDetails.add(txtInsuranceAmt, gridBagConstraints);

        lblInsuranceAmt.setText("Insurance Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        panInsuranceDetails.add(lblInsuranceAmt, gridBagConstraints);

        panInsuranceTabComDetails.add(panInsuranceDetails, new java.awt.GridBagConstraints());

        panInsuranceTabDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInsuranceTabDetails.setMaximumSize(new java.awt.Dimension(340, 190));
        panInsuranceTabDetails.setMinimumSize(new java.awt.Dimension(340, 190));
        panInsuranceTabDetails.setPreferredSize(new java.awt.Dimension(340, 190));
        panInsuranceTabDetails.setLayout(new java.awt.GridBagLayout());

        srpInsuranceDetails.setMaximumSize(new java.awt.Dimension(335, 185));
        srpInsuranceDetails.setMinimumSize(new java.awt.Dimension(335, 185));
        srpInsuranceDetails.setPreferredSize(new java.awt.Dimension(335, 185));

        tblInsuranceDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblInsuranceDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInsuranceDetailsMousePressed(evt);
            }
        });
        srpInsuranceDetails.setViewportView(tblInsuranceDetails);

        panInsuranceTabDetails.add(srpInsuranceDetails, new java.awt.GridBagConstraints());

        panInsuranceTabComDetails.add(panInsuranceTabDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        panInterCalculation.add(panInsuranceTabComDetails, gridBagConstraints);

        tabLoanProduct.addTab("Other Terms", panInterCalculation);

        panDocuments.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocuments.add(panDocumentFields, gridBagConstraints);

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocuments.add(panDocumentTable, gridBagConstraints);

        tabLoanProduct.addTab("Documents", panDocuments);

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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panLoanProduct.add(tabLoanProduct, gridBagConstraints);

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
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
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

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace12);

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

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace13);

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

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace14);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace15);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

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

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace16);

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

    private void txtBankSharePremiumFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankSharePremiumFocusLost
        // TODO add your handling code here:
        String bankSharePremium=CommonUtil.convertObjToStr(txtBankSharePremium.getText());
        if(bankSharePremium.length()>0){
            double customerShare=100- Double.parseDouble(bankSharePremium);
            txtCustomerSharePremium.setText(String .valueOf(customerShare));
            txtCustomerSharePremium.setEnabled(false);
            
        }
    }//GEN-LAST:event_txtBankSharePremiumFocusLost

    private void btnInsurancePremiumDebitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsurancePremiumDebitActionPerformed
        // TODO add your handling code here:
          popUp(INSURANCEPREMIUMDEBIT);
    }//GEN-LAST:event_btnInsurancePremiumDebitActionPerformed

    private void tblInsuranceDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInsuranceDetailsMousePressed
       // TODO add your handling code here:
        int row=tblInsuranceDetails.getSelectedRow();
        observable.populateInsuranceDetails(row);
        boolean val=false;
        if(viewType != AUTHORIZE){
            val=true;
        }
        setNewEnableDisable(val);
         setInsuranceEnableDisable(val);
         if(viewType == AUTHORIZE)
           btnInsuranceNew.setEnabled(val);

        txtCustomerSharePremium.setEnabled(false);
        tabInsusranceClicked=true;
    }//GEN-LAST:event_tblInsuranceDetailsMousePressed

    private void btnInsuranceDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsuranceDeleteActionPerformed
        // TODO add your handling code here:
         setNewEnableDisable(false);
        setInsuranceEnableDisable(false);
        int row=tblInsuranceDetails.getSelectedRow();
        observable.deleteInsuranceDetails(row);
        tabInsusranceClicked=false;
        resetInsuranceDetails();
    }//GEN-LAST:event_btnInsuranceDeleteActionPerformed

    private void btnInsuranceSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsuranceSaveActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        int row=tblInsuranceDetails.getSelectedRow();
        observable.addInsuranceDetails(row,tabInsusranceClicked);
        resetInsuranceDetails();
        setNewEnableDisable(false);
        setInsuranceEnableDisable(false);
        resetInsuranceDetails();
        tabInsusranceClicked=false;
    }//GEN-LAST:event_btnInsuranceSaveActionPerformed
    private void resetInsuranceDetails(){
        cboInsuranceType.setSelectedItem("");
        cboInsuranceUnderScheme.setSelectedItem("");
        txtBankSharePremium.setText("");
        txtCustomerSharePremium.setText("");
        txtInsuranceAmt.setText("");
        observable.setCboInsuranceType("");
        observable.setCboInsuranceUnderScheme("");
        observable.setTxtBankSharePremium("");
        observable.setTxtCustomerSharePremium("");
        observable.setTxtInsuranceAmt("");
    }
    private void btnInsuranceNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsuranceNewActionPerformed
        // TODO add your handling code here:
        setNewEnableDisable(true);
        setInsuranceEnableDisable(true);
        tabInsusranceClicked=false;
        resetInsuranceDetails();
    }//GEN-LAST:event_btnInsuranceNewActionPerformed
    private void setInsuranceEnableDisable(boolean val){
        cboInsuranceType.setEnabled(val);
        cboInsuranceUnderScheme.setEnabled(val);
        txtBankSharePremium.setEnabled(val);
        txtInsuranceAmt.setEnabled(val);
//        txtCustomerSharePremium.setEnabled(val);
}
    private void setNewEnableDisable(boolean val){
        btnInsuranceNew.setEnabled(!val);
        btnInsuranceSave.setEnabled(val);
        btnInsuranceDelete.setEnabled(val);
    }
    private void cboInsuranceUnderSchemeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboInsuranceUnderSchemeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboInsuranceUnderSchemeActionPerformed

    private void btnSubsidyAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubsidyAccountActionPerformed
        // TODO add your handling code here:
                popUp(SUBSIDY);
    }//GEN-LAST:event_btnSubsidyAccountActionPerformed

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
 
        
        if(rdoPenalAppl_Yes.isSelected()){
            if(rdoasAndWhenCustomer_Yes.isSelected()) {
                chkInterestDue.setEnabled(false);
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
        setInterestCalcMethod();
//        rdoasAndWhenCustomer_YesActionPerformed(evt);
    }//GEN-LAST:event_rdocalendarFrequency_NoActionPerformed

    private void rdoasAndWhenCustomer_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoasAndWhenCustomer_NoActionPerformed
        // TODO add your handling code here:
        rdocalendarFrequency_Yes.setSelected(true);
        setInterestCalcMethod();
//        rdocalendarFrequency_YesActionPerformed(evt);
    }//GEN-LAST:event_rdoasAndWhenCustomer_NoActionPerformed

    private void rdoasAndWhenCustomer_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoasAndWhenCustomer_YesActionPerformed
        // TODO add your handling code here:
        rdocalendarFrequency_No.setSelected(true);
        setInterestCalcMethod();
//        rdocalendarFrequency_NoActionPerformed(evt);
    }//GEN-LAST:event_rdoasAndWhenCustomer_YesActionPerformed

    private void rdocalendarFrequency_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdocalendarFrequency_YesActionPerformed
        // TODO add your handling code here:
        rdoasAndWhenCustomer_No.setSelected(true);
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
        if(tblNoticeCharges.getSelectedRow()>=0){
            int index=tblNoticeCharges.getSelectedRow();
//            index+=1;
            observable.deleteNoticeCharge(index);
            observable.resetNoticeChargeValues();
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
        tableNoticeChargeClicked=true;
        if(viewType==AUTHORIZE){
             ClientUtil.enableDisable(panNoticecharge_Amt,false);
             enableDisbleNotice_NewSaveDeleter(false);
        }
        else{
            ClientUtil.enableDisable(panNoticecharge_Amt,true);
            enableDisbleNotice_NewSaveDeleter(true);
        }
        observable.populateNoticeCharge(tblNoticeCharges.getSelectedRow());
       
        
        tableNoticeChargeClicked=false;
//        observable.saveNoticeCharge(tableNoticeChargeClicked,tblNoticeCharges.getSelectedRow());
        //  total notice charge pan panNoticeCharges
    }//GEN-LAST:event_tblNoticeChargesMousePressed

    private void btnNotice_Charge_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_SaveActionPerformed
        // TODO add your handling code here:
        ClientUtil.enableDisable(panNoticeCharges,false);
        enableDisableNoticeCharge_SaveDelete(true);
        updateOBFields();
        observable.saveNoticeCharge(tableNoticeChargeClicked,tblNoticeCharges.getSelectedRow());
        updateNoticeCharge();
        observable.resetNoticeChargeValues();
        updateNoticeCharge();
        
         
    }//GEN-LAST:event_btnNotice_Charge_SaveActionPerformed
   private void btnCheck(){
         btnCancel.setEnabled(true);
         btnSave.setEnabled(false);
         btnNew.setEnabled(false);
         btnDelete.setEnabled(false);
         btnAuthorize.setEnabled(false);
         btnReject.setEnabled(false);
         btnException.setEnabled(false);
         btnEdit.setEnabled(false);
     }

    
    private void resetValues(){
        txtPostageChargeAmt.setText("");
        txtNoticeChargeAmt.setText("");
        
    }
    private void updateNoticeCharge(){
        cboNoticeType.setSelectedItem(observable.getCboNoticeType());
        cboIssueAfter.setSelectedItem(observable.getCboIssueAfter());
        txtNoticeChargeAmt.setText((String)observable.getTxtNoticeChargeAmt());
        txtPostageChargeAmt.setText((String)observable.getTxtPostageChargeAmt());
        tblNoticeCharges.setModel(observable.getTblNoticeCharge());
        
        
    }
    private void btnNotice_Charge_NewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNotice_Charge_NewActionPerformed
        ClientUtil.enableDisable(panNoticeCharges,true);
         enableDisableNoticeCharge_SaveDelete(false);
        
        
    }//GEN-LAST:event_btnNotice_Charge_NewActionPerformed
    
    private void cboPeriodTransDoubtfulAssets3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransDoubtfulAssets3ActionPerformed
        // TODO add your handling code here:
        if(cboPeriodTransDoubtfulAssets3.getSelectedIndex() < 1){
            txtPeriodTransDoubtfulAssets3.setText("");
        }
    }//GEN-LAST:event_cboPeriodTransDoubtfulAssets3ActionPerformed
    
    private void cboPeriodTransDoubtfulAssets2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransDoubtfulAssets2ActionPerformed
        // TODO add your handling code here:
        if(cboPeriodTransDoubtfulAssets2.getSelectedIndex() < 1){
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
    public void enableDisablePanDocumentsFields(boolean flag){
        ClientUtil.enableDisable(panDocumentFields,flag);
        
    }
    private void btnDocumentSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDocumentSaveActionPerformed
        // TODO add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDocumentFields);
        boolean duplicate = observable.checkDocumentType(txtProductID.getText(),CommonUtil.convertObjToStr((((ComboBoxModel)(cboDocumentType).getModel())).getKeyForSelected()), txtDocumentNo.getText(), tableDocumentsClicked, tblDocuments.getSelectedRow());
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
            observable.saveDocuments(tableDocumentsClicked,tblDocuments.getSelectedRow());
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
            if(observable.populateDocuments(tblDocuments.getSelectedRow())) {
                //                enableDisablePanDocumentsFields(true);
                whenEditIsPressed(true);
            } else {
                //                enableDisablePanDocumentsFields(true);
                whenEditIsPressed(false);
                if(viewType == AUTHORIZE){
                    btnDocumentNew.setEnabled(false);
                    btnDocumentDelete.setEnabled(false);
                    btnDocumentSave.setEnabled(false);
                    txtDocumentDesc.setEnabled(false);
                    ClientUtil.enableDisable(this,false);
                }
            }
        }
    }//GEN-LAST:event_tblDocumentsMousePressed
    
    private void tabLoanProductStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabLoanProductStateChanged
        // TODO add your handling code here:
        int index = tabLoanProduct.getSelectedIndex();
        if(index == ACCOUNTHEADINDEX){
            if( (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
            || (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
                
                int rows = observable.getRowCount();
                if(rows < 1){
                    btnCheReturnChargest_Out.setEnabled(false);
                    txtCheReturnChargest_Out.setText("");
                    txtCheReturnChargest_Out.setEditable(false);
                    txtCheReturnChargest_Out.setEnabled(false);
//                    btnCheReturnChargest_Out.setEnabled(false);
                }else{
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
        if(message.length() > 0){
            displayAlert(message);
        }
    }//GEN-LAST:event_txtApplicableInterFocusLost
    private String txtApplicableInterRule(){
        String message = "";
        
        if(!(txtApplicableInter.getText().equalsIgnoreCase("")
        || txtMaxDebitInterRate.getText().equalsIgnoreCase("")
        || txtMinDebitInterRate.getText().equalsIgnoreCase(""))){
            
            if(!(txtMaxDebitInterRate.getText().equalsIgnoreCase("0")
            || txtMinDebitInterRate.getText().equalsIgnoreCase("0"))){
                double appInt = Double.parseDouble(txtApplicableInter.getText());
                double maxInt = Double.parseDouble(txtMaxDebitInterRate.getText());
                double minInt = Double.parseDouble(txtMinDebitInterRate.getText());
                
                if( (maxInt < appInt) || (appInt < minInt)){
                    message = resourceBundle.getString("ApplicableLoanWarning");
                }
            }
            
        }
        return message;
    }
    private void txtMaxAmtLoanFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxAmtLoanFocusLost
        // TODO add your handling code here:
        String message = txtMaxAmtLoanRule();
        if(message.length() > 0){
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxAmtLoanFocusLost
    private String txtMaxAmtLoanRule(){
        String message = "";
        
        if(!(txtMaxAmtLoan.getText().equalsIgnoreCase("")
        || txtMinAmtLoan.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble(txtMinAmtLoan.getText()) >  Double.parseDouble(txtMaxAmtLoan.getText()) ){
                message = resourceBundle.getString("MaxLoanWarning");
            }
        }
        return message;
    }
    private void txtMiscSerChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscSerChargesFocusLost
        // TODO add your handling code here:
        if(!(txtMiscSerCharges.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble(txtMiscSerCharges.getText())==0){
                txtMiscServCharges.setText("");
                txtMiscServCharges.setEditable(false);
                txtMiscServCharges.setEnabled(false);
                btnMiscServCharges.setEnabled(false);
            }else{
                txtMiscServCharges.setEditable(true);
                txtMiscServCharges.setEnabled(true);
                btnMiscServCharges.setEnabled(true);
            }
        }else{
            txtMiscServCharges.setText("");
            txtMiscServCharges.setEditable(false);
            txtMiscServCharges.setEnabled(false);
            btnMiscServCharges.setEnabled(false);
        }
    }//GEN-LAST:event_txtMiscSerChargesFocusLost
    
    private void txtAccClosingChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccClosingChargesFocusLost
        // TODO add your handling code here:
        if(!(txtAccClosingCharges.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble(txtAccClosingCharges.getText())==0){
                txtAccClosCharges.setText("");
                txtAccClosCharges.setEditable(false);
                txtAccClosCharges.setEnabled(false);
                btnAccClosCharges.setEnabled(false);
            }else{
                txtAccClosCharges.setEditable(true);
                txtAccClosCharges.setEnabled(true);
                btnAccClosCharges.setEnabled(true);
            }
        }else{
            txtAccClosCharges.setText("");
            txtAccClosCharges.setEditable(false);
            txtAccClosCharges.setEnabled(false);
            btnAccClosCharges.setEnabled(false);
        }
    }//GEN-LAST:event_txtAccClosingChargesFocusLost
    
    private void cboFolioChargesAppFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFolioChargesAppFreqActionPerformed
        // TODO add your handling code here:
        if((String)cboFolioChargesAppFreq.getSelectedItem()!= null &&((String)cboFolioChargesAppFreq.getSelectedItem()).length()>0 && !((String)cboFolioChargesAppFreq.getSelectedItem()).equals("")){
            int period = CommonUtil.convertObjToInt(((ComboBoxModel) cboFolioChargesAppFreq.getModel()).getKeyForSelected());
            computeStartProdCalc(period);
        }
    }//GEN-LAST:event_cboFolioChargesAppFreqActionPerformed
    private void computeStartProdCalc(int days){
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
        if(message.length() > 0){
            displayAlert(message);
        }
    }//GEN-LAST:event_txtRangeToFocusLost
    private String txtRangeToRule(){
        String message = "";
        
        if(!(txtRangeFrom.getText().equalsIgnoreCase("")
        || txtRangeTo.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble(txtRangeFrom.getText()) >= Double.parseDouble(txtRangeTo.getText()) ){
                message = resourceBundle.getString("RangeWarning");
            }
        }
        return message;
    }
    private void txtMaxDebitInterAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDebitInterAmtFocusLost
        // TODO add your handling code here:
        String message = txtMaxDebitInterAmtRule();
        if(message.length() > 0){
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxDebitInterAmtFocusLost
    private String txtMaxDebitInterAmtRule(){
        String message = "";
        
        if(!(txtMinDebitInterAmt.getText().equalsIgnoreCase("")
        || txtMaxDebitInterAmt.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble(txtMinDebitInterAmt.getText()) >  Double.parseDouble(txtMaxDebitInterAmt.getText()) ){
                message = resourceBundle.getString("AmountWarning");
            }
        }
        return message;
    }
    private void txtMaxDebitInterRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDebitInterRateFocusLost
        // TODO add your handling code here:
        String message = txtMaxDebitInterRateRule();
        if(message.length() > 0){
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxDebitInterRateFocusLost
    private String txtMaxDebitInterRateRule(){
        String message = "";
        
        if(!(txtMinDebitInterRate.getText().equalsIgnoreCase("")
        || txtMaxDebitInterRate.getText().equalsIgnoreCase(""))){
            if(Double.parseDouble(txtMinDebitInterRate.getText()) >  Double.parseDouble(txtMaxDebitInterRate.getText()) ){
                message = resourceBundle.getString("RateWarning");
            }
        }
        return message;
    }
    private void txtProcessingChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProcessingChargesFocusLost
        // TODO add your handling code here:
        if(!(txtProcessingCharges.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtProcessingCharges, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtProcessingChargesFocusLost
    
    private void txtCommitChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommitChargesFocusLost
        // TODO add your handling code here:
        if(!(txtCommitCharges.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtCommitCharges, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtCommitChargesFocusLost
    
    private void txtFolioChargesAccFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFolioChargesAccFocusLost
        // TODO add your handling code here:
        if(!(txtFolioChargesAcc.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtFolioChargesAcc, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtFolioChargesAccFocusLost
    
    private void txtCheReturnChargest_InFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCheReturnChargest_InFocusLost
        // TODO add your handling code here:
        if(!(txtCheReturnChargest_In.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtCheReturnChargest_In, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtCheReturnChargest_InFocusLost
    
    private void txtCheReturnChargest_OutFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCheReturnChargest_OutFocusLost
        // TODO add your handling code here:
        if(!(txtCheReturnChargest_Out.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtCheReturnChargest_Out, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtCheReturnChargest_OutFocusLost
    
    private void txtExpiryInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExpiryInterFocusLost
        // TODO add your handling code here:
        if(!(txtExpiryInter.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtExpiryInter, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtExpiryInterFocusLost
    
    private void txtAccCreditInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccCreditInterFocusLost
        // TODO add your handling code here:
        if(!(txtAccCreditInter.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtAccCreditInter, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtAccCreditInterFocusLost
    
    private void txtPenalInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalInterFocusLost
        // TODO add your handling code here:
        if(!(txtPenalInter.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtPenalInter, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtPenalInterFocusLost
    
    private void txtAccDebitInterFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccDebitInterFocusLost
        // TODO add your handling code here:
        if(!(txtAccDebitInter.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtAccDebitInter, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtAccDebitInterFocusLost
    
    private void txtStatementChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStatementChargesFocusLost
        // TODO add your handling code here:
        if(!(txtStatementCharges.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtStatementCharges, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtStatementChargesFocusLost
    
    private void txtMiscServChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscServChargesFocusLost
        // TODO add your handling code here:
        if(!(txtMiscServCharges.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtMiscServCharges, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtMiscServChargesFocusLost
    
    private void txtAccClosChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccClosChargesFocusLost
        // TODO add your handling code here:
        if(!(txtAccClosCharges.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtAccClosCharges, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtAccClosChargesFocusLost
    
    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            boolean verify = observable.verifyProdId(txtProductID.getText());
            if(verify){
                final NewLoanAgriProductRB resourceBundle = new NewLoanAgriProductRB();
                displayAlert(resourceBundle.getString("prodIdWarning"));
                txtProductID.setText("");
            }
        }
    }//GEN-LAST:event_txtProductIDFocusLost
    
    private void txtAccHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAccHeadFocusLost
        // TODO add your handling code here:
        if(!(txtAccHead.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtAccHead, "TermLoan.getSelectAcctHeadTOList");
        }
    }//GEN-LAST:event_txtAccHeadFocusLost
    
    private void cboMaxPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaxPeriodActionPerformed
        // Add your handling code here:
        if(cboMaxPeriod.getSelectedIndex() < 1){
            txtMaxPeriod.setText("");
        }else{
            String message = cboMaxPeriodRule();
            if(message.length() > 0){
                displayAlert(message);
            }
        }
    }//GEN-LAST:event_cboMaxPeriodActionPerformed
    private String cboMaxPeriodRule(){
        String message = "";
        if(!(txtMaxPeriod.getText().equalsIgnoreCase("")
        || txtMinPeriod.getText().equalsIgnoreCase(""))){
            try{
                double maxPeriod = observable.setCombo(CommonUtil.convertObjToStr(((ComboBoxModel) cboMaxPeriod.getModel()).getKeyForSelected()));
                double minPeriod = observable.setCombo(CommonUtil.convertObjToStr(((ComboBoxModel) cboMinPeriod.getModel()).getKeyForSelected()));
                if( (Double.parseDouble(txtMaxPeriod.getText()) * maxPeriod) < (Double.parseDouble(txtMinPeriod.getText()) * minPeriod) ){
                    message = resourceBundle.getString("PeriodWarning");
                }
            }catch(Exception e){
                System.out.println("Error in cboMaxPeriodRule");
            }
        }
        return message;
    }
    private void cboMinPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMinPeriodActionPerformed
        // Add your handling code here:
        if(cboMinPeriod.getSelectedIndex() < 1){
            txtMinPeriod.setText("");
        }
    }//GEN-LAST:event_cboMinPeriodActionPerformed
    
    private void cboPeriodAfterWhichTransNPerformingAssetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodAfterWhichTransNPerformingAssetsActionPerformed
        // Add your handling code here:
        if(cboPeriodAfterWhichTransNPerformingAssets.getSelectedIndex() < 1){
            txtPeriodAfterWhichTransNPerformingAssets.setText("");
        }
    }//GEN-LAST:event_cboPeriodAfterWhichTransNPerformingAssetsActionPerformed
    
    private void cboPeriodTransLossAssetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransLossAssetsActionPerformed
        // Add your handling code here:
        if(cboPeriodTransLossAssets.getSelectedIndex() < 1){
            txtPeriodTransLossAssets.setText("");
        }
    }//GEN-LAST:event_cboPeriodTransLossAssetsActionPerformed
    
    private void cboPeriodTransDoubtfulAssets1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTransDoubtfulAssets1ActionPerformed
        // Add your handling code here:
        if(cboPeriodTransDoubtfulAssets1.getSelectedIndex() < 1){
            txtPeriodTransDoubtfulAssets1.setText("");
        }
    }//GEN-LAST:event_cboPeriodTransDoubtfulAssets1ActionPerformed
    
    private void cboPeriodTranSStanAssetsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPeriodTranSStanAssetsActionPerformed
        // Add your handling code here:
        if(cboPeriodTranSStanAssets.getSelectedIndex() < 1){
            txtPeriodTranSStanAssets.setText("");
        }
    }//GEN-LAST:event_cboPeriodTranSStanAssetsActionPerformed
    
    private void cboMinPeriodsArrearsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMinPeriodsArrearsActionPerformed
        // Add your handling code here:
        if(cboMinPeriodsArrears.getSelectedIndex() < 1){
            txtMinPeriodsArrears.setText("");
        }
    }//GEN-LAST:event_cboMinPeriodsArrearsActionPerformed
    
    private void cboCharge_Limit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCharge_Limit3ActionPerformed
        // Add your handling code here:
        observable.setCboCharge_Limit3((String) cboCharge_Limit3.getSelectedItem());
        if( observable.getCboCharge_Limit3().length() > 0){
            txtCharge_Limit3.setText("");
            txtCharge_Limit3.setEditable(true);
            txtCharge_Limit3.setEnabled(true);
            if( (observable.getCboCharge_Limit3()).equalsIgnoreCase(ABSOLUTE)){
                txtCharge_Limit3.setMaxLength(17);
                txtCharge_Limit3.setValidation(new NumericValidation(14,2));
            }else{
                txtCharge_Limit3.setMaxLength(6);
                txtCharge_Limit3.setValidation(new NumericValidation(3,2));
            }
        }
    }//GEN-LAST:event_cboCharge_Limit3ActionPerformed
    
    private void cboCharge_Limit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCharge_Limit2ActionPerformed
        // Add your handling code here:
        observable.setCboCharge_Limit2((String) cboCharge_Limit2.getSelectedItem());
        if( observable.getCboCharge_Limit2().length() > 0){
            if(viewType!=AUTHORIZE){
                txtCharge_Limit2.setText("");
                txtCharge_Limit2.setEditable(true);
                txtCharge_Limit2.setEnabled(true);
            }
            if( (observable.getCboCharge_Limit2()).equalsIgnoreCase(ABSOLUTE)){
                txtCharge_Limit2.setMaxLength(17);
                txtCharge_Limit2.setValidation(new NumericValidation(14,2));
            }else{
                txtCharge_Limit2.setMaxLength(6);
                txtCharge_Limit2.setValidation(new NumericValidation(3,2));
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
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            //__ To Save the data in the Internal Frame...
            setModified(true);

            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getAgriLoanProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAgriLoanProduct");
            mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            rdoFolioChargesAppl_Yes.setEnabled(false);
            rdoFolioChargesAppl_No.setEnabled(false);
            btnDocumentNew.setEnabled(false);
            btnDocumentDelete.setEnabled(false);
            btnDocumentSave.setEnabled(false);
            ClientUtil.enableDisable(this,false);
            //__ If there's no data to be Authorized, call Cancel action...
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
            if(authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED))
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
             if(authorizeStatus.equals(CommonConstants.STATUS_REJECTED))
                observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);

        } else if (viewType == AUTHORIZE && isFilled){
            String warningMessage = tabLoanProduct.isAllTabsVisited();
            if(warningMessage.length() > 0){
                displayAlert(warningMessage);

            }else{
                //__ To reset the value of the visited tabs...
                tabLoanProduct.resetVisits();
           final String BehavesLike = CommonUtil.convertObjToStr(((ComboBoxModel) cboOperatesLike.getModel()).getKeyForSelected());
           final int actionType = observable.getActionType();

             HashMap dataMap = new HashMap();
            dataMap.put("ACTION_TYPE", String.valueOf(actionType));
            dataMap.put("PROD_ID", CommonUtil.convertObjToStr(txtProductID.getText()));
            dataMap.put("PROD_DESC",CommonUtil.convertObjToStr(txtProductDesc.getText()));
            dataMap.put("AGRI","AGRI");

                if(BehavesLike.equalsIgnoreCase("AOD") || BehavesLike.equalsIgnoreCase("ACC")){
                    String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo")};
                    int option = COptionPane.showOptionDialog(null, resourceBundle.getString("OD_CCALERT"), CommonConstants.INFORMATIONTITLE,
                    COptionPane.YES_NO_OPTION, COptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    
                    System.out.println("option: " + option);
                    if (option == 0){    //__ Yes is selected
                        TrueTransactMain.showScreen(new AdvancesProductUI(dataMap));
                    }
                }
                
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("PROD_ID", txtProductID.getText());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());

                ClientUtil.execute("authorizeAgriLoanProduct", singleAuthorizeMap);
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(txtProductID.getText());
                viewType = -1;
                btnCancelActionPerformed(null);
            }
        }
    }
    private void rdoCommitmentCharge_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCommitmentCharge_NoActionPerformed
        // Add your handling code here:
        if(rdoCommitmentCharge_No.isSelected()) {
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
        if(rdoProcessCharges_No.isSelected()) {
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
        if(rdoFolioChargesAppl_No.isSelected()) {
            ClientUtil.clearAll(panFolio);
            ClientUtil.enableDisable(panFolio, false);
            rdoFolioChargesAppl_No.setSelected(true);
            if(viewType!=AUTHORIZE){
            rdoFolioChargesAppl_No.setEnabled(true);
            rdoFolioChargesAppl_Yes.setEnabled(true);
            }
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
        if(rdoStatCharges_No.isSelected()) {
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
        if(rdoLimitExpiryInter_No.isSelected()) {
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
        if(rdoPenalAppl_No.isSelected()) {
            txtPenalInterRate.setText("");
            txtPenalInterRate.setEnabled(false);
            txtPenalInterRate.setEditable(false);
            txtPenalApplicableAmt.setEditable(false);
            txtPenalApplicableAmt.setEnabled(false);
            txtPenalApplicableAmt.setText("");
            
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
        txtPenalApplicableAmt.setEnabled(true);
        txtPenalApplicableAmt.setEditable(true);
        
//        chkprincipalDue.setEnabled(true);
//        chkInterestDue.setEnabled(true);
            
        btnPenalInter.setEnabled(true);
        txtPenalInter.setEnabled(true);
        txtPenalInter.setEditable(true);
        setInterestCalcMethod();

    }//GEN-LAST:event_rdoPenalAppl_YesActionPerformed
    
    private void rdoPLRRateAppl_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPLRRateAppl_NoActionPerformed
        // Add your handling code here:
        if(rdoPLRRateAppl_No.isSelected()) {
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
        txtPenalApplicableAmt.setEnabled(true);
        txtPenalApplicableAmt.setEditable(true);
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
    
    private void btnAccHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccHeadActionPerformed
        // Add your handling code here:
        popUp(ACCHEAD);
    }//GEN-LAST:event_btnAccHeadActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        //__ To reset the value of the visited tabs...
        tabLoanProduct.resetVisits();
        
        super.removeEditLock(txtProductID.getText());
        observable.resetForm();
        observable.resetTable();
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
    }//GEN-LAST:event_btnCancelActionPerformed
    // To disable the folder buttons in Account and Account Head
    // Disabled  when Delete or Cancel is done
    private void disableButtons() {
        btnAccHead.setEnabled(false);
        btnIntPayableAccount.setEnabled(false);
        btnSubsidyAccount.setEnabled(false);
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
        btnInsurancePremiumDebit.setEnabled(false);
        btnCommitCharges.setEnabled(false);
        btnProcessingCharges.setEnabled(false);
        btnNoticeCharges.setEnabled(false);
        btnPostageCharges.setEnabled(false);
        btnLegalCharges.setEnabled(false);
        btnInsuranceCharges.setEnabled(false);
        btnExecutionDecreeCharges.setEnabled(false);
        btnArbitraryCharges.setEnabled(false);
        
    }
    
    private void enableDisableButtons() {
        btnAccHead.setEnabled(!btnAccHead.isEnabled());
        btnIntPayableAccount.setEnabled(!btnIntPayableAccount.isEnabled());
        btnSubsidyAccount.setEnabled(!btnSubsidyAccount.isEnabled());
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
        btnInsurancePremiumDebit.setEnabled(!btnInsurancePremiumDebit.isEnabled());
        btnCommitCharges.setEnabled(!btnCommitCharges.isEnabled());
        btnProcessingCharges.setEnabled(!btnProcessingCharges.isEnabled());
        btnNoticeCharges.setEnabled(!btnNoticeCharges.isEnabled());
        btnPostageCharges.setEnabled(!btnPostageCharges.isEnabled());
        btnLegalCharges.setEnabled(!btnLegalCharges.isEnabled());
        btnInsuranceCharges.setEnabled(!btnInsuranceCharges.isEnabled());
        btnExecutionDecreeCharges.setEnabled(!btnExecutionDecreeCharges.isEnabled());
        btnArbitraryCharges.setEnabled(! btnArbitraryCharges.isEnabled());
        
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
        txtInsurancePremiumDebit.setEditable(value);
        txtInsurancePremiumDebit.setEditable(value);
        txtCommitCharges.setEditable(value);
        txtProcessingCharges.setEditable(value);
    }    
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
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
        
    }//GEN-LAST:event_btnEditActionPerformed
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field==VIEW){//Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewAgriLoanProduct");
            new ViewAll(this, viewMap).show();
            
        }else {
            updateOBFields();
            viewMap.put(CommonConstants.MAP_NAME, "TermLoan.getSelectAcctHeadTOList");
            new ViewAll(this, viewMap, true).show();
        }
        //        new ViewAll(this, viewMap).show();
    }
    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        //final String accountHead="AC_HD_ID";
        final String accountHead=(String)hash.get("AC_HD_ID");
        if (viewType != -1) {
            if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW) {
                isFilled = true;
                hash.put(CommonConstants.MAP_WHERE, CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                observable.populateData(hash);
                enableDisableNoticeCharge_SaveDelete(true);
                if(viewType==AUTHORIZE){
                    enableDisbleNotice_NewSaveDeleter(false);
                    setNewEnableDisable(false);
                    btnInsuranceNew.setEnabled(false);
                }else
                    setNewEnableDisable(false);
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE || viewType==VIEW) {
                    ClientUtil.enableDisable(this, false);
                    setInvisible();
                }else{
                    ClientUtil.enableDisable(this, true);
                    ClientUtil.enableDisable(panNoticecharge_Amt,false);
                    enableDisableButtons();
                    enableDisableCharge_SaveDelete();
                    enableDisableDocuments_SaveDelete();
                    enableDisablePanDocumentsFields(false);
                    //                    enableDisableTextBox();
                    setInvisible();
                    setDisplay();
                    if(!(tdtLastInterCalcDate.getDateValue()).equalsIgnoreCase("")){
                        tdtLastInterCalcDate.setEnabled(false);
                    }
                    if(!(tdtLastInterAppDate.getDateValue()).equalsIgnoreCase("")){
                        tdtLastInterAppDate.setEnabled(false);
                    }
                    
                    txtProductID.setEnabled(false);
                    cboOperatesLike.setEnabled(false);
                }
                
                
                observable.setStatus();
                setButtonEnableDisable();
            }else if (viewType==ACCHEAD) {
                txtAccHead.setText(accountHead);
            }else if (viewType==ACCCLOSCHARGES) {
                txtAccClosCharges.setText(accountHead);
            } else if (viewType==MISCSERVCHARGES) {
                txtMiscServCharges.setText(accountHead);
            } else if (viewType==STATEMENTCHARGES) {
                txtStatementCharges.setText(accountHead);
            } else if (viewType== ACCDEBITINTER) {
                txtAccDebitInter.setText(accountHead);
            } else if (viewType==PENALINTER) {
                txtPenalInter.setText(accountHead);
            } else if (viewType==ACCCREDITINTER) {
                txtAccCreditInter.setText(accountHead);
            } else if (viewType==EXPIRYINTER) {
                txtExpiryInter.setText(accountHead);
            } else if (viewType==CHERETURNCHARGES_OUT) {
                txtCheReturnChargest_Out.setText(accountHead);
            } else if (viewType==CHERETURNCHARGES_IN) {
                txtCheReturnChargest_In.setText(accountHead);
            } else if (viewType==FOLIOCHARGESACC) {
                txtFolioChargesAcc.setText(accountHead);
            } else if (viewType==COMMITCHARGES) {
                txtCommitCharges.setText(accountHead);
            } else if (viewType==PROCESSINGCHARGES) {
                txtProcessingCharges.setText(accountHead);
            }else if(viewType==NOTICECHARGES){
                txtNoticeCharges.setText(accountHead);
            }
            else if(viewType==POSTAGECHARGES){
                txtPostageCharges.setText(accountHead);
            }else if(viewType==INTPAYABLEACCHEAD){
                txtIntPayableAccount.setText(accountHead);
            }
            else if(viewType==LEGALCHARGES){
                txtLegalCharges.setText(accountHead);
            }
            else if(viewType==ARBITRARYCHARGES){
                txtArbitraryCharges.setText(accountHead);
            }
             else if(viewType==INSURANCECHARGES){
                txtInsuranceCharges.setText(accountHead);
            }
             else if(viewType==EXECUTION_DECREE_CHARGES){
                txtExecutionDecreeCharges.setText(accountHead);
            }
             else if(viewType==SUBSIDY){
                txtSubsidyAccount.setText(accountHead);
            }else if(viewType==INSURANCEPREMIUMDEBIT){
                 txtInsurancePremiumDebit.setText(accountHead);
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
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            caseNotDelete();
        }
    }//GEN-LAST:event_tblChargesMousePressed
    // Fills the Combo box and text field with the data in table... when
    // some row is selected...
    private void caseNotDelete(){
        updateOBFields();
        observable.populateChargesTab(tblCharges.getSelectedRow());
        ClientUtil.enableDisable(panChequeReturnCharges,true);
        
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
        ClientUtil.enableDisable(panChequeReturnCharges,false);
        enableDisableCharge_SaveDelete();
        observable.resetTab();
    }//GEN-LAST:event_btnCharge_DeleteActionPerformed
    
    private void btnCharge_SaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCharge_SaveActionPerformed
        //To enter the data into the Table from the Combo box and The Text Fields...
        //local variable on the basis of what table buttons are
        // Enabled or disabled...
        int result=0;
        updateOBFields();
        if(txtRangeToRule().length() > 0){
            displayAlert(txtRangeToRule());
        }else{
            result=observable.addChargesTab();
            ClientUtil.enableDisable(panChequeReturnCharges,false);
            enableDisableCharge_SaveDelete();
            if (result == 2){
                enableDisableCharge_New();
            }
        }
    }//GEN-LAST:event_btnCharge_SaveActionPerformed
    // To Enable or Disable the fields of the Table in Charges...
    // When Save(associated with the table only) is Pressed...
    private void enableDisableCharge_SaveDelete(){
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
    private void enableDisableCharge_New(){
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
    }//GEN-LAST:event_btnSaveActionPerformed
    // Does the necessary functions when Save Button is Pressed...
    private void savePerformed(){
        updateOBFields();
        
        //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
        StringBuffer strBAlert = new StringBuffer();
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLoanProduct);
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            strBAlert.append(mandatoryMessage+"\n");
        }
        if(txtMaxDebitInterRateRule().length() > 0){
            strBAlert.append(txtMaxDebitInterRateRule()+"\n");
        }
        if(txtMaxDebitInterAmtRule().length() > 0){
            strBAlert.append(txtMaxDebitInterAmtRule()+"\n");
        }
        if(cboMaxPeriodRule().length() > 0){
            strBAlert.append(cboMaxPeriodRule()+"\n");
        }
        if(txtMaxAmtLoanRule().length() > 0){
            strBAlert.append(txtMaxAmtLoanRule()+"\n");
        }
        if(txtApplicableInterRule().length() > 0){
            strBAlert.append(txtApplicableInterRule()+"\n");
        }
        if(checkPenalDue().length() > 0) {
            strBAlert.append(checkPenalDue()+"\n");
        }
        if(txtNumPatternFollowed.getText().length()<=0)
        {
           strBAlert.append(resourceBundle.getString("PREFIXWARNING"));  
        }
        if(txtNumPatternFollowedSuffix.getText().length()<=0)
        {
            strBAlert.append(resourceBundle.getString("SUFFIXWARNING"));
        }
        //__ Testing the Length of the Combination for the Days...
        String str = "";
        
        str = periodLengthValidation(txtMinPeriodsArrears, cboMinPeriodsArrears);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtPeriodTranSStanAssets, cboPeriodTranSStanAssets);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtPeriodTransDoubtfulAssets1, cboPeriodTransDoubtfulAssets1);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtPeriodTransDoubtfulAssets2, cboPeriodTransDoubtfulAssets2);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtPeriodTransDoubtfulAssets3, cboPeriodTransDoubtfulAssets3);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtPeriodTransLossAssets, cboPeriodTransLossAssets);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtPeriodAfterWhichTransNPerformingAssets, cboPeriodAfterWhichTransNPerformingAssets);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtMinPeriod, cboMinPeriod);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        str = periodLengthValidation(txtMaxPeriod, cboMaxPeriod);
        if(str.length() > 0){
            strBAlert.append(str+"\n");
            str = "";
        }
        
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0 ){
            displayAlert(strBAlert.toString());
        }else{
            
            //__ If the Behaves like is OD or CC, Display an alert to enter the Data in the Advances...
            final String BehavesLike = CommonUtil.convertObjToStr(((ComboBoxModel) cboOperatesLike.getModel()).getKeyForSelected());
            final int actionType = observable.getActionType();
            
            HashMap dataMap = new HashMap();
            dataMap.put("ACTION_TYPE", String.valueOf(actionType));
            dataMap.put("PROD_ID", CommonUtil.convertObjToStr(txtProductID.getText()));
            dataMap.put("PROD_DESC",CommonUtil.convertObjToStr(txtProductDesc.getText()));
            dataMap.put("AGRI","AGRI");
            System.out.println("BehavesLike: " +BehavesLike);
            
            observable.doAction();
            
            //__ if the Action is not Falied, Reset the fields...
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
//                super.removeEditLock(txtProductID.getText());
                observable.setResultStatus();
                observable.resetTable();
                
                ClientUtil.enableDisable(this, false);
                //disableButtons();
                if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
                    enableDisableButtons();
                    disableButtons();
                }
                btnCharge_New.setEnabled(false);
                enableDisableDocuments_NewSaveDelete(false);
                observable.resetForm();
                enableDisableTextBox();
                setButtonEnableDisable();
                
                
                //__ Make the Screen Closable..
                setModified(false);
                
                //__ To Call the Advances Screen..
                //                if((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                //                && (BehavesLike.equalsIgnoreCase("OD") || BehavesLike.equalsIgnoreCase("CC"))){
                //                    displayAlert(resourceBundle.getString("OD_CCALERT"));
                //                }
                
                if((actionType != ClientConstants.ACTIONTYPE_DELETE) && (BehavesLike.equalsIgnoreCase("AOD") || BehavesLike.equalsIgnoreCase("ACC"))){
                    String[] options = {resourceBundle.getString("cDialogYes"),resourceBundle.getString("cDialogNo")};
                    int option = COptionPane.showOptionDialog(null, resourceBundle.getString("OD_CCALERT"), CommonConstants.INFORMATIONTITLE,
                    COptionPane.YES_NO_OPTION, COptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                    
                    System.out.println("option: " + option);
                    if (option == 0){    //__ Yes is selected
                        TrueTransactMain.showScreen(new AdvancesProductUI(dataMap));
                    }
                }
            }
        }
    }
    
    private String checkPenalDue(){
        if (rdoPenalAppl_Yes.isSelected()) {
            if (!(chkprincipalDue.isSelected() || chkInterestDue.isSelected()))
                return "Select Penal applicable on Principal or Interest";
        }
        return "";
    }
    
    private String periodLengthValidation(CTextField txtField, CComboBox comboField){
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)){
            message = objMandatoryRB.getString(txtField.getName());
        }
        return message;
    }
    
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    /*
     * To Disable those fields which are to be displayed only.
     */
    private void setDisplay(){
        txtLastAccNum.setEditable(false);
        //        tdtLastInterCalcDate.setEnabled(false);
        //        tdtLastInterAppDate.setEnabled(false);
        tdtNextFolioDDate.setEnabled(false);
    }
    
    
    /*
     * To set the fields in the Non-Per-UI as Invisible...
     */
    private void setInvisible(){
        panChequeReturnCharges.setVisible(false);

        cboProdCurrency.setVisible(false);
        lblProdCurrency.setVisible(false);
        
        lblMinPeriodsArrears.setVisible(false);
        panMinPeriodsArrears.setVisible(false);
        
        lblPeriodAfterWhichTransNPerformingAssets.setVisible(false);
        panPeriodAfterWhichTransNPerformingAssets.setVisible(false);
    }
    private void enableDisableNoticeCharge_SaveDelete(boolean flag){

        btnNotice_Charge_Delete.setEnabled(!flag);
        btnNotice_Charge_Save.setEnabled(!flag);
        btnNotice_Charge_New.setEnabled(flag);
    }
    
    // If the Debit interest to be charged is false... Disable fields...
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();// reset all the fields in the Screen...
        observable.resetTable();// reset the table in Charges...
        txtReset();//reset the fields associated with Radio buttons...
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
        ClientUtil.enableDisable(panNoticeCharges,false);
        enableDisableNoticeCharge_SaveDelete(true);
        setDisplay();                   // To set some fields as Disable...
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
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboOperatesLike.setModel(observable.getCbmOperatesLike());
        cboDebInterCalcFreq.setModel(observable.getCbmDebInterCalcFreq());
        cboDebitInterAppFreq.setModel(observable.getCbmDebitInterAppFreq());
        cboDebitInterComFreq.setModel(observable.getCbmDebitInterComFreq());
        cboDebitProdRoundOff.setModel(observable.getCbmDebitProdRoundOff());
        cboDebitInterRoundOff.setModel(observable.getCbmDebitInterRoundOff());
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
        cboReviewPeriod.setModel(observable.getCbmReviewPeriod());
        
        
        cboCharge_Limit2.setModel(observable.getCbmCharge_Limit2());
        cboCharge_Limit3.setModel(observable.getCbmCharge_Limit3());
        cboDocumentType.setModel(observable.getCbmDocumentType());
        
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
       cboInsuranceType.setModel(observable.getCbmInsuranceType());
       cboInsuranceUnderScheme.setModel(observable.getCbmInsuranceUnderScheme());
       tblInsuranceDetails.setModel(observable.getTblInsurance());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccClosCharges;
    private com.see.truetransact.uicomponent.CButton btnAccCreditInter;
    private com.see.truetransact.uicomponent.CButton btnAccDebitInter;
    private com.see.truetransact.uicomponent.CButton btnAccHead;
    private com.see.truetransact.uicomponent.CButton btnArbitraryCharges;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCharge_Delete;
    private com.see.truetransact.uicomponent.CButton btnCharge_New;
    private com.see.truetransact.uicomponent.CButton btnCharge_Save;
    private com.see.truetransact.uicomponent.CButton btnCheReturnChargest_In;
    private com.see.truetransact.uicomponent.CButton btnCheReturnChargest_Out;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCommitCharges;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDocumentDelete;
    private com.see.truetransact.uicomponent.CButton btnDocumentNew;
    private com.see.truetransact.uicomponent.CButton btnDocumentSave;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExecutionDecreeCharges;
    private com.see.truetransact.uicomponent.CButton btnExpiryInter;
    private com.see.truetransact.uicomponent.CButton btnFolioChargesAcc;
    private com.see.truetransact.uicomponent.CButton btnInsuranceCharges;
    private com.see.truetransact.uicomponent.CButton btnInsuranceDelete;
    private com.see.truetransact.uicomponent.CButton btnInsuranceNew;
    private com.see.truetransact.uicomponent.CButton btnInsurancePremiumDebit;
    private com.see.truetransact.uicomponent.CButton btnInsuranceSave;
    private com.see.truetransact.uicomponent.CButton btnIntPayableAccount;
    private com.see.truetransact.uicomponent.CButton btnLegalCharges;
    private com.see.truetransact.uicomponent.CButton btnMiscServCharges;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNoticeCharges;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_Delete;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_New;
    private com.see.truetransact.uicomponent.CButton btnNotice_Charge_Save;
    private com.see.truetransact.uicomponent.CButton btnPenalInter;
    private com.see.truetransact.uicomponent.CButton btnPostageCharges;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnProcessingCharges;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnStatementCharges;
    private com.see.truetransact.uicomponent.CButton btnSubsidyAccount;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cbo20Code;
    private com.see.truetransact.uicomponent.CComboBox cboCharge_Limit2;
    private com.see.truetransact.uicomponent.CComboBox cboCharge_Limit3;
    private com.see.truetransact.uicomponent.CComboBox cboCommodityCode;
    private com.see.truetransact.uicomponent.CComboBox cboDebInterCalcFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitInterAppFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitInterComFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitInterRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboDebitProdRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboDocumentType;
    private com.see.truetransact.uicomponent.CComboBox cboFolioChargesAppFreq;
    private com.see.truetransact.uicomponent.CComboBox cboGovtSchemeCode;
    private com.see.truetransact.uicomponent.CComboBox cboGuaranteeCoverCode;
    private com.see.truetransact.uicomponent.CComboBox cboHealthCode;
    private com.see.truetransact.uicomponent.CComboBox cboIncompleteFolioRoundOffFreq;
    private com.see.truetransact.uicomponent.CComboBox cboIndusCode;
    private com.see.truetransact.uicomponent.CComboBox cboInsuranceType;
    private com.see.truetransact.uicomponent.CComboBox cboInsuranceUnderScheme;
    private com.see.truetransact.uicomponent.CComboBox cboIssueAfter;
    private com.see.truetransact.uicomponent.CComboBox cboLoanPeriodMul;
    private com.see.truetransact.uicomponent.CComboBox cboMaxPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboMinPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboMinPeriodsArrears;
    private com.see.truetransact.uicomponent.CComboBox cboNoticeType;
    private com.see.truetransact.uicomponent.CComboBox cboOperatesLike;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CComboBox cboProdCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboProductFreq;
    private com.see.truetransact.uicomponent.CComboBox cboPurposeCode;
    private com.see.truetransact.uicomponent.CComboBox cboRefinancingInsti;
    private com.see.truetransact.uicomponent.CComboBox cboReviewPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboSectorCode;
    private com.see.truetransact.uicomponent.CComboBox cboSecurityDeails;
    private com.see.truetransact.uicomponent.CComboBox cboToCollectFolioCharges;
    private com.see.truetransact.uicomponent.CComboBox cboTypeFacility;
    private com.see.truetransact.uicomponent.CComboBox cbocalcType;
    private com.see.truetransact.uicomponent.CCheckBox chkDirectFinance;
    private com.see.truetransact.uicomponent.CCheckBox chkECGC;
    private com.see.truetransact.uicomponent.CCheckBox chkInterestDue;
    private com.see.truetransact.uicomponent.CCheckBox chkQIS;
    private com.see.truetransact.uicomponent.CCheckBox chkprincipalDue;
    private com.see.truetransact.uicomponent.CLabel lbStatementCharges;
    private com.see.truetransact.uicomponent.CLabel lbl20Code;
    private com.see.truetransact.uicomponent.CLabel lblATMcardIssued;
    private com.see.truetransact.uicomponent.CLabel lblAccClosCharges;
    private com.see.truetransact.uicomponent.CLabel lblAccClosingCharges;
    private com.see.truetransact.uicomponent.CLabel lblAccCreditInter;
    private com.see.truetransact.uicomponent.CLabel lblAccDebitInter;
    private com.see.truetransact.uicomponent.CLabel lblAccHead;
    private com.see.truetransact.uicomponent.CLabel lblApplicableInter;
    private com.see.truetransact.uicomponent.CLabel lblApplicableInterPer;
    private com.see.truetransact.uicomponent.CLabel lblApplicableInterPer1;
    private com.see.truetransact.uicomponent.CLabel lblArbitraryCharges;
    private com.see.truetransact.uicomponent.CLabel lblBankSharePremium;
    private com.see.truetransact.uicomponent.CLabel lblChargeAppliedType;
    private com.see.truetransact.uicomponent.CLabel lblCharge_1;
    private com.see.truetransact.uicomponent.CLabel lblCharge_2;
    private com.see.truetransact.uicomponent.CLabel lblCheReturnChargest_In;
    private com.see.truetransact.uicomponent.CLabel lblCheReturnChargest_Out;
    private com.see.truetransact.uicomponent.CLabel lblCommitCharges;
    private com.see.truetransact.uicomponent.CLabel lblCommitmentCharge;
    private com.see.truetransact.uicomponent.CLabel lblCommodityCode;
    private com.see.truetransact.uicomponent.CLabel lblCreditCardIssued;
    private com.see.truetransact.uicomponent.CLabel lblCustomerSharePremium;
    private com.see.truetransact.uicomponent.CLabel lblDebInterCalcFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitInterAppFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitInterComFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitInterRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDebitProdRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDirectFinance;
    private com.see.truetransact.uicomponent.CLabel lblDocumentDesc;
    private com.see.truetransact.uicomponent.CLabel lblDocumentNo;
    private com.see.truetransact.uicomponent.CLabel lblDocumentType;
    private com.see.truetransact.uicomponent.CLabel lblECGC;
    private com.see.truetransact.uicomponent.CLabel lblExecutionDecreeCharges;
    private com.see.truetransact.uicomponent.CLabel lblExpiryInter;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Policy;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Policy_Per;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Prud;
    private com.see.truetransact.uicomponent.CLabel lblExposureLimit_Prud_Per;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargesAcc;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargesAppFreq;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargesAppl;
    private com.see.truetransact.uicomponent.CLabel lblGovtSchemeCode;
    private com.see.truetransact.uicomponent.CLabel lblGuaranteeCoverCode;
    private com.see.truetransact.uicomponent.CLabel lblHealthCode;
    private com.see.truetransact.uicomponent.CLabel lblIncompleteFolioRoundOffFreq;
    private com.see.truetransact.uicomponent.CLabel lblIndusCode;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceAmt;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceCharges;
    private com.see.truetransact.uicomponent.CLabel lblInsurancePremiumDebit;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceType;
    private com.see.truetransact.uicomponent.CLabel lblInsuranceUnderSchume;
    private com.see.truetransact.uicomponent.CLabel lblIntPayableAccount;
    private com.see.truetransact.uicomponent.CLabel lblIsAnyBranBankingAllowed;
    private com.see.truetransact.uicomponent.CLabel lblIsDebitInterUnderClearing;
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
    private com.see.truetransact.uicomponent.CLabel lblMaxAmtCashTrans;
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
    private com.see.truetransact.uicomponent.CLabel lblNextFolioDDate;
    private com.see.truetransact.uicomponent.CLabel lblNoEntriesPerFolio;
    private com.see.truetransact.uicomponent.CLabel lblNoticeChargeAmt;
    private com.see.truetransact.uicomponent.CLabel lblNoticeCharges;
    private com.see.truetransact.uicomponent.CLabel lblNoticeType;
    private com.see.truetransact.uicomponent.CLabel lblNumPatternFollowed;
    private com.see.truetransact.uicomponent.CLabel lblOperatesLike;
    private com.see.truetransact.uicomponent.CLabel lblPLRAppForm;
    private com.see.truetransact.uicomponent.CLabel lblPLRApplExistingAcc;
    private com.see.truetransact.uicomponent.CLabel lblPLRApplNewAcc;
    private com.see.truetransact.uicomponent.CLabel lblPLRRate;
    private com.see.truetransact.uicomponent.CLabel lblPLRRateAppl;
    private com.see.truetransact.uicomponent.CLabel lblPLRRate_Per;
    private com.see.truetransact.uicomponent.CLabel lblPenalAppl;
    private com.see.truetransact.uicomponent.CLabel lblPenalApplicableAmt;
    private com.see.truetransact.uicomponent.CLabel lblPenalDue;
    private com.see.truetransact.uicomponent.CLabel lblPenalInter;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterRate;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterRate_Per;
    private com.see.truetransact.uicomponent.CLabel lblPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CLabel lblPlrApplAccSancForm;
    private com.see.truetransact.uicomponent.CLabel lblPostageAmt;
    private com.see.truetransact.uicomponent.CLabel lblPostageCharges;
    private com.see.truetransact.uicomponent.CLabel lblProcessCharges;
    private com.see.truetransact.uicomponent.CLabel lblProcessingCharges;
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
    private com.see.truetransact.uicomponent.CLabel lblRefinancingInsti;
    private com.see.truetransact.uicomponent.CLabel lblReviewPeriodLoan;
    private com.see.truetransact.uicomponent.CLabel lblSectorCode;
    private com.see.truetransact.uicomponent.CLabel lblSecurityDeails;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpaces;
    private com.see.truetransact.uicomponent.CLabel lblStatCharges;
    private com.see.truetransact.uicomponent.CLabel lblStatChargesRate;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblSubsidy;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyAccount;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyAmt;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyFromDt;
    private com.see.truetransact.uicomponent.CLabel lblSubsidyToDate;
    private com.see.truetransact.uicomponent.CLabel lblSunbsidy;
    private com.see.truetransact.uicomponent.CLabel lblSunbsidy1;
    private com.see.truetransact.uicomponent.CLabel lblToChargeOn;
    private com.see.truetransact.uicomponent.CLabel lblToCollectFolioCharges;
    private com.see.truetransact.uicomponent.CLabel lblTypeFacility;
    private com.see.truetransact.uicomponent.CLabel lblasAndWhenCustomer;
    private com.see.truetransact.uicomponent.CLabel lblcalcType;
    private com.see.truetransact.uicomponent.CLabel lblcalendar;
    private com.see.truetransact.uicomponent.CLabel lbldebitInterCharged;
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
    private com.see.truetransact.uicomponent.CPanel panApplicableInter;
    private com.see.truetransact.uicomponent.CPanel panBankSharePremium;
    private com.see.truetransact.uicomponent.CPanel panButton;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCalendarFrequency;
    private com.see.truetransact.uicomponent.CPanel panCharge;
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
    private com.see.truetransact.uicomponent.CPanel panCustomerSharePremium;
    private com.see.truetransact.uicomponent.CPanel panDocumentFields;
    private com.see.truetransact.uicomponent.CPanel panDocumentTable;
    private com.see.truetransact.uicomponent.CPanel panDocuments;
    private com.see.truetransact.uicomponent.CPanel panExposureLimit_Policy;
    private com.see.truetransact.uicomponent.CPanel panExposureLimit_Prud;
    private com.see.truetransact.uicomponent.CPanel panFolio;
    private com.see.truetransact.uicomponent.CPanel panFolioChargesAppl;
    private com.see.truetransact.uicomponent.CPanel panFolio_Date;
    private com.see.truetransact.uicomponent.CPanel panFolio_Freq;
    private com.see.truetransact.uicomponent.CPanel panInsuranceButton;
    private com.see.truetransact.uicomponent.CPanel panInsuranceDetails;
    private com.see.truetransact.uicomponent.CPanel panInsuranceTabComDetails;
    private com.see.truetransact.uicomponent.CPanel panInsuranceTabDetails;
    private com.see.truetransact.uicomponent.CPanel panInterCalculation;
    private com.see.truetransact.uicomponent.CPanel panInterReceivable;
    private com.see.truetransact.uicomponent.CPanel panInterReceivable_Debit;
    private com.see.truetransact.uicomponent.CPanel panInterReceivable_PLR;
    private com.see.truetransact.uicomponent.CPanel panIsDebitInterUnderClearing;
    private com.see.truetransact.uicomponent.CPanel panIsLimitDefnAllowed;
    private com.see.truetransact.uicomponent.CPanel panIsStaffAccOpened;
    private com.see.truetransact.uicomponent.CPanel panLimitExpiryInter;
    private com.see.truetransact.uicomponent.CPanel panLoanProduct;
    private com.see.truetransact.uicomponent.CPanel panMaxDebitInterRate;
    private com.see.truetransact.uicomponent.CPanel panMaxPeriod;
    private com.see.truetransact.uicomponent.CPanel panMinDebitInterRate;
    private com.see.truetransact.uicomponent.CPanel panMinInterDebited;
    private com.see.truetransact.uicomponent.CPanel panMinPeriod;
    private com.see.truetransact.uicomponent.CPanel panMinPeriod1;
    private com.see.truetransact.uicomponent.CPanel panMinPeriodsArrears;
    private com.see.truetransact.uicomponent.CPanel panNonPerAssets;
    private com.see.truetransact.uicomponent.CPanel panNoticeButton;
    private com.see.truetransact.uicomponent.CPanel panNoticeCharges;
    private com.see.truetransact.uicomponent.CPanel panNoticeCharges_Table;
    private com.see.truetransact.uicomponent.CPanel panNoticecharge_Amt;
    private com.see.truetransact.uicomponent.CPanel panOtherItem;
    private com.see.truetransact.uicomponent.CPanel panPLRApplExistingAcc;
    private com.see.truetransact.uicomponent.CPanel panPLRApplNewAcc;
    private com.see.truetransact.uicomponent.CPanel panPLRRate;
    private com.see.truetransact.uicomponent.CPanel panPLRRateAppl;
    private com.see.truetransact.uicomponent.CPanel panPenalAppl;
    private com.see.truetransact.uicomponent.CPanel panPenalAppl_Due;
    private com.see.truetransact.uicomponent.CPanel panPenalApplicableAmt;
    private com.see.truetransact.uicomponent.CPanel panPenalInterRate;
    private com.see.truetransact.uicomponent.CPanel panPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CPanel panPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CPanel panPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CPanel panProcessCharge;
    private com.see.truetransact.uicomponent.CPanel panProcessCharges;
    private com.see.truetransact.uicomponent.CPanel panSpeItems;
    private com.see.truetransact.uicomponent.CPanel panSpecial_NonPerfoormingAssets;
    private com.see.truetransact.uicomponent.CPanel panStatCharges;
    private com.see.truetransact.uicomponent.CPanel panStateCharges;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubsidy;
    private com.see.truetransact.uicomponent.CPanel panToChargeOn;
    private com.see.truetransact.uicomponent.CPanel panToChargeType;
    private com.see.truetransact.uicomponent.CPanel panasAndWhenCustomer;
    private com.see.truetransact.uicomponent.CPanel pancharge_Amt;
    private com.see.truetransact.uicomponent.CPanel panldebitInterCharged;
    private com.see.truetransact.uicomponent.CButtonGroup rdoATMcardIssued;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMcardIssued_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMcardIssued_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCalendarFrequency;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCommitmentCharge;
    private com.see.truetransact.uicomponent.CRadioButton rdoCommitmentCharge_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCommitmentCharge_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditCardIssued;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCardIssued_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCardIssued_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFolioChargeType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFolioChargesAppl;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioChargesAppl_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioChargesAppl_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsAnyBranBankingAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsAnyBranBankingAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsAnyBranBankingAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsDebitInterUnderClearing;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDebitInterUnderClearing_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsDebitInterUnderClearing_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsLimitDefnAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsLimitDefnAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsLimitDefnAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsStaffAccOpened;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStaffAccOpened_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsStaffAccOpened_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLimitExpiryInter;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitExpiryInter_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitExpiryInter_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMobileBanlingClient;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobileBanlingClient_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobileBanlingClient_Yes;
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
    private com.see.truetransact.uicomponent.CButtonGroup rdoProcessCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoProcessCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatCharges;
    private com.see.truetransact.uicomponent.CRadioButton rdoStatCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStatCharges_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
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
    private com.see.truetransact.uicomponent.CRadioButton rdocalendarFrequency_No;
    private com.see.truetransact.uicomponent.CRadioButton rdocalendarFrequency_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoldebitInterCharged;
    private com.see.truetransact.uicomponent.CRadioButton rdoldebitInterCharged_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoldebitInterCharged_Yes;
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
    private com.see.truetransact.uicomponent.CScrollPane srpInsuranceDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpNoticeCharges;
    private com.see.truetransact.uicomponent.CTabbedPane tabLoanProduct;
    private com.see.truetransact.uicomponent.CTable tblCharges;
    private com.see.truetransact.uicomponent.CTable tblDocuments;
    private com.see.truetransact.uicomponent.CTable tblInsuranceDetails;
    private com.see.truetransact.uicomponent.CTable tblNoticeCharges;
    private javax.swing.JToolBar tbrLoantProduct;
    private com.see.truetransact.uicomponent.CDateField tdtLastFolioChargesAppl;
    private com.see.truetransact.uicomponent.CDateField tdtLastInterAppDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastInterCalcDate;
    private com.see.truetransact.uicomponent.CDateField tdtNextFolioDDate;
    private com.see.truetransact.uicomponent.CDateField tdtPLRAppForm;
    private com.see.truetransact.uicomponent.CDateField tdtPlrApplAccSancForm;
    private com.see.truetransact.uicomponent.CDateField tdtSubsidyFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtSubsidyToDate;
    private com.see.truetransact.uicomponent.CTextField txtAccClosCharges;
    private com.see.truetransact.uicomponent.CTextField txtAccClosingCharges;
    private com.see.truetransact.uicomponent.CTextField txtAccCreditInter;
    private com.see.truetransact.uicomponent.CTextField txtAccDebitInter;
    private com.see.truetransact.uicomponent.CTextField txtAccHead;
    private com.see.truetransact.uicomponent.CTextField txtApplicableInter;
    private com.see.truetransact.uicomponent.CTextField txtArbitraryCharges;
    private com.see.truetransact.uicomponent.CTextField txtBankSharePremium;
    private com.see.truetransact.uicomponent.CTextField txtCharge_Limit2;
    private com.see.truetransact.uicomponent.CTextField txtCharge_Limit3;
    private com.see.truetransact.uicomponent.CTextField txtCheReturnChargest_In;
    private com.see.truetransact.uicomponent.CTextField txtCheReturnChargest_Out;
    private com.see.truetransact.uicomponent.CTextField txtCommitCharges;
    private com.see.truetransact.uicomponent.CTextField txtCustomerSharePremium;
    private com.see.truetransact.uicomponent.CTextField txtDocumentDesc;
    private com.see.truetransact.uicomponent.CTextField txtDocumentNo;
    private com.see.truetransact.uicomponent.CTextField txtExecutionDecreeCharges;
    private com.see.truetransact.uicomponent.CTextField txtExpiryInter;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Policy;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Policy2;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Prud;
    private com.see.truetransact.uicomponent.CTextField txtExposureLimit_Prud2;
    private com.see.truetransact.uicomponent.CTextField txtFolioChargesAcc;
    private com.see.truetransact.uicomponent.CTextField txtInsuranceAmt;
    private com.see.truetransact.uicomponent.CTextField txtInsuranceCharges;
    private com.see.truetransact.uicomponent.CTextField txtInsurancePremiumDebit;
    private com.see.truetransact.uicomponent.CTextField txtIntPayableAccount;
    private com.see.truetransact.uicomponent.CTextField txtLastAccNum;
    private com.see.truetransact.uicomponent.CTextField txtLegalCharges;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmtCashTrans;
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
    private com.see.truetransact.uicomponent.CTextField txtNoEntriesPerFolio;
    private com.see.truetransact.uicomponent.CTextField txtNoticeChargeAmt;
    private com.see.truetransact.uicomponent.CTextField txtNoticeCharges;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowed;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowedSuffix;
    private com.see.truetransact.uicomponent.CTextField txtPLRRate;
    private com.see.truetransact.uicomponent.CTextField txtPenalApplicableAmt;
    private com.see.truetransact.uicomponent.CTextField txtPenalInter;
    private com.see.truetransact.uicomponent.CTextField txtPenalInterRate;
    private com.see.truetransact.uicomponent.CTextField txtPeriodAfterWhichTransNPerformingAssets;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTranSStanAssets;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransDoubtfulAssets1;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransDoubtfulAssets2;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransDoubtfulAssets3;
    private com.see.truetransact.uicomponent.CTextField txtPeriodTransLossAssets;
    private com.see.truetransact.uicomponent.CTextField txtPostageChargeAmt;
    private com.see.truetransact.uicomponent.CTextField txtPostageCharges;
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
    private com.see.truetransact.uicomponent.CTextField txtReviewPeriod;
    private com.see.truetransact.uicomponent.CTextField txtStatChargesRate;
    private com.see.truetransact.uicomponent.CTextField txtStatementCharges;
    private com.see.truetransact.uicomponent.CTextField txtSubsidyAccount;
    private com.see.truetransact.uicomponent.CTextField txtSubsidyAmt;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        NewLoanAgriProductUI  gui = new NewLoanAgriProductUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}

/*
 *
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * LoanProductOB.java
 *
 * Created on November 24, 2003, 10:56 AM
 */

package com.see.truetransact.ui.product.loan;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.commonutil.LocaleConstants;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.transferobject.product.loan.LoanProductAccountTO;
import com.see.truetransact.transferobject.product.loan.LoanProductGroupLoanTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccountParamTO;
import com.see.truetransact.transferobject.product.loan.LoanProductInterReceivableTO;
import com.see.truetransact.transferobject.product.loan.LoanProductChargesTO;
import com.see.truetransact.transferobject.product.loan.LoanProductChargesTabTO;
import com.see.truetransact.transferobject.product.loan.LoanProductSpeItemsTO;
import com.see.truetransact.transferobject.product.loan.LoanProductAccHeadTO;
import com.see.truetransact.transferobject.product.loan.LoanProductNonPerAssetsTO;
import com.see.truetransact.transferobject.product.loan.LoanProductInterCalcTO;
import com.see.truetransact.transferobject.product.loan.LoanProductSubsidyInterestRebateTO;
import com.see.truetransact.transferobject.product.loan.LoanProductDocumentsTO;
import com.see.truetransact.transferobject.product.loan.LoanProductClassificationsTO;

import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.ui.TrueTransactMain;

import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import javax.swing.table.TableModel;
import javax.swing.DefaultListModel;

import com.see.truetransact.uicomponent.CObservable;
//import javax.swing.table.*;
/**
 *
 * @author  rahul
 * Modified by prasath
 * Documents tab added
 */
public class NewLoanProductOB extends CObservable {
    private String chkExcludeTOD="";
    private String chkEMIInSimpleIntrst="";
    private String chkGroupLoan="";
    private String chkShareLink="";
    private String chkExcludeScSt="";
    private String chkFixed="";
    private String chkAuctionAllowed="";
    Date curDate = null;
    private ComboBoxModel cbmIntCalcDay;
    private HashMap lookUpHash;
    private HashMap operationMap;
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private ArrayList chargeTabRow;
    final ArrayList chargeTabTitle = new ArrayList();
    final ArrayList documentsTabTitle = new ArrayList();
    final ArrayList noticeChargeTabTitle=new ArrayList();
    private ProxyFactory proxy = null;
    private String cboNoticeType="";
    private String cboIssueAfter="";
    private String txtNoticeChargeAmt="";
    private String txtPostageChargeAmt="";
    private double txtMaxCashDisb = 0.0;
    private String cboIntCalcDay = "";
    private String cboIntCalcMonth = "";
    private String txtDebitAllowed = "";
    private String txtSuspenseCreditAchd = "";
    private String txtSuspenseDebitAchd = "";
    
    private int actionType;/** To set the status based on ActionType, either New, Edit, etc., */
    private int result;
    
    private double periodData = 0;// for setting data depending on period comboboxes...
    private double resultData = 0;// for setting data depending on period comboboxes...
    
    private int resultValue=0;// for retrieving data from the period comboboxes...
    private int depAmtPeriod=0;
    private final String MANUAL = "M";
    private final String SYSTEM = "S";
    private final String BOTH = "B";
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final String YEAR1 ="Years";
    private final String MONTH1 ="Months";
    private final String DAY1 ="Days";
    
    private final String YES ="Y";
    private final String NO ="N";
    private String duration = "";
    
    private final int year = 365;
    private final int month = 30;
    private final int day = 1;
    
    final String ABSOLUTE = "Absolute";
    final String PERCENT = "Percent";
    private EnhancedTableModel tblNoticeCharge;
    private EnhancedTableModel tblChargeTab;
    private EnhancedTableModel tblDocuments;
    // To get the Value of Column Title and Dialogue Box...
    //    final NewLoanProductRB objLoanProductRB = new NewLoanProductRB();
    java.util.ResourceBundle objLoanProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.loan.NewLoanProductRB", ProxyParameters.LANGUAGE);
    
    private ComboBoxModel cbmOperatesLike;
    private ComboBoxModel cbmProdCategory;
    private ComboBoxModel cbmProdHoliday;
    private ComboBoxModel cbmIntCalcMonth;
    private ComboBoxModel cbmDebInterCalcFreq;
    private ComboBoxModel cbmDebitInterAppFreq;
    private ComboBoxModel cbmDebitInterComFreq;
    private ComboBoxModel cbmDebitProdRoundOff;
    private ComboBoxModel cbmDebitInterRoundOff;
    private ComboBoxModel cbmInterestRepaymentFreq;
    private ComboBoxModel cbmProductFreq;
    private ComboBoxModel cbmDepositRoundOff;
    //-- Added by nithya  [ add two firelds under product ]    
    private ComboBoxModel cbmRepaymentType;
    private ComboBoxModel cbmRepaymentFreq;    
    //-- End    
    private ComboBoxModel cbmReviewPeriod;
    private ComboBoxModel cbmDepAmtMaturingPeriod;
    //    private ComboBoxModel cbmProdCurrency;
    private ComboBoxModel cbmFolioChargesAppFreq;
    private ComboBoxModel cbmToCollectFolioCharges;
    private ComboBoxModel cbmIncompleteFolioRoundOffFreq;
    private ComboBoxModel cbmMinPeriodsArrears;
    private ComboBoxModel cbmPeriodTranSStanAssets;
    private ComboBoxModel cbmPeriodTransDoubtfulAssets1;
    private ComboBoxModel cbmPeriodTransDoubtfulAssets2;
    private ComboBoxModel cbmPeriodTransDoubtfulAssets3;
    private ComboBoxModel cbmPeriodTransLossAssets;
    private ComboBoxModel cbmPeriodAfterWhichTransNPerformingAssets;
    private ComboBoxModel cbmcalcType;
    private ComboBoxModel cbmMinPeriod;
    private ComboBoxModel cbmMaxPeriod;
    private ComboBoxModel cbmLoanPeriodMul;
    private ComboBoxModel cbmCharge_Limit2;
    private ComboBoxModel cbmCharge_Limit3;
    
    
    private ComboBoxModel cbmCommodityCode;
    private ComboBoxModel cbmGuaranteeCoverCode;
    private ComboBoxModel cbmSectorCode;
    private ComboBoxModel cbmHealthCode;
    private ComboBoxModel cbmTypeFacility;
    private ComboBoxModel cbmPurposeCode;
    private ComboBoxModel cbmIndusCode;
    private ComboBoxModel cbm20Code;
    private ComboBoxModel cbmGovtSchemeCode;
    private ComboBoxModel cbmRefinancingInsti;
    private ComboBoxModel cbmSecurityDeails;
    
    //SUBSIDY
    private ComboBoxModel cbmInterestRebateCalculation;
    private ComboBoxModel cbmRebatePeriod;
    private boolean rdoSubsidy_Yes =false;
    private boolean rdoSubsidy_No = false;
    private boolean rdoLoanBalance_Yes=false;
    private boolean rdoSubsidyAdjustLoanBalance_Yes=false;
    private boolean rdoSubsidyReceivedDate=false;
    private boolean rdoLoanOpenDate =false;
    private boolean rdoInterestRebateAllowed_Yes=false;
    private boolean rdoInterestRebateAllowed_No=false;
    private boolean rdoDisbAftMoraPerd_Yes = false;
    private boolean rdoDisbAftMoraPerd_No = false;
    
    private double txtFixedMargin =0.0;
    private String  txtInterestRebatePercentage ="";
    private String  txtFinYearStartingFromDD ="";
    private String  txtFinYearStartingFromMM="";
    private boolean rdoPenalInterestWaiverAllowed_Yes=false;
    private boolean rdoPenalInterestWaiverAllowed_No=false;
    private boolean rdoPrincipalWaiverAllowed_Yes=false;
    private boolean rdoPrincipalWaiverAllowed_No=false;
    private boolean rdoDebitAllowedForCustomer_Yes=false;
    private boolean rdoDebitAllowedForCustomer_No=false;
    private boolean rdoNoticeWaiverAllowed_Yes=false;
    private boolean rdoNoticeWaiverAllowed_No=false;
    private boolean rdoInterestWaiverAllowed_Yes =false;
    private boolean rdoInterestWaiverAllowed_No =false;
    private boolean rdoInterestDueKeptReceivable_Yes =false;
    private boolean rdoInterestDueKeptReceivable_No =false;

    private boolean rdoRecoveryWaiverAllowed_Yes = false;
    private boolean rdoRecoveryWaiverAllowed_No = false;
    private boolean rdoMeasurementWaiverAllowed_Yes = false;
    private boolean rdoMeasurementWaiverAllowed_No = false;

    //-----------------------------------------------------------------------------------
    //==============================================================================================
    //Declaration of the Fields in UI
    private String txtProductID = "";
    private String txtProductDesc = "";
    private String cboOperatesLike = "";
    private String cboIfHoliday = "";
    private String cboDepositRoundOff="";
    //    private String cboProdCurrency = "";
    private String txtNumPatternFollowed = "";
    private String txtNumPatternFollowedSuffix = "";
    private String txtLastAccNum = "";
    private boolean rdoIsLimitDefnAllowed_Yes = false;
    private boolean rdoIsLimitDefnAllowed_No = false;
    private boolean rdoIsCreditAllowed_Yes = false;
    private boolean rdoIsCreditAllowed_No = false;
    private boolean rdoIsStaffAccOpened_Yes = false;
    private boolean rdoIsStaffAccOpened_No = false;
    private boolean rdoIsDebitInterUnderClearing_Yes = false;
    private boolean rdoIsDebitInterUnderClearing_No = false;
    private String txtAccHead = "";
    private String txtDebitIntDiscount="";
    private String txtRebateInterest="";
    private String txtStampAdvancesHead="";
    private String txtNoticeAdvancesHead="";
    private String txtAdvertisementHead="";
    private String txtARCEPSuspenceHead="";
    private boolean rdoldebitInterCharged_Yes = false;
    private boolean rdoldebitInterCharged_No = false;
    private boolean rdoasAndWhenCustomer_Yes=false;
    private boolean rdoasAndWhenCustomer_No=false;
    private boolean rdocalendarFrequency_Yes=false;
    private boolean rdocalendarFrequency_No=false;
    private boolean rdobill_Yes=false;
    private boolean rdobill_NO=false;
    private boolean chkprincipalDue=false;
    private boolean chkCreditStampAdvance = false;
    private boolean chkCreditNoticeAdvance = false;
    private boolean chkInterestDue = false;
    private boolean chkAuctionAllowd = false;
    private String txtMinDebitInterRate = "";
    private String txtMaxDebitInterRate = "";
    private String txtMinDebitInterAmt = "";
    private String txtMaxDebitInterAmt = "";
    private String cboDebInterCalcFreq = "";
    private String cboDebitInterAppFreq = "";
    private String cboDebitInterComFreq = "";
    //-- Added by nithya  [ add two firelds under product ]    
    private String cboRepaymentType;
    private String cboRepaymentFreq;    
    //-- End    
    private String cboDebitProdRoundOff = "";
    private String cboDebitInterRoundOff = "";
    private String tdtLastInterCalcDate = "";
    private String tdtLastInterAppDate = "";
    private boolean rdoPLRRateAppl_Yes = false;
    private boolean rdoPLRRateAppl_No = false;
    private String txtPLRRate = "";
    private String tdtPLRAppForm = "";
    private boolean rdoPLRApplNewAcc_Yes = false;
    private boolean rdoPLRApplNewAcc_No = false;
    private boolean rdoPLRApplExistingAcc_Yes = false;
    private boolean rdoPLRApplExistingAcc_No = false;
    private String tdtPlrApplAccSancForm = "";
    private boolean rdoPenalAppl_Yes = false;
    private boolean rdoPenalAppl_No = false;
    private boolean rdoLimitExpiryInter_Yes = false;
    private boolean rdoLimitExpiryInter_No = false;
    private String txtPenalInterRate = "";
    private String txtExposureLimit_Prud = "";
    private String txtExposureLimit_Prud2 = "";
    private String txtExposureLimit_Policy = "";
    private String txtExposureLimit_Policy2 = "";
    private String cboProductFreq = "";
    private String txtAccClosingCharges = "";
    private String txtMiscSerCharges = "";
    private boolean rdoStatCharges_Yes = false;
    private boolean rdoStatCharges_No = false;
    private String txtStatChargesRate = "";
    private String txtRangeFrom = "";
    private String txtRangeTo = "";
    private String txtRateAmt = "";
    private boolean rdoFolioChargesAppl_Yes = false;
    private boolean rdoFolioChargesAppl_No = false;
    private String tdtLastFolioChargesAppl = "";
    private String txtNoEntriesPerFolio = "";
    private String tdtNextFolioDDate = "";
    private String txtRatePerFolio = "";
    private String cboFolioChargesAppFreq = "";
    private String cboToCollectFolioCharges = "";
    private boolean rdoToChargeOn_Man = false;
    private boolean rdoToChargeOn_Sys = false;
    private boolean rdoToChargeOn_Both = false;
    private boolean rdoToChargeType_Credit=false;
    private boolean rdoToChargeType_Debit=false;
    private boolean rdoToChargeType_Both=false;
    private String cboIncompleteFolioRoundOffFreq = "";
    private boolean rdoProcessCharges_Yes = false;
    private boolean rdoProcessCharges_No = false;
    //    private String txtcharge_Limit1 = "";
    private String txtCharge_Limit2 = "";
    private boolean rdoCommitmentCharge_Yes = false;
    private boolean rdoCommitmentCharge_No = false;
    private String txtCharge_Limit3 = "";
    //    private String txtCharge_Limit4 = "";
    
    private String cboCharge_Limit2 = "";
    private String cboCharge_Limit3 = "";
    
    private boolean rdoATMcardIssued_Yes = false;
    private boolean rdoATMcardIssued_No = false;
    private boolean rdoCreditCardIssued_Yes = false;
    private boolean rdoCreditCardIssued_No = false;
    private boolean rdoMobileBanlingClient_Yes = false;
    private boolean rdoMobileBanlingClient_No = false;
    private boolean rdoIsAnyBranBankingAllowed_Yes = false;
    private boolean rdoIsAnyBranBankingAllowed_No = false;
    //subsidy
//    private boolean rdoLoanBalance_Yes =false;
//    private boolean  rdoSubsidyAdjustLoanBalance_Yes =false;
//    private boolean rdoSubsidyReceivedDate =false;
//    private boolean rdoLoanOpenDate =false;
//    private boolean rdoInterestRebateAllowed_Yes =false;
//    private boolean rdoInterestRebateAllowed_No=false;
//    private String txtInterestRebatePercentage="" ;
//    private String txtFinYearStartingFromDD="";
//    private String txtFinYearStartingFromMM="";
//    private boolean rdoPenalInterestWaiverAllowed_Yes=false;
//    private boolean rdoPenalInterestWaiverAllowed_No=false;
//    private boolean rdoInterestWaiverAllowed_Yes =false;
//    private boolean rdoInterestWaiverAllowed_No =false;
    
    private String txtAccClosCharges = "";
    private String txtIntPayableAccount="";
    private String txtLegalCharges="";
    private String txtPreMatIntCalctPeriod="";
    private String txtPreMatIntCollectPeriod="";
    private String txtMiscCharges="";
    private String txtArbitraryCharges="";
    private String txtInsuranceCharges="";
    private String txtExecutionDecreeCharges="";
    private String txtMiscServCharges = "";
    private String txtStatementCharges = "";
    private String txtNoticeCharges="";
    private String txtPostageCharges="";
    private String txtAccDebitInter = "";
    private String txtPenalInter = "";
    private String txtAccCreditInter = "";
    private String txtExpiryInter = "";
    private String txtCheReturnChargest_Out = "";
    private String txtCheReturnChargest_In = "";
    private String txtFolioChargesAcc = "";
    private String txtCommitCharges = "";
    private String txtProcessingCharges = "";
    private String txtMinPeriodsArrears = "";
    private String cboMinPeriodsArrears = "";
    private String txtPeriodTranSStanAssets = "";
    private String cboPeriodTranSStanAssets = "";
    private String txtProvisionsStanAssets = "";
    private String txtPeriodTransDoubtfulAssets1 = "";
    private String txtPeriodTransDoubtfulAssets2 = "";
    private String txtPeriodTransDoubtfulAssets3 = "";
    private String cboPeriodTransDoubtfulAssets1 = "";
    private String cboPeriodTransDoubtfulAssets2 = "";
    private String cboPeriodTransDoubtfulAssets3 = "";
    private String txtProvisionDoubtfulAssets1 = "";
    private String txtProvisionDoubtfulAssets2 = "";
    private String txtProvisionDoubtfulAssets3 = "";
    private String txtArcCostAccount = "";
    private String txtArcExpenseAccount = "";
    private String txtEaCostAccount = "";
    private String txtEaExpenseAccount = "";
    private String txtEpCostAccount = "";
    private String txtEpExpenseAccount = "";
    private String txtPenalWaiveoffHead = "";
    private String txtPrincipleWaiveoffHead = "";
    private String txtNoticeChargeDebitHead = "";
    private String txtProvisionStandardAssetss = "";
    private String txtProvisionLossAssets = "";
    private String txtPeriodTransLossAssets = "";
    private String cboPeriodTransLossAssets = "";
    private String txtPeriodAfterWhichTransNPerformingAssets = "";
    private String cboPeriodAfterWhichTransNPerformingAssets = "";
    private String cbocalcType = "";
    private String txtMinPeriod = "";
    private String cboMinPeriod = "";
    private String txtMaxPeriod = "";
    private String cboMaxPeriod = "";
    private String txtMinAmtLoan = "";
    private String txtMaxAmtLoan = "";
    private String txtApplicableInter = "";
    private String txtOtherCharges = "";
    private int txtFrequencyInDays = 0;
    private boolean chkSalRec = false;
    //added by rishad for waive
    private String txtEpWaiveoffHead = "";
    private String txtPostageWaiveoffHead = "";
    private String txtArcWaiveoffHead = "";
    private String txtDecreeWaiveoffHead = "";
    private String txtLegalWaiveoffHead = "";
    private String txtInsurenceWaiveoffHead = "";
    private String txtAdvertiseWaiveoffHead = "";
    private String txtMiscellaneousWaiveoffHead = "";
    private String txtArbitraryWaiveoffHead = "";
    private boolean rdoEpWaiverAllowed_Yes=false;
    private boolean rdoEpWaiverAllowed_No = false;
    private boolean rdoPostageWaiverAllowed_Yes = false;
    private boolean rdoPostageWaiverAllowed_No = false;
    private boolean rdoArcWaiverAllowed_Yes = false;
    private boolean rdoArcWaiverAllowed_No = false;
    private boolean rdoDecreeWaiverAllowed_Yes = false;
    private boolean rdoDecreeWaiverAllowed_No = false;
    private boolean rdoLeagalWaiverAllowed_Yes = false;
    private boolean rdoLeagalWaiverAllowed_No = false;
    private boolean rdoInsurenceWaiverAllowed_Yes = false;
    private boolean rdoInsurenceWaiverAllowed_No = false;
    private boolean rdoAdvertiseWaiverAllowed_Yes = false;
    private boolean rdoAdvertiseWaiverAllowed_No = false;
    private boolean rdoMiscellaneousWaiverAllowed_Yes = false;
    private boolean rdoMiscellaneousWaiverAllowed_No = false;
    private boolean rdoArbitraryWaiverAllowed_Yes = false;
    private boolean rdoArbitraryWaiverAllowed_No = false;
    private boolean chkRebtSpl = false;
    private String txtRebateLoanIntPercent = ""; // Added by nithya on 11-01-2020 for KD-1234
    
    private String  chkIsOverdueInt = "N";
    private boolean rdoPenalCalcDays = false;
    private boolean rdoPenalCalcMonths = true;
    private boolean chkEmiPenal = false;
    private boolean rdoOverDueWaiverAllowed_Yes = false;
    private boolean rdoOverDueWaiverAllowed_No = false;
    private String txtOverDueWaiverHead = "";
    private String txtOverdueIntHead = "";
    private String txtRecoveryWaiverHead = "";
    private String txtMeasurementWaiverHead = "";
    private String txtRecoveryChargeHead = "";
    private String txtMeasurementChargeHead = "";
    
    
    private boolean rdoKoleFieldOperationWaiverAllowed_Yes = false;
    private boolean rdoKoleFieldOperationWaiverAllowed_No = false;
    private boolean rdoKoleFieldExpenseWaiverAllowed_Yes = false;
    private boolean rdoKoleFieldExpenseWaiverAllowed_No = false;
    private String txtKoleFieldOperationWaiverHead = "";
    private String txtKoleFieldOperationHead = "";
    private String txtKoleFieldExpenseWaiverHead = "";
    private String txtKoleFieldExpenseHead = "";
    
    
    // Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
    private String chkPrematureIntCalc = "N";

    public String getChkPrematureIntCalc() {
        return chkPrematureIntCalc;
    }

    public void setChkPrematureIntCalc(String chkPrematureIntCalc) {
        this.chkPrematureIntCalc = chkPrematureIntCalc;
    }
    // End 
    
    // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE
    
    String chkIntCalcFromSanctionDt = "N";
    String txtGracePeriodForOverDueInt = "";

    public String getChkIntCalcFromSanctionDt() {
        return chkIntCalcFromSanctionDt;
    }

    public void setChkIntCalcFromSanctionDt(String chkIntCalcFromSanctionDt) {
        this.chkIntCalcFromSanctionDt = chkIntCalcFromSanctionDt;
    }

    public String getTxtGracePeriodForOverDueInt() {
        return txtGracePeriodForOverDueInt;
    }

    public void setTxtGracePeriodForOverDueInt(String txtGracePeriodForOverDueInt) {
        this.txtGracePeriodForOverDueInt = txtGracePeriodForOverDueInt;
    }
    
    // End
    
     // Added by nithya on 15-11-2017 for 0007867: Gold Loan Interest rate needed to set as slab wise [ Product level parameter added ]
    private String chkIsInterestPeriodWise = "";

    public String getChkIsInterestPeriodWise() {
        return chkIsInterestPeriodWise;
    }

    public void setChkIsInterestPeriodWise(String chkIsInterestPeriodWise) {
        this.chkIsInterestPeriodWise = chkIsInterestPeriodWise;
    }
    // End 

    public boolean isChkEmiPenal() {
        return chkEmiPenal;
    }

    public void setChkEmiPenal(boolean chkEmiPenal) {
        this.chkEmiPenal = chkEmiPenal;
    }

    public String getChkIsOverdueInt() {
        return chkIsOverdueInt;
    }

    public void setChkIsOverdueInt(String chkIsOverdueInt) {
        this.chkIsOverdueInt = chkIsOverdueInt;
    }

    public boolean isRdoPenalCalcDays() {
        return rdoPenalCalcDays;
    }

    public void setRdoPenalCalcDays(boolean rdoPenalCalcDays) {
        this.rdoPenalCalcDays = rdoPenalCalcDays;
    }

    public boolean isRdoPenalCalcMonths() {
        return rdoPenalCalcMonths;
    }

    public void setRdoPenalCalcMonths(boolean rdoPenalCalcMonths) {
        this.rdoPenalCalcMonths = rdoPenalCalcMonths;
    }

    public boolean isRdoOverDueWaiverAllowed_No() {
        return rdoOverDueWaiverAllowed_No;
    }

    public void setRdoOverDueWaiverAllowed_No(boolean rdoOverDueWaiverAllowed_No) {
        this.rdoOverDueWaiverAllowed_No = rdoOverDueWaiverAllowed_No;
    }

    public boolean isRdoOverDueWaiverAllowed_Yes() {
        return rdoOverDueWaiverAllowed_Yes;
    }

    public void setRdoOverDueWaiverAllowed_Yes(boolean rdoOverDueWaiverAllowed_Yes) {
        this.rdoOverDueWaiverAllowed_Yes = rdoOverDueWaiverAllowed_Yes;
    }

    public String getTxtOverDueWaiverHead() {
        return txtOverDueWaiverHead;
    }

    public void setTxtOverDueWaiverHead(String txtOverDueWaiverHead) {
        this.txtOverDueWaiverHead = txtOverDueWaiverHead;
    }

    public String getTxtOverdueIntHead() {
        return txtOverdueIntHead;
    }

    public void setTxtOverdueIntHead(String txtOverdueIntHead) {
        this.txtOverdueIntHead = txtOverdueIntHead;
    }
    
    public boolean isChkRebtSpl() {
        return chkRebtSpl;
    }

    public void setChkRebtSpl(boolean chkRebtSpl) {
        this.chkRebtSpl = chkRebtSpl;
    }

    public boolean isRdoAdvertiseWaiverAllowed_No() {
        return rdoAdvertiseWaiverAllowed_No;
    }

    public void setRdoAdvertiseWaiverAllowed_No(boolean rdoAdvertiseWaiverAllowed_No) {
        this.rdoAdvertiseWaiverAllowed_No = rdoAdvertiseWaiverAllowed_No;
    }

    public boolean isRdoAdvertiseWaiverAllowed_Yes() {
        return rdoAdvertiseWaiverAllowed_Yes;
    }

    public void setRdoAdvertiseWaiverAllowed_Yes(boolean rdoAdvertiseWaiverAllowed_Yes) {
        this.rdoAdvertiseWaiverAllowed_Yes = rdoAdvertiseWaiverAllowed_Yes;
    }

    public boolean isRdoArbitraryWaiverAllowed_No() {
        return rdoArbitraryWaiverAllowed_No;
    }

    public void setRdoArbitraryWaiverAllowed_No(boolean rdoArbitraryWaiverAllowed_No) {
        this.rdoArbitraryWaiverAllowed_No = rdoArbitraryWaiverAllowed_No;
    }

    public boolean isRdoArbitraryWaiverAllowed_Yes() {
        return rdoArbitraryWaiverAllowed_Yes;
    }

    public void setRdoArbitraryWaiverAllowed_Yes(boolean rdoArbitraryWaiverAllowed_Yes) {
        this.rdoArbitraryWaiverAllowed_Yes = rdoArbitraryWaiverAllowed_Yes;
    }

    public boolean isRdoArcWaiverAllowed_No() {
        return rdoArcWaiverAllowed_No;
    }

    public void setRdoArcWaiverAllowed_No(boolean rdoArcWaiverAllowed_No) {
        this.rdoArcWaiverAllowed_No = rdoArcWaiverAllowed_No;
    }

    public boolean isRdoArcWaiverAllowed_Yes() {
        return rdoArcWaiverAllowed_Yes;
    }

    public void setRdoArcWaiverAllowed_Yes(boolean rdoArcWaiverAllowed_Yes) {
        this.rdoArcWaiverAllowed_Yes = rdoArcWaiverAllowed_Yes;
    }

    public boolean isRdoDecreeWaiverAllowed_No() {
        return rdoDecreeWaiverAllowed_No;
    }

    public void setRdoDecreeWaiverAllowed_No(boolean rdoDecreeWaiverAllowed_No) {
        this.rdoDecreeWaiverAllowed_No = rdoDecreeWaiverAllowed_No;
    }

    public boolean isRdoDecreeWaiverAllowed_Yes() {
        return rdoDecreeWaiverAllowed_Yes;
    }

    public void setRdoDecreeWaiverAllowed_Yes(boolean rdoDecreeWaiverAllowed_Yes) {
        this.rdoDecreeWaiverAllowed_Yes = rdoDecreeWaiverAllowed_Yes;
    }

    public boolean isRdoEpWaiverAllowed_No() {
        return rdoEpWaiverAllowed_No;
    }

    public void setRdoEpWaiverAllowed_No(boolean rdoEpWaiverAllowed_No) {
        this.rdoEpWaiverAllowed_No = rdoEpWaiverAllowed_No;
    }

    public boolean isRdoEpWaiverAllowed_Yes() {
        return rdoEpWaiverAllowed_Yes;
    }

    public void setRdoEpWaiverAllowed_Yes(boolean rdoEpWaiverAllowed_Yes) {
        this.rdoEpWaiverAllowed_Yes = rdoEpWaiverAllowed_Yes;
    }

    public boolean isRdoInsurenceWaiverAllowed_No() {
        return rdoInsurenceWaiverAllowed_No;
    }

    public void setRdoInsurenceWaiverAllowed_No(boolean rdoInsurenceWaiverAllowed_No) {
        this.rdoInsurenceWaiverAllowed_No = rdoInsurenceWaiverAllowed_No;
    }

    public boolean isRdoInsurenceWaiverAllowed_Yes() {
        return rdoInsurenceWaiverAllowed_Yes;
    }

    public void setRdoInsurenceWaiverAllowed_Yes(boolean rdoInsurenceWaiverAllowed_Yes) {
        this.rdoInsurenceWaiverAllowed_Yes = rdoInsurenceWaiverAllowed_Yes;
    }

    public boolean isRdoLeagalWaiverAllowed_No() {
        return rdoLeagalWaiverAllowed_No;
    }

    public void setRdoLeagalWaiverAllowed_No(boolean rdoLeagalWaiverAllowed_No) {
        this.rdoLeagalWaiverAllowed_No = rdoLeagalWaiverAllowed_No;
    }

    public boolean isRdoLeagalWaiverAllowed_Yes() {
        return rdoLeagalWaiverAllowed_Yes;
    }

    public void setRdoLeagalWaiverAllowed_Yes(boolean rdoLeagalWaiverAllowed_Yes) {
        this.rdoLeagalWaiverAllowed_Yes = rdoLeagalWaiverAllowed_Yes;
    }



    public boolean isRdoMiscellaneousWaiverAllowed_No() {
        return rdoMiscellaneousWaiverAllowed_No;
    }

    public void setRdoMiscellaneousWaiverAllowed_No(boolean rdoMiscellaneousWaiverAllowed_No) {
        this.rdoMiscellaneousWaiverAllowed_No = rdoMiscellaneousWaiverAllowed_No;
    }

    public boolean isRdoMiscellaneousWaiverAllowed_Yes() {
        return rdoMiscellaneousWaiverAllowed_Yes;
    }

    public void setRdoMiscellaneousWaiverAllowed_Yes(boolean rdoMiscellaneousWaiverAllowed_Yes) {
        this.rdoMiscellaneousWaiverAllowed_Yes = rdoMiscellaneousWaiverAllowed_Yes;
    }

    public boolean isRdoPostageWaiverAllowed_No() {
        return rdoPostageWaiverAllowed_No;
    }

    public void setRdoPostageWaiverAllowed_No(boolean rdoPostageWaiverAllowed_No) {
        this.rdoPostageWaiverAllowed_No = rdoPostageWaiverAllowed_No;
    }

    public boolean isRdoPostageWaiverAllowed_Yes() {
        return rdoPostageWaiverAllowed_Yes;
    }

    public void setRdoPostageWaiverAllowed_Yes(boolean rdoPostageWaiverAllowed_Yes) {
        this.rdoPostageWaiverAllowed_Yes = rdoPostageWaiverAllowed_Yes;
    }

    public String getTxtAdvertiseWaiveoffHead() {
        return txtAdvertiseWaiveoffHead;
    }

    public void setTxtAdvertiseWaiveoffHead(String txtAdvertiseWaiveoffHead) {
        this.txtAdvertiseWaiveoffHead = txtAdvertiseWaiveoffHead;
    }

    public String getTxtArbitraryWaiveoffHead() {
        return txtArbitraryWaiveoffHead;
    }

    public void setTxtArbitraryWaiveoffHead(String txtArbitraryWaiveoffHead) {
        this.txtArbitraryWaiveoffHead = txtArbitraryWaiveoffHead;
    }

    public String getTxtArcWaiveoffHead() {
        return txtArcWaiveoffHead;
    }

    public void setTxtArcWaiveoffHead(String txtArcWaiveoffHead) {
        this.txtArcWaiveoffHead = txtArcWaiveoffHead;
    }

    public String getTxtDecreeWaiveoffHead() {
        return txtDecreeWaiveoffHead;
    }

    public void setTxtDecreeWaiveoffHead(String txtDecreeWaiveoffHead) {
        this.txtDecreeWaiveoffHead = txtDecreeWaiveoffHead;
    }

    public String getTxtEpWaiveoffHead() {
        return txtEpWaiveoffHead;
    }

    public void setTxtEpWaiveoffHead(String txtEpWaiveoffHead) {
        this.txtEpWaiveoffHead = txtEpWaiveoffHead;
    }



    public String getTxtInsurenceWaiveoffHead() {
        return txtInsurenceWaiveoffHead;
    }

    public void setTxtInsurenceWaiveoffHead(String txtInsurenceWaiveoffHead) {
        this.txtInsurenceWaiveoffHead = txtInsurenceWaiveoffHead;
    }

    public String getTxtLegalWaiveoffHead() {
        return txtLegalWaiveoffHead;
    }

    public void setTxtLegalWaiveoffHead(String txtLegalWaiveoffHead) {
        this.txtLegalWaiveoffHead = txtLegalWaiveoffHead;
    }

    public String getTxtMiscellaneousWaiveoffHead() {
        return txtMiscellaneousWaiveoffHead;
    }

    public void setTxtMiscellaneousWaiveoffHead(String txtMiscellaneousWaiveoffHead) {
        this.txtMiscellaneousWaiveoffHead = txtMiscellaneousWaiveoffHead;
    }

    public String getTxtPostageWaiveoffHead() {
        return txtPostageWaiveoffHead;
    }

    public void setTxtPostageWaiveoffHead(String txtPostageWaiveoffHead) {
        this.txtPostageWaiveoffHead = txtPostageWaiveoffHead;
    }
    
    public boolean isChkSalRec() {
        return chkSalRec;
    }

    public void setChkSalRec(boolean chkSalRec) {
        this.chkSalRec = chkSalRec;
    }
    
    public int getTxtFrequencyInDays() {
        return txtFrequencyInDays;
    }

    public void setTxtFrequencyInDays(int txtFrequencyInDays) {
        this.txtFrequencyInDays = txtFrequencyInDays;
    }

    public boolean isChkAuctionAllowd() {
        return chkAuctionAllowd;
    }

    public void setChkAuctionAllowd(boolean chkAuctionAllowd) {
        this.chkAuctionAllowd = chkAuctionAllowd;
    }

    public String getTxtOtherCharges() {
        return txtOtherCharges;
    }

    public ComboBoxModel getCbmIntCalcMonth() {
        return cbmIntCalcMonth;
    }

    public void setCbmIntCalcMonth(ComboBoxModel cbmIntCalcMonth) {
        this.cbmIntCalcMonth = cbmIntCalcMonth;
    }

    public void setTxtOtherCharges(String txtOtherCharges) {
        this.txtOtherCharges = txtOtherCharges;
    }
    //    private String txtMaxInter = "";
    private String txtMinInterDebited = "";
//    private boolean rdoSubsidy_Yes = false;
//    private boolean rdoSubsidy_No = false;
    private String cboLoanPeriodMul = "";
    // Fields Required for Documents
    private ComboBoxModel cbmDocumentType;
    private ComboBoxModel cbmNoticeType;
    private ComboBoxModel cbmIssueAfter;
    private ComboBoxModel cbmPeridFreq;
    private ComboBoxModel cbmPrematureIntCalcFreq;
    private ComboBoxModel cbmPrematureIntCalAmt;
    private String txtDocumentNo = "";
    private String txtDocumentDesc = "";
    private String cboDocumentType = "";
    private String documentSerialNo = "";
    private String documentStatus = "";
    private static int notice_Charge_No=1;
    private static int docSI_No = 1;// Seral No for Documents table
    private static int deletedDocSI_No = 1;// Seral No for Deleted Documents
    private static int deleted_NoticeCharge=1;
    private LinkedHashMap documentsTO = null;// Contains Only the Documents which the Status is not DELETED
    private LinkedHashMap deletedDocumentsTO = null;// Contains Only the Documents which the Status is DELETED
    private LinkedHashMap deletedNoticeType=null;
    private LinkedHashMap mainDocumentsTO = null;// Contains Both the Documents
    private LinkedHashMap NoticeTypeTO=null;//contain not deleted records
    private ArrayList entireNoticeTypeRow=null;
    private ArrayList entireRows = null;// All row collections of Documents Table
    private final String STATUS_WITH_DELETE = "STATUS_WITH_DELETE";
    private final String STATUS_WITHOUT_DELETE = "STATUS_WITHOUT_DELETE";
    
    
    private String cboCommodityCode = "";
    private String cboGuaranteeCoverCode = "";
    private String cboSectorCode = "";
    private String cboHealthCode = "";
    private String cboTypeFacility = "";
    private String cboPurposeCode = "";
    private String cboIndusCode = "";
    private String cbo20Code = "";
    private String cboRefinancingInsti = "";
    private String cboGovtSchemeCode = "";
    private String cboSecurityDeails = "";
    private String cboPeriodFreq="";
    private String cboPrematureIntCalcFreq="";
    private String cboPrematureIntCalAmt="";
    private String cboReviewPeriod="";
    private String txtReviewPeriod="";
    private String txtDepAmtLoanMaturingPeriod="";
    private String cboDepAmtLoanMaturingPeriod="";
    private String txtElgLoanAmt="";
    private String txtDepAmtMaturing="";
   
    private boolean rdoMaturity_Yes=false;
    private boolean rdoCurrDt_Yes=false;
    private boolean rdoMaturity_Deposit_closure_Yes;
    private boolean rdoMaturity_Deposit_closure_Upto_Curr_Yes;
    private String  txtGracePeriodPenalInterest="";
    
    private boolean chkDirectFinance = false;
    private boolean chkECGC = false;
    private boolean chkQIS = false;
    
    private boolean chkGldRenewOldAmt=false;
    private boolean chkGldRenewCash=false;
    private boolean chkGldRenewMarketRate=false;
    private boolean chkGldRnwNwIntRate=false;
    private boolean chkInterestOnMaturity =false;

    public boolean isChkGldRnwNwIntRate() {
        return chkGldRnwNwIntRate;
    }

    public boolean isRdoPrincipalWaiverAllowed_No() {
        return rdoPrincipalWaiverAllowed_No;
    }
    public void setRdoPrincipalWaiverAllowed_No(boolean rdoPrincipalWaiverAllowed_No) {
        this.rdoPrincipalWaiverAllowed_No = rdoPrincipalWaiverAllowed_No;
    }

    public boolean isRdoDebitAllowedForCustomer_No() {
        return rdoDebitAllowedForCustomer_No;
    }

    public void setRdoDebitAllowedForCustomer_No(boolean rdoDebitAllowedForCustomer_No) {
        this.rdoDebitAllowedForCustomer_No = rdoDebitAllowedForCustomer_No;
    }

    public boolean isRdoDebitAllowedForCustomer_Yes() {
        return rdoDebitAllowedForCustomer_Yes;
    }

    public void setRdoDebitAllowedForCustomer_Yes(boolean rdoDebitAllowedForCustomer_Yes) {
        this.rdoDebitAllowedForCustomer_Yes = rdoDebitAllowedForCustomer_Yes;
    }
    
    public boolean isRdoPrincipalWaiverAllowed_Yes() {
        return rdoPrincipalWaiverAllowed_Yes;
    }
    public void setRdoPrincipalWaiverAllowed_Yes(boolean rdoPrincipalWaiverAllowed_Yes) {
        this.rdoPrincipalWaiverAllowed_Yes = rdoPrincipalWaiverAllowed_Yes;
    }

    public boolean isRdoNoticeWaiverAllowed_No() {
        return rdoNoticeWaiverAllowed_No;
    }

    public void setRdoNoticeWaiverAllowed_No(boolean rdoNoticeWaiverAllowed_No) {
        this.rdoNoticeWaiverAllowed_No = rdoNoticeWaiverAllowed_No;
    }

    public boolean isRdoNoticeWaiverAllowed_Yes() {
        return rdoNoticeWaiverAllowed_Yes;
    }

    public void setRdoNoticeWaiverAllowed_Yes(boolean rdoNoticeWaiverAllowed_Yes) {
        this.rdoNoticeWaiverAllowed_Yes = rdoNoticeWaiverAllowed_Yes;
    }
    
    public void setChkGldRnwNwIntRate(boolean chkGldRnwNwIntRate) {
        this.chkGldRnwNwIntRate = chkGldRnwNwIntRate;
    }
    public String getChkFixed() {
        return chkFixed;
    }
    public void setChkFixed(String chkFixed) {
        this.chkFixed = chkFixed;
    }
    public double getTxtFixedMargin() {
        return txtFixedMargin;
    }
    public void setTxtFixedMargin(double txtFixedMargin) {
        this.txtFixedMargin = txtFixedMargin;
    }
    public boolean isChkInterestOnMaturity() {
        return chkInterestOnMaturity;
    }

    public void setChkInterestOnMaturity(boolean chkInterestOnMaturity) {
        this.chkInterestOnMaturity = chkInterestOnMaturity;
    }
    
    
    private List loanProductClassificationsTOList=null;
    private List depositList=null;
    private DefaultListModel  lstAvailableDeposits=new DefaultListModel();
    private DefaultListModel  lstSelectedDeposits=new  DefaultListModel();
    private LinkedHashMap existDepositMap=new LinkedHashMap();
    private LinkedHashMap deleteDepositMap=new LinkedHashMap();
    private LinkedHashMap newDepositMap=new LinkedHashMap();
    
    private DefaultListModel  lstAvailableTransaction=new DefaultListModel();
    private DefaultListModel  lstSelectedTransaction=new  DefaultListModel();
    private ArrayList availableTrans=null;
    private LinkedHashMap existTransactionMap=new LinkedHashMap();
    private LinkedHashMap deleteTransactionMap=new LinkedHashMap();
    private LinkedHashMap newTransactionMap=new LinkedHashMap();

    
    private DefaultListModel  lstAvailableTransaction_OTS=new DefaultListModel();
    private DefaultListModel  lstSelectedTransaction_OTS=new  DefaultListModel();
    private ArrayList availableTrans_OTS=null;
    private LinkedHashMap existOTSTransactionMap=new LinkedHashMap();
    private LinkedHashMap deleteOTSTransactionMap = new LinkedHashMap();
    private LinkedHashMap newOTSTransactionMap = new LinkedHashMap();
    private boolean rdoIsInterestFirst_Yes = false;
    private boolean rdoIsInterestFirst_No = false;
    private boolean rdoinsuranceCharge_Yes = false;
    private boolean rdoinsuranceCharge_No = false;
    private boolean rdosanctionDate_Yes = false;
    private boolean rdosanctionDate_No = false;
    private String txtInsuraceRate = "";
    //used for max auctamt
    private boolean rdoAuctionAmt_Yes = false;
    private boolean rdoAuctionAmt_No = false;
    // USED IN setLoanProductChargesTab()
    LoanProductChargesTabTO objLoanProductChargesTabTO;
    private String chkGoldStockPhoto = "N";
    private String chkBlockIFLimitExceed = "N"; // Added by nithya on 16-11-2019 for KD-729
    
    private final static Logger log = Logger.getLogger(NewLoanProductUI.class);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    // Initial Action type is set to cancel...
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    private static NewLoanProductOB loanProductOB;
    private boolean rdoIsInterestDue_Yes = false;
    private boolean rdoIsInterestDue_No = false;
    static {
        try {
            log.info("static LoanProductOB...");
            loanProductOB = new NewLoanProductOB();
            
        } catch(Exception e) {
            log.info("Error in LoanProductOB Declaration");
            parseException.logException(e,true);
        }
    }
    
    /** Creates a new instance of LoanProductOB */
    private NewLoanProductOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        initianSetup();
    }
    
    private void initianSetup() throws Exception{
        setOperationMap();
        try {
            log.info("In LoanProductOB...");
            proxy = ProxyFactory.createProxy();
        } catch (Exception e) {
            log.info("Exception Caught in LoanProductOB Constructor...");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        fillDropdown();// To Fill all the Combo Boxes
        setChargeTabTitle();// To set the Title of Table in Charges Tab...
        tblChargeTab = new EnhancedTableModel(null, chargeTabTitle);
        setDocumentTabTitle();// Sets the Column Names for Documents Table
        
        tblDocuments = new EnhancedTableModel(null, documentsTabTitle);
        setNoticeChargeTab();
        tblNoticeCharge=new EnhancedTableModel(null,noticeChargeTabTitle);
        docSI_No = 1;// Seral No for Documents table
        deletedDocSI_No = 1;// Seral No for Deleted Documents
    }
    
    // To set the Column title in Table...
    private void setChargeTabTitle() throws Exception{
        log.info("In setChargeTabTitle...");
        
        chargeTabTitle.add(objLoanProductRB.getString("tblColumn1"));
        chargeTabTitle.add(objLoanProductRB.getString("tblColumn2"));
        chargeTabTitle.add(objLoanProductRB.getString("tblColumn3"));
    }
    private void setNoticeChargeTab()throws Exception{
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn7"));
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn8"));
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn9"));
        noticeChargeTabTitle.add(objLoanProductRB.getString("tblColumn10"));
        
        
    }
    // To set the Column title in Document Table...
    private void setDocumentTabTitle() throws Exception{
        log.info("In setDocumentTabTitle...");
        documentsTabTitle.add(objLoanProductRB.getString("tblColumn4"));
        documentsTabTitle.add(objLoanProductRB.getString("tblColumn5"));
        documentsTabTitle.add(objLoanProductRB.getString("tblColumn6"));
    }
    
    /**
     * @return returns LoanProductOB object for Singleton implementation
     */
    public static NewLoanProductOB getInstance() {
        log.info("In getInstance...");
        
        return loanProductOB;
    }
    
    // To set the Connections...
    private void setOperationMap() throws Exception{
        log.info("In setOperationMap...");
        
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "LoanProductJNDI");
        operationMap.put(CommonConstants.HOME, "product.loan.LoanProductHome");
        operationMap.put(CommonConstants.REMOTE, "product.loan.LoanProduct");
    }
    
    //To fill The Combo Boxes...
    private void fillDropdown() throws Exception{
        log.info("In fillDropdown...");
        
        lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("LOANPRODUCT.OPERATESLIKE");
        lookup_keys.add("LOANPRODUCT.CATEGORY");
        lookup_keys.add("FREQUENCY");
        lookup_keys.add("OPERATIVEACCTPRODUCT.ROUNDOFF");
        //        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("OPERATIVEACCTPRODUCT.FOLIOCHRG");
        lookup_keys.add("PERIOD");
        
        lookup_keys.add("LOANPRODUCT.CALCTYPE");
        lookup_keys.add("LOANPRODUCT.LOANPERIODS");
        lookup_keys.add("ADVANCESPRODUCT.CHARGETYPE");
        lookup_keys.add("LOANPRODUCT.DOCUMENT_TYPE");
        lookup_keys.add("MDS_HOLIDAY_INST");
        lookup_keys.add("TERM_LOAN.COMMODITY_CODE");
        lookup_keys.add("TERM_LOAN.SECTOR_CODE");
        lookup_keys.add("TERM_LOAN.PURPOSE_CODE");
        lookup_keys.add("TERM_LOAN.INDUSTRY_CODE");
        lookup_keys.add("TERM_LOAN.20_CODE");
        lookup_keys.add("TERM_LOAN.GOVT_SCHEME_CODE");
        lookup_keys.add("TERM_LOAN.GUARANTEE_COVER_CODE");
        lookup_keys.add("TERM_LOAN.HEALTH_CODE");
        lookup_keys.add("TERM_LOAN.REFINANCING_INSTITUTION");
        lookup_keys.add("TERM_LOAN.FACILITY");
        lookup_keys.add("LOANPRODUCT.SECURITY_TYPE");
        lookup_keys.add("TERM_LOAN.NOTICE_TYPE");
        lookup_keys.add("TERM_LOAN.ISSUE_AFTER");
        lookup_keys.add("PERIOD");//TERM_LOAN.REPAYMENT_FREQUENCY
        lookup_keys.add("TERM_LOAN.PREMATURE_CALC_AMT");
        lookup_keys.add("TERMLOAN.TRANSACTION_TYPE");
        lookup_keys.add("TERM_LOAN.INT_REBATE_PERIOD");
        lookup_keys.add("TERM_LOAN.INT_REBATE_CAL");
        lookup_keys.add("TERM_LOAN.REPAYMENT_TYPE");//-- Added by nithya
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        keyValue = com.see.truetransact.clientutil.ClientUtil.populateLookupData(lookUpHash);
        lookUpHash = null;

        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        for (int i = 1; i <= 31; i++) {
            key.add(String.valueOf(i));
            value.add(String.valueOf(i));
            cbmIntCalcDay = new ComboBoxModel(key, value);
            // cbmAuctionDay = new ComboBoxModel(key,value);
        }
        
        for (int j = 1; j <= 12; j++) {
            key.add(String.valueOf(j));
            value.add(String.valueOf(j));
            cbmIntCalcMonth = new ComboBoxModel(key, value);
            // cbmAuctionDay = new ComboBoxModel(key,value);
        }
        makeNull();
        getKeyValue((HashMap) keyValue.get("LOANPRODUCT.OPERATESLIKE"));
        cbmOperatesLike = new ComboBoxModel(key, value);

        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.CATEGORY"));
        cbmProdCategory = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("MDS_HOLIDAY_INST"));
        cbmProdHoliday = new ComboBoxModel(key,value);
       
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.DOCUMENT_TYPE"));
        cbmDocumentType = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("FREQUENCY"));
        cbmDebInterCalcFreq = new ComboBoxModel(key,value);
        cbmDebitInterAppFreq = new ComboBoxModel(key,value);
        cbmProductFreq = new ComboBoxModel(key,value);
        cbmFolioChargesAppFreq = new ComboBoxModel(key,value);
        cbmInterestRepaymentFreq=new ComboBoxModel(key,value);
        // -- Added by nithya
        cbmRepaymentFreq = new ComboBoxModel(key,value);        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REPAYMENT_TYPE"));        
        cbmRepaymentType = new ComboBoxModel(key,value);
        
        
        //-- End
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.ROUNDOFF"));
        cbmDebitProdRoundOff = new ComboBoxModel(key,value);
        cbmDebitInterRoundOff = new ComboBoxModel(key,value);
        cbmDepositRoundOff=new ComboBoxModel(key,value);
        
        
        //        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        //        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        //        cbmProdCurrency = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.FOLIOCHRG"));
        cbmToCollectFolioCharges = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmMinPeriodsArrears = new ComboBoxModel(key,value);
        cbmPeriodTranSStanAssets = new ComboBoxModel(key,value);
        cbmPeriodTransDoubtfulAssets1 = new ComboBoxModel(key,value);
        cbmPeriodTransDoubtfulAssets2 = new ComboBoxModel(key,value);
        cbmPeriodTransDoubtfulAssets3 = new ComboBoxModel(key,value);
        cbmPeriodTransLossAssets = new ComboBoxModel(key,value);
        cbmPeriodAfterWhichTransNPerformingAssets = new ComboBoxModel(key,value);
        cbmMinPeriod = new ComboBoxModel(key,value);
        cbmMaxPeriod = new ComboBoxModel(key,value);
        
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.CALCTYPE"));
        cbmcalcType = new ComboBoxModel(key,value);
        
        //        keyValue = ClientUtil.populateLookupData(lookUpHash);
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.LOANPERIODS"));
        cbmDebitInterComFreq = new ComboBoxModel(key,value);
        cbmIncompleteFolioRoundOffFreq = new ComboBoxModel(key,value);
        cbmLoanPeriodMul = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("ADVANCESPRODUCT.CHARGETYPE"));
        cbmCharge_Limit2 = new ComboBoxModel(key,value);
        cbmCharge_Limit3 = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.COMMODITY_CODE"));
        cbmCommodityCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.SECTOR_CODE"));
        cbmSectorCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.PURPOSE_CODE"));
        cbmPurposeCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.INDUSTRY_CODE"));
        cbmIndusCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.20_CODE"));
        cbm20Code = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GOVT_SCHEME_CODE"));
        cbmGovtSchemeCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.GUARANTEE_COVER_CODE"));
        cbmGuaranteeCoverCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.HEALTH_CODE"));
        cbmHealthCode = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.REFINANCING_INSTITUTION"));
        cbmRefinancingInsti = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.FACILITY"));
        cbmTypeFacility = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.SECURITY_TYPE"));
        cbmSecurityDeails = new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.NOTICE_TYPE"));
        cbmNoticeType=new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.ISSUE_AFTER"));
        cbmIssueAfter=new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmPeridFreq=new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmPrematureIntCalcFreq=new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.PREMATURE_CALC_AMT"));
        cbmPrematureIntCalAmt=new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmReviewPeriod = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmDepAmtMaturingPeriod = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("TERM_LOAN.INT_REBATE_PERIOD"));
        cbmRebatePeriod = new ComboBoxModel(key,value);

        getKeyValue((HashMap)keyValue.get("TERM_LOAN.INT_REBATE_CAL"));
        cbmInterestRebateCalculation = new ComboBoxModel(key,value);
     

        getKeyValue((HashMap) keyValue.get("TERMLOAN.TRANSACTION_TYPE"));
        lstAvailableTransaction = new DefaultListModel();
        lstSelectedTransaction = new DefaultListModel();
        lstAvailableTransaction_OTS = new DefaultListModel();
        lstSelectedTransaction_OTS = new DefaultListModel();
        availableTrans = new ArrayList();
        availableTrans_OTS = new ArrayList();
        for (int i = 1; i < key.size(); i++) {
            //                 arrayList=(ArrayList)lst.get(i);
            lstAvailableTransaction.addElement((Object) key.get(i));
            lstAvailableTransaction_OTS.addElement((Object) key.get(i));
            availableTrans.add((Object) key.get(i));
            availableTrans_OTS.add((Object) key.get(i));
        }

    }
    
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        log.info("In getKeyValue...");
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
     private void makeNull(){
        key=null;
        value=null;
    }
    //------------------------------------------------------------------------------------------------
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData...");
        final HashMap mapData;
        try {
            mapData = proxy.executeQuery(whereMap, operationMap);
            System.out.println("mappppp"+mapData);
            log.info("proxy.executeQuery is working fine");
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("The error in populateData");
            parseException.logException(e,true);
           e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB...");
        
        LoanProductAccountTO objLoanProductAccountTO = null;
        LoanProductGroupLoanTO objLoanProductGroupLoanTO = null;
        LoanProductAccountParamTO objLoanProductAccountParamTO = null;
        LoanProductInterReceivableTO objLoanProductInterReceivableTO = null;
        LoanProductChargesTO objLoanProductChargesTO = null;
        // Fo adding the Data into the Table and
        // adding the table into the Database...
        ArrayList arrayLoanProductChargesTabTO = null;
        LoanProductSpeItemsTO objLoanProductSpeItemsTO = null;
        LoanProductAccHeadTO objLoanProductAccHeadTO = null;
        LoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO = null;
        LoanProductInterCalcTO objLoanProductInterCalcTO = null;
        LoanProductSubsidyInterestRebateTO objLoanProductSubsidyInterestRebateTO=null;
        LoanProductClassificationsTO objLoanProductClassificationsTO = null;
        
        //Taking the Value of Prod_Id from each Table...
        if(mapData.containsKey("LoanProductAccountTO")){
            objLoanProductAccountTO = (LoanProductAccountTO) ((List) mapData.get("LoanProductAccountTO")).get(0);
            setLoanProductAccountTO(objLoanProductAccountTO);
        }
        if(mapData.containsKey("LoanProductGroupLoanTO")){
            objLoanProductGroupLoanTO = (LoanProductGroupLoanTO) ((List) mapData.get("LoanProductGroupLoanTO")).get(0);
            setLoanProductGroupLoanTO(objLoanProductGroupLoanTO);
        }
        if(mapData.containsKey("LoanProductAccountParamTO")){
            objLoanProductAccountParamTO = (LoanProductAccountParamTO) ((List) mapData.get("LoanProductAccountParamTO")).get(0);
            setLoanProductAccountParamTO(objLoanProductAccountParamTO);
        }
        if(mapData.containsKey("LoanProductInterReceivableTO")){
            objLoanProductInterReceivableTO = (LoanProductInterReceivableTO) ((List) mapData.get("LoanProductInterReceivableTO")).get(0);
            setLoanProductInterReceivableTO(objLoanProductInterReceivableTO);
        }
        if(mapData.containsKey("LoanProductChargesTO")){
            objLoanProductChargesTO = (LoanProductChargesTO) ((List) mapData.get("LoanProductChargesTO")).get(0);
            setLoanProductChargesTO(objLoanProductChargesTO);
        }
        if(mapData.containsKey("LoanProductChargesTabTO")){
            arrayLoanProductChargesTabTO =  (ArrayList) (mapData.get("LoanProductChargesTabTO"));
            setLoanProductChargesTabTO(arrayLoanProductChargesTabTO);
        }
        if(mapData.containsKey("LoanProductSpeItemsTO")){
            objLoanProductSpeItemsTO = (LoanProductSpeItemsTO) ((List) mapData.get("LoanProductSpeItemsTO")).get(0);
            setLoanProductSpeItemsTO(objLoanProductSpeItemsTO);
        }
        if(mapData.containsKey("LoanProductAccHeadTO")){
            objLoanProductAccHeadTO = (LoanProductAccHeadTO) ((List) mapData.get("LoanProductAccHeadTO")).get(0);
            setLoanProductAccHeadT(objLoanProductAccHeadTO);
        }
        if(mapData.containsKey("LoanProductNonPerAssetsTO")){
            objLoanProductNonPerAssetsTO = (LoanProductNonPerAssetsTO) ((List) mapData.get("LoanProductNonPerAssetsTO")).get(0);
            setLoanProductNonPerAssetsTO(objLoanProductNonPerAssetsTO);
        }
        if(mapData.containsKey("LoanProductInterCalcTO")){
            objLoanProductInterCalcTO = (LoanProductInterCalcTO) ((List) mapData.get("LoanProductInterCalcTO")).get(0);
            setLoanProductInterCalcTO(objLoanProductInterCalcTO);
        }
        if(mapData.containsKey("LoanProductInterCalcTO")){
            objLoanProductInterCalcTO = (LoanProductInterCalcTO) ((List) mapData.get("LoanProductInterCalcTO")).get(0);
            setDepositProudct((ArrayList)mapData.get("DepositList"));
        }
      
        
        if( mapData.get("LoanProductSubsidyInterestRebateTO") !=null && ((List)mapData.get("LoanProductSubsidyInterestRebateTO")).size()>0){
            objLoanProductSubsidyInterestRebateTO = (LoanProductSubsidyInterestRebateTO) ((List) mapData.get("LoanProductSubsidyInterestRebateTO")).get(0);
            populateLoanProductSubsidyInterestRebateTOInterCalcTO(objLoanProductSubsidyInterestRebateTO);
        }
        //SELECTED DEPOSITlIST
        setSelectedDepositProudct((ArrayList)mapData.get("SELECTED_LIST"));
        
        setLoanTransactionType((ArrayList)mapData.get("AppropriationTransaction"));
        //SELECTED TRANSACTION LIST
        setSelectedTransactionType((ArrayList)mapData.get("SELECTED_TRANS_LIST"));
        setSelectedTransactionType_OTS((ArrayList)mapData.get("SELECTED_TRANS_LIST_OTS"));
        
        System.out.println("#### Before Classifi list in OB : ");
        loanProductClassificationsTOList = (List) mapData.get("LoanProductClassificationsTO");
        //change27-3-07
        if (loanProductClassificationsTOList.size()!=0) {
            objLoanProductClassificationsTO = (LoanProductClassificationsTO) loanProductClassificationsTOList.get(0);
            setLoanProductClassificationsTO(objLoanProductClassificationsTO);
        } else
            loanProductClassificationsTOList = null;
        System.out.println("#### Classifi list in OB : "+objLoanProductClassificationsTO);
        
        List list = (List) mapData.get("LoanProductDocumentsTO");
        
        populateOBDocuments(list);
        setChanged();
        ttNotifyObservers();
    }
    /**
     * To Populate Documents
     */
    private void populateOBDocuments(List list) throws Exception {
        log.info("In populateOBDocuments...");
        System.out.println("###### List : "+ list);
        if (list != null) {
            if (documentsTO == null)
                documentsTO = new LinkedHashMap();
            if (entireRows == null)
                entireRows = new ArrayList();
            for (int i=0,j=list.size();i<j;i++) {
                LoanProductDocumentsTO objLoanProductDocumentsTO = (LoanProductDocumentsTO) list.get(i);
                System.out.println("### objLoanProductDocumentsTO : " + objLoanProductDocumentsTO);
                entireRows.add(setColumnValuesForDocumentsTable(docSI_No, objLoanProductDocumentsTO));
                documentsTO.put(String.valueOf(docSI_No),objLoanProductDocumentsTO);
                docSI_No++;
                objLoanProductDocumentsTO = null;
            }
            tblDocuments.setDataArrayList(entireRows,documentsTabTitle);
        }
        
    }
    
    private void setSelectedDepositProudct(ArrayList selectedList){
        if(existDepositMap==null)
            existDepositMap =new LinkedHashMap();
        if(selectedList !=null && selectedList.size()>0){
            System.out.println("selectedList  ##"+selectedList);
            for(int i=0;i<selectedList.size();i++){
                HashMap singleMap=(HashMap)selectedList.get(i);
                String selectDeposits=CommonUtil.convertObjToStr(singleMap.get("SELECTED_DEPOSITS"));
                lstSelectedDeposits.addElement(selectDeposits);
                existDepositMap.put(selectDeposits, singleMap);
            }
            
            
        }
    }
    
    
private void setSelectedTransactionType(ArrayList selectedList){
        if(existTransactionMap==null)
            existTransactionMap =new LinkedHashMap();
        if(selectedList !=null && selectedList.size()>0){
            System.out.println("selectedList  ##"+selectedList);
            for(int i=0;i<selectedList.size();i++){
                HashMap singleMap=(HashMap)selectedList.get(i);
                String selectDeposits=CommonUtil.convertObjToStr(singleMap.get("1_PRIORITY"));
                lstSelectedTransaction.addElement(selectDeposits);
                  selectDeposits=CommonUtil.convertObjToStr(singleMap.get("2_PRIORITY"));
                lstSelectedTransaction.addElement(selectDeposits); 
                 selectDeposits=CommonUtil.convertObjToStr(singleMap.get("3_PRIORITY"));
                lstSelectedTransaction.addElement(selectDeposits);
                  selectDeposits=CommonUtil.convertObjToStr(singleMap.get("4_PRIORITY"));
                lstSelectedTransaction.addElement(selectDeposits);
                lstAvailableTransaction=new DefaultListModel();
                existTransactionMap.put(selectDeposits, singleMap);
            }
            
            
        }
    }    
    
    private void setSelectedTransactionType_OTS(ArrayList selectedList){
        if(existOTSTransactionMap==null)
            existOTSTransactionMap =new LinkedHashMap();
        if(selectedList !=null && selectedList.size()>0){
            System.out.println("selectedList  ##"+selectedList);
            for(int i=0;i<selectedList.size();i++){
                HashMap singleMap=(HashMap)selectedList.get(i);
                String selectDeposits=CommonUtil.convertObjToStr(singleMap.get("1_PRIORITY"));
                lstSelectedTransaction_OTS.addElement(selectDeposits);
                selectDeposits=CommonUtil.convertObjToStr(singleMap.get("2_PRIORITY"));
                lstSelectedTransaction_OTS.addElement(selectDeposits);
                selectDeposits=CommonUtil.convertObjToStr(singleMap.get("3_PRIORITY"));
                lstSelectedTransaction_OTS.addElement(selectDeposits);
                selectDeposits=CommonUtil.convertObjToStr(singleMap.get("4_PRIORITY"));
                lstSelectedTransaction_OTS.addElement(selectDeposits);
                lstAvailableTransaction_OTS=new DefaultListModel();
                existOTSTransactionMap.put(selectDeposits, singleMap);
            }
            
            
        }
    }
    /** populate deposit details
     */
    private void setDepositProudct(ArrayList arrayList)throws Exception{
        ArrayList singlearrayList=null;
        if(arrayList !=null && arrayList .size()>0){
            lstAvailableDeposits=new DefaultListModel();
            for(int i=0;i<arrayList.size();i++){
                //                 arrayList=(ArrayList)lst.get(i);
                lstAvailableDeposits.addElement(((HashMap)arrayList.get(i)).get("PROD_DESC"));
            }
            
        }
        //        System.out.println("depositList   "+lst);
        //        depositList=lst;
    }
    
    
    /** populate available Transaction details
     */
    private void setLoanTransactionType(ArrayList arrayList)throws Exception{
        ArrayList singlearrayList=null;
        if(arrayList !=null && arrayList .size()>0){
            lstAvailableTransaction=new DefaultListModel();
            for(int i=0;i<arrayList.size();i++){
                //                 arrayList=(ArrayList)lst.get(i);
                lstAvailableTransaction.addElement(((HashMap)arrayList.get(i)).get("LOOKUP_REF_ID"));
            }
            
        }
        //        System.out.println("depositList   "+lst);
        //        depositList=lst;
    }
    
    
    public void setPriorityTransDetails(){
        lstAvailableTransaction=new DefaultListModel();
         for(int i=0;i<availableTrans.size();i++)
            lstAvailableTransaction.addElement(availableTrans.get(i));
    }
    
     public void setPriorityTransDetails_OTS(){
        lstAvailableTransaction_OTS=new DefaultListModel();
        for(int i=0;i<availableTrans_OTS.size();i++)
            lstAvailableTransaction_OTS.addElement(availableTrans_OTS.get(i));
    }
    //==========ACCOUNT==========
    private void setLoanProductAccountTO(LoanProductAccountTO objLoanProductAccountTO) throws Exception{
        log.info("In setLoanProductAccountTO...");
        setChkExcludeScSt(CommonUtil.convertObjToStr(objLoanProductAccountTO.getExcludeScSt()));
        setChkExcludeTOD(CommonUtil.convertObjToStr(objLoanProductAccountTO.getExcludeTOD()));
        setChkShareLink(CommonUtil.convertObjToStr(objLoanProductAccountTO.getShareLink()));
        setStatusBy(objLoanProductAccountTO.getStatusBy());
        setTxtProductID(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdId()));
        setTxtProductDesc(CommonUtil.convertObjToStr(objLoanProductAccountTO.getProdDesc()));
        setCboOperatesLike((String) getCbmOperatesLike().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBehavesLike())));
        getCbmProdCategory().setKeyForSelected(objLoanProductAccountTO.getAuthorizeRemark());
        //        setCboProdCurrency((String) getCbmProdCurrency().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getBaseCurrency())));
        setTxtAccHead(CommonUtil.convertObjToStr(objLoanProductAccountTO.getAcctHead()));
        // Added by nithya 
        setCboRepaymentType((String)getCbmRepaymentType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getRepaymentType())));
        setCboRepaymentFreq((String)getCbmRepaymentFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductAccountTO.getRepaymentFreq())));
       // End
        setChkGoldStockPhoto(objLoanProductAccountTO.getGoldStockPhotoStored()); // Added by nithya on 29-10-2019 for KD-763 Need Gold ornaments photo saving option
        setChkBlockIFLimitExceed(objLoanProductAccountTO.getBlockIntPostIfLimitExceeds()); // Added by nithya on 16-11-2019 for KD-729
    }
     private void setLoanProductGroupLoanTO(LoanProductGroupLoanTO objLoanProductGroupLoanTO) throws Exception{
        log.info("In setLoanProductGroupProductTO...");
       
        setTxtProductID(CommonUtil.convertObjToStr(objLoanProductGroupLoanTO.getProdId()));
        setCboIntCalcDay((String) getCbmIntCalcDay().getDataForKey(CommonUtil.convertObjToStr(objLoanProductGroupLoanTO.getIntCalcDay())));
        setCboIntCalcMonth((String) getCbmIntCalcMonth().getDataForKey(CommonUtil.convertObjToStr(objLoanProductGroupLoanTO.getIntCalcMonth())));
                setCboIfHoliday(objLoanProductGroupLoanTO.getInterestCalcDay());
       // setTxtDebitAllowed(CommonUtil.convertObjToStr(objLoanProductGroupLoanTO.getIsDebitAllowedForDueCustomer()));
        //setCboIfHoliday((String) getCbmProdHoliday().getDataForKey(CommonUtil.convertObjToStr(objLoanProductGroupLoanTO.getInterestCalcDay())));
        System.out.println("cccc-----"+(String) getCbmProdHoliday().getDataForKey(CommonUtil.convertObjToStr(objLoanProductGroupLoanTO.getInterestCalcDay())));
         if (CommonUtil.convertObjToStr(objLoanProductGroupLoanTO.getIsDebitAllowedForDueCustomer()).equals(YES)) {
            setRdoDebitAllowedForCustomer_Yes(true);
        } else {
            setRdoDebitAllowedForCustomer_No(true);
        }     
    }
    
    private void setLoanProductAccountParamTO(LoanProductAccountParamTO objLoanProductAccountParamTO) throws Exception{
        log.info("In setLoanProductAccountParamTO...");
        setTxtSuspenseCreditAchd(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getSuspenseCreditAchd()));
        setTxtSuspenseDebitAchd(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getSuspenseDebitAchd()));
        setTxtNumPatternFollowed(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getNumberPattern()));
        setTxtMaxCashDisb(CommonUtil.convertObjToDouble(objLoanProductAccountParamTO.getMaxCashPayment()));
        setTxtNumPatternFollowedSuffix(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getNumberPatternSuffix()));
        setTxtLastAccNum(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getLastAcNo()));
        if(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getIsInterestLoanDue()).equals(YES)){
            setRdoIsInterestDue_Yes(true);
        }else{
            setRdoIsInterestDue_No(true);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getLimitDefAllowed()).equals(YES)) setRdoIsLimitDefnAllowed_Yes(true);
        else setRdoIsLimitDefnAllowed_No(true);
        
        if(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getIsInterestFirst()).equals(YES)){
            setRdoIsInterestFirst_Yes(true);
            if(objLoanProductAccountParamTO.getFreqInDays()!=0){
            	setTxtFrequencyInDays(objLoanProductAccountParamTO.getFreqInDays());
            }
        }else{
            setRdoIsInterestFirst_No(true);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getIsCreditAllowedToPricipal()).equals(YES)) {
            setRdoIsCreditAllowed_Yes(true);
        } else {
            setRdoIsCreditAllowed_No(true);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getAuctionAmt()).equals(YES)) {
            setRdoAuctionAmt_Yes(true);
        } else {
            setRdoAuctionAmt_No(true);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getIsDisbAftMoraPerd()).equals(YES)) {
            setRdoDisbAftMoraPerd_Yes(true);
        } else {
            setRdoDisbAftMoraPerd_No(true);
        }
                
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getStaffAcOpened()).equals(YES)) setRdoIsStaffAccOpened_Yes(true);
        else setRdoIsStaffAccOpened_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getDebitIntClearingappl()).equals(YES)) setRdoIsDebitInterUnderClearing_Yes(true);
        else setRdoIsDebitInterUnderClearing_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getGldRenewOldAmt()).equals(YES)) setChkGldRenewOldAmt(true);
        else setChkGldRenewOldAmt(false);
         if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getGldRenewCash()).equals(YES)) setChkGldRenewCash(true);
        else setChkGldRenewCash(false);
         if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getGldRenewMarketRate()).equals(YES)) setChkGldRenewMarketRate(true);
        else setChkGldRenewMarketRate(false);
         if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getGldRnwNwIntRate()).equals(YES))
             setChkGldRnwNwIntRate(true);
        else setChkGldRnwNwIntRate(false);
        if (CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getInterestOnMaturity()).equals(YES))
             setChkInterestOnMaturity(true);
        else setChkInterestOnMaturity(false);
        if(CommonUtil.convertObjToStr(objLoanProductAccountParamTO.getChkAuctionAllowed()).equals(YES)) setChkAuctionAllowd(true);
           
        else setChkAuctionAllowd(false);

        if (objLoanProductAccountParamTO.getIsSalaryRecovery() != null && !objLoanProductAccountParamTO.getIsSalaryRecovery().equals("")
                && objLoanProductAccountParamTO.getIsSalaryRecovery().equals("Y")) {
            setChkSalRec(true);
        } else {
            setChkSalRec(false);
        }        
        if(objLoanProductAccountParamTO.getIsIntCalcPeriodWise().equalsIgnoreCase("Y")){// Added by nithya on 15-11-2017 for 7867
            setChkIsInterestPeriodWise("Y");
        }else{
            setChkIsInterestPeriodWise("N");
        }
        if(objLoanProductAccountParamTO.getChkEMISimpleInt().equalsIgnoreCase("Y")){ //Added by nithya on 16-04-2020 for KD-380 
           setChkEMIInSimpleIntrst("Y"); 
        }else{
           setChkEMIInSimpleIntrst("N");  
        }
        
      }
    
    //==========INTEREST RECEIVABLE==========
    private void setLoanProductInterReceivableTO(LoanProductInterReceivableTO objLoanProductInterReceivableTO) throws Exception{
        log.info("In setLoanProductInterReceivableTO...");
        
        if (objLoanProductInterReceivableTO.getDebitIntCharged().equals(YES)) setRdoldebitInterCharged_Yes(true);
        else setRdoldebitInterCharged_No(true);
        
        setTxtMinDebitInterRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMinDebitintRate()));
        setTxtMaxDebitInterRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMaxDebitintRate()));
        setTxtMinDebitInterAmt(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMinDebitintAmt()));
        setTxtMaxDebitInterAmt(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getMaxDebitintAmt()));
        setCboDebInterCalcFreq((String) getCbmDebInterCalcFreq().getDataForKey( CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitintCalcFreq())));
        setCboDebitInterAppFreq((String) getCbmDebitInterAppFreq().getDataForKey( CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitintApplFreq())));
        setCboDebitInterComFreq((String) getCbmDebitInterComFreq().getDataForKey( CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitintCompFreq())));
        setCboDebitProdRoundOff((String) getCbmDebitProdRoundOff().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitProdRoundoff())));
        setCboDebitInterRoundOff((String) getCbmDebitInterRoundOff().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getDebitIntRoundoff())));
        getCbmInterestRepaymentFreq().setKeyForSelected(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInterestRepaymentFreq()));
        setTdtLastInterCalcDate(DateUtil.getStringDate(objLoanProductInterReceivableTO.getLastIntcalcDtdebit()));
        setTdtLastInterAppDate(DateUtil.getStringDate(objLoanProductInterReceivableTO.getLastIntapplDtdebit()));
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrRateAppl()).equals(YES)) setRdoPLRRateAppl_Yes(true);
        else setRdoPLRRateAppl_No(true);
        
        setTxtPLRRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrRate()));
        setTdtPLRAppForm(DateUtil.getStringDate(objLoanProductInterReceivableTO.getPlrApplFrom()));
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrApplNewac()).equals(YES)) setRdoPLRApplNewAcc_Yes(true);
        else setRdoPLRApplNewAcc_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPlrApplExistac()).equals(YES)) setRdoPLRApplExistingAcc_Yes(true);
        else setRdoPLRApplExistingAcc_No(true);
        
        setTdtPlrApplAccSancForm(DateUtil.getStringDate(objLoanProductInterReceivableTO.getPlrApplSancfrom()));
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPenalAppl()).equals(YES)) setRdoPenalAppl_Yes(true);
        else setRdoPenalAppl_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getLimitExpiryInt()).equals(YES)) setRdoLimitExpiryInter_Yes(true);
        else setRdoLimitExpiryInter_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getAsAndWhenCustomer_Yes()).equals(YES)) setRdoasAndWhenCustomer_Yes(true);
        else setRdoasAndWhenCustomer_No(true);
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getCalendarFrequency_Yes()).equals(YES)) setRdocalendarFrequency_Yes(true);
        else setRdocalendarFrequency_No(true);
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getBillByBill()).equals(YES)) setRdobill_Yes(true);
        else setRdobill_NO(true);
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPrincipalDue()).equals(YES)) setChkprincipalDue(true);
        else setChkprincipalDue(false);
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInterestDue()).equals(YES)) setChkInterestDue(true);
        else setChkInterestDue(false);
        
        //Added by nithya on 23-03-2018 for 0008470: Property Mortgage Loan Ledger modification as per attachment
        if(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getEmiPenal()).equals(YES)){
            setChkEmiPenal(true);
        }else{
            setChkEmiPenal(false);
        }
        if(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getEmiPenalCalcMethod()).equalsIgnoreCase("DAYS")){
            setRdoPenalCalcDays(true);
        }else{
            setRdoPenalCalcDays(false);
        }
        if(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getEmiPenalCalcMethod()).equalsIgnoreCase("MONTHS")){
            setRdoPenalCalcMonths(true);
        }else{
            setRdoPenalCalcMonths(false);
        }
        
        if(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getIsOverDueIntTaken()).equalsIgnoreCase("Y")){
            setChkIsOverdueInt("Y");
        }else{
            setChkIsOverdueInt("N");
        }
        
        // End
        
        
         if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getUptoMaturity()).equals(YES)) {
             setRdoMaturity_Yes(true);
         }
        else  if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getUptoMaturity()).equals(NO)) {
             setRdoCurrDt_Yes(true);
        }
        
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInterestDueKeptReceivable()).equals(YES)) {
            setRdoInterestDueKeptReceivable_Yes(true);
        }
        else  if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInterestDueKeptReceivable()).equals(NO)) {
            setRdoInterestDueKeptReceivable_No(true);
        }
        
         if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getUptoDepositMaturity()).equals(YES)) {
             setRdoMaturity_Deposit_closure_Yes(true);
         }
        else  if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getUptoDepositMaturity()).equals(NO)) {
             setRdoMaturity_Deposit_closure_Upto_Curr_Yes(true);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInsuranceApplicable()).equals(YES)) {
             setRdoinsuranceCharge_Yes(true);
         }
        else  if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInsuranceApplicable()).equals(NO)) {
             setRdoinsuranceCharge_No(true);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getUptoDepositMaturity()).equals(YES)) {
             setRdoinsuranceCharge_Yes(true);
         }
        else  if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getUptoDepositMaturity()).equals(NO)) {
             setRdoinsuranceCharge_No(true);
        }
        setTxtInsuraceRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getInsuranceRate()));
        setTxtGracePeriodPenalInterest(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getGracePeriodPenal()));
        setTxtPenalInterRate(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPenalIntRate()));
        setTxtExposureLimit_Prud(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPrudentialamt()));
        setTxtExposureLimit_Prud2(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPrudentialper()));
        setTxtExposureLimit_Policy(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPolicyamt()));
        setTxtExposureLimit_Policy2(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getExpoLmtPolicyper()));
        setCboProductFreq((String) getCbmProductFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getProdFreq())));
//        setCboPeriodFreq((String) getCbmPeridFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPeriodFreq())));
        setCboPrematureIntCalAmt((String) getCbmPrematureIntCalAmt().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPrematureIntCalAmt())));
        resultValue =  CommonUtil.convertObjToInt(objLoanProductInterReceivableTO.getPeriodFreq());
        String period = setPeriod(resultValue);
//        setCboPeriodFreq(period);
//        setTxtPreMatIntCalctPeriod(String.valueOf(resultValue));
//        added by nikhil
        setTxtPreMatIntCalctPeriod(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPeriodFreq()));
        setTxtPreMatIntCollectPeriod(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPeriodIntCalcFreq()));
        setCboPeriodFreq((String) getCbmPeridFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPrematurePeriod()))); 
        setCboPrematureIntCalcFreq((String) getCbmPrematureIntCalcFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPrematureIntCalcPeriod())));
        resetPeriod();
        
        // Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
        if(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getPrematureClosureIntCalcRequired()).equalsIgnoreCase("Y")){
            setChkPrematureIntCalc("Y");
        }else{
            setChkPrematureIntCalc("N");
        }
        
        // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE        
        if (CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getIntCalcFromSanctionDt()).equalsIgnoreCase("Y")) {
            setChkIntCalcFromSanctionDt("Y");
        } else {
            setChkIntCalcFromSanctionDt("N");
        }
        setTxtGracePeriodForOverDueInt(CommonUtil.convertObjToStr(objLoanProductInterReceivableTO.getGracePeriodForOverDueInt()));
        // End
        
    }
    
    //==========CHARGES==========
    private void setLoanProductChargesTO(LoanProductChargesTO objLoanProductChargesTO) throws Exception{
        log.info("In setLoanProductChargesTO...");
        
        setTxtAccClosingCharges(CommonUtil.convertObjToStr(objLoanProductChargesTO.getAcClosingChrg()));
        setTxtMiscSerCharges(CommonUtil.convertObjToStr(objLoanProductChargesTO.getMiscServChrg()));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getStatChrg()).equals(YES)) setRdoStatCharges_Yes(true);
        else setRdoStatCharges_No(true);
        
        setTxtStatChargesRate(CommonUtil.convertObjToStr(objLoanProductChargesTO.getStatChrgRate()));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getFolioChrgAppl()).equals(YES)) setRdoFolioChargesAppl_Yes(true);
        else setRdoFolioChargesAppl_No(true);
        
        setTdtLastFolioChargesAppl(CommonUtil.convertObjToStr(DateUtil.getStringDate(objLoanProductChargesTO.getLastFolioChrgon())));
        setTxtNoEntriesPerFolio(CommonUtil.convertObjToStr(objLoanProductChargesTO.getNoEntriesPerFolio()));
        setTdtNextFolioDDate(DateUtil.getStringDate(objLoanProductChargesTO.getNextFolioDuedate()));
        setTxtRatePerFolio(CommonUtil.convertObjToStr(objLoanProductChargesTO.getRatePerFolio()));
        setCboFolioChargesAppFreq((String) getCbmFolioChargesAppFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTO.getFolioChrgApplfreq())));
        setCboToCollectFolioCharges((String) getCbmToCollectFolioCharges().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg())));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectChrgOn()).equals(BOTH)) setRdoToChargeOn_Both(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectChrgOn()).equals(MANUAL)) setRdoToChargeOn_Man(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectChrgOn()).equals(SYSTEM)) setRdoToChargeOn_Sys(true) ;
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg()).equals("CREDIT")) setRdoToChargeType_Credit(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg()).equals("DEBIT")) setRdoToChargeType_Debit(true);
        else if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getToCollectFoliochrg()).equals("BOTH")) setRdoToChargeType_Both(true) ;
        
        setCboIncompleteFolioRoundOffFreq((String) getCbmIncompleteFolioRoundOffFreq().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTO.getIncompFolioRoundoff())));
        
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrg()).equals(YES)) setRdoProcessCharges_Yes(true);
        else setRdoProcessCharges_No(true);
        
        //        setTxtcharge_Limit1(CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrgPer()));
        
        if(CommonUtil.convertObjToInt(objLoanProductChargesTO.getProcChrgPer())!=0){
            setTxtCharge_Limit2(CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrgPer()));
            setCboCharge_Limit2(PERCENT);
        }else if(CommonUtil.convertObjToInt(objLoanProductChargesTO.getProcChrgAmt())!= 0){
            setTxtCharge_Limit2(CommonUtil.convertObjToStr(objLoanProductChargesTO.getProcChrgAmt()));
            setCboCharge_Limit2(ABSOLUTE);
        }
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getCreditStampAdvance()).equals(YES)) setChkCreditStampAdvance(true);
        else setChkCreditStampAdvance(false);
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getCreditNoticeAdvance()).equals(YES)) setChkCreditNoticeAdvance(true);
        else setChkCreditNoticeAdvance(false);
        if (CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrg()).equals(YES)) setRdoCommitmentCharge_Yes(true);
        else setRdoCommitmentCharge_No(true);
        
        //        setTxtCharge_Limit3(CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrgPer()));
        if(CommonUtil.convertObjToDouble(objLoanProductChargesTO.getCommitChrgPer()).doubleValue()!=0){
            setTxtCharge_Limit3(CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrgPer()));
            setCboCharge_Limit3(PERCENT);
        }else if(CommonUtil.convertObjToDouble(objLoanProductChargesTO.getCommitChrgAmt()).doubleValue()!=0){
            setTxtCharge_Limit3(CommonUtil.convertObjToStr(objLoanProductChargesTO.getCommitChrgAmt()));
            setCboCharge_Limit3(ABSOLUTE);
        }
    }
    
    private void setLoanProductChargesTabTO(ArrayList arrayLoanProductChargesTabTO) throws Exception{
        log.info("In setLoanProductChargesTabTO...");
        System.out.println("setLoanproductcharge###"+arrayLoanProductChargesTabTO);
        LoanProductChargesTabTO objLoanProductChargesTabTO= new LoanProductChargesTabTO();
        NoticeTypeTO=new LinkedHashMap();
        entireNoticeTypeRow=new ArrayList();
        ArrayList totNoticeList=new ArrayList();
        if(entireNoticeTypeRow ==null)
            entireNoticeTypeRow=new ArrayList();
        for (int i=0, j= arrayLoanProductChargesTabTO.size();i<j;i++){
            chargeTabRow = new ArrayList();
            
            objLoanProductChargesTabTO = (LoanProductChargesTabTO)arrayLoanProductChargesTabTO.get(i);
            NoticeTypeTO.put(String.valueOf(notice_Charge_No),objLoanProductChargesTabTO);
            entireNoticeTypeRow.add(setColumnValueNoticeCharge(notice_Charge_No, objLoanProductChargesTabTO));
            notice_Charge_No++;
            objLoanProductChargesTabTO = (LoanProductChargesTabTO)arrayLoanProductChargesTabTO.get(i);
            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeType()));
            chargeTabRow.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getIssueAfter()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getNoticeChargeAmt()));
            chargeTabRow.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getPostageAmt()));
            totNoticeList.add(chargeTabRow);
//            tblNoticeCharge.insertRow(i,chargeTabRow);
            //            tblNoticeCharge.setDataArrayList(chargeTabRow,noticeChargeTabTitle);
        }
        entireNoticeTypeRow=totNoticeList;
        tblNoticeCharge.setDataArrayList(totNoticeList,noticeChargeTabTitle);
    }
    
    //==========SPECIAL ITEMS==========
    private void setLoanProductSpeItemsTO(LoanProductSpeItemsTO objLoanProductSpeItemsTO) throws Exception{
        log.info("In setLoanProductSpeItemsTO...");
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getAtmCardIssued()).equals(YES)) setRdoATMcardIssued_Yes(true);
        else setRdoATMcardIssued_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getCrCardIssued()).equals(YES)) setRdoCreditCardIssued_Yes(true);
        else setRdoCreditCardIssued_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getMobileBankClient()).equals(YES)) setRdoMobileBanlingClient_Yes(true);
        else setRdoMobileBanlingClient_No(true);
        
        if (CommonUtil.convertObjToStr(objLoanProductSpeItemsTO.getBranchBankingAllowed()).equals(YES)) setRdoIsAnyBranBankingAllowed_Yes(true);
        else setRdoIsAnyBranBankingAllowed_No(true);
    }
    
    //==========ACCOUNT HEAD==========
    private void setLoanProductAccHeadT(LoanProductAccHeadTO objLoanProductAccHeadTO) throws Exception{
        log.info("In setLoanProductAccHeadTO...");
        
        setTxtAccClosCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAcClosingChrg()));
        setTxtMiscServCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getMiscServChrg()));
        setTxtStatementCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getStatChrg()));
        setTxtAccDebitInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAcDebitInt()));
        setTxtPenalInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getPenalInt()));
        setTxtAccCreditInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAcCreditInt()));
        setTxtExpiryInter(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getExpiryInt()));
        setTxtCheReturnChargest_Out(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getChqRetChrgOutward()));
        setTxtCheReturnChargest_In(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getChqRetChrgInward()));
        setTxtFolioChargesAcc(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getFolioChrgAc()));
        setTxtCommitCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getCommitmentChrg()));
        setTxtProcessingCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getProcChrg()));
        setTxtNoticeCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getNoticeCharges()));
        setTxtPostageCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getPostageCharges()));
        setTxtIntPayableAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getIntPayableAcHd()));
        setTxtLegalCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getLegalCharges()));
        //        setTxtMiscCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getMiscCharges()));
        setTxtArbitraryCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getArbitraryCharges()));
        setTxtInsuranceCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getInsuranceCharges()));
        setTxtExecutionDecreeCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getExecutionDecreeCharges()));
        setTxtArcCostAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getArcCost()));
        setTxtArcExpenseAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getArcExpense()));
        setTxtEaCostAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getEaCost()));
        setTxtEaExpenseAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getEaExpense()));
        setTxtEpCostAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getEpCost()));
        setTxtEpExpenseAccount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getEpExpense()));
        setTxtDebitIntDiscount(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getDebitIntDiscountAchd()));
        setTxtRebateInterest(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getRebateInterest()));
        setTxtStampAdvancesHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getStampAdvancesHead()));
        setTxtNoticeAdvancesHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getNoticeAdvancesHead()));
        setTxtAdvertisementHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAdvertisementHead()));
        setTxtARCEPSuspenceHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getArcEpSuspenceHead()));
        setTxtOtherCharges(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getOthrChrgsHead()));
        //added by rishad  24-04-2015
        setTxtArcWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getArcWaiver()));
        setTxtArbitraryWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getArbitraryWaiver()));
        setTxtAdvertiseWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getAdvertiseWaiver()));
        setTxtPostageWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getPostageWaiver()));
        setTxtLegalWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getLegalWaiver()));
        setTxtInsurenceWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getInsurenceWaiver()));
        setTxtEpWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getEpWaiver()));
        setTxtDecreeWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getDecreeWaiver()));
        setTxtMiscellaneousWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getMiscellaneousWaiver()));
       setTxtPrincipleWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getPrincipleWaveOff()));
       setTxtNoticeChargeDebitHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getNoticeWaiveOff()));
      setTxtPenalWaiveoffHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getPenalWaiveOff()));
      
      setTxtOverdueIntHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getOverDueIntHead()));
      setTxtOverDueWaiverHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getOverDueWaiver()));
      setTxtRecoveryWaiverHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getRecoveryWaiver())); 
      setTxtMeasurementWaiverHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getMeasurementWaiver()));
      setTxtRecoveryChargeHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getRecoveryCharges()));
      setTxtMeasurementChargeHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getMeasurementCharges()));
        
      setTxtKoleFieldExpenseHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getKoleFieldexpense()));
      setTxtKoleFieldExpenseWaiverHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getKoleFieldExpenseWaiver()));
      setTxtKoleFieldOperationHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getKoleFieldOperation()));
      setTxtKoleFieldOperationWaiverHead(CommonUtil.convertObjToStr(objLoanProductAccHeadTO.getKoleFieldOperationWaiver()));
    }
    
    //==========NON PERFORMING ASSET==========
    private void setLoanProductNonPerAssetsTO(LoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO) throws Exception {
        log.info("In setLoanProductNonPerAssetsTO...");
        
        
        setTxtProvisionStandardAssetss(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionStdAssets()));
        
        resultValue =  CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getMinPeriodArrears());
        String period = setPeriod(resultValue);
        setCboMinPeriodsArrears(period);
        setTxtMinPeriodsArrears(String.valueOf(resultValue));
        resetPeriod();
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransSubstandard());
        period = setPeriod(resultValue);
        setCboPeriodTranSStanAssets(period);
        setTxtPeriodTranSStanAssets(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionsStanAssets(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionSubstandard()));
        //        if (objLoanProductNonPerAssetsTO.getProvisionSubstandard().equals(YES)) {
        //            setChkProvisionsStanAssets(true);
        //        }else {
        //            setChkProvisionsStanAssets(false);
        //        }
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransDoubtful());
        period = setPeriod(resultValue);
        setCboPeriodTransDoubtfulAssets1(period);
        setTxtPeriodTransDoubtfulAssets1(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionDoubtfulAssets1(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionDoubtful()));
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransDoubtful2());
        period = setPeriod(resultValue);
        setCboPeriodTransDoubtfulAssets2(period);
        setTxtPeriodTransDoubtfulAssets2(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionDoubtfulAssets2(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionDoubtful2()));
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransDoubtful3());
        period = setPeriod(resultValue);
        setCboPeriodTransDoubtfulAssets3(period);
        setTxtPeriodTransDoubtfulAssets3(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionDoubtfulAssets3(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionDoubtful3()));
        
        //        if (objLoanProductNonPerAssetsTO.getProvisionDoubtful().equals(YES)) {
        //            setChkProvisionDoubtfulAssets(true);
        //        }else {
        //            setChkProvisionDoubtfulAssets(false);
        //        }
        
        
        resultValue= CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransLoss());
        period = setPeriod(resultValue);
        setCboPeriodTransLossAssets(period);
        setTxtPeriodTransLossAssets(String.valueOf(resultValue));
        resetPeriod();
        
        setTxtProvisionLossAssets(CommonUtil.convertObjToStr(objLoanProductNonPerAssetsTO.getProvisionLoseAssets()));
        
        resultValue= CommonUtil.convertObjToInt(objLoanProductNonPerAssetsTO.getPeriodTransNoperforming());
        period = setPeriod(resultValue);
        setCboPeriodAfterWhichTransNPerformingAssets(period);
        setTxtPeriodAfterWhichTransNPerformingAssets(String.valueOf(resultValue));
        resetPeriod();
    }
    
    //==========INTEREST CALCULATION==========
    private void setLoanProductInterCalcTO(LoanProductInterCalcTO objLoanProductInterCalcTO) throws Exception{
        log.info("In setLoanProductInterCalcTO...");
        
        setCbocalcType((String) getCbmcalcType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getCalcType())));
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductInterCalcTO.getMinPeriod());
        String period = setPeriod(resultValue);
        setCboMinPeriod(period);
        setTxtMinPeriod(String.valueOf(resultValue));
        resetPeriod();
        
        resultValue = CommonUtil.convertObjToInt(objLoanProductInterCalcTO.getMaxPeriod());
        String periodChar =CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMaxPeriodChar());
        if(periodChar.equals("DAYS")){
            setCboMaxPeriod(DAY1);
            setTxtMaxPeriod(String.valueOf(resultValue));
        }else{
        period = setPeriod(resultValue);
        setCboMaxPeriod(period);
        setTxtMaxPeriod(String.valueOf(resultValue));
        resetPeriod();
        }
        //review period
        resultValue = CommonUtil.convertObjToInt(objLoanProductInterCalcTO.getReviewPeriod());
       
        
        period = setPeriod(resultValue);
        setCboReviewPeriod(period);
        setTxtReviewPeriod(String.valueOf(resultValue));
        resetPeriod();
        //Dep Amt Loan Maturing period
        resultValue= CommonUtil.convertObjToInt(objLoanProductInterCalcTO.getDepAmtMaturingPeriod());
        period = setPeriod(resultValue);
        setCboDepAmtLoanMaturingPeriod(period);
        setTxtDepAmtLoanMaturingPeriod(String.valueOf(resultValue));
        resetPeriod();
        setTxtElgLoanAmt(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getEligibleDepForLoanAmt()));
        setTxtDepAmtMaturing(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getDepAmtMaturing()));
        setTxtMinAmtLoan(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMinAmtLoan()));
        setTxtMaxAmtLoan(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMaxAmtLoan()));
        setTxtApplicableInter(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getApplInterest()));
        setTxtMinInterDebited(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMinIntDebit()));
        setCboDepositRoundOff((String) getCbmDepositRoundOff().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getDepositRoundOff())));
        if (objLoanProductInterCalcTO.getSubsidy()!=null && objLoanProductInterCalcTO.getSubsidy().equals(YES)) setRdoSubsidy_Yes(true);
        else setRdoSubsidy_No(true);
        
        setCboLoanPeriodMul((String) getCbmLoanPeriodMul().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getLoanPeriodsMultiples())));
        setChkFixed(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getChkFixed()));
        setTxtFixedMargin(CommonUtil.convertObjToDouble(objLoanProductInterCalcTO.getFixedMargin()));
        
    }
    
    
        //==========INTEREST CALCULATION==========
    private void populateLoanProductSubsidyInterestRebateTOInterCalcTO(LoanProductSubsidyInterestRebateTO objLoanProductSubsidyInterestRebateTO) throws Exception{
        
        log.info("In objLoanProductSubsidyInterestRebateTOInterCalcTO...");
        if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getSubsidyAllowed()).equals("Y"))
            setRdoSubsidy_Yes(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getSubsidyAllowed()).equals("N"))
            setRdoSubsidy_No(true);
        
        
        if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getLoanBalance()).equals("Y"))
            setRdoLoanBalance_Yes(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getLoanBalance()).equals("N"))
            setRdoSubsidyAdjustLoanBalance_Yes( true);
        
        
        if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getSubsidyReceivedDate()).equals("Y"))
            setRdoSubsidyReceivedDate(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getSubsidyReceivedDate()).equals("N"))
            setRdoLoanOpenDate(true);
        else {
            setRdoSubsidyReceivedDate(false);
             setRdoLoanOpenDate(false);
        }
        
        if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getInterestRebateAllowed()).equals("Y"))
            setRdoInterestRebateAllowed_Yes(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getInterestRebateAllowed()).equals("N"))
            setRdoInterestRebateAllowed_No(true);
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getChkRebtSpl()).equals("Y")) {
            setChkRebtSpl(true);
        } else {
            setChkRebtSpl(false);
        }

        setTxtInterestRebatePercentage(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getInterestRebatePercentage()));
        setTxtFinYearStartingFromDD(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getFinYearStartingFromDD()));
        setTxtFinYearStartingFromMM(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getFinYearStartingFromMM()));
        
        getCbmInterestRebateCalculation().setKeyForSelected(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getRebateCalculation()));
        getCbmRebatePeriod().setKeyForSelected(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getRebatePeriod()));
        
        if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getPenalInterestWaiverAllowed()).equals("Y"))
            setRdoPenalInterestWaiverAllowed_Yes(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getPenalInterestWaiverAllowed()).equals("N"))
            setRdoPenalInterestWaiverAllowed_No(true);
        
        if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getInterestWaiverAllowed()).equals("Y"))
            setRdoInterestWaiverAllowed_Yes(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getInterestWaiverAllowed()).equals("N"))
            setRdoInterestWaiverAllowed_No(true);  
        if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getPrincipalWaiverAllowed()).equals("Y"))
            setRdoPrincipalWaiverAllowed_Yes(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getPrincipalWaiverAllowed()).equals("N"))
            setRdoPrincipalWaiverAllowed_No(true);  
         if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getNoticeWaiveOffAllowed()).equals("Y"))
            setRdoNoticeWaiverAllowed_Yes(true);
        else if(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getNoticeWaiveOffAllowed()).equals("N"))
            setRdoNoticeWaiverAllowed_No(true); 
       //added by rishad 23-04-2015
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getArcWaiver()).equals("Y")) {
            setRdoArcWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getArcWaiver()).equals("N")) {
            setRdoArcWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getArbitraryWaiver()).equals("Y")) {
            setRdoArbitraryWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getArbitraryWaiver()).equals("N")) {
            setRdoArbitraryWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getLegalWaiver()).equals("Y")) {
            setRdoLeagalWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getLegalWaiver()).equals("N")) {
            setRdoLeagalWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getAdvertiseWaiver()).equals("Y")) {
            setRdoAdvertiseWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getAdvertiseWaiver()).equals("N")) {
            setRdoAdvertiseWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getInsurenceWaiver()).equals("Y")) {
            setRdoInsurenceWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getInsurenceWaiver()).equals("N")) {
            setRdoInsurenceWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getMiscellaneousWaiver()).equals("Y")) {
            setRdoMiscellaneousWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getMiscellaneousWaiver()).equals("N")) {
            setRdoMiscellaneousWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getPostageWaiver()).equals("Y")) {
            setRdoPostageWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getPostageWaiver()).equals("N")) {
            setRdoPostageWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getDecreeWaiver()).equals("Y")) {
            setRdoDecreeWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getDecreeWaiver()).equals("N")) {
            setRdoDecreeWaiverAllowed_No(true);
        }
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getEpWaiver()).equals("Y")) {
            setRdoEpWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getEpWaiver()).equals("N")) {
            setRdoEpWaiverAllowed_No(true);
        }
        
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getOverdueIntWaiver()).equals("Y")) {
            setRdoOverDueWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getOverdueIntWaiver()).equals("N")) {
            setRdoOverDueWaiverAllowed_No(true);
        }   
        
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getRecoveryWaiver()).equals("Y")) {
            setRdoRecoveryWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getRecoveryWaiver()).equals("N")) {
            setRdoRecoveryWaiverAllowed_No(true);
        } 
        
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getMeasurementWaiver()).equals("Y")) {
            setRdoMeasurementWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getMeasurementWaiver()).equals("N")) {
            setRdoMeasurementWaiverAllowed_No(true);
        } 
        
        
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getKoleFieldExpenseWaiver()).equals("Y")) {
             setRdoKoleFieldExpenseWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getKoleFieldExpenseWaiver()).equals("N")) {
            setRdoKoleFieldExpenseWaiverAllowed_No(true);
        } 
        
        if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getKoleFieldOperationWaiver()).equals("Y")) {
             setRdoKoleFieldOperationWaiverAllowed_Yes(true);
        } else if (CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getKoleFieldOperationWaiver()).equals("N")) {
            setRdoKoleFieldOperationWaiverAllowed_No(true);
        } 
        
        setTxtRebateLoanIntPercent(CommonUtil.convertObjToStr(objLoanProductSubsidyInterestRebateTO.getLoanInteRebatePercent())); // Added by nithya on 11-01-2020 for KD-1234
        
        
//        setTxtElgLoanAmt(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getEligibleDepForLoanAmt()));
//        setTxtMinAmtLoan(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMinAmtLoan()));
//        setTxtMaxAmtLoan(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMaxAmtLoan()));
//        setTxtApplicableInter(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getApplInterest()));
//        setTxtMinInterDebited(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getMinIntDebit()));
//        setCboDepositRoundOff((String) getCbmDepositRoundOff().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getDepositRoundOff())));
//        if (objLoanProductInterCalcTO.getSubsidy().equals(YES)) setRdoSubsidy_Yes(true);
//        else setRdoSubsidy_No(true);
//        
//        setCboLoanPeriodMul((String) getCbmLoanPeriodMul().getDataForKey(CommonUtil.convertObjToStr(objLoanProductInterCalcTO.getLoanPeriodsMultiples())));
    }

    
    private void setLoanProductClassificationsTO(LoanProductClassificationsTO objLoanProductClassificationsTO) throws Exception{
        log.info("In setLoanProductcClassificationTO...");
        setCboCommodityCode((String) getCbmCommodityCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getCommodityCode())));
        setCboTypeFacility((String) getCbmTypeFacility().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getFacilityType())));
        setCboPurposeCode((String) getCbmPurposeCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getPurposeCode())));
        setCboIndusCode((String) getCbmIndusCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getIndustryCode())));
        setCbo20Code((String) getCbm20Code().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getTwentyCode())));
        setCboGovtSchemeCode((String) getCbmGovtSchemeCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getGovtSchemeCode())));
        if (CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getEcgc()).equals(YES)){
            setChkECGC(true);
        }else {
            setChkECGC(false);
        }
        
        setCboGuaranteeCoverCode((String) getCbmGuaranteeCoverCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getGuaranteeCoverCode())));
        setCboHealthCode((String) getCbmHealthCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getHealthCode())));
        setCboSectorCode((String) getCbmSectorCode().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getSectorCode())));
        setCboRefinancingInsti((String) getCbmRefinancingInsti().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getRefinancingInstitution())));
        if (CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getDirectFinance()).equals(YES)){
            setChkDirectFinance(true);
        }else {
            setChkDirectFinance(false);
        }
        if (CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getQis()).equals(YES)){
            setChkQIS(true);
        }else {
            setChkQIS(false);
        }
        setCboSecurityDeails((String) getCbmSecurityDeails().getDataForKey(CommonUtil.convertObjToStr(objLoanProductClassificationsTO.getSecurityDetails())));
    }
    
    // Method to calculate is its Years, Months or Days
    //depending on the numerical value from DataBase...
    // to be displayed in UI
    private String setPeriod(int rV) throws Exception{
        String periodValue;
        if ((rV >= year) && (rV%year == 0)) {
            periodValue = YEAR1;
            rV = rV/year;
        } else if ((rV >= month) && (rV % month == 0)) {
            periodValue=MONTH1;
            rV = rV/month;
        } else if ((rV >= day) && (rV % day == 0)) {
            periodValue=DAY1;
            rV = rV;
        } else {
            periodValue="";
            rV = 0;
        }
        resultValue = rV;
        return periodValue;
    }
    
    private void resetPeriod(){
        resultValue = 0;
    }
    // to find the value of multiplier on the basis of period...
    public double setCombo(String duration) throws Exception{
        periodData=0;
        resultData=0;
        int period=0;
        if(!duration.equalsIgnoreCase("")){
            if( duration.equals(DAY))
                period = day;
            else if(duration.equals(MONTH))
                period = month;
            else if(duration.equals(YEAR))
                period = year;
        }
        
        duration = "";
        return period;
    }
    public void addTargetList(int selected){
        String selectDeposit=CommonUtil.convertObjToStr(lstAvailableDeposits.getElementAt(selected));
        System.out.println("selectDeposit###"+selectDeposit);
        lstSelectedDeposits.addElement(selectDeposit);
        lstAvailableDeposits.removeElementAt(selected);
        HashMap singleMap=new HashMap();
        singleMap.put("SELECTED_DEPOSITS",selectDeposit);
        newDepositMap.put(selectDeposit,singleMap);
    }
    public void removeTargetList(int selected){
        String selectDeposit=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(selected));
        System.out.println("selectDeposit###"+selectDeposit);
        lstAvailableDeposits.addElement(selectDeposit);
        lstSelectedDeposits.removeElementAt(selected);
        deleteDepositMap.put(selectDeposit, existDepositMap.get(String.valueOf(selectDeposit)));
        if(newDepositMap.get(String.valueOf(selectDeposit))!=null)
            deleteDepositMap.put(selectDeposit, newDepositMap.get(String.valueOf(selectDeposit)));
        existDepositMap.remove(selectDeposit);
        newDepositMap.remove(selectDeposit);
        
    }
        public void addTargetTransactionList(int selected){
        String selectTransaction=CommonUtil.convertObjToStr(lstAvailableTransaction.getElementAt(selected));
        System.out.println("selectDeposit###"+selectTransaction);
        lstSelectedTransaction.addElement(selectTransaction);
        lstAvailableTransaction.removeElementAt(selected);
        HashMap singleMap=new HashMap();
        singleMap.put("SELECTED_DEPOSITS",selectTransaction);
        newTransactionMap.put(selectTransaction,singleMap);
    }
    public void addTargetTransactionList_OTS(int selected){
        String selectTransaction=CommonUtil.convertObjToStr(lstAvailableTransaction_OTS.getElementAt(selected));
        System.out.println("selectDeposit###"+selectTransaction);
        lstSelectedTransaction_OTS.addElement(selectTransaction);
        lstAvailableTransaction_OTS.removeElementAt(selected);
        HashMap singleMap=new HashMap();
        singleMap.put("SELECTED_DEPOSITS",selectTransaction);
        newOTSTransactionMap.put(selectTransaction,singleMap);
    }
    public void removeTargetTransactionList(int selected){
        String selectTransaction=CommonUtil.convertObjToStr(lstSelectedTransaction.getElementAt(selected));
        System.out.println("selectDeposit###"+selectTransaction);
        lstAvailableTransaction.addElement(selectTransaction);
        lstSelectedTransaction.removeElementAt(selected);
        deleteTransactionMap.put(selectTransaction, existTransactionMap.get(String.valueOf(selectTransaction)));
        if(newTransactionMap.get(String.valueOf(selectTransaction))!=null)
            deleteTransactionMap.put(selectTransaction, newTransactionMap.get(String.valueOf(selectTransaction)));
        existTransactionMap.remove(selectTransaction);
        newTransactionMap.remove(selectTransaction);
        
    }
    
    public void removeTargetTransactionList_OTS(int selected){
        String selectTransaction=CommonUtil.convertObjToStr(lstSelectedTransaction_OTS.getElementAt(selected));
        System.out.println("selectDeposit###"+selectTransaction);
        lstAvailableTransaction_OTS.addElement(selectTransaction);
        lstSelectedTransaction_OTS.removeElementAt(selected);
        deleteOTSTransactionMap.put(selectTransaction, existOTSTransactionMap.get(String.valueOf(selectTransaction)));
        if(newOTSTransactionMap.get(String.valueOf(selectTransaction))!=null)
            deleteOTSTransactionMap.put(selectTransaction, newOTSTransactionMap.get(String.valueOf(selectTransaction)));
        existOTSTransactionMap.remove(selectTransaction);
        newOTSTransactionMap.remove(selectTransaction);
        
    }
    

    //========================================================================================
    /* To inser the data into the database from the UI*/
    
    // Set and Get methods for setting and getting Action Type like New, Edit, Delete...
    public int getActionType(){
        log.info("In getActionType...");
        
        return actionType;
    }
    
    /**
     * @param actionType Form action Type
     */
    public void setActionType(int actionType) {
        log.info("In setActionType...");
        
        this.actionType = actionType;
        setChanged();
    }
    
    public void setResult(int result) {
        log.info("In setResult...");
        
        this.result = result;
        setChanged();
    }
    public int getResult(){
        log.info("In getResult...");
        return this.result;
    }
    
    //----------Account----------
    /* To set common data in the Transfer Object*/
    public LoanProductAccountTO setLoanProductAccount() {
        log.info("In setLoanProductAccount...");
        
        final LoanProductAccountTO objLoanProductAccountTO = new LoanProductAccountTO();
        try{
            objLoanProductAccountTO.setExcludeScSt(CommonUtil.convertObjToStr(getChkExcludeScSt()));
            objLoanProductAccountTO.setGroupLoan(CommonUtil.convertObjToStr(getChkGroupLoan()));
            objLoanProductAccountTO.setExcludeTOD(CommonUtil.convertObjToStr(getChkExcludeTOD()));
            objLoanProductAccountTO.setShareLink(CommonUtil.convertObjToStr(getChkShareLink()));
            objLoanProductAccountTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductAccountTO.setProdDesc(CommonUtil.convertObjToStr(txtProductDesc));
            objLoanProductAccountTO.setAcctHead(CommonUtil.convertObjToStr(txtAccHead));
            objLoanProductAccountTO.setBehavesLike(CommonUtil.convertObjToStr((String)cbmOperatesLike.getKeyForSelected()));
            objLoanProductAccountTO.setAuthorizeRemark(CommonUtil.convertObjToStr((String)cbmProdCategory.getKeyForSelected()));
            objLoanProductAccountTO.setBaseCurrency(LocaleConstants.DEFAULT_CURRENCY);
            
            
             //-- Added by Nithya
            objLoanProductAccountTO.setRepaymentType((String)cbmRepaymentType.getKeyForSelected());
            System.out.println("NewLoanProductOB ::getKeyForSelected : " + objLoanProductAccountTO.getRepaymentType());
            objLoanProductAccountTO.setRepaymentFreq(CommonUtil.convertObjToDouble(cbmRepaymentFreq.getKeyForSelected()));
            System.out.println("NewLoanProductOB ::getKeyForSelected : " + objLoanProductAccountTO.getRepaymentFreq());
            // --- End
            
            /** To set The Created By and/or Status By...
             * Depending on the Type of Task to be performed...
             */
            if(getActionType() ==  ClientConstants.ACTIONTYPE_NEW){
                objLoanProductAccountTO.setCreatedBy(TrueTransactMain.USER_ID);
            }else if((getActionType() ==  ClientConstants.ACTIONTYPE_DELETE)
            || (getActionType() ==  ClientConstants.ACTIONTYPE_EDIT)){
                objLoanProductAccountTO.setStatusBy(TrueTransactMain.USER_ID);
            }
            objLoanProductAccountTO.setDefault_Prod("N");
            objLoanProductAccountTO.setGoldStockPhotoStored(getChkGoldStockPhoto());// Added by nithya on 29-10-2019 for KD-763	Need Gold ornaments photo saving option
            objLoanProductAccountTO.setBlockIntPostIfLimitExceeds(getChkBlockIFLimitExceed()); // Added by nithya on 16-11-2019 for KD-729
            
        }catch(Exception e){
            log.info("Error in setLoanProductAccount()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductAccountTO;
    }
   
    public LoanProductGroupLoanTO setLoanProductGroupLoan() {
        log.info("In setLoanProductGroupLoan...");

        final LoanProductGroupLoanTO objLoanProductGroupLoanTO = new LoanProductGroupLoanTO();
        try {
            if (isRdoDebitAllowedForCustomer_Yes()) {
                objLoanProductGroupLoanTO.setIsDebitAllowedForDueCustomer("Y");
            } else if (isRdoLoanOpenDate()) {
                objLoanProductGroupLoanTO.setIsDebitAllowedForDueCustomer("N");
            } else {
                objLoanProductGroupLoanTO.setIsDebitAllowedForDueCustomer("");
            }
            objLoanProductGroupLoanTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductGroupLoanTO.setIntCalcDay(CommonUtil.convertObjToInt(cbmIntCalcDay.getSelectedItem()));
            objLoanProductGroupLoanTO.setIntCalcMonth(CommonUtil.convertObjToInt(cbmIntCalcMonth.getSelectedItem()));
           // objLoanProductGroupLoanTO.setIsDebitAllowedForDueCustomer(CommonUtil.convertObjToStr(getTxtDebitAllowed()));
            objLoanProductGroupLoanTO.setInterestCalcDay(getCboIfHoliday());
            
            /** To set The Created By and/or Status By...
             * Depending on the Type of Task to be performed...
             */
           
        }catch(Exception e){
            log.info("Error in setLoanProductAccount()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductGroupLoanTO;
    }
    
    private LoanProductSubsidyInterestRebateTO setLoanProductSubsidyInterestRebateTO(){
        LoanProductSubsidyInterestRebateTO obj =new  LoanProductSubsidyInterestRebateTO();
        try {
            obj.setProdId(CommonUtil.convertObjToStr(getTxtProductID()));

            if (isRdoSubsidy_Yes()) {
                obj.setSubsidyAllowed("Y");
            } else if (isRdoSubsidy_Yes()) {
                obj.setSubsidyAllowed("N");
            } else {
                obj.setSubsidyAllowed("");
            }
            if (isRdoLoanBalance_Yes()) {
                obj.setLoanBalance("Y");
            } else if (isRdoSubsidyAdjustLoanBalance_Yes()) {
                obj.setLoanBalance("N");
            } else {
                obj.setLoanBalance("");
            }
            if (isRdoSubsidyReceivedDate()) {
                obj.setSubsidyReceivedDate("Y");
            } else if (isRdoLoanOpenDate()) {
                obj.setSubsidyReceivedDate("N");
            } else {
                obj.setSubsidyReceivedDate("");
            }
            if (isRdoInterestRebateAllowed_Yes()) {
                obj.setInterestRebateAllowed("Y");
            } else if (isRdoInterestRebateAllowed_No()) {
                obj.setInterestRebateAllowed("N");
            } else {
                obj.setInterestRebateAllowed("");
            }
            if(isChkRebtSpl()){
                obj.setChkRebtSpl("Y");
            }else{
               obj.setChkRebtSpl("N");  
            }
            obj.setInterestRebatePercentage(CommonUtil.convertObjToDouble(getTxtInterestRebatePercentage()));
            obj.setFinYearStartingFromDD(CommonUtil.convertObjToInt(getTxtFinYearStartingFromDD()));
            obj.setFinYearStartingFromMM(CommonUtil.convertObjToInt(getTxtFinYearStartingFromMM()));
            obj.setRebateCalculation(CommonUtil.convertObjToStr(cbmInterestRebateCalculation.getKeyForSelected()));
            obj.setRebatePeriod(CommonUtil.convertObjToStr(cbmRebatePeriod.getKeyForSelected()));
            System.out.println("isRdoPrincipalWaiverAllowed_Yes()"+isRdoPrincipalWaiverAllowed_Yes());
            if (isRdoInterestWaiverAllowed_Yes()) {
                obj.setInterestWaiverAllowed("Y");
            } else if (isRdoInterestWaiverAllowed_No()) {
                obj.setInterestWaiverAllowed("N");
            } else {
                obj.setInterestWaiverAllowed("");
            }
            if (isRdoPrincipalWaiverAllowed_Yes()) {
                obj.setPrincipalWaiverAllowed("Y");
            } else if (isRdoPrincipalWaiverAllowed_No()) {
                obj.setPrincipalWaiverAllowed("N");
            }
            if (isRdoPenalInterestWaiverAllowed_Yes()) {
                obj.setPenalInterestWaiverAllowed("Y");
            } else if (isRdoPenalInterestWaiverAllowed_No()) {
                obj.setPenalInterestWaiverAllowed("N");
            } else {
                obj.setPenalInterestWaiverAllowed("");
            }

            if (isRdoNoticeWaiverAllowed_Yes()) {
                obj.setNoticeWaiveOffAllowed("Y");
            } else if (isRdoNoticeWaiverAllowed_No()) {
                obj.setNoticeWaiveOffAllowed("N");
            } else {
                obj.setNoticeWaiveOffAllowed("");
            }

            if (isRdoAdvertiseWaiverAllowed_Yes()) {
                obj.setAdvertiseWaiver("Y");
            } else if (isRdoAdvertiseWaiverAllowed_No()) {
                obj.setAdvertiseWaiver("N");
            }
            if (isRdoArbitraryWaiverAllowed_Yes()) {
                obj.setArbitraryWaiver("Y");
            } else if (isRdoArbitraryWaiverAllowed_No()) {
                obj.setArbitraryWaiver("N");
            }
            if (isRdoLeagalWaiverAllowed_Yes()) {
                obj.setLegalWaiver("Y");
            } else if (isRdoLeagalWaiverAllowed_No()) {
                obj.setLegalWaiver("N");
            }
            if (isRdoInsurenceWaiverAllowed_Yes()) {
                obj.setInsurenceWaiver("Y");
            } else if (isRdoInsurenceWaiverAllowed_No()) {
                obj.setInsurenceWaiver("N");
            }
            if (isRdoEpWaiverAllowed_Yes()) {
                obj.setEpWaiver("Y");
            } else if (isRdoEpWaiverAllowed_No()) {
                obj.setEpWaiver("N");
            }
            if (isRdoMiscellaneousWaiverAllowed_Yes()) {
                obj.setMiscellaneousWaiver("Y");
            } else if (isRdoMiscellaneousWaiverAllowed_No()) {
                obj.setMiscellaneousWaiver("N");
            }
            if (isRdoPostageWaiverAllowed_Yes()) {
                obj.setPostageWaiver("Y");
            } else if (isRdoPostageWaiverAllowed_No()) {
                obj.setPostageWaiver("N");
            }
            if (isRdoDecreeWaiverAllowed_Yes()) {
                obj.setDecreeWaiver("Y");
            } else if (isRdoDecreeWaiverAllowed_No()) {
                obj.setDecreeWaiver("N");
            }            
            if (isRdoArcWaiverAllowed_Yes()) {
                obj.setArcWaiver("Y");
            } else if (isRdoArcWaiverAllowed_No()) {
                obj.setArcWaiver("N");
            }
            
            if(isRdoOverDueWaiverAllowed_Yes()){
                obj.setOverdueIntWaiver("Y");
            }else if(isRdoOverDueWaiverAllowed_No()){
                obj.setOverdueIntWaiver("N");
            }
            
            if(isRdoRecoveryWaiverAllowed_Yes()){
                obj.setRecoveryWaiver("Y");
            }else if(isRdoRecoveryWaiverAllowed_No()){
                obj.setRecoveryWaiver("N"); 
            }
            
            if(isRdoMeasurementWaiverAllowed_Yes()){
                obj.setMeasurementWaiver("Y");
            }else if(isRdoMeasurementWaiverAllowed_No()){
                obj.setMeasurementWaiver("N");
            }
            
            if(isRdoKoleFieldExpenseWaiverAllowed_Yes()){
                obj.setKoleFieldExpenseWaiver("Y");
            }else if(isRdoKoleFieldExpenseWaiverAllowed_Yes()){
                obj.setKoleFieldExpenseWaiver("N");
            }
            
             if(isRdoKoleFieldOperationWaiverAllowed_Yes()){
                obj.setKoleFieldOperationWaiver("Y");
            }else if(isRdoKoleFieldOperationWaiverAllowed_Yes()){
                obj.setKoleFieldOperationWaiver("N");
            }
            
            obj.setLoanInteRebatePercent(CommonUtil.convertObjToDouble(getTxtRebateLoanIntPercent())); // Added by nithya on 11-01-2020 for KD-1234
            
         if(getCommand().equals(CommonConstants.TOSTATUS_INSERT))
            obj.setStatus(CommonConstants.STATUS_CREATED);
         else if (getCommand().equals(CommonConstants.TOSTATUS_UPDATE))
             obj.setStatus(CommonConstants.STATUS_MODIFIED);
         else if (getCommand().equals(CommonConstants.TOSTATUS_DELETE))
             obj.setStatus(CommonConstants.STATUS_DELETED);
        }catch(Exception e){
            e.printStackTrace();
        }
            
        return obj;
    }
    /**
     * To set the data in LoanProductDocuments TO
     */
    public LoanProductDocumentsTO setLoanProductDocumentsTO() {
        log.info("In setLoanProductDocumentsTO...");
        
        final LoanProductDocumentsTO objLoanProductDocumentsTO = new LoanProductDocumentsTO();
        try{
            objLoanProductDocumentsTO.setProdId(CommonUtil.convertObjToStr(getTxtProductID()));
            objLoanProductDocumentsTO.setSINo(CommonUtil.convertObjToStr(getDocumentSerialNo()));
            objLoanProductDocumentsTO.setDocDesc(CommonUtil.convertObjToStr(getTxtDocumentDesc()));
            objLoanProductDocumentsTO.setDocNo(CommonUtil.convertObjToStr(getTxtDocumentNo()));
            objLoanProductDocumentsTO.setDocType(CommonUtil.convertObjToStr((String)cbmDocumentType.getKeyForSelected()));
            objLoanProductDocumentsTO.setStatus(CommonUtil.convertObjToStr(getDocumentStatus()));
            
        }catch(Exception e){
            log.info("Error in setLoanProductDocumentsTO()");
            parseException.logException(e,true);
        }
        return objLoanProductDocumentsTO;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    /**
     * Set Documents fields in the OB
     */
    private void setLoanProductDocumentsOB(LoanProductDocumentsTO objLoanProductDocumentsTO) throws Exception {
        log.info("Inside setLoanProductDocumentsOB()");
        setTxtProductID(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getProdId()));
        setTxtDocumentDesc(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocDesc()));
        setTxtDocumentNo(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocNo()));
        setDocumentSerialNo(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getSINo()));
        setDocumentStatus(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getStatus()));
        setCboDocumentType((String) getCbmDocumentType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocType())));
        ttNotifyObservers();
    }
    public LoanProductAccountParamTO setLoanProductAccountParam() {
        log.info("In setLoanProductAccountParam...");
        
        final LoanProductAccountParamTO objLoanProductAccountParamTO = new LoanProductAccountParamTO();
        try{
            objLoanProductAccountParamTO.setChkEMISimpleInt(CommonUtil.convertObjToStr(getChkEMIInSimpleIntrst()));
            objLoanProductAccountParamTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
              objLoanProductAccountParamTO.setSuspenseCreditAchd(txtSuspenseCreditAchd);
            objLoanProductAccountParamTO.setSuspenseDebitAchd(txtSuspenseDebitAchd);
             objLoanProductAccountParamTO.setChkAuctionAllowed(CommonUtil.convertObjToStr(getChkAuctionAllowed()));
            objLoanProductAccountParamTO.setNumberPattern(CommonUtil.convertObjToStr(txtNumPatternFollowed));
            objLoanProductAccountParamTO.setNumberPatternSuffix(CommonUtil.convertObjToStr(txtNumPatternFollowedSuffix));
            objLoanProductAccountParamTO.setMaxCashPayment(CommonUtil.convertObjToDouble(txtMaxCashDisb));
             //            if(getTxtNumPatternFollowedSuffix().equals("0"))
            //               objLoanProductAccountParamTO.setLastAcNo("0") ;
            //            else{
            //                int s= CommonUtil.convertObjToInt(getTxtNumPatternFollowedSuffix());
            //                s=s-1;
            //                objLoanProductAccountParamTO.setLastAcNo(String.valueOf(s));
            //            }
            objLoanProductAccountParamTO.setLastAcNo(getTxtLastAccNum());
            if (getRdoIsLimitDefnAllowed_Yes() == true) {
                objLoanProductAccountParamTO.setLimitDefAllowed(YES);
            } else /*if (getRdoIsLimitDefnAllowed_No() == true)*/ {
                objLoanProductAccountParamTO.setLimitDefAllowed(NO);
            }
            
            if (isRdoDisbAftMoraPerd_Yes() == true) {
                objLoanProductAccountParamTO.setIsDisbAftMoraPerd(YES);
            } else {
                objLoanProductAccountParamTO.setIsDisbAftMoraPerd(NO);
            }
            
            if (isRdoIsCreditAllowed_Yes() == true) {
                objLoanProductAccountParamTO.setIsCreditAllowedToPricipal(YES);
            } else{
                objLoanProductAccountParamTO.setIsCreditAllowedToPricipal(NO);
            }
            
            if(isRdoIsInterestFirst_Yes() == true){
                objLoanProductAccountParamTO.setIsInterestFirst(YES);
                objLoanProductAccountParamTO.setFreqInDays(getTxtFrequencyInDays());
            } else { 
                objLoanProductAccountParamTO.setIsInterestFirst(NO);
            }
             if(isRdoIsInterestDue_Yes() == true){
                objLoanProductAccountParamTO.setIsInterestLoanDue(YES);
            } else { 
                objLoanProductAccountParamTO.setIsInterestLoanDue(NO);
            }
            if (getRdoIsStaffAccOpened_Yes() == true) {
                objLoanProductAccountParamTO.setStaffAcOpened(YES);
            } else /*if (getRdoIsStaffAccOpened_No() == true)*/ {
                objLoanProductAccountParamTO.setStaffAcOpened(NO);
            }
            
            if (getRdoIsDebitInterUnderClearing_Yes() == true) {
                objLoanProductAccountParamTO.setDebitIntClearingappl(YES);
            } else /*if (getRdoIsDebitInterUnderClearing_No() == true)*/ {
                objLoanProductAccountParamTO.setDebitIntClearingappl(NO);
            }
             if (isRdoAuctionAmt_Yes() == true) {
                objLoanProductAccountParamTO.setAuctionAmt(YES);
            } else /*if (getRdoIsDebitInterUnderClearing_No() == true)*/ {
                objLoanProductAccountParamTO.setAuctionAmt(NO);
            }
             ///goldloan renewal param
             if(isChkGldRenewOldAmt()==true){
                 objLoanProductAccountParamTO.setGldRenewOldAmt(YES);
             }else{
                 objLoanProductAccountParamTO.setGldRenewOldAmt(NO);
             }
              if(isChkAuctionAllowd()==true){
                 objLoanProductAccountParamTO.setChkAuctionAllowed(YES);
             }else{
                 objLoanProductAccountParamTO.setChkAuctionAllowed(NO);
             }
             if(isChkGldRenewCash()==true){
               objLoanProductAccountParamTO.setGldRenewCash(YES);
             }else{
                 objLoanProductAccountParamTO.setGldRenewCash(NO);
             }
              if(isChkGldRenewMarketRate()==true){
               objLoanProductAccountParamTO.setGldRenewMarketRate(YES);
             }else{
                 objLoanProductAccountParamTO.setGldRenewMarketRate(NO);
             }
              if(isChkGldRnwNwIntRate()==true)
              {
                 objLoanProductAccountParamTO.setGldRnwNwIntRate(YES);
              }
              else
              {
                  objLoanProductAccountParamTO.setGldRnwNwIntRate(NO);
              }
              if(isChkInterestOnMaturity() == true){
                  objLoanProductAccountParamTO.setInterestOnMaturity(YES);
              }else{
                  objLoanProductAccountParamTO.setInterestOnMaturity(NO);
              }
              
            if (CommonConstants.SAL_REC_MODULE.equals("Y")) {
                if (isChkSalRec()) {
                    objLoanProductAccountParamTO.setIsSalaryRecovery("Y");
                } else {
                    objLoanProductAccountParamTO.setIsSalaryRecovery("N");
                }
            }
            
            objLoanProductAccountParamTO.setIsIntCalcPeriodWise(getChkIsInterestPeriodWise());// Added by nithya on 15-11-2017 for 7867
              
        } catch(Exception e){
            log.info("Error in setLoanProductAccountParam()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductAccountParamTO;
    }
    
    //----------INTEREST RECEIVABLE----------
    public LoanProductInterReceivableTO setLoanProductInterReceivable() {
        log.info("In setLoanProductInterReceivable...");
        final LoanProductInterReceivableTO objLoanProductInterReceivableTO = new LoanProductInterReceivableTO();
        try{
            objLoanProductInterReceivableTO.setProdId(txtProductID);
            
            if (getRdoldebitInterCharged_Yes() == true) {
                objLoanProductInterReceivableTO.setDebitIntCharged(YES);
            } else /*if (getRdoldebitInterCharged_No() == true)*/ {
                objLoanProductInterReceivableTO.setDebitIntCharged(NO);
            }
            objLoanProductInterReceivableTO.setMinDebitintRate(CommonUtil.convertObjToDouble(txtMinDebitInterRate));
            objLoanProductInterReceivableTO.setMaxDebitintRate(CommonUtil.convertObjToDouble(txtMaxDebitInterRate));
            objLoanProductInterReceivableTO.setMinDebitintAmt(CommonUtil.convertObjToDouble(txtMinDebitInterAmt));
            objLoanProductInterReceivableTO.setMaxDebitintAmt(CommonUtil.convertObjToDouble(txtMaxDebitInterAmt));
            objLoanProductInterReceivableTO.setDebitintCalcFreq(CommonUtil.convertObjToDouble(cbmDebInterCalcFreq.getKeyForSelected()));
            objLoanProductInterReceivableTO.setDebitintApplFreq(CommonUtil.convertObjToDouble(cbmDebitInterAppFreq.getKeyForSelected()));
            objLoanProductInterReceivableTO.setDebitintCompFreq(CommonUtil.convertObjToDouble(cbmDebitInterComFreq.getKeyForSelected()));
            objLoanProductInterReceivableTO.setDebitProdRoundoff((String)cbmDebitProdRoundOff.getKeyForSelected());
            objLoanProductInterReceivableTO.setDebitIntRoundoff((String)cbmDebitInterRoundOff.getKeyForSelected());
            //objLoanProductInterReceivableTO.setInterestRepaymentFreq((String)cbmInterestRepaymentFreq.getKeyForSelected()); 
            objLoanProductInterReceivableTO.setInterestRepaymentFreq(CommonUtil.convertObjToInt(cbmInterestRepaymentFreq.getKeyForSelected()));           
          
            Date LstIntDt = DateUtil.getDateMMDDYYYY(tdtLastInterCalcDate);
            if(LstIntDt != null){
                Date lstintDate = (Date)curDate.clone();
                lstintDate.setDate(LstIntDt.getDate());
                lstintDate.setMonth(LstIntDt.getMonth());
                lstintDate.setYear(LstIntDt.getYear());
                //            objLoanProductInterReceivableTO.setLastIntcalcDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterCalcDate)));.
                objLoanProductInterReceivableTO.setLastIntcalcDtdebit(lstintDate);
            }else{
                objLoanProductInterReceivableTO.setLastIntcalcDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterCalcDate)));
            }
            
            Date LstAppDt = DateUtil.getDateMMDDYYYY(tdtLastInterAppDate);
            if(LstAppDt != null){
                Date lsAppDate = (Date)curDate.clone();
                lsAppDate.setDate(LstAppDt.getDate());
                lsAppDate.setMonth(LstAppDt.getMonth());
                lsAppDate.setYear(LstAppDt.getYear());
                //            objLoanProductInterReceivableTO.setLastIntapplDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterAppDate)));
                objLoanProductInterReceivableTO.setLastIntapplDtdebit(lsAppDate);
            }else{
                objLoanProductInterReceivableTO.setLastIntapplDtdebit(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastInterAppDate)));
            }
            
            objLoanProductInterReceivableTO.setProdFreq(CommonUtil.convertObjToDouble(cbmProductFreq.getKeyForSelected()));
            
            if (getRdoPLRRateAppl_Yes() == true) {
                objLoanProductInterReceivableTO.setPlrRateAppl(YES);
            } else /*if (getRdoPLRRateAppl_No() == true)*/ {
                objLoanProductInterReceivableTO.setPlrRateAppl(NO);
            }
            
            objLoanProductInterReceivableTO.setPlrRate(CommonUtil.convertObjToDouble(txtPLRRate));
            
            Date PlrDt = DateUtil.getDateMMDDYYYY(tdtPLRAppForm);
            if(PlrDt != null){
                Date plrDate = (Date)curDate.clone();
                plrDate.setDate(PlrDt.getDate());
                plrDate.setMonth(PlrDt.getMonth());
                plrDate.setYear(PlrDt.getYear());
                //            objLoanProductInterReceivableTO.setPlrApplFrom(DateUtil.getDateMMDDYYYY(tdtPLRAppForm));
                objLoanProductInterReceivableTO.setPlrApplFrom(plrDate);
            }else{
                objLoanProductInterReceivableTO.setPlrApplFrom(DateUtil.getDateMMDDYYYY(tdtPLRAppForm));
            }
            
            if (getRdoPLRApplNewAcc_Yes() == true) {
                objLoanProductInterReceivableTO.setPlrApplNewac(YES);
            } else /*if (getRdoPLRApplNewAcc_No() == true)*/ {
                objLoanProductInterReceivableTO.setPlrApplNewac(NO);
            }
            
            if (getRdoPLRApplExistingAcc_Yes() == true) {
                objLoanProductInterReceivableTO.setPlrApplExistac(YES);
            } else /*if (getRdoPLRApplExistingAcc_No() == true)*/ {
                objLoanProductInterReceivableTO.setPlrApplExistac(NO);
            }
            
            Date AccSanDt = DateUtil.getDateMMDDYYYY(tdtPlrApplAccSancForm);
            if(AccSanDt != null){
                Date accsanDate = (Date)curDate.clone();
                accsanDate.setDate(AccSanDt.getDate());
                accsanDate.setMonth(AccSanDt.getMonth());
                accsanDate.setYear(AccSanDt.getYear());
                //            objLoanProductInterReceivableTO.setPlrApplSancfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtPlrApplAccSancForm)));
                objLoanProductInterReceivableTO.setPlrApplSancfrom(accsanDate);
            }else{
                objLoanProductInterReceivableTO.setPlrApplSancfrom(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtPlrApplAccSancForm)));
            }
            
            if (getRdoPenalAppl_Yes() == true) {
                objLoanProductInterReceivableTO.setPenalAppl(YES);
            } else /*if (getRdoPenalAppl_No() == true)*/ {
                objLoanProductInterReceivableTO.setPenalAppl(NO);
            }
            if (getRdoLimitExpiryInter_Yes() == true) {
                objLoanProductInterReceivableTO.setLimitExpiryInt(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setLimitExpiryInt(NO);
            }
            if (isRdoasAndWhenCustomer_Yes() == true) {
                objLoanProductInterReceivableTO.setAsAndWhenCustomer_Yes(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setAsAndWhenCustomer_Yes(NO);
            }
            if (isRdocalendarFrequency_Yes() == true) {
                objLoanProductInterReceivableTO.setCalendarFrequency_Yes(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setCalendarFrequency_Yes(NO);
            }
            if (isRdobill_Yes() == true) {
                objLoanProductInterReceivableTO.setBillByBill(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setBillByBill(NO);
            }
            if (isChkprincipalDue() == true) {
                objLoanProductInterReceivableTO.setPrincipalDue(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setPrincipalDue(NO);
                
            }
            if (isChkInterestDue() == true) {
                objLoanProductInterReceivableTO.setInterestDue(YES);
            } else /*if (getRdoLimitExpiryInter_No() == true)*/ {
                objLoanProductInterReceivableTO.setInterestDue(NO);
                
            }
             // Added by nithya on 22-03-2018 for 0008470: Property Mortgage Loan Ledger modification as per attachment
            if(isChkEmiPenal() == true){
                objLoanProductInterReceivableTO.setEmiPenal(YES);
            }else{
                objLoanProductInterReceivableTO.setEmiPenal(NO);
            }
            
            if(isRdoPenalCalcDays() == true){
                objLoanProductInterReceivableTO.setEmiPenalCalcMethod("DAYS");
            }else{
                objLoanProductInterReceivableTO.setEmiPenalCalcMethod("MONTHS");
            }
            
            if(isRdoPenalCalcMonths() == true){
                objLoanProductInterReceivableTO.setEmiPenalCalcMethod("MONTHS");
            }else{
                objLoanProductInterReceivableTO.setEmiPenalCalcMethod("DAYS");
            }
            
            if(getChkIsOverdueInt().equalsIgnoreCase("Y")){
                objLoanProductInterReceivableTO.setIsOverDueIntTaken("Y");
            }else{
                objLoanProductInterReceivableTO.setIsOverDueIntTaken("N");  
            }
            //0008470: Property Mortgage Loan Ledger modification as per attachment : End
            
            // Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
            if(getChkPrematureIntCalc().equalsIgnoreCase("Y")){
              objLoanProductInterReceivableTO.setPrematureClosureIntCalcRequired("Y");  
            }else{
              objLoanProductInterReceivableTO.setPrematureClosureIntCalcRequired("N");   
            }

            // End
            
            // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE
            if(getChkIntCalcFromSanctionDt().equalsIgnoreCase("Y")){
                objLoanProductInterReceivableTO.setIntCalcFromSanctionDt("Y");
            }else{
                objLoanProductInterReceivableTO.setIntCalcFromSanctionDt("N");
            }
            objLoanProductInterReceivableTO.setGracePeriodForOverDueInt(CommonUtil.convertObjToInt(getTxtGracePeriodForOverDueInt()));
            // End
            
            
            if (isRdoMaturity_Yes()== true) {
                objLoanProductInterReceivableTO.setUptoMaturity(YES);
            } else if(isRdoCurrDt_Yes() == true) {
                objLoanProductInterReceivableTO.setUptoMaturity(NO);
                
            }else{
                objLoanProductInterReceivableTO.setUptoMaturity("");
            }
             if (isRdoMaturity_Deposit_closure_Yes()== true) {
                objLoanProductInterReceivableTO.setUptoDepositMaturity(YES);
            } else if(isRdoMaturity_Deposit_closure_Upto_Curr_Yes()== true) {
                objLoanProductInterReceivableTO.setUptoDepositMaturity(NO);
                
            }else{
                objLoanProductInterReceivableTO.setUptoMaturity("");
            }
            
             if (isRdoInterestDueKeptReceivable_Yes()== true) {
                objLoanProductInterReceivableTO.setInterestDueKeptReceivable(YES);
            } else if(isRdoInterestDueKeptReceivable_No()== true) {
                objLoanProductInterReceivableTO.setInterestDueKeptReceivable(NO);
                
            }else{
                objLoanProductInterReceivableTO.setInterestDueKeptReceivable("");
            }
            
             if (isRdoinsuranceCharge_Yes()== true) {
                objLoanProductInterReceivableTO.setInsuranceApplicable(YES);
            } else if(isRdoinsuranceCharge_No()== true) {
                objLoanProductInterReceivableTO.setInsuranceApplicable(NO);
                
            }else{
                objLoanProductInterReceivableTO.setInsuranceApplicable("");
            }
            
             if (isRdosanctionDate_Yes()== true) {
                 objLoanProductInterReceivableTO.setInsuranceSanctionDt(YES);
             } else if(isRdosanctionDate_No()== true) {
                 objLoanProductInterReceivableTO.setInsuranceSanctionDt(NO);
                 
             }else{
                 objLoanProductInterReceivableTO.setInsuranceSanctionDt("");
             }
            objLoanProductInterReceivableTO.setInsuranceRate(CommonUtil.convertObjToDouble(getTxtInsuraceRate()));
            objLoanProductInterReceivableTO.setGracePeriodPenal(CommonUtil.convertObjToDouble(getTxtGracePeriodPenalInterest()));
            objLoanProductInterReceivableTO.setPenalIntRate(CommonUtil.convertObjToDouble(txtPenalInterRate));
            objLoanProductInterReceivableTO.setExpoLmtPrudentialamt(CommonUtil.convertObjToDouble(txtExposureLimit_Prud));
            objLoanProductInterReceivableTO.setExpoLmtPolicyamt(CommonUtil.convertObjToDouble(txtExposureLimit_Policy));
            objLoanProductInterReceivableTO.setExpoLmtPrudentialper(CommonUtil.convertObjToDouble(txtExposureLimit_Prud2));
            objLoanProductInterReceivableTO.setExpoLmtPolicyper(CommonUtil.convertObjToDouble(txtExposureLimit_Policy2));
            //            objLoanProductInterReceivableTO.setPeriodFreq(CommonUtil.convertObjToDouble(cbmPeridFreq.getKeyForSelected()));
            objLoanProductInterReceivableTO.setPrematureIntCalAmt(CommonUtil.convertObjToStr(cbmPrematureIntCalAmt.getKeyForSelected()));
//            if(cboPeriodFreq.length() > 0)
//                duration = ((String)cbmPeridFreq.getKeyForSelected());
//            double periodData = setCombo(duration);
//            resultData = periodData * (CommonUtil.convertObjToDouble(txtPreMatIntCalctPeriod).doubleValue());
//            objLoanProductInterReceivableTO.setPeriodFreq(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
//            added by nikhil
            objLoanProductInterReceivableTO.setPeriodFreq(CommonUtil.convertObjToDouble(txtPreMatIntCalctPeriod)); 
            objLoanProductInterReceivableTO.setPeriodIntCalcFreq(CommonUtil.convertObjToDouble(txtPreMatIntCollectPeriod));
            
            objLoanProductInterReceivableTO.setPrematurePeriod((String)getCbmPeridFreq().getKeyForSelected());
            objLoanProductInterReceivableTO.setPrematureIntCalcPeriod((String)getCbmPrematureIntCalcFreq().getKeyForSelected());
            
        }catch(NumberFormatException ne){
            //log.info("NumberFormatException in what u expect");
        }catch(Exception e){
            log.info("Error in setLoanProductInterReceivable()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductInterReceivableTO;
    }
    
    //----------CHARGES----------
    /* To set common data in the Transfer Object*/
    public LoanProductChargesTO setLoanProductCharges() {
        log.info("In setLoanProductCharges...");
        
        final LoanProductChargesTO objLoanProductChargesTO = new LoanProductChargesTO();
        try{
            objLoanProductChargesTO.setProdId(txtProductID);
            objLoanProductChargesTO.setAcClosingChrg(CommonUtil.convertObjToDouble(txtAccClosingCharges));
            objLoanProductChargesTO.setMiscServChrg(CommonUtil.convertObjToDouble(txtMiscSerCharges));
            
            if (getRdoStatCharges_Yes() == true) {
                objLoanProductChargesTO.setStatChrg(CommonUtil.convertObjToStr(YES));
            } else /*if (getRdoStatCharges_No() == true)*/ {
                objLoanProductChargesTO.setStatChrg(CommonUtil.convertObjToStr(NO));
            }
            
            objLoanProductChargesTO.setStatChrgRate(CommonUtil.convertObjToDouble(txtStatChargesRate));
            
            if (getRdoFolioChargesAppl_Yes() == true) {
                objLoanProductChargesTO.setFolioChrgAppl(CommonUtil.convertObjToStr(YES));
            } else /*if (getRdoFolioChargesAppl_No() == true)*/ {
                objLoanProductChargesTO.setFolioChrgAppl(CommonUtil.convertObjToStr(NO));
            }
            
            Date LsFolDt = DateUtil.getDateMMDDYYYY(tdtLastFolioChargesAppl);
            if(LsFolDt != null){
                Date lsfolDate = (Date)curDate.clone();
                lsfolDate.setDate(LsFolDt.getDate());
                lsfolDate.setMonth(LsFolDt.getMonth());
                lsfolDate.setYear(LsFolDt.getYear());
                //            objLoanProductChargesTO.setLastFolioChrgon(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastFolioChargesAppl)));
                objLoanProductChargesTO.setLastFolioChrgon(lsfolDate);
            }else{
                objLoanProductChargesTO.setLastFolioChrgon(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtLastFolioChargesAppl)));
            }
            
            objLoanProductChargesTO.setNoEntriesPerFolio(CommonUtil.convertObjToDouble(txtNoEntriesPerFolio));
            
            Date NxFolDt = DateUtil.getDateMMDDYYYY(tdtNextFolioDDate);
            if(NxFolDt != null){
                Date nxfolDate = (Date)curDate.clone();
                nxfolDate.setDate(NxFolDt.getDate());
                nxfolDate.setMonth(NxFolDt.getMonth());
                nxfolDate.setYear(NxFolDt.getYear());
                //            objLoanProductChargesTO.setNextFolioDuedate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextFolioDDate)));
                objLoanProductChargesTO.setNextFolioDuedate(nxfolDate);
            }else{
                objLoanProductChargesTO.setNextFolioDuedate(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtNextFolioDDate)));
            }
            
            objLoanProductChargesTO.setRatePerFolio(CommonUtil.convertObjToDouble(txtRatePerFolio));
            objLoanProductChargesTO.setFolioChrgApplfreq(CommonUtil.convertObjToDouble(cbmFolioChargesAppFreq.getKeyForSelected()));
            objLoanProductChargesTO.setToCollectFoliochrg(CommonUtil.convertObjToStr((String)cbmToCollectFolioCharges.getKeyForSelected()));
            
            if (getRdoToChargeOn_Man() == true) {
                objLoanProductChargesTO.setToCollectChrgOn(CommonUtil.convertObjToStr(MANUAL));
            }  else if (getRdoToChargeOn_Sys() == true) {
                objLoanProductChargesTO.setToCollectChrgOn(CommonUtil.convertObjToStr(SYSTEM));
            } else {
                objLoanProductChargesTO.setToCollectChrgOn(CommonUtil.convertObjToStr(BOTH));
            }
            objLoanProductChargesTO.setIncompFolioRoundoff(CommonUtil.convertObjToDouble(cbmIncompleteFolioRoundOffFreq.getKeyForSelected()));
            if(isRdoToChargeType_Credit()== true){
                objLoanProductChargesTO.setFolioChargeType("CREDIT");
            }else if(isRdoToChargeType_Debit()== true) {
                objLoanProductChargesTO.setFolioChargeType("DEBIT");
            }else{
                objLoanProductChargesTO.setFolioChargeType("BOTH");
            }
            if (getRdoProcessCharges_No() == true) {
                objLoanProductChargesTO.setProcChrg(CommonUtil.convertObjToStr(NO));
                
            } else /*if (getRdoProcessCharges_No() == true)*/ {
                objLoanProductChargesTO.setProcChrg(CommonUtil.convertObjToStr(YES));
            }
            
            if(getCboCharge_Limit2().equalsIgnoreCase(ABSOLUTE)){
                objLoanProductChargesTO.setProcChrgAmt(CommonUtil.convertObjToDouble(txtCharge_Limit2));
            }else if(getCboCharge_Limit2().equalsIgnoreCase(PERCENT)){
                objLoanProductChargesTO.setProcChrgPer(CommonUtil.convertObjToDouble(txtCharge_Limit2));
            }
            
            
            if (getRdoCommitmentCharge_No() == true) {
                objLoanProductChargesTO.setCommitChrg(CommonUtil.convertObjToStr(NO));
                
            } else /*if (getRdoCommitmentCharge_No() == true)*/ {
                objLoanProductChargesTO.setCommitChrg(CommonUtil.convertObjToStr(YES));
            }
            if (isChkCreditStampAdvance() == true) {
                objLoanProductChargesTO.setCreditStampAdvance(CommonUtil.convertObjToStr(YES));
                
            } else /*if (getRdoCommitmentCharge_No() == true)*/ {
                objLoanProductChargesTO.setCreditStampAdvance(CommonUtil.convertObjToStr(NO));
            }
             if (isChkCreditNoticeAdvance() == true) {
                objLoanProductChargesTO.setCreditNoticeAdvance(CommonUtil.convertObjToStr(YES));
                
            } else /*if (getRdoCommitmentCharge_No() == true)*/ {
                objLoanProductChargesTO.setCreditNoticeAdvance(CommonUtil.convertObjToStr(NO));
            }
            
            if(getCboCharge_Limit3().equalsIgnoreCase(ABSOLUTE)){
                objLoanProductChargesTO.setCommitChrgAmt(CommonUtil.convertObjToDouble(txtCharge_Limit3));
            }else if(getCboCharge_Limit3().equalsIgnoreCase(PERCENT)){
                objLoanProductChargesTO.setCommitChrgPer(CommonUtil.convertObjToDouble(txtCharge_Limit3));
            }
            
            
            
        }catch(NumberFormatException ne){
            //log.info("NumberFormatException in what u expect");
        }catch(Exception e){
            log.info("Error in setLoanProductCharges()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductChargesTO;
    }
    
    public ArrayList setLoanProductChargesTab() {
        log.info("In setLoanProductChargesTab...");
        // Testing...
        //        ArrayList data = tblChargeTab.getDataArrayList(); now not need clearing charge so we have use as notic charge
        ArrayList data=tblNoticeCharge.getDataArrayList();
        ArrayList charges = new ArrayList();
        chargeTabRow = new ArrayList();
        final int dataSize = data.size();
        for (int i=0;i<dataSize;i++){
            try{
                objLoanProductChargesTabTO = new LoanProductChargesTabTO();
                objLoanProductChargesTabTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
                
                charges = (ArrayList)data.get(i);
                //                objLoanProductChargesTabTO.setChqReturnChrgFrom(CommonUtil.convertObjToDouble(charges.get(0)));
                //                objLoanProductChargesTabTO.setChqReturnChrgTo(CommonUtil.convertObjToDouble(charges.get(1)));
                //                objLoanProductChargesTabTO.setChqReturnChrgRate(CommonUtil.convertObjToDouble(charges.get(2)));
                
                
                objLoanProductChargesTabTO.setNoticeType(CommonUtil.convertObjToStr(charges.get(0)));
                objLoanProductChargesTabTO.setIssueAfter(CommonUtil.convertObjToStr(charges.get(1)));
                objLoanProductChargesTabTO.setNoticeChargeAmt(CommonUtil.convertObjToDouble(charges.get(2)));
                objLoanProductChargesTabTO.setPostageAmt(CommonUtil.convertObjToDouble(charges.get(3)));
                
                chargeTabRow.add(objLoanProductChargesTabTO);
            }catch(Exception e){
                log.info("Error in setLoanProductChargesTab()");
                parseException.logException(e,true);
                //e.printStackTrace();
            }
        }// End of for()...
        
        return chargeTabRow;
    }
    //----------SPECIAL ITEMS----------
    public LoanProductSpeItemsTO setLoanProductSpeItems() {
        log.info("In setLoanProductSpeItems...");
        
        final LoanProductSpeItemsTO objLoanProductSpeItemsTO = new LoanProductSpeItemsTO();
        try{
            objLoanProductSpeItemsTO.setProdId(txtProductID);
            
            if (getRdoATMcardIssued_Yes() == true) {
                objLoanProductSpeItemsTO.setAtmCardIssued(YES);
            } else /*if (getRdoATMcardIssued_No() == true)*/ {
                objLoanProductSpeItemsTO.setAtmCardIssued(NO);
            }
            if (getRdoCreditCardIssued_Yes() == true) {
                objLoanProductSpeItemsTO.setCrCardIssued(YES);
            } else /*if (getRdoCreditCardIssued_No() == true)*/ {
                objLoanProductSpeItemsTO.setCrCardIssued(NO);
            }
            if (getRdoMobileBanlingClient_Yes() == true) {
                objLoanProductSpeItemsTO.setMobileBankClient(YES);
            } else /*if (getRdoMobileBanlingClient_No() == true)*/ {
                objLoanProductSpeItemsTO.setMobileBankClient(NO);
            }
            if (getRdoIsAnyBranBankingAllowed_Yes() == true) {
                objLoanProductSpeItemsTO.setBranchBankingAllowed(YES);
            } else /*if (getRdoIsAnyBranBankingAllowed_No() == true)*/ {
                objLoanProductSpeItemsTO.setBranchBankingAllowed(NO);
            }
        }catch(Exception e){
            log.info("Error in setLoanProductSpeItems()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductSpeItemsTO;
    }
    //----------ACCOUNT HEAD----------
    public LoanProductAccHeadTO setLoanProductAccHead() {
        log.info("In setLoanProductAccHead...");
        
        final LoanProductAccHeadTO objLoanProductAccHeadTO = new LoanProductAccHeadTO();
        try{
            objLoanProductAccHeadTO.setProdId(txtProductID);
            objLoanProductAccHeadTO.setPenalWaiveOff(txtPenalWaiveoffHead);
            objLoanProductAccHeadTO.setPrincipleWaveOff(txtPrincipleWaiveoffHead);
            objLoanProductAccHeadTO.setNoticeWaiveOff(txtNoticeChargeDebitHead);
            objLoanProductAccHeadTO.setAcClosingChrg(txtAccClosCharges);
            objLoanProductAccHeadTO.setMiscServChrg(txtMiscServCharges);
            objLoanProductAccHeadTO.setStatChrg(txtStatementCharges);
            objLoanProductAccHeadTO.setAcDebitInt(txtAccDebitInter);
            objLoanProductAccHeadTO.setPenalInt(txtPenalInter);
            objLoanProductAccHeadTO.setAcCreditInt(txtAccCreditInter);
            objLoanProductAccHeadTO.setExpiryInt(txtExpiryInter);
            objLoanProductAccHeadTO.setChqRetChrgOutward(txtCheReturnChargest_Out);
            objLoanProductAccHeadTO.setChqRetChrgInward(txtCheReturnChargest_In);
            objLoanProductAccHeadTO.setFolioChrgAc(txtFolioChargesAcc);
            objLoanProductAccHeadTO.setCommitmentChrg(txtCommitCharges);
            objLoanProductAccHeadTO.setProcChrg(txtProcessingCharges);
            objLoanProductAccHeadTO.setPostageCharges(txtPostageCharges);
            objLoanProductAccHeadTO.setNoticeCharges(txtNoticeCharges);
            objLoanProductAccHeadTO.setIntPayableAcHd(txtIntPayableAccount);
            
            objLoanProductAccHeadTO.setLegalCharges(txtLegalCharges);
            //            objLoanProductAccHeadTO.setMiscCharges(txtMiscCharges);
            objLoanProductAccHeadTO.setArbitraryCharges(txtArbitraryCharges);
            objLoanProductAccHeadTO.setInsuranceCharges(txtInsuranceCharges);
            objLoanProductAccHeadTO.setExecutionDecreeCharges(txtExecutionDecreeCharges);
            
            objLoanProductAccHeadTO.setArcCost(txtArcCostAccount);
            objLoanProductAccHeadTO.setArcExpense(txtArcExpenseAccount);
            objLoanProductAccHeadTO.setEaCost(txtEaCostAccount);
            objLoanProductAccHeadTO.setEaExpense(txtEaExpenseAccount);
            objLoanProductAccHeadTO.setEpCost(txtEpCostAccount);
            objLoanProductAccHeadTO.setEpExpense(txtEpExpenseAccount);
            objLoanProductAccHeadTO.setDebitIntDiscountAchd(txtDebitIntDiscount);
            objLoanProductAccHeadTO.setRebateInterest(txtRebateInterest);
            objLoanProductAccHeadTO.setStampAdvancesHead(txtStampAdvancesHead);
            objLoanProductAccHeadTO.setNoticeAdvancesHead(txtNoticeAdvancesHead);
            objLoanProductAccHeadTO.setAdvertisementHead(txtAdvertisementHead);
            objLoanProductAccHeadTO.setArcEpSuspenceHead(txtARCEPSuspenceHead);
            objLoanProductAccHeadTO.setOthrChrgsHead(txtOtherCharges);
            //added by rishad 23-04-2015
            objLoanProductAccHeadTO.setPostageWaiver(txtPostageWaiveoffHead);
            objLoanProductAccHeadTO.setLegalWaiver(txtLegalWaiveoffHead);
            objLoanProductAccHeadTO.setDecreeWaiver(txtDecreeWaiveoffHead);
            objLoanProductAccHeadTO.setArbitraryWaiver(txtArbitraryWaiveoffHead);
            objLoanProductAccHeadTO.setAdvertiseWaiver(txtAdvertiseWaiveoffHead);
            objLoanProductAccHeadTO.setInsurenceWaiver(txtInsurenceWaiveoffHead);
            objLoanProductAccHeadTO.setMiscellaneousWaiver(txtMiscellaneousWaiveoffHead);
            objLoanProductAccHeadTO.setArcWaiver(txtArcWaiveoffHead);
            objLoanProductAccHeadTO.setEpWaiver(txtEpWaiveoffHead);
            
            objLoanProductAccHeadTO.setOverDueWaiver(txtOverDueWaiverHead);
            objLoanProductAccHeadTO.setOverDueIntHead(txtOverdueIntHead);
            objLoanProductAccHeadTO.setRecoveryWaiver(txtRecoveryWaiverHead);
            objLoanProductAccHeadTO.setMeasurementWaiver(txtMeasurementWaiverHead);
            objLoanProductAccHeadTO.setRecoveryCharges(txtRecoveryChargeHead);
            objLoanProductAccHeadTO.setMeasurementCharges(txtMeasurementChargeHead);
            
            objLoanProductAccHeadTO.setKoleFieldexpense(txtKoleFieldExpenseHead);
            objLoanProductAccHeadTO.setKoleFieldExpenseWaiver(txtKoleFieldExpenseWaiverHead);
            objLoanProductAccHeadTO.setKoleFieldOperation(txtKoleFieldOperationHead);
            objLoanProductAccHeadTO.setKoleFieldOperationWaiver(txtKoleFieldOperationWaiverHead);
            
        }catch(Exception e){
            log.info("Error in setLoanProductAccHead()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductAccHeadTO;
    }
    //----------NON PERFORMING ASSETS----------
    public LoanProductNonPerAssetsTO setLoanProductNonPerAssets() {
        log.info("In setLoanProductNonPerAssets...");
        
        final LoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO = new LoanProductNonPerAssetsTO();
        try{
            objLoanProductNonPerAssetsTO.setProdId(txtProductID);
            
            if(cboMinPeriodsArrears.length() > 0){
                duration = ((String)cbmMinPeriodsArrears.getKeyForSelected());
                periodData = setCombo(duration);
                resultData = periodData * (Double.parseDouble(txtMinPeriodsArrears));
                //objLoanProductNonPerAssetsTO.setMinPeriodArrears( new Double(resultData));
                objLoanProductNonPerAssetsTO.setMinPeriodArrears(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            }
            if(cboPeriodTranSStanAssets.length() > 0){
                duration = ((String)cbmPeriodTranSStanAssets.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(CommonUtil.convertObjToStr(txtPeriodTranSStanAssets)));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTranSStanAssets).doubleValue());
            //objLoanProductNonPerAssetsTO.setPeriodTransSubstandard( new Double(resultData));
            objLoanProductNonPerAssetsTO.setPeriodTransSubstandard(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionSubstandard(CommonUtil.convertObjToDouble(txtProvisionsStanAssets));
            
            if(cboPeriodTransDoubtfulAssets1.length() > 0){
                duration = ((String)cbmPeriodTransDoubtfulAssets1.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(CommonUtil.convertObjToStr(txtPeriodTransDoubtfulAssets)));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransDoubtfulAssets1).doubleValue());
            //objLoanProductNonPerAssetsTO.setPeriodTransDoubtful( new Double(resultData));
            objLoanProductNonPerAssetsTO.setPeriodTransDoubtful(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionDoubtful(CommonUtil.convertObjToDouble(txtProvisionDoubtfulAssets1));
            
            if(cboPeriodTransDoubtfulAssets2.length() > 0){
                duration = ((String)cbmPeriodTransDoubtfulAssets2.getKeyForSelected());
            }
            periodData = setCombo(duration);
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransDoubtfulAssets2).doubleValue());
            objLoanProductNonPerAssetsTO.setPeriodTransDoubtful2(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionDoubtful2(CommonUtil.convertObjToDouble(txtProvisionDoubtfulAssets2));
            
            if(cboPeriodTransDoubtfulAssets3.length() > 0){
                duration = ((String)cbmPeriodTransDoubtfulAssets3.getKeyForSelected());
            }
            periodData = setCombo(duration);
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransDoubtfulAssets3).doubleValue());
            objLoanProductNonPerAssetsTO.setPeriodTransDoubtful3(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductNonPerAssetsTO.setProvisionDoubtful3(CommonUtil.convertObjToDouble(txtProvisionDoubtfulAssets3));
            
            if(cboPeriodTransLossAssets.length() > 0){
                duration = ((String)cbmPeriodTransLossAssets.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtPeriodTransLossAssets));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodTransLossAssets).doubleValue());
            //objLoanProductNonPerAssetsTO.setPeriodTransLoss( new Double(resultData));
            objLoanProductNonPerAssetsTO.setPeriodTransLoss(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
            if(cboPeriodAfterWhichTransNPerformingAssets.length() > 0){
                duration = ((String)cbmPeriodAfterWhichTransNPerformingAssets.getKeyForSelected());
                periodData = setCombo(duration);
                //                resultData = periodData * (Double.parseDouble(txtPeriodAfterWhichTransNPerformingAssets));
                resultData = periodData * (CommonUtil.convertObjToDouble(txtPeriodAfterWhichTransNPerformingAssets).doubleValue());
                //objLoanProductNonPerAssetsTO.setPeriodTransNoperforming( new Double(resultData));
                objLoanProductNonPerAssetsTO.setPeriodTransNoperforming(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            }
            
            objLoanProductNonPerAssetsTO.setProvisionStdAssets(CommonUtil.convertObjToDouble(txtProvisionStandardAssetss));
            objLoanProductNonPerAssetsTO.setProvisionLoseAssets(CommonUtil.convertObjToDouble(txtProvisionLossAssets));
            
        }catch(Exception e){
            log.info("Error in setLoanProductNonPerAssets()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductNonPerAssetsTO;
    }
    //----------INTEREST CALCULATION----------
    public LoanProductInterCalcTO setLoanProductInterCalc() {
        log.info("In setLoanProductInterCalc...");
        
        final LoanProductInterCalcTO objLoanProductInterCalcTO = new LoanProductInterCalcTO();
        try{
            objLoanProductInterCalcTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductInterCalcTO.setCalcType(CommonUtil.convertObjToStr((String)cbmcalcType.getKeyForSelected()));
            objLoanProductInterCalcTO.setChkFixed(chkFixed);
            objLoanProductInterCalcTO.setFixedMargin(CommonUtil.convertObjToDouble(txtFixedMargin));
            
            if(cboMinPeriod.length() > 0){
                duration = ((String)cbmMinPeriod.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtMinPeriod));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtMinPeriod).doubleValue());
            //objLoanProductInterCalcTO.setMinPeriod( new Double(resultData));
            objLoanProductInterCalcTO.setMinPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
            if(cboMaxPeriod.length() > 0){
                duration = ((String)cbmMaxPeriod.getKeyForSelected());
                objLoanProductInterCalcTO.setMaxPeriodChar(CommonUtil.convertObjToStr(cbmMaxPeriod.getKeyForSelected()));
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtMaxPeriod));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtMaxPeriod).doubleValue());
            objLoanProductInterCalcTO.setMaxPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            
            if(cboReviewPeriod.length() > 0){
                duration = ((String)cbmReviewPeriod.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtMaxPeriod));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtReviewPeriod).doubleValue());
            objLoanProductInterCalcTO.setReviewPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            if(cboDepAmtLoanMaturingPeriod.length() > 0){
                duration = ((String)cbmDepAmtMaturingPeriod.getKeyForSelected());
            }
            periodData = setCombo(duration);
            //            resultData = periodData * (Double.parseDouble(txtMaxPeriod));
            resultData = periodData * (CommonUtil.convertObjToDouble(txtDepAmtLoanMaturingPeriod).doubleValue());
            objLoanProductInterCalcTO.setDepAmtMaturingPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
            objLoanProductInterCalcTO.setDepositRoundOff(CommonUtil.convertObjToStr(cbmDepositRoundOff.getKeyForSelected()));
            objLoanProductInterCalcTO.setEligibleDepForLoanAmt(CommonUtil.convertObjToDouble(txtElgLoanAmt));
            objLoanProductInterCalcTO.setDepAmtMaturing(CommonUtil.convertObjToDouble(getTxtDepAmtMaturing()));
            objLoanProductInterCalcTO.setMinAmtLoan(CommonUtil.convertObjToDouble(txtMinAmtLoan));
            objLoanProductInterCalcTO.setMaxAmtLoan(CommonUtil.convertObjToDouble(txtMaxAmtLoan));
            objLoanProductInterCalcTO.setApplInterest(CommonUtil.convertObjToDouble(txtApplicableInter));
            objLoanProductInterCalcTO.setMinIntDebit(CommonUtil.convertObjToDouble(txtMinInterDebited));
            objLoanProductInterCalcTO.setChkFixed(chkFixed);
            objLoanProductInterCalcTO.setFixedMargin(CommonUtil.convertObjToDouble(txtFixedMargin));
            if (getRdoSubsidy_Yes() == true) {
                objLoanProductInterCalcTO.setSubsidy(YES);
            } else /*if (getRdoSubsidy_No() == true)*/ {
                objLoanProductInterCalcTO.setSubsidy(NO);
            }
            
            objLoanProductInterCalcTO.setLoanPeriodsMultiples(CommonUtil.convertObjToDouble((String)cbmLoanPeriodMul.getKeyForSelected()));
        }catch(Exception e){
            log.info("Error in setLoanProductInterCalc()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductInterCalcTO;
    }
    
    
    public LoanProductClassificationsTO setLoanProductClassifications() {
        log.info("In setLoanProductClassifications...");
        
        final LoanProductClassificationsTO objLoanProductClassificationsTO = new LoanProductClassificationsTO();
        try{
            objLoanProductClassificationsTO.setProdId(CommonUtil.convertObjToStr(txtProductID));
            objLoanProductClassificationsTO.setCommodityCode((String)cbmCommodityCode.getKeyForSelected());
            objLoanProductClassificationsTO.setFacilityType((String)cbmTypeFacility.getKeyForSelected());
            objLoanProductClassificationsTO.setPurposeCode((String)cbmPurposeCode.getKeyForSelected());
            objLoanProductClassificationsTO.setIndustryCode((String)cbmIndusCode.getKeyForSelected());
            objLoanProductClassificationsTO.setTwentyCode((String)cbm20Code.getKeyForSelected());
            objLoanProductClassificationsTO.setGovtSchemeCode((String)cbmGovtSchemeCode.getKeyForSelected());
            
            if (getChkECGC()){
                objLoanProductClassificationsTO.setEcgc(YES);
            }else{
                objLoanProductClassificationsTO.setEcgc(NO);
            }
            
            objLoanProductClassificationsTO.setGuaranteeCoverCode((String)cbmGuaranteeCoverCode.getKeyForSelected());
            objLoanProductClassificationsTO.setHealthCode((String)cbmHealthCode.getKeyForSelected());
            objLoanProductClassificationsTO.setSectorCode((String)cbmSectorCode.getKeyForSelected());
            objLoanProductClassificationsTO.setRefinancingInstitution((String)cbmRefinancingInsti.getKeyForSelected());
            
            if (getChkDirectFinance()){
                objLoanProductClassificationsTO.setDirectFinance(YES);
            }else{
                objLoanProductClassificationsTO.setDirectFinance(NO);
            }
            
            if (getChkQIS()){
                objLoanProductClassificationsTO.setQis(YES);
            }else{
                objLoanProductClassificationsTO.setQis(NO);
            }
            if (loanProductClassificationsTOList!=null)
                objLoanProductClassificationsTO.setStatus(CommonConstants.STATUS_MODIFIED);
            else
                objLoanProductClassificationsTO.setStatus(CommonConstants.STATUS_CREATED);
            
            //            objLoanProductClassificationsTO.setStatusBy (getTxtStatusBy());
            //            objLoanProductClassificationsTO.setStatusDt (DateUtil.getDateMMDDYYYY (getTxtStatusDt()));
            
            objLoanProductClassificationsTO.setSecurityDetails((String)cbmSecurityDeails.getKeyForSelected());
            
        }catch(Exception e){
            log.info("Error in LoanProductClassificationsTO()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return objLoanProductClassificationsTO;
    }
    
    
    
    
    //=============================================================================
    //==========INSERT TABLE OF CHARGES...==========
    void setTblCharges(EnhancedTableModel tblChargeTab){
        log.info("In setTblCharges...");
        
        this.tblChargeTab = tblChargeTab;
        setChanged();
    }
    
    EnhancedTableModel getTblCharges(){
        return this.tblChargeTab;
    }
    
    //==========INSERT TABLE OF DOCUMENTS...==========
    void setTblDocuments(EnhancedTableModel tblDocuments){
        log.info("In setTblDocuments...");
        
        this.tblDocuments = tblDocuments;
        setChanged();
    }
    
    EnhancedTableModel getTblDocuments(){
        return this.tblDocuments;
    }
    
    public int addChargesTab(){
        log.info("In addChargesTab...");
        
        int option = -1;
        try{
            chargeTabRow = new ArrayList();
            chargeTabRow.add(txtRangeFrom);
            chargeTabRow.add(txtRangeTo);
            chargeTabRow.add(txtRateAmt);
            ArrayList data = tblChargeTab.getDataArrayList();
            tblChargeTab.setDataArrayList(data,chargeTabTitle);
            final int dataSize = data.size();
            boolean exist = false;
            ArrayList charges = new ArrayList();
            
            for (int i=0;i<dataSize;i++){
                // To check for the Amount in each row...
                // Whether it is already entered or not...
                charges = (ArrayList)data.get(i);
                if( (Double.parseDouble(txtRangeFrom) >= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(0))))
                && (Double.parseDouble(txtRangeFrom) <= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(1))))){
                    
                    exist = true;
                }if(!exist){
                    if( (Double.parseDouble(txtRangeTo) >= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(0))))
                    && (Double.parseDouble(txtRangeTo) <= Double.parseDouble(CommonUtil.convertObjToStr(charges.get(1))))){
                        exist = true;
                    }
                }if(!exist && (Double.parseDouble(txtRangeTo) < Double.parseDouble(CommonUtil.convertObjToStr(charges.get(1))))){
                    double difference = Double.parseDouble(txtRangeTo) - Double.parseDouble(txtRangeFrom) - Double.parseDouble(CommonUtil.convertObjToStr(charges.get(0)));
                    if(difference >=0){
                        exist = true;
                    }
                }
                
                if(exist){
                    String[] options = {objLoanProductRB.getString("cDialogYes"),objLoanProductRB.getString("cDialogNo"),objLoanProductRB.getString("cDialogCancel")};
                    option = COptionPane.showOptionDialog(null, objLoanProductRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    if (option == 0){
                        // option selected is Yes
                        updateChargesTab(i);
                        resetTab();
                    }else if( option == 1){
                        resetTab();
                    }
                    break;
                }
            }
            if (!exist){
                //The condition that the Entered Code is not in the table
                insertChargesTab();
            }
            if (option == 2){
                //The option selected is Cancel
            }
            setChanged();
            ttNotifyObservers();
            chargeTabRow = null;
        }catch(Exception e){
            log.info("The error in addChargesTab()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        return option;
    }
    
    /* */
    
    // Insert for the Table in Charges...
    private void insertChargesTab() throws Exception{
        log.info("In insertChargesTab...");
        
        tblChargeTab.insertRow(0,chargeTabRow);
    }
    
    // Update for the Table in Charges
    private void updateChargesTab(int row) throws Exception{
        log.info("In updateChargesTab...");
        
        tblChargeTab.setValueAt(txtRateAmt, row, 2);
        setChanged();
        ttNotifyObservers();
    }
    
    // Delete for the Table in Charges...
    public void deleteChargesTab(){
        log.info("In deleteChargesTab...");
        
        try{
            ArrayList data = tblChargeTab.getDataArrayList();
            final int dataSize = data.size();
            for (int i=0;i<dataSize;i++){
                if ( (((ArrayList)data.get(i)).get(0)).equals(txtRangeFrom)
                && (((ArrayList)data.get(i)).get(1)).equals(txtRangeTo)){
                    tblChargeTab.removeRow(i);
                    break;
                }
            }
            setChanged();
            ttNotifyObservers();
        }catch(Exception e){
            log.info("The error in deleteChargesTab()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
    }
    
    // To get the Date from the Table into the Amount Combo box and Rate...
    public void populateChargesTab(int row){
        log.info("In populateChargesTab...");
        
        ArrayList charges = (ArrayList)tblChargeTab.getDataArrayList().get(row);
        setTxtRangeFrom((String)charges.get(0));
        setTxtRangeTo((String)charges.get(1));
        setTxtRateAmt((String)charges.get(2));
        setChanged();
        ttNotifyObservers();
    }
    public void populateNoticeCharge(int row){
        ArrayList NoticeCharge=(ArrayList)tblNoticeCharge.getDataArrayList().get(row);
        System.out.println("populateNoticeCharge####"+NoticeCharge);
        setCboNoticeType(CommonUtil.convertObjToStr(NoticeCharge.get(0)));
        setCboIssueAfter(CommonUtil.convertObjToStr(NoticeCharge.get(1)));
        setTxtNoticeChargeAmt(CommonUtil.convertObjToStr(NoticeCharge.get(2)));
        setTxtPostageChargeAmt(CommonUtil.convertObjToStr(NoticeCharge.get(3)));
        setChanged();
        ttNotifyObservers();
    }
    public void resetTab(){
        log.info("In resetTab...");
        
        setTxtRangeFrom("");
        setTxtRangeTo("");
        setTxtRateAmt("");
        
        setChanged();
        ttNotifyObservers();
    }
    /**
     * To reset the Document Fields
     */
    public void resetDocuments() {
        resetDocumentsTextFields();
        documentsTO = null;
        deletedDocumentsTO = null;
        mainDocumentsTO = null;
        entireRows = null;
        tblDocuments.setDataArrayList(null,documentsTabTitle);
        docSI_No = 1;// Seral No for Documents table
        deletedDocSI_No = 1;// Seral No for Deleted Documents
        
    }
    
    public void resetNoticeTypeCharges(){
        resetNoticeChargeValues();
        NoticeTypeTO=null;
        entireNoticeTypeRow=null;
        tblNoticeCharge.setDataArrayList(null,noticeChargeTabTitle);
        notice_Charge_No=1;
    }
    
    
    
    
    /**
     * To reset the Document Text Fields
     */
    public void resetDocumentsTextFields() {
        setTxtDocumentDesc("");
        setTxtDocumentNo("");
        setCboDocumentType("");
        setDocumentSerialNo("");
        setDocumentStatus("");
        ttNotifyObservers();
    }
    public void resetNoticeChargeValues(){
        setCboIssueAfter("");
        setCboNoticeType("");
        setTxtPostageChargeAmt("");
        setTxtNoticeChargeAmt("");
        ttNotifyObservers();
    }
    //-------------------------------------------------------------------------------------------
    /** To perform the appropriate operation */
    public void doAction() {
        log.info("In doAction...");
        
        try {
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
            }
            else
                log.info("In doAction()-->actionType is null:" );
        } catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in doAction():");
            parseException.logException(e,true);
        }
    }
    //Added By Nidhin
    protected boolean isPricipalCreditToFirst() {
        boolean check = false;
        if (isRdoIsCreditAllowed_Yes() || isRdoIsCreditAllowed_No()){
                if(lstSelectedTransaction.getElementAt(1).equals("PRINCIPAL") || lstSelectedTransaction.getElementAt(2).equals("PRINCIPAL")) {
            ClientUtil.displayAlert("Please Rearrange Charge Hierachy " + "\n" + "");
            check = true;
            }
        else{
        if (isRdoIsCreditAllowed_Yes()) {
            if (!lstSelectedTransaction.getElementAt(0).equals("PRINCIPAL")) {
                ClientUtil.displayAlert("You have choosen credit principle YES "+"\n"+
                "Please change Hierachy PRINCIPAL first");
                check = true;
            }
        } else if(isRdoIsCreditAllowed_No() && lstSelectedTransaction.getElementAt(0).equals("PRINCIPAL")){
            ClientUtil.displayAlert("You have choosen credit principle NO "+"\n"+
                "Please change Hierachy PRINCIPAL Last");
            check = true;
        }
        }
        if(isRdoIsCreditAllowed_No()){
        updateAddedPrincipalFirst(getTxtProductID());
        }
        }
        return check;
    }
    protected void updateAddedPrincipalFirst(String prodId) {
        HashMap wheremap = new HashMap();
        wheremap.put("value", prodId);
        List list = (List) ClientUtil.executeQuery("getSelectLoanProductAccountParamTO", wheremap);
        if (list != null && list.size() > 0) {
            LoanProductAccountParamTO loanProductAccountParamTO = (LoanProductAccountParamTO) list.get(0);
            if (loanProductAccountParamTO.getIsCreditAllowedToPricipal() == null || loanProductAccountParamTO.getIsCreditAllowedToPricipal().equals("")) {
                wheremap.put("PROD_ID", CommonUtil.convertObjToStr(prodId));
                loanProductAccountParamTO.setIsCreditAllowedToPricipal("N");
                wheremap.put("updateLoanProductAccountParamTO", loanProductAccountParamTO);
                ClientUtil.execute("updateLoanProductAccountParamTO", wheremap);
            }
        }
    }

    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("In doActionPerform...");
        final LoanProductAccountTO objLoanProductAccountTO = setLoanProductAccount();
        final LoanProductGroupLoanTO objLoanProductGroupLoanTO = setLoanProductGroupLoan();
        final LoanProductAccountParamTO objLoanProductAccountParamTO = setLoanProductAccountParam();
        final LoanProductInterReceivableTO objLoanProductInterReceivableTO = setLoanProductInterReceivable();
        final LoanProductChargesTO objLoanProductChargesTO = setLoanProductCharges();
        final ArrayList arrayLoanProductChargesTabTO = setLoanProductChargesTab();
        final LoanProductSpeItemsTO objLoanProductSpeItemsTO = setLoanProductSpeItems();
        final LoanProductAccHeadTO objLoanProductAccHeadTO = setLoanProductAccHead();
        final LoanProductNonPerAssetsTO objLoanProductNonPerAssetsTO = setLoanProductNonPerAssets();
        final LoanProductInterCalcTO objLoanProductInterCalcTO = setLoanProductInterCalc();
        final LoanProductClassificationsTO objLoanProductClassifications = setLoanProductClassifications();
        final LinkedHashMap availableListMap=setSelectedAvailableDeposit();
        final LinkedHashMap availableTransMap=setSelectedAvailableTransaction();
        final LinkedHashMap availableOTSTransMap=setSelectedAvailableTransaction_OTS();
        final LoanProductSubsidyInterestRebateTO  objLoanProductSubsidyInterestRebateTO=setLoanProductSubsidyInterestRebateTO();
        
        objLoanProductAccountTO.setCommand(getCommand());
        // Documents
        if (mainDocumentsTO == null)
            mainDocumentsTO = new LinkedHashMap();
        if(getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (deletedDocumentsTO != null) {
                mainDocumentsTO.put(STATUS_WITH_DELETE, deletedDocumentsTO);
            }
        }
        mainDocumentsTO.put(STATUS_WITHOUT_DELETE, documentsTO);
        
        final HashMap data = new HashMap();
        data.put("LoanProductAccountTO",objLoanProductAccountTO);
        data.put("LoanProductAccountParamTO",objLoanProductAccountParamTO);
        data.put("LoanProductInterReceivableTO", objLoanProductInterReceivableTO);
        data.put("LoanProductChargesTO", objLoanProductChargesTO);
        data.put("LoanProductChargesTabTO", arrayLoanProductChargesTabTO);
        data.put("LoanProductSpeItemsTO",objLoanProductSpeItemsTO);
        data.put("LoanProductAccHeadTO",objLoanProductAccHeadTO);
        System.out.println("objLoanProductAccHeadTO"+objLoanProductAccHeadTO+data);
        data.put("LoanProductNonPerAssetsTO",objLoanProductNonPerAssetsTO);
        data.put("LoanProductInterCalcTO",objLoanProductInterCalcTO);
        data.put("LoanProductDocumentsTO",mainDocumentsTO);
        data.put("MODE",getCommand());// To Maintain the Status CREATED, MODIFIED, DELETED
        
        data.put("LoanProductClassificationsTO",objLoanProductClassifications);
        data.put("LoanAgainstAvailableDeposits",availableListMap);
        data.put("AppropriateTransaction",availableTransMap);
        data.put("AppropriateTransaction_OTS",availableOTSTransMap);
        data.put("LoanProductSubsidyInterestRebateTO",objLoanProductSubsidyInterestRebateTO);
        data.put("LoanProductGroupLoanTO",objLoanProductGroupLoanTO);
        
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        int countRec=0;
        HashMap proxyResultMap=null;
        System.out.println("getActionType()"+getActionType());
        if(getActionType() == 3){
            HashMap hash=new HashMap();
            hash.put("PROD_ID",objLoanProductAccountTO.getProdId());
            
            List lst=ClientUtil.executeQuery("getSelectLoanProductCountRecord", hash);
            hash=(HashMap)lst.get(0);
            countRec=CommonUtil.convertObjToInt(hash.get("COUNTS"));
        }
        if(countRec !=0)
            ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
        else
            proxyResultMap = proxy.execute(data,operationMap);
        log.info("doActionPerform is over...");
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        //        resetForm();
    }
    
    public void createHead(HashMap map) throws Exception{
         HashMap rerurnMap = proxy.execute(map,operationMap);
    }
    private String getCommand() throws Exception{
        log.info("In getCommand...");
        
        String command = null;
        switch (actionType) {
            case ClientConstants.ACTIONTYPE_NEW:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_COPY:
                command = CommonConstants.TOSTATUS_INSERT;
                break;
            case ClientConstants.ACTIONTYPE_EDIT:
                command = CommonConstants.TOSTATUS_UPDATE;
                break;
            case ClientConstants.ACTIONTYPE_DELETE:
                command = CommonConstants.TOSTATUS_DELETE;
                break;
            default:
        }
        return command;
    }
    public LinkedHashMap setSelectedAvailableDeposit(){
        HashMap  singleMap=new HashMap();
        HashMap totalMap=new HashMap();
        LinkedHashMap finalMap=new LinkedHashMap();
        String singleElement=null;
        
        //EXISTS RECORD FOR UPDATION PURPOSE
        if(existDepositMap !=null  && existDepositMap.size()>0){
            java.util.Set keySet=existDepositMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
        
        for(int i=0;i<existDepositMap.size();i++){
            singleMap=new HashMap();
            singleMap=(HashMap)existDepositMap.get(objKeySet[i]);
            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
            singleMap.put("PROD_ID",getTxtProductID());
            //                singleMap.put("SELECT_DEPOSITS",singleElement);
            totalMap.put(String.valueOf(i), singleMap);
        }
          finalMap.put("EXIST_RECORD",totalMap);
	}
          
        //NEW RECORD FOR INSERT PURPOSE
        if(newDepositMap !=null  && newDepositMap.size()>0){
            java.util.Set keySet=newDepositMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
        totalMap=new HashMap();
        for(int i=0;i<newDepositMap.size();i++){
            singleMap=new HashMap();
            singleMap=(HashMap)newDepositMap.get(objKeySet[i]);
            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
            singleMap.put("PROD_ID",getTxtProductID());
            //                singleMap.put("SELECT_DEPOSITS",singleElement);
            totalMap.put(String.valueOf(i), singleMap);
        }
          finalMap.put("NEW_RECORD",totalMap);
}
          
          //DELETE RECORD FOR INSERT PURPOSE
        if(deleteDepositMap !=null  && deleteDepositMap.size()>0){
            java.util.Set keySet=deleteDepositMap.keySet();
            Object objKeySet[]=(Object[])keySet.toArray();
          totalMap=new HashMap();
        for(int i=0;i<deleteDepositMap.size();i++){
            singleMap=new HashMap();
            singleMap=(HashMap)deleteDepositMap.get(objKeySet[i]);
            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
            singleMap.put("PROD_ID",getTxtProductID());
            //                singleMap.put("SELECT_DEPOSITS",singleElement);
            totalMap.put(String.valueOf(i), singleMap);
        }
          finalMap.put("DELETE_RECORD",totalMap);
	}
          
       
        System.out.println("finalmap###"+finalMap);
        return finalMap;
    }
    
    public LinkedHashMap setSelectedAvailableTransaction(){
        HashMap  singleMap=new HashMap();
        HashMap totalMap=new HashMap();
        LinkedHashMap finalMap=new LinkedHashMap();
        String singleElement=null;
        
        //EXISTS RECORD FOR UPDATION PURPOSE
//        if(existDepositMap !=null  && existDepositMap.size()>0){
//            java.util.Set keySet=existDepositMap.keySet();
//            Object objKeySet[]=(Object[])keySet.toArray();
//        
//        for(int i=0;i<existDepositMap.size();i++){
//            singleMap=new HashMap();
//            singleMap=(HashMap)existDepositMap.get(objKeySet[i]);
//            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
//            singleMap.put("PROD_ID",getTxtProductID());
//            //                singleMap.put("SELECT_DEPOSITS",singleElement);
//            totalMap.put(String.valueOf(i), singleMap);
//        }
//          finalMap.put("EXIST_RECORD",totalMap);
//	}
          if(lstSelectedTransaction !=null && lstSelectedTransaction.size()>0){
              singleMap=new HashMap();
              for(int i=0;i<lstSelectedTransaction.size();i++){
                  if(i==0)
                     singleMap.put("1_PRIORITY",lstSelectedTransaction.getElementAt(i)); 
                   if(i==1)
                     singleMap.put("2_PRIORITY",lstSelectedTransaction.getElementAt(i)); 
                   if(i==2)
                     singleMap.put("3_PRIORITY",lstSelectedTransaction.getElementAt(i)); 
                   if(i==3)
                     singleMap.put("4_PRIORITY",lstSelectedTransaction.getElementAt(i)); 
              }
              singleMap.put("PROD_ID",getTxtProductID());
               finalMap.put("NEW_RECORD",singleMap);
                System.out.println("finalMap$$$#"+finalMap);
          }
        //NEW RECORD FOR INSERT PURPOSE
//        if(newTransactionMap !=null  && newTransactionMap.size()>0){
//            java.util.Set keySet=newTransactionMap.keySet();
//            Object objKeySet[]=(Object[])keySet.toArray();
//        totalMap=new HashMap();
//        for(int i=0;i<newTransactionMap.size();i++){
//            singleMap=new HashMap();
//            singleMap=(HashMap)newTransactionMap.get(objKeySet[i]);
//            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
//            singleMap.put("PROD_ID",getTxtProductID());
//            //                singleMap.put("SELECT_DEPOSITS",singleElement);
//            totalMap.put(String.valueOf(i), singleMap);
//        }
//          finalMap.put("NEW_RECORD",totalMap);
//          System.out.println("finalMap$$$#"+finalMap);
//}
          
          //DELETE RECORD FOR INSERT PURPOSE
//        if(deleteDepositMap !=null  && deleteDepositMap.size()>0){
//            java.util.Set keySet=deleteDepositMap.keySet();
//            Object objKeySet[]=(Object[])keySet.toArray();
//          totalMap=new HashMap();
//        for(int i=0;i<deleteDepositMap.size();i++){
//            singleMap=new HashMap();
//            singleMap=(HashMap)deleteDepositMap.get(objKeySet[i]);
//            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
//            singleMap.put("PROD_ID",getTxtProductID());
//            //                singleMap.put("SELECT_DEPOSITS",singleElement);
//            totalMap.put(String.valueOf(i), singleMap);
//        }
//          finalMap.put("DELETE_RECORD",totalMap);
//	}
          
       
        System.out.println("finalmap###"+finalMap);
        return finalMap;
    }
    
    public LinkedHashMap setSelectedAvailableTransaction_OTS(){
        HashMap  singleMap=new HashMap();
        HashMap totalMap=new HashMap();
        LinkedHashMap finalMap=new LinkedHashMap();
        String singleElement=null;
        
        //EXISTS RECORD FOR UPDATION PURPOSE
        //        if(existDepositMap !=null  && existDepositMap.size()>0){
        //            java.util.Set keySet=existDepositMap.keySet();
        //            Object objKeySet[]=(Object[])keySet.toArray();
        //
        //        for(int i=0;i<existDepositMap.size();i++){
        //            singleMap=new HashMap();
        //            singleMap=(HashMap)existDepositMap.get(objKeySet[i]);
        //            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
        //            singleMap.put("PROD_ID",getTxtProductID());
        //            //                singleMap.put("SELECT_DEPOSITS",singleElement);
        //            totalMap.put(String.valueOf(i), singleMap);
        //        }
        //          finalMap.put("EXIST_RECORD",totalMap);
        //	}
        if(lstSelectedTransaction_OTS !=null && lstSelectedTransaction_OTS.size()>0){
            singleMap=new HashMap();
            for(int i=0;i<lstSelectedTransaction_OTS.size();i++){
                if(i==0)
                    singleMap.put("1_PRIORITY",lstSelectedTransaction_OTS.getElementAt(i));
                if(i==1)
                    singleMap.put("2_PRIORITY",lstSelectedTransaction_OTS.getElementAt(i));
                if(i==2)
                    singleMap.put("3_PRIORITY",lstSelectedTransaction_OTS.getElementAt(i));
                if(i==3)
                    singleMap.put("4_PRIORITY",lstSelectedTransaction_OTS.getElementAt(i));
            }
            singleMap.put("PROD_ID",getTxtProductID());
            finalMap.put("NEW_RECORD",singleMap);
            System.out.println("finalMap$$$#"+finalMap);
        }
        //NEW RECORD FOR INSERT PURPOSE
        //        if(newTransactionMap !=null  && newTransactionMap.size()>0){
        //            java.util.Set keySet=newTransactionMap.keySet();
        //            Object objKeySet[]=(Object[])keySet.toArray();
        //        totalMap=new HashMap();
        //        for(int i=0;i<newTransactionMap.size();i++){
        //            singleMap=new HashMap();
        //            singleMap=(HashMap)newTransactionMap.get(objKeySet[i]);
        //            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
        //            singleMap.put("PROD_ID",getTxtProductID());
        //            //                singleMap.put("SELECT_DEPOSITS",singleElement);
        //            totalMap.put(String.valueOf(i), singleMap);
        //        }
        //          finalMap.put("NEW_RECORD",totalMap);
        //          System.out.println("finalMap$$$#"+finalMap);
        //}
        
        //DELETE RECORD FOR INSERT PURPOSE
        //        if(deleteDepositMap !=null  && deleteDepositMap.size()>0){
        //            java.util.Set keySet=deleteDepositMap.keySet();
        //            Object objKeySet[]=(Object[])keySet.toArray();
        //          totalMap=new HashMap();
        //        for(int i=0;i<deleteDepositMap.size();i++){
        //            singleMap=new HashMap();
        //            singleMap=(HashMap)deleteDepositMap.get(objKeySet[i]);
        //            //                singleElement=CommonUtil.convertObjToStr(lstSelectedDeposits.getElementAt(i));
        //            singleMap.put("PROD_ID",getTxtProductID());
        //            //                singleMap.put("SELECT_DEPOSITS",singleElement);
        //            totalMap.put(String.valueOf(i), singleMap);
        //        }
        //          finalMap.put("DELETE_RECORD",totalMap);
        //	}
        
        
        System.out.println("finalmap###"+finalMap);
        return finalMap;
    }
    
    //To Reset all the fields in UI, Called from UI...
    public void resetForm(){
        //----------ACCOUNT----------        
        setTxtProductID("");
        setCboIntCalcDay("");
        setCboIntCalcMonth("");
        setCboIfHoliday("");
        setTxtProductDesc("");
        setCboOperatesLike("");
        setCboRepaymentFreq("");  //Added by nithya
        setCboRepaymentType("");  // Added by nithya
        getCbmProdCategory().setKeyForSelected("");
        //        setCboProdCurrency("");
        setTxtAccHead("");
        setCboPeriodFreq("");
        setCboPrematureIntCalcFreq("");
        setTxtNumPatternFollowed("");
        setTxtNumPatternFollowedSuffix("");
        setTxtLastAccNum("");
        setTxtPenalWaiveoffHead("");
        setTxtSuspenseCreditAchd("");
        setTxtSuspenseDebitAchd("");
        setTxtPrincipleWaiveoffHead("");
        setRdoIsLimitDefnAllowed_Yes(false);
        setRdoIsLimitDefnAllowed_No(false);
        setRdoIsInterestFirst_No(false);
        setRdoIsInterestFirst_Yes(false);
        setRdoIsInterestDue_No(false);//KD-3750
        setRdoIsInterestDue_Yes(false);
        setRdoIsCreditAllowed_Yes(false);
        setRdoIsCreditAllowed_No(false);
        setRdoDisbAftMoraPerd_No(false);
        setRdoDisbAftMoraPerd_Yes(false);
        setRdoAuctionAmt_Yes(false);
        setRdoAuctionAmt_No(false);
        setRdoIsStaffAccOpened_Yes(false);
        setRdoIsStaffAccOpened_No(false);
        setRdoIsDebitInterUnderClearing_Yes(false);
        setRdoIsDebitInterUnderClearing_No(false);
        //----------INTEREST RECEIVABLE----------
        setRdoldebitInterCharged_Yes(false);
        setRdoldebitInterCharged_No(false);
        setTxtMinDebitInterRate("");
        setTxtMaxDebitInterRate("");
        setTxtMinDebitInterAmt("");
        setTxtMaxDebitInterAmt("");
        setCboDebInterCalcFreq("");
        setCboDebitInterAppFreq("");
        setCboDebitInterComFreq("");
        setCboDebitProdRoundOff("");
        setCboDebitInterRoundOff("");
        setTdtLastInterCalcDate("");
        setTdtLastInterAppDate("");
        setRdoPLRRateAppl_Yes(false);
        setRdoPLRRateAppl_No(false);
        setTxtPLRRate("");
        setTdtPLRAppForm("");
        setRdoPLRApplNewAcc_Yes(false);
        setRdoPLRApplNewAcc_No(false);
        setRdoPLRApplExistingAcc_Yes(false);
        setRdoPLRApplExistingAcc_No(false);
        setTdtPlrApplAccSancForm("");
        setRdoPenalAppl_Yes(false);
        setRdoPenalAppl_No(false);
        setRdoLimitExpiryInter_Yes(false);
        setRdoLimitExpiryInter_No(false);
        setTxtPenalInterRate("");
        setTxtExposureLimit_Prud("");
        setTxtExposureLimit_Prud2("");
        setTxtExposureLimit_Policy("");
        setTxtExposureLimit_Policy2("");
        setCboProductFreq("");
        setCboPrematureIntCalAmt("");
        setTxtPreMatIntCalctPeriod("");
        setTxtPreMatIntCollectPeriod("");
        setRdoCurrDt_Yes(false);
        setRdoMaturity_Yes(false);
        setRdoMaturity_Deposit_closure_Upto_Curr_Yes(false);
        setRdoMaturity_Deposit_closure_Yes(false);
        setTxtGracePeriodPenalInterest("");
        setRdoInterestDueKeptReceivable_Yes(false);
        setRdoInterestDueKeptReceivable_No(false);
        setRdoinsuranceCharge_Yes(false);
        setRdoinsuranceCharge_No(false);
        setTxtInsuraceRate("");
        setTxtMaxCashDisb(0.0);
        setRdoinsuranceCharge_Yes(false);
        setRdoinsuranceCharge_No(false);
        getCbmInterestRepaymentFreq().setKeyForSelected("");
        //----------CHARGES----------
        setTxtAccClosingCharges("");
        setTxtMiscSerCharges("");
        setRdoStatCharges_Yes(false);
        setRdoStatCharges_No(false);
        setTxtStatChargesRate("");
        setRdoFolioChargesAppl_Yes(false);
        setRdoFolioChargesAppl_No(false);
        setTdtLastFolioChargesAppl("");
        setTxtNoEntriesPerFolio("");
        setTdtNextFolioDDate("");
        setTxtRatePerFolio("");
        setCboFolioChargesAppFreq("");
        setCboToCollectFolioCharges("");
        setRdoToChargeOn_Man(false);
        setRdoToChargeOn_Sys(false);
        setRdoToChargeOn_Both(false);
        setRdoPrincipalWaiverAllowed_No(false);
        setRdoPrincipalWaiverAllowed_Yes(false);
        setRdoNoticeWaiverAllowed_No(false);
        setRdoNoticeWaiverAllowed_Yes(false);
        setCboIncompleteFolioRoundOffFreq("");
        setRdoProcessCharges_Yes(false);
        setRdoProcessCharges_No(false);
        setCboCharge_Limit2("");
        setTxtCharge_Limit2("");
        setRdoCommitmentCharge_Yes(false);
        setRdoCommitmentCharge_No(false);
        setCboCharge_Limit3("");
        setTxtCharge_Limit3("");
        //added by rishad
        setRdoArcWaiverAllowed_Yes(false);
        setRdoArcWaiverAllowed_No(false);
        setRdoArbitraryWaiverAllowed_Yes(false);
        setRdoArbitraryWaiverAllowed_No(false);
        setRdoInsurenceWaiverAllowed_Yes(false);
        setRdoInsurenceWaiverAllowed_No(false);
        setRdoAdvertiseWaiverAllowed_Yes(false);
        setRdoAdvertiseWaiverAllowed_No(false);
        setRdoLeagalWaiverAllowed_Yes(false);
        setRdoLeagalWaiverAllowed_No(false);
        setRdoMiscellaneousWaiverAllowed_Yes(false);
        setRdoMiscellaneousWaiverAllowed_No(false);
        setRdoPostageWaiverAllowed_Yes(false);
        setRdoPostageWaiverAllowed_No(false);
        setRdoEpWaiverAllowed_Yes(false);
        setRdoEpWaiverAllowed_No(false);
        setRdoDecreeWaiverAllowed_Yes(false);
        setRdoDecreeWaiverAllowed_No(false);
        setTxtArcWaiveoffHead("");
        setTxtArbitraryWaiveoffHead("");
        setTxtAdvertiseWaiveoffHead("");
        setTxtPostageWaiveoffHead("");
        setTxtEpWaiveoffHead("");
        setTxtMiscellaneousWaiveoffHead("");
        setTxtDecreeWaiveoffHead("");
        setTxtLegalWaiveoffHead("");
        setTxtInsurenceWaiveoffHead("");
   
        setTxtRateAmt("");
        setTxtRangeFrom("");
        setTxtRangeTo("");
        setChkCreditStampAdvance(false);
        setChkAuctionAllowd(false);
        setChkCreditNoticeAdvance(false);
        //----------SPECIAL ITEMS----------
        setRdoATMcardIssued_Yes(false);
        setRdoATMcardIssued_No(false);
        setRdoCreditCardIssued_Yes(false);
        setRdoCreditCardIssued_No(false);
        setRdoMobileBanlingClient_Yes(false);
        setRdoMobileBanlingClient_No(false);
        setRdoIsAnyBranBankingAllowed_Yes(false);
        setRdoIsAnyBranBankingAllowed_No(false);
        //----------ACCOUNT HEAD----------
        setTxtAccClosCharges("");
        setTxtIntPayableAccount("");
        setTxtNoticeChargeAmt("");
        setTxtMiscServCharges("");
        setTxtStatementCharges("");
        setTxtAccDebitInter("");
        setTxtPenalInter("");
        setTxtAccCreditInter("");
        setTxtExpiryInter("");
        setTxtCheReturnChargest_Out("");
        setTxtCheReturnChargest_In("");
        setTxtFolioChargesAcc("");
        setTxtCommitCharges("");
        setTxtProcessingCharges("");
        setTxtLegalCharges("");
        setTxtArcCostAccount("");
        setTxtNoticeCharges("");
        setTxtPostageCharges("");
        setTxtArcExpenseAccount("");
        setTxtEaCostAccount("");
        setTxtEaExpenseAccount("");
        setTxtEpCostAccount("");
        setTxtEpExpenseAccount("");
        setTxtRebateInterest("");
        setTxtDebitIntDiscount("");
        setTxtOtherCharges("");
        //        setTxtMiscCharges("");
        setTxtArbitraryCharges("");
        setTxtInsuranceCharges("");
        setTxtExecutionDecreeCharges("");
        setTxtStampAdvancesHead("");
        setTxtNoticeAdvancesHead("");
        setTxtAdvertisementHead("");
        setTxtARCEPSuspenceHead("");
        //----------NON PERFORMING ASSETS----------
        setTxtMinPeriodsArrears("");
        setTxtProvisionStandardAssetss("");
        setCboMinPeriodsArrears("");
        setTxtPeriodTranSStanAssets("");
        setCboPeriodTranSStanAssets("");
        setTxtProvisionsStanAssets("");
        setTxtPeriodTransDoubtfulAssets1("");
        setTxtPeriodTransDoubtfulAssets2("");
        setTxtPeriodTransDoubtfulAssets3("");
        setCboPeriodTransDoubtfulAssets1("");
        setCboPeriodTransDoubtfulAssets2("");
        setCboPeriodTransDoubtfulAssets3("");
        setTxtProvisionDoubtfulAssets1("");
        setTxtProvisionDoubtfulAssets2("");
        setTxtProvisionDoubtfulAssets3("");
        setTxtPeriodTransLossAssets("");
        setCboPeriodTransLossAssets("");
        setTxtProvisionLossAssets("");
        setTxtPeriodAfterWhichTransNPerformingAssets("");
        setCboPeriodAfterWhichTransNPerformingAssets("");
        //----------INTEREST CALCULATION----------
        setCbocalcType("");
        setTxtMinPeriod("");
        setCboMinPeriod("");
        setTxtMaxPeriod("");
        setCboMaxPeriod("");
        setTxtMinAmtLoan("");
        setTxtMaxAmtLoan("");
        setTxtApplicableInter("");
        setTxtMinInterDebited("");
        setRdoSubsidy_Yes(false);
        setRdoSubsidy_No(false);
        setCboLoanPeriodMul("");
        setRdoasAndWhenCustomer_Yes(false);
        setRdoasAndWhenCustomer_No(false);
        setCboReviewPeriod("");
        setTxtReviewPeriod("");
        setTxtDepAmtLoanMaturingPeriod("");
        setCboDepAmtLoanMaturingPeriod(cboDepAmtLoanMaturingPeriod);
        setTxtElgLoanAmt("");
        setTxtDepAmtMaturing("");
        setCboDepositRoundOff("");
        //        setLstAvailableDeposits(new DefaultListModel());
        lstAvailableDeposits.clear();
        //        setLstSelectedDeposits(new DefaultListModel());
        lstSelectedDeposits.clear();
        existDepositMap.clear();
        newDepositMap.clear();
        deleteDepositMap.clear();
        
        lstSelectedTransaction.clear();
        lstAvailableTransaction.clear();
        existTransactionMap.clear();
        newTransactionMap.clear();
        deleteTransactionMap.clear();
        
        lstAvailableTransaction_OTS.clear();
        lstSelectedTransaction_OTS.clear();
        availableTrans_OTS.clear();
        existOTSTransactionMap.clear();
        deleteOTSTransactionMap.clear();
        newOTSTransactionMap.clear();
    
        
        //-------- DOCUMENTS ------------
        resetDocuments();
        //------------NOTICE TYPE CHARGES------
        resetNoticeTypeCharges();
        //-------- Classification -------
        setCboCommodityCode("");
        setCboTypeFacility("");
        setCboPurposeCode("");
        setCboIndusCode("");
        setCbo20Code("");
        setCboGovtSchemeCode("");
        setChkECGC(false);
        setCboGuaranteeCoverCode("");
        setCboHealthCode("");
        setCboSectorCode("");
        setCboRefinancingInsti("");
        setChkDirectFinance(false);
        setChkQIS(false);
        setCboSecurityDeails("");
        setRdobill_NO(false);
        setRdobill_Yes(false);
        setChkInterestOnMaturity(false);
        //subsidy
        rdoSubsidy_Yes=false; 
        rdoSubsidy_No =false; 
        rdoLoanBalance_Yes=false; 
        rdoSubsidyAdjustLoanBalance_Yes=false; 
        rdoSubsidyReceivedDate=false; 
        rdoLoanOpenDate =false; 
        rdoInterestRebateAllowed_Yes=false; 
        rdoInterestRebateAllowed_No=false; 
        txtInterestRebatePercentage ="";
        txtFinYearStartingFromDD  ="";
        txtFinYearStartingFromMM ="";
        rdoPenalInterestWaiverAllowed_Yes=false; 
        rdoPenalInterestWaiverAllowed_No=false; 
        rdoInterestWaiverAllowed_Yes =false; 
        rdoInterestWaiverAllowed_No =false; 
        rdoArcWaiverAllowed_Yes=false;
        rdoArcWaiverAllowed_No=false;
        rdoArbitraryWaiverAllowed_Yes=false;
        rdoArbitraryWaiverAllowed_No=false;
        rdoAdvertiseWaiverAllowed_Yes=false;
        rdoAdvertiseWaiverAllowed_No=false;
        rdoInsurenceWaiverAllowed_Yes=false;
        rdoInsurenceWaiverAllowed_No=false;
        rdoEpWaiverAllowed_Yes=false;
        rdoEpWaiverAllowed_No=false;
        rdoPostageWaiverAllowed_Yes=false;
        rdoPostageWaiverAllowed_No=false;
        rdoMiscellaneousWaiverAllowed_Yes=false;
        rdoMiscellaneousWaiverAllowed_No=false;
        rdoDecreeWaiverAllowed_Yes=false;
        rdoDecreeWaiverAllowed_No=false;
        rdoLeagalWaiverAllowed_Yes=false;
        rdoLeagalWaiverAllowed_No=false;
        rdoNoticeWaiverAllowed_Yes=false;
        rdoNoticeWaiverAllowed_No=false;
        rdoPrincipalWaiverAllowed_Yes=false;
        rdoPrincipalWaiverAllowed_No=false;
        rdoRecoveryWaiverAllowed_No = false;
        rdoRecoveryWaiverAllowed_Yes = false;
        rdoMeasurementWaiverAllowed_No = false;
        rdoMeasurementWaiverAllowed_Yes = false;
        
        rdoKoleFieldExpenseWaiverAllowed_Yes =  false;
        rdoKoleFieldExpenseWaiverAllowed_No = false;
        rdoKoleFieldOperationWaiverAllowed_No = false;
        rdoKoleFieldOperationWaiverAllowed_Yes = false;
        
        chkRebtSpl = false;
        
//        setCbmRebatePeriod(getCbmRebatePeriod().setKeyForSelected(""));
        getCbmInterestRebateCalculation().setKeyForSelected("");
        getCbmRebatePeriod().setKeyForSelected("");
        setChkGldRenewCash(false);
        setChkGldRenewOldAmt(false);
        setChkGldRenewMarketRate(false);
        setTxtFrequencyInDays(0);
        setChkSalRec(false);
        chkSalRec = false;        
        setChkIsInterestPeriodWise("");// Added by nithya on 15-11-2017 for 0007867
        setChkPrematureIntCalc("");// Added by nithya on 08-08-2018 for KD-187 need to change property loan penal calculation (mvnl)
        setTxtGracePeriodForOverDueInt(""); // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE
        setChkIntCalcFromSanctionDt(""); // Added by nithya on 13-02-2019 for KD-414 0019094: SUVARNA GOLD LOAN INTEREST ISSUE
        setChkGoldStockPhoto(""); // Added by nithya on 29-10-2019 for KD-763 Need Gold ornaments photo saving option
        setChkBlockIFLimitExceed(chkBlockIFLimitExceed); // Added by nithya on 16-11-2019 for KD-729
        setTxtRebateLoanIntPercent("");// Added by nithya on 11-01-2020 for KD-1234
        
        setTxtRecoveryChargeHead("");
        setTxtRecoveryWaiverHead("");
        setTxtMeasurementChargeHead("");
        setTxtMeasurementWaiverHead("");  
        
        setTxtKoleFieldExpenseHead("");
        setTxtKoleFieldExpenseWaiverHead("");
        setTxtKoleFieldOperationHead("");
        setTxtKoleFieldOperationWaiverHead("");
        
        ttNotifyObservers();
    }
    
    //To reset the table in UI, again called from UI
    public void resetTable(){
        try{
            setTxtRangeFrom("");
            setTxtRangeTo("");
            setTxtRateAmt("");
            ArrayList data = tblChargeTab.getDataArrayList();
            for(int i=data.size(); i>0; i--)
                tblChargeTab.removeRow(i-1);
            setChanged();
            ttNotifyObservers();
        }catch (Exception e) {
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            log.info("Error in resetTable():");
            parseException.logException(e,true);
        }
    }
    public String getTxtPenalWaiveoffHead() {
        return txtPenalWaiveoffHead;
    }
    public void setTxtPenalWaiveoffHead(String txtPenalWaiveoffHead) {
        this.txtPenalWaiveoffHead = txtPenalWaiveoffHead;
    }
    public String getTxtPrincipleWaiveoffHead() {
        return txtPrincipleWaiveoffHead;
    }
    public void setTxtPrincipleWaiveoffHead(String txtPrincipleWaiveoffHead) {
        this.txtPrincipleWaiveoffHead = txtPrincipleWaiveoffHead;
    }

    public String getTxtNoticeChargeDebitHead() {
        return txtNoticeChargeDebitHead;
    }

    public void setTxtNoticeChargeDebitHead(String txtNoticeChargeDebitHead) {
        this.txtNoticeChargeDebitHead = txtNoticeChargeDebitHead;
    }
    
    
    
    
    
    //set() and get() functions for all the fields in UI
    
    void setTxtProductID(String txtProductID){
        this.txtProductID = txtProductID;
        setChanged();
    }
    String getTxtProductID(){
        return this.txtProductID;
    }
    
    void setTxtProductDesc(String txtProductDesc){
        this.txtProductDesc = txtProductDesc;
        setChanged();
    }
    String getTxtProductDesc(){
        return this.txtProductDesc;
    }
    //--------------------------------------------
    
    void setCboOperatesLike(String cboOperatesLike){
        this.cboOperatesLike = cboOperatesLike;
        setChanged();
    }
    String getCboOperatesLike(){
        return this.cboOperatesLike;
    }
    public void setCbmOperatesLike(ComboBoxModel cbmOperatesLike){
        cbmOperatesLike = cbmOperatesLike;
        setChanged();
    }
    
    ComboBoxModel getCbmOperatesLike(){
        return cbmOperatesLike;
    }
    //--------------------------------------------
    
    void setTxtNumPatternFollowed(String txtNumPatternFollowed){
        this.txtNumPatternFollowed = txtNumPatternFollowed;
        setChanged();
    }
    String getTxtNumPatternFollowed(){
        return this.txtNumPatternFollowed;
    }
    
    void setTxtNumPatternFollowedSuffix(String txtNumPatternFollowedSuffix){
        this.txtNumPatternFollowedSuffix = txtNumPatternFollowedSuffix;
        setChanged();
    }
    String getTxtNumPatternFollowedSuffix(){
        return this.txtNumPatternFollowedSuffix;
    }
    
    void setTxtLastAccNum(String txtLastAccNum){
        this.txtLastAccNum = txtLastAccNum;
        setChanged();
    }
    String getTxtLastAccNum(){
        return this.txtLastAccNum;
    }
    
    void setRdoIsLimitDefnAllowed_Yes(boolean rdoIsLimitDefnAllowed_Yes){
        this.rdoIsLimitDefnAllowed_Yes = rdoIsLimitDefnAllowed_Yes;
        setChanged();
    }
    boolean getRdoIsLimitDefnAllowed_Yes(){
        return this.rdoIsLimitDefnAllowed_Yes;
    }
    
    void setRdoIsLimitDefnAllowed_No(boolean rdoIsLimitDefnAllowed_No){
        this.rdoIsLimitDefnAllowed_No = rdoIsLimitDefnAllowed_No;
        setChanged();
    }
    boolean getRdoIsLimitDefnAllowed_No(){
        return this.rdoIsLimitDefnAllowed_No;
    }
    
    void setRdoIsStaffAccOpened_Yes(boolean rdoIsStaffAccOpened_Yes){
        this.rdoIsStaffAccOpened_Yes = rdoIsStaffAccOpened_Yes;
        setChanged();
    }
    boolean getRdoIsStaffAccOpened_Yes(){
        return this.rdoIsStaffAccOpened_Yes;
    }
    
    void setRdoIsStaffAccOpened_No(boolean rdoIsStaffAccOpened_No){
        this.rdoIsStaffAccOpened_No = rdoIsStaffAccOpened_No;
        setChanged();
    }
    boolean getRdoIsStaffAccOpened_No(){
        return this.rdoIsStaffAccOpened_No;
    }
    
    void setRdoIsDebitInterUnderClearing_Yes(boolean rdoIsDebitInterUnderClearing_Yes){
        this.rdoIsDebitInterUnderClearing_Yes = rdoIsDebitInterUnderClearing_Yes;
        setChanged();
    }
    boolean getRdoIsDebitInterUnderClearing_Yes(){
        return this.rdoIsDebitInterUnderClearing_Yes;
    }
    
    void setRdoIsDebitInterUnderClearing_No(boolean rdoIsDebitInterUnderClearing_No){
        this.rdoIsDebitInterUnderClearing_No = rdoIsDebitInterUnderClearing_No;
        setChanged();
    }
    boolean getRdoIsDebitInterUnderClearing_No(){
        return this.rdoIsDebitInterUnderClearing_No;
    }
    
    void setTxtAccHead(String txtAccHead){
        this.txtAccHead = txtAccHead;
        setChanged();
    }
    String getTxtAccHead(){
        return this.txtAccHead;
    }
    
    void setRdoldebitInterCharged_Yes(boolean rdoldebitInterCharged_Yes){
        this.rdoldebitInterCharged_Yes = rdoldebitInterCharged_Yes;
        setChanged();
    }
    boolean getRdoldebitInterCharged_Yes(){
        return this.rdoldebitInterCharged_Yes;
    }
    
    void setRdoldebitInterCharged_No(boolean rdoldebitInterCharged_No){
        this.rdoldebitInterCharged_No = rdoldebitInterCharged_No;
        setChanged();
    }
    boolean getRdoldebitInterCharged_No(){
        return this.rdoldebitInterCharged_No;
    }
    
    void setTxtMinDebitInterRate(String txtMinDebitInterRate){
        this.txtMinDebitInterRate = txtMinDebitInterRate;
        setChanged();
    }
    String getTxtMinDebitInterRate(){
        return this.txtMinDebitInterRate;
    }
    
    void setTxtMaxDebitInterRate(String txtMaxDebitInterRate){
        this.txtMaxDebitInterRate = txtMaxDebitInterRate;
        setChanged();
    }
    String getTxtMaxDebitInterRate(){
        return this.txtMaxDebitInterRate;
    }
    
    void setTxtMinDebitInterAmt(String txtMinDebitInterAmt){
        this.txtMinDebitInterAmt = txtMinDebitInterAmt;
        setChanged();
    }
    String getTxtMinDebitInterAmt(){
        return this.txtMinDebitInterAmt;
    }
    
    void setTxtMaxDebitInterAmt(String txtMaxDebitInterAmt){
        this.txtMaxDebitInterAmt = txtMaxDebitInterAmt;
        setChanged();
    }
    String getTxtMaxDebitInterAmt(){
        return this.txtMaxDebitInterAmt;
    }
    //--------------------------------------------
    
    void setCboDebInterCalcFreq(String cboDebInterCalcFreq){
        this.cboDebInterCalcFreq = cboDebInterCalcFreq;
        setChanged();
    }
    String getCboDebInterCalcFreq(){
        return this.cboDebInterCalcFreq;
    }
    public void setCbmDebInterCalcFreq(ComboBoxModel cbmDebInterCalcFreq){
        cbmDebInterCalcFreq = cbmDebInterCalcFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmDebInterCalcFreq(){
        return cbmDebInterCalcFreq;
    }
    //--------------------------------------------
    
    void setCboDebitInterAppFreq(String cboDebitInterAppFreq){
        this.cboDebitInterAppFreq = cboDebitInterAppFreq;
        setChanged();
    }
    String getCboDebitInterAppFreq(){
        return this.cboDebitInterAppFreq;
    }
    public void setCbmDebitInterAppFreq(ComboBoxModel cbmDebitInterAppFreq){
        cbmDebitInterAppFreq = cbmDebitInterAppFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitInterAppFreq(){
        return cbmDebitInterAppFreq;
    }
    //--------------------------------------------
    
    void setCboDebitInterComFreq(String cboDebitInterComFreq){
        this.cboDebitInterComFreq = cboDebitInterComFreq;
        setChanged();
    }
    String getCboDebitInterComFreq(){
        return this.cboDebitInterComFreq;
    }
    public void setCbmDebitInterComFreq(ComboBoxModel cbmDebitInterComFreq){
        cbmDebitInterComFreq = cbmDebitInterComFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitInterComFreq(){
        return cbmDebitInterComFreq;
    }
    //--------------------------------------------
    
    void setCboDebitProdRoundOff(String cboDebitProdRoundOff){
        this.cboDebitProdRoundOff = cboDebitProdRoundOff;
        setChanged();
    }
    String getCboDebitProdRoundOff(){
        return this.cboDebitProdRoundOff;
    }
    public void setCbmDebitProdRoundOff(ComboBoxModel cbmDebitProdRoundOff){
        cbmDebitProdRoundOff = cbmDebitProdRoundOff;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitProdRoundOff(){
        return cbmDebitProdRoundOff;
    }
    //--------------------------------------------
    
    void setCboDebitInterRoundOff(String cboDebitInterRoundOff){
        this.cboDebitInterRoundOff = cboDebitInterRoundOff;
        setChanged();
    }
    String getCboDebitInterRoundOff(){
        return this.cboDebitInterRoundOff;
    }
    public void setCbmDebitInterRoundOff(ComboBoxModel cbmDebitInterRoundOff){
        cbmDebitInterRoundOff = cbmDebitInterRoundOff;
        setChanged();
    }
    
    ComboBoxModel getCbmDebitInterRoundOff(){
        return cbmDebitInterRoundOff;
    }
    
    public void setCbmInterestRepaymentFreq(ComboBoxModel cbmInterestRepaymentFreq){
        cbmInterestRepaymentFreq = cbmInterestRepaymentFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmInterestRepaymentFreq(){
        return cbmInterestRepaymentFreq;
    }
    
    
    //--------------------------------------------
    
    void setTdtLastInterCalcDate(String tdtLastInterCalcDate){
        this.tdtLastInterCalcDate = tdtLastInterCalcDate;
        setChanged();
    }
    String getTdtLastInterCalcDate(){
        return this.tdtLastInterCalcDate;
    }
    
    void setTdtLastInterAppDate(String tdtLastInterAppDate){
        this.tdtLastInterAppDate = tdtLastInterAppDate;
        setChanged();
    }
    String getTdtLastInterAppDate(){
        return this.tdtLastInterAppDate;
    }
    
    void setRdoPLRRateAppl_Yes(boolean rdoPLRRateAppl_Yes){
        this.rdoPLRRateAppl_Yes = rdoPLRRateAppl_Yes;
        setChanged();
    }
    boolean getRdoPLRRateAppl_Yes(){
        return this.rdoPLRRateAppl_Yes;
    }
    
    void setRdoPLRRateAppl_No(boolean rdoPLRRateAppl_No){
        this.rdoPLRRateAppl_No = rdoPLRRateAppl_No;
        setChanged();
    }
    boolean getRdoPLRRateAppl_No(){
        return this.rdoPLRRateAppl_No;
    }
    
    void setTxtPLRRate(String txtPLRRate){
        this.txtPLRRate = txtPLRRate;
        setChanged();
    }
    String getTxtPLRRate(){
        return this.txtPLRRate;
    }
    
    void setTdtPLRAppForm(String tdtPLRAppForm){
        this.tdtPLRAppForm = tdtPLRAppForm;
        setChanged();
    }
    String getTdtPLRAppForm(){
        return this.tdtPLRAppForm;
    }
    
    void setRdoPLRApplNewAcc_Yes(boolean rdoPLRApplNewAcc_Yes){
        this.rdoPLRApplNewAcc_Yes = rdoPLRApplNewAcc_Yes;
        setChanged();
    }
    boolean getRdoPLRApplNewAcc_Yes(){
        return this.rdoPLRApplNewAcc_Yes;
    }
    
    void setRdoPLRApplNewAcc_No(boolean rdoPLRApplNewAcc_No){
        this.rdoPLRApplNewAcc_No = rdoPLRApplNewAcc_No;
        setChanged();
    }
    boolean getRdoPLRApplNewAcc_No(){
        return this.rdoPLRApplNewAcc_No;
    }
    
    void setRdoPLRApplExistingAcc_Yes(boolean rdoPLRApplExistingAcc_Yes){
        this.rdoPLRApplExistingAcc_Yes = rdoPLRApplExistingAcc_Yes;
        setChanged();
    }
    boolean getRdoPLRApplExistingAcc_Yes(){
        return this.rdoPLRApplExistingAcc_Yes;
    }
    
    void setRdoPLRApplExistingAcc_No(boolean rdoPLRApplExistingAcc_No){
        this.rdoPLRApplExistingAcc_No = rdoPLRApplExistingAcc_No;
        setChanged();
    }
    boolean getRdoPLRApplExistingAcc_No(){
        return this.rdoPLRApplExistingAcc_No;
    }
    
    void setTdtPlrApplAccSancForm(String tdtPlrApplAccSancForm){
        this.tdtPlrApplAccSancForm = tdtPlrApplAccSancForm;
        setChanged();
    }
    String getTdtPlrApplAccSancForm(){
        return this.tdtPlrApplAccSancForm;
    }
    
    void setRdoPenalAppl_Yes(boolean rdoPenalAppl_Yes){
        this.rdoPenalAppl_Yes = rdoPenalAppl_Yes;
        setChanged();
    }
    boolean getRdoPenalAppl_Yes(){
        return this.rdoPenalAppl_Yes;
    }
    
    void setRdoPenalAppl_No(boolean rdoPenalAppl_No){
        this.rdoPenalAppl_No = rdoPenalAppl_No;
        setChanged();
    }
    boolean getRdoPenalAppl_No(){
        return this.rdoPenalAppl_No;
    }
    
    void setRdoLimitExpiryInter_Yes(boolean rdoLimitExpiryInter_Yes){
        this.rdoLimitExpiryInter_Yes = rdoLimitExpiryInter_Yes;
        setChanged();
    }
    boolean getRdoLimitExpiryInter_Yes(){
        return this.rdoLimitExpiryInter_Yes;
    }
    
    void setRdoLimitExpiryInter_No(boolean rdoLimitExpiryInter_No){
        this.rdoLimitExpiryInter_No = rdoLimitExpiryInter_No;
        setChanged();
    }
    boolean getRdoLimitExpiryInter_No(){
        return this.rdoLimitExpiryInter_No;
    }
    
    void setTxtPenalInterRate(String txtPenalInterRate){
        this.txtPenalInterRate = txtPenalInterRate;
        setChanged();
    }
    String getTxtPenalInterRate(){
        return this.txtPenalInterRate;
    }
    
    void setTxtExposureLimit_Prud(String txtExposureLimit_Prud){
        this.txtExposureLimit_Prud = txtExposureLimit_Prud;
        setChanged();
    }
    String getTxtExposureLimit_Prud(){
        return this.txtExposureLimit_Prud;
    }
    
    void setTxtExposureLimit_Prud2(String txtExposureLimit_Prud2){
        this.txtExposureLimit_Prud2 = txtExposureLimit_Prud2;
        setChanged();
    }
    String getTxtExposureLimit_Prud2(){
        return this.txtExposureLimit_Prud2;
    }
    
    void setTxtExposureLimit_Policy(String txtExposureLimit_Policy){
        this.txtExposureLimit_Policy = txtExposureLimit_Policy;
        setChanged();
    }
    String getTxtExposureLimit_Policy(){
        return this.txtExposureLimit_Policy;
    }
    
    void setTxtExposureLimit_Policy2(String txtExposureLimit_Policy2){
        this.txtExposureLimit_Policy2 = txtExposureLimit_Policy2;
        setChanged();
    }
    String getTxtExposureLimit_Policy2(){
        return this.txtExposureLimit_Policy2;
    }
    
    //--------------------------------------------
    
    void setCboProductFreq(String cboProductFreq){
        this.cboProductFreq = cboProductFreq;
        setChanged();
    }
    String getCboProductFreq(){
        return this.cboProductFreq;
    }
    public void setCbmProductFreq(ComboBoxModel cbmProductFreq){
        cbmProductFreq = cbmProductFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmProductFreq(){
        return cbmProductFreq;
    }
    //--------------------------------------------
    
    void setTxtAccClosingCharges(String txtAccClosingCharges){
        this.txtAccClosingCharges = txtAccClosingCharges;
        setChanged();
    }
    String getTxtAccClosingCharges(){
        return this.txtAccClosingCharges;
    }
    
    void setTxtMiscSerCharges(String txtMiscSerCharges){
        this.txtMiscSerCharges = txtMiscSerCharges;
        setChanged();
    }
    String getTxtMiscSerCharges(){
        return this.txtMiscSerCharges;
    }
    
    void setRdoStatCharges_Yes(boolean rdoStatCharges_Yes){
        this.rdoStatCharges_Yes = rdoStatCharges_Yes;
        setChanged();
    }
    boolean getRdoStatCharges_Yes(){
        return this.rdoStatCharges_Yes;
    }
    
    void setRdoStatCharges_No(boolean rdoStatCharges_No){
        this.rdoStatCharges_No = rdoStatCharges_No;
        setChanged();
    }
    boolean getRdoStatCharges_No(){
        return this.rdoStatCharges_No;
    }
    
    void setTxtStatChargesRate(String txtStatChargesRate){
        this.txtStatChargesRate = txtStatChargesRate;
        setChanged();
    }
    String getTxtStatChargesRate(){
        return this.txtStatChargesRate;
    }
    //------------------------------------------------
    
    
    
    //------------------------------------------------
    
    void setTxtRateAmt(String txtRateAmt){
        this.txtRateAmt = txtRateAmt;
        setChanged();
    }
    String getTxtRateAmt(){
        return this.txtRateAmt;
    }
    
    void setRdoFolioChargesAppl_Yes(boolean rdoFolioChargesAppl_Yes){
        this.rdoFolioChargesAppl_Yes = rdoFolioChargesAppl_Yes;
        setChanged();
    }
    boolean getRdoFolioChargesAppl_Yes(){
        return this.rdoFolioChargesAppl_Yes;
    }
    
    void setRdoFolioChargesAppl_No(boolean rdoFolioChargesAppl_No){
        this.rdoFolioChargesAppl_No = rdoFolioChargesAppl_No;
        setChanged();
    }
    boolean getRdoFolioChargesAppl_No(){
        return this.rdoFolioChargesAppl_No;
    }
    
    void setTdtLastFolioChargesAppl(String tdtLastFolioChargesAppl){
        this.tdtLastFolioChargesAppl = tdtLastFolioChargesAppl;
        setChanged();
    }
    String getTdtLastFolioChargesAppl(){
        return this.tdtLastFolioChargesAppl;
    }
    
    void setTxtNoEntriesPerFolio(String txtNoEntriesPerFolio){
        this.txtNoEntriesPerFolio = txtNoEntriesPerFolio;
        setChanged();
    }
    String getTxtNoEntriesPerFolio(){
        return this.txtNoEntriesPerFolio;
    }
    
    void setTdtNextFolioDDate(String tdtNextFolioDDate){
        this.tdtNextFolioDDate = tdtNextFolioDDate;
        setChanged();
    }
    String getTdtNextFolioDDate(){
        return this.tdtNextFolioDDate;
    }
    
    void setTxtRatePerFolio(String txtRatePerFolio){
        this.txtRatePerFolio = txtRatePerFolio;
        setChanged();
    }
    String getTxtRatePerFolio(){
        return this.txtRatePerFolio;
    }
    //------------------------------------------------
    
    void setCboFolioChargesAppFreq(String cboFolioChargesAppFreq){
        this.cboFolioChargesAppFreq = cboFolioChargesAppFreq;
        setChanged();
    }
    String getCboFolioChargesAppFreq(){
        return this.cboFolioChargesAppFreq;
    }
    public void setCbmFolioChargesAppFreq(ComboBoxModel cbmFolioChargesAppFreq){
        cbmFolioChargesAppFreq = cbmFolioChargesAppFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmFolioChargesAppFreq(){
        return cbmFolioChargesAppFreq;
    }
    //------------------------------------------------
    
    void setCboToCollectFolioCharges(String cboToCollectFolioCharges){
        this.cboToCollectFolioCharges = cboToCollectFolioCharges;
        setChanged();
    }
    String getCboToCollectFolioCharges(){
        return this.cboToCollectFolioCharges;
    }
    public void setCbmToCollectFolioCharges(ComboBoxModel cbmToCollectFolioCharges){
        cbmToCollectFolioCharges = cbmToCollectFolioCharges;
        setChanged();
    }
    
    ComboBoxModel getCbmToCollectFolioCharges(){
        return cbmToCollectFolioCharges;
    }
    //------------------------------------------------
    
    void setRdoToChargeOn_Man(boolean rdoToChargeOn_Man){
        this.rdoToChargeOn_Man = rdoToChargeOn_Man;
        setChanged();
    }
    boolean getRdoToChargeOn_Man(){
        return this.rdoToChargeOn_Man;
    }
    
    void setRdoToChargeOn_Sys(boolean rdoToChargeOn_Sys){
        this.rdoToChargeOn_Sys = rdoToChargeOn_Sys;
        setChanged();
    }
    boolean getRdoToChargeOn_Sys(){
        return this.rdoToChargeOn_Sys;
    }
    
    void setRdoToChargeOn_Both(boolean rdoToChargeOn_Both){
        this.rdoToChargeOn_Both = rdoToChargeOn_Both;
        setChanged();
    }
    boolean getRdoToChargeOn_Both(){
        return this.rdoToChargeOn_Both;
    }
    //------------------------------------------------
    
    void setCboIncompleteFolioRoundOffFreq(String cboIncompleteFolioRoundOffFreq){
        this.cboIncompleteFolioRoundOffFreq = cboIncompleteFolioRoundOffFreq;
        setChanged();
    }
    String getCboIncompleteFolioRoundOffFreq(){
        return this.cboIncompleteFolioRoundOffFreq;
    }
    public void setCbmIncompleteFolioRoundOffFreq(ComboBoxModel cbmIncompleteFolioRoundOffFreq){
        cbmIncompleteFolioRoundOffFreq = cbmIncompleteFolioRoundOffFreq;
        setChanged();
    }
    
    ComboBoxModel getCbmIncompleteFolioRoundOffFreq(){
        return cbmIncompleteFolioRoundOffFreq;
    }
    //------------------------------------------------
    
    void setRdoProcessCharges_Yes(boolean rdoProcessCharges_Yes){
        this.rdoProcessCharges_Yes = rdoProcessCharges_Yes;
        setChanged();
    }
    boolean getRdoProcessCharges_Yes(){
        return this.rdoProcessCharges_Yes;
    }
    
    void setRdoProcessCharges_No(boolean rdoProcessCharges_No){
        this.rdoProcessCharges_No = rdoProcessCharges_No;
        setChanged();
    }
    boolean getRdoProcessCharges_No(){
        return this.rdoProcessCharges_No;
    }
    
    //    void setTxtcharge_Limit1(String txtcharge_Limit1){
    //        this.txtcharge_Limit1 = txtcharge_Limit1;
    //        setChanged();
    //    }
    //    String getTxtcharge_Limit1(){
    //        return this.txtcharge_Limit1;
    //    }
    
    void setTxtCharge_Limit2(String txtCharge_Limit2){
        this.txtCharge_Limit2 = txtCharge_Limit2;
        setChanged();
    }
    String getTxtCharge_Limit2(){
        return this.txtCharge_Limit2;
    }
    
    void setRdoCommitmentCharge_Yes(boolean rdoCommitmentCharge_Yes){
        this.rdoCommitmentCharge_Yes = rdoCommitmentCharge_Yes;
        setChanged();
    }
    boolean getRdoCommitmentCharge_Yes(){
        return this.rdoCommitmentCharge_Yes;
    }
    
    void setRdoCommitmentCharge_No(boolean rdoCommitmentCharge_No){
        this.rdoCommitmentCharge_No = rdoCommitmentCharge_No;
        setChanged();
    }
    boolean getRdoCommitmentCharge_No(){
        return this.rdoCommitmentCharge_No;
    }
    
    void setTxtCharge_Limit3(String txtCharge_Limit3){
        this.txtCharge_Limit3 = txtCharge_Limit3;
        setChanged();
    }
    String getTxtCharge_Limit3(){
        return this.txtCharge_Limit3;
    }
    
    //    void setTxtCharge_Limit4(String txtCharge_Limit4){
    //        this.txtCharge_Limit4 = txtCharge_Limit4;
    //        setChanged();
    //    }
    //    String getTxtCharge_Limit4(){
    //        return this.txtCharge_Limit4;
    //    }
    
    void setRdoATMcardIssued_Yes(boolean rdoATMcardIssued_Yes){
        this.rdoATMcardIssued_Yes = rdoATMcardIssued_Yes;
        setChanged();
    }
    boolean getRdoATMcardIssued_Yes(){
        return this.rdoATMcardIssued_Yes;
    }
    
    void setRdoATMcardIssued_No(boolean rdoATMcardIssued_No){
        this.rdoATMcardIssued_No = rdoATMcardIssued_No;
        setChanged();
    }
    boolean getRdoATMcardIssued_No(){
        return this.rdoATMcardIssued_No;
    }
    
    void setRdoCreditCardIssued_Yes(boolean rdoCreditCardIssued_Yes){
        this.rdoCreditCardIssued_Yes = rdoCreditCardIssued_Yes;
        setChanged();
    }
    boolean getRdoCreditCardIssued_Yes(){
        return this.rdoCreditCardIssued_Yes;
    }
    
    void setRdoCreditCardIssued_No(boolean rdoCreditCardIssued_No){
        this.rdoCreditCardIssued_No = rdoCreditCardIssued_No;
        setChanged();
    }
    boolean getRdoCreditCardIssued_No(){
        return this.rdoCreditCardIssued_No;
    }
    
    void setRdoMobileBanlingClient_Yes(boolean rdoMobileBanlingClient_Yes){
        this.rdoMobileBanlingClient_Yes = rdoMobileBanlingClient_Yes;
        setChanged();
    }
    boolean getRdoMobileBanlingClient_Yes(){
        return this.rdoMobileBanlingClient_Yes;
    }
    
    void setRdoMobileBanlingClient_No(boolean rdoMobileBanlingClient_No){
        this.rdoMobileBanlingClient_No = rdoMobileBanlingClient_No;
        setChanged();
    }
    boolean getRdoMobileBanlingClient_No(){
        return this.rdoMobileBanlingClient_No;
    }
    
    void setRdoIsAnyBranBankingAllowed_Yes(boolean rdoIsAnyBranBankingAllowed_Yes){
        this.rdoIsAnyBranBankingAllowed_Yes = rdoIsAnyBranBankingAllowed_Yes;
        setChanged();
    }
    boolean getRdoIsAnyBranBankingAllowed_Yes(){
        return this.rdoIsAnyBranBankingAllowed_Yes;
    }
    
    void setRdoIsAnyBranBankingAllowed_No(boolean rdoIsAnyBranBankingAllowed_No){
        this.rdoIsAnyBranBankingAllowed_No = rdoIsAnyBranBankingAllowed_No;
        setChanged();
    }
    boolean getRdoIsAnyBranBankingAllowed_No(){
        return this.rdoIsAnyBranBankingAllowed_No;
    }
    
    void setTxtAccClosCharges(String txtAccClosCharges){
        this.txtAccClosCharges = txtAccClosCharges;
        setChanged();
    }
    String getTxtAccClosCharges(){
        return this.txtAccClosCharges;
    }
    
    void setTxtMiscServCharges(String txtMiscServCharges){
        this.txtMiscServCharges = txtMiscServCharges;
        setChanged();
    }
    String getTxtMiscServCharges(){
        return this.txtMiscServCharges;
    }
    
    void setTxtStatementCharges(String txtStatementCharges){
        this.txtStatementCharges = txtStatementCharges;
        setChanged();
    }
    String getTxtStatementCharges(){
        return this.txtStatementCharges;
    }
    
    void setTxtAccDebitInter(String txtAccDebitInter){
        this.txtAccDebitInter = txtAccDebitInter;
        setChanged();
    }
    String getTxtAccDebitInter(){
        return this.txtAccDebitInter;
    }
    
    void setTxtPenalInter(String txtPenalInter){
        this.txtPenalInter = txtPenalInter;
        setChanged();
    }
    String getTxtPenalInter(){
        return this.txtPenalInter;
    }
    
    void setTxtAccCreditInter(String txtAccCreditInter){
        this.txtAccCreditInter = txtAccCreditInter;
        setChanged();
    }
    String getTxtAccCreditInter(){
        return this.txtAccCreditInter;
    }
    
    void setTxtExpiryInter(String txtExpiryInter){
        this.txtExpiryInter = txtExpiryInter;
        setChanged();
    }
    String getTxtExpiryInter(){
        return this.txtExpiryInter;
    }
    
    void setTxtCheReturnChargest_Out(String txtCheReturnChargest_Out){
        this.txtCheReturnChargest_Out = txtCheReturnChargest_Out;
        setChanged();
    }
    String getTxtCheReturnChargest_Out(){
        return this.txtCheReturnChargest_Out;
    }
    
    void setTxtCheReturnChargest_In(String txtCheReturnChargest_In){
        this.txtCheReturnChargest_In = txtCheReturnChargest_In;
        setChanged();
    }
    String getTxtCheReturnChargest_In(){
        return this.txtCheReturnChargest_In;
    }
    
    void setTxtFolioChargesAcc(String txtFolioChargesAcc){
        this.txtFolioChargesAcc = txtFolioChargesAcc;
        setChanged();
    }
    String getTxtFolioChargesAcc(){
        return this.txtFolioChargesAcc;
    }
    
    void setTxtCommitCharges(String txtCommitCharges){
        this.txtCommitCharges = txtCommitCharges;
        setChanged();
    }
    String getTxtCommitCharges(){
        return this.txtCommitCharges;
    }
    
    void setTxtProcessingCharges(String txtProcessingCharges){
        this.txtProcessingCharges = txtProcessingCharges;
        setChanged();
    }
    String getTxtProcessingCharges(){
        return this.txtProcessingCharges;
    }
    
    void setTxtMinPeriodsArrears(String txtMinPeriodsArrears){
        this.txtMinPeriodsArrears = txtMinPeriodsArrears;
        setChanged();
    }
    String getTxtMinPeriodsArrears(){
        return this.txtMinPeriodsArrears;
    }
    //------------------------------------------------
    
    void setCboMinPeriodsArrears(String cboMinPeriodsArrears){
        this.cboMinPeriodsArrears = cboMinPeriodsArrears;
        setChanged();
    }
    String getCboMinPeriodsArrears(){
        return this.cboMinPeriodsArrears;
    }
    public void setCbmMinPeriodsArrears(ComboBoxModel cbmMinPeriodsArrears){
        cbmMinPeriodsArrears = cbmMinPeriodsArrears;
        setChanged();
    }
    
    ComboBoxModel getCbmMinPeriodsArrears(){
        return cbmMinPeriodsArrears;
    }
    //------------------------------------------------
    
    void setTxtPeriodTranSStanAssets(String txtPeriodTranSStanAssets){
        this.txtPeriodTranSStanAssets = txtPeriodTranSStanAssets;
        setChanged();
    }
    String getTxtPeriodTranSStanAssets(){
        return this.txtPeriodTranSStanAssets;
    }
    //------------------------------------------------
    
    void setCboPeriodTranSStanAssets(String cboPeriodTranSStanAssets){
        this.cboPeriodTranSStanAssets = cboPeriodTranSStanAssets;
        setChanged();
    }
    String getCboPeriodTranSStanAssets(){
        return this.cboPeriodTranSStanAssets;
    }
    public void setCbmPeriodTranSStanAssets(ComboBoxModel cbmPeriodTranSStanAssets){
        cbmPeriodTranSStanAssets = cbmPeriodTranSStanAssets;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTranSStanAssets(){
        return cbmPeriodTranSStanAssets;
    }
    //------------------------------------------------
    
    void setTxtProvisionsStanAssets(String txtProvisionsStanAssets){
        this.txtProvisionsStanAssets = txtProvisionsStanAssets;
        setChanged();
    }
    String getTxtProvisionsStanAssets(){
        return this.txtProvisionsStanAssets;
    }
    
    void setTxtPeriodTransDoubtfulAssets1(String txtPeriodTransDoubtfulAssets1){
        this.txtPeriodTransDoubtfulAssets1 = txtPeriodTransDoubtfulAssets1;
        setChanged();
    }
    String getTxtPeriodTransDoubtfulAssets1(){
        return this.txtPeriodTransDoubtfulAssets1;
    }
    
    void setTxtPeriodTransDoubtfulAssets2(String txtPeriodTransDoubtfulAssets2){
        this.txtPeriodTransDoubtfulAssets2 = txtPeriodTransDoubtfulAssets2;
        setChanged();
    }
    String getTxtPeriodTransDoubtfulAssets2(){
        return this.txtPeriodTransDoubtfulAssets2;
    }
    
    void setTxtPeriodTransDoubtfulAssets3(String txtPeriodTransDoubtfulAssets3){
        this.txtPeriodTransDoubtfulAssets3 = txtPeriodTransDoubtfulAssets3;
        setChanged();
    }
    String getTxtPeriodTransDoubtfulAssets3(){
        return this.txtPeriodTransDoubtfulAssets3;
    }
    //------------------------------------------------
    
    void setCboPeriodTransDoubtfulAssets1(String cboPeriodTransDoubtfulAssets1){
        this.cboPeriodTransDoubtfulAssets1 = cboPeriodTransDoubtfulAssets1;
        setChanged();
    }
    String getCboPeriodTransDoubtfulAssets1(){
        return this.cboPeriodTransDoubtfulAssets1;
    }
    public void setCbmPeriodTransDoubtfulAssets1(ComboBoxModel cbmPeriodTransDoubtfulAssets1){
        cbmPeriodTransDoubtfulAssets1 = cbmPeriodTransDoubtfulAssets1;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransDoubtfulAssets1(){
        return cbmPeriodTransDoubtfulAssets1;
    }
    
    
    void setCboPeriodTransDoubtfulAssets2(String cboPeriodTransDoubtfulAssets2){
        this.cboPeriodTransDoubtfulAssets2 = cboPeriodTransDoubtfulAssets2;
        setChanged();
    }
    String getCboPeriodTransDoubtfulAssets2(){
        return this.cboPeriodTransDoubtfulAssets2;
    }
    public void setCbmPeriodTransDoubtfulAssets2(ComboBoxModel cbmPeriodTransDoubtfulAssets2){
        cbmPeriodTransDoubtfulAssets2 = cbmPeriodTransDoubtfulAssets2;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransDoubtfulAssets2(){
        return cbmPeriodTransDoubtfulAssets2;
    }
    
    void setCboPeriodTransDoubtfulAssets3(String cboPeriodTransDoubtfulAssets3){
        this.cboPeriodTransDoubtfulAssets3 = cboPeriodTransDoubtfulAssets3;
        setChanged();
    }
    String getCboPeriodTransDoubtfulAssets3(){
        return this.cboPeriodTransDoubtfulAssets3;
    }
    public void setCbmPeriodTransDoubtfulAssets3(ComboBoxModel cbmPeriodTransDoubtfulAssets3){
        cbmPeriodTransDoubtfulAssets3 = cbmPeriodTransDoubtfulAssets3;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransDoubtfulAssets3(){
        return cbmPeriodTransDoubtfulAssets3;
    }
    
    
    //------------------------------------------------
    
    void setTxtProvisionDoubtfulAssets1(String txtProvisionDoubtfulAssets1){
        this.txtProvisionDoubtfulAssets1 = txtProvisionDoubtfulAssets1;
        setChanged();
    }
    String getTxtProvisionDoubtfulAssets1(){
        return this.txtProvisionDoubtfulAssets1;
    }
    
    void setTxtProvisionDoubtfulAssets2(String txtProvisionDoubtfulAssets2){
        this.txtProvisionDoubtfulAssets2 = txtProvisionDoubtfulAssets2;
        setChanged();
    }
    String getTxtProvisionDoubtfulAssets2(){
        return this.txtProvisionDoubtfulAssets2;
    }
    
    void setTxtProvisionDoubtfulAssets3(String txtProvisionDoubtfulAssets3){
        this.txtProvisionDoubtfulAssets3 = txtProvisionDoubtfulAssets3;
        setChanged();
    }
    String getTxtProvisionDoubtfulAssets3(){
        return this.txtProvisionDoubtfulAssets3;
    }
    
    
    
    void setTxtPeriodTransLossAssets(String txtPeriodTransLossAssets){
        this.txtPeriodTransLossAssets = txtPeriodTransLossAssets;
        setChanged();
    }
    String getTxtPeriodTransLossAssets(){
        return this.txtPeriodTransLossAssets;
    }
    //------------------------------------------------
    
    void setCboPeriodTransLossAssets(String cboPeriodTransLossAssets){
        this.cboPeriodTransLossAssets = cboPeriodTransLossAssets;
        setChanged();
    }
    String getCboPeriodTransLossAssets(){
        return this.cboPeriodTransLossAssets;
    }
    public void setCbmPeriodTransLossAssets(ComboBoxModel cbmPeriodTransLossAssets){
        cbmPeriodTransLossAssets = cbmPeriodTransLossAssets;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodTransLossAssets(){
        return cbmPeriodTransLossAssets;
    }
    //------------------------------------------------
    
    void setTxtPeriodAfterWhichTransNPerformingAssets(String txtPeriodAfterWhichTransNPerformingAssets){
        this.txtPeriodAfterWhichTransNPerformingAssets = txtPeriodAfterWhichTransNPerformingAssets;
        setChanged();
    }
    String getTxtPeriodAfterWhichTransNPerformingAssets(){
        return this.txtPeriodAfterWhichTransNPerformingAssets;
    }
    //------------------------------------------------
    
    void setCboPeriodAfterWhichTransNPerformingAssets(String cboPeriodAfterWhichTransNPerformingAssets){
        this.cboPeriodAfterWhichTransNPerformingAssets = cboPeriodAfterWhichTransNPerformingAssets;
        setChanged();
    }
    String getCboPeriodAfterWhichTransNPerformingAssets(){
        return this.cboPeriodAfterWhichTransNPerformingAssets;
    }
    public void setCbmPeriodAfterWhichTransNPerformingAssets(ComboBoxModel cbmPeriodAfterWhichTransNPerformingAssets){
        cbmPeriodAfterWhichTransNPerformingAssets = cbmPeriodAfterWhichTransNPerformingAssets;
        setChanged();
    }
    
    ComboBoxModel getCbmPeriodAfterWhichTransNPerformingAssets(){
        return cbmPeriodAfterWhichTransNPerformingAssets;
    }
    //------------------------------------------------
    void setCbocalcType(String cbocalcType){
        this.cbocalcType = cbocalcType;
        setChanged();
    }
    String getCbocalcType(){
        return this.cbocalcType;
    }
    public void setCbmcalcType(ComboBoxModel cbmcalcType){
        cbmcalcType = cbmcalcType;
        setChanged();
    }
    
    ComboBoxModel getCbmcalcType(){
        return cbmcalcType;
    }
    //------------------------------------------------
    
    void setTxtMinPeriod(String txtMinPeriod){
        this.txtMinPeriod = txtMinPeriod;
        setChanged();
    }
    String getTxtMinPeriod(){
        return this.txtMinPeriod;
    }
    //------------------------------------------------
    
    void setCboMinPeriod(String cboMinPeriod){
        this.cboMinPeriod = cboMinPeriod;
        setChanged();
    }
    String getCboMinPeriod(){
        return this.cboMinPeriod;
    }
    public void setCbmMinPeriod(ComboBoxModel cbmMinPeriod){
        cbmMinPeriod = cbmMinPeriod;
        setChanged();
    }
    
    ComboBoxModel getCbmMinPeriod(){
        return cbmMinPeriod;
    }
    //------------------------------------------------
    
    void setTxtMaxPeriod(String txtMaxPeriod){
        this.txtMaxPeriod = txtMaxPeriod;
        setChanged();
    }
    String getTxtMaxPeriod(){
        return this.txtMaxPeriod;
    }
    //------------------------------------------------
    
    void setCboMaxPeriod(String cboMaxPeriod){
        this.cboMaxPeriod = cboMaxPeriod;
        setChanged();
    }
    String getCboMaxPeriod(){
        return this.cboMaxPeriod;
    }
    public void setCbmMaxPeriod(ComboBoxModel cbmMaxPeriod){
        cbmMaxPeriod = cbmMaxPeriod;
        setChanged();
    }
    
    ComboBoxModel getCbmMaxPeriod(){
        return cbmMaxPeriod;
    }

    public String getChkAuctionAllowed() {
        return chkAuctionAllowed;
    }

    public void setChkAuctionAllowed(String chkAuctionAllowed) {
        this.chkAuctionAllowed = chkAuctionAllowed;
    }

    public String getTxtSuspenseCreditAchd() {
        return txtSuspenseCreditAchd;
    }

    public void setTxtSuspenseCreditAchd(String txtSuspenseCreditAchd) {
        this.txtSuspenseCreditAchd = txtSuspenseCreditAchd;
    }

    public String getTxtSuspenseDebitAchd() {
        return txtSuspenseDebitAchd;
    }

    public void setTxtSuspenseDebitAchd(String txtSuspenseDebitAchd) {
        this.txtSuspenseDebitAchd = txtSuspenseDebitAchd;
    }
    
    //------------------------------------------------
    
    void setTxtMinAmtLoan(String txtMinAmtLoan){
        this.txtMinAmtLoan = txtMinAmtLoan;
        setChanged();
    }
    String getTxtMinAmtLoan(){
        return this.txtMinAmtLoan;
    }
    
    void setTxtMaxAmtLoan(String txtMaxAmtLoan){
        this.txtMaxAmtLoan = txtMaxAmtLoan;
        setChanged();
    }
    String getTxtMaxAmtLoan(){
        return this.txtMaxAmtLoan;
    }
    
    void setTxtApplicableInter(String txtApplicableInter){
        this.txtApplicableInter = txtApplicableInter;
        setChanged();
    }
    String getTxtApplicableInter(){
        return this.txtApplicableInter;
    }//setTxtApplicableInter
    
    //    void setTxtMaxInter(String txtMaxInter){
    //        this.txtMaxInter = txtMaxInter;
    //        setChanged();
    //    }
    //    String getTxtMaxInter(){
    //        return this.txtMaxInter;
    //    }
    
    void setTxtMinInterDebited(String txtMinInterDebited){
        this.txtMinInterDebited = txtMinInterDebited;
        setChanged();
    }
    String getTxtMinInterDebited(){
        return this.txtMinInterDebited;
    }
    
    void setRdoSubsidy_Yes(boolean rdoSubsidy_Yes){
        this.rdoSubsidy_Yes = rdoSubsidy_Yes;
        setChanged();
    }
    boolean getRdoSubsidy_Yes(){
        return this.rdoSubsidy_Yes;
    }
    
    void setRdoSubsidy_No(boolean rdoSubsidy_No){
        this.rdoSubsidy_No = rdoSubsidy_No;
        setChanged();
    }
    boolean getRdoSubsidy_No(){
        return this.rdoSubsidy_No;
    }
    //------------------------------------------------
    
    void setCboLoanPeriodMul(String cboLoanPeriodMul){
        this.cboLoanPeriodMul = cboLoanPeriodMul;
        setChanged();
    }
    String getCboLoanPeriodMul(){
        return this.cboLoanPeriodMul;
    }
    public void setCbmLoanPeriodMul(ComboBoxModel cbmLoanPeriodMul){
        cbmLoanPeriodMul = cbmLoanPeriodMul;
        setChanged();
    }
    
    ComboBoxModel getCbmLoanPeriodMul(){
        return cbmLoanPeriodMul;
    }
    //------------------------------------------------
    
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public void resetStatus(){
        this.setLblStatus(ClientConstants.ACTION_STATUS[0]);
    }
    
    /** To set the status based on ActionType, either New, Edit, etc., */
    public void setStatus(){
        this.setLblStatus( ClientConstants.ACTION_STATUS[this.getActionType()]);
        ttNotifyObservers();
    }
    
    /** To update the Status based on result performed by doAction() method */
    public void setResultStatus(){
        this.setLblStatus(ClientConstants.RESULT_STATUS[this.getResult()]);
        ttNotifyObservers();
    }
    
    // To add the setter and getter for the Comboboxes and Combobox Models...
    
    //private ComboBoxModel cbmCharge_Limit2;
    //private ComboBoxModel cbmCharge_Limit3;
    
    
    void setCboCharge_Limit2(String cboCharge_Limit2){
        this.cboCharge_Limit2 = cboCharge_Limit2;
        setChanged();
    }
    String getCboCharge_Limit2(){
        return this.cboCharge_Limit2;
    }
    public void setCbmCharge_Limit2(ComboBoxModel cbmCharge_Limit2){
        cbmCharge_Limit2 = cbmCharge_Limit2;
        setChanged();
    }
    
    ComboBoxModel getCbmCharge_Limit2(){
        return cbmCharge_Limit2;
    }
    
    //    public void setCbmProdCurrency(ComboBoxModel cbmProdCurrency){
    //        cbmProdCurrency = cbmProdCurrency;
    //        setChanged();
    //    }
    //
    //    ComboBoxModel getCbmProdCurrency(){
    //        return cbmProdCurrency;
    //    }
    
    void setCboCharge_Limit3(String cboCharge_Limit3){
        this.cboCharge_Limit3 = cboCharge_Limit3;
        setChanged();
    }
    String getCboCharge_Limit3(){
        return this.cboCharge_Limit3;
    }
    public void setCbmCharge_Limit3(ComboBoxModel cbmCharge_Limit3){
        cbmCharge_Limit3 = cbmCharge_Limit3;
        setChanged();
    }
    
    ComboBoxModel getCbmCharge_Limit3(){
        return cbmCharge_Limit3;
    }
    
    // Setter method for txtProvisionStandardAssetss
    void setTxtProvisionStandardAssetss(String txtProvisionStandardAssetss){
        this.txtProvisionStandardAssetss = txtProvisionStandardAssetss;
        setChanged();
    }
    // Getter method for txtProvisionStandardAssetss
    String getTxtProvisionStandardAssetss(){
        return this.txtProvisionStandardAssetss;
    }
    
    // Setter method for txtProvisionLossAssets
    void setTxtProvisionLossAssets(String txtProvisionLossAssets){
        this.txtProvisionLossAssets = txtProvisionLossAssets;
        setChanged();
    }
    // Getter method for txtProvisionLossAssets
    String getTxtProvisionLossAssets(){
        return this.txtProvisionLossAssets;
    }
    // Documents Starts
    public void setCbmDocumentType(ComboBoxModel cbmDocumentType){
        cbmDocumentType = cbmDocumentType;
        setChanged();
    }
    
    ComboBoxModel getCbmDocumentType(){
        return cbmDocumentType;
    }
    // Setter method for txtDocumentNo
    void setTxtDocumentNo(String txtDocumentNo){
        this.txtDocumentNo = txtDocumentNo;
        setChanged();
    }
    // Getter method for txtDocumentNo
    String getTxtDocumentNo(){
        return this.txtDocumentNo;
    }
    
    // Setter method for txtDocumentDesc
    void setTxtDocumentDesc(String txtDocumentDesc){
        this.txtDocumentDesc = txtDocumentDesc;
        setChanged();
    }
    // Getter method for txtDocumentDesc
    String getTxtDocumentDesc(){
        return this.txtDocumentDesc;
    }
    
    // Setter method for documentSerialNo
    void setDocumentSerialNo(String documentSerialNo){
        this.documentSerialNo = documentSerialNo;
        setChanged();
    }
    // Getter method for documentSerialNo
    String getDocumentSerialNo(){
        return this.documentSerialNo;
    }
    
    // Setter method for documentStatus
    void setDocumentStatus(String documentStatus){
        this.documentStatus = documentStatus;
        setChanged();
    }
    // Getter method for documentStatus
    String getDocumentStatus(){
        return this.documentStatus;
    }
    
    // Setter method for cboDocumentType
    void setCboDocumentType(String cboDocumentType){
        this.cboDocumentType = cboDocumentType;
        setChanged();
    }
    // Getter method for cboDocumentType
    String getCboDocumentType(){
        return this.cboDocumentType;
    }
    
    // Documents Ends
    void setTxtRangeFrom(String txtRangeFrom){
        this.txtRangeFrom = txtRangeFrom;
        setChanged();
    }
    String getTxtRangeFrom(){
        return this.txtRangeFrom;
    }
    
    void setTxtRangeTo(String txtRangeTo){
        this.txtRangeTo = txtRangeTo;
        setChanged();
    }
    String getTxtRangeTo(){
        return this.txtRangeTo;
    }
    
    //    void setCboProdCurrency(String cboProdCurrency){
    //        this.cboProdCurrency = cboProdCurrency;
    //        setChanged();
    //    }
    //    String getCboProdCurrency(){
    //        return this.cboProdCurrency;
    //    }
    //
    
    
    void setCboCommodityCode(String cboCommodityCode){
        this.cboCommodityCode = cboCommodityCode;
        setChanged();
    }
    String getCboCommodityCode(){
        return this.cboCommodityCode;
    }
    
    void setCbmCommodityCode(ComboBoxModel cbmCommodityCode){
        this.cbmCommodityCode = cbmCommodityCode;
        setChanged();
    }
    ComboBoxModel getCbmCommodityCode(){
        return this.cbmCommodityCode;
    }
    
    void setCbmGuaranteeCoverCode(ComboBoxModel cbmGuaranteeCoverCode){
        this.cbmGuaranteeCoverCode = cbmGuaranteeCoverCode;
        setChanged();
    }
    ComboBoxModel getCbmGuaranteeCoverCode(){
        return this.cbmGuaranteeCoverCode;
    }
    
    void setCboGuaranteeCoverCode(String cboGuaranteeCoverCode){
        this.cboGuaranteeCoverCode = cboGuaranteeCoverCode;
        setChanged();
    }
    String getCboGuaranteeCoverCode(){
        return this.cboGuaranteeCoverCode;
    }
    
    void setCbmSectorCode(ComboBoxModel cbmSectorCode){
        this.cbmSectorCode = cbmSectorCode;
        setChanged();
    }
    ComboBoxModel getCbmSectorCode(){
        return this.cbmSectorCode;
    }
    
    void setCboSectorCode(String cboSectorCode){
        this.cboSectorCode = cboSectorCode;
        setChanged();
    }
    String getCboSectorCode(){
        return this.cboSectorCode;
    }
    
    void setCbmHealthCode(ComboBoxModel cbmHealthCode){
        this.cbmHealthCode = cbmHealthCode;
        setChanged();
    }
    ComboBoxModel getCbmHealthCode(){
        return this.cbmHealthCode;
    }
    
    void setCboHealthCode(String cboHealthCode){
        this.cboHealthCode = cboHealthCode;
        setChanged();
    }
    String getCboHealthCode(){
        return this.cboHealthCode;
    }
    
    void setCbmTypeFacility(ComboBoxModel cbmTypeFacility){
        this.cbmTypeFacility = cbmTypeFacility;
        setChanged();
    }
    ComboBoxModel getCbmTypeFacility(){
        return this.cbmTypeFacility;
    }
    
    void setCboTypeFacility(String cboTypeFacility){
        this.cboTypeFacility = cboTypeFacility;
        setChanged();
    }
    String getCboTypeFacility(){
        return this.cboTypeFacility;
    }
    
    void setCbmIndusCode(ComboBoxModel cbmIndusCode){
        this.cbmIndusCode = cbmIndusCode;
        setChanged();
    }
    ComboBoxModel getCbmIndusCode(){
        return this.cbmIndusCode;
    }
    
    void setCboIndusCode(String cboIndusCode){
        this.cboIndusCode = cboIndusCode;
        setChanged();
    }
    String getCboIndusCode(){
        return this.cboIndusCode;
    }
    
    void setCbm20Code(ComboBoxModel cbm20Code){
        this.cbm20Code = cbm20Code;
        setChanged();
    }
    ComboBoxModel getCbm20Code(){
        return this.cbm20Code;
    }
    
    void setCbo20Code(String cbo20Code){
        this.cbo20Code = cbo20Code;
        setChanged();
    }
    String getCbo20Code(){
        return this.cbo20Code;
    }
    
    void setCbmRefinancingInsti(ComboBoxModel cbmRefinancingInsti){
        this.cbmRefinancingInsti = cbmRefinancingInsti;
        setChanged();
    }
    ComboBoxModel getCbmRefinancingInsti(){
        return this.cbmRefinancingInsti;
    }
    
    void setCboRefinancingInsti(String cboRefinancingInsti){
        this.cboRefinancingInsti = cboRefinancingInsti;
        setChanged();
    }
    String getCboRefinancingInsti(){
        return this.cboRefinancingInsti;
    }
    
    void setCbmGovtSchemeCode(ComboBoxModel cbmGovtSchemeCode){
        this.cbmGovtSchemeCode = cbmGovtSchemeCode;
        setChanged();
    }
    ComboBoxModel getCbmGovtSchemeCode(){
        return this.cbmGovtSchemeCode;
    }
    
    void setCboGovtSchemeCode(String cboGovtSchemeCode){
        this.cboGovtSchemeCode = cboGovtSchemeCode;
        setChanged();
    }
    String getCboGovtSchemeCode(){
        return this.cboGovtSchemeCode;
    }
    
    void setCbmPurposeCode(ComboBoxModel cbmPurposeCode){
        this.cbmPurposeCode = cbmPurposeCode;
        setChanged();
    }
    ComboBoxModel getCbmPurposeCode(){
        return this.cbmPurposeCode;
    }
    
    void setCboPurposeCode(String cboPurposeCode){
        this.cboPurposeCode = cboPurposeCode;
        setChanged();
    }
    String getCboPurposeCode(){
        return this.cboPurposeCode;
    }
    //
    void setCbmSecurityDeails(ComboBoxModel cbmSecurityDeails){
        this.cbmSecurityDeails = cbmSecurityDeails;
        setChanged();
    }
    ComboBoxModel getCbmSecurityDeails(){
        return this.cbmSecurityDeails;
    }
    
    void setCboSecurityDeails(String cboSecurityDeails){
        this.cboSecurityDeails = cboSecurityDeails;
        setChanged();
    }
    String getCboSecurityDeails(){
        return this.cboSecurityDeails;
    }
    
    void setChkDirectFinance(boolean chkDirectFinance){
        this.chkDirectFinance = chkDirectFinance;
        setChanged();
    }
    boolean getChkDirectFinance(){
        return this.chkDirectFinance;
    }
    
    void setChkECGC(boolean chkECGC){
        this.chkECGC = chkECGC;
        setChanged();
    }
    boolean getChkECGC(){
        return this.chkECGC;
    }
    
    void setChkQIS(boolean chkQIS){
        this.chkQIS = chkQIS;
        setChanged();
    }
    
    boolean getChkQIS(){
        return this.chkQIS;
    }
    
    
    
    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,operationMap);
        }catch(Exception e){
            accountHead.setText("");
            parseException.logException(e,true);
        }
    }
    
    public boolean verifyProdId(String prodId) {
        boolean verify = false;
        try{
            final HashMap data = new HashMap();
            data.put("PROD_ID",prodId);
            final List resultList = ClientUtil.executeQuery("TermLoan.getProdId", data);
            if(resultList.size() > 0){
                verify = true;
            }
            else{
                verify = false;
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return verify;
    }
    
    public int getRowCount(){
        return tblChargeTab.getRowCount();
    }
    /**
     * Set Column values in the Document details table
     */
    private ArrayList setColumnValuesForDocumentsTable(int rowClicked,LoanProductDocumentsTO objLoanProductDocumentsTO) {
        ArrayList row = new ArrayList();
        row.add(String.valueOf(rowClicked));
        row.add((String) getCbmDocumentType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocType())));
        row.add(CommonUtil.convertObjToStr(objLoanProductDocumentsTO.getDocNo()));
        return row;
        
    }
    /**
     * Checking for duplication of document types
     */
    public boolean checkDocumentType(String prodId, String selectedDocType, String docNo, boolean press, int index) {
        boolean flag = false;
        try {
            if (((prodId.length()>0) && (prodId != null))&&((selectedDocType.length()>0) && (selectedDocType != null))&&((docNo.length()>0) && (docNo != null))) {
                int size = 0,count=0;
                if (documentsTO != null) {
                    size = documentsTO.size();
                    
                    for (int i=size;i>=1;--i) {
                        LoanProductDocumentsTO loanProductDocumentsTO = (LoanProductDocumentsTO) documentsTO.get(String.valueOf(i));
                        String docType = CommonUtil.convertObjToStr(loanProductDocumentsTO.getDocType());
                        String doc_No = CommonUtil.convertObjToStr(loanProductDocumentsTO.getDocNo());
                        String prod_Id = CommonUtil.convertObjToStr(loanProductDocumentsTO.getProdId());
                        
                        if ((press)&&(index>=0)&& (prodId.equals(prod_Id))&&(selectedDocType.equals(docType)) && (docNo.equals(doc_No))) {
                            if (String.valueOf(index+1).equals(String.valueOf(i))) {
                                // Nothing to do
                                flag = false;
                            } else {
                                flag = true;
                                break;
                                
                            }
                            
                        } else if ((!press)&& (prodId.equals(prod_Id)) && (selectedDocType.equals(docType)) && (docNo.equals(doc_No))) {
                            // Document Type and Document No are duplicated
                            flag = true;
                            break;
                            
                        }
                        
                        loanProductDocumentsTO = null;
                        docType = null;
                        doc_No = null;
                    }
                }
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    
    /**
     * Action Performed when Delete is pressed in Documents
     */
    public void deleteDocuments(int index) {
        log.info("deleteDocuments Invoked...");
        try {
            if (documentsTO != null) {
                LoanProductDocumentsTO loanProductDocumentsTO = (LoanProductDocumentsTO) documentsTO.remove(String.valueOf(index+1));
                if( ( loanProductDocumentsTO.getStatus().length()>0 ) && ( loanProductDocumentsTO.getStatus() != null ) && !(loanProductDocumentsTO.getStatus().equals(""))) {
                    if (deletedDocumentsTO == null)
                        deletedDocumentsTO = new LinkedHashMap();
                    deletedDocumentsTO.put(String.valueOf(deletedDocSI_No++), loanProductDocumentsTO);
                } else if (documentsTO != null) {
                    for(int i = index+1,j=documentsTO.size();i<=j;i++) {
                        documentsTO.put(String.valueOf(i),(LoanProductDocumentsTO)documentsTO.remove(String.valueOf((i+1))));
                    }
                    
                }
                loanProductDocumentsTO = null;
                docSI_No--;
                // Reset table data
                entireRows.remove(index);
                 /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
                for(int i=0,j = entireRows.size();i<j;i++){
                    ( (ArrayList) entireRows.get(i)).set(0,String.valueOf(i+1));
                }
                tblDocuments.setDataArrayList(entireRows,documentsTabTitle);
                
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
    
    public void deleteNoticeCharge(int index) {
        log.info("deleteDocuments Invoked...");
        try {
            if (NoticeTypeTO != null) {
                LoanProductChargesTabTO loanProductChargesTabTO = (LoanProductChargesTabTO) NoticeTypeTO.remove(String.valueOf(index+1));
                //                if( ( LoanProductChargesTabTO.getStatus().length()>0 ) && ( LoanProductChargesTabTO.getStatus() != null ) && !(LoanProductChargesTabTO.getStatus().equals(""))) {
                if (deletedDocumentsTO == null)
                    deletedNoticeType = new LinkedHashMap();
                deletedNoticeType.put(String.valueOf(deleted_NoticeCharge++), loanProductChargesTabTO);
                if (NoticeTypeTO != null) {
                    for(int i = index+1,j=NoticeTypeTO.size();i<=j;i++) {
                        NoticeTypeTO.put(String.valueOf(i),(LoanProductChargesTabTO)NoticeTypeTO.remove(String.valueOf((i+1))));
                    }
                    
                }
                loanProductChargesTabTO = null;
                deleted_NoticeCharge--;
                // Reset table data
                entireNoticeTypeRow.remove(index);
                 /* Orders the serial no in the arraylist (tableData) after the removal
               of selected Row in the table */
                System.out.println("DeleteNoticeCharge####"+NoticeTypeTO);
                //Commented By Suresh
//                for(int i=0,j = entireNoticeTypeRow.size();i<j;i++){
//                    ( (ArrayList) entireNoticeTypeRow.get(i)).set(0,String.valueOf(i+1));
//                }
                tblNoticeCharge.setDataArrayList(entireNoticeTypeRow,noticeChargeTabTitle);
                
            }
        } catch (Exception  e){
            parseException.logException(e,true);
        }
    }
    /**
     * populate the selected documents detaisl
     * Also check the status null or not
     */
    public boolean populateDocuments(int row) {
        log.info("populateDocuments Invoked...");
        boolean flag = false;
        try {
            LoanProductDocumentsTO loanProductDocumentsTO = (LoanProductDocumentsTO) documentsTO.get(String.valueOf(row+1));
            if ((loanProductDocumentsTO.getStatus() != null) && (loanProductDocumentsTO.getStatus().length() > 0) && !(loanProductDocumentsTO.getStatus().equals(""))) {
                flag = false;
            } else {
                flag = true;
            }
            setLoanProductDocumentsOB(loanProductDocumentsTO);
        } catch(Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * mandatory check for document fields
     */
    public boolean checkMandatoryForDocuments(String documentType, String documentNo) {
        log.info("checkMandatoryForDocuments Invoked...");
        boolean flag = false;
        try {
            if (documentType != null && documentNo != null && documentNo.length() > 0 && documentType.length() > 0) {
                flag = false;
            } else {
                flag = true;
            }
        } catch (Exception e) {
            parseException.logException(e,true);
        }
        return flag;
    }
    /**
     * Save Performed when save button in Documents in pressed
     */
    public void saveDocuments(boolean tableClick,int rowClicked) {
        log.info("saveDocuments Invoked...");
        try {
            LoanProductDocumentsTO objLoanProductDocumentsTO = setLoanProductDocumentsTO();
            if (documentsTO == null)
                documentsTO = new LinkedHashMap();
            if (entireRows == null)
                entireRows =  new ArrayList();
            if (tableClick) {
                objLoanProductDocumentsTO.setSINo(String.valueOf(rowClicked+1));
                entireRows.set(rowClicked, setColumnValuesForDocumentsTable(rowClicked+1, objLoanProductDocumentsTO));
                documentsTO.put(String.valueOf(rowClicked+1), objLoanProductDocumentsTO);
                setLoanProductDocumentsOB(objLoanProductDocumentsTO);
            } else {
                objLoanProductDocumentsTO.setSINo(String.valueOf(docSI_No));
                entireRows.add(setColumnValuesForDocumentsTable(docSI_No, objLoanProductDocumentsTO));
                documentsTO.put(String.valueOf(docSI_No),objLoanProductDocumentsTO);
                docSI_No++;
            }
            
            objLoanProductDocumentsTO = null;
            tblDocuments.setDataArrayList(entireRows,documentsTabTitle);
        } catch (Exception e) {
            parseException.logException(e,true);
        }
    }
    
    
    public void saveNoticeCharge(boolean tableClicked,int rowClick){
        try{
            LoanProductChargesTabTO objloanProductChargesTabTO=setLoanProductNoticeChargesTO();
            if(NoticeTypeTO==null)
                NoticeTypeTO=new LinkedHashMap();
            if(entireNoticeTypeRow==null)
                entireNoticeTypeRow=new ArrayList();
            if(tableClicked) {
                entireNoticeTypeRow.set(rowClick, setColumnValueNoticeCharge((rowClick+1), objloanProductChargesTabTO));
                NoticeTypeTO.put(String.valueOf(rowClick+1), objloanProductChargesTabTO);
                //                setLoanProductDocumentsOB(objLoanProductDocumentsTO);
                setLoanProductNoticeChargeOB(objloanProductChargesTabTO);
                
            }else{
                entireNoticeTypeRow.add(setColumnValueNoticeCharge(notice_Charge_No,objloanProductChargesTabTO));
                NoticeTypeTO.put(String.valueOf(notice_Charge_No),objloanProductChargesTabTO);
                notice_Charge_No++;
                System.out.println(entireNoticeTypeRow+":  notice value###ARRayList   :"+NoticeTypeTO);
            }
            
            objloanProductChargesTabTO=null;
            tblNoticeCharge.setDataArrayList(entireNoticeTypeRow, noticeChargeTabTitle);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public ArrayList setColumnValueNoticeCharge(int rowClicked,LoanProductChargesTabTO objLoanProductChargesTabTO){
        ArrayList row =new ArrayList();
        //Changed By Suresh
//        row.add((String) getCbmNoticeType().getDataForKey(objLoanProductChargesTabTO.getNoticeType()));
//        row.add((String)getCbmIssueAfter().getDataForKey(objLoanProductChargesTabTO.getIssueAfter()));
        row.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeType()));
        row.add(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getIssueAfter()));
        row.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getNoticeChargeAmt()));
        row.add(CommonUtil.convertObjToDouble(objLoanProductChargesTabTO.getPostageAmt()));
        return row;
        
        
        
    }
    
    public LoanProductChargesTabTO setLoanProductNoticeChargesTO(){
        
        LoanProductChargesTabTO objLoanProductChargesTabTO=new  LoanProductChargesTabTO();
        //Changed By Suresh
//        objLoanProductChargesTabTO.setNoticeType(CommonUtil.convertObjToStr(getCbmNoticeType().getKeyForSelected()));
//        objLoanProductChargesTabTO.setIssueAfter(CommonUtil.convertObjToStr(getCbmIssueAfter().getKeyForSelected()));
        objLoanProductChargesTabTO.setNoticeType(CommonUtil.convertObjToStr(getCboNoticeType()));
        objLoanProductChargesTabTO.setIssueAfter(CommonUtil.convertObjToStr(getCboIssueAfter()));
        objLoanProductChargesTabTO.setNoticeChargeAmt(new Double(Double.parseDouble(getTxtNoticeChargeAmt())));
        objLoanProductChargesTabTO.setPostageAmt(new Double(Double.parseDouble(getTxtPostageChargeAmt())));
        return objLoanProductChargesTabTO;
    }
    
    public void setLoanProductNoticeChargeOB(LoanProductChargesTabTO objLoanProductChargesTabTO){
        setCboNoticeType((String) getCbmNoticeType().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeType())));
        setCboIssueAfter((String) getCbmIssueAfter().getDataForKey(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getIssueAfter())));
        setTxtNoticeChargeAmt(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getNoticeChargeAmt()));
        setTxtProcessingCharges(CommonUtil.convertObjToStr(objLoanProductChargesTabTO.getPostageAmt()));
    }
    /**
     * Getter for property cbmNoticeType.
     * @return Value of property cbmNoticeType.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmNoticeType() {
        return cbmNoticeType;
    }
    
    /**
     * Setter for property cbmNoticeType.
     * @param cbmNoticeType New value of property cbmNoticeType.
     */
    public void setCbmNoticeType(com.see.truetransact.clientutil.ComboBoxModel cbmNoticeType) {
        this.cbmNoticeType = cbmNoticeType;
    }
    
    /**
     * Getter for property cbmIssueAfter.
     * @return Value of property cbmIssueAfter.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmIssueAfter() {
        return cbmIssueAfter;
    }
    
    /**
     * Setter for property cbmIssueAfter.
     * @param cbmIssueAfter New value of property cbmIssueAfter.
     */
    public void setCbmIssueAfter(com.see.truetransact.clientutil.ComboBoxModel cbmIssueAfter) {
        this.cbmIssueAfter = cbmIssueAfter;
    }
    
    /**
     * Getter for property cboNoticeType.
     * @return Value of property cboNoticeType.
     */
    public java.lang.String getCboNoticeType() {
        return cboNoticeType;
    }
    
    /**
     * Setter for property cboNoticeType.
     * @param cboNoticeType New value of property cboNoticeType.
     */
    public void setCboNoticeType(java.lang.String cboNoticeType) {
        this.cboNoticeType = cboNoticeType;
    }
    
    /**
     * Getter for property cboIssueAfter.
     * @return Value of property cboIssueAfter.
     */
    public java.lang.String getCboIssueAfter() {
        return cboIssueAfter;
    }
    
    /**
     * Setter for property cboIssueAfter.
     * @param cboIssueAfter New value of property cboIssueAfter.
     */
    public void setCboIssueAfter(java.lang.String cboIssueAfter) {
        this.cboIssueAfter = cboIssueAfter;
    }
    
    /**
     * Getter for property txtNoticeChargeAmt.
     * @return Value of property txtNoticeChargeAmt.
     */
    public java.lang.String getTxtNoticeChargeAmt() {
        return txtNoticeChargeAmt;
    }
    
    /**
     * Setter for property txtNoticeChargeAmt.
     * @param txtNoticeChargeAmt New value of property txtNoticeChargeAmt.
     */
    public void setTxtNoticeChargeAmt(java.lang.String txtNoticeChargeAmt) {
        this.txtNoticeChargeAmt = txtNoticeChargeAmt;
    }
    
    /**
     * Getter for property txtPostageChargeAmt.
     * @return Value of property txtPostageChargeAmt.
     */
    public java.lang.String getTxtPostageChargeAmt() {
        return txtPostageChargeAmt;
    }
    
    /**
     * Setter for property txtPostageChargeAmt.
     * @param txtPostageChargeAmt New value of property txtPostageChargeAmt.
     */
    public void setTxtPostageChargeAmt(java.lang.String txtPostageChargeAmt) {
        this.txtPostageChargeAmt = txtPostageChargeAmt;
    }
    
    /**
     * Getter for property tblNoticeCharge.
     * @return Value of property tblNoticeCharge.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblNoticeCharge() {
        return tblNoticeCharge;
    }
    
    /**
     * Setter for property tblNoticeCharge.
     * @param tblNoticeCharge New value of property tblNoticeCharge.
     */
    public void setTblNoticeCharge(com.see.truetransact.clientutil.EnhancedTableModel tblNoticeCharge) {
        this.tblNoticeCharge = tblNoticeCharge;
    }
    
    /**
     * Getter for property txtNoticeCharges.
     * @return Value of property txtNoticeCharges.
     */
    public java.lang.String getTxtNoticeCharges() {
        return txtNoticeCharges;
    }
    
    /**
     * Setter for property txtNoticeCharges.
     * @param txtNoticeCharges New value of property txtNoticeCharges.
     */
    public void setTxtNoticeCharges(java.lang.String txtNoticeCharges) {
        this.txtNoticeCharges = txtNoticeCharges;
    }
    
    /**
     * Getter for property txtPostageCharges.
     * @return Value of property txtPostageCharges.
     */
    public java.lang.String getTxtPostageCharges() {
        return txtPostageCharges;
    }
    
    /**
     * Setter for property txtPostageCharges.
     * @param txtPostageCharges New value of property txtPostageCharges.
     */
    public void setTxtPostageCharges(java.lang.String txtPostageCharges) {
        this.txtPostageCharges = txtPostageCharges;
    }
    
    /**
     * Getter for property txtIntPayableAccount.
     * @return Value of property txtIntPayableAccount.
     */
    public java.lang.String getTxtIntPayableAccount() {
        return txtIntPayableAccount;
    }
    
    /**
     * Setter for property txtIntPayableAccount.
     * @param txtIntPayableAccount New value of property txtIntPayableAccount.
     */
    public void setTxtIntPayableAccount(java.lang.String txtIntPayableAccount) {
        this.txtIntPayableAccount = txtIntPayableAccount;
    }
    
    /**
     * Getter for property rdoToChargeType_Credit.
     * @return Value of property rdoToChargeType_Credit.
     */
    public boolean isRdoToChargeType_Credit() {
        return rdoToChargeType_Credit;
    }
    
    /**
     * Setter for property rdoToChargeType_Credit.
     * @param rdoToChargeType_Credit New value of property rdoToChargeType_Credit.
     */
    public void setRdoToChargeType_Credit(boolean rdoToChargeType_Credit) {
        this.rdoToChargeType_Credit = rdoToChargeType_Credit;
    }
    
    /**
     * Getter for property rdoToChargeType_Debit.
     * @return Value of property rdoToChargeType_Debit.
     */
    public boolean isRdoToChargeType_Debit() {
        return rdoToChargeType_Debit;
    }
    
    /**
     * Setter for property rdoToChargeType_Debit.
     * @param rdoToChargeType_Debit New value of property rdoToChargeType_Debit.
     */
    public void setRdoToChargeType_Debit(boolean rdoToChargeType_Debit) {
        this.rdoToChargeType_Debit = rdoToChargeType_Debit;
    }
    
    /**
     * Getter for property rdoToChargeType_Both.
     * @return Value of property rdoToChargeType_Both.
     */
    public boolean isRdoToChargeType_Both() {
        return rdoToChargeType_Both;
    }
    
    /**
     * Setter for property rdoToChargeType_Both.
     * @param rdoToChargeType_Both New value of property rdoToChargeType_Both.
     */
    public void setRdoToChargeType_Both(boolean rdoToChargeType_Both) {
        this.rdoToChargeType_Both = rdoToChargeType_Both;
    }
    
    /**
     * Getter for property rdoasAndWhenCustomer_Yes.
     * @return Value of property rdoasAndWhenCustomer_Yes.
     */
    public boolean isRdoasAndWhenCustomer_Yes() {
        return rdoasAndWhenCustomer_Yes;
    }
    
    /**
     * Setter for property rdoasAndWhenCustomer_Yes.
     * @param rdoasAndWhenCustomer_Yes New value of property rdoasAndWhenCustomer_Yes.
     */
    public void setRdoasAndWhenCustomer_Yes(boolean rdoasAndWhenCustomer_Yes) {
        this.rdoasAndWhenCustomer_Yes = rdoasAndWhenCustomer_Yes;
    }
    
    /**
     * Getter for property rdoasAndWhenCustomer_No.
     * @return Value of property rdoasAndWhenCustomer_No.
     */
    public boolean isRdoasAndWhenCustomer_No() {
        return rdoasAndWhenCustomer_No;
    }
    
    /**
     * Setter for property rdoasAndWhenCustomer_No.
     * @param rdoasAndWhenCustomer_No New value of property rdoasAndWhenCustomer_No.
     */
    public void setRdoasAndWhenCustomer_No(boolean rdoasAndWhenCustomer_No) {
        this.rdoasAndWhenCustomer_No = rdoasAndWhenCustomer_No;
    }
    
    /**
     * Getter for property rdocalendarFrequency_Yes.
     * @return Value of property rdocalendarFrequency_Yes.
     */
    public boolean isRdocalendarFrequency_Yes() {
        return rdocalendarFrequency_Yes;
    }
    
    /**
     * Setter for property rdocalendarFrequency_Yes.
     * @param rdocalendarFrequency_Yes New value of property rdocalendarFrequency_Yes.
     */
    public void setRdocalendarFrequency_Yes(boolean rdocalendarFrequency_Yes) {
        this.rdocalendarFrequency_Yes = rdocalendarFrequency_Yes;
    }
    
    /**
     * Getter for property rdocalendarFrequency_No.
     * @return Value of property rdocalendarFrequency_No.
     */
    public boolean isRdocalendarFrequency_No() {
        return rdocalendarFrequency_No;
    }
    
    /**
     * Setter for property rdocalendarFrequency_No.
     * @param rdocalendarFrequency_No New value of property rdocalendarFrequency_No.
     */
    public void setRdocalendarFrequency_No(boolean rdocalendarFrequency_No) {
        this.rdocalendarFrequency_No = rdocalendarFrequency_No;
    }
    
    /**
     * Getter for property chkprincipalDue.
     * @return Value of property chkprincipalDue.
     */
    public boolean isChkprincipalDue() {
        return chkprincipalDue;
    }
    
    /**
     * Setter for property chkprincipalDue.
     * @param chkprincipalDue New value of property chkprincipalDue.
     */
    public void setChkprincipalDue(boolean chkprincipalDue) {
        this.chkprincipalDue = chkprincipalDue;
    }
    
    /**
     * Getter for property chkInterestDue.
     * @return Value of property chkInterestDue.
     */
    public boolean isChkInterestDue() {
        return chkInterestDue;
    }
    
    /**
     * Setter for property chkInterestDue.
     * @param chkInterestDue New value of property chkInterestDue.
     */
    public void setChkInterestDue(boolean chkInterestDue) {
        this.chkInterestDue = chkInterestDue;
    }
    
    /**
     * Getter for property txtLegalCharges.
     * @return Value of property txtLegalCharges.
     */
    public java.lang.String getTxtLegalCharges() {
        return txtLegalCharges;
    }
    
    /**
     * Setter for property txtLegalCharges.
     * @param txtLegalCharges New value of property txtLegalCharges.
     */
    public void setTxtLegalCharges(java.lang.String txtLegalCharges) {
        this.txtLegalCharges = txtLegalCharges;
    }
    
    /**
     * Getter for property txtMiscCharges.
     * @return Value of property txtMiscCharges.
     */
    public java.lang.String getTxtMiscCharges() {
        return txtMiscCharges;
    }
    
    /**
     * Setter for property txtMiscCharges.
     * @param txtMiscCharges New value of property txtMiscCharges.
     */
    public void setTxtMiscCharges(java.lang.String txtMiscCharges) {
        this.txtMiscCharges = txtMiscCharges;
    }
    
    /**
     * Getter for property txtArbitraryCharges.
     * @return Value of property txtArbitraryCharges.
     */
    public java.lang.String getTxtArbitraryCharges() {
        return txtArbitraryCharges;
    }
    
    /**
     * Setter for property txtArbitraryCharges.
     * @param txtArbitraryCharges New value of property txtArbitraryCharges.
     */
    public void setTxtArbitraryCharges(java.lang.String txtArbitraryCharges) {
        this.txtArbitraryCharges = txtArbitraryCharges;
    }
    
    /**
     * Getter for property txtInsuranceCharges.
     * @return Value of property txtInsuranceCharges.
     */
    public java.lang.String getTxtInsuranceCharges() {
        return txtInsuranceCharges;
    }
    
    /**
     * Setter for property txtInsuranceCharges.
     * @param txtInsuranceCharges New value of property txtInsuranceCharges.
     */
    public void setTxtInsuranceCharges(java.lang.String txtInsuranceCharges) {
        this.txtInsuranceCharges = txtInsuranceCharges;
    }
    
    /**
     * Getter for property txtExecutionDecreeCharges.
     * @return Value of property txtExecutionDecreeCharges.
     */
    public java.lang.String getTxtExecutionDecreeCharges() {
        return txtExecutionDecreeCharges;
    }
    
    /**
     * Setter for property txtExecutionDecreeCharges.
     * @param txtExecutionDecreeCharges New value of property txtExecutionDecreeCharges.
     */
    public void setTxtExecutionDecreeCharges(java.lang.String txtExecutionDecreeCharges) {
        this.txtExecutionDecreeCharges = txtExecutionDecreeCharges;
    }
    
    /**
     * Getter for property rdobill_Yes.
     * @return Value of property rdobill_Yes.
     */
    public boolean isRdobill_Yes() {
        return rdobill_Yes;
    }
    
    /**
     * Setter for property rdobill_Yes.
     * @param rdobill_Yes New value of property rdobill_Yes.
     */
    public void setRdobill_Yes(boolean rdobill_Yes) {
        this.rdobill_Yes = rdobill_Yes;
    }
    
    /**
     * Getter for property rdobill_NO.
     * @return Value of property rdobill_NO.
     */
    public boolean isRdobill_NO() {
        return rdobill_NO;
    }
    
    /**
     * Setter for property rdobill_NO.
     * @param rdobill_NO New value of property rdobill_NO.
     */
    public void setRdobill_NO(boolean rdobill_NO) {
        this.rdobill_NO = rdobill_NO;
    }
    /**
     * Getter for property cbmPeridFreq.
     * @return Value of property cbmPeridFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPeridFreq() {
        return cbmPeridFreq;
    }
    
    /**
     * Setter for property cbmPeridFreq.
     * @param cbmPeridFreq New value of property cbmPeridFreq.
     */
    public void setCbmPeridFreq(com.see.truetransact.clientutil.ComboBoxModel cbmPeridFreq) {
        this.cbmPeridFreq = cbmPeridFreq;
    }
    
    /**
     * Getter for property cboPeriodFreq.
     * @return Value of property cboPeriodFreq.
     */
    public java.lang.String getCboPeriodFreq() {
        return cboPeriodFreq;
    }

    public double getTxtMaxCashDisb() {
        return txtMaxCashDisb;
    }

    public void setTxtMaxCashDisb(double txtMaxCashDisb) {
        this.txtMaxCashDisb = txtMaxCashDisb;
    }

   
    /**
     * Setter for property cboPeriodFreq.
     * @param cboPeriodFreq New value of property cboPeriodFreq.
     */
    public void setCboPeriodFreq(java.lang.String cboPeriodFreq) {
        this.cboPeriodFreq = cboPeriodFreq;
    }
    
    /**
     * Getter for property cbmPrematureIntCalAmt.
     * @return Value of property cbmPrematureIntCalAmt.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPrematureIntCalAmt() {
        return cbmPrematureIntCalAmt;
    }
    
    /**
     * Setter for property cbmPrematureIntCalAmt.
     * @param cbmPrematureIntCalAmt New value of property cbmPrematureIntCalAmt.
     */
    public void setCbmPrematureIntCalAmt(com.see.truetransact.clientutil.ComboBoxModel cbmPrematureIntCalAmt) {
        this.cbmPrematureIntCalAmt = cbmPrematureIntCalAmt;
    }
    
    /**
     * Getter for property cboPrematureIntCalAmt.
     * @return Value of property cboPrematureIntCalAmt.
     */
    public java.lang.String getCboPrematureIntCalAmt() {
        return cboPrematureIntCalAmt;
    }
    
    /**
     * Setter for property cboPrematureIntCalAmt.
     * @param cboPrematureIntCalAmt New value of property cboPrematureIntCalAmt.
     */
    public void setCboPrematureIntCalAmt(java.lang.String cboPrematureIntCalAmt) {
        this.cboPrematureIntCalAmt = cboPrematureIntCalAmt;
    }
    
    /**
     * Getter for property txtPreMatIntCalctPeriod.
     * @return Value of property txtPreMatIntCalctPeriod.
     */
    public java.lang.String getTxtPreMatIntCalctPeriod() {
        return txtPreMatIntCalctPeriod;
    }
    
    /**
     * Setter for property txtPreMatIntCalctPeriod.
     * @param txtPreMatIntCalctPeriod New value of property txtPreMatIntCalctPeriod.
     */
    public void setTxtPreMatIntCalctPeriod(java.lang.String txtPreMatIntCalctPeriod) {
        this.txtPreMatIntCalctPeriod = txtPreMatIntCalctPeriod;
    }
    
    /**
     * Getter for property depositList.
     * @return Value of property depositList.
     */
    public java.util.List getDepositList() {
        return depositList;
    }
    
    /**
     * Setter for property depositList.
     * @param depositList New value of property depositList.
     */
    public void setDepositList(java.util.List depositList) {
        this.depositList = depositList;
    }
    
    /**
     * Getter for property lstAvailableDeposits.
     * @return Value of property lstAvailableDeposits.
     */
    public javax.swing.DefaultListModel getLstAvailableDeposits() {
        return lstAvailableDeposits;
    }
    
    /**
     * Setter for property lstAvailableDeposits.
     * @param lstAvailableDeposits New value of property lstAvailableDeposits.
     */
    public void setLstAvailableDeposits(javax.swing.DefaultListModel lstAvailableDeposits) {
        this.lstAvailableDeposits = lstAvailableDeposits;
    }
    
    /**
     * Getter for property lstSelectedDeposits.
     * @return Value of property lstSelectedDeposits.
     */
    public javax.swing.DefaultListModel getLstSelectedDeposits() {
        return lstSelectedDeposits;
    }
    
    /**
     * Setter for property lstSelectedDeposits.
     * @param lstSelectedDeposits New value of property lstSelectedDeposits.
     */
    public void setLstSelectedDeposits(javax.swing.DefaultListModel lstSelectedDeposits) {
        this.lstSelectedDeposits = lstSelectedDeposits;
    }
    
    /**
     * Getter for property cbmReviewPeriod.
     * @return Value of property cbmReviewPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmReviewPeriod() {
        return cbmReviewPeriod;
    }
    
    /**
     * Setter for property cbmReviewPeriod.
     * @param cbmReviewPeriod New value of property cbmReviewPeriod.
     */
    public void setCbmReviewPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmReviewPeriod) {
        this.cbmReviewPeriod = cbmReviewPeriod;
    }
    
    /**
     * Getter for property cboReviewPeriod.
     * @return Value of property cboReviewPeriod.
     */
    public java.lang.String getCboReviewPeriod() {
        return cboReviewPeriod;
    }
    
    /**
     * Setter for property cboReviewPeriod.
     * @param cboReviewPeriod New value of property cboReviewPeriod.
     */
    public void setCboReviewPeriod(java.lang.String cboReviewPeriod) {
        this.cboReviewPeriod = cboReviewPeriod;
    }
    
    /**
     * Getter for property txtReviewPeriod.
     * @return Value of property txtReviewPeriod.
     */
    public java.lang.String getTxtReviewPeriod() {
        return txtReviewPeriod;
    }
    
    /**
     * Setter for property txtReviewPeriod.
     * @param txtReviewPeriod New value of property txtReviewPeriod.
     */
    public void setTxtReviewPeriod(java.lang.String txtReviewPeriod) {
        this.txtReviewPeriod = txtReviewPeriod;
    }
    
    /**
     * Getter for property txtElgLoanAmt.
     * @return Value of property txtElgLoanAmt.
     */
    public java.lang.String getTxtElgLoanAmt() {
        return txtElgLoanAmt;
    }
    
    /**
     * Setter for property txtElgLoanAmt.
     * @param txtElgLoanAmt New value of property txtElgLoanAmt.
     */
    public void setTxtElgLoanAmt(java.lang.String txtElgLoanAmt) {
        this.txtElgLoanAmt = txtElgLoanAmt;
    }
    
    /**
     * Getter for property existDepositMap.
     * @return Value of property existDepositMap.
     */
    public java.util.LinkedHashMap getExistDepositMap() {
        return existDepositMap;
    }
    
    /**
     * Setter for property existDepositMap.
     * @param existDepositMap New value of property existDepositMap.
     */
    public void setExistDepositMap(java.util.LinkedHashMap existDepositMap) {
        this.existDepositMap = existDepositMap;
    }
    
    /**
     * Getter for property deleteDepositMap.
     * @return Value of property deleteDepositMap.
     */
    public java.util.LinkedHashMap getDeleteDepositMap() {
        return deleteDepositMap;
    }
    
    /**
     * Setter for property deleteDepositMap.
     * @param deleteDepositMap New value of property deleteDepositMap.
     */
    public void setDeleteDepositMap(java.util.LinkedHashMap deleteDepositMap) {
        this.deleteDepositMap = deleteDepositMap;
    }
    
    /**
     * Getter for property newDepositMap.
     * @return Value of property newDepositMap.
     */
    public java.util.LinkedHashMap getNewDepositMap() {
        return newDepositMap;
    }
    
    /**
     * Setter for property newDepositMap.
     * @param newDepositMap New value of property newDepositMap.
     */
    public void setNewDepositMap(java.util.LinkedHashMap newDepositMap) {
        this.newDepositMap = newDepositMap;
    }
    
    /**
     * Getter for property cbmDepositRoundOff.
     * @return Value of property cbmDepositRoundOff.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepositRoundOff() {
        return cbmDepositRoundOff;
    }
    
    /**
     * Setter for property cbmDepositRoundOff.
     * @param cbmDepositRoundOff New value of property cbmDepositRoundOff.
     */
    public void setCbmDepositRoundOff(com.see.truetransact.clientutil.ComboBoxModel cbmDepositRoundOff) {
        this.cbmDepositRoundOff = cbmDepositRoundOff;
    }
    
    /**
     * Getter for property cboDepositRoundOff.
     * @return Value of property cboDepositRoundOff.
     */
    public java.lang.String getCboDepositRoundOff() {
        return cboDepositRoundOff;
    }
    
    /**
     * Setter for property cboDepositRoundOff.
     * @param cboDepositRoundOff New value of property cboDepositRoundOff.
     */
    public void setCboDepositRoundOff(java.lang.String cboDepositRoundOff) {
        this.cboDepositRoundOff = cboDepositRoundOff;
    }
    
    /**
     * Getter for property txtArcCostAccount.
     * @return Value of property txtArcCostAccount.
     */
    public java.lang.String getTxtArcCostAccount() {
        return txtArcCostAccount;
    }
    
    /**
     * Setter for property txtArcCostAccount.
     * @param txtArcCostAccount New value of property txtArcCostAccount.
     */
    public void setTxtArcCostAccount(java.lang.String txtArcCostAccount) {
        this.txtArcCostAccount = txtArcCostAccount;
    }
    
    /**
     * Getter for property txtArcExpenseAccount.
     * @return Value of property txtArcExpenseAccount.
     */
    public java.lang.String getTxtArcExpenseAccount() {
        return txtArcExpenseAccount;
    }
    
    /**
     * Setter for property txtArcExpenseAccount.
     * @param txtArcExpenseAccount New value of property txtArcExpenseAccount.
     */
    public void setTxtArcExpenseAccount(java.lang.String txtArcExpenseAccount) {
        this.txtArcExpenseAccount = txtArcExpenseAccount;
    }
    
    /**
     * Getter for property txtEaCostAccount.
     * @return Value of property txtEaCostAccount.
     */
    public java.lang.String getTxtEaCostAccount() {
        return txtEaCostAccount;
    }
    
    /**
     * Setter for property txtEaCostAccount.
     * @param txtEaCostAccount New value of property txtEaCostAccount.
     */
    public void setTxtEaCostAccount(java.lang.String txtEaCostAccount) {
        this.txtEaCostAccount = txtEaCostAccount;
    }
    
    /**
     * Getter for property txtEaExpenseAccount.
     * @return Value of property txtEaExpenseAccount.
     */
    public java.lang.String getTxtEaExpenseAccount() {
        return txtEaExpenseAccount;
    }
    
    /**
     * Setter for property txtEaExpenseAccount.
     * @param txtEaExpenseAccount New value of property txtEaExpenseAccount.
     */
    public void setTxtEaExpenseAccount(java.lang.String txtEaExpenseAccount) {
        this.txtEaExpenseAccount = txtEaExpenseAccount;
    }
    
    /**
     * Getter for property txtEpCostAccount.
     * @return Value of property txtEpCostAccount.
     */
    public java.lang.String getTxtEpCostAccount() {
        return txtEpCostAccount;
    }
    
    /**
     * Setter for property txtEpCostAccount.
     * @param txtEpCostAccount New value of property txtEpCostAccount.
     */
    public void setTxtEpCostAccount(java.lang.String txtEpCostAccount) {
        this.txtEpCostAccount = txtEpCostAccount;
    }
    
    /**
     * Getter for property txtEpExpenseAccount.
     * @return Value of property txtEpExpenseAccount.
     */
    public java.lang.String getTxtEpExpenseAccount() {
        return txtEpExpenseAccount;
    }
    
    /**
     * Setter for property txtEpExpenseAccount.
     * @param txtEpExpenseAccount New value of property txtEpExpenseAccount.
     */
    public void setTxtEpExpenseAccount(java.lang.String txtEpExpenseAccount) {
        this.txtEpExpenseAccount = txtEpExpenseAccount;
    }
    
    /**
     * Getter for property txtPreMatIntCollectPeriod.
     * @return Value of property txtPreMatIntCollectPeriod.
     */
    public java.lang.String getTxtPreMatIntCollectPeriod() {
        return txtPreMatIntCollectPeriod;
    }
    
    /**
     * Setter for property txtPreMatIntCollectPeriod.
     * @param txtPreMatIntCollectPeriod New value of property txtPreMatIntCollectPeriod.
     */
    public void setTxtPreMatIntCollectPeriod(java.lang.String txtPreMatIntCollectPeriod) {
        this.txtPreMatIntCollectPeriod = txtPreMatIntCollectPeriod;
    }
    
    /**
     * Getter for property cbmPrematureIntCalcFreq.
     * @return Value of property cbmPrematureIntCalcFreq.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPrematureIntCalcFreq() {
        return cbmPrematureIntCalcFreq;
    }
    
    /**
     * Setter for property cbmPrematureIntCalcFreq.
     * @param cbmPrematureIntCalcFreq New value of property cbmPrematureIntCalcFreq.
     */
    public void setCbmPrematureIntCalcFreq(com.see.truetransact.clientutil.ComboBoxModel cbmPrematureIntCalcFreq) {
        this.cbmPrematureIntCalcFreq = cbmPrematureIntCalcFreq;
    }
    
    /**
     * Getter for property cboPrematureIntCalcFreq.
     * @return Value of property cboPrematureIntCalcFreq.
     */
    public java.lang.String getCboPrematureIntCalcFreq() {
        return cboPrematureIntCalcFreq;
    }
    
    /**
     * Setter for property cboPrematureIntCalcFreq.
     * @param cboPrematureIntCalcFreq New value of property cboPrematureIntCalcFreq.
     */
    public void setCboPrematureIntCalcFreq(java.lang.String cboPrematureIntCalcFreq) {
        this.cboPrematureIntCalcFreq = cboPrematureIntCalcFreq;
    }
    
    /**
     * Getter for property cbmProdCategory.
     * @return Value of property cbmProdCategory.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmProdCategory() {
        return cbmProdCategory;
    }
    
    /**
     * Setter for property cbmProdCategory.
     * @param cbmProdCategory New value of property cbmProdCategory.
     */
    public void setCbmProdCategory(com.see.truetransact.clientutil.ComboBoxModel cbmProdCategory) {
        this.cbmProdCategory = cbmProdCategory;
    }
    
    /**
     * Getter for property rdoMaturity_Yes.
     * @return Value of property rdoMaturity_Yes.
     */
    public boolean isRdoMaturity_Yes() {
        return rdoMaturity_Yes;
    }
    
    /**
     * Setter for property rdoMaturity_Yes.
     * @param rdoMaturity_Yes New value of property rdoMaturity_Yes.
     */
    public void setRdoMaturity_Yes(boolean rdoMaturity_Yes) {
        this.rdoMaturity_Yes = rdoMaturity_Yes;
    }
    
    /**
     * Getter for property rdoCurrDt_Yes.
     * @return Value of property rdoCurrDt_Yes.
     */
    public boolean isRdoCurrDt_Yes() {
        return rdoCurrDt_Yes;
    }
    
    /**
     * Setter for property rdoCurrDt_Yes.
     * @param rdoCurrDt_Yes New value of property rdoCurrDt_Yes.
     */
    public void setRdoCurrDt_Yes(boolean rdoCurrDt_Yes) {
        this.rdoCurrDt_Yes = rdoCurrDt_Yes;
    }
       /**
     * Getter for property lstAvailableTransaction.
     * @return Value of property lstAvailableTransaction.
     */
    public javax.swing.DefaultListModel getLstAvailableTransaction() {
        return lstAvailableTransaction;
    }
    
    /**
     * Setter for property lstAvailableTransaction.
     * @param lstAvailableTransaction New value of property lstAvailableTransaction.
     */
    public void setLstAvailableTransaction(javax.swing.DefaultListModel lstAvailableTransaction) {
        this.lstAvailableTransaction = lstAvailableTransaction;
    }
    
    /**
     * Getter for property lstSelectedTransaction.
     * @return Value of property lstSelectedTransaction.
     */
    public javax.swing.DefaultListModel getLstSelectedTransaction() {
        return lstSelectedTransaction;
    }
    
    /**
     * Setter for property lstSelectedTransaction.
     * @param lstSelectedTransaction New value of property lstSelectedTransaction.
     */
    public void setLstSelectedTransaction(javax.swing.DefaultListModel lstSelectedTransaction) {
        this.lstSelectedTransaction = lstSelectedTransaction;
    }
    
    /**
     * Getter for property rdoMaturity_Deposit_closure_Yes.
     * @return Value of property rdoMaturity_Deposit_closure_Yes.
     */
    public boolean isRdoMaturity_Deposit_closure_Yes() {
        return rdoMaturity_Deposit_closure_Yes;
    }
    
    /**
     * Setter for property rdoMaturity_Deposit_closure_Yes.
     * @param rdoMaturity_Deposit_closure_Yes New value of property rdoMaturity_Deposit_closure_Yes.
     */
    public void setRdoMaturity_Deposit_closure_Yes(boolean rdoMaturity_Deposit_closure_Yes) {
        this.rdoMaturity_Deposit_closure_Yes = rdoMaturity_Deposit_closure_Yes;
    }
    
    /**
     * Getter for property rdoMaturity_Deposit_closure_Upto_Curr_Yes.
     * @return Value of property rdoMaturity_Deposit_closure_Upto_Curr_Yes.
     */
    public boolean isRdoMaturity_Deposit_closure_Upto_Curr_Yes() {
        return rdoMaturity_Deposit_closure_Upto_Curr_Yes;
    }
    
    /**
     * Setter for property rdoMaturity_Deposit_closure_Upto_Curr_Yes.
     * @param rdoMaturity_Deposit_closure_Upto_Curr_Yes New value of property rdoMaturity_Deposit_closure_Upto_Curr_Yes.
     */
    public void setRdoMaturity_Deposit_closure_Upto_Curr_Yes(boolean rdoMaturity_Deposit_closure_Upto_Curr_Yes) {
        this.rdoMaturity_Deposit_closure_Upto_Curr_Yes = rdoMaturity_Deposit_closure_Upto_Curr_Yes;
    }
    
    /**
     * Getter for property txtGracePeriodPenalInterest.
     * @return Value of property txtGracePeriodPenalInterest.
     */
    public java.lang.String getTxtGracePeriodPenalInterest() {
        return txtGracePeriodPenalInterest;
    }
    
    /**
     * Setter for property txtGracePeriodPenalInterest.
     * @param txtGracePeriodPenalInterest New value of property txtGracePeriodPenalInterest.
     */
    public void setTxtGracePeriodPenalInterest(java.lang.String txtGracePeriodPenalInterest) {
        this.txtGracePeriodPenalInterest = txtGracePeriodPenalInterest;
    }
    
    /**
     * Getter for property rdoLoanBalance_Yes.
     * @return Value of property rdoLoanBalance_Yes.
     */
    public boolean isRdoLoanBalance_Yes() {
        return rdoLoanBalance_Yes;
    }
    
    /**
     * Setter for property rdoLoanBalance_Yes.
     * @param rdoLoanBalance_Yes New value of property rdoLoanBalance_Yes.
     */
    public void setRdoLoanBalance_Yes(boolean rdoLoanBalance_Yes) {
        this.rdoLoanBalance_Yes = rdoLoanBalance_Yes;
    }
    
  
    
    /**
     * Getter for property rdoSubsidyReceivedDate.
     * @return Value of property rdoSubsidyReceivedDate.
     */
    public boolean isRdoSubsidyReceivedDate() {
        return rdoSubsidyReceivedDate;
    }
    
   
    
   
    
    /**
     * Getter for property rdoInterestRebateAllowed_Yes.
     * @return Value of property rdoInterestRebateAllowed_Yes.
     */
    public boolean isRdoInterestRebateAllowed_Yes() {
        return rdoInterestRebateAllowed_Yes;
    }
    
    /**
     * Setter for property rdoInterestRebateAllowed_Yes.
     * @param rdoInterestRebateAllowed_Yes New value of property rdoInterestRebateAllowed_Yes.
     */
    public void setRdoInterestRebateAllowed_Yes(boolean rdoInterestRebateAllowed_Yes) {
        this.rdoInterestRebateAllowed_Yes = rdoInterestRebateAllowed_Yes;
    }
    
   
    
    /**
     * Getter for property txtInterestRebatePercentage.
     * @return Value of property txtInterestRebatePercentage.
     */
    public java.lang.String getTxtInterestRebatePercentage() {
        return txtInterestRebatePercentage;
    }
    
    /**
     * Setter for property txtInterestRebatePercentage.
     * @param txtInterestRebatePercentage New value of property txtInterestRebatePercentage.
     */
    public void setTxtInterestRebatePercentage(java.lang.String txtInterestRebatePercentage) {
        this.txtInterestRebatePercentage = txtInterestRebatePercentage;
    }
    
    /**
     * Getter for property txtFinYearStartingFromDD.
     * @return Value of property txtFinYearStartingFromDD.
     */
    public java.lang.String getTxtFinYearStartingFromDD() {
        return txtFinYearStartingFromDD;
    }
    
    /**
     * Setter for property txtFinYearStartingFromDD.
     * @param txtFinYearStartingFromDD New value of property txtFinYearStartingFromDD.
     */
    public void setTxtFinYearStartingFromDD(java.lang.String txtFinYearStartingFromDD) {
        this.txtFinYearStartingFromDD = txtFinYearStartingFromDD;
    }
    
    /**
     * Getter for property txtFinYearStartingFromMM.
     * @return Value of property txtFinYearStartingFromMM.
     */
    public java.lang.String getTxtFinYearStartingFromMM() {
        return txtFinYearStartingFromMM;
    }
    
    /**
     * Setter for property txtFinYearStartingFromMM.
     * @param txtFinYearStartingFromMM New value of property txtFinYearStartingFromMM.
     */
    public void setTxtFinYearStartingFromMM(java.lang.String txtFinYearStartingFromMM) {
        this.txtFinYearStartingFromMM = txtFinYearStartingFromMM;
    }
    
    /**
     * Getter for property rdoPenalInterestWaiverAllowed_Yes.
     * @return Value of property rdoPenalInterestWaiverAllowed_Yes.
     */
    public boolean isRdoPenalInterestWaiverAllowed_Yes() {
        return rdoPenalInterestWaiverAllowed_Yes;
    }
    
    /**
     * Setter for property rdoPenalInterestWaiverAllowed_Yes.
     * @param rdoPenalInterestWaiverAllowed_Yes New value of property rdoPenalInterestWaiverAllowed_Yes.
     */
    public void setRdoPenalInterestWaiverAllowed_Yes(boolean rdoPenalInterestWaiverAllowed_Yes) {
        this.rdoPenalInterestWaiverAllowed_Yes = rdoPenalInterestWaiverAllowed_Yes;
    }
    
  
    
    /**
     * Getter for property rdoInterestWaiverAllowed_Yes.
     * @return Value of property rdoInterestWaiverAllowed_Yes.
     */
    public boolean isRdoInterestWaiverAllowed_Yes() {
        return rdoInterestWaiverAllowed_Yes;
    }
    
    /**
     * Setter for property rdoInterestWaiverAllowed_Yes.
     * @param rdoInterestWaiverAllowed_Yes New value of property rdoInterestWaiverAllowed_Yes.
     */
    public void setRdoInterestWaiverAllowed_Yes(boolean rdoInterestWaiverAllowed_Yes) {
        this.rdoInterestWaiverAllowed_Yes = rdoInterestWaiverAllowed_Yes;
    }
    
   
    
    /**
     * Getter for property cbmInterestRebateCalculation.
     * @return Value of property cbmInterestRebateCalculation.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInterestRebateCalculation() {
        return cbmInterestRebateCalculation;
    }
    
    /**
     * Setter for property cbmInterestRebateCalculation.
     * @param cbmInterestRebateCalculation New value of property cbmInterestRebateCalculation.
     */
    public void setCbmInterestRebateCalculation(com.see.truetransact.clientutil.ComboBoxModel cbmInterestRebateCalculation) {
        this.cbmInterestRebateCalculation = cbmInterestRebateCalculation;
    }
    
    /**
     * Getter for property cbmRebatePeriod.
     * @return Value of property cbmRebatePeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRebatePeriod() {
        return cbmRebatePeriod;
    }
    
    /**
     * Setter for property cbmRebatePeriod.
     * @param cbmRebatePeriod New value of property cbmRebatePeriod.
     */
    public void setCbmRebatePeriod(com.see.truetransact.clientutil.ComboBoxModel cbmRebatePeriod) {
        this.cbmRebatePeriod = cbmRebatePeriod;
    }
    
    /**
     * Getter for property rdoSubsidy_Yes.
     * @return Value of property rdoSubsidy_Yes.
     */
    public boolean isRdoSubsidy_Yes() {
        return rdoSubsidy_Yes;
    }
    
    /**
     * Getter for property rdoSubsidyAdjustLoanBalance_Yes.
     * @return Value of property rdoSubsidyAdjustLoanBalance_Yes.
     */
    public boolean isRdoSubsidyAdjustLoanBalance_Yes() {
        return rdoSubsidyAdjustLoanBalance_Yes;
    }
    
    /**
     * Setter for property rdoSubsidyAdjustLoanBalance_Yes.
     * @param rdoSubsidyAdjustLoanBalance_Yes New value of property rdoSubsidyAdjustLoanBalance_Yes.
     */
    public void setRdoSubsidyAdjustLoanBalance_Yes(boolean rdoSubsidyAdjustLoanBalance_Yes) {
        this.rdoSubsidyAdjustLoanBalance_Yes = rdoSubsidyAdjustLoanBalance_Yes;
    }
    
    /**
     * Getter for property rdoLoanOpenDate.
     * @return Value of property rdoLoanOpenDate.
     */
    public boolean isRdoLoanOpenDate() {
        return rdoLoanOpenDate;
    }
    
    /**
     * Setter for property rdoLoanOpenDate.
     * @param rdoLoanOpenDate New value of property rdoLoanOpenDate.
     */
    public void setRdoLoanOpenDate(boolean rdoLoanOpenDate) {
        this.rdoLoanOpenDate = rdoLoanOpenDate;
    }
    
    /**
     * Getter for property rdoPenalInterestWaiverAllowed_No.
     * @return Value of property rdoPenalInterestWaiverAllowed_No.
     */
    public boolean isRdoPenalInterestWaiverAllowed_No() {
        return rdoPenalInterestWaiverAllowed_No;
    }
    
    /**
     * Setter for property rdoPenalInterestWaiverAllowed_No.
     * @param rdoPenalInterestWaiverAllowed_No New value of property rdoPenalInterestWaiverAllowed_No.
     */
    public void setRdoPenalInterestWaiverAllowed_No(boolean rdoPenalInterestWaiverAllowed_No) {
        this.rdoPenalInterestWaiverAllowed_No = rdoPenalInterestWaiverAllowed_No;
    }
    
    /**
     * Getter for property rdoInterestWaiverAllowed_No.
     * @return Value of property rdoInterestWaiverAllowed_No.
     */
    public boolean isRdoInterestWaiverAllowed_No() {
        return rdoInterestWaiverAllowed_No;
    }
    
    /**
     * Setter for property rdoInterestWaiverAllowed_No.
     * @param rdoInterestWaiverAllowed_No New value of property rdoInterestWaiverAllowed_No.
     */
    public void setRdoInterestWaiverAllowed_No(boolean rdoInterestWaiverAllowed_No) {
        this.rdoInterestWaiverAllowed_No = rdoInterestWaiverAllowed_No;
    }
    
    /**
     * Getter for property rdoInterestRebateAllowed_No.
     * @return Value of property rdoInterestRebateAllowed_No.
     */
    public boolean isRdoInterestRebateAllowed_No() {
        return rdoInterestRebateAllowed_No;
    }
    
    /**
     * Setter for property rdoInterestRebateAllowed_No.
     * @param rdoInterestRebateAllowed_No New value of property rdoInterestRebateAllowed_No.
     */
    public void setRdoInterestRebateAllowed_No(boolean rdoInterestRebateAllowed_No) {
        this.rdoInterestRebateAllowed_No = rdoInterestRebateAllowed_No;
    }
    
    /**
     * Setter for property rdoSubsidyReceivedDate.
     * @param rdoSubsidyReceivedDate New value of property rdoSubsidyReceivedDate.
     */
    public void setRdoSubsidyReceivedDate(boolean rdoSubsidyReceivedDate) {
        this.rdoSubsidyReceivedDate = rdoSubsidyReceivedDate;
    }
    
    /**
     * Getter for property txtDebitIntDiscount.
     * @return Value of property txtDebitIntDiscount.
     */
    public java.lang.String getTxtDebitIntDiscount() {
        return txtDebitIntDiscount;
    }
    
    /**
     * Setter for property txtDebitIntDiscount.
     * @param txtDebitIntDiscount New value of property txtDebitIntDiscount.
     */
    public void setTxtDebitIntDiscount(java.lang.String txtDebitIntDiscount) {
        this.txtDebitIntDiscount = txtDebitIntDiscount;
    }
    
    /**
     * Getter for property txtRebateInterest.
     * @return Value of property txtRebateInterest.
     */
    public java.lang.String getTxtRebateInterest() {
        return txtRebateInterest;
    }
    
    /**
     * Setter for property txtRebateInterest.
     * @param txtRebateInterest New value of property txtRebateInterest.
     */
    public void setTxtRebateInterest(java.lang.String txtRebateInterest) {
        this.txtRebateInterest = txtRebateInterest;
    }
    
    /**
     * Getter for property chkCreditStampAdvance.
     * @return Value of property chkCreditStampAdvance.
     */
    public boolean isChkCreditStampAdvance() {
        return chkCreditStampAdvance;
    }
    
    /**
     * Setter for property chkCreditStampAdvance.
     * @param chkCreditStampAdvance New value of property chkCreditStampAdvance.
     */
    public void setChkCreditStampAdvance(boolean chkCreditStampAdvance) {
        this.chkCreditStampAdvance = chkCreditStampAdvance;
    }
    
    /**
     * Getter for property txtStampAdvancesHead.
     * @return Value of property txtStampAdvancesHead.
     */
    public java.lang.String getTxtStampAdvancesHead() {
        return txtStampAdvancesHead;
    }
    
    /**
     * Setter for property txtStampAdvancesHead.
     * @param txtStampAdvancesHead New value of property txtStampAdvancesHead.
     */
    public void setTxtStampAdvancesHead(java.lang.String txtStampAdvancesHead) {
        this.txtStampAdvancesHead = txtStampAdvancesHead;
    }
    
    /**
     * Getter for property txtAdvertisementHead.
     * @return Value of property txtAdvertisementHead.
     */
    public java.lang.String getTxtAdvertisementHead() {
        return txtAdvertisementHead;
    }
    
    /**
     * Setter for property txtAdvertisementHead.
     * @param txtAdvertisementHead New value of property txtAdvertisementHead.
     */
    public void setTxtAdvertisementHead(java.lang.String txtAdvertisementHead) {
        this.txtAdvertisementHead = txtAdvertisementHead;
    }
    
    /**
     * Getter for property txtARCEPSuspenceHead.
     * @return Value of property txtARCEPSuspenceHead.
     */
    public java.lang.String getTxtARCEPSuspenceHead() {
        return txtARCEPSuspenceHead;
    }
    
    /**
     * Setter for property txtARCEPSuspenceHead.
     * @param txtARCEPSuspenceHead New value of property txtARCEPSuspenceHead.
     */
    public void setTxtARCEPSuspenceHead(java.lang.String txtARCEPSuspenceHead) {
        this.txtARCEPSuspenceHead = txtARCEPSuspenceHead;
    }
    
    /**
     * Getter for property rdoInterestDueKeptReceivable_Yes.
     * @return Value of property rdoInterestDueKeptReceivable_Yes.
     */
    public boolean isRdoInterestDueKeptReceivable_Yes() {
        return rdoInterestDueKeptReceivable_Yes;
    }
    
    /**
     * Setter for property rdoInterestDueKeptReceivable_Yes.
     * @param rdoInterestDueKeptReceivable_Yes New value of property rdoInterestDueKeptReceivable_Yes.
     */
    public void setRdoInterestDueKeptReceivable_Yes(boolean rdoInterestDueKeptReceivable_Yes) {
        this.rdoInterestDueKeptReceivable_Yes = rdoInterestDueKeptReceivable_Yes;
    }
    
    /**
     * Getter for property rdoInterestDueKeptReceivable_No.
     * @return Value of property rdoInterestDueKeptReceivable_No.
     */
    public boolean isRdoInterestDueKeptReceivable_No() {
        return rdoInterestDueKeptReceivable_No;
    }
    
    /**
     * Setter for property rdoInterestDueKeptReceivable_No.
     * @param rdoInterestDueKeptReceivable_No New value of property rdoInterestDueKeptReceivable_No.
     */
    public void setRdoInterestDueKeptReceivable_No(boolean rdoInterestDueKeptReceivable_No) {
        this.rdoInterestDueKeptReceivable_No = rdoInterestDueKeptReceivable_No;
    }
    
    /**
     * Getter for property lstAvailableTransaction_OTS.
     * @return Value of property lstAvailableTransaction_OTS.
     */
    public javax.swing.DefaultListModel getLstAvailableTransaction_OTS() {
        return lstAvailableTransaction_OTS;
    }
    
    /**
     * Setter for property lstAvailableTransaction_OTS.
     * @param lstAvailableTransaction_OTS New value of property lstAvailableTransaction_OTS.
     */
    public void setLstAvailableTransaction_OTS(javax.swing.DefaultListModel lstAvailableTransaction_OTS) {
        this.lstAvailableTransaction_OTS = lstAvailableTransaction_OTS;
    }
    
    /**
     * Getter for property lstSelectedTransaction_OTS.
     * @return Value of property lstSelectedTransaction_OTS.
     */
    public javax.swing.DefaultListModel getLstSelectedTransaction_OTS() {
        return lstSelectedTransaction_OTS;
    }
    
    /**
     * Setter for property lstSelectedTransaction_OTS.
     * @param lstSelectedTransaction_OTS New value of property lstSelectedTransaction_OTS.
     */
    public void setLstSelectedTransaction_OTS(javax.swing.DefaultListModel lstSelectedTransaction_OTS) {
        this.lstSelectedTransaction_OTS = lstSelectedTransaction_OTS;
    }
    
    /**
     * Getter for property rdoinsuranceCharge_Yes.
     * @return Value of property rdoinsuranceCharge_Yes.
     */
    public boolean isRdoinsuranceCharge_Yes() {
        return rdoinsuranceCharge_Yes;
    }
    
    /**
     * Setter for property rdoinsuranceCharge_Yes.
     * @param rdoinsuranceCharge_Yes New value of property rdoinsuranceCharge_Yes.
     */
    public void setRdoinsuranceCharge_Yes(boolean rdoinsuranceCharge_Yes) {
        this.rdoinsuranceCharge_Yes = rdoinsuranceCharge_Yes;
    }
    
    /**
     * Getter for property rdoIsInterestFirst_No.
     * @return Value of property rdoIsInterestFirst_No.
     */
    public boolean isRdoIsInterestFirst_No() {
        return rdoIsInterestFirst_No;
    }
    
     
    /**
     * Setter for property rdoIsInterestFirst_No.
     * @param rdoIsInterestFirst_No New value of property rdoIsInterestFirst_No.
     */
    public void setRdoIsInterestFirst_No(boolean rdoIsInterestFirst_No) {
        this.rdoIsInterestFirst_No = rdoIsInterestFirst_No;
    }
    
    /**
     * Getter for property rdoIsInterestFirst_Yes.
     * @return Value of property rdoIsInterestFirst_Yes.
     */
    public boolean isRdoIsInterestFirst_Yes() {
        return rdoIsInterestFirst_Yes;
    }
    
    
    /**
     * Setter for property rdoIsInterestFirst_Yes.
     * @param rdoIsInterestFirst_Yes New value of property rdoIsInterestFirst_Yes.
     */
    public void setRdoIsInterestFirst_Yes(boolean rdoIsInterestFirst_Yes) {
        this.rdoIsInterestFirst_Yes = rdoIsInterestFirst_Yes;
    }

    public boolean isRdoIsInterestDue_No() {
        return rdoIsInterestDue_No;
    }

    public void setRdoIsInterestDue_No(boolean rdoIsInterestDue_No) {
        this.rdoIsInterestDue_No = rdoIsInterestDue_No;
    }

    public boolean isRdoIsInterestDue_Yes() {
        return rdoIsInterestDue_Yes;
    }

    public void setRdoIsInterestDue_Yes(boolean rdoIsInterestDue_Yes) {
        this.rdoIsInterestDue_Yes = rdoIsInterestDue_Yes;
    }
    
    
    /**
     * Getter for property rdoinsuranceCharge_No.
     * @return Value of property rdoinsuranceCharge_No.
     */
    public boolean isRdoinsuranceCharge_No() {
        return rdoinsuranceCharge_No;
    }
    
    /**
     * Setter for property rdoinsuranceCharge_No.
     * @param rdoinsuranceCharge_No New value of property rdoinsuranceCharge_No.
     */
    public void setRdoinsuranceCharge_No(boolean rdoinsuranceCharge_No) {
        this.rdoinsuranceCharge_No = rdoinsuranceCharge_No;
    }
    
    /**
     * Getter for property rdosanctionDate_Yes.
     * @return Value of property rdosanctionDate_Yes.
     */
    public boolean isRdosanctionDate_Yes() {
        return rdosanctionDate_Yes;
    }
    
    /**
     * Setter for property rdosanctionDate_Yes.
     * @param rdosanctionDate_Yes New value of property rdosanctionDate_Yes.
     */
    public void setRdosanctionDate_Yes(boolean rdosanctionDate_Yes) {
        this.rdosanctionDate_Yes = rdosanctionDate_Yes;
    }
    
    /**
     * Getter for property rdosanctionDate_No.
     * @return Value of property rdosanctionDate_No.
     */
    public boolean isRdosanctionDate_No() {
        return rdosanctionDate_No;
    }
    
    /**
     * Setter for property rdosanctionDate_No.
     * @param rdosanctionDate_No New value of property rdosanctionDate_No.
     */
    public void setRdosanctionDate_No(boolean rdosanctionDate_No) {
        this.rdosanctionDate_No = rdosanctionDate_No;
    }
    
    /**
     * Getter for property txtInsuraceRate.
     * @return Value of property txtInsuraceRate.
     */
    public String getTxtInsuraceRate() {
        return txtInsuraceRate;
    }
    
    /**
     * Setter for property txtInsuraceRate.
     * @param txtInsuraceRate New value of property txtInsuraceRate.
     */
    public void setTxtInsuraceRate(String txtInsuraceRate) {
        this.txtInsuraceRate = txtInsuraceRate;
    }

    public String getTxtDepAmtMaturing() {
        return txtDepAmtMaturing;
    }

    public void setTxtDepAmtMaturing(String txtDepAmtMaturing) {
        this.txtDepAmtMaturing = txtDepAmtMaturing;
    }

    public String getTxtDepAmtLoanMaturingPeriod() {
        return txtDepAmtLoanMaturingPeriod;
    }

    public void setTxtDepAmtLoanMaturingPeriod(String txtDepAmtLoanMaturingPeriod) {
        this.txtDepAmtLoanMaturingPeriod = txtDepAmtLoanMaturingPeriod;
    }

    public String getCboDepAmtLoanMaturingPeriod() {
        return cboDepAmtLoanMaturingPeriod;
    }

    public void setCboDepAmtLoanMaturingPeriod(String cboDepAmtLoanMaturingPeriod) {
        this.cboDepAmtLoanMaturingPeriod = cboDepAmtLoanMaturingPeriod;
    }

    public ComboBoxModel getCbmDepAmtMaturingPeriod() {
        return cbmDepAmtMaturingPeriod;
    }

    public void setCbmDepAmtMaturingPeriod(ComboBoxModel cbmDepAmtMaturingPeriod) {
        this.cbmDepAmtMaturingPeriod = cbmDepAmtMaturingPeriod;
    }
    
    /**
     * Getter for property rdoAuctionAmt_Yes.
     * @return Value of property rdoAuctionAmt_Yes.
     */
    public boolean isRdoAuctionAmt_Yes() {
        return rdoAuctionAmt_Yes;
    }    
    
    /**
     * Setter for property rdoAuctionAmt_Yes.
     * @param rdoAuctionAmt_Yes New value of property rdoAuctionAmt_Yes.
     */
    public void setRdoAuctionAmt_Yes(boolean rdoAuctionAmt_Yes) {
        this.rdoAuctionAmt_Yes = rdoAuctionAmt_Yes;
    }
    
    /**
     * Getter for property rdoAuctionAmt_No.
     * @return Value of property rdoAuctionAmt_No.
     */
    public boolean isRdoAuctionAmt_No() {
        return rdoAuctionAmt_No;
    }
    
    /**
     * Setter for property rdoAuctionAmt_No.
     * @param rdoAuctionAmt_No New value of property rdoAuctionAmt_No.
     */
    public void setRdoAuctionAmt_No(boolean rdoAuctionAmt_No) {
        this.rdoAuctionAmt_No = rdoAuctionAmt_No;
    }
     public boolean isChkGldRenewOldAmt() {
        return chkGldRenewOldAmt;
    }

    public void setChkGldRenewOldAmt(boolean chkGldRenewOldAmt) {
        this.chkGldRenewOldAmt = chkGldRenewOldAmt;
    }

    public boolean isChkGldRenewCash() {
        return chkGldRenewCash;
    }

    public void setChkGldRenewCash(boolean chkGldRenewCash) {
        this.chkGldRenewCash = chkGldRenewCash;
    }

    public boolean isChkGldRenewMarketRate() {
        return chkGldRenewMarketRate;
    }

    public void setChkGldRenewMarketRate(boolean chkGldRenewMarketRate) {
        this.chkGldRenewMarketRate = chkGldRenewMarketRate;
    }
     
    public String getChkShareLink() {
        return chkShareLink;
    }

    public void setChkShareLink(String chkShareLink) {
        this.chkShareLink = chkShareLink;
    }
    
      public String getChkExcludeScSt() {
        return chkExcludeScSt;
    }

    public void setChkExcludeScSt(String chkExcludeScSt) {
        this.chkExcludeScSt = chkExcludeScSt;
    }
    
    public String getChkExcludeTOD() {
        return chkExcludeTOD;
    }

    public void setChkExcludeTOD(String chkExcludeTOD) {
        this.chkExcludeTOD = chkExcludeTOD;
    }

    public String getChkGroupLoan() {
        return chkGroupLoan;
    }

    public void setChkGroupLoan(String chkGroupLoan) {
        this.chkGroupLoan = chkGroupLoan;
    }
    
    public boolean isChkCreditNoticeAdvance() {
        return chkCreditNoticeAdvance;
    }

    public void setChkCreditNoticeAdvance(boolean chkCreditNoticeAdvance) {
        this.chkCreditNoticeAdvance = chkCreditNoticeAdvance;
    }
    
    public String getTxtNoticeAdvancesHead() {
        return txtNoticeAdvancesHead;
    }

    public void setTxtNoticeAdvancesHead(String txtNoticeAdvancesHead) {
        this.txtNoticeAdvancesHead = txtNoticeAdvancesHead;
    }

    public LinkedHashMap getDeletedDocumentsTO() {
        return deletedDocumentsTO;
    }

    public void setDeletedDocumentsTO(LinkedHashMap deletedDocumentsTO) {
        this.deletedDocumentsTO = deletedDocumentsTO;
    }

    public boolean isRdoIsCreditAllowed_No() {
        return rdoIsCreditAllowed_No;
    }

    public void setRdoIsCreditAllowed_No(boolean rdoIsCreditAllowed_No) {
        this.rdoIsCreditAllowed_No = rdoIsCreditAllowed_No;
    }

    public boolean isRdoIsCreditAllowed_Yes() {
        return rdoIsCreditAllowed_Yes;
    }

    public void setRdoIsCreditAllowed_Yes(boolean rdoIsCreditAllowed_Yes) {
        this.rdoIsCreditAllowed_Yes = rdoIsCreditAllowed_Yes;
    }

    public boolean isRdoDisbAftMoraPerd_No() {
        return rdoDisbAftMoraPerd_No;
    }

    public void setRdoDisbAftMoraPerd_No(boolean rdoDisbAftMoraPerd_No) {
        this.rdoDisbAftMoraPerd_No = rdoDisbAftMoraPerd_No;
    }

    public boolean isRdoDisbAftMoraPerd_Yes() {
        return rdoDisbAftMoraPerd_Yes;
    }

    public void setRdoDisbAftMoraPerd_Yes(boolean rdoDisbAftMoraPerd_Yes) {
        this.rdoDisbAftMoraPerd_Yes = rdoDisbAftMoraPerd_Yes;
    }

    public String getTxtDebitAllowed() {
        return txtDebitAllowed;
    }

    public void setTxtDebitAllowed(String txtDebitAllowed) {
        this.txtDebitAllowed = txtDebitAllowed;
    }

    public String getCboIntCalcDay() {
        return cboIntCalcDay;
    }

    public void setCboIntCalcDay(String cboIntCalcDay) {
        this.cboIntCalcDay = cboIntCalcDay;
    }

    public String getCboIntCalcMonth() {
        return cboIntCalcMonth;
    }

    public void setCboIntCalcMonth(String cboIntCalcMonth) {
        this.cboIntCalcMonth = cboIntCalcMonth;
        
    }  


    public ComboBoxModel getCbmIntCalcDay() {
        return cbmIntCalcDay;
    }

    public void setCbmIntCalcDay(ComboBoxModel cbmIntCalcDay) {
        this.cbmIntCalcDay = cbmIntCalcDay;
    }

    // -- Added by nithya 

    public ComboBoxModel getCbmRepaymentFreq() {
        return cbmRepaymentFreq;
    }

    public ComboBoxModel getCbmRepaymentType() {
        return cbmRepaymentType;
    }

    public String getCboRepaymentFreq() {
        return cboRepaymentFreq;
    }

    public String getCboRepaymentType() {
        return cboRepaymentType;
    }

    public void setCbmRepaymentFreq(ComboBoxModel cbmRepaymentFreq) {
        this.cbmRepaymentFreq = cbmRepaymentFreq;
        setChanged();
    }

    public void setCbmRepaymentType(ComboBoxModel cbmRepaymentType) {
        this.cbmRepaymentType = cbmRepaymentType;
        setChanged();
    }

    public void setCboRepaymentFreq(String cboRepaymentFreq) {
        this.cboRepaymentFreq = cboRepaymentFreq;
        setChanged();
    }

    public void setCboRepaymentType(String cboRepaymentType) {
        this.cboRepaymentType = cboRepaymentType;
        setChanged();
    }
    
  

    //-- End   

    public ComboBoxModel getCbmProdHoliday() {
        return cbmProdHoliday;
    }

    public void setCbmProdHoliday(ComboBoxModel cbmProdHoliday) {
        this.cbmProdHoliday = cbmProdHoliday;
    }

    public String getChkEMIInSimpleIntrst() {
        return chkEMIInSimpleIntrst;
    }

    public void setChkEMIInSimpleIntrst(String chkEMIInSimpleIntrst) {
        this.chkEMIInSimpleIntrst = chkEMIInSimpleIntrst;
    }

    public String getCboIfHoliday() {
        return cboIfHoliday;
    }

    public void setCboIfHoliday(String cboIfHoliday) {
        this.cboIfHoliday = cboIfHoliday;
    }

    public String getChkGoldStockPhoto() {
        return chkGoldStockPhoto;
    }

    public void setChkGoldStockPhoto(String chkGoldStockPhoto) {
        this.chkGoldStockPhoto = chkGoldStockPhoto;
    }

    public String getChkBlockIFLimitExceed() {
        return chkBlockIFLimitExceed;
    }

    public void setChkBlockIFLimitExceed(String chkBlockIFLimitExceed) {
        this.chkBlockIFLimitExceed = chkBlockIFLimitExceed;
    }

    // Added by nithya on 11-01-2020 for KD-1234
    public String getTxtRebateLoanIntPercent() {
        return txtRebateLoanIntPercent;
    }

    public void setTxtRebateLoanIntPercent(String txtRebateLoanIntPercent) {
        this.txtRebateLoanIntPercent = txtRebateLoanIntPercent;
    }

    public boolean isRdoMeasurementWaiverAllowed_No() {
        return rdoMeasurementWaiverAllowed_No;
    }

    public void setRdoMeasurementWaiverAllowed_No(boolean rdoMeasurementWaiverAllowed_No) {
        this.rdoMeasurementWaiverAllowed_No = rdoMeasurementWaiverAllowed_No;
    }

    public boolean isRdoMeasurementWaiverAllowed_Yes() {
        return rdoMeasurementWaiverAllowed_Yes;
    }

    public void setRdoMeasurementWaiverAllowed_Yes(boolean rdoMeasurementWaiverAllowed_Yes) {
        this.rdoMeasurementWaiverAllowed_Yes = rdoMeasurementWaiverAllowed_Yes;
    }

    public boolean isRdoRecoveryWaiverAllowed_No() {
        return rdoRecoveryWaiverAllowed_No;
    }

    public void setRdoRecoveryWaiverAllowed_No(boolean rdoRecoveryWaiverAllowed_No) {
        this.rdoRecoveryWaiverAllowed_No = rdoRecoveryWaiverAllowed_No;
    }

    public boolean isRdoRecoveryWaiverAllowed_Yes() {
        return rdoRecoveryWaiverAllowed_Yes;
    }

    public void setRdoRecoveryWaiverAllowed_Yes(boolean rdoRecoveryWaiverAllowed_Yes) {
        this.rdoRecoveryWaiverAllowed_Yes = rdoRecoveryWaiverAllowed_Yes;
    }

    public String getTxtMeasurementWaiverHead() {
        return txtMeasurementWaiverHead;
    }

    public void setTxtMeasurementWaiverHead(String txtMeasurementWaiverHead) {
        this.txtMeasurementWaiverHead = txtMeasurementWaiverHead;
    }

    public String getTxtRecoveryWaiverHead() {
        return txtRecoveryWaiverHead;
    }

    public void setTxtRecoveryWaiverHead(String txtRecoveryWaiverHead) {
        this.txtRecoveryWaiverHead = txtRecoveryWaiverHead;
    }

    public String getTxtMeasurementChargeHead() {
        return txtMeasurementChargeHead;
    }

    public void setTxtMeasurementChargeHead(String txtMeasurementChargeHead) {
        this.txtMeasurementChargeHead = txtMeasurementChargeHead;
    }

    public String getTxtRecoveryChargeHead() {
        return txtRecoveryChargeHead;
    }

    public void setTxtRecoveryChargeHead(String txtRecoveryChargeHead) {
        this.txtRecoveryChargeHead = txtRecoveryChargeHead;
    }

    public boolean isRdoKoleFieldOperationWaiverAllowed_Yes() {
        return rdoKoleFieldOperationWaiverAllowed_Yes;
    }

    public void setRdoKoleFieldOperationWaiverAllowed_Yes(boolean rdoKoleFieldOperationWaiverAllowed_Yes) {
        this.rdoKoleFieldOperationWaiverAllowed_Yes = rdoKoleFieldOperationWaiverAllowed_Yes;
    }

    public boolean isRdoKoleFieldOperationWaiverAllowed_No() {
        return rdoKoleFieldOperationWaiverAllowed_No;
    }

    public void setRdoKoleFieldOperationWaiverAllowed_No(boolean rdoKoleFieldOperationWaiverAllowed_No) {
        this.rdoKoleFieldOperationWaiverAllowed_No = rdoKoleFieldOperationWaiverAllowed_No;
    }

    public boolean isRdoKoleFieldExpenseWaiverAllowed_Yes() {
        return rdoKoleFieldExpenseWaiverAllowed_Yes;
    }

    public void setRdoKoleFieldExpenseWaiverAllowed_Yes(boolean rdoKoleFieldExpenseWaiverAllowed_Yes) {
        this.rdoKoleFieldExpenseWaiverAllowed_Yes = rdoKoleFieldExpenseWaiverAllowed_Yes;
    }

    public boolean isRdoKoleFieldExpenseWaiverAllowed_No() {
        return rdoKoleFieldExpenseWaiverAllowed_No;
    }

    public void setRdoKoleFieldExpenseWaiverAllowed_No(boolean rdoKoleFieldExpenseWaiverAllowed_No) {
        this.rdoKoleFieldExpenseWaiverAllowed_No = rdoKoleFieldExpenseWaiverAllowed_No;
    }

    public String getTxtKoleFieldOperationWaiverHead() {
        return txtKoleFieldOperationWaiverHead;
    }

    public void setTxtKoleFieldOperationWaiverHead(String txtKoleFieldOperationWaiverHead) {
        this.txtKoleFieldOperationWaiverHead = txtKoleFieldOperationWaiverHead;
    }

    public String getTxtKoleFieldOperationHead() {
        return txtKoleFieldOperationHead;
    }

    public void setTxtKoleFieldOperationHead(String txtKoleFieldOperationHead) {
        this.txtKoleFieldOperationHead = txtKoleFieldOperationHead;
    }

    public String getTxtKoleFieldExpenseWaiverHead() {
        return txtKoleFieldExpenseWaiverHead;
    }

    public void setTxtKoleFieldExpenseWaiverHead(String txtKoleFieldExpenseWaiverHead) {
        this.txtKoleFieldExpenseWaiverHead = txtKoleFieldExpenseWaiverHead;
    }

    public String getTxtKoleFieldExpenseHead() {
        return txtKoleFieldExpenseHead;
    }

    public void setTxtKoleFieldExpenseHead(String txtKoleFieldExpenseHead) {
        this.txtKoleFieldExpenseHead = txtKoleFieldExpenseHead;
    }
    
}

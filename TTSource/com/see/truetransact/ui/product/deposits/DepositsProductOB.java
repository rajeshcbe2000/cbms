/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * DepositsProductOB.java
 *
 * Created on December 10, 2003, 10:17 AM
 */

package com.see.truetransact.ui.product.deposits;

import com.see.truetransact.clientproxy.ProxyFactory;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import java.util.Observable;
import com.see.truetransact.commonutil.TTException;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.transferobject.product.deposits.DepositsProductTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductSchemeTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductIntPayTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductAcHdTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductTaxTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductRenewalTO;
import com.see.truetransact.transferobject.product.deposits.DepositsProductRDTO;

import com.see.truetransact.transferobject.deposit.interestmaintenance.InterestMaintenanceRateTO;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.see.truetransact.clientutil.TableModel;

import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.transferobject.product.deposits.*;

import org.apache.log4j.Logger;

import com.see.truetransact.uicomponent.CObservable;

/**
 *
 * @author  amathan
 */

public class DepositsProductOB extends CObservable {
  
    private String chkCummCertPrint = "";
    private String chkExcludeLienStanding = "";
    private String chkExcludeLienIntrstAppl = "";
    private String chkDepositLien = "";
    private String chkCanZeroBalAct = "";
    private boolean dueOn = false;
    private HashMap dataHash;
    public boolean isDueOn() {
        return dueOn;
    }

    public void setDueOn(boolean dueOn) {
        this.dueOn = dueOn;
    }
    public String getChkCanZeroBalAct() {
        return chkCanZeroBalAct;
    }

    public void setChkCanZeroBalAct(String chkCanZeroBalAct) {
        this.chkCanZeroBalAct = chkCanZeroBalAct;
    }
    private String txtAcctHd = "";
    private String txtProductID = "";
    private String txtDesc = "";
    private String cboOperatesLike = "";
    private String cboProdCurrency = "";
    private boolean rdoCalcTDS_Yes = false;
    private boolean rdoCalcTDS_No = false;
    private boolean rdoExtensionPenal_Yes = false;
    private boolean rdoExtensionPenal_No = false;
    private String rdoWithPeriod = "";
    private String rdoDoublingScheme = "";
    //private boolean rdoCalcTDS = false;
    private boolean rdoPayInterestOnHoliday_Yes = false;
    private boolean rdoPayInterestOnHoliday_No = false;
    private boolean rdoAmountRounded_Yes = false;
    private boolean rdoAmountRounded_No = false;
    private String cboRoundOffCriteria = "";
    private boolean rdoInterestAfterMaturity_Yes = false;
    private boolean rdoInterestAfterMaturity_No = false;
    private boolean rdoSBRateOneRate_Yes = false;
    private boolean rdoSBRateOneRate_No = false;
    private ComboBoxModel cbmEitherofTwoRatesChoose;
    private String cboMaturityInterestType = "";
    private String cboMaturityInterestRate = "";
    private String cboInterestCriteria = "";
    private String txtPeriodInMultiplesOf = "";
    private String cboPeriodInMultiplesOf = "";
    private String txtMaxDepositPeriod = "";
    private String cboMaxDepositPeriod = "";
    private String txtMinDepositPeriod = "";
    private String cboMinDepositPeriod = "";
    private String cboPrematureWithdrawal = "";
    private String txtDepositPerUnit = "";
    private boolean rdoInterestalreadyPaid_Yes = false;
    private boolean rdoInterestalreadyPaid_No = false;
    private boolean rdoInterestrateappliedoverdue_Yes = false;
    private boolean rdoInterestrateappliedoverdue_No = false;
    private boolean rdoBothRateNotAvail_Yes = false;
    private boolean rdoBothRateNotAvail_No = false;
    //    private String cboDepositPerUnit = "";
    private String chkRdNature;
    private String cboInterestType = "";
    private String txtAmtInMultiplesOf = "";
    private String txtMinDepositAmt = "";
    private String txtMaxDepositAmt = "";
    private boolean rdoCalcMaturityValue_Yes = false;
    private boolean rdoCalcMaturityValue_No = false;
    private boolean rdoProvisionOfInterest_Yes = false;
    private boolean rdoProvisionOfInterest_No = false;
    private String txtInterestOnMaturedDeposits = "";
    private String txtAlphaSuffix = "";
    private String txtMaxAmtOfCashPayment = "";
    private String txtMinAmtOfPAN = "";
    private String txtAcctNumberPattern = "";
    private String txtLastAcctNumber = "";
    private String txtSuffix = "";
    private String chkPrematureClosure = "";
    private String chkIntApplicationSlab="";
    private String cboRoundOffCriteriaPenal="";
    private String rdoPenalRounded_Yes="";
    private String cboInstType="";
    private String chkAppNormRate="";
    private String txtCategoryBenifitRate = "";
    private ComboBoxModel cbmSbrateModel;// added by chithra 17-05-14
    private String sbRateCmb = "";
    private String chkIntEditable="Y";
    private String  txtDoublingCount;
    private String chkWeeklySpecial;
    private String txtGracePeriod = ""; // Added by Shany
   
    /// Added by nithya on 02-03-2016 for 0003897
    private String installmentAmount = "";
    private String effectiveDate = "";    
    List objDepositsThriftBenevolentTOList;
    Date effectiveDateEntered;
    
    // Added by nithya for adding field for interest reserve head for benevolent deposits
    private String txtBenIntReserveHead = "";
    private ComboBoxModel cbmAgentCommCalcMethod;
    private String chkAgentCommSlabRequired = "N";
    private String cboAgentCommCalcMethod = "";

    public String getTxtBenIntReserveHead() {
        return txtBenIntReserveHead;
    }

    public void setTxtBenIntReserveHead(String txtBenIntReserveHead) {
        this.txtBenIntReserveHead = txtBenIntReserveHead;
    }    
    
    // End

    public void setEffectiveDateEntered(Date effectiveDateEntered) {
        this.effectiveDateEntered = effectiveDateEntered;
    }

    public Date getEffectiveDateEntered() {
        return effectiveDateEntered;
    }  
    
    // End
    public String getChkWeeklySpecial() {
        return chkWeeklySpecial;
    }

    public void setChkWeeklySpecial(String chkWeeklySpecial) {
        this.chkWeeklySpecial = chkWeeklySpecial;
    }

    public String getTxtDoublingCount() {
        return txtDoublingCount;
    }

    public void setTxtDoublingCount(String txtDoublingCount) {
        this.txtDoublingCount = txtDoublingCount;
    }
    
    public String getCboInstType() {
        return cboInstType;
    }

    public void setCboInstType(String cboInstType) {
        this.cboInstType = cboInstType;
    }
    public String getChkIntEditable() {
        return chkIntEditable;
    }

    public void setChkIntEditable(String chkIntEditable) {
        this.chkIntEditable = chkIntEditable;
    }
    public String getChkAppNormRate() {
        return chkAppNormRate;
    }

    public void setChkAppNormRate(String chkAppNormRate) {
        this.chkAppNormRate = chkAppNormRate;
    }

    public String getTxtNormPeriod() {
        return txtNormPeriod;
    }

    public void setTxtNormPeriod(String txtNormPeriod) {
        this.txtNormPeriod = txtNormPeriod;
    }
    private String txtNormPeriod="";

    public String getRdoPenalRounded_Yes() {
        return rdoPenalRounded_Yes;
    }

    public void setRdoPenalRounded_Yes(String rdoPenalRounded_Yes) {
        this.rdoPenalRounded_Yes = rdoPenalRounded_Yes;
    }
    private String rdoPenalRounded_No="";

    public String getRdoPenalRounded_No() {
        return rdoPenalRounded_No;
    }

    public void setRdoPenalRounded_No(String rdoPenalRounded_No) {
        this.rdoPenalRounded_No = rdoPenalRounded_No;
    }
   
    public String getCboRoundOffCriteriaPenal() {
        return cboRoundOffCriteriaPenal;
    }

    public String getChkRdNature() {
        return chkRdNature;
    }

    public void setChkRdNature(String chkRdNature) {
        this.chkRdNature = chkRdNature;
    }

    public void setCboRoundOffCriteriaPenal(String cboRoundOffCriteriaPenal) {
        this.cboRoundOffCriteriaPenal = cboRoundOffCriteriaPenal;
    }

    public String getChkIntApplicationSlab() {
        return chkIntApplicationSlab;
    }

    public void setChkIntApplicationSlab(String chkIntApplicationSlab) {
        this.chkIntApplicationSlab = chkIntApplicationSlab;
    }

    public String getChkPrematureClosure() {
        return chkPrematureClosure;
    }

    public void setChkPrematureClosure(String chkPrematureClosure) {
        this.chkPrematureClosure = chkPrematureClosure;
    }

    public String getCbxInterstRoundTime() {
        return cbxInterstRoundTime;
    }

    public void setCbxInterstRoundTime(String cbxInterstRoundTime) {
        this.cbxInterstRoundTime = cbxInterstRoundTime;
    }
    private String tdtSchemeIntroDate = "";
    private String tdtSchemeClosingDate = "";
    private String cbxInterstRoundTime="";
    //    private String txtScheme = "";
    private String txtAfterNoDays = "";
    private String txtTDSGLAcctHd = "";
    private String txtLimitForBulkDeposit = "";
    private boolean rdoTransferToMaturedDeposits_Yes = false;
    private boolean rdoTransferToMaturedDeposits_No = false;
    private boolean rdoAutoAdjustment_Yes = false;
    private boolean rdoAutoAdjustment_No = false;
    private boolean rdoAdjustIntOnDeposits_No = false;
    private boolean rdoAdjustIntOnDeposits_Yes = false;
    private boolean rdoAdjustPrincipleToLoan_Yes = false;
    private boolean rdoAdjustPrincipleToLoan_No = false;
    private boolean rdoExtnOfDepositBeforeMaturity_Yes = false;
    private boolean rdoExtnOfDepositBeforeMaturity_No = false;
    private String txtAfterHowManyDays = "";
    private String txtAdvanceMaturityNoticeGenPeriod = "";
    private String txtPrematureWithdrawal = "";
    private boolean rdoFlexiFromSBCA_Yes = false;
    private boolean rdoFlexiFromSBCA_No = false;
    private boolean rdoTermDeposit_Yes = false;
    private boolean rdoTermDeposit_No = false;
    private boolean rdoIntroducerReqd_Yes = false;
    private boolean rdoIntroducerReqd_No = false;
    //    private boolean rdoSystemCalcValues_Yes = false;
    //    private boolean rdoSystemCalcValues_No = false;
    private String rdoCertificate_printing = "";
    private String txtNoOfPartialWithdrawalAllowed = "";
    private String txtMaxNoOfPartialWithdrawalAllowed = "";
    private String txtMaxAmtOfPartialWithdrawalAllowed = "";
    private String txtMinAmtOfPartialWithdrawalAllowed = "";
    private String txtWithdrawalsInMultiplesOf = "";
    private boolean rdoPartialWithdrawalAllowed_Yes = false;
    private String cboRenewedclosedbefore = "";
    private boolean rdoPartialWithdrawalAllowed_No = false;
    private boolean rdoWithdrawalWithInterest_Yes = false;
    private boolean rdoWithdrawalWithInterest_No = false;
    private boolean rdoServiceCharge_Yes = false;
    private boolean rdoServiceCharge_No = false;
    private boolean rdoInsBeyondMaturityDt_Yes = false;
    private boolean rdoInsBeyondMaturityDt_No = false;
    private String txtServiceCharge = "";
    private String cboIntCalcMethod = "";
    private String cboIntMaintainedAsPartOf = "";
    private boolean rdoIntProvisioningApplicable_Yes = false;
    private boolean rdoIntProvisioningApplicable_No = false;
    private String cboIntProvisioningFreq = "";
    private String cboProvisioningLevel = "";
    private String cboNoOfDays = "";
    private String cboIntCompoundingFreq = "";
    private String cboIntApplnFreq = "";
    private String tdtNextInterestAppliedDate = "";
    private String tdtLastInterestAppliedDate = "";
    private String tdtNextIntProvisionalDate = "";
    private String tdtLastIntProvisionalDate = "";
    private String cboIntRoundOff = "";
    private String txtMinIntToBePaid = "";
    private String txtIntProvisioningAcctHd = "";
    private String txtIntOnMaturedDepositAcctHead = "";
    private String txtFixedDepositAcctHead = "";
    private boolean rdoClosedwithinperiod_Yes = false;
    private boolean rdoClosedwithinperiod_No = false;
    private String txttMaturityDepositAcctHead = "";
    private String txtIntProvisionOfMaturedDeposit = "";
    private String txtIntPaybleGLHead = "";
    private String txtCommisionPaybleGLHead ="";
    private String txtPenalChargesAchd = "";
    private String txtInterestRecoveryAcHd = "";
    private String txtTransferOutAcHd = "";
    private String txtIntDebitPLHead = "";
    private String txtAcctHeadForFloatAcct = "";
    private String cboIntPeriodForBackDatedRenewal = "";
    private String txtIntPeriodForBackDatedRenewal= "";
    private String cboMaxPeriodMDt = "";
    private String txtMaxPeriodMDt= "";
    private String txtMaxPeriodPDt= "";
    private boolean rdoExtensionBeyondOriginalDate_Yes = false;
    private boolean rdoExtensionBeyondOriginalDate_No = false;
    private String cboIntCriteria = "";
    private boolean rdoAutoRenewalAllowed_Yes = false;
    private boolean rdoAutoRenewalAllowed_No = false;
    private boolean rdoSameNoRenewalAllowed_Yes = false;
    private boolean rdoSameNoRenewalAllowed_No = false;
    private boolean rdoRenewalOfDepositAllowed_Yes = false;
    private boolean rdoRenewalOfDepositAllowed_No = false;
    private String txtMinNoOfDays = "";
    private String txtMaxNopfTimes = "";
    private String txtMaxNoSameNo = "";
    private String cboIntType = "";
    private String tdtMaturityDate = "";
    private String tdtPresentDate = "";
    private String txtMinimumRenewalDeposit = "";
    private boolean rdoRecalcOfMaturityValue_Yes = false;
    private boolean rdoRecalcOfMaturityValue_No = false;
    private boolean rdoPenaltyOnLateInstallmentsChargeble_Yes = false;
    private boolean rdoPenaltyOnLateInstallmentsChargeble_No = false;
    private String txtMaturityDateAfterLastInstalPaid = "";
    private ComboBoxModel cbmMinimumRenewalDeposit;
    private String txtAgentCommision = "";
    private String txtMinimumPeriod = "";
    private String txtInterestNotPayingValue = "";
    private String cboInterestNotPayingValue = "";
    private boolean rdoCanReceiveExcessInstal_yes = false;
    private boolean rdoCanReceiveExcessInstal_No = false;
    private boolean rdoInstallmentInRecurringDepositAcct_Yes = false;
    private boolean rdoInstallmentInRecurringDepositAcct_No = false;
    private String cboInstallmentToBeCharged = "";
    //added daily deposit scheme
    private String cboDepositsFrequency;
    private String cboChangeValue = "";
    private boolean rdoIntPayableOnExcessInstal_Yes = false;
    private boolean rdoIntPayableOnExcessInstal_No = false;
    private boolean rdoRecurringDepositToFixedDeposit_Yes = false;
    private boolean rdoRecurringDepositToFixedDeposit_No = false;
    
    private boolean rdoRegular = false;
    private boolean rdoNRO = false;
    private boolean rdoNRE = false;
    
    private boolean rdoStaffAccount_Yes = false;
    private boolean rdoStaffAccount_No = false;
    
    private String cboCutOffDayForPaymentOfInstal = "";
    private String txtCutOffDayForPaymentOfInstal = "";
    private String tdtFromDepositDate = "";
    private String tdtChosenDate = "";
    private String cboMinNoOfDays = "";
    private ComboBoxModel cbmRenewedclosedbefore;
    private String cboMaturityDateAfterLastInstalPaid = "";
    private String cboAgentCommision = "";
    private String cboMinimumPeriod = "";
    private boolean discounted_Yes = false;
    private boolean discounted_No = false;
    private boolean rdoDaily = false;
    private boolean rdoWeekly = false;
    private boolean rdoMonthly = false;
    private String cboWeekly_Day = "";
    private String cboMonthlyIntCalcMethod = "";
    private String rdoPartialWithdrawlAllowedForDD = "";
    
    private String discountedRate = "";
    
    //daily Deposit Agents Commision
    private String tdtDate ="";
    private String tdtToDate ="";
    private String txtFromPeriod ="";
    private String txtToPeriod ="";
    private String cboFromAmount ="";
    private String cboToAmount ="";
    private String cboFromPeriod ="";
    private String cboToPeriod ="";
    private String txtRateInterest ="";
    
    private ArrayList productTabRow;
    private ArrayList categoryTabRow;
    private ArrayList interestRateTabRow;
    private ArrayList interestTabRow;
    private ArrayList interestTempTabRow;
    private EnhancedTableModel tblInterestTable;
    ArrayList interestTabTitle = new ArrayList();
    public boolean isTableSet = false;
    private String duration = "";
    
    private ArrayList interestList = new ArrayList();
    private ArrayList interestDeleteTabRow = new ArrayList();
    private ArrayList interestDelete;
    private int SerialNo;
    private TableModel tbmInterestTable;
    private String txtRenewedclosedbefore = "";
    private String txtFromAmt = null;
    private String txtToAmt = null;
    private String txtDelayedChargesAmt = null;
    HashMap depSubNoAll;
    int depSubNoCount;
    public int depSubNo;
    private ArrayList depSubNoRow;
    private EnhancedTableModel tblDelayedInstallmet;
    private ArrayList delayedInstallmet;
    private HashMap depSubNoRec;
    public int depSubNoMode;
    int depSubNoK;
    private final int CANCEL = 2;
    int getSelectedRowCount;
    private ArrayList tblDepSubNoColTitle;
    private int depSubNoRowDel;
    private int ModifyDepSubNo;
    HashMap depSubNoCheckValues;
    ArrayList delayinterestTabTitle = null;
    private ArrayList delayedTabRow;
    //    private List productTabList;
    //    private ArrayList prodList;
    
    private String lblAuthorize = "";
    
    private boolean rdoLastInstallmentAllowed_Yes = false;
    private boolean rdoLastInstallmentAllowed_No = false;
    
    
    private HashMap keyValue;
    private ArrayList key;
    private ArrayList value;
    private String cboMinimumRenewalDeposit = "";
    private ComboBoxModel cbmIntRoundOff;
    private ComboBoxModel cbmInstType;
    private ComboBoxModel cbmOperatesLike;
    private ComboBoxModel cbmProdCurrency;
    private ComboBoxModel cbmRoundOffCriteria;
    private ComboBoxModel cbmMaturityInterestType;
    private ComboBoxModel cbmMaturityInterestRate;
    private ComboBoxModel cbmInterestCriteria;
    private boolean chkRateAsOnDateOfRenewal = false;
    private boolean chkRateAsOnDateOfMaturity = false;
    private boolean chkIntRateApp=false;
    private boolean chkIntRateDeathMarked= false;
    //    private ComboBoxModel cbmDepositPerUnit;
    private ComboBoxModel cbmMinDepositPeriod;
    private ComboBoxModel cbmMaxDepositPeriod;
    private ComboBoxModel cbmPeriodInMultiplesOf;
    private ComboBoxModel cbmInterestType;
    private ComboBoxModel cbmPrematureWithdrawal;
    
    //added for daily deposit scheme
    private ComboBoxModel cbmDepositsFrequency;
    private ComboBoxModel cbmFromAmount;
    private ComboBoxModel cbmToAmount;
    private ComboBoxModel cbmFromPeriod;
    private ComboBoxModel cbmToPeriod;
    private ComboBoxModel cbmWeekly;
    private ComboBoxModel cbmMonthlyIntCalcMethod;
    private ComboBoxModel cbmMaxMDt;
    private ComboBoxModel cbmMaxPDt;
    
    private ComboBoxModel cbmIntMaintainedAsPartOf;
    private ComboBoxModel cbmIntCalcMethod;
    private ComboBoxModel cbmIntProvisioningFreq;
    private ComboBoxModel cbmProvisioningLevel;
    private ComboBoxModel cbmNoOfDays;
    private ComboBoxModel cbmIntCompoundingFreq;
    private ComboBoxModel cbmIntApplnFreq;
    
    private ComboBoxModel cbmMinNoOfDays;
    private ComboBoxModel cbmMaturityDateAfterLastInstalPaid;
    private ComboBoxModel cbmAgentCommision;
    private ComboBoxModel cbmInterestNotPayingValue;
    private ComboBoxModel cbmMinimumPeriod;
    private ComboBoxModel cbmCutOffDayForPaymentOfInstal;
    private String cboEitherofTwoRatesChoose = "";
    private ComboBoxModel cbmIntPeriodForBackDatedRenewal;
    private ComboBoxModel cbmIntType;
    private ComboBoxModel cbmIntCriteria;
    private ComboBoxModel cbmDepositsProdFixd;
    private ComboBoxModel cbmSbProduct;
    
    private ComboBoxModel cbmInstallmentToBeCharged;
    private ComboBoxModel cbmChangeValue;
    
    private String lblStatus = ClientConstants.ACTION_STATUS[ClientConstants.ACTIONTYPE_CANCEL];
    
    private HashMap operationMap;
    private ProxyFactory proxy;
    
    private static DepositsProductOB objDepositsProductOB; // singleton object
    
    private final static Logger log = Logger.getLogger(DepositsProductOB.class);
    
    private int actionType;
    private int result;
    
    private long periodData;// for setting data depending on period comboboxes...
    private long resultData;// for setting data depending on period comboboxes...
    private int resultValue=0;// for retrieving data from the period comboboxes...
    
    private final String YES ="Y";
    private final String NO ="N";
    private final String YEAR_STR ="Years";
    private final String MONTH_STR ="Months";
    private final String DAY_STR ="Days";
    
    private final String REGULAR ="NORMAL";
    private final String NRO ="NRO";
    private final String NRE ="NRE";
    
    private final String YEAR ="YEARS";
    private final String MONTH ="MONTHS";
    private final String DAY ="DAYS";
    
    private final int YEAR_INT = 365;
    private final int MONTH_INT = 30;
    private final int DAY_INT = 1;
    Date curDate = null;
    private String agentsAuthStatus = null;
    public boolean authStatus = false;
    private String  postageAcHd="";
    private String txtRDIrregularIfInstallmentDue="";
    private boolean rdoIncaseOfIrregularRDSBRate=false;
    private boolean rdoIncaseOfIrregularRDRBRate=false;
    private boolean rdoDepositRate=false;
    private boolean rdoSBRate=false;
    private boolean chkFDRenewalSameNoTranPrincAmt=false;
    private boolean chkDifferentROI=false;
    private boolean chkSlabWiseInterest =false;
    private boolean chkSlabWiseCommision = false;
    private String CboPreMatIntType = "";
    private ComboBoxModel cbmPreMatIntTypeModel;
    private String cboSbProduct = "";
    private String chkIsGroupDepositProduct = "N"; //  Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
    
    private String chkRDClosingOtherROI = "N";
    private String chkIntForIrregularRD = "Y"; // Added by nithya on 18-03-2020 for KD-1535
    private String chkSpecialRD = "N"; // Added by nithya on 18-03-2020 for KD-1535
    private String txtSpecialRDInstallments = ""; //Added by nithya on 01-04-2020 for KD-1535
    private String cboprmatureCloseRate = "";
    private String cboPrmatureCloseProduct = "";
    private String cboIrregularRDCloseRate = "";
    private String cboIrregularRDCloseProduct = "";
            
    private ComboBoxModel cbmprmatureCloseRate ;
    private ComboBoxModel cbmPrmatureCloseProduct;
    private ComboBoxModel cbmIrregularRDCloseRate;
    private ComboBoxModel cbmIrregularRDCloseProduct;

    public boolean isChkIntRateDeathMarked() {
        return chkIntRateDeathMarked;
    }

    public void setChkIntRateDeathMarked(boolean chkIntRateDeathMarked) {
        this.chkIntRateDeathMarked = chkIntRateDeathMarked;
    }


    public ComboBoxModel getCbmSbProduct() {
        return cbmSbProduct;
    }

    public void setCbmSbProduct(ComboBoxModel cbmSbProduct) {
        this.cbmSbProduct = cbmSbProduct;
    }

    public String getCboSbProduct() {
        return cboSbProduct;
    }

    public void setCboSbProduct(String cboSbProduct) {
        this.cboSbProduct = cboSbProduct;
    }
    
    
    
    public String getCboPreMatIntType() {
        return CboPreMatIntType;
    }

    public void setCboPreMatIntType(String CboPreMatIntType) {
        this.CboPreMatIntType = CboPreMatIntType;
    }

    public ComboBoxModel getCbmPreMatIntTypeModel() {
        return cbmPreMatIntTypeModel;
    }

    public void setCbmPreMatIntTypeModel(ComboBoxModel cbmPreMatIntTypeModel) {
        this.cbmPreMatIntTypeModel = cbmPreMatIntTypeModel;
    }

    
    public boolean isChkSlabWiseCommision() {
        return chkSlabWiseCommision;
    }

    public void setChkSlabWiseCommision(boolean chkSlabWiseCommision) {
        this.chkSlabWiseCommision = chkSlabWiseCommision;
    }
    
    public boolean isChkSlabWiseInterest() {
        return chkSlabWiseInterest;
    }

    public void setChkSlabWiseInterest(boolean chkSlabWiseInterest) {
        this.chkSlabWiseInterest = chkSlabWiseInterest;
    }

    public boolean isChkDifferentROI() {
        return chkDifferentROI;
    }

    public void setChkDifferentROI(boolean chkDifferentROI) {
        this.chkDifferentROI = chkDifferentROI;
    }
    
    private String cboDepositsProdFixd = "";
    //    public boolean newRecEntering;
    /** To get the Value of Column Title and Dialogue Box...*/
    //    final InterestMaintenanceRB objInterestMaintenanceRB = new InterestMaintenanceRB();
    java.util.ResourceBundle objInterestMaintenanceRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.deposits.DepositsProductRB", ProxyParameters.LANGUAGE);
    java.util.ResourceBundle objDepositProductRB = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.deposits.DepositsProductRB", ProxyParameters.LANGUAGE);
    
    private static Date currDate = null;
    private final static ClientParseException parseException = ClientParseException.getInstance();
    
    private String cboProductType = "";
    private String txtProductId="";
    private String txtInstallmentFrom="";
    private String txtInstallmentTo="";
    private String txtPenal="";
    private String txtInstallmentNo="";
    private ComboBoxModel cbmProductType;
    private TableModel tbmWeeklySlabSettings;
    private TableModel tbmThriftBenevolent;    //   Added by nithya on 02-03-2016 for 0003897 
    private WeeklyDepositSlabSettingsTO objWeeklyDepositSlabTo;
    private ArrayList recordData, deleteData ,thriftBenevolentRecord;  // Added thriftBenevolentRecord by nithya on 02-03-2016 for 0003897
    private int instFrom=0;
    private int instTo=0;
   
    private String cboAgentCommProductType = "";
    private String txtAgentCommSlabAmtFrom = "";
    private String txtAgentCommSlabAmtTo = "";
    private String txtAgentCommSlabPercent = "";   
    private TableModel tbmAgentCommSlabSettings;
    private ArrayList agentCommRecordData, agentCommDeleteData;
    private AgentCommissionSlabSettingsTO objAgentCommissionSlabSettingsTO;
    
    // Added by nithya on 02-03-2016 for 0003897
    
    public TableModel getTbmThriftBenevolent() {
        return tbmThriftBenevolent;
    }
     
    public void setTbmThriftBenevolent(TableModel tbmThriftBenevolent) {
        this.tbmThriftBenevolent = tbmThriftBenevolent;
    }
    
    // End
    
    public TableModel getTbmWeeklySlabSettings() {
        return tbmWeeklySlabSettings;
    }
    
    public void setTbmWeeklySlabSettings(TableModel tbmWeeklySlabSettings) {
        this.tbmWeeklySlabSettings = tbmWeeklySlabSettings;
    }
    
    
    public int getInstFrom() {
        return instFrom;
    }

    public void setInstFrom(int instFrom) {
        this.instFrom = instFrom;
    }

    public int getInstTo() {
        return instTo;
    }

    public void setInstTo(int instTo) {
        this.instTo = instTo;
    }

    public ComboBoxModel getCbmProductType() {
        return cbmProductType;
    }

    public void setCbmProductType(ComboBoxModel cbmProductType) {
        this.cbmProductType = cbmProductType;
    }

    public String getCboProductType() {
        return cboProductType;
    }

    public void setCboProductType(String cboProductType) {
        this.cboProductType = cboProductType;
    }

    public String getTxtInstallmentFrom() {
        return txtInstallmentFrom;
    }

    public void setTxtInstallmentFrom(String txtInstallmentFrom) {
        this.txtInstallmentFrom = txtInstallmentFrom;
    }

    public String getTxtInstallmentNo() {
        return txtInstallmentNo;
    }

    public void setTxtInstallmentNo(String txtInstallmentNo) {
        this.txtInstallmentNo = txtInstallmentNo;
    }

    public String getTxtInstallmentTo() {
        return txtInstallmentTo;
    }

    public void setTxtInstallmentTo(String txtInstallmentTo) {
        this.txtInstallmentTo = txtInstallmentTo;
    }

    public String getTxtPenal() {
        return txtPenal;
    }

    public void setTxtPenal(String txtPenal) {
        this.txtPenal = txtPenal;
    }

    public String getTxtProductId() {
        return txtProductId;
    }

    public void setTxtProductId(String txtProductId) {
        this.txtProductId = txtProductId;
    } 
    
    static {
        try {
            log.info("Creating ...");
            currDate = ClientUtil.getCurrentDate();
            objDepositsProductOB = new DepositsProductOB();
        } catch(Exception e) {
            e.printStackTrace();
            parseException.logException(e,true);
        }
    }

    // Added by nithya on 01-03-2016
    
    public String getEffectiveDate() {
        return effectiveDate;
    }

    public String getInstallmentAmount() {
        return installmentAmount;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setInstallmentAmount(String installmentAmount) {
        this.installmentAmount = installmentAmount;
    }
    
    // End
    
    /** Creates a new instance of DepositsProductOB */
    private DepositsProductOB() throws Exception{
        curDate = ClientUtil.getCurrentDate();
        proxy = ProxyFactory.createProxy();
        setOperationMap();
        fillDropdown();
        
        setInterestTabTitle();
        setTblDelayedInstallmet();
        
        setTable();
        setAgentCommissionSlabTable();
        setThriftBenevolentTable(); // Added by nithya on 02-03-2016 for 0003897
        recordData = new ArrayList();
        agentCommRecordData = new ArrayList();
        agentCommDeleteData = new ArrayList();
        deleteData = new ArrayList();
        thriftBenevolentRecord = new ArrayList(); // Added by nithya on 02-03-2016 for 0003897
        
        //        tblInterestTable = new EnhancedTableModel(null, interestTabTitle);
        //        tblDelayedInstallmet = new EnhancedTableModel(null, delayedInstallmet);
        
        //        tblCurrency = new EnhancedTableModel();
    }
    
    /* Sets the HashMap required to set JNDI,Home and Remote*/
    private void setOperationMap() throws Exception{
        operationMap = new HashMap();
        operationMap.put(CommonConstants.JNDI, "DepositsProductJNDI");
        operationMap.put(CommonConstants.HOME, "deposits.DepositsProductHome");
        operationMap.put(CommonConstants.REMOTE, "deposits.DepositsProduct");
    }
    
    /**
     * Returns an instance of DepositsProductOB.
     * @return  DepositsProductOB
     */
    public static DepositsProductOB getInstance() {
        return objDepositsProductOB;
    }
    
    public void ttNotifyObservers(){
        notifyObservers();
    }
    
    public void fillDropdown() throws Exception{
        HashMap lookUpHash = new HashMap();
        lookUpHash.put(CommonConstants.MAP_NAME,null);
        final ArrayList lookup_keys = new ArrayList();
        
        lookup_keys.add("DEPOSITSPRODUCT.OPERATESLIKE");
        lookup_keys.add("OPERATIVEACCTPRODUCT.INTROUNDOFF");
        lookup_keys.add("DEPOSITSPRODUCT.INTEREST_TYPE");
        lookup_keys.add("DEPOSITSPRODUCT.INTEREST_RATE");
        lookup_keys.add("DEPOSITSPRODUCT.INTEREST_CRITERIA");
        lookup_keys.add("PERIOD");
        lookup_keys.add("LOANPRODUCT.LOANPERIODS");
        lookup_keys.add("DEPOSITSPRODUCT.INT_MAINTAIN");
        lookup_keys.add("DEPOSITSPRODUCT.INT_CALC_METHOD");
        lookup_keys.add("DEPOSITSPRODUCT.PROV_LEVEL");
        lookup_keys.add("DEPOSITSPRODUCT.NO_DAYS_YEAR");
        lookup_keys.add("DEPOSITSPRODUCT.CHANGE_VALUE");
        lookup_keys.add("FOREX.CURRENCY");
        lookup_keys.add("DEPOSIT_PROD.CUTOFF_TYPE");
        lookup_keys.add("INTEREST_TYPE");
        lookup_keys.add("WEEK_DAYS");
        lookup_keys.add("MDS_PERIOD");
        lookup_keys.add("DAILY_INT_CALC_METHOD");
        lookup_keys.add("DEPOSITS_INT_RATE_APPLY");
        //                lookup_keys.add("PERIOD");
        lookup_keys.add("DEPOSIT.AMT_RANGE_FROM");
        lookup_keys.add("DEPOSIT.AMT_RANGE");
        lookup_keys.add("RATE_SELECTION");
        lookup_keys.add("AGENT_COMM_CALC_METHOD");
        
        
        lookUpHash.put(CommonConstants.PARAMFORQUERY, lookup_keys);
        
        keyValue = ClientUtil.populateLookupData(lookUpHash);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.OPERATESLIKE"));
        cbmOperatesLike = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("MDS_PERIOD"));
        cbmInstType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("INTEREST_TYPE"));
        cbmInterestType = new ComboBoxModel(key,value);
        cbmPreMatIntTypeModel = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("FOREX.CURRENCY"));
        cbmProdCurrency = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("OPERATIVEACCTPRODUCT.INTROUNDOFF"));
        cbmRoundOffCriteria = new ComboBoxModel(key,value);
        cbmIntRoundOff = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.INTEREST_TYPE"));
        cbmMaturityInterestType = new ComboBoxModel(key,value);
        cbmIntType = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.INTEREST_RATE"));
        cbmMaturityInterestRate = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSIT_PROD.CUTOFF_TYPE"));
        cbmCutOffDayForPaymentOfInstal = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.INTEREST_CRITERIA"));
        cbmInterestCriteria = new ComboBoxModel(key,value);
        cbmIntCriteria = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        //        cbmDepositPerUnit = new ComboBoxModel(key,value);
        cbmMinDepositPeriod = new ComboBoxModel(key,value);
        cbmMaxDepositPeriod = new ComboBoxModel(key,value);
        cbmPrematureWithdrawal = new ComboBoxModel(key,value);
        cbmPeriodInMultiplesOf = new ComboBoxModel(key,value);
        cbmIntPeriodForBackDatedRenewal = new ComboBoxModel(key,value);
        cbmMinimumRenewalDeposit = new ComboBoxModel(key,value);
        cbmRenewedclosedbefore = new ComboBoxModel(key,value);
        cbmMaxMDt = new ComboBoxModel(key,value);
        cbmMaxPDt = new ComboBoxModel(key,value);
        cbmMinNoOfDays = new ComboBoxModel(key,value);
        cbmMaturityDateAfterLastInstalPaid = new ComboBoxModel(key,value);
        cbmAgentCommision = new ComboBoxModel(key,value);
        cbmMinimumPeriod = new ComboBoxModel(key,value);
        cbmInterestNotPayingValue = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.LOANPERIODS"));
        cbmIntProvisioningFreq = new ComboBoxModel(key,value);
        cbmIntCompoundingFreq = new ComboBoxModel(key,value);
        cbmIntApplnFreq = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.INT_MAINTAIN"));
        cbmIntMaintainedAsPartOf = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.INT_CALC_METHOD"));
        cbmIntCalcMethod = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.PROV_LEVEL"));
        cbmProvisioningLevel = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.NO_DAYS_YEAR"));
        cbmNoOfDays = new ComboBoxModel(key,value);
        
        
        getKeyValue((HashMap)keyValue.get("LOANPRODUCT.LOANPERIODS"));
        cbmInstallmentToBeCharged = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.CHANGE_VALUE"));
        cbmChangeValue = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITSPRODUCT.INT_CALC_METHOD"));
        cbmDepositsFrequency = new ComboBoxModel(key,value);
        
        //Agents Commision Process
        getKeyValue((HashMap)keyValue.get("PERIOD"));
        cbmFromPeriod = new ComboBoxModel(key,value);
        cbmToPeriod = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE_FROM"));
        cbmFromAmount = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("WEEK_DAYS"));
        cbmWeekly = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DAILY_INT_CALC_METHOD"));
        cbmMonthlyIntCalcMethod = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("AGENT_COMM_CALC_METHOD"));
        cbmAgentCommCalcMethod = new ComboBoxModel(key,value);
        
        // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
        getKeyValue((HashMap)keyValue.get("RATE_SELECTION"));
        cbmPrmatureCloseProduct = new ComboBoxModel(key,value);
        cbmprmatureCloseRate = new ComboBoxModel(key,value);
        cbmIrregularRDCloseProduct = new ComboBoxModel(key,value);
        cbmIrregularRDCloseRate = new ComboBoxModel(key,value);
        
        getKeyValue((HashMap)keyValue.get("DEPOSITS_INT_RATE_APPLY"));
        cbmEitherofTwoRatesChoose=new ComboBoxModel(key,value);
        getKeyValue((HashMap)keyValue.get("DEPOSIT.AMT_RANGE"));
        cbmToAmount = new ComboBoxModel(key,value);
        int idx=key.indexOf("0");
        key.remove(idx);
        value.remove(idx);
        HashMap whereMap = new HashMap();
        System.out.println("keykeykeykeykeykey"+key);
//        lookUpHash.put(CommonConstants.MAP_NAME, "getShift");
        key= new ArrayList();
        value= new ArrayList();
        key.add("");
        value.add("");
      List list= ClientUtil.executeQuery("getFD", whereMap);
        if(list!= null && list.size()>0)
        {
        whereMap= new HashMap();   
        for(int i=0;i<list.size();i++){
        DepositsProductTO depTo=(DepositsProductTO)list.get(i);
        
        key.add(depTo.getProdId());
        value.add(depTo.getProdDesc());
        }
        cbmDepositsProdFixd= new ComboBoxModel(key,value);
        } 
// added by chithra 17-05-14
       key= new ArrayList();
        value= new ArrayList();
        key.add("");
        value.add("");
        whereMap = new HashMap();
       list= ClientUtil.executeQuery("getProductDataOAForSB", whereMap);
        HashMap temp = new HashMap();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                temp = (HashMap) list.get(i);
                key.add(temp.get("PROD_ID"));
                value.add(temp.get("PROD_DESC"));
            }
            
            cbmSbrateModel=new ComboBoxModel(key,value);
            cbmSbProduct = new ComboBoxModel(key,value);
        }
    }
    
    private void getKeyValue(HashMap keyValue)  throws Exception{
        key = (ArrayList)keyValue.get(CommonConstants.KEY);
        value = (ArrayList)keyValue.get(CommonConstants.VALUE);
    }
    
    //Set of codes added by Anju Anand for Mantis Id: 0010363
    public void setProductType() {
        key.clear();
        value.clear();
        key = new ArrayList();
        value = new ArrayList();
        key.add("");
        value.add("");
        key.add(1, "Installment");
        value.add(1, "Installment");
        key.add(2, "Percentage");
        value.add(2, "Percentage");
        cbmProductType = new ComboBoxModel(key, value);
        setCbmProductType(cbmProductType);
    }
    
     public int insertIntoAgentCommissionSlabTableData(int rowNo) {
        objAgentCommissionSlabSettingsTO = (AgentCommissionSlabSettingsTO) setAgentCommissionSlabSettingsTO();
        ArrayList row = new ArrayList();
        if (rowNo == -1) {
            if (objAgentCommissionSlabSettingsTO != null) {
                row.add(objAgentCommissionSlabSettingsTO);
                agentCommRecordData.add(objAgentCommissionSlabSettingsTO);
                ArrayList irRow = this.setAgentCommSlabRow(objAgentCommissionSlabSettingsTO);
                tbmAgentCommSlabSettings.insertRow(tbmAgentCommSlabSettings.getRowCount(), irRow);
            }
        } else {
            objAgentCommissionSlabSettingsTO = updateAgentCommSlabTableDataTO((AgentCommissionSlabSettingsTO) agentCommRecordData.get(rowNo), objAgentCommissionSlabSettingsTO);
            ArrayList irRow = setAgentCommSlabRow(objAgentCommissionSlabSettingsTO);
            agentCommRecordData.set(rowNo, objWeeklyDepositSlabTo);
            tbmAgentCommSlabSettings.removeRow(rowNo);
            tbmAgentCommSlabSettings.insertRow(rowNo, irRow);
        }
        tbmAgentCommSlabSettings.fireTableDataChanged();
        //ttNotifyObservers();
        return 0;
    }
     
   private AgentCommissionSlabSettingsTO updateAgentCommSlabTableDataTO(AgentCommissionSlabSettingsTO oldObjAgentCommissionSlabSettingsTO, AgentCommissionSlabSettingsTO newObjAgentCommissionSlabSettingsTO) {
        oldObjAgentCommissionSlabSettingsTO.setAmtFrom(newObjAgentCommissionSlabSettingsTO.getAmtFrom());
        oldObjAgentCommissionSlabSettingsTO.setAmtTo(newObjAgentCommissionSlabSettingsTO.getAmtTo());
        oldObjAgentCommissionSlabSettingsTO.setCommissionPercent(newObjAgentCommissionSlabSettingsTO.getCommissionPercent());      
        return oldObjAgentCommissionSlabSettingsTO;
    }
  
    
    private ArrayList setAgentCommSlabRow(AgentCommissionSlabSettingsTO objAgentCommissionSlabSettingsTO) {
        ArrayList row = new ArrayList();
        row.add(objAgentCommissionSlabSettingsTO.getAmtFrom());
        row.add(objAgentCommissionSlabSettingsTO.getAmtTo());
        row.add(objAgentCommissionSlabSettingsTO.getCommissionPercent());
        return row;
    }
     
     
     public AgentCommissionSlabSettingsTO setAgentCommissionSlabSettingsTO() {
        AgentCommissionSlabSettingsTO objAgentCommissionSlabSettingsTO = new AgentCommissionSlabSettingsTO();
        objAgentCommissionSlabSettingsTO.setProdId(getTxtProductID());
        objAgentCommissionSlabSettingsTO.setProdType(getCboProductType());        
        objAgentCommissionSlabSettingsTO.setAmtFrom(CommonUtil.convertObjToDouble(getTxtAgentCommSlabAmtFrom()));
        objAgentCommissionSlabSettingsTO.setAmtTo(CommonUtil.convertObjToDouble(getTxtAgentCommSlabAmtTo()));
        objAgentCommissionSlabSettingsTO.setCommissionPercent(CommonUtil.convertObjToDouble(getTxtAgentCommSlabPercent()));
        objAgentCommissionSlabSettingsTO.setCreatedBy(TrueTransactMain.USER_ID);
        objAgentCommissionSlabSettingsTO.setCreatedDt(curDate);
        objAgentCommissionSlabSettingsTO.setStatus("CREATED");
        objAgentCommissionSlabSettingsTO.setStatusBy(TrueTransactMain.USER_ID);
        objAgentCommissionSlabSettingsTO.setStatusDt(curDate);
        objAgentCommissionSlabSettingsTO.setAuthorizeStatus("");
        objAgentCommissionSlabSettingsTO.setAuthorizeBy("");
        objAgentCommissionSlabSettingsTO.setAuthorizeDt(null);
        return objAgentCommissionSlabSettingsTO;
    }

    public int insertIntoTableData(int rowNo) {
        objWeeklyDepositSlabTo = (WeeklyDepositSlabSettingsTO) setWeeklyDepositTo();
        ArrayList row = new ArrayList();
        if (rowNo == -1) {
            if (objWeeklyDepositSlabTo != null) {
                row.add(objWeeklyDepositSlabTo);
                recordData.add(objWeeklyDepositSlabTo);
                ArrayList irRow = this.setRow(objWeeklyDepositSlabTo);
                tbmWeeklySlabSettings.insertRow(tbmWeeklySlabSettings.getRowCount(), irRow);
            }
        } else {
            objWeeklyDepositSlabTo = updateTableDataTO((WeeklyDepositSlabSettingsTO) recordData.get(rowNo), objWeeklyDepositSlabTo);
            ArrayList irRow = setRow(objWeeklyDepositSlabTo);
            recordData.set(rowNo, objWeeklyDepositSlabTo);
            tbmWeeklySlabSettings.removeRow(rowNo);
            tbmWeeklySlabSettings.insertRow(rowNo, irRow);
        }
        tbmWeeklySlabSettings.fireTableDataChanged();
        ttNotifyObservers();
        return 0;
    }

    public WeeklyDepositSlabSettingsTO setWeeklyDepositTo() {
        WeeklyDepositSlabSettingsTO objWeeklyDepositSlabTo = new WeeklyDepositSlabSettingsTO();
        objWeeklyDepositSlabTo.setProdId(getTxtProductID());
        objWeeklyDepositSlabTo.setProdType(getCboProductType());
        objWeeklyDepositSlabTo.setInstFrom(CommonUtil.convertObjToInt(getTxtInstallmentFrom()));
        objWeeklyDepositSlabTo.setInstTo(CommonUtil.convertObjToInt(getTxtInstallmentTo()));
        objWeeklyDepositSlabTo.setInstNo(CommonUtil.convertObjToInt(getTxtInstallmentNo()));
        objWeeklyDepositSlabTo.setPenal(CommonUtil.convertObjToInt(getTxtPenal()));
        objWeeklyDepositSlabTo.setCreatedBy(TrueTransactMain.USER_ID);
        objWeeklyDepositSlabTo.setCreatedDt(curDate);
        objWeeklyDepositSlabTo.setStatus("CREATED");
        objWeeklyDepositSlabTo.setStatusBy(TrueTransactMain.USER_ID);
        objWeeklyDepositSlabTo.setStatusDt(curDate);
        objWeeklyDepositSlabTo.setAuthorizeStatus("");
        objWeeklyDepositSlabTo.setAuthorizeBy("");
        objWeeklyDepositSlabTo.setAuthorizeDt(null);
        return objWeeklyDepositSlabTo;
    }

    private ArrayList setRow(WeeklyDepositSlabSettingsTO objWeeklyDepositTo) {
        ArrayList row = new ArrayList();
        row.add(objWeeklyDepositTo.getProdType());
        row.add(objWeeklyDepositTo.getInstFrom());
        row.add(objWeeklyDepositTo.getInstTo());
        row.add(objWeeklyDepositTo.getPenal());
        row.add(objWeeklyDepositTo.getInstNo());
        return row;
    }

     // Added by nithya on 02-03-2016 for 0003897
    private ArrayList setRow(DepositsThriftBenevolentTO objDepositsThriftBenevolentTO) {
        ArrayList row = new ArrayList();
        row.add(objDepositsThriftBenevolentTO.getEffectiveDate());  
        row.add(objDepositsThriftBenevolentTO.getInstallmentAmount());              
        return row;
    }
    // End
    private WeeklyDepositSlabSettingsTO updateTableDataTO(WeeklyDepositSlabSettingsTO oldWeeklyDepositTo, WeeklyDepositSlabSettingsTO newWeeklyDepositTo) {
        oldWeeklyDepositTo.setInstFrom(newWeeklyDepositTo.getInstFrom());
        oldWeeklyDepositTo.setInstTo(newWeeklyDepositTo.getInstTo());
        oldWeeklyDepositTo.setInstNo(newWeeklyDepositTo.getInstNo());
        oldWeeklyDepositTo.setPenal(newWeeklyDepositTo.getPenal());
        oldWeeklyDepositTo.setProdType(newWeeklyDepositTo.getProdType());
        return oldWeeklyDepositTo;
    }

    public void setTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Type");
        columnHeader.add("Install From");
        columnHeader.add("Install To");
        columnHeader.add("Penal");
        columnHeader.add("Installment No");
        ArrayList data = new ArrayList();
        tbmWeeklySlabSettings = new TableModel(data, columnHeader);
    }
    
     // Added by nithya on 02-03-2016 for 0003897
    public void setThriftBenevolentTable() {
        ArrayList columnHeader = new ArrayList();
        
        columnHeader.add("Effective Date");
        columnHeader.add("Installment");
       
        ArrayList data = new ArrayList();
        tbmThriftBenevolent = new TableModel(data, columnHeader);
    }
    // End
    
    public void setAgentCommissionSlabTable() {
        ArrayList columnHeader = new ArrayList();
        columnHeader.add("Amount From");
        columnHeader.add("Amount To");
        columnHeader.add("Percentage");
        ArrayList data = new ArrayList();
        tbmAgentCommSlabSettings = new TableModel(data, columnHeader);
    }
    
    public void populateTableData(int rowNum) {
        WeeklyDepositSlabSettingsTO objWeeklyDepositSlabTo = (WeeklyDepositSlabSettingsTO) recordData.get(rowNum);
        setTableValues(objWeeklyDepositSlabTo);
        ttNotifyObservers();
    }
    
    public void populateAgentCommissionSlabSettingsTableData(int rowNum) {
        AgentCommissionSlabSettingsTO objAgentCommissionSlabSettingsTO = (AgentCommissionSlabSettingsTO) agentCommRecordData.get(rowNum);
        System.out.println("objAgentCommissionSlabSettingsTO :: " + objAgentCommissionSlabSettingsTO);
        setAgentCommissionSlabTableValues(objAgentCommissionSlabSettingsTO);      
    }

    private void setTableValues(WeeklyDepositSlabSettingsTO objWeeklyDepositSlabTo) {
        setCboProductType(objWeeklyDepositSlabTo.getProdType());
        setTxtInstallmentFrom(CommonUtil.convertObjToStr(objWeeklyDepositSlabTo.getInstFrom()));
        setTxtInstallmentTo(CommonUtil.convertObjToStr(objWeeklyDepositSlabTo.getInstTo()));
        setTxtInstallmentNo(CommonUtil.convertObjToStr(objWeeklyDepositSlabTo.getInstNo()));
        setTxtPenal(CommonUtil.convertObjToStr(objWeeklyDepositSlabTo.getPenal()));
    } 
    
     private void setAgentCommissionSlabTableValues(AgentCommissionSlabSettingsTO onjAgentCommissionSlabSettingsTO) {
       
        setTxtAgentCommSlabAmtFrom(CommonUtil.convertObjToStr(onjAgentCommissionSlabSettingsTO.getAmtFrom()));
        setTxtAgentCommSlabAmtTo(CommonUtil.convertObjToStr(onjAgentCommissionSlabSettingsTO.getAmtTo()));
        setTxtAgentCommSlabPercent(CommonUtil.convertObjToStr(onjAgentCommissionSlabSettingsTO.getCommissionPercent()));
    } 
    
    void deleteAgentCommissionSlabTblData(int rowSelected) {
        objAgentCommissionSlabSettingsTO = (AgentCommissionSlabSettingsTO) agentCommRecordData.get(rowSelected);
        agentCommDeleteData.add(objAgentCommissionSlabSettingsTO);
        agentCommRecordData.remove(rowSelected);
        tbmAgentCommSlabSettings.removeRow(rowSelected);
        tbmAgentCommSlabSettings.fireTableDataChanged();
    }
   
    
    void deleteTblData(int rowSelected) {
        objWeeklyDepositSlabTo = (WeeklyDepositSlabSettingsTO) recordData.get(rowSelected);
        deleteData.add(objWeeklyDepositSlabTo);
        recordData.remove(rowSelected);
        tbmWeeklySlabSettings.removeRow(rowSelected);
        tbmWeeklySlabSettings.fireTableDataChanged();
    }
     // Added by nithya on 02-03-2016 for 0003897
    void deleteThriftBenevolentTableData(){
        if(tbmThriftBenevolent.getRowCount() > 0){
         for(int i=0; i< tbmThriftBenevolent.getRowCount(); i++){
           tbmThriftBenevolent.removeRow(i);
         } 
        } 
    }
    
    public void resetWeeklySettings() {
        setCboProductType("");
        setTxtInstallmentFrom("");
        setTxtInstallmentTo("");
        setTxtInstallmentNo("");
        setTxtPenal("");
        recordData = new ArrayList();
        this.tbmWeeklySlabSettings.setData(new ArrayList());
        this.tbmWeeklySlabSettings.fireTableDataChanged();
        this.recordData.clear();
        this.deleteData.clear();
    }
    
    public void resetAgentCommissionSlabSettings() {        
        setTxtAgentCommSlabAmtFrom("");
        setTxtAgentCommSlabAmtTo("");
        setTxtAgentCommSlabPercent("");        
        agentCommRecordData = new ArrayList();
        this.tbmAgentCommSlabSettings.setData(new ArrayList());
        this.tbmAgentCommSlabSettings.fireTableDataChanged();
        this.agentCommRecordData.clear();
        this.agentCommDeleteData.clear();
    }

    private void populateTable(List lstData) {
        recordData = new ArrayList();
        int size = lstData.size();
        setTable();
        for (int i = 0; i < size; i++) {
            WeeklyDepositSlabSettingsTO objWeeklyDepSettingsTo = new WeeklyDepositSlabSettingsTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) lstData.get(i);
            objWeeklyDepSettingsTo.setProdId(CommonUtil.convertObjToStr(newMap.get("PROD_ID")));
            objWeeklyDepSettingsTo.setProdType(CommonUtil.convertObjToStr(newMap.get("TYPE")));
            objWeeklyDepSettingsTo.setInstFrom(CommonUtil.convertObjToInt(newMap.get("FROM_INSTALL")));
            objWeeklyDepSettingsTo.setInstTo(CommonUtil.convertObjToInt(newMap.get("TO_INSTALL")));
            objWeeklyDepSettingsTo.setInstNo(CommonUtil.convertObjToInt(newMap.get("INSTALLMENT_NO")));
            objWeeklyDepSettingsTo.setPenal(CommonUtil.convertObjToInt(newMap.get("PENAL")));
            recordData.add(objWeeklyDepSettingsTo);
            ArrayList irRow = this.setRow(objWeeklyDepSettingsTo);
            tbmWeeklySlabSettings.insertRow(tbmWeeklySlabSettings.getRowCount(), irRow);
        }
        setTbmWeeklySlabSettings(tbmWeeklySlabSettings);
        tbmWeeklySlabSettings.fireTableDataChanged();
        ttNotifyObservers();
    }    
    
    //End of code added by Anju Anand for Mantis Id: 0010363
    
    private void populateAgentCommissionSlabTable(List lstData) {
        agentCommRecordData = new ArrayList();
        int size = lstData.size();
        setTable();
        for (int i = 0; i < size; i++) {
            AgentCommissionSlabSettingsTO objAgentCommissionSlabSettingsTO = new AgentCommissionSlabSettingsTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) lstData.get(i);
            System.out.println("newmap :::" + newMap);
            objAgentCommissionSlabSettingsTO.setAmtFrom(CommonUtil.convertObjToDouble(newMap.get("FROM_AMT")));
            objAgentCommissionSlabSettingsTO.setAmtTo(CommonUtil.convertObjToDouble(newMap.get("TO_AMT")));
            objAgentCommissionSlabSettingsTO.setCommissionPercent(CommonUtil.convertObjToDouble(newMap.get("COMM_PERCENTAGE")));
            
            agentCommRecordData.add(objAgentCommissionSlabSettingsTO);
            ArrayList irRow = this.setAgentCommSlabRow(objAgentCommissionSlabSettingsTO);
            tbmAgentCommSlabSettings.insertRow(tbmAgentCommSlabSettings.getRowCount(), irRow);
        }
        setTbmAgentCommSlabSettings(tbmAgentCommSlabSettings);
        tbmAgentCommSlabSettings.fireTableDataChanged();
        ttNotifyObservers();
    }    
    

   // Added by nithya on 02-03-2016 for 0003897
    
    private void populateThriftBenevolentTable(List lstData) {
        thriftBenevolentRecord=  new ArrayList();
        int size = lstData.size();
        setThriftBenevolentTable();
        for (int i = 0; i < size; i++) {
            DepositsThriftBenevolentTO objDepositsThriftBenevolentTO = new DepositsThriftBenevolentTO();
            HashMap newMap = new HashMap();
            newMap = (HashMap) lstData.get(i);          
            String effectiveDate = CommonUtil.convertObjToStr(newMap.get("EFFECTIVE_DATE"));            
            objDepositsThriftBenevolentTO.setEffectiveDate(DateUtil.getDateMMDDYYYY(effectiveDate));            
            objDepositsThriftBenevolentTO.setInstallmentAmount(CommonUtil.convertObjToDouble(newMap.get("INSTALLMENT_AMOUNT")));            
            thriftBenevolentRecord.add(objDepositsThriftBenevolentTO);
            ArrayList irRow = this.setRow(objDepositsThriftBenevolentTO);            
            tbmThriftBenevolent.insertRow(tbmThriftBenevolent.getRowCount(), irRow);
        }
        setTbmThriftBenevolent(tbmThriftBenevolent);
        tbmThriftBenevolent.fireTableDataChanged();
        ttNotifyObservers();
    }
    
    // End
    
    public void resetForm(){
        //        setTblInterestTable(null);
        setTxtProductID("");
        setTxtDesc("");
        setTxtAcctHd("");
        setCboOperatesLike("");
        //Schema...
        setRdoCalcTDS_Yes(false);
        setRdoWithPeriod("");
        setRdoDoublingScheme("");
        setRdoCalcTDS_No(false);
        setDiscounted_No(false);
        //        setDiscountedRate("");
        setDiscounted_Yes(false);
        setRdoPayInterestOnHoliday_Yes(false);
        setRdoPayInterestOnHoliday_No(false);
        setRdoAmountRounded_Yes(false);
        setRdoAmountRounded_No(false);
        setRdoInterestAfterMaturity_Yes(false);
        setRdoInterestAfterMaturity_No(false);
        setCboMaturityInterestType("");
        setCboMaturityInterestRate("");
        setCboInterestCriteria("");
        //        setCboDepositPerUnit("");
        setTxtDepositPerUnit("");
        setCboMinDepositPeriod("");
        setCboInstType("");
        setCboPrematureWithdrawal("");
        setTxtMinDepositPeriod("");
        setCboMaxDepositPeriod("");
        setTxtMaxDepositPeriod("");
        setCboPeriodInMultiplesOf("");
        setTxtPeriodInMultiplesOf("");
        setTxtMinDepositAmt("");
        setTxtMaxDepositAmt("");
        setTxtAmtInMultiplesOf("");
        setTxtCategoryBenifitRate("");
        setRdoCalcMaturityValue_Yes(false);
        setRdoCalcMaturityValue_No(false);
        setRdoProvisionOfInterest_Yes(false);
        setRdoProvisionOfInterest_No(false);
        setRdoTransferToMaturedDeposits_Yes(false);
        setRdoTransferToMaturedDeposits_No(false);
        setRdoPartialWithdrawalAllowed_Yes(false);
        setRdoPartialWithdrawalAllowed_No(false);
        setRdoWithdrawalWithInterest_Yes(false);
        setRdoWithdrawalWithInterest_No(false);
        setRdoServiceCharge_Yes(false);
        setRdoServiceCharge_No(false);
        setRdoInsBeyondMaturityDt_Yes(false);
        setRdoClosedwithinperiod_Yes(false);
        setRdoInsBeyondMaturityDt_No(false);
        setTxtServiceCharge("");
        setRdoPartialWithdrawalAllowed_No(false);
        setRdoPartialWithdrawalAllowed_Yes(false);
        setRdoExtnOfDepositBeforeMaturity_Yes(false);
        setRdoExtnOfDepositBeforeMaturity_No(false);
        setRdoAutoAdjustment_Yes(false);
        setRdoAutoAdjustment_No(false);
        setRdoAdjustIntOnDeposits_Yes(false);
        setRdoAdjustIntOnDeposits_No(false);
        setRdoAdjustPrincipleToLoan_Yes(false);
        setRdoAdjustPrincipleToLoan_No(false);
        setRdoFlexiFromSBCA_Yes(false);
        setRdoFlexiFromSBCA_No(false);
        setRdoTermDeposit_Yes(false);
        setRdoTermDeposit_No(false);
        setRdoRenewalOfDepositAllowed_Yes(false);
        setRdoRenewalOfDepositAllowed_No(false);
        setRdoAutoRenewalAllowed_Yes(false);
        setRdoAutoRenewalAllowed_No(false);
        setRdoSameNoRenewalAllowed_No(false);
        setRdoSameNoRenewalAllowed_Yes(false);
        //RD...
        setRdoLastInstallmentAllowed_Yes(false);
        setRdoLastInstallmentAllowed_No(false);
        setRdoPenaltyOnLateInstallmentsChargeble_Yes(false);
        setRdoPenaltyOnLateInstallmentsChargeble_No(false);
        setRdoRecurringDepositToFixedDeposit_Yes(false);
        setRdoRecurringDepositToFixedDeposit_No(false);
        setRdoInstallmentInRecurringDepositAcct_Yes(false);
        setRdoInstallmentInRecurringDepositAcct_No(false);
        setRdoCanReceiveExcessInstal_yes(false);
        setRdoCanReceiveExcessInstal_No(false);
        setRdoIntPayableOnExcessInstal_Yes(false);
        setRdoIntPayableOnExcessInstal_No(false);
        setRdoIntroducerReqd_Yes(false);
        setRdoIntroducerReqd_No(false);
        setRdoSBRateOneRate_Yes(false);
        setRdoSBRateOneRate_No(false);
        setChkFDRenewalSameNoTranPrincAmt(false);
        //        setRdoSystemCalcValues_Yes(false);
        //        setRdoSystemCalcValues_No(false);
        setRdoCertificate_printing("");
        setRdoIntProvisioningApplicable_Yes(false);
        setRdoIntProvisioningApplicable_No(false);
        setRdoRecalcOfMaturityValue_Yes(false);
        setRdoRecalcOfMaturityValue_No(false);
        setRdoClosedwithinperiod_No(false);
        setRdoRegular(false);
        setRdoNRO(false);
        setRdoNRE(false);
        setRdoStaffAccount_Yes(false);
        setRdoStaffAccount_No(false);
        
        setTxtAfterHowManyDays("");
        setTxtAdvanceMaturityNoticeGenPeriod("");
        setTxtInterestOnMaturedDeposits("");
        setTxtProductID("");
        setTxtDesc("");
        setTxtAcctHd("");
        setCboOperatesLike("");
        setCboRoundOffCriteria("");
        setCboProdCurrency("");
        setLblAuthorize("");
        setTdtSchemeIntroDate("");
        setTdtSchemeClosingDate("");
        setTxtNoOfPartialWithdrawalAllowed("");
        setTxtMaxAmtOfPartialWithdrawalAllowed("");
        setTxtMaxNoOfPartialWithdrawalAllowed("");
        setTxtMinAmtOfPartialWithdrawalAllowed("");
        setTxtWithdrawalsInMultiplesOf("");
        setTxtPrematureWithdrawal("");
        setCboMaturityInterestType("");
        setCboMaturityInterestRate("");
        setCboInterestCriteria("");
        //        setCboDepositPerUnit("");
        setTxtDepositPerUnit("");
        setCboMinDepositPeriod("");
        setTxtMinDepositPeriod("");
        setCboMaxDepositPeriod("");
        setTxtMaxDepositPeriod("");
        setCboPeriodInMultiplesOf("");
        setTxtPeriodInMultiplesOf("");
        setTxtMinDepositAmt("");
        setTxtMaxDepositAmt("");
        setTxtAmtInMultiplesOf("");
        setTxtAcctHd("");
        setTxtAlphaSuffix("");
        setTxtMaxAmtOfCashPayment("");
        setTxtMinAmtOfPAN("");
        setTdtSchemeIntroDate("");
        setTdtSchemeClosingDate("");
        //        setTxtScheme("");
        setTxtLimitForBulkDeposit("");
        setTxtAcctNumberPattern("");
        setTxtSuffix("");
        setTxtLastAcctNumber("");
        setTxtAfterNoDays("");
        //Int Pay...
        setCboIntMaintainedAsPartOf("");
        setCboIntCalcMethod("");
        setCboIntProvisioningFreq("");
        setCboProvisioningLevel("");
        setCboNoOfDays("");
        setCboIntCompoundingFreq("");
        setCboInterestType("");
        setCboIntApplnFreq("");
        setTdtLastIntProvisionalDate("");
        setTdtNextIntProvisionalDate("");
        setTdtLastInterestAppliedDate("");
        setTdtNextInterestAppliedDate("");
        setCboIntRoundOff("");
        setTxtMinIntToBePaid("");
        //Achd...
        setTxtIntProvisioningAcctHd("");
        setTxtIntPaybleGLHead("");
        setTxtIntDebitPLHead("");
        setTxttMaturityDepositAcctHead("");
        setTxtIntOnMaturedDepositAcctHead("");
        setTxtIntProvisionOfMaturedDeposit("");
        setTxtAcctHeadForFloatAcct("");
        setTxtFixedDepositAcctHead("");
        setTxtInterestRecoveryAcHd("");
        setTxtPenalChargesAchd("");
        setTxtTransferOutAcHd("");
        //Tax...
        setTxtTDSGLAcctHd("");
        //Renewal...
        setCboMaxPeriodMDt("");
        setTxtMaxPeriodMDt("");
        setTxtMaxPeriodPDt("");
        setCboIntPeriodForBackDatedRenewal("");
        setTxtIntPeriodForBackDatedRenewal("");
        setCboIntType("");
        setTdtMaturityDate("");
        setTdtPresentDate("");
        setCboIntCriteria("");
        setTxtMaxNopfTimes("");
        setTxtMaxNoSameNo("");
        setTxtMinNoOfDays("");
        setCboMinNoOfDays("");
        setTxtMaturityDateAfterLastInstalPaid("");
        setCboMaturityDateAfterLastInstalPaid("");
        setTxtAgentCommision("");
        setCboAgentCommision("");
        setTxtInterestNotPayingValue("");
        setCboInterestNotPayingValue("");
        setTxtMinimumPeriod("");
        setCboMinimumPeriod("");
        setRdoRecurringDepositToFixedDeposit_Yes(false);
        setRdoRecurringDepositToFixedDeposit_No(false);
        setRdoInstallmentInRecurringDepositAcct_Yes(false);
        setRdoInstallmentInRecurringDepositAcct_No(false);
        setCboInstallmentToBeCharged("");
        setCboDepositsFrequency("");
        setCboChangeValue("");
        setCboCutOffDayForPaymentOfInstal("");
        setTxtCutOffDayForPaymentOfInstal("");
        setTdtFromDepositDate("");
        setTdtChosenDate("");
        setTxtCommisionPaybleGLHead("");
        setRdoDaily(false);
        setRdoWeekly(false);
        setRdoMonthly(false);
        setCboWeekly_Day("");
        setCboMonthlyIntCalcMethod("");
        setRdoPartialWithdrawlAllowedForDD("");
        setRdoInterestalreadyPaid_Yes(false);
        setRdoInterestalreadyPaid_No(false);
        setRdoInterestrateappliedoverdue_Yes(false);
        setRdoInterestrateappliedoverdue_No(false);
        setCboEitherofTwoRatesChoose("");
        setRdoBothRateNotAvail_Yes(false);
        setRdoBothRateNotAvail_No(false);
        setRdoExtensionBeyondOriginalDate_No(false);
        setRdoExtensionBeyondOriginalDate_Yes(false);
        setRdoExtensionPenal_Yes(false);
        setRdoExtensionPenal_No(false);
        setTxtMinimumRenewalDeposit("");
        setCboMinimumRenewalDeposit("");
        setCboRoundOffCriteriaPenal("");
        setTxtRenewedclosedbefore("");
        setCboRenewedclosedbefore("");
        setCboDepositsProdFixd("");
        setPostageAcHd("");
        setRdoIncaseOfIrregularRDRBRate(false);
        setRdoIncaseOfIrregularRDSBRate(false);
        setTxtRDIrregularIfInstallmentDue("");
        setChkRateAsOnDateOfMaturity(false);
        setRdoDepositRate(false);
        setChkPrematureClosure("N");
        setChkIntApplicationSlab("N");
        setRdoPenalRounded_No("N");
        setRdoPenalRounded_Yes("Y");
        setRdoSBRate(false);
        resetDataList();
       setCbxInterstRoundTime("N");
       setChkCanZeroBalAct("N");
       setChkAppNormRate("N");
       setTxtNormPeriod("");
       setChkSlabWiseInterest(false);
       setChkSlabWiseCommision(false);
       setSbRateCmb("");// added by chithra 17-05-14
       setCboPreMatIntType("");
       setCboSbProduct("");
       setChkWeeklySpecial("N");
       setChkRdNature("N");
       setDueOn(false);
       setInstallmentAmount(""); // Added by nithya
       setEffectiveDate(""); // Added by nithya
       setEffectiveDateEntered(null); // Added by nithya
       deleteThriftBenevolentTableData();//Added by nithya
       setTxtGracePeriod("");
       setChkIsGroupDepositProduct("");// Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
       setTxtBenIntReserveHead("");
       // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
       setChkRDClosingOtherROI("");
       setCboPrmatureCloseProduct("");
       setCboprmatureCloseRate("");
       setCboIrregularRDCloseProduct("");
       setCboIrregularRDCloseRate("");
       setChkIntForIrregularRD("");// Added by nithya on 18-03-2020 for KD-1535
       setChkSpecialRD("");
       setTxtSpecialRDInstallments(""); // Added by nithya on 01-04-2020 for KD-1535
       setChkAgentCommSlabRequired("N");              
       ttNotifyObservers();
    }
    
    public void resetOBFields(){
        //        this.setCboAgentType("");
        //        this.tbmInterestTable.setData(new ArrayList());
        //        this.tbmInterestTable.fireTableDataChanged();
        //        this.interestList.clear();
        //        this.interestList.clear();
    }
    
    
    public void resetDataList(){
        interestList = null;
        interestDeleteTabRow = null;
        
        interestList = new ArrayList();
        interestDeleteTabRow = new ArrayList();
    }
    
    
    public void resetTable(){
        log.info("In resetTable...");
        setTdtDate("");
        setCboFromAmount("");
        setCboToAmount("");
        setTxtFromPeriod("");
        setCboFromPeriod("");
        setTxtToPeriod("");
        setCboToPeriod("");
        setTxtRateInterest("");
        setCboInstType("");
        setTdtToDate("");
        setTxtFromAmt("");
        setTxtToAmt("");
        ttNotifyObservers();
        
    }
    
    /** To perform the appropriate operation */
    public void doAction() {
        try {
            log.info("Inside doAction()");
            //If actionType such as NEW, EDIT, DELETE, then proceed
            if( actionType != ClientConstants.ACTIONTYPE_CANCEL ){
                //If actionType has got propervalue then doActionPerform, else throw error
                if( getCommand() != null ){
                    doActionPerform();
                }
                else{
                    final DepositsProductRB objDepositsProductRB = new DepositsProductRB();
                    throw new TTException(objDepositsProductRB.getString("TOCommandError"));
                }
            }
        } catch (Exception e) {
            log.info("error in doAction");
            setResult(ClientConstants.ACTIONTYPE_FAILED);
            parseException.logException(e,true);
        }
    }
    
    private String getCommand() throws Exception{
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
    
    /** To perform the necessary action */
    private void doActionPerform() throws Exception{
        log.info("doActionPerform");
        final DepositsProductTO objDepositsProductTO = setDepositsProductData();
        final DepositsProductSchemeTO objDepositsProductSchemeTO = setSchemeData();
        final DepositsProductIntPayTO objDepositsProductIntPayTO = setIntPayData();
        final DepositsProductAcHdTO objDepositsProductAcHdTO = setAcHdData();
        final DepositsProductTaxTO objDepositsProductTaxTO = setTaxData();
        final DepositsProductRenewalTO objDepositsProductRenewalTO = setRenewalData();
        final DepositsProductRDTO objDepositsProductRDTO = setRDData();
        // Added by nithya on 01-03-2016
        final DepositsThriftBenevolentTO objDepositsThriftBenevolentTO = setThriftBenevelontData(); // Added by nithya on 02-03-2016 for 0003897
        //Added by Anju Anand for Mantis Id: 0010363
        final WeeklyDepositSlabSettingsTO objWeeklyDepositSlabTo = setWeeklyDepositTo();
        final HashMap data = new HashMap();
        if (objWeeklyDepositSlabTo != null) {
            data.put("WeeklyDepositData", objWeeklyDepositSlabTo);
            data.put("WeeklyDepositSlabData", recordData);
        }
        
        if(getChkAgentCommSlabRequired().equalsIgnoreCase("Y")){
           data.put("AgentCommissionSlabData", agentCommRecordData); 
        }
        
        String behavesLike = (String)cbmOperatesLike.getKeyForSelected();
        
        final InterestMaintenanceRateTO objInterestMaintenanceRateTO = setInterestMaintenanceRate();
        
        objDepositsProductTO.setCommand(getCommand());
        data.put("DEPT_PROD",objDepositsProductTO);
        data.put("DEPT_PROD_SCHEME",objDepositsProductSchemeTO);
        data.put("DEPT_PROD_INTPAY",objDepositsProductIntPayTO);
        data.put("DEPT_PROD_ACHD",objDepositsProductAcHdTO);
        data.put("DEPT_PROD_TAX",objDepositsProductTaxTO);
        data.put("DEPT_PROD_RENEWAL",objDepositsProductRenewalTO);
        data.put("DEPT_PROD_RD",objDepositsProductRDTO);
        if(behavesLike.equals("RECURRING") || behavesLike.equals("DAILY")){
            data.put("AGENTS_COMMISION",interestList);
            data.put("AGENTS_COMMISION_DELETED_RECORD",interestDeleteTabRow);
        }
        // // Added by nithya on 02-03-2016 for 0003897
        if(behavesLike.equals("THRIFT") || behavesLike.equals("BENEVOLENT")){
            data.put("DEPO_THRIFT_BENEVOLENT",objDepositsThriftBenevolentTO);
        }
        // End
        data.put("PROD_MAP",getTxtProductID());
        sortData();
        
        data.put(CommonConstants.MODULE, getModule());
        data.put(CommonConstants.SCREEN, getScreen());
        int countRec=0;
        if(getActionType()==3) {
            HashMap hash=new HashMap();
            hash.put("PROD_ID",getTxtProductID());
            List lst =ClientUtil.executeQuery("DepositProductCount", hash);
            hash=(HashMap)lst.get(0);
            countRec=CommonUtil.convertObjToInt(hash.get("COUNTS"));
        }
        HashMap proxyResultMap=null;
        if(countRec==0){
            proxyResultMap = proxy.execute(data, operationMap);
            System.out.println("**** prod id :"+proxyResultMap);
            setProxyReturnMap(proxyResultMap);
        }
        else
            ClientUtil.displayAlert("THIS PRODUCT HAVING ACCOUNT");
        setResult(actionType);
        actionType = ClientConstants.ACTIONTYPE_CANCEL;
        resetForm();
        resetTable();
    }
    
    public ArrayList setdelayedData(){
        ArrayList data=tblDelayedInstallmet.getDataArrayList();
        ArrayList delayed = new ArrayList();
        delayedTabRow = new ArrayList();
        HashMap delayedRec;
        HashMap delayedMap = new HashMap();
        ArrayList delayedList = new ArrayList();
        final DepositsProductTO objDepositsProductTO = setDepositsProductData();
        try{
            int depSubNoSize = depSubNoAll.size();
            for(int i=0;i<depSubNoSize;i++){
                delayed = (ArrayList)data.get(i);
                delayedList.add(objDepositsProductTO.getProdId());
                delayedList.add(delayed.get(0));
                delayedList.add(delayed.get(1));
                delayedList.add(delayed.get(2));
            }
        }catch(Exception e){
            parseException.logException(e,true);
        }
        return delayedList;
    }
    
    /* To set common data in the Transfer Object -- DB Table: DEPOSITS_PRODUCT*/
    public DepositsProductTO setDepositsProductData() {
        log.info("setDepositsProductData");
        final DepositsProductTO objDepositsProductTO = new DepositsProductTO();
        try{
            objDepositsProductTO.setProdId( getTxtProductID());
            objDepositsProductTO.setProdDesc(getTxtDesc());
            objDepositsProductTO.setAcctHead(getTxtAcctHd());
            //modified of by rishad 24/07/2014 23:34
            objDepositsProductTO.setBehavesLike(CommonUtil.convertObjToStr(cbmOperatesLike.getKeyForSelected()));
           // objDepositsProductTO.setAuthorizeRemark(new Long(getTxtSuffix()));
             objDepositsProductTO.setAuthorizeRemark(CommonUtil.convertObjToLong(getTxtSuffix()));
           //objDepositsProductTO.setRemarks(new Long(getTxtLastAcctNumber()));
            objDepositsProductTO.setRemarks(CommonUtil.convertObjToLong(getTxtLastAcctNumber()));
            objDepositsProductTO.setStatusBy(TrueTransactMain.USER_ID);
            objDepositsProductTO.setStatusDt(curDate);
            objDepositsProductTO.setCreatedBy(TrueTransactMain.USER_ID);
            objDepositsProductTO.setCreatedDt(currDate);
            // objDepositsProductTO.setBaseCurrency((String)cbmProdCurrency.getKeyForSelected());
            //objDepositsProductTO.setRemarks (getTxtRemarks());
            objDepositsProductTO.setIsGroupDepositProduct(getChkIsGroupDepositProduct()); // Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
        }catch(Exception e){
            log.info("error in setDepositsProductData");
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsProductTO;
    }
    
    // Added by nithya on 01-03-2016 
    /* To set common data in the Transfer Object -- DB Table: DEPO_THRIFTBENEVOLENT_INSTALL */
    public DepositsThriftBenevolentTO setThriftBenevelontData(){
        log.info("setThriftBenevelontData");
        DepositsThriftBenevolentTO objDepositsThriftBenevolentTO = new DepositsThriftBenevolentTO();        
        try{
            if(getEffectiveDate().length() > 0){
                
                System.out.println("in setThriftBenevelontData  getEffectiveDate:: if ");
                objDepositsThriftBenevolentTO.setProductId(getTxtProductID());
                objDepositsThriftBenevolentTO.setOperatesLike(getCboOperatesLike());
                objDepositsThriftBenevolentTO.setInstallmentAmount(CommonUtil.convertObjToDouble(getInstallmentAmount()));
                System.out.println("in setThriftBenevelontData  getEffectiveDate:: " + getEffectiveDate());
                objDepositsThriftBenevolentTO.setEffectiveDate(DateUtil.getDateMMDDYYYY(getEffectiveDate()));
                
            }
            else{
                objDepositsThriftBenevolentTO = null;
            }
            
        }catch(Exception e){
            log.info("error in setThriftBenevelontData");
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsThriftBenevolentTO;
    }
    // End
    
    /* To set common data in the Transfer Object -- DB Table: DEPOSITS_PROD_SCHEME*/
    public DepositsProductSchemeTO setSchemeData() {
        log.info("setSchemeData");
        int multiply = 0;
        final DepositsProductSchemeTO objDepositsProductSchemeTO = new DepositsProductSchemeTO();
        try{
            objDepositsProductSchemeTO.setExcludeLienIntrstAppl(getChkExcludeLienIntrstAppl());
            objDepositsProductSchemeTO.setExcludeLienStanding(getChkExcludeLienStanding());
            objDepositsProductSchemeTO.setCummCertPrint(getChkCummCertPrint());
            objDepositsProductSchemeTO.setDepositUnlien(getChkDepositLien());
       
            objDepositsProductSchemeTO.setProdId(getTxtProductID());
            objDepositsProductSchemeTO.setCanZeroBalActOpeng(getChkCanZeroBalAct());
            if (getRdoCalcTDS_Yes() == true){
                objDepositsProductSchemeTO.setCalculateTds(YES);
            }else{
                objDepositsProductSchemeTO.setCalculateTds(NO);
            }
            if (getRdoPayInterestOnHoliday_Yes() == true){
                objDepositsProductSchemeTO.setPayintDepMaturity(YES);
            }else{
                objDepositsProductSchemeTO.setPayintDepMaturity(NO);
            }
            
            if (getRdoAmountRounded_Yes() == true){
                objDepositsProductSchemeTO.setMaturityAmtRound(YES);
            }else{
                objDepositsProductSchemeTO.setMaturityAmtRound(NO);
            }
            if (getDiscounted_Yes() == true)
                objDepositsProductSchemeTO.setDiscounted(YES);
            else
                objDepositsProductSchemeTO.setDiscounted(NO);
            
            objDepositsProductSchemeTO.setRoundoffCriteria((String)cbmRoundOffCriteria.getKeyForSelected());
            
            if (getRdoInterestAfterMaturity_Yes() == true){
                objDepositsProductSchemeTO.setIntpaidAftermaturity(YES);
            }else{
                objDepositsProductSchemeTO.setIntpaidAftermaturity(NO);
            }
            if (getRdoNRE() == true){
                objDepositsProductSchemeTO.setTypesOfDep(NRE);
            }else if(getRdoNRO() == true){
                objDepositsProductSchemeTO.setTypesOfDep(NRO);
            }else
                objDepositsProductSchemeTO.setTypesOfDep(REGULAR);
            
            if (getRdoStaffAccount_Yes() == true){
                objDepositsProductSchemeTO.setStaffAccount(YES);
            }else{
                objDepositsProductSchemeTO.setStaffAccount(NO);
            }
            //            for doubling scheme
            objDepositsProductSchemeTO.setRdoDoublingScheme(CommonUtil.convertObjToStr(getRdoDoublingScheme()));
            objDepositsProductSchemeTO.setDoubligCount(CommonUtil.convertObjToInt(getTxtDoublingCount()));
            //            changed by nikhil for Call deposit
            objDepositsProductSchemeTO.setRdoWithPeriod(CommonUtil.convertObjToStr(getRdoWithPeriod()));
            
            objDepositsProductSchemeTO.setAftermaturityInttype((String)cbmMaturityInterestType.getKeyForSelected());
            objDepositsProductSchemeTO.setAftermaturityIntrate((String)cbmMaturityInterestRate.getKeyForSelected());
            
            //            multiply = getMultiply(getCboDepositPerUnit());
            objDepositsProductSchemeTO.setDepositPerUnit(CommonUtil.convertObjToDouble(getTxtDepositPerUnit()));
            
            multiply = getMultiply(getCboMinDepositPeriod());
            objDepositsProductSchemeTO.setMinDepositPeriod(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtMinDepositPeriod()) * multiply)));
            //objDepositsProductSchemeTO.setMaxDepositPeriod (new Double (getTxtMaxDepositPeriod()));
            
            multiply = getMultiply(getCboMaxDepositPeriod());
            objDepositsProductSchemeTO.setMaxDepositPeriod(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtMaxDepositPeriod()) * multiply)));
            
            //objDepositsProductSchemeTO.setPeriodMultiples (new Double (getTxtPeriodInMultiplesOf()));
            
            multiply = getMultiply(getCboPeriodInMultiplesOf());
            objDepositsProductSchemeTO.setPeriodMultiples(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtPeriodInMultiplesOf()) * multiply)));
            
            multiply = getMultiply(getCboPrematureWithdrawal());
            objDepositsProductSchemeTO.setPrematureWithdrawal(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtPrematureWithdrawal()) * multiply)));
            
            objDepositsProductSchemeTO.setMinDepositAmt(CommonUtil.convertObjToDouble(getTxtMinDepositAmt()));
            objDepositsProductSchemeTO.setMaxDepositAmt(CommonUtil.convertObjToDouble(getTxtMaxDepositAmt()));
            objDepositsProductSchemeTO.setAmtMultiples(CommonUtil.convertObjToDouble(getTxtAmtInMultiplesOf()));
            
            if (getRdoCalcMaturityValue_Yes() == true){
                objDepositsProductSchemeTO.setCalcMaturityValue(YES);
            }else{
                objDepositsProductSchemeTO.setCalcMaturityValue(NO);
            }
            
            if (getRdoTransferToMaturedDeposits_Yes() == true){
                objDepositsProductSchemeTO.setTransMaturedDep(YES);
            }else{
                objDepositsProductSchemeTO.setTransMaturedDep(NO);
            }
            
            objDepositsProductSchemeTO.setAfterDays(CommonUtil.convertObjToDouble(getTxtAfterHowManyDays()));
            objDepositsProductSchemeTO.setAdvMaturityGenpd(CommonUtil.convertObjToDouble(getTxtAdvanceMaturityNoticeGenPeriod()));
            
            if (getRdoProvisionOfInterest_Yes() == true){
                objDepositsProductSchemeTO.setProvIntmaturedDep(YES);
            }else{
                objDepositsProductSchemeTO.setProvIntmaturedDep(NO);
            }
            objDepositsProductSchemeTO.setIntMaturedDep(CommonUtil.convertObjToDouble(getTxtInterestOnMaturedDeposits()));
            
            if (getRdoPartialWithdrawalAllowed_Yes() == true){
                objDepositsProductSchemeTO.setPartialWithdrawal(YES);
            }else{
                objDepositsProductSchemeTO.setPartialWithdrawal(NO);
            }
            objDepositsProductSchemeTO.setNoPartialWithdrawal(CommonUtil.convertObjToDouble(getTxtNoOfPartialWithdrawalAllowed()));
            objDepositsProductSchemeTO.setMaxAmtWithdrawal(CommonUtil.convertObjToDouble(getTxtMaxAmtOfPartialWithdrawalAllowed()));
            objDepositsProductSchemeTO.setMaxNoWithdrawalYr(CommonUtil.convertObjToDouble(getTxtMaxNoOfPartialWithdrawalAllowed()));
            objDepositsProductSchemeTO.setMinAmtWithdrawal(CommonUtil.convertObjToDouble(getTxtMinAmtOfPartialWithdrawalAllowed()));
            objDepositsProductSchemeTO.setWithdrawalMulti(CommonUtil.convertObjToDouble(getTxtWithdrawalsInMultiplesOf()));
            objDepositsProductSchemeTO.setServiceChargePer(CommonUtil.convertObjToDouble(getTxtServiceCharge()));
            
            if (getRdoWithdrawalWithInterest_Yes() == true){
                objDepositsProductSchemeTO.setWithdrawalInterest(YES);
            }else{
                objDepositsProductSchemeTO.setWithdrawalInterest(NO);
            }
            
            if (getRdoServiceCharge_Yes() == true){
                objDepositsProductSchemeTO.setServiceCharge(YES);
            }else{
                objDepositsProductSchemeTO.setServiceCharge(NO);
            }
            
            if (getRdoExtnOfDepositBeforeMaturity_Yes() == true){
                objDepositsProductSchemeTO.setExtDepMaturity(YES);
            }else{
                objDepositsProductSchemeTO.setExtDepMaturity(NO);
            }
            
            
            if (getRdoAutoAdjustment_Yes() == true){
                objDepositsProductSchemeTO.setAdjMaturityLoan(YES);
            }else{
                objDepositsProductSchemeTO.setAdjMaturityLoan(NO);
            }
            
            if (getRdoAdjustIntOnDeposits_Yes() == true){
                objDepositsProductSchemeTO.setAdjIntLien(YES);
            }else{
                objDepositsProductSchemeTO.setAdjIntLien(NO);
            }
            
            if (getRdoAdjustPrincipleToLoan_Yes() == true){
                objDepositsProductSchemeTO.setAdjPrinLoanLien(YES);
            }else{
                objDepositsProductSchemeTO.setAdjPrinLoanLien(NO);
            }
            
            //            objDepositsProductSchemeTO.setPrematureWithdrawal(CommonUtil.convertObjToDouble(getTxtPrematureWithdrawal()));
            
            if (getRdoFlexiFromSBCA_Yes() == true){
                objDepositsProductSchemeTO.setFlexiSbCa(YES);
            }else{
                objDepositsProductSchemeTO.setFlexiSbCa(NO);
            }
            
            if (getRdoTermDeposit_Yes() == true){
                objDepositsProductSchemeTO.setTdSecurityOd(YES);
            }else{
                objDepositsProductSchemeTO.setTdSecurityOd(NO);
            }
            
            objDepositsProductSchemeTO.setAcnumPattern(getTxtAcctNumberPattern());
            objDepositsProductSchemeTO.setAlphaSuffixTdrec(getTxtAlphaSuffix());
            objDepositsProductSchemeTO.setMaxAmtCash(CommonUtil.convertObjToDouble(getTxtMaxAmtOfCashPayment()));
            objDepositsProductSchemeTO.setMinAmtPan(CommonUtil.convertObjToDouble(getTxtMinAmtOfPAN()));
            
            if (getRdoIntroducerReqd_Yes() == true){
                objDepositsProductSchemeTO.setIntroRequired(YES);
            }else{
                objDepositsProductSchemeTO.setIntroRequired(NO);
            }
            
            Date SchDt = DateUtil.getDateMMDDYYYY(getTdtSchemeIntroDate());
            if(SchDt != null){
                Date schDate = (Date) curDate.clone();
                schDate.setDate(SchDt.getDate());
                schDate.setMonth(SchDt.getMonth());
                schDate.setYear(SchDt.getYear());
                //            objDepositsProductSchemeTO.setSchemeIntroDt(DateUtil.getDateMMDDYYYY(getTdtSchemeIntroDate()));
                objDepositsProductSchemeTO.setSchemeIntroDt(schDate);
            }else{
                objDepositsProductSchemeTO.setSchemeIntroDt(DateUtil.getDateMMDDYYYY(getTdtSchemeIntroDate()));
            }
            //            objDepositsProductSchemeTO.setScheme(getTxtScheme());
            
            if(getRdoCertificate_printing().length()>0 && !getRdoCertificate_printing().equals("")){
                objDepositsProductSchemeTO.setCertificatePrint(CommonUtil.convertObjToStr(getRdoCertificate_printing()));
            }else{
                objDepositsProductSchemeTO.setCertificatePrint("");
            }
            
            Date SchClosingDt = DateUtil.getDateMMDDYYYY(getTdtSchemeClosingDate());
            if(SchClosingDt != null){
                Date schDate = (Date) curDate.clone();
                schDate.setDate(SchClosingDt.getDate());
                schDate.setMonth(SchClosingDt.getMonth());
                schDate.setYear(SchClosingDt.getYear());
                //            objDepositsProductSchemeTO.setSchemeIntroDt(DateUtil.getDateMMDDYYYY(getTdtSchemeIntroDate()));
                objDepositsProductSchemeTO.setSchemeClosingDt(schDate);
            }else{
                objDepositsProductSchemeTO.setSchemeClosingDt(DateUtil.getDateMMDDYYYY(getTdtSchemeClosingDate()));
            }
            
            //            if (getRdoSystemCalcValues_Yes() == true){
            //                objDepositsProductSchemeTO.setSysCalcChg(YES);
            //            }else{
            //                objDepositsProductSchemeTO.setSysCalcChg(NO);
            //            }
            //
            
            if(isChkFDRenewalSameNoTranPrincAmt()==true){
                objDepositsProductSchemeTO.setFdRenewalSameNoTranPrincAmt("Y");
            }else{
                objDepositsProductSchemeTO.setFdRenewalSameNoTranPrincAmt("N");
            }
            objDepositsProductSchemeTO.setLimitBulkDeposit(CommonUtil.convertObjToDouble(getTxtLimitForBulkDeposit()));
            objDepositsProductSchemeTO.setAfterNoDays(CommonUtil.convertObjToDouble(getTxtAfterNoDays()));
            if(getChkAgentCommSlabRequired().equalsIgnoreCase("Y")){
              objDepositsProductSchemeTO.setAgentcommSlabRequired("Y");
            }else{
              objDepositsProductSchemeTO.setAgentcommSlabRequired("N");  
            }
             objDepositsProductSchemeTO.setAgentCommCalcMethod(getCboAgentCommCalcMethod());           
            
        }catch(Exception e){
            log.info("error in setSchemeData");
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsProductSchemeTO;
    }
    
    /* To set common data in the Transfer Object -- DB Table: DEPOSITS_PROD_INTPAY*/
    public DepositsProductIntPayTO setIntPayData() {
        log.info("setIntPayData");
        final DepositsProductIntPayTO objDepositsProductIntPayTO = new DepositsProductIntPayTO();
        try{
            objDepositsProductIntPayTO.setProdId(getTxtProductID());
            objDepositsProductIntPayTO.setIntMaintainPart((String)cbmIntMaintainedAsPartOf.getKeyForSelected());
            
            objDepositsProductIntPayTO.setIntCalcMethod(CommonUtil.convertObjToDouble(cbmIntCalcMethod.getKeyForSelected()));
            objDepositsProductIntPayTO.setIntRateEditable(getChkIntEditable());
            
            if (getRdoIntProvisioningApplicable_Yes() == true){
                objDepositsProductIntPayTO.setIntProvAppl(YES);
            }else{
                objDepositsProductIntPayTO.setIntProvAppl(NO);
            }
            
            objDepositsProductIntPayTO.setIntProvFreq(CommonUtil.convertObjToDouble(cbmIntProvisioningFreq.getKeyForSelected()));
            objDepositsProductIntPayTO.setProvLevel((String)cbmProvisioningLevel.getKeyForSelected());
            objDepositsProductIntPayTO.setNoDaysYear(CommonUtil.convertObjToDouble(cbmNoOfDays.getKeyForSelected()));
            objDepositsProductIntPayTO.setIntCompFreq(CommonUtil.convertObjToDouble(cbmIntCompoundingFreq.getKeyForSelected()));
            objDepositsProductIntPayTO.setIntType(CommonUtil.convertObjToStr(cbmInterestType.getKeyForSelected()));
            objDepositsProductIntPayTO.setIntApplFreq(CommonUtil.convertObjToDouble(cbmIntApplnFreq.getKeyForSelected()));
            
            Date LsPrDt = DateUtil.getDateMMDDYYYY(tdtLastIntProvisionalDate);
            if(LsPrDt != null){
                Date lsprDate = (Date) curDate.clone();
                lsprDate.setDate(LsPrDt.getDate());
                lsprDate.setMonth(LsPrDt.getMonth());
                lsprDate.setYear(LsPrDt.getYear());
                //            objDepositsProductIntPayTO.setLastIntProvdt(DateUtil.getDateMMDDYYYY(tdtLastIntProvisionalDate));
                objDepositsProductIntPayTO.setLastIntProvdt(lsprDate);
            }else{
                objDepositsProductIntPayTO.setLastIntProvdt(DateUtil.getDateMMDDYYYY(tdtLastIntProvisionalDate));
            }
            objDepositsProductIntPayTO.setPrematClosSIRat(getChkPrematureClosure());
           
            Date NxPrDt = DateUtil.getDateMMDDYYYY(tdtNextIntProvisionalDate);
            if(NxPrDt != null){
                Date nxprDate = (Date) curDate.clone();
                nxprDate.setDate(NxPrDt.getDate());
                nxprDate.setMonth(NxPrDt.getMonth());
                nxprDate.setYear(NxPrDt.getYear());
                //            objDepositsProductIntPayTO.setNextIntProvdt(DateUtil.getDateMMDDYYYY(tdtNextIntProvisionalDate));
                objDepositsProductIntPayTO.setNextIntProvdt(nxprDate);
            }else{
                objDepositsProductIntPayTO.setNextIntProvdt(DateUtil.getDateMMDDYYYY(tdtNextIntProvisionalDate));
            }
            
            Date LsIntDt = DateUtil.getDateMMDDYYYY(tdtLastInterestAppliedDate);
            if(LsIntDt != null){
                Date lsintDate = (Date) curDate.clone();
                lsintDate.setDate(LsIntDt.getDate());
                lsintDate.setMonth(LsIntDt.getMonth());
                lsintDate.setYear(LsIntDt.getYear());
                //            objDepositsProductIntPayTO.setLastIntAppldt(DateUtil.getDateMMDDYYYY(tdtLastInterestAppliedDate));
                objDepositsProductIntPayTO.setLastIntAppldt(lsintDate);
            }else{
                objDepositsProductIntPayTO.setLastIntAppldt(DateUtil.getDateMMDDYYYY(tdtLastInterestAppliedDate));
            }
            
            Date NxIntDt = DateUtil.getDateMMDDYYYY(tdtNextInterestAppliedDate);
            if(NxIntDt != null){
                Date nxintDate = (Date) curDate.clone();
                nxintDate.setDate(NxIntDt.getDate());
                nxintDate.setMonth(NxIntDt.getMonth());
                nxintDate.setYear(NxIntDt.getYear());
                //            objDepositsProductIntPayTO.setNextIntAppldt(DateUtil.getDateMMDDYYYY(tdtNextInterestAppliedDate));
                objDepositsProductIntPayTO.setNextIntAppldt(nxintDate);
            }else{
                objDepositsProductIntPayTO.setNextIntAppldt(DateUtil.getDateMMDDYYYY(tdtNextInterestAppliedDate));
            }
            if(chkDifferentROI){
                objDepositsProductIntPayTO.setChkROI("Y");
            }else{
                objDepositsProductIntPayTO.setChkROI("N");
            }
            if(chkSlabWiseInterest){
                objDepositsProductIntPayTO.setSlabWiseInterest("Y");
            }else{
                objDepositsProductIntPayTO.setSlabWiseInterest("N");
            }
            if(chkSlabWiseCommision){
                objDepositsProductIntPayTO.setSlabWiseCommision("Y");
            }else{
                objDepositsProductIntPayTO.setSlabWiseCommision("N");
            }
            objDepositsProductIntPayTO.setIntRoundoffTerms((String)cbmIntRoundOff.getKeyForSelected());
            objDepositsProductIntPayTO.setMinIntPaid(CommonUtil.convertObjToDouble(getTxtMinIntToBePaid()));
            if(isRdoDepositRate()){
                objDepositsProductIntPayTO.setPreMatureClosureApply("Deposit Rate");
                objDepositsProductIntPayTO.setFixedDepositProduct((String)cbmDepositsProdFixd.getKeyForSelected());
            }
            if(isRdoSBRate()){
                objDepositsProductIntPayTO.setPreMatureClosureApply("SB Rate");
                objDepositsProductIntPayTO.setFixedDepositProduct((String)cbmSbProduct.getKeyForSelected());
            }
           
           objDepositsProductIntPayTO.setCategoryBenifitRate(getTxtCategoryBenifitRate());
           objDepositsProductIntPayTO.setCbxInterstRoundTime(getCbxInterstRoundTime());
           objDepositsProductIntPayTO.setAppNormRate(getChkAppNormRate());
           objDepositsProductIntPayTO.setNormPeriod(CommonUtil.convertObjToInt(getTxtNormPeriod()));
           objDepositsProductIntPayTO.setPreMatIntType(getCboPreMatIntType());
            System.out.println("objDepositsProductIntPayTO.setCbxInterstRoundTime"+objDepositsProductIntPayTO.getCbxInterstRoundTime());
        }catch(Exception e){
            log.info("error in setIntPayData");
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsProductIntPayTO;
    }
    
    public DepositsProductAcHdTO setAcHdData() {
        log.info("setAcHdData");
        final DepositsProductAcHdTO objDepositsProductAcHdTO = new DepositsProductAcHdTO();
        try{
            objDepositsProductAcHdTO.setProdId(getTxtProductID());
            objDepositsProductAcHdTO.setIntProvAchd(getTxtIntProvisioningAcctHd());
            objDepositsProductAcHdTO.setIntPay(getTxtIntPaybleGLHead());
            objDepositsProductAcHdTO.setIntDebit(getTxtIntDebitPLHead());
            objDepositsProductAcHdTO.setMaturityDeposit(getTxttMaturityDepositAcctHead());
            objDepositsProductAcHdTO.setIntMaturedDeposit(getTxtIntOnMaturedDepositAcctHead());
            objDepositsProductAcHdTO.setIntProvMatured(getTxtIntProvisionOfMaturedDeposit());
            objDepositsProductAcHdTO.setAchdFloatAc(getTxtAcctHeadForFloatAcct());
            objDepositsProductAcHdTO.setFixedDepositAchd(getTxtFixedDepositAcctHead());
            objDepositsProductAcHdTO.setInterestRecoveryAcHd(getTxtInterestRecoveryAcHd());
            objDepositsProductAcHdTO.setCommisionAchd(getTxtCommisionPaybleGLHead());
            objDepositsProductAcHdTO.setDelayedAchd(getTxtPenalChargesAchd());
            objDepositsProductAcHdTO.setTrasferOutAchd(getTxtTransferOutAcHd());
            objDepositsProductAcHdTO.setPostageAcHd(getPostageAcHd());
            objDepositsProductAcHdTO.setBenovelentIntReserveHd(getTxtBenIntReserveHead());// Added by nithya for adding field for interest reserve head for benevolent deposits
            
        }catch(Exception e){
            log.info("error in setAcHdData");
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsProductAcHdTO;
    }
    
    public DepositsProductTaxTO setTaxData() {
        log.info("setTaxData");
        final DepositsProductTaxTO objDepositsProductTaxTO = new DepositsProductTaxTO();
        try{
            objDepositsProductTaxTO.setProdId(getTxtProductID());
            //setTxtTDSGLAcctHd
            objDepositsProductTaxTO.setTdsGlAchd(getTxtTDSGLAcctHd());
            
            if (getRdoRecalcOfMaturityValue_Yes() == true){
                objDepositsProductTaxTO.setRecalcMaturityValtds(YES);
            }else{
                objDepositsProductTaxTO.setRecalcMaturityValtds(NO);
            }
            
        }catch(Exception e){
            log.info("error in setTaxData");
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsProductTaxTO;
    }
    
    // final DepositsProductRenewalTO objDepositsProductRenewalTO = setRenewalData();
    public DepositsProductRenewalTO setRenewalData() {
        log.info("setRenewalData");
        int multiply = 0;
        final DepositsProductRenewalTO objDepositsProductRenewalTO = new DepositsProductRenewalTO();
        try{
            
            objDepositsProductRenewalTO.setProdId(getTxtProductID());
            //txtIntPeriodForBackDatedRenewal
            
            multiply = getMultiply(getCboIntPeriodForBackDatedRenewal());
            objDepositsProductRenewalTO.setMaxPdbkdtRenewal(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtIntPeriodForBackDatedRenewal()) * multiply)));
            objDepositsProductRenewalTO.setMaxPdInttype((String)cbmIntType.getKeyForSelected());
            
            multiply = getMultiply(getCboMaxPeriodMDt());
            objDepositsProductRenewalTO.setMaxPdMaturitydt(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtMaxPeriodMDt()) * multiply)));
            
            objDepositsProductRenewalTO.setIntCriteria((String)cbmIntCriteria.getKeyForSelected());
            if (getRdoRenewalOfDepositAllowed_Yes() == true){
                objDepositsProductRenewalTO.setRenewalDepositAllowed(YES);
            }else{
                objDepositsProductRenewalTO.setRenewalDepositAllowed(NO);
            }
            
            multiply = getMultiply(getCboMinNoOfDays());
            objDepositsProductRenewalTO.setMinDaysBkdtDeposits(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtMinNoOfDays()) * multiply)));
            
            if (getRdoAutoRenewalAllowed_Yes() == true){
                objDepositsProductRenewalTO.setAutoRenewalAllowed(YES);
            }else{
                objDepositsProductRenewalTO.setAutoRenewalAllowed(NO);
            }
            if(getTxtMinimumRenewalDeposit().length()>0){
                objDepositsProductRenewalTO. setPeriodOfRenewal(new Double(getTxtMinimumRenewalDeposit()));
                objDepositsProductRenewalTO.setPeriodOfFormat(CommonUtil.convertObjToStr(cbmMinimumRenewalDeposit.getKeyForSelected()));
            }else{
                objDepositsProductRenewalTO. setPeriodOfRenewal(new Double(0));
                objDepositsProductRenewalTO.setPeriodOfFormat("DAYS");
            }
            objDepositsProductRenewalTO.setMaxNoAutoRenewal(CommonUtil.convertObjToDouble(getTxtMaxNopfTimes()));
            if (getRdoSameNoRenewalAllowed_Yes()== true){
                objDepositsProductRenewalTO.setSameNoRenewalAllowed(YES);
            }else{
                objDepositsProductRenewalTO.setSameNoRenewalAllowed(NO);
            }
            if(getRdoInterestalreadyPaid_Yes() == true){
                objDepositsProductRenewalTO.setRenewedDepIntRecovered("Y");
            }else if(getRdoInterestalreadyPaid_No() == true){
                objDepositsProductRenewalTO.setRenewedDepIntRecovered("N");
            }
           if(getRdoExtensionBeyondOriginalDate_Yes() == true){
                objDepositsProductRenewalTO.setBeyondOriginal(YES);
            }else{
                objDepositsProductRenewalTO.setBeyondOriginal(NO);
            }
            
             if(getRdoExtensionPenal_Yes() == true){
                objDepositsProductRenewalTO.setExtensionPenal(YES);
            }else{
                objDepositsProductRenewalTO.setExtensionPenal(NO);
            }
            if(getRdoInterestrateappliedoverdue_Yes() == true){
                objDepositsProductRenewalTO.setIntRateAppliedOverdue("Y");
                objDepositsProductRenewalTO.setDateOfRenewal("N");
                objDepositsProductRenewalTO.setDateOfMaturity("N");
                //objDepositsProductRenewalTO.setEligibleTwoRate("N");
                objDepositsProductRenewalTO.setEligibleTwoRate("");
                objDepositsProductRenewalTO.setOneRateAvail("N");
                objDepositsProductRenewalTO.setBothRateNotAvail("N");
            }else if(getRdoInterestrateappliedoverdue_No() == true){
                objDepositsProductRenewalTO.setIntRateAppliedOverdue("N");
            }
            if(getChkRateAsOnDateOfRenewal() == true){
                objDepositsProductRenewalTO.setDateOfRenewal("Y");
            }else{
                objDepositsProductRenewalTO.setDateOfRenewal("N");
            }
            if(getChkRateAsOnDateOfMaturity() == true){
                objDepositsProductRenewalTO.setDateOfMaturity("Y");
            }else{
                objDepositsProductRenewalTO.setDateOfMaturity("N");
            }
            if(getChkRateAsOnDateOfRenewal()!= true && getChkRateAsOnDateOfMaturity() != true )
            {
                  objDepositsProductRenewalTO.setEligibleTwoRate(CommonUtil.convertObjToStr(cbmEitherofTwoRatesChoose.getKeyForSelected()));
            }
            if(getRdoSBRateOneRate_Yes() == true){
                objDepositsProductRenewalTO.setOneRateAvail("Y");
            }else if(getRdoSBRateOneRate_No() == true){
                objDepositsProductRenewalTO.setOneRateAvail("N");
            }
            if(getRdoBothRateNotAvail_Yes() == true){
                objDepositsProductRenewalTO.setBothRateNotAvail("Y");
            }else if(getRdoBothRateNotAvail_No() == true){
                objDepositsProductRenewalTO.setBothRateNotAvail("N");
            }
            objDepositsProductRenewalTO.setMaxNoSameNoRenewal(CommonUtil.convertObjToDouble(getTxtMaxNoSameNo()));
            if(!getCboOperatesLike().equals("") && getCboOperatesLike().equals("Daily")){
                if(getRdoDaily() == true){
                    objDepositsProductRenewalTO.setDailyIntCalc("DAILY_CALC");
                    objDepositsProductRenewalTO.setWeeklyBasis(new Double(0));
                    objDepositsProductRenewalTO.setCbmMonthlyIntCalcMethod("");
                    objDepositsProductRenewalTO.setRdoPartialWithdrawlForDD("");
                }else if(getRdoWeekly() == true){
                    objDepositsProductRenewalTO.setDailyIntCalc("WEEKLY");
                    objDepositsProductRenewalTO.setWeeklyBasis(CommonUtil.convertObjToDouble(cbmWeekly.getKeyForSelected()));
                    objDepositsProductRenewalTO.setCbmMonthlyIntCalcMethod("");
                    objDepositsProductRenewalTO.setRdoPartialWithdrawlForDD("");
                }else if(getRdoMonthly() == true){
                    objDepositsProductRenewalTO.setDailyIntCalc("MONTHLY");
                    objDepositsProductRenewalTO.setWeeklyBasis(new Double(0));
                    objDepositsProductRenewalTO.setCbmMonthlyIntCalcMethod(CommonUtil.convertObjToStr(cbmMonthlyIntCalcMethod.getKeyForSelected()));
                    
                }
                System.out.println("selected :: " + cbmDepositsFrequency.getKeyForSelected());
                if(CommonUtil.convertObjToStr(cbmDepositsFrequency.getKeyForSelected()).equals("999")){ // Added by nithya on 23-01-2017 for 0005664
                    objDepositsProductRenewalTO.setDailyIntCalc("INSTALLMENTS");
                    objDepositsProductRenewalTO.setWeeklyBasis(new Double(0));
                    objDepositsProductRenewalTO.setCbmMonthlyIntCalcMethod("");
                    objDepositsProductRenewalTO.setRdoPartialWithdrawlForDD("");
                }
                if(getRdoPartialWithdrawlAllowedForDD().equals("Y")){
                    objDepositsProductRenewalTO.setRdoPartialWithdrawlForDD("Y");
                }else if(getRdoPartialWithdrawlAllowedForDD().equals("N")){
                    objDepositsProductRenewalTO.setRdoPartialWithdrawlForDD("N");
                }
            }else{
                objDepositsProductRenewalTO.setCbmMonthlyIntCalcMethod("");
                objDepositsProductRenewalTO.setDailyIntCalc("");
                objDepositsProductRenewalTO.setWeeklyBasis(new Double(0));
                objDepositsProductRenewalTO.setRdoPartialWithdrawlForDD("");
            }
            if(getRdoClosedwithinperiod_Yes() == true){
                objDepositsProductRenewalTO.setRenewedDepIntPay("Y");
            }else if(getRdoClosedwithinperiod_No() == true){
                objDepositsProductRenewalTO. setRenewedDepIntPay("N");
            }
            if(getTxtRenewedclosedbefore().length()>0){
                objDepositsProductRenewalTO.setRenewdDepClosedBefore(new Double(getTxtRenewedclosedbefore()));
                objDepositsProductRenewalTO.setRenewedDepositFormat(CommonUtil.convertObjToStr(cbmRenewedclosedbefore.getKeyForSelected()));
            }else{
                objDepositsProductRenewalTO.setRenewdDepClosedBefore(new Double(0));
                objDepositsProductRenewalTO.setRenewedDepositFormat("DAYS");
            }
            if(getChkIntRateApp() == true){
                objDepositsProductRenewalTO.setClosureIntYN("Y");
            }else{
                objDepositsProductRenewalTO.setClosureIntYN("N");
            }     
            if (isChkIntRateDeathMarked() == true) {
                objDepositsProductRenewalTO.setDeathMarkedYN("Y");
            } else {
                objDepositsProductRenewalTO.setDeathMarkedYN("N");
            }
            objDepositsProductRenewalTO.setSbRateProdId(CommonUtil.convertObjToStr(cbmSbrateModel.getKeyForSelected()));// added by chithra 17-05-14
       
        }catch(Exception e){
            log.info("error in setRenewalData");
            //e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsProductRenewalTO;
    }
    //Agents Commision process
    private int setCombo(String duration) throws Exception{
        periodData=0;
        resultData=0;
        int period=0;
        if(!duration.equalsIgnoreCase("")){
            if(duration.equals(YEAR))
                period = YEAR_INT;
            else if(duration.equals(MONTH))
                period = MONTH_INT;
            else if( duration.equals(DAY))
                period = DAY_INT;
        }
        duration = "";
        return period;
    }
    
    //Agents Commision process
    public int period() {
        int yes = 0;
        try{
            String fromRange = (String)cbmFromPeriod.getKeyForSelected();
            double fromDays = setCombo(fromRange);
            double fromData = fromDays * (Double.parseDouble(txtFromPeriod));
            
            String toRange = (String)cbmToPeriod.getKeyForSelected();
            double toDays = setCombo(toRange);
            double toData = toDays * (Double.parseDouble(txtToPeriod));
            
            if(fromData > toData){
                yes = 1;
            }
        }catch(Exception e){
            log.info("Error in period()");
        }
        return yes;
    }
    
    //Agents Commision process
    private InterestMaintenanceRateTO setInterestMaintenanceRate() {
        log.info("In setInterestMaintenanceRate()");
        
        final InterestMaintenanceRateTO objInterestMaintenanceRateTO = new InterestMaintenanceRateTO();
        try{
            //            objInterestMaintenanceRateTO.setRoiGroupId(lblRoiGroupId);
            //            objInterestMaintenanceRateTO.setRateTypeId(getInterRateType());
            String behavesLike = (String)cbmOperatesLike.getKeyForSelected();
            if(behavesLike.equals("RECURRING")){
                objInterestMaintenanceRateTO.setRoiGroupId(getTxtProductID());
                objInterestMaintenanceRateTO.setFromAmount(CommonUtil.convertObjToDouble((String)getTxtFromAmt()));
                objInterestMaintenanceRateTO.setToAmount(CommonUtil.convertObjToDouble((String)getTxtToAmt()));
                objInterestMaintenanceRateTO.setStatus(getLblStatus());
            }else{
                tdtDate=getTdtDate();
                Date RoiDt = DateUtil.getDateMMDDYYYY(tdtDate);
                if(RoiDt != null){
                    Date roiDate = (Date) curDate.clone();
                    roiDate.setDate(RoiDt.getDate());
                    roiDate.setMonth(RoiDt.getMonth());
                    roiDate.setYear(RoiDt.getYear());
                    //                objInterestMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(tdtDate));
                    objInterestMaintenanceRateTO.setRoiDate(roiDate);
                }else{
                    objInterestMaintenanceRateTO.setRoiDate(DateUtil.getDateMMDDYYYY(tdtDate));
                }
                
                System.out.println("tdtDate : "+tdtDate);
                System.out.println("tdtDate : "+DateUtil.getDate(1,2,3));
                
                tdtToDate=getTdtToDate();
                Date RoiFrDt = DateUtil.getDateMMDDYYYY(tdtToDate);
                if(RoiFrDt != null){
                    Date roiFrDate = (Date) curDate.clone();
                    roiFrDate.setDate(RoiFrDt.getDate());
                    roiFrDate.setMonth(RoiFrDt.getMonth());
                    roiFrDate.setYear(RoiFrDt.getYear());
                    //                objInterestMaintenanceRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(tdtToDate));
                    objInterestMaintenanceRateTO.setRoiEndDate(roiFrDate);
                }else{
                    objInterestMaintenanceRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(tdtToDate));
                }
                
                System.out.println("tdtToDate : "+tdtToDate);
                objInterestMaintenanceRateTO.setFromAmount(CommonUtil.convertObjToDouble((String)getTxtFromAmt()));
                objInterestMaintenanceRateTO.setToAmount(CommonUtil.convertObjToDouble((String)getTxtToAmt()));
//                objInterestMaintenanceRateTO.setFromAmount(CommonUtil.convertObjToDouble((String)cbmFromAmount.getKeyForSelected()));
//                objInterestMaintenanceRateTO.setToAmount(CommonUtil.convertObjToDouble((String)cbmToAmount.getKeyForSelected()));
                objInterestMaintenanceRateTO.setCommand(getCommand());
                objInterestMaintenanceRateTO.setRoiGroupId(getTxtProductID());
//                                if(newRecEntering == true)
//                                    objInterestMaintenanceRateTO.setStatus(getLblStatus());
//                                else
                objInterestMaintenanceRateTO.setStatus(CommonConstants.STATUS_MODIFIED);
                
                //                objInterestMaintenanceRateTO.setStatus(getLblStatus());
                /** To Convert the Combination of From-Period into Days...
                 */
//                 duration=getCboFromPeriod();
//                duration=cboFromPeriod;
                duration = ((String)cbmFromPeriod.getKeyForSelected());
                periodData = setCombo(duration);
                resultData = periodData * (CommonUtil.convertObjToLong(txtFromPeriod));
                
                
                objInterestMaintenanceRateTO.setFromPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
                /** To Convert the Combination of To-Period into Days...
                 */
                
                duration = ((String)cbmToPeriod.getKeyForSelected());
                periodData = setCombo(duration);
                resultData = periodData * (CommonUtil.convertObjToLong(txtToPeriod));
                
                
                objInterestMaintenanceRateTO.setToPeriod(CommonUtil.convertObjToDouble(String.valueOf(resultData)));
               
            }
            if(getCboInstType().length()>0)
                objInterestMaintenanceRateTO.setInstType(CommonUtil.convertObjToStr(getCboInstType()));
            if(getTxtRateInterest().length()>0)
                objInterestMaintenanceRateTO.setRoi(CommonUtil.convertObjToDouble(getTxtRateInterest()));
        }catch(Exception e){
            parseException.logException(e,true);
            e.printStackTrace();
        }
        return objInterestMaintenanceRateTO;
    }
    
    //Agents Commision process
    public void setInterestTabTitle() {
        try{
            interestTabTitle = new ArrayList();
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter1"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter2"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter3"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter4"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter5"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter6"));
            interestTabTitle.add(objInterestMaintenanceRB.getString("tblInter7"));
            
            tblInterestTable = new EnhancedTableModel(null, interestTabTitle);
            
            setIsTableSet(true);
        }catch(Exception e){
            System.out.println("Error in setChargeTabTitleOA()");
            e.printStackTrace();
        }
        
    }
    
    public void setTblDelayedInstallmet() {
        try{
            delayinterestTabTitle = new ArrayList();
            delayinterestTabTitle.add(objDepositProductRB.getString("tblInterest1"));
            delayinterestTabTitle.add(objDepositProductRB.getString("tblInterest2"));
            delayinterestTabTitle.add(objDepositProductRB.getString("tblInterest3"));
            tblDelayedInstallmet = new EnhancedTableModel(null, delayinterestTabTitle);
            setIsTableSet(true);
        }catch(Exception e){
            System.out.println("Error in setChargeTabTitleOA()");
            e.printStackTrace();
        }
    }
    //Agents Commision process
    public int addTabData(int updateTab, int row){
        int option = -1;
        try {
            final int dataSize = interestList.size();
            
            SerialNo = dataSize;
            boolean addRow = false;
            
            boolean dateRange = true;
            boolean amountRange = true;
            boolean periodRange = true;
            
            ArrayList interestData = new ArrayList();
            
            InterestMaintenanceRateTO objInterestMaintenanceRateTO = setInterestMaintenanceRate();
            InterestMaintenanceRateTO objIntRateTO;
            interestTabRow = new ArrayList();
            
            String behavesLike = (String)cbmOperatesLike.getKeyForSelected();
            System.out.println("########behavesLike:"+behavesLike);
            String from = null;
            String to = null;
            if(behavesLike.equals("RECURRING")){
                //                interestTabRow.remove(tdtDate);
                //                interestTabRow.remove(tdtToDate);
                
                interestTabRow.add(tdtDate);
                interestTabRow.add(tdtToDate);
                interestTabRow.add((String)getTxtFromAmt());
                interestTabRow.add((String)getTxtToAmt());
                //                interestTabRow.remove(from);
                //                interestTabRow.remove(to);
                //                from  = 0.0 + " " + getCboFromPeriod();
                //                to  = 0.0 + " " + getCboToPeriod();
                from  = CommonUtil.convertObjToInt(txtFromPeriod) + " " + getCboFromPeriod();
                to  = CommonUtil.convertObjToInt(txtToPeriod) + " " + getCboToPeriod();
                interestTabRow.add(from);
                interestTabRow.add(to);
            }else{
                interestTabRow.add(tdtDate);
                interestTabRow.add(tdtToDate);
                
                //                interestTabRow.add((String)cbmFromAmount.getKeyForSelected());
                //                interestTabRow.add((String)cbmToAmount.getKeyForSelected());
                interestTabRow.add((String)getTxtFromAmt());
                interestTabRow.add((String)getTxtToAmt());
                /** To Store the From and To period Values...
                 */
                from  = CommonUtil.convertObjToInt(txtFromPeriod) + " " + getCboFromPeriod();
                to  = CommonUtil.convertObjToInt(txtToPeriod) + " " + getCboToPeriod();
                interestTabRow.add(from);
                interestTabRow.add(to);
            }
            interestTabRow.add(txtRateInterest);
            int rowSelected = -1;
            
            // Replacing ... null end dates
            
            
            System.out.println("Changes Implemented");
            
            //            if(!prodType.equalsIgnoreCase("OA")){
            //            interestTabRow.add((String)cbmFromAmount.getKeyForSelected());
            //            interestTabRow.add((String)cbmToAmount.getKeyForSelected());
            //            interestTabRow.add(txtRateInterest);
            //            int rowSelected = -1;
            if(dataSize > 0){
                for (int i=0;i<dataSize;i++){
                    objIntRateTO = (InterestMaintenanceRateTO)interestList.get(i);
                    //ArrayList range = new ArrayList();
                    //range = (ArrayList)data.get(i);
                    //__ Check for the Duplication of Date
                    if(!behavesLike.equals("RECURRING")){
                        if (!( DateUtil.getDateMMDDYYYY(tdtDate).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0))))
                        && DateUtil.getDateMMDDYYYY(tdtToDate).before(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,0)))))) {
                            if (!CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)).equals("")) {
                                if (!(DateUtil.getDateMMDDYYYY(tdtDate).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)))) &&
                                DateUtil.getDateMMDDYYYY(tdtToDate).after(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1))))))
                                    dateRange = false;
                            }
                        }
                    }
                    // setFromAmount, setToAmount, setFromPeriod, setToPeriod
                    if(((CommonUtil.convertObjToDouble(cbmFromAmount.getKeyForSelected()).doubleValue() < CommonUtil.convertObjToDouble(objIntRateTO.getFromAmount()).doubleValue())
                    && (CommonUtil.convertObjToDouble(cbmToAmount.getKeyForSelected()).doubleValue() < CommonUtil.convertObjToDouble(objIntRateTO.getFromAmount()).doubleValue()))
                    ||((CommonUtil.convertObjToDouble(cbmFromAmount.getKeyForSelected()).doubleValue() > CommonUtil.convertObjToDouble(objIntRateTO.getToAmount()).doubleValue())
                    && (CommonUtil.convertObjToDouble(cbmToAmount.getKeyForSelected()).doubleValue() > CommonUtil.convertObjToDouble(objIntRateTO.getToAmount()).doubleValue()))){
                        amountRange = true;
                    }else{
                        amountRange = false;
                    }
                    if(!behavesLike.equals("RECURRING")){
                        String fromRange = (String)cbmFromPeriod.getKeyForSelected();
                        int fromDays = setCombo(fromRange);
                        long fromPeriod = fromDays * (CommonUtil.convertObjToLong(txtFromPeriod));
                        
                        String toRange = (String)cbmToPeriod.getKeyForSelected();
                        int toDays = setCombo(toRange);
                        long toPeriod = toDays * (CommonUtil.convertObjToLong(txtToPeriod));
                        
                        long FROM = CommonUtil.convertObjToLong(objIntRateTO.getFromPeriod());
                        long TO = CommonUtil.convertObjToLong(objIntRateTO.getToPeriod());
                        
                        //__ If the FromPeriod is in terms of Months and/or Year; and is equal to the ToPeriod
                        //__ of the Already entered Record, Increase the value of the FromPeriod by 1 day...
                        if((CommonUtil.convertObjToStr(cbmFromPeriod.getKeyForSelected()).equalsIgnoreCase(MONTH_STR)
                        || CommonUtil.convertObjToStr(cbmFromPeriod.getKeyForSelected()).equalsIgnoreCase(YEAR_STR))
                        && (fromPeriod == TO)){
                            
                            fromPeriod ++;
                            //__ Modify the Data in the Object...
                            objInterestMaintenanceRateTO.setFromPeriod(CommonUtil.convertObjToDouble(String.valueOf(fromPeriod)));
                        }
                        
                        //__ Check for the Duplication of Period Range
                        if((fromPeriod < FROM && toPeriod < FROM)
                        ||(fromPeriod > TO && toPeriod > TO)){
                            periodRange = true;
                        } else{
                            periodRange = false;
                        }
                        /** If the Record(s) Already Exists in the Table, Display the Alert...
                         */
                        if(!dateRange && !amountRange && !periodRange && i!= row){
                            System.out.println(dateRange + "&&" + amountRange + "&&" + periodRange+ " && ");
                            rowSelected = i;
                            String[] options = {objInterestMaintenanceRB.getString("cDialogOk"),objInterestMaintenanceRB.getString("cDialogCancel")};
                            option = COptionPane.showOptionDialog(null, objInterestMaintenanceRB.getString("existanceWarning"), CommonConstants.WARNINGTITLE,
                            COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                            //__ If Ok is Pressed...
                            if (option == 0){
                                resetTable();
                                break;
                            }else if( option == 1){ //__ if Cancel is Pressed...
                                break;
                            }
                        }
                    }
                }
            }else{//__ If there is no row in the Table, Add the Row...
                dateRange   = true;
                amountRange  = true;
                periodRange = true;
            }
            //__ Non Duplicated record Can be entered...
            if ((dateRange || amountRange || periodRange) && updateTab!=1){
                tblInterestTable.addRow(interestTabRow);
                interestList.add(objInterestMaintenanceRateTO);
            }
            /** if the record is just updated,
             * update the valued of the Record...
             */
            if((updateTab == 1) && (rowSelected == -1) && (row !=-1)){
                interestList.remove(row);
                interestList.add(row, objInterestMaintenanceRateTO);
                tblInterestTable.removeRow(row);
                tblInterestTable.insertRow(row,interestTabRow);
                option = -1;
            }
            
            //__ Code to add the value to the  null ToDates in the Table...
            int yesno = -1;
            if (tblInterestTable.getRowCount() > 0) {
                //                yesno = COptionPane.showConfirmDialog (null, "Make previous schedule end date with currently selected Date.", "End Date", COptionPane.YES_NO_OPTION);
                //                if (yesno == COptionPane.YES_OPTION) {
                //__ If the Table Contains any null value for the toDate, Display Option Pane...
                for (int j=0;j<dataSize;j++){
                    if(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(j,1)).equals("")) {
                        
                        String[] options = {objInterestMaintenanceRB.getString("cDialogYes"),objInterestMaintenanceRB.getString("cDialogNo")};
                        yesno = COptionPane.showOptionDialog(null, objInterestMaintenanceRB.getString("TODATE_WARNING"), CommonConstants.WARNINGTITLE,
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                        
                        break;
                    }
                }
                
                //__ To Replace the Empty To_Date to the New From_Date minus one Day..
                if(yesno == 0){                //__ if yes is selected...
                    for (int i=0;i<dataSize;i++){
                        objIntRateTO = (InterestMaintenanceRateTO)interestList.get(i);
                        String fromDate = CommonUtil.convertObjToStr(objIntRateTO.getRoiDate());
                        
                        if (CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,1)).equals("")
                        && (!fromDate.equalsIgnoreCase(tdtDate))){
                            //__ To update the To_Date value in the Table...
                            String toDate = DateUtil.getStringDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate), - 1));
                            //                            tblInterestTable.setValueAt(
                            //                            DateUtil.getStringDate(
                            //                            DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtDate), - 1)), i, 1);
                            
                            tblInterestTable.setValueAt(toDate, i, 1);
                            Date ToDt = DateUtil.getDateMMDDYYYY(toDate);
                            if(ToDt != null){
                                Date tooDate = (Date) curDate.clone();
                                tooDate.setDate(ToDt.getDate());
                                tooDate.setMonth(ToDt.getMonth());
                                tooDate.setYear(ToDt.getYear());
                                //__ To update the Value of the To_Date in the ToObjects...
                                //                            objIntRateTO.setRoiEndDate(toDate);
                                objIntRateTO.setRoiEndDate(tooDate);
                            }else{
                                objIntRateTO.setRoiEndDate(DateUtil.getDateMMDDYYYY(toDate));
                            }
                        }
                        System.out.println("objIntRateTO[ " + i + " ]: " + interestList.get(i));
                    }
                }
            }
            interestTabRow = null;
        }catch(Exception e){
            log.info("The error in addChargesTab()");
            parseException.logException(e,true);
            //e.printStackTrace();
        }
        
        System.out.println("Changes Implemented 2");
        return option;
        
    }
    
    public boolean sortData(){
        boolean addData = true;
        int option = -1;
        ArrayList keyList;
        ArrayList valueList;
        HashMap comparisonMap = new HashMap();
        int dataSize = interestList.size();
        for(int i=0; i<dataSize; i++){
            InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO)interestList.get(i);
            
            keyList = new ArrayList();
            keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoiDate()));
            keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoiEndDate()));
            keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount()));
            keyList.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount()));
            
            valueList = new ArrayList();
            //__ if the Map Contains the Key, add the Values to the existing values...
            if(comparisonMap.containsKey(keyList)){
                valueList = (ArrayList)comparisonMap.get(keyList);
                valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getFromPeriod()));
                valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getToPeriod()));
                
            }
            //__ if the New Key, add the data in the Comparison Map..
            else{
                valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getFromPeriod()));
                valueList.add(CommonUtil.convertObjToDouble(objInterestMaintenanceRateTO.getToPeriod()));
            }
            
            comparisonMap.put(keyList,valueList);
        }
        System.out.println("comparisonMap: " + comparisonMap);
        
        Object objKey[] = comparisonMap.keySet().toArray();
        int keylen = objKey.length;
        System.out.println("keylen: " + keylen);
        
        //__ for allt he Keys in the ComparisonMap...
        for(int i=0; i< keylen; i++){
            System.out.println("In Key Loop");
            Object objVal[] = ((ArrayList)comparisonMap.get(objKey[i])).toArray();
            
            System.out.println("Before Sort" + objVal);
            java.util.Arrays.sort(objVal);
            for (int z=0; z < objVal.length; z++)
                System.out.println("z:" + z + ":" +  objVal[z]);
            
            int valLen = objVal.length;
            System.out.println("valLen: " + valLen);
            
            //__ For all the Values in the array...
            for(int j=1; j+1 < valLen;){
                System.out.println("In Val Loop");
                System.out.println("To: "+ (CommonUtil.convertObjToInt(objVal[j])+1));
                System.out.println("From: " + CommonUtil.convertObjToInt(objVal[j+1]));
                if( (CommonUtil.convertObjToInt(objVal[j])+1) !=  CommonUtil.convertObjToInt(objVal[j+1])){
                    addData = false;
                    String warning = "The Period Range for " + objKey[i] +
                    " Combination of Date Range and Amount Range is not Synchronised";
                    String[] options = {"Ok"};
                    option = COptionPane.showOptionDialog(null, warning, CommonConstants.WARNINGTITLE,
                    COptionPane.YES_NO_CANCEL_OPTION, COptionPane.WARNING_MESSAGE, null, options, options[0]);
                    break;
                }
                j+=2;
            }
        }
        
        System.out.println("addData: " + addData);
        return addData;
    }
    
    //Agents Commision process
    public boolean populateInterestTab(int row){
        try{
            log.info("populateInterestTab row: "+row);
            //            setInterestMaintenanceRateTO((InterestMaintenanceRateTO)getRateTypeDetails().get(row));
            authStatus = setInterestMaintenanceRateTO((InterestMaintenanceRateTO)interestList.get(row));
        }catch( Exception e ) {
            parseException.logException(e,true);
        }
        return authStatus;
    }
    
    //Agents Commision process
    public void deleteInterestTab(int row){
        try{
            if(getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                interestDeleteTabRow.add(interestList.get(row));
            }
            interestList.remove(row);
            
            tblInterestTable.removeRow(row);
        }catch(Exception e){
            System.out.println("Error in deleteInterestTab()");
            parseException.logException(e,true);
        }
    }
    
    private void resetPeriod(){
        resultValue = 0;
    }
    
    
    //Agents Commision process
    private boolean setInterestMaintenanceRateTO(InterestMaintenanceRateTO objInterestMaintenanceRateTO) throws Exception{
        log.info("In setInterestMaintenanceTypeTO()");
        
        String behavesLike = (String)cbmOperatesLike.getKeyForSelected();
        if(behavesLike.equals("RECURRING")){
            setTxtFromAmt(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount()));
            setTxtToAmt(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount()));
            if(CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y")){
                setCboInstType(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getInstType()));
            }
        }else{
            setTdtDate(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiDate()));
            setTdtToDate(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiEndDate()));
//            setCboFromAmount((String) getCbmFromAmount().getDataForKey(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount())));
//            setCboToAmount((String) getCbmToAmount().getDataForKey(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount())));
             setTxtFromAmt(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount()));
            setTxtToAmt(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount()));
            //            setStatus(CommonConstants.STATUS_MODIFIED);
            resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getFromPeriod());
            String period = setPeriod(resultValue);
            setCboFromPeriod(period);
            setTxtFromPeriod(String.valueOf(resultValue));
            resetPeriod();
            
            resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getToPeriod());
            period = setPeriod(resultValue);
            setCboToPeriod(period);
            setTxtToPeriod(String.valueOf(resultValue));
            resetPeriod();
        }
        setTxtRateInterest(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoi()));
        setCboInstType(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getInstType()));
        if(objInterestMaintenanceRateTO.getAuthorizedStatus() == null)
            authStatus = false;
        else
            authStatus = true;
        return authStatus;
    }
    
    
    //final DepositsProductRDTO objDepositsProductRDTO = setRDData();*/
    public DepositsProductRDTO setRDData() {
        log.info("setRDData");
        System.out.println("getRdoPenalRounded_No()"+getRdoPenalRounded_No()+"getRdoPenalRounded_Yes()))"+getRdoPenalRounded_Yes());
        int multiply = 0;
        final DepositsProductRDTO objDepositsProductRDTO = new DepositsProductRDTO();
        try{
            objDepositsProductRDTO.setProdId(getTxtProductID());
            
            if (getRdoLastInstallmentAllowed_Yes() == true){
                objDepositsProductRDTO.setLateInstallAllowed(YES);
            }else{
                objDepositsProductRDTO.setLateInstallAllowed(NO);
            }
            
            if (getRdoPenaltyOnLateInstallmentsChargeble_Yes() == true){
                objDepositsProductRDTO.setPenaltyLateInstall(YES);
            }else{
                objDepositsProductRDTO.setPenaltyLateInstall(NO);
            }
            
            if (getRdoInsBeyondMaturityDt_Yes() == true){
                objDepositsProductRDTO.setInsBeyondMaturityDt(YES);
            }else{
                objDepositsProductRDTO.setInsBeyondMaturityDt(NO);
            }
            
            //objDepositsProductRDTO.setMaturityDtLastinstall(CommonUtil.convertObjToDouble(getTxtMaturityDateAfterLastInstalPaid()));
            multiply = getMultiply(getCboMaturityDateAfterLastInstalPaid());
            objDepositsProductRDTO.setMaturityDtLastinstall(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtMaturityDateAfterLastInstalPaid()) * multiply)));
            
            multiply = getMultiply(getCboMinimumPeriod());
            objDepositsProductRDTO.setMinimumPeriod(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtMinimumPeriod()) * multiply)));
            
            //            multiply = getMultiply(getCboAgentCommision());
            objDepositsProductRDTO.setIntAppSlab(CommonUtil.convertObjToStr(getChkIntApplicationSlab()));
            objDepositsProductRDTO.setChkWeeklySpec(CommonUtil.convertObjToStr(getChkWeeklySpecial()));
            objDepositsProductRDTO.setChkRdNature(CommonUtil.convertObjToStr(getChkRdNature()));
             if((CommonUtil.convertObjToStr(getRdoPenalRounded_Yes())).equals("Y"))
             {
                 objDepositsProductRDTO.setCboPenalRound(getCboRoundOffCriteriaPenal());
                 objDepositsProductRDTO.setRdoPenalRound(getRdoPenalRounded_Yes());
             }
             else if((CommonUtil.convertObjToStr(getRdoPenalRounded_No())).equals("Y"))
             {
                 //System.out.println("fgdhdh");
                 objDepositsProductRDTO.setRdoPenalRound("N");
             }
            objDepositsProductRDTO.setAgentCommision(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtAgentCommision()))));
            //            objDepositsProductRDTO.setAgentCommision(CommonUtil.convertObjToDouble(String.valueOf(CommonUtil.convertObjToInt(getTxtAgentCommision()) * multiply)));
            objDepositsProductRDTO.setAgentCommisionMode(getCboAgentCommision());
            objDepositsProductRDTO.setTxtInterestNotPayingValue(CommonUtil.convertObjToDouble(getTxtInterestNotPayingValue()));
            objDepositsProductRDTO.setCboInterestNotPayingValue(getCboInterestNotPayingValue());
            //getRdoRecurringDepositToFixedDeposit_Yes(){getRdoRecurringDepositToFixedDeposit_No
            if (getRdoRecurringDepositToFixedDeposit_Yes() == true){
                objDepositsProductRDTO.setConvertRdToFixed(YES);
            }else{
                objDepositsProductRDTO.setConvertRdToFixed(NO);
            }
            if (getRdoInstallmentInRecurringDepositAcct_Yes() == true){
                objDepositsProductRDTO.setInstallRecurringDepac("F");
            }else{
                objDepositsProductRDTO.setInstallRecurringDepac("V");
            }
            
            objDepositsProductRDTO.setInstallCharged(CommonUtil.convertObjToDouble(cbmInstallmentToBeCharged.getKeyForSelected()));
            objDepositsProductRDTO.setChangeValue((String)cbmChangeValue.getKeyForSelected());
            
            if (getRdoCanReceiveExcessInstal_yes() == true){
                objDepositsProductRDTO.setReceiveExcessInstall(YES);
            }else{
                objDepositsProductRDTO.setReceiveExcessInstall(NO);
            }
            if (getRdoIntPayableOnExcessInstal_Yes() == true){
                objDepositsProductRDTO.setIntpayExcessInstall(YES);
            }else{
                objDepositsProductRDTO.setIntpayExcessInstall(NO);
            }
           
            
            objDepositsProductRDTO.setCutoffType((String)cbmCutOffDayForPaymentOfInstal.getKeyForSelected());
            objDepositsProductRDTO.setCutoffPayInstall(CommonUtil.convertObjToDouble(getTxtCutOffDayForPaymentOfInstal()));
            
            Date FrDpDt =DateUtil.getDateMMDDYYYY(getTdtFromDepositDate());
            if(FrDpDt != null){
                Date frdpDate = (Date) curDate.clone();
                frdpDate.setDate(FrDpDt.getDate());
                frdpDate.setMonth(FrDpDt.getMonth());
                frdpDate.setYear(FrDpDt.getYear());
                //            objDepositsProductRDTO.setFromDepositDt(DateUtil.getDateMMDDYYYY(getTdtFromDepositDate()));
                objDepositsProductRDTO.setFromDepositDt(frdpDate);
            }else{
                objDepositsProductRDTO.setFromDepositDt(DateUtil.getDateMMDDYYYY(getTdtFromDepositDate()));
            }
            
            Date ChoDt =DateUtil.getDateMMDDYYYY(getTdtChosenDate());
            if(ChoDt != null){
                Date choDate = (Date) curDate.clone();
                choDate.setDate(ChoDt.getDate());
                choDate.setMonth(ChoDt.getMonth());
                choDate.setYear(ChoDt.getYear());
                //            objDepositsProductRDTO.setChosenDt(DateUtil.getDateMMDDYYYY(getTdtChosenDate()));
                objDepositsProductRDTO.setChosenDt(choDate);
            }else{
                objDepositsProductRDTO.setChosenDt(DateUtil.getDateMMDDYYYY(getTdtChosenDate()));
            }
            //objDepositsProductRDTO.setDepositFreq((String)cbmDepositsFrequency.getKeyForSelected());
            objDepositsProductRDTO.setDepositFreq(CommonUtil.convertObjToInt(cbmDepositsFrequency.getKeyForSelected()));
            objDepositsProductRDTO.setTxtRDIrregularIfInstallmentDue(getTxtRDIrregularIfInstallmentDue());
            if(isRdoIncaseOfIrregularRDRBRate()){
                objDepositsProductRDTO.setRdoIncaseOfIrregular("RD Rate");
               
                
            }
            if(isRdoIncaseOfIrregularRDSBRate()){
                objDepositsProductRDTO.setRdoIncaseOfIrregular("SB Rate");
               
            }
   
            if(isDueOn()){
            objDepositsProductRDTO.setInclFullMonth("Y");
            }
            else{
            objDepositsProductRDTO.setInclFullMonth("N");
            }
            
            objDepositsProductRDTO.setGracePeriod(CommonUtil.convertObjToInt(getTxtGracePeriod()));//added by Shany
            //            objDepositsProductRDTO.setFromAmount(CommonUtil.convertObjToStr(getTxtFromAmt()));
            
            // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
            objDepositsProductRDTO.setRdCloseOtherProdROI(getChkRDClosingOtherROI());
            objDepositsProductRDTO.setPrmatureCloseProd(getCboPrmatureCloseProduct());
            objDepositsProductRDTO.setPrmatureCloseRate(getCboprmatureCloseRate());
            objDepositsProductRDTO.setIrregularCloseProduct(getCboIrregularRDCloseProduct());
            objDepositsProductRDTO.setIrregulareCloseRate(getCboIrregularRDCloseRate());
            
            objDepositsProductRDTO.setApplyIntForIrregularRD(getChkIntForIrregularRD()); // Added by nithya on 18-03-2020 for KD-1535
            objDepositsProductRDTO.setSpecialRD(getChkSpecialRD());// Added by nithya on 18-03-2020 for KD-1535
            objDepositsProductRDTO.setNoOfSpecialRDInstallments(CommonUtil.convertObjToInt(getTxtSpecialRDInstallments())); // Added by nithya on 01-04-2020 for KD-1535
            
        }catch(Exception e){
            log.info("error in setRDData");
            e.printStackTrace();
            parseException.logException(e,true);
        }
        return objDepositsProductRDTO;
    }
    
    
    public void resetTableRecords() {
        tblInterestTable.setDataArrayList(null,interestTabTitle);
        tblDelayedInstallmet.setDataArrayList(null,delayinterestTabTitle);
    }
    
    //to populate the Ui Screens from the Database...
    //Do display the Data from the Database, in UI
    public void populateData(HashMap whereMap) {
        log.info("In populateData()");
        
        final HashMap mapData;
        try {
            System.out.println("deposit product whereMap :: " + whereMap);
            mapData = proxy.executeQuery(whereMap, operationMap);
            System.out.println("populateData mapData : " +mapData);
            populateOB(mapData);
        } catch( Exception e ) {
            log.info("Error In populateData()");
            e.printStackTrace();
        }
    }
    
    private void populateOB(HashMap mapData) throws Exception{
        log.info("In populateOB()");
        System.out.println("mapdata is"+mapData);
        DepositsProductTO objDepositsProductTO = null;
        DepositsProductSchemeTO objDepositsProductSchemeTO = null;
        DepositsProductIntPayTO objDepositsProductIntPayTO = null;
        DepositsProductAcHdTO objDepositsProductAcHdTO = null;
        DepositsProductTaxTO objDepositsProductTaxTO = null;
        DepositsProductRenewalTO objDepositsProductRenewalTO = null;
        DepositsProductRDTO objDepositsProductRDTO = null;
        // Added by nithya on 01-03-2016
        DepositsThriftBenevolentTO objDepositsThriftBenevolentTO = null; // Added by nithya on 02-03-2016 for 0003897
        // End
        InterestMaintenanceRateTO objInterestMaintenanceRateTO = null;
        //Taking the Value of Transaction Id from each Table...
        // Here the first Row is selected...
        
        objDepositsProductTO = (DepositsProductTO) ((List) mapData.get("DEPT_PROD")).get(0);
        if(objDepositsProductTO!=null){
        setDepositsProductTO(objDepositsProductTO);
        
        }
        
        objDepositsProductSchemeTO = (DepositsProductSchemeTO) ((List) mapData.get("DepositsProductSchemeTO")).get(0);
        if(objDepositsProductSchemeTO!=null){
        setDepositsProductSchemeTO(objDepositsProductSchemeTO);
        }
        
        objDepositsProductIntPayTO = (DepositsProductIntPayTO) ((List) mapData.get("DEPT_PROD_INTPAY")).get(0);
        if(objDepositsProductIntPayTO!=null){
        setDepositsProductIntPayTO(objDepositsProductIntPayTO);
        }
        
        objDepositsProductAcHdTO = (DepositsProductAcHdTO) ((List) mapData.get("DEPT_PROD_ACHD")).get(0);
        if(objDepositsProductAcHdTO!=null){
        setDepositsProductAcHdTO(objDepositsProductAcHdTO);
        }
                
        List map=(List)(mapData.get("DEPT_PROD_TAX"));
        if(map.size()>0){
            objDepositsProductTaxTO = (DepositsProductTaxTO) ((List) mapData.get("DEPT_PROD_TAX")).get(0);
            setDepositsProductTaxTO(objDepositsProductTaxTO);
        }
        objDepositsProductRenewalTO = (DepositsProductRenewalTO) ((List) mapData.get("DEPT_PROD_RENEWAL")).get(0);
        if(objDepositsProductRenewalTO!=null){
        setDepositsProductRenewalTO(objDepositsProductRenewalTO);
        }
        
        objDepositsProductRDTO = (DepositsProductRDTO) ((List) mapData.get("DEPT_PROD_RD")).get(0);
        if(objDepositsProductRDTO!=null){
        setDepositsProductRDTO(objDepositsProductRDTO);
        setStatusBy(objDepositsProductTO.getStatusBy());
        setAuthorizeStatus(objDepositsProductTO.getAuthorizeStatus());
        }
        
        // Added by nithya on 02-03-2016 for 0003897        
        
        if(mapData.containsKey("DEPO_THRIFT_BENEVOLENT")){
            objDepositsThriftBenevolentTOList = new ArrayList();           
            for(int i = 0;i<((List)mapData.get("DEPO_THRIFT_BENEVOLENT")).size(); i++){
                objDepositsThriftBenevolentTO = (DepositsThriftBenevolentTO) ((List)mapData.get("DEPO_THRIFT_BENEVOLENT")).get(i);        
                HashMap thriftBenevolentMap = setThriftBenevelontDataForProduct(objDepositsThriftBenevolentTO);
                objDepositsThriftBenevolentTOList.add(thriftBenevolentMap);
                
            }
            if(objDepositsThriftBenevolentTOList != null && objDepositsThriftBenevolentTOList.size() > 0){
                populateThriftBenevolentTable(objDepositsThriftBenevolentTOList);
            } 
            else{
                deleteThriftBenevolentTableData();
            }
            
        }
        
        // End
        
        //Added by Anju Anand for Mantis Id: 0010363
        if (mapData != null && mapData.containsKey("WEEKLY_DEP_SETTING") && mapData.get("WEEKLY_DEP_SETTING") != null) {
            List weeklyDepList = null;
            weeklyDepList = (List) mapData.get("WEEKLY_DEP_SETTING");
            if (weeklyDepList != null && weeklyDepList.size() > 0) {
                populateTable(weeklyDepList);
            }
        }
        
        if (mapData != null && mapData.containsKey("AGENT_COMM_SLAB_SETTING") && mapData.get("AGENT_COMM_SLAB_SETTING") != null) {
            List agentCommSlabLst = null;
            agentCommSlabLst = (List) mapData.get("AGENT_COMM_SLAB_SETTING");
            if (agentCommSlabLst != null && agentCommSlabLst.size() > 0) {
                populateAgentCommissionSlabTable(agentCommSlabLst);
            }
        }
        
        //Agents Commision Interest Rate Maintananace....
        interestList = new ArrayList();
        interestList =(ArrayList) mapData.get("AGENTS_COMMISION_DAILY");
        
            setInterRateType();
        
        ttNotifyObservers();
    }
    
    
    
    //Agents Commision Interest Rate Maintananace....
    void setInterRateType() throws Exception{
        log.info("setInterRateType");
        int size = interestList.size();
        for(int j=0; j < size; j++){
            interestTabRow = new ArrayList();
            InterestMaintenanceRateTO objInterestMaintenanceRateTO = (InterestMaintenanceRateTO)interestList.get(j);
            interestTabRow.add(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiDate()));
            interestTabRow.add(DateUtil.getStringDate(objInterestMaintenanceRateTO.getRoiEndDate()));
            
            interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getFromAmount()));
            interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getToAmount()));
            
            /** To set the value of From and To Period in the Table int the refined form ...
             */
            resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getFromPeriod());
            String period = setPeriod(resultValue);
            interestTabRow.add(String.valueOf(resultValue) + " " + period);
            resetPeriod();
            
            resultValue = CommonUtil.convertObjToInt(objInterestMaintenanceRateTO.getToPeriod());
            period = setPeriod(resultValue);
            interestTabRow.add(String.valueOf(resultValue) + " " +period);
            resetPeriod();
            
            interestTabRow.add(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getRoi()));
            setAgentsAuthStatus(CommonUtil.convertObjToStr(objInterestMaintenanceRateTO.getAuthorizedStatus()));
            System.out.println("*****@@@@@@@@@*******#######: " + interestTabRow);
            tblInterestTable.addRow(interestTabRow);
        }
    }
    
    //Agents Commision Interest Rate Maintananace....
    private ArrayList setRow(InterestMaintenanceRateTO objInterestMaintenanceRateTO){
        ArrayList row= new ArrayList();
        row.add(objInterestMaintenanceRateTO.getRoiDate());
        row.add(objInterestMaintenanceRateTO.getRoiEndDate());
        row.add(objInterestMaintenanceRateTO.getFromPeriod());
        row.add(objInterestMaintenanceRateTO.getToPeriod());
        row.add(objInterestMaintenanceRateTO.getFromAmount());
        row.add(objInterestMaintenanceRateTO.getToAmount());
        row.add(objInterestMaintenanceRateTO.getRoi());
        return row;
    }
    
    
    
    // To Enter the values in the UI fields, from the database...
    private void setDepositsProductTO(DepositsProductTO objDepositsProductTO) throws Exception{
        log.info("In setDepositsProductTO()");

        setTxtProductID(CommonUtil.convertObjToStr(objDepositsProductTO.getProdId()));
        setTxtDesc(CommonUtil.convertObjToStr(objDepositsProductTO.getProdDesc()));
        setTxtAcctHd(CommonUtil.convertObjToStr(objDepositsProductTO.getAcctHead()));
        setCboOperatesLike(CommonUtil.convertObjToStr(getCbmOperatesLike().getDataForKey(objDepositsProductTO.getBehavesLike())));
        setTxtSuffix(CommonUtil.convertObjToStr(objDepositsProductTO.getAuthorizeRemark()));
        setTxtLastAcctNumber(CommonUtil.convertObjToStr(objDepositsProductTO.getRemarks()));
        setCboProdCurrency(CommonUtil.convertObjToStr(getCbmProdCurrency().getDataForKey(objDepositsProductTO.getBaseCurrency())));
        setLblAuthorize(CommonUtil.convertObjToStr(objDepositsProductTO.getAuthorizeStatus()));
        setChkIsGroupDepositProduct(CommonUtil.convertObjToStr(objDepositsProductTO.getIsGroupDepositProduct())); // Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
    }
    
    private void setDepositsProductSchemeTO(DepositsProductSchemeTO objDepositsProductSchemeTO) throws Exception{
        log.info("In setDepositsProductSchemeTO()");
        
        if(objDepositsProductSchemeTO.getExcludeLienIntrstAppl()!=null){
            setChkExcludeLienIntrstAppl(objDepositsProductSchemeTO.getExcludeLienIntrstAppl());
        }
        
        if(objDepositsProductSchemeTO.getExcludeLienStanding()!=null){
            setChkExcludeLienStanding(objDepositsProductSchemeTO.getExcludeLienStanding());
        }
        
        if(objDepositsProductSchemeTO.getCummCertPrint()!=null){
            setChkCummCertPrint(objDepositsProductSchemeTO.getCummCertPrint());
        }
        
        if(objDepositsProductSchemeTO.getDepositUnlien()!=null){
            setChkDepositLien(objDepositsProductSchemeTO.getDepositUnlien());
        }
        
        
        if (objDepositsProductSchemeTO.getCalculateTds() != null && objDepositsProductSchemeTO.getCalculateTds().equals(YES)) setRdoCalcTDS_Yes(true);
        else setRdoCalcTDS_No(true);
        
        if (objDepositsProductSchemeTO.getPayintDepMaturity() != null && objDepositsProductSchemeTO.getPayintDepMaturity().equals(YES)) setRdoPayInterestOnHoliday_Yes(true);
        else setRdoPayInterestOnHoliday_No(true);
        
        if (objDepositsProductSchemeTO.getMaturityAmtRound() != null && objDepositsProductSchemeTO.getMaturityAmtRound().equals(YES)) setRdoAmountRounded_Yes(true);
        else setRdoAmountRounded_No(true);
        
        if (objDepositsProductSchemeTO.getDiscounted()!= null && objDepositsProductSchemeTO.getDiscounted().equals(YES)) setDiscounted_Yes(true);
        else setDiscounted_No(true);
        
        if (objDepositsProductSchemeTO.getTypesOfDep() != null && objDepositsProductSchemeTO.getTypesOfDep().equals(REGULAR)) setRdoRegular(true);
        else if(objDepositsProductSchemeTO.getTypesOfDep() != null && objDepositsProductSchemeTO.getTypesOfDep().equals(NRO)) setRdoNRO(true);
        else if(objDepositsProductSchemeTO.getTypesOfDep() != null && objDepositsProductSchemeTO.getTypesOfDep().equals(NRE)) setRdoNRE(true);
        
        if (objDepositsProductSchemeTO.getStaffAccount() != null && objDepositsProductSchemeTO.getStaffAccount().equals(YES)) setRdoStaffAccount_Yes(true);
        else setRdoStaffAccount_No(true);
        
        //            changed by nikhil for Call deposit
        if(objDepositsProductSchemeTO.getRdoWithPeriod() != null){
            setRdoWithPeriod(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getRdoWithPeriod()));
        }
        if(objDepositsProductSchemeTO.getRdoDoublingScheme() != null){
            setRdoDoublingScheme(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getRdoDoublingScheme()));
        }
        setTxtDoublingCount(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getDoubligCount()));
        setCboRoundOffCriteria((String) getCbmRoundOffCriteria().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getRoundoffCriteria())));
        
        if (objDepositsProductSchemeTO.getIntpaidAftermaturity() != null && objDepositsProductSchemeTO.getIntpaidAftermaturity().equals(YES)) setRdoInterestAfterMaturity_Yes(true);
        else setRdoInterestAfterMaturity_No(true);
        
        setCboMaturityInterestType((String) getCbmMaturityInterestType().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAftermaturityInttype())));
        setCboMaturityInterestRate((String) getCbmMaturityInterestRate().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAftermaturityIntrate())));
        
        resultValue = CommonUtil.convertObjToInt(objDepositsProductSchemeTO.getDepositPerUnit());
        String period = setPeriod(resultValue);
        //        setCboDepositPerUnit(period);
        setTxtDepositPerUnit(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getDepositPerUnit()));
        resultValue = CommonUtil.convertObjToInt(objDepositsProductSchemeTO.getMinDepositPeriod());
        period = setPeriod(resultValue);
        setCboMinDepositPeriod(period);
        setTxtMinDepositPeriod(String.valueOf(resultValue));
        resultValue = CommonUtil.convertObjToInt(objDepositsProductSchemeTO.getMaxDepositPeriod());
        period = setPeriod(resultValue);
        setCboMaxDepositPeriod(period);
        setTxtMaxDepositPeriod(String.valueOf(resultValue));
        resultValue = CommonUtil.convertObjToInt(objDepositsProductSchemeTO.getPeriodMultiples());
        period = setPeriod(resultValue);
        setCboPeriodInMultiplesOf(period);
        setTxtPeriodInMultiplesOf(String.valueOf(resultValue));
        resultValue = CommonUtil.convertObjToInt(objDepositsProductSchemeTO.getPrematureWithdrawal());
        period = setPeriod(resultValue);
        setCboPrematureWithdrawal(period);
        setTxtPrematureWithdrawal(String.valueOf(resultValue));
        
        setTxtMinDepositAmt(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getMinDepositAmt()));
        setTxtMaxDepositAmt(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getMaxDepositAmt()));
        setTxtAmtInMultiplesOf(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAmtMultiples()));
        
        if (objDepositsProductSchemeTO.getCalcMaturityValue() != null && objDepositsProductSchemeTO.getCalcMaturityValue().equals(YES)) setRdoCalcMaturityValue_Yes(true);
        else setRdoCalcMaturityValue_No(true);
        
        if (objDepositsProductSchemeTO.getProvIntmaturedDep() != null && objDepositsProductSchemeTO.getProvIntmaturedDep().equals(YES)) setRdoProvisionOfInterest_Yes(true);
        else setRdoProvisionOfInterest_No(true);
        
        if (objDepositsProductSchemeTO.getTransMaturedDep() != null && objDepositsProductSchemeTO.getTransMaturedDep().equals(YES)) setRdoTransferToMaturedDeposits_Yes(true);
        else setRdoTransferToMaturedDeposits_No(true);
        
        setTxtAfterHowManyDays(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAfterDays()));
        setTxtAdvanceMaturityNoticeGenPeriod(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAdvMaturityGenpd()));
        setTxtInterestOnMaturedDeposits(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getIntMaturedDep()));
        
        if (objDepositsProductSchemeTO.getPartialWithdrawal() != null && objDepositsProductSchemeTO.getPartialWithdrawal().equals(YES)) setRdoPartialWithdrawalAllowed_Yes(true);
        else setRdoPartialWithdrawalAllowed_No(true);
        
        setTxtNoOfPartialWithdrawalAllowed(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getNoPartialWithdrawal()));
        setTxtMaxAmtOfPartialWithdrawalAllowed(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getMaxAmtWithdrawal()));
        setTxtMaxNoOfPartialWithdrawalAllowed(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getMaxNoWithdrawalYr()));
        setTxtMinAmtOfPartialWithdrawalAllowed(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getMinAmtWithdrawal()));
        setTxtWithdrawalsInMultiplesOf(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getWithdrawalMulti()));
        setTxtServiceCharge(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getServiceChargePer()));
        
        if (objDepositsProductSchemeTO.getWithdrawalInterest() != null && objDepositsProductSchemeTO.getWithdrawalInterest().equals(YES)) setRdoWithdrawalWithInterest_Yes(true);
        else setRdoWithdrawalWithInterest_No(true);
        
        if (objDepositsProductSchemeTO.getServiceCharge() != null && objDepositsProductSchemeTO.getServiceCharge().equals(YES)) setRdoServiceCharge_Yes(true);
        else setRdoServiceCharge_No(true);
        
        if (objDepositsProductSchemeTO.getExtDepMaturity() != null && objDepositsProductSchemeTO.getExtDepMaturity().equals(YES)) setRdoExtnOfDepositBeforeMaturity_Yes(true);
        else setRdoExtnOfDepositBeforeMaturity_No(true);
        
        if (objDepositsProductSchemeTO.getAdjMaturityLoan() != null && objDepositsProductSchemeTO.getAdjMaturityLoan().equals(YES)) setRdoAutoAdjustment_Yes(true);
        else setRdoAutoAdjustment_No(true);
        
        if (objDepositsProductSchemeTO.getAdjIntLien() != null && objDepositsProductSchemeTO.getAdjIntLien().equals(YES)) setRdoAdjustIntOnDeposits_Yes(true);
        else setRdoAdjustIntOnDeposits_No(true);
        
        if (objDepositsProductSchemeTO.getAdjPrinLoanLien() != null && objDepositsProductSchemeTO.getAdjPrinLoanLien().equals(YES)) setRdoAdjustPrincipleToLoan_Yes(true);
        else setRdoAdjustPrincipleToLoan_No(true);
        
        //        setTxtPrematureWithdrawal(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getPrematureWithdrawal()));
        
        if (objDepositsProductSchemeTO.getFlexiSbCa() != null && objDepositsProductSchemeTO.getFlexiSbCa().equals(YES)) setRdoFlexiFromSBCA_Yes(true);
        else setRdoFlexiFromSBCA_No(true);
        
        if (objDepositsProductSchemeTO.getTdSecurityOd() != null && objDepositsProductSchemeTO.getTdSecurityOd().equals(YES)) setRdoTermDeposit_Yes(true);
        else setRdoTermDeposit_No(true);
        
        
        setTxtAcctNumberPattern(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAcnumPattern()));
        setTxtAlphaSuffix(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAlphaSuffixTdrec()));
        setTxtMaxAmtOfCashPayment(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getMaxAmtCash()));
        setTxtMinAmtOfPAN(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getMinAmtPan()));
        
        
        if (objDepositsProductSchemeTO.getIntroRequired() != null && objDepositsProductSchemeTO.getIntroRequired().equals(YES)) setRdoIntroducerReqd_Yes(true);
        else setRdoIntroducerReqd_No(true);
        
        if (objDepositsProductSchemeTO.getCertificatePrint() != null){
            setRdoCertificate_printing(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getCertificatePrint()));
        }else{
            setRdoCertificate_printing("");
        }
        
        //        if (objDepositsProductSchemeTO.getSysCalcChg() != null && objDepositsProductSchemeTO.getSysCalcChg().equals(YES)) setRdoSystemCalcValues_Yes(true);
        //        else setRdoSystemCalcValues_No(true);
        
        setTdtSchemeIntroDate(DateUtil.getStringDate(objDepositsProductSchemeTO.getSchemeIntroDt()));
        setTdtSchemeClosingDate(DateUtil.getStringDate(objDepositsProductSchemeTO.getSchemeClosingDt()));
        //        setTxtScheme(objDepositsProductSchemeTO.getScheme());
        
        setTxtLimitForBulkDeposit(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getLimitBulkDeposit()));
        setTxtAfterNoDays(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getAfterNoDays()));
        if (objDepositsProductSchemeTO.getFdRenewalSameNoTranPrincAmt() != null && objDepositsProductSchemeTO.getFdRenewalSameNoTranPrincAmt().equals(YES)) setChkFDRenewalSameNoTranPrincAmt(true);
        else setChkFDRenewalSameNoTranPrincAmt(false);
        if(CommonUtil.convertObjToStr(objDepositsProductSchemeTO.getCanZeroBalActOpeng()).equals("Y"))
           setChkCanZeroBalAct("Y");
        else
           setChkCanZeroBalAct("N");
        
        if(objDepositsProductSchemeTO.getAgentcommSlabRequired().equalsIgnoreCase("Y")){
            setChkAgentCommSlabRequired("Y");            
        }else{
            setChkAgentCommSlabRequired("N");           
        }
        setCboAgentCommCalcMethod(objDepositsProductSchemeTO.getAgentCommCalcMethod());
        System.out.println("setCboAgentCommCalcMethod " + getCboAgentCommCalcMethod());
    }
    
    // Method to calculate is its Years, Months or Days
    //depending on the numerical value from DataBase...
    // to be displayed in UI
    private String setPeriod(int rV) throws Exception{
        String periodValue;
        //        if (rV >= YEAR_INT) {
        //            periodValue = YEAR_STR;
        //            rV = rV/YEAR_INT;
        //        } else if (rV >= MONTH_INT) {
        //            periodValue=MONTH_STR;
        //            rV = rV/MONTH_INT;
        //        } else if (rV >= DAY_INT) {
        //            periodValue=DAY_STR;
        //            rV = rV;
        //        } else {
        //            periodValue="";
        //            rV = 0;
        //        }
        if ((rV >= YEAR_INT) && (rV%YEAR_INT == 0)) {
            periodValue = YEAR_STR;
            rV = rV/YEAR_INT;
        } else if ((rV >= MONTH_INT) && (rV % MONTH_INT == 0)) {
            periodValue=MONTH_STR;
            rV = rV/MONTH_INT;
        } else if ((rV >= DAY_INT) && (rV % DAY_INT == 0)) {
            periodValue=DAY_STR;
            rV = rV;
        } else {
            periodValue="";
            rV = 0;
        }
        resultValue = rV;
        return periodValue;
    }
    
    
    private void setDepositsProductIntPayTO(DepositsProductIntPayTO objDepositsProductIntPayTO) throws Exception{
        log.info("In setDepositsProductIntPayTO()");
        
        setCboIntMaintainedAsPartOf((String) getCbmIntMaintainedAsPartOf().getDataForKey(objDepositsProductIntPayTO.getIntMaintainPart()));
        setCboIntCalcMethod((String) getCbmIntCalcMethod().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getIntCalcMethod())));
        
        if (objDepositsProductIntPayTO.getIntProvAppl() != null && objDepositsProductIntPayTO.getIntProvAppl().equals(YES)) setRdoIntProvisioningApplicable_Yes(true);
        else setRdoIntProvisioningApplicable_No(true);
        
        
        setCboIntProvisioningFreq((String) getCbmIntProvisioningFreq().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getIntProvFreq())));
        setCboProvisioningLevel((String) getCbmProvisioningLevel().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getProvLevel())));
        setCboNoOfDays((String) getCbmNoOfDays().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getNoDaysYear())));
        setCboIntCompoundingFreq((String) getCbmIntCompoundingFreq().getDataForKey( CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getIntCompFreq())));
        setCboInterestType((String) getCbmInterestType().getDataForKey( CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getIntType())));
        setCboIntApplnFreq((String) getCbmIntApplnFreq().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getIntApplFreq())));
        
        
        setTdtLastIntProvisionalDate(DateUtil.getStringDate(objDepositsProductIntPayTO.getLastIntProvdt()));
        setTdtNextIntProvisionalDate(DateUtil.getStringDate(objDepositsProductIntPayTO.getNextIntProvdt()));
        setTdtLastInterestAppliedDate(DateUtil.getStringDate(objDepositsProductIntPayTO.getLastIntAppldt()));
        setTdtNextInterestAppliedDate(DateUtil.getStringDate(objDepositsProductIntPayTO.getNextIntAppldt()));
        
        setCboIntRoundOff((String) getCbmIntRoundOff().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getIntRoundoffTerms())));
        setTxtMinIntToBePaid(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getMinIntPaid()));
        if(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getChkROI()).equals("Y"))
            setChkDifferentROI(true);
        else
            setChkDifferentROI(false);
        if(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getSlabWiseInterest()).equals("Y")){
            setChkSlabWiseInterest(true);
        }else{
            setChkSlabWiseInterest(false);
        }
                if(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getSlabWiseCommision()).equals("Y")){
            setChkSlabWiseCommision(true);
        }else{
            setChkSlabWiseCommision(false);
        }
        if(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getPrematClosSIRat()).equals("Y"))
           setChkPrematureClosure("Y");
        else
           setChkPrematureClosure("N");
       
        
                    if(objDepositsProductIntPayTO.getPreMatureClosureApply()!=null)
        if(objDepositsProductIntPayTO.getPreMatureClosureApply().equals("SB Rate")){
            setRdoDepositRate(false);
            setRdoSBRate(true);
            setCboSbProduct((String) getCbmSbProduct().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getFixedDepositProduct())));
        }
        else{
            setRdoDepositRate(true);
            setRdoSBRate(false);
            setCboDepositsProdFixd((String) getCbmDepositsProdFixd().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getFixedDepositProduct())));
        }
        
        setCboPreMatIntType(CommonUtil.convertObjToStr(getCbmPreMatIntTypeModel().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductIntPayTO.getPreMatIntType()))));
        if(objDepositsProductIntPayTO.getCbxInterstRoundTime()!=null && !objDepositsProductIntPayTO.getCbxInterstRoundTime().equals("") && objDepositsProductIntPayTO.getCbxInterstRoundTime().equals("Y"))
        {
            setCbxInterstRoundTime("Y");
        }
        else
        {
             setCbxInterstRoundTime("N");
        } if((objDepositsProductIntPayTO.getAppNormRate()!=null || !objDepositsProductIntPayTO.getAppNormRate().equals("")) && objDepositsProductIntPayTO.getAppNormRate().equals("Y"))
        {
            setChkAppNormRate("Y");
        }
        else
        {
             setChkAppNormRate("N");
        }
        setTxtNormPeriod(String.valueOf(objDepositsProductIntPayTO.getNormPeriod()));
        setTxtCategoryBenifitRate(String.valueOf(objDepositsProductIntPayTO.getCategoryBenifitRate()));
        setChkIntEditable(objDepositsProductIntPayTO.getIntRateEditable());
    }
    
    private void setDepositsProductAcHdTO(DepositsProductAcHdTO objDepositsProductAcHdTO) throws Exception{
        log.info("In setDepositsProductAcHdTO()");
        
        setTxtIntProvisioningAcctHd(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getIntProvAchd()));
        setTxtIntPaybleGLHead(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getIntPay()));
        setTxtIntDebitPLHead(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getIntDebit()));
        setTxttMaturityDepositAcctHead(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getMaturityDeposit()));
        setTxtIntOnMaturedDepositAcctHead(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getIntMaturedDeposit()));
        setTxtIntProvisionOfMaturedDeposit(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getIntProvMatured()));
        setTxtAcctHeadForFloatAcct(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getAchdFloatAc()));
        setTxtFixedDepositAcctHead(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getFixedDepositAchd()));
        setTxtInterestRecoveryAcHd(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getInterestRecoveryAcHd()));
        setTxtCommisionPaybleGLHead(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getCommisionAchd()));
        setTxtPenalChargesAchd(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getDelayedAchd()));
        setTxtTransferOutAcHd(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getTrasferOutAchd()));
         setPostageAcHd(CommonUtil.convertObjToStr(objDepositsProductAcHdTO.getPostageAcHd()));
         setTxtBenIntReserveHead(objDepositsProductAcHdTO.getBenovelentIntReserveHd());
        
    }
    
    private void setDepositsProductTaxTO(DepositsProductTaxTO objDepositsProductTaxTO) throws Exception{
        log.info("In seDepositsProductTaxTO()");
        
        setTxtTDSGLAcctHd(CommonUtil.convertObjToStr(objDepositsProductTaxTO.getTdsGlAchd()));
        if (objDepositsProductTaxTO.getRecalcMaturityValtds() != null && objDepositsProductTaxTO.getRecalcMaturityValtds().equals(YES)) setRdoRecalcOfMaturityValue_Yes(true);
        else setRdoRecalcOfMaturityValue_No(true);
    }
    
    private void setDepositsProductRenewalTO(DepositsProductRenewalTO objDepositsProductRenewalTO) throws Exception{
        log.info("In setDepositsProductRenewalTO()");
        resultValue = CommonUtil.convertObjToInt(objDepositsProductRenewalTO.getMaxPdbkdtRenewal());
        String period = setPeriod(resultValue);
        setCboIntPeriodForBackDatedRenewal(period);
        setTxtIntPeriodForBackDatedRenewal(String.valueOf(resultValue));
        
        setCboIntType((String) getCbmIntType().getDataForKey(objDepositsProductRenewalTO.getMaxPdInttype()));
        
        resultValue = CommonUtil.convertObjToInt(objDepositsProductRenewalTO.getMaxPdMaturitydt());
        setCboMaxPeriodMDt(setPeriod(resultValue));
        setTxtMaxPeriodMDt(String.valueOf(resultValue));
        
        resultValue = CommonUtil.convertObjToInt(objDepositsProductRenewalTO.getMinDaysBkdtDeposits());
        setCboMinNoOfDays(setPeriod(resultValue));
        setTxtMinNoOfDays(String.valueOf(resultValue));
        
        setCboIntCriteria((String) getCbmIntCriteria().getDataForKey(objDepositsProductRenewalTO.getIntCriteria()));
        setCboMinimumRenewalDeposit((String)getCbmMinimumRenewalDeposit().getDataForKey(objDepositsProductRenewalTO.getPeriodOfFormat()));
        if (objDepositsProductRenewalTO.getRenewalDepositAllowed() != null && objDepositsProductRenewalTO.getRenewalDepositAllowed().equals(YES)) setRdoRenewalOfDepositAllowed_Yes(true);
        else setRdoRenewalOfDepositAllowed_No(true);
        
        //        setTxtMinNoOfDays(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getMinDaysBkdtDeposits()));
        
        if (objDepositsProductRenewalTO.getAutoRenewalAllowed() != null && objDepositsProductRenewalTO.getAutoRenewalAllowed().equals(YES)) setRdoAutoRenewalAllowed_Yes(true);
        else setRdoAutoRenewalAllowed_No(true);
        
        if (objDepositsProductRenewalTO.getRenewedDepIntPay() != null && objDepositsProductRenewalTO.getRenewedDepIntPay().equals(YES)) setRdoClosedwithinperiod_Yes(true);
        else setRdoClosedwithinperiod_No(true);
        
        if (objDepositsProductRenewalTO.getSameNoRenewalAllowed() != null && objDepositsProductRenewalTO.getSameNoRenewalAllowed().equals(YES))
            setRdoSameNoRenewalAllowed_Yes(true);
        else
            setRdoSameNoRenewalAllowed_No(true);
         if(objDepositsProductRenewalTO.getBeyondOriginal()!=null && objDepositsProductRenewalTO.getBeyondOriginal().equals(YES)){
            setRdoExtensionBeyondOriginalDate_Yes(true);
        }else{
            setRdoExtensionBeyondOriginalDate_No(true);
        }
         if(objDepositsProductRenewalTO.getExtensionPenal()!=null && objDepositsProductRenewalTO.getExtensionPenal().equals(YES)){
            setRdoExtensionPenal_Yes(true);
        }else{
            setRdoExtensionPenal_No(true);
        }
        setTxtMaxNoSameNo(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getMaxNoSameNoRenewal()));
        if(!getCboOperatesLike().equals("") && getCboOperatesLike().equals("Daily")){
            if(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getDailyIntCalc()).equals("DAILY_CALC"))
                setRdoDaily(true);
            else if(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getDailyIntCalc()).equals("WEEKLY")){
                setRdoWeekly(true);
                setCboWeekly_Day((String)getCbmWeekly().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getWeeklyBasis())));
            }else if(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getDailyIntCalc()).equals("MONTHLY")){
                //                setCbo
                setRdoMonthly(true);
                setCboMonthlyIntCalcMethod((String)getCbmMonthlyIntCalcMethod().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getCbmMonthlyIntCalcMethod())));
                
            }
        }else{
            objDepositsProductRenewalTO.setDailyIntCalc("");
            objDepositsProductRenewalTO.setWeeklyBasis(new Double(0));
        }
        if(objDepositsProductRenewalTO.getOneRateAvail()!=null && objDepositsProductRenewalTO.getOneRateAvail().equals("Y")){
            setRdoSBRateOneRate_Yes(true);
        }else if(objDepositsProductRenewalTO.getOneRateAvail()!=null && objDepositsProductRenewalTO.getOneRateAvail().equals("N")){
            setRdoSBRateOneRate_No(true);
        }
        if(objDepositsProductRenewalTO.getRdoPartialWithdrawlForDD() != null && objDepositsProductRenewalTO.getRdoPartialWithdrawlForDD().equals("Y")){
            setRdoPartialWithdrawlAllowedForDD("Y");
        }else if(objDepositsProductRenewalTO.getRdoPartialWithdrawlForDD() != null && objDepositsProductRenewalTO.getRdoPartialWithdrawlForDD().equals("N")){
            setRdoPartialWithdrawlAllowedForDD("N");
        }else{
            setRdoPartialWithdrawlAllowedForDD("N");
        }
        setTxtMaxNopfTimes(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getMaxNoAutoRenewal()));
        if(objDepositsProductRenewalTO. getRenewedDepIntRecovered()!=null && objDepositsProductRenewalTO.getRenewedDepIntRecovered().equals("Y")){
            setRdoInterestalreadyPaid_Yes(true);
        }else if(objDepositsProductRenewalTO .getRenewedDepIntRecovered()!=null && objDepositsProductRenewalTO.getRenewedDepIntRecovered().equals("N")){
            setRdoInterestalreadyPaid_No(true);
        }
        if(objDepositsProductRenewalTO. getIntRateAppliedOverdue()!=null && objDepositsProductRenewalTO.getIntRateAppliedOverdue().equals("Y")){
            setRdoInterestrateappliedoverdue_Yes(true);
        }else if(objDepositsProductRenewalTO. getIntRateAppliedOverdue()!=null && objDepositsProductRenewalTO.getIntRateAppliedOverdue().equals("N")){
            setRdoInterestrateappliedoverdue_No(true);
        }
        setCboEitherofTwoRatesChoose((String)getCbmEitherofTwoRatesChoose().getDataForKey(objDepositsProductRenewalTO.getEligibleTwoRate()));
        if(objDepositsProductRenewalTO.getDateOfRenewal()!=null && objDepositsProductRenewalTO.getDateOfRenewal().equals("Y")){
            setChkRateAsOnDateOfRenewal(true);
        }else {
            setChkRateAsOnDateOfRenewal(false);
        }
        if(objDepositsProductRenewalTO.getDateOfMaturity()!=null && objDepositsProductRenewalTO.getDateOfMaturity().equals("Y")){
            setChkRateAsOnDateOfMaturity(true);
        }else {
            setChkRateAsOnDateOfMaturity(false);
        }
        if(objDepositsProductRenewalTO.getBothRateNotAvail()!=null && objDepositsProductRenewalTO.getBothRateNotAvail().equals("Y")){
            setRdoBothRateNotAvail_Yes(true);
        }else if(objDepositsProductRenewalTO.getBothRateNotAvail()!=null && objDepositsProductRenewalTO.getBothRateNotAvail().equals("N")){
            setRdoBothRateNotAvail_No(true);
        }
        setTxtMinimumRenewalDeposit(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getPeriodOfRenewal()));
        setTxtRenewedclosedbefore(CommonUtil.convertObjToStr(objDepositsProductRenewalTO.getRenewdDepClosedBefore()));
        setCboRenewedclosedbefore((String)getCbmRenewedclosedbefore().getDataForKey(objDepositsProductRenewalTO. getRenewedDepositFormat()));
        //        setCboMinimumRenewalDeposit((String)getCbmRenewedclosedbefore().getDataForKey(objDepositsProductRenewalTO.getPeriodOfFormat()));
          setSbRateCmb((String)getCbmSbrateModel().getDataForKey(objDepositsProductRenewalTO.getSbRateProdId()));
        if(objDepositsProductRenewalTO.getClosureIntYN()!=null && objDepositsProductRenewalTO.getClosureIntYN().equals("Y")){
            setChkIntRateApp(true);
        }else {
            setChkIntRateApp(false);
        }

        if (objDepositsProductRenewalTO.getDeathMarkedYN() != null && objDepositsProductRenewalTO.getDeathMarkedYN().equals("Y")) {
            setChkIntRateDeathMarked(true);
        } else {
            setChkIntRateDeathMarked(false);
        }
        
    }
    
    private void setDepositsProductRDTO(DepositsProductRDTO objDepositsProductRDTO) throws Exception{
        log.info("In setDepositsProductRDTO()");
        if (objDepositsProductRDTO.getLateInstallAllowed() != null && objDepositsProductRDTO.getLateInstallAllowed().equals(YES)) {
            setRdoLastInstallmentAllowed_Yes(true);
        }else {
            setRdoLastInstallmentAllowed_No(true);
        }
        
        if (objDepositsProductRDTO.getPenaltyLateInstall() != null && objDepositsProductRDTO.getPenaltyLateInstall().equals(YES)){
            setRdoPenaltyOnLateInstallmentsChargeble_Yes(true);
            setRdoPenaltyOnLateInstallmentsChargeble_No(false);
            
        }
        else {
            setRdoPenaltyOnLateInstallmentsChargeble_No(true);
            setRdoPenaltyOnLateInstallmentsChargeble_Yes(false);
        }
       
        if(CommonUtil.convertObjToStr(objDepositsProductRDTO.getIntAppSlab()).equals("Y"))
            setChkIntApplicationSlab("Y");
        else
            setChkIntApplicationSlab("N");
        if(CommonUtil.convertObjToStr(objDepositsProductRDTO.getChkWeeklySpec()).equals("Y"))
            setChkWeeklySpecial("Y");
        else
            setChkWeeklySpecial("N");
        if(CommonUtil.convertObjToStr(objDepositsProductRDTO.getChkRdNature()).equals("Y"))
            setChkRdNature("Y");
        else
            setChkRdNature("N");
        if(CommonUtil.convertObjToStr(objDepositsProductRDTO.getRdoPenalRound()).equals("Y"))
            setRdoPenalRounded_Yes("Y");
        else
            setRdoPenalRounded_No("Y");
        setCboRoundOffCriteriaPenal(objDepositsProductRDTO.getCboPenalRound());
        if (objDepositsProductRDTO.getInsBeyondMaturityDt() != null && objDepositsProductRDTO.getInsBeyondMaturityDt().equals(YES)) setRdoInsBeyondMaturityDt_Yes(true);
        else setRdoInsBeyondMaturityDt_No(true);
        
        //        setTxtMaturityDateAfterLastInstalPaid(CommonUtil.convertObjToStr(objDepositsProductRDTO.getMaturityDtLastinstall()));
        resultValue = CommonUtil.convertObjToInt(objDepositsProductRDTO.getMaturityDtLastinstall());
        String period = setPeriod(resultValue);
        setCboMaturityDateAfterLastInstalPaid(period);
        setTxtMaturityDateAfterLastInstalPaid(String.valueOf(resultValue));
        resultValue = 0;
        resultValue = CommonUtil.convertObjToInt(objDepositsProductRDTO.getMinimumPeriod());
        period = setPeriod(resultValue);
        setCboMinimumPeriod(period);
        setTxtMinimumPeriod(String.valueOf(resultValue));
        resultValue = 0;
        resultValue = CommonUtil.convertObjToInt(objDepositsProductRDTO.getAgentCommision());
        period = setPeriod(resultValue);
        setCboAgentCommision(period);
        setTxtAgentCommision(String.valueOf(resultValue));
        setCboAgentCommision(objDepositsProductRDTO.getAgentCommisionMode());
        setTxtInterestNotPayingValue(CommonUtil.convertObjToStr(objDepositsProductRDTO.getTxtInterestNotPayingValue()));
        setCboInterestNotPayingValue(objDepositsProductRDTO.getCboInterestNotPayingValue());
        if (objDepositsProductRDTO.getConvertRdToFixed() != null && objDepositsProductRDTO.getConvertRdToFixed().equals(YES)) setRdoRecurringDepositToFixedDeposit_Yes(true);
        else setRdoRecurringDepositToFixedDeposit_No(true);
        
        if (objDepositsProductRDTO.getInstallRecurringDepac() != null && objDepositsProductRDTO.getInstallRecurringDepac().equals("F")) setRdoInstallmentInRecurringDepositAcct_Yes(true);
        else setRdoInstallmentInRecurringDepositAcct_No(true);
        
        setCboInstallmentToBeCharged((String) getCbmInstallmentToBeCharged().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductRDTO.getInstallCharged())));
        setCboChangeValue((String) getCbmChangeValue().getDataForKey(objDepositsProductRDTO.getChangeValue()));
        
        //added for daily deposit scheme
        if(objDepositsProductRDTO.getDepositFreq() != null)
          setCboDepositsFrequency((String)getCbmDepositsFrequency().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductRDTO.getDepositFreq())));
        
        if (objDepositsProductRDTO.getReceiveExcessInstall() != null && objDepositsProductRDTO.getReceiveExcessInstall().equals(YES)) setRdoCanReceiveExcessInstal_yes(true);
        else setRdoCanReceiveExcessInstal_No(true);
        
        if (objDepositsProductRDTO.getIntpayExcessInstall() != null && objDepositsProductRDTO.getIntpayExcessInstall().equals(YES)) setRdoIntPayableOnExcessInstal_Yes(true);
        else setRdoIntPayableOnExcessInstal_No(true);
       
        setCboCutOffDayForPaymentOfInstal((String) getCbmCutOffDayForPaymentOfInstal().getDataForKey(CommonUtil.convertObjToStr(objDepositsProductRDTO.getCutoffType())));
        //        if(Double.parseDouble(CommonUtil.convertObjToStr(objDepositsProductRDTO.getCutoffPayInstall())) == 0){
        //            setTxtCutOffDayForPaymentOfInstal("");
        //        }else{
        //            setTxtCutOffDayForPaymentOfInstal(CommonUtil.convertObjToStr(objDepositsProductRDTO.getCutoffPayInstall()));
        //        }
       
        
        
        
        //        setCboDepositsFrequency((String)getCbmDepositsFrequency()
        setTdtFromDepositDate(DateUtil.getStringDate(objDepositsProductRDTO.getFromDepositDt()));
        setTdtChosenDate(DateUtil.getStringDate(objDepositsProductRDTO.getChosenDt()));
         if(objDepositsProductRDTO.getRdoIncaseOfIrregular()!=null)
        if(objDepositsProductRDTO.getRdoIncaseOfIrregular().equals("RD Rate")){
        setRdoIncaseOfIrregularRDRBRate(true);
        setRdoIncaseOfIrregularRDSBRate(false);
        }else{
            setRdoIncaseOfIrregularRDRBRate(false);
            setRdoIncaseOfIrregularRDSBRate(true);
        }
        setTxtRDIrregularIfInstallmentDue(objDepositsProductRDTO.getTxtRDIrregularIfInstallmentDue());
     
        if (objDepositsProductRDTO.getInclFullMonth().equals("Y")) {
            setDueOn(true);
        } else {
            setDueOn(false);
        }
        setTxtGracePeriod(CommonUtil.convertObjToStr(objDepositsProductRDTO.getGracePeriod()));// Added by shany
        
        // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
        setChkRDClosingOtherROI(objDepositsProductRDTO.getRdCloseOtherProdROI());
        setCboPrmatureCloseProduct(objDepositsProductRDTO.getPrmatureCloseProd());
        setCboprmatureCloseRate(objDepositsProductRDTO.getPrmatureCloseRate());
        setCboIrregularRDCloseProduct(objDepositsProductRDTO.getIrregularCloseProduct());
        setCboIrregularRDCloseRate(objDepositsProductRDTO.getIrregulareCloseRate());
        
        setChkIntForIrregularRD(objDepositsProductRDTO.getApplyIntForIrregularRD());// Added by nithya on 18-03-2020 for KD-1535
        setChkSpecialRD(objDepositsProductRDTO.getSpecialRD());// Added by nithya on 18-03-2020 for KD-1535
        setTxtSpecialRDInstallments(CommonUtil.convertObjToStr(objDepositsProductRDTO.getNoOfSpecialRDInstallments())); // Added by nithya on 01-04-2020 for KD-1535
    }
    
    public void resetDelayed(){
        setTxtFromAmt("");
        setTxtToAmt("");
        setTxtDelayedChargesAmt("");
        ttNotifyObservers();
    }
    
    private void depSubNoCheckValueInsert(){
        depSubNoCheckValues = new HashMap();
        depSubNoCheckValues.put("FROM_AMT", getTxtFromAmt());
        depSubNoCheckValues.put("TO_AMT", getTxtToAmt());
        depSubNoCheckValues.put("CHARGE_AMT", getTxtDelayedChargesAmt());
    }
    
    private int checkModeDelayed(int depSubNoMode){
        if(depSubNoAll == null){
            depSubNoAll= new HashMap();
        }
        int j=CANCEL;
        COptionPane depSubNoDuplicationCheck;
        HashMap depCalc;
        depSubNoCheckValueInsert();
        
        return j;
    }
    
    public void delayedInstallment(){
        try{
            if(depSubNoMode == 0){
                //--- Enters if a New Record is to be saved
                depSubNoK = checkModeDelayed(depSubNoMode);
                if(depSubNoK == CANCEL){
                    newModeDelayed();
                }
            } else if(depSubNoMode == 1){
                //--- Enters if Modification of existing record is taking place
                depSubNoK = checkModeDelayed(depSubNoMode);
                if(depSubNoK==CANCEL){
                    editModePopulateDepSubNoTbl();
                }
            }
        } catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void editModePopulateDepSubNoTbl(){
        depSubNoRow = tblDelayedInstallmet.getDataArrayList();
        ArrayList changedDepSubNoRow = new ArrayList();
        ModifyDepSubNo = depSubNoRowDel + 1;
        changedDepSubNoRow.add(0, getTxtFromAmt());
        changedDepSubNoRow.add(1, getTxtToAmt());
        changedDepSubNoRow.add(2, getTxtDelayedChargesAmt());
        depSubNoRow.set(getSelectedRowCount, changedDepSubNoRow);
        tblDelayedInstallmet.setDataArrayList(depSubNoRow, tblDepSubNoColTitle);
        depSubNoAll.remove(String.valueOf(depSubNoRowDel));
        depSubNoRec = new HashMap();
        insertDepSubNoRecord();
        depSubNoAll.put(String.valueOf(depSubNoRowDel), depSubNoRec);
        depSubNoRow = null;
        depSubNoRec = null;
        changedDepSubNoRow = null;
    }
    
    public void  populateDepSubNoFields(int row){
        try{
            HashMap listDepSubNoAll =  (HashMap)depSubNoAll.get(String.valueOf(row));
            depSubNoMode=1; //Set the mode for "Modification" of record
            depSubNoRowDel = row;
            setTxtFromAmt(CommonUtil.convertObjToStr(listDepSubNoAll.get("FROM_AMT")));
            setTxtToAmt(CommonUtil.convertObjToStr(CommonUtil.convertObjToStr(listDepSubNoAll.get("TO_AMT"))));
            setTxtDelayedChargesAmt(CommonUtil.convertObjToStr(listDepSubNoAll.get("CHARGE_AMT")));
            notifyObservers();
            listDepSubNoAll = null;
        }catch(Exception e){
            parseException.logException(e,true);
        }
    }
    
    private void newModeDelayed(){
        if(depSubNoAll == null){
            depSubNoAll = new HashMap();
        }
        depSubNo=depSubNoCount+1;
        depSubNoRow = new ArrayList();
        depSubNoRow.add(0, getTxtFromAmt());
        depSubNoRow.add(1, getTxtToAmt());
        depSubNoRow.add(2, getTxtDelayedChargesAmt());
        tblDelayedInstallmet.insertRow(tblDelayedInstallmet.getRowCount(),depSubNoRow);
        depSubNoRec = new HashMap();
        insertDepSubNoRecord();
        depSubNoAll.put(String.valueOf(depSubNo-1), depSubNoRec);
        depSubNoRec = null;
        depSubNoRow = null;
        depSubNoCount=depSubNoCount+1;
    }
    
    private void insertDepSubNoRecord(){
        //        if(depSubNoMode == 0){
        //            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(depSubNo));
        //        }else if(depSubNoMode == 1){
        //            depSubNoRec.put(FLD_DEP_SUB_NO_DEP_SUB_NO, String.valueOf(ModifyDepSubNo));
        //        }
        depSubNoRec.put("FROM_AMT" , getTxtFromAmt());
        depSubNoRec.put("TO_AMT", getTxtToAmt());
        depSubNoRec.put("CHARGE_AMT", getTxtDelayedChargesAmt());
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
    
    
    public String getLblStatus(){
        return this.lblStatus;
    }
    
    public void setLblStatus(String lblStatus) {
        this.lblStatus = lblStatus;
        setChanged();
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    public void setActionType(int actionType) {
        this.actionType = actionType;
        setChanged();
    }
    
    public java.lang.String getCboEitherofTwoRatesChoose() {
        return cboEitherofTwoRatesChoose;
    }
    
    /**
     * Setter for property cboEitherofTwoRatesChoose.
     * @param cboEitherofTwoRatesChoose New value of property cboEitherofTwoRatesChoose.
     */
    public void setCboEitherofTwoRatesChoose(java.lang.String cboEitherofTwoRatesChoose) {
        this.cboEitherofTwoRatesChoose = cboEitherofTwoRatesChoose;
        setChanged();
    }
    public int getResult(){
        return result;
    }
    
    public void setResult(int result) {
        this.result = result;
        setChanged();
    }
    
    
    void setTxtAcctHd(String txtAcctHd){
        this.txtAcctHd = txtAcctHd;
        setChanged();
    }
    String getTxtAcctHd(){
        return this.txtAcctHd;
    }
    
    void setTxtProductID(String txtProductID){
        this.txtProductID = txtProductID;
        setChanged();
    }
    String getTxtProductID(){
        return this.txtProductID;
    }
    
    void setTxtDesc(String txtDesc){
        this.txtDesc = txtDesc;
        setChanged();
    }
    String getTxtDesc(){
        return this.txtDesc;
    }
    
    void setCboOperatesLike(String cboOperatesLike){
        this.cboOperatesLike = cboOperatesLike;
        setChanged();
    }
    String getCboOperatesLike(){
        return this.cboOperatesLike;
    }
    
    void setCboProdCurrency(String cboProdCurrency){
        this.cboProdCurrency = cboProdCurrency;
        setChanged();
    }
    
    String getCboProdCurrency(){
        return this.cboProdCurrency;
    }
    public boolean getRdoExtensionPenal_Yes() {
        return rdoExtensionPenal_Yes;
    }
    
    /**
     * Setter for property rdoExtensionPenal_Yes.
     * @param rdoExtensionPenal_Yes New value of property rdoExtensionPenal_Yes.
     */
    public void setRdoExtensionPenal_Yes(boolean rdoExtensionPenal_Yes) {
        this.rdoExtensionPenal_Yes = rdoExtensionPenal_Yes;
    }
    
    /**
     * Getter for property rdoExtensionPenal_No.
     * @return Value of property rdoExtensionPenal_No.
     */
    public boolean getRdoExtensionPenal_No() {
        return rdoExtensionPenal_No;
    }
    
    /**
     * Setter for property rdoExtensionPenal_No.
     * @param rdoExtensionPenal_No New value of property rdoExtensionPenal_No.
     */
    public void setRdoExtensionPenal_No(boolean rdoExtensionPenal_No) {
        this.rdoExtensionPenal_No = rdoExtensionPenal_No;
    }
    
    
    //
    void setCbmOperatesLike(ComboBoxModel cbmOperatesLike){
        this.cbmOperatesLike = cbmOperatesLike;
        setChanged();
    }
    ComboBoxModel getCbmOperatesLike(){
        return this.cbmOperatesLike;
    }
    
    void setCbmInstType(ComboBoxModel cbmInstType){
        this.cbmInstType = cbmInstType;
    }
    
    ComboBoxModel getCbmInstType(){
        return this.cbmInstType;
    }
    
    void setCbmProdCurrency(ComboBoxModel cbmProdCurrency){
        this.cbmProdCurrency = cbmProdCurrency;
        setChanged();
    }
    
    ComboBoxModel getCbmProdCurrency(){
        return this.cbmProdCurrency;
    }
    //
    ComboBoxModel getCbmMaxPDt(){
        return this.cbmMaxPDt;
    }
    
    ComboBoxModel getCbmMaxMDt(){
        return this.cbmMaxMDt;
    }
    
    void setRdoCalcTDS_Yes(boolean rdoCalcTDS_Yes){
        this.rdoCalcTDS_Yes = rdoCalcTDS_Yes;
        setChanged();
    }
    boolean getRdoCalcTDS_Yes(){
        return this.rdoCalcTDS_Yes;
    }
    
    void setRdoCalcTDS_No(boolean rdoCalcTDS_No){
        this.rdoCalcTDS_No = rdoCalcTDS_No;
        setChanged();
    }
    boolean getRdoCalcTDS_No(){
        return this.rdoCalcTDS_No;
    }
    
        /*void setRdoCalcTDS(boolean rdoCalcTDS){
                this.rdoCalcTDS = rdoCalcTDS;
                setChanged();
        }
        boolean getRdoCalcTDS(){
                return this.rdoCalcTDS;
        }*/
    
    
    public boolean getRdoSBRateOneRate_Yes() {
        return rdoSBRateOneRate_Yes;
    }
    
    /**
     * Setter for property rdoSBRateOneRate_Yes.
     * @param rdoSBRateOneRate_Yes New value of property rdoSBRateOneRate_Yes.
     */
    public void setRdoSBRateOneRate_Yes(boolean rdoSBRateOneRate_Yes) {
        this.rdoSBRateOneRate_Yes = rdoSBRateOneRate_Yes;
    }
    
    public boolean getRdoSBRateOneRate_No() {
        return rdoSBRateOneRate_No;
    }
    
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmEitherofTwoRatesChoose() {
        return cbmEitherofTwoRatesChoose;
    }
    
    /**
     * Setter for property cbmEitherofTwoRatesChoose.
     * @param cbmEitherofTwoRatesChoose New value of property cbmEitherofTwoRatesChoose.
     */
    public void setCbmEitherofTwoRatesChoose(com.see.truetransact.clientutil.ComboBoxModel cbmEitherofTwoRatesChoose) {
        this.cbmEitherofTwoRatesChoose = cbmEitherofTwoRatesChoose;
    }
    /**
     * Setter for property rdoSBRateOneRate_No.
     * @param rdoSBRateOneRate_No New value of property rdoSBRateOneRate_No.
     */
    public void setRdoSBRateOneRate_No(boolean rdoSBRateOneRate_No) {
        this.rdoSBRateOneRate_No = rdoSBRateOneRate_No;
    }
    void setRdoPayInterestOnHoliday_Yes(boolean rdoPayInterestOnHoliday_Yes){
        this.rdoPayInterestOnHoliday_Yes = rdoPayInterestOnHoliday_Yes;
        setChanged();
    }
    boolean getRdoPayInterestOnHoliday_Yes(){
        return this.rdoPayInterestOnHoliday_Yes;
    }
    
    void setRdoPayInterestOnHoliday_No(boolean rdoPayInterestOnHoliday_No){
        this.rdoPayInterestOnHoliday_No = rdoPayInterestOnHoliday_No;
        setChanged();
    }
    boolean getRdoPayInterestOnHoliday_No(){
        return this.rdoPayInterestOnHoliday_No;
    }
    
    void setRdoAmountRounded_Yes(boolean rdoAmountRounded_Yes){
        this.rdoAmountRounded_Yes = rdoAmountRounded_Yes;
        setChanged();
    }
    boolean getRdoAmountRounded_Yes(){
        return this.rdoAmountRounded_Yes;
    }
    
    void setRdoAmountRounded_No(boolean rdoAmountRounded_No){
        this.rdoAmountRounded_No = rdoAmountRounded_No;
        setChanged();
    }
    boolean getRdoAmountRounded_No(){
        return this.rdoAmountRounded_No;
    }
    
    void setCboRoundOffCriteria(String cboRoundOffCriteria){
        this.cboRoundOffCriteria = cboRoundOffCriteria;
        setChanged();
    }
    String getCboRoundOffCriteria(){
        return this.cboRoundOffCriteria;
    }
    
    void setCbmRoundOffCriteria(ComboBoxModel cbmRoundOffCriteria){
        this.cbmRoundOffCriteria = cbmRoundOffCriteria;
        setChanged();
    }
    ComboBoxModel getCbmRoundOffCriteria(){
        return this.cbmRoundOffCriteria;
    }
    
    void setRdoInterestAfterMaturity_Yes(boolean rdoInterestAfterMaturity_Yes){
        this.rdoInterestAfterMaturity_Yes = rdoInterestAfterMaturity_Yes;
        setChanged();
    }
    boolean getRdoInterestAfterMaturity_Yes(){
        return this.rdoInterestAfterMaturity_Yes;
    }
    
    void setRdoInterestAfterMaturity_No(boolean rdoInterestAfterMaturity_No){
        this.rdoInterestAfterMaturity_No = rdoInterestAfterMaturity_No;
        setChanged();
    }
    boolean getRdoInterestAfterMaturity_No(){
        return this.rdoInterestAfterMaturity_No;
    }
    
    void setCboMaturityInterestType(String cboMaturityInterestType){
        this.cboMaturityInterestType = cboMaturityInterestType;
        setChanged();
    }
    String getCboMaturityInterestType(){
        return this.cboMaturityInterestType;
    }
    
    void setCbmMaturityInterestType(ComboBoxModel cbmMaturityInterestType){
        this.cbmMaturityInterestType = cbmMaturityInterestType;
        setChanged();
    }
    ComboBoxModel getCbmMaturityInterestType(){
        return this.cbmMaturityInterestType;
    }
    
    void setCboMaturityInterestRate(String cboMaturityInterestRate){
        this.cboMaturityInterestRate = cboMaturityInterestRate;
        setChanged();
    }
    String getCboMaturityInterestRate(){
        return this.cboMaturityInterestRate;
    }
    
    void setCbmMaturityInterestRate(ComboBoxModel cbmMaturityInterestRate){
        this.cbmMaturityInterestRate = cbmMaturityInterestRate;
        setChanged();
    }
    ComboBoxModel getCbmMaturityInterestRate(){
        return this.cbmMaturityInterestRate;
    }
    
    void setCboInterestCriteria(String cboInterestCriteria){
        this.cboInterestCriteria = cboInterestCriteria;
        setChanged();
    }
    String getCboInterestCriteria(){
        return this.cboInterestCriteria;
    }
    
    void setCbmInterestCriteria(ComboBoxModel cbmInterestCriteria){
        this.cbmInterestCriteria = cbmInterestCriteria;
        setChanged();
    }
    ComboBoxModel getCbmInterestCriteria(){
        return this.cbmInterestCriteria;
    }
    
    public boolean getChkRateAsOnDateOfRenewal() {
        return chkRateAsOnDateOfRenewal;
    }
    
    /**
     * Setter for property chkRateAsOnDateOfRenewal.
     * @param chkRateAsOnDateOfRenewal New value of property chkRateAsOnDateOfRenewal.
     */
    public void setChkRateAsOnDateOfRenewal(boolean chkRateAsOnDateOfRenewal) {
        this.chkRateAsOnDateOfRenewal = chkRateAsOnDateOfRenewal;
    }
    
    
    public boolean getChkRateAsOnDateOfMaturity() {
        return chkRateAsOnDateOfMaturity;
    }
    
    /**
     * Setter for property chkRateAsOnDateOfMaturity.
     * @param chkRateAsOnDateOfMaturity New value of property chkRateAsOnDateOfMaturity.
     */
    public void setChkRateAsOnDateOfMaturity(boolean chkRateAsOnDateOfMaturity) {
        this.chkRateAsOnDateOfMaturity = chkRateAsOnDateOfMaturity;
    }
    void setTxtPeriodInMultiplesOf(String txtPeriodInMultiplesOf){
        this.txtPeriodInMultiplesOf = txtPeriodInMultiplesOf;
        setChanged();
    }
    String getTxtPeriodInMultiplesOf(){
        return this.txtPeriodInMultiplesOf;
    }
    
    void setCboPeriodInMultiplesOf(String cboPeriodInMultiplesOf){
        this.cboPeriodInMultiplesOf = cboPeriodInMultiplesOf;
        setChanged();
    }
    String getCboPeriodInMultiplesOf(){
        return this.cboPeriodInMultiplesOf;
    }
    
    void setCbmPeriodInMultiplesOf(ComboBoxModel cbmPeriodInMultiplesOf){
        this.cbmPeriodInMultiplesOf = cbmPeriodInMultiplesOf;
        setChanged();
    }
    ComboBoxModel getCbmPeriodInMultiplesOf(){
        return this.cbmPeriodInMultiplesOf;
    }
    
    void setTxtMaxDepositPeriod(String txtMaxDepositPeriod){
        this.txtMaxDepositPeriod = txtMaxDepositPeriod;
        setChanged();
    }
    String getTxtMaxDepositPeriod(){
        return this.txtMaxDepositPeriod;
    }
    
    void setCboMaxDepositPeriod(String cboMaxDepositPeriod){
        this.cboMaxDepositPeriod = cboMaxDepositPeriod;
        setChanged();
    }
    String getCboMaxDepositPeriod(){
        return this.cboMaxDepositPeriod;
    }
    
    void setCbmMaxDepositPeriod(ComboBoxModel cbmMaxDepositPeriod){
        this.cbmMaxDepositPeriod = cbmMaxDepositPeriod;
        setChanged();
    }
    ComboBoxModel getCbmMaxDepositPeriod(){
        return this.cbmMaxDepositPeriod;
    }
    
    void setTxtMinDepositPeriod(String txtMinDepositPeriod){
        this.txtMinDepositPeriod = txtMinDepositPeriod;
        setChanged();
    }
    String getTxtMinDepositPeriod(){
        return this.txtMinDepositPeriod;
    }
    
    void setCboMinDepositPeriod(String cboMinDepositPeriod){
        this.cboMinDepositPeriod = cboMinDepositPeriod;
        setChanged();
    }
    String getCboMinDepositPeriod(){
        return this.cboMinDepositPeriod;
    }
    
    void setCbmMinDepositPeriod(ComboBoxModel cbmMinDepositPeriod){
        this.cbmMinDepositPeriod = cbmMinDepositPeriod;
        setChanged();
    }
    ComboBoxModel getCbmMinDepositPeriod(){
        return this.cbmMinDepositPeriod;
    }
    
    void setTxtDepositPerUnit(String txtDepositPerUnit){
        this.txtDepositPerUnit = txtDepositPerUnit;
        setChanged();
    }
    String getTxtDepositPerUnit(){
        return this.txtDepositPerUnit;
    }
    
    public boolean getRdoInterestalreadyPaid_Yes() {
        return rdoInterestalreadyPaid_Yes;
    }
    
    /**
     * Setter for property rdoInterestalreadyPaid_Yes.
     * @param rdoInterestalreadyPaid_Yes New value of property rdoInterestalreadyPaid_Yes.
     */
    public void setRdoInterestalreadyPaid_Yes(boolean rdoInterestalreadyPaid_Yes) {
        this.rdoInterestalreadyPaid_Yes = rdoInterestalreadyPaid_Yes;
    }
    
    /**
     * Getter for property rdoInterestalreadyPaid_No.
     * @return Value of property rdoInterestalreadyPaid_No.
     */
    public boolean getRdoInterestalreadyPaid_No() {
        return rdoInterestalreadyPaid_No;
    }
    
    /**
     * Setter for property rdoInterestalreadyPaid_No.
     * @param rdoInterestalreadyPaid_No New value of property rdoInterestalreadyPaid_No.
     */
    public void setRdoInterestalreadyPaid_No(boolean rdoInterestalreadyPaid_No) {
        this.rdoInterestalreadyPaid_No = rdoInterestalreadyPaid_No;
    }
    
    
    public boolean getRdoInterestrateappliedoverdue_Yes() {
        return rdoInterestrateappliedoverdue_Yes;
    }
    
    /**
     * Setter for property rdoInterestrateappliedoverdue_Yes.
     * @param rdoInterestrateappliedoverdue_Yes New value of property rdoInterestrateappliedoverdue_Yes.
     */
    public void setRdoInterestrateappliedoverdue_Yes(boolean rdoInterestrateappliedoverdue_Yes) {
        this.rdoInterestrateappliedoverdue_Yes = rdoInterestrateappliedoverdue_Yes;
    }
    
    /**
     * Getter for property rdoInterestrateappliedoverdue_No.
     * @return Value of property rdoInterestrateappliedoverdue_No.
     */
    public boolean getRdoInterestrateappliedoverdue_No() {
        return rdoInterestrateappliedoverdue_No;
    }
    
    /**
     * Setter for property rdoInterestrateappliedoverdue_No.
     * @param rdoInterestrateappliedoverdue_No New value of property rdoInterestrateappliedoverdue_No.
     */
    public void setRdoInterestrateappliedoverdue_No(boolean rdoInterestrateappliedoverdue_No) {
        this.rdoInterestrateappliedoverdue_No = rdoInterestrateappliedoverdue_No;
    }
    
    //    void setCboDepositPerUnit(String cboDepositPerUnit){
    //        this.cboDepositPerUnit = cboDepositPerUnit;
    //        setChanged();
    //    }
    //    String getCboDepositPerUnit(){
    //        return this.cboDepositPerUnit;
    //    }
    //
    //    void setCbmDepositPerUnit(ComboBoxModel cbmDepositPerUnit){
    //        this.cbmDepositPerUnit = cbmDepositPerUnit;
    //        setChanged();
    //    }
    //    ComboBoxModel getCbmDepositPerUnit(){
    //        return this.cbmDepositPerUnit;
    //    }
    public boolean getRdoBothRateNotAvail_Yes() {
        return rdoBothRateNotAvail_Yes;
    }
    
    /**
     * Setter for property lblBothRateNotAvail_Yes.
     * @param lblBothRateNotAvail_Yes New value of property lblBothRateNotAvail_Yes.
     */
    public void setRdoBothRateNotAvail_Yes(boolean rdoBothRateNotAvail_Yes) {
        this.rdoBothRateNotAvail_Yes = rdoBothRateNotAvail_Yes;
    }
    
    /**
     * Getter for property lblBothRateNotAvail_No.
     * @return Value of property lblBothRateNotAvail_No.
     */
    
    public void setRdoBothRateNotAvail_No(boolean rdoBothRateNotAvail_No) {
        this.rdoBothRateNotAvail_No = rdoBothRateNotAvail_No;
    }
    public boolean getRdoBothRateNotAvail_No() {
        return rdoBothRateNotAvail_No;
    }
    void setTxtAmtInMultiplesOf(String txtAmtInMultiplesOf){
        this.txtAmtInMultiplesOf = txtAmtInMultiplesOf;
        setChanged();
    }
    String getTxtAmtInMultiplesOf(){
        return this.txtAmtInMultiplesOf;
    }
    
    void setTxtMinDepositAmt(String txtMinDepositAmt){
        this.txtMinDepositAmt = txtMinDepositAmt;
        setChanged();
    }
    String getTxtMinDepositAmt(){
        return this.txtMinDepositAmt;
    }
    
    void setTxtMaxDepositAmt(String txtMaxDepositAmt){
        this.txtMaxDepositAmt = txtMaxDepositAmt;
        setChanged();
    }
    String getTxtMaxDepositAmt(){
        return this.txtMaxDepositAmt;
    }
    
    void setRdoCalcMaturityValue_Yes(boolean rdoCalcMaturityValue_Yes){
        this.rdoCalcMaturityValue_Yes = rdoCalcMaturityValue_Yes;
        setChanged();
    }
    boolean getRdoCalcMaturityValue_Yes(){
        return this.rdoCalcMaturityValue_Yes;
    }
    
    void setRdoCalcMaturityValue_No(boolean rdoCalcMaturityValue_No){
        this.rdoCalcMaturityValue_No = rdoCalcMaturityValue_No;
        setChanged();
    }
    boolean getRdoCalcMaturityValue_No(){
        return this.rdoCalcMaturityValue_No;
    }
    
    void setRdoProvisionOfInterest_Yes(boolean rdoProvisionOfInterest_Yes){
        this.rdoProvisionOfInterest_Yes = rdoProvisionOfInterest_Yes;
        setChanged();
    }
    boolean getRdoProvisionOfInterest_Yes(){
        return this.rdoProvisionOfInterest_Yes;
    }
    
    void setRdoProvisionOfInterest_No(boolean rdoProvisionOfInterest_No){
        this.rdoProvisionOfInterest_No = rdoProvisionOfInterest_No;
        setChanged();
    }
    boolean getRdoProvisionOfInterest_No(){
        return this.rdoProvisionOfInterest_No;
    }
    
    void setTxtInterestOnMaturedDeposits(String txtInterestOnMaturedDeposits){
        this.txtInterestOnMaturedDeposits = txtInterestOnMaturedDeposits;
        setChanged();
    }
    String getTxtInterestOnMaturedDeposits(){
        return this.txtInterestOnMaturedDeposits;
    }
    
    void setTxtAlphaSuffix(String txtAlphaSuffix){
        this.txtAlphaSuffix = txtAlphaSuffix;
        setChanged();
    }
    String getTxtAlphaSuffix(){
        return this.txtAlphaSuffix;
    }
    
    void setTxtMaxAmtOfCashPayment(String txtMaxAmtOfCashPayment){
        this.txtMaxAmtOfCashPayment = txtMaxAmtOfCashPayment;
        setChanged();
    }
    String getTxtMaxAmtOfCashPayment(){
        return this.txtMaxAmtOfCashPayment;
    }
    
    void setTxtMinAmtOfPAN(String txtMinAmtOfPAN){
        this.txtMinAmtOfPAN = txtMinAmtOfPAN;
        setChanged();
    }
    String getTxtMinAmtOfPAN(){
        return this.txtMinAmtOfPAN;
    }
    
    void setTxtAcctNumberPattern(String txtAcctNumberPattern){
        this.txtAcctNumberPattern = txtAcctNumberPattern;
        setChanged();
    }
    String getTxtAcctNumberPattern(){
        return this.txtAcctNumberPattern;
    }
    
    void setTdtSchemeIntroDate(String tdtSchemeIntroDate){
        this.tdtSchemeIntroDate = tdtSchemeIntroDate;
        setChanged();
    }
    String getTdtSchemeIntroDate(){
        return this.tdtSchemeIntroDate;
    }
    
    //    void setTxtScheme(String txtScheme){
    //        this.txtScheme = txtScheme;
    //        setChanged();
    //    }
    //    String getTxtScheme(){
    //        return this.txtScheme;
    //    }
    
    void setTxtLimitForBulkDeposit(String txtLimitForBulkDeposit){
        this.txtLimitForBulkDeposit = txtLimitForBulkDeposit;
        setChanged();
    }
    String getTxtLimitForBulkDeposit(){
        return this.txtLimitForBulkDeposit;
    }
    
    void setRdoTransferToMaturedDeposits_Yes(boolean rdoTransferToMaturedDeposits_Yes){
        this.rdoTransferToMaturedDeposits_Yes = rdoTransferToMaturedDeposits_Yes;
        setChanged();
    }
    boolean getRdoTransferToMaturedDeposits_Yes(){
        return this.rdoTransferToMaturedDeposits_Yes;
    }
    
    void setRdoTransferToMaturedDeposits_No(boolean rdoTransferToMaturedDeposits_No){
        this.rdoTransferToMaturedDeposits_No = rdoTransferToMaturedDeposits_No;
        setChanged();
    }
    boolean getRdoTransferToMaturedDeposits_No(){
        return this.rdoTransferToMaturedDeposits_No;
    }
    
    void setRdoAutoAdjustment_Yes(boolean rdoAutoAdjustment_Yes){
        this.rdoAutoAdjustment_Yes = rdoAutoAdjustment_Yes;
        setChanged();
    }
    boolean getRdoAutoAdjustment_Yes(){
        return this.rdoAutoAdjustment_Yes;
    }
    
    void setRdoAutoAdjustment_No(boolean rdoAutoAdjustment_No){
        this.rdoAutoAdjustment_No = rdoAutoAdjustment_No;
        setChanged();
    }
    boolean getRdoAutoAdjustment_No(){
        return this.rdoAutoAdjustment_No;
    }
    
    void setRdoAdjustIntOnDeposits_No(boolean rdoAdjustIntOnDeposits_No){
        this.rdoAdjustIntOnDeposits_No = rdoAdjustIntOnDeposits_No;
        setChanged();
    }
    boolean getRdoAdjustIntOnDeposits_No(){
        return this.rdoAdjustIntOnDeposits_No;
    }
    
    void setRdoAdjustIntOnDeposits_Yes(boolean rdoAdjustIntOnDeposits_Yes){
        this.rdoAdjustIntOnDeposits_Yes = rdoAdjustIntOnDeposits_Yes;
        setChanged();
    }
    boolean getRdoAdjustIntOnDeposits_Yes(){
        return this.rdoAdjustIntOnDeposits_Yes;
    }
    
    void setRdoAdjustPrincipleToLoan_Yes(boolean rdoAdjustPrincipleToLoan_Yes){
        this.rdoAdjustPrincipleToLoan_Yes = rdoAdjustPrincipleToLoan_Yes;
        setChanged();
    }
    boolean getRdoAdjustPrincipleToLoan_Yes(){
        return this.rdoAdjustPrincipleToLoan_Yes;
    }
    
    void setRdoAdjustPrincipleToLoan_No(boolean rdoAdjustPrincipleToLoan_No){
        this.rdoAdjustPrincipleToLoan_No = rdoAdjustPrincipleToLoan_No;
        setChanged();
    }
    boolean getRdoAdjustPrincipleToLoan_No(){
        return this.rdoAdjustPrincipleToLoan_No;
    }
    
    void setRdoExtnOfDepositBeforeMaturity_Yes(boolean rdoExtnOfDepositBeforeMaturity_Yes){
        this.rdoExtnOfDepositBeforeMaturity_Yes = rdoExtnOfDepositBeforeMaturity_Yes;
        setChanged();
    }
    boolean getRdoExtnOfDepositBeforeMaturity_Yes(){
        return this.rdoExtnOfDepositBeforeMaturity_Yes;
    }
    
    void setRdoExtnOfDepositBeforeMaturity_No(boolean rdoExtnOfDepositBeforeMaturity_No){
        this.rdoExtnOfDepositBeforeMaturity_No = rdoExtnOfDepositBeforeMaturity_No;
        setChanged();
    }
    boolean getRdoExtnOfDepositBeforeMaturity_No(){
        return this.rdoExtnOfDepositBeforeMaturity_No;
    }
    
    void setTxtAfterHowManyDays(String txtAfterHowManyDays){
        this.txtAfterHowManyDays = txtAfterHowManyDays;
        setChanged();
    }
    String getTxtAfterHowManyDays(){
        return this.txtAfterHowManyDays;
    }
    
    void setTxtAdvanceMaturityNoticeGenPeriod(String txtAdvanceMaturityNoticeGenPeriod){
        this.txtAdvanceMaturityNoticeGenPeriod = txtAdvanceMaturityNoticeGenPeriod;
        setChanged();
    }
    String getTxtAdvanceMaturityNoticeGenPeriod(){
        return this.txtAdvanceMaturityNoticeGenPeriod;
    }
    
    void setTxtPrematureWithdrawal(String txtPrematureWithdrawal){
        this.txtPrematureWithdrawal = txtPrematureWithdrawal;
        setChanged();
    }
    String getTxtPrematureWithdrawal(){
        return this.txtPrematureWithdrawal;
    }
    
    void setRdoFlexiFromSBCA_Yes(boolean rdoFlexiFromSBCA_Yes){
        this.rdoFlexiFromSBCA_Yes = rdoFlexiFromSBCA_Yes;
        setChanged();
    }
    boolean getRdoFlexiFromSBCA_Yes(){
        return this.rdoFlexiFromSBCA_Yes;
    }
    
    void setRdoFlexiFromSBCA_No(boolean rdoFlexiFromSBCA_No){
        this.rdoFlexiFromSBCA_No = rdoFlexiFromSBCA_No;
        setChanged();
    }
    boolean getRdoFlexiFromSBCA_No(){
        return this.rdoFlexiFromSBCA_No;
    }
    
    void setRdoTermDeposit_Yes(boolean rdoTermDeposit_Yes){
        this.rdoTermDeposit_Yes = rdoTermDeposit_Yes;
        setChanged();
    }
    boolean getRdoTermDeposit_Yes(){
        return this.rdoTermDeposit_Yes;
    }
    
    void setRdoTermDeposit_No(boolean rdoTermDeposit_No){
        this.rdoTermDeposit_No = rdoTermDeposit_No;
        setChanged();
    }
    boolean getRdoTermDeposit_No(){
        return this.rdoTermDeposit_No;
    }
    
    void setRdoIntroducerReqd_Yes(boolean rdoIntroducerReqd_Yes){
        this.rdoIntroducerReqd_Yes = rdoIntroducerReqd_Yes;
        setChanged();
    }
    boolean getRdoIntroducerReqd_Yes(){
        return this.rdoIntroducerReqd_Yes;
    }
    
    void setRdoIntroducerReqd_No(boolean rdoIntroducerReqd_No){
        this.rdoIntroducerReqd_No = rdoIntroducerReqd_No;
        setChanged();
    }
    boolean getRdoIntroducerReqd_No(){
        return this.rdoIntroducerReqd_No;
    }
    
    
    void setTxtNoOfPartialWithdrawalAllowed(String txtNoOfPartialWithdrawalAllowed){
        this.txtNoOfPartialWithdrawalAllowed = txtNoOfPartialWithdrawalAllowed;
        setChanged();
    }
    String getTxtNoOfPartialWithdrawalAllowed(){
        return this.txtNoOfPartialWithdrawalAllowed;
    }
    
    void setTxtMaxNoOfPartialWithdrawalAllowed(String txtMaxNoOfPartialWithdrawalAllowed){
        this.txtMaxNoOfPartialWithdrawalAllowed = txtMaxNoOfPartialWithdrawalAllowed;
        setChanged();
    }
    String getTxtMaxNoOfPartialWithdrawalAllowed(){
        return this.txtMaxNoOfPartialWithdrawalAllowed;
    }
    
    void setTxtMaxAmtOfPartialWithdrawalAllowed(String txtMaxAmtOfPartialWithdrawalAllowed){
        this.txtMaxAmtOfPartialWithdrawalAllowed = txtMaxAmtOfPartialWithdrawalAllowed;
        setChanged();
    }
    String getTxtMaxAmtOfPartialWithdrawalAllowed(){
        return this.txtMaxAmtOfPartialWithdrawalAllowed;
    }
    
    void setTxtMinAmtOfPartialWithdrawalAllowed(String txtMinAmtOfPartialWithdrawalAllowed){
        this.txtMinAmtOfPartialWithdrawalAllowed = txtMinAmtOfPartialWithdrawalAllowed;
        setChanged();
    }
    String getTxtMinAmtOfPartialWithdrawalAllowed(){
        return this.txtMinAmtOfPartialWithdrawalAllowed;
    }
    
    void setTxtWithdrawalsInMultiplesOf(String txtWithdrawalsInMultiplesOf){
        this.txtWithdrawalsInMultiplesOf = txtWithdrawalsInMultiplesOf;
        setChanged();
    }
    String getTxtWithdrawalsInMultiplesOf(){
        return this.txtWithdrawalsInMultiplesOf;
    }
    
    void setRdoPartialWithdrawalAllowed_Yes(boolean rdoPartialWithdrawalAllowed_Yes){
        this.rdoPartialWithdrawalAllowed_Yes = rdoPartialWithdrawalAllowed_Yes;
        setChanged();
    }
    boolean getRdoPartialWithdrawalAllowed_Yes(){
        return this.rdoPartialWithdrawalAllowed_Yes;
    }
    
    void setRdoPartialWithdrawalAllowed_No(boolean rdoPartialWithdrawalAllowed_No){
        this.rdoPartialWithdrawalAllowed_No = rdoPartialWithdrawalAllowed_No;
        setChanged();
    }
    boolean getRdoPartialWithdrawalAllowed_No(){
        return this.rdoPartialWithdrawalAllowed_No;
    }
    
    void setRdoWithdrawalWithInterest_Yes(boolean rdoWithdrawalWithInterest_Yes){
        this.rdoWithdrawalWithInterest_Yes = rdoWithdrawalWithInterest_Yes;
        setChanged();
    }
    boolean getRdoWithdrawalWithInterest_Yes(){
        return this.rdoWithdrawalWithInterest_Yes;
    }
    
    void setRdoWithdrawalWithInterest_No(boolean rdoWithdrawalWithInterest_No){
        this.rdoWithdrawalWithInterest_No = rdoWithdrawalWithInterest_No;
        setChanged();
    }
    boolean getRdoWithdrawalWithInterest_No(){
        return this.rdoWithdrawalWithInterest_No;
    }
    
    //Service Charge
    void setRdoServiceCharge_Yes(boolean rdoServiceCharge_Yes){
        this.rdoServiceCharge_Yes = rdoServiceCharge_Yes;
        setChanged();
    }
    boolean getRdoServiceCharge_Yes(){
        return this.rdoServiceCharge_Yes;
    }
    
    void setRdoServiceCharge_No(boolean rdoServiceCharge_No){
        this.rdoServiceCharge_No = rdoServiceCharge_No;
        setChanged();
    }
    boolean getRdoServiceCharge_No(){
        return this.rdoServiceCharge_No;
    }
    
    void setRdoInsBeyondMaturityDt_Yes(boolean rdoInsBeyondMaturityDt_Yes){
        this.rdoInsBeyondMaturityDt_Yes = rdoInsBeyondMaturityDt_Yes;
        setChanged();
    }
    boolean getRdoInsBeyondMaturityDt_Yes(){
        return this.rdoInsBeyondMaturityDt_Yes;
    }
    
    void setRdoInsBeyondMaturityDt_No(boolean rdoInsBeyondMaturityDt_No){
        this.rdoInsBeyondMaturityDt_No = rdoInsBeyondMaturityDt_No;
        setChanged();
    }
    boolean getRdoInsBeyondMaturityDt_No(){
        return this.rdoInsBeyondMaturityDt_No;
    }
    
    void setCboIntCalcMethod(String cboIntCalcMethod){
        this.cboIntCalcMethod = cboIntCalcMethod;
        setChanged();
    }
    String getCboIntCalcMethod(){
        return this.cboIntCalcMethod;
    }
    
    void setCbmIntCalcMethod(ComboBoxModel cbmIntCalcMethod){
        this.cbmIntCalcMethod = cbmIntCalcMethod;
        setChanged();
    }
    ComboBoxModel getCbmIntCalcMethod(){
        return this.cbmIntCalcMethod;
    }
    
    void setCboIntMaintainedAsPartOf(String cboIntMaintainedAsPartOf){
        this.cboIntMaintainedAsPartOf = cboIntMaintainedAsPartOf;
        setChanged();
    }
    String getCboIntMaintainedAsPartOf(){
        return this.cboIntMaintainedAsPartOf;
    }
    
    void setCbmIntMaintainedAsPartOf(ComboBoxModel cbmIntMaintainedAsPartOf){
        this.cbmIntMaintainedAsPartOf = cbmIntMaintainedAsPartOf;
        setChanged();
    }
    ComboBoxModel getCbmIntMaintainedAsPartOf(){
        return this.cbmIntMaintainedAsPartOf;
    }
    
    void setRdoIntProvisioningApplicable_Yes(boolean rdoIntProvisioningApplicable_Yes){
        this.rdoIntProvisioningApplicable_Yes = rdoIntProvisioningApplicable_Yes;
        setChanged();
    }
    boolean getRdoIntProvisioningApplicable_Yes(){
        return this.rdoIntProvisioningApplicable_Yes;
    }
    
    void setRdoIntProvisioningApplicable_No(boolean rdoIntProvisioningApplicable_No){
        this.rdoIntProvisioningApplicable_No = rdoIntProvisioningApplicable_No;
        setChanged();
    }
    boolean getRdoIntProvisioningApplicable_No(){
        return this.rdoIntProvisioningApplicable_No;
    }
    
    void setCboIntProvisioningFreq(String cboIntProvisioningFreq){
        this.cboIntProvisioningFreq = cboIntProvisioningFreq;
        setChanged();
    }
    String getCboIntProvisioningFreq(){
        return this.cboIntProvisioningFreq;
    }
    
    public java.lang.String getCboRenewedclosedbefore() {
        return cboRenewedclosedbefore;
    }
    
    /**
     * Setter for property cboRenewedclosedbefore.
     * @param cboRenewedclosedbefore New value of property cboRenewedclosedbefore.
     */
    public void setCboRenewedclosedbefore(java.lang.String cboRenewedclosedbefore) {
        this.cboRenewedclosedbefore = cboRenewedclosedbefore;
    }
    void setCbmIntProvisioningFreq(ComboBoxModel cbmIntProvisioningFreq){
        this.cbmIntProvisioningFreq = cbmIntProvisioningFreq;
        setChanged();
    }
    ComboBoxModel getCbmIntProvisioningFreq(){
        return this.cbmIntProvisioningFreq;
    }
    
    void setCboProvisioningLevel(String cboProvisioningLevel){
        this.cboProvisioningLevel = cboProvisioningLevel;
        setChanged();
    }
    String getCboProvisioningLevel(){
        return this.cboProvisioningLevel;
    }
    
    void setCbmProvisioningLevel(ComboBoxModel cbmProvisioningLevel){
        this.cbmProvisioningLevel = cbmProvisioningLevel;
        setChanged();
    }
    ComboBoxModel getCbmProvisioningLevel(){
        return this.cbmProvisioningLevel;
    }
    
    void setCboNoOfDays(String cboNoOfDays){
        this.cboNoOfDays = cboNoOfDays;
        setChanged();
    }
    String getCboNoOfDays(){
        return this.cboNoOfDays;
    }
    
    void setCbmNoOfDays(ComboBoxModel cbmNoOfDays){
        this.cbmNoOfDays = cbmNoOfDays;
        setChanged();
    }
    ComboBoxModel getCbmNoOfDays(){
        return this.cbmNoOfDays;
    }
    
    void setCboIntCompoundingFreq(String cboIntCompoundingFreq){
        this.cboIntCompoundingFreq = cboIntCompoundingFreq;
        setChanged();
    }
    String getCboIntCompoundingFreq(){
        return this.cboIntCompoundingFreq;
    }
    
    void setCbmIntCompoundingFreq(ComboBoxModel cbmIntCompoundingFreq){
        this.cbmIntCompoundingFreq = cbmIntCompoundingFreq;
        setChanged();
    }
    ComboBoxModel getCbmIntCompoundingFreq(){
        return this.cbmIntCompoundingFreq;
    }
    
    void setCboIntApplnFreq(String cboIntApplnFreq){
        this.cboIntApplnFreq = cboIntApplnFreq;
        setChanged();
    }
    String getCboIntApplnFreq(){
        return this.cboIntApplnFreq;
    }
    
    void setCbmIntApplnFreq(ComboBoxModel cbmIntApplnFreq){
        this.cbmIntApplnFreq = cbmIntApplnFreq;
        setChanged();
    }
    ComboBoxModel getCbmIntApplnFreq(){
        return this.cbmIntApplnFreq;
    }
    
    void setTdtNextInterestAppliedDate(String tdtNextInterestAppliedDate){
        this.tdtNextInterestAppliedDate = tdtNextInterestAppliedDate;
        setChanged();
    }
    String getTdtNextInterestAppliedDate(){
        return this.tdtNextInterestAppliedDate;
    }
    
    void setTdtLastInterestAppliedDate(String tdtLastInterestAppliedDate){
        this.tdtLastInterestAppliedDate = tdtLastInterestAppliedDate;
        setChanged();
    }
    String getTdtLastInterestAppliedDate(){
        return this.tdtLastInterestAppliedDate;
    }
    
    void setTdtNextIntProvisionalDate(String tdtNextIntProvisionalDate){
        this.tdtNextIntProvisionalDate = tdtNextIntProvisionalDate;
        setChanged();
    }
    String getTdtNextIntProvisionalDate(){
        return this.tdtNextIntProvisionalDate;
    }
    
    void setTdtLastIntProvisionalDate(String tdtLastIntProvisionalDate){
        this.tdtLastIntProvisionalDate = tdtLastIntProvisionalDate;
        setChanged();
    }
    String getTdtLastIntProvisionalDate(){
        return this.tdtLastIntProvisionalDate;
    }
    
    void setCboIntRoundOff(String cboIntRoundOff){
        this.cboIntRoundOff = cboIntRoundOff;
        setChanged();
    }
    String getCboIntRoundOff(){
        return this.cboIntRoundOff;
    }
    
    void setCbmIntRoundOff(ComboBoxModel cbmIntRoundOff){
        this.cbmIntRoundOff = cbmIntRoundOff;
        setChanged();
    }
    
    
    
    
    ComboBoxModel getCbmIntRoundOff(){
        return this.cbmIntRoundOff;
    }
    
    public java.lang.String getCboMinimumRenewalDeposit() {
        return cboMinimumRenewalDeposit;
    }
    
    /**
     * Setter for property cboMinimumRenewalDeposit.
     * @param cboMinimumRenewalDeposit New value of property cboMinimumRenewalDeposit.
     */
    public void setCboMinimumRenewalDeposit(java.lang.String cboMinimumRenewalDeposit) {
        this.cboMinimumRenewalDeposit = cboMinimumRenewalDeposit;
    }
    
    void setTxtMinIntToBePaid(String txtMinIntToBePaid){
        this.txtMinIntToBePaid = txtMinIntToBePaid;
        setChanged();
    }
    String getTxtMinIntToBePaid(){
        return this.txtMinIntToBePaid;
    }
    
    void setRdoRecalcOfMaturityValue_Yes(boolean rdoRecalcOfMaturityValue_Yes){
        this.rdoRecalcOfMaturityValue_Yes = rdoRecalcOfMaturityValue_Yes;
        setChanged();
    }
    boolean getRdoRecalcOfMaturityValue_Yes(){
        return this.rdoRecalcOfMaturityValue_Yes;
    }
    
    
    public java.lang.String getTxtMinimumRenewalDeposit() {
        return txtMinimumRenewalDeposit;
    }
    
    /**
     * Setter for property txtMinimumRenewalDeposit.
     * @param txtMinimumRenewalDeposit New value of property txtMinimumRenewalDeposit.
     */
    public void setTxtMinimumRenewalDeposit(java.lang.String txtMinimumRenewalDeposit) {
        this.txtMinimumRenewalDeposit = txtMinimumRenewalDeposit;
    }
    void setRdoRecalcOfMaturityValue_No(boolean rdoRecalcOfMaturityValue_No){
        this.rdoRecalcOfMaturityValue_No = rdoRecalcOfMaturityValue_No;
        setChanged();
    }
    boolean getRdoRecalcOfMaturityValue_No(){
        return this.rdoRecalcOfMaturityValue_No;
    }
    
    void setTxtIntProvisioningAcctHd(String txtIntProvisioningAcctHd){
        this.txtIntProvisioningAcctHd = txtIntProvisioningAcctHd;
        setChanged();
    }
    String getTxtIntProvisioningAcctHd(){
        return this.txtIntProvisioningAcctHd;
    }
    
    void setTxtIntOnMaturedDepositAcctHead(String txtIntOnMaturedDepositAcctHead){
        this.txtIntOnMaturedDepositAcctHead = txtIntOnMaturedDepositAcctHead;
        setChanged();
    }
    String getTxtIntOnMaturedDepositAcctHead(){
        return this.txtIntOnMaturedDepositAcctHead;
    }
    
    void setTxtFixedDepositAcctHead(String txtFixedDepositAcctHead){
        this.txtFixedDepositAcctHead = txtFixedDepositAcctHead;
        setChanged();
    }
    String getTxtFixedDepositAcctHead(){
        return this.txtFixedDepositAcctHead;
    }
    
    void setTxttMaturityDepositAcctHead(String txttMaturityDepositAcctHead){
        this.txttMaturityDepositAcctHead = txttMaturityDepositAcctHead;
        setChanged();
    }
    
    public boolean getRdoClosedwithinperiod_No() {
        return rdoClosedwithinperiod_No;
    }
    
    /**
     * Setter for property rdoClosedwithinperiod_No.
     * @param rdoClosedwithinperiod_No New value of property rdoClosedwithinperiod_No.
     */
    public void setRdoClosedwithinperiod_No(boolean rdoClosedwithinperiod_No) {
        this.rdoClosedwithinperiod_No = rdoClosedwithinperiod_No;
    }
    public boolean getRdoClosedwithinperiod_Yes() {
        return rdoClosedwithinperiod_Yes;
    }
    
    /**
     * Setter for property rdoClosedwithinperiod_Yes.
     * @param rdoClosedwithinperiod_Yes New value of property rdoClosedwithinperiod_Yes.
     */
    public void setRdoClosedwithinperiod_Yes(boolean rdoClosedwithinperiod_Yes) {
        this.rdoClosedwithinperiod_Yes = rdoClosedwithinperiod_Yes;
    }
    String getTxttMaturityDepositAcctHead(){
        return this.txttMaturityDepositAcctHead;
    }
    
    void setTxtIntProvisionOfMaturedDeposit(String txtIntProvisionOfMaturedDeposit){
        this.txtIntProvisionOfMaturedDeposit = txtIntProvisionOfMaturedDeposit;
        setChanged();
    }
    String getTxtIntProvisionOfMaturedDeposit(){
        return this.txtIntProvisionOfMaturedDeposit;
    }
    
    void setTxtIntPaybleGLHead(String txtIntPaybleGLHead){
        this.txtIntPaybleGLHead = txtIntPaybleGLHead;
        setChanged();
    }
    String getTxtIntPaybleGLHead(){
        return this.txtIntPaybleGLHead;
    }
    
    void setTxtIntDebitPLHead(String txtIntDebitPLHead){
        this.txtIntDebitPLHead = txtIntDebitPLHead;
        setChanged();
    }
    String getTxtIntDebitPLHead(){
        return this.txtIntDebitPLHead;
    }
    
    void setTxtAcctHeadForFloatAcct(String txtAcctHeadForFloatAcct){
        this.txtAcctHeadForFloatAcct = txtAcctHeadForFloatAcct;
        setChanged();
    }
    String getTxtAcctHeadForFloatAcct(){
        return this.txtAcctHeadForFloatAcct;
    }
    
    void setTxtIntPeriodForBackDatedRenewal(String txtIntPeriodForBackDatedRenewal){
        this.txtIntPeriodForBackDatedRenewal = txtIntPeriodForBackDatedRenewal;
        setChanged();
    }
    String getTxtIntPeriodForBackDatedRenewal(){
        return this.txtIntPeriodForBackDatedRenewal;
    }
    
    void setCboIntPeriodForBackDatedRenewal(String cboIntPeriodForBackDatedRenewal){
        this.cboIntPeriodForBackDatedRenewal = cboIntPeriodForBackDatedRenewal;
        setChanged();
    }
    String getCboIntPeriodForBackDatedRenewal(){
        return this.cboIntPeriodForBackDatedRenewal;
    }
    
    void setCboMaxPeriodMDt(String cboMaxPeriodMDt){
        this.cboMaxPeriodMDt = cboMaxPeriodMDt;
        setChanged();
    }
    String getCboMaxPeriodMDt(){
        return this.cboMaxPeriodMDt;
    }
    
    void setCboMinNoOfDays(String cboMinNoOfDays){
        this.cboMinNoOfDays = cboMinNoOfDays;
        setChanged();
    }
    String getCboMinNoOfDays(){
        return this.cboMinNoOfDays;
    }
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmRenewedclosedbefore() {
        return cbmRenewedclosedbefore;
    }
    
    /**
     * Setter for property cbmRenewedclosedbefore.
     * @param cbmRenewedclosedbefore New value of property cbmRenewedclosedbefore.
     */
    public void setCbmRenewedclosedbefore(com.see.truetransact.clientutil.ComboBoxModel cbmRenewedclosedbefore) {
        this.cbmRenewedclosedbefore = cbmRenewedclosedbefore;
    }
    
    void setTxtMaxPeriodMDt(String txtMaxPeriodMDt){
        this.txtMaxPeriodMDt = txtMaxPeriodMDt;
        setChanged();
    }
    String getTxtMaxPeriodMDt(){
        return this.txtMaxPeriodMDt;
    }
    
    public boolean getRdoExtensionBeyondOriginalDate_Yes() {
        return rdoExtensionBeyondOriginalDate_Yes;
    }
    
    /**
     * Setter for property rdoExtensionBeyondOriginalDate_Yes.
     * @param rdoExtensionBeyondOriginalDate_Yes New value of property rdoExtensionBeyondOriginalDate_Yes.
     */
    public void setRdoExtensionBeyondOriginalDate_Yes(boolean rdoExtensionBeyondOriginalDate_Yes) {
        this.rdoExtensionBeyondOriginalDate_Yes = rdoExtensionBeyondOriginalDate_Yes;
    }
    
    /**
     * Getter for property rdoExtensionBeyondOriginalDate_No.
     * @return Value of property rdoExtensionBeyondOriginalDate_No.
     */
    public boolean getRdoExtensionBeyondOriginalDate_No() {
        return rdoExtensionBeyondOriginalDate_No;
    }
    
    /**
     * Setter for property rdoExtensionBeyondOriginalDate_No.
     * @param rdoExtensionBeyondOriginalDate_No New value of property rdoExtensionBeyondOriginalDate_No.
     */
    public void setRdoExtensionBeyondOriginalDate_No(boolean rdoExtensionBeyondOriginalDate_No) {
        this.rdoExtensionBeyondOriginalDate_No = rdoExtensionBeyondOriginalDate_No;
    }
    
    void setTxtMaxPeriodPDt(String txtMaxPeriodPDt){
        this.txtMaxPeriodPDt = txtMaxPeriodPDt;
        setChanged();
    }
    String getTxtMaxPeriodPDt(){
        return this.txtMaxPeriodPDt;
    }
    
    void setCbmIntPeriodForBackDatedRenewal(ComboBoxModel cbmIntPeriodForBackDatedRenewal){
        this.cbmIntPeriodForBackDatedRenewal = cbmIntPeriodForBackDatedRenewal;
        setChanged();
    }
    ComboBoxModel getCbmIntPeriodForBackDatedRenewal(){
        return this.cbmIntPeriodForBackDatedRenewal;
    }
    
    void setCboIntCriteria(String cboIntCriteria){
        this.cboIntCriteria = cboIntCriteria;
        setChanged();
    }
    String getCboIntCriteria(){
        return this.cboIntCriteria;
    }
    
    void setCboIntCriteria(ComboBoxModel cbmIntCriteria){
        this.cbmIntCriteria = cbmIntCriteria;
        setChanged();
    }
    ComboBoxModel getCbmIntCriteria(){
        return this.cbmIntCriteria;
    }
    
    void setRdoAutoRenewalAllowed_Yes(boolean rdoAutoRenewalAllowed_Yes){
        this.rdoAutoRenewalAllowed_Yes = rdoAutoRenewalAllowed_Yes;
        setChanged();
    }
    boolean getRdoAutoRenewalAllowed_Yes(){
        return this.rdoAutoRenewalAllowed_Yes;
    }
    
    void setRdoAutoRenewalAllowed_No(boolean rdoAutoRenewalAllowed_No){
        this.rdoAutoRenewalAllowed_No = rdoAutoRenewalAllowed_No;
        setChanged();
    }
    boolean getRdoAutoRenewalAllowed_No(){
        return this.rdoAutoRenewalAllowed_No;
    }
    
    void setRdoRenewalOfDepositAllowed_Yes(boolean rdoRenewalOfDepositAllowed_Yes){
        this.rdoRenewalOfDepositAllowed_Yes = rdoRenewalOfDepositAllowed_Yes;
        setChanged();
    }
    boolean getRdoRenewalOfDepositAllowed_Yes(){
        return this.rdoRenewalOfDepositAllowed_Yes;
    }
    
    void setRdoRenewalOfDepositAllowed_No(boolean rdoRenewalOfDepositAllowed_No){
        this.rdoRenewalOfDepositAllowed_No = rdoRenewalOfDepositAllowed_No;
        setChanged();
    }
    boolean getRdoRenewalOfDepositAllowed_No(){
        return this.rdoRenewalOfDepositAllowed_No;
    }
    
    void setTxtMinNoOfDays(String txtMinNoOfDays){
        this.txtMinNoOfDays = txtMinNoOfDays;
        setChanged();
    }
    String getTxtMinNoOfDays(){
        return this.txtMinNoOfDays;
    }
    
    void setTxtMaxNopfTimes(String txtMaxNopfTimes){
        this.txtMaxNopfTimes = txtMaxNopfTimes;
        setChanged();
    }
    String getTxtMaxNopfTimes(){
        return this.txtMaxNopfTimes;
    }
    
    void setCboIntType(String cboIntType){
        this.cboIntType = cboIntType;
        setChanged();
    }
    String getCboIntType(){
        return this.cboIntType;
    }
    
    void setCbmIntType(ComboBoxModel cbmIntType){
        this.cbmIntType = cbmIntType;
        setChanged();
    }
    ComboBoxModel getCbmIntType(){
        return this.cbmIntType;
    }
    
    void setCboInterestType(String cboInterestType){
        this.cboInterestType = cboInterestType;
        setChanged();
    }
    String getCboInterestType(){
        return this.cboInterestType;
    }
    
    void setCbmInterestType(ComboBoxModel cbmInterestType){
        this.cbmInterestType = cbmInterestType;
        setChanged();
    }
    ComboBoxModel getCbmInterestType(){
        return this.cbmInterestType;
    }
    
    void setTdtMaturityDate(String tdtMaturityDate){
        this.tdtMaturityDate = tdtMaturityDate;
        setChanged();
    }
    String getTdtMaturityDate(){
        return this.tdtMaturityDate;
    }
    
    void setTdtPresentDate(String tdtPresentDate){
        this.tdtPresentDate = tdtPresentDate;
        setChanged();
    }
    String getTdtPresentDate(){
        return this.tdtPresentDate;
    }
    
    void setRdoPenaltyOnLateInstallmentsChargeble_Yes(boolean rdoPenaltyOnLateInstallmentsChargeble_Yes){
        this.rdoPenaltyOnLateInstallmentsChargeble_Yes = rdoPenaltyOnLateInstallmentsChargeble_Yes;
        setChanged();
    }
    boolean getRdoPenaltyOnLateInstallmentsChargeble_Yes(){
        return this.rdoPenaltyOnLateInstallmentsChargeble_Yes;
    }
    
    void setRdoPenaltyOnLateInstallmentsChargeble_No(boolean rdoPenaltyOnLateInstallmentsChargeble_No){
        this.rdoPenaltyOnLateInstallmentsChargeble_No = rdoPenaltyOnLateInstallmentsChargeble_No;
        setChanged();
    }
    boolean getRdoPenaltyOnLateInstallmentsChargeble_No(){
        return this.rdoPenaltyOnLateInstallmentsChargeble_No;
    }
    
    
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMinimumRenewalDeposit() {
        return cbmMinimumRenewalDeposit;
    }
    
    /**
     * Setter for property cbmMinimumRenewalDeposit.
     * @param cbmMinimumRenewalDeposit New value of property cbmMinimumRenewalDeposit.
     */
    public void setCbmMinimumRenewalDeposit(com.see.truetransact.clientutil.ComboBoxModel cbmMinimumRenewalDeposit) {
        this.cbmMinimumRenewalDeposit = cbmMinimumRenewalDeposit;
    }
    void setTxtMaturityDateAfterLastInstalPaid(String txtMaturityDateAfterLastInstalPaid){
        this.txtMaturityDateAfterLastInstalPaid = txtMaturityDateAfterLastInstalPaid;
        setChanged();
    }
    String getTxtMaturityDateAfterLastInstalPaid(){
        return this.txtMaturityDateAfterLastInstalPaid;
    }
    
    void setRdoCanReceiveExcessInstal_yes(boolean rdoCanReceiveExcessInstal_yes){
        this.rdoCanReceiveExcessInstal_yes = rdoCanReceiveExcessInstal_yes;
        setChanged();
    }
    boolean getRdoCanReceiveExcessInstal_yes(){
        return this.rdoCanReceiveExcessInstal_yes;
    }
    
    void setRdoCanReceiveExcessInstal_No(boolean rdoCanReceiveExcessInstal_No){
        this.rdoCanReceiveExcessInstal_No = rdoCanReceiveExcessInstal_No;
        setChanged();
    }
    boolean getRdoCanReceiveExcessInstal_No(){
        return this.rdoCanReceiveExcessInstal_No;
    }
    
    void setRdoInstallmentInRecurringDepositAcct_Yes(boolean rdoInstallmentInRecurringDepositAcct_Yes){
        this.rdoInstallmentInRecurringDepositAcct_Yes = rdoInstallmentInRecurringDepositAcct_Yes;
        setChanged();
    }
    boolean getRdoInstallmentInRecurringDepositAcct_Yes(){
        return this.rdoInstallmentInRecurringDepositAcct_Yes;
    }
    
    void setRdoInstallmentInRecurringDepositAcct_No(boolean rdoInstallmentInRecurringDepositAcct_No){
        this.rdoInstallmentInRecurringDepositAcct_No = rdoInstallmentInRecurringDepositAcct_No;
        setChanged();
    }
    boolean getRdoInstallmentInRecurringDepositAcct_No(){
        return this.rdoInstallmentInRecurringDepositAcct_No;
    }
    
    void setCboInstallmentToBeCharged(String cboInstallmentToBeCharged){
        this.cboInstallmentToBeCharged = cboInstallmentToBeCharged;
        setChanged();
    }
    String getCboInstallmentToBeCharged(){
        return this.cboInstallmentToBeCharged;
    }
    
    void setCbmInstallmentToBeCharged(ComboBoxModel cbmInstallmentToBeCharged){
        this.cbmInstallmentToBeCharged = cbmInstallmentToBeCharged;
        setChanged();
    }
    ComboBoxModel getCbmInstallmentToBeCharged(){
        return this.cbmInstallmentToBeCharged;
    }
    
    void setCboChangeValue(String cboChangeValue){
        this.cboChangeValue = cboChangeValue;
        setChanged();
    }
    String getCboChangeValue(){
        return this.cboChangeValue;
    }
    
    void setCbmChangeValue(ComboBoxModel cbmChangeValue){
        this.cbmChangeValue = cbmChangeValue;
        setChanged();
    }
    ComboBoxModel getCbmChangeValue(){
        return this.cbmChangeValue;
    }
    
    void setRdoIntPayableOnExcessInstal_Yes(boolean rdoIntPayableOnExcessInstal_Yes){
        this.rdoIntPayableOnExcessInstal_Yes = rdoIntPayableOnExcessInstal_Yes;
        setChanged();
    }
    boolean getRdoIntPayableOnExcessInstal_Yes(){
        return this.rdoIntPayableOnExcessInstal_Yes;
    }
    
    void setRdoIntPayableOnExcessInstal_No(boolean rdoIntPayableOnExcessInstal_No){
        this.rdoIntPayableOnExcessInstal_No = rdoIntPayableOnExcessInstal_No;
        setChanged();
    }
    boolean getRdoIntPayableOnExcessInstal_No(){
        return this.rdoIntPayableOnExcessInstal_No;
    }
    
    void setRdoRecurringDepositToFixedDeposit_Yes(boolean rdoRecurringDepositToFixedDeposit_Yes){
        this.rdoRecurringDepositToFixedDeposit_Yes = rdoRecurringDepositToFixedDeposit_Yes;
        setChanged();
    }
    boolean getRdoRecurringDepositToFixedDeposit_Yes(){
        return this.rdoRecurringDepositToFixedDeposit_Yes;
    }
    
    void setRdoRecurringDepositToFixedDeposit_No(boolean rdoRecurringDepositToFixedDeposit_No){
        this.rdoRecurringDepositToFixedDeposit_No = rdoRecurringDepositToFixedDeposit_No;
        setChanged();
    }
    boolean getRdoRecurringDepositToFixedDeposit_No(){
        return this.rdoRecurringDepositToFixedDeposit_No;
    }
    
    void setTxtCutOffDayForPaymentOfInstal(String txtCutOffDayForPaymentOfInstal){
        this.txtCutOffDayForPaymentOfInstal = txtCutOffDayForPaymentOfInstal;
        setChanged();
    }
    String getTxtCutOffDayForPaymentOfInstal(){
        return this.txtCutOffDayForPaymentOfInstal;
    }
    
    void setCboCutOffDayForPaymentOfInstal(String cboCutOffDayForPaymentOfInstal){
        this.cboCutOffDayForPaymentOfInstal = cboCutOffDayForPaymentOfInstal;
        setChanged();
    }
    String getCboCutOffDayForPaymentOfInstal(){
        return this.cboCutOffDayForPaymentOfInstal;
    }
    
    void setCbmCutOffDayForPaymentOfInstal(ComboBoxModel cbmCutOffDayForPaymentOfInstal){
        this.cbmCutOffDayForPaymentOfInstal = cbmCutOffDayForPaymentOfInstal;
        setChanged();
    }
    ComboBoxModel getCbmCutOffDayForPaymentOfInstal(){
        return this.cbmCutOffDayForPaymentOfInstal;
    }
    
    void setTdtFromDepositDate(String tdtFromDepositDate){
        this.tdtFromDepositDate = tdtFromDepositDate;
        setChanged();
    }
    String getTdtFromDepositDate(){
        return this.tdtFromDepositDate;
    }
    
    void setTdtChosenDate(String tdtChosenDate){
        this.tdtChosenDate = tdtChosenDate;
        setChanged();
    }
    String getTdtChosenDate(){
        return this.tdtChosenDate;
    }
    
    void setRdoLastInstallmentAllowed_Yes(boolean rdoLastInstallmentAllowed_Yes){
        this.rdoLastInstallmentAllowed_Yes = rdoLastInstallmentAllowed_Yes;
        setChanged();
    }
    boolean getRdoLastInstallmentAllowed_Yes(){
        return this.rdoLastInstallmentAllowed_Yes;
    }
    
    void setRdoLastInstallmentAllowed_No(boolean rdoLastInstallmentAllowed_No){
        this.rdoLastInstallmentAllowed_No = rdoLastInstallmentAllowed_No;
        setChanged();
    }
    boolean getRdoLastInstallmentAllowed_No(){
        return this.rdoLastInstallmentAllowed_No;
    }
    
    public int getMultiply(String period){
        int multiply = 0;
        period = CommonUtil.convertObjToStr(period);
        if (period.equalsIgnoreCase(DAY_STR)) {
            multiply = 1;
        } else if (period.equalsIgnoreCase(MONTH_STR)) {
            multiply = 30;
        } else if (period.equalsIgnoreCase(YEAR_STR)) {
            multiply = 365;
        }
        return multiply;
    }
    
    void setLblAuthorize(String lblAuthorize){
        this.lblAuthorize = lblAuthorize;
        setChanged();
    }
    String getLblAuthorize(){
        return this.lblAuthorize;
    }
    
    //    public void verifyAcctHead(String accountHead) {
    //        try{
    //            final HashMap data = new HashMap();
    //            data.put("ACCT_HD",accountHead);
    //            HashMap proxyResultMap = proxy.execute(data,operationMap);
    //        }catch(Exception e){
    //            System.out.println("Error in verifyAcctHead");
    //            parseException.logException(e,true);
    //        }
    //    }
    
    public void verifyAcctHead(com.see.truetransact.uicomponent.CTextField accountHead, String mapName) {
        try{
            final HashMap data = new HashMap();
            data.put("ACCT_HD",accountHead.getText());
            data.put(CommonConstants.MAP_NAME , mapName);
            HashMap proxyResultMap = proxy.execute(data,operationMap);
        }catch(Exception e){
            System.out.println("Error in verifyAcctHead");
            accountHead.setText("");
            parseException.logException(e,true);
        }
    }
    
    void setTxtAfterNoDays(String txtAfterNoDays){
        this.txtAfterNoDays = txtAfterNoDays;
        setChanged();
    }
    String getTxtAfterNoDays(){
        return this.txtAfterNoDays;
    }
    
    void setTxtTDSGLAcctHd(String txtTDSGLAcctHd){
        this.txtTDSGLAcctHd = txtTDSGLAcctHd;
        setChanged();
    }
    String getTxtTDSGLAcctHd(){
        return this.txtTDSGLAcctHd;
    }
    
    void setCbmMinNoOfDays(ComboBoxModel cbmMinNoOfDays){
        this.cbmMinNoOfDays = cbmMinNoOfDays;
        setChanged();
    }
    
    ComboBoxModel getCbmMinNoOfDays(){
        return this.cbmMinNoOfDays;
    }
    
    void setCboMaturityDateAfterLastInstalPaid(String cboMaturityDateAfterLastInstalPaid){
        this.cboMaturityDateAfterLastInstalPaid = cboMaturityDateAfterLastInstalPaid;
        setChanged();
    }
    String getCboMaturityDateAfterLastInstalPaid(){
        return this.cboMaturityDateAfterLastInstalPaid;
    }
    
    void setCbmMaturityDateAfterLastInstalPaid(ComboBoxModel cbmMaturityDateAfterLastInstalPaid){
        this.cbmMaturityDateAfterLastInstalPaid = cbmMaturityDateAfterLastInstalPaid;
        setChanged();
    }
    
    ComboBoxModel getCbmMaturityDateAfterLastInstalPaid(){
        return this.cbmMaturityDateAfterLastInstalPaid;
    }
    
    /**
     * Getter for property cbmDepositsFrequency.
     * @return Value of property cbmDepositsFrequency.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmDepositsFrequency() {
        return cbmDepositsFrequency;
    }
    
    /**
     * Setter for property cbmDepositsFrequency.
     * @param cbmDepositsFrequency New value of property cbmDepositsFrequency.
     */
    public void setCbmDepositsFrequency(com.see.truetransact.clientutil.ComboBoxModel cbmDepositsFrequency) {
        this.cbmDepositsFrequency = cbmDepositsFrequency;
    }
    
    /**
     * Getter for property cboDepositsFrequency.
     * @return Value of property cboDepositsFrequency.
     */
    public java.lang.String getCboDepositsFrequency() {
        return cboDepositsFrequency;
    }
    
    /**
     * Setter for property cboDepositsFrequency.
     * @param cboDepositsFrequency New value of property cboDepositsFrequency.
     */
    public void setCboDepositsFrequency(java.lang.String cboDepositsFrequency) {
        this.cboDepositsFrequency = cboDepositsFrequency;
    }
    
    /**
     * Getter for property tdtDate.
     * @return Value of property tdtDate.
     */
    public java.lang.String getTdtDate() {
        return tdtDate;
    }
    
    /**
     * Setter for property tdtDate.
     * @param tdtDate New value of property tdtDate.
     */
    public void setTdtDate(java.lang.String tdtDate) {
        this.tdtDate = tdtDate;
    }
    
    /**
     * Getter for property tdtToDate.
     * @return Value of property tdtToDate.
     */
    public java.lang.String getTdtToDate() {
        return tdtToDate;
    }
    
    /**
     * Setter for property tdtToDate.
     * @param tdtToDate New value of property tdtToDate.
     */
    public void setTdtToDate(java.lang.String tdtToDate) {
        this.tdtToDate = tdtToDate;
    }
    
    /**
     * Getter for property txtFromPeriod.
     * @return Value of property txtFromPeriod.
     */
    public java.lang.String getTxtFromPeriod() {
        return txtFromPeriod;
    }
    
    /**
     * Setter for property txtFromPeriod.
     * @param txtFromPeriod New value of property txtFromPeriod.
     */
    public void setTxtFromPeriod(java.lang.String txtFromPeriod) {
        this.txtFromPeriod = txtFromPeriod;
    }
    
    /**
     * Getter for property txtToPeriod.
     * @return Value of property txtToPeriod.
     */
    public java.lang.String getTxtToPeriod() {
        return txtToPeriod;
    }
    
    /**
     * Setter for property txtToPeriod.
     * @param txtToPeriod New value of property txtToPeriod.
     */
    public void setTxtToPeriod(java.lang.String txtToPeriod) {
        this.txtToPeriod = txtToPeriod;
    }
    
    /**
     * Getter for property cboFromAmount.
     * @return Value of property cboFromAmount.
     */
    public java.lang.String getCboFromAmount() {
        return cboFromAmount;
    }
    
    /**
     * Setter for property cboFromAmount.
     * @param cboFromAmount New value of property cboFromAmount.
     */
    public void setCboFromAmount(java.lang.String cboFromAmount) {
        this.cboFromAmount = cboFromAmount;
    }
    
    /**
     * Getter for property cboToAmount.
     * @return Value of property cboToAmount.
     */
    public java.lang.String getCboToAmount() {
        return cboToAmount;
    }
    
    /**
     * Setter for property cboToAmount.
     * @param cboToAmount New value of property cboToAmount.
     */
    public void setCboToAmount(java.lang.String cboToAmount) {
        this.cboToAmount = cboToAmount;
    }
    
    /**
     * Getter for property cboFromPeriod.
     * @return Value of property cboFromPeriod.
     */
    public java.lang.String getCboFromPeriod() {
        return cboFromPeriod;
    }
    
    /**
     * Setter for property cboFromPeriod.
     * @param cboFromPeriod New value of property cboFromPeriod.
     */
    public void setCboFromPeriod(java.lang.String cboFromPeriod) {
        this.cboFromPeriod = cboFromPeriod;
    }
    
    /**
     * Getter for property cboToPeriod.
     * @return Value of property cboToPeriod.
     */
    public java.lang.String getCboToPeriod() {
        return cboToPeriod;
    }
    
    /**
     * Setter for property cboToPeriod.
     * @param cboToPeriod New value of property cboToPeriod.
     */
    public void setCboToPeriod(java.lang.String cboToPeriod) {
        this.cboToPeriod = cboToPeriod;
    }
    
    /**
     * Getter for property cbmFromAmount.
     * @return Value of property cbmFromAmount.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFromAmount() {
        return cbmFromAmount;
    }
    
    /**
     * Setter for property cbmFromAmount.
     * @param cbmFromAmount New value of property cbmFromAmount.
     */
    public void setCbmFromAmount(com.see.truetransact.clientutil.ComboBoxModel cbmFromAmount) {
        this.cbmFromAmount = cbmFromAmount;
    }
    
    /**
     * Getter for property cbmToAmount.
     * @return Value of property cbmToAmount.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmToAmount() {
        return cbmToAmount;
    }
    
    /**
     * Setter for property cbmToAmount.
     * @param cbmToAmount New value of property cbmToAmount.
     */
    public void setCbmToAmount(com.see.truetransact.clientutil.ComboBoxModel cbmToAmount) {
        this.cbmToAmount = cbmToAmount;
    }
    
    /**
     * Getter for property cbmFromPeriod.
     * @return Value of property cbmFromPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmFromPeriod() {
        return cbmFromPeriod;
    }
    
    /**
     * Setter for property cbmFromPeriod.
     * @param cbmFromPeriod New value of property cbmFromPeriod.
     */
    public void setCbmFromPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmFromPeriod) {
        this.cbmFromPeriod = cbmFromPeriod;
    }
    
    /**
     * Getter for property cbmToPeriod.
     * @return Value of property cbmToPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmToPeriod() {
        return cbmToPeriod;
    }
    
    /**
     * Setter for property cbmToPeriod.
     * @param cbmToPeriod New value of property cbmToPeriod.
     */
    public void setCbmToPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmToPeriod) {
        this.cbmToPeriod = cbmToPeriod;
    }
    
    /**
     * Getter for property txtRateInterest.
     * @return Value of property txtRateInterest.
     */
    public java.lang.String getTxtRateInterest() {
        return txtRateInterest;
    }
    
    /**
     * Setter for property txtRateInterest.
     * @param txtRateInterest New value of property txtRateInterest.
     */
    public void setTxtRateInterest(java.lang.String txtRateInterest) {
        this.txtRateInterest = txtRateInterest;
    }
    
    /**
     * Getter for property isTableSet.
     * @return Value of property isTableSet.
     */
    public boolean isIsTableSet() {
        return isTableSet;
    }
    
    /**
     * Setter for property isTableSet.
     * @param isTableSet New value of property isTableSet.
     */
    public void setIsTableSet(boolean isTableSet) {
        this.isTableSet = isTableSet;
    }
    
    /**
     * Getter for property tblInterestTable.
     * @return Value of property tblInterestTable.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblInterestTable() {
        return tblInterestTable;
    }
    
    /**
     * Setter for property tblInterestTable.
     * @param tblInterestTable New value of property tblInterestTable.
     */
    public void setTblInterestTable(com.see.truetransact.clientutil.EnhancedTableModel tblInterestTable) {
        this.tblInterestTable = tblInterestTable;
    }
    
    /**
     * Getter for property txtCommisionPaybleGLHead.
     * @return Value of property txtCommisionPaybleGLHead.
     */
    public java.lang.String getTxtCommisionPaybleGLHead() {
        return txtCommisionPaybleGLHead;
    }
    
    /**
     * Setter for property txtCommisionPaybleGLHead.
     * @param txtCommisionPaybleGLHead New value of property txtCommisionPaybleGLHead.
     */
    public void setTxtCommisionPaybleGLHead(java.lang.String txtCommisionPaybleGLHead) {
        this.txtCommisionPaybleGLHead = txtCommisionPaybleGLHead;
    }
    
    /**
     * Getter for property txtFromAmt.
     * @return Value of property txtFromAmt.
     */
    public java.lang.String getTxtFromAmt() {
        return txtFromAmt;
    }
    
    /**
     * Setter for property txtFromAmt.
     * @param txtFromAmt New value of property txtFromAmt.
     */
    public void setTxtFromAmt(java.lang.String txtFromAmt) {
        this.txtFromAmt = txtFromAmt;
    }
    
    /**
     * Getter for property txtToAmt.
     * @return Value of property txtToAmt.
     */
    public java.lang.String getTxtToAmt() {
        return txtToAmt;
    }
    
    /**
     * Setter for property txtToAmt.
     * @param txtToAmt New value of property txtToAmt.
     */
    public void setTxtToAmt(java.lang.String txtToAmt) {
        this.txtToAmt = txtToAmt;
    }
    
    public java.lang.String getTxtRenewedclosedbefore() {
        return txtRenewedclosedbefore;
    }
    
    /**
     * Setter for property txtRenewedclosedbefore.
     * @param txtRenewedclosedbefore New value of property txtRenewedclosedbefore.
     */
    public void setTxtRenewedclosedbefore(java.lang.String txtRenewedclosedbefore) {
        this.txtRenewedclosedbefore = txtRenewedclosedbefore;
    }
    /**
     * Getter for property txtDelayedChargesAmt.
     * @return Value of property txtDelayedChargesAmt.
     */
    public java.lang.String getTxtDelayedChargesAmt() {
        return txtDelayedChargesAmt;
    }
    
    /**
     * Setter for property txtDelayedChargesAmt.
     * @param txtDelayedChargesAmt New value of property txtDelayedChargesAmt.
     */
    public void setTxtDelayedChargesAmt(java.lang.String txtDelayedChargesAmt) {
        this.txtDelayedChargesAmt = txtDelayedChargesAmt;
    }
    
    /**
     * Getter for property tblDelayedInstallmet.
     * @return Value of property tblDelayedInstallmet.
     */
    public com.see.truetransact.clientutil.EnhancedTableModel getTblDelayedInstallmet() {
        return tblDelayedInstallmet;
    }
    
    /**
     * Setter for property tblDelayedInstallmet.
     * @param tblDelayedInstallmet New value of property tblDelayedInstallmet.
     */
    public void setTblDelayedInstallmet(com.see.truetransact.clientutil.EnhancedTableModel tblDelayedInstallmet) {
        this.tblDelayedInstallmet = tblDelayedInstallmet;
    }
    
    /**
     * Getter for property txtPenalChargesAchd.
     * @return Value of property txtPenalChargesAchd.
     */
    public java.lang.String getTxtPenalChargesAchd() {
        return txtPenalChargesAchd;
    }
    
    /**
     * Setter for property txtPenalChargesAchd.
     * @param txtPenalChargesAchd New value of property txtPenalChargesAchd.
     */
    public void setTxtPenalChargesAchd(java.lang.String txtPenalChargesAchd) {
        this.txtPenalChargesAchd = txtPenalChargesAchd;
    }
    
    /**
     * Getter for property txtSuffix.
     * @return Value of property txtSuffix.
     */
    public java.lang.String getTxtSuffix() {
        return txtSuffix;
    }
    
    /**
     * Setter for property txtSuffix.
     * @param txtSuffix New value of property txtSuffix.
     */
    public void setTxtSuffix(java.lang.String txtSuffix) {
        this.txtSuffix = txtSuffix;
    }
    
    /**
     * Getter for property txtLastAcctNumber.
     * @return Value of property txtLastAcctNumber.
     */
    public java.lang.String getTxtLastAcctNumber() {
        return txtLastAcctNumber;
    }
    
    
    
    /**
     * Setter for property lblBothRateNotAvail_No.
     * @param lblBothRateNotAvail_No New value of property lblBothRateNotAvail_No.
     */
    
    /**
     * Setter for property txtLastAcctNumber.
     * @param txtLastAcctNumber New value of property txtLastAcctNumber.
     */
    public void setTxtLastAcctNumber(java.lang.String txtLastAcctNumber) {
        this.txtLastAcctNumber = txtLastAcctNumber;
    }
    
    /**
     * Getter for property discounted_Yes.
     * @return Value of property discounted_Yes.
     */
    boolean getDiscounted_Yes() {
        return discounted_Yes;
    }
    
    /**
     * Setter for property discounted_Yes.
     * @param discounted_Yes New value of property discounted_Yes.
     */
    void setDiscounted_Yes(boolean discounted_Yes) {
        this.discounted_Yes = discounted_Yes;
        setChanged();
    }
    
    /**
     * Getter for property discounted_No.
     * @return Value of property discounted_No.
     */
    boolean getDiscounted_No() {
        return discounted_No;
    }
    
    /**
     * Setter for property discounted_No.
     * @param discounted_No New value of property discounted_No.
     */
    void setDiscounted_No(boolean discounted_No) {
        this.discounted_No = discounted_No;
        setChanged();
    }
    
    /**
     * Getter for property discountedRate.
     * @return Value of property discountedRate.
     */
    public java.lang.String getDiscountedRate() {
        return discountedRate;
    }
    
    /**
     * Setter for property discountedRate.
     * @param discountedRate New value of property discountedRate.
     */
    public void setDiscountedRate(java.lang.String discountedRate) {
        this.discountedRate = discountedRate;
    }
    
    /**
     * Getter for property tdtSchemeClosingDate.
     * @return Value of property tdtSchemeClosingDate.
     */
    public java.lang.String getTdtSchemeClosingDate() {
        return tdtSchemeClosingDate;
    }
    
    /**
     * Setter for property tdtSchemeClosingDate.
     * @param tdtSchemeClosingDate New value of property tdtSchemeClosingDate.
     */
    public void setTdtSchemeClosingDate(java.lang.String tdtSchemeClosingDate) {
        this.tdtSchemeClosingDate = tdtSchemeClosingDate;
    }
    
    /**
     * Getter for property txtMaxNoSameNo.
     * @return Value of property txtMaxNoSameNo.
     */
    public java.lang.String getTxtMaxNoSameNo() {
        return txtMaxNoSameNo;
    }
    
    /**
     * Setter for property txtMaxNoSameNo.
     * @param txtMaxNoSameNo New value of property txtMaxNoSameNo.
     */
    public void setTxtMaxNoSameNo(java.lang.String txtMaxNoSameNo) {
        this.txtMaxNoSameNo = txtMaxNoSameNo;
    }
    
    /**
     * Getter for property rdoSameNoRenewalAllowed_Yes.
     * @return Value of property rdoSameNoRenewalAllowed_Yes.
     */
    boolean getRdoSameNoRenewalAllowed_Yes() {
        return rdoSameNoRenewalAllowed_Yes;
    }
    
    /**
     * Setter for property rdoSameNoRenewalAllowed_Yes.
     * @param rdoSameNoRenewalAllowed_Yes New value of property rdoSameNoRenewalAllowed_Yes.
     */
    void setRdoSameNoRenewalAllowed_Yes(boolean rdoSameNoRenewalAllowed_Yes) {
        this.rdoSameNoRenewalAllowed_Yes = rdoSameNoRenewalAllowed_Yes;
        setChanged();
    }
    
    /**
     * Getter for property rdoSameNoRenewalAllowed_No.
     * @return Value of property rdoSameNoRenewalAllowed_No.
     */
    public boolean getRdoSameNoRenewalAllowed_No() {
        return rdoSameNoRenewalAllowed_No;
    }
    
    /**
     * Setter for property rdoSameNoRenewalAllowed_No.
     * @param rdoSameNoRenewalAllowed_No New value of property rdoSameNoRenewalAllowed_No.
     */
    public void setRdoSameNoRenewalAllowed_No(boolean rdoSameNoRenewalAllowed_No) {
        this.rdoSameNoRenewalAllowed_No = rdoSameNoRenewalAllowed_No;
        setChanged();
    }
    
    /**
     * Getter for property cbmAgentCommision.
     * @return Value of property cbmAgentCommision.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmAgentCommision() {
        return cbmAgentCommision;
    }
    
    /**
     * Setter for property cbmAgentCommision.
     * @param cbmAgentCommision New value of property cbmAgentCommision.
     */
    public void setCbmAgentCommision(com.see.truetransact.clientutil.ComboBoxModel cbmAgentCommision) {
        this.cbmAgentCommision = cbmAgentCommision;
    }
    
    /**
     * Getter for property cbmMinimumPeriod.
     * @return Value of property cbmMinimumPeriod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMinimumPeriod() {
        return cbmMinimumPeriod;
    }
    
    /**
     * Setter for property cbmMinimumPeriod.
     * @param cbmMinimumPeriod New value of property cbmMinimumPeriod.
     */
    public void setCbmMinimumPeriod(com.see.truetransact.clientutil.ComboBoxModel cbmMinimumPeriod) {
        this.cbmMinimumPeriod = cbmMinimumPeriod;
    }
    
    /**
     * Getter for property txtAgentCommision.
     * @return Value of property txtAgentCommision.
     */
    public java.lang.String getTxtAgentCommision() {
        return txtAgentCommision;
    }
    
    /**
     * Setter for property txtAgentCommision.
     * @param txtAgentCommision New value of property txtAgentCommision.
     */
    public void setTxtAgentCommision(java.lang.String txtAgentCommision) {
        this.txtAgentCommision = txtAgentCommision;
    }
    
    /**
     * Getter for property txtMinimumPeriod.
     * @return Value of property txtMinimumPeriod.
     */
    public java.lang.String getTxtMinimumPeriod() {
        return txtMinimumPeriod;
    }
    
    /**
     * Setter for property txtMinimumPeriod.
     * @param txtMinimumPeriod New value of property txtMinimumPeriod.
     */
    public void setTxtMinimumPeriod(java.lang.String txtMinimumPeriod) {
        this.txtMinimumPeriod = txtMinimumPeriod;
    }
    
    /**
     * Getter for property cboAgentCommision.
     * @return Value of property cboAgentCommision.
     */
    public java.lang.String getCboAgentCommision() {
        return cboAgentCommision;
    }
    
    /**
     * Setter for property cboAgentCommision.
     * @param cboAgentCommision New value of property cboAgentCommision.
     */
    public void setCboAgentCommision(java.lang.String cboAgentCommision) {
        this.cboAgentCommision = cboAgentCommision;
    }
    
    /**
     * Getter for property cboMinimumPeriod.
     * @return Value of property cboMinimumPeriod.
     */
    public java.lang.String getCboMinimumPeriod() {
        return cboMinimumPeriod;
    }
    
    /**
     * Setter for property cboMinimumPeriod.
     * @param cboMinimumPeriod New value of property cboMinimumPeriod.
     */
    public void setCboMinimumPeriod(java.lang.String cboMinimumPeriod) {
        this.cboMinimumPeriod = cboMinimumPeriod;
    }
    
    /**
     * Getter for property cbmPrematureWithdrawal.
     * @return Value of property cbmPrematureWithdrawal.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmPrematureWithdrawal() {
        return cbmPrematureWithdrawal;
    }
    
    /**
     * Setter for property cbmPrematureWithdrawal.
     * @param cbmPrematureWithdrawal New value of property cbmPrematureWithdrawal.
     */
    public void setCbmPrematureWithdrawal(com.see.truetransact.clientutil.ComboBoxModel cbmPrematureWithdrawal) {
        this.cbmPrematureWithdrawal = cbmPrematureWithdrawal;
    }
    
    /**
     * Getter for property cboPrematureWithdrawal.
     * @return Value of property cboPrematureWithdrawal.
     */
    public java.lang.String getCboPrematureWithdrawal() {
        return cboPrematureWithdrawal;
    }
    
    /**
     * Setter for property cboPrematureWithdrawal.
     * @param cboPrematureWithdrawal New value of property cboPrematureWithdrawal.
     */
    public void setCboPrematureWithdrawal(java.lang.String cboPrematureWithdrawal) {
        this.cboPrematureWithdrawal = cboPrematureWithdrawal;
    }
    
    /**
     * Getter for property agentsAuthStatus.
     * @return Value of property agentsAuthStatus.
     */
    public java.lang.String getAgentsAuthStatus() {
        return agentsAuthStatus;
    }
    
    /**
     * Setter for property agentsAuthStatus.
     * @param agentsAuthStatus New value of property agentsAuthStatus.
     */
    public void setAgentsAuthStatus(java.lang.String agentsAuthStatus) {
        this.agentsAuthStatus = agentsAuthStatus;
    }
    
    /**
     * Getter for property cbmWeekly.
     * @return Value of property cbmWeekly.
     */
    ComboBoxModel getCbmWeekly() {
        return cbmWeekly;
    }
    
    /**
     * Setter for property cbmWeekly.
     * @param cbmWeekly New value of property cbmWeekly.
     */
    void setCbmWeekly(ComboBoxModel cbmWeekly) {
        this.cbmWeekly = cbmWeekly;
        setChanged();
    }
    
    /**
     * Getter for property rdoDaily.
     * @return Value of property rdoDaily.
     */
    public boolean getRdoDaily() {
        return rdoDaily;
    }
    
    /**
     * Setter for property rdoDaily.
     * @param rdoDaily New value of property rdoDaily.
     */
    public void setRdoDaily(boolean rdoDaily) {
        this.rdoDaily = rdoDaily;
    }
    
    /**
     * Getter for property rdoWeekly.
     * @return Value of property rdoWeekly.
     */
    public boolean getRdoWeekly() {
        return rdoWeekly;
    }
    
    /**
     * Setter for property rdoWeekly.
     * @param rdoWeekly New value of property rdoWeekly.
     */
    public void setRdoWeekly(boolean rdoWeekly) {
        this.rdoWeekly = rdoWeekly;
    }
    
    /**
     * Getter for property rdoMonthly.
     * @return Value of property rdoMonthly.
     */
    public boolean getRdoMonthly() {
        return rdoMonthly;
    }
    
    /**
     * Setter for property rdoMonthly.
     * @param rdoMonthly New value of property rdoMonthly.
     */
    public void setRdoMonthly(boolean rdoMonthly) {
        this.rdoMonthly = rdoMonthly;
    }
    
    /**
     * Getter for property cboWeekly_Day.
     * @return Value of property cboWeekly_Day.
     */
    public java.lang.String getCboWeekly_Day() {
        return cboWeekly_Day;
    }
    
    /**
     * Setter for property cboWeekly_Day.
     * @param cboWeekly_Day New value of property cboWeekly_Day.
     */
    public void setCboWeekly_Day(java.lang.String cboWeekly_Day) {
        this.cboWeekly_Day = cboWeekly_Day;
    }
    
    /**
     * Getter for property txtTransferOutAcHd.
     * @return Value of property txtTransferOutAcHd.
     */
    public java.lang.String getTxtTransferOutAcHd() {
        return txtTransferOutAcHd;
    }
    
    /**
     * Setter for property txtTransferOutAcHd.
     * @param txtTransferOutAcHd New value of property txtTransferOutAcHd.
     */
    public void setTxtTransferOutAcHd(java.lang.String txtTransferOutAcHd) {
        this.txtTransferOutAcHd = txtTransferOutAcHd;
    }
    
    /**
     * Getter for property rdoRegular.
     * @return Value of property rdoRegular.
     */
    public boolean getRdoRegular() {
        return rdoRegular;
    }
    
    /**
     * Setter for property rdoRegular.
     * @param rdoRegular New value of property rdoRegular.
     */
    public void setRdoRegular(boolean rdoRegular) {
        this.rdoRegular = rdoRegular;
    }
    
    /**
     * Getter for property rdoNRO.
     * @return Value of property rdoNRO.
     */
    public boolean getRdoNRO() {
        return rdoNRO;
    }
    
    /**
     * Setter for property rdoNRO.
     * @param rdoNRO New value of property rdoNRO.
     */
    public void setRdoNRO(boolean rdoNRO) {
        this.rdoNRO = rdoNRO;
    }
    
    /**
     * Getter for property rdoNRE.
     * @return Value of property rdoNRE.
     */
    public boolean getRdoNRE() {
        return rdoNRE;
    }
    
    /**
     * Setter for property rdoNRE.
     * @param rdoNRE New value of property rdoNRE.
     */
    public void setRdoNRE(boolean rdoNRE) {
        this.rdoNRE = rdoNRE;
    }
    
    /**
     * Getter for property rdoStaffAccount_Yes.
     * @return Value of property rdoStaffAccount_Yes.
     */
    public boolean getRdoStaffAccount_Yes() {
        return rdoStaffAccount_Yes;
    }
    
    /**
     * Setter for property rdoStaffAccount_Yes.
     * @param rdoStaffAccount_Yes New value of property rdoStaffAccount_Yes.
     */
    public void setRdoStaffAccount_Yes(boolean rdoStaffAccount_Yes) {
        this.rdoStaffAccount_Yes = rdoStaffAccount_Yes;
    }
    
    /**
     * Getter for property rdoStaffAccount_No.
     * @return Value of property rdoStaffAccount_No.
     */
    public boolean getRdoStaffAccount_No() {
        return rdoStaffAccount_No;
    }
    
    /**
     * Setter for property rdoStaffAccount_No.
     * @param rdoStaffAccount_No New value of property rdoStaffAccount_No.
     */
    public void setRdoStaffAccount_No(boolean rdoStaffAccount_No) {
        this.rdoStaffAccount_No = rdoStaffAccount_No;
    }
    
    /**
     * Getter for property cbmInterestNotPayingValue.
     * @return Value of property cbmInterestNotPayingValue.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmInterestNotPayingValue() {
        return cbmInterestNotPayingValue;
    }
    
    /**
     * Setter for property cbmInterestNotPayingValue.
     * @param cbmInterestNotPayingValue New value of property cbmInterestNotPayingValue.
     */
    public void setCbmInterestNotPayingValue(com.see.truetransact.clientutil.ComboBoxModel cbmInterestNotPayingValue) {
        this.cbmInterestNotPayingValue = cbmInterestNotPayingValue;
    }
    
    /**
     * Getter for property txtInterestNotPayingValue.
     * @return Value of property txtInterestNotPayingValue.
     */
    public java.lang.String getTxtInterestNotPayingValue() {
        return txtInterestNotPayingValue;
    }
    
    /**
     * Setter for property txtInterestNotPayingValue.
     * @param txtInterestNotPayingValue New value of property txtInterestNotPayingValue.
     */
    public void setTxtInterestNotPayingValue(java.lang.String txtInterestNotPayingValue) {
        this.txtInterestNotPayingValue = txtInterestNotPayingValue;
    }
    
    /**
     * Getter for property cboInterestNotPayingValue.
     * @return Value of property cboInterestNotPayingValue.
     */
    public java.lang.String getCboInterestNotPayingValue() {
        return cboInterestNotPayingValue;
    }
    
    /**
     * Setter for property cboInterestNotPayingValue.
     * @param cboInterestNotPayingValue New value of property cboInterestNotPayingValue.
     */
    public void setCboInterestNotPayingValue(java.lang.String cboInterestNotPayingValue) {
        this.cboInterestNotPayingValue = cboInterestNotPayingValue;
    }
    
    /**
     * Getter for property cboMonthlyIntCalcMethod.
     * @return Value of property cboMonthlyIntCalcMethod.
     */
    public java.lang.String getCboMonthlyIntCalcMethod() {
        return cboMonthlyIntCalcMethod;
    }
    
    /**
     * Setter for property cboMonthlyIntCalcMethod.
     * @param cboMonthlyIntCalcMethod New value of property cboMonthlyIntCalcMethod.
     */
    public void setCboMonthlyIntCalcMethod(java.lang.String cboMonthlyIntCalcMethod) {
        this.cboMonthlyIntCalcMethod = cboMonthlyIntCalcMethod;
    }
    
    /**
     * Getter for property cbmMonthlyIntCalcMethod.
     * @return Value of property cbmMonthlyIntCalcMethod.
     */
    public com.see.truetransact.clientutil.ComboBoxModel getCbmMonthlyIntCalcMethod() {
        return cbmMonthlyIntCalcMethod;
    }
    
    /**
     * Setter for property cbmMonthlyIntCalcMethod.
     * @param cbmMonthlyIntCalcMethod New value of property cbmMonthlyIntCalcMethod.
     */
    public void setCbmMonthlyIntCalcMethod(com.see.truetransact.clientutil.ComboBoxModel cbmMonthlyIntCalcMethod) {
        this.cbmMonthlyIntCalcMethod = cbmMonthlyIntCalcMethod;
    }
    
    /**
     * Getter for property rdoPartialWithdrawlAllowedForDD.
     * @return Value of property rdoPartialWithdrawlAllowedForDD.
     */
    public java.lang.String getRdoPartialWithdrawlAllowedForDD() {
        return rdoPartialWithdrawlAllowedForDD;
    }
    
    /**
     * Setter for property rdoPartialWithdrawlAllowedForDD.
     * @param rdoPartialWithdrawlAllowedForDD New value of property rdoPartialWithdrawlAllowedForDD.
     */
    public void setRdoPartialWithdrawlAllowedForDD(java.lang.String rdoPartialWithdrawlAllowedForDD) {
        this.rdoPartialWithdrawlAllowedForDD = rdoPartialWithdrawlAllowedForDD;
    }
    
    /**
     * Getter for property rdoWithPeriod.
     * @return Value of property rdoWithPeriod.
     */
    public java.lang.String getRdoWithPeriod() {
        return rdoWithPeriod;
    }
    
    /**
     * Setter for property rdoWithPeriod.
     * @param rdoWithPeriod New value of property rdoWithPeriod.
     */
    public void setRdoWithPeriod(java.lang.String rdoWithPeriod) {
        this.rdoWithPeriod = rdoWithPeriod;
    }
    
    /**
     * Getter for property rdoDoublingScheme.
     * @return Value of property rdoDoublingScheme.
     */
    public java.lang.String getRdoDoublingScheme() {
        return rdoDoublingScheme;
    }
    
    /**
     * Setter for property rdoDoublingScheme.
     * @param rdoDoublingScheme New value of property rdoDoublingScheme.
     */
    public void setRdoDoublingScheme(java.lang.String rdoDoublingScheme) {
        this.rdoDoublingScheme = rdoDoublingScheme;
    }
    
    /**
     * Getter for property rdoCertificate_printing.
     * @return Value of property rdoCertificate_printing.
     */
    public java.lang.String getRdoCertificate_printing() {
        return rdoCertificate_printing;
    }
    
    /**
     * Setter for property rdoCertificate_printing.
     * @param rdoCertificate_printing New value of property rdoCertificate_printing.
     */
    public void setRdoCertificate_printing(java.lang.String rdoCertificate_printing) {
        this.rdoCertificate_printing = rdoCertificate_printing;
    }
    
    /**
     * Getter for property txtServiceCharge.
     * @return Value of property txtServiceCharge.
     */
    public java.lang.String getTxtServiceCharge() {
        return txtServiceCharge;
    }
    
    /**
     * Setter for property txtServiceCharge.
     * @param txtServiceCharge New value of property txtServiceCharge.
     */
    public void setTxtServiceCharge(java.lang.String txtServiceCharge) {
        this.txtServiceCharge = txtServiceCharge;
    }
    
    /**
     * Getter for property postageAcHd.
     * @return Value of property postageAcHd.
     */
    public java.lang.String getPostageAcHd() {
        return postageAcHd;
    }
    
    /**
     * Setter for property postageAcHd.
     * @param postageAcHd New value of property postageAcHd.
     */
    public void setPostageAcHd(java.lang.String postageAcHd) {
        this.postageAcHd = postageAcHd;
    }
    
    /**
     * Getter for property txtRDIrregularIfInstallmentDue.
     * @return Value of property txtRDIrregularIfInstallmentDue.
     */
    public java.lang.String getTxtRDIrregularIfInstallmentDue() {
        return txtRDIrregularIfInstallmentDue;
    }
    
    /**
     * Setter for property txtRDIrregularIfInstallmentDue.
     * @param txtRDIrregularIfInstallmentDue New value of property txtRDIrregularIfInstallmentDue.
     */
    public void setTxtRDIrregularIfInstallmentDue(java.lang.String txtRDIrregularIfInstallmentDue) {
        this.txtRDIrregularIfInstallmentDue = txtRDIrregularIfInstallmentDue;
    }
    
    /**
     * Getter for property rdoIncaseOfIrregularRDSBRate.
     * @return Value of property rdoIncaseOfIrregularRDSBRate.
     */
    public boolean isRdoIncaseOfIrregularRDSBRate() {
        return rdoIncaseOfIrregularRDSBRate;
    }
    
    /**
     * Setter for property rdoIncaseOfIrregularRDSBRate.
     * @param rdoIncaseOfIrregularRDSBRate New value of property rdoIncaseOfIrregularRDSBRate.
     */
    public void setRdoIncaseOfIrregularRDSBRate(boolean rdoIncaseOfIrregularRDSBRate) {
        this.rdoIncaseOfIrregularRDSBRate = rdoIncaseOfIrregularRDSBRate;
    }
    
    /**
     * Getter for property rdoIncaseOfIrregularRDRBRate.
     * @return Value of property rdoIncaseOfIrregularRDRBRate.
     */
    public boolean isRdoIncaseOfIrregularRDRBRate() {
        return rdoIncaseOfIrregularRDRBRate;
    }
    
    /**
     * Setter for property rdoIncaseOfIrregularRDRBRate.
     * @param rdoIncaseOfIrregularRDRBRate New value of property rdoIncaseOfIrregularRDRBRate.
     */
    public void setRdoIncaseOfIrregularRDRBRate(boolean rdoIncaseOfIrregularRDRBRate) {
        this.rdoIncaseOfIrregularRDRBRate = rdoIncaseOfIrregularRDRBRate;
    }
    
    /**
     * Getter for property rdoDepositRate.
     * @return Value of property rdoDepositRate.
     */
    public boolean isRdoDepositRate() {
        return rdoDepositRate;
    }
    
    /**
     * Setter for property rdoDepositRate.
     * @param rdoDepositRate New value of property rdoDepositRate.
     */
    public void setRdoDepositRate(boolean rdoDepositRate) {
        this.rdoDepositRate = rdoDepositRate;
    }
    
    /**
     * Getter for property rdoSBRate.
     * @return Value of property rdoSBRate.
     */
    public boolean isRdoSBRate() {
        return rdoSBRate;
    }
    
    /**
     * Setter for property rdoSBRate.
     * @param rdoSBRate New value of property rdoSBRate.
     */
    public void setRdoSBRate(boolean rdoSBRate) {
        this.rdoSBRate = rdoSBRate;
    }
    
    /**
     * Getter for property chkFDRenewalSameNoTranPrincAmt.
     * @return Value of property chkFDRenewalSameNoTranPrincAmt.
     */
    public boolean isChkFDRenewalSameNoTranPrincAmt() {
        return chkFDRenewalSameNoTranPrincAmt;
    }
    
    /**
     * Setter for property chkFDRenewalSameNoTranPrincAmt.
     * @param chkFDRenewalSameNoTranPrincAmt New value of property chkFDRenewalSameNoTranPrincAmt.
     */
    public void setChkFDRenewalSameNoTranPrincAmt(boolean chkFDRenewalSameNoTranPrincAmt) {
        this.chkFDRenewalSameNoTranPrincAmt = chkFDRenewalSameNoTranPrincAmt;
    }
    
    /**
     * Getter for property cboDepositsProdFixd.
     * @return Value of property cboDepositsProdFixd.
     */
    public String getCboDepositsProdFixd() {
        return cboDepositsProdFixd;
    }
    
    /**
     * Setter for property cboDepositsProdFixd.
     * @param cboDepositsProdFixd New value of property cboDepositsProdFixd.
     */
    public void setCboDepositsProdFixd(String cboDepositsProdFixd) {
        this.cboDepositsProdFixd = cboDepositsProdFixd;
    }
    
    /**
     * Getter for property cbmDepositsProdFixd.
     * @return Value of property cbmDepositsProdFixd.
     */
    public ComboBoxModel getCbmDepositsProdFixd() {
        return cbmDepositsProdFixd;
    }
    
    /**
     * Setter for property cbmDepositsProdFixd.
     * @param cbmDepositsProdFixd New value of property cbmDepositsProdFixd.
     */
    public void setCbmDepositsProdFixd(ComboBoxModel cbmDepositsProdFixd) {
        this.cbmDepositsProdFixd = cbmDepositsProdFixd;
    }
    
     public String getChkExcludeLienStanding() {
        return chkExcludeLienStanding;
    }

    public void setChkExcludeLienStanding(String chkExcludeLienStanding) {
        this.chkExcludeLienStanding = chkExcludeLienStanding;
    }

    public String getChkExcludeLienIntrstAppl() {
        return chkExcludeLienIntrstAppl;
    }

    public void setChkExcludeLienIntrstAppl(String chkExcludeLienIntrstAppl) {
        this.chkExcludeLienIntrstAppl = chkExcludeLienIntrstAppl;
    }
    
    
    public String getChkCummCertPrint() {
        return chkCummCertPrint;
    }

    public void setChkCummCertPrint(String chkCummCertPrint) {
        this.chkCummCertPrint = chkCummCertPrint;
    }
    
    
    public String getChkDepositLien() {
        return chkDepositLien;
    }

    public void setChkDepositLien(String chkDepositLien) {
        this.chkDepositLien = chkDepositLien;
    }
    
        public String getTxtInterestRecoveryAcHd() {
        return txtInterestRecoveryAcHd;
    }

    public void setTxtInterestRecoveryAcHd(String txtInterestRecoveryAcHd) {
        this.txtInterestRecoveryAcHd = txtInterestRecoveryAcHd;
    }

    public String getTxtCategoryBenifitRate() {
        return txtCategoryBenifitRate;
    }

    public void setTxtCategoryBenifitRate(String txtCategoryBenifitRate) {
        this.txtCategoryBenifitRate = txtCategoryBenifitRate;
    }
// added by chithra 17-05-14
    public ComboBoxModel getCbmSbrateModel() {
        return cbmSbrateModel;
    }

    public void setCbmSbrateModel(ComboBoxModel cbmSbrateModel) {
        this.cbmSbrateModel = cbmSbrateModel;
    }

    public String getSbRateCmb() {
        return sbRateCmb;
    }

    public void setSbRateCmb(String sbRateCmb) {
        this.sbRateCmb = sbRateCmb;
    }

    public boolean getChkIntRateApp() {
        return chkIntRateApp;
    }

    public void setChkIntRateApp(boolean chkIntRateApp) {
        this.chkIntRateApp = chkIntRateApp;
    }
    
   // Added by nithya on 02-03-2016 for 0003897

    private HashMap setThriftBenevelontDataForProduct(DepositsThriftBenevolentTO objDepositsThriftBenevolentTO) {
        
        HashMap thriftBenevolentMap = new HashMap();
        thriftBenevolentMap.put("PROD_ID", objDepositsThriftBenevolentTO.getProductId());
        thriftBenevolentMap.put("OPERATES_LIKE",objDepositsThriftBenevolentTO.getOperatesLike());
        thriftBenevolentMap.put("EFFECTIVE_DATE",objDepositsThriftBenevolentTO.getEffectiveDate());
        thriftBenevolentMap.put("INSTALLMENT_AMOUNT",objDepositsThriftBenevolentTO.getInstallmentAmount());
        return thriftBenevolentMap;
    }

    
    public boolean verifyProdEffectiveDate(String prodId,Date effectiveDate) {
        boolean verify = false;
        try{
            final HashMap data = new HashMap();
            String effectiveDateNew = getEffectiveDate();
            data.put("PROD_ID",prodId);            
            data.put("EFFECTIVE_DATE",effectiveDate);
            
            final List resultList = ClientUtil.executeQuery("getEffectiveDateForProduct", data);
            System.out.println("resultList :: " + resultList);
            System.out.println("effective date :: " + getEffectiveDate());            
            
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
    //End

    // Added by shany on 04-08-2017
    public String getTxtGracePeriod() {
        return txtGracePeriod;
    }

    public void setTxtGracePeriod(String txtGracePeriod) {
        this.txtGracePeriod = txtGracePeriod;
    }
    
    // Added by nithya on 22-09-2017 gor identifying whether group deposit product or not

    public String getChkIsGroupDepositProduct() {
        return chkIsGroupDepositProduct;
    }

    public void setChkIsGroupDepositProduct(String chkIsGroupDepositProduct) {
        this.chkIsGroupDepositProduct = chkIsGroupDepositProduct;
    }

    public ComboBoxModel getCbmIrregularRDCloseProduct() {
        return cbmIrregularRDCloseProduct;
    }

    public void setCbmIrregularRDCloseProduct(ComboBoxModel cbmIrregularRDCloseProduct) {
        this.cbmIrregularRDCloseProduct = cbmIrregularRDCloseProduct;
    }

    public ComboBoxModel getCbmIrregularRDCloseRate() {
        return cbmIrregularRDCloseRate;
    }

    public void setCbmIrregularRDCloseRate(ComboBoxModel cbmIrregularRDCloseRate) {
        this.cbmIrregularRDCloseRate = cbmIrregularRDCloseRate;
    }

    public ComboBoxModel getCbmPrmatureCloseProduct() {
        return cbmPrmatureCloseProduct;
    }

    public void setCbmPrmatureCloseProduct(ComboBoxModel cbmPrmatureCloseProduct) {
        this.cbmPrmatureCloseProduct = cbmPrmatureCloseProduct;
    }

    public ComboBoxModel getCbmprmatureCloseRate() {
        return cbmprmatureCloseRate;
    }

    public void setCbmprmatureCloseRate(ComboBoxModel cbmprmatureCloseRate) {
        this.cbmprmatureCloseRate = cbmprmatureCloseRate;
    }

    public String getCboIrregularRDCloseProduct() {
        return cboIrregularRDCloseProduct;
    }

    public void setCboIrregularRDCloseProduct(String cboIrregularRDCloseProduct) {
        this.cboIrregularRDCloseProduct = cboIrregularRDCloseProduct;
    }

    public String getCboIrregularRDCloseRate() {
        return cboIrregularRDCloseRate;
    }

    public void setCboIrregularRDCloseRate(String cboIrregularRDCloseRate) {
        this.cboIrregularRDCloseRate = cboIrregularRDCloseRate;
    }

    public String getCboPrmatureCloseProduct() {
        return cboPrmatureCloseProduct;
    }

    public void setCboPrmatureCloseProduct(String cboPrmatureCloseProduct) {
        this.cboPrmatureCloseProduct = cboPrmatureCloseProduct;
    }

    public String getCboprmatureCloseRate() {
        return cboprmatureCloseRate;
    }

    public void setCboprmatureCloseRate(String cboprmatureCloseRate) {
        this.cboprmatureCloseRate = cboprmatureCloseRate;
    }

    public String getChkRDClosingOtherROI() {
        return chkRDClosingOtherROI;
    }

    public void setChkRDClosingOtherROI(String chkRDClosingOtherROI) {
        this.chkRDClosingOtherROI = chkRDClosingOtherROI;
    }
    
    public boolean populateROIProductCombo(String prodType, String closureType) {
        boolean dataExists = false;
        HashMap prodMap = new HashMap();
        prodMap.put("PROD_TYPE", prodType);
        List ROIProdLst = ClientUtil.executeQuery("getRDClosingOtherRateIntProducts", prodMap);
        key = new ArrayList();
        value = new ArrayList();
        if (ROIProdLst != null && ROIProdLst.size() > 0) {
            dataExists = true;
            for (int i = 0; i < ROIProdLst.size(); i++) {
                prodMap = (HashMap) ROIProdLst.get(i);
                String key1 = CommonUtil.convertObjToStr(prodMap.get("PROD_ID"));
                String val1 = CommonUtil.convertObjToStr(prodMap.get("PROD_DESC"));
                if (closureType.equalsIgnoreCase("PREMATURE")) {
                    cbmPrmatureCloseProduct = new ComboBoxModel(key, value);
                } else {
                    cbmIrregularRDCloseProduct = new ComboBoxModel(key, value);
                }
                if (closureType.equalsIgnoreCase("PREMATURE")) {
                    if (i == 0) {
                        cbmPrmatureCloseProduct.addKeyAndElement("", "");
                    }
                    cbmPrmatureCloseProduct.addKeyAndElement(key1, val1);
                } else {
                    if (i == 0) {
                        cbmIrregularRDCloseProduct.addKeyAndElement("", "");
                    }
                    cbmIrregularRDCloseProduct.addKeyAndElement(key1, val1);
                }
            }
        }
        return dataExists;
    }

    public String getChkIntForIrregularRD() {
        return chkIntForIrregularRD;
    }

    public void setChkIntForIrregularRD(String chkIntForIrregularRD) {
        this.chkIntForIrregularRD = chkIntForIrregularRD;
    }

    public String getChkSpecialRD() {
        return chkSpecialRD;
    }

    public void setChkSpecialRD(String chkSpecialRD) {
        this.chkSpecialRD = chkSpecialRD;
    }

    public String getTxtSpecialRDInstallments() {
        return txtSpecialRDInstallments;
    }

    public void setTxtSpecialRDInstallments(String txtSpecialRDInstallments) {
        this.txtSpecialRDInstallments = txtSpecialRDInstallments;
    }
   
    public TableModel getTbmAgentCommSlabSettings() {
        return tbmAgentCommSlabSettings;
    }

    public void setTbmAgentCommSlabSettings(TableModel tbmAgentCommSlabSettings) {
        this.tbmAgentCommSlabSettings = tbmAgentCommSlabSettings;
    }

    public String getTxtAgentCommSlabAmtFrom() {
        return txtAgentCommSlabAmtFrom;
    }

    public void setTxtAgentCommSlabAmtFrom(String txtAgentCommSlabAmtFrom) {
        this.txtAgentCommSlabAmtFrom = txtAgentCommSlabAmtFrom;
    }

    public String getTxtAgentCommSlabAmtTo() {
        return txtAgentCommSlabAmtTo;
    }

    public void setTxtAgentCommSlabAmtTo(String txtAgentCommSlabAmtTo) {
        this.txtAgentCommSlabAmtTo = txtAgentCommSlabAmtTo;
    }

    public String getTxtAgentCommSlabPercent() {
        return txtAgentCommSlabPercent;
    }

    public void setTxtAgentCommSlabPercent(String txtAgentCommSlabPercent) {
        this.txtAgentCommSlabPercent = txtAgentCommSlabPercent;
    }

    public String getCboAgentCommProductType() {
        return cboAgentCommProductType;
    }

    public void setCboAgentCommProductType(String cboAgentCommProductType) {
        this.cboAgentCommProductType = cboAgentCommProductType;
    }

    public ComboBoxModel getCbmAgentCommCalcMethod() {
        return cbmAgentCommCalcMethod;
    }

    public void setCbmAgentCommCalcMethod(ComboBoxModel cbmAgentCommCalcMethod) {
        this.cbmAgentCommCalcMethod = cbmAgentCommCalcMethod;
    }

    public String getCboAgentCommCalcMethod() {
        return cboAgentCommCalcMethod;
    }

    public void setCboAgentCommCalcMethod(String cboAgentCommCalcMethod) {
        this.cboAgentCommCalcMethod = cboAgentCommCalcMethod;
    }

    public String getChkAgentCommSlabRequired() {
        return chkAgentCommSlabRequired;
    }

    public void setChkAgentCommSlabRequired(String chkAgentCommSlabRequired) {
        this.chkAgentCommSlabRequired = chkAgentCommSlabRequired;
    }
    
    
}

/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * DepositsProductUI.java
 *
 * Created on November 25, 2003, 12:31 PM
 */
package com.see.truetransact.ui.product.deposits;

import java.util.Observable;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
//import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.transferobject.product.deposits.WeeklyDepositSlabSettingsTO;
import java.util.HashMap;

import org.apache.log4j.Logger;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uicomponent.CTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
/**
 *
 * @author  amathan
 *@modified : Sunil
 *      Added Edit Locking - 08-07-2005
 * 
 * @modified : Anju Anand
 *      Added Weekly Deposit Slab Settings for Mantis Id: 10363 on 13 Feb 2015
 * 
 */
public class DepositsProductUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    private DepositsProductOB observable;
    //    private final DepositsProductRB resourceBundle = new DepositsProductRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.deposits.DepositsProductRB", ProxyParameters.LANGUAGE);
    private HashMap mandatoryMap;
    int viewType = -1;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 100, VIEW = 200, COPY = 300;
    final int FIXEDDEPOSIT = 2, ACCHEAD = 3, INTPROVMAT = 4, INTONMAT = 5, MATDEPOSIT = 6;
    final int INTDEPPLHD = 7, INTPAYGLHEAD = 8, INTPROVACCHD = 9, DEPACCHD = 10,
            TDSGLACCTHD = 11, COMMISIONPAYGLHEAD = 12, RDPENALACHD = 13, TRANSFEROUTACHD = 14, POSTAGEACHD = 15, INTRSTRECOVRYACHD = 16;
    final int BENEVOLENTINTRESERVE = 18;
    boolean isFilled = false;
    private String ACCT_TYPE = "ACCT_TYPE";
    private String BALANCETYPE = "BALANCETYPE";
    boolean selectedDelayedRow = false;
    private boolean dateOfRenew = false;
    private boolean dateOfMat = false;
    int rowSelected = -1, updateTab = 1, rowCount = 0;
    private final static Logger log = Logger.getLogger(DepositsProductUI.class);
    private Date currDt = null;
    private boolean selectedSingleRow = false; 
    private WeeklyDepositSlabSettingsTO objWeeklyDepositSettingsTo;
    private ArrayList recordData, deleteData;
    private boolean chkTblSelected = false; 
    private boolean selectedSingleRowAgentSlab = false; 
    private boolean chkTblAgentCommSelected = false; 
    
    /** Creates new form DepositsProductUI */
    public DepositsProductUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initStartup();
    }

    private void initStartup() {
        setFieldNames();
        setHelpMessage();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        enableDisablePeriod(true);
        setButtonEnableDisable();
        enableDisableButtons(false);
        ClientUtil.enableDisable(this, false);
        setMaximumLength();
        observable.resetStatus();
        observable.resetForm();
        txtFromPeriod.setEnabled(false);
        txtToPeriod.setEnabled(false);
        txtCommisionRate.setEnabled(false);
        txtPenalCharges.setEnabled(false);
        txtInterestRecoveryAcHd.setEnabled(false);
        //        btnTransferOutHead.setEnabled(false);
        //        tabDepositsProduct.remove(panFloatingRateAccount);
        lblProdCurrency.setVisible(false);
        cboProdCurrency.setVisible(false);
        tabDepositsProduct.resetVisits();
        btnCommisionPaybleGLHead.setEnabled(false);
        //        btnPenalCharges.setEnabled(false);
        //        tabNewButtons();
        btnTabDelete.setEnabled(false);
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(false);
        btnDelayedNew.setEnabled(false);
        btnDelayedSave.setEnabled(false);
        btnDelayedDel.setEnabled(false);
        panDelayedInstallmets.setVisible(false);
        lblDelayedFromAmt.setVisible(false);
        lblDelayedToAmt.setVisible(false);
        lblDelayedChargesAmt.setVisible(false);
        txtDelayedChargesAmt.setVisible(false);
        txtLastAcctNumber.setEnabled(false);
        txtLastAcctNumber.setEditable(false);
        btnEnableDisplay();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panDepositsProduct);
        ClientUtil.enableDisable(panWithPeriod, false);
        panWithPeriod.setVisible(false);
        lblWithPeriod.setVisible(false);
        panWithPeriod.setEnabled(false);
        lblDoublingScheme.setVisible(false);
        panDoublingScheme.setVisible(false);
        panDoublingScheme.setEnabled(false);
        lblRDIrregularIfInstallmentDue.setVisible(false);
        txtRDIrregularIfInstallmentDue.setVisible(false);
        lblPrematureClosureApply.setVisible(false);
        panPrematureClosureApply.setVisible(false);
        lblPrematureClosureApplyROI.setVisible(false);
        lblDifferentROI.setVisible(false);
        chkDifferentROI.setVisible(false);
        chkPrematureClosure.setVisible(false);
        lblPrematureClosure.setVisible(false);
        lblFDRenewalSameNoTranPrincAmt.setVisible(false);
        chkFDRenewalSameNoTranPrincAmt.setVisible(false);
        disDesc();
        cboDepositsProdFixd.setVisible(false);
        lblDepositsProdFixd.setVisible(false);
        lblInstType.setVisible(false);
        cboInstType.setVisible(false);
        txtNormPeriod.setAllowAll(true);
        cboPreMatIntType.setVisible(false);
        lblpreMatIntType.setVisible(false);
        cboPreMatIntType.setVisible(false);
        cmbSbProduct.setVisible(false);
        panThriftBenevolent.setVisible(false); // Added by nithya on 02-03-2016 for 0003897
        
        ClientUtil.enableDisable(panWeeklyDepositSlab, false);
        tabDepositsProduct.remove(panWeeklyDepositSlab);
        chkIsGroupDeposit.setEnabled(false);// Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
        panAgentCommCalcMethod.setVisible(false);
        panAgentCommSlab.setVisible(false);
        srpAgentCommSlabSettings.setVisible(false);      
        //        tabDepositsProduct.remove(panRD);
        //        tabDepositsProduct.remove(panFloatingRateAccount);
        //        tabDepositsProduct.resetVisits();


//        setInvisible();

        //        new MandatoryCheck().checkMandatory(getClass().getName(), panDepositsProduct);

        //        String temp = "Back Dated Deposits are to be \n"
        //                      +"Renewed Within";
        ////	temp+="\nRenewed Within";
        //        lblMinNoOfDays.setVerticalAlignment(javax.swing.JLabel.TOP);
        //        lblMinNoOfDays.setText(temp);
        
    }

    public void disDesc() {
        txtIntProvisioningAcctHdDesc.setEnabled(false);
        txtIntOnMaturedDepositAcctHeadDesc.setEnabled(false);
        txtFixedDepositAcctHeadDesc.setEnabled(false);
        txttMaturityDepositAcctHeadDesc.setEnabled(false);
        txtIntProvisionOfMaturedDepositDesc.setEnabled(false);
        txtIntPaybleGLHeadDesc.setEnabled(false);
        txtCommisionPaybleGLHeadDesc.setEnabled(false);
        txtPenalChargesDesc.setEnabled(false);
        txtInterestRecoveryAcHdDesc.setEnabled(false);
        txtBenIntReserveHeadDesc.setEnabled(false);
        txtTransferOutHeadDesc.setEnabled(false);
        txtIntDebitPLHeadDesc.setEnabled(false);
        txtAcctHeadForFloatAcctDesc.setEnabled(false);
        txtTDSGLAcctHdDesc.setEnabled(false);
    }

    private void setInvisible() {
        lblAmountRounded.setVisible(false);
        rdoAmountRounded_Yes.setVisible(false);
        rdoAmountRounded_No.setVisible(false);
        lblRoundOffCriteria.setVisible(false);
        cboRoundOffCriteria.setVisible(false);
        txtDepositPerUnit.setVisible(false);
        lblCalcMaturityValue.setVisible(false);
        rdoCalcMaturityValue_Yes.setVisible(false);
        rdoCalcMaturityValue_No.setVisible(false);
        lblProvisionOfInterest.setVisible(false);
        rdoProvisionOfInterest_Yes.setVisible(false);
        rdoProvisionOfInterest_No.setVisible(false);
        lblNoOfPartialWithdrawalAllowed.setVisible(false);
        txtNoOfPartialWithdrawalAllowed.setVisible(false);
        lblMinAmtOfPartialWithdrawalAllowed.setVisible(false);
        txtMinAmtOfPartialWithdrawalAllowed.setVisible(false);
        lblWithdrawalWithInterest.setVisible(false);
        rdoWithdrawalWithInterest_Yes.setVisible(false);
        rdoWithdrawalWithInterest_No.setVisible(false);
        panInCaseOfLien.setVisible(false);
        lblLimitForBulkDeposit.setVisible(false);
        txtLimitForBulkDeposit.setVisible(false);
        lblRecurringDepositToFixedDeposit.setVisible(false);
        rdoRecurringDepositToFixedDeposit_Yes.setVisible(false);
        rdoRecurringDepositToFixedDeposit_No.setVisible(false);
        lblCanReceiveExcessInstal.setVisible(false);
        rdoCanReceiveExcessInstal_yes.setVisible(false);
        rdoCanReceiveExcessInstal_No.setVisible(false);
        lblIntPayableOnExcessInstal.setVisible(false);
        rdoIntPayableOnExcessInstal_Yes.setVisible(false);
        rdoIntPayableOnExcessInstal_No.setVisible(false);
        lblIntMaintainedAsPartOf.setVisible(false);
        cboIntMaintainedAsPartOf.setVisible(false);
        lblMinIntToBePaid.setVisible(false);
        txtMinIntToBePaid.setVisible(false);

    }

    private void setObservable() {
        observable = DepositsProductOB.getInstance();
        observable.addObserver(this);
    }

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

    /* Auto Generated Method - setFieldNames()
    This method assigns name for all the components.
    Other functions are working based on this name. */
    private void setFieldNames() {
        btnAcctHead.setName("btnAcctHead");
        btnAcctHeadForFloatAcct.setName("btnAcctHeadForFloatAcct");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnFixedDepositAcctHead.setName("btnFixedDepositAcctHead");
        btnIntDebitPLHead.setName("btnIntDebitPLHead");
        btnIntOnMaturedDepositAcctHead.setName("btnIntOnMaturedDepositAcctHead");
        btnIntPaybleGLHead.setName("btnIntPaybleGLHead");
        btnIntProvisionOfMaturedDeposit.setName("btnIntProvisionOfMaturedDeposit");
        btnIntProvisioningAcctHd.setName("btnIntProvisioningAcctHd");
        btnMaturityDepositAcctHead.setName("btnMaturityDepositAcctHead");
        btnMaturityInterestRate.setName("btnMaturityInterestRate");
        btnMaturityInterestType.setName("btnMaturityInterestType");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnTDSGLAcctHd.setName("btnTDSGLAcctHd");
        cboChangeValue.setName("cboChangeValue");
        cboCutOffDayForPaymentOfInstal.setName("cboCutOffDayForPaymentOfInstal");
        //        cboDepositPerUnit.setName("cboDepositPerUnit");
        cboInstallmentToBeCharged.setName("cboInstallmentToBeCharged");
        //added for daily deposits
        cboDepositsFrequency.setName("cboDepositsFreqency");
        txtFromAmt.setName("txtFromAmt");
        txtToAmt.setName("txtToAmt");
        lblDepositsFrequency.setName("lblDepositsFrequency");

        lblFromAmount.setName("lblFromAmount");
        lblToAmount.setName("lblToAmount");
        lblFromPeriod.setName("lblFromPeriod");
        lblToPeriod.setName("lblToPeriod");
        lblDate.setName("lblDate");
        lblToDate.setName("lblToDate");
        lblRateInterest.setName("lblRateInterest");
        //cboFromAmount.setName("cboFromAmount");
        //cboToAmount.setName("cboToAmount");
        cboFromPeriod.setName("cboFromPeriod");
        cboToPeriod.setName("cboToPeriod");
        txtFromPeriod.setName("txtFromPeriod");
        txtToPeriod.setName("txtToPeriod");
        tdtDate.setName("tdtDate");
        tdtToDate.setName("tdtToDate");
        txtCommisionRate.setName("txtCommisionRate");
        //        txtPenalCharges.setName("txtPenalCharges");
        tblInterestTable.setName("tblInterestTable");
        tblDelayedInstallmet.setName("tblDelayedInstallmet");
        panFromPeriod.setName("panFromPeriod");
        panToPeriod.setName("panToPeriod");
        panButtons.setName("panButtons");
        panAgentsCalculations.setName("panAgentsCalculations");
        srpInterestTable.setName("srpInterestTable");
        btnTabNew.setName("btnTabNew");
        btnTabDelete.setName("btnTabDelete");
        btnTabSave.setName("btnTabSave");

        cboIntApplnFreq.setName("cboIntApplnFreq");
        cboIntCalcMethod.setName("cboIntCalcMethod");
        cboIntCompoundingFreq.setName("cboIntCompoundingFreq");
        cboIntCriteria.setName("cboIntCriteria");
        //        cboIntMaintainedAsPartOf.setName("cboIntMaintainedAsPartOf");
        cboIntPeriodForBackDatedRenewal.setName("cboIntPeriodForBackDatedRenewal");
        cboIntProvisioningFreq.setName("cboIntProvisioningFreq");
        cboIntRoundOff.setName("cboIntRoundOff");
        cboIntType.setName("cboIntType");
        cboMaturityDateAfterLastInstalPaid.setName("cboMaturityDateAfterLastInstalPaid");
        cboMaturityInterestRate.setName("cboMaturityInterestRate");
        cboMaturityInterestType.setName("cboMaturityInterestType");
        cboMaxDepositPeriod.setName("cboMaxDepositPeriod");
        cboMaxPeriodMDt.setName("cboMaxPeriodMDt");
        cboMinDepositPeriod.setName("cboMinDepositPeriod");
        cboMinNoOfDays.setName("cboMinNoOfDays");
        cboNoOfDays.setName("cboNoOfDays");
        cboOperatesLike.setName("cboOperatesLike");
        cboPeriodInMultiplesOf.setName("cboPeriodInMultiplesOf");
        cboProdCurrency.setName("cboProdCurrency");
        cboInterestType.setName("cboInterestType");
        lblInterestType.setName("lblInterestType");
        cboProvisioningLevel.setName("cboProvisioningLevel");
        cboRoundOffCriteria.setName("cboRoundOffCriteria");
        lblAcctHd.setName("lblAcctHd");
        lblAcctHeadForFloatAcct.setName("lblAcctHeadForFloatAcct");
        lblAcctNumberPattern.setName("lblAcctNumberPattern");
        lblAdjustIntOnDeposits.setName("lblAdjustIntOnDeposits");
        lblAdjustPrincipleToLoan.setName("lblAdjustPrincipleToLoan");
        lblAdvanceMaturityNoticeGenPeriod.setName("lblAdvanceMaturityNoticeGenPeriod");
        lblAfterHowManyDays.setName("lblAfterHowManyDays");
        lblAfterHowManyDaysDisplay.setName("lblAfterHowManyDaysDisplay");
        lblAfterNoDays.setName("lblAfterNoDays");
        lblAlphaSuffix.setName("lblAlphaSuffix");
        lblAmountRounded.setName("lblAmountRounded");
        lblAmtInMultiplesOf.setName("lblAmtInMultiplesOf");
        lblAutoAdjustment.setName("lblAutoAdjustment");
        lblAutoRenewalAllowed.setName("lblAutoRenewalAllowed");
        lblCalcMaturityValue.setName("lblCalcMaturityValue");
        lblCalcTDS.setName("lblCalcTDS");
        lblCanReceiveExcessInstal.setName("lblCanReceiveExcessInstal");
        lblChangeValue.setName("lblChangeValue");
        lblChosenDate.setName("lblChosenDate");
        lblCutOffDayForPaymentOfInstal.setName("lblCutOffDayForPaymentOfInstal");
        lblDepositPerUnit.setName("lblDepositPerUnit");
        lblDesc.setName("lblDesc");
        lblExtnOfDepositBeforeMaturity.setName("lblExtnOfDepositBeforeMaturity");
        lblFixedDepositAcctHead.setName("lblFixedDepositAcctHead");
        lblPenalCharges.setName("lblPenalCharges");
        lblFlexiFromSBCA.setName("lblFlexiFromSBCA");
        lblFromDepositDate.setName("lblFromDepositDate");
        lblInstallmentInRecurringDepositAcct.setName("lblInstallmentInRecurringDepositAcct");
        lblInstallmentToBeCharged.setName("lblInstallmentToBeCharged");
        lblIntApplnFreq.setName("lblIntApplnFreq");
        lblIntCalcMethod.setName("lblIntCalcMethod");
        lblIntCompoundingFreq.setName("lblIntCompoundingFreq");
        lblIntCriteria.setName("lblIntCriteria");
        lblIntDebitPLHead.setName("lblIntDebitPLHead");
        lblIntMaintainedAsPartOf.setName("lblIntMaintainedAsPartOf");
        lblIntOnMaturedDepositAcctHead.setName("lblIntOnMaturedDepositAcctHead");
        lblIntPayableOnExcessInstal.setName("lblIntPayableOnExcessInstal");
        lblIntPaybleGLHead.setName("lblIntPaybleGLHead");

        lblCommisionPaybleGLHead.setName("lblCommisionPaybleGLHead");
        txtCommisionPaybleGLHead.setName("txtCommisionPaybleGLHead");
        btnCommisionPaybleGLHead.setName("btnCommisionPaybleGLHead");

        lblIntPeriodForBackDatedRenewal.setName("lblIntPeriodForBackDatedRenewal");
        lblIntProvisionOfMaturedDeposit.setName("lblIntProvisionOfMaturedDeposit");
        lblIntProvisioningAcctHd.setName("lblIntProvisioningAcctHd");
        lblIntProvisioningApplicable.setName("lblIntProvisioningApplicable");
        lblIntProvisioningFreq.setName("lblIntProvisioningFreq");
        lblIntRoundOff.setName("lblIntRoundOff");
        lblIntType.setName("lblIntType");
        lblInterestAfterMaturity.setName("lblInterestAfterMaturity");
        lblInterestOnMaturedDeposits.setName("lblInterestOnMaturedDeposits");
        lblInterestOnMaturedDepositsDays.setName("lblInterestOnMaturedDepositsDays");
        lblIntroducerReqd.setName("lblIntroducerReqd");
        lblLastInstallmentAllowed.setName("lblLastInstallmentAllowed");
        lblLastIntProvisionalDate.setName("lblLastIntProvisionalDate");
        lblLastInterestAppliedDate.setName("lblLastInterestAppliedDate");
        lblLimitForBulkDeposit.setName("lblLimitForBulkDeposit");
        lblMaturityDate.setName("lblMaturityDate");
        //        lblMaturityDate1.setName("lblMaturityDate1");
        lblMaturityDateAfterLastInstalPaid.setName("lblMaturityDateAfterLastInstalPaid");
        lblMaturityDepositAcctHead.setName("lblMaturityDepositAcctHead");
        lblMaxAmtOfCashPayment.setName("lblMaxAmtOfCashPayment");
        lblMaxAmtOfPartialWithdrawalAllowed.setName("lblMaxAmtOfPartialWithdrawalAllowed");
        lblMaxAmtPartialWithdrawalPercent.setName("lblMaxAmtPartialWithdrawalPercent");
        lblMaxDepositAmt.setName("lblMaxDepositAmt");
        lblMaxDepositPeriod.setName("lblMaxDepositPeriod");
        lblMaxNoOfPartialWithdrawalAllowed.setName("lblMaxNoOfPartialWithdrawalAllowed");
        lblMaxNopfTimes.setName("lblMaxNopfTimes");
        lblMinAmtOfPAN.setName("lblMinAmtOfPAN");
        lblMinAmtOfPartialWithdrawalAllowed.setName("lblMinAmtOfPartialWithdrawalAllowed");
        lblMinDepositAmt.setName("lblMinDepositAmt");
        lblMinDepositPeriod.setName("lblMinDepositPeriod");
        lblMinIntToBePaid.setName("lblMinIntToBePaid");
        lblMinNoOfDays.setName("lblMinNoOfDays");
        //        lblMinNoOfDays1.setName("lblMinNoOfDays1");
        lblMsg.setName("lblMsg");
        lblNextIntProvisionalDate.setName("lblNextIntProvisionalDate");
        lblNextInterestAppliedDate.setName("lblNextInterestAppliedDate");
        lblNoOfDays.setName("lblNoOfDays");
        lblNoOfPartialWithdrawalAllowed.setName("lblNoOfPartialWithdrawalAllowed");
        lblOperatesLike.setName("lblOperatesLike");
        lblPartialWithdrawalAllowed.setName("lblPartialWithdrawalAllowed");
        lblPayInterestOnHoliday.setName("lblPayInterestOnHoliday");

        lblPenaltyOnLateInstallmentsChargeble.setName("lblPenaltyOnLateInstallmentsChargeble");
        lblDelayedFromAmt.setName("lblDelayedFromAmt");
        lblDelayedToAmt.setName("lblDelayedToAmt");
        lblDelayedChargesAmt.setName("lblDelayedChargesAmt");
        lblDelayedChargesDet.setName("lblDelayedChargesDet");

        lblPeriodInMultiplesOf.setName("lblPeriodInMultiplesOf");
        lblPrematureWithdrawal.setName("lblPrematureWithdrawal");
        //        lblPrematureWithdrawalDays.setName("lblPrematureWithdrawalDays");
        lblProdCurrency.setName("lblProdCurrency");
        lblProductID.setName("lblProductID");
        lblProvisionOfInterest.setName("lblProvisionOfInterest");
        lblProvisioningLevel.setName("lblProvisioningLevel");
        lblRecalcOfMaturityValue.setName("lblRecalcOfMaturityValue");
        lblRecurringDepositToFixedDeposit.setName("lblRecurringDepositToFixedDeposit");
        lblRenewalOfDepositAllowed.setName("lblRenewalOfDepositAllowed");
        lblRoundOffCriteria.setName("lblRoundOffCriteria");
        lblSchemeIntroDate.setName("lblSchemeIntroDate");
        lblSchemeClosingDate.setName("lblSchemeClosingDate");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblSystemCalcValues.setName("lblSystemCalcValues");
        lblTDSGLAcctHd.setName("lblTDSGLAcctHd");
        lblTermDeposit.setName("lblTermDeposit");
        lblTransferToMaturedDeposits.setName("lblTransferToMaturedDeposits");
        lblWithdrawalWithInterest.setName("lblWithdrawalWithInterest");
        lblServiceCharge.setName("lblServiceCharge");
        lblInsBeyondMaturityDtYesorNo.setName("lblInsBeyondMaturityDtYesorNo");
        lblWithdrawalsInMultiplesOf.setName("lblWithdrawalsInMultiplesOf");
        mbrDepositsProduct.setName("mbrDepositsProduct");
        panAcHd.setName("panAcHd");
        panAcHdRenewal.setName("panAcHdRenewal");
        panAccHead.setName("panAccHead");
        panAccountHead.setName("panAccountHead");
        panAdvanceMaturityNoticeGenPeriod.setName("panAdvanceMaturityNoticeGenPeriod");
        panAfterHowManyDays.setName("panAfterHowManyDays");
        panAmountRounded.setName("panAmountRounded");
        panAutoRenewalAllowed.setName("panAutoRenewalAllowed");
        panCalcMaturityValue.setName("panCalcMaturityValue");
        panCalcTDS.setName("panCalcTDS");
        panTypeOfDeposit.setName("panTypeOfDeposit");
        panDiscounted.setName("panDiscounted");
        panCanReceiveExcessInstal.setName("panCanReceiveExcessInstal");
        panDate.setName("panDate");
        panDepositPerUnit.setName("panDepositPerUnit");
        panDepositsProduct.setName("panDepositsProduct");
        panExtnOfDepositBeforeMaturity.setName("panExtnOfDepositBeforeMaturity");
        panFlexiFromSBCA.setName("panFlexiFromSBCA");
        panFloatingRateAccount.setName("panFloatingRateAccount");
        panInCaseOfLien.setName("panInCaseOfLien");
        panInstallmentInRecurringDepositAcct.setName("panInstallmentInRecurringDepositAcct");
        panIntPayableOnExcessInstal.setName("panIntPayableOnExcessInstal");
        panIntPeriodForBackDatedRenewal.setName("panIntPeriodForBackDatedRenewal");
        panIntProvisioningApplicable.setName("panIntProvisioningApplicable");
        panInterest.setName("panInterest");
        panInterestAfterMaturity.setName("panInterestAfterMaturity");
        panInterestPayment.setName("panInterestPayment");
        panIntroducerReqd.setName("panIntroducerReqd");
        panLastInstallmentAllowed.setName("panLastInstallmentAllowed");
        panLastInstallmentAllowedRD.setName("panLastInstallmentAllowedRD");
        panMaturityDateAfterLastInstalPaid.setName("panMaturityDateAfterLastInstalPaid");
        panMaxDepositPeriod.setName("panMaxDepositPeriod");
        panMaxPeriodMDt.setName("panMaxPeriodMDt");
        panMinDepositPeriod.setName("panMinDepositPeriod");
        panMinNoOfDays.setName("panMinNoOfDays");
        panPartialWithdrawalAllowed.setName("panPartialWithdrawalAllowed");
        panPayInterestOnHoliday.setName("panPayInterestOnHoliday");
        panPenaltyOnLateInstallmentsChargeble.setName("panPenaltyOnLateInstallmentsChargeble");
        panPeriodInMultiplesOf.setName("panPeriodInMultiplesOf");
        panPrematureWithdrawal.setName("panPrematureWithdrawal");
        panProvisionOfInterest.setName("panProvisionOfInterest");
        panRD.setName("panRD");
        panRdgProvisionOfInterest.setName("panRdgProvisionOfInterest");
        panRecurringDepositToFixedDeposit.setName("panRecurringDepositToFixedDeposit");
        panRenewalAndTax.setName("panRenewalAndTax");
        //        panRenewalClosure.setName("panRenewalClosure");
        panRenewalOfDepositAllowed.setName("panRenewalOfDepositAllowed");
        panScheme.setName("panScheme");
        panStatus.setName("panStatus");
        panSystemCalcValues.setName("panSystemCalcValues");
        panTax.setName("panTax");
        panTermDeposit.setName("panTermDeposit");
        panTransferToMaturedDeposits.setName("panTransferToMaturedDeposits");
        panWithDrawIntPay.setName("panWithDrawIntPay");
        panWithdrawal.setName("panWithdrawal");
        panRecurringDetails.setName("panRecurringDetails");
        panDelayedInstallmets.setName("panDelayedInstallmets");
        panWithdrawalWithInterest.setName("panWithdrawalWithInterest");
        rdoAdjustIntOnDeposits_No.setName("rdoAdjustIntOnDeposits_No");
        rdoAdjustIntOnDeposits_Yes.setName("rdoAdjustIntOnDeposits_Yes");
        rdoAdjustPrincipleToLoan_No.setName("rdoAdjustPrincipleToLoan_No");
        rdoAdjustPrincipleToLoan_Yes.setName("rdoAdjustPrincipleToLoan_Yes");
        rdoAmountRounded_No.setName("rdoAmountRounded_No");
        rdoAmountRounded_Yes.setName("rdoAmountRounded_Yes");
        rdoAutoAdjustment_No.setName("rdoAutoAdjustment_No");
        rdoAutoAdjustment_Yes.setName("rdoAutoAdjustment_Yes");
        rdoAutoRenewalAllowed_No.setName("rdoAutoRenewalAllowed_No");
        rdoAutoRenewalAllowed_Yes.setName("rdoAutoRenewalAllowed_Yes");
        rdoCalcMaturityValue_No.setName("rdoCalcMaturityValue_No");
        rdoCalcMaturityValue_Yes.setName("rdoCalcMaturityValue_Yes");
        rdoCalcTDS_No.setName("rdoCalcTDS_No");
        rdoCalcTDS_Yes.setName("rdoCalcTDS_Yes");
        //        rdoRegular.setName("rdoRegular");
        //        rdoNRO.setName("rdoNRO");
        //        rdoNRE.setName("rdoNRE");
        rdoCanReceiveExcessInstal_No.setName("rdoCanReceiveExcessInstal_No");
        rdoCanReceiveExcessInstal_yes.setName("rdoCanReceiveExcessInstal_yes");
        rdoExtnOfDepositBeforeMaturity_No.setName("rdoExtnOfDepositBeforeMaturity_No");
        rdoExtnOfDepositBeforeMaturity_Yes.setName("rdoExtnOfDepositBeforeMaturity_Yes");
        rdoFlexiFromSBCA_No.setName("rdoFlexiFromSBCA_No");
        rdoFlexiFromSBCA_Yes.setName("rdoFlexiFromSBCA_Yes");
        rdoInstallmentInRecurringDepositAcct_No.setName("rdoInstallmentInRecurringDepositAcct_No");
        rdoInstallmentInRecurringDepositAcct_Yes.setName("rdoInstallmentInRecurringDepositAcct_Yes");
        rdoIntPayableOnExcessInstal_No.setName("rdoIntPayableOnExcessInstal_No");
        rdoIntPayableOnExcessInstal_Yes.setName("rdoIntPayableOnExcessInstal_Yes");
        rdoIntProvisioningApplicable_No.setName("rdoIntProvisioningApplicable_No");
        rdoIntProvisioningApplicable_Yes.setName("rdoIntProvisioningApplicable_Yes");
        rdoInterestAfterMaturity_No.setName("rdoInterestAfterMaturity_No");
        rdoInterestAfterMaturity_Yes.setName("rdoInterestAfterMaturity_Yes");
        rdoIntroducerReqd_No.setName("rdoIntroducerReqd_No");
        rdoIntroducerReqd_Yes.setName("rdoIntroducerReqd_Yes");
        rdoLastInstallmentAllowed_No.setName("rdoLastInstallmentAllowed_No");
        rdoLastInstallmentAllowed_Yes.setName("rdoLastInstallmentAllowed_Yes");
        rdoPartialWithdrawalAllowed_No.setName("rdoPartialWithdrawalAllowed_No");
        rdoPartialWithdrawalAllowed_Yes.setName("rdoPartialWithdrawalAllowed_Yes");
        rdoPayInterestOnHoliday_No.setName("rdoPayInterestOnHoliday_No");
        rdoPayInterestOnHoliday_Yes.setName("rdoPayInterestOnHoliday_Yes");
        rdoPenaltyOnLateInstallmentsChargeble_No.setName("rdoPenaltyOnLateInstallmentsChargeble_No");
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setName("rdoPenaltyOnLateInstallmentsChargeble_Yes");
        rdoProvisionOfInterest_No.setName("rdoProvisionOfInterest_No");
        rdoProvisionOfInterest_Yes.setName("rdoProvisionOfInterest_Yes");
        rdoRecalcOfMaturityValue_No.setName("rdoRecalcOfMaturityValue_No");
        rdoRecalcOfMaturityValue_Yes.setName("rdoRecalcOfMaturityValue_Yes");
        rdoRecurringDepositToFixedDeposit_No.setName("rdoRecurringDepositToFixedDeposit_No");
        rdoRecurringDepositToFixedDeposit_Yes.setName("rdoRecurringDepositToFixedDeposit_Yes");
        rdoRenewalOfDepositAllowed_No.setName("rdoRenewalOfDepositAllowed_No");
        rdoRenewalOfDepositAllowed_Yes.setName("rdoRenewalOfDepositAllowed_Yes");
        rdoSystemCalcValues_No.setName("rdoSystemCalcValues_No");
        rdoSystemCalcValues_Yes.setName("rdoSystemCalcValues_Yes");
        rdoTermDeposit_No.setName("rdoTermDeposit_No");
        rdoTermDeposit_Yes.setName("rdoTermDeposit_Yes");
        rdoTransferToMaturedDeposits_No.setName("rdoTransferToMaturedDeposits_No");
        rdoTransferToMaturedDeposits_Yes.setName("rdoTransferToMaturedDeposits_Yes");
        rdoWithdrawalWithInterest_No.setName("rdoWithdrawalWithInterest_No");
        rdoWithdrawalWithInterest_Yes.setName("rdoWithdrawalWithInterest_Yes");
        sptCentre.setName("sptCentre");
        sptInterestPayment.setName("sptInterestPayment");
        sptOperatesLike.setName("sptOperatesLike");
        tabDepositsProduct.setName("tabDepositsProduct");
        tdtChosenDate.setName("tdtChosenDate");
        tdtFromDepositDate.setName("tdtFromDepositDate");
        tdtLastIntProvisionalDate.setName("tdtLastIntProvisionalDate");
        tdtLastInterestAppliedDate.setName("tdtLastInterestAppliedDate");
        tdtMaturityDate.setName("tdtMaturityDate");
        tdtNextIntProvisionalDate.setName("tdtNextIntProvisionalDate");
        tdtNextInterestAppliedDate.setName("tdtNextInterestAppliedDate");
        tdtPresentDate.setName("tdtPresentDate");
        tdtSchemeIntroDate.setName("tdtSchemeIntroDate");
        tdtSchemeClosingDate.setName("tdtSchemeClosingDate");
        txtAcctHd.setName("txtAcctHd");
        txtAcctHeadForFloatAcct.setName("txtAcctHeadForFloatAcct");
        txtAcctNumberPattern.setName("txtAcctNumberPattern");
        txtLastAcctNumber.setName("txtLastAcctNumber");
        txtSuffix.setName("txtSuffix");
        txtAdvanceMaturityNoticeGenPeriod.setName("txtAdvanceMaturityNoticeGenPeriod");
        txtAfterHowManyDays.setName("txtAfterHowManyDays");
        txtAfterNoDays.setName("txtAfterNoDays");
        txtAlphaSuffix.setName("txtAlphaSuffix");
        txtAmtInMultiplesOf.setName("txtAmtInMultiplesOf");
        txtCutOffDayForPaymentOfInstal.setName("txtCutOffDayForPaymentOfInstal");
        txtDepositPerUnit.setName("txtDepositPerUnit");
        txtDesc.setName("txtDesc");
        txtFixedDepositAcctHead.setName("txtFixedDepositAcctHead");
        txtIntDebitPLHead.setName("txtIntDebitPLHead");
        txtIntOnMaturedDepositAcctHead.setName("txtIntOnMaturedDepositAcctHead");
        txtIntPaybleGLHead.setName("txtIntPaybleGLHead");
        txtIntPeriodForBackDatedRenewal.setName("txtIntPeriodForBackDatedRenewal");
        txtIntProvisionOfMaturedDeposit.setName("txtIntProvisionOfMaturedDeposit");
        txtIntProvisioningAcctHd.setName("txtIntProvisioningAcctHd");
        txtInterestOnMaturedDeposits.setName("txtInterestOnMaturedDeposits");
        txtLimitForBulkDeposit.setName("txtLimitForBulkDeposit");
        txtMaturityDateAfterLastInstalPaid.setName("txtMaturityDateAfterLastInstalPaid");
        txtMaxAmtOfCashPayment.setName("txtMaxAmtOfCashPayment");
        txtMaxAmtOfPartialWithdrawalAllowed.setName("txtMaxAmtOfPartialWithdrawalAllowed");
        txtMaxDepositAmt.setName("txtMaxDepositAmt");
        txtMaxDepositPeriod.setName("txtMaxDepositPeriod");
        txtMaxNoOfPartialWithdrawalAllowed.setName("txtMaxNoOfPartialWithdrawalAllowed");
        txtMaxNopfTimes.setName("txtMaxNopfTimes");
        txtMaxPeriodMDt.setName("txtMaxPeriodMDt");
        txtMinAmtOfPAN.setName("txtMinAmtOfPAN");
        txtMinAmtOfPartialWithdrawalAllowed.setName("txtMinAmtOfPartialWithdrawalAllowed");
        txtMinDepositAmt.setName("txtMinDepositAmt");
        txtMinDepositPeriod.setName("txtMinDepositPeriod");
        txtMinIntToBePaid.setName("txtMinIntToBePaid");
        txtMinNoOfDays.setName("txtMinNoOfDays");
        txtNoOfPartialWithdrawalAllowed.setName("txtNoOfPartialWithdrawalAllowed");
        txtServiceCharge.setName("txtServiceCharge");
        txtPeriodInMultiplesOf.setName("txtPeriodInMultiplesOf");
        txtPrematureWithdrawal.setName("txtPrematureWithdrawal");
        txtProductID.setName("txtProductID");
        txtTDSGLAcctHd.setName("txtTDSGLAcctHd");
        txtWithdrawalsInMultiplesOf.setName("txtWithdrawalsInMultiplesOf");
        txttMaturityDepositAcctHead.setName("txttMaturityDepositAcctHead");
        txtDelayedChargesAmt.setName("txtDelayedChargesAmt");
        rdoDiscounted_No.setName("rdoDiscounted_No");
        rdoDiscounted_Yes.setName("rdoDiscounted_Yes");
        rdoExtensionBeyondOriginalDate_Yes.setName("rdoExtensionBeyondOriginalDate_Yes");
        rdoExtensionPenal_Yes.setName("rdoExtensionPenal_Yes");
        rdoExtensionPenal_No.setName("rdoExtensionPenal_No");
        lblMinimumRenewalDeposit.setName("lblMinimumRenewalDeposit");
        lblRenewedclosedbefore.setName("lblRenewedclosedbefore");
        lblclosedwithinperiod.setName("lblclosedwithinperiod");
        lblInterestalreadyPaid.setName("lblInterestalreadyPaid");
        lblInterestrateappliedoverdue.setName("lblInterestrateappliedoverdue");
        lblRateAsOnDateOfRenewal.setName("lblRateAsOnDateOfRenewal");
        lblRateAsOnDateOfMaturity.setName("lblRateAsOnDateOfMaturity");
        lblBothRateAvail.setName("lblBothRateAvail");
        lblOneRateAvail.setName("lblOneRateAvail");
        lblBothRateNotAvail.setName("lblBothRateNotAvail");
        lblAutoRenewalAllowed.setName("lblAutoRenewalAllowed");
        lblMaxNopfTimes.setName("lblMaxNopfTimes");
        lblAutoRenewalAllowed1.setName("lblAutoRenewalAllowed1");
        lblMaxNopfTimes1.setName("lblMaxNopfTimes1");
        lblIntType.setName("lblIntType");
        lblMaturityDate.setName("lblMaturityDate");
        lblMinNoOfDays.setName("lblMinNoOfDays");
        lblIntCriteria.setName("lblIntCriteria");
        txtRenewedclosedbefore.setName("txtRenewedclosedbefore");
        cboRenewedclosedbefore.setName("cboRenewedclosedbefore");
        rdoClosedwithinperiod_Yes.setName("rdoClosedwithinperiod_Yes");
        rdoClosedwithinperiod_No.setName("rdoClosedwithinperiod_No");
        rdoInterestalreadyPaid_Yes.setName("rdoInterestalreadyPaid_Yes");
        rdoInterestalreadyPaid_No.setName("rdoInterestalreadyPaid_No");
        rdoInterestrateappliedoverdue_Yes.setName("rdoInterestrateappliedoverdue_Yes");
        rdoInterestrateappliedoverdue_No.setName("rdoInterestrateappliedoverdue_No");
        chkRateAsOnDateOfRenewal.setName("chkRateAsOnDateOfRenewal");
        chkRateAsOnDateOfMaturity.setName("chkRateAsOnDateOfMaturity");
        cboEitherofTwoRatesChoose.setName("cboEitherofTwoRatesChoose");
        rdoSBRateOneRate_Yes.setName("rdoSBRateOneRate_Yes");
        rdoSBRateOneRate_No.setName("rdoSBRateOneRate_No");
        rdoBothRateNotAvail_Yes.setName("rdoBothRateNotAvail_Yes");
        rdoBothRateNotAvail_No.setName("rdoBothRateNotAvail_No");
        rdoAutoRenewalAllowed_Yes.setName("rdoAutoRenewalAllowed_Yes");
        rdoAutoRenewalAllowed_No.setName("rdoAutoRenewalAllowed_No");
        txtMaxNopfTimes.setName("txtMaxNopfTimes");
        rdoSameNoRenewalAllowed_Yes.setName("rdoSameNoRenewalAllowed_Yes");
        rdoSameNoRenewalAllowed_No.setName("rdoSameNoRenewalAllowed_No");
        txtMaxNopfTimes1.setName("txtMaxNopfTimes1");
        cboIntType.setName("cboIntType");
        txtMaxPeriodMDt.setName("txtMaxPeriodMDt");
        cboMaxPeriodMDt.setName("cboMaxPeriodMDt");
        txtMinNoOfDays.setName("txtMinNoOfDays");
        cboMinNoOfDays.setName("cboMinNoOfDays");
        cboIntCriteria.setName("cboIntCriteria");
        lblBasedOnDepRate.setName("lblBasedOnDepRate");
        txtPostageAcHd.setName("txtPostageAcHd");
        sbRateCmb.setName("sbRateCmb");
        lblIntRateApp.setName("lblIntRateApp");
        chkIntRateApp.setName("chkIntRateApp");
        chkIntRateDeathMarked.setName("chkIntRateDeathMarked");
        panWeeklyDepositSlab.setName("panWeeklyDepositSlab");
        panSlabDetails.setName("panSlabDetails");        
        lblProductType.setName("lblProductType");
        cboProductType.setName("cboProductType");
        lbInstallmentFrom.setName("lbInstallmentFrom");
        txtInstallmentFrom.setName("txtInstallmentFrom");
        lblInstallmentTo.setName("lblInstallmentTo");
        txtInstallmentTo.setName("txtInstallmentTo");
        lblPenal.setName("lblPenal");
        txtPenal.setName("txtPenal");
        lblInstallmentNo.setName("lblInstallmentNo");
        txtInstallmentNo.setName("txtInstallmentNo");
        panSlab.setName("panSlab");
        lblDueOn.setName("lblDueOn");
        rdoMonthEnd.setName("rdoMonthEnd");
        rdoInstallmentDay.setName("rdoInstallmentDay");
                
    }

    /* Auto Generated Method - internationalize()
    This method used to assign display texts from
    the Resource Bundle File. */
    private void internationalize() {
        rdoSystemCalcValues_No.setText(resourceBundle.getString("rdoSystemCalcValues_No"));
        lblMaxNoOfPartialWithdrawalAllowed.setText(resourceBundle.getString("lblMaxNoOfPartialWithdrawalAllowed"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblOperatesLike.setText(resourceBundle.getString("lblOperatesLike"));
        rdoIntPayableOnExcessInstal_No.setText(resourceBundle.getString("rdoIntPayableOnExcessInstal_No"));
        lblMinDepositPeriod.setText(resourceBundle.getString("lblMinDepositPeriod"));
        rdoCanReceiveExcessInstal_yes.setText(resourceBundle.getString("rdoCanReceiveExcessInstal_yes"));
        lblInterestAfterMaturity.setText(resourceBundle.getString("lblInterestAfterMaturity"));
        lblCalcMaturityValue.setText(resourceBundle.getString("lblCalcMaturityValue"));
        btnTDSGLAcctHd.setText(resourceBundle.getString("btnTDSGLAcctHd"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblProdCurrency.setText(resourceBundle.getString("lblProdCurrency"));
        lblMaxNopfTimes.setText(resourceBundle.getString("lblMaxNopfTimes"));
        lblInterestType.setText(resourceBundle.getString("lblInterestType"));
        rdoPartialWithdrawalAllowed_No.setText(resourceBundle.getString("rdoPartialWithdrawalAllowed_No"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblIntDebitPLHead.setText(resourceBundle.getString("lblIntDebitPLHead"));
        lblSystemCalcValues.setText(resourceBundle.getString("lblSystemCalcValues"));
        rdoInterestAfterMaturity_No.setText(resourceBundle.getString("rdoInterestAfterMaturity_No"));
        rdoCalcMaturityValue_No.setText(resourceBundle.getString("rdoCalcMaturityValue_No"));
        rdoRecurringDepositToFixedDeposit_No.setText(resourceBundle.getString("rdoRecurringDepositToFixedDeposit_No"));
        rdoInstallmentInRecurringDepositAcct_Yes.setText(resourceBundle.getString("rdoInstallmentInRecurringDepositAcct_Yes"));
        rdoCalcTDS_Yes.setText(resourceBundle.getString("rdoCalcTDS_Yes"));

        rdoRegular.setText(resourceBundle.getString("rdoRegular"));
        rdoNRO.setText(resourceBundle.getString("rdoNRO"));
        rdoNRE.setText(resourceBundle.getString("rdoNRE"));
        rdoIntProvisioningApplicable_No.setText(resourceBundle.getString("rdoIntProvisioningApplicable_No"));

        lblPenaltyOnLateInstallmentsChargeble.setText(resourceBundle.getString("lblPenaltyOnLateInstallmentsChargeble"));
        lblDelayedFromAmt.setText(resourceBundle.getString("lblDelayedFromAmt"));
        lblDelayedToAmt.setText(resourceBundle.getString("lblDelayedToAmt"));
        lblDelayedChargesAmt.setText(resourceBundle.getString("lblDelayedChargesAmt"));
        lblDelayedChargesDet.setText(resourceBundle.getString("lblDelayedChargesDet"));
        lblIntCompoundingFreq.setText(resourceBundle.getString("lblIntCompoundingFreq"));
        lblInterestOnMaturedDepositsDays.setText(resourceBundle.getString("lblInterestOnMaturedDepositsDays"));
        lblCalcTDS.setText(resourceBundle.getString("lblCalcTDS"));
        //        lblPrematureWithdrawalDays.setText(resourceBundle.getString("lblPrematureWithdrawalDays"));
        rdoInterestAfterMaturity_Yes.setText(resourceBundle.getString("rdoInterestAfterMaturity_Yes"));
        lblNextInterestAppliedDate.setText(resourceBundle.getString("lblNextInterestAppliedDate"));
        rdoWithdrawalWithInterest_Yes.setText(resourceBundle.getString("rdoWithdrawalWithInterest_Yes"));
        lblDepositPerUnit.setText(resourceBundle.getString("lblDepositPerUnit"));
        lblTermDeposit.setText(resourceBundle.getString("lblTermDeposit"));
        lblMinIntToBePaid.setText(resourceBundle.getString("lblMinIntToBePaid"));
        rdoRenewalOfDepositAllowed_Yes.setText(resourceBundle.getString("rdoRenewalOfDepositAllowed_Yes"));
        rdoSystemCalcValues_Yes.setText(resourceBundle.getString("rdoSystemCalcValues_Yes"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        rdoCalcTDS_No.setText(resourceBundle.getString("rdoCalcTDS_No"));
        rdoDiscounted_No.setText(resourceBundle.getString("rdoDiscounted_No"));
        rdoDiscounted_Yes.setText(resourceBundle.getString("rdoDiscounted_Yes"));
        lblFixedDepositAcctHead.setText(resourceBundle.getString("lblFixedDepositAcctHead"));
        lblPenalCharges.setText(resourceBundle.getString("lblPenalCharges"));
        btnIntDebitPLHead.setText(resourceBundle.getString("btnIntDebitPLHead"));
        rdoTermDeposit_Yes.setText(resourceBundle.getString("rdoTermDeposit_Yes"));
        lblMaxDepositPeriod.setText(resourceBundle.getString("lblMaxDepositPeriod"));
        rdoProvisionOfInterest_Yes.setText(resourceBundle.getString("rdoProvisionOfInterest_Yes"));
        lblMinNoOfDays.setText(resourceBundle.getString("lblMinNoOfDays"));
        //        lblMinNoOfDays1.setText(resourceBundle.getString("lblMinNoOfDays1"));
        rdoAdjustIntOnDeposits_Yes.setText(resourceBundle.getString("rdoAdjustIntOnDeposits_Yes"));
        ((javax.swing.border.TitledBorder) panTax.getBorder()).setTitle(resourceBundle.getString("panTax"));
        ((javax.swing.border.TitledBorder) panAccountHead.getBorder()).setTitle(resourceBundle.getString("panAccountHead"));
        btnIntOnMaturedDepositAcctHead.setText(resourceBundle.getString("btnIntOnMaturedDepositAcctHead"));
        lblIntOnMaturedDepositAcctHead.setText(resourceBundle.getString("lblIntOnMaturedDepositAcctHead"));
        lblInstallmentToBeCharged.setText(resourceBundle.getString("lblInstallmentToBeCharged"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblNextIntProvisionalDate.setText(resourceBundle.getString("lblNextIntProvisionalDate"));
        lblMaturityDepositAcctHead.setText(resourceBundle.getString("lblMaturityDepositAcctHead"));
        ((javax.swing.border.TitledBorder) panInCaseOfLien.getBorder()).setTitle(resourceBundle.getString("panInCaseOfLien"));
        rdoTransferToMaturedDeposits_No.setText(resourceBundle.getString("rdoTransferToMaturedDeposits_No"));
        lblTDSGLAcctHd.setText(resourceBundle.getString("lblTDSGLAcctHd"));
        lblRoundOffCriteria.setText(resourceBundle.getString("lblRoundOffCriteria"));
        rdoFlexiFromSBCA_No.setText(resourceBundle.getString("rdoFlexiFromSBCA_No"));
        lblFlexiFromSBCA.setText(resourceBundle.getString("lblFlexiFromSBCA"));
        rdoWithdrawalWithInterest_No.setText(resourceBundle.getString("rdoWithdrawalWithInterest_No"));
        lblTransferToMaturedDeposits.setText(resourceBundle.getString("lblTransferToMaturedDeposits"));
        lblIntCriteria.setText(resourceBundle.getString("lblIntCriteria"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblSchemeIntroDate.setText(resourceBundle.getString("lblSchemeIntroDate"));
        lblSchemeClosingDate.setText(resourceBundle.getString("lblSchemeClosingDate"));
        lblExtnOfDepositBeforeMaturity.setText(resourceBundle.getString("lblExtnOfDepositBeforeMaturity"));
        lblAmtInMultiplesOf.setText(resourceBundle.getString("lblAmtInMultiplesOf"));
        lblIntProvisioningApplicable.setText(resourceBundle.getString("lblIntProvisioningApplicable"));
        lblIntApplnFreq.setText(resourceBundle.getString("lblIntApplnFreq"));
        rdoPenaltyOnLateInstallmentsChargeble_No.setText(resourceBundle.getString("rdoPenaltyOnLateInstallmentsChargeble_No"));
        lblAfterHowManyDaysDisplay.setText(resourceBundle.getString("lblAfterHowManyDaysDisplay"));
        lblMinAmtOfPAN.setText(resourceBundle.getString("lblMinAmtOfPAN"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblMinAmtOfPartialWithdrawalAllowed.setText(resourceBundle.getString("lblMinAmtOfPartialWithdrawalAllowed"));
        lblInstallmentInRecurringDepositAcct.setText(resourceBundle.getString("lblInstallmentInRecurringDepositAcct"));
        lblMaxAmtOfPartialWithdrawalAllowed.setText(resourceBundle.getString("lblMaxAmtOfPartialWithdrawalAllowed"));
        lblIntProvisioningFreq.setText(resourceBundle.getString("lblIntProvisioningFreq"));
        rdoAutoAdjustment_Yes.setText(resourceBundle.getString("rdoAutoAdjustment_Yes"));
        btnMaturityInterestRate.setText(resourceBundle.getString("btnMaturityInterestRate"));
        ((javax.swing.border.TitledBorder) panWithdrawal.getBorder()).setTitle(resourceBundle.getString("panWithdrawal"));
        rdoIntProvisioningApplicable_Yes.setText(resourceBundle.getString("rdoIntProvisioningApplicable_Yes"));
        lblLastInterestAppliedDate.setText(resourceBundle.getString("lblLastInterestAppliedDate"));
        rdoFlexiFromSBCA_Yes.setText(resourceBundle.getString("rdoFlexiFromSBCA_Yes"));
        rdoIntroducerReqd_No.setText(resourceBundle.getString("rdoIntroducerReqd_No"));
        lblProvisionOfInterest.setText(resourceBundle.getString("lblProvisionOfInterest"));
        rdoAmountRounded_Yes.setText(resourceBundle.getString("rdoAmountRounded_Yes"));
        lblLastIntProvisionalDate.setText(resourceBundle.getString("lblLastIntProvisionalDate"));
        btnMaturityInterestType.setText(resourceBundle.getString("btnMaturityInterestType"));
        lblAdjustPrincipleToLoan.setText(resourceBundle.getString("lblAdjustPrincipleToLoan"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblAlphaSuffix.setText(resourceBundle.getString("lblAlphaSuffix"));
        rdoInstallmentInRecurringDepositAcct_No.setText(resourceBundle.getString("rdoInstallmentInRecurringDepositAcct_No"));
        rdoCalcMaturityValue_Yes.setText(resourceBundle.getString("rdoCalcMaturityValue_Yes"));
        lblRecalcOfMaturityValue.setText(resourceBundle.getString("lblRecalcOfMaturityValue"));
        rdoTransferToMaturedDeposits_Yes.setText(resourceBundle.getString("rdoTransferToMaturedDeposits_Yes"));
        lblMaxAmtPartialWithdrawalPercent.setText(resourceBundle.getString("lblMaxAmtPartialWithdrawalPercent"));
        btnIntPaybleGLHead.setText(resourceBundle.getString("btnIntPaybleGLHead"));
        lblAfterNoDays.setText(resourceBundle.getString("lblAfterNoDays"));
        lblLimitForBulkDeposit.setText(resourceBundle.getString("lblLimitForBulkDeposit"));
        lblWithdrawalsInMultiplesOf.setText(resourceBundle.getString("lblWithdrawalsInMultiplesOf"));
        rdoIntroducerReqd_Yes.setText(resourceBundle.getString("rdoIntroducerReqd_Yes"));
        lblPartialWithdrawalAllowed.setText(resourceBundle.getString("lblPartialWithdrawalAllowed"));
        lblIntType.setText(resourceBundle.getString("lblIntType"));
        lblIntPeriodForBackDatedRenewal.setText(resourceBundle.getString("lblIntPeriodForBackDatedRenewal"));
        rdoPayInterestOnHoliday_No.setText(resourceBundle.getString("rdoPayInterestOnHoliday_No"));
        lblCutOffDayForPaymentOfInstal.setText(resourceBundle.getString("lblCutOffDayForPaymentOfInstal"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnIntProvisioningAcctHd.setText(resourceBundle.getString("btnIntProvisioningAcctHd"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblIntProvisioningAcctHd.setText(resourceBundle.getString("lblIntProvisioningAcctHd"));
        //        ((javax.swing.border.TitledBorder)panRenewalClosure.getBorder()).setTitle(resourceBundle.getString("panRenewalClosure"));
        rdoAdjustPrincipleToLoan_No.setText(resourceBundle.getString("rdoAdjustPrincipleToLoan_No"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblIntPaybleGLHead.setText(resourceBundle.getString("lblIntPaybleGLHead"));

        lblCommisionPaybleGLHead.setText(resourceBundle.getString("lblCommisionPaybleGLHead"));
        //        txtCommisionPaybleGLHead.setText(resourceBundle.getString("txtCommisionPaybleGLHead"));
        btnCommisionPaybleGLHead.setText(resourceBundle.getString("btnCommisionPaybleGLHead"));

        lblAcctNumberPattern.setText(resourceBundle.getString("lblAcctNumberPattern"));
        btnMaturityDepositAcctHead.setText(resourceBundle.getString("btnMaturityDepositAcctHead"));
        rdoExtnOfDepositBeforeMaturity_No.setText(resourceBundle.getString("rdoExtnOfDepositBeforeMaturity_No"));
        btnAcctHeadForFloatAcct.setText(resourceBundle.getString("btnAcctHeadForFloatAcct"));
        lblChangeValue.setText(resourceBundle.getString("lblChangeValue"));
        rdoProvisionOfInterest_No.setText(resourceBundle.getString("rdoProvisionOfInterest_No"));
        rdoLastInstallmentAllowed_No.setText(resourceBundle.getString("rdoLastInstallmentAllowed_No"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblDesc.setText(resourceBundle.getString("lblDesc"));
        lblLastInstallmentAllowed.setText(resourceBundle.getString("lblLastInstallmentAllowed"));
        lblMaturityDateAfterLastInstalPaid.setText(resourceBundle.getString("lblMaturityDateAfterLastInstalPaid"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        //        lblMaturityDate1.setText(resourceBundle.getString("lblMaturityDate1"));
        lblProductID.setText(resourceBundle.getString("lblProductID"));
        rdoAmountRounded_No.setText(resourceBundle.getString("rdoAmountRounded_No"));
        lblAdjustIntOnDeposits.setText(resourceBundle.getString("lblAdjustIntOnDeposits"));
        rdoTermDeposit_No.setText(resourceBundle.getString("rdoTermDeposit_No"));
        rdoCanReceiveExcessInstal_No.setText(resourceBundle.getString("rdoCanReceiveExcessInstal_No"));
        rdoLastInstallmentAllowed_Yes.setText(resourceBundle.getString("rdoLastInstallmentAllowed_Yes"));
        lblAcctHeadForFloatAcct.setText(resourceBundle.getString("lblAcctHeadForFloatAcct"));
        lblRecurringDepositToFixedDeposit.setText(resourceBundle.getString("lblRecurringDepositToFixedDeposit"));
        lblRenewalOfDepositAllowed.setText(resourceBundle.getString("lblRenewalOfDepositAllowed"));
        lblMinDepositAmt.setText(resourceBundle.getString("lblMinDepositAmt"));
        lblAfterHowManyDays.setText(resourceBundle.getString("lblAfterHowManyDays"));
        rdoRecalcOfMaturityValue_No.setText(resourceBundle.getString("rdoRecalcOfMaturityValue_No"));
        rdoAutoAdjustment_No.setText(resourceBundle.getString("rdoAutoAdjustment_No"));
        lblIntRoundOff.setText(resourceBundle.getString("lblIntRoundOff"));
        lblIntPayableOnExcessInstal.setText(resourceBundle.getString("lblIntPayableOnExcessInstal"));
        lblIntCalcMethod.setText(resourceBundle.getString("lblIntCalcMethod"));
        rdoAdjustPrincipleToLoan_Yes.setText(resourceBundle.getString("rdoAdjustPrincipleToLoan_Yes"));
        btnIntProvisionOfMaturedDeposit.setText(resourceBundle.getString("btnIntProvisionOfMaturedDeposit"));
        rdoAdjustIntOnDeposits_No.setText(resourceBundle.getString("rdoAdjustIntOnDeposits_No"));
        lblProvisioningLevel.setText(resourceBundle.getString("lblProvisioningLevel"));
        rdoAutoRenewalAllowed_Yes.setText(resourceBundle.getString("rdoAutoRenewalAllowed_Yes"));
        btnAcctHead.setText(resourceBundle.getString("btnAcctHead"));
        lblAutoAdjustment.setText(resourceBundle.getString("lblAutoAdjustment"));
        lblAmountRounded.setText(resourceBundle.getString("lblAmountRounded"));
        lblIntMaintainedAsPartOf.setText(resourceBundle.getString("lblIntMaintainedAsPartOf"));
        rdoPartialWithdrawalAllowed_Yes.setText(resourceBundle.getString("rdoPartialWithdrawalAllowed_Yes"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblPayInterestOnHoliday.setText(resourceBundle.getString("lblPayInterestOnHoliday"));
        lblAdvanceMaturityNoticeGenPeriod.setText(resourceBundle.getString("lblAdvanceMaturityNoticeGenPeriod"));
        lblCanReceiveExcessInstal.setText(resourceBundle.getString("lblCanReceiveExcessInstal"));
        lblPrematureWithdrawal.setText(resourceBundle.getString("lblPrematureWithdrawal"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblWithdrawalWithInterest.setText(resourceBundle.getString("lblWithdrawalWithInterest"));
        lblServiceCharge.setText(resourceBundle.getString("lblServiceCharge"));
        lblInsBeyondMaturityDtYesorNo.setText(resourceBundle.getString("lblInsBeyondMaturityDtYesorNo"));
        lblNoOfPartialWithdrawalAllowed.setText(resourceBundle.getString("lblNoOfPartialWithdrawalAllowed"));
        rdoRenewalOfDepositAllowed_No.setText(resourceBundle.getString("rdoRenewalOfDepositAllowed_No"));
        rdoIntPayableOnExcessInstal_Yes.setText(resourceBundle.getString("rdoIntPayableOnExcessInstal_Yes"));
        lblNoOfDays.setText(resourceBundle.getString("lblNoOfDays"));
        ((javax.swing.border.TitledBorder) panInterestPayment.getBorder()).setTitle(resourceBundle.getString("panInterestPayment"));
        btnFixedDepositAcctHead.setText(resourceBundle.getString("btnFixedDepositAcctHead"));
        lblMaxAmtOfCashPayment.setText(resourceBundle.getString("lblMaxAmtOfCashPayment"));
        lblMaxDepositAmt.setText(resourceBundle.getString("lblMaxDepositAmt"));
        rdoAutoRenewalAllowed_No.setText(resourceBundle.getString("rdoAutoRenewalAllowed_No"));
        lblInterestOnMaturedDeposits.setText(resourceBundle.getString("lblInterestOnMaturedDeposits"));
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setText(resourceBundle.getString("rdoPenaltyOnLateInstallmentsChargeble_Yes"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblIntroducerReqd.setText(resourceBundle.getString("lblIntroducerReqd"));
        rdoExtnOfDepositBeforeMaturity_Yes.setText(resourceBundle.getString("rdoExtnOfDepositBeforeMaturity_Yes"));
        rdoPayInterestOnHoliday_Yes.setText(resourceBundle.getString("rdoPayInterestOnHoliday_Yes"));
        lblPeriodInMultiplesOf.setText(resourceBundle.getString("lblPeriodInMultiplesOf"));
        rdoRecurringDepositToFixedDeposit_Yes.setText(resourceBundle.getString("rdoRecurringDepositToFixedDeposit_Yes"));
        rdoRecalcOfMaturityValue_Yes.setText(resourceBundle.getString("rdoRecalcOfMaturityValue_Yes"));
        lblIntProvisionOfMaturedDeposit.setText(resourceBundle.getString("lblIntProvisionOfMaturedDeposit"));
        lblAcctHd.setText(resourceBundle.getString("lblAcctHd"));
        lblAutoRenewalAllowed.setText(resourceBundle.getString("lblAutoRenewalAllowed"));
        lblDailyDepositCalc.setText(resourceBundle.getString("lblDailyDepositCalc"));
        lblWeekly.setText(resourceBundle.getString("lblWeekly"));
        lblTypesOfDeposit.setText(resourceBundle.getString("lblTypesOfDeposit"));
        lblStaffAccount.setText(resourceBundle.getString("lblStaffAccount"));
        rdoRegular.setText(resourceBundle.getString("rdoRegular"));

        lblDate.setText(resourceBundle.getString("lblDate"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblFromAmount.setText(resourceBundle.getString("lblFromAmount"));
        lblToAmount.setText(resourceBundle.getString("lblToAmount"));
        lblFromPeriod.setText(resourceBundle.getString("lblFromPeriod"));
        lblToPeriod.setText(resourceBundle.getString("lblToPeriod"));
        lblRateInterest.setText(resourceBundle.getString("lblRateInterest"));
        btnTabNew.setText(resourceBundle.getString("btnTabNew"));
        btnTabSave.setText(resourceBundle.getString("btnTabSave"));
        btnTabDelete.setText(resourceBundle.getString("btnTabDelete"));
        lblIntPeriodForBackDatedRenewal.setText(resourceBundle.getString("lblIntPeriodForBackDatedRenewal"));
        lblMinimumRenewalDeposit.setText(resourceBundle.getString("lblMinimumRenewalDeposit"));
        lblRenewedclosedbefore.setText(resourceBundle.getString("lblRenewedclosedbefore"));
        lblclosedwithinperiod.setText(resourceBundle.getString("lblclosedwithinperiod"));
        lblInterestalreadyPaid.setText(resourceBundle.getString("lblInterestalreadyPaid"));
        lblInterestrateappliedoverdue.setText(resourceBundle.getString("lblInterestrateappliedoverdue"));
        lblRateAsOnDateOfRenewal.setText(resourceBundle.getString("lblRateAsOnDateOfRenewal"));
        lblRateAsOnDateOfMaturity.setText(resourceBundle.getString("lblRateAsOnDateOfMaturity"));
        lblBothRateAvail.setText(resourceBundle.getString("lblBothRateAvail"));
        lblOneRateAvail.setText(resourceBundle.getString("lblOneRateAvail"));
        lblBothRateNotAvail.setText(resourceBundle.getString("lblBothRateNotAvail"));
        lblAutoRenewalAllowed.setText(resourceBundle.getString("lblAutoRenewalAllowed"));
        lblMaxNopfTimes.setText(resourceBundle.getString("lblMaxNopfTimes"));
        lblAutoRenewalAllowed1.setText(resourceBundle.getString("lblAutoRenewalAllowed1"));
        lblMaxNopfTimes1.setText(resourceBundle.getString("lblMaxNopfTimes1"));
        lblIntType.setText(resourceBundle.getString("lblIntType"));
        lblMaturityDate.setText(resourceBundle.getString("lblMaturityDate"));
        lblMinNoOfDays.setText(resourceBundle.getString("lblMinNoOfDays"));
        lblIntCriteria.setText(resourceBundle.getString("lblIntCriteria"));
        rdoClosedwithinperiod_Yes.setText(resourceBundle.getString("rdoClosedwithinperiod_Yes"));
        rdoClosedwithinperiod_No.setText(resourceBundle.getString("rdoClosedwithinperiod_No"));
        rdoInterestalreadyPaid_Yes.setText(resourceBundle.getString("rdoInterestalreadyPaid_Yes"));
        rdoInterestalreadyPaid_No.setText(resourceBundle.getString("rdoInterestalreadyPaid_No"));
        rdoInterestrateappliedoverdue_Yes.setText(resourceBundle.getString("rdoInterestrateappliedoverdue_Yes"));
        rdoInterestrateappliedoverdue_No.setText(resourceBundle.getString("rdoInterestrateappliedoverdue_No"));
        rdoSBRateOneRate_Yes.setText(resourceBundle.getString("rdoSBRateOneRate_Yes"));
        rdoSBRateOneRate_No.setText(resourceBundle.getString("rdoSBRateOneRate_No"));
        rdoBothRateNotAvail_Yes.setText(resourceBundle.getString("rdoBothRateNotAvail_Yes"));
        rdoBothRateNotAvail_No.setText(resourceBundle.getString("rdoBothRateNotAvail_No"));
        rdoAutoRenewalAllowed_Yes.setText(resourceBundle.getString("rdoAutoRenewalAllowed_Yes"));
        rdoAutoRenewalAllowed_No.setText(resourceBundle.getString("rdoAutoRenewalAllowed_No"));
        rdoSameNoRenewalAllowed_Yes.setText(resourceBundle.getString("rdoSameNoRenewalAllowed_Yes"));
        rdoSameNoRenewalAllowed_No.setText(resourceBundle.getString("rdoSameNoRenewalAllowed_No"));
        lblRenewalOfDepositAllowed1.setText(resourceBundle.getString("lblRenewalOfDepositAllowed1"));
        lblBasedOnDepRate.setText(resourceBundle.getString("lblBasedOnDepRate"));
        //        tblInterestTable.setText(resourceBundle.getString("tblInterestTable"));
        lblProductType.setText(resourceBundle.getString("lblProductType"));
        lbInstallmentFrom.setText(resourceBundle.getString("lbInstallmentFrom"));
        lblInstallmentTo.setText(resourceBundle.getString("lblInstallmentTo"));
        lblPenal.setText(resourceBundle.getString("lblPenal"));
        lblInstallmentNo.setText(resourceBundle.getString("lblInstallmentNo"));
        lblDueOn.setText(resourceBundle.getString("lblDueOn"));
        rdoMonthEnd.setText(resourceBundle.getString("rdoMonthEnd"));
        rdoInstallmentDay.setText(resourceBundle.getString("rdoInstallmentDay"));
        
    }

    /* Auto Generated Method - setMandatoryHashMap()
    This method list out all the Input Fields available in the UI.
    It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("txtDesc", new Boolean(true));
        mandatoryMap.put("cboOperatesLike", new Boolean(true));
        mandatoryMap.put("rdoCalcTDS_Yes", new Boolean(true));
        mandatoryMap.put("rdoPayInterestOnHoliday_Yes", new Boolean(true));
        mandatoryMap.put("rdoAmountRounded_Yes", new Boolean(true));
        mandatoryMap.put("cboRoundOffCriteria", new Boolean(true));
        mandatoryMap.put("rdoInterestAfterMaturity_Yes", new Boolean(true));
        mandatoryMap.put("cboMaturityInterestType", new Boolean(true));
        mandatoryMap.put("cboMaturityInterestRate", new Boolean(true));
        mandatoryMap.put("txtPeriodInMultiplesOf", new Boolean(true));
        mandatoryMap.put("cboPeriodInMultiplesOf", new Boolean(true));
        mandatoryMap.put("txtMaxDepositPeriod", new Boolean(true));
        mandatoryMap.put("cboMaxDepositPeriod", new Boolean(true));
        mandatoryMap.put("txtMinDepositPeriod", new Boolean(true));
        mandatoryMap.put("cboMinDepositPeriod", new Boolean(true));
        mandatoryMap.put("txtDepositPerUnit", new Boolean(true));
        mandatoryMap.put("cboDepositPerUnit", new Boolean(true));
        mandatoryMap.put("txtAmtInMultiplesOf", new Boolean(true));
        mandatoryMap.put("txtMinDepositAmt", new Boolean(true));
        mandatoryMap.put("txtMaxDepositAmt", new Boolean(true));
        mandatoryMap.put("txtAcctHd", new Boolean(true));
        mandatoryMap.put("cboProdCurrency", new Boolean(true));
        mandatoryMap.put("txtInterestOnMaturedDeposits", new Boolean(true));
        mandatoryMap.put("txtAfterHowManyDays", new Boolean(true));
        mandatoryMap.put("txtAlphaSuffix", new Boolean(true));
        mandatoryMap.put("txtMaxAmtOfCashPayment", new Boolean(true));
        mandatoryMap.put("txtMinAmtOfPAN", new Boolean(true));
        mandatoryMap.put("txtAcctNumberPattern", new Boolean(true));
        mandatoryMap.put("txtLastAcctNumber", new Boolean(true));
        mandatoryMap.put("txtSuffix", new Boolean(true));
        mandatoryMap.put("txtLimitForBulkDeposit", new Boolean(true));
        mandatoryMap.put("rdoTransferToMaturedDeposits_Yes", new Boolean(true));
        mandatoryMap.put("rdoAutoAdjustment_Yes", new Boolean(true));
        mandatoryMap.put("rdoAdjustIntOnDeposits_No", new Boolean(true));
        mandatoryMap.put("rdoAdjustPrincipleToLoan_Yes", new Boolean(true));
        mandatoryMap.put("rdoExtnOfDepositBeforeMaturity_Yes", new Boolean(true));
        mandatoryMap.put("txtAdvanceMaturityNoticeGenPeriod", new Boolean(true));
        mandatoryMap.put("txtPrematureWithdrawal", new Boolean(true));
        mandatoryMap.put("rdoFlexiFromSBCA_Yes", new Boolean(true));
        mandatoryMap.put("rdoTermDeposit_Yes", new Boolean(true));
        mandatoryMap.put("rdoIntroducerReqd_Yes", new Boolean(true));
        mandatoryMap.put("rdoSystemCalcValues_Yes", new Boolean(true));
        mandatoryMap.put("rdoCalcMaturityValue_Yes", new Boolean(true));
        mandatoryMap.put("rdoProvisionOfInterest_Yes", new Boolean(true));
        mandatoryMap.put("tdtSchemeIntroDate", new Boolean(true));
        mandatoryMap.put("tdtSchemeClosingDate", new Boolean(false));
        mandatoryMap.put("txtAfterNoDays", new Boolean(true));
        mandatoryMap.put("txtNoOfPartialWithdrawalAllowed", new Boolean(true));
        mandatoryMap.put("txtServiceCharge", new Boolean(true));
        mandatoryMap.put("txtMaxAmtOfPartialWithdrawalAllowed", new Boolean(true));
        mandatoryMap.put("txtMaxNoOfPartialWithdrawalAllowed", new Boolean(true));
        mandatoryMap.put("txtMinAmtOfPartialWithdrawalAllowed", new Boolean(true));
        mandatoryMap.put("txtWithdrawalsInMultiplesOf", new Boolean(true));
        mandatoryMap.put("rdoPartialWithdrawalAllowed_Yes", new Boolean(true));
        mandatoryMap.put("rdoWithdrawalWithInterest_Yes", new Boolean(true));
        mandatoryMap.put("cboIntCalcMethod", new Boolean(true));
        mandatoryMap.put("cboIntMaintainedAsPartOf", new Boolean(true));
        mandatoryMap.put("rdoIntProvisioningApplicable_Yes", new Boolean(true));
        mandatoryMap.put("cboIntProvisioningFreq", new Boolean(true));
        mandatoryMap.put("cboProvisioningLevel", new Boolean(true));
        mandatoryMap.put("cboNoOfDays", new Boolean(true));
        mandatoryMap.put("cboIntCompoundingFreq", new Boolean(true));
        mandatoryMap.put("cboIntApplnFreq", new Boolean(true));
        mandatoryMap.put("tdtNextInterestAppliedDate", new Boolean(true));
        mandatoryMap.put("tdtLastInterestAppliedDate", new Boolean(true));
        mandatoryMap.put("tdtNextIntProvisionalDate", new Boolean(true));
        mandatoryMap.put("tdtLastIntProvisionalDate", new Boolean(true));
        mandatoryMap.put("txtMinIntToBePaid", new Boolean(true));
        mandatoryMap.put("cboIntRoundOff", new Boolean(true));
        mandatoryMap.put("rdoRecalcOfMaturityValue_Yes", new Boolean(true));
        mandatoryMap.put("txtTDSGLAcctHd", new Boolean(true));
        mandatoryMap.put("txtIntProvisioningAcctHd", new Boolean(true));
        mandatoryMap.put("txtIntOnMaturedDepositAcctHead", new Boolean(true));
        mandatoryMap.put("txtFixedDepositAcctHead", new Boolean(true));
        //        mandatoryMap.put("txtPenalCharges", new Boolean(true));
        // mandatoryMap.put("txtInterestRecoveryAcHd", new Boolean(true));
        mandatoryMap.put("txttMaturityDepositAcctHead", new Boolean(true));
        mandatoryMap.put("txtIntProvisionOfMaturedDeposit", new Boolean(true));
        mandatoryMap.put("txtIntPaybleGLHead", new Boolean(true));
        mandatoryMap.put("txtIntDebitPLHead", new Boolean(true));
        mandatoryMap.put("txtAcctHeadForFloatAcct", new Boolean(true));
        mandatoryMap.put("cboIntCriteria", new Boolean(true));
        mandatoryMap.put("rdoAutoRenewalAllowed_Yes", new Boolean(true));
        mandatoryMap.put("rdoRenewalOfDepositAllowed_Yes", new Boolean(true));
        mandatoryMap.put("txtMinNoOfDays", new Boolean(true));
        mandatoryMap.put("cboMinNoOfDays", new Boolean(true));
        mandatoryMap.put("txtMaxNopfTimes", new Boolean(true));
        mandatoryMap.put("cboIntPeriodForBackDatedRenewal", new Boolean(true));
        mandatoryMap.put("txtIntPeriodForBackDatedRenewal", new Boolean(true));
        mandatoryMap.put("cboIntType", new Boolean(true));
        mandatoryMap.put("cboMaxPeriodMDt", new Boolean(true));
        mandatoryMap.put("txtMaxPeriodMDt", new Boolean(true));
        mandatoryMap.put("rdoPenaltyOnLateInstallmentsChargeble_Yes", new Boolean(true));
        mandatoryMap.put("txtMaturityDateAfterLastInstalPaid", new Boolean(true));
        mandatoryMap.put("cboMaturityDateAfterLastInstalPaid", new Boolean(true));
        mandatoryMap.put("rdoCanReceiveExcessInstal_yes", new Boolean(true));
        mandatoryMap.put("rdoInstallmentInRecurringDepositAcct_Yes", new Boolean(true));
        mandatoryMap.put("cboInstallmentToBeCharged", new Boolean(true));
        //added for daily deposit scheme
        mandatoryMap.put("cboDepositsFreqency", new Boolean(true));
        mandatoryMap.put("lblDepositsFrequency", new Boolean(true));

        mandatoryMap.put("txtPeriodInMultiplesOf", new Boolean(true));
        mandatoryMap.put("cboChangeValue", new Boolean(true));
        mandatoryMap.put("rdoIntPayableOnExcessInstal_Yes", new Boolean(true));
        mandatoryMap.put("rdoRecurringDepositToFixedDeposit_Yes", new Boolean(true));
        mandatoryMap.put("rdoLastInstallmentAllowed_Yes", new Boolean(true));
        mandatoryMap.put("cboCutOffDayForPaymentOfInstal", new Boolean(true));
        mandatoryMap.put("cboInterestType", new Boolean(false));
        mandatoryMap.put("txtCutOffDayForPaymentOfInstal", new Boolean(true));
        mandatoryMap.put("txtCommisionPaybleGLHead", new Boolean(true));
        mandatoryMap.put("rdoRegular", new Boolean(true));
        mandatoryMap.put("rdoStaffAccount_Yes", new Boolean(true));
        //        mandatoryMap.put("cboFromAmount", new Boolean(true));
        //        mandatoryMap.put("cboFromPeriod", new Boolean(true));
        //        mandatoryMap.put("cboToPeriod", new Boolean(true));
        //        mandatoryMap.put("tdtDate", new Boolean(true));
        //        mandatoryMap.put("tdtToDate", new Boolean(true));
        //        mandatoryMap.put("txtFromPeriod", new Boolean(true));
        //        mandatoryMap.put("txtToPeriod", new Boolean(true));
        //        mandatoryMap.put("txtCommisionRate", new Boolean(true));
        //        mandatoryMap.put("cboToAmount", new Boolean(true));
        //        mandatoryMap.put("txtFromAmount", new Boolean(true));
        //        mandatoryMap.put("txtToAmount", new Boolean(false));
        //        mandatoryMap.put("cboDepositsFrequency",new Boolean(true));

        mandatoryMap.put("cboFromAmount", new Boolean(true));
        mandatoryMap.put("cboToAmount", new Boolean(true));
        mandatoryMap.put("cboFromPeriod", new Boolean(true));
        mandatoryMap.put("cboToPeriod", new Boolean(true));
        //        mandatoryMap.put("txtFromAmount", new Boolean (true));
        //        mandatoryMap.put("txtToAmount", new Boolean (true));
        mandatoryMap.put("txtFromPeriod", new Boolean(true));
        mandatoryMap.put("txtToPeriod", new Boolean(true));
        mandatoryMap.put("txtCommisionRate", new Boolean(true));
        mandatoryMap.put("tdtDate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("txtPostageAcHd", new Boolean(true));

    }

    /* Auto Generated Method - getMandatoryHashMap()
    Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgCalcTDS = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayInterestOnHoliday = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgAmountRounded = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgInterestAfterMaturity = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgCalcMaturityValue = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTransferToMaturedDeposits = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgProvisionOfInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgAdjustPrincipleToLoan = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgAdjustIntOnDeposits = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgAutoAdjustment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgWithdrawalWithInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgExtnOfDepositBeforeMaturity = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgIntroducerReqd = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSystemCalcValues = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgIntProvisioningApplicable = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTDSGLAcctHd = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgRecalcOfMaturityValue = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgRenewalOfDepositAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgAutoRenewalAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPenaltyOnLateInstallmentsChargeble = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgLastInstallmentAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgRecurringDepositToFixedDeposit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgInstallmentInRecurringDepositAcct = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgCanReceiveExcessInstal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgIntPayableOnExcessInstal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgFlexiFromSBCA = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTermDeposit = new com.see.truetransact.uicomponent.CButtonGroup();
        tdtMaturityDate = new com.see.truetransact.uicomponent.CDateField();
        tdtPresentDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromDepositDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDepositDate = new com.see.truetransact.uicomponent.CDateField();
        lblChosenDate = new com.see.truetransact.uicomponent.CLabel();
        tdtChosenDate = new com.see.truetransact.uicomponent.CDateField();
        rdgDiscounted_Rate = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSameNoDepositAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDailyDepositCalculation = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTypesOfDeposit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgStaffAccount = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPartialWithdrawlAllowedForDD = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgWithPeriod = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDoublingScheme = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgServiceCharge = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgInsBeyondMaturityDt = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgclosedwithinperiod = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgIntRateforOverduePeriod = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgOneRateAvail = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBothRateNotAvail = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgExtensionBeyond = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgExtensionPeneal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgInterestalreadyPaid = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgirregularRdApply = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrematureClosureApply = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgExtensionDepositBeyond = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPenalIntOnWithdrawal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDueOn = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrDepositsProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panDepositsProduct = new com.see.truetransact.uicomponent.CPanel();
        tabDepositsProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panScheme = new com.see.truetransact.uicomponent.CPanel();
        panAcHd = new com.see.truetransact.uicomponent.CPanel();
        lblAcctHd = new com.see.truetransact.uicomponent.CLabel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        lblDesc = new com.see.truetransact.uicomponent.CLabel();
        txtDesc = new com.see.truetransact.uicomponent.CTextField();
        lblOperatesLike = new com.see.truetransact.uicomponent.CLabel();
        cboOperatesLike = new com.see.truetransact.uicomponent.CComboBox();
        sptOperatesLike = new com.see.truetransact.uicomponent.CSeparator();
        panCalcTDS = new com.see.truetransact.uicomponent.CPanel();
        rdoCalcTDS_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCalcTDS_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCalcTDS = new com.see.truetransact.uicomponent.CLabel();
        lblPayInterestOnHoliday = new com.see.truetransact.uicomponent.CLabel();
        lblAmountRounded = new com.see.truetransact.uicomponent.CLabel();
        panPayInterestOnHoliday = new com.see.truetransact.uicomponent.CPanel();
        rdoPayInterestOnHoliday_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPayInterestOnHoliday_No = new com.see.truetransact.uicomponent.CRadioButton();
        panAmountRounded = new com.see.truetransact.uicomponent.CPanel();
        rdoAmountRounded_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAmountRounded_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblRoundOffCriteria = new com.see.truetransact.uicomponent.CLabel();
        cboRoundOffCriteria = new com.see.truetransact.uicomponent.CComboBox();
        panInterestAfterMaturity = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestAfterMaturity_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestAfterMaturity_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblInterestAfterMaturity = new com.see.truetransact.uicomponent.CLabel();
        btnMaturityInterestType = new com.see.truetransact.uicomponent.CLabel();
        cboMaturityInterestType = new com.see.truetransact.uicomponent.CComboBox();
        btnMaturityInterestRate = new com.see.truetransact.uicomponent.CLabel();
        cboMaturityInterestRate = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositPerUnit = new com.see.truetransact.uicomponent.CLabel();
        panPeriodInMultiplesOf = new com.see.truetransact.uicomponent.CPanel();
        txtPeriodInMultiplesOf = new com.see.truetransact.uicomponent.CTextField();
        cboPeriodInMultiplesOf = new com.see.truetransact.uicomponent.CComboBox();
        lblMinDepositPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblMaxDepositPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblPeriodInMultiplesOf = new com.see.truetransact.uicomponent.CLabel();
        panMaxDepositPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtMaxDepositPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboMaxDepositPeriod = new com.see.truetransact.uicomponent.CComboBox();
        panMinDepositPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtMinDepositPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboMinDepositPeriod = new com.see.truetransact.uicomponent.CComboBox();
        panDepositPerUnit = new com.see.truetransact.uicomponent.CPanel();
        txtDepositPerUnit = new com.see.truetransact.uicomponent.CTextField();
        lblMinDepositAmt = new com.see.truetransact.uicomponent.CLabel();
        txtAmtInMultiplesOf = new com.see.truetransact.uicomponent.CTextField();
        lblMaxDepositAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMinDepositAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAmtInMultiplesOf = new com.see.truetransact.uicomponent.CLabel();
        txtMaxDepositAmt = new com.see.truetransact.uicomponent.CTextField();
        panAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtAcctHd = new com.see.truetransact.uicomponent.CTextField();
        btnAcctHead = new com.see.truetransact.uicomponent.CButton();
        lblProdCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboProdCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblInterestOnMaturedDeposits = new com.see.truetransact.uicomponent.CLabel();
        txtInterestOnMaturedDeposits = new com.see.truetransact.uicomponent.CTextField();
        lblAfterHowManyDays = new com.see.truetransact.uicomponent.CLabel();
        panAfterHowManyDays = new com.see.truetransact.uicomponent.CPanel();
        txtAfterHowManyDays = new com.see.truetransact.uicomponent.CTextField();
        lblInterestOnMaturedDepositsDays = new com.see.truetransact.uicomponent.CLabel();
        panDiscounted = new com.see.truetransact.uicomponent.CPanel();
        rdoDiscounted_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDiscounted_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblDiscounted = new com.see.truetransact.uicomponent.CLabel();
        panIntroducerReqd = new com.see.truetransact.uicomponent.CPanel();
        rdoIntroducerReqd_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIntroducerReqd_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIntroducerReqd = new com.see.truetransact.uicomponent.CLabel();
        panTypeOfDeposit = new com.see.truetransact.uicomponent.CPanel();
        rdoRegular = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNRO = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNRE = new com.see.truetransact.uicomponent.CRadioButton();
        lblTypesOfDeposit = new com.see.truetransact.uicomponent.CLabel();
        panWithPeriod = new com.see.truetransact.uicomponent.CPanel();
        rdoWithPeriod_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWithPeriod_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblWithPeriod = new com.see.truetransact.uicomponent.CLabel();
        panDoublingScheme = new com.see.truetransact.uicomponent.CPanel();
        rdoDoublingScheme_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDoublingScheme_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblDoublingScheme = new com.see.truetransact.uicomponent.CLabel();
        txtDoublingCount = new com.see.truetransact.uicomponent.CTextField();
        chkIsGroupDeposit = new com.see.truetransact.uicomponent.CCheckBox();
        panProvisionOfInterest = new com.see.truetransact.uicomponent.CPanel();
        lblFlexiFromSBCA = new com.see.truetransact.uicomponent.CLabel();
        lblExtnOfDepositBeforeMaturity = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureWithdrawal = new com.see.truetransact.uicomponent.CLabel();
        lblTermDeposit = new com.see.truetransact.uicomponent.CLabel();
        lblAlphaSuffix = new com.see.truetransact.uicomponent.CLabel();
        txtAlphaSuffix = new com.see.truetransact.uicomponent.CTextField();
        lblMaxAmtOfCashPayment = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmtOfCashPayment = new com.see.truetransact.uicomponent.CTextField();
        lblMinAmtOfPAN = new com.see.truetransact.uicomponent.CLabel();
        txtMinAmtOfPAN = new com.see.truetransact.uicomponent.CTextField();
        lblAcctNumberPattern = new com.see.truetransact.uicomponent.CLabel();
        lblSchemeIntroDate = new com.see.truetransact.uicomponent.CLabel();
        lblLimitForBulkDeposit = new com.see.truetransact.uicomponent.CLabel();
        txtLimitForBulkDeposit = new com.see.truetransact.uicomponent.CTextField();
        lblAdvanceMaturityNoticeGenPeriod = new com.see.truetransact.uicomponent.CLabel();
        panTransferToMaturedDeposits = new com.see.truetransact.uicomponent.CPanel();
        rdoTransferToMaturedDeposits_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransferToMaturedDeposits_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransferToMaturedDeposits = new com.see.truetransact.uicomponent.CLabel();
        panInCaseOfLien = new com.see.truetransact.uicomponent.CPanel();
        lblAutoAdjustment = new com.see.truetransact.uicomponent.CLabel();
        lblAdjustIntOnDeposits = new com.see.truetransact.uicomponent.CLabel();
        lblAdjustPrincipleToLoan = new com.see.truetransact.uicomponent.CLabel();
        rdoAutoAdjustment_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAutoAdjustment_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdjustIntOnDeposits_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdjustIntOnDeposits_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdjustPrincipleToLoan_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdjustPrincipleToLoan_No = new com.see.truetransact.uicomponent.CRadioButton();
        panExtnOfDepositBeforeMaturity = new com.see.truetransact.uicomponent.CPanel();
        rdoExtnOfDepositBeforeMaturity_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExtnOfDepositBeforeMaturity_No = new com.see.truetransact.uicomponent.CRadioButton();
        panAdvanceMaturityNoticeGenPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtAdvanceMaturityNoticeGenPeriod = new com.see.truetransact.uicomponent.CTextField();
        lblAfterHowManyDaysDisplay = new com.see.truetransact.uicomponent.CLabel();
        panFlexiFromSBCA = new com.see.truetransact.uicomponent.CPanel();
        rdoFlexiFromSBCA_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFlexiFromSBCA_No = new com.see.truetransact.uicomponent.CRadioButton();
        panTermDeposit = new com.see.truetransact.uicomponent.CPanel();
        rdoTermDeposit_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTermDeposit_No = new com.see.truetransact.uicomponent.CRadioButton();
        panSystemCalcValues = new com.see.truetransact.uicomponent.CPanel();
        rdoSystemCalcValues_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSystemCalcValues_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSystemCalcValues_Print_accOpen = new com.see.truetransact.uicomponent.CRadioButton();
        lblSystemCalcValues = new com.see.truetransact.uicomponent.CLabel();
        panCalcMaturityValue = new com.see.truetransact.uicomponent.CPanel();
        rdoCalcMaturityValue_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCalcMaturityValue_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCalcMaturityValue = new com.see.truetransact.uicomponent.CLabel();
        panRdgProvisionOfInterest = new com.see.truetransact.uicomponent.CPanel();
        rdoProvisionOfInterest_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoProvisionOfInterest_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblProvisionOfInterest = new com.see.truetransact.uicomponent.CLabel();
        tdtSchemeIntroDate = new com.see.truetransact.uicomponent.CDateField();
        lblAfterNoDays = new com.see.truetransact.uicomponent.CLabel();
        txtAfterNoDays = new com.see.truetransact.uicomponent.CTextField();
        txtLastAcctNumber = new com.see.truetransact.uicomponent.CTextField();
        lblAcctNumberPattern1 = new com.see.truetransact.uicomponent.CLabel();
        panNumberPattern = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNumberPattern = new com.see.truetransact.uicomponent.CTextField();
        txtSuffix = new com.see.truetransact.uicomponent.CTextField();
        lblSchemeClosingDate = new com.see.truetransact.uicomponent.CLabel();
        tdtSchemeClosingDate = new com.see.truetransact.uicomponent.CDateField();
        panPrematureWithdrawal = new com.see.truetransact.uicomponent.CPanel();
        txtPrematureWithdrawal = new com.see.truetransact.uicomponent.CTextField();
        cboPrematureWithdrawal = new com.see.truetransact.uicomponent.CComboBox();
        panTransferToMaturedDeposits1 = new com.see.truetransact.uicomponent.CPanel();
        rdoStaffAccount_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStaffAccount_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblStaffAccount = new com.see.truetransact.uicomponent.CLabel();
        lblCalcMaturityValue1 = new com.see.truetransact.uicomponent.CLabel();
        lblFDRenewalSameNoTranPrincAmt = new com.see.truetransact.uicomponent.CLabel();
        chkFDRenewalSameNoTranPrincAmt = new com.see.truetransact.uicomponent.CCheckBox();
        lblCanZeroBalAct = new com.see.truetransact.uicomponent.CLabel();
        chkCanZeroBalAct = new com.see.truetransact.uicomponent.CCheckBox();
        sptCentre = new com.see.truetransact.uicomponent.CSeparator();
        panWithDrawIntPay = new com.see.truetransact.uicomponent.CPanel();
        panWithdrawal = new com.see.truetransact.uicomponent.CPanel();
        lblPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CTextField();
        lblMaxAmtOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmtOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CTextField();
        lblMaxNoOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CLabel();
        txtMaxNoOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CTextField();
        txtMinAmtOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CTextField();
        lblMinAmtOfPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CLabel();
        lblWithdrawalsInMultiplesOf = new com.see.truetransact.uicomponent.CLabel();
        txtWithdrawalsInMultiplesOf = new com.see.truetransact.uicomponent.CTextField();
        lblWithdrawalWithInterest = new com.see.truetransact.uicomponent.CLabel();
        panPartialWithdrawalAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoPartialWithdrawalAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPartialWithdrawalAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        panWithdrawalWithInterest = new com.see.truetransact.uicomponent.CPanel();
        rdoWithdrawalWithInterest_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWithdrawalWithInterest_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMaxAmtPartialWithdrawalPercent = new com.see.truetransact.uicomponent.CLabel();
        panServiceCharge = new com.see.truetransact.uicomponent.CPanel();
        rdoServiceCharge_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoServiceCharge_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblServiceCharge = new com.see.truetransact.uicomponent.CLabel();
        lblServiceChargePercentage = new com.see.truetransact.uicomponent.CLabel();
        txtServiceCharge = new com.see.truetransact.uicomponent.CTextField();
        lblServiceChargePercent = new com.see.truetransact.uicomponent.CLabel();
        panInterestPayment = new com.see.truetransact.uicomponent.CPanel();
        panInterest = new com.see.truetransact.uicomponent.CPanel();
        cboIntCalcMethod = new com.see.truetransact.uicomponent.CComboBox();
        lblIntMaintainedAsPartOf = new com.see.truetransact.uicomponent.CLabel();
        cboIntMaintainedAsPartOf = new com.see.truetransact.uicomponent.CComboBox();
        lblIntCalcMethod = new com.see.truetransact.uicomponent.CLabel();
        panIntProvisioningApplicable = new com.see.truetransact.uicomponent.CPanel();
        rdoIntProvisioningApplicable_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIntProvisioningApplicable_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIntProvisioningApplicable = new com.see.truetransact.uicomponent.CLabel();
        lblIntProvisioningFreq = new com.see.truetransact.uicomponent.CLabel();
        cboIntProvisioningFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblProvisioningLevel = new com.see.truetransact.uicomponent.CLabel();
        cboProvisioningLevel = new com.see.truetransact.uicomponent.CComboBox();
        lblNoOfDays = new com.see.truetransact.uicomponent.CLabel();
        cboNoOfDays = new com.see.truetransact.uicomponent.CComboBox();
        lblIntCompoundingFreq = new com.see.truetransact.uicomponent.CLabel();
        cboIntCompoundingFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblInterestType = new com.see.truetransact.uicomponent.CLabel();
        cboInterestType = new com.see.truetransact.uicomponent.CComboBox();
        lblIntApplnFreq = new com.see.truetransact.uicomponent.CLabel();
        cboIntApplnFreq = new com.see.truetransact.uicomponent.CComboBox();
        chkPrematureClosure = new com.see.truetransact.uicomponent.CCheckBox();
        lblPrematureClosure = new com.see.truetransact.uicomponent.CLabel();
        lblAppNormRate = new com.see.truetransact.uicomponent.CLabel();
        lblNormPeriod = new com.see.truetransact.uicomponent.CLabel();
        txtNormPeriod = new com.see.truetransact.uicomponent.CTextField();
        chkAppNormRate = new com.see.truetransact.uicomponent.CCheckBox();
        lblCategoryBenifitRate = new com.see.truetransact.uicomponent.CLabel();
        txtCategoryBenifitRate = new com.see.truetransact.uicomponent.CTextField();
        panDate = new com.see.truetransact.uicomponent.CPanel();
        lblNextInterestAppliedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtNextInterestAppliedDate = new com.see.truetransact.uicomponent.CDateField();
        lblLastInterestAppliedDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLastInterestAppliedDate = new com.see.truetransact.uicomponent.CDateField();
        lblNextIntProvisionalDate = new com.see.truetransact.uicomponent.CLabel();
        tdtNextIntProvisionalDate = new com.see.truetransact.uicomponent.CDateField();
        lblLastIntProvisionalDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLastIntProvisionalDate = new com.see.truetransact.uicomponent.CDateField();
        lblIntRoundOff = new com.see.truetransact.uicomponent.CLabel();
        lblMinIntToBePaid = new com.see.truetransact.uicomponent.CLabel();
        txtMinIntToBePaid = new com.see.truetransact.uicomponent.CTextField();
        cboDepositsProdFixd = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositsProdFixd = new com.see.truetransact.uicomponent.CLabel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        cbxInterstRoundTime = new com.see.truetransact.uicomponent.CCheckBox();
        panPrematureClosureApply = new com.see.truetransact.uicomponent.CPanel();
        rdoDepositRate = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSBRate = new com.see.truetransact.uicomponent.CRadioButton();
        cboIntRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        lblPrematureClosureApply = new com.see.truetransact.uicomponent.CLabel();
        lblDifferentROI = new com.see.truetransact.uicomponent.CLabel();
        chkDifferentROI = new com.see.truetransact.uicomponent.CCheckBox();
        lblPrematureClosureApplyROI = new com.see.truetransact.uicomponent.CLabel();
        lblSlabWiseInterest = new com.see.truetransact.uicomponent.CLabel();
        chkSlabWiseInterest = new com.see.truetransact.uicomponent.CCheckBox();
        lblSlabWiseCommision = new com.see.truetransact.uicomponent.CLabel();
        chkSlabWiseCommision = new com.see.truetransact.uicomponent.CCheckBox();
        lblpreMatIntType = new com.see.truetransact.uicomponent.CLabel();
        cboPreMatIntType = new com.see.truetransact.uicomponent.CComboBox();
        cmbSbProduct = new com.see.truetransact.uicomponent.CComboBox();
        sptInterestPayment = new com.see.truetransact.uicomponent.CSeparator();
        panExcludeLienFrm = new com.see.truetransact.uicomponent.CPanel();
        chkExcludeLienFrmStanding = new com.see.truetransact.uicomponent.CCheckBox();
        chkExcludeLienFrmIntrstAppl = new com.see.truetransact.uicomponent.CCheckBox();
        chkDepositUnlien = new com.see.truetransact.uicomponent.CCheckBox();
        panIntEditable = new com.see.truetransact.uicomponent.CPanel();
        chkIntEditable = new com.see.truetransact.uicomponent.CCheckBox();
        chkCummCertPrint = new com.see.truetransact.uicomponent.CCheckBox();
        panAcHdRenewal = new com.see.truetransact.uicomponent.CPanel();
        panRenewalAndTax = new com.see.truetransact.uicomponent.CPanel();
        panTax = new com.see.truetransact.uicomponent.CPanel();
        lblTDSGLAcctHd = new com.see.truetransact.uicomponent.CLabel();
        lblRecalcOfMaturityValue = new com.see.truetransact.uicomponent.CLabel();
        rdoRecalcOfMaturityValue_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRecalcOfMaturityValue_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtTDSGLAcctHd = new com.see.truetransact.uicomponent.CTextField();
        btnTDSGLAcctHd = new com.see.truetransact.uicomponent.CButton();
        txtTDSGLAcctHdDesc = new com.see.truetransact.uicomponent.CTextField();
        panAccountHead = new com.see.truetransact.uicomponent.CPanel();
        lblIntProvisioningAcctHd = new com.see.truetransact.uicomponent.CLabel();
        txtIntProvisioningAcctHd = new com.see.truetransact.uicomponent.CTextField();
        btnIntProvisioningAcctHd = new com.see.truetransact.uicomponent.CButton();
        lblIntOnMaturedDepositAcctHead = new com.see.truetransact.uicomponent.CLabel();
        txtIntOnMaturedDepositAcctHead = new com.see.truetransact.uicomponent.CTextField();
        btnIntOnMaturedDepositAcctHead = new com.see.truetransact.uicomponent.CButton();
        lblFixedDepositAcctHead = new com.see.truetransact.uicomponent.CLabel();
        txtFixedDepositAcctHead = new com.see.truetransact.uicomponent.CTextField();
        btnFixedDepositAcctHead = new com.see.truetransact.uicomponent.CButton();
        lblMaturityDepositAcctHead = new com.see.truetransact.uicomponent.CLabel();
        txttMaturityDepositAcctHead = new com.see.truetransact.uicomponent.CTextField();
        btnMaturityDepositAcctHead = new com.see.truetransact.uicomponent.CButton();
        lblIntProvisionOfMaturedDeposit = new com.see.truetransact.uicomponent.CLabel();
        txtIntProvisionOfMaturedDeposit = new com.see.truetransact.uicomponent.CTextField();
        btnIntProvisionOfMaturedDeposit = new com.see.truetransact.uicomponent.CButton();
        lblIntPaybleGLHead = new com.see.truetransact.uicomponent.CLabel();
        txtIntPaybleGLHead = new com.see.truetransact.uicomponent.CTextField();
        btnIntPaybleGLHead = new com.see.truetransact.uicomponent.CButton();
        lblIntDebitPLHead = new com.see.truetransact.uicomponent.CLabel();
        txtIntDebitPLHead = new com.see.truetransact.uicomponent.CTextField();
        btnIntDebitPLHead = new com.see.truetransact.uicomponent.CButton();
        lblAcctHeadForFloatAcct = new com.see.truetransact.uicomponent.CLabel();
        txtAcctHeadForFloatAcct = new com.see.truetransact.uicomponent.CTextField();
        btnAcctHeadForFloatAcct = new com.see.truetransact.uicomponent.CButton();
        lblCommisionPaybleGLHead = new com.see.truetransact.uicomponent.CLabel();
        txtCommisionPaybleGLHead = new com.see.truetransact.uicomponent.CTextField();
        btnCommisionPaybleGLHead = new com.see.truetransact.uicomponent.CButton();
        lblPenalCharges = new com.see.truetransact.uicomponent.CLabel();
        txtPenalCharges = new com.see.truetransact.uicomponent.CTextField();
        btnPenalCharges = new com.see.truetransact.uicomponent.CButton();
        lblTransferOutHead = new com.see.truetransact.uicomponent.CLabel();
        txtTransferOutHead = new com.see.truetransact.uicomponent.CTextField();
        btnTransferOutHead = new com.see.truetransact.uicomponent.CButton();
        lblPostageAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtPostageAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnPostageAcHd = new com.see.truetransact.uicomponent.CButton();
        txtIntProvisioningAcctHdDesc = new com.see.truetransact.uicomponent.CTextField();
        txtIntPaybleGLHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        txtCommisionPaybleGLHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        txtIntDebitPLHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        txttMaturityDepositAcctHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        txtIntOnMaturedDepositAcctHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        txtIntProvisionOfMaturedDepositDesc = new com.see.truetransact.uicomponent.CTextField();
        txtAcctHeadForFloatAcctDesc = new com.see.truetransact.uicomponent.CTextField();
        txtFixedDepositAcctHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        txtPenalChargesDesc = new com.see.truetransact.uicomponent.CTextField();
        txtTransferOutHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        lblInterestRecoveryAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtInterestRecoveryAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnInterestRecoveryAcHd = new com.see.truetransact.uicomponent.CButton();
        txtInterestRecoveryAcHdDesc = new com.see.truetransact.uicomponent.CTextField();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtBenIntReserveHead = new com.see.truetransact.uicomponent.CTextField();
        btnBenIntReserveHead = new com.see.truetransact.uicomponent.CButton();
        txtBenIntReserveHeadDesc = new com.see.truetransact.uicomponent.CTextField();
        panRenewalParameter = new com.see.truetransact.uicomponent.CPanel();
        panRenewalExtension = new com.see.truetransact.uicomponent.CPanel();
        panRateAsOn = new com.see.truetransact.uicomponent.CPanel();
        lblIntPeriodForBackDatedRenewal = new com.see.truetransact.uicomponent.CLabel();
        lblIntCriteria = new com.see.truetransact.uicomponent.CLabel();
        cboIntCriteria = new com.see.truetransact.uicomponent.CComboBox();
        lblAutoRenewalAllowed = new com.see.truetransact.uicomponent.CLabel();
        panAutoRenewalAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoAutoRenewalAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAutoRenewalAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblRenewalOfDepositAllowed = new com.see.truetransact.uicomponent.CLabel();
        panRenewalOfDepositAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoRenewalOfDepositAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRenewalOfDepositAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMinNoOfDays = new com.see.truetransact.uicomponent.CLabel();
        panMinNoOfDays = new com.see.truetransact.uicomponent.CPanel();
        txtMinNoOfDays = new com.see.truetransact.uicomponent.CTextField();
        cboMinNoOfDays = new com.see.truetransact.uicomponent.CComboBox();
        lblMaxNopfTimes = new com.see.truetransact.uicomponent.CLabel();
        txtMaxNopfTimes = new com.see.truetransact.uicomponent.CTextField();
        panIntPeriodForBackDatedRenewal = new com.see.truetransact.uicomponent.CPanel();
        cboIntPeriodForBackDatedRenewal = new com.see.truetransact.uicomponent.CComboBox();
        txtIntPeriodForBackDatedRenewal = new com.see.truetransact.uicomponent.CTextField();
        lblIntType = new com.see.truetransact.uicomponent.CLabel();
        cboIntType = new com.see.truetransact.uicomponent.CComboBox();
        lblMaturityDate = new com.see.truetransact.uicomponent.CLabel();
        panMaxPeriodMDt = new com.see.truetransact.uicomponent.CPanel();
        cboMaxPeriodMDt = new com.see.truetransact.uicomponent.CComboBox();
        txtMaxPeriodMDt = new com.see.truetransact.uicomponent.CTextField();
        panAutoRenewalAllowed1 = new com.see.truetransact.uicomponent.CPanel();
        rdoSameNoRenewalAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSameNoRenewalAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblAutoRenewalAllowed1 = new com.see.truetransact.uicomponent.CLabel();
        lblMaxNopfTimes1 = new com.see.truetransact.uicomponent.CLabel();
        txtMaxNopfTimes1 = new com.see.truetransact.uicomponent.CTextField();
        panMinimumRenewalDeposit = new com.see.truetransact.uicomponent.CPanel();
        cboMinimumRenewalDeposit = new com.see.truetransact.uicomponent.CComboBox();
        txtMinimumRenewalDeposit = new com.see.truetransact.uicomponent.CTextField();
        lblMinimumRenewalDeposit = new com.see.truetransact.uicomponent.CLabel();
        panRenewedclosedbefore = new com.see.truetransact.uicomponent.CPanel();
        cboRenewedclosedbefore = new com.see.truetransact.uicomponent.CComboBox();
        txtRenewedclosedbefore = new com.see.truetransact.uicomponent.CTextField();
        lblRenewedclosedbefore = new com.see.truetransact.uicomponent.CLabel();
        panInterestalreadyPaid = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestalreadyPaid_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInterestalreadyPaid_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblclosedwithinperiod = new com.see.truetransact.uicomponent.CLabel();
        lblOneRateAvail = new com.see.truetransact.uicomponent.CLabel();
        panOneRateAvail = new com.see.truetransact.uicomponent.CPanel();
        rdoSBRateOneRate_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSBRateOneRate_No = new com.see.truetransact.uicomponent.CRadioButton();
        panclosedwithinperiod = new com.see.truetransact.uicomponent.CPanel();
        rdoClosedwithinperiod_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoClosedwithinperiod_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblInterestalreadyPaid = new com.see.truetransact.uicomponent.CLabel();
        panIntRateforOverduePeriod = new com.see.truetransact.uicomponent.CPanel();
        rdoInterestrateappliedoverdue_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblBothRateAvail = new com.see.truetransact.uicomponent.CLabel();
        lblInterestrateappliedoverdue = new com.see.truetransact.uicomponent.CLabel();
        lblRenewalOfDepositAllowed1 = new com.see.truetransact.uicomponent.CLabel();
        panBothRateNotAvail = new com.see.truetransact.uicomponent.CPanel();
        rdoBothRateNotAvail_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBothRateNotAvail_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblBothRateNotAvail = new com.see.truetransact.uicomponent.CLabel();
        lblBasedOnDepRate = new com.see.truetransact.uicomponent.CLabel();
        lblRateAsOnDateOfRenewal = new com.see.truetransact.uicomponent.CLabel();
        chkRateAsOnDateOfRenewal = new com.see.truetransact.uicomponent.CCheckBox();
        chkRateAsOnDateOfMaturity = new com.see.truetransact.uicomponent.CCheckBox();
        lblRateAsOnDateOfMaturity = new com.see.truetransact.uicomponent.CLabel();
        sptOperatesLike1 = new com.see.truetransact.uicomponent.CSeparator();
        sptOperatesLike2 = new com.see.truetransact.uicomponent.CSeparator();
        cboEitherofTwoRatesChoose = new com.see.truetransact.uicomponent.CComboBox();
        sbRateCmb = new com.see.truetransact.uicomponent.CComboBox();
        rdoInterestrateappliedoverdue_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblIntRateApp = new com.see.truetransact.uicomponent.CLabel();
        chkIntRateApp = new com.see.truetransact.uicomponent.CCheckBox();
        lblSbRateInterestDeath = new com.see.truetransact.uicomponent.CLabel();
        chkIntRateDeathMarked = new com.see.truetransact.uicomponent.CCheckBox();
        panTax1 = new com.see.truetransact.uicomponent.CPanel();
        panAutoRenewalAllowed3 = new com.see.truetransact.uicomponent.CPanel();
        rdoExtensionBeyondOriginalDate_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExtensionBeyondOriginalDate_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblExtensionBeyondOriginalDate = new com.see.truetransact.uicomponent.CLabel();
        lblExtensionPenal = new com.see.truetransact.uicomponent.CLabel();
        panAutoRenewalAllowed4 = new com.see.truetransact.uicomponent.CPanel();
        rdoExtensionPenal_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExtensionPenal_No = new com.see.truetransact.uicomponent.CRadioButton();
        panRD = new com.see.truetransact.uicomponent.CPanel();
        panRecurringDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLastInstallmentAllowed = new com.see.truetransact.uicomponent.CLabel();
        lblMaturityDateAfterLastInstalPaid = new com.see.truetransact.uicomponent.CLabel();
        panMaturityDateAfterLastInstalPaid = new com.see.truetransact.uicomponent.CPanel();
        txtMaturityDateAfterLastInstalPaid = new com.see.truetransact.uicomponent.CTextField();
        cboMaturityDateAfterLastInstalPaid = new com.see.truetransact.uicomponent.CComboBox();
        lblCanReceiveExcessInstal = new com.see.truetransact.uicomponent.CLabel();
        panCanReceiveExcessInstal = new com.see.truetransact.uicomponent.CPanel();
        rdoCanReceiveExcessInstal_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCanReceiveExcessInstal_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblInstallmentInRecurringDepositAcct = new com.see.truetransact.uicomponent.CLabel();
        panInstallmentInRecurringDepositAcct = new com.see.truetransact.uicomponent.CPanel();
        rdoInstallmentInRecurringDepositAcct_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInstallmentInRecurringDepositAcct_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblDepositsFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboDepositsFrequency = new com.see.truetransact.uicomponent.CComboBox();
        cboChangeValue = new com.see.truetransact.uicomponent.CComboBox();
        lblIntPayableOnExcessInstal = new com.see.truetransact.uicomponent.CLabel();
        panIntPayableOnExcessInstal = new com.see.truetransact.uicomponent.CPanel();
        rdoIntPayableOnExcessInstal_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIntPayableOnExcessInstal_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblRecurringDepositToFixedDeposit = new com.see.truetransact.uicomponent.CLabel();
        panRecurringDepositToFixedDeposit = new com.see.truetransact.uicomponent.CPanel();
        rdoRecurringDepositToFixedDeposit_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRecurringDepositToFixedDeposit_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCutOffDayForPaymentOfInstal = new com.see.truetransact.uicomponent.CLabel();
        panLastInstallmentAllowedRD = new com.see.truetransact.uicomponent.CPanel();
        rdoLastInstallmentAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLastInstallmentAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        cboCutOffDayForPaymentOfInstal = new com.see.truetransact.uicomponent.CComboBox();
        txtCutOffDayForPaymentOfInstal = new com.see.truetransact.uicomponent.CTextField();
        cboInstallmentToBeCharged = new com.see.truetransact.uicomponent.CComboBox();
        lblInstallmentToBeCharged = new com.see.truetransact.uicomponent.CLabel();
        lblChangeValue = new com.see.truetransact.uicomponent.CLabel();
        lblPenaltyOnLateInstallmentsChargeble = new com.see.truetransact.uicomponent.CLabel();
        panPenaltyOnLateInstallmentsChargeble = new com.see.truetransact.uicomponent.CPanel();
        rdoPenaltyOnLateInstallmentsChargeble_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenaltyOnLateInstallmentsChargeble_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMinimumPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblAgentCommision = new com.see.truetransact.uicomponent.CLabel();
        panMinimumPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtMinimumPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboMinimumPeriod = new com.see.truetransact.uicomponent.CComboBox();
        panAgentCommision = new com.see.truetransact.uicomponent.CPanel();
        txtAgentCommision = new com.see.truetransact.uicomponent.CTextField();
        cboAgentCommision = new com.see.truetransact.uicomponent.CComboBox();
        lblDailyDepositCalc = new com.see.truetransact.uicomponent.CLabel();
        lblWeekly = new com.see.truetransact.uicomponent.CLabel();
        cboWeekly = new com.see.truetransact.uicomponent.CComboBox();
        panDailyIntCalc = new com.see.truetransact.uicomponent.CPanel();
        rdoDaily = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWeekly = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMonthly = new com.see.truetransact.uicomponent.CRadioButton();
        panInterestNotPayingValue = new com.see.truetransact.uicomponent.CPanel();
        txtInterestNotPayingValue = new com.see.truetransact.uicomponent.CTextField();
        cboInterestNotPayingValue = new com.see.truetransact.uicomponent.CComboBox();
        lblCustomerInterestNotPaying = new com.see.truetransact.uicomponent.CLabel();
        cboMonthlyIntCalcMethod = new com.see.truetransact.uicomponent.CComboBox();
        panInsBeyondMaturityDtYesorNo = new com.see.truetransact.uicomponent.CPanel();
        rdoInsBeyondMaturityDt_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInsBeyondMaturityDt_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblInsBeyondMaturityDtYesorNo = new com.see.truetransact.uicomponent.CLabel();
        panPartialWithdrawlAllowedForDD = new com.see.truetransact.uicomponent.CPanel();
        rdoPartialWithdrawlAllowedForDD_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPartialWithdrawlAllowedForDD_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPartialWithdrawlAllowedForDD = new com.see.truetransact.uicomponent.CLabel();
        lblRDIrregularIfInstallmentDue = new com.see.truetransact.uicomponent.CLabel();
        txtRDIrregularIfInstallmentDue = new com.see.truetransact.uicomponent.CTextField();
        lblIncaseOfIrregularRD = new com.see.truetransact.uicomponent.CLabel();
        panPartialWithdrawlAllowedForDD1 = new com.see.truetransact.uicomponent.CPanel();
        rdoIncaseOfIrregularRDSBRate = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIncaseOfIrregularRDRBRate = new com.see.truetransact.uicomponent.CRadioButton();
        lblIntApplicationSlab = new com.see.truetransact.uicomponent.CLabel();
        cPanel2 = new com.see.truetransact.uicomponent.CPanel();
        chkIntApplicationSlab = new com.see.truetransact.uicomponent.CCheckBox();
        panPenalRounded = new com.see.truetransact.uicomponent.CPanel();
        rdoPenalRounded_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPenalRounded_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPenalRounded = new com.see.truetransact.uicomponent.CLabel();
        cboRoundOffCriteriaPenal = new com.see.truetransact.uicomponent.CComboBox();
        lblRoundOffCriteriaPenal = new com.see.truetransact.uicomponent.CLabel();
        chkWeeklySpecial = new com.see.truetransact.uicomponent.CCheckBox();
        lblDueOn = new com.see.truetransact.uicomponent.CLabel();
        rdoMonthEnd = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInstallmentDay = new com.see.truetransact.uicomponent.CRadioButton();
        chkRdNature = new com.see.truetransact.uicomponent.CCheckBox();
        lblGracePeriod = new com.see.truetransact.uicomponent.CLabel();
        txtGracePeriod = new com.see.truetransact.uicomponent.CTextField();
        panDelayedInstallmets = new com.see.truetransact.uicomponent.CPanel();
        panLastInstallmentAllowed = new com.see.truetransact.uicomponent.CPanel();
        panBtnDelayed = new com.see.truetransact.uicomponent.CPanel();
        btnDelayedNew = new com.see.truetransact.uicomponent.CButton();
        btnDelayedSave = new com.see.truetransact.uicomponent.CButton();
        btnDelayedDel = new com.see.truetransact.uicomponent.CButton();
        panDelayedInstallmet = new com.see.truetransact.uicomponent.CPanel();
        srpDelayedInstallmet = new com.see.truetransact.uicomponent.CScrollPane();
        tblDelayedInstallmet = new com.see.truetransact.uicomponent.CTable();
        panFloatingRateAccount = new com.see.truetransact.uicomponent.CPanel();
        panAgentsCalculations = new com.see.truetransact.uicomponent.CPanel();
        lblDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDate = new com.see.truetransact.uicomponent.CDateField();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        lblFromPeriod = new com.see.truetransact.uicomponent.CLabel();
        panFromPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtFromPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboFromPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblToPeriod = new com.see.truetransact.uicomponent.CLabel();
        panToPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtToPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboToPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblRateInterest = new com.see.truetransact.uicomponent.CLabel();
        txtCommisionRate = new com.see.truetransact.uicomponent.CTextField();
        panButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        lblDelayedChargesDet = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmt = new com.see.truetransact.uicomponent.CTextField();
        txtToAmt = new com.see.truetransact.uicomponent.CTextField();
        txtDelayedChargesAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDelayedToAmt = new com.see.truetransact.uicomponent.CLabel();
        lblDelayedFromAmt = new com.see.truetransact.uicomponent.CLabel();
        lblDelayedChargesAmt = new com.see.truetransact.uicomponent.CLabel();
        lblInstType = new com.see.truetransact.uicomponent.CLabel();
        cboInstType = new com.see.truetransact.uicomponent.CComboBox();
        srpInterestTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblInterestTable = new com.see.truetransact.uicomponent.CTable();
        panWeeklyDepositSlab = new com.see.truetransact.uicomponent.CPanel();
        srpWeeklySlabSettings = new com.see.truetransact.uicomponent.CScrollPane();
        tblWeeklySlabSettings = new com.see.truetransact.uicomponent.CTable();
        panSlab = new com.see.truetransact.uicomponent.CPanel();
        panSlabDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProductType = new com.see.truetransact.uicomponent.CLabel();
        lbInstallmentFrom = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentTo = new com.see.truetransact.uicomponent.CLabel();
        lblPenal = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentNo = new com.see.truetransact.uicomponent.CLabel();
        cboProductType = new com.see.truetransact.uicomponent.CComboBox();
        txtInstallmentFrom = new com.see.truetransact.uicomponent.CTextField();
        txtInstallmentTo = new com.see.truetransact.uicomponent.CTextField();
        txtPenal = new com.see.truetransact.uicomponent.CTextField();
        txtInstallmentNo = new com.see.truetransact.uicomponent.CTextField();
        panSalaryStructureButtons = new com.see.truetransact.uicomponent.CPanel();
        btnWeeklyDepSlabNew = new com.see.truetransact.uicomponent.CButton();
        btnWeeklyDepSlabSave = new com.see.truetransact.uicomponent.CButton();
        btnWeeklyDepSlabDelete = new com.see.truetransact.uicomponent.CButton();
        panThriftBenevolent = new com.see.truetransact.uicomponent.CPanel();
        panInsallmentDetails = new com.see.truetransact.uicomponent.CPanel();
        txtInstallmentAmount = new com.see.truetransact.uicomponent.CTextField();
        tdtEffectiveDate = new com.see.truetransact.uicomponent.CDateField();
        lblEffectiveDate = new com.see.truetransact.uicomponent.CLabel();
        lblInstallmentAmount = new com.see.truetransact.uicomponent.CLabel();
        srpThriftBenevelontDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblThriftBenevelontDetails = new com.see.truetransact.uicomponent.CTable();
        panRDClosingROISetting = new com.see.truetransact.uicomponent.CPanel();
        panSlab2 = new com.see.truetransact.uicomponent.CPanel();
        panROIPrematureClosure = new com.see.truetransact.uicomponent.CPanel();
        lblProductType2 = new com.see.truetransact.uicomponent.CLabel();
        lbInstallmentFrom2 = new com.see.truetransact.uicomponent.CLabel();
        cboprmatureCloseRate = new com.see.truetransact.uicomponent.CComboBox();
        cboPrmatureCloseProduct = new com.see.truetransact.uicomponent.CComboBox();
        panROIIrregularRDClosure = new com.see.truetransact.uicomponent.CPanel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        cboIrregularRDCloseRate = new com.see.truetransact.uicomponent.CComboBox();
        cboIrregularRDCloseProduct = new com.see.truetransact.uicomponent.CComboBox();
        cPanel4 = new com.see.truetransact.uicomponent.CPanel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        chkRDClosingOtherROI = new com.see.truetransact.uicomponent.CCheckBox();
        chkIntForIrregularRD = new com.see.truetransact.uicomponent.CCheckBox();
        chkSpecialRD = new com.see.truetransact.uicomponent.CCheckBox();
        lblSpecialRDInstalments = new com.see.truetransact.uicomponent.CLabel();
        txtSpecialRDInstallments = new com.see.truetransact.uicomponent.CTextField();
        panAgenCommissionSlab = new com.see.truetransact.uicomponent.CPanel();
        panAgentCommSlab = new com.see.truetransact.uicomponent.CPanel();
        panAgentCommSlabDetails = new com.see.truetransact.uicomponent.CPanel();
        lblAgentCommSlabAmtFrom = new com.see.truetransact.uicomponent.CLabel();
        lblAgentCommSlabAmtTo = new com.see.truetransact.uicomponent.CLabel();
        lblAgentCommSlabPercent = new com.see.truetransact.uicomponent.CLabel();
        txtAgentCommSlabAmtFrom = new com.see.truetransact.uicomponent.CTextField();
        TxtAgentCommSlabAmtTo = new com.see.truetransact.uicomponent.CTextField();
        txtAgentCommSlabPercent = new com.see.truetransact.uicomponent.CTextField();
        panSalaryStructureButtons1 = new com.see.truetransact.uicomponent.CPanel();
        btnAgentCommissionSlabNew = new com.see.truetransact.uicomponent.CButton();
        btnAgentCommissionSlabSave = new com.see.truetransact.uicomponent.CButton();
        btnAgentCommissionSlabDelete = new com.see.truetransact.uicomponent.CButton();
        srpAgentCommSlabSettings = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgentCommSlabSettings = new com.see.truetransact.uicomponent.CTable();
        cLabel3 = new com.see.truetransact.uicomponent.CLabel();
        panAgentCommCalcMethod = new com.see.truetransact.uicomponent.CPanel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        cboAgentCommCalcMethod = new com.see.truetransact.uicomponent.CComboBox();
        chkAgentCommSlabRequired = new com.see.truetransact.uicomponent.CCheckBox();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrDepositsProduct = new com.see.truetransact.uicomponent.CMenuBar();
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

        lblFromDepositDate.setText("From Deposit Date");

        tdtFromDepositDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFromDepositDate.setPreferredSize(new java.awt.Dimension(100, 21));

        lblChosenDate.setText("Chosen Date");

        tdtChosenDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtChosenDate.setPreferredSize(new java.awt.Dimension(100, 21));

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(1008, 700));
        setOpenForEditBy("");
        setPreferredSize(new java.awt.Dimension(1008, 700));

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
        tbrDepositsProduct.add(btnView);

        lblSpace4.setText("     ");
        tbrDepositsProduct.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepositsProduct.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepositsProduct.add(lblSpace18);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnCopy);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepositsProduct.add(lblSpace19);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrDepositsProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                btnSaveComponentHidden(evt);
            }
        });
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnSave);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepositsProduct.add(lblSpace20);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrDepositsProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnAuthorize);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepositsProduct.add(lblSpace21);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnException);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepositsProduct.add(lblSpace22);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnReject);

        lblSpace5.setText("     ");
        tbrDepositsProduct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrDepositsProduct.add(btnPrint);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrDepositsProduct.add(lblSpace23);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrDepositsProduct.add(btnClose);

        getContentPane().add(tbrDepositsProduct, java.awt.BorderLayout.NORTH);

        panDepositsProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDepositsProduct.setMinimumSize(new java.awt.Dimension(868, 620));
        panDepositsProduct.setPreferredSize(new java.awt.Dimension(868, 620));
        panDepositsProduct.setLayout(new java.awt.GridBagLayout());

        tabDepositsProduct.setMinimumSize(new java.awt.Dimension(850, 615));
        tabDepositsProduct.setPreferredSize(new java.awt.Dimension(850, 615));

        panScheme.setMinimumSize(new java.awt.Dimension(900, 560));
        panScheme.setPreferredSize(new java.awt.Dimension(900, 560));
        panScheme.setLayout(new java.awt.GridBagLayout());

        panAcHd.setMinimumSize(new java.awt.Dimension(415, 600));
        panAcHd.setPreferredSize(new java.awt.Dimension(415, 600));
        panAcHd.setLayout(new java.awt.GridBagLayout());

        lblAcctHd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAcctHd.setText("Account Head");
        lblAcctHd.setMinimumSize(new java.awt.Dimension(82, 15));
        lblAcctHd.setPreferredSize(new java.awt.Dimension(82, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcHd.add(lblAcctHd, gridBagConstraints);

        lblProductID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProductID.setText("Product ID");
        lblProductID.setMinimumSize(new java.awt.Dimension(70, 16));
        lblProductID.setPreferredSize(new java.awt.Dimension(70, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblProductID, gridBagConstraints);

        txtProductID.setAllowAll(true);
        txtProductID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductID.setPreferredSize(new java.awt.Dimension(100, 20));
        txtProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductIDActionPerformed(evt);
            }
        });
        txtProductID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(txtProductID, gridBagConstraints);

        lblDesc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDesc.setText("Product Description");
        lblDesc.setPreferredSize(new java.awt.Dimension(114, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblDesc, gridBagConstraints);

        txtDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtDesc.setPreferredSize(new java.awt.Dimension(150, 20));
        txtDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDescFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(txtDesc, gridBagConstraints);

        lblOperatesLike.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOperatesLike.setText("Operates Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblOperatesLike, gridBagConstraints);

        cboOperatesLike.setMinimumSize(new java.awt.Dimension(100, 21));
        cboOperatesLike.setPreferredSize(new java.awt.Dimension(150, 20));
        cboOperatesLike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboOperatesLikeActionPerformed(evt);
            }
        });
        cboOperatesLike.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboOperatesLikeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(cboOperatesLike, gridBagConstraints);

        sptOperatesLike.setMinimumSize(new java.awt.Dimension(400, 2));
        sptOperatesLike.setPreferredSize(new java.awt.Dimension(400, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcHd.add(sptOperatesLike, gridBagConstraints);

        panCalcTDS.setMinimumSize(new java.awt.Dimension(101, 18));
        panCalcTDS.setLayout(new java.awt.GridBagLayout());

        rdgCalcTDS.add(rdoCalcTDS_Yes);
        rdoCalcTDS_Yes.setText("Yes");
        rdoCalcTDS_Yes.setMaximumSize(new java.awt.Dimension(55, 27));
        rdoCalcTDS_Yes.setMinimumSize(new java.awt.Dimension(55, 27));
        rdoCalcTDS_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoCalcTDS_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCalcTDS_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCalcTDS.add(rdoCalcTDS_Yes, gridBagConstraints);

        rdgCalcTDS.add(rdoCalcTDS_No);
        rdoCalcTDS_No.setText("No");
        rdoCalcTDS_No.setMaximumSize(new java.awt.Dimension(41, 21));
        rdoCalcTDS_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoCalcTDS_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panCalcTDS.add(rdoCalcTDS_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panCalcTDS, gridBagConstraints);

        lblCalcTDS.setText("Calculate TDS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblCalcTDS, gridBagConstraints);

        lblPayInterestOnHoliday.setText("Pay Int.on Deposit Maturity on Holiday");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblPayInterestOnHoliday, gridBagConstraints);

        lblAmountRounded.setText("Should Mat Amt. be Rounded");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblAmountRounded, gridBagConstraints);

        panPayInterestOnHoliday.setLayout(new java.awt.GridBagLayout());

        rdgPayInterestOnHoliday.add(rdoPayInterestOnHoliday_Yes);
        rdoPayInterestOnHoliday_Yes.setText("Yes");
        rdoPayInterestOnHoliday_Yes.setMaximumSize(new java.awt.Dimension(55, 18));
        rdoPayInterestOnHoliday_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoPayInterestOnHoliday_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panPayInterestOnHoliday.add(rdoPayInterestOnHoliday_Yes, gridBagConstraints);

        rdgPayInterestOnHoliday.add(rdoPayInterestOnHoliday_No);
        rdoPayInterestOnHoliday_No.setText("No");
        rdoPayInterestOnHoliday_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoPayInterestOnHoliday_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panPayInterestOnHoliday.add(rdoPayInterestOnHoliday_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(panPayInterestOnHoliday, gridBagConstraints);

        panAmountRounded.setLayout(new java.awt.GridBagLayout());

        rdgAmountRounded.add(rdoAmountRounded_Yes);
        rdoAmountRounded_Yes.setText("Yes");
        rdoAmountRounded_Yes.setMaximumSize(new java.awt.Dimension(55, 18));
        rdoAmountRounded_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoAmountRounded_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoAmountRounded_Yes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoAmountRounded_YesItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panAmountRounded.add(rdoAmountRounded_Yes, gridBagConstraints);

        rdgAmountRounded.add(rdoAmountRounded_No);
        rdoAmountRounded_No.setText("No");
        rdoAmountRounded_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoAmountRounded_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoAmountRounded_No.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoAmountRounded_NoItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panAmountRounded.add(rdoAmountRounded_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panAmountRounded, gridBagConstraints);

        lblRoundOffCriteria.setText("Round Off Criteria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblRoundOffCriteria, gridBagConstraints);

        cboRoundOffCriteria.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoundOffCriteria.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(cboRoundOffCriteria, gridBagConstraints);

        panInterestAfterMaturity.setLayout(new java.awt.GridBagLayout());

        rdgInterestAfterMaturity.add(rdoInterestAfterMaturity_Yes);
        rdoInterestAfterMaturity_Yes.setText("Yes");
        rdoInterestAfterMaturity_Yes.setMaximumSize(new java.awt.Dimension(55, 27));
        rdoInterestAfterMaturity_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoInterestAfterMaturity_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoInterestAfterMaturity_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestAfterMaturity_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestAfterMaturity.add(rdoInterestAfterMaturity_Yes, gridBagConstraints);

        rdgInterestAfterMaturity.add(rdoInterestAfterMaturity_No);
        rdoInterestAfterMaturity_No.setText("No");
        rdoInterestAfterMaturity_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoInterestAfterMaturity_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoInterestAfterMaturity_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestAfterMaturity_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panInterestAfterMaturity.add(rdoInterestAfterMaturity_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panInterestAfterMaturity, gridBagConstraints);

        lblInterestAfterMaturity.setText("Int to be paid after Maturity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblInterestAfterMaturity, gridBagConstraints);

        btnMaturityInterestType.setText("After Maturity Interest Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(btnMaturityInterestType, gridBagConstraints);

        cboMaturityInterestType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMaturityInterestType.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(cboMaturityInterestType, gridBagConstraints);

        btnMaturityInterestRate.setText("After Maturity Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(btnMaturityInterestRate, gridBagConstraints);

        cboMaturityInterestRate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMaturityInterestRate.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(cboMaturityInterestRate, gridBagConstraints);

        lblDepositPerUnit.setText("Deposit Per Unit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblDepositPerUnit, gridBagConstraints);

        panPeriodInMultiplesOf.setPreferredSize(new java.awt.Dimension(154, 21));
        panPeriodInMultiplesOf.setLayout(new java.awt.GridBagLayout());

        txtPeriodInMultiplesOf.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPeriodInMultiplesOf.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panPeriodInMultiplesOf.add(txtPeriodInMultiplesOf, gridBagConstraints);

        cboPeriodInMultiplesOf.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPeriodInMultiplesOf.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPeriodInMultiplesOf.add(cboPeriodInMultiplesOf, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panPeriodInMultiplesOf, gridBagConstraints);

        lblMinDepositPeriod.setText("Minimum Deposit Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblMinDepositPeriod, gridBagConstraints);

        lblMaxDepositPeriod.setText("Maximum Deposit Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblMaxDepositPeriod, gridBagConstraints);

        lblPeriodInMultiplesOf.setText("Period in Multiples of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblPeriodInMultiplesOf, gridBagConstraints);

        panMaxDepositPeriod.setPreferredSize(new java.awt.Dimension(154, 21));
        panMaxDepositPeriod.setLayout(new java.awt.GridBagLayout());

        txtMaxDepositPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxDepositPeriod.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMaxDepositPeriod.add(txtMaxDepositPeriod, gridBagConstraints);

        cboMaxDepositPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMaxDepositPeriod.setPreferredSize(new java.awt.Dimension(100, 20));
        cboMaxDepositPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMaxDepositPeriodActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMaxDepositPeriod.add(cboMaxDepositPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(panMaxDepositPeriod, gridBagConstraints);

        panMinDepositPeriod.setPreferredSize(new java.awt.Dimension(154, 21));
        panMinDepositPeriod.setLayout(new java.awt.GridBagLayout());

        txtMinDepositPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinDepositPeriod.setPreferredSize(new java.awt.Dimension(50, 20));
        txtMinDepositPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMinDepositPeriodActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMinDepositPeriod.add(txtMinDepositPeriod, gridBagConstraints);

        cboMinDepositPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboMinDepositPeriod.setPreferredSize(new java.awt.Dimension(100, 20));
        cboMinDepositPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboMinDepositPeriodActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMinDepositPeriod.add(cboMinDepositPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panMinDepositPeriod, gridBagConstraints);

        panDepositPerUnit.setLayout(new java.awt.GridBagLayout());

        txtDepositPerUnit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDepositPerUnit.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panDepositPerUnit.add(txtDepositPerUnit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(panDepositPerUnit, gridBagConstraints);

        lblMinDepositAmt.setText("Minimum Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblMinDepositAmt, gridBagConstraints);

        txtAmtInMultiplesOf.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtInMultiplesOf.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(txtAmtInMultiplesOf, gridBagConstraints);

        lblMaxDepositAmt.setText("Maximum Deposit Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblMaxDepositAmt, gridBagConstraints);

        txtMinDepositAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinDepositAmt.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(txtMinDepositAmt, gridBagConstraints);

        lblAmtInMultiplesOf.setText("Amount in Multiples of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblAmtInMultiplesOf, gridBagConstraints);

        txtMaxDepositAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxDepositAmt.setPreferredSize(new java.awt.Dimension(100, 20));
        txtMaxDepositAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDepositAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(txtMaxDepositAmt, gridBagConstraints);

        panAccHead.setMinimumSize(new java.awt.Dimension(129, 19));
        panAccHead.setPreferredSize(new java.awt.Dimension(129, 21));
        panAccHead.setLayout(new java.awt.GridBagLayout());

        txtAcctHd.setAllowAll(true);
        txtAcctHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctHd.setPreferredSize(new java.awt.Dimension(100, 20));
        txtAcctHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAccHead.add(txtAcctHd, gridBagConstraints);

        btnAcctHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcctHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panAccHead.add(btnAcctHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHd.add(panAccHead, gridBagConstraints);

        lblProdCurrency.setText("Product Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 2, 4);
        panAcHd.add(lblProdCurrency, gridBagConstraints);

        cboProdCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdCurrency.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(cboProdCurrency, gridBagConstraints);

        lblInterestOnMaturedDeposits.setText("Interest on Matured Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblInterestOnMaturedDeposits, gridBagConstraints);

        txtInterestOnMaturedDeposits.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInterestOnMaturedDeposits.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(txtInterestOnMaturedDeposits, gridBagConstraints);

        lblAfterHowManyDays.setText("After How Many Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblAfterHowManyDays, gridBagConstraints);

        panAfterHowManyDays.setLayout(new java.awt.GridBagLayout());

        txtAfterHowManyDays.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAfterHowManyDays.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panAfterHowManyDays.add(txtAfterHowManyDays, gridBagConstraints);

        lblInterestOnMaturedDepositsDays.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panAfterHowManyDays.add(lblInterestOnMaturedDepositsDays, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 4);
        panAcHd.add(panAfterHowManyDays, gridBagConstraints);

        panDiscounted.setMinimumSize(new java.awt.Dimension(101, 18));
        panDiscounted.setLayout(new java.awt.GridBagLayout());

        rdgDiscounted_Rate.add(rdoDiscounted_Yes);
        rdoDiscounted_Yes.setText("Yes");
        rdoDiscounted_Yes.setMaximumSize(new java.awt.Dimension(55, 27));
        rdoDiscounted_Yes.setMinimumSize(new java.awt.Dimension(55, 27));
        rdoDiscounted_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoDiscounted_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscounted_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDiscounted.add(rdoDiscounted_Yes, gridBagConstraints);

        rdgDiscounted_Rate.add(rdoDiscounted_No);
        rdoDiscounted_No.setText("No");
        rdoDiscounted_No.setMaximumSize(new java.awt.Dimension(41, 21));
        rdoDiscounted_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoDiscounted_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panDiscounted.add(rdoDiscounted_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panDiscounted, gridBagConstraints);

        lblDiscounted.setText("Discounted Rate For Monthly IntPay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblDiscounted, gridBagConstraints);

        panIntroducerReqd.setPreferredSize(new java.awt.Dimension(101, 18));
        panIntroducerReqd.setLayout(new java.awt.GridBagLayout());

        rdgIntroducerReqd.add(rdoIntroducerReqd_Yes);
        rdoIntroducerReqd_Yes.setText("Yes");
        rdoIntroducerReqd_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoIntroducerReqd_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIntroducerReqd.add(rdoIntroducerReqd_Yes, gridBagConstraints);

        rdgIntroducerReqd.add(rdoIntroducerReqd_No);
        rdoIntroducerReqd_No.setText("No");
        rdoIntroducerReqd_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoIntroducerReqd_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIntroducerReqd.add(rdoIntroducerReqd_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(panIntroducerReqd, gridBagConstraints);

        lblIntroducerReqd.setText("Introducer Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panAcHd.add(lblIntroducerReqd, gridBagConstraints);

        panTypeOfDeposit.setMinimumSize(new java.awt.Dimension(175, 18));
        panTypeOfDeposit.setPreferredSize(new java.awt.Dimension(175, 18));
        panTypeOfDeposit.setLayout(new java.awt.GridBagLayout());

        rdgIntroducerReqd.add(rdoRegular);
        rdoRegular.setText("Normal");
        rdoRegular.setMinimumSize(new java.awt.Dimension(82, 18));
        rdoRegular.setPreferredSize(new java.awt.Dimension(66, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTypeOfDeposit.add(rdoRegular, gridBagConstraints);

        rdgIntroducerReqd.add(rdoNRO);
        rdoNRO.setText("NRO");
        rdoNRO.setMinimumSize(new java.awt.Dimension(33, 27));
        rdoNRO.setPreferredSize(new java.awt.Dimension(30, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panTypeOfDeposit.add(rdoNRO, gridBagConstraints);

        rdgIntroducerReqd.add(rdoNRE);
        rdoNRE.setText("NRE");
        rdoNRE.setMinimumSize(new java.awt.Dimension(33, 27));
        rdoNRE.setPreferredSize(new java.awt.Dimension(25, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panTypeOfDeposit.add(rdoNRE, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panTypeOfDeposit, gridBagConstraints);

        lblTypesOfDeposit.setText("Type of Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblTypesOfDeposit, gridBagConstraints);

        panWithPeriod.setMinimumSize(new java.awt.Dimension(101, 18));
        panWithPeriod.setLayout(new java.awt.GridBagLayout());

        rdgWithPeriod.add(rdoWithPeriod_Yes);
        rdoWithPeriod_Yes.setText("Yes");
        rdoWithPeriod_Yes.setMaximumSize(new java.awt.Dimension(55, 27));
        rdoWithPeriod_Yes.setMinimumSize(new java.awt.Dimension(55, 27));
        rdoWithPeriod_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoWithPeriod_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWithPeriod_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWithPeriod.add(rdoWithPeriod_Yes, gridBagConstraints);

        rdgWithPeriod.add(rdoWithPeriod_No);
        rdoWithPeriod_No.setText("No");
        rdoWithPeriod_No.setMaximumSize(new java.awt.Dimension(41, 21));
        rdoWithPeriod_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoWithPeriod_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoWithPeriod_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWithPeriod_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panWithPeriod.add(rdoWithPeriod_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(panWithPeriod, gridBagConstraints);

        lblWithPeriod.setText("With Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblWithPeriod, gridBagConstraints);

        panDoublingScheme.setMinimumSize(new java.awt.Dimension(101, 18));
        panDoublingScheme.setLayout(new java.awt.GridBagLayout());

        rdgDoublingScheme.add(rdoDoublingScheme_Yes);
        rdoDoublingScheme_Yes.setText("Yes");
        rdoDoublingScheme_Yes.setMaximumSize(new java.awt.Dimension(55, 27));
        rdoDoublingScheme_Yes.setMinimumSize(new java.awt.Dimension(55, 27));
        rdoDoublingScheme_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoDoublingScheme_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDoublingScheme_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDoublingScheme.add(rdoDoublingScheme_Yes, gridBagConstraints);

        rdgDoublingScheme.add(rdoDoublingScheme_No);
        rdoDoublingScheme_No.setText("No");
        rdoDoublingScheme_No.setMaximumSize(new java.awt.Dimension(41, 21));
        rdoDoublingScheme_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoDoublingScheme_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoDoublingScheme_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDoublingScheme_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panDoublingScheme.add(rdoDoublingScheme_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAcHd.add(panDoublingScheme, gridBagConstraints);

        lblDoublingScheme.setText("Doubling Scheme");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panAcHd.add(lblDoublingScheme, gridBagConstraints);

        txtDoublingCount.setAllowAll(true);
        txtDoublingCount.setMaximumSize(new java.awt.Dimension(60, 21));
        txtDoublingCount.setMinimumSize(new java.awt.Dimension(60, 21));
        txtDoublingCount.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        panAcHd.add(txtDoublingCount, gridBagConstraints);

        chkIsGroupDeposit.setText("Is Group Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        panAcHd.add(chkIsGroupDeposit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 2);
        panScheme.add(panAcHd, gridBagConstraints);

        panProvisionOfInterest.setMinimumSize(new java.awt.Dimension(460, 590));
        panProvisionOfInterest.setPreferredSize(new java.awt.Dimension(460, 590));
        panProvisionOfInterest.setLayout(new java.awt.GridBagLayout());

        lblFlexiFromSBCA.setText("Flexi (Float) from SB/CA Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblFlexiFromSBCA, gridBagConstraints);

        lblExtnOfDepositBeforeMaturity.setText("Extension of Deposit before Maturity Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblExtnOfDepositBeforeMaturity, gridBagConstraints);

        lblPrematureWithdrawal.setText("Premature Withdrawal to be Allowed for");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblPrematureWithdrawal, gridBagConstraints);

        lblTermDeposit.setText("Term Deposit can be Offered as Security to OD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblTermDeposit, gridBagConstraints);

        lblAlphaSuffix.setText("Alpha Suffix for Printing in TD Receipt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblAlphaSuffix, gridBagConstraints);

        txtAlphaSuffix.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(txtAlphaSuffix, gridBagConstraints);

        lblMaxAmtOfCashPayment.setText("Max.Amt.of Cash Payment for Matured Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblMaxAmtOfCashPayment, gridBagConstraints);

        txtMaxAmtOfCashPayment.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(txtMaxAmtOfCashPayment, gridBagConstraints);

        lblMinAmtOfPAN.setText("Minimum Amount for PAN");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblMinAmtOfPAN, gridBagConstraints);

        txtMinAmtOfPAN.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(txtMinAmtOfPAN, gridBagConstraints);

        lblAcctNumberPattern.setText("Account Number Pattern to be Followed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblAcctNumberPattern, gridBagConstraints);

        lblSchemeIntroDate.setText("Scheme Introduction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblSchemeIntroDate, gridBagConstraints);

        lblLimitForBulkDeposit.setText("Limit for Bulk Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblLimitForBulkDeposit, gridBagConstraints);

        txtLimitForBulkDeposit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(txtLimitForBulkDeposit, gridBagConstraints);

        lblAdvanceMaturityNoticeGenPeriod.setText("Advance Maturity Notice Generation Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblAdvanceMaturityNoticeGenPeriod, gridBagConstraints);

        panTransferToMaturedDeposits.setLayout(new java.awt.GridBagLayout());

        rdgTransferToMaturedDeposits.add(rdoTransferToMaturedDeposits_Yes);
        rdoTransferToMaturedDeposits_Yes.setText("Yes");
        rdoTransferToMaturedDeposits_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoTransferToMaturedDeposits_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoTransferToMaturedDeposits_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransferToMaturedDeposits_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panTransferToMaturedDeposits.add(rdoTransferToMaturedDeposits_Yes, gridBagConstraints);

        rdgTransferToMaturedDeposits.add(rdoTransferToMaturedDeposits_No);
        rdoTransferToMaturedDeposits_No.setText("No");
        rdoTransferToMaturedDeposits_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoTransferToMaturedDeposits_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoTransferToMaturedDeposits_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTransferToMaturedDeposits_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panTransferToMaturedDeposits.add(rdoTransferToMaturedDeposits_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(panTransferToMaturedDeposits, gridBagConstraints);

        lblTransferToMaturedDeposits.setText("Transfer to Matured Deposits on Maturity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblTransferToMaturedDeposits, gridBagConstraints);

        panInCaseOfLien.setBorder(javax.swing.BorderFactory.createTitledBorder("In case of Lien"));
        panInCaseOfLien.setMinimumSize(new java.awt.Dimension(388, 76));
        panInCaseOfLien.setName(""); // NOI18N
        panInCaseOfLien.setPreferredSize(new java.awt.Dimension(388, 76));
        panInCaseOfLien.setLayout(new java.awt.GridBagLayout());

        lblAutoAdjustment.setText("Auto Adjustment of Maturity Proceeds to Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panInCaseOfLien.add(lblAutoAdjustment, gridBagConstraints);

        lblAdjustIntOnDeposits.setText("Adjust only Interest on Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panInCaseOfLien.add(lblAdjustIntOnDeposits, gridBagConstraints);

        lblAdjustPrincipleToLoan.setText("Adjust only Priniciple to Loan");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 4);
        panInCaseOfLien.add(lblAdjustPrincipleToLoan, gridBagConstraints);

        rdgAutoAdjustment.add(rdoAutoAdjustment_Yes);
        rdoAutoAdjustment_Yes.setText("Yes");
        rdoAutoAdjustment_Yes.setMaximumSize(new java.awt.Dimension(50, 27));
        rdoAutoAdjustment_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoAutoAdjustment_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 0);
        panInCaseOfLien.add(rdoAutoAdjustment_Yes, gridBagConstraints);

        rdgAutoAdjustment.add(rdoAutoAdjustment_No);
        rdoAutoAdjustment_No.setText("No");
        rdoAutoAdjustment_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoAutoAdjustment_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panInCaseOfLien.add(rdoAutoAdjustment_No, gridBagConstraints);

        rdgAdjustIntOnDeposits.add(rdoAdjustIntOnDeposits_No);
        rdoAdjustIntOnDeposits_No.setText("No");
        rdoAdjustIntOnDeposits_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoAdjustIntOnDeposits_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panInCaseOfLien.add(rdoAdjustIntOnDeposits_No, gridBagConstraints);

        rdgAdjustIntOnDeposits.add(rdoAdjustIntOnDeposits_Yes);
        rdoAdjustIntOnDeposits_Yes.setText("Yes");
        rdoAdjustIntOnDeposits_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoAdjustIntOnDeposits_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 0);
        panInCaseOfLien.add(rdoAdjustIntOnDeposits_Yes, gridBagConstraints);

        rdgAdjustPrincipleToLoan.add(rdoAdjustPrincipleToLoan_Yes);
        rdoAdjustPrincipleToLoan_Yes.setText("Yes");
        rdoAdjustPrincipleToLoan_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoAdjustPrincipleToLoan_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 5, 0);
        panInCaseOfLien.add(rdoAdjustPrincipleToLoan_Yes, gridBagConstraints);

        rdgAdjustPrincipleToLoan.add(rdoAdjustPrincipleToLoan_No);
        rdoAdjustPrincipleToLoan_No.setText("No");
        rdoAdjustPrincipleToLoan_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoAdjustPrincipleToLoan_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        panInCaseOfLien.add(rdoAdjustPrincipleToLoan_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panProvisionOfInterest.add(panInCaseOfLien, gridBagConstraints);

        panExtnOfDepositBeforeMaturity.setLayout(new java.awt.GridBagLayout());

        rdgExtnOfDepositBeforeMaturity.add(rdoExtnOfDepositBeforeMaturity_Yes);
        rdoExtnOfDepositBeforeMaturity_Yes.setText("Yes");
        rdoExtnOfDepositBeforeMaturity_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoExtnOfDepositBeforeMaturity_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panExtnOfDepositBeforeMaturity.add(rdoExtnOfDepositBeforeMaturity_Yes, gridBagConstraints);

        rdgExtnOfDepositBeforeMaturity.add(rdoExtnOfDepositBeforeMaturity_No);
        rdoExtnOfDepositBeforeMaturity_No.setText("No");
        rdoExtnOfDepositBeforeMaturity_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoExtnOfDepositBeforeMaturity_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panExtnOfDepositBeforeMaturity.add(rdoExtnOfDepositBeforeMaturity_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panProvisionOfInterest.add(panExtnOfDepositBeforeMaturity, gridBagConstraints);

        panAdvanceMaturityNoticeGenPeriod.setLayout(new java.awt.GridBagLayout());

        txtAdvanceMaturityNoticeGenPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAdvanceMaturityNoticeGenPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panAdvanceMaturityNoticeGenPeriod.add(txtAdvanceMaturityNoticeGenPeriod, gridBagConstraints);

        lblAfterHowManyDaysDisplay.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panAdvanceMaturityNoticeGenPeriod.add(lblAfterHowManyDaysDisplay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panProvisionOfInterest.add(panAdvanceMaturityNoticeGenPeriod, gridBagConstraints);

        panFlexiFromSBCA.setLayout(new java.awt.GridBagLayout());

        rdgFlexiFromSBCA.add(rdoFlexiFromSBCA_Yes);
        rdoFlexiFromSBCA_Yes.setText("Yes");
        rdoFlexiFromSBCA_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoFlexiFromSBCA_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFlexiFromSBCA.add(rdoFlexiFromSBCA_Yes, gridBagConstraints);

        rdgFlexiFromSBCA.add(rdoFlexiFromSBCA_No);
        rdoFlexiFromSBCA_No.setText("No");
        rdoFlexiFromSBCA_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoFlexiFromSBCA_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panFlexiFromSBCA.add(rdoFlexiFromSBCA_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panProvisionOfInterest.add(panFlexiFromSBCA, gridBagConstraints);

        panTermDeposit.setLayout(new java.awt.GridBagLayout());

        rdgTermDeposit.add(rdoTermDeposit_Yes);
        rdoTermDeposit_Yes.setText("Yes");
        rdoTermDeposit_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoTermDeposit_Yes.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTermDeposit.add(rdoTermDeposit_Yes, gridBagConstraints);

        rdgTermDeposit.add(rdoTermDeposit_No);
        rdoTermDeposit_No.setText("No");
        rdoTermDeposit_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoTermDeposit_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panTermDeposit.add(rdoTermDeposit_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panProvisionOfInterest.add(panTermDeposit, gridBagConstraints);

        panSystemCalcValues.setMinimumSize(new java.awt.Dimension(400, 55));
        panSystemCalcValues.setPreferredSize(new java.awt.Dimension(400, 55));
        panSystemCalcValues.setLayout(new java.awt.GridBagLayout());

        rdgSystemCalcValues.add(rdoSystemCalcValues_Yes);
        rdoSystemCalcValues_Yes.setText("Account Authorizing Time");
        rdoSystemCalcValues_Yes.setMinimumSize(new java.awt.Dimension(250, 18));
        rdoSystemCalcValues_Yes.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panSystemCalcValues.add(rdoSystemCalcValues_Yes, gridBagConstraints);

        rdgSystemCalcValues.add(rdoSystemCalcValues_No);
        rdoSystemCalcValues_No.setText("Seperate Printing Window");
        rdoSystemCalcValues_No.setMinimumSize(new java.awt.Dimension(250, 18));
        rdoSystemCalcValues_No.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panSystemCalcValues.add(rdoSystemCalcValues_No, gridBagConstraints);

        rdgSystemCalcValues.add(rdoSystemCalcValues_Print_accOpen);
        rdoSystemCalcValues_Print_accOpen.setText("Account Opening Time");
        rdoSystemCalcValues_Print_accOpen.setMaximumSize(new java.awt.Dimension(250, 18));
        rdoSystemCalcValues_Print_accOpen.setMinimumSize(new java.awt.Dimension(250, 18));
        rdoSystemCalcValues_Print_accOpen.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panSystemCalcValues.add(rdoSystemCalcValues_Print_accOpen, gridBagConstraints);

        lblSystemCalcValues.setText("Certificate Printing Priority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panSystemCalcValues.add(lblSystemCalcValues, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        panProvisionOfInterest.add(panSystemCalcValues, gridBagConstraints);

        panCalcMaturityValue.setMinimumSize(new java.awt.Dimension(95, 27));
        panCalcMaturityValue.setPreferredSize(new java.awt.Dimension(95, 27));
        panCalcMaturityValue.setLayout(new java.awt.GridBagLayout());

        rdgCalcMaturityValue.add(rdoCalcMaturityValue_Yes);
        rdoCalcMaturityValue_Yes.setText("Yes");
        rdoCalcMaturityValue_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoCalcMaturityValue_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panCalcMaturityValue.add(rdoCalcMaturityValue_Yes, gridBagConstraints);

        rdgCalcMaturityValue.add(rdoCalcMaturityValue_No);
        rdoCalcMaturityValue_No.setText("No");
        rdoCalcMaturityValue_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoCalcMaturityValue_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panCalcMaturityValue.add(rdoCalcMaturityValue_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panProvisionOfInterest.add(panCalcMaturityValue, gridBagConstraints);

        lblCalcMaturityValue.setText("Calculate Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblCalcMaturityValue, gridBagConstraints);

        panRdgProvisionOfInterest.setLayout(new java.awt.GridBagLayout());

        rdgProvisionOfInterest.add(rdoProvisionOfInterest_Yes);
        rdoProvisionOfInterest_Yes.setText("Yes");
        rdoProvisionOfInterest_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoProvisionOfInterest_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        rdoProvisionOfInterest_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoProvisionOfInterest_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRdgProvisionOfInterest.add(rdoProvisionOfInterest_Yes, gridBagConstraints);

        rdgProvisionOfInterest.add(rdoProvisionOfInterest_No);
        rdoProvisionOfInterest_No.setText("No");
        rdoProvisionOfInterest_No.setMaximumSize(new java.awt.Dimension(46, 27));
        rdoProvisionOfInterest_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoProvisionOfInterest_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoProvisionOfInterest_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoProvisionOfInterest_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRdgProvisionOfInterest.add(rdoProvisionOfInterest_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 1, 4);
        panProvisionOfInterest.add(panRdgProvisionOfInterest, gridBagConstraints);

        lblProvisionOfInterest.setText("Provision of Int. on Matured Deposits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblProvisionOfInterest, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(tdtSchemeIntroDate, gridBagConstraints);

        lblAfterNoDays.setText("After No Of Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblAfterNoDays, gridBagConstraints);

        txtAfterNoDays.setText("\n");
        txtAfterNoDays.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(txtAfterNoDays, gridBagConstraints);

        txtLastAcctNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(txtLastAcctNumber, gridBagConstraints);

        lblAcctNumberPattern1.setText("Next Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 2);
        panProvisionOfInterest.add(lblAcctNumberPattern1, gridBagConstraints);

        panNumberPattern.setMinimumSize(new java.awt.Dimension(100, 21));
        panNumberPattern.setPreferredSize(new java.awt.Dimension(100, 21));
        panNumberPattern.setLayout(new java.awt.GridBagLayout());

        txtAcctNumberPattern.setMinimumSize(new java.awt.Dimension(40, 21));
        txtAcctNumberPattern.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panNumberPattern.add(txtAcctNumberPattern, gridBagConstraints);

        txtSuffix.setMinimumSize(new java.awt.Dimension(52, 21));
        txtSuffix.setPreferredSize(new java.awt.Dimension(52, 21));
        panNumberPattern.add(txtSuffix, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 10);
        panProvisionOfInterest.add(panNumberPattern, gridBagConstraints);

        lblSchemeClosingDate.setText("Scheme Closing Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblSchemeClosingDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(tdtSchemeClosingDate, gridBagConstraints);

        panPrematureWithdrawal.setLayout(new java.awt.GridBagLayout());

        txtPrematureWithdrawal.setMinimumSize(new java.awt.Dimension(25, 21));
        txtPrematureWithdrawal.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panPrematureWithdrawal.add(txtPrematureWithdrawal, gridBagConstraints);

        cboPrematureWithdrawal.setMinimumSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panPrematureWithdrawal.add(cboPrematureWithdrawal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(panPrematureWithdrawal, gridBagConstraints);

        panTransferToMaturedDeposits1.setLayout(new java.awt.GridBagLayout());

        rdgTransferToMaturedDeposits.add(rdoStaffAccount_Yes);
        rdoStaffAccount_Yes.setText("Yes");
        rdoStaffAccount_Yes.setMinimumSize(new java.awt.Dimension(50, 18));
        rdoStaffAccount_Yes.setPreferredSize(new java.awt.Dimension(50, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panTransferToMaturedDeposits1.add(rdoStaffAccount_Yes, gridBagConstraints);

        rdgTransferToMaturedDeposits.add(rdoStaffAccount_No);
        rdoStaffAccount_No.setText("No");
        rdoStaffAccount_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoStaffAccount_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panTransferToMaturedDeposits1.add(rdoStaffAccount_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(panTransferToMaturedDeposits1, gridBagConstraints);

        lblStaffAccount.setText("Staff Account Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblStaffAccount, gridBagConstraints);

        lblCalcMaturityValue1.setText("Calculate Maturity Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblCalcMaturityValue1, gridBagConstraints);

        lblFDRenewalSameNoTranPrincAmt.setText("FD renewal with same No transaction for principal Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblFDRenewalSameNoTranPrincAmt, gridBagConstraints);

        chkFDRenewalSameNoTranPrincAmt.setMinimumSize(new java.awt.Dimension(18, 18));
        chkFDRenewalSameNoTranPrincAmt.setPreferredSize(new java.awt.Dimension(18, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(chkFDRenewalSameNoTranPrincAmt, gridBagConstraints);

        lblCanZeroBalAct.setText("Can Account be opened with Zero Balance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(lblCanZeroBalAct, gridBagConstraints);

        chkCanZeroBalAct.setMinimumSize(new java.awt.Dimension(18, 18));
        chkCanZeroBalAct.setPreferredSize(new java.awt.Dimension(18, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panProvisionOfInterest.add(chkCanZeroBalAct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 0);
        panScheme.add(panProvisionOfInterest, gridBagConstraints);

        sptCentre.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptCentre.setToolTipText("Seperator");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panScheme.add(sptCentre, gridBagConstraints);

        tabDepositsProduct.addTab("Scheme", panScheme);

        panWithDrawIntPay.setPreferredSize(new java.awt.Dimension(950, 637));
        panWithDrawIntPay.setLayout(new java.awt.GridBagLayout());

        panWithdrawal.setBorder(javax.swing.BorderFactory.createTitledBorder("Withdrawal"));
        panWithdrawal.setMinimumSize(new java.awt.Dimension(650, 240));
        panWithdrawal.setPreferredSize(new java.awt.Dimension(650, 240));
        panWithdrawal.setLayout(new java.awt.GridBagLayout());

        lblPartialWithdrawalAllowed.setText("Partial Withdrawal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblPartialWithdrawalAllowed, gridBagConstraints);

        lblNoOfPartialWithdrawalAllowed.setText("Number of Partial Withdrawal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblNoOfPartialWithdrawalAllowed, gridBagConstraints);

        txtNoOfPartialWithdrawalAllowed.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoOfPartialWithdrawalAllowed.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(txtNoOfPartialWithdrawalAllowed, gridBagConstraints);

        lblMaxAmtOfPartialWithdrawalAllowed.setText("Maximum Amount of Partial Withdrawal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblMaxAmtOfPartialWithdrawalAllowed, gridBagConstraints);

        txtMaxAmtOfPartialWithdrawalAllowed.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxAmtOfPartialWithdrawalAllowed.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMaxAmtOfPartialWithdrawalAllowed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxAmtOfPartialWithdrawalAllowedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(txtMaxAmtOfPartialWithdrawalAllowed, gridBagConstraints);

        lblMaxNoOfPartialWithdrawalAllowed.setText("Maximum Number of Partial Withdrawal in a Year");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblMaxNoOfPartialWithdrawalAllowed, gridBagConstraints);

        txtMaxNoOfPartialWithdrawalAllowed.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxNoOfPartialWithdrawalAllowed.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMaxNoOfPartialWithdrawalAllowed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxNoOfPartialWithdrawalAllowedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(txtMaxNoOfPartialWithdrawalAllowed, gridBagConstraints);

        txtMinAmtOfPartialWithdrawalAllowed.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinAmtOfPartialWithdrawalAllowed.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMinAmtOfPartialWithdrawalAllowedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(txtMinAmtOfPartialWithdrawalAllowed, gridBagConstraints);

        lblMinAmtOfPartialWithdrawalAllowed.setText("Minimum Amount of Partial Withdrawal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblMinAmtOfPartialWithdrawalAllowed, gridBagConstraints);

        lblWithdrawalsInMultiplesOf.setText("Further Withdrawals in Multiples of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblWithdrawalsInMultiplesOf, gridBagConstraints);

        txtWithdrawalsInMultiplesOf.setMinimumSize(new java.awt.Dimension(50, 21));
        txtWithdrawalsInMultiplesOf.setPreferredSize(new java.awt.Dimension(50, 21));
        txtWithdrawalsInMultiplesOf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtWithdrawalsInMultiplesOfFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(txtWithdrawalsInMultiplesOf, gridBagConstraints);

        lblWithdrawalWithInterest.setText(" Withdrawal with Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblWithdrawalWithInterest, gridBagConstraints);

        panPartialWithdrawalAllowed.setLayout(new java.awt.GridBagLayout());

        rdgPartialWithdrawalAllowed.add(rdoPartialWithdrawalAllowed_Yes);
        rdoPartialWithdrawalAllowed_Yes.setText("Yes");
        rdoPartialWithdrawalAllowed_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoPartialWithdrawalAllowed_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoPartialWithdrawalAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPartialWithdrawalAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPartialWithdrawalAllowed.add(rdoPartialWithdrawalAllowed_Yes, gridBagConstraints);

        rdgPartialWithdrawalAllowed.add(rdoPartialWithdrawalAllowed_No);
        rdoPartialWithdrawalAllowed_No.setText("No");
        rdoPartialWithdrawalAllowed_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoPartialWithdrawalAllowed_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoPartialWithdrawalAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPartialWithdrawalAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panPartialWithdrawalAllowed.add(rdoPartialWithdrawalAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        panWithdrawal.add(panPartialWithdrawalAllowed, gridBagConstraints);

        panWithdrawalWithInterest.setLayout(new java.awt.GridBagLayout());

        rdgWithdrawalWithInterest.add(rdoWithdrawalWithInterest_Yes);
        rdoWithdrawalWithInterest_Yes.setText("Yes");
        rdoWithdrawalWithInterest_Yes.setMaximumSize(new java.awt.Dimension(49, 18));
        rdoWithdrawalWithInterest_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoWithdrawalWithInterest_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWithdrawalWithInterest.add(rdoWithdrawalWithInterest_Yes, gridBagConstraints);

        rdgWithdrawalWithInterest.add(rdoWithdrawalWithInterest_No);
        rdoWithdrawalWithInterest_No.setText("No");
        rdoWithdrawalWithInterest_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoWithdrawalWithInterest_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panWithdrawalWithInterest.add(rdoWithdrawalWithInterest_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWithdrawal.add(panWithdrawalWithInterest, gridBagConstraints);

        lblMaxAmtPartialWithdrawalPercent.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWithdrawal.add(lblMaxAmtPartialWithdrawalPercent, gridBagConstraints);

        panServiceCharge.setLayout(new java.awt.GridBagLayout());

        rdgServiceCharge.add(rdoServiceCharge_Yes);
        rdoServiceCharge_Yes.setText("Yes");
        rdoServiceCharge_Yes.setMaximumSize(new java.awt.Dimension(49, 18));
        rdoServiceCharge_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoServiceCharge_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoServiceCharge_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoServiceCharge_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panServiceCharge.add(rdoServiceCharge_Yes, gridBagConstraints);

        rdgServiceCharge.add(rdoServiceCharge_No);
        rdoServiceCharge_No.setText("No");
        rdoServiceCharge_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoServiceCharge_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoServiceCharge_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoServiceCharge_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panServiceCharge.add(rdoServiceCharge_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWithdrawal.add(panServiceCharge, gridBagConstraints);

        lblServiceCharge.setText("Service Charge Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblServiceCharge, gridBagConstraints);

        lblServiceChargePercentage.setText("Service Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(lblServiceChargePercentage, gridBagConstraints);

        txtServiceCharge.setMinimumSize(new java.awt.Dimension(50, 21));
        txtServiceCharge.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panWithdrawal.add(txtServiceCharge, gridBagConstraints);

        lblServiceChargePercent.setText("%");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panWithdrawal.add(lblServiceChargePercent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panWithDrawIntPay.add(panWithdrawal, gridBagConstraints);

        panInterestPayment.setBorder(javax.swing.BorderFactory.createTitledBorder("Interest Payment"));
        panInterestPayment.setMinimumSize(new java.awt.Dimension(820, 380));
        panInterestPayment.setPreferredSize(new java.awt.Dimension(820, 380));
        panInterestPayment.setLayout(new java.awt.GridBagLayout());

        panInterest.setMinimumSize(new java.awt.Dimension(400, 350));
        panInterest.setPreferredSize(new java.awt.Dimension(400, 350));
        panInterest.setLayout(new java.awt.GridBagLayout());

        cboIntCalcMethod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboIntCalcMethod, gridBagConstraints);

        lblIntMaintainedAsPartOf.setText("Interest to be Maintained as Part of");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblIntMaintainedAsPartOf, gridBagConstraints);

        cboIntMaintainedAsPartOf.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboIntMaintainedAsPartOf, gridBagConstraints);

        lblIntCalcMethod.setText("Interest Calculation Method");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblIntCalcMethod, gridBagConstraints);

        panIntProvisioningApplicable.setLayout(new java.awt.GridBagLayout());

        rdgIntProvisioningApplicable.add(rdoIntProvisioningApplicable_Yes);
        rdoIntProvisioningApplicable_Yes.setText("Yes");
        rdoIntProvisioningApplicable_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoIntProvisioningApplicable_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoIntProvisioningApplicable_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIntProvisioningApplicable_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIntProvisioningApplicable.add(rdoIntProvisioningApplicable_Yes, gridBagConstraints);

        rdgIntProvisioningApplicable.add(rdoIntProvisioningApplicable_No);
        rdoIntProvisioningApplicable_No.setText("No");
        rdoIntProvisioningApplicable_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoIntProvisioningApplicable_No.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoIntProvisioningApplicable_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIntProvisioningApplicable_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panIntProvisioningApplicable.add(rdoIntProvisioningApplicable_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panInterest.add(panIntProvisioningApplicable, gridBagConstraints);

        lblIntProvisioningApplicable.setText("Interest Provisioning Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblIntProvisioningApplicable, gridBagConstraints);

        lblIntProvisioningFreq.setText("Interest Provisioning Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblIntProvisioningFreq, gridBagConstraints);

        cboIntProvisioningFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboIntProvisioningFreq, gridBagConstraints);

        lblProvisioningLevel.setText("Provisioning Level");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblProvisioningLevel, gridBagConstraints);

        cboProvisioningLevel.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboProvisioningLevel, gridBagConstraints);

        lblNoOfDays.setText("No. of Days in a Year");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblNoOfDays, gridBagConstraints);

        cboNoOfDays.setMinimumSize(new java.awt.Dimension(50, 21));
        cboNoOfDays.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboNoOfDays, gridBagConstraints);

        lblIntCompoundingFreq.setText("Interest Compounding Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblIntCompoundingFreq, gridBagConstraints);

        cboIntCompoundingFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboIntCompoundingFreq, gridBagConstraints);

        lblInterestType.setText("Interest Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblInterestType, gridBagConstraints);

        cboInterestType.setPopupWidth(125);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboInterestType, gridBagConstraints);

        lblIntApplnFreq.setText("Interest Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblIntApplnFreq, gridBagConstraints);

        cboIntApplnFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(cboIntApplnFreq, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterest.add(chkPrematureClosure, gridBagConstraints);

        lblPrematureClosure.setText("Premature Closure in SI Rate");
        lblPrematureClosure.setFocusable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panInterest.add(lblPrematureClosure, gridBagConstraints);

        lblAppNormRate.setText("Apply Normal Rate for Senior Citizen in Prem.Closure");
        lblAppNormRate.setMaximumSize(new java.awt.Dimension(303, 18));
        lblAppNormRate.setMinimumSize(new java.awt.Dimension(303, 18));
        lblAppNormRate.setPreferredSize(new java.awt.Dimension(303, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panInterest.add(lblAppNormRate, gridBagConstraints);

        lblNormPeriod.setText("Apply Normal rate Up to(Months)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterest.add(lblNormPeriod, gridBagConstraints);

        txtNormPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterest.add(txtNormPeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterest.add(chkAppNormRate, gridBagConstraints);

        lblCategoryBenifitRate.setText("Category Benifit Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterest.add(lblCategoryBenifitRate, gridBagConstraints);

        txtCategoryBenifitRate.setAllowAll(true);
        txtCategoryBenifitRate.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterest.add(txtCategoryBenifitRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterestPayment.add(panInterest, gridBagConstraints);

        panDate.setMinimumSize(new java.awt.Dimension(385, 320));
        panDate.setPreferredSize(new java.awt.Dimension(385, 320));
        panDate.setLayout(new java.awt.GridBagLayout());

        lblNextInterestAppliedDate.setText("Next Interest Applied Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblNextInterestAppliedDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(tdtNextInterestAppliedDate, gridBagConstraints);

        lblLastInterestAppliedDate.setText("Last Interest Applied Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblLastInterestAppliedDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(tdtLastInterestAppliedDate, gridBagConstraints);

        lblNextIntProvisionalDate.setText("Next Interest Provisional Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblNextIntProvisionalDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(tdtNextIntProvisionalDate, gridBagConstraints);

        lblLastIntProvisionalDate.setText("Last Interest Provisional Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblLastIntProvisionalDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(tdtLastIntProvisionalDate, gridBagConstraints);

        lblIntRoundOff.setText("Interest Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblIntRoundOff, gridBagConstraints);

        lblMinIntToBePaid.setText("Minimum Interest to be Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblMinIntToBePaid, gridBagConstraints);

        txtMinIntToBePaid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(txtMinIntToBePaid, gridBagConstraints);

        cboDepositsProdFixd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(cboDepositsProdFixd, gridBagConstraints);

        lblDepositsProdFixd.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblDepositsProdFixd, gridBagConstraints);

        cPanel1.setMinimumSize(new java.awt.Dimension(300, 18));
        cPanel1.setPreferredSize(new java.awt.Dimension(300, 18));
        cPanel1.setLayout(new java.awt.GridBagLayout());

        cLabel1.setText("Rounding done at Interest Application");
        cPanel1.add(cLabel1, new java.awt.GridBagConstraints());
        cPanel1.add(cbxInterstRoundTime, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        panDate.add(cPanel1, gridBagConstraints);

        panPrematureClosureApply.setMinimumSize(new java.awt.Dimension(200, 18));
        panPrematureClosureApply.setPreferredSize(new java.awt.Dimension(200, 18));
        panPrematureClosureApply.setLayout(new java.awt.GridBagLayout());

        rdgIntProvisioningApplicable.add(rdoDepositRate);
        rdoDepositRate.setText("Deposit Rate");
        rdoDepositRate.setMinimumSize(new java.awt.Dimension(69, 18));
        rdoDepositRate.setPreferredSize(new java.awt.Dimension(100, 18));
        rdoDepositRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDepositRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPrematureClosureApply.add(rdoDepositRate, gridBagConstraints);

        rdgIntProvisioningApplicable.add(rdoSBRate);
        rdoSBRate.setText("SB Rate");
        rdoSBRate.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoSBRate.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoSBRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSBRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panPrematureClosureApply.add(rdoSBRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 4);
        panDate.add(panPrematureClosureApply, gridBagConstraints);

        cboIntRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDate.add(cboIntRoundOff, gridBagConstraints);

        lblPrematureClosureApply.setText("Premature Closure Apply");
        lblPrematureClosureApply.setMaximumSize(new java.awt.Dimension(0, 0));
        lblPrematureClosureApply.setMinimumSize(new java.awt.Dimension(0, 0));
        lblPrematureClosureApply.setPreferredSize(new java.awt.Dimension(0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblPrematureClosureApply, gridBagConstraints);

        lblDifferentROI.setText("          Different ROI");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblDifferentROI, gridBagConstraints);

        chkDifferentROI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDifferentROIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDate.add(chkDifferentROI, gridBagConstraints);

        lblPrematureClosureApplyROI.setText("Premature Closure Apply");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panDate.add(lblPrematureClosureApplyROI, gridBagConstraints);

        lblSlabWiseInterest.setText("SlabWise Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        panDate.add(lblSlabWiseInterest, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDate.add(chkSlabWiseInterest, gridBagConstraints);

        lblSlabWiseCommision.setText("SlabWise Commision");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        panDate.add(lblSlabWiseCommision, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDate.add(chkSlabWiseCommision, gridBagConstraints);

        lblpreMatIntType.setText("Premature Interest Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(lblpreMatIntType, gridBagConstraints);

        cboPreMatIntType.setMinimumSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDate.add(cboPreMatIntType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panDate.add(cmbSbProduct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInterestPayment.add(panDate, gridBagConstraints);

        sptInterestPayment.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 0);
        panInterestPayment.add(sptInterestPayment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panWithDrawIntPay.add(panInterestPayment, gridBagConstraints);

        panExcludeLienFrm.setBorder(javax.swing.BorderFactory.createTitledBorder("Exclude Lien"));
        panExcludeLienFrm.setMaximumSize(new java.awt.Dimension(300, 150));
        panExcludeLienFrm.setMinimumSize(new java.awt.Dimension(300, 150));
        panExcludeLienFrm.setPreferredSize(new java.awt.Dimension(300, 150));
        panExcludeLienFrm.setLayout(new java.awt.GridBagLayout());

        chkExcludeLienFrmStanding.setText("From Standing Instruction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        panExcludeLienFrm.add(chkExcludeLienFrmStanding, gridBagConstraints);

        chkExcludeLienFrmIntrstAppl.setText("From Interest Application");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panExcludeLienFrm.add(chkExcludeLienFrmIntrstAppl, gridBagConstraints);

        chkDepositUnlien.setText("Deposit Unlien");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panExcludeLienFrm.add(chkDepositUnlien, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panWithDrawIntPay.add(panExcludeLienFrm, gridBagConstraints);

        panIntEditable.setMinimumSize(new java.awt.Dimension(300, 80));
        panIntEditable.setPreferredSize(new java.awt.Dimension(300, 80));
        panIntEditable.setLayout(new java.awt.GridBagLayout());

        chkIntEditable.setText("Allow Interest Editable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        panIntEditable.add(chkIntEditable, gridBagConstraints);

        chkCummCertPrint.setText("Cummulative Certificate Print ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        panIntEditable.add(chkCummCertPrint, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        panWithDrawIntPay.add(panIntEditable, gridBagConstraints);

        tabDepositsProduct.addTab("Withdrawal & Int Payment", panWithDrawIntPay);

        panAcHdRenewal.setLayout(new java.awt.GridBagLayout());

        panRenewalAndTax.setLayout(new java.awt.GridBagLayout());

        panTax.setBorder(javax.swing.BorderFactory.createTitledBorder("Tax"));
        panTax.setMinimumSize(new java.awt.Dimension(600, 89));
        panTax.setPreferredSize(new java.awt.Dimension(600, 89));
        panTax.setLayout(new java.awt.GridBagLayout());

        lblTDSGLAcctHd.setText("TDS GL Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTax.add(lblTDSGLAcctHd, gridBagConstraints);

        lblRecalcOfMaturityValue.setText("Recalculatin of Maturity Value on TDS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 28, 4, 4);
        panTax.add(lblRecalcOfMaturityValue, gridBagConstraints);

        rdgRecalcOfMaturityValue.add(rdoRecalcOfMaturityValue_Yes);
        rdoRecalcOfMaturityValue_Yes.setText("Yes");
        rdoRecalcOfMaturityValue_Yes.setMinimumSize(new java.awt.Dimension(50, 21));
        rdoRecalcOfMaturityValue_Yes.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panTax.add(rdoRecalcOfMaturityValue_Yes, gridBagConstraints);

        rdgRecalcOfMaturityValue.add(rdoRecalcOfMaturityValue_No);
        rdoRecalcOfMaturityValue_No.setText("No");
        rdoRecalcOfMaturityValue_No.setMinimumSize(new java.awt.Dimension(45, 21));
        rdoRecalcOfMaturityValue_No.setPreferredSize(new java.awt.Dimension(45, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panTax.add(rdoRecalcOfMaturityValue_No, gridBagConstraints);

        txtTDSGLAcctHd.setText("\n");
        txtTDSGLAcctHd.setAllowAll(true);
        txtTDSGLAcctHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTDSGLAcctHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTDSGLAcctHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTax.add(txtTDSGLAcctHd, gridBagConstraints);

        btnTDSGLAcctHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTDSGLAcctHd.setToolTipText("Save");
        btnTDSGLAcctHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTDSGLAcctHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTDSGLAcctHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTDSGLAcctHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTDSGLAcctHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTax.add(btnTDSGLAcctHd, gridBagConstraints);

        txtTDSGLAcctHdDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtTDSGLAcctHdDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panTax.add(txtTDSGLAcctHdDesc, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRenewalAndTax.add(panTax, gridBagConstraints);

        panAccountHead.setBorder(javax.swing.BorderFactory.createTitledBorder("Account Head"));
        panAccountHead.setMinimumSize(new java.awt.Dimension(556, 410));
        panAccountHead.setPreferredSize(new java.awt.Dimension(556, 410));
        panAccountHead.setLayout(new java.awt.GridBagLayout());

        lblIntProvisioningAcctHd.setText("Interest Provisioning Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblIntProvisioningAcctHd, gridBagConstraints);

        txtIntProvisioningAcctHd.setAllowAll(true);
        txtIntProvisioningAcctHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntProvisioningAcctHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntProvisioningAcctHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtIntProvisioningAcctHd, gridBagConstraints);

        btnIntProvisioningAcctHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntProvisioningAcctHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntProvisioningAcctHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntProvisioningAcctHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntProvisioningAcctHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnIntProvisioningAcctHd, gridBagConstraints);

        lblIntOnMaturedDepositAcctHead.setText("Interest on Matured Deposit A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblIntOnMaturedDepositAcctHead, gridBagConstraints);

        txtIntOnMaturedDepositAcctHead.setAllowAll(true);
        txtIntOnMaturedDepositAcctHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntOnMaturedDepositAcctHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntOnMaturedDepositAcctHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtIntOnMaturedDepositAcctHead, gridBagConstraints);

        btnIntOnMaturedDepositAcctHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntOnMaturedDepositAcctHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntOnMaturedDepositAcctHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntOnMaturedDepositAcctHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntOnMaturedDepositAcctHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnIntOnMaturedDepositAcctHead, gridBagConstraints);

        lblFixedDepositAcctHead.setText("Fixed Deposit A/c Head for Converted Depts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblFixedDepositAcctHead, gridBagConstraints);

        txtFixedDepositAcctHead.setAllowAll(true);
        txtFixedDepositAcctHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFixedDepositAcctHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFixedDepositAcctHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtFixedDepositAcctHead, gridBagConstraints);

        btnFixedDepositAcctHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFixedDepositAcctHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnFixedDepositAcctHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFixedDepositAcctHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFixedDepositAcctHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnFixedDepositAcctHead, gridBagConstraints);

        lblMaturityDepositAcctHead.setText("Maturity Deposit A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblMaturityDepositAcctHead, gridBagConstraints);

        txttMaturityDepositAcctHead.setAllowAll(true);
        txttMaturityDepositAcctHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txttMaturityDepositAcctHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txttMaturityDepositAcctHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txttMaturityDepositAcctHead, gridBagConstraints);

        btnMaturityDepositAcctHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMaturityDepositAcctHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMaturityDepositAcctHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMaturityDepositAcctHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaturityDepositAcctHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnMaturityDepositAcctHead, gridBagConstraints);

        lblIntProvisionOfMaturedDeposit.setText("Int Provision of Matured Deposit A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblIntProvisionOfMaturedDeposit, gridBagConstraints);

        txtIntProvisionOfMaturedDeposit.setAllowAll(true);
        txtIntProvisionOfMaturedDeposit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntProvisionOfMaturedDeposit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntProvisionOfMaturedDepositFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtIntProvisionOfMaturedDeposit, gridBagConstraints);

        btnIntProvisionOfMaturedDeposit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntProvisionOfMaturedDeposit.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntProvisionOfMaturedDeposit.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntProvisionOfMaturedDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntProvisionOfMaturedDepositActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnIntProvisionOfMaturedDeposit, gridBagConstraints);

        lblIntPaybleGLHead.setText("Interest Payable GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblIntPaybleGLHead, gridBagConstraints);

        txtIntPaybleGLHead.setAllowAll(true);
        txtIntPaybleGLHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntPaybleGLHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntPaybleGLHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtIntPaybleGLHead, gridBagConstraints);

        btnIntPaybleGLHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntPaybleGLHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntPaybleGLHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntPaybleGLHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntPaybleGLHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnIntPaybleGLHead, gridBagConstraints);

        lblIntDebitPLHead.setText("Interest Debit P & L Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblIntDebitPLHead, gridBagConstraints);

        txtIntDebitPLHead.setAllowAll(true);
        txtIntDebitPLHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIntDebitPLHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIntDebitPLHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtIntDebitPLHead, gridBagConstraints);

        btnIntDebitPLHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIntDebitPLHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIntDebitPLHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIntDebitPLHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIntDebitPLHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnIntDebitPLHead, gridBagConstraints);

        lblAcctHeadForFloatAcct.setText("Service Charge A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblAcctHeadForFloatAcct, gridBagConstraints);

        txtAcctHeadForFloatAcct.setAllowAll(true);
        txtAcctHeadForFloatAcct.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctHeadForFloatAcct.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctHeadForFloatAcctFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtAcctHeadForFloatAcct, gridBagConstraints);

        btnAcctHeadForFloatAcct.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctHeadForFloatAcct.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcctHeadForFloatAcct.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctHeadForFloatAcct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctHeadForFloatAcctActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnAcctHeadForFloatAcct, gridBagConstraints);

        lblCommisionPaybleGLHead.setText("Commision Payable GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblCommisionPaybleGLHead, gridBagConstraints);

        txtCommisionPaybleGLHead.setAllowAll(true);
        txtCommisionPaybleGLHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommisionPaybleGLHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommisionPaybleGLHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtCommisionPaybleGLHead, gridBagConstraints);

        btnCommisionPaybleGLHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommisionPaybleGLHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCommisionPaybleGLHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommisionPaybleGLHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommisionPaybleGLHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnCommisionPaybleGLHead, gridBagConstraints);

        lblPenalCharges.setText("Penal Charges Delayed Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPenalCharges, gridBagConstraints);

        txtPenalCharges.setAllowAll(true);
        txtPenalCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtPenalCharges, gridBagConstraints);

        btnPenalCharges.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPenalCharges.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPenalCharges.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPenalCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenalChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnPenalCharges, gridBagConstraints);

        lblTransferOutHead.setText("TransferOut Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblTransferOutHead, gridBagConstraints);

        txtTransferOutHead.setAllowAll(true);
        txtTransferOutHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransferOutHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTransferOutHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtTransferOutHead, gridBagConstraints);

        btnTransferOutHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransferOutHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTransferOutHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransferOutHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransferOutHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnTransferOutHead, gridBagConstraints);

        lblPostageAcHd.setText("Postage Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblPostageAcHd, gridBagConstraints);

        txtPostageAcHd.setAllowAll(true);
        txtPostageAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPostageAcHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostageAcHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtPostageAcHd, gridBagConstraints);

        btnPostageAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostageAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPostageAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPostageAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostageAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnPostageAcHd, gridBagConstraints);

        txtIntProvisioningAcctHdDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntProvisioningAcctHdDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        panAccountHead.add(txtIntProvisioningAcctHdDesc, new java.awt.GridBagConstraints());

        txtIntPaybleGLHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntPaybleGLHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panAccountHead.add(txtIntPaybleGLHeadDesc, gridBagConstraints);

        txtCommisionPaybleGLHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtCommisionPaybleGLHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        panAccountHead.add(txtCommisionPaybleGLHeadDesc, gridBagConstraints);

        txtIntDebitPLHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntDebitPLHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        panAccountHead.add(txtIntDebitPLHeadDesc, gridBagConstraints);

        txttMaturityDepositAcctHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txttMaturityDepositAcctHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        panAccountHead.add(txttMaturityDepositAcctHeadDesc, gridBagConstraints);

        txtIntOnMaturedDepositAcctHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntOnMaturedDepositAcctHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        panAccountHead.add(txtIntOnMaturedDepositAcctHeadDesc, gridBagConstraints);

        txtIntProvisionOfMaturedDepositDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtIntProvisionOfMaturedDepositDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        panAccountHead.add(txtIntProvisionOfMaturedDepositDesc, gridBagConstraints);

        txtAcctHeadForFloatAcctDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtAcctHeadForFloatAcctDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        panAccountHead.add(txtAcctHeadForFloatAcctDesc, gridBagConstraints);

        txtFixedDepositAcctHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtFixedDepositAcctHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        panAccountHead.add(txtFixedDepositAcctHeadDesc, gridBagConstraints);

        txtPenalChargesDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtPenalChargesDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        panAccountHead.add(txtPenalChargesDesc, gridBagConstraints);

        txtTransferOutHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtTransferOutHeadDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        panAccountHead.add(txtTransferOutHeadDesc, gridBagConstraints);

        lblInterestRecoveryAcHd.setText("Interest Recovery Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountHead.add(lblInterestRecoveryAcHd, gridBagConstraints);

        txtInterestRecoveryAcHd.setAllowAll(true);
        txtInterestRecoveryAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterestRecoveryAcHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInterestRecoveryAcHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAccountHead.add(txtInterestRecoveryAcHd, gridBagConstraints);

        btnInterestRecoveryAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInterestRecoveryAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInterestRecoveryAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInterestRecoveryAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestRecoveryAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAccountHead.add(btnInterestRecoveryAcHd, gridBagConstraints);

        txtInterestRecoveryAcHdDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtInterestRecoveryAcHdDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        panAccountHead.add(txtInterestRecoveryAcHdDesc, gridBagConstraints);

        cLabel2.setText("Benevolent Interest Reserve Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        panAccountHead.add(cLabel2, gridBagConstraints);

        txtBenIntReserveHead.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        panAccountHead.add(txtBenIntReserveHead, gridBagConstraints);

        btnBenIntReserveHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBenIntReserveHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBenIntReserveHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBenIntReserveHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        panAccountHead.add(btnBenIntReserveHead, gridBagConstraints);

        txtBenIntReserveHeadDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        panAccountHead.add(txtBenIntReserveHeadDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRenewalAndTax.add(panAccountHead, gridBagConstraints);

        panAcHdRenewal.add(panRenewalAndTax, new java.awt.GridBagConstraints());

        tabDepositsProduct.addTab("A/C Head ", panAcHdRenewal);

        panRenewalParameter.setMinimumSize(new java.awt.Dimension(845, 600));
        panRenewalParameter.setPreferredSize(new java.awt.Dimension(845, 600));

        panRenewalExtension.setLayout(new java.awt.GridBagLayout());

        panRateAsOn.setBorder(javax.swing.BorderFactory.createTitledBorder("Renewal Parameter"));
        panRateAsOn.setMaximumSize(new java.awt.Dimension(850, 500));
        panRateAsOn.setMinimumSize(new java.awt.Dimension(850, 500));
        panRateAsOn.setPreferredSize(new java.awt.Dimension(850, 500));
        panRateAsOn.setLayout(new java.awt.GridBagLayout());

        lblIntPeriodForBackDatedRenewal.setText("From the date of maturity, deposit to be recovered with in");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 229, 0, 0);
        panRateAsOn.add(lblIntPeriodForBackDatedRenewal, gridBagConstraints);

        lblIntCriteria.setText("Interest Criteria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 24, 5, 0);
        panRateAsOn.add(lblIntCriteria, gridBagConstraints);

        cboIntCriteria.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 5, 0);
        panRateAsOn.add(cboIntCriteria, gridBagConstraints);

        lblAutoRenewalAllowed.setText("Auto Renewal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 429, 0, 0);
        panRateAsOn.add(lblAutoRenewalAllowed, gridBagConstraints);

        panAutoRenewalAllowed.setMinimumSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed.setPreferredSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed.setLayout(new java.awt.GridBagLayout());

        rdoAutoRenewalAllowed_Yes.setText("Yes");
        rdoAutoRenewalAllowed_Yes.setMinimumSize(new java.awt.Dimension(55, 21));
        rdoAutoRenewalAllowed_Yes.setPreferredSize(new java.awt.Dimension(55, 21));
        rdoAutoRenewalAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAutoRenewalAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAutoRenewalAllowed.add(rdoAutoRenewalAllowed_Yes, gridBagConstraints);

        rdoAutoRenewalAllowed_No.setText("No");
        rdoAutoRenewalAllowed_No.setMinimumSize(new java.awt.Dimension(39, 21));
        rdoAutoRenewalAllowed_No.setPreferredSize(new java.awt.Dimension(39, 21));
        rdoAutoRenewalAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAutoRenewalAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panAutoRenewalAllowed.add(rdoAutoRenewalAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panAutoRenewalAllowed, gridBagConstraints);

        lblRenewalOfDepositAllowed.setText("Renewal of Deposit Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 398, 3, 2);
        panRateAsOn.add(lblRenewalOfDepositAllowed, gridBagConstraints);

        panRenewalOfDepositAllowed.setMinimumSize(new java.awt.Dimension(100, 18));
        panRenewalOfDepositAllowed.setPreferredSize(new java.awt.Dimension(100, 18));
        panRenewalOfDepositAllowed.setLayout(new java.awt.GridBagLayout());

        rdoRenewalOfDepositAllowed_Yes.setText("Yes");
        rdoRenewalOfDepositAllowed_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoRenewalOfDepositAllowed_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoRenewalOfDepositAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalOfDepositAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRenewalOfDepositAllowed.add(rdoRenewalOfDepositAllowed_Yes, gridBagConstraints);

        rdoRenewalOfDepositAllowed_No.setText("No");
        rdoRenewalOfDepositAllowed_No.setMinimumSize(new java.awt.Dimension(39, 21));
        rdoRenewalOfDepositAllowed_No.setPreferredSize(new java.awt.Dimension(39, 21));
        rdoRenewalOfDepositAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRenewalOfDepositAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panRenewalOfDepositAllowed.add(rdoRenewalOfDepositAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        panRateAsOn.add(panRenewalOfDepositAllowed, gridBagConstraints);

        lblMinNoOfDays.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinNoOfDays.setText("Back Dated Deposits are to be Renewed Within");
        lblMinNoOfDays.setMaximumSize(new java.awt.Dimension(182, 15));
        lblMinNoOfDays.setMinimumSize(new java.awt.Dimension(290, 15));
        lblMinNoOfDays.setPreferredSize(new java.awt.Dimension(290, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 268, 0, 0);
        panRateAsOn.add(lblMinNoOfDays, gridBagConstraints);

        panMinNoOfDays.setLayout(new java.awt.GridBagLayout());

        txtMinNoOfDays.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinNoOfDays.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMinNoOfDays.add(txtMinNoOfDays, gridBagConstraints);

        cboMinNoOfDays.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMinNoOfDays.add(cboMinNoOfDays, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 42;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        panRateAsOn.add(panMinNoOfDays, gridBagConstraints);

        lblMaxNopfTimes.setText("Max No. of Times Auto Renewal Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 323, 0, 0);
        panRateAsOn.add(lblMaxNopfTimes, gridBagConstraints);

        txtMaxNopfTimes.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxNopfTimes.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 0, 0);
        panRateAsOn.add(txtMaxNopfTimes, gridBagConstraints);

        panIntPeriodForBackDatedRenewal.setLayout(new java.awt.GridBagLayout());

        cboIntPeriodForBackDatedRenewal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panIntPeriodForBackDatedRenewal.add(cboIntPeriodForBackDatedRenewal, gridBagConstraints);

        txtIntPeriodForBackDatedRenewal.setMinimumSize(new java.awt.Dimension(50, 21));
        txtIntPeriodForBackDatedRenewal.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panIntPeriodForBackDatedRenewal.add(txtIntPeriodForBackDatedRenewal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 31;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panIntPeriodForBackDatedRenewal, gridBagConstraints);

        lblIntType.setText("If Renewed beyond Max Period, Interest Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 296, 0, 0);
        panRateAsOn.add(lblIntType, gridBagConstraints);

        cboIntType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        panRateAsOn.add(cboIntType, gridBagConstraints);

        lblMaturityDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaturityDate.setText("If Renewed beyond Max Period,Maturity Date");
        lblMaturityDate.setMaximumSize(new java.awt.Dimension(183, 15));
        lblMaturityDate.setMinimumSize(new java.awt.Dimension(275, 15));
        lblMaturityDate.setPreferredSize(new java.awt.Dimension(275, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 29;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 283, 0, 0);
        panRateAsOn.add(lblMaturityDate, gridBagConstraints);

        panMaxPeriodMDt.setLayout(new java.awt.GridBagLayout());

        cboMaxPeriodMDt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMaxPeriodMDt.add(cboMaxPeriodMDt, gridBagConstraints);

        txtMaxPeriodMDt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxPeriodMDt.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMaxPeriodMDt.add(txtMaxPeriodMDt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 42;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        panRateAsOn.add(panMaxPeriodMDt, gridBagConstraints);

        panAutoRenewalAllowed1.setMinimumSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed1.setPreferredSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed1.setLayout(new java.awt.GridBagLayout());

        rdoSameNoRenewalAllowed_Yes.setText("Yes");
        rdoSameNoRenewalAllowed_Yes.setMinimumSize(new java.awt.Dimension(55, 21));
        rdoSameNoRenewalAllowed_Yes.setPreferredSize(new java.awt.Dimension(55, 21));
        rdoSameNoRenewalAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSameNoRenewalAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAutoRenewalAllowed1.add(rdoSameNoRenewalAllowed_Yes, gridBagConstraints);

        rdoSameNoRenewalAllowed_No.setText("No");
        rdoSameNoRenewalAllowed_No.setMinimumSize(new java.awt.Dimension(39, 21));
        rdoSameNoRenewalAllowed_No.setPreferredSize(new java.awt.Dimension(39, 21));
        rdoSameNoRenewalAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSameNoRenewalAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panAutoRenewalAllowed1.add(rdoSameNoRenewalAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        panRateAsOn.add(panAutoRenewalAllowed1, gridBagConstraints);

        lblAutoRenewalAllowed1.setText("Same No Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 9, 0, 0);
        panRateAsOn.add(lblAutoRenewalAllowed1, gridBagConstraints);

        lblMaxNopfTimes1.setText("No. of Times Renewal with same number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 322, 0, 0);
        panRateAsOn.add(lblMaxNopfTimes1, gridBagConstraints);

        txtMaxNopfTimes1.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxNopfTimes1.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 0, 0);
        panRateAsOn.add(txtMaxNopfTimes1, gridBagConstraints);

        panMinimumRenewalDeposit.setLayout(new java.awt.GridBagLayout());

        cboMinimumRenewalDeposit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMinimumRenewalDeposit.add(cboMinimumRenewalDeposit, gridBagConstraints);

        txtMinimumRenewalDeposit.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinimumRenewalDeposit.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMinimumRenewalDeposit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMinimumRenewalDepositFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMinimumRenewalDeposit.add(txtMinimumRenewalDeposit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 31;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panMinimumRenewalDeposit, gridBagConstraints);

        lblMinimumRenewalDeposit.setText("Minimum period of renewal from the date of renewal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 262, 0, 0);
        panRateAsOn.add(lblMinimumRenewalDeposit, gridBagConstraints);

        panRenewedclosedbefore.setLayout(new java.awt.GridBagLayout());

        cboRenewedclosedbefore.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panRenewedclosedbefore.add(cboRenewedclosedbefore, gridBagConstraints);

        txtRenewedclosedbefore.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRenewedclosedbefore.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panRenewedclosedbefore.add(txtRenewedclosedbefore, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 31;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panRenewedclosedbefore, gridBagConstraints);

        lblRenewedclosedbefore.setText("Renewed deposit should not be closed before");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 296, 0, 0);
        panRateAsOn.add(lblRenewedclosedbefore, gridBagConstraints);

        panInterestalreadyPaid.setMinimumSize(new java.awt.Dimension(100, 18));
        panInterestalreadyPaid.setPreferredSize(new java.awt.Dimension(100, 18));
        panInterestalreadyPaid.setLayout(new java.awt.GridBagLayout());

        rdgSameNoDepositAllowed.add(rdoInterestalreadyPaid_Yes);
        rdoInterestalreadyPaid_Yes.setText("Yes");
        rdoInterestalreadyPaid_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoInterestalreadyPaid_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInterestalreadyPaid.add(rdoInterestalreadyPaid_Yes, gridBagConstraints);

        rdgSameNoDepositAllowed.add(rdoInterestalreadyPaid_No);
        rdoInterestalreadyPaid_No.setText("No");
        rdoInterestalreadyPaid_No.setMinimumSize(new java.awt.Dimension(39, 18));
        rdoInterestalreadyPaid_No.setPreferredSize(new java.awt.Dimension(39, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panInterestalreadyPaid.add(rdoInterestalreadyPaid_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panInterestalreadyPaid, gridBagConstraints);

        lblclosedwithinperiod.setText("If closed, within above period, whether interest to be paid for the period run");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 131, 0, 0);
        panRateAsOn.add(lblclosedwithinperiod, gridBagConstraints);

        lblOneRateAvail.setText("If only one of the above two rates is avaialable, then");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 262, 0, 0);
        panRateAsOn.add(lblOneRateAvail, gridBagConstraints);

        panOneRateAvail.setMinimumSize(new java.awt.Dimension(255, 18));
        panOneRateAvail.setPreferredSize(new java.awt.Dimension(255, 18));
        panOneRateAvail.setLayout(new java.awt.GridBagLayout());

        rdgSameNoDepositAllowed.add(rdoSBRateOneRate_Yes);
        rdoSBRateOneRate_Yes.setText("Apply available Rate");
        rdoSBRateOneRate_Yes.setToolTipText("Apply available Rate");
        rdoSBRateOneRate_Yes.setMaximumSize(new java.awt.Dimension(148, 27));
        rdoSBRateOneRate_Yes.setMinimumSize(new java.awt.Dimension(148, 18));
        rdoSBRateOneRate_Yes.setPreferredSize(new java.awt.Dimension(148, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panOneRateAvail.add(rdoSBRateOneRate_Yes, gridBagConstraints);

        rdgSameNoDepositAllowed.add(rdoSBRateOneRate_No);
        rdoSBRateOneRate_No.setText("Apply SBRate");
        rdoSBRateOneRate_No.setToolTipText("Apply SBRate");
        rdoSBRateOneRate_No.setMaximumSize(new java.awt.Dimension(121, 27));
        rdoSBRateOneRate_No.setMinimumSize(new java.awt.Dimension(121, 21));
        rdoSBRateOneRate_No.setPreferredSize(new java.awt.Dimension(121, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panOneRateAvail.add(rdoSBRateOneRate_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 56;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panOneRateAvail, gridBagConstraints);

        panclosedwithinperiod.setMinimumSize(new java.awt.Dimension(100, 18));
        panclosedwithinperiod.setPreferredSize(new java.awt.Dimension(100, 18));
        panclosedwithinperiod.setLayout(new java.awt.GridBagLayout());

        rdgSameNoDepositAllowed.add(rdoClosedwithinperiod_Yes);
        rdoClosedwithinperiod_Yes.setText("Yes");
        rdoClosedwithinperiod_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoClosedwithinperiod_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panclosedwithinperiod.add(rdoClosedwithinperiod_Yes, gridBagConstraints);

        rdgSameNoDepositAllowed.add(rdoClosedwithinperiod_No);
        rdoClosedwithinperiod_No.setText("No");
        rdoClosedwithinperiod_No.setMinimumSize(new java.awt.Dimension(39, 18));
        rdoClosedwithinperiod_No.setPreferredSize(new java.awt.Dimension(39, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panclosedwithinperiod.add(rdoClosedwithinperiod_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panclosedwithinperiod, gridBagConstraints);

        lblInterestalreadyPaid.setText("If closed, within above period, whether interest already paid for the overdue period to be recovered");
        lblInterestalreadyPaid.setToolTipText("If closed, within above period, whether interest already paid for the overdue period to be recovered");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipadx = -6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panRateAsOn.add(lblInterestalreadyPaid, gridBagConstraints);

        panIntRateforOverduePeriod.setMinimumSize(new java.awt.Dimension(175, 18));
        panIntRateforOverduePeriod.setPreferredSize(new java.awt.Dimension(175, 18));
        panIntRateforOverduePeriod.setLayout(new java.awt.GridBagLayout());

        rdgSameNoDepositAllowed.add(rdoInterestrateappliedoverdue_No);
        rdoInterestrateappliedoverdue_No.setText("Deposit Rate");
        rdoInterestrateappliedoverdue_No.setMaximumSize(new java.awt.Dimension(106, 27));
        rdoInterestrateappliedoverdue_No.setMinimumSize(new java.awt.Dimension(106, 27));
        rdoInterestrateappliedoverdue_No.setPreferredSize(new java.awt.Dimension(106, 27));
        rdoInterestrateappliedoverdue_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestrateappliedoverdue_NoActionPerformed(evt);
            }
        });
        panIntRateforOverduePeriod.add(rdoInterestrateappliedoverdue_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.ipadx = -54;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        panRateAsOn.add(panIntRateforOverduePeriod, gridBagConstraints);

        lblBothRateAvail.setText("Either of the above two rates which ever is");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 316, 0, 0);
        panRateAsOn.add(lblBothRateAvail, gridBagConstraints);

        lblInterestrateappliedoverdue.setText("Interest rate to be applied for the overdue period is to be based on");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 52;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 16, 0, 0);
        panRateAsOn.add(lblInterestrateappliedoverdue, gridBagConstraints);

        lblRenewalOfDepositAllowed1.setText("If the deposit is renewed after the period mentioned above, then - ");
        lblRenewalOfDepositAllowed1.setFont(new java.awt.Font("MS Sans Serif", 1, 15)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 172, 0, 0);
        panRateAsOn.add(lblRenewalOfDepositAllowed1, gridBagConstraints);

        panBothRateNotAvail.setMinimumSize(new java.awt.Dimension(220, 18));
        panBothRateNotAvail.setPreferredSize(new java.awt.Dimension(220, 18));
        panBothRateNotAvail.setLayout(new java.awt.GridBagLayout());

        rdgSameNoDepositAllowed.add(rdoBothRateNotAvail_Yes);
        rdoBothRateNotAvail_Yes.setText("Apply SBRate");
        rdoBothRateNotAvail_Yes.setToolTipText("Apply SBRate");
        rdoBothRateNotAvail_Yes.setMaximumSize(new java.awt.Dimension(120, 27));
        rdoBothRateNotAvail_Yes.setMinimumSize(new java.awt.Dimension(113, 18));
        rdoBothRateNotAvail_Yes.setPreferredSize(new java.awt.Dimension(113, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBothRateNotAvail.add(rdoBothRateNotAvail_Yes, gridBagConstraints);

        rdgSameNoDepositAllowed.add(rdoBothRateNotAvail_No);
        rdoBothRateNotAvail_No.setText("Pay no interest");
        rdoBothRateNotAvail_No.setToolTipText("Pay no interest");
        rdoBothRateNotAvail_No.setMaximumSize(new java.awt.Dimension(135, 27));
        rdoBothRateNotAvail_No.setMinimumSize(new java.awt.Dimension(135, 21));
        rdoBothRateNotAvail_No.setPreferredSize(new java.awt.Dimension(135, 21));
        rdoBothRateNotAvail_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBothRateNotAvail_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panBothRateNotAvail.add(rdoBothRateNotAvail_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 55;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(panBothRateNotAvail, gridBagConstraints);

        lblBothRateNotAvail.setText("If both the above rates are not available");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 334, 0, 0);
        panRateAsOn.add(lblBothRateNotAvail, gridBagConstraints);

        lblBasedOnDepRate.setText("If based on deposit rate, apply - ");
        lblBasedOnDepRate.setFont(new java.awt.Font("MS Sans Serif", 1, 15)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 294, 0, 0);
        panRateAsOn.add(lblBasedOnDepRate, gridBagConstraints);

        lblRateAsOnDateOfRenewal.setText("Rate as on the date of renewal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 382, 0, 0);
        panRateAsOn.add(lblRateAsOnDateOfRenewal, gridBagConstraints);

        chkRateAsOnDateOfRenewal.setMinimumSize(new java.awt.Dimension(18, 18));
        chkRateAsOnDateOfRenewal.setPreferredSize(new java.awt.Dimension(18, 18));
        chkRateAsOnDateOfRenewal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRateAsOnDateOfRenewalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(chkRateAsOnDateOfRenewal, gridBagConstraints);

        chkRateAsOnDateOfMaturity.setMinimumSize(new java.awt.Dimension(18, 18));
        chkRateAsOnDateOfMaturity.setPreferredSize(new java.awt.Dimension(18, 18));
        chkRateAsOnDateOfMaturity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRateAsOnDateOfMaturityActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(chkRateAsOnDateOfMaturity, gridBagConstraints);

        lblRateAsOnDateOfMaturity.setText("Rate as on the date of maturity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 379, 0, 0);
        panRateAsOn.add(lblRateAsOnDateOfMaturity, gridBagConstraints);

        sptOperatesLike1.setMinimumSize(new java.awt.Dimension(400, 2));
        sptOperatesLike1.setPreferredSize(new java.awt.Dimension(400, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 62;
        gridBagConstraints.ipadx = 300;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panRateAsOn.add(sptOperatesLike1, gridBagConstraints);

        sptOperatesLike2.setMinimumSize(new java.awt.Dimension(400, 2));
        sptOperatesLike2.setPreferredSize(new java.awt.Dimension(400, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 62;
        gridBagConstraints.ipadx = 407;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panRateAsOn.add(sptOperatesLike2, gridBagConstraints);

        cboEitherofTwoRatesChoose.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEitherofTwoRatesChoose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEitherofTwoRatesChooseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(cboEitherofTwoRatesChoose, gridBagConstraints);

        sbRateCmb.setMinimumSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 43;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.ipady = -2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        panRateAsOn.add(sbRateCmb, gridBagConstraints);

        rdgSameNoDepositAllowed.add(rdoInterestrateappliedoverdue_Yes);
        rdoInterestrateappliedoverdue_Yes.setText("SB Rate");
        rdoInterestrateappliedoverdue_Yes.setMaximumSize(new java.awt.Dimension(80, 18));
        rdoInterestrateappliedoverdue_Yes.setMinimumSize(new java.awt.Dimension(80, 18));
        rdoInterestrateappliedoverdue_Yes.setPreferredSize(new java.awt.Dimension(80, 18));
        rdoInterestrateappliedoverdue_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInterestrateappliedoverdue_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panRateAsOn.add(rdoInterestrateappliedoverdue_Yes, gridBagConstraints);

        lblIntRateApp.setText("   Interest rate to be applied for the overdue period for closure");
        lblIntRateApp.setMaximumSize(new java.awt.Dimension(360, 18));
        lblIntRateApp.setMinimumSize(new java.awt.Dimension(360, 18));
        lblIntRateApp.setPreferredSize(new java.awt.Dimension(360, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(lblIntRateApp, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(chkIntRateApp, gridBagConstraints);

        lblSbRateInterestDeath.setText("Interest rate to be applied for the overdue period is to SBRate Death Marked");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(lblSbRateInterestDeath, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRateAsOn.add(chkIntRateDeathMarked, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panRenewalExtension.add(panRateAsOn, gridBagConstraints);

        panTax1.setBorder(javax.swing.BorderFactory.createTitledBorder("Extension Parameter"));
        panTax1.setMaximumSize(new java.awt.Dimension(825, 65));
        panTax1.setMinimumSize(new java.awt.Dimension(825, 65));
        panTax1.setPreferredSize(new java.awt.Dimension(825, 65));
        panTax1.setLayout(new java.awt.GridBagLayout());

        panAutoRenewalAllowed3.setMinimumSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed3.setPreferredSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed3.setLayout(new java.awt.GridBagLayout());

        rdgExtensionDepositBeyond.add(rdoExtensionBeyondOriginalDate_Yes);
        rdoExtensionBeyondOriginalDate_Yes.setText("Yes");
        rdoExtensionBeyondOriginalDate_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoExtensionBeyondOriginalDate_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAutoRenewalAllowed3.add(rdoExtensionBeyondOriginalDate_Yes, gridBagConstraints);

        rdgExtensionDepositBeyond.add(rdoExtensionBeyondOriginalDate_No);
        rdoExtensionBeyondOriginalDate_No.setText("No");
        rdoExtensionBeyondOriginalDate_No.setMinimumSize(new java.awt.Dimension(39, 18));
        rdoExtensionBeyondOriginalDate_No.setPreferredSize(new java.awt.Dimension(39, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panAutoRenewalAllowed3.add(rdoExtensionBeyondOriginalDate_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTax1.add(panAutoRenewalAllowed3, gridBagConstraints);

        lblExtensionBeyondOriginalDate.setText("Extension of Deposit Beyond the original deposit date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTax1.add(lblExtensionBeyondOriginalDate, gridBagConstraints);

        lblExtensionPenal.setText("Penal Interest on Withdrawal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panTax1.add(lblExtensionPenal, gridBagConstraints);

        panAutoRenewalAllowed4.setMinimumSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed4.setPreferredSize(new java.awt.Dimension(100, 18));
        panAutoRenewalAllowed4.setLayout(new java.awt.GridBagLayout());

        rdgPenalIntOnWithdrawal.add(rdoExtensionPenal_Yes);
        rdoExtensionPenal_Yes.setText("Yes");
        rdoExtensionPenal_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoExtensionPenal_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAutoRenewalAllowed4.add(rdoExtensionPenal_Yes, gridBagConstraints);

        rdgPenalIntOnWithdrawal.add(rdoExtensionPenal_No);
        rdoExtensionPenal_No.setText("No");
        rdoExtensionPenal_No.setMinimumSize(new java.awt.Dimension(39, 18));
        rdoExtensionPenal_No.setPreferredSize(new java.awt.Dimension(39, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panAutoRenewalAllowed4.add(rdoExtensionPenal_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTax1.add(panAutoRenewalAllowed4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panRenewalExtension.add(panTax1, gridBagConstraints);

        panRenewalParameter.add(panRenewalExtension);

        tabDepositsProduct.addTab("Renewal Parameters", panRenewalParameter);

        panRD.setMinimumSize(new java.awt.Dimension(790, 480));
        panRD.setPreferredSize(new java.awt.Dimension(790, 480));
        panRD.setLayout(new java.awt.GridBagLayout());

        panRecurringDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Recurring Details"));
        panRecurringDetails.setMinimumSize(new java.awt.Dimension(900, 545));
        panRecurringDetails.setPreferredSize(new java.awt.Dimension(900, 545));
        panRecurringDetails.setLayout(new java.awt.GridBagLayout());

        lblLastInstallmentAllowed.setText("Late Installment Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblLastInstallmentAllowed, gridBagConstraints);

        lblMaturityDateAfterLastInstalPaid.setText("Maturity Date after Last Installment Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblMaturityDateAfterLastInstalPaid, gridBagConstraints);

        panMaturityDateAfterLastInstalPaid.setLayout(new java.awt.GridBagLayout());

        txtMaturityDateAfterLastInstalPaid.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaturityDateAfterLastInstalPaid.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMaturityDateAfterLastInstalPaid.add(txtMaturityDateAfterLastInstalPaid, gridBagConstraints);

        cboMaturityDateAfterLastInstalPaid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMaturityDateAfterLastInstalPaid.add(cboMaturityDateAfterLastInstalPaid, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(panMaturityDateAfterLastInstalPaid, gridBagConstraints);

        lblCanReceiveExcessInstal.setText("Can Receive Excess Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblCanReceiveExcessInstal, gridBagConstraints);

        panCanReceiveExcessInstal.setLayout(new java.awt.GridBagLayout());

        rdgCanReceiveExcessInstal.add(rdoCanReceiveExcessInstal_yes);
        rdoCanReceiveExcessInstal_yes.setText("Yes");
        rdoCanReceiveExcessInstal_yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoCanReceiveExcessInstal_yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoCanReceiveExcessInstal_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCanReceiveExcessInstal_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCanReceiveExcessInstal.add(rdoCanReceiveExcessInstal_yes, gridBagConstraints);

        rdgCanReceiveExcessInstal.add(rdoCanReceiveExcessInstal_No);
        rdoCanReceiveExcessInstal_No.setText("No");
        rdoCanReceiveExcessInstal_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoCanReceiveExcessInstal_No.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoCanReceiveExcessInstal_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCanReceiveExcessInstal_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panCanReceiveExcessInstal.add(rdoCanReceiveExcessInstal_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panRecurringDetails.add(panCanReceiveExcessInstal, gridBagConstraints);

        lblInstallmentInRecurringDepositAcct.setText("Installment in Recurring Deposit Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblInstallmentInRecurringDepositAcct, gridBagConstraints);

        panInstallmentInRecurringDepositAcct.setLayout(new java.awt.GridBagLayout());

        rdgInstallmentInRecurringDepositAcct.add(rdoInstallmentInRecurringDepositAcct_Yes);
        rdoInstallmentInRecurringDepositAcct_Yes.setText("Fixed");
        rdoInstallmentInRecurringDepositAcct_Yes.setMinimumSize(new java.awt.Dimension(57, 18));
        rdoInstallmentInRecurringDepositAcct_Yes.setPreferredSize(new java.awt.Dimension(57, 18));
        rdoInstallmentInRecurringDepositAcct_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInstallmentInRecurringDepositAcct_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInstallmentInRecurringDepositAcct.add(rdoInstallmentInRecurringDepositAcct_Yes, gridBagConstraints);

        rdgInstallmentInRecurringDepositAcct.add(rdoInstallmentInRecurringDepositAcct_No);
        rdoInstallmentInRecurringDepositAcct_No.setText("Variable");
        rdoInstallmentInRecurringDepositAcct_No.setMinimumSize(new java.awt.Dimension(73, 18));
        rdoInstallmentInRecurringDepositAcct_No.setPreferredSize(new java.awt.Dimension(73, 18));
        rdoInstallmentInRecurringDepositAcct_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInstallmentInRecurringDepositAcct_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panInstallmentInRecurringDepositAcct.add(rdoInstallmentInRecurringDepositAcct_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panRecurringDetails.add(panInstallmentInRecurringDepositAcct, gridBagConstraints);

        lblDepositsFrequency.setText("Deposit Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 106, 2, 4);
        panRecurringDetails.add(lblDepositsFrequency, gridBagConstraints);

        cboDepositsFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepositsFrequency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboDepositsFrequencyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 257, 2, 0);
        panRecurringDetails.add(cboDepositsFrequency, gridBagConstraints);

        cboChangeValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panRecurringDetails.add(cboChangeValue, gridBagConstraints);

        lblIntPayableOnExcessInstal.setText("Interest Payable on Excess Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblIntPayableOnExcessInstal, gridBagConstraints);

        panIntPayableOnExcessInstal.setLayout(new java.awt.GridBagLayout());

        rdgIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_Yes);
        rdoIntPayableOnExcessInstal_Yes.setText("Yes");
        rdoIntPayableOnExcessInstal_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoIntPayableOnExcessInstal_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_Yes, gridBagConstraints);

        rdgIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_No);
        rdoIntPayableOnExcessInstal_No.setText("No");
        rdoIntPayableOnExcessInstal_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoIntPayableOnExcessInstal_No.setPreferredSize(new java.awt.Dimension(41, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panRecurringDetails.add(panIntPayableOnExcessInstal, gridBagConstraints);

        lblRecurringDepositToFixedDeposit.setText("Convert Recurring Deposit to Fixed Deposit after Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblRecurringDepositToFixedDeposit, gridBagConstraints);

        panRecurringDepositToFixedDeposit.setLayout(new java.awt.GridBagLayout());

        rdgRecurringDepositToFixedDeposit.add(rdoRecurringDepositToFixedDeposit_Yes);
        rdoRecurringDepositToFixedDeposit_Yes.setText("Yes");
        rdoRecurringDepositToFixedDeposit_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoRecurringDepositToFixedDeposit_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRecurringDepositToFixedDeposit.add(rdoRecurringDepositToFixedDeposit_Yes, gridBagConstraints);

        rdgRecurringDepositToFixedDeposit.add(rdoRecurringDepositToFixedDeposit_No);
        rdoRecurringDepositToFixedDeposit_No.setText("No");
        rdoRecurringDepositToFixedDeposit_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoRecurringDepositToFixedDeposit_No.setPreferredSize(new java.awt.Dimension(41, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panRecurringDepositToFixedDeposit.add(rdoRecurringDepositToFixedDeposit_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panRecurringDetails.add(panRecurringDepositToFixedDeposit, gridBagConstraints);

        lblCutOffDayForPaymentOfInstal.setText("Cut Off Day for Payment of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblCutOffDayForPaymentOfInstal, gridBagConstraints);

        panLastInstallmentAllowedRD.setLayout(new java.awt.GridBagLayout());

        rdgLastInstallmentAllowed.add(rdoLastInstallmentAllowed_Yes);
        rdoLastInstallmentAllowed_Yes.setText("Yes");
        rdoLastInstallmentAllowed_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoLastInstallmentAllowed_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoLastInstallmentAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLastInstallmentAllowed_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panLastInstallmentAllowedRD.add(rdoLastInstallmentAllowed_Yes, gridBagConstraints);

        rdgLastInstallmentAllowed.add(rdoLastInstallmentAllowed_No);
        rdoLastInstallmentAllowed_No.setText("No");
        rdoLastInstallmentAllowed_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoLastInstallmentAllowed_No.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoLastInstallmentAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLastInstallmentAllowed_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panLastInstallmentAllowedRD.add(rdoLastInstallmentAllowed_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRecurringDetails.add(panLastInstallmentAllowedRD, gridBagConstraints);

        cboCutOffDayForPaymentOfInstal.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCutOffDayForPaymentOfInstal.setPopupWidth(200);
        cboCutOffDayForPaymentOfInstal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCutOffDayForPaymentOfInstalActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panRecurringDetails.add(cboCutOffDayForPaymentOfInstal, gridBagConstraints);

        txtCutOffDayForPaymentOfInstal.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCutOffDayForPaymentOfInstal.setPreferredSize(new java.awt.Dimension(50, 21));
        txtCutOffDayForPaymentOfInstal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCutOffDayForPaymentOfInstalFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 130, 4, 4);
        panRecurringDetails.add(txtCutOffDayForPaymentOfInstal, gridBagConstraints);

        cboInstallmentToBeCharged.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 160, 2, 0);
        panRecurringDetails.add(cboInstallmentToBeCharged, gridBagConstraints);

        lblInstallmentToBeCharged.setText("Installment to be Changed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panRecurringDetails.add(lblInstallmentToBeCharged, gridBagConstraints);

        lblChangeValue.setText("ChangeValue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panRecurringDetails.add(lblChangeValue, gridBagConstraints);

        lblPenaltyOnLateInstallmentsChargeble.setText("Penalty on Late Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblPenaltyOnLateInstallmentsChargeble, gridBagConstraints);

        panPenaltyOnLateInstallmentsChargeble.setLayout(new java.awt.GridBagLayout());

        rdgPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_Yes);
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setText("Yes");
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setMaximumSize(new java.awt.Dimension(49, 18));
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        rdoPenaltyOnLateInstallmentsChargeble_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenaltyOnLateInstallmentsChargeble_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_Yes, gridBagConstraints);

        rdgPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_No);
        rdoPenaltyOnLateInstallmentsChargeble_No.setText("No");
        rdoPenaltyOnLateInstallmentsChargeble_No.setMaximumSize(new java.awt.Dimension(41, 18));
        rdoPenaltyOnLateInstallmentsChargeble_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoPenaltyOnLateInstallmentsChargeble_No.setPreferredSize(new java.awt.Dimension(41, 18));
        rdoPenaltyOnLateInstallmentsChargeble_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenaltyOnLateInstallmentsChargeble_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panRecurringDetails.add(panPenaltyOnLateInstallmentsChargeble, gridBagConstraints);

        lblMinimumPeriod.setText("Minimum Period Account has to run for premature closure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblMinimumPeriod, gridBagConstraints);

        lblAgentCommision.setText("Agent Commision to be recovered if closed before");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblAgentCommision, gridBagConstraints);

        panMinimumPeriod.setLayout(new java.awt.GridBagLayout());

        txtMinimumPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinimumPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMinimumPeriod.add(txtMinimumPeriod, gridBagConstraints);

        cboMinimumPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMinimumPeriod.add(cboMinimumPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panRecurringDetails.add(panMinimumPeriod, gridBagConstraints);

        panAgentCommision.setLayout(new java.awt.GridBagLayout());

        txtAgentCommision.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAgentCommision.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panAgentCommision.add(txtAgentCommision, gridBagConstraints);

        cboAgentCommision.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panAgentCommision.add(cboAgentCommision, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panRecurringDetails.add(panAgentCommision, gridBagConstraints);

        lblDailyDepositCalc.setText("Daily Deposit InterestCalculation basis");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblDailyDepositCalc, gridBagConstraints);

        lblWeekly.setText("Daily Deposit InterestCalculation basis");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblWeekly, gridBagConstraints);

        cboWeekly.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panRecurringDetails.add(cboWeekly, gridBagConstraints);

        panDailyIntCalc.setPreferredSize(new java.awt.Dimension(210, 20));
        panDailyIntCalc.setLayout(new java.awt.GridBagLayout());

        rdoDaily.setText("Daily");
        rdoDaily.setMaximumSize(new java.awt.Dimension(68, 15));
        rdoDaily.setMinimumSize(new java.awt.Dimension(66, 15));
        rdoDaily.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoDaily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDailyActionPerformed(evt);
            }
        });
        rdoDaily.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoDailyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDailyIntCalc.add(rdoDaily, gridBagConstraints);

        rdoWeekly.setText("Weekly");
        rdoWeekly.setMaximumSize(new java.awt.Dimension(85, 21));
        rdoWeekly.setMinimumSize(new java.awt.Dimension(70, 15));
        rdoWeekly.setPreferredSize(new java.awt.Dimension(70, 18));
        rdoWeekly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWeeklyActionPerformed(evt);
            }
        });
        rdoWeekly.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoWeeklyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDailyIntCalc.add(rdoWeekly, gridBagConstraints);

        rdoMonthly.setText("Monthly");
        rdoMonthly.setMaximumSize(new java.awt.Dimension(74, 21));
        rdoMonthly.setMinimumSize(new java.awt.Dimension(70, 18));
        rdoMonthly.setPreferredSize(new java.awt.Dimension(72, 18));
        rdoMonthly.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMonthlyActionPerformed(evt);
            }
        });
        rdoMonthly.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoMonthlyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panDailyIntCalc.add(rdoMonthly, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panRecurringDetails.add(panDailyIntCalc, gridBagConstraints);

        panInterestNotPayingValue.setLayout(new java.awt.GridBagLayout());

        txtInterestNotPayingValue.setMinimumSize(new java.awt.Dimension(50, 21));
        txtInterestNotPayingValue.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panInterestNotPayingValue.add(txtInterestNotPayingValue, gridBagConstraints);

        cboInterestNotPayingValue.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInterestNotPayingValue.add(cboInterestNotPayingValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 4);
        panRecurringDetails.add(panInterestNotPayingValue, gridBagConstraints);

        lblCustomerInterestNotPaying.setText("Interest not to be paid in case of premature closure before");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblCustomerInterestNotPaying, gridBagConstraints);

        cboMonthlyIntCalcMethod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 180, 0, 0);
        panRecurringDetails.add(cboMonthlyIntCalcMethod, gridBagConstraints);

        panInsBeyondMaturityDtYesorNo.setLayout(new java.awt.GridBagLayout());

        rdgInsBeyondMaturityDt.add(rdoInsBeyondMaturityDt_Yes);
        rdoInsBeyondMaturityDt_Yes.setText("Yes");
        rdoInsBeyondMaturityDt_Yes.setMaximumSize(new java.awt.Dimension(49, 18));
        rdoInsBeyondMaturityDt_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoInsBeyondMaturityDt_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsBeyondMaturityDtYesorNo.add(rdoInsBeyondMaturityDt_Yes, gridBagConstraints);

        rdgInsBeyondMaturityDt.add(rdoInsBeyondMaturityDt_No);
        rdoInsBeyondMaturityDt_No.setText("No");
        rdoInsBeyondMaturityDt_No.setMaximumSize(new java.awt.Dimension(41, 18));
        rdoInsBeyondMaturityDt_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoInsBeyondMaturityDt_No.setPreferredSize(new java.awt.Dimension(41, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panInsBeyondMaturityDtYesorNo.add(rdoInsBeyondMaturityDt_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panRecurringDetails.add(panInsBeyondMaturityDtYesorNo, gridBagConstraints);

        lblInsBeyondMaturityDtYesorNo.setText("Accept Installments Beyond Maturity Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblInsBeyondMaturityDtYesorNo, gridBagConstraints);

        panPartialWithdrawlAllowedForDD.setLayout(new java.awt.GridBagLayout());

        rdgPartialWithdrawlAllowedForDD.add(rdoPartialWithdrawlAllowedForDD_Yes);
        rdoPartialWithdrawlAllowedForDD_Yes.setText("Yes");
        rdoPartialWithdrawlAllowedForDD_Yes.setMaximumSize(new java.awt.Dimension(49, 18));
        rdoPartialWithdrawlAllowedForDD_Yes.setMinimumSize(new java.awt.Dimension(49, 18));
        rdoPartialWithdrawlAllowedForDD_Yes.setPreferredSize(new java.awt.Dimension(49, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPartialWithdrawlAllowedForDD.add(rdoPartialWithdrawlAllowedForDD_Yes, gridBagConstraints);

        rdgPartialWithdrawlAllowedForDD.add(rdoPartialWithdrawlAllowedForDD_No);
        rdoPartialWithdrawlAllowedForDD_No.setText("No");
        rdoPartialWithdrawlAllowedForDD_No.setMaximumSize(new java.awt.Dimension(41, 18));
        rdoPartialWithdrawlAllowedForDD_No.setMinimumSize(new java.awt.Dimension(41, 18));
        rdoPartialWithdrawlAllowedForDD_No.setPreferredSize(new java.awt.Dimension(41, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panPartialWithdrawlAllowedForDD.add(rdoPartialWithdrawlAllowedForDD_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panRecurringDetails.add(panPartialWithdrawlAllowedForDD, gridBagConstraints);

        lblPartialWithdrawlAllowedForDD.setText("Partial WithDrawl Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblPartialWithdrawlAllowedForDD, gridBagConstraints);

        lblRDIrregularIfInstallmentDue.setText("Waive penalty for delayed installments Upto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblRDIrregularIfInstallmentDue, gridBagConstraints);

        txtRDIrregularIfInstallmentDue.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRDIrregularIfInstallmentDue.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 0);
        panRecurringDetails.add(txtRDIrregularIfInstallmentDue, gridBagConstraints);

        lblIncaseOfIrregularRD.setText("In case of Irregular RD apply");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblIncaseOfIrregularRD, gridBagConstraints);

        panPartialWithdrawlAllowedForDD1.setMinimumSize(new java.awt.Dimension(180, 18));
        panPartialWithdrawlAllowedForDD1.setPreferredSize(new java.awt.Dimension(180, 18));
        panPartialWithdrawlAllowedForDD1.setLayout(new java.awt.GridBagLayout());

        rdgirregularRdApply.add(rdoIncaseOfIrregularRDSBRate);
        rdoIncaseOfIrregularRDSBRate.setText("SB rate");
        rdoIncaseOfIrregularRDSBRate.setMaximumSize(new java.awt.Dimension(69, 18));
        rdoIncaseOfIrregularRDSBRate.setMinimumSize(new java.awt.Dimension(69, 18));
        rdoIncaseOfIrregularRDSBRate.setPreferredSize(new java.awt.Dimension(69, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPartialWithdrawlAllowedForDD1.add(rdoIncaseOfIrregularRDSBRate, gridBagConstraints);

        rdgirregularRdApply.add(rdoIncaseOfIrregularRDRBRate);
        rdoIncaseOfIrregularRDRBRate.setText("RD rate");
        rdoIncaseOfIrregularRDRBRate.setMaximumSize(new java.awt.Dimension(99, 18));
        rdoIncaseOfIrregularRDRBRate.setMinimumSize(new java.awt.Dimension(99, 18));
        rdoIncaseOfIrregularRDRBRate.setPreferredSize(new java.awt.Dimension(99, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        panPartialWithdrawlAllowedForDD1.add(rdoIncaseOfIrregularRDRBRate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panRecurringDetails.add(panPartialWithdrawlAllowedForDD1, gridBagConstraints);

        lblIntApplicationSlab.setText("Interest Application Slab");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblIntApplicationSlab, gridBagConstraints);
        panRecurringDetails.add(cPanel2, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRecurringDetails.add(chkIntApplicationSlab, gridBagConstraints);

        panPenalRounded.setLayout(new java.awt.GridBagLayout());

        rdgAmountRounded.add(rdoPenalRounded_Yes);
        rdoPenalRounded_Yes.setText("Yes");
        rdoPenalRounded_Yes.setMaximumSize(new java.awt.Dimension(55, 18));
        rdoPenalRounded_Yes.setMinimumSize(new java.awt.Dimension(55, 18));
        rdoPenalRounded_Yes.setPreferredSize(new java.awt.Dimension(55, 18));
        rdoPenalRounded_Yes.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoPenalRounded_YesItemStateChanged(evt);
            }
        });
        rdoPenalRounded_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalRounded_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panPenalRounded.add(rdoPenalRounded_Yes, gridBagConstraints);

        rdgAmountRounded.add(rdoPenalRounded_No);
        rdoPenalRounded_No.setText("No");
        rdoPenalRounded_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoPenalRounded_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoPenalRounded_No.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdoPenalRounded_NoItemStateChanged(evt);
            }
        });
        rdoPenalRounded_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalRounded_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panPenalRounded.add(rdoPenalRounded_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panRecurringDetails.add(panPenalRounded, gridBagConstraints);

        lblPenalRounded.setText("Should Penal Interest be Rounded");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 2, 4);
        panRecurringDetails.add(lblPenalRounded, gridBagConstraints);

        cboRoundOffCriteriaPenal.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoundOffCriteriaPenal.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panRecurringDetails.add(cboRoundOffCriteriaPenal, gridBagConstraints);

        lblRoundOffCriteriaPenal.setText("Round Off Criteria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 1, 4);
        panRecurringDetails.add(lblRoundOffCriteriaPenal, gridBagConstraints);

        chkWeeklySpecial.setText("Weekly Special ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panRecurringDetails.add(chkWeeklySpecial, gridBagConstraints);

        lblDueOn.setText("Due On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRecurringDetails.add(lblDueOn, gridBagConstraints);

        rdgDueOn.add(rdoMonthEnd);
        rdoMonthEnd.setText("Month End");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRecurringDetails.add(rdoMonthEnd, gridBagConstraints);

        rdgDueOn.add(rdoInstallmentDay);
        rdoInstallmentDay.setText("Installment Day");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 92, 0, 0);
        panRecurringDetails.add(rdoInstallmentDay, gridBagConstraints);

        chkRdNature.setText("RD Nature");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panRecurringDetails.add(chkRdNature, gridBagConstraints);

        lblGracePeriod.setText("Grace Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(0, 214, 0, 2);
        panRecurringDetails.add(lblGracePeriod, gridBagConstraints);

        txtGracePeriod.setAllowAll(true);
        txtGracePeriod.setAllowNumber(true);
        txtGracePeriod.setMinimumSize(new java.awt.Dimension(100, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        panRecurringDetails.add(txtGracePeriod, gridBagConstraints);

        panRD.add(panRecurringDetails, new java.awt.GridBagConstraints());
        panRecurringDetails.getAccessibleContext().setAccessibleName("");

        panDelayedInstallmets.setBorder(javax.swing.BorderFactory.createTitledBorder("Delayed Installments Details"));
        panDelayedInstallmets.setEnabled(false);
        panDelayedInstallmets.setMinimumSize(new java.awt.Dimension(790, 100));
        panDelayedInstallmets.setPreferredSize(new java.awt.Dimension(790, 80));
        panDelayedInstallmets.setLayout(new java.awt.GridBagLayout());

        panLastInstallmentAllowed.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panDelayedInstallmets.add(panLastInstallmentAllowed, gridBagConstraints);

        panBtnDelayed.setLayout(new java.awt.GridBagLayout());

        btnDelayedNew.setText("New");
        btnDelayedNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelayedNewActionPerformed(evt);
            }
        });
        panBtnDelayed.add(btnDelayedNew, new java.awt.GridBagConstraints());

        btnDelayedSave.setText("Save");
        btnDelayedSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelayedSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 1;
        panBtnDelayed.add(btnDelayedSave, gridBagConstraints);

        btnDelayedDel.setText("Delete");
        btnDelayedDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelayedDelActionPerformed(evt);
            }
        });
        panBtnDelayed.add(btnDelayedDel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        panDelayedInstallmets.add(panBtnDelayed, gridBagConstraints);

        panDelayedInstallmet.setLayout(new java.awt.GridBagLayout());

        srpDelayedInstallmet.setMinimumSize(new java.awt.Dimension(250, 75));
        srpDelayedInstallmet.setPreferredSize(new java.awt.Dimension(250, 75));

        tblDelayedInstallmet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "From Amt", "To Amt", "Penalty Charges"
            }
        ));
        tblDelayedInstallmet.setMinimumSize(new java.awt.Dimension(350, 100));
        tblDelayedInstallmet.setPreferredScrollableViewportSize(new java.awt.Dimension(350, 400));
        tblDelayedInstallmet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDelayedInstallmetMousePressed(evt);
            }
        });
        srpDelayedInstallmet.setViewportView(tblDelayedInstallmet);

        panDelayedInstallmet.add(srpDelayedInstallmet, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDelayedInstallmets.add(panDelayedInstallmet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panRD.add(panDelayedInstallmets, gridBagConstraints);

        tabDepositsProduct.addTab("Recurring Deposit", panRD);

        panFloatingRateAccount.setLayout(new java.awt.GridBagLayout());

        panAgentsCalculations.setLayout(new java.awt.GridBagLayout());

        lblDate.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblDate, gridBagConstraints);

        tdtDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(tdtDate, gridBagConstraints);

        lblToDate.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblToDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(tdtToDate, gridBagConstraints);

        lblFromAmount.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblFromAmount, gridBagConstraints);

        lblToAmount.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblToAmount, gridBagConstraints);

        lblFromPeriod.setText("From Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblFromPeriod, gridBagConstraints);

        panFromPeriod.setLayout(new java.awt.GridBagLayout());

        txtFromPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtFromPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panFromPeriod.add(txtFromPeriod, gridBagConstraints);

        cboFromPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panFromPeriod.add(cboFromPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(panFromPeriod, gridBagConstraints);

        lblToPeriod.setText("To Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblToPeriod, gridBagConstraints);

        panToPeriod.setLayout(new java.awt.GridBagLayout());

        txtToPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtToPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        panToPeriod.add(txtToPeriod, new java.awt.GridBagConstraints());

        cboToPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        panToPeriod.add(cboToPeriod, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(panToPeriod, gridBagConstraints);

        lblRateInterest.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblRateInterest, gridBagConstraints);

        txtCommisionRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCommisionRate.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(txtCommisionRate, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(panButtons, gridBagConstraints);

        lblDelayedChargesDet.setText("Per Rs 100 / Per Month Delay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentsCalculations.add(lblDelayedChargesDet, gridBagConstraints);

        txtFromAmt.setEnabled(false);
        txtFromAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentsCalculations.add(txtFromAmt, gridBagConstraints);

        txtToAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmt.setEnabled(false);
        txtToAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentsCalculations.add(txtToAmt, gridBagConstraints);

        txtDelayedChargesAmt.setEnabled(false);
        txtDelayedChargesAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentsCalculations.add(txtDelayedChargesAmt, gridBagConstraints);

        lblDelayedToAmt.setText("To Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentsCalculations.add(lblDelayedToAmt, gridBagConstraints);

        lblDelayedFromAmt.setText("From Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentsCalculations.add(lblDelayedFromAmt, gridBagConstraints);

        lblDelayedChargesAmt.setText("Penalty Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAgentsCalculations.add(lblDelayedChargesAmt, gridBagConstraints);

        lblInstType.setText("Installment Type");
        lblInstType.setMaximumSize(new java.awt.Dimension(105, 18));
        lblInstType.setMinimumSize(new java.awt.Dimension(105, 18));
        lblInstType.setPreferredSize(new java.awt.Dimension(105, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(lblInstType, gridBagConstraints);

        cboInstType.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        cboInstType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentsCalculations.add(cboInstType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panFloatingRateAccount.add(panAgentsCalculations, gridBagConstraints);

        srpInterestTable.setMinimumSize(new java.awt.Dimension(450, 400));
        srpInterestTable.setPreferredSize(new java.awt.Dimension(550, 400));

        tblInterestTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From Date", "To Date", "From Amount", "To Amount", "From Period", "To Period", "Rate of Interest"
            }
        ));
        tblInterestTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInterestTableMousePressed(evt);
            }
        });
        srpInterestTable.setViewportView(tblInterestTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFloatingRateAccount.add(srpInterestTable, gridBagConstraints);

        tabDepositsProduct.addTab("Agent's Commission Rate Maintenance", panFloatingRateAccount);

        panWeeklyDepositSlab.setLayout(new java.awt.GridBagLayout());

        srpWeeklySlabSettings.setAutoscrolls(true);
        srpWeeklySlabSettings.setMinimumSize(new java.awt.Dimension(400, 450));
        srpWeeklySlabSettings.setPreferredSize(new java.awt.Dimension(400, 450));

        tblWeeklySlabSettings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Install From", "Install To", "Penal", "Installment No"
            }
        ));
        tblWeeklySlabSettings.setMinimumSize(new java.awt.Dimension(350, 1000));
        tblWeeklySlabSettings.setOpaque(false);
        tblWeeklySlabSettings.setPreferredSize(new java.awt.Dimension(270, 1000));
        tblWeeklySlabSettings.setReorderingAllowed(false);
        tblWeeklySlabSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblWeeklySlabSettingsMouseClicked(evt);
            }
        });
        srpWeeklySlabSettings.setViewportView(tblWeeklySlabSettings);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 46);
        panWeeklyDepositSlab.add(srpWeeklySlabSettings, gridBagConstraints);

        panSlab.setBorder(javax.swing.BorderFactory.createTitledBorder("Slab Settings"));
        panSlab.setMinimumSize(new java.awt.Dimension(300, 320));
        panSlab.setPreferredSize(new java.awt.Dimension(600, 600));
        panSlab.setLayout(new java.awt.GridBagLayout());

        panSlabDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSlabDetails.setMinimumSize(new java.awt.Dimension(250, 200));
        panSlabDetails.setPreferredSize(new java.awt.Dimension(250, 200));
        panSlabDetails.setLayout(new java.awt.GridBagLayout());

        lblProductType.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(lblProductType, gridBagConstraints);

        lbInstallmentFrom.setText("Installment From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(lbInstallmentFrom, gridBagConstraints);

        lblInstallmentTo.setText("Installment To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(lblInstallmentTo, gridBagConstraints);

        lblPenal.setText("Penal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(lblPenal, gridBagConstraints);

        lblInstallmentNo.setText("Installment No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(lblInstallmentNo, gridBagConstraints);

        cboProductType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(cboProductType, gridBagConstraints);

        txtInstallmentFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstallmentFrom.setNextFocusableComponent(txtInstallmentTo);
        txtInstallmentFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstallmentFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(txtInstallmentFrom, gridBagConstraints);

        txtInstallmentTo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInstallmentTo.setNextFocusableComponent(txtPenal);
        txtInstallmentTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInstallmentToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(txtInstallmentTo, gridBagConstraints);

        txtPenal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(txtPenal, gridBagConstraints);

        txtInstallmentNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSlabDetails.add(txtInstallmentNo, gridBagConstraints);

        panSlab.add(panSlabDetails, new java.awt.GridBagConstraints());

        panSalaryStructureButtons.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalaryStructureButtons.setMinimumSize(new java.awt.Dimension(140, 55));
        panSalaryStructureButtons.setPreferredSize(new java.awt.Dimension(140, 55));
        panSalaryStructureButtons.setLayout(new java.awt.GridBagLayout());

        btnWeeklyDepSlabNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnWeeklyDepSlabNew.setMinimumSize(new java.awt.Dimension(30, 25));
        btnWeeklyDepSlabNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnWeeklyDepSlabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWeeklyDepSlabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panSalaryStructureButtons.add(btnWeeklyDepSlabNew, gridBagConstraints);

        btnWeeklyDepSlabSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnWeeklyDepSlabSave.setDefaultCapable(false);
        btnWeeklyDepSlabSave.setMinimumSize(new java.awt.Dimension(30, 25));
        btnWeeklyDepSlabSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnWeeklyDepSlabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWeeklyDepSlabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panSalaryStructureButtons.add(btnWeeklyDepSlabSave, gridBagConstraints);

        btnWeeklyDepSlabDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnWeeklyDepSlabDelete.setMinimumSize(new java.awt.Dimension(30, 25));
        btnWeeklyDepSlabDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnWeeklyDepSlabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWeeklyDepSlabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panSalaryStructureButtons.add(btnWeeklyDepSlabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panSlab.add(panSalaryStructureButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(50, 46, 0, 0);
        panWeeklyDepositSlab.add(panSlab, gridBagConstraints);

        tabDepositsProduct.addTab("Weekly Deposit Slab", panWeeklyDepositSlab);

        panThriftBenevolent.setPreferredSize(new java.awt.Dimension(600, 499));
        panThriftBenevolent.setLayout(new java.awt.GridBagLayout());

        panInsallmentDetails.setPreferredSize(new java.awt.Dimension(356, 100));
        panInsallmentDetails.setLayout(new java.awt.GridBagLayout());

        txtInstallmentAmount.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 6, 0, 0);
        panInsallmentDetails.add(txtInstallmentAmount, gridBagConstraints);

        tdtEffectiveDate.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tdtEffectiveDateAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 0, 0, 0);
        panInsallmentDetails.add(tdtEffectiveDate, gridBagConstraints);

        lblEffectiveDate.setText("Effective date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 26, 0, 0);
        panInsallmentDetails.add(lblEffectiveDate, gridBagConstraints);

        lblInstallmentAmount.setText("Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        panInsallmentDetails.add(lblInstallmentAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 122;
        gridBagConstraints.ipady = 77;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(144, 23, 0, 0);
        panThriftBenevolent.add(panInsallmentDetails, gridBagConstraints);

        tblThriftBenevelontDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Effective Date", "Installment Amount"
            }
        ));
        srpThriftBenevelontDetails.setViewportView(tblThriftBenevelontDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 406;
        gridBagConstraints.ipady = 352;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(38, 42, 204, 43);
        panThriftBenevolent.add(srpThriftBenevelontDetails, gridBagConstraints);

        tabDepositsProduct.addTab("Thrift/Benevolent", panThriftBenevolent);

        panRDClosingROISetting.setBorder(javax.swing.BorderFactory.createTitledBorder("Irregular RD Closing ROI Settings"));
        panRDClosingROISetting.setLayout(new java.awt.GridBagLayout());

        panSlab2.setBorder(javax.swing.BorderFactory.createTitledBorder("ROI Settings"));
        panSlab2.setMinimumSize(new java.awt.Dimension(500, 320));
        panSlab2.setPreferredSize(new java.awt.Dimension(800, 800));
        panSlab2.setLayout(new java.awt.GridBagLayout());

        panROIPrematureClosure.setBorder(javax.swing.BorderFactory.createTitledBorder("Premature Closure"));
        panROIPrematureClosure.setMinimumSize(new java.awt.Dimension(350, 150));
        panROIPrematureClosure.setPreferredSize(new java.awt.Dimension(250, 150));
        panROIPrematureClosure.setLayout(new java.awt.GridBagLayout());

        lblProductType2.setText("Rate to be set for Premature closure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 16, 0, 0);
        panROIPrematureClosure.add(lblProductType2, gridBagConstraints);

        lbInstallmentFrom2.setText("Product Id of ROI Settings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 0, 0);
        panROIPrematureClosure.add(lbInstallmentFrom2, gridBagConstraints);

        cboprmatureCloseRate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboprmatureCloseRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboprmatureCloseRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 8, 0, 16);
        panROIPrematureClosure.add(cboprmatureCloseRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.ipady = -1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 22, 16);
        panROIPrematureClosure.add(cboPrmatureCloseProduct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = 163;
        gridBagConstraints.ipady = -60;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 105, 0, 121);
        panSlab2.add(panROIPrematureClosure, gridBagConstraints);

        panROIIrregularRDClosure.setBorder(javax.swing.BorderFactory.createTitledBorder("Irregular RD"));
        panROIIrregularRDClosure.setMinimumSize(new java.awt.Dimension(350, 150));
        panROIIrregularRDClosure.setPreferredSize(new java.awt.Dimension(250, 150));
        panROIIrregularRDClosure.setLayout(new java.awt.GridBagLayout());

        cLabel4.setText("Rate to be set for Irregular RD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 16, 0, 0);
        panROIIrregularRDClosure.add(cLabel4, gridBagConstraints);

        cLabel5.setText("Product Id of ROI Settings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 20, 0, 0);
        panROIIrregularRDClosure.add(cLabel5, gridBagConstraints);

        cboIrregularRDCloseRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIrregularRDCloseRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 29, 0, 16);
        panROIIrregularRDClosure.add(cboIrregularRDCloseRate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 75;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 29, 16, 16);
        panROIIrregularRDClosure.add(cboIrregularRDCloseProduct, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.ipadx = 163;
        gridBagConstraints.ipady = -59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 105, 41, 121);
        panSlab2.add(panROIIrregularRDClosure, gridBagConstraints);

        cPanel4.setLayout(new java.awt.GridBagLayout());

        cLabel6.setText("Is Irregular Rate Change required ? ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        cPanel4.add(cLabel6, gridBagConstraints);

        chkRDClosingOtherROI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRDClosingOtherROIActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        cPanel4.add(chkRDClosingOtherROI, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(39, 256, 0, 0);
        panSlab2.add(cPanel4, gridBagConstraints);

        chkIntForIrregularRD.setText("Is interest to be apply for Irregular RD?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 245, 0, 0);
        panSlab2.add(chkIntForIrregularRD, gridBagConstraints);

        chkSpecialRD.setText("Special RD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 245, 0, 0);
        panSlab2.add(chkSpecialRD, gridBagConstraints);

        lblSpecialRDInstalments.setText("No of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 42, 0, 0);
        panSlab2.add(lblSpecialRDInstalments, gridBagConstraints);

        txtSpecialRDInstallments.setAllowNumber(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 94;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 8, 0, 0);
        panSlab2.add(txtSpecialRDInstallments, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 239;
        gridBagConstraints.ipady = 71;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(181, 95, 42, 50);
        panRDClosingROISetting.add(panSlab2, gridBagConstraints);

        tabDepositsProduct.addTab("Irregular RD Closing ROI Settings", panRDClosingROISetting);

        panAgenCommissionSlab.setLayout(new java.awt.GridBagLayout());

        panAgentCommSlab.setBorder(javax.swing.BorderFactory.createTitledBorder("Slab Settings"));
        panAgentCommSlab.setMinimumSize(new java.awt.Dimension(300, 320));
        panAgentCommSlab.setPreferredSize(new java.awt.Dimension(600, 600));
        panAgentCommSlab.setLayout(new java.awt.GridBagLayout());

        panAgentCommSlabDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAgentCommSlabDetails.setMinimumSize(new java.awt.Dimension(250, 200));
        panAgentCommSlabDetails.setPreferredSize(new java.awt.Dimension(250, 200));
        panAgentCommSlabDetails.setLayout(new java.awt.GridBagLayout());

        lblAgentCommSlabAmtFrom.setText("Amount From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentCommSlabDetails.add(lblAgentCommSlabAmtFrom, gridBagConstraints);

        lblAgentCommSlabAmtTo.setText("Amount To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentCommSlabDetails.add(lblAgentCommSlabAmtTo, gridBagConstraints);

        lblAgentCommSlabPercent.setText("Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentCommSlabDetails.add(lblAgentCommSlabPercent, gridBagConstraints);

        txtAgentCommSlabAmtFrom.setAllowNumber(true);
        txtAgentCommSlabAmtFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAgentCommSlabAmtFrom.setNextFocusableComponent(txtInstallmentTo);
        txtAgentCommSlabAmtFrom.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAgentCommSlabAmtFromFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentCommSlabDetails.add(txtAgentCommSlabAmtFrom, gridBagConstraints);

        TxtAgentCommSlabAmtTo.setAllowNumber(true);
        TxtAgentCommSlabAmtTo.setMinimumSize(new java.awt.Dimension(100, 21));
        TxtAgentCommSlabAmtTo.setNextFocusableComponent(txtPenal);
        TxtAgentCommSlabAmtTo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                TxtAgentCommSlabAmtToFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentCommSlabDetails.add(TxtAgentCommSlabAmtTo, gridBagConstraints);

        txtAgentCommSlabPercent.setAllowNumber(true);
        txtAgentCommSlabPercent.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgentCommSlabDetails.add(txtAgentCommSlabPercent, gridBagConstraints);

        panAgentCommSlab.add(panAgentCommSlabDetails, new java.awt.GridBagConstraints());

        panSalaryStructureButtons1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSalaryStructureButtons1.setMinimumSize(new java.awt.Dimension(140, 55));
        panSalaryStructureButtons1.setPreferredSize(new java.awt.Dimension(140, 55));
        panSalaryStructureButtons1.setLayout(new java.awt.GridBagLayout());

        btnAgentCommissionSlabNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnAgentCommissionSlabNew.setMinimumSize(new java.awt.Dimension(30, 25));
        btnAgentCommissionSlabNew.setPreferredSize(new java.awt.Dimension(30, 30));
        btnAgentCommissionSlabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentCommissionSlabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        panSalaryStructureButtons1.add(btnAgentCommissionSlabNew, gridBagConstraints);

        btnAgentCommissionSlabSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnAgentCommissionSlabSave.setDefaultCapable(false);
        btnAgentCommissionSlabSave.setMinimumSize(new java.awt.Dimension(30, 25));
        btnAgentCommissionSlabSave.setPreferredSize(new java.awt.Dimension(30, 30));
        btnAgentCommissionSlabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentCommissionSlabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panSalaryStructureButtons1.add(btnAgentCommissionSlabSave, gridBagConstraints);

        btnAgentCommissionSlabDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnAgentCommissionSlabDelete.setMinimumSize(new java.awt.Dimension(30, 25));
        btnAgentCommissionSlabDelete.setPreferredSize(new java.awt.Dimension(30, 30));
        btnAgentCommissionSlabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgentCommissionSlabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        panSalaryStructureButtons1.add(btnAgentCommissionSlabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(20, 0, 0, 0);
        panAgentCommSlab.add(panSalaryStructureButtons1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 69;
        gridBagConstraints.ipady = 55;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(35, 53, 7, 0);
        panAgenCommissionSlab.add(panAgentCommSlab, gridBagConstraints);

        srpAgentCommSlabSettings.setAutoscrolls(true);
        srpAgentCommSlabSettings.setMinimumSize(new java.awt.Dimension(400, 450));

        tblAgentCommSlabSettings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Type", "Install From", "Install To", "Penal", "Installment No"
            }
        ));
        tblAgentCommSlabSettings.setMinimumSize(new java.awt.Dimension(350, 1000));
        tblAgentCommSlabSettings.setOpaque(false);
        tblAgentCommSlabSettings.setPreferredSize(new java.awt.Dimension(270, 1000));
        tblAgentCommSlabSettings.setReorderingAllowed(false);
        tblAgentCommSlabSettings.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAgentCommSlabSettingsMouseClicked(evt);
            }
        });
        srpAgentCommSlabSettings.setViewportView(tblAgentCommSlabSettings);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.ipadx = -163;
        gridBagConstraints.ipady = -89;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(45, 46, 0, 99);
        panAgenCommissionSlab.add(srpAgentCommSlabSettings, gridBagConstraints);

        cLabel3.setText("Agent Commission Disbursal Slabwise Settings Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(70, 63, 0, 0);
        panAgenCommissionSlab.add(cLabel3, gridBagConstraints);

        panAgentCommCalcMethod.setBorder(javax.swing.BorderFactory.createTitledBorder("Calculation Method"));
        panAgentCommCalcMethod.setLayout(new java.awt.GridBagLayout());

        cLabel7.setText("Commssion Calculation Method");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(7, 25, 0, 0);
        panAgentCommCalcMethod.add(cLabel7, gridBagConstraints);

        cboAgentCommCalcMethod.setMaximumSize(new java.awt.Dimension(100, 21));
        cboAgentCommCalcMethod.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(6, 18, 8, 277);
        panAgentCommCalcMethod.add(cboAgentCommCalcMethod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.ipady = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 83, 0, 0);
        panAgenCommissionSlab.add(panAgentCommCalcMethod, gridBagConstraints);

        chkAgentCommSlabRequired.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAgentCommSlabRequiredActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(70, 18, 0, 0);
        panAgenCommissionSlab.add(chkAgentCommSlabRequired, gridBagConstraints);

        tabDepositsProduct.addTab("Agent Commission Disbursal Slab", panAgenCommissionSlab);
        panAgenCommissionSlab.getAccessibleContext().setAccessibleName("Agent Commission Disbursal Slab");

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panDepositsProduct.add(tabDepositsProduct, gridBagConstraints);

        getContentPane().add(panDepositsProduct, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

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

        mbrDepositsProduct.add(mnuProcess);

        setJMenuBar(mbrDepositsProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoSBRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSBRateActionPerformed
        // TODO add your handling code here:
        if (rdoSBRate.isSelected()) {
            lblDepositsProdFixd.setVisible(true);
            cboDepositsProdFixd.setVisible(false);
            lblpreMatIntType.setVisible(false);
            cboPreMatIntType.setVisible(false);
            cmbSbProduct.setVisible(true);
        }
    }//GEN-LAST:event_rdoSBRateActionPerformed

    private void rdoDepositRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDepositRateActionPerformed
        // TODO add your handling code here:
        if (rdoDepositRate.isSelected()) {
            lblDepositsProdFixd.setVisible(true);
            cboDepositsProdFixd.setVisible(true);
            cboPreMatIntType.setVisible(true);
            lblpreMatIntType.setVisible(true);
             cmbSbProduct.setVisible(false);
        } else if(rdoSBRate.isSelected()){
            lblDepositsProdFixd.setVisible(true);
            cboDepositsProdFixd.setVisible(false);
            cboPreMatIntType.setVisible(false);
            lblpreMatIntType.setVisible(false);
            cmbSbProduct.setVisible(true);
        } else {
            lblDepositsProdFixd.setVisible(false);
            cboDepositsProdFixd.setVisible(false);
            cboPreMatIntType.setVisible(false);
            lblpreMatIntType.setVisible(false);
            cmbSbProduct.setVisible(false);
        }
    }//GEN-LAST:event_rdoDepositRateActionPerformed

    private void txtTransferOutHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransferOutHeadFocusLost
        // TODO add your handling code here:
        if (!txtTransferOutHead.getText().equals("")) {
            txtTransferOutHeadDesc.setText(getAccHeadDesc(txtTransferOutHead.getText()));
            txtTransferOutHeadDesc.setEnabled(false);
        }
    }//GEN-LAST:event_txtTransferOutHeadFocusLost

    private void btnPostageAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostageAcHdActionPerformed
        // TODO add your handling code here:
        popUp(POSTAGEACHD);
    }//GEN-LAST:event_btnPostageAcHdActionPerformed

    private void txtPostageAcHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostageAcHdFocusLost
        // TODO add your handling code here:
        if (!(txtPostageAcHd.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtPostageAcHd, "product.deposits.getAcctHeadList");
            btnPostageAcHd.setToolTipText(getAccHeadDesc(txtPostageAcHd.getText()));
        }
    }//GEN-LAST:event_txtPostageAcHdFocusLost

    private void btnSaveComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_btnSaveComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSaveComponentHidden

    private void cboEitherofTwoRatesChooseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEitherofTwoRatesChooseActionPerformed
        // TODO add your handling code here:

        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (cboEitherofTwoRatesChoose.getSelectedIndex() > 0) {
                chkRateAsOnDateOfRenewal.setSelected(false);
                chkRateAsOnDateOfMaturity.setSelected(false);
                chkRateAsOnDateOfRenewal.setEnabled(false);
                chkRateAsOnDateOfMaturity.setEnabled(false);
            } else if (cboEitherofTwoRatesChoose.getSelectedIndex() <= 0 && dateOfRenew == true) {
                chkRateAsOnDateOfRenewal.setEnabled(true);
                chkRateAsOnDateOfMaturity.setEnabled(false);
                cboEitherofTwoRatesChoose.setEnabled(false);
            } else if (cboEitherofTwoRatesChoose.getSelectedIndex() <= 0 && dateOfMat == true) {
                chkRateAsOnDateOfRenewal.setEnabled(false);
                chkRateAsOnDateOfMaturity.setEnabled(true);
                cboEitherofTwoRatesChoose.setEnabled(false);
            } else if (cboEitherofTwoRatesChoose.getSelectedIndex() <= 0 && dateOfRenew == false && dateOfMat == false) {
                chkRateAsOnDateOfRenewal.setEnabled(true);
                chkRateAsOnDateOfMaturity.setEnabled(true);
                cboEitherofTwoRatesChoose.setEnabled(true);
            }
            dateOfRenew = false;
            dateOfMat = false;
        }
    }//GEN-LAST:event_cboEitherofTwoRatesChooseActionPerformed

    private void chkRateAsOnDateOfMaturityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRateAsOnDateOfMaturityActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
            if (chkRateAsOnDateOfMaturity.isSelected() == true) {
                dateOfMat = true;
                chkRateAsOnDateOfRenewal.setSelected(false);
                chkRateAsOnDateOfRenewal.setEnabled(false);
                cboEitherofTwoRatesChoose.setEnabled(false);
            } else {
                dateOfMat = false;
                chkRateAsOnDateOfRenewal.setEnabled(true);
                cboEitherofTwoRatesChoose.setEnabled(true);
            }
            cboEitherofTwoRatesChoose.setSelectedItem("");
        }
    }//GEN-LAST:event_chkRateAsOnDateOfMaturityActionPerformed

    private void chkRateAsOnDateOfRenewalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRateAsOnDateOfRenewalActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
//        if (cboEitherofTwoRatesChoose.getSelectedIndex() > 0) {
//            chkRateAsOnDateOfRenewal.setSelected(false);
//            chkRateAsOnDateOfMaturity.setSelected(false);
//            chkRateAsOnDateOfRenewal.setEnabled(false);
//            chkRateAsOnDateOfMaturity.setEnabled(false);
//        } else if (cboEitherofTwoRatesChoose.getSelectedIndex() <= 0 && dateOfRenew == true) {
//            chkRateAsOnDateOfRenewal.setEnabled(true);
//            chkRateAsOnDateOfMaturity.setEnabled(false);
//            cboEitherofTwoRatesChoose.setEnabled(false);
//        } else if (cboEitherofTwoRatesChoose.getSelectedIndex() <= 0 && dateOfMat == true) {
//            chkRateAsOnDateOfRenewal.setEnabled(false);
//            chkRateAsOnDateOfMaturity.setEnabled(true);
//            cboEitherofTwoRatesChoose.setEnabled(false);
//        } else if (cboEitherofTwoRatesChoose.getSelectedIndex() <= 0 && dateOfRenew == false && dateOfMat == false) {
//            chkRateAsOnDateOfRenewal.setEnabled(true);
//            chkRateAsOnDateOfMaturity.setEnabled(true);
//            cboEitherofTwoRatesChoose.setEnabled(true);
//        }
               //Added by Chithra on 21-04-14
            if (chkRateAsOnDateOfRenewal.isSelected() == true) {
                dateOfRenew = true;
                chkRateAsOnDateOfMaturity.setSelected(false);
                chkRateAsOnDateOfMaturity.setEnabled(false);
                cboEitherofTwoRatesChoose.setEnabled(false);
            } else {
                dateOfRenew = false;
                chkRateAsOnDateOfMaturity.setEnabled(true);
                cboEitherofTwoRatesChoose.setEnabled(true);
            }
            cboEitherofTwoRatesChoose.setSelectedItem("");
        
            dateOfMat = false;
        }
    }//GEN-LAST:event_chkRateAsOnDateOfRenewalActionPerformed

    private void rdoSameNoRenewalAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSameNoRenewalAllowed_NoActionPerformed
        // TODO add your handling code here:

        lblMaxNopfTimes1.setVisible(false);
        txtMaxNopfTimes1.setVisible(false);
        lblMaxNopfTimes1.setEnabled(false);
        txtMaxNopfTimes1.setEditable(false);
        txtMaxNopfTimes1.setEnabled(false);
        txtMaxNopfTimes1.setText("");
    }//GEN-LAST:event_rdoSameNoRenewalAllowed_NoActionPerformed

    private void rdoSameNoRenewalAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSameNoRenewalAllowed_YesActionPerformed
        // TODO add your handling code here:

        lblMaxNopfTimes1.setVisible(true);
        txtMaxNopfTimes1.setVisible(true);
        lblMaxNopfTimes1.setEnabled(true);
        txtMaxNopfTimes1.setEditable(true);
        txtMaxNopfTimes1.setEnabled(true);
    }//GEN-LAST:event_rdoSameNoRenewalAllowed_YesActionPerformed

    private void rdoAutoRenewalAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAutoRenewalAllowed_NoActionPerformed
        // TODO add your handling code here:

        if (rdoAutoRenewalAllowed_No.isSelected()) {
            txtMaxNopfTimes.setText("");
            txtMaxNopfTimes.setEnabled(false);
            txtMaxNopfTimes.setEditable(false);
        }
    }//GEN-LAST:event_rdoAutoRenewalAllowed_NoActionPerformed

    private void rdoAutoRenewalAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAutoRenewalAllowed_YesActionPerformed
        // TODO add your handling code here:
        txtMaxNopfTimes.setEnabled(true);
        txtMaxNopfTimes.setEditable(true);
    }//GEN-LAST:event_rdoAutoRenewalAllowed_YesActionPerformed

    private void rdoRenewalOfDepositAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalOfDepositAllowed_NoActionPerformed
        // TODO add your handling code here:
        if (rdoRenewalOfDepositAllowed_No.isSelected()) {
            txtMinNoOfDays.setText("");
            txtMinNoOfDays.setEnabled(false);
            txtMinNoOfDays.setEditable(false);

            cboMinNoOfDays.setSelectedItem("");
            cboMinNoOfDays.setEnabled(false);
            cboMinNoOfDays.setEditable(false);
            chkRateAsOnDateOfRenewal.setEnabled(false);
            txtIntPeriodForBackDatedRenewal.setText("");
            txtIntPeriodForBackDatedRenewal.setEnabled(false);
            txtIntPeriodForBackDatedRenewal.setEditable(false);

            cboIntPeriodForBackDatedRenewal.setSelectedItem("");
            cboIntPeriodForBackDatedRenewal.setEnabled(false);
            cboIntPeriodForBackDatedRenewal.setEditable(false);

            cboIntType.setSelectedItem("");
            cboIntType.setEnabled(false);
            cboIntType.setEditable(false);

            rdoClosedwithinperiod_No.setEnabled(false);
            txtMaxPeriodMDt.setText("");
            txtMaxPeriodMDt.setEnabled(false);
            txtMaxPeriodMDt.setEditable(false);
            txtRenewedclosedbefore.setEnabled(false);
            cboMaxPeriodMDt.setSelectedItem("");
            cboMaxPeriodMDt.setEnabled(false);
            cboMaxPeriodMDt.setEditable(false);
            txtMinimumRenewalDeposit.setEnabled(false);
            cboMinimumRenewalDeposit.setEnabled(false);
            cboRenewedclosedbefore.setEnabled(false);
            rdoClosedwithinperiod_Yes.setEnabled(false);
            rdoInterestalreadyPaid_Yes.setEnabled(false);
            rdoInterestalreadyPaid_No.setEnabled(false);
            rdoInterestrateappliedoverdue_Yes.setEnabled(false);
            rdoInterestrateappliedoverdue_No.setEnabled(false);
            rdoSBRateOneRate_Yes.setEnabled(false);
            rdoSBRateOneRate_No.setEnabled(false);
            rdoBothRateNotAvail_Yes.setEnabled(false);
            rdoBothRateNotAvail_No.setEnabled(false);
        }
    }//GEN-LAST:event_rdoRenewalOfDepositAllowed_NoActionPerformed

    private void rdoRenewalOfDepositAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRenewalOfDepositAllowed_YesActionPerformed
        // TODO add your handling code here:
        txtMinNoOfDays.setEnabled(true);
        txtMinNoOfDays.setEditable(true);
        cboMinNoOfDays.setEnabled(true);

        txtIntPeriodForBackDatedRenewal.setEnabled(true);
        txtIntPeriodForBackDatedRenewal.setEditable(true);
        cboIntPeriodForBackDatedRenewal.setEnabled(true);
        chkRateAsOnDateOfRenewal.setEnabled(true);
        cboIntType.setEnabled(true);
        txtRenewedclosedbefore.setEnabled(true);
        txtMaxPeriodMDt.setEnabled(true);
        txtMaxPeriodMDt.setEditable(true);
        cboMaxPeriodMDt.setEnabled(true);
        txtMinimumRenewalDeposit.setEnabled(true);
        txtMinimumRenewalDeposit.setText("");
        cboMinimumRenewalDeposit.setEnabled(true);
        cboMinimumRenewalDeposit.setSelectedItem("");
        cboRenewedclosedbefore.setEnabled(true);
        cboRenewedclosedbefore.setSelectedItem("");
        rdoClosedwithinperiod_Yes.setEnabled(true);
        rdoClosedwithinperiod_No.setEnabled(true);
        rdoInterestalreadyPaid_Yes.setEnabled(true);
        rdoInterestalreadyPaid_No.setEnabled(true);
        rdoInterestrateappliedoverdue_Yes.setEnabled(true);
        rdoInterestrateappliedoverdue_No.setEnabled(true);
        rdoSBRateOneRate_Yes.setEnabled(true);
        rdoSBRateOneRate_No.setEnabled(true);
        rdoBothRateNotAvail_Yes.setEnabled(true);
        rdoBothRateNotAvail_No.setEnabled(true);
    }//GEN-LAST:event_rdoRenewalOfDepositAllowed_YesActionPerformed

    private void txtMinimumRenewalDepositFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinimumRenewalDepositFocusLost
        // TODO add your handling code here:
        if (txtMinimumRenewalDeposit.getText().length() > 0) {
            if (CommonUtil.convertObjToDouble(txtMinDepositPeriod.getText()).doubleValue() < CommonUtil.convertObjToDouble(txtMinimumRenewalDeposit.getText()).doubleValue()
                    && cboMinimumRenewalDeposit.getSelectedItem().equals(cboMinDepositPeriod.getSelectedItem())) {
                ClientUtil.showAlertWindow("");
            }
        }
    }//GEN-LAST:event_txtMinimumRenewalDepositFocusLost

    private void txtProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductIDActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_txtProductIDActionPerformed

    private void cboOperatesLikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboOperatesLikeActionPerformed

        if (cboOperatesLike.getSelectedIndex() > 0) {
            tabDepositsProduct.remove(panRD);
            tabDepositsProduct.remove(panFloatingRateAccount);
            tabDepositsProduct.resetVisits();
            if (((String) cboOperatesLike.getSelectedItem()).startsWith("Fixed") || ((String) cboOperatesLike.getSelectedItem()).startsWith("Cummulative")) {
                lblFDRenewalSameNoTranPrincAmt.setVisible(true);
                chkFDRenewalSameNoTranPrincAmt.setVisible(true);
                tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
            } else {
                lblFDRenewalSameNoTranPrincAmt.setVisible(false);
                chkFDRenewalSameNoTranPrincAmt.setVisible(false);                
            }
            if (((String) cboOperatesLike.getSelectedItem()).startsWith("Recurring") || ((String) cboOperatesLike.getSelectedItem()).startsWith("Daily")) {
                chkIsGroupDeposit.setEnabled(true);// Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
                if (((String) cboOperatesLike.getSelectedItem()).startsWith("Recurring")) {
                     tabDepositsProduct.remove(panThriftBenevolent);// Added by nithya on 02-03-2016 for 0003897
                    if (tabDepositsProduct.getTabCount() < 6) {
                        tdtDate.setVisible(false);
                        tdtToDate.setVisible(false);
                        lblDate.setVisible(false);
                        lblToDate.setVisible(false);
                        lblFromPeriod.setVisible(false);
                        txtFromPeriod.setVisible(false);
                        cboFromPeriod.setVisible(false);
                        lblToPeriod.setVisible(false);
                        txtToPeriod.setVisible(false);
                        cboToPeriod.setVisible(false);
                        lblFromAmount.setVisible(true);
                        txtFromAmt.setVisible(true);
                        lblToAmount.setVisible(true);
                        if (CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y")) {
                            lblInstType.setVisible(true);
                            cboInstType.setVisible(true);
                            cboInstType.setEnabled(false);
                        }
                        txtToAmt.setVisible(true);
                        lblFromAmount.setText("From Amount");
                        lblToAmount.setText("To Amount");
                        lblRateInterest.setVisible(true);
                        txtCommisionRate.setVisible(true);
                        tabDepositsProduct.resetVisits();
                        tabDepositsProduct.add(panRD);
                        tabDepositsProduct.add(panFloatingRateAccount);
                        tabDepositsProduct.resetVisits();
                        tabDepositsProduct.remove(panRenewalParameter);
                        tabDepositsProduct.setTitleAt(3, "Recurring & Daily Deposit");
                        tabDepositsProduct.setTitleAt(4, "Delayed Installment Interest Rate");
                        lblRecurringDepositToFixedDeposit.setText("Convert Recurring Deposit to Fixed Deposit after Maturity Date");
                        lblInstallmentInRecurringDepositAcct.setText("Installment in Recurring Deposit Account");
                        lblDepositsFrequency.setText("Recurring Deposits Frequency");
                        panMinimumPeriod.setVisible(false);
                        panAgentCommision.setVisible(false);
                        lblAgentCommision.setVisible(false);
                        lblMinimumPeriod.setVisible(false);
                        lblInsBeyondMaturityDtYesorNo.setVisible(true);
                        panInsBeyondMaturityDtYesorNo.setVisible(true);
                        cboPeriodInMultiplesOf.setSelectedItem("Months");
                        observable.setCboPeriodInMultiplesOf("Months");
                        cboPeriodInMultiplesOf.setEnabled(false);
                        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                                || (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
                            ClientUtil.enableDisable(panRecurringDetails, true);
                        } else {
                            ClientUtil.enableDisable(panRecurringDetails, false);
                        }
                    }
                } else {
                    tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
                    if (tabDepositsProduct.getTabCount() < 6) {
                        tabDepositsProduct.add(panRD);                        
                        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW)
                                || (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)) {
                            ClientUtil.enableDisable(panRecurringDetails, true);
                        } else {
                            ClientUtil.enableDisable(panRecurringDetails, false);
                        }
                        tabDepositsProduct.add(panFloatingRateAccount);
                        tabDepositsProduct.remove(panRenewalParameter);                        
                        tabDepositsProduct.setTitleAt(3, "Recurring & Daily Deposit");
                        tabDepositsProduct.setTitleAt(4, "Agent's Commission Rate Maintenance");
                        tabDepositsProduct.resetVisits();
                        lblRecurringDepositToFixedDeposit.setText("Convert Daily Deposit to Fixed Deposit after Maturity Date");
                        lblInstallmentInRecurringDepositAcct.setText("Installment in Daily Deposit Account");
                        lblDepositsFrequency.setText("Daily Deposits Frequency");
                        panMinimumPeriod.setVisible(true);
                        panAgentCommision.setVisible(true);
                        lblAgentCommision.setVisible(true);
                        lblMinimumPeriod.setVisible(true);
                        lblDate.setVisible(true);
                        tdtDate.setVisible(true);
                        lblToDate.setVisible(true);
                        tdtToDate.setVisible(true);
                        lblFromAmount.setVisible(true);
                        txtFromAmt.setVisible(true);
                        lblToAmount.setVisible(true);
                        txtToAmt.setVisible(true);
                        lblFromPeriod.setVisible(true);
                        txtFromPeriod.setVisible(true);
                        cboFromPeriod.setVisible(true);
                        lblToPeriod.setVisible(true);
                        txtToPeriod.setVisible(true);
                        cboToPeriod.setVisible(true);
                        lblDate.setText("From Date");
                        lblToDate.setText("To Date");
                        lblFromPeriod.setText("From Period");
                        lblToPeriod.setText("To Period");
                        lblRateInterest.setText("Rate of Interest");
                        lblRateInterest.setVisible(true);
                        txtCommisionRate.setVisible(true);
                        lblInsBeyondMaturityDtYesorNo.setVisible(false);
                        panInsBeyondMaturityDtYesorNo.setVisible(false);
                        tabDepositsProduct.resetVisits();
                    }
                }
            } else {
                tabDepositsProduct.resetVisits();
                tabDepositsProduct.remove(panRD);
                tabDepositsProduct.remove(panFloatingRateAccount);
                tabDepositsProduct.resetVisits();                     
                //tabDepositsProduct.gett
            }
            //                added by nikhil for floating point
            if (((String) cboOperatesLike.getSelectedItem()).startsWith("Floating")) {
                tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
                ClientUtil.enableDisable(panWithPeriod, true);
                lblWithPeriod.setVisible(true);
                panWithPeriod.setVisible(true);
                panWithPeriod.setEnabled(true);
            } else {
                ClientUtil.enableDisable(panWithPeriod, false);
                lblWithPeriod.setVisible(false);
                panWithPeriod.setVisible(false);
                panWithPeriod.setEnabled(false);                
            }
            //             added by nikhil for doubling scheme
            if (((String) cboOperatesLike.getSelectedItem()).startsWith("Cummulative")) {
                tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
                tabDepositsProduct.add(panRenewalParameter);
                tabDepositsProduct.setTitleAt(3, "Renewal Parameters");
                ClientUtil.enableDisable(panDoublingScheme, true);
                lblDoublingScheme.setVisible(true);
                panDoublingScheme.setVisible(true);
                panDoublingScheme.setEnabled(true);
                txtMaxDepositPeriod.setEnabled(true);
                cboMaxDepositPeriod.setEnabled(true);
                lblPrematureClosureApply.setVisible(true);
                panPrematureClosureApply.setVisible(true);
                lblPrematureClosureApplyROI.setVisible(true);
                lblDifferentROI.setVisible(true);
                chkDifferentROI.setVisible(true);
                chkPrematureClosure.setVisible(true);
                lblPrematureClosure.setVisible(true);
                txtDoublingCount.setVisible(true);
                
            } else {                
                ClientUtil.enableDisable(panDoublingScheme, false);
                lblDoublingScheme.setVisible(false);
                panDoublingScheme.setVisible(false);
                panDoublingScheme.setEnabled(false);
                rdoDoublingScheme_No.setSelected(false);
                rdoDoublingScheme_Yes.setSelected(false);
                lblPrematureClosureApply.setVisible(false);
                panPrematureClosureApply.setVisible(false);
                lblPrematureClosureApplyROI.setVisible(false);
                lblDifferentROI.setVisible(false);
                chkDifferentROI.setVisible(false);
                chkPrematureClosure.setVisible(false);
                lblPrematureClosure.setVisible(false);
                txtDoublingCount.setVisible(false);
                txtDoublingCount.setText("");
            }

            if (((String) cboOperatesLike.getSelectedItem()).startsWith("Unit")) {
                tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
                ClientUtil.enableDisable(panWithdrawal, true);
            } else {                
                ClientUtil.enableDisable(panWithdrawal, false);
            }
            if (((String) cboOperatesLike.getSelectedItem()).startsWith("Floating Rate")) {
                tabDepositsProduct.add(panRenewalParameter);
                tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
                tabDepositsProduct.setTitleAt(3, "Renewal Parameters");
            }
            if (((String) cboOperatesLike.getSelectedItem()).startsWith("Fixed")) {
                tabDepositsProduct.add(panRenewalParameter);
                tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
                tabDepositsProduct.setTitleAt(3, "Renewal Parameters");
                lblDiscounted.setVisible(true);
                lblDiscounted.setEnabled(true);
                panDiscounted.setVisible(true);
                panDiscounted.setEnabled(true);
            } else {                
                lblDiscounted.setVisible(false);
                lblDiscounted.setEnabled(false);
                panDiscounted.setVisible(false);
                panDiscounted.setEnabled(false);
            }
        }

        if (cboOperatesLike.getSelectedItem().equals("Fixed")) {
            chkExcludeLienFrmIntrstAppl.setEnabled(true);
            chkExcludeLienFrmStanding.setEnabled(true);
            tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
        } else {
            chkExcludeLienFrmIntrstAppl.setEnabled(false);
            chkExcludeLienFrmStanding.setEnabled(false);           
        }        
        
        if (cboOperatesLike.getSelectedItem().equals("Cummulative")) {
            chkCummCertPrint.setEnabled(true);
            tabDepositsProduct.remove(panThriftBenevolent); // Added by nithya on 02-03-2016 for 0003897
            // chkCummCertPrint.setEnabled(true);
        } else {
            chkCummCertPrint.setEnabled(false);            
            // chkExcludeLienFrmStanding.setEnabled(false);
        }
        // Added by nithya on 02-03-2016 for 0003897
        if(cboOperatesLike.getSelectedItem().equals("Thrift") || cboOperatesLike.getSelectedItem().equals("Benevolent") ){
           tabDepositsProduct.add(panThriftBenevolent);  
           txtInstallmentAmount.setEnabled(true);
           tdtEffectiveDate.setEnabled(true);
           tabDepositsProduct.setTitleAt(tabDepositsProduct.getTabCount()-1,"Thrift/Benevolent");           
        }else{
            tabDepositsProduct.remove(panThriftBenevolent);
        }
        // End
        if(cboOperatesLike.getSelectedItem().equals("Recurring")){ // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
            tabDepositsProduct.add(panRDClosingROISetting); 
            tabDepositsProduct.setTitleAt(tabDepositsProduct.getTabCount()-1,"Irregular RD Closing ROI Settings"); 
            rdoIncaseOfIrregularRDSBRate.setEnabled(false);
            rdoIncaseOfIrregularRDRBRate.setEnabled(false);
        }else{
            tabDepositsProduct.remove(panRDClosingROISetting);
        }
        
        if(cboOperatesLike.getSelectedItem().equals("Daily")){ //16-06-2020
            tabDepositsProduct.add(panAgenCommissionSlab); 
            tabDepositsProduct.setTitleAt(tabDepositsProduct.getTabCount()-1,"Agent Commission Disbursal Slab"); 
        }else{
            tabDepositsProduct.remove(panAgenCommissionSlab);
        }
        
    }//GEN-LAST:event_cboOperatesLikeActionPerformed

    private void rdoCalcTDS_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCalcTDS_YesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCalcTDS_YesActionPerformed

    private void rdoAmountRounded_YesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoAmountRounded_YesItemStateChanged
        // Add your handling code here:
        cboRoundOffCriteria.setEnabled(true);
    }//GEN-LAST:event_rdoAmountRounded_YesItemStateChanged

    private void rdoAmountRounded_NoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoAmountRounded_NoItemStateChanged
        // Add your handling code here:
        if (rdoAmountRounded_No.isSelected()) {
            cboRoundOffCriteria.setSelectedItem("");
            cboRoundOffCriteria.setEnabled(false);
            cboRoundOffCriteria.setEditable(false);
        }
    }//GEN-LAST:event_rdoAmountRounded_NoItemStateChanged

    private void rdoInterestAfterMaturity_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestAfterMaturity_YesActionPerformed
        txtInterestOnMaturedDeposits.setEnabled(true);
        txtInterestOnMaturedDeposits.setEditable(true);

        txtAfterHowManyDays.setEnabled(true);
        txtAfterHowManyDays.setEditable(true);

        cboMaturityInterestType.setEnabled(true);
        cboMaturityInterestRate.setEnabled(true);

        btnIntOnMaturedDepositAcctHead.setEnabled(true);
        btnIntProvisionOfMaturedDeposit.setEnabled(true);
    }//GEN-LAST:event_rdoInterestAfterMaturity_YesActionPerformed

    private void rdoInterestAfterMaturity_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestAfterMaturity_NoActionPerformed
        // Add your handling code here:
        if (rdoInterestAfterMaturity_No.isSelected()) {
            txtInterestOnMaturedDeposits.setText("");
            txtInterestOnMaturedDeposits.setEnabled(false);
            txtInterestOnMaturedDeposits.setEditable(false);

            txtAfterHowManyDays.setText("");
            txtAfterHowManyDays.setEnabled(false);
            txtAfterHowManyDays.setEditable(false);

            cboMaturityInterestType.setSelectedItem("");
            cboMaturityInterestType.setEnabled(false);
            cboMaturityInterestType.setEditable(false);

            cboMaturityInterestRate.setSelectedItem("");
            cboMaturityInterestRate.setEnabled(false);
            cboMaturityInterestRate.setEditable(false);

            txtIntOnMaturedDepositAcctHead.setText("");
            txtIntOnMaturedDepositAcctHead.setEnabled(false);
            btnIntOnMaturedDepositAcctHead.setEnabled(false);

            txtIntProvisionOfMaturedDeposit.setText("");
            txtIntProvisionOfMaturedDeposit.setEnabled(false);
            btnIntProvisionOfMaturedDeposit.setEnabled(false);
        }
    }//GEN-LAST:event_rdoInterestAfterMaturity_NoActionPerformed

    private void cboMaxDepositPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMaxDepositPeriodActionPerformed
        // TODO add your handling code here:
        String message = cboMaxDepositPeriodRule();
        if (!message.equals("") && message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_cboMaxDepositPeriodActionPerformed

    private void txtMinDepositPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMinDepositPeriodActionPerformed
        // TODO add your handling code here:
        //             added by nikhil for doubling scheme
        if (((String) cboOperatesLike.getSelectedItem()).startsWith("Cummulative") && rdoDoublingScheme_Yes.isSelected()) {
            txtMaxDepositPeriod.setText(txtMinDepositPeriod.getText());
            txtMaxDepositPeriod.setEnabled(false);
        }
    }//GEN-LAST:event_txtMinDepositPeriodActionPerformed

    private void cboMinDepositPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboMinDepositPeriodActionPerformed
        // TODO add your handling code here:
        if (((String) cboOperatesLike.getSelectedItem()).startsWith("Cummulative") && rdoDoublingScheme_Yes.isSelected()) {
            cboMaxDepositPeriod.setSelectedItem(cboMinDepositPeriod.getSelectedItem());
            //            cboMaxDepositPeriod.setSelectedItem(observable.getCbmMinimumPeriod().getKeyForSelected());
            cboMaxDepositPeriod.setEnabled(false);
            txtMaxDepositPeriod.setText(txtMinDepositPeriod.getText());
            txtMaxDepositPeriod.setEnabled(false);
            txtPeriodInMultiplesOf.setEnabled(false);
            cboPeriodInMultiplesOf.setEnabled(false);
        }
    }//GEN-LAST:event_cboMinDepositPeriodActionPerformed

    private void txtMaxDepositAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDepositAmtFocusLost
        // TODO add your handling code here:
        String message = txtMaxDepositAmtRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxDepositAmtFocusLost

    private void txtAcctHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctHdFocusLost
        // TODO add your handling code here:
        if (!(txtAcctHd.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAcctHd, "product.deposits.getAcctHeadList");
        }
    }//GEN-LAST:event_txtAcctHdFocusLost

    private void btnAcctHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctHeadActionPerformed
        // Add your handling code here:
        popUp(DEPACCHD);
    }//GEN-LAST:event_btnAcctHeadActionPerformed

    private void rdoDiscounted_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscounted_YesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDiscounted_YesActionPerformed

    private void rdoWithPeriod_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWithPeriod_YesActionPerformed
        // TODO add your handling code here:
        if (rdoWithPeriod_Yes.isSelected()) {
            txtMinDepositPeriod.setText("");
            cboMinDepositPeriod.setSelectedItem("");
            txtMaxDepositPeriod.setText("");
            cboMaxDepositPeriod.setSelectedItem("");
            enableDisablePeriod(true);
        }
    }//GEN-LAST:event_rdoWithPeriod_YesActionPerformed

    private void rdoWithPeriod_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWithPeriod_NoActionPerformed
        // TODO add your handling code here:
        if (rdoWithPeriod_No.isSelected()) {
            txtMinDepositPeriod.setText("1");
            cboMinDepositPeriod.setSelectedItem("Days");
            txtMaxDepositPeriod.setText("999");
            cboMaxDepositPeriod.setSelectedItem("Years");
        } else {
            txtMinDepositPeriod.setText("");
            cboMinDepositPeriod.setSelectedItem("");
            txtMaxDepositPeriod.setText("");
            cboMaxDepositPeriod.setSelectedItem("");
        }
        enableDisablePeriod(false);
    }//GEN-LAST:event_rdoWithPeriod_NoActionPerformed

    private void rdoDoublingScheme_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDoublingScheme_YesActionPerformed
        // TODO add your handling code here:
        if (rdoDoublingScheme_Yes.isSelected()) {
            txtPeriodInMultiplesOf.setText("1");
            cboPeriodInMultiplesOf.setSelectedItem("Days");
            txtMaxDepositPeriod.setEnabled(false);
            cboMaxDepositPeriod.setEnabled(false);
            txtPeriodInMultiplesOf.setEnabled(false);
            cboPeriodInMultiplesOf.setEnabled(false);
            txtDoublingCount.setVisible(true);
        } else {
            txtMinDepositPeriod.setText("");
            cboMinDepositPeriod.setSelectedItem("");
            txtMaxDepositPeriod.setText("");
            cboMaxDepositPeriod.setSelectedItem("");
            txtPeriodInMultiplesOf.setText("");
            cboPeriodInMultiplesOf.setSelectedItem("");
            txtMaxDepositPeriod.setEnabled(true);
            cboMaxDepositPeriod.setEnabled(true);
            txtPeriodInMultiplesOf.setEnabled(true);
            cboPeriodInMultiplesOf.setEnabled(true);
            txtDoublingCount.setVisible(false);
            txtDoublingCount.setText("");
        }
    }//GEN-LAST:event_rdoDoublingScheme_YesActionPerformed

    private void rdoDoublingScheme_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDoublingScheme_NoActionPerformed
        // TODO add your handling code here:
        if (rdoDoublingScheme_No.isSelected()) {
            txtMinDepositPeriod.setText("");
            cboMinDepositPeriod.setSelectedItem("");
            txtMaxDepositPeriod.setText("");
            cboMaxDepositPeriod.setSelectedItem("");
            txtMaxDepositPeriod.setEnabled(true);
            cboMaxDepositPeriod.setEnabled(true);
            txtPeriodInMultiplesOf.setEnabled(true);
            cboPeriodInMultiplesOf.setEnabled(true);
            txtDoublingCount.setVisible(false);
            txtDoublingCount.setText("");
        }
    }//GEN-LAST:event_rdoDoublingScheme_NoActionPerformed

    private void rdoTransferToMaturedDeposits_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransferToMaturedDeposits_YesActionPerformed
        // TODO add your handling code here:
        txtAfterNoDays.setEnabled(true);
        txtAfterNoDays.setEditable(true);

        btnMaturityDepositAcctHead.setEnabled(true);
    }//GEN-LAST:event_rdoTransferToMaturedDeposits_YesActionPerformed

    private void rdoTransferToMaturedDeposits_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTransferToMaturedDeposits_NoActionPerformed
        // TODO add your handling code here:
        if (rdoTransferToMaturedDeposits_No.isSelected()) {
            txtAfterNoDays.setText("");
            txtAfterNoDays.setEnabled(false);
            txtAfterNoDays.setEditable(false);

            txttMaturityDepositAcctHead.setText("");
            txttMaturityDepositAcctHead.setEnabled(false);

            btnMaturityDepositAcctHead.setEnabled(false);
        }
    }//GEN-LAST:event_rdoTransferToMaturedDeposits_NoActionPerformed

    private void rdoProvisionOfInterest_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoProvisionOfInterest_YesActionPerformed
        // Add your handling code here:
        txtAdvanceMaturityNoticeGenPeriod.setEnabled(true);
        txtAdvanceMaturityNoticeGenPeriod.setEditable(true);
    }//GEN-LAST:event_rdoProvisionOfInterest_YesActionPerformed

    private void rdoProvisionOfInterest_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoProvisionOfInterest_NoActionPerformed
        // Add your handling code here:
        if (rdoProvisionOfInterest_No.isSelected()) {
            txtAdvanceMaturityNoticeGenPeriod.setText("");
            txtAdvanceMaturityNoticeGenPeriod.setEnabled(false);
            txtAdvanceMaturityNoticeGenPeriod.setEditable(false);
        }
    }//GEN-LAST:event_rdoProvisionOfInterest_NoActionPerformed

    private void txtMaxAmtOfPartialWithdrawalAllowedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxAmtOfPartialWithdrawalAllowedFocusLost
        // TODO add your handling code here:
        String message = txtMaxAmtOfPartialWithdrawalAllowedRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxAmtOfPartialWithdrawalAllowedFocusLost

    private void txtMaxNoOfPartialWithdrawalAllowedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxNoOfPartialWithdrawalAllowedFocusLost
        // TODO add your handling code here:
        String message = txtMaxNoOfPartialWithdrawalAllowedRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMaxNoOfPartialWithdrawalAllowedFocusLost

    private void txtMinAmtOfPartialWithdrawalAllowedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinAmtOfPartialWithdrawalAllowedFocusLost
        // TODO add your handling code here:
        String message = txtMinAmtOfPartialWithdrawalAllowedRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtMinAmtOfPartialWithdrawalAllowedFocusLost

    private void txtWithdrawalsInMultiplesOfFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtWithdrawalsInMultiplesOfFocusLost
        // TODO add your handling code here:
        String message = txtWithdrawalsInMultiplesOfRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtWithdrawalsInMultiplesOfFocusLost

    private void rdoPartialWithdrawalAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPartialWithdrawalAllowed_YesActionPerformed
        // Add your handling code here:
        //        txtNoOfPartialWithdrawalAllowed.setEnabled(true);
        //        txtNoOfPartialWithdrawalAllowed.setEditable(true);
        //
        //        txtMaxNoOfPartialWithdrawalAllowed.setEnabled(true);
        //        txtMaxNoOfPartialWithdrawalAllowed.setEditable(true);
        //
        //        txtMaxAmtOfPartialWithdrawalAllowed.setEnabled(true);
        //        txtMaxAmtOfPartialWithdrawalAllowed.setEditable(true);
        //
        //        txtMinAmtOfPartialWithdrawalAllowed.setEnabled(true);
        //        txtMinAmtOfPartialWithdrawalAllowed.setEditable(true);
        //
        //        txtWithdrawalsInMultiplesOf.setEnabled(true);
        //        txtWithdrawalsInMultiplesOf.setEditable(true);
        ClientUtil.clearAll(panWithdrawal);
        ClientUtil.enableDisable(panWithdrawal, true);

        btnIntDebitPLHead.setEnabled(true);
    }//GEN-LAST:event_rdoPartialWithdrawalAllowed_YesActionPerformed

    private void rdoPartialWithdrawalAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPartialWithdrawalAllowed_NoActionPerformed
        // Add your handling code here:
        if (rdoPartialWithdrawalAllowed_No.isSelected()) {
            //
            //            txtNoOfPartialWithdrawalAllowed.setText("");
            //            txtNoOfPartialWithdrawalAllowed.setEnabled(false);
            //            txtNoOfPartialWithdrawalAllowed.setEditable(false);
            //
            //            txtMaxNoOfPartialWithdrawalAllowed.setText("");
            //            txtMaxNoOfPartialWithdrawalAllowed.setEnabled(false);
            //            txtMaxNoOfPartialWithdrawalAllowed.setEditable(false);
            //
            //            txtMaxAmtOfPartialWithdrawalAllowed.setText("");
            //            txtMaxAmtOfPartialWithdrawalAllowed.setEnabled(false);
            //            txtMaxAmtOfPartialWithdrawalAllowed.setEditable(false);
            //
            //            txtMinAmtOfPartialWithdrawalAllowed.setText("");
            //            txtMinAmtOfPartialWithdrawalAllowed.setEnabled(false);
            //            txtMinAmtOfPartialWithdrawalAllowed.setEditable(false);
            //
            //            txtWithdrawalsInMultiplesOf.setText("");
            //            txtWithdrawalsInMultiplesOf.setEnabled(false);
            //            txtWithdrawalsInMultiplesOf.setEditable(false);


            ClientUtil.clearAll(panWithdrawal);
            ClientUtil.enableDisable(panWithdrawal, false);
            rdoPartialWithdrawalAllowed_No.setSelected(true);
            rdoPartialWithdrawalAllowed_No.setEnabled(true);
            rdoPartialWithdrawalAllowed_Yes.setEnabled(true);

            txtIntDebitPLHead.setText("");
            txtIntDebitPLHead.setEnabled(false);
            btnIntDebitPLHead.setEnabled(false);
            ClientUtil.enableDisable(panServiceCharge, true);
        }
    }//GEN-LAST:event_rdoPartialWithdrawalAllowed_NoActionPerformed

    private void rdoServiceCharge_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoServiceCharge_YesActionPerformed
        // TODO add your handling code here:
        if (rdoServiceCharge_Yes.isSelected() == true) {
            txtServiceCharge.setEnabled(true);
        } else {
            txtServiceCharge.setEnabled(false);
        }
    }//GEN-LAST:event_rdoServiceCharge_YesActionPerformed

    private void rdoServiceCharge_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoServiceCharge_NoActionPerformed
        // TODO add your handling code here:
        if (rdoServiceCharge_No.isSelected() == true) {
            txtServiceCharge.setEnabled(false);
            txtServiceCharge.setText("");
        }
    }//GEN-LAST:event_rdoServiceCharge_NoActionPerformed

    private void txtTDSGLAcctHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTDSGLAcctHdFocusLost
        // TODO add your handling code here:
        if (!(txtTDSGLAcctHd.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtTDSGLAcctHd, "product.deposits.getAcctHeadList");
            txtTDSGLAcctHdDesc.setText(getAccHeadDesc(txtTDSGLAcctHd.getText()));
        }
    }//GEN-LAST:event_txtTDSGLAcctHdFocusLost

    private void btnTDSGLAcctHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTDSGLAcctHdActionPerformed
        // TODO add your handling code here:
        popUp(TDSGLACCTHD);
    }//GEN-LAST:event_btnTDSGLAcctHdActionPerformed

    /*8 private void rdoAutoRenewalAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {
    // Add your handling code here:
    txtMaxNopfTimes.setEnabled(true);
    txtMaxNopfTimes.setEditable(true);
    }*/
    /**  private void rdoAutoRenewalAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {
     * // Add your handling code here:
     * if(rdoAutoRenewalAllowed_No.isSelected()) {
     * txtMaxNopfTimes.setText("");
     * txtMaxNopfTimes.setEnabled(false);
     * txtMaxNopfTimes.setEditable(false);
     * }
     * }*/
    /** private void rdoRenewalOfDepositAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {
     * // Add your handling code here:
     * txtMinNoOfDays.setEnabled(true);
     * txtMinNoOfDays.setEditable(true);
     * cboMinNoOfDays.setEnabled(true);
     *
     * txtIntPeriodForBackDatedRenewal.setEnabled(true);
     * txtIntPeriodForBackDatedRenewal.setEditable(true);
     * cboIntPeriodForBackDatedRenewal.setEnabled(true);
     *
     * cboIntType.setEnabled(true);
     *
     * txtMaxPeriodMDt.setEnabled(true);
     * txtMaxPeriodMDt.setEditable(true);
     * cboMaxPeriodMDt.setEnabled(true);
     * }*/
    /** private void rdoRenewalOfDepositAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {
     * // Add your handling code here:
     * if(rdoRenewalOfDepositAllowed_No.isSelected()) {
     * txtMinNoOfDays.setText("");
     * txtMinNoOfDays.setEnabled(false);
     * txtMinNoOfDays.setEditable(false);
     *
     * cboMinNoOfDays.setSelectedItem("");
     * cboMinNoOfDays.setEnabled(false);
     * cboMinNoOfDays.setEditable(false);
     *
     * txtIntPeriodForBackDatedRenewal.setText("");
     * txtIntPeriodForBackDatedRenewal.setEnabled(false);
     * txtIntPeriodForBackDatedRenewal.setEditable(false);
     *
     * cboIntPeriodForBackDatedRenewal.setSelectedItem("");
     * cboIntPeriodForBackDatedRenewal.setEnabled(false);
     * cboIntPeriodForBackDatedRenewal.setEditable(false);
     *
     * cboIntType.setSelectedItem("");
     * cboIntType.setEnabled(false);
     * cboIntType.setEditable(false);
     *
     *
     * txtMaxPeriodMDt.setText("");
     * txtMaxPeriodMDt.setEnabled(false);
     * txtMaxPeriodMDt.setEditable(false);
     *
     * cboMaxPeriodMDt.setSelectedItem("");
     * cboMaxPeriodMDt.setEnabled(false);
     * cboMaxPeriodMDt.setEditable(false);
     * }
     * }*/
    /** private void rdoSameNoRenewalAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {
     * // TODO add your handling code here:
     * lblMaxNopfTimes1.setVisible(true);
     * txtMaxNopfTimes1.setVisible(true);
     * lblMaxNopfTimes1.setEnabled(true);
     * txtMaxNopfTimes1.setEditable(true);
     * txtMaxNopfTimes1.setEnabled(true);
     * }*/
    /** private void rdoSameNoRenewalAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {
     * // TODO add your handling code here:
     * lblMaxNopfTimes1.setVisible(false);
     * txtMaxNopfTimes1.setVisible(false);
     * lblMaxNopfTimes1.setEnabled(false);
     * txtMaxNopfTimes1.setEditable(false);
     * txtMaxNopfTimes1.setEnabled(false);
     * txtMaxNopfTimes1.setText("");
     * }*/
    private void rdoCanReceiveExcessInstal_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCanReceiveExcessInstal_yesActionPerformed
        // TODO add your handling code here:
        rdgIntPayableOnExcessInstal.remove(rdoIntPayableOnExcessInstal_Yes);
        rdgIntPayableOnExcessInstal.remove(rdoIntPayableOnExcessInstal_No);

        rdoIntPayableOnExcessInstal_Yes.setEnabled(true);
        rdoIntPayableOnExcessInstal_Yes.setSelected(false);
        rdoIntPayableOnExcessInstal_No.setEnabled(true);
        rdoIntPayableOnExcessInstal_No.setSelected(false);

        rdgIntPayableOnExcessInstal = new CButtonGroup();
        rdgIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_Yes);
        rdgIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_No);
    }//GEN-LAST:event_rdoCanReceiveExcessInstal_yesActionPerformed

    private void rdoCanReceiveExcessInstal_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCanReceiveExcessInstal_NoActionPerformed
        // TODO add your handling code here:
        if (rdoCanReceiveExcessInstal_No.isSelected()) {
            rdoIntPayableOnExcessInstal_Yes.setEnabled(false);
            rdoIntPayableOnExcessInstal_Yes.setSelected(false);
            rdoIntPayableOnExcessInstal_No.setEnabled(false);
            rdoIntPayableOnExcessInstal_No.setSelected(true);
        }
    }//GEN-LAST:event_rdoCanReceiveExcessInstal_NoActionPerformed

    private void rdoInstallmentInRecurringDepositAcct_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInstallmentInRecurringDepositAcct_YesActionPerformed
        // TODO add your handling code here:
        if (rdoInstallmentInRecurringDepositAcct_Yes.isSelected()) {
            cboInstallmentToBeCharged.setSelectedItem("");
            cboInstallmentToBeCharged.setEnabled(false);
            cboInstallmentToBeCharged.setEditable(false);

            cboChangeValue.setSelectedItem("");
            cboChangeValue.setEnabled(false);
            cboChangeValue.setEditable(false);
        }
    }//GEN-LAST:event_rdoInstallmentInRecurringDepositAcct_YesActionPerformed

    private void rdoInstallmentInRecurringDepositAcct_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInstallmentInRecurringDepositAcct_NoActionPerformed
        // TODO add your handling code here:
        cboInstallmentToBeCharged.setEnabled(true);
        cboChangeValue.setEnabled(true);
    }//GEN-LAST:event_rdoInstallmentInRecurringDepositAcct_NoActionPerformed

    private void rdoLastInstallmentAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLastInstallmentAllowed_YesActionPerformed
        // TODO add your handling code here:
        rdgPenaltyOnLateInstallmentsChargeble.remove(rdoPenaltyOnLateInstallmentsChargeble_No);
        rdgPenaltyOnLateInstallmentsChargeble.remove(rdoPenaltyOnLateInstallmentsChargeble_Yes);

        rdoPenaltyOnLateInstallmentsChargeble_Yes.setEnabled(true);
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setSelected(false);
        rdoPenaltyOnLateInstallmentsChargeble_No.setEnabled(true);
        rdoPenaltyOnLateInstallmentsChargeble_No.setSelected(false);

        rdgPenaltyOnLateInstallmentsChargeble = new CButtonGroup();
        rdgPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_Yes);
        rdgPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_No);
    }//GEN-LAST:event_rdoLastInstallmentAllowed_YesActionPerformed

    private void rdoLastInstallmentAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLastInstallmentAllowed_NoActionPerformed
        // TODO add your handling code here:
        if (rdoLastInstallmentAllowed_No.isSelected()) {
            rdoPenaltyOnLateInstallmentsChargeble_Yes.setEnabled(false);
            rdoPenaltyOnLateInstallmentsChargeble_Yes.setSelected(false);
            rdoPenaltyOnLateInstallmentsChargeble_No.setEnabled(false);
            rdoPenaltyOnLateInstallmentsChargeble_No.setSelected(true);
        }
    }//GEN-LAST:event_rdoLastInstallmentAllowed_NoActionPerformed

    private void cboCutOffDayForPaymentOfInstalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCutOffDayForPaymentOfInstalActionPerformed
        // TODO add your handling code here:
        observable.setCboCutOffDayForPaymentOfInstal((String) cboCutOffDayForPaymentOfInstal.getSelectedItem());
        if (observable.getCboCutOffDayForPaymentOfInstal().length() > 0) {
            if (((String) (((ComboBoxModel) (cboCutOffDayForPaymentOfInstal).getModel())).getKeyForSelected()).equalsIgnoreCase("CHOSEN_DATE")) {
                txtCutOffDayForPaymentOfInstal.setEnabled(true);
                txtCutOffDayForPaymentOfInstal.setEditable(true);
            } else {
                txtCutOffDayForPaymentOfInstal.setText("");
                txtCutOffDayForPaymentOfInstal.setEnabled(false);
                txtCutOffDayForPaymentOfInstal.setEditable(false);
            }
        }
    }//GEN-LAST:event_cboCutOffDayForPaymentOfInstalActionPerformed

    private void txtCutOffDayForPaymentOfInstalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCutOffDayForPaymentOfInstalFocusLost
        // TODO add your handling code here:
        String message = txtCutOffDayForPaymentOfInstalRule();
        if (message.length() > 0) {
            displayAlert(message);
        }
    }//GEN-LAST:event_txtCutOffDayForPaymentOfInstalFocusLost

    private void rdoPenaltyOnLateInstallmentsChargeble_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenaltyOnLateInstallmentsChargeble_YesActionPerformed
        // TODO add your handling code here:
        //        btnDelayedNew.setEnabled(true);
        //        lblDelayedChargesDet.setVisible(true);
        //        srpDelayedInstallmet.setVisible(true);
        tabDepositsProduct.add(panFloatingRateAccount);
        tabDepositsProduct.setTitleAt(4, "Delayed Installment Interest Rate");
        tabDepositsProduct.setVisible(true);
        tabDepositsProduct.resetVisits();
        lblDelayedChargesDet.setVisible(true);
        lblRDIrregularIfInstallmentDue.setVisible(true);
        txtRDIrregularIfInstallmentDue.setVisible(true);

        //        txtFromAmt.setEnabled(true);
        //        txtToAmt.setEnabled(true);
        //        txtCommisionRate.setEnabled(true);
        btnTabNew.setEnabled(true);
        //added by Shany
        lblGracePeriod.setVisible(true);
        txtGracePeriod.setVisible(true);
        txtGracePeriod.setEnabled(true);
    }//GEN-LAST:event_rdoPenaltyOnLateInstallmentsChargeble_YesActionPerformed

    private void rdoPenaltyOnLateInstallmentsChargeble_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenaltyOnLateInstallmentsChargeble_NoActionPerformed
        // TODO add your handling code here:
        //        txtFromAmt.setEnabled(false);
        //        txtToAmt.setEnabled(false);
        //        txtDelayedChargesAmt.setEnabled(false);
        //        tabDepositsProduct.remove(panFloatingRateAccount);

        //        txtFromAmt.setEditable(false);
        //        txtToAmt.setEditable(false);
        //        txtDelayedChargesAmt.setEditable(false);

        //        txtFromAmt.setText("");
        //        txtToAmt.setText("");
        //        txtDelayedChargesAmt.setText("");

        btnDelayedNew.setEnabled(false);
        btnDelayedSave.setEnabled(false);
        btnDelayedDel.setEnabled(false);
        lblDelayedChargesDet.setVisible(false);
        srpDelayedInstallmet.setVisible(false);
        lblRDIrregularIfInstallmentDue.setVisible(false);
        txtRDIrregularIfInstallmentDue.setVisible(false);
        txtRDIrregularIfInstallmentDue.setText("");
        //        observable.resetTableRecords();
         //added by Shany
        lblGracePeriod.setVisible(false);
        txtGracePeriod.setVisible(false);
        txtGracePeriod.setEnabled(false);
    }//GEN-LAST:event_rdoPenaltyOnLateInstallmentsChargeble_NoActionPerformed

    private void rdoDailyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDailyActionPerformed
        // TODO add your handling code here:
        lblWeekly.setVisible(false);
        cboWeekly.setVisible(false);
        cboMonthlyIntCalcMethod.setVisible(false);
        cboMonthlyIntCalcMethod.setSelectedItem("");
        //        cboMonthlyIntCalcMethod.setSelectedItem("");
        //        cboWeekly.setSelectedItem("");
        panPartialWithdrawlAllowedForDD.setVisible(true);
        lblPartialWithdrawlAllowedForDD.setVisible(true);
        //        cboWeekly.setSelectedItem("");
    }//GEN-LAST:event_rdoDailyActionPerformed

    private void rdoDailyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoDailyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDailyFocusLost

    private void rdoWeeklyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWeeklyActionPerformed
        // TODO add your handling code here:
        lblWeekly.setVisible(true);
        cboWeekly.setVisible(true);
        cboMonthlyIntCalcMethod.setVisible(false);
        cboMonthlyIntCalcMethod.setSelectedItem("");
        //        cboMonthlyIntCalcMethod.setSelectedItem("");
        panPartialWithdrawlAllowedForDD.setVisible(true);
        lblPartialWithdrawlAllowedForDD.setVisible(true);
    }//GEN-LAST:event_rdoWeeklyActionPerformed

    private void rdoWeeklyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoWeeklyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoWeeklyFocusLost

    private void rdoMonthlyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMonthlyActionPerformed
        // TODO add your handling code here:
        lblWeekly.setVisible(false);
        cboWeekly.setVisible(false);
        cboMonthlyIntCalcMethod.setVisible(true);
        //        cboMonthlyIntCalcMethod.setSelectedItem("");
        //        cboWeekly.setSelectedItem("");
        panPartialWithdrawlAllowedForDD.setVisible(true);
        lblPartialWithdrawlAllowedForDD.setVisible(true);
        //        cboWeekly.setSelectedItem("");
    }//GEN-LAST:event_rdoMonthlyActionPerformed

    private void rdoMonthlyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoMonthlyFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMonthlyFocusLost

    private void btnDelayedNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelayedNewActionPerformed
        //        // TODO add your handling code here:
        //        updateOBFields();
        //        if(rdoPenaltyOnLateInstallmentsChargeble_Yes.isSelected() == true){
        //            txtFromAmt.setEditable(true);
        //            txtToAmt.setEditable(true);
        //            txtDelayedChargesAmt.setEditable(true);
        //            txtFromAmt.setEnabled(true);
        //            txtToAmt.setEnabled(true);
        //            txtDelayedChargesAmt.setEnabled(true);
        //            btnDelayedSave.setEnabled(true);
        //            btnDelayedDel.setEnabled(true);
        //            btnDelayedNew.setEnabled(false);
        //            observable.resetDelayed();
        //        }else
        //            ClientUtil.displayAlert("Choose Penalty on Late Installments Chargeble...!");
    }//GEN-LAST:event_btnDelayedNewActionPerformed

    private void btnDelayedSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelayedSaveActionPerformed
        //        // TODO add your handling code here:
        //        updateOBFields();
        //        if(txtFromAmt.getText().equals("")){
        //            ClientUtil.displayAlert("From Amount Should not be empty....");
        //        }else if(txtToAmt.getText().equals("")){
        //            ClientUtil.displayAlert("To Amount Should not be empty....");
        //        }else if(txtDelayedChargesAmt.getText().equals("")){
        //            ClientUtil.displayAlert("Penalty Amount Should not be empty....");
        //        }else{
        //            observable.delayedInstallment();
        //            btnDelayedNew.setEnabled(true);
        //            btnDelayedSave.setEnabled(false);
        //            btnDelayedDel.setEnabled(false);
        //            observable.resetDelayed();
        //        }
    }//GEN-LAST:event_btnDelayedSaveActionPerformed

    private void btnDelayedDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelayedDelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDelayedDelActionPerformed

    private void tblDelayedInstallmetMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDelayedInstallmetMousePressed
        // TODO add your handling code here:
        selectedDelayedRow = true;
        //        observable.depSubNoStatus = CommonConstants.STATUS_MODIFIED;
        int rowcount = (int) (Integer.parseInt(CommonUtil.convertObjToStr(tblDelayedInstallmet.getValueAt(tblDelayedInstallmet.getSelectedRow(), 0))));

        tblDepSubNoRowSelected((rowcount - 1));
        selectedDelayedRow = false;
    }//GEN-LAST:event_tblDelayedInstallmetMousePressed

    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // Add your handling code here:
        //        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panDepositsProduct);
        //        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0){
        //            displayAlert(resourceBundle.getString(mandatoryMessage));
        //        }else{
        updateOBFields();
        observable.resetTable();
        observable.ttNotifyObservers();
        ClientUtil.enableDisable(panAgentsCalculations, true);
        tabNewButtons();
        updateTab = -1;
        rowSelected = -1;
        rowCount = tblInterestTable.getRowCount() + 1;
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(true);
        btnTabDelete.setEnabled(true);
        txtFromAmt.setEnabled(true);
        cboInstType.setEnabled(true);
        txtToAmt.setEnabled(true);
        txtCommisionRate.setEnabled(true);
        tabDepositsProduct.setSelectedComponent(panFloatingRateAccount);

        //        observable.newRecEntering = true;
        //            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
        //            //__  To put the default values into Amount and period range...
        //            if(PRODTYPE.equalsIgnoreCase(OPERAIVE)){
        //                cboFromAmount.setSelectedIndex(1);
        //                cboToAmount.setSelectedIndex(cboToAmount.getItemCount() - 1);
        //
        //                txtFromPeriod.setText("1");
        //                cboFromPeriod.setSelectedItem(((ComboBoxModel) cboFromPeriod.getModel()).getDataForKey("DAYS"));
        //
        //                txtToPeriod.setText("99");
        //                cboToPeriod.setSelectedItem(((ComboBoxModel) cboToPeriod.getModel()).getDataForKey("YEARS"));
        //            }
        //        }       // TODO add your handling code here:
    }//GEN-LAST:event_btnTabNewActionPerformed

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        int result = 0;
        updateOBFields();

        //        final String FROMAMT = CommonUtil.convertObjToStr(((ComboBoxModel)(cboFromAmount.getModel())).getKeyForSelected());
        //        final String TOAMT = CommonUtil.convertObjToStr(((ComboBoxModel)(cboToAmount.getModel())).getKeyForSelected());
        final String FROMAMT = txtFromAmt.getText();
        final String TOAMT = txtToAmt.getText();
        try {
            String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAgentsCalculations);
            StringBuffer strBAlert = new StringBuffer();
            int count = tblInterestTable.getRowCount();
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
                strBAlert.append(mandatoryMessage + "\n");
            }
            if (CommonUtil.convertObjToDouble(TOAMT).doubleValue() < CommonUtil.convertObjToDouble(FROMAMT).doubleValue()) {
                strBAlert.append(resourceBundle.getString("AMOUNT_WARNING") + "\n");
            }
            if (observable.period() == 1) {
                strBAlert.append(resourceBundle.getString("RANGE_WARNING") + "\n");
            }
            if ((CommonUtil.convertObjToDouble(txtCommisionRate.getText()).doubleValue() > 100)) {
                strBAlert.append(resourceBundle.getString("RATE_WARNING") + "\n");
            }
            if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0) {
                displayAlert(strBAlert.toString());
            }
            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel) (cboOperatesLike).getModel())).getKeyForSelected());

            if (txtFromAmt.getText().equals("") && PRODTYPE.equals("RECURRING")) {
                ClientUtil.displayAlert("From Amount Should not be empty....");
            }
            if (txtToAmt.getText().equals("") && PRODTYPE.equals("RECURRING")) {
                ClientUtil.displayAlert("To Amount Should not be empty....");
            }
            if (txtCommisionRate.getText().equals("") && PRODTYPE.equals("RECURRING")) {
                ClientUtil.displayAlert("Penalty Amount Should not be empty....");
            } else {
                //            final String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel)(cboProdType).getModel())).getKeyForSelected());
                result = observable.addTabData(updateTab, rowSelected);

                if (result == 1) {
                    ClientUtil.enableDisable(panAgentsCalculations, true);
                    tabNewButtons();
                } else {
                    ClientUtil.enableDisable(panAgentsCalculations, false);
                    tabSaveDeleteButtons();
                    observable.resetTable();
                }
                observable.ttNotifyObservers();
                updateTab = -1;
                rowSelected = -1;
                btnTabNew.setEnabled(true);
                btnTabSave.setEnabled(false);
                btnTabDelete.setEnabled(false);
                txtFromAmt.setText("");
                txtToAmt.setText("");
            }
        } catch (Exception e) {
            System.out.println("Error in btnTabSaveActionPerformed");
            e.printStackTrace();
        }
        tabDepositsProduct.setSelectedComponent(panFloatingRateAccount);

        //        }       // TODO add your handling code here:
    }//GEN-LAST:event_btnTabSaveActionPerformed

    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // Add your handling code here:
        if (observable.authStatus == true) {
            ClientUtil.showAlertWindow("can not delete this record bcz already authorized...");
            return;
        } else {
            updateOBFields();
            observable.deleteInterestTab(rowSelected);

            ClientUtil.enableDisable(panAgentsCalculations, false);
            tabSaveDeleteButtons();
            observable.resetTable();
            observable.ttNotifyObservers();
            rowSelected = -1;
        }
        tabDepositsProduct.setSelectedComponent(panFloatingRateAccount);

        // TODO add your handling code here:
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void txtToAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAmtFocusLost
        // TODO add your handling code here:
        double fromAmt = CommonUtil.convertObjToDouble(txtFromAmt.getText()).doubleValue();
        double toAmt = CommonUtil.convertObjToDouble(txtToAmt.getText()).doubleValue();
        if (fromAmt < toAmt) {
            System.out.println("$$$$$$$$");
        } else {
            ClientUtil.displayAlert("Enter from amount is greater than To amt...");
            txtToAmt.setText("");
            txtToAmt.requestFocus(true);
        }
    }//GEN-LAST:event_txtToAmtFocusLost

    private void tblInterestTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInterestTableMousePressed
        // Add your handling code here:
        //        updateOBFields();
        int selectedRow = tblInterestTable.getSelectedRow();
        observable.authStatus = observable.populateInterestTab(selectedRow);
        if ((observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE)
                && (observable.getActionType() != ClientConstants.ACTIONTYPE_AUTHORIZE)) {
            ClientUtil.enableDisable(panAgentsCalculations, true);
            //            setInterTabEnableDisable(false);
            tabNewButtons();
        }
        updateTab = 1;
        rowSelected = selectedRow;
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(true);
        btnTabDelete.setEnabled(true);
        observable.ttNotifyObservers();
        if (observable.authStatus == true) {
            ClientUtil.enableDisable(panFloatingRateAccount, false);
            btnTabNew.setEnabled(true);
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
        } else {
            ClientUtil.enableDisable(panFloatingRateAccount, true);
        }
        tabDepositsProduct.setSelectedComponent(panFloatingRateAccount);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            ClientUtil.enableDisable(panAgentsCalculations, false);
            btnTabNew.setEnabled(false);
            btnTabSave.setEnabled(false);
            btnTabDelete.setEnabled(false);
        }
        //        observable.newRecEntering = false;
        // TODO add your handling code here:
    }//GEN-LAST:event_tblInterestTableMousePressed

    private void txtIntProvisioningAcctHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntProvisioningAcctHdFocusLost
        // TODO add your handling code here:
        if (!(txtIntProvisioningAcctHd.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtIntProvisioningAcctHd, "product.deposits.getAcctHeadList");
            txtIntProvisioningAcctHdDesc.setText(getAccHeadDesc(txtIntProvisioningAcctHd.getText()));
        }
    }//GEN-LAST:event_txtIntProvisioningAcctHdFocusLost

    private void btnIntProvisioningAcctHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntProvisioningAcctHdActionPerformed
        // Add your handling code here:
        popUp(INTPROVACCHD);
    }//GEN-LAST:event_btnIntProvisioningAcctHdActionPerformed

    private void txtIntOnMaturedDepositAcctHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntOnMaturedDepositAcctHeadFocusLost
        // TODO add your handling code here:
        if (!(txtIntOnMaturedDepositAcctHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtIntOnMaturedDepositAcctHead, "product.deposits.getAcctHeadList");
            txtIntOnMaturedDepositAcctHeadDesc.setText(getAccHeadDesc(txtIntOnMaturedDepositAcctHead.getText()));
        }
    }//GEN-LAST:event_txtIntOnMaturedDepositAcctHeadFocusLost

    private void btnIntOnMaturedDepositAcctHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntOnMaturedDepositAcctHeadActionPerformed
        // Add your handling code here:
        popUp(INTONMAT);
    }//GEN-LAST:event_btnIntOnMaturedDepositAcctHeadActionPerformed

    private void txtFixedDepositAcctHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFixedDepositAcctHeadFocusLost
        // TODO add your handling code here:
        if (!(txtFixedDepositAcctHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtFixedDepositAcctHead, "product.deposits.getAcctHeadList");
            txtFixedDepositAcctHeadDesc.setText(getAccHeadDesc(txtFixedDepositAcctHead.getText()));
        }
    }//GEN-LAST:event_txtFixedDepositAcctHeadFocusLost

    private void btnFixedDepositAcctHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFixedDepositAcctHeadActionPerformed
        // Add your handling code here:
        popUp(FIXEDDEPOSIT);
        //final int FIXEDDEPOSIT=2,ACCHEAD=3,INTPROVMAT=4,INTONMAT=5,MATDEPOSIT=6;
        //final int INTDEPPLHD=7,INTPAYGLHEAD=8,INTPROVACCHD=9;
    }//GEN-LAST:event_btnFixedDepositAcctHeadActionPerformed

    private void txttMaturityDepositAcctHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txttMaturityDepositAcctHeadFocusLost
        // TODO add your handling code here:
        if (!(txttMaturityDepositAcctHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txttMaturityDepositAcctHead, "product.deposits.getAcctHeadList");
            txttMaturityDepositAcctHeadDesc.setText(getAccHeadDesc(txttMaturityDepositAcctHead.getText()));
        }
    }//GEN-LAST:event_txttMaturityDepositAcctHeadFocusLost

    private void btnMaturityDepositAcctHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaturityDepositAcctHeadActionPerformed
        // Add your handling code here:
        popUp(MATDEPOSIT);

        //final int FIXEDDEPOSIT=2,ACCHEAD=3,INTPROVMAT=4,INTONMAT=5,MATDEPOSIT=6;
        //final int INTDEPPLHD=7,INTPAYGLHEAD=8,INTPROVACCHD=9;
    }//GEN-LAST:event_btnMaturityDepositAcctHeadActionPerformed

    private void txtIntProvisionOfMaturedDepositFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntProvisionOfMaturedDepositFocusLost
        // TODO add your handling code here:
        if (!(txtIntProvisionOfMaturedDeposit.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtIntProvisionOfMaturedDeposit, "product.deposits.getAcctHeadList");
            txtIntProvisionOfMaturedDepositDesc.setText(getAccHeadDesc(txtIntProvisionOfMaturedDeposit.getText()));
        }
    }//GEN-LAST:event_txtIntProvisionOfMaturedDepositFocusLost

    private void btnIntProvisionOfMaturedDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntProvisionOfMaturedDepositActionPerformed
        // Add your handling code here:
        popUp(INTPROVMAT);
    }//GEN-LAST:event_btnIntProvisionOfMaturedDepositActionPerformed

    private void txtIntPaybleGLHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntPaybleGLHeadFocusLost
        // TODO add your handling code here:
        if (!(txtIntPaybleGLHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtIntPaybleGLHead, "product.deposits.getAcctHeadList");
            txtIntPaybleGLHeadDesc.setText(getAccHeadDesc(txtIntPaybleGLHead.getText()));
        }
    }//GEN-LAST:event_txtIntPaybleGLHeadFocusLost

    private void btnIntPaybleGLHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntPaybleGLHeadActionPerformed
        // Add your handling code here:
        popUp(INTPAYGLHEAD);
    }//GEN-LAST:event_btnIntPaybleGLHeadActionPerformed

    private void txtIntDebitPLHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIntDebitPLHeadFocusLost
        // TODO add your handling code here:
        if (!(txtIntDebitPLHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtIntDebitPLHead, "product.deposits.getAcctHeadList");
            txtIntDebitPLHeadDesc.setText(getAccHeadDesc(observable.getTxtIntDebitPLHead()));
        }
    }//GEN-LAST:event_txtIntDebitPLHeadFocusLost

    private void btnIntDebitPLHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIntDebitPLHeadActionPerformed
        // Add your handling code here:
        popUp(INTDEPPLHD);
    }//GEN-LAST:event_btnIntDebitPLHeadActionPerformed

    private void txtAcctHeadForFloatAcctFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctHeadForFloatAcctFocusLost
        // TODO add your handling code here:
        if (!(txtAcctHeadForFloatAcct.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtAcctHeadForFloatAcct, "product.deposits.getAcctHeadList");
            txtAcctHeadForFloatAcctDesc.setText(getAccHeadDesc(txtAcctHeadForFloatAcct.getText()));
        }
    }//GEN-LAST:event_txtAcctHeadForFloatAcctFocusLost

    private void btnAcctHeadForFloatAcctActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctHeadForFloatAcctActionPerformed
        // Add your handling code here:
        popUp(ACCHEAD);
    }//GEN-LAST:event_btnAcctHeadForFloatAcctActionPerformed

    private void txtCommisionPaybleGLHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommisionPaybleGLHeadFocusLost
        if (!(txtCommisionPaybleGLHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtCommisionPaybleGLHead, "product.deposits.getAcctHeadList");
            txtCommisionPaybleGLHeadDesc.setText(getAccHeadDesc(observable.getTxtCommisionPaybleGLHead()));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCommisionPaybleGLHeadFocusLost

    private void btnCommisionPaybleGLHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommisionPaybleGLHeadActionPerformed
        popUp(COMMISIONPAYGLHEAD);        // TODO add your handling code here:
    }//GEN-LAST:event_btnCommisionPaybleGLHeadActionPerformed

    private void txtPenalChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalChargesFocusLost
        if (!(txtPenalCharges.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtPenalCharges, "product.deposits.getAcctHeadList");
            txtPenalChargesDesc.setText(getAccHeadDesc(txtPenalCharges.getText()));
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPenalChargesFocusLost

    private void btnPenalChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenalChargesActionPerformed
        popUp(RDPENALACHD);
        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPenalChargesActionPerformed

    private void txtTransferOutHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTransferOutHeadActionPerformed
        // TODO add your handling code here:
        if (!(txtTransferOutHead.getText().equalsIgnoreCase(""))) {
            observable.verifyAcctHead(txtTransferOutHead, "product.deposits.getAcctHeadList");
        }
    }//GEN-LAST:event_txtTransferOutHeadActionPerformed

    private void btnTransferOutHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransferOutHeadActionPerformed
        // TODO add your handling code here:
        popUp(TRANSFEROUTACHD);
    }//GEN-LAST:event_btnTransferOutHeadActionPerformed
    public void enableDisablePeriod(boolean flag) {
        txtMinDepositPeriod.setEnabled(flag);
        cboMinDepositPeriod.setEnabled(flag);
        txtMaxDepositPeriod.setEnabled(flag);
        cboMaxDepositPeriod.setEnabled(flag);
    }

    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // TODO add your handling code here:
        observable.resetForm();                                      // Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_COPY);//Sets the Action Type to be performed...
        popUp(COPY);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        //id generation
        HashMap idMap = generateID();
        if(idMap != null && idMap.size()>0)
            txtProductID.setText((String) idMap.get(CommonConstants.DATA)); 
    }//GEN-LAST:event_btnCopyActionPerformed
    public HashMap generateID() {
        HashMap hash = null, result = null;
        try {
            String mapName = "getCurrentID";
            HashMap where = new HashMap();
            where.put("ID_KEY", "LIABILITY_ID"); //Here u have to pass assetid or something else
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
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    private void tblDepSubNoRowSelected(int rowSelected) {
        setDepSubNoNewAndTblPress();
        btnDelayedDel.setEnabled(true);
        observable.populateDepSubNoFields(tblDelayedInstallmet.getSelectedRow());
    }

    private void setDepSubNoNewAndTblPress() {
        updateOBFields();
        //        setDepSubNoFields(true);
        //        setBtnDepSubNo(false);
        btnDelayedSave.setEnabled(true);

    }

    private void setInterTabEnableDisable(boolean value) {
        tdtDate.setEnabled(value);
        tdtToDate.setEnabled(value);
        //        cboFromAmount.setEnabled(value);
        //        cboToAmount.setEnabled(value);
        txtFromAmt.setEnabled(value);
        txtToAmt.setEnabled(value);
        ClientUtil.enableDisable(panFromPeriod, value);
        ClientUtil.enableDisable(panToPeriod, value);
    }

    private void btnEnableDisplay() {
        tdtDate.setEnabled(false);
        tdtToDate.setEnabled(false);
        txtFromPeriod.setEnabled(false);
        txtToPeriod.setEnabled(false);
        //        cboFromAmount.setEnabled(false);
        //        cboToAmount.setEnabled(false);
        txtToAmt.setEnabled(false);
        txtFromAmt.setEnabled(false);
        cboFromPeriod.setEnabled(false);
        cboToPeriod.setEnabled(false);
        txtCommisionRate.setEnabled(false);
        txtFromPeriod.setEnabled(false);
        panFromPeriod.setEnabled(false);
        panToPeriod.setEnabled(false);

    }

    private void tabSaveDeleteButtons() {
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
    }

    private void tabNewButtons() {
        btnTabNew.setEnabled(true);
        btnTabSave.setEnabled(true);
        btnTabDelete.setEnabled(true);
    }

    private String txtCutOffDayForPaymentOfInstalRule() {
        String message = "";

        if (!(txtCutOffDayForPaymentOfInstal.getText().equalsIgnoreCase(""))) {
            System.out.println("txtCutOffDayForPaymentOfInstal.getText(): " + txtCutOffDayForPaymentOfInstal.getText());
            double numericDate = Double.parseDouble(txtCutOffDayForPaymentOfInstal.getText());
            if (numericDate < 1
                    || numericDate > 31) {
                message = resourceBundle.getString("CHOOSENDAYTWARNING");
            }
        }
        return message;
    }

    private String txtWithdrawalsInMultiplesOfRule() {
        String message = "";

        if (!((txtMinAmtOfPartialWithdrawalAllowed.getText().equalsIgnoreCase(""))
                || (txtWithdrawalsInMultiplesOf.getText().equalsIgnoreCase("")))) {
            if (Double.parseDouble(txtMinAmtOfPartialWithdrawalAllowed.getText())
                    > Double.parseDouble(txtWithdrawalsInMultiplesOf.getText())) {
                message = resourceBundle.getString("FURTHERWITHDRAWLWARNING");
            }
        }
        return message;
    }

    private String txtMinAmtOfPartialWithdrawalAllowedRule() {
        String message = "";

        if (!((txtMinAmtOfPartialWithdrawalAllowed.getText().equalsIgnoreCase(""))
                || (txtMinDepositAmt.getText().equalsIgnoreCase("")))) {
            if (Double.parseDouble(txtMinAmtOfPartialWithdrawalAllowed.getText())
                    < Double.parseDouble(txtMinDepositAmt.getText())) {
                message = resourceBundle.getString("MINWITHDRAWLWARNING");
            }
        }
        return message;
    }

    private String txtMaxNoOfPartialWithdrawalAllowedRule() {
        String message = "";

        if (!((txtMaxNoOfPartialWithdrawalAllowed.getText().equalsIgnoreCase(""))
                || (txtNoOfPartialWithdrawalAllowed.getText().equalsIgnoreCase("")))) {
            if (Double.parseDouble(txtMaxNoOfPartialWithdrawalAllowed.getText())
                    > Double.parseDouble(txtNoOfPartialWithdrawalAllowed.getText())) {
                message = resourceBundle.getString("MAXWITHDRAWLWARNING");
            }
        }
        return message;
    }

    private String txtMaxAmtOfPartialWithdrawalAllowedRule() {
        String message = "";

        if (!(txtMaxAmtOfPartialWithdrawalAllowed.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtMaxAmtOfPartialWithdrawalAllowed.getText()) > 100) {
                message = resourceBundle.getString("MAXAMOUNTWARNING");
            }
        }
        return message;
    }            //        private String txtAmtInMultiplesOfRule(){
    //        String message = "";
    //
    //        if(!(txtAmtInMultiplesOf.getText().equalsIgnoreCase("")
    //        || txtMaxDepositAmt.getText().equalsIgnoreCase("")
    //        || txtMinDepositAmt.getText().equalsIgnoreCase(""))){
    //            double maxAmount = Double.parseDouble(txtMaxDepositAmt.getText());
    //            double minAmount = Double.parseDouble(txtMinDepositAmt.getText());
    //            double rangeamount = Double.parseDouble(txtAmtInMultiplesOf.getText());
    //
    //            if( (rangeamount > maxAmount)
    //            || (rangeamount < minAmount) ){
    //                message = resourceBundle.getString("AMOUNTRANGEWARNING");
    //            }
    //        }
    //        return message;
    //    }

    private String txtMaxDepositAmtRule() {
        String message = "";

        if (!(txtMinDepositAmt.getText().equalsIgnoreCase("")
                || txtMaxDepositAmt.getText().equalsIgnoreCase(""))) {
            if (Double.parseDouble(txtMinDepositAmt.getText()) > Double.parseDouble(txtMaxDepositAmt.getText())) {
                message = resourceBundle.getString("AMOUNTWARNING");
            }
        }
        return message;
    }

    //    private String cboPeriodInMultiplesOfRule(){
    //        String message = "";
    //
    //        observable.setCboPeriodInMultiplesOf((String) cboPeriodInMultiplesOf.getSelectedItem());
    //        observable.setCboMinDepositPeriod((String) cboMinDepositPeriod.getSelectedItem());
    //        observable.setCboMaxDepositPeriod((String) cboMaxDepositPeriod.getSelectedItem());
    //
    //        if((observable.getCboPeriodInMultiplesOf().length() > 0)
    //            &&(observable.getCboMinDepositPeriod().length() > 0)
    //            &&(observable.getCboMaxDepositPeriod().length() > 0)){
    //            double minPeriod = observable.getMultiply(observable.getCboMinDepositPeriod()) * Double.parseDouble(txtMinDepositPeriod.getText());
    //            double maxPeriod = observable.getMultiply(observable.getCboMaxDepositPeriod()) * Double.parseDouble(txtMaxDepositPeriod.getText());
    //            double rangeReriod = observable.getMultiply(observable.getCboPeriodInMultiplesOf()) *Double.parseDouble(txtPeriodInMultiplesOf.getText());
    //
    //            if ( (rangeReriod > maxPeriod)
    //            || (rangeReriod < minPeriod) ){
    //                message = resourceBundle.getString("PERIODRANGEWARNING");
    //            }
    //        }
    //        return message;
    //  }
    private String cboMaxDepositPeriodRule() {
        String message = "";

        observable.setCboMinDepositPeriod((String) cboMinDepositPeriod.getSelectedItem());
        observable.setCboMaxDepositPeriod((String) cboMaxDepositPeriod.getSelectedItem());
        if ((observable.getCboMinDepositPeriod().length() > 0)
                && (observable.getCboMaxDepositPeriod().length() > 0)) {
            int maxPeriod = observable.getMultiply(observable.getCboMaxDepositPeriod());
            int minPeriod = observable.getMultiply(observable.getCboMinDepositPeriod());
            if ((minPeriod * CommonUtil.convertObjToDouble(txtMinDepositPeriod.getText()).doubleValue())
                    > (maxPeriod * CommonUtil.convertObjToDouble(txtMaxDepositPeriod.getText()).doubleValue())) {
                message = resourceBundle.getString("PERIODWARNING");
            }
        }
        return message;
    }    	private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
            // Add your handling code here:
            observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
            authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
                    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
                        // Add your handling code here:
                        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
                        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
                                                            private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
                                                                // Add your handling code here:
                                                                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                                                                authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
                                                                disDesc();
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    public void authorizeStatus(String authorizeStatus) {//        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        if (viewType == AUTHORIZE && isFilled) {
            //                String strWarnMsg = tabDepositsProduct.isAllTabsVisited();
            //            if (strWarnMsg.length() > 0){
            //                displayAlert(strWarnMsg);
            //                return;
            //            }
            ////                strWarnMsg = null;

            String warningMessage = tabDepositsProduct.isAllTabsVisited();
            if (warningMessage.length() > 0) {
                displayAlert(warningMessage);
            } else {
                tabDepositsProduct.resetVisits();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("PRODUCT ID", txtProductID.getText());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
                ClientUtil.execute("authorizeDepositProduct", singleAuthorizeMap);
                ClientUtil.execute("authorizeWeeklyDepositSlab", singleAuthorizeMap);
                ClientUtil.execute("authorizeAgentCommissionSlab", singleAuthorizeMap);
                String PRODTYPE = CommonUtil.convertObjToStr((((ComboBoxModel) (cboOperatesLike).getModel())).getKeyForSelected());
                if (PRODTYPE.equals("RECURRING") || PRODTYPE.equals("DAILY")) {
                    HashMap roiMap = new HashMap();
                    roiMap.put("ROI_GROUP_ID", txtProductID.getText());
                    List lst = ClientUtil.executeQuery("getSelectDailyRecrringRecord", roiMap);
                    if (lst != null && lst.size() > 0) {
                        for (int i = 0; i < lst.size(); i++) {
                            roiMap = (HashMap) lst.get(i);
                            roiMap.put("STATUS", CommonConstants.STATUS_AUTHORIZED);
                            roiMap.put("USER_ID", TrueTransactMain.USER_ID);
                            roiMap.put("AUTHORIZEDT", currDt.clone());
                            roiMap.put("ROI GROUPID", roiMap.get("ROI_GROUP_ID"));
                            ClientUtil.execute("authInterMaintenanceForDepositRoiGroupTypeRate", roiMap);
                        }
                    }
                }
                btnCancelActionPerformed(null);
                observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
                lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(txtProductID.getText());
                viewType = 0;
            }
        } else {
            final HashMap mapParam = new HashMap();
            mapParam.put("USER_ID", TrueTransactMain.USER_ID);
            mapParam.put(CommonConstants.MAP_NAME, "getDepositProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeDepositProduct");
            viewType = AUTHORIZE;
            isFilled = false;
            //__ To Save the data in the Internal Frame...
            //            setModified(true);

            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
        }
    }

    private void renewalParameterReset() {
        rdoRenewalOfDepositAllowed_Yes.setEnabled(true);
        rdoRenewalOfDepositAllowed_No.setEnabled(true);
        txtIntPeriodForBackDatedRenewal.setEnabled(true);
        cboIntPeriodForBackDatedRenewal.setEnabled(true);
        txtMinimumRenewalDeposit.setEnabled(true);
        cboMinimumRenewalDeposit.setEnabled(true);
        txtRenewedclosedbefore.setEnabled(true);
        cboRenewedclosedbefore.setEnabled(true);
        rdoClosedwithinperiod_Yes.setEnabled(true);
        rdoClosedwithinperiod_No.setEnabled(true);
        rdoInterestalreadyPaid_Yes.setEnabled(true);
        rdoInterestalreadyPaid_No.setEnabled(true);
        rdoInterestrateappliedoverdue_Yes.setEnabled(true);
        rdoInterestrateappliedoverdue_No.setEnabled(true);
        rdoSBRateOneRate_Yes.setEnabled(true);
        rdoSBRateOneRate_No.setEnabled(true);
        rdoBothRateNotAvail_Yes.setEnabled(true);
        rdoBothRateNotAvail_No.setEnabled(true);
        rdoAutoRenewalAllowed_Yes.setEnabled(true);
        rdoAutoRenewalAllowed_No.setEnabled(true);
        rdoSameNoRenewalAllowed_Yes.setEnabled(true);
        rdoSameNoRenewalAllowed_No.setEnabled(true);
        txtMaxNopfTimes1.setEnabled(true);
        cboIntType.setEnabled(true);
        txtMaxPeriodMDt.setEnabled(true);
        cboMaxPeriodMDt.setEnabled(true);
        txtMinNoOfDays.setEnabled(true);
        cboMinNoOfDays.setEnabled(true);
        cboIntCriteria.setEnabled(true);
        chkRateAsOnDateOfRenewal.setEnabled(true);
        chkRateAsOnDateOfMaturity.setEnabled(true);
        cboEitherofTwoRatesChoose.setEnabled(true);
        txtMaxNopfTimes.setEnabled(true);
        chkIntRateApp.setEnabled(true);
        chkIntRateDeathMarked.setEnabled(true);

    }
    // To reset the fields associated with radio buttons...

    private void txtReset() {
        cboRoundOffCriteria.setEnabled(true);
        cboMaturityInterestType.setEnabled(true);
        cboMaturityInterestRate.setEnabled(true);
        txtInterestOnMaturedDeposits.setEnabled(true);
        txtInterestOnMaturedDeposits.setEditable(true);
        txtAfterHowManyDays.setEnabled(true);
        txtAfterHowManyDays.setEditable(true);
        txtAdvanceMaturityNoticeGenPeriod.setEnabled(true);
        txtAdvanceMaturityNoticeGenPeriod.setEditable(true);
        txtNoOfPartialWithdrawalAllowed.setEnabled(true);
        txtNoOfPartialWithdrawalAllowed.setEditable(true);
        txtMaxNoOfPartialWithdrawalAllowed.setEnabled(true);
        txtMaxNoOfPartialWithdrawalAllowed.setEditable(true);
        txtMaxAmtOfPartialWithdrawalAllowed.setEnabled(true);
        txtMaxAmtOfPartialWithdrawalAllowed.setEditable(true);
        txtMinAmtOfPartialWithdrawalAllowed.setEnabled(true);
        txtMinAmtOfPartialWithdrawalAllowed.setEditable(true);
        txtWithdrawalsInMultiplesOf.setEnabled(true);
        txtWithdrawalsInMultiplesOf.setEditable(true);
        cboIntProvisioningFreq.setEnabled(true);
        cboProvisioningLevel.setEnabled(true);

        txtMinIntToBePaid.setEnabled(true);
        txtMinIntToBePaid.setEditable(true);
        txtMinNoOfDays.setEnabled(true);
        txtMinNoOfDays.setEditable(true);
        txtMaxNopfTimes.setEnabled(true);
        txtMaxNopfTimes.setEditable(true);

        txtAfterNoDays.setEnabled(true);
        txtAfterNoDays.setEditable(true);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();                                     // Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        popUp(DELETE);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();                                      // Reset the fields in the UI to null...
        
        System.out.println("getEffectiveDateEntered" + observable.getEffectiveDateEntered());
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        disDesc();
        if (rdoPenalRounded_No.isSelected()) {
            //lblRoundOffCriteriaPenal
            lblRoundOffCriteriaPenal.setVisible(false);
            cboRoundOffCriteriaPenal.setVisible(false);
        } else if (rdoPenalRounded_Yes.isSelected()) {
            cboRoundOffCriteriaPenal.setVisible(true);
            lblRoundOffCriteriaPenal.setVisible(true);
        }
        if (rdoInterestrateappliedoverdue_Yes.isSelected()) {
            sbRateCmb.setVisible(true);
        } else if (rdoInterestrateappliedoverdue_No.isSelected()) {
            sbRateCmb.setVisible(false);
        }
    }//GEN-LAST:event_btnEditActionPerformed
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
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...

    private void popUp(int field) {
        final ViewAll objViewAll;
        final HashMap viewMap = new HashMap();
        viewType = field;
        if (field == EDIT || field == DELETE || field == VIEW || field == COPY) {//Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("PRODUCT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put("MAPNAME", "viewDepositsProduct");
            objViewAll = new ViewAll(this, viewMap);
            //mapped statement: viewDepositsProduct---> result map should be a Hashmap...
        } else {
            updateOBFields();
            HashMap whereMap = new HashMap();

            if (viewType == FIXEDDEPOSIT) {
            } else if (viewType == ACCHEAD) {
                whereMap.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);
                //                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                whereMap.put(BALANCETYPE, CommonConstants.CREDIT);
            } else if (viewType == INTPROVMAT) {
            } else if (viewType == INTONMAT) {
            } else if (viewType == MATDEPOSIT) {
            } else if (viewType == INTDEPPLHD) {
            } else if (viewType == INTPAYGLHEAD) {
            } else if (viewType == INTPROVACCHD) {
            } else if (viewType == DEPACCHD) {
            } else if (viewType == TDSGLACCTHD) {
            } else if (viewType == COMMISIONPAYGLHEAD) {
            } else if (viewType == RDPENALACHD) {
            } else if (viewType == TRANSFEROUTACHD) {
            } else if (viewType == INTRSTRECOVRYACHD) {
            }
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "product.deposits.getAcctHeadList");
            objViewAll = new ViewAll(this, viewMap);
            objViewAll.setTitle(resourceBundle.getString("acHdTitle"));
        }
        //        new ViewAll(this, viewMap).show();
        tblInterestTable.setModel(observable.getTblInterestTable());
        tblDelayedInstallmet.setModel(observable.getTblDelayedInstallmet());
        btnTabSave.setEnabled(true);
        btnTabNew.setEnabled(true);
        btnTabDelete.setEnabled(true);
        objViewAll.show();
    }

    // Called Automatically when viewAll() is Called...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("fillData  : " + hash);
        final String ACCOUNTHEAD = (String) hash.get("ACCOUNT HEAD");
        final String ACCOUNTHEADDESC = (String) hash.get("ACCOUNT HEAD DESCRIPTION");
        if (viewType != -1) {
            if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == VIEW || viewType == COPY) {
                isFilled = true;
                hash.put(CommonConstants.MAP_WHERE, hash.get("PRODUCT ID"));
                btnEnableDisplay();
                btnTabDelete.setEnabled(false);
                btnTabSave.setEnabled(false);
                btnTabNew.setEnabled(true);
                btnCommisionPaybleGLHead.setEnabled(true);
                observable.populateData(hash);
                if (hash.get("OPERATES LIKE").equals("CUMMULATIVE") || hash.get("OPERATES LIKE").equals("FIXED")) {
                    lblFDRenewalSameNoTranPrincAmt.setVisible(true);
                    chkFDRenewalSameNoTranPrincAmt.setVisible(true);
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                    ClientUtil.enableDisable(this, false);
                    rdoPartialWithdrawalAllowed_Yes.setEnabled(false);
                    rdoPartialWithdrawalAllowed_No.setEnabled(false);
                    btnCommisionPaybleGLHead.setEnabled(false);
                } else {
                    ClientUtil.enableDisable(this, true);
                    enableDisableButtons(true);
                    if (hash.get("OPERATES LIKE").equals("CUMMULATIVE")) {
                        chkDifferentROIActionPerformed(null);
                    }
                    /*
                     * Account Head for the Authorized records Shpuld not be Modified.
                     */
                    if (observable.getLblAuthorize().equalsIgnoreCase("AUTHORIZED")) {
                        btnAcctHead.setEnabled(false);
                    }
                    //                    disableTextBox();
                    txtProductID.setEnabled(false);
                }
                setButtonEnableDisable();       // Enables or Disables the buttons and menu Items depending on their previous state...
                observable.setStatus();         // To set the Value of lblStatus...
                btnEnableDisplay();
                if (viewType == EDIT) {
                    rowCount = tblInterestTable.getRowCount();
                    rdoPartialWithdrawalAllowed_Yes.setEnabled(true);
                    rdoPartialWithdrawalAllowed_No.setEnabled(true);
                    if (hash.get("OPERATES LIKE").equals("RECURRING")) {
                        tabDepositsProduct.resetVisits();
                        tabDepositsProduct.add(panFloatingRateAccount);
                        //                        tabDepositsProduct.setTitleAt(3, "Recurring Deposit");
                        //                        tabDepositsProduct.setTitleAt(4,"Delayed Installment Interest Rate");

                        //                        tabDepositsProduct.setTitleAt(3, "Recurring Deposit");
                        //                        tabDepositsProduct.setTitleAt(4,"Delayed Installment Interest Rate");
                        //                        tabDepositsProduct.setTitleAt(5, "Renewal Parameters");
                        //tabDepositsProduct.setTitleAt(3, "Renewal Parameters");
                        tabDepositsProduct.setTitleAt(3, "Recurring Deposit");
                        tabDepositsProduct.setTitleAt(4, "Delayed Installment Interest Rate");
                        //                        tabDepositsProduct.setTitleAt(5,"Delayed Installment Interest Rate");



                        //                        tabDepositsProduct.setTitleAt(5,"Delayed Installment Interest Rate");
                        //                                                tabDepositsProduct.resetVisits();
                        lblDelayedChargesDet.setVisible(true);
                        lblFromAmount.setVisible(true);
                        lblFromAmount.setText("Installment Amount From");
                        lblToAmount.setVisible(true);
                        lblToAmount.setText("Installment Amount To");
                        //                        cboFromAmount.setVisible(false);
                        //                        cboToAmount.setVisible(false);
                        txtFromAmt.setVisible(false);
                        txtToAmt.setVisible(false);
                        lblFromPeriod.setVisible(false);
                        txtFromPeriod.setVisible(false);
                        cboFromPeriod.setVisible(false);
                        lblToPeriod.setVisible(false);
                        txtToPeriod.setVisible(false);
                        cboToPeriod.setVisible(false);
                        lblRateInterest.setVisible(true);
                        txtCommisionRate.setVisible(true);
                        lblRateInterest.setText("Delayed Installment Interest Rate");
                        lblDelayedFromAmt.setVisible(false);
                        txtFromAmt.setVisible(true);
                        lblDelayedToAmt.setVisible(false);
                        txtToAmt.setVisible(true);
                        lblDelayedChargesAmt.setVisible(false);
                        txtDelayedChargesAmt.setVisible(false);
                        lblDate.setVisible(false);
                        tdtDate.setVisible(false);
                        lblToDate.setVisible(false);
                        tdtToDate.setVisible(false);
                        lblLastInstallmentAllowed.setVisible(true);
                        lblMaturityDateAfterLastInstalPaid.setVisible(true);
                        lblRecurringDepositToFixedDeposit.setVisible(true);
                        lblInstallmentInRecurringDepositAcct.setVisible(true);
                        lblInstallmentToBeCharged.setVisible(true);
                        lblChangeValue.setVisible(true);
                        lblCanReceiveExcessInstal.setVisible(true);
                        lblIntPayableOnExcessInstal.setVisible(true);
                        lblCutOffDayForPaymentOfInstal.setVisible(true);
                        lblPenaltyOnLateInstallmentsChargeble.setVisible(true);
                        txtMaturityDateAfterLastInstalPaid.setVisible(true);
                        cboMaturityDateAfterLastInstalPaid.setVisible(true);
                        lblAgentCommision.setVisible(false);
                        lblMinimumPeriod.setVisible(false);
                        panLastInstallmentAllowedRD.setVisible(false);
                        lblLastInstallmentAllowed.setVisible(false);
                        panMaturityDateAfterLastInstalPaid.setVisible(true);
                        panRecurringDepositToFixedDeposit.setVisible(true);
                        panInstallmentInRecurringDepositAcct.setVisible(true);
                        cboInstallmentToBeCharged.setVisible(true);
                        cboChangeValue.setVisible(true);
                        cboDepositsFrequency.setVisible(true);
                        panCanReceiveExcessInstal.setVisible(true);
                        panIntPayableOnExcessInstal.setVisible(true);
                        cboCutOffDayForPaymentOfInstal.setVisible(true);
                        txtCutOffDayForPaymentOfInstal.setVisible(true);
                        panPenaltyOnLateInstallmentsChargeble.setVisible(true);
                        rdoPenaltyOnLateInstallmentsChargeble_Yes.setVisible(true);
                        rdoPenaltyOnLateInstallmentsChargeble_No.setVisible(true);
                        rdoCanReceiveExcessInstal_yes.setVisible(true);
                        rdoCanReceiveExcessInstal_No.setVisible(true);
                        rdoIntPayableOnExcessInstal_Yes.setVisible(true);
                        rdoIntPayableOnExcessInstal_No.setVisible(true);
                        panAgentCommision.setVisible(false);
                        panMinimumPeriod.setVisible(false);
                        lblDailyDepositCalc.setVisible(false);
                        lblWeekly.setVisible(false);
                        panDailyIntCalc.setVisible(false);
                        cboWeekly.setVisible(false);
                        lblWeekly.setVisible(false);
                        cboMonthlyIntCalcMethod.setVisible(false);
                        panPartialWithdrawlAllowedForDD.setVisible(true);
                        lblPartialWithdrawlAllowedForDD.setVisible(true);
                        ClientUtil.enableDisable(panWeeklyDepositSlab, true);
                        tabDepositsProduct.addTab("Weekly Deposit Slab", panWeeklyDepositSlab);
                        enableWeeklySlabTxt(false);
                        //                        rdoWeeklyActionPerformed(null);
                        //                        rdoMonthlyActionPerformed(null);
                        //                        rdoDailyActionPerformed(null);
                        //                        tabDepositsProduct.setVisible(true);
                        //                        panDelayedInstallmets.setVisible(true);
                    } else if (hash.get("OPERATES LIKE").equals("DAILY")) {
                        tabDepositsProduct.resetVisits();
                        tabDepositsProduct.add(panFloatingRateAccount);

                        tabDepositsProduct.setTitleAt(3, "Agent's Commission Rate Maintenance");
                        //                        tabDepositsProduct.resetVisits();
                        lblDelayedChargesDet.setVisible(false);
                        lblDate.setText("Effective Date From");
                        lblToDate.setText("Effective Date To");
                        lblFromPeriod.setText("Deposit Period From");
                        lblToPeriod.setText("Deposit Period To");
                        //                        panRecurringDetails.set
                        lblFromAmount.setVisible(true);
                        lblFromAmount.setText("From Amount");
                        lblToAmount.setVisible(true);
                        lblToAmount.setText("To Amount");
                        //                        cboFromAmount.setVisible(true);
                        //                        cboToAmount.setVisible(true);
                        txtFromAmt.setVisible(true);
                        txtToAmt.setVisible(true);
                        lblFromPeriod.setVisible(true);
                        txtFromPeriod.setVisible(true);
                        cboFromPeriod.setVisible(true);
                        lblToPeriod.setVisible(true);
                        txtToPeriod.setVisible(true);
                        cboToPeriod.setVisible(true);
                        lblRateInterest.setVisible(true);
                        txtCommisionRate.setVisible(true);
                        lblRateInterest.setText("Rate of Interest");
                        lblDelayedFromAmt.setVisible(false);
                        txtFromAmt.setVisible(false);
                        lblDelayedToAmt.setVisible(false);
                        txtToAmt.setVisible(false);
                        lblDelayedChargesAmt.setVisible(false);
                        txtDelayedChargesAmt.setVisible(false);
                        lblDate.setVisible(true);
                        tdtDate.setVisible(true);
                        lblToDate.setVisible(true);
                        tdtToDate.setVisible(true);
                        lblLastInstallmentAllowed.setVisible(false);
                        lblMaturityDateAfterLastInstalPaid.setVisible(false);
                        lblRecurringDepositToFixedDeposit.setVisible(true);
                        lblInstallmentInRecurringDepositAcct.setVisible(true);
                        lblInstallmentToBeCharged.setVisible(true);
                        lblChangeValue.setVisible(true);
                        lblCanReceiveExcessInstal.setVisible(false);
                        lblIntPayableOnExcessInstal.setVisible(false);
                        lblCutOffDayForPaymentOfInstal.setVisible(false);
                        lblPenaltyOnLateInstallmentsChargeble.setVisible(false);
                        txtMaturityDateAfterLastInstalPaid.setVisible(false);
                        cboMaturityDateAfterLastInstalPaid.setVisible(false);
                        lblAgentCommision.setVisible(true);
                        lblMinimumPeriod.setVisible(true);
                        panLastInstallmentAllowedRD.setVisible(false);
                        lblLastInstallmentAllowed.setVisible(false);
                        panMaturityDateAfterLastInstalPaid.setVisible(false);
                        panRecurringDepositToFixedDeposit.setVisible(true);
                        panInstallmentInRecurringDepositAcct.setVisible(true);
                        cboInstallmentToBeCharged.setVisible(true);
                        cboChangeValue.setVisible(true);
                        cboDepositsFrequency.setVisible(true);
                        panCanReceiveExcessInstal.setVisible(true);
                        panIntPayableOnExcessInstal.setVisible(true);
                        cboCutOffDayForPaymentOfInstal.setVisible(false);
                        txtCutOffDayForPaymentOfInstal.setVisible(false);
                        panPenaltyOnLateInstallmentsChargeble.setVisible(false);
                        rdoPenaltyOnLateInstallmentsChargeble_Yes.setVisible(false);
                        rdoPenaltyOnLateInstallmentsChargeble_No.setVisible(false);
                        rdoCanReceiveExcessInstal_yes.setVisible(false);
                        rdoCanReceiveExcessInstal_No.setVisible(false);
                        rdoIntPayableOnExcessInstal_Yes.setVisible(false);
                        rdoIntPayableOnExcessInstal_No.setVisible(false);
                        panAgentCommision.setVisible(true);
                        panMinimumPeriod.setVisible(true);
                        lblDailyDepositCalc.setVisible(true);
                        lblWeekly.setVisible(true);
                        panDailyIntCalc.setVisible(true);
                        cboWeekly.setVisible(false);
                        lblWeekly.setVisible(false);
                        //                        rdoWeeklyActionPerformed(null);
                        //                        rdoMonthlyActionPerformed(null);
                        //                        rdoDailyActionPerformed(null);
                        //                        cboMonthlyIntCalcMethod.setVisible(false);
                        panPartialWithdrawlAllowedForDD.setVisible(true);
                        lblPartialWithdrawlAllowedForDD.setVisible(true);
                    } else {
                        lblDelayedChargesDet.setVisible(false);
                        tabDepositsProduct.remove(panFloatingRateAccount);
                        //                        tabDepositsProduct.resetVisits();
                    }
                    if (hash.get("OPERATES LIKE").equals("CUMMULATIVE")) {
                        tabDepositsProduct.add(panRenewalParameter);
                        tabDepositsProduct.setTitleAt(3, "Renewal Parameters");
                    }
                    if (hash.get("OPERATES LIKE").equals("FLOATING_RATE")) {
                        tabDepositsProduct.add(panRenewalParameter);
                        tabDepositsProduct.setTitleAt(3, "Renewal Parameters");
                    }
                    if (hash.get("OPERATES LIKE").equals("FIXED")) {
                        tabDepositsProduct.add(panRenewalParameter);
                        tabDepositsProduct.setTitleAt(3, "Renewal Parameters");
                        lblDiscounted.setVisible(true);
                        lblDiscounted.setEnabled(true);
                        panDiscounted.setVisible(true);
                        panDiscounted.setEnabled(true);
                    } else {

                        lblMaxNopfTimes1.setVisible(false);
                        txtMaxNopfTimes1.setVisible(false);
                        lblDiscounted.setVisible(false);
                        lblDiscounted.setEnabled(false);
                        panDiscounted.setVisible(false);
                        panDiscounted.setEnabled(false);
                    }
                    if (observable.getRdoAutoRenewalAllowed_No() == true) {
                        txtMaxNopfTimes.setText("");
                    }
                    txtLastAcctNumber.setEnabled(false);
                    txtLastAcctNumber.setEditable(false);
                    txtAcctNumberPattern.setEditable(false);
                    txtSuffix.setEditable(false);
                }
                if (viewType == AUTHORIZE) {
                    rdoPartialWithdrawalAllowed_Yes.setEnabled(false);
                    rdoPartialWithdrawalAllowed_No.setEnabled(false);
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    txtMaxDepositPeriod.setEnabled(false);
                    cboMaxDepositPeriod.setEnabled(false);
                    txtPeriodInMultiplesOf.setEnabled(false);
                    cboPeriodInMultiplesOf.setEnabled(false);

                }
                if (viewType == COPY) {
                    btnAcctHead.setEnabled(true);
                    txtProductID.setEnabled(true);
                    txtProductID.setEditable(true);
                    txtDesc.setEnabled(true);
                    txtDesc.setEditable(true);
                    panAccHead.setEnabled(true);
                    btnCopy.setEnabled(false);
                    enableDisableRecurring(true);
                    enableWeeklySlabTxt(true);
                }

                btnCopy.setEnabled(false);
                if (observable.getChkRateAsOnDateOfRenewal() == true) {
                    cboEitherofTwoRatesChoose.setEnabled(false);
                    chkRateAsOnDateOfMaturity.setEnabled(false);
                } else if (observable.getChkRateAsOnDateOfMaturity() == true) {
                    chkRateAsOnDateOfRenewal.setEnabled(false);
                    cboEitherofTwoRatesChoose.setEnabled(false);
                } else if (observable.getRdoInterestrateappliedoverdue_Yes() == true) {
                    enableDisableRenewal(false);
                } else if (observable.getRdoInterestrateappliedoverdue_No() == true) {
                    enableDisableRenewal(true);
                }
                if (observable.isRdoDepositRate()) {
                    //lblMinPerToBeTreatAsFD.setVisible(true);
                    // txtMinPerToBeTreatAsFD.setVisible(true);
                    //txtMinPerToBeTreatAsFD.setText(CommonUtil.convertObjToStr(observable.getTxtMinPerToBeTreatAsFD()));
                    lblDepositsProdFixd.setVisible(true);
                    cboDepositsProdFixd.setVisible(true);
                    cboDepositsProdFixd.setSelectedItem(observable.getCbmDepositsProdFixd());
                    if (hash!=null && hash.containsKey("OPERATES LIKE") && hash.get("OPERATES LIKE").equals("CUMMULATIVE")) {
                        cboPreMatIntType.setVisible(true);
                        lblpreMatIntType.setVisible(true);
                        cboPreMatIntType.setSelectedItem(observable.getCboPreMatIntType());
                    }
                }else if( observable.isRdoSBRate()) {
                   lblDepositsProdFixd.setVisible(true);
                   cmbSbProduct.setVisible(true);
                   cmbSbProduct.setSelectedItem(observable.getCbmSbProduct());
                }
                // Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
                if(hash.get("OPERATES LIKE").equals("RECURRING") || hash.get("OPERATES LIKE").equals("DAILY")){
                    chkIsGroupDeposit.setVisible(true);
                }else{
                    chkIsGroupDeposit.setVisible(false);
                }
                if (chkAgentCommSlabRequired.isSelected()) {                   
                    panAgentCommCalcMethod.setVisible(true);
                    panAgentCommSlab.setVisible(true);
                    srpAgentCommSlabSettings.setVisible(true);
                }
            } else if (viewType == FIXEDDEPOSIT) {
                txtFixedDepositAcctHead.setText(ACCOUNTHEAD);
                txtFixedDepositAcctHeadDesc.setText(ACCOUNTHEADDESC);
                txtFixedDepositAcctHeadDesc.setEnabled(false);
                
            } else if (viewType == ACCHEAD) {
                txtAcctHeadForFloatAcct.setText(ACCOUNTHEAD);
                txtAcctHeadForFloatAcctDesc.setText(ACCOUNTHEADDESC);
                txtAcctHeadForFloatAcctDesc.setEnabled(false);
            } else if (viewType == INTPROVMAT) {
                txtIntProvisionOfMaturedDeposit.setText(ACCOUNTHEAD);
                txtIntProvisionOfMaturedDepositDesc.setText(ACCOUNTHEADDESC);
                txtIntProvisionOfMaturedDepositDesc.setEnabled(false);
            } else if (viewType == INTONMAT) {
                txtIntOnMaturedDepositAcctHead.setText(ACCOUNTHEAD);
                txtIntOnMaturedDepositAcctHeadDesc.setText(ACCOUNTHEADDESC);
                txtIntOnMaturedDepositAcctHeadDesc.setEnabled(false);
            } else if (viewType == MATDEPOSIT) {
                txttMaturityDepositAcctHead.setText(ACCOUNTHEAD);
                txttMaturityDepositAcctHeadDesc.setText(ACCOUNTHEADDESC);
                txttMaturityDepositAcctHeadDesc.setEnabled(false);
            } else if (viewType == INTDEPPLHD) {
                txtIntDebitPLHead.setText(ACCOUNTHEAD);
                txtIntDebitPLHeadDesc.setText(ACCOUNTHEADDESC);
                txtIntDebitPLHeadDesc.setEnabled(false);
            } else if (viewType == INTPAYGLHEAD) {
                txtIntPaybleGLHead.setText(ACCOUNTHEAD);
                txtIntPaybleGLHeadDesc.setText(ACCOUNTHEADDESC);
                txtIntPaybleGLHeadDesc.setEnabled(false);
            } else if (viewType == INTPROVACCHD) {
                txtIntProvisioningAcctHd.setText(ACCOUNTHEAD);
                txtIntProvisioningAcctHdDesc.setText(ACCOUNTHEADDESC);
                txtIntProvisioningAcctHdDesc.setEnabled(false);
            } else if (viewType == DEPACCHD) {
                txtAcctHd.setText(ACCOUNTHEAD);
            } else if (viewType == TDSGLACCTHD) {
                txtTDSGLAcctHd.setText(ACCOUNTHEAD);
                txtTDSGLAcctHdDesc.setText(ACCOUNTHEADDESC);
                txtTDSGLAcctHdDesc.setEnabled(false);
            } else if (viewType == COMMISIONPAYGLHEAD) {
                txtCommisionPaybleGLHead.setText(ACCOUNTHEAD);
                txtCommisionPaybleGLHeadDesc.setText(ACCOUNTHEADDESC);
                txtCommisionPaybleGLHeadDesc.setEnabled(false);
            } else if (viewType == RDPENALACHD) {
                txtPenalCharges.setText(ACCOUNTHEAD);
                txtPenalChargesDesc.setText(ACCOUNTHEADDESC);
                txtPenalChargesDesc.setEnabled(false);
            } else if (viewType == TRANSFEROUTACHD) {
                txtTransferOutHead.setText(ACCOUNTHEAD);
            } else if (viewType == POSTAGEACHD) {
                txtTransferOutHeadDesc.setText(ACCOUNTHEADDESC);
                txtTransferOutHeadDesc.setEnabled(false);
            } else if (viewType == INTRSTRECOVRYACHD) {
                txtInterestRecoveryAcHd.setText(ACCOUNTHEAD);
                txtInterestRecoveryAcHdDesc.setText(ACCOUNTHEADDESC);
                txtInterestRecoveryAcHdDesc.setEnabled(false);
            }else if (viewType == BENEVOLENTINTRESERVE) {
                txtBenIntReserveHead.setText(ACCOUNTHEAD);
                txtBenIntReserveHeadDesc.setText(ACCOUNTHEADDESC);
                txtBenIntReserveHeadDesc.setEnabled(false);
            }
        }

        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void enableDisableRenewal(boolean value) {
        lblBasedOnDepRate.setVisible(value);
        lblRateAsOnDateOfRenewal.setVisible(value);
        chkRateAsOnDateOfRenewal.setVisible(value);
        lblRateAsOnDateOfMaturity.setVisible(value);
        chkRateAsOnDateOfMaturity.setVisible(value);
        lblBothRateAvail.setVisible(value);
        cboEitherofTwoRatesChoose.setVisible(value);
        lblOneRateAvail.setVisible(value);
        panOneRateAvail.setVisible(value);
        lblBothRateNotAvail.setVisible(value);
        panBothRateNotAvail.setVisible(value);

    }

     private void enableDisableRecurring(boolean value) {
        txtMaturityDateAfterLastInstalPaid.setEnabled(value);
        rdoInsBeyondMaturityDt_Yes.setEnabled(value);
        rdoInsBeyondMaturityDt_No.setEnabled(value);
        rdoLastInstallmentAllowed_Yes.setEnabled(value);
        rdoLastInstallmentAllowed_No.setEnabled(value);
        cboMaturityDateAfterLastInstalPaid.setEnabled(value);
        rdoRecurringDepositToFixedDeposit_Yes.setEnabled(value);
        rdoRecurringDepositToFixedDeposit_No.setEnabled(value);
        rdoInstallmentInRecurringDepositAcct_Yes.setEnabled(value);
        rdoInstallmentInRecurringDepositAcct_No.setEnabled(value);
        cboInstallmentToBeCharged.setEnabled(value);
        cboChangeValue.setEnabled(value);
        cboDepositsFrequency.setEnabled(value);
        rdoCanReceiveExcessInstal_yes.setEnabled(value);
        rdoCanReceiveExcessInstal_No.setEnabled(value);
        rdoIntPayableOnExcessInstal_Yes.setEnabled(value);
        rdoIntPayableOnExcessInstal_No.setEnabled(value);
        cboCutOffDayForPaymentOfInstal.setEnabled(value);
        txtCutOffDayForPaymentOfInstal.setEnabled(value);
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setEnabled(value);
        rdoPenaltyOnLateInstallmentsChargeble_No.setEnabled(value);
        txtRDIrregularIfInstallmentDue.setEnabled(value);
        txtMinimumPeriod.setEnabled(value);
        cboMinimumPeriod.setEnabled(value);
        txtAgentCommision.setEnabled(value);
        cboAgentCommision.setEnabled(value);
        txtInterestNotPayingValue.setEnabled(value);
        cboInterestNotPayingValue.setEnabled(value);
        cboMonthlyIntCalcMethod.setEnabled(value);
        rdoDaily.setEnabled(value);
        rdoWeekly.setEnabled(value);
        rdoMonthly.setEnabled(value);
        cboWeekly.setEnabled(value);
        rdoPartialWithdrawlAllowedForDD_Yes.setEnabled(value);
        rdoPartialWithdrawlAllowedForDD_No.setEnabled(value);
        rdoIncaseOfIrregularRDSBRate.setEnabled(value);
        rdoIncaseOfIrregularRDRBRate.setEnabled(value);
        chkIntApplicationSlab.setEnabled(value);
        rdoPenalRounded_Yes.setEnabled(value);
        rdoPenalRounded_No.setEnabled(value);
        cboRoundOffCriteriaPenal.setEnabled(value);
        chkWeeklySpecial.setEnabled(value);
		rdoMonthEnd.setEnabled(value);
        rdoInstallmentDay.setEnabled(value);
        chkRdNature.setEnabled(value);
        
    }

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        btnDeleteActionPerformed(evt);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        btnEditActionPerformed(evt);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        btnSaveActionPerformed(evt);
    }//GEN-LAST:event_mitSaveActionPerformed

    //To enable and/or Disable buttons(folder)...
    private void enableDisableButtons(boolean value) {
        btnAcctHead.setEnabled(value);
        btnFixedDepositAcctHead.setEnabled(value);
        btnPenalCharges.setEnabled(value);
        btnAcctHeadForFloatAcct.setEnabled(value);
        btnIntProvisionOfMaturedDeposit.setEnabled(value);
        btnIntOnMaturedDepositAcctHead.setEnabled(value);
        btnMaturityDepositAcctHead.setEnabled(value);
        btnIntDebitPLHead.setEnabled(value);
        btnIntPaybleGLHead.setEnabled(value);
        btnIntProvisioningAcctHd.setEnabled(value);

        btnTDSGLAcctHd.setEnabled(value);
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        //Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panScheme);
        StringBuffer strBAlert = new StringBuffer();
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        }
        else{
        if (txtInterestRecoveryAcHd.getText().equals("") || txtInterestRecoveryAcHd.getText() == null) {
            ClientUtil.displayAlert("Please select Interest Recovery Head");
            return;
        }
        if (chkDifferentROI.isSelected()) {
            if (rdoDepositRate.isSelected() == false && rdoSBRate.isSelected() == false && cboOperatesLike.getSelectedItem().equals("Cummulative")) {
                ClientUtil.displayAlert("Please select ROI to be applied in case of Premature closer");
                return;
            }
        }
        if (rdoPenaltyOnLateInstallmentsChargeble_Yes.isSelected() == true && cboOperatesLike.getSelectedItem().equals("Recurring") && txtRDIrregularIfInstallmentDue.getText().equals("")) {
            ClientUtil.displayAlert("Please Enter Minimum installment due for penalty");
            return;
        }
        if (rdoIncaseOfIrregularRDSBRate.isSelected() == false && cboOperatesLike.getSelectedItem().equals("Recurring") && rdoIncaseOfIrregularRDRBRate.isSelected() == false) {
            ClientUtil.displayAlert("Please select ROI to be applied in case of irregular RD");
            return;
        }
        if (rdoPenalRounded_Yes.isSelected()) {
            System.out.println("cboRoundOffCriteriaPenal.getSelectedIndex()" + cboRoundOffCriteriaPenal.getSelectedIndex());
            if (cboRoundOffCriteriaPenal.getSelectedIndex() < 0) {
                ClientUtil.displayAlert("Please select Round Off Criteria for penal");
                return;
            }
        }
        updateOBFields();
        boolean value = true;
        value = observable.sortData();

        
        
        if (value == false) {
            return;
        }
        //        if(cboMaxDepositPeriodRule().length() > 0){
        //            strBAlert.append(cboMaxDepositPeriodRule()+"\n");
        //        }
        if (txtInterestRecoveryAcHd.getText().equals("") || txtInterestRecoveryAcHd.getText() == null) {
            ClientUtil.displayAlert("Please select Interest Recovery Head");
            return;
        }
        if (txtMaxDepositAmtRule().length() > 0) {
            strBAlert.append(txtMaxDepositAmtRule() + "\n");
        }
        if (txtWithdrawalsInMultiplesOfRule().length() > 0) {
            strBAlert.append(txtWithdrawalsInMultiplesOfRule() + "\n");
        }
        if (txtMinAmtOfPartialWithdrawalAllowedRule().length() > 0) {
            strBAlert.append(txtMinAmtOfPartialWithdrawalAllowedRule() + "\n");
        }
        if (txtMaxNoOfPartialWithdrawalAllowedRule().length() > 0) {
            strBAlert.append(txtMaxNoOfPartialWithdrawalAllowedRule() + "\n");
        }
        if (txtMaxAmtOfPartialWithdrawalAllowedRule().length() > 0) {
            strBAlert.append(txtMaxAmtOfPartialWithdrawalAllowedRule() + "\n");
        }
        if (txtCutOffDayForPaymentOfInstalRule().length() > 0) {
            strBAlert.append(txtCutOffDayForPaymentOfInstalRule() + "\n");
        }
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0) {
            displayAlert(strBAlert.toString());
        } else if (!(isProdIdExist(txtProductID.getText(), txtDesc.getText()))) {            
            
            //        }else{
            if (cboOperatesLike.getSelectedItem().equals("Daily") && (txtMinimumPeriod.getText().length() == 0 || cboMinimumPeriod.getSelectedIndex() == 0)) {
                ClientUtil.showAlertWindow("Premature closing account period should not be empty");
                return;
            } else if (cboOperatesLike.getSelectedItem().equals("Daily") && (txtAgentCommision.getText().length() == 0 || cboAgentCommision.getSelectedIndex() == 0)) {
                ClientUtil.showAlertWindow("Agent commision recovered should be proper value");
                return;
            } else if (cboOperatesLike.getSelectedItem().equals("Daily") && (txtInterestNotPayingValue.getText().length() == 0 || cboInterestNotPayingValue.getSelectedIndex() == 0)) {
                ClientUtil.showAlertWindow("premature closing interest period should be proper value");
                return;
            } else {
                if(cboOperatesLike.getSelectedItem().equals("Thrift") || cboOperatesLike.getSelectedItem().equals("Benevolent")){ // // Added by nithya on 02-03-2016 for 0003897
                  if(tdtEffectiveDate.getDateValue().length() > 0 && DateUtil.getDateMMDDYYYY(tdtEffectiveDate.getDateValue()) != null)
                  {          
                    boolean verify = observable.verifyProdEffectiveDate(txtProductID.getText(),DateUtil.getDateMMDDYYYY(tdtEffectiveDate.getDateValue()));
                    if (verify) {
                      ClientUtil.showAlertWindow("installment set on this date");
                    return;
                  }
                }
              }
                savePerformed();
            }
        }
        }
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        txtInterestRecoveryAcHd.setText("");
        txtInterestRecoveryAcHdDesc.setText("");
        txtBenIntReserveHeadDesc.setText("");
        txtBenIntReserveHead.setText("");
        cboInstType.setSelectedItem("");
        chkIntApplicationSlab.setText("");
        txtCategoryBenifitRate.setText("");
        // Added by nithya on 02-03-2016 for 0003897
        tdtEffectiveDate.setDateValue(""); 
        txtInstallmentAmount.setText(""); 
        observable.deleteThriftBenevolentTableData();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    public boolean isProdIdExist(String strProdId, String strProdDesc) {
        boolean exist = false;
        if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) || (observable.getActionType() == ClientConstants.ACTIONTYPE_COPY)) {
            int option = -1;
            final HashMap transactionMap = new HashMap();
            //            strProdId.toUpperCase();
            //            txtProductID.setText(strProdId.toUpperCase());
            HashMap retrieve = new HashMap();
            transactionMap.put("prodId", strProdId);
            transactionMap.put("prodDesc", strProdDesc);
            java.util.List resultList = ClientUtil.executeQuery("getDepositProdIdCount", transactionMap);
            if (Integer.parseInt(((HashMap) resultList.get(0)).get("NO_ID").toString()) > 0) {
                ClientUtil.showMessageWindow(resourceBundle.getString("PRODIDWARNING"));
                exist = true;
            }
        }
        return exist;
    }

    /** To perform all actions for Save functionality when the mandatory conditions are satisfied
     */
    private void savePerformed() {
        //        updateOBFields();
        observable.doAction();

        //__ if the Action is not Falied, Reset the fields...
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            //            super.removeEditLock(txtProductID.getText());
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("PRODUCT ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getProxyReturnMap() != null) {
                if (observable.getProxyReturnMap().containsKey("PRODUCT ID")) {
                    lockMap.put("PRODUCT ID", observable.getProxyReturnMap().get("PROD_ID"));
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                lockMap.put("PRODUCT ID", observable.getTxtProductID());
            }

            setEditLockMap(lockMap);
            setEditLock();

            observable.resetForm();
            ClientUtil.enableDisable(this, false);
            setButtonEnableDisable();
            enableDisableButtons(false);
            observable.setResultStatus();
            //            observable.setInterestTabTitle();
            observable.resetDataList();
            observable.resetTable();
            //        observable.resetDataList();
            observable.resetOBFields();
            observable.resetTableRecords();
            updateTable();
            observable.resetWeeklySettings();
            resetWeeklyTxt();
            observable.resetAgentCommissionSlabSettings();
            resetAgentCommissionTxt();
            rdoMonthEnd.setSelected(false);
            rdoInstallmentDay.setSelected(false);
            //__ Make the Screen Closable..
            setModified(false);
        }

    }
   
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        btnCancelActionPerformed(evt);
    }//GEN-LAST:event_mitCancelActionPerformed
    public void clearDesc() {
        chkExcludeLienFrmIntrstAppl.setSelected(false);
        chkExcludeLienFrmStanding.setSelected(false);
        chkDepositUnlien.setSelected(false);

        chkCummCertPrint.setSelected(false);
        txtIntProvisioningAcctHdDesc.setText("");
        txtIntPaybleGLHeadDesc.setText("");
        txtCommisionPaybleGLHeadDesc.setText("");
        txtIntDebitPLHeadDesc.setText("");
        txttMaturityDepositAcctHeadDesc.setText("");
        txtIntOnMaturedDepositAcctHeadDesc.setText("");
        txtAcctHeadForFloatAcctDesc.setText("");
        txtFixedDepositAcctHeadDesc.setText("");
        txtPenalChargesDesc.setText("");
        txtTransferOutHeadDesc.setText("");
        txtTDSGLAcctHdDesc.setText("");
        txtIntProvisionOfMaturedDepositDesc.setText("");
        chkIntEditable.setSelected(false);
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        //        super.removeEditLock(txtProductID.getText());
        clearDesc();
        if (observable.getAuthorizeStatus() != null) {
            super.removeEditLock(txtProductID.getText());
        }

        observable.resetForm();
        observable.resetDelayed();
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        enableDisablePeriod(true);
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        enableDisableButtons(false);
        btnCommisionPaybleGLHead.setEnabled(false);
        //        disableButtons();
        //        observable.setInterestTabTitle();
        isFilled = false;
        observable.setStatus();
        observable.resetTable();
        //        observable.resetDataList();
        observable.resetOBFields();
        observable.resetTableRecords();
        tabDepositsProduct.remove(panRD);
        tabDepositsProduct.remove(panFloatingRateAccount);
        tabDepositsProduct.remove(panRenewalParameter);
        tabDepositsProduct.resetVisits();
        //__ Make the Screen Closable..
        setModified(false);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnCopy.setEnabled(true);
        txtTransferOutHead.setEnabled(false);
        btnTransferOutHead.setEnabled(false);
        rdoWithPeriod_No.setSelected(false);
        rdoWithPeriod_Yes.setSelected(false);
        rdoDoublingScheme_No.setSelected(false);
        rdoDoublingScheme_Yes.setSelected(false);
        lblPrematureClosureApply.setVisible(false);
        lblPrematureClosureApplyROI.setVisible(false);
        lblDifferentROI.setVisible(false);
        chkDifferentROI.setVisible(false);
        chkPrematureClosure.setVisible(false);
        chkIntApplicationSlab.setSelected(false);
        chkWeeklySpecial.setSelected(false);
        chkRdNature.setEnabled(false);
        lblPrematureClosure.setVisible(false);
        lblInstType.setVisible(false);
        cboInstType.setVisible(false);
        cboInstType.setSelectedItem("");
        panPrematureClosureApply.setVisible(false);
        lblFDRenewalSameNoTranPrincAmt.setVisible(false);
        chkFDRenewalSameNoTranPrincAmt.setVisible(false);
        lblDepositsProdFixd.setVisible(false);
        cboDepositsProdFixd.setVisible(false);
        chkExcludeLienFrmIntrstAppl.setSelected(false);
        chkExcludeLienFrmStanding.setSelected(false);
        chkDepositUnlien.setSelected(false);

        chkCummCertPrint.setSelected(false);
        txtInterestRecoveryAcHd.setText("");
        txtInterestRecoveryAcHdDesc.setText("");
        txtBenIntReserveHead.setText("");
        txtBenIntReserveHeadDesc.setText("");
        txtCategoryBenifitRate.setText("");
        chkIntEditable.setSelected(false);
        cboPreMatIntType.setVisible(false);
        lblpreMatIntType.setVisible(false);
        txtDoublingCount.setVisible(false);
        txtDoublingCount.setText("");
        
        if(panWeeklyDepositSlab.isShowing()){
            enableWeeklySlabTxt(false);
            observable.resetWeeklySettings();
            updateTable();
            tblWeeklySlabSettings.removeAll();
            resetWeeklyTxt();
        }
        enableDisableRenewal(false);
        rdoMonthEnd.setSelected(false);
        rdoInstallmentDay.setEnabled(false);
        
        if(panAgenCommissionSlab.isShowing()){
            enableAgentCommissionSlabTxt(false);
            observable.resetAgentCommissionSlabSettings();
            updateTable();
            tblAgentCommSlabSettings.removeAll();
            resetAgentCommissionTxt();
        }
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(this, true);
        //        disableTextBox();
        setButtonEnableDisable();
        enableDisableButtons(true);
        txtReset();                         // To reset the fields associated with radio buttons...
        renewalParameterReset();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        btnCommisionPaybleGLHead.setEnabled(true);
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            txtLastAcctNumber.setEnabled(true);
            txtLastAcctNumber.setEditable(true);
        } else {
            txtLastAcctNumber.setEnabled(false);
            txtLastAcctNumber.setEditable(false);
        }

        //__ To Save the data in the Internal Frame...
        setModified(true);
        tabNewButtons();
        btnEnableDisplay();
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnCopy.setEnabled(false);
        btnTabNew.setEnabled(false);
        btnTabSave.setEnabled(false);
        btnTabDelete.setEnabled(false);
        txtFromAmt.setEnabled(false);
        txtToAmt.setEnabled(false);
        txtCommisionRate.setEnabled(false);
        if (observable.getAuthorizeStatus() != null) {
            super.removeEditLock(txtProductID.getText());
        }
        rdoWithPeriod_No.setSelected(false);
        rdoWithPeriod_Yes.setSelected(false);
        rdoDoublingScheme_No.setSelected(false);
        rdoDoublingScheme_Yes.setSelected(false);
        txtDoublingCount.setVisible(false);
        txtDoublingCount.setText("");
        disDesc();
        chkExcludeLienFrmIntrstAppl.setSelected(false);
        chkExcludeLienFrmStanding.setSelected(false);
        chkDepositUnlien.setSelected(false);

        chkCummCertPrint.setSelected(false);
        txtInterestRecoveryAcHd.setText("");
        txtInterestRecoveryAcHdDesc.setText("");
        txtBenIntReserveHead.setText("");
        txtBenIntReserveHeadDesc.setText("");
        rdoPenalRounded_No.setSelected(true);
        cboRoundOffCriteriaPenal.setVisible(false);
        lblRoundOffCriteriaPenal.setVisible(false);
        
        sbRateCmb.setVisible(false);
   
        //id generation
        HashMap idMap = generateID();
        System.out.println("idMap+++"+idMap);
        if (idMap != null && idMap.size() > 0)
            txtProductID.setText((String) idMap.get(CommonConstants.DATA));
        chkIntEditable.setSelected(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        btnNewActionPerformed(evt);
    }//GEN-LAST:event_mitNewActionPerformed

    private void chkDifferentROIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDifferentROIActionPerformed
        // TODO add your handling code here:
        if (chkDifferentROI.isSelected()) {
            enableDisableCummulativeDeposit(true);
        } else {
            enableDisableCummulativeDeposit(false);
            removeCummulativeRadioBtns();
            rdoDepositRate.setSelected(false);
            rdoSBRate.setSelected(false);
            addCummulativeRadioBtns();

            ((ComboBoxModel) cboDepositsProdFixd.getModel()).setKeyForSelected("");
            ((ComboBoxModel) cboPreMatIntType.getModel()).setKeyForSelected("");
        }

    }//GEN-LAST:event_chkDifferentROIActionPerformed

    private void cboOperatesLikeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboOperatesLikeFocusLost
        // TODO add your handling code here:
        if (cboOperatesLike.getSelectedItem().equals("Fixed")) {
            chkExcludeLienFrmIntrstAppl.setEnabled(true);
            chkExcludeLienFrmStanding.setEnabled(true);
        } else {
            chkExcludeLienFrmIntrstAppl.setEnabled(false);
            chkExcludeLienFrmStanding.setEnabled(false);
        }

        if (cboOperatesLike.getSelectedItem().equals("Cummulative")) {
            chkCummCertPrint.setEnabled(true);
            // chkCummCertPrint.setEnabled(true);
        } else {
            chkCummCertPrint.setEnabled(false);
            // chkExcludeLienFrmStanding.setEnabled(false);
        }

    }//GEN-LAST:event_cboOperatesLikeFocusLost

    private void txtInterestRecoveryAcHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInterestRecoveryAcHdFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInterestRecoveryAcHdFocusLost

    private void btnInterestRecoveryAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestRecoveryAcHdActionPerformed
        // TODO add your handling code here:
        popUp(INTRSTRECOVRYACHD);
    }//GEN-LAST:event_btnInterestRecoveryAcHdActionPerformed

    private void rdoPenalRounded_YesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoPenalRounded_YesItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoPenalRounded_YesItemStateChanged

    private void rdoPenalRounded_NoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdoPenalRounded_NoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoPenalRounded_NoItemStateChanged

    private void rdoPenalRounded_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalRounded_YesActionPerformed
        // TODO add your handling code here:
        cboRoundOffCriteriaPenal.setVisible(true);
        lblRoundOffCriteriaPenal.setVisible(true);
    }//GEN-LAST:event_rdoPenalRounded_YesActionPerformed

    private void rdoPenalRounded_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalRounded_NoActionPerformed
        // TODO add your handling code here:
        cboRoundOffCriteriaPenal.setSelectedIndex(-1);
        cboRoundOffCriteriaPenal.setVisible(false);
        lblRoundOffCriteriaPenal.setVisible(false);
    }//GEN-LAST:event_rdoPenalRounded_NoActionPerformed

private void rdoIntProvisioningApplicable_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIntProvisioningApplicable_NoActionPerformed
    // Add your handling code here:
    if (rdoIntProvisioningApplicable_No.isSelected()) {
        cboIntProvisioningFreq.setSelectedItem("");
        cboIntProvisioningFreq.setEnabled(false);
        cboIntProvisioningFreq.setEditable(false);

        cboProvisioningLevel.setSelectedItem("");
        cboProvisioningLevel.setEnabled(false);
        cboProvisioningLevel.setEditable(false);
    }
}//GEN-LAST:event_rdoIntProvisioningApplicable_NoActionPerformed

private void rdoIntProvisioningApplicable_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIntProvisioningApplicable_YesActionPerformed
    // Add your handling code here:
    cboIntProvisioningFreq.setEnabled(true);
    cboProvisioningLevel.setEnabled(true);
}//GEN-LAST:event_rdoIntProvisioningApplicable_YesActionPerformed

private void rdoInterestrateappliedoverdue_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestrateappliedoverdue_YesActionPerformed
    if(rdoInterestrateappliedoverdue_Yes.isSelected()){
        sbRateCmb.setVisible(true);
    }
        
}//GEN-LAST:event_rdoInterestrateappliedoverdue_YesActionPerformed

private void rdoInterestrateappliedoverdue_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInterestrateappliedoverdue_NoActionPerformed
if(rdoInterestrateappliedoverdue_No.isSelected()){
        sbRateCmb.setVisible(false);
    }
}//GEN-LAST:event_rdoInterestrateappliedoverdue_NoActionPerformed

    private void validateWeeklyDepTxtFields() {
        txtInstallmentFrom.setAllowNumber(true);
        txtInstallmentTo.setAllowNumber(true);
        txtPenal.setAllowNumber(true);
        txtInstallmentNo.setAllowNumber(true);
    }

    private void cboDepositsFrequencyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboDepositsFrequencyFocusLost
        // TODO add your handling code here:
        if (cboDepositsFrequency.getSelectedItem().equals("Weekly") || cboDepositsFrequency.getSelectedItem().equals("Installments")) {// Added installments by nithya on 23-01-2017 for 0005664
            ClientUtil.enableDisable(panWeeklyDepositSlab, true);
            tabDepositsProduct.addTab("Weekly Deposit Slab", panWeeklyDepositSlab);
            validateWeeklyDepTxtFields();
            observable.setProductType();
            if (observable.getCbmProductType() != null) {
                cboProductType.setModel(observable.getCbmProductType());
            }
            enableWeeklySlabTxt(false);
            btnWeeklyDepSlabNew.setEnabled(true);
            btnWeeklyDepSlabSave.setEnabled(false);
            btnWeeklyDepSlabDelete.setEnabled(false);
        } else {
            ClientUtil.enableDisable(panWeeklyDepositSlab, false);
            tabDepositsProduct.remove(panWeeklyDepositSlab);
        }
    }//GEN-LAST:event_cboDepositsFrequencyFocusLost
    
    private void txtInstallmentFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstallmentFromFocusLost
        // TODO add your handling code here:
        int selRow = 0;
        selRow = tblWeeklySlabSettings.getSelectedRow();
        int rowCount = tblWeeklySlabSettings.getRowCount();
        if (rowCount > 0 && selRow > 0) {
            int instTo = 0;
            instTo = CommonUtil.convertObjToInt(tblWeeklySlabSettings.getValueAt(selRow - 1, 2));
            int instFrom = 0;
            instFrom = CommonUtil.convertObjToInt(txtInstallmentFrom.getText());
            if (instFrom < instTo) {
                ClientUtil.showMessageWindow("Please enter a value greater than " + instTo);
                txtInstallmentFrom.setText("");
            }
        }
    }//GEN-LAST:event_txtInstallmentFromFocusLost

    private void txtInstallmentToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInstallmentToFocusLost
        // TODO add your handling code here:
        int instFrom = 0;
        instFrom = CommonUtil.convertObjToInt(txtInstallmentFrom.getText());
        if (txtInstallmentFrom.getText().equals("")) {
            ClientUtil.showMessageWindow("Please enter the Installment From..!!");
            txtInstallmentTo.setText("");
        } else {
            int instTo = 0;
            instTo = CommonUtil.convertObjToInt(txtInstallmentTo.getText());
            if (instFrom >= instTo) {
                ClientUtil.showMessageWindow("Please enter a value greater than " + instFrom);
                txtInstallmentTo.setText("");
            }
        }
    }//GEN-LAST:event_txtInstallmentToFocusLost

    private void enableWeeklySlabTxt(boolean flag) {
        cboProductType.setEnabled(flag);
        txtInstallmentFrom.setEnabled(flag);
        txtInstallmentTo.setEnabled(flag);
        txtInstallmentNo.setEnabled(flag);
        txtPenal.setEnabled(flag);
    }
    
    private void tblWeeklySlabSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblWeeklySlabSettingsMouseClicked
        // TODO add your handling code here:
        this.selectedSingleRow = true;
        chkTblSelected = true;
        int rowCount = 0;
        int selRow = 0;
        rowCount = tblWeeklySlabSettings.getRowCount();
        selRow = tblWeeklySlabSettings.getSelectedRow();
        if (rowCount >= 0) {
            updateTableData();
            this.selectedSingleRow = false;
            btnWeeklyDepSlabSave.setEnabled(true);
            btnWeeklyDepSlabDelete.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("There are no records to display!!!");
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_COPY) {
            enableWeeklySlabTxt(true);
            enableDisableRecurring(true);
        } else {
            enableWeeklySlabTxt(false);
        }
    }//GEN-LAST:event_tblWeeklySlabSettingsMouseClicked

    private void updateTableData() {
        this.selectedSingleRow = true;
        observable.populateTableData(tblWeeklySlabSettings.getSelectedRow());
        cboProductType.setSelectedItem(observable.getCboProductType());
        txtInstallmentFrom.setText(observable.getTxtInstallmentFrom());
        txtInstallmentTo.setText(observable.getTxtInstallmentTo());
        txtInstallmentNo.setText(observable.getTxtInstallmentNo());
        txtPenal.setText(observable.getTxtPenal());
    }
 
    private void btnWeeklyDepSlabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeeklyDepSlabNewActionPerformed
        this.selectedSingleRow = true;
        chkTblSelected = false;
        enableWeeklySlabTxt(true);
        resetWeeklyTxt();
        btnWeeklyDepSlabNew.setEnabled(false);
        btnWeeklyDepSlabSave.setEnabled(true);
        btnWeeklyDepSlabDelete.setEnabled(false);
    }//GEN-LAST:event_btnWeeklyDepSlabNewActionPerformed

    private void btnWeeklyDepSlabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeeklyDepSlabSaveActionPerformed
        if (cboProductType.getSelectedItem().equals("") || txtInstallmentFrom.getText().equals("") || txtInstallmentTo.getText().equals("")) {
            ClientUtil.showMessageWindow("Please enter all the necessary details!!!");
        } else {
            int rowCount = tblWeeklySlabSettings.getRowCount();
            observable.setCboDepositsFrequency((String) cboDepositsFrequency.getSelectedItem());// Added by nithya on 23-01-2017 for 0005664
            updateWeeklyDepositFields();
            if (!this.selectedSingleRow) {
                int rowSelected = this.tblWeeklySlabSettings.getSelectedRow();
                observable.insertIntoTableData(rowSelected);
            } else {
                observable.insertIntoTableData(-1);
            }
            resetWeeklyTxt();
            updateTable();
            btnWeeklyDepSlabSave.setEnabled(false);
            btnWeeklyDepSlabNew.setEnabled(true);
            btnWeeklyDepSlabDelete.setEnabled(false);
            enableWeeklySlabTxt(false);
            enableDisableRecurring(true);
        }
    }//GEN-LAST:event_btnWeeklyDepSlabSaveActionPerformed

     public void updateWeeklyDepositFields() {
        observable.setTxtProductID(txtProductID.getText());
        observable.setCboProductType(CommonUtil.convertObjToStr(cboProductType.getSelectedItem()));
        observable.setTxtInstallmentFrom(txtInstallmentFrom.getText());
        observable.setTxtInstallmentTo(txtInstallmentTo.getText());
        observable.setTxtInstallmentNo(txtInstallmentNo.getText());
        observable.setTxtPenal(txtPenal.getText());
    }
    
     private void resetWeeklyTxt() {
        txtInstallmentFrom.setText("");
        txtInstallmentTo.setText("");
        txtInstallmentNo.setText("");
        txtPenal.setText("");
        cboProductType.setSelectedItem("");
    }

    private void updateTable() {
        this.tblWeeklySlabSettings.setModel(observable.getTbmWeeklySlabSettings());
        this.tblWeeklySlabSettings.revalidate();
        // Added by nithya on 02-03-2016 for 0003897
        this.tblThriftBenevelontDetails.setModel(observable.getTbmThriftBenevolent());
        this.tblThriftBenevelontDetails.revalidate();
        
        this.tblAgentCommSlabSettings.setModel(observable.getTbmAgentCommSlabSettings());
        this.tblAgentCommSlabSettings.revalidate();
    }
    
    private void btnWeeklyDepSlabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeeklyDepSlabDeleteActionPerformed
        // TODO add your handling code here:
        int rowSelected = this.tblWeeklySlabSettings.getSelectedRow();
        if (rowSelected >= 0) {
            observable.deleteTblData(rowSelected);
            this.updateTable();
            resetWeeklyTxt();
        } else {
            ClientUtil.showMessageWindow("Please select a row to delete!!!");
        }
        btnWeeklyDepSlabNew.setEnabled(true);
    }//GEN-LAST:event_btnWeeklyDepSlabDeleteActionPerformed

    private void tdtEffectiveDateAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tdtEffectiveDateAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtEffectiveDateAncestorAdded

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
        // Added by nithya on 19/05/2016
//        HashMap whereMap = new HashMap();
//        whereMap.put("PROD_ID",txtProductID.getText());
//        List lst = ClientUtil.executeQuery("getAllProductIds",whereMap);
//        System.out.println("getSBODBorrowerEligAmt : " + lst);
//        if(lst != null && lst.size() > 0){
//            HashMap existingProdIdMap = (HashMap)lst.get(0);
//            if(existingProdIdMap.containsKey("PROD_ID")){
//                ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") +"\n Please change"); 
//                txtProductID.setText("");
//            }
//        }
        
    }//GEN-LAST:event_txtProductIDFocusLost

    private void txtDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDescFocusGained
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
    }//GEN-LAST:event_txtDescFocusGained

    private void btnBenIntReserveHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBenIntReserveHeadActionPerformed
        // TODO add your handling code here:
        popUp(BENEVOLENTINTRESERVE);
    }//GEN-LAST:event_btnBenIntReserveHeadActionPerformed

    private void cboprmatureCloseRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboprmatureCloseRateActionPerformed
        // TODO add your handling code here:
        if (cboprmatureCloseRate.getSelectedIndex() > 0) {
            String closureType = "PREMATURE";
            String prodType = CommonUtil.convertObjToStr(cboprmatureCloseRate.getSelectedItem());
            if(prodType.equalsIgnoreCase("SB Rate")){
                prodType = "OA";
            }else if(prodType.equalsIgnoreCase("Deposit Rate")){
                prodType = "TD";
            }
            Boolean dataExists = observable.populateROIProductCombo(prodType, closureType);
            if (dataExists) {
                cboPrmatureCloseProduct.setModel(observable.getCbmPrmatureCloseProduct());
            } else {
                cboPrmatureCloseProduct.removeAllItems();
            }
        }
    }//GEN-LAST:event_cboprmatureCloseRateActionPerformed

    private void cboIrregularRDCloseRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIrregularRDCloseRateActionPerformed
        // TODO add your handling code here:
         if (cboIrregularRDCloseRate.getSelectedIndex() > 0) {
            String closureType = "NORMAL";
            String prodType = CommonUtil.convertObjToStr(cboIrregularRDCloseRate.getSelectedItem());
            if(prodType.equalsIgnoreCase("SB Rate")){
                prodType = "OA";
            }else if(prodType.equalsIgnoreCase("Deposit Rate")){
                prodType = "TD";
            }
            Boolean dataExists = observable.populateROIProductCombo(prodType, closureType);
            if (dataExists) {
                cboIrregularRDCloseProduct.setModel(observable.getCbmIrregularRDCloseProduct());
            } else {
                cboIrregularRDCloseProduct.removeAllItems();
            }
        }
    }//GEN-LAST:event_cboIrregularRDCloseRateActionPerformed

    private void chkRDClosingOtherROIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRDClosingOtherROIActionPerformed
        // TODO add your handling code here:
        if(chkRDClosingOtherROI.isSelected()){
            panROIPrematureClosure.setVisible(true);
            panROIIrregularRDClosure.setVisible(true);
            chkIntForIrregularRD.setVisible(true);
            chkIntForIrregularRD.setEnabled(true); // Added by nithya on 18-03-2020 for KD-1535
        }else{
            panROIPrematureClosure.setVisible(false);
            panROIIrregularRDClosure.setVisible(false);
            chkIntForIrregularRD.setEnabled(false); // Added by nithya on 18-03-2020 for KD-1535
            chkIntForIrregularRD.setVisible(false);
        }
    }//GEN-LAST:event_chkRDClosingOtherROIActionPerformed

    private void txtAgentCommSlabAmtFromFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAgentCommSlabAmtFromFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAgentCommSlabAmtFromFocusLost

    private void TxtAgentCommSlabAmtToFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_TxtAgentCommSlabAmtToFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtAgentCommSlabAmtToFocusLost

    private void btnAgentCommissionSlabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentCommissionSlabNewActionPerformed
        // TODO add your handling code here:
        this.selectedSingleRowAgentSlab = true;
        chkTblAgentCommSelected = false;
        enableAgentCommissionSlabTxt(true);
        resetAgentCommissionTxt();
        btnAgentCommissionSlabNew.setEnabled(false);
        btnAgentCommissionSlabSave.setEnabled(true);
        btnAgentCommissionSlabDelete.setEnabled(false);
    }//GEN-LAST:event_btnAgentCommissionSlabNewActionPerformed

    private void enableAgentCommissionSlabTxt(boolean flag) {   
        txtAgentCommSlabAmtFrom.setEnabled(flag);
        TxtAgentCommSlabAmtTo.setEnabled(flag);
        txtAgentCommSlabPercent.setEnabled(flag);   
    }
    
      private void resetAgentCommissionTxt() {
        txtAgentCommSlabAmtFrom.setText("");
        TxtAgentCommSlabAmtTo.setText("");
        txtAgentCommSlabPercent.setText("");      
    }
    
    public void updateAgentCommissionSlabFields() {      
        observable.setTxtAgentCommSlabAmtFrom(txtAgentCommSlabAmtFrom.getText());
        observable.setTxtAgentCommSlabAmtTo(TxtAgentCommSlabAmtTo.getText());
        observable.setTxtAgentCommSlabPercent(txtAgentCommSlabPercent.getText());      
    }
  
    
    private void btnAgentCommissionSlabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentCommissionSlabSaveActionPerformed
            int rowCount = tblAgentCommSlabSettings.getRowCount();           
            updateAgentCommissionSlabFields();
            if (!this.selectedSingleRowAgentSlab) {
                int rowSelected = this.tblAgentCommSlabSettings.getSelectedRow();
                observable.insertIntoAgentCommissionSlabTableData(rowSelected);
            } else {
                observable.insertIntoAgentCommissionSlabTableData(-1);
            }
            resetAgentCommissionTxt();
            updateTable();
            btnAgentCommissionSlabSave.setEnabled(false);
            btnAgentCommissionSlabNew.setEnabled(true);
            btnAgentCommissionSlabDelete.setEnabled(false);
            enableAgentCommissionSlabTxt(false); 
            panAgenCommissionSlab.setFocusable(true);
    }//GEN-LAST:event_btnAgentCommissionSlabSaveActionPerformed

    private void btnAgentCommissionSlabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgentCommissionSlabDeleteActionPerformed
        // TODO add your handling code here:
        int rowSelected = this.tblAgentCommSlabSettings.getSelectedRow();
        if (rowSelected >= 0) {
            observable.deleteAgentCommissionSlabTblData(rowSelected);
            this.updateTable();
            resetAgentCommissionTxt();
        } else {
            ClientUtil.showMessageWindow("Please select a row to delete!!!");
        }
        btnAgentCommissionSlabNew.setEnabled(true);
    }//GEN-LAST:event_btnAgentCommissionSlabDeleteActionPerformed

    private void tblAgentCommSlabSettingsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgentCommSlabSettingsMouseClicked
        // TODO add your handling code here:
        this.selectedSingleRowAgentSlab = true;
        chkTblAgentCommSelected = true;
        int rowCount = 0;
        int selRow = 0;
        rowCount = tblAgentCommSlabSettings.getRowCount();
        selRow = tblAgentCommSlabSettings.getSelectedRow();
        if (rowCount >= 0) {
            updateAgentCommissionSlabTableData();
            this.selectedSingleRowAgentSlab = false;
            btnAgentCommissionSlabSave.setEnabled(true);
            btnAgentCommissionSlabDelete.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("There are no records to display!!!");
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_COPY) {
            enableAgentCommissionSlabTxt(true);
            enableDisableRecurring(true);
        } else {
            enableAgentCommissionSlabTxt(false);
        }
    }//GEN-LAST:event_tblAgentCommSlabSettingsMouseClicked

    private void chkAgentCommSlabRequiredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAgentCommSlabRequiredActionPerformed
        // TODO add your handling code here:
        if(chkAgentCommSlabRequired.isSelected()){
            panAgentCommCalcMethod.setVisible(true);
            panAgentCommSlab.setVisible(true);
            srpAgentCommSlabSettings.setVisible(true);
        }else{
            panAgentCommCalcMethod.setVisible(false);
            panAgentCommSlab.setVisible(false);
            srpAgentCommSlabSettings.setVisible(false);  
        }
    }//GEN-LAST:event_chkAgentCommSlabRequiredActionPerformed

    private void rdoBothRateNotAvail_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBothRateNotAvail_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoBothRateNotAvail_NoActionPerformed

    private void updateAgentCommissionSlabTableData() {
        this.selectedSingleRowAgentSlab = true;
        observable.populateAgentCommissionSlabSettingsTableData(tblAgentCommSlabSettings.getSelectedRow());        
        txtAgentCommSlabAmtFrom.setText(observable.getTxtAgentCommSlabAmtFrom());
        TxtAgentCommSlabAmtTo.setText(observable.getTxtAgentCommSlabAmtTo());
        txtAgentCommSlabPercent.setText(observable.getTxtAgentCommSlabPercent());        
    }
    
    
    
    private void enableDisableCummulativeDeposit(boolean flag) {
        rdoDepositRate.setEnabled(flag);
        rdoSBRate.setEnabled(flag);
        cboDepositsProdFixd.setEnabled(flag);
        cboPreMatIntType.setEditable(flag);
    }
    /*To set model for combo boxes*/

    private void initComponentData() {
        cboOperatesLike.setModel(observable.getCbmOperatesLike());
        cboInstType.setModel(observable.getCbmInstType());
        cboRoundOffCriteria.setModel(observable.getCbmRoundOffCriteria());
        cboRoundOffCriteriaPenal.setModel(observable.getCbmRoundOffCriteria());
        cboMaturityInterestType.setModel(observable.getCbmMaturityInterestType());
        cboMaturityInterestRate.setModel(observable.getCbmMaturityInterestRate());
        //        cboInterestCriteria.setModel(observable.getCbmInterestCriteria());
        //        cboDepositPerUnit.setModel(observable.getCbmDepositPerUnit());
        cboMinDepositPeriod.setModel(observable.getCbmMinDepositPeriod());
        cboMaxDepositPeriod.setModel(observable.getCbmMaxDepositPeriod());
        cboPeriodInMultiplesOf.setModel(observable.getCbmPeriodInMultiplesOf());
        cboPrematureWithdrawal.setModel(observable.getCbmPrematureWithdrawal());
        cboIntProvisioningFreq.setModel(observable.getCbmIntProvisioningFreq());
        cboIntCompoundingFreq.setModel(observable.getCbmIntCompoundingFreq());
        cboIntApplnFreq.setModel(observable.getCbmIntApplnFreq());
        cboIntMaintainedAsPartOf.setModel(observable.getCbmIntMaintainedAsPartOf());
        cboIntCalcMethod.setModel(observable.getCbmIntCalcMethod());
        cboProvisioningLevel.setModel(observable.getCbmProvisioningLevel());
        cboNoOfDays.setModel(observable.getCbmNoOfDays());

        cboIntPeriodForBackDatedRenewal.setModel(observable.getCbmIntPeriodForBackDatedRenewal());
        cboMaxPeriodMDt.setModel(observable.getCbmMaxMDt());
        cboMinNoOfDays.setModel(observable.getCbmMinNoOfDays());

        cboIntType.setModel(observable.getCbmIntType());
        cboIntCriteria.setModel(observable.getCbmIntCriteria());

        cboInstallmentToBeCharged.setModel(observable.getCbmInstallmentToBeCharged());
        cboChangeValue.setModel(observable.getCbmChangeValue());
        cboProdCurrency.setModel(observable.getCbmProdCurrency());

        //added for daily deposits scheme
        cboDepositsFrequency.setModel(observable.getCbmDepositsFrequency());

        cboIntRoundOff.setModel(observable.getCbmIntRoundOff());
        cboMaturityDateAfterLastInstalPaid.setModel(observable.getCbmMaturityDateAfterLastInstalPaid());
        cboAgentCommision.setModel(observable.getCbmAgentCommision());
        cboInterestNotPayingValue.setModel(observable.getCbmInterestNotPayingValue());
        cboMinimumPeriod.setModel(observable.getCbmMinimumPeriod());
        cboCutOffDayForPaymentOfInstal.setModel(observable.getCbmCutOffDayForPaymentOfInstal());
        cboInterestType.setModel(observable.getCbmInterestType());

        //        cboFromAmount.setModel(observable.getCbmFromAmount());
        //        cboToAmount.setModel(observable.getCbmToAmount());
        cboFromPeriod.setModel(observable.getCbmFromPeriod());
        cboToPeriod.setModel(observable.getCbmToPeriod());
        cboWeekly.setModel(observable.getCbmWeekly());
        cboMonthlyIntCalcMethod.setModel(observable.getCbmMonthlyIntCalcMethod());
        txtCommisionRate.setText(observable.getTxtRateInterest());
        //        tdtDate.

        tblInterestTable.setModel(observable.getTblInterestTable());
        tblDelayedInstallmet.setModel(observable.getTblDelayedInstallmet());
        cboMinimumRenewalDeposit.setModel(observable.getCbmMinimumRenewalDeposit());
        cboRenewedclosedbefore.setModel(observable.getCbmRenewedclosedbefore());
        cboEitherofTwoRatesChoose.setModel(observable.getCbmEitherofTwoRatesChoose());
        lblProvisionOfInterest.setVisible(false);
        rdoProvisionOfInterest_Yes.setVisible(false);
        rdoProvisionOfInterest_No.setVisible(false);
        cboDepositsProdFixd.setModel(observable.getCbmDepositsProdFixd());
        sbRateCmb.setModel(observable.getCbmSbrateModel());
        cboPreMatIntType.setModel(observable.getCbmPreMatIntTypeModel());
        cmbSbProduct.setModel(observable.getCbmSbProduct());
        
       // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
        cboprmatureCloseRate.setModel(observable.getCbmprmatureCloseRate()); 
        cboPrmatureCloseProduct.setModel(observable.getCbmPrmatureCloseProduct());
        cboIrregularRDCloseRate.setModel(observable.getCbmIrregularRDCloseRate());
        cboIrregularRDCloseProduct.setModel(observable.getCbmIrregularRDCloseProduct());    
        
        cboAgentCommCalcMethod.setModel(observable.getCbmAgentCommCalcMethod());
    }

    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {

        rdgAdjustIntOnDeposits.remove(rdoAdjustIntOnDeposits_No);
        rdgAdjustIntOnDeposits.remove(rdoAdjustIntOnDeposits_Yes);

        rdgAdjustPrincipleToLoan.remove(rdoAdjustPrincipleToLoan_No);
        rdgAdjustPrincipleToLoan.remove(rdoAdjustPrincipleToLoan_Yes);

        rdgAmountRounded.remove(rdoAmountRounded_No);
        rdgAmountRounded.remove(rdoAmountRounded_Yes);

        rdgAutoAdjustment.remove(rdoAutoAdjustment_No);
        rdgAutoAdjustment.remove(rdoAutoAdjustment_Yes);

        rdgAutoRenewalAllowed.remove(rdoAutoRenewalAllowed_No);
        rdgAutoRenewalAllowed.remove(rdoAutoRenewalAllowed_Yes);

        rdgSameNoDepositAllowed.remove(rdoSameNoRenewalAllowed_No);
        rdgSameNoDepositAllowed.remove(rdoSameNoRenewalAllowed_Yes);

        rdgCalcMaturityValue.remove(rdoCalcMaturityValue_No);
        rdgCalcMaturityValue.remove(rdoCalcMaturityValue_Yes);

        rdgCalcTDS.remove(rdoCalcTDS_No);
        rdgCalcTDS.remove(rdoCalcTDS_Yes);

        rdgDiscounted_Rate.remove(rdoDiscounted_No);
        rdgDiscounted_Rate.remove(rdoDiscounted_Yes);

        rdgCanReceiveExcessInstal.remove(rdoCanReceiveExcessInstal_No);
        rdgCanReceiveExcessInstal.remove(rdoCanReceiveExcessInstal_yes);

        rdgExtnOfDepositBeforeMaturity.remove(rdoExtnOfDepositBeforeMaturity_No);
        rdgExtnOfDepositBeforeMaturity.remove(rdoExtnOfDepositBeforeMaturity_Yes);

        rdgFlexiFromSBCA.remove(rdoFlexiFromSBCA_No);
        rdgFlexiFromSBCA.remove(rdoFlexiFromSBCA_Yes);

        rdgInstallmentInRecurringDepositAcct.remove(rdoInstallmentInRecurringDepositAcct_No);
        rdgInstallmentInRecurringDepositAcct.remove(rdoInstallmentInRecurringDepositAcct_Yes);

        rdgIntPayableOnExcessInstal.remove(rdoIntPayableOnExcessInstal_No);
        rdgIntPayableOnExcessInstal.remove(rdoIntPayableOnExcessInstal_Yes);

        rdgIntProvisioningApplicable.remove(rdoIntProvisioningApplicable_No);
        rdgIntProvisioningApplicable.remove(rdoIntProvisioningApplicable_Yes);

        rdgInterestAfterMaturity.remove(rdoInterestAfterMaturity_No);
        rdgInterestAfterMaturity.remove(rdoInterestAfterMaturity_Yes);

        rdgIntroducerReqd.remove(rdoIntroducerReqd_No);
        rdgIntroducerReqd.remove(rdoIntroducerReqd_Yes);

        rdgLastInstallmentAllowed.remove(rdoLastInstallmentAllowed_No);
        rdgLastInstallmentAllowed.remove(rdoLastInstallmentAllowed_Yes);

        rdgPartialWithdrawalAllowed.remove(rdoPartialWithdrawalAllowed_No);
        rdgPartialWithdrawalAllowed.remove(rdoPartialWithdrawalAllowed_Yes);

        rdgPayInterestOnHoliday.remove(rdoPayInterestOnHoliday_No);
        rdgPayInterestOnHoliday.remove(rdoPayInterestOnHoliday_Yes);

        rdgPenaltyOnLateInstallmentsChargeble.remove(rdoPenaltyOnLateInstallmentsChargeble_No);
        rdgPenaltyOnLateInstallmentsChargeble.remove(rdoPenaltyOnLateInstallmentsChargeble_Yes);

        rdgProvisionOfInterest.remove(rdoProvisionOfInterest_No);
        rdgProvisionOfInterest.remove(rdoProvisionOfInterest_Yes);

        rdgRecalcOfMaturityValue.remove(rdoRecalcOfMaturityValue_No);
        rdgRecalcOfMaturityValue.remove(rdoRecalcOfMaturityValue_Yes);

        rdgRecurringDepositToFixedDeposit.remove(rdoRecurringDepositToFixedDeposit_No);
        rdgRecurringDepositToFixedDeposit.remove(rdoRecurringDepositToFixedDeposit_Yes);

        rdgRenewalOfDepositAllowed.remove(rdoRenewalOfDepositAllowed_No);
        rdgRenewalOfDepositAllowed.remove(rdoRenewalOfDepositAllowed_Yes);

        rdgSystemCalcValues.remove(rdoSystemCalcValues_No);
        rdgSystemCalcValues.remove(rdoSystemCalcValues_Yes);
        rdgSystemCalcValues.remove(rdoSystemCalcValues_Print_accOpen);

        rdgTermDeposit.remove(rdoTermDeposit_No);
        rdgTermDeposit.remove(rdoTermDeposit_Yes);

        rdgTransferToMaturedDeposits.remove(rdoTransferToMaturedDeposits_No);
        rdgTransferToMaturedDeposits.remove(rdoTransferToMaturedDeposits_Yes);

        rdgWithdrawalWithInterest.remove(rdoWithdrawalWithInterest_No);
        rdgWithdrawalWithInterest.remove(rdoWithdrawalWithInterest_Yes);

        rdgServiceCharge.remove(rdoServiceCharge_Yes);
        rdgServiceCharge.remove(rdoServiceCharge_No);

        rdgInsBeyondMaturityDt.remove(rdoInsBeyondMaturityDt_Yes);
        rdgInsBeyondMaturityDt.remove(rdoInsBeyondMaturityDt_No);

        rdgTypesOfDeposit.remove(rdoRegular);
        rdgTypesOfDeposit.remove(rdoNRO);
        rdgTypesOfDeposit.remove(rdoNRE);

        rdgStaffAccount.remove(rdoStaffAccount_Yes);
        rdgStaffAccount.remove(rdoStaffAccount_No);

        //        rdgIntRateforOverduePeriod.remove(rdoInterestrateappliedoverdue_Yes);
        //        rdgIntRateforOverduePeriod.remove(rdoInterestrateappliedoverdue_No);


        rdgDailyDepositCalculation.remove(rdoDaily);
        rdgDailyDepositCalculation.remove(rdoWeekly);
        rdgDailyDepositCalculation.remove(rdoMonthly);
        rdgPartialWithdrawlAllowedForDD.remove(rdoPartialWithdrawalAllowed_Yes);
        rdgPartialWithdrawlAllowedForDD.remove(rdoPartialWithdrawalAllowed_No);
        //        rdgclosedwithinperiod.remove(rdoClosedwithinperiod_Yes);
        //        rdgInterestalreadyPaid.remove(rdoInterestalreadyPaid_Yes);
        //        rdgInterestalreadyPaid.remove(rdoInterestalreadyPaid_No);
        rdgclosedwithinperiod.remove(rdoClosedwithinperiod_Yes);
        rdgclosedwithinperiod.remove(rdoClosedwithinperiod_No);

        rdgInterestalreadyPaid.remove(rdoInterestalreadyPaid_Yes);
        rdgInterestalreadyPaid.remove(rdoInterestalreadyPaid_No);

        rdgIntRateforOverduePeriod.remove(rdoInterestrateappliedoverdue_Yes);
        rdgIntRateforOverduePeriod.remove(rdoInterestrateappliedoverdue_No);

        rdgOneRateAvail.remove(rdoSBRateOneRate_Yes);
        rdgOneRateAvail.remove(rdoSBRateOneRate_No);

        rdgBothRateNotAvail.remove(rdoBothRateNotAvail_Yes);
        rdgBothRateNotAvail.remove(rdoBothRateNotAvail_No);

        rdgirregularRdApply.remove(rdoIncaseOfIrregularRDSBRate);
        rdgirregularRdApply.remove(rdoIncaseOfIrregularRDRBRate);

        removeCummulativeRadioBtns();


        rdgExtensionDepositBeyond.remove(rdoExtensionBeyondOriginalDate_No);
        rdgExtensionDepositBeyond.remove(rdoExtensionBeyondOriginalDate_Yes);

        rdgPenalIntOnWithdrawal.remove(rdoExtensionPenal_Yes);
        rdgPenalIntOnWithdrawal.remove(rdoExtensionPenal_No);
   
        rdgDueOn.remove(rdoMonthEnd);
        rdgDueOn.remove(rdoInstallmentDay);

    }

    // b.) To Add the Radio buttons...
    private void addRadioButtons() {

        rdgAdjustIntOnDeposits = new CButtonGroup();
        rdgAdjustIntOnDeposits.add(rdoAdjustIntOnDeposits_No);
        rdgAdjustIntOnDeposits.add(rdoAdjustIntOnDeposits_Yes);

        rdgAdjustPrincipleToLoan = new CButtonGroup();
        rdgAdjustPrincipleToLoan.add(rdoAdjustPrincipleToLoan_No);
        rdgAdjustPrincipleToLoan.add(rdoAdjustPrincipleToLoan_Yes);

        rdgAmountRounded = new CButtonGroup();
        rdgAmountRounded.add(rdoAmountRounded_No);
        rdgAmountRounded.add(rdoAmountRounded_Yes);

        rdgAutoAdjustment = new CButtonGroup();
        rdgAutoAdjustment.add(rdoAutoAdjustment_No);
        rdgAutoAdjustment.add(rdoAutoAdjustment_Yes);

        rdgAutoRenewalAllowed = new CButtonGroup();
        rdgAutoRenewalAllowed.add(rdoAutoRenewalAllowed_No);
        rdgAutoRenewalAllowed.add(rdoAutoRenewalAllowed_Yes);

        rdgSameNoDepositAllowed = new CButtonGroup();
        rdgSameNoDepositAllowed.add(rdoSameNoRenewalAllowed_No);
        rdgSameNoDepositAllowed.add(rdoSameNoRenewalAllowed_Yes);

        rdgCalcMaturityValue = new CButtonGroup();
        rdgCalcMaturityValue.add(rdoCalcMaturityValue_No);
        rdgCalcMaturityValue.add(rdoCalcMaturityValue_Yes);

        rdgCalcTDS = new CButtonGroup();
        rdgCalcTDS.add(rdoCalcTDS_No);
        rdgCalcTDS.add(rdoCalcTDS_Yes);

        rdgclosedwithinperiod = new CButtonGroup();
        rdgclosedwithinperiod.add(rdoClosedwithinperiod_Yes);
        rdgclosedwithinperiod.add(rdoClosedwithinperiod_No);
        //        rdgInterestalreadyPaid = new CButtonGroup();
        //        rdgInterestalreadyPaid.add(rdoInterestalreadyPaid_Yes);
        //        rdgInterestalreadyPaid.add(rdoInterestalreadyPaid_No);

        rdgDiscounted_Rate = new CButtonGroup();
        rdgDiscounted_Rate.add(rdoDiscounted_No);
        rdgDiscounted_Rate.add(rdoDiscounted_Yes);

        rdgCanReceiveExcessInstal = new CButtonGroup();
        rdgCanReceiveExcessInstal.add(rdoCanReceiveExcessInstal_No);
        rdgCanReceiveExcessInstal.add(rdoCanReceiveExcessInstal_yes);

        rdgExtnOfDepositBeforeMaturity = new CButtonGroup();
        rdgExtnOfDepositBeforeMaturity.add(rdoExtnOfDepositBeforeMaturity_No);
        rdgExtnOfDepositBeforeMaturity.add(rdoExtnOfDepositBeforeMaturity_Yes);

        rdgFlexiFromSBCA = new CButtonGroup();
        rdgFlexiFromSBCA.add(rdoFlexiFromSBCA_No);
        rdgFlexiFromSBCA.add(rdoFlexiFromSBCA_Yes);

        rdgInstallmentInRecurringDepositAcct = new CButtonGroup();
        rdgInstallmentInRecurringDepositAcct.add(rdoInstallmentInRecurringDepositAcct_No);
        rdgInstallmentInRecurringDepositAcct.add(rdoInstallmentInRecurringDepositAcct_Yes);

        rdgIntPayableOnExcessInstal = new CButtonGroup();
        rdgIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_No);
        rdgIntPayableOnExcessInstal.add(rdoIntPayableOnExcessInstal_Yes);

        rdgIntProvisioningApplicable = new CButtonGroup();
        rdgIntProvisioningApplicable.add(rdoIntProvisioningApplicable_No);
        rdgIntProvisioningApplicable.add(rdoIntProvisioningApplicable_Yes);

        rdgInterestAfterMaturity = new CButtonGroup();
        rdgInterestAfterMaturity.add(rdoInterestAfterMaturity_No);
        rdgInterestAfterMaturity.add(rdoInterestAfterMaturity_Yes);

        rdgIntroducerReqd = new CButtonGroup();
        rdgIntroducerReqd.add(rdoIntroducerReqd_No);
        rdgIntroducerReqd.add(rdoIntroducerReqd_Yes);

        rdgIntRateforOverduePeriod = new CButtonGroup();
        rdgIntRateforOverduePeriod.add(rdoInterestrateappliedoverdue_Yes);
        rdgIntRateforOverduePeriod.add(rdoInterestrateappliedoverdue_No);

        rdgLastInstallmentAllowed = new CButtonGroup();
        rdgLastInstallmentAllowed.add(rdoLastInstallmentAllowed_No);
        rdgLastInstallmentAllowed.add(rdoLastInstallmentAllowed_Yes);

        rdgPartialWithdrawalAllowed = new CButtonGroup();
        rdgPartialWithdrawalAllowed.add(rdoPartialWithdrawalAllowed_No);
        rdgPartialWithdrawalAllowed.add(rdoPartialWithdrawalAllowed_Yes);

        rdgPayInterestOnHoliday = new CButtonGroup();
        rdgPayInterestOnHoliday.add(rdoPayInterestOnHoliday_No);
        rdgPayInterestOnHoliday.add(rdoPayInterestOnHoliday_Yes);

        rdgPenaltyOnLateInstallmentsChargeble = new CButtonGroup();
        rdgPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_No);
        rdgPenaltyOnLateInstallmentsChargeble.add(rdoPenaltyOnLateInstallmentsChargeble_Yes);

        rdgProvisionOfInterest = new CButtonGroup();
        rdgProvisionOfInterest.add(rdoProvisionOfInterest_No);
        rdgProvisionOfInterest.add(rdoProvisionOfInterest_Yes);

        rdgRecalcOfMaturityValue = new CButtonGroup();
        rdgRecalcOfMaturityValue.add(rdoRecalcOfMaturityValue_No);
        rdgRecalcOfMaturityValue.add(rdoRecalcOfMaturityValue_Yes);

        rdgRecurringDepositToFixedDeposit = new CButtonGroup();
        rdgRecurringDepositToFixedDeposit.add(rdoRecurringDepositToFixedDeposit_No);
        rdgRecurringDepositToFixedDeposit.add(rdoRecurringDepositToFixedDeposit_Yes);

        rdgRenewalOfDepositAllowed = new CButtonGroup();
        rdgRenewalOfDepositAllowed.add(rdoRenewalOfDepositAllowed_No);
        rdgRenewalOfDepositAllowed.add(rdoRenewalOfDepositAllowed_Yes);

        rdgSystemCalcValues = new CButtonGroup();
        rdgSystemCalcValues.add(rdoSystemCalcValues_No);
        rdgSystemCalcValues.add(rdoSystemCalcValues_Yes);
        rdgSystemCalcValues.add(rdoSystemCalcValues_Print_accOpen);

        rdgTermDeposit = new CButtonGroup();
        rdgTermDeposit.add(rdoTermDeposit_No);
        rdgTermDeposit.add(rdoTermDeposit_Yes);

        rdgTransferToMaturedDeposits = new CButtonGroup();
        rdgTransferToMaturedDeposits.add(rdoTransferToMaturedDeposits_No);
        rdgTransferToMaturedDeposits.add(rdoTransferToMaturedDeposits_Yes);

        rdgWithdrawalWithInterest = new CButtonGroup();
        rdgWithdrawalWithInterest.add(rdoWithdrawalWithInterest_No);
        rdgWithdrawalWithInterest.add(rdoWithdrawalWithInterest_Yes);

        rdgServiceCharge = new CButtonGroup();
        rdgServiceCharge.add(rdoServiceCharge_Yes);
        rdgServiceCharge.add(rdoServiceCharge_No);

        rdgInsBeyondMaturityDt = new CButtonGroup();
        rdgInsBeyondMaturityDt.add(rdoInsBeyondMaturityDt_Yes);
        rdgInsBeyondMaturityDt.add(rdoInsBeyondMaturityDt_No);

        rdgTypesOfDeposit = new CButtonGroup();
        rdgTypesOfDeposit.add(rdoRegular);
        rdgTypesOfDeposit.add(rdoNRO);
        rdgTypesOfDeposit.add(rdoNRE);

        rdgStaffAccount = new CButtonGroup();
        rdgStaffAccount.add(rdoStaffAccount_Yes);
        rdgStaffAccount.add(rdoStaffAccount_No);

        rdgDailyDepositCalculation = new CButtonGroup();
        rdgDailyDepositCalculation.add(rdoDaily);
        rdgDailyDepositCalculation.add(rdoWeekly);
        rdgDailyDepositCalculation.add(rdoMonthly);

        rdgPartialWithdrawlAllowedForDD = new CButtonGroup();
        rdgPartialWithdrawlAllowedForDD.add(rdoPartialWithdrawlAllowedForDD_No);
        rdgPartialWithdrawlAllowedForDD.add(rdoPartialWithdrawlAllowedForDD_Yes);

        rdgInterestalreadyPaid = new CButtonGroup();
        rdgInterestalreadyPaid.add(rdoInterestalreadyPaid_Yes);
        rdgInterestalreadyPaid.add(rdoInterestalreadyPaid_No);

        rdgOneRateAvail = new CButtonGroup();
        rdgOneRateAvail.add(rdoSBRateOneRate_Yes);
        rdgOneRateAvail.add(rdoSBRateOneRate_No);

        rdgBothRateNotAvail = new CButtonGroup();
        rdgBothRateNotAvail.add(rdoBothRateNotAvail_Yes);
        rdgBothRateNotAvail.add(rdoBothRateNotAvail_No);

        rdgirregularRdApply = new CButtonGroup();
        rdgirregularRdApply.add(rdoIncaseOfIrregularRDSBRate);
        rdgirregularRdApply.add(rdoIncaseOfIrregularRDRBRate);


        addCummulativeRadioBtns();
        rdgExtensionDepositBeyond = new CButtonGroup();
        rdgExtensionDepositBeyond.add(rdoExtensionBeyondOriginalDate_No);
        rdgExtensionDepositBeyond.add(rdoExtensionBeyondOriginalDate_Yes);

        rdgPenalIntOnWithdrawal = new CButtonGroup();
        rdgPenalIntOnWithdrawal.add(rdoExtensionPenal_Yes);
        rdgPenalIntOnWithdrawal.add(rdoExtensionPenal_No);
       
        rdgDueOn = new CButtonGroup();
        rdgDueOn.add(rdoMonthEnd);
        rdgDueOn.add(rdoInstallmentDay);

    }

    private void addCummulativeRadioBtns() {
        rdgPrematureClosureApply = new CButtonGroup();
        rdgPrematureClosureApply.add(rdoDepositRate);
        rdgPrematureClosureApply.add(rdoSBRate);
    }

    private void removeCummulativeRadioBtns() {
        rdgPrematureClosureApply.remove(rdoDepositRate);
        rdgPrematureClosureApply.remove(rdoSBRate);
        
    }

    public String getAccHeadDesc(String accHeadID) {
        HashMap map1 = new HashMap();
        map1.put("ACCHD_ID", accHeadID);
        List list1 = ClientUtil.executeQuery("getSelectAcchdDesc", map1);
        if (!list1.isEmpty()) {
            HashMap map2 = new HashMap();
            map2 = (HashMap) list1.get(0);
            String accHeadDesc = map2.get("AC_HD_DESC").toString();
            return accHeadDesc;
        } else {
            return "";
        }
    }

    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        if (observable.getChkExcludeLienIntrstAppl().equals("Y") && observable.getCboOperatesLike().equals("Fixed")) {
            //System.out.println("jnnhnm111");
            chkExcludeLienFrmIntrstAppl.setSelected(true);
        } else {
            //System.out.println("jnnhnm222");
            chkExcludeLienFrmIntrstAppl.setSelected(false);
        }
        if (observable.getChkExcludeLienStanding().equals("Y") && observable.getCboOperatesLike().equals("Fixed")) {
            chkExcludeLienFrmStanding.setSelected(true);
        } else {
            chkExcludeLienFrmStanding.setSelected(false);
        }
        if (observable.getChkCummCertPrint().equals("Y") && observable.getCboOperatesLike().equals("Cummulative")) {
            chkCummCertPrint.setSelected(true);
        } else {
            chkCummCertPrint.setSelected(false);
        }
        if (observable.getChkDepositLien().equals("Y")) {
            chkDepositUnlien.setSelected(true);
        } else {
            chkDepositUnlien.setSelected(false);
        }
        if (observable.getChkIntEditable().equals("Y")) {
            chkIntEditable.setSelected(true);
        } else {
            chkIntEditable.setSelected(false);
        }
        if (observable.getChkCanZeroBalAct().equals("Y")) {
            chkCanZeroBalAct.setSelected(true);
        } else {
            chkCanZeroBalAct.setSelected(false);
        }

        if (observable.getChkPrematureClosure().equals("Y")) {
            chkPrematureClosure.setSelected(true);
        } else {
            chkPrematureClosure.setSelected(false);
        }
        if (observable.getChkIntApplicationSlab().equals("Y")) {
            chkIntApplicationSlab.setSelected(true);
        } else {
            chkIntApplicationSlab.setSelected(false);
        }
        if (observable.getChkWeeklySpecial().equals("Y")) {
            chkWeeklySpecial.setSelected(true);
        } else {
            chkWeeklySpecial.setSelected(false);
        }
        if (observable.getChkRdNature().equals("Y")) {
            chkRdNature.setSelected(true);
        } else {
            chkRdNature.setSelected(false);
        }
        if (CommonUtil.convertObjToStr(observable.getRdoPenalRounded_Yes()).equals("Y")) {
            rdoPenalRounded_Yes.setSelected(true);
        }
        if (CommonUtil.convertObjToStr(observable.getRdoPenalRounded_No()).equals("Y")) {
            rdoPenalRounded_No.setSelected(true);
        }
        txtAcctHd.setText(observable.getTxtAcctHd());
        txtProductID.setText(observable.getTxtProductID());
        txtDesc.setText(observable.getTxtDesc());
        cboOperatesLike.setSelectedItem(observable.getCboOperatesLike());
        cboProdCurrency.setSelectedItem(observable.getCboProdCurrency());
        rdoCalcTDS_Yes.setSelected(observable.getRdoCalcTDS_Yes());
        rdoCalcTDS_No.setSelected(observable.getRdoCalcTDS_No());
        rdoDiscounted_No.setSelected(observable.getDiscounted_No());
        rdoDiscounted_Yes.setSelected(observable.getDiscounted_Yes());
        rdoPayInterestOnHoliday_Yes.setSelected(observable.getRdoPayInterestOnHoliday_Yes());
        rdoPayInterestOnHoliday_No.setSelected(observable.getRdoPayInterestOnHoliday_No());
        rdoAmountRounded_Yes.setSelected(observable.getRdoAmountRounded_Yes());
        rdoAmountRounded_No.setSelected(observable.getRdoAmountRounded_No());
        rdoAmountRounded_NoItemStateChanged(null);

        if (CommonUtil.convertObjToStr(observable.getRdoWithPeriod()).equals("Y")) {
            rdoWithPeriod_Yes.setSelected(true);
        } else if (CommonUtil.convertObjToStr(observable.getRdoWithPeriod()).equals("N")) {
            rdoWithPeriod_No.setSelected(true);
        }
        if (CommonUtil.convertObjToStr(observable.getRdoDoublingScheme()).equals("Y")) {
            rdoDoublingScheme_Yes.setSelected(true);
            txtDoublingCount.setVisible(true);
            txtDoublingCount.setText(observable.getTxtDoublingCount());
        } else if (CommonUtil.convertObjToStr(observable.getRdoDoublingScheme()).equals("N")) {
            rdoDoublingScheme_No.setSelected(true);
            txtDoublingCount.setVisible(false);
            txtDoublingCount.setText("");
        }

        cboRoundOffCriteria.setSelectedItem(observable.getCboRoundOffCriteria());
        cboRoundOffCriteriaPenal.setSelectedItem(observable.getCboRoundOffCriteriaPenal());
        rdoInterestAfterMaturity_Yes.setSelected(observable.getRdoInterestAfterMaturity_Yes());
        rdoInterestAfterMaturity_No.setSelected(observable.getRdoInterestAfterMaturity_No());
        rdoInterestAfterMaturity_NoActionPerformed(null);

        cboMaturityInterestType.setSelectedItem(observable.getCboMaturityInterestType());
        cboInterestType.setSelectedItem(observable.getCboInterestType());
        cboMaturityInterestRate.setSelectedItem(observable.getCboMaturityInterestRate());
        //        cboInterestCriteria.setSelectedItem(observable.getCboInterestCriteria());
        txtMinDepositPeriod.setText(observable.getTxtMinDepositPeriod());
        txtMaxDepositPeriod.setText(observable.getTxtMaxDepositPeriod());
        cboMinDepositPeriod.setSelectedItem(observable.getCboMinDepositPeriod());
        cboPrematureWithdrawal.setSelectedItem(observable.getCboPrematureWithdrawal());
        cboMaxDepositPeriod.setSelectedItem(observable.getCboMaxDepositPeriod());
        txtPeriodInMultiplesOf.setText(observable.getTxtPeriodInMultiplesOf());
        cboPeriodInMultiplesOf.setSelectedItem(observable.getCboPeriodInMultiplesOf());
        txtDepositPerUnit.setText(observable.getTxtDepositPerUnit());
        //        cboDepositPerUnit.setSelectedItem(observable.getCboDepositPerUnit());
        txtAmtInMultiplesOf.setText(observable.getTxtAmtInMultiplesOf());
        txtMinDepositAmt.setText(observable.getTxtMinDepositAmt());
        txtMaxDepositAmt.setText(observable.getTxtMaxDepositAmt());
        rdoCalcMaturityValue_Yes.setSelected(observable.getRdoCalcMaturityValue_Yes());
        rdoCalcMaturityValue_No.setSelected(observable.getRdoCalcMaturityValue_No());
        rdoProvisionOfInterest_Yes.setSelected(observable.getRdoProvisionOfInterest_Yes());
        rdoProvisionOfInterest_No.setSelected(observable.getRdoProvisionOfInterest_No());
        rdoProvisionOfInterest_NoActionPerformed(null);

        txtInterestOnMaturedDeposits.setText(observable.getTxtInterestOnMaturedDeposits());
        txtAlphaSuffix.setText(observable.getTxtAlphaSuffix());
        txtMaxAmtOfCashPayment.setText(observable.getTxtMaxAmtOfCashPayment());
        txtMinAmtOfPAN.setText(observable.getTxtMinAmtOfPAN());
        txtAcctNumberPattern.setText(observable.getTxtAcctNumberPattern());
        txtLastAcctNumber.setText(observable.getTxtLastAcctNumber());
        txtSuffix.setText(observable.getTxtSuffix());
        rdoClosedwithinperiod_Yes.setSelected(observable.getRdoClosedwithinperiod_Yes());
        rdoClosedwithinperiod_No.setSelected(observable.getRdoClosedwithinperiod_No());
        //        txtScheme.setText(observable.getTxtScheme());
        txtLimitForBulkDeposit.setText(observable.getTxtLimitForBulkDeposit());
        rdoTransferToMaturedDeposits_Yes.setSelected(observable.getRdoTransferToMaturedDeposits_Yes());
        rdoTransferToMaturedDeposits_No.setSelected(observable.getRdoTransferToMaturedDeposits_No());
        rdoTransferToMaturedDeposits_NoActionPerformed(null);

        rdoAutoAdjustment_Yes.setSelected(observable.getRdoAutoAdjustment_Yes());

        rdoAutoAdjustment_No.setSelected(observable.getRdoAutoAdjustment_No());
        rdoAdjustIntOnDeposits_No.setSelected(observable.getRdoAdjustIntOnDeposits_No());
        rdoAdjustIntOnDeposits_Yes.setSelected(observable.getRdoAdjustIntOnDeposits_Yes());
        rdoAdjustPrincipleToLoan_Yes.setSelected(observable.getRdoAdjustPrincipleToLoan_Yes());
        rdoAdjustPrincipleToLoan_No.setSelected(observable.getRdoAdjustPrincipleToLoan_No());
        rdoExtnOfDepositBeforeMaturity_Yes.setSelected(observable.getRdoExtnOfDepositBeforeMaturity_Yes());
        rdoExtnOfDepositBeforeMaturity_No.setSelected(observable.getRdoExtnOfDepositBeforeMaturity_No());
        txtAfterHowManyDays.setText(observable.getTxtAfterHowManyDays());
        txtAdvanceMaturityNoticeGenPeriod.setText(observable.getTxtAdvanceMaturityNoticeGenPeriod());
        txtPrematureWithdrawal.setText(observable.getTxtPrematureWithdrawal());
        rdoFlexiFromSBCA_Yes.setSelected(observable.getRdoFlexiFromSBCA_Yes());
        rdoFlexiFromSBCA_No.setSelected(observable.getRdoFlexiFromSBCA_No());
        rdoTermDeposit_Yes.setSelected(observable.getRdoTermDeposit_Yes());
        rdoTermDeposit_No.setSelected(observable.getRdoTermDeposit_No());
        rdoIntroducerReqd_Yes.setSelected(observable.getRdoIntroducerReqd_Yes());
        rdoIntroducerReqd_No.setSelected(observable.getRdoIntroducerReqd_No());
        if (observable.getRdoCertificate_printing().length() > 0 && !observable.getRdoCertificate_printing().equals("")
                && observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
            if (observable.getRdoCertificate_printing().equals("A")) {
                rdoSystemCalcValues_Yes.setSelected(true);
            } else if (observable.getRdoCertificate_printing().equals("O")) {
                rdoSystemCalcValues_Print_accOpen.setSelected(true);
            } else if (observable.getRdoCertificate_printing().equals("S")) {
                rdoSystemCalcValues_No.setSelected(true);
            } else {
                rdoSystemCalcValues_Yes.setSelected(false);
                rdoSystemCalcValues_Print_accOpen.setSelected(false);
                rdoSystemCalcValues_No.setSelected(false);
            }
        }
        //        rdoSystemCalcValues_Yes.setSelected(observable.getRdoSystemCalcValues_Yes());
        //        rdoSystemCalcValues_No.setSelected(observable.getRdoSystemCalcValues_No());

        txtNoOfPartialWithdrawalAllowed.setText(observable.getTxtNoOfPartialWithdrawalAllowed());
        txtMaxNoOfPartialWithdrawalAllowed.setText(observable.getTxtMaxNoOfPartialWithdrawalAllowed());
        txtMaxAmtOfPartialWithdrawalAllowed.setText(observable.getTxtMaxAmtOfPartialWithdrawalAllowed());
        txtMinAmtOfPartialWithdrawalAllowed.setText(observable.getTxtMinAmtOfPartialWithdrawalAllowed());
        txtWithdrawalsInMultiplesOf.setText(observable.getTxtWithdrawalsInMultiplesOf());
        rdoPartialWithdrawalAllowed_Yes.setSelected(observable.getRdoPartialWithdrawalAllowed_Yes());
        rdoPartialWithdrawalAllowed_No.setSelected(observable.getRdoPartialWithdrawalAllowed_No());
        rdoPartialWithdrawalAllowed_NoActionPerformed(null);

        rdoWithdrawalWithInterest_Yes.setSelected(observable.getRdoWithdrawalWithInterest_Yes());
        rdoWithdrawalWithInterest_No.setSelected(observable.getRdoWithdrawalWithInterest_No());
        rdoServiceCharge_Yes.setSelected(observable.getRdoServiceCharge_Yes());
        rdoServiceCharge_No.setSelected(observable.getRdoServiceCharge_No());
        rdoInsBeyondMaturityDt_Yes.setSelected(observable.getRdoInsBeyondMaturityDt_Yes());
        rdoInsBeyondMaturityDt_No.setSelected(observable.getRdoInsBeyondMaturityDt_No());
        txtServiceCharge.setText(observable.getTxtServiceCharge());
        if (rdoServiceCharge_No.isSelected() == true) {
            txtServiceCharge.setEnabled(false);
        } else {
            txtServiceCharge.setEnabled(true);
        }
        cboIntCalcMethod.setSelectedItem(observable.getCboIntCalcMethod());
        cboIntMaintainedAsPartOf.setSelectedItem(observable.getCboIntMaintainedAsPartOf());
        rdoIntProvisioningApplicable_Yes.setSelected(observable.getRdoIntProvisioningApplicable_Yes());
        rdoIntProvisioningApplicable_No.setSelected(observable.getRdoIntProvisioningApplicable_No());
        rdoIntProvisioningApplicable_NoActionPerformed(null);

        cboIntProvisioningFreq.setSelectedItem(observable.getCboIntProvisioningFreq());
        cboProvisioningLevel.setSelectedItem(observable.getCboProvisioningLevel());
        cboNoOfDays.setSelectedItem(observable.getCboNoOfDays());
        cboIntCompoundingFreq.setSelectedItem(observable.getCboIntCompoundingFreq());
        cboIntApplnFreq.setSelectedItem(observable.getCboIntApplnFreq());
        txtCategoryBenifitRate.setText(observable.getTxtCategoryBenifitRate());
        tdtNextInterestAppliedDate.setDateValue(observable.getTdtNextInterestAppliedDate());
        tdtLastInterestAppliedDate.setDateValue(observable.getTdtLastInterestAppliedDate());
        tdtNextIntProvisionalDate.setDateValue(observable.getTdtNextIntProvisionalDate());
        tdtLastIntProvisionalDate.setDateValue(observable.getTdtLastIntProvisionalDate());
        tdtSchemeIntroDate.setDateValue(observable.getTdtSchemeIntroDate());
        tdtSchemeClosingDate.setDateValue(observable.getTdtSchemeClosingDate());
        cboIntRoundOff.setSelectedItem(observable.getCboIntRoundOff());
        txtMinIntToBePaid.setText(observable.getTxtMinIntToBePaid());
        txtIntProvisioningAcctHd.setText(observable.getTxtIntProvisioningAcctHd());
        if (observable.getCbxInterstRoundTime().equals("Y")) {
            cbxInterstRoundTime.setSelected(true);
        } else {
            cbxInterstRoundTime.setSelected(false);
        }
        if (!txtIntProvisioningAcctHd.getText().equals("")) {
            txtIntProvisioningAcctHdDesc.setText(getAccHeadDesc(observable.getTxtIntProvisioningAcctHd()));
            txtIntProvisioningAcctHdDesc.setEnabled(false);
        }
        txtIntOnMaturedDepositAcctHead.setText(observable.getTxtIntOnMaturedDepositAcctHead());
        if (!txtIntOnMaturedDepositAcctHead.getText().equals("")) {
            txtIntOnMaturedDepositAcctHeadDesc.setText(getAccHeadDesc(observable.getTxtIntOnMaturedDepositAcctHead()));
            txtIntOnMaturedDepositAcctHeadDesc.setEnabled(false);
        }
        txtFixedDepositAcctHead.setText(observable.getTxtFixedDepositAcctHead());
        if (!txtFixedDepositAcctHead.getText().equals("")) {
            txtFixedDepositAcctHeadDesc.setText(getAccHeadDesc(observable.getTxtFixedDepositAcctHead()));
            txtFixedDepositAcctHeadDesc.setEnabled(false);
        }
        txttMaturityDepositAcctHead.setText(observable.getTxttMaturityDepositAcctHead());
        if (!txttMaturityDepositAcctHead.getText().equals("")) {
            txttMaturityDepositAcctHeadDesc.setText(getAccHeadDesc(observable.getTxttMaturityDepositAcctHead()));
            txttMaturityDepositAcctHeadDesc.setEnabled(false);
        }
        txtIntProvisionOfMaturedDeposit.setText(observable.getTxtIntProvisionOfMaturedDeposit());
        if (!txtIntProvisionOfMaturedDeposit.getText().equals("")) {
            txtIntProvisionOfMaturedDepositDesc.setText(getAccHeadDesc(observable.getTxtIntProvisionOfMaturedDeposit()));
            txtIntProvisionOfMaturedDepositDesc.setEnabled(false);
        }
        txtIntPaybleGLHead.setText(observable.getTxtIntPaybleGLHead());
        if (!txtIntPaybleGLHead.getText().equals("")) {
            txtIntPaybleGLHeadDesc.setText(getAccHeadDesc(observable.getTxtIntPaybleGLHead()));
            txtIntPaybleGLHeadDesc.setEnabled(false);
        }
        txtCommisionPaybleGLHead.setText(observable.getTxtCommisionPaybleGLHead());
        if (!txtCommisionPaybleGLHead.getText().equals("")) {
            txtCommisionPaybleGLHeadDesc.setText(getAccHeadDesc(observable.getTxtCommisionPaybleGLHead()));
            txtCommisionPaybleGLHeadDesc.setEnabled(false);
        }
        txtPenalCharges.setText(observable.getTxtPenalChargesAchd());
        if (!txtPenalCharges.getText().equals("")) {
            txtPenalChargesDesc.setText(getAccHeadDesc(observable.getTxtPenalChargesAchd()));
            txtPenalChargesDesc.setEnabled(false);
        }
        txtInterestRecoveryAcHd.setText(observable.getTxtInterestRecoveryAcHd());
        if (!txtInterestRecoveryAcHd.getText().equals("")) {
            txtInterestRecoveryAcHdDesc.setText(getAccHeadDesc(observable.getTxtInterestRecoveryAcHd()));
            txtInterestRecoveryAcHdDesc.setEnabled(false);
        }
        txtTransferOutHead.setText(observable.getTxtTransferOutAcHd());
        if (!txtTransferOutHead.getText().equals("")) {
            txtTransferOutHeadDesc.setText(getAccHeadDesc(observable.getTxtTransferOutAcHd()));
            txtTransferOutHeadDesc.setEnabled(false);
        }
        txtIntDebitPLHead.setText(observable.getTxtIntDebitPLHead());
        if (!txtIntDebitPLHead.getText().equals("")) {
            txtIntDebitPLHeadDesc.setText(getAccHeadDesc(observable.getTxtIntDebitPLHead()));
            txtIntDebitPLHeadDesc.setEnabled(false);
        }
        txtAcctHeadForFloatAcct.setText(observable.getTxtAcctHeadForFloatAcct());
        if (!txtAcctHeadForFloatAcct.getText().equals("")) {
            txtAcctHeadForFloatAcctDesc.setText(getAccHeadDesc(observable.getTxtAcctHeadForFloatAcct()));
            txtAcctHeadForFloatAcctDesc.setEnabled(false);
        }
        txtIntPeriodForBackDatedRenewal.setText(observable.getTxtIntPeriodForBackDatedRenewal());
        cboIntPeriodForBackDatedRenewal.setSelectedItem(observable.getCboIntPeriodForBackDatedRenewal());
        txtIntPeriodForBackDatedRenewal.setText(observable.getTxtIntPeriodForBackDatedRenewal());
        cboIntCriteria.setSelectedItem(observable.getCboIntCriteria());
        rdoAutoRenewalAllowed_Yes.setSelected(observable.getRdoAutoRenewalAllowed_Yes());
        rdoAutoRenewalAllowed_No.setSelected(observable.getRdoAutoRenewalAllowed_No());
        rdoSameNoRenewalAllowed_Yes.setSelected(observable.getRdoSameNoRenewalAllowed_Yes());
        rdoSameNoRenewalAllowed_No.setSelected(observable.getRdoSameNoRenewalAllowed_No());
        rdoDaily.setSelected(observable.getRdoDaily());
        rdoWeekly.setSelected(observable.getRdoWeekly());
        rdoMonthly.setSelected(observable.getRdoMonthly());
        if (observable.getRdoDaily()) {
            //            rdoDailyActionPerformed(null);
        } else if (observable.getRdoMonthly()) {
            //            rdoMonthlyActionPerformed(null);
            cboMonthlyIntCalcMethod.setVisible(true);
        } else if (observable.getRdoWeekly()) {
            //            rdoWeeklyActionPerformed(null);
        }
        //        rdoAutoRenewalAllowed_NoActionPerformed(null);
        rdoRenewalOfDepositAllowed_Yes.setSelected(observable.getRdoRenewalOfDepositAllowed_Yes());
        rdoRenewalOfDepositAllowed_No.setSelected(observable.getRdoRenewalOfDepositAllowed_No());
        //        rdoRenewalOfDepositAllowed_NoActionPerformed(null);
        rdoExtensionPenal_Yes.setSelected(observable.getRdoExtensionPenal_Yes());
        rdoExtensionPenal_No.setSelected(observable.getRdoExtensionPenal_No());
        txtMinNoOfDays.setText(observable.getTxtMinNoOfDays());
        txtMaxNopfTimes.setText(observable.getTxtMaxNopfTimes());
        txtMaxNopfTimes1.setText(observable.getTxtMaxNoSameNo());
        cboIntType.setSelectedItem(observable.getCboIntType());
        //tdtMaturityDate.setDateValue(observable.getTdtMaturityDate());
        //tdtPresentDate.setDateValue(observable.getTdtPresentDate());
        cboMaxPeriodMDt.setSelectedItem(observable.getCboMaxPeriodMDt());
        txtMaxPeriodMDt.setText(observable.getTxtMaxPeriodMDt());
        cboMinNoOfDays.setSelectedItem(observable.getCboMinNoOfDays());

        rdoRecalcOfMaturityValue_Yes.setSelected(observable.getRdoRecalcOfMaturityValue_Yes());
        rdoRecalcOfMaturityValue_No.setSelected(observable.getRdoRecalcOfMaturityValue_No());

        //        rdoPenaltyOnLateInstallmentsChargeble_Yes.setSelected(observable.getRdoPenaltyOnLateInstallmentsChargeble_Yes());
        //        rdoPenaltyOnLateInstallmentsChargeble_No.setSelected(observable.getRdoPenaltyOnLateInstallmentsChargeble_No());
        //        rdoPartialWithdrawlAllowedForDD_No.setSelected(observable.
        txtMaturityDateAfterLastInstalPaid.setText(observable.getTxtMaturityDateAfterLastInstalPaid());
        txtMinimumPeriod.setText(observable.getTxtMinimumPeriod());
        txtAgentCommision.setText(observable.getTxtAgentCommision());
        txtInterestNotPayingValue.setText(observable.getTxtInterestNotPayingValue());
        rdoCanReceiveExcessInstal_yes.setSelected(observable.getRdoCanReceiveExcessInstal_yes());
        rdoCanReceiveExcessInstal_No.setSelected(observable.getRdoCanReceiveExcessInstal_No());
        rdoCanReceiveExcessInstal_NoActionPerformed(null);

        rdoInstallmentInRecurringDepositAcct_No.setSelected(observable.getRdoInstallmentInRecurringDepositAcct_No());
        rdoInstallmentInRecurringDepositAcct_Yes.setSelected(observable.getRdoInstallmentInRecurringDepositAcct_Yes());
        rdoInstallmentInRecurringDepositAcct_YesActionPerformed(null);

        cboInstallmentToBeCharged.setSelectedItem(observable.getCboInstallmentToBeCharged());

        // added for daily deposits scheme       
        cboDepositsFrequency.setSelectedItem(observable.getCboDepositsFrequency());
        if (observable.getCboDepositsFrequency() != null && observable.getCboDepositsFrequency().equals("Weekly")) {
            cboDepositsFrequency.setSelectedItem("Weekly");
            ClientUtil.enableDisable(panWeeklyDepositSlab, true);
            tabDepositsProduct.addTab("Weekly Deposit Slab", panWeeklyDepositSlab);
            validateWeeklyDepTxtFields();
            observable.setProductType();
            if (observable.getCbmProductType() != null) {
                cboProductType.setModel(observable.getCbmProductType());
                updateTable();
                enableWeeklySlabTxt(false);
            }
        }

        // Added by nithya on 23-01-2017 for 0005664
        if (observable.getCboDepositsFrequency() !=  null && observable.getCboDepositsFrequency().equals("Installments")) {
            cboDepositsFrequency.setSelectedItem("Installments");
            ClientUtil.enableDisable(panWeeklyDepositSlab, true);
            tabDepositsProduct.addTab("Weekly Deposit Slab", panWeeklyDepositSlab);
            validateWeeklyDepTxtFields();
            observable.setProductType();
            if (observable.getCbmProductType() != null) {
                cboProductType.setModel(observable.getCbmProductType());
                updateTable();
                enableWeeklySlabTxt(false);
            }
        }
        // End
        
        cboChangeValue.setSelectedItem(observable.getCboChangeValue());
        rdoIntPayableOnExcessInstal_Yes.setSelected(observable.getRdoIntPayableOnExcessInstal_Yes());
        rdoIntPayableOnExcessInstal_No.setSelected(observable.getRdoIntPayableOnExcessInstal_No());
        rdoRecurringDepositToFixedDeposit_Yes.setSelected(observable.getRdoRecurringDepositToFixedDeposit_Yes());
        rdoRecurringDepositToFixedDeposit_No.setSelected(observable.getRdoRecurringDepositToFixedDeposit_No());
        cboCutOffDayForPaymentOfInstal.setSelectedItem(observable.getCboCutOffDayForPaymentOfInstal());
        tdtFromDepositDate.setDateValue(observable.getTdtFromDepositDate());
        tdtChosenDate.setDateValue(observable.getTdtChosenDate());
        rdoLastInstallmentAllowed_Yes.setSelected(observable.getRdoLastInstallmentAllowed_Yes());
        rdoLastInstallmentAllowed_No.setSelected(observable.getRdoLastInstallmentAllowed_No());
        cboDepositsFrequency.setSelectedItem(observable.getCboDepositsFrequency());
        rdoRegular.setSelected(observable.getRdoRegular());
        rdoNRO.setSelected(observable.getRdoNRO());
        rdoNRE.setSelected(observable.getRdoNRE());
        rdoStaffAccount_Yes.setSelected(observable.getRdoStaffAccount_Yes());
        rdoStaffAccount_No.setSelected(observable.getRdoStaffAccount_No());
//        rdoLastInstallmentAllowed_NoActionPerformed(null);
        cboMinimumPeriod.setSelectedItem(observable.getCboMinimumRenewalDeposit());
        txtAfterNoDays.setText(observable.getTxtAfterNoDays());
        txtTDSGLAcctHd.setText(observable.getTxtTDSGLAcctHd());
        if (!txtTDSGLAcctHd.getText().equals("")) {
            txtTDSGLAcctHdDesc.setText(getAccHeadDesc(observable.getTxtTDSGLAcctHd()));
            txtTDSGLAcctHdDesc.setEnabled(false);
        }

        lblStatus.setText(observable.getLblStatus());

        cboMaturityDateAfterLastInstalPaid.setSelectedItem(observable.getCboMaturityDateAfterLastInstalPaid());
        cboAgentCommision.setSelectedItem(observable.getCboAgentCommision());
        cboInterestNotPayingValue.setSelectedItem(observable.getCboInterestNotPayingValue());
        cboMinimumPeriod.setSelectedItem(observable.getCboMinimumPeriod());
        txtCutOffDayForPaymentOfInstal.setText(observable.getTxtCutOffDayForPaymentOfInstal());

        //        cboFromAmount.setSelectedItem(observable.getCboFromAmount());
        //        cboToAmount.setSelectedItem(observable.getCboToAmount());
//        txtFromAmt.setText(observable.getCboFromAmount());
//        txtToAmt.setText(observable.getCboToAmount());
        cboFromPeriod.setSelectedItem(observable.getCboFromPeriod());
        cboToPeriod.setSelectedItem(observable.getCboToPeriod());
        txtCommisionRate.setText(observable.getTxtRateInterest());
        txtPenalCharges.setText(observable.getTxtPenalChargesAchd());
        txtInterestRecoveryAcHd.setText(observable.getTxtInterestRecoveryAcHd());
        txtTransferOutHead.setText(observable.getTxtTransferOutAcHd());
        txtFromPeriod.setText(observable.getTxtFromPeriod());
        txtToPeriod.setText(observable.getTxtToPeriod());
        tdtDate.setDateValue(observable.getTdtDate());
        tdtToDate.setDateValue(observable.getTdtToDate());
        txtCommisionRate.setText(observable.getTxtRateInterest());
        cboWeekly.setSelectedItem(observable.getCboWeekly_Day());
        cboMonthlyIntCalcMethod.setSelectedItem(observable.getCboMonthlyIntCalcMethod());
        if (observable.getRdoPartialWithdrawlAllowedForDD().equals("Y")) {
            rdoPartialWithdrawlAllowedForDD_Yes.setSelected(true);
        } else if (observable.getRdoPartialWithdrawlAllowedForDD().equals("N")) {
            rdoPartialWithdrawlAllowedForDD_No.setSelected(true);
        }

        txtFromAmt.setText(observable.getTxtFromAmt());
        txtToAmt.setText(observable.getTxtToAmt());
        if(CommonConstants.SAL_REC_MODULE != null && !CommonConstants.SAL_REC_MODULE.equals("") && CommonConstants.SAL_REC_MODULE.equals("Y")){
            cboInstType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCboInstType()));
        }
        txtDelayedChargesAmt.setText(observable.getTxtDelayedChargesAmt());
        txtMinimumRenewalDeposit.setText(observable.getTxtMinimumRenewalDeposit());

        cboMinimumPeriod.setSelectedItem(observable.getCboMinimumRenewalDeposit());
        txtRenewedclosedbefore.setText(observable.getTxtRenewedclosedbefore());
        rdoSameNoRenewalAllowed_Yes.setSelected(observable.getRdoSameNoRenewalAllowed_Yes());
        rdoSameNoRenewalAllowed_No.setSelected(observable.getRdoSameNoRenewalAllowed_No());
        rdoInterestalreadyPaid_Yes.setSelected(observable.getRdoInterestalreadyPaid_Yes());
        rdoInterestalreadyPaid_No.setSelected(observable.getRdoInterestalreadyPaid_No());
        rdoInterestrateappliedoverdue_Yes.setSelected(observable.getRdoInterestrateappliedoverdue_Yes());
        rdoInterestrateappliedoverdue_No.setSelected(observable.getRdoInterestrateappliedoverdue_No());
        chkRateAsOnDateOfMaturity.setSelected(observable.getChkRateAsOnDateOfMaturity());
        chkRateAsOnDateOfRenewal.setSelected(observable.getChkRateAsOnDateOfRenewal());//added by Chithra on 21-04-14
        chkIntRateApp.setSelected(observable.getChkIntRateApp());
        chkIntRateDeathMarked.setSelected(observable.isChkIntRateDeathMarked());
        cboEitherofTwoRatesChoose.setSelectedItem(observable.getCboEitherofTwoRatesChoose());
        rdoSBRateOneRate_Yes.setSelected(observable.getRdoSBRateOneRate_Yes());
        rdoSBRateOneRate_No.setSelected(observable.getRdoSBRateOneRate_No());
        rdoBothRateNotAvail_Yes.setSelected(observable.getRdoBothRateNotAvail_Yes());
        rdoBothRateNotAvail_No.setSelected(observable.getRdoBothRateNotAvail_No());
        rdoExtensionBeyondOriginalDate_Yes.setSelected(observable.getRdoExtensionBeyondOriginalDate_Yes());
        rdoExtensionBeyondOriginalDate_No.setSelected(observable.getRdoExtensionBeyondOriginalDate_No());
        cboRenewedclosedbefore.setSelectedItem(observable.getCboRenewedclosedbefore());
        cboMinimumRenewalDeposit.setSelectedItem(observable.getCboMinimumRenewalDeposit());
        txtPostageAcHd.setText(observable.getPostageAcHd());
        sbRateCmb.setSelectedItem(observable.getSbRateCmb());
        if (!txtPostageAcHd.getText().equals("")) {
            btnPostageAcHd.setToolTipText(getAccHeadDesc(observable.getPostageAcHd()));
            //txtIntProvisioningAcctHdDesc.setEnabled(false);
        }
        txtRDIrregularIfInstallmentDue.setText(observable.getTxtRDIrregularIfInstallmentDue());
        rdoIncaseOfIrregularRDSBRate.setSelected(observable.isRdoIncaseOfIrregularRDSBRate());
        rdoIncaseOfIrregularRDRBRate.setSelected(observable.isRdoIncaseOfIrregularRDRBRate());
        rdoDepositRate.setSelected(observable.isRdoDepositRate());
        chkDifferentROI.setSelected(observable.isChkDifferentROI());
        chkSlabWiseInterest.setSelected(observable.isChkSlabWiseInterest());
        chkSlabWiseCommision.setSelected(observable.isChkSlabWiseCommision());
        rdoSBRate.setSelected(observable.isRdoSBRate());
        if (observable.getRdoPenaltyOnLateInstallmentsChargeble_Yes() == true) {
            rdoPenaltyOnLateInstallmentsChargeble_Yes.setSelected(true);
            rdoPenaltyOnLateInstallmentsChargeble_No.setSelected(false);
            lblRDIrregularIfInstallmentDue.setVisible(true);
            txtRDIrregularIfInstallmentDue.setVisible(true);
        } else {
            rdoPenaltyOnLateInstallmentsChargeble_No.setSelected(true);
            rdoPenaltyOnLateInstallmentsChargeble_Yes.setSelected(false);

        }
        if (observable.isChkFDRenewalSameNoTranPrincAmt() == true) {
            chkFDRenewalSameNoTranPrincAmt.setSelected(true);
        } else {
            chkFDRenewalSameNoTranPrincAmt.setSelected(false);
        }
        //        tblInterestTable.setModel(observable.getTblInterestTable());


        addRadioButtons();
        if(rdoSBRate.isSelected()){
            cmbSbProduct.setSelectedItem(observable.getCboSbProduct());
        }else{
        cboDepositsProdFixd.setSelectedItem(observable.getCboDepositsProdFixd());
        }
        cboPreMatIntType.setSelectedItem(observable.getCboPreMatIntType());
        
        if (observable.getChkAppNormRate().equals("Y")) {
            chkAppNormRate.setSelected(true);
        } else {
            chkAppNormRate.setSelected(false);
        }
        txtNormPeriod.setText(observable.getTxtNormPeriod());

        if (observable.isDueOn()) {
            rdoMonthEnd.setSelected(true);
        } else {
            rdoInstallmentDay.setSelected(true);
        }
        
         if(cboOperatesLike.getSelectedItem().equals("Thrift") || cboOperatesLike.getSelectedItem().equals("Benevolent")){
            updateTable();
            txtInstallmentAmount.setEditable(true);
            tdtEffectiveDate.setEnabled(true);
         }
         
         txtGracePeriod.setText(observable.getTxtGracePeriod()); // Added by shany
         
         // Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
         if(observable.getChkIsGroupDepositProduct().equalsIgnoreCase("Y")){
             chkIsGroupDeposit.setSelected(true);
         }else{
             chkIsGroupDeposit.setSelected(false);
         }
         
         txtBenIntReserveHead.setText(observable.getTxtBenIntReserveHead());
         if (!txtBenIntReserveHead.getText().equals("")) {
            txtBenIntReserveHeadDesc.setText(getAccHeadDesc(observable.getTxtBenIntReserveHead()));
            txtBenIntReserveHeadDesc.setEnabled(false);
        }
         
         // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
         if(observable.getChkRDClosingOtherROI().equalsIgnoreCase("Y")){
             chkRDClosingOtherROI.setSelected(true);
         }else{
             chkRDClosingOtherROI.setSelected(false);
         }
         
         cboprmatureCloseRate.setSelectedItem(observable.getCbmprmatureCloseRate().getDataForKey(CommonUtil.convertObjToStr(observable.getCboprmatureCloseRate())));
         cboPrmatureCloseProduct.setSelectedItem(observable.getCbmPrmatureCloseProduct().getDataForKey(CommonUtil.convertObjToStr(observable.getCboPrmatureCloseProduct())));
         cboIrregularRDCloseRate.setSelectedItem(observable.getCbmIrregularRDCloseRate().getDataForKey(CommonUtil.convertObjToStr(observable.getCboIrregularRDCloseRate())));
         cboIrregularRDCloseProduct.setSelectedItem(observable.getCbmIrregularRDCloseProduct().getDataForKey(CommonUtil.convertObjToStr(observable.getCboIrregularRDCloseProduct())));
         
         if(observable.getChkIntForIrregularRD().equalsIgnoreCase("Y")){ // Added by nithya on 18-03-2020 for KD-1535
             chkIntForIrregularRD.setSelected(true);
         }else{
             chkIntForIrregularRD.setSelected(false);
         }
         
         if(observable.getChkSpecialRD().equalsIgnoreCase("Y")){ // Added by nithya on 18-03-2020 for KD-1535
             chkSpecialRD.setSelected(true);
         }else{
             chkSpecialRD.setSelected(false);
         }
         
         txtSpecialRDInstallments.setText(observable.getTxtSpecialRDInstallments()); // Added by nithya on 01-04-2020 for KD-1535
         
        
         cboAgentCommCalcMethod.setSelectedItem(observable.getCbmAgentCommCalcMethod().getDataForKey(CommonUtil.convertObjToStr(observable.getCboAgentCommCalcMethod())));
         if(observable.getChkAgentCommSlabRequired().equalsIgnoreCase("Y")){
             chkAgentCommSlabRequired.setSelected(true);             
         }else{
             chkAgentCommSlabRequired.setSelected(false);            
         }
         
         txtAgentCommSlabAmtFrom.setText(observable.getTxtAgentCommSlabAmtFrom());
         TxtAgentCommSlabAmtTo.setText(observable.getTxtAgentCommSlabAmtTo());
         txtAgentCommSlabPercent.setText(observable.getTxtAgentCommSlabPercent());
         
          // Added by nithya on 17-06-2020
      
          
            if (observable.getChkAgentCommSlabRequired().equalsIgnoreCase("Y")) {               
                updateTable();
                enableAgentCommissionSlabTxt(false);
            }
        
        // End
    }
    
   
  
    
    public void updateOBFields() {
        System.out.println("ddfgdgf");
        if (chkExcludeLienFrmIntrstAppl.isSelected() == true) {
            observable.setChkExcludeLienIntrstAppl("Y");
        } else {
            observable.setChkExcludeLienIntrstAppl("N");
        }
        if (chkExcludeLienFrmStanding.isSelected() == true) {
            observable.setChkExcludeLienStanding("Y");
        } else {
            observable.setChkExcludeLienStanding("N");
        }
        if (chkDepositUnlien.isSelected() == true) {
            observable.setChkDepositLien("Y");
        } else {
            observable.setChkDepositLien("N");
        }

        if (chkCummCertPrint.isSelected() == true) {
            observable.setChkCummCertPrint("Y");
        } else {
            observable.setChkCummCertPrint("N");
        }
        if (chkIntEditable.isSelected() == true) {
            observable.setChkIntEditable("Y");
        } else {
            observable.setChkIntEditable("N");
        }
        if (chkCanZeroBalAct.isSelected() == true) {
            observable.setChkCanZeroBalAct("Y");
        } else {
            observable.setChkCanZeroBalAct("N");
        }
        if (chkPrematureClosure.isSelected() == true) {
            observable.setChkPrematureClosure("Y");
        } else {
            observable.setChkPrematureClosure("N");
        }
        if (chkIntApplicationSlab.isSelected() == true) {
            observable.setChkIntApplicationSlab("Y");
        } else {
            observable.setChkIntApplicationSlab("N");
        }
        if (chkWeeklySpecial.isSelected() == true) {
            observable.setChkWeeklySpecial("Y");
        } else {
            observable.setChkWeeklySpecial("N");
        }
         if (chkRdNature.isSelected() == true) {
            observable.setChkRdNature("Y");
        } else {
            observable.setChkRdNature("N");
        }
        System.out.println("rdoPenalRounded_Yes.isSelected()" + rdoPenalRounded_Yes.isSelected() + "rdoPenalRounded_No.isSelected()" + rdoPenalRounded_No.isSelected());
        if (rdoPenalRounded_Yes.isSelected()) {
            observable.setRdoPenalRounded_Yes("Y");
            observable.setRdoPenalRounded_No("N");
            observable.setCboRoundOffCriteriaPenal((String) ((ComboBoxModel) (cboRoundOffCriteriaPenal).getModel()).getKeyForSelected());
        }
        if (rdoPenalRounded_No.isSelected()) {
            observable.setRdoPenalRounded_Yes("N");
            observable.setRdoPenalRounded_No("Y");
            observable.setCboRoundOffCriteriaPenal((String) ((ComboBoxModel) (cboRoundOffCriteriaPenal).getModel()).getKeyForSelected());
        }
        observable.setScreen(getScreen());
        observable.setModule(getModule());

        observable.setTxtAcctHd(txtAcctHd.getText());
        observable.setTxtProductID(txtProductID.getText());
        observable.setTxtDesc(txtDesc.getText());
        observable.setCboOperatesLike((String) cboOperatesLike.getSelectedItem());
        observable.setCboProdCurrency((String) cboProdCurrency.getSelectedItem());
        observable.setRdoCalcTDS_Yes(rdoCalcTDS_Yes.isSelected());
        if (rdoWithPeriod_No.isSelected()) {
            observable.setRdoWithPeriod("N");
        } else if (rdoWithPeriod_Yes.isSelected()) {
            observable.setRdoWithPeriod("Y");
        }
        if (rdoDoublingScheme_No.isSelected()) {
            observable.setRdoDoublingScheme("N");
        } else if (rdoDoublingScheme_Yes.isSelected()) {
            observable.setRdoDoublingScheme("Y");
            observable.setTxtDoublingCount(txtDoublingCount.getText());
        }
        System.out.println("txtDoublingCount.getText() ========== :"+txtDoublingCount.getText());
        observable.setTxtDoublingCount(txtDoublingCount.getText());
        observable.setRdoCalcTDS_No(rdoCalcTDS_No.isSelected());
        observable.setDiscounted_Yes(rdoDiscounted_Yes.isSelected());
        observable.setDiscounted_No(rdoDiscounted_No.isSelected());
        observable.setRdoPayInterestOnHoliday_Yes(rdoPayInterestOnHoliday_Yes.isSelected());
        observable.setRdoPayInterestOnHoliday_No(rdoPayInterestOnHoliday_No.isSelected());
        observable.setRdoAmountRounded_Yes(rdoAmountRounded_Yes.isSelected());
        observable.setRdoAmountRounded_No(rdoAmountRounded_No.isSelected());
        observable.setCboRoundOffCriteria((String) cboRoundOffCriteria.getSelectedItem());
        observable.setCboInstType(CommonUtil.convertObjToStr(cboInstType.getSelectedItem()));
        observable.setRdoInterestAfterMaturity_Yes(rdoInterestAfterMaturity_Yes.isSelected());
        observable.setRdoInterestAfterMaturity_No(rdoInterestAfterMaturity_No.isSelected());
        observable.setCboMaturityInterestType((String) cboMaturityInterestType.getSelectedItem());
        observable.setCboMaturityInterestRate((String) cboMaturityInterestRate.getSelectedItem());
        //        observable.setCboInterestCriteria((String) cboInterestCriteria.getSelectedItem());
        observable.setTxtPeriodInMultiplesOf(txtPeriodInMultiplesOf.getText());
        observable.setCboPeriodInMultiplesOf((String) cboPeriodInMultiplesOf.getSelectedItem());
        observable.setTxtMaxDepositPeriod(txtMaxDepositPeriod.getText());
        observable.setCboMaxDepositPeriod((String) cboMaxDepositPeriod.getSelectedItem());
        observable.setTxtMinDepositPeriod(txtMinDepositPeriod.getText());
        observable.setCboMinDepositPeriod((String) cboMinDepositPeriod.getSelectedItem());
        observable.setCboPrematureWithdrawal((String) cboPrematureWithdrawal.getSelectedItem());
        observable.setTxtDepositPerUnit(txtDepositPerUnit.getText());
        //        observable.setCboDepositPerUnit((String) cboDepositPerUnit.getSelectedItem());
        observable.setTxtAmtInMultiplesOf(txtAmtInMultiplesOf.getText());
        observable.setTxtMinDepositAmt(txtMinDepositAmt.getText());
        observable.setTxtMaxDepositAmt(txtMaxDepositAmt.getText());
        observable.setRdoCalcMaturityValue_Yes(rdoCalcMaturityValue_Yes.isSelected());
        observable.setRdoCalcMaturityValue_No(rdoCalcMaturityValue_No.isSelected());
        observable.setRdoProvisionOfInterest_Yes(rdoProvisionOfInterest_Yes.isSelected());
        observable.setRdoProvisionOfInterest_No(rdoProvisionOfInterest_No.isSelected());
        observable.setTxtInterestOnMaturedDeposits(txtInterestOnMaturedDeposits.getText());
        observable.setTxtAlphaSuffix(txtAlphaSuffix.getText());
        observable.setTxtMaxAmtOfCashPayment(txtMaxAmtOfCashPayment.getText());
        observable.setTxtMinAmtOfPAN(txtMinAmtOfPAN.getText());
        observable.setTxtAcctNumberPattern(txtAcctNumberPattern.getText());
        observable.setTxtLastAcctNumber(txtLastAcctNumber.getText());
        observable.setTxtSuffix(txtSuffix.getText());
        observable.setRdoClosedwithinperiod_Yes(rdoClosedwithinperiod_Yes.isSelected());
        observable.setRdoClosedwithinperiod_No(rdoClosedwithinperiod_No.isSelected());
        //        observable.setTxtScheme(txtScheme.getText());
        observable.setTxtLimitForBulkDeposit(txtLimitForBulkDeposit.getText());
        observable.setRdoTransferToMaturedDeposits_Yes(rdoTransferToMaturedDeposits_Yes.isSelected());
        observable.setRdoTransferToMaturedDeposits_No(rdoTransferToMaturedDeposits_No.isSelected());
        observable.setRdoAutoAdjustment_Yes(rdoAutoAdjustment_Yes.isSelected());
        observable.setRdoAutoAdjustment_No(rdoAutoAdjustment_No.isSelected());
        observable.setRdoAdjustIntOnDeposits_No(rdoAdjustIntOnDeposits_No.isSelected());
        observable.setRdoAdjustIntOnDeposits_Yes(rdoAdjustIntOnDeposits_Yes.isSelected());
        observable.setRdoAdjustPrincipleToLoan_Yes(rdoAdjustPrincipleToLoan_Yes.isSelected());
        observable.setRdoAdjustPrincipleToLoan_No(rdoAdjustPrincipleToLoan_No.isSelected());
        observable.setRdoExtnOfDepositBeforeMaturity_Yes(rdoExtnOfDepositBeforeMaturity_Yes.isSelected());
        observable.setRdoExtnOfDepositBeforeMaturity_No(rdoExtnOfDepositBeforeMaturity_No.isSelected());
        observable.setTxtAfterHowManyDays(txtAfterHowManyDays.getText());
        observable.setTxtAdvanceMaturityNoticeGenPeriod(txtAdvanceMaturityNoticeGenPeriod.getText());
        observable.setTxtPrematureWithdrawal(txtPrematureWithdrawal.getText());
        observable.setRdoFlexiFromSBCA_Yes(rdoFlexiFromSBCA_Yes.isSelected());
        observable.setRdoFlexiFromSBCA_No(rdoFlexiFromSBCA_No.isSelected());
        observable.setRdoTermDeposit_Yes(rdoTermDeposit_Yes.isSelected());
        observable.setRdoTermDeposit_No(rdoTermDeposit_No.isSelected());
        observable.setRdoIntroducerReqd_Yes(rdoIntroducerReqd_Yes.isSelected());
        observable.setRdoIntroducerReqd_No(rdoIntroducerReqd_No.isSelected());
        observable.setCboMinimumRenewalDeposit((String) cboMinimumPeriod.getSelectedItem());
        observable.setCboMinimumRenewalDeposit((String) cboMinimumRenewalDeposit.getSelectedItem());
        if (rdoSystemCalcValues_Yes.isSelected()) {
            observable.setRdoCertificate_printing("A");
        } else if (rdoSystemCalcValues_No.isSelected()) {
            observable.setRdoCertificate_printing("S");
        } else if (rdoSystemCalcValues_Print_accOpen.isSelected()) {
            observable.setRdoCertificate_printing("O");
        } else {
            observable.setRdoCertificate_printing("");
        }
        //        observable.setRdoSystemCalcValues_Yes(rdoSystemCalcValues_Yes.isSelected());
        //        observable.setRdoSystemCalcValues_No(rdoSystemCalcValues_No.isSelected());
        observable.setTxtNoOfPartialWithdrawalAllowed(txtNoOfPartialWithdrawalAllowed.getText());
        observable.setTxtMaxNoOfPartialWithdrawalAllowed(txtMaxNoOfPartialWithdrawalAllowed.getText());
        observable.setTxtMaxAmtOfPartialWithdrawalAllowed(txtMaxAmtOfPartialWithdrawalAllowed.getText());
        observable.setTxtMinAmtOfPartialWithdrawalAllowed(txtMinAmtOfPartialWithdrawalAllowed.getText());
        observable.setTxtWithdrawalsInMultiplesOf(txtWithdrawalsInMultiplesOf.getText());
        observable.setRdoPartialWithdrawalAllowed_Yes(rdoPartialWithdrawalAllowed_Yes.isSelected());
        observable.setRdoPartialWithdrawalAllowed_No(rdoPartialWithdrawalAllowed_No.isSelected());
        observable.setRdoWithdrawalWithInterest_Yes(rdoWithdrawalWithInterest_Yes.isSelected());
        observable.setRdoWithdrawalWithInterest_No(rdoWithdrawalWithInterest_No.isSelected());
        observable.setRdoServiceCharge_Yes(rdoServiceCharge_Yes.isSelected());
        observable.setRdoServiceCharge_No(rdoServiceCharge_No.isSelected());
        observable.setRdoInsBeyondMaturityDt_Yes(rdoInsBeyondMaturityDt_Yes.isSelected());
        observable.setRdoInsBeyondMaturityDt_No(rdoInsBeyondMaturityDt_No.isSelected());
        observable.setTxtServiceCharge(txtServiceCharge.getText());
        observable.setCboIntCalcMethod((String) cboIntCalcMethod.getSelectedItem());
        observable.setCboIntMaintainedAsPartOf((String) cboIntMaintainedAsPartOf.getSelectedItem());
        observable.setRdoIntProvisioningApplicable_Yes(rdoIntProvisioningApplicable_Yes.isSelected());
        observable.setRdoIntProvisioningApplicable_No(rdoIntProvisioningApplicable_No.isSelected());
        observable.setCboIntProvisioningFreq((String) cboIntProvisioningFreq.getSelectedItem());
        observable.setCboProvisioningLevel((String) cboProvisioningLevel.getSelectedItem());
        observable.setCboNoOfDays((String) cboNoOfDays.getSelectedItem());
        observable.setCboIntCompoundingFreq((String) cboIntCompoundingFreq.getSelectedItem());
        observable.setCboIntApplnFreq((String) cboIntApplnFreq.getSelectedItem());
        observable.setTdtNextInterestAppliedDate(tdtNextInterestAppliedDate.getDateValue());
        observable.setTdtLastInterestAppliedDate(tdtLastInterestAppliedDate.getDateValue());
        observable.setTdtNextIntProvisionalDate(tdtNextIntProvisionalDate.getDateValue());
        observable.setTdtLastIntProvisionalDate(tdtLastIntProvisionalDate.getDateValue());
        observable.setCboIntRoundOff((String) cboIntRoundOff.getSelectedItem());
        observable.setTxtMinIntToBePaid(txtMinIntToBePaid.getText());
        observable.setTxtIntProvisioningAcctHd(txtIntProvisioningAcctHd.getText());
        observable.setTxtIntOnMaturedDepositAcctHead(txtIntOnMaturedDepositAcctHead.getText());
        observable.setTxtFixedDepositAcctHead(txtFixedDepositAcctHead.getText());
        observable.setTxttMaturityDepositAcctHead(txttMaturityDepositAcctHead.getText());
        observable.setTxtIntProvisionOfMaturedDeposit(txtIntProvisionOfMaturedDeposit.getText());
        observable.setTxtIntPaybleGLHead(txtIntPaybleGLHead.getText());
        observable.setTdtSchemeIntroDate(tdtSchemeIntroDate.getDateValue());
        observable.setTdtSchemeClosingDate(tdtSchemeClosingDate.getDateValue());
        observable.setTxtCommisionPaybleGLHead(txtCommisionPaybleGLHead.getText());
        observable.setTxtPenalChargesAchd(txtPenalCharges.getText());
        observable.setTxtInterestRecoveryAcHd(txtInterestRecoveryAcHd.getText());
        observable.setTxtTransferOutAcHd(txtTransferOutHead.getText());
        observable.setTxtIntDebitPLHead(txtIntDebitPLHead.getText());
        observable.setTxtAcctHeadForFloatAcct(txtAcctHeadForFloatAcct.getText());
        observable.setTxtIntPeriodForBackDatedRenewal(txtIntPeriodForBackDatedRenewal.getText());
        observable.setCboIntPeriodForBackDatedRenewal((String) cboIntPeriodForBackDatedRenewal.getSelectedItem());
        observable.setTxtIntPeriodForBackDatedRenewal(txtIntPeriodForBackDatedRenewal.getText());
        observable.setCboIntCriteria((String) cboIntCriteria.getSelectedItem());
        observable.setRdoAutoRenewalAllowed_Yes(rdoAutoRenewalAllowed_Yes.isSelected());
        observable.setRdoAutoRenewalAllowed_No(rdoAutoRenewalAllowed_No.isSelected());
        observable.setRdoSameNoRenewalAllowed_Yes(rdoSameNoRenewalAllowed_Yes.isSelected());
        observable.setRdoSameNoRenewalAllowed_No(rdoSameNoRenewalAllowed_No.isSelected());
        observable.setRdoRenewalOfDepositAllowed_Yes(rdoRenewalOfDepositAllowed_Yes.isSelected());
        observable.setRdoRenewalOfDepositAllowed_No(rdoRenewalOfDepositAllowed_No.isSelected());
        observable.setTxtMinNoOfDays(txtMinNoOfDays.getText());
        observable.setTxtMaxNopfTimes(txtMaxNopfTimes.getText());
        observable.setTxtMaxNoSameNo(txtMaxNopfTimes1.getText());
        observable.setCboIntType((String) cboIntType.getSelectedItem());
        //        observable.setTdtMaturityDate(tdtMaturityDate.getDateValue());
        //        observable.setTdtPresentDate(tdtPresentDate.getDateValue());
        observable.setRdoRecalcOfMaturityValue_Yes(rdoRecalcOfMaturityValue_Yes.isSelected());
        observable.setRdoRecalcOfMaturityValue_No(rdoRecalcOfMaturityValue_No.isSelected());
        observable.setRdoPenaltyOnLateInstallmentsChargeble_Yes(rdoPenaltyOnLateInstallmentsChargeble_Yes.isSelected());
        observable.setRdoPenaltyOnLateInstallmentsChargeble_No(rdoPenaltyOnLateInstallmentsChargeble_No.isSelected());
        observable.setTxtMaturityDateAfterLastInstalPaid(txtMaturityDateAfterLastInstalPaid.getText());
        observable.setTxtMinimumPeriod(txtMinimumPeriod.getText());
        observable.setTxtAgentCommision(txtAgentCommision.getText());
        observable.setTxtInterestNotPayingValue(txtInterestNotPayingValue.getText());
        observable.setRdoCanReceiveExcessInstal_yes(rdoCanReceiveExcessInstal_yes.isSelected());
        observable.setRdoCanReceiveExcessInstal_No(rdoCanReceiveExcessInstal_No.isSelected());
        observable.setRdoInstallmentInRecurringDepositAcct_Yes(rdoInstallmentInRecurringDepositAcct_Yes.isSelected());
        observable.setRdoInstallmentInRecurringDepositAcct_No(rdoInstallmentInRecurringDepositAcct_No.isSelected());
        observable.setCboInstallmentToBeCharged((String) cboInstallmentToBeCharged.getSelectedItem());

        //added for daily deposits scheme
        observable.setCboDepositsFrequency((String) cboDepositsFrequency.getSelectedItem());

        observable.setCboInterestType((String) cboInterestType.getSelectedItem());
        observable.setCboChangeValue((String) cboChangeValue.getSelectedItem());
        observable.setCboMaxPeriodMDt((String) cboMaxPeriodMDt.getSelectedItem());
        observable.setTxtMaxPeriodMDt(txtMaxPeriodMDt.getText());
        observable.setCboMinNoOfDays((String) cboMinNoOfDays.getSelectedItem());
        observable.setRdoIntPayableOnExcessInstal_Yes(rdoIntPayableOnExcessInstal_Yes.isSelected());
        observable.setRdoIntPayableOnExcessInstal_No(rdoIntPayableOnExcessInstal_No.isSelected());
        observable.setRdoRecurringDepositToFixedDeposit_Yes(rdoRecurringDepositToFixedDeposit_Yes.isSelected());
        observable.setRdoRecurringDepositToFixedDeposit_No(rdoRecurringDepositToFixedDeposit_No.isSelected());
        observable.setCboCutOffDayForPaymentOfInstal((String) cboCutOffDayForPaymentOfInstal.getSelectedItem());
        observable.setTdtFromDepositDate(tdtFromDepositDate.getDateValue());
        observable.setTdtChosenDate(tdtChosenDate.getDateValue());
        observable.setRdoLastInstallmentAllowed_Yes(rdoLastInstallmentAllowed_Yes.isSelected());
        observable.setRdoLastInstallmentAllowed_No(rdoLastInstallmentAllowed_No.isSelected());
        observable.setRdoDaily(rdoDaily.isSelected());
        observable.setRdoWeekly(rdoWeekly.isSelected());
        observable.setRdoMonthly(rdoMonthly.isSelected());

        observable.setCboWeekly_Day((String) cboWeekly.getSelectedItem());
        observable.setCboMonthlyIntCalcMethod((String) cboMonthlyIntCalcMethod.getSelectedItem());
        if (rdoPartialWithdrawlAllowedForDD_No.isSelected()) {
            observable.setRdoPartialWithdrawlAllowedForDD("N");
        } else if (rdoPartialWithdrawlAllowedForDD_Yes.isSelected()) {
            observable.setRdoPartialWithdrawlAllowedForDD("Y");
        }
        observable.setRdoRegular(rdoRegular.isSelected());
        observable.setRdoNRO(rdoNRO.isSelected());
        observable.setRdoNRE(rdoNRE.isSelected());
        observable.setRdoStaffAccount_Yes(rdoStaffAccount_Yes.isSelected());
        observable.setRdoStaffAccount_No(rdoStaffAccount_No.isSelected());

        observable.setTxtAfterNoDays(txtAfterNoDays.getText());
        observable.setTxtTDSGLAcctHd(txtTDSGLAcctHd.getText());

        observable.setCboMaturityDateAfterLastInstalPaid((String) cboMaturityDateAfterLastInstalPaid.getSelectedItem());
        observable.setCboMinimumPeriod((String) cboMinimumPeriod.getSelectedItem());
        observable.setCboAgentCommision((String) cboAgentCommision.getSelectedItem());
        observable.setCboInterestNotPayingValue((String) cboInterestNotPayingValue.getSelectedItem());
        observable.setTxtCutOffDayForPaymentOfInstal(txtCutOffDayForPaymentOfInstal.getText());

        //        observable.setCboFromAmount((String)cboFromAmount.getSelectedItem());
        //        observable.setCboToAmount((String)cboToAmount.getSelectedItem());
//        observable.setCboFromAmount(txtFromAmt.getText());
//        observable.setCboToAmount(txtToAmt.getText());
        observable.setCboFromPeriod((String) cboFromPeriod.getSelectedItem());
        observable.setCboToPeriod((String) cboToPeriod.getSelectedItem());
        observable.setTxtFromPeriod((String) txtFromPeriod.getText());
        observable.setTxtToPeriod((String) txtToPeriod.getText());
        observable.setTxtRateInterest((String) txtCommisionRate.getText());
        observable.setTxtPenalChargesAchd((String) txtPenalCharges.getText());
        observable.setTxtInterestRecoveryAcHd((String) txtInterestRecoveryAcHd.getText());
        observable.setTxtTransferOutAcHd((String) txtTransferOutHead.getText());
        observable.setTdtDate(tdtDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());

        observable.setTxtFromAmt((String) txtFromAmt.getText());
        observable.setTxtToAmt((String) txtToAmt.getText());
        observable.setTxtDelayedChargesAmt((String) txtDelayedChargesAmt.getText());
        observable.setTxtMinimumRenewalDeposit(txtMinimumRenewalDeposit.getText());
        observable.setCboMinimumRenewalDeposit((String) cboMinimumPeriod.getSelectedItem());

        observable.setTxtRenewedclosedbefore(txtRenewedclosedbefore.getText());

        observable.setRdoInterestalreadyPaid_Yes(rdoInterestalreadyPaid_Yes.isSelected());
        observable.setRdoInterestalreadyPaid_No(rdoInterestalreadyPaid_No.isSelected());
        observable.setRdoInterestrateappliedoverdue_Yes(rdoInterestrateappliedoverdue_Yes.isSelected());

        observable.setRdoInterestrateappliedoverdue_No(rdoInterestrateappliedoverdue_No.isSelected());
        observable.setChkRateAsOnDateOfMaturity(chkRateAsOnDateOfMaturity.isSelected());
        observable.setChkRateAsOnDateOfRenewal(chkRateAsOnDateOfRenewal.isSelected());//added by Chithra on 21-04-14
        observable.setChkIntRateApp(chkIntRateApp.isSelected());
        observable.setChkIntRateDeathMarked(chkIntRateDeathMarked.isSelected());
        observable.setCboEitherofTwoRatesChoose((String) cboEitherofTwoRatesChoose.getSelectedItem());
        observable.setRdoSBRateOneRate_Yes(rdoSBRateOneRate_Yes.isSelected());
        observable.setRdoSBRateOneRate_No(rdoSBRateOneRate_No.isSelected());
        observable.setRdoBothRateNotAvail_Yes(rdoBothRateNotAvail_Yes.isSelected());
        observable.setRdoBothRateNotAvail_No(rdoBothRateNotAvail_No.isSelected());
        observable.setRdoExtensionBeyondOriginalDate_Yes(rdoExtensionBeyondOriginalDate_Yes.isSelected());
        observable.setRdoExtensionBeyondOriginalDate_No(rdoExtensionBeyondOriginalDate_No.isSelected());
        observable.setRdoExtensionPenal_Yes(rdoExtensionPenal_Yes.isSelected());
        observable.setRdoExtensionPenal_No(rdoExtensionPenal_No.isSelected());
        observable.setCboRenewedclosedbefore((String) cboRenewedclosedbefore.getSelectedItem());
        observable.setPostageAcHd(txtPostageAcHd.getText());
        observable.setTxtRDIrregularIfInstallmentDue(txtRDIrregularIfInstallmentDue.getText());
        observable.setRdoIncaseOfIrregularRDSBRate(rdoIncaseOfIrregularRDSBRate.isSelected());
        observable.setRdoIncaseOfIrregularRDRBRate(rdoIncaseOfIrregularRDRBRate.isSelected());
        observable.setRdoDepositRate(rdoDepositRate.isSelected());
        observable.setChkDifferentROI(chkDifferentROI.isSelected());
        observable.setChkSlabWiseInterest(chkSlabWiseInterest.isSelected());
        observable.setChkSlabWiseCommision(chkSlabWiseCommision.isSelected());
        observable.setRdoSBRate(rdoSBRate.isSelected());
        observable.setChkFDRenewalSameNoTranPrincAmt(chkFDRenewalSameNoTranPrincAmt.isSelected());
        observable.setCboDepositsProdFixd((String) cboDepositsProdFixd.getSelectedItem());
        observable.setTxtCategoryBenifitRate(txtCategoryBenifitRate.getText());
        observable.setCboPreMatIntType(CommonUtil.convertObjToStr(((ComboBoxModel) (cboPreMatIntType).getModel()).getKeyForSelected()));
        if (cbxInterstRoundTime.isSelected()) {
            observable.setCbxInterstRoundTime("Y");
        } else {
            observable.setCbxInterstRoundTime("N");
        }
        if (chkAppNormRate.isSelected()) {
            observable.setChkAppNormRate("Y");
        } else {
            observable.setChkAppNormRate("N");
        }
        observable.setTxtNormPeriod(txtNormPeriod.getText());
        observable.setSbRateCmb(CommonUtil.convertObjToStr(sbRateCmb.getSelectedItem()));

        //       int n= tblInterestTable.getRowCount();
        //       for(int i=0;i<n;i++){
        //           observable.setCboFromAmount(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,2)));
        //           observable.setCboToAmount(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,3)));
        //            observable.setCboFromPeriod(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,4)));
        //            observable.setCboToPeriod(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,5)));
        //              observable.setTxtFromPeriod(CommonUtil.convertObjToStr(tblInterestTable.getValueAt(i,5)));
        //       }
        //        tblInterestTable.setModel(observable.getTblInterestTable());
       
        if(rdoMonthEnd.isSelected()){
        observable.setDueOn(true);
        }
        else if(rdoInstallmentDay.isSelected()){
        observable.setDueOn(false);
        }
        
        // Added by nithya on 01-03-2016          
           observable.setInstallmentAmount(txtInstallmentAmount.getText());
           observable.setEffectiveDate(tdtEffectiveDate.getDateValue().toString());           
           observable.setEffectiveDateEntered(DateUtil.getDateMMDDYYYY(tdtEffectiveDate.getDateValue()));
        // End
           
           observable.setTxtGracePeriod(txtGracePeriod.getText());// Added by shany
           
           // Added by nithya on 22-09-2017 gor identifying whether group deposit product or not
           if(chkIsGroupDeposit.isSelected()){
               observable.setChkIsGroupDepositProduct("Y");
           }else{
               observable.setChkIsGroupDepositProduct("N");
           }
           // Added by nithya for adding field for interest reserve head for benevolent deposits
           observable.setTxtBenIntReserveHead(txtBenIntReserveHead.getText());
           
           // Added by nithya on 18-09-2019 for KD 606 - RD Closure - Premature And Defaulter Payment Needs Different Settings
           if(chkRDClosingOtherROI.isSelected()){
              observable.setChkRDClosingOtherROI("Y"); 
           }else{
              observable.setChkRDClosingOtherROI("N"); 
           }   
           observable.setCboprmatureCloseRate(CommonUtil.convertObjToStr(((ComboBoxModel) cboprmatureCloseRate.getModel()).getKeyForSelected()));
           observable.setCboPrmatureCloseProduct(CommonUtil.convertObjToStr(((ComboBoxModel) cboPrmatureCloseProduct.getModel()).getKeyForSelected()));
           observable.setCboIrregularRDCloseProduct(CommonUtil.convertObjToStr(((ComboBoxModel) cboIrregularRDCloseProduct.getModel()).getKeyForSelected()));
           observable.setCboIrregularRDCloseRate(CommonUtil.convertObjToStr(((ComboBoxModel) cboIrregularRDCloseRate.getModel()).getKeyForSelected()));
           
           if(chkIntForIrregularRD.isSelected()){ // Added by nithya on 18-03-2020 for KD-1535
              observable.setChkIntForIrregularRD("Y"); 
           }else{
              observable.setChkIntForIrregularRD("N"); 
           } 
           
           if(chkSpecialRD.isSelected()){ // Added by nithya on 18-03-2020 for KD-1535
              observable.setChkSpecialRD("Y"); 
           }else{
              observable.setChkSpecialRD("N"); 
           } 
           
           observable.setTxtSpecialRDInstallments(txtSpecialRDInstallments.getText()); // Added by nithya on 01-04-2020 for KD-1535
           //16-06-2020  
           if(chkAgentCommSlabRequired.isSelected()){
               observable.setChkAgentCommSlabRequired("Y");
           }else{
               observable.setChkAgentCommSlabRequired("N");
           }           
           observable.setCboAgentCommCalcMethod(CommonUtil.convertObjToStr(((ComboBoxModel) cboAgentCommCalcMethod.getModel()).getKeyForSelected()));        
           observable.setTxtAgentCommSlabAmtFrom(txtAgentCommSlabAmtFrom.getText());
           observable.setTxtAgentCommSlabAmtTo(TxtAgentCommSlabAmtTo.getText());
           observable.setTxtAgentCommSlabPercent(txtAgentCommSlabPercent.getText());    
           // End
    }

    public void setHelpMessage() {
        DepositsProductMRB objMandatoryRB = new DepositsProductMRB();
        txtAcctHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctHd"));
        txtProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductID"));
        txtDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesc"));
        cboOperatesLike.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOperatesLike"));
        rdoCalcTDS_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCalcTDS_Yes"));
        rdoRegular.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoRegular"));
        //        rdoCalcTDS_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCalcTDS_Yes"));
        //        rdoCalcTDS_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCalcTDS_Yes"));
        rdoPayInterestOnHoliday_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPayInterestOnHoliday_Yes"));
        //        rdoAmountRounded_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAmountRounded_Yes"));
        cboRoundOffCriteria.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRoundOffCriteria"));
        rdoInterestAfterMaturity_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoInterestAfterMaturity_Yes"));
        cboMaturityInterestType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMaturityInterestType"));
        cboMaturityInterestRate.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMaturityInterestRate"));
        txtPeriodInMultiplesOf.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPeriodInMultiplesOf"));
        cboPeriodInMultiplesOf.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPeriodInMultiplesOf"));
        txtMaxDepositPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDepositPeriod"));
        cboMaxDepositPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMaxDepositPeriod"));
        txtMinDepositPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDepositPeriod"));
        cboMinDepositPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinDepositPeriod"));
        txtDepositPerUnit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDepositPerUnit"));
        //        cboDepositPerUnit.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepositPerUnit"));
        txtAmtInMultiplesOf.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmtInMultiplesOf"));
        txtMinDepositAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDepositAmt"));
        txtMaxDepositAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDepositAmt"));
        rdoCalcMaturityValue_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCalcMaturityValue_Yes"));
        rdoProvisionOfInterest_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoProvisionOfInterest_Yes"));
        txtInterestOnMaturedDeposits.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestOnMaturedDeposits"));
        txtAlphaSuffix.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAlphaSuffix"));
        txtMaxAmtOfCashPayment.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxAmtOfCashPayment"));
        txtMinAmtOfPAN.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinAmtOfPAN"));
        txtAcctNumberPattern.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctNumberPattern"));
        //        txtScheme.setHelpMessage(lblMsg, objMandatoryRB.getString("txtScheme"));
        txtLastAcctNumber.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLastAcctNumber"));
        txtSuffix.setHelpMessage(lblMsg, objMandatoryRB.getString("setTxtSuffix"));
        txtLimitForBulkDeposit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLimitForBulkDeposit"));
        rdoTransferToMaturedDeposits_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTransferToMaturedDeposits_Yes"));
        rdoAutoAdjustment_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAutoAdjustment_Yes"));
        rdoAdjustIntOnDeposits_No.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAdjustIntOnDeposits_No"));
        rdoAdjustPrincipleToLoan_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAdjustPrincipleToLoan_Yes"));
        rdoExtnOfDepositBeforeMaturity_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoExtnOfDepositBeforeMaturity_Yes"));
        txtAfterHowManyDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAfterHowManyDays"));
        txtAdvanceMaturityNoticeGenPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAdvanceMaturityNoticeGenPeriod"));
        txtPrematureWithdrawal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrematureWithdrawal"));
        rdoFlexiFromSBCA_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoFlexiFromSBCA_Yes"));
        rdoTermDeposit_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTermDeposit_Yes"));
        rdoIntroducerReqd_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIntroducerReqd_Yes"));
        rdoSystemCalcValues_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoSystemCalcValues_Yes"));
        txtNoOfPartialWithdrawalAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfPartialWithdrawalAllowed"));
        txtServiceCharge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceCharge"));
        txtMaxNoOfPartialWithdrawalAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxNoOfPartialWithdrawalAllowed"));
        txtMaxAmtOfPartialWithdrawalAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxAmtOfPartialWithdrawalAllowed"));
        txtMinAmtOfPartialWithdrawalAllowed.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinAmtOfPartialWithdrawalAllowed"));
        txtWithdrawalsInMultiplesOf.setHelpMessage(lblMsg, objMandatoryRB.getString("txtWithdrawalsInMultiplesOf"));
        rdoPartialWithdrawalAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPartialWithdrawalAllowed_Yes"));
        rdoWithdrawalWithInterest_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoWithdrawalWithInterest_Yes"));
        cboIntCalcMethod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntCalcMethod"));
        cboIntMaintainedAsPartOf.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntMaintainedAsPartOf"));
        rdoIntProvisioningApplicable_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIntProvisioningApplicable_Yes"));
        cboIntProvisioningFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntProvisioningFreq"));
        cboProvisioningLevel.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProvisioningLevel"));
        cboNoOfDays.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNoOfDays"));
        cboIntCompoundingFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntCompoundingFreq"));
        cboIntApplnFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntApplnFreq"));
        tdtNextInterestAppliedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNextInterestAppliedDate"));
        tdtLastInterestAppliedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastInterestAppliedDate"));
        tdtNextIntProvisionalDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtNextIntProvisionalDate"));
        tdtLastIntProvisionalDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastIntProvisionalDate"));
        cboIntRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntRoundOff"));
        txtMinIntToBePaid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinIntToBePaid"));
        rdoRecalcOfMaturityValue_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoRecalcOfMaturityValue_Yes"));
        txtIntProvisioningAcctHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntProvisioningAcctHd"));
        txtIntOnMaturedDepositAcctHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntOnMaturedDepositAcctHead"));
        txtFixedDepositAcctHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFixedDepositAcctHead"));
        //        txtPenalCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalCharges"));

        txttMaturityDepositAcctHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txttMaturityDepositAcctHead"));
        txtIntProvisionOfMaturedDeposit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntProvisionOfMaturedDeposit"));
        txtIntPaybleGLHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntPaybleGLHead"));

        txtCommisionPaybleGLHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionPaybleGLHead"));

        txtIntDebitPLHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntDebitPLHead"));
        txtAcctHeadForFloatAcct.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctHeadForFloatAcct"));
        cboIntCriteria.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntCriteria"));
        rdoAutoRenewalAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAutoRenewalAllowed_Yes"));
        rdoRenewalOfDepositAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoRenewalOfDepositAllowed_Yes"));
        txtMinNoOfDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinNoOfDays"));
        txtMaxNopfTimes.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxNopfTimes"));
        cboIntType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntType"));
        //        tdtMaturityDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtMaturityDate"));
        //        tdtPresentDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPresentDate"));
        cboIntPeriodForBackDatedRenewal.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntPeriodForBackDatedRenewal"));
        txtIntPeriodForBackDatedRenewal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntPeriodForBackDatedRenewal"));
        rdoPenaltyOnLateInstallmentsChargeble_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPenaltyOnLateInstallmentsChargeble_Yes"));
        txtMaturityDateAfterLastInstalPaid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaturityDateAfterLastInstalPaid"));
        rdoCanReceiveExcessInstal_yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCanReceiveExcessInstal_yes"));
        rdoInstallmentInRecurringDepositAcct_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoInstallmentInRecurringDepositAcct_Yes"));
        cboInstallmentToBeCharged.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstallmentToBeCharged"));

        //added for daily deposits scheme
        cboDepositsFrequency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDepositsFrequency"));

        cboChangeValue.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChangeValue"));
        cboProdCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdCurrency"));
        rdoIntPayableOnExcessInstal_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIntPayableOnExcessInstal_Yes"));
        rdoRecurringDepositToFixedDeposit_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoRecurringDepositToFixedDeposit_Yes"));
        txtCutOffDayForPaymentOfInstal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCutOffDayForPaymentOfInstal"));
        rdoLastInstallmentAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLastInstallmentAllowed_Yes"));
        //        tdtFromDepositDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDepositDate"));
        //        tdtChosenDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChosenDate"));
        //        cboInterestType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInterestType"));

        //        cboFromAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFromAmount"));
        //        cboToAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToAmount"));
        cboFromPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFromPeriod"));
        cboToPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToPeriod"));
        txtFromPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromPeriod"));
        txtToPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToPeriod"));
        txtCommisionRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionRate"));

        //        txtDelayedChargesAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDelayedChargesAmt"));
        //        cboInterestType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInterestType"));
        cboProductType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProductType"));
        txtInstallmentFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentFrom"));
        txtInstallmentTo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentTo"));
        txtPenal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenal"));
        txtInstallmentNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstallmentNo"));
        rdoMonthEnd.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMonthEnd"));
        rdoInstallmentDay.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoInstallmentDay"));

    }

    private void setMaximumLength() {
        txtProductID.setMaxLength(8);
        txtDesc.setMaxLength(128);
        txtAcctHd.setMaxLength(16);
        txtAcctHd.setAllowAll(true);
        /*----Scheme----*/
        //        txtDepositPerUnit.setMaxLength(3);
        txtDepositPerUnit.setValidation(new CurrencyValidation(14, 2));
        txtMinDepositPeriod.setMaxLength(3);
        txtMinDepositPeriod.setValidation(new NumericValidation(3, 0));
        txtMaxDepositPeriod.setMaxLength(5);
        txtMaxDepositPeriod.setValidation(new NumericValidation(3, 0));
        txtPeriodInMultiplesOf.setMaxLength(3);
        txtPeriodInMultiplesOf.setValidation(new NumericValidation(3, 0));
        txtMinDepositAmt.setMaxLength(16);
        txtMinDepositAmt.setValidation(new CurrencyValidation(14, 2));
        txtMaxDepositAmt.setMaxLength(16);
        txtMaxDepositAmt.setValidation(new CurrencyValidation(14, 2));
        txtAmtInMultiplesOf.setMaxLength(16);
        txtAmtInMultiplesOf.setValidation(new NumericValidation(14, 2));
        txtAfterHowManyDays.setMaxLength(3);
        txtAfterHowManyDays.setValidation(new NumericValidation(3, 0));
        txtAdvanceMaturityNoticeGenPeriod.setMaxLength(3);
        txtAdvanceMaturityNoticeGenPeriod.setValidation(new NumericValidation(3, 0));
        txtInterestOnMaturedDeposits.setMaxLength(6);
        txtInterestOnMaturedDeposits.setValidation(new NumericValidation(4, 2));
        txtNoOfPartialWithdrawalAllowed.setMaxLength(3);
        txtNoOfPartialWithdrawalAllowed.setValidation(new NumericValidation(3, 0));
        txtMaxNoOfPartialWithdrawalAllowed.setMaxLength(16);
        txtMaxNoOfPartialWithdrawalAllowed.setValidation(new NumericValidation(3, 0));
        txtMaxAmtOfPartialWithdrawalAllowed.setMaxLength(3);
        txtMaxAmtOfPartialWithdrawalAllowed.setValidation(new CurrencyValidation(14, 2));
        txtMinAmtOfPartialWithdrawalAllowed.setMaxLength(16);
        txtMinAmtOfPartialWithdrawalAllowed.setValidation(new CurrencyValidation(14, 2));
        txtWithdrawalsInMultiplesOf.setMaxLength(3);
        txtWithdrawalsInMultiplesOf.setValidation(new NumericValidation(3, 0));
        txtPrematureWithdrawal.setMaxLength(3);
        txtPrematureWithdrawal.setValidation(new NumericValidation(3, 0));
        txtAcctNumberPattern.setMaxLength(8);
        txtLastAcctNumber.setValidation(new NumericValidation(12, 0));
        txtLastAcctNumber.setMaxLength(12);
        txtSuffix.setMaxLength(12);
        txtSuffix.setValidation(new NumericValidation(10, 0));
        txtAlphaSuffix.setMaxLength(16);
        txtMaxAmtOfCashPayment.setMaxLength(16);
        txtMaxAmtOfCashPayment.setValidation(new CurrencyValidation(14, 2));
        txtMinAmtOfPAN.setMaxLength(16);
        txtMinAmtOfPAN.setValidation(new CurrencyValidation(14, 2));
        txtLimitForBulkDeposit.setMaxLength(16);
        txtLimitForBulkDeposit.setValidation(new CurrencyValidation(14, 2));
        txtAfterNoDays.setValidation(new NumericValidation(5, 0));
        /*----Interest Payment----*/
        txtMinIntToBePaid.setMaxLength(16);
        txtMinIntToBePaid.setValidation(new CurrencyValidation(14, 2));
        /*----Ac Hd----*/
        txtIntProvisioningAcctHd.setMaxLength(16);
        txtIntPaybleGLHead.setMaxLength(16);
        txtCommisionPaybleGLHead.setMaxLength(16);
        txtIntDebitPLHead.setMaxLength(16);
        txttMaturityDepositAcctHead.setMaxLength(16);
        txtIntOnMaturedDepositAcctHead.setMaxLength(16);
        txtIntProvisionOfMaturedDeposit.setMaxLength(16);
        txtAcctHeadForFloatAcct.setMaxLength(16);
        txtFixedDepositAcctHead.setMaxLength(16);
        txtPenalCharges.setMaxLength(16);
        txtInterestRecoveryAcHd.setMaxLength(16);
        /*----Tax----*/
        /*----Renewal/Closure----*/
        txtIntPeriodForBackDatedRenewal.setMaxLength(3);
        txtIntPeriodForBackDatedRenewal.setValidation(new NumericValidation(3, 0));
        txtMinNoOfDays.setMaxLength(3);
        txtMinNoOfDays.setValidation(new NumericValidation(3, 0));
        txtMaxNopfTimes.setMaxLength(3);
        txtMaxNopfTimes.setValidation(new NumericValidation(3, 0));
        txtMaxNopfTimes1.setMaxLength(3);
        txtMaxNopfTimes1.setValidation(new NumericValidation(3, 0));
        txtMaxPeriodMDt.setValidation(new NumericValidation(3, 0));
        /*----RD----*/
        txtMaturityDateAfterLastInstalPaid.setMaxLength(3);
        txtMaturityDateAfterLastInstalPaid.setValidation(new NumericValidation(3, 0));
        txtCutOffDayForPaymentOfInstal.setMaxLength(3);
        txtCutOffDayForPaymentOfInstal.setValidation(new NumericValidation(3, 0));
        txtAgentCommision.setMaxLength(3);
        txtAgentCommision.setValidation(new NumericValidation(3, 0));
        txtMinimumPeriod.setMaxLength(3);
        txtMinimumPeriod.setValidation(new NumericValidation(3, 0));
        txtInterestNotPayingValue.setMaxLength(3);
        txtInterestNotPayingValue.setValidation(new NumericValidation(3, 0));
        txtFromAmt.setMaxLength(14);
        txtFromAmt.setValidation(new NumericValidation(12, 2));
        txtToAmt.setMaxLength(14);
        txtToAmt.setValidation(new NumericValidation(12, 2));
        txtDelayedChargesAmt.setMaxLength(7);
        txtDelayedChargesAmt.setValidation(new NumericValidation(5, 2));
        txtFromPeriod.setMaxLength(6);
        txtFromPeriod.setValidation(new NumericValidation());
        txtToPeriod.setMaxLength(6);
        txtToPeriod.setValidation(new NumericValidation());
        txtCommisionRate.setMaxLength(6);
        txtCommisionRate.setValidation(new NumericValidation(4, 2));
        txtServiceCharge.setValidation(new NumericValidation(3, 2));
        txtMinimumRenewalDeposit.setValidation(new NumericValidation(14, 2));
        txtRenewedclosedbefore.setValidation(new NumericValidation(14, 2));
        txtPostageAcHd.setMaxLength(16);
        txtRDIrregularIfInstallmentDue.setValidation(new NumericValidation(3, 0));

    }

    public static void main(String args[]) {
        javax.swing.JFrame jf = new javax.swing.JFrame();

        DepositsProductUI bui = new DepositsProductUI();
        jf.setSize(300, 525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CTextField TxtAgentCommSlabAmtTo;
    private com.see.truetransact.uicomponent.CButton btnAcctHead;
    private com.see.truetransact.uicomponent.CButton btnAcctHeadForFloatAcct;
    private com.see.truetransact.uicomponent.CButton btnAgentCommissionSlabDelete;
    private com.see.truetransact.uicomponent.CButton btnAgentCommissionSlabNew;
    private com.see.truetransact.uicomponent.CButton btnAgentCommissionSlabSave;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBenIntReserveHead;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCommisionPaybleGLHead;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnDelayedDel;
    private com.see.truetransact.uicomponent.CButton btnDelayedNew;
    private com.see.truetransact.uicomponent.CButton btnDelayedSave;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnFixedDepositAcctHead;
    private com.see.truetransact.uicomponent.CButton btnIntDebitPLHead;
    private com.see.truetransact.uicomponent.CButton btnIntOnMaturedDepositAcctHead;
    private com.see.truetransact.uicomponent.CButton btnIntPaybleGLHead;
    private com.see.truetransact.uicomponent.CButton btnIntProvisionOfMaturedDeposit;
    private com.see.truetransact.uicomponent.CButton btnIntProvisioningAcctHd;
    private com.see.truetransact.uicomponent.CButton btnInterestRecoveryAcHd;
    private com.see.truetransact.uicomponent.CButton btnMaturityDepositAcctHead;
    private com.see.truetransact.uicomponent.CLabel btnMaturityInterestRate;
    private com.see.truetransact.uicomponent.CLabel btnMaturityInterestType;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPenalCharges;
    private com.see.truetransact.uicomponent.CButton btnPostageAcHd;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTDSGLAcctHd;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnTransferOutHead;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnWeeklyDepSlabDelete;
    private com.see.truetransact.uicomponent.CButton btnWeeklyDepSlabNew;
    private com.see.truetransact.uicomponent.CButton btnWeeklyDepSlabSave;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel3;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CPanel cPanel2;
    private com.see.truetransact.uicomponent.CPanel cPanel4;
    private com.see.truetransact.uicomponent.CComboBox cboAgentCommCalcMethod;
    private com.see.truetransact.uicomponent.CComboBox cboAgentCommision;
    private com.see.truetransact.uicomponent.CComboBox cboChangeValue;
    private com.see.truetransact.uicomponent.CComboBox cboCutOffDayForPaymentOfInstal;
    private com.see.truetransact.uicomponent.CComboBox cboDepositsFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboDepositsProdFixd;
    private com.see.truetransact.uicomponent.CComboBox cboEitherofTwoRatesChoose;
    private com.see.truetransact.uicomponent.CComboBox cboFromPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboInstType;
    private com.see.truetransact.uicomponent.CComboBox cboInstallmentToBeCharged;
    private com.see.truetransact.uicomponent.CComboBox cboIntApplnFreq;
    private com.see.truetransact.uicomponent.CComboBox cboIntCalcMethod;
    private com.see.truetransact.uicomponent.CComboBox cboIntCompoundingFreq;
    private com.see.truetransact.uicomponent.CComboBox cboIntCriteria;
    private com.see.truetransact.uicomponent.CComboBox cboIntMaintainedAsPartOf;
    private com.see.truetransact.uicomponent.CComboBox cboIntPeriodForBackDatedRenewal;
    private com.see.truetransact.uicomponent.CComboBox cboIntProvisioningFreq;
    private com.see.truetransact.uicomponent.CComboBox cboIntRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboIntType;
    private com.see.truetransact.uicomponent.CComboBox cboInterestNotPayingValue;
    private com.see.truetransact.uicomponent.CComboBox cboInterestType;
    private com.see.truetransact.uicomponent.CComboBox cboIrregularRDCloseProduct;
    private com.see.truetransact.uicomponent.CComboBox cboIrregularRDCloseRate;
    private com.see.truetransact.uicomponent.CComboBox cboMaturityDateAfterLastInstalPaid;
    private com.see.truetransact.uicomponent.CComboBox cboMaturityInterestRate;
    private com.see.truetransact.uicomponent.CComboBox cboMaturityInterestType;
    private com.see.truetransact.uicomponent.CComboBox cboMaxDepositPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboMaxPeriodMDt;
    private com.see.truetransact.uicomponent.CComboBox cboMinDepositPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboMinNoOfDays;
    private com.see.truetransact.uicomponent.CComboBox cboMinimumPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboMinimumRenewalDeposit;
    private com.see.truetransact.uicomponent.CComboBox cboMonthlyIntCalcMethod;
    private com.see.truetransact.uicomponent.CComboBox cboNoOfDays;
    private com.see.truetransact.uicomponent.CComboBox cboOperatesLike;
    private com.see.truetransact.uicomponent.CComboBox cboPeriodInMultiplesOf;
    private com.see.truetransact.uicomponent.CComboBox cboPreMatIntType;
    private com.see.truetransact.uicomponent.CComboBox cboPrematureWithdrawal;
    private com.see.truetransact.uicomponent.CComboBox cboPrmatureCloseProduct;
    private com.see.truetransact.uicomponent.CComboBox cboProdCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboProductType;
    private com.see.truetransact.uicomponent.CComboBox cboProvisioningLevel;
    private com.see.truetransact.uicomponent.CComboBox cboRenewedclosedbefore;
    private com.see.truetransact.uicomponent.CComboBox cboRoundOffCriteria;
    private com.see.truetransact.uicomponent.CComboBox cboRoundOffCriteriaPenal;
    private com.see.truetransact.uicomponent.CComboBox cboToPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboWeekly;
    private com.see.truetransact.uicomponent.CComboBox cboprmatureCloseRate;
    private com.see.truetransact.uicomponent.CCheckBox cbxInterstRoundTime;
    private com.see.truetransact.uicomponent.CCheckBox chkAgentCommSlabRequired;
    private com.see.truetransact.uicomponent.CCheckBox chkAppNormRate;
    private com.see.truetransact.uicomponent.CCheckBox chkCanZeroBalAct;
    private com.see.truetransact.uicomponent.CCheckBox chkCummCertPrint;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositUnlien;
    private com.see.truetransact.uicomponent.CCheckBox chkDifferentROI;
    private com.see.truetransact.uicomponent.CCheckBox chkExcludeLienFrmIntrstAppl;
    private com.see.truetransact.uicomponent.CCheckBox chkExcludeLienFrmStanding;
    private com.see.truetransact.uicomponent.CCheckBox chkFDRenewalSameNoTranPrincAmt;
    private com.see.truetransact.uicomponent.CCheckBox chkIntApplicationSlab;
    private com.see.truetransact.uicomponent.CCheckBox chkIntEditable;
    private com.see.truetransact.uicomponent.CCheckBox chkIntForIrregularRD;
    private com.see.truetransact.uicomponent.CCheckBox chkIntRateApp;
    private com.see.truetransact.uicomponent.CCheckBox chkIntRateDeathMarked;
    private com.see.truetransact.uicomponent.CCheckBox chkIsGroupDeposit;
    private com.see.truetransact.uicomponent.CCheckBox chkPrematureClosure;
    private com.see.truetransact.uicomponent.CCheckBox chkRDClosingOtherROI;
    private com.see.truetransact.uicomponent.CCheckBox chkRateAsOnDateOfMaturity;
    private com.see.truetransact.uicomponent.CCheckBox chkRateAsOnDateOfRenewal;
    private com.see.truetransact.uicomponent.CCheckBox chkRdNature;
    private com.see.truetransact.uicomponent.CCheckBox chkSlabWiseCommision;
    private com.see.truetransact.uicomponent.CCheckBox chkSlabWiseInterest;
    private com.see.truetransact.uicomponent.CCheckBox chkSpecialRD;
    private com.see.truetransact.uicomponent.CCheckBox chkWeeklySpecial;
    private com.see.truetransact.uicomponent.CComboBox cmbSbProduct;
    private com.see.truetransact.uicomponent.CLabel lbInstallmentFrom;
    private com.see.truetransact.uicomponent.CLabel lbInstallmentFrom2;
    private com.see.truetransact.uicomponent.CLabel lblAcctHd;
    private com.see.truetransact.uicomponent.CLabel lblAcctHeadForFloatAcct;
    private com.see.truetransact.uicomponent.CLabel lblAcctNumberPattern;
    private com.see.truetransact.uicomponent.CLabel lblAcctNumberPattern1;
    private com.see.truetransact.uicomponent.CLabel lblAdjustIntOnDeposits;
    private com.see.truetransact.uicomponent.CLabel lblAdjustPrincipleToLoan;
    private com.see.truetransact.uicomponent.CLabel lblAdvanceMaturityNoticeGenPeriod;
    private com.see.truetransact.uicomponent.CLabel lblAfterHowManyDays;
    private com.see.truetransact.uicomponent.CLabel lblAfterHowManyDaysDisplay;
    private com.see.truetransact.uicomponent.CLabel lblAfterNoDays;
    private com.see.truetransact.uicomponent.CLabel lblAgentCommSlabAmtFrom;
    private com.see.truetransact.uicomponent.CLabel lblAgentCommSlabAmtTo;
    private com.see.truetransact.uicomponent.CLabel lblAgentCommSlabPercent;
    private com.see.truetransact.uicomponent.CLabel lblAgentCommision;
    private com.see.truetransact.uicomponent.CLabel lblAlphaSuffix;
    private com.see.truetransact.uicomponent.CLabel lblAmountRounded;
    private com.see.truetransact.uicomponent.CLabel lblAmtInMultiplesOf;
    private com.see.truetransact.uicomponent.CLabel lblAppNormRate;
    private com.see.truetransact.uicomponent.CLabel lblAutoAdjustment;
    private com.see.truetransact.uicomponent.CLabel lblAutoRenewalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblAutoRenewalAllowed1;
    private com.see.truetransact.uicomponent.CLabel lblBasedOnDepRate;
    private com.see.truetransact.uicomponent.CLabel lblBothRateAvail;
    private com.see.truetransact.uicomponent.CLabel lblBothRateNotAvail;
    private com.see.truetransact.uicomponent.CLabel lblCalcMaturityValue;
    private com.see.truetransact.uicomponent.CLabel lblCalcMaturityValue1;
    private com.see.truetransact.uicomponent.CLabel lblCalcTDS;
    private com.see.truetransact.uicomponent.CLabel lblCanReceiveExcessInstal;
    private com.see.truetransact.uicomponent.CLabel lblCanZeroBalAct;
    private com.see.truetransact.uicomponent.CLabel lblCategoryBenifitRate;
    private com.see.truetransact.uicomponent.CLabel lblChangeValue;
    private com.see.truetransact.uicomponent.CLabel lblChosenDate;
    private com.see.truetransact.uicomponent.CLabel lblCommisionPaybleGLHead;
    private com.see.truetransact.uicomponent.CLabel lblCustomerInterestNotPaying;
    private com.see.truetransact.uicomponent.CLabel lblCutOffDayForPaymentOfInstal;
    private com.see.truetransact.uicomponent.CLabel lblDailyDepositCalc;
    private com.see.truetransact.uicomponent.CLabel lblDate;
    private com.see.truetransact.uicomponent.CLabel lblDelayedChargesAmt;
    private com.see.truetransact.uicomponent.CLabel lblDelayedChargesDet;
    private com.see.truetransact.uicomponent.CLabel lblDelayedFromAmt;
    private com.see.truetransact.uicomponent.CLabel lblDelayedToAmt;
    private com.see.truetransact.uicomponent.CLabel lblDepositPerUnit;
    private com.see.truetransact.uicomponent.CLabel lblDepositsFrequency;
    private com.see.truetransact.uicomponent.CLabel lblDepositsProdFixd;
    private com.see.truetransact.uicomponent.CLabel lblDesc;
    private com.see.truetransact.uicomponent.CLabel lblDifferentROI;
    private com.see.truetransact.uicomponent.CLabel lblDiscounted;
    private com.see.truetransact.uicomponent.CLabel lblDoublingScheme;
    private com.see.truetransact.uicomponent.CLabel lblDueOn;
    private com.see.truetransact.uicomponent.CLabel lblEffectiveDate;
    private com.see.truetransact.uicomponent.CLabel lblExtensionBeyondOriginalDate;
    private com.see.truetransact.uicomponent.CLabel lblExtensionPenal;
    private com.see.truetransact.uicomponent.CLabel lblExtnOfDepositBeforeMaturity;
    private com.see.truetransact.uicomponent.CLabel lblFDRenewalSameNoTranPrincAmt;
    private com.see.truetransact.uicomponent.CLabel lblFixedDepositAcctHead;
    private com.see.truetransact.uicomponent.CLabel lblFlexiFromSBCA;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblFromDepositDate;
    private com.see.truetransact.uicomponent.CLabel lblFromPeriod;
    private com.see.truetransact.uicomponent.CLabel lblGracePeriod;
    private com.see.truetransact.uicomponent.CLabel lblIncaseOfIrregularRD;
    private com.see.truetransact.uicomponent.CLabel lblInsBeyondMaturityDtYesorNo;
    private com.see.truetransact.uicomponent.CLabel lblInstType;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentAmount;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentInRecurringDepositAcct;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentNo;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentTo;
    private com.see.truetransact.uicomponent.CLabel lblInstallmentToBeCharged;
    private com.see.truetransact.uicomponent.CLabel lblIntApplicationSlab;
    private com.see.truetransact.uicomponent.CLabel lblIntApplnFreq;
    private com.see.truetransact.uicomponent.CLabel lblIntCalcMethod;
    private com.see.truetransact.uicomponent.CLabel lblIntCompoundingFreq;
    private com.see.truetransact.uicomponent.CLabel lblIntCriteria;
    private com.see.truetransact.uicomponent.CLabel lblIntDebitPLHead;
    private com.see.truetransact.uicomponent.CLabel lblIntMaintainedAsPartOf;
    private com.see.truetransact.uicomponent.CLabel lblIntOnMaturedDepositAcctHead;
    private com.see.truetransact.uicomponent.CLabel lblIntPayableOnExcessInstal;
    private com.see.truetransact.uicomponent.CLabel lblIntPaybleGLHead;
    private com.see.truetransact.uicomponent.CLabel lblIntPeriodForBackDatedRenewal;
    private com.see.truetransact.uicomponent.CLabel lblIntProvisionOfMaturedDeposit;
    private com.see.truetransact.uicomponent.CLabel lblIntProvisioningAcctHd;
    private com.see.truetransact.uicomponent.CLabel lblIntProvisioningApplicable;
    private com.see.truetransact.uicomponent.CLabel lblIntProvisioningFreq;
    private com.see.truetransact.uicomponent.CLabel lblIntRateApp;
    private com.see.truetransact.uicomponent.CLabel lblIntRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblIntType;
    private com.see.truetransact.uicomponent.CLabel lblInterestAfterMaturity;
    private com.see.truetransact.uicomponent.CLabel lblInterestOnMaturedDeposits;
    private com.see.truetransact.uicomponent.CLabel lblInterestOnMaturedDepositsDays;
    private com.see.truetransact.uicomponent.CLabel lblInterestRecoveryAcHd;
    private com.see.truetransact.uicomponent.CLabel lblInterestType;
    private com.see.truetransact.uicomponent.CLabel lblInterestalreadyPaid;
    private com.see.truetransact.uicomponent.CLabel lblInterestrateappliedoverdue;
    private com.see.truetransact.uicomponent.CLabel lblIntroducerReqd;
    private com.see.truetransact.uicomponent.CLabel lblLastInstallmentAllowed;
    private com.see.truetransact.uicomponent.CLabel lblLastIntProvisionalDate;
    private com.see.truetransact.uicomponent.CLabel lblLastInterestAppliedDate;
    private com.see.truetransact.uicomponent.CLabel lblLimitForBulkDeposit;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDate;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDateAfterLastInstalPaid;
    private com.see.truetransact.uicomponent.CLabel lblMaturityDepositAcctHead;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmtOfCashPayment;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmtOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmtPartialWithdrawalPercent;
    private com.see.truetransact.uicomponent.CLabel lblMaxDepositAmt;
    private com.see.truetransact.uicomponent.CLabel lblMaxDepositPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMaxNoOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblMaxNopfTimes;
    private com.see.truetransact.uicomponent.CLabel lblMaxNopfTimes1;
    private com.see.truetransact.uicomponent.CLabel lblMinAmtOfPAN;
    private com.see.truetransact.uicomponent.CLabel lblMinAmtOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblMinDepositAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinDepositPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMinIntToBePaid;
    private com.see.truetransact.uicomponent.CLabel lblMinNoOfDays;
    private com.see.truetransact.uicomponent.CLabel lblMinimumPeriod;
    private com.see.truetransact.uicomponent.CLabel lblMinimumRenewalDeposit;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNextIntProvisionalDate;
    private com.see.truetransact.uicomponent.CLabel lblNextInterestAppliedDate;
    private com.see.truetransact.uicomponent.CLabel lblNoOfDays;
    private com.see.truetransact.uicomponent.CLabel lblNoOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblNormPeriod;
    private com.see.truetransact.uicomponent.CLabel lblOneRateAvail;
    private com.see.truetransact.uicomponent.CLabel lblOperatesLike;
    private com.see.truetransact.uicomponent.CLabel lblPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CLabel lblPartialWithdrawlAllowedForDD;
    private com.see.truetransact.uicomponent.CLabel lblPayInterestOnHoliday;
    private com.see.truetransact.uicomponent.CLabel lblPenal;
    private com.see.truetransact.uicomponent.CLabel lblPenalCharges;
    private com.see.truetransact.uicomponent.CLabel lblPenalRounded;
    private com.see.truetransact.uicomponent.CLabel lblPenaltyOnLateInstallmentsChargeble;
    private com.see.truetransact.uicomponent.CLabel lblPeriodInMultiplesOf;
    private com.see.truetransact.uicomponent.CLabel lblPostageAcHd;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosure;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosureApply;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosureApplyROI;
    private com.see.truetransact.uicomponent.CLabel lblPrematureWithdrawal;
    private com.see.truetransact.uicomponent.CLabel lblProdCurrency;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblProductType;
    private com.see.truetransact.uicomponent.CLabel lblProductType2;
    private com.see.truetransact.uicomponent.CLabel lblProvisionOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblProvisioningLevel;
    private com.see.truetransact.uicomponent.CLabel lblRDIrregularIfInstallmentDue;
    private com.see.truetransact.uicomponent.CLabel lblRateAsOnDateOfMaturity;
    private com.see.truetransact.uicomponent.CLabel lblRateAsOnDateOfRenewal;
    private com.see.truetransact.uicomponent.CLabel lblRateInterest;
    private com.see.truetransact.uicomponent.CLabel lblRecalcOfMaturityValue;
    private com.see.truetransact.uicomponent.CLabel lblRecurringDepositToFixedDeposit;
    private com.see.truetransact.uicomponent.CLabel lblRenewalOfDepositAllowed;
    private com.see.truetransact.uicomponent.CLabel lblRenewalOfDepositAllowed1;
    private com.see.truetransact.uicomponent.CLabel lblRenewedclosedbefore;
    private com.see.truetransact.uicomponent.CLabel lblRoundOffCriteria;
    private com.see.truetransact.uicomponent.CLabel lblRoundOffCriteriaPenal;
    private com.see.truetransact.uicomponent.CLabel lblSbRateInterestDeath;
    private com.see.truetransact.uicomponent.CLabel lblSchemeClosingDate;
    private com.see.truetransact.uicomponent.CLabel lblSchemeIntroDate;
    private com.see.truetransact.uicomponent.CLabel lblServiceCharge;
    private com.see.truetransact.uicomponent.CLabel lblServiceChargePercent;
    private com.see.truetransact.uicomponent.CLabel lblServiceChargePercentage;
    private com.see.truetransact.uicomponent.CLabel lblSlabWiseCommision;
    private com.see.truetransact.uicomponent.CLabel lblSlabWiseInterest;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpecialRDInstalments;
    private com.see.truetransact.uicomponent.CLabel lblStaffAccount;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSystemCalcValues;
    private com.see.truetransact.uicomponent.CLabel lblTDSGLAcctHd;
    private com.see.truetransact.uicomponent.CLabel lblTermDeposit;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblToPeriod;
    private com.see.truetransact.uicomponent.CLabel lblTransferOutHead;
    private com.see.truetransact.uicomponent.CLabel lblTransferToMaturedDeposits;
    private com.see.truetransact.uicomponent.CLabel lblTypesOfDeposit;
    private com.see.truetransact.uicomponent.CLabel lblWeekly;
    private com.see.truetransact.uicomponent.CLabel lblWithPeriod;
    private com.see.truetransact.uicomponent.CLabel lblWithdrawalWithInterest;
    private com.see.truetransact.uicomponent.CLabel lblWithdrawalsInMultiplesOf;
    private com.see.truetransact.uicomponent.CLabel lblclosedwithinperiod;
    private com.see.truetransact.uicomponent.CLabel lblpreMatIntType;
    private com.see.truetransact.uicomponent.CMenuBar mbrDepositsProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcHd;
    private com.see.truetransact.uicomponent.CPanel panAcHdRenewal;
    private com.see.truetransact.uicomponent.CPanel panAccHead;
    private com.see.truetransact.uicomponent.CPanel panAccountHead;
    private com.see.truetransact.uicomponent.CPanel panAdvanceMaturityNoticeGenPeriod;
    private com.see.truetransact.uicomponent.CPanel panAfterHowManyDays;
    private com.see.truetransact.uicomponent.CPanel panAgenCommissionSlab;
    private com.see.truetransact.uicomponent.CPanel panAgentCommCalcMethod;
    private com.see.truetransact.uicomponent.CPanel panAgentCommSlab;
    private com.see.truetransact.uicomponent.CPanel panAgentCommSlabDetails;
    private com.see.truetransact.uicomponent.CPanel panAgentCommision;
    private com.see.truetransact.uicomponent.CPanel panAgentsCalculations;
    private com.see.truetransact.uicomponent.CPanel panAmountRounded;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewalAllowed;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewalAllowed1;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewalAllowed3;
    private com.see.truetransact.uicomponent.CPanel panAutoRenewalAllowed4;
    private com.see.truetransact.uicomponent.CPanel panBothRateNotAvail;
    private com.see.truetransact.uicomponent.CPanel panBtnDelayed;
    private com.see.truetransact.uicomponent.CPanel panButtons;
    private com.see.truetransact.uicomponent.CPanel panCalcMaturityValue;
    private com.see.truetransact.uicomponent.CPanel panCalcTDS;
    private com.see.truetransact.uicomponent.CPanel panCanReceiveExcessInstal;
    private com.see.truetransact.uicomponent.CPanel panDailyIntCalc;
    private com.see.truetransact.uicomponent.CPanel panDate;
    private com.see.truetransact.uicomponent.CPanel panDelayedInstallmet;
    private com.see.truetransact.uicomponent.CPanel panDelayedInstallmets;
    private com.see.truetransact.uicomponent.CPanel panDepositPerUnit;
    private com.see.truetransact.uicomponent.CPanel panDepositsProduct;
    private com.see.truetransact.uicomponent.CPanel panDiscounted;
    private com.see.truetransact.uicomponent.CPanel panDoublingScheme;
    private com.see.truetransact.uicomponent.CPanel panExcludeLienFrm;
    private com.see.truetransact.uicomponent.CPanel panExtnOfDepositBeforeMaturity;
    private com.see.truetransact.uicomponent.CPanel panFlexiFromSBCA;
    private com.see.truetransact.uicomponent.CPanel panFloatingRateAccount;
    private com.see.truetransact.uicomponent.CPanel panFromPeriod;
    private com.see.truetransact.uicomponent.CPanel panInCaseOfLien;
    private com.see.truetransact.uicomponent.CPanel panInsBeyondMaturityDtYesorNo;
    private com.see.truetransact.uicomponent.CPanel panInsallmentDetails;
    private com.see.truetransact.uicomponent.CPanel panInstallmentInRecurringDepositAcct;
    private com.see.truetransact.uicomponent.CPanel panIntEditable;
    private com.see.truetransact.uicomponent.CPanel panIntPayableOnExcessInstal;
    private com.see.truetransact.uicomponent.CPanel panIntPeriodForBackDatedRenewal;
    private com.see.truetransact.uicomponent.CPanel panIntProvisioningApplicable;
    private com.see.truetransact.uicomponent.CPanel panIntRateforOverduePeriod;
    private com.see.truetransact.uicomponent.CPanel panInterest;
    private com.see.truetransact.uicomponent.CPanel panInterestAfterMaturity;
    private com.see.truetransact.uicomponent.CPanel panInterestNotPayingValue;
    private com.see.truetransact.uicomponent.CPanel panInterestPayment;
    private com.see.truetransact.uicomponent.CPanel panInterestalreadyPaid;
    private com.see.truetransact.uicomponent.CPanel panIntroducerReqd;
    private com.see.truetransact.uicomponent.CPanel panLastInstallmentAllowed;
    private com.see.truetransact.uicomponent.CPanel panLastInstallmentAllowedRD;
    private com.see.truetransact.uicomponent.CPanel panMaturityDateAfterLastInstalPaid;
    private com.see.truetransact.uicomponent.CPanel panMaxDepositPeriod;
    private com.see.truetransact.uicomponent.CPanel panMaxPeriodMDt;
    private com.see.truetransact.uicomponent.CPanel panMinDepositPeriod;
    private com.see.truetransact.uicomponent.CPanel panMinNoOfDays;
    private com.see.truetransact.uicomponent.CPanel panMinimumPeriod;
    private com.see.truetransact.uicomponent.CPanel panMinimumRenewalDeposit;
    private com.see.truetransact.uicomponent.CPanel panNumberPattern;
    private com.see.truetransact.uicomponent.CPanel panOneRateAvail;
    private com.see.truetransact.uicomponent.CPanel panPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CPanel panPartialWithdrawlAllowedForDD;
    private com.see.truetransact.uicomponent.CPanel panPartialWithdrawlAllowedForDD1;
    private com.see.truetransact.uicomponent.CPanel panPayInterestOnHoliday;
    private com.see.truetransact.uicomponent.CPanel panPenalRounded;
    private com.see.truetransact.uicomponent.CPanel panPenaltyOnLateInstallmentsChargeble;
    private com.see.truetransact.uicomponent.CPanel panPeriodInMultiplesOf;
    private com.see.truetransact.uicomponent.CPanel panPrematureClosureApply;
    private com.see.truetransact.uicomponent.CPanel panPrematureWithdrawal;
    private com.see.truetransact.uicomponent.CPanel panProvisionOfInterest;
    private com.see.truetransact.uicomponent.CPanel panRD;
    private com.see.truetransact.uicomponent.CPanel panRDClosingROISetting;
    private com.see.truetransact.uicomponent.CPanel panROIIrregularRDClosure;
    private com.see.truetransact.uicomponent.CPanel panROIPrematureClosure;
    private com.see.truetransact.uicomponent.CPanel panRateAsOn;
    private com.see.truetransact.uicomponent.CPanel panRdgProvisionOfInterest;
    private com.see.truetransact.uicomponent.CPanel panRecurringDepositToFixedDeposit;
    private com.see.truetransact.uicomponent.CPanel panRecurringDetails;
    private com.see.truetransact.uicomponent.CPanel panRenewalAndTax;
    private com.see.truetransact.uicomponent.CPanel panRenewalExtension;
    private com.see.truetransact.uicomponent.CPanel panRenewalOfDepositAllowed;
    private com.see.truetransact.uicomponent.CPanel panRenewalParameter;
    private com.see.truetransact.uicomponent.CPanel panRenewedclosedbefore;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureButtons;
    private com.see.truetransact.uicomponent.CPanel panSalaryStructureButtons1;
    private com.see.truetransact.uicomponent.CPanel panScheme;
    private com.see.truetransact.uicomponent.CPanel panServiceCharge;
    private com.see.truetransact.uicomponent.CPanel panSlab;
    private com.see.truetransact.uicomponent.CPanel panSlab2;
    private com.see.truetransact.uicomponent.CPanel panSlabDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSystemCalcValues;
    private com.see.truetransact.uicomponent.CPanel panTax;
    private com.see.truetransact.uicomponent.CPanel panTax1;
    private com.see.truetransact.uicomponent.CPanel panTermDeposit;
    private com.see.truetransact.uicomponent.CPanel panThriftBenevolent;
    private com.see.truetransact.uicomponent.CPanel panToPeriod;
    private com.see.truetransact.uicomponent.CPanel panTransferToMaturedDeposits;
    private com.see.truetransact.uicomponent.CPanel panTransferToMaturedDeposits1;
    private com.see.truetransact.uicomponent.CPanel panTypeOfDeposit;
    private com.see.truetransact.uicomponent.CPanel panWeeklyDepositSlab;
    private com.see.truetransact.uicomponent.CPanel panWithDrawIntPay;
    private com.see.truetransact.uicomponent.CPanel panWithPeriod;
    private com.see.truetransact.uicomponent.CPanel panWithdrawal;
    private com.see.truetransact.uicomponent.CPanel panWithdrawalWithInterest;
    private com.see.truetransact.uicomponent.CPanel panclosedwithinperiod;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAdjustIntOnDeposits;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAdjustPrincipleToLoan;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAmountRounded;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAutoAdjustment;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAutoRenewalAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBothRateNotAvail;
    private com.see.truetransact.uicomponent.CButtonGroup rdgCalcMaturityValue;
    private com.see.truetransact.uicomponent.CButtonGroup rdgCalcTDS;
    private com.see.truetransact.uicomponent.CButtonGroup rdgCanReceiveExcessInstal;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDailyDepositCalculation;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDiscounted_Rate;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDoublingScheme;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDueOn;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExtensionBeyond;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExtensionDepositBeyond;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExtensionPeneal;
    private com.see.truetransact.uicomponent.CButtonGroup rdgExtnOfDepositBeforeMaturity;
    private com.see.truetransact.uicomponent.CButtonGroup rdgFlexiFromSBCA;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInsBeyondMaturityDt;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInstallmentInRecurringDepositAcct;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIntPayableOnExcessInstal;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIntProvisioningApplicable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIntRateforOverduePeriod;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInterestAfterMaturity;
    private com.see.truetransact.uicomponent.CButtonGroup rdgInterestalreadyPaid;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIntroducerReqd;
    private com.see.truetransact.uicomponent.CButtonGroup rdgLastInstallmentAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgOneRateAvail;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPartialWithdrawlAllowedForDD;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayInterestOnHoliday;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPenalIntOnWithdrawal;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPenaltyOnLateInstallmentsChargeble;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrematureClosureApply;
    private com.see.truetransact.uicomponent.CButtonGroup rdgProvisionOfInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdgRecalcOfMaturityValue;
    private com.see.truetransact.uicomponent.CButtonGroup rdgRecurringDepositToFixedDeposit;
    private com.see.truetransact.uicomponent.CButtonGroup rdgRenewalOfDepositAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSameNoDepositAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgServiceCharge;
    private com.see.truetransact.uicomponent.CButtonGroup rdgStaffAccount;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSystemCalcValues;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTDSGLAcctHd;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTermDeposit;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTransferToMaturedDeposits;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTypesOfDeposit;
    private com.see.truetransact.uicomponent.CButtonGroup rdgWithPeriod;
    private com.see.truetransact.uicomponent.CButtonGroup rdgWithdrawalWithInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdgclosedwithinperiod;
    private com.see.truetransact.uicomponent.CButtonGroup rdgirregularRdApply;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdjustIntOnDeposits_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdjustIntOnDeposits_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdjustPrincipleToLoan_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdjustPrincipleToLoan_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoAmountRounded_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAmountRounded_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoAutoAdjustment_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAutoAdjustment_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoAutoRenewalAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAutoRenewalAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoBothRateNotAvail_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBothRateNotAvail_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoCalcMaturityValue_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCalcMaturityValue_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoCalcTDS_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCalcTDS_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoCanReceiveExcessInstal_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCanReceiveExcessInstal_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoClosedwithinperiod_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoClosedwithinperiod_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoDaily;
    private com.see.truetransact.uicomponent.CRadioButton rdoDepositRate;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscounted_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscounted_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoDoublingScheme_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDoublingScheme_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoExtensionBeyondOriginalDate_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoExtensionBeyondOriginalDate_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoExtensionPenal_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoExtensionPenal_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoExtnOfDepositBeforeMaturity_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoExtnOfDepositBeforeMaturity_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoFlexiFromSBCA_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoFlexiFromSBCA_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIncaseOfIrregularRDRBRate;
    private com.see.truetransact.uicomponent.CRadioButton rdoIncaseOfIrregularRDSBRate;
    private com.see.truetransact.uicomponent.CRadioButton rdoInsBeyondMaturityDt_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInsBeyondMaturityDt_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoInstallmentDay;
    private com.see.truetransact.uicomponent.CRadioButton rdoInstallmentInRecurringDepositAcct_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInstallmentInRecurringDepositAcct_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntPayableOnExcessInstal_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntPayableOnExcessInstal_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntProvisioningApplicable_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntProvisioningApplicable_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestAfterMaturity_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestAfterMaturity_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestalreadyPaid_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestalreadyPaid_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestrateappliedoverdue_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoInterestrateappliedoverdue_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntroducerReqd_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntroducerReqd_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoLastInstallmentAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLastInstallmentAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoMonthEnd;
    private com.see.truetransact.uicomponent.CRadioButton rdoMonthly;
    private com.see.truetransact.uicomponent.CRadioButton rdoNRE;
    private com.see.truetransact.uicomponent.CRadioButton rdoNRO;
    private com.see.truetransact.uicomponent.CRadioButton rdoPartialWithdrawalAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPartialWithdrawalAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPartialWithdrawlAllowedForDD_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPartialWithdrawlAllowedForDD_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPayInterestOnHoliday_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPayInterestOnHoliday_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalRounded_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenalRounded_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenaltyOnLateInstallmentsChargeble_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPenaltyOnLateInstallmentsChargeble_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoProvisionOfInterest_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoProvisionOfInterest_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoRecalcOfMaturityValue_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoRecalcOfMaturityValue_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoRecurringDepositToFixedDeposit_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoRecurringDepositToFixedDeposit_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoRegular;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalOfDepositAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoRenewalOfDepositAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSBRate;
    private com.see.truetransact.uicomponent.CRadioButton rdoSBRateOneRate_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSBRateOneRate_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSameNoRenewalAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSameNoRenewalAllowed_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoServiceCharge_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoServiceCharge_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoStaffAccount_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStaffAccount_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSystemCalcValues_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSystemCalcValues_Print_accOpen;
    private com.see.truetransact.uicomponent.CRadioButton rdoSystemCalcValues_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoTermDeposit_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoTermDeposit_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransferToMaturedDeposits_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransferToMaturedDeposits_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoWeekly;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithPeriod_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithPeriod_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithdrawalWithInterest_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoWithdrawalWithInterest_Yes;
    private com.see.truetransact.uicomponent.CComboBox sbRateCmb;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptCentre;
    private com.see.truetransact.uicomponent.CSeparator sptInterestPayment;
    private com.see.truetransact.uicomponent.CSeparator sptOperatesLike;
    private com.see.truetransact.uicomponent.CSeparator sptOperatesLike1;
    private com.see.truetransact.uicomponent.CSeparator sptOperatesLike2;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpAgentCommSlabSettings;
    private com.see.truetransact.uicomponent.CScrollPane srpDelayedInstallmet;
    private com.see.truetransact.uicomponent.CScrollPane srpInterestTable;
    private com.see.truetransact.uicomponent.CScrollPane srpThriftBenevelontDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpWeeklySlabSettings;
    private com.see.truetransact.uicomponent.CTabbedPane tabDepositsProduct;
    private com.see.truetransact.uicomponent.CTable tblAgentCommSlabSettings;
    private com.see.truetransact.uicomponent.CTable tblDelayedInstallmet;
    private com.see.truetransact.uicomponent.CTable tblInterestTable;
    private com.see.truetransact.uicomponent.CTable tblThriftBenevelontDetails;
    private com.see.truetransact.uicomponent.CTable tblWeeklySlabSettings;
    private javax.swing.JToolBar tbrDepositsProduct;
    private com.see.truetransact.uicomponent.CDateField tdtChosenDate;
    private com.see.truetransact.uicomponent.CDateField tdtDate;
    private com.see.truetransact.uicomponent.CDateField tdtEffectiveDate;
    private com.see.truetransact.uicomponent.CDateField tdtFromDepositDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastIntProvisionalDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastInterestAppliedDate;
    private com.see.truetransact.uicomponent.CDateField tdtMaturityDate;
    private com.see.truetransact.uicomponent.CDateField tdtNextIntProvisionalDate;
    private com.see.truetransact.uicomponent.CDateField tdtNextInterestAppliedDate;
    private com.see.truetransact.uicomponent.CDateField tdtPresentDate;
    private com.see.truetransact.uicomponent.CDateField tdtSchemeClosingDate;
    private com.see.truetransact.uicomponent.CDateField tdtSchemeIntroDate;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CTextField txtAcctHd;
    private com.see.truetransact.uicomponent.CTextField txtAcctHeadForFloatAcct;
    private com.see.truetransact.uicomponent.CTextField txtAcctHeadForFloatAcctDesc;
    private com.see.truetransact.uicomponent.CTextField txtAcctNumberPattern;
    private com.see.truetransact.uicomponent.CTextField txtAdvanceMaturityNoticeGenPeriod;
    private com.see.truetransact.uicomponent.CTextField txtAfterHowManyDays;
    private com.see.truetransact.uicomponent.CTextField txtAfterNoDays;
    private com.see.truetransact.uicomponent.CTextField txtAgentCommSlabAmtFrom;
    private com.see.truetransact.uicomponent.CTextField txtAgentCommSlabPercent;
    private com.see.truetransact.uicomponent.CTextField txtAgentCommision;
    private com.see.truetransact.uicomponent.CTextField txtAlphaSuffix;
    private com.see.truetransact.uicomponent.CTextField txtAmtInMultiplesOf;
    private com.see.truetransact.uicomponent.CTextField txtBenIntReserveHead;
    private com.see.truetransact.uicomponent.CTextField txtBenIntReserveHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtCategoryBenifitRate;
    private com.see.truetransact.uicomponent.CTextField txtCommisionPaybleGLHead;
    private com.see.truetransact.uicomponent.CTextField txtCommisionPaybleGLHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtCommisionRate;
    private com.see.truetransact.uicomponent.CTextField txtCutOffDayForPaymentOfInstal;
    private com.see.truetransact.uicomponent.CTextField txtDelayedChargesAmt;
    private com.see.truetransact.uicomponent.CTextField txtDepositPerUnit;
    private com.see.truetransact.uicomponent.CTextField txtDesc;
    private com.see.truetransact.uicomponent.CTextField txtDoublingCount;
    private com.see.truetransact.uicomponent.CTextField txtFixedDepositAcctHead;
    private com.see.truetransact.uicomponent.CTextField txtFixedDepositAcctHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtFromAmt;
    private com.see.truetransact.uicomponent.CTextField txtFromPeriod;
    private com.see.truetransact.uicomponent.CTextField txtGracePeriod;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentAmount;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentFrom;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentNo;
    private com.see.truetransact.uicomponent.CTextField txtInstallmentTo;
    private com.see.truetransact.uicomponent.CTextField txtIntDebitPLHead;
    private com.see.truetransact.uicomponent.CTextField txtIntDebitPLHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntOnMaturedDepositAcctHead;
    private com.see.truetransact.uicomponent.CTextField txtIntOnMaturedDepositAcctHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntPaybleGLHead;
    private com.see.truetransact.uicomponent.CTextField txtIntPaybleGLHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntPeriodForBackDatedRenewal;
    private com.see.truetransact.uicomponent.CTextField txtIntProvisionOfMaturedDeposit;
    private com.see.truetransact.uicomponent.CTextField txtIntProvisionOfMaturedDepositDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntProvisioningAcctHd;
    private com.see.truetransact.uicomponent.CTextField txtIntProvisioningAcctHdDesc;
    private com.see.truetransact.uicomponent.CTextField txtInterestNotPayingValue;
    private com.see.truetransact.uicomponent.CTextField txtInterestOnMaturedDeposits;
    private com.see.truetransact.uicomponent.CTextField txtInterestRecoveryAcHd;
    private com.see.truetransact.uicomponent.CTextField txtInterestRecoveryAcHdDesc;
    private com.see.truetransact.uicomponent.CTextField txtLastAcctNumber;
    private com.see.truetransact.uicomponent.CTextField txtLimitForBulkDeposit;
    private com.see.truetransact.uicomponent.CTextField txtMaturityDateAfterLastInstalPaid;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmtOfCashPayment;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmtOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CTextField txtMaxDepositAmt;
    private com.see.truetransact.uicomponent.CTextField txtMaxDepositPeriod;
    private com.see.truetransact.uicomponent.CTextField txtMaxNoOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CTextField txtMaxNopfTimes;
    private com.see.truetransact.uicomponent.CTextField txtMaxNopfTimes1;
    private com.see.truetransact.uicomponent.CTextField txtMaxPeriodMDt;
    private com.see.truetransact.uicomponent.CTextField txtMinAmtOfPAN;
    private com.see.truetransact.uicomponent.CTextField txtMinAmtOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CTextField txtMinDepositAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinDepositPeriod;
    private com.see.truetransact.uicomponent.CTextField txtMinIntToBePaid;
    private com.see.truetransact.uicomponent.CTextField txtMinNoOfDays;
    private com.see.truetransact.uicomponent.CTextField txtMinimumPeriod;
    private com.see.truetransact.uicomponent.CTextField txtMinimumRenewalDeposit;
    private com.see.truetransact.uicomponent.CTextField txtNoOfPartialWithdrawalAllowed;
    private com.see.truetransact.uicomponent.CTextField txtNormPeriod;
    private com.see.truetransact.uicomponent.CTextField txtPenal;
    private com.see.truetransact.uicomponent.CTextField txtPenalCharges;
    private com.see.truetransact.uicomponent.CTextField txtPenalChargesDesc;
    private com.see.truetransact.uicomponent.CTextField txtPeriodInMultiplesOf;
    private com.see.truetransact.uicomponent.CTextField txtPostageAcHd;
    private com.see.truetransact.uicomponent.CTextField txtPrematureWithdrawal;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtRDIrregularIfInstallmentDue;
    private com.see.truetransact.uicomponent.CTextField txtRenewedclosedbefore;
    private com.see.truetransact.uicomponent.CTextField txtServiceCharge;
    private com.see.truetransact.uicomponent.CTextField txtSpecialRDInstallments;
    private com.see.truetransact.uicomponent.CTextField txtSuffix;
    private com.see.truetransact.uicomponent.CTextField txtTDSGLAcctHd;
    private com.see.truetransact.uicomponent.CTextField txtTDSGLAcctHdDesc;
    private com.see.truetransact.uicomponent.CTextField txtToAmt;
    private com.see.truetransact.uicomponent.CTextField txtToPeriod;
    private com.see.truetransact.uicomponent.CTextField txtTransferOutHead;
    private com.see.truetransact.uicomponent.CTextField txtTransferOutHeadDesc;
    private com.see.truetransact.uicomponent.CTextField txtWithdrawalsInMultiplesOf;
    private com.see.truetransact.uicomponent.CTextField txttMaturityDepositAcctHead;
    private com.see.truetransact.uicomponent.CTextField txttMaturityDepositAcctHeadDesc;
    // End of variables declaration//GEN-END:variables
}

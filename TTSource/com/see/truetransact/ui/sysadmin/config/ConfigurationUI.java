/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ConfigurationUI.java
 *
 * Created on February 10, 2005, 5:20 PM
 */
package com.see.truetransact.ui.sysadmin.config;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;

import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.CurrencyValidation;

import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import java.util.Date;

/**
 *
 * @author 152715
 */
public class ConfigurationUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    //    final private ConfigurationRB resourceBundle = new ConfigurationRB();
    final ConfigurationMRB objMandatoryRB = new ConfigurationMRB();

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.config.ConfigurationRB", ProxyParameters.LANGUAGE);

    private ConfigurationOB observable;
    private HashMap mandatoryMap;
    private int viewType = 0;
    private final int AUTHORIZE = 999;
    private final String CAH = "Cash Account Head";
    private final String IAH = "IBR Account Head";
    private final String SIC = "SIC Account Head";
    private final String RC = "RC Account Head";
    private final String AC = "AC Account Head";
    private final String EC = "EC Account Head";
    private final String FC = "FC Account Head";
    private final String ASH = "ASH Account Head";
    private final String NMF = "NMF Account Head";
    private final String ST = "ST Account Head";
    private final String SS = "SS Account Head";
    private final String RTGS = "RTGS Account Head";
    private String btnType = "";
    private Date currDt = null;

    /**
     * Creates new form ConfigurationUI
     */
    public ConfigurationUI() {
        initComponents();
        initStartUp();
    }

    private void initStartUp() {
        setFieldNames();
        internationalize();
        setObservable();
        cboHeadOffice.setModel(observable.getCbmBranches());
        setMandatoryHashMap();
        setHelpMessage();
        setMaximumLength();
        observable.populateData();
        enableDisablePanPassword(true);
        setButtonEnableDisable(true);

        //__ If the Record is Authrized, Disable the Auth Buttons...
        setAuthEnable(observable.getIsAuth());
        setHelpBtnEnableDisable(true);
        setEnab();
        
    }

    //__ To Enable/Disable the Authorize buttons...
    private void setAuthEnable(boolean value) {
        btnAuthorize.setEnabled(!value);
        btnReject.setEnabled(!value);
        btnException.setEnabled(!value);
    }
    
    private void setEnab(){
        txtSIChargesHead.setEnabled(false);
        txtRemitChargesHead.setEnabled(false);
        txtRTGS_GL.setEnabled(false);
        txtSalarySuspense.setEnabled(false);
        txtAppSuspenseAcHd.setEnabled(false);
        txtIBRActHead.setEnabled(false);
        txtNmfAcHead.setEnabled(false);
        txtFailChargesHead.setEnabled(false);
        txtServiceTaxHead.setEnabled(false);
        txtAcceptChargesHead.setEnabled(false);
        txtExecChargesHead.setEnabled(false);
        txtCashActHead.setEnabled(false);
    }

    private void setMaximumLength() {
        txtDays.setMaxLength(4);
        txtDays.setValidation(new NumericValidation());
        txtMinLength.setMaxLength(2);
        txtMinLength.setValidation(new NumericValidation());
        txtMaxLength.setMaxLength(2);
        txtMaxLength.setValidation(new NumericValidation());
        txtSplChar.setMaxLength(1);
        txtSplChar.setValidation(new NumericValidation());
        txtUpperCase.setMaxLength(1);
        txtUpperCase.setValidation(new NumericValidation());
        txtNo.setMaxLength(1);
        txtNo.setValidation(new NumericValidation());
        txtPwds.setMaxLength(1);
        txtPwds.setValidation(new NumericValidation());
        txtAttempts.setMaxLength(2);
        txtAttempts.setValidation(new NumericValidation());
        txtMinorAge.setMaxLength(2);
        txtMinorAge.setValidation(new NumericValidation());
        txtRetirementAge.setMaxLength(2);
        txtRetirementAge.setValidation(new NumericValidation());
        txtCashActHead.setMaxLength(16);
        txtIBRActHead.setMaxLength(16);
        txtSIChargesHead.setMaxLength(16);
        txtRemitChargesHead.setMaxLength(16);
        txtAcceptChargesHead.setMaxLength(16);
        txtExecChargesHead.setMaxLength(16);
        txtFailChargesHead.setMaxLength(16);
        txtSalarySuspense.setMaxLength(16);
        txtServiceTax.setMaxLength(5);
        txtServiceTax.setValidation(new PercentageValidation());
        txtPanDetails.setValidation(new CurrencyValidation(14, 2));
        txtLogOutTime.setMaxLength(2);
        txtLogOutTime.setValidation(new NumericValidation());

        txtMemFee.setValidation(new CurrencyValidation(14, 2));
        txtNmfAcHead.setMaxLength(16);

        txtPendingTxnAllowedDays.setMaxLength(3);
        txtPendingTxnAllowedDays.setValidation(new NumericValidation());
        txtSeniorCitizenAge.setMaxLength(2);
        txtSeniorCitizenAge.setValidation(new NumericValidation());

        txtGahanPeriod.setMaxLength(16);
        txtGahanPeriod.setValidation(new NumericValidation());
        txtServicePeriod.setMaxLength(2);
        txtServicePeriod.setValidation(new NumericValidation());

        txtAmcAlertTime.setMaxLength(3);
        txtAmcAlertTime.setValidation(new NumericValidation());
    }

    private void setObservable() {
        /* Implementing Singleton pattern */
        observable = ConfigurationOB.getInstance();
        observable.addObserver(this);
    }
    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */

    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnPrint.setName("btnPrint");
        btnEdit.setName("btnEdit");
        btnSave.setName("btnSave");
        chkAccLocked.setName("chkAccLocked");
        sptPwdConfig.setName("sptPwdConfig");
        chkCantChangePwd.setName("chkCantChangePwd");
        chkFirstLogin.setName("chkFirstLogin");
        chkPwdNeverExpire.setName("chkPwdNeverExpire");
        lblAfter.setName("lblAfter");
        lblAtleast.setName("lblAtleast");
        lblAtleastUp.setName("lblAtleastUp");
        lblAttempts.setName("lblAttempts");
        lblDays.setName("lblDays");
        lblFirstLogin.setName("lblFirstLogin");
        lblLastPwd.setName("lblLastPwd");
        lblLowAtleast.setName("lblLowAtleast");
        lblMaxLength.setName("lblMaxLength");
        lblMinLength.setName("lblMinLength");
        lblMsg.setName("lblMsg");
        lblNo.setName("lblNo");
        lblPasswordExpire.setName("lblPasswordExpire");
        lblPwdExpiry.setName("lblPwdExpiry");
        lblPwds.setName("lblPwds");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSplChar1.setName("lblSplChar1");
        lblStatus.setName("lblStatus");
        lblUpCase.setName("lblUpCase");
        lblUserAccLocked.setName("lblUserAccLocked");
        lblUserCantChangePwd.setName("lblUserCantChangePwd");
        mbrConfig.setName("mbrConfig");
        panAttempts.setName("panAttempts");
        panDays.setName("panDays");
        panNo.setName("panNo");
        panPassword.setName("panPassword");
        panPwds.setName("panPwds");
        panSplChar.setName("panSplChar");
        panStatus.setName("panStatus");
        panUpperCase.setName("panUpperCase");
        tabConfig.setName("tabConfig");
        txtAttempts.setName("txtAttempts");
        txtDays.setName("txtDays");
        txtMaxLength.setName("txtMaxLength");
        txtMinLength.setName("txtMinLength");
        txtNo.setName("txtNo");
        txtPwds.setName("txtPwds");
        txtSplChar.setName("txtSplChar");
        txtUpperCase.setName("txtUpperCase");
        lblMinorAge.setName("lblMinorAge");
        lblRetireAge.setName("lblRetireAge");
        lblCashActHead.setName("lblCashActHead");
        lblIBRActHead.setName("lblIBRActHead");
        lblSIChargesHead.setName("lblSIChargesHead");
        lblRemitChargesHead.setName("lblRemitChargesHead");
        lblAcceptChargesHead.setName("lblAcceptChargesHead");
        lblExecChargesHead.setName("lblExecChargesHead");
        lblFailChargesHead.setName("lblFailChargesHead");
        lblRTGSGL.setName("lblRTGSGL");
        lblServiceTax.setName("lblServiceTax");
        txtMinorAge.setName("txtMinorAge");
        txtRetirementAge.setName("txtRetirementAge");
        txtCashActHead.setName("txtCashActHead");
        txtIBRActHead.setName("txtIBRActHead");
        btnCashActHead.setName("btnCashActHead");
        btnIBRActHead.setName("btnIBRActHead");
        btnSIChargesHead.setName("btnSIChargesHead");
        btnRemitChargesHead.setName("btnRemitChargesHead");
        btnAcceptChargesHead.setName("btnAcceptChargesHead");
        btnExecChargesHead.setName("btnExecChargesHead");
        btnlFailChargesHead.setName("btnlFailChargesHead");
        panAgeRules.setName("panAgeRules");
        panMiscRules.setName("panMiscRules");
        panDenomination.setName("panDenomination");
        txtAppSuspenseAcHd.setName("txtAppSuspenseAcHd");
        cboHeadOffice.setName("cboHeadOffice");
        txtSIChargesHead.setName("txtSIChargesHead");
        txtRemitChargesHead.setName("txtRemitChargesHead");
        txtAcceptChargesHead.setName("txtAcceptChargesHead");
        txtExecChargesHead.setName("txtExecChargesHead");
        txtFailChargesHead.setName("txtFailChargesHead");
        txtSalarySuspense.setName("txtSalarySuspense");
        txtServiceTax.setName("txtServiceTax");
        lblPanDetails.setName("lblPanDetails");
        txtLogOutTime.setName("txtLogOutTime");
        lblLogOutTime.setName("lblLogOutTime");
        lblSeniorCitizenAge.setName("lblSeniorCitizenAge");
        lblLastFinancialYearEnd.setName("lblLastFinancialYearEnd");
        lblYearEndProcessDate.setName("lblYearEndProcessDate");
        lblSuspenseAcHead.setName("lblSuspenseAcHead");
        lblSalarySuspense.setName("lblSalarySuspense");
        lblRTGSGL.setName("lblRTGSGL");
        lblFromDate.setName("lblFromDate");
        lblToDate.setName("lblToDate");
        lblAmcAlertTime.setName("lblAmcAlertTime");
        lblGahanPeriod.setName("lblGahanPeriod");
        lblAllowCashierAuthorization.setName("lblAllowCashierAuthorization");
        lblAllowDenomination.setName("lblAllowDenomination");
        lblAllowMultiShare.setName("lblAllowMultiShare");
        lblAllowServiceTax.setName("lblAllowServiceTax");
        lblAllowTokenNo.setName("lblAllowTokenNo");
        lblPenalIntFromReports.setName("lblPenalIntFromReports");
        lblEffectiveFrom.setName("lblEffectiveFrom");
        lblPendingTxnAllowedDays.setName("lblPendingTxnAllowedDays");
        panAMC_Details.setName("panServicePeriod");
        lblEffectiveFrom.setName("lblEffectiveFrom");
        lblGahanPeriod.setName("lblGahanPeriod");
        lblServicePeriod.setName("lblServicePeriod");

        txtSeniorCitizenAge.setName("txtSeniorCitizenAge");
        tdtLastFinancialYearEnd.setName("tdtLastFinancialYearEnd");
        tdtYearEndProcessDate.setName("tdtYearEndProcessDate");

        btnServiceTaxHead.setName("btnServiceTaxHead");
        btnSuspenseAcHd.setName("btnSuspenseAcHd");
        btnSalarySuspense.setName("btnSalarySuspense");
        btnRTGS_GL.setName("btnRTGS_GL");
        txtRTGS_GL.setName("txtRTGS_GL");
        tdtFromDate.setName("tdtFromDate");
        tdtToDate.setName("tdtToDate");
        txtAmcAlertTime.setName("txtAmcAlertTime");
        txtGahanPeriod.setName("txtGahanPeriod");
        panAuthorizationByStaff.setName("panAuthorizationByStaff");
        panDenomination.setName("panDenomination");
        panMultiShare.setName("panMultiShare");
        panServiceTax.setName("panServiceTax");
        panTokenNoAllow.setName("panTokenNoAllow");
        panCashierAuthorization.setName("panCashierAuthorization");
        panExcludePenalIntFromReports.setName("panExcludePenalIntFromReports");
        txtPendingTxnAllowedDays.setName("txtPendingTxnAllowedDays");
        txtPanDetails.setName("txtPanDetails");

        tdtEffectiveFrom.setName("tdtEffectiveFrom");
        txtServicePeriod.setName("txtServicePeriod");
        txtGahanPeriod.setName("txtGahanPeriod");
    }
    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */

    private void internationalize() {
        lblLastPwd.setText(resourceBundle.getString("lblLastPwd"));

        lblLastPwd.setText(resourceBundle.getString("lblLastPwd"));

        lblLogOutTime.setText(resourceBundle.getString("lblLogOutTime"));
        lblSeniorCitizenAge.setText(resourceBundle.getString("lblSeniorCitizenAge"));
        lblLastFinancialYearEnd.setText(resourceBundle.getString("lblLastFinancialYearEnd"));
        lblYearEndProcessDate.setText(resourceBundle.getString("lblYearEndProcessDate"));
        lblSuspenseAcHead.setText(resourceBundle.getString("lblSuspenseAcHead"));
        lblSalarySuspense.setText(resourceBundle.getString("lblSalarySuspense"));
        lblRTGSGL.setText(resourceBundle.getString("lblRTGSGL"));
        lblFromDate.setText(resourceBundle.getString("lblFromDate"));
        lblToDate.setText(resourceBundle.getString("lblToDate"));
        lblAmcAlertTime.setText(resourceBundle.getString("lblAmcAlertTime"));

        lblGahanPeriod.setText(resourceBundle.getString("lblGahanPeriod"));
        lblAllowCashierAuthorization.setText(resourceBundle.getString("lblAllowCashierAuthorization"));
        lblAllowDenomination.setText(resourceBundle.getString("lblAllowDenomination"));
        lblAllowMultiShare.setText(resourceBundle.getString("lblAllowMultiShare"));
        lblAllowServiceTax.setText(resourceBundle.getString("lblAllowServiceTax"));
        lblAllowTokenNo.setText(resourceBundle.getString("lblAllowTokenNo"));
        lblPenalIntFromReports.setText(resourceBundle.getString("lblPenalIntFromReports"));
        lblEffectiveFrom.setText(resourceBundle.getString("lblEffectiveFrom"));
        lblPendingTxnAllowedDays.setText(resourceBundle.getString("lblPendingTxnAllowedDays"));
        lblPanDetails.setText(resourceBundle.getString("lblPanDetails"));
        lblServicePeriod.setText(resourceBundle.getString("lblServicePeriod"));

        btnClose.setText(resourceBundle.getString("btnClose"));
        lblPasswordExpire.setText(resourceBundle.getString("lblPasswordExpire"));
        chkCantChangePwd.setText(resourceBundle.getString("chkCantChangePwd"));
        lblAtleast.setText(resourceBundle.getString("lblAtleast"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblMinLength.setText(resourceBundle.getString("lblMinLength"));
        lblPwds.setText(resourceBundle.getString("lblPwds"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblFirstLogin.setText(resourceBundle.getString("lblFirstLogin"));
        chkAccLocked.setText(resourceBundle.getString("chkAccLocked"));
        lblMaxLength.setText(resourceBundle.getString("lblMaxLength"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblAttempts.setText(resourceBundle.getString("lblAttempts"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblUserCantChangePwd.setText(resourceBundle.getString("lblUserCantChangePwd"));
        lblPwdExpiry.setText(resourceBundle.getString("lblPwdExpiry"));
        lblUpCase.setText(resourceBundle.getString("lblUpCase"));
        lblSplChar1.setText(resourceBundle.getString("lblSplChar1"));
        lblNo.setText(resourceBundle.getString("lblNo"));
        lblDays.setText(resourceBundle.getString("lblDays"));
        lblLowAtleast.setText(resourceBundle.getString("lblLowAtleast"));
        chkPwdNeverExpire.setText(resourceBundle.getString("chkPwdNeverExpire"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblAtleastUp.setText(resourceBundle.getString("lblAtleastUp"));
        chkFirstLogin.setText(resourceBundle.getString("chkFirstLogin"));
        lblAfter.setText(resourceBundle.getString("lblAfter"));
        lblUserAccLocked.setText(resourceBundle.getString("lblUserAccLocked"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblMinorAge.setText(resourceBundle.getString("lblMinorAge"));
        lblRetireAge.setText(resourceBundle.getString("lblRetireAge"));
        lblCashActHead.setText(resourceBundle.getString("lblCashActHead"));
        lblIBRActHead.setText(resourceBundle.getString("lblIBRActHead"));
        lblSIChargesHead.setText(resourceBundle.getString("lblSIChargesHead"));
        lblAcceptChargesHead.setText(resourceBundle.getString("lblAcceptChargesHead"));
        lblExecChargesHead.setText(resourceBundle.getString("lblExecChargesHead"));
        lblFailChargesHead.setText(resourceBundle.getString("lblFailChargesHead"));
        lblServiceTaxHead.setText(resourceBundle.getString("lblServiceTaxHead"));
        lblServiceTax.setText(resourceBundle.getString("lblServiceTax"));
        lblRemitChargesHead.setText(resourceBundle.getString("lblRemitChargesHead"));
        btnCashActHead.setText(resourceBundle.getString("btnCashActHead"));
        btnIBRActHead.setText(resourceBundle.getString("btnIBRActHead"));
        btnServiceTaxHead.setText(resourceBundle.getString("btnServiceTaxHead"));
        btnSalarySuspense.setText(resourceBundle.getString("btnSalarySuspense"));
        btnRTGS_GL.setText(resourceBundle.getString("btnRTGS_GL"));
        btnSIChargesHead.setText(resourceBundle.getString("btnSIChargesHead"));
        btnRemitChargesHead.setText(resourceBundle.getString("btnRemitChargesHead"));
        btnAcceptChargesHead.setText(resourceBundle.getString("btnAcceptChargesHead"));
        btnExecChargesHead.setText(resourceBundle.getString("btnExecChargesHead"));
        btnlFailChargesHead.setText(resourceBundle.getString("btnlFailChargesHead"));
        lblDayEndType.setText(resourceBundle.getString("lblDayEndType"));
        rdoDayEndType_BranchLevel.setText(resourceBundle.getString("rdoDayEndType_BranchLevel"));
        rdoDayEndType_BankLevel.setText(resourceBundle.getString("rdoDayEndType_BankLevel"));
        rdoAllowAuthorizationNo.setText(resourceBundle.getString("rdoAllowAuthorizationNo"));
        rdoAllowAuthorizationYes.setText(resourceBundle.getString("rdoAllowAuthorizationYes"));
        rdoTokenNoAllowNo.setText(resourceBundle.getString("rdoTokenNoAllowNo"));
        rdoTokenNoAllowYes.setText(resourceBundle.getString("rdoTokenNoAllowYes"));
        rdoDenominationYes.setText(resourceBundle.getString("rdoDenominationYes"));
        rdoDenominationNo.setText(resourceBundle.getString("rdoDenominationNo"));
        rdoServiceTaxYes.setText(resourceBundle.getString("rdoServiceTaxYes"));
        rdoServiceTaxNo.setText(resourceBundle.getString("rdoServiceTaxNo"));
        rdoMultiShareYes.setText(resourceBundle.getString("rdoMultiShareYes"));
        rdoMultiShareNo.setText(resourceBundle.getString("rdoMultiShareNo"));

        rdoExcludePenalIntFromReportsNo.setText(resourceBundle.getString("rdoExcludePenalIntFromReportsNo"));
        rdoExcludePenalIntFromReportsYes.setText(resourceBundle.getString("rdoExcludePenalIntFromReportsYes"));
        rdoCashierAuthorizationYes.setText(resourceBundle.getString("rdoCashierAuthorizationYes"));
        rdoCashierAuthorizationNo.setText(resourceBundle.getString("rdoCashierAuthorizationNo"));

        lblHolidayIB_Trans1.setText(resourceBundle.getString("lblHolidayIB_Trans1"));
        lblHolidayIB_Trans2.setText(resourceBundle.getString("lblHolidayIB_Trans2"));
        rdoIB_OnHoliday_Yes.setText(resourceBundle.getString("rdoIB_OnHoliday_Yes"));
        rdoIB_OnHoliday_No.setText(resourceBundle.getString("rdoIB_OnHoliday_No"));
        lblPanDetails.setText(resourceBundle.getString("lblPanDetails"));
        lblLogOutTime.setText(resourceBundle.getString("lblLogOutTime"));
    }

    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        //---Account---
        rdoDayEndType.remove(rdoDayEndType_BankLevel);
        rdoDayEndType.remove(rdoDayEndType_BranchLevel);
        rdoIB_OnHoliday.remove(rdoIB_OnHoliday_Yes);
        rdoIB_OnHoliday.remove(rdoIB_OnHoliday_No);
        rdoAllowAuth.remove(rdoAllowAuthorizationYes);
        rdoAllowAuth.remove(rdoAllowAuthorizationNo);

        rdoDenomination.remove(rdoDenominationYes);
        rdoDenomination.remove(rdoDenominationNo);

        rdoMultiShare.remove(rdoMultiShareNo);
        rdoMultiShare.remove(rdoMultiShareYes);

        rdoServiceTx.remove(rdoServiceTaxNo);
        rdoServiceTx.remove(rdoServiceTaxYes);

        rdoServiceTx.remove(rdoExcludePenalIntFromReportsNo);
        rdoServiceTx.remove(rdoExcludePenalIntFromReportsYes);

        rdoExcludePenal.remove(rdoCashierAuthorizationNo);
        rdoExcludePenal.remove(rdoCashierAuthorizationYes);

        rdoToken.remove(rdoTokenNoAllowYes);
        rdoToken.remove(rdoTokenNoAllowNo);
    }

    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        //---Account---
        rdoDayEndType = new CButtonGroup();
        rdoDayEndType.add(rdoDayEndType_BankLevel);
        rdoDayEndType.add(rdoDayEndType_BranchLevel);
        rdoIB_OnHoliday = new CButtonGroup();
        rdoIB_OnHoliday.add(rdoIB_OnHoliday_Yes);
        rdoIB_OnHoliday.add(rdoIB_OnHoliday_No);
        rdoAllowAuth = new CButtonGroup();
        rdoAllowAuth.add(rdoAllowAuthorizationNo);
        rdoAllowAuth.add(rdoAllowAuthorizationYes);

        rdoDenomination = new CButtonGroup();
        rdoDenomination.add(rdoDenominationYes);
        rdoDenomination.add(rdoDenominationNo);

        rdoMultiShare = new CButtonGroup();

        rdoMultiShare.add(rdoMultiShareNo);
        rdoMultiShare.add(rdoMultiShareYes);

        rdoServiceTx = new CButtonGroup();
        rdoServiceTx.add(rdoServiceTaxNo);
        rdoServiceTx.add(rdoServiceTaxYes);

        rdoServiceTx = new CButtonGroup();
        rdoServiceTx.add(rdoExcludePenalIntFromReportsNo);
        rdoServiceTx.add(rdoExcludePenalIntFromReportsYes);

        rdoExcludePenal = new CButtonGroup();
        rdoExcludePenal.add(rdoCashierAuthorizationNo);
        rdoExcludePenal.add(rdoCashierAuthorizationYes);

        rdoToken = new CButtonGroup();

        rdoToken.add(rdoTokenNoAllowYes);
        rdoToken.add(rdoTokenNoAllowNo);

    }

    /* Auto Generated Method - update()
     This method called by Observable. It updates the UI with
     Observable's data. If needed add/Remove RadioButtons
     method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        rdoDayEndType_BankLevel.setSelected(observable.isRdoDayEndType_Bank());
        rdoDayEndType_BranchLevel.setSelected(observable.isRdoDayEndType_Branch());
        rdoIB_OnHoliday_Yes.setSelected(observable.isRdoIB_OnHoliday_Yes());
        rdoIB_OnHoliday_No.setSelected(observable.isRdoIB_OnHoliday_No());
        chkPwdNeverExpire.setSelected(observable.getChkPwdNeverExpire());
        txtMinLength.setText(observable.getTxtMinLength());
        txtMaxLength.setText(observable.getTxtMaxLength());
        chkCantChangePwd.setSelected(observable.getChkCantChangePwd());
        txtAttempts.setText(observable.getTxtAttempts());
        txtUpperCase.setText(observable.getTxtUpperCase());
        txtSplChar.setText(observable.getTxtSplChar());
        txtNo.setText(observable.getTxtNo());
        txtDays.setText(observable.getTxtDays());
        chkAccLocked.setSelected(observable.getChkAccLocked());
        chkFirstLogin.setSelected(observable.getChkFirstLogin());
        txtPwds.setText(observable.getTxtPwds());
        txtMinorAge.setText(observable.getTxtMinorAge());
        txtRetirementAge.setText(observable.getTxtRetireAge());
        txtCashActHead.setText(observable.getTxtCashActHead());
        txtIBRActHead.setText(observable.getTxtIBRActHead());
        txtSIChargesHead.setText(observable.getTxtSIChargesHead());
        txtRemitChargesHead.setText(observable.getTxtRemitChargesHead());
        txtAcceptChargesHead.setText(observable.getTxtAcceptChargesHead());
        txtExecChargesHead.setText(observable.getTxtExecChargesHead());
        txtFailChargesHead.setText(observable.getTxtFailChargesHead());
        txtServiceTaxHead.setText(observable.getTxtServiceTaxHead());
        txtServiceTax.setText(observable.getTxtServiceTax());
        txtSalarySuspense.setText(observable.getTxtSalarySuspense());
        txtAppSuspenseAcHd.setText(observable.getTxtAppSuspenseAcHd());
        lblStatus.setText(observable.getLblStatus());
        cboHeadOffice.setModel(observable.getCbmBranches());
        txtPanDetails.setText(observable.getTxtPanDetails());
        txtLogOutTime.setText(observable.getLogOutTime());
        txtMemFee.setText(observable.getTxtNominalMemFee());
        if (txtMemFee.getText().length() > 0) {
            btnNmfAcHead.setEnabled(true);
        }
        txtNmfAcHead.setText(observable.getTxtNmfAcHead());
        rdoAllowAuthorizationNo.setSelected(observable.isRdoAllowAuthorizationNo());
        rdoAllowAuthorizationYes.setSelected(observable.isRdoAllowAuthorizationYes());

        txtPendingTxnAllowedDays.setText(observable.getTxtPendingTxnAllowedDays());
        txtGahanPeriod.setText(observable.getTxtGahanPeriod());
        txtServicePeriod.setText(observable.getTxtServicePeriod());

        txtRTGS_GL.setText(observable.getTxtRTGS_GL());
        txtAmcAlertTime.setText(observable.getTxtAmcAlertTime());
        txtSeniorCitizenAge.setText(observable.getTxtSeniorCitizenAge());
        rdoDenominationYes.setSelected(observable.isRdoDenominationYes());
        rdoDenominationNo.setSelected(observable.isRdoDenominationNo());
        rdoMultiShareYes.setSelected(observable.isRdoMultiShareYes());
        rdoMultiShareNo.setSelected(observable.isRdoMultiShareNo());

        rdoServiceTaxYes.setSelected(observable.isRdoServiceTaxYes());
        rdoServiceTaxNo.setSelected(observable.isRdoServiceTaxNo());
        rdoExcludePenalIntFromReportsYes.setSelected(observable.isRdoExcludePenalIntFromReportsYes());
        rdoExcludePenalIntFromReportsNo.setSelected(observable.isRdoExcludePenalIntFromReportsNo());

        rdoCashierAuthorizationYes.setSelected(observable.isRdoCashierAuthorizationYes());
        rdoCashierAuthorizationNo.setSelected(observable.isRdoCashierAuthorizationNo());
        rdoTokenNoAllowYes.setSelected(observable.isRdoTokenNoAllowYes());
        rdoTokenNoAllowNo.setSelected(observable.isRdoTokenNoAllowNo());

        if (!observable.getTdtFromDate().equals("null")) {
            tdtFromDate.setDateValue(observable.getTdtFromDate());
        }
        if (!observable.getTdtToDate().equals("null")) {

            tdtToDate.setDateValue(observable.getTdtToDate());

        }
        if (!observable.getTdtYearEndProcessDate().equals("null")) {

            tdtYearEndProcessDate.setDateValue(observable.getTdtYearEndProcessDate());

        }
        if (!observable.getTdtLastFinancialYearEnd().equals("null")) {

            tdtLastFinancialYearEnd.setDateValue(observable.getTdtLastFinancialYearEnd());

        }

        if (!observable.getTdtEffectiveDate().equals("null")) {
            tdtEffectiveFrom.setDateValue(observable.getTdtEffectiveDate());
        }

//        cboHeadOffice.setSelectedItem(observable.getCboBranches());
        addRadioButtons();
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setChkPwdNeverExpire(chkPwdNeverExpire.isSelected());
        observable.setTxtMinLength(txtMinLength.getText());
        observable.setTxtMaxLength(txtMaxLength.getText());
        observable.setChkCantChangePwd(chkCantChangePwd.isSelected());
        observable.setTxtAttempts(txtAttempts.getText());
        observable.setTxtUpperCase(txtUpperCase.getText());
        observable.setTxtSplChar(txtSplChar.getText());
        observable.setTxtNo(txtNo.getText());
        observable.setTxtDays(txtDays.getText());
        observable.setChkAccLocked(chkAccLocked.isSelected());
        observable.setChkFirstLogin(chkFirstLogin.isSelected());
        observable.setTxtPwds(txtPwds.getText());
        observable.setTxtMinorAge(txtMinorAge.getText());
        observable.setTxtRetireAge(txtRetirementAge.getText());
        observable.setTxtCashActHead(txtCashActHead.getText());
        observable.setTxtIBRActHead(txtIBRActHead.getText());
        observable.setTxtSIChargesHead(txtSIChargesHead.getText());
        observable.setTxtAcceptChargesHead(txtAcceptChargesHead.getText());
        observable.setTxtExecChargesHead(txtExecChargesHead.getText());
        observable.setTxtRemitChargesHead(txtRemitChargesHead.getText());
        observable.setTxtFailChargesHead(txtFailChargesHead.getText());
        observable.setTxtServiceTaxHead(txtServiceTaxHead.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setCboBranches(CommonUtil.convertObjToStr(cboHeadOffice.getSelectedItem()));
        observable.setRdoDayEndType_Bank(rdoDayEndType_BankLevel.isSelected());
        observable.setRdoDayEndType_Branch(rdoDayEndType_BranchLevel.isSelected());
        observable.setRdoIB_OnHoliday_Yes(rdoIB_OnHoliday_Yes.isSelected());
        observable.setRdoIB_OnHoliday_No(rdoIB_OnHoliday_No.isSelected());
        observable.setTxtPanDetails(txtPanDetails.getText());
        observable.setLogOutTime(txtLogOutTime.getText());
        observable.setTxtNominalMemFee(txtMemFee.getText());
        observable.setTxtNmfAcHead(txtNmfAcHead.getText());

        observable.setTdtEffectiveDate(tdtEffectiveFrom.getDateValue());

        observable.setTxtAmcAlertTime(txtAmcAlertTime.getText());
        observable.setTxtAppSuspenseAcHd(txtAppSuspenseAcHd.getText());
        observable.setTxtGahanPeriod(txtGahanPeriod.getText());
        observable.setTxtPendingTxnAllowedDays(txtPendingTxnAllowedDays.getText());
        observable.setTxtServicePeriod(txtServicePeriod.getText());
        observable.setTxtSeniorCitizenAge(txtSeniorCitizenAge.getText());
        observable.setTxtSalarySuspense(txtSalarySuspense.getText());
        observable.setTxtRTGS_GL(txtRTGS_GL.getText());
        observable.setTdtEffectiveFrom(tdtEffectiveFrom.getDateValue());
        observable.setTdtFromDate(tdtFromDate.getDateValue());
        observable.setTdtToDate(tdtToDate.getDateValue());
        observable.setTdtYearEndProcessDate(tdtYearEndProcessDate.getDateValue());
        observable.setTdtLastFinancialYearEnd(tdtLastFinancialYearEnd.getDateValue());
        observable.setRdoAllowAuthorizationNo(rdoAllowAuthorizationNo.isSelected());
        observable.setRdoAllowAuthorizationYes(rdoAllowAuthorizationYes.isSelected());
        observable.setRdoTokenNoAllowNo(rdoTokenNoAllowYes.isSelected());
        observable.setRdoTokenNoAllowYes(rdoTokenNoAllowYes.isSelected());
        observable.setRdoCashierAuthorizationNo(rdoCashierAuthorizationNo.isSelected());
        observable.setRdoCashierAuthorizationYes(rdoCashierAuthorizationYes.isSelected());
        observable.setRdoDenominationNo(rdoDenominationNo.isSelected());
        observable.setRdoDenominationYes(rdoDenominationYes.isSelected());
        observable.setRdoExcludePenalIntFromReportsNo(rdoExcludePenalIntFromReportsYes.isSelected());
        observable.setRdoExcludePenalIntFromReportsYes(rdoExcludePenalIntFromReportsYes.isSelected());
        observable.setRdoMultiShareNo(rdoMultiShareNo.isSelected());
        observable.setRdoMultiShareYes(rdoMultiShareYes.isSelected());
        observable.setRdoServiceTaxNo(rdoServiceTaxNo.isSelected());
        observable.setRdoServiceTaxYes(rdoServiceTaxYes.isSelected());

    }

    /* Auto Generated Method - setMandatoryHashMap()
 
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("chkPwdNeverExpire", new Boolean(true));
        mandatoryMap.put("txtMinLength", new Boolean(true));
        mandatoryMap.put("txtMaxLength", new Boolean(true));
        mandatoryMap.put("chkCantChangePwd", new Boolean(true));
        mandatoryMap.put("txtAttempts", new Boolean(true));
        mandatoryMap.put("txtUpperCase", new Boolean(true));
        mandatoryMap.put("txtSplChar", new Boolean(true));
        mandatoryMap.put("txtNo", new Boolean(true));
        mandatoryMap.put("txtDays", new Boolean(true));
        mandatoryMap.put("chkAccLocked", new Boolean(true));
        mandatoryMap.put("chkFirstLogin", new Boolean(true));
        mandatoryMap.put("txtPwds", new Boolean(true));
        mandatoryMap.put("txtMinorAge", new Boolean(true));
        mandatoryMap.put("txtRetireAge", new Boolean(true));
        mandatoryMap.put("txtCashActHead", new Boolean(true));
        mandatoryMap.put("txtIBRActHead", new Boolean(true));
        mandatoryMap.put("txtSIChargesHead", new Boolean(true));
        mandatoryMap.put("txtRemitChargesHead", new Boolean(true));
        mandatoryMap.put("txtAcceptChargesHead", new Boolean(true));
        mandatoryMap.put("txtExecChargesHead", new Boolean(true));
        mandatoryMap.put("txtFailChargesHead", new Boolean(true));
        mandatoryMap.put("txtPanDetails", new Boolean(true));
        mandatoryMap.put("txtLogOutTime", new Boolean(true));

        mandatoryMap.put("txtPendingDays", new Boolean(true));
        mandatoryMap.put("txtGahanPeriod", new Boolean(true));
        mandatoryMap.put("txtlCashLimitForPAN", new Boolean(true));

        mandatoryMap.put("txtPendingTxnAllowedDays", new Boolean(true));
        mandatoryMap.put("tdtEffectiveFrom", new Boolean(true));
        mandatoryMap.put("txtSeniorCitizenAge", new Boolean(true));

        mandatoryMap.put("tdtLastFinancialYearEnd", new Boolean(true));

        mandatoryMap.put("tdtYearEndProcessDate", new Boolean(true));
        mandatoryMap.put("tdtFromDate", new Boolean(true));
        mandatoryMap.put("tdtToDate", new Boolean(true));
        mandatoryMap.put("txtAmcAlertTime", new Boolean(true));
        mandatoryMap.put("txtAppSuspenseAcHd", new Boolean(true));
        mandatoryMap.put("txtSalarySuspense", new Boolean(true));
        mandatoryMap.put("txtRTGS_GL", new Boolean(true));

    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        chkPwdNeverExpire.setHelpMessage(lblMsg, objMandatoryRB.getString("chkPwdNeverExpire"));
        txtMinLength.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinLength"));
        txtMaxLength.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxLength"));
        chkCantChangePwd.setHelpMessage(lblMsg, objMandatoryRB.getString("chkCantChangePwd"));
        txtAttempts.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAttempts"));
        txtUpperCase.setHelpMessage(lblMsg, objMandatoryRB.getString("txtUpperCase"));
        txtSplChar.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSplChar"));
        txtNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNo"));
        txtDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDays"));
        chkAccLocked.setHelpMessage(lblMsg, objMandatoryRB.getString("chkAccLocked"));
        chkFirstLogin.setHelpMessage(lblMsg, objMandatoryRB.getString("chkFirstLogin"));
        txtPwds.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPwds"));
        txtMinorAge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinorAge"));
        txtSeniorCitizenAge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSeniorCitizenAge"));
        txtCashActHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashActHead"));
        txtIBRActHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIBRActHead"));

        txtRetirementAge.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRetirementAge"));
        txtAppSuspenseAcHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAppSuspenseAcHd"));
        txtSalarySuspense.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSalarySuspense"));
        txtRTGS_GL.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRTGS_GL"));
        tdtFromDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFromDate"));
        tdtToDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtToDate"));
        txtAmcAlertTime.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmcAlertTime"));
        txtPendingTxnAllowedDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPendingTxnAllowedDays"));
        txtGahanPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGahanPeriod"));
        txtPanDetails.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPanDetails"));

    }

    /**
     * To enable or disable the helpbuttons of ActHeads fileds according to the
     * need *
     */
    private void setHelpBtnEnableDisable(boolean flag) {
        btnCashActHead.setEnabled(flag);
        btnIBRActHead.setEnabled(flag);
        btnSIChargesHead.setEnabled(flag);
        btnRemitChargesHead.setEnabled(flag);
        btnExecChargesHead.setEnabled(flag);
        btnlFailChargesHead.setEnabled(flag);
        btnServiceTaxHead.setEnabled(flag);
        btnNmfAcHead.setEnabled(flag);
        btnSuspenseAcHd.setEnabled(flag);
        btnSalarySuspense.setEnabled(flag);
        btnRTGS_GL.setEnabled(flag);
        btnAcceptChargesHead.setEnabled(flag);
    }

    private void callView(String viewType) {
        btnType = viewType;
        HashMap viewMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getSelectActHdId");
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object map) {
        HashMap paramMap = (HashMap) map;
        System.out.println("Params " + paramMap);
        System.out.println("BtnType " + btnType);
        if (CAH.equals(btnType)) {
            txtCashActHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (IAH.equals(btnType)) {
            txtIBRActHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (ASH.equals(btnType)) {
            txtAppSuspenseAcHd.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (SS.equals(btnType)) {
            txtSalarySuspense.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (RTGS.equals(btnType)) {
            txtRTGS_GL.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (SIC.equals(btnType)) {
            txtSIChargesHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (RC.equals(btnType)) {
            txtRemitChargesHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (AC.equals(btnType)) {
            txtAcceptChargesHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (EC.equals(btnType)) {
            txtExecChargesHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (FC.equals(btnType)) {
            txtFailChargesHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (ST.equals(btnType)) {
            txtServiceTaxHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        } else if (NMF.equals(btnType)) {
            txtNmfAcHead.setText(CommonUtil.convertObjToStr(paramMap.get("AC_HD_ID")));
        }

        if (viewType == AUTHORIZE) {
            btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
            btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
            btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoDayEndType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIB_OnHoliday = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAllowAuth = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCashAuth = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDenomination = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMultiShare = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoServiceTx = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoToken = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExcludePenal = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrConfig = new javax.swing.JToolBar();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace28 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace29 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabConfig = new com.see.truetransact.uicomponent.CTabbedPane();
        panPassword = new com.see.truetransact.uicomponent.CPanel();
        lblPasswordExpire = new com.see.truetransact.uicomponent.CLabel();
        lblMinLength = new com.see.truetransact.uicomponent.CLabel();
        lblUserCantChangePwd = new com.see.truetransact.uicomponent.CLabel();
        lblMaxLength = new com.see.truetransact.uicomponent.CLabel();
        lblUserAccLocked = new com.see.truetransact.uicomponent.CLabel();
        chkPwdNeverExpire = new com.see.truetransact.uicomponent.CCheckBox();
        txtMinLength = new com.see.truetransact.uicomponent.CTextField();
        txtMaxLength = new com.see.truetransact.uicomponent.CTextField();
        lblAfter = new com.see.truetransact.uicomponent.CLabel();
        chkCantChangePwd = new com.see.truetransact.uicomponent.CCheckBox();
        lblAtleast = new com.see.truetransact.uicomponent.CLabel();
        panAttempts = new com.see.truetransact.uicomponent.CPanel();
        lblAttempts = new com.see.truetransact.uicomponent.CLabel();
        txtAttempts = new com.see.truetransact.uicomponent.CTextField();
        panUpperCase = new com.see.truetransact.uicomponent.CPanel();
        lblUpCase = new com.see.truetransact.uicomponent.CLabel();
        txtUpperCase = new com.see.truetransact.uicomponent.CTextField();
        panSplChar = new com.see.truetransact.uicomponent.CPanel();
        lblSplChar1 = new com.see.truetransact.uicomponent.CLabel();
        txtSplChar = new com.see.truetransact.uicomponent.CTextField();
        lblAtleastUp = new com.see.truetransact.uicomponent.CLabel();
        lblLowAtleast = new com.see.truetransact.uicomponent.CLabel();
        panNo = new com.see.truetransact.uicomponent.CPanel();
        lblNo = new com.see.truetransact.uicomponent.CLabel();
        txtNo = new com.see.truetransact.uicomponent.CTextField();
        lblPwdExpiry = new com.see.truetransact.uicomponent.CLabel();
        panDays = new com.see.truetransact.uicomponent.CPanel();
        lblDays = new com.see.truetransact.uicomponent.CLabel();
        txtDays = new com.see.truetransact.uicomponent.CTextField();
        chkAccLocked = new com.see.truetransact.uicomponent.CCheckBox();
        lblFirstLogin = new com.see.truetransact.uicomponent.CLabel();
        chkFirstLogin = new com.see.truetransact.uicomponent.CCheckBox();
        lblLastPwd = new com.see.truetransact.uicomponent.CLabel();
        panPwds = new com.see.truetransact.uicomponent.CPanel();
        lblPwds = new com.see.truetransact.uicomponent.CLabel();
        txtPwds = new com.see.truetransact.uicomponent.CTextField();
        sptPwdConfig = new com.see.truetransact.uicomponent.CSeparator();
        panAgeRules = new com.see.truetransact.uicomponent.CPanel();
        lblMinorAge = new com.see.truetransact.uicomponent.CLabel();
        lblRetireAge = new com.see.truetransact.uicomponent.CLabel();
        txtMinorAge = new com.see.truetransact.uicomponent.CTextField();
        txtRetirementAge = new com.see.truetransact.uicomponent.CTextField();
        lblSeniorCitizenAge = new javax.swing.JLabel();
        txtSeniorCitizenAge = new com.see.truetransact.uicomponent.CTextField();
        panMiscRules = new com.see.truetransact.uicomponent.CPanel();
        txtPanDetails = new com.see.truetransact.uicomponent.CTextField();
        lblMemFee = new com.see.truetransact.uicomponent.CLabel();
        txtMemFee = new com.see.truetransact.uicomponent.CTextField();
        lblAllowCashierAuthorization = new com.see.truetransact.uicomponent.CLabel();
        panAuthorizationByStaff = new com.see.truetransact.uicomponent.CPanel();
        rdoAllowAuthorizationYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAllowAuthorizationNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblLogOutTime = new com.see.truetransact.uicomponent.CLabel();
        txtLogOutTime = new com.see.truetransact.uicomponent.CTextField();
        lblAllowDenomination = new com.see.truetransact.uicomponent.CLabel();
        lblAllowServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblAllowAuthorizationByStaff = new com.see.truetransact.uicomponent.CLabel();
        panDenomination = new com.see.truetransact.uicomponent.CPanel();
        rdoDenominationYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDenominationNo = new com.see.truetransact.uicomponent.CRadioButton();
        panMultiShare = new com.see.truetransact.uicomponent.CPanel();
        rdoMultiShareYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMultiShareNo = new com.see.truetransact.uicomponent.CRadioButton();
        panServiceTax = new com.see.truetransact.uicomponent.CPanel();
        rdoServiceTaxYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoServiceTaxNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblAllowTokenNo = new com.see.truetransact.uicomponent.CLabel();
        lblAllowMultiShare = new com.see.truetransact.uicomponent.CLabel();
        panExcludePenalIntFromReports = new com.see.truetransact.uicomponent.CPanel();
        rdoExcludePenalIntFromReportsYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoExcludePenalIntFromReportsNo = new com.see.truetransact.uicomponent.CRadioButton();
        panCashierAuthorization = new com.see.truetransact.uicomponent.CPanel();
        rdoCashierAuthorizationYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCashierAuthorizationNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblPanDetails = new com.see.truetransact.uicomponent.CLabel();
        lblPenalIntFromReports = new com.see.truetransact.uicomponent.CLabel();
        txtPendingTxnAllowedDays = new com.see.truetransact.uicomponent.CTextField();
        panTokenNoAllow = new com.see.truetransact.uicomponent.CPanel();
        rdoTokenNoAllowYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTokenNoAllowNo = new com.see.truetransact.uicomponent.CRadioButton();
        lblEffectiveFrom = new com.see.truetransact.uicomponent.CLabel();
        lblServicePeriod = new com.see.truetransact.uicomponent.CLabel();
        tdtEffectiveFrom = new com.see.truetransact.uicomponent.CDateField();
        lblPendingTxnAllowedDays = new com.see.truetransact.uicomponent.CLabel();
        txtServicePeriod = new com.see.truetransact.uicomponent.CTextField();
        lblGahanPeriod = new com.see.truetransact.uicomponent.CLabel();
        txtGahanPeriod = new com.see.truetransact.uicomponent.CTextField();
        panHOandBranches = new com.see.truetransact.uicomponent.CPanel();
        lblHeadOffice = new com.see.truetransact.uicomponent.CLabel();
        cboHeadOffice = new com.see.truetransact.uicomponent.CComboBox();
        lblDayEndType = new com.see.truetransact.uicomponent.CLabel();
        panTransactionType = new com.see.truetransact.uicomponent.CPanel();
        rdoDayEndType_BranchLevel = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDayEndType_BankLevel = new com.see.truetransact.uicomponent.CRadioButton();
        lblHolidayIB_Trans2 = new com.see.truetransact.uicomponent.CLabel();
        panIB_OnHoliday = new com.see.truetransact.uicomponent.CPanel();
        rdoIB_OnHoliday_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIB_OnHoliday_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblHolidayIB_Trans1 = new com.see.truetransact.uicomponent.CLabel();
        lblLastFinancialYearEnd = new com.see.truetransact.uicomponent.CLabel();
        lblYearEndProcessDate = new com.see.truetransact.uicomponent.CLabel();
        tdtYearEndProcessDate = new com.see.truetransact.uicomponent.CDateField();
        tdtLastFinancialYearEnd = new com.see.truetransact.uicomponent.CDateField();
        panAcHds = new com.see.truetransact.uicomponent.CPanel();
        panTransaction = new com.see.truetransact.uicomponent.CPanel();
        btnServiceTaxHead = new com.see.truetransact.uicomponent.CButton();
        lblServiceTaxHead = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTaxHead = new com.see.truetransact.uicomponent.CTextField();
        btnNmfAcHead = new com.see.truetransact.uicomponent.CButton();
        txtNmfAcHead = new com.see.truetransact.uicomponent.CTextField();
        lblNmfAcHead = new com.see.truetransact.uicomponent.CLabel();
        btnIBRActHead = new com.see.truetransact.uicomponent.CButton();
        txtIBRActHead = new com.see.truetransact.uicomponent.CTextField();
        lblIBRActHead = new com.see.truetransact.uicomponent.CLabel();
        lblSIChargesHead = new com.see.truetransact.uicomponent.CLabel();
        txtFailChargesHead = new com.see.truetransact.uicomponent.CTextField();
        btnlFailChargesHead = new com.see.truetransact.uicomponent.CButton();
        lblFailChargesHead = new com.see.truetransact.uicomponent.CLabel();
        txtSIChargesHead = new com.see.truetransact.uicomponent.CTextField();
        btnSIChargesHead = new com.see.truetransact.uicomponent.CButton();
        lblRemitChargesHead = new com.see.truetransact.uicomponent.CLabel();
        txtRemitChargesHead = new com.see.truetransact.uicomponent.CTextField();
        btnRemitChargesHead = new com.see.truetransact.uicomponent.CButton();
        lblExecChargesHead = new com.see.truetransact.uicomponent.CLabel();
        txtExecChargesHead = new com.see.truetransact.uicomponent.CTextField();
        btnExecChargesHead = new com.see.truetransact.uicomponent.CButton();
        lblAcceptChargesHead = new com.see.truetransact.uicomponent.CLabel();
        txtAcceptChargesHead = new com.see.truetransact.uicomponent.CTextField();
        btnAcceptChargesHead = new com.see.truetransact.uicomponent.CButton();
        panOverdueRateBills8 = new com.see.truetransact.uicomponent.CPanel();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblDefaultPostage_Per1 = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblCashActHead = new com.see.truetransact.uicomponent.CLabel();
        txtCashActHead = new com.see.truetransact.uicomponent.CTextField();
        btnCashActHead = new com.see.truetransact.uicomponent.CButton();
        panTransaction1 = new com.see.truetransact.uicomponent.CPanel();
        btnSalarySuspense = new com.see.truetransact.uicomponent.CButton();
        txtAppSuspenseAcHd = new com.see.truetransact.uicomponent.CTextField();
        txtRTGS_GL = new com.see.truetransact.uicomponent.CTextField();
        lblSuspenseAcHead = new com.see.truetransact.uicomponent.CLabel();
        lblSalarySuspense = new com.see.truetransact.uicomponent.CLabel();
        btnRTGS_GL = new com.see.truetransact.uicomponent.CButton();
        lblRTGSGL = new com.see.truetransact.uicomponent.CLabel();
        btnSuspenseAcHd = new com.see.truetransact.uicomponent.CButton();
        txtSalarySuspense = new com.see.truetransact.uicomponent.CTextField();
        panAMC_Details = new com.see.truetransact.uicomponent.CPanel();
        txtAmcAlertTime = new com.see.truetransact.uicomponent.CTextField();
        lblFromDate = new com.see.truetransact.uicomponent.CLabel();
        tdtFromDate = new com.see.truetransact.uicomponent.CDateField();
        tdtToDate = new com.see.truetransact.uicomponent.CDateField();
        lblAmcAlertTime = new com.see.truetransact.uicomponent.CLabel();
        lblToDate = new com.see.truetransact.uicomponent.CLabel();
        panOthers = new com.see.truetransact.uicomponent.CPanel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrConfig = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitEdit = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Configuration");
        setMinimumSize(new java.awt.Dimension(657, 550));
        setPreferredSize(new java.awt.Dimension(657, 550));

        tbrConfig.setEnabled(false);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrConfig.add(btnEdit);

        lblSpace5.setText("     ");
        tbrConfig.add(lblSpace5);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrConfig.add(btnSave);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrConfig.add(lblSpace26);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrConfig.add(btnCancel);

        lblSpace1.setText("     ");
        tbrConfig.add(lblSpace1);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrConfig.add(btnAuthorize);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrConfig.add(lblSpace27);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrConfig.add(btnException);

        lblSpace28.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace28.setText("     ");
        lblSpace28.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace28.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrConfig.add(lblSpace28);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrConfig.add(btnReject);

        lblSpace4.setText("     ");
        tbrConfig.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrConfig.add(btnPrint);

        lblSpace29.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace29.setText("     ");
        lblSpace29.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace29.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrConfig.add(lblSpace29);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrConfig.add(btnClose);

        getContentPane().add(tbrConfig, java.awt.BorderLayout.NORTH);

        tabConfig.setMinimumSize(new java.awt.Dimension(641, 550));
        tabConfig.setPreferredSize(new java.awt.Dimension(641, 550));

        panPassword.setLayout(new java.awt.GridBagLayout());

        lblPasswordExpire.setText("Password Never Expire");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblPasswordExpire, gridBagConstraints);

        lblMinLength.setText("Minimum Length");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblMinLength, gridBagConstraints);

        lblUserCantChangePwd.setText("User Cannot Change Password");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblUserCantChangePwd, gridBagConstraints);

        lblMaxLength.setText("Maximum Length");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblMaxLength, gridBagConstraints);

        lblUserAccLocked.setText("User Account Locked");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblUserAccLocked, gridBagConstraints);

        chkPwdNeverExpire.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPwdNeverExpireActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPassword.add(chkPwdNeverExpire, gridBagConstraints);

        txtMinLength.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinLength.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(txtMinLength, gridBagConstraints);

        txtMaxLength.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxLength.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(txtMaxLength, gridBagConstraints);

        lblAfter.setText("After");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblAfter, gridBagConstraints);

        chkCantChangePwd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCantChangePwdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPassword.add(chkCantChangePwd, gridBagConstraints);

        lblAtleast.setText("Atleast");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblAtleast, gridBagConstraints);

        panAttempts.setLayout(new java.awt.GridBagLayout());

        lblAttempts.setText("Attempts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAttempts.add(lblAttempts, gridBagConstraints);

        txtAttempts.setMinimumSize(new java.awt.Dimension(30, 21));
        txtAttempts.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAttempts.add(txtAttempts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassword.add(panAttempts, gridBagConstraints);

        panUpperCase.setLayout(new java.awt.GridBagLayout());

        lblUpCase.setText("Upper Case");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panUpperCase.add(lblUpCase, gridBagConstraints);

        txtUpperCase.setMinimumSize(new java.awt.Dimension(30, 21));
        txtUpperCase.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panUpperCase.add(txtUpperCase, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassword.add(panUpperCase, gridBagConstraints);

        panSplChar.setLayout(new java.awt.GridBagLayout());

        lblSplChar1.setText("Special Character");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplChar.add(lblSplChar1, gridBagConstraints);

        txtSplChar.setMinimumSize(new java.awt.Dimension(30, 21));
        txtSplChar.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSplChar.add(txtSplChar, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassword.add(panSplChar, gridBagConstraints);

        lblAtleastUp.setText("Atleast");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblAtleastUp, gridBagConstraints);

        lblLowAtleast.setText("Atleast");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblLowAtleast, gridBagConstraints);

        panNo.setLayout(new java.awt.GridBagLayout());

        lblNo.setText("Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNo.add(lblNo, gridBagConstraints);

        txtNo.setMinimumSize(new java.awt.Dimension(30, 21));
        txtNo.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNo.add(txtNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassword.add(panNo, gridBagConstraints);

        lblPwdExpiry.setText("Password Expiry");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblPwdExpiry, gridBagConstraints);

        panDays.setLayout(new java.awt.GridBagLayout());

        lblDays.setText("Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDays.add(lblDays, gridBagConstraints);

        txtDays.setMinimumSize(new java.awt.Dimension(30, 21));
        txtDays.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDays.add(txtDays, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassword.add(panDays, gridBagConstraints);

        chkAccLocked.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAccLockedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPassword.add(chkAccLocked, gridBagConstraints);

        lblFirstLogin.setText("Change Password On First Login");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblFirstLogin, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPassword.add(chkFirstLogin, gridBagConstraints);

        lblLastPwd.setText("Should Not Be Any Of The Last");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPassword.add(lblLastPwd, gridBagConstraints);

        panPwds.setLayout(new java.awt.GridBagLayout());

        lblPwds.setText("Passwords");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPwds.add(lblPwds, gridBagConstraints);

        txtPwds.setMinimumSize(new java.awt.Dimension(30, 21));
        txtPwds.setPreferredSize(new java.awt.Dimension(30, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPwds.add(txtPwds, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPassword.add(panPwds, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPassword.add(sptPwdConfig, gridBagConstraints);

        tabConfig.addTab("Pwd Rules", panPassword);

        panAgeRules.setLayout(new java.awt.GridBagLayout());

        lblMinorAge.setText("Minor Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgeRules.add(lblMinorAge, gridBagConstraints);

        lblRetireAge.setText("Retirement Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgeRules.add(lblRetireAge, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgeRules.add(txtMinorAge, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgeRules.add(txtRetirementAge, gridBagConstraints);

        lblSeniorCitizenAge.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
        lblSeniorCitizenAge.setText("Senior Citizen Age");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panAgeRules.add(lblSeniorCitizenAge, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgeRules.add(txtSeniorCitizenAge, gridBagConstraints);

        tabConfig.addTab("Age Rules ", panAgeRules);

        panMiscRules.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(txtPanDetails, gridBagConstraints);

        lblMemFee.setText("Nominal Membership Fee");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblMemFee, gridBagConstraints);

        txtMemFee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMemFeeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(txtMemFee, gridBagConstraints);

        lblAllowCashierAuthorization.setText("Allow Cashier Authorization");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblAllowCashierAuthorization, gridBagConstraints);

        panAuthorizationByStaff.setLayout(new java.awt.GridBagLayout());

        rdoAllowAuth.add(rdoAllowAuthorizationYes);
        rdoAllowAuthorizationYes.setText("Yes");
        rdoAllowAuthorizationYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAllowAuthorizationYesActionPerformed(evt);
            }
        });
        rdoAllowAuthorizationYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoAllowAuthorizationYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panAuthorizationByStaff.add(rdoAllowAuthorizationYes, gridBagConstraints);

        rdoAllowAuth.add(rdoAllowAuthorizationNo);
        rdoAllowAuthorizationNo.setText("No");
        rdoAllowAuthorizationNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAllowAuthorizationNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAuthorizationByStaff.add(rdoAllowAuthorizationNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(panAuthorizationByStaff, gridBagConstraints);

        lblLogOutTime.setText("Auto Logout Time(Minutes)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblLogOutTime, gridBagConstraints);

        txtLogOutTime.setMinimumSize(new java.awt.Dimension(50, 21));
        txtLogOutTime.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(txtLogOutTime, gridBagConstraints);

        lblAllowDenomination.setText("Allow Denomination");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblAllowDenomination, gridBagConstraints);

        lblAllowServiceTax.setText("Allow Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblAllowServiceTax, gridBagConstraints);

        lblAllowAuthorizationByStaff.setText("Allow Authorization By Staff in Own A/c");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblAllowAuthorizationByStaff, gridBagConstraints);

        panDenomination.setLayout(new java.awt.GridBagLayout());

        rdoDenomination.add(rdoDenominationYes);
        rdoDenominationYes.setText("Yes");
        rdoDenominationYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDenominationYesActionPerformed(evt);
            }
        });
        rdoDenominationYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoDenominationYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panDenomination.add(rdoDenominationYes, gridBagConstraints);

        rdoDenomination.add(rdoDenominationNo);
        rdoDenominationNo.setText("No");
        rdoDenominationNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDenominationNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDenomination.add(rdoDenominationNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(panDenomination, gridBagConstraints);

        panMultiShare.setLayout(new java.awt.GridBagLayout());

        rdoMultiShare.add(rdoMultiShareYes);
        rdoMultiShareYes.setText("Yes");
        rdoMultiShareYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMultiShareYesActionPerformed(evt);
            }
        });
        rdoMultiShareYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoMultiShareYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panMultiShare.add(rdoMultiShareYes, gridBagConstraints);

        rdoMultiShare.add(rdoMultiShareNo);
        rdoMultiShareNo.setText("No");
        rdoMultiShareNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMultiShareNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMultiShare.add(rdoMultiShareNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(panMultiShare, gridBagConstraints);

        panServiceTax.setLayout(new java.awt.GridBagLayout());

        rdoServiceTx.add(rdoServiceTaxYes);
        rdoServiceTaxYes.setText("Yes");
        rdoServiceTaxYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoServiceTaxYesActionPerformed(evt);
            }
        });
        rdoServiceTaxYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoServiceTaxYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panServiceTax.add(rdoServiceTaxYes, gridBagConstraints);

        rdoServiceTx.add(rdoServiceTaxNo);
        rdoServiceTaxNo.setText("No");
        rdoServiceTaxNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoServiceTaxNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panServiceTax.add(rdoServiceTaxNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(panServiceTax, gridBagConstraints);

        lblAllowTokenNo.setText("Allow Token No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblAllowTokenNo, gridBagConstraints);

        lblAllowMultiShare.setText("Allow MultiShare");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblAllowMultiShare, gridBagConstraints);

        panExcludePenalIntFromReports.setLayout(new java.awt.GridBagLayout());

        rdoExcludePenal.add(rdoExcludePenalIntFromReportsYes);
        rdoExcludePenalIntFromReportsYes.setText("Yes");
        rdoExcludePenalIntFromReportsYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExcludePenalIntFromReportsYesActionPerformed(evt);
            }
        });
        rdoExcludePenalIntFromReportsYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoExcludePenalIntFromReportsYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panExcludePenalIntFromReports.add(rdoExcludePenalIntFromReportsYes, gridBagConstraints);

        rdoExcludePenal.add(rdoExcludePenalIntFromReportsNo);
        rdoExcludePenalIntFromReportsNo.setText("No");
        rdoExcludePenalIntFromReportsNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoExcludePenalIntFromReportsNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panExcludePenalIntFromReports.add(rdoExcludePenalIntFromReportsNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(panExcludePenalIntFromReports, gridBagConstraints);

        panCashierAuthorization.setLayout(new java.awt.GridBagLayout());

        rdoCashAuth.add(rdoCashierAuthorizationYes);
        rdoCashierAuthorizationYes.setText("Yes");
        rdoCashierAuthorizationYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCashierAuthorizationYesActionPerformed(evt);
            }
        });
        rdoCashierAuthorizationYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoCashierAuthorizationYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panCashierAuthorization.add(rdoCashierAuthorizationYes, gridBagConstraints);

        rdoCashAuth.add(rdoCashierAuthorizationNo);
        rdoCashierAuthorizationNo.setText("No");
        rdoCashierAuthorizationNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCashierAuthorizationNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCashierAuthorization.add(rdoCashierAuthorizationNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(panCashierAuthorization, gridBagConstraints);

        lblPanDetails.setText("Cash Limit For PAN Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblPanDetails, gridBagConstraints);

        lblPenalIntFromReports.setText("Exclude Penal Int From Reports");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblPenalIntFromReports, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(txtPendingTxnAllowedDays, gridBagConstraints);

        panTokenNoAllow.setLayout(new java.awt.GridBagLayout());

        rdoToken.add(rdoTokenNoAllowYes);
        rdoTokenNoAllowYes.setText("Yes");
        rdoTokenNoAllowYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTokenNoAllowYesActionPerformed(evt);
            }
        });
        rdoTokenNoAllowYes.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoTokenNoAllowYesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        panTokenNoAllow.add(rdoTokenNoAllowYes, gridBagConstraints);

        rdoToken.add(rdoTokenNoAllowNo);
        rdoTokenNoAllowNo.setText("No");
        rdoTokenNoAllowNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTokenNoAllowNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTokenNoAllow.add(rdoTokenNoAllowNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(panTokenNoAllow, gridBagConstraints);

        lblEffectiveFrom.setText("Effective From ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblEffectiveFrom, gridBagConstraints);

        lblServicePeriod.setText("Employee Service Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblServicePeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(tdtEffectiveFrom, gridBagConstraints);

        lblPendingTxnAllowedDays.setText("Pending Txn Allowed Days");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblPendingTxnAllowedDays, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(txtServicePeriod, gridBagConstraints);

        lblGahanPeriod.setText("Gahan Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(lblGahanPeriod, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMiscRules.add(txtGahanPeriod, gridBagConstraints);

        tabConfig.addTab("Misc Rules", panMiscRules);

        panHOandBranches.setLayout(new java.awt.GridBagLayout());

        lblHeadOffice.setText("Head Office");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(lblHeadOffice, gridBagConstraints);

        cboHeadOffice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboHeadOfficeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(cboHeadOffice, gridBagConstraints);

        lblDayEndType.setText("Day End Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(lblDayEndType, gridBagConstraints);

        panTransactionType.setMinimumSize(new java.awt.Dimension(130, 23));
        panTransactionType.setPreferredSize(new java.awt.Dimension(130, 23));
        panTransactionType.setLayout(new java.awt.GridBagLayout());

        rdoDayEndType.add(rdoDayEndType_BranchLevel);
        rdoDayEndType_BranchLevel.setText("Branch Level");
        rdoDayEndType_BranchLevel.setMaximumSize(new java.awt.Dimension(100, 27));
        rdoDayEndType_BranchLevel.setMinimumSize(new java.awt.Dimension(97, 27));
        rdoDayEndType_BranchLevel.setPreferredSize(new java.awt.Dimension(97, 27));
        rdoDayEndType_BranchLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDayEndType_BranchLevelActionPerformed(evt);
            }
        });
        panTransactionType.add(rdoDayEndType_BranchLevel, new java.awt.GridBagConstraints());

        rdoDayEndType.add(rdoDayEndType_BankLevel);
        rdoDayEndType_BankLevel.setText("Bank Level");
        rdoDayEndType_BankLevel.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoDayEndType_BankLevel.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoDayEndType_BankLevel.setMinimumSize(new java.awt.Dimension(97, 27));
        rdoDayEndType_BankLevel.setPreferredSize(new java.awt.Dimension(100, 27));
        panTransactionType.add(rdoDayEndType_BankLevel, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(panTransactionType, gridBagConstraints);

        lblHolidayIB_Trans2.setText("Transactions on Holiday");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panHOandBranches.add(lblHolidayIB_Trans2, gridBagConstraints);

        panIB_OnHoliday.setMinimumSize(new java.awt.Dimension(130, 23));
        panIB_OnHoliday.setPreferredSize(new java.awt.Dimension(106, 23));
        panIB_OnHoliday.setLayout(new java.awt.GridBagLayout());

        rdoIB_OnHoliday.add(rdoIB_OnHoliday_Yes);
        rdoIB_OnHoliday_Yes.setText("Yes");
        rdoIB_OnHoliday_Yes.setMaximumSize(new java.awt.Dimension(100, 27));
        rdoIB_OnHoliday_Yes.setMinimumSize(new java.awt.Dimension(57, 27));
        rdoIB_OnHoliday_Yes.setPreferredSize(new java.awt.Dimension(57, 27));
        rdoIB_OnHoliday_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIB_OnHoliday_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 3);
        panIB_OnHoliday.add(rdoIB_OnHoliday_Yes, gridBagConstraints);

        rdoIB_OnHoliday.add(rdoIB_OnHoliday_No);
        rdoIB_OnHoliday_No.setText("No");
        rdoIB_OnHoliday_No.setMargin(new java.awt.Insets(2, 5, 2, 2));
        rdoIB_OnHoliday_No.setMaximumSize(new java.awt.Dimension(69, 27));
        rdoIB_OnHoliday_No.setMinimumSize(new java.awt.Dimension(97, 27));
        rdoIB_OnHoliday_No.setPreferredSize(new java.awt.Dimension(100, 27));
        rdoIB_OnHoliday_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIB_OnHoliday_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panIB_OnHoliday.add(rdoIB_OnHoliday_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 59;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(panIB_OnHoliday, gridBagConstraints);

        lblHolidayIB_Trans1.setText("Allow Interbranch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panHOandBranches.add(lblHolidayIB_Trans1, gridBagConstraints);

        lblLastFinancialYearEnd.setText("Last Financial Year End");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(lblLastFinancialYearEnd, gridBagConstraints);

        lblYearEndProcessDate.setText("Year End Process Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(lblYearEndProcessDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(tdtYearEndProcessDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panHOandBranches.add(tdtLastFinancialYearEnd, gridBagConstraints);

        tabConfig.addTab("H.O. and Dayend", panHOandBranches);

        panAcHds.setLayout(new java.awt.GridBagLayout());

        panTransaction.setBorder(javax.swing.BorderFactory.createTitledBorder("SI Charge Ac Hd Details"));
        panTransaction.setMinimumSize(new java.awt.Dimension(200, 350));
        panTransaction.setPreferredSize(new java.awt.Dimension(200, 350));
        panTransaction.setLayout(new java.awt.GridBagLayout());

        btnServiceTaxHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnServiceTaxHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnServiceTaxHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnServiceTaxHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceTaxHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnServiceTaxHead, gridBagConstraints);

        lblServiceTaxHead.setText("SI/Failure Service Tax Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblServiceTaxHead, gridBagConstraints);

        txtServiceTaxHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtServiceTaxHead, gridBagConstraints);

        btnNmfAcHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNmfAcHead.setEnabled(false);
        btnNmfAcHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnNmfAcHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNmfAcHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNmfAcHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnNmfAcHead, gridBagConstraints);

        txtNmfAcHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtNmfAcHead, gridBagConstraints);

        lblNmfAcHead.setText("NMF Act Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblNmfAcHead, gridBagConstraints);

        btnIBRActHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIBRActHead.setEnabled(false);
        btnIBRActHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIBRActHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIBRActHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIBRActHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnIBRActHead, gridBagConstraints);

        txtIBRActHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtIBRActHead, gridBagConstraints);

        lblIBRActHead.setText("IBR Act. Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblIBRActHead, gridBagConstraints);

        lblSIChargesHead.setText("SI Commission Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblSIChargesHead, gridBagConstraints);

        txtFailChargesHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtFailChargesHead, gridBagConstraints);

        btnlFailChargesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnlFailChargesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnlFailChargesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnlFailChargesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlFailChargesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnlFailChargesHead, gridBagConstraints);

        lblFailChargesHead.setText("Failure Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblFailChargesHead, gridBagConstraints);

        txtSIChargesHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtSIChargesHead, gridBagConstraints);

        btnSIChargesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSIChargesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSIChargesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSIChargesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSIChargesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnSIChargesHead, gridBagConstraints);

        lblRemitChargesHead.setText("Remittance Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblRemitChargesHead, gridBagConstraints);

        txtRemitChargesHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtRemitChargesHead, gridBagConstraints);

        btnRemitChargesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRemitChargesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRemitChargesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRemitChargesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemitChargesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnRemitChargesHead, gridBagConstraints);

        lblExecChargesHead.setText("Execution Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblExecChargesHead, gridBagConstraints);

        txtExecChargesHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtExecChargesHead, gridBagConstraints);

        btnExecChargesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExecChargesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnExecChargesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExecChargesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExecChargesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnExecChargesHead, gridBagConstraints);

        lblAcceptChargesHead.setText("Postage Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblAcceptChargesHead, gridBagConstraints);

        txtAcceptChargesHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtAcceptChargesHead, gridBagConstraints);

        btnAcceptChargesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcceptChargesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcceptChargesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcceptChargesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptChargesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnAcceptChargesHead, gridBagConstraints);

        panOverdueRateBills8.setLayout(new java.awt.GridBagLayout());

        txtServiceTax.setMinimumSize(new java.awt.Dimension(50, 21));
        txtServiceTax.setPreferredSize(new java.awt.Dimension(50, 21));
        txtServiceTax.setValidation(new PercentageValidation());
        panOverdueRateBills8.add(txtServiceTax, new java.awt.GridBagConstraints());

        lblDefaultPostage_Per1.setText("%");
        panOverdueRateBills8.add(lblDefaultPostage_Per1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(panOverdueRateBills8, gridBagConstraints);

        lblServiceTax.setText("SI/Failure Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblServiceTax, gridBagConstraints);

        lblCashActHead.setText("Cash Act. Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(lblCashActHead, gridBagConstraints);

        txtCashActHead.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(txtCashActHead, gridBagConstraints);

        btnCashActHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCashActHead.setEnabled(false);
        btnCashActHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCashActHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCashActHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCashActHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction.add(btnCashActHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 277;
        gridBagConstraints.ipady = -45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAcHds.add(panTransaction, gridBagConstraints);

        panTransaction1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTransaction1.setMinimumSize(new java.awt.Dimension(200, 200));
        panTransaction1.setPreferredSize(new java.awt.Dimension(200, 200));
        panTransaction1.setLayout(new java.awt.GridBagLayout());

        btnSalarySuspense.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSalarySuspense.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSalarySuspense.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSalarySuspense.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalarySuspenseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(btnSalarySuspense, gridBagConstraints);

        txtAppSuspenseAcHd.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(txtAppSuspenseAcHd, gridBagConstraints);

        txtRTGS_GL.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(txtRTGS_GL, gridBagConstraints);

        lblSuspenseAcHead.setText("App Suspense Act Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(lblSuspenseAcHead, gridBagConstraints);

        lblSalarySuspense.setText("Salary Suspense");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(lblSalarySuspense, gridBagConstraints);

        btnRTGS_GL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRTGS_GL.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRTGS_GL.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRTGS_GL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRTGS_GLActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(btnRTGS_GL, gridBagConstraints);

        lblRTGSGL.setText("RTGS GL");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(lblRTGSGL, gridBagConstraints);

        btnSuspenseAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSuspenseAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSuspenseAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSuspenseAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuspenseAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(btnSuspenseAcHd, gridBagConstraints);

        txtSalarySuspense.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panTransaction1.add(txtSalarySuspense, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 277;
        gridBagConstraints.ipady = -45;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAcHds.add(panTransaction1, gridBagConstraints);

        tabConfig.addTab("A/c Heads", panAcHds);

        panAMC_Details.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAMC_Details.add(txtAmcAlertTime, gridBagConstraints);

        lblFromDate.setText("CBMS++ AMC From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAMC_Details.add(lblFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAMC_Details.add(tdtFromDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAMC_Details.add(tdtToDate, gridBagConstraints);

        lblAmcAlertTime.setText("CBMS++ AMC Alert Time");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAMC_Details.add(lblAmcAlertTime, gridBagConstraints);

        lblToDate.setText("CBMS++ AMC To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAMC_Details.add(lblToDate, gridBagConstraints);

        tabConfig.addTab("AMC Details", panAMC_Details);

        panOthers.setLayout(new java.awt.GridBagLayout());
        tabConfig.addTab("Others", panOthers);

        getContentPane().add(tabConfig, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace2.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace2, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        mnuProcess.setText("Process");

        mitEdit.setText("Edit");
        mnuProcess.add(mitEdit);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitClose.setText("Close");
        mnuProcess.add(mitClose);

        mbrConfig.add(mnuProcess);

        setJMenuBar(mbrConfig);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuspenseAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuspenseAcHdActionPerformed
        // TODO add your handling code here:
        callView("ASH Account Head");
    }//GEN-LAST:event_btnSuspenseAcHdActionPerformed

    private void rdoDayEndType_BranchLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDayEndType_BranchLevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDayEndType_BranchLevelActionPerformed

    private void btnlFailChargesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlFailChargesHeadActionPerformed
        // TODO add your handling code here:
        callView("FC Account Head");
    }//GEN-LAST:event_btnlFailChargesHeadActionPerformed

    private void btnExecChargesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExecChargesHeadActionPerformed
        // TODO add your handling code here:
        callView("EC Account Head");
    }//GEN-LAST:event_btnExecChargesHeadActionPerformed

    private void btnAcceptChargesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptChargesHeadActionPerformed
        // TODO add your handling code here:
        callView("AC Account Head");
    }//GEN-LAST:event_btnAcceptChargesHeadActionPerformed

    private void btnRemitChargesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemitChargesHeadActionPerformed
        // TODO add your handling code here:
        callView("RC Account Head");
    }//GEN-LAST:event_btnRemitChargesHeadActionPerformed

    private void btnSIChargesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSIChargesHeadActionPerformed
        // TODO add your handling code here:
        callView("SIC Account Head");
    }//GEN-LAST:event_btnSIChargesHeadActionPerformed

    private void cboHeadOfficeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboHeadOfficeActionPerformed
        // TODO add your handling code here:
//        System.out.println("### cboHeadOffice.getSelectedItem() : "+cboHeadOffice.getSelectedItem());
    }//GEN-LAST:event_cboHeadOfficeActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorize(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorize(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorize(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void authorize(String authorizeStatus) {
        if (viewType != AUTHORIZE) {
            viewType = AUTHORIZE;
            //__ To set the ViweType in OB...
            observable.setViewType(AUTHORIZE);

            //__ To Display the Data...
            observable.populateData();

            //__ If the Some Record is there to be Authorized...
            boolean isAuth = observable.getIsAuth();
            if (isAuth) {
                setAuthEnable(isAuth);
                displayAlert(resourceBundle.getString("AUTHWARNING"));
                btnCancelActionPerformed(null);
            } else {
                enableDisablePanPassword(false);
                setButtonEnableDisable(true);
                btnSave.setEnabled(false);
                //__ To Save the data in the Internal Frame...
                setModified(true);
            }

        } else if (viewType == AUTHORIZE) {
            HashMap authMap = new HashMap();
            authMap.put("STATUS", authorizeStatus);
            authMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            authMap.put("AUTHORIZEDT", currDt.clone());

            observable.setAuthorizeMap(authMap);
            observable.doAction();

            //__ To reset the AuthMap in OB...
            observable.setAuthorizeMap(null);
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
        }
    }
    private void chkAccLockedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAccLockedActionPerformed
        // TODO add your handling code here:
        if (chkAccLocked.isSelected() == true) {
            txtAttempts.setText("");
            txtAttempts.setEditable(true);
            txtAttempts.setEnabled(true);
        } else {
            txtAttempts.setEditable(false);
            txtAttempts.setEnabled(false);
            txtAttempts.setText("");
        }
    }//GEN-LAST:event_chkAccLockedActionPerformed

    private void chkCantChangePwdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCantChangePwdActionPerformed
        // TODO add your handling code here:
        if (chkCantChangePwd.isSelected() == true) {
            txtPwds.setEditable(false);
            txtPwds.setEnabled(false);
            txtPwds.setText("");
        } else {
            txtPwds.setText("");
            txtPwds.setEditable(true);
            txtPwds.setEnabled(true);
        }
    }//GEN-LAST:event_chkCantChangePwdActionPerformed

    private void chkPwdNeverExpireActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPwdNeverExpireActionPerformed
        // TODO add your handling code here:
        if (chkPwdNeverExpire.isSelected() == true) {
            txtDays.setEditable(false);
            txtDays.setEnabled(false);
            txtDays.setText("");
        } else {
            txtDays.setText("");
            txtDays.setEditable(true);
            txtDays.setEnabled(true);
        }
    }//GEN-LAST:event_chkPwdNeverExpireActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        observable.populateData();
        enableDisablePanPassword(true);
        setButtonEnableDisable(true);
        setHelpBtnEnableDisable(true);
                setEnab();
        //__ If the Record is Authrized, Disable the Auth Buttons...
        setAuthEnable(observable.getIsAuth());

        //__ To Save the data in the Internal Frame...
        setModified(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        observable.setLblStatus("");
        enableDisablePanPassword(false);
        setButtonEnableDisable(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setHelpBtnEnableDisable(false);
        observable.resetForm();

        //__ To Enable all the Auth Buttons...
        setAuthEnable(false);

        //__ Make the Screen Closable..
        setModified(false);

        //__ Reset the ViewType...
        viewType = 0;
        //__ To reset the ViweType in OB...
        observable.setViewType(0);
        observable.setIsAuth(false);
        cboHeadOffice.setSelectedItem("");
//        cboHeadOffice.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (CommonUtil.convertObjToInt(txtMemFee.getText()) > 0 && txtNmfAcHead.getText().length() == 0) {
            ClientUtil.displayAlert("Please Select NMF A/c Head");
            return;
        }
        if (chkPwdNeverExpire.isSelected() == false && ((!(txtDays.getText().length() > 0) || txtDays.getText().equals("0")))) {
            ClientUtil.showAlertWindow(objMandatoryRB.getString("txtDays"));
        } else if (chkCantChangePwd.isSelected() == false && ((!(txtPwds.getText().length() > 0) || txtPwds.getText().equals("0")))) {
            ClientUtil.showAlertWindow(objMandatoryRB.getString("txtPwds"));
        } else if (chkAccLocked.isSelected() == true && ((!(txtAttempts.getText().length() > 0) || txtAttempts.getText().equals("0")))) {
            ClientUtil.showAlertWindow(objMandatoryRB.getString("txtAttempts"));
        } else if (observable.validatePwdRules(txtMinLength.getText(), txtSplChar.getText(), txtNo.getText(), txtUpperCase.getText())) {
            ClientUtil.showAlertWindow(resourceBundle.getString("PwdRules"));
        } else if (observable.validateMaxLength(txtMinLength.getText(), txtMaxLength.getText())) {
            ClientUtil.showAlertWindow(resourceBundle.getString("MaxWarning"));
        } else if (CommonUtil.convertObjToInt(txtMinLength.getText()) < 6
                || CommonUtil.convertObjToInt(txtUpperCase.getText()) < 1
                || CommonUtil.convertObjToInt(txtNo.getText()) < 1) {
            StringBuffer strMessage = new StringBuffer();
            if (CommonUtil.convertObjToInt(txtMinLength.getText()) < 6) {
                strMessage.append(resourceBundle.getString("PwdMinLength"));
                txtMinLength.setText("6");
            }
            if (CommonUtil.convertObjToInt(txtUpperCase.getText()) < 1) {
                strMessage.append("\n").append(resourceBundle.getString("PwdChar"));
                txtUpperCase.setText("1");
            }
            if (CommonUtil.convertObjToInt(txtNo.getText()) < 1) {
                strMessage.append("\n").append(resourceBundle.getString("PwdNumChar"));
                txtNo.setText("1");
            }
            if (strMessage.length() > 0) {
                displayAlert(strMessage.toString());
            }
            strMessage = null;
        } else {
            savePerformed();
        }

        //__ Make the Screen Closable..
        setModified(false);

        //__ To Enable all the Auth Buttons...
        setAuthEnable(false);
        //__ Reset the ViewType...
        viewType = 0;
        //__ To reset the ViweType in OB...
        observable.setViewType(0);
        observable.setIsAuth(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCashActHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCashActHeadActionPerformed
        // TODO add your handling code here:
        callView("Cash Account Head");
    }//GEN-LAST:event_btnCashActHeadActionPerformed

    private void btnIBRActHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIBRActHeadActionPerformed
        // TODO add your handling code here:
        callView("IBR Account Head");
    }//GEN-LAST:event_btnIBRActHeadActionPerformed

    private void btnNmfAcHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNmfAcHeadActionPerformed
        // TODO add your handling code here:
        callView("NMF Account Head");
    }//GEN-LAST:event_btnNmfAcHeadActionPerformed

    private void txtMemFeeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMemFeeFocusLost
        // TODO add your handling code here:
        if (txtMemFee.getText().length() > 0) {
            btnNmfAcHead.setEnabled(true);
        }
    }//GEN-LAST:event_txtMemFeeFocusLost

    private void rdoAllowAuthorizationYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAllowAuthorizationYesActionPerformed
        if (rdoAllowAuthorizationYes.isSelected() == true) {
            rdoAllowAuthorizationYes.setSelected(true);
            rdoAllowAuthorizationNo.setSelected(false);
        }
        }//GEN-LAST:event_rdoAllowAuthorizationYesActionPerformed

        private void rdoAllowAuthorizationYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoAllowAuthorizationYesFocusLost


    }//GEN-LAST:event_rdoAllowAuthorizationYesFocusLost

    private void rdoAllowAuthorizationNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAllowAuthorizationNoActionPerformed
        // TODO add your handling code here:
        if (rdoAllowAuthorizationNo.isSelected() == true) {
            rdoAllowAuthorizationNo.setSelected(true);
            rdoAllowAuthorizationYes.setSelected(false);
        }
        }//GEN-LAST:event_rdoAllowAuthorizationNoActionPerformed

    private void btnRTGS_GLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRTGS_GLActionPerformed
        callView("RTGS Account Head");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRTGS_GLActionPerformed

    private void rdoDenominationYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDenominationYesActionPerformed
        if (rdoDenominationYes.isSelected() == true) {
            rdoDenominationYes.setSelected(true);
            rdoDenominationNo.setSelected(false);
        }         // TODO add your handling code here:
    }//GEN-LAST:event_rdoDenominationYesActionPerformed

    private void rdoDenominationYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoDenominationYesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoDenominationYesFocusLost

    private void rdoDenominationNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDenominationNoActionPerformed
        if (rdoDenominationNo.isSelected() == true) {
            rdoDenominationNo.setSelected(true);
            rdoDenominationNo.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoDenominationNoActionPerformed

    private void rdoMultiShareYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMultiShareYesActionPerformed
        if (rdoMultiShareYes.isSelected() == true) {
            rdoMultiShareYes.setSelected(true);
            rdoMultiShareNo.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoMultiShareYesActionPerformed

    private void rdoMultiShareYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoMultiShareYesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoMultiShareYesFocusLost

    private void rdoMultiShareNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMultiShareNoActionPerformed
        if (rdoMultiShareNo.isSelected() == true) {
            rdoMultiShareNo.setSelected(true);
            rdoMultiShareYes.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoMultiShareNoActionPerformed

    private void rdoServiceTaxYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoServiceTaxYesActionPerformed
        if (rdoServiceTaxYes.isSelected() == true) {
            rdoServiceTaxYes.setSelected(true);
            rdoServiceTaxNo.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoServiceTaxYesActionPerformed

    private void rdoServiceTaxYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoServiceTaxYesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoServiceTaxYesFocusLost

    private void rdoServiceTaxNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoServiceTaxNoActionPerformed
        if (rdoServiceTaxNo.isSelected() == true) {
            rdoServiceTaxNo.setSelected(true);
            rdoServiceTaxYes.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoServiceTaxNoActionPerformed

    private void rdoExcludePenalIntFromReportsYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExcludePenalIntFromReportsYesActionPerformed
        if (rdoExcludePenalIntFromReportsYes.isSelected() == true) {
            rdoExcludePenalIntFromReportsYes.setSelected(true);
            rdoExcludePenalIntFromReportsNo.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoExcludePenalIntFromReportsYesActionPerformed

    private void rdoExcludePenalIntFromReportsYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoExcludePenalIntFromReportsYesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoExcludePenalIntFromReportsYesFocusLost

    private void rdoExcludePenalIntFromReportsNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoExcludePenalIntFromReportsNoActionPerformed
        if (rdoExcludePenalIntFromReportsNo.isSelected() == true) {
            rdoExcludePenalIntFromReportsNo.setSelected(true);
            rdoExcludePenalIntFromReportsYes.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoExcludePenalIntFromReportsNoActionPerformed

    private void rdoCashierAuthorizationYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCashierAuthorizationYesActionPerformed
        if (rdoCashierAuthorizationYes.isSelected() == true) {
            rdoCashierAuthorizationYes.setSelected(true);
            rdoCashierAuthorizationNo.setSelected(false);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCashierAuthorizationYesActionPerformed

    private void rdoCashierAuthorizationYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoCashierAuthorizationYesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCashierAuthorizationYesFocusLost

    private void rdoCashierAuthorizationNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCashierAuthorizationNoActionPerformed
        if (rdoCashierAuthorizationNo.isSelected() == true) {
            rdoCashierAuthorizationNo.setSelected(true);
            rdoCashierAuthorizationYes.setSelected(false);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_rdoCashierAuthorizationNoActionPerformed

    private void btnSalarySuspenseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalarySuspenseActionPerformed
        callView("SS Account Head");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalarySuspenseActionPerformed

    private void btnServiceTaxHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTaxHeadActionPerformed
        // TODO add your handling code here:
        callView("ST Account Head");
    }//GEN-LAST:event_btnServiceTaxHeadActionPerformed

    private void rdoTokenNoAllowYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTokenNoAllowYesActionPerformed
        if (rdoTokenNoAllowYes.isSelected() == true) {
            rdoTokenNoAllowYes.setSelected(true);
            rdoTokenNoAllowNo.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoTokenNoAllowYesActionPerformed

    private void rdoTokenNoAllowYesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoTokenNoAllowYesFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoTokenNoAllowYesFocusLost

    private void rdoTokenNoAllowNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTokenNoAllowNoActionPerformed
        if (rdoTokenNoAllowNo.isSelected() == true) {
            rdoTokenNoAllowNo.setSelected(true);
            rdoTokenNoAllowYes.setSelected(false);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_rdoTokenNoAllowNoActionPerformed

    private void rdoIB_OnHoliday_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIB_OnHoliday_YesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoIB_OnHoliday_YesActionPerformed

    private void rdoIB_OnHoliday_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIB_OnHoliday_NoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoIB_OnHoliday_NoActionPerformed
    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void enableDisablePanPassword(boolean flag) {
        ClientUtil.enableDisable(panPassword, flag);
        ClientUtil.enableDisable(panAgeRules, flag);
        ClientUtil.enableDisable(panMiscRules, flag);
        ClientUtil.enableDisable(panAMC_Details, flag);
        ClientUtil.enableDisable(panHOandBranches, flag);
        ClientUtil.enableDisable(panAcHds, flag);
    }
    /* action to perform when  main save button is pressed */

    private void savePerformed() {
        updateOBFields();
        observable.doAction();
        enableDisablePanPassword(false);
        setButtonEnableDisable(false);
        setHelpBtnEnableDisable(false);
        observable.resetForm();
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable(boolean flag) {
        btnEdit.setEnabled(!flag);
        mitEdit.setEnabled(!flag);
        btnSave.setEnabled(flag);
        btnCancel.setEnabled(flag);
        mitSave.setEnabled(flag);
        mitCancel.setEnabled(flag);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcceptChargesHead;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCashActHead;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExecChargesHead;
    private com.see.truetransact.uicomponent.CButton btnIBRActHead;
    private com.see.truetransact.uicomponent.CButton btnNmfAcHead;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRTGS_GL;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRemitChargesHead;
    private com.see.truetransact.uicomponent.CButton btnSIChargesHead;
    private com.see.truetransact.uicomponent.CButton btnSalarySuspense;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnServiceTaxHead;
    private com.see.truetransact.uicomponent.CButton btnSuspenseAcHd;
    private com.see.truetransact.uicomponent.CButton btnlFailChargesHead;
    private com.see.truetransact.uicomponent.CComboBox cboHeadOffice;
    private com.see.truetransact.uicomponent.CCheckBox chkAccLocked;
    private com.see.truetransact.uicomponent.CCheckBox chkCantChangePwd;
    private com.see.truetransact.uicomponent.CCheckBox chkFirstLogin;
    private com.see.truetransact.uicomponent.CCheckBox chkPwdNeverExpire;
    private com.see.truetransact.uicomponent.CLabel lblAcceptChargesHead;
    private com.see.truetransact.uicomponent.CLabel lblAfter;
    private com.see.truetransact.uicomponent.CLabel lblAllowAuthorizationByStaff;
    private com.see.truetransact.uicomponent.CLabel lblAllowCashierAuthorization;
    private com.see.truetransact.uicomponent.CLabel lblAllowDenomination;
    private com.see.truetransact.uicomponent.CLabel lblAllowMultiShare;
    private com.see.truetransact.uicomponent.CLabel lblAllowServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblAllowTokenNo;
    private com.see.truetransact.uicomponent.CLabel lblAmcAlertTime;
    private com.see.truetransact.uicomponent.CLabel lblAtleast;
    private com.see.truetransact.uicomponent.CLabel lblAtleastUp;
    private com.see.truetransact.uicomponent.CLabel lblAttempts;
    private com.see.truetransact.uicomponent.CLabel lblCashActHead;
    private com.see.truetransact.uicomponent.CLabel lblDayEndType;
    private com.see.truetransact.uicomponent.CLabel lblDays;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage_Per1;
    private com.see.truetransact.uicomponent.CLabel lblEffectiveFrom;
    private com.see.truetransact.uicomponent.CLabel lblExecChargesHead;
    private com.see.truetransact.uicomponent.CLabel lblFailChargesHead;
    private com.see.truetransact.uicomponent.CLabel lblFirstLogin;
    private com.see.truetransact.uicomponent.CLabel lblFromDate;
    private com.see.truetransact.uicomponent.CLabel lblGahanPeriod;
    private com.see.truetransact.uicomponent.CLabel lblHeadOffice;
    private com.see.truetransact.uicomponent.CLabel lblHolidayIB_Trans1;
    private com.see.truetransact.uicomponent.CLabel lblHolidayIB_Trans2;
    private com.see.truetransact.uicomponent.CLabel lblIBRActHead;
    private com.see.truetransact.uicomponent.CLabel lblLastFinancialYearEnd;
    private com.see.truetransact.uicomponent.CLabel lblLastPwd;
    private com.see.truetransact.uicomponent.CLabel lblLogOutTime;
    private com.see.truetransact.uicomponent.CLabel lblLowAtleast;
    private com.see.truetransact.uicomponent.CLabel lblMaxLength;
    private com.see.truetransact.uicomponent.CLabel lblMemFee;
    private com.see.truetransact.uicomponent.CLabel lblMinLength;
    private com.see.truetransact.uicomponent.CLabel lblMinorAge;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNmfAcHead;
    private com.see.truetransact.uicomponent.CLabel lblNo;
    private com.see.truetransact.uicomponent.CLabel lblPanDetails;
    private com.see.truetransact.uicomponent.CLabel lblPasswordExpire;
    private com.see.truetransact.uicomponent.CLabel lblPenalIntFromReports;
    private com.see.truetransact.uicomponent.CLabel lblPendingTxnAllowedDays;
    private com.see.truetransact.uicomponent.CLabel lblPwdExpiry;
    private com.see.truetransact.uicomponent.CLabel lblPwds;
    private com.see.truetransact.uicomponent.CLabel lblRTGSGL;
    private com.see.truetransact.uicomponent.CLabel lblRemitChargesHead;
    private com.see.truetransact.uicomponent.CLabel lblRetireAge;
    private com.see.truetransact.uicomponent.CLabel lblSIChargesHead;
    private com.see.truetransact.uicomponent.CLabel lblSalarySuspense;
    private javax.swing.JLabel lblSeniorCitizenAge;
    private com.see.truetransact.uicomponent.CLabel lblServicePeriod;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxHead;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace28;
    private com.see.truetransact.uicomponent.CLabel lblSpace29;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSplChar1;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSuspenseAcHead;
    private com.see.truetransact.uicomponent.CLabel lblToDate;
    private com.see.truetransact.uicomponent.CLabel lblUpCase;
    private com.see.truetransact.uicomponent.CLabel lblUserAccLocked;
    private com.see.truetransact.uicomponent.CLabel lblUserCantChangePwd;
    private com.see.truetransact.uicomponent.CLabel lblYearEndProcessDate;
    private com.see.truetransact.uicomponent.CMenuBar mbrConfig;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAMC_Details;
    private com.see.truetransact.uicomponent.CPanel panAcHds;
    private com.see.truetransact.uicomponent.CPanel panAgeRules;
    private com.see.truetransact.uicomponent.CPanel panAttempts;
    private com.see.truetransact.uicomponent.CPanel panAuthorizationByStaff;
    private com.see.truetransact.uicomponent.CPanel panCashierAuthorization;
    private com.see.truetransact.uicomponent.CPanel panDays;
    private com.see.truetransact.uicomponent.CPanel panDenomination;
    private com.see.truetransact.uicomponent.CPanel panExcludePenalIntFromReports;
    private com.see.truetransact.uicomponent.CPanel panHOandBranches;
    private com.see.truetransact.uicomponent.CPanel panIB_OnHoliday;
    private com.see.truetransact.uicomponent.CPanel panMiscRules;
    private com.see.truetransact.uicomponent.CPanel panMultiShare;
    private com.see.truetransact.uicomponent.CPanel panNo;
    private com.see.truetransact.uicomponent.CPanel panOthers;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills8;
    private com.see.truetransact.uicomponent.CPanel panPassword;
    private com.see.truetransact.uicomponent.CPanel panPwds;
    private com.see.truetransact.uicomponent.CPanel panServiceTax;
    private com.see.truetransact.uicomponent.CPanel panSplChar;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTokenNoAllow;
    private com.see.truetransact.uicomponent.CPanel panTransaction;
    private com.see.truetransact.uicomponent.CPanel panTransaction1;
    private com.see.truetransact.uicomponent.CPanel panTransactionType;
    private com.see.truetransact.uicomponent.CPanel panUpperCase;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAllowAuth;
    private com.see.truetransact.uicomponent.CRadioButton rdoAllowAuthorizationNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoAllowAuthorizationYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCashAuth;
    private com.see.truetransact.uicomponent.CRadioButton rdoCashierAuthorizationNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoCashierAuthorizationYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDayEndType;
    private com.see.truetransact.uicomponent.CRadioButton rdoDayEndType_BankLevel;
    private com.see.truetransact.uicomponent.CRadioButton rdoDayEndType_BranchLevel;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDenomination;
    private com.see.truetransact.uicomponent.CRadioButton rdoDenominationNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoDenominationYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExcludePenal;
    private com.see.truetransact.uicomponent.CRadioButton rdoExcludePenalIntFromReportsNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoExcludePenalIntFromReportsYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIB_OnHoliday;
    private com.see.truetransact.uicomponent.CRadioButton rdoIB_OnHoliday_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIB_OnHoliday_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultiShare;
    private com.see.truetransact.uicomponent.CRadioButton rdoMultiShareNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoMultiShareYes;
    private com.see.truetransact.uicomponent.CRadioButton rdoServiceTaxNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoServiceTaxYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoServiceTx;
    private com.see.truetransact.uicomponent.CButtonGroup rdoToken;
    private com.see.truetransact.uicomponent.CRadioButton rdoTokenNoAllowNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoTokenNoAllowYes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private com.see.truetransact.uicomponent.CSeparator sptPwdConfig;
    private com.see.truetransact.uicomponent.CTabbedPane tabConfig;
    private javax.swing.JToolBar tbrConfig;
    private com.see.truetransact.uicomponent.CDateField tdtEffectiveFrom;
    private com.see.truetransact.uicomponent.CDateField tdtFromDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastFinancialYearEnd;
    private com.see.truetransact.uicomponent.CDateField tdtToDate;
    private com.see.truetransact.uicomponent.CDateField tdtYearEndProcessDate;
    private com.see.truetransact.uicomponent.CTextField txtAcceptChargesHead;
    private com.see.truetransact.uicomponent.CTextField txtAmcAlertTime;
    private com.see.truetransact.uicomponent.CTextField txtAppSuspenseAcHd;
    private com.see.truetransact.uicomponent.CTextField txtAttempts;
    private com.see.truetransact.uicomponent.CTextField txtCashActHead;
    private com.see.truetransact.uicomponent.CTextField txtDays;
    private com.see.truetransact.uicomponent.CTextField txtExecChargesHead;
    private com.see.truetransact.uicomponent.CTextField txtFailChargesHead;
    private com.see.truetransact.uicomponent.CTextField txtGahanPeriod;
    private com.see.truetransact.uicomponent.CTextField txtIBRActHead;
    private com.see.truetransact.uicomponent.CTextField txtLogOutTime;
    private com.see.truetransact.uicomponent.CTextField txtMaxLength;
    private com.see.truetransact.uicomponent.CTextField txtMemFee;
    private com.see.truetransact.uicomponent.CTextField txtMinLength;
    private com.see.truetransact.uicomponent.CTextField txtMinorAge;
    private com.see.truetransact.uicomponent.CTextField txtNmfAcHead;
    private com.see.truetransact.uicomponent.CTextField txtNo;
    private com.see.truetransact.uicomponent.CTextField txtPanDetails;
    private com.see.truetransact.uicomponent.CTextField txtPendingTxnAllowedDays;
    private com.see.truetransact.uicomponent.CTextField txtPwds;
    private com.see.truetransact.uicomponent.CTextField txtRTGS_GL;
    private com.see.truetransact.uicomponent.CTextField txtRemitChargesHead;
    private com.see.truetransact.uicomponent.CTextField txtRetirementAge;
    private com.see.truetransact.uicomponent.CTextField txtSIChargesHead;
    private com.see.truetransact.uicomponent.CTextField txtSalarySuspense;
    private com.see.truetransact.uicomponent.CTextField txtSeniorCitizenAge;
    private com.see.truetransact.uicomponent.CTextField txtServicePeriod;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    private com.see.truetransact.uicomponent.CTextField txtServiceTaxHead;
    private com.see.truetransact.uicomponent.CTextField txtSplChar;
    private com.see.truetransact.uicomponent.CTextField txtUpperCase;
    // End of variables declaration//GEN-END:variables

}

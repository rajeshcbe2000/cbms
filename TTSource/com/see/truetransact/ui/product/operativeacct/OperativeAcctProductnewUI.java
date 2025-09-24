/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * OperativeAcctProductnewUI.java
 *
 * Created on August 5, 2003, 2:29 PM
 */

package com.see.truetransact.ui.product.operativeacct;

import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.TableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.AcctStatusConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CButtonGroup;
import javax.swing.border.TitledBorder;
import java.util.List;
import java.util.HashMap;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import javax.swing.JFrame;
import javax.swing.UIManager;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.MandatoryDBCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CComboBox;

/**
 *
 * @author balachandar
 *
 * @modified Pinky
 * @modified Rahul
 * @modified K.R.Jayakrishnan
 * @modified : Sunil
 *      Added Edit Locking - 08-07-2005
 */
public class OperativeAcctProductnewUI extends CInternalFrame implements Observer, UIMandatoryField {
    
    //    final OperativeAcctProductnewRB resourceBundle = new OperativeAcctProductnewRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.operativeacct.OperativeAcctProductnewRB", ProxyParameters.LANGUAGE);
    
    private final OperativeAcctProductnewMRB objMandatoryRB = new OperativeAcctProductnewMRB();
    private OperativeAcctProductOB observable;
    private int viewType = 0;
    private HashMap mandatoryMap;
    private TableModel tmlIntRate;
    private boolean _intRateNew = false;
    private boolean isFilled = false;
    
    final private int ACCTHD = 1;
    final private int ACCTCLOSINGCHRG = 2;
    final private int MISCSERVCHRG = 3;
    final private int STATCHRG = 4;
    final private int FREEWDCHRG = 5;
    final private int ACCTDEBITINT = 6;
    final private int ACCTCRINT = 7;
    final private int CLEARINGINTACCTHD = 8;
    final private int CHKBKISSUECHRG = 9;
    final private int STOPPAYMENTCHRG = 10;
    final private int OUTWARDCHKRETCHRG = 11;
    final private int INWARDCHKRETCHRG = 12;
    final private int ACCTOPENCHRG = 13;
    final private int EXCESSFREEWDCHRG = 14;
    final private int TAXGL = 15;
    final private int NONMAINMINBALCHRG = 16;
    final private int INOPERATIVE = 17;
    final private int FOLIOCHRG = 18;
    final private int PREMATURECLOSURECHRG = 19;
    final private int INOPCHRG = 20;
    final private int IRACCTHD = 21;
    final private int ATMID = 21;
    
    final private int DEBIT_WITHDRAWAL_CHRG = 22; // Added by nithya on 17-03-2016 for 0004021
    final private int SPECIFIC_DATE = 14;
    
    final private int EDIT = 100;
    final private int DELETE = 200;
    final private int AUTHORIZE = 300;
    final private int SAVEAS = 400;
    final private int VIEW = 500;
    final private int COPY = 600;
    
    final private String ACHDID = "AC_HD_ID";
    final private String ACHDDESC= "AC_HD_DESC";
    private String ACCT_TYPE = "ACCT_TYPE";
    private String BALANCETYPE = "BALANCETYPE";
    Date curDate = null;
    
    /** Creates new form OperativeAcctProduct */
    public OperativeAcctProductnewUI() {
        initComponents();
        initStartup();
        curDate = ClientUtil.getCurrentDate();
    }
    
    private void initStartup() {
        setFieldNames();
        internationalize();
        setHelpMessage();
        setMaxLengths();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        
        (new MandatoryDBCheck()).setComponentInit(getClass().getName(), this);
        
        ClientUtil.clearAll(panOperativeProduct);
        ClientUtil.enableDisable(this, false);
/*
 * To Make the Folder buttons and the Text fields
 * associated with 'em enable or Disable...
 */
        setFolderButtonEnableDisable(false);
        setFolderTxtEnableDisable(false);
        setButtonEnableDisable();
        setFieldInvisible();
        tabOperativeAcctProduct.resetVisits();
        txtLastAccNum.setEditable(false);
        txtLastAccNum.setEnabled(false);
        ClientUtil.enableDisable(panTaxIntApplNRO,false); 
//        panOtherAcctHead.setT
//        tabOperativeAcctProduct.setTitleAt(6, "Other Account Head");
//        tabOperativeAcctProduct.add(panOtherAcctHead);
      
        /** kerala co -operative purpose hided
         *please dont delete  it may use  in future */
//        lblMinCrIntRate.setVisible(false);
//        panMinCrIntRate.setVisible(false);
//        lblMaxCrIntRate.setVisible(false);
//        panMaxDrIntRate1.setVisible(false);
//        lblStMonIntCalc.setVisible(false);
//        txtStMonIntCalc.setVisible(false);
//        cboStMonIntCalc.setVisible(false);
//        lblIntCalcEndMon.setVisible(false);
//        txtIntCalcEndMon.setVisible(false);
//        cboIntCalcEndMon.setVisible(false);
//        lblMinDrIntRate.setVisible(false);
//        panMinDrIntRate.setVisible(false);
//        lblMaxDebitIntRate.setVisible(false);
//        panMaxDrIntRate.setVisible(false);
//        lblStartInterCalc.setVisible(false);
//        txtStartInterCalc.setVisible(false);
//        cboStartInterCalc.setVisible(false);
//        lblEndInterCalc.setVisible(false);
//        txtEndInterCalc.setVisible(false);
//        cboEndInterCalc.setVisible(false);
//        lblStartProdCalc.setVisible(false);
//        txtStartProdCalc.setVisible(false);
//        cboStartProdCalc.setVisible(false);
//        lblEndProdCalc.setVisible(false);
//        txtEndProdCalc.setVisible(false);
//        cboEndProdCalc.setVisible(false);
//        lblChkRetChrOutward.setVisible(false);
//        txtChkRetChrOutward.setVisible(false);
//        lblOutwardChkRetChrg.setVisible(false);
//        txtOutwardChkRetChrg.setVisible(false);
//        btnOutwardChkRetChrg.setVisible(false);
//        lblInwardChkRetChrg.setVisible(false);
//        txtInwardChkRetChrg.setVisible(false);
//        btnInwardChkRetChrg.setVisible(false);
//        lblIssue.setVisible(false);
//        panIssueToken.setVisible(false);
//        lblCrIntUnclear.setVisible(false);
//        panIntUnClearBal.setVisible(false);
//        lblDebitInt.setVisible(false);
//        panIntClearing.setVisible(false);
        disDesc();
        
    }
    
/* Auto Generated Method - setMandatoryHashMap()
 This method list out all the Input Fields available in the UI.
 It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductID", new Boolean(true));
        mandatoryMap.put("txtDesc", new Boolean(true));
        mandatoryMap.put("cboBehaves", new Boolean(true));
        mandatoryMap.put("txtMinBalChkbk", new Boolean(true));
        mandatoryMap.put("txtMinBalwchk", new Boolean(true));
        mandatoryMap.put("rdoChkAllowed_Yes", new Boolean(true));
        mandatoryMap.put("txtNoNominees", new Boolean(true));
        mandatoryMap.put("rdoAcctOpenAppr_Yes", new Boolean(true));
        mandatoryMap.put("rdoNomineeReq_Yes", new Boolean(true));
        mandatoryMap.put("rdoIntroReq_Yes", new Boolean(true));
        mandatoryMap.put("txtMainTreatNewAcctClosure", new Boolean(true));
        mandatoryMap.put("txtMinTreatasDormant", new Boolean(true));
        mandatoryMap.put("txtMinTreatasNew", new Boolean(true));
        mandatoryMap.put("txtMinTreatasInOp", new Boolean(true));
        mandatoryMap.put("cboMinTreatasDormant", new Boolean(true));
        mandatoryMap.put("cboMinTreatInOp", new Boolean(true));
        mandatoryMap.put("cboMinTreatasNew", new Boolean(true));
        mandatoryMap.put("cboMinTreatNewClosure", new Boolean(true));
        mandatoryMap.put("cboStatFreq", new Boolean(true));
        mandatoryMap.put("txtAcctHd", new Boolean(true));
        mandatoryMap.put("cboProdCurrency", new Boolean(true));
        mandatoryMap.put("txtNoFreeChkLeaves", new Boolean(true));
        mandatoryMap.put("txtRateTaxNRO", new Boolean(true));
        mandatoryMap.put("txtMaxAmtWDSlip", new Boolean(true));
        mandatoryMap.put("rdoAllowWD_Yes", new Boolean(true));
        mandatoryMap.put("rdoIntClearing_Yes", new Boolean(true));
        mandatoryMap.put("rdoStaffAcct_Yes", new Boolean(true));
        mandatoryMap.put("rdoCollectInt_Yes", new Boolean(true));
        mandatoryMap.put("rdoLimitDefAllow_Yes", new Boolean(true));
        mandatoryMap.put("txtNoFreeWD", new Boolean(true));
        mandatoryMap.put("rdoIntUnClearBal_Yes", new Boolean(true));
        mandatoryMap.put("rdoTempODAllow_Yes", new Boolean(true));
        mandatoryMap.put("rdoIssueToken_Yes", new Boolean(true));
        mandatoryMap.put("rdoTaxIntApplNRO_Yes", new Boolean(true));
        mandatoryMap.put("tdtFreeWDStFrom", new Boolean(true));
        mandatoryMap.put("tdtFreeChkLeaveStFrom", new Boolean(true));
        mandatoryMap.put("txtFreeWDPd", new Boolean(true));
        mandatoryMap.put("cboFreeWDPd", new Boolean(true));
        mandatoryMap.put("txtFreeChkPD", new Boolean(true));
        mandatoryMap.put("cboFreeChkPd", new Boolean(true));
        mandatoryMap.put("cboFreeWDStFrom", new Boolean(true));
        mandatoryMap.put("cboFreeChkLeaveStFrom", new Boolean(true));
        mandatoryMap.put("txtMinDebitIntRate", new Boolean(true));
        mandatoryMap.put("txtMaxDebitIntRate", new Boolean(true));
        mandatoryMap.put("txtApplDebitIntRate", new Boolean(true));
        mandatoryMap.put("txtMinDebitIntAmt", new Boolean(true));
        mandatoryMap.put("txtMaxDebitIntAmt", new Boolean(true));
        mandatoryMap.put("cboDebitIntCalcFreq", new Boolean(true));
        mandatoryMap.put("cboDebitIntApplFreq", new Boolean(true));
        mandatoryMap.put("rdoDebitIntChrg_Yes2", new Boolean(true));
        mandatoryMap.put("txtStartInterCalc", new Boolean(true));
        mandatoryMap.put("cboStartInterCalc", new Boolean(true));
        mandatoryMap.put("txtEndInterCalc", new Boolean(true));
        mandatoryMap.put("cboEndInterCalc", new Boolean(true));
        mandatoryMap.put("rdoDebitCompReq_Yes", new Boolean(true));
        mandatoryMap.put("cboDebitCompIntCalcFreq", new Boolean(true));
        mandatoryMap.put("cboDebitProductRoundOff", new Boolean(true));
        mandatoryMap.put("cboDebitIntRoundOff", new Boolean(true));
        mandatoryMap.put("tdtLastIntCalcDate", new Boolean(true));
        mandatoryMap.put("tdtLastIntApplDate", new Boolean(true));
        mandatoryMap.put("cboProdFreq", new Boolean(true));
        mandatoryMap.put("txtPenalIntDebitBalAcct", new Boolean(true));
        mandatoryMap.put("txtPenalIntChrgStart", new Boolean(true));
        mandatoryMap.put("txtStartProdCalc", new Boolean(true));
        mandatoryMap.put("cboStartProdCalc", new Boolean(true));
        mandatoryMap.put("txtEndProdCalc", new Boolean(true));
        mandatoryMap.put("cboEndProdCalc", new Boolean(true));
        mandatoryMap.put("txtStDayProdCalcSBCrInt", new Boolean(true));
        mandatoryMap.put("txtEndDayProdCalcSBCrInt", new Boolean(true));
        mandatoryMap.put("cboCrIntCalcFreq", new Boolean(true));
        mandatoryMap.put("cboCrIntApplFreq", new Boolean(true));
        mandatoryMap.put("cboStMonIntCalc", new Boolean(true));
        mandatoryMap.put("cboIntCalcEndMon", new Boolean(true));
        mandatoryMap.put("tdtLastIntCalcDateCR", new Boolean(true));
        mandatoryMap.put("tdtLastIntApplDateCr", new Boolean(true));
        mandatoryMap.put("rdoCreditComp_Yes", new Boolean(true));
        mandatoryMap.put("cboCreditCompIntCalcFreq", new Boolean(true));
        mandatoryMap.put("cboCreditProductRoundOff", new Boolean(true));
        mandatoryMap.put("cboCreditIntRoundOff", new Boolean(true));
        mandatoryMap.put("cboCalcCriteria", new Boolean(true));
        mandatoryMap.put("cboProdFreqCr", new Boolean(true));
        mandatoryMap.put("rdoCrIntGiven_Yes", new Boolean(true));
        mandatoryMap.put("txtMinCrIntRate", new Boolean(true));
        mandatoryMap.put("txtMaxCrIntRate", new Boolean(true));
        mandatoryMap.put("txtApplCrIntRate", new Boolean(true));
        mandatoryMap.put("txtMinCrIntAmt", new Boolean(true));
        mandatoryMap.put("txtMaxCrIntAmt", new Boolean(true));
        mandatoryMap.put("cboStDayProdCalcSBCrInt", new Boolean(true));
        mandatoryMap.put("cboEndDayProdCalcSBCrInt", new Boolean(true));
        mandatoryMap.put("txtStMonIntCalc", new Boolean(true));
        mandatoryMap.put("txtIntCalcEndMon", new Boolean(true));
        mandatoryMap.put("txtlInOpAcChrg", new Boolean(true));
        mandatoryMap.put("cboInOpAcChrgPd", new Boolean(true));
        mandatoryMap.put("txtChrgPreClosure", new Boolean(true));
        mandatoryMap.put("txtAcClosingChrg", new Boolean(true));
        mandatoryMap.put("txtChrgMiscServChrg", new Boolean(true));
        mandatoryMap.put("rdoNonMainMinBalChrg_Yes", new Boolean(true));
        mandatoryMap.put("txtMinBalAmt", new Boolean(true));
        mandatoryMap.put("cboMinBalAmt", new Boolean(true));
        mandatoryMap.put("rdoStatCharges_Yes", new Boolean(true));
        mandatoryMap.put("txtStatChargesChr", new Boolean(true));
        mandatoryMap.put("cboStatChargesChr", new Boolean(true));
        mandatoryMap.put("rdoChkIssuedChrgCh_Yes", new Boolean(true));
        mandatoryMap.put("txtChkBkIssueChrgPL", new Boolean(true));
        mandatoryMap.put("rdoStopPaymentChrg_Yes", new Boolean(true));
        mandatoryMap.put("txtStopPayChrg", new Boolean(true));
        mandatoryMap.put("rdoFolioChargeAppl_Yes", new Boolean(true));
        mandatoryMap.put("txtRatePerFolio", new Boolean(true));
        mandatoryMap.put("txtNoEntryPerFolio", new Boolean(true));
        mandatoryMap.put("rdoFolioToChargeOn_Credit", new Boolean(true));
        mandatoryMap.put("cboFolioChrgApplFreq", new Boolean(true));
        mandatoryMap.put("cboToCollectFolioChrg", new Boolean(true));
        mandatoryMap.put("rdoToChargeOnApplFreq_Manual", new Boolean(true));
        mandatoryMap.put("cboIncompFolioROffFreq", new Boolean(true));
        mandatoryMap.put("txtChkRetChrOutward", new Boolean(true));
        mandatoryMap.put("txtChkRetChrgIn", new Boolean(true));
        mandatoryMap.put("txtAcctOpenCharges", new Boolean(true));
        mandatoryMap.put("txtExcessFreeWDChrgPT", new Boolean(true));
        mandatoryMap.put("rdoFlexiHappen_SB", new Boolean(true));
        mandatoryMap.put("rdoLinkFlexiAcct_Yes", new Boolean(true));
        mandatoryMap.put("txtMinBal1FlexiDep", new Boolean(true));
        mandatoryMap.put("txtMinBal2FlexiDep", new Boolean(true));
        mandatoryMap.put("cboFlexiTD", new Boolean(true));
        mandatoryMap.put("rdoATMIssued_Yes", new Boolean(true));
        mandatoryMap.put("rdoABBAllowed_Yes", new Boolean(true));
        mandatoryMap.put("rdoMobBankClient_Yes", new Boolean(true));
        mandatoryMap.put("rdoIVRSProvided_Yes", new Boolean(true));
        mandatoryMap.put("rdoDebitCdIssued_Yes", new Boolean(true));
        mandatoryMap.put("rdoCreditCdIssued_Yes", new Boolean(true));
        mandatoryMap.put("txtMinATMBal", new Boolean(true));
        mandatoryMap.put("txtMinBalCreditCd", new Boolean(true));
        mandatoryMap.put("txtMinBalDebitCards", new Boolean(true));
        mandatoryMap.put("txtMinBalIVRS", new Boolean(true));
        mandatoryMap.put("txtMinMobBank", new Boolean(true));
        mandatoryMap.put("txtMinBalABB", new Boolean(true));
        mandatoryMap.put("txtInOpChrg", new Boolean(true));
        mandatoryMap.put("txtPrematureClosureChrg", new Boolean(true));
        mandatoryMap.put("txtAcctClosingChrg", new Boolean(true));
        mandatoryMap.put("txtMiscServChrg", new Boolean(true));
        mandatoryMap.put("txtStatChrg", new Boolean(true));
        mandatoryMap.put("txtFreeWDChrg", new Boolean(true));
        mandatoryMap.put("txtAcctDebitInt", new Boolean(true));
        mandatoryMap.put("txtAcctCrInt", new Boolean(true));
        mandatoryMap.put("txtChkBkIssueChrg", new Boolean(true));
        mandatoryMap.put("txtClearingIntAcctHd", new Boolean(true));
        mandatoryMap.put("txtStopPaymentChrg", new Boolean(true));
        mandatoryMap.put("txtOutwardChkRetChrg", new Boolean(true));
        mandatoryMap.put("txtInwardChkRetChrg", new Boolean(true));
        mandatoryMap.put("txtAcctOpenChrg", new Boolean(true));
        mandatoryMap.put("txtExcessFreeWDChrg", new Boolean(true));
        mandatoryMap.put("txtTaxGL", new Boolean(true));
        mandatoryMap.put("txtNonMainMinBalChrg", new Boolean(true));
        mandatoryMap.put("txtInOperative", new Boolean(true));
        mandatoryMap.put("txtFolioChrg", new Boolean(true));
        mandatoryMap.put("cboIntCategory", new Boolean(true));
        mandatoryMap.put("tdtRateEffDate", new Boolean(true));
        mandatoryMap.put("txtRateIntRate", new Boolean(true));
        mandatoryMap.put("txtNumPatternFollowedPrefix", new Boolean(true));
        mandatoryMap.put("txtNumPatternFollowedPSuffix", new Boolean(true));
        mandatoryMap.put("txtLastAccNum", new Boolean(true));
        mandatoryMap.put("rdoAcc_Reg", new Boolean(true));
        
    }
    
/* Auto Generated Method - getMandatoryHashMap()
 Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void disDesc() {
        txtInOpChrgDesc.setEnabled(false);
        txtPrematureClosureChrgDesc.setEnabled(false);
        txtAcctClosingChrgDesc.setEnabled(false);
        txtMiscServChrgDesc.setEnabled(false);
        txtStatChrgDesc.setEnabled(false);
        txtFreeWDChrgDesc.setEnabled(false);
        txtAcctDebitIntDesc.setEnabled(false);
        txtAcctCrIntDesc.setEnabled(false);
        txtClearingIntAcctHdDesc.setEnabled(false);
        txtChkBkIssueChrgDesc.setEnabled(false);
        txtStopPaymentChrgDesc.setEnabled(false);
        txtOutwardChkRetChrgDesc.setEnabled(false);
        txtInwardChkRetChrgDesc.setEnabled(false);
        txtAcctOpenChrgDesc.setEnabled(false);
        txtExcessFreeWDChrgDesc.setEnabled(false);
        txtTaxGLDesc.setEnabled(false);
        txtNonMainMinBalChrgDesc.setEnabled(false);
        txtInOperativeDesc.setEnabled(false);
        txtFolioChrgDesc.setEnabled(false);
    }


    private void setMaxLengths() {
        txtAcctHd.setMaxLength(16);
        txtProductID.setMaxLength(8);
        txtDesc.setMaxLength(128);
        
        //        txtMinBalwchk.setMaxLength(10); //16 db size
        txtMinBalwchk.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBalChkbk.setMaxLength(10); // 16
        txtMinBalChkbk.setValidation(new CurrencyValidation(14,2));
        
        txtNoNominees.setMaxLength(10); // 10
        txtNoNominees.setValidation(new NumericValidation());
        
        txtMinTreatasNew.setMaxLength(5); // 10
        txtMinTreatasNew.setValidation(new NumericValidation());
        
        txtMinTreatasDormant.setMaxLength(5); //10
        txtMinTreatasDormant.setValidation(new NumericValidation());
        
        txtMinTreatasInOp.setMaxLength(5); // 10
        txtMinTreatasInOp.setValidation(new NumericValidation());
        
        txtMainTreatNewAcctClosure.setMaxLength(5); // 10
        txtMainTreatNewAcctClosure.setValidation(new NumericValidation());
        
        txtNoFreeWD.setMaxLength(10); // 10
        txtNoFreeWD.setValidation(new NumericValidation());
        
        txtFreeWDPd.setMaxLength(5); // 10
        txtFreeWDPd.setValidation(new NumericValidation());
        
        txtNoFreeChkLeaves.setMaxLength(10); // 10
        txtNoFreeChkLeaves.setValidation(new NumericValidation());
        
        txtFreeChkPD.setMaxLength(5); // 10
        txtFreeChkPD.setValidation(new NumericValidation());
        
        //        txtRateTaxNRO.setMaxLength(6);
        txtRateTaxNRO.setValidation(new PercentageValidation());
        
        //        txtMaxAmtWDSlip.setMaxLength(16);
        txtMaxAmtWDSlip.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinDebitIntRate.setMaxLength(6); //16
        txtMinDebitIntRate.setValidation(new PercentageValidation());
        
        //        txtMaxDebitIntRate.setMaxLength(6); //16
        txtMaxDebitIntRate.setValidation(new PercentageValidation());
        
        //        txtApplDebitIntRate.setMaxLength(6); //16
        txtApplDebitIntRate.setValidation(new PercentageValidation());
        
        //        txtMinDebitIntAmt.setMaxLength(16);
        txtMinDebitIntAmt.setValidation(new CurrencyValidation(14,2));
        
        //        txtMaxDebitIntAmt.setMaxLength(16);
        txtMaxDebitIntAmt.setValidation(new CurrencyValidation(14,2));
        
        //        txtPenalIntDebitBalAcct.setMaxLength(16);
        txtPenalIntDebitBalAcct.setValidation(new NumericValidation(14,2));
        
        txtPenalIntChrgStart.setMaxLength(4); //10
        txtPenalIntChrgStart.setValidation(new NumericValidation());
        
        //        txtMinCrIntRate.setMaxLength(6); //16
        txtMinCrIntRate.setValidation(new NumericValidation(4,2));
        
        //        txtMaxCrIntRate.setMaxLength(6); //16
        txtMaxCrIntRate.setValidation(new NumericValidation(4,2));
        
        txtApplCrIntRate.setMaxLength(6); //16
        txtApplCrIntRate.setValidation(new NumericValidation());
        
        //        txtMinCrIntAmt.setMaxLength(16);
        txtMinCrIntAmt.setValidation(new CurrencyValidation(14,2));
        
        //        txtMaxCrIntAmt.setMaxLength(16);
        txtMaxCrIntAmt.setValidation(new CurrencyValidation(14,2));
        
        txtStDayProdCalcSBCrInt.setMaxLength(2);
        txtStDayProdCalcSBCrInt.setValidation(new NumericValidation());
        
        txtEndDayProdCalcSBCrInt.setMaxLength(2);
        txtEndDayProdCalcSBCrInt.setValidation(new NumericValidation());
        
        
        txtStartInterCalc.setMaxLength(2);
        txtStartInterCalc.setValidation(new NumericValidation());
        
        txtEndInterCalc.setMaxLength(2);
        txtEndInterCalc.setValidation(new NumericValidation());
        
        txtStartProdCalc.setMaxLength(2);
        txtStartProdCalc.setValidation(new NumericValidation());
        
        txtEndProdCalc.setMaxLength(2);
        txtEndProdCalc.setValidation(new NumericValidation());
        
        // txtAddIntStaff.setMaxLength(6); //16
        // txtAddIntStaff.setValidation(new NumericValidation());
        
        txtStMonIntCalc.setMaxLength(2);
        txtStMonIntCalc.setValidation(new NumericValidation());
        
        txtIntCalcEndMon.setMaxLength(2);
        txtIntCalcEndMon.setValidation(new NumericValidation());
        
        //        txtlInOpAcChrg.setMaxLength(16);
        txtlInOpAcChrg.setValidation(new CurrencyValidation(14,2));
        
        //        txtChrgPreClosure.setMaxLength(16);
        txtChrgPreClosure.setValidation(new CurrencyValidation(14,2));
        
        //        txtAcClosingChrg.setMaxLength(16);
        txtAcClosingChrg.setValidation(new CurrencyValidation(14,2));
        
        //        txtChrgMiscServChrg.setMaxLength(16);
        txtChrgMiscServChrg.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBalAmt.setMaxLength(16);
        txtMinBalAmt.setValidation(new CurrencyValidation(14,2));
        
        //        txtStatChargesChr.setMaxLength(16);
        txtStatChargesChr.setValidation(new CurrencyValidation(14,2));
        
        //        txtChkBkIssueChrgPL.setMaxLength(16);
        txtChkBkIssueChrgPL.setValidation(new CurrencyValidation(14,2));
        
        //        txtStopPayChrg.setMaxLength(16);
        txtStopPayChrg.setValidation(new CurrencyValidation(14,2));
        
        //        txtChkRetChrOutward.setMaxLength(16);
        txtChkRetChrOutward.setValidation(new CurrencyValidation(14,2));
        
        //        txtAcctOpenCharges.setMaxLength(16);
        txtAcctOpenCharges.setValidation(new CurrencyValidation(14,2));
        
        txtNoEntryPerFolio.setMaxLength(10); // 10
        txtNoEntryPerFolio.setValidation(new NumericValidation());
        
        //        txtRatePerFolio.setMaxLength(16);
        txtRatePerFolio.setValidation(new CurrencyValidation(14,2));
        
        //        txtExcessFreeWDChrgPT.setMaxLength(16);
        txtExcessFreeWDChrgPT.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBal1FlexiDep.setMaxLength(16);
        txtMinBal1FlexiDep.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBal2FlexiDep.setMaxLength(16);
        txtMinBal2FlexiDep.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinATMBal.setMaxLength(16);
        txtMinATMBal.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBalCreditCd.setMaxLength(16);
        txtMinBalCreditCd.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBalDebitCards.setMaxLength(16);
        txtMinBalDebitCards.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBalIVRS.setMaxLength(16);
        txtMinBalIVRS.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinMobBank.setMaxLength(16);
        txtMinMobBank.setValidation(new CurrencyValidation(14,2));
        
        //        txtMinBalABB.setMaxLength(16);
        txtMinBalABB.setValidation(new CurrencyValidation(14,2));
        txtIMPSLimit.setValidation(new CurrencyValidation(14,2));
        
        txtMinbalForInt.setValidation(new CurrencyValidation(14,2));
        
        txtInOpChrg.setMaxLength(16);
        txtPrematureClosureChrg.setMaxLength(16);
        txtAcctClosingChrg.setMaxLength(16);
        txtMiscServChrg.setMaxLength(16);
        txtStatChrg.setMaxLength(16);
        txtFreeWDChrg.setMaxLength(16);
        txtAcctDebitInt.setMaxLength(16);
        txtAcctCrInt.setMaxLength(16);
        txtClearingIntAcctHd.setMaxLength(16);
        txtChkBkIssueChrg.setMaxLength(16);
        txtStopPaymentChrg.setMaxLength(16);
        txtOutwardChkRetChrg.setMaxLength(16);
        txtInwardChkRetChrg.setMaxLength(16);
        txtAcctOpenChrg.setMaxLength(16);
        txtExcessFreeWDChrg.setMaxLength(16);
        txtTaxGL.setMaxLength(16);
        txtNonMainMinBalChrg.setMaxLength(16);
        txtInOperative.setMaxLength(16);
        txtFolioChrg.setMaxLength(16);
        txtATMGL.setMaxLength(16);
        
        //        txtChkRetChrgIn.setMaxLength(16);
        txtChkRetChrgIn.setValidation(new CurrencyValidation(14,2));
        txtRateTaxNRO.setValidation(new PercentageValidation());
        txtNumPatternFollowedPrefix.setMaxLength(8);
        txtNumPatternFollowedSuffix.setMaxLength(10);
        txtNumPatternFollowedSuffix.setValidation(new NumericValidation());
        txtLastAccNum.setMaxLength(12);
        txtLastAccNum.setValidation(new NumericValidation());
        
        txtDebitChargeTypeRate.setValidation(new CurrencyValidation(14,2)); // Added by nithya on 17-03-2016 for 0004021
        txtFolioRestrictionPriod.setValidation(new NumericValidation());
        txtFolioRestrictionPriod.setMaxLength(2);
    }
    
    private void setObservable() {
        try {
            observable = new OperativeAcctProductOB();
            observable.addObserver(this);
            //update(observable, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void initComponentData() {
        cboBehaves.setModel(observable.getCbmBehaves());
        cboProdCurrency.setModel(observable.getCbmProdCurrency());
        cboCalcCriteria.setModel(observable.getCbmCalcCrit());
        cboCrIntApplFreq.setModel(observable.getCbmCrApplFreq());
        cboCrIntCalcFreq.setModel(observable.getCbmCrCalcFreq());
        cboCreditCompIntCalcFreq.setModel(observable.getCbmCrCompFreq());
        cboCreditIntRoundOff.setModel(observable.getCbmCrIntRound());
        cboCreditProductRoundOff.setModel(observable.getCbmCrProdRound());
        cboDebitCompIntCalcFreq.setModel(observable.getCbmDrCompFreq());
        cboFlexiTD.setModel(observable.getCbmFlexiTD());
        cboDebitIntApplFreq.setModel(observable.getCbmDrApplFreq());
        cboDebitIntCalcFreq.setModel(observable.getCbmDrCalcFreq());
        cboDebitIntRoundOff.setModel(observable.getCbmDrIntRound());
        cboDebitProductRoundOff.setModel(observable.getCbmDrProdRound());
        cboFolioChrgApplFreq.setModel(observable.getCbmFolioChrgApplFreq());
        cboProdFreq.setModel(observable.getCbmProdFreq());
        cboFreeChkPd.setModel(observable.getCbmChqPeriod());
        cboFreeWDPd.setModel(observable.getCbmWDPeriod());
        cboIncompFolioROffFreq.setModel(observable.getCbmIncFolioROffFreq());
        cboMinBalAmt.setModel(observable.getCbmNonMain());
        cboMinTreatInOp.setModel(observable.getCbmPeriodInop());
        cboMinTreatNewClosure.setModel(observable.getCbmPeriodCloser());
        cboMinTreatasDormant.setModel(observable.getCbmPeriodDormant());
        cboMinTreatasNew.setModel(observable.getCbmPeriodNew());
        cboProdFreqCr.setModel(observable.getCbmCrProdFreq());
        cboIntCalcEndMon.setModel(observable.getCbmIntCalcEndMon());
        cboStMonIntCalc.setModel(observable.getCbmStMonIntCalc());
        cboStatFreq.setModel(observable.getCbmStatFreq());
        cboToCollectFolioChrg.setModel(observable.getCbmCollFolioChrg());
        cboStatChargesChr.setModel(observable.getCbmAmtStatChrg());
        cboInOpAcChrgPd.setModel(observable.getCbmInOpAcChrgPd());
        
        cboFreeWDStFrom.setModel(observable.getCbmFreeWDStFrom());
        cboFreeChkLeaveStFrom.setModel(observable.getCbmFreeChkLeaveStFrom());
        cboStDayProdCalcSBCrInt.setModel(observable.getCbmStDayProdCalcSBCrInt());
        cboEndDayProdCalcSBCrInt.setModel(observable.getCbmEndDayProdCalcSBCrInt());
        
        cboStartInterCalc.setModel(observable.getCbmStartInterCalc());
        cboEndInterCalc.setModel(observable.getCbmEndInterCalc());
        cboStartProdCalc.setModel(observable.getCbmStartProdCalc());
        cboEndProdCalc.setModel(observable.getCbmEndProdCalc());
        
        cboDebitChargeType.setModel(observable.getCbmDebitChargeType()); // Added by nithya on 17-03-2016 for 0004021
        cboFolioChargedBefore.setModel(observable.getCbmFolioChargedbefore());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoAcctOpenAppr = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoChkAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNomineeReq = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIntroReq = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAllowWD = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStaffAcct = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCollectInt = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLimitDefAllow = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIntUnClearBal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTempODAllow = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExtraIntAppl = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIssueToken = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTaxIntApplNRO = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDebitIntChrg = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDebitCompReq = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCreditComp = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCrIntGiven = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoLinkFlexiAcct = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoFlexiHappen = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoATMIssued = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCreditCdIssued = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDebitCdIssued = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoABBAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMobBankClient = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIVRSProvided = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNonMainMinBalCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNonMainMinBalChrg = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatCharges = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoChkIssuedChrg = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStopPaymentChrg = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoFolioChargeAppl = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoToChargeOnApplFreq = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoFolioToChargeOn = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIntClearing = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatChargesCh = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoChkIssuedChrgCh = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoTypeAcc = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoFolioChargeGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrOperativeAcctProduct = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panOperativeProduct = new com.see.truetransact.uicomponent.CPanel();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        tabOperativeAcctProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panAccount = new com.see.truetransact.uicomponent.CPanel();
        panAcctCol1 = new com.see.truetransact.uicomponent.CPanel();
        panMinPerd1 = new com.see.truetransact.uicomponent.CPanel();
        lblProductID = new com.see.truetransact.uicomponent.CLabel();
        txtProductID = new com.see.truetransact.uicomponent.CTextField();
        lblDesc = new com.see.truetransact.uicomponent.CLabel();
        txtDesc = new com.see.truetransact.uicomponent.CTextField();
        lblAcctHd = new com.see.truetransact.uicomponent.CLabel();
        lblBehaves = new com.see.truetransact.uicomponent.CLabel();
        lblProdCurrency = new com.see.truetransact.uicomponent.CLabel();
        panAcctHd = new com.see.truetransact.uicomponent.CPanel();
        txtAcctHd = new com.see.truetransact.uicomponent.CTextField();
        btnAcctHd = new com.see.truetransact.uicomponent.CButton();
        cboBehaves = new com.see.truetransact.uicomponent.CComboBox();
        cboProdCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblChequeAllowed = new com.see.truetransact.uicomponent.CLabel();
        lblMinBalChqbk = new com.see.truetransact.uicomponent.CLabel();
        txtMinBalChkbk = new com.see.truetransact.uicomponent.CTextField();
        lblMinBalwochk = new com.see.truetransact.uicomponent.CLabel();
        txtMinBalwchk = new com.see.truetransact.uicomponent.CTextField();
        lblIntroReq = new com.see.truetransact.uicomponent.CLabel();
        lblNomineereq = new com.see.truetransact.uicomponent.CLabel();
        lblNoNominee = new com.see.truetransact.uicomponent.CLabel();
        panChkAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoChkAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoChkAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtNoNominees = new com.see.truetransact.uicomponent.CTextField();
        panNomineeReq = new com.see.truetransact.uicomponent.CPanel();
        rdoNomineeReq_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNomineeReq_No = new com.see.truetransact.uicomponent.CRadioButton();
        panIntroReq = new com.see.truetransact.uicomponent.CPanel();
        rdoIntroReq_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIntroReq_No = new com.see.truetransact.uicomponent.CRadioButton();
        panMinPerd = new com.see.truetransact.uicomponent.CPanel();
        txtMainTreatNewAcctClosure = new com.see.truetransact.uicomponent.CTextField();
        lblMinTreatasNew = new com.see.truetransact.uicomponent.CLabel();
        lblMinTreatasDormant = new com.see.truetransact.uicomponent.CLabel();
        txtMinTreatasDormant = new com.see.truetransact.uicomponent.CTextField();
        txtMinTreatasNew = new com.see.truetransact.uicomponent.CTextField();
        lblMinTreatasInOp = new com.see.truetransact.uicomponent.CLabel();
        lblMinTreatNewAcctClosure = new com.see.truetransact.uicomponent.CLabel();
        txtMinTreatasInOp = new com.see.truetransact.uicomponent.CTextField();
        cboMinTreatasDormant = new com.see.truetransact.uicomponent.CComboBox();
        cboMinTreatInOp = new com.see.truetransact.uicomponent.CComboBox();
        cboMinTreatasNew = new com.see.truetransact.uicomponent.CComboBox();
        cboMinTreatNewClosure = new com.see.truetransact.uicomponent.CComboBox();
        lblStatFreq = new com.see.truetransact.uicomponent.CLabel();
        cboStatFreq = new com.see.truetransact.uicomponent.CComboBox();
        sptAcct = new com.see.truetransact.uicomponent.CSeparator();
        txtNumPatternFollowedSuffix = new com.see.truetransact.uicomponent.CTextField();
        txtNumPatternFollowedPrefix = new com.see.truetransact.uicomponent.CTextField();
        txtLastAccNum = new com.see.truetransact.uicomponent.CTextField();
        lblNumPatternFollowed = new com.see.truetransact.uicomponent.CLabel();
        lblLastAccNum = new com.see.truetransact.uicomponent.CLabel();
        panAcctSep = new com.see.truetransact.uicomponent.CSeparator();
        panAcctCol2 = new com.see.truetransact.uicomponent.CPanel();
        lblNROTax = new com.see.truetransact.uicomponent.CLabel();
        lblNoFreeWD = new com.see.truetransact.uicomponent.CLabel();
        txtNoFreeChkLeaves = new com.see.truetransact.uicomponent.CTextField();
        lblFreeWDStart = new com.see.truetransact.uicomponent.CLabel();
        lblFreeWDPd = new com.see.truetransact.uicomponent.CLabel();
        txtRateTaxNRO = new com.see.truetransact.uicomponent.CTextField();
        lblFreeChkLeavePd = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmtWDSlip = new com.see.truetransact.uicomponent.CTextField();
        panAllowWDSlip = new com.see.truetransact.uicomponent.CPanel();
        rdoAllowWD_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAllowWD_No = new com.see.truetransact.uicomponent.CRadioButton();
        panIntClearing = new com.see.truetransact.uicomponent.CPanel();
        rdoIntClearing_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIntClearing_No = new com.see.truetransact.uicomponent.CRadioButton();
        panStaffAcct = new com.see.truetransact.uicomponent.CPanel();
        rdoStaffAcct_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStaffAcct_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblFreeChkLeaveStart = new com.see.truetransact.uicomponent.CLabel();
        lblLimit = new com.see.truetransact.uicomponent.CLabel();
        lblRateNRO = new com.see.truetransact.uicomponent.CLabel();
        panCollectInt = new com.see.truetransact.uicomponent.CPanel();
        rdoCollectInt_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCollectInt_No = new com.see.truetransact.uicomponent.CRadioButton();
        panLimitDefAllow = new com.see.truetransact.uicomponent.CPanel();
        rdoLimitDefAllow_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLimitDefAllow_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtNoFreeWD = new com.see.truetransact.uicomponent.CTextField();
        lblAllowWith = new com.see.truetransact.uicomponent.CLabel();
        panIntUnClearBal = new com.see.truetransact.uicomponent.CPanel();
        rdoIntUnClearBal_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIntUnClearBal_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblDebitInt = new com.see.truetransact.uicomponent.CLabel();
        panTempODAllow = new com.see.truetransact.uicomponent.CPanel();
        rdoTempODAllow_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTempODAllow_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblNoFreeChkLeaves = new com.see.truetransact.uicomponent.CLabel();
        lblTempOD = new com.see.truetransact.uicomponent.CLabel();
        lblCollectInt = new com.see.truetransact.uicomponent.CLabel();
        lblIssue = new com.see.truetransact.uicomponent.CLabel();
        lblStaff = new com.see.truetransact.uicomponent.CLabel();
        lblCrIntUnclear = new com.see.truetransact.uicomponent.CLabel();
        panIssueToken = new com.see.truetransact.uicomponent.CPanel();
        rdoIssueToken_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIssueToken_No = new com.see.truetransact.uicomponent.CRadioButton();
        panTaxIntApplNRO = new com.see.truetransact.uicomponent.CPanel();
        rdoTaxIntApplNRO_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTaxIntApplNRO_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblMaxAmtWDSlip = new com.see.truetransact.uicomponent.CLabel();
        tdtFreeWDStFrom = new com.see.truetransact.uicomponent.CDateField();
        tdtFreeChkLeaveStFrom = new com.see.truetransact.uicomponent.CDateField();
        panFreeWDPd = new com.see.truetransact.uicomponent.CPanel();
        txtFreeWDPd = new com.see.truetransact.uicomponent.CTextField();
        cboFreeWDPd = new com.see.truetransact.uicomponent.CComboBox();
        panFreeChkPd = new com.see.truetransact.uicomponent.CPanel();
        txtFreeChkPD = new com.see.truetransact.uicomponent.CTextField();
        cboFreeChkPd = new com.see.truetransact.uicomponent.CComboBox();
        cboFreeWDStFrom = new com.see.truetransact.uicomponent.CComboBox();
        cboFreeChkLeaveStFrom = new com.see.truetransact.uicomponent.CComboBox();
        panTypeAcc = new com.see.truetransact.uicomponent.CPanel();
        rdoAcc_Reg = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAcc_Nro = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAcc_Nre = new com.see.truetransact.uicomponent.CRadioButton();
        lblTypeOfAcc = new com.see.truetransact.uicomponent.CLabel();
        panIntRec = new com.see.truetransact.uicomponent.CPanel();
        panDebitIntRate = new com.see.truetransact.uicomponent.CPanel();
        lblMinDrIntRate = new com.see.truetransact.uicomponent.CLabel();
        panMinDrIntRate = new com.see.truetransact.uicomponent.CPanel();
        txtMinDebitIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblPer1 = new com.see.truetransact.uicomponent.CLabel();
        lblMaxDebitIntRate = new com.see.truetransact.uicomponent.CLabel();
        panMaxDrIntRate = new com.see.truetransact.uicomponent.CPanel();
        txtMaxDebitIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblPer2 = new com.see.truetransact.uicomponent.CLabel();
        lblDebitIntApplicable = new com.see.truetransact.uicomponent.CLabel();
        panApplDrIntRate = new com.see.truetransact.uicomponent.CPanel();
        txtApplDebitIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblPer3 = new com.see.truetransact.uicomponent.CLabel();
        lblMinDebitIntAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMinDebitIntAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMaxDebitIntAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaxDebitIntAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDebitIntCalcFreq = new com.see.truetransact.uicomponent.CLabel();
        cboDebitIntCalcFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitIntApplFreq = new com.see.truetransact.uicomponent.CLabel();
        cboDebitIntApplFreq = new com.see.truetransact.uicomponent.CComboBox();
        panDebitIntChrg = new com.see.truetransact.uicomponent.CPanel();
        rdoDebitIntChrg_Yes2 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDebitIntChrg_No2 = new com.see.truetransact.uicomponent.CRadioButton();
        lblDebitIntChrg = new com.see.truetransact.uicomponent.CLabel();
        lblStartInterCalc = new com.see.truetransact.uicomponent.CLabel();
        txtStartInterCalc = new com.see.truetransact.uicomponent.CTextField();
        cboStartInterCalc = new com.see.truetransact.uicomponent.CComboBox();
        lblEndInterCalc = new com.see.truetransact.uicomponent.CLabel();
        txtEndInterCalc = new com.see.truetransact.uicomponent.CTextField();
        cboEndInterCalc = new com.see.truetransact.uicomponent.CComboBox();
        panDebitProdData = new com.see.truetransact.uicomponent.CPanel();
        panDebitCompoundReq = new com.see.truetransact.uicomponent.CPanel();
        rdoDebitCompReq_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDebitCompReq_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblDebitCompoundReq = new com.see.truetransact.uicomponent.CLabel();
        lblDebitCompIntCalcFreq = new com.see.truetransact.uicomponent.CLabel();
        cboDebitCompIntCalcFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitProductRoundOff = new com.see.truetransact.uicomponent.CLabel();
        cboDebitProductRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        lblDebitIntRoundOff = new com.see.truetransact.uicomponent.CLabel();
        cboDebitIntRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        lblLastIntCalcDateDebit = new com.see.truetransact.uicomponent.CLabel();
        tdtLastIntCalcDate = new com.see.truetransact.uicomponent.CDateField();
        lblLastIntApplDate = new com.see.truetransact.uicomponent.CLabel();
        tdtLastIntApplDate = new com.see.truetransact.uicomponent.CDateField();
        lblProductFreq = new com.see.truetransact.uicomponent.CLabel();
        cboProdFreq = new com.see.truetransact.uicomponent.CComboBox();
        panPenalIntDebitBalAcct = new com.see.truetransact.uicomponent.CPanel();
        txtPenalIntDebitBalAcct = new com.see.truetransact.uicomponent.CTextField();
        lblPer4 = new com.see.truetransact.uicomponent.CLabel();
        lblPenalIntDebitBalAcct = new com.see.truetransact.uicomponent.CLabel();
        lblPenalIntChrgStart = new com.see.truetransact.uicomponent.CLabel();
        txtPenalIntChrgStart = new com.see.truetransact.uicomponent.CTextField();
        lblStartProdCalc = new com.see.truetransact.uicomponent.CLabel();
        txtStartProdCalc = new com.see.truetransact.uicomponent.CTextField();
        cboStartProdCalc = new com.see.truetransact.uicomponent.CComboBox();
        lblEndProdCalc = new com.see.truetransact.uicomponent.CLabel();
        txtEndProdCalc = new com.see.truetransact.uicomponent.CTextField();
        cboEndProdCalc = new com.see.truetransact.uicomponent.CComboBox();
        sptIntReceivable = new com.see.truetransact.uicomponent.CSeparator();
        panIntPay = new com.see.truetransact.uicomponent.CPanel();
        sptCrInt = new com.see.truetransact.uicomponent.CSeparator();
        panIntPayRate = new com.see.truetransact.uicomponent.CPanel();
        panCrIntGiven = new com.see.truetransact.uicomponent.CPanel();
        rdoCrIntGiven_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCrIntGiven_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCrIntGiven = new com.see.truetransact.uicomponent.CLabel();
        panMinCrIntRate = new com.see.truetransact.uicomponent.CPanel();
        txtMinCrIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblPer5 = new com.see.truetransact.uicomponent.CLabel();
        lblMinCrIntRate = new com.see.truetransact.uicomponent.CLabel();
        panMaxDrIntRate1 = new com.see.truetransact.uicomponent.CPanel();
        txtMaxCrIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblPer6 = new com.see.truetransact.uicomponent.CLabel();
        lblMaxCrIntRate = new com.see.truetransact.uicomponent.CLabel();
        panApplDrIntRate1 = new com.see.truetransact.uicomponent.CPanel();
        txtApplCrIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblPer7 = new com.see.truetransact.uicomponent.CLabel();
        lblCrIntApplicable = new com.see.truetransact.uicomponent.CLabel();
        txtMinCrIntAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMinCrIntAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaxCrIntAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMaxCrIntAmt = new com.see.truetransact.uicomponent.CLabel();
        lblCrIntCalcFreq = new com.see.truetransact.uicomponent.CLabel();
        cboCrIntCalcFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblStMonIntCalc = new com.see.truetransact.uicomponent.CLabel();
        txtStMonIntCalc = new com.see.truetransact.uicomponent.CTextField();
        cboStMonIntCalc = new com.see.truetransact.uicomponent.CComboBox();
        lblIntCalcEndMon = new com.see.truetransact.uicomponent.CLabel();
        txtIntCalcEndMon = new com.see.truetransact.uicomponent.CTextField();
        cboIntCalcEndMon = new com.see.truetransact.uicomponent.CComboBox();
        cboCrIntApplFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblCrIntApplFreq = new com.see.truetransact.uicomponent.CLabel();
        txtMinbalForInt = new com.see.truetransact.uicomponent.CTextField();
        lblIMinBalForInts = new com.see.truetransact.uicomponent.CLabel();
        panIntPayProd = new com.see.truetransact.uicomponent.CPanel();
        lblLastIntCalcDateCR = new com.see.truetransact.uicomponent.CLabel();
        tdtLastIntCalcDateCR = new com.see.truetransact.uicomponent.CDateField();
        lblLastIntApplDateCr = new com.see.truetransact.uicomponent.CLabel();
        tdtLastIntApplDateCr = new com.see.truetransact.uicomponent.CDateField();
        lblCreditProductRoundOff = new com.see.truetransact.uicomponent.CLabel();
        cboCreditProductRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        lblCreditIntRoundOff = new com.see.truetransact.uicomponent.CLabel();
        cboCreditIntRoundOff = new com.see.truetransact.uicomponent.CComboBox();
        panCreditComp = new com.see.truetransact.uicomponent.CPanel();
        rdoCreditComp_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCreditComp_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblCreditComp = new com.see.truetransact.uicomponent.CLabel();
        cboCreditCompIntCalcFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblCreditCompIntCalcFreq = new com.see.truetransact.uicomponent.CLabel();
        cboProdFreqCr = new com.see.truetransact.uicomponent.CComboBox();
        lblProdFreqCr = new com.see.truetransact.uicomponent.CLabel();
        lblStDayProdCalcSBCrInt = new com.see.truetransact.uicomponent.CLabel();
        txtStDayProdCalcSBCrInt = new com.see.truetransact.uicomponent.CTextField();
        cboStDayProdCalcSBCrInt = new com.see.truetransact.uicomponent.CComboBox();
        lblEndDayProdCalcSBCrInt = new com.see.truetransact.uicomponent.CLabel();
        txtEndDayProdCalcSBCrInt = new com.see.truetransact.uicomponent.CTextField();
        cboEndDayProdCalcSBCrInt = new com.see.truetransact.uicomponent.CComboBox();
        lblCalcCriteria = new com.see.truetransact.uicomponent.CLabel();
        cboCalcCriteria = new com.see.truetransact.uicomponent.CComboBox();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        panChargesData = new com.see.truetransact.uicomponent.CPanel();
        lblInOpAcChrg = new com.see.truetransact.uicomponent.CLabel();
        txtlInOpAcChrg = new com.see.truetransact.uicomponent.CTextField();
        lblInOpAcChrgPd = new com.see.truetransact.uicomponent.CLabel();
        cboInOpAcChrgPd = new com.see.truetransact.uicomponent.CComboBox();
        lblChrgPreClosure = new com.see.truetransact.uicomponent.CLabel();
        txtChrgPreClosure = new com.see.truetransact.uicomponent.CTextField();
        lblAcClosingChrg = new com.see.truetransact.uicomponent.CLabel();
        txtAcClosingChrg = new com.see.truetransact.uicomponent.CTextField();
        lblChrgMiscServChrg = new com.see.truetransact.uicomponent.CLabel();
        txtChrgMiscServChrg = new com.see.truetransact.uicomponent.CTextField();
        panNonMainMinBalCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoNonMainMinBalChrg_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNonMainMinBalChrg_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblNonMainMinBalCharges = new com.see.truetransact.uicomponent.CLabel();
        panMinBalAmt = new com.see.truetransact.uicomponent.CPanel();
        txtMinBalAmt = new com.see.truetransact.uicomponent.CTextField();
        cboMinBalAmt = new com.see.truetransact.uicomponent.CComboBox();
        lblMinBalAmt = new com.see.truetransact.uicomponent.CLabel();
        panStatCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoStatCharges_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStatCharges_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblStatCharges = new com.see.truetransact.uicomponent.CLabel();
        panStatChargesChr = new com.see.truetransact.uicomponent.CPanel();
        txtStatChargesChr = new com.see.truetransact.uicomponent.CTextField();
        cboStatChargesChr = new com.see.truetransact.uicomponent.CComboBox();
        lblStatChargesChrg = new com.see.truetransact.uicomponent.CLabel();
        panChkIssuedChrg = new com.see.truetransact.uicomponent.CPanel();
        rdoChkIssuedChrgCh_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoChkIssuedChrgCh_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblChkIssuedChrg = new com.see.truetransact.uicomponent.CLabel();
        lblChkBkIssueChrgPL = new com.see.truetransact.uicomponent.CLabel();
        txtChkBkIssueChrgPL = new com.see.truetransact.uicomponent.CTextField();
        rdoStopPaymentCharges = new com.see.truetransact.uicomponent.CPanel();
        rdoStopPaymentChrg_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoStopPaymentChrg_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblStopPaymentCharges = new com.see.truetransact.uicomponent.CLabel();
        lblStopPayChrg = new com.see.truetransact.uicomponent.CLabel();
        txtStopPayChrg = new com.see.truetransact.uicomponent.CTextField();
        panFolioData = new com.see.truetransact.uicomponent.CPanel();
        panFolioChargeAppl = new com.see.truetransact.uicomponent.CPanel();
        rdoFolioChargeAppl_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFolioChargeAppl_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblFolioChargeAppl = new com.see.truetransact.uicomponent.CLabel();
        lblRatePerFolio = new com.see.truetransact.uicomponent.CLabel();
        txtRatePerFolio = new com.see.truetransact.uicomponent.CTextField();
        lblNoEntryPerFolio = new com.see.truetransact.uicomponent.CLabel();
        txtNoEntryPerFolio = new com.see.truetransact.uicomponent.CTextField();
        panFolioToChargeOn = new com.see.truetransact.uicomponent.CPanel();
        rdoFolioToChargeOn_Credit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFolioToChargeOn_Debit = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFolioToChargeOn_Both = new com.see.truetransact.uicomponent.CRadioButton();
        lblToChargeOnApplFreq = new com.see.truetransact.uicomponent.CLabel();
        lblFolioChrgApplFreq = new com.see.truetransact.uicomponent.CLabel();
        cboFolioChrgApplFreq = new com.see.truetransact.uicomponent.CComboBox();
        cboToCollectFolioChrg = new com.see.truetransact.uicomponent.CComboBox();
        lblToCollectFolioChrg = new com.see.truetransact.uicomponent.CLabel();
        panToChargeOnApplFreq = new com.see.truetransact.uicomponent.CPanel();
        rdoToChargeOnApplFreq_Manual = new com.see.truetransact.uicomponent.CRadioButton();
        rdoToChargeOnApplFreq_System = new com.see.truetransact.uicomponent.CRadioButton();
        rdoToChargeOnApplFreq_Both = new com.see.truetransact.uicomponent.CRadioButton();
        lblFolioToChargeOn = new com.see.truetransact.uicomponent.CLabel();
        cboIncompFolioROffFreq = new com.see.truetransact.uicomponent.CComboBox();
        lblIncompFolioROffFreq = new com.see.truetransact.uicomponent.CLabel();
        tdtLastFolioAppliedDate = new com.see.truetransact.uicomponent.CDateField();
        tdtNextFolioAppliedDt = new com.see.truetransact.uicomponent.CDateField();
        lblFolioChargeLastAppliedDt = new com.see.truetransact.uicomponent.CLabel();
        lblFolioChargeNextAppDt = new com.see.truetransact.uicomponent.CLabel();
        rdoFolioChargeVariable = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFoliochargeFixed = new com.see.truetransact.uicomponent.CRadioButton();
        lblFolioRestriction = new com.see.truetransact.uicomponent.CLabel();
        txtFolioRestrictionPriod = new com.see.truetransact.uicomponent.CTextField();
        cboFolioChargedBefore = new com.see.truetransact.uicomponent.CComboBox();
        panReturnChargesData = new com.see.truetransact.uicomponent.CPanel();
        lblChkRetChrOutward = new com.see.truetransact.uicomponent.CLabel();
        txtChkRetChrOutward = new com.see.truetransact.uicomponent.CTextField();
        lblChkRetChrgIn = new com.see.truetransact.uicomponent.CLabel();
        txtChkRetChrgIn = new com.see.truetransact.uicomponent.CTextField();
        lblAcctOpenCharges = new com.see.truetransact.uicomponent.CLabel();
        txtAcctOpenCharges = new com.see.truetransact.uicomponent.CTextField();
        lblExcessFreeWDChrgPT = new com.see.truetransact.uicomponent.CLabel();
        txtExcessFreeWDChrgPT = new com.see.truetransact.uicomponent.CTextField();
        sptFolio = new com.see.truetransact.uicomponent.CSeparator();
        sptFolioVertical = new com.see.truetransact.uicomponent.CSeparator();
        panDebitWithdrawalCharge = new com.see.truetransact.uicomponent.CPanel();
        lblDebitWithsrawalChargePeriod = new com.see.truetransact.uicomponent.CLabel();
        txtDebiWithdrawalChargePeriod = new com.see.truetransact.uicomponent.CTextField();
        lblDebitChargeType = new com.see.truetransact.uicomponent.CLabel();
        lblDebitChargeTypeRate = new com.see.truetransact.uicomponent.CLabel();
        txtDebitChargeTypeRate = new com.see.truetransact.uicomponent.CTextField();
        chkDebitWithdrawalCharge = new com.see.truetransact.uicomponent.CCheckBox();
        cboDebitChargeType = new com.see.truetransact.uicomponent.CComboBox();
        panSpecialItem = new com.see.truetransact.uicomponent.CPanel();
        panSpecialDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLinkFlexiAcct = new com.see.truetransact.uicomponent.CLabel();
        lblMinBal1FlexiDep = new com.see.truetransact.uicomponent.CLabel();
        lblMinBal2FlexiDep = new com.see.truetransact.uicomponent.CLabel();
        lblFlexiHappen = new com.see.truetransact.uicomponent.CLabel();
        panFlexiHappen = new com.see.truetransact.uicomponent.CPanel();
        rdoFlexiHappen_SB = new com.see.truetransact.uicomponent.CRadioButton();
        rdoFlexiHappen_TD = new com.see.truetransact.uicomponent.CRadioButton();
        panLinkFlexiAcct = new com.see.truetransact.uicomponent.CPanel();
        rdoLinkFlexiAcct_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLinkFlexiAcct_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtMinBal1FlexiDep = new com.see.truetransact.uicomponent.CTextField();
        txtMinBal2FlexiDep = new com.see.truetransact.uicomponent.CTextField();
        lblFlexiTD = new com.see.truetransact.uicomponent.CLabel();
        cboFlexiTD = new com.see.truetransact.uicomponent.CComboBox();
        panMiscDetails = new com.see.truetransact.uicomponent.CPanel();
        lblATMIssued = new com.see.truetransact.uicomponent.CLabel();
        lblMinATMBal = new com.see.truetransact.uicomponent.CLabel();
        lblCreditCdIssued = new com.see.truetransact.uicomponent.CLabel();
        lblMinBalCreditCd = new com.see.truetransact.uicomponent.CLabel();
        panATMIssued = new com.see.truetransact.uicomponent.CPanel();
        rdoATMIssued_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoATMIssued_No = new com.see.truetransact.uicomponent.CRadioButton();
        panABBAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoABBAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoABBAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        panMobBankClient = new com.see.truetransact.uicomponent.CPanel();
        rdoMobBankClient_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMobBankClient_No = new com.see.truetransact.uicomponent.CRadioButton();
        panIVRSProvided = new com.see.truetransact.uicomponent.CPanel();
        rdoIVRSProvided_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIVRSProvided_No = new com.see.truetransact.uicomponent.CRadioButton();
        panDebitCdIssued = new com.see.truetransact.uicomponent.CPanel();
        rdoDebitCdIssued_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDebitCdIssued_No = new com.see.truetransact.uicomponent.CRadioButton();
        panCreditCdIssued = new com.see.truetransact.uicomponent.CPanel();
        rdoCreditCdIssued_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoCreditCdIssued_No = new com.see.truetransact.uicomponent.CRadioButton();
        txtMinATMBal = new com.see.truetransact.uicomponent.CTextField();
        txtMinBalCreditCd = new com.see.truetransact.uicomponent.CTextField();
        txtMinBalDebitCards = new com.see.truetransact.uicomponent.CTextField();
        txtMinBalIVRS = new com.see.truetransact.uicomponent.CTextField();
        txtMinMobBank = new com.see.truetransact.uicomponent.CTextField();
        txtMinBalABB = new com.see.truetransact.uicomponent.CTextField();
        lblMinBalABB = new com.see.truetransact.uicomponent.CLabel();
        lblDebitCardIssued = new com.see.truetransact.uicomponent.CLabel();
        lblMinBalDebitCards = new com.see.truetransact.uicomponent.CLabel();
        lblIVRSProvided = new com.see.truetransact.uicomponent.CLabel();
        lblMinBalIVRS = new com.see.truetransact.uicomponent.CLabel();
        lblMobBankClient = new com.see.truetransact.uicomponent.CLabel();
        lblMinMobBank = new com.see.truetransact.uicomponent.CLabel();
        lblABBAllowed = new com.see.truetransact.uicomponent.CLabel();
        cPanel1 = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtIMPSLimit = new com.see.truetransact.uicomponent.CTextField();
        panAcctHead = new com.see.truetransact.uicomponent.CPanel();
        lblInOpChrg = new com.see.truetransact.uicomponent.CLabel();
        lblPrematureClosureChrg = new com.see.truetransact.uicomponent.CLabel();
        lblMiscServChrg = new com.see.truetransact.uicomponent.CLabel();
        lblAcctClosingChrg = new com.see.truetransact.uicomponent.CLabel();
        lblStatChrg = new com.see.truetransact.uicomponent.CLabel();
        lblFreeWDChrg = new com.see.truetransact.uicomponent.CLabel();
        lblAcctDebitInt = new com.see.truetransact.uicomponent.CLabel();
        lblAcctCrInt = new com.see.truetransact.uicomponent.CLabel();
        lblClearingIntAcctHd = new com.see.truetransact.uicomponent.CLabel();
        lblChkBkIssueChrg = new com.see.truetransact.uicomponent.CLabel();
        lblStopPaymentChrg = new com.see.truetransact.uicomponent.CLabel();
        lblOutwardChkRetChrg = new com.see.truetransact.uicomponent.CLabel();
        lblInwardChkRetChrg = new com.see.truetransact.uicomponent.CLabel();
        lblAcctOpenChrg = new com.see.truetransact.uicomponent.CLabel();
        lblExcessFreeWDChrg = new com.see.truetransact.uicomponent.CLabel();
        lblTaxGL = new com.see.truetransact.uicomponent.CLabel();
        txtInOpChrg = new com.see.truetransact.uicomponent.CTextField();
        txtPrematureClosureChrg = new com.see.truetransact.uicomponent.CTextField();
        txtAcctClosingChrg = new com.see.truetransact.uicomponent.CTextField();
        txtMiscServChrg = new com.see.truetransact.uicomponent.CTextField();
        txtStatChrg = new com.see.truetransact.uicomponent.CTextField();
        txtFreeWDChrg = new com.see.truetransact.uicomponent.CTextField();
        txtAcctDebitInt = new com.see.truetransact.uicomponent.CTextField();
        txtAcctCrInt = new com.see.truetransact.uicomponent.CTextField();
        txtChkBkIssueChrg = new com.see.truetransact.uicomponent.CTextField();
        txtClearingIntAcctHd = new com.see.truetransact.uicomponent.CTextField();
        txtStopPaymentChrg = new com.see.truetransact.uicomponent.CTextField();
        txtOutwardChkRetChrg = new com.see.truetransact.uicomponent.CTextField();
        txtInwardChkRetChrg = new com.see.truetransact.uicomponent.CTextField();
        txtAcctOpenChrg = new com.see.truetransact.uicomponent.CTextField();
        txtExcessFreeWDChrg = new com.see.truetransact.uicomponent.CTextField();
        txtTaxGL = new com.see.truetransact.uicomponent.CTextField();
        btnInOpChrg = new com.see.truetransact.uicomponent.CButton();
        btnPrematureClosureChrg = new com.see.truetransact.uicomponent.CButton();
        btnAcctClosingChrg = new com.see.truetransact.uicomponent.CButton();
        btnMiscServChrg = new com.see.truetransact.uicomponent.CButton();
        btnAcctDebitInt = new com.see.truetransact.uicomponent.CButton();
        btnFreeWDChrg = new com.see.truetransact.uicomponent.CButton();
        btnStatChrg = new com.see.truetransact.uicomponent.CButton();
        btnAcctCrInt = new com.see.truetransact.uicomponent.CButton();
        btnClearingIntAcctHd = new com.see.truetransact.uicomponent.CButton();
        btnStopPaymentChrg = new com.see.truetransact.uicomponent.CButton();
        btnChkBkIssueChrg = new com.see.truetransact.uicomponent.CButton();
        btnOutwardChkRetChrg = new com.see.truetransact.uicomponent.CButton();
        btnInwardChkRetChrg = new com.see.truetransact.uicomponent.CButton();
        btnAcctOpenChrg = new com.see.truetransact.uicomponent.CButton();
        btnExcessFreeWDChrg = new com.see.truetransact.uicomponent.CButton();
        btnTaxGL = new com.see.truetransact.uicomponent.CButton();
        txtInOpChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtPrematureClosureChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtAcctClosingChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtMiscServChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtStatChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtFreeWDChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtAcctDebitIntDesc = new com.see.truetransact.uicomponent.CTextField();
        txtAcctCrIntDesc = new com.see.truetransact.uicomponent.CTextField();
        txtClearingIntAcctHdDesc = new com.see.truetransact.uicomponent.CTextField();
        txtChkBkIssueChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtStopPaymentChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtOutwardChkRetChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtInwardChkRetChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtAcctOpenChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtExcessFreeWDChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        txtTaxGLDesc = new com.see.truetransact.uicomponent.CTextField();
        panOtherAcctHead = new com.see.truetransact.uicomponent.CPanel();
        lblNonMainMinBalChrg = new com.see.truetransact.uicomponent.CLabel();
        txtNonMainMinBalChrg = new com.see.truetransact.uicomponent.CTextField();
        btnInOperative = new com.see.truetransact.uicomponent.CButton();
        txtNonMainMinBalChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        lblInOperative = new com.see.truetransact.uicomponent.CLabel();
        txtFolioChrg = new com.see.truetransact.uicomponent.CTextField();
        btnNonMainMinBalChrg = new com.see.truetransact.uicomponent.CButton();
        txtInOperativeDesc = new com.see.truetransact.uicomponent.CTextField();
        lblFolioChrg = new com.see.truetransact.uicomponent.CLabel();
        txtInOperative = new com.see.truetransact.uicomponent.CTextField();
        btnFolioChrg = new com.see.truetransact.uicomponent.CButton();
        txtFolioChrgDesc = new com.see.truetransact.uicomponent.CTextField();
        lblDebitWithdrawalChargeHead = new com.see.truetransact.uicomponent.CLabel();
        txtDebitWithdrawalChargeHead = new com.see.truetransact.uicomponent.CTextField();
        btnDebitWithdrawalCharge = new com.see.truetransact.uicomponent.CButton();
        txtDebitWithdrawalChargeDesc = new com.see.truetransact.uicomponent.CTextField();
        lblATMGL = new com.see.truetransact.uicomponent.CLabel();
        txtATMGL = new com.see.truetransact.uicomponent.CTextField();
        btnATMGL = new com.see.truetransact.uicomponent.CButton();
        txtATMGLDisplay = new com.see.truetransact.uicomponent.CTextField();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrOperativeAcctProduct = new com.see.truetransact.uicomponent.CMenuBar();
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
        mitSaveAs = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(860, 650));
        setPreferredSize(new java.awt.Dimension(860, 650));

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
        tbrOperativeAcctProduct.add(btnView);

        lblSpace6.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace12);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCopy);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace13);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnSave);

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace14);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnAuthorize);

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace15);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnException);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace16);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnReject);

        lblSpace5.setText("     ");
        tbrOperativeAcctProduct.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrOperativeAcctProduct.add(btnPrint);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrOperativeAcctProduct.add(lblSpace17);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrOperativeAcctProduct.add(btnClose);

        getContentPane().add(tbrOperativeAcctProduct, java.awt.BorderLayout.NORTH);

        panOperativeProduct.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panOperativeProduct.setLayout(new java.awt.GridBagLayout());

        lblSpace4.setText(" ");
        lblSpace4.setMaximumSize(new java.awt.Dimension(3, 5));
        lblSpace4.setMinimumSize(new java.awt.Dimension(3, 5));
        lblSpace4.setPreferredSize(new java.awt.Dimension(3, 5));
        panOperativeProduct.add(lblSpace4, new java.awt.GridBagConstraints());

        panAccount.setMinimumSize(new java.awt.Dimension(550, 415));
        panAccount.setPreferredSize(new java.awt.Dimension(550, 415));
        panAccount.setLayout(new java.awt.GridBagLayout());

        panAcctCol1.setMinimumSize(new java.awt.Dimension(358, 500));
        panAcctCol1.setPreferredSize(new java.awt.Dimension(358, 500));
        panAcctCol1.setLayout(new java.awt.GridBagLayout());

        panMinPerd1.setMinimumSize(new java.awt.Dimension(350, 129));
        panMinPerd1.setPreferredSize(new java.awt.Dimension(350, 124));
        panMinPerd1.setLayout(new java.awt.GridBagLayout());

        lblProductID.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProductID.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panMinPerd1.add(lblProductID, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMinPerd1.add(txtProductID, gridBagConstraints);

        lblDesc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMinPerd1.add(lblDesc, gridBagConstraints);

        txtDesc.setMinimumSize(new java.awt.Dimension(200, 21));
        txtDesc.setPreferredSize(new java.awt.Dimension(200, 21));
        txtDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDescFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 3);
        panMinPerd1.add(txtDesc, gridBagConstraints);

        lblAcctHd.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAcctHd.setText("Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMinPerd1.add(lblAcctHd, gridBagConstraints);

        lblBehaves.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblBehaves.setText("Operates Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMinPerd1.add(lblBehaves, gridBagConstraints);

        lblProdCurrency.setText("Product Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMinPerd1.add(lblProdCurrency, gridBagConstraints);

        panAcctHd.setMinimumSize(new java.awt.Dimension(129, 23));
        panAcctHd.setLayout(new java.awt.GridBagLayout());

        txtAcctHd.setAllowAll(true);
        txtAcctHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctHd.setEnabled(false);
        txtAcctHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctHd.add(txtAcctHd, gridBagConstraints);

        btnAcctHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctHd.setToolTipText("Select");
        btnAcctHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcctHd.add(btnAcctHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMinPerd1.add(panAcctHd, gridBagConstraints);

        cboBehaves.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMinPerd1.add(cboBehaves, gridBagConstraints);

        cboProdCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panMinPerd1.add(cboProdCurrency, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCol1.add(panMinPerd1, gridBagConstraints);

        lblChequeAllowed.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblChequeAllowed.setText("Are Chequebooks Allowed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol1.add(lblChequeAllowed, gridBagConstraints);

        lblMinBalChqbk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinBalChqbk.setText("Minimum Balance with Chequebook");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol1.add(lblMinBalChqbk, gridBagConstraints);

        txtMinBalChkbk.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(txtMinBalChkbk, gridBagConstraints);

        lblMinBalwochk.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinBalwochk.setText("Minimum Balance without Chequebook");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(lblMinBalwochk, gridBagConstraints);

        txtMinBalwchk.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(txtMinBalwchk, gridBagConstraints);

        lblIntroReq.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblIntroReq.setText("Introducer Required?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(lblIntroReq, gridBagConstraints);

        lblNomineereq.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNomineereq.setText("Nominee Required?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(lblNomineereq, gridBagConstraints);

        lblNoNominee.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoNominee.setText("Number of Nominees");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(lblNoNominee, gridBagConstraints);

        panChkAllowed.setLayout(new java.awt.GridBagLayout());

        rdoChkAllowed.add(rdoChkAllowed_Yes);
        rdoChkAllowed_Yes.setText("Yes");
        rdoChkAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChkAllowed_YesActionPerformed(evt);
            }
        });
        panChkAllowed.add(rdoChkAllowed_Yes, new java.awt.GridBagConstraints());

        rdoChkAllowed.add(rdoChkAllowed_No);
        rdoChkAllowed_No.setText("No");
        rdoChkAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChkAllowed_NoActionPerformed(evt);
            }
        });
        panChkAllowed.add(rdoChkAllowed_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panAcctCol1.add(panChkAllowed, gridBagConstraints);

        txtNoNominees.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(txtNoNominees, gridBagConstraints);

        panNomineeReq.setLayout(new java.awt.GridBagLayout());

        rdoNomineeReq.add(rdoNomineeReq_Yes);
        rdoNomineeReq_Yes.setText("Yes");
        rdoNomineeReq_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNomineeReq_YesActionPerformed(evt);
            }
        });
        panNomineeReq.add(rdoNomineeReq_Yes, new java.awt.GridBagConstraints());

        rdoNomineeReq.add(rdoNomineeReq_No);
        rdoNomineeReq_No.setText("No");
        rdoNomineeReq_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNomineeReq_NoActionPerformed(evt);
            }
        });
        panNomineeReq.add(rdoNomineeReq_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panAcctCol1.add(panNomineeReq, gridBagConstraints);

        panIntroReq.setLayout(new java.awt.GridBagLayout());

        rdoIntroReq.add(rdoIntroReq_Yes);
        rdoIntroReq_Yes.setText("Yes");
        rdoIntroReq_Yes.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panIntroReq.add(rdoIntroReq_Yes, new java.awt.GridBagConstraints());

        rdoIntroReq.add(rdoIntroReq_No);
        rdoIntroReq_No.setText("No");
        rdoIntroReq_No.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        panIntroReq.add(rdoIntroReq_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panAcctCol1.add(panIntroReq, gridBagConstraints);

        panMinPerd.setBorder(javax.swing.BorderFactory.createTitledBorder(" Minimum Period to Treat Account "));
        panMinPerd.setMinimumSize(new java.awt.Dimension(350, 129));
        panMinPerd.setPreferredSize(new java.awt.Dimension(350, 129));
        panMinPerd.setLayout(new java.awt.GridBagLayout());

        txtMainTreatNewAcctClosure.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMainTreatNewAcctClosure.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMainTreatNewAcctClosure.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMainTreatNewAcctClosureFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panMinPerd.add(txtMainTreatNewAcctClosure, gridBagConstraints);

        lblMinTreatasNew.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinTreatasNew.setText("as New");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(lblMinTreatasNew, gridBagConstraints);

        lblMinTreatasDormant.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinTreatasDormant.setText("as Dormant");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(lblMinTreatasDormant, gridBagConstraints);

        txtMinTreatasDormant.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinTreatasDormant.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(txtMinTreatasDormant, gridBagConstraints);

        txtMinTreatasNew.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinTreatasNew.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(txtMinTreatasNew, gridBagConstraints);

        lblMinTreatasInOp.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinTreatasInOp.setText("as Inoperative");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(lblMinTreatasInOp, gridBagConstraints);

        lblMinTreatNewAcctClosure.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinTreatNewAcctClosure.setText("New Account for Closure");
        lblMinTreatNewAcctClosure.setMaximumSize(new java.awt.Dimension(144, 15));
        lblMinTreatNewAcctClosure.setMinimumSize(new java.awt.Dimension(144, 15));
        lblMinTreatNewAcctClosure.setPreferredSize(new java.awt.Dimension(144, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panMinPerd.add(lblMinTreatNewAcctClosure, gridBagConstraints);

        txtMinTreatasInOp.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinTreatasInOp.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMinTreatasInOp.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMinTreatasInOpFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(txtMinTreatasInOp, gridBagConstraints);

        cboMinTreatasDormant.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Days", "Months", "Years" }));
        cboMinTreatasDormant.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(cboMinTreatasDormant, gridBagConstraints);

        cboMinTreatInOp.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Days", "Months", "Years" }));
        cboMinTreatInOp.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(cboMinTreatInOp, gridBagConstraints);

        cboMinTreatasNew.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Days", "Months", "Years" }));
        cboMinTreatasNew.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 4);
        panMinPerd.add(cboMinTreatasNew, gridBagConstraints);

        cboMinTreatNewClosure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Days", "Months", "Years" }));
        cboMinTreatNewClosure.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 2, 4);
        panMinPerd.add(cboMinTreatNewClosure, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panAcctCol1.add(panMinPerd, gridBagConstraints);

        lblStatFreq.setText("Statement Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol1.add(lblStatFreq, gridBagConstraints);

        cboStatFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol1.add(cboStatFreq, gridBagConstraints);

        sptAcct.setMinimumSize(new java.awt.Dimension(5, 3));
        sptAcct.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        panAcctCol1.add(sptAcct, gridBagConstraints);

        txtNumPatternFollowedSuffix.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowedSuffix.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(1, 48, 1, 3);
        panAcctCol1.add(txtNumPatternFollowedSuffix, gridBagConstraints);

        txtNumPatternFollowedPrefix.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNumPatternFollowedPrefix.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panAcctCol1.add(txtNumPatternFollowedPrefix, gridBagConstraints);

        txtLastAccNum.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol1.add(txtLastAccNum, gridBagConstraints);

        lblNumPatternFollowed.setText("Numbering Pattern to be Followed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 4, 4);
        panAcctCol1.add(lblNumPatternFollowed, gridBagConstraints);

        lblLastAccNum.setText("Next Account Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcctCol1.add(lblLastAccNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAccount.add(panAcctCol1, gridBagConstraints);

        panAcctSep.setOrientation(javax.swing.SwingConstants.VERTICAL);
        panAcctSep.setMinimumSize(new java.awt.Dimension(5, 5));
        panAcctSep.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAccount.add(panAcctSep, gridBagConstraints);

        panAcctCol2.setMinimumSize(new java.awt.Dimension(421, 500));
        panAcctCol2.setPreferredSize(new java.awt.Dimension(421, 500));
        panAcctCol2.setLayout(new java.awt.GridBagLayout());

        lblNROTax.setText("Tax on Interest Applicable (NRO)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(lblNROTax, gridBagConstraints);

        lblNoFreeWD.setText("No. Free Withdrawals");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(lblNoFreeWD, gridBagConstraints);

        txtNoFreeChkLeaves.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoFreeChkLeaves.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoFreeChkLeavesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(txtNoFreeChkLeaves, gridBagConstraints);

        lblFreeWDStart.setText("Free Withdrawals Starting From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(lblFreeWDStart, gridBagConstraints);

        lblFreeWDPd.setText("Free Withdrawals Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(lblFreeWDPd, gridBagConstraints);

        txtRateTaxNRO.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRateTaxNRO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRateTaxNROFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(txtRateTaxNRO, gridBagConstraints);

        lblFreeChkLeavePd.setText("Free Cheque Leaves Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(lblFreeChkLeavePd, gridBagConstraints);

        txtMaxAmtWDSlip.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 4);
        panAcctCol2.add(txtMaxAmtWDSlip, gridBagConstraints);

        panAllowWDSlip.setLayout(new java.awt.GridBagLayout());

        rdoAllowWD.add(rdoAllowWD_Yes);
        rdoAllowWD_Yes.setText("Yes");
        rdoAllowWD_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAllowWD_YesActionPerformed(evt);
            }
        });
        panAllowWDSlip.add(rdoAllowWD_Yes, new java.awt.GridBagConstraints());

        rdoAllowWD.add(rdoAllowWD_No);
        rdoAllowWD_No.setText("No");
        rdoAllowWD_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAllowWD_NoActionPerformed(evt);
            }
        });
        panAllowWDSlip.add(rdoAllowWD_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panAllowWDSlip, gridBagConstraints);

        panIntClearing.setLayout(new java.awt.GridBagLayout());

        rdoIntClearing.add(rdoIntClearing_Yes);
        rdoIntClearing_Yes.setText("Yes");
        rdoIntClearing_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIntClearing_YesActionPerformed(evt);
            }
        });
        panIntClearing.add(rdoIntClearing_Yes, new java.awt.GridBagConstraints());

        rdoIntClearing.add(rdoIntClearing_No);
        rdoIntClearing_No.setText("No");
        rdoIntClearing_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIntClearing_NoActionPerformed(evt);
            }
        });
        panIntClearing.add(rdoIntClearing_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panIntClearing, gridBagConstraints);

        panStaffAcct.setLayout(new java.awt.GridBagLayout());

        rdoStaffAcct.add(rdoStaffAcct_Yes);
        rdoStaffAcct_Yes.setText("Yes");
        rdoStaffAcct_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStaffAcct_YesActionPerformed(evt);
            }
        });
        panStaffAcct.add(rdoStaffAcct_Yes, new java.awt.GridBagConstraints());

        rdoStaffAcct.add(rdoStaffAcct_No);
        rdoStaffAcct_No.setText("No");
        panStaffAcct.add(rdoStaffAcct_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panStaffAcct, gridBagConstraints);

        lblFreeChkLeaveStart.setText("Free Cheque Leaves Starting From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(lblFreeChkLeaveStart, gridBagConstraints);

        lblLimit.setText("Limit Definition Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblLimit, gridBagConstraints);

        lblRateNRO.setText("Rate of Tax (NRO) %");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblRateNRO, gridBagConstraints);

        panCollectInt.setLayout(new java.awt.GridBagLayout());

        rdoCollectInt.add(rdoCollectInt_Yes);
        rdoCollectInt_Yes.setText("Yes");
        panCollectInt.add(rdoCollectInt_Yes, new java.awt.GridBagConstraints());

        rdoCollectInt.add(rdoCollectInt_No);
        rdoCollectInt_No.setText("No");
        panCollectInt.add(rdoCollectInt_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panCollectInt, gridBagConstraints);

        panLimitDefAllow.setLayout(new java.awt.GridBagLayout());

        rdoLimitDefAllow.add(rdoLimitDefAllow_Yes);
        rdoLimitDefAllow_Yes.setText("Yes");
        rdoLimitDefAllow_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLimitDefAllow_YesActionPerformed(evt);
            }
        });
        panLimitDefAllow.add(rdoLimitDefAllow_Yes, new java.awt.GridBagConstraints());

        rdoLimitDefAllow.add(rdoLimitDefAllow_No);
        rdoLimitDefAllow_No.setText("No");
        rdoLimitDefAllow_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLimitDefAllow_NoActionPerformed(evt);
            }
        });
        panLimitDefAllow.add(rdoLimitDefAllow_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panLimitDefAllow, gridBagConstraints);

        txtNoFreeWD.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoFreeWD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoFreeWDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(txtNoFreeWD, gridBagConstraints);

        lblAllowWith.setText("Allow Withdrawal Slip?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblAllowWith, gridBagConstraints);

        panIntUnClearBal.setLayout(new java.awt.GridBagLayout());

        rdoIntUnClearBal.add(rdoIntUnClearBal_Yes);
        rdoIntUnClearBal_Yes.setText("Yes");
        panIntUnClearBal.add(rdoIntUnClearBal_Yes, new java.awt.GridBagConstraints());

        rdoIntUnClearBal.add(rdoIntUnClearBal_No);
        rdoIntUnClearBal_No.setText("No");
        panIntUnClearBal.add(rdoIntUnClearBal_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panIntUnClearBal, gridBagConstraints);

        lblDebitInt.setText("Debit Interest against Clearing Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblDebitInt, gridBagConstraints);

        panTempODAllow.setLayout(new java.awt.GridBagLayout());

        rdoTempODAllow.add(rdoTempODAllow_Yes);
        rdoTempODAllow_Yes.setText("Yes");
        rdoTempODAllow_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTempODAllow_YesActionPerformed(evt);
            }
        });
        panTempODAllow.add(rdoTempODAllow_Yes, new java.awt.GridBagConstraints());

        rdoTempODAllow.add(rdoTempODAllow_No);
        rdoTempODAllow_No.setText("No");
        rdoTempODAllow_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTempODAllow_NoActionPerformed(evt);
            }
        });
        panTempODAllow.add(rdoTempODAllow_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panTempODAllow, gridBagConstraints);

        lblNoFreeChkLeaves.setText("No. of Free Cheque Leaves");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(lblNoFreeChkLeaves, gridBagConstraints);

        lblTempOD.setText("Temporary O/D Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblTempOD, gridBagConstraints);

        lblCollectInt.setText("Collect Interest after Balance gets to Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblCollectInt, gridBagConstraints);

        lblIssue.setText("Issue Token");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblIssue, gridBagConstraints);

        lblStaff.setText("Staff Account Only");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblStaff, gridBagConstraints);

        lblCrIntUnclear.setText("Cr. Interest on Unclear Balance Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblCrIntUnclear, gridBagConstraints);

        panIssueToken.setLayout(new java.awt.GridBagLayout());

        rdoIssueToken.add(rdoIssueToken_Yes);
        rdoIssueToken_Yes.setText("Yes");
        panIssueToken.add(rdoIssueToken_Yes, new java.awt.GridBagConstraints());

        rdoIssueToken.add(rdoIssueToken_No);
        rdoIssueToken_No.setText("No");
        panIssueToken.add(rdoIssueToken_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panIssueToken, gridBagConstraints);

        panTaxIntApplNRO.setLayout(new java.awt.GridBagLayout());

        rdoTaxIntApplNRO.add(rdoTaxIntApplNRO_Yes);
        rdoTaxIntApplNRO_Yes.setText("Yes");
        rdoTaxIntApplNRO_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTaxIntApplNRO_YesActionPerformed(evt);
            }
        });
        panTaxIntApplNRO.add(rdoTaxIntApplNRO_Yes, new java.awt.GridBagConstraints());

        rdoTaxIntApplNRO.add(rdoTaxIntApplNRO_No);
        rdoTaxIntApplNRO_No.setText("No");
        rdoTaxIntApplNRO_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoTaxIntApplNRO_NoActionPerformed(evt);
            }
        });
        panTaxIntApplNRO.add(rdoTaxIntApplNRO_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panAcctCol2.add(panTaxIntApplNRO, gridBagConstraints);

        lblMaxAmtWDSlip.setText("Max. Amt. Allowed for Withdrawal Slip");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblMaxAmtWDSlip, gridBagConstraints);

        tdtFreeWDStFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFreeWDStFrom.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(tdtFreeWDStFrom, gridBagConstraints);

        tdtFreeChkLeaveStFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtFreeChkLeaveStFrom.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(tdtFreeChkLeaveStFrom, gridBagConstraints);

        panFreeWDPd.setLayout(new java.awt.GridBagLayout());

        txtFreeWDPd.setMinimumSize(new java.awt.Dimension(50, 21));
        txtFreeWDPd.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panFreeWDPd.add(txtFreeWDPd, gridBagConstraints);

        cboFreeWDPd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panFreeWDPd.add(cboFreeWDPd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(panFreeWDPd, gridBagConstraints);

        panFreeChkPd.setLayout(new java.awt.GridBagLayout());

        txtFreeChkPD.setMinimumSize(new java.awt.Dimension(50, 21));
        txtFreeChkPD.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panFreeChkPd.add(txtFreeChkPD, gridBagConstraints);

        cboFreeChkPd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panFreeChkPd.add(cboFreeChkPd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(panFreeChkPd, gridBagConstraints);

        cboFreeWDStFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFreeWDStFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFreeWDStFromActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(cboFreeWDStFrom, gridBagConstraints);

        cboFreeChkLeaveStFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFreeChkLeaveStFrom.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFreeChkLeaveStFromActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panAcctCol2.add(cboFreeChkLeaveStFrom, gridBagConstraints);

        panTypeAcc.setMinimumSize(new java.awt.Dimension(159, 21));
        panTypeAcc.setPreferredSize(new java.awt.Dimension(159, 21));
        panTypeAcc.setLayout(new java.awt.GridBagLayout());

        rdoLimitDefAllow.add(rdoAcc_Reg);
        rdoAcc_Reg.setText("REG");
        rdoAcc_Reg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAcc_RegActionPerformed(evt);
            }
        });
        rdoAcc_Reg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoAcc_RegFocusLost(evt);
            }
        });
        panTypeAcc.add(rdoAcc_Reg, new java.awt.GridBagConstraints());

        rdoLimitDefAllow.add(rdoAcc_Nro);
        rdoAcc_Nro.setText("NRO");
        rdoAcc_Nro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAcc_NroActionPerformed(evt);
            }
        });
        rdoAcc_Nro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoAcc_NroFocusLost(evt);
            }
        });
        panTypeAcc.add(rdoAcc_Nro, new java.awt.GridBagConstraints());

        rdoLimitDefAllow.add(rdoAcc_Nre);
        rdoAcc_Nre.setText("NRE");
        rdoAcc_Nre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoAcc_NreActionPerformed(evt);
            }
        });
        rdoAcc_Nre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                rdoAcc_NreFocusLost(evt);
            }
        });
        panTypeAcc.add(rdoAcc_Nre, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAcctCol2.add(panTypeAcc, gridBagConstraints);

        lblTypeOfAcc.setText("Type Of Account");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAcctCol2.add(lblTypeOfAcc, gridBagConstraints);

        panAccount.add(panAcctCol2, new java.awt.GridBagConstraints());

        tabOperativeAcctProduct.addTab("Account", panAccount);

        panIntRec.setLayout(new java.awt.GridBagLayout());

        panDebitIntRate.setLayout(new java.awt.GridBagLayout());

        lblMinDrIntRate.setText("Minimum Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblMinDrIntRate, gridBagConstraints);

        panMinDrIntRate.setLayout(new java.awt.GridBagLayout());

        txtMinDebitIntRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinDebitIntRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinDrIntRate.add(txtMinDebitIntRate, new java.awt.GridBagConstraints());

        lblPer1.setText("%");
        panMinDrIntRate.add(lblPer1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(panMinDrIntRate, gridBagConstraints);

        lblMaxDebitIntRate.setText("Maximum Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblMaxDebitIntRate, gridBagConstraints);

        panMaxDrIntRate.setLayout(new java.awt.GridBagLayout());

        txtMaxDebitIntRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxDebitIntRate.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMaxDebitIntRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDebitIntRateFocusLost(evt);
            }
        });
        panMaxDrIntRate.add(txtMaxDebitIntRate, new java.awt.GridBagConstraints());

        lblPer2.setText("%");
        panMaxDrIntRate.add(lblPer2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(panMaxDrIntRate, gridBagConstraints);

        lblDebitIntApplicable.setText("Applicable Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblDebitIntApplicable, gridBagConstraints);

        panApplDrIntRate.setLayout(new java.awt.GridBagLayout());

        txtApplDebitIntRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtApplDebitIntRate.setPreferredSize(new java.awt.Dimension(50, 21));
        txtApplDebitIntRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApplDebitIntRateFocusLost(evt);
            }
        });
        panApplDrIntRate.add(txtApplDebitIntRate, new java.awt.GridBagConstraints());

        lblPer3.setText("%");
        panApplDrIntRate.add(lblPer3, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(panApplDrIntRate, gridBagConstraints);

        lblMinDebitIntAmt.setText("Min Interest Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblMinDebitIntAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(txtMinDebitIntAmt, gridBagConstraints);

        lblMaxDebitIntAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaxDebitIntAmt.setText("Max Interest Amt");
        lblMaxDebitIntAmt.setMaximumSize(new java.awt.Dimension(130, 18));
        lblMaxDebitIntAmt.setMinimumSize(new java.awt.Dimension(96, 18));
        lblMaxDebitIntAmt.setPreferredSize(new java.awt.Dimension(130, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblMaxDebitIntAmt, gridBagConstraints);

        txtMaxDebitIntAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxDebitIntAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(txtMaxDebitIntAmt, gridBagConstraints);

        lblDebitIntCalcFreq.setText("Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblDebitIntCalcFreq, gridBagConstraints);

        cboDebitIntCalcFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDebitIntCalcFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDebitIntCalcFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(cboDebitIntCalcFreq, gridBagConstraints);

        lblDebitIntApplFreq.setText("Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblDebitIntApplFreq, gridBagConstraints);

        cboDebitIntApplFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(cboDebitIntApplFreq, gridBagConstraints);

        panDebitIntChrg.setLayout(new java.awt.GridBagLayout());

        rdoDebitIntChrg.add(rdoDebitIntChrg_Yes2);
        rdoDebitIntChrg_Yes2.setText("Yes");
        rdoDebitIntChrg_Yes2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitIntChrg_Yes2ActionPerformed(evt);
            }
        });
        panDebitIntChrg.add(rdoDebitIntChrg_Yes2, new java.awt.GridBagConstraints());

        rdoDebitIntChrg.add(rdoDebitIntChrg_No2);
        rdoDebitIntChrg_No2.setText("No");
        rdoDebitIntChrg_No2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitIntChrg_No2ActionPerformed(evt);
            }
        });
        panDebitIntChrg.add(rdoDebitIntChrg_No2, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(panDebitIntChrg, gridBagConstraints);

        lblDebitIntChrg.setText("Debit Interest to be Charged");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblDebitIntChrg, gridBagConstraints);

        lblStartInterCalc.setText("Start Day of Interest Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblStartInterCalc, gridBagConstraints);

        txtStartInterCalc.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStartInterCalc.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panDebitIntRate.add(txtStartInterCalc, gridBagConstraints);

        cboStartInterCalc.setMinimumSize(new java.awt.Dimension(100, 21));
        cboStartInterCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStartInterCalcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panDebitIntRate.add(cboStartInterCalc, gridBagConstraints);

        lblEndInterCalc.setText("End Day of Interest Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitIntRate.add(lblEndInterCalc, gridBagConstraints);

        txtEndInterCalc.setMinimumSize(new java.awt.Dimension(50, 21));
        txtEndInterCalc.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panDebitIntRate.add(txtEndInterCalc, gridBagConstraints);

        cboEndInterCalc.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEndInterCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEndInterCalcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panDebitIntRate.add(cboEndInterCalc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntRec.add(panDebitIntRate, gridBagConstraints);

        panDebitProdData.setLayout(new java.awt.GridBagLayout());

        panDebitCompoundReq.setLayout(new java.awt.GridBagLayout());

        rdoDebitCompReq.add(rdoDebitCompReq_Yes);
        rdoDebitCompReq_Yes.setText("Yes");
        rdoDebitCompReq_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitCompReq_YesActionPerformed(evt);
            }
        });
        panDebitCompoundReq.add(rdoDebitCompReq_Yes, new java.awt.GridBagConstraints());

        rdoDebitCompReq.add(rdoDebitCompReq_No);
        rdoDebitCompReq_No.setText("No");
        rdoDebitCompReq_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitCompReq_NoActionPerformed(evt);
            }
        });
        panDebitCompoundReq.add(rdoDebitCompReq_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(panDebitCompoundReq, gridBagConstraints);

        lblDebitCompoundReq.setText("Debit Compound Required?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblDebitCompoundReq, gridBagConstraints);

        lblDebitCompIntCalcFreq.setText("Debit Compound Interest Calculation Freq");
        lblDebitCompIntCalcFreq.setMinimumSize(new java.awt.Dimension(260, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblDebitCompIntCalcFreq, gridBagConstraints);

        cboDebitCompIntCalcFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(cboDebitCompIntCalcFreq, gridBagConstraints);

        lblDebitProductRoundOff.setText("Debit Product Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblDebitProductRoundOff, gridBagConstraints);

        cboDebitProductRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(cboDebitProductRoundOff, gridBagConstraints);

        lblDebitIntRoundOff.setText("Debit Interest Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblDebitIntRoundOff, gridBagConstraints);

        cboDebitIntRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(cboDebitIntRoundOff, gridBagConstraints);

        lblLastIntCalcDateDebit.setText("Last Interest Calculation Date - Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblLastIntCalcDateDebit, gridBagConstraints);

        tdtLastIntCalcDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLastIntCalcDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(tdtLastIntCalcDate, gridBagConstraints);

        lblLastIntApplDate.setText("Last Interest Application Date - Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblLastIntApplDate, gridBagConstraints);

        tdtLastIntApplDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLastIntApplDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(tdtLastIntApplDate, gridBagConstraints);

        lblProductFreq.setText("Product Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblProductFreq, gridBagConstraints);

        cboProdFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(cboProdFreq, gridBagConstraints);

        panPenalIntDebitBalAcct.setLayout(new java.awt.GridBagLayout());

        txtPenalIntDebitBalAcct.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPenalIntDebitBalAcct.setPreferredSize(new java.awt.Dimension(50, 21));
        panPenalIntDebitBalAcct.add(txtPenalIntDebitBalAcct, new java.awt.GridBagConstraints());

        lblPer4.setText("%");
        panPenalIntDebitBalAcct.add(lblPer4, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(panPenalIntDebitBalAcct, gridBagConstraints);

        lblPenalIntDebitBalAcct.setText("Penal Interest for Debit Balance Accounts");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblPenalIntDebitBalAcct, gridBagConstraints);

        lblPenalIntChrgStart.setText("Penal Interest Charge Start Day (Nth Day)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblPenalIntChrgStart, gridBagConstraints);

        txtPenalIntChrgStart.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(txtPenalIntChrgStart, gridBagConstraints);

        lblStartProdCalc.setText("Start Day of Product Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblStartProdCalc, gridBagConstraints);

        txtStartProdCalc.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStartProdCalc.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panDebitProdData.add(txtStartProdCalc, gridBagConstraints);

        cboStartProdCalc.setMinimumSize(new java.awt.Dimension(100, 21));
        cboStartProdCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStartProdCalcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panDebitProdData.add(cboStartProdCalc, gridBagConstraints);

        lblEndProdCalc.setText("End Day of Product Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDebitProdData.add(lblEndProdCalc, gridBagConstraints);

        txtEndProdCalc.setMinimumSize(new java.awt.Dimension(50, 21));
        txtEndProdCalc.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panDebitProdData.add(txtEndProdCalc, gridBagConstraints);

        cboEndProdCalc.setMinimumSize(new java.awt.Dimension(100, 21));
        cboEndProdCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEndProdCalcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panDebitProdData.add(cboEndProdCalc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntRec.add(panDebitProdData, gridBagConstraints);

        sptIntReceivable.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntRec.add(sptIntReceivable, gridBagConstraints);

        tabOperativeAcctProduct.addTab("Interest Receivable", panIntRec);

        panIntPay.setLayout(new java.awt.GridBagLayout());

        sptCrInt.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptCrInt.setMinimumSize(new java.awt.Dimension(5, 5));
        sptCrInt.setPreferredSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 3);
        panIntPay.add(sptCrInt, gridBagConstraints);

        panIntPayRate.setMinimumSize(new java.awt.Dimension(360, 312));
        panIntPayRate.setPreferredSize(new java.awt.Dimension(360, 312));
        panIntPayRate.setLayout(new java.awt.GridBagLayout());

        panCrIntGiven.setLayout(new java.awt.GridBagLayout());

        rdoCrIntGiven.add(rdoCrIntGiven_Yes);
        rdoCrIntGiven_Yes.setText("Yes");
        rdoCrIntGiven_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCrIntGiven_YesActionPerformed(evt);
            }
        });
        panCrIntGiven.add(rdoCrIntGiven_Yes, new java.awt.GridBagConstraints());

        rdoCrIntGiven.add(rdoCrIntGiven_No);
        rdoCrIntGiven_No.setText("No");
        rdoCrIntGiven_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCrIntGiven_NoActionPerformed(evt);
            }
        });
        panCrIntGiven.add(rdoCrIntGiven_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(panCrIntGiven, gridBagConstraints);

        lblCrIntGiven.setText("Credit Interest to be Given");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblCrIntGiven, gridBagConstraints);

        panMinCrIntRate.setLayout(new java.awt.GridBagLayout());

        txtMinCrIntRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinCrIntRate.setPreferredSize(new java.awt.Dimension(50, 21));
        panMinCrIntRate.add(txtMinCrIntRate, new java.awt.GridBagConstraints());

        lblPer5.setText("%");
        panMinCrIntRate.add(lblPer5, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(panMinCrIntRate, gridBagConstraints);

        lblMinCrIntRate.setText("Minimum Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblMinCrIntRate, gridBagConstraints);

        panMaxDrIntRate1.setLayout(new java.awt.GridBagLayout());

        txtMaxCrIntRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxCrIntRate.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMaxCrIntRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxCrIntRateFocusLost(evt);
            }
        });
        panMaxDrIntRate1.add(txtMaxCrIntRate, new java.awt.GridBagConstraints());

        lblPer6.setText("%");
        panMaxDrIntRate1.add(lblPer6, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(panMaxDrIntRate1, gridBagConstraints);

        lblMaxCrIntRate.setText("Maximum Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblMaxCrIntRate, gridBagConstraints);

        panApplDrIntRate1.setLayout(new java.awt.GridBagLayout());

        txtApplCrIntRate.setMinimumSize(new java.awt.Dimension(50, 21));
        txtApplCrIntRate.setPreferredSize(new java.awt.Dimension(50, 21));
        txtApplCrIntRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtApplCrIntRateFocusLost(evt);
            }
        });
        panApplDrIntRate1.add(txtApplCrIntRate, new java.awt.GridBagConstraints());

        lblPer7.setText("%");
        panApplDrIntRate1.add(lblPer7, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(panApplDrIntRate1, gridBagConstraints);

        lblCrIntApplicable.setText("Applicable Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblCrIntApplicable, gridBagConstraints);

        txtMinCrIntAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(txtMinCrIntAmt, gridBagConstraints);

        lblMinCrIntAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinCrIntAmt.setText("Minimum Interest Amount");
        lblMinCrIntAmt.setMinimumSize(new java.awt.Dimension(220, 15));
        lblMinCrIntAmt.setPreferredSize(new java.awt.Dimension(220, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblMinCrIntAmt, gridBagConstraints);

        txtMaxCrIntAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxCrIntAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxCrIntAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(txtMaxCrIntAmt, gridBagConstraints);

        lblMaxCrIntAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaxCrIntAmt.setText("Maximum Interest Amount");
        lblMaxCrIntAmt.setMinimumSize(new java.awt.Dimension(220, 15));
        lblMaxCrIntAmt.setPreferredSize(new java.awt.Dimension(220, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblMaxCrIntAmt, gridBagConstraints);

        lblCrIntCalcFreq.setText("Credit Interest Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblCrIntCalcFreq, gridBagConstraints);

        cboCrIntCalcFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCrIntCalcFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCrIntCalcFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(cboCrIntCalcFreq, gridBagConstraints);

        lblStMonIntCalc.setText("Start Day of Interest Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblStMonIntCalc, gridBagConstraints);

        txtStMonIntCalc.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStMonIntCalc.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panIntPayRate.add(txtStMonIntCalc, gridBagConstraints);

        cboStMonIntCalc.setMinimumSize(new java.awt.Dimension(84, 21));
        cboStMonIntCalc.setPreferredSize(new java.awt.Dimension(84, 21));
        cboStMonIntCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStMonIntCalcActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panIntPayRate.add(cboStMonIntCalc, gridBagConstraints);

        lblIntCalcEndMon.setText("End Day of Interest Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblIntCalcEndMon, gridBagConstraints);

        txtIntCalcEndMon.setMinimumSize(new java.awt.Dimension(50, 21));
        txtIntCalcEndMon.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panIntPayRate.add(txtIntCalcEndMon, gridBagConstraints);

        cboIntCalcEndMon.setMinimumSize(new java.awt.Dimension(84, 21));
        cboIntCalcEndMon.setPreferredSize(new java.awt.Dimension(84, 21));
        cboIntCalcEndMon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntCalcEndMonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panIntPayRate.add(cboIntCalcEndMon, gridBagConstraints);

        cboCrIntApplFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(cboCrIntApplFreq, gridBagConstraints);

        lblCrIntApplFreq.setText("Credit Interest Application Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblCrIntApplFreq, gridBagConstraints);

        txtMinbalForInt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(txtMinbalForInt, gridBagConstraints);

        lblIMinBalForInts.setText("Min Balance For Interest ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayRate.add(lblIMinBalForInts, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 4, 4);
        panIntPay.add(panIntPayRate, gridBagConstraints);

        panIntPayProd.setMinimumSize(new java.awt.Dimension(425, 296));
        panIntPayProd.setPreferredSize(new java.awt.Dimension(425, 296));
        panIntPayProd.setLayout(new java.awt.GridBagLayout());

        lblLastIntCalcDateCR.setText("Last Interest Calculation Date - Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblLastIntCalcDateCR, gridBagConstraints);

        tdtLastIntCalcDateCR.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLastIntCalcDateCR.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(tdtLastIntCalcDateCR, gridBagConstraints);

        lblLastIntApplDateCr.setText("Last Interest Application Date - Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblLastIntApplDateCr, gridBagConstraints);

        tdtLastIntApplDateCr.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLastIntApplDateCr.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(tdtLastIntApplDateCr, gridBagConstraints);

        lblCreditProductRoundOff.setText("Credit Product Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblCreditProductRoundOff, gridBagConstraints);

        cboCreditProductRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(cboCreditProductRoundOff, gridBagConstraints);

        lblCreditIntRoundOff.setText("Credit Interest Round Off");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblCreditIntRoundOff, gridBagConstraints);

        cboCreditIntRoundOff.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(cboCreditIntRoundOff, gridBagConstraints);

        panCreditComp.setLayout(new java.awt.GridBagLayout());

        rdoCreditComp.add(rdoCreditComp_Yes);
        rdoCreditComp_Yes.setText("Yes");
        rdoCreditComp_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditComp_YesActionPerformed(evt);
            }
        });
        panCreditComp.add(rdoCreditComp_Yes, new java.awt.GridBagConstraints());

        rdoCreditComp.add(rdoCreditComp_No);
        rdoCreditComp_No.setText("No");
        rdoCreditComp_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditComp_NoActionPerformed(evt);
            }
        });
        panCreditComp.add(rdoCreditComp_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(panCreditComp, gridBagConstraints);

        lblCreditComp.setText("Credit Compound?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblCreditComp, gridBagConstraints);

        cboCreditCompIntCalcFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(cboCreditCompIntCalcFreq, gridBagConstraints);

        lblCreditCompIntCalcFreq.setText("Credit Compound Interest Calculation Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblCreditCompIntCalcFreq, gridBagConstraints);

        cboProdFreqCr.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdFreqCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdFreqCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(cboProdFreqCr, gridBagConstraints);

        lblProdFreqCr.setText("Product Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblProdFreqCr, gridBagConstraints);

        lblStDayProdCalcSBCrInt.setText("Start Day of Product Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblStDayProdCalcSBCrInt, gridBagConstraints);

        txtStDayProdCalcSBCrInt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStDayProdCalcSBCrInt.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panIntPayProd.add(txtStDayProdCalcSBCrInt, gridBagConstraints);

        cboStDayProdCalcSBCrInt.setMinimumSize(new java.awt.Dimension(84, 21));
        cboStDayProdCalcSBCrInt.setPreferredSize(new java.awt.Dimension(84, 21));
        cboStDayProdCalcSBCrInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboStDayProdCalcSBCrIntActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panIntPayProd.add(cboStDayProdCalcSBCrInt, gridBagConstraints);

        lblEndDayProdCalcSBCrInt.setText("End Day of Product Calculation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblEndDayProdCalcSBCrInt, gridBagConstraints);

        txtEndDayProdCalcSBCrInt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtEndDayProdCalcSBCrInt.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        panIntPayProd.add(txtEndDayProdCalcSBCrInt, gridBagConstraints);

        cboEndDayProdCalcSBCrInt.setMinimumSize(new java.awt.Dimension(84, 21));
        cboEndDayProdCalcSBCrInt.setPreferredSize(new java.awt.Dimension(84, 21));
        cboEndDayProdCalcSBCrInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboEndDayProdCalcSBCrIntActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panIntPayProd.add(cboEndDayProdCalcSBCrInt, gridBagConstraints);

        lblCalcCriteria.setText("Calculation Criteria");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(lblCalcCriteria, gridBagConstraints);

        cboCalcCriteria.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPayProd.add(cboCalcCriteria, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntPay.add(panIntPayProd, gridBagConstraints);

        tabOperativeAcctProduct.addTab("Interest Payable", panIntPay);

        panCharges.setLayout(new java.awt.GridBagLayout());

        panChargesData.setMaximumSize(new java.awt.Dimension(400, 401));
        panChargesData.setMinimumSize(new java.awt.Dimension(400, 401));
        panChargesData.setPreferredSize(new java.awt.Dimension(400, 401));
        panChargesData.setLayout(new java.awt.GridBagLayout());

        lblInOpAcChrg.setText("In-Operative A/c Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 66, 0, 0);
        panChargesData.add(lblInOpAcChrg, gridBagConstraints);

        txtlInOpAcChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtlInOpAcChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtlInOpAcChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        panChargesData.add(txtlInOpAcChrg, gridBagConstraints);

        lblInOpAcChrgPd.setText("In-Operative A/c Charges Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 82, 0, 0);
        panChargesData.add(lblInOpAcChrgPd, gridBagConstraints);

        cboInOpAcChrgPd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panChargesData.add(cboInOpAcChrgPd, gridBagConstraints);

        lblChrgPreClosure.setText("Charges for Premature Closure (for New A/c)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panChargesData.add(lblChrgPreClosure, gridBagConstraints);

        txtChrgPreClosure.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panChargesData.add(txtChrgPreClosure, gridBagConstraints);

        lblAcClosingChrg.setText("Account Closing Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 120, 0, 0);
        panChargesData.add(lblAcClosingChrg, gridBagConstraints);

        txtAcClosingChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcClosingChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcClosingChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panChargesData.add(txtAcClosingChrg, gridBagConstraints);

        lblChrgMiscServChrg.setText("Misc. Service Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 137, 0, 0);
        panChargesData.add(lblChrgMiscServChrg, gridBagConstraints);

        txtChrgMiscServChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChrgMiscServChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChrgMiscServChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panChargesData.add(txtChrgMiscServChrg, gridBagConstraints);

        panNonMainMinBalCharges.setLayout(new java.awt.GridBagLayout());

        rdoNonMainMinBalCharges.add(rdoNonMainMinBalChrg_Yes);
        rdoNonMainMinBalChrg_Yes.setText("Yes");
        rdoNonMainMinBalChrg_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNonMainMinBalChrg_YesActionPerformed(evt);
            }
        });
        panNonMainMinBalCharges.add(rdoNonMainMinBalChrg_Yes, new java.awt.GridBagConstraints());

        rdoNonMainMinBalCharges.add(rdoNonMainMinBalChrg_No);
        rdoNonMainMinBalChrg_No.setText("No");
        rdoNonMainMinBalChrg_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNonMainMinBalChrg_NoActionPerformed(evt);
            }
        });
        panNonMainMinBalCharges.add(rdoNonMainMinBalChrg_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panChargesData.add(panNonMainMinBalCharges, gridBagConstraints);

        lblNonMainMinBalCharges.setText("Non-Maintenance Min Bal Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 66, 0, 0);
        panChargesData.add(lblNonMainMinBalCharges, gridBagConstraints);

        panMinBalAmt.setMinimumSize(new java.awt.Dimension(300, 30));
        panMinBalAmt.setPreferredSize(new java.awt.Dimension(300, 47));
        panMinBalAmt.setLayout(new java.awt.GridBagLayout());

        txtMinBalAmt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinBalAmt.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMinBalAmt.add(txtMinBalAmt, gridBagConstraints);

        cboMinBalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panMinBalAmt.add(cboMinBalAmt, gridBagConstraints);

        lblMinBalAmt.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 6);
        panMinBalAmt.add(lblMinBalAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 11;
        gridBagConstraints.ipadx = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 75, 0, 23);
        panChargesData.add(panMinBalAmt, gridBagConstraints);

        panStatCharges.setLayout(new java.awt.GridBagLayout());

        rdoStatChargesCh.add(rdoStatCharges_Yes);
        rdoStatCharges_Yes.setText("Yes");
        rdoStatCharges_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStatCharges_YesActionPerformed(evt);
            }
        });
        panStatCharges.add(rdoStatCharges_Yes, new java.awt.GridBagConstraints());

        rdoStatChargesCh.add(rdoStatCharges_No);
        rdoStatCharges_No.setText("No");
        rdoStatCharges_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStatCharges_NoActionPerformed(evt);
            }
        });
        panStatCharges.add(rdoStatCharges_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 9, 0, 0);
        panChargesData.add(panStatCharges, gridBagConstraints);

        lblStatCharges.setText("Statement Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 155, 0, 0);
        panChargesData.add(lblStatCharges, gridBagConstraints);

        panStatChargesChr.setMinimumSize(new java.awt.Dimension(260, 30));
        panStatChargesChr.setPreferredSize(new java.awt.Dimension(260, 30));
        panStatChargesChr.setLayout(new java.awt.GridBagLayout());

        txtStatChargesChr.setMinimumSize(new java.awt.Dimension(50, 21));
        txtStatChargesChr.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panStatChargesChr.add(txtStatChargesChr, gridBagConstraints);

        cboStatChargesChr.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        panStatChargesChr.add(cboStatChargesChr, gridBagConstraints);

        lblStatChargesChrg.setText("Statement Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 6);
        panStatChargesChr.add(lblStatChargesChrg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 70, 0, 0);
        panChargesData.add(panStatChargesChr, gridBagConstraints);

        panChkIssuedChrg.setLayout(new java.awt.GridBagLayout());

        rdoChkIssuedChrgCh.add(rdoChkIssuedChrgCh_Yes);
        rdoChkIssuedChrgCh_Yes.setText("Yes");
        rdoChkIssuedChrgCh_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChkIssuedChrgCh_YesActionPerformed(evt);
            }
        });
        panChkIssuedChrg.add(rdoChkIssuedChrgCh_Yes, new java.awt.GridBagConstraints());

        rdoChkIssuedChrgCh.add(rdoChkIssuedChrgCh_No);
        rdoChkIssuedChrgCh_No.setText("No");
        rdoChkIssuedChrgCh_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChkIssuedChrgCh_NoActionPerformed(evt);
            }
        });
        panChkIssuedChrg.add(rdoChkIssuedChrgCh_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panChargesData.add(panChkIssuedChrg, gridBagConstraints);

        lblChkIssuedChrg.setText("Chequebooks Issued Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 94, 0, 0);
        panChargesData.add(lblChkIssuedChrg, gridBagConstraints);

        lblChkBkIssueChrgPL.setText("Chequebook Issue Charges (per Leaf)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 50, 0, 0);
        panChargesData.add(lblChkBkIssueChrgPL, gridBagConstraints);

        txtChkBkIssueChrgPL.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panChargesData.add(txtChkBkIssueChrgPL, gridBagConstraints);

        rdoStopPaymentCharges.setLayout(new java.awt.GridBagLayout());

        rdoStopPaymentChrg.add(rdoStopPaymentChrg_Yes);
        rdoStopPaymentChrg_Yes.setText("Yes");
        rdoStopPaymentChrg_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStopPaymentChrg_YesActionPerformed(evt);
            }
        });
        rdoStopPaymentCharges.add(rdoStopPaymentChrg_Yes, new java.awt.GridBagConstraints());

        rdoStopPaymentChrg.add(rdoStopPaymentChrg_No);
        rdoStopPaymentChrg_No.setText("No");
        rdoStopPaymentChrg_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoStopPaymentChrg_NoActionPerformed(evt);
            }
        });
        rdoStopPaymentCharges.add(rdoStopPaymentChrg_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panChargesData.add(rdoStopPaymentCharges, gridBagConstraints);

        lblStopPaymentCharges.setText("Stop Payment Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 132, 0, 0);
        panChargesData.add(lblStopPaymentCharges, gridBagConstraints);

        lblStopPayChrg.setText("Stop Payment Charge");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 137, 0, 0);
        panChargesData.add(lblStopPayChrg, gridBagConstraints);

        txtStopPayChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 11, 0);
        panChargesData.add(txtStopPayChrg, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -16;
        gridBagConstraints.ipady = -16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 0, 0);
        panCharges.add(panChargesData, gridBagConstraints);

        panFolioData.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Folio Charge", 0, 0, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        panFolioData.setLayout(new java.awt.GridBagLayout());

        panFolioChargeAppl.setLayout(new java.awt.GridBagLayout());

        rdoFolioChargeAppl.add(rdoFolioChargeAppl_Yes);
        rdoFolioChargeAppl_Yes.setText("Yes");
        rdoFolioChargeAppl_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFolioChargeAppl_YesActionPerformed(evt);
            }
        });
        panFolioChargeAppl.add(rdoFolioChargeAppl_Yes, new java.awt.GridBagConstraints());

        rdoFolioChargeAppl.add(rdoFolioChargeAppl_No);
        rdoFolioChargeAppl_No.setText("No");
        rdoFolioChargeAppl_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFolioChargeAppl_NoActionPerformed(evt);
            }
        });
        panFolioChargeAppl.add(rdoFolioChargeAppl_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolioData.add(panFolioChargeAppl, gridBagConstraints);

        lblFolioChargeAppl.setText("Folio Charges Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 4, 4);
        panFolioData.add(lblFolioChargeAppl, gridBagConstraints);

        lblRatePerFolio.setText("Rate per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panFolioData.add(lblRatePerFolio, gridBagConstraints);

        txtRatePerFolio.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 4, 4);
        panFolioData.add(txtRatePerFolio, gridBagConstraints);

        lblNoEntryPerFolio.setText("No. of Entries per Folio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(lblNoEntryPerFolio, gridBagConstraints);

        txtNoEntryPerFolio.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(txtNoEntryPerFolio, gridBagConstraints);

        panFolioToChargeOn.setMinimumSize(new java.awt.Dimension(280, 40));
        panFolioToChargeOn.setPreferredSize(new java.awt.Dimension(280, 40));
        panFolioToChargeOn.setRequestFocusEnabled(false);
        panFolioToChargeOn.setLayout(new java.awt.GridBagLayout());

        rdoFolioToChargeOn.add(rdoFolioToChargeOn_Credit);
        rdoFolioToChargeOn_Credit.setText("Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panFolioToChargeOn.add(rdoFolioToChargeOn_Credit, gridBagConstraints);

        rdoFolioToChargeOn.add(rdoFolioToChargeOn_Debit);
        rdoFolioToChargeOn_Debit.setText("Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panFolioToChargeOn.add(rdoFolioToChargeOn_Debit, gridBagConstraints);

        rdoFolioToChargeOn_Both.setText("Both");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panFolioToChargeOn.add(rdoFolioToChargeOn_Both, gridBagConstraints);

        lblToChargeOnApplFreq.setText("To Charge On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioToChargeOn.add(lblToChargeOnApplFreq, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panFolioData.add(panFolioToChargeOn, gridBagConstraints);

        lblFolioChrgApplFreq.setText("Folio Charges Applicability Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(lblFolioChrgApplFreq, gridBagConstraints);

        cboFolioChrgApplFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        cboFolioChrgApplFreq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboFolioChrgApplFreqActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(cboFolioChrgApplFreq, gridBagConstraints);

        cboToCollectFolioChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(cboToCollectFolioChrg, gridBagConstraints);

        lblToCollectFolioChrg.setText("To Collect Folio Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(lblToCollectFolioChrg, gridBagConstraints);

        panToChargeOnApplFreq.setMinimumSize(new java.awt.Dimension(280, 45));
        panToChargeOnApplFreq.setPreferredSize(new java.awt.Dimension(280, 45));
        panToChargeOnApplFreq.setRequestFocusEnabled(false);
        panToChargeOnApplFreq.setLayout(new java.awt.GridBagLayout());

        rdoToChargeOnApplFreq.add(rdoToChargeOnApplFreq_Manual);
        rdoToChargeOnApplFreq_Manual.setText("Manual");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panToChargeOnApplFreq.add(rdoToChargeOnApplFreq_Manual, gridBagConstraints);

        rdoToChargeOnApplFreq.add(rdoToChargeOnApplFreq_System);
        rdoToChargeOnApplFreq_System.setText("System");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panToChargeOnApplFreq.add(rdoToChargeOnApplFreq_System, gridBagConstraints);

        rdoToChargeOnApplFreq_Both.setText("Both");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panToChargeOnApplFreq.add(rdoToChargeOnApplFreq_Both, gridBagConstraints);

        lblFolioToChargeOn.setText("To Charge On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panToChargeOnApplFreq.add(lblFolioToChargeOn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panFolioData.add(panToChargeOnApplFreq, gridBagConstraints);

        cboIncompFolioROffFreq.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(cboIncompFolioROffFreq, gridBagConstraints);

        lblIncompFolioROffFreq.setText("Incomplete Folio Rounding Off Freq");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(lblIncompFolioROffFreq, gridBagConstraints);

        tdtLastFolioAppliedDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtLastFolioAppliedDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(tdtLastFolioAppliedDate, gridBagConstraints);

        tdtNextFolioAppliedDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtNextFolioAppliedDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(tdtNextFolioAppliedDt, gridBagConstraints);

        lblFolioChargeLastAppliedDt.setText("Folio Charges Last App Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(lblFolioChargeLastAppliedDt, gridBagConstraints);

        lblFolioChargeNextAppDt.setText("Folio Charges Next App Dt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panFolioData.add(lblFolioChargeNextAppDt, gridBagConstraints);

        rdoFolioChargeGroup.add(rdoFolioChargeVariable);
        rdoFolioChargeVariable.setText("Variable");
        rdoFolioChargeVariable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFolioChargeVariableActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panFolioData.add(rdoFolioChargeVariable, gridBagConstraints);

        rdoFolioChargeGroup.add(rdoFoliochargeFixed);
        rdoFoliochargeFixed.setText("Fixed");
        rdoFoliochargeFixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFoliochargeFixedActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panFolioData.add(rdoFoliochargeFixed, gridBagConstraints);

        lblFolioRestriction.setText("Should not Collect Before");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        panFolioData.add(lblFolioRestriction, gridBagConstraints);

        txtFolioRestrictionPriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtFolioRestrictionPriod.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panFolioData.add(txtFolioRestrictionPriod, gridBagConstraints);

        cboFolioChargedBefore.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panFolioData.add(cboFolioChargedBefore, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 57, 0, 0);
        panCharges.add(panFolioData, gridBagConstraints);

        panReturnChargesData.setLayout(new java.awt.GridBagLayout());

        lblChkRetChrOutward.setText("Cheque Return Charges (Outward)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(lblChkRetChrOutward, gridBagConstraints);

        txtChkRetChrOutward.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChkRetChrOutward.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChkRetChrOutwardFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(txtChkRetChrOutward, gridBagConstraints);

        lblChkRetChrgIn.setText("Cheque Return Charges (Inward)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(lblChkRetChrgIn, gridBagConstraints);

        txtChkRetChrgIn.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChkRetChrgIn.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChkRetChrgInFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(txtChkRetChrgIn, gridBagConstraints);

        lblAcctOpenCharges.setText("Account Opening Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(lblAcctOpenCharges, gridBagConstraints);

        txtAcctOpenCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctOpenCharges.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctOpenChargesFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(txtAcctOpenCharges, gridBagConstraints);

        lblExcessFreeWDChrgPT.setText("Excess Free Withdrawals Charge (per Transaction)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(lblExcessFreeWDChrgPT, gridBagConstraints);

        txtExcessFreeWDChrgPT.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExcessFreeWDChrgPT.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExcessFreeWDChrgPTFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panReturnChargesData.add(txtExcessFreeWDChrgPT, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 6, 0, 0);
        panCharges.add(panReturnChargesData, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCharges.add(sptFolio, gridBagConstraints);

        sptFolioVertical.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panCharges.add(sptFolioVertical, gridBagConstraints);

        panDebitWithdrawalCharge.setLayout(new java.awt.GridBagLayout());

        lblDebitWithsrawalChargePeriod.setText("Charge Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 82, 0, 0);
        panDebitWithdrawalCharge.add(lblDebitWithsrawalChargePeriod, gridBagConstraints);

        txtDebiWithdrawalChargePeriod.setAllowNumber(true);
        txtDebiWithdrawalChargePeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebiWithdrawalChargePeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebiWithdrawalChargePeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 73);
        panDebitWithdrawalCharge.add(txtDebiWithdrawalChargePeriod, gridBagConstraints);

        lblDebitChargeType.setText("Debit Charge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 57, 0, 0);
        panDebitWithdrawalCharge.add(lblDebitChargeType, gridBagConstraints);

        lblDebitChargeTypeRate.setText("Debit Charge Type Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 38, 0, 0);
        panDebitWithdrawalCharge.add(lblDebitChargeTypeRate, gridBagConstraints);

        txtDebitChargeTypeRate.setAllowNumber(true);
        txtDebitChargeTypeRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitChargeTypeRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDebitChargeTypeRateActionPerformed(evt);
            }
        });
        txtDebitChargeTypeRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebitChargeTypeRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 4, 21, 0);
        panDebitWithdrawalCharge.add(txtDebitChargeTypeRate, gridBagConstraints);

        chkDebitWithdrawalCharge.setText("Debit Withdrawal Charge");
        chkDebitWithdrawalCharge.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkDebitWithdrawalChargeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        panDebitWithdrawalCharge.add(chkDebitWithdrawalCharge, gridBagConstraints);

        cboDebitChargeType.setPreferredSize(new java.awt.Dimension(32, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 73;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 6, 0, 73);
        panDebitWithdrawalCharge.add(cboDebitChargeType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 27, 10, 0);
        panCharges.add(panDebitWithdrawalCharge, gridBagConstraints);

        tabOperativeAcctProduct.addTab("Charges", panCharges);

        panSpecialItem.setLayout(new java.awt.GridBagLayout());

        panSpecialDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(" Special Details "));
        panSpecialDetails.setMinimumSize(new java.awt.Dimension(375, 160));
        panSpecialDetails.setPreferredSize(new java.awt.Dimension(375, 160));
        panSpecialDetails.setLayout(new java.awt.GridBagLayout());

        lblLinkFlexiAcct.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblLinkFlexiAcct.setText("Whether linked to Flexi Acc");
        lblLinkFlexiAcct.setMaximumSize(new java.awt.Dimension(190, 15));
        lblLinkFlexiAcct.setMinimumSize(new java.awt.Dimension(190, 15));
        lblLinkFlexiAcct.setPreferredSize(new java.awt.Dimension(190, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialDetails.add(lblLinkFlexiAcct, gridBagConstraints);

        lblMinBal1FlexiDep.setText("Min. Balance1 for Flexi Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialDetails.add(lblMinBal1FlexiDep, gridBagConstraints);

        lblMinBal2FlexiDep.setText("Min. Balance2 for Flexi Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialDetails.add(lblMinBal2FlexiDep, gridBagConstraints);

        lblFlexiHappen.setText(" Whether Flexi will Happen in");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialDetails.add(lblFlexiHappen, gridBagConstraints);

        panFlexiHappen.setLayout(new java.awt.GridBagLayout());

        rdoFlexiHappen.add(rdoFlexiHappen_SB);
        rdoFlexiHappen_SB.setText("SB");
        panFlexiHappen.add(rdoFlexiHappen_SB, new java.awt.GridBagConstraints());

        rdoFlexiHappen.add(rdoFlexiHappen_TD);
        rdoFlexiHappen_TD.setText("New TD Account");
        rdoFlexiHappen_TD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoFlexiHappen_TDActionPerformed(evt);
            }
        });
        panFlexiHappen.add(rdoFlexiHappen_TD, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panSpecialDetails.add(panFlexiHappen, gridBagConstraints);

        panLinkFlexiAcct.setLayout(new java.awt.GridBagLayout());

        rdoLinkFlexiAcct.add(rdoLinkFlexiAcct_Yes);
        rdoLinkFlexiAcct_Yes.setText("Yes");
        rdoLinkFlexiAcct_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLinkFlexiAcct_YesActionPerformed(evt);
            }
        });
        panLinkFlexiAcct.add(rdoLinkFlexiAcct_Yes, new java.awt.GridBagConstraints());

        rdoLinkFlexiAcct.add(rdoLinkFlexiAcct_No);
        rdoLinkFlexiAcct_No.setText("No");
        rdoLinkFlexiAcct_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLinkFlexiAcct_NoActionPerformed(evt);
            }
        });
        panLinkFlexiAcct.add(rdoLinkFlexiAcct_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panSpecialDetails.add(panLinkFlexiAcct, gridBagConstraints);

        txtMinBal1FlexiDep.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panSpecialDetails.add(txtMinBal1FlexiDep, gridBagConstraints);

        txtMinBal2FlexiDep.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panSpecialDetails.add(txtMinBal2FlexiDep, gridBagConstraints);

        lblFlexiTD.setText("Flexi Term Deposit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSpecialDetails.add(lblFlexiTD, gridBagConstraints);

        cboFlexiTD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panSpecialDetails.add(cboFlexiTD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 207, 0, 0);
        panSpecialItem.add(panSpecialDetails, gridBagConstraints);

        panMiscDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(" Miscellaneous Details "));
        panMiscDetails.setMinimumSize(new java.awt.Dimension(363, 349));
        panMiscDetails.setPreferredSize(new java.awt.Dimension(363, 349));
        panMiscDetails.setLayout(new java.awt.GridBagLayout());

        lblATMIssued.setText("ATM Card Issued?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblATMIssued, gridBagConstraints);

        lblMinATMBal.setText("Minimum Balance for ATM");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblMinATMBal, gridBagConstraints);

        lblCreditCdIssued.setText("Credit Card Issued?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblCreditCdIssued, gridBagConstraints);

        lblMinBalCreditCd.setText("Minimum Balance for Credit Card");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 4, 4);
        panMiscDetails.add(lblMinBalCreditCd, gridBagConstraints);

        panATMIssued.setMinimumSize(new java.awt.Dimension(148, 23));
        panATMIssued.setPreferredSize(new java.awt.Dimension(148, 23));
        panATMIssued.setLayout(new java.awt.GridBagLayout());

        rdoATMIssued.add(rdoATMIssued_Yes);
        rdoATMIssued_Yes.setText("Yes");
        rdoATMIssued_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoATMIssued_YesActionPerformed(evt);
            }
        });
        panATMIssued.add(rdoATMIssued_Yes, new java.awt.GridBagConstraints());

        rdoATMIssued.add(rdoATMIssued_No);
        rdoATMIssued_No.setText("No");
        rdoATMIssued_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoATMIssued_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        panATMIssued.add(rdoATMIssued_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panMiscDetails.add(panATMIssued, gridBagConstraints);

        panABBAllowed.setLayout(new java.awt.GridBagLayout());

        rdoABBAllowed.add(rdoABBAllowed_Yes);
        rdoABBAllowed_Yes.setText("Yes");
        rdoABBAllowed_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoABBAllowed_YesActionPerformed(evt);
            }
        });
        panABBAllowed.add(rdoABBAllowed_Yes, new java.awt.GridBagConstraints());

        rdoABBAllowed.add(rdoABBAllowed_No);
        rdoABBAllowed_No.setText("No");
        rdoABBAllowed_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoABBAllowed_NoActionPerformed(evt);
            }
        });
        panABBAllowed.add(rdoABBAllowed_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panMiscDetails.add(panABBAllowed, gridBagConstraints);

        panMobBankClient.setLayout(new java.awt.GridBagLayout());

        rdoMobBankClient.add(rdoMobBankClient_Yes);
        rdoMobBankClient_Yes.setText("Yes");
        rdoMobBankClient_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMobBankClient_YesActionPerformed(evt);
            }
        });
        panMobBankClient.add(rdoMobBankClient_Yes, new java.awt.GridBagConstraints());

        rdoMobBankClient.add(rdoMobBankClient_No);
        rdoMobBankClient_No.setText("No");
        rdoMobBankClient_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoMobBankClient_NoActionPerformed(evt);
            }
        });
        panMobBankClient.add(rdoMobBankClient_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panMiscDetails.add(panMobBankClient, gridBagConstraints);

        panIVRSProvided.setLayout(new java.awt.GridBagLayout());

        rdoIVRSProvided.add(rdoIVRSProvided_Yes);
        rdoIVRSProvided_Yes.setText("Yes");
        rdoIVRSProvided_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIVRSProvided_YesActionPerformed(evt);
            }
        });
        panIVRSProvided.add(rdoIVRSProvided_Yes, new java.awt.GridBagConstraints());

        rdoIVRSProvided.add(rdoIVRSProvided_No);
        rdoIVRSProvided_No.setText("No");
        rdoIVRSProvided_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIVRSProvided_NoActionPerformed(evt);
            }
        });
        panIVRSProvided.add(rdoIVRSProvided_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panMiscDetails.add(panIVRSProvided, gridBagConstraints);

        panDebitCdIssued.setLayout(new java.awt.GridBagLayout());

        rdoDebitCdIssued.add(rdoDebitCdIssued_Yes);
        rdoDebitCdIssued_Yes.setText("Yes");
        rdoDebitCdIssued_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitCdIssued_YesActionPerformed(evt);
            }
        });
        panDebitCdIssued.add(rdoDebitCdIssued_Yes, new java.awt.GridBagConstraints());

        rdoDebitCdIssued.add(rdoDebitCdIssued_No);
        rdoDebitCdIssued_No.setText("No");
        rdoDebitCdIssued_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDebitCdIssued_NoActionPerformed(evt);
            }
        });
        panDebitCdIssued.add(rdoDebitCdIssued_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panMiscDetails.add(panDebitCdIssued, gridBagConstraints);

        panCreditCdIssued.setLayout(new java.awt.GridBagLayout());

        rdoCreditCdIssued.add(rdoCreditCdIssued_Yes);
        rdoCreditCdIssued_Yes.setText("Yes");
        rdoCreditCdIssued_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditCdIssued_YesActionPerformed(evt);
            }
        });
        panCreditCdIssued.add(rdoCreditCdIssued_Yes, new java.awt.GridBagConstraints());

        rdoCreditCdIssued.add(rdoCreditCdIssued_No);
        rdoCreditCdIssued_No.setText("No");
        rdoCreditCdIssued_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoCreditCdIssued_NoActionPerformed(evt);
            }
        });
        panCreditCdIssued.add(rdoCreditCdIssued_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 4);
        panMiscDetails.add(panCreditCdIssued, gridBagConstraints);

        txtMinATMBal.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panMiscDetails.add(txtMinATMBal, gridBagConstraints);

        txtMinBalCreditCd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panMiscDetails.add(txtMinBalCreditCd, gridBagConstraints);

        txtMinBalDebitCards.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMiscDetails.add(txtMinBalDebitCards, gridBagConstraints);

        txtMinBalIVRS.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMiscDetails.add(txtMinBalIVRS, gridBagConstraints);

        txtMinMobBank.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 4);
        panMiscDetails.add(txtMinMobBank, gridBagConstraints);

        txtMinBalABB.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panMiscDetails.add(txtMinBalABB, gridBagConstraints);

        lblMinBalABB.setText("Minimum Balance for ABB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblMinBalABB, gridBagConstraints);

        lblDebitCardIssued.setText("Debit Card Issued?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblDebitCardIssued, gridBagConstraints);

        lblMinBalDebitCards.setText("Minimum Balance for Debit Card");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblMinBalDebitCards, gridBagConstraints);

        lblIVRSProvided.setText("Is IVRS provided?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblIVRSProvided, gridBagConstraints);

        lblMinBalIVRS.setText("Min. Balance for IVRS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblMinBalIVRS, gridBagConstraints);

        lblMobBankClient.setText("Mobile Banking Client?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblMobBankClient, gridBagConstraints);

        lblMinMobBank.setText("Min. Balance for Mobile Banking");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblMinMobBank, gridBagConstraints);

        lblABBAllowed.setText("Is Any Branch Banking Allowed?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMiscDetails.add(lblABBAllowed, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 207, 26, 0);
        panSpecialItem.add(panMiscDetails, gridBagConstraints);

        cPanel1.setLayout(new java.awt.GridBagLayout());

        cLabel1.setText("IMPS Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 12, 33);
        cPanel1.add(cLabel1, gridBagConstraints);

        txtIMPSLimit.setAllowNumber(true);
        txtIMPSLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 8, 0);
        cPanel1.add(txtIMPSLimit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(220, 10, 0, 1);
        panSpecialItem.add(cPanel1, gridBagConstraints);

        tabOperativeAcctProduct.addTab("Special Item", panSpecialItem);

        panAcctHead.setLayout(new java.awt.GridBagLayout());

        lblInOpChrg.setText("In-Operative Account Charges P & L Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 142, 0, 0);
        panAcctHead.add(lblInOpChrg, gridBagConstraints);

        lblPrematureClosureChrg.setText("Charges for Premature Closure (for new A/c) - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 119, 0, 0);
        panAcctHead.add(lblPrematureClosureChrg, gridBagConstraints);

        lblMiscServChrg.setText("Misc. Service Charges - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 244, 0, 0);
        panAcctHead.add(lblMiscServChrg, gridBagConstraints);

        lblAcctClosingChrg.setText("Account Closing Charges - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 227, 0, 0);
        panAcctHead.add(lblAcctClosingChrg, gridBagConstraints);

        lblStatChrg.setText("Statement Charges - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 262, 0, 0);
        panAcctHead.add(lblStatChrg, gridBagConstraints);

        lblFreeWDChrg.setText("Free Withdrawal Charges - P & L Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 160, 0, 0);
        panAcctHead.add(lblFreeWDChrg, gridBagConstraints);

        lblAcctDebitInt.setText("P& L Head for Account for Debit Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 205, 0, 0);
        panAcctHead.add(lblAcctDebitInt, gridBagConstraints);

        lblAcctCrInt.setText("P & L Head of Account for Credit Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 201, 0, 0);
        panAcctHead.add(lblAcctCrInt, gridBagConstraints);

        lblClearingIntAcctHd.setText("Against Clearing Interest Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 209, 0, 0);
        panAcctHead.add(lblClearingIntAcctHd, gridBagConstraints);

        lblChkBkIssueChrg.setText("Cheque Book Issue Charges - Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 178, 0, 0);
        panAcctHead.add(lblChkBkIssueChrg, gridBagConstraints);

        lblStopPaymentChrg.setText("Stop Payment Charges - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 239, 0, 0);
        panAcctHead.add(lblStopPaymentChrg, gridBagConstraints);

        lblOutwardChkRetChrg.setText("Cheque Return Charges (Outward) - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 176, 0, 0);
        panAcctHead.add(lblOutwardChkRetChrg, gridBagConstraints);

        lblInwardChkRetChrg.setText("Cheque Return Charges (Inward) - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 187, 0, 0);
        panAcctHead.add(lblInwardChkRetChrg, gridBagConstraints);

        lblAcctOpenChrg.setText("Account Opening Charges - GL Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 222, 0, 0);
        panAcctHead.add(lblAcctOpenChrg, gridBagConstraints);

        lblExcessFreeWDChrg.setText("P & L Account Head for Charges in Excess of Free Withdrawals");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 67, 0, 0);
        panAcctHead.add(lblExcessFreeWDChrg, gridBagConstraints);

        lblTaxGL.setText("Tax GL Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 309, 0, 0);
        panAcctHead.add(lblTaxGL, gridBagConstraints);

        txtInOpChrg.setAllowAll(true);
        txtInOpChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInOpChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtInOpChrgActionPerformed(evt);
            }
        });
        txtInOpChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtInOpChrgFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInOpChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 8, 0, 0);
        panAcctHead.add(txtInOpChrg, gridBagConstraints);

        txtPrematureClosureChrg.setAllowAll(true);
        txtPrematureClosureChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPrematureClosureChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrematureClosureChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panAcctHead.add(txtPrematureClosureChrg, gridBagConstraints);

        txtAcctClosingChrg.setAllowAll(true);
        txtAcctClosingChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctClosingChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctClosingChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panAcctHead.add(txtAcctClosingChrg, gridBagConstraints);

        txtMiscServChrg.setAllowAll(true);
        txtMiscServChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMiscServChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMiscServChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtMiscServChrg, gridBagConstraints);

        txtStatChrg.setAllowAll(true);
        txtStatChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStatChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStatChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtStatChrg, gridBagConstraints);

        txtFreeWDChrg.setAllowAll(true);
        txtFreeWDChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFreeWDChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFreeWDChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtFreeWDChrg, gridBagConstraints);

        txtAcctDebitInt.setAllowAll(true);
        txtAcctDebitInt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctDebitInt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctDebitIntFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtAcctDebitInt, gridBagConstraints);

        txtAcctCrInt.setAllowAll(true);
        txtAcctCrInt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctCrInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcctCrIntActionPerformed(evt);
            }
        });
        txtAcctCrInt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtAcctCrIntFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctCrIntFocusLost(evt);
            }
        });
        txtAcctCrInt.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtAcctCrIntInputMethodTextChanged(evt);
            }
        });
        txtAcctCrInt.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtAcctCrIntPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtAcctCrInt, gridBagConstraints);

        txtChkBkIssueChrg.setAllowAll(true);
        txtChkBkIssueChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChkBkIssueChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChkBkIssueChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtChkBkIssueChrg, gridBagConstraints);

        txtClearingIntAcctHd.setAllowAll(true);
        txtClearingIntAcctHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtClearingIntAcctHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClearingIntAcctHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtClearingIntAcctHd, gridBagConstraints);

        txtStopPaymentChrg.setAllowAll(true);
        txtStopPaymentChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtStopPaymentChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtStopPaymentChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtStopPaymentChrg, gridBagConstraints);

        txtOutwardChkRetChrg.setAllowAll(true);
        txtOutwardChkRetChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOutwardChkRetChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOutwardChkRetChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtOutwardChkRetChrg, gridBagConstraints);

        txtInwardChkRetChrg.setAllowAll(true);
        txtInwardChkRetChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInwardChkRetChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInwardChkRetChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtInwardChkRetChrg, gridBagConstraints);

        txtAcctOpenChrg.setAllowAll(true);
        txtAcctOpenChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctOpenChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctOpenChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtAcctOpenChrg, gridBagConstraints);

        txtExcessFreeWDChrg.setAllowAll(true);
        txtExcessFreeWDChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtExcessFreeWDChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtExcessFreeWDChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtExcessFreeWDChrg, gridBagConstraints);

        txtTaxGL.setAllowAll(true);
        txtTaxGL.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTaxGL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTaxGLFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panAcctHead.add(txtTaxGL, gridBagConstraints);

        btnInOpChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInOpChrg.setToolTipText("Select");
        btnInOpChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInOpChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInOpChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(32, 4, 0, 0);
        panAcctHead.add(btnInOpChrg, gridBagConstraints);

        btnPrematureClosureChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPrematureClosureChrg.setToolTipText("Select");
        btnPrematureClosureChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPrematureClosureChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrematureClosureChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAcctHead.add(btnPrematureClosureChrg, gridBagConstraints);

        btnAcctClosingChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctClosingChrg.setToolTipText("Select");
        btnAcctClosingChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctClosingChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctClosingChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panAcctHead.add(btnAcctClosingChrg, gridBagConstraints);

        btnMiscServChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMiscServChrg.setToolTipText("Select");
        btnMiscServChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMiscServChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMiscServChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnMiscServChrg, gridBagConstraints);

        btnAcctDebitInt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctDebitInt.setToolTipText("Select");
        btnAcctDebitInt.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctDebitInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctDebitIntActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnAcctDebitInt, gridBagConstraints);

        btnFreeWDChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFreeWDChrg.setToolTipText("Select");
        btnFreeWDChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFreeWDChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFreeWDChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnFreeWDChrg, gridBagConstraints);

        btnStatChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStatChrg.setToolTipText("Select");
        btnStatChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStatChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStatChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnStatChrg, gridBagConstraints);

        btnAcctCrInt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctCrInt.setToolTipText("Select");
        btnAcctCrInt.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctCrInt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctCrIntActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnAcctCrInt, gridBagConstraints);

        btnClearingIntAcctHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnClearingIntAcctHd.setToolTipText("Select");
        btnClearingIntAcctHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnClearingIntAcctHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearingIntAcctHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnClearingIntAcctHd, gridBagConstraints);

        btnStopPaymentChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnStopPaymentChrg.setToolTipText("Select");
        btnStopPaymentChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnStopPaymentChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopPaymentChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnStopPaymentChrg, gridBagConstraints);

        btnChkBkIssueChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChkBkIssueChrg.setToolTipText("Select");
        btnChkBkIssueChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChkBkIssueChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChkBkIssueChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnChkBkIssueChrg, gridBagConstraints);

        btnOutwardChkRetChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOutwardChkRetChrg.setToolTipText("Select");
        btnOutwardChkRetChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOutwardChkRetChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOutwardChkRetChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnOutwardChkRetChrg, gridBagConstraints);

        btnInwardChkRetChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInwardChkRetChrg.setToolTipText("Select");
        btnInwardChkRetChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInwardChkRetChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInwardChkRetChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnInwardChkRetChrg, gridBagConstraints);

        btnAcctOpenChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAcctOpenChrg.setToolTipText("Select");
        btnAcctOpenChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctOpenChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctOpenChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnAcctOpenChrg, gridBagConstraints);

        btnExcessFreeWDChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExcessFreeWDChrg.setToolTipText("Select");
        btnExcessFreeWDChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExcessFreeWDChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcessFreeWDChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnExcessFreeWDChrg, gridBagConstraints);

        btnTaxGL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTaxGL.setToolTipText("Select");
        btnTaxGL.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTaxGL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaxGLActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panAcctHead.add(btnTaxGL, gridBagConstraints);

        txtInOpChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtInOpChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(32, 0, 0, 0);
        panAcctHead.add(txtInOpChrgDesc, gridBagConstraints);

        txtPrematureClosureChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtPrematureClosureChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAcctHead.add(txtPrematureClosureChrgDesc, gridBagConstraints);

        txtAcctClosingChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtAcctClosingChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panAcctHead.add(txtAcctClosingChrgDesc, gridBagConstraints);

        txtMiscServChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtMiscServChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtMiscServChrgDesc, gridBagConstraints);

        txtStatChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtStatChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtStatChrgDesc, gridBagConstraints);

        txtFreeWDChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtFreeWDChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtFreeWDChrgDesc, gridBagConstraints);

        txtAcctDebitIntDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtAcctDebitIntDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtAcctDebitIntDesc, gridBagConstraints);

        txtAcctCrIntDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtAcctCrIntDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtAcctCrIntDesc, gridBagConstraints);

        txtClearingIntAcctHdDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtClearingIntAcctHdDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtClearingIntAcctHdDesc, gridBagConstraints);

        txtChkBkIssueChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtChkBkIssueChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtChkBkIssueChrgDesc, gridBagConstraints);

        txtStopPaymentChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtStopPaymentChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtStopPaymentChrgDesc, gridBagConstraints);

        txtOutwardChkRetChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtOutwardChkRetChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtOutwardChkRetChrgDesc, gridBagConstraints);

        txtInwardChkRetChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtInwardChkRetChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtInwardChkRetChrgDesc, gridBagConstraints);

        txtAcctOpenChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtAcctOpenChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtAcctOpenChrgDesc, gridBagConstraints);

        txtExcessFreeWDChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtExcessFreeWDChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtExcessFreeWDChrgDesc, gridBagConstraints);

        txtTaxGLDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtTaxGLDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panAcctHead.add(txtTaxGLDesc, gridBagConstraints);

        tabOperativeAcctProduct.addTab("Account Head", panAcctHead);

        panOtherAcctHead.setLayout(new java.awt.GridBagLayout());

        lblNonMainMinBalChrg.setText("Charges Non-Maintenance Minimum Balance P & L Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 53, 0, 0);
        panOtherAcctHead.add(lblNonMainMinBalChrg, gridBagConstraints);

        txtNonMainMinBalChrg.setAllowAll(true);
        txtNonMainMinBalChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNonMainMinBalChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNonMainMinBalChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 0);
        panOtherAcctHead.add(txtNonMainMinBalChrg, gridBagConstraints);

        btnInOperative.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInOperative.setToolTipText("Select");
        btnInOperative.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInOperative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInOperativeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOtherAcctHead.add(btnInOperative, gridBagConstraints);

        txtNonMainMinBalChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtNonMainMinBalChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        panOtherAcctHead.add(txtNonMainMinBalChrgDesc, gridBagConstraints);

        lblInOperative.setText("In-Operative Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 283, 0, 0);
        panOtherAcctHead.add(lblInOperative, gridBagConstraints);

        txtFolioChrg.setAllowAll(true);
        txtFolioChrg.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFolioChrg.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFolioChrgFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panOtherAcctHead.add(txtFolioChrg, gridBagConstraints);

        btnNonMainMinBalChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNonMainMinBalChrg.setToolTipText("Select");
        btnNonMainMinBalChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnNonMainMinBalChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNonMainMinBalChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 0, 0);
        panOtherAcctHead.add(btnNonMainMinBalChrg, gridBagConstraints);

        txtInOperativeDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtInOperativeDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panOtherAcctHead.add(txtInOperativeDesc, gridBagConstraints);

        lblFolioChrg.setText("Folio Charges Account P & L Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 182, 0, 0);
        panOtherAcctHead.add(lblFolioChrg, gridBagConstraints);

        txtInOperative.setAllowAll(true);
        txtInOperative.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInOperative.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInOperativeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panOtherAcctHead.add(txtInOperative, gridBagConstraints);

        btnFolioChrg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnFolioChrg.setToolTipText("Select");
        btnFolioChrg.setPreferredSize(new java.awt.Dimension(21, 21));
        btnFolioChrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFolioChrgActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOtherAcctHead.add(btnFolioChrg, gridBagConstraints);

        txtFolioChrgDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtFolioChrgDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panOtherAcctHead.add(txtFolioChrgDesc, gridBagConstraints);

        lblDebitWithdrawalChargeHead.setText("Debit Withdrawal Charge Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 208, 0, 0);
        panOtherAcctHead.add(lblDebitWithdrawalChargeHead, gridBagConstraints);

        txtDebitWithdrawalChargeHead.setAllowNumber(true);
        txtDebitWithdrawalChargeHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebitWithdrawalChargeHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panOtherAcctHead.add(txtDebitWithdrawalChargeHead, gridBagConstraints);

        btnDebitWithdrawalCharge.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitWithdrawalCharge.setToolTipText("Select");
        btnDebitWithdrawalCharge.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitWithdrawalCharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitWithdrawalChargeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOtherAcctHead.add(btnDebitWithdrawalCharge, gridBagConstraints);

        txtDebitWithdrawalChargeDesc.setAllowNumber(true);
        txtDebitWithdrawalChargeDesc.setMinimumSize(new java.awt.Dimension(150, 21));
        txtDebitWithdrawalChargeDesc.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panOtherAcctHead.add(txtDebitWithdrawalChargeDesc, gridBagConstraints);

        lblATMGL.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblATMGL.setText("ATM  Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 208, 0, 0);
        panOtherAcctHead.add(lblATMGL, gridBagConstraints);

        txtATMGL.setAllowNumber(true);
        txtATMGL.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtATMGLFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 8, 0, 0);
        panOtherAcctHead.add(txtATMGL, gridBagConstraints);

        btnATMGL.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnATMGL.setToolTipText("Select");
        btnATMGL.setPreferredSize(new java.awt.Dimension(21, 21));
        btnATMGL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnATMGLActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panOtherAcctHead.add(btnATMGL, gridBagConstraints);

        txtATMGLDisplay.setAllowNumber(true);
        txtATMGLDisplay.setMinimumSize(new java.awt.Dimension(150, 21));
        txtATMGLDisplay.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 40;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panOtherAcctHead.add(txtATMGLDisplay, gridBagConstraints);

        tabOperativeAcctProduct.addTab("Other Account Head", panOtherAcctHead);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panOperativeProduct.add(tabOperativeAcctProduct, gridBagConstraints);

        getContentPane().add(panOperativeProduct, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        panStatus.add(lblSpace1, new java.awt.GridBagConstraints());

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        panStatus.add(lblStatus, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
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

        mitSaveAs.setText("Save As");
        mitSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveAsActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSaveAs);

        mbrOperativeAcctProduct.add(mnuProcess);

        setJMenuBar(mbrOperativeAcctProduct);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtAcctCrIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcctCrIntActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtAcctCrIntActionPerformed

    private void txtAcctCrIntInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtAcctCrIntInputMethodTextChanged
        // TODO add your handling code here:
   
    }//GEN-LAST:event_txtAcctCrIntInputMethodTextChanged

    private void txtAcctCrIntPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtAcctCrIntPropertyChange
        // TODO add your handling code here:
//          String achdid="";
//        if(!txtAcctCrInt.getText().equals(""))
//        {
//        achdid = txtAcctCrInt.getText();
//        System.out.println("achdidachdidachdidachdidachdid"+achdid); 
//        }
//        String accHdDesc = getAccHdDesc(achdid);
//        txtAcctCrIntDesc.setText(accHdDesc);
    }//GEN-LAST:event_txtAcctCrIntPropertyChange

    private void txtAcctCrIntFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctCrIntFocusGained
        // TODO add your handling code here:
//         String achdid="";
//        if(!txtAcctCrInt.getText().equals(""))
//        {
//        achdid = txtAcctCrInt.getText();
//        System.out.println("achdidachdidachdidachdidachdid"+achdid); 
//        }
//        String accHdDesc = getAccHdDesc(achdid);
//        txtAcctCrIntDesc.setText(accHdDesc);
    }//GEN-LAST:event_txtAcctCrIntFocusGained

    private void txtInOpChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtInOpChrgActionPerformed
        // TODO add your handling code here:
//         String achdid="";
//        if(!txtInOpChrg.getText().equals(""))
//        {
//        achdid = txtInOpChrg.getText();
//        System.out.println("achdidachdidachdidachdidachdid"+achdid);  
//        }
//        String accHdDesc = getAccHdDesc(achdid);
        
    }//GEN-LAST:event_txtInOpChrgActionPerformed

   
    private void txtInOpChrgFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInOpChrgFocusGained
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtInOpChrgFocusGained

    private void rdoAcc_NreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAcc_NreActionPerformed
        // TODO add your handling code here:
         ClientUtil.enableDisable(panTaxIntApplNRO,false);
         rdoTaxIntApplNRO_Yes.setSelected(false);
         rdoTaxIntApplNRO_No.setSelected(true);
         txtRateTaxNRO.setText("");
         txtRateTaxNRO.setEnabled(false);
    }//GEN-LAST:event_rdoAcc_NreActionPerformed

    private void rdoAcc_NroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAcc_NroActionPerformed
        // TODO add your handling code here:
         ClientUtil.enableDisable(panTaxIntApplNRO,true);
    }//GEN-LAST:event_rdoAcc_NroActionPerformed

    private void rdoAcc_NreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoAcc_NreFocusLost

    }//GEN-LAST:event_rdoAcc_NreFocusLost

    private void rdoAcc_RegActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAcc_RegActionPerformed
        // TODO add your handling code here:
         ClientUtil.enableDisable(panTaxIntApplNRO,false);
         rdoTaxIntApplNRO_Yes.setSelected(false);
         rdoTaxIntApplNRO_No.setSelected(true);
         txtRateTaxNRO.setText("");
         txtRateTaxNRO.setEnabled(false);
    }//GEN-LAST:event_rdoAcc_RegActionPerformed

    private void rdoAcc_RegFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoAcc_RegFocusLost

    }//GEN-LAST:event_rdoAcc_RegFocusLost

    private void rdoAcc_NroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_rdoAcc_NroFocusLost

    }//GEN-LAST:event_rdoAcc_NroFocusLost

    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // TODO add your handling code here:
         setCommand(ClientConstants.ACTIONTYPE_COPY);
                         callView(COPY);
                          btnReject.setEnabled(false);
                           btnAuthorize.setEnabled(false);
                         btnException.setEnabled(false);
    }//GEN-LAST:event_btnCopyActionPerformed

    private void rdoStaffAcct_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStaffAcct_YesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoStaffAcct_YesActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        setCommand(ClientConstants.ACTIONTYPE_VIEW);
        lblStatus.setText(observable.getLblStatus());
        callView(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed

    private void cboFolioChrgApplFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFolioChrgApplFreqActionPerformed
        // TODO add your handling code here:
        if(cboFolioChrgApplFreq.getSelectedItem() !=null && (! CommonUtil.convertObjToStr(cboFolioChrgApplFreq.getSelectedItem()).equals(""))){
            int period= CommonUtil.convertObjToInt(((ComboBoxModel)cboFolioChrgApplFreq.getModel()).getKeyForSelected());
            if(tdtLastFolioAppliedDate.getDateValue() !=null && tdtLastFolioAppliedDate.getDateValue().length()>0){
                Date lastFoliodate=DateUtil.getDateMMDDYYYY(tdtLastFolioAppliedDate.getDateValue());
                lastFoliodate= DateUtil.addDays(lastFoliodate, period);
                tdtNextFolioAppliedDt.setDateValue(DateUtil.getStringDate(lastFoliodate));
                tdtNextFolioAppliedDt.setEnabled(false);
            }else
                ClientUtil.showMessageWindow("Enter the Last FolioCharge Applied Date");
        }
    }//GEN-LAST:event_cboFolioChrgApplFreqActionPerformed
    
    private void cboEndDayProdCalcSBCrIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEndDayProdCalcSBCrIntActionPerformed
        // TODO add your handling code here:
        if( !(txtEndDayProdCalcSBCrInt.getText().equalsIgnoreCase("")
        || CommonUtil.convertObjToStr(cboEndDayProdCalcSBCrInt.getSelectedItem()).equalsIgnoreCase(""))){
            //__ Validation for the No of Days in the Particular Month...
            
            String message = ValidateMaxDays(txtEndDayProdCalcSBCrInt, cboEndDayProdCalcSBCrInt);
            if(message.length() > 0){
                displayAlert(message);
            }
        }
    }//GEN-LAST:event_cboEndDayProdCalcSBCrIntActionPerformed
    
    private void cboIntCalcEndMonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntCalcEndMonActionPerformed
        // TODO add your handling code here:
        if( !(txtIntCalcEndMon.getText().equalsIgnoreCase("")
        || CommonUtil.convertObjToStr(cboIntCalcEndMon.getSelectedItem()).equalsIgnoreCase(""))){
            //__ Validation for the No of Days in the Particular Month...
            
            String message = ValidateMaxDays(txtIntCalcEndMon, cboIntCalcEndMon);
            if(message.length() > 0){
                displayAlert(message);
            }
        }
    }//GEN-LAST:event_cboIntCalcEndMonActionPerformed
    
    private void cboEndProdCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEndProdCalcActionPerformed
        // TODO add your handling code here:
        if( !(txtEndProdCalc.getText().equalsIgnoreCase("")
        || CommonUtil.convertObjToStr(cboEndProdCalc.getSelectedItem()).equalsIgnoreCase(""))){
            //__ Validation for the No of Days in the Particular Month...
            
            String message = ValidateMaxDays(txtEndProdCalc, cboEndProdCalc);
            if(message.length() > 0){
                displayAlert(message);
            }
        }
    }//GEN-LAST:event_cboEndProdCalcActionPerformed
    
    private void cboEndInterCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboEndInterCalcActionPerformed
        // TODO add your handling code here:
        if( !(txtEndInterCalc.getText().equalsIgnoreCase("")
        || CommonUtil.convertObjToStr(cboEndInterCalc.getSelectedItem()).equalsIgnoreCase(""))){
            //__ Validation for the No of Days in the Particular Month...
            
            String message = ValidateMaxDays(txtEndInterCalc, cboEndInterCalc);
            if(message.length() > 0){
                displayAlert(message);
            }
        }
    }//GEN-LAST:event_cboEndInterCalcActionPerformed
    private String ValidateMaxDays(CTextField txtField, CComboBox comboField){
        String message = "";
        int month = CommonUtil.convertObjToInt(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        int maxDays = ClientUtil.getMaxDayInMonth(month-1);
        
        if(CommonUtil.convertObjToInt(txtField.getText()) > maxDays){
            message = resourceBundle.getString("MAXDAYSWARNING");
        }
        return message;
    }
    private void txtRateTaxNROFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateTaxNROFocusLost
        //        //--- If the txtRateTax is not null, then check for the Percentage Criteria
        //        if(txtRateTaxNRO.getText()!= null){
        //        double rateTaxNRO = Double.parseDouble(txtRateTaxNRO.getText());
        //        //--- if the Percentage is more than 100%, alert the user.
        //        if(rateTaxNRO>100){
        //            ClientUtil.showAlertWindow("The Rate of tax (NRO) % should not be greater than 100");
        //            txtRateTaxNRO.setFocusable(true);
        //        }
        //        }
    }//GEN-LAST:event_txtRateTaxNROFocusLost
    
private void mitSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveAsActionPerformed
    // TODO add your handling code here:
    /**This Gives the Save As Functionality to the Code*/
    setCommand(ClientConstants.ACTIONTYPE_NEW);
    callView(SAVEAS);
    txtProductID.setEditable(true);
}//GEN-LAST:event_mitSaveAsActionPerformed

 private void cboStartProdCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStartProdCalcActionPerformed
     // TODO add your handling code here:
     if( !(txtStartProdCalc.getText().equalsIgnoreCase("")
     || ((String)cboStartProdCalc.getSelectedItem()).equalsIgnoreCase(""))){
         
         String message = ValidateMaxDays(txtStartProdCalc, cboStartProdCalc);
         if(message.length() > 0){
             displayAlert(message);
             
         }else{
             int prodFreq = CommonUtil.convertObjToInt(observable.getCboProdFreq());
             
             // if(CommonUtil.convertObjToInt(observable.getCboProdFreq()) == 7){
             // computeStartProdCalc(7);
             // }else if(CommonUtil.convertObjToInt(observable.getCboProdFreq()) == 30){
             // computeStartProdMonth(1);
             // }else
             if(prodFreq == 90){
                 computeStartProdMonth(3);
             }else if(prodFreq == 180){
                 computeStartProdMonth(6);
             }else if(prodFreq == 365){
                 computeStartProdMonth(12);
             }
         }
     }
 }//GEN-LAST:event_cboStartProdCalcActionPerformed
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
 
 private void computeStartProdCalc(int days){
     StringBuffer strDate = new StringBuffer();
     strDate.append((String) ((ComboBoxModel) cboStartProdCalc.getModel()).getKeyForSelected()+"/");
     strDate.append(txtStartProdCalc.getText()+"/");
     
     Date currentDate = (Date) curDate.clone();
     strDate.append(String.valueOf(currentDate.getYear()));
     
     Date workingDate = DateUtil.getDateMMDDYYYY(strDate.toString());
     
     GregorianCalendar calendar = new GregorianCalendar();
     calendar.setTime(workingDate);
     calendar.add(calendar.DATE, days);
     
     Date endDate = calendar.getTime();
     txtEndProdCalc.setText(String.valueOf(endDate.getDate()));
     int month = endDate.getMonth()+1;
     if(month < 10){
         cboEndProdCalc.setSelectedItem(((ComboBoxModel) cboEndProdCalc.getModel()).getDataForKey("0"+String.valueOf(month)));
     }else{
         cboEndProdCalc.setSelectedItem(((ComboBoxModel) cboEndProdCalc.getModel()).getDataForKey(String.valueOf(month)));
     }
 }
 
 private void computeStartProdMonth(int months){
     txtEndProdCalc.setText(txtStartProdCalc.getText());
     int realEndMonth = CommonUtil.convertObjToInt(((ComboBoxModel) cboStartProdCalc.getModel()).getKeyForSelected()) + months;
     int endMonth = realEndMonth % 12;
     if(endMonth == 0){
         endMonth = 12;
     }
     
     if(endMonth < 10){
         cboEndProdCalc.setSelectedItem(((ComboBoxModel) cboEndProdCalc.getModel()).getDataForKey("0"+String.valueOf(endMonth)));
     }else{
         cboEndProdCalc.setSelectedItem(((ComboBoxModel) cboEndProdCalc.getModel()).getDataForKey(String.valueOf(endMonth)));
     }
 }
 private void cboProdFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdFreqActionPerformed
     // TODO add your handling code here:
     observable.setCboProdFreq(CommonUtil.convertObjToStr(((ComboBoxModel)cboProdFreq.getModel()).getKeyForSelected()));
     if(observable.getCboProdFreq()!=null){
         if(observable.getCboProdFreq().length() > 0){
             String prodFreq = CommonUtil.convertObjToStr(observable.getCboProdFreq());
             if(prodFreq.equalsIgnoreCase("1")
             || prodFreq.equalsIgnoreCase("7")
             || prodFreq.equalsIgnoreCase("30")){
                 
                 cboStartProdCalc.setEnabled(false);
                 cboStartProdCalc.setSelectedIndex(0);
                 
                 cboEndProdCalc.setEnabled(false);
                 cboEndProdCalc.setSelectedIndex(0);
                 
                 if(((String)observable.getCboProdFreq()).equalsIgnoreCase("1")){
                     txtStartProdCalc.setText("");
                     txtStartProdCalc.setEditable(false);
                     txtStartProdCalc.setEnabled(false);
                     
                     
                     txtEndProdCalc.setText("");
                     txtEndProdCalc.setEditable(false);
                     txtEndProdCalc.setEnabled(false);
                 }else{
                     txtStartProdCalc.setEditable(true);
                     txtStartProdCalc.setEnabled(true);
                     
                     txtEndProdCalc.setEditable(true);
                     txtEndProdCalc.setEnabled(true);
                 }
             }else {
                 enableProdFreqData(true);
             }
         }else if(CommonUtil.convertObjToStr(observable.getCboProdFreqCr()).equalsIgnoreCase("")){
             cboStartProdCalc.setEnabled(true);
             cboStartProdCalc.setSelectedIndex(0);
             
             cboEndProdCalc.setEnabled(true);
             cboEndProdCalc.setSelectedIndex(0);
             
             txtStartProdCalc.setText("");
             txtStartProdCalc.setEditable(true);
             txtStartProdCalc.setEnabled(true);
             
             
             txtEndProdCalc.setText("");
             txtEndProdCalc.setEditable(true);
             txtEndProdCalc.setEnabled(true);
         }
     }
     // else {
     // enableProdFreqData();
     // }
 }//GEN-LAST:event_cboProdFreqActionPerformed
 private void enableProdFreqData(boolean value){
     txtStartProdCalc.setEditable(value);
     txtStartProdCalc.setEnabled(value);
     
     cboStartProdCalc.setEnabled(value);
     
     txtEndProdCalc.setEditable(value);
     txtEndProdCalc.setEnabled(value);
     
     cboEndProdCalc.setEnabled(value);
 }
 private void cboStartInterCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStartInterCalcActionPerformed
     // TODO add your handling code here:
     if( !(txtStartInterCalc.getText().equalsIgnoreCase("")
     || CommonUtil.convertObjToStr(cboStartInterCalc.getSelectedItem()).equalsIgnoreCase(""))){
         int debitFreq = CommonUtil.convertObjToInt(observable.getCboDebitIntCalcFreq());
         //__ Validation for the No of Days in the Particular Month...
         
         String message = ValidateMaxDays(txtStartInterCalc, cboStartInterCalc);
         
         if(message.length() > 0){
             displayAlert(message);
         }else{
             if(debitFreq == 7){
                 computeStartInterCalc(7);
             }else if(debitFreq == 30){
                 computeStartInterMonth(1);
             }else if(debitFreq == 90){
                 computeStartInterMonth(3);
             }else if(debitFreq == 180){
                 computeStartInterMonth(6);
             }else if(debitFreq == 365){
                 computeStartInterMonth(12);
             }
         }
     }
 }//GEN-LAST:event_cboStartInterCalcActionPerformed
 private void computeStartInterCalc(int days){
     StringBuffer strDate = new StringBuffer();
     strDate.append((String) ((ComboBoxModel) cboStartInterCalc.getModel()).getKeyForSelected()+"/");
     strDate.append(txtStartInterCalc.getText()+"/");
     
     Date currentDate = (Date)curDate.clone();
     strDate.append(String.valueOf(currentDate.getYear()));
     
     Date workingDate = DateUtil.getDateMMDDYYYY(strDate.toString());
     
     GregorianCalendar calendar = new GregorianCalendar();
     calendar.setTime(workingDate);
     calendar.add(calendar.DATE, days);
     
     Date endDate = calendar.getTime();
     
     txtEndInterCalc.setText(String.valueOf(endDate.getDate()));
     int month = endDate.getMonth()+1;
     if(month < 10){
         cboEndInterCalc.setSelectedItem(((ComboBoxModel) cboEndInterCalc.getModel()).getDataForKey("0"+String.valueOf(month)));
     }else{
         cboEndInterCalc.setSelectedItem(((ComboBoxModel) cboEndInterCalc.getModel()).getDataForKey(String.valueOf(month)));
     }
 }
 
 private void computeStartInterMonth(int months){
     txtEndInterCalc.setText(txtStartInterCalc.getText());
     int realEndMonth = CommonUtil.convertObjToInt(((ComboBoxModel) cboStartInterCalc.getModel()).getKeyForSelected()) + months;
     int endMonth = realEndMonth % 12;
     if(endMonth == 0){
         endMonth = 12;
     }
     
     if(endMonth < 10){
         cboEndInterCalc.setSelectedItem(((ComboBoxModel) cboEndInterCalc.getModel()).getDataForKey("0"+String.valueOf(endMonth)));
     }else{
         cboEndInterCalc.setSelectedItem(((ComboBoxModel) cboEndInterCalc.getModel()).getDataForKey(String.valueOf(endMonth)));
     }
 }
 private void cboDebitIntCalcFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDebitIntCalcFreqActionPerformed
     // TODO add your handling code here:
     observable.setCboDebitIntCalcFreq((String) ((ComboBoxModel) cboDebitIntCalcFreq.getModel()).getKeyForSelected());
     if(observable.getCboDebitIntCalcFreq()!=null){
         if(observable.getCboDebitIntCalcFreq().length() > 0){
             if(((String)observable.getCboDebitIntCalcFreq()).equalsIgnoreCase("1")){
                 txtStartInterCalc.setText("");
                 txtStartInterCalc.setEditable(false);
                 txtStartInterCalc.setEnabled(false);
                 
                 cboStartInterCalc.setEnabled(false);
                 cboStartInterCalc.setSelectedIndex(0);
                 
                 txtEndInterCalc.setText("");
                 txtEndInterCalc.setEditable(false);
                 txtEndInterCalc.setEnabled(false);
                 
                 cboEndInterCalc.setEnabled(false);
                 cboEndInterCalc.setSelectedIndex(0);
             }else {
                 enableDebitIntCalcFreq();
             }
         }
     }
     // else {
     // enableDebitIntCalcFreq();
     // }
 }//GEN-LAST:event_cboDebitIntCalcFreqActionPerformed
 private void enableDebitIntCalcFreq(){
     txtStartInterCalc.setEditable(true);
     txtStartInterCalc.setEnabled(true);
     
     cboStartInterCalc.setEnabled(true);
     
     txtEndInterCalc.setEditable(true);
     txtEndInterCalc.setEnabled(true);
     
     cboEndInterCalc.setEnabled(true);
 }
 private void txtChrgMiscServChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChrgMiscServChrgFocusLost
     // TODO add your handling code here:
     if(!(txtChrgMiscServChrg.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtChrgMiscServChrg.getText())==0){
             txtMiscServChrg.setText("");
             txtMiscServChrg.setEditable(false);
             txtMiscServChrg.setEnabled(false);
             btnMiscServChrg.setEnabled(false);
         }else{
             txtMiscServChrg.setEditable(true);
             txtMiscServChrg.setEnabled(true);
             btnMiscServChrg.setEnabled(true);
         }
     }else{
         txtMiscServChrg.setText("");
         txtMiscServChrg.setEditable(false);
         txtMiscServChrg.setEnabled(false);
         btnMiscServChrg.setEnabled(false);
     }
 }//GEN-LAST:event_txtChrgMiscServChrgFocusLost
 
 private void txtFolioChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFolioChrgFocusLost
     // TODO add your handling code here:
     if(!(txtFolioChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtFolioChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtFolioChrgDesc.setText(getAccHdDesc(txtFolioChrg.getText()));
         txtFolioChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtFolioChrgFocusLost
 
 private void txtInOperativeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInOperativeFocusLost
     // TODO add your handling code here:
     if(!(txtInOperative.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtInOperative, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtInOperativeDesc.setText(getAccHdDesc(txtInOperative.getText()));
         txtInOperativeDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtInOperativeFocusLost
 
 private void txtNonMainMinBalChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNonMainMinBalChrgFocusLost
     // TODO add your handling code here:
     if(!(txtNonMainMinBalChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtNonMainMinBalChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtNonMainMinBalChrgDesc.setText(getAccHdDesc(txtNonMainMinBalChrg.getText()));
         txtNonMainMinBalChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtNonMainMinBalChrgFocusLost
 
 private void txtTaxGLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTaxGLFocusLost
     // TODO add your handling code here:
     if(!(txtTaxGL.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtTaxGL, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtTaxGLDesc.setText(getAccHdDesc(txtTaxGL.getText()));
         txtTaxGLDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtTaxGLFocusLost
 
 private void txtExcessFreeWDChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExcessFreeWDChrgFocusLost
     // TODO add your handling code here:
     if(!(txtExcessFreeWDChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtExcessFreeWDChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtExcessFreeWDChrgDesc.setText(getAccHdDesc(txtExcessFreeWDChrg.getText()));
         txtExcessFreeWDChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtExcessFreeWDChrgFocusLost
 
 private void txtAcctOpenChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctOpenChrgFocusLost
     // TODO add your handling code here:
     if(!(txtAcctOpenChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtAcctOpenChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtAcctOpenChrgDesc.setText(getAccHdDesc(txtAcctOpenChrg.getText()));
         txtAcctOpenChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtAcctOpenChrgFocusLost
 
 private void txtInwardChkRetChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInwardChkRetChrgFocusLost
     // TODO add your handling code here:
     if(!(txtInwardChkRetChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtInwardChkRetChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtInwardChkRetChrgDesc.setText(getAccHdDesc(txtInwardChkRetChrg.getText()));
         txtInwardChkRetChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtInwardChkRetChrgFocusLost
 
 private void txtOutwardChkRetChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOutwardChkRetChrgFocusLost
     // TODO add your handling code here:
     if(!(txtOutwardChkRetChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtOutwardChkRetChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtOutwardChkRetChrgDesc.setText(getAccHdDesc(txtOutwardChkRetChrg.getText()));
         txtOutwardChkRetChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtOutwardChkRetChrgFocusLost
 
 private void txtStopPaymentChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStopPaymentChrgFocusLost
     // TODO add your handling code here:
     if(!(txtStopPaymentChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtStopPaymentChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtStopPaymentChrgDesc.setText(getAccHdDesc(txtStopPaymentChrg.getText()));
         txtStopPaymentChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtStopPaymentChrgFocusLost
 
 private void txtChkBkIssueChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChkBkIssueChrgFocusLost
     // TODO add your handling code here:
     if(!(txtChkBkIssueChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtChkBkIssueChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtChkBkIssueChrgDesc.setText(getAccHdDesc(txtChkBkIssueChrg.getText()));
         txtChkBkIssueChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtChkBkIssueChrgFocusLost
 
 private void txtClearingIntAcctHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClearingIntAcctHdFocusLost
     // TODO add your handling code here:
     if(!(txtClearingIntAcctHd.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtClearingIntAcctHd, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtClearingIntAcctHdDesc.setText(getAccHdDesc(txtClearingIntAcctHd.getText()));
         txtClearingIntAcctHdDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtClearingIntAcctHdFocusLost
 
 private void txtAcctCrIntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctCrIntFocusLost
     // TODO add your handling code here:
     if(!(txtAcctCrInt.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtAcctCrInt, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtAcctCrIntDesc.setText(getAccHdDesc(txtAcctCrInt.getText()));
         txtAcctCrIntDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtAcctCrIntFocusLost
 
 private void txtAcctDebitIntFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctDebitIntFocusLost
     // TODO add your handling code here:
     if(!(txtAcctDebitInt.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtAcctDebitInt, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtAcctDebitIntDesc.setText(getAccHdDesc(txtAcctDebitInt.getText()));
         txtAcctDebitIntDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtAcctDebitIntFocusLost
 
 private void txtFreeWDChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFreeWDChrgFocusLost
     // TODO add your handling code here:
     if(!(txtFreeWDChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtFreeWDChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtFreeWDChrgDesc.setText(getAccHdDesc(txtFreeWDChrg.getText()));
         txtFreeWDChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtFreeWDChrgFocusLost
 
 private void txtStatChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtStatChrgFocusLost
     // TODO add your handling code here:
     if(!(txtStatChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtStatChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtStatChrgDesc.setText(getAccHdDesc(txtStatChrg.getText()));
         txtStatChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtStatChrgFocusLost
 
 private void txtMiscServChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMiscServChrgFocusLost
     // TODO add your handling code here:
     if(!(txtMiscServChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtMiscServChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtMiscServChrgDesc.setText(getAccHdDesc(txtMiscServChrg.getText()));
                          txtMiscServChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtMiscServChrgFocusLost
 
 private void txtAcctClosingChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctClosingChrgFocusLost
     // TODO add your handling code here:
     if(!(txtAcctClosingChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtAcctClosingChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
          txtAcctClosingChrgDesc.setText(getAccHdDesc(txtAcctClosingChrg.getText()));
                          txtAcctClosingChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtAcctClosingChrgFocusLost
 
 private void txtPrematureClosureChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrematureClosureChrgFocusLost
     // TODO add your handling code here:
     if(!(txtPrematureClosureChrg.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtPrematureClosureChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
                               txtPrematureClosureChrgDesc.setText(getAccHdDesc(txtPrematureClosureChrg.getText()));
                           txtPrematureClosureChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtPrematureClosureChrgFocusLost
 
 private void txtInOpChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInOpChrgFocusLost
     // TODO add your handling code here:
     if(!(txtInOpChrg.getText().equalsIgnoreCase("")))
     {
         observable.verifyAcctHead(txtInOpChrg, "OperativeAcctProduct.getSelectAcctHeadTOList");
                                txtInOpChrgDesc.setText(getAccHdDesc(txtInOpChrg.getText()));
                           txtInOpChrgDesc.setEnabled(false);
     }
 }//GEN-LAST:event_txtInOpChrgFocusLost
 
 private void txtAcctOpenChargesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctOpenChargesFocusLost
     // TODO add your handling code here:
     if(!(txtAcctOpenCharges.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtAcctOpenCharges.getText())==0){
             txtAcctOpenChrg.setText("");
             txtAcctOpenChrg.setEditable(false);
             txtAcctOpenChrg.setEnabled(false);
             btnAcctOpenChrg.setEnabled(false);
         }else{
             txtAcctOpenChrg.setEditable(true);
             txtAcctOpenChrg.setEnabled(true);
             btnAcctOpenChrg.setEnabled(true);
         }
     }else{
         txtAcctOpenChrg.setText("");
         txtAcctOpenChrg.setEditable(false);
         txtAcctOpenChrg.setEnabled(false);
         btnAcctOpenChrg.setEnabled(false);
     }
 }//GEN-LAST:event_txtAcctOpenChargesFocusLost
 
 private void txtChkRetChrgInFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChkRetChrgInFocusLost
     // TODO add your handling code here:
     if(!(txtChkRetChrgIn.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtChkRetChrgIn.getText())==0){
             txtInwardChkRetChrg.setText("");
             txtInwardChkRetChrg.setEditable(false);
             txtInwardChkRetChrg.setEnabled(false);
             btnInwardChkRetChrg.setEnabled(false);
         }else{
             txtInwardChkRetChrg.setEditable(true);
             txtInwardChkRetChrg.setEnabled(true);
             btnInwardChkRetChrg.setEnabled(true);
         }
     }else{
         txtInwardChkRetChrg.setText("");
         txtInwardChkRetChrg.setEditable(false);
         txtInwardChkRetChrg.setEnabled(false);
         btnInwardChkRetChrg.setEnabled(false);
     }
 }//GEN-LAST:event_txtChkRetChrgInFocusLost
 
 private void txtChkRetChrOutwardFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChkRetChrOutwardFocusLost
     // TODO add your handling code here:
     if(!(txtChkRetChrOutward.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtChkRetChrOutward.getText())==0){
             txtOutwardChkRetChrg.setText("");
             txtOutwardChkRetChrg.setEditable(false);
             txtOutwardChkRetChrg.setEnabled(false);
             btnOutwardChkRetChrg.setEnabled(false);
         }else{
             txtOutwardChkRetChrg.setEditable(true);
             txtOutwardChkRetChrg.setEnabled(true);
             btnOutwardChkRetChrg.setEnabled(true);
         }
     }else{
         txtOutwardChkRetChrg.setText("");
         txtOutwardChkRetChrg.setEditable(false);
         txtOutwardChkRetChrg.setEnabled(false);
         btnOutwardChkRetChrg.setEnabled(false);
     }
 }//GEN-LAST:event_txtChkRetChrOutwardFocusLost
 private void disableCreditInt(){
     ClientUtil.clearAll(panIntPay);
     ClientUtil.enableDisable(panIntPay, false);
     rdoCrIntGiven_Yes.setEnabled(true);
     rdoCrIntGiven_No.setSelected(true);
     rdoCrIntGiven_No.setEnabled(true);
     
     txtAcctCrInt.setText("");
     txtAcctCrInt.setEditable(false);
     txtAcctCrInt.setEnabled(false);
     btnAcctCrInt.setEnabled(false);
 }
 private void rdoCrIntGiven_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCrIntGiven_NoActionPerformed
     // TODO add your handling code here:
     if (rdoCrIntGiven_No.isSelected()) {
         disableCreditInt();
     }else{
         rdoCrIntGiven_YesActionPerformed(null);
     }
 }//GEN-LAST:event_rdoCrIntGiven_NoActionPerformed
 
 private void rdoCrIntGiven_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCrIntGiven_YesActionPerformed
     // TODO add your handling code here:
     // ClientUtil.clearAll(panIntPay);
     ClientUtil.enableDisable(panIntPay, true);
     
     txtAcctCrInt.setEditable(true);
     txtAcctCrInt.setEnabled(true);
     btnAcctCrInt.setEnabled(true);
 }//GEN-LAST:event_rdoCrIntGiven_YesActionPerformed
 
 private void txtExcessFreeWDChrgPTFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtExcessFreeWDChrgPTFocusLost
     // TODO add your handling code here:
     if(!(txtExcessFreeWDChrgPT.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtExcessFreeWDChrgPT.getText())==0){
             txtFreeWDChrg.setText("");
             txtFreeWDChrg.setEditable(false);
             txtFreeWDChrg.setEnabled(false);
             btnFreeWDChrg.setEnabled(false);
             
             txtExcessFreeWDChrg.setText("");
             txtExcessFreeWDChrg.setEditable(false);
             txtExcessFreeWDChrg.setEnabled(false);
             btnExcessFreeWDChrg.setEnabled(false);
         }else{
             txtFreeWDChrg.setEditable(true);
             txtFreeWDChrg.setEnabled(true);
             btnFreeWDChrg.setEnabled(true);
             
             txtExcessFreeWDChrg.setEditable(true);
             txtExcessFreeWDChrg.setEnabled(true);
             btnExcessFreeWDChrg.setEnabled(true);
         }
     }else{
         txtFreeWDChrg.setText("");
         txtFreeWDChrg.setEditable(false);
         txtFreeWDChrg.setEnabled(false);
         btnFreeWDChrg.setEnabled(false);
         
         txtExcessFreeWDChrg.setText("");
         txtExcessFreeWDChrg.setEditable(false);
         txtExcessFreeWDChrg.setEnabled(false);
         btnExcessFreeWDChrg.setEnabled(false);
     }
 }//GEN-LAST:event_txtExcessFreeWDChrgPTFocusLost
 
 private void txtAcClosingChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcClosingChrgFocusLost
     // TODO add your handling code here:
     if(!(txtAcClosingChrg.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtAcClosingChrg.getText())==0){
             txtAcctClosingChrg.setText("");
             txtAcctClosingChrg.setEditable(false);
             txtAcctClosingChrg.setEnabled(false);
             btnAcctClosingChrg.setEnabled(false);
         }else{
             txtAcctClosingChrg.setEditable(true);
             txtAcctClosingChrg.setEnabled(true);
             btnAcctClosingChrg.setEnabled(true);
         }
     }else{
         txtAcctClosingChrg.setText("");
         txtAcctClosingChrg.setEditable(false);
         txtAcctClosingChrg.setEnabled(false);
         btnAcctClosingChrg.setEnabled(false);
     }
 }//GEN-LAST:event_txtAcClosingChrgFocusLost
 
 private void txtMainTreatNewAcctClosureFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMainTreatNewAcctClosureFocusLost
     // TODO add your handling code here:
     if(!(txtMainTreatNewAcctClosure.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtMainTreatNewAcctClosure.getText())==0){
             txtPrematureClosureChrg.setText("");
             txtPrematureClosureChrg.setEditable(false);
             txtPrematureClosureChrg.setEnabled(false);
             btnPrematureClosureChrg.setEnabled(false);
         }else{
             txtPrematureClosureChrg.setEditable(true);
             txtPrematureClosureChrg.setEnabled(true);
             btnPrematureClosureChrg.setEnabled(true);
         }
     }else{
         txtPrematureClosureChrg.setText("");
         txtPrematureClosureChrg.setEditable(false);
         txtPrematureClosureChrg.setEnabled(false);
         btnPrematureClosureChrg.setEnabled(false);
     }
 }//GEN-LAST:event_txtMainTreatNewAcctClosureFocusLost
 
 private void txtMinTreatasInOpFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinTreatasInOpFocusLost
     // TODO add your handling code here:
     if(!(txtMinTreatasInOp.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtMinTreatasInOp.getText())==0){
             txtInOpChrg.setText("");
             txtInOpChrg.setEditable(false);
             txtInOpChrg.setEnabled(false);
             btnInOpChrg.setEnabled(false);
             
             txtInOperative.setText("");
             txtInOperative.setEditable(false);
             txtInOperative.setEnabled(false);
             btnInOperative.setEnabled(false);
         }else{
             txtInOpChrg.setEditable(true);
             txtInOpChrg.setEnabled(true);
             btnInOpChrg.setEnabled(true);
             
             txtInOperative.setEditable(true);
             txtInOperative.setEnabled(true);
             btnInOperative.setEnabled(true);
         }
     }else{
         txtInOpChrg.setText("");
         txtInOpChrg.setEditable(false);
         txtInOpChrg.setEnabled(false);
         btnInOpChrg.setEnabled(false);
         
         txtInOperative.setText("");
         txtInOperative.setEditable(false);
         txtInOperative.setEnabled(false);
         btnInOperative.setEnabled(false);
     }
 }//GEN-LAST:event_txtMinTreatasInOpFocusLost
 
 private void txtlInOpAcChrgFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtlInOpAcChrgFocusLost
     // TODO add your handling code here: cboInOpAcChrgPd
     if(!(txtlInOpAcChrg.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtlInOpAcChrg.getText())==0){
             cboInOpAcChrgPd.setSelectedIndex(0);
             cboInOpAcChrgPd.setEnabled(false);
         }else{
             cboInOpAcChrgPd.setEnabled(true);
         }
     }
 }//GEN-LAST:event_txtlInOpAcChrgFocusLost
 
 private void cboStMonIntCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStMonIntCalcActionPerformed
     // TODO add your handling code here:
     if( !(txtStMonIntCalc.getText().equalsIgnoreCase("")
     || ((String)cboStMonIntCalc.getSelectedItem()).equalsIgnoreCase(""))){
         
         String message = ValidateMaxDays(txtStMonIntCalc, cboStMonIntCalc);
         if(message.length() > 0){
             displayAlert(message);
             
         }else{
             if(CommonUtil.convertObjToInt(observable.getCboCrIntCalcFreq()) == 7){
                 computeEndIntCalculation(7);
             }else if(CommonUtil.convertObjToInt(observable.getCboCrIntCalcFreq()) == 30){
                 computeEndIntDay(1);
             }else if(CommonUtil.convertObjToInt(observable.getCboCrIntCalcFreq()) == 90){
                 computeEndIntDay(3);
             }else if(CommonUtil.convertObjToInt(observable.getCboCrIntCalcFreq()) == 180){
                 computeEndIntDay(6);
             }else if(CommonUtil.convertObjToInt(observable.getCboCrIntCalcFreq()) == 365){
                 computeEndIntDay(12);
             }
         }
     }
 }//GEN-LAST:event_cboStMonIntCalcActionPerformed
 private void computeEndIntCalculation(int days){
     StringBuffer strDate = new StringBuffer();
     strDate.append((String) ((ComboBoxModel) cboStMonIntCalc.getModel()).getKeyForSelected()+"/");
     strDate.append(txtStMonIntCalc.getText()+"/");
     
     Date currentDate = (Date)curDate.clone();
     strDate.append(String.valueOf(currentDate.getYear()));
     
     Date workingDate = DateUtil.getDateMMDDYYYY(strDate.toString());
     
     GregorianCalendar calendar = new GregorianCalendar();
     calendar.setTime(workingDate);
     calendar.add(calendar.DATE, days);
     
     Date endDate = calendar.getTime();
     
     txtIntCalcEndMon.setText(String.valueOf(endDate.getDate()));
     int month = endDate.getMonth()+1;
     if(month < 10){
         cboIntCalcEndMon.setSelectedItem(((ComboBoxModel) cboIntCalcEndMon.getModel()).getDataForKey("0"+String.valueOf(month)));
     }else{
         cboIntCalcEndMon.setSelectedItem(((ComboBoxModel) cboIntCalcEndMon.getModel()).getDataForKey(String.valueOf(month)));
     }
 }
 
 private void computeEndIntDay(int months){
     txtIntCalcEndMon.setText(txtStMonIntCalc.getText());
     int realEndMonth = CommonUtil.convertObjToInt(((ComboBoxModel) cboStMonIntCalc.getModel()).getKeyForSelected()) + months;
     int endMonth = realEndMonth % 12;
     if(endMonth == 0){
         endMonth = 12;
     }
     
     if(endMonth < 10){
         cboIntCalcEndMon.setSelectedItem(((ComboBoxModel) cboIntCalcEndMon.getModel()).getDataForKey("0"+String.valueOf(endMonth)));
     }else{
         cboIntCalcEndMon.setSelectedItem(((ComboBoxModel) cboIntCalcEndMon.getModel()).getDataForKey(String.valueOf(endMonth)));
     }
 }
 private void cboCrIntCalcFreqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCrIntCalcFreqActionPerformed
     // TODO add your handling code here:
     observable.setCboCrIntCalcFreq(CommonUtil.convertObjToStr(((ComboBoxModel) cboCrIntCalcFreq.getModel()).getKeyForSelected()));
     if(observable.getCboCrIntCalcFreq()!=null){
         if(observable.getCboCrIntCalcFreq().length() > 0){
             if(CommonUtil.convertObjToStr(observable.getCboCrIntCalcFreq()).equalsIgnoreCase("1")){
                 txtStMonIntCalc.setText("");
                 txtStMonIntCalc.setEditable(false);
                 txtStMonIntCalc.setEnabled(false);
                 
                 cboStMonIntCalc.setEnabled(false);
                 cboStMonIntCalc.setSelectedIndex(0);
                 
                 txtIntCalcEndMon.setText("");
                 txtIntCalcEndMon.setEditable(false);
                 txtIntCalcEndMon.setEnabled(false);
                 
                 cboIntCalcEndMon.setEnabled(false);
                 cboIntCalcEndMon.setSelectedIndex(0);
             }else {
                 enableIntFreq();
             }
         }
     }
     // else {
     // enableIntFreq();
     // }
 }//GEN-LAST:event_cboCrIntCalcFreqActionPerformed
 private void enableIntFreq(){
     txtStMonIntCalc.setEditable(true);
     txtStMonIntCalc.setEnabled(true);
     
     cboStMonIntCalc.setEnabled(true);
     
     txtIntCalcEndMon.setEditable(true);
     txtIntCalcEndMon.setEnabled(true);
     
     cboIntCalcEndMon.setEnabled(true);
 }
 private void cboStDayProdCalcSBCrIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboStDayProdCalcSBCrIntActionPerformed
     // TODO add your handling code here:
     if( !(txtStDayProdCalcSBCrInt.getText().equalsIgnoreCase("")
     || CommonUtil.convertObjToStr(cboStDayProdCalcSBCrInt.getSelectedItem()).equalsIgnoreCase(""))){
         
         String message = ValidateMaxDays(txtStDayProdCalcSBCrInt, cboStDayProdCalcSBCrInt);
         
         if(message.length() > 0){
             displayAlert(message);
             
         }else{
             // if(CommonUtil.convertObjToInt(observable.getCboProdFreqCr()) == 7){
             // computeEndProdCalculation(7);
             // }else if(CommonUtil.convertObjToInt(observable.getCboProdFreqCr()) == 30){
             // computeEndProdDay(1);
             // }
             if(CommonUtil.convertObjToInt(observable.getCboProdFreqCr()) == 90){
                 computeEndProdDay(3);
             }else if(CommonUtil.convertObjToInt(observable.getCboProdFreqCr()) == 180){
                 computeEndProdDay(6);
             }else if(CommonUtil.convertObjToInt(observable.getCboProdFreqCr()) == 365){
                 computeEndProdDay(12);
             }
         }
     }
 }//GEN-LAST:event_cboStDayProdCalcSBCrIntActionPerformed
 private void computeEndProdCalculation(int days){
     StringBuffer strDate = new StringBuffer();
     strDate.append((String) ((ComboBoxModel) cboStDayProdCalcSBCrInt.getModel()).getKeyForSelected()+"/");
     strDate.append(txtStDayProdCalcSBCrInt.getText()+"/");
     
     Date currentDate = (Date) curDate.clone();
     strDate.append(String.valueOf(currentDate.getYear()));
     
     Date workingDate = DateUtil.getDateMMDDYYYY(strDate.toString());
     
     GregorianCalendar calendar = new GregorianCalendar();
     calendar.setTime(workingDate);
     calendar.add(calendar.DATE, days);
     
     Date endDate = calendar.getTime();
     txtEndDayProdCalcSBCrInt.setText(String.valueOf(endDate.getDate()));
     int month = endDate.getMonth()+1;
     if(month < 10){
         cboEndDayProdCalcSBCrInt.setSelectedItem(((ComboBoxModel) cboEndDayProdCalcSBCrInt.getModel()).getDataForKey("0"+String.valueOf(month)));
     }else{
         cboEndDayProdCalcSBCrInt.setSelectedItem(((ComboBoxModel) cboEndDayProdCalcSBCrInt.getModel()).getDataForKey(String.valueOf(month)));
     }
 }
 
 private void computeEndProdDay(int months){
     txtEndDayProdCalcSBCrInt.setText(txtStDayProdCalcSBCrInt.getText());
     int realEndMonth = CommonUtil.convertObjToInt(((ComboBoxModel) cboStDayProdCalcSBCrInt.getModel()).getKeyForSelected()) + months;
     int endMonth = realEndMonth % 12;
     if(endMonth == 0){
         endMonth = 12;
     }
     
     if(endMonth < 10){
         cboEndDayProdCalcSBCrInt.setSelectedItem(((ComboBoxModel) cboEndDayProdCalcSBCrInt.getModel()).getDataForKey("0"+String.valueOf(endMonth)));
     }else{
         cboEndDayProdCalcSBCrInt.setSelectedItem(((ComboBoxModel) cboEndDayProdCalcSBCrInt.getModel()).getDataForKey(String.valueOf(endMonth)));
     }
 }
 private void cboProdFreqCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdFreqCrActionPerformed
     // TODO add your handling code here:
     observable.setCboProdFreqCr((String) ((ComboBoxModel) cboProdFreqCr.getModel()).getKeyForSelected());
     if(observable.getCboProdFreqCr()!=null){
         if(observable.getCboProdFreqCr().length() > 0){
             if(((String)observable.getCboProdFreqCr()).equalsIgnoreCase("1")
             ||((String)observable.getCboProdFreqCr()).equalsIgnoreCase("7")
             ||((String)observable.getCboProdFreqCr()).equalsIgnoreCase("30") ){
                 
                 
                 cboStDayProdCalcSBCrInt.setEnabled(false);
                 cboStDayProdCalcSBCrInt.setSelectedIndex(0);
                 
                 cboEndDayProdCalcSBCrInt.setEnabled(false);
                 cboEndDayProdCalcSBCrInt.setSelectedIndex(0);
                 if(((String)observable.getCboProdFreqCr()).equalsIgnoreCase("1")){
                     txtStDayProdCalcSBCrInt.setText("");
                     txtStDayProdCalcSBCrInt.setEditable(false);
                     txtStDayProdCalcSBCrInt.setEnabled(false);
                     
                     txtEndDayProdCalcSBCrInt.setText("");
                     txtEndDayProdCalcSBCrInt.setEditable(false);
                     txtEndDayProdCalcSBCrInt.setEnabled(false);
                 }else{
                     txtStDayProdCalcSBCrInt.setEditable(true);
                     txtStDayProdCalcSBCrInt.setEnabled(true);
                     
                     txtEndDayProdCalcSBCrInt.setEditable(true);
                     txtEndDayProdCalcSBCrInt.setEnabled(true);
                     
                 }
                 
             }else {
                 enableProdFreq(true);
             }
         }else if(((String)observable.getCboProdFreqCr()).equalsIgnoreCase("")){
             cboStDayProdCalcSBCrInt.setSelectedIndex(0);
             cboStDayProdCalcSBCrInt.setEnabled(true);
             
             cboEndDayProdCalcSBCrInt.setSelectedIndex(0);
             cboEndDayProdCalcSBCrInt.setEnabled(true);
             
             txtStDayProdCalcSBCrInt.setText("");
             txtStDayProdCalcSBCrInt.setEditable(true);
             txtStDayProdCalcSBCrInt.setEnabled(true);
             
             txtEndDayProdCalcSBCrInt.setText("");
             txtEndDayProdCalcSBCrInt.setEditable(true);
             txtEndDayProdCalcSBCrInt.setEnabled(true);
         }
     }
     // else {
     // enableProdFreq();
     // }
 }//GEN-LAST:event_cboProdFreqCrActionPerformed
 private void enableProdFreq(boolean value){
     txtStDayProdCalcSBCrInt.setEditable(value);
     txtStDayProdCalcSBCrInt.setEnabled(value);
     cboStDayProdCalcSBCrInt.setEnabled(value);
     
     txtEndDayProdCalcSBCrInt.setEditable(value);
     txtEndDayProdCalcSBCrInt.setEnabled(value);
     cboEndDayProdCalcSBCrInt.setEnabled(value);
 }
 
 private void txtMaxCrIntAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxCrIntAmtFocusLost
     // TODO add your handling code here:
     String message = txtMaxCrIntAmtRule();
     if(message.length() > 0){
         displayAlert(message);
     }
 }//GEN-LAST:event_txtMaxCrIntAmtFocusLost
 private String txtMaxCrIntAmtRule(){
     String message = "";
     
     if(!(txtMinCrIntAmt.getText().equalsIgnoreCase("")
     || txtMaxCrIntAmt.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtMinCrIntAmt.getText()) > Double.parseDouble(txtMaxCrIntAmt.getText()) ){
             message = resourceBundle.getString("MAXAMOUNTWARNING");
         }
     }
     return message;
 }
 private void txtApplCrIntRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApplCrIntRateFocusLost
     // TODO add your handling code here:
     String message = txtApplCrIntRateRule();
     if(message.length() > 0){
         displayAlert(message);
     }
 }//GEN-LAST:event_txtApplCrIntRateFocusLost
 private String txtApplCrIntRateRule(){
     String message = "";
     
     if(!(txtApplCrIntRate.getText().equalsIgnoreCase("")
     || txtMinCrIntRate.getText().equalsIgnoreCase("")
     || txtMaxCrIntRate.getText().equalsIgnoreCase(""))){
         double maxRate = Double.parseDouble(txtMaxCrIntRate.getText());
         double minRate = Double.parseDouble(txtMinCrIntRate.getText());
         double applicableRate = Double.parseDouble(txtApplCrIntRate.getText());
         
         if( (applicableRate > maxRate)
         || (applicableRate < minRate) ){
             message = resourceBundle.getString("INTERESTRANGEWARNING");
         }
     }
     return message;
 }
 private void txtMaxCrIntRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxCrIntRateFocusLost
     // TODO add your handling code here:
     String message = txtMaxCrIntRateRule();
     if(message.length() > 0){
         displayAlert(message);
     }
 }//GEN-LAST:event_txtMaxCrIntRateFocusLost
 private String txtMaxCrIntRateRule(){
     String message = "";
     
     if(!(txtMinCrIntRate.getText().equalsIgnoreCase("")
     || txtMaxCrIntRate.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtMinCrIntRate.getText()) > Double.parseDouble(txtMaxCrIntRate.getText()) ){
             message = resourceBundle.getString("MAXRATEWARNING");
         }
     }
     return message;
 }
 private void txtMaxDebitIntAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDebitIntAmtFocusLost
     // TODO add your handling code here:
     String message = txtMaxDebitIntAmtRule();
     if(message.length() > 0){
         displayAlert(message);
     }
 }//GEN-LAST:event_txtMaxDebitIntAmtFocusLost
 private String txtMaxDebitIntAmtRule(){
     String message = "";
     
     if(!(txtMinDebitIntAmt.getText().equalsIgnoreCase("")
     || txtMaxDebitIntAmt.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtMinDebitIntAmt.getText()) > Double.parseDouble(txtMaxDebitIntAmt.getText()) ){
             message = resourceBundle.getString("MAXAMOUNTWARNING");
         }
     }
     return message;
 }
 private void txtApplDebitIntRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtApplDebitIntRateFocusLost
     // TODO add your handling code here:
     String message = txtApplDebitIntRateRule();
     if(message.length() > 0){
         displayAlert(message);
     }
 }//GEN-LAST:event_txtApplDebitIntRateFocusLost
 private String txtApplDebitIntRateRule(){
     String message = "";
     
     if(!(txtApplDebitIntRate.getText().equalsIgnoreCase("")
     || txtMinDebitIntRate.getText().equalsIgnoreCase("")
     || txtMaxDebitIntRate.getText().equalsIgnoreCase(""))){
         double maxRate = Double.parseDouble(txtMaxDebitIntRate.getText());
         double minRate = Double.parseDouble(txtMinDebitIntRate.getText());
         double applicableRate = Double.parseDouble(txtApplDebitIntRate.getText());
         
         if( (applicableRate > maxRate)
         || (applicableRate < minRate) ){
             message = resourceBundle.getString("INTERESTRANGEWARNING");
         }
     }
     return message;
 }
 private void txtMaxDebitIntRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxDebitIntRateFocusLost
     // TODO add your handling code here:
     String message = txtMaxDebitIntRateRule();
     if(message.length() > 0){
         displayAlert(message);
     }
 }//GEN-LAST:event_txtMaxDebitIntRateFocusLost
 private String txtMaxDebitIntRateRule(){
     String message = "";
     
     if(!(txtMinDebitIntRate.getText().equalsIgnoreCase("")
     || txtMaxDebitIntRate.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtMinDebitIntRate.getText()) > Double.parseDouble(txtMaxDebitIntRate.getText()) ){
             message = resourceBundle.getString("MAXRATEWARNING");
         }
     }
     return message;
 }
 private void rdoIntClearing_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIntClearing_NoActionPerformed
     // TODO add your handling code here:
     if (rdoIntClearing_No.isSelected()) {
         disableClearing();
     }
 }//GEN-LAST:event_rdoIntClearing_NoActionPerformed
 private void disableClearing(){
     disablePanIntRec();
     txtClearingIntAcctHd.setText("");
     txtClearingIntAcctHd.setEditable(false);
     txtClearingIntAcctHd.setEnabled(false);
     btnClearingIntAcctHd.setEnabled(false);
 }
 private void rdoIntClearing_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIntClearing_YesActionPerformed
     // TODO add your handling code here:
     enablePanIntRec();
     txtClearingIntAcctHd.setEditable(true);
     txtClearingIntAcctHd.setEnabled(true);
     btnClearingIntAcctHd.setEnabled(true);
 }//GEN-LAST:event_rdoIntClearing_YesActionPerformed
 
 private void rdoTempODAllow_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTempODAllow_NoActionPerformed
     // TODO add your handling code here:
     if (rdoTempODAllow_No.isSelected()) {
         disablePanIntRec();
     }
 }//GEN-LAST:event_rdoTempODAllow_NoActionPerformed
 
 private void rdoTempODAllow_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTempODAllow_YesActionPerformed
     // TODO add your handling code here:
     enablePanIntRec();
 }//GEN-LAST:event_rdoTempODAllow_YesActionPerformed
 
 private void rdoLimitDefAllow_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLimitDefAllow_NoActionPerformed
     // TODO add your handling code here:
     if (rdoLimitDefAllow_No.isSelected()) {
         disablePanIntRec();
     }
 }//GEN-LAST:event_rdoLimitDefAllow_NoActionPerformed
 
 private void rdoLimitDefAllow_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLimitDefAllow_YesActionPerformed
     // TODO add your handling code here:
     enablePanIntRec();
 }//GEN-LAST:event_rdoLimitDefAllow_YesActionPerformed
 //panIntRec
 private void enablePanIntRec(){
     if( !(rdoLimitDefAllow_No.isSelected()
     && rdoTempODAllow_No.isSelected()
     && rdoIntClearing_No.isSelected())) {
         
         // ClientUtil.clearAll(panIntRec);
         ClientUtil.enableDisable(panIntRec, true);
         rdoDebitIntChrg_Yes2.setSelected(true);
     }
 }
 
 private void disablePanIntRec(){
     if (rdoLimitDefAllow_No.isSelected()
     && rdoTempODAllow_No.isSelected()
     && rdoIntClearing_No.isSelected()) {
         
         ClientUtil.clearAll(panIntRec);
         ClientUtil.enableDisable(panIntRec, false);
         rdoDebitIntChrg_No2.setSelected(true);
         rdoDebitIntChrg_No2.setEnabled(false);
         rdoDebitIntChrg_Yes2.setEnabled(false);
         
         // rdoDebitIntChrg_No2ActionPerformed(null);
     }
 }
 private void cboFreeChkLeaveStFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFreeChkLeaveStFromActionPerformed
     // TODO add your handling code here:
     observable.setCboFreeChkLeaveStFrom((String) ((ComboBoxModel) cboFreeChkLeaveStFrom.getModel()).getKeyForSelected());
     if(observable.getCboFreeChkLeaveStFrom().length() > 0){
         if(CommonUtil.convertObjToInt(observable.getCboFreeChkLeaveStFrom()) == SPECIFIC_DATE){
             tdtFreeChkLeaveStFrom.setEnabled(true);
         }else{
             tdtFreeChkLeaveStFrom.setDateValue("");
             tdtFreeChkLeaveStFrom.setEnabled(false);
         }
     }
 }//GEN-LAST:event_cboFreeChkLeaveStFromActionPerformed
 
 private void cboFreeWDStFromActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboFreeWDStFromActionPerformed
     // TODO add your handling code here:
     observable.setCboFreeWDStFrom((String) ((ComboBoxModel) cboFreeWDStFrom.getModel()).getKeyForSelected());
     if(observable.getCboFreeWDStFrom().length() > 0){
         if(CommonUtil.convertObjToInt(observable.getCboFreeWDStFrom()) == SPECIFIC_DATE){
             tdtFreeWDStFrom.setEnabled(true);
         }else{
             tdtFreeWDStFrom.setDateValue("");
             tdtFreeWDStFrom.setEnabled(false);
         }
     }
 }//GEN-LAST:event_cboFreeWDStFromActionPerformed
 
 private void txtNoFreeChkLeavesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoFreeChkLeavesFocusLost
     // TODO add your handling code here:
     if(!(txtNoFreeChkLeaves.getText().equalsIgnoreCase(""))){
         
         if(Double.parseDouble(txtNoFreeChkLeaves.getText())==0){
             disableFreeCheque();
         }else{
             enableFreeCheque();
             String message = txtNoFreeChkLeavesRule();
             if(message.length() > 0){
                 displayAlert(message);
             }
             txtFreeChkPD.setText(CommonUtil.convertObjToStr(txtFreeWDPd.getText()));
             cboFreeChkPd.setSelectedItem(cboFreeWDPd.getSelectedItem());
             cboFreeChkLeaveStFrom.setSelectedItem(cboFreeWDStFrom.getSelectedItem());
             tdtFreeChkLeaveStFrom.setDateValue(CommonUtil.convertObjToStr(tdtFreeWDStFrom.getDateValue()));
         }
     }
 }//GEN-LAST:event_txtNoFreeChkLeavesFocusLost
 private String txtNoFreeChkLeavesRule(){
     String message = "";
     if(!((txtNoFreeChkLeaves.getText().equalsIgnoreCase(""))
     || (txtNoFreeWD.getText().equalsIgnoreCase("")))){
         if(Double.parseDouble(txtNoFreeChkLeaves.getText())
         > Double.parseDouble(txtNoFreeWD.getText())){
             message = resourceBundle.getString("CHEQUELEAVESWARNING");
         }
     }
     return message;
 }
 
 
 private void enableFreeCheque(){
     txtFreeChkPD.setEnabled(true);
     txtFreeChkPD.setEditable(true);
     
     cboFreeChkPd.setEnabled(true);
     cboFreeChkLeaveStFrom.setEnabled(true);
     tdtFreeChkLeaveStFrom.setEnabled(true);
 }
 
 private void disableFreeCheque(){
     txtFreeChkPD.setText("");
     txtFreeChkPD.setEnabled(false);
     txtFreeChkPD.setEditable(false);
     
     cboFreeChkPd.setSelectedIndex(0);
     cboFreeChkPd.setEnabled(false);
     
     cboFreeChkLeaveStFrom.setSelectedIndex(0);
     cboFreeChkLeaveStFrom.setEnabled(false);
     tdtFreeChkLeaveStFrom.setDateValue("");
     tdtFreeChkLeaveStFrom.setEnabled(false);
 }
 
 private void disableCreditCompIntCalc(){
     cboCreditCompIntCalcFreq.setSelectedIndex(0);
     cboCreditCompIntCalcFreq.setEnabled(false);
 }
 private void txtNoFreeWDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoFreeWDFocusLost
     // TODO add your handling code here:
     if(!(txtNoFreeWD.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtNoFreeWD.getText())==0){
             txtFreeWDPd.setText("");
             txtFreeWDPd.setEnabled(false);
             txtFreeWDPd.setEditable(false);
             
             cboFreeWDPd.setSelectedIndex(0);
             cboFreeWDPd.setEnabled(false);
             
             cboFreeWDStFrom.setSelectedIndex(0);
             cboFreeWDStFrom.setEnabled(false);
             tdtFreeWDStFrom.setDateValue("");
             tdtFreeWDStFrom.setEnabled(false);
             
             txtExcessFreeWDChrgPT.setText("");
             txtExcessFreeWDChrgPT.setEnabled(false);
             txtExcessFreeWDChrgPT.setEditable(false);
         }else{
             txtFreeWDPd.setEnabled(true);
             txtFreeWDPd.setEditable(true);
             
             cboFreeWDPd.setEnabled(true);
             cboFreeWDStFrom.setEnabled(true);
             tdtFreeWDStFrom.setEnabled(true);
             
             txtExcessFreeWDChrgPT.setEnabled(true);
             txtExcessFreeWDChrgPT.setEditable(true);
         }
     }
 }//GEN-LAST:event_txtNoFreeWDFocusLost
 private void disableChkAllowed(){
//     txtMinBalwchk.setText("");
//     txtMinBalwchk.setEditable(false);
//     txtMinBalwchk.setEnabled(false);
     txtMinBalChkbk.setText("");
     txtMinBalChkbk.setEditable(false);
     txtMinBalChkbk.setEnabled(false);
     
     txtNoFreeChkLeaves.setText("");
     txtNoFreeChkLeaves.setEditable(false);
     txtNoFreeChkLeaves.setEnabled(false);
     disableFreeCheque();
 }
 
 private void disableMaxAmtWDSlip(){
     txtMaxAmtWDSlip.setText("");
     txtMaxAmtWDSlip.setEnabled(false);
     txtMaxAmtWDSlip.setEditable(false);
 }
 private void rdoChkAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChkAllowed_NoActionPerformed
     // TODO add your handling code here:
     if (rdoChkAllowed_No.isSelected()) {
         disableChkAllowed();
     }
 }//GEN-LAST:event_rdoChkAllowed_NoActionPerformed
 
 private void rdoChkAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChkAllowed_YesActionPerformed
     // TODO add your handling code here:
     txtMinBalwchk.setEditable(true);
     txtMinBalwchk.setEnabled(true);
     txtMinBalChkbk.setEnabled(true);
     
     txtNoFreeChkLeaves.setEditable(true);
     txtNoFreeChkLeaves.setEnabled(true);
     enableFreeCheque();
 }//GEN-LAST:event_rdoChkAllowed_YesActionPerformed
 
 private void txtAcctHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctHdFocusLost
     // TODO add your handling code here:
     if(!(txtAcctHd.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtAcctHd, "OperativeAcctProduct.getSelectAcctHeadTOList");
         btnAcctHd.setToolTipText(getAccHdDesc(txtAcctHd.getText()));
     }
 }//GEN-LAST:event_txtAcctHdFocusLost
   private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
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
       public void authorizeStatus(String authorizeStatus) {
           if (viewType == AUTHORIZE && isFilled){
               String strWarnMsg = tabOperativeAcctProduct.isAllTabsVisited();
               if (strWarnMsg.length() > 0){
                   displayAlert(strWarnMsg);
                   return;
               }
               strWarnMsg = null;
               tabOperativeAcctProduct.resetVisits();
               HashMap singleAuthorizeMap = new HashMap();
               singleAuthorizeMap.put("STATUS", authorizeStatus);
               singleAuthorizeMap.put("USER_ID", TrueTransactMain.USER_ID);
               singleAuthorizeMap.put("PROD_ID", txtProductID.getText());
               singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curDate);
               
//               java.util.List interestConfigAvl = ClientUtil.executeQuery("getCountIntConfig", singleAuthorizeMap);
//               int noOfInterestConfigAvl = CommonUtil.convertObjToInt(((HashMap)interestConfigAvl.get(0)).get("COUNT"));
//               if(noOfInterestConfigAvl>0){
                   ClientUtil.execute("authorizeOperativeAcctProduct", singleAuthorizeMap);
//               } else {
//                   ClientUtil.showAlertWindow("The Rate of interest for the product is not set.");
//               }
               viewType = 0;
               btnSave.setEnabled(true);
               btnCancelActionPerformed(null);
           } else {
               viewType = AUTHORIZE;
               isFilled = false;
               //__ To Save the data in the Internal Frame...
               setModified(true);
               HashMap mapParam = new HashMap();
               mapParam.put(CommonConstants.MAP_NAME, "getSelectOperativeAcctProductAuthorizeTOList");
               mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");
               mapParam.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
               
               AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
               authorizeUI.show();
               btnSave.setEnabled(false);
           }
       }
private void btnAcctHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctHdActionPerformed
    // Add your handling code here:
    callView(ACCTHD);
 }//GEN-LAST:event_btnAcctHdActionPerformed
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
 }//GEN-LAST:event_mitCloseActionPerformed
    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
        //btnPrintActionPerformed(evt);
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
        private void btnAcctClosingChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctClosingChrgActionPerformed
            // Add your handling code here:
            callView(ACCTCLOSINGCHRG);
 }//GEN-LAST:event_btnAcctClosingChrgActionPerformed
        private void btnMiscServChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMiscServChrgActionPerformed
            // Add your handling code here:
            callView(MISCSERVCHRG);
 }//GEN-LAST:event_btnMiscServChrgActionPerformed
        private void btnStatChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStatChrgActionPerformed
            // Add your handling code here:
            callView(STATCHRG);
 }//GEN-LAST:event_btnStatChrgActionPerformed
        private void btnFreeWDChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFreeWDChrgActionPerformed
            // Add your handling code here:
            callView(FREEWDCHRG);
 }//GEN-LAST:event_btnFreeWDChrgActionPerformed
         private void btnAcctDebitIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctDebitIntActionPerformed
             // Add your handling code here:
             callView(ACCTDEBITINT);
 }//GEN-LAST:event_btnAcctDebitIntActionPerformed
          private void btnAcctCrIntActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctCrIntActionPerformed
              // Add your handling code here:
              callView(ACCTCRINT);
 }//GEN-LAST:event_btnAcctCrIntActionPerformed
          private void btnClearingIntAcctHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearingIntAcctHdActionPerformed
              // Add your handling code here:
              callView(CLEARINGINTACCTHD);
 }//GEN-LAST:event_btnClearingIntAcctHdActionPerformed
          private void btnChkBkIssueChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChkBkIssueChrgActionPerformed
              // Add your handling code here:
              callView(CHKBKISSUECHRG);
 }//GEN-LAST:event_btnChkBkIssueChrgActionPerformed
           private void btnStopPaymentChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopPaymentChrgActionPerformed
               // Add your handling code here:
               callView(STOPPAYMENTCHRG);
 }//GEN-LAST:event_btnStopPaymentChrgActionPerformed
            private void btnOutwardChkRetChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOutwardChkRetChrgActionPerformed
                // Add your handling code here:
                callView(OUTWARDCHKRETCHRG);
 }//GEN-LAST:event_btnOutwardChkRetChrgActionPerformed
            private void btnInwardChkRetChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInwardChkRetChrgActionPerformed
                // Add your handling code here:
                callView(INWARDCHKRETCHRG);
 }//GEN-LAST:event_btnInwardChkRetChrgActionPerformed
            private void btnAcctOpenChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctOpenChrgActionPerformed
                // Add your handling code here:
                callView(ACCTOPENCHRG);
 }//GEN-LAST:event_btnAcctOpenChrgActionPerformed
             private void btnExcessFreeWDChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcessFreeWDChrgActionPerformed
                 // Add your handling code here:
                 callView(EXCESSFREEWDCHRG);
 }//GEN-LAST:event_btnExcessFreeWDChrgActionPerformed
             private void btnTaxGLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaxGLActionPerformed
                 // Add your handling code here:
                 callView(TAXGL);
 }//GEN-LAST:event_btnTaxGLActionPerformed
              private void btnNonMainMinBalChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNonMainMinBalChrgActionPerformed
                  // Add your handling code here:
                  callView(NONMAINMINBALCHRG);
 }//GEN-LAST:event_btnNonMainMinBalChrgActionPerformed
               private void btnInOperativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInOperativeActionPerformed
                   // Add your handling code here:
                   callView(INOPERATIVE);
 }//GEN-LAST:event_btnInOperativeActionPerformed
   
   // Added viewType ==  DEBIT_WITHDRAWAL_CHRG by nithya for 0004021           
   private void callView(int currField) {
       viewType = currField;
       HashMap viewMap = new HashMap();
       //if (currField == EDIT || currField == DELETE)
       if (currField == EDIT || currField == DELETE || currField == SAVEAS || currField == VIEW || currField == COPY){
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
           viewMap.put(CommonConstants.MAP_NAME, "getSelectOperativeAcctProductTOList");
       }else {
            HashMap whereMap = new HashMap();
            if (viewType == ACCTHD) {
                whereMap.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);
//                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                whereMap.put (BALANCETYPE, CommonConstants.CREDIT);
           } else if (viewType == ACCTCLOSINGCHRG || viewType == MISCSERVCHRG || 
                viewType == STATCHRG || viewType == FREEWDCHRG || viewType == CHKBKISSUECHRG || 
                viewType == STOPPAYMENTCHRG || viewType == OUTWARDCHKRETCHRG || viewType == INWARDCHKRETCHRG || 
                viewType == ACCTOPENCHRG || viewType == EXCESSFREEWDCHRG || 
                viewType == FOLIOCHRG  || viewType ==ATMID || viewType == NONMAINMINBALCHRG || 
                viewType == INOPCHRG || viewType == PREMATURECLOSURECHRG || viewType == DEBIT_WITHDRAWAL_CHRG) {
               whereMap.put (ACCT_TYPE, AcctStatusConstants.INCOME);
//               whereMap.put (ACCT_TYPE, AcctStatusConstants.INC_EXP);
               whereMap.put (BALANCETYPE, CommonConstants.DEBIT);
           } else if (viewType == ACCTDEBITINT) {
           } else if (viewType == ACCTCRINT) {
           } else if (viewType == CLEARINGINTACCTHD) {
           } else if (viewType == TAXGL) {
           } else if (viewType == INOPERATIVE) {
                whereMap.put(ACCT_TYPE, AcctStatusConstants.LIABILITY);
//                whereMap.put(ACCT_TYPE, AcctStatusConstants.AST_LIAB);
                whereMap.put(BALANCETYPE, CommonConstants.CREDIT);
           }
           viewMap.put(CommonConstants.MAP_WHERE, whereMap);
           viewMap.put(CommonConstants.MAP_NAME, "OperativeAcctProduct.getSelectAcctHeadTOList");
       }
       
       new ViewAll(this, viewMap).show();
   }
               
               private void disableNonMaintOfBalChanges(){
               cboMinBalAmt.setEnabled(false);
               cboMinBalAmt.setSelectedIndex(0);
                     
                txtMinBalAmt.setText("");
                txtMinBalAmt.setEditable(false);
                txtMinBalAmt.setEnabled(false);
                     
                txtNonMainMinBalChrg.setText("");
                txtNonMainMinBalChrg.setEditable(false);
                txtNonMainMinBalChrg.setEnabled(false);
                btnNonMainMinBalChrg.setEnabled(false);
                
               }
                private void disableStCharges(){
                    cboStatChargesChr.setSelectedIndex(0);
                    cboStatChargesChr.setEnabled(false);
                    txtStatChargesChr.setText("");
                    txtStatChargesChr.setEnabled(false);
                    txtStatChargesChr.setEditable(false);
                    
                    txtStatChrg.setText("");
                    txtStatChrg.setEnabled(false);
                    txtStatChrg.setEditable(false);
                    btnStatChrg.setEnabled(false);
                }
                
                 private void disableChqBkIssCharges(){
                   txtChkBkIssueChrgPL.setText("");
                   txtChkBkIssueChrgPL.setEnabled(false);
                   txtChkBkIssueChrgPL.setEditable(false);
                   
                   txtChkBkIssueChrg.setText("");
                   txtChkBkIssueChrg.setEnabled(false);
                   txtChkBkIssueChrg.setEditable(false);
                   btnChkBkIssueChrg.setEnabled(false);
                 }
                 

                 private void disableStopPayCharges(){
                  txtStopPayChrg.setText("");
                  txtStopPayChrg.setEditable(false);
                  txtStopPayChrg.setEnabled(false);
                  
                  txtStopPaymentChrg.setText("");
                  txtStopPaymentChrg.setEditable(false);
                  txtStopPaymentChrg.setEnabled(false);
                  btnStopPaymentChrg.setEnabled(false);
                 }
                  
                 private void disableLinktoFlexi(){
                    ClientUtil.clearAll(panSpecialDetails);
                    ClientUtil.enableDisable(panSpecialDetails, false);
                    rdoLinkFlexiAcct_No.setSelected(true);
                    rdoLinkFlexiAcct_No.setEnabled(true);
                    rdoLinkFlexiAcct_Yes.setEnabled(true);
                 }
                 
                 private void disableATNCardIss(){
                    txtMinATMBal.setText("");
                    txtMinATMBal.setEnabled(false);
                    txtMinATMBal.setEditable(false);
                }
                 
                 private void disableCrdtCardIss(){
                    txtMinBalCreditCd.setText("");
                    txtMinBalCreditCd.setEnabled(false);
                    txtMinBalCreditCd.setEditable(false);
                 }
                 
                  private void disableDbtCardIss(){
                      txtMinBalDebitCards.setText("");
                      txtMinBalDebitCards.setEnabled(false);
                      txtMinBalDebitCards.setEditable(false);
                  }
                  
                  private void disableIVRS(){
                        txtMinBalIVRS.setText("");
                        txtMinBalIVRS.setEnabled(false);
                        txtMinBalIVRS.setEditable(false);
                  }
                  
                  private void disableMobilebnkClient(){
                      txtMinMobBank.setText("");
                      txtMinMobBank.setEnabled(false);
                      txtMinMobBank.setEditable(false);
                  }
                  
                   private void disableABB(){
                        txtMinBalABB.setText("");
                        txtMinBalABB.setEnabled(false);
                        txtMinBalABB.setEditable(false);
                   }
                   
                   // Added by nithya on 17-03-2016 for 0004021
                   private void disableDebitWithdrawalCharge(){
                       txtDebiWithdrawalChargePeriod.setEnabled(false); 
                       txtDebitChargeTypeRate.setEnabled(false);
                       cboDebitChargeType.setEnabled(false);
                   }
 private void btnFolioChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFolioChrgActionPerformed
     // Add your handling code here:
     callView(FOLIOCHRG);
 }//GEN-LAST:event_btnFolioChrgActionPerformed
  private void btnPrematureClosureChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrematureClosureChrgActionPerformed
      // Add your handling code here:
      callView(PREMATURECLOSURECHRG);
 }//GEN-LAST:event_btnPrematureClosureChrgActionPerformed
  private void btnInOpChrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInOpChrgActionPerformed
      // Add your handling code here:
      callView(INOPCHRG);
 }//GEN-LAST:event_btnInOpChrgActionPerformed
  private void rdoABBAllowed_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoABBAllowed_NoActionPerformed
      // Add your handling code here:
      if (rdoABBAllowed_No.isSelected()) {
          disableABB();
      } else {
          txtMinBalABB.setEnabled(true);
          txtMinBalABB.setEditable(true);
      }
 }//GEN-LAST:event_rdoABBAllowed_NoActionPerformed
   private void rdoABBAllowed_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoABBAllowed_YesActionPerformed
       // Add your handling code here:
       rdoABBAllowed_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoABBAllowed_YesActionPerformed
   private void rdoMobBankClient_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMobBankClient_NoActionPerformed
       // Add your handling code here:
       if (rdoMobBankClient_No.isSelected()) {
           disableMobilebnkClient();
       } else {
           txtMinMobBank.setEnabled(true);
           txtMinMobBank.setEditable(true);
       }
 }//GEN-LAST:event_rdoMobBankClient_NoActionPerformed
    private void rdoMobBankClient_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoMobBankClient_YesActionPerformed
        // Add your handling code here:
        rdoMobBankClient_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoMobBankClient_YesActionPerformed
    private void rdoIVRSProvided_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIVRSProvided_NoActionPerformed
        // Add your handling code here:
        if (rdoIVRSProvided_No.isSelected()) {
            disableIVRS();
        } else {
            txtMinBalIVRS.setEnabled(true);
            txtMinBalIVRS.setEditable(true);
        }
 }//GEN-LAST:event_rdoIVRSProvided_NoActionPerformed
     private void rdoIVRSProvided_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIVRSProvided_YesActionPerformed
         // Add your handling code here:
         rdoIVRSProvided_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoIVRSProvided_YesActionPerformed
      private void rdoDebitCdIssued_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitCdIssued_NoActionPerformed
          // Add your handling code here:
          if (rdoDebitCdIssued_No.isSelected()) {
              disableDbtCardIss();
          } else {
              txtMinBalDebitCards.setEnabled(true);
              txtMinBalDebitCards.setEditable(true);
          }
 }//GEN-LAST:event_rdoDebitCdIssued_NoActionPerformed
       private void rdoDebitCdIssued_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitCdIssued_YesActionPerformed
           // Add your handling code here:
           rdoDebitCdIssued_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoDebitCdIssued_YesActionPerformed
       private void rdoCreditCdIssued_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditCdIssued_NoActionPerformed
           // Add your handling code here:
           if (rdoCreditCdIssued_No.isSelected()) {
               disableCrdtCardIss();
           } else {
               txtMinBalCreditCd.setEnabled(true);
               txtMinBalCreditCd.setEditable(true);
           }
 }//GEN-LAST:event_rdoCreditCdIssued_NoActionPerformed
        private void rdoCreditCdIssued_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditCdIssued_YesActionPerformed
            // Add your handling code here:
            rdoCreditCdIssued_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoCreditCdIssued_YesActionPerformed
        private void rdoATMIssued_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoATMIssued_NoActionPerformed
            // Add your handling code here:
            if (rdoATMIssued_No.isSelected()) {
                disableATNCardIss();
            } else {
                txtMinATMBal.setEnabled(true);
                txtMinATMBal.setEditable(true);
            }
 }//GEN-LAST:event_rdoATMIssued_NoActionPerformed
        private void rdoATMIssued_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoATMIssued_YesActionPerformed
            // Add your handling code here:
            rdoATMIssued_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoATMIssued_YesActionPerformed
        private void rdoLinkFlexiAcct_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLinkFlexiAcct_NoActionPerformed
            // Add your handling code here:
            if (rdoLinkFlexiAcct_No.isSelected()) {
                disableLinktoFlexi();
                // txtMinBal1FlexiDep.setText("");
                // txtMinBal1FlexiDep.setEnabled(false);
                // txtMinBal2FlexiDep.setText("");
                // txtMinBal2FlexiDep.setEnabled(false);
                // rdoFlexiHappen_SB.setSelected(false);
                // rdoFlexiHappen_SB.setEnabled(false);
                // rdoFlexiHappen_TD.setSelected(false);
                // rdoFlexiHappen_TD.setEnabled(false);
            }
 }//GEN-LAST:event_rdoLinkFlexiAcct_NoActionPerformed
        private void rdoLinkFlexiAcct_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLinkFlexiAcct_YesActionPerformed
            // Add your handling code here:
            // ClientUtil.clearAll(panSpecialDetails);
            ClientUtil.enableDisable(panSpecialDetails, true);
 }//GEN-LAST:event_rdoLinkFlexiAcct_YesActionPerformed
         private void rdoFolioChargeAppl_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFolioChargeAppl_NoActionPerformed
             // Add your handling code here:
             if (rdoFolioChargeAppl_No.isSelected()) {
                 disableFolioChrAppl();
             }
 }//GEN-LAST:event_rdoFolioChargeAppl_NoActionPerformed
          private void rdoFolioChargeAppl_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFolioChargeAppl_YesActionPerformed
              // Add your handling code here:
              // ClientUtil.clearAll(panFolioData);
              ClientUtil.enableDisable(panFolioData, true);
              
              txtFolioChrg.setEditable(true);
              txtFolioChrg.setEnabled(true);
              btnFolioChrg.setEnabled(true);
 }//GEN-LAST:event_rdoFolioChargeAppl_YesActionPerformed
          private void rdoStopPaymentChrg_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStopPaymentChrg_NoActionPerformed
              // Add your handling code here:
              if (rdoStopPaymentChrg_No.isSelected()) {
                  disableStopPayCharges();
                  
              } else {
                  txtStopPayChrg.setEnabled(true);
                  txtStopPayChrg.setEditable(true);
                  
                  txtStopPaymentChrg.setEditable(true);
                  txtStopPaymentChrg.setEnabled(true);
                  btnStopPaymentChrg.setEnabled(true);
              }
 }//GEN-LAST:event_rdoStopPaymentChrg_NoActionPerformed
          private void rdoStopPaymentChrg_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStopPaymentChrg_YesActionPerformed
              // Add your handling code here:
              rdoStopPaymentChrg_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoStopPaymentChrg_YesActionPerformed
           private void rdoChkIssuedChrgCh_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChkIssuedChrgCh_NoActionPerformed
               // Add your handling code here:
               if (rdoChkIssuedChrgCh_No.isSelected()) {
                   disableChqBkIssCharges();
                   
               } else {
                   txtChkBkIssueChrgPL.setEnabled(true);
                   txtChkBkIssueChrgPL.setEditable(true);
                   
                   txtChkBkIssueChrg.setEnabled(true);
                   txtChkBkIssueChrg.setEditable(true);
                   btnChkBkIssueChrg.setEnabled(true);
               }
 }//GEN-LAST:event_rdoChkIssuedChrgCh_NoActionPerformed
            private void rdoChkIssuedChrgCh_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChkIssuedChrgCh_YesActionPerformed
                // Add your handling code here:
                rdoChkIssuedChrgCh_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoChkIssuedChrgCh_YesActionPerformed
            private void rdoStatCharges_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStatCharges_NoActionPerformed
                // Add your handling code here:
                if (rdoStatCharges_No.isSelected()) {
                    disableStCharges();
                    
                } else {
                    cboStatChargesChr.setEnabled(true);
                    
                    txtStatChargesChr.setEnabled(true);
                    txtStatChargesChr.setEditable(true);
                    
                    txtStatChrg.setEnabled(true);
                    txtStatChrg.setEditable(true);
                    btnStatChrg.setEnabled(true);
                }
 }//GEN-LAST:event_rdoStatCharges_NoActionPerformed
            private void rdoStatCharges_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStatCharges_YesActionPerformed
                // Add your handling code here:
                rdoStatCharges_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoStatCharges_YesActionPerformed
             private void rdoNonMainMinBalChrg_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNonMainMinBalChrg_NoActionPerformed
                 // Add your handling code here:
                 if (rdoNonMainMinBalChrg_No.isSelected()) {
                     disableNonMaintOfBalChanges();
                 } else {
                     cboMinBalAmt.setEnabled(true);
                     
                     txtMinBalAmt.setEnabled(true);
                     txtMinBalAmt.setEditable(true);
                     
                     txtNonMainMinBalChrg.setEditable(true);
                     txtNonMainMinBalChrg.setEnabled(true);
                     btnNonMainMinBalChrg.setEnabled(true);
                 }
 }//GEN-LAST:event_rdoNonMainMinBalChrg_NoActionPerformed
             private void rdoNonMainMinBalChrg_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNonMainMinBalChrg_YesActionPerformed
                 // Add your handling code here:
                 rdoNonMainMinBalChrg_NoActionPerformed(null);
 }//GEN-LAST:event_rdoNonMainMinBalChrg_YesActionPerformed
              private void rdoCreditComp_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditComp_NoActionPerformed
                  // Add your handling code here:
                  if (rdoCreditComp_No.isSelected()) {
                      disableCreditCompIntCalc();
                  }
 }//GEN-LAST:event_rdoCreditComp_NoActionPerformed
               private void rdoCreditComp_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoCreditComp_YesActionPerformed
                   // Add your handling code here:
                   rdoCreditComp_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoCreditComp_YesActionPerformed
                private void rdoDebitCompReq_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitCompReq_YesActionPerformed
                    // Add your handling code here:
                    cboDebitCompIntCalcFreq.setEnabled(true);
 }//GEN-LAST:event_rdoDebitCompReq_YesActionPerformed
                private void rdoDebitCompReq_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitCompReq_NoActionPerformed
                    // Add your handling code here:
                    if (rdoDebitCompReq_No.isSelected()) {
                        disableDebitComp();
                    }
 }//GEN-LAST:event_rdoDebitCompReq_NoActionPerformed
                 private void rdoDebitIntChrg_Yes2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitIntChrg_Yes2ActionPerformed
                     // Add your handling code here:
                     // rdoDebitIntChrg_No2ActionPerformed(evt);
                     // ClientUtil.clearAll(panIntRec);
                     ClientUtil.enableDisable(panIntRec, true);
                     txtAcctDebitInt.setEditable(true);
                     txtAcctDebitInt.setEnabled(true);
                     btnAcctDebitInt.setEnabled(true);
 }//GEN-LAST:event_rdoDebitIntChrg_Yes2ActionPerformed
                  private void rdoDebitIntChrg_No2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDebitIntChrg_No2ActionPerformed
                      // Add your handling code here:
                      if (rdoDebitIntChrg_No2.isSelected()) {
                          disableDebitDet();
                          // txtMinDebitIntRate.setText("");
                          // txtMinDebitIntRate.setEnabled(false);
                          // txtMaxDebitIntRate.setText("");
                          // txtMaxDebitIntRate.setEnabled(false);
                          // txtApplDebitIntRate.setText("");
                          // txtApplDebitIntRate.setEnabled(false);
                          // txtMinDebitIntAmt.setText("");
                          // txtMinDebitIntAmt.setEnabled(false);
                          // txtMaxDebitIntAmt.setText("");
                          // txtMaxDebitIntAmt.setEnabled(false);
                          // cboDebitIntCalcFreq.setSelectedIndex(0);
                          // cboDebitIntCalcFreq.setEnabled(false);
                          // cboDebitIntApplFreq.setSelectedIndex(0);
                          // cboDebitIntApplFreq.setEnabled(false);
                      }
                      // else {
                      // txtMinDebitIntRate.setEnabled(true);
                      // txtMaxDebitIntRate.setEnabled(true);
                      // txtApplDebitIntRate.setEnabled(true);
                      // txtMinDebitIntAmt.setEnabled(true);
                      // txtMaxDebitIntAmt.setEnabled(true);
                      // cboDebitIntCalcFreq.setEnabled(true);
                      // cboDebitIntApplFreq.setEnabled(true);
                      // }
 }//GEN-LAST:event_rdoDebitIntChrg_No2ActionPerformed
                   private void rdoAllowWD_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAllowWD_NoActionPerformed
                       // Add your handling code here:
                       if (rdoAllowWD_No.isSelected()) {
                           disableMaxAmtWDSlip();
                       } else {
                           txtMaxAmtWDSlip.setEnabled(true);
                           txtMaxAmtWDSlip.setEditable(true);
                       }
 }//GEN-LAST:event_rdoAllowWD_NoActionPerformed
                   private void rdoAllowWD_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoAllowWD_YesActionPerformed
                       // Add your handling code here:
                       rdoAllowWD_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoAllowWD_YesActionPerformed
                   private void rdoTaxIntApplNRO_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTaxIntApplNRO_YesActionPerformed
                       // Add your handling code here:
                       rdoTaxIntApplNRO_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoTaxIntApplNRO_YesActionPerformed
                   private void rdoTaxIntApplNRO_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoTaxIntApplNRO_NoActionPerformed
                       // Add your handling code here:
                       if (rdoTaxIntApplNRO_No.isSelected()) {
                           disableTaxNROandGL();
                       } else {
                           txtRateTaxNRO.setEnabled(true);
                           txtRateTaxNRO.setEditable(true);
                           
                           txtTaxGL.setEditable(true);
                           txtTaxGL.setEnabled(true);
                           btnTaxGL.setEnabled(true);
                       }
 }//GEN-LAST:event_rdoTaxIntApplNRO_NoActionPerformed
                   private void rdoNomineeReq_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNomineeReq_YesActionPerformed
                       // Add your handling code here:
                       rdoNomineeReq_NoActionPerformed(evt);
 }//GEN-LAST:event_rdoNomineeReq_YesActionPerformed
                    private void rdoNomineeReq_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNomineeReq_NoActionPerformed
                        // Add your handling code here:
                        if (rdoNomineeReq_No.isSelected()) {
                            txtNoNominees.setText("");
                            disableNominee();
                        } else {
//                            txtNoNominees.setEnabled(true);
//                            txtNoNominees.setEditable(true);
                            txtNoNominees.setText("1");
                            disableNominee();
                        }
 }//GEN-LAST:event_rdoNomineeReq_NoActionPerformed
                     private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
                         // Add your handling code here:
                         super.removeEditLock(txtProductID.getText());
                         observable.resetOBFields();
                         ClientUtil.clearAll(panOperativeProduct);
                         //ClientUtil.clearAll(panAcctHead);
                         ClientUtil.enableDisable(this, false);
                         
                         setButtonEnableDisable();
                         setFolderButtonEnableDisable(false);
                         setFolderTxtEnableDisable(false);
                         enableProdFreqData(false);
                         enableProdFreq(false);
                         viewType = 0;
                         setCommand(ClientConstants.ACTIONTYPE_CANCEL);
                         
                         //__ To Save the data in the Internal Frame...
                        setModified(false);
                        btnReject.setEnabled(true);
                        btnAuthorize.setEnabled(true);
                        btnException.setEnabled(true);
                        btnView.setEnabled(true);
                        btnCopy.setEnabled(true);
                        txtInOpChrgDesc.setText("");
                        txtAcctCrIntDesc.setText("");
                        
 }//GEN-LAST:event_btnCancelActionPerformed
                     private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
                         // Add your handling code here:
                         setCommand(ClientConstants.ACTIONTYPE_DELETE);
                         callView(DELETE);
                          btnReject.setEnabled(false);
                      btnAuthorize.setEnabled(false);
                         btnException.setEnabled(false);
 }//GEN-LAST:event_btnDeleteActionPerformed
                     private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
                         // Add your handling code here:
                         setCommand(ClientConstants.ACTIONTYPE_EDIT);
                         callView(EDIT);
                          btnReject.setEnabled(false);
                           btnAuthorize.setEnabled(false);
                         btnException.setEnabled(false);
                         ClientUtil.enableDisable(panTaxIntApplNRO,false);
                       //  txtAcctCrInt.setFocusable(true);
                         disDesc();
 }//GEN-LAST:event_btnEditActionPerformed
                      private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                          // Add your handling code here:
                          System.out.println("llllllllllllllllllll");
                          updateOBFields();
                          String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panOperativeProduct);
                          StringBuffer strBAlert = new StringBuffer();
                          if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                              strBAlert.append(mandatoryMessage+"\n");
                          }
                          if(txtNoFreeChkLeavesRule().length() > 0){
                              strBAlert.append(txtNoFreeChkLeavesRule()+"\n");
                          }
                          if(txtMaxDebitIntRateRule().length() > 0){
                              strBAlert.append(txtMaxDebitIntRateRule()+"\n");
                          }
                          if(txtMaxDebitIntAmtRule().length() > 0){
                              strBAlert.append(txtMaxDebitIntAmtRule()+"\n");
                          }
                          if(txtApplDebitIntRateRule().length() > 0){
                              strBAlert.append(txtApplDebitIntRateRule()+"\n");
                          }
                          if(txtMaxCrIntRateRule().length() > 0){
                              strBAlert.append(txtMaxCrIntRateRule()+"\n");
                          }
                          if(txtApplCrIntRateRule().length() > 0){
                              strBAlert.append(txtApplCrIntRateRule()+"\n");
                          }
                          if(txtMaxCrIntAmtRule().length() > 0){
                              strBAlert.append(txtMaxCrIntAmtRule()+"\n");
                          }
                          if(txtNumPatternFollowedPrefix.getText().length()<=0)
                          {
                            strBAlert.append(resourceBundle.getString("PREFIXWARNING"));  
                          }
                          if(txtNumPatternFollowedSuffix.getText().length()<=0)
                          {
                            strBAlert.append(resourceBundle.getString("SUFFIXWARNING"));  
                          }
                          //__ Valideting the Length of the Combination for the Days...
                            String str = "";
                            str = validateAllPeriodLength();
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            
                            //__ To validate the Max days in the Selected Month...
                            str = validateAllMaxOfMonth();
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            

                          if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && strBAlert.toString().length() > 0 ){
                             // System.out.println("sfsfsfsdfsdfsdf");
                              displayAlert(strBAlert.toString());
                              return;
                          }else{
                              //System.out.println("dgdgdhgjhg");
                              if (!(observable.isProdIdExist(txtProductID.getText(), txtDesc.getText()))){
                                  updateOBFields();
                                  observable.doAction();
                                  //__ if the Action is not Falied, Reset the fields...
                                    if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                                        super.removeEditLock(txtProductID.getText()); 
                                        observable.resetOBFields();
                                          ClientUtil.clearAll(panOperativeProduct);
                                          ClientUtil.enableDisable(this, false);
                                          setButtonEnableDisable();
                                          setFolderButtonEnableDisable(false);
                                          setFolderTxtEnableDisable(false);
                                          //ClientUtil.clearAll(panIntRateDetails);
                                          enableProdFreqData(false);
                                          enableProdFreq(false);
                                          lblStatus.setText(observable.getLblStatus());
                                    }
                              }
                          }
                          //__ To Save the data in the Internal Frame...
                            setModified(false);
                             btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
 }//GEN-LAST:event_btnSaveActionPerformed
                       private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
                           // Add your handling code here:
                           ClientUtil.clearAll(panOperativeProduct);
                           ClientUtil.enableDisable(panOperativeProduct, true);
                           // ClientUtil.enableDisable(this, true);
                           txtProductID.setEditable(true);
                           setButtonEnableDisable();
                           setFolderButtonEnableDisable(true);
                           setFolderTxtEnableDisable(true);
                           txtMiscServChrg.setEditable(false);
                           txtMiscServChrg.setEnabled(false);
                           //__ To Save the data in the Internal Frame...
                            setModified(true);
                            rdoAcc_Reg.setSelected(true);
                           //Disable panIntRateDetails
 /*
 ClientUtil.enableDisable(panIntRateDetails,false);
 btnIRSave.setEnabled(false);
 btnIRDelete.setEnabled(false);
 btnIRAcctHd.setEnabled(false);
  */
                           disableChkAllowed();
                           disableNominee();
                           disableTaxNROandGL();
                           disablePanIntRec();
                           disableClearing();
                           disableMaxAmtWDSlip();
                           disableDebitDet();
                           disableDebitComp();
                           disableCreditInt();
                           disableCreditCompIntCalc();
                           disableNonMaintOfBalChanges();
                           disableStCharges();
                           disableLinktoFlexi();
                           disableATNCardIss();
                           disableCrdtCardIss();
                           disableDbtCardIss();
                           disableIVRS();
                           disableMobilebnkClient();
                           disableABB();
                           disableFolioChrAppl();
                           disableChqBkIssCharges();
                           disableStopPayCharges();
                           btnMiscServChrg.setEnabled(false);
                           setCommand(ClientConstants.ACTIONTYPE_NEW);
                            btnReject.setEnabled(false);
                           btnAuthorize.setEnabled(false);
                          btnException.setEnabled(false);
                          btnCopy.setEnabled(false);
                          ClientUtil.enableDisable(panTaxIntApplNRO,false);
                          disDesc();
                          disableDebitWithdrawalCharge();
 }//GEN-LAST:event_btnNewActionPerformed
                       private void displayAlert(String message){
                           CMandatoryDialog cmd = new CMandatoryDialog();
                           cmd.setMessage(message);
                           cmd.setModal(true);
                           cmd.show();
                       }
                       private String periodLengthValidation(CTextField txtField, CComboBox comboField){
                            String message = "";
                            String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
                            if (!ClientUtil.validPeriodMaxLength(txtField, key)){
                                System.out.println("key: " +key);
                                System.out.println("Name: " +txtField.getName());
                                message = objMandatoryRB.getString(txtField.getName());
                                System.out.println("Message: " +message);
                            }
                            return message;
                        }
                       
                       private String validateAllPeriodLength(){
                           StringBuffer strBAlert = new StringBuffer();
                           String str = "";
                           
                           str = periodLengthValidation(txtMinTreatasNew, cboMinTreatasNew);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = periodLengthValidation(txtMinTreatasDormant, cboMinTreatasDormant);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = periodLengthValidation(txtMinTreatasInOp, cboMinTreatInOp);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = periodLengthValidation(txtMainTreatNewAcctClosure, cboMinTreatNewClosure);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = periodLengthValidation(txtFreeWDPd, cboFreeWDPd);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = periodLengthValidation(txtFreeChkPD, cboFreeChkPd);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            return strBAlert.toString();
                       }
                       
                       private String validateAllMaxOfMonth(){
                           StringBuffer strBAlert = new StringBuffer();
                           String str = "";
                           
                           str = ValidateMaxDays(txtStartInterCalc, cboStartInterCalc);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = ValidateMaxDays(txtEndInterCalc, cboEndInterCalc);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = ValidateMaxDays(txtStartProdCalc, cboStartProdCalc);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = ValidateMaxDays(txtEndProdCalc, cboEndProdCalc);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = ValidateMaxDays(txtStMonIntCalc, cboStMonIntCalc);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = ValidateMaxDays(txtIntCalcEndMon, cboIntCalcEndMon);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = ValidateMaxDays(txtStDayProdCalcSBCrInt, cboStDayProdCalcSBCrInt);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            str = ValidateMaxDays(txtEndDayProdCalcSBCrInt, cboEndDayProdCalcSBCrInt);
                            if(str.length() > 0){
                                strBAlert.append(str+"\n");
                                str = "";
                            }
                            
                            return strBAlert.toString();
                       }
    
                       
                       
                       private void disableDebitComp(){
                        cboDebitCompIntCalcFreq.setSelectedIndex(0);
                        cboDebitCompIntCalcFreq.setEnabled(false);
                       }
                       
                       private void disableDebitDet(){
                           ClientUtil.clearAll(panIntRec);
                          ClientUtil.enableDisable(panIntRec, false);
                          rdoDebitIntChrg_No2.setSelected(true);
                          rdoDebitIntChrg_No2.setEnabled(true);
                          rdoDebitIntChrg_Yes2.setEnabled(true);
                          
                          txtAcctDebitInt.setText("");
                          txtAcctDebitInt.setEditable(false);
                          txtAcctDebitInt.setEnabled(false);
                          btnAcctDebitInt.setEnabled(false);
                          
                          disablePanIntRec();
                       }
                       
                       private void disableTaxNROandGL(){
                           txtRateTaxNRO.setText("");
                           txtRateTaxNRO.setEditable(false);
                           txtRateTaxNRO.setEnabled(false);

                           txtTaxGL.setText("");
                           txtTaxGL.setEditable(false);
                           txtTaxGL.setEnabled(false);
                           btnTaxGL.setEnabled(false);
                       }
                       
                       private void disableNominee(){
//                           txtNoNominees.setText("");
                           txtNoNominees.setEnabled(false);
                           txtNoNominees.setEditable(false);
                       }
                       
                       private void disableFolioChrAppl(){
                         ClientUtil.clearAll(panFolioData);
                         ClientUtil.enableDisable(panFolioData, false);
                         rdoFolioChargeAppl_No.setSelected(true);
                         rdoFolioChargeAppl_No.setEnabled(true);
                         rdoFolioChargeAppl_Yes.setEnabled(true);

                         txtFolioChrg.setText("");
                         txtFolioChrg.setEditable(false);
                         txtFolioChrg.setEnabled(false);
                         btnFolioChrg.setEnabled(false);
                       }
                       
                       public void fillData(Object obj) {
                           HashMap hash = (HashMap) obj;
                           if (viewType != 0) {
                               //if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE) {
                               if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE || viewType == SAVEAS || viewType == VIEW || viewType == COPY) {
                                   ClientUtil.enableDisable(this, true);
                                   setFolderButtonEnableDisable(true);
                                   setFolderTxtEnableDisable(true);
                                   
                                   isFilled = true;
                                   hash.put("WHERE", hash.get("PROD_ID"));
                                   observable.populateData(hash);
                                   update(null, null);
                                   setButtonEnableDisable();
                                   btnCopy.setEnabled(false);
                                   //setIntRatePan(false);
                                   if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE || viewType == VIEW) {
                                       ClientUtil.enableDisable(this, false);
                                       setFolderButtonEnableDisable(false);
                                       setFolderTxtEnableDisable(false);
                                   }
                                   else {

                                       // ClientUtil.enableDisable(this, true);
                                       //setIntRatePan(false);
                                       // btnIRAdd.setEnabled(true);
                                       txtProductID.setEditable(false);
                                       // setEnableDisableAccordingToRdoBtns();
                                       // setCommand(ClientConstants.ACTIONTYPE_EDIT);
                                   }
                                   if(viewType == COPY) {
                                       txtProductID.setEnabled(true);
                                       txtProductID.setEditable(true);
                                       btnCopy.setEnabled(false);
                                       txtLastAccNum.setText("");
                                       
                                   }
/*ClientUtil.enableDisable(panIntRateDetails,false);
 
btnIRSave.setEnabled(false);
btnIRDelete.setEnabled(false);
btnIRAcctHd.setEnabled(false);*/
                                   
                               } else if (viewType == ACCTHD) {
                                   txtAcctHd.setText((String) hash.get(ACHDID));
                                   btnAcctHd.setToolTipText((String) hash.get(ACHDDESC));
                                   txtAcctHdFocusLost(null);
                               } else if (viewType == ACCTCLOSINGCHRG) {
                                   txtAcctClosingChrg.setText((String) hash.get(ACHDID));
                                   txtAcctClosingChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtAcctClosingChrgDesc.setEnabled(false);
                                   txtAcctClosingChrgFocusLost(null);
                               } else if (viewType == MISCSERVCHRG) {
                                   txtMiscServChrg.setText((String) hash.get(ACHDID));
                                   txtMiscServChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtMiscServChrgDesc.setEnabled(false);
                                   txtMiscServChrgFocusLost(null);
                               } else if (viewType == STATCHRG) {
                                   txtStatChrg.setText((String) hash.get(ACHDID));
                                   txtStatChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtStatChrgDesc.setEnabled(false);
                                   txtStatChrgFocusLost(null);
                               } else if (viewType == FREEWDCHRG) {
                                   txtFreeWDChrg.setText((String) hash.get(ACHDID));
                                   txtFreeWDChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtFreeWDChrgDesc.setEnabled(false);
                                   txtFreeWDChrgFocusLost(null);
                               } else if (viewType == ACCTDEBITINT) {
                                   txtAcctDebitInt.setText((String) hash.get(ACHDID));
                                   txtAcctDebitIntDesc.setText((String) hash.get(ACHDDESC));
                                   txtAcctDebitIntDesc.setEnabled(false);
                                   txtAcctDebitIntFocusLost(null);
                               } else if (viewType == ACCTCRINT) {
                                   txtAcctCrInt.setText((String) hash.get(ACHDID));
                                   txtAcctCrIntDesc.setText((String) hash.get(ACHDDESC));
                                   txtAcctCrIntDesc.setEnabled(false);
                                   txtAcctCrIntFocusLost(null);
                               } else if (viewType == CLEARINGINTACCTHD) {
                                   txtClearingIntAcctHd.setText((String) hash.get(ACHDID));
                                   txtClearingIntAcctHdDesc.setText((String) hash.get(ACHDDESC));
                                   txtClearingIntAcctHdDesc.setEnabled(false);
                                   txtClearingIntAcctHdFocusLost(null);
                               } else if (viewType == CHKBKISSUECHRG) {
                                   txtChkBkIssueChrg.setText((String) hash.get(ACHDID));
                                   txtChkBkIssueChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtChkBkIssueChrgDesc.setEnabled(false);
                                   txtChkBkIssueChrgFocusLost(null);
                               } else if (viewType == STOPPAYMENTCHRG) {
                                   txtStopPaymentChrg.setText((String) hash.get(ACHDID));
                                   txtStopPaymentChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtStopPaymentChrgDesc.setEnabled(false);
                                   txtStopPaymentChrgFocusLost(null);
                               } else if (viewType == OUTWARDCHKRETCHRG) {
                                   txtOutwardChkRetChrg.setText((String) hash.get(ACHDID));
                                  txtOutwardChkRetChrgDesc.setText((String) hash.get(ACHDDESC));
                                  txtOutwardChkRetChrgDesc.setEnabled(false);
                                   txtOutwardChkRetChrgFocusLost(null);
                               } else if (viewType == INWARDCHKRETCHRG) {
                                   txtInwardChkRetChrg.setText((String) hash.get(ACHDID));
                                   txtInwardChkRetChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtInwardChkRetChrgDesc.setEnabled(false);
                                   txtInwardChkRetChrgFocusLost(null);
                               } else if (viewType == ACCTOPENCHRG) {
                                   txtAcctOpenChrg.setText((String) hash.get(ACHDID));
                                   txtAcctOpenChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtAcctOpenChrgDesc.setEnabled(false);
                                   txtAcctOpenChrgFocusLost(null);
                               } else if (viewType == EXCESSFREEWDCHRG) {
                                   txtExcessFreeWDChrg.setText((String) hash.get(ACHDID));
                                   txtExcessFreeWDChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtExcessFreeWDChrgDesc.setEnabled(false);
                                   txtExcessFreeWDChrgFocusLost(null);
                               } else if (viewType == TAXGL) {
                                   txtTaxGL.setText((String) hash.get(ACHDID));
                                    txtTaxGLDesc.setText((String) hash.get(ACHDDESC));
                                    txtTaxGLDesc.setEnabled(false);
                                   txtTaxGLFocusLost(null);
                               } else if (viewType == NONMAINMINBALCHRG) {
                                   txtNonMainMinBalChrg.setText((String) hash.get(ACHDID));
                                   txtNonMainMinBalChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtNonMainMinBalChrgDesc.setEnabled(false);
                                   txtNonMainMinBalChrgFocusLost(null);
                               } else if (viewType == INOPERATIVE) {
                                   txtInOperative.setText((String) hash.get(ACHDID));
                                   txtInOperativeDesc.setText((String) hash.get(ACHDDESC));
                                   txtInOperativeDesc.setEnabled(false);
                                   txtInOperativeFocusLost(null);
                               } else if (viewType == FOLIOCHRG) {
                                   txtFolioChrg.setText((String) hash.get(ACHDID));
                                   txtFolioChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtFolioChrgDesc.setEnabled(false);
                                   txtFolioChrgFocusLost(null);
                               }else if (viewType == ATMID) {
                                   txtATMGL.setText((String) hash.get(ACHDID));
                                   txtATMGLDisplay.setText((String) hash.get(ACHDDESC));
                                   txtATMGLDisplay.setEnabled(false);
                                   txtATMGLFocusLost(null);
                               }else if (viewType == PREMATURECLOSURECHRG) {
                                   txtPrematureClosureChrg.setText((String) hash.get(ACHDID));
                                   txtPrematureClosureChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtPrematureClosureChrgDesc.setEnabled(false);
                                   txtPrematureClosureChrgFocusLost(null);
                               } else if (viewType == INOPCHRG) { 
                                   txtInOpChrg.setText((String) hash.get(ACHDID));
                                   txtInOpChrgDesc.setText((String) hash.get(ACHDDESC));
                                   txtInOpChrgDesc.setEnabled(false);
                                   txtInOpChrgFocusLost(null);
                               }else if (viewType == DEBIT_WITHDRAWAL_CHRG) { // Added by nithya on 17-03-2016 for 0004021
                                   txtDebitWithdrawalChargeHead.setText((String) hash.get(ACHDID));
                                   txtDebitWithdrawalChargeDesc.setText((String) hash.get(ACHDDESC));
                                   txtDebitWithdrawalChargeDesc.setEnabled(false);
                                   txtDebitWithdrawalChargeHeadFocusLost(null);
                               }
                               
                           }
                           if(viewType == AUTHORIZE){
                                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                            }
                           //__ To Save the data in the Internal Frame...
                            setModified(true);
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
                       
                       private void setFolderButtonEnableDisable(boolean value){
                           btnAcctClosingChrg.setEnabled(value);
                           btnAcctCrInt.setEnabled(value);
                           btnAcctDebitInt.setEnabled(value);
                           btnAcctOpenChrg.setEnabled(value);
                           btnChkBkIssueChrg.setEnabled(value);
                           btnClearingIntAcctHd.setEnabled(value);
                           btnExcessFreeWDChrg.setEnabled(value);
                           btnFolioChrg.setEnabled(value);
                           btnATMGL.setEnabled(value);
                           btnFreeWDChrg.setEnabled(value);
                           btnInOpChrg.setEnabled(value);
                           btnInOperative.setEnabled(value);
                           btnInwardChkRetChrg.setEnabled(value);
                           btnMiscServChrg.setEnabled(value);
                           btnNonMainMinBalChrg.setEnabled(value);
                           btnOutwardChkRetChrg.setEnabled(value);
                           btnPrematureClosureChrg.setEnabled(value);
                           btnStatChrg.setEnabled(value);
                           btnStopPaymentChrg.setEnabled(value);
                           btnTaxGL.setEnabled(value);
                           btnAcctHd.setEnabled(value);
                       }
                       
                       private void setFolderTxtEnableDisable(boolean value){
                           txtAcctClosingChrg.setEnabled(value);
                           txtAcctCrInt.setEnabled(value);
                           txtAcctDebitInt.setEnabled(value);
                           txtAcctOpenChrg.setEnabled(value);
                           txtChkBkIssueChrg.setEnabled(value);
                           txtClearingIntAcctHd.setEnabled(value);
                           txtExcessFreeWDChrg.setEnabled(value);
                           txtFolioChrg.setEnabled(value);
                           txtFreeWDChrg.setEnabled(value);
                           txtInOpChrg.setEnabled(value);
                           txtInOperative.setEnabled(value);
                           txtInwardChkRetChrg.setEnabled(value);
                           txtMiscServChrg.setEnabled(value);
                           txtNonMainMinBalChrg.setEnabled(value);
                           txtOutwardChkRetChrg.setEnabled(value);
                           txtPrematureClosureChrg.setEnabled(value);
                           txtStatChrg.setEnabled(value);
                           txtStopPaymentChrg.setEnabled(value);
                           txtTaxGL.setEnabled(value);
                           txtAcctHd.setEnabled(value);
                           
                           txtAcctClosingChrg.setEditable(value);
                           txtAcctCrInt.setEditable(value);
                           txtAcctDebitInt.setEditable(value);
                           txtAcctOpenChrg.setEditable(value);
                           txtChkBkIssueChrg.setEditable(value);
                           txtClearingIntAcctHd.setEditable(value);
                           txtExcessFreeWDChrg.setEditable(value);
                           txtFolioChrg.setEditable(value);
                           txtFreeWDChrg.setEditable(value);
                           txtInOpChrg.setEditable(value);
                           txtInOperative.setEditable(value);
                           txtInwardChkRetChrg.setEditable(value);
                           txtMiscServChrg.setEditable(value);
                           txtNonMainMinBalChrg.setEditable(value);
                           txtOutwardChkRetChrg.setEditable(value);
                           txtPrematureClosureChrg.setEditable(value);
                           txtStatChrg.setEditable(value);
                           txtStopPaymentChrg.setEditable(value);
                           txtTaxGL.setEditable(value);
                           txtAcctHd.setEditable(value);
                       }
                       
                       private void setEnableDisableAccordingToRdoBtns(){
                           rdoABBAllowed_NoActionPerformed(null);
                           // rdoABBAllowed_YesActionPerformed(null);
                           rdoMobBankClient_NoActionPerformed(null);
                           // rdoMobBankClient_YesActionPerformed(null);
                           rdoIVRSProvided_NoActionPerformed(null);
                           // rdoIVRSProvided_YesActionPerformed(null);
                           rdoDebitCdIssued_NoActionPerformed(null);
                           // rdoDebitCdIssued_YesActionPerformed(null);
                           rdoCreditCdIssued_NoActionPerformed(null);
                           // rdoCreditCdIssued_YesActionPerformed(null);
                           rdoATMIssued_NoActionPerformed(null);
                           // rdoATMIssued_YesActionPerformed(null);
                           rdoLinkFlexiAcct_NoActionPerformed(null);
                           // rdoLinkFlexiAcct_YesActionPerformed(null);
                           rdoFolioChargeAppl_NoActionPerformed(null);
                           // rdoFolioChargeAppl_YesActionPerformed(null);
                           rdoStopPaymentChrg_NoActionPerformed(null);
                           // rdoStopPaymentChrg_YesActionPerformed(null);
                           rdoChkIssuedChrgCh_NoActionPerformed(null);
                           // rdoChkIssuedChrgCh_YesActionPerformed(null);
                           rdoStatCharges_NoActionPerformed(null);
                           // rdoStatCharges_YesActionPerformed(null);
                           rdoNonMainMinBalChrg_NoActionPerformed(null);
                           // rdoNonMainMinBalChrg_YesActionPerformed(null);
                           rdoCreditComp_NoActionPerformed(null);
                           // rdoCreditComp_YesActionPerformed(null);
                           // rdoDebitCompReq_YesActionPerformed(null);
                           rdoDebitCompReq_NoActionPerformed(null);
                           // rdoDebitIntChrg_Yes2ActionPerformed(null);
                           rdoDebitIntChrg_No2ActionPerformed(null);
                           rdoAllowWD_NoActionPerformed(null);
                           // rdoAllowWD_YesActionPerformed(null);
                           // rdoTaxIntApplNRO_YesActionPerformed(null);
                           rdoTaxIntApplNRO_NoActionPerformed(null);
                           // rdoNomineeReq_YesActionPerformed(null);
                           rdoNomineeReq_NoActionPerformed(null);
                       }
                       
/* Auto Generated Method - setFieldNames()
 This method assigns name for all the components.
 Other functions are working based on this name. */
                       private void setFieldNames() {
                           btnAcctClosingChrg.setName("btnAcctClosingChrg");
                           btnAcctCrInt.setName("btnAcctCrInt");
                           btnAcctDebitInt.setName("btnAcctDebitInt");
                           btnAcctHd.setName("btnAcctHd");
                           btnAcctOpenChrg.setName("btnAcctOpenChrg");
                           btnAuthorize.setName("btnAuthorize");
                           btnCancel.setName("btnCancel");
                           btnChkBkIssueChrg.setName("btnChkBkIssueChrg");
                           btnClearingIntAcctHd.setName("btnClearingIntAcctHd");
                           btnClose.setName("btnClose");
                           btnDelete.setName("btnDelete");
                           btnEdit.setName("btnEdit");
                           btnException.setName("btnException");
                           btnExcessFreeWDChrg.setName("btnExcessFreeWDChrg");
                           btnFolioChrg.setName("btnFolioChrg");
                           btnFreeWDChrg.setName("btnFreeWDChrg");
                           btnInOpChrg.setName("btnInOpChrg");
                           btnInOperative.setName("btnInOperative");
                           btnInwardChkRetChrg.setName("btnInwardChkRetChrg");
                           btnMiscServChrg.setName("btnMiscServChrg");
                           btnNew.setName("btnNew");
                           btnNonMainMinBalChrg.setName("btnNonMainMinBalChrg");
                           btnOutwardChkRetChrg.setName("btnOutwardChkRetChrg");
                           btnPrematureClosureChrg.setName("btnPrematureClosureChrg");
                           btnPrint.setName("btnPrint");
                           btnReject.setName("btnReject");
                           btnSave.setName("btnSave");
                           btnStatChrg.setName("btnStatChrg");
                           btnStopPaymentChrg.setName("btnStopPaymentChrg");
                           btnTaxGL.setName("btnTaxGL");
                           cboBehaves.setName("cboBehaves");
                           cboCalcCriteria.setName("cboCalcCriteria");
                           cboCrIntApplFreq.setName("cboCrIntApplFreq");
                           cboCrIntCalcFreq.setName("cboCrIntCalcFreq");
                           cboCreditCompIntCalcFreq.setName("cboCreditCompIntCalcFreq");
                           cboCreditIntRoundOff.setName("cboCreditIntRoundOff");
                           cboCreditProductRoundOff.setName("cboCreditProductRoundOff");
                           cboDebitCompIntCalcFreq.setName("cboDebitCompIntCalcFreq");
                           cboDebitIntApplFreq.setName("cboDebitIntApplFreq");
                           cboDebitIntCalcFreq.setName("cboDebitIntCalcFreq");
                           cboDebitIntRoundOff.setName("cboDebitIntRoundOff");
                           cboDebitProductRoundOff.setName("cboDebitProductRoundOff");
                           cboEndDayProdCalcSBCrInt.setName("cboEndDayProdCalcSBCrInt");
                           cboEndInterCalc.setName("cboEndInterCalc");
                           cboEndProdCalc.setName("cboEndProdCalc");
                           cboFlexiTD.setName("cboFlexiTD");
                           cboFolioChrgApplFreq.setName("cboFolioChrgApplFreq");
                           cboFreeChkLeaveStFrom.setName("cboFreeChkLeaveStFrom");
                           cboFreeChkPd.setName("cboFreeChkPd");
                           cboFreeWDPd.setName("cboFreeWDPd");
                           cboFreeWDStFrom.setName("cboFreeWDStFrom");
                           cboInOpAcChrgPd.setName("cboInOpAcChrgPd");
                           cboIncompFolioROffFreq.setName("cboIncompFolioROffFreq");
                           cboIntCalcEndMon.setName("cboIntCalcEndMon");
                           cboMinBalAmt.setName("cboMinBalAmt");
                           cboMinTreatInOp.setName("cboMinTreatInOp");
                           cboMinTreatNewClosure.setName("cboMinTreatNewClosure");
                           cboMinTreatasDormant.setName("cboMinTreatasDormant");
                           cboMinTreatasNew.setName("cboMinTreatasNew");
                           cboProdCurrency.setName("cboProdCurrency");
                           cboProdFreq.setName("cboProdFreq");
                           cboProdFreqCr.setName("cboProdFreqCr");
                           cboStDayProdCalcSBCrInt.setName("cboStDayProdCalcSBCrInt");
                           cboStMonIntCalc.setName("cboStMonIntCalc");
                           cboStartInterCalc.setName("cboStartInterCalc");
                           cboStartProdCalc.setName("cboStartProdCalc");
                           cboStatChargesChr.setName("cboStatChargesChr");
                           cboStatFreq.setName("cboStatFreq");
                           cboToCollectFolioChrg.setName("cboToCollectFolioChrg");
                           lblABBAllowed.setName("lblABBAllowed");
                           lblATMIssued.setName("lblATMIssued");
                           lblAcClosingChrg.setName("lblAcClosingChrg");
                           lblAcctClosingChrg.setName("lblAcctClosingChrg");
                           lblAcctCrInt.setName("lblAcctCrInt");
                           lblAcctDebitInt.setName("lblAcctDebitInt");
                           lblAcctHd.setName("lblAcctHd");
//                           lblAcctOpenApprReq.setName("lblAcctOpenApprReq");
                           lblAcctOpenCharges.setName("lblAcctOpenCharges");
                           lblAcctOpenChrg.setName("lblAcctOpenChrg");
                           lblAllowWith.setName("lblAllowWith");
                           lblBehaves.setName("lblBehaves");
                           lblCalcCriteria.setName("lblCalcCriteria");
                           lblChequeAllowed.setName("lblChequeAllowed");
                           lblChkBkIssueChrg.setName("lblChkBkIssueChrg");
                           lblChkBkIssueChrgPL.setName("lblChkBkIssueChrgPL");
                           lblChkIssuedChrg.setName("lblChkIssuedChrg");
                           lblChkRetChrOutward.setName("lblChkRetChrOutward");
                           lblChkRetChrgIn.setName("lblChkRetChrgIn");
                           lblChrgMiscServChrg.setName("lblChrgMiscServChrg");
                           lblChrgPreClosure.setName("lblChrgPreClosure");
                           lblClearingIntAcctHd.setName("lblClearingIntAcctHd");
                           lblCollectInt.setName("lblCollectInt");
                           lblCrIntApplFreq.setName("lblCrIntApplFreq");
                           lblCrIntApplicable.setName("lblCrIntApplicable");
                           lblCrIntCalcFreq.setName("lblCrIntCalcFreq");
                           lblCrIntGiven.setName("lblCrIntGiven");
                           lblCrIntUnclear.setName("lblCrIntUnclear");
                           lblCreditCdIssued.setName("lblCreditCdIssued");
                           lblCreditComp.setName("lblCreditComp");
                           lblCreditCompIntCalcFreq.setName("lblCreditCompIntCalcFreq");
                           lblCreditIntRoundOff.setName("lblCreditIntRoundOff");
                           lblCreditProductRoundOff.setName("lblCreditProductRoundOff");
                           lblDebitCardIssued.setName("lblDebitCardIssued");
                           lblDebitCompIntCalcFreq.setName("lblDebitCompIntCalcFreq");
                           lblDebitCompoundReq.setName("lblDebitCompoundReq");
                           lblDebitInt.setName("lblDebitInt");
                           lblDebitIntApplFreq.setName("lblDebitIntApplFreq");
                           lblDebitIntApplicable.setName("lblDebitIntApplicable");
                           lblDebitIntCalcFreq.setName("lblDebitIntCalcFreq");
                           lblDebitIntChrg.setName("lblDebitIntChrg");
                           lblDebitIntRoundOff.setName("lblDebitIntRoundOff");
                           lblDebitProductRoundOff.setName("lblDebitProductRoundOff");
                           lblDesc.setName("lblDesc");
                           lblEndDayProdCalcSBCrInt.setName("lblEndDayProdCalcSBCrInt");
                           lblEndInterCalc.setName("lblEndInterCalc");
                           lblEndProdCalc.setName("lblEndProdCalc");
                           lblExcessFreeWDChrg.setName("lblExcessFreeWDChrg");
                           lblExcessFreeWDChrgPT.setName("lblExcessFreeWDChrgPT");
                           lblFlexiHappen.setName("lblFlexiHappen");
                           lblFlexiTD.setName("lblFlexiTD");
                           lblFolioChargeAppl.setName("lblFolioChargeAppl");
                           lblFolioChrg.setName("lblFolioChrg");
                           lblFolioChrgApplFreq.setName("lblFolioChrgApplFreq");
                           lblATMGL.setName("lblATMGL");
                           lblFolioToChargeOn.setName("lblFolioToChargeOn");
                           lblFreeChkLeavePd.setName("lblFreeChkLeavePd");
                           lblFreeChkLeaveStart.setName("lblFreeChkLeaveStart");
                           lblFreeWDChrg.setName("lblFreeWDChrg");
                           lblFreeWDPd.setName("lblFreeWDPd");
                           lblFreeWDStart.setName("lblFreeWDStart");
                           lblIVRSProvided.setName("lblIVRSProvided");
                           lblInOpAcChrg.setName("lblInOpAcChrg");
                           lblInOpAcChrgPd.setName("lblInOpAcChrgPd");
                           lblInOpChrg.setName("lblInOpChrg");
                           lblInOperative.setName("lblInOperative");
                           lblIncompFolioROffFreq.setName("lblIncompFolioROffFreq");
                           lblIntCalcEndMon.setName("lblIntCalcEndMon");
                           lblIntroReq.setName("lblIntroReq");
                           lblInwardChkRetChrg.setName("lblInwardChkRetChrg");
                           lblIssue.setName("lblIssue");
                           lblLastIntApplDate.setName("lblLastIntApplDate");
                           lblLastIntApplDateCr.setName("lblLastIntApplDateCr");
                           lblLastIntCalcDateCR.setName("lblLastIntCalcDateCR");
                           lblLastIntCalcDateDebit.setName("lblLastIntCalcDateDebit");
                           lblLimit.setName("lblLimit");
                           lblLinkFlexiAcct.setName("lblLinkFlexiAcct");
                           lblMaxAmtWDSlip.setName("lblMaxAmtWDSlip");
                           lblMaxCrIntAmt.setName("lblMaxCrIntAmt");
                           lblMaxCrIntRate.setName("lblMaxCrIntRate");
                           lblMaxDebitIntAmt.setName("lblMaxDebitIntAmt");
                           lblMaxDebitIntRate.setName("lblMaxDebitIntRate");
                           lblMinATMBal.setName("lblMinATMBal");
                           lblMinBal1FlexiDep.setName("lblMinBal1FlexiDep");
                           lblMinBal2FlexiDep.setName("lblMinBal2FlexiDep");
                           lblMinBalABB.setName("lblMinBalABB");
                           lblMinBalAmt.setName("lblMinBalAmt");
                           lblMinBalChqbk.setName("lblMinBalChqbk");
                           lblMinBalCreditCd.setName("lblMinBalCreditCd");
                           lblMinBalDebitCards.setName("lblMinBalDebitCards");
                           lblMinBalIVRS.setName("lblMinBalIVRS");
                           lblMinBalwochk.setName("lblMinBalwochk");
                           lblMinCrIntAmt.setName("lblMinCrIntAmt");
                           lblMinCrIntRate.setName("lblMinCrIntRate");
                           lblMinDebitIntAmt.setName("lblMinDebitIntAmt");
                           lblMinDrIntRate.setName("lblMinDrIntRate");
                           lblMinMobBank.setName("lblMinMobBank");
                           lblMinTreatNewAcctClosure.setName("lblMinTreatNewAcctClosure");
                           lblMinTreatasDormant.setName("lblMinTreatasDormant");
                           lblMinTreatasInOp.setName("lblMinTreatasInOp");
                           lblMinTreatasNew.setName("lblMinTreatasNew");
                           lblMiscServChrg.setName("lblMiscServChrg");
                           lblMobBankClient.setName("lblMobBankClient");
                           lblMsg.setName("lblMsg");
                           lblNROTax.setName("lblNROTax");
                           lblNoEntryPerFolio.setName("lblNoEntryPerFolio");
                           lblNoFreeChkLeaves.setName("lblNoFreeChkLeaves");
                           lblNoFreeWD.setName("lblNoFreeWD");
                           lblNoNominee.setName("lblNoNominee");
                           lblNomineereq.setName("lblNomineereq");
                           lblNonMainMinBalCharges.setName("lblNonMainMinBalCharges");
                           lblNonMainMinBalChrg.setName("lblNonMainMinBalChrg");
                           lblOutwardChkRetChrg.setName("lblOutwardChkRetChrg");
                           lblPenalIntChrgStart.setName("lblPenalIntChrgStart");
                           lblPenalIntDebitBalAcct.setName("lblPenalIntDebitBalAcct");
                           lblPer1.setName("lblPer1");
                           lblPer2.setName("lblPer2");
                           lblPer3.setName("lblPer3");
                           lblPer4.setName("lblPer4");
                           lblPer5.setName("lblPer5");
                           lblPer6.setName("lblPer6");
                           lblPer7.setName("lblPer7");
                           lblPrematureClosureChrg.setName("lblPrematureClosureChrg");
                           lblProdCurrency.setName("lblProdCurrency");
                           lblProdFreqCr.setName("lblProdFreqCr");
                           lblProductFreq.setName("lblProductFreq");
                           lblProductID.setName("lblProductID");
                           lblRateNRO.setName("lblRateNRO");
                           lblRatePerFolio.setName("lblRatePerFolio");
                           lblSpace1.setName("lblSpace1");
                           lblSpace2.setName("lblSpace2");
                           lblSpace3.setName("lblSpace3");
                           lblSpace4.setName("lblSpace4");
                           lblSpace5.setName("lblSpace5");
                           lblStDayProdCalcSBCrInt.setName("lblStDayProdCalcSBCrInt");
                           lblStMonIntCalc.setName("lblStMonIntCalc");
                           lblStaff.setName("lblStaff");
                           lblStartInterCalc.setName("lblStartInterCalc");
                           lblStartProdCalc.setName("lblStartProdCalc");
                           lblStatCharges.setName("lblStatCharges");
                           lblStatChargesChrg.setName("lblStatChargesChrg");
                           lblStatChrg.setName("lblStatChrg");
                           lblStatFreq.setName("lblStatFreq");
                           lblStatus.setName("lblStatus");
                           lblStopPayChrg.setName("lblStopPayChrg");
                           lblStopPaymentCharges.setName("lblStopPaymentCharges");
                           lblStopPaymentChrg.setName("lblStopPaymentChrg");
                           lblTaxGL.setName("lblTaxGL");
                           lblTempOD.setName("lblTempOD");
                           lblToChargeOnApplFreq.setName("lblToChargeOnApplFreq");
                           lblToCollectFolioChrg.setName("lblToCollectFolioChrg");
                           lblFolioChargeLastAppliedDt.setName("lblFolioChargeLastAppliedDt");
                           lblFolioChargeNextAppDt.setName("lblFolioChargeNextAppDt");
                           mbrOperativeAcctProduct.setName("mbrOperativeAcctProduct");
                           panABBAllowed.setName("panABBAllowed");
                           panATMIssued.setName("panATMIssued");
                           panAccount.setName("panAccount");
                           panAcctCol1.setName("panAcctCol1");
                           panAcctCol2.setName("panAcctCol2");
                           panAcctHd.setName("panAcctHd");
                           panAcctHead.setName("panAcctHead");
//                           panAcctOpenApprReq.setName("panAcctOpenApprReq");
                           panAcctSep.setName("panAcctSep");
                           panAllowWDSlip.setName("panAllowWDSlip");
                           panApplDrIntRate.setName("panApplDrIntRate");
                           panApplDrIntRate1.setName("panApplDrIntRate1");
                           panCharges.setName("panCharges");
                           panChargesData.setName("panChargesData");
                           panChkAllowed.setName("panChkAllowed");
                           panChkIssuedChrg.setName("panChkIssuedChrg");
                           panCollectInt.setName("panCollectInt");
                           panCrIntGiven.setName("panCrIntGiven");
                           panCreditCdIssued.setName("panCreditCdIssued");
                           panCreditComp.setName("panCreditComp");
                           panDebitCdIssued.setName("panDebitCdIssued");
                           panDebitCompoundReq.setName("panDebitCompoundReq");
                           panDebitIntChrg.setName("panDebitIntChrg");
                           panDebitIntRate.setName("panDebitIntRate");
                           panDebitProdData.setName("panDebitProdData");
                           panFlexiHappen.setName("panFlexiHappen");
                           panFolioChargeAppl.setName("panFolioChargeAppl");
                           panFolioData.setName("panFolioData");
                           panFolioToChargeOn.setName("panFolioToChargeOn");
                           panFreeChkPd.setName("panFreeChkPd");
                           panFreeWDPd.setName("panFreeWDPd");
                           panIVRSProvided.setName("panIVRSProvided");
                           panIntClearing.setName("panIntClearing");
                           panIntPay.setName("panIntPay");
                           panIntPayProd.setName("panIntPayProd");
                           panIntPayRate.setName("panIntPayRate");
                           panIntRec.setName("panIntRec");
                           panIntUnClearBal.setName("panIntUnClearBal");
                           panIntroReq.setName("panIntroReq");
                           panIssueToken.setName("panIssueToken");
                           panLimitDefAllow.setName("panLimitDefAllow");
                           panLinkFlexiAcct.setName("panLinkFlexiAcct");
                           panMaxDrIntRate.setName("panMaxDrIntRate");
                           panMaxDrIntRate1.setName("panMaxDrIntRate1");
                           panMinBalAmt.setName("panMinBalAmt");
                           panMinCrIntRate.setName("panMinCrIntRate");
                           panMinDrIntRate.setName("panMinDrIntRate");
                           panMinPerd.setName("panMinPerd");
                           panMiscDetails.setName("panMiscDetails");
                           panMobBankClient.setName("panMobBankClient");
                           panNomineeReq.setName("panNomineeReq");
                           panNonMainMinBalCharges.setName("panNonMainMinBalCharges");
                           panOperativeProduct.setName("panOperativeProduct");
                           panPenalIntDebitBalAcct.setName("panPenalIntDebitBalAcct");
                           panReturnChargesData.setName("panReturnChargesData");
                           panSpecialDetails.setName("panSpecialDetails");
                           panSpecialItem.setName("panSpecialItem");
                           panStaffAcct.setName("panStaffAcct");
                           panStatCharges.setName("panStatCharges");
                           panStatChargesChr.setName("panStatChargesChr");
                           panStatus.setName("panStatus");
                           panTaxIntApplNRO.setName("panTaxIntApplNRO");
                           panTempODAllow.setName("panTempODAllow");
                           panToChargeOnApplFreq.setName("panToChargeOnApplFreq");
                           rdoABBAllowed_No.setName("rdoABBAllowed_No");
                           rdoABBAllowed_Yes.setName("rdoABBAllowed_Yes");
                           rdoATMIssued_No.setName("rdoATMIssued_No");
                           rdoATMIssued_Yes.setName("rdoATMIssued_Yes");
//                           rdoAcctOpenAppr_No.setName("rdoAcctOpenAppr_No");
//                           rdoAcctOpenAppr_Yes.setName("rdoAcctOpenAppr_Yes");
                           rdoAllowWD_No.setName("rdoAllowWD_No");
                           rdoAllowWD_Yes.setName("rdoAllowWD_Yes");
                           rdoChkAllowed_No.setName("rdoChkAllowed_No");
                           rdoChkAllowed_Yes.setName("rdoChkAllowed_Yes");
                           rdoChkIssuedChrgCh_No.setName("rdoChkIssuedChrgCh_No");
                           rdoChkIssuedChrgCh_Yes.setName("rdoChkIssuedChrgCh_Yes");
                           rdoCollectInt_No.setName("rdoCollectInt_No");
                           rdoCollectInt_Yes.setName("rdoCollectInt_Yes");
                           rdoCrIntGiven_No.setName("rdoCrIntGiven_No");
                           rdoCrIntGiven_Yes.setName("rdoCrIntGiven_Yes");
                           rdoCreditCdIssued_No.setName("rdoCreditCdIssued_No");
                           rdoCreditCdIssued_Yes.setName("rdoCreditCdIssued_Yes");
                           rdoCreditComp_No.setName("rdoCreditComp_No");
                           rdoCreditComp_Yes.setName("rdoCreditComp_Yes");
                           rdoDebitCdIssued_No.setName("rdoDebitCdIssued_No");
                           rdoDebitCdIssued_Yes.setName("rdoDebitCdIssued_Yes");
                           rdoDebitCompReq_No.setName("rdoDebitCompReq_No");
                           rdoDebitCompReq_Yes.setName("rdoDebitCompReq_Yes");
                           rdoDebitIntChrg_No2.setName("rdoDebitIntChrg_No2");
                           rdoDebitIntChrg_Yes2.setName("rdoDebitIntChrg_Yes2");
                           rdoFlexiHappen_SB.setName("rdoFlexiHappen_SB");
                           rdoFlexiHappen_TD.setName("rdoFlexiHappen_TD");
                           rdoFolioChargeAppl_No.setName("rdoFolioChargeAppl_No");
                           rdoFolioChargeAppl_Yes.setName("rdoFolioChargeAppl_Yes");
                           rdoFolioToChargeOn_Both.setName("rdoFolioToChargeOn_Both");
                           rdoFolioToChargeOn_Credit.setName("rdoFolioToChargeOn_Credit");
                           rdoFolioToChargeOn_Debit.setName("rdoFolioToChargeOn_Debit");
                           rdoIVRSProvided_No.setName("rdoIVRSProvided_No");
                           rdoIVRSProvided_Yes.setName("rdoIVRSProvided_Yes");
                           rdoIntClearing_No.setName("rdoIntClearing_No");
                           rdoIntClearing_Yes.setName("rdoIntClearing_Yes");
                           rdoIntUnClearBal_No.setName("rdoIntUnClearBal_No");
                           rdoIntUnClearBal_Yes.setName("rdoIntUnClearBal_Yes");
                           rdoIntroReq_No.setName("rdoIntroReq_No");
                           rdoIntroReq_Yes.setName("rdoIntroReq_Yes");
                           rdoIssueToken_No.setName("rdoIssueToken_No");
                           rdoIssueToken_Yes.setName("rdoIssueToken_Yes");
                           rdoLimitDefAllow_No.setName("rdoLimitDefAllow_No");
                           rdoLimitDefAllow_Yes.setName("rdoLimitDefAllow_Yes");
                           rdoLinkFlexiAcct_No.setName("rdoLinkFlexiAcct_No");
                           rdoLinkFlexiAcct_Yes.setName("rdoLinkFlexiAcct_Yes");
                           rdoMobBankClient_No.setName("rdoMobBankClient_No");
                           rdoMobBankClient_Yes.setName("rdoMobBankClient_Yes");
                           rdoNomineeReq_No.setName("rdoNomineeReq_No");
                           rdoNomineeReq_Yes.setName("rdoNomineeReq_Yes");
                           rdoNonMainMinBalChrg_No.setName("rdoNonMainMinBalChrg_No");
                           rdoNonMainMinBalChrg_Yes.setName("rdoNonMainMinBalChrg_Yes");
                           rdoStaffAcct_No.setName("rdoStaffAcct_No");
                           rdoStaffAcct_Yes.setName("rdoStaffAcct_Yes");
                           rdoStatCharges_No.setName("rdoStatCharges_No");
                           rdoStatCharges_Yes.setName("rdoStatCharges_Yes");
                           rdoStopPaymentCharges.setName("rdoStopPaymentCharges");
                           rdoStopPaymentChrg_No.setName("rdoStopPaymentChrg_No");
                           rdoStopPaymentChrg_Yes.setName("rdoStopPaymentChrg_Yes");
                           rdoTaxIntApplNRO_No.setName("rdoTaxIntApplNRO_No");
                           rdoTaxIntApplNRO_Yes.setName("rdoTaxIntApplNRO_Yes");
                           rdoTempODAllow_No.setName("rdoTempODAllow_No");
                           rdoTempODAllow_Yes.setName("rdoTempODAllow_Yes");
                           rdoToChargeOnApplFreq_Both.setName("rdoToChargeOnApplFreq_Both");
                           rdoToChargeOnApplFreq_Manual.setName("rdoToChargeOnApplFreq_Manual");
                           rdoToChargeOnApplFreq_System.setName("rdoToChargeOnApplFreq_System");
                           sptAcct.setName("sptAcct");
                           sptCrInt.setName("sptCrInt");
                           sptFolio.setName("sptFolio");
                           sptFolioVertical.setName("sptFolioVertical");
                           sptIntReceivable.setName("sptIntReceivable");
                           tabOperativeAcctProduct.setName("tabOperativeAcctProduct");
                           tdtFreeChkLeaveStFrom.setName("tdtFreeChkLeaveStFrom");
                           tdtFreeWDStFrom.setName("tdtFreeWDStFrom");
                           tdtLastIntApplDate.setName("tdtLastIntApplDate");
                           tdtLastIntApplDateCr.setName("tdtLastIntApplDateCr");
                           tdtLastIntCalcDate.setName("tdtLastIntCalcDate");
                           tdtLastIntCalcDateCR.setName("tdtLastIntCalcDateCR");
                           tdtLastFolioAppliedDate.setName("tdtLastFolioAppliedDate");
                           tdtNextFolioAppliedDt.setName("tdtNextFolioAppliedDt");
                           txtAcClosingChrg.setName("txtAcClosingChrg");
                           txtAcctClosingChrg.setName("txtAcctClosingChrg");
                           txtAcctCrInt.setName("txtAcctCrInt");
                           txtAcctDebitInt.setName("txtAcctDebitInt");
                           txtAcctHd.setName("txtAcctHd");
                           txtAcctOpenCharges.setName("txtAcctOpenCharges");
                           txtAcctOpenChrg.setName("txtAcctOpenChrg");
                           txtApplCrIntRate.setName("txtApplCrIntRate");
                           txtApplDebitIntRate.setName("txtApplDebitIntRate");
                           txtChkBkIssueChrg.setName("txtChkBkIssueChrg");
                           txtChkBkIssueChrgPL.setName("txtChkBkIssueChrgPL");
                           txtChkRetChrOutward.setName("txtChkRetChrOutward");
                           txtChkRetChrgIn.setName("txtChkRetChrgIn");
                           txtChrgMiscServChrg.setName("txtChrgMiscServChrg");
                           txtChrgPreClosure.setName("txtChrgPreClosure");
                           txtClearingIntAcctHd.setName("txtClearingIntAcctHd");
                           txtDesc.setName("txtDesc");
                           txtEndDayProdCalcSBCrInt.setName("txtEndDayProdCalcSBCrInt");
                           txtEndInterCalc.setName("txtEndInterCalc");
                           txtEndProdCalc.setName("txtEndProdCalc");
                           txtExcessFreeWDChrg.setName("txtExcessFreeWDChrg");
                           txtExcessFreeWDChrgPT.setName("txtExcessFreeWDChrgPT");
                           txtFolioChrg.setName("txtFolioChrg");
                           txtFreeChkPD.setName("txtFreeChkPD");
                           txtFreeWDChrg.setName("txtFreeWDChrg");
                           txtFreeWDPd.setName("txtFreeWDPd");
                           txtInOpChrg.setName("txtInOpChrg");
                           txtInOperative.setName("txtInOperative");
                           txtIntCalcEndMon.setName("txtIntCalcEndMon");
                           txtInwardChkRetChrg.setName("txtInwardChkRetChrg");
                           txtMainTreatNewAcctClosure.setName("txtMainTreatNewAcctClosure");
                           txtMaxAmtWDSlip.setName("txtMaxAmtWDSlip");
                           txtMaxCrIntAmt.setName("txtMaxCrIntAmt");
                           txtMaxCrIntRate.setName("txtMaxCrIntRate");
                           txtMaxDebitIntAmt.setName("txtMaxDebitIntAmt");
                           txtMaxDebitIntRate.setName("txtMaxDebitIntRate");
                           txtMinATMBal.setName("txtMinATMBal");
                           txtMinBal1FlexiDep.setName("txtMinBal1FlexiDep");
                           txtMinBal2FlexiDep.setName("txtMinBal2FlexiDep");
                           txtMinBalABB.setName("txtMinBalABB");
                           txtMinBalAmt.setName("txtMinBalAmt");
                           txtMinBalChkbk.setName("txtMinBalChkbk");
                           txtMinBalCreditCd.setName("txtMinBalCreditCd");
                           txtMinBalDebitCards.setName("txtMinBalDebitCards");
                           txtMinBalIVRS.setName("txtMinBalIVRS");
                           txtMinBalwchk.setName("txtMinBalwchk");
                           txtMinCrIntAmt.setName("txtMinCrIntAmt");
                           txtMinCrIntRate.setName("txtMinCrIntRate");
                           txtMinDebitIntAmt.setName("txtMinDebitIntAmt");
                           txtMinDebitIntRate.setName("txtMinDebitIntRate");
                           txtMinMobBank.setName("txtMinMobBank");
                           txtMinTreatasDormant.setName("txtMinTreatasDormant");
                           txtMinTreatasInOp.setName("txtMinTreatasInOp");
                           txtMinTreatasNew.setName("txtMinTreatasNew");
                           txtMiscServChrg.setName("txtMiscServChrg");
                           txtNoEntryPerFolio.setName("txtNoEntryPerFolio");
                           txtNoFreeChkLeaves.setName("txtNoFreeChkLeaves");
                           txtNoFreeWD.setName("txtNoFreeWD");
                           txtNoNominees.setName("txtNoNominees");
                           txtNonMainMinBalChrg.setName("txtNonMainMinBalChrg");
                           txtOutwardChkRetChrg.setName("txtOutwardChkRetChrg");
                           txtPenalIntChrgStart.setName("txtPenalIntChrgStart");
                           txtPenalIntDebitBalAcct.setName("txtPenalIntDebitBalAcct");
                           txtPrematureClosureChrg.setName("txtPrematureClosureChrg");
                           txtProductID.setName("txtProductID");
                           txtRatePerFolio.setName("txtRatePerFolio");
                           txtRateTaxNRO.setName("txtRateTaxNRO");
                           txtStDayProdCalcSBCrInt.setName("txtStDayProdCalcSBCrInt");
                           txtStMonIntCalc.setName("txtStMonIntCalc");
                           txtStartInterCalc.setName("txtStartInterCalc");
                           txtStartProdCalc.setName("txtStartProdCalc");
                           txtStatChargesChr.setName("txtStatChargesChr");
                           txtStatChrg.setName("txtStatChrg");
                           txtStopPayChrg.setName("txtStopPayChrg");
                           txtStopPaymentChrg.setName("txtStopPaymentChrg");
                           txtTaxGL.setName("txtTaxGL");
                           txtlInOpAcChrg.setName("txtlInOpAcChrg");
                           txtNumPatternFollowedPrefix.setName("txtNumPatternFollowedPrefix");
                           txtNumPatternFollowedPrefix.setName("txtNumPatternFollowedSuffix");
                           txtLastAccNum.setName("txtLastAccNum");
                           rdoAcc_Reg.setName("rdoAcc_Reg");
                           rdoAcc_Nro.setName("rdoAcc_Nro");
                           rdoAcc_Nre.setName("rdoAcc_Nre");
                           panTypeAcc.setName("panTypeAcc");
                           lblTypeOfAcc.setName("lblTypeOfAcc");
                           txtMinbalForInt.setName("txtMinbalForInt");
                           lblIMinBalForInts.setName("lblIMinBalForInts");
                           
                       }
                       
                       
                       
/* Auto Generated Method - internationalize()
This method used to assign display texts from
the Resource Bundle File. */
/* Auto Generated Method - internationalize()
This method used to assign display texts from
the Resource Bundle File. */
                       private void internationalize() {
                           lblMinBalAmt.setText(resourceBundle.getString("lblMinBalAmt"));
                           lblDebitIntApplicable.setText(resourceBundle.getString("lblDebitIntApplicable"));
                           rdoTaxIntApplNRO_No.setText(resourceBundle.getString("rdoTaxIntApplNRO_No"));
                           lblMinTreatasInOp.setText(resourceBundle.getString("lblMinTreatasInOp"));
                           lblMaxDebitIntRate.setText(resourceBundle.getString("lblMaxDebitIntRate"));
                           lblStDayProdCalcSBCrInt.setText(resourceBundle.getString("lblStDayProdCalcSBCrInt"));
                           lblCrIntUnclear.setText(resourceBundle.getString("lblCrIntUnclear"));
                           lblMinCrIntAmt.setText(resourceBundle.getString("lblMinCrIntAmt"));
                           lblAllowWith.setText(resourceBundle.getString("lblAllowWith"));
                           rdoTempODAllow_No.setText(resourceBundle.getString("rdoTempODAllow_No"));
                           btnFolioChrg.setText(resourceBundle.getString("btnFolioChrg"));
                           rdoToChargeOnApplFreq_Both.setText(resourceBundle.getString("rdoToChargeOnApplFreq_Both"));
                           lblMinBalIVRS.setText(resourceBundle.getString("lblMinBalIVRS"));
                           lblNonMainMinBalChrg.setText(resourceBundle.getString("lblNonMainMinBalChrg"));
                           lblChkBkIssueChrgPL.setText(resourceBundle.getString("lblChkBkIssueChrgPL"));
                           rdoStatCharges_No.setText(resourceBundle.getString("rdoStatCharges_No"));
                           rdoIVRSProvided_No.setText(resourceBundle.getString("rdoIVRSProvided_No"));
                           rdoTaxIntApplNRO_Yes.setText(resourceBundle.getString("rdoTaxIntApplNRO_Yes"));
                           btnAcctDebitInt.setText(resourceBundle.getString("btnAcctDebitInt"));
                           rdoIssueToken_No.setText(resourceBundle.getString("rdoIssueToken_No"));
                           rdoToChargeOnApplFreq_System.setText(resourceBundle.getString("rdoToChargeOnApplFreq_System"));
                           rdoATMIssued_Yes.setText(resourceBundle.getString("rdoATMIssued_Yes"));
                           lblCreditComp.setText(resourceBundle.getString("lblCreditComp"));
                           btnReject.setText(resourceBundle.getString("btnReject"));
                           lblMinTreatNewAcctClosure.setText(resourceBundle.getString("lblMinTreatNewAcctClosure"));
                           rdoChkAllowed_Yes.setText(resourceBundle.getString("rdoChkAllowed_Yes"));
                           lblNonMainMinBalCharges.setText(resourceBundle.getString("lblNonMainMinBalCharges"));
                           rdoLimitDefAllow_No.setText(resourceBundle.getString("rdoLimitDefAllow_No"));
                           lblRatePerFolio.setText(resourceBundle.getString("lblRatePerFolio"));
                           lblEndProdCalc.setText(resourceBundle.getString("lblEndProdCalc"));
                           lblFreeChkLeavePd.setText(resourceBundle.getString("lblFreeChkLeavePd"));
                           lblFlexiTD.setText(resourceBundle.getString("lblFlexiTD"));
                           rdoNonMainMinBalChrg_Yes.setText(resourceBundle.getString("rdoNonMainMinBalChrg_Yes"));
                           lblRateNRO.setText(resourceBundle.getString("lblRateNRO"));
                           rdoABBAllowed_No.setText(resourceBundle.getString("rdoABBAllowed_No"));
                           ((javax.swing.border.TitledBorder)panSpecialDetails.getBorder()).setTitle(resourceBundle.getString("panSpecialDetails"));
                           lblCreditIntRoundOff.setText(resourceBundle.getString("lblCreditIntRoundOff"));
                           lblEndInterCalc.setText(resourceBundle.getString("lblEndInterCalc"));
                           lblCrIntApplFreq.setText(resourceBundle.getString("lblCrIntApplFreq"));
                           lblDebitIntCalcFreq.setText(resourceBundle.getString("lblDebitIntCalcFreq"));
                           btnException.setText(resourceBundle.getString("btnException"));
                           rdoFlexiHappen_TD.setText(resourceBundle.getString("rdoFlexiHappen_TD"));
                           lblMaxCrIntRate.setText(resourceBundle.getString("lblMaxCrIntRate"));
                           lblAcctClosingChrg.setText(resourceBundle.getString("lblAcctClosingChrg"));
                           lblPenalIntDebitBalAcct.setText(resourceBundle.getString("lblPenalIntDebitBalAcct"));
                           btnAcctHd.setText(resourceBundle.getString("btnAcctHd"));
                           lblStartInterCalc.setText(resourceBundle.getString("lblStartInterCalc"));
                           lblInOperative.setText(resourceBundle.getString("lblInOperative"));
                           rdoFolioChargeAppl_Yes.setText(resourceBundle.getString("rdoFolioChargeAppl_Yes"));
                           btnTaxGL.setText(resourceBundle.getString("btnTaxGL"));
                           btnSave.setText(resourceBundle.getString("btnSave"));
                           btnStopPaymentChrg.setText(resourceBundle.getString("btnStopPaymentChrg"));
                           rdoFolioChargeAppl_No.setText(resourceBundle.getString("rdoFolioChargeAppl_No"));
                           lblFolioToChargeOn.setText(resourceBundle.getString("lblFolioToChargeOn"));
                           lblStatChargesChrg.setText(resourceBundle.getString("lblStatChargesChrg"));
                           rdoTempODAllow_Yes.setText(resourceBundle.getString("rdoTempODAllow_Yes"));
                           btnMiscServChrg.setText(resourceBundle.getString("btnMiscServChrg"));
                           lblStatCharges.setText(resourceBundle.getString("lblStatCharges"));
                           lblCrIntApplicable.setText(resourceBundle.getString("lblCrIntApplicable"));
                           lblAcClosingChrg.setText(resourceBundle.getString("lblAcClosingChrg"));
                           lblExcessFreeWDChrgPT.setText(resourceBundle.getString("lblExcessFreeWDChrgPT"));
                           lblCalcCriteria.setText(resourceBundle.getString("lblCalcCriteria"));
                           lblIncompFolioROffFreq.setText(resourceBundle.getString("lblIncompFolioROffFreq"));
                           lblChkIssuedChrg.setText(resourceBundle.getString("lblChkIssuedChrg"));
                           lblStartProdCalc.setText(resourceBundle.getString("lblStartProdCalc"));
                           rdoNonMainMinBalChrg_No.setText(resourceBundle.getString("rdoNonMainMinBalChrg_No"));
                           lblExcessFreeWDChrg.setText(resourceBundle.getString("lblExcessFreeWDChrg"));
                           lblDebitProductRoundOff.setText(resourceBundle.getString("lblDebitProductRoundOff"));
                           lblStopPaymentChrg.setText(resourceBundle.getString("lblStopPaymentChrg"));
                           lblStatChrg.setText(resourceBundle.getString("lblStatChrg"));
                           lblStopPayChrg.setText(resourceBundle.getString("lblStopPayChrg"));
                           lblPenalIntChrgStart.setText(resourceBundle.getString("lblPenalIntChrgStart"));
                           rdoStaffAcct_No.setText(resourceBundle.getString("rdoStaffAcct_No"));
                           btnFreeWDChrg.setText(resourceBundle.getString("btnFreeWDChrg"));
                           lblPer5.setText(resourceBundle.getString("lblPer5"));
                           rdoCreditComp_No.setText(resourceBundle.getString("rdoCreditComp_No"));
                           lblCrIntGiven.setText(resourceBundle.getString("lblCrIntGiven"));
                           lblLimit.setText(resourceBundle.getString("lblLimit"));
                           lblSpace4.setText(resourceBundle.getString("lblSpace4"));
                           lblDebitCompoundReq.setText(resourceBundle.getString("lblDebitCompoundReq"));
                           lblSpace2.setText(resourceBundle.getString("lblSpace2"));
                           btnAcctCrInt.setText(resourceBundle.getString("btnAcctCrInt"));
                           
                           lblSpace3.setText(resourceBundle.getString("lblSpace3"));
                           lblATMIssued.setText(resourceBundle.getString("lblATMIssued"));
                           rdoDebitIntChrg_No2.setText(resourceBundle.getString("rdoDebitIntChrg_No2"));
                           lblSpace1.setText(resourceBundle.getString("lblSpace1"));
//                           lblAcctOpenApprReq.setText(resourceBundle.getString("lblAcctOpenApprReq"));
                           lblLinkFlexiAcct.setText(resourceBundle.getString("lblLinkFlexiAcct"));
                           lblMinBalChqbk.setText(resourceBundle.getString("lblMinBalChqbk"));
                           lblCreditCdIssued.setText(resourceBundle.getString("lblCreditCdIssued"));
                           rdoLimitDefAllow_Yes.setText(resourceBundle.getString("rdoLimitDefAllow_Yes"));
                           btnAcctClosingChrg.setText(resourceBundle.getString("btnAcctClosingChrg"));
                           rdoChkIssuedChrgCh_Yes.setText(resourceBundle.getString("rdoChkIssuedChrgCh_Yes"));
                           rdoLinkFlexiAcct_No.setText(resourceBundle.getString("rdoLinkFlexiAcct_No"));
                           lblPer4.setText(resourceBundle.getString("lblPer4"));
                           lblProductID.setText(resourceBundle.getString("lblProductID"));
                           lblTaxGL.setText(resourceBundle.getString("lblTaxGL"));
                           rdoABBAllowed_Yes.setText(resourceBundle.getString("rdoABBAllowed_Yes"));
                           lblTempOD.setText(resourceBundle.getString("lblTempOD"));
                           lblChkRetChrOutward.setText(resourceBundle.getString("lblChkRetChrOutward"));
                           rdoStopPaymentChrg_No.setText(resourceBundle.getString("rdoStopPaymentChrg_No"));
                           lblChrgPreClosure.setText(resourceBundle.getString("lblChrgPreClosure"));
                           rdoAllowWD_Yes.setText(resourceBundle.getString("rdoAllowWD_Yes"));
                           lblStopPaymentCharges.setText(resourceBundle.getString("lblStopPaymentCharges"));
                           lblABBAllowed.setText(resourceBundle.getString("lblABBAllowed"));
                           rdoMobBankClient_No.setText(resourceBundle.getString("rdoMobBankClient_No"));
                           rdoToChargeOnApplFreq_Manual.setText(resourceBundle.getString("rdoToChargeOnApplFreq_Manual"));
                           lblFreeWDPd.setText(resourceBundle.getString("lblFreeWDPd"));
//                           rdoAcctOpenAppr_Yes.setText(resourceBundle.getString("rdoAcctOpenAppr_Yes"));
                           lblMinBal1FlexiDep.setText(resourceBundle.getString("lblMinBal1FlexiDep"));
                           lblChrgMiscServChrg.setText(resourceBundle.getString("lblChrgMiscServChrg"));
                           ((javax.swing.border.TitledBorder)panMiscDetails.getBorder()).setTitle(resourceBundle.getString("panMiscDetails"));
                           lblDebitCompIntCalcFreq.setText(resourceBundle.getString("lblDebitCompIntCalcFreq"));
                           lblStatus.setText(resourceBundle.getString("lblStatus"));
                           lblMinCrIntRate.setText(resourceBundle.getString("lblMinCrIntRate"));
                           lblFlexiHappen.setText(resourceBundle.getString("lblFlexiHappen"));
                           ((javax.swing.border.TitledBorder)panMinPerd.getBorder()).setTitle(resourceBundle.getString("panMinPerd"));
                           lblClearingIntAcctHd.setText(resourceBundle.getString("lblClearingIntAcctHd"));
                           rdoFolioToChargeOn_Both.setText(resourceBundle.getString("rdoFolioToChargeOn_Both"));
                           lblSpace5.setText(resourceBundle.getString("lblSpace5"));
                           btnDelete.setText(resourceBundle.getString("btnDelete"));
                           rdoDebitCdIssued_Yes.setText(resourceBundle.getString("rdoDebitCdIssued_Yes"));
                           rdoChkAllowed_No.setText(resourceBundle.getString("rdoChkAllowed_No"));
                           lblPer1.setText(resourceBundle.getString("lblPer1"));
                           lblMinMobBank.setText(resourceBundle.getString("lblMinMobBank"));
                           lblInwardChkRetChrg.setText(resourceBundle.getString("lblInwardChkRetChrg"));
                           rdoATMIssued_No.setText(resourceBundle.getString("rdoATMIssued_No"));
                           lblDebitIntApplFreq.setText(resourceBundle.getString("lblDebitIntApplFreq"));
                           rdoDebitCdIssued_No.setText(resourceBundle.getString("rdoDebitCdIssued_No"));
                           lblLastIntCalcDateCR.setText(resourceBundle.getString("lblLastIntCalcDateCR"));
                           rdoLinkFlexiAcct_Yes.setText(resourceBundle.getString("rdoLinkFlexiAcct_Yes"));
                           lblIntroReq.setText(resourceBundle.getString("lblIntroReq"));
                           rdoIntClearing_Yes.setText(resourceBundle.getString("rdoIntClearing_Yes"));
                           lblOutwardChkRetChrg.setText(resourceBundle.getString("lblOutwardChkRetChrg"));
                           lblStMonIntCalc.setText(resourceBundle.getString("lblStMonIntCalc"));
                           lblAcctHd.setText(resourceBundle.getString("lblAcctHd"));
                           lblFolioChargeAppl.setText(resourceBundle.getString("lblFolioChargeAppl"));
                           btnClose.setText(resourceBundle.getString("btnClose"));
                           lblNoNominee.setText(resourceBundle.getString("lblNoNominee"));
                           lblStatFreq.setText(resourceBundle.getString("lblStatFreq"));
                           btnOutwardChkRetChrg.setText(resourceBundle.getString("btnOutwardChkRetChrg"));
                           lblNoFreeChkLeaves.setText(resourceBundle.getString("lblNoFreeChkLeaves"));
                           lblFreeChkLeaveStart.setText(resourceBundle.getString("lblFreeChkLeaveStart"));
                           lblIntCalcEndMon.setText(resourceBundle.getString("lblIntCalcEndMon"));
                           lblProdCurrency.setText(resourceBundle.getString("lblProdCurrency"));
                           btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
                           lblChequeAllowed.setText(resourceBundle.getString("lblChequeAllowed"));
                           rdoDebitCompReq_Yes.setText(resourceBundle.getString("rdoDebitCompReq_Yes"));
                           lblMsg.setText(resourceBundle.getString("lblMsg"));
                           lblFreeWDChrg.setText(resourceBundle.getString("lblFreeWDChrg"));
                           lblDebitIntRoundOff.setText(resourceBundle.getString("lblDebitIntRoundOff"));
                           lblFolioChrg.setText(resourceBundle.getString("lblFolioChrg"));
                           lblATMGL.setText(resourceBundle.getString("lblATMGL"));
                           lblDebitInt.setText(resourceBundle.getString("lblDebitInt"));
                           rdoNomineeReq_No.setText(resourceBundle.getString("rdoNomineeReq_No"));
                           rdoFolioToChargeOn_Credit.setText(resourceBundle.getString("rdoFolioToChargeOn_Credit"));
                           lblNomineereq.setText(resourceBundle.getString("lblNomineereq"));
                           lblMinBalwochk.setText(resourceBundle.getString("lblMinBalwochk"));
                           rdoCrIntGiven_Yes.setText(resourceBundle.getString("rdoCrIntGiven_Yes"));
                           lblPer2.setText(resourceBundle.getString("lblPer2"));
                           lblAcctOpenCharges.setText(resourceBundle.getString("lblAcctOpenCharges"));
                           rdoStopPaymentChrg_Yes.setText(resourceBundle.getString("rdoStopPaymentChrg_Yes"));
                           lblMinDebitIntAmt.setText(resourceBundle.getString("lblMinDebitIntAmt"));
                           btnInwardChkRetChrg.setText(resourceBundle.getString("btnInwardChkRetChrg"));
                           rdoIntUnClearBal_Yes.setText(resourceBundle.getString("rdoIntUnClearBal_Yes"));
                           lblDebitCardIssued.setText(resourceBundle.getString("lblDebitCardIssued"));
                           lblMinBalDebitCards.setText(resourceBundle.getString("lblMinBalDebitCards"));
                           rdoAllowWD_No.setText(resourceBundle.getString("rdoAllowWD_No"));
                           lblProductFreq.setText(resourceBundle.getString("lblProductFreq"));
                           btnPrint.setText(resourceBundle.getString("btnPrint"));
                           rdoNomineeReq_Yes.setText(resourceBundle.getString("rdoNomineeReq_Yes"));
                           lblPer3.setText(resourceBundle.getString("lblPer3"));
                           lblMinBalABB.setText(resourceBundle.getString("lblMinBalABB"));
                           btnInOpChrg.setText(resourceBundle.getString("btnInOpChrg"));
                           rdoIntroReq_Yes.setText(resourceBundle.getString("rdoIntroReq_Yes"));
                           lblInOpAcChrgPd.setText(resourceBundle.getString("lblInOpAcChrgPd"));
                           lblMaxDebitIntAmt.setText(resourceBundle.getString("lblMaxDebitIntAmt"));
                           rdoStatCharges_Yes.setText(resourceBundle.getString("rdoStatCharges_Yes"));
                           lblAcctCrInt.setText(resourceBundle.getString("lblAcctCrInt"));
                           lblToChargeOnApplFreq.setText(resourceBundle.getString("lblToChargeOnApplFreq"));
                           lblNROTax.setText(resourceBundle.getString("lblNROTax"));
                           lblCollectInt.setText(resourceBundle.getString("lblCollectInt"));
                           rdoCreditCdIssued_Yes.setText(resourceBundle.getString("rdoCreditCdIssued_Yes"));
                           lblInOpAcChrg.setText(resourceBundle.getString("lblInOpAcChrg"));
                           lblToCollectFolioChrg.setText(resourceBundle.getString("lblToCollectFolioChrg"));
                           rdoIntClearing_No.setText(resourceBundle.getString("rdoIntClearing_No"));
                           lblCreditProductRoundOff.setText(resourceBundle.getString("lblCreditProductRoundOff"));
                           lblLastIntCalcDateDebit.setText(resourceBundle.getString("lblLastIntCalcDateDebit"));
                           rdoIVRSProvided_Yes.setText(resourceBundle.getString("rdoIVRSProvided_Yes"));
                           btnNew.setText(resourceBundle.getString("btnNew"));
                           lblMobBankClient.setText(resourceBundle.getString("lblMobBankClient"));
                           rdoIntroReq_No.setText(resourceBundle.getString("rdoIntroReq_No"));
                           rdoIntUnClearBal_No.setText(resourceBundle.getString("rdoIntUnClearBal_No"));
                           rdoDebitCompReq_No.setText(resourceBundle.getString("rdoDebitCompReq_No"));
                           rdoFlexiHappen_SB.setText(resourceBundle.getString("rdoFlexiHappen_SB"));
                           rdoCreditComp_Yes.setText(resourceBundle.getString("rdoCreditComp_Yes"));
                           lblMinBalCreditCd.setText(resourceBundle.getString("lblMinBalCreditCd"));
                           rdoIssueToken_Yes.setText(resourceBundle.getString("rdoIssueToken_Yes"));
//                           rdoAcctOpenAppr_No.setText(resourceBundle.getString("rdoAcctOpenAppr_No"));
                           lblPrematureClosureChrg.setText(resourceBundle.getString("lblPrematureClosureChrg"));
                           lblStaff.setText(resourceBundle.getString("lblStaff"));
                           rdoCreditCdIssued_No.setText(resourceBundle.getString("rdoCreditCdIssued_No"));
                           lblPer7.setText(resourceBundle.getString("lblPer7"));
                           rdoCollectInt_Yes.setText(resourceBundle.getString("rdoCollectInt_Yes"));
                           lblEndDayProdCalcSBCrInt.setText(resourceBundle.getString("lblEndDayProdCalcSBCrInt"));
                           lblFreeWDStart.setText(resourceBundle.getString("lblFreeWDStart"));
                           rdoCrIntGiven_No.setText(resourceBundle.getString("rdoCrIntGiven_No"));
                           lblNoFreeWD.setText(resourceBundle.getString("lblNoFreeWD"));
                           lblPer6.setText(resourceBundle.getString("lblPer6"));
                           lblIssue.setText(resourceBundle.getString("lblIssue"));
                           lblChkRetChrgIn.setText(resourceBundle.getString("lblChkRetChrgIn"));
                           lblCrIntCalcFreq.setText(resourceBundle.getString("lblCrIntCalcFreq"));
                           rdoCollectInt_No.setText(resourceBundle.getString("rdoCollectInt_No"));
                           lblAcctOpenChrg.setText(resourceBundle.getString("lblAcctOpenChrg"));
                           btnClearingIntAcctHd.setText(resourceBundle.getString("btnClearingIntAcctHd"));
                           rdoChkIssuedChrgCh_No.setText(resourceBundle.getString("rdoChkIssuedChrgCh_No"));
                           lblChkBkIssueChrg.setText(resourceBundle.getString("lblChkBkIssueChrg"));
                           btnChkBkIssueChrg.setText(resourceBundle.getString("btnChkBkIssueChrg"));
                           lblNoEntryPerFolio.setText(resourceBundle.getString("lblNoEntryPerFolio"));
                           btnEdit.setText(resourceBundle.getString("btnEdit"));
                           lblFolioChrgApplFreq.setText(resourceBundle.getString("lblFolioChrgApplFreq"));
                           lblDesc.setText(resourceBundle.getString("lblDesc"));
                           rdoMobBankClient_Yes.setText(resourceBundle.getString("rdoMobBankClient_Yes"));
                           lblMaxCrIntAmt.setText(resourceBundle.getString("lblMaxCrIntAmt"));
                           lblMiscServChrg.setText(resourceBundle.getString("lblMiscServChrg"));
                           rdoStaffAcct_Yes.setText(resourceBundle.getString("rdoStaffAcct_Yes"));
                           btnNonMainMinBalChrg.setText(resourceBundle.getString("btnNonMainMinBalChrg"));
                           btnExcessFreeWDChrg.setText(resourceBundle.getString("btnExcessFreeWDChrg"));
                           lblMinATMBal.setText(resourceBundle.getString("lblMinATMBal"));
                           lblAcctDebitInt.setText(resourceBundle.getString("lblAcctDebitInt"));
                           rdoDebitIntChrg_Yes2.setText(resourceBundle.getString("rdoDebitIntChrg_Yes2"));
                           lblMaxAmtWDSlip.setText(resourceBundle.getString("lblMaxAmtWDSlip"));
                           lblLastIntApplDate.setText(resourceBundle.getString("lblLastIntApplDate"));
                           lblMinDrIntRate.setText(resourceBundle.getString("lblMinDrIntRate"));
                           lblInOpChrg.setText(resourceBundle.getString("lblInOpChrg"));
                           lblCreditCompIntCalcFreq.setText(resourceBundle.getString("lblCreditCompIntCalcFreq"));
                           lblMinTreatasNew.setText(resourceBundle.getString("lblMinTreatasNew"));
                           btnAcctOpenChrg.setText(resourceBundle.getString("btnAcctOpenChrg"));
                           rdoFolioToChargeOn_Debit.setText(resourceBundle.getString("rdoFolioToChargeOn_Debit"));
                           lblDebitIntChrg.setText(resourceBundle.getString("lblDebitIntChrg"));
                           lblMinTreatasDormant.setText(resourceBundle.getString("lblMinTreatasDormant"));
                           lblMinBal2FlexiDep.setText(resourceBundle.getString("lblMinBal2FlexiDep"));
                           lblLastIntApplDateCr.setText(resourceBundle.getString("lblLastIntApplDateCr"));
                           lblProdFreqCr.setText(resourceBundle.getString("lblProdFreqCr"));
                           btnCancel.setText(resourceBundle.getString("btnCancel"));
                           btnPrematureClosureChrg.setText(resourceBundle.getString("btnPrematureClosureChrg"));
                           btnStatChrg.setText(resourceBundle.getString("btnStatChrg"));
                           lblFolioChargeLastAppliedDt.setText(resourceBundle.getString("lblFolioChargeLastAppliedDt"));
                           lblFolioChargeNextAppDt.setText(resourceBundle.getString("lblFolioChargeNextAppDt"));
                           lblIVRSProvided.setText(resourceBundle.getString("lblIVRSProvided"));
                           btnInOperative.setText(resourceBundle.getString("btnInOperative"));
                           lblBehaves.setText(resourceBundle.getString("lblBehaves"));
                           rdoAcc_Reg.setText(resourceBundle.getString("rdoAcc_Reg"));
                           rdoAcc_Nro.setText(resourceBundle.getString("rdoAcc_Nro"));
                           rdoAcc_Nre.setText(resourceBundle.getString("rdoAcc_Nre"));
                           lblTypeOfAcc.setText(resourceBundle.getString("lblTypeOfAcc"));
                           lblIMinBalForInts.setText(resourceBundle.getString("lblIMinBalForInts"));
                       }
                       
                       
                       private void removeRadioButtons() {
                           rdoABBAllowed.remove(rdoABBAllowed_No);
                           rdoABBAllowed.remove(rdoABBAllowed_Yes);
                           rdoATMIssued.remove(rdoATMIssued_No);
                           rdoATMIssued.remove(rdoATMIssued_Yes);
//                           rdoAcctOpenAppr.remove(rdoAcctOpenAppr_No);
//                           rdoAcctOpenAppr.remove(rdoAcctOpenAppr_Yes);
                           rdoAllowWD.remove(rdoAllowWD_No);
                           rdoAllowWD.remove(rdoAllowWD_Yes);
                           rdoChkAllowed.remove(rdoChkAllowed_No);
                           rdoChkAllowed.remove(rdoChkAllowed_Yes);
                           rdoChkIssuedChrgCh.remove(rdoChkIssuedChrgCh_No);
                           rdoChkIssuedChrgCh.remove(rdoChkIssuedChrgCh_Yes);
                           rdoCollectInt.remove(rdoCollectInt_No);
                           rdoCollectInt.remove(rdoCollectInt_Yes);
                           rdoCrIntGiven.remove(rdoCrIntGiven_No);
                           rdoCrIntGiven.remove(rdoCrIntGiven_Yes);
                           rdoCreditCdIssued.remove(rdoCreditCdIssued_No);
                           rdoCreditCdIssued.remove(rdoCreditCdIssued_Yes);
                           rdoCreditComp.remove(rdoCreditComp_No);
                           rdoCreditComp.remove(rdoCreditComp_Yes);
                           rdoDebitCdIssued.remove(rdoDebitCdIssued_No);
                           rdoDebitCdIssued.remove(rdoDebitCdIssued_Yes);
                           rdoDebitCompReq.remove(rdoDebitCompReq_No);
                           rdoDebitCompReq.remove(rdoDebitCompReq_Yes);
                           rdoDebitIntChrg.remove(rdoDebitIntChrg_No2);
                           rdoDebitIntChrg.remove(rdoDebitIntChrg_Yes2);
                           // rdoExtraIntAppl.remove(rdoExtraIntAppl_No);
                           // rdoExtraIntAppl.remove(rdoExtraIntAppl_Yes);
                           rdoFlexiHappen.remove(rdoFlexiHappen_SB);
                           rdoFlexiHappen.remove(rdoFlexiHappen_TD);
                           rdoFolioChargeAppl.remove(rdoFolioChargeAppl_No);
                           rdoFolioChargeAppl.remove(rdoFolioChargeAppl_Yes);
                           rdoFolioToChargeOn.remove(rdoFolioToChargeOn_Credit);
                           rdoFolioToChargeOn.remove(rdoFolioToChargeOn_Debit);
                           rdoFolioToChargeOn.remove(rdoFolioToChargeOn_Both);
                           rdoIVRSProvided.remove(rdoIVRSProvided_No);
                           rdoIVRSProvided.remove(rdoIVRSProvided_Yes);
                           rdoIntClearing.remove(rdoIntClearing_No);
                           rdoIntClearing.remove(rdoIntClearing_Yes);
                           rdoIntUnClearBal.remove(rdoIntUnClearBal_No);
                           rdoIntUnClearBal.remove(rdoIntUnClearBal_Yes);
                           rdoIntroReq.remove(rdoIntroReq_No);
                           rdoIntroReq.remove(rdoIntroReq_Yes);
                           rdoIssueToken.remove(rdoIssueToken_No);
                           rdoIssueToken.remove(rdoIssueToken_Yes);
                           rdoLimitDefAllow.remove(rdoLimitDefAllow_No);
                           rdoLimitDefAllow.remove(rdoLimitDefAllow_Yes);
                           rdoLinkFlexiAcct.remove(rdoLinkFlexiAcct_No);
                           rdoLinkFlexiAcct.remove(rdoLinkFlexiAcct_Yes);
                           rdoMobBankClient.remove(rdoMobBankClient_No);
                           rdoMobBankClient.remove(rdoMobBankClient_Yes);
                           rdoNomineeReq.remove(rdoNomineeReq_No);
                           rdoNomineeReq.remove(rdoNomineeReq_Yes);
                           rdoNonMainMinBalChrg.remove(rdoNonMainMinBalChrg_No);
                           rdoNonMainMinBalChrg.remove(rdoNonMainMinBalChrg_Yes);
                           rdoStaffAcct.remove(rdoStaffAcct_No);
                           rdoStaffAcct.remove(rdoStaffAcct_Yes);
                           rdoStatCharges.remove(rdoStatCharges_No);
                           rdoStatCharges.remove(rdoStatCharges_Yes);
                           rdoStopPaymentChrg.remove(rdoStopPaymentChrg_No);
                           rdoStopPaymentChrg.remove(rdoStopPaymentChrg_Yes);
                           rdoTaxIntApplNRO.remove(rdoTaxIntApplNRO_No);
                           rdoTaxIntApplNRO.remove(rdoTaxIntApplNRO_Yes);
                           rdoTempODAllow.remove(rdoTempODAllow_No);
                           rdoTempODAllow.remove(rdoTempODAllow_Yes);
                           rdoToChargeOnApplFreq.remove(rdoToChargeOnApplFreq_Manual);
                           rdoToChargeOnApplFreq.remove(rdoToChargeOnApplFreq_System);
                           rdoToChargeOnApplFreq.remove(rdoToChargeOnApplFreq_Both);
                           rdoTypeAcc.remove(rdoAcc_Reg);
                           rdoTypeAcc.remove(rdoAcc_Nro);
                           rdoTypeAcc.remove(rdoAcc_Nre);
//                          
                       }
                       //CboStMonIntCalc,TxtStMonIntCalc
                        private String getAccHdDesc(String achdid)
                        {
                            HashMap map1= new HashMap();
                            map1.put("ACCHD_ID",achdid);
                            List accdes= ClientUtil.executeQuery("getSelectAcchdDesc", map1);
                            if(!accdes.isEmpty())
                            {
                            HashMap mapd1 = new HashMap();
                            mapd1=(HashMap) accdes.get(0);
                             String acchddes=mapd1.get("AC_HD_DESC").toString();
                             return acchddes;
                            }
                            else
                            {
                                return "";
                            }
                        }
                       public void update(Observable observe, Object arg) {
                           removeRadioButtons();
                           
                           txtProductID.setText(observable.getTxtProductID());
                           txtDesc.setText(observable.getTxtDesc());
                           cboBehaves.setSelectedItem(((ComboBoxModel) cboBehaves.getModel()).getDataForKey(observable.getCboBehaves()));
                           cboProdCurrency.setSelectedItem(((ComboBoxModel) cboProdCurrency.getModel()).getDataForKey(observable.getCboProdCurrency()));
                           txtMinBalChkbk.setText(observable.getTxtMinBalChkbk());
                           txtMinBalwchk.setText(observable.getTxtMinBalwchk());
                           rdoChkAllowed_Yes.setSelected(observable.getRdoChkAllowedYes());
                           rdoChkAllowed_No.setSelected(observable.getRdoChkAllowedNo());
                           rdoChkAllowed_NoActionPerformed(null);
                           txtNoNominees.setText(observable.getTxtNoNominees());
//                           rdoAcctOpenAppr_Yes.setSelected(observable.getRdoAcctOpenApprYes());
//                           rdoAcctOpenAppr_No.setSelected(observable.getRdoAcctOpenApprNo());
                           rdoNomineeReq_Yes.setSelected(observable.getRdoNomineeReqYes());
                           rdoNomineeReq_No.setSelected(observable.getRdoNomineeReqNo());
                           //
                           rdoNomineeReq_NoActionPerformed(null);
                           
                           rdoIntroReq_Yes.setSelected(observable.getRdoIntroReqYes());
                           rdoIntroReq_No.setSelected(observable.getRdoIntroReqNo());
                           txtMainTreatNewAcctClosure.setText(observable.getTxtMainTreatNewAcctClosure());
                           txtMinTreatasDormant.setText(observable.getTxtMinTreatasDormant());
                           txtMinTreatasNew.setText(observable.getTxtMinTreatasNew());
                           txtMinTreatasInOp.setText(observable.getTxtMinTreatasInOp());
                           
                           cboMinTreatasDormant.setSelectedItem(((ComboBoxModel) cboMinTreatasDormant.getModel()).getDataForKey(observable.getCboMinTreatasDormant()));
                           cboMinTreatInOp.setSelectedItem(((ComboBoxModel) cboMinTreatInOp.getModel()).getDataForKey(observable.getCboMinTreatInOp()));
                           cboMinTreatasNew.setSelectedItem(((ComboBoxModel) cboMinTreatasNew.getModel()).getDataForKey(observable.getCboMinTreatasNew()));
                           cboMinTreatNewClosure.setSelectedItem(((ComboBoxModel) cboMinTreatNewClosure.getModel()).getDataForKey(observable.getCboMinTreatNewClosure()));
                           cboStatFreq.setSelectedItem(((ComboBoxModel) cboStatFreq.getModel()).getDataForKey(observable.getCboStatFreq()));
                           txtAcctHd.setText(observable.getTxtAcctHd());
                           if(!txtAcctHd.getText().equals(""))
                           {
                               btnAcctHd.setToolTipText(getAccHdDesc(observable.getTxtAcctHd()));
                           }
                           txtNoFreeWD.setText(observable.getTxtNoFreeWD());
                           txtNoFreeWDFocusLost(null);
                           txtFreeWDPd.setText(observable.getTxtFreeWDPd());
                           cboFreeWDPd.setSelectedItem(((ComboBoxModel) cboFreeWDPd.getModel()).getDataForKey(observable.getCboFreeWDPd()));
                           cboFreeWDStFrom.setSelectedItem(((ComboBoxModel) cboFreeWDStFrom.getModel()).getDataForKey(observable.getCboFreeWDStFrom()));
                           tdtFreeWDStFrom.setDateValue(observable.getTdtFreeWDStFrom());
                           
                           txtNoFreeChkLeaves.setText(observable.getTxtNoFreeChkLeaves());
                           txtNoFreeChkLeavesFocusLost(null);
                           txtFreeChkPD.setText(observable.getTxtFreeChkPD());
                           cboFreeChkPd.setSelectedItem(((ComboBoxModel) cboFreeChkPd.getModel()).getDataForKey(observable.getCboFreeChkPd()));
                           cboFreeChkLeaveStFrom.setSelectedItem(((ComboBoxModel) cboFreeChkLeaveStFrom.getModel()).getDataForKey(observable.getCboFreeChkLeaveStFrom()));
                           tdtFreeChkLeaveStFrom.setDateValue(observable.getTdtFreeChkLeaveStFrom());
                           
                           txtRateTaxNRO.setText(observable.getTxtRateTaxNRO());
                           txtMaxAmtWDSlip.setText(observable.getTxtMaxAmtWDSlip());
                           rdoAllowWD_Yes.setSelected(observable.getRdoAllowWDYes());
                           rdoAllowWD_No.setSelected(observable.getRdoAllowWDNo());
                           //
                           rdoAllowWD_NoActionPerformed(null);
                           
                           rdoIntClearing_Yes.setSelected(observable.getRdoIntClearing_Yes());
                           rdoIntClearing_No.setSelected(observable.getRdoIntClearing_No());
                           rdoIntClearing_NoActionPerformed(null);
                           
                           rdoStaffAcct_Yes.setSelected(observable.getRdoStaffAcctYes());
                           rdoStaffAcct_No.setSelected(observable.getRdoStaffAcctNo());
                           rdoCollectInt_Yes.setSelected(observable.getRdoCollectIntYes());
                           rdoCollectInt_No.setSelected(observable.getRdoCollectIntNo());
                           rdoLimitDefAllow_Yes.setSelected(observable.getRdoLimitDefAllowYes());
                           rdoLimitDefAllow_No.setSelected(observable.getRdoLimitDefAllowNo());
                           rdoLimitDefAllow_NoActionPerformed(null);
                           
                           rdoIntUnClearBal_Yes.setSelected(observable.getRdoIntUnClearBalYes());
                           rdoIntUnClearBal_No.setSelected(observable.getRdoIntUnClearBalNo());
                           rdoTempODAllow_Yes.setSelected(observable.getRdoTempODAllowYes());
                           rdoTempODAllow_No.setSelected(observable.getRdoTempODAllowNo());
                           rdoTempODAllow_NoActionPerformed(null);
                           
                           // rdoExtraIntAppl_Yes.setSelected(observable.getRdoExtraIntApplYes());
                           // rdoExtraIntAppl_No.setSelected(observable.getRdoExtraIntApplNo());
                           rdoIssueToken_Yes.setSelected(observable.getRdoIssueTokenYes());
                           rdoIssueToken_No.setSelected(observable.getRdoIssueTokenNo());
                           rdoTaxIntApplNRO_Yes.setSelected(observable.getRdoTaxIntApplNROYes());
                           rdoTaxIntApplNRO_No.setSelected(observable.getRdoTaxIntApplNRONo());
                           //
                           rdoTaxIntApplNRO_NoActionPerformed(null);
                           
                           rdoDebitCompReq_Yes.setSelected(observable.getRdoDebitCompReqYes());
                           rdoDebitCompReq_No.setSelected(observable.getRdoDebitCompReqNo());
                           rdoDebitCompReq_NoActionPerformed(null);
                           
                           txtMinDebitIntRate.setText(observable.getTxtMinDebitIntRate());
                           txtMaxDebitIntRate.setText(observable.getTxtMaxDebitIntRate());
                           txtApplDebitIntRate.setText(observable.getTxtApplDebitIntRate());
                           txtMinDebitIntAmt.setText(observable.getTxtMinDebitIntAmt());
                           txtMaxDebitIntAmt.setText(observable.getTxtMaxDebitIntAmt());
                           cboDebitIntApplFreq.setSelectedItem(((ComboBoxModel) cboDebitIntApplFreq.getModel()).getDataForKey(observable.getCboDebitIntApplFreq()));
                           cboDebitIntRoundOff.setSelectedItem(((ComboBoxModel) cboDebitIntRoundOff.getModel()).getDataForKey(observable.getCboDebitIntRoundOff()));
                           cboDebitCompIntCalcFreq.setSelectedItem(((ComboBoxModel) cboDebitCompIntCalcFreq.getModel()).getDataForKey(observable.getCboDebitCompIntCalcFreq()));
                           cboFlexiTD.setSelectedItem(((ComboBoxModel) cboFlexiTD.getModel()).getDataForKey(observable.getCboFlexiTD()));
                           cboDebitProductRoundOff.setSelectedItem(((ComboBoxModel) cboDebitProductRoundOff.getModel()).getDataForKey(observable.getCboDebitProductRoundOff()));
                           tdtLastIntCalcDate.setDateValue(observable.getTdtLastIntCalcDate());
                           tdtLastIntApplDate.setDateValue(observable.getTdtLastIntApplDate());
                           rdoDebitIntChrg_Yes2.setSelected(observable.getRdoDebitIntChrgYes2());
                           rdoDebitIntChrg_No2.setSelected(observable.getRdoDebitIntChrgNo2());
                           txtPenalIntDebitBalAcct.setText(observable.getTxtPenalIntDebitBalAcct());
                           txtPenalIntChrgStart.setText(observable.getTxtPenalIntChrgStart());
                           txtMinCrIntRate.setText(observable.getTxtMinCrIntRate());
                           txtMaxCrIntRate.setText(observable.getTxtMaxCrIntRate());
                           txtApplCrIntRate.setText(observable.getTxtApplCrIntRate());
                           txtMinCrIntAmt.setText(observable.getTxtMinCrIntAmt());
                           txtMaxCrIntAmt.setText(observable.getTxtMaxCrIntAmt());
                           txtMinbalForInt.setText(observable.getMinBalForIntCalc());
                           txtStDayProdCalcSBCrInt.setText(observable.getTxtStDayProdCalcSBCrInt());
                           txtEndDayProdCalcSBCrInt.setText(observable.getTxtEndDayProdCalcSBCrInt());
                           cboCrIntApplFreq.setSelectedItem(((ComboBoxModel) cboCrIntApplFreq.getModel()).getDataForKey(observable.getCboCrIntApplFreq()));
                           cboCalcCriteria.setSelectedItem(((ComboBoxModel) cboCalcCriteria.getModel()).getDataForKey(observable.getCboCalcCriteria()));
                           cboStMonIntCalc.setSelectedItem(((ComboBoxModel) cboStMonIntCalc.getModel()).getDataForKey(observable.getCboStMonIntCalc()));
                           cboIntCalcEndMon.setSelectedItem(((ComboBoxModel) cboIntCalcEndMon.getModel()).getDataForKey(observable.getCboIntCalcEndMon()));
                           tdtLastIntCalcDateCR.setDateValue(observable.getTdtLastIntCalcDateCR());
                           tdtLastIntApplDateCr.setDateValue(observable.getTdtLastIntApplDateCr());
                           rdoCreditComp_Yes.setSelected(observable.getRdoCreditCompYes());
                           rdoCreditComp_No.setSelected(observable.getRdoCreditCompNo());
                           rdoCreditComp_NoActionPerformed(null);
                           tdtLastFolioAppliedDate.setDateValue(observable.getTdtLastFolioChargeDt());
                           tdtNextFolioAppliedDt.setDateValue(observable.getTdtNextFolioDueDate());
                           cboCreditCompIntCalcFreq.setSelectedItem(((ComboBoxModel) cboCreditCompIntCalcFreq.getModel()).getDataForKey(observable.getCboCreditCompIntCalcFreq()));
                           cboCreditProductRoundOff.setSelectedItem(((ComboBoxModel) cboCreditProductRoundOff.getModel()).getDataForKey(observable.getCboCreditProductRoundOff()));
                           cboCreditIntRoundOff.setSelectedItem(((ComboBoxModel) cboCreditIntRoundOff.getModel()).getDataForKey(observable.getCboCreditIntRoundOff()));
                           cboProdFreqCr.setSelectedItem(((ComboBoxModel) cboProdFreqCr.getModel()).getDataForKey(observable.getCboProdFreqCr()));
                           // txtAddIntStaff.setText(observable.getTxtAddIntStaff());
                           cboInOpAcChrgPd.setSelectedItem(((ComboBoxModel) cboInOpAcChrgPd.getModel()).getDataForKey(observable.getCboInOpAcChrgPd()));
                           txtlInOpAcChrg.setText(observable.getTxtlInOpAcChrg());
                           txtChrgPreClosure.setText(observable.getTxtChrgPreClosure());
                           txtAcClosingChrg.setText(observable.getTxtAcClosingChrg());
                           txtChrgMiscServChrg.setText(observable.getTxtChrgMiscServChrg());
                           rdoNonMainMinBalChrg_Yes.setSelected(observable.getRdoNonMainMinBalChrg_Yes());
                           rdoNonMainMinBalChrg_No.setSelected(observable.getRdoNonMainMinBalChrg_No());
                           rdoChkIssuedChrgCh_Yes.setSelected(observable.getRdoChkIssuedChrgCh_Yes());
                           rdoChkIssuedChrgCh_No.setSelected(observable.getRdoChkIssuedChrgCh_No());
                           rdoFolioChargeAppl_Yes.setSelected(observable.getRdoFolioChargeAppl_Yes());
                           rdoFolioChargeAppl_No.setSelected(observable.getRdoFolioChargeAppl_No());
                           rdoFolioToChargeOn_Credit.setSelected(observable.getRdoFolioToChargeOn_Credit());
                           rdoFolioToChargeOn_Debit.setSelected(observable.getRdoFolioToChargeOn_Debit());
                           rdoFolioToChargeOn_Both.setSelected(observable.getRdoFolioToChargeOn_Both());
                           rdoStopPaymentChrg_Yes.setSelected(observable.getRdoStopPaymentChrg_Yes());
                           rdoStopPaymentChrg_No.setSelected(observable.getRdoStopPaymentChrg_No());
                           rdoStatCharges_Yes.setSelected(observable.getRdoStatCharges_Yes());
                           rdoStatCharges_No.setSelected(observable.getRdoStatCharges_No());
                           txtChkBkIssueChrgPL.setText(observable.getTxtChkBkIssueChrgPL());
                           txtStopPayChrg.setText(observable.getTxtStopPayChrg());
                           txtChkRetChrOutward.setText(observable.getTxtChkRetChrOutward());
                           txtChkRetChrgIn.setText(observable.getTxtChkRetChrgIn());
                           txtAcctOpenCharges.setText(observable.getTxtAcctOpenCharges());
                           txtNoEntryPerFolio.setText(observable.getTxtNoEntryPerFolio());
                           txtRatePerFolio.setText(observable.getTxtRatePerFolio());
                           txtExcessFreeWDChrgPT.setText(observable.getTxtExcessFreeWDChrgPT());
                           cboFolioChrgApplFreq.setSelectedItem(((ComboBoxModel) cboFolioChrgApplFreq.getModel()).getDataForKey(observable.getCboFolioChrgApplFreq()));
                           cboToCollectFolioChrg.setSelectedItem(((ComboBoxModel) cboToCollectFolioChrg.getModel()).getDataForKey(observable.getCboToCollectFolioChrg()));
                           cboIncompFolioROffFreq.setSelectedItem(((ComboBoxModel) cboIncompFolioROffFreq.getModel()).getDataForKey(observable.getCboIncompFolioROffFreq()));
                           txtMinBalAmt.setText(observable.getTxtMinBalAmt());
                           cboMinBalAmt.setSelectedItem(((ComboBoxModel) cboMinBalAmt.getModel()).getDataForKey(observable.getCboMinBalAmt()));
                           txtStatChargesChr.setText(observable.getTxtStatChargesChr());
                           cboStatChargesChr.setSelectedItem(((ComboBoxModel) cboStatChargesChr.getModel()).getDataForKey(observable.getCboStatChargesChr()));
                           rdoToChargeOnApplFreq_Manual.setSelected(observable.getRdoToChargeOnApplFreq_Manual());
                           rdoToChargeOnApplFreq_System.setSelected(observable.getRdoToChargeOnApplFreq_System());
                           rdoToChargeOnApplFreq_Both.setSelected(observable.getRdoToChargeOnApplFreq_Both());
                           rdoFlexiHappen_SB.setSelected(observable.getRdoFlexiHappen_SB());
                           rdoFlexiHappen_TD.setSelected(observable.getRdoFlexiHappen_TD());
                           rdoLinkFlexiAcct_Yes.setSelected(observable.getRdoLinkFlexiAcct_Yes());
                           rdoLinkFlexiAcct_No.setSelected(observable.getRdoLinkFlexiAcct_No());
                           txtMinBal1FlexiDep.setText(observable.getTxtMinBal1FlexiDep());
                           txtMinBal2FlexiDep.setText(observable.getTxtMinBal2FlexiDep());
                           rdoATMIssued_Yes.setSelected(observable.getRdoATMIssuedYes());
                           rdoATMIssued_No.setSelected(observable.getRdoATMIssuedNo());
                           rdoABBAllowed_Yes.setSelected(observable.getRdoABBAllowed_Yes());
                           rdoABBAllowed_No.setSelected(observable.getRdoABBAllowed_No());
                           //
                           rdoABBAllowed_NoActionPerformed(null);
                           
                           txtMinMobBank.setText(observable.getTxtMinMobBank());
                           rdoMobBankClient_Yes.setSelected(observable.getRdoMobBankClient_Yes());
                           rdoMobBankClient_No.setSelected(observable.getRdoMobBankClient_No());
                           rdoMobBankClient_NoActionPerformed(null);
                           
                           rdoIVRSProvided_Yes.setSelected(observable.getRdoIVRSProvided_Yes());
                           rdoIVRSProvided_No.setSelected(observable.getRdoIVRSProvided_No());
                           //
                           rdoIVRSProvided_NoActionPerformed(null);
                           
                           rdoDebitCdIssued_Yes.setSelected(observable.getRdoDebitCdIssued_Yes());
                           rdoDebitCdIssued_No.setSelected(observable.getRdoDebitCdIssued_No());
                           rdoCreditCdIssued_Yes.setSelected(observable.getRdoCreditCdIssued_Yes());
                           rdoCreditCdIssued_No.setSelected(observable.getRdoCreditCdIssued_No());
                           txtMinATMBal.setText(observable.getTxtMinATMBal());
                           txtMinBalCreditCd.setText(observable.getTxtMinBalCreditCd());
                           txtMinBalDebitCards.setText(observable.getTxtMinBalDebitCards());
                           txtMinBalIVRS.setText(observable.getTxtMinBalIVRS());
                           
                           txtMinBalABB.setText(observable.getTxtMinBalABB());
                           txtIMPSLimit.setText(observable.getTxtIMPSLimit());
                           txtInOpChrg.setText(observable.getTxtInOpChrg());
                           if(!txtInOpChrg.getText().equals(""))
                           {
                           txtInOpChrgDesc.setText(getAccHdDesc(observable.getTxtInOpChrg()));
                           txtInOpChrgDesc.setEnabled(false);
                           }
                           txtPrematureClosureChrg.setText(observable.getTxtPrematureClosureChrg());
                            if(!txtPrematureClosureChrg.getText().equals(""))
                           {
                           txtPrematureClosureChrgDesc.setText(getAccHdDesc(observable.getTxtPrematureClosureChrg()));
                           txtPrematureClosureChrgDesc.setEnabled(false);
                           }
                           txtAcctClosingChrg.setText(observable.getTxtAcctClosingChrg());
                           if(!txtAcctClosingChrg.getText().equals(""))
                           {
                           txtAcctClosingChrgDesc.setText(getAccHdDesc(observable.getTxtAcctClosingChrg()));
                           txtAcctClosingChrgDesc.setEnabled(false);
                           }
                           txtMiscServChrg.setText(observable.getTxtMiscServChrg());
                            if(!txtMiscServChrg.getText().equals(""))
                           {
                           txtMiscServChrgDesc.setText(getAccHdDesc(observable.getTxtMiscServChrg()));
                           txtMiscServChrgDesc.setEnabled(false);
                           }
                           txtStatChrg.setText(observable.getTxtStatChrg());
                            if(!txtStatChrg.getText().equals(""))
                           {
                           txtStatChrgDesc.setText(getAccHdDesc(observable.getTxtStatChrg()));
                           txtStatChrgDesc.setEnabled(false);
                           }
                           txtFreeWDChrg.setText(observable.getTxtFreeWDChrg());
                            if(!txtFreeWDChrg.getText().equals(""))
                           {
                           txtFreeWDChrgDesc.setText(getAccHdDesc(observable.getTxtFreeWDChrg()));
                           txtFreeWDChrgDesc.setEnabled(false);
                           }
                           txtAcctDebitInt.setText(observable.getTxtAcctDebitInt());
                           if(!txtAcctDebitInt.getText().equals(""))
                           {
                           txtAcctDebitIntDesc.setText(getAccHdDesc(observable.getTxtAcctDebitInt()));
                           txtAcctDebitIntDesc.setEnabled(false);
                           }
                           txtAcctCrInt.setText(observable.getTxtAcctCrInt());
                           if(!txtAcctCrInt.getText().equals(""))
                           {
                           txtAcctCrIntDesc.setText(getAccHdDesc(observable.getTxtAcctCrInt()));
                           txtAcctCrIntDesc.setEnabled(false);
                           }
                           txtChkBkIssueChrg.setText(observable.getTxtChkBkIssueChrg());
                           if(!txtChkBkIssueChrg.getText().equals(""))
                           {
                           txtChkBkIssueChrgDesc.setText(getAccHdDesc(observable.getTxtChkBkIssueChrg()));
                           txtChkBkIssueChrgDesc.setEnabled(false);
                           }
                           txtClearingIntAcctHd.setText(observable.getTxtClearingIntAcctHd());
                            if(!txtClearingIntAcctHd.getText().equals(""))
                           {
                           txtClearingIntAcctHdDesc.setText(getAccHdDesc(observable.getTxtClearingIntAcctHd()));
                           txtClearingIntAcctHdDesc.setEnabled(false);
                           }
                           txtStopPaymentChrg.setText(observable.getTxtStopPaymentChrg());
                             if(!txtStopPaymentChrg.getText().equals(""))
                           {
                           txtStopPaymentChrgDesc.setText(getAccHdDesc(observable.getTxtStopPaymentChrg()));
                           txtStopPaymentChrgDesc.setEnabled(false);
                           }
                           txtOutwardChkRetChrg.setText(observable.getTxtOutwardChkRetChrg());
                           if(!txtOutwardChkRetChrg.getText().equals(""))
                           {
                           txtOutwardChkRetChrgDesc.setText(getAccHdDesc(observable.getTxtOutwardChkRetChrg()));
                           txtOutwardChkRetChrgDesc.setEnabled(false);
                           }
                           txtInwardChkRetChrg.setText(observable.getTxtInwardChkRetChrg());
                           if(!txtInwardChkRetChrg.getText().equals(""))
                           {
                           txtInwardChkRetChrgDesc.setText(getAccHdDesc(observable.getTxtInwardChkRetChrg()));
                           txtInwardChkRetChrgDesc.setEnabled(false);
                           }
                           txtAcctOpenChrg.setText(observable.getTxtAcctOpenChrg());
                           if(!txtAcctOpenChrg.getText().equals(""))
                           {
                           txtAcctOpenChrgDesc.setText(getAccHdDesc(observable.getTxtAcctOpenChrg()));
                           txtAcctOpenChrgDesc.setEnabled(false);
                           }
                           txtExcessFreeWDChrg.setText(observable.getTxtExcessFreeWDChrg());
                           if(!txtExcessFreeWDChrg.getText().equals(""))
                           {
                           txtExcessFreeWDChrgDesc.setText(getAccHdDesc(observable.getTxtExcessFreeWDChrg()));
                           txtExcessFreeWDChrgDesc.setEnabled(false);
                           }
                           txtTaxGL.setText(observable.getTxtTaxGL());
                           if(!txtTaxGL.getText().equals(""))
                           {
                           txtTaxGLDesc.setText(getAccHdDesc(observable.getTxtTaxGL()));
                           txtTaxGLDesc.setEnabled(false);
                           }
                           txtNonMainMinBalChrg.setText(observable.getTxtNonMainMinBalChrg());
                           if(!txtNonMainMinBalChrg.getText().equals(""))
                           {
                           txtNonMainMinBalChrgDesc.setText(getAccHdDesc(observable.getTxtNonMainMinBalChrg()));
                           txtNonMainMinBalChrgDesc.setEnabled(false);
                           }
                           txtInOperative.setText(observable.getTxtInOperative());
                            if(!txtInOperative.getText().equals(""))
                           {
                           txtInOperativeDesc.setText(getAccHdDesc(observable.getTxtInOperative()));
                           txtInOperativeDesc.setEnabled(false);
                           }
                           txtFolioChrg.setText(observable.getTxtFolioChrg());
                            if(!txtFolioChrg.getText().equals(""))
                           {
                           txtFolioChrgDesc.setText(getAccHdDesc(observable.getTxtFolioChrg()));
                           txtFolioChrgDesc.setEnabled(false);
                           }
                            
                            txtATMGL.setText(observable.getTxtATMGL());
                            if(!txtATMGL.getText().equals("")) {
                           		txtATMGLDisplay.setText(getAccHdDesc(observable.getTxtATMGL()));
                           		txtATMGLDisplay.setEnabled(false);
                            }
                           
                           txtStMonIntCalc.setText(observable.getTxtStMonIntCalc());
                           txtIntCalcEndMon.setText(observable.getTxtIntCalcEndMon());
                           cboCrIntCalcFreq.setSelectedItem(((ComboBoxModel) cboCrIntCalcFreq.getModel()).getDataForKey(observable.getCboCrIntCalcFreq()));
                           cboStDayProdCalcSBCrInt.setSelectedItem(((ComboBoxModel) cboStDayProdCalcSBCrInt.getModel()).getDataForKey(observable.getCboStDayProdCalcSBCrInt()));
                           cboEndDayProdCalcSBCrInt.setSelectedItem(((ComboBoxModel) cboEndDayProdCalcSBCrInt.getModel()).getDataForKey(observable.getCboEndDayProdCalcSBCrInt()));
                           rdoCrIntGiven_Yes.setSelected(observable.getRdoCrIntGivenYes());
                           rdoCrIntGiven_No.setSelected(observable.getRdoCrIntGivenNo());
                           rdoCrIntGiven_NoActionPerformed(null);
                           
                           
                           txtStartInterCalc.setText(observable.getTxtStartInterCalc());
                           txtEndInterCalc.setText(observable.getTxtEndInterCalc());
                           cboDebitIntCalcFreq.setSelectedItem(((ComboBoxModel) cboDebitIntCalcFreq.getModel()).getDataForKey(observable.getCboDebitIntCalcFreq()));
                           cboStartInterCalc.setSelectedItem(((ComboBoxModel) cboStartInterCalc.getModel()).getDataForKey(observable.getCboStartInterCalc()));
                           cboEndInterCalc.setSelectedItem(((ComboBoxModel) cboEndInterCalc.getModel()).getDataForKey(observable.getCboEndInterCalc()));
                           
                           txtStartProdCalc.setText(observable.getTxtStartProdCalc());
                           txtEndProdCalc.setText(observable.getTxtEndProdCalc());
                           cboProdFreq.setSelectedItem(((ComboBoxModel) cboProdFreq.getModel()).getDataForKey(observable.getCboProdFreq()));
                           cboStartProdCalc.setSelectedItem(((ComboBoxModel) cboStartProdCalc.getModel()).getDataForKey(observable.getCboStartProdCalc()));

                           cboEndProdCalc.setSelectedItem(((ComboBoxModel) cboEndProdCalc.getModel()).getDataForKey(observable.getCboEndProdCalc()));
                           
                           rdoIVRSProvided_NoActionPerformed(null);
                           rdoDebitCdIssued_NoActionPerformed(null);
                           rdoCreditCdIssued_NoActionPerformed(null);
                           rdoATMIssued_NoActionPerformed(null);
                           rdoLinkFlexiAcct_NoActionPerformed(null);
                           rdoFolioChargeAppl_NoActionPerformed(null);
                           rdoStopPaymentChrg_NoActionPerformed(null);
                           rdoChkIssuedChrgCh_NoActionPerformed(null);
                           rdoStatCharges_NoActionPerformed(null);
                           rdoNonMainMinBalChrg_NoActionPerformed(null);
                           rdoCreditComp_NoActionPerformed(null);
                           rdoDebitIntChrg_No2ActionPerformed(null);
                           
                           txtNumPatternFollowedPrefix.setText(observable.getTxtNumPatternFollowedPrefix());
                           txtNumPatternFollowedSuffix.setText(observable.getTxtNumPatternFollowedSuffix());
                           txtLastAccNum.setText(observable.getTxtLastAccNum());
                           
                           rdoAcc_Reg.setSelected(observable.isRdoAcc_Reg());
                           rdoAcc_Nro.setSelected(observable.isRdoAcc_Nro());
                           rdoAcc_Nre.setSelected(observable.isRdoAcc_Nre());
                           
                           // Added by nithya on 17-03-2016 for 0004021
                           
                           if(observable.getChkDebitWithdrawalCharge().equalsIgnoreCase("Y")){
                             chkDebitWithdrawalCharge.setSelected(true);  
                           }else{
                             chkDebitWithdrawalCharge.setSelected(false);  
                           }
                        
                           txtDebiWithdrawalChargePeriod.setText(observable.getTxtDebitWithdrawalChargePeriod());
                           cboDebitChargeType.setSelectedItem(((ComboBoxModel) cboDebitChargeType.getModel()).getDataForKey(observable.getCboDebitChargeType()));
                           txtDebitChargeTypeRate.setText(observable.getTxtDebitChargeTypeRate());
//                         
        txtDebitWithdrawalChargeHead.setText(observable.getTxtDebitWithdrawalChargeAcctHead());
        if (!txtDebitWithdrawalChargeHead.getText().equals("")) {
            txtDebitWithdrawalChargeDesc.setText(getAccHdDesc(observable.getTxtDebitWithdrawalChargeAcctHead()));
            txtDebitWithdrawalChargeDesc.setEnabled(false);
        }
//                          
        // End
        cboFolioChargedBefore.setSelectedItem(((ComboBoxModel) cboFolioChargedBefore.getModel()).getDataForKey(observable.getCboFolioChargeRestritionFreq()));
        txtFolioRestrictionPriod.setText(observable.getTxtFolioRestrictionPriod());
        if (observable.getTxtFolioChargeType()!=null && observable.getTxtFolioChargeType().equalsIgnoreCase("VARIABLE")) {
            rdoFolioChargeVariable.setSelected(true);
        } else {
            rdoFoliochargeFixed.setSelected(true);
            lblRatePerFolio.setText(resourceBundle.getString("lblFixedFolio"));
            lblNoEntryPerFolio.setText(resourceBundle.getString("lblMinimumNoOfEntry"));
        }
             
/*
 * Events for the Focus Lost...
 */
                           textFocusLostEvents();
                           addRadioButtons();
                       }
                       
                       private void textFocusLostEvents(){
                           txtlInOpAcChrgFocusLost(null);
                           txtMinTreatasInOpFocusLost(null);
                           txtMainTreatNewAcctClosureFocusLost(null);
                           txtAcClosingChrgFocusLost(null);
                           txtExcessFreeWDChrgPTFocusLost(null);
                           txtChkRetChrOutwardFocusLost(null);
                           txtChkRetChrgInFocusLost(null);
                           txtAcctOpenChargesFocusLost(null);
                           txtChrgMiscServChrgFocusLost(null);
                       }
                       
                       private void addRadioButtons() {
                           rdoABBAllowed = new CButtonGroup();
                           rdoABBAllowed.add(rdoABBAllowed_No);
                           rdoABBAllowed.add(rdoABBAllowed_Yes);
                           
                           rdoATMIssued = new CButtonGroup();
                           rdoATMIssued.add(rdoATMIssued_No);
                           rdoATMIssued.add(rdoATMIssued_Yes);
                           
                           rdoAcctOpenAppr = new CButtonGroup();
//                           rdoAcctOpenAppr.add(rdoAcctOpenAppr_No);
//                           rdoAcctOpenAppr.add(rdoAcctOpenAppr_Yes);
                           
                           rdoAllowWD = new CButtonGroup();
                           rdoAllowWD.add(rdoAllowWD_No);
                           rdoAllowWD.add(rdoAllowWD_Yes);
                           
                           rdoChkAllowed = new CButtonGroup();
                           rdoChkAllowed.add(rdoChkAllowed_No);
                           rdoChkAllowed.add(rdoChkAllowed_Yes);
                           
                           rdoChkIssuedChrgCh = new CButtonGroup();
                           rdoChkIssuedChrgCh.add(rdoChkIssuedChrgCh_No);
                           rdoChkIssuedChrgCh.add(rdoChkIssuedChrgCh_Yes);
                           
                           rdoCollectInt = new CButtonGroup();
                           rdoCollectInt.add(rdoCollectInt_No);
                           rdoCollectInt.add(rdoCollectInt_Yes);
                           
                           rdoCrIntGiven = new CButtonGroup();
                           rdoCrIntGiven.add(rdoCrIntGiven_No);
                           rdoCrIntGiven.add(rdoCrIntGiven_Yes);
                           
                           rdoCreditCdIssued = new CButtonGroup();
                           rdoCreditCdIssued.add(rdoCreditCdIssued_No);
                           rdoCreditCdIssued.add(rdoCreditCdIssued_Yes);
                           
                           rdoCreditComp = new CButtonGroup();
                           rdoCreditComp.add(rdoCreditComp_No);
                           rdoCreditComp.add(rdoCreditComp_Yes);
                           
                           rdoDebitCdIssued = new CButtonGroup();
                           rdoDebitCdIssued.add(rdoDebitCdIssued_No);
                           rdoDebitCdIssued.add(rdoDebitCdIssued_Yes);
                           
                           rdoDebitCompReq = new CButtonGroup();
                           rdoDebitCompReq.add(rdoDebitCompReq_No);
                           rdoDebitCompReq.add(rdoDebitCompReq_Yes);
                           
                           rdoDebitIntChrg = new CButtonGroup();
                           rdoDebitIntChrg.add(rdoDebitIntChrg_No2);
                           rdoDebitIntChrg.add(rdoDebitIntChrg_Yes2);
                           
                           // rdoExtraIntAppl = new CButtonGroup();
                           // rdoExtraIntAppl.add(rdoExtraIntAppl_No);
                           // rdoExtraIntAppl.add(rdoExtraIntAppl_Yes);
                           
                           rdoFlexiHappen = new CButtonGroup();
                           rdoFlexiHappen.add(rdoFlexiHappen_SB);
                           rdoFlexiHappen.add(rdoFlexiHappen_TD);
                           
                           rdoFolioChargeAppl = new CButtonGroup();
                           rdoFolioChargeAppl.add(rdoFolioChargeAppl_No);
                           rdoFolioChargeAppl.add(rdoFolioChargeAppl_Yes);
                           
                           rdoFolioToChargeOn = new CButtonGroup();
                           rdoFolioToChargeOn.add(rdoFolioToChargeOn_Credit);
                           rdoFolioToChargeOn.add(rdoFolioToChargeOn_Debit);
                           rdoFolioToChargeOn.add(rdoFolioToChargeOn_Both);
                           
                           rdoIVRSProvided = new CButtonGroup();
                           rdoIVRSProvided.add(rdoIVRSProvided_No);
                           rdoIVRSProvided.add(rdoIVRSProvided_Yes);
                           
                           rdoIntClearing = new CButtonGroup();
                           rdoIntClearing.add(rdoIntClearing_No);
                           rdoIntClearing.add(rdoIntClearing_Yes);
                           
                           rdoIntUnClearBal = new CButtonGroup();
                           rdoIntUnClearBal.add(rdoIntUnClearBal_No);
                           rdoIntUnClearBal.add(rdoIntUnClearBal_Yes);
                           
                           rdoIntroReq = new CButtonGroup();
                           rdoIntroReq.add(rdoIntroReq_No);
                           rdoIntroReq.add(rdoIntroReq_Yes);
                           
                           rdoIssueToken = new CButtonGroup();
                           rdoIssueToken.add(rdoIssueToken_No);
                           rdoIssueToken.add(rdoIssueToken_Yes);
                           
                           rdoABBAllowed = new CButtonGroup();
                           rdoLimitDefAllow.add(rdoLimitDefAllow_No);
                           rdoLimitDefAllow.add(rdoLimitDefAllow_Yes);
                           
                           rdoLinkFlexiAcct = new CButtonGroup();
                           rdoLinkFlexiAcct.add(rdoLinkFlexiAcct_No);
                           rdoLinkFlexiAcct.add(rdoLinkFlexiAcct_Yes);
                           
                           rdoMobBankClient = new CButtonGroup();
                           rdoMobBankClient.add(rdoMobBankClient_No);
                           rdoMobBankClient.add(rdoMobBankClient_Yes);
                           
                           rdoNomineeReq = new CButtonGroup();
                           rdoNomineeReq.add(rdoNomineeReq_No);
                           rdoNomineeReq.add(rdoNomineeReq_Yes);
                           
                           rdoNonMainMinBalChrg = new CButtonGroup();
                           rdoNonMainMinBalChrg.add(rdoNonMainMinBalChrg_No);
                           rdoNonMainMinBalChrg.add(rdoNonMainMinBalChrg_Yes);
                           
                           rdoStaffAcct = new CButtonGroup();
                           rdoStaffAcct.add(rdoStaffAcct_No);
                           rdoStaffAcct.add(rdoStaffAcct_Yes);
                           
                           rdoStatCharges = new CButtonGroup();
                           rdoStatCharges.add(rdoStatCharges_No);
                           rdoStatCharges.add(rdoStatCharges_Yes);
                           
                           rdoStopPaymentChrg = new CButtonGroup();
                           rdoStopPaymentChrg.add(rdoStopPaymentChrg_No);
                           rdoStopPaymentChrg.add(rdoStopPaymentChrg_Yes);
                           
                           rdoTaxIntApplNRO = new CButtonGroup();
                           rdoTaxIntApplNRO.add(rdoTaxIntApplNRO_No);
                           rdoTaxIntApplNRO.add(rdoTaxIntApplNRO_Yes);
                           
                           rdoTempODAllow = new CButtonGroup();
                           rdoTempODAllow.add(rdoTempODAllow_No);
                           rdoTempODAllow.add(rdoTempODAllow_Yes);
                           
                           rdoToChargeOnApplFreq = new CButtonGroup();
                           rdoToChargeOnApplFreq.add(rdoToChargeOnApplFreq_Manual);
                           rdoToChargeOnApplFreq.add(rdoToChargeOnApplFreq_System);
                           rdoToChargeOnApplFreq.add(rdoToChargeOnApplFreq_Both);
                           
                           rdoTypeAcc = new CButtonGroup();
                           rdoTypeAcc.add(rdoAcc_Reg);
                           rdoTypeAcc.add(rdoAcc_Nro);
                           rdoTypeAcc.add(rdoAcc_Nre);
                       }
                       
                       public void updateOBFields() {
                           observable.setScreen(getScreen());
                           observable.setModule(getModule());
                           
                           observable.setTxtProductID(txtProductID.getText());
                           observable.setTxtAcctHd(txtAcctHd.getText());
                           observable.setTxtProductID(txtProductID.getText());
                           observable.setTxtDesc(txtDesc.getText());
                           observable.setCboBehaves((String) ((ComboBoxModel) cboBehaves.getModel()).getKeyForSelected());
                           observable.setCboProdCurrency((String) ((ComboBoxModel) cboProdCurrency.getModel()).getKeyForSelected());
                           observable.setTxtMinBalChkbk(txtMinBalChkbk.getText());
                           observable.setTxtMinBalwchk(txtMinBalwchk.getText());
                           observable.setRdoChkAllowedYes(rdoChkAllowed_Yes.isSelected());
                           observable.setRdoChkAllowedNo(rdoChkAllowed_No.isSelected());
                           observable.setTxtNoNominees(txtNoNominees.getText());
//                           observable.setRdoAcctOpenApprYes(rdoAcctOpenAppr_Yes.isSelected());
//                           observable.setRdoAcctOpenApprNo(rdoAcctOpenAppr_No.isSelected());
                           observable.setRdoNomineeReqYes(rdoNomineeReq_Yes.isSelected());
                           observable.setRdoNomineeReqNo(rdoNomineeReq_No.isSelected());
                           observable.setRdoIntroReqYes(rdoIntroReq_Yes.isSelected());
                           observable.setRdoIntroReqNo(rdoIntroReq_No.isSelected());
                           observable.setTxtMainTreatNewAcctClosure(txtMainTreatNewAcctClosure.getText());
                           observable.setTxtMinTreatasDormant(txtMinTreatasDormant.getText());
                           observable.setTxtMinTreatasNew(txtMinTreatasNew.getText());
                           observable.setTxtMinTreatasInOp(txtMinTreatasInOp.getText());
                           observable.setCboMinTreatasDormant((String) ((ComboBoxModel) cboMinTreatasDormant.getModel()).getKeyForSelected());
                           observable.setCboMinTreatInOp((String) ((ComboBoxModel) cboMinTreatInOp.getModel()).getKeyForSelected());
                           observable.setCboMinTreatasNew((String) ((ComboBoxModel) cboMinTreatasNew.getModel()).getKeyForSelected());
                           observable.setCboMinTreatNewClosure((String) ((ComboBoxModel) cboMinTreatNewClosure.getModel()).getKeyForSelected());
                           observable.setCboStatFreq((String) ((ComboBoxModel) cboStatFreq.getModel()).getKeyForSelected());
                           observable.setTxtNoFreeChkLeaves(txtNoFreeChkLeaves.getText());
                           observable.setTxtRateTaxNRO(txtRateTaxNRO.getText());
                           observable.setTxtMaxAmtWDSlip(txtMaxAmtWDSlip.getText());
                           observable.setRdoAllowWDYes(rdoAllowWD_Yes.isSelected());
                           observable.setRdoAllowWDNo(rdoAllowWD_No.isSelected());
                           observable.setRdoIntClearing_Yes(rdoIntClearing_Yes.isSelected());
                           observable.setRdoIntClearing_No(rdoIntClearing_No.isSelected());
                           observable.setRdoStaffAcctYes(rdoStaffAcct_Yes.isSelected());
                           observable.setRdoStaffAcctNo(rdoStaffAcct_No.isSelected());
                           observable.setRdoCollectIntYes(rdoCollectInt_Yes.isSelected());
                           observable.setRdoCollectIntNo(rdoCollectInt_No.isSelected());
                           observable.setRdoLimitDefAllowYes(rdoLimitDefAllow_Yes.isSelected());
                           observable.setRdoLimitDefAllowNo(rdoLimitDefAllow_No.isSelected());
                           observable.setTxtNoFreeWD(txtNoFreeWD.getText());
                           observable.setRdoIntUnClearBalYes(rdoIntUnClearBal_Yes.isSelected());
                           observable.setRdoIntUnClearBalNo(rdoIntUnClearBal_No.isSelected());
                           observable.setRdoTempODAllowYes(rdoTempODAllow_Yes.isSelected());
                           observable.setRdoTempODAllowNo(rdoTempODAllow_No.isSelected());
                           // observable.setRdoExtraIntApplYes(rdoExtraIntAppl_Yes.isSelected());
                           // observable.setRdoExtraIntApplNo(rdoExtraIntAppl_No.isSelected());
                           observable.setRdoIssueTokenYes(rdoIssueToken_Yes.isSelected());
                           observable.setRdoIssueTokenNo(rdoIssueToken_No.isSelected());
                           observable.setRdoTaxIntApplNROYes(rdoTaxIntApplNRO_Yes.isSelected());
                           observable.setRdoTaxIntApplNRONo(rdoTaxIntApplNRO_No.isSelected());
                           observable.setTdtFreeWDStFrom(tdtFreeWDStFrom.getDateValue());
                           observable.setTdtFreeChkLeaveStFrom(tdtFreeChkLeaveStFrom.getDateValue());
                           observable.setTxtFreeWDPd(txtFreeWDPd.getText());
                           observable.setCboFreeWDPd((String) ((ComboBoxModel) cboFreeWDPd.getModel()).getKeyForSelected());
                           observable.setTxtFreeChkPD(txtFreeChkPD.getText());
                           observable.setCboFreeChkPd((String) ((ComboBoxModel) cboFreeChkPd.getModel()).getKeyForSelected());
                           observable.setRdoDebitCompReqYes(rdoDebitCompReq_Yes.isSelected());
                           observable.setRdoDebitCompReqNo(rdoDebitCompReq_No.isSelected());
                           observable.setTxtMinDebitIntRate(txtMinDebitIntRate.getText());
                           observable.setTxtMaxDebitIntRate(txtMaxDebitIntRate.getText());
                           observable.setTxtApplDebitIntRate(txtApplDebitIntRate.getText());
                           observable.setTxtMinDebitIntAmt(txtMinDebitIntAmt.getText());
                           observable.setTxtMaxDebitIntAmt(txtMaxDebitIntAmt.getText());
                           observable.setCboDebitIntCalcFreq((String) ((ComboBoxModel) cboDebitIntCalcFreq.getModel()).getKeyForSelected());
                           observable.setCboDebitIntApplFreq((String) ((ComboBoxModel) cboDebitIntApplFreq.getModel()).getKeyForSelected());
                           observable.setCboDebitIntRoundOff((String) ((ComboBoxModel) cboDebitIntRoundOff.getModel()).getKeyForSelected());
                           observable.setCboDebitCompIntCalcFreq((String) ((ComboBoxModel) cboDebitCompIntCalcFreq.getModel()).getKeyForSelected());
                           observable.setCboFlexiTD((String) ((ComboBoxModel) cboFlexiTD.getModel()).getKeyForSelected());
                           observable.setCboDebitProductRoundOff((String) ((ComboBoxModel) cboDebitProductRoundOff.getModel()).getKeyForSelected());
                           observable.setCboProdFreq((String) ((ComboBoxModel) cboProdFreq.getModel()).getKeyForSelected());
                           observable.setTdtLastIntCalcDate(tdtLastIntCalcDate.getDateValue());
                           observable.setTdtLastIntApplDate(tdtLastIntApplDate.getDateValue());
                           observable.setRdoDebitIntChrgYes2(rdoDebitIntChrg_Yes2.isSelected());
                           observable.setRdoDebitIntChrgNo2(rdoDebitIntChrg_No2.isSelected());
                           observable.setTxtPenalIntDebitBalAcct(txtPenalIntDebitBalAcct.getText());
                           observable.setTxtPenalIntChrgStart(txtPenalIntChrgStart.getText());
                           observable.setTxtMinCrIntRate(txtMinCrIntRate.getText());
                           observable.setTxtMaxCrIntRate(txtMaxCrIntRate.getText());
                           observable.setTxtApplCrIntRate(txtApplCrIntRate.getText());
                           observable.setTxtMinCrIntAmt(txtMinCrIntAmt.getText());
                           observable.setTxtMaxCrIntAmt(txtMaxCrIntAmt.getText());
                           observable.setMinBalForIntCalc(txtMinbalForInt.getText());
                           observable.setTxtStDayProdCalcSBCrInt(txtStDayProdCalcSBCrInt.getText());
                           observable.setTxtEndDayProdCalcSBCrInt(txtEndDayProdCalcSBCrInt.getText());
                           observable.setCboCrIntCalcFreq((String) ((ComboBoxModel) cboCrIntCalcFreq.getModel()).getKeyForSelected());
                           observable.setCboCrIntApplFreq((String) ((ComboBoxModel) cboCrIntApplFreq.getModel()).getKeyForSelected());
                           observable.setCboStMonIntCalc((String) ((ComboBoxModel) cboStMonIntCalc.getModel()).getKeyForSelected());
                           observable.setCboIntCalcEndMon((String) ((ComboBoxModel) cboIntCalcEndMon.getModel()).getKeyForSelected());
                           observable.setTdtLastIntCalcDateCR(tdtLastIntCalcDateCR.getDateValue());
                           observable.setTdtLastIntApplDateCr(tdtLastIntApplDateCr.getDateValue());
                           observable.setRdoCreditCompYes(rdoCreditComp_Yes.isSelected());
                           observable.setRdoCreditCompNo(rdoCreditComp_No.isSelected());
                           observable.setCboCreditCompIntCalcFreq((String) ((ComboBoxModel) cboCreditCompIntCalcFreq.getModel()).getKeyForSelected());
                           observable.setCboCreditProductRoundOff((String) ((ComboBoxModel) cboCreditProductRoundOff.getModel()).getKeyForSelected());
                           observable.setCboCreditIntRoundOff((String) ((ComboBoxModel) cboCreditIntRoundOff.getModel()).getKeyForSelected());
                           observable.setCboCalcCriteria((String) ((ComboBoxModel) cboCalcCriteria.getModel()).getKeyForSelected());
                           observable.setCboProdFreqCr((String) ((ComboBoxModel) cboProdFreqCr.getModel()).getKeyForSelected());
                           observable.setRdoCrIntGivenYes(rdoCrIntGiven_Yes.isSelected());
                           observable.setRdoCrIntGivenNo(rdoCrIntGiven_No.isSelected());
                           // observable.setTxtAddIntStaff(txtAddIntStaff.getText());
                           observable.setCboInOpAcChrgPd((String) ((ComboBoxModel) cboInOpAcChrgPd.getModel()).getKeyForSelected());
                           observable.setTxtlInOpAcChrg(txtlInOpAcChrg.getText());
                           observable.setTxtChrgPreClosure(txtChrgPreClosure.getText());
                           observable.setTxtAcClosingChrg(txtAcClosingChrg.getText());
                           observable.setTxtChrgMiscServChrg(txtChrgMiscServChrg.getText());
                           observable.setRdoNonMainMinBalChrg_Yes(rdoNonMainMinBalChrg_Yes.isSelected());
                           observable.setRdoNonMainMinBalChrg_No(rdoNonMainMinBalChrg_No.isSelected());
                           observable.setRdoChkIssuedChrgCh_Yes(rdoChkIssuedChrgCh_Yes.isSelected());
                           observable.setRdoChkIssuedChrgCh_No(rdoChkIssuedChrgCh_No.isSelected());
                           observable.setRdoFolioChargeAppl_Yes(rdoFolioChargeAppl_Yes.isSelected());
                           observable.setRdoFolioChargeAppl_No(rdoFolioChargeAppl_No.isSelected());
                           observable.setRdoFolioToChargeOn_Credit(rdoFolioToChargeOn_Credit.isSelected());
                           observable.setRdoFolioToChargeOn_Debit(rdoFolioToChargeOn_Debit.isSelected());
                           observable.setRdoFolioToChargeOn_Both(rdoFolioToChargeOn_Both.isSelected());
                           observable.setRdoStopPaymentChrg_Yes(rdoStopPaymentChrg_Yes.isSelected());
                           observable.setRdoStopPaymentChrg_No(rdoStopPaymentChrg_No.isSelected());
                           observable.setRdoStatCharges_Yes(rdoStatCharges_Yes.isSelected());
                           observable.setRdoStatCharges_No(rdoStatCharges_No.isSelected());
                           observable.setTxtChkBkIssueChrgPL(txtChkBkIssueChrgPL.getText());
                           observable.setTxtStopPayChrg(txtStopPayChrg.getText());
                           observable.setTxtChkRetChrOutward(txtChkRetChrOutward.getText());
                           observable.setTxtChkRetChrgIn(txtChkRetChrgIn.getText());
                           observable.setTxtAcctOpenCharges(txtAcctOpenCharges.getText());
                           observable.setTxtNoEntryPerFolio(txtNoEntryPerFolio.getText());
                           observable.setTxtRatePerFolio(txtRatePerFolio.getText());
                           observable.setTxtExcessFreeWDChrgPT(txtExcessFreeWDChrgPT.getText());
                           observable.setCboFolioChrgApplFreq((String) ((ComboBoxModel) cboFolioChrgApplFreq.getModel()).getKeyForSelected());
                           observable.setCboToCollectFolioChrg((String) ((ComboBoxModel) cboToCollectFolioChrg.getModel()).getKeyForSelected());
                           observable.setCboIncompFolioROffFreq((String) ((ComboBoxModel) cboIncompFolioROffFreq.getModel()).getKeyForSelected());
                           observable.setTxtMinBalAmt(txtMinBalAmt.getText());
                           observable.setCboMinBalAmt((String) ((ComboBoxModel) cboMinBalAmt.getModel()).getKeyForSelected());
                           observable.setTxtStatChargesChr(txtStatChargesChr.getText());
                           observable.setCboStatChargesChr((String) ((ComboBoxModel) cboStatChargesChr.getModel()).getKeyForSelected());
                           observable.setRdoToChargeOnApplFreq_Manual(rdoToChargeOnApplFreq_Manual.isSelected());
                           observable.setRdoToChargeOnApplFreq_System(rdoToChargeOnApplFreq_System.isSelected());
                           observable.setRdoToChargeOnApplFreq_Both(rdoToChargeOnApplFreq_Both.isSelected());
                           observable.setRdoFlexiHappen_SB(rdoFlexiHappen_SB.isSelected());
                           observable.setRdoFlexiHappen_TD(rdoFlexiHappen_TD.isSelected());
                           observable.setRdoLinkFlexiAcct_Yes(rdoLinkFlexiAcct_Yes.isSelected());
                           observable.setRdoLinkFlexiAcct_No(rdoLinkFlexiAcct_No.isSelected());
                           observable.setTxtMinBal1FlexiDep(txtMinBal1FlexiDep.getText());
                           observable.setTxtMinBal2FlexiDep(txtMinBal2FlexiDep.getText());
                           observable.setRdoATMIssuedYes(rdoATMIssued_Yes.isSelected());
                           observable.setRdoATMIssuedNo(rdoATMIssued_No.isSelected());
                           observable.setRdoABBAllowed_Yes(rdoABBAllowed_Yes.isSelected());
                           observable.setRdoABBAllowed_No(rdoABBAllowed_No.isSelected());
                           observable.setRdoMobBankClient_Yes(rdoMobBankClient_Yes.isSelected());
                           observable.setRdoMobBankClient_No(rdoMobBankClient_No.isSelected());
                           observable.setRdoIVRSProvided_Yes(rdoIVRSProvided_Yes.isSelected());
                           observable.setRdoIVRSProvided_No(rdoIVRSProvided_No.isSelected());
                           observable.setRdoDebitCdIssued_Yes(rdoDebitCdIssued_Yes.isSelected());
                           observable.setRdoDebitCdIssued_No(rdoDebitCdIssued_No.isSelected());
                           observable.setRdoCreditCdIssued_Yes(rdoCreditCdIssued_Yes.isSelected());
                           observable.setRdoCreditCdIssued_No(rdoCreditCdIssued_No.isSelected());
                           observable.setTxtMinATMBal(txtMinATMBal.getText());
                           observable.setTxtMinBalCreditCd(txtMinBalCreditCd.getText());
                           observable.setTxtMinBalDebitCards(txtMinBalDebitCards.getText());
                           observable.setTxtMinBalIVRS(txtMinBalIVRS.getText());
                           observable.setTxtMinMobBank(txtMinMobBank.getText());
                           observable.setTxtMinBalABB(txtMinBalABB.getText());
                           observable.setTxtIMPSLimit(txtIMPSLimit.getText());
                           observable.setTxtInOpChrg(txtInOpChrg.getText());
                           observable.setTxtPrematureClosureChrg(txtPrematureClosureChrg.getText());
                           observable.setTxtAcctClosingChrg(txtAcctClosingChrg.getText());
                           observable.setTxtMiscServChrg(txtMiscServChrg.getText());
                           observable.setTxtStatChrg(txtStatChrg.getText());
                           observable.setTxtFreeWDChrg(txtFreeWDChrg.getText());
                           observable.setTxtAcctDebitInt(txtAcctDebitInt.getText());
                           observable.setTxtAcctCrInt(txtAcctCrInt.getText());
                           observable.setTxtChkBkIssueChrg(txtChkBkIssueChrg.getText());
                           observable.setTxtClearingIntAcctHd(txtClearingIntAcctHd.getText());
                           observable.setTxtStopPaymentChrg(txtStopPaymentChrg.getText());
                           observable.setTxtOutwardChkRetChrg(txtOutwardChkRetChrg.getText());
                           observable.setTxtInwardChkRetChrg(txtInwardChkRetChrg.getText());
                           observable.setTxtAcctOpenChrg(txtAcctOpenChrg.getText());
                           observable.setTxtExcessFreeWDChrg(txtExcessFreeWDChrg.getText());
                           observable.setTxtTaxGL(txtTaxGL.getText());
                           observable.setTxtNonMainMinBalChrg(txtNonMainMinBalChrg.getText());
                           observable.setTxtInOperative(txtInOperative.getText());
                           observable.setTxtFolioChrg(txtFolioChrg.getText());
                           observable.setTxtATMGL(txtATMGL.getText());
                           observable.setTdtLastFolioChargeDt(tdtLastFolioAppliedDate.getDateValue());
                           observable.setTdtNextFolioDueDate(tdtNextFolioAppliedDt.getDateValue());
                           observable.setCboFreeWDStFrom((String) ((ComboBoxModel) cboFreeWDStFrom.getModel()).getKeyForSelected());
                           observable.setCboFreeChkLeaveStFrom((String) ((ComboBoxModel) cboFreeChkLeaveStFrom.getModel()).getKeyForSelected());
                           
                           observable.setTxtIntCalcEndMon(txtIntCalcEndMon.getText());
                           observable.setTxtStMonIntCalc(txtStMonIntCalc.getText());
                           observable.setCboStDayProdCalcSBCrInt((String) ((ComboBoxModel) cboStDayProdCalcSBCrInt.getModel()).getKeyForSelected());
                           observable.setCboEndDayProdCalcSBCrInt((String) ((ComboBoxModel) cboEndDayProdCalcSBCrInt.getModel()).getKeyForSelected());
                           
                           observable.setTxtStartInterCalc(txtStartInterCalc.getText());
                           observable.setCboStartInterCalc((String) ((ComboBoxModel) cboStartInterCalc.getModel()).getKeyForSelected());
                           observable.setTxtEndInterCalc(txtEndInterCalc.getText());
                           observable.setCboEndInterCalc((String) ((ComboBoxModel) cboEndInterCalc.getModel()).getKeyForSelected());
                           
                           observable.setTxtStartProdCalc(txtStartProdCalc.getText());
                           observable.setCboStartProdCalc((String) ((ComboBoxModel) cboStartProdCalc.getModel()).getKeyForSelected());
                           observable.setTxtEndProdCalc(txtEndProdCalc.getText());
                           observable.setCboEndProdCalc((String) ((ComboBoxModel) cboEndProdCalc.getModel()).getKeyForSelected());
                           
                           observable.setTxtNumPatternFollowedPrefix(txtNumPatternFollowedPrefix.getText());
                           observable.setTxtNumPatternFollowedSuffix(txtNumPatternFollowedSuffix.getText());
                           observable.setTxtLastAccNum(txtLastAccNum.getText());
                           
                           observable.setRdoAcc_Reg(rdoAcc_Reg.isSelected());
                           observable.setRdoAcc_Nro(rdoAcc_Nro.isSelected());
                           observable.setRdoAcc_Nre(rdoAcc_Nre.isSelected());
                           
                           // Added by nithya on 17-03-2016 for 0004021
                           if(chkDebitWithdrawalCharge.isSelected()){
                             observable.setChkDebitWithdrawalCharge("Y");
                           }else{
                             observable.setChkDebitWithdrawalCharge("N");  
                           } 
                           observable.setTxtDebitWithdrawalChargePeriod(txtDebiWithdrawalChargePeriod.getText());
                           observable.setCboDebitChargeType((String) ((ComboBoxModel) cboDebitChargeType.getModel()).getKeyForSelected());
                           observable.setTxtDebitChargeTypeRate(txtDebitChargeTypeRate.getText());
                           observable.setTxtDebitWithdrawalChargeAcctHead(txtDebitWithdrawalChargeHead.getText());
                           observable.setTxtDebitWithdrawalChargeAcctHeadDesc(txtDebitWithdrawalChargeDesc.getText());
                           // End
                       }
                       
/* Auto Generated Method - setHelpMessage()
This method shows tooltip help for all the input fields
available in the UI. It needs the Mandatory Resource Bundle
object. Help display Label name should be lblMsg. */
                       public void setHelpMessage() {
                           OperativeAcctProductnewMRB objMandatoryRB = new OperativeAcctProductnewMRB() ;
                           txtProductID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductID"));
                           txtDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesc"));
                           cboBehaves.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBehaves"));
                           txtMinBalChkbk.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalChkbk"));
                           txtMinBalwchk.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalwchk"));
                           rdoChkAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoChkAllowed_Yes"));
                           txtNoNominees.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoNominees"));
//                           rdoAcctOpenAppr_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAcctOpenAppr_Yes"));
                           rdoNomineeReq_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNomineeReq_Yes"));
                           rdoIntroReq_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIntroReq_Yes"));
                           txtMainTreatNewAcctClosure.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMainTreatNewAcctClosure"));
                           txtMinTreatasDormant.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinTreatasDormant"));
                           txtMinTreatasNew.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinTreatasNew"));
                           txtMinTreatasInOp.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinTreatasInOp"));
                           cboMinTreatasDormant.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinTreatasDormant"));
                           cboMinTreatInOp.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinTreatInOp"));
                           cboMinTreatasNew.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinTreatasNew"));
                           cboMinTreatNewClosure.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinTreatNewClosure"));
                           cboStatFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStatFreq"));
                           txtAcctHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctHd"));
                           cboProdCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdCurrency"));
                           txtNoFreeChkLeaves.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoFreeChkLeaves"));
                           txtRateTaxNRO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateTaxNRO"));
                           txtMaxAmtWDSlip.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxAmtWDSlip"));
                           rdoAllowWD_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAllowWD_Yes"));
                           rdoIntClearing_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIntClearing_Yes"));
                           rdoStaffAcct_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoStaffAcct_Yes"));
                           rdoCollectInt_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCollectInt_Yes"));
                           rdoLimitDefAllow_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLimitDefAllow_Yes"));
                           txtNoFreeWD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoFreeWD"));
                           rdoIntUnClearBal_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIntUnClearBal_Yes"));
                           rdoTempODAllow_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTempODAllow_Yes"));
                           rdoIssueToken_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIssueToken_Yes"));
                           rdoTaxIntApplNRO_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoTaxIntApplNRO_Yes"));
                           tdtFreeWDStFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFreeWDStFrom"));
                           tdtFreeChkLeaveStFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtFreeChkLeaveStFrom"));
                           txtFreeWDPd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFreeWDPd"));
                           cboFreeWDPd.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFreeWDPd"));
                           txtFreeChkPD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFreeChkPD"));
                           cboFreeChkPd.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFreeChkPd"));
                           cboFreeWDStFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFreeWDStFrom"));
                           cboFreeChkLeaveStFrom.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFreeChkLeaveStFrom"));
                           txtMinDebitIntRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDebitIntRate"));
                           txtMaxDebitIntRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDebitIntRate"));
                           txtApplDebitIntRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplDebitIntRate"));
                           txtMinDebitIntAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinDebitIntAmt"));
                           txtMaxDebitIntAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxDebitIntAmt"));
                           cboDebitIntCalcFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitIntCalcFreq"));
                           cboDebitIntApplFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitIntApplFreq"));
                           rdoDebitIntChrg_Yes2.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDebitIntChrg_Yes2"));
                           txtStartInterCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartInterCalc"));
                           cboStartInterCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStartInterCalc"));
                           txtEndInterCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndInterCalc"));
                           cboEndInterCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEndInterCalc"));
                           rdoDebitCompReq_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDebitCompReq_Yes"));
                           cboDebitCompIntCalcFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitCompIntCalcFreq"));
                           cboDebitProductRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitProductRoundOff"));
                           cboDebitIntRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("cboDebitIntRoundOff"));
                           tdtLastIntCalcDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastIntCalcDate"));
                           tdtLastIntApplDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastIntApplDate"));
                           cboProdFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdFreq"));
                           txtPenalIntDebitBalAcct.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalIntDebitBalAcct"));
                           txtPenalIntChrgStart.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalIntChrgStart"));
                           txtStartProdCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStartProdCalc"));
                           cboStartProdCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStartProdCalc"));
                           txtEndProdCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndProdCalc"));
                           cboEndProdCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEndProdCalc"));
                           txtStDayProdCalcSBCrInt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStDayProdCalcSBCrInt"));
                           txtEndDayProdCalcSBCrInt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEndDayProdCalcSBCrInt"));
                           cboCrIntCalcFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCrIntCalcFreq"));
                           cboCrIntApplFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCrIntApplFreq"));
                           cboStMonIntCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStMonIntCalc"));
                           cboIntCalcEndMon.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntCalcEndMon"));
                           tdtLastIntCalcDateCR.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastIntCalcDateCR"));
                           tdtLastIntApplDateCr.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtLastIntApplDateCr"));
                           rdoCreditComp_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCreditComp_Yes"));
                           cboCreditCompIntCalcFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCreditCompIntCalcFreq"));
                           cboCreditProductRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCreditProductRoundOff"));
                           cboCreditIntRoundOff.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCreditIntRoundOff"));
                           cboCalcCriteria.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCalcCriteria"));
                           cboProdFreqCr.setHelpMessage(lblMsg, objMandatoryRB.getString("cboProdFreqCr"));
                           rdoCrIntGiven_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCrIntGiven_Yes"));
                           txtMinCrIntRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinCrIntRate"));
                           txtMaxCrIntRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxCrIntRate"));
                           txtApplCrIntRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtApplCrIntRate"));
                           txtMinCrIntAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinCrIntAmt"));
                           txtMaxCrIntAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMaxCrIntAmt"));
                           cboStDayProdCalcSBCrInt.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStDayProdCalcSBCrInt"));
                           cboEndDayProdCalcSBCrInt.setHelpMessage(lblMsg, objMandatoryRB.getString("cboEndDayProdCalcSBCrInt"));
                           txtStMonIntCalc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStMonIntCalc"));
                           txtIntCalcEndMon.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntCalcEndMon"));
                           txtlInOpAcChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtlInOpAcChrg"));
                           cboInOpAcChrgPd.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInOpAcChrgPd"));
                           txtChrgPreClosure.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChrgPreClosure"));
                           txtAcClosingChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcClosingChrg"));
                           txtChrgMiscServChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChrgMiscServChrg"));
                           rdoNonMainMinBalChrg_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoNonMainMinBalChrg_Yes"));
                           txtMinBalAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalAmt"));
                           cboMinBalAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("cboMinBalAmt"));
                           rdoStatCharges_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoStatCharges_Yes"));
                           txtStatChargesChr.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStatChargesChr"));
                           cboStatChargesChr.setHelpMessage(lblMsg, objMandatoryRB.getString("cboStatChargesChr"));
                           rdoChkIssuedChrgCh_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoChkIssuedChrgCh_Yes"));
                           txtChkBkIssueChrgPL.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChkBkIssueChrgPL"));
                           rdoStopPaymentChrg_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoStopPaymentChrg_Yes"));
                           txtStopPayChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStopPayChrg"));
                           rdoFolioChargeAppl_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoFolioChargeAppl_Yes"));
                           txtRatePerFolio.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRatePerFolio"));
                           txtNoEntryPerFolio.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoEntryPerFolio"));
                           rdoFolioToChargeOn_Credit.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoFolioToChargeOn_Credit"));
                           cboFolioChrgApplFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFolioChrgApplFreq"));
                           cboToCollectFolioChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToCollectFolioChrg"));
                           rdoToChargeOnApplFreq_Manual.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoToChargeOnApplFreq_Manual"));
                           cboIncompFolioROffFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIncompFolioROffFreq"));
                           txtChkRetChrOutward.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChkRetChrOutward"));
                           txtChkRetChrgIn.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChkRetChrgIn"));
                           txtAcctOpenCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctOpenCharges"));
                           txtExcessFreeWDChrgPT.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExcessFreeWDChrgPT"));
                           rdoFlexiHappen_SB.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoFlexiHappen_SB"));
                           rdoLinkFlexiAcct_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoLinkFlexiAcct_Yes"));
                           txtMinBal1FlexiDep.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBal1FlexiDep"));
                           txtMinBal2FlexiDep.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBal2FlexiDep"));
                           cboFlexiTD.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFlexiTD"));
                           rdoATMIssued_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoATMIssued_Yes"));
                           rdoABBAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoABBAllowed_Yes"));
                           rdoMobBankClient_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoMobBankClient_Yes"));
                           rdoIVRSProvided_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoIVRSProvided_Yes"));
                           rdoDebitCdIssued_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoDebitCdIssued_Yes"));
                           rdoCreditCdIssued_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoCreditCdIssued_Yes"));
                           txtMinATMBal.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinATMBal"));
                           txtMinBalCreditCd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalCreditCd"));
                           txtMinBalDebitCards.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalDebitCards"));
                           txtMinBalIVRS.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalIVRS"));
                           txtMinMobBank.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinMobBank"));
                           txtMinBalABB.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinBalABB"));
                           txtInOpChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInOpChrg"));
                           txtPrematureClosureChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrematureClosureChrg"));
                           txtAcctClosingChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctClosingChrg"));
                           txtMiscServChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMiscServChrg"));
                           txtStatChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStatChrg"));
                           txtFreeWDChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFreeWDChrg"));
                           txtAcctDebitInt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctDebitInt"));
                           txtAcctCrInt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctCrInt"));
                           txtChkBkIssueChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChkBkIssueChrg"));
                           txtClearingIntAcctHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingIntAcctHd"));
                           txtStopPaymentChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStopPaymentChrg"));
                           txtOutwardChkRetChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOutwardChkRetChrg"));
                           txtInwardChkRetChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInwardChkRetChrg"));
                           txtAcctOpenChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAcctOpenChrg"));
                           txtExcessFreeWDChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtExcessFreeWDChrg"));
                           txtTaxGL.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTaxGL"));
                           txtNonMainMinBalChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNonMainMinBalChrg"));
                           txtInOperative.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInOperative"));
                           txtFolioChrg.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFolioChrg"));
                           rdoAcc_Reg.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAcc_Reg"));
                           rdoAcc_Nro.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAcc_Nro"));
                           rdoAcc_Nre.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoAcc_Nre"));
                       }
                       
                       void setCommand(int actionType) {
                           observable.setActionType(actionType);
                           //observable.setStatus();
                           lblStatus.setText(observable.getLblStatus());
                       }
                private void setFieldInvisible(){
                    lblProdCurrency.setVisible(true);
                    cboProdCurrency.setVisible(true);
                    
                    lblIntroReq.setVisible(false);
                    panIntroReq.setVisible(false);
                }

                       
 private void rdoFlexiHappen_TDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFlexiHappen_TDActionPerformed
     // Add your handling code here:
 }//GEN-LAST:event_rdoFlexiHappen_TDActionPerformed
  private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
      // Add your handling code here:
      cifClosingAlert();
//      this.dispose();
 }//GEN-LAST:event_btnCloseActionPerformed

    private void txtDebiWithdrawalChargePeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebiWithdrawalChargePeriodFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDebiWithdrawalChargePeriodFocusLost

    
    private void txtDebitChargeTypeRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitChargeTypeRateFocusLost
        // TODO add your handling code here:
    // Added by nithya on 17-03-2016 for 0004021    
 if(!(txtDebitChargeTypeRate.getText().equalsIgnoreCase(""))){
         if(Double.parseDouble(txtDebitChargeTypeRate.getText())==0){
             txtDebitWithdrawalChargeHead.setText("");
             txtDebitWithdrawalChargeHead.setEditable(false);
             txtDebitWithdrawalChargeHead.setEnabled(false);
             txtDebitWithdrawalChargeHead.setEnabled(false);
         }else{
             txtDebitWithdrawalChargeHead.setEditable(true);
             txtDebitWithdrawalChargeHead.setEnabled(true);
             btnDebitWithdrawalCharge.setEnabled(true);
         }
     }else{
         txtDebitWithdrawalChargeHead.setText("");
         txtDebitWithdrawalChargeHead.setEditable(false);
         txtDebitWithdrawalChargeHead.setEnabled(false);
         txtDebitWithdrawalChargeHead.setEnabled(false);
     }
         
         
    }//GEN-LAST:event_txtDebitChargeTypeRateFocusLost

    private void txtDebitChargeTypeRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDebitChargeTypeRateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDebitChargeTypeRateActionPerformed
    
    private void btnATMGLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnATMGLActionPerformed
        // TODO add your handling code here:
        callView(DEBIT_WITHDRAWAL_CHRG); // Added by nithya on 17-03-2016 for 0004021
    }//GEN-LAST:event_btnATMGLActionPerformed
    
    private void txtATMGLFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtATMGLFocusLost
        // TODO add your handling code here:
        // Added by nithya on 17-03-2016 for 0004021
        if(!(txtATMGL.getText().equalsIgnoreCase(""))){
            observable.verifyAcctHead(txtATMGL, "OperativeAcctProduct.getSelectAcctHeadTOList");
            txtATMGLDisplay.setText(getAccHdDesc(txtATMGL.getText()));
            txtATMGLDisplay.setEnabled(false);
        }
    }//GEN-LAST:event_txtATMGLFocusLost
    
    private void btnDebitWithdrawalChargeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitWithdrawalChargeActionPerformed
        // TODO add your handling code here:
        callView(DEBIT_WITHDRAWAL_CHRG); // Added by nithya on 17-03-2016 for 0004021
    }//GEN-LAST:event_btnDebitWithdrawalChargeActionPerformed

    
    private void txtDebitWithdrawalChargeHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitWithdrawalChargeHeadFocusLost
        // TODO add your handling code here:
        // Added by nithya on 17-03-2016 for 0004021
        if(!(txtDebitWithdrawalChargeHead.getText().equalsIgnoreCase(""))){
         observable.verifyAcctHead(txtDebitWithdrawalChargeHead, "OperativeAcctProduct.getSelectAcctHeadTOList");
         txtDebitWithdrawalChargeDesc.setText(getAccHdDesc(txtDebitWithdrawalChargeHead.getText()));
         txtDebitWithdrawalChargeDesc.setEnabled(false);
     }
    }//GEN-LAST:event_txtDebitWithdrawalChargeHeadFocusLost

    private void chkDebitWithdrawalChargeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkDebitWithdrawalChargeItemStateChanged
        // TODO add your handling code here:
        // Added by nithya on 17-03-2016 for 0004021
        if(chkDebitWithdrawalCharge.isSelected()){
            txtDebiWithdrawalChargePeriod.setEnabled(true); 
            txtDebitChargeTypeRate.setEnabled(true);
            cboDebitChargeType.setEnabled(true);
        }else{            
            if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
              txtDebiWithdrawalChargePeriod.setEnabled(true); 
              txtDebitChargeTypeRate.setEnabled(true);
              cboDebitChargeType.setEnabled(true);
            }else{
              txtDebiWithdrawalChargePeriod.setEnabled(false); 
              txtDebitChargeTypeRate.setEnabled(false);
              cboDebitChargeType.setEnabled(false);
            }
        }
    }//GEN-LAST:event_chkDebitWithdrawalChargeItemStateChanged

    private void txtProductIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIDFocusLost
        // TODO add your handling code here:
//        // Added by nithya on 19/05/2016
//        HashMap whereMap = new HashMap();
//        whereMap.put("PROD_ID", txtProductID.getText());
//        List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
//        System.out.println("getSBODBorrowerEligAmt : " + lst);
//        if (lst != null && lst.size() > 0) {
//            HashMap existingProdIdMap = (HashMap) lst.get(0);
//            if (existingProdIdMap.containsKey("PROD_ID")) {
//                ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change");
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

    private void rdoFoliochargeFixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFoliochargeFixedActionPerformed
        // TODO add your handling code here:
        if (rdoFoliochargeFixed.isSelected()) {
            lblRatePerFolio.setText(resourceBundle.getString("lblFixedFolio"));
            lblNoEntryPerFolio.setText(resourceBundle.getString("lblMinimumNoOfEntry"));
        } else {
            lblRatePerFolio.setText(resourceBundle.getString("lblRatePerFolio"));
            lblNoEntryPerFolio.setText(resourceBundle.getString("lblNoEntryPerFolio"));
        }
    }//GEN-LAST:event_rdoFoliochargeFixedActionPerformed

    private void rdoFolioChargeVariableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoFolioChargeVariableActionPerformed
        if (rdoFolioChargeVariable.isSelected()) {
            lblRatePerFolio.setText(resourceBundle.getString("lblRatePerFolio"));
            lblNoEntryPerFolio.setText(resourceBundle.getString("lblNoEntryPerFolio"));
        } else {
            lblRatePerFolio.setText(resourceBundle.getString("lblFixedFolio"));
            lblNoEntryPerFolio.setText(resourceBundle.getString("lblMinimumNoOfEntry"));
        }// TODO add your handling code here:
    }//GEN-LAST:event_rdoFolioChargeVariableActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnATMGL;
    private com.see.truetransact.uicomponent.CButton btnAcctClosingChrg;
    private com.see.truetransact.uicomponent.CButton btnAcctCrInt;
    private com.see.truetransact.uicomponent.CButton btnAcctDebitInt;
    private com.see.truetransact.uicomponent.CButton btnAcctHd;
    private com.see.truetransact.uicomponent.CButton btnAcctOpenChrg;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChkBkIssueChrg;
    private com.see.truetransact.uicomponent.CButton btnClearingIntAcctHd;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnDebitWithdrawalCharge;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExcessFreeWDChrg;
    private com.see.truetransact.uicomponent.CButton btnFolioChrg;
    private com.see.truetransact.uicomponent.CButton btnFreeWDChrg;
    private com.see.truetransact.uicomponent.CButton btnInOpChrg;
    private com.see.truetransact.uicomponent.CButton btnInOperative;
    private com.see.truetransact.uicomponent.CButton btnInwardChkRetChrg;
    private com.see.truetransact.uicomponent.CButton btnMiscServChrg;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNonMainMinBalChrg;
    private com.see.truetransact.uicomponent.CButton btnOutwardChkRetChrg;
    private com.see.truetransact.uicomponent.CButton btnPrematureClosureChrg;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnStatChrg;
    private com.see.truetransact.uicomponent.CButton btnStopPaymentChrg;
    private com.see.truetransact.uicomponent.CButton btnTaxGL;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CPanel cPanel1;
    private com.see.truetransact.uicomponent.CComboBox cboBehaves;
    private com.see.truetransact.uicomponent.CComboBox cboCalcCriteria;
    private com.see.truetransact.uicomponent.CComboBox cboCrIntApplFreq;
    private com.see.truetransact.uicomponent.CComboBox cboCrIntCalcFreq;
    private com.see.truetransact.uicomponent.CComboBox cboCreditCompIntCalcFreq;
    private com.see.truetransact.uicomponent.CComboBox cboCreditIntRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboCreditProductRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboDebitChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboDebitCompIntCalcFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitIntApplFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitIntCalcFreq;
    private com.see.truetransact.uicomponent.CComboBox cboDebitIntRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboDebitProductRoundOff;
    private com.see.truetransact.uicomponent.CComboBox cboEndDayProdCalcSBCrInt;
    private com.see.truetransact.uicomponent.CComboBox cboEndInterCalc;
    private com.see.truetransact.uicomponent.CComboBox cboEndProdCalc;
    private com.see.truetransact.uicomponent.CComboBox cboFlexiTD;
    private com.see.truetransact.uicomponent.CComboBox cboFolioChargedBefore;
    private com.see.truetransact.uicomponent.CComboBox cboFolioChrgApplFreq;
    private com.see.truetransact.uicomponent.CComboBox cboFreeChkLeaveStFrom;
    private com.see.truetransact.uicomponent.CComboBox cboFreeChkPd;
    private com.see.truetransact.uicomponent.CComboBox cboFreeWDPd;
    private com.see.truetransact.uicomponent.CComboBox cboFreeWDStFrom;
    private com.see.truetransact.uicomponent.CComboBox cboInOpAcChrgPd;
    private com.see.truetransact.uicomponent.CComboBox cboIncompFolioROffFreq;
    private com.see.truetransact.uicomponent.CComboBox cboIntCalcEndMon;
    private com.see.truetransact.uicomponent.CComboBox cboMinBalAmt;
    private com.see.truetransact.uicomponent.CComboBox cboMinTreatInOp;
    private com.see.truetransact.uicomponent.CComboBox cboMinTreatNewClosure;
    private com.see.truetransact.uicomponent.CComboBox cboMinTreatasDormant;
    private com.see.truetransact.uicomponent.CComboBox cboMinTreatasNew;
    private com.see.truetransact.uicomponent.CComboBox cboProdCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboProdFreq;
    private com.see.truetransact.uicomponent.CComboBox cboProdFreqCr;
    private com.see.truetransact.uicomponent.CComboBox cboStDayProdCalcSBCrInt;
    private com.see.truetransact.uicomponent.CComboBox cboStMonIntCalc;
    private com.see.truetransact.uicomponent.CComboBox cboStartInterCalc;
    private com.see.truetransact.uicomponent.CComboBox cboStartProdCalc;
    private com.see.truetransact.uicomponent.CComboBox cboStatChargesChr;
    private com.see.truetransact.uicomponent.CComboBox cboStatFreq;
    private com.see.truetransact.uicomponent.CComboBox cboToCollectFolioChrg;
    private com.see.truetransact.uicomponent.CCheckBox chkDebitWithdrawalCharge;
    private com.see.truetransact.uicomponent.CLabel lblABBAllowed;
    private com.see.truetransact.uicomponent.CLabel lblATMGL;
    private com.see.truetransact.uicomponent.CLabel lblATMIssued;
    private com.see.truetransact.uicomponent.CLabel lblAcClosingChrg;
    private com.see.truetransact.uicomponent.CLabel lblAcctClosingChrg;
    private com.see.truetransact.uicomponent.CLabel lblAcctCrInt;
    private com.see.truetransact.uicomponent.CLabel lblAcctDebitInt;
    private com.see.truetransact.uicomponent.CLabel lblAcctHd;
    private com.see.truetransact.uicomponent.CLabel lblAcctOpenCharges;
    private com.see.truetransact.uicomponent.CLabel lblAcctOpenChrg;
    private com.see.truetransact.uicomponent.CLabel lblAllowWith;
    private com.see.truetransact.uicomponent.CLabel lblBehaves;
    private com.see.truetransact.uicomponent.CLabel lblCalcCriteria;
    private com.see.truetransact.uicomponent.CLabel lblChequeAllowed;
    private com.see.truetransact.uicomponent.CLabel lblChkBkIssueChrg;
    private com.see.truetransact.uicomponent.CLabel lblChkBkIssueChrgPL;
    private com.see.truetransact.uicomponent.CLabel lblChkIssuedChrg;
    private com.see.truetransact.uicomponent.CLabel lblChkRetChrOutward;
    private com.see.truetransact.uicomponent.CLabel lblChkRetChrgIn;
    private com.see.truetransact.uicomponent.CLabel lblChrgMiscServChrg;
    private com.see.truetransact.uicomponent.CLabel lblChrgPreClosure;
    private com.see.truetransact.uicomponent.CLabel lblClearingIntAcctHd;
    private com.see.truetransact.uicomponent.CLabel lblCollectInt;
    private com.see.truetransact.uicomponent.CLabel lblCrIntApplFreq;
    private com.see.truetransact.uicomponent.CLabel lblCrIntApplicable;
    private com.see.truetransact.uicomponent.CLabel lblCrIntCalcFreq;
    private com.see.truetransact.uicomponent.CLabel lblCrIntGiven;
    private com.see.truetransact.uicomponent.CLabel lblCrIntUnclear;
    private com.see.truetransact.uicomponent.CLabel lblCreditCdIssued;
    private com.see.truetransact.uicomponent.CLabel lblCreditComp;
    private com.see.truetransact.uicomponent.CLabel lblCreditCompIntCalcFreq;
    private com.see.truetransact.uicomponent.CLabel lblCreditIntRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblCreditProductRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDebitCardIssued;
    private com.see.truetransact.uicomponent.CLabel lblDebitChargeType;
    private com.see.truetransact.uicomponent.CLabel lblDebitChargeTypeRate;
    private com.see.truetransact.uicomponent.CLabel lblDebitCompIntCalcFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitCompoundReq;
    private com.see.truetransact.uicomponent.CLabel lblDebitInt;
    private com.see.truetransact.uicomponent.CLabel lblDebitIntApplFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitIntApplicable;
    private com.see.truetransact.uicomponent.CLabel lblDebitIntCalcFreq;
    private com.see.truetransact.uicomponent.CLabel lblDebitIntChrg;
    private com.see.truetransact.uicomponent.CLabel lblDebitIntRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDebitProductRoundOff;
    private com.see.truetransact.uicomponent.CLabel lblDebitWithdrawalChargeHead;
    private com.see.truetransact.uicomponent.CLabel lblDebitWithsrawalChargePeriod;
    private com.see.truetransact.uicomponent.CLabel lblDesc;
    private com.see.truetransact.uicomponent.CLabel lblEndDayProdCalcSBCrInt;
    private com.see.truetransact.uicomponent.CLabel lblEndInterCalc;
    private com.see.truetransact.uicomponent.CLabel lblEndProdCalc;
    private com.see.truetransact.uicomponent.CLabel lblExcessFreeWDChrg;
    private com.see.truetransact.uicomponent.CLabel lblExcessFreeWDChrgPT;
    private com.see.truetransact.uicomponent.CLabel lblFlexiHappen;
    private com.see.truetransact.uicomponent.CLabel lblFlexiTD;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargeAppl;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargeLastAppliedDt;
    private com.see.truetransact.uicomponent.CLabel lblFolioChargeNextAppDt;
    private com.see.truetransact.uicomponent.CLabel lblFolioChrg;
    private com.see.truetransact.uicomponent.CLabel lblFolioChrgApplFreq;
    private com.see.truetransact.uicomponent.CLabel lblFolioRestriction;
    private com.see.truetransact.uicomponent.CLabel lblFolioToChargeOn;
    private com.see.truetransact.uicomponent.CLabel lblFreeChkLeavePd;
    private com.see.truetransact.uicomponent.CLabel lblFreeChkLeaveStart;
    private com.see.truetransact.uicomponent.CLabel lblFreeWDChrg;
    private com.see.truetransact.uicomponent.CLabel lblFreeWDPd;
    private com.see.truetransact.uicomponent.CLabel lblFreeWDStart;
    private com.see.truetransact.uicomponent.CLabel lblIMinBalForInts;
    private com.see.truetransact.uicomponent.CLabel lblIVRSProvided;
    private com.see.truetransact.uicomponent.CLabel lblInOpAcChrg;
    private com.see.truetransact.uicomponent.CLabel lblInOpAcChrgPd;
    private com.see.truetransact.uicomponent.CLabel lblInOpChrg;
    private com.see.truetransact.uicomponent.CLabel lblInOperative;
    private com.see.truetransact.uicomponent.CLabel lblIncompFolioROffFreq;
    private com.see.truetransact.uicomponent.CLabel lblIntCalcEndMon;
    private com.see.truetransact.uicomponent.CLabel lblIntroReq;
    private com.see.truetransact.uicomponent.CLabel lblInwardChkRetChrg;
    private com.see.truetransact.uicomponent.CLabel lblIssue;
    private com.see.truetransact.uicomponent.CLabel lblLastAccNum;
    private com.see.truetransact.uicomponent.CLabel lblLastIntApplDate;
    private com.see.truetransact.uicomponent.CLabel lblLastIntApplDateCr;
    private com.see.truetransact.uicomponent.CLabel lblLastIntCalcDateCR;
    private com.see.truetransact.uicomponent.CLabel lblLastIntCalcDateDebit;
    private com.see.truetransact.uicomponent.CLabel lblLimit;
    private com.see.truetransact.uicomponent.CLabel lblLinkFlexiAcct;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmtWDSlip;
    private com.see.truetransact.uicomponent.CLabel lblMaxCrIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblMaxCrIntRate;
    private com.see.truetransact.uicomponent.CLabel lblMaxDebitIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblMaxDebitIntRate;
    private com.see.truetransact.uicomponent.CLabel lblMinATMBal;
    private com.see.truetransact.uicomponent.CLabel lblMinBal1FlexiDep;
    private com.see.truetransact.uicomponent.CLabel lblMinBal2FlexiDep;
    private com.see.truetransact.uicomponent.CLabel lblMinBalABB;
    private com.see.truetransact.uicomponent.CLabel lblMinBalAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinBalChqbk;
    private com.see.truetransact.uicomponent.CLabel lblMinBalCreditCd;
    private com.see.truetransact.uicomponent.CLabel lblMinBalDebitCards;
    private com.see.truetransact.uicomponent.CLabel lblMinBalIVRS;
    private com.see.truetransact.uicomponent.CLabel lblMinBalwochk;
    private com.see.truetransact.uicomponent.CLabel lblMinCrIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinCrIntRate;
    private com.see.truetransact.uicomponent.CLabel lblMinDebitIntAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinDrIntRate;
    private com.see.truetransact.uicomponent.CLabel lblMinMobBank;
    private com.see.truetransact.uicomponent.CLabel lblMinTreatNewAcctClosure;
    private com.see.truetransact.uicomponent.CLabel lblMinTreatasDormant;
    private com.see.truetransact.uicomponent.CLabel lblMinTreatasInOp;
    private com.see.truetransact.uicomponent.CLabel lblMinTreatasNew;
    private com.see.truetransact.uicomponent.CLabel lblMiscServChrg;
    private com.see.truetransact.uicomponent.CLabel lblMobBankClient;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNROTax;
    private com.see.truetransact.uicomponent.CLabel lblNoEntryPerFolio;
    private com.see.truetransact.uicomponent.CLabel lblNoFreeChkLeaves;
    private com.see.truetransact.uicomponent.CLabel lblNoFreeWD;
    private com.see.truetransact.uicomponent.CLabel lblNoNominee;
    private com.see.truetransact.uicomponent.CLabel lblNomineereq;
    private com.see.truetransact.uicomponent.CLabel lblNonMainMinBalCharges;
    private com.see.truetransact.uicomponent.CLabel lblNonMainMinBalChrg;
    private com.see.truetransact.uicomponent.CLabel lblNumPatternFollowed;
    private com.see.truetransact.uicomponent.CLabel lblOutwardChkRetChrg;
    private com.see.truetransact.uicomponent.CLabel lblPenalIntChrgStart;
    private com.see.truetransact.uicomponent.CLabel lblPenalIntDebitBalAcct;
    private com.see.truetransact.uicomponent.CLabel lblPer1;
    private com.see.truetransact.uicomponent.CLabel lblPer2;
    private com.see.truetransact.uicomponent.CLabel lblPer3;
    private com.see.truetransact.uicomponent.CLabel lblPer4;
    private com.see.truetransact.uicomponent.CLabel lblPer5;
    private com.see.truetransact.uicomponent.CLabel lblPer6;
    private com.see.truetransact.uicomponent.CLabel lblPer7;
    private com.see.truetransact.uicomponent.CLabel lblPrematureClosureChrg;
    private com.see.truetransact.uicomponent.CLabel lblProdCurrency;
    private com.see.truetransact.uicomponent.CLabel lblProdFreqCr;
    private com.see.truetransact.uicomponent.CLabel lblProductFreq;
    private com.see.truetransact.uicomponent.CLabel lblProductID;
    private com.see.truetransact.uicomponent.CLabel lblRateNRO;
    private com.see.truetransact.uicomponent.CLabel lblRatePerFolio;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStDayProdCalcSBCrInt;
    private com.see.truetransact.uicomponent.CLabel lblStMonIntCalc;
    private com.see.truetransact.uicomponent.CLabel lblStaff;
    private com.see.truetransact.uicomponent.CLabel lblStartInterCalc;
    private com.see.truetransact.uicomponent.CLabel lblStartProdCalc;
    private com.see.truetransact.uicomponent.CLabel lblStatCharges;
    private com.see.truetransact.uicomponent.CLabel lblStatChargesChrg;
    private com.see.truetransact.uicomponent.CLabel lblStatChrg;
    private com.see.truetransact.uicomponent.CLabel lblStatFreq;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStopPayChrg;
    private com.see.truetransact.uicomponent.CLabel lblStopPaymentCharges;
    private com.see.truetransact.uicomponent.CLabel lblStopPaymentChrg;
    private com.see.truetransact.uicomponent.CLabel lblTaxGL;
    private com.see.truetransact.uicomponent.CLabel lblTempOD;
    private com.see.truetransact.uicomponent.CLabel lblToChargeOnApplFreq;
    private com.see.truetransact.uicomponent.CLabel lblToCollectFolioChrg;
    private com.see.truetransact.uicomponent.CLabel lblTypeOfAcc;
    private com.see.truetransact.uicomponent.CMenuBar mbrOperativeAcctProduct;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenuItem mitSaveAs;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panABBAllowed;
    private com.see.truetransact.uicomponent.CPanel panATMIssued;
    private com.see.truetransact.uicomponent.CPanel panAccount;
    private com.see.truetransact.uicomponent.CPanel panAcctCol1;
    private com.see.truetransact.uicomponent.CPanel panAcctCol2;
    private com.see.truetransact.uicomponent.CPanel panAcctHd;
    private com.see.truetransact.uicomponent.CPanel panAcctHead;
    private com.see.truetransact.uicomponent.CSeparator panAcctSep;
    private com.see.truetransact.uicomponent.CPanel panAllowWDSlip;
    private com.see.truetransact.uicomponent.CPanel panApplDrIntRate;
    private com.see.truetransact.uicomponent.CPanel panApplDrIntRate1;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panChargesData;
    private com.see.truetransact.uicomponent.CPanel panChkAllowed;
    private com.see.truetransact.uicomponent.CPanel panChkIssuedChrg;
    private com.see.truetransact.uicomponent.CPanel panCollectInt;
    private com.see.truetransact.uicomponent.CPanel panCrIntGiven;
    private com.see.truetransact.uicomponent.CPanel panCreditCdIssued;
    private com.see.truetransact.uicomponent.CPanel panCreditComp;
    private com.see.truetransact.uicomponent.CPanel panDebitCdIssued;
    private com.see.truetransact.uicomponent.CPanel panDebitCompoundReq;
    private com.see.truetransact.uicomponent.CPanel panDebitIntChrg;
    private com.see.truetransact.uicomponent.CPanel panDebitIntRate;
    private com.see.truetransact.uicomponent.CPanel panDebitProdData;
    private com.see.truetransact.uicomponent.CPanel panDebitWithdrawalCharge;
    private com.see.truetransact.uicomponent.CPanel panFlexiHappen;
    private com.see.truetransact.uicomponent.CPanel panFolioChargeAppl;
    private com.see.truetransact.uicomponent.CPanel panFolioData;
    private com.see.truetransact.uicomponent.CPanel panFolioToChargeOn;
    private com.see.truetransact.uicomponent.CPanel panFreeChkPd;
    private com.see.truetransact.uicomponent.CPanel panFreeWDPd;
    private com.see.truetransact.uicomponent.CPanel panIVRSProvided;
    private com.see.truetransact.uicomponent.CPanel panIntClearing;
    private com.see.truetransact.uicomponent.CPanel panIntPay;
    private com.see.truetransact.uicomponent.CPanel panIntPayProd;
    private com.see.truetransact.uicomponent.CPanel panIntPayRate;
    private com.see.truetransact.uicomponent.CPanel panIntRec;
    private com.see.truetransact.uicomponent.CPanel panIntUnClearBal;
    private com.see.truetransact.uicomponent.CPanel panIntroReq;
    private com.see.truetransact.uicomponent.CPanel panIssueToken;
    private com.see.truetransact.uicomponent.CPanel panLimitDefAllow;
    private com.see.truetransact.uicomponent.CPanel panLinkFlexiAcct;
    private com.see.truetransact.uicomponent.CPanel panMaxDrIntRate;
    private com.see.truetransact.uicomponent.CPanel panMaxDrIntRate1;
    private com.see.truetransact.uicomponent.CPanel panMinBalAmt;
    private com.see.truetransact.uicomponent.CPanel panMinCrIntRate;
    private com.see.truetransact.uicomponent.CPanel panMinDrIntRate;
    private com.see.truetransact.uicomponent.CPanel panMinPerd;
    private com.see.truetransact.uicomponent.CPanel panMinPerd1;
    private com.see.truetransact.uicomponent.CPanel panMiscDetails;
    private com.see.truetransact.uicomponent.CPanel panMobBankClient;
    private com.see.truetransact.uicomponent.CPanel panNomineeReq;
    private com.see.truetransact.uicomponent.CPanel panNonMainMinBalCharges;
    private com.see.truetransact.uicomponent.CPanel panOperativeProduct;
    private com.see.truetransact.uicomponent.CPanel panOtherAcctHead;
    private com.see.truetransact.uicomponent.CPanel panPenalIntDebitBalAcct;
    private com.see.truetransact.uicomponent.CPanel panReturnChargesData;
    private com.see.truetransact.uicomponent.CPanel panSpecialDetails;
    private com.see.truetransact.uicomponent.CPanel panSpecialItem;
    private com.see.truetransact.uicomponent.CPanel panStaffAcct;
    private com.see.truetransact.uicomponent.CPanel panStatCharges;
    private com.see.truetransact.uicomponent.CPanel panStatChargesChr;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTaxIntApplNRO;
    private com.see.truetransact.uicomponent.CPanel panTempODAllow;
    private com.see.truetransact.uicomponent.CPanel panToChargeOnApplFreq;
    private com.see.truetransact.uicomponent.CPanel panTypeAcc;
    private com.see.truetransact.uicomponent.CButtonGroup rdoABBAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoABBAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoABBAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoATMIssued;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMIssued_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoATMIssued_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoAcc_Nre;
    private com.see.truetransact.uicomponent.CRadioButton rdoAcc_Nro;
    private com.see.truetransact.uicomponent.CRadioButton rdoAcc_Reg;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAcctOpenAppr;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAllowWD;
    private com.see.truetransact.uicomponent.CRadioButton rdoAllowWD_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAllowWD_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoChkAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoChkAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoChkAllowed_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoChkIssuedChrg;
    private com.see.truetransact.uicomponent.CButtonGroup rdoChkIssuedChrgCh;
    private com.see.truetransact.uicomponent.CRadioButton rdoChkIssuedChrgCh_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoChkIssuedChrgCh_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCollectInt;
    private com.see.truetransact.uicomponent.CRadioButton rdoCollectInt_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCollectInt_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCrIntGiven;
    private com.see.truetransact.uicomponent.CRadioButton rdoCrIntGiven_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCrIntGiven_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditCdIssued;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCdIssued_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditCdIssued_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditComp;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditComp_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoCreditComp_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDebitCdIssued;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCdIssued_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCdIssued_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDebitCompReq;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCompReq_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitCompReq_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDebitIntChrg;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitIntChrg_No2;
    private com.see.truetransact.uicomponent.CRadioButton rdoDebitIntChrg_Yes2;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExtraIntAppl;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFlexiHappen;
    private com.see.truetransact.uicomponent.CRadioButton rdoFlexiHappen_SB;
    private com.see.truetransact.uicomponent.CRadioButton rdoFlexiHappen_TD;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFolioChargeAppl;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioChargeAppl_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioChargeAppl_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFolioChargeGroup;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioChargeVariable;
    private com.see.truetransact.uicomponent.CButtonGroup rdoFolioToChargeOn;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioToChargeOn_Both;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioToChargeOn_Credit;
    private com.see.truetransact.uicomponent.CRadioButton rdoFolioToChargeOn_Debit;
    private com.see.truetransact.uicomponent.CRadioButton rdoFoliochargeFixed;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIVRSProvided;
    private com.see.truetransact.uicomponent.CRadioButton rdoIVRSProvided_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIVRSProvided_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIntClearing;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntClearing_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntClearing_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIntUnClearBal;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntUnClearBal_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntUnClearBal_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIntroReq;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntroReq_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIntroReq_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIssueToken;
    private com.see.truetransact.uicomponent.CRadioButton rdoIssueToken_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIssueToken_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLimitDefAllow;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitDefAllow_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLimitDefAllow_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoLinkFlexiAcct;
    private com.see.truetransact.uicomponent.CRadioButton rdoLinkFlexiAcct_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoLinkFlexiAcct_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMobBankClient;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobBankClient_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoMobBankClient_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNomineeReq;
    private com.see.truetransact.uicomponent.CRadioButton rdoNomineeReq_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoNomineeReq_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNonMainMinBalCharges;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNonMainMinBalChrg;
    private com.see.truetransact.uicomponent.CRadioButton rdoNonMainMinBalChrg_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoNonMainMinBalChrg_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStaffAcct;
    private com.see.truetransact.uicomponent.CRadioButton rdoStaffAcct_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStaffAcct_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatCharges;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatChargesCh;
    private com.see.truetransact.uicomponent.CRadioButton rdoStatCharges_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStatCharges_Yes;
    private com.see.truetransact.uicomponent.CPanel rdoStopPaymentCharges;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStopPaymentChrg;
    private com.see.truetransact.uicomponent.CRadioButton rdoStopPaymentChrg_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoStopPaymentChrg_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTaxIntApplNRO;
    private com.see.truetransact.uicomponent.CRadioButton rdoTaxIntApplNRO_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoTaxIntApplNRO_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTempODAllow;
    private com.see.truetransact.uicomponent.CRadioButton rdoTempODAllow_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoTempODAllow_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoToChargeOnApplFreq;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeOnApplFreq_Both;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeOnApplFreq_Manual;
    private com.see.truetransact.uicomponent.CRadioButton rdoToChargeOnApplFreq_System;
    private com.see.truetransact.uicomponent.CButtonGroup rdoTypeAcc;
    private com.see.truetransact.uicomponent.CSeparator sptAcct;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptCrInt;
    private com.see.truetransact.uicomponent.CSeparator sptFolio;
    private com.see.truetransact.uicomponent.CSeparator sptFolioVertical;
    private com.see.truetransact.uicomponent.CSeparator sptIntReceivable;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CTabbedPane tabOperativeAcctProduct;
    private javax.swing.JToolBar tbrOperativeAcctProduct;
    private com.see.truetransact.uicomponent.CDateField tdtFreeChkLeaveStFrom;
    private com.see.truetransact.uicomponent.CDateField tdtFreeWDStFrom;
    private com.see.truetransact.uicomponent.CDateField tdtLastFolioAppliedDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastIntApplDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastIntApplDateCr;
    private com.see.truetransact.uicomponent.CDateField tdtLastIntCalcDate;
    private com.see.truetransact.uicomponent.CDateField tdtLastIntCalcDateCR;
    private com.see.truetransact.uicomponent.CDateField tdtNextFolioAppliedDt;
    private com.see.truetransact.uicomponent.CTextField txtATMGL;
    private com.see.truetransact.uicomponent.CTextField txtATMGLDisplay;
    private com.see.truetransact.uicomponent.CTextField txtAcClosingChrg;
    private com.see.truetransact.uicomponent.CTextField txtAcctClosingChrg;
    private com.see.truetransact.uicomponent.CTextField txtAcctClosingChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtAcctCrInt;
    private com.see.truetransact.uicomponent.CTextField txtAcctCrIntDesc;
    private com.see.truetransact.uicomponent.CTextField txtAcctDebitInt;
    private com.see.truetransact.uicomponent.CTextField txtAcctDebitIntDesc;
    private com.see.truetransact.uicomponent.CTextField txtAcctHd;
    private com.see.truetransact.uicomponent.CTextField txtAcctOpenCharges;
    private com.see.truetransact.uicomponent.CTextField txtAcctOpenChrg;
    private com.see.truetransact.uicomponent.CTextField txtAcctOpenChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtApplCrIntRate;
    private com.see.truetransact.uicomponent.CTextField txtApplDebitIntRate;
    private com.see.truetransact.uicomponent.CTextField txtChkBkIssueChrg;
    private com.see.truetransact.uicomponent.CTextField txtChkBkIssueChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtChkBkIssueChrgPL;
    private com.see.truetransact.uicomponent.CTextField txtChkRetChrOutward;
    private com.see.truetransact.uicomponent.CTextField txtChkRetChrgIn;
    private com.see.truetransact.uicomponent.CTextField txtChrgMiscServChrg;
    private com.see.truetransact.uicomponent.CTextField txtChrgPreClosure;
    private com.see.truetransact.uicomponent.CTextField txtClearingIntAcctHd;
    private com.see.truetransact.uicomponent.CTextField txtClearingIntAcctHdDesc;
    private com.see.truetransact.uicomponent.CTextField txtDebiWithdrawalChargePeriod;
    private com.see.truetransact.uicomponent.CTextField txtDebitChargeTypeRate;
    private com.see.truetransact.uicomponent.CTextField txtDebitWithdrawalChargeDesc;
    private com.see.truetransact.uicomponent.CTextField txtDebitWithdrawalChargeHead;
    private com.see.truetransact.uicomponent.CTextField txtDesc;
    private com.see.truetransact.uicomponent.CTextField txtEndDayProdCalcSBCrInt;
    private com.see.truetransact.uicomponent.CTextField txtEndInterCalc;
    private com.see.truetransact.uicomponent.CTextField txtEndProdCalc;
    private com.see.truetransact.uicomponent.CTextField txtExcessFreeWDChrg;
    private com.see.truetransact.uicomponent.CTextField txtExcessFreeWDChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtExcessFreeWDChrgPT;
    private com.see.truetransact.uicomponent.CTextField txtFolioChrg;
    private com.see.truetransact.uicomponent.CTextField txtFolioChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtFolioRestrictionPriod;
    private com.see.truetransact.uicomponent.CTextField txtFreeChkPD;
    private com.see.truetransact.uicomponent.CTextField txtFreeWDChrg;
    private com.see.truetransact.uicomponent.CTextField txtFreeWDChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtFreeWDPd;
    private com.see.truetransact.uicomponent.CTextField txtIMPSLimit;
    private com.see.truetransact.uicomponent.CTextField txtInOpChrg;
    private com.see.truetransact.uicomponent.CTextField txtInOpChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtInOperative;
    private com.see.truetransact.uicomponent.CTextField txtInOperativeDesc;
    private com.see.truetransact.uicomponent.CTextField txtIntCalcEndMon;
    private com.see.truetransact.uicomponent.CTextField txtInwardChkRetChrg;
    private com.see.truetransact.uicomponent.CTextField txtInwardChkRetChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtLastAccNum;
    private com.see.truetransact.uicomponent.CTextField txtMainTreatNewAcctClosure;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmtWDSlip;
    private com.see.truetransact.uicomponent.CTextField txtMaxCrIntAmt;
    private com.see.truetransact.uicomponent.CTextField txtMaxCrIntRate;
    private com.see.truetransact.uicomponent.CTextField txtMaxDebitIntAmt;
    private com.see.truetransact.uicomponent.CTextField txtMaxDebitIntRate;
    private com.see.truetransact.uicomponent.CTextField txtMinATMBal;
    private com.see.truetransact.uicomponent.CTextField txtMinBal1FlexiDep;
    private com.see.truetransact.uicomponent.CTextField txtMinBal2FlexiDep;
    private com.see.truetransact.uicomponent.CTextField txtMinBalABB;
    private com.see.truetransact.uicomponent.CTextField txtMinBalAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinBalChkbk;
    private com.see.truetransact.uicomponent.CTextField txtMinBalCreditCd;
    private com.see.truetransact.uicomponent.CTextField txtMinBalDebitCards;
    private com.see.truetransact.uicomponent.CTextField txtMinBalIVRS;
    private com.see.truetransact.uicomponent.CTextField txtMinBalwchk;
    private com.see.truetransact.uicomponent.CTextField txtMinCrIntAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinCrIntRate;
    private com.see.truetransact.uicomponent.CTextField txtMinDebitIntAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinDebitIntRate;
    private com.see.truetransact.uicomponent.CTextField txtMinMobBank;
    private com.see.truetransact.uicomponent.CTextField txtMinTreatasDormant;
    private com.see.truetransact.uicomponent.CTextField txtMinTreatasInOp;
    private com.see.truetransact.uicomponent.CTextField txtMinTreatasNew;
    private com.see.truetransact.uicomponent.CTextField txtMinbalForInt;
    private com.see.truetransact.uicomponent.CTextField txtMiscServChrg;
    private com.see.truetransact.uicomponent.CTextField txtMiscServChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtNoEntryPerFolio;
    private com.see.truetransact.uicomponent.CTextField txtNoFreeChkLeaves;
    private com.see.truetransact.uicomponent.CTextField txtNoFreeWD;
    private com.see.truetransact.uicomponent.CTextField txtNoNominees;
    private com.see.truetransact.uicomponent.CTextField txtNonMainMinBalChrg;
    private com.see.truetransact.uicomponent.CTextField txtNonMainMinBalChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowedPrefix;
    private com.see.truetransact.uicomponent.CTextField txtNumPatternFollowedSuffix;
    private com.see.truetransact.uicomponent.CTextField txtOutwardChkRetChrg;
    private com.see.truetransact.uicomponent.CTextField txtOutwardChkRetChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtPenalIntChrgStart;
    private com.see.truetransact.uicomponent.CTextField txtPenalIntDebitBalAcct;
    private com.see.truetransact.uicomponent.CTextField txtPrematureClosureChrg;
    private com.see.truetransact.uicomponent.CTextField txtPrematureClosureChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtProductID;
    private com.see.truetransact.uicomponent.CTextField txtRatePerFolio;
    private com.see.truetransact.uicomponent.CTextField txtRateTaxNRO;
    private com.see.truetransact.uicomponent.CTextField txtStDayProdCalcSBCrInt;
    private com.see.truetransact.uicomponent.CTextField txtStMonIntCalc;
    private com.see.truetransact.uicomponent.CTextField txtStartInterCalc;
    private com.see.truetransact.uicomponent.CTextField txtStartProdCalc;
    private com.see.truetransact.uicomponent.CTextField txtStatChargesChr;
    private com.see.truetransact.uicomponent.CTextField txtStatChrg;
    private com.see.truetransact.uicomponent.CTextField txtStatChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtStopPayChrg;
    private com.see.truetransact.uicomponent.CTextField txtStopPaymentChrg;
    private com.see.truetransact.uicomponent.CTextField txtStopPaymentChrgDesc;
    private com.see.truetransact.uicomponent.CTextField txtTaxGL;
    private com.see.truetransact.uicomponent.CTextField txtTaxGLDesc;
    private com.see.truetransact.uicomponent.CTextField txtlInOpAcChrg;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        JFrame jf = new JFrame();
        OperativeAcctProductnewUI gui = new OperativeAcctProductnewUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
    
}


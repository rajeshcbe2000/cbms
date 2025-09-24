/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * MDSClosedReciptUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 */

package com.see.truetransact.ui.mdsapplication.mdsclosedreceipt;

import java.util.*;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryMRB;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.EditWaiveTableUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.customer.SmartCustomerUI;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import org.apache.xerces.parsers.IntegratedParserConfiguration;
import javax.swing.*;
/**
 *
 * @author Hemant
 * Modification Lohith R.
 *@modified : Sunil
 *      Added Edit Locking - 08-07-2005
 */

public class MDSClosedReciptUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private RemittanceProductRB resourceBundle = new RemittanceProductRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdsreceiptentry.MDSReceiptEntryRB", ProxyParameters.LANGUAGE);
    final MDSClosedReciptMRB objMandatoryMRB = new MDSClosedReciptMRB();
    
    private TransactionUI transactionUI = new TransactionUI();
    private HashMap mandatoryMap;
    private MDSClosedReciptOB observable;
    //    private RemittanceProductMRB objMandatoryMRB;
    private String viewType = "";
    private boolean isFilled = false;
    private final String AUTHORIZE="AUTHORIZE";
    private String accountHead = "ACCOUNT HEAD ID";
    private Date curr_dt=null;
    HashMap addressMap = null;
    //Newly Added
    String isSplitMDSTransaction = "";
    private boolean isPenalEdit = true;
    ArrayList bonusList = null;
    ArrayList bonusAmountList = null;
    ArrayList bonusReversalLsit = null; // 16-07-2020
    ArrayList copyList = new ArrayList();
    ArrayList instAmountList = null;
    private HashMap splitTransMap = new HashMap();
    ArrayList penalList = null;
    ArrayList penalRealList = null;
    ArrayList noticeList = null;
    ArrayList narrationList = null;
    ArrayList ForfeitebonusAmountList = null;
    //
    boolean fromAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    private ServiceTaxCalculation objServiceTax;
    public HashMap serviceTax_Map;
    private String isWeeklyOrMonthlyScheme = "";
    private double penalAmtWithoutEdit =0.0;
    String bonusPrint = "";
    boolean fromSmartCustUI = false;
    SmartCustomerUI smartUI = null;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;
    private EditWaiveTableUI editableWaiveUI = null;
    
    /** Creates new form BeanForm */
    public MDSClosedReciptUI() {
        initComponents();
        settingupUI();
        tabRemittanceProduct.resetVisits();
    }
    
    private void settingupUI(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSchemeNameDetails);
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this,false);
        panInsideGeneralRemittance1.add(transactionUI);
        transactionUI.setSourceScreen("MDS_RECEIPT");
        txtSchemeName.setEnabled(false);
        txtInstPayable.setEnabled(false);
        txtPenalAmtPayable.setEnabled(false);
        txtBonusAmt.setEnabled(false);
        txtForfeitBonus.setEnabled(false); //2093
        txtDiscountAmt.setEnabled(false);
        btnViewLedger.setEnabled(false);
        //        txtInterest.setEnabled(false);
        txtNetAmtPaid.setEnabled(false);
        btnDelete.setEnabled(true);
        btnNoOfInstToPaay.setEnabled(false);
        curr_dt=ClientUtil.getCurrentDate();
        panPrizedMember1.setVisible(false);
        lblPrizedMember1.setVisible(false);
        cbobranch.setModel(observable.getCbmbranch());
    }
    
    private void setObservable() {
        try{
            observable = MDSClosedReciptOB.getInstance();
            observable.setTransactionOB(transactionUI.getTransactionOB());
            observable.addObserver(this);
            observable.fillDropdown();
        }catch (Exception e){
            
        }
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnChittalNo.setName("btnChittalNo");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnNoOfInstToPaay.setName("btnNoOfInstToPaay");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSchemeName.setName("btnSchemeName");
        btnView.setName("btnView");
        chkMemberChanged.setName("chkMemberChanged");
        chkMunnal.setName("chkMunnal");
        chkThalayal.setName("chkThalayal");
        lblArea2.setName("lblArea2");
        lblAribitrationAmt.setName("lblAribitrationAmt");
        lblBonusAmt.setName("lblBonusAmt");
        lblBonusAmtAvail.setName("lblBonusAmtAvail");
        lblChangedDate.setName("lblChangedDate");
        lblChangedInst.setName("lblChangedInst");
        lblChitEndDt6.setName("lblChitEndDt6");
        lblChitStartDt.setName("lblChitStartDt");
        lblChittalNo.setName("lblChittalNo");
        lblCity2.setName("lblCity2");
        lblCurrentInstNo.setName("lblCurrentInstNo");
        lblDiscountAmt.setName("lblDiscountAmt");
        lblDivisionNo.setName("lblDivisionNo");
        lblEarlierMember.setName("lblEarlierMember");
        lblEarlierMemberName.setName("lblEarlierMemberName");
        lblInstAmt.setName("lblInstAmt");
        lblInstPayable.setName("lblInstPayable");
        //        lblInterest.setName("lblInterest");
        lblMemberChanged.setName("lblMemberChanged");
        lblMemberName.setName("lblMemberName");
        lblMemberNameVal.setName("lblMemberNameVal");
        lblMsg.setName("lblMsg");
        lblMunnal.setName("lblMunnal");
        lblNetAmtPaid.setName("lblNetAmtPaid");
        lblNoOfInst.setName("lblNoOfInst");
        lblNoOfInstToPaay.setName("lblNoOfInstToPaay");
        lblNoticeAmt.setName("lblNoticeAmt");
        lblPaidAmt.setName("lblPaidAmt");
        lblPaidDate.setName("lblPaidDate");
        lblPaidInst.setName("lblPaidInst");
        lblPenalAmtPayable.setName("lblPenalAmtPayable");
        lblPendingInst.setName("lblPendingInst");
        lblPin2.setName("lblPin2");
        lblPrizedMember.setName("lblPrizedMember");
        lblSchemeName.setName("lblSchemeName");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblState2.setName("lblState2");
        lblStatus.setName("lblStatus");
        lblStreet2.setName("lblStreet2");
        lblThalayal.setName("lblThalayal");
        lblTotalInstAmt.setName("lblTotalInstAmt");
        lblValArea.setName("lblValArea");
        lblValCity.setName("lblValCity");
        lblValPin.setName("lblValPin");
        lblValState.setName("lblValState");
        lblValStreet.setName("lblValStreet");
        mbrMain.setName("mbrMain");
        panChittalNo.setName("panChittalNo");
        panDiscountPeriodDetails1.setName("panDiscountPeriodDetails1");
        panGeneralRemittance.setName("panGeneralRemittance");
        panGeneralRemittance1.setName("panGeneralRemittance1");
        //        panInsideGeneralRemittance.setName("panInsideGeneralRemittance");
        panInsideGeneralRemittance1.setName("panInsideGeneralRemittance1");
        panInsideGeneralRemittance2.setName("panInsideGeneralRemittance2");
        panInsideGeneralRemittance3.setName("panInsideGeneralRemittance3");
        panInsideGeneralRemittance4.setName("panInsideGeneralRemittance4");
        panInsideGeneralRemittance5.setName("panInsideGeneralRemittance5");
        panLoanDetails.setName("panLoanDetails");
        panLoanDetailsTable.setName("panLoanDetailsTable");
        panOtherDetails.setName("panOtherDetails");
        panPrizedDetails.setName("panPrizedDetails");
        panPrizedMember.setName("panPrizedMember");
        panSchemeName.setName("panSchemeName");
        panStatus.setName("panStatus");
        panTable2_SD.setName("panTable2_SD");
        //        panThalayalAllowed1.setName("panThalayalAllowed1");
        panTransactionDetails.setName("panTransactionDetails");
        panTransactionTable.setName("panTransactionTable");
        rdoPrizedMember_No.setName("rdoPrizedMember_No");
        rdoPrizedMember_Yes.setName("rdoPrizedMember_Yes");
        srpLoanDetailsTable.setName("srpLoanDetailsTable");
        srpOtherDetailsTable.setName("srpOtherDetailsTable");
        srpTransactionTable.setName("srpTransactionTable");
        tabRemittanceProduct.setName("tabRemittanceProduct");
        tblLoanDetailsTable.setName("tblLoanDetailsTable");
        tblOtherDetailsTable.setName("tblOtherDetailsTable");
        tblTransactionTable.setName("tblTransactionTable");
        tdtChangedDate.setName("tdtChangedDate");
        tdtChitStartDt.setName("tdtChitStartDt");
        tdtPaidDate.setName("tdtPaidDate");
        txtAribitrationAmt.setName("txtAribitrationAmt");
        txtBonusAmt.setName("txtBonusAmt");
        txtBonusAmtAvail.setName("txtBonusAmtAvail");
        txtChangedInst.setName("txtChangedInst");
        txtChittalNo.setName("txtChittalNo");
        txtCurrentInstNo.setName("txtCurrentInstNo");
        txtDiscountAmt.setName("txtDiscountAmt");
        txtDivisionNo.setName("txtDivisionNo");
        txtEarlierMember.setName("txtEarlierMember");
        txtEarlierMemberName.setName("txtEarlierMemberName");
        txtInstAmt.setName("txtInstAmt");
        txtInstPayable.setName("txtInstPayable");
        //        txtInterest.setName("txtInterest");
        txtNetAmtPaid.setName("txtNetAmtPaid");
        txtNoOfInst.setName("txtNoOfInst");
        txtSubNo.setName("txtSubNo");
        txtNoOfInstToPaay.setName("txtNoOfInstToPaay");
        txtNoticeAmt.setName("txtNoticeAmt");
        txtPaidAmt.setName("txtPaidAmt");
        txtPaidInst.setName("txtPaidInst");
        txtPenalAmtPayable.setName("txtPenalAmtPayable");
        txtPendingInst.setName("txtPendingInst");
        txtSchemeName.setName("txtSchemeName");
        txtTotalInstAmt.setName("txtTotalInstAmt");
    }
    
    /* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblInstAmt.setText(resourceBundle.getString("lblInstAmt"));
        chkThalayal.setText(resourceBundle.getString("chkThalayal"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblPaidAmt.setText(resourceBundle.getString("lblPaidAmt"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblValArea.setText(resourceBundle.getString("lblValArea"));
        lblChitEndDt6.setText(resourceBundle.getString("lblChitEndDt6"));
        lblArea2.setText(resourceBundle.getString("lblArea2"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblValStreet.setText(resourceBundle.getString("lblValStreet"));
        lblEarlierMember.setText(resourceBundle.getString("lblEarlierMember"));
        lblChangedDate.setText(resourceBundle.getString("lblChangedDate"));
        lblThalayal.setText(resourceBundle.getString("lblThalayal"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        btnChittalNo.setText(resourceBundle.getString("btnChittalNo"));
        lblInstPayable.setText(resourceBundle.getString("lblInstPayable"));
        lblPrizedMember.setText(resourceBundle.getString("lblPrizedMember"));
        rdoPrizedMember_Yes.setText(resourceBundle.getString("rdoPrizedMember_Yes"));
        lblBonusAmtAvail.setText(resourceBundle.getString("lblBonusAmtAvail"));
        lblChitStartDt.setText(resourceBundle.getString("lblChitStartDt"));
        lblNoticeAmt.setText(resourceBundle.getString("lblNoticeAmt"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblDivisionNo.setText(resourceBundle.getString("lblDivisionNo"));
        lblPendingInst.setText(resourceBundle.getString("lblPendingInst"));
        lblPenalAmtPayable.setText(resourceBundle.getString("lblPenalAmtPayable"));
        lblEarlierMemberName.setText(resourceBundle.getString("lblEarlierMemberName"));
        chkMemberChanged.setText(resourceBundle.getString("chkMemberChanged"));
        lblTotalInstAmt.setText(resourceBundle.getString("lblTotalInstAmt"));
        lblPin2.setText(resourceBundle.getString("lblPin2"));
        lblMunnal.setText(resourceBundle.getString("lblMunnal"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblNoOfInst.setText(resourceBundle.getString("lblNoOfInst"));
        lblChangedInst.setText(resourceBundle.getString("lblChangedInst"));
        lblNetAmtPaid.setText(resourceBundle.getString("lblNetAmtPaid"));
        lblValState.setText(resourceBundle.getString("lblValState"));
        lblDiscountAmt.setText(resourceBundle.getString("lblDiscountAmt"));
        lblPaidInst.setText(resourceBundle.getString("lblPaidInst"));
        chkMunnal.setText(resourceBundle.getString("chkMunnal"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblCurrentInstNo.setText(resourceBundle.getString("lblCurrentInstNo"));
        btnNoOfInstToPaay.setText(resourceBundle.getString("btnNoOfInstToPaay"));
        lblNoOfInstToPaay.setText(resourceBundle.getString("lblNoOfInstToPaay"));
        lblValPin.setText(resourceBundle.getString("lblValPin"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblChittalNo.setText(resourceBundle.getString("lblChittalNo"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblAribitrationAmt.setText(resourceBundle.getString("lblAribitrationAmt"));
        rdoPrizedMember_No.setText(resourceBundle.getString("rdoPrizedMember_No"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblSchemeName.setText(resourceBundle.getString("lblSchemeName"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        //        lblInterest.setText(resourceBundle.getString("lblInterest"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblStreet2.setText(resourceBundle.getString("lblStreet2"));
        lblBonusAmt.setText(resourceBundle.getString("lblBonusAmt"));
        lblMemberNameVal.setText(resourceBundle.getString("lblMemberNameVal"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        lblCity2.setText(resourceBundle.getString("lblCity2"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblState2.setText(resourceBundle.getString("lblState2"));
        lblValCity.setText(resourceBundle.getString("lblValCity"));
        lblPaidDate.setText(resourceBundle.getString("lblPaidDate"));
        btnSchemeName.setText(resourceBundle.getString("btnSchemeName"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblMemberChanged.setText(resourceBundle.getString("lblMemberChanged"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        lblInsUptoPaid.setText(resourceBundle.getString("lblInsUptoPaid"));
    }
    
    /* Auto Generated Method - setMandatoryHashMap()
     
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNetAmtPaid", new Boolean(false));
        //        mandatoryMap.put("txtInterest", new Boolean(false));
        mandatoryMap.put("txtDiscountAmt", new Boolean(false));
        mandatoryMap.put("txtBonusAmt", new Boolean(false));
        mandatoryMap.put("txtPenalAmtPayable", new Boolean(false));
        mandatoryMap.put("txtInstPayable", new Boolean(false));
        mandatoryMap.put("txtNoOfInstToPaay", new Boolean(false));
        mandatoryMap.put("txtTotalInstAmt", new Boolean(false));
        mandatoryMap.put("txtPendingInst", new Boolean(false));
        mandatoryMap.put("txtInstAmt", new Boolean(false));
        mandatoryMap.put("txtCurrentInstNo", new Boolean(false));
        mandatoryMap.put("txtNoOfInst", new Boolean(false));
        mandatoryMap.put("tdtChitStartDt", new Boolean(false));
        mandatoryMap.put("rdoPrizedMember_Yes", new Boolean(false));
        mandatoryMap.put("txtBonusAmtAvail", new Boolean(false));
        mandatoryMap.put("txtDivisionNo", new Boolean(false));
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtNoticeAmt", new Boolean(false));
        mandatoryMap.put("txtAribitrationAmt", new Boolean(false));
        mandatoryMap.put("txtEarlierMember", new Boolean(false));
        mandatoryMap.put("txtChangedInst", new Boolean(false));
        mandatoryMap.put("txtEarlierMemberName", new Boolean(false));
        mandatoryMap.put("chkThalayal", new Boolean(false));
        mandatoryMap.put("chkMunnal", new Boolean(false));
        mandatoryMap.put("chkMemberChanged", new Boolean(false));
        mandatoryMap.put("tdtChangedDate", new Boolean(false));
        mandatoryMap.put("txtPaidAmt", new Boolean(false));
        mandatoryMap.put("txtPaidInst", new Boolean(false));
        mandatoryMap.put("tdtPaidDate", new Boolean(false));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        try{
            
            
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        txtDivisionNo.setValidation(new NumericValidation());
        txtNoOfInst.setValidation(new NumericValidation());
        txtSubNo.setValidation(new NumericValidation());
        txtCurrentInstNo.setValidation(new NumericValidation());
        txtPaidInstallments.setValidation(new NumericValidation());
        txtInstAmt.setValidation(new CurrencyValidation());
        txtPendingInst.setValidation(new NumericValidation());
        txtTotalInstAmt.setValidation(new CurrencyValidation());
        txtBonusAmtAvail.setValidation(new CurrencyValidation());
        txtNoticeAmt.setValidation(new CurrencyValidation());
        txtAribitrationAmt.setValidation(new CurrencyValidation());
        txtNoOfInstToPaay.setValidation(new NumericValidation());
        txtInstPayable.setValidation(new CurrencyValidation());
        txtPenalAmtPayable.setValidation(new CurrencyValidation());
        txtBonusAmt.setValidation(new CurrencyValidation());
        txtDiscountAmt.setValidation(new CurrencyValidation());
        txtNetAmtPaid.setValidation(new CurrencyValidation());
        txtPaidInst.setValidation(new NumericValidation());
        txtPaidAmt.setValidation(new CurrencyValidation());
        txtChangedInst.setValidation(new NumericValidation());
        txtSchemeName.setAllowAll(true);
        txtChittalNo.setAllowAll(true);
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtSchemeName.setText(observable.getTxtSchemeName());
        txtChittalNo.setText(observable.getTxtChittalNo());
        lblMemberNameVal.setText(observable.getLblMemberNameVal());
        lblMemberNo.setText(observable.getLblMemberNo());
        txtDivisionNo.setText(observable.getTxtDivisionNo());
        tdtChitStartDt.setDateValue(observable.getTdtChitStartDt());
        tdtInsUptoPaid.setDateValue(observable.getTdtInsUptoPaid());
        tdtInstDt.setDateValue(observable.getTdtChitEndDt());
        txtNoOfInst.setText(observable.getTxtNoOfInst());
        txtSubNo.setText(observable.getTxtSubNo());
        txtCurrentInstNo.setText(observable.getTxtCurrentInstNo());
        txtInstAmt.setText(observable.getTxtInstAmt());
        txtPendingInst.setText(observable.getTxtPendingInst());
        txtTotalInstAmt.setText(observable.getTxtTotalInstAmt());
        txtBonusAmtAvail.setText(observable.getTxtBonusAmtAvail());
        rdoPrizedMember_Yes.setSelected(observable.getRdoPrizedMember_Yes());
        rdoPrizedMember_No.setSelected(observable.getRdoPrizedMember_No());
//        rdoBankPay_Yes.setSelected(observable.getRdoBankPay_Yes());
//        rdoBankPay_No.setSelected(observable.getRdoBankPay_No());
        txtNoticeAmt.setText(observable.getTxtNoticeAmt());
        txtAribitrationAmt.setText(observable.getTxtAribitrationAmt());
        txtNoOfInstToPaay.setText(observable.getTxtNoOfInstToPaay());
        txtInstPayable.setText(observable.getTxtInstPayable());
        txtPenalAmtPayable.setText(observable.getTxtPenalAmtPayable());
        txtBonusAmt.setText(observable.getTxtBonusAmt());
        txtForfeitBonus.setText(observable.getTxtForfeitBonus()); //2093
        txtDiscountAmt.setText(observable.getTxtDiscountAmt());
        //        txtInterest.setText(observable.getTxtInterest());
        txtNetAmtPaid.setText(observable.getTxtNetAmtPaid());
        
        tdtPaidDate.setDateValue(observable.getTdtPaidDate());
        txtPaidInst.setText(observable.getTxtPaidInst());
        txtPaidInstallments.setText(observable.getTxtPaidNoOfInst());
        txtPaidAmt.setText(observable.getTxtPaidAmt());
        chkThalayal.setSelected(observable.getChkThalayal());
        chkMunnal.setSelected(observable.getChkMunnal());
        chkMemberChanged.setSelected(observable.getChkMemberChanged());
        txtEarlierMember.setText(observable.getTxtEarlierMember());
        txtEarlierMemberName.setText(observable.getTxtEarlierMemberName());
        txtChangedInst.setText(observable.getTxtChangedInst());
        tdtChangedDate.setDateValue(observable.getTdtChangedDate());
        lblStatus.setText(observable.getLblStatus());
        addRadioButtons();
        changeRdoBtnToBold();
		lblServiceTaxval.setText(observable.getLblServiceTaxval());
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdgPrizeMember.remove(rdoPrizedMember_Yes);
        rdgPrizeMember.remove(rdoPrizedMember_No);
        
        rdgBankAdvance.remove(rdoBankPay_Yes);
        rdgBankAdvance.remove(rdoBankPay_No);
        changeRdoBtnToBold();
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdgPrizeMember = new CButtonGroup();
        rdgPrizeMember.add(rdoPrizedMember_Yes);
        rdgPrizeMember.add(rdoPrizedMember_No);
        
        rdgBankAdvance = new CButtonGroup();
        rdgBankAdvance.add(rdoBankPay_Yes);
        rdgBankAdvance.add(rdoBankPay_No);
    }
    private void changeRdoBtnToBold() {
        if (observable.getRdoPrizedMember_Yes() == true) {
            lblPrizedBlueClr_Yes.setForeground(Color.BLUE);
            lblPrizedBlueClr_Yes.setFont(new java.awt.Font("MS Sans Serif", Font.BOLD, 13));
        } else {
            lblPrizedBlueClr_Yes.setForeground(Color.BLACK);
            lblPrizedBlueClr_Yes.setFont(new java.awt.Font("MS Sans Serif", Font.PLAIN, 13));
        }
        if (observable.getRdoPrizedMember_No() == true) {
            lblPrizedRedClr_No.setForeground(Color.RED);
            lblPrizedRedClr_No.setFont(new java.awt.Font("MS Sans Serif", Font.BOLD, 13));
        } else {
            lblPrizedRedClr_No.setForeground(Color.BLACK);
            lblPrizedRedClr_No.setFont(new java.awt.Font("MS Sans Serif", Font.PLAIN, 13));
        }
    }
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtSchemeName(txtSchemeName.getText());
        observable.setTxtChittalNo(txtChittalNo.getText());
        observable.setLblMemberNameVal(lblMemberNameVal.getText());
        observable.setTxtDivisionNo(txtDivisionNo.getText());
        observable.setTdtChitStartDt(tdtChitStartDt.getDateValue());
        observable.setTdtChitEndDt(tdtInstDt.getDateValue());
        observable.setTxtNoOfInst(txtNoOfInst.getText());
        observable.setTxtSubNo(txtSubNo.getText());
        observable.setTxtCurrentInstNo(txtCurrentInstNo.getText());
        observable.setTxtInstAmt(txtInstAmt.getText());
        observable.setTxtPendingInst(txtPendingInst.getText());
        observable.setTxtTotalInstAmt(txtTotalInstAmt.getText());
        observable.setTxtBonusAmtAvail(txtBonusAmtAvail.getText());
        observable.setRdoPrizedMember_Yes(rdoPrizedMember_Yes.isSelected());
        observable.setRdoPrizedMember_No(rdoPrizedMember_No.isSelected());
        observable.setRdoBankPay_Yes(rdoBankPay_Yes.isSelected());
        observable.setRdoBankPay_No(rdoBankPay_No.isSelected());
        observable.setTxtNoticeAmt(txtNoticeAmt.getText());
        observable.setTxtAribitrationAmt(txtAribitrationAmt.getText());
        observable.setTxtNoOfInstToPaay(txtNoOfInstToPaay.getText());
        observable.setTxtInstPayable(txtInstPayable.getText());
        observable.setTxtPenalAmtPayable(txtPenalAmtPayable.getText());
        observable.setTxtBonusAmt(txtBonusAmt.getText());
        observable.setTxtForfeitBonus(txtForfeitBonus.getText()); //16-07-2020
        observable.setBankAdvAmt(observable.getBankAdvAmt());
        observable.setTxtDiscountAmt(txtDiscountAmt.getText());
        //        observable.setTxtInterest(txtInterest.getText());
        observable.setTxtNetAmtPaid(txtNetAmtPaid.getText());
        
        observable.setTdtPaidDate(tdtPaidDate.getDateValue());
        observable.setTxtPaidInst(txtPaidInst.getText());
        observable.setTxtPaidAmt(txtPaidAmt.getText());
        observable.setChkThalayal(chkThalayal.isSelected());
        observable.setChkMunnal(chkMunnal.isSelected());
        observable.setChkMemberChanged(chkMemberChanged.isSelected());
        observable.setTxtEarlierMember(txtEarlierMember.getText());
        observable.setTxtEarlierMemberName(txtEarlierMemberName.getText());
        observable.setTxtChangedInst(txtChangedInst.getText());
        observable.setTdtChangedDate(tdtChangedDate.getDateValue());
        String narration = "";
        //java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
        int paidInstallments = CommonUtil.convertObjToInt(txtPaidInstallments.getText());
        int noInstPay = CommonUtil.convertObjToInt(txtNoOfInstToPaay.getText());
        /*if (noInstPay == 1) {
            narration = "Inst#"+(paidInstallments+1);
            //Date dt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtInsUptoPaid.getDateValue()), 30);
             Date dt1=DateUtil.addDays(DateUtil.getDateMMDDYYYY(observable.getTdtChitStartDt()), 30 *paidInstallments);
             narration+=" "+sdf.format(dt1);
        } else if (noInstPay > 1) {
               narration = "Inst#"+(paidInstallments+1);
               narration+="-"+(paidInstallments+noInstPay);
              //  Date dt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtInsUptoPaid.getDateValue()), 30);
                Date dt1=DateUtil.addDays(DateUtil.getDateMMDDYYYY(observable.getTdtChitStartDt()), 30 * paidInstallments);
                narration+=" "+sdf.format(dt1);
              //  dt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtInsUptoPaid.getDateValue()), 30*noInstPay);
                dt1 = DateUtil.addDays(dt1, 30 * (noInstPay-1));
                narration+=" To "+sdf.format(dt1);
        }
        System.out.println("#$#$# narration :"+narration);*/
      
        narration = getMDSNattarion(paidInstallments, noInstPay);
        System.out.println("new narration :: " + narration);

        observable.setNarration(narration);
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setServiceTax_Map(serviceTax_Map);
    }
    
     private String getMDSNattarion(int paidInstallments,int noInstPay){
        String narration = "";
        HashMap whereMap = new HashMap();
         whereMap.put("SCHEME", txtSchemeName.getText());
         whereMap.put("DIVNO", CommonUtil.convertObjToInt(txtDivisionNo.getText()));
         whereMap.put("INST_TO_PAY", noInstPay);
         whereMap.put("INST_PAID", paidInstallments);
        List narrationList = ClientUtil.executeQuery("getMDSNarration", whereMap);
        if (narrationList != null && narrationList.size() > 0) {
                whereMap = (HashMap) narrationList.get(0);
                narration += CommonUtil.convertObjToStr(whereMap.get("NARRATION"));
        }
        return narration;        
    }
    
    /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        MDSClosedReciptMRB objMandatoryRB = new MDSClosedReciptMRB();
        txtNetAmtPaid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetAmtPaid"));
        //        txtInterest.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterest"));
        txtDiscountAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiscountAmt"));
        txtBonusAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBonusAmt"));
        txtPenalAmtPayable.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalAmtPayable"));
        txtInstPayable.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstPayable"));
        txtNoOfInstToPaay.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfInstToPaay"));
        txtTotalInstAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalInstAmt"));
        txtPendingInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPendingInst"));
        txtInstAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInstAmt"));
        txtCurrentInstNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCurrentInstNo"));
        txtNoOfInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfInst"));
        txtSubNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSubNo"));
        tdtChitStartDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChitStartDt"));
        txtBonusAmtAvail.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBonusAmtAvail"));
        txtDivisionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDivisionNo"));
        txtSchemeName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSchemeName"));
        txtChittalNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNo"));
        txtNoticeAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoticeAmt"));
        txtAribitrationAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAribitrationAmt"));
        txtEarlierMember.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEarlierMember"));
        txtChangedInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChangedInst"));
        txtEarlierMemberName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEarlierMemberName"));
        tdtChangedDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtChangedDate"));
        txtPaidAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaidAmt"));
        txtPaidInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPaidInst"));
        tdtPaidDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPaidDate"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgPrizeMember = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBankAdvance = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace41 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace42 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace43 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace44 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace45 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabRemittanceProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        panInsideGeneralRemittance1 = new com.see.truetransact.uicomponent.CPanel();
        panInsideGeneralRemittance3 = new com.see.truetransact.uicomponent.CPanel();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCurrentInstNo = new com.see.truetransact.uicomponent.CLabel();
        txtCurrentInstNo = new com.see.truetransact.uicomponent.CTextField();
        lblPaidInstallments = new com.see.truetransact.uicomponent.CLabel();
        txtPaidInstallments = new com.see.truetransact.uicomponent.CTextField();
        lblPendingInst = new com.see.truetransact.uicomponent.CLabel();
        txtPendingInst = new com.see.truetransact.uicomponent.CTextField();
        panSchemeNameDetails = new com.see.truetransact.uicomponent.CPanel();
        lblSchemeName = new com.see.truetransact.uicomponent.CLabel();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtSchemeName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        panChittalNo = new com.see.truetransact.uicomponent.CPanel();
        txtChittalNo = new com.see.truetransact.uicomponent.CTextField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        btnViewLedger = new com.see.truetransact.uicomponent.CButton();
        txtSubNo = new com.see.truetransact.uicomponent.CTextField();
        lblSchemeDescription = new com.see.truetransact.uicomponent.CLabel();
        lblMDSDescription = new com.see.truetransact.uicomponent.CLabel();
        lblbranch = new com.see.truetransact.uicomponent.CLabel();
        cbobranch = new com.see.truetransact.uicomponent.CComboBox();
        panPendingInstallmentDetails = new com.see.truetransact.uicomponent.CPanel();
        lblChitStartDt1 = new com.see.truetransact.uicomponent.CLabel();
        tdtInstDt = new com.see.truetransact.uicomponent.CDateField();
        lblInsUptoPaid = new com.see.truetransact.uicomponent.CLabel();
        tdtInsUptoPaid = new com.see.truetransact.uicomponent.CDateField();
        lblTotalInstAmt = new com.see.truetransact.uicomponent.CLabel();
        txtTotalInstAmt = new com.see.truetransact.uicomponent.CTextField();
        panInstallmentToPay = new com.see.truetransact.uicomponent.CPanel();
        btnNoOfInstToPaay = new com.see.truetransact.uicomponent.CButton();
        txtNoOfInstToPaay = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfInstToPaay = new com.see.truetransact.uicomponent.CLabel();
        btnWaive = new com.see.truetransact.uicomponent.CButton();
        panInsDetails = new com.see.truetransact.uicomponent.CPanel();
        lblInstPayable = new com.see.truetransact.uicomponent.CLabel();
        txtInstPayable = new com.see.truetransact.uicomponent.CTextField();
        lblPenalAmtPayable = new com.see.truetransact.uicomponent.CLabel();
        txtPenalAmtPayable = new com.see.truetransact.uicomponent.CTextField();
        lblNoticeAmt = new com.see.truetransact.uicomponent.CLabel();
        txtNoticeAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAribitrationAmt = new com.see.truetransact.uicomponent.CLabel();
        txtAribitrationAmt = new com.see.truetransact.uicomponent.CTextField();
        lblNetAmtPaid = new com.see.truetransact.uicomponent.CLabel();
        txtNetAmtPaid = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        panMemberNo_Name = new com.see.truetransact.uicomponent.CPanel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        panInsideGeneralRemittance2 = new com.see.truetransact.uicomponent.CPanel();
        panDiscountPeriodDetails1 = new com.see.truetransact.uicomponent.CPanel();
        lblStreet2 = new com.see.truetransact.uicomponent.CLabel();
        lblValStreet = new com.see.truetransact.uicomponent.CLabel();
        lblValArea = new com.see.truetransact.uicomponent.CLabel();
        lblArea2 = new com.see.truetransact.uicomponent.CLabel();
        lblPin2 = new com.see.truetransact.uicomponent.CLabel();
        lblValPin = new com.see.truetransact.uicomponent.CLabel();
        lblState2 = new com.see.truetransact.uicomponent.CLabel();
        lblValState = new com.see.truetransact.uicomponent.CLabel();
        lblValCity = new com.see.truetransact.uicomponent.CLabel();
        lblCity2 = new com.see.truetransact.uicomponent.CLabel();
        panDiscountPeriodDetails2 = new com.see.truetransact.uicomponent.CPanel();
        lblPrizedMember1 = new com.see.truetransact.uicomponent.CLabel();
        panPrizedMember1 = new com.see.truetransact.uicomponent.CPanel();
        rdoBankPay_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBankPay_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblTrnDate = new com.see.truetransact.uicomponent.CLabel();
        tdtTrnDate = new com.see.truetransact.uicomponent.CDateField();
        panChitDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPrizedMember = new com.see.truetransact.uicomponent.CLabel();
        lblInstAmt = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfInst = new com.see.truetransact.uicomponent.CLabel();
        lblChitStartDt = new com.see.truetransact.uicomponent.CLabel();
        txtInstAmt = new com.see.truetransact.uicomponent.CTextField();
        txtNoOfInst = new com.see.truetransact.uicomponent.CTextField();
        tdtChitStartDt = new com.see.truetransact.uicomponent.CDateField();
        panPrizedMember = new com.see.truetransact.uicomponent.CPanel();
        rdoPrizedMember_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPrizedMember_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPrizedBlueClr_Yes = new com.see.truetransact.uicomponent.CLabel();
        lblPrizedRedClr_No = new com.see.truetransact.uicomponent.CLabel();
        lblBonusAmtAvail = new com.see.truetransact.uicomponent.CLabel();
        txtBonusAmtAvail = new com.see.truetransact.uicomponent.CTextField();
        txtDivisionNo = new com.see.truetransact.uicomponent.CTextField();
        lblDivisionNo = new com.see.truetransact.uicomponent.CLabel();
        txtDiscountAmt = new com.see.truetransact.uicomponent.CTextField();
        txtBonusAmt = new com.see.truetransact.uicomponent.CTextField();
        lblDiscountAmt = new com.see.truetransact.uicomponent.CLabel();
        lblBonusAmt = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtForfeitBonus = new com.see.truetransact.uicomponent.CTextField();
        panGeneralRemittance1 = new com.see.truetransact.uicomponent.CPanel();
        panInsideGeneralRemittance4 = new com.see.truetransact.uicomponent.CPanel();
        panTransactionDetails = new com.see.truetransact.uicomponent.CPanel();
        panTransactionTable = new com.see.truetransact.uicomponent.CPanel();
        srpTransactionTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblTransactionTable = new com.see.truetransact.uicomponent.CTable();
        panInsideGeneralRemittance5 = new com.see.truetransact.uicomponent.CPanel();
        txtEarlierMember = new com.see.truetransact.uicomponent.CTextField();
        txtChangedInst = new com.see.truetransact.uicomponent.CTextField();
        txtEarlierMemberName = new com.see.truetransact.uicomponent.CTextField();
        lblChangedInst = new com.see.truetransact.uicomponent.CLabel();
        lblEarlierMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblEarlierMember = new com.see.truetransact.uicomponent.CLabel();
        lblChangedDate = new com.see.truetransact.uicomponent.CLabel();
        lblMemberChanged = new com.see.truetransact.uicomponent.CLabel();
        lblMunnal = new com.see.truetransact.uicomponent.CLabel();
        lblThalayal = new com.see.truetransact.uicomponent.CLabel();
        chkThalayal = new com.see.truetransact.uicomponent.CCheckBox();
        chkMunnal = new com.see.truetransact.uicomponent.CCheckBox();
        chkMemberChanged = new com.see.truetransact.uicomponent.CCheckBox();
        tdtChangedDate = new com.see.truetransact.uicomponent.CDateField();
        panPrizedDetails = new com.see.truetransact.uicomponent.CPanel();
        lblChitEndDt6 = new com.see.truetransact.uicomponent.CLabel();
        lblPaidInst = new com.see.truetransact.uicomponent.CLabel();
        lblPaidDate = new com.see.truetransact.uicomponent.CLabel();
        txtPaidAmt = new com.see.truetransact.uicomponent.CTextField();
        txtPaidInst = new com.see.truetransact.uicomponent.CTextField();
        tdtPaidDate = new com.see.truetransact.uicomponent.CDateField();
        lblPaidAmt = new com.see.truetransact.uicomponent.CLabel();
        panOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        panTable2_SD = new com.see.truetransact.uicomponent.CPanel();
        srpOtherDetailsTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblOtherDetailsTable = new com.see.truetransact.uicomponent.CTable();
        panLoanDetails = new com.see.truetransact.uicomponent.CPanel();
        panLoanDetailsTable = new com.see.truetransact.uicomponent.CPanel();
        srpLoanDetailsTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanDetailsTable = new com.see.truetransact.uicomponent.CTable();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrMain = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptDelete = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setMinimumSize(new java.awt.Dimension(855, 665));
        setPreferredSize(new java.awt.Dimension(855, 665));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace40);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace41.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace41.setText("     ");
        lblSpace41.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace41.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace41);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnDelete);

        lblSpace3.setText("     ");
        tbrAdvances.add(lblSpace3);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnSave);

        lblSpace42.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace42.setText("     ");
        lblSpace42.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace42);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCancel);

        lblSpace4.setText("     ");
        tbrAdvances.add(lblSpace4);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnAuthorize);

        lblSpace43.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace43.setText("     ");
        lblSpace43.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace43);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace44.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace44.setText("     ");
        lblSpace44.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace44);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnReject);

        lblSpace5.setText("     ");
        tbrAdvances.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrAdvances.add(btnPrint);

        lblSpace45.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace45.setText("     ");
        lblSpace45.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace45);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnClose);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(tbrAdvances, gridBagConstraints);

        tabRemittanceProduct.setMinimumSize(new java.awt.Dimension(850, 480));
        tabRemittanceProduct.setPreferredSize(new java.awt.Dimension(850, 480));

        panGeneralRemittance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneralRemittance.setMinimumSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setPreferredSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        panInsideGeneralRemittance1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance1.setMinimumSize(new java.awt.Dimension(850, 225));
        panInsideGeneralRemittance1.setPreferredSize(new java.awt.Dimension(850, 225));
        panInsideGeneralRemittance1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGeneralRemittance.add(panInsideGeneralRemittance1, gridBagConstraints);

        panInsideGeneralRemittance3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance3.setMinimumSize(new java.awt.Dimension(450, 352));
        panInsideGeneralRemittance3.setPreferredSize(new java.awt.Dimension(450, 352));
        panInsideGeneralRemittance3.setLayout(new java.awt.GridBagLayout());

        panMemberDetails.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        panMemberDetails.setMinimumSize(new java.awt.Dimension(190, 70));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(190, 70));
        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        lblCurrentInstNo.setText("Current Ins No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panMemberDetails.add(lblCurrentInstNo, gridBagConstraints);

        txtCurrentInstNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCurrentInstNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panMemberDetails.add(txtCurrentInstNo, gridBagConstraints);

        lblPaidInstallments.setText("Paid Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panMemberDetails.add(lblPaidInstallments, gridBagConstraints);

        txtPaidInstallments.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPaidInstallments.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panMemberDetails.add(txtPaidInstallments, gridBagConstraints);

        lblPendingInst.setText("Pending installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panMemberDetails.add(lblPendingInst, gridBagConstraints);

        txtPendingInst.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPendingInst.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panMemberDetails.add(txtPendingInst, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsideGeneralRemittance3.add(panMemberDetails, gridBagConstraints);

        panSchemeNameDetails.setMinimumSize(new java.awt.Dimension(430, 80));
        panSchemeNameDetails.setPreferredSize(new java.awt.Dimension(430, 80));
        panSchemeNameDetails.setLayout(new java.awt.GridBagLayout());

        lblSchemeName.setText("MDS Scheme Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panSchemeNameDetails.add(lblSchemeName, gridBagConstraints);

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtSchemeName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtSchemeName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtSchemeName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtSchemeNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(txtSchemeName, gridBagConstraints);

        btnSchemeName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnSchemeName.setEnabled(false);
        btnSchemeName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnSchemeName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnSchemeName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSchemeNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(btnSchemeName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 1);
        panSchemeNameDetails.add(panSchemeName, gridBagConstraints);

        lblChittalNo.setText("Chittal No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchemeNameDetails.add(lblChittalNo, gridBagConstraints);

        panChittalNo.setLayout(new java.awt.GridBagLayout());

        txtChittalNo.setMinimumSize(new java.awt.Dimension(120, 21));
        txtChittalNo.setPreferredSize(new java.awt.Dimension(120, 21));
        txtChittalNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChittalNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChittalNo.add(txtChittalNo, gridBagConstraints);

        btnChittalNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChittalNo.setEnabled(false);
        btnChittalNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChittalNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChittalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChittalNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panChittalNo.add(btnChittalNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 1);
        panSchemeNameDetails.add(panChittalNo, gridBagConstraints);

        btnViewLedger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnViewLedger.setText("View Ledger");
        btnViewLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewLedgerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 9, 0, 0);
        panSchemeNameDetails.add(btnViewLedger, gridBagConstraints);

        txtSubNo.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSubNo.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panSchemeNameDetails.add(txtSubNo, gridBagConstraints);

        lblSchemeDescription.setText("Scheme Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panSchemeNameDetails.add(lblSchemeDescription, gridBagConstraints);

        lblMDSDescription.setForeground(new java.awt.Color(0, 51, 204));
        lblMDSDescription.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMDSDescription.setMaximumSize(new java.awt.Dimension(80, 18));
        lblMDSDescription.setMinimumSize(new java.awt.Dimension(80, 18));
        lblMDSDescription.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 1);
        panSchemeNameDetails.add(lblMDSDescription, gridBagConstraints);

        lblbranch.setText("Branch");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panSchemeNameDetails.add(lblbranch, gridBagConstraints);

        cbobranch.setMaximumSize(new java.awt.Dimension(100, 21));
        cbobranch.setMinimumSize(new java.awt.Dimension(100, 21));
        cbobranch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbobranchActionPerformed(evt);
            }
        });
        cbobranch.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbobranchFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 19, 0, 0);
        panSchemeNameDetails.add(cbobranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panInsideGeneralRemittance3.add(panSchemeNameDetails, gridBagConstraints);

        panPendingInstallmentDetails.setMinimumSize(new java.awt.Dimension(242, 70));
        panPendingInstallmentDetails.setPreferredSize(new java.awt.Dimension(242, 70));
        panPendingInstallmentDetails.setLayout(new java.awt.GridBagLayout());

        lblChitStartDt1.setText("Installment Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPendingInstallmentDetails.add(lblChitStartDt1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panPendingInstallmentDetails.add(tdtInstDt, gridBagConstraints);

        lblInsUptoPaid.setText("Ins Paid Upto Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panPendingInstallmentDetails.add(lblInsUptoPaid, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panPendingInstallmentDetails.add(tdtInsUptoPaid, gridBagConstraints);

        lblTotalInstAmt.setText("Total Inst Amount Due");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panPendingInstallmentDetails.add(lblTotalInstAmt, gridBagConstraints);

        txtTotalInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panPendingInstallmentDetails.add(txtTotalInstAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsideGeneralRemittance3.add(panPendingInstallmentDetails, gridBagConstraints);

        panInstallmentToPay.setMinimumSize(new java.awt.Dimension(450, 27));
        panInstallmentToPay.setPreferredSize(new java.awt.Dimension(450, 29));
        panInstallmentToPay.setLayout(new java.awt.GridBagLayout());

        btnNoOfInstToPaay.setText("Calculate");
        btnNoOfInstToPaay.setMaximumSize(new java.awt.Dimension(89, 21));
        btnNoOfInstToPaay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoOfInstToPaayActionPerformed(evt);
            }
        });
        btnNoOfInstToPaay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnNoOfInstToPaayFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInstallmentToPay.add(btnNoOfInstToPaay, gridBagConstraints);

        txtNoOfInstToPaay.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoOfInstToPaay.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNoOfInstToPaay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOfInstToPaayFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInstallmentToPay.add(txtNoOfInstToPaay, gridBagConstraints);

        lblNoOfInstToPaay.setText("No. of installments about to pay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInstallmentToPay.add(lblNoOfInstToPaay, gridBagConstraints);

        btnWaive.setText("Waive");
        btnWaive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWaiveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panInstallmentToPay.add(btnWaive, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsideGeneralRemittance3.add(panInstallmentToPay, gridBagConstraints);

        panInsDetails.setMinimumSize(new java.awt.Dimension(340, 140));
        panInsDetails.setPreferredSize(new java.awt.Dimension(340, 140));
        panInsDetails.setVerifyInputWhenFocusTarget(false);
        panInsDetails.setLayout(new java.awt.GridBagLayout());

        lblInstPayable.setText("Installment Amount payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(lblInstPayable, gridBagConstraints);

        txtInstPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(txtInstPayable, gridBagConstraints);

        lblPenalAmtPayable.setText("Penal Amount payable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(lblPenalAmtPayable, gridBagConstraints);

        txtPenalAmtPayable.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalAmtPayable.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalAmtPayableFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(txtPenalAmtPayable, gridBagConstraints);

        lblNoticeAmt.setText("Notice Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(lblNoticeAmt, gridBagConstraints);

        txtNoticeAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoticeAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(txtNoticeAmt, gridBagConstraints);

        lblAribitrationAmt.setText("Total Case Expense Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(lblAribitrationAmt, gridBagConstraints);

        txtAribitrationAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAribitrationAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAribitrationAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(txtAribitrationAmt, gridBagConstraints);

        lblNetAmtPaid.setText("Net Amount to be paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(lblNetAmtPaid, gridBagConstraints);

        txtNetAmtPaid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panInsDetails.add(txtNetAmtPaid, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panInsDetails.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(90, 21));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(90, 21));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(90, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panInsDetails.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panInsideGeneralRemittance3.add(panInsDetails, gridBagConstraints);

        panMemberNo_Name.setMinimumSize(new java.awt.Dimension(430, 22));
        panMemberNo_Name.setPreferredSize(new java.awt.Dimension(430, 22));
        panMemberNo_Name.setLayout(new java.awt.GridBagLayout());

        lblMemberName.setText("Member No & Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMemberNo_Name.add(lblMemberName, gridBagConstraints);

        lblMemberNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberNameVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMemberNameVal.setMaximumSize(new java.awt.Dimension(80, 18));
        lblMemberNameVal.setMinimumSize(new java.awt.Dimension(190, 18));
        lblMemberNameVal.setPreferredSize(new java.awt.Dimension(190, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMemberNo_Name.add(lblMemberNameVal, gridBagConstraints);

        lblMemberNo.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberNo.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblMemberNo.setMaximumSize(new java.awt.Dimension(80, 18));
        lblMemberNo.setMinimumSize(new java.awt.Dimension(80, 18));
        lblMemberNo.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panMemberNo_Name.add(lblMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panInsideGeneralRemittance3.add(panMemberNo_Name, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 1);
        panGeneralRemittance.add(panInsideGeneralRemittance3, gridBagConstraints);

        panInsideGeneralRemittance2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance2.setMinimumSize(new java.awt.Dimension(410, 345));
        panInsideGeneralRemittance2.setPreferredSize(new java.awt.Dimension(410, 345));
        panInsideGeneralRemittance2.setLayout(new java.awt.GridBagLayout());

        panDiscountPeriodDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder("Address Details"));
        panDiscountPeriodDetails1.setMinimumSize(new java.awt.Dimension(400, 70));
        panDiscountPeriodDetails1.setPreferredSize(new java.awt.Dimension(400, 70));
        panDiscountPeriodDetails1.setLayout(new java.awt.GridBagLayout());

        lblStreet2.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblStreet2, gridBagConstraints);

        lblValStreet.setMinimumSize(new java.awt.Dimension(70, 10));
        lblValStreet.setPreferredSize(new java.awt.Dimension(70, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblValStreet, gridBagConstraints);

        lblValArea.setMinimumSize(new java.awt.Dimension(100, 10));
        lblValArea.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblValArea, gridBagConstraints);

        lblArea2.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblArea2, gridBagConstraints);

        lblPin2.setText("Pin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblPin2, gridBagConstraints);

        lblValPin.setMinimumSize(new java.awt.Dimension(90, 10));
        lblValPin.setPreferredSize(new java.awt.Dimension(90, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblValPin, gridBagConstraints);

        lblState2.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblState2, gridBagConstraints);

        lblValState.setMinimumSize(new java.awt.Dimension(100, 10));
        lblValState.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblValState, gridBagConstraints);

        lblValCity.setText("                     ");
        lblValCity.setMinimumSize(new java.awt.Dimension(100, 10));
        lblValCity.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblValCity, gridBagConstraints);

        lblCity2.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails1.add(lblCity2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance2.add(panDiscountPeriodDetails1, gridBagConstraints);

        panDiscountPeriodDetails2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panDiscountPeriodDetails2.setMinimumSize(new java.awt.Dimension(400, 30));
        panDiscountPeriodDetails2.setPreferredSize(new java.awt.Dimension(400, 30));
        panDiscountPeriodDetails2.setLayout(new java.awt.GridBagLayout());

        lblPrizedMember1.setText("Bank Advance");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(lblPrizedMember1, gridBagConstraints);

        panPrizedMember1.setLayout(new java.awt.GridBagLayout());

        rdoBankPay_Yes.setText("Yes");
        rdoBankPay_Yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoBankPay_Yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoBankPay_Yes.setPreferredSize(new java.awt.Dimension(48, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrizedMember1.add(rdoBankPay_Yes, gridBagConstraints);

        rdoBankPay_No.setText("No");
        rdoBankPay_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoBankPay_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrizedMember1.add(rdoBankPay_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(panPrizedMember1, gridBagConstraints);

        lblTrnDate.setText("Transaction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panDiscountPeriodDetails2.add(lblTrnDate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountPeriodDetails2.add(tdtTrnDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 1, 1, 1);
        panInsideGeneralRemittance2.add(panDiscountPeriodDetails2, gridBagConstraints);

        panChitDetails.setMinimumSize(new java.awt.Dimension(350, 200));
        panChitDetails.setPreferredSize(new java.awt.Dimension(350, 200));
        panChitDetails.setLayout(new java.awt.GridBagLayout());

        lblPrizedMember.setText("Whether Prized Member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 51, 0, 0);
        panChitDetails.add(lblPrizedMember, gridBagConstraints);

        lblInstAmt.setText("Installment Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 80, 0, 0);
        panChitDetails.add(lblInstAmt, gridBagConstraints);

        lblNoOfInst.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoOfInst.setText("No. of installments ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 78, 0, 0);
        panChitDetails.add(lblNoOfInst, gridBagConstraints);

        lblChitStartDt.setText("Chit start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 109, 0, 0);
        panChitDetails.add(lblChitStartDt, gridBagConstraints);

        txtInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        panChitDetails.add(txtInstAmt, gridBagConstraints);

        txtNoOfInst.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoOfInst.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 0);
        panChitDetails.add(txtNoOfInst, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 0, 53);
        panChitDetails.add(tdtChitStartDt, gridBagConstraints);

        panPrizedMember.setLayout(new java.awt.GridBagLayout());

        rdoPrizedMember_Yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoPrizedMember_Yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoPrizedMember_Yes.setPreferredSize(new java.awt.Dimension(48, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPrizedMember.add(rdoPrizedMember_Yes, gridBagConstraints);

        rdoPrizedMember_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoPrizedMember_No.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPrizedMember.add(rdoPrizedMember_No, gridBagConstraints);

        lblPrizedBlueClr_Yes.setText("Yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        panPrizedMember.add(lblPrizedBlueClr_Yes, gridBagConstraints);

        lblPrizedRedClr_No.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 2, 0);
        panPrizedMember.add(lblPrizedRedClr_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        panChitDetails.add(panPrizedMember, gridBagConstraints);

        lblBonusAmtAvail.setText("Last Auction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 70, 0, 0);
        panChitDetails.add(lblBonusAmtAvail, gridBagConstraints);

        txtBonusAmtAvail.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBonusAmtAvail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusAmtAvailFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 3, 6, 0);
        panChitDetails.add(txtBonusAmtAvail, gridBagConstraints);

        txtDivisionNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDivisionNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 0, 0);
        panChitDetails.add(txtDivisionNo, gridBagConstraints);

        lblDivisionNo.setText("Division No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 129, 0, 0);
        panChitDetails.add(lblDivisionNo, gridBagConstraints);

        txtDiscountAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panChitDetails.add(txtDiscountAmt, gridBagConstraints);

        txtBonusAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 0, 0);
        panChitDetails.add(txtBonusAmt, gridBagConstraints);

        lblDiscountAmt.setText("Discount Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 93, 0, 0);
        panChitDetails.add(lblDiscountAmt, gridBagConstraints);

        lblBonusAmt.setText("Bonus Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 107, 0, 0);
        panChitDetails.add(lblBonusAmt, gridBagConstraints);

        cLabel1.setText("Fofiet Bonus");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        panChitDetails.add(cLabel1, gridBagConstraints);

        txtForfeitBonus.setAllowNumber(true);
        txtForfeitBonus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        panChitDetails.add(txtForfeitBonus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        panInsideGeneralRemittance2.add(panChitDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panGeneralRemittance.add(panInsideGeneralRemittance2, gridBagConstraints);

        tabRemittanceProduct.addTab("Receipt Entry Details", panGeneralRemittance);

        panGeneralRemittance1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneralRemittance1.setMinimumSize(new java.awt.Dimension(570, 450));
        panGeneralRemittance1.setPreferredSize(new java.awt.Dimension(570, 450));
        panGeneralRemittance1.setLayout(new java.awt.GridBagLayout());

        panInsideGeneralRemittance4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance4.setMinimumSize(new java.awt.Dimension(850, 575));
        panInsideGeneralRemittance4.setPreferredSize(new java.awt.Dimension(850, 575));
        panInsideGeneralRemittance4.setLayout(new java.awt.GridBagLayout());

        panTransactionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Transaction Details"));
        panTransactionDetails.setMinimumSize(new java.awt.Dimension(400, 120));
        panTransactionDetails.setPreferredSize(new java.awt.Dimension(400, 120));
        panTransactionDetails.setLayout(new java.awt.GridBagLayout());

        panTransactionTable.setMinimumSize(new java.awt.Dimension(270, 340));
        panTransactionTable.setPreferredSize(new java.awt.Dimension(270, 340));
        panTransactionTable.setLayout(new java.awt.GridBagLayout());

        srpTransactionTable.setMinimumSize(new java.awt.Dimension(260, 340));
        srpTransactionTable.setPreferredSize(new java.awt.Dimension(260, 340));

        tblTransactionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MDS Scheme name", "Installment no", "Transaction id", "Transaction Date", "Amount paid"
            }
        ));
        tblTransactionTable.setPreferredSize(new java.awt.Dimension(75, 0));
        srpTransactionTable.setViewportView(tblTransactionTable);

        panTransactionTable.add(srpTransactionTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransactionDetails.add(panTransactionTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panTransactionDetails, gridBagConstraints);

        panInsideGeneralRemittance5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance5.setMinimumSize(new java.awt.Dimension(400, 200));
        panInsideGeneralRemittance5.setPreferredSize(new java.awt.Dimension(400, 200));
        panInsideGeneralRemittance5.setLayout(new java.awt.GridBagLayout());

        txtEarlierMember.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(txtEarlierMember, gridBagConstraints);

        txtChangedInst.setMinimumSize(new java.awt.Dimension(50, 21));
        txtChangedInst.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(txtChangedInst, gridBagConstraints);

        txtEarlierMemberName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(txtEarlierMemberName, gridBagConstraints);

        lblChangedInst.setText("Changed from Installment no ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(lblChangedInst, gridBagConstraints);

        lblEarlierMemberName.setText("Earlier Member name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(lblEarlierMemberName, gridBagConstraints);

        lblEarlierMember.setText("Earlier Member no");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(lblEarlierMember, gridBagConstraints);

        lblChangedDate.setText("Changed Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(lblChangedDate, gridBagConstraints);

        lblMemberChanged.setText("Whether the member changed in between");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(lblMemberChanged, gridBagConstraints);

        lblMunnal.setText("Whether the member is a Munnal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(lblMunnal, gridBagConstraints);

        lblThalayal.setText("Whether the member is a Thalayal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(lblThalayal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideGeneralRemittance5.add(chkThalayal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideGeneralRemittance5.add(chkMunnal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideGeneralRemittance5.add(chkMemberChanged, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideGeneralRemittance5.add(tdtChangedDate, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panInsideGeneralRemittance5, gridBagConstraints);

        panPrizedDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Prized  Details"));
        panPrizedDetails.setMinimumSize(new java.awt.Dimension(400, 150));
        panPrizedDetails.setPreferredSize(new java.awt.Dimension(400, 150));
        panPrizedDetails.setLayout(new java.awt.GridBagLayout());

        lblChitEndDt6.setText("Security Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPrizedDetails.add(lblChitEndDt6, gridBagConstraints);

        lblPaidInst.setText("Paid Installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPrizedDetails.add(lblPaidInst, gridBagConstraints);

        lblPaidDate.setText("Paid Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPrizedDetails.add(lblPaidDate, gridBagConstraints);

        txtPaidAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedDetails.add(txtPaidAmt, gridBagConstraints);

        txtPaidInst.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedDetails.add(txtPaidInst, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPrizedDetails.add(tdtPaidDate, gridBagConstraints);

        lblPaidAmt.setText("Paid Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panPrizedDetails.add(lblPaidAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panPrizedDetails, gridBagConstraints);

        panOtherDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Other MDS details"));
        panOtherDetails.setMinimumSize(new java.awt.Dimension(400, 120));
        panOtherDetails.setPreferredSize(new java.awt.Dimension(400, 120));
        panOtherDetails.setLayout(new java.awt.GridBagLayout());

        panTable2_SD.setMinimumSize(new java.awt.Dimension(270, 340));
        panTable2_SD.setPreferredSize(new java.awt.Dimension(270, 340));
        panTable2_SD.setLayout(new java.awt.GridBagLayout());

        srpOtherDetailsTable.setMinimumSize(new java.awt.Dimension(260, 340));
        srpOtherDetailsTable.setPreferredSize(new java.awt.Dimension(260, 340));

        tblOtherDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MDS Scheme  name", "Installment Amount", "Installments Paid", "Installment overdues", "Overdue  Amount"
            }
        ));
        tblOtherDetailsTable.setPreferredSize(new java.awt.Dimension(75, 0));
        srpOtherDetailsTable.setViewportView(tblOtherDetailsTable);

        panTable2_SD.add(srpOtherDetailsTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDetails.add(panTable2_SD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panOtherDetails, gridBagConstraints);

        panLoanDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("MDS Loan details"));
        panLoanDetails.setMinimumSize(new java.awt.Dimension(400, 120));
        panLoanDetails.setPreferredSize(new java.awt.Dimension(400, 120));
        panLoanDetails.setLayout(new java.awt.GridBagLayout());

        panLoanDetailsTable.setMinimumSize(new java.awt.Dimension(270, 340));
        panLoanDetailsTable.setPreferredSize(new java.awt.Dimension(270, 340));
        panLoanDetailsTable.setLayout(new java.awt.GridBagLayout());

        srpLoanDetailsTable.setMinimumSize(new java.awt.Dimension(260, 340));
        srpLoanDetailsTable.setPreferredSize(new java.awt.Dimension(260, 340));

        tblLoanDetailsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MDS Loan no", "Loan  date", "Loan amount", "outstanding amount", "Overdue amount"
            }
        ));
        tblLoanDetailsTable.setPreferredSize(new java.awt.Dimension(75, 0));
        srpLoanDetailsTable.setViewportView(tblLoanDetailsTable);

        panLoanDetailsTable.add(srpLoanDetailsTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLoanDetails.add(panLoanDetailsTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideGeneralRemittance4.add(panLoanDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance1.add(panInsideGeneralRemittance4, gridBagConstraints);

        tabRemittanceProduct.addTab("Other Details", panGeneralRemittance1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabRemittanceProduct, gridBagConstraints);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        getContentPane().add(panStatus, gridBagConstraints);

        mnuProcess.setText("Process");

        mitNew.setText("New");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);
        mnuProcess.add(sptDelete);

        mitSave.setText("Save");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mnuProcess.add(mitAuthorize);

        mitReject.setText("Rejection");
        mnuProcess.add(mitReject);

        mitException.setText("Exception");
        mnuProcess.add(mitException);
        mnuProcess.add(sptException);

        mitPrint.setText("Print");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewLedgerActionPerformed
        // TODO add your handling code here:
        if(txtChittalNo.getText().length()>0){
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("ChittalNo", txtChittalNo.getText());
            paramMap.put("SubNo", txtSubNo.getText());
            paramMap.put("FromDt", curr_dt);
            paramMap.put("ToDt", curr_dt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            ttIntgration.integration("MDSLedger");
        }else{
            ClientUtil.displayAlert("Chittal No Should Not Be Empty!!! ");
        }
    }//GEN-LAST:event_btnViewLedgerActionPerformed

    private void txtChittalNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChittalNoFocusLost
        // TODO add your handling code here:
        if(txtChittalNo.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME",txtSchemeName.getText());
            whereMap.put("CHITTAL_NUMBER",txtChittalNo.getText());
            List lst=ClientUtil.executeQuery("getSelectChittalReceiptEntryDetails",whereMap);
            System.out.println("lstlstlstlstlst"+lst);
            if(lst !=null && lst.size()>0){
                viewType = "CHITTAL_NO";
                whereMap=(HashMap)lst.get(0);
                fillData(whereMap);
                lst=null;
                whereMap=null;
            }else{
                ClientUtil.displayAlert("Invalid Chittal No !!! ");                
                txtChittalNo.setText("");
            }
        } 
        setEnableDisable();
    }//GEN-LAST:event_txtChittalNoFocusLost

    private void txtSchemeNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtSchemeNameFocusLost
        // TODO add your handling code here:
        if(txtSchemeName.getText().length()>0){
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME",txtSchemeName.getText());
            List lst=ClientUtil.executeQuery("getSelectSchemeName",whereMap);
            if(lst !=null && lst.size()>0){
                observable.setTxtSchemeName(txtSchemeName.getText());
                viewType = "SCHEME_DETAILS";
                whereMap=(HashMap)lst.get(0);
                fillData(whereMap);
                lst=null;
                whereMap=null;
            }else{
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtSchemeName.setText("");
                lblMDSDescription.setText("");
            }
        }        
    }//GEN-LAST:event_txtSchemeNameFocusLost
    
    
    private void processPenalAmount() {
        for (int i = 0; i < penalRealList.size(); i++) {
            penalRealList.set(i, 0);
        }
        splitTransMap.put("PENAL_AMT_LIST", penalRealList);
        splitTransMap.put("IS_SPLIT_MDS_TRANSACTION", isSplitMDSTransaction);
        observable.setMDSClosedTransListMap(splitTransMap);
    }

    private void editPenalListAmount(double penalAmt) {
        ArrayList newPenalList = null;
        ListIterator penalIterate = null;
        penalIterate = penalRealList.listIterator();
        ArrayList copyLists = new ArrayList();
        double amountCount = 0.0;
        double difference = 0.0;
        double amt = 0.0;
        double lastAmt =0.0;
        double newAmt = 0.0;
        double org = 0.0;
        double addedPenal = 0.0;
        newPenalList = new ArrayList();
        newPenalList.clear();
        for (int i = 0; i < copyList.size(); i++) {
            amt = CommonUtil.convertObjToDouble(copyList.get(i));
            amountCount = amountCount + amt;
            if (amountCount >= penalAmt) {
                difference = amountCount - penalAmt;
                lastAmt = CommonUtil.convertObjToDouble(copyList.get(i));
                break;
            } else {
                newAmt += amt;
                
                if (i == copyList.size() - 1) {  //KD-3661 - MDS INTEREST DIFFERENCE IN VOUCHER & DAY BOOK
                    if (penalAmt > penalAmtWithoutEdit) {
                        System.out.println("Executing if penal changed to higher amount");
                        addedPenal = penalAmt - penalAmtWithoutEdit;
                        amt += addedPenal;
                    }
                }
                newPenalList.add(amt);
            }
        }
        newAmt += difference;
        if (newAmt > penalAmt) {
            org = newAmt - penalAmt;
        } else {
            org = penalAmt - newAmt;
        }
        double amount = lastAmt - difference;
            newPenalList.add(amount);
        if (penalRealList.size() != newPenalList.size()) {
            int cnt = penalRealList.size() - newPenalList.size();
            for (int i = 0; i < cnt; i++) {
                newPenalList.add(0);
            }
        }
        copyLists.clear();
        copyLists.addAll(newPenalList);
        penalRealList.addAll(newPenalList);
        splitTransMap.put("PENAL_AMT_LIST", copyLists);
        splitTransMap.put("IS_SPLIT_MDS_TRANSACTION", isSplitMDSTransaction);
        observable.setMDSClosedTransListMap(splitTransMap);
    }
    private void calculatePenalRate(double penalAmt) {
        if (penalAmtWithoutEdit != penalAmt && penalAmt != 0) {
                editPenalListAmount(penalAmt);
        } else if (penalAmt == 0) {
                processPenalAmount();
            }
        }
    
    private void txtPenalAmtPayableFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalAmtPayableFocusLost
        // TODO add your handling code here:
        // added by rishad for tolerance checking 25/10/2019
         String tolerance_amt = CommonUtil.convertObjToStr(CommonConstants.TOLERANCE_AMT);
        if (tolerance_amt.length() == 0) {
            ClientUtil.displayAlert("Please Add Tolerance Property in  TT property");
            return;
        }
        if (txtPenalAmtPayable.getText().length() == 0) {     // Copied this block from receipt entry  by nithya 25-02-2022 for avoiding  issue when clearing penal amount field 
            txtPenalAmtPayable.setText("0");
        }
        String selectedAmt = txtPenalAmtPayable.getText();
        double calcAmt = CommonUtil.convertObjToDouble(observable.getTxtGlobalPenalAmt());
        double tolerance_amt_value = CommonUtil.convertObjToDouble(tolerance_amt);
        double selected_amt = CommonUtil.convertObjToDouble(selectedAmt);

        double minTolerance = calcAmt - tolerance_amt_value;
        if (minTolerance < 0) {
            minTolerance = 0;
        }
        double maxTolerance = calcAmt + tolerance_amt_value;
    
        if (minTolerance > selected_amt || maxTolerance < selected_amt) {
            ClientUtil.showMessageWindow("Minimum Changing Amount should be greater than or equal to  Rs. " + minTolerance + "  &  " + "Maximum Changing Amount should be less than or equal to Rs. " + maxTolerance);
            txtPenalAmtPayable.setText(CommonUtil.convertObjToStr(observable.getTxtGlobalPenalAmt()));
            return;
        }
        if(txtPenalAmtPayable.getText().length()>0){
            isPenalEdit = false;
            setCaseExpensesAmount();
            setNoticeChargeAmount();
            txtNetAmtPaid.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInstPayable.getText()).doubleValue() //-
            //CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue() -
            //CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() 
            +
            CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText()).doubleValue()+
            CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue() +
            CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()+ 
            CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingAmount(txtNetAmtPaid.getText());
            if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                double penalAmount = CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText());
                calculatePenalRate(penalAmount);
            }
        }
    }//GEN-LAST:event_txtPenalAmtPayableFocusLost
    
    private void txtBonusAmtAvailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusAmtAvailFocusLost
        // TODO add your handling code here:
        if(txtBonusAmtAvail.getText().length()>0){
            txtNetAmtPaid.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInstPayable.getText()).doubleValue() //-
            //CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue() -
            //CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() 
            +
            CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText()).doubleValue()+
            CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue() +
            CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()+ 
            CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingAmount(txtNetAmtPaid.getText());
        }
    }//GEN-LAST:event_txtBonusAmtAvailFocusLost
    
    private void txtAribitrationAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAribitrationAmtFocusLost
        // TODO add your handling code here:
        if(txtAribitrationAmt.getText().length()>0){
            txtNetAmtPaid.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInstPayable.getText()).doubleValue() //-
            //CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue() -
            //CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() 
            +
            CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText()).doubleValue()+
            CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue() +
            CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()+ 
            CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingAmount(txtNetAmtPaid.getText());
        }
    }//GEN-LAST:event_txtAribitrationAmtFocusLost
    
    private void txtNoticeAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeAmtFocusLost
        // TODO add your handling code here:
        if(txtNoticeAmt.getText().length()>0){
            setCaseExpensesAmount();
            setNoticeChargeAmount();
            txtNetAmtPaid.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInstPayable.getText()).doubleValue() //-
            //CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue() -
            //CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() 
            +
            CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText()).doubleValue()+
            CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue() +
            CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()+ 
            CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
            transactionUI.setCallingAmount(txtNetAmtPaid.getText());
        }
    }//GEN-LAST:event_txtNoticeAmtFocusLost
   
    private HashMap checkServiceTaxApplicable (String chargeType) {// Modifying the function by nithya for GST
        String checkFlag = "N";
        HashMap checkForTaxMap = new HashMap();
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y") && chargeType != null && chargeType.length() > 0) {
            HashMap applicationMap = new HashMap();
            String accId = "";
            applicationMap.put("SCHEME_NAME", txtSchemeName.getText().trim());
            List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", applicationMap);
            if (lst != null && lst.size() > 0) {
                applicationMap = (HashMap) lst.get(0);
            }
            if (applicationMap != null) {
                if (chargeType.equals("ARC_COST")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("ARC_COST"));
                } else if (chargeType.equals("ARC Expense")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("ARC_EXPENSE"));
                } else if (chargeType.equals("EA Cost")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("EA_COST"));
                } else if (chargeType.equals("EA Expense")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("EA_EXPENSE"));
                } else if (chargeType.equals("EP_COST")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("EP_COST"));
                } else if (chargeType.equals("EP Expense")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("EP_EXPENSE"));
                } else if (chargeType.equals("ARBITRARY CHARGES")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("ARC_COST"));
                } else if (chargeType.equals("EXECUTION DECREE CHARGES")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("EP_COST"));
                } else if (chargeType.equals("MISCELLANEOUS CHARGES")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("MISCELLANEOUS_CHARGES"));
                } else if (chargeType.equals("OTHER CHARGES")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("OTHER_CHARGES"));
                } else if (chargeType.equals("LEGAL CHARGES")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("LEGAL_CHARGES"));
                } else if (chargeType.equals("INSURANCE CHARGES")) {
                    accId = CommonUtil.convertObjToStr(applicationMap.get("INSURANCE_CHARGES"));
                }
                HashMap whereMap = new HashMap();
        whereMap.put("AC_HD_ID", accId);
        List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
        if (accHeadList != null && accHeadList.size() > 0) {
            HashMap accHeadMap = (HashMap) accHeadList.get(0);
            if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE") && accHeadMap.containsKey("SERVICE_TAX_ID")) {
                checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
            }
        }
       }
        }
        return checkForTaxMap;
    }
    private void setCaseExpensesAmount(){
        HashMap whereMap = new HashMap();
        double chargeAmount =0.0,totTaxAmt=0.00;
        double totChrgAmt = 0.0;
        HashMap checkForTaxMap = new HashMap();
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        whereMap.put("ACT_NUM",txtChittalNo.getText()+"_"+txtSubNo.getText());
        List chargeList = ClientUtil.executeQuery("getMDSCaseChargeDetails", whereMap);
        if(chargeList!=null && chargeList.size()>0){
//            for(int i=0;i<chargeList.size();i++){
//                whereMap=(HashMap) chargeList.get(i);
//                chargeAmount+=CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
//                String chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
//               String serFlag = checkServiceTaxApplicable(chargeType);
//               if(serFlag!=null && serFlag.equals("Y")){
//                   totTaxAmt=totTaxAmt+CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT"));
//               }
//            }
            for(int i=0;i<chargeList.size();i++){
                chargeAmount = 0.0;
                whereMap = (HashMap) chargeList.get(i);
                totChrgAmt+=CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                chargeAmount = CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
                String chargeType = CommonUtil.convertObjToStr(whereMap.get("CHARGE_TYPE"));
                checkForTaxMap = checkServiceTaxApplicable(chargeType);
                if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                    if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                        if (chargeAmount > 0) {
                            taxMap = new HashMap();
                            taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                            taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, chargeAmount);
                            taxSettingsList.add(taxMap);
                        }
                    }
                }
            }
            
            Rounding rod =new Rounding();
            totChrgAmt = (double)rod.getNearest((long)(totChrgAmt *100),100)/100;
			//added by chithra for service Tax
            //if(totTaxAmt>0){
            if(taxSettingsList != null && taxSettingsList.size() > 0){
                HashMap ser_Tax_Val = new HashMap();
                ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, curr_dt);
                ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, totTaxAmt);
                ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
                try {
                    objServiceTax = new ServiceTaxCalculation();
                    serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                    if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                        String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                        
//                        lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
//                        observable.setLblServiceTaxval(lblServiceTaxval.getText());
//                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                        
                        lblServiceTaxval.setText(amt);
                        observable.setLblServiceTaxval(lblServiceTaxval.getText());
                        serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    } else {
                        lblServiceTaxval.setText("0.00");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
             txtAribitrationAmt.setText(String.valueOf(totChrgAmt));
        }
        chargeList = null;
    }
    private void setNoticeChargeAmount(){
        HashMap whereMap = new HashMap();
        double chargeAmount =0.0;
        whereMap.put("ACT_NUM",txtChittalNo.getText()+"_"+txtSubNo.getText());
        List chargeList = ClientUtil.executeQuery("getMDSNoticeChargeDetails", whereMap);
        if(chargeList!=null && chargeList.size()>0){
            for(int i=0;i<chargeList.size();i++){
                whereMap=(HashMap) chargeList.get(i);
                chargeAmount+=CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
            }
            Rounding rod =new Rounding();
            chargeAmount = (double)rod.getNearest((long)(chargeAmount *100),100)/100;
            txtNoticeAmt.setText(String.valueOf(chargeAmount));
        }
        chargeList = null;
    }
    private void btnNoOfInstToPaayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoOfInstToPaayActionPerformed
        // TODO add your handling code here:
        
        int monthlyOrWeeklyScheme = CommonUtil.convertObjToInt(observable.getInstalmntFreq());
        if (monthlyOrWeeklyScheme == 7) {
            isWeeklyOrMonthlyScheme = "W";
        } else if (monthlyOrWeeklyScheme == 30) {
            isWeeklyOrMonthlyScheme = "M";
        }
        if (txtChittalNo.getText().length() == 0) { // Added by nithya for KD-2974
            ClientUtil.showAlertWindow("Chittal No. should not be empty");
            return;
        }
        if(tdtInstDt.getDateValue().length()== 0){
            ClientUtil.showAlertWindow("Enter Installment Date");
            return;
        }else if(CommonUtil.convertObjToDouble(txtNoOfInstToPaay.getText()).doubleValue() == 0){
            ClientUtil.showAlertWindow("Enter No of Installment");
            return;
        }else{
            txtPenalAmtPayable.setEnabled(true);
            txtInstPayable.setText("");
            txtPenalAmtPayable.setText("");
            txtBonusAmt.setText("");
            txtForfeitBonus.setText(""); //2093
            txtDiscountAmt.setText("");
            //            txtInterest.setText("");
            txtNetAmtPaid.setText("");
            observable.setProductMapDetails();
            HashMap productMap = new HashMap();
            productMap = observable.getProductMap();
            bonusAmountList = new ArrayList();
            penalList = new ArrayList();
            noticeList = new ArrayList();
            instAmountList = new ArrayList();
            bonusReversalLsit = new ArrayList(); //16-07-2020
            narrationList = new ArrayList();
            ForfeitebonusAmountList = new ArrayList();
            isSplitMDSTransaction = "";
            // Added by Rajesh For checking BONUS_FIRST_INSTALLMENT. Based on this for loop initial value will be changed for Penal calculation.
//            String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("BONUS_FIRST_INSTALLMENT"));
            String forfeitChk = CommonUtil.convertObjToStr(productMap.get("FORFEITE_HD_Y_N"));
            String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
            isSplitMDSTransaction = CommonUtil.convertObjToStr(productMap.get("IS_SPLIT_MDS_TRANSACTION")); 
            Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
            int startNoForPenal = 0;
            int addNo = 1;
            int firstInst_No = -1;
            if (bonusFirstInst.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                startNoForPenal = 1;
                addNo = 0;
                firstInst_No = 0;
            }
            long diffDayPending = 0;
            int noOfInsPaid = 0;
            Date currDate = (Date) curr_dt.clone();
            Date instDate = null;
            boolean bonusAvailabe = true;
            long currentInst = CommonUtil.convertObjToInt(txtCurrentInstNo.getText());
            long pendingInst = CommonUtil.convertObjToInt(txtPendingInst.getText());
            long noOfInstPay = CommonUtil.convertObjToInt(txtNoOfInstToPaay.getText());
            double insAmt = CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue();
            System.out.println("in uiiiiisa cureent pending and alla tht +"+currentInst+"  "+pendingInst+" "+noOfInstPay+"  "+insAmt);
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO", txtChittalNo.getText());
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));
            List insList = ClientUtil.executeQuery("getNoOfInstalmentsPaid", whereMap);
            if (insList != null && insList.size() > 0) {
                whereMap = (HashMap) insList.get(0);
                noOfInsPaid = CommonUtil.convertObjToInt(whereMap.get("NO_OF_INST"));
            }
            Date currDate1 = null;
            HashMap c_hash1 = new HashMap();
            c_hash1.put("SCHEME_NAME", txtSchemeName.getText().trim());
            List closureList1 = ClientUtil.executeQuery("checkSchemeClosureDetailsForClosed", c_hash1);

            if (closureList1 != null && closureList1.size() > 0) {
                c_hash1 = (HashMap) closureList1.get(0);
                System.out.println(" Value of Scheme Closed Date " + (Date) c_hash1.get("SCHEME_END_DT"));
                //   Date clsDate  = (Date)c_hash.get("SCHEME_CLOSE_DT");
                currDate1 = (Date) c_hash1.get("SCHEME_END_DT");
//                                 Date clsDate  = DateUtil.getDateMMDDYYYY(c_hash.get("SCHEME_END_DT").toString());

                System.out.println("annnn" + currDate1);
                Calendar cal = null;
                cal = Calendar.getInstance();
                cal.setTime(currDate1);
//                                cal.add(Calendar.MONTH, 1);
                currDate1 = cal.getTime();
                System.out.println("cccccssss" + currDate1);
            }
           
           
           
           
            int totIns = CommonUtil.convertObjToInt(txtNoOfInst.getText());
            int balanceIns = totIns - noOfInsPaid;
            long totDiscAmt = 0;
            double totBonusAmt = 0;
            double totReversalBonusAmt = 0.0; //2093
            double bonusAmt= 0;
            HashMap installmentMap = new HashMap();
            System.out.println("balanceIns"+balanceIns+"--sadad--"+(CommonUtil.convertObjToDouble(txtNoOfInstToPaay.getText()).doubleValue()));
            if(balanceIns >= (CommonUtil.convertObjToDouble(txtNoOfInstToPaay.getText()).doubleValue())){
                String penalIntType = "";
                String calculateIntOn = "";
                double penalValue = 0;
                String penalGraceType = "";
                double penalGraceValue = 0;
                String penalCalcBaseOn = "";
                setCaseExpensesAmount();
                setNoticeChargeAmount();               
                if(pendingInst>=0){//pending installment calculation starts... && pendingInst>=noOfInstPay
                    int totPendingInst = (int)pendingInst;
                    System.out.println("totalPending Istallment"+totPendingInst);
                    double calc = 0;
                    long totInst = pendingInst;
                    penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                    if(observable.getRdoPrizedMember_Yes() == true){
                        if (productMap.containsKey("PENEL_PZ_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT") != null) {  // Added by nithya on 16-03-2021 for KD-2712
                            calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_PZ_INT_FULL_AMT_INST_AMT"));
                        }
                        penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                        penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_INT_AMT"));
                        penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                        penalGraceValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                        if (penalGraceType.equals("Installments") && ( penalIntType!=null && penalIntType.equals("Percent"))) {
                            pendingInst -= penalGraceValue;
                        }
                    }else if(observable.getRdoPrizedMember_No() == true){
                        if (productMap.containsKey("PENEL_INT_FULL_AMT_INST_AMT") && productMap.get("PENEL_INT_FULL_AMT_INST_AMT") != null) {  // Added by nithya on 16-03-2021 for KD-2712
                            calculateIntOn = CommonUtil.convertObjToStr(productMap.get("PENEL_INT_FULL_AMT_INST_AMT"));
                        }
                        penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_INT_TYPE"));
                        penalValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_INT_AMT"));
                        penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_GRACE_PERIOD_TYPE"));
                        penalGraceValue = CommonUtil.convertObjToDouble(productMap.get("PENAL_GRACE_PERIOD"));
                        if (penalGraceType.equals("Installments") && ( penalIntType!=null && penalIntType.equals("Percent"))) {
                            pendingInst -= penalGraceValue;
                        }
                    }
            
                    // Added by nithya on 16-03-2021 for KD-2712
                    List bonusAmout = new ArrayList();
                    if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                        HashMap nextInstMaps = null;
                        for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                            nextInstMaps = new HashMap();
                            nextInstMaps.put("SCHEME_NAME", txtSchemeName.getText());
                            nextInstMaps.put("DIVISION_NO", txtDivisionNo.getText());
                            nextInstMaps.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                            List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMaps);
                            if (listRec != null && listRec.size() > 0) {
                                nextInstMaps = (HashMap) listRec.get(0);
                            }
                            if (nextInstMaps != null && nextInstMaps.containsKey("NEXT_BONUS_AMOUNT")) {
                                bonusAmout.add(CommonUtil.convertObjToDouble(nextInstMaps.get("NEXT_BONUS_AMOUNT")));
                            } else {
                                bonusAmout.add(CommonUtil.convertObjToDouble(0));
                            }
                        }
                    }
                    // End
                    //System.out.println("types penalIntType" + penalIntType + " penalValue " + penalValue + " penalGraceType " +  " penalGraceValue " + penalGraceValue);
                    System.out.println("startNoForPenal"+startNoForPenal+"mmm"+noOfInstPay+"..."+(noOfInstPay+startNoForPenal));
                    Rounding rod = new Rounding();
                    int instFreq= CommonUtil.convertObjToInt(observable.getInstalmntFreq());
                    System.out.println("pendingInst@#%@#%"+pendingInst);
                    System.out.println("startNoForPenal@#%@#%"+startNoForPenal);
                    System.out.println("noOfInstPay@#%@#%"+noOfInstPay);
                    
                    
                    long prependingInst = pendingInst;
                    if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                        HashMap nextActMap = new HashMap();
                        nextActMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(txtSchemeName.getText()));
                        nextActMap.put("DIVISION_NO", CommonUtil.convertObjToStr(txtDivisionNo.getText()));
                        nextActMap.put("SL_NO", CommonUtil.convertObjToDouble(txtCurrentInstNo.getText()));
                        List listAuc = ClientUtil.executeQuery("getSelectNextAuctDate", nextActMap);
                        if (listAuc != null && listAuc.size() > 0) {
                            nextActMap = (HashMap) listAuc.get(0);
                        }
                        Date drawAuctDate = null;
                        if (nextActMap.containsKey("DRAW_AUCTION_DATE")) {
                            drawAuctDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                        }
                        System.out.println("DateUtil.dateDiff(drawAuctDate, currDate" + DateUtil.dateDiff(drawAuctDate, currDate));

                        int addingGraceDays = CommonUtil.convertObjToInt(penalGraceValue);
                        System.out.println("addingGraceDays :: " + addingGraceDays);
                        if (DateUtil.dateDiff(DateUtil.addDays(drawAuctDate, addingGraceDays), currDate) <= 0) {
                            prependingInst = prependingInst - 1;
                        }
                    }
                    
                    
                    
                    for(int i = startNoForPenal;i<noOfInstPay+startNoForPenal;i++){
                        System.out.println("iiiiiikkkkkkkk"+i);
                        
                         // Added by nithya on 16-03-2021 for KD-2712                        
                        if (calculateIntOn != null && calculateIntOn.equals("Installment Amount")) {
                            int predefinedIterator = i - 1;
                            insAmt = 0.0;
                            double instAmount = CommonUtil.convertObjToDouble(txtInstAmt.getText());
                            if (bonusAmout != null && bonusAmout.size() > 0) {
                                if (bonusFirstInst.equals("Y") || CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                    instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(predefinedIterator));
                                } else {
                                    instAmount -= CommonUtil.convertObjToDouble(bonusAmout.get(i));
                                }
                            }
                            insAmt = instAmount;
                        }
                        // End
                        
//                        for(int i = 1;i<=noOfInstPay;i++){
                        HashMap nextInstMap = new HashMap();
                        nextInstMap.put("SCHEME_NAME",txtSchemeName.getText());
                        nextInstMap.put("DIVISION_NO",txtDivisionNo.getText());
                        nextInstMap.put("SL_NO",new Double(i+noOfInsPaid));
                        List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                        System.out.println("listRecgggg"+listRec);
                        if(listRec!=null && listRec.size()>0){
                            double penal = 0;
                            nextInstMap = (HashMap)listRec.get(0);
                            System.out.println("nextInstMaplll"+nextInstMap);
                            nextInstMap.put("INST_AMT", txtInstAmt.getText());
                            //Changed by sreekrishnan
                            if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("DRAW_AUCTION_DATE")));
                            }else{
                                instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                            }
                            diffDayPending = DateUtil.dateDiff(instDate,(Date)curr_dt.clone());
                            System.out.println("First ####....####diffDay : "+diffDayPending);
                            //Holiday Checking - Added By Suresh
                            HashMap holidayMap = new HashMap(); 
                            boolean checkHoliday=true;
                            System.out.println("instDate   "+instDate);
                            instDate = setProperDtFormat(instDate);
                            System.out.println("instDate   "+instDate);
                            holidayMap.put("NEXT_DATE",instDate);
                            holidayMap.put("BRANCH_CODE",getSelectedBranchID());
                            while(checkHoliday){
                                boolean tholiday = false;
                                System.out.println("enterytothecheckholiday"+checkHoliday);
                                List Holiday=ClientUtil.executeQuery("checkHolidayDateOD",holidayMap);
                                List weeklyOf=ClientUtil.executeQuery("checkWeeklyOffOD",holidayMap);
                                boolean isHoliday = Holiday.size()>0 ? true : false;
                                boolean isWeekOff = weeklyOf.size()>0 ? true : false;
                                if (isHoliday || isWeekOff) {
                                    System.out.println("#### diffDayPending Holiday True : "+diffDayPending);
                                    if(CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")){
                                        diffDayPending-=1;
                                        instDate.setDate(instDate.getDate()+1);
                                    }else{
                                        diffDayPending+=1;
                                        instDate.setDate(instDate.getDate()-1);
                                    }
                                    holidayMap.put("NEXT_DATE",instDate);
                                    checkHoliday=true;
                                    System.out.println("#### holidayMap : "+holidayMap);
                                }else{
                                    System.out.println("#### diffDay Holiday False : "+diffDayPending);
                                    checkHoliday=false;
                                }
                            }
                            System.out.println("#### diffDayPending Final Days : "+diffDayPending);
                            
                            
                            if (penalCalcBaseOn!=null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                                long penalCalcDays = 0;
                                System.out.println("diffDayPending%#$%%#$%#$ in days"+diffDayPending);
                                System.out.println("penalGraceValue%#$%%#$%#$ in days"+penalGraceValue);
                                //diffDayPending = DateUtil.dateDiff(instDate,DateUtil.addDays(endDate, (30+CommonUtil.convertObjToInt(penalGraceValue)))); // Commented by nithya on 16-03-2021 for KD-2712
                                Date uptoDt = DateUtil.addDays(instDate, CommonUtil.convertObjToInt(penalGraceValue));
                                System.out.println("uptoDt :: " + uptoDt);
                                diffDayPending = DateUtil.dateDiff(instDate,DateUtil.addDays(endDate, (30+CommonUtil.convertObjToInt(penalGraceValue))));
                                if(DateUtil.dateDiff(currDate, uptoDt) < 0){
                                   penalCalcDays = DateUtil.dateDiff(instDate,endDate);
                                }else{
                                   penalCalcDays = 0; 
                                }
                                System.out.println("diffDayPending%#$%%#$%#$ in days 2222"+diffDayPending);
                                if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Days")){
                                    if(diffDayPending > penalGraceValue){
                                        diffDayPending = penalCalcDays;
                                        if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Percent")){
                                            calc += (diffDayPending * penalValue * insAmt)/ 36500;
                                            penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                        }else if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Absolute")){
                                            calc += penalValue;
                                            penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                        }
                                    }
                                }else if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Installments")){
                                    // To be written
                                    if (diffDayPending > penalGraceValue *instFreq) {
                                        if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
//                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                            if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                            } else {
                                                calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                            }
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                            calc += penalValue;
                                            penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        }
                                    }
                                }
                            } else if (penalCalcBaseOn!=null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                                System.out.println("based on installments ");
                                if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Days")){
                                    System.out.println("diffDayPending%#$%%#$%#$"+diffDayPending);
                                    System.out.println("penalGraceValue%#$%%#$%#$"+penalGraceValue);
                                    if(diffDayPending > penalGraceValue){
                                        if (pendingInst == 0) {
                                            pendingInst = 1;
                                        }
                                        
                                        System.out.println("prependingInst before :: " + prependingInst);
                                        if(prependingInst == 0){
                                            prependingInst = 1;
                                        }
                                        System.out.println("prependingInst after :: " + prependingInst);
                                        
                                        System.out.println("Inside calculation procedure 1 ");
                                        if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Percent")){
                                            System.out.println("Inside calculation procedure pendingInst " + pendingInst+"mm"+insAmt+" "+penalValue);
                                            if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                calc += (double) ((insAmt * penalValue) * 7 / 36500.0) * pendingInst--;
                                            } else {
                                                if (CommonUtil.convertObjToStr(productMap.get("PREDEFINITION_INSTALLMENT")).equals("Y")) {
                                                    calc += (double) ((insAmt * penalValue) / 1200.0) * prependingInst--;
                                                }else{
                                                calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                                }
                                            }
                                            penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                             System.out.println("Calc " + calc);
                                        }else if(penalIntType!=null && !penalIntType.equals("") && penalIntType.equals("Absolute") && totInst<=noOfInstPay){
                                            calc += penalValue;
                                            penalList.add(rod.getNearest((double)(calc *100),100)/100);
                                        }
                                        System.out.println("nitz calc " + calc);
                                    }
                                }else if(penalGraceType!=null && !penalGraceType.equals("") && penalGraceType.equals("Installments")){
                                    // To be written
                                    if (diffDayPending > penalGraceValue *instFreq) {
                                        if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                            //                                                calc = insAmt*(((pendingInst+1)*pendingInst/2)*penalValue)/1200;
                                            if (isWeeklyOrMonthlyScheme != null && !isWeeklyOrMonthlyScheme.equals("") && isWeeklyOrMonthlyScheme.equals("W")) {
                                                calc = (double) ((pendingInst *(pendingInst+1)/2)*insAmt*penalValue)*7/36500 ; 
                                            } else {
                                                calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInst--;
                                            }
                                            	penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        	} else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                            	calc += penalValue;
                                            	penalList.add(rod.getNearest((double) (calc * 100), 100) / 100);
                                        	}
                                    }
                                }
                            }
                            
                            //After Scheme End Date Penal Calculating
                            HashMap c_hash = new HashMap();
                            c_hash.put("SCHEME_NAME", txtSchemeName.getText().trim());
                         //   List closureList = ClientUtil.executeQuery("checkSchemeClosureDetails", c_hash);
//                            if(closureList != null && closureList.size()>0)
//                            {
//                                c_hash=(HashMap)closureList.get(0);
//                                System.out.println(" Value of Scheme Closed Date " + (Date)c_hash.get("SCHEME_CLOSE_DT") );
//                             //   Date clsDate  = (Date)c_hash.get("SCHEME_CLOSE_DT");
//                                Date clsDate  = (Date)c_hash.get("SCHEME_END_DT");
////                                 Date clsDate  = DateUtil.getDateMMDDYYYY(c_hash.get("SCHEME_END_DT").toString());
//                                
//                                System.out.println("annnn"+clsDate);
//                                Calendar cal=null;
//                                cal=Calendar.getInstance();
//                                cal.setTime(clsDate);
//                                cal.add(Calendar.DATE, 1);
//                                clsDate=cal.getTime();
//                                System.out.println("clsDate.."+clsDate+"currrdddaaattteeeee"+currDate);
//                                        System.out.println("currDate.before(clsDate)"+currDate.before(clsDate)+"currDate.afetr(clsDate)"+currDate.after(clsDate));
//                                if(currDate.after(clsDate))
//                                {
//                                    System.out.println("m.m.m.,m.,m"+penalCalcBaseOn.equals("Days") +"blaaa"+ penalIntType.equals("Percent"));
//                                if((i+1==noOfInstPay+startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate,currDate)>0)){
//                                    System.out.println("#### endDate : "+endDate+"currDate"+currDate);
//                                    if(penalIntType.equals("Percent")){
//                                        diffDayPending = DateUtil.dateDiff(endDate,currDate);
//                                        System.out.println("#### endDate_diffDayPending mkmkmk: "+diffDayPending);
//                                        System.out.println("noOfInstPay"+noOfInstPay+"insAmt"+insAmt+"penalValue"+penalValue);
//                                        calc += (double) ((((insAmt * noOfInstPay*penalValue)/100.0)*diffDayPending)/365);
//                                    }
//                                    // Absolute Not Required...
//                                }
//                                }
//                            }
                            System.out.println(" callll"+calc);
                            penal = (calc+0.5) - penal;
                            System.out.println("pppenall"+penal);
                            nextInstMap.put("PENAL", String.valueOf(penal));
                            installmentMap.put(String.valueOf(i+noOfInsPaid+addNo),nextInstMap);
                            penal = calc+0.5;
                        }
                    }
                    
                    
                      HashMap c_hash = new HashMap();
                            c_hash.put("SCHEME_NAME", txtSchemeName.getText().trim());
                            List closureList = ClientUtil.executeQuery("checkSchemeClosureDetailsForClosed", c_hash);
                          List closedPenal=ClientUtil.executeQuery("getClosedRate", c_hash);
                         double clodespenalRt=0.0;
                          if(closedPenal!=null && closedPenal.size()>0)
                          {
                              if(((HashMap)closedPenal.get(0)).get("CLOSED_PENAL")!=null)
                              {
                           clodespenalRt=Double.parseDouble(((HashMap)closedPenal.get(0)).get("CLOSED_PENAL").toString());
                              
                              }else
                              {
                                  ClientUtil.showMessageWindow("Closed penal Rate is not set for this product !!!"); 
                                  clodespenalRt=0.0;
                              }       
                          }
                          else
                          {
                              ClientUtil.showMessageWindow("Closed penal Rate is not set for this product !!!"); 
                               clodespenalRt=0.0;   
                                      
                           }
                            if(closureList != null && closureList.size()>0)
                            {
                                c_hash=(HashMap)closureList.get(0);
                                System.out.println(" Value of Scheme Closed Date " + (Date)c_hash.get("SCHEME_END_DT") );
                             //   Date clsDate  = (Date)c_hash.get("SCHEME_CLOSE_DT");
                                Date clsDate  = (Date)c_hash.get("SCHEME_END_DT");
//                                 Date clsDate  = DateUtil.getDateMMDDYYYY(c_hash.get("SCHEME_END_DT").toString());
                                
                                System.out.println("annnn"+clsDate);
                                Calendar cal=null;
                                cal=Calendar.getInstance();
                                cal.setTime(clsDate);
                                cal.add(Calendar.MONTH, 1);
                              //  cal.add(Calendar.DATE, 1);
                                clsDate=cal.getTime();
                                if (bonusFirstInst.equals("Y")){
                                    clsDate  = (Date)c_hash.get("SCHEME_END_DT");
                                }
                                System.out.println("clsDate.."+clsDate+"currrdddaaattteeeee"+currDate);
                                System.out.println("currDate.before(clsDate)"+currDate.before(clsDate)+"currDate.afetr(clsDate)"+currDate.after(clsDate));
                              
//commented by Nidhin --> Pelal will become large if pending installment and no of inst to pay field is equal                                
//                    if(txtPendingInst.getText().equals(txtNoOfInstToPaay.getText()))
//                    {
//                         double forOne=(double)((insAmt * penalValue)/1200.0);
//                         System.out.println("forOne.."+forOne);
//                         calc=calc+forOne;
//                         System.out.println("ACTUAL:++++++"+calc);
//                    }
                    
                                
                                
                                
                                if(currDate.after(clsDate))
                                {
                                    System.out.println("m.m.m.,m.,m"+penalCalcBaseOn.equals("Days") +"blaaa"+ penalIntType.equals("Percent"));
//                                if((i+1==noOfInstPay+startNoForPenal) && !(penalCalcBaseOn.equals("Days") && penalIntType.equals("Percent")) && (DateUtil.dateDiff(endDate,currDate)>0)){
                                    System.out.println("#### endDate : "+endDate+"currDate"+currDate);
//                                    if(penalIntType.equals("Percent")){
                                        System.out.println("(30+CommonUtil.convertObjToInt(penalGraceValue)) "+(30+CommonUtil.convertObjToInt(penalGraceValue)));
                                        diffDayPending = DateUtil.dateDiff(DateUtil.addDays(endDate, (30+CommonUtil.convertObjToInt(penalGraceValue))),currDate);//Jeffin John
                                        if (bonusFirstInst.equals("Y")){
                                            System.out.println("advance collection --- "+ bonusFirstInst);
                                            diffDayPending = DateUtil.dateDiff(DateUtil.addDays(endDate, (CommonUtil.convertObjToInt(penalGraceValue))),currDate);
                                        }
                                        
                                        System.out.println("#### endDate_diffDayPending mkmkmk: "+diffDayPending);
                                        System.out.println("calc%#$%#%#%#$"+calc);
                                        System.out.println("insAmt%#$%#%#%#$"+insAmt);
                                        System.out.println("clodespenalRt%#$%#%#%#$"+clodespenalRt);
                                        System.out.println("diffDayPending%#$%#%#%#$"+diffDayPending);
                                        System.out.println("noOfInstPay%#$%#%#%#$"+noOfInstPay);
                                      //  calc += (double) ((((insAmt * noOfInstPay*clodespenalRt)/100.0)*diffDayPending)/365);
                                          if(diffDayPending > penalGraceValue){
                                          calc += (double) ((((insAmt * noOfInstPay*clodespenalRt))*diffDayPending)/36500);
                                          }
////                                    }
                                    // Absolute Not Required...
                              //  }
                                }
                            }
                    /// penal = (calc+0.5) - penal;
                           // System.out.println("pppenall"+penal);
                           // nextInstMap.put("PENAL", String.valueOf(penal));
                           // installmentMap.put(String.valueOf(i+noOfInsPaid+addNo),nextInstMap);
                          //  penal = calc+0.5;
                    
                    
                    System.out.println("calcmmmmmm"+calc);
                    
                    // Added by nithya on 23 Jun 2025 for KD-4275
                    if (observable.getChkThalayal()) {
                       calc = 0.0;
                    } else if (observable.getChkMunnal()) {
                        calc = 0.0;
                    }
                    
                    if(calc>0){
                        txtPenalAmtPayable.setText(String.valueOf((long)(calc+0.5)));
                    }
                    penalAmtWithoutEdit = calc; 
                    penalRealList = new ArrayList();
                    DecimalFormat df = new DecimalFormat("#.##");
//                   al.add(CommonUtil.convertObjToDouble(penalList.get(0)));
                    double d = 0;
                    if (penalList != null && penalList.size() > 0) {
                        double firVal = CommonUtil.convertObjToDouble(penalList.get(0));
                        Collections.reverse(penalList);
                        for (int i = 0; i <= penalList.size(); i++) {
                            if (i + 1 < penalList.size()) {
                                d = CommonUtil.convertObjToDouble(penalList.get(i)) - CommonUtil.convertObjToDouble(penalList.get(i + 1));
                                penalRealList.add(d);
                            }
                            
                        }
                        penalRealList.add(firVal);
                    }
                    Collections.reverse(penalRealList);
                   double  calcPe = (double) ((((insAmt * clodespenalRt)) * diffDayPending) / 36500);
                    for (int i = 0; i < penalRealList.size(); i++) {
                        double totPenal = 0.0;
                        totPenal = CommonUtil.convertObjToDouble(penalRealList.get(i))+calcPe;
                      penalRealList.set(i,df.format(totPenal));
                      
                    }
                }//pending installment calculation ends...
                
                
                //Discount calculation details Starts...
                for(int i = 0;i<noOfInstPay;i++){
//                    for(int i = 1;i<=noOfInstPay;i++){
                        HashMap nextInstMap = new HashMap();
                        nextInstMap.put("SCHEME_NAME",txtSchemeName.getText());
                        nextInstMap.put("DIVISION_NO",txtDivisionNo.getText());
                        nextInstMap.put("SL_NO",new Double(i+noOfInsPaid));
                        List listRec = ClientUtil.executeQuery("getSelectNextInstDate", nextInstMap);
                        if(listRec==null || listRec.size()==0) {
                            int instDay = CommonUtil.convertObjToInt(observable.getChittalMap().get("INSTALLMENT_DAY"));
                            System.out.println("#@$@#@#@ instDay : "+instDay);
                            Date curDate = (Date) curr_dt.clone();
                            int curMonth = curDate.getMonth();
                            System.out.println("@#$$#$#instDay"+instDay);
                            curDate.setMonth(curMonth+i+1);
                            curDate.setDate(instDay);
                            listRec = new ArrayList();
                            nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                            listRec.add(nextInstMap);
                            //                        bonusAvailabe = false;
                        }
                        if(listRec!=null && listRec.size()>0){
                            nextInstMap = (HashMap)listRec.get(0);
                            instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                            long diffDay = DateUtil.dateDiff(instDate,currDate);
                            System.out.println("First #########diffDay : "+diffDay);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday=true;
                        System.out.println("instDate   "+instDate);
                        instDate = setProperDtFormat(instDate);
                        System.out.println("instDate   "+instDate);
                        holidayMap.put("NEXT_DATE",instDate);
                        holidayMap.put("BRANCH_CODE",getSelectedBranchID());
                        while(checkHoliday){
                            boolean tholiday = false;
                            System.out.println("enterytothecheckholiday"+checkHoliday);
                            List Holiday=ClientUtil.executeQuery("checkHolidayDateOD",holidayMap);
                            List weeklyOf=ClientUtil.executeQuery("checkWeeklyOffOD",holidayMap);
                            boolean isHoliday = Holiday.size()>0 ? true : false;
                            boolean isWeekOff = weeklyOf.size()>0 ? true : false;
                            if (isHoliday || isWeekOff) {
                                System.out.println("#### diffDay Holiday True : "+diffDay);
                                if(CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")){
                                    diffDay-=1;
                                    instDate.setDate(instDate.getDate()+1);
                                }else{
                                    diffDay+=1;
                                    instDate.setDate(instDate.getDate()-1);
                                }
                                holidayMap.put("NEXT_DATE",instDate);
                                checkHoliday=true;
                                System.out.println("#### holidayMap : "+holidayMap);
                            }else{
                                System.out.println("#### diffDay Holiday False : "+diffDay);
                                checkHoliday=false;
                            }
                        }
                        System.out.println("#### diffDay Final Days : "+diffDay);
                            String hoildayInt = CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT"));
                            if(productMap.get("BONUS_ALLOWED")!=null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y") ||
                            productMap.get("BONUS_ALLOWED").equals("N"))){
                                
                                //discount calculation details...
                                long discountAmt = 0;
                                String discount = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_ALLOWED"));
                                if(discount!=null && !discount.equals("") && discount.equals("Y")){
                                    String discountType = CommonUtil.convertObjToStr(productMap.get("DISCOUNT_RATE_TYPE"));
                                    long discountValue = CommonUtil.convertObjToLong(productMap.get("DISCOUNT_RATE_AMT"));
                                    if(observable.getRdoPrizedMember_Yes() == true){//discount calculation for prized prerson...
                                        String discountPrizedDays = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_DAYS"));
                                        String discountPrizedMonth = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_MONTHS"));
                                        String discountPrizedAfter = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_AFTER"));
                                        String discountPrizedEnd = CommonUtil.convertObjToStr(productMap.get("DIS_PRIZED_GRACE_PERIOD_END"));
                                        long discountPrizedValue = CommonUtil.convertObjToLong(productMap.get("DIS_PRIZED_GRACE_PERIOD"));
                                        if(discountPrizedDays!=null && !discountPrizedDays.equals("") && discountPrizedDays.equals("D") && diffDay<=discountPrizedValue){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                if(diffDay<=discountPrizedValue){
                                                    totDiscAmt = totDiscAmt + calc;
                                                }
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                if(diffDay<=discountPrizedValue){
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            }
                                        }else if(discountPrizedMonth!=null && !discountPrizedMonth.equals("") && discountPrizedMonth.equals("M") && diffDay<=(discountPrizedValue * 30) /*&& pendingInst<noOfInstPay*/){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                txtDiscountAmt.setText(String.valueOf(calc));
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                txtDiscountAmt.setText(String.valueOf(discountValue));
                                            }
                                        }else if(discountPrizedAfter!=null && !discountPrizedAfter.equals("") && discountPrizedAfter.equals("A") && currDate.getDate()<= discountPrizedValue /*&& pendingInst<noOfInstPay*/){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                txtDiscountAmt.setText(String.valueOf(calc));
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                txtDiscountAmt.setText(String.valueOf(discountValue));
                                            }
                                        }else if(discountPrizedEnd!=null && !discountPrizedEnd.equals("") && discountPrizedEnd.equals("E") && pendingInst<noOfInstPay){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                txtDiscountAmt.setText(String.valueOf(calc));
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                txtDiscountAmt.setText(String.valueOf(discountValue));
                                            }
                                        }else{
                                            txtDiscountAmt.setText(String.valueOf("0"));
                                        }
                                    }else if(observable.getRdoPrizedMember_No() == true){//discount calculation non prized person...
                                        String discountGraceDays = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_DAYS"));
                                        String discountGraceMonth = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_MONTHS"));
                                        String discountGraceAfter = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_AFTER"));
                                        String discountGraceEnd = CommonUtil.convertObjToStr(productMap.get("DIS_GRACE_PERIOD_END"));
                                        long discountGraceValue = CommonUtil.convertObjToLong(productMap.get("DIS_GRACE_PERIOD"));
                                        if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("D")){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")) {
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                if(diffDay<=discountGraceValue){
                                                    totDiscAmt = totDiscAmt + calc;
                                                }
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")) {
                                                if(diffDay<=discountGraceValue){
                                                    totDiscAmt = totDiscAmt + discountValue;
                                                }
                                            }else{
                                                txtDiscountAmt.setText(String.valueOf("0"));
                                            }
                                        }else if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("M") && diffDay<=discountGraceValue * 30 && pendingInst<noOfInstPay){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                txtDiscountAmt.setText(String.valueOf(calc));
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                txtDiscountAmt.setText(String.valueOf(discountValue));
                                            }
                                        }else if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("A") && currDate.getDate()<= discountGraceValue && pendingInst<noOfInstPay){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                txtDiscountAmt.setText(String.valueOf(calc));
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                txtDiscountAmt.setText(String.valueOf(discountValue));
                                            }
                                        }else if(discountGraceDays!=null && !discountGraceDays.equals("") && discountGraceDays.equals("E") && pendingInst<noOfInstPay){
                                            if(discountType!=null && !discountType.equals("") && discountType.equals("Percent")){
                                                long calc = discountValue * (long)CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()/100;
                                                txtDiscountAmt.setText(String.valueOf(calc));
                                            }else if(discountType!=null && !discountType.equals("") && discountType.equals("Absolute")){
                                                txtDiscountAmt.setText(String.valueOf(discountValue));
                                            }
                                        }else{
                                            txtDiscountAmt.setText(String.valueOf("0"));
                                        }
                                    }
                                }else if(discount!=null && !discount.equals("") && discount.equals("N")){
                                    txtDiscountAmt.setText(String.valueOf("0"));
                                }
                                //------------------------------------------------------------later check it-----------------------------------------------------------------------
                                if(productMap.get("PRIZED_OWNER_BONUS")!=null && !productMap.get("PRIZED_OWNER_BONUS").equals("") && productMap.get("PRIZED_OWNER_BONUS").equals("Y")){
                                    
                                }else if(productMap.get("PRIZED_OWNER_BONUS")!=null && !productMap.get("PRIZED_OWNER_BONUS").equals("") && productMap.get("PRIZED_OWNER_BONUS").equals("N")){
                                    
                                }
                                if(productMap.get("AFTER_AUCTION_ELIGIBLE")!=null && !productMap.get("AFTER_AUCTION_ELIGIBLE").equals("") && productMap.get("AFTER_AUCTION_ELIGIBLE").equals("Y")){
                                    
                                }else if(productMap.get("AFTER_AUCTION_ELIGIBLE")!=null && !productMap.get("AFTER_AUCTION_ELIGIBLE").equals("") && productMap.get("AFTER_AUCTION_ELIGIBLE").equals("N")){
                                    
                                }
                                if(productMap.get("AFTER_PAYMENT_ELIGIBLE")!=null && !productMap.get("AFTER_PAYMENT_ELIGIBLE").equals("") && productMap.get("AFTER_PAYMENT_ELIGIBLE").equals("Y")){
                                    
                                }else if(productMap.get("AFTER_PAYMENT_ELIGIBLE")!=null && !productMap.get("AFTER_PAYMENT_ELIGIBLE").equals("") && productMap.get("AFTER_PAYMENT_ELIGIBLE").equals("N")){
                                    
                                }
                                if(productMap.get("PRIZED_DEFAULTERS")!=null && !productMap.get("PRIZED_DEFAULTERS").equals("") && productMap.get("PRIZED_DEFAULTERS").equals("Y")){
                                    
                                }else if(productMap.get("PRIZED_DEFAULTERS")!=null && !productMap.get("PRIZED_DEFAULTERS").equals("") && productMap.get("PRIZED_DEFAULTERS").equals("N")){
                                    
                                }
                                if(productMap.get("BONUS_ALLOWED").equals("N")){
                                    txtBonusAmtAvail.setText(String.valueOf("0"));
                                }
                                discountAmt = totDiscAmt - discountAmt;
                                HashMap instMap = new HashMap();
                                if (installmentMap.containsKey(String.valueOf(i+noOfInsPaid+1))) {
                                    instMap = (HashMap)installmentMap.get(String.valueOf(i+noOfInsPaid+1));
                                    instMap.put("DISCOUNT", String.valueOf(discountAmt));
                                    installmentMap.put(String.valueOf(i+noOfInsPaid+1),instMap);
                                }
                                discountAmt = totDiscAmt;
                            }
                        }
                    }
                    txtDiscountAmt.setText(String.valueOf(totDiscAmt));
                    
                    //Bonus calculation details Starts...
                // Changed by Rajesh
                   
                for (int i = startNoForPenal; i <= noOfInstPay - addNo; i++) {
                    
                    // Check for bonus reversal
                    boolean reversalBonusExists = false;
                     if(productMap.containsKey("BANK_ADV_FORFIET") && productMap.get("BANK_ADV_FORFIET") != null && productMap.get("BANK_ADV_FORFIET").equals(("N"))){
                         HashMap reversalMap =  new HashMap();
                         reversalMap.put("CHITTAL_NO",txtChittalNo.getText());
                         reversalMap.put("SCHEME_NAME", txtSchemeName.getText());
                         reversalMap.put("DIVISION_NO", CommonUtil.convertObjToInt(txtDivisionNo.getText()));
                         reversalMap.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                         List reversalList = ClientUtil.executeQuery("getSelectForfietAmountForChittal", reversalMap);
                         if(reversalList != null && reversalList.size() > 0){
                             reversalBonusExists = true;
                         }
                     }
                    // End
                    
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("SCHEME_NAME", txtSchemeName.getText());
                    nextInstMap.put("DIVISION_NO", CommonUtil.convertObjToInt(txtDivisionNo.getText()));
                    nextInstMap.put("SL_NO", new Double(i + noOfInsPaid + addNo + firstInst_No));
                    List listRec = ClientUtil.executeQuery("getSelectBonusAndNextInstDateWithoutDivision", nextInstMap);
                    if (listRec == null || listRec.size() == 0) {
                        int instDay = CommonUtil.convertObjToInt(observable.getChittalMap().get("INSTALLMENT_DAY"));
                        System.out.println("#@$@#@#@ instDay : " + instDay);
                        Date curDate = (Date) curr_dt.clone();
                        int curMonth = curDate.getMonth();
                        System.out.println("@#$$#$#instDay" + instDay);
                        curDate.setMonth(curMonth + i + 1);
                        curDate.setDate(instDay);
                        listRec = new ArrayList();
                        nextInstMap.put("NEXT_INSTALLMENT_DATE", curDate);
                        listRec.add(nextInstMap);
                        bonusAvailabe = false;
                        System.out.println("y000000"+bonusAvailabe);
                    }
                    if (listRec != null && listRec.size() > 1) {
                        ClientUtil.showAlertWindow("Divisions having different installment dates...\nCannot proceed...");
                        return;
                    }
                    if (listRec != null && listRec.size() > 0) {
                        nextInstMap = (HashMap) listRec.get(0);
                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                        bonusAmt = CommonUtil.convertObjToDouble(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_BONUS_AMOUNT"))).doubleValue();
                        if (observable.getMultipleMember().equals("Y")) {
                            whereMap = new HashMap();
                            int noOfCoChittal = 0;
                            whereMap.put("CHITTAL_NUMBER", txtChittalNo.getText());
                            whereMap.put("SCHEME_NAMES", txtSchemeName.getText());
                            List applicationLst = ClientUtil.executeQuery("getSelectChitNoNotinMasterDetails", whereMap); // Count No Of Co-Chittals
                            if (applicationLst != null && applicationLst.size() > 0) {
                                noOfCoChittal = applicationLst.size();
                                bonusAmt = bonusAmt / noOfCoChittal;
                            }
                        }
                        System.out.println("  bonusAmt  "+bonusAmt);
//                        if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
//                                && bonusAmt > 0) {
//                            Rounding rod = new Rounding();
//                            if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
//                                bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
//                            } else {
//                                bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
//                            }
//                        }
                        long diffDay = DateUtil.dateDiff(instDate, currDate);
                        System.out.println("First #########diffDay : " + diffDay);
                        //Holiday Checking - Added By Suresh
                        HashMap holidayMap = new HashMap();
                        boolean checkHoliday = true;
                        System.out.println("instDate   " + instDate);
                        instDate = setProperDtFormat(instDate);
                        System.out.println("instDate   " + instDate);
                        holidayMap.put("NEXT_DATE", instDate);
                        holidayMap.put("BRANCH_CODE", getSelectedBranchID());
                        while (checkHoliday) {
                            boolean tholiday = false;
                            System.out.println("enterytothecheckholiday" + checkHoliday);
                            List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayMap);
                            List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayMap);
                            boolean isHoliday = Holiday.size() > 0 ? true : false;
                            boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                            if (isHoliday || isWeekOff) {
                                System.out.println("#### diffDay Holiday True : " + diffDay);
                                if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                    diffDay -= 1;
                                    instDate.setDate(instDate.getDate() + 1);
                                } else {
                                    diffDay += 1;
                                    instDate.setDate(instDate.getDate() - 1);
                                }
                                holidayMap.put("NEXT_DATE", instDate);
                                checkHoliday = true;
                                System.out.println("#### holidayMap : " + holidayMap);
                            } else {
                                System.out.println("#### diffDay Holiday False : " + diffDay);
                                checkHoliday = false;
                            }
                        }
                        System.out.println("#### diffDay Final Days : " + diffDay);
                        if (productMap.get("BONUS_ALLOWED") != null && !productMap.get("BONUS_ALLOWED").equals("") && (productMap.get("BONUS_ALLOWED").equals("Y"))) {
                            //bonus calculation details...
                            String prizedDefaultYesN = "";
                            if (productMap.containsKey("PRIZED_DEFAULTERS") && productMap.get("PRIZED_DEFAULTERS") != null) {
                                prizedDefaultYesN = CommonUtil.convertObjToStr(productMap.get("PRIZED_DEFAULTERS"));
                            }
                            System.out.println("dsfszxczzx543534543");
////                            if (bonusAvailabe == true) {
//////                                if (observable.getRdoPrizedMember_Yes() == true) {
//////                                    whereMap = new HashMap();
//////                                    whereMap.put("CHITTAL_NO", txtChittalNo.getText());
//////                                    whereMap.put("SUB_NO", CommonUtil.convertObjToStr(txtSubNo.getText()));
//////                                    whereMap.put("SCHEME_NAME", txtSchemeName.getText());
//////                                    List paymentList = ClientUtil.executeQuery("getSelectMDSPaymentDetails", whereMap);
//////                                  System.out.println("paymentList"+paymentList);
//////                                    //meaning changed prized owner bonus.. If Y then prized money payemt==prized 
//////                                    System.out.println("productMap.get()"+productMap.get("PRIZED_OWNER_BONUS"));
//////                                    if(productMap.get("PRIZED_OWNER_BONUS").equals("Y"))
//////                                    {
//////                                        System.out.println("paymentList"+paymentList);
//////                                        
//////                                        if(paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("Y")) {
//////                                        String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
//////                                        String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
//////                                        String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
//////                                        String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
//////                                        long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
//////                                        if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
//////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////                                        } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
//////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////                                        } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
//////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////                                        } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
//////                                        } else {
//////                                        }
//////                                   System.out.println("111ss"+totBonusAmt);
//////                                    }
//////                                        //else non prized case is considered
//////                                        else
//////                                        {
//////                                            
//////                                               String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
//////                                    String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
//////                                    String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
//////                                    String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
//////                                    long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
//////                                    System.out.println("bonusGraceDays "+bonusGraceDays+"bonusGraceMonth"+bonusGraceMonth+"bonusGraceOnAfter"+bonusGraceOnAfter+" "+bonusGraceEnd+" bonusGraceValue"+bonusGraceValue);
//////                                    if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue /*
//////                                             * && pendingInst<noOfInstPay
//////                                             */) {
//////                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
//////                                        totBonusAmt = totBonusAmt + bonusAmt;
//////                                    } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30) /*
//////                                             * && pendingInst<noOfInstPay
//////                                             */) {
//////                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
//////                                        totBonusAmt = totBonusAmt + bonusAmt;
//////                                        System.out.println("dayDiff in Bomnusss"+diffDay+" "+(bonusGraceValue * 30)+" "+totBonusAmt+" "+bonusAmt);
//////                                    } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue /*
//////                                             * && pendingInst<noOfInstPay
//////                                             */) {
//////                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
//////                                        totBonusAmt = totBonusAmt + bonusAmt;
//////                                    } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E") /*
//////                                             * && pendingInst<noOfInstPay
//////                                             */) {
//////                                    } else {
//////                                        //                                        txtBonusAmt.setText(String.valueOf("0"));
//////                                    }
//////                                            
//////                                        }
//////                                        
//////                                        
//////                                        
//////                                    }
//////                                    else //prized money paymen done person is prized
//////                                    {
//////                                        
//////                                         String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
//////                                        String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
//////                                        String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
//////                                        String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
//////                                        long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
//////                                        if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
//////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////                                        } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
//////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////                                        } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
//////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////                                        } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
//////                                        } else {
//////                                        }
//////                                   System.out.println("111ss"+totBonusAmt);
//////                                        
//////                                        
//////                                    }
                                    
                                    
                                    
    //bonus calculation details...
                            if (bonusAvailabe == true) {
                                long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                String bonusPrzInMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                if (observable.getRdoPrizedMember_Yes() == true) {
                                    HashMap nextActMap = new HashMap();
                                    nextActMap.put("SCHEME_NAME", txtSchemeName.getText());
                                    nextActMap.put("DIVISION_NO", txtDivisionNo.getText());
                                    nextActMap.put("SL_NO",CommonUtil.convertObjToDouble(i + noOfInsPaid));
                                    List listAuc = ClientUtil.executeQuery("getSelectNextAuctDate", nextActMap);
                                    if (listAuc != null && listAuc.size() > 0) {
                                        nextActMap = (HashMap) listAuc.get(0);
                                    }
                                    Date drawAuctionDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextActMap.get("DRAW_AUCTION_DATE")));
                                    System.out.println("Date acution d" + drawAuctionDate);
                                    Calendar cal = null;
                                    Date newDate = null;
                                    if (bonusPrzInMonth != null && bonusPrzInMonth.equalsIgnoreCase("M")) {
                                        cal = Calendar.getInstance();
                                        cal.setTime(drawAuctionDate);
                                        cal.add(Calendar.MONTH, CommonUtil.convertObjToInt(bonusPrizedValue));
                                        newDate = cal.getTime();
                                    } else {
                                        newDate = DateUtil.addDays(drawAuctionDate, CommonUtil.convertObjToInt(bonusPrizedValue));
                                    }
//                                    Date newDate = DateUtil.addDays(drawAuctionDate, CommonUtil.convertObjToInt(bonusPrizedValue));
                                    long dateDiff = DateUtil.dateDiff(curr_dt, newDate);
                                    System.out.println("Date acution c" + newDate);
                                    System.out.println("Date acution c" + dateDiff);
                                    //Holiday checking Added  By Nidhin 12/11/2014
                                    HashMap holidayCheckMap = new HashMap();
                                    boolean checkForHoliday = true;
                                    newDate = setProperDtFormat(newDate);
                                    holidayCheckMap.put("NEXT_DATE", newDate);
                                    holidayCheckMap.put("BRANCH_CODE", getSelectedBranchID());
                                    while (checkForHoliday) {
                                        List Holiday = ClientUtil.executeQuery("checkHolidayDateOD", holidayCheckMap);
                                        List weeklyOf = ClientUtil.executeQuery("checkWeeklyOffOD", holidayCheckMap);
                                        boolean isHoliday = Holiday.size() > 0 ? true : false;
                                        boolean isWeekOff = weeklyOf.size() > 0 ? true : false;
                                        if (isHoliday || isWeekOff) {
                                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                                dateDiff += 1;
                                                diffDay -= 1; // Added by nithya on 14-08-2020 for KD-2163 
                                                newDate.setDate(newDate.getDate() + 1);
                                            } else {
                                                dateDiff -= 1;
                                                diffDay += 1; // Added by nithya on 14-08-2020 for KD-2163 
                                                newDate.setDate(newDate.getDate() - 1);
                                            }
                                            holidayCheckMap.put("NEXT_DATE", newDate);
                                            checkForHoliday = true;
                                          } else {
                                            checkForHoliday = false;
                                        }
                                    }
                                    //End for HoliDay Checking
                                    whereMap = new HashMap();
                                    whereMap.put("CHITTAL_NO", txtChittalNo.getText());
                                    whereMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));
                                    whereMap.put("SCHEME_NAME", txtSchemeName.getText());
                                    List paymentList = ClientUtil.executeQuery("getSelectMDSPaymentDetails", whereMap);
                                    if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("N")) {
                                        System.out.println("###### NO BONUS FOR PRODUCT PARAMETER");
                                    } else if(paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("Y")) {
                                        String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
                                        String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
                                        String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
                                        String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
                                        //long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
                                        if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
                                            System.out.println("Total Bonus before" + totBonusAmt);
                                            if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                                totBonusAmt = totBonusAmt + bonusAmt;
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            }
                                            else{
                                                 // 16-07-2020
                                            if(reversalBonusExists){
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                            }
                                            System.out.println("Total Bonus after" + totBonusAmt);
                                        } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
                                            if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            }
                                            else{
                                                 // 16-07-2020
                                            if(reversalBonusExists){
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                            }
                                        } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
                                            if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                            totBonusAmt = totBonusAmt + bonusAmt;
                                            bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                            }
                                            else{
                                                 // 16-07-2020
                                            if(reversalBonusExists){
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                            }
                                        } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
                                        } else {
                                            if (productMap.containsKey("FORFEITE_HD_Y_N") && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                System.out.println("BonusAmount Yes" + bonusAmt);
                                            }
                                            // 16-07-2020
                                            if(reversalBonusExists){
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                        }
                                   System.out.println("111ss"+totBonusAmt);
                                    }
                                    
                                    else if(productMap.get("PRIZED_OWNER_BONUS").equals("Y") || productMap.get("PRIZED_OWNER_BONUS").equals("N"))
                                    {
                                         String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                    String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                    String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                    String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                    long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                    System.out.println("bonusGraceDays "+bonusGraceDays+"bonusGraceMonth"+bonusGraceMonth+"bonusGraceOnAfter"+bonusGraceOnAfter+" "+bonusGraceEnd+" bonusGraceValue"+bonusGraceValue);
                                    if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        System.out.println("dateDiffdateDiff"+dateDiff);
                                    if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                        totBonusAmt = totBonusAmt + bonusAmt;
                                        bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));                                        
                                    } else {
                                            // 16-07-2020
                                            if (reversalBonusExists) {
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                      }
                                    } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30) /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                        totBonusAmt = totBonusAmt + bonusAmt;
                                        bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        }
                                        else {
                                            // 16-07-2020
                                            if (reversalBonusExists) {
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                        }
                                        System.out.println("dayDiff in Bomnusss"+diffDay+" "+(bonusGraceValue * 30)+" "+totBonusAmt+" "+bonusAmt);
                                    } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        if (dateDiff >= 0 && (prizedDefaultYesN.equals("N") || prizedDefaultYesN.equals("Y"))) {
                                        totBonusAmt = totBonusAmt + bonusAmt;
                                        bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        }
                                        else {
                                            // 16-07-2020
                                            if (reversalBonusExists) {
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                        }
                                    } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E") /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                    } else {
                                        //                                        txtBonusAmt.setText(String.valueOf("0"));
                                        
                                          if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                                System.out.println("BonusAmount Yes23" + bonusAmt);
                                            }
                                        
                                        // 16-07-2020
                                            if(reversalBonusExists){
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                    }
                                    System.out.println("222ss"+totBonusAmt);
                                    }
                                    else
                                    {
                                       // System.out.println("Prized chittal with no bomus eligibility");
                                        
                                        
                                    }                                
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
                                    
//////////                                    if (paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("N")) {
//////////                                        System.out.println("###### NO BONUS FOR PRODUCT PARAMETER");
//////////                                    } else if(paymentList != null && paymentList.size() > 0 && productMap.get("PRIZED_OWNER_BONUS").equals("Y")) {
//////////                                        String bonusPrizedDays = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_DAYS"));
//////////                                        String bonusPrizedMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_MNTH"));
//////////                                        String bonusPrizedAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_AFT"));
//////////                                        String bonusPrizedEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_PRIZED_GRACE_PERIOD_END"));
//////////                                        long bonusPrizedValue = CommonUtil.convertObjToLong(productMap.get("BONUS_PRIZED_GRACE_PERIOD"));
//////////                                        if (bonusPrizedDays != null && !bonusPrizedDays.equals("") && bonusPrizedDays.equals("D") && diffDay <= bonusPrizedValue) {
//////////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////////                                        } else if (bonusPrizedMonth != null && !bonusPrizedMonth.equals("") && bonusPrizedMonth.equals("M") && diffDay <= (bonusPrizedValue * 30)) {
//////////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////////                                        } else if (bonusPrizedAfter != null && !bonusPrizedAfter.equals("") && bonusPrizedAfter.equals("A") && currDate.getDate() <= bonusPrizedValue) {
//////////                                            totBonusAmt = totBonusAmt + bonusAmt;
//////////                                        } else if (bonusPrizedEnd != null && !bonusPrizedEnd.equals("") && bonusPrizedEnd.equals("E")) {
//////////                                        } else {
//////////                                        }
//////////                                   System.out.println("111ss"+totBonusAmt);
//////////                                    }
//////////                                    
//////////                                    else if(productMap.get("PRIZED_OWNER_BONUS").equals("Y"))
//////////                                    {
//////////                                         String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
//////////                                    String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
//////////                                    String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
//////////                                    String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
//////////                                    long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
//////////                                    System.out.println("bonusGraceDays "+bonusGraceDays+"bonusGraceMonth"+bonusGraceMonth+"bonusGraceOnAfter"+bonusGraceOnAfter+" "+bonusGraceEnd+" bonusGraceValue"+bonusGraceValue);
//////////                                    if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue /*
//////////                                             * && pendingInst<noOfInstPay
//////////                                             */) {
//////////                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
//////////                                        totBonusAmt = totBonusAmt + bonusAmt;
//////////                                    } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30) /*
//////////                                             * && pendingInst<noOfInstPay
//////////                                             */) {
//////////                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
//////////                                        totBonusAmt = totBonusAmt + bonusAmt;
//////////                                        System.out.println("dayDiff in Bomnusss"+diffDay+" "+(bonusGraceValue * 30)+" "+totBonusAmt+" "+bonusAmt);
//////////                                    } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue /*
//////////                                             * && pendingInst<noOfInstPay
//////////                                             */) {
//////////                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
//////////                                        totBonusAmt = totBonusAmt + bonusAmt;
//////////                                    } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E") /*
//////////                                             * && pendingInst<noOfInstPay
//////////                                             */) {
//////////                                    } else {
//////////                                        //                                        txtBonusAmt.setText(String.valueOf("0"));
//////////                                    }
//////////                                    System.out.println("222ss"+totBonusAmt);
//////////                                    }
//////////                                    else
//////////                                    {
//////////                                       // System.out.println("Prized chittal with no bomus eligibility");
//////////                                        
//////////                                        
//////////                                    }
                                    ////////////////////////////////////////////////////////////////////////////
                                } else if (observable.getRdoPrizedMember_No() == true) {
                                    String bonusGraceDays = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_DAYS"));
                                    String bonusGraceMonth = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_MONTHS"));
                                    String bonusGraceOnAfter = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_AFTER"));
                                    String bonusGraceEnd = CommonUtil.convertObjToStr(productMap.get("BONUS_GRACE_PERIOD_END"));
                                    long bonusGraceValue = CommonUtil.convertObjToLong(productMap.get("BONUS_GRACE_PERIOD"));
                                    System.out.println("bonusGraceDays "+bonusGraceDays+"bonusGraceMonth"+bonusGraceMonth+"bonusGraceOnAfter"+bonusGraceOnAfter+" "+bonusGraceEnd+" bonusGraceValue"+bonusGraceValue);
                                    if (bonusGraceDays != null && !bonusGraceDays.equals("") && bonusGraceDays.equals("D") && diffDay <= bonusGraceValue /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        totBonusAmt = totBonusAmt + bonusAmt;
                                        bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusGraceMonth != null && !bonusGraceMonth.equals("") && bonusGraceMonth.equals("M") && diffDay <= (bonusGraceValue * 30) /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        totBonusAmt = totBonusAmt + bonusAmt;
                                        bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                        System.out.println("dayDiff in Bomnusss"+diffDay+" "+(bonusGraceValue * 30)+" "+totBonusAmt+" "+bonusAmt);
                                    } else if (bonusGraceOnAfter != null && !bonusGraceOnAfter.equals("") && bonusGraceOnAfter.equals("A") && currDate.getDate() <= bonusGraceValue /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                        //                                        txtBonusAmt.setText(txtBonusAmtAvail.getText());
                                        totBonusAmt = totBonusAmt + bonusAmt;
                                        bonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                    } else if (bonusGraceEnd != null && !bonusGraceEnd.equals("") && bonusGraceEnd.equals("E") /*
                                             * && pendingInst<noOfInstPay
                                             */) {
                                    } else {
                                        //                                        txtBonusAmt.setText(String.valueOf("0"));
                                          if (productMap.get("FORFEITE_HD_Y_N") != null && productMap.get("FORFEITE_HD_Y_N").equals("Y")) {
                                                ForfeitebonusAmountList.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                bonusAmountList.add(CommonUtil.convertObjToDouble(0));
                                                System.out.println("BonusAmount Yes23" + bonusAmt);
                                            }
                                        // 16-07-2020
                                            if(reversalBonusExists){
                                                bonusReversalLsit.add(CommonUtil.convertObjToDouble(bonusAmt));
                                                totReversalBonusAmt = totReversalBonusAmt + bonusAmt;
                                            }
                                            // End
                                    }
                                }
                            }
                            
                            
                            
                            System.out.println("totBonusAmtmmmm"+totBonusAmt);
                    System.out.println("bbb"+(productMap.get("BONUS_ROUNDING")!=null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt>0 ));
                    System.out.println("productMap.get()"+productMap.get("BONUS_ROUNDOFF"));
                            // +addNo added by Rajesh
                            HashMap instMap = new HashMap();
                            if (installmentMap.containsKey(String.valueOf(i + noOfInsPaid + addNo))) {
                                Rounding rod = new Rounding();
                                instMap = (HashMap) installmentMap.get(String.valueOf(i + noOfInsPaid + addNo));
                                //Added By Suresh
////                                if (productMap.get("BONUS_ROUNDING") != null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
////                                        && bonusAmt > 0) {
////                                    if (productMap.get("BONUS_ROUNDOFF") != null && productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")) {
////                                        bonusAmt = (double) rod.getNearest((long) (bonusAmt * 100), 100) / 100;
////                                    } else {
////                                        bonusAmt = (double) rod.lower((long) (bonusAmt * 100), 100) / 100;
////                                    }
////                                }
                                instMap.put("BONUS", String.valueOf(bonusAmt));
                                installmentMap.put(String.valueOf(i + noOfInsPaid + addNo), instMap);
                            }
                        }
                    }
                    bonusAmt = 0;
                }
                if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                    if (totBonusAmt == 0) {
                        for (int i = 0; i < noOfInstPay; i++) {
                            bonusAmountList.add(0.0);
                        }
                    }
                    if (penalRealList.isEmpty()) {
                        for (int i = 0; i < noOfInstPay; i++) {
                            penalRealList.add(0.0);
                        }
                    }
                }
                    System.out.println("totBonusAmtmmmm"+totBonusAmt);
                    System.out.println("bbb"+(productMap.get("BONUS_ROUNDING")!=null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt>0 ));
                    System.out.println("productMap.get()"+productMap.get("BONUS_ROUNDOFF"));
                    if(productMap.get("BONUS_ROUNDING")!=null && !productMap.get("BONUS_ROUNDING").equals("") && productMap.get("BONUS_ROUNDING").equals("Y")
                    && totBonusAmt>0 ){
                        Rounding rod =new Rounding();
                        if(productMap.get("BONUS_ROUNDOFF")!=null &&  productMap.get("BONUS_ROUNDOFF").equals("NEAREST_VALUE")){
                        totBonusAmt = (double)rod.getNearest((long)(totBonusAmt *100),100)/100;
                        }
                          else  if(productMap.get("BONUS_ROUNDOFF")!=null &&  productMap.get("BONUS_ROUNDOFF").equals("LOWER_VALUE")){
                            totBonusAmt = (double)rod.lower((long)(totBonusAmt *100),100)/100;
                        }
                       else
                        {
                             totBonusAmt=  Math.round(totBonusAmt * 100.0) / 100.0;
                             System.out.println("totBonusAmt "+totBonusAmt);
                             Double d=totBonusAmt;
                             String s=d.toString();
                           String s2=s.substring(s.indexOf('.')+1);
                           if(s2.length() >=2){
                             String s1=""+s2.charAt(1);
                             String s3=""+s2.charAt(0);
                         System.out.println("d "+d+"   s"+s+"   s2"+s2+" s1"+s1+" s3"+s3);
                             int num1=Integer.parseInt(s1);
                             int num= Integer.parseInt(s3);
                           if(num1>=5)
                           {
                              num+=1; 
                           }
                            s=s.substring(0, s.indexOf('.'))+"."+num+"0";
                           }else{
                               s=s+"0";
                           }
                           
                          
                            System.out.println("sss"+s);
                            totBonusAmt=Double.parseDouble(s);
//                             float number = (float)totBonusAmt; // you have this
//                             System.out.println("number++"+number);  
//                             int decimal = (int) number; // you have 12345
//                                float fractional = number - decimal ;// you have 0.6789
//                                System.out.println("fractionalssdas===s"+fractional+"..."+decimal);
//                                
//                                float z=fractional%10;
//                                System.out.println("zzz"+z);
//                                float y=10-z;
//                                if(z<y)
//                                {
//                                    fractional=fractional-z;
//                                    System.out.println("fractionalsas"+fractional);
//                                }
//                                else
//                                {
//                                    fractional=fractional+y;
//                                    System.out.println("fractionalsas..."+fractional);
//                                }
                                
                             
                        }
                       
                    }
                    
                    txtBonusAmt.setText(String.valueOf(totBonusAmt));
                    txtForfeitBonus.setText(String.valueOf(totReversalBonusAmt)); //2093
                    
                    txtNetAmtPaid.setText(txtInstAmt.getText());
                    txtInstPayable.setText(String.valueOf(
                    CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue() *
                    CommonUtil.convertObjToDouble(txtNoOfInstToPaay.getText()).doubleValue()-
                    CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()-
                    CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
                    
                    txtNetAmtPaid.setText(String.valueOf(CommonUtil.convertObjToDouble(txtInstPayable.getText()).doubleValue() //-
                    //CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue() -
                    //CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()
                    +
                    CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText()).doubleValue()+
                    CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue() +
                    CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()+ 
                    CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
                    transactionUI.cancelAction(false);
                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.resetObjects();
                    transactionUI.setCallingAmount(txtNetAmtPaid.getText());
            }else{
                ClientUtil.showAlertWindow("Exceeds The No Of Total Installment !!! ");
                transactionUI.cancelAction(false);
                transactionUI.setButtonEnableDisable(true);
                transactionUI.resetObjects();
                return;
            }
            System.out.println("!@!@!@! installmentMap:"+installmentMap);
            if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                int noOfInstToPay = CommonUtil.convertObjToInt(txtNoOfInstToPaay.getText());
                for (int i = 0; i < noOfInstToPay; i++) {
                    setNarrationToSplitTransaction(i);
                }
                if (bonusAmountList.size() != narrationList.size()) {
                    int count = CommonUtil.convertObjToInt(narrationList.size() - bonusAmountList.size());
                    for (int i = 0; i < count; i++) {
                        bonusAmountList.add(0);
                    }
                }
                double insAmta = 0.0;
                for (int h = 0; h < bonusAmountList.size(); h++) {
                    double instAmounts = CommonUtil.convertObjToDouble(txtInstAmt.getText());
                    instAmounts -= CommonUtil.convertObjToDouble(bonusAmountList.get(h));
                    insAmta = instAmounts;
                    instAmountList.add(insAmta);
                }
                if (instAmountList.size() != penalRealList.size()) {
                    int count = 0;
                    int insSize = instAmountList.size();
                    int penalSize = penalRealList.size();
                    count = insSize - penalSize;
                    for (int i = 0; i < count; i++) {
                        penalRealList.add(0);
                    }

                }
                Rounding rod = new Rounding();
                double excessAmount = 0;
                double totEx = 0.0;
                DecimalFormat df = new DecimalFormat("#.##");
                for (int r = 0; r < penalRealList.size(); r++) {
                    double roundVal = 0.0;
                    double realVal = 0.0;
                    realVal = CommonUtil.convertObjToDouble(penalRealList.get(r));
                    roundVal = rod.getNearest((CommonUtil.convertObjToDouble(penalRealList.get(r)) * 100), 100) / 100;
                    penalRealList.set(r, roundVal);
                    excessAmount = roundVal - realVal;
                    String excessAmounts = df.format(excessAmount);
                    totEx += CommonUtil.convertObjToDouble(excessAmounts);
                }
                if(penalRealList != null && penalRealList.size() >0){
                    penalRealList.set(penalRealList.size()-1, CommonUtil.convertObjToDouble(penalRealList.get(penalRealList.size()-1))-totEx);
                }
                copyList.clear();
                copyList = penalRealList;
                splitTransMap.put("BONUS_AMT_LIST", bonusAmountList);
                splitTransMap.put("INST_AMT_LIST", instAmountList);
                splitTransMap.put("PENAL_AMT_LIST", penalRealList);
                splitTransMap.put("NARRATION_LIST", narrationList);

                updateOBFields();
                splitTransMap.put("IS_SPLIT_MDS_TRANSACTION", isSplitMDSTransaction);
                observable.setMDSClosedTransListMap(splitTransMap);
            }
            if (forfeitChk != null && forfeitChk.equals("Y")) {
                observable.setInstallGraceDate(getLastGraceDate());
                System.out.println("InstallGraceDate  :: " + observable.getInstallGraceDate());
            }
            
            if(!isPenalEdit){
                splitTransMap.clear();
            }
            if (installmentMap == null || installmentMap.size() <= 0) {
                double bonusAmount= 0;
                for (int s = 1; s <= noOfInstPay; s++) {
                    whereMap = new HashMap();
                    whereMap.put("BONUS", String.valueOf(bonusAmount));
                    whereMap.put("INST_AMT", String.valueOf(txtInstAmt.getText()));
                    installmentMap.put(String.valueOf(s + noOfInsPaid), whereMap);
                }
            }
            System.out.println("!@!@!@! installmentMap:"+installmentMap);
            observable.setInstallmentMap(installmentMap);
            observable.setTxtGlobalPenalAmt(CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText()));
        }
    }//GEN-LAST:event_btnNoOfInstToPaayActionPerformed
        
    private Date setProperDtFormat(Date dt) {
        Date tempDt=(Date)curr_dt.clone();
        if(dt!=null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
        
        private void UIValueResetting(){
            txtSchemeName.setText("");
            txtChittalNo.setText("");
            lblMemberNameVal.setText("");
            lblMemberNo.setText("");
            txtDivisionNo.setText("");
            tdtChitStartDt.setDateValue("");
            tdtInsUptoPaid.setDateValue("");
            txtNoOfInst.setText("");
            txtCurrentInstNo.setText("");
            txtInstAmt.setText("");
            txtPendingInst.setText("");
            txtTotalInstAmt.setText("");
            txtBonusAmtAvail.setText("");
            rdoPrizedMember_Yes.setSelected(false);
            rdoPrizedMember_No.setSelected(false);
            txtNoticeAmt.setText("");
            txtAribitrationAmt.setText("");
            txtNoOfInstToPaay.setText("");
            txtInstPayable.setText("");
            txtPenalAmtPayable.setText("");
            txtBonusAmt.setText("");
            txtForfeitBonus.setText(""); //2093
            txtDiscountAmt.setText("");
            //        txtInterest.setText("");
            txtNetAmtPaid.setText("");
            
            tdtPaidDate.setDateValue("");
            txtPaidInst.setText("");
            txtPaidInstallments.setText("");
            txtPaidAmt.setText("");
            chkThalayal.setSelected(false);
            chkMunnal.setSelected(false);
            chkMemberChanged.setSelected(false);
            txtEarlierMember.setText("");
            txtEarlierMemberName.setText("");
            txtChangedInst.setText("");
            tdtChangedDate.setDateValue("");
            lblStatus.setText("");
            btnSchemeName.setEnabled(false);
            btnChittalNo.setEnabled(false);
            btnCancel.setEnabled(true);
        }
    private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
        // TODO add your handling code here:
        callView("CHITTAL_NO");
        setEnableDisable();
    }//GEN-LAST:event_btnChittalNoActionPerformed
    private void  setEnableDisable(){
        txtInstPayable.setText("");
        txtPenalAmtPayable.setText("");
        txtBonusAmt.setText("");
        txtForfeitBonus.setText(""); //2093
        txtDiscountAmt.setText("");
        ClientUtil.enableDisable(panChitDetails,false);
        txtPenalAmtPayable.setText("");
        txtNetAmtPaid.setText("");
        transactionUI.setCallingApplicantName(lblMemberNameVal.getText());
        transactionUI.setCallingTransType("CASH");
    }
    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        callView("SCHEME_DETAILS");
    }//GEN-LAST:event_btnSchemeNameActionPerformed
    
    private void btnNoOfInstToPaayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnNoOfInstToPaayFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnNoOfInstToPaayFocusLost
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquirystatus");
        btnCheck();
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        //        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnReject.setEnabled(true);
        btnView.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(false);
        
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        //        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        //        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //__ If there's no data to be Authorized, call Cancel action...
        if(!isModified()){
            setButtonEnableDisable();
            btnCancelActionPerformed(null);
        }
        observable.setResultStatus();
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(false);
        btnReject.setEnabled(false);
        btnView.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(false);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        setButtonEnableDisable();
        txtInstPayable.setEnabled(false);
        txtPenalAmtPayable.setEnabled(false);
        txtBonusAmt.setEnabled(false);
        txtForfeitBonus.setEnabled(false); //2093
        txtDiscountAmt.setEnabled(false);
        txtNetAmtPaid.setEnabled(false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setMdsWaiveAmt(0.0);
        txtNoOfInstToPaay.setEnabled(true);
        btnNoOfInstToPaay.setEnabled(true);
        ClientUtil.clearAll(this);
        //__ To Save the data in the Internal Frame...
        setModified(true);
        tdtTrnDate.setDateValue(DateUtil.getStringDate(curr_dt));
        txtSchemeName.setEnabled(true);
        txtChittalNo.setEnabled(true);
        btnChittalNo.setEnabled(true);
        btnSchemeName.setEnabled(true);
        lblMDSDescription.setText("");
        cbobranch.setEnabled(true);
        cbobranch.setSelectedItem(ProxyParameters.BRANCH_ID);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnReject.setEnabled(false);
        btnView.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(false);
        //        observable.existingData();
        
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        btnDelete.setEnabled(false);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        btnReject.setEnabled(false);
        btnView.setEnabled(false);
        btnNew.setEnabled(false);
        btnEdit.setEnabled(false);
        btnSave.setEnabled(true);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        btnSave.setEnabled(false);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW){
            String status = transactionUI.getCallingStatus();
            String transProdType = transactionUI.getCallingTransProdType();
            int transactionSize = 0 ;
            if(transactionUI.getOutputTO().size() == 0 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue() > 0){
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                return;
            }else {
                if(CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()>0){
                    transactionSize = (transactionUI.getOutputTO()).size();
                    if(transactionSize != 1 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()>0){
                        ClientUtil.showAlertWindow("Multiple Transactions are Not allowed, Make it one Transaction") ;
                        return;
                    }else{
                        observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                    }
                }else if(transactionUI.getOutputTO().size()>0){
                    observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
                }
            }
            if(transactionSize == 0 && CommonUtil.convertObjToDouble(txtInstAmt.getText()).doubleValue()>0){
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS)) ;
                return;
            }else if(transactionSize != 0 ){
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                }else{
                    
                }
            }
        }else{
            if(transactionUI.getOutputTO().size()>0){
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
        }
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSchemeNameDetails);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else {
            boolean interbranchFlag = false;
            if(transactionUI.getTransactionOB().getSelectedTxnType() != null && observable.getSelectedBranchID() != null){
                if(transactionUI.getTransactionOB().getSelectedTxnType().equals(CommonConstants.TX_TRANSFER)){
                    if(cbobranch.getSelectedItem().equals("")){
                       displayAlert("Please select branch code"); 
                       return;
                    }
                    if(ProxyParameters.BRANCH_ID.equals(cbobranch.getSelectedItem())){
                        interbranchFlag = false;
                    }else if(ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())){
                        interbranchFlag = false;
                    }else if(ProxyParameters.BRANCH_ID.equals(transactionUI.getTransactionOB().getSelectedTxnBranchId())){
                        interbranchFlag = false;
                    }else if(ProxyParameters.BRANCH_ID.equals(transactionUI.getTransactionOB().getSelectedTxnBranchId())){
                        interbranchFlag = false;
                    }else {
                        interbranchFlag = true;
                    }
                }else{
                    interbranchFlag = false;
                }
                if(interbranchFlag){
                    displayAlert("Incase of interbranch transaction either "+"\n"+"Dr or Cr account of the transaction should be of own branch");
                }else {//if(selectedMDSBranchDetails()){
                    savePerformed();
                    btnSave.setEnabled(false);
                }
            }
        }
        //__ Make the Screen Closable..
        setModified(false);
        TrueTransactMain.populateBranches();
        TrueTransactMain.selBranch = ProxyParameters.BRANCH_ID;
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
    }//GEN-LAST:event_btnSaveActionPerformed

    private boolean selectedMDSBranchDetails(){
        boolean flag = false;
        if(observable.getSelectedBranchID()!=null && !ProxyParameters.BRANCH_ID.equals(cbobranch.getSelectedItem())){
            Date selectedBranchDt = ClientUtil.getOtherBranchCurrentDate(CommonUtil.convertObjToStr(cbobranch.getSelectedItem()));
            Date currentDate = (Date) curr_dt.clone();
            System.out.println("selectedBranchDt : "+selectedBranchDt + " currentDate : "+currentDate);
            if(selectedBranchDt == null){
                ClientUtil.displayAlert("BOD is not completed for the selected branch " +"\n"+"Interbranch Transaction Not allowed");
                txtChittalNo.setText("");
            }else if(DateUtil.dateDiff(currentDate, selectedBranchDt)!=0){
                ClientUtil.displayAlert("Application Date is different in the Selected branch " +"\n"+"Interbranch Transaction Not allowed");
                txtChittalNo.setText("");
            }else {
                flag = true;
                System.out.println("Continue for interbranch trasactions ...");
            }
        }
        else{
            flag = true;
        }
        return flag;
    }

    private String periodLengthValidation(CTextField txtField, CComboBox comboField) {
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)){
            message = objMandatoryMRB.getString(txtField.getName());
        }
        return message;
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        deleteEditLock(txtChittalNo.getText());
        observable.resetStatus();
        observable.resetOBFields();
        super.removeEditLock(observable.getTxtChittalNo());
        UIValueResetting();
        //Following line moved down
        ClientUtil.enableDisable(this, false);
        ClientUtil.clearAll(this);
        viewType = "";
        btnDelete.setEnabled(true);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        btnSchemeName.setEnabled(false);
        btnChittalNo.setEnabled(false);
        btnCancel.setEnabled(true);
        lblValStreet.setText("");
        lblValArea.setText("");
        lblValCity.setText("");
        lblValState.setText("");
        lblValPin.setText("");
        btnNoOfInstToPaay.setEnabled(false);
        tdtTrnDate.setDateValue("");
        ClientUtil.enableDisable(this, false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        btnViewLedger.setEnabled(false);
        txtPenalAmtPayable.setText("");
        lblMDSDescription.setText("");
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
        }
        if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
        }
        bonusPrint = "";
        transactionUI.setMdsWaiveAmt(0.0);
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    public void authorize(HashMap map){
        map.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
        //        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        HashMap instMap = new HashMap();
        //        instMap.put("NO_OF_INST",txtNoOfInstToPaay.getText());
        observable.setInstMap(instMap);
        observable.setAuthorizeMap(map);
        try{
            if(transactionUI.getOutputTO().size()>0){
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doSave();
            if (observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED) {
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                } 
                if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("MDS Receipt Entry After Closure");
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        setModified(false);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
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
    
    public void authorizeStatus(String authorizeStatus) {
        ArrayList arrList = new ArrayList();
        HashMap authDataMap = new HashMap();
        final HashMap singleAuthorizeMap = new HashMap();
        if (viewType.equals(AUTHORIZE) && isFilled) {
            //Changed BY Suresh
//            String strWarnMsg = tabRemittanceProduct.isAllTabsVisited();
//            if (strWarnMsg.length() > 0){
//                displayAlert(strWarnMsg);
//                return;
//            }
//            strWarnMsg = null;
            authDataMap.put("TRANS_ID", observable.getTransId());
            arrList.add(authDataMap);
            singleAuthorizeMap.put("NET_TRANS_ID",observable.getTransId());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, ProxyParameters.BRANCH_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            //authorize(singleAuthorizeMap);
            //Added by sreekrishnan
            CommonUtil comm = new CommonUtil();
            final JDialog loading = comm.addProgressBar();
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws InterruptedException /** Execute some operation */
                {
                    try {
                        authorize(singleAuthorizeMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loading.dispose();
                }
            };
            worker.execute();
            loading.show();
            try {
                worker.get();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            super.setOpenForEditBy(observable.getStatusBy());
            //            super.removeEditLock(txtProductIdGR.getText());
            btnCancelActionPerformed(null);
            observable.setResult(observable.getActionType());
            //            btnCancelActionPerformed(null);
            observable.setResultStatus();
            viewType = "";
        } else {
            final HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("SELECTED_BRANCH_ID", getSelectedBranchID());
            whereMap.put("TODAY_DT", curr_dt.clone());
            whereMap.put("TRANS_DT", curr_dt.clone());
               whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if(TrueTransactMain.CASHIER_AUTH_ALLOWED!=null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y"))
            { 
             mapParam.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForCashierReceipt");
            }
            else
            mapParam.put(CommonConstants.MAP_NAME, "getSelectNonAuthRecordForReceipt");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "getSelectNonAuthRecordForReceipt");
            viewType = AUTHORIZE;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            //            setAuthBtnEnableDisable();
        }
    }
    private void btnSaveDisable(){
        btnSave.setEnabled(false);
        mitSave.setEnabled(false);
        btnCancel.setEnabled(true);
        mitCancel.setEnabled(true);
    }
    
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
    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed

    private void cbobranchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbobranchActionPerformed
        // TODO add your handling code here:
        txtSchemeName.setText("");
        observable.setTxtSchemeName("");
        txtChittalNo.setText("");
        observable.setTxtChittalNo("");
    }//GEN-LAST:event_cbobranchActionPerformed

    private void cbobranchFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbobranchFocusLost
        // TODO add your handling code here:
        txtSchemeName.setText("");
        observable.setTxtSchemeName("");
        txtChittalNo.setText("");
        observable.setTxtChittalNo("");
    }//GEN-LAST:event_cbobranchFocusLost

    private void btnWaiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWaiveActionPerformed
        System.out.println("product map :: " + observable.getProductMap());
        transactionUI.setMdsWaiveAmt(0.0);
        String penalWaiveOff = "N";
        String noticeWaiveOff = "N";
        String arcWaiveOff = "N";
        if(observable.getProductMap().containsKey("PENAL_WAIVE_OFF") && observable.getProductMap().get("PENAL_WAIVE_OFF") != null){
            penalWaiveOff = CommonUtil.convertObjToStr(observable.getProductMap().get("PENAL_WAIVE_OFF"));
        }
        if(observable.getProductMap().containsKey("NOTICE_WAIVE_OFF") && observable.getProductMap().get("NOTICE_WAIVE_OFF") != null){
            noticeWaiveOff = CommonUtil.convertObjToStr(observable.getProductMap().get("NOTICE_WAIVE_OFF"));
        }
        if(observable.getProductMap().containsKey("ARC_WAIVE_OFF") && observable.getProductMap().get("ARC_WAIVE_OFF") != null){
            arcWaiveOff = CommonUtil.convertObjToStr(observable.getProductMap().get("ARC_WAIVE_OFF"));
        } 
        
        if (CommonUtil.convertObjToDouble(txtInstPayable.getText()) > 0) {
            HashMap allMdsAmtMap = new HashMap();
            if(penalWaiveOff.equals("Y")){
              allMdsAmtMap.put("PENAL", CommonUtil.convertObjToDouble(txtPenalAmtPayable.getText()));
            }else{
              allMdsAmtMap.put("PENAL", 0.0);   
            }
            if(noticeWaiveOff.equals("Y")){
              allMdsAmtMap.put("NOTICE_AMOUNT", CommonUtil.convertObjToDouble(txtNoticeAmt.getText()));
            }else{
              allMdsAmtMap.put("NOTICE_AMOUNT", 0.0);  
            }
            if(arcWaiveOff.equals("Y")){
              allMdsAmtMap.put("ARC_AMOUNT", CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()));
            }else{
              allMdsAmtMap.put("ARC_AMOUNT", 0.0);  
            }
            if (allMdsAmtMap.containsKey("PENAL") || allMdsAmtMap.containsKey("NOTICE_AMOUNT") || allMdsAmtMap.containsKey("ARC_AMOUNT")) {
                showEditWaiveTableUI(allMdsAmtMap);
                observable.setReturnWaiveMap(waiveOffEditInterestAmt());
                observable.getReturnWaiveMap().put("CHITTAL_NO", txtChittalNo.getText());
                System.out.println("returnWaiveMap :: " + observable.getReturnWaiveMap());
            }
        }   
    }//GEN-LAST:event_btnWaiveActionPerformed

    private void txtNoOfInstToPaayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOfInstToPaayFocusLost
        // TODO add your handling code here:
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        txtInstPayable.setText("");
        txtPenalAmtPayable.setText("");
        txtNoticeAmt.setText("");
        txtAribitrationAmt.setText("");
        txtForfeitBonus.setText("");
        txtBonusAmt.setText("");
        txtNetAmtPaid.setText("");
        transactionUI.setCallingAmount(txtNetAmtPaid.getText());
    }//GEN-LAST:event_txtNoOfInstToPaayFocusLost

     private HashMap waiveOffEditInterestAmt() {
        double totalWaiveamt = 0;
        double editWaiveOffTransAmt = 0;
        HashMap resultWaiveMap = new HashMap();
        ArrayList singleList = new ArrayList();
        if (editableWaiveUI != null) {
            ArrayList list = editableWaiveUI.getTableData();
            for (int i = 0; i < list.size(); i++) {
                singleList = (ArrayList) list.get(i);
                totalWaiveamt += CommonUtil.convertObjToDouble(singleList.get(2));
                resultWaiveMap.put(singleList.get(0), singleList.get(2));
            }
        }
        resultWaiveMap.put("Total_WaiveAmt", CommonUtil.convertObjToStr(totalWaiveamt));
        transactionUI.setMdsWaiveAmt(totalWaiveamt);
        return resultWaiveMap;
    }
    
    
     public void showEditWaiveTableUI(HashMap allMdsAmtMap) {
        ArrayList singleList = new ArrayList();
        HashMap listMap = new HashMap();
        listMap.put("PENAL_AMOUNT", CommonUtil.convertObjToDouble(allMdsAmtMap.get("PENAL")));
        listMap.put("NOTICE_AMOUNT",CommonUtil.convertObjToDouble(allMdsAmtMap.get("NOTICE_AMOUNT")));
        listMap.put("ARC_AMOUNT",CommonUtil.convertObjToDouble(allMdsAmtMap.get("ARC_AMOUNT")));
        singleList.add(listMap);
        editableWaiveUI = new EditWaiveTableUI("MDS", listMap);
        editableWaiveUI.show();
//        TrueTransactMain.showScreen(editableUI);
    }
    
    
    
    /**
     * This method helps in popoualting the data from the data base
     *
     * @param currField Action the argument is passed according to the command
     * issued
     */
    private void callView(String currField) {
        updateOBFields();
        viewType = currField;
        //        final String PAYBRANCH = CommonUtil.convertObjToStr(((ComboBoxModel)(cboPayableBranchGR.getModel())).getKeyForSelected());
        
        //System.out.println("PAYBRANCH in CallView: " + PAYBRANCH);
        HashMap where = new HashMap();
        HashMap viewMap = new HashMap();
        if (currField.equals("Edit")|| currField.equals("Enquirystatus") || currField.equals("Delete")){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            ArrayList lst = new ArrayList();
            lst.add("CHITTAL_NO");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            where.put("INITIATED_BRANCH",ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getSelctReceiptEnteredDetails");
        } else if (currField.equalsIgnoreCase("SCHEME_DETAILS")) {
            HashMap datemap=new HashMap();
           // Date d=Date
            String branch=CommonUtil.convertObjToStr(cbobranch.getSelectedItem());
            datemap.put("BRANCH_CODE", branch);
            datemap.put("CURR_DATE",curr_dt);
            System.out.println("curr_dt.."+curr_dt);
              viewMap.put(CommonConstants.MAP_WHERE, datemap);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectClosedEachSchemeDetails");
        }else if (currField.equalsIgnoreCase("CHITTAL_NO")) {
            if(!txtSchemeName.getText().equals(""))
            {
            HashMap map1= new HashMap();
            HashMap mapTrans = new HashMap();
            map1.put("SCHEME_NAME", txtSchemeName.getText());
            String branch=CommonUtil.convertObjToStr(cbobranch.getSelectedItem());
            if(branch != null && branch.length()>0 && !branch.equals(ProxyParameters.BRANCH_ID)){
                where.put("BRANCH_CODE", branch);
            }                        
            List list1= ClientUtil.executeQuery("getSelectTransDet", map1);
            if(list1!=null && list1.size()>0){
                        mapTrans = (HashMap) list1.get(0);
                        if(CommonUtil.convertObjToStr(mapTrans.get("TRANS_FIRST_INSTALLMENT")).equals("Y")){
            where.put("SCHEME_NAMES",txtSchemeName.getText());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetails");
                        }
                        else
                        {
                           where.put("SCHEME_NAMES",txtSchemeName.getText());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getSelectChittalReceiptEntryDetailsN");  
                        }
        }
        }
            else
            {
                viewMap.put(CommonConstants.MAP_NAME, "getSelectAllClosedChittalReceiptEntryDetails"); 
            }
        }
        new ViewAll(this, viewMap).show();
    
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param obj param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        System.out.println("hashhashhashhashhash"+hash);
        HashMap schemeMap = new HashMap();
        if (hash.containsKey("SCHEME_NAME") && hash.get("SCHEME_NAME") != null) {
            schemeMap.put("SCHEME_NAME", CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
            List lst = ClientUtil.executeQuery("getSelectSchemeAcctHead", schemeMap);
            if (lst != null && lst.size() > 0) {
                schemeMap = (HashMap) lst.get(0);
            }
        }
        if (schemeMap != null && schemeMap.containsKey("IS_SPLIT_MDS_TRANSACTION") && schemeMap.get("IS_SPLIT_MDS_TRANSACTION") != null) {
            isSplitMDSTransaction = CommonUtil.convertObjToStr(schemeMap.get("IS_SPLIT_MDS_TRANSACTION"));
        }
        if (schemeMap != null && schemeMap.containsKey("BONUS_PRINT") && schemeMap.get("BONUS_PRINT") != null) {
            bonusPrint = CommonUtil.convertObjToStr(schemeMap.get("BONUS_PRINT"));
        }
        
        if (hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
            fromNewAuthorizeUI = true;
            newauthorizeListUI = (NewAuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnSaveDisable();
            btnReject.setEnabled(false);           
        }
        if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
            fromAuthorizeUI = true;
            authorizeListUI = (AuthorizeListUI) hash.get("PARENT");
            hash.remove("PARENT");
            viewType = AUTHORIZE;
            observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
            observable.setStatus();
            btnSaveDisable();
        }
        if (hash.containsKey("FROM_SMART_CUSTOMER_UI")) {
            System.out.println("HASH DATE ======innnn" + hash);
            fromSmartCustUI= true;
            smartUI = (SmartCustomerUI) hash.get("PARENT");
            hash.remove("PARENT");
            btnNewActionPerformed(null);            
            txtSchemeName.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
            txtSchemeNameFocusLost(null);            
            txtChittalNo.setText(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
            txtChittalNoFocusLost(null);
            observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
            observable.setStatus();
              //btnSaveDisable();
        }
        HashMap hashMap = new HashMap();
        int remitProduBranchRow = 0;
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals(AUTHORIZE) ||  viewType.equals("Enquirystatus") || viewType.equals("Delete") ) {
                isFilled = true;
//                hash.put(CommonConstants.MAP_WHERE, hash.get("PRODUCT ID"));
//                observable.checkAcNoWithoutProdType(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                lblMDSDescription.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_DESC")));
                hash.put("VOUCHER_DETAILS","VOUCHER_DETAILS");
                if (isSplitMDSTransaction != null && isSplitMDSTransaction.equals("Y")) {
                    hash.put("isSplitMDSTransaction", isSplitMDSTransaction);
                }
                if (observable.populateData(hash)) {
                    
                }
                if (viewType.equals("Edit") || viewType.equals("Enquirystatus")) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    System.out.println("#$#$$ yesNo : "+yesNo);
                    if (yesNo==0) {
                        TTIntegration ttIntgration = null;
                        HashMap paramMap = new HashMap();
                        paramMap.put("TransId", hash.get("NET_TRANS_ID"));
                        paramMap.put("TransDt", curr_dt);
                        paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        ttIntgration.setParam(paramMap);
                        ttIntgration.integrationForPrint("MDSReceipts", false);
                    }
                }
                tdtTrnDate.setDateValue(DateUtil.getStringDate(curr_dt));
                if ( viewType.equals("Delete") || viewType.equals(AUTHORIZE)|| viewType.equals("Enquirystatus")) {
                    setButtonEnableDisable();
                    ClientUtil.enableDisable(this, false);
                    transactionUI.setMainEnableDisable(false);
                }
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    btnNoOfInstToPaay.setEnabled(false);
                }
                hashMap.put("CHITTAL_NO",hash.get("CHITTAL_NO"));
                hashMap.put("SUB_NO",CommonUtil.convertObjToInt(hash.get("SUB_NO")));
                List lst=ClientUtil.executeQuery("getMemberAddressDetails", hashMap);
                if(lst!=null && lst.size()>0){
                    addressMap = (HashMap) lst.get(0);
                    populateAddressData(addressMap);
                }
            }else if (viewType.equalsIgnoreCase("SCHEME_DETAILS")) {
                if(txtChittalNo.getText().length() > 0){
                    deleteEditLock(txtChittalNo.getText());
                }
                observable.resetOBFields();
                transactionUI.setMdsWaiveAmt(0.0);
                UIValueResetting();
                if(hash.containsKey("MULTIPLE_MEMBER") && hash.get("MULTIPLE_MEMBER").equals("Y") && !hash.get("MULTIPLE_MEMBER").equals("") ){
                    observable.setMultipleMember("Y");
//                    txtDivisionNo.setText("1");
//                    observable.setTxtDivisionNo("1");
                }else{
                    observable.setMultipleMember("N");
                }
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                txtSchemeName.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                lblMDSDescription.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_DESC")));
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && txtSchemeName.getText().length() > 0) {
                    String branch = CommonUtil.convertObjToStr(cbobranch.getSelectedItem());
                    txtChittalNo.setText(CommonUtil.convertObjToStr(branch + txtSchemeName.getText()));
                    observable.setTxtChittalNo(CommonUtil.convertObjToStr(branch + txtSchemeName.getText()));
                } else {
                    txtChittalNo.setText("");
                }
                btnSchemeName.setEnabled(true);
                btnChittalNo.setEnabled(true);
                List closureList = ClientUtil.executeQuery("checkSchemeClosureDetails", hash);
                hash.put("CUR_DT", curr_dt);
                //List endList = ClientUtil.executeQuery("checkSchemeEndDate", hash);
                if(closureList!=null && closureList.size()>0){
                    observable.setClosureDetails(true);
//                  ClientUtil.showMessageWindow("This Scheme is Closed !!!");
                }
                //else if(endList!=null && endList.size()>0){
                //    observable.setClosureDetails(true);
                //}
                else{
                    observable.setClosureDetails(false);
                }
            }else if (viewType.equalsIgnoreCase("CHITTAL_NO")) {
               
               
                observable.resetOBFields();
                whenTableRowSelected(hash);
                transactionUI.setMdsWaiveAmt(0.0);
                System.out.println("hashhh"+hash);
                hash.put("SUB_NO",CommonUtil.convertObjToInt(hash.get("SUB_NO")));
//               List defaulterList=ClientUtil.executeQuery("getDefaulterDts", hash);
//               if(defaulterList!=null && defaulterList.size()>0)
//               {
//                   HashMap aMpa=(HashMap)defaulterList.get(0);
//                   if(aMpa.get("DEFAULTER")!=null && !aMpa.get("DEFAULTER").toString().equals("") && aMpa.get("DEFAULTER").toString().equals("Y"))
//                   {
//                        ClientUtil.showMessageWindow(" The Chittal is marked as Defaulter cannot make receipt entry  !!! ");
//                       btnCancelActionPerformed(null);
//                       return;
//                   }
//                   
//               }
                List pendingAuthlst=ClientUtil.executeQuery("checkPendingForAuthorization", hash);
                if(pendingAuthlst!=null && pendingAuthlst.size()>0){
                    ClientUtil.showMessageWindow(" There is a Pending Transaction for this Chittal...Please Authorize OR Reject first  !!! ");
                    deleteEditLock(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                    btnCancelActionPerformed(null);
                }else{
                txtSchemeName.setText(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                observable.setTxtSchemeName(CommonUtil.convertObjToStr(hash.get("SCHEME_NAME")));
                observable.setTxtChittalNo(CommonUtil.convertObjToStr(hash.get("CHITTAL_NO")));
                observable.setSelectedBranchID(CommonUtil.convertObjToStr(hash.get("BRANCH_CODE")));
                txtSubNo.setText(CommonUtil.convertObjToStr(hash.get("SUB_NO")));
                observable.setTxtSubNo(CommonUtil.convertObjToStr(hash.get("SUB_NO")));
                observable.setReceiptDetails(hash);
                txtDivisionNo.setEnabled(false);
                tdtChitStartDt.setEnabled(false);
                txtNoOfInst.setEnabled(false);
                txtCurrentInstNo.setEnabled(false);
                txtInstAmt.setEnabled(false);
                txtPendingInst.setEnabled(false);
                txtTotalInstAmt.setEnabled(false);
                if(CommonUtil.convertObjToStr(hash.get("SCHEME_START_DT"))!=null)
                {
                    tdtInstDt.setDateValue(CommonUtil.convertObjToStr(hash.get("SCHEME_START_DT")));
                }
                populateChittalDetails();
                observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hash.get("CUST_ID")));
                txtBonusAmtAvail.setEnabled(true);
                tdtInstDt.setEnabled(false);
                rdoBankPay_No.setSelected(true);
                observable.setRdoBankPay_No(true);
                observable.getCustomerAddressDetails();
                lblValStreet.setText(observable.getLblHouseStNo());
                lblValArea.setText(observable.getLblArea());
                lblValCity.setText(observable.getLblCity());
                lblValState.setText(observable.getLblState());
                lblValPin.setText(observable.getLblpin());
                txtSchemeName.setEnabled(false);
                txtChittalNo.setEnabled(false);
                //btnChittalNo.setEnabled(false);
                //btnSchemeName.setEnabled(false);
                //Added By Suresh
                if(CommonUtil.convertObjToStr(CommonConstants.SAL_REC_MODULE).equals("Y")){
                    HashMap whereMap = new HashMap();
                    whereMap.put("SCHEME_NAME",txtSchemeName.getText());
                    whereMap.put("CHITTAL_NO",txtChittalNo.getText());
                    whereMap.put("SUB_NO",CommonUtil.convertObjToInt(txtSubNo.getText()));
                    List masterLst=ClientUtil.executeQuery("getSelectMDSMasterData", whereMap);
                    if(masterLst!=null && masterLst.size()>0){
                        whereMap = (HashMap) masterLst.get(0);
                        if(CommonUtil.convertObjToStr(whereMap.get("LOCK_STATUS")).equals("Y")){
                            ClientUtil.showMessageWindow("This Chittal No is Locked... Can not Proceed... !!!");
                            btnCancelActionPerformed(null);
                            return;
                            }
                        }
                    }
                }
            }
            if (!viewType.equalsIgnoreCase("SCHEME_DETAILS")) {
                btnViewLedger.setEnabled(true);
            }else{
                btnViewLedger.setEnabled(false);
            }
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        if(hash.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        if(hash.containsKey("FROM_AUTHORIZE_LIST_UI")){
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
            btnAuthorize.setEnabled(true);
            btnReject.setEnabled(true);
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    
    private void populateAddressData(HashMap addressMap){
        lblValStreet.setText(CommonUtil.convertObjToStr(addressMap.get("HOUSE_ST")));
        lblValArea.setText(CommonUtil.convertObjToStr(addressMap.get("AREA")));
        lblValCity.setText(CommonUtil.convertObjToStr(addressMap.get("CITY")));
        lblValState.setText(CommonUtil.convertObjToStr(addressMap.get("STATE")));
        lblValPin.setText(CommonUtil.convertObjToStr(addressMap.get("PIN")));
    }
    
    
    private void savePerformed(){
        updateOBFields();
        boolean productID = false;
        // If the Action Type is  NEW or EDIT, Check for the Uniqueness of Product ID and Product Description
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        //System.out.println("productID : " + productID);
        //added by rishad for avoiding doubling issue at 05/08/2015
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException /** Execute some operation */
            {
                try {
                    observable.doSave();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (observable.getProxyReturnMap()!=null) {
             btnSave.setEnabled(false);
         }
        //__ if the Action is not Falied, Reset the fields...
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("PRODUCT ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getProxyReturnMap()!=null) {
                if (observable.getProxyReturnMap().containsKey("PRODUCT ID")) {
                    lockMap.put("PRODUCT ID", observable.getTxtChittalNo());
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                    int yesNo = 0;
                    String[] options = {"Yes", "No"};
                    yesNo = COptionPane.showOptionDialog(null,"Do you want to print?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
                    System.out.println("#$#$$ yesNo : "+yesNo);
                    if (yesNo==0) {
                        TTIntegration ttIntgration = null;
                        Object keys[] = observable.getProxyReturnMap().keySet().toArray();
                        
                        
                        
                        
                          int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        HashMap transIdMap = new HashMap();
      HashMap  Pmap=observable.getProxyReturnMap();
                        System.out.println("ppppp"+Pmap);
        for (int i=0; i<keys.length; i++) {
            if (Pmap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List)Pmap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
//                        transId = (String)transMap.get("TRANS_ID");
//                        cashId = transId;
                    }
////                    cashDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
//                    "   Trans Type : "+transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
//                        cashDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
//                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
//                        cashDisplayStr += "   Account Head : "+transMap.get("AC_HD_ID")+
//                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                cashCount++;
            } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER")!=-1) {
                for (int j=0; j<tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j==0) {
//                        transId = (String)transMap.get("BATCH_ID");
                    }
//                    transferDisplayStr += "Trans Id : "+transMap.get("TRANS_ID")+
//                    "   Batch Id : "+transMap.get("BATCH_ID")+
//                    "   Trans Type : "+transMap.get("TRANS_TYPE");
//                    actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                    if(actNum != null && !actNum.equals("")){
//                        transferDisplayStr +="   Account No : "+transMap.get("ACT_NUM")+
//                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }else{
//                        transferDisplayStr += "   Account Head : "+transMap.get("AC_HD_ID")+
//                        "   Amount : "+transMap.get("AMOUNT")+"\n";
                    }
                }
                transferCount++;
            }
        }
                        
                        
                        System.out.println("transferCount"+transferCount+""+cashCount);     
                        
//                        
                        
                        
                        
//                        int cashCount = 0;
//                        int transferCount = 0;
                         tempList = null;
                     transMap = null;
                        String transID = "";
                        for (int i=0; i<keys.length; i++) {
                            tempList = (List)observable.getProxyReturnMap().get(keys[i]);
                            System.out.println("tempList..."+tempList);
                            System.out.println("keys...."+keys[i]+"LLLLL"+CommonUtil.convertObjToStr(keys[i]).indexOf("NET")+";;;;"+CommonUtil.convertObjToStr(keys[i]));
//                            if (CommonUtil.convertObjToStr(keys[i]).indexOf("NET")!=-1) {
                                for (int j=0; j<tempList.size(); j++) {
                                    transMap = (HashMap) tempList.get(j);
                                    System.out.println("transMap"+transMap);
//                                    if (transMap.get("BATCH_ID")!=null) { // Commented and added new lines by nithya on 25-09-2020 for KD-2285
//                                        transID = CommonUtil.convertObjToStr(transMap.get("BATCH_ID"));
//                                        System.out.println("transIDiinn"+transID);
//                                    } else {
//                                        transID = CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
//                                        System.out.println("transIDouttt"+transID);
//                                    }                                    
                                    if(transMap.containsKey("SINGLE_TRANS_ID") && transMap.get("SINGLE_TRANS_ID") != null){
                                        transID = CommonUtil.convertObjToStr(transMap.get("SINGLE_TRANS_ID"));
                                    }
                                    
                                }
//                            }
                        }
                        lockMap.put("TransId", transID);
                        lockMap.put("TransDt", curr_dt);
                        lockMap.put("BranchId", ProxyParameters.BRANCH_ID);
                        ttIntgration.setParam(lockMap);
                        //                        if (((String)TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase().lastIndexOf("POLPULLY")!=-1) {
                        //                            ttIntgration.integrationForPrint("ReceiptPayment");
                        //                        } else {
                       
                       
                        if(cashCount>0)
                        {  System.out.println("outttt   ");
                            ttIntgration.integrationForPrint("MDSReceipts", false);
                            if (transferCount > 0) {
                                    if(bonusPrint!=null && !bonusPrint.equals("") && bonusPrint.equalsIgnoreCase("Y")){                                        
                                        ttIntgration.setParam(lockMap);
                                        ttIntgration.integrationForPrint("MDSReceiptsTransfer", false);
                                    }
                                }
                        }else if(transferCount>0)
                        {
                           System.out.println("innnn");
                           ttIntgration.integrationForPrint("MDSReceiptsTransfer", false); 
                        }
                        //                        }
                    }
                }
                
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                lockMap.put("PRODUCT ID", observable.getTxtChittalNo());
            }
            System.out.println("%$@#%$@#$@ lockMap : "+lockMap);
            setEditLockMap(lockMap);
            setEditLock();
            deleteEditLock(txtChittalNo.getText());
            observable.resetOBFields();
            ClientUtil.enableDisable(this, false);
            if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
                observable.setResult(observable.getActionType());
            }
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            setButtonEnableDisable();
            btnCancelActionPerformed(null);
            if(fromSmartCustUI){
                System.out.println("fromSmartCustUI#%#%"+fromSmartCustUI);
                this.dispose();
                fromSmartCustUI = false;
            }
        }
        isPenalEdit = true;
    }
    
    private void enableDisableall(){
        observable.resetOBFields();
        setButtonEnableDisable();
        ClientUtil.enableDisable(this, false);
    }
    private void populateChittalDetails(){
        txtChittalNo.setText(observable.getTxtChittalNo());
        lblMemberNameVal.setText(observable.getLblMemberNameVal());
        lblMemberNo.setText(observable.getLblMemberNo());
        txtDivisionNo.setText(observable.getTxtDivisionNo());
        tdtChitStartDt.setDateValue(observable.getTdtChitStartDt());
        tdtInsUptoPaid.setDateValue(observable.getTdtInsUptoPaid());
       // tdtInstDt.setDateValue(observable.ge
        tdtInstDt.setDateValue(observable.getTdtChitEndDt());
        txtNoOfInst.setText(observable.getTxtNoOfInst());
        txtCurrentInstNo.setText(observable.getTxtCurrentInstNo());
        txtInstAmt.setText(observable.getTxtInstAmt());
        txtPendingInst.setText(observable.getTxtPendingInst());
        txtTotalInstAmt.setText(observable.getTxtTotalInstAmt());
        tdtPaidDate.setDateValue(observable.getTdtPaidDate());
        txtPaidInst.setText(observable.getTxtPaidInst());
        txtPaidAmt.setText(observable.getTxtPaidAmt());
        chkThalayal.setSelected(observable.getChkThalayal());
        chkMunnal.setSelected(observable.getChkThalayal());
        txtEarlierMember.setText(observable.getTxtEarlierMember());
        txtEarlierMemberName.setText(observable.getTxtEarlierMemberName());
        txtChangedInst.setText(observable.getTxtChangedInst());
        tdtChangedDate.setDateValue(observable.getTdtChangedDate());
        // Added by nithya for 0003756 on 18.02.2016
        if(observable.getIsStandingInstructionSet().equalsIgnoreCase("Y")){
            ClientUtil.showMessageWindow("Standing Instruction set for this chittal");
        }
        // End
    }
    
    /** To Enable or Disable New, Edit, Save and Cancel Button */
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        btnSave.setEnabled(!btnNew.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
        setDelBtnEnableDisable(false);
        setAuthBtnEnableDisable();
    }
    
    /** To Enable or Disable Delete Button */
    private void setDelBtnEnableDisable(boolean enableDisable){
        btnDelete.setEnabled(enableDisable );
        mitDelete.setEnabled(enableDisable);
    }
    
    /** To Enable or Disable Authorize, Rejection and Exception Button */
    private void setAuthBtnEnableDisable(){
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNoOfInstToPaay;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewLedger;
    private com.see.truetransact.uicomponent.CButton btnWaive;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cbobranch;
    private com.see.truetransact.uicomponent.CCheckBox chkMemberChanged;
    private com.see.truetransact.uicomponent.CCheckBox chkMunnal;
    private com.see.truetransact.uicomponent.CCheckBox chkThalayal;
    private com.see.truetransact.uicomponent.CLabel lblArea2;
    private com.see.truetransact.uicomponent.CLabel lblAribitrationAmt;
    private com.see.truetransact.uicomponent.CLabel lblBonusAmt;
    private com.see.truetransact.uicomponent.CLabel lblBonusAmtAvail;
    private com.see.truetransact.uicomponent.CLabel lblChangedDate;
    private com.see.truetransact.uicomponent.CLabel lblChangedInst;
    private com.see.truetransact.uicomponent.CLabel lblChitEndDt6;
    private com.see.truetransact.uicomponent.CLabel lblChitStartDt;
    private com.see.truetransact.uicomponent.CLabel lblChitStartDt1;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblCity2;
    private com.see.truetransact.uicomponent.CLabel lblCurrentInstNo;
    private com.see.truetransact.uicomponent.CLabel lblDiscountAmt;
    private com.see.truetransact.uicomponent.CLabel lblDivisionNo;
    private com.see.truetransact.uicomponent.CLabel lblEarlierMember;
    private com.see.truetransact.uicomponent.CLabel lblEarlierMemberName;
    private com.see.truetransact.uicomponent.CLabel lblInsUptoPaid;
    private com.see.truetransact.uicomponent.CLabel lblInstAmt;
    private com.see.truetransact.uicomponent.CLabel lblInstPayable;
    private com.see.truetransact.uicomponent.CLabel lblMDSDescription;
    private com.see.truetransact.uicomponent.CLabel lblMemberChanged;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMunnal;
    private com.see.truetransact.uicomponent.CLabel lblNetAmtPaid;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInst;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstToPaay;
    private com.see.truetransact.uicomponent.CLabel lblNoticeAmt;
    private com.see.truetransact.uicomponent.CLabel lblPaidAmt;
    private com.see.truetransact.uicomponent.CLabel lblPaidDate;
    private com.see.truetransact.uicomponent.CLabel lblPaidInst;
    private com.see.truetransact.uicomponent.CLabel lblPaidInstallments;
    private com.see.truetransact.uicomponent.CLabel lblPenalAmtPayable;
    private com.see.truetransact.uicomponent.CLabel lblPendingInst;
    private com.see.truetransact.uicomponent.CLabel lblPin2;
    private com.see.truetransact.uicomponent.CLabel lblPrizedBlueClr_Yes;
    private com.see.truetransact.uicomponent.CLabel lblPrizedMember;
    private com.see.truetransact.uicomponent.CLabel lblPrizedMember1;
    private com.see.truetransact.uicomponent.CLabel lblPrizedRedClr_No;
    private com.see.truetransact.uicomponent.CLabel lblSchemeDescription;
    private com.see.truetransact.uicomponent.CLabel lblSchemeName;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace41;
    private com.see.truetransact.uicomponent.CLabel lblSpace42;
    private com.see.truetransact.uicomponent.CLabel lblSpace43;
    private com.see.truetransact.uicomponent.CLabel lblSpace44;
    private com.see.truetransact.uicomponent.CLabel lblSpace45;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblState2;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet2;
    private com.see.truetransact.uicomponent.CLabel lblThalayal;
    private com.see.truetransact.uicomponent.CLabel lblTotalInstAmt;
    private com.see.truetransact.uicomponent.CLabel lblTrnDate;
    private com.see.truetransact.uicomponent.CLabel lblValArea;
    private com.see.truetransact.uicomponent.CLabel lblValCity;
    private com.see.truetransact.uicomponent.CLabel lblValPin;
    private com.see.truetransact.uicomponent.CLabel lblValState;
    private com.see.truetransact.uicomponent.CLabel lblValStreet;
    private com.see.truetransact.uicomponent.CLabel lblbranch;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitAuthorize;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitException;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitReject;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panChitDetails;
    private com.see.truetransact.uicomponent.CPanel panChittalNo;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriodDetails1;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriodDetails2;
    private com.see.truetransact.uicomponent.CPanel panGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panGeneralRemittance1;
    private com.see.truetransact.uicomponent.CPanel panInsDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance1;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance2;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance3;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance4;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance5;
    private com.see.truetransact.uicomponent.CPanel panInstallmentToPay;
    private com.see.truetransact.uicomponent.CPanel panLoanDetails;
    private com.see.truetransact.uicomponent.CPanel panLoanDetailsTable;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberNo_Name;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panPendingInstallmentDetails;
    private com.see.truetransact.uicomponent.CPanel panPrizedDetails;
    private com.see.truetransact.uicomponent.CPanel panPrizedMember;
    private com.see.truetransact.uicomponent.CPanel panPrizedMember1;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panSchemeNameDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable2_SD;
    private com.see.truetransact.uicomponent.CPanel panTransactionDetails;
    private com.see.truetransact.uicomponent.CPanel panTransactionTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBankAdvance;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrizeMember;
    private com.see.truetransact.uicomponent.CRadioButton rdoBankPay_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBankPay_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrizedMember_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrizedMember_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpLoanDetailsTable;
    private com.see.truetransact.uicomponent.CScrollPane srpOtherDetailsTable;
    private com.see.truetransact.uicomponent.CScrollPane srpTransactionTable;
    private com.see.truetransact.uicomponent.CTabbedPane tabRemittanceProduct;
    private com.see.truetransact.uicomponent.CTable tblLoanDetailsTable;
    private com.see.truetransact.uicomponent.CTable tblOtherDetailsTable;
    private com.see.truetransact.uicomponent.CTable tblTransactionTable;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtChangedDate;
    private com.see.truetransact.uicomponent.CDateField tdtChitStartDt;
    private com.see.truetransact.uicomponent.CDateField tdtInsUptoPaid;
    private com.see.truetransact.uicomponent.CDateField tdtInstDt;
    private com.see.truetransact.uicomponent.CDateField tdtPaidDate;
    private com.see.truetransact.uicomponent.CDateField tdtTrnDate;
    private com.see.truetransact.uicomponent.CTextField txtAribitrationAmt;
    private com.see.truetransact.uicomponent.CTextField txtBonusAmt;
    private com.see.truetransact.uicomponent.CTextField txtBonusAmtAvail;
    private com.see.truetransact.uicomponent.CTextField txtChangedInst;
    private com.see.truetransact.uicomponent.CTextField txtChittalNo;
    private com.see.truetransact.uicomponent.CTextField txtCurrentInstNo;
    private com.see.truetransact.uicomponent.CTextField txtDiscountAmt;
    private com.see.truetransact.uicomponent.CTextField txtDivisionNo;
    private com.see.truetransact.uicomponent.CTextField txtEarlierMember;
    private com.see.truetransact.uicomponent.CTextField txtEarlierMemberName;
    private com.see.truetransact.uicomponent.CTextField txtForfeitBonus;
    private com.see.truetransact.uicomponent.CTextField txtInstAmt;
    private com.see.truetransact.uicomponent.CTextField txtInstPayable;
    private com.see.truetransact.uicomponent.CTextField txtNetAmtPaid;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInst;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstToPaay;
    private com.see.truetransact.uicomponent.CTextField txtNoticeAmt;
    private com.see.truetransact.uicomponent.CTextField txtPaidAmt;
    private com.see.truetransact.uicomponent.CTextField txtPaidInst;
    private com.see.truetransact.uicomponent.CTextField txtPaidInstallments;
    private com.see.truetransact.uicomponent.CTextField txtPenalAmtPayable;
    private com.see.truetransact.uicomponent.CTextField txtPendingInst;
    private com.see.truetransact.uicomponent.CTextField txtSchemeName;
    private com.see.truetransact.uicomponent.CTextField txtSubNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalInstAmt;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MDSClosedReciptUI gui = new MDSClosedReciptUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
        
    }
    private void setNarrationToSplitTransaction(int i) {
        String narration = "";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM/yyyy");
        int paidInstallments = CommonUtil.convertObjToInt(txtPaidInstallments.getText());
        int paidInst = paidInstallments + 1;
        paidInstallments += i;
        paidInst += i;
        narration = "Inst#" + (paidInst);
        Date dt1 = DateUtil.addDays(DateUtil.getDateMMDDYYYY(observable.getTdtChitStartDt()), 30 * paidInstallments);
//        Date dt = DateUtil.addDays(DateUtil.getDateMMDDYYYY(observable.getTdtChitStartDt()), 30 * (1));
        narration += " " + sdf.format(dt1);
        narrationList.add(narration);
    }
    
    
     private HashMap getLastGraceDate() {
        HashMap graceCalculateMap = new HashMap();
        HashMap graceMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("SCHEME_NAME", observable.getTxtSchemeName());
        whereMap.put("DIVISION_NO", CommonUtil.convertObjToInt(observable.getTxtDivisionNo()));
        whereMap.put("INSTALLMENT_NO", CommonUtil.convertObjToInt(txtCurrentInstNo.getText()));
        List lst = ClientUtil.executeQuery("getSelectInstUptoPaid", whereMap);
        if (lst != null && lst.size() > 0) {
            whereMap = (HashMap) lst.get(0);
        }
        Date nextInstlDate = DateUtil.getDateMMDDYYYY(tdtInstDt.getDateValue());
        Calendar c = Calendar.getInstance();
        long dateDiff = DateUtil.dateDiff(curr_dt, nextInstlDate);
        graceMap.put("DATE_DIFF", dateDiff);
        graceCalculateMap.put("DATE_DIFF", dateDiff);
        HashMap forFeiteMap = new HashMap();
        forFeiteMap.put("SCHEME_NAME", observable.getTxtSchemeName());
        List forFeiteLst = null;
        if (whereMap != null && whereMap.get("PROD_ID") != null && !whereMap.get("PROD_ID").equals("")) {
            forFeiteMap.put("PROD_ID", whereMap.get("PROD_ID"));
            forFeiteLst = ClientUtil.executeQuery("getSelectForFeiteHead", forFeiteMap);
            if (forFeiteLst != null && forFeiteLst.size() > 0) {
                forFeiteMap = (HashMap) forFeiteLst.get(0);
                System.out.println("MAPS" + forFeiteMap);
                graceMap.put("FORFEITED_HEAD", forFeiteMap.get("MDS_FORFEITED_ACCT_HEAD"));
                graceCalculateMap.put("FORFEITED_HEAD", forFeiteMap.get("MDS_FORFEITED_ACCT_HEAD"));
                graceCalculateMap.put("NEXT_INSTALLMENT_DATE", nextInstlDate);
                System.out.println("gracemap :" + graceMap);
            }
        }
        lst.clear();
        whereMap.clear();
        forFeiteMap.clear();
        forFeiteLst.clear();
        lst = null;
        whereMap = null;
        forFeiteMap = null;
        forFeiteLst = null;
        graceCalculateMap.put("BONUS_AMOUNT_LIST", bonusAmountList);
        graceCalculateMap.put("FORFEITE_BONUS_AMOUNT_LIST", ForfeitebonusAmountList);
        System.out.println("GRace" + graceCalculateMap);
        return graceCalculateMap;
    }
    
      private void whenTableRowSelected(HashMap paramMap) {
         
        if(txtChittalNo.getText().length() > 0){
            deleteEditLock(txtChittalNo.getText());
        } 
         
        String lockedBy = "";
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", paramMap.get("CHITTAL_NO"));
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", curr_dt.clone());
        System.out.println("Record Key Map : " + map);
        List lstLock = ClientUtil.executeQuery("selectEditLock", map);
        if (lstLock.size() > 0) {
            lockedBy = CommonUtil.convertObjToStr(lstLock.get(0));
            if (!lockedBy.equals(ProxyParameters.USER_ID)) {
                //                            setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
                btnSave.setEnabled(false);
            } else {
                //                            setMode(ClientConstants.ACTIONTYPE_EDIT);
                btnSave.setEnabled(true);
            }
        } else {
            //                        setMode(ClientConstants.ACTIONTYPE_EDIT);
            btnSave.setEnabled(true);
        }
        setOpenForEditBy(lockedBy);
        if (lockedBy.equals(""))
            ClientUtil.execute("insertEditLock", map);
        if (lockedBy.length() > 0 && !lockedBy.equals(ProxyParameters.USER_ID)) {
            String data = getLockDetails(lockedBy, getScreenID()) ;
            ClientUtil.showMessageWindow("Selected Record is Opened/Modified by " + lockedBy + data.toString());
            //                    setMode(ClientConstants.ACTIONTYPE_VIEW_MODE);
            btnSave.setEnabled(false);
        }
        
        
    }
     
     private String getLockDetails(String lockedBy, String screenId){
        HashMap map = new HashMap();
        StringBuffer data = new StringBuffer() ;
        map.put("LOCKED_BY", lockedBy) ;
        map.put("SCREEN_ID", screenId) ;
        java.util.List lstLock = ClientUtil.executeQuery("getLockedDetails", map);
        map.clear();
        if(lstLock.size() > 0){
            map = (HashMap)(lstLock.get(0));
            data.append("\nLog in Time : ").append(map.get("LOCKED_TIME")) ;
            data.append("\nIP Address : ").append(map.get("IP_ADDR")) ;
            data.append("\nBranch : ").append(map.get("BRANCH_ID"));
        }
        lstLock = null ;
        map = null ;
        return data.toString();
    }
    
    private void deleteEditLock(String chittalNo) {
        HashMap map = new HashMap();
        map.put("SCREEN_ID", getScreenID());
        map.put("RECORD_KEY", chittalNo);
        map.put(CommonConstants.USER_ID, ProxyParameters.USER_ID);
        map.put("BRANCH_ID", ProxyParameters.BRANCH_ID);
        map.put("CUR_DATE", curr_dt.clone());
        System.out.println("Record Key Map : " + map);
        ClientUtil.execute("deleteEditLock", map);
    }
    
    
    }
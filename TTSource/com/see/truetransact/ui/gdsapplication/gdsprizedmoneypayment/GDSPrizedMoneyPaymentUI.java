/*

 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * GDSPrizedMoneyPaymentUI.java
 *
 * Created on November 26, 2003, 11:27 AM
 */
package com.see.truetransact.ui.gdsapplication.gdsprizedmoneypayment;

import com.see.truetransact.ui.mdsapplication.mdsprizedmoneypayment.*;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.transaction.TransactionUI;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.transferobject.gdsapplication.GDSApplicationTO;
import com.see.truetransact.ui.common.servicetax.ServiceTaxCalculation;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.common.viewall.NewAuthorizeListUI;
import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.customer.SmartCustomerUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListDebitUI;
import com.see.truetransact.ui.salaryrecovery.AuthorizeListCreditUI;
import java.util.LinkedHashMap;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

public class GDSPrizedMoneyPaymentUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField {

    private final static ClientParseException parseException = ClientParseException.getInstance();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.gdsapplication.gdsprizedmoneypayment.GDSPrizedMoneyPaymentRB", ProxyParameters.LANGUAGE);
    final GDSPrizedMoneyPaymentMRB objMandatoryMRB = new GDSPrizedMoneyPaymentMRB();
    private TransactionUI transactionUI = new TransactionUI();
    private GDSPrizedMoneyPaymentOB observable;
    private String viewType = new String();
    final String AUTHORIZE = "Authorize";
    private boolean updateMode = false;
    private boolean isFilled = false;
    HashMap mandatoryMap = null;
    HashMap addressMap = null;
   
    int updateTab = -1;
    Date currDt = null;
    private boolean defaulterMarked = false;
    String defaulter_bonus_recover = "";
    boolean fromAuthorizeUI = false;
    boolean fromCashierAuthorizeUI = false;
    boolean fromManagerAuthorizeUI = false;
    AuthorizeListUI authorizeListUI = null;
    AuthorizeListDebitUI ManagerauthorizeListUI=null;
    AuthorizeListCreditUI CashierauthorizeListUI=null;
    private int rejectFlag = 0;
    private String schemeDesc = "";
    private ServiceTaxCalculation objServiceTax;
    public HashMap serviceTax_Map;
    boolean fromSmartCustUI = false;
    SmartCustomerUI smartUI = null;
    boolean thalayal = false;
    boolean munnal = false;
    NewAuthorizeListUI newauthorizeListUI = null;
    boolean fromNewAuthorizeUI = false;

    /**
     * Creates new form BeanForm
     */
    public GDSPrizedMoneyPaymentUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initForm();
        tabRemittanceProduct.resetVisits();
    }

    private void initForm() {
        setFieldNames();
        internationalize();
        observable = new GDSPrizedMoneyPaymentOB();
        observable.setTransactionOB(transactionUI.getTransactionOB());
        setMandatoryHashMap();
        initComponentData();
        setHelpMessage();
        setButtonEnableDisable();
        ClientUtil.enableDisable(panGeneralRemittance, false);
        ClientUtil.enableDisable(panOtherDetailsInside, false);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panInsideGeneralRemittance);
        setMaximumLength();
        panInsideGeneralRemittance1.add(transactionUI);
        transactionUI.setSourceScreen("MDS_PRIZED_PAYMENT");
        tblChangedMemberDetails.setModel(observable.getTblChangedDetails());
        btnViewLedger.setEnabled(false);
        txtTotalAmtPaid.setEnabled(false);
        txtEarlierChitMemberNumber.setEnabled(false);
        panEarlierChitDetails.setVisible(false);
        lblSchemeDesc.setVisible(false);
        rdoDefaulters_NoActionPerformed(null);
        ClientUtil.enableDisable(panDefaulters, false);
        rdoDefaulters_Yes.setEnabled(false);
        rdoDefaulters_No.setEnabled(true);
        rdoDefaulters_No.setSelected(true);
    }

    /* Auto Generated Method - setFieldNames()
     This method assigns name for all the components.
     Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSchemeName.setName("btnSchemeName");
        btnChittalNo.setName("btnChittalNo");
        btnMemberNo.setName("btnMemberNo");
        btnView.setName("btnView");
        chkChangedBetween.setName("chkChangedBetween");
        chkMunnal.setName("chkMunnal");
        chkThalayal.setName("chkThalayal");
        lblArea.setName("lblArea");
        lblAribitrationAmt.setName("lblAribitrationAmt");
        lblBonusAmt.setName("lblBonusAmt");
        lblChangedBetween.setName("lblChangedBetween");
        lblChittalNo.setName("lblChittalNo");
        lblCity.setName("lblCity");
        lblCommisionAmt.setName("lblCommisionAmt");
        lblDiscountAmt.setName("lblDiscountAmt");
        lblDivisionNo.setName("lblDivisionNo");
        lblDrawDate.setName("lblDrawDate");
        lblMemberName.setName("lblMemberName");
        lblMemberNameVal.setName("lblMemberNameVal");
        lblMemberNo.setName("lblMemberNo");
        lblMsg.setName("lblMsg");
        lblMunnal.setName("lblMunnal");
        lblNetAmt.setName("lblNetAmt");
        lblNoOfInstPaid.setName("lblNoOfInstPaid");
        lblNoticeAmt.setName("lblNoticeAmt");
        lblOverDueAmt.setName("lblOverDueAmt");
        lblOverdueInst.setName("lblOverdueInst");
        lblPin.setName("lblPin");
        lblPrizedAmt.setName("lblPrizedAmt");
        lblPrizedInstNo.setName("lblPrizedInstNo");
        lblGroupNo.setName("lblSchemeName");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblState.setName("lblState");
        lblStatus.setName("lblStatus");
        lblChangedMemberDetails.setName("lblChangedMemberDetails");
        lblLoanDetails.setName("lblLoanDetails");
        lblLiabilityMemberDetails.setName("lblLiabilityMemberDetails");
        lblLiabilitySuretiesDetails.setName("lblLiabilitySuretiesDetails");
        lblStreet.setName("lblStreet");
        lblThalayal.setName("lblThalayal");
        lblTotalInst.setName("lblTotalInst");
        lblValArea.setName("lblValArea");
        lblValCity.setName("lblValCity");
        lblValPin.setName("lblValPin");
        lblValState.setName("lblValState");
        lblValStreet.setName("lblValStreet");
        mbrMain.setName("mbrMain");
        panOtherDetailsInside.setName("panOtherDetailsInside");
        panAddressDetails.setName("panAddressDetails");
        panGeneralRemittance.setName("panGeneralRemittance");
        panOtherDetailsInside.setName("panOtherDetailsInside");
        panInsideGeneralRemittance.setName("panInsideGeneralRemittance");
        panInsideGeneralRemittance1.setName("panInsideGeneralRemittance1");
        panInsideGeneralRemittance2.setName("panInsideGeneralRemittance2");
        panInsideOtherDetails.setName("panInsideOtherDetails");
//        panInsideGeneralRemittance7.setName("panInsideGeneralRemittance7");
        panSchemeName.setName("panSchemeName");
        panSchemeName1.setName("panSchemeName1");
        panSchemeName2.setName("panSchemeName2");
        panStatus.setName("panStatus");
        panTableLiabilityDetailsofSureties.setName("panTableLiabilityDetailsofSureties");
        panTableLoanDetails.setName("panTableLoanDetails");
        panTableChangedMemberDetails.setName("panTableChangedMemberDetails");
        panTableLiabilityDetailsofMember.setName("panTableLiabilityDetailsofMember");
        srpTableDetailsofSureties.setName("srpTableDetailsofSureties");
        srpTableLoanDetails.setName("srpTableLoanDetails");
        srpTableChangedMemberDetails.setName("srpTableChangedMemberDetails");
        srpTableLiabilityDetailsofMember.setName("srpTableLiabilityDetailsofMember");
        tabRemittanceProduct.setName("tabRemittanceProduct");
        tblSuretyDetails.setName("tblSuretyDetails");
        tblLoanDetails.setName("tblLoanDetails");
        tblChangedMemberDetails.setName("tblChangedMemberDetails");
        tblLiabilityDetailsofMember.setName("tblLiabilityDetailsofMember");
        tdtDrawDate.setName("tdtDrawDate");
        txtAribitrationAmt.setName("txtAribitrationAmt");
        txtBonusAmt.setName("txtBonusAmt");
        txtPenalAmt.setName("txtPenalAmt");
        txtBonusRecovered.setName("txtBonusRecovered");
        txtGdsNo.setName("txtChittalNo");
        txtMemberNo.setName("txtMemberNo");
        txtCommisionAmt.setName("txtCommisionAmt");
        txtDiscountAmt.setName("txtDiscountAmt");
        txtDivisionNo.setName("txtDivisionNo");
        txtSubNo.setName("txtSubNo");
        txtNetAmt.setName("txtNetAmt");
        txtNoOfInstPaid.setName("txtNoOfInstPaid");
        txtNoticeAmt.setName("txtNoticeAmt");
        txtChargeAmount.setName("txtChargeAmount");
        txtOverDueAmt.setName("txtOverDueAmt");
        txtOverdueInst.setName("txtOverdueInst");
        txtPrizedAmt.setName("txtPrizedAmt");
        txtPrizedInstNo.setName("txtPrizedInstNo");
        txtGroupName.setName("txtSchemeName");
        txtTotalInst.setName("txtTotalInst");
    }

    /* Auto Generated Method - internationalize()
     This method used to assign display texts from
     the Resource Bundle File. */
    private void internationalize() {
        chkThalayal.setText(resourceBundle.getString("chkThalayal"));
        lblOverDueAmt.setText(resourceBundle.getString("lblOverDueAmt"));
        lblLoanDetails.setText(resourceBundle.getString("lblLoanDetails"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblThalayal.setText(resourceBundle.getString("lblThalayal"));
        btnChittalNo.setText(resourceBundle.getString("btnChittalNo"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblLiabilityMemberDetails.setText(resourceBundle.getString("lblLiabilityMemberDetails"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblDivisionNo.setText(resourceBundle.getString("lblDivisionNo"));
        lblOverdueInst.setText(resourceBundle.getString("lblOverdueInst"));
        lblPrizedInstNo.setText(resourceBundle.getString("lblPrizedInstNo"));
        lblNetAmt.setText(resourceBundle.getString("lblNetAmt"));
        lblMunnal.setText(resourceBundle.getString("lblMunnal"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblValStreet.setText(resourceBundle.getString("lblValStreet"));
        lblDiscountAmt.setText(resourceBundle.getString("lblDiscountAmt"));
        chkMunnal.setText(resourceBundle.getString("chkMunnal"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMemberNo.setText(resourceBundle.getString("lblMemberNo"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblChittalNo.setText(resourceBundle.getString("lblChittalNo"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblValState.setText(resourceBundle.getString("lblValState"));
        lblAribitrationAmt.setText(resourceBundle.getString("lblAribitrationAmt"));
        lblLiabilitySuretiesDetails.setText(resourceBundle.getString("lblLiabilitySuretiesDetails"));
        lblCommisionAmt.setText(resourceBundle.getString("lblCommisionAmt"));
        btnView.setText(resourceBundle.getString("btnView"));
        lblGroupNo.setText(resourceBundle.getString("lblSchemeName"));
        lblValCity.setText(resourceBundle.getString("lblValCity"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblPin.setText(resourceBundle.getString("lblPin"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblBonusAmt.setText(resourceBundle.getString("lblBonusAmt"));
        lblBonusRecovered.setText(resourceBundle.getString("lblBonusRecovered"));
        lblPenalAmt.setText(resourceBundle.getString("lblPenalAmt"));
        lblChangedMemberDetails.setText(resourceBundle.getString("lblChangedMemberDetails"));
        lblMemberNameVal.setText(resourceBundle.getString("lblMemberNameVal"));
        lblMemberName.setText(resourceBundle.getString("lblMemberName"));
        btnMemberNo.setText(resourceBundle.getString("btnMemberNo"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblChangedBetween.setText(resourceBundle.getString("lblChangedBetween"));
        lblValArea.setText(resourceBundle.getString("lblValArea"));
        lblValPin.setText(resourceBundle.getString("lblValPin"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnSchemeName.setText(resourceBundle.getString("btnSchemeName"));
        lblNoOfInstPaid.setText(resourceBundle.getString("lblNoOfInstPaid"));
        lblDrawDate.setText(resourceBundle.getString("lblDrawDate"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        lblTotalInst.setText(resourceBundle.getString("lblTotalInst"));
        chkChangedBetween.setText(resourceBundle.getString("chkChangedBetween"));
    }

    /* Auto Generated Method - setMandatoryHashMap()
 
     ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
     This method list out all the Input Fields available in the UI.
     It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtSchemeName", new Boolean(true));
        mandatoryMap.put("txtDivisionNo", new Boolean(true));
        mandatoryMap.put("txtTotalInst", new Boolean(true));
        mandatoryMap.put("txtNoOfInstPaid", new Boolean(true));
        mandatoryMap.put("txtCommisionAmt", new Boolean(true));
        mandatoryMap.put("txtBonusAmt", new Boolean(true));
        mandatoryMap.put("txtPrizedInstNo", new Boolean(true));
        mandatoryMap.put("txtOverDueAmt", new Boolean(true));
        mandatoryMap.put("txtNetAmt", new Boolean(true));
        mandatoryMap.put("txtAribitrationAmt", new Boolean(true));
        mandatoryMap.put("txtDiscountAmt", new Boolean(true));
        mandatoryMap.put("txtPrizedAmt", new Boolean(true));
        mandatoryMap.put("txtNoticeAmt", new Boolean(true));
        mandatoryMap.put("txtChargeAmount", new Boolean(true));
        mandatoryMap.put("txtOverdueInst", new Boolean(true));
        mandatoryMap.put("tdtDrawDate", new Boolean(true));
        mandatoryMap.put("txtChittalNo", new Boolean(true));
        mandatoryMap.put("txtMemberNo", new Boolean(true));
        mandatoryMap.put("txtApplicantsName", new Boolean(true));
        mandatoryMap.put("cboTransType", new Boolean(true));
        mandatoryMap.put("txtTransactionAmt", new Boolean(true));
        mandatoryMap.put("txtTransProductId", new Boolean(true));
        mandatoryMap.put("cboProductType", new Boolean(true));
        mandatoryMap.put("txtDebitAccNo", new Boolean(true));
        mandatoryMap.put("tdtChequeDate", new Boolean(true));
        mandatoryMap.put("txtChequeNo", new Boolean(true));
        mandatoryMap.put("txtChequeNo2", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("txtTokenNo", new Boolean(true));
    }

    /* Auto Generated Method - getMandatoryHashMap()
     Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void initComponentData() {
        try {
        } catch (ClassCastException e) {
            parseException.logException(e, true);
        }
    }

    private void setMaximumLength() {
        txtOverDueAmt.setValidation(new CurrencyValidation(14, 2));
        txtBonusAmt.setValidation(new CurrencyValidation(14, 2));
        txtPenalAmt.setValidation(new CurrencyValidation(14, 2));
        txtBonusRecovered.setValidation(new CurrencyValidation(14, 2));
        txtCommisionAmt.setValidation(new CurrencyValidation(14, 2));
        txtDiscountAmt.setValidation(new CurrencyValidation(14, 2));
        txtNoticeAmt.setValidation(new CurrencyValidation(14, 2));
        txtChargeAmount.setValidation(new CurrencyValidation(14, 2));
        txtAribitrationAmt.setValidation(new CurrencyValidation(14, 2));
        txtPrizedAmt.setValidation(new CurrencyValidation(14, 2));
        txtNetAmt.setValidation(new CurrencyValidation(14, 2));
        txtPrizedInstNo.setValidation(new NumericValidation(3, 0));
        txtTotalInst.setAllowNumber(true);
        txtNoOfInstPaid.setAllowNumber(true);
        txtOverdueInst.setAllowNumber(true);
        txtTotalAmtPaid.setValidation(new CurrencyValidation(14, 2));
        txtAmountRefunded.setValidation(new CurrencyValidation(14, 2));
    }

    /**
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    public void update() {
        lblSchemeDesc.setText(CommonUtil.convertObjToStr(schemeDesc));
        txtGroupName.setText(observable.getTxtGroupName());
        txtDivisionNo.setText(observable.getTxtDivisionNo());
        txtSubNo.setText(observable.getTxtSubNo());
        txtTotalInst.setText(observable.getTxtTotalInst());
        txtNoOfInstPaid.setText(observable.getTxtNoOfInstPaid());
        txtCommisionAmt.setText(observable.getTxtCommisionAmt());
        txtAmountRefunded.setText(observable.getTxtAmountRefunded());
        txtBonusAmt.setText(observable.getTxtBonusAmt());
        txtPrizedInstNo.setText(observable.getTxtPrizedInstNo());
        txtOverDueAmt.setText(observable.getTxtOverDueAmt());
        txtNetAmt.setText(observable.getTxtNetAmt());
        txtAribitrationAmt.setText(observable.getTxtAribitrationAmt());
        txtDiscountAmt.setText(observable.getTxtDiscountAmt());
        txtPrizedAmt.setText(observable.getTxtPrizedAmt());
        txtNoticeAmt.setText(observable.getTxtNoticeAmt());
        txtChargeAmount.setText(observable.getTxtChargeAmount());
        txtOverdueInst.setText(observable.getTxtOverdueInst());
        txtGdsNo.setText(observable.getTxtGdsNo());
        txtMemberNo.setText(observable.getTxtMemberNo());
        lblMemberNameVal.setText(observable.getLblMemberNameVal());
        String defaulter = CommonUtil.convertObjToStr(observable.getRdoDefaulters());
        txtPenalAmt.setText(observable.getTxtPenalAmt());
        txtBonusRecovered.setText(observable.getTxtBonusRecovered());
        lblServiceTaxval.setText(observable.getLblServiceTaxval());

        if (!observable.getDefaulter().equals("Y")) {
            if (defaulter.length() > 0 && defaulter.equals("Y")) {
                rdoDefaulters_Yes.setSelected(true);
                defaulterEnableDisable(true);
                txtBonusRecovered.setVisible(false);
                lblBonusRecovered.setVisible(false);
            } else {
                rdoDefaulters_No.setSelected(true);
                defaulterEnableDisable(false);
            }
        } else {
            rdoDefaulters_No.setSelected(true);
            defaulterEnableDisable(false);
        }
    }

    /* Auto Generated Method - updateOBFields()
     This method called by Save option of UI.
     It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setScreen(this.getScreen());
        observable.setTxtGroupName(txtGroupName.getText());
        observable.setTxtDivisionNo(txtDivisionNo.getText());
        observable.setTxtSubNo(txtSubNo.getText());
        observable.setTxtTotalInst(txtTotalInst.getText());
        observable.setTdtDrawDate(tdtDrawDate.getDateValue());
        observable.setTxtNoOfInstPaid(txtNoOfInstPaid.getText());
        observable.setTxtCommisionAmt(txtCommisionAmt.getText());
        observable.setTxtAmountRefunded(txtAmountRefunded.getText());
        observable.setTxtTotalAmtPaid(txtTotalAmtPaid.getText());
        observable.setTxtBonusAmt(txtBonusAmt.getText());
        observable.setTxtPrizedInstNo(txtPrizedInstNo.getText());
        observable.setTxtOverDueAmt(txtOverDueAmt.getText());
        observable.setTxtNetAmt(txtNetAmt.getText());
        observable.setTxtAribitrationAmt(txtAribitrationAmt.getText());
        observable.setTxtDiscountAmt(txtDiscountAmt.getText());
        observable.setTxtPrizedAmt(txtPrizedAmt.getText());
        observable.setTxtNoticeAmt(txtNoticeAmt.getText());
        observable.setTxtChargeAmount(txtChargeAmount.getText());
        observable.setTxtOverdueInst(txtOverdueInst.getText());
        observable.setTdtDrawDate(tdtDrawDate.getDateValue());
        observable.setTxtGdsNo(txtGdsNo.getText());
        observable.setTxtMemberNo(txtMemberNo.getText());
        observable.setLblMemberNameVal(lblMemberNameVal.getText());
        if (rdoDefaulters_Yes.isSelected() == true) {
            observable.setRdoDefaulters("Y");
            observable.setTxtPenalAmt(txtPenalAmt.getText());
            observable.setTxtBonusRecovered(txtBonusRecovered.getText());
        } else {
            observable.setRdoDefaulters("N");
            observable.setTxtPenalAmt("");
            observable.setTxtBonusRecovered("");
        }
        if (defaulterMarked) {
            observable.setDefaulter("Y");
            if (!txtDefaulterBonus.getText().equals("")) {
                observable.setDefaulter_Bonus_recoverd(txtDefaulterBonus.getText());
            } else {
                observable.setDefaulter_Bonus_recoverd(txtDefaulterBonus.getText());
            }
            if (!txtDefaulterComm.getText().equals("")) {
                observable.setDefaulter_Commission(txtDefaulterComm.getText());
            } else {
                observable.setDefaulter_Commission("0.0");
            }
            if (!txtDefaulterInterest.getText().equals("")) {
                observable.setDefaulter_Interest(txtDefaulterInterest.getText());
            } else {
                observable.setDefaulter_Interest("0.0");
            }

        } else {
            observable.setDefaulter("N");
            observable.setDefaulter_Bonus_recoverd("0.0");
            observable.setDefaulter_Commission("0.0");
            observable.setDefaulter_Interest("0.0");
        }
        observable.setLblServiceTaxval(lblServiceTaxval.getText());
        observable.setServiceTax_Map(serviceTax_Map);
        observable.setBounsCommisionTransfer(chkBonusTrans.isSelected());
        observable.setPartPay(chkPartPay.isSelected());
        if(lblServiceTaxval.getText().length() > 0 && CommonUtil.convertObjToDouble(lblServiceTaxval.getText()) > 0){ // Added by nithya on 22-11-2019 for KD-910
            observable.setPaymentTaxAmt(CommonUtil.convertObjToDouble(lblServiceTaxval.getText()));
        }else{
            observable.setPaymentTaxAmt(0.0);
        }
    }

    /* Auto Generated Method - setHelpMessage()
     This method shows tooltip help for all the input fields
     available in the UI. It needs the Mandatory Resource Bundle
     object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        GDSPrizedMoneyPaymentMRB objMandatoryRB = new GDSPrizedMoneyPaymentMRB();
        txtGroupName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSchemeName"));
        txtDivisionNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDivisionNo"));
        txtTotalInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalInst"));
        txtNoOfInstPaid.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfInstPaid"));
        txtCommisionAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommisionAmt"));
        txtBonusAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBonusAmt"));
        txtBonusRecovered.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBonusRecovered"));
        txtPenalAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPenalAmt"));
        txtPrizedInstNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrizedInstNo"));
        txtOverDueAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOverDueAmt"));
        txtNetAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNetAmt"));
        txtAribitrationAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAribitrationAmt"));
        txtDiscountAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiscountAmt"));
        txtPrizedAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPrizedAmt"));
        txtNoticeAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoticeAmt"));
        txtChargeAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargeAmount"));
        txtOverdueInst.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOverdueInst"));
        tdtDrawDate.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtDrawDate"));
        txtGdsNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChittalNo"));
        txtMemberNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMemberNo"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDefaulters = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace76 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace77 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace78 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabRemittanceProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        panInsideGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        panAddressDetails = new com.see.truetransact.uicomponent.CPanel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblValStreet = new com.see.truetransact.uicomponent.CLabel();
        lblValArea = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblPin = new com.see.truetransact.uicomponent.CLabel();
        lblValPin = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblValState = new com.see.truetransact.uicomponent.CLabel();
        lblValCity = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        panInsideGeneralRemittance2 = new com.see.truetransact.uicomponent.CPanel();
        txtCommisionAmt = new com.see.truetransact.uicomponent.CTextField();
        txtBonusAmt = new com.see.truetransact.uicomponent.CTextField();
        txtPrizedInstNo = new com.see.truetransact.uicomponent.CTextField();
        txtOverDueAmt = new com.see.truetransact.uicomponent.CTextField();
        lblCommisionAmt = new com.see.truetransact.uicomponent.CLabel();
        lblBonusAmt = new com.see.truetransact.uicomponent.CLabel();
        lblPrizedInstNo = new com.see.truetransact.uicomponent.CLabel();
        lblOverDueAmt = new com.see.truetransact.uicomponent.CLabel();
        txtNetAmt = new com.see.truetransact.uicomponent.CTextField();
        txtAribitrationAmt = new com.see.truetransact.uicomponent.CTextField();
        txtDiscountAmt = new com.see.truetransact.uicomponent.CTextField();
        lblNetAmt = new com.see.truetransact.uicomponent.CLabel();
        lblPrizedAmt = new com.see.truetransact.uicomponent.CLabel();
        lblDiscountAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPrizedAmt = new com.see.truetransact.uicomponent.CTextField();
        txtNoticeAmt = new com.see.truetransact.uicomponent.CTextField();
        lblNoticeAmt = new com.see.truetransact.uicomponent.CLabel();
        lblAribitrationAmt = new com.see.truetransact.uicomponent.CLabel();
        panEarlierChitDetails = new com.see.truetransact.uicomponent.CPanel();
        lblEarlierChitMemberName = new com.see.truetransact.uicomponent.CLabel();
        lblAmountRefunded = new com.see.truetransact.uicomponent.CLabel();
        lblEarlierChitMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmtPaid = new com.see.truetransact.uicomponent.CLabel();
        txtTotalAmtPaid = new com.see.truetransact.uicomponent.CTextField();
        txtAmountRefunded = new com.see.truetransact.uicomponent.CTextField();
        txtEarlierChitMemberNumber = new com.see.truetransact.uicomponent.CTextField();
        lblPenalAmt = new com.see.truetransact.uicomponent.CLabel();
        txtPenalAmt = new com.see.truetransact.uicomponent.CTextField();
        lblBonusRecovered = new com.see.truetransact.uicomponent.CLabel();
        txtBonusRecovered = new com.see.truetransact.uicomponent.CTextField();
        lblChargeAmount = new com.see.truetransact.uicomponent.CLabel();
        txtChargeAmount = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblServiceTaxval = new com.see.truetransact.uicomponent.CLabel();
        panInsideSchemeDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMemberName = new com.see.truetransact.uicomponent.CLabel();
        txtDivisionNo = new com.see.truetransact.uicomponent.CTextField();
        panSchemeName2 = new com.see.truetransact.uicomponent.CPanel();
        txtMemberNo = new com.see.truetransact.uicomponent.CTextField();
        btnMemberNo = new com.see.truetransact.uicomponent.CButton();
        lblDrawDate = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInst = new com.see.truetransact.uicomponent.CLabel();
        lblDivisionNo = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfInstPaid = new com.see.truetransact.uicomponent.CTextField();
        panSchemeName = new com.see.truetransact.uicomponent.CPanel();
        txtGroupName = new com.see.truetransact.uicomponent.CTextField();
        btnSchemeName = new com.see.truetransact.uicomponent.CButton();
        lblNoOfInstPaid = new com.see.truetransact.uicomponent.CLabel();
        txtTotalInst = new com.see.truetransact.uicomponent.CTextField();
        txtOverdueInst = new com.see.truetransact.uicomponent.CTextField();
        lblMemberNo = new com.see.truetransact.uicomponent.CLabel();
        lblMemberNameVal = new com.see.truetransact.uicomponent.CLabel();
        lblOverdueInst = new com.see.truetransact.uicomponent.CLabel();
        lblGroupNo = new com.see.truetransact.uicomponent.CLabel();
        lblChittalNo = new com.see.truetransact.uicomponent.CLabel();
        tdtDrawDate = new com.see.truetransact.uicomponent.CDateField();
        panSchemeName1 = new com.see.truetransact.uicomponent.CPanel();
        txtGdsNo = new com.see.truetransact.uicomponent.CTextField();
        btnChittalNo = new com.see.truetransact.uicomponent.CButton();
        txtSubNo = new com.see.truetransact.uicomponent.CTextField();
        panDefaulters = new com.see.truetransact.uicomponent.CPanel();
        rdoDefaulters_No = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDefaulters_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        lblDefaulters = new com.see.truetransact.uicomponent.CLabel();
        panDefaulterCharges = new com.see.truetransact.uicomponent.CPanel();
        lblDefaulterComm = new com.see.truetransact.uicomponent.CLabel();
        txtDefaulterComm = new com.see.truetransact.uicomponent.CTextField();
        lblDefaulterInterst = new com.see.truetransact.uicomponent.CLabel();
        txtDefaulterInterest = new com.see.truetransact.uicomponent.CTextField();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtDefaulterBonus = new com.see.truetransact.uicomponent.CTextField();
        panBtnLedger = new com.see.truetransact.uicomponent.CPanel();
        lblSchemeDesc = new com.see.truetransact.uicomponent.CLabel();
        btnViewLedger = new com.see.truetransact.uicomponent.CButton();
        chkBonusTrans = new com.see.truetransact.uicomponent.CCheckBox();
        chkPartPay = new com.see.truetransact.uicomponent.CCheckBox();
        panInsideGeneralRemittance1 = new com.see.truetransact.uicomponent.CPanel();
        panOtherDetailsInside = new com.see.truetransact.uicomponent.CPanel();
        panInsideOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        panOtherMDSDetails = new com.see.truetransact.uicomponent.CPanel();
        lblThalayal = new com.see.truetransact.uicomponent.CLabel();
        lblMunnal = new com.see.truetransact.uicomponent.CLabel();
        lblChangedBetween = new com.see.truetransact.uicomponent.CLabel();
        chkThalayal = new com.see.truetransact.uicomponent.CCheckBox();
        chkMunnal = new com.see.truetransact.uicomponent.CCheckBox();
        chkChangedBetween = new com.see.truetransact.uicomponent.CCheckBox();
        panTableLiabilityDetailsofSureties = new com.see.truetransact.uicomponent.CPanel();
        srpTableDetailsofSureties = new com.see.truetransact.uicomponent.CScrollPane();
        tblSuretyDetails = new com.see.truetransact.uicomponent.CTable();
        panTableLoanDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTableLoanDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblLoanDetails = new com.see.truetransact.uicomponent.CTable();
        lblLoanDetails = new com.see.truetransact.uicomponent.CLabel();
        lblLiabilityMemberDetails = new com.see.truetransact.uicomponent.CLabel();
        panTableChangedMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTableChangedMemberDetails = new com.see.truetransact.uicomponent.CScrollPane();
        tblChangedMemberDetails = new com.see.truetransact.uicomponent.CTable();
        lblLiabilitySuretiesDetails = new com.see.truetransact.uicomponent.CLabel();
        panTableLiabilityDetailsofMember = new com.see.truetransact.uicomponent.CPanel();
        srpTableLiabilityDetailsofMember = new com.see.truetransact.uicomponent.CScrollPane();
        tblLiabilityDetailsofMember = new com.see.truetransact.uicomponent.CTable();
        lblChangedMemberDetails = new com.see.truetransact.uicomponent.CLabel();
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
        setMinimumSize(new java.awt.Dimension(858, 666));
        setPreferredSize(new java.awt.Dimension(858, 666));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(25, 27));
        btnView.setPreferredSize(new java.awt.Dimension(25, 27));
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

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace73);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace74);

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

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace75);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.setFocusable(false);
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

        lblSpace76.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace76.setText("     ");
        lblSpace76.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace76.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace76);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace77.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace77.setText("     ");
        lblSpace77.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace77.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace77);

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

        lblSpace78.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace78.setText("     ");
        lblSpace78.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace78.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace78);

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

        tabRemittanceProduct.setMinimumSize(new java.awt.Dimension(680, 480));
        tabRemittanceProduct.setPreferredSize(new java.awt.Dimension(680, 480));

        panGeneralRemittance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneralRemittance.setMinimumSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setPreferredSize(new java.awt.Dimension(850, 450));
        panGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        panInsideGeneralRemittance.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance.setMinimumSize(new java.awt.Dimension(850, 340));
        panInsideGeneralRemittance.setPreferredSize(new java.awt.Dimension(850, 340));
        panInsideGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        panAddressDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Address Details"));
        panAddressDetails.setMinimumSize(new java.awt.Dimension(500, 55));
        panAddressDetails.setPreferredSize(new java.awt.Dimension(500, 55));
        panAddressDetails.setLayout(new java.awt.GridBagLayout());

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblStreet, gridBagConstraints);

        lblValStreet.setMinimumSize(new java.awt.Dimension(190, 10));
        lblValStreet.setPreferredSize(new java.awt.Dimension(200, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblValStreet, gridBagConstraints);

        lblValArea.setMinimumSize(new java.awt.Dimension(200, 10));
        lblValArea.setPreferredSize(new java.awt.Dimension(200, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblValArea, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblArea, gridBagConstraints);

        lblPin.setText("Pin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblPin, gridBagConstraints);

        lblValPin.setMinimumSize(new java.awt.Dimension(75, 10));
        lblValPin.setPreferredSize(new java.awt.Dimension(75, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblValPin, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblState, gridBagConstraints);

        lblValState.setMinimumSize(new java.awt.Dimension(100, 10));
        lblValState.setPreferredSize(new java.awt.Dimension(100, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblValState, gridBagConstraints);

        lblValCity.setText("                     ");
        lblValCity.setMinimumSize(new java.awt.Dimension(75, 10));
        lblValCity.setPreferredSize(new java.awt.Dimension(75, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblValCity, gridBagConstraints);

        lblCity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblCity.setText("City");
        lblCity.setPreferredSize(new java.awt.Dimension(31, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAddressDetails.add(lblCity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 8, 1);
        panInsideGeneralRemittance.add(panAddressDetails, gridBagConstraints);

        panInsideGeneralRemittance2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance2.setMinimumSize(new java.awt.Dimension(310, 325));
        panInsideGeneralRemittance2.setPreferredSize(new java.awt.Dimension(310, 340));
        panInsideGeneralRemittance2.setLayout(new java.awt.GridBagLayout());

        txtCommisionAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtCommisionAmt, gridBagConstraints);

        txtBonusAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBonusAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtBonusAmt, gridBagConstraints);

        txtPrizedInstNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPrizedInstNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtPrizedInstNo, gridBagConstraints);

        txtOverDueAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtOverDueAmt, gridBagConstraints);

        lblCommisionAmt.setText("Commission Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblCommisionAmt, gridBagConstraints);

        lblBonusAmt.setText("Bonus Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblBonusAmt, gridBagConstraints);

        lblPrizedInstNo.setText("Prized Installment No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblPrizedInstNo, gridBagConstraints);

        lblOverDueAmt.setText("Overdue Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblOverDueAmt, gridBagConstraints);

        txtNetAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNetAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNetAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtNetAmt, gridBagConstraints);

        txtAribitrationAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAribitrationAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAribitrationAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtAribitrationAmt, gridBagConstraints);

        txtDiscountAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDiscountAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtDiscountAmt, gridBagConstraints);

        lblNetAmt.setText("Net Amount Disbursed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblNetAmt, gridBagConstraints);

        lblPrizedAmt.setText("Prized Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblPrizedAmt, gridBagConstraints);

        lblDiscountAmt.setText("Discount Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblDiscountAmt, gridBagConstraints);

        txtPrizedAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtPrizedAmt, gridBagConstraints);

        txtNoticeAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNoticeAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoticeAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtNoticeAmt, gridBagConstraints);

        lblNoticeAmt.setText("Notice Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblNoticeAmt, gridBagConstraints);

        lblAribitrationAmt.setText("Total Case Expense Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblAribitrationAmt, gridBagConstraints);

        panEarlierChitDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panEarlierChitDetails.setMinimumSize(new java.awt.Dimension(290, 92));
        panEarlierChitDetails.setPreferredSize(new java.awt.Dimension(290, 92));
        panEarlierChitDetails.setLayout(new java.awt.GridBagLayout());

        lblEarlierChitMemberName.setForeground(new java.awt.Color(0, 51, 204));
        lblEarlierChitMemberName.setText(" ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEarlierChitDetails.add(lblEarlierChitMemberName, gridBagConstraints);

        lblAmountRefunded.setText("Amount to be Refunded");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEarlierChitDetails.add(lblAmountRefunded, gridBagConstraints);

        lblEarlierChitMemberNo.setText("Earlier Chit Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEarlierChitDetails.add(lblEarlierChitMemberNo, gridBagConstraints);

        lblTotalAmtPaid.setText("Total Amount Paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEarlierChitDetails.add(lblTotalAmtPaid, gridBagConstraints);

        txtTotalAmtPaid.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEarlierChitDetails.add(txtTotalAmtPaid, gridBagConstraints);

        txtAmountRefunded.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountRefunded.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountRefundedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panEarlierChitDetails.add(txtAmountRefunded, gridBagConstraints);

        txtEarlierChitMemberNumber.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panEarlierChitDetails.add(txtEarlierChitMemberNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        panInsideGeneralRemittance2.add(panEarlierChitDetails, gridBagConstraints);

        lblPenalAmt.setText("Penal Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblPenalAmt, gridBagConstraints);

        txtPenalAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtPenalAmt, gridBagConstraints);

        lblBonusRecovered.setText("Bonus to be Recovered");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblBonusRecovered, gridBagConstraints);

        txtBonusRecovered.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBonusRecovered.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusRecoveredFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtBonusRecovered, gridBagConstraints);

        lblChargeAmount.setText("Charge Amount");
        lblChargeAmount.setMaximumSize(new java.awt.Dimension(111, 16));
        lblChargeAmount.setMinimumSize(new java.awt.Dimension(111, 16));
        lblChargeAmount.setPreferredSize(new java.awt.Dimension(111, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(lblChargeAmount, gridBagConstraints);

        txtChargeAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtChargeAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChargeAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChargeAmountActionPerformed(evt);
            }
        });
        txtChargeAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargeAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panInsideGeneralRemittance2.add(txtChargeAmount, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        panInsideGeneralRemittance2.add(lblServiceTax, gridBagConstraints);

        lblServiceTaxval.setText("cLabel3");
        lblServiceTaxval.setMaximumSize(new java.awt.Dimension(100, 18));
        lblServiceTaxval.setMinimumSize(new java.awt.Dimension(100, 18));
        lblServiceTaxval.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panInsideGeneralRemittance2.add(lblServiceTaxval, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 4, 4);
        panInsideGeneralRemittance.add(panInsideGeneralRemittance2, gridBagConstraints);

        panInsideSchemeDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideSchemeDetails.setMinimumSize(new java.awt.Dimension(500, 252));
        panInsideSchemeDetails.setPreferredSize(new java.awt.Dimension(500, 252));
        panInsideSchemeDetails.setLayout(new java.awt.GridBagLayout());

        lblMemberName.setText("Member Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblMemberName, gridBagConstraints);

        txtDivisionNo.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDivisionNo.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideSchemeDetails.add(txtDivisionNo, gridBagConstraints);

        panSchemeName2.setLayout(new java.awt.GridBagLayout());

        txtMemberNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panSchemeName2.add(txtMemberNo, gridBagConstraints);

        btnMemberNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMemberNo.setEnabled(false);
        btnMemberNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMemberNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMemberNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMemberNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName2.add(btnMemberNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(panSchemeName2, gridBagConstraints);

        lblDrawDate.setText("Draw/Auction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblDrawDate, gridBagConstraints);

        lblTotalInst.setText("Total Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblTotalInst, gridBagConstraints);

        lblDivisionNo.setText("Division No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideSchemeDetails.add(lblDivisionNo, gridBagConstraints);

        txtNoOfInstPaid.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoOfInstPaid.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(txtNoOfInstPaid, gridBagConstraints);

        panSchemeName.setLayout(new java.awt.GridBagLayout());

        txtGroupName.setAllowAll(true);
        txtGroupName.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGroupName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGroupNameFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName.add(txtGroupName, gridBagConstraints);

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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideSchemeDetails.add(panSchemeName, gridBagConstraints);

        lblNoOfInstPaid.setText("No.of installments paid");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblNoOfInstPaid, gridBagConstraints);

        txtTotalInst.setMinimumSize(new java.awt.Dimension(50, 21));
        txtTotalInst.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(txtTotalInst, gridBagConstraints);

        txtOverdueInst.setMinimumSize(new java.awt.Dimension(50, 21));
        txtOverdueInst.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(txtOverdueInst, gridBagConstraints);

        lblMemberNo.setText("Member No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblMemberNo, gridBagConstraints);

        lblMemberNameVal.setForeground(new java.awt.Color(0, 51, 204));
        lblMemberNameVal.setText("                                            ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panInsideSchemeDetails.add(lblMemberNameVal, gridBagConstraints);

        lblOverdueInst.setText("No.of Overdue Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblOverdueInst, gridBagConstraints);

        lblGroupNo.setText("GDS Group Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideSchemeDetails.add(lblGroupNo, gridBagConstraints);

        lblChittalNo.setText("Chittal No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblChittalNo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(tdtDrawDate, gridBagConstraints);

        panSchemeName1.setLayout(new java.awt.GridBagLayout());

        txtGdsNo.setAllowAll(true);
        txtGdsNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGdsNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGdsNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSchemeName1.add(txtGdsNo, gridBagConstraints);

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
        panSchemeName1.add(btnChittalNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideSchemeDetails.add(panSchemeName1, gridBagConstraints);

        txtSubNo.setMinimumSize(new java.awt.Dimension(40, 21));
        txtSubNo.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        panInsideSchemeDetails.add(txtSubNo, gridBagConstraints);

        panDefaulters.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoDefaulters_No);
        rdoDefaulters_No.setText("No");
        rdoDefaulters_No.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoDefaulters_No.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoDefaulters_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDefaulters_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDefaulters.add(rdoDefaulters_No, gridBagConstraints);

        rdgDefaulters.add(rdoDefaulters_Yes);
        rdoDefaulters_Yes.setText("Yes");
        rdoDefaulters_Yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoDefaulters_Yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoDefaulters_Yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoDefaulters_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDefaulters_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDefaulters.add(rdoDefaulters_Yes, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 2);
        panInsideSchemeDetails.add(panDefaulters, gridBagConstraints);

        lblDefaulters.setText("Defaulters");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panInsideSchemeDetails.add(lblDefaulters, gridBagConstraints);

        panDefaulterCharges.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDefaulterCharges.setMinimumSize(new java.awt.Dimension(320, 60));
        panDefaulterCharges.setPreferredSize(new java.awt.Dimension(205, 60));
        panDefaulterCharges.setLayout(new java.awt.GridBagLayout());

        lblDefaulterComm.setText("Defaulter Commission ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(-1, 0, 0, 0);
        panDefaulterCharges.add(lblDefaulterComm, gridBagConstraints);

        txtDefaulterComm.setAllowNumber(true);
        txtDefaulterComm.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDefaulterCharges.add(txtDefaulterComm, gridBagConstraints);

        lblDefaulterInterst.setText("Defaulter Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDefaulterCharges.add(lblDefaulterInterst, gridBagConstraints);

        txtDefaulterInterest.setAllowNumber(true);
        txtDefaulterInterest.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDefaulterCharges.add(txtDefaulterInterest, gridBagConstraints);

        cLabel1.setText("Bonus Recoverd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panDefaulterCharges.add(cLabel1, gridBagConstraints);

        txtDefaulterBonus.setAllowNumber(true);
        txtDefaulterBonus.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panDefaulterCharges.add(txtDefaulterBonus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        panInsideSchemeDetails.add(panDefaulterCharges, gridBagConstraints);

        panBtnLedger.setMaximumSize(new java.awt.Dimension(170, 27));
        panBtnLedger.setMinimumSize(new java.awt.Dimension(200, 27));
        panBtnLedger.setPreferredSize(new java.awt.Dimension(200, 27));
        panBtnLedger.setLayout(new java.awt.GridBagLayout());

        lblSchemeDesc.setText("cLabel2");
        lblSchemeDesc.setMinimumSize(new java.awt.Dimension(90, 18));
        lblSchemeDesc.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panBtnLedger.add(lblSchemeDesc, gridBagConstraints);

        btnViewLedger.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnViewLedger.setText("View Ledger");
        btnViewLedger.setMaximumSize(new java.awt.Dimension(110, 27));
        btnViewLedger.setMinimumSize(new java.awt.Dimension(100, 27));
        btnViewLedger.setPreferredSize(new java.awt.Dimension(100, 27));
        btnViewLedger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewLedgerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 1, 2);
        panBtnLedger.add(btnViewLedger, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panInsideSchemeDetails.add(panBtnLedger, gridBagConstraints);

        chkBonusTrans.setText("Transfer Bonus and Commision");
        chkBonusTrans.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBonusTransActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        panInsideSchemeDetails.add(chkBonusTrans, gridBagConstraints);

        chkPartPay.setText("Part Pay");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        panInsideSchemeDetails.add(chkPartPay, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(9, 1, 0, 1);
        panInsideGeneralRemittance.add(panInsideSchemeDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(panInsideGeneralRemittance, gridBagConstraints);

        panInsideGeneralRemittance1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideGeneralRemittance1.setMinimumSize(new java.awt.Dimension(850, 220));
        panInsideGeneralRemittance1.setPreferredSize(new java.awt.Dimension(850, 220));
        panInsideGeneralRemittance1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 4);
        panGeneralRemittance.add(panInsideGeneralRemittance1, gridBagConstraints);

        tabRemittanceProduct.addTab("Prized Money Payment Details", panGeneralRemittance);

        panOtherDetailsInside.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panOtherDetailsInside.setMinimumSize(new java.awt.Dimension(570, 450));
        panOtherDetailsInside.setPreferredSize(new java.awt.Dimension(570, 450));
        panOtherDetailsInside.setLayout(new java.awt.GridBagLayout());

        panInsideOtherDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panInsideOtherDetails.setMinimumSize(new java.awt.Dimension(880, 600));
        panInsideOtherDetails.setPreferredSize(new java.awt.Dimension(880, 600));
        panInsideOtherDetails.setLayout(new java.awt.GridBagLayout());

        panOtherMDSDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Other MDS details"));
        panOtherMDSDetails.setMinimumSize(new java.awt.Dimension(350, 100));
        panOtherMDSDetails.setPreferredSize(new java.awt.Dimension(350, 100));
        panOtherMDSDetails.setLayout(new java.awt.GridBagLayout());

        lblThalayal.setText("Whether the member is a Thalayal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panOtherMDSDetails.add(lblThalayal, gridBagConstraints);

        lblMunnal.setText("Whether the member is a Munnal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panOtherMDSDetails.add(lblMunnal, gridBagConstraints);

        lblChangedBetween.setText("Whether the member changed in between");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panOtherMDSDetails.add(lblChangedBetween, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherMDSDetails.add(chkThalayal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherMDSDetails.add(chkMunnal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panOtherMDSDetails.add(chkChangedBetween, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideOtherDetails.add(panOtherMDSDetails, gridBagConstraints);

        panTableLiabilityDetailsofSureties.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTableLiabilityDetailsofSureties.setMinimumSize(new java.awt.Dimension(500, 130));
        panTableLiabilityDetailsofSureties.setPreferredSize(new java.awt.Dimension(500, 130));
        panTableLiabilityDetailsofSureties.setLayout(new java.awt.GridBagLayout());

        srpTableDetailsofSureties.setMinimumSize(new java.awt.Dimension(500, 130));
        srpTableDetailsofSureties.setPreferredSize(new java.awt.Dimension(500, 130));

        tblSuretyDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member no", "Name", "MDS Scheme Name", "Division No", "Chit No", "Start Date", "Installment Amount", "installments paid", "Overdue Installments", "Total Amount Due"
            }
        ));
        tblSuretyDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(100, 100));
        srpTableDetailsofSureties.setViewportView(tblSuretyDetails);

        panTableLiabilityDetailsofSureties.add(srpTableDetailsofSureties, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 15, 0);
        panInsideOtherDetails.add(panTableLiabilityDetailsofSureties, gridBagConstraints);

        panTableLoanDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTableLoanDetails.setMinimumSize(new java.awt.Dimension(500, 130));
        panTableLoanDetails.setPreferredSize(new java.awt.Dimension(500, 130));
        panTableLoanDetails.setLayout(new java.awt.GridBagLayout());

        srpTableLoanDetails.setMinimumSize(new java.awt.Dimension(500, 130));
        srpTableLoanDetails.setPreferredSize(new java.awt.Dimension(500, 130));

        tblLoanDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Loan No", "Product id", "Loan  amount", "No. of repayment installments", "Installment Amount", "Balance outstanding"
            }
        ));
        tblLoanDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(110, 100));
        srpTableLoanDetails.setViewportView(tblLoanDetails);

        panTableLoanDetails.add(srpTableLoanDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInsideOtherDetails.add(panTableLoanDetails, gridBagConstraints);

        lblLoanDetails.setText("                                     Loan Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsideOtherDetails.add(lblLoanDetails, gridBagConstraints);

        lblLiabilityMemberDetails.setText("                                     Liability Details of member");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panInsideOtherDetails.add(lblLiabilityMemberDetails, gridBagConstraints);

        panTableChangedMemberDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTableChangedMemberDetails.setMinimumSize(new java.awt.Dimension(500, 130));
        panTableChangedMemberDetails.setPreferredSize(new java.awt.Dimension(500, 130));
        panTableChangedMemberDetails.setLayout(new java.awt.GridBagLayout());

        srpTableChangedMemberDetails.setMinimumSize(new java.awt.Dimension(500, 130));
        srpTableChangedMemberDetails.setPreferredSize(new java.awt.Dimension(500, 130));

        tblChangedMemberDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Earlier Member no", "Earlier Member name", "Changed from Installment no ", "Changed Date"
            }
        ));
        tblChangedMemberDetails.setMinimumSize(new java.awt.Dimension(100, 100));
        tblChangedMemberDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(100, 100));
        srpTableChangedMemberDetails.setViewportView(tblChangedMemberDetails);

        panTableChangedMemberDetails.add(srpTableChangedMemberDetails, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInsideOtherDetails.add(panTableChangedMemberDetails, gridBagConstraints);

        lblLiabilitySuretiesDetails.setText("                                     Liability Details of sureties");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsideOtherDetails.add(lblLiabilitySuretiesDetails, gridBagConstraints);

        panTableLiabilityDetailsofMember.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panTableLiabilityDetailsofMember.setMinimumSize(new java.awt.Dimension(500, 130));
        panTableLiabilityDetailsofMember.setPreferredSize(new java.awt.Dimension(500, 130));
        panTableLiabilityDetailsofMember.setLayout(new java.awt.GridBagLayout());

        srpTableLiabilityDetailsofMember.setMinimumSize(new java.awt.Dimension(500, 130));
        srpTableLiabilityDetailsofMember.setPreferredSize(new java.awt.Dimension(500, 130));

        tblLiabilityDetailsofMember.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Member no", "Name", "MDS Scheme Name", "Division No", "Chit No", "Start Date", "Installment Amount", "installments paid", "Overdue Installments", "Total Amount Due"
            }
        ));
        srpTableLiabilityDetailsofMember.setViewportView(tblLiabilityDetailsofMember);

        panTableLiabilityDetailsofMember.add(srpTableLiabilityDetailsofMember, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panInsideOtherDetails.add(panTableLiabilityDetailsofMember, gridBagConstraints);

        lblChangedMemberDetails.setText("                                     Changed Member Details");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsideOtherDetails.add(lblChangedMemberDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 50, 0);
        panOtherDetailsInside.add(panInsideOtherDetails, gridBagConstraints);

        tabRemittanceProduct.addTab("Other Details", panOtherDetailsInside);

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

    private void txtBonusAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusAmtFocusLost
        // TODO add your handling code here:
        if (txtBonusAmt.getText().length() > 0 && rdoDefaulters_Yes.isSelected() == true) {
            if(!thalayal){
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }else{
                setThalayalTransactionUI();
            }
        }
    }//GEN-LAST:event_txtBonusAmtFocusLost

    private void setThalayalTransactionUI(){
//        HashMap whereMap = new HashMap();
//        double chargeAmount = 0.0;
//        whereMap.put("SCHEME_NAME", txtSchemeName.getText());
//        List chargeList = ClientUtil.executeQuery("getThalayalHeadForChittal", whereMap);
//        if (chargeList != null && chargeList.size() > 0) {            
//             whereMap = (HashMap) chargeList.get(0);
//             if(whereMap!=null && whereMap.get("THALAYAL_REP_PAY_HEAD")!=null && !whereMap.get("THALAYAL_REP_PAY_HEAD").equals("")){
//                 transactionUI.cancelAction(false);
//                 transactionUI.setButtonEnableDisable(true);
//                 transactionUI.resetObjects();
//                 transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
//                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
//                 transactionUI.setCallingTransType("TRANSFER");
//                 System.out.println("transactionUI^$^$^"+transactionUI.getCallingTransType());
//                 transactionUI.setCallingTransProdType("General Ledger");
//                 System.out.println("transactionUI^$^$^"+transactionUI.getCallingTransProdType());
//                 transactionUI.setCallingTransAcctNo(CommonUtil.convertObjToStr(whereMap.get("THALAYAL_REP_PAY_HEAD")));
//                 transactionUI.setCallingApplicantName(CommonUtil.convertObjToStr(whereMap.get("AC_HD_DESC")));
//            }
//        }
        if(thalayal){
        transactionUI.setChitType("THALAYAL");
        }if(munnal){
          transactionUI.setChitType("MUNNAL");  
        }
        String scheme = "";
        HashMap schemeMap = new HashMap();
        schemeMap.put("GROUP_NO", txtGroupName.getText());
        List schemeList = ClientUtil.executeQuery("getGDSScheme", schemeMap);
        int schemeCount = schemeList.size();
        if (schemeList != null && schemeList.size() > 0) {
            schemeMap = (HashMap) schemeList.get(0);
            scheme = CommonUtil.convertObjToStr(schemeMap.get("SCHEME_NAME"));
        }
        transactionUI.setSchemeName(scheme);           
       // transactionUI.setSchemeName(txtGroupName.getText());
    }
    
    private void txtPenalAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalAmtFocusLost
        // TODO add your handling code here:
        if (txtPenalAmt.getText().length() > 0 && rdoDefaulters_Yes.isSelected() == true) {
            if(!thalayal){
            double netAmount = 0.0;
            if (txtTotalAmtPaid.getText().length() > 0) {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            } else {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
            }
            if (CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() >= CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()) {
                txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue())-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
            } else {
                ClientUtil.showAlertWindow("Total Credit Amount Should not be more than the prized money amount");
                txtPenalAmt.setText("");
                txtNetAmt.setText(String.valueOf(netAmount));
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(txtNetAmt.getText());
            //            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }else{
                setThalayalTransactionUI();
            }
        }
    }//GEN-LAST:event_txtPenalAmtFocusLost

    private void txtBonusRecoveredFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusRecoveredFocusLost
        // TODO add your handling code here:
        if (txtBonusRecovered.getText().length() > 0 && rdoDefaulters_Yes.isSelected() == true) {
            if(!thalayal){
            double netAmount = 0.0;
            if (txtTotalAmtPaid.getText().length() > 0) {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            } else {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
            }
            if (CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() >= CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue()) {
                txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()
                        + CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue())));
            } else {
                ClientUtil.showAlertWindow("Total Credit Amount Should not be more than the prized money amount");
                txtBonusRecovered.setText("");
                txtNetAmt.setText(String.valueOf(netAmount));
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(txtNetAmt.getText());
//            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }else{
            setThalayalTransactionUI();
    }
    }//GEN-LAST:event_txtBonusRecoveredFocusLost
    }
    private void rdoDefaulters_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDefaulters_YesActionPerformed
        // TODO add your handling code here:
        System.out.println("hhhh");
        if (!observable.getDefaulter().equals("Y")) {
            defaulterEnableDisable(true);
            txtBonusRecovered.setVisible(false);
            lblBonusRecovered.setVisible(false);
            if (CommonUtil.convertObjToStr(CommonConstants.OPERATE_MODE).equals(CommonConstants.IMPLEMENTATION)) {
                txtPenalAmt.setEnabled(true);
            } else {
                txtPenalAmt.setEnabled(false);
            }
            calculatePenalInterest();
            txtPenalAmtFocusLost(null);
            txtBonusAmt.setEnabled(true);
            boolean recoveredAmt = checkRecovedAmount();
        }
        if(rdoDefaulters_Yes.isSelected()){
            chkPartPay.setVisible(false);
            chkPartPay.setEnabled(false);
        }
    }//GEN-LAST:event_rdoDefaulters_YesActionPerformed
    public void calculatePenalInterest() {
        HashMap schemeMap = new HashMap();
        HashMap productMap = new HashMap();
        schemeMap.put("GROUP_NO", txtGroupName.getText());
        List lst = ClientUtil.executeQuery("getSelectAllSchemeAcctHead", schemeMap);
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
            String bonusFirstInst = CommonUtil.convertObjToStr(productMap.get("ADVANCE_COLLECTION"));
            int startNoForPenal = 0;
            int addNo = 1;
            int firstInst_No = -1;
            if (bonusFirstInst.equals("Y")) {
                startNoForPenal = 1;
                addNo = 0;
                firstInst_No = 0;
            }
            long diffDayPending = 0;
            int noOfInsPaid = 0;
            String penalIntType = "";
            long penalValue = 0;
            Date instDate = null;
            Date nextInstDate = null;
            String penalGraceType = "";
            long penalGraceValue = 0;
            String penalCalcBaseOn = "";
            noOfInsPaid = CommonUtil.convertObjToInt(txtNoOfInstPaid.getText());
            int totalInst = CommonUtil.convertObjToInt(productMap.get("NO_OF_INSTALLMENTS"));
            int pendingInst = 0;
            pendingInst = totalInst - noOfInsPaid;
            double calc = 0;
            if (pendingInst > 0) {
                long pendingInstallment = CommonUtil.convertObjToLong(String.valueOf(pendingInst));
                System.out.println("########### pendingInst : " + pendingInst);
                penalIntType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_INT_TYPE"));
                penalValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_INT_AMT"));
                penalGraceType = CommonUtil.convertObjToStr(productMap.get("PENAL_PRIZED_GRACE_PERIOD_TYPE"));
                penalGraceValue = CommonUtil.convertObjToLong(productMap.get("PENAL_PRIZED_GRACE_PERIOD"));
                penalCalcBaseOn = CommonUtil.convertObjToStr(productMap.get("PENAL_CALC"));
                double insAmt = CommonUtil.convertObjToDouble(productMap.get("INSTALLMENT_AMOUNT")).doubleValue();
                Date endDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(productMap.get("SCHEME_END_DT")));
                System.out.println("###### endDate : " + endDate);
                Date currentDate = (Date) currDt.clone();
                //Added By Suresh
                if (DateUtil.dateDiff(currentDate, endDate) > 0) {
                    currentDate = endDate;
                }

                for (int i = startNoForPenal; i < pendingInst + startNoForPenal; i++) {
                    HashMap nextInstMap = new HashMap();
                    nextInstMap.put("GROUP_NO", txtGroupName.getText());
                    nextInstMap.put("DIVISION_NO", txtDivisionNo.getText());
                    nextInstMap.put("SL_NO", new Double(i + noOfInsPaid));
                    List listRec = ClientUtil.executeQuery("getGDSSelectNextInstDate", nextInstMap);
                    if (listRec != null && listRec.size() > 0) {
                        double penal = 0;
                        nextInstMap = (HashMap) listRec.get(0);
                        instDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(nextInstMap.get("NEXT_INSTALLMENT_DATE")));
                        System.out.println("########### ListInstDate : " + instDate);
                    } else {
                        nextInstDate = instDate;
                        nextInstDate.setMonth(nextInstDate.getMonth() + 1);
                        instDate = nextInstDate;
                        System.out.println("########### instDate : " + instDate);
                    }
                    diffDayPending = DateUtil.dateDiff(instDate, currentDate);
                    System.out.println("########### diffDay : " + diffDayPending);
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
                            System.out.println("#### diffDayPending Holiday True : " + diffDayPending);
                            if (CommonUtil.convertObjToStr(productMap.get("HOLIDAY_INT")).equals("any next working day")) {
                                diffDayPending -= 1;
                                instDate.setDate(instDate.getDate() + 1);
                            } else {
                                diffDayPending += 1;
                                instDate.setDate(instDate.getDate() - 1);
                            }
                            holidayMap.put("NEXT_DATE", instDate);
                            checkHoliday = true;
                            System.out.println("#### holidayMap : " + holidayMap);
                        } else {
                            System.out.println("#### diffDay Holiday False : " + diffDayPending);
                            checkHoliday = false;
                        }
                    }
                    System.out.println("#### diffDayPending Final : " + diffDayPending);


                    if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Days")) {
                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                            if (diffDayPending > penalGraceValue) {
                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                    calc += (diffDayPending * penalValue * insAmt) / 36500;
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                    calc += penalValue;
                                }
                            }
                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                            if (diffDayPending > penalGraceValue) {
                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInstallment--;
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                    calc += penalValue;
                                }
                            }
                        }
                    } else if (penalCalcBaseOn != null && !penalCalcBaseOn.equals("") && penalCalcBaseOn.equals("Installments")) {
                        if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Days")) {
                            if (diffDayPending > penalGraceValue) {
                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInstallment--;
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                    calc += penalValue;
                                }
                            }
                        } else if (penalGraceType != null && !penalGraceType.equals("") && penalGraceType.equals("Installments")) {
                            if (diffDayPending > penalGraceValue) {
                                if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Percent")) {
                                    calc += (double) ((insAmt * penalValue) / 1200.0) * pendingInstallment--;
                                } else if (penalIntType != null && !penalIntType.equals("") && penalIntType.equals("Absolute")) {
                                    calc += penalValue;
                                }
                            }
                        }
                    }
                }
            }
            if (calc > 0) {
                txtPenalAmt.setText(String.valueOf((long) (calc + 0.5)));
            }
        }
    }

    public boolean checkRecovedAmount() {
        boolean recoveredAmt = true;
        int totalInst = CommonUtil.convertObjToInt(txtTotalInst.getText());
        int paidInst = CommonUtil.convertObjToInt(txtNoOfInstPaid.getText());
        int balanceInst = totalInst - paidInst;
        double instAmount = 0.0;
        instAmount = CommonUtil.convertObjToDouble(observable.getInstAmount()).doubleValue();        
        //txtOverDueAmt.setText(String.valueOf((instAmount * balanceInst) + (CommonUtil.convertObjToDouble(txtOverdueInst.getText()).doubleValue() * instAmount)));        
        if (CommonUtil.convertObjToDouble(txtOverDueAmt.getText()).doubleValue() > CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue()) {
            txtNetAmt.setText("0");
            ClientUtil.showMessageWindow("Can not make Payment !!! Amount to be Recovered From the Chittal Rs. " + (CommonUtil.convertObjToDouble(txtOverDueAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()));
            recoveredAmt = false;
        }
        return recoveredAmt;
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) currDt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private void rdoDefaulters_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDefaulters_NoActionPerformed
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && txtGdsNo.getText().length() > 0 && txtSubNo.getText().length() > 0) {
            boolean security = checkSecurityDetails();
            if (security == false) {
                return;
            }
        }
        defaulterEnableDisable(false);
        txtPenalAmt.setText("");
        txtBonusRecovered.setText("");
        txtBonusAmt.setEnabled(false);
        resetAmount();
        int overDueInst = CommonUtil.convertObjToInt(txtOverdueInst.getText());
        double instAmount = 0.0;
        instAmount = CommonUtil.convertObjToDouble(observable.getInstAmount()).doubleValue();
        txtOverDueAmt.setText(String.valueOf((instAmount * overDueInst)));   
        if(rdoDefaulters_No.isSelected()){
            chkPartPay.setVisible(true);
            chkPartPay.setEnabled(true);
        }else{
            chkPartPay.setVisible(false);
            chkPartPay.setEnabled(false);
        }
    }//GEN-LAST:event_rdoDefaulters_NoActionPerformed
    private void resetAmount() {
        double netAmount = 0.0;
        if(!thalayal){
        if (txtTotalAmtPaid.getText().length() > 0) {
            netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
        } else {
            netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
        }
        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue())-CommonUtil.convertObjToDouble(lblServiceTaxval.getText()).doubleValue()));
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
//        transactionUI.setCallingAmount(txtNetAmt.getText());
//        transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
        transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }else{
            setThalayalTransactionUI();
        }
        }

    private void defaulterEnableDisable(boolean flag) {
        lblPenalAmt.setVisible(flag);
        txtPenalAmt.setVisible(flag);
        lblBonusRecovered.setVisible(flag);
        txtBonusRecovered.setVisible(flag);
    }
    private void txtAmountRefundedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountRefundedFocusLost
        // TODO add your handling code here:
        if (txtAmountRefunded.getText().length() > 0) {
            if(!thalayal){
            if (CommonUtil.convertObjToDouble(txtAmountRefunded.getText()).doubleValue() > CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue()) {
                ClientUtil.showMessageWindow("Refunded Amount Should Be Equal Or Less Than Toatal Amount Paid !!! ");
                txtAmountRefunded.setText("");
            }
            if (txtAmountRefunded.getText().length() > 0) {
                double earnedBonus = 0.0;
                double bonus = 0.0;
                HashMap hashMap = new HashMap();
                hashMap.put("CHITTAL_NO", txtGdsNo.getText());
                hashMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));
                earnedBonus = CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtAmountRefunded.getText()).doubleValue();
                List oldMemList = ClientUtil.executeQuery("getMDSChangeOfMemDetails", hashMap);
                if (oldMemList != null && oldMemList.size() > 0) {
                    hashMap = (HashMap) oldMemList.get(0);
                    bonus = CommonUtil.convertObjToDouble(hashMap.get("OLD_MEMBER_BONUS_EARNED")).doubleValue();
                    if (bonus < earnedBonus) {
                        int yesNo = 0;
                        String[] options = {"OK", "CANCEL"};
                        yesNo = COptionPane.showOptionDialog(null, "You are Recovering More Than The Bonus Paid ", CommonConstants.WARNINGTITLE,
                                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                null, options, options[0]);
                        System.out.println("#$#$$ yesNo : " + yesNo);
                        if (yesNo == 0) {
                        } else {
                            txtAmountRefunded.setText("");
                        }
                    }
                }

            }
            double netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue())));
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(txtNetAmt.getText());
//            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }
    }else{
    setThalayalTransactionUI();
}
    }//GEN-LAST:event_txtAmountRefundedFocusLost

    private void btnViewLedgerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewLedgerActionPerformed
        // TODO add your handling code here:
        if (txtGdsNo.getText().length() > 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = new HashMap();
            paramMap.put("ChittalNo", txtGdsNo.getText());
            paramMap.put("SubNo", txtSubNo.getText());
            paramMap.put("FromDt", currDt);
            paramMap.put("ToDt", currDt);
            paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
            ttIntgration.setParam(paramMap);
            ttIntgration.integration("MDSLedger");
        } else {
            ClientUtil.displayAlert("Chittal No Should Not Be Empty!!! ");
        }
    }//GEN-LAST:event_btnViewLedgerActionPerformed

    private void txtGdsNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGdsNoFocusLost
        // TODO add your handling code here:
        if (txtGdsNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CHITTAL_NO", txtGdsNo.getText());
            List lst = ClientUtil.executeQuery("getSelectChittalNo", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "CHITTAL_NO";
                whereMap = (HashMap) lst.get(0);
                List chitLst = ClientUtil.executeQuery("getSelctChittalReceiptDetails", whereMap);
                if (chitLst != null && chitLst.size() > 0) {
                    whereMap = (HashMap) chitLst.get(0);
                    transactionUI.setCallingTransType("CASH");
                    transactionUI.setCallingApplicantName(lblMemberNameVal.getText());
                    fillData(whereMap);                    
                    chitLst = null;
                    lst = null;
                    whereMap = null;
                } else {
                    ClientUtil.displayAlert("Not A Prized Chittal OR Already Prized !!! ");
                    txtGdsNo.setText("");
                    ClientUtil.clearAll(this);
                }
            } else {
                ClientUtil.displayAlert("Invalid Chittal No !!! ");
                txtGdsNo.setText("");
                ClientUtil.clearAll(this);
            }


            if (observable.getDefaulter().equals("Y")) {
                transactionUI.setButtonEnableDisable(false);
                transactionUI.cancelAction(false);
                transactionUI.resetObjects();
                transactionUI.setCallingApplicantName("");
                transactionUI.setCallingAmount("");
                transactionUI.okAction(false);
                transactionUI.setButtonEnableDisable(false);
                System.out.println("herrrrrr");
            }
        }
        //added by sreekrishnan-------------------
        displayLoanLienSecurityDetails();
        displayMDSLienSecurityDetails(); 
//added by sreekrishnan-------------------
    }//GEN-LAST:event_txtGdsNoFocusLost
private void displayLoanLienSecurityDetails() {
        if (txtGdsNo.getText().length() > 0 && txtSubNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("DEPOSIT_NO", txtGdsNo.getText() + "_" + txtSubNo.getText());
            List recordList = ClientUtil.executeQuery("getLienLoan", whereMap);
            if (recordList != null && recordList.size() > 0) {
                whereMap = (HashMap) recordList.get(0);
                int yesNo = 0;
                                    String[] options = {"Yes", "No"};
                                    yesNo = COptionPane.showOptionDialog(null, "This Chittal Lien Marked on this Loan " + whereMap.get("LIEN_AC_NO") + "\n" + "Do you want to Continue?", CommonConstants.WARNINGTITLE,
                                          COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                          null, options, options[0]);
                                  System.out.println("#$#$$ yesNo : " + yesNo);
                                   if (yesNo == 0) {
                                    } else {
                                        btnCancelActionPerformed(null);
                                        return;
                                    }
                               
            }
        }
    }
 private void displayMDSLienSecurityDetails() {
        if (txtGdsNo.getText().length() > 0 && txtSubNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("DEPOSIT_NO", txtGdsNo.getText() + "_" + txtSubNo.getText());
            List recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinMaster", whereMap);
            if (recordList != null && recordList.size() > 0) {
                whereMap = (HashMap) recordList.get(0);
                int yesNo = 0;
                                    String[] options = {"Yes", "No"};
                                    yesNo = COptionPane.showOptionDialog(null, "This Chittal Lien Marked on this Loan " + whereMap.get("CHITTAL_NO") + "\n" + "Do you want to Continue?", CommonConstants.WARNINGTITLE,
                                          COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                          null, options, options[0]);
                                  System.out.println("#$#$$ yesNo : " + yesNo);
                                   if (yesNo == 0) {
                                    } else {
                                        btnCancelActionPerformed(null);
                                        return;
                                    }
                

            }
        }
    }
    private void txtGroupNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGroupNameFocusLost
        // TODO add your handling code here:
        if (txtGroupName.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("SCHEME_NAME", txtGroupName.getText());
            List lst = ClientUtil.executeQuery("getSelectSchemeName", whereMap);
            if (lst != null && lst.size() > 0) {
                observable.setTxtGroupName(txtGroupName.getText());
                viewType = "SCHEME_DETAILS";
                whereMap = (HashMap) lst.get(0);
                fillData(whereMap);
                lst = null;
                whereMap = null;
            } else {
                ClientUtil.displayAlert("Invalid Scheme Name !!! ");
                txtGroupName.setText("");
                txtGdsNo.setText("");
            }
        }
    }//GEN-LAST:event_txtGroupNameFocusLost
    private void setCaseExpensesAmount() {
        HashMap whereMap = new HashMap();
        double chargeAmount = 0.0;
        whereMap.put("ACT_NUM", txtGdsNo.getText() + "_" + txtSubNo.getText());
        List chargeList = ClientUtil.executeQuery("getMDSCaseChargeDetails", whereMap);
        if (chargeList != null && chargeList.size() > 0) {
            for (int i = 0; i < chargeList.size(); i++) {
                whereMap = (HashMap) chargeList.get(i);
                chargeAmount += CommonUtil.convertObjToDouble(whereMap.get("CHARGE_AMT")).doubleValue();
            }
            Rounding rod = new Rounding();
            chargeAmount = (double) rod.getNearest((long) (chargeAmount * 100), 100) / 100;
            txtAribitrationAmt.setText(String.valueOf(chargeAmount));
            txtAribitrationAmt.setEnabled(false);
        }
        chargeList = null;
    }
    private void txtDiscountAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountAmtFocusLost
        // TODO add your handling code here:
        if (txtDiscountAmt.getText().length() > 0) {
            if(!thalayal){
            setCaseExpensesAmount();
            double netAmount = 0.0;
            if (txtTotalAmtPaid.getText().length() > 0) {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            } else {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
            }
            if (CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() >= CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()) {
                if (rdoDefaulters_Yes.isSelected() == true || rdoDefaulters_No.isSelected() == true) {
                    if (rdoDefaulters_Yes.isSelected() == true) {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue())));
                    } else {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue())));
                    }
                } else {
                    ClientUtil.showAlertWindow("Please Select Defaulters Yes or No !!! ");
                    txtDiscountAmt.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Total Credit Amount Should not be more than the prized money amount");
                txtDiscountAmt.setText("");
                txtNetAmt.setText(String.valueOf(netAmount));
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(txtNetAmt.getText());
//            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }
        else{
            setThalayalTransactionUI();
        }
    }
    }//GEN-LAST:event_txtDiscountAmtFocusLost

    private void txtAribitrationAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAribitrationAmtFocusLost
        // TODO add your handling code here:
        if (txtAribitrationAmt.getText().length() > 0) {
            if(!thalayal){
            double netAmount = 0.0;
            if (txtTotalAmtPaid.getText().length() > 0) {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            } else {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
            }
            if (CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() >= CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()) {
                if (rdoDefaulters_Yes.isSelected() == true || rdoDefaulters_No.isSelected() == true) {
                    if (rdoDefaulters_Yes.isSelected() == true) {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue())));
                    } else {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue())));
                    }
                } else {
                    ClientUtil.showAlertWindow("Please Select Defaulters Yes or No !!! ");
                    txtAribitrationAmt.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Total Credit Amount Should not be more than the prized money amount");
                txtAribitrationAmt.setText("");
                txtNetAmt.setText(String.valueOf(netAmount));
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(txtNetAmt.getText());
//            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }else{
            
            setThalayalTransactionUI();
        }
    }
    }//GEN-LAST:event_txtAribitrationAmtFocusLost

    private void txtNoticeAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoticeAmtFocusLost
        // TODO add your handling code here:
        if (txtNoticeAmt.getText().length() > 0) {
            if(!thalayal){
            setCaseExpensesAmount();
            double netAmount = 0.0;
            if (txtTotalAmtPaid.getText().length() > 0) {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            } else {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
            }
            if (CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() >= CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()) {
                if (rdoDefaulters_Yes.isSelected() == true || rdoDefaulters_No.isSelected() == true) {
                    if (rdoDefaulters_Yes.isSelected() == true) {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue())));
                    } else {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue())));
                    }
                } else {
                    ClientUtil.showAlertWindow("Please Select Defaulters Yes or No !!! ");
                    txtNoticeAmt.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Total Credit Amount Should not be more than the prized money amount");
                txtNoticeAmt.setText("");
                txtNetAmt.setText(String.valueOf(netAmount));
            }
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(txtNetAmt.getText());
//            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }else{
            
            setThalayalTransactionUI();
        }
        }
        
    }//GEN-LAST:event_txtNoticeAmtFocusLost

    private void txtNetAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNetAmtFocusLost
        // TODO add your handling code here:
//        transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
        transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
    }//GEN-LAST:event_txtNetAmtFocusLost

    private void btnMemberNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMemberNoActionPerformed
        // TODO add your handling code here:
        popUp("MEMBER_NO");
    }//GEN-LAST:event_btnMemberNoActionPerformed

    private void btnChittalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChittalNoActionPerformed
        // TODO add your handling code here:
        popUp("CHITTAL_NO");
        // Commented the below line after thre review from testing
        //transactionUI.setCallingTransType("CASH"); // Removed the comment by nithya on 25-08-2016
        transactionUI.setCallingApplicantName(lblMemberNameVal.getText()); // Removed the comment by nithya on 25-08-2016
        System.out.println("grere");
        if (observable.getDefaulter().equals("Y")) {
            transactionUI.setButtonEnableDisable(false);
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setCallingApplicantName("");
            transactionUI.setCallingAmount("");
            transactionUI.okAction(false);
            transactionUI.setButtonEnableDisable(false);
            System.out.println("herrrrrr");
        }
         //added by sreekrishnan-------------------
        displayLoanLienSecurityDetails();
        displayMDSLienSecurityDetails();
        //added by sreekrishnan-------------------
        btnChittalNo.setEnabled(true);
        btnSchemeName.setEnabled(true);
    }//GEN-LAST:event_btnChittalNoActionPerformed
    private void setFieldClear() {
        txtGdsNo.setText("");
        txtDivisionNo.setText("");
        observable.setTxtGdsNo("");
        txtSubNo.setText("");
        tdtDrawDate.setDateValue("");
        txtTotalInst.setText("");
        txtNoOfInstPaid.setText("");
        txtOverdueInst.setText("");
        btnMemberNo.setEnabled(true);
        //        observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
        txtMemberNo.setText("");
        lblMemberNameVal.setText("");
        lblValStreet.setText("");
        lblValArea.setText("");
        lblValCity.setText("");
        lblValState.setText("");
        lblValPin.setText("");
    }
    private void btnSchemeNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSchemeNameActionPerformed
        // TODO add your handling code here:
        popUp("SCHEME_DETAILS");
        setFieldClear();
        //Added BY Suresh 
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && txtGroupName.getText().length() > 0) {
            txtGdsNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID + txtGroupName.getText()));
        } else {
            txtGdsNo.setText("");
        }
        observable.setTxtGdsNo(txtGdsNo.getText());
        rdoDefaulters_Yes.setEnabled(false);
    }//GEN-LAST:event_btnSchemeNameActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panGeneralRemittance, false);
        ClientUtil.enableDisable(panOtherDetailsInside, false);
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_REJECT);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {

        if (viewType == AUTHORIZE && isFilled) {

            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                HashMap countMap = new HashMap();
                System.out.println("observable.getIRNo()asssobservable.getCashId()>>>>>" + observable.getCashId());
                countMap.put("IRID", observable.getTransId());
                List countList = ClientUtil.executeQuery("getCountForReceiptCashierAuthorizeMDS", countMap);
                if (countList != null && countList.size() > 0) {
                    System.out.println("nnnjdj>>>???");
                    countMap = new HashMap();
                    countMap = (HashMap) countList.get(0);
                    //if (CommonUtil.convertObjToInt(countMap.get("COUNT")) != 0) {
                    //    System.out.println("nnnjdj4566>>>???");
                        //ClientUtil.showMessageWindow("Receipt cash transaction authorization for the cash Id is pending\nPlease authorize the pending receipt cash transactions for the cash Id first");
                      //  return;
                   // }
                }
            }

            int option = 0;
            if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)) {
                String[] obj = {"Ok", "Cancel"};
                option = COptionPane.showOptionDialog(null, (" Do you want to reject ..."),
                        ("Select The Desired Option"),
                        COptionPane.YES_NO_CANCEL_OPTION, COptionPane.QUESTION_MESSAGE, null, obj, obj[0]);
            }
            System.out.println("opptionn" + option);
            if (option == 1) {
                btnCancelActionPerformed(null);
            } else {


                ArrayList arrList = new ArrayList();
                HashMap authorizeMap = new HashMap();
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put("STATUS", authorizeStatus);
                singleAuthorizeMap.put("GDS_NO", observable.getTxtGdsNo());
                singleAuthorizeMap.put("SUB_NO", CommonUtil.convertObjToInt(observable.getTxtSubNo()));
                singleAuthorizeMap.put("TRANS_ID", observable.getTransId());
               // singleAuthorizeMap.put("SCHEME_NAME", observable.getTxtGroupName());
                singleAuthorizeMap.put("GROUP_NAME", observable.getTxtGroupName());
                singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
                singleAuthorizeMap.put("AUTHORIZED_DT", currDt);
                arrList.add(singleAuthorizeMap);
                if (!CommonUtil.convertObjToStr(observable.getCashId()).equals("")) {
                    authorizeMap.put("CASH_ID", observable.getCashId());
                }
                authorizeMap.put("TRANS_ID", observable.getTransId());
                authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
                authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
                authorize(authorizeMap, observable.getTxtGroupName());
                viewType = "";
                super.setOpenForEditBy(observable.getStatusBy());
                singleAuthorizeMap = null;
                arrList = null;
                authorizeMap = null;
            }
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            whereMap.put("CASHIER_AUTH_ALLOWED", TrueTransactMain.CASHIER_AUTH_ALLOWED);
            whereMap.put("TRANS_DT", currDt.clone());
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.HIERARCHY_ID, ProxyParameters.HIERARCHY_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            if (TrueTransactMain.CASHIER_AUTH_ALLOWED != null && TrueTransactMain.CASHIER_AUTH_ALLOWED.equals("Y")) {
                mapParam.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentCashierAuthorize");
            } else {
                mapParam.put(CommonConstants.MAP_NAME, "getGDSPrizedMoneyPaymentAuthorize");
            }
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
         }
    }

    public void authorize(HashMap map, String id) {
        System.out.println("Authorize Map : " + map);

        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            if (transactionUI.getOutputTO().size() > 0) {
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
                 if (fromNewAuthorizeUI) {
                    newauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    newauthorizeListUI.setFocusToTable();
                    newauthorizeListUI.displayDetails("GDS Prized Money Payment");
                }
                if (fromAuthorizeUI) {
                    authorizeListUI.removeSelectedRow();
                    this.dispose();
                    authorizeListUI.setFocusToTable();
                    authorizeListUI.displayDetails("MDS Payments");
                }
                if (fromCashierAuthorizeUI) {
                    CashierauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    CashierauthorizeListUI.setFocusToTable();
                } 
                if (fromManagerAuthorizeUI) {
                    ManagerauthorizeListUI.removeSelectedRow();
                    this.dispose();
                    ManagerauthorizeListUI.setFocusToTable();
                }
    
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panInsideGeneralRemittance, true);
        ClientUtil.enableDisable(panOtherDetailsInside, true);
        ClientUtil.clearAll(this);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        txtDivisionNo.setEnabled(false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_NEW);
        transactionUI.cancelAction(false);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        txtGroupName.setEnabled(true);
        txtGdsNo.setEnabled(true);
        txtMemberNo.setEnabled(false);
        btnSchemeName.setEnabled(true);
        btnChittalNo.setEnabled(true);
        btnMemberNo.setEnabled(true);
        txtOverDueAmt.setEnabled(false);
        txtPrizedInstNo.setEnabled(false);
        txtBonusAmt.setEnabled(false);
        tdtDrawDate.setEnabled(false);
        txtCommisionAmt.setEnabled(false);
        txtPrizedAmt.setEnabled(false);
        txtSubNo.setEnabled(false);
        txtNetAmt.setEnabled(false);
        txtOverdueInst.setEnabled(false);
        txtTotalInst.setEnabled(false);
        txtNoOfInstPaid.setEnabled(false);
        txtBonusAmt.setEnabled(false);
//        rdoDefaulters_No.setSelected(true);
        rdoDefaulters_NoActionPerformed(null);
        ClientUtil.enableDisable(panDefaulters, false);
        chkPartPay.setSelected(false);
        chkPartPay.setVisible(false);
        chkPartPay.setEnabled(false);
        thalayal = false;
        munnal = false;
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        ClientUtil.enableDisable(panInsideGeneralRemittance, true);
        popUp("Edit");
        btnDelete.setEnabled(false);
        txtGroupName.setEnabled(false);
        txtGdsNo.setEnabled(false);
        txtMemberNo.setEnabled(false);
        btnSave.setEnabled(true);
        txtOverDueAmt.setEnabled(false);
        txtPrizedInstNo.setEnabled(false);
        txtBonusAmt.setEnabled(false);
        txtCommisionAmt.setEnabled(false);
        txtNetAmt.setEnabled(false);
        txtPrizedAmt.setEnabled(false);
        txtSubNo.setEnabled(false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_EDIT);
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        calculateOtherChargeAmount();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panGeneralRemittance, false);
        ClientUtil.enableDisable(panOtherDetailsInside, false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_DELETE);
        transactionUI.cancelAction(false);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
       
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInsideGeneralRemittance);
        if (observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0) {
            displayAlert(mandatoryMessage);
        } else {
            savePerformed();
            btnCancel.setEnabled(true);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void savePerformed() {
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && rdoDefaulters_No.isSelected() == true) {
            if (!observable.getDefaulter().equals("Y")) {
                boolean security = checkSecurityDetails();
                if (security == false) {
                    return;
                }
            }
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && rdoDefaulters_Yes.isSelected() == true) {
            if (!observable.getDefaulter().equals("Y")) {
                boolean recoveredAmt = checkRecovedAmount();
                if (recoveredAmt == false) {
                    btnCancelActionPerformed(null);
                    return;
                }
            }
        }
        HashMap whereMap = new HashMap();
        whereMap.put("DEPOSIT_NO", txtGdsNo.getText());
        if (!txtSubNo.getText().equals("")) {
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));
        } else {
            whereMap.put("SUB_NO", CommonUtil.convertObjToInt("1"));
        }
        List lienList = ClientUtil.executeQuery("checkLienOrNot", whereMap);
        if (lienList != null && lienList.size() > 0) {
            whereMap = (HashMap) lienList.get(0);
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "This Chittal is Lien Marked  \n" + "Do you want to Continue?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
           
            if (yesNo == 0) {
            } else {
                btnCancelActionPerformed(null);
                return;
            }
        }



        //        double totalNetAmount = CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue();
        double totalNetAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()
                + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
         System.out.println("#$#$$ totalNetAmount : " + totalNetAmount);
        int transactionSize = 0;

        if (!observable.getDefaulter().equals("Y")) {
            if (transactionUI.getOutputTO() == null && totalNetAmount != 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else {
                transactionSize = (transactionUI.getOutputTO()).size();
                observable.setAllowedTransactionDetailsTO(transactionUI.getOutputTO());
            }
            if (transactionSize == 0 && totalNetAmount != 0) {
                ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NO_RECORDS));
                return;
            } else if (transactionSize != 0 || totalNetAmount == 0) {
                if (!transactionUI.isBtnSaveTransactionDetailsFlag()) {
                    ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.SAVE_TX_DETAILS));
                    return;
                } else {
                    double transTotalAmt = CommonUtil.convertObjToDouble(transactionUI.getTransactionOB().getLblTotalTransactionAmtVal()).doubleValue();
                   if (ClientUtil.checkTotalAmountTallied(totalNetAmount, transTotalAmt) == false && totalNetAmount != 0) {
                        ClientUtil.showAlertWindow(transactionUI.getMessage(TransactionUI.NOT_TALLY));
                        return;
                    } else {
                        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                            //Checking For Loan Security
                            whereMap = new HashMap();
                            whereMap.put("DEPOSIT_NO", txtGdsNo.getText() + "_" + txtSubNo.getText());
                            List recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinLoansDeposit", whereMap);
                            if (recordList != null && recordList.size() > 0) {
                                whereMap = (HashMap) recordList.get(0);
                                List loanList = ClientUtil.executeQuery("getLoanFacilityDetailsFromBorrowNo", whereMap);
                                if (loanList != null && loanList.size() > 0) {
                                    whereMap = (HashMap) loanList.get(0);
                                    int yesNo = 0;
                                    String[] options = {"Yes", "No"};
                                    yesNo = COptionPane.showOptionDialog(null, "This Chittal Lien Marked on this Loan " + whereMap.get("ACCT_NUM") + "\n" + "Do you want to Continue?", CommonConstants.WARNINGTITLE,
                                            COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                            null, options, options[0]);
                                    System.out.println("#$#$$ yesNo : " + yesNo);
                                    if (yesNo == 0) {
                                    } else {
                                        btnCancelActionPerformed(null);
                                        return;
                                    }
                                }
                            }
//                           else 


                            //Checking MDS Master Security
                            whereMap = new HashMap();
                            whereMap.put("DEPOSIT_NO", txtGdsNo.getText() + "_" + txtSubNo.getText());
                            recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinMaster", whereMap);
                            if (recordList != null && recordList.size() > 0) {
                                whereMap = (HashMap) recordList.get(0);
                                int yesNo = 0;
                                String[] options = {"Yes", "No"};
                                yesNo = COptionPane.showOptionDialog(null, "This Chittal Lien Marked on this MDS " + whereMap.get("CHITTAL_NO") + "_" + whereMap.get("SUB_NO") + "\n" + "Do you want to Continue?", CommonConstants.WARNINGTITLE,
                                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                                        null, options, options[0]);
                                System.out.println("#$#$$ yesNo : " + yesNo);
                                if (yesNo == 0) {
                                } else {
                                    btnCancelActionPerformed(null);
                                    return;
                                }
                            }
                        }
                        boolean interbranchFlag = false;
                        System.out.println("transactionUI.getTransactionOB()"+ transactionUI.getTransactionOB());
                        System.out.println("transactionUI.getTransactionOB().getSelectedTxnType() :: "+ transactionUI.getTransactionOB().getSelectedTxnType());
                        System.out.println("observable.getSelectedBranchID() :"+observable.getSelectedBranchID());
                        if(transactionUI.getTransactionOB().getSelectedTxnType() != null && observable.getSelectedBranchID() != null){
                            System.out.println("interbranchFlag value"+interbranchFlag+ "  getSelectedTxnType() "+transactionUI.getTransactionOB().getSelectedTxnType()); 
                           if(transactionUI.getTransactionOB().getSelectedTxnType().equals(CommonConstants.TX_TRANSFER)){
                               System.out.println("interbranchFlag value"+interbranchFlag); 
                            if(ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())){
                                    interbranchFlag = false;
                                    System.out.println("interbranchFlag value"+interbranchFlag); 
                                }else if(ProxyParameters.BRANCH_ID.equals(transactionUI.getTransactionOB().getSelectedTxnBranchId())){
                                    interbranchFlag = false;
                                    System.out.println("interbranchFlag value"+interbranchFlag); 
                                }else {
                                    interbranchFlag = true;
                                    System.out.println("interbranchFlag value"+interbranchFlag); 
                                }
                            }else{
                                interbranchFlag = false;
                                System.out.println("interbranchFlag value"+interbranchFlag); 
                            }
                            if(interbranchFlag){
                                ClientUtil.displayAlert("Incase of interbranch transaction,either Dr or Cr account of the transaction should be of own branch");
                            }else{
                                observable.doAction();
                            }
                        }
                    }
                }
            }
        } else {
            // transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
            transactionUI.setButtonEnableDisable(false);
            transactionUI.cancelAction(false);
            transactionUI.resetObjects();
            transactionUI.setCallingApplicantName("");
            transactionUI.setCallingAmount("");
            transactionUI.okAction(false);
            transactionUI.setButtonEnableDisable(false);
            System.out.println("herrrrrr");
            boolean interbranchFlag = false;
            if(transactionUI.getTransactionOB().getSelectedTxnType() != null && observable.getSelectedBranchID() != null){
                if(transactionUI.getTransactionOB().getSelectedTxnType().equals(CommonConstants.TX_TRANSFER)){
                    if(ProxyParameters.BRANCH_ID.equals(observable.getSelectedBranchID())){
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
                }else{
                    observable.doAction();
                }
            }
        }
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {

            HashMap exMap = new HashMap();
            exMap = observable.getProxyReturnMap();
            if (exMap != null && exMap.containsKey("EXECPTION_DEF") && exMap.get("EXECPTION_DEF") != null && !exMap.get("EXECPTION_DEF").toString().equals("")) {
                if (exMap.get("EXECPTION_DEF").toString().equals("Y")) {
                    displayAlert("Cannot make defaulter payment now");
                    btnCancelActionPerformed(null);
                }
            }

            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if (observable.getProxyReturnMap().containsKey("CASH_TRANS_LIST") || observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetailMDS(observable.getProxyReturnMap(),true);
                }
            }
            System.out.println("before btnCancelActionPerformed(null);");
            
            btnCancelActionPerformed(null);
            System.out.println("after btnCancelActionPerformed(null);");
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            if(fromSmartCustUI){
                System.out.println("fromSmartCustUI#%#%"+fromSmartCustUI);
                this.dispose();
                fromSmartCustUI = false;
            }
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

    private void displayTransDetail(HashMap proxyResultMap) {
          System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
    String str1 = "Cash Transaction Details...\n";
    String str2 = "Transfer Transaction Details...\n";
    String str3 = "";
    String str4 = "";
    String str5 = "";
    Object[] arrayOfObject = proxyResultMap.keySet().toArray();
    int i = 0;
    int j = 0;
    List localList = null;
    HashMap localHashMap1 = null;
    String str6 = "";
    ArrayList localArrayList = new ArrayList();
    for (int k = 0; k < arrayOfObject.length; k++)
      if (!(proxyResultMap.get(arrayOfObject[k]) instanceof String))
      {
        localList = (List)proxyResultMap.get(arrayOfObject[k]);
          System.out.println("localList##"+localList);
        int m;
        if (CommonUtil.convertObjToStr(arrayOfObject[k]).indexOf("CASH") != -1)
        {
          for (m = 0; m < localList.size(); m++)
          {
            localHashMap1 = (HashMap)localList.get(m);
            if (m <= 1)
            {
              str4 = (String)localHashMap1.get("SINGLE_TRANS_ID");
              localArrayList.add(str4);
              localArrayList.add((String)localHashMap1.get("TRANS_TYPE"));
              str5 = "CASH";
            }
            str1 = str1 + "Trans Id : " + localHashMap1.get("TRANS_ID") + "   Trans Type : " + localHashMap1.get("TRANS_TYPE");
            str6 = CommonUtil.convertObjToStr(localHashMap1.get("ACT_NUM"));
            if ((str6 != null) && (!str6.equals("")))
              str1 = str1 + "   Account No : " + localHashMap1.get("ACT_NUM") + "   Amount : " + localHashMap1.get("AMOUNT") + "\n";
            else
              str1 = str1 + "   Ac Hd Desc : " + localHashMap1.get("AC_HD_ID") + "   Amount : " + localHashMap1.get("AMOUNT") + "\n";
          }
          i++;
        }
        else if (CommonUtil.convertObjToStr(arrayOfObject[k]).indexOf("TRANSFER") != -1)
        {
          for (m = 0; m < localList.size(); m++)
          {
            localHashMap1 = (HashMap)localList.get(m);
            if (m == 0)
            {
              str4 = (String)localHashMap1.get("SINGLE_TRANS_ID");
              localArrayList.add(str4);
              str5 = "TRANSFER";
            }
            str2 = str2 + "Trans Id : " + localHashMap1.get("TRANS_ID") + "   Batch Id : " + localHashMap1.get("BATCH_ID") + "   Trans Type : " + localHashMap1.get("TRANS_TYPE");
            str6 = CommonUtil.convertObjToStr(localHashMap1.get("ACT_NUM"));
            if ((str6 != null) && (!str6.equals("")))
              str2 = str2 + "   Account No : " + localHashMap1.get("ACT_NUM") + "   Amount : " + localHashMap1.get("AMOUNT") + "\n";
            else
              str2 = str2 + "   Ac Hd Desc : " + localHashMap1.get("AC_HD_ID") + "   Amount : " + localHashMap1.get("AMOUNT") + "\n";
          }
          j++;
        }
      }
    if (i > 0)
      str3 = str3 + str1;
    if (j > 0)
      str3 = str3 + str2;
    ClientUtil.showMessageWindow("" + str3);
    int k = 0;
    String[] arrayOfString = { "Yes", "No" };
    k = COptionPane.showOptionDialog(null, "Do you want to print?", "Warning", 0, 2, null, arrayOfString, arrayOfString[0]);
    System.out.println("#$#$$ yesNo : " + k);
    if (k == 0)
    {
      Object localObject = null;
      String str7 = "";
      HashMap localHashMap2 = new HashMap();
      localHashMap2.put("TransDt", this.currDt);
      localHashMap2.put("BranchId", ProxyParameters.BRANCH_ID);
        System.out.println("localArrayList###"+localArrayList);
      for (int n = 0; n < localArrayList.size(); n++)
      {
        str7 = "";
        localHashMap2.put("TransId", localArrayList.get(n));
        TTIntegration.setParam(localHashMap2);
        System.out.println("transMode.equals" + str5);
        for (int i1 = 0; i1 < localList.size(); i1++)
        {
          localHashMap1 = (HashMap)localList.get(i1);
            System.out.println("localHashMap1##"+localHashMap1);
          //if (str5.equals("TRANSFER"))
         // {
         //   if (localArrayList.get(n).equals(localHashMap1.get("SINGLE_TRANS_ID")))
          //  {
          //    str7 = (String)localHashMap1.get("TRANS_TYPE");
          //    break;
          //  }
         // }
          if (str5.equals("CASH"))
          {
            str7 = (String)localHashMap1.get("TRANS_TYPE");
            break;
          }
        }
        System.out.println("transMap.get(TRANS_TYPE)" + str7);
        if (str5.equals("TRANSFER"))
        {
          //if (str7.equals("CREDIT"))
          //  TTIntegration.integrationForPrint("MDSReceiptPayment", false);
          //else
            TTIntegration.integrationForPrint("ReceiptPayment");
        }
        //else if (str7.equals("CREDIT"))
        if (str5.equals("CASH")){
            if (str7.equals("CREDIT"))
                TTIntegration.integrationForPrint("MDSReceipts", false);
            else
                TTIntegration.integrationForPrint("CashPayment", false);
            }
        }
    }
    }
    
    private void getTransNewDetails(HashMap map) throws Exception {
        System.out.println("map$@!$$!"+map);
        HashMap getTransMap = new HashMap();
        getTransMap.put("BATCH_ID", map.get("TRANS_ID"));
        getTransMap.put("BATCH_ID1", map.get("CASH_ID"));
        getTransMap.put("TRANS_DT", currDt);
        getTransMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
        HashMap transDetMap = new HashMap();
        List transList;
        List cashList;
//        if(!prizedMoneyPaymentTO.getDefaulter_marked().equals("Y"))
//        {
        transList = (List) ClientUtil.executeQuery("getTransferDetails", getTransMap);
        if (transList != null && transList.size() > 0) {
            transDetMap.put("TRANSFER_TRANS_LIST", transList);
        }
        cashList = (List) ClientUtil.executeQuery("getMultipleCashTransDetails", getTransMap);
        if (cashList != null && cashList.size() > 0) {
            transDetMap.put("CASH_TRANS_LIST", cashList);
            //Added by sreekrishnan for kottayam
            if (!(transList != null && transList.size() > 0)) {
                transList = (List) ClientUtil.executeQuery("getTransferDetailsForCashPayment", getTransMap);
                if (transList != null && transList.size() > 0) {
                    transDetMap.put("TRANSFER_TRANS_LIST", transList);
                }
            }
        }
        displayTransDetailMDS(transDetMap,false);
        getTransMap.clear();
        getTransMap = null;
        transList = null;
        cashList = null;
    }
        
private void displayTransDetailMDS(HashMap proxyResultMap,boolean print) {
        try {
            System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
            String cashDisplayStr = "Cash Transaction Details...\n";
            String transferDisplayStr = "Transfer Transaction Details...\n";
            String displayStr = "";
            String transId = "";
            String transType = "";
            Object keys[] = proxyResultMap.keySet().toArray();
            int cashCount = 0;
            int CreditcashCount = 0;
            int DebitcashCount = 0;
            int transferCount = 0;
            List tempList = null;
            HashMap transMap = null;
            String actNum = "";
            ArrayList crList = new ArrayList();
            ArrayList drList = new ArrayList();

            for (int i = 0; i < keys.length; i++) {
                if (proxyResultMap.get(keys[i]) instanceof String) {
                    continue;
                }
                tempList = (List) proxyResultMap.get(keys[i]);
                if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                    for (int j = 0; j < tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        if (j == 0) {
                            transId = (String) transMap.get("SINGLE_TRANS_ID");
                        }
                        cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                                + "   Trans Type : " + transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if (actNum != null && !actNum.equals("")) {
                            cashDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        } else {
                            cashDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                                    + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                        System.out.println("transMap===" + transMap);
                        if (transMap.get("TRANS_TYPE").equals("DEBIT")) {
                            DebitcashCount++;
                            System.out.println("DebitcashCount===" + DebitcashCount);
                        }
                        if (transMap.get("TRANS_TYPE").equals("CREDIT")) {
                            CreditcashCount++;
                            System.out.println("CreditcashCount===" + CreditcashCount);
                        }
                    }
//                cashDisplayStr += "Trans Id : " + transMap.get("TRANS_ID");

                    cashCount++;
                } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                    for (int j = 0; j < tempList.size(); j++) {
                        transMap = (HashMap) tempList.get(j);
                        if (j == 0) {
                            transId = (String) transMap.get("BATCH_ID");
                        }
                        transferDisplayStr += "Trans Id : " + transMap.get("TRANS_ID")
                            + "   Batch Id : " + transMap.get("BATCH_ID")
                            + "   Trans Type : " + transMap.get("TRANS_TYPE");
                        actNum = CommonUtil.convertObjToStr(transMap.get("ACT_NUM"));
                        if (actNum != null && !actNum.equals("")) {
                            transferDisplayStr += "   Account No : " + transMap.get("ACT_NUM")
                                + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        } else {                        
                            transferDisplayStr += "   Ac Hd Desc : " + transMap.get("AC_HD_ID")
                            + "   Amount : " + transMap.get("AMOUNT") + "\n";
                        }
                    }
                    //transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");
                    transferCount++;
                }
            }
            if (cashCount > 0) {
                displayStr += cashDisplayStr;
            }
            if (transferCount > 0) {
                displayStr += transferDisplayStr;
            }
            if (!displayStr.equals("")) {
                ClientUtil.showMessageWindow("" + displayStr);
            }

            if(print){
            int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE, COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            System.out.println("#$#$$ yesNo : " + yesNo);
            if (yesNo == 0) {
                TTIntegration ttIntgration = null;
                HashMap paramMap = new HashMap();
                // paramMap.put("TransId", transId);
                paramMap.put("TransDt", ClientUtil.getCurrentDateProperFormat());
                System.out.println("ClientUtil.getCurrentDateProperFormat()" + ClientUtil.getCurrentDateProperFormat());
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    System.out.println("observable.getTdtDateIndand()>>>" + this.currDt);
                    paramMap.put("TransDt", this.currDt);
                }

                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);


                for (int i = 0; i < keys.length; i++) {
                    if (proxyResultMap.get(keys[i]) instanceof String) {
                        continue;
                    }
                    tempList = (List) proxyResultMap.get(keys[i]);
                    if (CommonUtil.convertObjToStr(keys[i]).indexOf("CASH") != -1) {
                        //transMap = (HashMap) tempList.get(0);
             
                        for (int j = 0; j < tempList.size(); j++) {
                            transMap = (HashMap) tempList.get(j);
                            if (j == 0) {
                                transId = (String) transMap.get("SINGLE_TRANS_ID");
                                paramMap.put("TransId", transId);
                            }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                            break;
                            //if(transMap.get("TRANS_TYPE").equals("DEBIT"))
                            //{
                            //   drList.add(tempList.get(j));
                            //paramMap.put("ToTransId",transMap.get("TRANS_ID"));
                            //}else
                            //{
                            //crList.add(tempList.get(j));
                            //}}
                            //paramMap.put("ToTransId",transMap.get("TRANS_ID"));
                        }
                    } else if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                        for (int j = 0; j < tempList.size(); j++) {
                            transMap = (HashMap) tempList.get(j);
                            if (j == 0) {
                               // transId = (String) transMap.get("BATCH_ID");
                                 transId = (String) transMap.get("SINGLE_TRANS_ID");
                                 paramMap.put("TransId", transId);
                               
                            }
//                    transferDisplayStr += "   Batch Id : " + transMap.get("BATCH_ID");//"Trans Id : " + transMap.get("TRANS_ID")
                            break;
                        }
                    }

                }
               System.out.println("paramMap===" + paramMap);
                ttIntgration.setParam(paramMap);
                String reportName = "";
//            transType = cboSupplier.getSelectedItem() + "";
                //  System.out.println("crList.size()==="+crList.size());
                //  System.out.println("drList.size()==="+drList.size());
                //  System.out.println("transferCount==="+transferCount);
                System.out.println("transferCount===" + transferCount);
                System.out.println("CreditcashCount===" + CreditcashCount);
                System.out.println("DebitcashCount===" + DebitcashCount);

                if (transferCount > 0) {
                    //reportName = "MDSReceiptsTransfer";
                    reportName = "ReceiptPayment";
                    System.out.println("ReceiptPayment@@@@@" + reportName);
                    ttIntgration.integrationForPrint(reportName, false);
                }

                if (CreditcashCount > 0) {
                    reportName = "CashReceipt";
                    System.out.println("CashReceipt@@@@@" + reportName);
                    ttIntgration.integrationForPrint(reportName, false);
                }

                if (DebitcashCount > 0) {
                    reportName = "CashPayment";
                    System.out.println("CashPayment@@@@@" + reportName);
                    ttIntgration.integrationForPrint(reportName, false);
                }
	
        }
        }
        }catch (Exception e) {
          //ttIntgration.integrationForPrint("CashReceipt", false);
          
        }
        

    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        HashMap where = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentEditDelete");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            where.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getPrizedMoneyPaymentView");
        } else if (currAction.equalsIgnoreCase("SCHEME_DETAILS")) {
            where.put("BRANCH_CODE", getSelectedBranchID());
            //viewMap.put(CommonConstants.MAP_NAME, "getSelectEachSchemeDetails");//commented by Shany
            viewMap.put(CommonConstants.MAP_NAME, "getSelectEachGDSGroupDetails");//added by shany
        } else if (currAction.equalsIgnoreCase("CHITTAL_NO")) {
            where.put("GROUP_NO", txtGroupName.getText());
            viewMap.put(CommonConstants.MAP_WHERE, where);
            viewMap.put(CommonConstants.MAP_NAME, "getGDSSelctChittalReceiptDetailsView");
        } else if (currAction.equalsIgnoreCase("MEMBER_NO")) {
            viewMap.put(CommonConstants.MAP_NAME, "getMemberDetailsFromMDSApplication");
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        try {
         HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            if(hashMap.containsKey("GDS_NO")){
                observable.checkAcNoWithoutProdType(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
            }
            if (hashMap.containsKey("NEW_FROM_AUTHORIZE_LIST_UI")) {
                fromNewAuthorizeUI = true;
                newauthorizeListUI = (NewAuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                fromAuthorizeUI = true;
                authorizeListUI = (AuthorizeListUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hashMap.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                fromManagerAuthorizeUI = true;
                ManagerauthorizeListUI = (AuthorizeListDebitUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hashMap.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
                fromCashierAuthorizeUI = true;
                CashierauthorizeListUI = (AuthorizeListCreditUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                viewType = AUTHORIZE;
                observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
                observable.setStatus();
                transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_AUTHORIZE);
                btnReject.setEnabled(false);
                rejectFlag = 1;
            }
            if (hashMap.containsKey("FROM_SMART_CUSTOMER_UI")) {
                System.out.println("HASH DATE ======innnn" + hashMap);
                fromSmartCustUI= true;
                smartUI = (SmartCustomerUI) hashMap.get("PARENT");
                hashMap.remove("PARENT");
                btnNewActionPerformed(null);            
                txtGroupName.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
               // txtSchemeNameFocusLost(null);  
                txtGroupNameFocusLost(null);
                txtGdsNo.setText(CommonUtil.convertObjToStr(hashMap.get("CHITTAL_NO")));
                txtGdsNoFocusLost(null);
                observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
                observable.setStatus();
                  //btnSaveDisable();
            }
            isFilled = true;
            if (viewType == "SCHEME_DETAILS") {
                observable.setTxtGroupName(CommonUtil.convertObjToStr(hashMap.get("GROUP_NO")));
                txtGroupName.setText(CommonUtil.convertObjToStr(hashMap.get("GROUP_NO")));
                //Added BY Suresh 
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && txtGroupName.getText().length() > 0) {
                    txtGdsNo.setText(CommonUtil.convertObjToStr(TrueTransactMain.BRANCH_ID + txtGroupName.getText()));
                } else {
                    txtGdsNo.setText("");
                }
                //observable.setTxtGdsNo(txtGdsNo.getText());
                btnChittalNo.setEnabled(true);
                checkClosureDetail(hashMap);
                calculateOtherChargeAmount();
                List bonusTransList = ClientUtil.executeQuery("getGDSPrizedMoneyBonusPaymentisCashOrTransfer", hashMap);
                if(bonusTransList != null && bonusTransList.size() > 0){
                    HashMap bounusTranMap = (HashMap)bonusTransList.get(0);
                    if(bounusTranMap.containsKey("IS_PAYMENT_BONUS_TRANS") && bounusTranMap.get("IS_PAYMENT_BONUS_TRANS") != null && CommonUtil.convertObjToStr(bounusTranMap.get("IS_PAYMENT_BONUS_TRANS")).equalsIgnoreCase("Y")){
                        chkBonusTrans.setSelected(true);
                    }
                }
                
            } else if (viewType == "CHITTAL_NO") {
                boolean flag = false;
                int count = 0;
                String bondNo = "";
                observable.setThalayalChittal(CommonUtil.convertObjToStr(hashMap.get("THALAYAL")));
                observable.setMunnalChittal(CommonUtil.convertObjToStr(hashMap.get("MUNNAL")));
                //Added by sreekrishnan
                if(hashMap.containsKey("THALAYAL") && hashMap.get("THALAYAL")!=null && !hashMap.get("THALAYAL").equals("")
                        && hashMap.get("THALAYAL").equals("Y")){
                    thalayal = true;
                    munnal = false;
                } else if(hashMap.containsKey("MUNNAL") && hashMap.get("MUNNAL")!=null && !hashMap.get("MUNNAL").equals("")
                        && hashMap.get("MUNNAL").equals("Y")){
                    munnal = true;
                    thalayal = false;
                }else{
                    thalayal = false;
                    munnal =false;
                }
                observable.setTxtGdsNo(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                txtSubNo.setText(CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                observable.setTxtSubNo(CommonUtil.convertObjToStr(hashMap.get("SUB_NO")));
                observable.setAuctionTrans(CommonUtil.convertObjToStr(hashMap.get("AUCTION_TRANS")));
                System.out.println("hashss" + hashMap);
                
                List DefaulterList = ClientUtil.executeQuery("getGDSDefaulterDts", hashMap);
                if (DefaulterList != null && DefaulterList.size() > 0) {
                    HashMap aMpa = (HashMap) DefaulterList.get(0);
                    if (aMpa.get("DEFAULTER") != null && !aMpa.get("DEFAULTER").toString().equals("") && aMpa.get("DEFAULTER").toString().equals("Y")) {
                        observable.setDefaulter("Y");
                        System.out.println("hhhh");
                        panDefaulterCharges.setVisible(true);
                        txtDefaulterComm.setText("");
                        txtDefaulterInterest.setText("");
                        HashMap bonusMap = new HashMap();
//                        bonusMap.put("SCHEME_NAME", txtSchemeName.getText());
//                        bonusMap.put("CHITTAL_NO", txtChittalNo.getText());
                        List bonusRecoverList = ClientUtil.executeQuery("getBonusRecoverd", hashMap);
                        if (bonusRecoverList != null && bonusRecoverList.size() > 0) {
                            bonusMap = (HashMap) bonusRecoverList.get(0);
                            System.out.println("asdjadhhh" + bonusMap);
                            if (bonusMap.get("BONUS_SUM") != null && !bonusMap.get("BONUS_SUM").toString().equals("")) {
                                txtDefaulterBonus.setText(bonusMap.get("BONUS_SUM").toString());
                                defaulter_bonus_recover = txtBonusRecovered.getText();
                                System.out.println("txt" + txtBonusRecovered.getText());
                            } else {
                                System.out.println("dddd1");
                                txtDefaulterBonus.setText("0.0");
                            }

                        } else {
                            txtDefaulterBonus.setText("0.0");
                            System.out.println("dddd2");
                        }

                        defaulterMarked = true;
                        transactionUI.setButtonEnableDisable(false);
                        transactionUI.cancelAction(false);
                        transactionUI.resetObjects();
                        transactionUI.setCallingApplicantName("");
                        transactionUI.setCallingAmount("");
                        transactionUI.okAction(false);
                        transactionUI.setButtonEnableDisable(false);
                        System.out.println("herrrrrr");
                    } else {
                        panDefaulterCharges.setVisible(false);
                        txtDefaulterComm.setText("");
                        txtDefaulterInterest.setText("");
                        defaulterMarked = false;
                        observable.setDefaulter("N");
                        System.out.println("dddd3");
                    }
                }
                hashMap.put("GROUP_NO",CommonUtil.convertObjToStr(txtGroupName.getText()));
                hashMap.put("GDS_NO",hashMap.get("GDS_NO"));
                List chittalLst = ClientUtil.executeQuery("getGDSSelctReceiptDetails", hashMap);
                    double inst_amt=0; 
                    double installment_paid=0;  
                    double next_bonus_amount=0;          
                    double total_bonus_amount=0;  
                    double prized_amount=0;  
                    double commision_amount=0; 
                if (chittalLst != null && chittalLst.size() > 0) {
                    
                    for(int i=0;i<chittalLst.size();i++){
                       hashMap = (HashMap) chittalLst.get(i);
                       System.out.println("$#@#$$$#@count" + hashMap); 
                       inst_amt=inst_amt+CommonUtil.convertObjToDouble(hashMap.get("INST_AMT"));
                      // installment_paid=installment_paid+CommonUtil.convertObjToDouble(hashMap.get("installment_paid"));
                       next_bonus_amount=next_bonus_amount+CommonUtil.convertObjToDouble(hashMap.get("NEXT_BONUS_AMOUNT"));
                       total_bonus_amount=total_bonus_amount+CommonUtil.convertObjToDouble(hashMap.get("TOTAL_BONUS_AMOUNT"));
                       prized_amount=prized_amount+CommonUtil.convertObjToDouble(hashMap.get("PRIZED_AMOUNT"));
                       commision_amount=commision_amount+CommonUtil.convertObjToDouble(hashMap.get("COMMISION_AMOUNT"));
                       
                    }
                    hashMap.put("INST_AMT",inst_amt);
                    //hashMap.put("installment_paid",installment_paid);
                    hashMap.put("NEXT_BONUS_AMOUNT",next_bonus_amount);
                    hashMap.put("TOTAL_BONUS_AMOUNT",total_bonus_amount);
                    hashMap.put("PRIZED_AMOUNT",prized_amount);
                    hashMap.put("COMMISION_AMOUNT",commision_amount);
                    
                    System.out.println("$#@#$$$#@count!!!!!!" + hashMap); 
                   
                }
                hashMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));
                //                List securityLst = ClientUtil.executeQuery("getSelectCountSecurityDetails", hashMap);
                //                if(securityLst!= null && securityLst.size()>0){
                //                    HashMap securityMap = (HashMap)securityLst.get(0);
                //                    count = CommonUtil.convertObjToInt(securityMap.get("COUNT"));
                //                    System.out.println("$#@#$$$#@count"+count);
                //                }
                //                if(count>0){
                //                    List bondLst = ClientUtil.executeQuery("getSelectBondDetails", hashMap);
                //                    System.out.println("$#@#$$$#@bondLst"+bondLst);
                //                    if(bondLst!= null && bondLst.size()>0){
                //                        System.out.println("$#@#$$$#@bondLst"+bondLst);
                //                        HashMap bondMap = (HashMap)bondLst.get(0);
                //                        bondNo = CommonUtil.convertObjToStr(bondMap.get("BOND_NO"));
                //                        //                        java.util.Date bondDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(bondMap.get("BOND_DT")));
                //                    }
                //                    if(bondNo.length()>0 ){
                List lst = ClientUtil.executeQuery("getSelectPaidStatus", hashMap);
                if (lst != null && lst.size() > 0) {
                    HashMap paidStatusMap = (HashMap) lst.get(0);
                    if (paidStatusMap.get("PAID_STATUS") != null && CommonUtil.convertObjToStr(paidStatusMap.get("PAID_STATUS")).equals("N")
                            && CommonUtil.convertObjToStr(paidStatusMap.get("AUTHORIZED_STATUS")).equals("")) {
                        ClientUtil.showAlertWindow("Transaction already done pending for authorization");
                        return;
                    } else if (paidStatusMap.get("PAID_STATUS") != null && CommonUtil.convertObjToStr(paidStatusMap.get("PAID_STATUS")).equals("N")
                            && CommonUtil.convertObjToStr(paidStatusMap.get("AUTHORIZED_STATUS")).equals("AUTHORIZED")) {
                        ClientUtil.showAlertWindow("Payment already completed");
                        return;
                    }
                }
                if (flag == false) {
                    txtGroupName.setText(CommonUtil.convertObjToStr(observable.getTxtGroupName()));
                    txtGdsNo.setText(CommonUtil.convertObjToStr(observable.getTxtGdsNo()));
                    txtDivisionNo.setText(CommonUtil.convertObjToStr(hashMap.get("DIVISION_NO")));
                    //observable.setTxtGdsNo(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                    btnChittalNo.setEnabled(true);
                    txtDivisionNo.setText(CommonUtil.convertObjToStr(hashMap.get("DIVISION_NO")));
                    tdtDrawDate.setDateValue(CommonUtil.convertObjToStr(hashMap.get("DRAW_AUCTION_DATE")));
                    txtTotalInst.setText(observable.getTxtTotalInst());
                    txtNoOfInstPaid.setText(observable.getTxtNoOfInstPaid());
//                            txtOverdueInst.setText(observable.getTxtOverdueInst());
                    btnMemberNo.setEnabled(true);
                    observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                    txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
                    lblMemberNameVal.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                    lblValStreet.setText(observable.getLblHouseStNo());
                    lblValArea.setText(observable.getLblArea());
                    lblValCity.setText(observable.getLblCity());
                    lblValState.setText(observable.getLblState());
                    lblValPin.setText(observable.getLblpin());
                    observable.setReceiptDetails(hashMap);                      //SET RECEIPT DETAILS
                    txtTotalInst.setText(observable.getTxtTotalInst());
                    txtNoOfInstPaid.setText(observable.getTxtNoOfInstPaid());
                    txtOverdueInst.setText(observable.getTxtOverdueInst());
                    txtOverDueAmt.setText(observable.getTxtOverDueAmt());
                    txtPrizedInstNo.setText(CommonUtil.convertObjToStr(hashMap.get("SL_NO")));
                    txtBonusAmt.setText(CommonUtil.convertObjToStr(hashMap.get("TOTAL_BONUS_AMOUNT")));
                    txtPrizedAmt.setText(CommonUtil.convertObjToStr(hashMap.get("PRIZED_AMOUNT")));
                    txtCommisionAmt.setText(CommonUtil.convertObjToStr(hashMap.get("COMMISION_AMOUNT")));
                    observable.setTxtDivisionNo(CommonUtil.convertObjToStr(hashMap.get("DIVISION_NO")));
                    observable.setTdtDrawDate(tdtDrawDate.getDateValue());
                    observable.setTxtPrizedInstNo(CommonUtil.convertObjToStr(hashMap.get("SL_NO")));
                    observable.setTxtPrizedInstNo(CommonUtil.convertObjToStr(hashMap.get("SL_NO")));
                    observable.setTxtPrizedAmt(CommonUtil.convertObjToStr(hashMap.get("PRIZED_AMOUNT")));
                    observable.setTxtCommisionAmt(CommonUtil.convertObjToStr(hashMap.get("COMMISION_AMOUNT")));
                    observable.setTxtBonusAmt(CommonUtil.convertObjToStr(hashMap.get("TOTAL_BONUS_AMOUNT")));
                    setCaseExpensesAmount();
                    txtNetAmt.setText(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                            - (CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue())));
                    observable.setTxtNetAmt(txtNetAmt.getText());
                    chkThalayal.setSelected(observable.getChkThalayal());           // OTHER DETAILS
                    chkMunnal.setSelected(observable.getChkMunnal());
                    chkChangedBetween.setSelected(observable.getChkChangedBetween());
                    ClientUtil.enableDisable(panOtherMDSDetails, false);
                    observable.setChangedMemberTableData();
                    tblChangedMemberDetails.setModel(observable.getTblChangedDetails());
                    displyEarlierMemberDetails(hashMap);
                    //transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
                    transactionUI.setButtonEnableDisable(true);
                    transactionUI.cancelAction(false);
                    transactionUI.resetObjects();
                    transactionUI.setCallingDepositeAmount(txtNetAmt.getText());
                    transactionUI.setCallingApplicantName("");
                    //                            transactionUI.setCallingAmount(txtNetAmt.getText());
                    if (defaulterMarked) {
                        System.out.println("asdasd");
                        transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
                    } else {
                        transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));

                    }
                } else {
                    btnCancelActionPerformed(null);
                }
                //                    }else{
                //                        ClientUtil.showAlertWindow("Bond Details Should be Entered in MDS Master Maintenance !!!");
                //                        btnCancelActionPerformed(null);
                //                    }
                //                }else{
                //                    ClientUtil.showAlertWindow("Enter Security  Details in MDS Master Maintenance !!!");
                //                    btnCancelActionPerformed(null);
                //                }
                if (!observable.getDefaulter().equals("Y")) {
                    System.out.println("asdasdccccc");
                    if (CommonUtil.convertObjToInt(txtOverdueInst.getText()) > 0) {
                        ClientUtil.showAlertWindow("This Particular Chittal Number has Overdue !!!");
                        panDefaulters.setVisible(true);
                        lblDefaulters.setVisible(true);
                        rdoDefaulters_No.setSelected(false);
                        rdoDefaulters_Yes.setSelected(true);
                        rdoDefaulters_YesActionPerformed(null);
                        //Commented By Suresh
//                    btnCancelActionPerformed(null);
                    } else {
                        panDefaulters.setVisible(false);
                        lblDefaulters.setVisible(false);
                        rdoDefaulters_No.setSelected(true);
                    }
                } else {
                    panDefaulters.setVisible(false);
                    lblDefaulters.setVisible(false);
                    rdoDefaulters_No.setSelected(true);
                }
                txtGroupName.setEnabled(false);
                txtGdsNo.setEnabled(false);
                btnChittalNo.setEnabled(false);
                btnSchemeName.setEnabled(false);
                txtSubNo.setEnabled(false);
                calculateOtherChargeAmount();                
            } else if (viewType == "MEMBER_NO") {
                observable.setTxtGroupName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                txtGroupName.setText(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                txtGdsNo.setText(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                txtDivisionNo.setText(CommonUtil.convertObjToStr(hashMap.get("DIVISION_NO")));
                observable.setTxtGdsNo(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                lblMemberNameVal.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NAME")));
                observable.setLblMemberNameVal(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NAME")));
                btnChittalNo.setEnabled(true);
                txtDivisionNo.setText(observable.getTxtDivisionNo());
                tdtDrawDate.setDateValue(observable.getTdtDrawDate());
                txtTotalInst.setText(observable.getTxtTotalInst());
                txtNoOfInstPaid.setText(observable.getTxtNoOfInstPaid());
                txtOverdueInst.setText(observable.getTxtOverdueInst());
                btnMemberNo.setEnabled(true);
                observable.getCustomerAddressDetails(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                txtMemberNo.setText(CommonUtil.convertObjToStr(hashMap.get("MEMBER_NO")));
                lblMemberNameVal.setText(CommonUtil.convertObjToStr(hashMap.get("CUSTOMER")));
                lblValStreet.setText(observable.getLblHouseStNo());
                lblValArea.setText(observable.getLblArea());
                lblValCity.setText(observable.getLblCity());
                lblValState.setText(observable.getLblState());
                lblValPin.setText(observable.getLblpin());
                observable.setReceiptDetails(hashMap);
                calculateOtherChargeAmount();
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                observable.setTxtGroupName(CommonUtil.convertObjToStr(hashMap.get("SCHEME_NAME")));
                observable.setTxtGdsNo(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                this.setButtonEnableDisable();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                    hashMap.put("GET_TRANS_DATE", "GET_TRANS_DATE");
                }
                observable.getData(hashMap);
                update();
                hashMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));
                List lst = ClientUtil.executeQuery("getMemberAddressDetails", hashMap);
                if (lst != null && lst.size() > 0) {
                    addressMap = (HashMap) lst.get(0);
                    populateAddressData(addressMap);
                }
                observable.setTransId(CommonUtil.convertObjToStr(hashMap.get("TRANS_ID")));
                txtDivisionNo.setEnabled(false);
                tdtDrawDate.setEnabled(false);
                txtTotalInst.setEnabled(false);
                txtNoOfInstPaid.setEnabled(false);
                txtOverdueInst.setEnabled(false);
                if (observable.getActionType() != ClientConstants.ACTIONTYPE_NEW) {
                    observable.setOtherDetails();                           // OTHER DETAILS
                    chkThalayal.setSelected(observable.getChkThalayal());
                    chkMunnal.setSelected(observable.getChkMunnal());
                    chkChangedBetween.setSelected(observable.getChkChangedBetween());
                    ClientUtil.enableDisable(panOtherMDSDetails, false);
                    observable.setChangedMemberTableData();
                    tblChangedMemberDetails.setModel(observable.getTblChangedDetails());
                    displyEarlierMemberDetails(hashMap);
//                    if(defaulterMarked)
//                    {
//                        System.out.println("dxx");
//                        txtDefaulterBonus.setText(defaulter_bonus_recover);
//                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    checkClosureDetail(hashMap);
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                observable.setTxtGroupName(CommonUtil.convertObjToStr(hashMap.get("GROUP_NO")));
                observable.setTxtGdsNo(CommonUtil.convertObjToStr(hashMap.get("GDS_NO")));
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
                 //   setSchemeDescription(hashMap);
                //	lblSchemeDesc.setVisible(true);        
                }else{
	                lblSchemeDesc.setVisible(false);    
	                schemeDesc = "";
                }
                observable.getData(hashMap);
                update();
                observable.setOtherDetails();                           // OTHER DETAILS
                chkThalayal.setSelected(observable.getChkThalayal());
                chkMunnal.setSelected(observable.getChkMunnal());
                chkChangedBetween.setSelected(observable.getChkChangedBetween());
                ClientUtil.enableDisable(panOtherMDSDetails, false);
                observable.setChangedMemberTableData();
                tblChangedMemberDetails.setModel(observable.getTblChangedDetails());
                hashMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));
                List lst = ClientUtil.executeQuery("getGDSMemberAddressDetails", hashMap);
                if (lst != null && lst.size() > 0) {
                    addressMap = (HashMap) lst.get(0);
                    populateAddressData(addressMap);
                }
                observable.setTransId(CommonUtil.convertObjToStr(hashMap.get("TRANS_ID")));
                if (!CommonUtil.convertObjToStr(hashMap.get("CASH_ID")).equals("")) {
                    observable.setCashId(CommonUtil.convertObjToStr(hashMap.get("CASH_ID")));
                }
                displyEarlierMemberDetails(hashMap);
                displayLoanLienSecurityDetails();
                displayMDSLienSecurityDetails();
                getTransNewDetails(hashMap);
                //calculateOtherChargeAmount();
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                
                HashMap MDSMap = new HashMap();
                MDSMap.put("CHITTAL_NO", hashMap.get("CHITTAL_NO"));
                MDSMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));
                TableDialogUI tableDialogUI = new TableDialogUI("getSelectMDSSecurityDetails", MDSMap);
                if(tableDialogUI!=null){
                    tableDialogUI.setTitle("MDS Security Details");
                    tableDialogUI.show();
                }
            }
            if (viewType != "SCHEME_DETAILS") {
                btnViewLedger.setEnabled(true);
            } else {
                btnViewLedger.setEnabled(false);
            }
            if (hashMap.containsKey("FROM_AUTHORIZE_LIST_UI")) {
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
            }
            if (hashMap.containsKey("FROM_CASHIER_AUTHORIZE_LIST_UI")) {
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
            }
            if (hashMap.containsKey("FROM_MANAGER_AUTHORIZE_LIST_UI")) {
                btnSave.setEnabled(false);
                btnCancel.setEnabled(true);
                btnAuthorize.setEnabled(true);
                btnReject.setEnabled(true);
            }
            if (txtGdsNo.getText().length() > 0 && viewType != AUTHORIZE) {
                ClientUtil.enableDisable(panDefaulters, true);
            } else {
                ClientUtil.enableDisable(panDefaulters, false);
            }
           // hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        if (rejectFlag == 1) {
            btnReject.setEnabled(false);
        }
		if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
        	setServiceTaxForCommission();
		}
                
    }
    private void calculateOtherChargeAmount(){
        double prizedAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText());
        String groupName = CommonUtil.convertObjToStr(txtGroupName.getText());
        HashMap schemeMap = new HashMap();
        HashMap productMap = new HashMap();
        String paymentType = "";
        double paymentAmt = 0;
        double otherChargeAmt = 0.0;
        double installmentAmount = 0.0;
        double bonusAmount = 0.0;
        schemeMap.put("GROUP_NO", groupName);
        List lst = ClientUtil.executeQuery("getSelectAllSchemeAcctHead", schemeMap);
        if (lst != null && lst.size() > 0) {
            productMap = (HashMap) lst.get(0);
        }
        if(productMap != null && (productMap.containsKey("PRIZED_MONEY_PAYMENT_TYPE") && productMap.get("PRIZED_MONEY_PAYMENT_TYPE") != null) 
          && (productMap.containsKey("PRIZED_MONEY_PAYMENT_AMT") && productMap.get("PRIZED_MONEY_PAYMENT_AMT") != null)     
        ){
            paymentType = CommonUtil.convertObjToStr(productMap.get("PRIZED_MONEY_PAYMENT_TYPE"));
            paymentAmt = CommonUtil.convertObjToDouble(productMap.get("PRIZED_MONEY_PAYMENT_AMT"));
        }
        if(paymentType != null && paymentType.equalsIgnoreCase("PERCENT")){
            otherChargeAmt = prizedAmount * (paymentAmt/100);
        }else if(paymentType != null && paymentType.equalsIgnoreCase("ABSOLUTE")){
            otherChargeAmt = CommonUtil.convertObjToDouble(productMap.get("PRIZED_MONEY_PAYMENT_AMT"));
        }else if(paymentType != null && (paymentType.equalsIgnoreCase("PENDING_INST") || paymentType.equalsIgnoreCase("PENDING_INST_WOUT_BONUS"))){
            if(paymentType.equalsIgnoreCase("PENDING_INST_WOUT_BONUS")){
                otherChargeAmt = ((CommonUtil.convertObjToDouble(observable.getInstAmount())* observable.getPendingInstallment())-observable.getPendingBonus()) * (paymentAmt/100);
                System.out.println("otherChargeAmt^$^^$^$PENDING_INST_WOUT_BONUS"+otherChargeAmt);
            }else{
                otherChargeAmt = (CommonUtil.convertObjToDouble(observable.getInstAmount())* observable.getPendingInstallment()) * (paymentAmt/100);
                System.out.println("otherChargeAmt^$^^$^$PENDING_INST"+otherChargeAmt);
            }
        }else if(paymentType != null && paymentType.equalsIgnoreCase("TOTAL_SCHEME_AMOUNT")){
            otherChargeAmt = ((CommonUtil.convertObjToDouble(observable.getTxtPrizedAmt())+ CommonUtil.convertObjToDouble(observable.getTxtCommisionAmt())+
                                CommonUtil.convertObjToDouble(observable.getTxtBonusAmt()))*(paymentAmt/100));
            System.out.println("otherChargeAmt^$^^$^SCHEME_AMOUNT"+otherChargeAmt);
        }else{                  
            otherChargeAmt =0;
        }
        otherChargeAmt = Math.round(otherChargeAmt);
        txtChargeAmount.setText(CommonUtil.convertObjToStr(otherChargeAmt));
        txtChargeAmountFocusLost(null);
        observable.setTxtChargeAmount(CommonUtil.convertObjToStr(otherChargeAmt));
        txtChargeAmount.setEditable(true);//Changed by sreekrishnan        
    }
    public String checkServiceTaxApplicable_old(String accheadId) {
        String checkFlag = "N";
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                }
            }
        }
        return checkFlag;
    }
    private void setServiceTaxForCommission_old() {
        HashMap whereMap = new HashMap();
        whereMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(txtGdsNo.getText()));
        String checkFlag = "N";
        String bankChareHead = "";
        double taxAmount = 0;
        List resultList = ClientUtil.executeQuery("getAccounthead", whereMap);
        if (resultList != null && resultList.size() > 0) {
            HashMap newMap = (HashMap) resultList.get(0);
            if (newMap != null && newMap.containsKey("COMMISION_HEAD")) {
                bankChareHead = CommonUtil.convertObjToStr(newMap.get("COMMISION_HEAD"));
                //checkFlag = checkServiceTaxApplicable(bankChareHead);
                if (checkFlag != null && checkFlag.equals("Y")) {
                    taxAmount = CommonUtil.convertObjToDouble(txtCommisionAmt.getText());
                }
            }

        }
        //added by chithra for service tax
        if (taxAmount > 0) {
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmount);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);
                System.out.println("serviceTax_Map :" + serviceTax_Map);
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));
                    lblServiceTaxval.setText(objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, objServiceTax.roundOffAmt(amt, "NEAREST_VALUE"));
                    double totamt = CommonUtil.convertObjToDouble(txtNetAmt.getText()) - CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    txtNetAmt.setText(CommonUtil.convertObjToStr(totamt));
                    totamt = CommonUtil.convertObjToDouble(transactionUI.getCallingAmount()) - CommonUtil.convertObjToDouble(lblServiceTaxval.getText());
                    transactionUI.setCallingAmount(CommonUtil.convertObjToStr(totamt));
                } else {
                    lblServiceTaxval.setText("0.00");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            lblServiceTaxval.setText("0.00");
        }
    }
private void setSchemeDescription(HashMap hashMap){
    HashMap wheremap = new HashMap();
    List lst = ClientUtil.executeQuery("getSelectSchemeName", hashMap);
    if (lst != null && lst.size() > 0) {
        wheremap = (HashMap) lst.get(0);
        if(wheremap.get("SCHEME_DESC") != null && wheremap.containsKey("SCHEME_DESC")){
        	schemeDesc = " [ "+CommonUtil.convertObjToStr(wheremap.get("SCHEME_DESC"))+" ] ";
        }
    }
}
//    private void displayLoanLienSecurityDetails() {
//        if (txtChittalNo.getText().length() > 0 && txtSubNo.getText().length() > 0) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("DEPOSIT_NO", txtChittalNo.getText() + "_" + txtSubNo.getText());
//            List recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinLoansDeposit", whereMap);
//            if (recordList != null && recordList.size() > 0) {
//                whereMap = (HashMap) recordList.get(0);
//                List loanList = ClientUtil.executeQuery("getLoanFacilityDetailsFromBorrowNo", whereMap);
//                if (loanList != null && loanList.size() > 0) {
//                    whereMap = (HashMap) loanList.get(0);
//                    ClientUtil.showMessageWindow("This Chittal Lien Marked on this Loan " + whereMap.get("ACCT_NUM"));
//                }
//            }
//        }
//    }
//
//    private void displayMDSLienSecurityDetails() {
//        if (txtChittalNo.getText().length() > 0 && txtSubNo.getText().length() > 0) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("DEPOSIT_NO", txtChittalNo.getText() + "_" + txtSubNo.getText());
//            List recordList = ClientUtil.executeQuery("checkDepositNoAlreadyinMaster", whereMap);
//            if (recordList != null && recordList.size() > 0) {
//                whereMap = (HashMap) recordList.get(0);
//                ClientUtil.showMessageWindow("This Chittal Lien Marked on this MDS " + whereMap.get("CHITTAL_NO") + "_" + whereMap.get("SUB_NO"));
//            }
//        }
//    }

    private void checkClosureDetail(HashMap hashMap) {
        List closureList = ClientUtil.executeQuery("checkGDSSchemeClosureDetails", hashMap);
        if (closureList != null && closureList.size() > 0) {
            observable.setClosureDetails(true);
            ClientUtil.showMessageWindow("This Scheme is Closed !!!");
        } else {
            observable.setClosureDetails(false);
        }
    }

    private boolean checkSecurityDetails() {
        int count = 0;
        String value="";
        String bondNo = "";
        boolean security = true;
        HashMap hashMap = new HashMap();
        hashMap.put("GDS_NO", CommonUtil.convertObjToStr(txtGdsNo.getText()));
        hashMap.put("SUB_NO", CommonUtil.convertObjToInt(txtSubNo.getText()));
        hashMap.put("GROUP_NO",CommonUtil.convertObjToStr(txtGroupName.getText()));
        HashMap applicationMap = new HashMap();
        applicationMap.put("GDS_NO",CommonUtil.convertObjToStr(txtGdsNo.getText()));
        List allApplnDetails = ClientUtil.executeQuery("getAllApplcationDetailsForGDS", applicationMap);
        GDSApplicationTO objGDSApplicationTO = (GDSApplicationTO) allApplnDetails.get(0);
        hashMap.put("CHITTAL_NO", objGDSApplicationTO.getChittalNo());
        List securityLst = ClientUtil.executeQuery("getSelectCountSecurityDetails", hashMap);
        if (securityLst != null && securityLst.size() > 0) {
            HashMap securityMap = (HashMap) securityLst.get(0);
            count = CommonUtil.convertObjToInt(securityMap.get("COUNT"));
            System.out.println("$#@#$$$#@count" + count);
        }
         List mandatoryList = ClientUtil.executeQuery("getGDSMandatorySecurityData", hashMap);
        if (mandatoryList != null && mandatoryList.size() > 0) {
            HashMap securityMap = (HashMap) mandatoryList.get(0);
            value = CommonUtil.convertObjToStr(securityMap.get("SCHEME_NAME"));
           }
        if (count > 0  && value!=null) {
            List bondLst = ClientUtil.executeQuery("getGDSSelectBondDetails", hashMap);
            System.out.println("$#@#$$$#@bondLst" + bondLst);
            if (bondLst != null && bondLst.size() > 0) {
                System.out.println("$#@#$$$#@bondLst" + bondLst);
                HashMap bondMap = (HashMap) bondLst.get(0);
                bondNo = CommonUtil.convertObjToStr(bondMap.get("BOND_NO"));
            }
            if (bondNo.length() > 0) {
            } else if(value== null) {
                security = false;
                ClientUtil.showAlertWindow("Bond Details Should be Entered in MDS Master Maintenance !!!");
                btnCancelActionPerformed(null);
            }
           } else {
                if(!thalayal && !munnal){
                    security = false;
                    ClientUtil.showAlertWindow("Enter Security  Details in MDS Master Maintenance !!!");
                    btnCancelActionPerformed(null);
                }
        }
        return security;
    }

    private void displyEarlierMemberDetails(HashMap hashMap) {
        hashMap.put("SUB_NO", CommonUtil.convertObjToInt(hashMap.get("SUB_NO")));
        List oldMemberList = ClientUtil.executeQuery("getGDSChangeOfMemDetails", hashMap);
        if (oldMemberList != null && oldMemberList.size() > 0) {
            panEarlierChitDetails.setVisible(true);
            HashMap oldMemberMap = new HashMap();
            oldMemberMap = (HashMap) oldMemberList.get(0);
            txtEarlierChitMemberNumber.setText(CommonUtil.convertObjToStr(oldMemberMap.get("OLD_MEMBER_NO")));
            lblEarlierChitMemberName.setText(CommonUtil.convertObjToStr(oldMemberMap.get("OLD_MEMBER_NAME")));
//            List paidList = ClientUtil.executeQuery("getTotalInstAmount", hashMap);
//            if(paidList!=null && paidList.size()>0){
//                oldMemberMap = new HashMap();
//                oldMemberMap = (HashMap)paidList.get(0);
//                txtTotalAmtPaid.setText(CommonUtil.convertObjToStr(oldMemberMap.get("PAID_AMT")));
            txtTotalAmtPaid.setText(CommonUtil.convertObjToStr(oldMemberMap.get("TOTAL_AMOUNT")));
            txtTotalAmtPaid.setEnabled(false);
            txtEarlierChitMemberNumber.setEnabled(false);
            List schemeLst = (List) ClientUtil.executeQuery("getSchemeDetailsList", hashMap);
            if (schemeLst != null && schemeLst.size() > 0) {
                HashMap productMap = new HashMap();
                productMap = (HashMap) schemeLst.get(0);
                if (productMap.get("BONUS_EXISTING_CHITTAL") != null && !productMap.get("BONUS_EXISTING_CHITTAL").equals("") && (productMap.get("BONUS_EXISTING_CHITTAL").equals("Y"))) {
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        txtAmountRefunded.setText("0");
                        txtAmountRefunded.setEnabled(true);
                        double netAmt = CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
                        txtNetAmt.setText(String.valueOf(netAmt));
                    }
                } else {
                    if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
                        txtAmountRefunded.setText(txtTotalAmtPaid.getText());
                        txtAmountRefunded.setEnabled(false);
                        double netAmt = CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtAmountRefunded.getText()).doubleValue();
                        txtNetAmt.setText(String.valueOf(netAmt));
                    }
                    txtAmountRefunded.setEnabled(false);
                }

            }
//            }
        } else {
            panEarlierChitDetails.setVisible(false);
            txtTotalAmtPaid.setText("");
            txtAmountRefunded.setText("");
        }
    }

    public void cancelAction() {
        System.out.println("dddd");
        btnCancelActionPerformed(null);
        return;
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        //        super.removeEditLock(observable.getTxtSchemeName());
        System.out.println("mmmmhygfgfffs");
        viewType = "CANCEL";
        lblStatus.setText("               ");
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
        //        observable.resetForm();
        observable.resetTableValues();
        rdoDefaulters_Yes.setSelected(false);
        lblSchemeDesc.setVisible(false);
        rdoDefaulters_No.setSelected(false);
        setModified(false);
        btnNew.setEnabled(true);
        btnReject.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnView.setEnabled(true);
        isFilled = false;
        enableDisableButton(false);
        transactionUI.setCallingUiMode(ClientConstants.ACTIONTYPE_CANCEL);
        transactionUI.setButtonEnableDisable(true);
        transactionUI.cancelAction(false);
        transactionUI.resetObjects();
        transactionUI.setCallingApplicantName("");
        transactionUI.setCallingAmount("");
        lblMemberNameVal.setText("");
        lblValStreet.setText("");
        lblValArea.setText("");
        lblValCity.setText("");
        lblValState.setText("");
        lblValPin.setText("");
        lblEarlierChitMemberName.setText("");
        btnSchemeName.setEnabled(false);
        btnChittalNo.setEnabled(false);
        btnMemberNo.setEnabled(false);
        btnViewLedger.setEnabled(false);
        panEarlierChitDetails.setVisible(false);
         if (fromNewAuthorizeUI) {
            this.dispose();
            fromNewAuthorizeUI = false;
            newauthorizeListUI.setFocusToTable();
        }
        if (fromAuthorizeUI) {
            this.dispose();
            fromAuthorizeUI = false;
            authorizeListUI.setFocusToTable();
        }
        if (fromCashierAuthorizeUI) {
            this.dispose();
            fromCashierAuthorizeUI = false;
            CashierauthorizeListUI.setFocusToTable();
        }
        if (fromManagerAuthorizeUI) {
            this.dispose();
            fromManagerAuthorizeUI = false;
            ManagerauthorizeListUI.setFocusToTable();
        }
        observable.setSelectedBranchID(ProxyParameters.BRANCH_ID);
        setSelectedBranchID(ProxyParameters.BRANCH_ID);

        rdgDefaulters.remove(rdoDefaulters_Yes);
        rdgDefaulters.remove(rdoDefaulters_No);
        rdgDefaulters = new CButtonGroup();
        rdgDefaulters.add(rdoDefaulters_Yes);
        rdgDefaulters.add(rdoDefaulters_No);
        System.out.println("nnnn");
        lblMemberNameVal.setText("");
        lblSchemeDesc.setVisible(false);
        lblServiceTaxval.setText("");
        serviceTax_Map = null;
        chkPartPay.setSelected(false);
        chkPartPay.setVisible(false);
        chkPartPay.setEnabled(false);
        thalayal = false;
        munnal = false;
    }//GEN-LAST:event_btnCancelActionPerformed
    private void enableDisableButton(boolean flag) {
        //        btnSchemeName.setEnabled(flag);
        //        btnChittalNo.setEnabled(flag);
        //        btnMemberNo.setEnabled(flag);
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
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

    private void populateAddressData(HashMap addressMap) {
        lblValStreet.setText(CommonUtil.convertObjToStr(addressMap.get("HOUSE_ST")));
        lblValArea.setText(CommonUtil.convertObjToStr(addressMap.get("AREA")));
        lblValCity.setText(CommonUtil.convertObjToStr(addressMap.get("CITY")));
        lblValState.setText(CommonUtil.convertObjToStr(addressMap.get("STATE")));
        lblValPin.setText(CommonUtil.convertObjToStr(addressMap.get("PIN")));
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

    private void txtChargeAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChargeAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChargeAmountActionPerformed

    private void txtChargeAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargeAmountFocusLost
        // TODO add your handling code here:
        if (txtChargeAmount.getText().length() > 0) {
            setCaseExpensesAmount();
            double netAmount = 0.0;
            if (txtTotalAmtPaid.getText().length() > 0) {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
            } else {
                netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
            }
            if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
        	setServiceTaxForCommission();
	    }
            
            if (CommonUtil.convertObjToDouble(txtNetAmt.getText()).doubleValue() >= CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()) {
                if (rdoDefaulters_Yes.isSelected() == true || rdoDefaulters_No.isSelected() == true) {
                    if (rdoDefaulters_Yes.isSelected() == true) {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue())));
                    } else {
                        txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue())-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));                        
                    }
                } else {
                    ClientUtil.showAlertWindow("Please Select Defaulters Yes or No !!! ");
                    txtNoticeAmt.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Total Credit Amount Should not be more than the prized money amount");
//              txtChargeAmount.setText("");
                txtNetAmt.setText(String.valueOf(netAmount));
            }
            
        System.out.println("$@#$@#$#@$@#$@"+thalayal);
        if(!thalayal && !munnal){
            transactionUI.cancelAction(false);
            transactionUI.setButtonEnableDisable(true);
            transactionUI.resetObjects();
//            transactionUI.setCallingAmount(txtNetAmt.getText());
//            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()+CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue()));
            transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
        }else{
            setThalayalTransactionUI();
        }
        }
    }//GEN-LAST:event_txtChargeAmountFocusLost

private void chkBonusTransActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBonusTransActionPerformed
// TODO add your handling code here:    
}//GEN-LAST:event_chkBonusTransActionPerformed
    private void displayAlert(String message) {
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }

    
    
     public HashMap checkServiceTaxApplicable(String accheadId) {
        HashMap checkForTaxMap = new HashMap();
        String checkFlag = "N";
        if (TrueTransactMain.SERVICE_TAX_REQ.equals("Y")) {
            HashMap whereMap = new HashMap();
            whereMap.put("AC_HD_ID", accheadId);
            List accHeadList = ClientUtil.executeQuery("getCheckServiceTaxApplicableForShare", whereMap);
            if (accHeadList != null && accHeadList.size() > 0) {
                HashMap accHeadMap = (HashMap) accHeadList.get(0);
                if (accHeadMap != null && accHeadMap.containsKey("SERVICE_TAX_APPLICABLE")&& accHeadMap.containsKey("SERVICE_TAX_ID")) {
                    checkFlag = CommonUtil.convertObjToStr(accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_APPLICABLE",accHeadMap.get("SERVICE_TAX_APPLICABLE"));
                    checkForTaxMap.put("SERVICE_TAX_ID",accHeadMap.get("SERVICE_TAX_ID"));
                }
            }
        }
        return checkForTaxMap;
    }
    
    private void setServiceTaxForCommission() {
        HashMap whereMap = new HashMap();
        whereMap.put("CHITTAL_NO", CommonUtil.convertObjToStr(txtGdsNo.getText()));
        String checkFlag = "N";
        String bankChareHead = "";
        String otherChargeHead = "";
        double taxAmount = 0;       
    
        HashMap taxMap;
        List taxSettingsList = new ArrayList();
        HashMap checkForTaxMap = new HashMap();
        List resultList = ClientUtil.executeQuery("getGDSAccountheadForMoneyPayment", whereMap);
        if (resultList != null && resultList.size() > 0) {
            HashMap newMap = (HashMap) resultList.get(0);
            if (newMap != null && newMap.containsKey("COMMISION_HEAD")) {
                bankChareHead = CommonUtil.convertObjToStr(newMap.get("COMMISION_HEAD"));
                 checkForTaxMap = checkServiceTaxApplicable(bankChareHead);
                 if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                    if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                        taxAmount = CommonUtil.convertObjToDouble(txtCommisionAmt.getText());
                        if (taxAmount > 0) {
                            taxMap = new HashMap();
                            taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                            taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmount);
                                taxSettingsList.add(taxMap);                                
                            
                        }
                    }
                 }   
            }
            // Added by nithya on 07-09-2018 for KD 239 : Mds prized money payment service tax/gst not calculating charge amount  
            if (newMap != null && newMap.containsKey("CHARGE_PAYMENT_HEAD")) {
                otherChargeHead = CommonUtil.convertObjToStr(newMap.get("CHARGE_PAYMENT_HEAD"));
                checkForTaxMap = checkServiceTaxApplicable(otherChargeHead);
                if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                    if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                        taxAmount = CommonUtil.convertObjToDouble(txtChargeAmount.getText());
                        if (taxAmount > 0) {
                            taxMap = new HashMap();
                            taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                            taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmount);
                            taxSettingsList.add(taxMap);
                        }
                    }
                }
            }
            // End
            
            if (newMap != null && newMap.containsKey("NOTICE_CHARGES_HEAD") && rdoDefaulters_Yes.isSelected()) { // Added by nithya on 22-11-2019 for KD-910
                otherChargeHead = CommonUtil.convertObjToStr(newMap.get("NOTICE_CHARGES_HEAD"));
                checkForTaxMap = checkServiceTaxApplicable(otherChargeHead);
                if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                    if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                        taxAmount = CommonUtil.convertObjToDouble(txtNoticeAmt.getText());
                        if (taxAmount > 0) {
                            taxMap = new HashMap();
                            taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                            taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmount);
                            taxSettingsList.add(taxMap);
                        }
                    }
                }
            }
            
           if (newMap != null && newMap.containsKey("PENAL_INTEREST_HEAD") && rdoDefaulters_Yes.isSelected()) { // Added by nithya on 22-11-2019 for KD-910
                otherChargeHead = CommonUtil.convertObjToStr(newMap.get("PENAL_INTEREST_HEAD"));
                checkForTaxMap = checkServiceTaxApplicable(otherChargeHead);
                if (checkForTaxMap.containsKey("SERVICE_TAX_APPLICABLE") && checkForTaxMap.get("SERVICE_TAX_APPLICABLE") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_APPLICABLE")).equalsIgnoreCase("Y")) {
                    if (checkForTaxMap.containsKey("SERVICE_TAX_ID") && checkForTaxMap.get("SERVICE_TAX_ID") != null && CommonUtil.convertObjToStr(checkForTaxMap.get("SERVICE_TAX_ID")).length() > 0) {
                        taxAmount = CommonUtil.convertObjToDouble(txtPenalAmt.getText());
                        if (taxAmount > 0) {
                            taxMap = new HashMap();
                            taxMap.put("SETTINGS_ID", checkForTaxMap.get("SERVICE_TAX_ID"));
                            taxMap.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmount);
                            taxSettingsList.add(taxMap);
                        }
                    }
                }
            }
        }
        if(taxSettingsList != null && taxSettingsList.size() > 0){
            HashMap ser_Tax_Val = new HashMap();
            ser_Tax_Val.put(ServiceTaxCalculation.CURR_DT, currDt);
            ser_Tax_Val.put(ServiceTaxCalculation.TOT_AMOUNT, taxAmount);
             ser_Tax_Val.put("SERVICE_TAX_DATA", taxSettingsList);
            try {
                objServiceTax = new ServiceTaxCalculation();
                serviceTax_Map = objServiceTax.calculateServiceTax(ser_Tax_Val);                
                if (serviceTax_Map != null && serviceTax_Map.containsKey(ServiceTaxCalculation.TOT_TAX_AMT)) {
                    String amt = CommonUtil.convertObjToStr(serviceTax_Map.get(ServiceTaxCalculation.TOT_TAX_AMT));

                    
                    lblServiceTaxval.setText(amt);
                    observable.setLblServiceTaxval(lblServiceTaxval.getText());
                    serviceTax_Map.put(ServiceTaxCalculation.TOT_TAX_AMT, amt);
                    
                    double netAmount = 0.0;
                    if (txtTotalAmtPaid.getText().length() > 0) {
                        netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue() - CommonUtil.convertObjToDouble(txtTotalAmtPaid.getText()).doubleValue();
                    } else {
                        netAmount = CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue();
                    }
                    txtNetAmt.setText(String.valueOf(netAmount - (CommonUtil.convertObjToDouble(txtNoticeAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtChargeAmount.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtAribitrationAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtDiscountAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtPenalAmt.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtBonusRecovered.getText()).doubleValue()
                                + CommonUtil.convertObjToDouble(txtOverDueAmt.getText()).doubleValue()+ CommonUtil.convertObjToDouble(lblServiceTaxval.getText()))));
                    transactionUI.setCallingAmount(String.valueOf(CommonUtil.convertObjToDouble(txtPrizedAmt.getText()).doubleValue()
                    + CommonUtil.convertObjToDouble(txtBonusAmt.getText()).doubleValue() + CommonUtil.convertObjToDouble(txtCommisionAmt.getText()).doubleValue()-CommonUtil.convertObjToDouble(lblServiceTaxval.getText())));
                    
                    // End KD 239 : Mds prized money payment service tax/gst not calculating charge amount  
                } else {
                    lblServiceTaxval.setText("0.00");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else {
            lblServiceTaxval.setText("0.00");
        }
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

        btnAuthorize.setEnabled(btnNew.isEnabled());
        btnReject.setEnabled(btnNew.isEnabled());
        btnException.setEnabled(btnNew.isEnabled());
        btnView.setEnabled(!btnView.isEnabled());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChittalNo;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnMemberNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSchemeName;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CButton btnViewLedger;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CCheckBox chkBonusTrans;
    private com.see.truetransact.uicomponent.CCheckBox chkChangedBetween;
    private com.see.truetransact.uicomponent.CCheckBox chkMunnal;
    private com.see.truetransact.uicomponent.CCheckBox chkPartPay;
    private com.see.truetransact.uicomponent.CCheckBox chkThalayal;
    private com.see.truetransact.uicomponent.CLabel lblAmountRefunded;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAribitrationAmt;
    private com.see.truetransact.uicomponent.CLabel lblBonusAmt;
    private com.see.truetransact.uicomponent.CLabel lblBonusRecovered;
    private com.see.truetransact.uicomponent.CLabel lblChangedBetween;
    private com.see.truetransact.uicomponent.CLabel lblChangedMemberDetails;
    private com.see.truetransact.uicomponent.CLabel lblChargeAmount;
    private com.see.truetransact.uicomponent.CLabel lblChittalNo;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCommisionAmt;
    private com.see.truetransact.uicomponent.CLabel lblDefaulterComm;
    private com.see.truetransact.uicomponent.CLabel lblDefaulterInterst;
    private com.see.truetransact.uicomponent.CLabel lblDefaulters;
    private com.see.truetransact.uicomponent.CLabel lblDiscountAmt;
    private com.see.truetransact.uicomponent.CLabel lblDivisionNo;
    private com.see.truetransact.uicomponent.CLabel lblDrawDate;
    private com.see.truetransact.uicomponent.CLabel lblEarlierChitMemberName;
    private com.see.truetransact.uicomponent.CLabel lblEarlierChitMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblGroupNo;
    private com.see.truetransact.uicomponent.CLabel lblLiabilityMemberDetails;
    private com.see.truetransact.uicomponent.CLabel lblLiabilitySuretiesDetails;
    private com.see.truetransact.uicomponent.CLabel lblLoanDetails;
    private com.see.truetransact.uicomponent.CLabel lblMemberName;
    private com.see.truetransact.uicomponent.CLabel lblMemberNameVal;
    private com.see.truetransact.uicomponent.CLabel lblMemberNo;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMunnal;
    private com.see.truetransact.uicomponent.CLabel lblNetAmt;
    private com.see.truetransact.uicomponent.CLabel lblNoOfInstPaid;
    private com.see.truetransact.uicomponent.CLabel lblNoticeAmt;
    private com.see.truetransact.uicomponent.CLabel lblOverDueAmt;
    private com.see.truetransact.uicomponent.CLabel lblOverdueInst;
    private com.see.truetransact.uicomponent.CLabel lblPenalAmt;
    private com.see.truetransact.uicomponent.CLabel lblPin;
    private com.see.truetransact.uicomponent.CLabel lblPrizedAmt;
    private com.see.truetransact.uicomponent.CLabel lblPrizedInstNo;
    private com.see.truetransact.uicomponent.CLabel lblSchemeDesc;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblServiceTaxval;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace76;
    private com.see.truetransact.uicomponent.CLabel lblSpace77;
    private com.see.truetransact.uicomponent.CLabel lblSpace78;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblThalayal;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmtPaid;
    private com.see.truetransact.uicomponent.CLabel lblTotalInst;
    private com.see.truetransact.uicomponent.CLabel lblValArea;
    private com.see.truetransact.uicomponent.CLabel lblValCity;
    private com.see.truetransact.uicomponent.CLabel lblValPin;
    private com.see.truetransact.uicomponent.CLabel lblValState;
    private com.see.truetransact.uicomponent.CLabel lblValStreet;
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
    private com.see.truetransact.uicomponent.CPanel panAddressDetails;
    private com.see.truetransact.uicomponent.CPanel panBtnLedger;
    private com.see.truetransact.uicomponent.CPanel panDefaulterCharges;
    private com.see.truetransact.uicomponent.CPanel panDefaulters;
    private com.see.truetransact.uicomponent.CPanel panEarlierChitDetails;
    private com.see.truetransact.uicomponent.CPanel panGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance1;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance2;
    private com.see.truetransact.uicomponent.CPanel panInsideOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideSchemeDetails;
    private com.see.truetransact.uicomponent.CPanel panOtherDetailsInside;
    private com.see.truetransact.uicomponent.CPanel panOtherMDSDetails;
    private com.see.truetransact.uicomponent.CPanel panSchemeName;
    private com.see.truetransact.uicomponent.CPanel panSchemeName1;
    private com.see.truetransact.uicomponent.CPanel panSchemeName2;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTableChangedMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panTableLiabilityDetailsofMember;
    private com.see.truetransact.uicomponent.CPanel panTableLiabilityDetailsofSureties;
    private com.see.truetransact.uicomponent.CPanel panTableLoanDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDefaulters;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private com.see.truetransact.uicomponent.CRadioButton rdoDefaulters_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoDefaulters_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CScrollPane srpTableChangedMemberDetails;
    private com.see.truetransact.uicomponent.CScrollPane srpTableDetailsofSureties;
    private com.see.truetransact.uicomponent.CScrollPane srpTableLiabilityDetailsofMember;
    private com.see.truetransact.uicomponent.CScrollPane srpTableLoanDetails;
    private com.see.truetransact.uicomponent.CTabbedPane tabRemittanceProduct;
    private com.see.truetransact.uicomponent.CTable tblChangedMemberDetails;
    private com.see.truetransact.uicomponent.CTable tblLiabilityDetailsofMember;
    private com.see.truetransact.uicomponent.CTable tblLoanDetails;
    private com.see.truetransact.uicomponent.CTable tblSuretyDetails;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtDrawDate;
    private com.see.truetransact.uicomponent.CTextField txtAmountRefunded;
    private com.see.truetransact.uicomponent.CTextField txtAribitrationAmt;
    private com.see.truetransact.uicomponent.CTextField txtBonusAmt;
    private com.see.truetransact.uicomponent.CTextField txtBonusRecovered;
    private com.see.truetransact.uicomponent.CTextField txtChargeAmount;
    private com.see.truetransact.uicomponent.CTextField txtCommisionAmt;
    private com.see.truetransact.uicomponent.CTextField txtDefaulterBonus;
    private com.see.truetransact.uicomponent.CTextField txtDefaulterComm;
    private com.see.truetransact.uicomponent.CTextField txtDefaulterInterest;
    private com.see.truetransact.uicomponent.CTextField txtDiscountAmt;
    private com.see.truetransact.uicomponent.CTextField txtDivisionNo;
    private com.see.truetransact.uicomponent.CTextField txtEarlierChitMemberNumber;
    private com.see.truetransact.uicomponent.CTextField txtGdsNo;
    private com.see.truetransact.uicomponent.CTextField txtGroupName;
    private com.see.truetransact.uicomponent.CTextField txtMemberNo;
    private com.see.truetransact.uicomponent.CTextField txtNetAmt;
    private com.see.truetransact.uicomponent.CTextField txtNoOfInstPaid;
    private com.see.truetransact.uicomponent.CTextField txtNoticeAmt;
    private com.see.truetransact.uicomponent.CTextField txtOverDueAmt;
    private com.see.truetransact.uicomponent.CTextField txtOverdueInst;
    private com.see.truetransact.uicomponent.CTextField txtPenalAmt;
    private com.see.truetransact.uicomponent.CTextField txtPrizedAmt;
    private com.see.truetransact.uicomponent.CTextField txtPrizedInstNo;
    private com.see.truetransact.uicomponent.CTextField txtSubNo;
    private com.see.truetransact.uicomponent.CTextField txtTotalAmtPaid;
    private com.see.truetransact.uicomponent.CTextField txtTotalInst;
    // End of variables declaration//GEN-END:variables

    public static void main(String[] arg) {
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        GDSPrizedMoneyPaymentUI gui = new GDSPrizedMoneyPaymentUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}

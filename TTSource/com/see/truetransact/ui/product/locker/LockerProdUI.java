/**
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * BillsUI.java
 *
 * Created on March 18, 2004, 11:22 AM
 */

package com.see.truetransact.ui.product.locker;

import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.product.locker.LockerProdOB;
import com.see.truetransact.ui.product.locker.LockerProdMRB;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ComboBoxModel;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import java.text.DateFormat;
import java.util.*;

/**
 *
 * @author  Ashok
 */

public class LockerProdUI extends com.see.truetransact.uicomponent.CInternalFrame implements UIMandatoryField, Observer{
    
       // private BillsRB resourceBundle = new BillsRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.locker.LockerProdRB", ProxyParameters.LANGUAGE);
    java.util.ResourceBundle objMandatoryRB1;
    
    private HashMap mandatoryMap;
    private LockerProdOB observable;
    private LockerProdMRB objMandatoryRB;
    private String viewType="";
    final String AUTHORIZE="Authorize";
    private StringBuffer message;
    boolean tableBillsChargeClicked=false;
    private Date currDt = null;
    //private EnhancedTableModel tblInterestTable;
    
    /** Creates new form BillsUI */
    public LockerProdUI() {
        initSettings();
        tabBills.resetVisits();
    }
    
    /* Set the inital settings of BillsUI    */
    private void initSettings(){
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaximumLength();
        //setHelpMessage();
       enableDisablePanBillsDetails(false);
        enableDisablePanBillsRates(false);
        enableDisablePanBillsCharges(false);
        //        ClientUtil.enableDisable(panBillsDetails, false);
//        lblBaseCurrency.setVisible(false);
//        cboBaseCurrency.setVisible(false);
//        lblFixedRate.setVisible(false);
//        txtFixRate.setVisible(false);
        enableDisableProductId(false);
//        lblStateCapMetroOthers.setVisible(false);
//        panrdoContraAccountHead.setVisible(false);
//        lblContraAccountHead.setVisible(false);
//        lblContractAccountHead.setVisible(false);
//        txtContractAccountHead.setVisible(false);
//        btnContractAccountHead.setVisible(false);
        setButtonEnableDisable();
        tdtToDt.setEnabled(false);
        objMandatoryRB1=java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.locker.LockerProdMRB",ProxyParameters.LANGUAGE);
       
        
    }
    private void enableDisableProductId(boolean flag) {
        txtProductId.setEditable(flag);
        txtProductId.setEnabled(flag);
    }
    private void setMaximumLength() {
        txtProductId.setMaxLength(16);
        txtProdDesc.setMaxLength(32);
//        txtDiscountRateBills.setMaxLength(5);
//        txtDiscountRateBills.setValidation(new PercentageValidation());
//        txtOverdueRateBills.setMaxLength(5);
//        txtOverdueRateBills.setValidation(new PercentageValidation());
//        txtRateForCBP.setMaxLength(5);
//        txtRateForCBP.setValidation(new PercentageValidation());
//        txtAtParLimit.setMaxLength(16);
//        txtAtParLimit.setValidation(new NumericValidation());
//        txtCleanBills.setMaxLength(5);
//        txtCleanBills.setValidation(new PercentageValidation());
//        txtTransitPeriod.setMaxLength(5);
//        txtTransitPeriod.setValidation(new NumericValidation());
//        txtIntDays.setMaxLength(5);
//        txtIntDays.setValidation(new NumericValidation());
//        txtDefaultPostage.setMaxLength(8);
//        txtDefaultPostage.setValidation(new PercentageValidation());
//        txtRateForDelay.setMaxLength(8);
//        txtRateForDelay.setValidation(new PercentageValidation());
//        txtFromSlab.setValidation(new CurrencyValidation(14,2));
//        txtToSlab.setValidation(new CurrencyValidation(14,2));
//        txtCommision.setValidation(new CurrencyValidation(14,2));
        txtAmt.setValidation(new CurrencyValidation(14,2));
        txtServiceTax.setValidation(new CurrencyValidation(14,2));
//        txtFixRate.setValidation(new CurrencyValidation(14,2));
//        txtRateVal.setValidation(new CurrencyValidation(14,2));
//        txtForEvery.setValidation(new CurrencyValidation(14,2));
//        txtCommision.setValidation(new PercentageValidation());
        txtServiceTax.setValidation(new PercentageValidation());
        txtLockerRentAcHd.setAllowAll(true);
        txtLockerSusAcHd.setAllowAll(true);
        txtLockerMiscAcHd.setAllowAll(true);
        txtLockerBrkAcHd.setAllowAll(true);
        txtLockerServAcHd.setAllowAll(true);
        txtLockerRentAdvAcHd.setAllowAll(true);
        
    }
    private void enableDisablePanBillsDetails(boolean flag) {
        ClientUtil.enableDisable(panBillsDetails, flag);
        btnSave.setEnabled(true);
        btnDelete.setEnabled(true);
    }
    private void enableDisablePanBillsRates(boolean flag) {
//        ClientUtil.enableDisable(panRate, flag);
    }
    private void enableDisablePanBillsCharges(boolean flag) {
        ClientUtil.enableDisable(panCharges, flag);
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        //cboChargeType.setName("cboChargeType");
        
        
        btnAuthorize.setName("btnAuthorize");
//        btnBillsRealisedHead.setName("btnBillsRealisedHead");
        btnCancel.setName("btnCancel");
//        btnChargesAccountHead.setName("btnChargesAccountHead");
        btnClose.setName("btnClose");
//        btnCommissionAccountHead.setName("btnCommissionAccountHead");
//        btnContractAccountHead.setName("btnContractAccountHead");
//        btnDDAccountHead.setName("btnDDAccountHead");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
//        btnGLAccountHead.setName("btnGLAccountHead");
//        btnIBRAccountHead.setName("btnIBRAccountHead");
//        btnServiceTaxHd.setName("btnServiceTaxHd");
//        btnInterestAccountHead.setName("btnInterestAccountHead");
//        btnMarginAccountHead.setName("btnMarginAccountHead");
        btnNew.setName("btnNew");
//        btnOthersHead.setName("btnOthersHead");
//        btnPostageAccountHead.setName("btnPostageAccountHead");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
//        btnTelChargesHead.setName("btnTelChargesHead");
        tabBills.setName("tabBills");
//        cboBaseCurrency.setName("cboBaseCurrency");
//        cboOperatesLike.setName("cboOperatesLike");
//        cboRegType.setName("cboRegType");
//        cboSubRegType.setName("cboSubRegType");
//        cboTransitPeriod.setName("cboTransitPeriod");
//        cboIntDays.setName("cboIntDays");
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        btnTabDelete.setName("btnTabDelete");
//        cboInstrumentType.setName("cboInstrumentType");
        cboChargeType.setName("cboChargeType");
        cboMetric.setName("cboMetric");
       lblPenalToBeCollected.setName("lblPenalToBeCollected");
       //rdoPenalToBeCollected.setName("rdoPenalToBeCollected");
//        cboCustCategory.setName("cboCustCategory");
//        cboRateType.setName("cboRateType");
//        lblAtParLimit.setName("lblAtParLimit");
//        lblBaseCurrency.setName("lblBaseCurrency");
//        lblBillsRealisedHD.setName("lblBillsRealisedHD");
//        lblChargesAccountHead.setName("lblChargesAccountHead");
//        lblCleanBillsPurchased.setName("lblCleanBillsPurchased");
//        lblCleanBills_Per.setName("lblCleanBills_Per");
//        lblCommissionAccountHead.setName("lblCommissionAccountHead");
//        lblContraAccountHead.setName("lblContraAccountHead");
//        lblContractAccountHead.setName("lblContractAccountHead");
//        lblDDAccountHead.setName("lblDDAccountHead");
//        lblDefaultPostage.setName("lblDefaultPostage");
//        lblDefaultPostage_Per.setName("lblDefaultPostage_Per");
//        lblIntICC.setName("lblIntICC");
//        lblDiscountRateBills_Per.setName("lblDiscountRateBills_Per");
//        lblDiscountRateOfBD.setName("lblDiscountRateOfBD");
//        lblGLAccountHead.setName("lblGLAccountHead");
//        lblIBRAccountHead.setName("lblIBRAccountHead");
//        lblInterestAccountHead.setName("lblInterestAccountHead");
//        lblMarginAccountHead.setName("lblMarginAccountHead");
        lblMsg.setName("lblMsg");
//        lblOperatesLike.setName("lblOperatesLike");
//        lblOthersHead.setName("lblOthersHead");
//        lblOverdueInterestForBD.setName("lblOverdueInterestForBD");
//        lblOverdueRateBills_Per.setName("lblOverdueRateBills_Per");
//        lblOverdueRateCBP.setName("lblOverdueRateCBP");
//        lblPostDtdCheqAllowed.setName("lblPostDtdCheqAllowed");
//        lblPostageAccountHead.setName("lblPostageAccountHead");
        lblProductId.setName("lblProductId");
        lblProdDesc.setName("lblProdDesc");
        lblDimensions.setName("lblDimensions");
        lblLength.setName("lblLength");
        lblBreth.setName("lblBreth");
        lblHeight.setName("lblHeight");
        lblLockerRentAcHd.setName("lblLockerRentAcHd");
        lblLockerRentAdvAcHd.setName("lblLockerRentAdvAcHd");
        lblLockerSusAcHd.setName("lblLockerSusAcHd");
        lblLockerMiscAcHd.setName("lblLockerMiscAcHd");
        lblLockerBrkAcHd.setName("lblLockerBrkAcHd");
        lblLockerServAcHd.setName("lblLockerServAcHd");
        lblFromDt.setName("lblFromDt");
        lblToDt.setName("lblToDt");
        lblAmt.setName("lblAmt");
        lblServiceTax.setName("lblServiceTax");
//        lblRateForCBP_Per.setName("lblRateForCBP_Per");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
//        lblStateCapMetroOthers.setName("lblStateCapMetroOthers");
        lblStatus.setName("lblStatus");
//        lblTelChargesHead.setName("lblTelChargesHead");
//        lblTransitPeriod.setName("lblTransitPeriod");
//        lblInstrument.setName("lblInstrument");
        lblChargeType.setName("lblChargeType");
//        lblCustCategory.setName("lblCustCategory");
//        lblFromSlab.setName("lblFromSlab");
//        lblToSlab.setName("lblToSlab");
//        lblFixedRate.setName("lblFixedRate");
//        lblForEvery.setName("lblForEvery");
//        lblCommision.setName("lblCommision");
        lblServiceTax.setName("lblServiceTax");
        mbrBills.setName("mbrBills");
//        panIntICC.setName("panIntICC");
        panAcHdDetails.setName("panAcHdDetails");
//        panAcHeadDetails.setName("panAcHeadDetails");
        panBillsAcHeadDetails.setName("panBillsAcHeadDetails");
        panBillsDetails.setName("panBillsDetails");
//        panDiscountRate.setName("panDiscountRate");
//        panOverdueRateBills.setName("panOverdueRateBills");
//        panOverdueRateBills1.setName("panOverdueRateBills1");
//        panOverdueRateBills2.setName("panOverdueRateBills2");
//        panOverdueRateBills3.setName("panOverdueRateBills3");
        panProductId.setName("panProductId");
//        panRate.setName("panRate");
        panBillsCharges.setName("panBillsCharges");
        panCharges.setName("panCharges");
//        panForEvery.setName("panForEvery");
        panStatus.setName("panStatus");
//        panTransitPeriod.setName("panTransitPeriod");
//        panIntDays.setName("panIntDays");
//        panrdoContraAccountHead.setName("panrdoContraAccountHead");
//        panrdoPostDtdCheqAllowed.setName("panrdoPostDtdCheqAllowed");
//        rdoContraAccountHead_No.setName("rdoContraAccountHead_No");
//        rdoContraAccountHead_Yes.setName("rdoContraAccountHead_Yes");
//        rdoPostDtdCheqAllowed_No.setName("rdoPostDtdCheqAllowed_No");
//        rdoPostDtdCheqAllowed_Yes.setName("rdoPostDtdCheqAllowed_Yes");
//        cRadio_ICC_Yes.setName("cRadio_ICC_Yes");
//        cRadio_ICC_No.setName("cRadio_ICC_No");
//        sptBills_Vert.setName("sptBills_Vert");
//        txtAtParLimit.setName("txtAtParLimit");
//        txtBillsRealisedHead.setName("txtBillsRealisedHead");
//        txtChargesAccountHead.setName("txtChargesAccountHead");
//        txtCleanBills.setName("txtCleanBills");
//        txtCommissionAccountHead.setName("txtCommissionAccountHead");
//        txtContractAccountHead.setName("txtContractAccountHead");
//        txtDDAccountHead.setName("txtDDAccountHead");
//        txtDefaultPostage.setName("txtDefaultPostage");
//        txtRateForDelay.setName("txtRateForDelay");
//        txtDiscountRateBills.setName("txtDiscountRateBills");
//        txtGLAccountHead.setName("txtGLAccountHead");
//        txtIBRAccountHead.setName("txtIBRAccountHead");
//        txtServiceTaxHd.setName("txtServiceTaxHd");
//        txtInterestAccountHead.setName("txtInterestAccountHead");
//        txtMarginAccountHead.setName("txtMarginAccountHead");
//        txtOthersHead.setName("txtOthersHead");
//        txtOverdueRateBills.setName("txtOverdueRateBills");
//        txtPostageAccountHead.setName("txtPostageAccountHead");
        txtProductId.setName("txtProductId");
        txtProdDesc.setName("txtProdDesc");
        txtLength.setName("txtLength");
        txtBreth.setName("txtBreth");
        txtHeight.setName("txtHeight");
        cboMetric.setName("cboMetric");
        txtLockerRentAcHd.setName("txtLockerRentAcHd");
        txtLockerRentAdvAcHd.setName("txtLockerRentAdvAcHd");
        txtLockerSusAcHd.setName("txtLockerSusAcHd");
        txtLockerMiscAcHd.setName("txtLockerMiscAcHd");
        txtLockerBrkAcHd.setName("txtLockerBrkAcHd");
        txtLockerServAcHd.setName("txtLockerServAcHd");
        tdtFromDt.setName("tdtFromDt");
        tdtToDt.setName("tdtToDt");
        txtAmt.setName("txtAmt");
//        txtRateForCBP.setName("txtRateForCBP");
//        txtTelChargesHead.setName("txtTelChargesHead");
//        txtTransitPeriod.setName("txtTransitPeriod");
//        txtIntDays.setName("txtIntDays");
//        txtFromSlab.setName("txtFromSlab");
//        txtToSlab.setName("txtToSlab");
//        txtCommision.setName("txtCommision");
        txtServiceTax.setName("txtServiceTax");
//        txtFixRate.setName("txtFixRate");
//        txtRateVal.setName("txtRateVal");
//        txtForEvery.setName("txtForEvery");
        tblBillsCharges.setName("tblBillsCharges");
       
        
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        //changed jithins
////        lblMarginAccountHead.setText(resourceBundle.getString("lblMarginAccountHead"));
//        btnClose.setText(resourceBundle.getString("btnClose"));
////        lblPostDtdCheqAllowed.setText(resourceBundle.getString("lblPostDtdCheqAllowed"));
////        rdoPostDtdCheqAllowed_Yes.setText(resourceBundle.getString("rdoPostDtdCheqAllowed_Yes"));
////        lblChargesAccountHead.setText(resourceBundle.getString("lblChargesAccountHead"));
////        rdoContraAccountHead_No.setText(resourceBundle.getString("rdoContraAccountHead_No"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
////        btnDDAccountHead.setText(resourceBundle.getString("btnDDAccountHead"));
////        lblCommissionAccountHead.setText(resourceBundle.getString("lblCommissionAccountHead"));
//        btnException.setText(resourceBundle.getString("btnException"));
//        lblMsg.setText(resourceBundle.getString("lblMsg"));
//        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
////        lblDDAccountHead.setText(resourceBundle.getString("lblDDAccountHead"));
////        btnGLAccountHead.setText(resourceBundle.getString("btnGLAccountHead"));
////        btnInterestAccountHead.setText(resourceBundle.getString("btnInterestAccountHead"));
////        lblTransitPeriod.setText(resourceBundle.getString("lblTransitPeriod"));
//        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
//        lblProductId.setText(resourceBundle.getString("lblProductId"));
//        btnSave.setText(resourceBundle.getString("btnSave"));
//        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
////        lblStateCapMetroOthers.setText(resourceBundle.getString("lblStateCapMetroOthers"));
//        lblStatus.setText(resourceBundle.getString("lblStatus"));
////        btnContractAccountHead.setText(resourceBundle.getString("btnContractAccountHead"));
//        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
////        lblBaseCurrency.setText(resourceBundle.getString("lblBaseCurrency"));
//        btnDelete.setText(resourceBundle.getString("btnDelete"));
////        btnMarginAccountHead.setText(resourceBundle.getString("btnMarginAccountHead"));
//        btnReject.setText(resourceBundle.getString("btnReject"));
//        btnEdit.setText(resourceBundle.getString("btnEdit"));
////        lblPostageAccountHead.setText(resourceBundle.getString("lblPostageAccountHead"));
////        btnChargesAccountHead.setText(resourceBundle.getString("btnChargesAccountHead"));
////        rdoPostDtdCheqAllowed_No.setText(resourceBundle.getString("rdoPostDtdCheqAllowed_No"));
////        lblIBRAccountHead.setText(resourceBundle.getString("lblIBRAccountHead"));
////        btnPostageAccountHead.setText(resourceBundle.getString("btnPostageAccountHead"));
//        btnNew.setText(resourceBundle.getString("btnNew"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
////        lblInterestAccountHead.setText(resourceBundle.getString("lblInterestAccountHead"));
////        rdoContraAccountHead_Yes.setText(resourceBundle.getString("rdoContraAccountHead_Yes"));
////        cRadio_ICC_Yes.setText(resourceBundle.getString("cRadio_ICC_Yes"));
////        cRadio_ICC_No.setText(resourceBundle.getString("cRadio_ICC_No"));
////        lblContraAccountHead.setText(resourceBundle.getString("lblContraAccountHead"));
////        btnCommissionAccountHead.setText(resourceBundle.getString("btnCommissionAccountHead"));
////        lblAtParLimit.setText(resourceBundle.getString("lblAtParLimit"));
////        lblGLAccountHead.setText(resourceBundle.getString("lblGLAccountHead"));
////        btnIBRAccountHead.setText(resourceBundle.getString("btnIBRAccountHead"));
////        btnServiceTaxHd.setText(resourceBundle.getString("btnServiceTaxHd"));
//        btnPrint.setText(resourceBundle.getString("btnPrint"));
////        lblContractAccountHead.setText(resourceBundle.getString("lblContractAccountHead"));
////        lblInstrument.setText(resourceBundle.getString("lblInstrument"));
//        lblChargeType.setText(resourceBundle.getString("lblChargeType"));
////        lblCustCategory.setText(resourceBundle.getString("lblCustCategory"));
//        
////        lblFromSlab.setText(resourceBundle.getString("lblFromSlab"));
////        lblToSlab.setText(resourceBundle.getString("lblToSlab"));
////        lblCommision.setText(resourceBundle.getString("lblCommision"));
//        lblServiceTax.setText(resourceBundle.getString("lblServiceTax"));
////        lblFixedRate.setText(resourceBundle.getString("lblFixedRate"));
////        lblForEvery.setText(resourceBundle.getString("lblForEvery"));
    }
    
    /* To remove the radio buttons */
    private void removeRadioButtons(){
     rdoRefund.remove(rdoRefundYes);
        rdoRefund.remove(rdoRefundNo);
        rdoPenalToBeCollected.remove(rdoPenalYes);
        rdoPenalToBeCollected.remove(rdoPenalNo);
        
    }
    
    /* To remove the radio buttons */
    private void addRadioButtons(){
        rdoRefund = new CButtonGroup();
        rdoRefund.add(rdoRefundYes);
        rdoRefund.add(rdoRefundNo);
        rdoPenalToBeCollected= new CButtonGroup();
        rdoPenalToBeCollected.add(rdoPenalYes);
        rdoPenalToBeCollected.add(rdoPenalNo);
          
    }
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
       // System.out.println("xxxxxxxx");
       
        txtProductId.setText(observable.getTxtProductId());
        //        //		cboBaseCurrency.setSelectedItem(observable.getCboBaseCurrency());
//        cboOperatesLike.setSelectedItem(observable.getCboOperatesLike());
//        cboRegType.setSelectedItem(observable.getCboRegType());
//        cboSubRegType.setSelectedItem(observable.getCboSubRegType());
        txtProdDesc.setText(observable.getTxtProdDesc());
        txtLength.setText(observable.getTxtLength());
        txtBreth.setText(observable.getTxtBreth());
        txtHeight.setText(observable.getTxtHeight());
//        rdoContraAccountHead_Yes.setSelected(observable.getRdoContraAccountHead_Yes());
//        rdoContraAccountHead_No.setSelected(observable.getRdoContraAccountHead_No());
//        cRadio_ICC_Yes.setSelected(observable.isCRadio_ICC_Yes());
//        cRadio_ICC_No.setSelected(observable.isCRadio_ICC_No());
//        rdoPostDtdCheqAllowed_Yes.setSelected(observable.getRdoPostDtdCheqAllowed_Yes());
//        rdoPostDtdCheqAllowed_No.setSelected(observable.getRdoPostDtdCheqAllowed_No());
        txtLockerRentAcHd.setText(observable.getTxtLockerRentAcHd());
        txtLockerRentAdvAcHd.setText(observable.getTxtLockerRentAdvAcHd());
        txtLockerSusAcHd.setText(observable.getTxtLockerSusAcHd());
        txtLockerMiscAcHd.setText(observable.getTxtLockerMiscAcHd());
        txtLockerBrkAcHd.setText(observable.getTxtLockerBrkAcHd());
        txtLockerServAcHd.setText(observable.getTxtLockerServAcHd());
//        txtContractAccountHead.setText(observable.getTxtContractAccountHead());
//        txtIBRAccountHead.setText(observable.getTxtIBRAccountHead());
//        txtServiceTaxHd.setText(observable.getTxtServiceTaxHd());
//        txtOthersHead.setText(observable.getTxtOthersHead());
//        txtTelChargesHead.setText(observable.getTxtTelChargesHead());
//        txtAtParLimit.setText(observable.getTxtAtParLimit());
//        txtDiscountRateBills.setText(observable.getTxtDiscountRateBills());
//        txtDefaultPostage.setText(observable.getTxtDefaultPostage());
//        txtRateForDelay.setText(observable.getTxtRateForDelay());
//        txtOverdueRateBills.setText(observable.getTxtOverdueRateBills());
//        txtRateForCBP.setText(observable.getTxtRateForCBP());
//        txtCleanBills.setText(observable.getTxtCleanBills());
//        txtTransitPeriod.setText(observable.getTxtTransitPeriod());
//        txtIntDays.setText(observable.getTxtIntDays());
//        cboTransitPeriod.setSelectedItem(observable.getCboTransitPeriod());
//        cboIntDays.setSelectedItem(observable.getCboIntDays());
//        txtFromSlab.setText(observable.getTxtFromSlab());
//        txtToSlab.setText(observable.getTxtToSlab());
//        txtCommision.setText(observable.getTxtCommision());
        txtServiceTax.setText(observable.getTxtServiceTax());
        txtAmt.setText(observable.getTxtAmt());
//        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
        cboMetric.setSelectedItem(observable.getCboMetric());
//        cboCustCategory.setSelectedItem(observable.getCboCustCategory());
//        cboRateType.setSelectedItem(observable.getCboRateType());
//        txtFixRate.setText(observable.getTxtFixRate());
//        txtForEvery.setText(observable.getTxtForEvery());
//        txtRateVal.setText(observable.getTxtRateVal());
        tblBillsCharges.setModel(observable.getTblBillsCharges());
        tdtFromDt.setDateValue(observable.getTdtFromDt());
        tdtToDt.setDateValue(observable.getTdtToDt());
        
        txtPenalRateInterest.setText(observable.getTxtPenalRateInterest());
        txtPenalIntrstAcHd.setText(observable.getTxtPenalIntrstAcHd());
       if(observable.isRdoRefundYes()==true ||observable.isRdoRefundYes()==false ){
        rdoRefundYes.setSelected(observable.isRdoRefundYes());
        }
       if(observable.isRdoRefundNo()==true || observable.isRdoRefundNo()==false){
        rdoRefundNo.setSelected(observable.isRdoRefundNo());
        }
       // System.out.println("isRdoPenalYes***************"+isRdoPenalYes());
         if(observable.isRdoPenalYes()==true ||observable.isRdoPenalYes()==false ){
        rdoPenalYes.setSelected(observable.isRdoPenalYes());
        }
       if(observable.isRdoPenalNo()==true || observable.isRdoPenalNo()==false){
        rdoPenalNo.setSelected(observable.isRdoPenalNo());
        
        }
        System.out.println("22222222");
        
        addRadioButtons();
        
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
   public void updateOBFields() {
        
       
        observable.setTxtProductId(txtProductId.getText());
//        //		observable.setCboBaseCurrency((String) cboBaseCurrency.getSelectedItem());
//        observable.setCboOperatesLike((String) cboOperatesLike.getSelectedItem());
//        observable.setCboRegType((String) cboRegType.getSelectedItem());
//        observable.setCboSubRegType((String) cboSubRegType.getSelectedItem());
        observable.setTxtProdDesc(txtProdDesc.getText());
        observable.setTxtLength(txtLength.getText());
        observable.setTxtBreth(txtBreth.getText());
        observable.setTxtHeight(txtHeight.getText());
        observable.setRdoPenalYes(rdoPenalYes.isSelected());
        observable.setRdoPenalNo(rdoPenalNo.isSelected());
        observable.setTxtPenalRateInterest(txtPenalRateInterest.getText());
        //        observable.setRdoContraAccountHead_Yes(rdoContraAccountHead_Yes.isSelected());
//        observable.setRdoContraAccountHead_No(rdoContraAccountHead_No.isSelected());
//        observable.setCRadio_ICC_No(cRadio_ICC_No.isSelected());
//        observable.setCRadio_ICC_Yes(cRadio_ICC_Yes.isSelected());
//        observable.setRdoPostDtdCheqAllowed_Yes(rdoPostDtdCheqAllowed_Yes.isSelected());
//        observable.setRdoPostDtdCheqAllowed_No(rdoPostDtdCheqAllowed_No.isSelected());
//        observable.setTxtDDAccountHead(txtDDAccountHead.getText());
        observable.setTxtLockerRentAcHd(txtLockerRentAcHd.getText());
        observable.setTxtLockerRentAdvAcHd(txtLockerRentAdvAcHd.getText());
        observable.setTxtPenalIntrstAcHd(txtPenalIntrstAcHd.getText());
        observable.setTxtLockerBrkAcHd(txtLockerBrkAcHd.getText());
        observable.setTxtLockerMiscAcHd(txtLockerMiscAcHd.getText());
        observable.setTxtLockerSusAcHd(txtLockerSusAcHd.getText());
//        observable.setTxtContractAccountHead(txtContractAccountHead.getText());
//        observable.setTxtIBRAccountHead(txtIBRAccountHead.getText());
        observable.setTxtLockerServAcHd(txtLockerServAcHd.getText());
//        observable.setTxtOthersHead(txtOthersHead.getText());
//        observable.setTxtTelChargesHead(txtTelChargesHead.getText());
//        observable.setTxtAtParLimit(txtAtParLimit.getText());
//        observable.setTxtDiscountRateBills(txtDiscountRateBills.getText());
//        observable.setTxtDefaultPostage(txtDefaultPostage.getText());
//        observable.setTxtRateForDelay(txtRateForDelay.getText());
//        observable.setTxtOverdueRateBills(txtOverdueRateBills.getText());
//        observable.setTxtRateForCBP(txtRateForCBP.getText());
//        observable.setTxtCleanBills(txtCleanBills.getText());
//        observable.setTxtTransitPeriod(txtTransitPeriod.getText());
//        observable.setTxtIntDays(txtIntDays.getText());
//        observable.setCboTransitPeriod((String) cboTransitPeriod.getSelectedItem()); 
//        observable.setCboIntDays((String) cboIntDays.getSelectedItem());
//        observable.setCboInstrumentType((String) cboInstrumentType.getSelectedItem());
        observable.setCboChargeType((String) cboChargeType.getSelectedItem());
        observable.setCboMetric((String) cboMetric.getSelectedItem());
      observable.setRdoRefundYes(rdoRefundYes.isSelected());
      observable.setRdoRefundNo(rdoRefundNo.isSelected());
//        observable.setCboCustCategory((String) cboCustCategory.getSelectedItem());
        observable.setTdtFromDt(tdtFromDt.getDateValue());
        
       
      
        //java.util.Date d=dateFormat.parse(date);
    //  int MILLIS_IN_DAY=1000*60*60*24;
        observable.setTdtToDt(tdtToDt.getDateValue());
       //observable.setTdtToDt(dateFormat.format(d.getTime()-MILLIS_IN_DAY));
       
      
              //else
   //observable.setTdtToDt("");
                 
       
       
   
      // String dd2=(String) dd1;
    // java.util.Date d=(java.util.Date)
     //observable.setTdtToDt();
    // String dd=(String)dd;
     
    
     
     
     
      
     
       
    
//        observable.setTxtFromSlab(txtFromSlab.getText());
//        observable.setTxtToSlab(txtToSlab.getText());  
        observable.setTxtAmt(txtAmt.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        
//        observable.setTxtFixRate(txtFixRate.getText());
//        observable.setTxtForEvery(txtForEvery.getText());
//        observable.setCboRateType((String) cboRateType.getSelectedItem());
//        observable.setTxtRateVal(txtRateVal.getText());
        observable.setTblBillsCharges((com.see.truetransact.clientutil.EnhancedTableModel)tblBillsCharges.getModel());
        
    }
    
   
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductId", new Boolean(true));
        mandatoryMap.put("txtProdDesc", new Boolean(true));
        mandatoryMap.put("txtLength", new Boolean(true));
        mandatoryMap.put("txtBreth", new Boolean(true));
        mandatoryMap.put("txtHeight", new Boolean(true));
        mandatoryMap.put("cboMetric", new Boolean(true));
        mandatoryMap.put("txtLockerRentAcHd", new Boolean(true));
        mandatoryMap.put("cboChargeType",new Boolean(true));
        mandatoryMap.put("tdtFromDt",new Boolean(true));
        mandatoryMap.put("tdtToDt",new Boolean(true));
        mandatoryMap.put("txtAmt",new Boolean(true));
        mandatoryMap.put("rdoPenalToBeCollected",new Boolean(true));
                
        mandatoryMap.put("cboBaseCurrency", new Boolean(true));
        mandatoryMap.put("cboOperatesLike", new Boolean(true));
        mandatoryMap.put("cboSubRegType", new Boolean(true));
        mandatoryMap.put("cboRegType", new Boolean(true));
        mandatoryMap.put("txtGLAccountHead", new Boolean(true));
        mandatoryMap.put("txtInterestAccountHead", new Boolean(true));
        mandatoryMap.put("txtChargesAccountHead", new Boolean(true));
        mandatoryMap.put("rdoContraAccountHead_Yes", new Boolean(true));
        mandatoryMap.put("rdoPostDtdCheqAllowed_Yes", new Boolean(true));
        mandatoryMap.put("txtDDAccountHead", new Boolean(true));
        mandatoryMap.put("txtBillsRealisedHead", new Boolean(true));
        mandatoryMap.put("txtMarginAccountHead", new Boolean(true));
        mandatoryMap.put("txtCommissionAccountHead", new Boolean(true));
        mandatoryMap.put("txtPostageAccountHead", new Boolean(true));
        mandatoryMap.put("txtContractAccountHead", new Boolean(true));
        mandatoryMap.put("txtIBRAccountHead", new Boolean(true));
        mandatoryMap.put("txtOthersHead", new Boolean(true));
        mandatoryMap.put("txtTelChargesHead", new Boolean(true));
        mandatoryMap.put("txtAtParLimit", new Boolean(true));
        mandatoryMap.put("txtDiscountRateBills", new Boolean(true));
        mandatoryMap.put("txtDefaultPostage", new Boolean(true));
        mandatoryMap.put("txtOverdueRateBills", new Boolean(true));
        mandatoryMap.put("txtRateForCBP", new Boolean(true));
        mandatoryMap.put("txtCleanBills", new Boolean(true));
        mandatoryMap.put("txtTransitPeriod", new Boolean(true));
        mandatoryMap.put("cboTransitPeriod", new Boolean(true));
        mandatoryMap.put("txtFromSlab", new Boolean(true));
        mandatoryMap.put("txtToSlab", new Boolean(true));
        mandatoryMap.put("txtCommision", new Boolean(true));
        mandatoryMap.put("txtServiceTax", new Boolean(true));
        mandatoryMap.put("cboInstrumentType", new Boolean(true));
        mandatoryMap.put("cboChargeType", new Boolean(true));
        mandatoryMap.put("cboCustCategory", new Boolean(true));
       
        
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
        objMandatoryRB = new LockerProdMRB();
        txtProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductId"));
//        cboBaseCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBaseCurrency"));
//        cboOperatesLike.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOperatesLike"));
//        cboRegType.setHelpMessage(lblMsg,  objMandatoryRB.getString("cboRegType"));
//        cboSubRegType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubRegType"));
        txtProdDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProdDesc"));
//        txtGLAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGLAccountHead"));
//        txtInterestAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestAccountHead"));
//        txtChargesAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargesAccountHead"));
//        rdoContraAccountHead_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoContraAccountHead_Yes"));
//        rdoPostDtdCheqAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPostDtdCheqAllowed_Yes"));
//        cRadio_ICC_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("cRadio_ICC_Yes"));
//        txtDDAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDDAccountHead"));
//        txtBillsRealisedHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillsRealisedHead"));
//        txtMarginAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMarginAccountHead"));
//        txtCommissionAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommissionAccountHead"));
//        txtPostageAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPostageAccountHead"));
//        txtContractAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContractAccountHead"));
//        txtIBRAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIBRAccountHead"));
//        txtServiceTaxHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTaxHd"));
//        txtOthersHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOthersHead"));
//        txtTelChargesHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTelChargesHead"));
//        txtAtParLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAtParLimit"));
//        txtDiscountRateBills.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiscountRateBills"));
//        txtDefaultPostage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDefaultPostage"));
//        txtRateForDelay.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateForDelay"));
//        txtOverdueRateBills.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOverdueRateBills"));
//        txtRateForCBP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateForCBP"));
//        txtCleanBills.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCleanBills"));
//        txtTransitPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransitPeriod"));
//        txtIntDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntDays"));
//        cboTransitPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransitPeriod"));
//        cboIntDays.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntDays"));
//        txtFromSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromSlab"));
//        txtToSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToSlab"));
//        txtCommision.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommision"));
        txtServiceTax.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTax"));
//        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        cboChargeType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChargeType"));
//        cboCustCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustCategory"));
//        txtFixRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFixRate"));
    }
    
    private void setObservable() {
        try{
            observable = LockerProdOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            System.out.println("Exception is caught "+e);
        }
    }
    
    /* Initialising the Data in the combobox in the ui    */
    private void initComponentData() {
        try{
//            cboTransitPeriod.setModel(observable.getCbmTransitPeriod());
//            cboOperatesLike.setModel(observable.getCbmOperatesLike());
//            cboRegType.setModel(observable.getCbmRegType());
//            cboSubRegType.setModel(observable.getCbmSubRegType());
//           cboInstrumentType.setModel(observable.getCbmInstrumentType());
           cboChargeType.setModel(observable.getCbmChargeType());
           cboMetric.setModel(observable.getCbmMetric());
           new MandatoryCheck().putMandatoryMarks(getClass().getName(),panBillsDetails);
//           cboCustCategory.setModel(observable.getCbmCustCategory());
//           cboRateType.setModel(observable.getCbmRateType());
//           cboIntDays.setModel(observable.getCbmIntDays());
            //            cboBaseCurrency.setModel(observable.getCbmBaseCurrency());
        }catch(ClassCastException e){
            System.out.println("Inside Intilize Data");
            e.printStackTrace();
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
        lblStatus.setText(observable.getLblStatus());
    }
    
    
    private void callView(String currField) {
        viewType = currField;
        HashMap viewMap = new HashMap();
        if (currField.equals(ClientConstants.ACTION_STATUS[2])  ||
        currField.equals(ClientConstants.ACTION_STATUS[3]) ||
        currField.equals(ClientConstants.ACTION_STATUS[20])) {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectLockerProdMap");
       } else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBills");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        final String accHdId="AC_HD_ID";
       if (viewType != null) {
            if (viewType.equals(ClientConstants.ACTION_STATUS[2]) ||
            viewType.equals(ClientConstants.ACTION_STATUS[3])|| viewType.equals(AUTHORIZE) ||
           viewType.equals(ClientConstants.ACTION_STATUS[20])) {
                hash.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                observable.populateData(hash);
               if (viewType.equals(ClientConstants.ACTION_STATUS[3]) || viewType.equals(AUTHORIZE) ) {
                    enableDisablePanBillsDetails(false);
                    enableDisablePanBillsRates(false);
                    enableDisablePanBillsCharges(false);
                    setHelpBtnsEnable(false);
               }else if(viewType.equals(ClientConstants.ACTION_STATUS[2]) || viewType.equals(ClientConstants.ACTION_STATUS[20])){
                    enableDisablePanBillsDetails(true);
                    enableDisablePanBillsRates(true);
                    enableDisablePanBillsCharges(true);
                    setHelpBtnsEnable(true);
                    btnTabNew.setEnabled(true);
                    ClientUtil.enableDisable(panCharges,false);
//                    panCharges.setEnabled(false);
                    if(rdoPenalNo.isSelected()) {
                        txtPenalRateInterest.setEnabled(false);
                        txtPenalIntrstAcHd.setEnabled(false);
                    }
                    
                }
                setButtonEnableDisable();
                enableDisableContraActHead();
                if (viewType.equals(ClientConstants.ACTION_STATUS[20])){
                   enableDisableProductId(true);
//                    btnCopy.setEnabled(false);
                }
                btnCopy.setEnabled(false);
                
            }
           if(viewType.equals("PLRentAccHd")){
               
              
                txtPenalIntrstAcHd.setText((String)hash.get(accHdId));
               observable.setTxtPenalIntrstAcHd(txtPenalIntrstAcHd.getText());
            }
            
            if(viewType.equals("GLRentAcHd")) {
               txtLockerRentAcHd.setText((String)hash.get(accHdId));
               observable.setTxtLockerRentAcHd(txtLockerRentAcHd.getText());
            }
            else if(viewType.equals("SuspAcHd")){
                txtLockerSusAcHd.setText((String)hash.get(accHdId));
               observable.setTxtLockerSusAcHd( txtLockerSusAcHd.getText());
                System.out.println("the value is"+ txtLockerSusAcHd.getText());
            }
            else if(viewType.equals("MiscAcHd")){
                txtLockerMiscAcHd.setText((String)hash.get(accHdId));
                observable.setTxtLockerMiscAcHd(txtLockerMiscAcHd.getText());
            }
            else if(viewType.equals("BrkAcHd")){
                txtLockerBrkAcHd.setText((String)hash.get(accHdId));
              observable.setTxtLockerBrkAcHd(txtLockerBrkAcHd.getText());
            }
            else if(viewType.equals("ServTaxHead")){
                txtLockerServAcHd.setText((String)hash.get(accHdId));
               observable.setTxtLockerServAcHd(txtLockerServAcHd.getText());
            }
            else if(viewType.equals("GLRentAdvAcHd")){
                txtLockerRentAdvAcHd.setText((String)hash.get(accHdId));
                observable.setTxtLockerRentAdvAcHd(txtLockerRentAdvAcHd.getText());
            }
//            else if(viewType.equals("PostAcHd")){
////                txtPostageAccountHead.setText((String)hash.get(accHdId));
//            }
//            else if(viewType.equals("ContraAcHd")){
////                txtContractAccountHead.setText((String)hash.get(accHdId));
//            }
//            else if(viewType.equals("IBRAcHd")){
////                txtIBRAccountHead.setText((String)hash.get(accHdId));
//            }
//             else if(viewType.equals("ServTaxAcHd")){
////                txtServiceTaxHd.setText((String)hash.get(accHdId));
//            }
//            else if(viewType.equals("AtPar")){
////                txtAtParLimit.setText((String)hash.get(accHdId));
//            }
//            else if(viewType.equals("OthersHead")){
////                txtOthersHead.setText((String)hash.get(accHdId));
//            }
//            else if(viewType.equals("BillsRealisedHead")){
////                txtBillsRealisedHead.setText((String)hash.get(accHdId));
//            }
//            else if(viewType.equals("TelChargesHead")){
////                txtTelChargesHead.setText((String)hash.get(accHdId));
//            }
        }
        
        //__ To Save the data in the Internal Frame...
       //update(null,null);
       setModified(true);
    }
    
    private String checkMandatory(javax.swing.JComponent component){
        return new MandatoryCheck().checkMandatory(getClass().getName(), component);
    }
    
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /* Calls the execute method of TerminalOB to do insertion or updation or deletion */
    private void saveAction(String status){
        //To check mandtoryness of the Terminal panAcHdDetails,panAcHeadDetails panel and diplay appropriate
        //error message, else proceed
        final String mandatoryMessage = checkMandatory(panBills);
        message = new StringBuffer(mandatoryMessage);
//        if(observable.getRdoContraAccountHead_Yes()){
//            if(txtContractAccountHead.getText().equals("") || txtContractAccountHead.getText().length() == 0){
//                message.append(objMandatoryRB.getString("txtContractAccountHead"));
//            }
//            if(cboTransitPeriod.getSelectedIndex()!=0)
//                if(ClientUtil.validPeriodMaxLength(txtTransitPeriod, CommonUtil.convertObjToStr(observable.getCbmTransitPeriod().getKeyForSelected()))==false){
//                    message.append(resourceBundle.getString("periodMsg"));
//                }
//        }
        
        
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }
        else if(rdoPenalYes.isSelected()==false && rdoPenalNo.isSelected()==false){
            displayAlert("please select penal to be collected Yes/No");
            return;
        }
        else if (rdoRefundYes.isSelected()==false && rdoRefundNo.isSelected()==false) {
            displayAlert("please select locker refund Yes/No");
            return;
        }
        
        
        else{
            if(rdoPenalYes.isSelected()==true) {
            
                if(txtPenalRateInterest.getText()==null || txtPenalRateInterest.getText().equals("")) {
             displayAlert("Penal rate should not be empty");   
             return;
            }
                if(txtPenalIntrstAcHd.getText()==null || txtPenalIntrstAcHd.getText().equals("")) {
             displayAlert("penal interest account head should not be empty");  
             return;
            }
            
        }
             if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW){
             if(tblBillsCharges.getRowCount()==0){
                   displayAlert("Locker product charges should not be empty");  
             return;
             }
             }
            
            observable.execute(status);
            //__ if the Action is not Falied, Reset the fields...
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                settings();
            }
        }
    }
    
    /* set the screen after the updation,insertion, deletion */
   private void settings(){
        observable.resetForm();
        ClientUtil.clearAll(this);
        enableDisablePanBillsDetails(false);
        enableDisablePanBillsRates(false);
        setButtonEnableDisable();
        observable.setResultStatus();
        setHelpBtnsEnable(false);
        observable.resetTable();
        //__ Make the Screen Closable..
        setModified(false);
    }
    
    /* Does necessary operaion when user clicks the save button */
   private void savePerformed(){
        updateOBFields();
        String action;
        if(observable.getActionType()==ClientConstants.ACTIONTYPE_NEW ||
       observable.getActionType()==ClientConstants.ACTIONTYPE_COPY){
            action=CommonConstants.TOSTATUS_INSERT;
            if(observable.isProductIdAvailable(txtProductId.getText())) {
                displayAlert(resourceBundle.getString("warningProductId"));
           } else {
                saveAction(action);
            }
            
       }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_EDIT){
            action=CommonConstants.TOSTATUS_UPDATE;
            saveAction(action);
       }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            action=CommonConstants.TOSTATUS_DELETE;
            saveAction(action);
        }
    }
    
    public static void main(String args[]){
        javax.swing.JFrame frame= new javax.swing.JFrame();
        LockerProdUI bui=new LockerProdUI();
        frame.getContentPane().add(bui);
        frame.setSize(700,400);
        frame.show();
        bui.show();
    }
    
   private void setHelpBtnsEnable(boolean flag){
        btnLockerRentAcHd.setEnabled(flag);
        btnLockerSusAcHd.setEnabled(flag);
        btnLockerMiscAcHd.setEnabled(flag);
        btnLockerBrkAcHd.setEnabled(flag);
        btnLockerServAcHd.setEnabled(flag);
        btnLockerRentAdvAcHd.setEnabled(flag);
        btnPenalAcHd.setEnabled(flag);
//        btnCommissionAccountHead.setEnabled(flag);
//        btnPostageAccountHead.setEnabled(flag);
//        btnContractAccountHead.setEnabled(flag);
//        btnIBRAccountHead.setEnabled(flag);
//        btnServiceTaxHd.setEnabled(flag);
//        btnBillsRealisedHead.setEnabled(flag);
//        btnOthersHead.setEnabled(flag);
//        btnTelChargesHead.setEnabled(flag);
    }
    
    private void enableDisableContraActHead(){
        if(observable.getActionType()== ClientConstants.ACTIONTYPE_EDIT){
//            if(rdoContraAccountHead_No.isSelected()){
//                txtContractAccountHead.setText("");
//                txtContractAccountHead.setEnabled(false);
//                btnContractAccountHead.setEnabled(false);
//                btnContractAccountHead.setEnabled(false);
//            }else{
//                txtContractAccountHead.setEnabled(true);
//                btnContractAccountHead.setEnabled(true);
//            }
//        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
//            rdoContraAccountHead_No.setEnabled(false);
//            rdoContraAccountHead_Yes.setEnabled(false);
//            btnContractAccountHead.setEnabled(false);
        }
    }
    private void enableDisableBillsCharges_SaveDelete(boolean flag){

        btnTabDelete.setEnabled(!flag);
        btnTabSave.setEnabled(!flag);
        btnTabNew.setEnabled(flag);
    }
    private void enableDisbleBillsCharges__NewSaveDeleter(boolean flag){
        btnTabNew.setEnabled(flag);
        btnTabSave.setEnabled(flag);
        btnTabDelete.setEnabled(flag);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoContraAccountHead = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPostDtdCheqAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        rdoICCInt = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRefund = new com.see.truetransact.uicomponent.CButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        rdoPenalToBeCollected = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrBills = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace34 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace35 = new com.see.truetransact.uicomponent.CLabel();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        lblSpace36 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace37 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace38 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace39 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        panBills = new com.see.truetransact.uicomponent.CPanel();
        tabBills = new com.see.truetransact.uicomponent.CTabbedPane();
        panBillsDetails = new com.see.truetransact.uicomponent.CPanel();
        panProductId = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        txtProductId = new com.see.truetransact.uicomponent.CTextField();
        lblProdDesc = new com.see.truetransact.uicomponent.CLabel();
        txtProdDesc = new com.see.truetransact.uicomponent.CTextField();
        panPeriodOfDeposit = new com.see.truetransact.uicomponent.CPanel();
        txtLength = new com.see.truetransact.uicomponent.CTextField();
        lblLength = new com.see.truetransact.uicomponent.CLabel();
        txtBreth = new com.see.truetransact.uicomponent.CTextField();
        lblBreth = new com.see.truetransact.uicomponent.CLabel();
        txtHeight = new com.see.truetransact.uicomponent.CTextField();
        lblHeight = new com.see.truetransact.uicomponent.CLabel();
        cboMetric = new com.see.truetransact.uicomponent.CComboBox();
        lblDimensions = new com.see.truetransact.uicomponent.CLabel();
        txtPenalRateInterest = new javax.swing.JTextField();
        lblPenalRateOfInterst = new com.see.truetransact.uicomponent.CLabel();
        lblPenalToBeCollected = new com.see.truetransact.uicomponent.CLabel();
        jPanel2 = new javax.swing.JPanel();
        rdoPenalYes = new javax.swing.JRadioButton();
        rdoPenalNo = new javax.swing.JRadioButton();
        panBillsAcHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        panAcHdDetails = new com.see.truetransact.uicomponent.CPanel();
        lblLockerRentAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtLockerRentAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnLockerRentAcHd = new com.see.truetransact.uicomponent.CButton();
        lblLockerSusAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtLockerSusAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnLockerSusAcHd = new com.see.truetransact.uicomponent.CButton();
        lblLockerMiscAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtLockerMiscAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnLockerMiscAcHd = new com.see.truetransact.uicomponent.CButton();
        lblLockerBrkAcHd = new com.see.truetransact.uicomponent.CLabel();
        lblLockerServAcHd = new com.see.truetransact.uicomponent.CLabel();
        btnLockerBrkAcHd = new com.see.truetransact.uicomponent.CButton();
        txtLockerBrkAcHd = new com.see.truetransact.uicomponent.CTextField();
        txtLockerServAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnLockerRentAdvAcHd = new com.see.truetransact.uicomponent.CButton();
        lblLockerRentAdvAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtLockerRentAdvAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnLockerServAcHd = new com.see.truetransact.uicomponent.CButton();
        lblPenalInterestAcHd = new com.see.truetransact.uicomponent.CLabel();
        txtPenalIntrstAcHd = new javax.swing.JTextField();
        btnPenalAcHd = new com.see.truetransact.uicomponent.CButton();
        panProductId1 = new com.see.truetransact.uicomponent.CPanel();
        lblRefund = new com.see.truetransact.uicomponent.CLabel();
        rdoRefundYes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoRefundNo = new com.see.truetransact.uicomponent.CRadioButton();
        panBillsCharges = new com.see.truetransact.uicomponent.CPanel();
        panDiscountRate1 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills4 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills5 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills6 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills7 = new com.see.truetransact.uicomponent.CPanel();
        panTransitPeriod1 = new com.see.truetransact.uicomponent.CPanel();
        panCharges1 = new com.see.truetransact.uicomponent.CPanel();
        panRemitProdCharges = new com.see.truetransact.uicomponent.CPanel();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        lblChargeType = new com.see.truetransact.uicomponent.CLabel();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        cboChargeType = new com.see.truetransact.uicomponent.CComboBox();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        tdtFromDt = new com.see.truetransact.uicomponent.CDateField();
        tdtToDt = new com.see.truetransact.uicomponent.CDateField();
        lblToDt = new com.see.truetransact.uicomponent.CLabel();
        lblFromDt = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        panRemitProdChargesButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        panAliasBrchRemittNumber = new com.see.truetransact.uicomponent.CPanel();
        sprRemitProdCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblBillsCharges = new com.see.truetransact.uicomponent.CTable();
        mbrBills = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptNew = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptSave = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(825, 600));
        setPreferredSize(new java.awt.Dimension(825, 600));

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrBills.add(btnNew);

        lblSpace34.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace34.setText("     ");
        lblSpace34.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace34.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace34);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrBills.add(btnEdit);

        lblSpace35.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace35.setText("     ");
        lblSpace35.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace35.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace35);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrBills.add(btnCopy);

        lblSpace36.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace36.setText("     ");
        lblSpace36.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace36.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace36);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrBills.add(btnDelete);

        lblSpace2.setText("     ");
        tbrBills.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrBills.add(btnSave);

        lblSpace37.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace37.setText("     ");
        lblSpace37.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace37.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace37);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrBills.add(btnCancel);

        lblSpace3.setText("     ");
        tbrBills.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrBills.add(btnAuthorize);

        lblSpace38.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace38.setText("     ");
        lblSpace38.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace38.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace38);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrBills.add(btnException);

        lblSpace39.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace39.setText("     ");
        lblSpace39.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace39.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace39);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrBills.add(btnReject);

        lblSpace4.setText("     ");
        tbrBills.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrBills.add(btnPrint);

        lblSpace40.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace40.setText("     ");
        lblSpace40.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace40.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace40);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrBills.add(btnClose);

        getContentPane().add(tbrBills, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 550);
        panStatus.add(lblStatus, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panBills.setMinimumSize(new java.awt.Dimension(670, 500));
        panBills.setPreferredSize(new java.awt.Dimension(670, 500));
        panBills.setLayout(new java.awt.GridBagLayout());

        tabBills.setMinimumSize(new java.awt.Dimension(690, 490));
        tabBills.setPreferredSize(new java.awt.Dimension(690, 490));

        panBillsDetails.setLayout(new java.awt.GridBagLayout());

        panProductId.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProductId.setMinimumSize(new java.awt.Dimension(520, 153));
        panProductId.setPreferredSize(new java.awt.Dimension(520, 153));
        panProductId.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product Id");
        lblProductId.setMaximumSize(new java.awt.Dimension(70, 16));
        lblProductId.setMinimumSize(new java.awt.Dimension(70, 16));
        lblProductId.setPreferredSize(new java.awt.Dimension(70, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(lblProductId, gridBagConstraints);

        txtProductId.setAllowAll(true);
        txtProductId.setMaximumSize(new java.awt.Dimension(100, 21));
        txtProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(txtProductId, gridBagConstraints);

        lblProdDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(lblProdDesc, gridBagConstraints);

        txtProdDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(txtProdDesc, gridBagConstraints);

        panPeriodOfDeposit.setMinimumSize(new java.awt.Dimension(300, 29));
        panPeriodOfDeposit.setPreferredSize(new java.awt.Dimension(300, 29));
        panPeriodOfDeposit.setLayout(new java.awt.GridBagLayout());

        txtLength.setAllowNumber(true);
        txtLength.setMinimumSize(new java.awt.Dimension(45, 21));
        txtLength.setPreferredSize(new java.awt.Dimension(45, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 2);
        panPeriodOfDeposit.add(txtLength, gridBagConstraints);

        lblLength.setText("L");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(lblLength, gridBagConstraints);

        txtBreth.setAllowNumber(true);
        txtBreth.setMinimumSize(new java.awt.Dimension(45, 21));
        txtBreth.setPreferredSize(new java.awt.Dimension(45, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(txtBreth, gridBagConstraints);

        lblBreth.setText("B");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(lblBreth, gridBagConstraints);

        txtHeight.setAllowNumber(true);
        txtHeight.setMinimumSize(new java.awt.Dimension(45, 21));
        txtHeight.setPreferredSize(new java.awt.Dimension(45, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(txtHeight, gridBagConstraints);

        lblHeight.setText("H");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(lblHeight, gridBagConstraints);

        cboMetric.setMinimumSize(new java.awt.Dimension(75, 21));
        cboMetric.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
        panPeriodOfDeposit.add(cboMetric, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(panPeriodOfDeposit, gridBagConstraints);

        lblDimensions.setText("Dimensions");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(lblDimensions, gridBagConstraints);

        txtPenalRateInterest.setMinimumSize(new java.awt.Dimension(100, 19));
        txtPenalRateInterest.setPreferredSize(new java.awt.Dimension(100, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(txtPenalRateInterest, gridBagConstraints);

        lblPenalRateOfInterst.setText("Penal Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(lblPenalRateOfInterst, gridBagConstraints);

        lblPenalToBeCollected.setText("Penal to be Collected");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(lblPenalToBeCollected, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        rdoPenalYes.setText("Yes");
        rdoPenalYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(rdoPenalYes, gridBagConstraints);

        rdoPenalNo.setText("No");
        rdoPenalNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPenalNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        jPanel2.add(rdoPenalNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panProductId.add(jPanel2, gridBagConstraints);

        panBillsDetails.add(panProductId, new java.awt.GridBagConstraints());

        panBillsAcHeadDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBillsAcHeadDetails.setPreferredSize(new java.awt.Dimension(415, 200));
        panBillsAcHeadDetails.setLayout(new java.awt.GridBagLayout());

        panAcHdDetails.setPreferredSize(new java.awt.Dimension(390, 190));
        panAcHdDetails.setLayout(new java.awt.GridBagLayout());

        lblLockerRentAcHd.setText("Locker Rent Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(lblLockerRentAcHd, gridBagConstraints);

        txtLockerRentAcHd.setEditable(false);
        txtLockerRentAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLockerRentAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(txtLockerRentAcHd, gridBagConstraints);

        btnLockerRentAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerRentAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerRentAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerRentAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerRentAcHd.setEnabled(false);
        btnLockerRentAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerRentAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHdDetails.add(btnLockerRentAcHd, gridBagConstraints);

        lblLockerSusAcHd.setText("Locker Suspense Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(lblLockerSusAcHd, gridBagConstraints);

        txtLockerSusAcHd.setEditable(false);
        txtLockerSusAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLockerSusAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLockerSusAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLockerSusAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(txtLockerSusAcHd, gridBagConstraints);

        btnLockerSusAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerSusAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerSusAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerSusAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerSusAcHd.setEnabled(false);
        btnLockerSusAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerSusAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHdDetails.add(btnLockerSusAcHd, gridBagConstraints);

        lblLockerMiscAcHd.setText("Misc Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(lblLockerMiscAcHd, gridBagConstraints);

        txtLockerMiscAcHd.setEditable(false);
        txtLockerMiscAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLockerMiscAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(txtLockerMiscAcHd, gridBagConstraints);

        btnLockerMiscAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerMiscAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerMiscAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerMiscAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerMiscAcHd.setEnabled(false);
        btnLockerMiscAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerMiscAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHdDetails.add(btnLockerMiscAcHd, gridBagConstraints);

        lblLockerBrkAcHd.setText("Break Open Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(lblLockerBrkAcHd, gridBagConstraints);

        lblLockerServAcHd.setText("Service Tax Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(lblLockerServAcHd, gridBagConstraints);

        btnLockerBrkAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerBrkAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerBrkAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerBrkAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerBrkAcHd.setEnabled(false);
        btnLockerBrkAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerBrkAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHdDetails.add(btnLockerBrkAcHd, gridBagConstraints);

        txtLockerBrkAcHd.setEditable(false);
        txtLockerBrkAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLockerBrkAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(txtLockerBrkAcHd, gridBagConstraints);

        txtLockerServAcHd.setEditable(false);
        txtLockerServAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLockerServAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(txtLockerServAcHd, gridBagConstraints);

        btnLockerRentAdvAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerRentAdvAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerRentAdvAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerRentAdvAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerRentAdvAcHd.setEnabled(false);
        btnLockerRentAdvAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerRentAdvAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHdDetails.add(btnLockerRentAdvAcHd, gridBagConstraints);

        lblLockerRentAdvAcHd.setText("Locker Rent Recieved in Advance Ac Hd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(lblLockerRentAdvAcHd, gridBagConstraints);

        txtLockerRentAdvAcHd.setEditable(false);
        txtLockerRentAdvAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtLockerRentAdvAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(txtLockerRentAdvAcHd, gridBagConstraints);

        btnLockerServAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLockerServAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnLockerServAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLockerServAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLockerServAcHd.setEnabled(false);
        btnLockerServAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLockerServAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHdDetails.add(btnLockerServAcHd, gridBagConstraints);

        lblPenalInterestAcHd.setText("Penal Interest Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHdDetails.add(lblPenalInterestAcHd, gridBagConstraints);

        txtPenalIntrstAcHd.setMinimumSize(new java.awt.Dimension(100, 19));
        txtPenalIntrstAcHd.setPreferredSize(new java.awt.Dimension(150, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        panAcHdDetails.add(txtPenalIntrstAcHd, gridBagConstraints);

        btnPenalAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPenalAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPenalAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPenalAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPenalAcHd.setEnabled(false);
        btnPenalAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPenalAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHdDetails.add(btnPenalAcHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 20, 0);
        panBillsAcHeadDetails.add(panAcHdDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panBillsDetails.add(panBillsAcHeadDetails, gridBagConstraints);

        panProductId1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panProductId1.setLayout(new java.awt.GridBagLayout());

        lblRefund.setText("Refund Rent on Locker Surrender");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId1.add(lblRefund, gridBagConstraints);

        rdoRefund.add(rdoRefundYes);
        rdoRefundYes.setText("Yes");
        rdoRefundYes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRefundYesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        panProductId1.add(rdoRefundYes, gridBagConstraints);

        rdoRefund.add(rdoRefundNo);
        rdoRefundNo.setText("No");
        rdoRefundNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoRefundNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panProductId1.add(rdoRefundNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        panBillsDetails.add(panProductId1, gridBagConstraints);

        tabBills.addTab("Locker Details", panBillsDetails);

        panBillsCharges.setMinimumSize(new java.awt.Dimension(500, 300));
        panBillsCharges.setPreferredSize(new java.awt.Dimension(500, 300));
        panBillsCharges.setLayout(new java.awt.GridBagLayout());

        panDiscountRate1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panDiscountRate1, gridBagConstraints);

        panOverdueRateBills4.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills4, gridBagConstraints);

        panOverdueRateBills5.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills5, gridBagConstraints);

        panOverdueRateBills6.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills6, gridBagConstraints);

        panOverdueRateBills7.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBillsCharges.add(panOverdueRateBills7, gridBagConstraints);

        panTransitPeriod1.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBillsCharges.add(panTransitPeriod1, gridBagConstraints);

        panCharges1.setMinimumSize(new java.awt.Dimension(640, 450));
        panCharges1.setPreferredSize(new java.awt.Dimension(640, 300));
        panCharges1.setLayout(new java.awt.GridBagLayout());

        panRemitProdCharges.setMinimumSize(new java.awt.Dimension(650, 300));
        panRemitProdCharges.setPreferredSize(new java.awt.Dimension(650, 350));
        panRemitProdCharges.setLayout(new java.awt.GridBagLayout());

        panCharges.setMinimumSize(new java.awt.Dimension(250, 250));
        panCharges.setPreferredSize(new java.awt.Dimension(250, 250));
        panCharges.setLayout(new java.awt.GridBagLayout());

        lblChargeType.setText("Charge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblChargeType, gridBagConstraints);

        lblAmt.setText("Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblAmt, gridBagConstraints);

        cboChargeType.setInheritsPopupMenu(true);
        cboChargeType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboChargeType.setPopupWidth(180);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboChargeType, gridBagConstraints);

        txtAmt.setAllowNumber(true);
        txtAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtAmt, gridBagConstraints);

        tdtFromDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtFromDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtFromDt, gridBagConstraints);

        tdtToDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtToDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtToDt, gridBagConstraints);

        lblToDt.setText("To Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblToDt, gridBagConstraints);

        lblFromDt.setText("From Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblFromDt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtServiceTax, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblServiceTax, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdCharges.add(panCharges, gridBagConstraints);

        panRemitProdChargesButtons.setMinimumSize(new java.awt.Dimension(217, 35));
        panRemitProdChargesButtons.setLayout(new java.awt.GridBagLayout());

        btnTabNew.setText("New");
        btnTabNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnTabNew, gridBagConstraints);

        btnTabSave.setText("Save");
        btnTabSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnTabSave, gridBagConstraints);

        btnTabDelete.setText("Delete");
        btnTabDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnTabDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdCharges.add(panRemitProdChargesButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCharges1.add(panRemitProdCharges, gridBagConstraints);

        panAliasBrchRemittNumber.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panCharges1.add(panAliasBrchRemittNumber, gridBagConstraints);

        sprRemitProdCharges.setMinimumSize(new java.awt.Dimension(650, 300));
        sprRemitProdCharges.setPreferredSize(new java.awt.Dimension(700, 300));

        tblBillsCharges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblBillsCharges.setMinimumSize(new java.awt.Dimension(450, 300));
        tblBillsCharges.setPreferredScrollableViewportSize(new java.awt.Dimension(450, 300));
        tblBillsCharges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBillsChargesMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBillsChargesMousePressed(evt);
            }
        });
        sprRemitProdCharges.setViewportView(tblBillsCharges);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges1.add(sprRemitProdCharges, gridBagConstraints);

        panBillsCharges.add(panCharges1, new java.awt.GridBagConstraints());

        tabBills.addTab("Locker Charges", panBillsCharges);

        panBills.add(tabBills, new java.awt.GridBagConstraints());

        getContentPane().add(panBills, java.awt.BorderLayout.CENTER);

        mbrBills.setName("mbrCustomer");

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess");

        mitNew.setText("New");
        mitNew.setName("mitNew");
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit");
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete");
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew");
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave");
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave");
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint");
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrBills.add(mnuProcess);

        setJMenuBar(mbrBills);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdoRefundNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRefundNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoRefundNoActionPerformed

    private void rdoPenalYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalYesActionPerformed
        // TODO add your handling code here:
         txtPenalRateInterest.setEnabled(true);
        txtPenalIntrstAcHd.setEnabled(true);
        btnPenalAcHd.setEnabled(true);
        
    }//GEN-LAST:event_rdoPenalYesActionPerformed

    private void rdoPenalNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPenalNoActionPerformed
        // TODO add your handling code here:
        txtPenalRateInterest.setText("");
        txtPenalIntrstAcHd.setText("");
        txtPenalRateInterest.setEnabled(false);
        txtPenalIntrstAcHd.setEnabled(false);
        btnPenalAcHd.setEnabled(false);
        
    }//GEN-LAST:event_rdoPenalNoActionPerformed

    private void btnPenalAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPenalAcHdActionPerformed
        // TODO add your handling code here:
         callView("PLRentAccHd");
    }//GEN-LAST:event_btnPenalAcHdActionPerformed

    private void rdoRefundYesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoRefundYesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoRefundYesActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnLockerServAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerServAcHdActionPerformed
        // TODO add your handling code here:
        callView("ServTaxHead");
    }//GEN-LAST:event_btnLockerServAcHdActionPerformed

    private void txtLockerSusAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLockerSusAcHdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLockerSusAcHdActionPerformed

    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_COPY);
        callView("Copy");
//        enableDisableProductId(false);
    }//GEN-LAST:event_btnCopyActionPerformed

    private void txtToSlabFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToSlabFocusLost
        // TODO add your handling code here:
         HashMap where = new HashMap();
         HashMap data = new HashMap();
//          if(!( txtFromSlab.getText().equals("")))
//          if(!( txtFromSlab.getText().equals(null))){
//         where.put("FROM_SLAB", txtFromSlab.getText());
//         String fromslab = (String) where.get("FROM_SLAB");
//         double frmSlb = Double.parseDouble(fromslab);
//         data.put("TO_SLAB", txtToSlab.getText());
//         String toslab = (String) data.get("TO_SLAB");
//         double toSlb = Double.parseDouble(toslab);
//         if(frmSlb >= toSlb){
//             displayAlert("From Slab should be less than To Slab");
//             txtFromSlab.setText("");
//             txtToSlab.setText("");
//         }
//       }
    }//GEN-LAST:event_txtToSlabFocusLost

    private void tdtFromDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtFromDtFocusLost
        // TODO add your handling code here:
//            ClientUtil.validateFromDate(tdtStartDt, tdtEndDt.getDateValue());
    }//GEN-LAST:event_tdtFromDtFocusLost

    private void tdtToDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtToDtFocusLost
        // TODO add your handling code here:
//         ClientUtil.validateToDate(tdtEndDt, tdtStartDt.getDateValue());
    }//GEN-LAST:event_tdtToDtFocusLost

    private void tblBillsChargesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillsChargesMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblBillsChargesMouseClicked

    private void tblBillsChargesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillsChargesMousePressed
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT  ){
             tableBillsChargeClicked=true;
            //ClientUtil.enableDisable(panNoticecharge_Amt,true);
            enableDisbleBillsCharges__NewSaveDeleter(true);
            observable.populateBillsCharge(tblBillsCharges.getSelectedRow());
          int n=  tblBillsCharges.getSelectedRow();
       String s= CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(n, 5));
       String auth="AUTHORIZED";
       
            //observable.resetBillsChargeValues();
             ClientUtil.enableDisable(panCharges,true);
            cboChargeType.setEnabled(false);
           if(s.equals(auth)){
               tdtFromDt.setEnabled(false);
               tdtToDt.setEnabled(false);
               txtAmt.setEnabled(false);
               txtServiceTax.setEnabled(false);
               btnTabDelete.setEnabled(false);
               btnTabSave.setEnabled(false);
               btnTabNew.setEnabled(true);
           }
        }else  if(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE){
             tableBillsChargeClicked=true;
            //ClientUtil.enableDisable(panNoticecharge_Amt,true);
            enableDisbleBillsCharges__NewSaveDeleter(true);
            observable.populateBillsCharge(tblBillsCharges.getSelectedRow());
            
            tdtFromDt.setEnabled(false);
               tdtToDt.setEnabled(false);
               txtAmt.setEnabled(false);
               txtServiceTax.setEnabled(false);
               btnTabDelete.setEnabled(false);
               btnTabSave.setEnabled(false);
               btnTabNew.setEnabled(true);
        }
            
            
//            cboInstrumentType.setEnabled(false);
//            cboCustCategory.setEnabled(false);
//            txtFromSlab.setEnabled(false);
        
        
        else{
                tableBillsChargeClicked=true;
    //        tableBillsChargeClicked=false;
            enableDisbleBillsCharges__NewSaveDeleter(true);
            observable.populateBillsCharge(tblBillsCharges.getSelectedRow());
            ClientUtil.enableDisable(panCharges,true);
        }
        System.out.println("action type is"+observable.getActionType());
       
    }//GEN-LAST:event_tblBillsChargesMousePressed

    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // TODO add your handling code here:
        if(tblBillsCharges. getSelectedRow()>=0){
             String cType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboChargeType.getModel())).getKeyForSelected());
            int index=tblBillsCharges.getSelectedRow();
     /** if(index>=0){
  for(int j=0;j<index;j++)
  {
      String toDate = DateUtil.getStringDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()), -1));
      if(toDate.equals(CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(j,2))) && CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(index,0)).equals(CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(j,0))))
  
      
            {
                    String PREVDATE = CommonUtil.convertObjToStr(tblBillsCharges.getValueAt( j, 1));
                    String CURRDATE = CommonUtil.convertObjToStr(tblBillsCharges.getValueAt( index-1, 1));
                   
                        yesno=0;
                        //break;
                  // }
           }
       }
  
  }*/
           // int index=tblBillsCharges.getSelectedRow();
        // String date= CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(index, 1));
         
            
//            index+=1;
           
            
            // String toDate = DateUtil.getStringDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(date)),-1));
        
      
            
          String pid=  txtProductId.getText();
          
          // HashMap hmap=new HashMap();
          // hmap.put("PROD_ID",pid);
             //java.util.List list= ClientUtil.executeQuery("getAuthorizeLockerProd", hmap);
          //  hmap=null;
           // hmap=(HashMap)list.get(0);
              
           // String astatus=CommonUtil.convertObjToStr(hmap.get("AUTHORIZE_STATUS"));
           // String r="REJECTED";
           // String n="";
            //if(astatus.equals(r) || astatus.equals(n)){
                 observable.deleteBillsCharge(index);
            for(int i=0;i<index; i++) {
              String a= CommonUtil.convertObjToStr(tblBillsCharges.getValueAt((index-(i+1)), 2));
               String toDate = CommonUtil.convertObjToStr(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()), -1));
              String s="";
              System.out.println("the vales is"+a);
               if((toDate.equals(a)) && (cType.equals(CommonUtil.convertObjToStr(tblBillsCharges.getValueAt((index-(i+1)),0))))){
               
               
                   tblBillsCharges.setValueAt(s,index-(i+1), 2);
                   
                 //break;
               }
                 //}
            }
           observable.resetBillsChargeValues();
            updateBillsCharges();
            
            
            
            
               //if(date==tblBillsCharges.getValueAt(i,2))
                   
               //if(CommonUtil.convertObjToStr(date).equals(CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(i,2))));
                   
                   
           
        }
        
        
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
    
        // TODO add your handling code here:
//        ClientUtil.enableDisable(panCharges,false);
         //String prevDate;
       
        if(cboChargeType.getSelectedIndex()==0) {
            
            displayAlert("Charge type should not be empty");
            return;
            
        }
        if(tdtFromDt.getDateValue()==null || tdtFromDt.getDateValue().equals("")) {
            
            displayAlert("From date should not be empty");
             return;
        }
        //        if(tdtToDt.getDateValue()==null || tdtToDt.getDateValue().equals(""))
        //        {
        //
        //            displayAlert("To date should not be empty");
        //             return;
        //        }
        if(txtAmt.getText()==null || txtAmt.getText().equals("")) {
            
            displayAlert("Amount should not be empty");
             return;
        }
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panCharges,true);
        }
//        enableDisableBillsCharges_SaveDelete(true);
        //String date=observable.getTdtFromDt();
         
       
      
      // java.awt.Component c= sprRemitProdCharges.getComponentAt(0,2);

         
        
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCharges);
        
        if (chargeCheck()) {
            mandatoryMessage = mandatoryMessage + resourceBundle.getString("chargeCheck");
        }
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
        observable.saveBillsCharge(tableBillsChargeClicked,tblBillsCharges.getSelectedRow());
        
       int n= tblBillsCharges.getRowCount();
   int yesno=-1;
      if(n>=2){
                for(int j=0;j<n;j++) {
       if(CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(j,2)).equals("")){
                        if(tblBillsCharges.getValueAt(j,0)==tblBillsCharges.getValueAt(n-1, 0)) {
                    String PREVDATE = CommonUtil.convertObjToStr(tblBillsCharges.getValueAt( j, 1));
                    String CURRDATE = CommonUtil.convertObjToStr(tblBillsCharges.getValueAt( n-1, 1));
                    //if (!PREVDATE.equalsIgnoreCase(CURRDATE)) { 
                        yesno=0;
                        //break;
                   //}
           }
       }
  
  }
      
      }                   
                 if(yesno == 0){                
                    for (int i=0;i<n-1;i++){      
                        
                        if (CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(i,2)).equals("")){
                            if(CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(i,0)).equals(
                        CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(n-1, 0)))) {
                            String toDate = DateUtil.getStringDate(DateUtil.addDays(DateUtil.getDateMMDDYYYY(tdtFromDt.getDateValue()), -1));
                            String fromDt = CommonUtil.convertObjToStr(tblBillsCharges.getValueAt(i,1));
                            Date fromDtFormat=DateUtil.getDateMMDDYYYY(fromDt);
                            Date toDateFormat=DateUtil.getDateMMDDYYYY(toDate);
                             if(DateUtil.dateDiff(toDateFormat,fromDtFormat)<0)
                               tblBillsCharges.setValueAt(DateUtil.getStringDate(DateUtil.addDays(fromDtFormat,0)), i, 2);
                             
                            tblBillsCharges.setValueAt(toDate, i, 2);
                            }
                            
                          
                    
                      
                                     }
                               }
                           }
                            
                          
                        

                                     //tblBillsCharges.setValueAt(toDate, i, 2);     
              
        updateBillsCharges();
        observable.resetBillsChargeValues();
        updateBillsCharges();
        ClientUtil.enableDisable(panCharges,false);
        enableDisableBillsCharges_SaveDelete(true);
        }  
    }//GEN-LAST:event_btnTabSaveActionPerformed

    private void btnTabNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabNewActionPerformed
        // TODO add your handling code here:
//        if(observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT)
            tableBillsChargeClicked = false;
//        else
//            tableBillsChargeClicked = true;
        ClientUtil.enableDisable(panCharges,true);
        tdtToDt.setEnabled(false);
        enableDisableBillsCharges_SaveDelete(false);
        observable.resetBillsChargeValues();
        // observable.resetTable();
        updateBillsCharges();
        
    }//GEN-LAST:event_btnTabNewActionPerformed
 private void updateBillsCharges(){
//        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
//        cboChargeType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmChargeType().getKeyForSelected()));
        
//        cboCustCategory.setSelectedItem(observable.getCboCustCategory());
        
        
        tdtFromDt.setDateValue((String)observable.getTdtFromDt());
        tdtToDt.setDateValue((String)observable.getTdtToDt());
//        txtFromSlab.setText((String)observable.getTxtFromSlab());
//        txtToSlab.setText((String)observable.getTxtToSlab());
        txtAmt.setText((String)observable.getTxtAmt());
        txtServiceTax.setText((String)observable.getTxtServiceTax());
//        txtFixRate.setText((String)observable.getTxtFixRate());
//        txtForEvery.setText((String)observable.getTxtForEvery());
//        cboRateType.setSelectedItem(observable.getCboRateType());
//        txtRateVal.setText((String)observable.getTxtRateVal());
//        tblBillsCharges.setModel(observable.getTblBillsCharges());
            
    }
  private void enableDisableBillRates(boolean flag){
//       lblDiscountRateOfBD.setVisible(flag);
//        lblOverdueInterestForBD.setVisible(flag);
//        lblOverdueRateCBP.setVisible(flag);
//       // lblAtParLimit.setVisible(flag);
//        lblCleanBillsPurchased.setVisible(flag);
//        lblDefaultPostage.setVisible(flag);
//        panDiscountRate.setVisible(flag);
//        panOverdueRateBills.setVisible(flag);
//        panOverdueRateBills1.setVisible(flag);
//        panOverdueRateBills2.setVisible(flag);
//        panOverdueRateBills3.setVisible(flag);
//        txtAtParLimit.setVisible(flag);
//        txtCleanBills.setVisible(flag);
    }
  
  private boolean chargeCheck(){
        // Checking for whether Charges and Percentage is containing proper value(s)
//        if ((txtFixRate == null || txtFixRate.getText().length() == 0 || CommonUtil.convertObjToDouble(txtFixRate.getText()).doubleValue() == 0) && (txtCommision == null || txtCommision.getText().length() == 0 || CommonUtil.convertObjToDouble(txtCommision.getText()).doubleValue() == 0)){
//            if ((txtForEvery == null ||txtForEvery.getText().length() == 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() == 0) && cboRateType.getSelectedIndex() <= 0 && (txtRateVal == null || txtRateVal.getText().length() == 0 || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() == 0)){
//                return true;
//            }
//        }
        return false;
    }
  private void btnLockerRentAdvAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerRentAdvAcHdActionPerformed
        // TODO add your handling code here:
         callView("GLRentAdvAcHd");
  }//GEN-LAST:event_btnLockerRentAdvAcHdActionPerformed

    private void btnLockerBrkAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerBrkAcHdActionPerformed
        // TODO add your handling code here:
        callView("BrkAcHd");
    }//GEN-LAST:event_btnLockerBrkAcHdActionPerformed

    private void btnLockerMiscAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerMiscAcHdActionPerformed
        // TODO add your handling code here:
        callView("MiscAcHd");
    }//GEN-LAST:event_btnLockerMiscAcHdActionPerformed

    private void btnLockerSusAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerSusAcHdActionPerformed
        // TODO add your handling code here:
        callView("SuspAcHd");
    }//GEN-LAST:event_btnLockerSusAcHdActionPerformed

    private void btnLockerRentAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLockerRentAcHdActionPerformed
        // TODO add your handling code here:
         callView("GLRentAcHd");
    }//GEN-LAST:event_btnLockerRentAcHdActionPerformed

    private void cboChargeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChargeTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChargeTypeActionPerformed
                    
    private void txtProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIdFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if(observable.isProductIdAvailable(txtProductId.getText())) {
                displayAlert(resourceBundle.getString("warningProductId"));
                txtProductId.setText("");
            }else{
                // else part Added by nithya on 19/05/2016
                HashMap whereMap = new HashMap();
                whereMap.put("PROD_ID", txtProductId.getText());
                List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
                System.out.println("getSBODBorrowerEligAmt : " + lst);
                if (lst != null && lst.size() > 0) {
                    HashMap existingProdIdMap = (HashMap) lst.get(0);
                    if (existingProdIdMap.containsKey("PROD_ID")) {
                        ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change");
                        txtProductId.setText("");
                    }
                }
            }
        }
    }//GEN-LAST:event_txtProductIdFocusLost
    
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
         observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //__ To Save the data in the Internal Frame...
            setModified(true);
           // tblBillsCharges.get
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
              mapParam.put(CommonConstants.MAP_NAME, "getLockerProdAuthorizeList");
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
             whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
          
           mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLockerProd");
           mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLockerProdCharges");
           
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            btnCancel.setEnabled(true);
        } else if (viewType.equals(AUTHORIZE)){
            String strWarnMsg = tabBills.isAllTabsVisited();
            if (strWarnMsg.length() > 0){
                displayAlert(strWarnMsg);
                return;
            }
            strWarnMsg = null;
            tabBills.resetVisits();
            HashMap singleAuthorizeMap = new HashMap();
            
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT,currDt.clone());
            singleAuthorizeMap.put("PROD_ID", txtProductId.getText());
            
            
            ClientUtil.execute("authorizeLockerProd", singleAuthorizeMap);
            ClientUtil.execute("authorizeLockerProdCharges", singleAuthorizeMap);
            viewType = "";
            btnCancelActionPerformed(null);
            
        }
        
    }                                                                                    
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_mitCloseActionPerformed
    
    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
        savePerformed();
    }//GEN-LAST:event_mitSaveActionPerformed
    
    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
        observable.resetForm();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(panBillsDetails, false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        setButtonEnableDisable();
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_mitCancelActionPerformed
    
    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        
    }//GEN-LAST:event_mitDeleteActionPerformed
    
    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
    }//GEN-LAST:event_mitEditActionPerformed
    
    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
        observable.resetForm();
        ClientUtil.enableDisable(panBillsDetails, true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_mitNewActionPerformed
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        savePerformed();
      //  tblBillsCharges.getSelectedRow()
//        observable.resetTable();
        //enableDisbleBillsCharges__NewSaveDeleter(false);
//        enableDisableProductId(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
    public boolean checkNumber(String value) {
        //String amtRentIn=amountRentText.getText();
        boolean incorrect = true;
        try {
            Double.parseDouble(value);
            return true;
        }
        catch(NumberFormatException nfe) {
          return false;
        }
       // return 
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.entireBillsDataRow = null;
        observable.resetForm();
        enableDisableBillRates(true);
        ClientUtil.clearAll(this);
        enableDisablePanBillsDetails(false);
        enableDisablePanBillsRates(false);
        enableDisablePanBillsCharges(false);
        enableDisbleBillsCharges__NewSaveDeleter(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        enableDisableProductId(false);
        setButtonEnableDisable();
        setHelpBtnsEnable(false);
        observable.resetTable();
         btnCopy.setEnabled(true);
        //__ Make the Screen Closable..
          // rdoRefundYes.setEnabled(false);
     //  rdoRefundNo.setEnabled(false);
        setModified(false);
     
        
        
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();//Reset all the fields in UI
        observable.resetTable();
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
       // To Disable the Table
        tblBillsCharges.setEnabled(false);
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        enableDisableProductId(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        
        observable.resetForm();
        enableDisableBillRates(true);
        enableDisablePanBillsDetails(true);
        enableDisablePanBillsRates(true);
        enableDisableBillsCharges_SaveDelete(true);
        //panBillsCharges
        //enableDisablePanBillsCharges(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        enableDisableProductId(true);
        setButtonEnableDisable();
        setHelpBtnsEnable(true);
        observable.resetBillsChargeValues();
        observable.resetTable();
        btnCopy.setEnabled(false);
        //__ To Save the data in the Internal Frame...set
        setModified(true);
        txtPenalRateInterest.setEnabled(false);
        txtPenalIntrstAcHd.setEnabled(false);
        btnPenalAcHd.setEnabled(false);
        btnTabNew.setEnabled(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnLockerBrkAcHd;
    private com.see.truetransact.uicomponent.CButton btnLockerMiscAcHd;
    private com.see.truetransact.uicomponent.CButton btnLockerRentAcHd;
    private com.see.truetransact.uicomponent.CButton btnLockerRentAdvAcHd;
    private com.see.truetransact.uicomponent.CButton btnLockerServAcHd;
    private com.see.truetransact.uicomponent.CButton btnLockerSusAcHd;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPenalAcHd;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.see.truetransact.uicomponent.CComboBox cboChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboMetric;
    private javax.swing.JPanel jPanel2;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblBreth;
    private com.see.truetransact.uicomponent.CLabel lblChargeType;
    private com.see.truetransact.uicomponent.CLabel lblDimensions;
    private com.see.truetransact.uicomponent.CLabel lblFromDt;
    private com.see.truetransact.uicomponent.CLabel lblHeight;
    private com.see.truetransact.uicomponent.CLabel lblLength;
    private com.see.truetransact.uicomponent.CLabel lblLockerBrkAcHd;
    private com.see.truetransact.uicomponent.CLabel lblLockerMiscAcHd;
    private com.see.truetransact.uicomponent.CLabel lblLockerRentAcHd;
    private com.see.truetransact.uicomponent.CLabel lblLockerRentAdvAcHd;
    private com.see.truetransact.uicomponent.CLabel lblLockerServAcHd;
    private com.see.truetransact.uicomponent.CLabel lblLockerSusAcHd;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterestAcHd;
    private com.see.truetransact.uicomponent.CLabel lblPenalRateOfInterst;
    private com.see.truetransact.uicomponent.CLabel lblPenalToBeCollected;
    private com.see.truetransact.uicomponent.CLabel lblProdDesc;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblRefund;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace34;
    private com.see.truetransact.uicomponent.CLabel lblSpace35;
    private com.see.truetransact.uicomponent.CLabel lblSpace36;
    private com.see.truetransact.uicomponent.CLabel lblSpace37;
    private com.see.truetransact.uicomponent.CLabel lblSpace38;
    private com.see.truetransact.uicomponent.CLabel lblSpace39;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblToDt;
    private com.see.truetransact.uicomponent.CMenuBar mbrBills;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAcHdDetails;
    private com.see.truetransact.uicomponent.CPanel panAliasBrchRemittNumber;
    private com.see.truetransact.uicomponent.CPanel panBills;
    private com.see.truetransact.uicomponent.CPanel panBillsAcHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panBillsCharges;
    private com.see.truetransact.uicomponent.CPanel panBillsDetails;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panCharges1;
    private com.see.truetransact.uicomponent.CPanel panDiscountRate1;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills4;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills5;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills6;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills7;
    private com.see.truetransact.uicomponent.CPanel panPeriodOfDeposit;
    private com.see.truetransact.uicomponent.CPanel panProductId;
    private com.see.truetransact.uicomponent.CPanel panProductId1;
    private com.see.truetransact.uicomponent.CPanel panRemitProdCharges;
    private com.see.truetransact.uicomponent.CPanel panRemitProdChargesButtons;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransitPeriod1;
    private com.see.truetransact.uicomponent.CButtonGroup rdoContraAccountHead;
    private com.see.truetransact.uicomponent.CButtonGroup rdoICCInt;
    private javax.swing.JRadioButton rdoPenalNo;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPenalToBeCollected;
    private javax.swing.JRadioButton rdoPenalYes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDtdCheqAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRefund;
    private com.see.truetransact.uicomponent.CRadioButton rdoRefundNo;
    private com.see.truetransact.uicomponent.CRadioButton rdoRefundYes;
    private com.see.truetransact.uicomponent.CScrollPane sprRemitProdCharges;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabBills;
    private com.see.truetransact.uicomponent.CTable tblBillsCharges;
    private javax.swing.JToolBar tbrBills;
    private com.see.truetransact.uicomponent.CDateField tdtFromDt;
    private com.see.truetransact.uicomponent.CDateField tdtToDt;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtBreth;
    private com.see.truetransact.uicomponent.CTextField txtHeight;
    private com.see.truetransact.uicomponent.CTextField txtLength;
    private com.see.truetransact.uicomponent.CTextField txtLockerBrkAcHd;
    private com.see.truetransact.uicomponent.CTextField txtLockerMiscAcHd;
    private com.see.truetransact.uicomponent.CTextField txtLockerRentAcHd;
    private com.see.truetransact.uicomponent.CTextField txtLockerRentAdvAcHd;
    private com.see.truetransact.uicomponent.CTextField txtLockerServAcHd;
    private com.see.truetransact.uicomponent.CTextField txtLockerSusAcHd;
    private javax.swing.JTextField txtPenalIntrstAcHd;
    private javax.swing.JTextField txtPenalRateInterest;
    private com.see.truetransact.uicomponent.CTextField txtProdDesc;
    private com.see.truetransact.uicomponent.CTextField txtProductId;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    // End of variables declaration//GEN-END:variables
    
}
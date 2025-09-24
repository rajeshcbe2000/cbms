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

package com.see.truetransact.ui.product.bills;

import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.product.bills.BillsOB;
import com.see.truetransact.ui.product.bills.BillsMRB;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientutil.ComboBoxModel;
import java.util.List;
import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
/**
 *
 * @author  Ashok
 */

public class BillsUI extends com.see.truetransact.uicomponent.CInternalFrame implements UIMandatoryField,Observer{
    
    //    private BillsRB resourceBundle = new BillsRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.bills.BillsRB", ProxyParameters.LANGUAGE);
    
    private HashMap mandatoryMap;
    private BillsOB observable;
    private BillsMRB objMandatoryRB;
    private String viewType="";
    final String AUTHORIZE="Authorize";
    private StringBuffer message;
    boolean tableBillsChargeClicked=false;
    private Date currDt = null;
    /** Creates new form BillsUI */
    public BillsUI() {
        initSettings();
        tabBills.resetVisits();
    }
    
    /* Set the inital settings of BillsUI    */
    private void initSettings(){
        initComponents();
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMandatoryHashMap();
        setMaximumLength();
        //setHelpMessage();
        currDt = ClientUtil.getCurrentDate();
        enableDisablePanBillsDetails(false);
        enableDisablePanBillsRates(false);
        enableDisablePanBillsCharges(false);
        //        ClientUtil.enableDisable(panBillsDetails, false);
        lblBaseCurrency.setVisible(false);
        cboBaseCurrency.setVisible(false);
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
        
    }
    private void enableDisableProductId(boolean flag) {
        txtProductId.setEditable(flag);
        txtProductId.setEnabled(flag);
    }
    private void setMaximumLength() {
        txtProductId.setMaxLength(16);
        txtProdDesc.setMaxLength(32);
        txtDiscountRateBills.setMaxLength(5);
        txtDiscountRateBills.setValidation(new PercentageValidation());
        txtOverdueRateBills.setMaxLength(5);
        txtOverdueRateBills.setValidation(new PercentageValidation());
        txtRateForCBP.setMaxLength(5);
        txtRateForCBP.setValidation(new PercentageValidation());
        txtAtParLimit.setMaxLength(16);
        txtAtParLimit.setValidation(new NumericValidation());
        txtCleanBills.setMaxLength(5);
        txtCleanBills.setValidation(new PercentageValidation());
        txtTransitPeriod.setMaxLength(5);
        txtTransitPeriod.setValidation(new NumericValidation());
        txtIntDays.setMaxLength(5);
        txtIntDays.setValidation(new NumericValidation());
        txtDefaultPostage.setMaxLength(8);
        txtDefaultPostage.setValidation(new PercentageValidation());
        txtRateForDelay.setMaxLength(8);
        txtRateForDelay.setValidation(new PercentageValidation());
        txtFromSlab.setValidation(new CurrencyValidation(14,2));
        txtToSlab.setValidation(new CurrencyValidation(14,2));
        txtCommision.setValidation(new CurrencyValidation(14,2));
        txtServiceTax.setValidation(new CurrencyValidation(14,2));
        txtFixRate.setValidation(new CurrencyValidation(14,2));
        txtRateVal.setValidation(new CurrencyValidation(14,2));
        txtForEvery.setValidation(new CurrencyValidation(14,2));
        txtCommision.setValidation(new PercentageValidation());
        txtServiceTax.setValidation(new PercentageValidation());
        
    }
    private void enableDisablePanBillsDetails(boolean flag) {
        ClientUtil.enableDisable(panBillsDetails, flag);
    }
    private void enableDisablePanBillsRates(boolean flag) {
        ClientUtil.enableDisable(panRate, flag);
    }
    private void enableDisablePanBillsCharges(boolean flag) {
        ClientUtil.enableDisable(panCharges, flag);
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnBillsRealisedHead.setName("btnBillsRealisedHead");
        btnCancel.setName("btnCancel");
        btnChargesAccountHead.setName("btnChargesAccountHead");
        btnClose.setName("btnClose");
        btnCommissionAccountHead.setName("btnCommissionAccountHead");
        btnContractCrAccountHead.setName("btnContractCrAccountHead");
        btnContractDrAccountHead.setName("btnContractDrAccountHead");
        //added by rishad
        btnDrBkChargeAccountHead.setName("btnDrBkChargeAccountHead");
        btnMisBkChargeAccountHead.setName("btnMisBkChargeAccountHead");
        btnDDAccountHead.setName("btnDDAccountHead");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnGLAccountHead.setName("btnGLAccountHead");
        btnIBRAccountHead.setName("btnIBRAccountHead");
        btnServiceTaxHd.setName("btnServiceTaxHd");
        btnInterestAccountHead.setName("btnInterestAccountHead");
        btnMarginAccountHead.setName("btnMarginAccountHead");
        btnNew.setName("btnNew");
        btnOthersHead.setName("btnOthersHead");
        btnPostageAccountHead.setName("btnPostageAccountHead");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnTelChargesHead.setName("btnTelChargesHead");
        tabBills.setName("tabBills");
        cboBaseCurrency.setName("cboBaseCurrency");
        cboOperatesLike.setName("cboOperatesLike");
        cboRegType.setName("cboRegType");
        cboSubRegType.setName("cboSubRegType");
        cboTransitPeriod.setName("cboTransitPeriod");
        cboIntDays.setName("cboIntDays");
        btnTabNew.setName("btnTabNew");
        btnTabSave.setName("btnTabSave");
        btnTabDelete.setName("btnTabDelete");
        cboInstrumentType.setName("cboInstrumentType");
        cboChargeType.setName("cboChargeType");
        cboCustCategory.setName("cboCustCategory");
        cboRateType.setName("cboRateType");
        lblAtParLimit.setName("lblAtParLimit");
        lblBaseCurrency.setName("lblBaseCurrency");
        lblBillsRealisedHD.setName("lblBillsRealisedHD");
        lblChargesAccountHead.setName("lblChargesAccountHead");
        lblCleanBillsPurchased.setName("lblCleanBillsPurchased");
        lblCleanBills_Per.setName("lblCleanBills_Per");
        lblCommissionAccountHead.setName("lblCommissionAccountHead");
        lblContraAccountHead.setName("lblContraAccountHead");
        lblContractCrAccountHead.setName("lblContractCrAccountHead");
        lblContractDrAccountHead.setName("lblContractDrAccountHead");
        lblDDAccountHead.setName("lblDDAccountHead");
        lblDefaultPostage.setName("lblDefaultPostage");
        lblDefaultPostage_Per.setName("lblDefaultPostage_Per");
        lblIntICC.setName("lblIntICC");
        lblDiscountRateBills_Per.setName("lblDiscountRateBills_Per");
        lblDiscountRateOfBD.setName("lblDiscountRateOfBD");
        lblGLAccountHead.setName("lblGLAccountHead");
        lblIBRAccountHead.setName("lblIBRAccountHead");
        lblInterestAccountHead.setName("lblInterestAccountHead");
        lblMarginAccountHead.setName("lblMarginAccountHead");
        lblCollectCommFrmCustomer.setName("lblCollectCommFrmCustomer");
        lblMsg.setName("lblMsg");
        lblOperatesLike.setName("lblOperatesLike");
        lblOthersHead.setName("lblOthersHead");
        lblOverdueInterestForBD.setName("lblOverdueInterestForBD");
        lblOverdueRateBills_Per.setName("lblOverdueRateBills_Per");
        lblOverdueRateCBP.setName("lblOverdueRateCBP");
        lblPostDtdCheqAllowed.setName("lblPostDtdCheqAllowed");
        lblPostageAccountHead.setName("lblPostageAccountHead");
        lblProdDesc.setName("lblProdDesc");
        lblProductId.setName("lblProductId");
        lblRateForCBP_Per.setName("lblRateForCBP_Per");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStateCapMetroOthers.setName("lblStateCapMetroOthers");
        lblStatus.setName("lblStatus");
        lblTelChargesHead.setName("lblTelChargesHead");
        lblTransitPeriod.setName("lblTransitPeriod");
        lblInstrument.setName("lblInstrument");
        lblChargeType.setName("lblChargeType");
        lblCustCategory.setName("lblCustCategory");
        lblFromSlab.setName("lblFromSlab");
        lblToSlab.setName("lblToSlab");
        lblFixedRate.setName("lblFixedRate");
        lblForEvery.setName("lblForEvery");
        lblCommision.setName("lblCommision");
        lblServiceTax.setName("lblServiceTax");
        mbrBills.setName("mbrBills");
        panIntICC.setName("panIntICC");
        panAcHdDetails.setName("panAcHdDetails");
        panAcHeadDetails.setName("panAcHeadDetails");
        panBillsAcHeadDetails.setName("panBillsAcHeadDetails");
        panBillsDetails.setName("panBillsDetails");
        panDiscountRate.setName("panDiscountRate");
        panOverdueRateBills.setName("panOverdueRateBills");
        panOverdueRateBills1.setName("panOverdueRateBills1");
        panOverdueRateBills2.setName("panOverdueRateBills2");
        panOverdueRateBills3.setName("panOverdueRateBills3");
        panProductId.setName("panProductId");
        panRate.setName("panRate");
        panBillsCharges.setName("panBillsCharges");
        panCharges.setName("panCharges");
        //        panForEvery.setName("panForEvery");
        panStatus.setName("panStatus");
        panTransitPeriod.setName("panTransitPeriod");
        panIntDays.setName("panIntDays");
        panrdoContraAccountHead.setName("panrdoContraAccountHead");
        panrdoPostDtdCheqAllowed.setName("panrdoPostDtdCheqAllowed");
        rdoContraAccountHead_No.setName("rdoContraAccountHead_No");
        rdoContraAccountHead_Yes.setName("rdoContraAccountHead_Yes");
        rdoPostDtdCheqAllowed_No.setName("rdoPostDtdCheqAllowed_No");
        rdoPostDtdCheqAllowed_Yes.setName("rdoPostDtdCheqAllowed_Yes");
        cRadio_ICC_Yes.setName("cRadio_ICC_Yes");
        cRadio_ICC_No.setName("cRadio_ICC_No");
        sptBills_Vert.setName("sptBills_Vert");
        txtAtParLimit.setName("txtAtParLimit");
        txtBillsRealisedHead.setName("txtBillsRealisedHead");
        txtChargesAccountHead.setName("txtChargesAccountHead");
        txtCleanBills.setName("txtCleanBills");
        txtCommissionAccountHead.setName("txtCommissionAccountHead");
        txtContractCrAccountHead.setName("txtContractCrAccountHead");
        txtContractDrAccountHead.setName("txtContractDrAccountHead");
        txtDDAccountHead.setName("txtDDAccountHead");
        txtDefaultPostage.setName("txtDefaultPostage");
        txtRateForDelay.setName("txtRateForDelay");
        txtDiscountRateBills.setName("txtDiscountRateBills");
        txtGLAccountHead.setName("txtGLAccountHead");
        txtIBRAccountHead.setName("txtIBRAccountHead");
        txtServiceTaxHd.setName("txtServiceTaxHd");
        txtInterestAccountHead.setName("txtInterestAccountHead");
        txtMarginAccountHead.setName("txtMarginAccountHead");
        txtOthersHead.setName("txtOthersHead");
        txtOverdueRateBills.setName("txtOverdueRateBills");
        txtPostageAccountHead.setName("txtPostageAccountHead");
        txtProdDesc.setName("txtProdDesc");
        txtProductId.setName("txtProductId");
        txtRateForCBP.setName("txtRateForCBP");
        txtTelChargesHead.setName("txtTelChargesHead");
        txtTransitPeriod.setName("txtTransitPeriod");
        txtIntDays.setName("txtIntDays");
        txtFromSlab.setName("txtFromSlab");
        txtToSlab.setName("txtToSlab");
        txtCommision.setName("txtCommision");
        txtServiceTax.setName("txtServiceTax");
        txtFixRate.setName("txtFixRate");
        txtRateVal.setName("txtRateVal");
        txtForEvery.setName("txtForEvery");
        tblBillsCharges.setName("tblBillsCharges");
        
        
    }
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblMarginAccountHead.setText(resourceBundle.getString("lblMarginAccountHead"));
        lblCollectCommFrmCustomer.setText(resourceBundle.getString("lblCollectCommFrmCustomer"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblPostDtdCheqAllowed.setText(resourceBundle.getString("lblPostDtdCheqAllowed"));
        rdoPostDtdCheqAllowed_Yes.setText(resourceBundle.getString("rdoPostDtdCheqAllowed_Yes"));
        lblChargesAccountHead.setText(resourceBundle.getString("lblChargesAccountHead"));
        rdoContraAccountHead_No.setText(resourceBundle.getString("rdoContraAccountHead_No"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnDDAccountHead.setText(resourceBundle.getString("btnDDAccountHead"));
        lblCommissionAccountHead.setText(resourceBundle.getString("lblCommissionAccountHead"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblDDAccountHead.setText(resourceBundle.getString("lblDDAccountHead"));
        btnGLAccountHead.setText(resourceBundle.getString("btnGLAccountHead"));
        btnInterestAccountHead.setText(resourceBundle.getString("btnInterestAccountHead"));
        lblTransitPeriod.setText(resourceBundle.getString("lblTransitPeriod"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStateCapMetroOthers.setText(resourceBundle.getString("lblStateCapMetroOthers"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        btnContractCrAccountHead.setText(resourceBundle.getString("btnContractCrAccountHead"));
        btnContractDrAccountHead.setText(resourceBundle.getString("btnContractDrAccountHead"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblBaseCurrency.setText(resourceBundle.getString("lblBaseCurrency"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnMarginAccountHead.setText(resourceBundle.getString("btnMarginAccountHead"));
        btnDrBkChargeAccountHead.setText(resourceBundle.getString("btnDrBkChargeAccountHead"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblPostageAccountHead.setText(resourceBundle.getString("lblPostageAccountHead"));
        btnChargesAccountHead.setText(resourceBundle.getString("btnChargesAccountHead"));
        rdoPostDtdCheqAllowed_No.setText(resourceBundle.getString("rdoPostDtdCheqAllowed_No"));
        lblIBRAccountHead.setText(resourceBundle.getString("lblIBRAccountHead"));
        btnPostageAccountHead.setText(resourceBundle.getString("btnPostageAccountHead"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblInterestAccountHead.setText(resourceBundle.getString("lblInterestAccountHead"));
        rdoContraAccountHead_Yes.setText(resourceBundle.getString("rdoContraAccountHead_Yes"));
        cRadio_ICC_Yes.setText(resourceBundle.getString("cRadio_ICC_Yes"));
        cRadio_ICC_No.setText(resourceBundle.getString("cRadio_ICC_No"));
        lblContraAccountHead.setText(resourceBundle.getString("lblContraAccountHead"));
        btnCommissionAccountHead.setText(resourceBundle.getString("btnCommissionAccountHead"));
        lblAtParLimit.setText(resourceBundle.getString("lblAtParLimit"));
        lblGLAccountHead.setText(resourceBundle.getString("lblGLAccountHead"));
        btnIBRAccountHead.setText(resourceBundle.getString("btnIBRAccountHead"));
        btnServiceTaxHd.setText(resourceBundle.getString("btnServiceTaxHd"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblContractCrAccountHead.setText(resourceBundle.getString("lblContractCrAccountHead"));
        lblContractDrAccountHead.setText(resourceBundle.getString("lblContractDrAccountHead"));
        lblInstrument.setText(resourceBundle.getString("lblInstrument"));
        lblChargeType.setText(resourceBundle.getString("lblChargeType"));
        lblCustCategory.setText(resourceBundle.getString("lblCustCategory"));
        
        lblFromSlab.setText(resourceBundle.getString("lblFromSlab"));
        lblToSlab.setText(resourceBundle.getString("lblToSlab"));
        lblCommision.setText(resourceBundle.getString("lblCommision"));
        lblServiceTax.setText(resourceBundle.getString("lblServiceTax"));
        lblFixedRate.setText(resourceBundle.getString("lblFixedRate"));
        lblForEvery.setText(resourceBundle.getString("lblForEvery"));
    }
    
    /* To remove the radio buttons */
    private void removeRadioButtons(){
        rdoContraAccountHead.remove(rdoContraAccountHead_Yes);
        rdoContraAccountHead.remove(rdoContraAccountHead_No);
        rdoICCInt.remove(cRadio_ICC_Yes);
        rdoICCInt.remove(cRadio_ICC_No);
        rdoPostDtdCheqAllowed.remove(rdoPostDtdCheqAllowed_No);
        rdoPostDtdCheqAllowed.remove(rdoPostDtdCheqAllowed_Yes);
        rdoCreditOtherBankComm.remove(rdoGlAcHd);
        rdoCreditOtherBankComm.remove(rdoInvestmentAcHd);
        
    }
    
    /* To remove the radio buttons */
    private void addRadioButtons(){
        rdoContraAccountHead = new CButtonGroup();
        rdoContraAccountHead.add(rdoContraAccountHead_Yes);
        rdoContraAccountHead.add(rdoContraAccountHead_No);
        
        rdoPostDtdCheqAllowed = new CButtonGroup();
        rdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_No);
        rdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_Yes);
        
        rdoICCInt = new CButtonGroup();
        rdoICCInt.add(cRadio_ICC_Yes);
        rdoICCInt.add(cRadio_ICC_No);
        
        rdoCreditOtherBankComm=new CButtonGroup();
        rdoCreditOtherBankComm.add(rdoGlAcHd);
        rdoCreditOtherBankComm.add(rdoInvestmentAcHd);
        
    }
    public String getAccHdDesc(String accHdId)
    {
        //System.out.println("accHdIdaccHdIdaccHdIdaccHdId"+accHdId);
        HashMap map1= new HashMap();
        map1.put("ACCHD_ID",accHdId);
        List list1= ClientUtil.executeQuery("getSelectAcchdDesc",map1);
        if(!list1.isEmpty())
        {
            HashMap map2= new HashMap();
            map2= (HashMap) list1.get(0);
            String accHdDesc= map2.get("AC_HD_DESC").toString();
            return accHdDesc;
        }
        else
        {
            return "";
        }
    }
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtProductId.setText(observable.getTxtProductId());
        //		cboBaseCurrency.setSelectedItem(observable.getCboBaseCurrency());
        cboOperatesLike.setSelectedItem(observable.getCboOperatesLike());
        cboRegType.setSelectedItem(observable.getCboRegType());
        cboSubRegType.setSelectedItem(observable.getCboSubRegType());
        txtProdDesc.setText(observable.getTxtProdDesc());
        txtGLAccountHead.setText(observable.getTxtGLAccountHead());
        if(!txtGLAccountHead.getText().equals(""))
        {
            btnGLAccountHead.setToolTipText(getAccHdDesc(observable.getTxtGLAccountHead()));
        }
        txtInterestAccountHead.setText(observable.getTxtInterestAccountHead());
         if(!txtInterestAccountHead.getText().equals(""))
        {
            btnInterestAccountHead.setToolTipText(getAccHdDesc(observable.getTxtInterestAccountHead()));
        }
        txtChargesAccountHead.setText(observable.getTxtChargesAccountHead());
         if(!txtChargesAccountHead.getText().equals(""))
        {
            btnChargesAccountHead.setToolTipText(getAccHdDesc(observable.getTxtChargesAccountHead()));
        }
        rdoContraAccountHead_Yes.setSelected(observable.getRdoContraAccountHead_Yes());
        rdoContraAccountHead_No.setSelected(observable.getRdoContraAccountHead_No());
        cRadio_ICC_Yes.setSelected(observable.isCRadio_ICC_Yes());
        cRadio_ICC_No.setSelected(observable.isCRadio_ICC_No());
        rdoPostDtdCheqAllowed_Yes.setSelected(observable.getRdoPostDtdCheqAllowed_Yes());
        rdoPostDtdCheqAllowed_No.setSelected(observable.getRdoPostDtdCheqAllowed_No());
        txtDDAccountHead.setText(observable.getTxtDDAccountHead());
        if(!txtDDAccountHead.getText().equals(""))
        {
            btnDDAccountHead.setToolTipText(getAccHdDesc(observable.getTxtDDAccountHead()));
        }
        txtBillsRealisedHead.setText(observable.getTxtBillsRealisedHead());
        if(!txtBillsRealisedHead.getText().equals(""))
        {
            btnBillsRealisedHead.setToolTipText(getAccHdDesc(observable.getTxtBillsRealisedHead()));
        }
        txtMarginAccountHead.setText(observable.getTxtMarginAccountHead());
        if(!txtMarginAccountHead.getText().equals(""))
        {
            btnMarginAccountHead.setToolTipText(getAccHdDesc(observable.getTxtMarginAccountHead()));
        }
        //added by rishad
        txtDrBkChargeAccountHead.setText(observable.getTxtDrBkChargeAccountHead());
        txtMisBkChargeAccountHead.setText(observable.getTxtMisBkChargeAccountHead());
        txtCommissionAccountHead.setText(observable.getTxtCommissionAccountHead());
        if(!txtCommissionAccountHead.getText().equals(""))
        {
            btnCommissionAccountHead.setToolTipText(getAccHdDesc(observable.getTxtCommissionAccountHead()));
        }
        txtPostageAccountHead.setText(observable.getTxtPostageAccountHead());
        if(!txtPostageAccountHead.getText().equals(""))
        {
            btnPostageAccountHead.setToolTipText(getAccHdDesc(observable.getTxtPostageAccountHead()));
        }
        txtContractCrAccountHead.setText(observable.getTxtContraCrAccountHead());
         if(!txtContractCrAccountHead.getText().equals(""))
        {
            btnContractCrAccountHead.setToolTipText(getAccHdDesc(observable.getTxtContraCrAccountHead()));
        }
        txtContractDrAccountHead.setText(observable.getTxtContraDrAccountHead());
         if(!txtContractDrAccountHead.getText().equals(""))
        {
            btnContractDrAccountHead.setToolTipText(getAccHdDesc(observable.getTxtContraDrAccountHead()));
        }
       
        txtOBCCommAcHd.setText(observable.getTxtOBCCommAcHd());
        if(!txtOBCCommAcHd.getText().equals(""))
        {
            btnOBCCommAcHd.setToolTipText(getAccHdDesc(observable.getTxtOBCCommAcHd()));
        }
        
        txtIBRAccountHead.setText(observable.getTxtIBRAccountHead());
        if(!txtIBRAccountHead.getText().equals(""))
        {
            btnIBRAccountHead.setToolTipText(getAccHdDesc(observable.getTxtIBRAccountHead()));
        }
        txtServiceTaxHd.setText(observable.getTxtServiceTaxHd());
         if(!txtServiceTaxHd.getText().equals(""))
        {
            btnServiceTaxHd.setToolTipText(getAccHdDesc(observable.getTxtServiceTaxHd()));
        }
        txtOthersHead.setText(observable.getTxtOthersHead());
        if(!txtOthersHead.getText().equals(""))
        {
            btnOthersHead.setToolTipText(getAccHdDesc(observable.getTxtOthersHead()));
        }
        txtTelChargesHead.setText(observable.getTxtTelChargesHead());
        if(!txtTelChargesHead.getText().equals(""))
        {
            btnTelChargesHead.setToolTipText(getAccHdDesc(observable.getTxtTelChargesHead()));
        }
        txtBankChargesAcHd.setText(observable.getTxtBankChargesAcHd());
        if(!txtBankChargesAcHd.getText().equals(""))
        {
            btnBankChargesAcHd.setToolTipText(getAccHdDesc(observable.getTxtBankChargesAcHd()));
        }
         txtDebitBankChargesAcHd.setText(observable.getTxtDebitBankChargesAcHd());
        if(!txtDebitBankChargesAcHd.getText().equals(""))
        {
            btnDebitBankChargesAcHd.setToolTipText(getAccHdDesc(observable.getTxtDebitBankChargesAcHd()));
        }
        txtAtParLimit.setText(observable.getTxtAtParLimit());
        txtDiscountRateBills.setText(observable.getTxtDiscountRateBills());
        txtDefaultPostage.setText(observable.getTxtDefaultPostage());
        txtRateForDelay.setText(observable.getTxtRateForDelay());
        txtOverdueRateBills.setText(observable.getTxtOverdueRateBills());
        txtRateForCBP.setText(observable.getTxtRateForCBP());
        txtCleanBills.setText(observable.getTxtCleanBills());
        txtTransitPeriod.setText(observable.getTxtTransitPeriod());
        txtIntDays.setText(observable.getTxtIntDays());
        cboTransitPeriod.setSelectedItem(observable.getCboTransitPeriod());
        cboIntDays.setSelectedItem(observable.getCboIntDays());
        txtFromSlab.setText(observable.getTxtFromSlab());
        txtToSlab.setText(observable.getTxtToSlab());
        txtCommision.setText(observable.getTxtCommision());
        txtServiceTax.setText(observable.getTxtServiceTax());
        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
        cboCustCategory.setSelectedItem(observable.getCboCustCategory());
        cboRateType.setSelectedItem(observable.getCboRateType());
        txtFixRate.setText(observable.getTxtFixRate());
        txtForEvery.setText(observable.getTxtForEvery());
        txtRateVal.setText(observable.getTxtRateVal());
        tblBillsCharges.setModel(observable.getTblBillsCharges());
        tdtStartDt.setDateValue(observable.getTdtStartDt());
        tdtEndDt.setDateValue(observable.getTdtEndDt());
        String  commisionFrmCust = CommonUtil.convertObjToStr(observable.getChkCollectCommFrmCustomer());
        if(commisionFrmCust.length()>0 && commisionFrmCust.equals("Y")){
            chkCollectCommFrmCustomer.setSelected(true);
           
        }
       
        else{
            chkCollectCommFrmCustomer.setSelected(false);
           
        }
        if(observable.isRdoGlAcHd()==true){
            rdoGlAcHd.setSelected(true);
             rdoInvestmentAcHd.setSelected(false);
        }else{
             rdoGlAcHd.setSelected(false);
             rdoInvestmentAcHd.setSelected(true);
        }
        addRadioButtons();
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtProductId(txtProductId.getText());
        //		observable.setCboBaseCurrency((String) cboBaseCurrency.getSelectedItem());
        observable.setCboOperatesLike((String) cboOperatesLike.getSelectedItem());
        observable.setCboRegType((String) cboRegType.getSelectedItem());
        observable.setCboSubRegType((String) cboSubRegType.getSelectedItem());
        observable.setTxtProdDesc(txtProdDesc.getText());
        observable.setTxtGLAccountHead(txtGLAccountHead.getText());
        observable.setTxtInterestAccountHead(txtInterestAccountHead.getText());
        observable.setTxtChargesAccountHead(txtChargesAccountHead.getText());
        observable.setRdoContraAccountHead_Yes(rdoContraAccountHead_Yes.isSelected());
        observable.setRdoContraAccountHead_No(rdoContraAccountHead_No.isSelected());
        observable.setCRadio_ICC_No(cRadio_ICC_No.isSelected());
        observable.setCRadio_ICC_Yes(cRadio_ICC_Yes.isSelected());
        observable.setRdoPostDtdCheqAllowed_Yes(rdoPostDtdCheqAllowed_Yes.isSelected());
        observable.setRdoPostDtdCheqAllowed_No(rdoPostDtdCheqAllowed_No.isSelected());
        observable.setTxtDDAccountHead(txtDDAccountHead.getText());
        observable.setTxtBillsRealisedHead(txtBillsRealisedHead.getText());
        observable.setTxtMarginAccountHead(txtMarginAccountHead.getText());
        observable.setTxtCommissionAccountHead(txtCommissionAccountHead.getText());
        //added by rishad
        observable.setTxtDrBkChargeAccountHead(txtDrBkChargeAccountHead.getText());
        observable.setTxtMisBkChargeAccountHead(txtMisBkChargeAccountHead.getText());
        observable.setTxtPostageAccountHead(txtPostageAccountHead.getText());
        observable.setTxtContraCrAccountHead(txtContractCrAccountHead.getText());
        observable.setTxtContraDrAccountHead(txtContractDrAccountHead.getText());
        
        observable.setTxtOBCCommAcHd(txtOBCCommAcHd.getText());
        observable.setTxtIBRAccountHead(txtIBRAccountHead.getText());
        observable.setTxtServiceTaxHd(txtServiceTaxHd.getText());
        observable.setTxtOthersHead(txtOthersHead.getText());
        observable.setTxtTelChargesHead(txtTelChargesHead.getText());
        observable.setTxtAtParLimit(txtAtParLimit.getText());
        observable.setTxtDiscountRateBills(txtDiscountRateBills.getText());
        observable.setTxtDefaultPostage(txtDefaultPostage.getText());
        observable.setTxtRateForDelay(txtRateForDelay.getText());
        observable.setTxtOverdueRateBills(txtOverdueRateBills.getText());
        observable.setTxtRateForCBP(txtRateForCBP.getText());
        observable.setTxtCleanBills(txtCleanBills.getText());
        observable.setTxtTransitPeriod(txtTransitPeriod.getText());
        observable.setTxtIntDays(txtIntDays.getText());
        observable.setCboTransitPeriod((String) cboTransitPeriod.getSelectedItem());
        observable.setCboIntDays((String) cboIntDays.getSelectedItem());
        observable.setCboInstrumentType((String) cboInstrumentType.getSelectedItem());
        observable.setCboChargeType((String) cboChargeType.getSelectedItem());
        observable.setCboCustCategory((String) cboCustCategory.getSelectedItem());
        observable.setTdtStartDt(tdtStartDt.getDateValue());
        observable.setTdtEndDt(tdtEndDt.getDateValue());
        observable.setTxtFromSlab(txtFromSlab.getText());
        observable.setTxtToSlab(txtToSlab.getText());
        observable.setTxtCommision(txtCommision.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setTxtFixRate(txtFixRate.getText());
        observable.setTxtForEvery(txtForEvery.getText());
        observable.setCboRateType((String) cboRateType.getSelectedItem());
        observable.setTxtRateVal(txtRateVal.getText());
        observable.setTblBillsCharges((com.see.truetransact.clientutil.EnhancedTableModel)tblBillsCharges.getModel());
        observable.setTxtBankChargesAcHd(txtBankChargesAcHd.getText());
        observable.setTxtDebitBankChargesAcHd(txtDebitBankChargesAcHd.getText());
        if(chkCollectCommFrmCustomer.isSelected()==true){
            observable.setChkCollectCommFrmCustomer(CommonUtil.convertObjToStr("Y"));
       
        }
        else
            observable.setChkCollectCommFrmCustomer(CommonUtil.convertObjToStr("N"));
        if(rdoGlAcHd.isSelected()){
        observable.setRdoGlAcHd(true);
        observable.setRdoInvestmentAcHd(false);
        }else{
        observable.setRdoInvestmentAcHd(true);
         observable.setRdoGlAcHd(false);
        }
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductId", new Boolean(true));
        mandatoryMap.put("cboBaseCurrency", new Boolean(true));
        mandatoryMap.put("cboOperatesLike", new Boolean(true));
        mandatoryMap.put("cboSubRegType", new Boolean(true));
        mandatoryMap.put("cboRegType", new Boolean(true));
        mandatoryMap.put("txtProdDesc", new Boolean(true));
        mandatoryMap.put("txtGLAccountHead", new Boolean(true));
        mandatoryMap.put("txtInterestAccountHead", new Boolean(true));
        mandatoryMap.put("txtChargesAccountHead", new Boolean(true));
        mandatoryMap.put("rdoContraAccountHead_Yes", new Boolean(true));
        mandatoryMap.put("rdoPostDtdCheqAllowed_Yes", new Boolean(true));
        mandatoryMap.put("txtDDAccountHead", new Boolean(true));
        mandatoryMap.put("txtBillsRealisedHead", new Boolean(true));
        mandatoryMap.put("txtMarginAccountHead", new Boolean(true));
        mandatoryMap.put("txtCommissionAccountHead", new Boolean(true));
        mandatoryMap.put("txtDrBkChargeAccountHead", new Boolean(true));
        mandatoryMap.put("txtMisBkChargeAccountHead", new Boolean(true));
        mandatoryMap.put("txtPostageAccountHead", new Boolean(true));
        mandatoryMap.put("txtContractCrAccountHead", new Boolean(true));
        mandatoryMap.put("txtContractDrAccountHead", new Boolean(true));
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
        objMandatoryRB = new BillsMRB();
        txtProductId.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProductId"));
        cboBaseCurrency.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBaseCurrency"));
        cboOperatesLike.setHelpMessage(lblMsg, objMandatoryRB.getString("cboOperatesLike"));
        cboRegType.setHelpMessage(lblMsg,  objMandatoryRB.getString("cboRegType"));
        cboSubRegType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubRegType"));
        txtProdDesc.setHelpMessage(lblMsg, objMandatoryRB.getString("txtProdDesc"));
        txtGLAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGLAccountHead"));
        txtInterestAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtInterestAccountHead"));
        txtChargesAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtChargesAccountHead"));
        rdoContraAccountHead_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoContraAccountHead_Yes"));
        rdoPostDtdCheqAllowed_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoPostDtdCheqAllowed_Yes"));
        cRadio_ICC_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("cRadio_ICC_Yes"));
        txtDDAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDDAccountHead"));
        txtBillsRealisedHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBillsRealisedHead"));
        txtMarginAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMarginAccountHead"));
        txtCommissionAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommissionAccountHead"));
         txtDrBkChargeAccountHead.setHelpMessage(lblMsg,objMandatoryRB.getString("txtDrBkChargeAccountHead"));
        txtPostageAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPostageAccountHead"));
        txtContractCrAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContractCrAccountHead"));
        txtContractDrAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtContractDrAccountHead"));
        txtIBRAccountHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIBRAccountHead"));
        txtServiceTaxHd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTaxHd"));
        txtOthersHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOthersHead"));
        txtTelChargesHead.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTelChargesHead"));
        txtAtParLimit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAtParLimit"));
        txtDiscountRateBills.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDiscountRateBills"));
        txtDefaultPostage.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDefaultPostage"));
        txtRateForDelay.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateForDelay"));
        txtOverdueRateBills.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOverdueRateBills"));
        txtRateForCBP.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRateForCBP"));
        txtCleanBills.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCleanBills"));
        txtTransitPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransitPeriod"));
        txtIntDays.setHelpMessage(lblMsg, objMandatoryRB.getString("txtIntDays"));
        cboTransitPeriod.setHelpMessage(lblMsg, objMandatoryRB.getString("cboTransitPeriod"));
        cboIntDays.setHelpMessage(lblMsg, objMandatoryRB.getString("cboIntDays"));
        txtFromSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFromSlab"));
        txtToSlab.setHelpMessage(lblMsg, objMandatoryRB.getString("txtToSlab"));
        txtCommision.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCommision"));
        txtServiceTax.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceTax"));
        cboInstrumentType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboInstrumentType"));
        cboChargeType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboChargeType"));
        cboCustCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCustCategory"));
        txtFixRate.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFixRate"));
    }
    
    private void setObservable() {
        try{
            observable = BillsOB.getInstance();
            observable.addObserver(this);
        }catch(Exception e){
            System.out.println("Exception is caught "+e);
        }
    }
    
    /* Initialising the Data in the combobox in the ui    */
    private void initComponentData() {
        try{
            cboTransitPeriod.setModel(observable.getCbmTransitPeriod());
            cboOperatesLike.setModel(observable.getCbmOperatesLike());
            cboRegType.setModel(observable.getCbmRegType());
            cboSubRegType.setModel(observable.getCbmSubRegType());
            cboInstrumentType.setModel(observable.getCbmInstrumentType());
            cboChargeType.setModel(observable.getCbmChargeType());
            cboCustCategory.setModel(observable.getCbmCustCategory());
            cboRateType.setModel(observable.getCbmRateType());
            cboIntDays.setModel(observable.getCbmIntDays());
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
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBillsMap");
        } else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectBills");
        }
        new ViewAll(this,viewMap).show();
    }
    
    /* Fills up the HashMap with data when user selects the row in ViewAll screen  */
    public void fillData(Object  map) {
        HashMap hash = (HashMap) map;
        final String accHdId="AC_HD_ID";
        final String accHdDesc="AC_HD_DESC";
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
                }
                setButtonEnableDisable();
                enableDisableContraActHead();
                if (viewType.equals(ClientConstants.ACTION_STATUS[20])){
                    enableDisableProductId(true);
                    //                    btnCopy.setEnabled(false);
                }
                btnCopy.setEnabled(false);
            }else if(viewType.equals("GLAcHd")){
                txtGLAccountHead.setText((String)hash.get(accHdId));
                btnGLAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("BankDrChargesAcHd"))
            {
            txtDrBkChargeAccountHead.setText(CommonUtil.convertObjToStr(hash.get(accHdId)));
            btnDrBkChargeAccountHead.setText(CommonUtil.convertObjToStr(hash.get(accHdDesc)));
            }
            else if(viewType.equals("BankMisChargesAcHd"))
            {
            txtMisBkChargeAccountHead.setText(CommonUtil.convertObjToStr(hash.get(accHdId)));
           btnMisBkChargeAccountHead.setText(CommonUtil.convertObjToStr(hash.get(accHdDesc)));
            }
            else if(viewType.equals("IntAcHd")){
                txtInterestAccountHead.setText((String)hash.get(accHdId));
                btnInterestAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("ChargesAcHd")){
                txtChargesAccountHead.setText((String)hash.get(accHdId));
                btnChargesAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("DDAcHd")){
                txtDDAccountHead.setText((String)hash.get(accHdId));
                btnDDAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("MgAcHd")){
                txtMarginAccountHead.setText((String)hash.get(accHdId));
                btnMarginAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("CommAcHd")){
                txtCommissionAccountHead.setText((String)hash.get(accHdId));
                btnCommissionAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("PostAcHd")){
                txtPostageAccountHead.setText((String)hash.get(accHdId));
                btnPostageAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("ContraCrAcHd")){
                txtContractCrAccountHead.setText((String)hash.get(accHdId));
                btnContractCrAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }else if(viewType.equals("ContraDrAcHd")){
                txtContractDrAccountHead.setText((String)hash.get(accHdId));
                btnContractDrAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }else if(viewType.equals("IBRAcHd")){
                txtIBRAccountHead.setText((String)hash.get(accHdId));
                btnIBRAccountHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("ServTaxAcHd")){
                txtServiceTaxHd.setText((String)hash.get(accHdId));
                btnServiceTaxHd.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("AtPar")){
                txtAtParLimit.setText((String)hash.get(accHdId));
                //btnAtParLimit.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("OthersHead")){
                txtOthersHead.setText((String)hash.get(accHdId));
                btnOthersHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("BillsRealisedHead")){
                txtBillsRealisedHead.setText((String)hash.get(accHdId));
                btnBillsRealisedHead.setToolTipText((String)hash.get(accHdDesc));
            }
            else if(viewType.equals("TelChargesHead")){
                txtTelChargesHead.setText((String)hash.get(accHdId));
                btnTelChargesHead.setToolTipText((String)hash.get(accHdDesc));
            }else if(viewType.equals("OBCAcHd")){
                txtOBCCommAcHd.setText((String)hash.get(accHdId));
            }else if(viewType.equals("BankChargesAcHd")){
                txtBankChargesAcHd.setText(CommonUtil.convertObjToStr(hash.get(accHdId)));
                btnBankChargesAcHd.setToolTipText(CommonUtil.convertObjToStr(hash.get(accHdDesc)));
            }else if(viewType.equals("DebitBankChargesAcHd")){
                txtDebitBankChargesAcHd.setText(CommonUtil.convertObjToStr(hash.get(accHdId)));
                btnDebitBankChargesAcHd.setToolTipText(CommonUtil.convertObjToStr(hash.get(accHdDesc)));
            }
        }
        
        //__ To Save the data in the Internal Frame...
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
        final String mandatoryMessage = checkMandatory(panBillsDetails);
        message = new StringBuffer(mandatoryMessage);
        if(rdoContraAccountHead_Yes.isSelected()){
            if(txtContractCrAccountHead.getText().equals("") || txtContractCrAccountHead.getText().length() == 0){
                message.append(objMandatoryRB.getString("txtContractCrAccountHead"));
            }
            if(cboTransitPeriod.getSelectedIndex()!=0)
                if(ClientUtil.validPeriodMaxLength(txtTransitPeriod, CommonUtil.convertObjToStr(observable.getCbmTransitPeriod().getKeyForSelected()))==false){
                    message.append(resourceBundle.getString("periodMsg"));
                }
        }
        if(message.length() > 0 ){
            displayAlert(message.toString());
        }else{
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
        BillsUI bui=new BillsUI();
        frame.getContentPane().add(bui);
        frame.setSize(700,400);
        frame.show();
        bui.show();
    }
    
    private void setHelpBtnsEnable(boolean flag){
        btnGLAccountHead.setEnabled(flag);
        btnInterestAccountHead.setEnabled(flag);
        btnChargesAccountHead.setEnabled(flag);
        btnDDAccountHead.setEnabled(flag);
        btnMarginAccountHead.setEnabled(flag);
        btnDrBkChargeAccountHead.setEnabled(flag);
        btnMisBkChargeAccountHead.setEnabled(flag);
        btnCommissionAccountHead.setEnabled(flag);
        btnPostageAccountHead.setEnabled(flag);
        btnContractCrAccountHead.setEnabled(flag);
        btnContractDrAccountHead.setEnabled(flag);
        btnIBRAccountHead.setEnabled(flag);
        btnServiceTaxHd.setEnabled(flag);
        btnBillsRealisedHead.setEnabled(flag);
        btnOthersHead.setEnabled(flag);
        btnTelChargesHead.setEnabled(flag);
        btnBankChargesAcHd.setEnabled(flag);
        btnDebitBankChargesAcHd.setEnabled(flag);
    }
    
    private void enableDisableContraActHead(){
        if(observable.getActionType()== ClientConstants.ACTIONTYPE_EDIT){
            if(rdoContraAccountHead_No.isSelected()){
                txtContractCrAccountHead.setText("");
                txtContractCrAccountHead.setEnabled(false);
                btnContractCrAccountHead.setEnabled(false);
                btnContractDrAccountHead.setEnabled(false);
                txtContractDrAccountHead.setText("");
                txtContractDrAccountHead.setEnabled(false);
            }else{
                txtContractCrAccountHead.setEnabled(true);
                txtContractDrAccountHead.setEnabled(true);
                btnContractCrAccountHead.setEnabled(true);
                btnContractDrAccountHead.setEnabled(true);
            }
        }else if(observable.getActionType()==ClientConstants.ACTIONTYPE_DELETE){
            rdoContraAccountHead_No.setEnabled(false);
            rdoContraAccountHead_Yes.setEnabled(false);
            btnContractCrAccountHead.setEnabled(false);
            btnContractDrAccountHead.setEnabled(false);
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
        rdoCreditOtherBankComm = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrBills = new javax.swing.JToolBar();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace27 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg1 = new com.see.truetransact.uicomponent.CLabel();
        panBills = new com.see.truetransact.uicomponent.CPanel();
        tabBills = new com.see.truetransact.uicomponent.CTabbedPane();
        panBillsDetails = new com.see.truetransact.uicomponent.CPanel();
        panProductId = new com.see.truetransact.uicomponent.CPanel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        txtProductId = new com.see.truetransact.uicomponent.CTextField();
        lblBaseCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboBaseCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblOperatesLike = new com.see.truetransact.uicomponent.CLabel();
        cboOperatesLike = new com.see.truetransact.uicomponent.CComboBox();
        lblProdDesc = new com.see.truetransact.uicomponent.CLabel();
        txtProdDesc = new com.see.truetransact.uicomponent.CTextField();
        cboRegType = new com.see.truetransact.uicomponent.CComboBox();
        cboSubRegType = new com.see.truetransact.uicomponent.CComboBox();
        lblRegisterType = new com.see.truetransact.uicomponent.CLabel();
        lblSubRegType = new com.see.truetransact.uicomponent.CLabel();
        panBillsAcHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        panAcHdDetails = new com.see.truetransact.uicomponent.CPanel();
        lblGLAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtGLAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnGLAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblInterestAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtInterestAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnInterestAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblChargesAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtChargesAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnChargesAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblContraAccountHead = new com.see.truetransact.uicomponent.CLabel();
        panrdoContraAccountHead = new com.see.truetransact.uicomponent.CPanel();
        rdoContraAccountHead_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoContraAccountHead_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblDDAccountHead = new com.see.truetransact.uicomponent.CLabel();
        lblPostDtdCheqAllowed = new com.see.truetransact.uicomponent.CLabel();
        panrdoPostDtdCheqAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoPostDtdCheqAllowed_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPostDtdCheqAllowed_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblBillsRealisedHD = new com.see.truetransact.uicomponent.CLabel();
        btnDDAccountHead = new com.see.truetransact.uicomponent.CButton();
        txtDDAccountHead = new com.see.truetransact.uicomponent.CTextField();
        txtBillsRealisedHead = new com.see.truetransact.uicomponent.CTextField();
        btnBillsRealisedHead = new com.see.truetransact.uicomponent.CButton();
        lblBillsRealisedHD1 = new com.see.truetransact.uicomponent.CLabel();
        txtOBCCommAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnOBCCommAcHd = new com.see.truetransact.uicomponent.CButton();
        lblBillsDebitBankChargesHD = new com.see.truetransact.uicomponent.CLabel();
        txtBankChargesAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnBankChargesAcHd = new com.see.truetransact.uicomponent.CButton();
        lblBillsBankChargesHD = new com.see.truetransact.uicomponent.CLabel();
        txtDebitBankChargesAcHd = new com.see.truetransact.uicomponent.CTextField();
        btnDebitBankChargesAcHd = new com.see.truetransact.uicomponent.CButton();
        panAcHeadDetails = new com.see.truetransact.uicomponent.CPanel();
        lblMarginAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtMarginAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnMarginAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblCommissionAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtCommissionAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnCommissionAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblPostageAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtPostageAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnPostageAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblContractDrAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtContractDrAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnContractDrAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblIBRAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtIBRAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnIBRAccountHead = new com.see.truetransact.uicomponent.CButton();
        lblStateCapMetroOthers = new com.see.truetransact.uicomponent.CLabel();
        lblOthersHead = new com.see.truetransact.uicomponent.CLabel();
        txtOthersHead = new com.see.truetransact.uicomponent.CTextField();
        btnOthersHead = new com.see.truetransact.uicomponent.CButton();
        lblTelChargesHead = new com.see.truetransact.uicomponent.CLabel();
        txtTelChargesHead = new com.see.truetransact.uicomponent.CTextField();
        btnTelChargesHead = new com.see.truetransact.uicomponent.CButton();
        txtServiceTaxHd = new com.see.truetransact.uicomponent.CTextField();
        btnServiceTaxHd = new com.see.truetransact.uicomponent.CButton();
        lblContractCrAccountHead = new com.see.truetransact.uicomponent.CLabel();
        txtContractCrAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnContractCrAccountHead = new com.see.truetransact.uicomponent.CButton();
        sptBills_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panCollectCommFrmCustomer = new com.see.truetransact.uicomponent.CPanel();
        lblCollectCommFrmCustomer = new com.see.truetransact.uicomponent.CLabel();
        chkCollectCommFrmCustomer = new com.see.truetransact.uicomponent.CCheckBox();
        lblCreditOtherBankCommision = new com.see.truetransact.uicomponent.CLabel();
        panrdoContraAccountHead2 = new com.see.truetransact.uicomponent.CPanel();
        rdoGlAcHd = new com.see.truetransact.uicomponent.CRadioButton();
        rdoInvestmentAcHd = new com.see.truetransact.uicomponent.CRadioButton();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        txtDrBkChargeAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnDrBkChargeAccountHead = new com.see.truetransact.uicomponent.CButton();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        txtMisBkChargeAccountHead = new com.see.truetransact.uicomponent.CTextField();
        btnMisBkChargeAccountHead = new com.see.truetransact.uicomponent.CButton();
        panRate = new com.see.truetransact.uicomponent.CPanel();
        lblDiscountRateOfBD = new com.see.truetransact.uicomponent.CLabel();
        lblOverdueInterestForBD = new com.see.truetransact.uicomponent.CLabel();
        lblCleanBillsPurchased = new com.see.truetransact.uicomponent.CLabel();
        lblDefaultPostage = new com.see.truetransact.uicomponent.CLabel();
        lblOverdueRateCBP = new com.see.truetransact.uicomponent.CLabel();
        lblAtParLimit = new com.see.truetransact.uicomponent.CLabel();
        txtAtParLimit = new com.see.truetransact.uicomponent.CTextField();
        panDiscountRate = new com.see.truetransact.uicomponent.CPanel();
        txtDiscountRateBills = new com.see.truetransact.uicomponent.CTextField();
        lblDiscountRateBills_Per = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills = new com.see.truetransact.uicomponent.CPanel();
        txtDefaultPostage = new com.see.truetransact.uicomponent.CTextField();
        lblDefaultPostage_Per = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills1 = new com.see.truetransact.uicomponent.CPanel();
        txtOverdueRateBills = new com.see.truetransact.uicomponent.CTextField();
        lblOverdueRateBills_Per = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills2 = new com.see.truetransact.uicomponent.CPanel();
        txtRateForCBP = new com.see.truetransact.uicomponent.CTextField();
        lblRateForCBP_Per = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills3 = new com.see.truetransact.uicomponent.CPanel();
        txtCleanBills = new com.see.truetransact.uicomponent.CTextField();
        lblCleanBills_Per = new com.see.truetransact.uicomponent.CLabel();
        panTransitPeriod = new com.see.truetransact.uicomponent.CPanel();
        txtTransitPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboTransitPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblTransitPeriod = new com.see.truetransact.uicomponent.CLabel();
        panOverdueRateBills8 = new com.see.truetransact.uicomponent.CPanel();
        txtRateForDelay = new com.see.truetransact.uicomponent.CTextField();
        lblDefaultPostage_Per1 = new com.see.truetransact.uicomponent.CLabel();
        lblRateForDelay = new com.see.truetransact.uicomponent.CLabel();
        panIntDays = new com.see.truetransact.uicomponent.CPanel();
        txtIntDays = new com.see.truetransact.uicomponent.CTextField();
        cboIntDays = new com.see.truetransact.uicomponent.CComboBox();
        lblIntDays = new com.see.truetransact.uicomponent.CLabel();
        panIntICC = new com.see.truetransact.uicomponent.CPanel();
        cRadio_ICC_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        cRadio_ICC_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblIntICC = new com.see.truetransact.uicomponent.CLabel();
        panBillsCharges = new com.see.truetransact.uicomponent.CPanel();
        panDiscountRate1 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills4 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills5 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills6 = new com.see.truetransact.uicomponent.CPanel();
        panOverdueRateBills7 = new com.see.truetransact.uicomponent.CPanel();
        panTransitPeriod1 = new com.see.truetransact.uicomponent.CPanel();
        panCharges1 = new com.see.truetransact.uicomponent.CPanel();
        sprRemitProdCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblBillsCharges = new com.see.truetransact.uicomponent.CTable();
        panRemitProdCharges = new com.see.truetransact.uicomponent.CPanel();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        lblChargeType = new com.see.truetransact.uicomponent.CLabel();
        lblCustCategory = new com.see.truetransact.uicomponent.CLabel();
        lblFromSlab = new com.see.truetransact.uicomponent.CLabel();
        lblToSlab = new com.see.truetransact.uicomponent.CLabel();
        cboChargeType = new com.see.truetransact.uicomponent.CComboBox();
        cboCustCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblCommision = new com.see.truetransact.uicomponent.CLabel();
        lblForEvery = new com.see.truetransact.uicomponent.CLabel();
        txtCommision = new com.see.truetransact.uicomponent.CTextField();
        txtForEvery = new com.see.truetransact.uicomponent.CTextField();
        txtFromSlab = new com.see.truetransact.uicomponent.CTextField();
        txtToSlab = new com.see.truetransact.uicomponent.CTextField();
        txtRateVal = new com.see.truetransact.uicomponent.CTextField();
        cboInstrumentType = new com.see.truetransact.uicomponent.CComboBox();
        lblInstrument = new com.see.truetransact.uicomponent.CLabel();
        tdtStartDt = new com.see.truetransact.uicomponent.CDateField();
        tdtEndDt = new com.see.truetransact.uicomponent.CDateField();
        lblCustCategory1 = new com.see.truetransact.uicomponent.CLabel();
        lblCustCategory2 = new com.see.truetransact.uicomponent.CLabel();
        cboRateType = new com.see.truetransact.uicomponent.CComboBox();
        txtFixRate = new com.see.truetransact.uicomponent.CTextField();
        lblFixedRate = new com.see.truetransact.uicomponent.CLabel();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        panRemitProdChargesButtons = new com.see.truetransact.uicomponent.CPanel();
        btnTabNew = new com.see.truetransact.uicomponent.CButton();
        btnTabSave = new com.see.truetransact.uicomponent.CButton();
        btnTabDelete = new com.see.truetransact.uicomponent.CButton();
        panAliasBrchRemittNumber = new com.see.truetransact.uicomponent.CPanel();
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

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace21);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrBills.add(btnEdit);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace22);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrBills.add(btnCopy);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace23);

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

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace24);

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

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace25);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrBills.add(btnException);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace26);

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
        tbrBills.add(btnPrint);

        lblSpace27.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace27.setText("     ");
        lblSpace27.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace27.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrBills.add(lblSpace27);

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
        panStatus.add(lblStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg1, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        panBills.setMinimumSize(new java.awt.Dimension(670, 500));
        panBills.setPreferredSize(new java.awt.Dimension(670, 500));
        panBills.setLayout(new java.awt.GridBagLayout());

        tabBills.setMinimumSize(new java.awt.Dimension(780, 490));
        tabBills.setPreferredSize(new java.awt.Dimension(780, 490));

        panBillsDetails.setMaximumSize(new java.awt.Dimension(672, 548));
        panBillsDetails.setMinimumSize(new java.awt.Dimension(672, 548));
        panBillsDetails.setPreferredSize(new java.awt.Dimension(672, 548));
        panBillsDetails.setLayout(new java.awt.GridBagLayout());

        panProductId.setMinimumSize(new java.awt.Dimension(230, 200));
        panProductId.setLayout(new java.awt.GridBagLayout());

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(txtProductId, gridBagConstraints);

        lblBaseCurrency.setText("Base Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblBaseCurrency, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(cboBaseCurrency, gridBagConstraints);

        lblOperatesLike.setText("Operates Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblOperatesLike, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(cboOperatesLike, gridBagConstraints);

        lblProdDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblProdDesc, gridBagConstraints);

        txtProdDesc.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(txtProdDesc, gridBagConstraints);

        cboRegType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRegTypeActionPerformed(evt);
            }
        });
        cboRegType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboRegTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(cboRegType, gridBagConstraints);

        cboSubRegType.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(cboSubRegType, gridBagConstraints);

        lblRegisterType.setText("Register Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblRegisterType, gridBagConstraints);

        lblSubRegType.setText("Sub Reg Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductId.add(lblSubRegType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 136, 0, 0);
        panBillsDetails.add(panProductId, gridBagConstraints);

        panBillsAcHeadDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBillsAcHeadDetails.setMinimumSize(new java.awt.Dimension(690, 285));
        panBillsAcHeadDetails.setPreferredSize(new java.awt.Dimension(690, 285));
        panBillsAcHeadDetails.setLayout(new java.awt.GridBagLayout());

        panAcHdDetails.setMaximumSize(new java.awt.Dimension(356, 267));
        panAcHdDetails.setMinimumSize(new java.awt.Dimension(356, 267));
        panAcHdDetails.setPreferredSize(new java.awt.Dimension(356, 267));
        panAcHdDetails.setLayout(new java.awt.GridBagLayout());

        lblGLAccountHead.setText("B/P - Discount Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = -7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 39, 0, 0);
        panAcHdDetails.add(lblGLAccountHead, gridBagConstraints);

        txtGLAccountHead.setEditable(false);
        txtGLAccountHead.setAllowAll(true);
        txtGLAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtGLAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtGLAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGLAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panAcHdDetails.add(txtGLAccountHead, gridBagConstraints);

        btnGLAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnGLAccountHead.setEnabled(false);
        btnGLAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnGLAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnGLAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnGLAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGLAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipady = -8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 14);
        panAcHdDetails.add(btnGLAccountHead, gridBagConstraints);

        lblInterestAccountHead.setText("Interest Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 81, 0, 0);
        panAcHdDetails.add(lblInterestAccountHead, gridBagConstraints);

        txtInterestAccountHead.setEditable(false);
        txtInterestAccountHead.setAllowAll(true);
        txtInterestAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtInterestAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtInterestAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtInterestAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panAcHdDetails.add(txtInterestAccountHead, gridBagConstraints);

        btnInterestAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnInterestAccountHead.setEnabled(false);
        btnInterestAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnInterestAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnInterestAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnInterestAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInterestAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 14);
        panAcHdDetails.add(btnInterestAccountHead, gridBagConstraints);

        lblChargesAccountHead.setText("Charges Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 76, 0, 0);
        panAcHdDetails.add(lblChargesAccountHead, gridBagConstraints);

        txtChargesAccountHead.setEditable(false);
        txtChargesAccountHead.setAllowAll(true);
        txtChargesAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtChargesAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtChargesAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtChargesAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panAcHdDetails.add(txtChargesAccountHead, gridBagConstraints);

        btnChargesAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnChargesAccountHead.setEnabled(false);
        btnChargesAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnChargesAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnChargesAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnChargesAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChargesAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 14);
        panAcHdDetails.add(btnChargesAccountHead, gridBagConstraints);

        lblContraAccountHead.setText("Contra Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 86, 0, 0);
        panAcHdDetails.add(lblContraAccountHead, gridBagConstraints);

        panrdoContraAccountHead.setLayout(new java.awt.GridBagLayout());

        rdoContraAccountHead.add(rdoContraAccountHead_Yes);
        rdoContraAccountHead_Yes.setText("Yes");
        rdoContraAccountHead_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoContraAccountHead_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panrdoContraAccountHead.add(rdoContraAccountHead_Yes, gridBagConstraints);

        rdoContraAccountHead.add(rdoContraAccountHead_No);
        rdoContraAccountHead_No.setText("No");
        rdoContraAccountHead_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoContraAccountHead_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panrdoContraAccountHead.add(rdoContraAccountHead_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panAcHdDetails.add(panrdoContraAccountHead, gridBagConstraints);

        lblDDAccountHead.setText("DD Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 106, 0, 0);
        panAcHdDetails.add(lblDDAccountHead, gridBagConstraints);

        lblPostDtdCheqAllowed.setText("Post Dated Cheque Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 48, 0, 0);
        panAcHdDetails.add(lblPostDtdCheqAllowed, gridBagConstraints);

        panrdoPostDtdCheqAllowed.setLayout(new java.awt.GridBagLayout());

        rdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_Yes);
        rdoPostDtdCheqAllowed_Yes.setText("Yes");
        panrdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_Yes, new java.awt.GridBagConstraints());

        rdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_No);
        rdoPostDtdCheqAllowed_No.setText("No");
        panrdoPostDtdCheqAllowed.add(rdoPostDtdCheqAllowed_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panAcHdDetails.add(panrdoPostDtdCheqAllowed, gridBagConstraints);

        lblBillsRealisedHD.setText("Bills/Cheques Realised Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 42, 0, 0);
        panAcHdDetails.add(lblBillsRealisedHD, gridBagConstraints);

        btnDDAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDDAccountHead.setEnabled(false);
        btnDDAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDDAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDDAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDDAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDDAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 14);
        panAcHdDetails.add(btnDDAccountHead, gridBagConstraints);

        txtDDAccountHead.setEditable(false);
        txtDDAccountHead.setAllowAll(true);
        txtDDAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDDAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDDAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDDAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        panAcHdDetails.add(txtDDAccountHead, gridBagConstraints);

        txtBillsRealisedHead.setEditable(false);
        txtBillsRealisedHead.setAllowAll(true);
        txtBillsRealisedHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBillsRealisedHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBillsRealisedHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBillsRealisedHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 0, 0);
        panAcHdDetails.add(txtBillsRealisedHead, gridBagConstraints);

        btnBillsRealisedHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBillsRealisedHead.setEnabled(false);
        btnBillsRealisedHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBillsRealisedHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBillsRealisedHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBillsRealisedHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillsRealisedHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 0, 14);
        panAcHdDetails.add(btnBillsRealisedHead, gridBagConstraints);

        lblBillsRealisedHD1.setText("Other Bank Commision A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 17, 0, 0);
        panAcHdDetails.add(lblBillsRealisedHD1, gridBagConstraints);

        txtOBCCommAcHd.setEditable(false);
        txtOBCCommAcHd.setAllowAll(true);
        txtOBCCommAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOBCCommAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOBCCommAcHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOBCCommAcHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panAcHdDetails.add(txtOBCCommAcHd, gridBagConstraints);

        btnOBCCommAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOBCCommAcHd.setEnabled(false);
        btnOBCCommAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnOBCCommAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOBCCommAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOBCCommAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOBCCommAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 14);
        panAcHdDetails.add(btnOBCCommAcHd, gridBagConstraints);

        lblBillsDebitBankChargesHD.setText("Debit Bank Chrges A/c Hd");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 50, 0, 0);
        panAcHdDetails.add(lblBillsDebitBankChargesHD, gridBagConstraints);

        txtBankChargesAcHd.setEditable(false);
        txtBankChargesAcHd.setAllowAll(true);
        txtBankChargesAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtBankChargesAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBankChargesAcHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBankChargesAcHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 8, 0, 0);
        panAcHdDetails.add(txtBankChargesAcHd, gridBagConstraints);

        btnBankChargesAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankChargesAcHd.setEnabled(false);
        btnBankChargesAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnBankChargesAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankChargesAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankChargesAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankChargesAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 14);
        panAcHdDetails.add(btnBankChargesAcHd, gridBagConstraints);

        lblBillsBankChargesHD.setText("Bank Charges A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(9, 70, 0, 0);
        panAcHdDetails.add(lblBillsBankChargesHD, gridBagConstraints);

        txtDebitBankChargesAcHd.setEditable(false);
        txtDebitBankChargesAcHd.setAllowAll(true);
        txtDebitBankChargesAcHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtDebitBankChargesAcHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDebitBankChargesAcHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDebitBankChargesAcHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 9, 0);
        panAcHdDetails.add(txtDebitBankChargesAcHd, gridBagConstraints);

        btnDebitBankChargesAcHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDebitBankChargesAcHd.setEnabled(false);
        btnDebitBankChargesAcHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnDebitBankChargesAcHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDebitBankChargesAcHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDebitBankChargesAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDebitBankChargesAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 9, 14);
        panAcHdDetails.add(btnDebitBankChargesAcHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -1;
        gridBagConstraints.ipady = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 4, 0);
        panBillsAcHeadDetails.add(panAcHdDetails, gridBagConstraints);

        panAcHeadDetails.setMinimumSize(new java.awt.Dimension(310, 271));
        panAcHeadDetails.setPreferredSize(new java.awt.Dimension(310, 271));
        panAcHeadDetails.setLayout(new java.awt.GridBagLayout());

        lblMarginAccountHead.setText("Margin Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblMarginAccountHead, gridBagConstraints);

        txtMarginAccountHead.setEditable(false);
        txtMarginAccountHead.setAllowAll(true);
        txtMarginAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMarginAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMarginAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtMarginAccountHead, gridBagConstraints);

        btnMarginAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMarginAccountHead.setEnabled(false);
        btnMarginAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnMarginAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnMarginAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnMarginAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMarginAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnMarginAccountHead, gridBagConstraints);

        lblCommissionAccountHead.setText("Commission Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblCommissionAccountHead, gridBagConstraints);

        txtCommissionAccountHead.setEditable(false);
        txtCommissionAccountHead.setAllowAll(true);
        txtCommissionAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCommissionAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommissionAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommissionAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtCommissionAccountHead, gridBagConstraints);

        btnCommissionAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCommissionAccountHead.setEnabled(false);
        btnCommissionAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnCommissionAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCommissionAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCommissionAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCommissionAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnCommissionAccountHead, gridBagConstraints);

        lblPostageAccountHead.setText("Postage Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblPostageAccountHead, gridBagConstraints);

        txtPostageAccountHead.setEditable(false);
        txtPostageAccountHead.setAllowAll(true);
        txtPostageAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPostageAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPostageAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPostageAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtPostageAccountHead, gridBagConstraints);

        btnPostageAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostageAccountHead.setEnabled(false);
        btnPostageAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnPostageAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPostageAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPostageAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostageAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnPostageAccountHead, gridBagConstraints);

        lblContractDrAccountHead.setText("Contra Dr Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblContractDrAccountHead, gridBagConstraints);

        txtContractDrAccountHead.setEditable(false);
        txtContractDrAccountHead.setAllowAll(true);
        txtContractDrAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtContractDrAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtContractDrAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContractDrAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtContractDrAccountHead, gridBagConstraints);

        btnContractDrAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnContractDrAccountHead.setEnabled(false);
        btnContractDrAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnContractDrAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnContractDrAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnContractDrAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContractDrAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnContractDrAccountHead, gridBagConstraints);

        lblIBRAccountHead.setText("IBR Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblIBRAccountHead, gridBagConstraints);

        txtIBRAccountHead.setEditable(false);
        txtIBRAccountHead.setAllowAll(true);
        txtIBRAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIBRAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIBRAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIBRAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtIBRAccountHead, gridBagConstraints);

        btnIBRAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIBRAccountHead.setEnabled(false);
        btnIBRAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnIBRAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIBRAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIBRAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIBRAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnIBRAccountHead, gridBagConstraints);

        lblStateCapMetroOthers.setText("Service Tax Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblStateCapMetroOthers, gridBagConstraints);

        lblOthersHead.setText("PO Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblOthersHead, gridBagConstraints);

        txtOthersHead.setEditable(false);
        txtOthersHead.setAllowAll(true);
        txtOthersHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtOthersHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOthersHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtOthersHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtOthersHead, gridBagConstraints);

        btnOthersHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOthersHead.setEnabled(false);
        btnOthersHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnOthersHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOthersHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOthersHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOthersHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnOthersHead, gridBagConstraints);

        lblTelChargesHead.setText("Telephone Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblTelChargesHead, gridBagConstraints);

        txtTelChargesHead.setEditable(false);
        txtTelChargesHead.setAllowAll(true);
        txtTelChargesHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTelChargesHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTelChargesHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTelChargesHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtTelChargesHead, gridBagConstraints);

        btnTelChargesHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTelChargesHead.setEnabled(false);
        btnTelChargesHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnTelChargesHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTelChargesHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTelChargesHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTelChargesHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnTelChargesHead, gridBagConstraints);

        txtServiceTaxHd.setEditable(false);
        txtServiceTaxHd.setAllowAll(true);
        txtServiceTaxHd.setMaximumSize(new java.awt.Dimension(100, 21));
        txtServiceTaxHd.setMinimumSize(new java.awt.Dimension(100, 21));
        txtServiceTaxHd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtServiceTaxHdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtServiceTaxHd, gridBagConstraints);

        btnServiceTaxHd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnServiceTaxHd.setEnabled(false);
        btnServiceTaxHd.setMaximumSize(new java.awt.Dimension(21, 21));
        btnServiceTaxHd.setMinimumSize(new java.awt.Dimension(21, 21));
        btnServiceTaxHd.setPreferredSize(new java.awt.Dimension(21, 21));
        btnServiceTaxHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnServiceTaxHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnServiceTaxHd, gridBagConstraints);

        lblContractCrAccountHead.setText("Contra Cr Account Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAcHeadDetails.add(lblContractCrAccountHead, gridBagConstraints);

        txtContractCrAccountHead.setEditable(false);
        txtContractCrAccountHead.setAllowAll(true);
        txtContractCrAccountHead.setMaximumSize(new java.awt.Dimension(100, 21));
        txtContractCrAccountHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtContractCrAccountHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtContractCrAccountHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 0);
        panAcHeadDetails.add(txtContractCrAccountHead, gridBagConstraints);

        btnContractCrAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnContractCrAccountHead.setEnabled(false);
        btnContractCrAccountHead.setMaximumSize(new java.awt.Dimension(21, 21));
        btnContractCrAccountHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnContractCrAccountHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnContractCrAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContractCrAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAcHeadDetails.add(btnContractCrAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 5, 0, 4);
        panBillsAcHeadDetails.add(panAcHeadDetails, gridBagConstraints);

        sptBills_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -1;
        gridBagConstraints.ipady = 266;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 15, 0, 0);
        panBillsAcHeadDetails.add(sptBills_Vert, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipady = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 42, 11, 43);
        panBillsDetails.add(panBillsAcHeadDetails, gridBagConstraints);

        panCollectCommFrmCustomer.setMinimumSize(new java.awt.Dimension(400, 185));
        panCollectCommFrmCustomer.setPreferredSize(new java.awt.Dimension(400, 185));
        panCollectCommFrmCustomer.setLayout(new java.awt.GridBagLayout());

        lblCollectCommFrmCustomer.setText("Collect Commision/Charges From Cust A/c");
        lblCollectCommFrmCustomer.setMaximumSize(new java.awt.Dimension(289, 18));
        lblCollectCommFrmCustomer.setMinimumSize(new java.awt.Dimension(260, 18));
        lblCollectCommFrmCustomer.setPreferredSize(new java.awt.Dimension(260, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panCollectCommFrmCustomer.add(lblCollectCommFrmCustomer, gridBagConstraints);

        chkCollectCommFrmCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkCollectCommFrmCustomerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panCollectCommFrmCustomer.add(chkCollectCommFrmCustomer, gridBagConstraints);

        lblCreditOtherBankCommision.setText("Credit Other Bank Commision To");
        lblCreditOtherBankCommision.setMinimumSize(new java.awt.Dimension(190, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 5, 0, 0);
        panCollectCommFrmCustomer.add(lblCreditOtherBankCommision, gridBagConstraints);

        panrdoContraAccountHead2.setMinimumSize(new java.awt.Dimension(264, 35));
        panrdoContraAccountHead2.setPreferredSize(new java.awt.Dimension(254, 25));
        panrdoContraAccountHead2.setLayout(new java.awt.GridBagLayout());

        rdoGlAcHd.setText("GL A/c Head");
        rdoGlAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGlAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panrdoContraAccountHead2.add(rdoGlAcHd, gridBagConstraints);

        rdoInvestmentAcHd.setText("Investment A/c Head");
        rdoInvestmentAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInvestmentAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panrdoContraAccountHead2.add(rdoInvestmentAcHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 7);
        panCollectCommFrmCustomer.add(panrdoContraAccountHead2, gridBagConstraints);

        cLabel1.setText("Bank Charges Dr A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panCollectCommFrmCustomer.add(cLabel1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panCollectCommFrmCustomer.add(txtDrBkChargeAccountHead, gridBagConstraints);

        btnDrBkChargeAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDrBkChargeAccountHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnDrBkChargeAccountHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnDrBkChargeAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDrBkChargeAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panCollectCommFrmCustomer.add(btnDrBkChargeAccountHead, gridBagConstraints);

        cLabel2.setText("Misc Charges Dr A/c Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panCollectCommFrmCustomer.add(cLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panCollectCommFrmCustomer.add(txtMisBkChargeAccountHead, gridBagConstraints);

        btnMisBkChargeAccountHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnMisBkChargeAccountHead.setMaximumSize(new java.awt.Dimension(25, 25));
        btnMisBkChargeAccountHead.setMinimumSize(new java.awt.Dimension(25, 25));
        btnMisBkChargeAccountHead.setPreferredSize(new java.awt.Dimension(25, 25));
        btnMisBkChargeAccountHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMisBkChargeAccountHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panCollectCommFrmCustomer.add(btnMisBkChargeAccountHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 21);
        panBillsDetails.add(panCollectCommFrmCustomer, gridBagConstraints);

        tabBills.addTab("Bills/Cheque Details", panBillsDetails);

        panRate.setLayout(new java.awt.GridBagLayout());

        lblDiscountRateOfBD.setText("Discount Rate For Bills");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblDiscountRateOfBD, gridBagConstraints);

        lblOverdueInterestForBD.setText("Overdue Interest Rate For Bills");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblOverdueInterestForBD, gridBagConstraints);

        lblCleanBillsPurchased.setText("Clean Bills Purchased");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblCleanBillsPurchased, gridBagConstraints);

        lblDefaultPostage.setText("Default Postage Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblDefaultPostage, gridBagConstraints);

        lblOverdueRateCBP.setText("Overdue Rate For CBP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblOverdueRateCBP, gridBagConstraints);

        lblAtParLimit.setText("At Par Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblAtParLimit, gridBagConstraints);

        txtAtParLimit.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAtParLimit.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 5, 4, 75);
        panRate.add(txtAtParLimit, gridBagConstraints);

        panDiscountRate.setLayout(new java.awt.GridBagLayout());

        txtDiscountRateBills.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDiscountRateBills.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDiscountRateBills.setValidation(new PercentageValidation());
        panDiscountRate.add(txtDiscountRateBills, new java.awt.GridBagConstraints());

        lblDiscountRateBills_Per.setText("%");
        panDiscountRate.add(lblDiscountRateBills_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(panDiscountRate, gridBagConstraints);

        panOverdueRateBills.setLayout(new java.awt.GridBagLayout());

        txtDefaultPostage.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDefaultPostage.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDefaultPostage.setValidation(new PercentageValidation());
        panOverdueRateBills.add(txtDefaultPostage, new java.awt.GridBagConstraints());

        lblDefaultPostage_Per.setText("%");
        panOverdueRateBills.add(lblDefaultPostage_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 1);
        panRate.add(panOverdueRateBills, gridBagConstraints);

        panOverdueRateBills1.setLayout(new java.awt.GridBagLayout());

        txtOverdueRateBills.setMinimumSize(new java.awt.Dimension(50, 21));
        txtOverdueRateBills.setPreferredSize(new java.awt.Dimension(50, 21));
        txtOverdueRateBills.setValidation(new PercentageValidation());
        panOverdueRateBills1.add(txtOverdueRateBills, new java.awt.GridBagConstraints());

        lblOverdueRateBills_Per.setText("%");
        panOverdueRateBills1.add(lblOverdueRateBills_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(panOverdueRateBills1, gridBagConstraints);

        panOverdueRateBills2.setLayout(new java.awt.GridBagLayout());

        txtRateForCBP.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateForCBP.setPreferredSize(new java.awt.Dimension(50, 21));
        txtRateForCBP.setValidation(new PercentageValidation());
        panOverdueRateBills2.add(txtRateForCBP, new java.awt.GridBagConstraints());

        lblRateForCBP_Per.setText("%");
        panOverdueRateBills2.add(lblRateForCBP_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(panOverdueRateBills2, gridBagConstraints);

        panOverdueRateBills3.setLayout(new java.awt.GridBagLayout());

        txtCleanBills.setMinimumSize(new java.awt.Dimension(50, 21));
        txtCleanBills.setPreferredSize(new java.awt.Dimension(50, 21));
        txtCleanBills.setValidation(new PercentageValidation());
        panOverdueRateBills3.add(txtCleanBills, new java.awt.GridBagConstraints());

        lblCleanBills_Per.setText("%");
        panOverdueRateBills3.add(lblCleanBills_Per, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(panOverdueRateBills3, gridBagConstraints);

        panTransitPeriod.setLayout(new java.awt.GridBagLayout());

        txtTransitPeriod.setMaximumSize(new java.awt.Dimension(100, 21));
        txtTransitPeriod.setMinimumSize(new java.awt.Dimension(25, 21));
        txtTransitPeriod.setPreferredSize(new java.awt.Dimension(25, 21));
        txtTransitPeriod.setValidation(new NumericValidation()
        );
        txtTransitPeriod.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTransitPeriod.add(txtTransitPeriod, gridBagConstraints);

        cboTransitPeriod.setMinimumSize(new java.awt.Dimension(70, 25));
        cboTransitPeriod.setPreferredSize(new java.awt.Dimension(70, 21));
        cboTransitPeriod.setEnabled(false);
        cboTransitPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboTransitPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panTransitPeriod.add(cboTransitPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRate.add(panTransitPeriod, gridBagConstraints);

        lblTransitPeriod.setText("Transit Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblTransitPeriod, gridBagConstraints);

        panOverdueRateBills8.setLayout(new java.awt.GridBagLayout());

        txtRateForDelay.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateForDelay.setPreferredSize(new java.awt.Dimension(50, 21));
        txtRateForDelay.setValidation(new PercentageValidation());
        panOverdueRateBills8.add(txtRateForDelay, new java.awt.GridBagConstraints());

        lblDefaultPostage_Per1.setText("%");
        panOverdueRateBills8.add(lblDefaultPostage_Per1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(panOverdueRateBills8, gridBagConstraints);

        lblRateForDelay.setText("Margin");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblRateForDelay, gridBagConstraints);

        panIntDays.setLayout(new java.awt.GridBagLayout());

        txtIntDays.setMaximumSize(new java.awt.Dimension(100, 21));
        txtIntDays.setMinimumSize(new java.awt.Dimension(25, 21));
        txtIntDays.setPreferredSize(new java.awt.Dimension(25, 21));
        txtIntDays.setValidation(new NumericValidation()
        );
        txtIntDays.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntDays.add(txtIntDays, gridBagConstraints);

        cboIntDays.setMinimumSize(new java.awt.Dimension(70, 25));
        cboIntDays.setPreferredSize(new java.awt.Dimension(70, 21));
        cboIntDays.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panIntDays.add(cboIntDays, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panRate.add(panIntDays, gridBagConstraints);

        lblIntDays.setText("No. Of Days Interest to be collected for Cheque Discount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblIntDays, gridBagConstraints);

        panIntICC.setMinimumSize(new java.awt.Dimension(92, 27));
        panIntICC.setPreferredSize(new java.awt.Dimension(92, 27));
        panIntICC.setLayout(new java.awt.GridBagLayout());

        rdoICCInt.add(cRadio_ICC_Yes);
        cRadio_ICC_Yes.setText("Yes");
        cRadio_ICC_Yes.setMaximumSize(new java.awt.Dimension(55, 27));
        cRadio_ICC_Yes.setMinimumSize(new java.awt.Dimension(55, 27));
        cRadio_ICC_Yes.setPreferredSize(new java.awt.Dimension(55, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panIntICC.add(cRadio_ICC_Yes, gridBagConstraints);

        rdoICCInt.add(cRadio_ICC_No);
        cRadio_ICC_No.setText("No");
        cRadio_ICC_No.setMinimumSize(new java.awt.Dimension(50, 27));
        cRadio_ICC_No.setPreferredSize(new java.awt.Dimension(50, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 4);
        panIntICC.add(cRadio_ICC_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 100);
        panRate.add(panIntICC, gridBagConstraints);

        lblIntICC.setText("Collect Interest during Lodgement for ICC");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRate.add(lblIntICC, gridBagConstraints);

        tabBills.addTab("Bills/Cheque Rates", panRate);

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
        panCharges1.setPreferredSize(new java.awt.Dimension(640, 450));
        panCharges1.setLayout(new java.awt.GridBagLayout());

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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblBillsChargesMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBillsChargesMouseClicked(evt);
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

        panRemitProdCharges.setMinimumSize(new java.awt.Dimension(650, 300));
        panRemitProdCharges.setPreferredSize(new java.awt.Dimension(650, 350));
        panRemitProdCharges.setLayout(new java.awt.GridBagLayout());

        panCharges.setMinimumSize(new java.awt.Dimension(250, 400));
        panCharges.setPreferredSize(new java.awt.Dimension(250, 377));
        panCharges.setLayout(new java.awt.GridBagLayout());

        lblChargeType.setText("Charge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblChargeType, gridBagConstraints);

        lblCustCategory.setText("Customer Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblCustCategory, gridBagConstraints);

        lblFromSlab.setText("Amt Range from");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblFromSlab, gridBagConstraints);

        lblToSlab.setText("Amt Range to");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblToSlab, gridBagConstraints);

        cboChargeType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboChargeType.setPopupWidth(180);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboChargeType, gridBagConstraints);

        cboCustCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCustCategory.setPopupWidth(135);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboCustCategory, gridBagConstraints);

        lblCommision.setText("Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblCommision, gridBagConstraints);

        lblForEvery.setText("For Every");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblForEvery, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtCommision, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtForEvery, gridBagConstraints);

        txtFromSlab.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtFromSlab, gridBagConstraints);

        txtToSlab.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtToSlab, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtRateVal, gridBagConstraints);

        cboInstrumentType.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboInstrumentType, gridBagConstraints);

        lblInstrument.setText("Instrument Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblInstrument, gridBagConstraints);

        tdtStartDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtStartDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtStartDt, gridBagConstraints);

        tdtEndDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtEndDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(tdtEndDt, gridBagConstraints);

        lblCustCategory1.setText("End Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblCustCategory1, gridBagConstraints);

        lblCustCategory2.setText("Start Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblCustCategory2, gridBagConstraints);

        cboRateType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRateType.setPopupWidth(135);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(cboRateType, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(txtFixRate, gridBagConstraints);

        lblFixedRate.setText("Fix Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(lblFixedRate, gridBagConstraints);
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

        panBillsCharges.add(panCharges1, new java.awt.GridBagConstraints());

        tabBills.addTab("Bills/Cheque Charges", panBillsCharges);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 7, 0);
        panBills.add(tabBills, gridBagConstraints);

        getContentPane().add(panBills, java.awt.BorderLayout.CENTER);

        mbrBills.setName("mbrCustomer"); // NOI18N

        mnuProcess.setText("Process");
        mnuProcess.setName("mnuProcess"); // NOI18N

        mitNew.setText("New");
        mitNew.setName("mitNew"); // NOI18N
        mitNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitNewActionPerformed(evt);
            }
        });
        mnuProcess.add(mitNew);

        mitEdit.setText("Edit");
        mitEdit.setName("mitEdit"); // NOI18N
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setText("Delete");
        mitDelete.setName("mitDelete"); // NOI18N
        mitDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitDeleteActionPerformed(evt);
            }
        });
        mnuProcess.add(mitDelete);

        sptNew.setName("sptNew"); // NOI18N
        mnuProcess.add(sptNew);

        mitSave.setText("Save");
        mitSave.setName("mitSave"); // NOI18N
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setText("Cancel");
        mitCancel.setName("mitCancel"); // NOI18N
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);

        sptSave.setName("sptSave"); // NOI18N
        mnuProcess.add(sptSave);

        mitPrint.setText("Print");
        mitPrint.setName("mitPrint"); // NOI18N
        mnuProcess.add(mitPrint);

        mitClose.setText("Close");
        mitClose.setName("mitClose"); // NOI18N
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

    private void txtTelChargesHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelChargesHeadFocusLost
        // TODO add your handling code here:
        if(!txtTelChargesHead.getText().equals(""))
        {
            btnTelChargesHead.setToolTipText(getAccHdDesc(txtTelChargesHead.getText()));
        }
    }//GEN-LAST:event_txtTelChargesHeadFocusLost

    private void txtOthersHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOthersHeadFocusLost
        // TODO add your handling code here:
        if(!txtOthersHead.getText().equals(""))
        {
            btnOthersHead.setToolTipText(getAccHdDesc(txtOthersHead.getText()));
        }
    }//GEN-LAST:event_txtOthersHeadFocusLost

    private void txtServiceTaxHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtServiceTaxHdFocusLost
        // TODO add your handling code here:
        if(!txtServiceTaxHd.getText().equals(""))
        {
            btnServiceTaxHd.setToolTipText(getAccHdDesc(txtServiceTaxHd.getText()));
        }
    }//GEN-LAST:event_txtServiceTaxHdFocusLost

    private void txtIBRAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIBRAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtIBRAccountHead.getText().equals(""))
        {
            btnIBRAccountHead.setToolTipText(getAccHdDesc(txtIBRAccountHead.getText()));
        }
    }//GEN-LAST:event_txtIBRAccountHeadFocusLost

    private void txtContractDrAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContractDrAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtContractDrAccountHead.getText().equals(""))
        {
            btnContractDrAccountHead.setToolTipText(getAccHdDesc(txtContractDrAccountHead.getText()));
        }
    }//GEN-LAST:event_txtContractDrAccountHeadFocusLost

    private void txtContractCrAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContractCrAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtContractCrAccountHead.getText().equals(""))
        {
            btnContractCrAccountHead.setToolTipText(getAccHdDesc(txtContractCrAccountHead.getText()));
        }
    }//GEN-LAST:event_txtContractCrAccountHeadFocusLost

    private void txtPostageAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPostageAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtPostageAccountHead.getText().equals(""))
        {
            btnPostageAccountHead.setToolTipText(getAccHdDesc(txtPostageAccountHead.getText()));
        }
    }//GEN-LAST:event_txtPostageAccountHeadFocusLost

    private void txtCommissionAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommissionAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtCommissionAccountHead.getText().equals(""))
        {
            btnCommissionAccountHead.setToolTipText(getAccHdDesc(txtCommissionAccountHead.getText()));
        }
    }//GEN-LAST:event_txtCommissionAccountHeadFocusLost

    private void txtMarginAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarginAccountHeadFocusLost
        // TODO add your handling code here:
        
if(!txtMarginAccountHead.getText().equals(""))
        {
            btnMarginAccountHead.setToolTipText(getAccHdDesc(txtMarginAccountHead.getText()));
        }
    }//GEN-LAST:event_txtMarginAccountHeadFocusLost

    private void txtOBCCommAcHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtOBCCommAcHdFocusLost
        // TODO add your handling code here:
        if(!txtOBCCommAcHd.getText().equals(""))
        {
            btnOBCCommAcHd.setToolTipText(getAccHdDesc(txtOBCCommAcHd.getText()));
        }
    }//GEN-LAST:event_txtOBCCommAcHdFocusLost

    private void txtBillsRealisedHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBillsRealisedHeadFocusLost
        // TODO add your handling code here:
        if(!txtBillsRealisedHead.getText().equals(""))
        {
            btnBillsRealisedHead.setToolTipText(getAccHdDesc(txtBillsRealisedHead.getText()));
        }
    }//GEN-LAST:event_txtBillsRealisedHeadFocusLost

    private void txtDDAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDDAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtDDAccountHead.getText().equals(""))
        {
            btnDDAccountHead.setToolTipText(getAccHdDesc(txtDDAccountHead.getText()));
        }
    }//GEN-LAST:event_txtDDAccountHeadFocusLost

    private void txtChargesAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtChargesAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtChargesAccountHead.getText().equals(""))
        {
            btnChargesAccountHead.setToolTipText(getAccHdDesc(txtChargesAccountHead.getText()));
        }
    }//GEN-LAST:event_txtChargesAccountHeadFocusLost

    private void txtInterestAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtInterestAccountHeadFocusLost
        // TODO add your handling code here:
        if(!txtInterestAccountHead.getText().equals(""))
        {
            btnInterestAccountHead.setToolTipText(getAccHdDesc(txtInterestAccountHead.getText()));
        }
    }//GEN-LAST:event_txtInterestAccountHeadFocusLost

    private void txtGLAccountHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGLAccountHeadFocusLost
        // TODO add your handling code here:
       if(!txtGLAccountHead.getText().equals(""))
        {
            btnGLAccountHead.setToolTipText(getAccHdDesc(txtGLAccountHead.getText()));
        } 
    }//GEN-LAST:event_txtGLAccountHeadFocusLost

    private void btnOBCCommAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOBCCommAcHdActionPerformed
        // TODO add your handling code here:
        callView("OBCAcHd");
    }//GEN-LAST:event_btnOBCCommAcHdActionPerformed

    private void btnContractCrAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContractCrAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("ContraCrAcHd");  
    }//GEN-LAST:event_btnContractCrAccountHeadActionPerformed

    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_COPY);
        callView("Copy");
//        enableDisableProductId(false);
    }//GEN-LAST:event_btnCopyActionPerformed

    private void btnServiceTaxHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnServiceTaxHdActionPerformed
        // TODO add your handling code here:
         callView("ServTaxAcHd");
    }//GEN-LAST:event_btnServiceTaxHdActionPerformed

    private void cboRegTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRegTypeActionPerformed
        // TODO add your handling code here:
          if (cboRegType.getSelectedIndex() > 0) {
//              if(observable.getActionType() != ClientConstants.ACTIONTYPE_EDIT){
            String RegType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboRegType.getModel())).getKeyForSelected());
            System.out.println("***************"+RegType);
            observable.setSubRegType(RegType);
            cboSubRegType.setModel(observable.getCbmSubRegType());
//              }
          }
    }//GEN-LAST:event_cboRegTypeActionPerformed

    private void cboRegTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboRegTypeItemStateChanged
        // TODO add your handling code here:
        if (cboRegType.getSelectedIndex() > 0) {
            String RegType = CommonUtil.convertObjToStr(((ComboBoxModel)(cboRegType.getModel())).getKeyForSelected());
            System.out.println("***************"+RegType);
            if(RegType.equalsIgnoreCase("CHEQUE"))
                enableDisableBillRates(false);
            else
                enableDisableBillRates(true);
          }
    }//GEN-LAST:event_cboRegTypeItemStateChanged

    private void rdoContraAccountHead_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoContraAccountHead_NoActionPerformed
        // TODO add your handling code here:
        if(rdoContraAccountHead_No.isSelected()){
            btnContractCrAccountHead.setEnabled(false);
            txtContractCrAccountHead.setEnabled(false);
            txtContractCrAccountHead.setText("");
            btnContractDrAccountHead.setEnabled(false);
            txtContractDrAccountHead.setEnabled(false);
            txtContractDrAccountHead.setText("");
        }
    }//GEN-LAST:event_rdoContraAccountHead_NoActionPerformed

    private void rdoContraAccountHead_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoContraAccountHead_YesActionPerformed
        // TODO add your handling code here:
        if(rdoContraAccountHead_Yes.isSelected()){
            btnContractCrAccountHead.setEnabled(true);
            txtContractCrAccountHead.setEnabled(true);
            btnContractDrAccountHead.setEnabled(true);
            txtContractDrAccountHead.setEnabled(true);
        }
    }//GEN-LAST:event_rdoContraAccountHead_YesActionPerformed

    private void txtToSlabFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToSlabFocusLost
        // TODO add your handling code here:
         HashMap where = new HashMap();
         HashMap data = new HashMap();
          if(!( txtFromSlab.getText().equals("")))
          if(!( txtFromSlab.getText().equals(null))){
         where.put("FROM_SLAB", txtFromSlab.getText());
         String fromslab = (String) where.get("FROM_SLAB");
         double frmSlb = Double.parseDouble(fromslab);
         data.put("TO_SLAB", txtToSlab.getText());
         String toslab = (String) data.get("TO_SLAB");
         double toSlb = Double.parseDouble(toslab);
         if(frmSlb >= toSlb){
             displayAlert("From Slab should be less than To Slab");
             txtFromSlab.setText("");
             txtToSlab.setText("");
         }
       }
    }//GEN-LAST:event_txtToSlabFocusLost

    private void tdtStartDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtStartDtFocusLost
        // TODO add your handling code here:
            ClientUtil.validateFromDate(tdtStartDt, tdtEndDt.getDateValue());
    }//GEN-LAST:event_tdtStartDtFocusLost

    private void tdtEndDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtEndDtFocusLost
        // TODO add your handling code here:
         ClientUtil.validateToDate(tdtEndDt, tdtStartDt.getDateValue());
    }//GEN-LAST:event_tdtEndDtFocusLost

    private void tblBillsChargesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillsChargesMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_tblBillsChargesMouseClicked

    private void tblBillsChargesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBillsChargesMousePressed
        // TODO add your handling code here:
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
             tableBillsChargeClicked=true;
            //ClientUtil.enableDisable(panNoticecharge_Amt,true);
            enableDisbleBillsCharges__NewSaveDeleter(true);
            observable.populateBillsCharge(tblBillsCharges.getSelectedRow());
            //observable.resetBillsChargeValues();
             ClientUtil.enableDisable(panCharges,true);
            cboChargeType.setEnabled(false);
            cboInstrumentType.setEnabled(false);
            cboCustCategory.setEnabled(false);
            txtFromSlab.setEnabled(false);
        }
        else{
                tableBillsChargeClicked=true;
    //        tableBillsChargeClicked=false;
            enableDisbleBillsCharges__NewSaveDeleter(true);
            observable.populateBillsCharge(tblBillsCharges.getSelectedRow());
            ClientUtil.enableDisable(panCharges,true);
        }
    }//GEN-LAST:event_tblBillsChargesMousePressed

    private void btnTabDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabDeleteActionPerformed
        // TODO add your handling code here:
        if(tblBillsCharges.getSelectedRow()>=0){
            int index=tblBillsCharges.getSelectedRow();
//            index+=1;
            observable.deleteBillsCharge(index);
            observable.resetBillsChargeValues();
            updateBillsCharges();
        }
        
    }//GEN-LAST:event_btnTabDeleteActionPerformed

    private void btnTabSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabSaveActionPerformed
        // TODO add your handling code here:
//        ClientUtil.enableDisable(panCharges,false);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
            ClientUtil.enableDisable(panCharges,true);
        }
//        enableDisableBillsCharges_SaveDelete(true);
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panCharges);
        
        if (chargeCheck()){
            mandatoryMessage = mandatoryMessage + resourceBundle.getString("chargeCheck");
        }
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
        observable.saveBillsCharge(tableBillsChargeClicked,tblBillsCharges.getSelectedRow());
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
        enableDisableBillsCharges_SaveDelete(false);
        observable.resetBillsChargeValues();
        // observable.resetTable();
        updateBillsCharges();
        
    }//GEN-LAST:event_btnTabNewActionPerformed
 private void updateBillsCharges(){
        cboInstrumentType.setSelectedItem(observable.getCboInstrumentType());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
//        cboChargeType.setSelectedItem(CommonUtil.convertObjToStr(observable.getCbmChargeType().getKeyForSelected()));
        
        cboCustCategory.setSelectedItem(observable.getCboCustCategory());
        tdtStartDt.setDateValue((String)observable.getTdtStartDt());
        tdtEndDt.setDateValue((String)observable.getTdtEndDt());
        txtFromSlab.setText((String)observable.getTxtFromSlab());
        txtToSlab.setText((String)observable.getTxtToSlab());
        txtCommision.setText((String)observable.getTxtCommision());
        txtServiceTax.setText((String)observable.getTxtServiceTax());
        txtFixRate.setText((String)observable.getTxtFixRate());
        txtForEvery.setText((String)observable.getTxtForEvery());
        cboRateType.setSelectedItem(observable.getCboRateType());
        txtRateVal.setText((String)observable.getTxtRateVal());
        tblBillsCharges.setModel(observable.getTblBillsCharges());
            
    }
  private void enableDisableBillRates(boolean flag){
       lblDiscountRateOfBD.setVisible(flag);
        lblOverdueInterestForBD.setVisible(flag);
        lblOverdueRateCBP.setVisible(flag);
       // lblAtParLimit.setVisible(flag);
        lblCleanBillsPurchased.setVisible(flag);
        lblDefaultPostage.setVisible(flag);
        panDiscountRate.setVisible(flag);
        panOverdueRateBills.setVisible(flag);
        panOverdueRateBills1.setVisible(flag);
        panOverdueRateBills2.setVisible(flag);
        panOverdueRateBills3.setVisible(flag);
//        txtAtParLimit.setVisible(flag);
        txtCleanBills.setVisible(flag);
    }
  
  private boolean chargeCheck(){
        // Checking for whether Charges and Percentage is containing proper value(s)
        if ((txtFixRate == null || txtFixRate.getText().length() == 0 || CommonUtil.convertObjToDouble(txtFixRate.getText()).doubleValue() == 0) && (txtCommision == null || txtCommision.getText().length() == 0 || CommonUtil.convertObjToDouble(txtCommision.getText()).doubleValue() == 0)){
            if ((txtForEvery == null ||txtForEvery.getText().length() == 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() == 0) && cboRateType.getSelectedIndex() <= 0 && (txtRateVal == null || txtRateVal.getText().length() == 0 || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() == 0)){
                return true;
            }
        }
        return false;
    }
    private void btnTelChargesHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTelChargesHeadActionPerformed
        // TODO add your handling code here:
        callView("TelChargesHead");
    }//GEN-LAST:event_btnTelChargesHeadActionPerformed

    private void btnOthersHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOthersHeadActionPerformed
        // TODO add your handling code here:
         callView("OthersHead");
    }//GEN-LAST:event_btnOthersHeadActionPerformed

    private void btnIBRAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIBRAccountHeadActionPerformed
        // TODO add your handling code here:
         callView("IBRAcHd");
    }//GEN-LAST:event_btnIBRAccountHeadActionPerformed

    private void btnContractDrAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContractDrAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("ContraDrAcHd");  
    }//GEN-LAST:event_btnContractDrAccountHeadActionPerformed

    private void btnPostageAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostageAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("PostAcHd");
    }//GEN-LAST:event_btnPostageAccountHeadActionPerformed

    private void btnCommissionAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCommissionAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("CommAcHd");
    }//GEN-LAST:event_btnCommissionAccountHeadActionPerformed

    private void btnMarginAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMarginAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("MgAcHd");
    }//GEN-LAST:event_btnMarginAccountHeadActionPerformed

    private void btnBillsRealisedHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillsRealisedHeadActionPerformed
        // TODO add your handling code here:
        callView("BillsRealisedHead");
    }//GEN-LAST:event_btnBillsRealisedHeadActionPerformed

    private void btnDDAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDDAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("DDAcHd");
    }//GEN-LAST:event_btnDDAccountHeadActionPerformed

    private void btnChargesAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChargesAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("ChargesAcHd");
    }//GEN-LAST:event_btnChargesAccountHeadActionPerformed

    private void btnInterestAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInterestAccountHeadActionPerformed
        // TODO add your handling code here:
        callView("IntAcHd");
    }//GEN-LAST:event_btnInterestAccountHeadActionPerformed

    private void btnGLAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGLAccountHeadActionPerformed
        // TODO add your handling code here:
         callView("GLAcHd");
    }//GEN-LAST:event_btnGLAccountHeadActionPerformed

    private void cboChargeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboChargeTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboChargeTypeActionPerformed
    
    private void cboTransitPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboTransitPeriodFocusLost
        // TODO add your handling code here:
        if(txtTransitPeriod.getText().length()!=0 && cboTransitPeriod.getSelectedIndex()!=0)
            ClientUtil.validPeriodMaxLength(txtTransitPeriod, CommonUtil.convertObjToStr(observable.getCbmTransitPeriod().getKeyForSelected()));
    }//GEN-LAST:event_cboTransitPeriodFocusLost
                
    private void txtProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIdFocusLost
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if(observable.isProductIdAvailable(txtProductId.getText())) {
                displayAlert(resourceBundle.getString("warningProductId"));
            }else{
                HashMap whereMap = new HashMap();
                whereMap.put("PROD_ID", txtProductId.getText());
                List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);                
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
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    public void authorizeStatus(String authorizeStatus) {
        if (!viewType.equals(AUTHORIZE)){
            viewType = AUTHORIZE;
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getBillsProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeBillsProduct");
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
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt.clone());
            singleAuthorizeMap.put("PROD_ID", txtProductId.getText());
            
            ClientUtil.execute("authorizeBillsProduct", singleAuthorizeMap);
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
        if(rdoGlAcHd.isSelected()){
           if(txtOBCCommAcHd.getText().equals("")){
               ClientUtil.displayAlert("Other Bank cmmision head should not be empty");
               return;
           }
        }
        savePerformed();
        observable.resetTable();
        enableDisbleBillsCharges__NewSaveDeleter(false);
//        enableDisableProductId(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    
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
        //enableDisablePanBillsCharges(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        enableDisableProductId(true);
        setButtonEnableDisable();
        setHelpBtnsEnable(true);
        observable.resetBillsChargeValues();
        observable.resetTable();
        btnCopy.setEnabled(false);
        rdoContraAccountHead_No.setSelected(true);
        rdoContraAccountHead_NoActionPerformed(null);
        txtOBCCommAcHd.setEnabled(false);
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed

private void txtBankChargesAcHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBankChargesAcHdFocusLost
// TODO add your handling code here:
    if(!txtBankChargesAcHd.getText().equals(""))
        {
            btnBankChargesAcHd.setToolTipText(getAccHdDesc(txtBankChargesAcHd.getText()));
        }
}//GEN-LAST:event_txtBankChargesAcHdFocusLost

private void btnBankChargesAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankChargesAcHdActionPerformed
// TODO add your handling code here:
        callView("BankChargesAcHd");  
}//GEN-LAST:event_btnBankChargesAcHdActionPerformed

private void txtDebitBankChargesAcHdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDebitBankChargesAcHdFocusLost
    // TODO add your handling code here:
}//GEN-LAST:event_txtDebitBankChargesAcHdFocusLost

private void btnDebitBankChargesAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDebitBankChargesAcHdActionPerformed
    // TODO add your handling code here:
    callView("DebitBankChargesAcHd");  
}//GEN-LAST:event_btnDebitBankChargesAcHdActionPerformed

    private void chkCollectCommFrmCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkCollectCommFrmCustomerActionPerformed
        // TODO add your handling code here:
        if (chkCollectCommFrmCustomer.isSelected()) {

            rdoGlAcHd.setEnabled(true);
            rdoInvestmentAcHd.setEnabled(true);
        } else {
            rdoGlAcHd.setEnabled(false);
            rdoInvestmentAcHd.setEnabled(false);
        }
    }//GEN-LAST:event_chkCollectCommFrmCustomerActionPerformed

    private void rdoGlAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGlAcHdActionPerformed
        // TODO add your handling code here:
        if (rdoGlAcHd.isSelected()) {
            txtOBCCommAcHd.setEnabled(true);
            btnOBCCommAcHd.setEnabled(true);
        } else {
            txtOBCCommAcHd.setEnabled(false);
            btnOBCCommAcHd.setEnabled(false);
        }
    }//GEN-LAST:event_rdoGlAcHdActionPerformed

    private void rdoInvestmentAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInvestmentAcHdActionPerformed
        // TODO add your handling code here:
        if (rdoInvestmentAcHd.isSelected()) {
            txtOBCCommAcHd.setText("");
            txtOBCCommAcHd.setEnabled(false);
            btnOBCCommAcHd.setEnabled(false);
        }
    }//GEN-LAST:event_rdoInvestmentAcHdActionPerformed

    private void btnDrBkChargeAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDrBkChargeAccountHeadActionPerformed
        callView("BankDrChargesAcHd");        // TODO add your handling code here:
    }//GEN-LAST:event_btnDrBkChargeAccountHeadActionPerformed

    private void btnMisBkChargeAccountHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMisBkChargeAccountHeadActionPerformed
        callView("BankMisChargesAcHd");
        // TODO add your handling code here:
    }//GEN-LAST:event_btnMisBkChargeAccountHeadActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankChargesAcHd;
    private com.see.truetransact.uicomponent.CButton btnBillsRealisedHead;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnChargesAccountHead;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCommissionAccountHead;
    private com.see.truetransact.uicomponent.CButton btnContractCrAccountHead;
    private com.see.truetransact.uicomponent.CButton btnContractDrAccountHead;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnDDAccountHead;
    private com.see.truetransact.uicomponent.CButton btnDebitBankChargesAcHd;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDrBkChargeAccountHead;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnGLAccountHead;
    private com.see.truetransact.uicomponent.CButton btnIBRAccountHead;
    private com.see.truetransact.uicomponent.CButton btnInterestAccountHead;
    private com.see.truetransact.uicomponent.CButton btnMarginAccountHead;
    private com.see.truetransact.uicomponent.CButton btnMisBkChargeAccountHead;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOBCCommAcHd;
    private com.see.truetransact.uicomponent.CButton btnOthersHead;
    private com.see.truetransact.uicomponent.CButton btnPostageAccountHead;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnServiceTaxHd;
    private com.see.truetransact.uicomponent.CButton btnTabDelete;
    private com.see.truetransact.uicomponent.CButton btnTabNew;
    private com.see.truetransact.uicomponent.CButton btnTabSave;
    private com.see.truetransact.uicomponent.CButton btnTelChargesHead;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_ICC_No;
    private com.see.truetransact.uicomponent.CRadioButton cRadio_ICC_Yes;
    private com.see.truetransact.uicomponent.CComboBox cboBaseCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboCustCategory;
    private com.see.truetransact.uicomponent.CComboBox cboInstrumentType;
    private com.see.truetransact.uicomponent.CComboBox cboIntDays;
    private com.see.truetransact.uicomponent.CComboBox cboOperatesLike;
    private com.see.truetransact.uicomponent.CComboBox cboRateType;
    private com.see.truetransact.uicomponent.CComboBox cboRegType;
    private com.see.truetransact.uicomponent.CComboBox cboSubRegType;
    private com.see.truetransact.uicomponent.CComboBox cboTransitPeriod;
    private com.see.truetransact.uicomponent.CCheckBox chkCollectCommFrmCustomer;
    private com.see.truetransact.uicomponent.CLabel lblAtParLimit;
    private com.see.truetransact.uicomponent.CLabel lblBaseCurrency;
    private com.see.truetransact.uicomponent.CLabel lblBillsBankChargesHD;
    private com.see.truetransact.uicomponent.CLabel lblBillsDebitBankChargesHD;
    private com.see.truetransact.uicomponent.CLabel lblBillsRealisedHD;
    private com.see.truetransact.uicomponent.CLabel lblBillsRealisedHD1;
    private com.see.truetransact.uicomponent.CLabel lblChargeType;
    private com.see.truetransact.uicomponent.CLabel lblChargesAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblCleanBillsPurchased;
    private com.see.truetransact.uicomponent.CLabel lblCleanBills_Per;
    private com.see.truetransact.uicomponent.CLabel lblCollectCommFrmCustomer;
    private com.see.truetransact.uicomponent.CLabel lblCommision;
    private com.see.truetransact.uicomponent.CLabel lblCommissionAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblContraAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblContractCrAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblContractDrAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblCreditOtherBankCommision;
    private com.see.truetransact.uicomponent.CLabel lblCustCategory;
    private com.see.truetransact.uicomponent.CLabel lblCustCategory1;
    private com.see.truetransact.uicomponent.CLabel lblCustCategory2;
    private com.see.truetransact.uicomponent.CLabel lblDDAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage_Per;
    private com.see.truetransact.uicomponent.CLabel lblDefaultPostage_Per1;
    private com.see.truetransact.uicomponent.CLabel lblDiscountRateBills_Per;
    private com.see.truetransact.uicomponent.CLabel lblDiscountRateOfBD;
    private com.see.truetransact.uicomponent.CLabel lblFixedRate;
    private com.see.truetransact.uicomponent.CLabel lblForEvery;
    private com.see.truetransact.uicomponent.CLabel lblFromSlab;
    private com.see.truetransact.uicomponent.CLabel lblGLAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblIBRAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblInstrument;
    private com.see.truetransact.uicomponent.CLabel lblIntDays;
    private com.see.truetransact.uicomponent.CLabel lblIntICC;
    private com.see.truetransact.uicomponent.CLabel lblInterestAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblMarginAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMsg1;
    private com.see.truetransact.uicomponent.CLabel lblOperatesLike;
    private com.see.truetransact.uicomponent.CLabel lblOthersHead;
    private com.see.truetransact.uicomponent.CLabel lblOverdueInterestForBD;
    private com.see.truetransact.uicomponent.CLabel lblOverdueRateBills_Per;
    private com.see.truetransact.uicomponent.CLabel lblOverdueRateCBP;
    private com.see.truetransact.uicomponent.CLabel lblPostDtdCheqAllowed;
    private com.see.truetransact.uicomponent.CLabel lblPostageAccountHead;
    private com.see.truetransact.uicomponent.CLabel lblProdDesc;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblRateForCBP_Per;
    private com.see.truetransact.uicomponent.CLabel lblRateForDelay;
    private com.see.truetransact.uicomponent.CLabel lblRegisterType;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace27;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblStateCapMetroOthers;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubRegType;
    private com.see.truetransact.uicomponent.CLabel lblTelChargesHead;
    private com.see.truetransact.uicomponent.CLabel lblToSlab;
    private com.see.truetransact.uicomponent.CLabel lblTransitPeriod;
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
    private com.see.truetransact.uicomponent.CPanel panAcHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panAliasBrchRemittNumber;
    private com.see.truetransact.uicomponent.CPanel panBills;
    private com.see.truetransact.uicomponent.CPanel panBillsAcHeadDetails;
    private com.see.truetransact.uicomponent.CPanel panBillsCharges;
    private com.see.truetransact.uicomponent.CPanel panBillsDetails;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panCharges1;
    private com.see.truetransact.uicomponent.CPanel panCollectCommFrmCustomer;
    private com.see.truetransact.uicomponent.CPanel panDiscountRate;
    private com.see.truetransact.uicomponent.CPanel panDiscountRate1;
    private com.see.truetransact.uicomponent.CPanel panIntDays;
    private com.see.truetransact.uicomponent.CPanel panIntICC;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills1;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills2;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills3;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills4;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills5;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills6;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills7;
    private com.see.truetransact.uicomponent.CPanel panOverdueRateBills8;
    private com.see.truetransact.uicomponent.CPanel panProductId;
    private com.see.truetransact.uicomponent.CPanel panRate;
    private com.see.truetransact.uicomponent.CPanel panRemitProdCharges;
    private com.see.truetransact.uicomponent.CPanel panRemitProdChargesButtons;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTransitPeriod;
    private com.see.truetransact.uicomponent.CPanel panTransitPeriod1;
    private com.see.truetransact.uicomponent.CPanel panrdoContraAccountHead;
    private com.see.truetransact.uicomponent.CPanel panrdoContraAccountHead2;
    private com.see.truetransact.uicomponent.CPanel panrdoPostDtdCheqAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdoContraAccountHead;
    private com.see.truetransact.uicomponent.CRadioButton rdoContraAccountHead_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoContraAccountHead_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCreditOtherBankComm;
    private com.see.truetransact.uicomponent.CRadioButton rdoGlAcHd;
    private com.see.truetransact.uicomponent.CButtonGroup rdoICCInt;
    private com.see.truetransact.uicomponent.CRadioButton rdoInvestmentAcHd;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDtdCheqAllowed;
    private com.see.truetransact.uicomponent.CRadioButton rdoPostDtdCheqAllowed_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPostDtdCheqAllowed_Yes;
    private com.see.truetransact.uicomponent.CScrollPane sprRemitProdCharges;
    private com.see.truetransact.uicomponent.CSeparator sptBills_Vert;
    private javax.swing.JSeparator sptNew;
    private javax.swing.JSeparator sptSave;
    private com.see.truetransact.uicomponent.CTabbedPane tabBills;
    private com.see.truetransact.uicomponent.CTable tblBillsCharges;
    private javax.swing.JToolBar tbrBills;
    private com.see.truetransact.uicomponent.CDateField tdtEndDt;
    private com.see.truetransact.uicomponent.CDateField tdtStartDt;
    private com.see.truetransact.uicomponent.CTextField txtAtParLimit;
    private com.see.truetransact.uicomponent.CTextField txtBankChargesAcHd;
    private com.see.truetransact.uicomponent.CTextField txtBillsRealisedHead;
    private com.see.truetransact.uicomponent.CTextField txtChargesAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtCleanBills;
    private com.see.truetransact.uicomponent.CTextField txtCommision;
    private com.see.truetransact.uicomponent.CTextField txtCommissionAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtContractCrAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtContractDrAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtDDAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtDebitBankChargesAcHd;
    private com.see.truetransact.uicomponent.CTextField txtDefaultPostage;
    private com.see.truetransact.uicomponent.CTextField txtDiscountRateBills;
    private com.see.truetransact.uicomponent.CTextField txtDrBkChargeAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtFixRate;
    private com.see.truetransact.uicomponent.CTextField txtForEvery;
    private com.see.truetransact.uicomponent.CTextField txtFromSlab;
    private com.see.truetransact.uicomponent.CTextField txtGLAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtIBRAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtIntDays;
    private com.see.truetransact.uicomponent.CTextField txtInterestAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtMarginAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtMisBkChargeAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtOBCCommAcHd;
    private com.see.truetransact.uicomponent.CTextField txtOthersHead;
    private com.see.truetransact.uicomponent.CTextField txtOverdueRateBills;
    private com.see.truetransact.uicomponent.CTextField txtPostageAccountHead;
    private com.see.truetransact.uicomponent.CTextField txtProdDesc;
    private com.see.truetransact.uicomponent.CTextField txtProductId;
    private com.see.truetransact.uicomponent.CTextField txtRateForCBP;
    private com.see.truetransact.uicomponent.CTextField txtRateForDelay;
    private com.see.truetransact.uicomponent.CTextField txtRateVal;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    private com.see.truetransact.uicomponent.CTextField txtServiceTaxHd;
    private com.see.truetransact.uicomponent.CTextField txtTelChargesHead;
    private com.see.truetransact.uicomponent.CTextField txtToSlab;
    private com.see.truetransact.uicomponent.CTextField txtTransitPeriod;
    // End of variables declaration//GEN-END:variables
    
}
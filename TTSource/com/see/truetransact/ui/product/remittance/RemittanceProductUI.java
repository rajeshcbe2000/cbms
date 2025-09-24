  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * RemittanceProductUI.java
   *
   * Created on November 26, 2003, 11:27 AM
   */

package com.see.truetransact.ui.product.remittance;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;
import com.see.truetransact.ui.product.remittance.RemittanceProductMRB;
import com.see.truetransact.ui.product.remittance.RemittanceProductRB;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;// To add and Remove the Radio Buttons...
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.uicomponent.CComboBox;
import java.util.*;
import com.see.truetransact.transferobject.generalledger.AccountMaintenanceTO;

/**
 *
 * @author Hemant
 * Modification Lohith R.
 *@modified : Sunil
 *      Added Edit Locking - 08-07-2005
 */

public class RemittanceProductUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    private final static ClientParseException parseException = ClientParseException.getInstance();
    //    private RemittanceProductRB resourceBundle = new RemittanceProductRB();
    
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.remittance.RemittanceProductRB", ProxyParameters.LANGUAGE);
    final RemittanceProductMRB objMandatoryMRB = new RemittanceProductMRB();
    
    private HashMap mandatoryMap;
    private RemittanceProductOB observable;
    //    private RemittanceProductMRB objMandatoryMRB;
    private String viewType = "";
    private final String AUTHORIZE="AUTHORIZE";
    private int RemitProdBrchResult;
    private int RemitProdChrgResult;
    
    private boolean isFilled = false;
    private boolean actionAuthExcepReject = false;
    private boolean tblAliasSelected = false;
    private boolean tblChargesSelected = false;
    private String accountHead = "ACCOUNT HEAD ID";
    private String bankCode = "";
    private Date currDt = null;
    
    /** Creates new form BeanForm */
    public RemittanceProductUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        settingupUI();
        // Hide product currency
        lblProdCurrency.setVisible(false);
        cboProdCurrency.setVisible(false);
        tabRemittanceProduct.resetVisits();
    }
    
    private void settingupUI(){
        setObservable();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        initComponentData();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panGeneralRemittance);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panBranchAndRttNoRemittance);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panCharges);
        setMaximumLength();
        setHelpMessage();
        setButtonEnableDisable();
        setRemitProdBrchBtnEndableDisable();
        setRemitProdChrgBtnEndableDisable();
        ClientUtil.enableDisable(this,false);
        //        enableDisableAliasBranchTable(false);// To Enable Disable the Branch Table in the Charges tab screen
        observable.resetStatus();
        resetBranchTable();
        resetChargeTable();
        observable.resetOBFields();
        panSeriesGR.setVisible(false);
        lblSeriesGR.setVisible(false);
        panNewProcedurePan.setVisible(false);//Added by kannan
        enabledisableRTGSFields(false); //Added By Suresh
        setVisbleRTGSSuspenceHead(false); //added by abi        
    }
    
    private void setObservable() {
        observable = RemittanceProductOB.getInstance();
        observable.addObserver(this);
    }
    
    private void enabledisableRTGSFields(boolean flag){
        lblMaximumAmount.setVisible(flag);
        txtMaximumAmount.setVisible(flag);
        lblMinimumAmount.setVisible(flag);
        txtMinimumAmount.setVisible(flag);
        if(flag==false){
           txtMaximumAmount.setText("");
           txtMinimumAmount.setText("");
        }
    }
    
    /** Auto Generated Method - setFieldNames()
     * This method assigns name for all the components.
     * Other functions are working based on this name. */
    private void setFieldNames() {
        txtPercentage.setName("txtPercentage");
        txtServiceTax.setName("txtServiceTax");
        txtRateVal.setName("txtRateVal");
        txtForEvery.setName("txtForEvery");
        cboRateType.setName("cboRateType");
        lblForEvery.setName("lblForEvery");
        lblPercentage.setName("lblPercentage");
        btnAuthorize.setName("btnAuthorize");
        btnBankCode.setName("btnBankCode");
        btnBranchName.setName("btnBranchName");
        btnBrchRemittNumberDelete.setName("btnBrchRemittNumberDelete");
        btnBrchRemittNumberNew.setName("btnBrchRemittNumberNew");
        btnBrchRemittNumberSave.setName("btnBrchRemittNumberSave");
        btnCCHeadGRHelp.setName("btnCCHeadGRHelp");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDCHeadGRHelp.setName("btnDCHeadGRHelp");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnExchangeHeadGRHelp.setName("btnExchangeHeadGRHelp");
        btnIssueHeadGRHelp.setName("btnIssueHeadGRHelp");
        btnLapsedHeadGRHelp.setName("btnLapsedHeadGRHelp");
        btnNew.setName("btnNew");
        btnOCHeadGRHelp.setName("btnOCHeadGRHelp");
        btnPayableHeadGRHelp.setName("btnPayableHeadGRHelp");
        btnPostageHeadGRHelp.setName("btnPostageHeadGRHelp");
        btnPrint.setName("btnPrint");
        btnRCHeadGRHelp.setName("btnRCHeadGRHelp");
        btnReject.setName("btnReject");
        btnRemitProdChargesDelete.setName("btnRemitProdChargesDelete");
        btnRemitProdChargesNew.setName("btnRemitProdChargesNew");
        btnRemitProdChargesSave.setName("btnRemitProdChargesSave");
        btnSave.setName("btnSave");
        btnTCHeadGRHelp.setName("btnTCHeadGRHelp");
        cboBehavesLike.setName("cboBehavesLike");
        cboCategory.setName("cboCategory");
        cboChargeType.setName("cboChargeType");
        cboLapsedPeriodGR.setName("cboLapsedPeriodGR");
        cboPayableBranchGR.setName("cboPayableBranchGR");
        cboProdCurrency.setName("cboProdCurrency");
        cboRttTypeBR.setName("cboRttTypeBR");
        lblAmtRangeFrom.setName("lblAmtRangeFrom");
        lblAmtRangeTo.setName("lblAmtRangeTo");
        lblBankCode.setName("lblBankCode");
        lblBehavesLike.setName("lblBehavesLike");
        lblBranchCodeBR.setName("lblBranchCodeBR");
        lblBranchNameBR.setName("lblBranchNameBR");
        lblCCHeadGR.setName("lblCCHeadGR");
        lblRTGSSuspenseHead.setName("lblRTGSSuspenseHead");
        lblRTGSSuspenseHeadDesc.setName("lblRTGSSuspenseHeadDesc");      
        lblCashLimitGR.setName("lblCashLimitGR");
        lblCategory.setName("lblCategory");
        lblChargeType.setName("lblChargeType");
        lblCharges.setName("lblCharges");
        lblDCHeadGR.setName("lblDCHeadGR");
        lblEFTProductGR.setName("lblEFTProductGR");
        lblExchangeHeadGR.setName("lblExchangeHeadGR");
        lblIVNoRR.setName("lblIVNoRR");
        lblIsLapsedGR.setName("lblIsLapsedGR");
        lblIssueHeadGR.setName("lblIssueHeadGR");
        lblLapsedHeadGR.setName("lblLapsedHeadGR");
        lblLapsedPeriodGR.setName("lblLapsedPeriodGR");
        lblMaxAmtRR.setName("lblMaxAmtRR");
        lblMinAmtRR.setName("lblMinAmtRR");
        lblMsg.setName("lblMsg");
        lblNumberPattern.setName("lblNumberPattern");
        lblOCHeadGR.setName("lblOCHeadGR");
        lblOVNoRR.setName("lblOVNoRR");
        lblPayableBranchGR.setName("lblPayableBranchGR");
        lblPayableHeadGR.setName("lblPayableHeadGR");
        lblPostageHeadGR.setName("lblPostageHeadGR");
        lblPrintServicesGR.setName("lblPrintServicesGR");
        lblProdCurrency.setName("lblProdCurrency");
        lblProdDescRemitProdBranch.setName("lblProdDescRemitProdBranch");
        lblProdDescRemitProdBrch.setName("lblProdDescRemitProdBrch");
        lblProdDescRemitProdCharge.setName("lblProdDescRemitProdCharge");
        lblProdDescRemitProdChrg.setName("lblProdDescRemitProdChrg");
        lblProdIDRemitProdBranch.setName("lblProdIDRemitProdBranch");
        lblProdIDRemitProdBrch.setName("lblProdIDRemitProdBrch");
        lblProdIDRemitProdCharge.setName("lblProdIDRemitProdCharge");
        lblProdIDRemitProdChrg.setName("lblProdIDRemitProdChrg");
        lblProductDescGR.setName("lblProductDescGR");
        lblChargesBankCode.setName("lblChargesBankCode");
        lblChargesBranchCode.setName("lblChargesBranchCode");
        lblDisplayBranchCode.setName("lblDisplayBranchCode");
        lblDisplayBankCode.setName("lblDisplayBankCode");
        lblProductIdGR.setName("lblProductIdGR");
        lblRCHeadGR.setName("lblRCHeadGR");
        lblRemarksGR.setName("lblRemarksGR");
        lblRttLimitBR.setName("lblRttLimitBR");
        lblRttTypeBR.setName("lblRttTypeBR");
        lblSeriesGR.setName("lblSeriesGR");
        lblSpace.setName("lblSpace");
//        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblStatus.setName("lblStatus");
        lblTCHeadGR.setName("lblTCHeadGR");
        mbrMain.setName("mbrMain");
        panBranchAndRttNoRemittance.setName("panBranchAndRttNoRemittance");
        panBranchRemittance.setName("panBranchRemittance");
        panBrchRemittNumberButton.setName("panBrchRemittNumberButton");
        panBrchRemittance.setName("panBrchRemittance");
        panCharges.setName("panCharges");
        panEFTProductGR.setName("panEFTProductGR");
        panGeneralRemittance.setName("panGeneralRemittance");
        panInsideGeneralRemittance.setName("panInsideGeneralRemittance");
        panIsLapsedGR.setName("panIsLapsedGR");
        panLapsedGR.setName("panLapsedGR");
        panLapsedHeadGR.setName("panLapsedHeadGR");
        panLapsedPeriodGR.setName("panLapsedPeriodGR");
        panNumberPattern.setName("panNumberPattern");
        panOtherGR.setName("panOtherGR");
        panPrintServicesGR.setName("panPrintServicesGR");
        panProdDescGeneralRemittance.setName("panProdDescGeneralRemittance");
        panProdDescRemitProdBranch.setName("panProdDescRemitProdBranch");
        panProdDescRemitProdCharges.setName("panProdDescRemitProdCharges");
        panRemitBankCode.setName("panRemitBankCode");
        panRemitProdCharges.setName("panRemitProdCharges");
        panRemitProdChargesButtons.setName("panRemitProdChargesButtons");
        panRemitProdChargesFields.setName("panRemitProdChargesFields");
        panRemitType.setName("panRemitType");
        panSeriesGR.setName("panSeriesGR");
        panStatus.setName("panStatus");
        rdoEFTProductGR_No.setName("rdoEFTProductGR_No");
        rdoEFTProductGR_Yes.setName("rdoEFTProductGR_Yes");
        rdoIsLapsedGR_No.setName("rdoIsLapsedGR_No");
        rdoIsLapsedGR_Yes.setName("rdoIsLapsedGR_Yes");
        rdoPrintServicesGR_No.setName("rdoPrintServicesGR_No");
        rdoPrintServicesGR_Yes.setName("rdoPrintServicesGR_Yes");
        rdoSeriesGR_No.setName("rdoSeriesGR_No");
        rdoSeriesGR_Yes.setName("rdoSeriesGR_Yes");
        sprRemitProdCharges.setName("sprRemitProdCharges");
        sptGeneralRemittanceH.setName("sptGeneralRemittanceH");
        sptGeneralRemittanceL.setName("sptGeneralRemittanceL");
        sptGeneralRemittanceV.setName("sptGeneralRemittanceV");
        sptProdDescRemitProdBranch.setName("sptProdDescRemitProdBranch");
        sptProdDescRemitProdCharges.setName("sptProdDescRemitProdCharges");
        sptRemitButtons.setName("sptRemitButtons");
        sptRemitProdChargesFieldsAndButtons.setName("sptRemitProdChargesFieldsAndButtons");
        srpBrchRemittNumber.setName("srpBrchRemittNumber");
        tabRemittanceProduct.setName("tabRemittanceProduct");
        tblBrchRemittNumber.setName("tblBrchRemittNumber");
        tblRemitProdCharges.setName("tblRemitProdCharges");
        txtBankCode.setName("txtBankCode");
        txtBranchCodeBR.setName("txtBranchCodeBR");
        txtBranchName.setName("txtBranchName");
        txtCCHeadGR.setName("txtCCHeadGR");
        txtRTGSSuspenseHead.setName("txtRTGSSuspenseHead");
        txtCashLimitGR.setName("txtCashLimitGR");
        txtMaximumAmount.setName("txtMaximumAmount");
        txtMinimumAmount.setName("txtMinimumAmount");
        txtAmtRangeFrom.setName("txtAmtRangeFrom");
        txtAmtRangeTo.setName("txtAmtRangeTo");
        txtCharges.setName("txtCharges");
        txtDCHeadGR.setName("txtDCHeadGR");
        txtExchangeHeadGR.setName("txtExchangeHeadGR");
        txtIVNoRR.setName("txtIVNoRR");
        txtIssueHeadGR.setName("txtIssueHeadGR");
        txtLapsedHeadGR.setName("txtLapsedHeadGR");
        txtLapsedPeriodGR.setName("txtLapsedPeriodGR");
        txtMaxAmtRR.setName("txtMaxAmtRR");
        txtMinAmtRR.setName("txtMinAmtRR");
        txtOCHeadGR.setName("txtOCHeadGR");
        txtOVNoRR.setName("txtOVNoRR");
        txtPayableHeadGR.setName("txtPayableHeadGR");
        txtPerfix.setName("txtPerfix");
        txtPostageHeadGR.setName("txtPostageHeadGR");
        txtProductDescGR.setName("txtProductDescGR");
        txtProductIdGR.setName("txtProductIdGR");
        txtRCHeadGR.setName("txtRCHeadGR");
        txtRemarksGR.setName("txtRemarksGR");
        txtRttLimitBR.setName("txtRttLimitBR");
        txtSuffix.setName("txtSuffix");
        txtTCHeadGR.setName("txtTCHeadGR");
    }
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        btnOCHeadGRHelp.setText(resourceBundle.getString("btnOCHeadGRHelp"));
        lblForEvery.setText(resourceBundle.getString("lblForEvery"));
        lblPercentage.setText(resourceBundle.getString("lblPercentage"));
        rdoEFTProductGR_No.setText(resourceBundle.getString("rdoEFTProductGR_No"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        btnBrchRemittNumberDelete.setText(resourceBundle.getString("btnBrchRemittNumberDelete"));
        lblAmtRangeTo.setText(resourceBundle.getString("lblAmtRangeTo"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        lblProdCurrency.setText(resourceBundle.getString("lblProdCurrency"));
        lblBranchNameBR.setText(resourceBundle.getString("lblBranchNameBR"));
        lblProdIDRemitProdChrg.setText(resourceBundle.getString("lblProdIDRemitProdChrg"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblDCHeadGR.setText(resourceBundle.getString("lblDCHeadGR"));
        btnRemitProdChargesDelete.setText(resourceBundle.getString("btnRemitProdChargesDelete"));
        lblProdDescRemitProdCharge.setText(resourceBundle.getString("lblProdDescRemitProdCharge"));
        lblMinAmtRR.setText(resourceBundle.getString("lblMinAmtRR"));
        btnRemitProdChargesSave.setText(resourceBundle.getString("btnRemitProdChargesSave"));
        lblPayableHeadGR.setText(resourceBundle.getString("lblPayableHeadGR"));
        lblBranchCodeBR.setText(resourceBundle.getString("lblBranchCodeBR"));
        lblBehavesLike.setText(resourceBundle.getString("lblBehavesLike"));
        btnBankCode.setText(resourceBundle.getString("btnBankCode"));
        lblLapsedHeadGR.setText(resourceBundle.getString("lblLapsedHeadGR"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        ((javax.swing.border.TitledBorder)panRemitProdCharges.getBorder()).setTitle(resourceBundle.getString("panRemitProdCharges"));
        lblChargeType.setText(resourceBundle.getString("lblChargeType"));
        rdoSeriesGR_Yes.setText(resourceBundle.getString("rdoSeriesGR_Yes"));
        btnDCHeadGRHelp.setText(resourceBundle.getString("btnDCHeadGRHelp"));
        lblProdDescRemitProdBrch.setText(resourceBundle.getString("lblProdDescRemitProdBrch"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnPostageHeadGRHelp.setText(resourceBundle.getString("btnPostageHeadGRHelp"));
        lblIssueHeadGR.setText(resourceBundle.getString("lblIssueHeadGR"));
        lblExchangeHeadGR.setText(resourceBundle.getString("lblExchangeHeadGR"));
        rdoIsLapsedGR_No.setText(resourceBundle.getString("rdoIsLapsedGR_No"));
        rdoSeriesGR_No.setText(resourceBundle.getString("rdoSeriesGR_No"));
        btnException.setText(resourceBundle.getString("btnException"));
        lblOVNoRR.setText(resourceBundle.getString("lblOVNoRR"));
        lblProductDescGR.setText(resourceBundle.getString("lblProductDescGR"));
        rdoEFTProductGR_Yes.setText(resourceBundle.getString("rdoEFTProductGR_Yes"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblProdDescRemitProdChrg.setText(resourceBundle.getString("lblProdDescRemitProdChrg"));
        lblPayableBranchGR.setText(resourceBundle.getString("lblPayableBranchGR"));
        lblCharges.setText(resourceBundle.getString("lblCharges"));
        lblRCHeadGR.setText(resourceBundle.getString("lblRCHeadGR"));
        lblProdDescRemitProdBranch.setText(resourceBundle.getString("lblProdDescRemitProdBranch"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnExchangeHeadGRHelp.setText(resourceBundle.getString("btnExchangeHeadGRHelp"));
        lblAmtRangeFrom.setText(resourceBundle.getString("lblAmtRangeFrom"));
        btnPayableHeadGRHelp.setText(resourceBundle.getString("btnPayableHeadGRHelp"));
        lblEFTProductGR.setText(resourceBundle.getString("lblEFTProductGR"));
        lblProductIdGR.setText(resourceBundle.getString("lblProductIdGR"));
        lblProdIDRemitProdBranch.setText(resourceBundle.getString("lblProdIDRemitProdBranch"));
        lblRttTypeBR.setText(resourceBundle.getString("lblRttTypeBR"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        ((javax.swing.border.TitledBorder)panBranchRemittance.getBorder()).setTitle(resourceBundle.getString("panBranchRemittance"));
//        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        rdoPrintServicesGR_No.setText(resourceBundle.getString("rdoPrintServicesGR_No"));
        lblCashLimitGR.setText(resourceBundle.getString("lblCashLimitGR"));
        lblLapsedPeriodGR.setText(resourceBundle.getString("lblLapsedPeriodGR"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblProdIDRemitProdCharge.setText(resourceBundle.getString("lblProdIDRemitProdCharge"));
        lblChargesBankCode.setText(resourceBundle.getString("lblChargesBankCode"));
        lblChargesBranchCode.setText(resourceBundle.getString("lblChargesBranchCode"));
        lblDisplayBranchCode.setText(resourceBundle.getString("lblDisplayBranchCode"));
        lblDisplayBankCode.setText(resourceBundle.getString("lblDisplayBankCode"));
        lblProdIDRemitProdBrch.setText(resourceBundle.getString("lblProdIDRemitProdBrch"));
        btnBrchRemittNumberNew.setText(resourceBundle.getString("btnBrchRemittNumberNew"));
        lblSeriesGR.setText(resourceBundle.getString("lblSeriesGR"));
        lblIsLapsedGR.setText(resourceBundle.getString("lblIsLapsedGR"));
        rdoPrintServicesGR_Yes.setText(resourceBundle.getString("rdoPrintServicesGR_Yes"));
        btnLapsedHeadGRHelp.setText(resourceBundle.getString("btnLapsedHeadGRHelp"));
        btnRCHeadGRHelp.setText(resourceBundle.getString("btnRCHeadGRHelp"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblRttLimitBR.setText(resourceBundle.getString("lblRttLimitBR"));
        lblMaxAmtRR.setText(resourceBundle.getString("lblMaxAmtRR"));
        rdoIsLapsedGR_Yes.setText(resourceBundle.getString("rdoIsLapsedGR_Yes"));
        lblIVNoRR.setText(resourceBundle.getString("lblIVNoRR"));
        lblPrintServicesGR.setText(resourceBundle.getString("lblPrintServicesGR"));
        lblRemarksGR.setText(resourceBundle.getString("lblRemarksGR"));
        lblNumberPattern.setText(resourceBundle.getString("lblNumberPattern"));
        btnBranchName.setText(resourceBundle.getString("btnBranchName"));
        btnIssueHeadGRHelp.setText(resourceBundle.getString("btnIssueHeadGRHelp"));
        lblTCHeadGR.setText(resourceBundle.getString("lblTCHeadGR"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblPostageHeadGR.setText(resourceBundle.getString("lblPostageHeadGR"));
        lblSpace5.setText(resourceBundle.getString("lblSpace5"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnTCHeadGRHelp.setText(resourceBundle.getString("btnTCHeadGRHelp"));
        btnCCHeadGRHelp.setText(resourceBundle.getString("btnCCHeadGRHelp"));
        btnRTGSSuspenseHead.setText(resourceBundle.getString("btnRTGSSuspenseHeadHelp"));
        btnRemitProdChargesNew.setText(resourceBundle.getString("btnRemitProdChargesNew"));
        btnBrchRemittNumberSave.setText(resourceBundle.getString("btnBrchRemittNumberSave"));
        lblOCHeadGR.setText(resourceBundle.getString("lblOCHeadGR"));
        lblBankCode.setText(resourceBundle.getString("lblBankCode"));
        lblCCHeadGR.setText(resourceBundle.getString("lblCCHeadGR"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtIssueHeadGR", new Boolean(true));
        mandatoryMap.put("txtExchangeHeadGR", new Boolean(false));
        mandatoryMap.put("txtPayableHeadGR", new Boolean(false));
        mandatoryMap.put("txtRCHeadGR", new Boolean(false));
        mandatoryMap.put("txtTCHeadGR", new Boolean(false));
        mandatoryMap.put("txtOCHeadGR", new Boolean(false));
        mandatoryMap.put("txtCCHeadGR", new Boolean(false));
        mandatoryMap.put("txtRTGSSuspenseHead", new Boolean(false));
        mandatoryMap.put("txtDCHeadGR", new Boolean(false));
        mandatoryMap.put("txtPostageHeadGR", new Boolean(true));
        mandatoryMap.put("cboProdCurrency", new Boolean(true));
        mandatoryMap.put("txtProductIdGR", new Boolean(true));
        mandatoryMap.put("txtProductDescGR", new Boolean(true));
        mandatoryMap.put("cboLapsedPeriodGR", new Boolean(false));
        mandatoryMap.put("txtLapsedPeriodGR", new Boolean(false));
        mandatoryMap.put("rdoIsLapsedGR_Yes", new Boolean(true));
        mandatoryMap.put("txtLapsedHeadGR", new Boolean(false));
        mandatoryMap.put("rdoEFTProductGR_Yes", new Boolean(true));
        mandatoryMap.put("rdoPrintServicesGR_Yes", new Boolean(true));
        mandatoryMap.put("rdoSeriesGR_Yes", new Boolean(true));
        mandatoryMap.put("txtCashLimitGR", new Boolean(false));
        mandatoryMap.put("txtMaximumAmount", new Boolean(false));
        mandatoryMap.put("txtMinimumAmount", new Boolean(false));
        mandatoryMap.put("txtRemarksGR", new Boolean(false));
        mandatoryMap.put("txtPerfix", new Boolean(false));
        mandatoryMap.put("txtSuffix", new Boolean(false));
        mandatoryMap.put("cboPayableBranchGR", new Boolean(true));
        mandatoryMap.put("txtBankCode", new Boolean(true));
        mandatoryMap.put("txtBranchName", new Boolean(true));
        mandatoryMap.put("txtBranchCodeBR", new Boolean(true));
        mandatoryMap.put("cboRttTypeBR", new Boolean(true));
        mandatoryMap.put("txtRttLimitBR", new Boolean(true));
        mandatoryMap.put("txtIVNoRR", new Boolean(true));
        mandatoryMap.put("txtOVNoRR", new Boolean(true));
        mandatoryMap.put("txtMinAmtRR", new Boolean(false));
        mandatoryMap.put("txtMaxAmtRR", new Boolean(false));
        mandatoryMap.put("cboChargeType", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("txtAmtRangeFrom", new Boolean(true));
        mandatoryMap.put("txtAmtRangeTo", new Boolean(true));
        mandatoryMap.put("cboBehavesLike", new Boolean(true));
        mandatoryMap.put("txtCharges", new Boolean(false));
        mandatoryMap.put("txtPercentage", new Boolean(false));
        mandatoryMap.put("txtServiceTax", new Boolean(false));
        mandatoryMap.put("txtRateVal", new Boolean(false));
        mandatoryMap.put("txtForEvery", new Boolean(false));
        mandatoryMap.put("cboRateType", new Boolean(false));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void initComponentData() {
        try{
            //            cboProdCurrency.setModel(observable.getCbmProdCurrency());
            cboLapsedPeriodGR.setModel(observable.getCbmLapsedPeriodGR());
            cboPayableBranchGR.setModel(observable.getCbmPayableBranchGR());
            cboRttTypeBR.setModel(observable.getCbmRttTypeBR());
            cboChargeType.setModel(observable.getCbmChargeType());
            cboCategory.setModel(observable.getCbmCategory());
            cboBehavesLike.setModel(observable.getCbmBehavesLike());
            cboRateType.setModel(observable.getCbmRateType());
            cboProdTypeCr.setModel(observable.getCbmProdTypeCr());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    private void setMaximumLength(){
        
        txtForEvery.setMaxLength(15);
        txtPercentage.setMaxLength(15);
        txtServiceTax.setMaxLength(15);
        txtRateVal.setMaxLength(15);
        
        txtForEvery.setValidation(new CurrencyValidation());
        txtPercentage.setValidation(new PercentageValidation());
        txtServiceTax.setValidation(new PercentageValidation());
        txtRateVal.setValidation(new CurrencyValidation(3,2));
        
        txtIVNoRR.setAllowNumber(true);
        txtOVNoRR.setAllowNumber(true);
        
        txtBranchCodeBR.setMaxLength(64);
        txtBranchName.setMaxLength(64);
        txtRttLimitBR.setMaxLength(16);
        txtRttLimitBR.setValidation(new CurrencyValidation(14, 2));
        txtIVNoRR.setMaxLength(16);
        txtOVNoRR.setMaxLength(16);
        txtMinAmtRR.setMaxLength(16);
        txtMinAmtRR.setValidation(new CurrencyValidation(14, 2));
        txtMaxAmtRR.setMaxLength(16);
        txtMaxAmtRR.setValidation(new CurrencyValidation(14, 2));
        txtProductIdGR.setMaxLength(8);
        txtProductDescGR.setMaxLength(128);
        txtIssueHeadGR.setMaxLength(16);
        txtExchangeHeadGR.setMaxLength(16);
        txtTCHeadGR.setMaxLength(16);
        txtRCHeadGR.setMaxLength(16);
        txtOCHeadGR.setMaxLength(16);
        txtLapsedHeadGR.setMaxLength(16);
        txtCashLimitGR.setMaxLength(16);
        txtCashLimitGR.setValidation(new CurrencyValidation(14, 2));
        txtPayableHeadGR.setMaxLength(16);
        txtPostageHeadGR.setMaxLength(16);
        txtDCHeadGR.setMaxLength(16);
        txtCCHeadGR.setMaxLength(16);
        txtRTGSSuspenseHead.setMaxLength(16);
        txtLapsedPeriodGR.setMaxLength(5);// 3
        txtLapsedPeriodGR.setValidation(new NumericValidation());
        txtPerfix.setMaxLength(16);
        txtPerfix.setValidation(new NumericValidation());
        txtRemarksGR.setMaxLength(128);
        txtSuffix.setMaxLength(16);
        txtSuffix.setValidation(new NumericValidation());
        txtBankCode.setMaxLength(16);
        txtCharges.setMaxLength(16);
        txtCharges.setValidation(new CurrencyValidation(14, 2));
        txtAmtRangeFrom.setMaxLength(16);
        txtAmtRangeFrom.setValidation(new CurrencyValidation(14, 2));
        txtAmtRangeTo.setMaxLength(16);
        txtAmtRangeTo.setValidation(new CurrencyValidation(14, 2));
        txtMinimumAmt.setMaxLength(16);
        txtMinimumAmt.setValidation(new CurrencyValidation(14, 2));
        txtMaxAmt.setMaxLength(16);
        txtMaxAmt.setValidation(new CurrencyValidation(14, 2));
        txtMaximumAmount.setMaxLength(16);
        txtMinimumAmount.setMaxLength(16);
        txtMaximumAmount.setValidation(new CurrencyValidation(14, 2));
        txtMinimumAmount.setValidation(new CurrencyValidation(14, 2));
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtIssueHeadGR.setText(observable.getTxtIssueHeadGR());
        txtExchangeHeadGR.setText(observable.getTxtExchangeHeadGR());
        txtPayableHeadGR.setText(observable.getTxtPayableHeadGR());
        txtRCHeadGR.setText(observable.getTxtRCHeadGR());
        txtTCHeadGR.setText(observable.getTxtTCHeadGR());
        txtOCHeadGR.setText(observable.getTxtOCHeadGR());
        txtCCHeadGR.setText(observable.getTxtCCHeadGR());
        txtRTGSSuspenseHead.setText(observable.getTxtRTGSSuspenseHead());
        txtDCHeadGR.setText(observable.getTxtDCHeadGR());
        txtPostageHeadGR.setText(observable.getTxtPostageHeadGR());
        cboRateType.setSelectedItem(observable.getCboRateType());
        //        cboProdCurrency.setSelectedItem(observable.getCboProdCurrency());
        txtProductIdGR.setText(observable.getTxtProductIdGR());
        txtProductDescGR.setText(observable.getTxtProductDescGR());
        cboLapsedPeriodGR.setSelectedItem(observable.getCboLapsedPeriodGR());
        txtLapsedPeriodGR.setText(observable.getTxtLapsedPeriodGR());
        rdoIsLapsedGR_Yes.setSelected(observable.getRdoIsLapsedGR_Yes());
        rdoIsLapsedGR_No.setSelected(observable.getRdoIsLapsedGR_No());
        txtLapsedHeadGR.setText(observable.getTxtLapsedHeadGR());
        rdoEFTProductGR_Yes.setSelected(observable.getRdoEFTProductGR_Yes());
        rdoEFTProductGR_No.setSelected(observable.getRdoEFTProductGR_No());
        cboPayableBranchGR.setSelectedItem(observable.getCboPayableBranchGR());
        rdoPrintServicesGR_Yes.setSelected(observable.getRdoPrintServicesGR_Yes());
        rdoPrintServicesGR_No.setSelected(observable.getRdoPrintServicesGR_No());
        rdoSeriesGR_Yes.setSelected(observable.getRdoSeriesGR_Yes());
        rdoSeriesGR_No.setSelected(observable.getRdoSeriesGR_No());
        txtCashLimitGR.setText(observable.getTxtCashLimitGR());
        txtMaximumAmount.setText(observable.getTxtMaximumAmount());
        txtMinimumAmount.setText(observable.getTxtMinimumAmount());
        txtRemarksGR.setText(observable.getTxtRemarksGR());
        txtPerfix.setText(observable.getTxtPerfix());
        txtSuffix.setText(observable.getTxtSuffix());
        txtBranchCodeBR.setText(observable.getTxtBranchCodeBR());
        cboRttTypeBR.setSelectedItem(observable.getCboRttTypeBR());
        txtRttLimitBR.setText(observable.getTxtRttLimitBR());
        txtIVNoRR.setText(observable.getTxtIVNoRR());
        txtOVNoRR.setText(observable.getTxtOVNoRR());
        txtMinAmtRR.setText(observable.getTxtMinAmtRR());
        txtMaxAmtRR.setText(observable.getTxtMaxAmtRR());
        txtBranchName.setText(observable.getTxtBranchName());
        txtBankCode.setText(observable.getTxtBankCode());
        tblBrchRemittNumber.setModel(observable.getTblBrchRemittNumber());
        tblAliasBrchRemittNumber.setModel(observable.getTblAliasRemittanceProductBranch());
        tblRemitProdCharges.setModel(observable.getTblRemittanceProductCharge());
        txtAmtRangeFrom.setText(observable.getTxtAmtRangeFrom());
        cboBehavesLike.setSelectedItem(observable.getCboBehavesLike());
        txtAmtRangeTo.setText(observable.getTxtAmtRangeTo());
        cboCategory.setSelectedItem(observable.getCboCategory());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
        txtCharges.setText(observable.getTxtCharges());
        lblStatus.setText(observable.getLblStatus());
        lblProdIDRemitProdBranch.setText(observable.getLblRemitProdIDBranch());
        lblProdIDRemitProdCharge.setText(observable.getLblRemitProdIDCharge());
        lblProdDescRemitProdBranch.setText(observable.getLblRemitProdDescBranch());
        lblProdDescRemitProdCharge.setText(observable.getLblRemitProdDescCharge());
        lblDisplayBankCode.setText(observable.getLblChargesBankCode());
        lblDisplayBranchCode.setText(observable.getLblChargesBranchCode());
        txtPercentage.setText(observable.getTxtPercent());
        txtServiceTax.setText(observable.getTxtServiceTax());
        txtForEvery.setText(observable.getTxtForEvery());
        txtRateVal.setText(observable.getTxtRateVal());
        txtMinimumAmt.setText(observable.getTxtMinAmt());
        txtMaxAmt.setText(observable.getTxtMaxAmt());
        chkNewProcedure.setSelected(observable.isChkNewProcedure()); //Added By Kannan
        if (observable.isChkNewProcedure()) {
            panNewProcedurePan.setVisible(true);
            tdtStartDate.setDateValue(observable.getTdtNewProcStartDt());
            txtDdIssueHead.setText(observable.getTxtNewProcIssueAcHd());
            btnDdIssueHead.setEnabled(true);
            txtDdIssueHead.setEnabled(false);
        } else {
            panNewProcedurePan.setVisible(false);
            tdtStartDate.setDateValue(null);
            txtDdIssueHead.setText("");
            btnDdIssueHead.setEnabled(false);
            txtDdIssueHead.setEnabled(false);
        }
        if(rdoEFTProductGR_Yes.isSelected()){
            enabledisableRTGSFields(true);
        }else{
            enabledisableRTGSFields(false);
        }
        if(observable.getRtgsNeftGLType()!= null && CommonUtil.convertObjToStr(observable.getRtgsNeftGLType()).equals("N")){
//            cboProdTypeCr.setSelectedItem(observable.getCboProdTypeCr());
            txtTransProductId.setText(observable.getTxtRtgsNeftProductId());
            txtAcctNo.setText(observable.getTxtRtgsNeftAcctNo());
            rdoInvestmentAcHd.setSelected(true);
            rdoGlAcHd.setSelected(false);
        }else{
            cboProdTypeCr.setSelectedItem("");
            txtTransProductId.setText("");
            txtAcctNo.setText("");
            rdoInvestmentAcHd.setSelected(false);
            rdoGlAcHd.setSelected(true);
        }
        validateEnableDisableRTGS();
        addRadioButtons();
    }
    private void updateChargesCombo() {
        cboCategory.setSelectedItem(observable.getCboCategory());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
//        cboAmtRangeFrom.setSelectedItem(observable.getCboAmtRangeFrom());
//        txtAmtRangeFrom.setText(observable.getTxtAmtRangeFrom());
//        txtAmtRangeTo.setText(observable.getTxtAmtRangeTo());
    }
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdgIsLapsedGR.remove(rdoIsLapsedGR_Yes);
        rdgIsLapsedGR.remove(rdoIsLapsedGR_No);
        rdgEFTProductGR.remove(rdoEFTProductGR_Yes);
        rdgEFTProductGR.remove(rdoEFTProductGR_No);
        rdgPrintServicesGR.remove(rdoPrintServicesGR_Yes);
        rdgPrintServicesGR.remove(rdoPrintServicesGR_No);
        rdgSeriesGR.remove(rdoSeriesGR_Yes);
        rdgSeriesGR.remove(rdoSeriesGR_No);
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdgIsLapsedGR = new CButtonGroup();
        
        
        rdgEFTProductGR = new CButtonGroup();
        rdgPrintServicesGR = new CButtonGroup();
        rdgSeriesGR = new CButtonGroup();
        rdgIsLapsedGR.add(rdoIsLapsedGR_Yes);
        rdgIsLapsedGR.add(rdoIsLapsedGR_No);
        rdgEFTProductGR.add(rdoEFTProductGR_Yes);
        rdgEFTProductGR.add(rdoEFTProductGR_No);
        rdgPrintServicesGR.add(rdoPrintServicesGR_Yes);
        rdgPrintServicesGR.add(rdoPrintServicesGR_No);
        rdgSeriesGR.add(rdoSeriesGR_Yes);
        rdgSeriesGR.add(rdoSeriesGR_No);
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtIssueHeadGR(txtIssueHeadGR.getText());
        observable.setTxtExchangeHeadGR(txtExchangeHeadGR.getText());
        observable.setTxtPayableHeadGR(txtPayableHeadGR.getText());
        observable.setTxtRCHeadGR(txtRCHeadGR.getText());
        observable.setTxtTCHeadGR(txtTCHeadGR.getText());
        observable.setTxtOCHeadGR(txtOCHeadGR.getText());
        observable.setTxtCCHeadGR(txtCCHeadGR.getText());
        observable.setTxtRTGSSuspenseHead(txtRTGSSuspenseHead.getText());
        observable.setTxtDCHeadGR(txtDCHeadGR.getText());
        observable.setTxtPostageHeadGR(txtPostageHeadGR.getText());
        //        observable.setCboProdCurrency((String) cboProdCurrency.getSelectedItem());
        observable.setTxtProductIdGR(txtProductIdGR.getText());
        observable.setTxtProductDescGR(txtProductDescGR.getText());
        observable.setCboLapsedPeriodGR((String) cboLapsedPeriodGR.getSelectedItem());
        observable.setTxtLapsedPeriodGR(txtLapsedPeriodGR.getText());
        observable.setRdoIsLapsedGR_Yes(rdoIsLapsedGR_Yes.isSelected());
        observable.setRdoIsLapsedGR_No(rdoIsLapsedGR_No.isSelected());
        observable.setTxtLapsedHeadGR(txtLapsedHeadGR.getText());
        observable.setRdoEFTProductGR_Yes(rdoEFTProductGR_Yes.isSelected());
        observable.setRdoEFTProductGR_No(rdoEFTProductGR_No.isSelected());
        observable.setRdoPrintServicesGR_Yes(rdoPrintServicesGR_Yes.isSelected());
        observable.setRdoPrintServicesGR_No(rdoPrintServicesGR_No.isSelected());
        observable.setRdoSeriesGR_Yes(rdoSeriesGR_Yes.isSelected());
        observable.setRdoSeriesGR_No(rdoSeriesGR_No.isSelected());
        observable.setTxtCashLimitGR(txtCashLimitGR.getText());
        observable.setTxtMaximumAmount(txtMaximumAmount.getText());
        observable.setTxtMinimumAmount(txtMinimumAmount.getText());
        observable.setTxtRemarksGR(txtRemarksGR.getText());
        observable.setTxtPerfix(txtPerfix.getText());
        observable.setTxtSuffix(txtSuffix.getText());
        observable.setTxtBranchCodeBR(txtBranchCodeBR.getText());
        observable.setCboRttTypeBR((String) cboRttTypeBR.getSelectedItem());
        observable.setTxtRttLimitBR(txtRttLimitBR.getText());
        observable.setTxtIVNoRR(txtIVNoRR.getText());
        observable.setTxtOVNoRR(txtOVNoRR.getText());
        observable.setTxtMinAmtRR(txtMinAmtRR.getText());
        observable.setTxtMaxAmtRR(txtMaxAmtRR.getText());
        observable.setTxtBranchName(txtBranchName.getText());
        observable.setTxtBankCode(txtBankCode.getText());
        observable.setTxtCharges(txtCharges.getText());
        observable.setCboChargeType((String) cboChargeType.getSelectedItem());
        observable.setCboCategory((String) cboCategory.getSelectedItem());
        observable.setTxtAmtRangeFrom(txtAmtRangeFrom.getText());
        observable.setTxtAmtRangeTo(txtAmtRangeTo.getText());
        observable.setCboBehavesLike((String) cboBehavesLike.getSelectedItem());
        observable.setCboPayableBranchGR((String) cboPayableBranchGR.getSelectedItem());
        observable.setTblRemittanceProductBranch((EnhancedTableModel)tblBrchRemittNumber.getModel());
        observable.setTblAliasRemittanceProductBranch();
        observable.setTblRemittanceProductCharge((EnhancedTableModel)tblRemitProdCharges.getModel());
        observable.setTxtPercent(txtPercentage.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setTxtForEvery(txtForEvery.getText());
        observable.setTxtRateVal(txtRateVal.getText());
        observable.setCboRateType((String) cboRateType.getSelectedItem());
        observable.setTxtMinAmt(txtMinimumAmt.getText());
        observable.setTxtMaxAmt(txtMaxAmt.getText());
        if (chkNewProcedure.isSelected()) { //Added By Kannan
            observable.setChkNewProcedure(true);
        } else {
            observable.setChkNewProcedure(false);
        }
        observable.setTdtNewProcStartDt(CommonUtil.convertObjToStr(tdtStartDate.getDateValue()));
        observable.setTxtNewProcIssueAcHd(txtDdIssueHead.getText());        
        if(rdoInvestmentAcHd.isSelected()){
            observable.setRtgsNeftGLType("N");
            observable.setCboProdTypeCr(CommonUtil.convertObjToStr(observable.getCbmProdTypeCr().getKeyForSelected()));
            observable.setTxtRtgsNeftProductId(txtTransProductId.getText());
            observable.setTxtRtgsNeftAcctNo(CommonUtil.convertObjToStr(txtAcctNo.getText()));
        }else{
            observable.setRtgsNeftGLType("Y");
        }
    }
    
    private void updateChargesOB() {
        observable.setTxtProductIdGR(txtProductIdGR.getText());
        //        observable.setLblChargesBankCode(txtBankCode.getText());
        //        observable.setLblChargesBranchCode(txtBranchCodeBR.getText());
        observable.setCboChargeType((String) cboChargeType.getSelectedItem());
        observable.setCboCategory((String) cboCategory.getSelectedItem());
        observable.setTxtAmtRangeFrom(txtAmtRangeFrom.getText());
        observable.setTxtAmtRangeTo(txtAmtRangeTo.getText());
        observable.setTxtCharges(txtCharges.getText());
        
        observable.setTxtPercent(txtPercentage.getText());
        observable.setTxtServiceTax(txtServiceTax.getText());
        observable.setTxtForEvery(txtForEvery.getText());
        observable.setTxtRateVal(txtRateVal.getText());
        observable.setCboRateType((String) cboRateType.getSelectedItem());
        observable.setTxtMinAmt(txtMinimumAmt.getText());
        observable.setTxtMaxAmt(txtMaxAmt.getText());
        
    }
    private void updateCharges() {
        txtProductIdGR.setText(observable.getTxtProductIdGR());
        txtAmtRangeFrom.setText(observable.getTxtAmtRangeFrom());
        txtAmtRangeTo.setText(observable.getTxtAmtRangeTo());
        txtCharges.setText(observable.getTxtCharges());
        txtPercentage.setText(observable.getTxtPercent());
        txtServiceTax.setText(observable.getTxtServiceTax());
        txtForEvery.setText(observable.getTxtForEvery());
        txtRateVal.setText(observable.getTxtRateVal());
        cboCategory.setSelectedItem(observable.getCboCategory());
        cboChargeType.setSelectedItem(observable.getCboChargeType());
        cboRateType.setSelectedItem(observable.getCboRateType());
        txtMinimumAmt.setText(observable.getTxtMinAmt());
        txtMaxAmt.setText(observable.getTxtMaxAmt());
        
        //        txtBankCode.setText(observable.getLblChargesBankCode());
        //        txtBranchCodeBR.setText(observable.getLblChargesBranchCode());
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        //        objMandatoryMRB = new RemittanceProductMRB();
        txtIssueHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtIssueHeadGR"));
        txtExchangeHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtExchangeHeadGR"));
        txtPayableHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtPayableHeadGR"));
        txtRCHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtRCHeadGR"));
        txtTCHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtTCHeadGR"));
        txtOCHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtOCHeadGR"));
        txtCCHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtCCHeadGR"));
        txtRTGSSuspenseHead.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtRTGSSuspenseHead"));
        txtDCHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtDCHeadGR"));
        txtPostageHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtPostageHeadGR"));
        //        cboProdCurrency.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboProdCurrency"));
        txtProductIdGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtProductIdGR"));
        txtProductDescGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtProductDescGR"));
        cboLapsedPeriodGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboLapsedPeriodGR"));
        txtLapsedPeriodGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtLapsedPeriodGR"));
        rdoIsLapsedGR_Yes.setHelpMessage(lblMsg, objMandatoryMRB.getString("rdoIsLapsedGR_Yes"));
        txtLapsedHeadGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtLapsedHeadGR"));
        rdoEFTProductGR_Yes.setHelpMessage(lblMsg, objMandatoryMRB.getString("rdoEFTProductGR_Yes"));
        rdoPrintServicesGR_Yes.setHelpMessage(lblMsg, objMandatoryMRB.getString("rdoPrintServicesGR_Yes"));
        rdoSeriesGR_Yes.setHelpMessage(lblMsg, objMandatoryMRB.getString("rdoSeriesGR_Yes"));
        txtCashLimitGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtCashLimitGR"));
        txtMaximumAmount.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtMaximumAmount"));
        txtMinimumAmount.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtMinimumAmount"));
        txtRemarksGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtRemarksGR"));
        txtPerfix.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtPerfix"));
        txtSuffix.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtSuffix"));
        cboPayableBranchGR.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboPayableBranchGR"));
        txtBankCode.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtBankCode"));
        txtBranchName.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtBranchName"));
        txtBranchCodeBR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtBranchCodeBR"));
        cboRttTypeBR.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboRttTypeBR"));
        txtRttLimitBR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtRttLimitBR"));
        txtIVNoRR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtIVNoRR"));
        txtOVNoRR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtOVNoRR"));
        txtMinAmtRR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtMinAmtRR"));
        txtMaxAmtRR.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtMaxAmtRR"));
        cboChargeType.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboChargeType"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboCategory"));
        txtAmtRangeFrom.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtAmtRangeFrom"));
        txtAmtRangeTo.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtAmtRangeTo"));
        cboBehavesLike.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboBehavesLike"));
        txtCharges.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtCharges"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgIsLapsedGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgEFTProductGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPayableBranchGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrintServicesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSeriesGR = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace11 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace12 = new com.see.truetransact.uicomponent.CLabel();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        lblSpace13 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace14 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace15 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace16 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabRemittanceProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        sptGeneralRemittanceH = new com.see.truetransact.uicomponent.CSeparator();
        panInsideGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        btnPayableHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        btnPostageHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        txtIssueHeadGR = new com.see.truetransact.uicomponent.CTextField();
        txtExchangeHeadGR = new com.see.truetransact.uicomponent.CTextField();
        btnExchangeHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        txtPayableHeadGR = new com.see.truetransact.uicomponent.CTextField();
        lblExchangeHeadGR = new com.see.truetransact.uicomponent.CLabel();
        lblIssueHeadGR = new com.see.truetransact.uicomponent.CLabel();
        txtRCHeadGR = new com.see.truetransact.uicomponent.CTextField();
        txtTCHeadGR = new com.see.truetransact.uicomponent.CTextField();
        lblCCHeadGR = new com.see.truetransact.uicomponent.CLabel();
        btnOCHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        btnIssueHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        lblTCHeadGR = new com.see.truetransact.uicomponent.CLabel();
        lblDCHeadGR = new com.see.truetransact.uicomponent.CLabel();
        lblPostageHeadGR = new com.see.truetransact.uicomponent.CLabel();
        txtOCHeadGR = new com.see.truetransact.uicomponent.CTextField();
        btnDCHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        btnCCHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        txtCCHeadGR = new com.see.truetransact.uicomponent.CTextField();
        txtDCHeadGR = new com.see.truetransact.uicomponent.CTextField();
        lblRCHeadGR = new com.see.truetransact.uicomponent.CLabel();
        lblOCHeadGR = new com.see.truetransact.uicomponent.CLabel();
        txtPostageHeadGR = new com.see.truetransact.uicomponent.CTextField();
        lblPayableHeadGR = new com.see.truetransact.uicomponent.CLabel();
        btnRCHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        btnTCHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        lblProdCurrency = new com.see.truetransact.uicomponent.CLabel();
        cboProdCurrency = new com.see.truetransact.uicomponent.CComboBox();
        lblTCHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblRCHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblOCHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblPayableHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblPostageHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblDCHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblIssueHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblCCHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        lblExchangeHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        panNewProcedurePan = new com.see.truetransact.uicomponent.CPanel();
        lblStartDate = new com.see.truetransact.uicomponent.CLabel();
        txtDdIssueHead = new com.see.truetransact.uicomponent.CTextField();
        lblDdIssueHead = new com.see.truetransact.uicomponent.CLabel();
        tdtStartDate = new com.see.truetransact.uicomponent.CDateField();
        lblDdIssueHeadVal = new com.see.truetransact.uicomponent.CLabel();
        btnDdIssueHead = new com.see.truetransact.uicomponent.CButton();
        panSelectAll = new com.see.truetransact.uicomponent.CPanel();
        lblNewProcedure = new com.see.truetransact.uicomponent.CLabel();
        chkNewProcedure = new com.see.truetransact.uicomponent.CCheckBox();
        lblRTGSSuspenseHead = new com.see.truetransact.uicomponent.CLabel();
        txtRTGSSuspenseHead = new com.see.truetransact.uicomponent.CTextField();
        btnRTGSSuspenseHead = new com.see.truetransact.uicomponent.CButton();
        lblRTGSSuspenseHeadDesc = new com.see.truetransact.uicomponent.CLabel();
        panProdDescGeneralRemittance = new com.see.truetransact.uicomponent.CPanel();
        lblProductIdGR = new com.see.truetransact.uicomponent.CLabel();
        txtProductIdGR = new com.see.truetransact.uicomponent.CTextField();
        txtProductDescGR = new com.see.truetransact.uicomponent.CTextField();
        lblProductDescGR = new com.see.truetransact.uicomponent.CLabel();
        lblBehavesLike = new com.see.truetransact.uicomponent.CLabel();
        cboBehavesLike = new com.see.truetransact.uicomponent.CComboBox();
        sptGeneralRemittanceV = new com.see.truetransact.uicomponent.CSeparator();
        panLapsedGR = new com.see.truetransact.uicomponent.CPanel();
        lblIsLapsedGR = new com.see.truetransact.uicomponent.CLabel();
        panLapsedPeriodGR = new com.see.truetransact.uicomponent.CPanel();
        cboLapsedPeriodGR = new com.see.truetransact.uicomponent.CComboBox();
        txtLapsedPeriodGR = new com.see.truetransact.uicomponent.CTextField();
        lblLapsedPeriodGR = new com.see.truetransact.uicomponent.CLabel();
        lblLapsedHeadGR = new com.see.truetransact.uicomponent.CLabel();
        panIsLapsedGR = new com.see.truetransact.uicomponent.CPanel();
        rdoIsLapsedGR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoIsLapsedGR_No = new com.see.truetransact.uicomponent.CRadioButton();
        panLapsedHeadGR = new com.see.truetransact.uicomponent.CPanel();
        txtLapsedHeadGR = new com.see.truetransact.uicomponent.CTextField();
        btnLapsedHeadGRHelp = new com.see.truetransact.uicomponent.CButton();
        sptGeneralRemittanceL = new com.see.truetransact.uicomponent.CSeparator();
        panOtherGR = new com.see.truetransact.uicomponent.CPanel();
        panEFTProductGR = new com.see.truetransact.uicomponent.CPanel();
        rdoEFTProductGR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoEFTProductGR_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblEFTProductGR = new com.see.truetransact.uicomponent.CLabel();
        lblPayableBranchGR = new com.see.truetransact.uicomponent.CLabel();
        panPrintServicesGR = new com.see.truetransact.uicomponent.CPanel();
        rdoPrintServicesGR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPrintServicesGR_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblPrintServicesGR = new com.see.truetransact.uicomponent.CLabel();
        panSeriesGR = new com.see.truetransact.uicomponent.CPanel();
        rdoSeriesGR_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSeriesGR_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblSeriesGR = new com.see.truetransact.uicomponent.CLabel();
        txtCashLimitGR = new com.see.truetransact.uicomponent.CTextField();
        txtRemarksGR = new com.see.truetransact.uicomponent.CTextField();
        lblCashLimitGR = new com.see.truetransact.uicomponent.CLabel();
        lblRemarksGR = new com.see.truetransact.uicomponent.CLabel();
        panNumberPattern = new com.see.truetransact.uicomponent.CPanel();
        txtPerfix = new com.see.truetransact.uicomponent.CTextField();
        txtSuffix = new com.see.truetransact.uicomponent.CTextField();
        lblNumberPattern = new com.see.truetransact.uicomponent.CLabel();
        cboPayableBranchGR = new com.see.truetransact.uicomponent.CComboBox();
        lblMaximumAmount = new com.see.truetransact.uicomponent.CLabel();
        txtMaximumAmount = new com.see.truetransact.uicomponent.CTextField();
        lblMinimumAmount = new com.see.truetransact.uicomponent.CLabel();
        txtMinimumAmount = new com.see.truetransact.uicomponent.CTextField();
        panAccountDetails1 = new com.see.truetransact.uicomponent.CPanel();
        cboProdTypeCr = new com.see.truetransact.uicomponent.CComboBox();
        lblProdTypeCr = new com.see.truetransact.uicomponent.CLabel();
        lblAccNo = new com.see.truetransact.uicomponent.CLabel();
        lblProdIdCr = new com.see.truetransact.uicomponent.CLabel();
        panAccHd = new com.see.truetransact.uicomponent.CPanel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnAccNo = new com.see.truetransact.uicomponent.CButton();
        panAcctNo = new com.see.truetransact.uicomponent.CPanel();
        panDebitAccHead = new com.see.truetransact.uicomponent.CPanel();
        txtTransProductId = new com.see.truetransact.uicomponent.CTextField();
        btnTransProductId = new com.see.truetransact.uicomponent.CButton();
        rdoInvestmentAcHd = new com.see.truetransact.uicomponent.CRadioButton();
        rdoGlAcHd = new com.see.truetransact.uicomponent.CRadioButton();
        panBranchAndRttNoRemittance = new com.see.truetransact.uicomponent.CPanel();
        panProdDescRemitProdBranch = new com.see.truetransact.uicomponent.CPanel();
        lblProdIDRemitProdBrch = new com.see.truetransact.uicomponent.CLabel();
        lblProdDescRemitProdBrch = new com.see.truetransact.uicomponent.CLabel();
        lblProdDescRemitProdBranch = new com.see.truetransact.uicomponent.CLabel();
        lblProdIDRemitProdBranch = new com.see.truetransact.uicomponent.CLabel();
        panBrchRemittance = new com.see.truetransact.uicomponent.CPanel();
        panBranchRemittance = new com.see.truetransact.uicomponent.CPanel();
        sptRemitButtons = new com.see.truetransact.uicomponent.CSeparator();
        panBrchRemittNumberButton = new com.see.truetransact.uicomponent.CPanel();
        btnBrchRemittNumberSave = new com.see.truetransact.uicomponent.CButton();
        btnBrchRemittNumberDelete = new com.see.truetransact.uicomponent.CButton();
        btnBrchRemittNumberNew = new com.see.truetransact.uicomponent.CButton();
        panRemitType = new com.see.truetransact.uicomponent.CPanel();
        cboRttTypeBR = new com.see.truetransact.uicomponent.CComboBox();
        txtRttLimitBR = new com.see.truetransact.uicomponent.CTextField();
        txtIVNoRR = new com.see.truetransact.uicomponent.CTextField();
        txtOVNoRR = new com.see.truetransact.uicomponent.CTextField();
        txtMinAmtRR = new com.see.truetransact.uicomponent.CTextField();
        txtMaxAmtRR = new com.see.truetransact.uicomponent.CTextField();
        lblRttTypeBR = new com.see.truetransact.uicomponent.CLabel();
        lblRttLimitBR = new com.see.truetransact.uicomponent.CLabel();
        lblIVNoRR = new com.see.truetransact.uicomponent.CLabel();
        lblOVNoRR = new com.see.truetransact.uicomponent.CLabel();
        lblMinAmtRR = new com.see.truetransact.uicomponent.CLabel();
        lblMaxAmtRR = new com.see.truetransact.uicomponent.CLabel();
        panRemitBankCode = new com.see.truetransact.uicomponent.CPanel();
        panBankCode = new javax.swing.JPanel();
        btnBankCode = new com.see.truetransact.uicomponent.CButton();
        txtBankCode = new com.see.truetransact.uicomponent.CTextField();
        panBranchName = new javax.swing.JPanel();
        btnBranchName = new com.see.truetransact.uicomponent.CButton();
        txtBranchName = new com.see.truetransact.uicomponent.CTextField();
        txtBranchCodeBR = new com.see.truetransact.uicomponent.CTextField();
        lblBranchCodeBR = new com.see.truetransact.uicomponent.CLabel();
        lblBankCode = new com.see.truetransact.uicomponent.CLabel();
        lblBranchNameBR = new com.see.truetransact.uicomponent.CLabel();
        srpBrchRemittNumber = new com.see.truetransact.uicomponent.CScrollPane();
        tblBrchRemittNumber = new com.see.truetransact.uicomponent.CTable();
        sptProdDescRemitProdBranch = new com.see.truetransact.uicomponent.CSeparator();
        panCharges = new com.see.truetransact.uicomponent.CPanel();
        sprRemitProdCharges = new com.see.truetransact.uicomponent.CScrollPane();
        tblRemitProdCharges = new com.see.truetransact.uicomponent.CTable();
        panRemitProdCharges = new com.see.truetransact.uicomponent.CPanel();
        panRemitProdChargesFields = new com.see.truetransact.uicomponent.CPanel();
        lblChargeType = new com.see.truetransact.uicomponent.CLabel();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        lblAmtRangeFrom = new com.see.truetransact.uicomponent.CLabel();
        lblAmtRangeTo = new com.see.truetransact.uicomponent.CLabel();
        lblCharges = new com.see.truetransact.uicomponent.CLabel();
        cboChargeType = new com.see.truetransact.uicomponent.CComboBox();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        txtCharges = new com.see.truetransact.uicomponent.CTextField();
        lblPercentage = new com.see.truetransact.uicomponent.CLabel();
        lblForEvery = new com.see.truetransact.uicomponent.CLabel();
        txtPercentage = new com.see.truetransact.uicomponent.CTextField();
        txtForEvery = new com.see.truetransact.uicomponent.CTextField();
        txtRateVal = new com.see.truetransact.uicomponent.CTextField();
        cboRateType = new com.see.truetransact.uicomponent.CComboBox();
        txtAmtRangeFrom = new com.see.truetransact.uicomponent.CTextField();
        txtAmtRangeTo = new com.see.truetransact.uicomponent.CTextField();
        txtServiceTax = new com.see.truetransact.uicomponent.CTextField();
        lblServiceTax = new com.see.truetransact.uicomponent.CLabel();
        lblMaximumAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaxAmt = new com.see.truetransact.uicomponent.CTextField();
        lblMinimumAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMinimumAmt = new com.see.truetransact.uicomponent.CTextField();
        panRemitProdChargesButtons = new com.see.truetransact.uicomponent.CPanel();
        btnRemitProdChargesNew = new com.see.truetransact.uicomponent.CButton();
        btnRemitProdChargesSave = new com.see.truetransact.uicomponent.CButton();
        btnRemitProdChargesDelete = new com.see.truetransact.uicomponent.CButton();
        sptRemitProdChargesFieldsAndButtons = new com.see.truetransact.uicomponent.CSeparator();
        sptProdDescRemitProdCharges = new com.see.truetransact.uicomponent.CSeparator();
        panProdDescRemitProdCharges = new com.see.truetransact.uicomponent.CPanel();
        lblProdDescRemitProdCharge = new com.see.truetransact.uicomponent.CLabel();
        lblChargesBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayBankCode = new com.see.truetransact.uicomponent.CLabel();
        lblDisplayBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblProdIDRemitProdChrg = new com.see.truetransact.uicomponent.CLabel();
        lblProdIDRemitProdCharge = new com.see.truetransact.uicomponent.CLabel();
        lblProdDescRemitProdChrg = new com.see.truetransact.uicomponent.CLabel();
        lblChargesBankCode = new com.see.truetransact.uicomponent.CLabel();
        panAliasBrchRemittNumber = new com.see.truetransact.uicomponent.CPanel();
        srpAliasBrchRemittNumber = new com.see.truetransact.uicomponent.CScrollPane();
        tblAliasBrchRemittNumber = new com.see.truetransact.uicomponent.CTable();
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
        setMinimumSize(new java.awt.Dimension(900, 700));
        setPreferredSize(new java.awt.Dimension(900, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());

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
        tbrAdvances.add(btnView);

        lblSpace6.setText("     ");
        tbrAdvances.add(lblSpace6);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.setNextFocusableComponent(txtProductIdGR);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnNew);

        lblSpace11.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace11.setText("     ");
        lblSpace11.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace11.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace11);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnEdit);

        lblSpace12.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace12.setText("     ");
        lblSpace12.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace12.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace12);

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCopy);

        lblSpace13.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace13.setText("     ");
        lblSpace13.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace13.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace13);

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

        lblSpace14.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace14.setText("     ");
        lblSpace14.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace14.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace14);

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

        lblSpace15.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace15.setText("     ");
        lblSpace15.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace15.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace15);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace16.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace16.setText("     ");
        lblSpace16.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace16.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace16);

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

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace17);

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

        tabRemittanceProduct.setMinimumSize(new java.awt.Dimension(780, 680));
        tabRemittanceProduct.setPreferredSize(new java.awt.Dimension(780, 680));

        panGeneralRemittance.setMinimumSize(new java.awt.Dimension(570, 450));
        panGeneralRemittance.setPreferredSize(new java.awt.Dimension(570, 450));
        panGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        sptGeneralRemittanceH.setMinimumSize(new java.awt.Dimension(2, 2));
        sptGeneralRemittanceH.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(sptGeneralRemittanceH, gridBagConstraints);

        panInsideGeneralRemittance.setMinimumSize(new java.awt.Dimension(470, 400));
        panInsideGeneralRemittance.setPreferredSize(new java.awt.Dimension(470, 450));
        panInsideGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        btnPayableHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPayableHeadGRHelp.setEnabled(false);
        btnPayableHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPayableHeadGRHelp.setNextFocusableComponent(btnPostageHeadGRHelp);
        btnPayableHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPayableHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayableHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        panInsideGeneralRemittance.add(btnPayableHeadGRHelp, gridBagConstraints);

        btnPostageHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnPostageHeadGRHelp.setEnabled(false);
        btnPostageHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnPostageHeadGRHelp.setNextFocusableComponent(btnDCHeadGRHelp);
        btnPostageHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnPostageHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPostageHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        panInsideGeneralRemittance.add(btnPostageHeadGRHelp, gridBagConstraints);

        txtIssueHeadGR.setEnabled(false);
        txtIssueHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtIssueHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtIssueHeadGR, gridBagConstraints);

        txtExchangeHeadGR.setEnabled(false);
        txtExchangeHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtExchangeHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtExchangeHeadGR, gridBagConstraints);

        btnExchangeHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnExchangeHeadGRHelp.setEnabled(false);
        btnExchangeHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnExchangeHeadGRHelp.setNextFocusableComponent(btnTCHeadGRHelp);
        btnExchangeHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnExchangeHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExchangeHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        panInsideGeneralRemittance.add(btnExchangeHeadGRHelp, gridBagConstraints);

        txtPayableHeadGR.setEnabled(false);
        txtPayableHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtPayableHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtPayableHeadGR, gridBagConstraints);

        lblExchangeHeadGR.setText("Exchange / Commission Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblExchangeHeadGR, gridBagConstraints);

        lblIssueHeadGR.setText("Issue Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblIssueHeadGR, gridBagConstraints);

        txtRCHeadGR.setEnabled(false);
        txtRCHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtRCHeadGR.setNextFocusableComponent(txtOCHeadGR);
        txtRCHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtRCHeadGR, gridBagConstraints);

        txtTCHeadGR.setEnabled(false);
        txtTCHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtTCHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtTCHeadGR, gridBagConstraints);

        lblCCHeadGR.setText("Cancellation Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblCCHeadGR, gridBagConstraints);

        btnOCHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnOCHeadGRHelp.setEnabled(false);
        btnOCHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnOCHeadGRHelp.setNextFocusableComponent(btnPayableHeadGRHelp);
        btnOCHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnOCHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOCHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        panInsideGeneralRemittance.add(btnOCHeadGRHelp, gridBagConstraints);

        btnIssueHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnIssueHeadGRHelp.setEnabled(false);
        btnIssueHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnIssueHeadGRHelp.setNextFocusableComponent(btnExchangeHeadGRHelp);
        btnIssueHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnIssueHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIssueHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panInsideGeneralRemittance.add(btnIssueHeadGRHelp, gridBagConstraints);

        lblTCHeadGR.setText("Telegram Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblTCHeadGR, gridBagConstraints);

        lblDCHeadGR.setText("Duplicate Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblDCHeadGR, gridBagConstraints);

        lblPostageHeadGR.setText("Postage Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblPostageHeadGR, gridBagConstraints);

        txtOCHeadGR.setEnabled(false);
        txtOCHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtOCHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtOCHeadGR, gridBagConstraints);

        btnDCHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDCHeadGRHelp.setEnabled(false);
        btnDCHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDCHeadGRHelp.setNextFocusableComponent(btnCCHeadGRHelp);
        btnDCHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDCHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDCHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        panInsideGeneralRemittance.add(btnDCHeadGRHelp, gridBagConstraints);

        btnCCHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCCHeadGRHelp.setEnabled(false);
        btnCCHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnCCHeadGRHelp.setNextFocusableComponent(rdoIsLapsedGR_Yes);
        btnCCHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCCHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCCHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        panInsideGeneralRemittance.add(btnCCHeadGRHelp, gridBagConstraints);

        txtCCHeadGR.setEnabled(false);
        txtCCHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtCCHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtCCHeadGR, gridBagConstraints);

        txtDCHeadGR.setEnabled(false);
        txtDCHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtDCHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtDCHeadGR, gridBagConstraints);

        lblRCHeadGR.setText("Revalidation Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblRCHeadGR, gridBagConstraints);

        lblOCHeadGR.setText("Service Tax Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblOCHeadGR, gridBagConstraints);

        txtPostageHeadGR.setEnabled(false);
        txtPostageHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtPostageHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtPostageHeadGR, gridBagConstraints);

        lblPayableHeadGR.setText("Payable Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblPayableHeadGR, gridBagConstraints);

        btnRCHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRCHeadGRHelp.setEnabled(false);
        btnRCHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRCHeadGRHelp.setNextFocusableComponent(btnOCHeadGRHelp);
        btnRCHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRCHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRCHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        panInsideGeneralRemittance.add(btnRCHeadGRHelp, gridBagConstraints);

        btnTCHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTCHeadGRHelp.setEnabled(false);
        btnTCHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnTCHeadGRHelp.setNextFocusableComponent(btnRCHeadGRHelp);
        btnTCHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTCHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTCHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        panInsideGeneralRemittance.add(btnTCHeadGRHelp, gridBagConstraints);

        lblProdCurrency.setText("Product Currency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInsideGeneralRemittance.add(lblProdCurrency, gridBagConstraints);

        cboProdCurrency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboProdCurrency.setNextFocusableComponent(btnIssueHeadGRHelp);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panInsideGeneralRemittance.add(cboProdCurrency, gridBagConstraints);

        lblTCHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblTCHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblTCHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblTCHeadDesc, gridBagConstraints);

        lblRCHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblRCHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblRCHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblRCHeadDesc, gridBagConstraints);

        lblOCHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblOCHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblOCHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblOCHeadDesc, gridBagConstraints);

        lblPayableHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblPayableHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblPayableHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblPayableHeadDesc, gridBagConstraints);

        lblPostageHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblPostageHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblPostageHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblPostageHeadDesc, gridBagConstraints);

        lblDCHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblDCHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblDCHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblDCHeadDesc, gridBagConstraints);

        lblIssueHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblIssueHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblIssueHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblIssueHeadDesc, gridBagConstraints);

        lblCCHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblCCHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblCCHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblCCHeadDesc, gridBagConstraints);

        lblExchangeHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblExchangeHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblExchangeHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblExchangeHeadDesc, gridBagConstraints);

        panNewProcedurePan.setLayout(new java.awt.GridBagLayout());

        lblStartDate.setText("StartDate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewProcedurePan.add(lblStartDate, gridBagConstraints);

        txtDdIssueHead.setEnabled(false);
        txtDdIssueHead.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDdIssueHead.setNextFocusableComponent(cboProdCurrency);
        txtDdIssueHead.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDdIssueHeadFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewProcedurePan.add(txtDdIssueHead, gridBagConstraints);

        lblDdIssueHead.setText("DD Issue Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewProcedurePan.add(lblDdIssueHead, gridBagConstraints);

        tdtStartDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtStartDateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panNewProcedurePan.add(tdtStartDate, gridBagConstraints);

        lblDdIssueHeadVal.setMaximumSize(new java.awt.Dimension(165, 18));
        lblDdIssueHeadVal.setMinimumSize(new java.awt.Dimension(165, 18));
        lblDdIssueHeadVal.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNewProcedurePan.add(lblDdIssueHeadVal, gridBagConstraints);

        btnDdIssueHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnDdIssueHead.setEnabled(false);
        btnDdIssueHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnDdIssueHead.setNextFocusableComponent(rdoIsLapsedGR_Yes);
        btnDdIssueHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnDdIssueHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDdIssueHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panNewProcedurePan.add(btnDdIssueHead, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 13);
        panInsideGeneralRemittance.add(panNewProcedurePan, gridBagConstraints);

        panSelectAll.setMinimumSize(new java.awt.Dimension(180, 17));
        panSelectAll.setPreferredSize(new java.awt.Dimension(180, 17));
        panSelectAll.setLayout(new java.awt.GridBagLayout());

        lblNewProcedure.setText("DD Issue-New Procedure");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 19, 5, 0);
        panSelectAll.add(lblNewProcedure, gridBagConstraints);

        chkNewProcedure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNewProcedureActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 5, 1);
        panSelectAll.add(chkNewProcedure, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        panInsideGeneralRemittance.add(panSelectAll, gridBagConstraints);

        lblRTGSSuspenseHead.setText("RTGS NEFT Suspense Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblRTGSSuspenseHead, gridBagConstraints);

        txtRTGSSuspenseHead.setEnabled(false);
        txtRTGSSuspenseHead.setMinimumSize(new java.awt.Dimension(76, 21));
        txtRTGSSuspenseHead.setPreferredSize(new java.awt.Dimension(76, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(txtRTGSSuspenseHead, gridBagConstraints);

        btnRTGSSuspenseHead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnRTGSSuspenseHead.setEnabled(false);
        btnRTGSSuspenseHead.setMinimumSize(new java.awt.Dimension(21, 21));
        btnRTGSSuspenseHead.setNextFocusableComponent(rdoIsLapsedGR_Yes);
        btnRTGSSuspenseHead.setPreferredSize(new java.awt.Dimension(21, 21));
        btnRTGSSuspenseHead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRTGSSuspenseHeadActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        panInsideGeneralRemittance.add(btnRTGSSuspenseHead, gridBagConstraints);

        lblRTGSSuspenseHeadDesc.setMaximumSize(new java.awt.Dimension(165, 18));
        lblRTGSSuspenseHeadDesc.setMinimumSize(new java.awt.Dimension(165, 18));
        lblRTGSSuspenseHeadDesc.setPreferredSize(new java.awt.Dimension(165, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInsideGeneralRemittance.add(lblRTGSSuspenseHeadDesc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(panInsideGeneralRemittance, gridBagConstraints);

        panProdDescGeneralRemittance.setLayout(new java.awt.GridBagLayout());

        lblProductIdGR.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescGeneralRemittance.add(lblProductIdGR, gridBagConstraints);

        txtProductIdGR.setAllowAll(true);
        txtProductIdGR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductIdGR.setNextFocusableComponent(txtProductDescGR);
        txtProductIdGR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductIdGRFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescGeneralRemittance.add(txtProductIdGR, gridBagConstraints);

        txtProductDescGR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtProductDescGR.setNextFocusableComponent(cboProdCurrency);
        txtProductDescGR.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtProductDescGRFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescGeneralRemittance.add(txtProductDescGR, gridBagConstraints);

        lblProductDescGR.setText("Product (Long) Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescGeneralRemittance.add(lblProductDescGR, gridBagConstraints);

        lblBehavesLike.setText("Behaves Like");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescGeneralRemittance.add(lblBehavesLike, gridBagConstraints);

        cboBehavesLike.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBehavesLikeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescGeneralRemittance.add(cboBehavesLike, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 13);
        panGeneralRemittance.add(panProdDescGeneralRemittance, gridBagConstraints);

        sptGeneralRemittanceV.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptGeneralRemittanceV.setMinimumSize(new java.awt.Dimension(2, 2));
        sptGeneralRemittanceV.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(sptGeneralRemittanceV, gridBagConstraints);

        panLapsedGR.setPreferredSize(new java.awt.Dimension(225, 87));
        panLapsedGR.setLayout(new java.awt.GridBagLayout());

        lblIsLapsedGR.setText("Lapsed Applicable");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLapsedGR.add(lblIsLapsedGR, gridBagConstraints);

        panLapsedPeriodGR.setLayout(new java.awt.GridBagLayout());

        cboLapsedPeriodGR.setMinimumSize(new java.awt.Dimension(75, 21));
        cboLapsedPeriodGR.setNextFocusableComponent(rdoEFTProductGR_Yes);
        cboLapsedPeriodGR.setPreferredSize(new java.awt.Dimension(75, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panLapsedPeriodGR.add(cboLapsedPeriodGR, gridBagConstraints);

        txtLapsedPeriodGR.setMinimumSize(new java.awt.Dimension(21, 21));
        txtLapsedPeriodGR.setNextFocusableComponent(cboLapsedPeriodGR);
        txtLapsedPeriodGR.setPreferredSize(new java.awt.Dimension(21, 21));
        txtLapsedPeriodGR.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panLapsedPeriodGR.add(txtLapsedPeriodGR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLapsedGR.add(panLapsedPeriodGR, gridBagConstraints);

        lblLapsedPeriodGR.setText("Lapsed Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLapsedGR.add(lblLapsedPeriodGR, gridBagConstraints);

        lblLapsedHeadGR.setText("Lapsed Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLapsedGR.add(lblLapsedHeadGR, gridBagConstraints);

        panIsLapsedGR.setMaximumSize(new java.awt.Dimension(120, 21));
        panIsLapsedGR.setMinimumSize(new java.awt.Dimension(120, 21));
        panIsLapsedGR.setName("");
        panIsLapsedGR.setPreferredSize(new java.awt.Dimension(120, 21));
        panIsLapsedGR.setLayout(new java.awt.GridBagLayout());

        rdgIsLapsedGR.add(rdoIsLapsedGR_Yes);
        rdoIsLapsedGR_Yes.setText("Yes");
        rdoIsLapsedGR_Yes.setToolTipText("Yes");
        rdoIsLapsedGR_Yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoIsLapsedGR_Yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoIsLapsedGR_Yes.setNextFocusableComponent(rdoIsLapsedGR_No);
        rdoIsLapsedGR_Yes.setPreferredSize(new java.awt.Dimension(53, 21));
        rdoIsLapsedGR_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsLapsedGR_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panIsLapsedGR.add(rdoIsLapsedGR_Yes, gridBagConstraints);

        rdgIsLapsedGR.add(rdoIsLapsedGR_No);
        rdoIsLapsedGR_No.setText("No");
        rdoIsLapsedGR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoIsLapsedGR_No.setNextFocusableComponent(btnLapsedHeadGRHelp);
        rdoIsLapsedGR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoIsLapsedGR_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoIsLapsedGR_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panIsLapsedGR.add(rdoIsLapsedGR_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLapsedGR.add(panIsLapsedGR, gridBagConstraints);

        panLapsedHeadGR.setLayout(new java.awt.GridBagLayout());

        txtLapsedHeadGR.setMinimumSize(new java.awt.Dimension(76, 21));
        txtLapsedHeadGR.setPreferredSize(new java.awt.Dimension(76, 21));
        txtLapsedHeadGR.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panLapsedHeadGR.add(txtLapsedHeadGR, gridBagConstraints);

        btnLapsedHeadGRHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnLapsedHeadGRHelp.setEnabled(false);
        btnLapsedHeadGRHelp.setMinimumSize(new java.awt.Dimension(21, 21));
        btnLapsedHeadGRHelp.setNextFocusableComponent(txtLapsedPeriodGR);
        btnLapsedHeadGRHelp.setPreferredSize(new java.awt.Dimension(21, 21));
        btnLapsedHeadGRHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLapsedHeadGRHelpActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panLapsedHeadGR.add(btnLapsedHeadGRHelp, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLapsedGR.add(panLapsedHeadGR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 45);
        panGeneralRemittance.add(panLapsedGR, gridBagConstraints);

        sptGeneralRemittanceL.setMinimumSize(new java.awt.Dimension(2, 2));
        sptGeneralRemittanceL.setPreferredSize(new java.awt.Dimension(2, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(sptGeneralRemittanceL, gridBagConstraints);

        panOtherGR.setMinimumSize(new java.awt.Dimension(342, 203));
        panOtherGR.setPreferredSize(new java.awt.Dimension(342, 203));
        panOtherGR.setLayout(new java.awt.GridBagLayout());

        panEFTProductGR.setMaximumSize(new java.awt.Dimension(120, 21));
        panEFTProductGR.setMinimumSize(new java.awt.Dimension(120, 21));
        panEFTProductGR.setPreferredSize(new java.awt.Dimension(120, 21));
        panEFTProductGR.setLayout(new java.awt.GridBagLayout());

        rdgEFTProductGR.add(rdoEFTProductGR_Yes);
        rdoEFTProductGR_Yes.setText("Yes");
        rdoEFTProductGR_Yes.setToolTipText("Yes");
        rdoEFTProductGR_Yes.setMaximumSize(new java.awt.Dimension(55, 21));
        rdoEFTProductGR_Yes.setMinimumSize(new java.awt.Dimension(55, 21));
        rdoEFTProductGR_Yes.setNextFocusableComponent(rdoEFTProductGR_No);
        rdoEFTProductGR_Yes.setPreferredSize(new java.awt.Dimension(55, 21));
        rdoEFTProductGR_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEFTProductGR_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panEFTProductGR.add(rdoEFTProductGR_Yes, gridBagConstraints);

        rdgEFTProductGR.add(rdoEFTProductGR_No);
        rdoEFTProductGR_No.setText("No");
        rdoEFTProductGR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoEFTProductGR_No.setNextFocusableComponent(cboPayableBranchGR);
        rdoEFTProductGR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoEFTProductGR_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoEFTProductGR_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panEFTProductGR.add(rdoEFTProductGR_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(panEFTProductGR, gridBagConstraints);

        lblEFTProductGR.setText("EFT Product?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblEFTProductGR, gridBagConstraints);

        lblPayableBranchGR.setText("Payable at");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblPayableBranchGR, gridBagConstraints);

        panPrintServicesGR.setMaximumSize(new java.awt.Dimension(102, 21));
        panPrintServicesGR.setLayout(new java.awt.GridBagLayout());

        rdgPrintServicesGR.add(rdoPrintServicesGR_Yes);
        rdoPrintServicesGR_Yes.setText("Yes");
        rdoPrintServicesGR_Yes.setToolTipText("Yes");
        rdoPrintServicesGR_Yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoPrintServicesGR_Yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoPrintServicesGR_Yes.setNextFocusableComponent(rdoPrintServicesGR_No);
        rdoPrintServicesGR_Yes.setPreferredSize(new java.awt.Dimension(53, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR.add(rdoPrintServicesGR_Yes, gridBagConstraints);

        rdgPrintServicesGR.add(rdoPrintServicesGR_No);
        rdoPrintServicesGR_No.setText("No");
        rdoPrintServicesGR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoPrintServicesGR_No.setNextFocusableComponent(rdoSeriesGR_Yes);
        rdoPrintServicesGR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR.add(rdoPrintServicesGR_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(panPrintServicesGR, gridBagConstraints);

        lblPrintServicesGR.setText("Print Services?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblPrintServicesGR, gridBagConstraints);

        panSeriesGR.setMaximumSize(new java.awt.Dimension(120, 21));
        panSeriesGR.setMinimumSize(new java.awt.Dimension(120, 21));
        panSeriesGR.setPreferredSize(new java.awt.Dimension(120, 21));
        panSeriesGR.setLayout(new java.awt.GridBagLayout());

        rdgSeriesGR.add(rdoSeriesGR_Yes);
        rdoSeriesGR_Yes.setText("Yes");
        rdoSeriesGR_Yes.setToolTipText("Yes");
        rdoSeriesGR_Yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoSeriesGR_Yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoSeriesGR_Yes.setNextFocusableComponent(rdoSeriesGR_No);
        rdoSeriesGR_Yes.setPreferredSize(new java.awt.Dimension(53, 21));
        rdoSeriesGR_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSeriesGR_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSeriesGR.add(rdoSeriesGR_Yes, gridBagConstraints);

        rdgSeriesGR.add(rdoSeriesGR_No);
        rdoSeriesGR_No.setText("No");
        rdoSeriesGR_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoSeriesGR_No.setNextFocusableComponent(txtPerfix);
        rdoSeriesGR_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoSeriesGR_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSeriesGR_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSeriesGR.add(rdoSeriesGR_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(panSeriesGR, gridBagConstraints);

        lblSeriesGR.setText("Branch-wise Series to be maintained?");
        lblSeriesGR.setToolTipText("Branch-wise Series to be maintained?");
        lblSeriesGR.setMaximumSize(new java.awt.Dimension(230, 18));
        lblSeriesGR.setMinimumSize(new java.awt.Dimension(230, 18));
        lblSeriesGR.setPreferredSize(new java.awt.Dimension(230, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblSeriesGR, gridBagConstraints);

        txtCashLimitGR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCashLimitGR.setNextFocusableComponent(txtRemarksGR);
        txtCashLimitGR.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(txtCashLimitGR, gridBagConstraints);

        txtRemarksGR.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(txtRemarksGR, gridBagConstraints);

        lblCashLimitGR.setText("Cash Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblCashLimitGR, gridBagConstraints);

        lblRemarksGR.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblRemarksGR, gridBagConstraints);

        panNumberPattern.setLayout(new java.awt.GridBagLayout());

        txtPerfix.setMinimumSize(new java.awt.Dimension(21, 21));
        txtPerfix.setNextFocusableComponent(txtSuffix);
        txtPerfix.setPreferredSize(new java.awt.Dimension(21, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panNumberPattern.add(txtPerfix, gridBagConstraints);

        txtSuffix.setMinimumSize(new java.awt.Dimension(75, 21));
        txtSuffix.setNextFocusableComponent(txtCashLimitGR);
        txtSuffix.setPreferredSize(new java.awt.Dimension(75, 21));
        panNumberPattern.add(txtSuffix, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(panNumberPattern, gridBagConstraints);

        lblNumberPattern.setText("Number Pattern");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblNumberPattern, gridBagConstraints);

        cboPayableBranchGR.setNextFocusableComponent(rdoPrintServicesGR_Yes);
        cboPayableBranchGR.setPopupWidth(209);
        cboPayableBranchGR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPayableBranchGRActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(cboPayableBranchGR, gridBagConstraints);

        lblMaximumAmount.setText("Maximum Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblMaximumAmount, gridBagConstraints);

        txtMaximumAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaximumAmount.setNextFocusableComponent(txtRemarksGR);
        txtMaximumAmount.setValidation(new NumericValidation());
        txtMaximumAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaximumAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(txtMaximumAmount, gridBagConstraints);

        lblMinimumAmount.setText("Minimum Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(lblMinimumAmount, gridBagConstraints);

        txtMinimumAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinimumAmount.setNextFocusableComponent(txtRemarksGR);
        txtMinimumAmount.setValidation(new NumericValidation());
        txtMinimumAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMinimumAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherGR.add(txtMinimumAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneralRemittance.add(panOtherGR, gridBagConstraints);

        panAccountDetails1.setBorder(javax.swing.BorderFactory.createTitledBorder("Debit Account Details"));
        panAccountDetails1.setMinimumSize(new java.awt.Dimension(400, 130));
        panAccountDetails1.setPreferredSize(new java.awt.Dimension(400, 135));
        panAccountDetails1.setLayout(new java.awt.GridBagLayout());

        cboProdTypeCr.setToolTipText("Debit Account Product");
        cboProdTypeCr.setMinimumSize(new java.awt.Dimension(150, 21));
        cboProdTypeCr.setPopupWidth(160);
        cboProdTypeCr.setPreferredSize(new java.awt.Dimension(150, 21));
        cboProdTypeCr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboProdTypeCrActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(cboProdTypeCr, gridBagConstraints);

        lblProdTypeCr.setText("Product Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblProdTypeCr, gridBagConstraints);

        lblAccNo.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblAccNo, gridBagConstraints);

        lblProdIdCr.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAccountDetails1.add(lblProdIdCr, gridBagConstraints);

        panAccHd.setMinimumSize(new java.awt.Dimension(121, 21));
        panAccHd.setPreferredSize(new java.awt.Dimension(21, 200));
        panAccHd.setLayout(new java.awt.GridBagLayout());

        txtAcctNo.setToolTipText("Debit Account Number / Debit Account Head");
        txtAcctNo.setAllowAll(true);
        txtAcctNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcctNoActionPerformed(evt);
            }
        });
        txtAcctNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAcctNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAccHd.add(txtAcctNo, gridBagConstraints);

        btnAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnAccNo.setToolTipText("Account Number");
        btnAccNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAccNoActionPerformed(evt);
            }
        });
        panAccHd.add(btnAccNo, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panAccountDetails1.add(panAccHd, gridBagConstraints);

        panAcctNo.setMinimumSize(new java.awt.Dimension(121, 21));
        panAcctNo.setPreferredSize(new java.awt.Dimension(21, 200));
        panAcctNo.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, -8, 1, 0);
        panAccountDetails1.add(panAcctNo, gridBagConstraints);

        panDebitAccHead.setLayout(new java.awt.GridBagLayout());

        txtTransProductId.setToolTipText("Debit Product");
        txtTransProductId.setAllowAll(true);
        txtTransProductId.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransProductId.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtTransProductIdFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDebitAccHead.add(txtTransProductId, gridBagConstraints);

        btnTransProductId.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnTransProductId.setPreferredSize(new java.awt.Dimension(21, 21));
        btnTransProductId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTransProductIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panDebitAccHead.add(btnTransProductId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, -28, 0, 0);
        panAccountDetails1.add(panDebitAccHead, gridBagConstraints);

        rdoInvestmentAcHd.setText("Other Bank ");
        rdoInvestmentAcHd.setMaximumSize(new java.awt.Dimension(145, 18));
        rdoInvestmentAcHd.setMinimumSize(new java.awt.Dimension(145, 18));
        rdoInvestmentAcHd.setPreferredSize(new java.awt.Dimension(145, 18));
        rdoInvestmentAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoInvestmentAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails1.add(rdoInvestmentAcHd, gridBagConstraints);

        rdoGlAcHd.setText("GL A/c Head");
        rdoGlAcHd.setToolTipText("GL A/c Head");
        rdoGlAcHd.setMaximumSize(new java.awt.Dimension(120, 18));
        rdoGlAcHd.setMinimumSize(new java.awt.Dimension(120, 18));
        rdoGlAcHd.setPreferredSize(new java.awt.Dimension(120, 18));
        rdoGlAcHd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoGlAcHdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails1.add(rdoGlAcHd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(16, 2, 3, 2);
        panGeneralRemittance.add(panAccountDetails1, gridBagConstraints);

        tabRemittanceProduct.addTab("General", panGeneralRemittance);

        panBranchAndRttNoRemittance.setMinimumSize(new java.awt.Dimension(790, 270));
        panBranchAndRttNoRemittance.setPreferredSize(new java.awt.Dimension(790, 270));
        panBranchAndRttNoRemittance.setLayout(new java.awt.GridBagLayout());

        panProdDescRemitProdBranch.setMinimumSize(new java.awt.Dimension(231, 48));
        panProdDescRemitProdBranch.setPreferredSize(new java.awt.Dimension(231, 55));
        panProdDescRemitProdBranch.setLayout(new java.awt.GridBagLayout());

        lblProdIDRemitProdBrch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdIDRemitProdBrch.setText("Product ID");
        lblProdIDRemitProdBrch.setMaximumSize(new java.awt.Dimension(63, 21));
        lblProdIDRemitProdBrch.setMinimumSize(new java.awt.Dimension(63, 21));
        lblProdIDRemitProdBrch.setPreferredSize(new java.awt.Dimension(63, 21));
        lblProdIDRemitProdBrch.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 4, 4);
        panProdDescRemitProdBranch.add(lblProdIDRemitProdBrch, gridBagConstraints);

        lblProdDescRemitProdBrch.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblProdDescRemitProdBrch.setText("Product Name");
        lblProdDescRemitProdBrch.setMaximumSize(new java.awt.Dimension(85, 21));
        lblProdDescRemitProdBrch.setMinimumSize(new java.awt.Dimension(85, 21));
        lblProdDescRemitProdBrch.setPreferredSize(new java.awt.Dimension(85, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 15, 0, 4);
        panProdDescRemitProdBranch.add(lblProdDescRemitProdBrch, gridBagConstraints);

        lblProdDescRemitProdBranch.setMaximumSize(new java.awt.Dimension(200, 21));
        lblProdDescRemitProdBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        lblProdDescRemitProdBranch.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panProdDescRemitProdBranch.add(lblProdDescRemitProdBranch, gridBagConstraints);

        lblProdIDRemitProdBranch.setMinimumSize(new java.awt.Dimension(100, 21));
        lblProdIDRemitProdBranch.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 4, 0);
        panProdDescRemitProdBranch.add(lblProdIDRemitProdBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 54, 4, 4);
        panBranchAndRttNoRemittance.add(panProdDescRemitProdBranch, gridBagConstraints);

        panBrchRemittance.setMinimumSize(new java.awt.Dimension(750, 338));
        panBrchRemittance.setPreferredSize(new java.awt.Dimension(750, 338));
        panBrchRemittance.setLayout(new java.awt.GridBagLayout());

        panBranchRemittance.setBorder(javax.swing.BorderFactory.createTitledBorder("Branch and Remittance Number"));
        panBranchRemittance.setMinimumSize(new java.awt.Dimension(330, 332));
        panBranchRemittance.setPreferredSize(new java.awt.Dimension(330, 332));
        panBranchRemittance.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchRemittance.add(sptRemitButtons, gridBagConstraints);

        panBrchRemittNumberButton.setLayout(new java.awt.GridBagLayout());

        btnBrchRemittNumberSave.setText("Save");
        btnBrchRemittNumberSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrchRemittNumberSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBrchRemittNumberButton.add(btnBrchRemittNumberSave, gridBagConstraints);

        btnBrchRemittNumberDelete.setText("Delete");
        btnBrchRemittNumberDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrchRemittNumberDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBrchRemittNumberButton.add(btnBrchRemittNumberDelete, gridBagConstraints);

        btnBrchRemittNumberNew.setText("New");
        btnBrchRemittNumberNew.setNextFocusableComponent(btnBankCode);
        btnBrchRemittNumberNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrchRemittNumberNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBrchRemittNumberButton.add(btnBrchRemittNumberNew, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panBranchRemittance.add(panBrchRemittNumberButton, gridBagConstraints);

        panRemitType.setMinimumSize(new java.awt.Dimension(310, 174));
        panRemitType.setPreferredSize(new java.awt.Dimension(310, 174));
        panRemitType.setLayout(new java.awt.GridBagLayout());

        cboRttTypeBR.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRttTypeBR.setNextFocusableComponent(txtRttLimitBR);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(cboRttTypeBR, gridBagConstraints);

        txtRttLimitBR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtRttLimitBR.setNextFocusableComponent(txtIVNoRR);
        txtRttLimitBR.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(txtRttLimitBR, gridBagConstraints);

        txtIVNoRR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtIVNoRR.setNextFocusableComponent(txtOVNoRR);
        txtIVNoRR.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(txtIVNoRR, gridBagConstraints);

        txtOVNoRR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtOVNoRR.setNextFocusableComponent(txtMinAmtRR);
        txtOVNoRR.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(txtOVNoRR, gridBagConstraints);

        txtMinAmtRR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMinAmtRR.setNextFocusableComponent(txtMaxAmtRR);
        txtMinAmtRR.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(txtMinAmtRR, gridBagConstraints);

        txtMaxAmtRR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtMaxAmtRR.setNextFocusableComponent(btnBrchRemittNumberSave);
        txtMaxAmtRR.setValidation(new NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(txtMaxAmtRR, gridBagConstraints);

        lblRttTypeBR.setText("Transmission Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(lblRttTypeBR, gridBagConstraints);

        lblRttLimitBR.setText("Remittance Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(lblRttLimitBR, gridBagConstraints);

        lblIVNoRR.setText("Last Inward Variable No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(lblIVNoRR, gridBagConstraints);

        lblOVNoRR.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblOVNoRR.setText("Last Outward Variable No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(lblOVNoRR, gridBagConstraints);

        lblMinAmtRR.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMinAmtRR.setText("Minimum Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(lblMinAmtRR, gridBagConstraints);

        lblMaxAmtRR.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMaxAmtRR.setText("Maximum Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitType.add(lblMaxAmtRR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 1, 0, 10);
        panBranchRemittance.add(panRemitType, gridBagConstraints);

        panRemitBankCode.setLayout(new java.awt.GridBagLayout());

        panBankCode.setLayout(new java.awt.GridBagLayout());

        btnBankCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBankCode.setEnabled(false);
        btnBankCode.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBankCode.setNextFocusableComponent(btnBranchName);
        btnBankCode.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBankCode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBankCodeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panBankCode.add(btnBankCode, gridBagConstraints);

        txtBankCode.setMinimumSize(new java.awt.Dimension(76, 21));
        txtBankCode.setPreferredSize(new java.awt.Dimension(78, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBankCode.add(txtBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitBankCode.add(panBankCode, gridBagConstraints);

        panBranchName.setLayout(new java.awt.GridBagLayout());

        btnBranchName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnBranchName.setEnabled(false);
        btnBranchName.setMinimumSize(new java.awt.Dimension(21, 21));
        btnBranchName.setNextFocusableComponent(cboRttTypeBR);
        btnBranchName.setPreferredSize(new java.awt.Dimension(21, 21));
        btnBranchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBranchNameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panBranchName.add(btnBranchName, gridBagConstraints);

        txtBranchName.setMinimumSize(new java.awt.Dimension(76, 21));
        txtBranchName.setPreferredSize(new java.awt.Dimension(78, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panBranchName.add(txtBranchName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitBankCode.add(panBranchName, gridBagConstraints);

        txtBranchCodeBR.setMinimumSize(new java.awt.Dimension(100, 21));
        txtBranchCodeBR.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitBankCode.add(txtBranchCodeBR, gridBagConstraints);

        lblBranchCodeBR.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 68, 4, 4);
        panRemitBankCode.add(lblBranchCodeBR, gridBagConstraints);

        lblBankCode.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 68, 4, 4);
        panRemitBankCode.add(lblBankCode, gridBagConstraints);

        lblBranchNameBR.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 68, 4, 4);
        panRemitBankCode.add(lblBranchNameBR, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panBranchRemittance.add(panRemitBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBrchRemittance.add(panBranchRemittance, gridBagConstraints);

        srpBrchRemittNumber.setMinimumSize(new java.awt.Dimension(390, 320));
        srpBrchRemittNumber.setPreferredSize(new java.awt.Dimension(450, 320));

        tblBrchRemittNumber.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblBrchRemittNumber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBrchRemittNumberMouseClicked(evt);
            }
        });
        srpBrchRemittNumber.setViewportView(tblBrchRemittNumber);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBrchRemittance.add(srpBrchRemittNumber, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchAndRttNoRemittance.add(panBrchRemittance, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBranchAndRttNoRemittance.add(sptProdDescRemitProdBranch, gridBagConstraints);

        tabRemittanceProduct.addTab("Branch / Remittance Number", panBranchAndRttNoRemittance);

        panCharges.setMinimumSize(new java.awt.Dimension(710, 450));
        panCharges.setPreferredSize(new java.awt.Dimension(710, 450));
        panCharges.setLayout(new java.awt.GridBagLayout());

        sprRemitProdCharges.setMinimumSize(new java.awt.Dimension(330, 210));
        sprRemitProdCharges.setPreferredSize(new java.awt.Dimension(330, 210));

        tblRemitProdCharges.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblRemitProdCharges.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRemitProdChargesMouseClicked(evt);
            }
        });
        sprRemitProdCharges.setViewportView(tblRemitProdCharges);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(sprRemitProdCharges, gridBagConstraints);

        panRemitProdCharges.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panRemitProdCharges.setMinimumSize(new java.awt.Dimension(199, 150));
        panRemitProdCharges.setPreferredSize(new java.awt.Dimension(180, 200));
        panRemitProdCharges.setLayout(new java.awt.GridBagLayout());

        panRemitProdChargesFields.setMinimumSize(new java.awt.Dimension(300, 310));
        panRemitProdChargesFields.setPreferredSize(new java.awt.Dimension(300, 310));
        panRemitProdChargesFields.setLayout(new java.awt.GridBagLayout());

        lblChargeType.setText("Charge Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblChargeType, gridBagConstraints);

        lblCategory.setText("Customer Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblCategory, gridBagConstraints);

        lblAmtRangeFrom.setText("Amt Range from");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblAmtRangeFrom, gridBagConstraints);

        lblAmtRangeTo.setText("Amt Range to");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblAmtRangeTo, gridBagConstraints);

        lblCharges.setText("Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblCharges, gridBagConstraints);

        cboChargeType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboChargeType.setNextFocusableComponent(cboCategory);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(cboChargeType, gridBagConstraints);

        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(135);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(cboCategory, gridBagConstraints);

        txtCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCharges.setNextFocusableComponent(btnRemitProdChargesSave);
        txtCharges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChargesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtCharges, gridBagConstraints);

        lblPercentage.setText("Percentage");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblPercentage, gridBagConstraints);

        lblForEvery.setText("For Every");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblForEvery, gridBagConstraints);

        txtPercentage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPercentageActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtPercentage, gridBagConstraints);

        txtForEvery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtForEveryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtForEvery, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtRateVal, gridBagConstraints);

        cboRateType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRateTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(cboRateType, gridBagConstraints);

        txtAmtRangeFrom.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtRangeFrom.setNextFocusableComponent(btnRemitProdChargesSave);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtAmtRangeFrom, gridBagConstraints);

        txtAmtRangeTo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmtRangeTo.setNextFocusableComponent(btnRemitProdChargesSave);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtAmtRangeTo, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtServiceTax, gridBagConstraints);

        lblServiceTax.setText("Service Tax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblServiceTax, gridBagConstraints);

        lblMaximumAmt.setText("Maximum Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblMaximumAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtMaxAmt, gridBagConstraints);

        lblMinimumAmt.setText("Minimum Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesFields.add(lblMinimumAmt, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRemitProdChargesFields.add(txtMinimumAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdCharges.add(panRemitProdChargesFields, gridBagConstraints);

        panRemitProdChargesButtons.setLayout(new java.awt.GridBagLayout());

        btnRemitProdChargesNew.setText("New");
        btnRemitProdChargesNew.setNextFocusableComponent(cboChargeType);
        btnRemitProdChargesNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemitProdChargesNewActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnRemitProdChargesNew, gridBagConstraints);

        btnRemitProdChargesSave.setText("Save");
        btnRemitProdChargesSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemitProdChargesSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnRemitProdChargesSave, gridBagConstraints);

        btnRemitProdChargesDelete.setText("Delete");
        btnRemitProdChargesDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemitProdChargesDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdChargesButtons.add(btnRemitProdChargesDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdCharges.add(panRemitProdChargesButtons, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panRemitProdCharges.add(sptRemitProdChargesFieldsAndButtons, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panCharges.add(panRemitProdCharges, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(sptProdDescRemitProdCharges, gridBagConstraints);

        panProdDescRemitProdCharges.setMinimumSize(new java.awt.Dimension(210, 110));
        panProdDescRemitProdCharges.setPreferredSize(new java.awt.Dimension(210, 110));
        panProdDescRemitProdCharges.setRequestFocusEnabled(false);
        panProdDescRemitProdCharges.setLayout(new java.awt.GridBagLayout());

        lblProdDescRemitProdCharge.setMaximumSize(new java.awt.Dimension(200, 21));
        lblProdDescRemitProdCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        lblProdDescRemitProdCharge.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblProdDescRemitProdCharge, gridBagConstraints);

        lblChargesBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblChargesBranchCode, gridBagConstraints);

        lblDisplayBankCode.setMinimumSize(new java.awt.Dimension(100, 21));
        lblDisplayBankCode.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblDisplayBankCode, gridBagConstraints);

        lblDisplayBranchCode.setMinimumSize(new java.awt.Dimension(100, 21));
        lblDisplayBranchCode.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblDisplayBranchCode, gridBagConstraints);

        lblProdIDRemitProdChrg.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblProdIDRemitProdChrg, gridBagConstraints);

        lblProdIDRemitProdCharge.setMinimumSize(new java.awt.Dimension(100, 21));
        lblProdIDRemitProdCharge.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblProdIDRemitProdCharge, gridBagConstraints);

        lblProdDescRemitProdChrg.setText("Product Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblProdDescRemitProdChrg, gridBagConstraints);

        lblChargesBankCode.setText("Bank Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProdDescRemitProdCharges.add(lblChargesBankCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCharges.add(panProdDescRemitProdCharges, gridBagConstraints);

        panAliasBrchRemittNumber.setLayout(new java.awt.GridBagLayout());

        srpAliasBrchRemittNumber.setMinimumSize(new java.awt.Dimension(350, 100));
        srpAliasBrchRemittNumber.setPreferredSize(new java.awt.Dimension(350, 100));

        tblAliasBrchRemittNumber.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tblAliasBrchRemittNumber.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAliasBrchRemittNumberMousePressed(evt);
            }
        });
        srpAliasBrchRemittNumber.setViewportView(tblAliasBrchRemittNumber);

        panAliasBrchRemittNumber.add(srpAliasBrchRemittNumber, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panCharges.add(panAliasBrchRemittNumber, gridBagConstraints);

        tabRemittanceProduct.addTab("Charges", panCharges);

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

    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // TODO add your handling code here:
        actionAuthExcepReject = false;
        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_COPY);
        callView("Copy");
        observable.existingData();
    }//GEN-LAST:event_btnCopyActionPerformed

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquirystatus");
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
        private void cboPayableBranchGRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPayableBranchGRActionPerformed
            //        if (cboPayableBranchGR.getSelectedIndex() > 0) {
            //            if (CommonUtil.convertObjToStr(observable.getCbmPayableBranchGR().getKeyForSelected()).equals("ISSUE_BRAN")) {
            //
            //            }
            //        }
    }//GEN-LAST:event_cboPayableBranchGRActionPerformed
            private void cboRateTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRateTypeActionPerformed
                // TODO add your handling code here:
                if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
                    String key = CommonUtil.convertObjToStr(((ComboBoxModel)cboRateType.getModel()).getKeyForSelected());
                    if (key != null && !key.equals("")) {
                        txtRateVal.setText("");
                        if (key.equals("AMOUNT")) {
                            txtRateVal.setValidation(new CurrencyValidation(14, 2));
                        } else if  (key.equals("PERCENTAGE")) {
                            txtRateVal.setValidation(new PercentageValidation());
                        }
                    }
                }
    }//GEN-LAST:event_cboRateTypeActionPerformed
            //    private void enableDisableAliasBranchTable(boolean flag) {
            //        tblAliasBrchRemittNumber.setEnabled(flag);
            //    }
    private void tblAliasBrchRemittNumberMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAliasBrchRemittNumberMousePressed
        // TODO add your handling code here:
        //        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && actionAuthExcepReject == false ){
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            if (tblAliasBrchRemittNumber.getSelectedRow()>=0) {
                //                if (rdoSeriesGR_Yes.isSelected()) {
                tblAliasSelected = true;
                //                    enableDisableAliasBranchTable(true);
                //                    updateOBFields();
                observable.populateBrchRemittNumberTab(tblAliasBrchRemittNumber.getSelectedRow());
                observable.setChargesBankBranchCodes();
                observable.remitProdBrnchSelectedRow = tblBrchRemittNumber.getSelectedRow();
                //                    ClientUtil.enableDisable(panBranchRemittance,true);
                
                updateChargesBankBranchDisplay();
                observable.tableBranchRemittanceNumberIsSelected();
                
                updateChargesOB();
                //                observable.getDataForCharges();
                //                observable.chargeExistingData();
                enableDisableRemitProductBrchTableField(false);
                enableDisableTableBtn(false);
                //                    setRemitProdBrchBtnEndableDisable();
                //                    if (actionAuthExcepReject){
                //                        remitBtnDefaultEnableDisable(false);
                //                    }
                //                    if(observable.getRdoSeriesGR_No() == true){
                //                        btnBrchRemittNumberNew.setEnabled(btnNew.isEnabled());
                //                    }
                //                    btnBrchRemittNumberSaveActionPerformed(null);
                //                } else {
                //                    enableDisableAliasBranchTable(false);
                //                }
            }
        }
    }//GEN-LAST:event_tblAliasBrchRemittNumberMousePressed
    
    private void rdoSeriesGR_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSeriesGR_NoActionPerformed
        // TODO add your handling code here:
        if (rdoSeriesGR_No.isSelected()){
            //            resetRemitProdBranchTab();
            //            enableDisableAliasBranchTable(false);
        }
    }//GEN-LAST:event_rdoSeriesGR_NoActionPerformed
    
    private void rdoSeriesGR_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSeriesGR_YesActionPerformed
        // TODO add your handling code here:
        if (rdoSeriesGR_Yes.isSelected()){
            //            resetRemitProdBranchTab();
            //            enableDisableAliasBranchTable(true);
        }
        
    }//GEN-LAST:event_rdoSeriesGR_YesActionPerformed
    
    private void txtProductDescGRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductDescGRFocusLost
        // TODO add your handling code here:
        updateOBFields();
        observable.setLableProductDesc();
    }//GEN-LAST:event_txtProductDescGRFocusLost
    
    private void txtProductIdGRFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIdGRFocusLost
        // TODO add your handling code here:
        // Added by nithya on 19-06-2016
        HashMap whereMap = new HashMap();
        whereMap.put("PROD_ID", txtProductIdGR.getText());
        List lst = ClientUtil.executeQuery("getAllProductIds", whereMap);
        System.out.println("getSBODBorrowerEligAmt : " + lst);
        if (lst != null && lst.size() > 0) {
            HashMap existingProdIdMap = (HashMap) lst.get(0);
            if (existingProdIdMap.containsKey("PROD_ID")) {
                ClientUtil.showMessageWindow("Id is already exists for Product " + existingProdIdMap.get("PRODUCT") + "\n Please change");
                txtProductIdGR.setText("");
            }
        } else {
            updateOBFields();
            observable.setLableProductID();
        }
    }//GEN-LAST:event_txtProductIdGRFocusLost
    
    private void rdoEFTProductGR_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEFTProductGR_NoActionPerformed
        // TODO add your handling code here:
        if (rdoEFTProductGR_No.isSelected()){
            updateOBFields();
            observable.resetPrintRadioButton();
            ClientUtil.enableDisable(panPrintServicesGR, true);
            enabledisableRTGSFields(false);
        }
    }//GEN-LAST:event_rdoEFTProductGR_NoActionPerformed
    
    private void rdoEFTProductGR_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoEFTProductGR_YesActionPerformed
        // TODO add your handling code here:
        if (rdoEFTProductGR_Yes.isSelected()){
            rdoPrintServicesGR_Yes.setSelected(false);
            rdoPrintServicesGR_No.setSelected(true);
            ClientUtil.enableDisable(panPrintServicesGR, false);
            enabledisableRTGSFields(true);
        }
    }//GEN-LAST:event_rdoEFTProductGR_YesActionPerformed
    
    private void tblBrchRemittNumberMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBrchRemittNumberMouseClicked
        // Add your handling code here:
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            updateOBFields();
            observable.populateBrchRemittNumberTab(tblBrchRemittNumber.getSelectedRow());
            observable.remitProdBrnchSelectedRow = tblBrchRemittNumber.getSelectedRow();
            enableDisableRemitProductBrchTableField(false);
            enableDisableTableBtn(false);
            if (actionAuthExcepReject){
                remitBtnDefaultEnableDisable(false);
                ClientUtil.enableDisable(panBranchRemittance,false);
            }else{
                remitBtnDefaultEnableDisable(true);
                ClientUtil.enableDisable(panBranchRemittance,true);
            }
            //            if(observable.getRdoSeriesGR_No() == true){
            //                btnBrchRemittNumberNew.setEnabled(btnNew.isEnabled());
            //            }
            
        }
    }//GEN-LAST:event_tblBrchRemittNumberMouseClicked
    private void updateChargesBankBranchDisplay(){
        lblDisplayBankCode.setText(observable.getLblChargesBankCode());
        lblDisplayBranchCode.setText(observable.getLblChargesBranchCode());
    }
    private void btnBranchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBranchNameActionPerformed
        // Add your handling code here:
        callView("BranchName");
    }//GEN-LAST:event_btnBranchNameActionPerformed
    
    private void btnBankCodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBankCodeActionPerformed
        // Add your handling code here:
        callView("BankCode");
    }//GEN-LAST:event_btnBankCodeActionPerformed
    
    private void btnBrchRemittNumberDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrchRemittNumberDeleteActionPerformed
        // Add your handling code here:
        boolean dataExist = false;
        updateOBFields();
        observable.remitProdBrnchTabSelected = false;
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW ){
            dataExist = observable.remittanceProductBranchDelete();
        }
        if(!dataExist){
            observable.deleteRemittanceProductBranchTab();
        }else {
            observable.displayCDialogue("deleteWarning");
        }
        observable.resetRemitProdBrchFields();
        setRemitProdBrchtabBtnEndableDisable(false);
        btnBranchName.setEnabled(btnNew.isEnabled());
        btnBankCode.setEnabled(btnNew.isEnabled());
        ClientUtil.enableDisable(panBranchRemittance, false);
    }//GEN-LAST:event_btnBrchRemittNumberDeleteActionPerformed
    
    private void btnBrchRemittNumberSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrchRemittNumberSaveActionPerformed
        // Add your handling code here:
        if(!btnBankCode.isEnabled()){
            final String remitTypeMandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panRemitType);
            /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
            if (remitTypeMandatoryMessage.length() > 0){
                displayAlert(remitTypeMandatoryMessage);
            }else{
                doaddRemittanceProductBranchTable();
            }
        }else{
            
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panBranchRemittance);
            /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
            if (mandatoryMessage.length() > 0){
                displayAlert(mandatoryMessage);
            }else{
                doaddRemittanceProductBranchTable();
            }
        }
    }//GEN-LAST:event_btnBrchRemittNumberSaveActionPerformed
    
    private void remitBtnDefaultEnableDisable(boolean enableDisable) {
        btnBrchRemittNumberNew.setEnabled(enableDisable);
        btnBrchRemittNumberSave.setEnabled(enableDisable);
        btnBrchRemittNumberDelete.setEnabled(enableDisable);
    }
    
    /* To enable or disable Remittance buttons when any one of them is selected*/
    private void remitBtnOnSelectionEnableDisable() {
        btnBrchRemittNumberNew.setEnabled(true);
        btnBrchRemittNumberSave.setEnabled(true);
        btnBrchRemittNumberDelete.setEnabled(false);
    }
    
    /* To enable only the Remittance New button*/
    private void remitBtnEnableDisable() {
        btnBrchRemittNumberNew.setEnabled(true);
        btnBrchRemittNumberSave.setEnabled(false);
        btnBrchRemittNumberDelete.setEnabled(false);
    }
    
    private void btnBrchRemittNumberNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrchRemittNumberNewActionPerformed
        // Add your handling code here:
        updateOBFields();
        updateChargesOB();
        observable.setBankName("");
        //        if (observable.getRdoSeriesGR_Yes() == true){
        observable.remitProdBrnchTabSelected = false;
        ClientUtil.enableDisable(panBranchRemittance,true);
        observable.resetRemitProdBrchFields();
        enableDisableRemitProductBrchTableField(false);
        enableDisableTableBtn(true);
        remitBtnOnSelectionEnableDisable();
        //            btnBrchRemittNumberSave.setEnabled(!btnNew.isEnabled());
        //            btnBrchRemittNumberDelete.setEnabled(btnNew.isEnabled());
        //            btnBrchRemittNumberNew.setEnabled(!btnNew.isEnabled());
        //        }else if (observable.getRdoSeriesGR_No() == true){
        //            observable.remitProdBrnchTabSelected = false;
        //            ClientUtil.enableDisable(panBranchRemittance,true);
        //            observable.resetRemitProdBrchFields();
        //            enableDisableRemitProductBrchTableField(false);
        //            btnBrchRemittNumberSave.setEnabled(!btnNew.isEnabled());
        //            btnBrchRemittNumberDelete.setEnabled(btnNew.isEnabled());
        //            btnBrchRemittNumberNew.setEnabled(btnNew.isEnabled());
        //        }else{
        //            observable.displayCDialogue("maintainSeriesWarning");
        //        }
        
        final String PAYBRANCH = CommonUtil.convertObjToStr(((ComboBoxModel)(cboPayableBranchGR.getModel())).getKeyForSelected());
        HashMap resultMap = TrueTransactMain.BRANCHINFO;
        resultMap.putAll(TrueTransactMain.BANKINFO);
        
        //__ if the Selected type is Issue Branch...
        if(PAYBRANCH.equalsIgnoreCase("ISSU_BRANCH")){
            //            txtBankCode.setText(CommonUtil.convertObjToStr(resultMap.get("BANK_CODE")));
            //            txtBranchName.setText(CommonUtil.convertObjToStr(resultMap.get("BRANCH_NAME")));
            //            txtBranchCodeBR.setText(CommonUtil.convertObjToStr(resultMap.get(CommonConstants.BRANCH_ID)));
            
            //__ To disable the Buttons...
            enableDisableTableBtn(false);
        }
        //__ if the Selected type is Any Branch of the Issue Bank...
        else if(PAYBRANCH.equalsIgnoreCase("ANY_BRANCH_BANK")){
            txtBankCode.setText(CommonUtil.convertObjToStr(resultMap.get("BANK_CODE")));
            btnBankCode.setEnabled(false);
            btnBranchName.setEnabled(true);
        }
        
        //__ if the Selected type is Designated Branch of the Other Bank...
        else if(PAYBRANCH.equalsIgnoreCase("DESIG_OTHER_BANK_BRAN")){
            enableDisableTableBtn(true);
        }
        
        else if(PAYBRANCH.equalsIgnoreCase("ANY_BANK_BRANCH")){
            enableDisableTableBtn(true);
        }
        
    }//GEN-LAST:event_btnBrchRemittNumberNewActionPerformed
    
    private void tblRemitProdChargesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRemitProdChargesMouseClicked
        // Add your handling code here:
        //        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && actionAuthExcepReject == false ){
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE){
            if (tblRemitProdCharges.getSelectedRow()>=0) {
                
                tblChargesSelected = true;
                
                updateChargesOB();
                observable.getKeyFromTable(tblRemitProdCharges.getSelectedRow());
                updateChargesCombo();
                // tblRemitProdCharges.getRowCount()
                observable.populateRemitProdCharge();
                //            observable.populateRemitProdChrgTab(tblRemitProdCharges.getSelectedRow());
                observable.remitProdChrgSelectedRow = tblRemitProdCharges.getSelectedRow();
                ClientUtil.enableDisable(panRemitProdChargesFields,true);
                enableDisableRemitProductChrgTableField(false);
                setRemitProdChrgBtnEndableDisable();
                if (actionAuthExcepReject){
                    //                    setRemitProdChrgBtnEndableDisable();
                    remitProdChrgDefaultEnableDisableBtn(false);
                    ClientUtil.enableDisable(panRemitProdChargesFields,false);
                }
            }
        }
    }//GEN-LAST:event_tblRemitProdChargesMouseClicked
    
    private void btnRemitProdChargesDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemitProdChargesDeleteActionPerformed
        // Add your handling code here:
        boolean dataExist = false;
        updateOBFields();
        updateChargesOB();
        observable.remitProdChrgTabSelected = false;
        if(observable.getActionType() != ClientConstants.ACTIONTYPE_NEW ){
            dataExist = observable.remittanceProductChargeDelete();
        }
        if(!dataExist){
            // The Product is not Issued yet, you can Delete the Product
            observable.deleteRemittanceProductCharges(tblRemitProdCharges.getSelectedRow());
        }else {
            // The Product is already Issued  ( so can't Delete )
            observable.displayCDialogue("deleteWarning");
        }
        btnRemitProdChargesSave.setEnabled(btnNew.isEnabled());
        btnRemitProdChargesDelete.setEnabled(btnNew.isEnabled());
        btnRemitProdChargesNew.setEnabled(!btnNew.isEnabled());
        observable.resetRemitProdChrgFields();
        ClientUtil.enableDisable(panRemitProdChargesFields, false);
    }//GEN-LAST:event_btnRemitProdChargesDeleteActionPerformed
    
    private void btnRemitProdChargesSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemitProdChargesSaveActionPerformed
        // Add your handling code here:
        updateOBFields();
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panRemitProdChargesFields);
        
        if (chargeCheck()){
            mandatoryMessage = mandatoryMessage + resourceBundle.getString("chargeCheck");
        }
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else if (!forEveryCheck()){
            displayAlert(resourceBundle.getString("forEveryCheck"));
        }else {
            addRemittanceProductChargesTable();
        }
    }//GEN-LAST:event_btnRemitProdChargesSaveActionPerformed
    
    private boolean forEveryCheck(){
        if ((txtForEvery == null || txtForEvery.getText().length() == 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() == 0) && cboRateType.getSelectedIndex() <= 0 && (txtRateVal == null || txtRateVal.getText().length() == 0 || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() == 0)){
            return true;
        }else if ((txtForEvery != null && txtForEvery.getText().length() != 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() != 0) && cboRateType.getSelectedIndex() > 0 && (txtRateVal != null && txtRateVal.getText().length() != 0 || ! txtRateVal.getText().equals("0") || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() != 0))  {
            return true;
        }
        return false;
    }
    
    private boolean chargeCheck(){
        // Checking for whether Charges and Percentage is containing proper value(s)
        if ((txtCharges == null || txtCharges.getText().length() == 0 || CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue() == 0) && (txtPercentage == null || txtPercentage.getText().length() == 0 || CommonUtil.convertObjToDouble(txtPercentage.getText()).doubleValue() == 0)){
            if ((txtForEvery == null ||txtForEvery.getText().length() == 0 || CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue() == 0) && cboRateType.getSelectedIndex() <= 0 && (txtRateVal == null || txtRateVal.getText().length() == 0 || CommonUtil.convertObjToDouble(txtRateVal.getText()).doubleValue() == 0)){
                return true;
            }
        }
        return false;
    }
    private void btnRemitProdChargesNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemitProdChargesNewActionPerformed
        // Add your handling code here:
        
        tblChargesSelected = false;
        //        if (rdoSeriesGR_Yes.isSelected()) {
        if (tblAliasSelected == true) {
            ClientUtil.enableDisable(panRemitProdChargesFields,true);
            observable.remitProdChrgTabSelected = false;
            observable.resetRemitProdChrgFields();
            btnRemitProdChargesSave.setEnabled(!btnNew.isEnabled());
            btnRemitProdChargesDelete.setEnabled(btnNew.isEnabled());
            btnRemitProdChargesNew.setEnabled(!btnNew.isEnabled());
        } else {
            displayAlert(resourceBundle.getString("Warning"));
        }
        //        } else {
        //            ClientUtil.enableDisable(panRemitProdChargesFields,true);
        //            observable.remitProdChrgTabSelected = false;
        //            observable.resetRemitProdChrgFields();
        //            btnRemitProdChargesSave.setEnabled(!btnNew.isEnabled());
        //            btnRemitProdChargesDelete.setEnabled(btnNew.isEnabled());
        //            btnRemitProdChargesNew.setEnabled(btnNew.isEnabled());
        //        }
        
    }//GEN-LAST:event_btnRemitProdChargesNewActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //__ If there's no data to be Authorized, call Cancel action...
        if(!isModified()){
            setButtonEnableDisable();
            btnCancelActionPerformed(null);
        }
         btnDdIssueHead.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        actionAuthExcepReject = false;
        ClientUtil.enableDisable(panProdDescGeneralRemittance, true);
        ClientUtil.enableDisable(panInsideGeneralRemittance, true);
        ClientUtil.enableDisable(panOtherGR, true);
        ClientUtil.enableDisable(panLapsedGR, false);
        ClientUtil.enableDisable(panIsLapsedGR, true);
        
        ClientUtil.enableDisable(panBranchRemittance, false);
        ClientUtil.enableDisable(panRemitProdCharges, false);
        
        txtProductIdGR.setEnabled(true);
        txtProductIdGR.setEditable(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        resetHelpButton();
        setButtonEnableDisable();
        btnLapsedHeadGRHelp.setEnabled(false);
        btnBrchRemittNumberNew.setEnabled(true);
        btnRemitProdChargesNew.setEnabled(true);
        tblAliasSelected = false;
        btnCopy.setEnabled(false);
        setVisbleRTGSSuspenceHead(false);
        rdoGlAcHd.setEnabled(true);
        rdoInvestmentAcHd.setEnabled(true);
        txtTransProductId.setEnabled(false);
        txtAcctNo.setEnabled(false);
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        actionAuthExcepReject = false;
        tblAliasSelected = false;
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        observable.existingData();
        txtTransProductId.setEnabled(false);
        txtAcctNo.setEnabled(false);
        btnAccNo.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        actionAuthExcepReject = false;
        tblAliasSelected = false;
        final String[] options = {resourceBundle.getString("cDialogOk"),resourceBundle.getString("cDialogCancel")};
        final int option = COptionPane.showOptionDialog(null, resourceBundle.getString("deleteOptionWarning"), CommonConstants.WARNINGTITLE,
        COptionPane.OK_CANCEL_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        if (option == 0){
            observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
            observable.doSave();
            resetHelpButton();
            setButtonEnableDisable();
            setRemitProdBrchBtnEndableDisable();
            setRemitProdChrgBtnEndableDisable();
            resetBranchTable();
            resetChargeTable();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            observable.resetRemitProdBrchFields();
            observable.resetRemitProdChrgFields();
            observable.resetOBFields();
            ClientUtil.enableDisable(this, false);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        RemittanceProductMRB mandatoryResourceBundle = new RemittanceProductMRB();
        StringBuffer mandatoryProdOthersGeneralMessage = new StringBuffer();
        
        mandatoryProdOthersGeneralMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panProdDescGeneralRemittance).toString());
        mandatoryProdOthersGeneralMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panInsideGeneralRemittance).toString());
        mandatoryProdOthersGeneralMessage.append(new MandatoryCheck().checkMandatory(getClass().getName(), panOtherGR).toString());
        
        final String mandatoryLapsedMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panIsLapsedGR);
        final String mandatoryRemitProdBranchMessage = resourceBundle.getString("maintainSeriesOptionNoWarning");
        StringBuffer fieldsLapsedApplicableWarning = new StringBuffer();
        fieldsLapsedApplicableWarning.append(new MandatoryCheck().checkMandatory(getClass().getName(), panLapsedGR));
        if (rdoIsLapsedGR_Yes.isSelected()){
            String str = periodLengthValidation(txtLapsedPeriodGR, cboLapsedPeriodGR);
            if(str.length() > 0){
                mandatoryProdOthersGeneralMessage.append(str+"\n");
                str = "";
            }
        }
        //Added by kannan
        if(chkNewProcedure.isSelected() && (CommonUtil.convertObjToStr(tdtStartDate.getDateValue()).equals("")
                || CommonUtil.convertObjToStr(txtDdIssueHead).equals(""))){
            ClientUtil.showMessageWindow("Start Date and DD Issue Head should Not Be Empty !!!");
            return;            
        }
        if (mandatoryProdOthersGeneralMessage.length() > 0 || mandatoryLapsedMessage.length() > 0){
            if(mandatoryProdOthersGeneralMessage.length() > 0 ){
                /** Check maditory fields for Product panel, Others panel and Inside General Remittance panel **/
                displayAlert(mandatoryProdOthersGeneralMessage.toString());
            }else if(mandatoryLapsedMessage.length() > 0){
                /** Check maditory fields for Lapsed Appicable panel **/
                displayAlert(mandatoryLapsedMessage);
            }
        }else{
            int BrchRemittNumberTabRow = tblBrchRemittNumber.getRowCount();
            //            if((rdoSeriesGR_No.isSelected()) && (BrchRemittNumberTabRow == 0 ) ){
            if(BrchRemittNumberTabRow == 0 ) {
                displayAlert(mandatoryRemitProdBranchMessage);
            }else{
                if(rdoIsLapsedGR_Yes.isSelected()){
                    int lapsedPeriod = -1;
                    if(txtLapsedPeriodGR.getText().length() > 0){
                        lapsedPeriod = Integer.parseInt(txtLapsedPeriodGR.getText());
                    }
                    if(txtLapsedHeadGR.getText().equalsIgnoreCase("")){
                        // If txt Lapsed Head is null, then append the alert message
                        fieldsLapsedApplicableWarning.append(mandatoryResourceBundle.getString("txtLapsedHeadGR"));
                        fieldsLapsedApplicableWarning.append("\n");
                    }
                    
                    if(txtRCHeadGR.getText().equalsIgnoreCase("")){
                        // If txt RC Head is null, then append the alert message
                        fieldsLapsedApplicableWarning.append(mandatoryResourceBundle.getString("txtRCHeadGR"));
                        fieldsLapsedApplicableWarning.append("\n");
                    }else if(lapsedPeriod == 0){
                        // If Lapsed Period is 0 (Zero), then append the alert message
                        fieldsLapsedApplicableWarning.append(resourceBundle.getString("lapsedPeriod"));
                        fieldsLapsedApplicableWarning.append("\n");
                    }
                    
                    /** If YES is Selected in Lapsed Appicable panel **/
                    if((!txtLapsedHeadGR.getText().equalsIgnoreCase("")) && (!txtLapsedPeriodGR.getText().equalsIgnoreCase("")) && (lapsedPeriod != 0) && (!cboLapsedPeriodGR.getSelectedItem().toString().equalsIgnoreCase("")) && (!txtRCHeadGR.getText().equalsIgnoreCase(""))){
                        /** All the fields filled DO SAVE **/
                        savePerformed();
                    }else{
                        /** Check maditory combo fields for Lapsed Appicable **/
                        displayAlert(fieldsLapsedApplicableWarning.toString());
                    }
                }else if(rdoIsLapsedGR_No.isSelected()){
                    /** If NO is selected in Lapsed Appicable pane DO SAVE **/
                    savePerformed();
                }
            }
        }
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private String periodLengthValidation(CTextField txtField, CComboBox comboField){
        String message = "";
        String key = CommonUtil.convertObjToStr(((ComboBoxModel) comboField.getModel()).getKeyForSelected());
        if (!ClientUtil.validPeriodMaxLength(txtField, key)){
            message = objMandatoryMRB.getString(txtField.getName());
        }
        return message;
    }
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        tblAliasSelected = false;
        
        actionAuthExcepReject = false;
        if(observable.getAuthorizeStatus()!=null)
            super.removeEditLock(txtProductIdGR.getText());
        observable.resetStatus();
        resetBranchTable();
        resetChargeTable();
        observable.clearData();
        observable.resetOBFields();
        observable.resetRemitProdBrchFields();
        observable.resetRemitProdChrgFields();
        resetHelpButton();
        //Following line moved down
        //        setButtonEnableDisable();
        
        //        setRemitProdBrchBtnEndableDisable();
        remitBtnDefaultEnableDisable(false);
        //        setRemitProdChrgBtnEndableDisable();
        remitProdChrgDefaultEnableDisableBtn(false);
        enableDisableRemitProductBrchTableField(false);
        enableDisableTableBtn(false);
        btnCopy.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        viewType = "";  
        setButtonEnableDisable();
        btnCopy.setEnabled(true);
        lblIssueHeadDesc.setText("");
        lblExchangeHeadDesc.setText("");
        lblTCHeadDesc.setText("");
        lblRCHeadDesc.setText("");
        lblOCHeadDesc.setText("");
        lblPayableHeadDesc.setText("");
        lblDCHeadDesc.setText("");
        lblCCHeadDesc.setText("");
        lblPostageHeadDesc.setText("");
        lblDdIssueHeadVal.setText("");
        panNewProcedurePan.setVisible(false);
        //__ Make the Screen Closable..
        setModified(false);
        cboProdTypeCr.setSelectedItem("");
        txtTransProductId.setText("");
        txtAcctNo.setText("");
        rdoGlAcHd.setSelected(false);
        rdoInvestmentAcHd.setSelected(false);
        btnTransProductId.setEnabled(false);
        btnAccNo.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
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
        actionAuthExcepReject = true;
        if (viewType.equals(AUTHORIZE) && isFilled) {
            String strWarnMsg = tabRemittanceProduct.isAllTabsVisited();
            if (strWarnMsg.length() > 0){
                displayAlert(strWarnMsg);
                return;
            }
            strWarnMsg = null;
            tabRemittanceProduct.resetVisits();
            final HashMap remittanceProductMap = new HashMap();
            remittanceProductMap.put("STATUS", authorizeStatus);
            remittanceProductMap.put("USER_ID", TrueTransactMain.USER_ID);
            remittanceProductMap.put("AUTHORIZEDT", currDt.clone());
            remittanceProductMap.put("PRODUCT ID",observable.getTxtProductIdGR());
            
            //System.out.println("remittanceProductMap: " + remittanceProductMap);
            
            ClientUtil.execute("authorizeRemittanceProduct", remittanceProductMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(txtProductIdGR.getText());
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
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getRemittanceProductAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRemittanceProduct");
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
    
    private void btnLapsedHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLapsedHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("LapsedHead");
    }//GEN-LAST:event_btnLapsedHeadGRHelpActionPerformed
    
    private void btnCCHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCCHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("CChargesHead");
    }//GEN-LAST:event_btnCCHeadGRHelpActionPerformed
    
    private void btnDCHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDCHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("DChargesHead");
    }//GEN-LAST:event_btnDCHeadGRHelpActionPerformed
    
    private void btnPostageHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPostageHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("PostageHead");
    }//GEN-LAST:event_btnPostageHeadGRHelpActionPerformed
    
    private void btnPayableHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayableHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("PayableHead");
    }//GEN-LAST:event_btnPayableHeadGRHelpActionPerformed
    
    private void btnOCHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOCHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("OtherChargesHead");
    }//GEN-LAST:event_btnOCHeadGRHelpActionPerformed
    
    private void btnRCHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRCHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("RChargesHead");
    }//GEN-LAST:event_btnRCHeadGRHelpActionPerformed
    
    private void btnTCHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTCHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("TCChrgesHead");
    }//GEN-LAST:event_btnTCHeadGRHelpActionPerformed
    
    private void btnExchangeHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExchangeHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("ExchangeHead");
    }//GEN-LAST:event_btnExchangeHeadGRHelpActionPerformed
    
    private void btnIssueHeadGRHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIssueHeadGRHelpActionPerformed
        // Add your handling code here:
        callView("IssueHead");
    }//GEN-LAST:event_btnIssueHeadGRHelpActionPerformed
    
    private void rdoIsLapsedGR_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsLapsedGR_YesActionPerformed
        // Add your handling code here:
        if (rdoIsLapsedGR_Yes.isSelected()){
            setEnableDisableLapsedBtn();
        }
    }//GEN-LAST:event_rdoIsLapsedGR_YesActionPerformed
    
    private void rdoIsLapsedGR_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoIsLapsedGR_NoActionPerformed
        // Add your handling code here:
        if (rdoIsLapsedGR_No.isSelected()){
            setEnableDisableLapsedBtn();
        }
    }//GEN-LAST:event_rdoIsLapsedGR_NoActionPerformed
    
    private void txtPercentageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPercentageActionPerformed
        // TODO add your handling code here:
        if(CommonUtil.convertObjToDouble(txtPercentage.getText()).doubleValue()>0.0){
            txtCharges.setText("");
            txtForEvery.setText("");
            cboRateType.setSelectedItem(null);
            txtRateVal.setText("");
        }
    }//GEN-LAST:event_txtPercentageActionPerformed

    private void txtChargesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChargesActionPerformed
        // TODO add your handling code here:
        if(CommonUtil.convertObjToDouble(txtCharges.getText()).doubleValue()>0.0){
            txtPercentage.setText("");
            txtForEvery.setText("");
            cboRateType.setSelectedItem(null);
            txtRateVal.setText("");
        }
        
    }//GEN-LAST:event_txtChargesActionPerformed

    private void txtForEveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtForEveryActionPerformed
        // TODO add your handling code here:
         if(CommonUtil.convertObjToDouble(txtForEvery.getText()).doubleValue()>0.0){
            txtPercentage.setText("");
            txtCharges.setText("");
           
        }
    }//GEN-LAST:event_txtForEveryActionPerformed

    private void txtDdIssueHeadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDdIssueHeadFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDdIssueHeadFocusLost

    private void chkNewProcedureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNewProcedureActionPerformed
        // TODO add your handling code here:
        //Added by kannan
        if (chkNewProcedure.isSelected()) {
            panNewProcedurePan.setVisible(true);
            btnDdIssueHead.setEnabled(true);
            txtDdIssueHead.setEnabled(false);
        } else {
            boolean visible = true;
            if (!CommonUtil.convertObjToStr(txtDdIssueHead.getText()).equals("")) {
                HashMap acHdMap = new HashMap();
                acHdMap.put("AC_HD_ID", txtDdIssueHead.getText());
                List acList = ClientUtil.executeQuery("getSelectAccountMaintenanceTO", acHdMap);
                if (acList != null && acList.size() > 0) {
                    AccountMaintenanceTO objAccountMaintenanceTO = (AccountMaintenanceTO) acList.get(0);
                    if (CommonUtil.convertObjToDouble(objAccountMaintenanceTO.getGlbalance()) > 0) {
                        ClientUtil.showMessageWindow("Balance is there, can't deselect !!!");
                        chkNewProcedure.setSelected(true);
                        visible = false;
                        return;
                    }
                }
            }
            if (visible) {//Added By Kannan
                panNewProcedurePan.setVisible(false);
                tdtStartDate.setDateValue(null);
                txtDdIssueHead.setText("");
                lblDdIssueHeadVal.setText("");
            }
        }
    }//GEN-LAST:event_chkNewProcedureActionPerformed

    private void tdtStartDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtStartDateFocusLost
        // TODO add your handling code here:
       
    }//GEN-LAST:event_tdtStartDateFocusLost

    private void btnDdIssueHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDdIssueHeadActionPerformed
        // TODO add your handling code here:
        callView("DDissueHead");
    }//GEN-LAST:event_btnDdIssueHeadActionPerformed

    private void txtMaximumAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaximumAmountFocusLost
        // TODO add your handling code here:
        if(txtMaximumAmount.getText().length()>0 && txtMinimumAmount.getText().length()>0){
            if(CommonUtil.convertObjToDouble(txtMaximumAmount.getText())<CommonUtil.convertObjToDouble(txtMinimumAmount.getText())){
                ClientUtil.showMessageWindow("Maximum Amount Should be Greater than Minimum Amount ...!!!");
                txtMaximumAmount.setText("");
            }
        }
    }//GEN-LAST:event_txtMaximumAmountFocusLost

    private void txtMinimumAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMinimumAmountFocusLost
        // TODO add your handling code here:
        if(txtMaximumAmount.getText().length()>0 && txtMinimumAmount.getText().length()>0){
            if(CommonUtil.convertObjToDouble(txtMaximumAmount.getText())<CommonUtil.convertObjToDouble(txtMinimumAmount.getText())){
                ClientUtil.showMessageWindow("Minimum Amount Should be Less than Maximum Amount ...!!!");
                txtMinimumAmount.setText("");
            }
        }
    }//GEN-LAST:event_txtMinimumAmountFocusLost

    private void btnRTGSSuspenseHeadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRTGSSuspenseHeadActionPerformed
        // TODO add your handling code here:
         callView("RTGSSuspenseHead");
    }//GEN-LAST:event_btnRTGSSuspenseHeadActionPerformed

    private void cboBehavesLikeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBehavesLikeActionPerformed
        // TODO add your handling code here:
       validateEnableDisableRTGS();
        
    }//GEN-LAST:event_cboBehavesLikeActionPerformed

    private void cboProdTypeCrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeCrActionPerformed
        // TODO add your handling code here:
        if(cboProdTypeCr.getSelectedItem()!=null && !cboProdTypeCr.getSelectedItem().equals("")){
            if(cboProdTypeCr.getSelectedItem().equals("General Ledger")){
                txtTransProductId.setEnabled(false);
                btnTransProductId.setEnabled(false);
            }else{
                txtTransProductId.setEnabled(true);
                btnTransProductId.setEnabled(true);
            }
//            observable.setPensionProductType(CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
        }
    }//GEN-LAST:event_cboProdTypeCrActionPerformed

    private void txtAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcctNoActionPerformed

    }//GEN-LAST:event_txtAcctNoActionPerformed

    private void txtAcctNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAcctNoFocusLost

    }//GEN-LAST:event_txtAcctNoFocusLost

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        callView("RTGS_NEFT_ACCT_NO");
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void txtTransProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTransProductIdFocusLost
        // TODO add your handling code here:
        if (txtTransProductId.getText() != null && txtTransProductId.getText().length() > 0) {

        }
    }//GEN-LAST:event_txtTransProductIdFocusLost

    private void btnTransProductIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTransProductIdActionPerformed
        callView("RTGS_NEFT_PROD_ID");
    }//GEN-LAST:event_btnTransProductIdActionPerformed

    private void rdoInvestmentAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoInvestmentAcHdActionPerformed
        // TODO add your handling code here:
        rdoGlAcHd.setSelected(false);
        rdoInvestmentAcHd.setSelected(true);
        if (rdoInvestmentAcHd.isSelected()) {
            cboProdTypeCr.setEnabled(true);
            btnTransProductId.setEnabled(true);
            btnAccNo.setEnabled(true);
        }else{
            cboProdTypeCr.setEnabled(false);
            btnTransProductId.setEnabled(false);
            btnAccNo.setEnabled(false);
        }
    }//GEN-LAST:event_rdoInvestmentAcHdActionPerformed

    private void rdoGlAcHdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoGlAcHdActionPerformed
        // TODO add your handling code here:
        rdoGlAcHd.setSelected(true);
        rdoInvestmentAcHd.setSelected(false);
        if (rdoGlAcHd.isSelected()) {
            cboProdTypeCr.setSelectedItem("");
            txtTransProductId.setText("");
            txtAcctNo.setText("");
            cboProdTypeCr.setEnabled(false);
            btnTransProductId.setEnabled(false);
            btnAccNo.setEnabled(false);
        } else {
            cboProdTypeCr.setEnabled(true);
            btnTransProductId.setEnabled(true);
            btnAccNo.setEnabled(true);
        }
    }//GEN-LAST:event_rdoGlAcHdActionPerformed
    
    private void validateEnableDisableRTGS(){
         String behavesLike =CommonUtil.convertObjToStr(((ComboBoxModel)cboBehavesLike.getModel()).getKeyForSelected());
        if(behavesLike.length()>0 ){
            if(behavesLike.equals("EFT")){
                setVisbleRTGSSuspenceHead(true);
            }else{
                setVisbleRTGSSuspenceHead(false);
                txtRTGSSuspenseHead.setText("");
                lblRTGSSuspenseHeadDesc.setText("");
            }
        }
    }
    /** This method helps in popoualting the data from the data base
     * @param currField Action the argument is passed according to the command issued
     */
    private void callView(String currField) {
        updateOBFields();
        viewType = currField;
        final String PAYBRANCH = CommonUtil.convertObjToStr(((ComboBoxModel)(cboPayableBranchGR.getModel())).getKeyForSelected());
        
        //System.out.println("PAYBRANCH in CallView: " + PAYBRANCH);
        
        HashMap viewMap = new HashMap();
        if (currField.equals("Edit")|| currField.equals("Enquirystatus") || currField.equals("Copy")){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            ArrayList lst = new ArrayList();
            lst.add("PRODUCT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectRemittanceProductTOList");
        } else   if (currField.equalsIgnoreCase("BranchName")) {
            viewMap = getBranch(PAYBRANCH);
        } else if (currField.equalsIgnoreCase("BankCode")) {
            //            viewMap.put(CommonConstants.MAP_NAME, "Remittance_Product_Branch.getBankCode");
            viewMap = getBank(PAYBRANCH);
        }else if(currField.equals("RTGS_NEFT_PROD_ID")){  
            viewMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboProdTypeCr).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "InterMaintenance.getProductData" + CommonUtil.convertObjToStr(observable.getCbmProdTypeCr().getKeyForSelected()));
            viewMap.put(CommonConstants.MAP_WHERE, viewMap);
        } else if (viewType.equals("RTGS_NEFT_ACCT_NO")) {
            System.out.println("observable.getPensionProductType()%#%#%"+observable.getCbmProdTypeCr());
                if(!observable.getCbmProdTypeCr().equals("GL")){
                viewMap.put("PROD_ID", txtTransProductId.getText());
                viewMap.put("SELECTED_BRANCH", TrueTransactMain.BRANCH_ID);
                viewMap.put(CommonConstants.MAP_WHERE, viewMap);
                if (observable.getCbmProdTypeCr().equals("TD") || observable.getCbmProdTypeCr().equals("TL") || observable.getCbmProdTypeCr().equals("AB")) {
                    if (observable.getCbmProdTypeCr().equals("TL") || observable.getCbmProdTypeCr().equals("AB")) {
                        viewMap.put("RECEIPT", "RECEIPT");
                    }
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                            + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
                } else {
                    viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList"
                            + CommonUtil.convertObjToStr(((ComboBoxModel) cboProdTypeCr.getModel()).getKeyForSelected()));
                }   
            }else{
                   viewMap.put(CommonConstants.MAP_NAME, "getSelectAccountHead"); 
                }
        } else{
            viewMap.put(CommonConstants.MAP_NAME, "RemittancePayment.getSelectAccoutHead");
        }
        new ViewAll(this, viewMap).show();
    }
    
    private HashMap getBank(String PAYBRANCH){
        HashMap viewMap = new HashMap();
        if(PAYBRANCH.equalsIgnoreCase("DESIG_OTHER_BANK_BRAN")){
            viewMap.put(CommonConstants.MAP_NAME, "RemittanceProd.getOtherBankData");
        }
        //__
        else if(PAYBRANCH.equalsIgnoreCase("ANY_BANK_BRANCH")){
            viewMap.put(CommonConstants.MAP_NAME, "RemittanceProd.getOtherBankData");
        }
        
        
        return viewMap;
    }
    
    private HashMap getBranch(String PAYBRANCH){
        HashMap viewMap = new HashMap();
        HashMap where =  new HashMap();
//        String bankCode = txtBankCode.getText();
        where.put("BANK_CODE", bankCode);
        viewMap.put(CommonConstants.MAP_WHERE, where);
        //        viewMap.put(CommonConstants.MAP_NAME, "Remittance_Product_Branch.getBranch");
        
        if(PAYBRANCH.equalsIgnoreCase("ANY_BRANCH_BANK")){
            viewMap.put(CommonConstants.MAP_NAME, "RemittanceProd.getCurrentBankBranch");
        }
        //__ if the Selected type is Designated Branch of the Other Bank...
        else if(PAYBRANCH.equalsIgnoreCase("DESIG_OTHER_BANK_BRAN")){
            viewMap.put(CommonConstants.MAP_NAME, "RemittanceProd.getDesgOtherBankBranch");
        }
        //__
        else if(PAYBRANCH.equalsIgnoreCase("ANY_BANK_BRANCH")){
            viewMap.put(CommonConstants.MAP_NAME, "RemittanceProd.getOtherBankBranch");
        }
//        bankCode = "";
        return viewMap;
    }
    
    /** This method helps in filling the data frm the data base to respective txt fields
     * @param obj param The selected data from the viewAll() is passed as a param
     */
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        int remitProduBranchRow = 0;
        if (viewType != null) {
            if (viewType.equals("Edit") || viewType.equals(AUTHORIZE) ||  viewType.equals("Enquirystatus") || viewType.equals("Copy") ) {
                isFilled = true;
                hash.put(CommonConstants.MAP_WHERE, hash.get("PRODUCT ID"));
                if (observable.populateData(hash)) {
                    //                    enableDisableAliasBranchTable(true);
                    //                } else {
                    //                    enableDisableAliasBranchTable(false);
                }
                 ArrayList descLst=observable.getDescList();
                    if(descLst!=null && descLst.size()>0){
                        for(int i=0;i<descLst.size();i++){
                            HashMap hmap = (HashMap) descLst.get(i);
                            Set keySet;
                            Object[] objKeySet;
                            keySet = hmap.keySet();
                            objKeySet = (Object[]) keySet.toArray();
                            //System.out.println("n@@@@" + objKeySet[0]);
                            String key = CommonUtil.convertObjToStr(objKeySet[0]);
                            //System.out.println("n@@@@" + key);
                            //System.out.println("hmap@@@@" + hmap);
                            if(key.equals(txtIssueHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblIssueHeadDesc.setText(desc);
                            }  if(key.equals(txtExchangeHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblExchangeHeadDesc.setText(desc);
                            } if(key.equals(txtTCHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblTCHeadDesc.setText(desc);
                            } if(key.equals(txtRCHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblRCHeadDesc.setText(desc);
                            } if(key.equals(txtOCHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblOCHeadDesc.setText(desc);
                            } if(key.equals(txtPayableHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblPayableHeadDesc.setText(desc);
                            } if(key.equals(txtPostageHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblPostageHeadDesc.setText(desc);
                            } if(key.equals(txtDCHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblDCHeadDesc.setText(desc);
                            } if(key.equals(txtCCHeadGR.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblCCHeadDesc.setText(desc);
                            }if(key.equals(txtDdIssueHead.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblDdIssueHeadVal.setText(desc);
                            }if(key.equals(txtRTGSSuspenseHead.getText())){
                               String desc = CommonUtil.convertObjToStr(hmap.get(key));
                               lblRTGSSuspenseHeadDesc.setText(desc);
                            }
                        }
                    }
                observable.setTxtProductIdGR((String) hash.get("PRODUCT ID"));
                if ( viewType.equals("Delete") || viewType.equals(AUTHORIZE)|| viewType.equals("Enquirystatus")) {
                    setButtonEnableDisable();
                    ClientUtil.enableDisable(this, false);
                }
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                }
                if (viewType.equals("Copy")){
                    txtProductIdGR.setEnabled(true);
                    txtProductIdGR.setEditable(true);
//                    ClientUtil.enableDisable(panInsideGeneralRemittance, true);
                    ClientUtil.enableDisable(panLapsedGR, true);
                    ClientUtil.enableDisable(panOtherGR, true);
                    ClientUtil.enableDisable(panProdDescGeneralRemittance, true);
                    btnCopy.setEnabled(false);
                    btnNew.setEnabled(false);
                    btnEdit.setEnabled(false);
                    btnSave.setEnabled(true);
                    btnCancel.setEnabled(true);
                    btnAuthorize.setEnabled(false);
                    btnReject.setEnabled(false);
                    btnException.setEnabled(false);
                    btnIssueHeadGRHelp.setEnabled(true);
                    btnExchangeHeadGRHelp.setEnabled(true);
                    btnTCHeadGRHelp.setEnabled(true);
                    btnRCHeadGRHelp.setEnabled(true);
                    btnOCHeadGRHelp.setEnabled(true);
                    btnPayableHeadGRHelp.setEnabled(true);
                    btnPostageHeadGRHelp.setEnabled(true);
                    btnDCHeadGRHelp.setEnabled(true);
                    btnCCHeadGRHelp.setEnabled(true);
                    
                }
                btnCopy.setEnabled(false);
            }else if (viewType.equalsIgnoreCase("IssueHead")) {
                observable.setTxtIssueHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblIssueHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            }else if (viewType.equalsIgnoreCase("ExchangeHead")) {
                observable.setTxtExchangeHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblExchangeHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("TCChrgesHead")) {
                observable.setTxtTCHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblTCHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("RChargesHead")) {
                observable.setTxtRCHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblRCHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("OtherChargesHead")) {
                observable.setTxtOCHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblOCHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("PayableHead")) {
                observable.setTxtPayableHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblPayableHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("PostageHead")) {
                observable.setTxtPostageHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblPostageHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("DChargesHead")) {
                observable.setTxtDCHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblDCHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("CChargesHead")) {
                observable.setTxtCCHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblCCHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("LapsedHead")) {
                observable.setTxtLapsedHeadGR(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblIssueHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            } else if (viewType.equalsIgnoreCase("BankCode")) {
                bankCode = (CommonUtil.convertObjToStr(hash.get("BANK CODE")));
                observable.setTxtBranchName("");
                observable.setBankName(CommonUtil.convertObjToStr(hash.get("BANK NAME")));
//                observable.setTxtBankCode((CommonUtil.convertObjToStr(hash.get("BANK SHORT NAME")))+(CommonUtil.convertObjToStr(hash.get("BANK CODE"))));
                observable.setTxtBankCode(CommonUtil.convertObjToStr(hash.get("BANK CODE")));
                observable.setTxtBranchName("");
                observable.setTxtBranchCodeBR("");
            }else if (viewType.equalsIgnoreCase("BranchName")) {
                observable.setTxtBranchName(CommonUtil.convertObjToStr(hash.get("BRANCH NAME")));
//                observable.setTxtBranchCodeBR((CommonUtil.convertObjToStr(hash.get("BRANCH_SHORT_NAME")))+(CommonUtil.convertObjToStr(hash.get("BRANCH CODE"))));
                observable.setTxtBranchCodeBR(CommonUtil.convertObjToStr(hash.get("BRANCH CODE")));
            }else if (viewType.equalsIgnoreCase("DDissueHead")) {
                observable.setTxtNewProcIssueAcHd(CommonUtil.convertObjToStr(hash.get(accountHead)));
                txtDdIssueHead.setText(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblDdIssueHeadVal.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            }else if (viewType.equalsIgnoreCase("RTGSSuspenseHead")) {
                observable.setTxtRTGSSuspenseHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
                txtRTGSSuspenseHead.setText(CommonUtil.convertObjToStr(hash.get(accountHead)));
                lblRTGSSuspenseHeadDesc.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNT HEAD DESC")));
            }else if (viewType.equalsIgnoreCase("RTGS_NEFT_PROD_ID")) {
                observable.setTxtRtgsNeftProductId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                txtTransProductId.setText(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
            }else if (viewType.equalsIgnoreCase("RTGS_NEFT_ACCT_NO")) {
                observable.setTxtRtgsNeftAcctNo(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
                txtAcctNo.setText(CommonUtil.convertObjToStr(hash.get("ACCOUNTNO")));
            }
        }
        observable.setLableProductID();
        observable.setLableProductDesc();
        if (viewType.equalsIgnoreCase("Edit")){
            remitProduBranchRow = tblBrchRemittNumber.getRowCount();
            ClientUtil.enableDisable(this, true);
            resetHelpButton();
            setRemitProdBrchBtnEndableDisable();
            setRemitProdChrgBtnEndableDisable();
            setButtonEnableDisable();
            setDelBtnEnableDisable(true);
            ClientUtil.enableDisable(panBranchRemittance, false);
            ClientUtil.enableDisable(panRemitProdChargesFields, false);
            //            ClientUtil.enableDisable(panSeriesGR, false);
            txtProductIdGR.setEditable(false);
            txtProductIdGR.setEnabled(false);
            btnBrchRemittNumberNew.setEnabled(true);
            //            if (observable.getRdoSeriesGR_No() == true && remitProduBranchRow == 1){
            //                // If Series Number maintain radio button NO is selected and
            //                // If there is no row in Remittance Product Branch (Enable Remittance Product Branch NEW button)
            //                btnBrchRemittNumberNew.setEnabled(false);
            //            }
            if (observable.getRdoEFTProductGR_Yes() == true ){
                // If EFT Product radio button YES is selected (Disable Print Services radio button)
                ClientUtil.enableDisable(panPrintServicesGR, false);
            }
            btnRemitProdChargesNew.setEnabled(true);
            setEnableDisableLapsedBtn();
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void setVisbleRTGSSuspenceHead(boolean flag){
        lblRTGSSuspenseHead.setVisible(flag);
        txtRTGSSuspenseHead.setVisible(flag);
        btnRTGSSuspenseHead.setVisible(flag);
        lblRTGSSuspenseHeadDesc.setVisible(flag);
        }
    private void savePerformed(){
        updateOBFields();
        boolean productID = false;
        // If the Action Type is  NEW or EDIT, Check for the Uniqueness of Product ID and Product Description
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        //System.out.println("productID : " + productID);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_COPY){
            productID = observable.uniqueProduct();
        }
        if(productID == false){
            observable.doSave();
            //__ if the Action is not Falied, Reset the fields...
            if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
                HashMap lockMap = new HashMap();
                ArrayList lst = new ArrayList();
                lst.add("PRODUCT ID");
                lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
                if (observable.getProxyReturnMap()!=null) {
                    if (observable.getProxyReturnMap().containsKey("PRODUCT ID")) {
                        lockMap.put("PRODUCT ID", observable.getProxyReturnMap().get("PRODUCT ID"));
                    }
                }
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    lockMap.put("PRODUCT ID", observable.getTxtProductIdGR());
                }
                setEditLockMap(lockMap);
                setEditLock();
                resetBranchTable();
                resetChargeTable();
                observable.resetOBFields();
                resetHelpButton();
                //                setRemitProdBrchBtnEndableDisable();
                remitBtnDefaultEnableDisable(false);
                //                setRemitProdChrgBtnEndableDisable();
                remitProdChrgDefaultEnableDisableBtn(false);
                observable.resetRemitProdBrchFields();
                observable.resetRemitProdChrgFields();
                enableDisableRemitProductBrchTableField(false);
                enableDisableTableBtn(false);
                ClientUtil.enableDisable(this, false);
                if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
//                    super.removeEditLock(txtProductIdGR.getText());
                    observable.setResult(observable.getActionType());
                }
                observable.setResultStatus();
                lblStatus.setText(observable.getLblStatus());
                setButtonEnableDisable();
            }
        }
    }
    
    private void setEnableDisableLapsedBtn(){
        // If Lapsed Applicable YES is selected
        if(rdoIsLapsedGR_Yes.isSelected()){
            btnLapsedHeadGRHelp.setEnabled(true);
            ClientUtil.enableDisable(panLapsedGR, true);
            txtLapsedPeriodGR.setEditable(true);
            txtLapsedPeriodGR.setEnabled(true);
            txtLapsedHeadGR.setEnabled(false);
            txtLapsedHeadGR.setEditable(false);
            btnRCHeadGRHelp.setEnabled(true);
            cboLapsedPeriodGR.setEnabled(true);
        }else{
            // If Lapsed Applicable NO is selected
            btnLapsedHeadGRHelp.setEnabled(false);
            txtLapsedPeriodGR.setText("0");
            cboLapsedPeriodGR.setSelectedItem("");
            txtRCHeadGR.setText("");
            txtLapsedHeadGR.setText("");
            btnRCHeadGRHelp.setEnabled(false);
            ClientUtil.enableDisable(panLapsedGR, false);
            ClientUtil.enableDisable(panIsLapsedGR, true);
        }
    }
    
    private void setEnableDisableRodLapsedBtn(){
        txtLapsedHeadGR.setText("");
        txtLapsedHeadGR.setEnabled(false);
        txtLapsedPeriodGR.setText("0");
        cboLapsedPeriodGR.setSelectedItem("");
        btnLapsedHeadGRHelp.setEnabled(false);
        txtLapsedPeriodGR.setEditable(false);
        txtLapsedPeriodGR.setEnabled(false);
        txtLapsedPeriodGR.setEnabled(false);
        cboLapsedPeriodGR.setEnabled(false);
    }
    
    private void enableDisableall(){
        resetBranchTable();
        resetChargeTable();
        observable.clearData();
        observable.resetOBFields();
        observable.resetRemitProdBrchFields();
        observable.resetRemitProdChrgFields();
        resetHelpButton();
        setButtonEnableDisable();
        setRemitProdBrchBtnEndableDisable();
        setRemitProdChrgBtnEndableDisable();
        enableDisableRemitProductBrchTableField(false);
        enableDisableTableBtn(false);
        ClientUtil.enableDisable(this, false);
    }
    
    private void doaddRemittanceProductBranchTable(){
        updateOBFields();
        if((observable.getTxtMinAmtRR().length() > 0) || (observable.getTxtMaxAmtRR().length() > 0)){
            //  If any of Minimum Amt or Maximum Amt is not null
            if(((observable.getTxtMinAmtRR().equals(""))) && (!(observable.getTxtMaxAmtRR().equals("")))){
                //  If Minimum Amt is null and Maximum Amt is not null
                observable.displayCDialogue("amountMinWarning");
            }else if((!(observable.getTxtMinAmtRR().equals(""))) && ((observable.getTxtMaxAmtRR().equals("")))){
                //  If Minimum Amt is not null and Maximum Amt is null
                observable.displayCDialogue("amountMaxWarning");
            }
        }
        if((observable.getTxtMinAmtRR().length() > 0) && (observable.getTxtMaxAmtRR().length() > 0)){
            //  If both of Minimum Amt and Maximum Amt is not null
            int minAmount = -1;
            int maxAmount = -1;
            if(observable.getTxtMinAmtRR().length() > 0){
                minAmount = CommonUtil.convertObjToInt(observable.getTxtMinAmtRR());
            }
            if(observable.getTxtMaxAmtRR().length() > 0){
                maxAmount = CommonUtil.convertObjToInt(observable.getTxtMaxAmtRR());
            }
            if(maxAmount > minAmount ){
                //  If Maximun Amt is greater than Minimum Amt, then add to the CTable
                addRemittanceProductBranchTable();
            }else{
                // Display alert message ( Minimum Amt is greater than Maximum Amt )
                observable.displayCDialogue("amountMinMaxWarning");
            }
        }else if((observable.getTxtMinAmtRR().equals("")) && (observable.getTxtMaxAmtRR().equals(""))){
            //  If both of Minimum Amt and Maximum Amt is null
            addRemittanceProductBranchTable();
        }
    }
    
    
    /** Adding datas from Fields of Remittance Product Branch to the CTable
     * of Remittance Product Branch
     */
    public void addRemittanceProductBranchTable(){
        RemitProdBrchResult = observable.addRemittanceProductBranchTab();
        ClientUtil.enableDisable(panBranchRemittance,false);
        btnBranchName.setEnabled(btnNew.isEnabled());
        btnBankCode.setEnabled(btnNew.isEnabled());
        btnBrchRemittNumberSave.setEnabled(btnNew.isEnabled());
        btnBrchRemittNumberDelete.setEnabled(btnNew.isEnabled());
        //        if(observable.getRdoSeriesGR_Yes() == true){
        // If radio button Series Number to maintain, NO is selected
        btnBrchRemittNumberNew.setEnabled(!btnNew.isEnabled());
        //        }
        if (RemitProdBrchResult == 2){
            /** The action taken for the Cancel option **/
            ClientUtil.enableDisable(panBranchRemittance,true);
            btnBranchName.setEnabled(!btnNew.isEnabled());
            btnBankCode.setEnabled(!btnNew.isEnabled());
            btnBrchRemittNumberSave.setEnabled(!btnNew.isEnabled());
            btnBrchRemittNumberDelete.setEnabled(btnNew.isEnabled());
            btnBrchRemittNumberNew.setEnabled(btnNew.isEnabled());
        }
    }
    
    /** Adding datas from Fields of Remittance Product Charges to the CTable
     * of Remittance Product Charges
     */
    public void addRemittanceProductChargesTable(){
        updateOBFields();
        updateChargesOB();
        RemitProdChrgResult = observable.saveRemittanceProductCharges(tblChargesSelected,tblRemitProdCharges.getSelectedRow());
        updateCharges();
        tblChargesSelected = false;
        ClientUtil.enableDisable(panRemitProdChargesFields,false);
        btnRemitProdChargesSave.setEnabled(btnNew.isEnabled());
        btnRemitProdChargesDelete.setEnabled(btnNew.isEnabled());
        btnRemitProdChargesNew.setEnabled(!btnNew.isEnabled());
        if (RemitProdChrgResult == 2){
            updateOBFields();
            updateChargesOB();
            /** The action taken for the Cancel option **/
            ClientUtil.enableDisable(panRemitProdChargesFields,true);
            btnRemitProdChargesSave.setEnabled(!btnNew.isEnabled());
            btnRemitProdChargesDelete.setEnabled(btnNew.isEnabled());
            btnRemitProdChargesNew.setEnabled(btnNew.isEnabled());
        }
        
    }
    
    private void resetRemitProdBranchTab(){
        updateOBFields();
        updateChargesOB();
        resetBranchTable();
        observable.resetRemitProdBrchFields();
        ClientUtil.enableDisable(panBranchRemittance, false);
        btnBranchName.setEnabled(btnNew.isEnabled());
        btnBankCode.setEnabled(btnNew.isEnabled());
        setRemitProdBrchtabBtnEndableDisable(false);
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
    
    /** To Enable or Disable the Fields of General Remittance... */
    private void resetHelpButton(){
        btnIssueHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnExchangeHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnTCHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnRCHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnOCHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnPayableHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnPostageHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnDCHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnCCHeadGRHelp.setEnabled(btnNew.isEnabled());
        btnRTGSSuspenseHead.setEnabled(btnNew.isEnabled());
        btnLapsedHeadGRHelp.setEnabled(btnNew.isEnabled());
        txtIssueHeadGR.setEditable(!btnNew.isEnabled());
        txtExchangeHeadGR.setEditable(!btnNew.isEnabled());
        txtTCHeadGR.setEditable(!btnNew.isEnabled());
        txtRCHeadGR.setEditable(!btnNew.isEnabled());
        txtOCHeadGR.setEditable(!btnNew.isEnabled());
        txtPostageHeadGR.setEditable(!btnNew.isEnabled());
        txtPayableHeadGR.setEditable(!btnNew.isEnabled());
        txtDCHeadGR.setEditable(!btnNew.isEnabled());
        txtCCHeadGR.setEditable(!btnNew.isEnabled());
        txtRTGSSuspenseHead.setEditable(!btnNew.isEnabled());
    }
    
    /** To Enable or Disable the help buttons of Remittance Product Branch (CTable)... */
    private void enableDisableTableBtn(boolean value){
        btnBranchName.setEnabled(value);
        btnBankCode.setEnabled(value);
    }
    
    /** To Enable or Disable the fields of Remittance Product Branch (CTable)... */
    private void enableDisableRemitProductBrchTableField(boolean value){
        txtBranchName.setEnabled(value);
        txtBankCode.setEnabled(value);
        txtBranchCodeBR.setEnabled(value);
        
        txtBranchName.setEditable(value);
        txtBankCode.setEditable(value);
        txtBranchCodeBR.setEditable(value);
    }
    
    /** To Enable or Disable the fields of Remittance Product Branch (CTable)... */
    private void enableDisableRemitProductChrgTableField(boolean value){
        cboChargeType.setEnabled(value);
        cboCategory.setEnabled(value);
    }
    
    /** To Enable or Disable the Buttons of Remittance Product Branch (CTable)... */
    private void setRemitProdBrchBtnEndableDisable(){
        btnBrchRemittNumberNew.setEnabled(!btnNew.isEnabled());
        btnBrchRemittNumberSave.setEnabled(!btnNew.isEnabled());
        btnBrchRemittNumberDelete.setEnabled(!btnNew.isEnabled());
    }
    
    private void setRemitProdBrchtabBtnEndableDisable(boolean value){
        btnBrchRemittNumberSave.setEnabled(value);
        btnBrchRemittNumberDelete.setEnabled(value);
        btnBrchRemittNumberNew.setEnabled(!value);
    }
    
    /** To Enable or Disable the Buttons of Remittance Product Charge (CTable)... */
    private void setRemitProdChrgBtnEndableDisable(){
        btnRemitProdChargesNew.setEnabled(!btnNew.isEnabled());
        btnRemitProdChargesSave.setEnabled(!btnNew.isEnabled());
        btnRemitProdChargesDelete.setEnabled(!btnNew.isEnabled());
    }
    
    /** To Enable or Disable the Buttons of Remittance Product Charge (CTable)... */
    private void remitProdChrgDefaultEnableDisableBtn(boolean truefalse){
        btnRemitProdChargesNew.setEnabled(truefalse);
        btnRemitProdChargesSave.setEnabled(truefalse);
        btnRemitProdChargesDelete.setEnabled(truefalse);
    }
    
    /** Removes all the row present from Remittance Product Branch */
    private void resetBranchTable() {
        observable.removeRemittanceProductBranchRow();
    }
    
    /** Removes all the row present from Remittance Product Charge */
    private void resetChargeTable() {
        observable.removeRemittanceProductChargeRow();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAccNo;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnBankCode;
    private com.see.truetransact.uicomponent.CButton btnBranchName;
    private com.see.truetransact.uicomponent.CButton btnBrchRemittNumberDelete;
    private com.see.truetransact.uicomponent.CButton btnBrchRemittNumberNew;
    private com.see.truetransact.uicomponent.CButton btnBrchRemittNumberSave;
    private com.see.truetransact.uicomponent.CButton btnCCHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnDCHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnDdIssueHead;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnExchangeHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnIssueHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnLapsedHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnOCHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnPayableHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnPostageHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnRCHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnRTGSSuspenseHead;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnRemitProdChargesDelete;
    private com.see.truetransact.uicomponent.CButton btnRemitProdChargesNew;
    private com.see.truetransact.uicomponent.CButton btnRemitProdChargesSave;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnTCHeadGRHelp;
    private com.see.truetransact.uicomponent.CButton btnTransProductId;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboBehavesLike;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboChargeType;
    private com.see.truetransact.uicomponent.CComboBox cboLapsedPeriodGR;
    private com.see.truetransact.uicomponent.CComboBox cboPayableBranchGR;
    private com.see.truetransact.uicomponent.CComboBox cboProdCurrency;
    private com.see.truetransact.uicomponent.CComboBox cboProdTypeCr;
    private com.see.truetransact.uicomponent.CComboBox cboRateType;
    private com.see.truetransact.uicomponent.CComboBox cboRttTypeBR;
    private com.see.truetransact.uicomponent.CCheckBox chkNewProcedure;
    private com.see.truetransact.uicomponent.CLabel lblAccNo;
    private com.see.truetransact.uicomponent.CLabel lblAmtRangeFrom;
    private com.see.truetransact.uicomponent.CLabel lblAmtRangeTo;
    private com.see.truetransact.uicomponent.CLabel lblBankCode;
    private com.see.truetransact.uicomponent.CLabel lblBehavesLike;
    private com.see.truetransact.uicomponent.CLabel lblBranchCodeBR;
    private com.see.truetransact.uicomponent.CLabel lblBranchNameBR;
    private com.see.truetransact.uicomponent.CLabel lblCCHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblCCHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblCashLimitGR;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblChargeType;
    private com.see.truetransact.uicomponent.CLabel lblCharges;
    private com.see.truetransact.uicomponent.CLabel lblChargesBankCode;
    private com.see.truetransact.uicomponent.CLabel lblChargesBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblDCHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblDCHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblDdIssueHead;
    private com.see.truetransact.uicomponent.CLabel lblDdIssueHeadVal;
    private com.see.truetransact.uicomponent.CLabel lblDisplayBankCode;
    private com.see.truetransact.uicomponent.CLabel lblDisplayBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblEFTProductGR;
    private com.see.truetransact.uicomponent.CLabel lblExchangeHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblExchangeHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblForEvery;
    private com.see.truetransact.uicomponent.CLabel lblIVNoRR;
    private com.see.truetransact.uicomponent.CLabel lblIsLapsedGR;
    private com.see.truetransact.uicomponent.CLabel lblIssueHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblIssueHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblLapsedHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblLapsedPeriodGR;
    private com.see.truetransact.uicomponent.CLabel lblMaxAmtRR;
    private com.see.truetransact.uicomponent.CLabel lblMaximumAmount;
    private com.see.truetransact.uicomponent.CLabel lblMaximumAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinAmtRR;
    private com.see.truetransact.uicomponent.CLabel lblMinimumAmount;
    private com.see.truetransact.uicomponent.CLabel lblMinimumAmt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNewProcedure;
    private com.see.truetransact.uicomponent.CLabel lblNumberPattern;
    private com.see.truetransact.uicomponent.CLabel lblOCHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblOCHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblOVNoRR;
    private com.see.truetransact.uicomponent.CLabel lblPayableBranchGR;
    private com.see.truetransact.uicomponent.CLabel lblPayableHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblPayableHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblPercentage;
    private com.see.truetransact.uicomponent.CLabel lblPostageHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblPostageHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblPrintServicesGR;
    private com.see.truetransact.uicomponent.CLabel lblProdCurrency;
    private com.see.truetransact.uicomponent.CLabel lblProdDescRemitProdBranch;
    private com.see.truetransact.uicomponent.CLabel lblProdDescRemitProdBrch;
    private com.see.truetransact.uicomponent.CLabel lblProdDescRemitProdCharge;
    private com.see.truetransact.uicomponent.CLabel lblProdDescRemitProdChrg;
    private com.see.truetransact.uicomponent.CLabel lblProdIDRemitProdBranch;
    private com.see.truetransact.uicomponent.CLabel lblProdIDRemitProdBrch;
    private com.see.truetransact.uicomponent.CLabel lblProdIDRemitProdCharge;
    private com.see.truetransact.uicomponent.CLabel lblProdIDRemitProdChrg;
    private com.see.truetransact.uicomponent.CLabel lblProdIdCr;
    private com.see.truetransact.uicomponent.CLabel lblProdTypeCr;
    private com.see.truetransact.uicomponent.CLabel lblProductDescGR;
    private com.see.truetransact.uicomponent.CLabel lblProductIdGR;
    private com.see.truetransact.uicomponent.CLabel lblRCHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblRCHeadGR;
    private com.see.truetransact.uicomponent.CLabel lblRTGSSuspenseHead;
    private com.see.truetransact.uicomponent.CLabel lblRTGSSuspenseHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblRemarksGR;
    private com.see.truetransact.uicomponent.CLabel lblRttLimitBR;
    private com.see.truetransact.uicomponent.CLabel lblRttTypeBR;
    private com.see.truetransact.uicomponent.CLabel lblSeriesGR;
    private com.see.truetransact.uicomponent.CLabel lblServiceTax;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace11;
    private com.see.truetransact.uicomponent.CLabel lblSpace12;
    private com.see.truetransact.uicomponent.CLabel lblSpace13;
    private com.see.truetransact.uicomponent.CLabel lblSpace14;
    private com.see.truetransact.uicomponent.CLabel lblSpace15;
    private com.see.truetransact.uicomponent.CLabel lblSpace16;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStartDate;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTCHeadDesc;
    private com.see.truetransact.uicomponent.CLabel lblTCHeadGR;
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
    private com.see.truetransact.uicomponent.CPanel panAccHd;
    private com.see.truetransact.uicomponent.CPanel panAccountDetails1;
    private com.see.truetransact.uicomponent.CPanel panAcctNo;
    private com.see.truetransact.uicomponent.CPanel panAliasBrchRemittNumber;
    private javax.swing.JPanel panBankCode;
    private com.see.truetransact.uicomponent.CPanel panBranchAndRttNoRemittance;
    private javax.swing.JPanel panBranchName;
    private com.see.truetransact.uicomponent.CPanel panBranchRemittance;
    private com.see.truetransact.uicomponent.CPanel panBrchRemittNumberButton;
    private com.see.truetransact.uicomponent.CPanel panBrchRemittance;
    private com.see.truetransact.uicomponent.CPanel panCharges;
    private com.see.truetransact.uicomponent.CPanel panDebitAccHead;
    private com.see.truetransact.uicomponent.CPanel panEFTProductGR;
    private com.see.truetransact.uicomponent.CPanel panGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panIsLapsedGR;
    private com.see.truetransact.uicomponent.CPanel panLapsedGR;
    private com.see.truetransact.uicomponent.CPanel panLapsedHeadGR;
    private com.see.truetransact.uicomponent.CPanel panLapsedPeriodGR;
    private com.see.truetransact.uicomponent.CPanel panNewProcedurePan;
    private com.see.truetransact.uicomponent.CPanel panNumberPattern;
    private com.see.truetransact.uicomponent.CPanel panOtherGR;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR;
    private com.see.truetransact.uicomponent.CPanel panProdDescGeneralRemittance;
    private com.see.truetransact.uicomponent.CPanel panProdDescRemitProdBranch;
    private com.see.truetransact.uicomponent.CPanel panProdDescRemitProdCharges;
    private com.see.truetransact.uicomponent.CPanel panRemitBankCode;
    private com.see.truetransact.uicomponent.CPanel panRemitProdCharges;
    private com.see.truetransact.uicomponent.CPanel panRemitProdChargesButtons;
    private com.see.truetransact.uicomponent.CPanel panRemitProdChargesFields;
    private com.see.truetransact.uicomponent.CPanel panRemitType;
    private com.see.truetransact.uicomponent.CPanel panSelectAll;
    private com.see.truetransact.uicomponent.CPanel panSeriesGR;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdgEFTProductGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgIsLapsedGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPayableBranchGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrintServicesGR;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSeriesGR;
    private com.see.truetransact.uicomponent.CRadioButton rdoEFTProductGR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoEFTProductGR_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoGlAcHd;
    private com.see.truetransact.uicomponent.CRadioButton rdoInvestmentAcHd;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsLapsedGR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoIsLapsedGR_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrintServicesGR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrintServicesGR_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSeriesGR_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoSeriesGR_Yes;
    private com.see.truetransact.uicomponent.CScrollPane sprRemitProdCharges;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CSeparator sptGeneralRemittanceH;
    private com.see.truetransact.uicomponent.CSeparator sptGeneralRemittanceL;
    private com.see.truetransact.uicomponent.CSeparator sptGeneralRemittanceV;
    private com.see.truetransact.uicomponent.CSeparator sptProdDescRemitProdBranch;
    private com.see.truetransact.uicomponent.CSeparator sptProdDescRemitProdCharges;
    private com.see.truetransact.uicomponent.CSeparator sptRemitButtons;
    private com.see.truetransact.uicomponent.CSeparator sptRemitProdChargesFieldsAndButtons;
    private com.see.truetransact.uicomponent.CScrollPane srpAliasBrchRemittNumber;
    private com.see.truetransact.uicomponent.CScrollPane srpBrchRemittNumber;
    private com.see.truetransact.uicomponent.CTabbedPane tabRemittanceProduct;
    private com.see.truetransact.uicomponent.CTable tblAliasBrchRemittNumber;
    private com.see.truetransact.uicomponent.CTable tblBrchRemittNumber;
    private com.see.truetransact.uicomponent.CTable tblRemitProdCharges;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CDateField tdtStartDate;
    public static com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtAmtRangeFrom;
    private com.see.truetransact.uicomponent.CTextField txtAmtRangeTo;
    private com.see.truetransact.uicomponent.CTextField txtBankCode;
    private com.see.truetransact.uicomponent.CTextField txtBranchCodeBR;
    private com.see.truetransact.uicomponent.CTextField txtBranchName;
    private com.see.truetransact.uicomponent.CTextField txtCCHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtCashLimitGR;
    private com.see.truetransact.uicomponent.CTextField txtCharges;
    private com.see.truetransact.uicomponent.CTextField txtDCHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtDdIssueHead;
    private com.see.truetransact.uicomponent.CTextField txtExchangeHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtForEvery;
    private com.see.truetransact.uicomponent.CTextField txtIVNoRR;
    private com.see.truetransact.uicomponent.CTextField txtIssueHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtLapsedHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtLapsedPeriodGR;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmt;
    private com.see.truetransact.uicomponent.CTextField txtMaxAmtRR;
    private com.see.truetransact.uicomponent.CTextField txtMaximumAmount;
    private com.see.truetransact.uicomponent.CTextField txtMinAmtRR;
    private com.see.truetransact.uicomponent.CTextField txtMinimumAmount;
    private com.see.truetransact.uicomponent.CTextField txtMinimumAmt;
    private com.see.truetransact.uicomponent.CTextField txtOCHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtOVNoRR;
    private com.see.truetransact.uicomponent.CTextField txtPayableHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtPercentage;
    private com.see.truetransact.uicomponent.CTextField txtPerfix;
    private com.see.truetransact.uicomponent.CTextField txtPostageHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtProductDescGR;
    private com.see.truetransact.uicomponent.CTextField txtProductIdGR;
    private com.see.truetransact.uicomponent.CTextField txtRCHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtRTGSSuspenseHead;
    private com.see.truetransact.uicomponent.CTextField txtRateVal;
    private com.see.truetransact.uicomponent.CTextField txtRemarksGR;
    private com.see.truetransact.uicomponent.CTextField txtRttLimitBR;
    private com.see.truetransact.uicomponent.CTextField txtServiceTax;
    private com.see.truetransact.uicomponent.CTextField txtSuffix;
    private com.see.truetransact.uicomponent.CTextField txtTCHeadGR;
    private com.see.truetransact.uicomponent.CTextField txtTransProductId;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        RemittanceProductUI gui = new RemittanceProductUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}
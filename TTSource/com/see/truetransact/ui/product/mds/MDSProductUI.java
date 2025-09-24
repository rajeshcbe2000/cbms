  /*
   * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
   *
   * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
   * 
   * MDSProductUI.java
   *
   * Created on November 26, 2003, 11:27 AM
   */
package com.see.truetransact.ui.product.mds;

import java.util.HashMap;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CComboBox;
import com.see.truetransact.uicomponent.CTextField;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.product.mds.MDSProductMRB;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.clientexception.ClientParseException;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.*;
/**
 *
 * @author Hemant
 * Modification Lohith R.
 *@modified : Sunil
 *      Added Edit Locking - 08-07-2005
 */

public class MDSProductUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, UIMandatoryField{
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.product.mds.MDSProductRB", ProxyParameters.LANGUAGE);
    private final static ClientParseException parseException = ClientParseException.getInstance();
    MDSProductMRB objMandatoryMRB = new MDSProductMRB();
    private String accountHead = "ACCOUNT HEAD";
    private final String AUTHORIZE="AUTHORIZE";
    private boolean isFilled = false;
    private MDSProductOB observable;
    private HashMap mandatoryMap;
    private String viewType = "";
    private Date currDt = null;
    /** Creates new form BeanForm */
    public MDSProductUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        settingupUI();
        tabMDSProduct.resetVisits();
    }
    
    private void settingupUI(){
        setObservable();
        setFieldNames();
        internationalize();
        initComponentData();
        setMaximumLength();
        setMandatoryHashMap();
        setHelpMessage();
        setButtonEnableDisable();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), tabMDSProduct);
        ClientUtil.enableDisable(this,false);
        btnDelete.setEnabled(true);
        observable.resetStatus();
        panPrintServicesGR35.setVisible(false);
        lblBonusChitOwner3.setVisible(false);
        panPrintServicesGR36.setVisible(false);
        chkIsBonusTrans.setEnabled(true);
        chkIsBonusTrans.setSelected(true);
        
    }
    
    private void setObservable() {
        observable = MDSProductOB.getInstance();
        observable.addObserver(this);
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCopy.setName("btnCopy");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnView.setName("btnView");
        cboCommisionRate.setName("cboCommisionRate");
        cboDiscountRate.setName("cboDiscountRate");
        cboLoanIntRate.setName("cboLoanIntRate");
        cboPenalCalc.setName("cboPenalCalc");
        cboPenalIntRate.setName("cboPenalIntRate");
        cboPenalPeriod.setName("cboPenalPeriod");
        cboPenalPrizedPeriod.setName("cboPenalPrizedPeriod");
        cboPenalPrizedRate.setName("cboPenalPrizedRate");
        cboholidayInt.setName("cboholidayInt");
        chkAcceptClassA.setName("chkAcceptClassA");
        chkAcceptClassAll.setName("chkAcceptClassAll");
        chkAcceptClassB.setName("chkAcceptClassB");
        chkAcceptClassC.setName("chkAcceptClassC");
        chkAcceptableClassA.setName("chkAcceptableClassA");
        chkAcceptableClassAll.setName("chkAcceptableClassAll");
        chkAcceptableClassB.setName("chkAcceptableClassB");
        chkAcceptableClassC.setName("chkAcceptableClassC");
        chkFromAuctionEnrtry.setName("chkFromAuctionEnrtry");
        chkAfterCashPayment.setName("chkAfterCashPayment");
        chkSplitMDSTransaction.setName("chkSplitMDSTransaction");
        lblAcceptType1.setName("lblAcceptType1");
        lblAcceptType2.setName("lblAcceptType2");
        lblAcceptableType10.setName("lblAcceptableType10");
        lblAcceptableType12.setName("lblAcceptableType12");
        lblAcceptableType13.setName("lblAcceptableType13");
        lblAcceptableType14.setName("lblAcceptableType14");
        lblAcceptableType5.setName("lblAcceptableType5");
        lblAcceptableType7.setName("lblAcceptableType7");
        lblAcceptableType8.setName("lblAcceptableType8");
        lblAcceptableType9.setName("lblAcceptableType9");
        lblAuctionMaxAmt.setName("lblAuctionMaxAmt");
        lblAuctionMaxAmtVal.setName("lblAuctionMaxAmtVal");
        lblAuctionMinAmt.setName("lblAuctionMinAmt");
        lblAuctionMinAmtVal.setName("lblAuctionMinAmtVal");
        lblBonusAllowed.setName("lblBonusAllowed");
        lblBonusChitOwner.setName("lblBonusChitOwner");
        lblBonusChitOwner1.setName("lblBonusChitOwner1");
        lblBonusChitOwner2.setName("lblBonusChitOwner2");
        lblBonusChitOwner3.setName("lblBonusChitOwner3");
        lblBonusGracePeriod.setName("lblBonusGracePeriod");
        lblBonusPrizedPreriod.setName("lblBonusPrizedPreriod");
        lblChitDefaults.setName("lblChitDefaults");
        lblChitDefaults1.setName("lblChitDefaults1");
        lblCommisionRate.setName("lblCommisionRate");
        lblCommisionRateVal.setName("lblCommisionRateVal");
        lblDiscountAllowed.setName("lblDiscountAllowed");
        lblDiscountPeriod.setName("lblDiscountPeriod");
        lblDiscountPrizedPeriod.setName("lblDiscountPrizedPeriod");
        lblDiscountRate.setName("lblDiscountRate");
        lblDiscountRateVal.setName("lblDiscountRateVal");
        lblHolidayInst.setName("lblHolidayInst");
        lblLoanCanbeGranted.setName("lblLoanCanbeGranted");
        lblLoanIntRate.setName("lblLoanIntRate");
        lblLoanIntRateVal.setName("lblLoanIntRateVal");
        lblMargin.setName("lblMargin");
        lblMarginVal.setName("lblMarginVal");
        lblMaxLoanAmt.setName("lblMaxLoanAmt");
        lblMinLoanAmt.setName("lblMinLoanAmt");
        lblMsg.setName("lblMsg");
        lblNonPrizedChit.setName("lblNonPrizedChit");
        lblNonPrizedChit1.setName("lblNonPrizedChit1");
        lblOnlyMembers.setName("lblOnlyMembers");
        lblPenalCalc.setName("lblPenalCalc");
        lblPenalIntRate.setName("lblPenalIntRate");
        lblPenalIntRateVal.setName("lblPenalIntRateVal");
        lblPenalPeriod.setName("lblPenalPeriod");
        lblPenalPrizedPeriod.setName("lblPenalPrizedPeriod");
        lblPenalPrizedRate.setName("lblPenalPrizedRate");
        lblPenalPrizedRateVal.setName("lblPenalPrizedRateVal");
        lblProductDesc.setName("lblProductDesc");
        lblProductDescVal.setName("lblProductDescVal");
        lblProductId.setName("lblProductId");
        lblShortfall.setName("lblShortfall");
        lblSpace.setName("lblSpace");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace5.setName("lblSpace5");
        lblSpace6.setName("lblSpace6");
        lblStatus.setName("lblStatus");
        lblSuretyRequired.setName("lblSuretyRequired");
        mbrMain.setName("mbrMain");
        panAcceptType.setName("panAcceptType");
        panAcceptableType.setName("panAcceptableType");
        panAuctionMaxAmt.setName("panAuctionMaxAmt");
        panAuctionMinAmt.setName("panAuctionMinAmt");
        panBonusAllowed.setName("panBonusAllowed");
        panBonusGracePeriod.setName("panBonusGracePeriod");
        panBonusGracePeriod1.setName("panBonusGracePeriod1");
        panCommisionRate.setName("panCommisionRate");
        panDiscountAllowed.setName("panDiscountAllowed");
        panDiscountAllowed1.setName("panDiscountAllowed1");
        panDiscountAllowed2.setName("panDiscountAllowed2");
        panDiscountAllowed3.setName("panDiscountAllowed3");
        panDiscountAllowed4.setName("panDiscountAllowed4");
        panDiscountPeriod.setName("panDiscountPeriod");
        panDiscountPeriodDetails.setName("panDiscountPeriodDetails");
        panDiscountPrizedPeriod.setName("panDiscountPrizedPeriod");
        panDiscountRate.setName("panDiscountRate");
        panGeneral.setName("panGeneral");
        panGeneralDetails.setName("panGeneralDetails");
        panGeneralDetailsEntry.setName("panGeneralDetailsEntry");
        panGracePeriodDetails.setName("panGracePeriodDetails");
        panHolidayInst.setName("panHolidayInst");
        panLapsedGR.setName("panLapsedGR");
        panLoanCanbeGranted.setName("panLoanCanbeGranted");
        panLoanIntRate.setName("panLoanIntRate");
        panMDSLoan.setName("panMDSLoan");
        panMargin.setName("panMargin");
        panNonPrizedChit.setName("panNonPrizedChit");
        panOtherDetails.setName("panOtherDetails");
        panPenalIntRate.setName("panPenalIntRate");
        panPenalPeriod.setName("panPenalPeriod");
        panPenalPrizedPeriod.setName("panPenalPrizedPeriod");
        panPenalPrizedRate.setName("panPenalPrizedRate");
        panPrintServicesGR1.setName("panPrintServicesGR1");
        panPrintServicesGR35.setName("panPrintServicesGR35");
        panPrintServicesGR36.setName("panPrintServicesGR36");
        panPrintServicesGR37.setName("panPrintServicesGR37");
        panPrintServicesGR5.setName("panPrintServicesGR5");
        panRateDetails.setName("panRateDetails");
        panShortfall.setName("panShortfall");
        panStatus.setName("panStatus");
        panSuretyRequired.setName("panSuretyRequired");
        rdoBonusAllowed_no.setName("rdoBonusAllowed_no");
        rdoBonusAllowed_yes.setName("rdoBonusAllowed_yes");
        rdoBonusGraceAfter.setName("rdoBonusGraceAfter");
        rdoBonusGraceDays.setName("rdoBonusGraceDays");
        rdoBonusGraceEnd.setName("rdoBonusGraceEnd");
        rdoBonusGraceMonth.setName("rdoBonusGraceMonth");
        rdoBonusPrizedAfter.setName("rdoBonusPrizedAfter");
        rdoBonusPrizedDays.setName("rdoBonusPrizedDays");
        rdoBonusPrizedEnd.setName("rdoBonusPrizedEnd");
        rdoBonusPrizedMonth.setName("rdoBonusPrizedMonth");
        rdoChitDefaults_no.setName("rdoChitDefaults_no");
        rdoChitDefaults_yes.setName("rdoChitDefaults_yes");
        rdoDiscountAllowed_no.setName("rdoDiscountAllowed_no");
        rdoDiscountAllowed_yes.setName("rdoDiscountAllowed_yes");
        rdoDiscountPeriodAfter.setName("rdoDiscountPeriodAfter");
        rdoDiscountPeriodDays.setName("rdoDiscountPeriodDays");
        rdoDiscountPeriodEnd.setName("rdoDiscountPeriodEnd");
        rdoDiscountPeriodMonth.setName("rdoDiscountPeriodMonth");
        rdoDiscountPrizedPeriodAfter.setName("rdoDiscountPrizedPeriodAfter");
        rdoDiscountPrizedPeriodDays.setName("rdoDiscountPrizedPeriodDays");
        rdoDiscountPrizedPeriodEnd.setName("rdoDiscountPrizedPeriodEnd");
        rdoDiscountPrizedPeriodMonth.setName("rdoDiscountPrizedPeriodMonth");
        rdoLoanCanbeGranted_no.setName("rdoLoanCanbeGranted_no");
        rdoLoanCanbeGranted_yes.setName("rdoLoanCanbeGranted_yes");
        rdoNonPrizedChit_no.setName("rdoNonPrizedChit_no");
        rdoNonPrizedChit_yes.setName("rdoNonPrizedChit_yes");
        rdoOnlyMembers_no.setName("rdoOnlyMembers_no");
        rdoOnlyMembers_yes.setName("rdoOnlyMembers_yes");
        rdoPrizedChitOwnerAfter_no.setName("rdoPrizedChitOwnerAfter_no");
        rdoPrizedChitOwnerAfter_yes.setName("rdoPrizedChitOwnerAfter_yes");
        rdoPrizedChitOwner_no.setName("rdoPrizedChitOwner_no");
        rdoPrizedChitOwner_yes.setName("rdoPrizedChitOwner_yes");
        rdoShortfall_no.setName("rdoShortfall_no");
        rdoShortfall_yes.setName("rdoShortfall_yes");
        rdoSuretyRequired_no.setName("rdoSuretyRequired_no");
        rdoSuretyRequired_yes.setName("rdoSuretyRequired_yes");
        rdobonusPayableOwner_no.setName("rdobonusPayableOwner_no");
        rdobonusPayableOwner_yes.setName("rdobonusPayableOwner_yes");
        rdoprizedDefaulters_no.setName("rdoprizedDefaulters_no");
        rdoprizedDefaulters_yes.setName("rdoprizedDefaulters_yes");
        tabMDSProduct.setName("tabMDSProduct");
        txtAuctionMaxAmt.setName("txtAuctionMaxAmt");
        txtAuctionMinAmt.setName("txtAuctionMinAmt");
        txtBonusGracePeriod.setName("txtBonusGracePeriod");
        txtBonusPrizedPeriod.setName("txtBonusPrizedPeriod");
        txtCommisionRate.setName("txtCommisionRate");
        txtDiscountPeriodEnd.setName("txtDiscountPeriodEnd");
        txtDiscountPrizedPeriodEnd.setName("txtDiscountPrizedPeriodEnd");
        txtDiscountRate.setName("txtDiscountRate");
        txtLoanIntRate.setName("txtLoanIntRate");
        txtMargin.setName("txtMargin");
        txtMaxLoanAmt.setName("txtMaxLoanAmt");
        txtMinLoanAmt.setName("txtMinLoanAmt");
        txtPenalIntRate.setName("txtPenalIntRate");
        txtPenalPeriod.setName("txtPenalPeriod");
        txtPenalPrizedPeriod.setName("txtPenalPrizedPeriod");
        txtPenalPrizedRate.setName("txtPenalPrizedRate");
        txtProductId.setName("txtProductId");
        lblPaymentTimeCharges.setName("lblPaymentTimeCharges");
        cboPaymentTimeCharges.setName("cboPaymentTimeCharges");
        txtpaymentTimeCharges.setName("txtpaymentTimeCharges");
    }
    
    
    
    
    /** Auto Generated Method - internationalize()
     * This method used to assign display texts from
     * the Resource Bundle File. */
    private void internationalize() {
        lblDiscountRateVal.setText(resourceBundle.getString("lblDiscountRateVal"));
        lblBonusGracePeriod.setText(resourceBundle.getString("lblBonusGracePeriod"));
        lblShortfall.setText(resourceBundle.getString("lblShortfall"));
        lblAcceptableType7.setText(resourceBundle.getString("lblAcceptableType7"));
        lblPenalPrizedRate.setText(resourceBundle.getString("lblPenalPrizedRate"));
        lblPenalIntRate.setText(resourceBundle.getString("lblPenalIntRate"));
        lblDiscountPrizedPeriod.setText(resourceBundle.getString("lblDiscountPrizedPeriod"));
        lblLoanCanbeGranted.setText(resourceBundle.getString("lblLoanCanbeGranted"));
        lblDiscountPeriod.setText(resourceBundle.getString("lblDiscountPeriod"));
        lblBonusPrizedPreriod.setText(resourceBundle.getString("lblBonusPrizedPreriod"));
        lblDiscountRate.setText(resourceBundle.getString("lblDiscountRate"));
        lblSuretyRequired.setText(resourceBundle.getString("lblSuretyRequired"));
        lblOnlyMembers.setText(resourceBundle.getString("lblOnlyMembers"));
        lblAuctionMinAmt.setText(resourceBundle.getString("lblAuctionMinAmt"));
        lblNonPrizedChit1.setText(resourceBundle.getString("lblNonPrizedChit1"));
        lblAcceptableType8.setText(resourceBundle.getString("lblAcceptableType8"));
        lblLoanIntRateVal.setText(resourceBundle.getString("lblLoanIntRateVal"));
        lblAcceptableType14.setText(resourceBundle.getString("lblAcceptableType14"));
        lblProductDesc.setText(resourceBundle.getString("lblProductDesc"));
        lblBonusChitOwner.setText(resourceBundle.getString("lblBonusChitOwner"));
        lblCommisionRate.setText(resourceBundle.getString("lblCommisionRate"));
        lblDiscountAllowed.setText(resourceBundle.getString("lblDiscountAllowed"));
        lblChitDefaults1.setText(resourceBundle.getString("lblChitDefaults1"));
        lblProductId.setText(resourceBundle.getString("lblProductId"));
        lblCommisionRateVal.setText(resourceBundle.getString("lblCommisionRateVal"));
        lblPenalPrizedRateVal.setText(resourceBundle.getString("lblPenalPrizedRateVal"));
        lblNonPrizedChit.setText(resourceBundle.getString("lblNonPrizedChit"));
        lblAuctionMinAmtVal.setText(resourceBundle.getString("lblAuctionMinAmtVal"));
        lblSpace.setText(resourceBundle.getString("lblSpace"));
        lblMargin.setText(resourceBundle.getString("lblMargin"));
        lblAcceptableType13.setText(resourceBundle.getString("lblAcceptableType13"));
        lblLoanIntRate.setText(resourceBundle.getString("lblLoanIntRate"));
        lblAcceptableType9.setText(resourceBundle.getString("lblAcceptableType9"));
        lblAcceptType2.setText(resourceBundle.getString("lblAcceptType2"));
        lblPenalIntRateVal.setText(resourceBundle.getString("lblPenalIntRateVal"));
        lblAcceptableType12.setText(resourceBundle.getString("lblAcceptableType12"));
        lblBonusChitOwner2.setText(resourceBundle.getString("lblBonusChitOwner2"));
        lblAcceptableType5.setText(resourceBundle.getString("lblAcceptableType5"));
        lblAcceptableType10.setText(resourceBundle.getString("lblAcceptableType10"));
        lblHolidayInst.setText(resourceBundle.getString("lblHolidayInst"));
        lblMaxLoanAmt.setText(resourceBundle.getString("lblMaxLoanAmt"));
        lblAuctionMaxAmtVal.setText(resourceBundle.getString("lblAuctionMaxAmtVal"));
        lblAcceptType1.setText(resourceBundle.getString("lblAcceptType1"));
        lblMarginVal.setText(resourceBundle.getString("lblMarginVal"));
        lblChitDefaults.setText(resourceBundle.getString("lblChitDefaults"));
        lblBonusChitOwner3.setText(resourceBundle.getString("lblBonusChitOwner3"));
        lblPenalPrizedPeriod.setText(resourceBundle.getString("lblPenalPrizedPeriod"));
        lblPenalPeriod.setText(resourceBundle.getString("lblPenalPeriod"));
        lblMinLoanAmt.setText(resourceBundle.getString("lblMinLoanAmt"));
        lblBonusAllowed.setText(resourceBundle.getString("lblBonusAllowed"));
        lblAuctionMaxAmt.setText(resourceBundle.getString("lblAuctionMaxAmt"));
        lblBonusChitOwner1.setText(resourceBundle.getString("lblBonusChitOwner1"));
        lblPenalCalc.setText(resourceBundle.getString("lblPenalCalc"));
    }
    
    /** Auto Generated Method - setMandatoryHashMap()
     * This method list out all the Input Fields available in the UI.
     * It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtProductId",  new Boolean(false));
        mandatoryMap.put("txtAuctionMaxAmt", new Boolean(true));
        mandatoryMap.put("txtAuctionMinAmt", new Boolean(true));
        mandatoryMap.put("txtCommisionRate", new Boolean(true));
        mandatoryMap.put("txtDiscountRate", new Boolean(true));
        mandatoryMap.put("txtPenalIntRate", new Boolean(true));
        mandatoryMap.put("txtPenalPrizedRate", new Boolean(true));
        mandatoryMap.put("txtLoanIntRate", new Boolean(true));
        mandatoryMap.put("txtBonusGracePeriod", new Boolean(true));
        mandatoryMap.put("txtBonusPrizedPreriod", new Boolean(true));
        mandatoryMap.put("txtPenalPeriod", new Boolean(true));
        mandatoryMap.put("txtPenalPrizedPeriod", new Boolean(true));
        mandatoryMap.put("txtDiscountPeriod", new Boolean(true));
        mandatoryMap.put("txtDiscountPrizedPeriod", new Boolean(true));
        mandatoryMap.put("txtMargin", new Boolean(true));
        mandatoryMap.put("txtMinLoanAmt",  new Boolean(true));
        mandatoryMap.put("txtMaxLoanAmt",  new Boolean(true));
        mandatoryMap.put("cboCommisionRate",  new Boolean(true));
        mandatoryMap.put("cboDiscountRate",  new Boolean(true));
        mandatoryMap.put("cboPenalIntRate",  new Boolean(true));
        mandatoryMap.put("cboPenalPrizedRate",  new Boolean(true));
        mandatoryMap.put("cboLoanIntRate",  new Boolean(true));
        mandatoryMap.put("cboBonusGracePeriod",  new Boolean(true));
        mandatoryMap.put("cboBonusPrizedPreriod",  new Boolean(true));
        mandatoryMap.put("cboPenalPeriod",  new Boolean(true));
        mandatoryMap.put("cboPenalPrizedPeriod",  new Boolean(true));
        mandatoryMap.put("cboPenalCalc",  new Boolean(true));
        mandatoryMap.put("cboDiscountPeriod",  new Boolean(true));
        mandatoryMap.put("cboDiscountPrizedPeriod",  new Boolean(true));
    }
    
    /** Auto Generated Method - getMandatoryHashMap()
     * Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** Auto Generated Method - setHelpMessage()
     * This method shows tooltip help for all the input fields
     * available in the UI. It needs the Mandatory Resource Bundle
     * object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryMRB = new MDSProductMRB();
        txtProductId.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtProductId"));
        lblProductDescVal.setHelpMessage(lblMsg, objMandatoryMRB.getString("lblProductDescVal"));
        txtAuctionMaxAmt.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtAuctionMaxAmt"));
        txtAuctionMinAmt.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtAuctionMinAmt"));
        cboholidayInt.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboholidayInt"));
        cboCommisionRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboCommisionRate"));
        cboDiscountRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboDiscountRate"));
        cboPenalIntRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboPenalIntRate"));
        cboPenalPrizedRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboPenalPrizedRate"));
        cboLoanIntRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboLoanIntRate"));
        txtCommisionRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtCommisionRate"));
        txtDiscountRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtDiscountRate"));
        txtPenalIntRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtPenalIntRate"));
        txtPenalPrizedRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtPenalPrizedRate"));
        txtLoanIntRate.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtLoanIntRate"));
        txtBonusGracePeriod.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtBonusGracePeriod"));
        txtBonusPrizedPeriod.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtBonusPrizedPeriod"));
        txtPenalPeriod.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtPenalPeriod"));
        txtPenalPrizedPeriod.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtPenalPrizedPeriod"));
        cboPenalPeriod.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboPenalPeriod"));
        cboPenalPrizedPeriod.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboPenalPrizedPeriod"));
        cboPenalCalc.setHelpMessage(lblMsg, objMandatoryMRB.getString("cboPenalCalc"));
        txtDiscountPeriodEnd.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtDiscountPeriodEnd"));
        txtDiscountPrizedPeriodEnd.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtDiscountPrizedPeriodEnd"));
        txtMargin.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtMargin"));
        txtMinLoanAmt.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtMinLoanAmt"));
        txtMaxLoanAmt.setHelpMessage(lblMsg, objMandatoryMRB.getString("txtMaxLoanAmt"));
    }
    
    private void initComponentData() {
        try{
            cboCommisionRate.setModel(observable.getCbmCommisionRate());
            cboDiscountRate.setModel(observable.getCbmDiscountRate());
            cboPenalIntRate.setModel(observable.getCbmPenalIntRate());
            cboPenalPrizedRate.setModel(observable.getCbmPenalPrizedRate());
            cboLoanIntRate.setModel(observable.getCbmLoanIntRate());
            cboholidayInt.setModel(observable.getCbmholidayInt());
            cboPenalPeriod.setModel(observable.getCbmPenalPeriod());
            cboPenalPrizedPeriod.setModel(observable.getCbmPenalPrizedPeriod());
            cboPenalCalc.setModel(observable.getCbmPenalCalc());
            cboPenalIntRateFullInstAmt.setModel(observable.getCbmPenalIntRateFullInstAmt());
            cboPenalPrizedIntRateFullInstAmt.setModel(observable.getCbmPenalPrizedIntRateFullInstAmt());
            cboRoundOffCriteria.setModel(observable.getCbmRoundOffCriteria());
            cboPaymentTimeCharges.setModel(observable.getCbmPaymentTimeCharges());
            cboThalayalTransCategory.setModel(observable.getCbmThalayalTransCategory());
            cboMunnalTransCategory.setModel(observable.getCbmMunnalTransCategory());
        }catch(ClassCastException e){
            parseException.logException(e,true);
        }
    }
    
    
    private void setMaximumLength(){
        txtAuctionMaxAmt.setValidation(new NumericValidation());
        txtAuctionMinAmt.setValidation(new NumericValidation());
        txtCommisionRate.setValidation(new NumericValidation());
        txtDiscountRate.setValidation(new NumericValidation());
        txtPenalIntRate.setValidation(new NumericValidation());
        txtPenalPrizedRate.setValidation(new NumericValidation());
        txtLoanIntRate.setValidation(new NumericValidation());
        txtBonusGracePeriod.setValidation(new NumericValidation());
        txtBonusPrizedPeriod.setValidation(new NumericValidation());
        txtPenalPeriod.setValidation(new NumericValidation());
        txtPenalPrizedPeriod.setValidation(new NumericValidation());
        txtMargin.setValidation(new NumericValidation());
        txtMinLoanAmt.setValidation(new CurrencyValidation());
        txtMaxLoanAmt.setValidation(new CurrencyValidation());
        txtDiscountPeriodEnd.setValidation(new NumericValidation());
        txtDiscountPrizedPeriodEnd.setValidation(new NumericValidation());
        txtAuctionMaxAmt.setMaxLength(3);
        txtAuctionMinAmt.setMaxLength(3);
        txtBonusGracePeriod.setMaxLength(3);
        txtBonusPrizedPeriod.setMaxLength(3);
        txtDiscountPeriodEnd.setMaxLength(2);
        txtDiscountPrizedPeriodEnd.setMaxLength(2);
        txtMargin.setMaxLength(3);
    }
    
    /** Auto Generated Method - update()
     * This method called by Observable. It updates the UI with
     * Observable's data. If needed add/Remove RadioButtons
     * method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtProductId.setText(observable.getTxtProductId());
        lblProductDescVal.setText(observable.getLblProductDescVal());
        txtAuctionMaxAmt.setText(CommonUtil.convertObjToStr(observable.getTxtAuctionMaxAmt()));  //AJITH
        txtAuctionMinAmt.setText(CommonUtil.convertObjToStr(observable.getTxtAuctionMinAmt()));  //AJITH
        rdoOnlyMembers_yes.setSelected(observable.getRdoOnlyMembers_yes());
        rdoOnlyMembers_no.setSelected(observable.getRdoOnlyMembers_no());
        rdoSuretyRequired_yes.setSelected(observable.getRdoSuretyRequired_yes());
        rdoSuretyRequired_no.setSelected(observable.getRdoSuretyRequired_no());
        rdoShortfall_yes.setSelected(observable.getRdoShortfall_yes());
        rdoShortfall_no.setSelected(observable.getRdoShortfall_no());
        rdoChitDefaults_yes.setSelected(observable.getRdoChitDefaults_yes());
        rdoChitDefaults_no.setSelected(observable.getRdoChitDefaults_no());
        rdoNonPrizedChit_yes.setSelected(observable.getRdoNonPrizedChit_yes());
        rdoNonPrizedChit_no.setSelected(observable.getRdoNonPrizedChit_no());
        rdoBonusRecoveredExistingChittal_yes.setSelected(observable.getRdoBonusRecoveredExistingChittal_yes());
        rdoBonusRecoveredExistingChittal_no.setSelected(observable.getRdoBonusRecoveredExistingChittal_no());
        if(rdoNonPrizedChit_yes.isSelected() == true){
            BonusEarliarVisible(true);
        }else{
            BonusEarliarVisible(false);
        }
        rdoBonusAllowed_yes.setSelected(observable.getRdoBonusAllowed_yes());
        rdoBonusAllowed_no.setSelected(observable.getRdoBonusAllowed_no());
        rdobonusPayableOwner_yes.setSelected(observable.getRdobonusPayableOwner_yes());
        rdobonusPayableOwner_no.setSelected(observable.getRdobonusPayableOwner_no());
        rdoPrizedChitOwner_yes.setSelected(observable.getRdoPrizedChitOwner_yes());
        rdoPrizedChitOwner_no.setSelected(observable.getRdoPrizedChitOwner_no());
        rdoPrizedChitOwnerAfter_yes.setSelected(observable.getRdoPrizedChitOwnerAfter_yes());
        rdoPrizedChitOwnerAfter_no.setSelected(observable.getRdoPrizedChitOwnerAfter_no());
        rdoprizedDefaulters_yes.setSelected(observable.getRdoprizedDefaulters_yes());
        rdoprizedDefaulters_no.setSelected(observable.getRdoprizedDefaulters_no());
        rdoBonusAmt_Yes.setSelected(observable.getRdoBonusAmt_Yes());
        rdoBonusAmt_No.setSelected(observable.getRdoBonusAmt_No());
        rdoBonusPayFirstIns_Yes.setSelected(observable.getRdoBonusPayFirstIns_Yes());
        rdoBonusPayFirstIns_No.setSelected(observable.getRdoBonusPayFirstIns_No());
        rdoAdvanceCollection_Yes.setSelected(observable.getRdoAdvanceCollection_Yes());
        rdoAdvanceCollection_No.setSelected(observable.getRdoAdvanceCollection_NO());
        if(rdoBonusAmt_Yes.isSelected()==true){
            cboRoundOffCriteria.setVisible(true);
        }else{
            cboRoundOffCriteria.setVisible(false);
        }
        cboCommisionRate.setSelectedItem(observable.getCboCommisionRate());
        cboPaymentTimeCharges.setSelectedItem(observable.getCbmPaymentTimeCharges().getDataForKey(observable.getCboPaymentTimeCharges()));
        txtpaymentTimeCharges.setText(CommonUtil.convertObjToStr(observable.getTxtpaymentTimeCharges()));   //AJITH
        cboDiscountRate.setSelectedItem(observable.getCboDiscountRate());
        cboPenalIntRate.setSelectedItem(observable.getCboPenalIntRate());
        cboPenalPrizedRate.setSelectedItem(observable.getCboPenalPrizedRate());
        cboLoanIntRate.setSelectedItem(observable.getCboLoanIntRate());
        cboPenalPeriod.setSelectedItem(observable.getCboPenalPeriod());
        cboPenalPrizedPeriod.setSelectedItem(observable.getCboPenalPrizedPeriod());
        cboPenalCalc.setSelectedItem(observable.getCboPenalCalc());
        cboPenalIntRateFullInstAmt.setSelectedItem(observable.getCboPenalIntRateFullInstAmt());
        cboPenalPrizedIntRateFullInstAmt.setSelectedItem(observable.getCboPenalPrizedIntRateFullInstAmt());
        System.out.println(" 0"+observable.getCboPenalCalc());
        System.out.println("1 "+observable.getCboPenalIntRateFullInstAmt());
        System.out.println("2 "+observable.getCboPenalPrizedIntRateFullInstAmt());
        cboholidayInt.setSelectedItem(observable.getCboholidayInt());
        txtCommisionRate.setText(CommonUtil.convertObjToStr(observable.getTxtCommisionRate()));  //AJITH
        txtDiscountRate.setText(CommonUtil.convertObjToStr(observable.getTxtDiscountRate()));  //AJITH
        txtPenalIntRate.setText(CommonUtil.convertObjToStr(observable.getTxtPenalIntRate()));  //AJITH
        txtPenalPrizedRate.setText(CommonUtil.convertObjToStr(observable.getTxtPenalPrizedRate()));  //AJITH
        txtLoanIntRate.setText(CommonUtil.convertObjToStr(observable.getTxtLoanIntRate()));  //AJITH
        txtBonusGracePeriod.setText(CommonUtil.convertObjToStr(observable.getTxtBonusGracePeriod()));  //AJITH
        txtBonusPrizedPeriod.setText(CommonUtil.convertObjToStr(observable.getTxtBonusPrizedPreriod()));  //AJITH
        txtPenalPeriod.setText(CommonUtil.convertObjToStr(observable.getTxtPenalPeriod()));  //AJITH
        txtPenalPrizedPeriod.setText(CommonUtil.convertObjToStr(observable.getTxtPenalPrizedPeriod()));  //AJITH
        txtDiscountPeriodEnd.setText(CommonUtil.convertObjToStr(observable.getTxtDiscountPeriod()));  //AJITH
        txtDiscountPrizedPeriodEnd.setText(CommonUtil.convertObjToStr(observable.getTxtDiscountPrizedPeriod()));  //AJITH
        txtMargin.setText(CommonUtil.convertObjToStr(observable.getTxtMargin()));  //AJITH
        txtMinLoanAmt.setText(CommonUtil.convertObjToStr(observable.getTxtMinLoanAmt()));  //AJITH
        txtMaxLoanAmt.setText(CommonUtil.convertObjToStr(observable.getTxtMaxLoanAmt()));  //AJITH
        
        rdoBonusGraceDays.setSelected(observable.getRdoBonusGraceDays());
        rdoBonusGraceMonth.setSelected(observable.getRdoBonusGraceMonth());
        rdoBonusGraceAfter.setSelected(observable.getRdoBonusGraceAfter());
        rdoBonusGraceEnd.setSelected(observable.getRdoBonusGraceEnd());
        
        rdoBonusPrizedDays.setSelected(observable.getRdoBonusPrizedDays());
        rdoBonusPrizedMonth.setSelected(observable.getRdoBonusPrizedMonth());
        rdoBonusPrizedAfter.setSelected(observable.getRdoBonusPrizedAfter());
        rdoBonusPrizedEnd.setSelected(observable.getRdoBonusPrizedEnd());
        
        rdoDiscountPrizedPeriodDays.setSelected(observable.getRdoDiscountPrizedPeriodDays());
        rdoDiscountPrizedPeriodMonth.setSelected(observable.getRdoDiscountPrizedPeriodMonth());
        rdoDiscountPrizedPeriodAfter.setSelected(observable.getRdoDiscountPrizedPeriodAfter());
        rdoDiscountPrizedPeriodEnd.setSelected(observable.getRdoDiscountPrizedPeriodEnd());
        
        rdoDiscountPeriodDays.setSelected(observable.getRdoDiscountPeriodDays());
        rdoDiscountPeriodMonth.setSelected(observable.getRdoDiscountPeriodMonth());
        rdoDiscountPeriodAfter.setSelected(observable.getRdoDiscountPeriodAfter());
        rdoDiscountPeriodEnd.setSelected(observable.getRdoDiscountPeriodEnd());
        
        chkAcceptClassA.setSelected(observable.getChkAcceptableClassA());
        chkAcceptClassB.setSelected(observable.getChkAcceptableClassB());
        chkAcceptClassC.setSelected(observable.getChkAcceptableClassC());
        chkAcceptClassAll.setSelected(observable.getChkAcceptableClassAll());
        
        chkAcceptableClassA.setSelected(observable.getChkAcceptClassA());
        chkAcceptableClassB.setSelected(observable.getChkAcceptClassB());
        chkAcceptableClassC.setSelected(observable.getChkAcceptClassC());
        chkAcceptableClassAll.setSelected(observable.getChkAcceptClassAll());
        chkFromAuctionEnrtry.setSelected(observable.isChkFromAuctionEnrtry());
        chkAfterCashPayment.setSelected(observable.isChkAfterCashPayment());
        chkSplitMDSTransaction.setSelected(observable.isChkSplitMDSTransaction());
        
        if(chkFromAuctionEnrtry.isSelected()){
            chkAfterCashPayment.setEnabled(false);
        }
        if(chkAfterCashPayment.isSelected()){
            chkFromAuctionEnrtry.setEnabled(false);
        }
        rdoDiscountAllowed_yes.setSelected(observable.getRdoDiscountAllowed_yes());
        rdoDiscountAllowed_no.setSelected(observable.getRdoDiscountAllowed_no());
        rdoLoanCanbeGranted_yes.setSelected(observable.getRdoLoanCanbeGranted_yes());
        rdoLoanCanbeGranted_no.setSelected(observable.getRdoLoanCanbeGranted_no());
        cboRoundOffCriteria.setSelectedItem(observable.getCboRoundOffCriteria());
        lblStatus.setText(observable.getLblStatus());
        addRadioButtons();
        if(observable.getRdoPending()!=null && !observable.getRdoPending().equals("") && observable.getRdoPending().equals("Y"))
        {
            System.out.println("pendingg inn");
            rdoPending.setSelected(true);
         //   rdoPrized.setSelected(false);
            
        }
        else
        {
            System.out.println("pendingg out");
               rdoPending.setSelected(false);
            //   rdoPrized.setSelected(true);
        }
        
         if(observable.getRdoPrized()!=null && !observable.getRdoPrized().equals("") && observable.getRdoPrized().equals("Y"))
        {
            System.out.println("prized inn");
            rdoPrized.setSelected(true);
         //   rdoPrized.setSelected(false);
            
        }
        else
        {
            System.out.println("prized out  ");
               rdoPrized.setSelected(false);
            //   rdoPrized.setSelected(true);
        }
        
        
         if(observable.getRdoMethod1()!=null && !observable.getRdoMethod1().equals("") && observable.getRdoMethod1().equals("Y"))
        {
         //   rdoPending.setSelected(false);
            rdoMethod1.setSelected(true);
            
        }
        else
        {
            //   rdoPending.setSelected(true);
               rdoMethod1.setSelected(false);
        }
         
         
          if(observable.getRdoMethod2()!=null && !observable.getRdoMethod2().equals("") && observable.getRdoMethod2().equals("Y"))
        {
         //   rdoPending.setSelected(false);
            rdoMethod2.setSelected(true);
            
        }
        else
        {
            //   rdoPending.setSelected(true);
               rdoMethod2.setSelected(false);
        }
         
         if(observable.getRdoTransFirstIns_Yes())
         {
             rdoTransFirstIns_Yes.setSelected(true);
             System.out.println("in tttt");
         }
         else
         {
              rdoTransFirstIns_No.setSelected(true);
         }
         
        if (observable.getRdoWhichEverIsLess() != null && !observable.getRdoWhichEverIsLess().equals("") && observable.getRdoWhichEverIsLess().equals("Y")) {
            rdoWhichEverIsLess.setSelected(true);

        } else {
            rdoWhichEverIsLess.setSelected(false);
        }
        
        if(observable.getChkIsGDS().equalsIgnoreCase("Y")){
            chkIsGDS.setSelected(true);
        }else{
            chkIsGDS.setSelected(false);
        }
        
        if(observable.getChkIsBonusTrans().equalsIgnoreCase("Y")){
            chkIsBonusTrans.setSelected(true);
        }else{
            chkIsBonusTrans.setSelected(false);
        }
        
        cboThalayalTransCategory.setSelectedItem(observable.getCbmThalayalTransCategory().getDataForKey(observable.getCboThalayalTransCategory()));
        cboMunnalTransCategory.setSelectedItem(observable.getCbmMunnalTransCategory().getDataForKey(observable.getCboMunnalTransCategory()));                  
    }
    
    // To Reset the Radio Buttons in the UI after any operation, We've to
    //1. Remove the Radio Buttons fron the Radio Groups...
    //2. Add the Radio Buttons Back in The Radio Groups...
    //a.) To Remove the Radio buttons...
    private void removeRadioButtons() {
        rdgOnlyMember.remove(rdoOnlyMembers_yes);
        rdgOnlyMember.remove(rdoOnlyMembers_no);
        
        rdgSurety.remove(rdoSuretyRequired_yes);
        rdgSurety.remove(rdoSuretyRequired_no);
        
        rdgShortfall.remove(rdoShortfall_yes);
        rdgShortfall.remove(rdoShortfall_no);
        
        rdgDefaulters.remove(rdoChitDefaults_yes);
        rdgDefaulters.remove(rdoChitDefaults_no);
        
        rdgNonPrizedChit.remove(rdoNonPrizedChit_yes);
        rdgNonPrizedChit.remove(rdoNonPrizedChit_no);
        
        rdgBonusRecoveredExistingChittal.remove(rdoBonusRecoveredExistingChittal_yes);
        rdgBonusRecoveredExistingChittal.remove(rdoBonusRecoveredExistingChittal_no);
        
        rdgBonusAllowed.remove(rdoBonusAllowed_yes);
        rdgBonusAllowed.remove(rdoBonusAllowed_no);
        
        rdgBonusPayableOwner.remove(rdobonusPayableOwner_yes);
        rdgBonusPayableOwner.remove(rdobonusPayableOwner_no);
        
        rdgPrizedChitOwner.remove(rdoPrizedChitOwner_yes);
        rdgPrizedChitOwner.remove(rdoPrizedChitOwner_no);
        
        rdgPrizedChitOwnerAfter.remove(rdoPrizedChitOwnerAfter_yes);
        rdgPrizedChitOwnerAfter.remove(rdoPrizedChitOwnerAfter_no);
        
        rdgPrizedDefaulters.remove(rdoprizedDefaulters_yes);
        rdgPrizedDefaulters.remove(rdoprizedDefaulters_no);
        
        rdgDiscountAllowed.remove(rdoDiscountAllowed_yes);
        rdgDiscountAllowed.remove(rdoDiscountAllowed_no);
        
        rdgLoanCanbeGranted.remove(rdoLoanCanbeGranted_yes);
        rdgLoanCanbeGranted.remove(rdoLoanCanbeGranted_no);
        
        rdgBonusGrace.remove(rdoBonusGraceDays);
        rdgBonusGrace.remove(rdoBonusGraceMonth);
        rdgBonusGrace.remove(rdoBonusGraceAfter);
        rdgBonusGrace.remove(rdoBonusGraceEnd);
        
        rdgBonusPrized.remove(rdoBonusPrizedDays);
        rdgBonusPrized.remove(rdoBonusPrizedMonth);
        rdgBonusPrized.remove(rdoBonusPrizedAfter);
        rdgBonusPrized.remove(rdoBonusPrizedEnd);
        
        rdgDiscountPeriod.remove(rdoDiscountPeriodDays);
        rdgDiscountPeriod.remove(rdoDiscountPeriodMonth);
        rdgDiscountPeriod.remove(rdoDiscountPeriodAfter);
        rdgDiscountPeriod.remove(rdoDiscountPeriodEnd);
        
        rdgDiscountPrizedPeriod.remove(rdoDiscountPrizedPeriodDays);
        rdgDiscountPrizedPeriod.remove(rdoDiscountPrizedPeriodMonth);
        rdgDiscountPrizedPeriod.remove(rdoDiscountPrizedPeriodAfter);
        rdgDiscountPrizedPeriod.remove(rdoDiscountPrizedPeriodEnd);
        
    }
    
    // b.) To Add the Radio buttons...
    private void addRadioButtons() {
        rdgOnlyMember = new CButtonGroup();
        rdgOnlyMember.add(rdoOnlyMembers_yes);
        rdgOnlyMember.add(rdoOnlyMembers_no);
        
        rdgSurety = new CButtonGroup();
        rdgSurety.add(rdoSuretyRequired_yes);
        rdgSurety.add(rdoSuretyRequired_no);
        
        rdgShortfall = new CButtonGroup();
        rdgShortfall.add(rdoShortfall_yes);
        rdgShortfall.add(rdoShortfall_no);
        
        rdgDefaulters = new CButtonGroup();
        rdgDefaulters.add(rdoChitDefaults_yes);
        rdgDefaulters.add(rdoChitDefaults_no);
        
        rdgNonPrizedChit = new CButtonGroup();
        rdgNonPrizedChit.add(rdoNonPrizedChit_yes);
        rdgNonPrizedChit.add(rdoNonPrizedChit_no);
        
        rdgBonusRecoveredExistingChittal = new CButtonGroup();
        rdgBonusRecoveredExistingChittal.add(rdoBonusRecoveredExistingChittal_yes);
        rdgBonusRecoveredExistingChittal.add(rdoBonusRecoveredExistingChittal_no);
                
        rdgBonusAllowed = new CButtonGroup();
        rdgBonusAllowed.add(rdoBonusAllowed_yes);
        rdgBonusAllowed.add(rdoBonusAllowed_no);
        
        rdgBonusPayableOwner = new CButtonGroup();
        rdgBonusPayableOwner.add(rdobonusPayableOwner_yes);
        rdgBonusPayableOwner.add(rdobonusPayableOwner_no);
        
        rdgPrizedChitOwner = new CButtonGroup();
        rdgPrizedChitOwner.add(rdoPrizedChitOwner_yes);
        rdgPrizedChitOwner.add(rdoPrizedChitOwner_no);
        
        rdgPrizedChitOwnerAfter = new CButtonGroup();
        rdgPrizedChitOwnerAfter.add(rdoPrizedChitOwnerAfter_yes);
        rdgPrizedChitOwnerAfter.add(rdoPrizedChitOwnerAfter_no);
        
        rdgPrizedDefaulters = new CButtonGroup();
        rdgPrizedDefaulters.add(rdoprizedDefaulters_yes);
        rdgPrizedDefaulters.add(rdoprizedDefaulters_no);
        
        rdgDiscountAllowed = new CButtonGroup();
        rdgDiscountAllowed.add(rdoDiscountAllowed_yes);
        rdgDiscountAllowed.add(rdoDiscountAllowed_no);
        
        rdgLoanCanbeGranted = new CButtonGroup();
        rdgLoanCanbeGranted.add(rdoLoanCanbeGranted_yes);
        rdgLoanCanbeGranted.add(rdoLoanCanbeGranted_no);
        
        rdgBonusGrace = new CButtonGroup();
        rdgBonusGrace.add(rdoBonusGraceDays);
        rdgBonusGrace.add(rdoBonusGraceMonth);
        rdgBonusGrace.add(rdoBonusGraceAfter);
        rdgBonusGrace.add(rdoBonusGraceEnd);
        
        rdgBonusPrized = new CButtonGroup();
        rdgBonusPrized.add(rdoBonusPrizedDays);
        rdgBonusPrized.add(rdoBonusPrizedMonth);
        rdgBonusPrized.add(rdoBonusPrizedAfter);
        rdgBonusPrized.add(rdoBonusPrizedEnd);
        
        rdgDiscountPeriod = new CButtonGroup();
        rdgDiscountPeriod.add(rdoDiscountPeriodDays);
        rdgDiscountPeriod.add(rdoDiscountPeriodMonth);
        rdgDiscountPeriod.add(rdoDiscountPeriodAfter);
        rdgDiscountPeriod.add(rdoDiscountPeriodEnd);
        
        rdgDiscountPrizedPeriod = new CButtonGroup();
        rdgDiscountPrizedPeriod.add(rdoDiscountPrizedPeriodDays);
        rdgDiscountPrizedPeriod.add(rdoDiscountPrizedPeriodMonth);
        rdgDiscountPrizedPeriod.add(rdoDiscountPrizedPeriodAfter);
        rdgDiscountPrizedPeriod.add(rdoDiscountPrizedPeriodEnd);
    }
    
    /** Auto Generated Method - updateOBFields()
     * This method called by Save option of UI.
     * It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        
        observable.setTxtProductId(txtProductId.getText());
        observable.setLblProductDescVal(lblProductDescVal.getText());
        observable.setTxtAuctionMaxAmt(CommonUtil.convertObjToDouble(txtAuctionMaxAmt.getText()));    //AJITH
        observable.setTxtAuctionMinAmt(CommonUtil.convertObjToDouble(txtAuctionMinAmt.getText()));    //AJITH
        observable.setRdoOnlyMembers_yes(rdoOnlyMembers_yes.isSelected());
        observable.setRdoOnlyMembers_no(rdoOnlyMembers_no.isSelected());
        observable.setRdoSuretyRequired_yes(rdoSuretyRequired_yes.isSelected());
        observable.setRdoSuretyRequired_no(rdoSuretyRequired_no.isSelected());
        observable.setRdoShortfall_yes(rdoShortfall_yes.isSelected());
        observable.setRdoShortfall_no(rdoShortfall_no.isSelected());
        observable.setRdoChitDefaults_yes(rdoChitDefaults_yes.isSelected());
        observable.setRdoChitDefaults_no(rdoChitDefaults_no.isSelected());
        observable.setRdoNonPrizedChit_yes(rdoNonPrizedChit_yes.isSelected());
        observable.setRdoNonPrizedChit_no(rdoNonPrizedChit_no.isSelected());
        observable.setRdoBonusRecoveredExistingChittal_yes(rdoBonusRecoveredExistingChittal_yes.isSelected());
        observable.setRdoBonusRecoveredExistingChittal_no(rdoBonusRecoveredExistingChittal_no.isSelected());        
        observable.setRdoBonusAllowed_yes(rdoBonusAllowed_yes.isSelected());
        observable.setRdoBonusAllowed_no(rdoBonusAllowed_no.isSelected());
        observable.setRdobonusPayableOwner_yes(rdobonusPayableOwner_yes.isSelected());
        observable.setRdobonusPayableOwner_no(rdobonusPayableOwner_no.isSelected());
        observable.setRdoPrizedChitOwner_yes(rdoPrizedChitOwner_yes.isSelected());
        observable.setRdoPrizedChitOwner_no(rdoPrizedChitOwner_no.isSelected());
        observable.setRdoPrizedChitOwnerAfter_yes(rdoPrizedChitOwnerAfter_yes.isSelected());
        observable.setRdoPrizedChitOwnerAfter_no(rdoPrizedChitOwnerAfter_no.isSelected());
        observable.setRdoprizedDefaulters_yes(rdoprizedDefaulters_yes.isSelected());
        observable.setRdoprizedDefaulters_no(rdoprizedDefaulters_no.isSelected());
        observable.setRdoBonusAmt_Yes(rdoBonusAmt_Yes.isSelected());
        observable.setRdoBonusAmt_No(rdoBonusAmt_No.isSelected());
        observable.setRdoBonusPayFirstIns_Yes(rdoBonusPayFirstIns_Yes.isSelected());
        observable.setRdoBonusPayFirstIns_No(rdoBonusPayFirstIns_No.isSelected());
        observable.setRdoAdvanceCollection_Yes(rdoAdvanceCollection_Yes.isSelected());
        observable.setRdoAdvanceCollection_NO(rdoAdvanceCollection_No.isSelected());
        
        observable.setCboholidayInt(CommonUtil.convertObjToStr(cboholidayInt.getSelectedItem()));
        observable.setCboCommisionRate(CommonUtil.convertObjToStr(cboCommisionRate.getSelectedItem()));
        observable.setCboPaymentTimeCharges(CommonUtil.convertObjToStr(observable.getCbmPaymentTimeCharges().getKeyForSelected()));
        observable.setCboDiscountRate(CommonUtil.convertObjToStr(cboDiscountRate.getSelectedItem()));
        observable.setCboPenalIntRate(CommonUtil.convertObjToStr(cboPenalIntRate.getSelectedItem()));
        observable.setCboPenalPrizedRate(CommonUtil.convertObjToStr(cboPenalPrizedRate.getSelectedItem()));
        observable.setCboLoanIntRate(CommonUtil.convertObjToStr(cboLoanIntRate.getSelectedItem()));
        observable.setCboPenalPeriod(CommonUtil.convertObjToStr(cboPenalPeriod.getSelectedItem()));
        observable.setCboPenalPrizedPeriod(CommonUtil.convertObjToStr(cboPenalPrizedPeriod.getSelectedItem()));
        observable.setCboPenalCalc(CommonUtil.convertObjToStr(cboPenalCalc.getSelectedItem()));
        observable.setCboPenalIntRateFullInstAmt(CommonUtil.convertObjToStr(cboPenalIntRateFullInstAmt.getSelectedItem()));
        observable.setCboPenalPrizedIntRateFullInstAmt(CommonUtil.convertObjToStr(cboPenalPrizedIntRateFullInstAmt.getSelectedItem()));
        observable.setTxtpaymentTimeCharges(CommonUtil.convertObjToDouble(txtpaymentTimeCharges.getText()));   //AJITH
        
        observable.setTxtCommisionRate(CommonUtil.convertObjToDouble(txtCommisionRate.getText()));  //AJITH
        observable.setTxtDiscountRate(CommonUtil.convertObjToDouble(txtDiscountRate.getText()));  //AJITH
        observable.setTxtPenalIntRate(CommonUtil.convertObjToDouble(txtPenalIntRate.getText()));  //AJITH
        observable.setTxtPenalPrizedRate(CommonUtil.convertObjToDouble(txtPenalPrizedRate.getText()));  //AJITH
        observable.setTxtLoanIntRate(CommonUtil.convertObjToDouble(txtLoanIntRate.getText()));  //AJITH
        observable.setTxtBonusGracePeriod(CommonUtil.convertObjToDouble(txtBonusGracePeriod.getText()));  //AJITH
        observable.setTxtBonusPrizedPreriod(CommonUtil.convertObjToDouble(txtBonusPrizedPeriod.getText()));  //AJITH
        observable.setTxtPenalPeriod(CommonUtil.convertObjToDouble(txtPenalPeriod.getText()));  //AJITH
        observable.setTxtPenalPrizedPeriod(CommonUtil.convertObjToDouble(txtPenalPrizedPeriod.getText()));  //AJITH
        observable.setTxtDiscountPeriod(CommonUtil.convertObjToDouble(txtDiscountPeriodEnd.getText()));  //AJITH
        observable.setTxtDiscountPrizedPeriod(CommonUtil.convertObjToDouble(txtDiscountPrizedPeriodEnd.getText()));  //AJITH
        observable.setTxtMargin(CommonUtil.convertObjToDouble(txtMargin.getText()));  //AJITH
        observable.setTxtMinLoanAmt(CommonUtil.convertObjToDouble(txtMinLoanAmt.getText()));  //AJITH
        observable.setTxtMaxLoanAmt(CommonUtil.convertObjToDouble(txtMaxLoanAmt.getText()));  //AJITH
        observable.setTxtpaymentTimeCharges(CommonUtil.convertObjToDouble(txtpaymentTimeCharges.getText()));
        
        observable.setRdoDiscountPrizedPeriodDays(rdoDiscountPrizedPeriodDays.isSelected());
        observable.setRdoDiscountPrizedPeriodMonth(rdoDiscountPrizedPeriodMonth.isSelected());
        observable.setRdoDiscountPrizedPeriodAfter(rdoDiscountPrizedPeriodAfter.isSelected());
        observable.setRdoDiscountPrizedPeriodEnd(rdoDiscountPrizedPeriodEnd.isSelected());
        
        observable.setRdoDiscountPeriodDays(rdoDiscountPeriodDays.isSelected());
        observable.setRdoDiscountPeriodMonth(rdoDiscountPeriodMonth.isSelected());
        observable.setRdoDiscountPeriodAfter(rdoDiscountPeriodAfter.isSelected());
        observable.setRdoDiscountPeriodEnd(rdoDiscountPeriodEnd.isSelected());
        
        observable.setRdoBonusPrizedDays(rdoBonusPrizedDays.isSelected());
        observable.setRdoBonusPrizedMonth(rdoBonusPrizedMonth.isSelected());
        observable.setRdoBonusPrizedAfter(rdoBonusPrizedAfter.isSelected());
        observable.setRdoBonusPrizedEnd(rdoBonusPrizedEnd.isSelected());
        
        observable.setRdoBonusGraceDays(rdoBonusGraceDays.isSelected());
        observable.setRdoBonusGraceMonth(rdoBonusGraceMonth.isSelected());
        observable.setRdoBonusGraceAfter(rdoBonusGraceAfter.isSelected());
        observable.setRdoBonusGraceEnd(rdoBonusGraceEnd.isSelected());
        
        observable.setChkAcceptableClassA(chkAcceptClassA.isSelected());
        observable.setChkAcceptableClassB(chkAcceptClassB.isSelected());
        observable.setChkAcceptableClassC(chkAcceptClassC.isSelected());
        observable.setChkAcceptableClassAll(chkAcceptClassAll.isSelected());
        
        observable.setChkAcceptClassA(chkAcceptableClassA.isSelected());
        observable.setChkAcceptClassB(chkAcceptableClassB.isSelected());
        observable.setChkAcceptClassC(chkAcceptableClassC.isSelected());
        observable.setChkAcceptClassAll(chkAcceptableClassAll.isSelected());
        observable.setChkFromAuctionEnrtry(chkFromAuctionEnrtry.isSelected());
        observable.setChkAfterCashPayment(chkAfterCashPayment.isSelected());
        observable.setChkSplitMDSTransaction(chkSplitMDSTransaction.isSelected());
        
        observable.setRdoDiscountAllowed_yes(rdoDiscountAllowed_yes.isSelected());
        observable.setRdoDiscountAllowed_no(rdoDiscountAllowed_no.isSelected());
        observable.setRdoLoanCanbeGranted_yes(rdoLoanCanbeGranted_yes.isSelected());
        observable.setRdoLoanCanbeGranted_no(rdoLoanCanbeGranted_no.isSelected());
        observable.setCboRoundOffCriteria((String) cboRoundOffCriteria.getSelectedItem());
        
        observable.setRdoTransFirstIns_Yes(rdoTransFirstIns_Yes.isSelected());
        observable.setRdoTransFirstIns_No(rdoTransFirstIns_No.isSelected());
        if(rdoPending.isSelected())
        {
           observable.setRdoPending("Y");
           
        }
        else
        {
            observable.setRdoPending("N");
        }
        
        System.out.println("rdoPending.isSelected()"+rdoPending.isSelected());
         if(rdoPrized.isSelected())
        {
           observable.setRdoPrized("Y");
        }
        else
        {
            observable.setRdoPrized("N");
        }
        if (rdoWhichEverIsLess.isSelected()) {
            observable.setRdoWhichEverIsLess("Y");
        } else {
            observable.setRdoWhichEverIsLess("N");
        }
         if(rdoMethod2.isSelected())
         {
             observable.setRdoMethod2("Y");
         }
         else
         {
              observable.setRdoMethod2("N");
         }
         
         
         
           if(rdoMethod1.isSelected())
         {
             observable.setRdoMethod1("Y");
         }
         else
         {
              observable.setRdoMethod1("N");
         }
         
           if(chkIsGDS.isSelected()){
               observable.setChkIsGDS("Y");
           }else{
               observable.setChkIsGDS("N");
           }
           
           if(chkIsBonusTrans.isSelected()){
               observable.setChkIsBonusTrans("Y");
           }else{
               observable.setChkIsBonusTrans("N"); 
           }
           
           observable.setCboThalayalTransCategory(CommonUtil.convertObjToStr(observable.getCbmThalayalTransCategory().getKeyForSelected()));
           observable.setCboMunnalTransCategory(CommonUtil.convertObjToStr(observable.getCbmMunnalTransCategory().getKeyForSelected()));
        
         
         System.out.println("mmm in ob update"+observable.getRdoPrized()+observable.getRdoPrized());
    }
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdgOnlyMember = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgSurety = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgShortfall = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDefaulters = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgNonPrizedChit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBonusAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBonusPayableOwner = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrizedChitOwner = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrizedChitOwnerAfter = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgPrizedDefaulters = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDiscountAllowed = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgLoanCanbeGranted = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBonusGrace = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBonusPrized = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDiscountPeriod = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgDiscountPrizedPeriod = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBonusRounding = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBonusPayFirstIns = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgBonusRecoveredExistingChittal = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgAdvanceCollection = new com.see.truetransact.uicomponent.CButtonGroup();
        rdgTransFirstIns = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoGroupSecurity = new com.see.truetransact.uicomponent.CButtonGroup();
        commencmtGrp = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrAdvances = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace40 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace41 = new com.see.truetransact.uicomponent.CLabel();
        btnCopy = new com.see.truetransact.uicomponent.CButton();
        lblSpace42 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace43 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace44 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace45 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace46 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        tabMDSProduct = new com.see.truetransact.uicomponent.CTabbedPane();
        panGeneral = new com.see.truetransact.uicomponent.CPanel();
        panLapsedGR = new com.see.truetransact.uicomponent.CPanel();
        panGeneralDetails = new com.see.truetransact.uicomponent.CPanel();
        panNonPrizedChit = new com.see.truetransact.uicomponent.CPanel();
        rdoNonPrizedChit_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoNonPrizedChit_no = new com.see.truetransact.uicomponent.CRadioButton();
        panShortfall = new com.see.truetransact.uicomponent.CPanel();
        rdoShortfall_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoShortfall_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblNonPrizedChit = new com.see.truetransact.uicomponent.CLabel();
        lblChitDefaults = new com.see.truetransact.uicomponent.CLabel();
        lblShortfall = new com.see.truetransact.uicomponent.CLabel();
        lblSuretyRequired = new com.see.truetransact.uicomponent.CLabel();
        panPrintServicesGR5 = new com.see.truetransact.uicomponent.CPanel();
        rdoChitDefaults_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoChitDefaults_no = new com.see.truetransact.uicomponent.CRadioButton();
        panSuretyRequired = new com.see.truetransact.uicomponent.CPanel();
        rdoSuretyRequired_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSuretyRequired_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblChitDefaults1 = new com.see.truetransact.uicomponent.CLabel();
        lblNonPrizedChit1 = new com.see.truetransact.uicomponent.CLabel();
        panAcceptableType = new com.see.truetransact.uicomponent.CPanel();
        lblAcceptableType5 = new com.see.truetransact.uicomponent.CLabel();
        chkAcceptableClassAll = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcceptableClassC = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcceptableClassB = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcceptableClassA = new com.see.truetransact.uicomponent.CCheckBox();
        lblAcceptableType7 = new com.see.truetransact.uicomponent.CLabel();
        lblAcceptableType8 = new com.see.truetransact.uicomponent.CLabel();
        lblAcceptableType9 = new com.see.truetransact.uicomponent.CLabel();
        lblAcceptType1 = new com.see.truetransact.uicomponent.CLabel();
        panAcceptType = new com.see.truetransact.uicomponent.CPanel();
        lblAcceptableType10 = new com.see.truetransact.uicomponent.CLabel();
        chkAcceptClassAll = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcceptClassC = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcceptClassB = new com.see.truetransact.uicomponent.CCheckBox();
        chkAcceptClassA = new com.see.truetransact.uicomponent.CCheckBox();
        lblAcceptableType12 = new com.see.truetransact.uicomponent.CLabel();
        lblAcceptableType13 = new com.see.truetransact.uicomponent.CLabel();
        lblAcceptableType14 = new com.see.truetransact.uicomponent.CLabel();
        lblAcceptType2 = new com.see.truetransact.uicomponent.CLabel();
        lblBonusPayFirstIns = new com.see.truetransact.uicomponent.CLabel();
        panBonusPayFirstIns = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusPayFirstIns_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusPayFirstIns_No = new com.see.truetransact.uicomponent.CRadioButton();
        panBonusRecoveredExistingChittal = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusRecoveredExistingChittal_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusRecoveredExistingChittal_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblBonusRecoveredExistingChittal = new com.see.truetransact.uicomponent.CLabel();
        panProductDetails = new com.see.truetransact.uicomponent.CPanel();
        lblProductDesc = new com.see.truetransact.uicomponent.CLabel();
        lblProductId = new com.see.truetransact.uicomponent.CLabel();
        txtProductId = new com.see.truetransact.uicomponent.CTextField();
        lblProductDescVal = new com.see.truetransact.uicomponent.CTextField();
        panPrintServicesGR1 = new com.see.truetransact.uicomponent.CPanel();
        rdoOnlyMembers_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoOnlyMembers_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblOnlyMembers = new com.see.truetransact.uicomponent.CLabel();
        panTransFirstIns = new com.see.truetransact.uicomponent.CPanel();
        rdoTransFirstIns_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoTransFirstIns_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblTransFirstIns = new com.see.truetransact.uicomponent.CLabel();
        panGeneralDetailsEntry = new com.see.truetransact.uicomponent.CPanel();
        panBonusAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusAllowed_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusAllowed_no = new com.see.truetransact.uicomponent.CRadioButton();
        panPrintServicesGR35 = new com.see.truetransact.uicomponent.CPanel();
        rdoPrizedChitOwner_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPrizedChitOwner_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblBonusChitOwner = new com.see.truetransact.uicomponent.CLabel();
        lblBonusAllowed = new com.see.truetransact.uicomponent.CLabel();
        lblHolidayInst = new com.see.truetransact.uicomponent.CLabel();
        panPrintServicesGR36 = new com.see.truetransact.uicomponent.CPanel();
        rdoPrizedChitOwnerAfter_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPrizedChitOwnerAfter_no = new com.see.truetransact.uicomponent.CRadioButton();
        panHolidayInst = new com.see.truetransact.uicomponent.CPanel();
        rdoprizedDefaulters_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoprizedDefaulters_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblBonusChitOwner1 = new com.see.truetransact.uicomponent.CLabel();
        lblBonusChitOwner2 = new com.see.truetransact.uicomponent.CLabel();
        lblBonusChitOwner3 = new com.see.truetransact.uicomponent.CLabel();
        panPrintServicesGR37 = new com.see.truetransact.uicomponent.CPanel();
        rdobonusPayableOwner_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdobonusPayableOwner_no = new com.see.truetransact.uicomponent.CRadioButton();
        cboholidayInt = new com.see.truetransact.uicomponent.CComboBox();
        chkFromAuctionEnrtry = new com.see.truetransact.uicomponent.CCheckBox();
        chkAfterCashPayment = new com.see.truetransact.uicomponent.CCheckBox();
        chkSplitMDSTransaction = new com.see.truetransact.uicomponent.CCheckBox();
        panInsideGeneralDetails = new com.see.truetransact.uicomponent.CPanel();
        panSecurityAgainst = new com.see.truetransact.uicomponent.CPanel();
        rdoPending = new com.see.truetransact.uicomponent.CRadioButton();
        rdoPrized = new com.see.truetransact.uicomponent.CRadioButton();
        rdoWhichEverIsLess = new com.see.truetransact.uicomponent.CRadioButton();
        panBonusRounding = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusAmt_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusAmt_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblBonusAmountRounding = new com.see.truetransact.uicomponent.CLabel();
        panAdvanceCollection = new com.see.truetransact.uicomponent.CPanel();
        rdoAdvanceCollection_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoAdvanceCollection_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblAdvanceCollection = new com.see.truetransact.uicomponent.CLabel();
        panAuctionMaxAmt = new com.see.truetransact.uicomponent.CPanel();
        txtAuctionMaxAmt = new com.see.truetransact.uicomponent.CTextField();
        panAuctionMinAmt = new com.see.truetransact.uicomponent.CPanel();
        txtAuctionMinAmt = new com.see.truetransact.uicomponent.CTextField();
        lblAuctionMaxAmt = new com.see.truetransact.uicomponent.CLabel();
        lblAuctionMinAmt = new com.see.truetransact.uicomponent.CLabel();
        cboRoundOffCriteria = new com.see.truetransact.uicomponent.CComboBox();
        lblSecurityAgainst = new com.see.truetransact.uicomponent.CLabel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        panCommencmnt = new com.see.truetransact.uicomponent.CPanel();
        rdoMethod2 = new com.see.truetransact.uicomponent.CRadioButton();
        rdoMethod1 = new com.see.truetransact.uicomponent.CRadioButton();
        chkIsGDS = new com.see.truetransact.uicomponent.CCheckBox();
        chkIsBonusTrans = new com.see.truetransact.uicomponent.CCheckBox();
        lblAuctionMaxAmtVal = new com.see.truetransact.uicomponent.CLabel();
        lblAuctionMinAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panOtherDetails = new com.see.truetransact.uicomponent.CPanel();
        panMDSLoan = new com.see.truetransact.uicomponent.CPanel();
        lblMaxLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        lblMinLoanAmt = new com.see.truetransact.uicomponent.CLabel();
        txtMaxLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        txtMinLoanAmt = new com.see.truetransact.uicomponent.CTextField();
        lblLoanCanbeGranted = new com.see.truetransact.uicomponent.CLabel();
        panLoanCanbeGranted = new com.see.truetransact.uicomponent.CPanel();
        rdoLoanCanbeGranted_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoLoanCanbeGranted_no = new com.see.truetransact.uicomponent.CRadioButton();
        lblMargin = new com.see.truetransact.uicomponent.CLabel();
        panMargin = new com.see.truetransact.uicomponent.CPanel();
        txtMargin = new com.see.truetransact.uicomponent.CTextField();
        lblMarginVal = new com.see.truetransact.uicomponent.CLabel();
        panDiscountPeriodDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDiscountPrizedPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblDiscountPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblDiscountAllowed = new com.see.truetransact.uicomponent.CLabel();
        panDiscountAllowed = new com.see.truetransact.uicomponent.CPanel();
        rdoDiscountAllowed_yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDiscountAllowed_no = new com.see.truetransact.uicomponent.CRadioButton();
        panDiscountPrizedPeriod = new com.see.truetransact.uicomponent.CPanel();
        rdoDiscountPrizedPeriodEnd = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDiscountPrizedPeriodAfter = new com.see.truetransact.uicomponent.CRadioButton();
        panDiscountAllowed3 = new com.see.truetransact.uicomponent.CPanel();
        rdoDiscountPrizedPeriodDays = new com.see.truetransact.uicomponent.CRadioButton();
        txtDiscountPrizedPeriodEnd = new com.see.truetransact.uicomponent.CTextField();
        rdoDiscountPrizedPeriodMonth = new com.see.truetransact.uicomponent.CRadioButton();
        panDiscountPeriod = new com.see.truetransact.uicomponent.CPanel();
        rdoDiscountPeriodEnd = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDiscountPeriodAfter = new com.see.truetransact.uicomponent.CRadioButton();
        panDiscountAllowed4 = new com.see.truetransact.uicomponent.CPanel();
        rdoDiscountPeriodDays = new com.see.truetransact.uicomponent.CRadioButton();
        rdoDiscountPeriodMonth = new com.see.truetransact.uicomponent.CRadioButton();
        txtDiscountPeriodEnd = new com.see.truetransact.uicomponent.CTextField();
        panGracePeriodDetails = new com.see.truetransact.uicomponent.CPanel();
        lblPenalPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblPenalPrizedPeriod = new com.see.truetransact.uicomponent.CLabel();
        lblBonusPrizedPreriod = new com.see.truetransact.uicomponent.CLabel();
        lblBonusGracePeriod = new com.see.truetransact.uicomponent.CLabel();
        panBonusGracePeriod = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusPrizedEnd = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusPrizedAfter = new com.see.truetransact.uicomponent.CRadioButton();
        panDiscountAllowed1 = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusPrizedDays = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusPrizedMonth = new com.see.truetransact.uicomponent.CRadioButton();
        txtBonusPrizedPeriod = new com.see.truetransact.uicomponent.CTextField();
        panPenalPeriod = new com.see.truetransact.uicomponent.CPanel();
        cboPenalPeriod = new com.see.truetransact.uicomponent.CComboBox();
        txtPenalPeriod = new com.see.truetransact.uicomponent.CTextField();
        panPenalPrizedPeriod = new com.see.truetransact.uicomponent.CPanel();
        cboPenalPrizedPeriod = new com.see.truetransact.uicomponent.CComboBox();
        txtPenalPrizedPeriod = new com.see.truetransact.uicomponent.CTextField();
        lblPenalCalc = new com.see.truetransact.uicomponent.CLabel();
        panBonusGracePeriod1 = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusGraceEnd = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusGraceAfter = new com.see.truetransact.uicomponent.CRadioButton();
        panDiscountAllowed2 = new com.see.truetransact.uicomponent.CPanel();
        rdoBonusGraceDays = new com.see.truetransact.uicomponent.CRadioButton();
        rdoBonusGraceMonth = new com.see.truetransact.uicomponent.CRadioButton();
        txtBonusGracePeriod = new com.see.truetransact.uicomponent.CTextField();
        panPenalCalculation = new com.see.truetransact.uicomponent.CPanel();
        cboPenalCalc = new com.see.truetransact.uicomponent.CComboBox();
        panRateDetails = new com.see.truetransact.uicomponent.CPanel();
        lblCommisionRate = new com.see.truetransact.uicomponent.CLabel();
        lblDiscountRate = new com.see.truetransact.uicomponent.CLabel();
        lblPenalIntRate = new com.see.truetransact.uicomponent.CLabel();
        lblPenalPrizedRate = new com.see.truetransact.uicomponent.CLabel();
        lblLoanIntRate = new com.see.truetransact.uicomponent.CLabel();
        panCommisionRate = new com.see.truetransact.uicomponent.CPanel();
        cboCommisionRate = new com.see.truetransact.uicomponent.CComboBox();
        txtCommisionRate = new com.see.truetransact.uicomponent.CTextField();
        lblCommisionRateVal = new com.see.truetransact.uicomponent.CLabel();
        panLoanIntRate = new com.see.truetransact.uicomponent.CPanel();
        cboLoanIntRate = new com.see.truetransact.uicomponent.CComboBox();
        txtLoanIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblLoanIntRateVal = new com.see.truetransact.uicomponent.CLabel();
        panPenalIntRate = new com.see.truetransact.uicomponent.CPanel();
        cboPenalIntRate = new com.see.truetransact.uicomponent.CComboBox();
        txtPenalIntRate = new com.see.truetransact.uicomponent.CTextField();
        lblPenalIntRateVal = new com.see.truetransact.uicomponent.CLabel();
        panPenalPrizedRate = new com.see.truetransact.uicomponent.CPanel();
        cboPenalPrizedRate = new com.see.truetransact.uicomponent.CComboBox();
        txtPenalPrizedRate = new com.see.truetransact.uicomponent.CTextField();
        lblPenalPrizedRateVal = new com.see.truetransact.uicomponent.CLabel();
        panDiscountRate = new com.see.truetransact.uicomponent.CPanel();
        cboDiscountRate = new com.see.truetransact.uicomponent.CComboBox();
        txtDiscountRate = new com.see.truetransact.uicomponent.CTextField();
        lblDiscountRateVal = new com.see.truetransact.uicomponent.CLabel();
        cboPenalIntRateFullInstAmt = new com.see.truetransact.uicomponent.CComboBox();
        cboPenalPrizedIntRateFullInstAmt = new com.see.truetransact.uicomponent.CComboBox();
        lblOperatesForPenalInt = new com.see.truetransact.uicomponent.CLabel();
        lblOperatesForPenalPrzInt = new com.see.truetransact.uicomponent.CLabel();
        lblPaymentTimeCharges = new com.see.truetransact.uicomponent.CLabel();
        cboPaymentTimeCharges = new com.see.truetransact.uicomponent.CComboBox();
        txtpaymentTimeCharges = new com.see.truetransact.uicomponent.CTextField();
        panThalayalMunnalTrans = new com.see.truetransact.uicomponent.CPanel();
        lblThalayalTransCategory = new com.see.truetransact.uicomponent.CLabel();
        cboThalayalTransCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblMunnalTransCategory = new com.see.truetransact.uicomponent.CLabel();
        cboMunnalTransCategory = new com.see.truetransact.uicomponent.CComboBox();
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
        setMinimumSize(new java.awt.Dimension(900, 650));
        setPreferredSize(new java.awt.Dimension(900, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setEnabled(false);
        btnView.setMinimumSize(new java.awt.Dimension(29, 27));
        btnView.setPreferredSize(new java.awt.Dimension(29, 27));
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

        btnCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_COPY.gif"))); // NOI18N
        btnCopy.setToolTipText("Copy");
        btnCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCopyActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnCopy);

        lblSpace42.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace42.setText("     ");
        lblSpace42.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace42.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace42);

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

        lblSpace43.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace43.setText("     ");
        lblSpace43.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace43.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace43);

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

        lblSpace44.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace44.setText("     ");
        lblSpace44.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace44.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace44);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrAdvances.add(btnException);

        lblSpace45.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace45.setText("     ");
        lblSpace45.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace45.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace45);

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

        lblSpace46.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace46.setText("     ");
        lblSpace46.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace46.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrAdvances.add(lblSpace46);

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

        tabMDSProduct.setMinimumSize(new java.awt.Dimension(680, 480));
        tabMDSProduct.setPreferredSize(new java.awt.Dimension(680, 480));

        panGeneral.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneral.setMinimumSize(new java.awt.Dimension(570, 450));
        panGeneral.setPreferredSize(new java.awt.Dimension(570, 450));
        panGeneral.setLayout(new java.awt.GridBagLayout());

        panLapsedGR.setPreferredSize(new java.awt.Dimension(225, 87));
        panLapsedGR.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panGeneral.add(panLapsedGR, gridBagConstraints);

        panGeneralDetails.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panGeneralDetails.setMinimumSize(new java.awt.Dimension(500, 400));
        panGeneralDetails.setPreferredSize(new java.awt.Dimension(500, 400));
        panGeneralDetails.setLayout(new java.awt.GridBagLayout());

        panNonPrizedChit.setMinimumSize(new java.awt.Dimension(102, 18));
        panNonPrizedChit.setPreferredSize(new java.awt.Dimension(102, 18));
        panNonPrizedChit.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoNonPrizedChit_yes);
        rdoNonPrizedChit_yes.setText("Yes");
        rdoNonPrizedChit_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoNonPrizedChit_yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoNonPrizedChit_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoNonPrizedChit_yes.setPreferredSize(new java.awt.Dimension(53, 21));
        rdoNonPrizedChit_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNonPrizedChit_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panNonPrizedChit.add(rdoNonPrizedChit_yes, gridBagConstraints);

        rdgDefaulters.add(rdoNonPrizedChit_no);
        rdoNonPrizedChit_no.setText("No");
        rdoNonPrizedChit_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoNonPrizedChit_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoNonPrizedChit_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoNonPrizedChit_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panNonPrizedChit.add(rdoNonPrizedChit_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panGeneralDetails.add(panNonPrizedChit, gridBagConstraints);

        panShortfall.setMinimumSize(new java.awt.Dimension(102, 18));
        panShortfall.setPreferredSize(new java.awt.Dimension(102, 18));
        panShortfall.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoShortfall_yes);
        rdoShortfall_yes.setText("Yes");
        rdoShortfall_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoShortfall_yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoShortfall_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoShortfall_yes.setPreferredSize(new java.awt.Dimension(53, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panShortfall.add(rdoShortfall_yes, gridBagConstraints);

        rdgDefaulters.add(rdoShortfall_no);
        rdoShortfall_no.setText("No");
        rdoShortfall_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoShortfall_no.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panShortfall.add(rdoShortfall_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(14, 11, 0, 0);
        panGeneralDetails.add(panShortfall, gridBagConstraints);

        lblNonPrizedChit.setText("Whether a non-prized chit owner can be changed ?");
        lblNonPrizedChit.setToolTipText("Whether a non-prized chit owner can be changed ?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 68, 0, 0);
        panGeneralDetails.add(lblNonPrizedChit, gridBagConstraints);

        lblChitDefaults.setText("If chit owners default in making payment of any");
        lblChitDefaults.setToolTipText("If chit owners default in making payment of any");
        lblChitDefaults.setMaximumSize(new java.awt.Dimension(280, 18));
        lblChitDefaults.setMinimumSize(new java.awt.Dimension(280, 18));
        lblChitDefaults.setName("");
        lblChitDefaults.setPreferredSize(new java.awt.Dimension(280, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 32, 0, 0);
        panGeneralDetails.add(lblChitDefaults, gridBagConstraints);

        lblShortfall.setText("If there is a shortfall of members in a chit whether");
        lblShortfall.setToolTipText("If there is a shortfall of members in a chit whether");
        lblShortfall.setMaximumSize(new java.awt.Dimension(330, 18));
        lblShortfall.setMinimumSize(new java.awt.Dimension(330, 18));
        lblShortfall.setPreferredSize(new java.awt.Dimension(330, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 30, 0, 0);
        panGeneralDetails.add(lblShortfall, gridBagConstraints);

        lblSuretyRequired.setText("Surety Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 269, 0, 0);
        panGeneralDetails.add(lblSuretyRequired, gridBagConstraints);

        panPrintServicesGR5.setMinimumSize(new java.awt.Dimension(102, 18));
        panPrintServicesGR5.setPreferredSize(new java.awt.Dimension(102, 18));
        panPrintServicesGR5.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoChitDefaults_yes);
        rdoChitDefaults_yes.setText("Yes");
        rdoChitDefaults_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoChitDefaults_yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoChitDefaults_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoChitDefaults_yes.setPreferredSize(new java.awt.Dimension(53, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR5.add(rdoChitDefaults_yes, gridBagConstraints);

        rdgDefaulters.add(rdoChitDefaults_no);
        rdoChitDefaults_no.setText("No");
        rdoChitDefaults_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoChitDefaults_no.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR5.add(rdoChitDefaults_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 5, 0, 0);
        panGeneralDetails.add(panPrintServicesGR5, gridBagConstraints);

        panSuretyRequired.setMinimumSize(new java.awt.Dimension(102, 18));
        panSuretyRequired.setPreferredSize(new java.awt.Dimension(102, 18));
        panSuretyRequired.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoSuretyRequired_yes);
        rdoSuretyRequired_yes.setText("Yes");
        rdoSuretyRequired_yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoSuretyRequired_yes.setMinimumSize(new java.awt.Dimension(48, 21));
        rdoSuretyRequired_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoSuretyRequired_yes.setPreferredSize(new java.awt.Dimension(48, 21));
        rdoSuretyRequired_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSuretyRequired_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSuretyRequired.add(rdoSuretyRequired_yes, gridBagConstraints);

        rdgDefaulters.add(rdoSuretyRequired_no);
        rdoSuretyRequired_no.setText("No");
        rdoSuretyRequired_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoSuretyRequired_no.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoSuretyRequired_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSuretyRequired_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSuretyRequired.add(rdoSuretyRequired_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 7, 0, 0);
        panGeneralDetails.add(panSuretyRequired, gridBagConstraints);

        lblChitDefaults1.setText("the Bank will advance for the balance no.of members ?");
        lblChitDefaults1.setToolTipText("the Bank will advance for the balance no.of members ?");
        lblChitDefaults1.setMaximumSize(new java.awt.Dimension(350, 15));
        lblChitDefaults1.setMinimumSize(new java.awt.Dimension(350, 15));
        lblChitDefaults1.setPreferredSize(new java.awt.Dimension(350, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panGeneralDetails.add(lblChitDefaults1, gridBagConstraints);

        lblNonPrizedChit1.setText("installments, whether the Bank will advance the deficit ?");
        lblNonPrizedChit1.setToolTipText("installments, whether the Bank will advance the deficit ?");
        lblNonPrizedChit1.setMaximumSize(new java.awt.Dimension(360, 18));
        lblNonPrizedChit1.setMinimumSize(new java.awt.Dimension(360, 18));
        lblNonPrizedChit1.setPreferredSize(new java.awt.Dimension(360, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panGeneralDetails.add(lblNonPrizedChit1, gridBagConstraints);

        panAcceptableType.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAcceptableType.setMinimumSize(new java.awt.Dimension(450, 35));
        panAcceptableType.setPreferredSize(new java.awt.Dimension(450, 35));
        panAcceptableType.setLayout(new java.awt.GridBagLayout());

        lblAcceptableType5.setText("All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptableType.add(lblAcceptableType5, gridBagConstraints);

        chkAcceptableClassAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptableClassAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptableType.add(chkAcceptableClassAll, gridBagConstraints);

        chkAcceptableClassC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptableClassCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptableType.add(chkAcceptableClassC, gridBagConstraints);

        chkAcceptableClassB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptableClassBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptableType.add(chkAcceptableClassB, gridBagConstraints);

        chkAcceptableClassA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptableClassAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptableType.add(chkAcceptableClassA, gridBagConstraints);

        lblAcceptableType7.setText("Class B");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptableType.add(lblAcceptableType7, gridBagConstraints);

        lblAcceptableType8.setText("Class A");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptableType.add(lblAcceptableType8, gridBagConstraints);

        lblAcceptableType9.setText("Class C");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptableType.add(lblAcceptableType9, gridBagConstraints);

        lblAcceptType1.setText("Acceptable types ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptableType.add(lblAcceptType1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 25, 0, 0);
        panGeneralDetails.add(panAcceptableType, gridBagConstraints);

        panAcceptType.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAcceptType.setMinimumSize(new java.awt.Dimension(450, 35));
        panAcceptType.setPreferredSize(new java.awt.Dimension(450, 35));
        panAcceptType.setLayout(new java.awt.GridBagLayout());

        lblAcceptableType10.setText("All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptType.add(lblAcceptableType10, gridBagConstraints);

        chkAcceptClassAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptClassAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptType.add(chkAcceptClassAll, gridBagConstraints);

        chkAcceptClassC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptClassCActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptType.add(chkAcceptClassC, gridBagConstraints);

        chkAcceptClassB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptClassBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptType.add(chkAcceptClassB, gridBagConstraints);

        chkAcceptClassA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkAcceptClassAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panAcceptType.add(chkAcceptClassA, gridBagConstraints);

        lblAcceptableType12.setText("Class B");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptType.add(lblAcceptableType12, gridBagConstraints);

        lblAcceptableType13.setText("Class A");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptType.add(lblAcceptableType13, gridBagConstraints);

        lblAcceptableType14.setText("Class C");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptType.add(lblAcceptableType14, gridBagConstraints);

        lblAcceptType2.setText("Acceptable types ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAcceptType.add(lblAcceptType2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 25, 0, 0);
        panGeneralDetails.add(panAcceptType, gridBagConstraints);

        lblBonusPayFirstIns.setText("Whether Bonus Payable For First Installment");
        lblBonusPayFirstIns.setToolTipText("Whether Bonus Payable For First Installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 101, 0, 0);
        panGeneralDetails.add(lblBonusPayFirstIns, gridBagConstraints);

        panBonusPayFirstIns.setMinimumSize(new java.awt.Dimension(102, 18));
        panBonusPayFirstIns.setPreferredSize(new java.awt.Dimension(102, 18));
        panBonusPayFirstIns.setLayout(new java.awt.GridBagLayout());

        rdgBonusPayFirstIns.add(rdoBonusPayFirstIns_Yes);
        rdoBonusPayFirstIns_Yes.setText("Yes");
        rdoBonusPayFirstIns_Yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoBonusPayFirstIns_Yes.setMinimumSize(new java.awt.Dimension(53, 18));
        rdoBonusPayFirstIns_Yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusPayFirstIns_Yes.setPreferredSize(new java.awt.Dimension(53, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusPayFirstIns.add(rdoBonusPayFirstIns_Yes, gridBagConstraints);

        rdgBonusPayFirstIns.add(rdoBonusPayFirstIns_No);
        rdoBonusPayFirstIns_No.setText("No");
        rdoBonusPayFirstIns_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoBonusPayFirstIns_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusPayFirstIns.add(rdoBonusPayFirstIns_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panGeneralDetails.add(panBonusPayFirstIns, gridBagConstraints);

        panBonusRecoveredExistingChittal.setMinimumSize(new java.awt.Dimension(102, 18));
        panBonusRecoveredExistingChittal.setPreferredSize(new java.awt.Dimension(102, 18));
        panBonusRecoveredExistingChittal.setLayout(new java.awt.GridBagLayout());

        rdgBonusRecoveredExistingChittal.add(rdoBonusRecoveredExistingChittal_yes);
        rdoBonusRecoveredExistingChittal_yes.setText("Yes");
        rdoBonusRecoveredExistingChittal_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoBonusRecoveredExistingChittal_yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoBonusRecoveredExistingChittal_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusRecoveredExistingChittal_yes.setPreferredSize(new java.awt.Dimension(53, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusRecoveredExistingChittal.add(rdoBonusRecoveredExistingChittal_yes, gridBagConstraints);

        rdgBonusRecoveredExistingChittal.add(rdoBonusRecoveredExistingChittal_no);
        rdoBonusRecoveredExistingChittal_no.setText("No");
        rdoBonusRecoveredExistingChittal_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoBonusRecoveredExistingChittal_no.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusRecoveredExistingChittal.add(rdoBonusRecoveredExistingChittal_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        panGeneralDetails.add(panBonusRecoveredExistingChittal, gridBagConstraints);

        lblBonusRecoveredExistingChittal.setText("Whether Bonus to be Recovered From the Existing Chittal");
        lblBonusRecoveredExistingChittal.setToolTipText("Whether Bonus to be Recovered From the Existing Chittal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 27, 0, 0);
        panGeneralDetails.add(lblBonusRecoveredExistingChittal, gridBagConstraints);

        panProductDetails.setMinimumSize(new java.awt.Dimension(435, 75));
        panProductDetails.setPreferredSize(new java.awt.Dimension(435, 75));
        panProductDetails.setRequestFocusEnabled(false);
        panProductDetails.setLayout(new java.awt.GridBagLayout());

        lblProductDesc.setText("Product Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(lblProductDesc, gridBagConstraints);

        lblProductId.setText("Product Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(lblProductId, gridBagConstraints);

        txtProductId.setAllowAll(true);
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
        panProductDetails.add(txtProductId, gridBagConstraints);

        lblProductDescVal.setAllowAll(true);
        lblProductDescVal.setMinimumSize(new java.awt.Dimension(125, 21));
        lblProductDescVal.setPreferredSize(new java.awt.Dimension(125, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProductDetails.add(lblProductDescVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 42, 0, 23);
        panGeneralDetails.add(panProductDetails, gridBagConstraints);

        panPrintServicesGR1.setMinimumSize(new java.awt.Dimension(102, 18));
        panPrintServicesGR1.setPreferredSize(new java.awt.Dimension(102, 18));
        panPrintServicesGR1.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoOnlyMembers_yes);
        rdoOnlyMembers_yes.setText("Yes");
        rdoOnlyMembers_yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoOnlyMembers_yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoOnlyMembers_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoOnlyMembers_yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoOnlyMembers_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOnlyMembers_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR1.add(rdoOnlyMembers_yes, gridBagConstraints);

        rdgDefaulters.add(rdoOnlyMembers_no);
        rdoOnlyMembers_no.setText("No");
        rdoOnlyMembers_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoOnlyMembers_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoOnlyMembers_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoOnlyMembers_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR1.add(rdoOnlyMembers_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 7, 0, 0);
        panGeneralDetails.add(panPrintServicesGR1, gridBagConstraints);

        lblOnlyMembers.setText("Only Members");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 277, 0, 0);
        panGeneralDetails.add(lblOnlyMembers, gridBagConstraints);

        panTransFirstIns.setMinimumSize(new java.awt.Dimension(102, 18));
        panTransFirstIns.setPreferredSize(new java.awt.Dimension(102, 18));
        panTransFirstIns.setLayout(new java.awt.GridBagLayout());

        rdgTransFirstIns.add(rdoTransFirstIns_Yes);
        rdoTransFirstIns_Yes.setText("Yes");
        rdoTransFirstIns_Yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoTransFirstIns_Yes.setMinimumSize(new java.awt.Dimension(53, 18));
        rdoTransFirstIns_Yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoTransFirstIns_Yes.setPreferredSize(new java.awt.Dimension(53, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTransFirstIns.add(rdoTransFirstIns_Yes, gridBagConstraints);

        rdgTransFirstIns.add(rdoTransFirstIns_No);
        rdoTransFirstIns_No.setText("No");
        rdoTransFirstIns_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoTransFirstIns_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panTransFirstIns.add(rdoTransFirstIns_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 3, 0);
        panGeneralDetails.add(panTransFirstIns, gridBagConstraints);

        lblTransFirstIns.setText("Whether Transaction Required For First Installment");
        lblTransFirstIns.setToolTipText("Whether Transaction Required For First Installment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 67, 3, 0);
        panGeneralDetails.add(lblTransFirstIns, gridBagConstraints);

        panGeneral.add(panGeneralDetails, new java.awt.GridBagConstraints());

        panGeneralDetailsEntry.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panGeneralDetailsEntry.setMaximumSize(new java.awt.Dimension(1000, 125));
        panGeneralDetailsEntry.setMinimumSize(new java.awt.Dimension(1000, 125));
        panGeneralDetailsEntry.setOpaque(false);
        panGeneralDetailsEntry.setPreferredSize(new java.awt.Dimension(1000, 125));
        panGeneralDetailsEntry.setLayout(new java.awt.GridBagLayout());

        panBonusAllowed.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoBonusAllowed_yes);
        rdoBonusAllowed_yes.setText("Yes");
        rdoBonusAllowed_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoBonusAllowed_yes.setMinimumSize(new java.awt.Dimension(53, 18));
        rdoBonusAllowed_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusAllowed_yes.setPreferredSize(new java.awt.Dimension(53, 18));
        rdoBonusAllowed_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusAllowed_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusAllowed.add(rdoBonusAllowed_yes, gridBagConstraints);

        rdgDefaulters.add(rdoBonusAllowed_no);
        rdoBonusAllowed_no.setText("No");
        rdoBonusAllowed_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoBonusAllowed_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoBonusAllowed_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusAllowed_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusAllowed.add(rdoBonusAllowed_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 2, 0, 0);
        panGeneralDetailsEntry.add(panBonusAllowed, gridBagConstraints);

        panPrintServicesGR35.setMinimumSize(new java.awt.Dimension(0, 0));
        panPrintServicesGR35.setPreferredSize(new java.awt.Dimension(0, 0));
        panPrintServicesGR35.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoPrizedChitOwner_yes);
        rdoPrizedChitOwner_yes.setText("Yes");
        rdoPrizedChitOwner_yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoPrizedChitOwner_yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoPrizedChitOwner_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoPrizedChitOwner_yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoPrizedChitOwner_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPrizedChitOwner_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR35.add(rdoPrizedChitOwner_yes, gridBagConstraints);

        rdgDefaulters.add(rdoPrizedChitOwner_no);
        rdoPrizedChitOwner_no.setText("No");
        rdoPrizedChitOwner_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoPrizedChitOwner_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoPrizedChitOwner_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPrizedChitOwner_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR35.add(rdoPrizedChitOwner_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panGeneralDetailsEntry.add(panPrintServicesGR35, gridBagConstraints);

        lblBonusChitOwner.setText("Bonus allowed for prized defaulters");
        lblBonusChitOwner.setToolTipText("Bonus allowed for prized defaulters");
        lblBonusChitOwner.setMaximumSize(new java.awt.Dimension(250, 18));
        lblBonusChitOwner.setMinimumSize(new java.awt.Dimension(250, 18));
        lblBonusChitOwner.setPreferredSize(new java.awt.Dimension(250, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 9, 0, 0);
        panGeneralDetailsEntry.add(lblBonusChitOwner, gridBagConstraints);

        lblBonusAllowed.setText("Bonus Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 205, 0, 0);
        panGeneralDetailsEntry.add(lblBonusAllowed, gridBagConstraints);

        lblHolidayInst.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblHolidayInst.setText("If installment Date is a holiday, installment to be collected ");
        lblHolidayInst.setToolTipText("If installment Date is a holiday, installment to be collected ");
        lblHolidayInst.setMaximumSize(new java.awt.Dimension(350, 18));
        lblHolidayInst.setMinimumSize(new java.awt.Dimension(350, 18));
        lblHolidayInst.setName("");
        lblHolidayInst.setPreferredSize(new java.awt.Dimension(350, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 11, 0, 0);
        panGeneralDetailsEntry.add(lblHolidayInst, gridBagConstraints);

        panPrintServicesGR36.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoPrizedChitOwnerAfter_yes);
        rdoPrizedChitOwnerAfter_yes.setText("Yes");
        rdoPrizedChitOwnerAfter_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoPrizedChitOwnerAfter_yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoPrizedChitOwnerAfter_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoPrizedChitOwnerAfter_yes.setPreferredSize(new java.awt.Dimension(53, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR36.add(rdoPrizedChitOwnerAfter_yes, gridBagConstraints);

        rdgDefaulters.add(rdoPrizedChitOwnerAfter_no);
        rdoPrizedChitOwnerAfter_no.setText("No");
        rdoPrizedChitOwnerAfter_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoPrizedChitOwnerAfter_no.setPreferredSize(new java.awt.Dimension(46, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR36.add(rdoPrizedChitOwnerAfter_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(24, 2, 0, 4);
        panGeneralDetailsEntry.add(panPrintServicesGR36, gridBagConstraints);

        panHolidayInst.setMinimumSize(new java.awt.Dimension(102, 18));
        panHolidayInst.setPreferredSize(new java.awt.Dimension(102, 18));
        panHolidayInst.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoprizedDefaulters_yes);
        rdoprizedDefaulters_yes.setText("Yes");
        rdoprizedDefaulters_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoprizedDefaulters_yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoprizedDefaulters_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoprizedDefaulters_yes.setPreferredSize(new java.awt.Dimension(53, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panHolidayInst.add(rdoprizedDefaulters_yes, gridBagConstraints);

        rdgDefaulters.add(rdoprizedDefaulters_no);
        rdoprizedDefaulters_no.setText("No");
        rdoprizedDefaulters_no.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoprizedDefaulters_no.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panHolidayInst.add(rdoprizedDefaulters_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 18, 0, 0);
        panGeneralDetailsEntry.add(panHolidayInst, gridBagConstraints);

        lblBonusChitOwner1.setText("Whether Prized Money Holder Eligible For Bonus");
        lblBonusChitOwner1.setToolTipText("Whether Prized Money Holder Eligible For Bonus");
        lblBonusChitOwner1.setMaximumSize(new java.awt.Dimension(298, 18));
        lblBonusChitOwner1.setMinimumSize(new java.awt.Dimension(298, 18));
        lblBonusChitOwner1.setPreferredSize(new java.awt.Dimension(298, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, -4, 0, 0);
        panGeneralDetailsEntry.add(lblBonusChitOwner1, gridBagConstraints);

        lblBonusChitOwner2.setText("After Cash Payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 175, 0, 0);
        panGeneralDetailsEntry.add(lblBonusChitOwner2, gridBagConstraints);

        lblBonusChitOwner3.setText("Prized chit owner is eligible for bonus after cash payment");
        lblBonusChitOwner3.setToolTipText("Prized chit owner is eligible for bonus after cash payment");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(23, 28, 0, 0);
        panGeneralDetailsEntry.add(lblBonusChitOwner3, gridBagConstraints);

        panPrintServicesGR37.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdobonusPayableOwner_yes);
        rdobonusPayableOwner_yes.setText("Yes");
        rdobonusPayableOwner_yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdobonusPayableOwner_yes.setMinimumSize(new java.awt.Dimension(53, 18));
        rdobonusPayableOwner_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdobonusPayableOwner_yes.setPreferredSize(new java.awt.Dimension(53, 18));
        rdobonusPayableOwner_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdobonusPayableOwner_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR37.add(rdobonusPayableOwner_yes, gridBagConstraints);

        rdgDefaulters.add(rdobonusPayableOwner_no);
        rdobonusPayableOwner_no.setText("No");
        rdobonusPayableOwner_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdobonusPayableOwner_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdobonusPayableOwner_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdobonusPayableOwner_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panPrintServicesGR37.add(rdobonusPayableOwner_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 3, 0, 0);
        panGeneralDetailsEntry.add(panPrintServicesGR37, gridBagConstraints);

        cboholidayInt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 17;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 12, 0, 0);
        panGeneralDetailsEntry.add(cboholidayInt, gridBagConstraints);

        chkFromAuctionEnrtry.setText("From the time of Auction Entry");
        chkFromAuctionEnrtry.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkFromAuctionEnrtryItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 47, 10, 0);
        panGeneralDetailsEntry.add(chkFromAuctionEnrtry, gridBagConstraints);

        chkAfterCashPayment.setText("After cash payment");
        chkAfterCashPayment.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkAfterCashPaymentItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 1, 10, 0);
        panGeneralDetailsEntry.add(chkAfterCashPayment, gridBagConstraints);

        chkSplitMDSTransaction.setText("Split MDS Transaction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        panGeneralDetailsEntry.add(chkSplitMDSTransaction, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 13);
        panGeneral.add(panGeneralDetailsEntry, gridBagConstraints);

        panInsideGeneralDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panInsideGeneralDetails.setMinimumSize(new java.awt.Dimension(475, 400));
        panInsideGeneralDetails.setPreferredSize(new java.awt.Dimension(475, 400));
        panInsideGeneralDetails.setLayout(new java.awt.GridBagLayout());

        panSecurityAgainst.setMinimumSize(new java.awt.Dimension(215, 18));
        panSecurityAgainst.setPreferredSize(new java.awt.Dimension(215, 18));

        rdoGroupSecurity.add(rdoPending);
        rdoPending.setText("Pending Instmt");
        rdoPending.setToolTipText("Pending Instmt");
        rdoPending.setMaximumSize(new java.awt.Dimension(110, 27));
        rdoPending.setMinimumSize(new java.awt.Dimension(110, 27));
        rdoPending.setPreferredSize(new java.awt.Dimension(125, 27));
        rdoPending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPendingActionPerformed(evt);
            }
        });

        rdoGroupSecurity.add(rdoPrized);
        rdoPrized.setText("Prized Amt");
        rdoPrized.setToolTipText("Prized Amt");
        rdoPrized.setMaximumSize(new java.awt.Dimension(97, 27));
        rdoPrized.setMinimumSize(new java.awt.Dimension(97, 27));
        rdoPrized.setPreferredSize(new java.awt.Dimension(97, 27));

        rdoGroupSecurity.add(rdoWhichEverIsLess);
        rdoWhichEverIsLess.setText("Whichever is less");
        rdoWhichEverIsLess.setToolTipText("Whichever is less");
        rdoWhichEverIsLess.setMaximumSize(new java.awt.Dimension(125, 27));
        rdoWhichEverIsLess.setMinimumSize(new java.awt.Dimension(125, 27));
        rdoWhichEverIsLess.setPreferredSize(new java.awt.Dimension(125, 27));
        rdoWhichEverIsLess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoWhichEverIsLessActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panSecurityAgainstLayout = new javax.swing.GroupLayout(panSecurityAgainst);
        panSecurityAgainst.setLayout(panSecurityAgainstLayout);
        panSecurityAgainstLayout.setHorizontalGroup(
            panSecurityAgainstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityAgainstLayout.createSequentialGroup()
                .addComponent(rdoPending, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdoPrized, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdoWhichEverIsLess, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                .addGap(4, 4, 4))
        );
        panSecurityAgainstLayout.setVerticalGroup(
            panSecurityAgainstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSecurityAgainstLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(rdoPrized, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(rdoPending, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(rdoWhichEverIsLess, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 75;
        gridBagConstraints.ipadx = 149;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(117, 3, 0, 2);
        panInsideGeneralDetails.add(panSecurityAgainst, gridBagConstraints);

        panBonusRounding.setMaximumSize(new java.awt.Dimension(125, 18));
        panBonusRounding.setMinimumSize(new java.awt.Dimension(125, 18));
        panBonusRounding.setPreferredSize(new java.awt.Dimension(125, 18));
        panBonusRounding.setLayout(new java.awt.GridBagLayout());

        rdgBonusRounding.add(rdoBonusAmt_Yes);
        rdoBonusAmt_Yes.setText("Yes");
        rdoBonusAmt_Yes.setToolTipText("Yes");
        rdoBonusAmt_Yes.setMaximumSize(new java.awt.Dimension(51, 21));
        rdoBonusAmt_Yes.setMinimumSize(new java.awt.Dimension(51, 21));
        rdoBonusAmt_Yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusAmt_Yes.setPreferredSize(new java.awt.Dimension(51, 21));
        rdoBonusAmt_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusAmt_YesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusRounding.add(rdoBonusAmt_Yes, gridBagConstraints);

        rdgBonusRounding.add(rdoBonusAmt_No);
        rdoBonusAmt_No.setText("No");
        rdoBonusAmt_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoBonusAmt_No.setPreferredSize(new java.awt.Dimension(46, 21));
        rdoBonusAmt_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusAmt_NoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panBonusRounding.add(rdoBonusAmt_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        panInsideGeneralDetails.add(panBonusRounding, gridBagConstraints);

        lblBonusAmountRounding.setText("Bonus Amount Rounding");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 31, 0, 0);
        panInsideGeneralDetails.add(lblBonusAmountRounding, gridBagConstraints);

        panAdvanceCollection.setMinimumSize(new java.awt.Dimension(102, 18));
        panAdvanceCollection.setPreferredSize(new java.awt.Dimension(102, 18));
        panAdvanceCollection.setLayout(new java.awt.GridBagLayout());

        rdgAdvanceCollection.add(rdoAdvanceCollection_Yes);
        rdoAdvanceCollection_Yes.setText("Yes");
        rdoAdvanceCollection_Yes.setMaximumSize(new java.awt.Dimension(53, 21));
        rdoAdvanceCollection_Yes.setMinimumSize(new java.awt.Dimension(53, 21));
        rdoAdvanceCollection_Yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoAdvanceCollection_Yes.setPreferredSize(new java.awt.Dimension(53, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAdvanceCollection.add(rdoAdvanceCollection_Yes, gridBagConstraints);

        rdgAdvanceCollection.add(rdoAdvanceCollection_No);
        rdoAdvanceCollection_No.setText("No");
        rdoAdvanceCollection_No.setMinimumSize(new java.awt.Dimension(46, 21));
        rdoAdvanceCollection_No.setPreferredSize(new java.awt.Dimension(46, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panAdvanceCollection.add(rdoAdvanceCollection_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 17;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        panInsideGeneralDetails.add(panAdvanceCollection, gridBagConstraints);

        lblAdvanceCollection.setText("Advance Collection");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 51, 0, 0);
        panInsideGeneralDetails.add(lblAdvanceCollection, gridBagConstraints);

        panAuctionMaxAmt.setMinimumSize(new java.awt.Dimension(75, 25));
        panAuctionMaxAmt.setPreferredSize(new java.awt.Dimension(75, 25));
        panAuctionMaxAmt.setLayout(new java.awt.GridBagLayout());

        txtAuctionMaxAmt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAuctionMaxAmt.setPreferredSize(new java.awt.Dimension(50, 21));
        txtAuctionMaxAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuctionMaxAmtActionPerformed(evt);
            }
        });
        txtAuctionMaxAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAuctionMaxAmtFocusLost(evt);
            }
        });
        panAuctionMaxAmt.add(txtAuctionMaxAmt, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 18;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.ipadx = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 9, 0, 0);
        panInsideGeneralDetails.add(panAuctionMaxAmt, gridBagConstraints);

        panAuctionMinAmt.setMinimumSize(new java.awt.Dimension(75, 25));
        panAuctionMinAmt.setPreferredSize(new java.awt.Dimension(75, 25));
        panAuctionMinAmt.setLayout(new java.awt.GridBagLayout());

        txtAuctionMinAmt.setMinimumSize(new java.awt.Dimension(50, 21));
        txtAuctionMinAmt.setPreferredSize(new java.awt.Dimension(50, 21));
        txtAuctionMinAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAuctionMinAmtActionPerformed(evt);
            }
        });
        txtAuctionMinAmt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAuctionMinAmtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panAuctionMinAmt.add(txtAuctionMinAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 21;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 18;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = -20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 9, 0, 0);
        panInsideGeneralDetails.add(panAuctionMinAmt, gridBagConstraints);

        lblAuctionMaxAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAuctionMaxAmt.setText("In case of Auction, maximum bid amount");
        lblAuctionMaxAmt.setToolTipText("In case of Auction, maximum bid amount");
        lblAuctionMaxAmt.setMaximumSize(new java.awt.Dimension(280, 18));
        lblAuctionMaxAmt.setMinimumSize(new java.awt.Dimension(280, 18));
        lblAuctionMaxAmt.setName("");
        lblAuctionMaxAmt.setPreferredSize(new java.awt.Dimension(280, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(13, 0, 0, 0);
        panInsideGeneralDetails.add(lblAuctionMaxAmt, gridBagConstraints);

        lblAuctionMinAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAuctionMinAmt.setText("In case of Auction, minimum bid amount");
        lblAuctionMinAmt.setToolTipText("In case of Auction, minimum bid amount");
        lblAuctionMinAmt.setMaximumSize(new java.awt.Dimension(280, 18));
        lblAuctionMinAmt.setMinimumSize(new java.awt.Dimension(280, 18));
        lblAuctionMinAmt.setPreferredSize(new java.awt.Dimension(280, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 10;
        gridBagConstraints.ipadx = -9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        panInsideGeneralDetails.add(lblAuctionMinAmt, gridBagConstraints);

        cboRoundOffCriteria.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRoundOffCriteria.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 0);
        panInsideGeneralDetails.add(cboRoundOffCriteria, gridBagConstraints);

        lblSecurityAgainst.setText("Security Against");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(117, 8, 0, 0);
        panInsideGeneralDetails.add(lblSecurityAgainst, gridBagConstraints);

        cLabel1.setText("Commencement Method");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 14, 0, 0);
        panInsideGeneralDetails.add(cLabel1, gridBagConstraints);

        panCommencmnt.setMinimumSize(new java.awt.Dimension(160, 18));
        panCommencmnt.setPreferredSize(new java.awt.Dimension(160, 18));
        panCommencmnt.setLayout(new java.awt.GridBagLayout());

        commencmtGrp.add(rdoMethod2);
        rdoMethod2.setText("Method2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        panCommencmnt.add(rdoMethod2, gridBagConstraints);

        commencmtGrp.add(rdoMethod1);
        rdoMethod1.setText("Method 1");
        rdoMethod1.setMaximumSize(new java.awt.Dimension(90, 27));
        rdoMethod1.setMinimumSize(new java.awt.Dimension(90, 27));
        rdoMethod1.setPreferredSize(new java.awt.Dimension(90, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panCommencmnt.add(rdoMethod1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 36;
        gridBagConstraints.ipadx = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        panInsideGeneralDetails.add(panCommencmnt, gridBagConstraints);

        chkIsGDS.setText("Is GDS");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 35, 0, 0);
        panInsideGeneralDetails.add(chkIsGDS, gridBagConstraints);

        chkIsBonusTrans.setSelected(true);
        chkIsBonusTrans.setText("Bonus Transaction Required");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 35, 34, 0);
        panInsideGeneralDetails.add(chkIsBonusTrans, gridBagConstraints);

        lblAuctionMaxAmtVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAuctionMaxAmtVal.setText("%");
        lblAuctionMaxAmtVal.setMinimumSize(new java.awt.Dimension(15, 18));
        lblAuctionMaxAmtVal.setPreferredSize(new java.awt.Dimension(15, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 39;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        panInsideGeneralDetails.add(lblAuctionMaxAmtVal, gridBagConstraints);

        lblAuctionMinAmtVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAuctionMinAmtVal.setText("%");
        lblAuctionMinAmtVal.setMinimumSize(new java.awt.Dimension(15, 18));
        lblAuctionMinAmtVal.setPreferredSize(new java.awt.Dimension(15, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 39;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 12, 0, 0);
        panInsideGeneralDetails.add(lblAuctionMinAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGeneral.add(panInsideGeneralDetails, gridBagConstraints);

        tabMDSProduct.addTab("General", panGeneral);

        panOtherDetails.setMinimumSize(new java.awt.Dimension(790, 700));
        panOtherDetails.setPreferredSize(new java.awt.Dimension(790, 700));
        panOtherDetails.setLayout(new java.awt.GridBagLayout());

        panMDSLoan.setBorder(javax.swing.BorderFactory.createTitledBorder("Loan against MDS"));
        panMDSLoan.setMinimumSize(new java.awt.Dimension(775, 115));
        panMDSLoan.setPreferredSize(new java.awt.Dimension(775, 115));
        panMDSLoan.setLayout(new java.awt.GridBagLayout());

        lblMaxLoanAmt.setText("Maximum Loan amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMDSLoan.add(lblMaxLoanAmt, gridBagConstraints);

        lblMinLoanAmt.setText("Minimum Loan amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSLoan.add(lblMinLoanAmt, gridBagConstraints);

        txtMaxLoanAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMDSLoan.add(txtMaxLoanAmt, gridBagConstraints);

        txtMinLoanAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSLoan.add(txtMinLoanAmt, gridBagConstraints);

        lblLoanCanbeGranted.setText("Whether loan can be granted under this scheme ?");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMDSLoan.add(lblLoanCanbeGranted, gridBagConstraints);

        panLoanCanbeGranted.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoLoanCanbeGranted_yes);
        rdoLoanCanbeGranted_yes.setText("Yes");
        rdoLoanCanbeGranted_yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoLoanCanbeGranted_yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoLoanCanbeGranted_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoLoanCanbeGranted_yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoLoanCanbeGranted_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLoanCanbeGranted_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panLoanCanbeGranted.add(rdoLoanCanbeGranted_yes, gridBagConstraints);

        rdgDefaulters.add(rdoLoanCanbeGranted_no);
        rdoLoanCanbeGranted_no.setText("No");
        rdoLoanCanbeGranted_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoLoanCanbeGranted_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoLoanCanbeGranted_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoLoanCanbeGranted_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panLoanCanbeGranted.add(rdoLoanCanbeGranted_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMDSLoan.add(panLoanCanbeGranted, gridBagConstraints);

        lblMargin.setText("If yes,  the Margin  to be Maintained");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMDSLoan.add(lblMargin, gridBagConstraints);

        panMargin.setMinimumSize(new java.awt.Dimension(75, 25));
        panMargin.setPreferredSize(new java.awt.Dimension(75, 25));
        panMargin.setLayout(new java.awt.GridBagLayout());

        txtMargin.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMargin.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMargin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMarginActionPerformed(evt);
            }
        });
        txtMargin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMarginFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panMargin.add(txtMargin, gridBagConstraints);

        lblMarginVal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblMarginVal.setText("%");
        lblMarginVal.setMinimumSize(new java.awt.Dimension(15, 18));
        lblMarginVal.setPreferredSize(new java.awt.Dimension(15, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMargin.add(lblMarginVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panMDSLoan.add(panMargin, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panOtherDetails.add(panMDSLoan, gridBagConstraints);

        panDiscountPeriodDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Discount Period Details"));
        panDiscountPeriodDetails.setMinimumSize(new java.awt.Dimension(775, 90));
        panDiscountPeriodDetails.setPreferredSize(new java.awt.Dimension(775, 90));
        panDiscountPeriodDetails.setLayout(new java.awt.GridBagLayout());

        lblDiscountPrizedPeriod.setText("Discount prized grace period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panDiscountPeriodDetails.add(lblDiscountPrizedPeriod, gridBagConstraints);

        lblDiscountPeriod.setText("Discount grace period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panDiscountPeriodDetails.add(lblDiscountPeriod, gridBagConstraints);

        lblDiscountAllowed.setText("Discount allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panDiscountPeriodDetails.add(lblDiscountAllowed, gridBagConstraints);

        panDiscountAllowed.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoDiscountAllowed_yes);
        rdoDiscountAllowed_yes.setText("Yes");
        rdoDiscountAllowed_yes.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoDiscountAllowed_yes.setMinimumSize(new java.awt.Dimension(48, 18));
        rdoDiscountAllowed_yes.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoDiscountAllowed_yes.setPreferredSize(new java.awt.Dimension(48, 18));
        rdoDiscountAllowed_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountAllowed_yesActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDiscountAllowed.add(rdoDiscountAllowed_yes, gridBagConstraints);

        rdgDefaulters.add(rdoDiscountAllowed_no);
        rdoDiscountAllowed_no.setText("No");
        rdoDiscountAllowed_no.setMinimumSize(new java.awt.Dimension(46, 18));
        rdoDiscountAllowed_no.setPreferredSize(new java.awt.Dimension(46, 18));
        rdoDiscountAllowed_no.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountAllowed_noActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panDiscountAllowed.add(rdoDiscountAllowed_no, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panDiscountPeriodDetails.add(panDiscountAllowed, gridBagConstraints);

        panDiscountPrizedPeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDiscountPrizedPeriod.setMinimumSize(new java.awt.Dimension(460, 25));
        panDiscountPrizedPeriod.setPreferredSize(new java.awt.Dimension(460, 25));
        panDiscountPrizedPeriod.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoDiscountPrizedPeriodEnd);
        rdoDiscountPrizedPeriodEnd.setText("Month end");
        rdoDiscountPrizedPeriodEnd.setMaximumSize(new java.awt.Dimension(148, 21));
        rdoDiscountPrizedPeriodEnd.setMinimumSize(new java.awt.Dimension(88, 21));
        rdoDiscountPrizedPeriodEnd.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoDiscountPrizedPeriodEnd.setPreferredSize(new java.awt.Dimension(88, 21));
        rdoDiscountPrizedPeriodEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPrizedPeriodEndActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 1, 0);
        panDiscountPrizedPeriod.add(rdoDiscountPrizedPeriodEnd, gridBagConstraints);

        rdgDefaulters.add(rdoDiscountPrizedPeriodAfter);
        rdoDiscountPrizedPeriodAfter.setText("On or before");
        rdoDiscountPrizedPeriodAfter.setMaximumSize(new java.awt.Dimension(98, 21));
        rdoDiscountPrizedPeriodAfter.setMinimumSize(new java.awt.Dimension(98, 21));
        rdoDiscountPrizedPeriodAfter.setPreferredSize(new java.awt.Dimension(98, 21));
        rdoDiscountPrizedPeriodAfter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPrizedPeriodAfterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 18, 1, 0);
        panDiscountPrizedPeriod.add(rdoDiscountPrizedPeriodAfter, gridBagConstraints);

        rdgDefaulters.add(rdoDiscountPrizedPeriodDays);
        rdoDiscountPrizedPeriodDays.setText("Days");
        rdoDiscountPrizedPeriodDays.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoDiscountPrizedPeriodDays.setMinimumSize(new java.awt.Dimension(58, 21));
        rdoDiscountPrizedPeriodDays.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoDiscountPrizedPeriodDays.setPreferredSize(new java.awt.Dimension(58, 21));
        rdoDiscountPrizedPeriodDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPrizedPeriodDaysActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panDiscountAllowed3Layout = new javax.swing.GroupLayout(panDiscountAllowed3);
        panDiscountAllowed3.setLayout(panDiscountAllowed3Layout);
        panDiscountAllowed3Layout.setHorizontalGroup(
            panDiscountAllowed3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDiscountAllowed3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rdoDiscountPrizedPeriodDays, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panDiscountAllowed3Layout.setVerticalGroup(
            panDiscountAllowed3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panDiscountAllowed3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(rdoDiscountPrizedPeriodDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 18, 0, 0);
        panDiscountPrizedPeriod.add(panDiscountAllowed3, gridBagConstraints);

        txtDiscountPrizedPeriodEnd.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDiscountPrizedPeriodEnd.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDiscountPrizedPeriodEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountPrizedPeriodEndFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 14);
        panDiscountPrizedPeriod.add(txtDiscountPrizedPeriodEnd, gridBagConstraints);

        rdgDefaulters.add(rdoDiscountPrizedPeriodMonth);
        rdoDiscountPrizedPeriodMonth.setText("Months");
        rdoDiscountPrizedPeriodMonth.setMinimumSize(new java.awt.Dimension(68, 21));
        rdoDiscountPrizedPeriodMonth.setPreferredSize(new java.awt.Dimension(68, 21));
        rdoDiscountPrizedPeriodMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPrizedPeriodMonthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 0);
        panDiscountPrizedPeriod.add(rdoDiscountPrizedPeriodMonth, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panDiscountPeriodDetails.add(panDiscountPrizedPeriod, gridBagConstraints);

        panDiscountPeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDiscountPeriod.setMinimumSize(new java.awt.Dimension(460, 25));
        panDiscountPeriod.setPreferredSize(new java.awt.Dimension(460, 25));
        panDiscountPeriod.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoDiscountPeriodEnd);
        rdoDiscountPeriodEnd.setText("Month end");
        rdoDiscountPeriodEnd.setMaximumSize(new java.awt.Dimension(148, 21));
        rdoDiscountPeriodEnd.setMinimumSize(new java.awt.Dimension(88, 21));
        rdoDiscountPeriodEnd.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoDiscountPeriodEnd.setPreferredSize(new java.awt.Dimension(88, 21));
        rdoDiscountPeriodEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPeriodEndActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 1, 0);
        panDiscountPeriod.add(rdoDiscountPeriodEnd, gridBagConstraints);

        rdgDefaulters.add(rdoDiscountPeriodAfter);
        rdoDiscountPeriodAfter.setText("On or before");
        rdoDiscountPeriodAfter.setMaximumSize(new java.awt.Dimension(98, 21));
        rdoDiscountPeriodAfter.setMinimumSize(new java.awt.Dimension(98, 21));
        rdoDiscountPeriodAfter.setPreferredSize(new java.awt.Dimension(98, 21));
        rdoDiscountPeriodAfter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPeriodAfterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 32, 1, 0);
        panDiscountPeriod.add(rdoDiscountPeriodAfter, gridBagConstraints);

        rdgDefaulters.add(rdoDiscountPeriodDays);
        rdoDiscountPeriodDays.setText("Days");
        rdoDiscountPeriodDays.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoDiscountPeriodDays.setMinimumSize(new java.awt.Dimension(58, 21));
        rdoDiscountPeriodDays.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoDiscountPeriodDays.setPreferredSize(new java.awt.Dimension(58, 21));
        rdoDiscountPeriodDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPeriodDaysActionPerformed(evt);
            }
        });

        rdgDefaulters.add(rdoDiscountPeriodMonth);
        rdoDiscountPeriodMonth.setText("Months");
        rdoDiscountPeriodMonth.setMinimumSize(new java.awt.Dimension(68, 21));
        rdoDiscountPeriodMonth.setPreferredSize(new java.awt.Dimension(68, 21));
        rdoDiscountPeriodMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoDiscountPeriodMonthActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panDiscountAllowed4Layout = new javax.swing.GroupLayout(panDiscountAllowed4);
        panDiscountAllowed4.setLayout(panDiscountAllowed4Layout);
        panDiscountAllowed4Layout.setHorizontalGroup(
            panDiscountAllowed4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDiscountAllowed4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(rdoDiscountPeriodDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rdoDiscountPeriodMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        panDiscountAllowed4Layout.setVerticalGroup(
            panDiscountAllowed4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDiscountAllowed4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(rdoDiscountPeriodMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(rdoDiscountPeriodDays, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 18, 0, 0);
        panDiscountPeriod.add(panDiscountAllowed4, gridBagConstraints);

        txtDiscountPeriodEnd.setMinimumSize(new java.awt.Dimension(50, 21));
        txtDiscountPeriodEnd.setPreferredSize(new java.awt.Dimension(50, 21));
        txtDiscountPeriodEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountPeriodEndFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 19);
        panDiscountPeriod.add(txtDiscountPeriodEnd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panDiscountPeriodDetails.add(panDiscountPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panOtherDetails.add(panDiscountPeriodDetails, gridBagConstraints);

        panGracePeriodDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Grace Period Details"));
        panGracePeriodDetails.setMinimumSize(new java.awt.Dimension(775, 150));
        panGracePeriodDetails.setPreferredSize(new java.awt.Dimension(775, 150));
        panGracePeriodDetails.setLayout(new java.awt.GridBagLayout());

        lblPenalPeriod.setText("Penal grace period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(lblPenalPeriod, gridBagConstraints);

        lblPenalPrizedPeriod.setText("Penal prized grace period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(lblPenalPrizedPeriod, gridBagConstraints);

        lblBonusPrizedPreriod.setText("Bonus prized grace period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(lblBonusPrizedPreriod, gridBagConstraints);

        lblBonusGracePeriod.setText("Bonus grace period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(lblBonusGracePeriod, gridBagConstraints);

        panBonusGracePeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBonusGracePeriod.setMinimumSize(new java.awt.Dimension(460, 25));
        panBonusGracePeriod.setPreferredSize(new java.awt.Dimension(460, 25));
        panBonusGracePeriod.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoBonusPrizedEnd);
        rdoBonusPrizedEnd.setText("Month end");
        rdoBonusPrizedEnd.setMaximumSize(new java.awt.Dimension(148, 21));
        rdoBonusPrizedEnd.setMinimumSize(new java.awt.Dimension(88, 21));
        rdoBonusPrizedEnd.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusPrizedEnd.setPreferredSize(new java.awt.Dimension(88, 21));
        rdoBonusPrizedEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusPrizedEndActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 18, 1, 0);
        panBonusGracePeriod.add(rdoBonusPrizedEnd, gridBagConstraints);

        rdgDefaulters.add(rdoBonusPrizedAfter);
        rdoBonusPrizedAfter.setText("On or before");
        rdoBonusPrizedAfter.setMaximumSize(new java.awt.Dimension(98, 21));
        rdoBonusPrizedAfter.setMinimumSize(new java.awt.Dimension(98, 21));
        rdoBonusPrizedAfter.setPreferredSize(new java.awt.Dimension(98, 21));
        rdoBonusPrizedAfter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusPrizedAfterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 8, 1, 0);
        panBonusGracePeriod.add(rdoBonusPrizedAfter, gridBagConstraints);

        panDiscountAllowed1.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoBonusPrizedDays);
        rdoBonusPrizedDays.setText("Days");
        rdoBonusPrizedDays.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoBonusPrizedDays.setMinimumSize(new java.awt.Dimension(58, 21));
        rdoBonusPrizedDays.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusPrizedDays.setPreferredSize(new java.awt.Dimension(58, 21));
        rdoBonusPrizedDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusPrizedDaysActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        panDiscountAllowed1.add(rdoBonusPrizedDays, gridBagConstraints);

        rdgDefaulters.add(rdoBonusPrizedMonth);
        rdoBonusPrizedMonth.setText("Months");
        rdoBonusPrizedMonth.setMinimumSize(new java.awt.Dimension(68, 21));
        rdoBonusPrizedMonth.setPreferredSize(new java.awt.Dimension(68, 21));
        rdoBonusPrizedMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusPrizedMonthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 8);
        panDiscountAllowed1.add(rdoBonusPrizedMonth, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 0, 0);
        panBonusGracePeriod.add(panDiscountAllowed1, gridBagConstraints);

        txtBonusPrizedPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtBonusPrizedPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtBonusPrizedPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusPrizedPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 3, 0, 12);
        panBonusGracePeriod.add(txtBonusPrizedPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(panBonusGracePeriod, gridBagConstraints);

        panPenalPeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPenalPeriod.setMinimumSize(new java.awt.Dimension(160, 25));
        panPenalPeriod.setPreferredSize(new java.awt.Dimension(160, 25));
        panPenalPeriod.setLayout(new java.awt.GridBagLayout());

        cboPenalPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPenalPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPenalPeriodActionPerformed(evt);
            }
        });
        cboPenalPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPenalPeriodFocusLost(evt);
            }
        });
        cboPenalPeriod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboPenalPeriodMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPenalPeriod.add(cboPenalPeriod, gridBagConstraints);

        txtPenalPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPenalPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtPenalPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPenalPeriod.add(txtPenalPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(panPenalPeriod, gridBagConstraints);

        panPenalPrizedPeriod.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPenalPrizedPeriod.setMinimumSize(new java.awt.Dimension(160, 25));
        panPenalPrizedPeriod.setPreferredSize(new java.awt.Dimension(160, 25));
        panPenalPrizedPeriod.setLayout(new java.awt.GridBagLayout());

        cboPenalPrizedPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPenalPrizedPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPenalPrizedPeriodActionPerformed(evt);
            }
        });
        cboPenalPrizedPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPenalPrizedPeriodFocusLost(evt);
            }
        });
        cboPenalPrizedPeriod.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboPenalPrizedPeriodMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPenalPrizedPeriod.add(cboPenalPrizedPeriod, gridBagConstraints);

        txtPenalPrizedPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPenalPrizedPeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtPenalPrizedPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalPrizedPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPenalPrizedPeriod.add(txtPenalPrizedPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(panPenalPrizedPeriod, gridBagConstraints);

        lblPenalCalc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPenalCalc.setText("Penal calculation based on");
        lblPenalCalc.setMinimumSize(new java.awt.Dimension(210, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(lblPenalCalc, gridBagConstraints);

        panBonusGracePeriod1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panBonusGracePeriod1.setMinimumSize(new java.awt.Dimension(460, 25));
        panBonusGracePeriod1.setPreferredSize(new java.awt.Dimension(460, 25));
        panBonusGracePeriod1.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoBonusGraceEnd);
        rdoBonusGraceEnd.setText("Month end");
        rdoBonusGraceEnd.setMaximumSize(new java.awt.Dimension(148, 21));
        rdoBonusGraceEnd.setMinimumSize(new java.awt.Dimension(88, 21));
        rdoBonusGraceEnd.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusGraceEnd.setPreferredSize(new java.awt.Dimension(88, 21));
        rdoBonusGraceEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusGraceEndActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 1, 0);
        panBonusGracePeriod1.add(rdoBonusGraceEnd, gridBagConstraints);

        rdgDefaulters.add(rdoBonusGraceAfter);
        rdoBonusGraceAfter.setText("On or before");
        rdoBonusGraceAfter.setMaximumSize(new java.awt.Dimension(98, 21));
        rdoBonusGraceAfter.setMinimumSize(new java.awt.Dimension(98, 21));
        rdoBonusGraceAfter.setPreferredSize(new java.awt.Dimension(98, 21));
        rdoBonusGraceAfter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusGraceAfterActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 1, 0);
        panBonusGracePeriod1.add(rdoBonusGraceAfter, gridBagConstraints);

        panDiscountAllowed2.setLayout(new java.awt.GridBagLayout());

        rdgDefaulters.add(rdoBonusGraceDays);
        rdoBonusGraceDays.setText("Days");
        rdoBonusGraceDays.setMaximumSize(new java.awt.Dimension(48, 21));
        rdoBonusGraceDays.setMinimumSize(new java.awt.Dimension(58, 21));
        rdoBonusGraceDays.setNextFocusableComponent(rdoNonPrizedChit_no);
        rdoBonusGraceDays.setPreferredSize(new java.awt.Dimension(58, 21));
        rdoBonusGraceDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusGraceDaysActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 0);
        panDiscountAllowed2.add(rdoBonusGraceDays, gridBagConstraints);

        rdgDefaulters.add(rdoBonusGraceMonth);
        rdoBonusGraceMonth.setText("Months");
        rdoBonusGraceMonth.setMinimumSize(new java.awt.Dimension(68, 21));
        rdoBonusGraceMonth.setPreferredSize(new java.awt.Dimension(68, 21));
        rdoBonusGraceMonth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoBonusGraceMonthActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panDiscountAllowed2.add(rdoBonusGraceMonth, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        panBonusGracePeriod1.add(panDiscountAllowed2, gridBagConstraints);

        txtBonusGracePeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtBonusGracePeriod.setPreferredSize(new java.awt.Dimension(50, 21));
        txtBonusGracePeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtBonusGracePeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 8, 0, 35);
        panBonusGracePeriod1.add(txtBonusGracePeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(panBonusGracePeriod1, gridBagConstraints);

        panPenalCalculation.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPenalCalculation.setMinimumSize(new java.awt.Dimension(105, 25));
        panPenalCalculation.setPreferredSize(new java.awt.Dimension(105, 25));
        panPenalCalculation.setLayout(new java.awt.GridBagLayout());

        cboPenalCalc.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPenalCalc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPenalCalcActionPerformed(evt);
            }
        });
        cboPenalCalc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPenalCalcFocusLost(evt);
            }
        });
        cboPenalCalc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cboPenalCalcMouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPenalCalculation.add(cboPenalCalc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panGracePeriodDetails.add(panPenalCalculation, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panOtherDetails.add(panGracePeriodDetails, gridBagConstraints);

        panRateDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Rate Details"));
        panRateDetails.setMinimumSize(new java.awt.Dimension(775, 160));
        panRateDetails.setPreferredSize(new java.awt.Dimension(775, 160));
        panRateDetails.setLayout(new java.awt.GridBagLayout());

        lblCommisionRate.setText("MDS Commission Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(lblCommisionRate, gridBagConstraints);

        lblDiscountRate.setText("Discount Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(lblDiscountRate, gridBagConstraints);

        lblPenalIntRate.setText("Penal Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(lblPenalIntRate, gridBagConstraints);

        lblPenalPrizedRate.setText("Penal Prized Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(lblPenalPrizedRate, gridBagConstraints);

        lblLoanIntRate.setText("Loan Interest Rate");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(lblLoanIntRate, gridBagConstraints);

        panCommisionRate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panCommisionRate.setMinimumSize(new java.awt.Dimension(245, 25));
        panCommisionRate.setPreferredSize(new java.awt.Dimension(245, 25));
        panCommisionRate.setLayout(new java.awt.GridBagLayout());

        cboCommisionRate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCommisionRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCommisionRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCommisionRate.add(cboCommisionRate, gridBagConstraints);

        txtCommisionRate.setAllowAll(true);
        txtCommisionRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCommisionRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCommisionRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCommisionRate.add(txtCommisionRate, gridBagConstraints);

        lblCommisionRateVal.setText("Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCommisionRate.add(lblCommisionRateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(panCommisionRate, gridBagConstraints);

        panLoanIntRate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLoanIntRate.setMinimumSize(new java.awt.Dimension(245, 25));
        panLoanIntRate.setPreferredSize(new java.awt.Dimension(245, 25));
        panLoanIntRate.setLayout(new java.awt.GridBagLayout());

        cboLoanIntRate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLoanIntRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoanIntRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanIntRate.add(cboLoanIntRate, gridBagConstraints);

        txtLoanIntRate.setAllowAll(true);
        txtLoanIntRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLoanIntRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLoanIntRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanIntRate.add(txtLoanIntRate, gridBagConstraints);

        lblLoanIntRateVal.setText("Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panLoanIntRate.add(lblLoanIntRateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(panLoanIntRate, gridBagConstraints);

        panPenalIntRate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPenalIntRate.setMinimumSize(new java.awt.Dimension(245, 25));
        panPenalIntRate.setPreferredSize(new java.awt.Dimension(245, 25));
        panPenalIntRate.setLayout(new java.awt.GridBagLayout());

        cboPenalIntRate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPenalIntRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPenalIntRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPenalIntRate.add(cboPenalIntRate, gridBagConstraints);

        txtPenalIntRate.setAllowAll(true);
        txtPenalIntRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalIntRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalIntRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPenalIntRate.add(txtPenalIntRate, gridBagConstraints);

        lblPenalIntRateVal.setText("Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPenalIntRate.add(lblPenalIntRateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(panPenalIntRate, gridBagConstraints);

        panPenalPrizedRate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panPenalPrizedRate.setMinimumSize(new java.awt.Dimension(245, 25));
        panPenalPrizedRate.setPreferredSize(new java.awt.Dimension(245, 25));
        panPenalPrizedRate.setLayout(new java.awt.GridBagLayout());

        cboPenalPrizedRate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPenalPrizedRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPenalPrizedRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPenalPrizedRate.add(cboPenalPrizedRate, gridBagConstraints);

        txtPenalPrizedRate.setAllowAll(true);
        txtPenalPrizedRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPenalPrizedRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPenalPrizedRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPenalPrizedRate.add(txtPenalPrizedRate, gridBagConstraints);

        lblPenalPrizedRateVal.setText("Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panPenalPrizedRate.add(lblPenalPrizedRateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(panPenalPrizedRate, gridBagConstraints);

        panDiscountRate.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panDiscountRate.setMinimumSize(new java.awt.Dimension(245, 25));
        panDiscountRate.setPreferredSize(new java.awt.Dimension(245, 25));
        panDiscountRate.setLayout(new java.awt.GridBagLayout());

        cboDiscountRate.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDiscountRate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDiscountRateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountRate.add(cboDiscountRate, gridBagConstraints);

        txtDiscountRate.setAllowAll(true);
        txtDiscountRate.setMinimumSize(new java.awt.Dimension(100, 21));
        txtDiscountRate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtDiscountRateFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountRate.add(txtDiscountRate, gridBagConstraints);

        lblDiscountRateVal.setText("Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panDiscountRate.add(lblDiscountRateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panRateDetails.add(panDiscountRate, gridBagConstraints);

        cboPenalIntRateFullInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRateDetails.add(cboPenalIntRateFullInstAmt, gridBagConstraints);

        cboPenalPrizedIntRateFullInstAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRateDetails.add(cboPenalPrizedIntRateFullInstAmt, gridBagConstraints);

        lblOperatesForPenalInt.setText("Calculate Int On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 5;
        panRateDetails.add(lblOperatesForPenalInt, gridBagConstraints);

        lblOperatesForPenalPrzInt.setText("Calculate Int On");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 5;
        panRateDetails.add(lblOperatesForPenalPrzInt, gridBagConstraints);

        lblPaymentTimeCharges.setText("Payment Time charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 8, 0, 7);
        panRateDetails.add(lblPaymentTimeCharges, gridBagConstraints);

        cboPaymentTimeCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPaymentTimeCharges.setPopupWidth(160);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRateDetails.add(cboPaymentTimeCharges, gridBagConstraints);

        txtpaymentTimeCharges.setAllowAll(true);
        txtpaymentTimeCharges.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRateDetails.add(txtpaymentTimeCharges, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOtherDetails.add(panRateDetails, gridBagConstraints);

        panThalayalMunnalTrans.setMinimumSize(new java.awt.Dimension(775, 48));
        panThalayalMunnalTrans.setLayout(new java.awt.GridBagLayout());

        lblThalayalTransCategory.setText("Thalayal Bank Transfter Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 30, 0);
        panThalayalMunnalTrans.add(lblThalayalTransCategory, gridBagConstraints);

        cboThalayalTransCategory.setMinimumSize(new java.awt.Dimension(150, 21));
        cboThalayalTransCategory.setPopupWidth(150);
        cboThalayalTransCategory.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panThalayalMunnalTrans.add(cboThalayalTransCategory, gridBagConstraints);

        lblMunnalTransCategory.setText("Munnal Bank Transfer Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panThalayalMunnalTrans.add(lblMunnalTransCategory, gridBagConstraints);

        cboMunnalTransCategory.setMinimumSize(new java.awt.Dimension(150, 21));
        cboMunnalTransCategory.setPopupWidth(150);
        cboMunnalTransCategory.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        panThalayalMunnalTrans.add(cboMunnalTransCategory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        panOtherDetails.add(panThalayalMunnalTrans, gridBagConstraints);

        tabMDSProduct.addTab("Other Details", panOtherDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(tabMDSProduct, gridBagConstraints);

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

    private void rdoBonusAmt_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusAmt_YesActionPerformed
        // TODO add your handling code here:
        if(rdoBonusAmt_Yes.isSelected()==true){
            cboRoundOffCriteria.setVisible(true);
        }else{
            cboRoundOffCriteria.setVisible(false);
            cboRoundOffCriteria.setSelectedItem("");
        }
    }//GEN-LAST:event_rdoBonusAmt_YesActionPerformed

    private void rdoBonusAmt_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusAmt_NoActionPerformed
        // TODO add your handling code here:
        if(rdoBonusAmt_No.isSelected()==true){
            cboRoundOffCriteria.setVisible(false);
            cboRoundOffCriteria.setSelectedItem("");
        }else{
            cboRoundOffCriteria.setVisible(true);
        }
    }//GEN-LAST:event_rdoBonusAmt_NoActionPerformed

    private void rdoNonPrizedChit_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNonPrizedChit_noActionPerformed
        // TODO add your handling code here:
        if(rdoNonPrizedChit_no.isSelected() == true){
            BonusEarliarVisible(false);
        }
    }//GEN-LAST:event_rdoNonPrizedChit_noActionPerformed

    private void rdoNonPrizedChit_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoNonPrizedChit_yesActionPerformed
        // TODO add your handling code here:
        if(rdoNonPrizedChit_yes.isSelected() == true){
            BonusEarliarVisible(true);
        }
    }//GEN-LAST:event_rdoNonPrizedChit_yesActionPerformed
    private void BonusEarliarVisible(boolean flag){
        lblBonusRecoveredExistingChittal.setVisible(flag);
        panBonusRecoveredExistingChittal.setVisible(flag);
    }
    private void txtPenalPrizedPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalPrizedPeriodFocusLost
        // TODO add your handling code here:
        //Commented By Suresh 28-Sep-2012
//        if(rdoBonusPrizedDays.isSelected() == true && cboPenalPrizedPeriod.getSelectedIndex()>0 && cboPenalPrizedPeriod.getSelectedItem().equals("Days")){
//            if(txtBonusPrizedPeriod.getText().length()>0 && txtPenalPrizedPeriod.getText().length()>0 &&
//            CommonUtil.convertObjToDouble(txtBonusPrizedPeriod.getText()).doubleValue()>=CommonUtil.convertObjToDouble(txtPenalPrizedPeriod.getText()).doubleValue()){
//                ClientUtil.showAlertWindow("Value should be greater than bonus prized grace period");
//                txtPenalPrizedPeriod.setText("");
//            }
//        }
    }//GEN-LAST:event_txtPenalPrizedPeriodFocusLost
    
    private void txtPenalPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalPeriodFocusLost
        // TODO add your handling code here:
        //Commented By Suresh 28-Sep-2012
//        if(rdoBonusGraceDays.isSelected() == true && cboPenalPeriod.getSelectedIndex()>0 && cboPenalPeriod.getSelectedItem().equals("Days")){
//            if(txtBonusGracePeriod.getText().length()>0 && txtPenalPeriod.getText().length()>0 &&
//            CommonUtil.convertObjToDouble(txtBonusGracePeriod.getText()).doubleValue()>=CommonUtil.convertObjToDouble(txtPenalPeriod.getText()).doubleValue()){
//                ClientUtil.showAlertWindow("Value should be greater than bonus grace period");
//                txtPenalPeriod.setText("");
//            }
//        }
    }//GEN-LAST:event_txtPenalPeriodFocusLost
                        
    private void chkAcceptableClassCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptableClassCActionPerformed
        // TODO add your handling code here:
        if(chkAcceptableClassA.isSelected()== true && chkAcceptableClassB.isSelected()== true && chkAcceptableClassC.isSelected()== true){
            ClientUtil.showMessageWindow("All Classes Selected !!!");
            chkAcceptableClassAll.setSelected(true);
            chkAcceptableClassA.setEnabled(false);
            chkAcceptableClassB.setEnabled(false);
            chkAcceptableClassC.setEnabled(false);
            chkAcceptableClassA.setSelected(false);
            chkAcceptableClassB.setSelected(false);
            chkAcceptableClassC.setSelected(false);
        }
    }//GEN-LAST:event_chkAcceptableClassCActionPerformed
    
    private void chkAcceptableClassBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptableClassBActionPerformed
        // TODO add your handling code here:
        if(chkAcceptableClassA.isSelected()== true && chkAcceptableClassB.isSelected()== true && chkAcceptableClassC.isSelected()== true){
            ClientUtil.showMessageWindow("All Classes Selected !!!");
            chkAcceptableClassAll.setSelected(true);
            chkAcceptableClassA.setEnabled(false);
            chkAcceptableClassB.setEnabled(false);
            chkAcceptableClassC.setEnabled(false);
            chkAcceptableClassA.setSelected(false);
            chkAcceptableClassB.setSelected(false);
            chkAcceptableClassC.setSelected(false);
        }
    }//GEN-LAST:event_chkAcceptableClassBActionPerformed
    
    private void chkAcceptableClassAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptableClassAActionPerformed
        // TODO add your handling code here:
        if(chkAcceptableClassA.isSelected()== true && chkAcceptableClassB.isSelected()== true && chkAcceptableClassC.isSelected()== true){
            ClientUtil.showMessageWindow("All Classes Selected !!!");
            chkAcceptableClassAll.setSelected(true);
            chkAcceptableClassA.setEnabled(false);
            chkAcceptableClassB.setEnabled(false);
            chkAcceptableClassC.setEnabled(false);
            chkAcceptableClassA.setSelected(false);
            chkAcceptableClassB.setSelected(false);
            chkAcceptableClassC.setSelected(false);
        }
    }//GEN-LAST:event_chkAcceptableClassAActionPerformed
    
    private void chkAcceptClassCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptClassCActionPerformed
        // TODO add your handling code here:
        if(chkAcceptClassA.isSelected()== true && chkAcceptClassB.isSelected()== true && chkAcceptClassC.isSelected()== true){
            ClientUtil.showMessageWindow("All Classes Selected !!!");
            chkAcceptClassAll.setSelected(true);
            chkAcceptClassA.setEnabled(false);
            chkAcceptClassB.setEnabled(false);
            chkAcceptClassC.setEnabled(false);
            chkAcceptClassA.setSelected(false);
            chkAcceptClassB.setSelected(false);
            chkAcceptClassC.setSelected(false);
        }
    }//GEN-LAST:event_chkAcceptClassCActionPerformed
    
    private void chkAcceptClassBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptClassBActionPerformed
        // TODO add your handling code here:
        if(chkAcceptClassA.isSelected()== true && chkAcceptClassB.isSelected()== true && chkAcceptClassC.isSelected()== true){
            ClientUtil.showMessageWindow("All Classes Selected !!!");
            chkAcceptClassAll.setSelected(true);
            chkAcceptClassA.setEnabled(false);
            chkAcceptClassB.setEnabled(false);
            chkAcceptClassC.setEnabled(false);
            chkAcceptClassA.setSelected(false);
            chkAcceptClassB.setSelected(false);
            chkAcceptClassC.setSelected(false);
        }
    }//GEN-LAST:event_chkAcceptClassBActionPerformed
    
    private void chkAcceptClassAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptClassAActionPerformed
        // TODO add your handling code here:
        if(chkAcceptClassA.isSelected()== true && chkAcceptClassB.isSelected()== true && chkAcceptClassC.isSelected()== true){
            ClientUtil.showMessageWindow("All Classes Selected !!!");
            chkAcceptClassAll.setSelected(true);
            chkAcceptClassA.setEnabled(false);
            chkAcceptClassB.setEnabled(false);
            chkAcceptClassC.setEnabled(false);
            chkAcceptClassA.setSelected(false);
            chkAcceptClassB.setSelected(false);
            chkAcceptClassC.setSelected(false);
        }
    }//GEN-LAST:event_chkAcceptClassAActionPerformed
    
    private void chkAcceptableClassAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptableClassAllActionPerformed
        // TODO add your handling code here:
        if(chkAcceptableClassAll.isSelected()== true){
            chkAcceptableClassA.setEnabled(false);
            chkAcceptableClassB.setEnabled(false);
            chkAcceptableClassC.setEnabled(false);
            chkAcceptableClassA.setSelected(false);
            chkAcceptableClassB.setSelected(false);
            chkAcceptableClassC.setSelected(false);
        }else{
            chkAcceptableClassA.setEnabled(true);
            chkAcceptableClassB.setEnabled(true);
            chkAcceptableClassC.setEnabled(true);
        }
    }//GEN-LAST:event_chkAcceptableClassAllActionPerformed
    
    private void chkAcceptClassAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAcceptClassAllActionPerformed
        // TODO add your handling code here:
        if(chkAcceptClassAll.isSelected() == true){
            chkAcceptClassA.setEnabled(false);
            chkAcceptClassB.setEnabled(false);
            chkAcceptClassC.setEnabled(false);
            chkAcceptClassA.setSelected(false);
            chkAcceptClassB.setSelected(false);
            chkAcceptClassC.setSelected(false);
        }else{
            chkAcceptClassA.setEnabled(true);
            chkAcceptClassB.setEnabled(true);
            chkAcceptClassC.setEnabled(true);
        }
    }//GEN-LAST:event_chkAcceptClassAllActionPerformed
    
    private void txtLoanIntRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLoanIntRateFocusLost
        // TODO add your handling code here:
        if(cboLoanIntRate.getSelectedIndex()>0 && cboLoanIntRate.getSelectedItem().equals("Percent") && txtLoanIntRate.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtLoanIntRate.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtLoanIntRate.setText("");
            }
        }
    }//GEN-LAST:event_txtLoanIntRateFocusLost
    
    private void txtPenalPrizedRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalPrizedRateFocusLost
        // TODO add your handling code here:
        if(cboPenalPrizedRate.getSelectedIndex()>0 && cboPenalPrizedRate.getSelectedItem().equals("Percent") && txtPenalPrizedRate.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtPenalPrizedRate.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtPenalPrizedRate.setText("");
            }
        }
    }//GEN-LAST:event_txtPenalPrizedRateFocusLost
    
    private void txtPenalIntRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPenalIntRateFocusLost
        // TODO add your handling code here:
        if(cboPenalIntRate.getSelectedIndex()>0 && cboPenalIntRate.getSelectedItem().equals("Percent") && txtPenalIntRate.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtPenalIntRate.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtPenalIntRate.setText("");
            }
        }
    }//GEN-LAST:event_txtPenalIntRateFocusLost
    
    private void txtDiscountRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountRateFocusLost
        // TODO add your handling code here:
        if(cboDiscountRate.getSelectedIndex()>0 && cboDiscountRate.getSelectedItem().equals("Percent") && txtDiscountRate.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtDiscountRate.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtDiscountRate.setText("");
            }
        }
    }//GEN-LAST:event_txtDiscountRateFocusLost
    
    private void txtMarginFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarginFocusLost
        // TODO add your handling code here:
        if(txtMargin.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtMargin.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtMargin.setText("");
            }
        }
    }//GEN-LAST:event_txtMarginFocusLost
    
    private void txtAuctionMinAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAuctionMinAmtFocusLost
        // TODO add your handling code here:
        if(txtAuctionMinAmt.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtAuctionMinAmt.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtAuctionMinAmt.setText("");
            }
        }
    }//GEN-LAST:event_txtAuctionMinAmtFocusLost
    
    private void txtAuctionMaxAmtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAuctionMaxAmtFocusLost
        // TODO add your handling code here:
        if(txtAuctionMaxAmt.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtAuctionMaxAmt.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtAuctionMaxAmt.setText("");
            }
        }
    }//GEN-LAST:event_txtAuctionMaxAmtFocusLost
    
    private void txtMarginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMarginActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtMarginActionPerformed
    
    private void txtAuctionMinAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuctionMinAmtActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtAuctionMinAmtActionPerformed
    
    private void txtAuctionMaxAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAuctionMaxAmtActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtAuctionMaxAmtActionPerformed
    
    private void cboPenalCalcFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPenalCalcFocusLost
        
    }//GEN-LAST:event_cboPenalCalcFocusLost
    
    private void cboPenalPrizedPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPenalPrizedPeriodFocusLost
        
    }//GEN-LAST:event_cboPenalPrizedPeriodFocusLost
    
    private void cboPenalPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPenalPeriodFocusLost
       
        
    }//GEN-LAST:event_cboPenalPeriodFocusLost
    
    private void cboPenalCalcMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboPenalCalcMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboPenalCalcMouseClicked
    
    private void cboPenalPrizedPeriodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboPenalPrizedPeriodMouseClicked
        // TODO add your handling code here:
        if(cboPenalPeriod.getSelectedIndex()>0 && cboPenalPrizedPeriod.getSelectedIndex()>0){
            String gracePeriod = CommonUtil.convertObjToStr(cboPenalPeriod.getSelectedItem());
            String gracePrizePeriod = CommonUtil.convertObjToStr(cboPenalPrizedPeriod.getSelectedItem());
            if(gracePeriod != gracePrizePeriod){
                ClientUtil.showMessageWindow("Pls Select Same Grace Period !!!!!");
                cboPenalPrizedPeriod.setSelectedItem("");
//                cboPenalCalc.setSelectedItem("");
            }
        }
    }//GEN-LAST:event_cboPenalPrizedPeriodMouseClicked
    
    private void cboPenalPeriodMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cboPenalPeriodMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_cboPenalPeriodMouseClicked
    
    private void cboPenalCalcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPenalCalcActionPerformed
       
    }//GEN-LAST:event_cboPenalCalcActionPerformed
    
    private void cboPenalPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPenalPeriodActionPerformed
        
    }//GEN-LAST:event_cboPenalPeriodActionPerformed
    
    private void cboPenalPrizedPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPenalPrizedPeriodActionPerformed
        
    }//GEN-LAST:event_cboPenalPrizedPeriodActionPerformed
    
    private void rdobonusPayableOwner_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdobonusPayableOwner_noActionPerformed
        // TODO add your handling code here:
        lblBonusChitOwner2.setVisible(true);
        panPrintServicesGR35.setVisible(false);
        lblBonusChitOwner3.setVisible(false);
        panPrintServicesGR36.setVisible(false);
    }//GEN-LAST:event_rdobonusPayableOwner_noActionPerformed
    
    private void cboLoanIntRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoanIntRateActionPerformed
        // TODO add your handling code here:
        if(cboLoanIntRate.getSelectedIndex()>0){
            if(CommonUtil.convertObjToStr(cboLoanIntRate.getSelectedItem()).equals("Absolute")){
                txtLoanIntRate.setValidation(new CurrencyValidation());
            }else if(CommonUtil.convertObjToStr(cboLoanIntRate.getSelectedItem()).equals("Percent")){
                txtLoanIntRate.setValidation(new NumericValidation(3,2));
            }
        }
    }//GEN-LAST:event_cboLoanIntRateActionPerformed
    
    private void cboPenalPrizedRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPenalPrizedRateActionPerformed
        // TODO add your handling code here:
        if(cboPenalPrizedRate.getSelectedIndex()>0){
            if(CommonUtil.convertObjToStr(cboPenalPrizedRate.getSelectedItem()).equals("Absolute")){
                txtPenalPrizedRate.setValidation(new CurrencyValidation());
            }else if(CommonUtil.convertObjToStr(cboPenalPrizedRate.getSelectedItem()).equals("Percent")){
                txtPenalPrizedRate.setValidation(new NumericValidation(3,2));
            }
        }
    }//GEN-LAST:event_cboPenalPrizedRateActionPerformed
    
    private void cboPenalIntRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPenalIntRateActionPerformed
        // TODO add your handling code here:
        if(cboPenalIntRate.getSelectedIndex()>0){
            if(CommonUtil.convertObjToStr(cboPenalIntRate.getSelectedItem()).equals("Absolute")){
                txtPenalIntRate.setValidation(new CurrencyValidation());
            }else if(CommonUtil.convertObjToStr(cboPenalIntRate.getSelectedItem()).equals("Percent")){
                txtPenalIntRate.setValidation(new NumericValidation(3,2));
            }
        }
    }//GEN-LAST:event_cboPenalIntRateActionPerformed
    
    private void cboDiscountRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDiscountRateActionPerformed
        // TODO add your handling code here:
        if(cboDiscountRate.getSelectedIndex()>0){
            if(CommonUtil.convertObjToStr(cboDiscountRate.getSelectedItem()).equals("Absolute")){
                txtDiscountRate.setValidation(new CurrencyValidation());
            }else if(CommonUtil.convertObjToStr(cboDiscountRate.getSelectedItem()).equals("Percent")){
                txtDiscountRate.setValidation(new NumericValidation(3,2));
            }
        }
    }//GEN-LAST:event_cboDiscountRateActionPerformed
    
    private void cboCommisionRateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCommisionRateActionPerformed
        // TODO add your handling code here:
        if(cboCommisionRate.getSelectedIndex()>0){
            if(CommonUtil.convertObjToStr(cboCommisionRate.getSelectedItem()).equals("Absolute")){
                txtCommisionRate.setValidation(new CurrencyValidation());
            }else if(CommonUtil.convertObjToStr(cboCommisionRate.getSelectedItem()).equals("Percent")){
                txtCommisionRate.setValidation(new NumericValidation(3,2));
            }
        }
    }//GEN-LAST:event_cboCommisionRateActionPerformed
    
    private void txtCommisionRateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCommisionRateFocusLost
        // TODO add your handling code here:
        if(cboCommisionRate.getSelectedIndex()>0 && cboCommisionRate.getSelectedItem().equals("Percent") && txtCommisionRate.getText().length()>0){
            int countPercn = CommonUtil.convertObjToInt(txtCommisionRate.getText());
            if(countPercn > 100){
                ClientUtil.showMessageWindow("Percentage Should Not Be More Than 100 !!!!");
                txtCommisionRate.setText("");
            }
        }
    }//GEN-LAST:event_txtCommisionRateFocusLost
    
    private void txtDiscountPrizedPeriodEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountPrizedPeriodEndFocusLost
        // TODO add your handling code here:
        if(rdoDiscountPrizedPeriodAfter.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtDiscountPrizedPeriodEnd.getText()).doubleValue()>31){
                ClientUtil.showAlertWindow("Value should be 31 or less than 31 day");
                txtDiscountPrizedPeriodEnd.setText("");
            }
        }
        if(rdoDiscountPrizedPeriodMonth.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtDiscountPrizedPeriodEnd.getText()).doubleValue()>30){
                ClientUtil.showAlertWindow("Value should be 30 or less than 30 day");
                txtDiscountPrizedPeriodEnd.setText("");
            }
        }
        
    }//GEN-LAST:event_txtDiscountPrizedPeriodEndFocusLost
    
    private void txtDiscountPeriodEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDiscountPeriodEndFocusLost
        // TODO add your handling code here:
        if(rdoDiscountPeriodAfter.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtDiscountPeriodEnd.getText()).doubleValue()>31){
                ClientUtil.showAlertWindow("Value should be 31 or less than 31 day");
                txtDiscountPeriodEnd.setText("");
            }
        }
        if(rdoDiscountPeriodMonth.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtDiscountPeriodEnd.getText()).doubleValue()>30){
                ClientUtil.showAlertWindow("Value should be 30 or less than 30 day");
                txtDiscountPeriodEnd.setText("");
            }
        }
        
    }//GEN-LAST:event_txtDiscountPeriodEndFocusLost
    
    private void txtBonusGracePeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusGracePeriodFocusLost
        // TODO add your handling code here:
        if(rdoBonusGraceAfter.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtBonusGracePeriod.getText()).doubleValue()>31){
                ClientUtil.showAlertWindow("Value should be 31 or less than 31 day");
                txtBonusGracePeriod.setText("");
            }
        }
        if(rdoBonusGraceMonth.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtBonusGracePeriod.getText()).doubleValue()>30){
           //     ClientUtil.showAlertWindow("Value should be 30 or less than 30 day");//ammdam req
           //     txtBonusGracePeriod.setText("");
            }
        }
        //Commented By Suresh 28-Sep-2012
//        if(rdoBonusGraceDays.isSelected() == true && cboPenalPeriod.getSelectedIndex()>0 && cboPenalPeriod.getSelectedItem().equals("Days")){
//            if(txtBonusGracePeriod.getText().length()>0 && txtPenalPeriod.getText().length()>0 &&
//            CommonUtil.convertObjToDouble(txtBonusGracePeriod.getText()).doubleValue()>CommonUtil.convertObjToDouble(txtPenalPeriod.getText()).doubleValue()){
//                ClientUtil.showAlertWindow("Value should be less than penal grace period");
//                txtBonusGracePeriod.setText("");
//            }
//        }
    }//GEN-LAST:event_txtBonusGracePeriodFocusLost
    
    private void txtBonusPrizedPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBonusPrizedPeriodFocusLost
        // TODO add your handling code here:
        if(rdoBonusPrizedAfter.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtBonusPrizedPeriod.getText()).doubleValue()>31){
                ClientUtil.showAlertWindow("Value should be 31 or less than 31 day");
                txtBonusPrizedPeriod.setText("");
            }
        }
        if(rdoBonusPrizedMonth.isSelected() == true){
            if(CommonUtil.convertObjToDouble(txtBonusPrizedPeriod.getText()).doubleValue()>30){
               // ClientUtil.showAlertWindow("Value should be 30 or less than 30 day");
              //  txtBonusPrizedPeriod.setText("");
            }
        }
        //Commented By Suresh 28-Sep-2012
//        if(rdoBonusPrizedDays.isSelected() == true){
//            if(txtBonusPrizedPeriod.getText().length()>0 && txtPenalPrizedPeriod.getText().length()>0 &&
//            CommonUtil.convertObjToDouble(txtBonusPrizedPeriod.getText()).doubleValue()>CommonUtil.convertObjToDouble(txtPenalPrizedPeriod.getText()).doubleValue()){
//                ClientUtil.showAlertWindow("Value should be less than penal prized period");
//                txtBonusPrizedPeriod.setText("");
//            }
//        }
    }//GEN-LAST:event_txtBonusPrizedPeriodFocusLost
    
    private void rdoDiscountPrizedPeriodDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPrizedPeriodDaysActionPerformed
        // TODO add your handling code here:
        txtDiscountPrizedPeriodEnd.setEnabled(true);
        txtDiscountPrizedPeriodEnd.setText("");
    }//GEN-LAST:event_rdoDiscountPrizedPeriodDaysActionPerformed
    
    private void rdoDiscountPrizedPeriodMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPrizedPeriodMonthActionPerformed
        // TODO add your handling code here:
        txtDiscountPrizedPeriodEnd.setEnabled(true);
        txtDiscountPrizedPeriodEnd.setText("");
    }//GEN-LAST:event_rdoDiscountPrizedPeriodMonthActionPerformed
    
    private void rdoDiscountPrizedPeriodAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPrizedPeriodAfterActionPerformed
        // TODO add your handling code here:
        txtDiscountPrizedPeriodEnd.setEnabled(true);
        txtDiscountPrizedPeriodEnd.setText("");
    }//GEN-LAST:event_rdoDiscountPrizedPeriodAfterActionPerformed
    
    private void rdoDiscountPrizedPeriodEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPrizedPeriodEndActionPerformed
        // TODO add your handling code here:
        txtDiscountPrizedPeriodEnd.setText("");
        txtDiscountPrizedPeriodEnd.setEnabled(false);
    }//GEN-LAST:event_rdoDiscountPrizedPeriodEndActionPerformed
    
    private void rdoDiscountPeriodDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPeriodDaysActionPerformed
        // TODO add your handling code here:
        txtDiscountPeriodEnd.setEnabled(true);
        txtDiscountPeriodEnd.setText("");
    }//GEN-LAST:event_rdoDiscountPeriodDaysActionPerformed
    
    private void rdoDiscountPeriodMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPeriodMonthActionPerformed
        // TODO add your handling code here:
        txtDiscountPeriodEnd.setEnabled(true);
        txtDiscountPeriodEnd.setText("");
    }//GEN-LAST:event_rdoDiscountPeriodMonthActionPerformed
    
    private void rdoDiscountPeriodAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPeriodAfterActionPerformed
        // TODO add your handling code here:
        txtDiscountPeriodEnd.setEnabled(true);
        txtDiscountPeriodEnd.setText("");
    }//GEN-LAST:event_rdoDiscountPeriodAfterActionPerformed
    
    private void rdoDiscountPeriodEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountPeriodEndActionPerformed
        // TODO add your handling code here:
        txtDiscountPeriodEnd.setEnabled(false);
        txtDiscountPeriodEnd.setText("");
    }//GEN-LAST:event_rdoDiscountPeriodEndActionPerformed
    
    private void rdoBonusPrizedEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusPrizedEndActionPerformed
        // TODO add your handling code here:
        txtBonusPrizedPeriod.setEnabled(false);
        txtBonusPrizedPeriod.setText("");
    }//GEN-LAST:event_rdoBonusPrizedEndActionPerformed
    
    private void rdoBonusPrizedAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusPrizedAfterActionPerformed
        // TODO add your handling code here:
        txtBonusPrizedPeriod.setEnabled(true);
        txtBonusPrizedPeriod.setText("");
    }//GEN-LAST:event_rdoBonusPrizedAfterActionPerformed
    
    private void rdoBonusPrizedMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusPrizedMonthActionPerformed
        // TODO add your handling code here:
        txtBonusPrizedPeriod.setEnabled(true);
        txtBonusPrizedPeriod.setText("");
    }//GEN-LAST:event_rdoBonusPrizedMonthActionPerformed
    
    private void rdoBonusPrizedDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusPrizedDaysActionPerformed
        // TODO add your handling code here:
        txtBonusPrizedPeriod.setEnabled(true);
        txtBonusPrizedPeriod.setText("");
    }//GEN-LAST:event_rdoBonusPrizedDaysActionPerformed
    
    private void rdoBonusGraceDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusGraceDaysActionPerformed
        // TODO add your handling code here:
        txtBonusGracePeriod.setEnabled(true);
        txtBonusGracePeriod.setText("");
    }//GEN-LAST:event_rdoBonusGraceDaysActionPerformed
    
    private void rdoBonusGraceMonthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusGraceMonthActionPerformed
        // TODO add your handling code here:
        txtBonusGracePeriod.setEnabled(true);
        txtBonusGracePeriod.setText("");
    }//GEN-LAST:event_rdoBonusGraceMonthActionPerformed
    
    private void rdoBonusGraceAfterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusGraceAfterActionPerformed
        // TODO add your handling code here:
        txtBonusGracePeriod.setEnabled(true);
        txtBonusGracePeriod.setText("");
    }//GEN-LAST:event_rdoBonusGraceAfterActionPerformed
    
    private void rdoBonusGraceEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusGraceEndActionPerformed
        // TODO add your handling code here:
        txtBonusGracePeriod.setEnabled(false);
        txtBonusGracePeriod.setText("");
    }//GEN-LAST:event_rdoBonusGraceEndActionPerformed
        
    private void rdoDiscountAllowed_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountAllowed_yesActionPerformed
        // TODO add your handling code here:
        lblDiscountPeriod.setVisible(true);
        lblDiscountPrizedPeriod.setVisible(true);
        panDiscountPeriod.setVisible(true);
        panDiscountPrizedPeriod.setVisible(true);
    }//GEN-LAST:event_rdoDiscountAllowed_yesActionPerformed
    
    private void rdoDiscountAllowed_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoDiscountAllowed_noActionPerformed
        // TODO add your handling code here:
        lblDiscountPeriod.setVisible(false);
        lblDiscountPrizedPeriod.setVisible(false);
        panDiscountPeriod.setVisible(false);
        panDiscountPrizedPeriod.setVisible(false);
    }//GEN-LAST:event_rdoDiscountAllowed_noActionPerformed
    
    private void rdoLoanCanbeGranted_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLoanCanbeGranted_noActionPerformed
        // TODO add your handling code here:
        lblMargin.setVisible(false);
        panMargin.setVisible(false);
        lblMinLoanAmt.setVisible(false);
        txtMinLoanAmt.setVisible(false);
        lblMaxLoanAmt.setVisible(false);
        txtMaxLoanAmt.setVisible(false);
    }//GEN-LAST:event_rdoLoanCanbeGranted_noActionPerformed
    
    private void rdoLoanCanbeGranted_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoLoanCanbeGranted_yesActionPerformed
        // TODO add your handling code here:
        lblMargin.setVisible(true);
        panMargin.setVisible(true);
        lblMinLoanAmt.setVisible(true);
        txtMinLoanAmt.setVisible(true);
        lblMaxLoanAmt.setVisible(true);
        txtMaxLoanAmt.setVisible(true);
    }//GEN-LAST:event_rdoLoanCanbeGranted_yesActionPerformed
    
    private void rdoPrizedChitOwner_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPrizedChitOwner_noActionPerformed
        // TODO add your handling code here:
        rdoPrizedChitOwnerAfter_yes.setEnabled(true);
        rdoPrizedChitOwnerAfter_no.setEnabled(true);
        
    }//GEN-LAST:event_rdoPrizedChitOwner_noActionPerformed
    
    private void rdoPrizedChitOwner_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPrizedChitOwner_yesActionPerformed
        // TODO add your handling code here:
        rdoPrizedChitOwnerAfter_yes.setEnabled(false);
        rdoPrizedChitOwnerAfter_no.setEnabled(false);
        
    }//GEN-LAST:event_rdoPrizedChitOwner_yesActionPerformed
    
    private void rdobonusPayableOwner_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdobonusPayableOwner_yesActionPerformed
        // TODO add your handling code here:
        rdoPrizedChitOwner_yes.setEnabled(true);
        rdoPrizedChitOwner_no.setEnabled(true);
        rdoPrizedChitOwnerAfter_yes.setEnabled(true);
        rdoPrizedChitOwnerAfter_no.setEnabled(true);
        lblBonusChitOwner2.setVisible(true);
//        panPrintServicesGR35.setVisible(true);
//        lblBonusChitOwner3.setVisible(true);
//        panPrintServicesGR36.setVisible(true);
    }//GEN-LAST:event_rdobonusPayableOwner_yesActionPerformed
    
    private void rdoBonusAllowed_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusAllowed_noActionPerformed
        // TODO add your handling code here:
        BonusEnableDisable(false);
    }//GEN-LAST:event_rdoBonusAllowed_noActionPerformed
    private void BonusEnableDisable(boolean val){
        rdobonusPayableOwner_yes.setVisible(val);
        rdobonusPayableOwner_no.setVisible(val);
        rdoPrizedChitOwner_yes.setVisible(val);
        rdoPrizedChitOwner_no.setVisible(val);
        rdoPrizedChitOwnerAfter_yes.setVisible(val);
        rdoPrizedChitOwnerAfter_no.setVisible(val);
        rdoprizedDefaulters_yes.setVisible(val);
        rdoprizedDefaulters_no.setVisible(val);
        lblBonusChitOwner1.setVisible(val);
        lblBonusChitOwner2.setVisible(val);
        lblBonusChitOwner.setVisible(val);
    }
    private void rdoBonusAllowed_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoBonusAllowed_yesActionPerformed
        // TODO add your handling code here:
        BonusEnableDisable(true);
    }//GEN-LAST:event_rdoBonusAllowed_yesActionPerformed
    
    private void rdoSuretyRequired_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSuretyRequired_noActionPerformed
        // TODO add your handling code here:
        if(rdoSuretyRequired_no.isSelected() == true){
            panAcceptableType.setVisible(false);
        }
    }//GEN-LAST:event_rdoSuretyRequired_noActionPerformed
    
    private void rdoOnlyMembers_noActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOnlyMembers_noActionPerformed
        // TODO add your handling code here:
        if(rdoOnlyMembers_no.isSelected() == true){
            panAcceptType.setVisible(false);
        }
    }//GEN-LAST:event_rdoOnlyMembers_noActionPerformed
    
    private void rdoSuretyRequired_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSuretyRequired_yesActionPerformed
        // TODO add your handling code here:
        if(rdoSuretyRequired_yes.isSelected() == true){
            panAcceptableType.setVisible(true);
        }
    }//GEN-LAST:event_rdoSuretyRequired_yesActionPerformed
    
    private void rdoOnlyMembers_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoOnlyMembers_yesActionPerformed
        // TODO add your handling code here:
        if(rdoOnlyMembers_yes.isSelected() == true){
            panAcceptType.setVisible(true);
        }
    }//GEN-LAST:event_rdoOnlyMembers_yesActionPerformed
                                                            
    private void btnCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCopyActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_COPY);
        callView("Copy");
        lblStatus.setText("Copy");
        btnCopy.setEnabled(false);
    }//GEN-LAST:event_btnCopyActionPerformed
    
    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquirystatus");
        btnCheck();
        txtBonusGracePeriod.setEnabled(false);
        txtDiscountPeriodEnd.setEnabled(false);
        txtBonusPrizedPeriod.setEnabled(false);
        txtDiscountPrizedPeriodEnd.setEnabled(false);
    }            //    private void enableDisableAliasBranchTable(boolean flag) {//GEN-LAST:event_btnViewActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        txtBonusGracePeriod.setEnabled(false);
        txtDiscountPeriodEnd.setEnabled(false);
        txtBonusPrizedPeriod.setEnabled(false);
        txtDiscountPrizedPeriodEnd.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
        txtBonusGracePeriod.setEnabled(false);
        txtDiscountPeriodEnd.setEnabled(false);
        txtBonusPrizedPeriod.setEnabled(false);
        txtDiscountPrizedPeriodEnd.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        //__ If there's no data to be Authorized, call Cancel action...
        if(!isModified()){
            setButtonEnableDisable();
            btnCancelActionPerformed(null);
        }
        txtBonusGracePeriod.setEnabled(false);
        txtDiscountPeriodEnd.setEnabled(false);
        txtBonusPrizedPeriod.setEnabled(false);
        txtDiscountPrizedPeriodEnd.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        observable.setStatus();
        ClientUtil.enableDisable(panGeneralDetails, true);
        ClientUtil.enableDisable(panInsideGeneralDetails, true);
        ClientUtil.enableDisable(panGeneralDetailsEntry, true);
        ClientUtil.enableDisable(panRateDetails, true);
        ClientUtil.enableDisable(panGracePeriodDetails, true);
        ClientUtil.enableDisable(panDiscountPeriodDetails, true);
        ClientUtil.enableDisable(panMDSLoan, true);
        ClientUtil.enableDisable(panThalayalMunnalTrans, true);
        resetUIFields();
        observable.resetForm();
        setButtonEnableDisable();
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        btnCopy.setEnabled(false);
        chkIsBonusTrans.setSelected(true);
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Edit");
        btnDelete.setEnabled(false);
        btnCopy.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        callView("Delete");
        txtBonusGracePeriod.setEnabled(false);
        txtDiscountPeriodEnd.setEnabled(false);
        txtBonusPrizedPeriod.setEnabled(false);
        txtDiscountPrizedPeriodEnd.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), tabMDSProduct);
        if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }else if(cboCommisionRate.getSelectedIndex()>0 && txtCommisionRate.getText().length() == 0){
            ClientUtil.showAlertWindow("Commision rate should not be empty");
        }else if(cboDiscountRate.getSelectedIndex()>0 && txtDiscountRate.getText().length() == 0){
            ClientUtil.showAlertWindow("Discount rate should not be empty");
        }else if(cboPenalIntRate.getSelectedIndex()>0 && txtPenalIntRate.getText().length() == 0){
            ClientUtil.showAlertWindow("penal interest rate should not be empty");
        }else if(cboPenalPrizedRate.getSelectedIndex()>0 && txtPenalPrizedRate.getText().length() == 0){
            ClientUtil.showAlertWindow("Penal prized rate should not be empty");
        }else if(cboLoanIntRate.getSelectedIndex()>0 && txtLoanIntRate.getText().length() == 0){
            ClientUtil.showAlertWindow("Loan interest rate should not be empty");
        }else if(cboPenalPeriod.getSelectedIndex() <= 0 || cboPenalPrizedPeriod.getSelectedIndex() <= 0 || cboPenalCalc.getSelectedIndex() <= 0){
            ClientUtil.showMessageWindow("Grace Period value should not be empty!!!!!");
             }else if(!rdoTransFirstIns_Yes.isSelected() && !rdoTransFirstIns_No.isSelected()){
            ClientUtil.showMessageWindow("Whether Transaction Required For First Installment must select YES or NO!!!!!");
            
//        }else if((rdoBonusGraceDays.isSelected() == true || rdoBonusGraceMonth.isSelected() == true ||
//        rdoBonusGraceAfter.isSelected() == true || rdoBonusGraceEnd.isSelected() == true) &&
//        txtBonusGracePeriod.getText().length() == 0 && txtPenalPeriod.getText().length() == 0){
//            ClientUtil.showAlertWindow("Bonus grace period value should not be empty");
//            txtBonusGracePeriod.setText("");
//            txtPenalPeriod.setText("");
//        }else if(cboPenalPeriod.getSelectedItem().equals("Days") && rdoBonusGraceDays.isSelected() == true && txtBonusGracePeriod.getText().length()>0 && txtPenalPeriod.getText().length()>0 &&
//        CommonUtil.convertObjToDouble(txtBonusGracePeriod.getText()).doubleValue()>=CommonUtil.convertObjToDouble(txtPenalPeriod.getText()).doubleValue()){
//            ClientUtil.showAlertWindow("Bonus grace period value should be proper");
//            txtBonusGracePeriod.setText("");
//            txtPenalPeriod.setText("");
//        }else if((rdoBonusPrizedDays.isSelected() == true || rdoBonusPrizedMonth.isSelected() == true ||
//        rdoBonusPrizedAfter.isSelected() == true || rdoBonusPrizedEnd.isSelected() == true) &&
//        txtBonusPrizedPeriod.getText().length() == 0 && txtPenalPrizedPeriod.getText().length() == 0){
//            ClientUtil.showAlertWindow("Bonus prized grace period value should not be empty");
//            txtBonusGracePeriod.setText("");
//            txtPenalPeriod.setText("");
//        }else if(cboPenalPeriod.getSelectedItem().equals("Days") && rdoBonusPrizedDays.isSelected() == true && txtBonusPrizedPeriod.getText().length()>0 && txtPenalPrizedPeriod.getText().length()>0 &&
//        CommonUtil.convertObjToDouble(txtBonusPrizedPeriod.getText()).doubleValue()>=CommonUtil.convertObjToDouble(txtPenalPrizedPeriod.getText()).doubleValue()){
//            ClientUtil.showAlertWindow("penal grace period value should be proper");
//            txtBonusPrizedPeriod.setText("");
//            txtPenalPrizedPeriod.setText("");
        }else if((rdoDiscountPeriodDays.isSelected() == true || rdoDiscountPeriodMonth.isSelected() == true ||
        rdoDiscountPeriodAfter.isSelected() == true) &&  txtDiscountPeriodEnd.getText().length() == 0){
            ClientUtil.showMessageWindow("Discount grace period value should not be empty!!!!!");
        }else if((rdoDiscountPrizedPeriodDays.isSelected() == true || rdoDiscountPrizedPeriodMonth.isSelected() == true ||
        rdoDiscountPrizedPeriodAfter.isSelected() == true) &&  txtDiscountPrizedPeriodEnd.getText().length() == 0){
            ClientUtil.showMessageWindow("Discount prized grace period value should not be empty!!!!!");
        }else if(rdoLoanCanbeGranted_yes.isSelected() == true &&  txtMargin.getText().length() == 0){
            ClientUtil.showMessageWindow("Margin percentage should not be empty!!!!!");
        }else if(rdoLoanCanbeGranted_yes.isSelected() == true &&  txtMinLoanAmt.getText().length() == 0){
            ClientUtil.showMessageWindow("Minimun loan amount should not be empty!!!!!");
        }else if(rdoLoanCanbeGranted_yes.isSelected() == true &&  txtMaxLoanAmt.getText().length() == 0){
            ClientUtil.showMessageWindow("Maximum loan amount should not be empty!!!!!");
        }else{
            savePerformed();
            btnCancel.setEnabled(true);
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
        super.removeEditLock(observable.getTxtProductId());
        observable.resetStatus();
        observable.resetForm();
        resetUIFields();
        setButtonEnableDisable();
        btnCopy.setEnabled(true);
        btnDelete.setEnabled(true);
        ClientUtil.enableDisable(this, false);
        btnNew.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnException.setEnabled(true);
        btnReject.setEnabled(true);
        viewType = "";
        btnCopy.setEnabled(true);
        rdoTransFirstIns_Yes.setSelected(false);
           rdoTransFirstIns_No.setSelected(false);
        //__ Make the Screen Closable..
        setModified(false);
        
    }//GEN-LAST:event_btnCancelActionPerformed
    private void resetUIFields(){
        txtProductId.setText("");
        lblProductDescVal.setText("");
        txtAuctionMaxAmt.setText("");
        txtAuctionMinAmt.setText("");
        txtBonusGracePeriod.setText("");
        txtBonusPrizedPeriod.setText("");
        txtDiscountPeriodEnd.setText("");
        txtDiscountPrizedPeriodEnd.setText("");
        
        rdoOnlyMembers_yes.setSelected(false);
        rdoOnlyMembers_no.setSelected(false);
        rdoSuretyRequired_yes.setSelected(false);
        rdoSuretyRequired_no.setSelected(false);
        rdoShortfall_yes.setSelected(false);
        rdoShortfall_no.setSelected(false);
        rdoChitDefaults_yes.setSelected(false);
        rdoChitDefaults_no.setSelected(false);
        rdoNonPrizedChit_yes.setSelected(false);
        rdoNonPrizedChit_no.setSelected(false);
        rdoBonusAllowed_yes.setSelected(false);
        rdoBonusAllowed_no.setSelected(false);
        rdobonusPayableOwner_yes.setSelected(false);
        rdobonusPayableOwner_no.setSelected(false);
        rdoPrizedChitOwner_yes.setSelected(false);
        rdoPrizedChitOwner_no.setSelected(false);
        rdoPrizedChitOwnerAfter_yes.setSelected(false);
        rdoPrizedChitOwnerAfter_no.setSelected(false);
        rdoprizedDefaulters_yes.setSelected(false);
        rdoprizedDefaulters_no.setSelected(false);
        rdoBonusAmt_Yes.setSelected(false);
        rdoBonusAmt_No.setSelected(false);
        rdoBonusPayFirstIns_Yes.setSelected(false);
        rdoBonusPayFirstIns_No.setSelected(false);
        rdoAdvanceCollection_Yes.setSelected(false);
        rdoAdvanceCollection_No.setSelected(false);
        
        chkAcceptClassA.setSelected(false);
        chkAcceptClassB.setSelected(false);
        chkAcceptClassC.setSelected(false);
        chkAcceptClassAll.setSelected(false);
        
        chkAcceptableClassA.setSelected(false);
        chkAcceptableClassB.setSelected(false);
        chkAcceptableClassC.setSelected(false);
        chkAcceptableClassAll.setSelected(false);
        
        cboCommisionRate.setSelectedItem("");
        cboPaymentTimeCharges.setSelectedItem("");
        txtpaymentTimeCharges.setText("");
        cboDiscountRate.setSelectedItem("");
        cboPenalIntRate.setSelectedItem("");
        cboPenalPrizedRate.setSelectedItem("");
        cboLoanIntRate.setSelectedItem("");
        cboholidayInt.setSelectedItem("");
        cboPenalPeriod.setSelectedItem("");
        cboPenalPrizedPeriod.setSelectedItem("");
        cboPenalCalc.setSelectedItem("");
        cboPenalIntRateFullInstAmt.setSelectedItem("");
        cboPenalPrizedIntRateFullInstAmt.setSelectedItem("");
        
        txtCommisionRate.setText("");
        txtDiscountRate.setText("");
        txtPenalIntRate.setText("");
        txtPenalPrizedRate.setText("");
        txtLoanIntRate.setText("");
        txtPenalPeriod.setText("");
        txtPenalPrizedPeriod.setText("");
        txtMargin.setText("");
        txtMinLoanAmt.setText("");
        txtMaxLoanAmt.setText("");
        
        rdoDiscountAllowed_yes.setSelected(false);
        rdoDiscountAllowed_no.setSelected(false);
        rdoLoanCanbeGranted_yes.setSelected(false);
        rdoLoanCanbeGranted_no.setSelected(false);
        
        rdoBonusGraceDays.setSelected(false);
        rdoBonusGraceMonth.setSelected(false);
        rdoBonusGraceAfter.setSelected(false);
        rdoBonusGraceEnd.setSelected(false);
        
        rdoBonusPrizedDays.setSelected(false);
        rdoBonusPrizedMonth.setSelected(false);
        rdoBonusPrizedAfter.setSelected(false);
        rdoBonusPrizedEnd.setSelected(false);
        
        rdoDiscountPrizedPeriodDays.setSelected(false);
        rdoDiscountPrizedPeriodMonth.setSelected(false);
        rdoDiscountPrizedPeriodAfter.setSelected(false);
        rdoDiscountPrizedPeriodEnd.setSelected(false);
        
        rdoDiscountPeriodDays.setSelected(false);
        rdoDiscountPeriodMonth.setSelected(false);
        rdoDiscountPeriodAfter.setSelected(false);
        rdoDiscountPeriodEnd.setSelected(false);
        lblStatus.setText("");
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
        if (viewType.equals(AUTHORIZE) && isFilled) {
            String strWarnMsg = tabMDSProduct.isAllTabsVisited();
            if (strWarnMsg.length() > 0){
                displayAlert(strWarnMsg);
                return;
            }
            strWarnMsg = null;
            tabMDSProduct.resetVisits();
            final HashMap productMap = new HashMap();
            productMap.put("STATUS", authorizeStatus);
            productMap.put("USER_ID", TrueTransactMain.USER_ID);
            productMap.put("AUTHORIZEDT", currDt.clone());
            productMap.put("PROD_ID",observable.getTxtProductId());
            
            ClientUtil.execute("updateMDSAuthorizeStatus", productMap);
            super.setOpenForEditBy(observable.getStatusBy());
            super.removeEditLock(this.txtProductId.getText());
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
            mapParam.put(CommonConstants.MAP_NAME, "getSelectNotAuthroziedProductList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeRemittanceProduct");
            viewType = AUTHORIZE;
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            isFilled = false;
            final AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSaveDisable();
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
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

    private void chkFromAuctionEnrtryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkFromAuctionEnrtryItemStateChanged
        // TODO add your handling code here:
        if(chkFromAuctionEnrtry.isSelected()){
            chkAfterCashPayment.setEnabled(true);
            chkAfterCashPayment.setSelected(false);
        }else{
            chkAfterCashPayment.setEnabled(true);
        }
    }//GEN-LAST:event_chkFromAuctionEnrtryItemStateChanged

    private void chkAfterCashPaymentItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkAfterCashPaymentItemStateChanged
        // TODO add your handling code here:
        if (chkAfterCashPayment.isSelected()) {
            chkFromAuctionEnrtry.setEnabled(true);
            chkFromAuctionEnrtry.setSelected(false);
        }else{
            chkFromAuctionEnrtry.setEnabled(true);
        }
    }//GEN-LAST:event_chkAfterCashPaymentItemStateChanged

    private void txtProductIdFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtProductIdFocusLost
        // TODO add your handling code here:
        // Added by nithya on 19-05-2016
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_COPY) {
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
    }//GEN-LAST:event_txtProductIdFocusLost

    private void rdoPendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPendingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoPendingActionPerformed

    private void rdoWhichEverIsLessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoWhichEverIsLessActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoWhichEverIsLessActionPerformed
    
    /** This method helps in popoualting the data from the data base
     * @param currField Action the argument is passed according to the command issued
     */
    private void callView(String currField) {
        updateOBFields();
        viewType = currField;
        //        final String PAYBRANCH = CommonUtil.convertObjToStr(((ComboBoxModel)(cboPayableBranchGR.getModel())).getKeyForSelected());
        
        //System.out.println("PAYBRANCH in CallView: " + PAYBRANCH);
        HashMap whereMap = new HashMap();
        HashMap viewMap = new HashMap();
        if (currField.equals("Edit")|| currField.equals("Delete") || currField.equals("Enquirystatus") || currField.equals("Copy")){
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAllProductList");
        } else   if (currField.equalsIgnoreCase("BranchName")) {
            //            viewMap = getBranch(PAYBRANCH);
        } else if (currField.equalsIgnoreCase("BankCode")) {
            //            viewMap.put(CommonConstants.MAP_NAME, "Remittance_Product_Branch.getBankCode");
            //            viewMap = getBank(PAYBRANCH);
        } else if(viewType.equals("ACCT_HEAD") || viewType.equals("RECEIPT_HEAD") || viewType.equals("PAYMENT_HEAD") ||
        viewType.equals("SUSPENSE_HEAD") || viewType.equals("MISCELLANEOUS_HEAD") || viewType.equals("COMMISION_HEAD") ||
        viewType.equals("BONUS_PAYABLE_HEAD") || viewType.equals("BONUS_RECEIVABLE_HEAD") || viewType.equals("PENAL_HEAD") ||
        viewType.equals("THALAYAL_BONUS_HEAD") || viewType.equals("MUNNAL_BONUS_HEAD") || viewType.equals("THALAYAL_RECEIPT_HEAD") ||
        viewType.equals("MUNNAL_RECEIPT_HEAD") || viewType.equals("BANKING_HEAD") || viewType.equals("NOTICE_HEAD") || viewType.equals("CASE_EXPENSE_HEAD")){
            viewMap.put(CommonConstants.MAP_NAME, "MDS.getSelectAcctHeadTOList");
            whereMap.put(CommonConstants.SELECTED_BRANCH_ID, getSelectedBranchID());
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            //            viewMap.put(CommonConstants.MAP_NAME, "RemittancePayment.getSelectAccoutHead");
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
        //        where.put("BANK_CODE", bankCode);
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
            if (viewType.equals("Edit") || viewType.equals("Delete")|| viewType.equals(AUTHORIZE) ||  viewType.equals("Enquirystatus") || viewType.equals("Copy") ) {
                isFilled = true;
                hash.put(CommonConstants.MAP_WHERE, hash.get("PROD_ID"));
                observable.setTxtProductId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                if (observable.populateData(hash)) {
                    //                    enableDisableAliasBranchTable(true);
                    //                } else {
                    //                    enableDisableAliasBranchTable(false);
                }
                //                observable.setTxtProductIdGR((String) hash.get("PRODUCT ID"));
                if ( viewType.equals("Delete") || viewType.equals(AUTHORIZE)|| viewType.equals("Enquirystatus")) {
                    setButtonEnableDisable();
                    ClientUtil.enableDisable(this, false);
                }
                if(viewType ==  AUTHORIZE) {
                    btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                    btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                    btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
                    panAcceptType.setEnabled(false);
                    panAcceptableType.setEnabled(false);
                    panPrintServicesGR35.setEnabled(false);
                    panPrintServicesGR36.setEnabled(false);
                }
                btnCopy.setEnabled(false);               
            }else if (viewType.equalsIgnoreCase("ACCT_HEAD")) {
                //                observable.setTxtActHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            }else if (viewType.equalsIgnoreCase("RECEIPT_HEAD")) {
                observable.setTxtReceiptHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("PAYMENT_HEAD")) {
                observable.setTxtPaymentHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("SUSPENSE_HEAD")) {
                observable.setTxtSuspenseHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("MISCELLANEOUS_HEAD")) {
                observable.setTxtMiscellaneousHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("COMMISION_HEAD")) {
                observable.setTxtCommisionHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("BONUS_PAYABLE_HEAD")) {
                observable.setTxtBonusPayableHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("BONUS_RECEIVABLE_HEAD")) {
                observable.setTxtBonusReceivableHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("PENAL_HEAD")) {
                observable.setTxtPenalHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("THALAYAL_RECEIPT_HEAD")) {
                observable.setTxtThalayalReceiptsHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("THALAYAL_BONUS_HEAD")) {
                observable.setTxtThalayalBonusHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("MUNNAL_BONUS_HEAD")) {
                observable.setTxtMunnalBonusHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("MUNNAL_RECEIPT_HEAD")) {
                observable.setTxtMunnalReceiptsHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("BANKING_HEAD")) {
                observable.setTxtBankingHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("NOTICE_HEAD")) {
                observable.setTxtNoticeChargesHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            } else if (viewType.equalsIgnoreCase("CASE_EXPENSE_HEAD")) {
                observable.setTxtCaseExpensesHead(CommonUtil.convertObjToStr(hash.get(accountHead)));
            }
        }
        if (viewType.equalsIgnoreCase("Edit")){
            ClientUtil.enableDisable(this, true);
            setButtonEnableDisable();
            setDelBtnEnableDisable(true);
            setEnableDisableLapsedBtn();
        }
        if(viewType.equals("Copy")){
            btnSave.setEnabled(true);
            ClientUtil.enableDisable(this, true);
            setButtonEnableDisable();
            setDelBtnEnableDisable(true);
            setEnableDisableLapsedBtn();
        }
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        
        enableDisableButtons();
        if(viewType ==  AUTHORIZE){
            panAcceptType.setEnabled(false);
            panAcceptableType.setEnabled(false);
            panPrintServicesGR35.setEnabled(false);
            panPrintServicesGR36.setEnabled(false);
            rdoPrizedChitOwner_yes.setEnabled(false);
            rdoPrizedChitOwner_no.setEnabled(false);
            rdoPrizedChitOwnerAfter_yes.setEnabled(false);
            rdoPrizedChitOwnerAfter_no.setEnabled(false);
            chkAcceptClassA.setEnabled(false);
            chkAcceptClassB.setEnabled(false);
            chkAcceptClassC.setEnabled(false);
            chkAcceptClassAll.setEnabled(false);
            chkAcceptableClassA.setEnabled(false);
            chkAcceptableClassB.setEnabled(false);
            chkAcceptableClassC.setEnabled(false);
            chkAcceptableClassAll.setEnabled(false);
            chkIsGDS.setEnabled(false);
            chkIsBonusTrans.setEnabled(false);
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
        if (viewType.equals("Copy")) {
            txtProductId.setEnabled(true);
            txtProductId.setText("");
        }
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    private void savePerformed(){
        updateOBFields();
        // If the Action Type is  NEW or EDIT, Check for the Uniqueness of Product ID and Product Description
        //System.out.println("observable.getActionType() : " + observable.getActionType());
        //System.out.println("productID : " + productID);
        if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW || observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT || observable.getActionType() == ClientConstants.ACTIONTYPE_COPY){
            //            productID = observable.uniqueProduct();
        }
        observable.doSave();
        //__ if the Action is not Falied, Reset the fields...
        if(observable.getResult() != ClientConstants.ACTIONTYPE_FAILED){
            HashMap lockMap = new HashMap();
            ArrayList lst = new ArrayList();
            lst.add("PROD_ID");
            lockMap.put(ClientConstants.RECORD_KEY_COL, lst);
            if (observable.getProxyReturnMap()!=null) {
                if (observable.getProxyReturnMap().containsKey("PROD_ID")) {
                    lockMap.put("PROD_ID", observable.getProxyReturnMap().get("PROD_ID"));
                }
            }
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                lockMap.put("PROD_ID", observable.getTxtProductId());
            }
            setEditLockMap(lockMap);
            setEditLock();
            
            
            
            btnCancelActionPerformed(null);
            ClientUtil.enableDisable(this, false);
            if(observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE ){
                //                    super.removeEditLock(txtProductIdGR.getText());
                observable.setResult(observable.getActionType());
            }
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
            setButtonEnableDisable();
            btnSave.setEnabled(false);
            btnCopy.setEnabled(false);
        }
    }
    
    private void enableDisableButtons(){
        if(rdoOnlyMembers_no.isSelected() == true){
            panAcceptType.setVisible(false);
            chkAcceptClassAll.setSelected(false);
        }else{
            panAcceptType.setVisible(true);
        }
        if(rdoSuretyRequired_no.isSelected() == true){
            panAcceptableType.setVisible(false);
            chkAcceptableClassAll.setSelected(false);
        }else{
            panAcceptableType.setVisible(true);
        }
        txtProductId.setEnabled(false);
        if(rdoDiscountAllowed_yes.isSelected() == true){
            lblDiscountPeriod.setVisible(true);
            lblDiscountPrizedPeriod.setVisible(true);
            panDiscountPeriod.setVisible(true);
            panDiscountPrizedPeriod.setVisible(true);
        }else{
            lblDiscountPeriod.setVisible(false);
            lblDiscountPrizedPeriod.setVisible(false);
            panDiscountPeriod.setVisible(false);
            panDiscountPrizedPeriod.setVisible(false);
        }
        if(rdoLoanCanbeGranted_yes.isSelected() == true){
            lblMargin.setVisible(true);
            panMargin.setVisible(true);
            lblMinLoanAmt.setVisible(true);
            txtMinLoanAmt.setVisible(true);
            lblMaxLoanAmt.setVisible(true);
            txtMaxLoanAmt.setVisible(true);
        }else{
            lblMargin.setVisible(false);
            panMargin.setVisible(false);
            lblMinLoanAmt.setVisible(false);
            txtMinLoanAmt.setVisible(false);
            lblMaxLoanAmt.setVisible(false);
            txtMaxLoanAmt.setVisible(false);
        }
        if(rdoBonusGraceEnd.isSelected() == true){
            txtBonusGracePeriod.setEnabled(false);
        }else{
            txtBonusGracePeriod.setEnabled(true);
        }
        if(rdoBonusPrizedEnd.isSelected() == true){
            txtBonusPrizedPeriod.setEnabled(false);
        }else{
            txtBonusPrizedPeriod.setEnabled(true);
        }
        if(rdoDiscountPeriodEnd.isSelected() == true){
            txtDiscountPeriodEnd.setEnabled(false);
        }else{
            txtDiscountPeriodEnd.setEnabled(true);
        }
        if(rdoDiscountPrizedPeriodEnd.isSelected() == true){
            txtDiscountPrizedPeriodEnd.setEnabled(false);
        }else{
            txtDiscountPrizedPeriodEnd.setEnabled(true);
        }
        if(observable.getRdobonusPayableOwner_yes() == true){
            rdobonusPayableOwner_yesActionPerformed(null);
            //            BonusEnableDisable(true);
        }else{
            rdobonusPayableOwner_noActionPerformed(null);
            //            BonusEnableDisable(false);
        }
        if(chkAcceptClassAll.isSelected() == true){
            chkAcceptClassA.setEnabled(false);
            chkAcceptClassB.setEnabled(false);
            chkAcceptClassC.setEnabled(false);
        }else{
            chkAcceptClassA.setEnabled(true);
            chkAcceptClassB.setEnabled(true);
            chkAcceptClassC.setEnabled(true);
        }
        if(chkAcceptableClassAll.isSelected()==true){
            chkAcceptableClassA.setEnabled(false);
            chkAcceptableClassB.setEnabled(false);
            chkAcceptableClassC.setEnabled(false);
        }else{
            chkAcceptableClassA.setEnabled(true);
            chkAcceptableClassB.setEnabled(true);
            chkAcceptableClassC.setEnabled(true);
        }
    }
    
    private void setEnableDisableLapsedBtn(){
    }
    
    private void setEnableDisableRodLapsedBtn(){
    }
    
    private void enableDisableall(){
        
        setButtonEnableDisable();
        
        ClientUtil.enableDisable(this, false);
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
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCopy;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CComboBox cboCommisionRate;
    private com.see.truetransact.uicomponent.CComboBox cboDiscountRate;
    private com.see.truetransact.uicomponent.CComboBox cboLoanIntRate;
    private com.see.truetransact.uicomponent.CComboBox cboMunnalTransCategory;
    private com.see.truetransact.uicomponent.CComboBox cboPaymentTimeCharges;
    private com.see.truetransact.uicomponent.CComboBox cboPenalCalc;
    private com.see.truetransact.uicomponent.CComboBox cboPenalIntRate;
    private com.see.truetransact.uicomponent.CComboBox cboPenalIntRateFullInstAmt;
    private com.see.truetransact.uicomponent.CComboBox cboPenalPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboPenalPrizedIntRateFullInstAmt;
    private com.see.truetransact.uicomponent.CComboBox cboPenalPrizedPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboPenalPrizedRate;
    private com.see.truetransact.uicomponent.CComboBox cboRoundOffCriteria;
    private com.see.truetransact.uicomponent.CComboBox cboThalayalTransCategory;
    private com.see.truetransact.uicomponent.CComboBox cboholidayInt;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptClassA;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptClassAll;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptClassB;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptClassC;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptableClassA;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptableClassAll;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptableClassB;
    private com.see.truetransact.uicomponent.CCheckBox chkAcceptableClassC;
    private com.see.truetransact.uicomponent.CCheckBox chkAfterCashPayment;
    private com.see.truetransact.uicomponent.CCheckBox chkFromAuctionEnrtry;
    private com.see.truetransact.uicomponent.CCheckBox chkIsBonusTrans;
    private com.see.truetransact.uicomponent.CCheckBox chkIsGDS;
    private com.see.truetransact.uicomponent.CCheckBox chkSplitMDSTransaction;
    private com.see.truetransact.uicomponent.CButtonGroup commencmtGrp;
    private com.see.truetransact.uicomponent.CLabel lblAcceptType1;
    private com.see.truetransact.uicomponent.CLabel lblAcceptType2;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType10;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType12;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType13;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType14;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType5;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType7;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType8;
    private com.see.truetransact.uicomponent.CLabel lblAcceptableType9;
    private com.see.truetransact.uicomponent.CLabel lblAdvanceCollection;
    private com.see.truetransact.uicomponent.CLabel lblAuctionMaxAmt;
    private com.see.truetransact.uicomponent.CLabel lblAuctionMaxAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblAuctionMinAmt;
    private com.see.truetransact.uicomponent.CLabel lblAuctionMinAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblBonusAllowed;
    private com.see.truetransact.uicomponent.CLabel lblBonusAmountRounding;
    private com.see.truetransact.uicomponent.CLabel lblBonusChitOwner;
    private com.see.truetransact.uicomponent.CLabel lblBonusChitOwner1;
    private com.see.truetransact.uicomponent.CLabel lblBonusChitOwner2;
    private com.see.truetransact.uicomponent.CLabel lblBonusChitOwner3;
    private com.see.truetransact.uicomponent.CLabel lblBonusGracePeriod;
    private com.see.truetransact.uicomponent.CLabel lblBonusPayFirstIns;
    private com.see.truetransact.uicomponent.CLabel lblBonusPrizedPreriod;
    private com.see.truetransact.uicomponent.CLabel lblBonusRecoveredExistingChittal;
    private com.see.truetransact.uicomponent.CLabel lblChitDefaults;
    private com.see.truetransact.uicomponent.CLabel lblChitDefaults1;
    private com.see.truetransact.uicomponent.CLabel lblCommisionRate;
    private com.see.truetransact.uicomponent.CLabel lblCommisionRateVal;
    private com.see.truetransact.uicomponent.CLabel lblDiscountAllowed;
    private com.see.truetransact.uicomponent.CLabel lblDiscountPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDiscountPrizedPeriod;
    private com.see.truetransact.uicomponent.CLabel lblDiscountRate;
    private com.see.truetransact.uicomponent.CLabel lblDiscountRateVal;
    private com.see.truetransact.uicomponent.CLabel lblHolidayInst;
    private com.see.truetransact.uicomponent.CLabel lblLoanCanbeGranted;
    private com.see.truetransact.uicomponent.CLabel lblLoanIntRate;
    private com.see.truetransact.uicomponent.CLabel lblLoanIntRateVal;
    private com.see.truetransact.uicomponent.CLabel lblMargin;
    private com.see.truetransact.uicomponent.CLabel lblMarginVal;
    private com.see.truetransact.uicomponent.CLabel lblMaxLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblMinLoanAmt;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblMunnalTransCategory;
    private com.see.truetransact.uicomponent.CLabel lblNonPrizedChit;
    private com.see.truetransact.uicomponent.CLabel lblNonPrizedChit1;
    private com.see.truetransact.uicomponent.CLabel lblOnlyMembers;
    private com.see.truetransact.uicomponent.CLabel lblOperatesForPenalInt;
    private com.see.truetransact.uicomponent.CLabel lblOperatesForPenalPrzInt;
    private com.see.truetransact.uicomponent.CLabel lblPaymentTimeCharges;
    private com.see.truetransact.uicomponent.CLabel lblPenalCalc;
    private com.see.truetransact.uicomponent.CLabel lblPenalIntRate;
    private com.see.truetransact.uicomponent.CLabel lblPenalIntRateVal;
    private com.see.truetransact.uicomponent.CLabel lblPenalPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPenalPrizedPeriod;
    private com.see.truetransact.uicomponent.CLabel lblPenalPrizedRate;
    private com.see.truetransact.uicomponent.CLabel lblPenalPrizedRateVal;
    private com.see.truetransact.uicomponent.CLabel lblProductDesc;
    private com.see.truetransact.uicomponent.CTextField lblProductDescVal;
    private com.see.truetransact.uicomponent.CLabel lblProductId;
    private com.see.truetransact.uicomponent.CLabel lblSecurityAgainst;
    private com.see.truetransact.uicomponent.CLabel lblShortfall;
    private com.see.truetransact.uicomponent.CLabel lblSpace;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace40;
    private com.see.truetransact.uicomponent.CLabel lblSpace41;
    private com.see.truetransact.uicomponent.CLabel lblSpace42;
    private com.see.truetransact.uicomponent.CLabel lblSpace43;
    private com.see.truetransact.uicomponent.CLabel lblSpace44;
    private com.see.truetransact.uicomponent.CLabel lblSpace45;
    private com.see.truetransact.uicomponent.CLabel lblSpace46;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSuretyRequired;
    private com.see.truetransact.uicomponent.CLabel lblThalayalTransCategory;
    private com.see.truetransact.uicomponent.CLabel lblTransFirstIns;
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
    private com.see.truetransact.uicomponent.CPanel panAcceptType;
    private com.see.truetransact.uicomponent.CPanel panAcceptableType;
    private com.see.truetransact.uicomponent.CPanel panAdvanceCollection;
    private com.see.truetransact.uicomponent.CPanel panAuctionMaxAmt;
    private com.see.truetransact.uicomponent.CPanel panAuctionMinAmt;
    private com.see.truetransact.uicomponent.CPanel panBonusAllowed;
    private com.see.truetransact.uicomponent.CPanel panBonusGracePeriod;
    private com.see.truetransact.uicomponent.CPanel panBonusGracePeriod1;
    private com.see.truetransact.uicomponent.CPanel panBonusPayFirstIns;
    private com.see.truetransact.uicomponent.CPanel panBonusRecoveredExistingChittal;
    private com.see.truetransact.uicomponent.CPanel panBonusRounding;
    private com.see.truetransact.uicomponent.CPanel panCommencmnt;
    private com.see.truetransact.uicomponent.CPanel panCommisionRate;
    private com.see.truetransact.uicomponent.CPanel panDiscountAllowed;
    private com.see.truetransact.uicomponent.CPanel panDiscountAllowed1;
    private com.see.truetransact.uicomponent.CPanel panDiscountAllowed2;
    private com.see.truetransact.uicomponent.CPanel panDiscountAllowed3;
    private com.see.truetransact.uicomponent.CPanel panDiscountAllowed4;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriod;
    private com.see.truetransact.uicomponent.CPanel panDiscountPeriodDetails;
    private com.see.truetransact.uicomponent.CPanel panDiscountPrizedPeriod;
    private com.see.truetransact.uicomponent.CPanel panDiscountRate;
    private com.see.truetransact.uicomponent.CPanel panGeneral;
    private com.see.truetransact.uicomponent.CPanel panGeneralDetails;
    private com.see.truetransact.uicomponent.CPanel panGeneralDetailsEntry;
    private com.see.truetransact.uicomponent.CPanel panGracePeriodDetails;
    private com.see.truetransact.uicomponent.CPanel panHolidayInst;
    private com.see.truetransact.uicomponent.CPanel panInsideGeneralDetails;
    private com.see.truetransact.uicomponent.CPanel panLapsedGR;
    private com.see.truetransact.uicomponent.CPanel panLoanCanbeGranted;
    private com.see.truetransact.uicomponent.CPanel panLoanIntRate;
    private com.see.truetransact.uicomponent.CPanel panMDSLoan;
    private com.see.truetransact.uicomponent.CPanel panMargin;
    private com.see.truetransact.uicomponent.CPanel panNonPrizedChit;
    private com.see.truetransact.uicomponent.CPanel panOtherDetails;
    private com.see.truetransact.uicomponent.CPanel panPenalCalculation;
    private com.see.truetransact.uicomponent.CPanel panPenalIntRate;
    private com.see.truetransact.uicomponent.CPanel panPenalPeriod;
    private com.see.truetransact.uicomponent.CPanel panPenalPrizedPeriod;
    private com.see.truetransact.uicomponent.CPanel panPenalPrizedRate;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR1;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR35;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR36;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR37;
    private com.see.truetransact.uicomponent.CPanel panPrintServicesGR5;
    private com.see.truetransact.uicomponent.CPanel panProductDetails;
    private com.see.truetransact.uicomponent.CPanel panRateDetails;
    private com.see.truetransact.uicomponent.CPanel panSecurityAgainst;
    private com.see.truetransact.uicomponent.CPanel panShortfall;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSuretyRequired;
    private com.see.truetransact.uicomponent.CPanel panThalayalMunnalTrans;
    private com.see.truetransact.uicomponent.CPanel panTransFirstIns;
    private com.see.truetransact.uicomponent.CButtonGroup rdgAdvanceCollection;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBonusAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBonusGrace;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBonusPayFirstIns;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBonusPayableOwner;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBonusPrized;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBonusRecoveredExistingChittal;
    private com.see.truetransact.uicomponent.CButtonGroup rdgBonusRounding;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDefaulters;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDiscountAllowed;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDiscountPeriod;
    private com.see.truetransact.uicomponent.CButtonGroup rdgDiscountPrizedPeriod;
    private com.see.truetransact.uicomponent.CButtonGroup rdgLoanCanbeGranted;
    private com.see.truetransact.uicomponent.CButtonGroup rdgNonPrizedChit;
    private com.see.truetransact.uicomponent.CButtonGroup rdgOnlyMember;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrizedChitOwner;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrizedChitOwnerAfter;
    private com.see.truetransact.uicomponent.CButtonGroup rdgPrizedDefaulters;
    private com.see.truetransact.uicomponent.CButtonGroup rdgShortfall;
    private com.see.truetransact.uicomponent.CButtonGroup rdgSurety;
    private com.see.truetransact.uicomponent.CButtonGroup rdgTransFirstIns;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdvanceCollection_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoAdvanceCollection_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusAllowed_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusAllowed_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusAmt_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusAmt_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusGraceAfter;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusGraceDays;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusGraceEnd;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusGraceMonth;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusPayFirstIns_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusPayFirstIns_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusPrizedAfter;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusPrizedDays;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusPrizedEnd;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusPrizedMonth;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusRecoveredExistingChittal_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoBonusRecoveredExistingChittal_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoChitDefaults_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoChitDefaults_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountAllowed_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountAllowed_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPeriodAfter;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPeriodDays;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPeriodEnd;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPeriodMonth;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPrizedPeriodAfter;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPrizedPeriodDays;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPrizedPeriodEnd;
    private com.see.truetransact.uicomponent.CRadioButton rdoDiscountPrizedPeriodMonth;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGroupSecurity;
    private com.see.truetransact.uicomponent.CRadioButton rdoLoanCanbeGranted_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoLoanCanbeGranted_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoMethod1;
    private com.see.truetransact.uicomponent.CRadioButton rdoMethod2;
    private com.see.truetransact.uicomponent.CRadioButton rdoNonPrizedChit_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoNonPrizedChit_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoOnlyMembers_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoOnlyMembers_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPending;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrized;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrizedChitOwnerAfter_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrizedChitOwnerAfter_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrizedChitOwner_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoPrizedChitOwner_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoShortfall_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoShortfall_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoSuretyRequired_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoSuretyRequired_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransFirstIns_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoTransFirstIns_Yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoWhichEverIsLess;
    private com.see.truetransact.uicomponent.CRadioButton rdobonusPayableOwner_no;
    private com.see.truetransact.uicomponent.CRadioButton rdobonusPayableOwner_yes;
    private com.see.truetransact.uicomponent.CRadioButton rdoprizedDefaulters_no;
    private com.see.truetransact.uicomponent.CRadioButton rdoprizedDefaulters_yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JSeparator sptException;
    private com.see.truetransact.uicomponent.CTabbedPane tabMDSProduct;
    private javax.swing.JToolBar tbrAdvances;
    private com.see.truetransact.uicomponent.CTextField txtAuctionMaxAmt;
    private com.see.truetransact.uicomponent.CTextField txtAuctionMinAmt;
    private com.see.truetransact.uicomponent.CTextField txtBonusGracePeriod;
    private com.see.truetransact.uicomponent.CTextField txtBonusPrizedPeriod;
    private com.see.truetransact.uicomponent.CTextField txtCommisionRate;
    private com.see.truetransact.uicomponent.CTextField txtDiscountPeriodEnd;
    private com.see.truetransact.uicomponent.CTextField txtDiscountPrizedPeriodEnd;
    private com.see.truetransact.uicomponent.CTextField txtDiscountRate;
    private com.see.truetransact.uicomponent.CTextField txtLoanIntRate;
    private com.see.truetransact.uicomponent.CTextField txtMargin;
    private com.see.truetransact.uicomponent.CTextField txtMaxLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtMinLoanAmt;
    private com.see.truetransact.uicomponent.CTextField txtPenalIntRate;
    private com.see.truetransact.uicomponent.CTextField txtPenalPeriod;
    private com.see.truetransact.uicomponent.CTextField txtPenalPrizedPeriod;
    private com.see.truetransact.uicomponent.CTextField txtPenalPrizedRate;
    private com.see.truetransact.uicomponent.CTextField txtProductId;
    private com.see.truetransact.uicomponent.CTextField txtpaymentTimeCharges;
    // End of variables declaration//GEN-END:variables
    public static void main(String[] arg){
        try {
            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Throwable th) {
            th.printStackTrace();
        }
        javax.swing.JFrame jf = new javax.swing.JFrame();
        MDSProductUI gui = new MDSProductUI();
        jf.getContentPane().add(gui);
        jf.setSize(536, 566);
        jf.show();
        gui.show();
    }
}
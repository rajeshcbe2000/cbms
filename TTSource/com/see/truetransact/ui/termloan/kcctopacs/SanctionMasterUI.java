/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * TermLoanUI.java
 *
 * Created on November 28, 2003, 3:55 PM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import java.util.Map;
import java.util.Date;
import java.util.List;
import javax.swing.table.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import org.apache.log4j.Logger;
import java.util.LinkedHashMap;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.ToDateValidation;
import com.see.truetransact.ui.deposit.lien.DepositLienUI;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.DefaultValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.ui.common.customer.MembershipLiabilityUI;
import com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyUI;
import com.see.truetransact.ui.termloan.emicalculator.TermLoanInstallmentUI;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementUI;
import com.see.truetransact.ui.termloan.loandisbursement.LoanDisbursementOB;
import com.see.truetransact.transferobject.termloan.TermLoanSanctionFacilityTO;
import com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryUI;
import com.see.truetransact.ui.termloan.customerDetailsScreen.CustomerDetailsScreenUI;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.ui.customer.IndividualCustUI;

/*
 *
 * @author shanmugavel Created on November 28, 2003, 3:55 PM
 *
 */
public class SanctionMasterUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.kcctopacs.SanctionMasterRB", ProxyParameters.LANGUAGE);
    HashMap mandatoryMap;
    SanctionMasterOB observable;
    SanctionMasterMRB objMandatoryRB = new SanctionMasterMRB();
    private boolean btnNewPressed;
    private int viewType = -1;
    private final String branchID;
    private final int OP_ACC_NUM = 1;
    private final String PROD_ID = "PROD_ID";
    private final int KCC_ACC_NUM = 2;
    private boolean isFilled = false;
    private final int CUSTOMER = 0;
    Date curDate = null;
    private Date collDT;
    private boolean updateMode = false;
    int updateTab = -1;
    private String view = new String();
    final String AUTHORIZE = "Authorize";
    private String AuthType = new String();

    /**
     * Creates new form TermLoanUI
     */
    public SanctionMasterUI() {
        curDate = ClientUtil.getCurrentDate();
        initComponents();
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setMaxLength();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), tabLimitAmount);
        setObservable();
        branchID = TrueTransactMain.BRANCH_ID;
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        initComponentData();
        setFormButtonEnableDisable(false);
        txtEditSanctionNo.setEnabled(true);
//        btnMembershipLia.setVisible(false);
    }

    private void setFormButtonEnableDisable(boolean flag) {
//        btnMembershipLia.setEnabled(flag);
        btnCustID.setEnabled(flag);
        btnCAAccNo.setEnabled(flag);
        btnKCCAccNo.setEnabled(flag);
        buttonEnableDisable(false);
        buttonEnableDisableMem(false);
        buttonEnableDisableLoan(false);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        PanAcc_CD.setName("PanAcc_CD");
        btnAuthorize.setName("btnAuthorize");
        btnCAAccNo.setName("btnCAAccNo");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustID.setName("btnCustID");
        btnDelete.setName("btnDelete");
        btnDelete_ASW.setName("btnDelete_ASW");
        btnDelete_SubLimit.setName("btnDelete_SubLimit");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnKCCAccNo.setName("btnKCCAccNo");
//        btnMembershipLia.setName("btnMembershipLia");
        btnNew.setName("btnNew");
        btnNew_ASW.setName("btnNew_ASW");
        btnNew_SubLimit.setName("btnNew_SubLimit");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSave_ASW.setName("btnSave_ASW");
        btnSave_SubLimit.setName("btnSave_SubLimit");
        btnView.setName("btnView");
        cboCAProdId.setName("cboCAProdId");
        cboCAProdType.setName("cboCAProdType");
        cboFromAmount.setName("cboFromAmount");
        cboKCCProdId.setName("cboKCCProdId");
        cboToAmount.setName("cboToAmount");
        lblCAAccName.setName("lblCAAccName");
        lblCAAccNo.setName("lblCAAccNo");
        lblCAProdId.setName("lblCAProdId");
        lblCAProdType.setName("lblCAProdType");
        lblClassificationSanDate.setName("lblClassificationSanDate");
        lblClassificationSanDateVal.setName("lblClassificationSanDateVal");
        lblClassificationSanNo.setName("lblClassificationSanNo");
        lblClassificationSanNoVal.setName("lblClassificationSanNoVal");
        lblCustID.setName("lblCustID");
        lblDash.setName("lblDash");
        lblExpiryDt.setName("lblExpiryDt");
        lblFinancialYear.setName("lblFinancialYear");
        lblFromAmount.setName("lblFromAmount");
        lblKCCAccName.setName("lblKCCAccName");
        lblKCCAccName_CD.setName("lblKCCAccName_CD");
        lblKCCAccNo.setName("lblKCCAccNo");
        lblKCCAccNo_CD.setName("lblKCCAccNo_CD");
        lblKCCAccNo_CDVal.setName("lblKCCAccNo_CDVal");
        lblKCCProdId.setName("lblKCCProdId");
        lblMsg.setName("lblMsg");
        lblSlabNoOfMembers.setName("lblNoOfMembers");
        lblPanNumber1.setName("lblPanNumber1");
        lblRemarks.setName("lblRemarks");
        lblSanctionAmt.setName("lblSanctionAmt");
        lblSanctionDt.setName("lblSanctionDt");
        lblSanctionNo.setName("lblSanctionNo");
        lblSanctionNoVal.setName("lblSanctionNoVal");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace6.setName("lblSpace6");
        lblSpace7.setName("lblSpace7");
        lblSpace9.setName("lblSpace9");
        lblStatus.setName("lblStatus");
        lblSubLimitAmt.setName("lblSubLimitAmt");
        lblToAmount.setName("lblToAmount");
        lblTotMembers.setName("lblTotMembers");
        lblTotaMembersVal.setName("lblTotaMembersVal");
        lblTotalLimit.setName("lblTotalLimit");
        lblTotalLimitAmt.setName("lblTotalLimitAmt");
        lblTotalMembers.setName("lblTotalMembers");
        lblTotalNoOfShare.setName("lblTotalNoOfShare");
        lblTotalShareAmount.setName("lblTotalShareAmount");
        lblspace3.setName("lblspace3");
        mbrTermLoan.setName("mbrTermLoan");
        panAccountDetails.setName("panAccountDetails");
        panAmtSlabWise.setName("panAmtSlabWise");
        panAmtSlabWiseDet.setName("panAmtSlabWiseDet");
        panButton2_SD2.setName("panButton2_SD2");
        panButton_AmtSlabWise.setName("panButton_AmtSlabWise");
        panCAAccNo.setName("panCAAccNo");
        panCADetails.setName("panCADetails");
        panClassDetails_Acc.setName("panClassDetails_Acc");
        panClassDetails_Details.setName("panClassDetails_Details");
        panClassMembers.setName("panClassMembers");
        panClassificationDetails.setName("panClassificationDetails");
        panKCCAccNo.setName("panKCCAccNo");
        panKCCDetails.setName("panKCCDetails");
        panProd_CD.setName("panProd_CD");
        panSanction.setName("panSanction");
        panSanctionCustDet.setName("panSanctionCustDet");
        panSanctionDetails.setName("panSanctionDetails");
        panShareDetails.setName("panShareDetails");
        panStatus.setName("panStatus");
        panSubLimit.setName("panSubLimit");
        panSubLimitAcDetails.setName("panSubLimitAcDetails");
        panSubLimitDetails.setName("panSubLimitDetails");
        panTable_ASW.setName("panTable_ASW");
        panTable_SubLimit.setName("panTable_SubLimit");
        panTermLoan.setName("panTermLoan");
        sptBorroewrProfile.setName("sptBorroewrProfile");
        sptClassDetails.setName("sptClassDetails");
        srpComp_Tab_Addr.setName("srpComp_Tab_Addr");
        srpTable_ASW.setName("srpTable_ASW");
        srpTable_SubLimit.setName("srpTable_SubLimit");
        tabLimitAmount.setName("tabLimitAmount");
        tblAmtSlabWise.setName("tblAmtSlabWise");
        tblSubLimit.setName("tblSubLimit");
        tdtExpiryDt.setName("tdtExpiryDt");
        tdtSanctionDt.setName("tdtSanctionDt");
        txtSanctionAmt.setName("txtSanctionAmt");
        txtCAAccNo.setName("txtCAAccNo");
        txtCustID.setName("txtCustID");
        txtEditSanctionNo.setName("txtEditTermLoanNo");
        txtFinancialYear.setName("txtFinancialYear");
        txtFinancialYearEnd.setName("txtFinancialYearEnd");
        txtKCCAccNo.setName("txtKCCAccNo");
        txtRemarks.setName("txtRemarks");
        txtSubLimitAmt.setName("txtSubLimitAmt");
        txtTotMembers.setName("txtTotMembers");
        txtTotalNoOfShare.setName("txtTotalNoOfShare");
        txtTotalShareAmount.setName("txtTotalShareAmount");
        cboCategory.setName("cboCategory");
        cboSubCategory.setName("cboSubCategory");
        txtNoOfMembers.setName("txtNoOfMembers");
        txtAmount.setName("txtAmount");
        txtSlabNoOfMembers.setName("txtSlabNoOfMembers");
    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
        lblFromAmount.setText(resourceBundle.getString("lblFromAmount"));
        btnKCCAccNo.setText(resourceBundle.getString("btnKCCAccNo"));
        btnCAAccNo.setText(resourceBundle.getString("btnCAAccNo"));
        btnCustID.setText(resourceBundle.getString("btnCustID"));
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnDelete_ASW.setText(resourceBundle.getString("btnDelete_ASW"));
        lblKCCAccNo_CD.setText(resourceBundle.getString("lblKCCAccNo_CD"));
        lblKCCAccNo_CDVal.setText(resourceBundle.getString("lblKCCAccNo_CDVal"));
        lblClassificationSanNoVal.setText(resourceBundle.getString("lblClassificationSanNoVal"));
        lblClassificationSanDate.setText(resourceBundle.getString("lblClassificationSanDate"));
        lblClassificationSanDateVal.setText(resourceBundle.getString("lblClassificationSanDateVal"));
        ((javax.swing.border.TitledBorder) panSubLimit.getBorder()).setTitle(resourceBundle.getString("panSubLimit"));
        ((javax.swing.border.TitledBorder) panAmtSlabWise.getBorder()).setTitle(resourceBundle.getString("panAmtSlabWise"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lblSanctionNoVal.setText(resourceBundle.getString("lblSanctionNoVal"));
        lblCustID.setText(resourceBundle.getString("lblCustID"));
        lblClassificationSanNo.setText(resourceBundle.getString("lblClassificationSanNo"));
        lblspace3.setText(resourceBundle.getString("lblspace3"));
        lblTotalNoOfShare.setText(resourceBundle.getString("lblTotalNoOfShare"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblTotalLimit.setText(resourceBundle.getString("lblTotalLimit"));
        lblSanctionAmt.setText(resourceBundle.getString("lblSanctionAmt"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblCAAccNo.setText(resourceBundle.getString("lblCAAccNo"));
        lblKCCProdId.setText(resourceBundle.getString("lblKCCProdId"));
        lblCAAccName.setText(resourceBundle.getString("lblCAAccName"));
        lblKCCAccName.setText(resourceBundle.getString("lblKCCAccName"));
        btnSave_SubLimit.setText(resourceBundle.getString("btnSave_SubLimit"));
        lblPanNumber1.setText(resourceBundle.getString("lblPanNumber1"));
        lblToAmount.setText(resourceBundle.getString("lblToAmount"));
        lblSpace4.setText(resourceBundle.getString("lblSpace4"));
        lblSpace7.setText(resourceBundle.getString("lblSpace7"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblSpace6.setText(resourceBundle.getString("lblSpace6"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        lblKCCAccNo.setText(resourceBundle.getString("lblKCCAccNo"));
        ((javax.swing.border.TitledBorder) panCADetails.getBorder()).setTitle(resourceBundle.getString("panCADetails"));
        lblTotalShareAmount.setText(resourceBundle.getString("lblTotalShareAmount"));
        lblSpace9.setText(resourceBundle.getString("lblSpace9"));
        lblTotMembers.setText(resourceBundle.getString("lblTotMembers"));
//        btnMembershipLia.setText(resourceBundle.getString("btnMembershipLia"));
        lblCAProdId.setText(resourceBundle.getString("lblCAProdId"));
        lblKCCAccName_CD.setText(resourceBundle.getString("lblKCCAccName_CD"));
        lblSanctionNo.setText(resourceBundle.getString("lblSanctionNo"));
        btnReject.setText(resourceBundle.getString("btnReject"));
        btnDelete_SubLimit.setText(resourceBundle.getString("btnDelete_SubLimit"));
        btnNew_SubLimit.setText(resourceBundle.getString("btnNew_SubLimit"));
        btnView.setText(resourceBundle.getString("btnView"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        btnSave_ASW.setText(resourceBundle.getString("btnSave_ASW"));
        lblTotalMembers.setText(resourceBundle.getString("lblTotalMembers"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        btnNew_ASW.setText(resourceBundle.getString("btnNew_ASW"));
        lblSubLimitAmt.setText(resourceBundle.getString("lblSubLimitAmt"));
        ((javax.swing.border.TitledBorder) panSubLimitAcDetails.getBorder()).setTitle(resourceBundle.getString("panSubLimitAcDetails"));
        lblTotalLimitAmt.setText(resourceBundle.getString("lblTotalLimitAmt"));
        lblExpiryDt.setText(resourceBundle.getString("lblExpiryDt"));
        lblTotaMembersVal.setText(resourceBundle.getString("lblTotaMembersVal"));
        lblSanctionDt.setText(resourceBundle.getString("lblSanctionDt"));
        ((javax.swing.border.TitledBorder) panSanction.getBorder()).setTitle(resourceBundle.getString("panSanction"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        ((javax.swing.border.TitledBorder) panShareDetails.getBorder()).setTitle(resourceBundle.getString("panShareDetails"));
        ((javax.swing.border.TitledBorder) panKCCDetails.getBorder()).setTitle(resourceBundle.getString("panKCCDetails"));
        lblFinancialYear.setText(resourceBundle.getString("lblFinancialYear"));
        lblSlabNoOfMembers.setText(resourceBundle.getString("lblNoOfMembers"));
        lblCAProdType.setText(resourceBundle.getString("lblCAProdType"));
        lblDash.setText(resourceBundle.getString("lblDash"));
        lblCategory.setText(resourceBundle.getString("lblCategory"));
        lblSubCategory.setText(resourceBundle.getString("lblSubCategory"));
        lblNoOfMembers.setText(resourceBundle.getString("lblNoOfMembers"));
        lblAmount.setText(resourceBundle.getString("lblAmount"));
    }

    /*
     * Auto Generated Method - setMandatoryHashMap()
     *
     * ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
     *
     * This method list out all the Input Fields available in the UI. It needs a
     * class level HashMap variable mandatoryMap.
     */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtEditTermLoanNo", new Boolean(true));
        mandatoryMap.put("txtCustID", new Boolean(true));
        mandatoryMap.put("tdtSanctionDt", new Boolean(true));
        mandatoryMap.put("tdtExpiryDt", new Boolean(true));
        mandatoryMap.put("txtSanctionAmt", new Boolean(true));
        mandatoryMap.put("txtTotalNoOfShare", new Boolean(true));
        mandatoryMap.put("txtTotalShareAmount", new Boolean(true));
        mandatoryMap.put("txtFinancialYear", new Boolean(true));
        mandatoryMap.put("txtSubLimitAmt", new Boolean(true));
        mandatoryMap.put("txtFinancialYearEnd", new Boolean(true));
        mandatoryMap.put("cboCAProdType", new Boolean(true));
        mandatoryMap.put("cboCAProdId", new Boolean(true));
        mandatoryMap.put("txtCAAccNo", new Boolean(true));
        mandatoryMap.put("cboKCCProdType", new Boolean(true));
        mandatoryMap.put("cboKCCProdId", new Boolean(true));
        mandatoryMap.put("txtKCCAccNo", new Boolean(true));
        mandatoryMap.put("txtRemarks", new Boolean(true));
        mandatoryMap.put("txtTotMembers", new Boolean(true));
        mandatoryMap.put("txtSmallFarmers", new Boolean(true));
        mandatoryMap.put("txtMarginalFarmers", new Boolean(true));
        mandatoryMap.put("txtWomen", new Boolean(true));
        mandatoryMap.put("txtOthersMain", new Boolean(true));
        mandatoryMap.put("txtSC", new Boolean(true));
        mandatoryMap.put("txtST", new Boolean(true));
        mandatoryMap.put("txtMinorityCommunity", new Boolean(true));
        mandatoryMap.put("txtTenantFarmers", new Boolean(true));
        mandatoryMap.put("txtOralLessees", new Boolean(true));
        mandatoryMap.put("txtOthers", new Boolean(true));
        mandatoryMap.put("txtMisc1", new Boolean(true));
        mandatoryMap.put("txtMisc2", new Boolean(true));
        mandatoryMap.put("cboFromAmount", new Boolean(true));
        mandatoryMap.put("cboToAmount", new Boolean(true));
        mandatoryMap.put("txtNoOfMembers", new Boolean(true));
        mandatoryMap.put("cboCategory", new Boolean(true));
        mandatoryMap.put("cboSubCategory", new Boolean(true));
        mandatoryMap.put("txtNoOfMembers", new Boolean(true));
        mandatoryMap.put("txtAmount", new Boolean(true));
        mandatoryMap.put("txtSlabNoOfMembers", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaxLength() {
        txtCustID.setMaxLength(16);
        txtEditSanctionNo.setMaxLength(32);
        txtSanctionAmt.setMaxLength(16);
        txtSanctionAmt.setValidation(new CurrencyValidation(14, 2));
        txtRemarks.setMaxLength(128);
        txtRemarks.setAllowAll(true);
        txtCAAccNo.setMaxLength(16);
        txtCAAccNo.setAllowAll(true);
        txtKCCAccNo.setMaxLength(16);
        txtKCCAccNo.setAllowAll(true);
        txtFinancialYear.setMaxLength(4);
        txtFinancialYearEnd.setMaxLength(4);
        txtFinancialYear.setAllowAll(true);
        txtFinancialYearEnd.setAllowAll(true);
        txtSubLimitAmt.setMaxLength(16);
        txtSubLimitAmt.setValidation(new CurrencyValidation(14, 2));
        txtTotMembers.setMaxLength(5);
        txtSlabNoOfMembers.setMaxLength(4);
        txtTotMembers.setAllowNumber(true);
        txtAmount.setValidation(new CurrencyValidation(14, 2));
        txtNoOfMembers.setAllowAll(true);
        txtSlabNoOfMembers.setAllowNumber(true);
        txtNoOfMembers.setValidation(new NumericValidation());
        txtSlabNoOfMembers.setValidation(new NumericValidation());
        txtTotMembers.setValidation(new NumericValidation());
        txtTotalNoOfShare.setValidation(new NumericValidation());
        txtTotalShareAmount.setValidation(new NumericValidation());
        txtEditSanctionNo.setAllowAll(true);
    }

    private void setButtonEnableDisable() {
        btnView.setEnabled(!btnNew.isEnabled());
        btnNew.setEnabled(!btnNew.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        btnSave.setEnabled(btnCancel.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
        setAuthBtnEnableDisable();
    }

    /**
     * To Enable or Disable Authorize, Rejection and Exception Button
     */
    private void setAuthBtnEnableDisable() {
        final boolean enableDisable = !btnSave.isEnabled();
        btnAuthorize.setEnabled(enableDisable);
        btnException.setEnabled(enableDisable);
        btnReject.setEnabled(enableDisable);
        mitAuthorize.setEnabled(enableDisable);
        mitException.setEnabled(enableDisable);
        mitReject.setEnabled(enableDisable);
    }

    private void initComponentData() {
        cboCAProdType.setModel(observable.getCbmCAProdType());
        cboKCCProdId.setModel(observable.getCbmKCCProdId());
        cboCategory.setModel(observable.getCbmCategory());
        cboSubCategory.setModel(observable.getCbmSubCategory());
        tblSubLimit.setModel(observable.getTblSubLimit());
        tblMemberDetails.setModel(observable.getTblMemberDetails());
        tblAmtSlabWise.setModel(observable.getTbltblAmtSlabWise());
        cboFromAmount.setModel(observable.getCbmFromAmt());
        cboToAmount.setModel(observable.getCbmToAmt());
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
        SanctionMasterMRB objMandatoryRB = new SanctionMasterMRB();
        txtCustID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustID"));
        tdtSanctionDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtSanctionDt"));
        tdtExpiryDt.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtExpiryDt"));
        txtSanctionAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSanctionAmt"));
        txtTotalNoOfShare.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalNoOfShare"));
        txtTotalShareAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalShareAmount"));
        txtFinancialYear.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFinancialYear"));
        txtSubLimitAmt.setHelpMessage(lblMsg, objMandatoryRB.getString("txtSubLimitAmt"));
        txtFinancialYearEnd.setHelpMessage(lblMsg, objMandatoryRB.getString("txtFinancialYearEnd"));
        cboCAProdType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCAProdType"));
        cboCAProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCAProdId"));
        txtCAAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCAAccNo"));
        cboKCCProdId.setHelpMessage(lblMsg, objMandatoryRB.getString("cboKCCProdId"));
        txtKCCAccNo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtKCCAccNo"));
        txtRemarks.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemarks"));
        txtTotMembers.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotMembers"));
        cboFromAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("cboFromAmount"));
        cboToAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("cboToAmount"));
        txtNoOfMembers.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfMembers"));
        cboCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCategory"));
        cboSubCategory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboSubCategory"));
        txtNoOfMembers.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNoOfMembers"));
        txtAmount.setHelpMessage(lblMsg, objMandatoryRB.getString("txtAmount"));
    }

    private void setObservable() {
        try {
            observable = new SanctionMasterOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */

    public void update(Observable observed, Object arg) {
        //        txtEditTermLoanNo.setText(observable.getTxtEditTermLoanNo());                
        txtCustID.setText(observable.getTxtCustID());
        tdtSanctionDt.setDateValue(observable.getTdtSanctionDt());
        tdtExpiryDt.setDateValue(observable.getTdtExpiryDt());
        cboCAProdType.setSelectedItem(observable.getCboCAProdType());
        cboCAProdId.setSelectedItem(observable.getCboCAProdId());
        txtCAAccNo.setText(observable.getTxtCAAccNo());
        cboKCCProdId.setSelectedItem(observable.getCboKCCProdId());
        txtKCCAccNo.setText(observable.getTxtKCCAccNo());
        txtRemarks.setText(observable.getTxtRemarks());
        cboCategory.setSelectedItem(observable.getCboCategory());
        cboSubCategory.setSelectedItem(observable.getCboSubCategory());
        txtAmount.setText(observable.getTxtAmount());
        txtNoOfMembers.setText(observable.getTxtNoOfMembers());
        cboFromAmount.setSelectedItem(observable.getCboFromAmount());
        cboToAmount.setSelectedItem(observable.getCboToAmount());
        txtSlabNoOfMembers.setText(observable.getTxtSlabNoOfMembers());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setLblSanctionNoVal(lblSanctionNoVal.getText());
        observable.setTxtCustID(txtCustID.getText());
        observable.setTdtSanctionDt(tdtSanctionDt.getDateValue());
        observable.setTdtExpiryDt(tdtExpiryDt.getDateValue());
        observable.setTxtSanctionAmt(txtSanctionAmt.getText());
        observable.setTxtFinancialYear(txtFinancialYear.getText());
        observable.setTxtSubLimitAmt(txtSubLimitAmt.getText());
        observable.setTxtFinancialYearEnd(txtFinancialYearEnd.getText());
        observable.setCboCAProdType((String) cboCAProdType.getSelectedItem());
        observable.setCboCAProdId((String) cboCAProdId.getSelectedItem());
        observable.setTxtCAAccNo(txtCAAccNo.getText());
        observable.setCboKCCProdId((String) cboKCCProdId.getSelectedItem());
        observable.setTxtKCCAccNo(txtKCCAccNo.getText());
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setCboFromAmount((String) cboFromAmount.getSelectedItem());
        observable.setCboToAmount((String) cboToAmount.getSelectedItem());
        observable.setTxtSlabNoOfMembers(txtSlabNoOfMembers.getText());
        observable.setTxtTotMembers(txtTotMembers.getText());
    }

    public void updateMemberOBFields() {
        observable.setCboCategory((String) cboCategory.getSelectedItem());
        observable.setCboSubCategory((String) cboSubCategory.getSelectedItem());
        observable.setTxtNoOfMembers(txtNoOfMembers.getText());
        observable.setTxtAmount(txtAmount.getText());
        observable.setTxtTotMembers(txtTotMembers.getText());
    }

    public void updateLoanOBFields() {
        String fromAmt = CommonUtil.convertObjToStr(cboFromAmount.getSelectedItem());
        fromAmt = CurrencyValidation.formatCrore(fromAmt).replaceAll(",", "");
        String toAmt = CommonUtil.convertObjToStr(cboToAmount.getSelectedItem());
        toAmt = CurrencyValidation.formatCrore(toAmt).replaceAll(",", "");
        observable.setCboFromAmount(CommonUtil.convertObjToStr(fromAmt));
        observable.setCboToAmount(CommonUtil.convertObjToStr(toAmt));
        observable.setTxtSlabNoOfMembers(txtSlabNoOfMembers.getText());
    }

    private void btnCustomerID_GDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerID_GDActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnCustomerID_GDActionPerformed

    private void cboProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdTypeActionPerformed
    private void cboPLINameActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }
    private void cboProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboProdIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboProdIdActionPerformed

    private void btnAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAccNoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAccNoActionPerformed

    private void tdtAsOn_GDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtAsOn_GDFocusLost
        // Add your handling code here:
    }//GEN-LAST:event_tdtAsOn_GDFocusLost

    /**
     * This method is called from within the constructor to initialize the form.
     *
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoStatus = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoAccLimit = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoNatureInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRiskWeight = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMultiDisburseAllow = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidy = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoInterest = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSecurityType = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDoAddSIs = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoPostDatedCheque = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoIsSubmitted_DocumentDetails = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoStatus_Repayment = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoExecuted_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoMandatory_DOC = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoGuarnConstution = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSHG = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRenewalGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoEnhanceGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSalaryRecovery = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoRebateInterestGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSubsidyAddMinusGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoDirectRepaymentGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCourtOrderGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        tbrTermLoan = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace70 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace71 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace72 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace73 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace74 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblspace3 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        lblSpace6 = new com.see.truetransact.uicomponent.CLabel();
        lblPanNumber1 = new com.see.truetransact.uicomponent.CLabel();
        lblSpace7 = new com.see.truetransact.uicomponent.CLabel();
        txtEditSanctionNo = new com.see.truetransact.uicomponent.CTextField();
        lblSpace9 = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTermLoan = new com.see.truetransact.uicomponent.CPanel();
        tabLimitAmount = new com.see.truetransact.uicomponent.CTabbedPane();
        panSanctionDetails = new com.see.truetransact.uicomponent.CPanel();
        panSanction = new com.see.truetransact.uicomponent.CPanel();
        panSanctionCustDet = new com.see.truetransact.uicomponent.CPanel();
        lblSanctionNo = new com.see.truetransact.uicomponent.CLabel();
        lblSanctionNoVal = new com.see.truetransact.uicomponent.CLabel();
        lblCustID = new com.see.truetransact.uicomponent.CLabel();
        txtCustID = new com.see.truetransact.uicomponent.CTextField();
        btnCustID = new com.see.truetransact.uicomponent.CButton();
        lblSanctionDt = new com.see.truetransact.uicomponent.CLabel();
        tdtSanctionDt = new com.see.truetransact.uicomponent.CDateField();
        lblExpiryDt = new com.see.truetransact.uicomponent.CLabel();
        tdtExpiryDt = new com.see.truetransact.uicomponent.CDateField();
        lblSanctionAmt = new com.see.truetransact.uicomponent.CLabel();
        txtSanctionAmt = new com.see.truetransact.uicomponent.CTextField();
        panShareDetails = new com.see.truetransact.uicomponent.CPanel();
        lblTotalNoOfShare = new com.see.truetransact.uicomponent.CLabel();
        txtTotalNoOfShare = new com.see.truetransact.uicomponent.CTextField();
        lblTotalShareAmount = new com.see.truetransact.uicomponent.CLabel();
        txtTotalShareAmount = new com.see.truetransact.uicomponent.CTextField();
        sptBorroewrProfile = new com.see.truetransact.uicomponent.CSeparator();
        panViewPhotoSign = new com.see.truetransact.uicomponent.CPanel();
        lblPincode = new com.see.truetransact.uicomponent.CLabel();
        lblPincodeValue = new com.see.truetransact.uicomponent.CLabel();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        lblCountryVaue = new com.see.truetransact.uicomponent.CLabel();
        lblStateValue = new com.see.truetransact.uicomponent.CLabel();
        lblCityValue = new com.see.truetransact.uicomponent.CLabel();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        lblDOB = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerName1 = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerId = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerIdValue = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblDOBValue = new com.see.truetransact.uicomponent.CLabel();
        lblStreetValue = new com.see.truetransact.uicomponent.CLabel();
        lblPincode1 = new com.see.truetransact.uicomponent.CLabel();
        lblPhoneNoValue = new com.see.truetransact.uicomponent.CLabel();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        lblAreaValue = new com.see.truetransact.uicomponent.CLabel();
        panSubLimitAcDetails = new com.see.truetransact.uicomponent.CPanel();
        srpComp_Tab_Addr = new com.see.truetransact.uicomponent.CSeparator();
        panSubLimit = new com.see.truetransact.uicomponent.CPanel();
        panTable_SubLimit = new com.see.truetransact.uicomponent.CPanel();
        srpTable_SubLimit = new com.see.truetransact.uicomponent.CScrollPane();
        tblSubLimit = new com.see.truetransact.uicomponent.CTable();
        lblTotalLimit = new com.see.truetransact.uicomponent.CLabel();
        lblTotalLimitAmt = new com.see.truetransact.uicomponent.CLabel();
        panSubLimitDetails = new com.see.truetransact.uicomponent.CPanel();
        lblFinancialYear = new com.see.truetransact.uicomponent.CLabel();
        txtFinancialYear = new com.see.truetransact.uicomponent.CTextField();
        txtSubLimitAmt = new com.see.truetransact.uicomponent.CTextField();
        lblSubLimitAmt = new com.see.truetransact.uicomponent.CLabel();
        txtFinancialYearEnd = new com.see.truetransact.uicomponent.CTextField();
        lblDash = new com.see.truetransact.uicomponent.CLabel();
        panButton2_SD2 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_SubLimit = new com.see.truetransact.uicomponent.CButton();
        btnSave_SubLimit = new com.see.truetransact.uicomponent.CButton();
        btnDelete_SubLimit = new com.see.truetransact.uicomponent.CButton();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        panCADetails = new com.see.truetransact.uicomponent.CPanel();
        lblCAProdType = new com.see.truetransact.uicomponent.CLabel();
        cboCAProdType = new com.see.truetransact.uicomponent.CComboBox();
        cboCAProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblCAProdId = new com.see.truetransact.uicomponent.CLabel();
        lblCAAccNo = new com.see.truetransact.uicomponent.CLabel();
        panCAAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtCAAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnCAAccNo = new com.see.truetransact.uicomponent.CButton();
        lblCAAccName = new com.see.truetransact.uicomponent.CLabel();
        panKCCDetails = new com.see.truetransact.uicomponent.CPanel();
        cboKCCProdId = new com.see.truetransact.uicomponent.CComboBox();
        lblKCCProdId = new com.see.truetransact.uicomponent.CLabel();
        lblKCCAccNo = new com.see.truetransact.uicomponent.CLabel();
        panKCCAccNo = new com.see.truetransact.uicomponent.CPanel();
        txtKCCAccNo = new com.see.truetransact.uicomponent.CTextField();
        btnKCCAccNo = new com.see.truetransact.uicomponent.CButton();
        lblKCCAccName = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panClassificationDetails = new com.see.truetransact.uicomponent.CPanel();
        panClassDetails_Acc = new com.see.truetransact.uicomponent.CPanel();
        panProd_CD = new com.see.truetransact.uicomponent.CPanel();
        lblClassificationSanNo = new com.see.truetransact.uicomponent.CLabel();
        lblClassificationSanNoVal = new com.see.truetransact.uicomponent.CLabel();
        lblClassificationSanDate = new com.see.truetransact.uicomponent.CLabel();
        lblClassificationSanDateVal = new com.see.truetransact.uicomponent.CLabel();
        PanAcc_CD = new com.see.truetransact.uicomponent.CPanel();
        lblKCCAccName_CD = new com.see.truetransact.uicomponent.CLabel();
        lblKCCAccNo_CD = new com.see.truetransact.uicomponent.CLabel();
        lblKCCAccNo_CDVal = new com.see.truetransact.uicomponent.CLabel();
        sptClassDetails = new com.see.truetransact.uicomponent.CSeparator();
        panClassDetails_Details = new com.see.truetransact.uicomponent.CPanel();
        panClassMembers = new com.see.truetransact.uicomponent.CPanel();
        lblTotMembers = new com.see.truetransact.uicomponent.CLabel();
        txtTotMembers = new com.see.truetransact.uicomponent.CTextField();
        panMemberDetails = new com.see.truetransact.uicomponent.CPanel();
        panAmtSlabWiseDet1 = new com.see.truetransact.uicomponent.CPanel();
        lblCategory = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblSubCategory = new com.see.truetransact.uicomponent.CLabel();
        cboSubCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblNoOfMembers = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfMembers = new com.see.truetransact.uicomponent.CTextField();
        panButton_AmtSlabWise1 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Mem = new com.see.truetransact.uicomponent.CButton();
        btnSave_Mem = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Mem = new com.see.truetransact.uicomponent.CButton();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblAmount = new com.see.truetransact.uicomponent.CLabel();
        panTable_ASW1 = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ASW1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberDetails = new com.see.truetransact.uicomponent.CTable();
        lblTotalMembers1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotaMembersVal1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCatAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCatAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panAmtSlabWise = new com.see.truetransact.uicomponent.CPanel();
        panAmtSlabWiseDet = new com.see.truetransact.uicomponent.CPanel();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        cboFromAmount = new com.see.truetransact.uicomponent.CComboBox();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        cboToAmount = new com.see.truetransact.uicomponent.CComboBox();
        lblSlabNoOfMembers = new com.see.truetransact.uicomponent.CLabel();
        txtSlabNoOfMembers = new com.see.truetransact.uicomponent.CTextField();
        panButton_AmtSlabWise = new com.see.truetransact.uicomponent.CPanel();
        btnNew_ASW = new com.see.truetransact.uicomponent.CButton();
        btnSave_ASW = new com.see.truetransact.uicomponent.CButton();
        btnDelete_ASW = new com.see.truetransact.uicomponent.CButton();
        panTable_ASW = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ASW = new com.see.truetransact.uicomponent.CScrollPane();
        tblAmtSlabWise = new com.see.truetransact.uicomponent.CTable();
        lblTotalMembers = new com.see.truetransact.uicomponent.CLabel();
        lblTotaMembersVal = new com.see.truetransact.uicomponent.CLabel();
        mbrTermLoan = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitAuthorize = new javax.swing.JMenuItem();
        mitException = new javax.swing.JMenuItem();
        mitReject = new javax.swing.JMenuItem();
        sptException = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Sanction Master");
        setMinimumSize(new java.awt.Dimension(860, 663));
        setPreferredSize(new java.awt.Dimension(860, 663));

        tbrTermLoan.setMinimumSize(new java.awt.Dimension(345, 29));

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
        tbrTermLoan.add(btnView);

        lblSpace4.setText("     ");
        tbrTermLoan.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnDelete);

        lblSpace2.setText("     ");
        tbrTermLoan.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnCancel);

        lblSpace3.setText("     ");
        tbrTermLoan.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setEnabled(false);
        btnAuthorize.setMaximumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setMinimumSize(new java.awt.Dimension(29, 27));
        btnAuthorize.setPreferredSize(new java.awt.Dimension(29, 27));
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setEnabled(false);
        btnException.setMaximumSize(new java.awt.Dimension(29, 27));
        btnException.setMinimumSize(new java.awt.Dimension(29, 27));
        btnException.setPreferredSize(new java.awt.Dimension(29, 27));
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setEnabled(false);
        btnReject.setMaximumSize(new java.awt.Dimension(29, 27));
        btnReject.setMinimumSize(new java.awt.Dimension(29, 27));
        btnReject.setPreferredSize(new java.awt.Dimension(29, 27));
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnReject);

        lblspace3.setMaximumSize(new java.awt.Dimension(15, 15));
        lblspace3.setMinimumSize(new java.awt.Dimension(15, 15));
        lblspace3.setPreferredSize(new java.awt.Dimension(15, 15));
        tbrTermLoan.add(lblspace3);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrTermLoan.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrTermLoan.add(btnClose);

        lblSpace6.setText("     ");
        lblSpace6.setMinimumSize(new java.awt.Dimension(200, 18));
        lblSpace6.setPreferredSize(new java.awt.Dimension(200, 18));
        tbrTermLoan.add(lblSpace6);

        lblPanNumber1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblPanNumber1.setText("NCL Sanction No");
        lblPanNumber1.setMinimumSize(new java.awt.Dimension(72, 16));
        tbrTermLoan.add(lblPanNumber1);

        lblSpace7.setText("     ");
        tbrTermLoan.add(lblSpace7);

        txtEditSanctionNo.setMinimumSize(new java.awt.Dimension(100, 18));
        txtEditSanctionNo.setPreferredSize(new java.awt.Dimension(100, 18));
        txtEditSanctionNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEditSanctionNoFocusLost(evt);
            }
        });
        tbrTermLoan.add(txtEditSanctionNo);

        lblSpace9.setText("     ");
        tbrTermLoan.add(lblSpace9);

        getContentPane().add(tbrTermLoan, java.awt.BorderLayout.NORTH);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

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

        panTermLoan.setMinimumSize(new java.awt.Dimension(850, 650));
        panTermLoan.setPreferredSize(new java.awt.Dimension(850, 650));
        panTermLoan.setLayout(new java.awt.GridBagLayout());

        tabLimitAmount.setMaximumSize(new java.awt.Dimension(2147483647, 2147483647));
        tabLimitAmount.setMinimumSize(new java.awt.Dimension(850, 650));
        tabLimitAmount.setPreferredSize(new java.awt.Dimension(850, 650));

        panSanctionDetails.setMinimumSize(new java.awt.Dimension(850, 300));
        panSanctionDetails.setPreferredSize(new java.awt.Dimension(850, 300));
        panSanctionDetails.setLayout(new java.awt.GridBagLayout());

        panSanction.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSanction.setMinimumSize(new java.awt.Dimension(815, 240));
        panSanction.setPreferredSize(new java.awt.Dimension(815, 240));
        panSanction.setLayout(new java.awt.GridBagLayout());

        panSanctionCustDet.setMaximumSize(new java.awt.Dimension(330, 225));
        panSanctionCustDet.setMinimumSize(new java.awt.Dimension(330, 225));
        panSanctionCustDet.setPreferredSize(new java.awt.Dimension(330, 225));
        panSanctionCustDet.setLayout(new java.awt.GridBagLayout());

        lblSanctionNo.setText("NCL Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblSanctionNo, gridBagConstraints);

        lblSanctionNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblSanctionNoVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblSanctionNoVal.setMaximumSize(new java.awt.Dimension(120, 16));
        lblSanctionNoVal.setMinimumSize(new java.awt.Dimension(120, 21));
        lblSanctionNoVal.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblSanctionNoVal, gridBagConstraints);

        lblCustID.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblCustID, gridBagConstraints);

        txtCustID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID.setPreferredSize(new java.awt.Dimension(120, 21));
        txtCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustIDActionPerformed(evt);
            }
        });
        txtCustID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(txtCustID, gridBagConstraints);

        btnCustID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID.setToolTipText("Select Customer");
        btnCustID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustID.setMaximumSize(new java.awt.Dimension(22, 21));
        btnCustID.setMinimumSize(new java.awt.Dimension(22, 21));
        btnCustID.setPreferredSize(new java.awt.Dimension(22, 21));
        btnCustID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSanctionCustDet.add(btnCustID, gridBagConstraints);

        lblSanctionDt.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblSanctionDt, gridBagConstraints);

        tdtSanctionDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtSanctionDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtSanctionDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtSanctionDtFocusLost(evt);
            }
        });
        tdtSanctionDt.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tdtSanctionDtAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(tdtSanctionDt, gridBagConstraints);

        lblExpiryDt.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblExpiryDt, gridBagConstraints);

        tdtExpiryDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtExpiryDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtExpiryDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtExpiryDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(tdtExpiryDt, gridBagConstraints);

        lblSanctionAmt.setText("Sanction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblSanctionAmt, gridBagConstraints);

        txtSanctionAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSanctionAmt.setMinimumSize(new java.awt.Dimension(120, 21));
        txtSanctionAmt.setPreferredSize(new java.awt.Dimension(120, 21));
        txtSanctionAmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSanctionAmtActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(txtSanctionAmt, gridBagConstraints);

        panShareDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Share Details"));
        panShareDetails.setMinimumSize(new java.awt.Dimension(265, 70));
        panShareDetails.setPreferredSize(new java.awt.Dimension(265, 70));
        panShareDetails.setLayout(new java.awt.GridBagLayout());

        lblTotalNoOfShare.setText("Total No Of Share");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 44, 2, 4);
        panShareDetails.add(lblTotalNoOfShare, gridBagConstraints);

        txtTotalNoOfShare.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalNoOfShare.setEditable(false);
        txtTotalNoOfShare.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtTotalNoOfShare.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 1, 4);
        panShareDetails.add(txtTotalNoOfShare, gridBagConstraints);

        lblTotalShareAmount.setText("Total Share Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 6, 4);
        panShareDetails.add(lblTotalShareAmount, gridBagConstraints);

        txtTotalShareAmount.setBackground(new java.awt.Color(212, 208, 200));
        txtTotalShareAmount.setEditable(false);
        txtTotalShareAmount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTotalShareAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 2, 5, 4);
        panShareDetails.add(txtTotalShareAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        panSanctionCustDet.add(panShareDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panSanction.add(panSanctionCustDet, gridBagConstraints);

        sptBorroewrProfile.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptBorroewrProfile.setMinimumSize(new java.awt.Dimension(1, 15));
        sptBorroewrProfile.setPreferredSize(new java.awt.Dimension(1, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 3, 0);
        panSanction.add(sptBorroewrProfile, gridBagConstraints);

        panViewPhotoSign.setMaximumSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setMinimumSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setPreferredSize(new java.awt.Dimension(560, 460));
        panViewPhotoSign.setLayout(new java.awt.GridBagLayout());

        lblPincode.setText("Pin Code  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblPincode, gridBagConstraints);

        lblPincodeValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblPincodeValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblPincodeValue, gridBagConstraints);

        lblCountry.setText("Country  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCountry, gridBagConstraints);

        lblCountryVaue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCountryVaue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCountryVaue, gridBagConstraints);

        lblStateValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblStateValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblStateValue, gridBagConstraints);

        lblCityValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCityValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCityValue, gridBagConstraints);

        lblCity.setText("City  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCity, gridBagConstraints);

        lblState.setText("State  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblState, gridBagConstraints);

        lblStreet.setText("Street  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblStreet, gridBagConstraints);

        lblDOB.setText("Date of Birth  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblDOB, gridBagConstraints);

        lblCustomerName1.setText("Customer Name  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCustomerName1, gridBagConstraints);

        lblCustomerId.setText("Customer Id  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblCustomerId, gridBagConstraints);

        lblCustomerIdValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerIdValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCustomerIdValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCustomerIdValue, gridBagConstraints);

        lblCustomerNameValue.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerNameValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblCustomerNameValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblCustomerNameValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblCustomerNameValue, gridBagConstraints);

        lblDOBValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblDOBValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblDOBValue, gridBagConstraints);

        lblStreetValue.setMinimumSize(new java.awt.Dimension(200, 15));
        lblStreetValue.setPreferredSize(new java.awt.Dimension(200, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblStreetValue, gridBagConstraints);

        lblPincode1.setText("Phone No  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblPincode1, gridBagConstraints);

        lblPhoneNoValue.setMinimumSize(new java.awt.Dimension(150, 15));
        lblPhoneNoValue.setPreferredSize(new java.awt.Dimension(150, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblPhoneNoValue, gridBagConstraints);

        lblArea.setText("Area  : ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 2);
        panViewPhotoSign.add(lblArea, gridBagConstraints);

        lblAreaValue.setMaximumSize(new java.awt.Dimension(300, 15));
        lblAreaValue.setMinimumSize(new java.awt.Dimension(300, 15));
        lblAreaValue.setPreferredSize(new java.awt.Dimension(300, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        panViewPhotoSign.add(lblAreaValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panSanction.add(panViewPhotoSign, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 4, 0);
        panSanctionDetails.add(panSanction, gridBagConstraints);

        panSubLimitAcDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSubLimitAcDetails.setMinimumSize(new java.awt.Dimension(815, 280));
        panSubLimitAcDetails.setPreferredSize(new java.awt.Dimension(815, 280));
        panSubLimitAcDetails.setLayout(new java.awt.GridBagLayout());

        srpComp_Tab_Addr.setOrientation(javax.swing.SwingConstants.VERTICAL);
        srpComp_Tab_Addr.setPreferredSize(new java.awt.Dimension(3, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 8);
        panSubLimitAcDetails.add(srpComp_Tab_Addr, gridBagConstraints);

        panSubLimit.setBorder(javax.swing.BorderFactory.createTitledBorder("Sub Limit for Financial Year"));
        panSubLimit.setMinimumSize(new java.awt.Dimension(520, 200));
        panSubLimit.setPreferredSize(new java.awt.Dimension(520, 200));
        panSubLimit.setLayout(new java.awt.GridBagLayout());

        panTable_SubLimit.setMinimumSize(new java.awt.Dimension(280, 175));
        panTable_SubLimit.setPreferredSize(new java.awt.Dimension(280, 175));
        panTable_SubLimit.setLayout(new java.awt.GridBagLayout());

        srpTable_SubLimit.setMinimumSize(new java.awt.Dimension(250, 140));
        srpTable_SubLimit.setPreferredSize(new java.awt.Dimension(250, 140));

        tblSubLimit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        tblSubLimit.setMinimumSize(new java.awt.Dimension(400, 700));
        tblSubLimit.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblSubLimit.setPreferredSize(new java.awt.Dimension(400, 700));
        tblSubLimit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSubLimitMousePressed(evt);
            }
        });
        srpTable_SubLimit.setViewportView(tblSubLimit);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable_SubLimit.add(srpTable_SubLimit, gridBagConstraints);

        lblTotalLimit.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalLimit.setText("Total Limit : ");
        lblTotalLimit.setMaximumSize(new java.awt.Dimension(100, 15));
        lblTotalLimit.setMinimumSize(new java.awt.Dimension(100, 15));
        lblTotalLimit.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 1, 3, 4);
        panTable_SubLimit.add(lblTotalLimit, gridBagConstraints);

        lblTotalLimitAmt.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalLimitAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalLimitAmt.setMaximumSize(new java.awt.Dimension(100, 15));
        lblTotalLimitAmt.setMinimumSize(new java.awt.Dimension(100, 15));
        lblTotalLimitAmt.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 16);
        panTable_SubLimit.add(lblTotalLimitAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubLimit.add(panTable_SubLimit, gridBagConstraints);

        panSubLimitDetails.setMinimumSize(new java.awt.Dimension(220, 82));
        panSubLimitDetails.setPreferredSize(new java.awt.Dimension(220, 82));
        panSubLimitDetails.setLayout(new java.awt.GridBagLayout());

        lblFinancialYear.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblFinancialYear.setText("Financial Year");
        lblFinancialYear.setMinimumSize(new java.awt.Dimension(100, 18));
        lblFinancialYear.setPreferredSize(new java.awt.Dimension(100, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSubLimitDetails.add(lblFinancialYear, gridBagConstraints);

        txtFinancialYear.setMinimumSize(new java.awt.Dimension(40, 21));
        txtFinancialYear.setPreferredSize(new java.awt.Dimension(40, 21));
        txtFinancialYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFinancialYearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubLimitDetails.add(txtFinancialYear, gridBagConstraints);

        txtSubLimitAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubLimitDetails.add(txtSubLimitAmt, gridBagConstraints);

        lblSubLimitAmt.setText("Limit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSubLimitDetails.add(lblSubLimitAmt, gridBagConstraints);

        txtFinancialYearEnd.setMinimumSize(new java.awt.Dimension(40, 21));
        txtFinancialYearEnd.setPreferredSize(new java.awt.Dimension(40, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubLimitDetails.add(txtFinancialYearEnd, gridBagConstraints);

        lblDash.setText("-");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubLimitDetails.add(lblDash, gridBagConstraints);

        panButton2_SD2.setMinimumSize(new java.awt.Dimension(215, 33));
        panButton2_SD2.setPreferredSize(new java.awt.Dimension(215, 33));
        panButton2_SD2.setLayout(new java.awt.GridBagLayout());

        btnNew_SubLimit.setText("New");
        btnNew_SubLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_SubLimitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnNew_SubLimit, gridBagConstraints);

        btnSave_SubLimit.setText("Save");
        btnSave_SubLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_SubLimitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnSave_SubLimit, gridBagConstraints);

        btnDelete_SubLimit.setText("Delete");
        btnDelete_SubLimit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_SubLimitActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton2_SD2.add(btnDelete_SubLimit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSubLimitDetails.add(panButton2_SD2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panSubLimit.add(panSubLimitDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panSubLimitAcDetails.add(panSubLimit, gridBagConstraints);

        panAccountDetails.setMinimumSize(new java.awt.Dimension(250, 250));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(250, 250));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        panCADetails.setBorder(javax.swing.BorderFactory.createTitledBorder("Current A/c Details"));
        panCADetails.setMinimumSize(new java.awt.Dimension(250, 140));
        panCADetails.setPreferredSize(new java.awt.Dimension(250, 140));
        panCADetails.setLayout(new java.awt.GridBagLayout());

        lblCAProdType.setText("Prod Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCADetails.add(lblCAProdType, gridBagConstraints);

        cboCAProdType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCAProdType.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCAProdType.setPopupWidth(150);
        cboCAProdType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCAProdTypeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCADetails.add(cboCAProdType, gridBagConstraints);

        cboCAProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCAProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCAProdId.setPopupWidth(250);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCADetails.add(cboCAProdId, gridBagConstraints);

        lblCAProdId.setText("Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panCADetails.add(lblCAProdId, gridBagConstraints);

        lblCAAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 21, 4, 4);
        panCADetails.add(lblCAAccNo, gridBagConstraints);

        panCAAccNo.setLayout(new java.awt.GridBagLayout());

        txtCAAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCAAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCAAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panCAAccNo.add(txtCAAccNo, gridBagConstraints);

        btnCAAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCAAccNo.setToolTipText("Select Customer");
        btnCAAccNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCAAccNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnCAAccNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnCAAccNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnCAAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCAAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panCAAccNo.add(btnCAAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panCADetails.add(panCAAccNo, gridBagConstraints);

        lblCAAccName.setForeground(new java.awt.Color(0, 51, 204));
        lblCAAccName.setText("Account Name");
        lblCAAccName.setMaximumSize(new java.awt.Dimension(242, 32));
        lblCAAccName.setMinimumSize(new java.awt.Dimension(242, 32));
        lblCAAccName.setPreferredSize(new java.awt.Dimension(242, 32));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panCADetails.add(lblCAAccName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAccountDetails.add(panCADetails, gridBagConstraints);

        panKCCDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("KCC Loan A/c Details"));
        panKCCDetails.setMinimumSize(new java.awt.Dimension(250, 110));
        panKCCDetails.setPreferredSize(new java.awt.Dimension(250, 110));
        panKCCDetails.setLayout(new java.awt.GridBagLayout());

        cboKCCProdId.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboKCCProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboKCCProdId.setPopupWidth(250);
        cboKCCProdId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboKCCProdIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panKCCDetails.add(cboKCCProdId, gridBagConstraints);

        lblKCCProdId.setText("Prod Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panKCCDetails.add(lblKCCProdId, gridBagConstraints);

        lblKCCAccNo.setText("Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 26, 0, 0);
        panKCCDetails.add(lblKCCAccNo, gridBagConstraints);

        panKCCAccNo.setLayout(new java.awt.GridBagLayout());

        txtKCCAccNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtKCCAccNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtKCCAccNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 1, 0);
        panKCCAccNo.add(txtKCCAccNo, gridBagConstraints);

        btnKCCAccNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnKCCAccNo.setToolTipText("Select Customer");
        btnKCCAccNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnKCCAccNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnKCCAccNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnKCCAccNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnKCCAccNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKCCAccNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panKCCAccNo.add(btnKCCAccNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panKCCDetails.add(panKCCAccNo, gridBagConstraints);

        lblKCCAccName.setForeground(new java.awt.Color(0, 51, 204));
        lblKCCAccName.setText("Account Name");
        lblKCCAccName.setMaximumSize(new java.awt.Dimension(242, 18));
        lblKCCAccName.setMinimumSize(new java.awt.Dimension(242, 18));
        lblKCCAccName.setPreferredSize(new java.awt.Dimension(242, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panKCCDetails.add(lblKCCAccName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAccountDetails.add(panKCCDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panSubLimitAcDetails.add(panAccountDetails, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 30, 3, 4);
        panSubLimitAcDetails.add(lblRemarks, gridBagConstraints);

        txtRemarks.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 3, 4);
        panSubLimitAcDetails.add(txtRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(9, 0, 34, 0);
        panSanctionDetails.add(panSubLimitAcDetails, gridBagConstraints);

        tabLimitAmount.addTab("Sanction Details", panSanctionDetails);

        panClassificationDetails.setMinimumSize(new java.awt.Dimension(700, 500));
        panClassificationDetails.setPreferredSize(new java.awt.Dimension(700, 500));
        panClassificationDetails.setLayout(new java.awt.GridBagLayout());

        panClassDetails_Acc.setMinimumSize(new java.awt.Dimension(650, 45));
        panClassDetails_Acc.setPreferredSize(new java.awt.Dimension(650, 45));
        panClassDetails_Acc.setLayout(new java.awt.GridBagLayout());

        panProd_CD.setMinimumSize(new java.awt.Dimension(250, 58));
        panProd_CD.setPreferredSize(new java.awt.Dimension(250, 58));
        panProd_CD.setLayout(new java.awt.GridBagLayout());

        lblClassificationSanNo.setText("Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblClassificationSanNo, gridBagConstraints);

        lblClassificationSanNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblClassificationSanNoVal.setMaximumSize(new java.awt.Dimension(120, 21));
        lblClassificationSanNoVal.setMinimumSize(new java.awt.Dimension(120, 21));
        lblClassificationSanNoVal.setPreferredSize(new java.awt.Dimension(120, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblClassificationSanNoVal, gridBagConstraints);

        lblClassificationSanDate.setText("Sanction Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblClassificationSanDate, gridBagConstraints);

        lblClassificationSanDateVal.setForeground(new java.awt.Color(0, 51, 204));
        lblClassificationSanDateVal.setMinimumSize(new java.awt.Dimension(100, 21));
        lblClassificationSanDateVal.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblClassificationSanDateVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panClassDetails_Acc.add(panProd_CD, gridBagConstraints);

        PanAcc_CD.setMinimumSize(new java.awt.Dimension(370, 55));
        PanAcc_CD.setPreferredSize(new java.awt.Dimension(370, 55));
        PanAcc_CD.setLayout(new java.awt.GridBagLayout());

        lblKCCAccName_CD.setForeground(new java.awt.Color(0, 51, 204));
        lblKCCAccName_CD.setText("KCC Account Name");
        lblKCCAccName_CD.setMinimumSize(new java.awt.Dimension(250, 21));
        lblKCCAccName_CD.setPreferredSize(new java.awt.Dimension(250, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblKCCAccName_CD, gridBagConstraints);

        lblKCCAccNo_CD.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblKCCAccNo_CD, gridBagConstraints);

        lblKCCAccNo_CDVal.setForeground(new java.awt.Color(0, 51, 204));
        lblKCCAccNo_CDVal.setText("4325");
        lblKCCAccNo_CDVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        PanAcc_CD.add(lblKCCAccNo_CDVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 48, 4, 4);
        panClassDetails_Acc.add(PanAcc_CD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassificationDetails.add(panClassDetails_Acc, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassificationDetails.add(sptClassDetails, gridBagConstraints);

        panClassDetails_Details.setMinimumSize(new java.awt.Dimension(800, 245));
        panClassDetails_Details.setPreferredSize(new java.awt.Dimension(800, 245));
        panClassDetails_Details.setLayout(new java.awt.GridBagLayout());

        panClassMembers.setMinimumSize(new java.awt.Dimension(800, 235));
        panClassMembers.setPreferredSize(new java.awt.Dimension(800, 235));
        panClassMembers.setLayout(new java.awt.GridBagLayout());

        lblTotMembers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotMembers.setText("Total No. of Members");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panClassMembers.add(lblTotMembers, gridBagConstraints);

        txtTotMembers.setMaximumSize(new java.awt.Dimension(60, 21));
        txtTotMembers.setMinimumSize(new java.awt.Dimension(60, 21));
        txtTotMembers.setPreferredSize(new java.awt.Dimension(60, 21));
        panClassMembers.add(txtTotMembers, new java.awt.GridBagConstraints());

        panMemberDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("MemberDetails"));
        panMemberDetails.setMinimumSize(new java.awt.Dimension(700, 200));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(700, 200));
        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        panAmtSlabWiseDet1.setMinimumSize(new java.awt.Dimension(230, 120));
        panAmtSlabWiseDet1.setPreferredSize(new java.awt.Dimension(230, 120));
        panAmtSlabWiseDet1.setLayout(new java.awt.GridBagLayout());

        lblCategory.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(lblCategory, gridBagConstraints);

        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(cboCategory, gridBagConstraints);

        lblSubCategory.setText("Sub Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(lblSubCategory, gridBagConstraints);

        cboSubCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSubCategory.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(cboSubCategory, gridBagConstraints);

        lblNoOfMembers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoOfMembers.setText("No. of Members");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet1.add(lblNoOfMembers, gridBagConstraints);

        txtNoOfMembers.setMaximumSize(new java.awt.Dimension(60, 21));
        txtNoOfMembers.setMinimumSize(new java.awt.Dimension(60, 21));
        txtNoOfMembers.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet1.add(txtNoOfMembers, gridBagConstraints);

        panButton_AmtSlabWise1.setMinimumSize(new java.awt.Dimension(215, 33));
        panButton_AmtSlabWise1.setPreferredSize(new java.awt.Dimension(215, 33));
        panButton_AmtSlabWise1.setLayout(new java.awt.GridBagLayout());

        btnNew_Mem.setText("New");
        btnNew_Mem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_MemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton_AmtSlabWise1.add(btnNew_Mem, gridBagConstraints);

        btnSave_Mem.setText("Save");
        btnSave_Mem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_MemActionPerformed(evt);
            }
        });
        panButton_AmtSlabWise1.add(btnSave_Mem, new java.awt.GridBagConstraints());

        btnDelete_Mem.setText("Delete");
        btnDelete_Mem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_MemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton_AmtSlabWise1.add(btnDelete_Mem, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(panButton_AmtSlabWise1, gridBagConstraints);

        txtAmount.setMaximumSize(new java.awt.Dimension(60, 21));
        txtAmount.setMinimumSize(new java.awt.Dimension(60, 21));
        txtAmount.setPreferredSize(new java.awt.Dimension(120, 21));
        txtAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet1.add(txtAmount, gridBagConstraints);

        lblAmount.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblAmount.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet1.add(lblAmount, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panMemberDetails.add(panAmtSlabWiseDet1, gridBagConstraints);

        panTable_ASW1.setMinimumSize(new java.awt.Dimension(430, 175));
        panTable_ASW1.setPreferredSize(new java.awt.Dimension(430, 175));
        panTable_ASW1.setLayout(new java.awt.GridBagLayout());

        srpTable_ASW1.setMinimumSize(new java.awt.Dimension(400, 150));
        srpTable_ASW1.setPreferredSize(new java.awt.Dimension(400, 150));
        srpTable_ASW1.setRequestFocusEnabled(false);

        tblMemberDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Category", "Sub Category", "No of Members", "Amount"
            }
        ));
        tblMemberDetails.setMinimumSize(new java.awt.Dimension(500, 700));
        tblMemberDetails.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblMemberDetails.setPreferredSize(new java.awt.Dimension(400, 700));
        tblMemberDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblMemberDetailsMousePressed(evt);
            }
        });
        srpTable_ASW1.setViewportView(tblMemberDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable_ASW1.add(srpTable_ASW1, gridBagConstraints);

        lblTotalMembers1.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalMembers1.setText("Total Members : ");
        lblTotalMembers1.setMaximumSize(new java.awt.Dimension(100, 15));
        lblTotalMembers1.setMinimumSize(new java.awt.Dimension(100, 15));
        lblTotalMembers1.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTable_ASW1.add(lblTotalMembers1, gridBagConstraints);

        lblTotaMembersVal1.setForeground(new java.awt.Color(0, 51, 204));
        lblTotaMembersVal1.setMaximumSize(new java.awt.Dimension(50, 15));
        lblTotaMembersVal1.setMinimumSize(new java.awt.Dimension(50, 15));
        lblTotaMembersVal1.setPreferredSize(new java.awt.Dimension(50, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTable_ASW1.add(lblTotaMembersVal1, gridBagConstraints);

        lblTotalCatAmt.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalCatAmt.setText("Total Amount : ");
        lblTotalCatAmt.setMaximumSize(new java.awt.Dimension(100, 15));
        lblTotalCatAmt.setMinimumSize(new java.awt.Dimension(100, 15));
        lblTotalCatAmt.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTable_ASW1.add(lblTotalCatAmt, gridBagConstraints);

        lblTotalCatAmtVal.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalCatAmtVal.setMaximumSize(new java.awt.Dimension(130, 15));
        lblTotalCatAmtVal.setMinimumSize(new java.awt.Dimension(130, 15));
        lblTotalCatAmtVal.setPreferredSize(new java.awt.Dimension(130, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTable_ASW1.add(lblTotalCatAmtVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panMemberDetails.add(panTable_ASW1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panClassMembers.add(panMemberDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassDetails_Details.add(panClassMembers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassificationDetails.add(panClassDetails_Details, gridBagConstraints);

        panAmtSlabWise.setBorder(javax.swing.BorderFactory.createTitledBorder("Loan Amount Slab Wise Details"));
        panAmtSlabWise.setMinimumSize(new java.awt.Dimension(700, 200));
        panAmtSlabWise.setPreferredSize(new java.awt.Dimension(700, 200));
        panAmtSlabWise.setLayout(new java.awt.GridBagLayout());

        panAmtSlabWiseDet.setMinimumSize(new java.awt.Dimension(220, 82));
        panAmtSlabWiseDet.setPreferredSize(new java.awt.Dimension(150, 82));
        panAmtSlabWiseDet.setLayout(new java.awt.GridBagLayout());

        lblFromAmount.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet.add(lblFromAmount, gridBagConstraints);

        cboFromAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet.add(cboFromAmount, gridBagConstraints);

        lblToAmount.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet.add(lblToAmount, gridBagConstraints);

        cboToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        cboToAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboToAmountFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet.add(cboToAmount, gridBagConstraints);

        lblSlabNoOfMembers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblSlabNoOfMembers.setText("No. of Members");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet.add(lblSlabNoOfMembers, gridBagConstraints);

        txtSlabNoOfMembers.setMaximumSize(new java.awt.Dimension(60, 21));
        txtSlabNoOfMembers.setMinimumSize(new java.awt.Dimension(60, 21));
        txtSlabNoOfMembers.setPreferredSize(new java.awt.Dimension(60, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet.add(txtSlabNoOfMembers, gridBagConstraints);

        panButton_AmtSlabWise.setMinimumSize(new java.awt.Dimension(215, 33));
        panButton_AmtSlabWise.setPreferredSize(new java.awt.Dimension(215, 33));
        panButton_AmtSlabWise.setLayout(new java.awt.GridBagLayout());

        btnNew_ASW.setText("New");
        btnNew_ASW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_ASWActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton_AmtSlabWise.add(btnNew_ASW, gridBagConstraints);

        btnSave_ASW.setText("Save");
        btnSave_ASW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_ASWActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton_AmtSlabWise.add(btnSave_ASW, gridBagConstraints);

        btnDelete_ASW.setText("Delete");
        btnDelete_ASW.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_ASWActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton_AmtSlabWise.add(btnDelete_ASW, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet.add(panButton_AmtSlabWise, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panAmtSlabWise.add(panAmtSlabWiseDet, gridBagConstraints);

        panTable_ASW.setMinimumSize(new java.awt.Dimension(350, 175));
        panTable_ASW.setPreferredSize(new java.awt.Dimension(280, 175));
        panTable_ASW.setLayout(new java.awt.GridBagLayout());

        srpTable_ASW.setMinimumSize(new java.awt.Dimension(300, 160));
        srpTable_ASW.setPreferredSize(new java.awt.Dimension(300, 160));

        tblAmtSlabWise.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tblAmtSlabWise.setMinimumSize(new java.awt.Dimension(350, 600));
        tblAmtSlabWise.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblAmtSlabWise.setPreferredSize(new java.awt.Dimension(350, 600));
        tblAmtSlabWise.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAmtSlabWiseMousePressed(evt);
            }
        });
        srpTable_ASW.setViewportView(tblAmtSlabWise);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable_ASW.add(srpTable_ASW, gridBagConstraints);

        lblTotalMembers.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalMembers.setText("Total Members : ");
        lblTotalMembers.setMaximumSize(new java.awt.Dimension(100, 15));
        lblTotalMembers.setMinimumSize(new java.awt.Dimension(100, 15));
        lblTotalMembers.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTable_ASW.add(lblTotalMembers, gridBagConstraints);

        lblTotaMembersVal.setForeground(new java.awt.Color(0, 51, 204));
        lblTotaMembersVal.setMaximumSize(new java.awt.Dimension(100, 15));
        lblTotaMembersVal.setMinimumSize(new java.awt.Dimension(100, 15));
        lblTotaMembersVal.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 4);
        panTable_ASW.add(lblTotaMembersVal, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWise.add(panTable_ASW, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 35, 0);
        panClassificationDetails.add(panAmtSlabWise, gridBagConstraints);
        panAmtSlabWise.getAccessibleContext().setAccessibleDescription("panAmtSlabWise");

        tabLimitAmount.addTab("Classification Details", panClassificationDetails);

        panTermLoan.add(tabLimitAmount, new java.awt.GridBagConstraints());

        getContentPane().add(panTermLoan, java.awt.BorderLayout.CENTER);

        mnuProcess.setText("Process");
        mnuProcess.setMinimumSize(new java.awt.Dimension(73, 19));

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
        mitEdit.setEnabled(false);
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
        mitDelete.setEnabled(false);
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
        mitSave.setEnabled(false);
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.setEnabled(false);
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

        mitAuthorize.setText("Authorize");
        mitAuthorize.setEnabled(false);
        mitAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAuthorizeActionPerformed(evt);
            }
        });
        mnuProcess.add(mitAuthorize);

        mitException.setText("Exception");
        mitException.setEnabled(false);
        mitException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitExceptionActionPerformed(evt);
            }
        });
        mnuProcess.add(mitException);

        mitReject.setText("Rejection");
        mitReject.setEnabled(false);
        mitReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitRejectActionPerformed(evt);
            }
        });
        mnuProcess.add(mitReject);
        mnuProcess.add(sptException);

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

        mbrTermLoan.add(mnuProcess);

        setJMenuBar(mbrTermLoan);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtEditSanctionNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEditSanctionNoFocusLost
        // TODO add your handling code here:  
        if (txtEditSanctionNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("NCL_SANCTION_NO", txtEditSanctionNo.getText());
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getNclSanctionDetailsEdit", whereMap);
            if (lst != null && lst.size() > 0) {
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                lblStatus.setText("Edit");
                btnNew_SubLimit.setEnabled(true);
                whereMap = (HashMap) lst.get(0);
                fillData(whereMap);
                btnCancel.setEnabled(true);
                lst = null;
                whereMap = null;
                EnableDisableValues();
                btnSave.setEnabled(true);
            } else {
                ClientUtil.displayAlert("Invalid NclSanction Number !!! ");
                txtEditSanctionNo.setText("");
                btncancelActionPerformed();
                return;
            }
        }
    }//GEN-LAST:event_txtEditSanctionNoFocusLost

    private void EnableDisableValues() {
        txtCustID.setEnabled(false);
        tdtSanctionDt.setEnabled(false);
        tdtExpiryDt.setEnabled(false);
        txtRemarks.setEnabled(false);
        cboCAProdType.setEnabled(false);
        cboCAProdId.setEnabled(false);
        cboKCCProdId.setEnabled(false);
        txtKCCAccNo.setEnabled(false);
        txtTotMembers.setEnabled(false);
    }

            private void txtCustIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustIDFocusLost
                // TODO add your handling code here:
                showingCustomerDetails(txtCustID.getText());
                HashMap newMap = new HashMap();
                newMap.put("CUSTOMER ID", txtCustID.getText());
                displayShareDetails(newMap);
    }//GEN-LAST:event_txtCustIDFocusLost

    private void showingCustomerDetails(String custId) {
        HashMap customerMap = new HashMap();
        customerMap.put("CUST_ID", custId);
        customerMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        List custListData = ClientUtil.executeQuery("getSelectAccInfoDisplay", customerMap);
        if (custListData != null && custListData.size() > 0) {
            customerMap = (HashMap) custListData.get(0);
            lblCustomerIdValue.setText(String.valueOf(custId));
            lblCustomerNameValue.setText(CommonUtil.convertObjToStr(customerMap.get("Name")));
            lblDOBValue.setText(CommonUtil.convertObjToStr(customerMap.get("DOB")));
            lblStreetValue.setText(CommonUtil.convertObjToStr(customerMap.get("STREET")));
            lblAreaValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA")));
            lblCityValue.setText(CommonUtil.convertObjToStr(customerMap.get("CITY1")));
            lblStateValue.setText(CommonUtil.convertObjToStr(customerMap.get("STATE1")));
            lblCountryVaue.setText(String.valueOf("India"));
            lblPincodeValue.setText(CommonUtil.convertObjToStr(customerMap.get("PIN_CODE")));
            if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("LAND LINE"))) {
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE")) + CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
            if ((customerMap.get("PHONE_TYPE_ID") != null) && (customerMap.get("PHONE_TYPE_ID").equals("FAX"))) {
                lblPhoneNoValue.setText(CommonUtil.convertObjToStr(customerMap.get("AREA_CODE")) + CommonUtil.convertObjToStr(customerMap.get("PHONE_NUMBER")));
            }
        }
        tdtSanctionDt.setDateValue(DateUtil.getStringDate(curDate));
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        callView("Enquiry");
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

    private void txtCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustIDActionPerformed
    }//GEN-LAST:event_txtCustIDActionPerformed

    public void fillData(Object param) {
        isFilled = true;
        final HashMap hash = (HashMap) param;
        String prodType = ((ComboBoxModel) cboCAProdType.getModel()).getKeyForSelected().toString();

        System.out.println("calling filldata#####" + hash);
        if (viewType != -1) {
            if (viewType == CUSTOMER) {
                if (hash.containsKey("CUST_ID")) {
                    hash.put("CUSTOMER ID", hash.get("CUST_ID"));
                    displayShareDetails(hash);
                }
                showingCustomerDetails(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                txtCustID.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
            } else if (viewType == OP_ACC_NUM || viewType == KCC_ACC_NUM) {
                String accountNum = null;
                accountNum = CommonUtil.convertObjToStr(hash.get("ACCOUNTNO"));
                if (accountNum.lastIndexOf("_") == -1) {
                    hash.put("ACCOUNTNO", hash.get("ACCOUNTNO") + "_1");
                }
                String msg = "";
                if (viewType == OP_ACC_NUM) {
                    msg = observable.checkAcNoWithoutProdType(accountNum, "OA");
                    txtCAAccNo.setText(accountNum);
                    lblCAAccName.setText(observable.getLblCAAccName());
                } else if (viewType == KCC_ACC_NUM) {
                    msg = observable.checkAcNoWithoutProdType(accountNum, "AD");
                    txtKCCAccNo.setText(accountNum);
                    lblKCCAccName.setText(observable.getLblKCCAccName());
                }
            }
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            this.setButtonEnableDisable();
            lblSanctionNoVal.setText((CommonUtil.convertObjToStr(hash.get("NCL_SANCTION_NO"))));
            observable.getData(hash);
            HashMap dispMap = new HashMap();
            dispMap.put("CUSTOMER ID", hash.get("CUST_ID"));
            displayShareDetails(dispMap);
            tblSubLimit.setModel(observable.getTblSubLimit());
            tblMemberDetails.setModel(observable.getTblMemberDetails());
            updatesanction();
            ClientUtil.enableDisable(panCADetails, true);
            ClientUtil.enableDisable(panKCCDetails, true);
            btnCAAccNo.setEnabled(true);
            btnKCCAccNo.setEnabled(true);
        } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
            this.setButtonEnableDisable();
            btnReject.setEnabled(true);
            btnException.setEnabled(true);
            lblSanctionNoVal.setText((CommonUtil.convertObjToStr(hash.get("NCL_SANCTION_NO"))));
            observable.getData(hash);
            updatesanction();
            ClientUtil.enableDisable(panSanctionDetails, false);
            ClientUtil.enableDisable(panClassificationDetails, false);
            ClientUtil.enableDisable(panCADetails, false);
            ClientUtil.enableDisable(panKCCDetails, false);
            cboCAProdId.setEnabled(false);
        }
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
            ClientUtil.enableDisable(panSanctionDetails, false);
            ClientUtil.enableDisable(panClassificationDetails, false);
        }
    }

    public void updatesanction() {
        lblSanctionNoVal.setText(observable.getLblSanctionNoVal());
        txtCustID.setText(observable.getTxtCustID());
        tdtSanctionDt.setDateValue(observable.getTdtSanctionDt());
        tdtExpiryDt.setDateValue(observable.getTdtExpiryDt());
        cboCAProdType.setSelectedItem(observable.getCboCAProdType());
        cboCAProdId.setSelectedItem(observable.getCboCAProdId());
        txtCAAccNo.setText(observable.getTxtCAAccNo());
        cboKCCProdId.setSelectedItem(observable.getCboKCCProdId());
        txtKCCAccNo.setText(observable.getTxtKCCAccNo());
        txtRemarks.setText(observable.getTxtRemarks());
        txtSanctionAmt.setText(observable.getTxtSanctionAmt());
        cboCategory.setSelectedItem(observable.getCboCategory());
        cboSubCategory.setSelectedItem(observable.getCboSubCategory());
        txtNoOfMembers.setText(observable.getTxtNoOfMembers());
        txtAmount.setText(observable.getTxtAmount());
        txtTotMembers.setText(observable.getTxtTotMembers());
        showingCustomerDetails(txtCustID.getText());
        calcCategoryTotalMember();
        txtTotMembers.setText(lblTotaMembersVal1.getText());
        totalLimitCalc();
        totalCatAmountCalc();
        loanTableTotmembers();
        displayAccountNameDetails();
        tdtSanctionDt.setDateValue(observable.getTdtSanctionDt());
        lblClassificationSanNoVal.setText(lblSanctionNoVal.getText());
        lblClassificationSanDateVal.setText(tdtSanctionDt.getDateValue());
        lblKCCAccNo_CDVal.setText(txtKCCAccNo.getText());
        lblKCCAccName_CD.setText(lblKCCAccName.getText());
        txtSanctionAmt.setEnabled(true);
//        cboCAProdId.setEnabled(false);
    }

    private void mitRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitRejectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitRejectActionPerformed

    private void mitExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitExceptionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitExceptionActionPerformed

    private void mitAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAuthorizeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mitAuthorizeActionPerformed
            private void btnDelete_BorrowerMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDelete_BorrowerMousePressed
                // Add your handling code here:
    }//GEN-LAST:event_btnDelete_BorrowerMousePressed
//GEN-FIRST:event_btnDelete_BorrowerActionPerformed
//GEN-LAST:event_btnDelete_BorrowerActionPerformed
                                                private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
                                                    // Add your handling code here:
                                                    authEnableDisable();
                                                    observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                                                    authorizeActionPerformed(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void authEnableDisable() {
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        authEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeActionPerformed(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        authEnableDisable();
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeActionPerformed(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnCustIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustIDActionPerformed
        // Add your handling code here:
        viewType = CUSTOMER;
        new CheckCustomerIdUI(this);
    }//GEN-LAST:event_btnCustIDActionPerformed

    private void displayShareDetails(HashMap hash) {
        List shareLst = ClientUtil.executeQuery("getShareAccountDetails", hash);
        if (shareLst != null && shareLst.size() > 0) {
            HashMap shareMap = new HashMap();
            shareMap = (HashMap) shareLst.get(0);
            txtTotalNoOfShare.setText(CommonUtil.convertObjToStr(shareMap.get("NO_OF_SHARES")));
            txtTotalShareAmount.setText(CommonUtil.convertObjToStr(shareMap.get("TOTAL_SHARE_AMOUNT")));
        } else {
            txtTotalNoOfShare.setText("0");
            txtTotalShareAmount.setText("0");
        }
    }
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here: 
        cifClosingAlert();
//        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        btncancelActionPerformed();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btncancelActionPerformed() {
        ClientUtil.clearAll(this);
        setFocusFirstTab();
        observable.resetForm();
        observable.resetSublimitTableValues();
        observable.resetMembertableValues();
        observable.resetLoanTable();
        observable.resetLoantableValues();
        panSubLimit.setEnabled(false);
        setModified(false);
        lblStatus.setText("               ");
        lblCustomerIdValue.setText("");
        lblCustomerNameValue.setText("");
        lblDOBValue.setText("");
        lblStreetValue.setText("");
        lblPhoneNoValue.setText("");
        lblAreaValue.setText("");
        lblCityValue.setText("");
        lblStateValue.setText("");
        lblCountryVaue.setText("");
        lblPincodeValue.setText("");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        btnNew.setEnabled(true);
        btnCancel.setEnabled(false);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnSave.setEnabled(false);
        lblSanctionNoVal.setText("");
        lblTotaMembersVal1.setText("");
        lblTotaMembersVal.setText("");
        lblTotalLimitAmt.setText("");
        lblTotalCatAmtVal.setText("");
        lblCAAccName.setText("");
        lblKCCAccName.setText("");
        observable.resetMemberTable();
        lblClassificationSanNoVal.setText("");
        lblClassificationSanDateVal.setText("");
        lblKCCAccNo_CDVal.setText("");
        lblKCCAccName_CD.setText("");
        btnView.setEnabled(true);
        setFormButtonEnableDisable(false);
        ClientUtil.enableDisable(this, false);
        ClientUtil.enableDisable(panSubLimit, false);
        txtEditSanctionNo.setEnabled(true);
        viewType = -1;
    }

    private void authorizeActionPerformed(String authorizeStatus) {

        if (AuthType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("AUTHORIZE_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZE_DT", ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("NCL_SANCTION_NO", observable.getLblSanctionNoVal());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap, CommonUtil.convertObjToStr(observable.getLblSanctionNoVal()));
            AuthType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            AuthType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getNclSanctionDetailsAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
            btnAuthorize.setEnabled(true);
        }
    }

    public void authorize(HashMap map, String id) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                super.setOpenForEditBy(observable.getStatusBy());
                super.removeEditLock(id);
            }
            btnCancelActionPerformed(null);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_mitNewActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        btndeleteActionPerformed();
    }//GEN-LAST:event_btnDeleteActionPerformed
            private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
                // Add your handling code here:                
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                callView("Edit");
                lblStatus.setText("Edit");
                btnNew_SubLimit.setEnabled(true);
                btnNew_Mem.setEnabled(true);
                btnNew_ASW.setEnabled(true);
    }//GEN-LAST:event_btnEditActionPerformed
                        private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
                            // Add your handling code here:   
                            setModified(false);
                            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSanctionDetails);
                            if (mandatoryMessage.length() > 0) {
                                displayAlert(mandatoryMessage);
                                return;
                            }
                            if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW && tblSubLimit.getRowCount() > 0) {
                                String endYear = CommonUtil.convertObjToStr(tblSubLimit.getValueAt(tblSubLimit.getRowCount() - 1, 0));
                                endYear = endYear.substring(2, 4);
                                observable.setEndYear(endYear);
                            } else {
                                observable.setEndYear("");
                            }
                            double totalAmt = 0;
                            for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
//                                totalAmt = totalAmt + CommonUtil.convertObjToDouble(tblMemberDetails.getValueAt(i, 4)).doubleValue();
                                String Amt = CommonUtil.convertObjToStr(tblMemberDetails.getValueAt(i, 4));
                                Amt = Amt.replace(",", "");
                                totalAmt = totalAmt + CommonUtil.convertObjToDouble(Amt);
                            }
                            double totalLimtAmt = 0;
                            for (int i = 0; i < tblSubLimit.getRowCount(); i++) {
//                                totalLimtAmt = totalLimtAmt + CommonUtil.convertObjToDouble(tblSubLimit.getValueAt(i, 1)).doubleValue();
                                String Amt = CommonUtil.convertObjToStr(tblSubLimit.getValueAt(i, 1));
                                Amt = Amt.replace(",", "");
                                totalLimtAmt = totalLimtAmt + CommonUtil.convertObjToDouble(Amt);
                            }

                            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT && CommonUtil.convertObjToDouble(txtSanctionAmt.getText()) != totalLimtAmt) {
                                ClientUtil.showAlertWindow("Total Limit Amount is different from Sanction Amount!!! ");
                            }

                            if ((CommonUtil.convertObjToInt(txtTotMembers.getText()) == CommonUtil.convertObjToInt(lblTotaMembersVal1.getText()))
                                    && (CommonUtil.convertObjToInt(txtTotMembers.getText()) == CommonUtil.convertObjToInt(lblTotaMembersVal.getText()))) {
                                if (CommonUtil.convertObjToDouble(txtSanctionAmt.getText()) == totalAmt) {
                                    String minAmt = CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(0, 0));
                                    String maxAmt = CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1));
                                    minAmt = minAmt.replace(".00", "");
                                    maxAmt = maxAmt.replace(".00", "");
                                    maxAmt = maxAmt.replace(",", "");
                                    if (minAmt.equals(("1")) && maxAmt.equals("9999999")) {
//                                    if (tblAmtSlabWise.getValueAt(0, 0).equals(CommonUtil.convertObjToDouble("1.00")) && tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1).equals(CommonUtil.convertObjToDouble("99,99,999"))) {
                                        updateOBFields();
                                        savePerformed();
                                        btnCancel.setEnabled(true);
                                    } else {
                                        ClientUtil.showAlertWindow("Slab Should End With Max. Amount 999999999 !!! ");
                                    }
                                } else {
                                    ClientUtil.showAlertWindow("Categorywise Total Amount Should be Equal To sanction Amount!!! ");
                                }
                            } else {
                                ClientUtil.showAlertWindow("Total Members Should be Equal To Total No.of Members !!!");
                            }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void savePerformed() {
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            btnCancelActionPerformed(null);
            btnCancel.setEnabled(true);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
            HashMap resultMap = new HashMap();
            if (observable.getProxyReturnMap() != null) {
                if (observable.getProxyReturnMap().containsKey("NCL_SANCTION_NO")) {
                    resultMap.put("NCL_SANCTION_NO", observable.getProxyReturnMap().get("NCL_SANCTION_NO"));
                }
                if (observable.getResult() == ClientConstants.ACTIONTYPE_NEW) {
                    ClientUtil.showMessageWindow("Sanction No : " + CommonUtil.convertObjToStr(resultMap.get("NCL_SANCTION_NO")));
                }
            }
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

                                        private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
                                            // Add your handling code here:
                                            btnnewActionPerformed();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnnewActionPerformed() {
        btnNewPressed = true;
        observable.resetForm();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        ClientUtil.enableDisable(this, true);// Enables the panel...
        setFocusFirstTab();
        setButtonEnableDisable();
        observable.setStatus();
        setModified(true);
        txtFinancialYear.setEnabled(false);
        txtFinancialYearEnd.setEnabled(false);
        txtSubLimitAmt.setEnabled(false);
        btnSave_SubLimit.setEnabled(false);
        btnDelete_SubLimit.setEnabled(false);
        btnNew_SubLimit.setEnabled(true);
        btnNew_Mem.setEnabled(true);
        cboCategory.setEnabled(false);
        cboSubCategory.setEnabled(false);
        txtNoOfMembers.setEnabled(false);
        txtAmount.setEnabled(false);
        btnCustID.setEnabled(true);
        btnNew_Mem.setEnabled(true);
        btnNew_ASW.setEnabled(true);
        btnKCCAccNo.setEnabled(true);
        btnCAAccNo.setEnabled(true);
        lblStatus.setText(observable.getLblStatus());
        ClientUtil.enableDisable(panShareDetails, false);
        ClientUtil.enableDisable(panAmtSlabWiseDet, false);
    }

    private void cboCAProdTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCAProdTypeActionPerformed
        // TODO add your handling code here:
        populateProdId();
    }//GEN-LAST:event_cboCAProdTypeActionPerformed

    private void btnCAAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCAAccNoActionPerformed
        // TODO add your handling code here:
        popUp(OP_ACC_NUM);
    }//GEN-LAST:event_btnCAAccNoActionPerformed

    private void btnKCCAccNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKCCAccNoActionPerformed
        // TODO add your handling code here:
        popUp(KCC_ACC_NUM);
    }//GEN-LAST:event_btnKCCAccNoActionPerformed

    private void txtCAAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCAAccNoFocusLost
        // TODO add your handling code here:
        if (txtCAAccNo.getText().length() > 0) {
            viewType = OP_ACC_NUM;
            HashMap caMap = new HashMap();
            caMap.put("ACCOUNTNO", txtCAAccNo.getText());
            fillData(caMap);
        }
    }//GEN-LAST:event_txtCAAccNoFocusLost

    private void buttonEnableDisable(boolean flag) {
        btnNew_SubLimit.setEnabled(flag);
        btnSave_SubLimit.setEnabled(flag);
        btnDelete_SubLimit.setEnabled(flag);
    }

    private void buttonEnableDisableMem(boolean flag) {
        btnNew_Mem.setEnabled(flag);
        btnSave_Mem.setEnabled(flag);
        btnDelete_Mem.setEnabled(flag);
    }

    private void buttonEnableDisableLoan(boolean flag) {
        btnNew_ASW.setEnabled(flag);
        btnSave_ASW.setEnabled(flag);
        btnDelete_ASW.setEnabled(flag);
    }
    private void txtKCCAccNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtKCCAccNoFocusLost
        // TODO add your handling code here:
        if (txtKCCAccNo.getText().length() > 0) {
            viewType = KCC_ACC_NUM;
            HashMap caMap = new HashMap();
            caMap.put("ACCOUNTNO", txtKCCAccNo.getText());
            fillData(caMap);
        }
    }//GEN-LAST:event_txtKCCAccNoFocusLost

    private void tdtSanctionDtAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tdtSanctionDtAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtSanctionDtAncestorAdded

    private void tdtSanctionDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtSanctionDtFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tdtSanctionDtFocusLost

    private void tdtExpiryDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtExpiryDtFocusLost
        // TODO add your handling code here:      
        if ((tdtSanctionDt != null && tdtSanctionDt.getDateValue().length() > 0) && (tdtExpiryDt != null && tdtExpiryDt.getDateValue().length() > 0)) {
            if ((DateUtil.getDateMMDDYYYY(tdtSanctionDt.getDateValue())).compareTo(DateUtil.getDateMMDDYYYY(tdtExpiryDt.getDateValue())) >= 0) {
                ClientUtil.showAlertWindow("Expiry Date Should Be Greater than Sanction Date !!!");
                tdtExpiryDt.setDateValue("");
            }
        }
    }//GEN-LAST:event_tdtExpiryDtFocusLost

    private void btnNew_SubLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_SubLimitActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        buttonEnableDisable(false);
        btnSave_SubLimit.setEnabled(true);
        btnDelete_SubLimit.setEnabled(true);
        observable.setNewData(true);
        txtFinancialYear.setEnabled(true);
        txtFinancialYearEnd.setEnabled(true);
        txtSubLimitAmt.setEnabled(true);
        setFinancialYears();
    }//GEN-LAST:event_btnNew_SubLimitActionPerformed

    private void setFinancialYears() {
        if (tblSubLimit.getRowCount() <= 0) {
            String year1 = DateUtil.getStringDate(curDate);
            year1 = year1.replace("/", "");
            year1 = year1.substring(4, 8);
            int end = CommonUtil.convertObjToInt(year1);
            end = end + 1;
            txtFinancialYear.setText(year1);
            txtFinancialYearEnd.setText(CommonUtil.convertObjToStr(end));
        } else {
            //Added By Suresh
            if (tblSubLimit.getRowCount() > 0) {
                String endYear = CommonUtil.convertObjToStr(tblSubLimit.getValueAt(tblSubLimit.getRowCount() - 1, 0));
                //Changed By Suresh
                endYear = endYear.substring(5, 9);
//                System.out.println("############ endYear : " + endYear);
                txtFinancialYear.setText(String.valueOf(endYear));
                txtFinancialYearEnd.setText(String.valueOf(CommonUtil.convertObjToInt(endYear) + 1));
            }
        }
    }

    private void btndeleteActionPerformed() {
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        callView("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panSanctionCustDet, false);
    }

    private void btnSave_SubLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_SubLimitActionPerformed
        // TODO add your handling code here:            
        try {
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panSubLimitDetails);
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
                return;
            }
            Double seqYear = CommonUtil.convertObjToDouble(txtFinancialYearEnd.getText()) - CommonUtil.convertObjToDouble(txtFinancialYear.getText());
            if (seqYear == 1) {
                if (!updateMode) {
                    for (int i = 0; i < tblSubLimit.getRowCount(); i++) {
                        if (tblSubLimit.getValueAt(i, 0).equals(CommonUtil.convertObjToDouble(txtFinancialYear.getText()))) {
                            ClientUtil.showAlertWindow("Same Financial Years Not Allowed !!!");
                            return;
                        }
                    }
                }
                updateOBFields();
                String finYear1 = txtFinancialYear.getText();
                String finYear2 = txtFinancialYearEnd.getText();
                String limit = txtSubLimitAmt.getText();
                if (finYear1.length() > 0 && finYear2.length() > 0 && limit.length() > 0) {
                    observable.addDataToSublimitTable(updateTab, updateMode);
                    totalLimitCalc();
                    tblSubLimit.setModel(observable.getTblSubLimit());
                    observable.resetSublimitTable();
                    resetSublimitTable();
                    ClientUtil.enableDisable(panSubLimit, false);
                    buttonEnableDisable(false);
                    btnNew_SubLimit.setEnabled(true);
                    txtFinancialYear.setEnabled(false);
                    txtFinancialYearEnd.setEnabled(false);
                    txtSubLimitAmt.setEnabled(false);
                } else {
                    ClientUtil.showAlertWindow("Financial Years,Limit Should Not Be empty!!!");
                }
            } else {
                ClientUtil.showAlertWindow("Enter Proper Financial Year !!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSave_SubLimitActionPerformed

    private void resetSublimitTable() {
        txtFinancialYear.setText("");
        txtFinancialYearEnd.setText("");
        txtSubLimitAmt.setText("");
    }

    private void resetMemberTable() {
        txtAmount.setText("");
        txtNoOfMembers.setText("");
        cboCategory.setSelectedItem("");
        cboSubCategory.setSelectedItem("");
    }

    private void resetLoanTable() {
        txtSlabNoOfMembers.setText("");
        cboFromAmount.setSelectedItem("");
        cboToAmount.setSelectedItem("");
    }

    private void txtFinancialYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFinancialYearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFinancialYearActionPerformed

    private void tblSubLimitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSubLimitMousePressed
        // TODO add your handling code here:   
        if (tblSubLimit.getRowCount() > 0) {
            updateOBFields();
            updateMode = true;
            updateTab = tblSubLimit.getSelectedRow();
            observable.setNewData(false);
            String st = CommonUtil.convertObjToStr(tblSubLimit.getValueAt(tblSubLimit.getSelectedRow(), 0));
            //Changed By Suresh
            st = st.substring(0, st.indexOf("-"));
            ClientUtil.enableDisable(panSubLimit, false);
            txtSubLimitAmt.setEnabled(true);
            observable.populateSublimitTableDetails(st);
            sublimitTableUpdate();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                buttonEnableDisable(false);
                ClientUtil.enableDisable(panSubLimit, false);
            } else {
                buttonEnableDisable(true);
                btnNew_SubLimit.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tblSubLimitMousePressed

    private void btnDelete_SubLimitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_SubLimitActionPerformed
        // TODO add your handling code here:
        if (tblSubLimit.getSelectedRow() == tblSubLimit.getRowCount() - 1) {
            String st = CommonUtil.convertObjToStr(tblSubLimit.getValueAt(tblSubLimit.getSelectedRow(), 0));
            //Changed By Suresh
            st = st.substring(0, st.indexOf("-"));
            observable.deleteSublimitTableData(st, tblSubLimit.getSelectedRow());
            totalLimitCalc();
            observable.resetSublimitTable();
            resetSublimitTable();
            buttonEnableDisable(false);
            btnNew_SubLimit.setEnabled(true);
        }
    }//GEN-LAST:event_btnDelete_SubLimitActionPerformed

    private void btnNew_MemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_MemActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        buttonEnableDisableMem(true);
        btnNew_Mem.setEnabled(false);
        observable.setMembereNewData(true);
        cboCategory.setEnabled(true);
        cboSubCategory.setEnabled(true);
        txtNoOfMembers.setEnabled(true);
        txtAmount.setEnabled(true);
    }//GEN-LAST:event_btnNew_MemActionPerformed

    private void btnSave_MemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_MemActionPerformed
        // TODO add your handling code here:
        try {
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAmtSlabWiseDet1);
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
                return;
            }
            double totalMember = 0;
            if (updateMode) {
                totalMember = CommonUtil.convertObjToDouble(lblTotaMembersVal1.getText()) + CommonUtil.convertObjToDouble(txtNoOfMembers.getText()) - CommonUtil.convertObjToDouble(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 3));
            } else {
                totalMember = CommonUtil.convertObjToDouble(lblTotaMembersVal1.getText()) + CommonUtil.convertObjToDouble(txtNoOfMembers.getText());
            }
            double totalAmt = 0.0;
            if (tblMemberDetails.getRowCount() > 0) {
                for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
                    totalAmt = totalAmt + CommonUtil.convertObjToDouble(tblMemberDetails.getValueAt(i, 4)).doubleValue();
                }
                if (updateMode) {
                    totalAmt = totalAmt - CommonUtil.convertObjToDouble(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 4)).doubleValue();
                }
            }
            totalAmt += CommonUtil.convertObjToDouble(txtAmount.getText());
            if (CommonUtil.convertObjToDouble(totalMember) <= (CommonUtil.convertObjToDouble(txtTotMembers.getText()))) {
                if (CommonUtil.convertObjToDouble(txtSanctionAmt.getText()) >= CommonUtil.convertObjToDouble(totalAmt)) {
                    updateMemberOBFields();
                    if (cboCategory.getSelectedIndex() > 0 && cboSubCategory.getSelectedIndex() > 0 && txtAmount.getText().length() > 0) {
                        if (!updateMode) {
                            if (tblMemberDetails.getRowCount() > 0) {
                                for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
                                    if ((tblMemberDetails.getValueAt(i, 1).equals(CommonUtil.convertObjToStr(cboCategory.getSelectedItem())) && (tblMemberDetails.getValueAt(i, 2).equals(CommonUtil.convertObjToStr(cboSubCategory.getSelectedItem()))))) {
                                        ClientUtil.showAlertWindow("Category and Subcategory Combination Should Not Be Repeated !!!");
                                        return;
                                    }
                                }
                            }
                        }
                        observable.addDataToMemberDetailsTable(updateTab, updateMode);
                        totalCatAmountCalc();
                        tblMemberDetails.setModel(observable.getTblMemberDetails());
                        calcCategoryTotalMember();
                        observable.resetMemberTable();
                        resetMemberTable();
                        txtTotMembers.setEnabled(false);
                        ClientUtil.enableDisable(panMemberDetails, false);
                        buttonEnableDisableMem(false);
                        btnNew_Mem.setEnabled(true);
                    }
                } else {
                    ClientUtil.showAlertWindow("Categorywise Total Amount Should Not Exceed Sanction Amount !!!");
                    txtAmount.setText("");
                    return;
                }
            } else {
                ClientUtil.showAlertWindow("Categorywise Number Of Members Should Not Exceed Total No .Of Members !!!");
                txtNoOfMembers.setText("");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSave_MemActionPerformed

    private void calcCategoryTotalMember() {
        double totAmt = 0.0;
        if (tblMemberDetails.getRowCount() > 0) {
            for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblMemberDetails.getValueAt(i, 3)).doubleValue();
            }
        }
        lblTotaMembersVal1.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
    }

    private double loanTableTotmembers() {
        double totAmt = 0.0;
        if (tblAmtSlabWise.getRowCount() > 0) {
            for (int i = 0; i < tblAmtSlabWise.getRowCount(); i++) {
                totAmt = totAmt + CommonUtil.convertObjToDouble(tblAmtSlabWise.getValueAt(i, 2)).doubleValue();
            }
        }
        lblTotaMembersVal.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        return totAmt;
    }

    private double totalLimitCalc() {
        double totAmt = 0.0;
        if (tblSubLimit.getRowCount() > 0) {
            for (int i = 0; i < tblSubLimit.getRowCount(); i++) {
//                totAmt = totAmt + CommonUtil.convertObjToDouble(tblSubLimit.getValueAt(i, 1)).doubleValue();
                String Amt = CommonUtil.convertObjToStr(tblSubLimit.getValueAt(i, 1));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
        }
        lblTotalLimitAmt.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        return totAmt;
    }

    private double totalCatAmountCalc() {
        double totAmt = 0.0;
        if (tblMemberDetails.getRowCount() > 0) {
            for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
//                totAmt = totAmt + CommonUtil.convertObjToDouble(tblSubLimit.getValueAt(i, 1)).doubleValue();
                String Amt = CommonUtil.convertObjToStr(tblMemberDetails.getValueAt(i, 4));
                Amt = Amt.replace(",", "");
                totAmt = totAmt + CommonUtil.convertObjToDouble(Amt);
            }
        }
        lblTotalCatAmtVal.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        return totAmt;
    }

    private void btnDelete_MemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_MemActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 0));
        observable.deleteMemberTableData(st, tblMemberDetails.getSelectedRow());
        calcCategoryTotalMember();
        totalCatAmountCalc();
        observable.resetMemberTable();
        resetMemberTable();
        buttonEnableDisableMem(false);
        btnNew_Mem.setEnabled(true);
    }//GEN-LAST:event_btnDelete_MemActionPerformed

    private void btnNew_ASWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_ASWActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        buttonEnableDisableLoan(false);
        btnSave_ASW.setEnabled(true);
        btnDelete_ASW.setEnabled(true);
        observable.setLoanNewData(true);
        cboFromAmount.setEnabled(true);
        cboToAmount.setEnabled(true);
        txtSlabNoOfMembers.setEnabled(true);
        if (tblAmtSlabWise.getRowCount() <= 0) {
            cboFromAmount.setSelectedIndex(1);
        } else {
//            String Amt=CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1));
//            Amt=Amt.replace(".00", "");
//            cboFromAmount.setSelectedItem(String.valueOf(Amt));
            cboFromAmount.setSelectedItem(String.valueOf(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1)).replace(".00", ""));
        }
        cboFromAmount.setEnabled(false);
    }//GEN-LAST:event_btnNew_ASWActionPerformed

    private void btnSave_ASWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_ASWActionPerformed
        // TODO add your handling code here:        
        try {
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panAmtSlabWiseDet);
            if (mandatoryMessage.length() > 0) {
                displayAlert(mandatoryMessage);
                return;
            }
            double totalMember = 0;
            if (updateMode) {
                totalMember = CommonUtil.convertObjToDouble(lblTotaMembersVal.getText()) + CommonUtil.convertObjToDouble(txtSlabNoOfMembers.getText()) - CommonUtil.convertObjToDouble(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getSelectedRow(), 2));
            } else {
                totalMember = CommonUtil.convertObjToDouble(lblTotaMembersVal.getText()) + CommonUtil.convertObjToDouble(txtSlabNoOfMembers.getText());
            }
            if ((CommonUtil.convertObjToDouble(totalMember)) <= (CommonUtil.convertObjToDouble(txtTotMembers.getText()))) {
                updateLoanOBFields();
                String frm_amt = CommonUtil.convertObjToStr(cboFromAmount.getSelectedItem());
                String to_amt = CommonUtil.convertObjToStr(cboToAmount.getSelectedItem());
                String tot_members = txtSlabNoOfMembers.getText();
                if (frm_amt.length() > 0 && to_amt.length() > 0 && tot_members.length() > 0) {
                    observable.addDataToLoanAmtSlabWiseTable(updateTab, updateMode);
                    loanTableTotmembers();
                    tblAmtSlabWise.setModel(observable.getTbltblAmtSlabWise());
                    observable.resetLoanTable();
                    resetLoanTable();
                    ClientUtil.enableDisable(panAmtSlabWise, false);
                    buttonEnableDisableLoan(false);
                    btnNew_ASW.setEnabled(true);
                    cboFromAmount.setEnabled(false);
                    cboToAmount.setEnabled(false);
                    txtSlabNoOfMembers.setEnabled(false);
                } else {
                    ClientUtil.showAlertWindow("Amounts,No.Of Members Should Not Be empty!!!");
                }
            } else {
                ClientUtil.showAlertWindow("LoanAmountSlabwise Number Of Members Should Not Exceed Total No .Of Members !!!");
                txtSlabNoOfMembers.setText("");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSave_ASWActionPerformed

    private void btnDelete_ASWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_ASWActionPerformed
        // TODO add your handling code here:
        if (tblAmtSlabWise.getSelectedRow() == tblAmtSlabWise.getRowCount() - 1) {
            String st = CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getSelectedRow(), 0));
            st = st.replace(".00", "");
            st = st.replace(",", "");
            observable.deleteloanTableData(st, tblAmtSlabWise.getSelectedRow());
            loanTableTotmembers();
            observable.resetLoanTable();
            resetLoanTable();
            buttonEnableDisableLoan(false);
            btnNew_ASW.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("Delete From Last Row!!!");
            return;
        }
    }//GEN-LAST:event_btnDelete_ASWActionPerformed

    private void displayAccountNameDetails() {
        if (txtCAAccNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", txtCAAccNo.getText());
            List accountLst = (List) ClientUtil.executeQuery("getAllCustomerName", whereMap);
            if (accountLst != null && accountLst.size() > 0) {
                whereMap = (HashMap) accountLst.get(0);
                lblCAAccName.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_NAME")));
            }
        }
        if (txtKCCAccNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", txtKCCAccNo.getText());
            List accountLst = (List) ClientUtil.executeQuery("getAllCustomerName", whereMap);
            if (accountLst != null && accountLst.size() > 0) {
                whereMap = (HashMap) accountLst.get(0);
                lblKCCAccName.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_NAME")));
            }
        }
    }

    private void cboKCCProdIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboKCCProdIdActionPerformed
        // TODO add your handling code here:
//        btnKCCAccNo.setEnabled(true);
    }//GEN-LAST:event_cboKCCProdIdActionPerformed

    private void tblMemberDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberDetailsMousePressed
        // TODO add your handling code here: 
        if (tblMemberDetails.getRowCount() > 0) {
            updateOBFields();
            updateMode = true;
            updateTab = tblMemberDetails.getSelectedRow();
            observable.setMembereNewData(false);
            int st = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 0));
            ClientUtil.enableDisable(panAmtSlabWiseDet1, true);
            observable.populateMemberTableDetails(st);
            memberTableUpdate();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                buttonEnableDisableMem(false);
                ClientUtil.enableDisable(panMemberDetails, false);
            } else {
                buttonEnableDisableMem(true);
                btnNew_Mem.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tblMemberDetailsMousePressed

    private void tblAmtSlabWiseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAmtSlabWiseMousePressed
        // TODO add your handling code here:
        if (tblAmtSlabWise.getRowCount() > 0 && tblAmtSlabWise.getSelectedRow() != -1) {
            updateMode = true;
            updateTab = tblAmtSlabWise.getSelectedRow();
            observable.setNewData(false);
            String st = CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getSelectedRow(), 0));
            st = st.replace(".00", "");
            st = st.replace(",", "");
            ClientUtil.enableDisable(panAmtSlabWise, true);
            cboFromAmount.setEnabled(false);
            cboToAmount.setEnabled(true);
            observable.populateLoanTableDetails(st);
            loanTableUpdate();
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION) {
                buttonEnableDisableLoan(false);
                ClientUtil.enableDisable(panAmtSlabWise, false);
            } else {
                buttonEnableDisableLoan(true);
                btnNew_ASW.setEnabled(false);
            }
        }
    }//GEN-LAST:event_tblAmtSlabWiseMousePressed

    private void cboToAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboToAmountFocusLost
        // TODO add your handling code here:
        if (cboFromAmount.getSelectedIndex() > 0 && cboToAmount.getSelectedIndex() > 0) {
            String fAmt = CommonUtil.convertObjToStr(cboFromAmount.getSelectedItem());
            String tAmt = CommonUtil.convertObjToStr(cboToAmount.getSelectedItem());
            fAmt = fAmt.replace(",", "");
            tAmt = tAmt.replace(",", "");
            Double frmAmt = CommonUtil.convertObjToDouble(fAmt);
            Double toAmt = CommonUtil.convertObjToDouble(tAmt);
            if (CommonUtil.convertObjToDouble(frmAmt) >= CommonUtil.convertObjToDouble(toAmt)) {
                ClientUtil.showAlertWindow("To Amount Should be Greater Than From Amount");
                cboToAmount.setSelectedItem("");
            }
            return;
        }
    }//GEN-LAST:event_cboToAmountFocusLost

    private void txtSanctionAmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSanctionAmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSanctionAmtActionPerformed

    private void txtAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountFocusLost
        // TODO add your handling code here:                  
    }//GEN-LAST:event_txtAmountFocusLost

    public void sublimitTableUpdate() {
        txtFinancialYear.setText(observable.getTxtFinancialYear());
        txtFinancialYearEnd.setText(observable.getTxtFinancialYearEnd());
        txtSubLimitAmt.setText(observable.getTxtSubLimitAmt());
    }

    public void memberTableUpdate() {
        txtAmount.setText(observable.getTxtAmount());
        txtNoOfMembers.setText(observable.getTxtNoOfMembers());
        cboSubCategory.setSelectedItem(observable.getCboSubCategory());
        cboCategory.setSelectedItem(observable.getCboCategory());
    }

    public void loanTableUpdate() {
        txtSlabNoOfMembers.setText(observable.getTxtSlabNoOfMembers());
        cboFromAmount.setSelectedItem(observable.getCboFromAmount().replace(".00", ""));
        cboToAmount.setSelectedItem(observable.getCboToAmount().replace(".00", ""));
    }

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        final HashMap where_map = new HashMap();
        viewType = field;
        if (field == OP_ACC_NUM) {
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboCAProdType).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountList" + observable.getCbmCAProdType().getKeyForSelected().toString());
            where_map.put(PROD_ID, observable.getCbmCAProdId().getKeyForSelected());
            where_map.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
            new ViewAll(this, "Operative A/c", viewMap).show();
        }
        if (field == KCC_ACC_NUM) {
            String prodType = CommonUtil.convertObjToStr((((ComboBoxModel) (cboCAProdType).getModel())).getKeyForSelected());
            viewMap.put(CommonConstants.MAP_NAME, "Cash.getAccountListAD");
            where_map.put(PROD_ID, observable.getCbmKCCProdId().getKeyForSelected());
            where_map.put("SELECTED_BRANCH", ProxyParameters.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, where_map);
            new ViewAll(this, "KCC A/c", viewMap).show();
        }
    }

    private void callView(String currAction) {
        view = currAction;
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNclSanctionDetailsEdit");
        } else if (currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNclSanctionDetailsDelete");
        }
        new ViewAll(this, viewMap).show();
    }

    private void populateProdId() {
        String prodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboCAProdType.getModel()).getKeyForSelected());
        if (!prodType.equals("")) {
            if (prodType.equals("GL")) {
                cboCAProdId.setEnabled(false);
                lblCAAccNo.setText("Account Head");
                cboCAProdId.setSelectedItem("");
            } else {
//                cboCAProdId.setEnabled(true);
                lblCAAccNo.setText(resourceBundle.getString("lblCAAccNo"));
                observable.setCbmCAProdId(prodType);
                cboCAProdId.setModel(observable.getCbmCAProdId());
//                btnCAAccNo.setEnabled(true);
            }
        }
    }

    private void setFocusFirstTab() {
        tabLimitAmount.setSelectedIndex(0);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel PanAcc_CD;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCAAccNo;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete_ASW;
    private com.see.truetransact.uicomponent.CButton btnDelete_Mem;
    private com.see.truetransact.uicomponent.CButton btnDelete_SubLimit;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnKCCAccNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_ASW;
    private com.see.truetransact.uicomponent.CButton btnNew_Mem;
    private com.see.truetransact.uicomponent.CButton btnNew_SubLimit;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_ASW;
    private com.see.truetransact.uicomponent.CButton btnSave_Mem;
    private com.see.truetransact.uicomponent.CButton btnSave_SubLimit;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCAProdId;
    private com.see.truetransact.uicomponent.CComboBox cboCAProdType;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboFromAmount;
    private com.see.truetransact.uicomponent.CComboBox cboKCCProdId;
    private com.see.truetransact.uicomponent.CComboBox cboSubCategory;
    private com.see.truetransact.uicomponent.CComboBox cboToAmount;
    private com.see.truetransact.uicomponent.CLabel lblAmount;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblAreaValue;
    private com.see.truetransact.uicomponent.CLabel lblCAAccName;
    private com.see.truetransact.uicomponent.CLabel lblCAAccNo;
    private com.see.truetransact.uicomponent.CLabel lblCAProdId;
    private com.see.truetransact.uicomponent.CLabel lblCAProdType;
    private com.see.truetransact.uicomponent.CLabel lblCategory;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCityValue;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanDate;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanDateVal;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanNo;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanNoVal;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblCountryVaue;
    private com.see.truetransact.uicomponent.CLabel lblCustID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerId;
    private com.see.truetransact.uicomponent.CLabel lblCustomerIdValue;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName1;
    private com.see.truetransact.uicomponent.CLabel lblCustomerNameValue;
    private com.see.truetransact.uicomponent.CLabel lblDOB;
    private com.see.truetransact.uicomponent.CLabel lblDOBValue;
    private com.see.truetransact.uicomponent.CLabel lblDash;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDt;
    private com.see.truetransact.uicomponent.CLabel lblFinancialYear;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccName;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccName_CD;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccNo;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccNo_CD;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccNo_CDVal;
    private com.see.truetransact.uicomponent.CLabel lblKCCProdId;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNoOfMembers;
    private com.see.truetransact.uicomponent.CLabel lblPanNumber1;
    private com.see.truetransact.uicomponent.CLabel lblPhoneNoValue;
    private com.see.truetransact.uicomponent.CLabel lblPincode;
    private com.see.truetransact.uicomponent.CLabel lblPincode1;
    private com.see.truetransact.uicomponent.CLabel lblPincodeValue;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSanctionAmt;
    private com.see.truetransact.uicomponent.CLabel lblSanctionDt;
    private com.see.truetransact.uicomponent.CLabel lblSanctionNo;
    private com.see.truetransact.uicomponent.CLabel lblSanctionNoVal;
    private com.see.truetransact.uicomponent.CLabel lblSlabNoOfMembers;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace6;
    private com.see.truetransact.uicomponent.CLabel lblSpace7;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblSpace9;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStateValue;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CLabel lblStreetValue;
    private com.see.truetransact.uicomponent.CLabel lblSubCategory;
    private com.see.truetransact.uicomponent.CLabel lblSubLimitAmt;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotMembers;
    private com.see.truetransact.uicomponent.CLabel lblTotaMembersVal;
    private com.see.truetransact.uicomponent.CLabel lblTotaMembersVal1;
    private com.see.truetransact.uicomponent.CLabel lblTotalCatAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalCatAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalLimit;
    private com.see.truetransact.uicomponent.CLabel lblTotalLimitAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalMembers;
    private com.see.truetransact.uicomponent.CLabel lblTotalMembers1;
    private com.see.truetransact.uicomponent.CLabel lblTotalNoOfShare;
    private com.see.truetransact.uicomponent.CLabel lblTotalShareAmount;
    private com.see.truetransact.uicomponent.CLabel lblspace3;
    private com.see.truetransact.uicomponent.CMenuBar mbrTermLoan;
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
    private com.see.truetransact.uicomponent.CPanel panAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panAmtSlabWise;
    private com.see.truetransact.uicomponent.CPanel panAmtSlabWiseDet;
    private com.see.truetransact.uicomponent.CPanel panAmtSlabWiseDet1;
    private com.see.truetransact.uicomponent.CPanel panButton2_SD2;
    private com.see.truetransact.uicomponent.CPanel panButton_AmtSlabWise;
    private com.see.truetransact.uicomponent.CPanel panButton_AmtSlabWise1;
    private com.see.truetransact.uicomponent.CPanel panCAAccNo;
    private com.see.truetransact.uicomponent.CPanel panCADetails;
    private com.see.truetransact.uicomponent.CPanel panClassDetails_Acc;
    private com.see.truetransact.uicomponent.CPanel panClassDetails_Details;
    private com.see.truetransact.uicomponent.CPanel panClassMembers;
    private com.see.truetransact.uicomponent.CPanel panClassificationDetails;
    private com.see.truetransact.uicomponent.CPanel panKCCAccNo;
    private com.see.truetransact.uicomponent.CPanel panKCCDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panProd_CD;
    private com.see.truetransact.uicomponent.CPanel panSanction;
    private com.see.truetransact.uicomponent.CPanel panSanctionCustDet;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails;
    private com.see.truetransact.uicomponent.CPanel panShareDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panSubLimit;
    private com.see.truetransact.uicomponent.CPanel panSubLimitAcDetails;
    private com.see.truetransact.uicomponent.CPanel panSubLimitDetails;
    private com.see.truetransact.uicomponent.CPanel panTable_ASW;
    private com.see.truetransact.uicomponent.CPanel panTable_ASW1;
    private com.see.truetransact.uicomponent.CPanel panTable_SubLimit;
    private com.see.truetransact.uicomponent.CPanel panTermLoan;
    private com.see.truetransact.uicomponent.CPanel panViewPhotoSign;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccLimit;
    private com.see.truetransact.uicomponent.CButtonGroup rdoAccType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCourtOrderGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDirectRepaymentGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoDoAddSIs;
    private com.see.truetransact.uicomponent.CButtonGroup rdoEnhanceGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoExecuted_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoGuarnConstution;
    private com.see.truetransact.uicomponent.CButtonGroup rdoInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoIsSubmitted_DocumentDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMandatory_DOC;
    private com.see.truetransact.uicomponent.CButtonGroup rdoMultiDisburseAllow;
    private com.see.truetransact.uicomponent.CButtonGroup rdoNatureInterest;
    private com.see.truetransact.uicomponent.CButtonGroup rdoPostDatedCheque;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRebateInterestGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRenewalGroup;
    private com.see.truetransact.uicomponent.CButtonGroup rdoRiskWeight;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSHG;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSalaryRecovery;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityDetails;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSecurityType;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoStatus_Repayment;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidy;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSubsidyAddMinusGroup;
    private com.see.truetransact.uicomponent.CSeparator sptBorroewrProfile;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptClassDetails;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CSeparator srpComp_Tab_Addr;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ASW;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ASW1;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_SubLimit;
    private com.see.truetransact.uicomponent.CTabbedPane tabLimitAmount;
    private com.see.truetransact.uicomponent.CTable tblAmtSlabWise;
    private com.see.truetransact.uicomponent.CTable tblMemberDetails;
    private com.see.truetransact.uicomponent.CTable tblSubLimit;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdtExpiryDt;
    private com.see.truetransact.uicomponent.CDateField tdtSanctionDt;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtCAAccNo;
    private com.see.truetransact.uicomponent.CTextField txtCustID;
    private com.see.truetransact.uicomponent.CTextField txtEditSanctionNo;
    private com.see.truetransact.uicomponent.CTextField txtFinancialYear;
    private com.see.truetransact.uicomponent.CTextField txtFinancialYearEnd;
    private com.see.truetransact.uicomponent.CTextField txtKCCAccNo;
    private com.see.truetransact.uicomponent.CTextField txtNoOfMembers;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtSanctionAmt;
    private com.see.truetransact.uicomponent.CTextField txtSlabNoOfMembers;
    private com.see.truetransact.uicomponent.CTextField txtSubLimitAmt;
    private com.see.truetransact.uicomponent.CTextField txtTotMembers;
    private com.see.truetransact.uicomponent.CTextField txtTotalNoOfShare;
    private com.see.truetransact.uicomponent.CTextField txtTotalShareAmount;
    // End of variables declaration//GEN-END:variables
}
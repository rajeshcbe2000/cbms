/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ReleaseDetailsUI.java
 *
 * Created on November 28, 2003, 3:55 PM
 */
package com.see.truetransact.ui.termloan.kcctopacs;

import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observable;
import java.util.GregorianCalendar;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.ui.customer.CheckCustomerIdUI;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.ui.common.authorize.AuthorizeUI;
import com.see.truetransact.commonutil.interestcalc.Rounding;
import com.see.truetransact.ui.common.viewall.AuthorizeListUI;
import com.see.truetransact.clientexception.ClientParseException;

/*
 *
 * @author Suresh R
 * Created on Apr 17, 2013, 5:35 PM
 *
 */
public class ReleaseDetailsUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer, UIMandatoryField {
//        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.mdsapplication.mdschangeofmember.MDSChangeofMemberRB", ProxyParameters.LANGUAGE);

    private final static ClientParseException parseException = ClientParseException.getInstance();
//        private MDSChangeofMemberMRB objMandatoryRB = new MDSChangeofMemberMRB();
    private String viewType = new String();
    ReleaseDetailsOB observable = null;
    final String AUTHORIZE = "Authorize";
    private int yearTobeAdded = 1900;
    HashMap mandatoryMap = null;
    boolean isFilled = false;
    private Date curDate = null;
    private boolean updateMode = false;
    int updateTab = -1;
    private Date curr_dt = null;
    private boolean update = true;

    /**
     * Creates new form ReleaseDetailsUI
     */
    public ReleaseDetailsUI() {
        initComponents();
        initForm();
    }

    private void initForm() {
        setFieldNames();
        observable = new ReleaseDetailsOB();
        setMaxLengths();
        initComponentData();
        setMandatoryHashMap();
        setButtonEnableDisable();
        curDate = ClientUtil.getCurrentDate();
        ClientUtil.enableDisable(panReleaseDetails, false);
        ClientUtil.enableDisable(panClassificationDetails, false);
        ClientUtil.enableDisable(panDocumentDetails, false);
        enableDisableButtons(false);
        lblCustomerName.setText("");
        lbOperativeAcName.setText("");
        lbKCCAcName.setText("");
        buttonEnableDisableLoan(false);
        buttonEnableDisableMem(false);
        setSizeTableData();
//        btnMembershipLia.setVisible(false);
        lblPanNumber1.setVisible(false);
        txtEditTermLoanNo.setVisible(false);
    }

    private void setSizeTableData() {
        tblAmtSlabWise.getColumnModel().getColumn(0).setPreferredWidth(15);
        tblAmtSlabWise.getColumnModel().getColumn(1).setPreferredWidth(40);
        tblAmtSlabWise.getColumnModel().getColumn(2).setPreferredWidth(35);
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
    }

    public void update() {
        lblReleaseNoVal.setText(observable.getLblReleaseNo());
        txtNCLSanctionNo.setText(observable.getTxtNCLSanctionNo());
        txtCustomerID.setText(observable.getTxtCustomerID());
        txtAmountRequested.setText(observable.getTxtAmountRequested());
        tdRequestedDt.setDateValue(observable.getTdRequestedDt());
        txtAmountReleased.setText(observable.getTxtAmountReleased());
        tdtReleaseDt.setDateValue(observable.getTdtReleaseDt());
        txtRepaymentPeriod.setText(observable.getTxtRepaymentPeriod());
        cboRepaymentPeriod.setSelectedItem(observable.getCboRepaymentPeriod());
        tdtDueDt.setDateValue(observable.getTdtDueDt());
        txtNoOFInst.setText(observable.getTxtNoOFInst());
        cboPrincipalFrequency.setSelectedItem(observable.getCboPrincipalFrequency());
        cboIntFrequency.setSelectedItem(observable.getCboIntFrequency());
        txtRateOfInterest.setText(observable.getTxtRateOfInterest());
        txtPenalInterest.setText(observable.getTxtPenalInterest());
        cboLoanCategory.setSelectedItem(observable.getCboLoanCategory());
        cboSubCategory.setSelectedItem(observable.getCboSubCategory());
        cboCrop.setSelectedItem(observable.getCboCrop());
        txtRemarks.setText(observable.getTxtRemarks());
        lblTotalInterestPayableVal.setText(observable.getLblTotalInterestPayable());
        lblTotalAmountPayableVal.setText(observable.getLblTotalAmountPayable());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setLblReleaseNo(lblReleaseNoVal.getText());
        observable.setTxtNCLSanctionNo(txtNCLSanctionNo.getText());
        observable.setTxtCustomerID(txtCustomerID.getText());
        observable.setTxtAmountRequested(txtAmountRequested.getText());
        observable.setTdRequestedDt(tdRequestedDt.getDateValue());
        observable.setTxtAmountReleased(txtAmountReleased.getText());
        observable.setTdtReleaseDt(tdtReleaseDt.getDateValue());
        observable.setTxtRepaymentPeriod(txtRepaymentPeriod.getText());
        observable.setCboRepaymentPeriod(CommonUtil.convertObjToStr(cboRepaymentPeriod.getSelectedItem()));
        observable.setTdtDueDt(tdtDueDt.getDateValue());
        observable.setTxtNoOFInst(txtNoOFInst.getText());
        observable.setCboPrincipalFrequency(CommonUtil.convertObjToStr(cboPrincipalFrequency.getSelectedItem()));
        observable.setCboIntFrequency(CommonUtil.convertObjToStr(cboIntFrequency.getSelectedItem()));
        observable.setTxtRateOfInterest(txtRateOfInterest.getText());
        observable.setTxtPenalInterest(txtPenalInterest.getText());
        observable.setCboLoanCategory(CommonUtil.convertObjToStr(cboLoanCategory.getSelectedItem()));
        observable.setCboSubCategory(CommonUtil.convertObjToStr(cboSubCategory.getSelectedItem()));
        observable.setCboCrop(CommonUtil.convertObjToStr(cboCrop.getSelectedItem()));
        observable.setTxtRemarks(txtRemarks.getText());
        observable.setLblTotalInterestPayable(lblTotalInterestPayableVal.getText());
        observable.setLblTotalAmountPayable(lblTotalAmountPayableVal.getText());
        observable.setTxtTotMembers(txtTotMembers.getText());
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
    }

    /**
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    private void setMaxLengths() {
        txtCustomerID.setAllowAll(true);
        txtNCLSanctionNo.setAllowAll(true);
        txtSanctionAmount.setValidation(new CurrencyValidation(14, 2));
        txtUnUsedAmount.setValidation(new CurrencyValidation(14, 2));
        txtAmountRequested.setValidation(new CurrencyValidation(14, 2));
        txtAmountReleased.setValidation(new CurrencyValidation(14, 2));
        txtRepaymentPeriod.setValidation(new NumericValidation());
        txtNoOFInst.setValidation(new NumericValidation());
        txtRemarks.setAllowAll(true);
        txtRateOfInterest.setValidation(new NumericValidation(3, 2));
        txtPenalInterest.setValidation(new NumericValidation(3, 2));
        txtNoOfMembers.setValidation(new NumericValidation());
        txtAmount.setValidation(new CurrencyValidation(14, 2));
        txtSlabNoOfMembers.setValidation(new NumericValidation());
        txtTotMembers.setValidation(new NumericValidation());
    }

    private void initComponentData() {
        try {
            cboPrincipalFrequency.setModel(observable.getCbmPrincipalFrequency());
            cboIntFrequency.setModel(observable.getCbmIntFrequency());
            cboRepaymentPeriod.setModel(observable.getCbmInstFreq());
            cboFromAmount.setModel(observable.getCbmFromAmount());
            cboToAmount.setModel(observable.getCbmToAmount());
            cboCrop.setModel(observable.getCbmCrop());
            cboCategory.setModel(observable.getCbmCategory());
            cboSubCategory1.setModel(observable.getCbmSubCategory());
            tblAmtSlabWise.setModel(observable.getTblAmtSlabWise());
            tblMemberDetails.setModel(observable.getTblMemberDetails());
        } catch (ClassCastException e) {
            parseException.logException(e, true);
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
        lblStatus.setText(observable.getLblStatus());
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        PanAcc_CD.setName("PanAcc_CD");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnCustomerID.setName("btnCustomerID");
        btnDelete.setName("btnDelete");
        btnDelete_ASW.setName("btnDelete_ASW");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
//        btnMembershipLia.setName("btnMembershipLia");
        btnNCLSanctionNo.setName("btnNCLSanctionNo");
        btnNew.setName("btnNew");
        btnNew_ASW.setName("btnNew_ASW");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        btnSave_ASW.setName("btnSave_ASW");
        btnView.setName("btnView");
        cboCrop.setName("cboCrop");
        cboFromAmount.setName("cboFromAmount");
        cboIntFrequency.setName("cboIntFrequency");
        cboLoanCategory.setName("cboLoanCategory");
        cboPrincipalFrequency.setName("cboPrincipalFrequency");
        cboRepaymentPeriod.setName("cboRepaymentPeriod");
        cboSubCategory.setName("cboSubCategory");
        cboToAmount.setName("cboToAmount");
        lbKCCAcName.setName("lbKCCAcName");
        lbOperativeAcName.setName("lbOperativeAcName");
        lblAmountReleased.setName("lblAmountReleased");
        lblAmountRequested.setName("lblAmountRequested");
        lblClassificationSanDate.setName("lblClassificationSanDate");
        lblClassificationSanDateVal.setName("lblClassificationSanDateVal");
        lblClassificationSanNo.setName("lblClassificationSanNo");
        lblClassificationSanNoVal.setName("lblClassificationSanNoVal");
        lblCrop.setName("lblCrop");
        lblCustomerID.setName("lblCustomerID");
        lblCustomerName.setName("lblCustomerName");
        lblDueDate.setName("lblDueDate");
        lblDueDt.setName("lblDueDt");
        lblFromAmount.setName("lblFromAmount");
        lblIntFrequency.setName("lblIntFrequency");
        lblInterestRepaymentFrequency.setName("lblInterestRepaymentFrequency");
        lblKCCAcNo.setName("lblKCCAcNo");
        lblKCCAccName_CD.setName("lblKCCAccName_CD");
        lblKCCAccNo_CD.setName("lblKCCAccNo_CD");
        lblKCCAccNo_CDVal.setName("lblKCCAccNo_CDVal");
        lblLoanCategory.setName("lblLoanCategory");
        lblMsg.setName("lblMsg");
        lblNCLSanctionNo.setName("lblNCLSanctionNo");
        lblNoOFInst.setName("lblNoOFInst");
        lblNoOfMembers.setName("lblNoOfMembers");
        lblOperativeAcNo.setName("lblOperativeAcNo");
        lblPanNumber1.setName("lblPanNumber1");
        lblPenalInterest.setName("lblPenalInterest");
        lblPrincipalFrequency.setName("lblPrincipalFrequency");
        lblRateOfInterest.setName("lblRateOfInterest");
        lblReleaseDt.setName("lblReleaseDt");
        lblReleaseNo.setName("lblReleaseNo");
        lblReleaseNoVal.setName("lblReleaseNoVal");
        lblRemarks.setName("lblRemarks");
        lblRepaymentFrequency.setName("lblRepaymentFrequency");
        lblRepaymentPeriod.setName("lblRepaymentPeriod");
        lblRequestedDt.setName("lblRequestedDt");
        lblSanctionAmount.setName("lblSanctionAmount");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblSpace6.setName("lblSpace6");
        lblSpace7.setName("lblSpace7");
        lblSpace9.setName("lblSpace9");
        lblStatus.setName("lblStatus");
        lblSubCategory.setName("lblSubCategory");
        lblToAmount.setName("lblToAmount");
        lblTotMembers.setName("lblTotMembers");
        lblTotaMembersVal.setName("lblTotaMembersVal");
        lblTotalAmountPayable.setName("lblTotalAmountPayable");
        lblTotalAmountPayableVal.setName("lblTotalAmountPayableVal");
        lblTotalInterestPayable.setName("lblTotalInterestPayable");
        lblTotalInterestPayableVal.setName("lblTotalInterestPayableVal");
        lblTotalMembers.setName("lblTotalMembers");
        lblUnUsedAmount.setName("lblUnUsedAmount");
        lblspace3.setName("lblspace3");
        mbrTermLoan.setName("mbrTermLoan");
        panAccountDetails.setName("panAccountDetails");
        panAmtSlabWise.setName("panAmtSlabWise");
        panAmtSlabWiseDet.setName("panAmtSlabWiseDet");
        panButton_AmtSlabWise.setName("panButton_AmtSlabWise");
        panClassDetails_Acc.setName("panClassDetails_Acc");
        panClassDetails_Details.setName("panClassDetails_Details");
        panClassMembers.setName("panClassMembers");
        panClassificationDetails.setName("panClassificationDetails");
        panDocumentDetails.setName("panDocumentDetails");
        panInsideDocumentDetails.setName("panInsideDocumentDetails");
        panInterestDetails.setName("panInterestDetails");
        panMinDepositPeriod3.setName("panMinDepositPeriod3");
        panProd_CD.setName("panProd_CD");
        panReleaseDetails.setName("panReleaseDetails");
        panSanction.setName("panSanction");
        panSanctionDetails.setName("panSanction1");
        panSanctionCustDet.setName("panSanctionCustDet");
        panSanctionCustomerDet.setName("panSanctionCustomerDet");
        panStatus.setName("panStatus");
        panTable_ASW.setName("panTable_ASW");
        panTermLoan.setName("panTermLoan");
        sptBorroewrProfile.setName("sptBorroewrProfile");
        sptBorroewrProfile1.setName("sptBorroewrProfile1");
        sptClassDetails.setName("sptClassDetails");
        sptClassification_vertical.setName("sptClassification_vertical");
        srpTable_ASW.setName("srpTable_ASW");
        srpTable_Document.setName("srpTable_Document");
        tabLimitAmount.setName("tabLimitAmount");
        tblAmtSlabWise.setName("tblAmtSlabWise");
        tblDocumentTable.setName("tblDocumentTable");
        tdRequestedDt.setName("tdRequestedDt");
        tdtDueDate.setName("tdtDueDate");
        tdtDueDt.setName("tdtDueDt");
        tdtReleaseDt.setName("tdtReleaseDt");
        txtAmountReleased.setName("txtAmountReleased");
        txtAmountRequested.setName("txtAmountRequested");
        txtCustomerID.setName("txtCustomerID");
        txtEditTermLoanNo.setName("txtEditTermLoanNo");
        txtKCCAcNo.setName("txtKCCAcNo");
        txtNCLSanctionNo.setName("txtNCLSanctionNo");
        txtNoOFInst.setName("txtNoOFInst");
        txtNoOfMembers.setName("txtNoOfMembers");
        txtOperativeAcNo.setName("txtOperativeAcNo");
        txtPenalInterest.setName("txtPenalInterest");
        txtRateOfInterest.setName("txtRateOfInterest");
        txtRemarks.setName("txtRemarks");
        txtRepaymentPeriod.setName("txtRepaymentPeriod");
        txtSanctionAmount.setName("txtSanctionAmount");
        txtTotMembers.setName("txtTotMembers");
        txtUnUsedAmount.setName("txtUnUsedAmount");
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
        txtEditTermLoanNo = new com.see.truetransact.uicomponent.CTextField();
        lblSpace9 = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        panTermLoan = new com.see.truetransact.uicomponent.CPanel();
        tabLimitAmount = new com.see.truetransact.uicomponent.CTabbedPane();
        panReleaseDetails = new com.see.truetransact.uicomponent.CPanel();
        panSanctionDetails = new com.see.truetransact.uicomponent.CPanel();
        panAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        lblReleaseNo = new com.see.truetransact.uicomponent.CLabel();
        lblReleaseNoVal = new com.see.truetransact.uicomponent.CLabel();
        lblRequestedDt = new com.see.truetransact.uicomponent.CLabel();
        tdRequestedDt = new com.see.truetransact.uicomponent.CDateField();
        lblDueDt = new com.see.truetransact.uicomponent.CLabel();
        tdtDueDt = new com.see.truetransact.uicomponent.CDateField();
        lblAmountReleased = new com.see.truetransact.uicomponent.CLabel();
        txtAmountReleased = new com.see.truetransact.uicomponent.CTextField();
        lblAmountRequested = new com.see.truetransact.uicomponent.CLabel();
        txtAmountRequested = new com.see.truetransact.uicomponent.CTextField();
        lblNoOFInst = new com.see.truetransact.uicomponent.CLabel();
        lblReleaseDt = new com.see.truetransact.uicomponent.CLabel();
        tdtReleaseDt = new com.see.truetransact.uicomponent.CDateField();
        lblRepaymentPeriod = new com.see.truetransact.uicomponent.CLabel();
        panMinDepositPeriod3 = new com.see.truetransact.uicomponent.CPanel();
        txtRepaymentPeriod = new com.see.truetransact.uicomponent.CTextField();
        cboRepaymentPeriod = new com.see.truetransact.uicomponent.CComboBox();
        lblRepaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblPrincipalFrequency = new com.see.truetransact.uicomponent.CLabel();
        txtNoOFInst = new com.see.truetransact.uicomponent.CTextField();
        cboPrincipalFrequency = new com.see.truetransact.uicomponent.CComboBox();
        panInterestDetails = new com.see.truetransact.uicomponent.CPanel();
        lblRateOfInterest = new com.see.truetransact.uicomponent.CLabel();
        txtRateOfInterest = new com.see.truetransact.uicomponent.CTextField();
        lblPenalInterest = new com.see.truetransact.uicomponent.CLabel();
        txtPenalInterest = new com.see.truetransact.uicomponent.CTextField();
        lblCrop = new com.see.truetransact.uicomponent.CLabel();
        cboCrop = new com.see.truetransact.uicomponent.CComboBox();
        lblLoanCategory = new com.see.truetransact.uicomponent.CLabel();
        cboLoanCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblSubCategory = new com.see.truetransact.uicomponent.CLabel();
        cboSubCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblTotalAmountPayable = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestPayableVal = new com.see.truetransact.uicomponent.CLabel();
        lblTotalInterestPayable = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmountPayableVal = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        lblInterestRepaymentFrequency = new com.see.truetransact.uicomponent.CLabel();
        lblIntFrequency = new com.see.truetransact.uicomponent.CLabel();
        cboIntFrequency = new com.see.truetransact.uicomponent.CComboBox();
        sptBorroewrProfile1 = new com.see.truetransact.uicomponent.CSeparator();
        panSanction = new com.see.truetransact.uicomponent.CPanel();
        panSanctionCustDet = new com.see.truetransact.uicomponent.CPanel();
        lblNCLSanctionNo = new com.see.truetransact.uicomponent.CLabel();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        txtNCLSanctionNo = new com.see.truetransact.uicomponent.CTextField();
        btnNCLSanctionNo = new com.see.truetransact.uicomponent.CButton();
        lblCustomerName = new com.see.truetransact.uicomponent.CLabel();
        lblDueDate = new com.see.truetransact.uicomponent.CLabel();
        tdtDueDate = new com.see.truetransact.uicomponent.CDateField();
        lblUnUsedAmount = new com.see.truetransact.uicomponent.CLabel();
        txtUnUsedAmount = new com.see.truetransact.uicomponent.CTextField();
        txtSanctionAmount = new com.see.truetransact.uicomponent.CTextField();
        lblSanctionAmount = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerID = new com.see.truetransact.uicomponent.CButton();
        panSanctionCustomerDet = new com.see.truetransact.uicomponent.CPanel();
        lblOperativeAcNo = new com.see.truetransact.uicomponent.CLabel();
        txtOperativeAcNo = new com.see.truetransact.uicomponent.CTextField();
        lbOperativeAcName = new com.see.truetransact.uicomponent.CLabel();
        lblKCCAcNo = new com.see.truetransact.uicomponent.CLabel();
        txtKCCAcNo = new com.see.truetransact.uicomponent.CTextField();
        lbKCCAcName = new com.see.truetransact.uicomponent.CLabel();
        sptBorroewrProfile = new com.see.truetransact.uicomponent.CSeparator();
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
        lblFromAmount1 = new com.see.truetransact.uicomponent.CLabel();
        cboCategory = new com.see.truetransact.uicomponent.CComboBox();
        lblToAmount1 = new com.see.truetransact.uicomponent.CLabel();
        cboSubCategory1 = new com.see.truetransact.uicomponent.CComboBox();
        lblNoOfMembers1 = new com.see.truetransact.uicomponent.CLabel();
        txtNoOfMembers = new com.see.truetransact.uicomponent.CTextField();
        panButton_AmtSlabWise1 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_Mem = new com.see.truetransact.uicomponent.CButton();
        btnSave_Mem = new com.see.truetransact.uicomponent.CButton();
        btnDelete_Mem = new com.see.truetransact.uicomponent.CButton();
        txtAmount = new com.see.truetransact.uicomponent.CTextField();
        lblNoOfMembers2 = new com.see.truetransact.uicomponent.CLabel();
        panTable_ASW1 = new com.see.truetransact.uicomponent.CPanel();
        srpTable_ASW1 = new com.see.truetransact.uicomponent.CScrollPane();
        tblMemberDetails = new com.see.truetransact.uicomponent.CTable();
        lblTotalMembers1 = new com.see.truetransact.uicomponent.CLabel();
        lblTotaMembersValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmt = new com.see.truetransact.uicomponent.CLabel();
        lblTotalAmtValue = new com.see.truetransact.uicomponent.CLabel();
        panAmtSlabWise = new com.see.truetransact.uicomponent.CPanel();
        panAmtSlabWiseDet = new com.see.truetransact.uicomponent.CPanel();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        cboFromAmount = new com.see.truetransact.uicomponent.CComboBox();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        cboToAmount = new com.see.truetransact.uicomponent.CComboBox();
        lblNoOfMembers = new com.see.truetransact.uicomponent.CLabel();
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
        sptClassification_vertical = new com.see.truetransact.uicomponent.CSeparator();
        panDocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        panInsideDocumentDetails = new com.see.truetransact.uicomponent.CPanel();
        srpTable_Document = new com.see.truetransact.uicomponent.CScrollPane();
        tblDocumentTable = new com.see.truetransact.uicomponent.CTable();
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
        setTitle("Release");
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
        lblPanNumber1.setText("Sanction No");
        lblPanNumber1.setMinimumSize(new java.awt.Dimension(72, 16));
        tbrTermLoan.add(lblPanNumber1);

        lblSpace7.setText("     ");
        tbrTermLoan.add(lblSpace7);

        txtEditTermLoanNo.setMinimumSize(new java.awt.Dimension(100, 18));
        txtEditTermLoanNo.setPreferredSize(new java.awt.Dimension(100, 18));
        txtEditTermLoanNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEditTermLoanNoFocusLost(evt);
            }
        });
        tbrTermLoan.add(txtEditTermLoanNo);

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

        panReleaseDetails.setMinimumSize(new java.awt.Dimension(850, 660));
        panReleaseDetails.setPreferredSize(new java.awt.Dimension(850, 660));
        panReleaseDetails.setLayout(new java.awt.GridBagLayout());

        panSanctionDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSanctionDetails.setMinimumSize(new java.awt.Dimension(815, 310));
        panSanctionDetails.setPreferredSize(new java.awt.Dimension(815, 310));
        panSanctionDetails.setLayout(new java.awt.GridBagLayout());

        panAccountDetails.setMaximumSize(new java.awt.Dimension(330, 300));
        panAccountDetails.setMinimumSize(new java.awt.Dimension(330, 300));
        panAccountDetails.setPreferredSize(new java.awt.Dimension(330, 300));
        panAccountDetails.setLayout(new java.awt.GridBagLayout());

        lblReleaseNo.setText("Release No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblReleaseNo, gridBagConstraints);

        lblReleaseNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblReleaseNoVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblReleaseNoVal.setMaximumSize(new java.awt.Dimension(100, 16));
        lblReleaseNoVal.setMinimumSize(new java.awt.Dimension(100, 16));
        lblReleaseNoVal.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblReleaseNoVal, gridBagConstraints);

        lblRequestedDt.setText("Requested Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblRequestedDt, gridBagConstraints);

        tdRequestedDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdRequestedDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdRequestedDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdRequestedDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(tdRequestedDt, gridBagConstraints);

        lblDueDt.setText("Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblDueDt, gridBagConstraints);

        tdtDueDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDueDt.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(tdtDueDt, gridBagConstraints);

        lblAmountReleased.setText("Amount Released");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblAmountReleased, gridBagConstraints);

        txtAmountReleased.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAmountReleased.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountReleased.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountReleasedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtAmountReleased, gridBagConstraints);

        lblAmountRequested.setText("Amount Requested");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblAmountRequested, gridBagConstraints);

        txtAmountRequested.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAmountRequested.setMinimumSize(new java.awt.Dimension(100, 21));
        txtAmountRequested.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtAmountRequestedFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtAmountRequested, gridBagConstraints);

        lblNoOFInst.setText("No. of Installments");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblNoOFInst, gridBagConstraints);

        lblReleaseDt.setText("Date of Release");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblReleaseDt, gridBagConstraints);

        tdtReleaseDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtReleaseDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtReleaseDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtReleaseDtFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(tdtReleaseDt, gridBagConstraints);

        lblRepaymentPeriod.setText("Repayment Period");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblRepaymentPeriod, gridBagConstraints);

        panMinDepositPeriod3.setPreferredSize(new java.awt.Dimension(154, 21));
        panMinDepositPeriod3.setLayout(new java.awt.GridBagLayout());

        txtRepaymentPeriod.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRepaymentPeriod.setPreferredSize(new java.awt.Dimension(50, 20));
        txtRepaymentPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRepaymentPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        panMinDepositPeriod3.add(txtRepaymentPeriod, gridBagConstraints);

        cboRepaymentPeriod.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRepaymentPeriod.setPreferredSize(new java.awt.Dimension(100, 20));
        cboRepaymentPeriod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboRepaymentPeriodActionPerformed(evt);
            }
        });
        cboRepaymentPeriod.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboRepaymentPeriodFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        panMinDepositPeriod3.add(cboRepaymentPeriod, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(panMinDepositPeriod3, gridBagConstraints);

        lblRepaymentFrequency.setText("Principal Repayment Frequency :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblRepaymentFrequency, gridBagConstraints);

        lblPrincipalFrequency.setText("Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(lblPrincipalFrequency, gridBagConstraints);

        txtNoOFInst.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNoOFInst.setPreferredSize(new java.awt.Dimension(50, 20));
        txtNoOFInst.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNoOFInstFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(txtNoOFInst, gridBagConstraints);

        cboPrincipalFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPrincipalFrequency.setPreferredSize(new java.awt.Dimension(100, 20));
        cboPrincipalFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPrincipalFrequencyActionPerformed(evt);
            }
        });
        cboPrincipalFrequency.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboPrincipalFrequencyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAccountDetails.add(cboPrincipalFrequency, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panSanctionDetails.add(panAccountDetails, gridBagConstraints);

        panInterestDetails.setMaximumSize(new java.awt.Dimension(435, 300));
        panInterestDetails.setMinimumSize(new java.awt.Dimension(435, 300));
        panInterestDetails.setPreferredSize(new java.awt.Dimension(435, 300));
        panInterestDetails.setLayout(new java.awt.GridBagLayout());

        lblRateOfInterest.setText("Rate of Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblRateOfInterest, gridBagConstraints);

        txtRateOfInterest.setMinimumSize(new java.awt.Dimension(50, 21));
        txtRateOfInterest.setPreferredSize(new java.awt.Dimension(50, 20));
        txtRateOfInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRateOfInterestActionPerformed(evt);
            }
        });
        txtRateOfInterest.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtRateOfInterestFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(txtRateOfInterest, gridBagConstraints);

        lblPenalInterest.setText("Penal Interest");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblPenalInterest, gridBagConstraints);

        txtPenalInterest.setMinimumSize(new java.awt.Dimension(50, 21));
        txtPenalInterest.setPreferredSize(new java.awt.Dimension(50, 20));
        txtPenalInterest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPenalInterestActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(txtPenalInterest, gridBagConstraints);

        lblCrop.setText("Crop");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblCrop, gridBagConstraints);

        cboCrop.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCrop.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCrop.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(cboCrop, gridBagConstraints);

        lblLoanCategory.setText("Loan Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblLoanCategory, gridBagConstraints);

        cboLoanCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboLoanCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboLoanCategory.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(cboLoanCategory, gridBagConstraints);

        lblSubCategory.setText("Sub Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblSubCategory, gridBagConstraints);

        cboSubCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboSubCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSubCategory.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipady = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(cboSubCategory, gridBagConstraints);

        lblTotalAmountPayable.setText("Total Amount Payable Rs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblTotalAmountPayable, gridBagConstraints);

        lblTotalInterestPayableVal.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalInterestPayableVal.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalInterestPayableVal.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblTotalInterestPayableVal, gridBagConstraints);

        lblTotalInterestPayable.setText("Total Interest Payable Rs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblTotalInterestPayable, gridBagConstraints);

        lblTotalAmountPayableVal.setMaximumSize(new java.awt.Dimension(100, 16));
        lblTotalAmountPayableVal.setMinimumSize(new java.awt.Dimension(100, 16));
        lblTotalAmountPayableVal.setPreferredSize(new java.awt.Dimension(100, 16));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblTotalAmountPayableVal, gridBagConstraints);

        txtRemarks.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(txtRemarks, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblRemarks, gridBagConstraints);

        lblInterestRepaymentFrequency.setText("Interest Repayment Frequency :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblInterestRepaymentFrequency, gridBagConstraints);

        lblIntFrequency.setText("Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(lblIntFrequency, gridBagConstraints);

        cboIntFrequency.setMinimumSize(new java.awt.Dimension(100, 21));
        cboIntFrequency.setPreferredSize(new java.awt.Dimension(100, 20));
        cboIntFrequency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboIntFrequencyActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInterestDetails.add(cboIntFrequency, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panSanctionDetails.add(panInterestDetails, gridBagConstraints);

        sptBorroewrProfile1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptBorroewrProfile1.setMinimumSize(new java.awt.Dimension(1, 15));
        sptBorroewrProfile1.setPreferredSize(new java.awt.Dimension(1, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 3, 0);
        panSanctionDetails.add(sptBorroewrProfile1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panReleaseDetails.add(panSanctionDetails, gridBagConstraints);

        panSanction.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panSanction.setMinimumSize(new java.awt.Dimension(815, 200));
        panSanction.setPreferredSize(new java.awt.Dimension(815, 200));
        panSanction.setLayout(new java.awt.GridBagLayout());

        panSanctionCustDet.setMaximumSize(new java.awt.Dimension(330, 180));
        panSanctionCustDet.setMinimumSize(new java.awt.Dimension(330, 180));
        panSanctionCustDet.setPreferredSize(new java.awt.Dimension(330, 180));
        panSanctionCustDet.setLayout(new java.awt.GridBagLayout());

        lblNCLSanctionNo.setText("NCL Sanction No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblNCLSanctionNo, gridBagConstraints);

        lblCustomerID.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblCustomerID, gridBagConstraints);

        txtNCLSanctionNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNCLSanctionNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNCLSanctionNoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(txtNCLSanctionNo, gridBagConstraints);

        btnNCLSanctionNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnNCLSanctionNo.setToolTipText("Select Customer");
        btnNCLSanctionNo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNCLSanctionNo.setMaximumSize(new java.awt.Dimension(22, 21));
        btnNCLSanctionNo.setMinimumSize(new java.awt.Dimension(22, 21));
        btnNCLSanctionNo.setPreferredSize(new java.awt.Dimension(22, 21));
        btnNCLSanctionNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNCLSanctionNoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSanctionCustDet.add(btnNCLSanctionNo, gridBagConstraints);

        lblCustomerName.setForeground(new java.awt.Color(0, 51, 204));
        lblCustomerName.setText("Customer Name");
        lblCustomerName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblCustomerName, gridBagConstraints);

        lblDueDate.setText("Due Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblDueDate, gridBagConstraints);

        tdtDueDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtDueDate.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(tdtDueDate, gridBagConstraints);

        lblUnUsedAmount.setText("Unused Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblUnUsedAmount, gridBagConstraints);

        txtUnUsedAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtUnUsedAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(txtUnUsedAmount, gridBagConstraints);

        txtSanctionAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtSanctionAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(txtSanctionAmount, gridBagConstraints);

        lblSanctionAmount.setText("Sanction Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(lblSanctionAmount, gridBagConstraints);

        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtCustomerIDFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustDet.add(txtCustomerID, gridBagConstraints);

        btnCustomerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustomerID.setToolTipText("Select Customer");
        btnCustomerID.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCustomerID.setMaximumSize(new java.awt.Dimension(22, 21));
        btnCustomerID.setMinimumSize(new java.awt.Dimension(22, 21));
        btnCustomerID.setPreferredSize(new java.awt.Dimension(22, 21));
        btnCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIDActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSanctionCustDet.add(btnCustomerID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panSanction.add(panSanctionCustDet, gridBagConstraints);

        panSanctionCustomerDet.setMaximumSize(new java.awt.Dimension(435, 190));
        panSanctionCustomerDet.setMinimumSize(new java.awt.Dimension(435, 190));
        panSanctionCustomerDet.setPreferredSize(new java.awt.Dimension(435, 190));
        panSanctionCustomerDet.setLayout(new java.awt.GridBagLayout());

        lblOperativeAcNo.setText("Operative Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustomerDet.add(lblOperativeAcNo, gridBagConstraints);

        txtOperativeAcNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustomerDet.add(txtOperativeAcNo, gridBagConstraints);

        lbOperativeAcName.setForeground(new java.awt.Color(0, 51, 204));
        lbOperativeAcName.setText("Account Name");
        lbOperativeAcName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustomerDet.add(lbOperativeAcName, gridBagConstraints);

        lblKCCAcNo.setText("KCC Account No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustomerDet.add(lblKCCAcNo, gridBagConstraints);

        txtKCCAcNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustomerDet.add(txtKCCAcNo, gridBagConstraints);

        lbKCCAcName.setForeground(new java.awt.Color(0, 51, 204));
        lbKCCAcName.setText("Account Name");
        lbKCCAcName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSanctionCustomerDet.add(lbKCCAcName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panSanction.add(panSanctionCustomerDet, gridBagConstraints);

        sptBorroewrProfile.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptBorroewrProfile.setMinimumSize(new java.awt.Dimension(1, 190));
        sptBorroewrProfile.setPreferredSize(new java.awt.Dimension(1, 190));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 3, 0);
        panSanction.add(sptBorroewrProfile, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panReleaseDetails.add(panSanction, gridBagConstraints);

        tabLimitAmount.addTab("Release Details", panReleaseDetails);

        panClassificationDetails.setMinimumSize(new java.awt.Dimension(500, 282));
        panClassificationDetails.setPreferredSize(new java.awt.Dimension(500, 282));
        panClassificationDetails.setLayout(new java.awt.GridBagLayout());

        panClassDetails_Acc.setMinimumSize(new java.awt.Dimension(750, 45));
        panClassDetails_Acc.setPreferredSize(new java.awt.Dimension(750, 45));
        panClassDetails_Acc.setLayout(new java.awt.GridBagLayout());

        panProd_CD.setMinimumSize(new java.awt.Dimension(258, 58));
        panProd_CD.setPreferredSize(new java.awt.Dimension(258, 58));
        panProd_CD.setLayout(new java.awt.GridBagLayout());

        lblClassificationSanNo.setText("Sanction No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panProd_CD.add(lblClassificationSanNo, gridBagConstraints);

        lblClassificationSanNoVal.setForeground(new java.awt.Color(0, 51, 204));
        lblClassificationSanNoVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblClassificationSanNoVal.setMaximumSize(new java.awt.Dimension(130, 21));
        lblClassificationSanNoVal.setMinimumSize(new java.awt.Dimension(130, 21));
        lblClassificationSanNoVal.setPreferredSize(new java.awt.Dimension(130, 21));
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

        PanAcc_CD.setMinimumSize(new java.awt.Dimension(250, 55));
        PanAcc_CD.setPreferredSize(new java.awt.Dimension(250, 55));
        PanAcc_CD.setLayout(new java.awt.GridBagLayout());

        lblKCCAccName_CD.setForeground(new java.awt.Color(0, 51, 204));
        lblKCCAccName_CD.setText("KCC Account Name");
        lblKCCAccName_CD.setMinimumSize(new java.awt.Dimension(139, 21));
        lblKCCAccName_CD.setPreferredSize(new java.awt.Dimension(139, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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

        panClassMembers.setMinimumSize(new java.awt.Dimension(800, 250));
        panClassMembers.setPreferredSize(new java.awt.Dimension(800, 245));
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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panClassMembers.add(txtTotMembers, gridBagConstraints);

        panMemberDetails.setBorder(javax.swing.BorderFactory.createTitledBorder("MemberDetails"));
        panMemberDetails.setMinimumSize(new java.awt.Dimension(700, 200));
        panMemberDetails.setPreferredSize(new java.awt.Dimension(700, 200));
        panMemberDetails.setLayout(new java.awt.GridBagLayout());

        panAmtSlabWiseDet1.setMinimumSize(new java.awt.Dimension(230, 120));
        panAmtSlabWiseDet1.setPreferredSize(new java.awt.Dimension(230, 120));
        panAmtSlabWiseDet1.setLayout(new java.awt.GridBagLayout());

        lblFromAmount1.setText("Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(lblFromAmount1, gridBagConstraints);

        cboCategory.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCategory.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(cboCategory, gridBagConstraints);

        lblToAmount1.setText("Sub Category");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(lblToAmount1, gridBagConstraints);

        cboSubCategory1.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSubCategory1.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAmtSlabWiseDet1.add(cboSubCategory1, gridBagConstraints);

        lblNoOfMembers1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoOfMembers1.setText("No. of Members");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet1.add(lblNoOfMembers1, gridBagConstraints);

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
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panButton_AmtSlabWise1.add(btnSave_Mem, gridBagConstraints);

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

        txtAmount.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet1.add(txtAmount, gridBagConstraints);

        lblNoOfMembers2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoOfMembers2.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet1.add(lblNoOfMembers2, gridBagConstraints);

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
        srpTable_ASW1.setPreferredSize(new java.awt.Dimension(400, 300));
        srpTable_ASW1.setRequestFocusEnabled(false);

        tblMemberDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl.No", "Category", "Sub Category", "No of Members", "Amount"
            }
        ));
        tblMemberDetails.setMinimumSize(new java.awt.Dimension(400, 700));
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
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable_ASW1.add(srpTable_ASW1, gridBagConstraints);

        lblTotalMembers1.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalMembers1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalMembers1.setText("Total Members : ");
        lblTotalMembers1.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalMembers1.setMaximumSize(new java.awt.Dimension(10, 15));
        lblTotalMembers1.setMinimumSize(new java.awt.Dimension(10, 15));
        lblTotalMembers1.setPreferredSize(new java.awt.Dimension(10, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTable_ASW1.add(lblTotalMembers1, gridBagConstraints);

        lblTotaMembersValue.setForeground(new java.awt.Color(0, 51, 204));
        lblTotaMembersValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotaMembersValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotaMembersValue.setMaximumSize(new java.awt.Dimension(45, 15));
        lblTotaMembersValue.setMinimumSize(new java.awt.Dimension(45, 15));
        lblTotaMembersValue.setPreferredSize(new java.awt.Dimension(45, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panTable_ASW1.add(lblTotaMembersValue, gridBagConstraints);

        lblTotalAmt.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalAmt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalAmt.setText("Total Amount : ");
        lblTotalAmt.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalAmt.setMaximumSize(new java.awt.Dimension(10, 15));
        lblTotalAmt.setMinimumSize(new java.awt.Dimension(10, 15));
        lblTotalAmt.setPreferredSize(new java.awt.Dimension(10, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 133;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panTable_ASW1.add(lblTotalAmt, gridBagConstraints);

        lblTotalAmtValue.setForeground(new java.awt.Color(0, 51, 204));
        lblTotalAmtValue.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalAmtValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalAmtValue.setMaximumSize(new java.awt.Dimension(45, 15));
        lblTotalAmtValue.setMinimumSize(new java.awt.Dimension(45, 15));
        lblTotalAmtValue.setPreferredSize(new java.awt.Dimension(45, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
        panTable_ASW1.add(lblTotalAmtValue, gridBagConstraints);

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

        panAmtSlabWiseDet.setMinimumSize(new java.awt.Dimension(230, 82));
        panAmtSlabWiseDet.setPreferredSize(new java.awt.Dimension(230, 82));
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

        lblNoOfMembers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNoOfMembers.setText("No. of Members");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panAmtSlabWiseDet.add(lblNoOfMembers, gridBagConstraints);

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

        panTable_ASW.setMinimumSize(new java.awt.Dimension(400, 175));
        panTable_ASW.setPreferredSize(new java.awt.Dimension(400, 175));
        panTable_ASW.setLayout(new java.awt.GridBagLayout());

        srpTable_ASW.setMinimumSize(new java.awt.Dimension(350, 140));

        tblAmtSlabWise.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "From Amount", "To Amount", "No Of Members"
            }
        ));
        tblAmtSlabWise.setMinimumSize(new java.awt.Dimension(400, 700));
        tblAmtSlabWise.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblAmtSlabWise.setPreferredSize(new java.awt.Dimension(400, 700));
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
        lblTotalMembers.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblTotalMembers.setText("Total Members : ");
        lblTotalMembers.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalMembers.setMaximumSize(new java.awt.Dimension(120, 15));
        lblTotalMembers.setMinimumSize(new java.awt.Dimension(120, 15));
        lblTotalMembers.setPreferredSize(new java.awt.Dimension(120, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 26);
        panTable_ASW.add(lblTotalMembers, gridBagConstraints);

        lblTotaMembersVal.setForeground(new java.awt.Color(0, 51, 204));
        lblTotaMembersVal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotaMembersVal.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotaMembersVal.setMaximumSize(new java.awt.Dimension(100, 15));
        lblTotaMembersVal.setMinimumSize(new java.awt.Dimension(100, 15));
        lblTotaMembersVal.setPreferredSize(new java.awt.Dimension(100, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 3, 25);
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
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 0, 0);
        panClassificationDetails.add(panAmtSlabWise, gridBagConstraints);

        sptClassification_vertical.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClassificationDetails.add(sptClassification_vertical, gridBagConstraints);

        tabLimitAmount.addTab("Classification Details", panClassificationDetails);

        panDocumentDetails.setMinimumSize(new java.awt.Dimension(510, 200));
        panDocumentDetails.setPreferredSize(new java.awt.Dimension(510, 200));
        panDocumentDetails.setLayout(new java.awt.GridBagLayout());

        panInsideDocumentDetails.setMinimumSize(new java.awt.Dimension(460, 300));
        panInsideDocumentDetails.setPreferredSize(new java.awt.Dimension(460, 300));
        panInsideDocumentDetails.setLayout(new java.awt.GridBagLayout());

        srpTable_Document.setMinimumSize(new java.awt.Dimension(460, 300));
        srpTable_Document.setPreferredSize(new java.awt.Dimension(460, 300));

        tblDocumentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Select", "Document Name", "Date of Receipt"
            }
        ));
        tblDocumentTable.setMinimumSize(new java.awt.Dimension(330, 0));
        tblDocumentTable.setPreferredScrollableViewportSize(new java.awt.Dimension(330, 161));
        tblDocumentTable.setPreferredSize(new java.awt.Dimension(330, 0));
        srpTable_Document.setViewportView(tblDocumentTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panInsideDocumentDetails.add(srpTable_Document, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocumentDetails.add(panInsideDocumentDetails, gridBagConstraints);

        tabLimitAmount.addTab("Documents", panDocumentDetails);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTermLoan.add(tabLimitAmount, gridBagConstraints);

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

    private void txtEditTermLoanNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEditTermLoanNoFocusLost
        // TODO add your handling code here:
        if (txtEditTermLoanNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("NCL_SANCTION_NO", txtEditTermLoanNo.getText());
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getNCLSanctionEditDelete", whereMap);
            if (lst != null && lst.size() > 0) {
                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
                lblStatus.setText("Edit");
                whereMap = (HashMap) lst.get(0);
                fillData(whereMap);
                btnCancel.setEnabled(true);
                lst = null;
                whereMap = null;
                EnableDisableValues();
            } else {
                ClientUtil.displayAlert("Invalid NclSanction Number !!! ");
                txtEditTermLoanNo.setText("");
                return;
            }
        }
    }//GEN-LAST:event_txtEditTermLoanNoFocusLost
            private void txtNCLSanctionNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNCLSanctionNoFocusLost
                // TODO add your handling code here:
                if (txtNCLSanctionNo.getText().length() > 0) {
                    HashMap whereMap = new HashMap();
                    whereMap.put("CUST_ID", txtCustomerID.getText());
                    whereMap.put("NCL_SANCTION_NO", txtNCLSanctionNo.getText());
                    whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                    List lst = ClientUtil.executeQuery("getNCLMasterCustID", whereMap);
                    if (lst != null && lst.size() > 0) {
                        viewType = "NCL_SANCTION_NO";
                        whereMap = (HashMap) lst.get(0);
                        fillData(whereMap);
                        lst = null;
                        whereMap = null;
                    } else {
                        ClientUtil.showMessageWindow("Invalid NCL Sanction Number !!! ");
                        txtNCLSanctionNo.setText("");
                        txtSanctionAmount.setText("");
                        txtUnUsedAmount.setText("");
                        tdtDueDate.setDateValue("");
                        txtOperativeAcNo.setText("");
                        lbOperativeAcName.setText("");
                        txtKCCAcNo.setText("");
                        lbKCCAcName.setText("");
                    }
                }
    }//GEN-LAST:event_txtNCLSanctionNoFocusLost

    private void EnableDisableValues() {
        txtCustomerID.setEnabled(false);
        tdtDueDate.setEnabled(false);
        txtRemarks.setEnabled(false);
        cboCrop.setEnabled(false);
        txtTotMembers.setEnabled(false);
    }

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        btnView.setEnabled(false);
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        popUp("Enquiry");
        ClientUtil.enableDisable(panReleaseDetails, false);
        lblStatus.setText("Enquiry");
        btnSave.setEnabled(false);
        btnView.setEnabled(false);
    }//GEN-LAST:event_btnViewActionPerformed

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
                                                    observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
                                                    updateAuthorizeStatus(CommonConstants.STATUS_EXCEPTION);
                                                    btnCancel.setEnabled(true);
                                                    btnReject.setEnabled(false);
                                                    btnAuthorize.setEnabled(false);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        updateAuthorizeStatus(CommonConstants.STATUS_REJECTED);
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // Add your handling code here:
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        updateAuthorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnCancel.setEnabled(true);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    private void updateAuthorizeStatus(String authorizeStatus) {
        if (viewType == AUTHORIZE && isFilled) {
            ArrayList arrList = new ArrayList();
            HashMap authorizeMap = new HashMap();
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put("STATUS", authorizeStatus);
            singleAuthorizeMap.put("RELEASE_NO", lblReleaseNoVal.getText());
            singleAuthorizeMap.put("NCL_SANCTION_NO", txtNCLSanctionNo.getText());
            singleAuthorizeMap.put("AUTHORIZED_BY", TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("AUTHORIZED_DT", ClientUtil.getCurrentDateWithTime());
            singleAuthorizeMap.put("CLEAR_BALANCE", txtAmountReleased.getText());
            arrList.add(singleAuthorizeMap);
            authorizeMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            authorizeMap.put(CommonConstants.AUTHORIZEDATA, arrList);
            authorize(authorizeMap);
            viewType = "";
            super.setOpenForEditBy(observable.getStatusBy());
            singleAuthorizeMap = null;
            arrList = null;
            authorizeMap = null;
        } else {
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            HashMap whereMap = new HashMap();
            whereMap.put("USER_ID", TrueTransactMain.USER_ID);
            whereMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            mapParam.put(CommonConstants.MAP_NAME, "getNCLreleaseAuthorize");
            isFilled = false;
            AuthorizeUI authorizeUI = new AuthorizeUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            observable.setStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }

    public void authorize(HashMap map) {
        if (map.get(CommonConstants.AUTHORIZEDATA) != null) {
            observable.set_authorizeMap(map);
            observable.doAction();
            btnCancelActionPerformed(null);
            observable.setStatus();
            observable.setResultStatus();
            lblStatus.setText(observable.getLblStatus());
        }
    }
    private void btnNCLSanctionNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNCLSanctionNoActionPerformed
        // TODO add your handling code here:
        popUp("NCL_SANCTION_NO");
    }//GEN-LAST:event_btnNCLSanctionNoActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        viewType = "CANCEL";
        lblStatus.setText("               ");
        observable.resetForm();
        observable.resetTableValues();
        ClientUtil.clearAll(this);
        ClientUtil.enableDisable(this, false);
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
        enableDisableButtons(false);
        lblCustomerName.setText("");
        lbOperativeAcName.setText("");
        lbKCCAcName.setText("");
        lblReleaseNoVal.setText("");
        lblTotalInterestPayableVal.setText("");
        lblTotalAmountPayableVal.setText("");
        lblTotaMembersValue.setText("");
        lblTotaMembersVal.setText("");
        lblClassificationSanNoVal.setText("");
        lblClassificationSanDateVal.setText("");
        lblKCCAccNo_CDVal.setText("");
        lblKCCAccName_CD.setText("");
        lblTotalAmtValue.setText("");
        buttonEnableDisableLoan(false);
        buttonEnableDisableMem(false);
        setSizeTableData();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
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
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp("Delete");
        lblStatus.setText("Delete");
        btnReject.setEnabled(false);
        btnAuthorize.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(panReleaseDetails, false);
        if (lblReleaseNoVal.getText().length() <= 0) {
            btnCancelActionPerformed(null);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp("Edit");
        lblStatus.setText("Edit");
        btnDelete.setEnabled(false);
        ClientUtil.enableDisable(panSanction, false);
        ClientUtil.enableDisable(panSanctionDetails, true);
        tdtDueDt.setEnabled(false);
        if (lblReleaseNoVal.getText().length() <= 0) {
            btnCancelActionPerformed(null);
        }
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        if (txtCustomerID.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Customer ID Should not be Empty !!! ");
            return;
        } else if (txtNCLSanctionNo.getText().length() <= 0) {
            ClientUtil.showMessageWindow("NCL Sanction Number Should not be Empty !!! ");
            return;
        } else if (txtOperativeAcNo.getText().length() <= 0) {
            ClientUtil.showMessageWindow("Operative Account Number Should not be Empty !!! ");
            return;
        } else if (txtKCCAcNo.getText().length() <= 0) {
            ClientUtil.showMessageWindow("KCC Account Number Should not be Empty !!! ");
            return;
        } else if (CommonUtil.convertObjToDouble(txtAmountReleased.getText()).doubleValue() <= 0) {
            ClientUtil.showMessageWindow("Released Amount Should not be Empty !!! ");
            return;
        } else if (txtRepaymentPeriod.getText().length() <= 0 || cboRepaymentPeriod.getSelectedIndex() <= 0) {
            ClientUtil.showMessageWindow("Repayment Period Should not be Empty !!! ");
            return;
        } else {
            double totalAmt = 0.0;
            for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
                //Changed By Kannan
//                totalAmt = totalAmt + CommonUtil.convertObjToDouble(tblMemberDetails.getValueAt(i, 4)).doubleValue();
                String Amt = CommonUtil.convertObjToStr(tblMemberDetails.getValueAt(i, 4));
                Amt = Amt.replace(",", "");
                totalAmt = totalAmt + CommonUtil.convertObjToDouble(Amt);
            }
            if ((CommonUtil.convertObjToInt(txtTotMembers.getText()) == CommonUtil.convertObjToInt(lblTotaMembersValue.getText()))
                    && (CommonUtil.convertObjToInt(txtTotMembers.getText()) == CommonUtil.convertObjToInt(lblTotaMembersVal.getText()))) {
                if (CommonUtil.convertObjToDouble(txtAmountReleased.getText()) == totalAmt) {
                    //Changed By Kannan
                    String minAmt = CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(0, 0));
                    String maxAmt = CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1));
                    minAmt = minAmt.replace(".00", "");
                    maxAmt = maxAmt.replace(".00", "");
                    maxAmt = maxAmt.replace(",", "");
                    if (minAmt.equals(("1")) && maxAmt.equals("9999999")) {
//                    if (tblAmtSlabWise.getValueAt(0, 0).equals(CommonUtil.convertObjToDouble("1")) && tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1).equals(CommonUtil.convertObjToDouble("9999999"))) {
                        updateOBFields();
                        savePerformed();
                    } else {
                        ClientUtil.showAlertWindow("Slab Should End With Max. Amount 999999999 !!! ");
                    }
                } else {
                    ClientUtil.showAlertWindow("Categorywise Total Amount Should be Equals Amount Released Amount!!! ");
                }
            } else {
                ClientUtil.showAlertWindow("Total No.of Member Should be Equals Total Members ");
            }
    }//GEN-LAST:event_btnSaveActionPerformed
    }

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        setModified(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        ClientUtil.enableDisable(panReleaseDetails, true);
        ClientUtil.enableDisable(panClassMembers, true);
        ClientUtil.enableDisable(panAmtSlabWiseDet, false);
        ClientUtil.enableDisable(panAmtSlabWiseDet1, false);
        txtEditTermLoanNo.setEnabled(true);
        ClientUtil.clearAll(this);
        lblStatus.setText("New");
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        enableDisableButtons(true);
        tdtDueDt.setEnabled(false);
        tdtReleaseDt.setDateValue(DateUtil.getStringDate(curDate));
        btnNew_ASW.setEnabled(true);
        btnNew_Mem.setEnabled(true);
        cboPrincipalFrequency.setSelectedItem("On Maturity");
        txtNoOFInst.setText("1");
    }//GEN-LAST:event_btnNewActionPerformed

    private void savePerformed() {
        observable.doAction();
        if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
            if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                if ((observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) && 
                        observable.getProxyReturnMap().containsKey("RELEASE_NO")) {
                    ClientUtil.showMessageWindow("Release No : " + observable.getProxyReturnMap().get("RELEASE_NO"));
                }
                if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                    displayTransDetail(observable.getProxyReturnMap());
                }
            }
            btnCancelActionPerformed(null);
            lblStatus.setText(ClientConstants.RESULT_STATUS[observable.getResult()]);
        }
        btnCancel.setEnabled(true);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        setModified(false);
        ClientUtil.clearAll(this);
    }

    private void displayTransDetail(HashMap proxyResultMap) {
//        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        String cashDisplayStr = "Cash Transaction Details...\n";
        String transferDisplayStr = "Transfer Transaction Details...\n";
        String displayStr = "";
        String transId = "";
        String transMode = "";
        Object keys[] = proxyResultMap.keySet().toArray();
        int cashCount = 0;
        int transferCount = 0;
        List tempList = null;
        HashMap transMap = null;
        String actNum = "";
        List transIdList = new ArrayList();
        for (int i = 0; i < keys.length; i++) {
            if (proxyResultMap.get(keys[i]) instanceof String) {
                continue;
            }
            tempList = (List) proxyResultMap.get(keys[i]);
            if (CommonUtil.convertObjToStr(keys[i]).indexOf("TRANSFER") != -1) {
                for (int j = 0; j < tempList.size(); j++) {
                    transMap = (HashMap) tempList.get(j);
                    if (j == 0) {
                        transId = (String) transMap.get("BATCH_ID");
                        transIdList.add(transId);
                        transMode = "TRANSFER";
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
                transferCount++;
            }
        }
        if (cashCount > 0) {
            displayStr += cashDisplayStr;
        }
        if (transferCount > 0) {
            displayStr += transferDisplayStr;
        }
        ClientUtil.showMessageWindow("" + displayStr);
    }

    public void enableDisableButtons(boolean flag) {
        btnCustomerID.setEnabled(flag);
        btnNCLSanctionNo.setEnabled(flag);
//        btnMembershipLia.setEnabled(flag);
    }
    private void cboPrincipalFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPrincipalFrequencyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboPrincipalFrequencyActionPerformed

    private void cboRepaymentPeriodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboRepaymentPeriodActionPerformed
        // TODO add your handling code here:
        if (txtRepaymentPeriod.getText().length() > 0) {
            calculateMatDate();
            calculatePayableAmount();
        }
    }//GEN-LAST:event_cboRepaymentPeriodActionPerformed
    private void calculatePayableAmount() {
        if (tdtDueDt.getDateValue().length() > 0) {
            Date dueDate = DateUtil.getDateMMDDYYYY(tdtDueDt.getDateValue());
            long diffDay = DateUtil.dateDiff(curDate, dueDate);
            if (diffDay > 0) {
                double total_Int_Payable = 0.0;
                double total_Amt_Payable = 0.0;
                total_Int_Payable = (CommonUtil.convertObjToDouble(txtAmountReleased.getText())
                        * CommonUtil.convertObjToDouble(txtRateOfInterest.getText()) * diffDay) / 36500;
                Rounding rod = new Rounding();
                total_Int_Payable = (double) rod.getNearest((long) (total_Int_Payable * 100), 100) / 100;
                lblTotalInterestPayableVal.setText(String.valueOf(total_Int_Payable));
                lblTotalAmountPayableVal.setText(String.valueOf(CommonUtil.convertObjToDouble(txtAmountReleased.getText()) + total_Int_Payable));
            }
        }
    }
    private void txtRateOfInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRateOfInterestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRateOfInterestActionPerformed

    private void txtPenalInterestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPenalInterestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPenalInterestActionPerformed

    private void cboIntFrequencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboIntFrequencyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboIntFrequencyActionPerformed

    private void txtCustomerIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCustomerIDFocusLost
        // TODO add your handling code here:
        if (txtCustomerID.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CUST_ID", txtCustomerID.getText());
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            List lst = ClientUtil.executeQuery("getNCLMasterCustID", whereMap);
            if (lst != null && lst.size() > 0) {
                viewType = "NCL_CUST_ID";
                whereMap = (HashMap) lst.get(0);
                fillData(whereMap);
                lst = null;
                whereMap = null;
            } else {
                ClientUtil.showMessageWindow("Invalid Customer ID !!! ");
                txtCustomerID.setText("");
                lblCustomerName.setText("");
            }
        }
    }//GEN-LAST:event_txtCustomerIDFocusLost

    private void btnCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIDActionPerformed
        // TODO add your handling code here:
        popUp("NCL_CUST_ID");
    }//GEN-LAST:event_btnCustomerIDActionPerformed

        private void txtRepaymentPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRepaymentPeriodFocusLost
            // TODO add your handling code here:
            if (cboRepaymentPeriod.getSelectedIndex() > 0) {
                calculateMatDate();
                calculatePayableAmount();
                defaultSetInstallment();
            }
        }//GEN-LAST:event_txtRepaymentPeriodFocusLost

    private void txtRateOfInterestFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtRateOfInterestFocusLost
        // TODO add your handling code here:
        if (txtRateOfInterest.getText().length() > 0) {
            calculatePayableAmount();
        }
    }//GEN-LAST:event_txtRateOfInterestFocusLost

    private void txtAmountReleasedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountReleasedFocusLost
        // TODO add your handling code here:        
        calculatePayableAmount();
        double unusedAmount = CommonUtil.convertObjToDouble(txtUnUsedAmount.getText());
        double RequestAmount = CommonUtil.convertObjToDouble(txtAmountRequested.getText());
        if (CommonUtil.convertObjToDouble(txtAmountReleased.getText()) > unusedAmount) {
            ClientUtil.showAlertWindow("Released Amount Should Be Less Than Or Equal Unused Amount !!!");
            txtAmountReleased.setText("");
            update = false;
        }
        if (txtAmountReleased.getText().length() > 0) {
            if (CommonUtil.convertObjToDouble(txtAmountReleased.getText()) > RequestAmount) {
                ClientUtil.showAlertWindow("Amount Released should not be Greater than Amount Requested!!!...");
                txtAmountReleased.setText("");
                update = false;
                return;
            }
        }
        // Added By kannan        
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW) {
            if (CommonUtil.convertObjToDouble(txtAmountReleased.getText()).doubleValue() > 0) {
                double shareReleaseAmt = 0.0;
                double totalshareReleaseAmt = 0.0;
                double finaltotalReleaseAmt = 0.0;
                HashMap shareMap = new HashMap();
                String loanType = "";
                String displayStr = "";
                shareMap.put("CUST_ID", txtCustomerID.getText());
                List shareLimitLst = ClientUtil.executeQuery("getShareLoanLimitPercentage", shareMap);
                if (shareLimitLst != null && shareLimitLst.size() > 0) {
                    for (int i = 0; i < shareLimitLst.size(); i++) {
                        double percentage = 0.0;
                        double totalReleaseAmt = 0.0;
                        shareMap = (HashMap) shareLimitLst.get(i);
                        percentage = CommonUtil.convertObjToDouble(shareMap.get("BORROWER_SHARE_PERCENTAGE")).doubleValue();
                        loanType = CommonUtil.convertObjToStr(shareMap.get("LOAN_TYPE"));
                        double maxLoanAmt = CommonUtil.convertObjToDouble(shareMap.get("MAX_LOAN_AMT")).doubleValue();
                        if (loanType.equals("KCM_LOAN")) {
                            totalReleaseAmt = CommonUtil.convertObjToDouble(txtAmountReleased.getText()).doubleValue();
                        }
                        HashMap loanSanctionMap = new HashMap();
                        loanSanctionMap.put("CUST_ID", txtCustomerID.getText());
                        loanSanctionMap.put("AUTHORIZE_REMARK", loanType);
                        List list = null;
                        if (loanType.equals("KCM_LOAN")) {
                            list = ClientUtil.executeQuery("getAllLimitAmountsForOtherLoans", loanSanctionMap);
                        } else {
                            list = ClientUtil.executeQuery("getAllLimitAmountsForGoldLoans", loanSanctionMap);
                        }
                        if (list != null && list.size() > 0) {
                            HashMap hmap = (HashMap) list.get(0);
                            double limitAmt = CommonUtil.convertObjToDouble(hmap.get("SANCTION")).doubleValue();
                            double limit = CommonUtil.convertObjToDouble(txtAmountReleased.getText()).doubleValue();
                            limitAmt = limitAmt + limit;
//                                if (maxLoanAmt > 0.0) {
//                                    if (limitAmt > maxLoanAmt) {
//                                        ClientUtil.showMessageWindow("Exceeds  Maximum Loan Limit Specified");
//                                        txtAmountReleased.setText("");
//                                        txtAmountReleased.requestDefaultFocus();
//                                        return;
//                                    }
//                                }
                        }
                        List loanSanctionLst = ClientUtil.executeQuery("getTotalSanctionAmount", loanSanctionMap);
                        if (loanSanctionLst != null && loanSanctionLst.size() > 0) {
                            loanSanctionMap = new HashMap();
                            for (int j = 0; j < loanSanctionLst.size(); j++) {
                                loanSanctionMap = (HashMap) loanSanctionLst.get(j);
                                totalReleaseAmt = totalReleaseAmt + CommonUtil.convertObjToDouble(loanSanctionMap.get("SANCTION_AMOUNT")).doubleValue();
//                                    displayStr += "Existing Loan No  : " + loanSanctionMap.get("ACCT_NUM") + "\n"
//                                            + "Limit                     : Rs " + loanSanctionMap.get("SANCTION_AMOUNT") + "\n";
                            }
                            finaltotalReleaseAmt = finaltotalReleaseAmt + totalReleaseAmt;
                            shareReleaseAmt = totalReleaseAmt * percentage / 100;
                        }
                        totalshareReleaseAmt = totalshareReleaseAmt + shareReleaseAmt;
                    }
                    if (finaltotalReleaseAmt > 0) {
                        finaltotalReleaseAmt = finaltotalReleaseAmt - CommonUtil.convertObjToDouble(txtAmountReleased.getText()).doubleValue();
                    }
                    double shortFallAmt = 0.0;
                    HashMap shMap = new HashMap();
                    shMap.put("CUSTOMER ID", txtCustomerID.getText());
                    List shareLst = ClientUtil.executeQuery("getShareAccountDetails", shMap);
                    if (shareLst != null && shareLst.size() > 0) {
                        shareMap = (HashMap) shareLst.get(0);
                    }
                    double shrAmt = CommonUtil.convertObjToDouble(shareMap.get("TOTAL_SHARE_AMOUNT")).doubleValue();
                    shortFallAmt = totalshareReleaseAmt - shrAmt;
                    Rounding rod = new Rounding();
                    shortFallAmt = (double) rod.getNearest((long) (shortFallAmt * 100), 100) / 100;
//                        System.out.println("shortFallAmt:" + shortFallAmt);
//                        System.out.println("totalshareReleaseAmt:" + totalshareReleaseAmt);
//                        System.out.println("shrAmt : " + shrAmt);
                    if (shrAmt < totalshareReleaseAmt) {
                        displayStr += "Total Limits                                      : Rs " + finaltotalReleaseAmt + "\n";
                        displayStr += "Share Value to be Subscribed         : Rs " + totalshareReleaseAmt + "\n";
                        displayStr += "Present Share Amount Subscribed : Rs " + shrAmt + "\n";
                        displayStr += "Shortfall                                          : Rs " + shortFallAmt;
                        ClientUtil.showMessageWindow("Share Holding Not Covering The Release Amount short Fall Rs." + shortFallAmt);
                        txtAmountReleased.setText("");
                        return;
                    }
//                        if (!displayStr.equals("")) {
//                            ClientUtil.showMessageWindow("" + displayStr);
//                        }
//                        int c = ClientUtil.confirmationAlert("Do you want to Continue");
//                        int d = 0;
//                        System.out.println("####### Yes/No : " + c);
//                        if (c != d) {
//                            return;
//                        }
                }
            }
        }
    }//GEN-LAST:event_txtAmountReleasedFocusLost

    private void tdRequestedDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdRequestedDtFocusLost
        // TODO add your handling code here:
        if (tdRequestedDt.getDateValue().length() > 0) {
            java.util.Date requestedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdRequestedDt.getDateValue()));
            if (curDate != null && requestedDate != null && DateUtil.dateDiff(curDate, requestedDate) > 0) {
                ClientUtil.showAlertWindow("Requested Date should be equal Or less than Current Date");
                tdRequestedDt.setDateValue("");
            }
        }
    }//GEN-LAST:event_tdRequestedDtFocusLost

    private void tdtReleaseDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtReleaseDtFocusLost
        // TODO add your handling code here:
        if (tdtReleaseDt.getDateValue().length() > 0) {
            java.util.Date requestedDate = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(tdtReleaseDt.getDateValue()));
            if (curDate != null && requestedDate != null && DateUtil.dateDiff(curDate, requestedDate) > 0) {
                ClientUtil.showAlertWindow("Release Date should be equal Or less than Current Date");
                tdtReleaseDt.setDateValue("");
            }
        }
    }//GEN-LAST:event_tdtReleaseDtFocusLost

    private void btnNew_MemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_MemActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        buttonEnableDisableMem(true);
        btnNew_Mem.setEnabled(false);
        observable.setNewData(true);
        ClientUtil.enableDisable(panAmtSlabWiseDet1, true);
    }//GEN-LAST:event_btnNew_MemActionPerformed
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

    public void updateMemberOBFields() {
        observable.setCboCategory((String) cboCategory.getSelectedItem());
        observable.setCboSubCategory((String) cboSubCategory1.getSelectedItem());
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

    private void resetMemberTable() {
        txtAmount.setText("");
        txtNoOfMembers.setText("");
        cboCategory.setSelectedItem("");
        cboSubCategory1.setSelectedItem("");
    }

    private void resetLoanTable() {
        txtSlabNoOfMembers.setText("");
        cboFromAmount.setSelectedItem("");
        cboToAmount.setSelectedItem("");
    }
    private void btnSave_MemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_MemActionPerformed
        // TODO add your handling code here:
        try {
            // Added By Kannan
            double totalMember = 0;
            if (updateMode) {
                totalMember = CommonUtil.convertObjToDouble(lblTotaMembersValue.getText()) + CommonUtil.convertObjToDouble(txtNoOfMembers.getText()) - CommonUtil.convertObjToDouble(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 3));
            } else {
                totalMember = CommonUtil.convertObjToDouble(lblTotaMembersValue.getText()) + CommonUtil.convertObjToDouble(txtNoOfMembers.getText());
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
                if (CommonUtil.convertObjToDouble(txtSanctionAmount.getText()) >= CommonUtil.convertObjToDouble(totalAmt)) {
                    updateMemberOBFields();
                    if (cboCategory.getSelectedIndex() > 0 && cboSubCategory1.getSelectedIndex() > 0 && txtAmount.getText().length() > 0) {
                        if (!updateMode) {
                            if (tblMemberDetails.getRowCount() > 0) {
                                for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
                                    if ((tblMemberDetails.getValueAt(i, 1).equals(CommonUtil.convertObjToStr(cboCategory.getSelectedItem())) && (tblMemberDetails.getValueAt(i, 2).equals(CommonUtil.convertObjToStr(cboSubCategory1.getSelectedItem()))))) {
                                        ClientUtil.showAlertWindow("Category and Subcategory Combination Should Not Be Repeated !!!");
                                        return;
                                    }
                                }
                            }
                        }
                        observable.addDataToMemberDetailsTable(updateTab, updateMode);
                        totalCatAmountCalc();
                        tblMemberDetails.setModel(observable.getTblMemberDetails());
                        observable.resetMemberTable();
                        resetMemberTable();
                        ClientUtil.enableDisable(panMemberDetails, false);
                        buttonEnableDisableMem(false);
                        btnNew_Mem.setEnabled(true);
                        calcCategoryTotalMember();
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
        int totMember = 0;
        if (tblMemberDetails.getRowCount() > 0) {
            for (int i = 0; i < tblMemberDetails.getRowCount(); i++) {
                totMember = totMember + CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(i, 3).toString());
            }
        }
        lblTotaMembersValue.setText(String.valueOf(totMember));
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
        lblTotalAmtValue.setText(CommonUtil.convertObjToStr(new Double(totAmt)));
        return totAmt;
    }
    
    private void calcSlabTotalMember() {
        int totMember = 0;
        if (tblAmtSlabWise.getRowCount() > 0) {
            for (int i = 0; i < tblAmtSlabWise.getRowCount(); i++) {
                totMember = totMember + CommonUtil.convertObjToInt(tblAmtSlabWise.getValueAt(i, 2).toString());
            }
        }
        lblTotaMembersVal.setText(String.valueOf(totMember));
    }
    private void btnDelete_MemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_MemActionPerformed
        // TODO add your handling code here:
        int st = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 0));
        observable.deleteMemberTableData(st, tblMemberDetails.getSelectedRow());
        totalCatAmountCalc();
        observable.resetMemberTable();
        resetMemberTable();
        buttonEnableDisableMem(false);
        btnNew_Mem.setEnabled(true);
        calcCategoryTotalMember();
    }//GEN-LAST:event_btnDelete_MemActionPerformed

    private void tblMemberDetailsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMemberDetailsMousePressed
        // TODO add your handling code here: 
        if (tblMemberDetails.getRowCount() > 0) {
            updateMode = true;
            updateTab = tblMemberDetails.getSelectedRow();
            observable.setNewData(false);
            int st = CommonUtil.convertObjToInt(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 0));
//            String st = CommonUtil.convertObjToStr(tblMemberDetails.getValueAt(tblMemberDetails.getSelectedRow(), 0));
            ClientUtil.enableDisable(panAmtSlabWiseDet1, true);
            cboCategory.setEnabled(false);
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
    public void memberTableUpdate() {
        txtAmount.setText(observable.getTxtAmount());
        txtNoOfMembers.setText(observable.getTxtNoOfMembers());
        cboSubCategory1.setSelectedItem(observable.getCboSubCategory());
        cboCategory.setSelectedItem(observable.getCboCategory());
    }

    public void loanTableupdate() {
        txtSlabNoOfMembers.setText(observable.getTxtSlabNoOfMembers());
        cboFromAmount.setSelectedItem(observable.getCboFromAmount().replace(".00", ""));
        cboToAmount.setSelectedItem(observable.getCboToAmount().replace(".00", ""));
    }
    private void btnNew_ASWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_ASWActionPerformed
        // TODO add your handling code here:
        updateMode = false;
        buttonEnableDisableLoan(false);
        btnSave_ASW.setEnabled(true);
        btnDelete_ASW.setEnabled(true);
        observable.setSlabNewData(true);
        cboFromAmount.setEnabled(true);
        cboToAmount.setEnabled(true);
        txtSlabNoOfMembers.setEnabled(true);
        if (tblAmtSlabWise.getRowCount() <= 0) {
            cboFromAmount.setSelectedIndex(1);
        } else {
            //Changed By Kannan
//            cboFromAmount.setSelectedItem(ClientUtil.convertObjToCurrency(String.valueOf(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1))).replace(".00", ""));
            cboFromAmount.setSelectedItem(String.valueOf(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getRowCount() - 1, 1)).replace(".00", ""));
        }
        cboFromAmount.setEnabled(false);
    }//GEN-LAST:event_btnNew_ASWActionPerformed

    private void btnSave_ASWActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_ASWActionPerformed
        // TODO add your handling code here:
        try {
            // Added By kannan
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
                    tblAmtSlabWise.setModel(observable.getTblAmtSlabWise());
                    observable.resetLoanTable();
                    resetLoanTable();
                    ClientUtil.enableDisable(panAmtSlabWise, false);
                    buttonEnableDisableLoan(false);
                    btnNew_ASW.setEnabled(true);
                    calcSlabTotalMember();
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
            st=st.replace(".00", "");
            st=st.replace(",", "");
            observable.deleteloanTableData(st, tblAmtSlabWise.getSelectedRow());
            observable.resetLoanTable();
            resetLoanTable();
            buttonEnableDisableLoan(false);
            btnNew_ASW.setEnabled(true);
            calcSlabTotalMember();
        }
    }//GEN-LAST:event_btnDelete_ASWActionPerformed

    private void tblAmtSlabWiseMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAmtSlabWiseMousePressed
        // TODO add your handling code here:
        if (tblAmtSlabWise.getRowCount() > 0) {
            updateMode = true;
            updateTab = tblAmtSlabWise.getSelectedRow();
            observable.setSlabNewData(false);
            String st = CommonUtil.convertObjToStr(tblAmtSlabWise.getValueAt(tblAmtSlabWise.getSelectedRow(), 0));
            st=st.replace(".00", "");
            st=st.replace(",", "");
            ClientUtil.enableDisable(panAmtSlabWise, true);
            cboFromAmount.setEnabled(false);
            observable.populateLoanTableDetails(st);
            loanTableupdate();
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
            String fromAmount = CommonUtil.convertObjToStr(cboFromAmount.getSelectedItem());
            String toAmount = CommonUtil.convertObjToStr(cboToAmount.getSelectedItem());
            fromAmount = fromAmount.replace(",", "");
            toAmount = toAmount.replace(",", "");
            Double frmAmt = CommonUtil.convertObjToDouble(fromAmount);
            Double toAmt = CommonUtil.convertObjToDouble(toAmount);
            if (CommonUtil.convertObjToDouble(frmAmt) >= CommonUtil.convertObjToDouble(toAmt)) {
                ClientUtil.showAlertWindow("To Amount Should be Greater Than From Amount");
            }
            observable.setCboToAmount("");
            return;
        }
    }//GEN-LAST:event_cboToAmountFocusLost

    private void txtAmountRequestedFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtAmountRequestedFocusLost
    }//GEN-LAST:event_txtAmountRequestedFocusLost

    private void cboRepaymentPeriodFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboRepaymentPeriodFocusLost
        // TODO add your handling code here:
        defaultSetInstallment();
    }//GEN-LAST:event_cboRepaymentPeriodFocusLost

    private void txtNoOFInstFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNoOFInstFocusLost
        // TODO add your handling code here:
        installmentValidation();
    }//GEN-LAST:event_txtNoOFInstFocusLost

    private void cboPrincipalFrequencyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboPrincipalFrequencyFocusLost
        // TODO add your handling code here:
        installmentValidation();
    }//GEN-LAST:event_cboPrincipalFrequencyFocusLost
    private void defaultSetInstallment() {
        if (txtRepaymentPeriod.getText().length() > 0 && cboRepaymentPeriod.getSelectedIndex() > 0) {
//            txtNoOFInst.setText(txtRepaymentPeriod.getText());
//            if (cboRepaymentPeriod.getSelectedItem().equals("Years")) {
//                cboPrincipalFrequency.setSelectedItem("Yearly");
//            } else if (cboRepaymentPeriod.getSelectedItem().equals("Months")) {
//                cboPrincipalFrequency.setSelectedItem("Monthly");
//            }
        }
    }

    private void installmentValidation() {
        if (txtRepaymentPeriod.getText().length() > 0 && cboRepaymentPeriod.getSelectedIndex() > 0
                && txtNoOFInst.getText().length() > 0 && cboPrincipalFrequency.getSelectedIndex() > 0) {
            java.util.Date instDate = curDate;
            if (instDate != null) {
                if (!(cboPrincipalFrequency.getSelectedItem().equals("Lump Sum") || cboPrincipalFrequency.getSelectedItem().equals("On Maturity"))) {
                    GregorianCalendar cal = new GregorianCalendar((instDate.getYear() + yearTobeAdded), instDate.getMonth(), instDate.getDate());
                    if (cboPrincipalFrequency.getSelectedItem().equals("Yearly")) {
                        cal.add(GregorianCalendar.YEAR, Integer.parseInt(txtNoOFInst.getText()));
                    }
                    if (cboPrincipalFrequency.getSelectedItem().equals("Monthly")) {
                        cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtNoOFInst.getText()));
                    }
                    if (cboPrincipalFrequency.getSelectedItem().equals("Half Yearly")) {
                        cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtNoOFInst.getText()) * 6);
                    }
                    if (cboPrincipalFrequency.getSelectedItem().equals("Quaterly")) {
                        cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtNoOFInst.getText()) * 3);
                    }
                    instDate = (Date) DateUtil.getDateMMDDYYYY(DateUtil.getStringDate(cal.getTime()));
                    Date dueDate = DateUtil.getDateMMDDYYYY(tdtDueDt.getDateValue());
                    long diffDayPending = DateUtil.dateDiff(instDate, dueDate);
//                System.out.println("########### Diff InstDate : " + diffDayPending);
                    if (diffDayPending > 0 || diffDayPending < 0) {
                        ClientUtil.showMessageWindow("Enter Proper No of Installments/Frequency based on Repayment Period!!!");
                        txtNoOFInst.setText("");
                        cboPrincipalFrequency.setSelectedItem("");
                        return;
                    }
                }
            }
        }
    }

    private void checkShareHolder() {
//        String selectKey=CommonUtil.convertObjToStr(((ComboBoxModel)cboAccountName.getModel()).getKeyForSelected());
        HashMap shareMap = new HashMap();
        shareMap.put("LOAN_TYPE", "KCM_LOAN");
        String sharetype = "NOMINAL";
        List shareList = ClientUtil.executeQuery("getSelectShareProductLoanAcct", shareMap);
        if (shareList != null && shareList.size() > 0) {
            shareMap.put("CUST_ID", txtCustomerID.getText());
            List share = ClientUtil.executeQuery("getShareAccInfoDataForLoan", shareMap);
            if (share != null && share.size() > 0) {
                shareMap = (HashMap) share.get(0);
                String notElegibleLoan = CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_LOAN"));
                if (notElegibleLoan != null && notElegibleLoan.equals("Y") && shareMap.get("NOT_ELIGIBLE_DT") != null) {
                    Date eligibal_dt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(shareMap.get("NOT_ELIGIBLE_DT")));
                    if (DateUtil.dateDiff(eligibal_dt, (Date) curr_dt.clone()) < 0) {
                        ClientUtil.showMessageWindow(" Eligible Date is Not Expiry");
                    }
                    return;

                } else if (shareMap.get("SHARE_TYPE") != null && sharetype.equals(CommonUtil.convertObjToStr(shareMap.get("SHARE_TYPE")))) {
                    ClientUtil.showMessageWindow("Share Type is NOMINAL");
                    return;
                }
            }
        }
    }

    public void updateSlabAmountDetails() {
        observable.setCboFromAmount((String) cboFromAmount.getSelectedItem());
        observable.setCboToAmount((String) cboToAmount.getSelectedItem());
        observable.setTxtNoOfMembers(txtNoOfMembers.getText());
    }

    private void calculateMatDate() {
        java.util.Date depDate = curDate;
//        System.out.println("####calculateMatDate : " + depDate);
        if (depDate != null) {
            GregorianCalendar cal = new GregorianCalendar((depDate.getYear() + yearTobeAdded), depDate.getMonth(), depDate.getDate());
            if (cboRepaymentPeriod.getSelectedIndex() > 0 && cboRepaymentPeriod.getSelectedItem().equals("Years")) {
                cal.add(GregorianCalendar.YEAR, Integer.parseInt(txtRepaymentPeriod.getText()));
            } else {
                cal.add(GregorianCalendar.YEAR, 0);
            }
            if (cboRepaymentPeriod.getSelectedIndex() > 0 && cboRepaymentPeriod.getSelectedItem().equals("Months")) {
                cal.add(GregorianCalendar.MONTH, Integer.parseInt(txtRepaymentPeriod.getText()));
            } else {
                cal.add(GregorianCalendar.MONTH, 0);
            }
            if (cboRepaymentPeriod.getSelectedIndex() > 0 && cboRepaymentPeriod.getSelectedItem().equals("Days")) {
                cal.add(GregorianCalendar.DAY_OF_MONTH, Integer.parseInt(txtRepaymentPeriod.getText()));
            } else {
                cal.add(GregorianCalendar.DAY_OF_MONTH, 0);
            }
            observable.setTdtDueDt(DateUtil.getStringDate(cal.getTime()));
            tdtDueDt.setDateValue(observable.getTdtDueDt());
        }
        tdtDueDt.setEnabled(false);
    }

    /**
     * To display a popUp window for viewing existing data
     */
    private void popUp(String currAction) {
        viewType = currAction;
        HashMap viewMap = new HashMap();
        if (currAction.equalsIgnoreCase("Edit") || currAction.equalsIgnoreCase("Delete")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNCLSanctionEditDelete");
        } else if (currAction.equalsIgnoreCase("Enquiry")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNCLSanctionView");
        } else if (currAction.equals("NCL_CUST_ID")) {
            HashMap map = new HashMap();
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNCLMasterCustID");
        } else if (currAction.equals("NCL_SANCTION_NO")) {
            HashMap map = new HashMap();
            map.put("CUST_ID", txtCustomerID.getText());
            map.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, map);
            viewMap.put(CommonConstants.MAP_NAME, "getNCLMasterCustID");
        }
        new ViewAll(this, viewMap).show();
    }

    public void fillData(Object obj) {
        try {
            HashMap hashMap = (HashMap) obj;
            System.out.println("### fillData Hash : " + hashMap);
            isFilled = true;
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                this.setButtonEnableDisable();
                if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                    hashMap.put("DISPLAY_TRANS_DETAILS", "DISPLAY_TRANS_DETAILS");
                }
                observable.setTxtCustomerID(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                observable.getData(hashMap);
                update();
                displayCustomerName();
                displayNCLSanctionDetails();
                calcCategoryTotalMember();
                calcSlabTotalMember();
                totalCatAmountCalc();//Added By Kannan
                txtTotMembers.setText(lblTotaMembersValue.getText());
                lblClassificationSanNoVal.setText(txtNCLSanctionNo.getText());
                lblClassificationSanDateVal.setText(tdtDueDate.getDateValue());
                lblKCCAccNo_CDVal.setText(txtKCCAcNo.getText());
                lblKCCAccName_CD.setText(lbKCCAcName.getText());
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION
                    || observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT) {
                this.setButtonEnableDisable();
                hashMap.put("DISPLAY_TRANS_DETAILS", "DISPLAY_TRANS_DETAILS");
                observable.setLblReleaseNo(CommonUtil.convertObjToStr(hashMap.get("RELEASE_NO")));
                observable.setTxtNCLSanctionNo(CommonUtil.convertObjToStr(hashMap.get("NCL_SANCTION_NO")));
                observable.setTxtCustomerID(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                observable.getData(hashMap);
                update();
                displayCustomerName();
                displayNCLSanctionDetails();
                calcCategoryTotalMember();
                calcSlabTotalMember();
                totalCatAmountCalc();
                txtTotMembers.setText(lblTotaMembersValue.getText());
                lblClassificationSanNoVal.setText(txtNCLSanctionNo.getText());
                lblClassificationSanDateVal.setText(tdtDueDate.getDateValue());
                lblKCCAccNo_CDVal.setText(txtKCCAcNo.getText());
                lblKCCAccName_CD.setText(lbKCCAcName.getText());
                if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
                    if (observable.getProxyReturnMap().containsKey("TRANSFER_TRANS_LIST")) {
                        displayTransDetail(observable.getProxyReturnMap());
                        observable.setProxyReturnMap(null);
                    }
                }
            } else if (viewType.equalsIgnoreCase("NCL_CUST_ID")) {
                txtCustomerID.setText(CommonUtil.convertObjToStr(hashMap.get("CUST_ID")));
                displayCustomerName();
            } else if (viewType.equalsIgnoreCase("NCL_SANCTION_NO")) {
                txtNCLSanctionNo.setText(CommonUtil.convertObjToStr(hashMap.get("NCL_SANCTION_NO")));
                displayNCLSanctionDetails();
            }
            if (viewType == AUTHORIZE) {
                btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
                btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
                btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            hashMap = null;
            btnCancel.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void displayCustomerName() {
        if (txtCustomerID.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("CUST_ID", txtCustomerID.getText());
            List custLst = (List) ClientUtil.executeQuery("getSelectCustomerName", whereMap);
            if (custLst != null && custLst.size() > 0) {
                HashMap custMap = new HashMap();
                custMap = (HashMap) custLst.get(0);
                lblCustomerName.setText(CommonUtil.convertObjToStr(custMap.get("CUSTOMER NAME")));
            }
        }
    }

    private void displayNCLSanctionDetails() {
        if (txtNCLSanctionNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("NCL_SANCTION_NO", txtNCLSanctionNo.getText());
            List sanctionLst = (List) ClientUtil.executeQuery("getNCLSanctionDetails", whereMap);
            if (sanctionLst != null && sanctionLst.size() > 0) {
                whereMap = (HashMap) sanctionLst.get(0);
                txtSanctionAmount.setText(CommonUtil.convertObjToStr(whereMap.get("SANCTION_AMT")));
                txtUnUsedAmount.setText(CommonUtil.convertObjToStr(whereMap.get("SANCTION_AMT")));
                tdtDueDate.setDateValue(CommonUtil.convertObjToStr(whereMap.get("EXPIRY_DT")));
                txtOperativeAcNo.setText(CommonUtil.convertObjToStr(whereMap.get("CA_ACT_NUM")));
                txtKCCAcNo.setText(CommonUtil.convertObjToStr(whereMap.get("KCC_ACT_NUM")));
                txtCustomerID.setEnabled(false);
                btnCustomerID.setEnabled(false);
                txtSanctionAmount.setEnabled(false);
                txtUnUsedAmount.setEnabled(false);
                tdtDueDate.setEnabled(false);
                txtOperativeAcNo.setEnabled(false);
                txtKCCAcNo.setEnabled(false);
                displayAccountNameDetails();

                //Added by kannan
                String year = DateUtil.getStringDate(curDate);
                year = year.replace("/", "");
                year = year.substring(4, 8);
                HashMap useMap = new HashMap();
                useMap.put("NCL_SANCTION_NO", txtNCLSanctionNo.getText());
                useMap.put("START_FIN_YEAR", year);
                useMap.put("BRANCH_CODE", TrueTransactMain.BRANCH_ID);
                List limitlist = (List) ClientUtil.executeQuery("getLimitAmountFromNclSublimit", useMap);
                if (limitlist != null && limitlist.size() > 0) {
                    useMap = (HashMap) limitlist.get(0);
                    txtSanctionAmount.setText(CommonUtil.convertObjToStr(useMap.get("LIMIT_AMT")));
                }
                HashMap releaseMap = new HashMap();
                releaseMap.put("NCL_SANCTION_NO", txtNCLSanctionNo.getText());
                releaseMap.put("START_FIN_YEAR", year);
                releaseMap.put("END_FIN_YEAR", String.valueOf(CommonUtil.convertObjToInt(year)+1));                
                List releaseAmtlist = (List) ClientUtil.executeQuery("getTotalReleasedAmount", releaseMap);
                if (releaseAmtlist != null && releaseAmtlist.size() > 0) {
                    releaseMap = (HashMap) releaseAmtlist.get(0);
                    Double sanctinAmt = CommonUtil.convertObjToDouble(txtSanctionAmount.getText());
                    Double releaseAmt = CommonUtil.convertObjToDouble(releaseMap.get("AMOUNT_RELEASED"));
                    if (releaseAmt > 0) {
                        Double unusedAmt = sanctinAmt - releaseAmt;
                        if (sanctinAmt == releaseAmt) {
                            txtUnUsedAmount.setText(CommonUtil.convertObjToStr(sanctinAmt));
                        } else {
                            txtUnUsedAmount.setText(String.valueOf(unusedAmt));
                        }
                    } else {
                        txtUnUsedAmount.setText(CommonUtil.convertObjToStr(sanctinAmt));
                    }
                }
            }
        }
    }

    private void displayAccountNameDetails() {
        if (txtOperativeAcNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", txtOperativeAcNo.getText());
            List accountLst = (List) ClientUtil.executeQuery("getAllCustomerName", whereMap);
            if (accountLst != null && accountLst.size() > 0) {
                whereMap = (HashMap) accountLst.get(0);
                lbOperativeAcName.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_NAME")));
            }
        }
        if (txtKCCAcNo.getText().length() > 0) {
            HashMap whereMap = new HashMap();
            whereMap.put("ACT_NUM", txtKCCAcNo.getText());
            List accountLst = (List) ClientUtil.executeQuery("getAllCustomerName", whereMap);
            if (accountLst != null && accountLst.size() > 0) {
                whereMap = (HashMap) accountLst.get(0);
                lbKCCAcName.setText(CommonUtil.convertObjToStr(whereMap.get("CUST_NAME")));
            }
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CPanel PanAcc_CD;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustomerID;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnDelete_ASW;
    private com.see.truetransact.uicomponent.CButton btnDelete_Mem;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNCLSanctionNo;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnNew_ASW;
    private com.see.truetransact.uicomponent.CButton btnNew_Mem;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnSave_ASW;
    private com.see.truetransact.uicomponent.CButton btnSave_Mem;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboCategory;
    private com.see.truetransact.uicomponent.CComboBox cboCrop;
    private com.see.truetransact.uicomponent.CComboBox cboFromAmount;
    private com.see.truetransact.uicomponent.CComboBox cboIntFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboLoanCategory;
    private com.see.truetransact.uicomponent.CComboBox cboPrincipalFrequency;
    private com.see.truetransact.uicomponent.CComboBox cboRepaymentPeriod;
    private com.see.truetransact.uicomponent.CComboBox cboSubCategory;
    private com.see.truetransact.uicomponent.CComboBox cboSubCategory1;
    private com.see.truetransact.uicomponent.CComboBox cboToAmount;
    private com.see.truetransact.uicomponent.CLabel lbKCCAcName;
    private com.see.truetransact.uicomponent.CLabel lbOperativeAcName;
    private com.see.truetransact.uicomponent.CLabel lblAmountReleased;
    private com.see.truetransact.uicomponent.CLabel lblAmountRequested;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanDate;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanDateVal;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanNo;
    private com.see.truetransact.uicomponent.CLabel lblClassificationSanNoVal;
    private com.see.truetransact.uicomponent.CLabel lblCrop;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblCustomerName;
    private com.see.truetransact.uicomponent.CLabel lblDueDate;
    private com.see.truetransact.uicomponent.CLabel lblDueDt;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount1;
    private com.see.truetransact.uicomponent.CLabel lblIntFrequency;
    private com.see.truetransact.uicomponent.CLabel lblInterestRepaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblKCCAcNo;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccName_CD;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccNo_CD;
    private com.see.truetransact.uicomponent.CLabel lblKCCAccNo_CDVal;
    private com.see.truetransact.uicomponent.CLabel lblLoanCategory;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNCLSanctionNo;
    private com.see.truetransact.uicomponent.CLabel lblNoOFInst;
    private com.see.truetransact.uicomponent.CLabel lblNoOfMembers;
    private com.see.truetransact.uicomponent.CLabel lblNoOfMembers1;
    private com.see.truetransact.uicomponent.CLabel lblNoOfMembers2;
    private com.see.truetransact.uicomponent.CLabel lblOperativeAcNo;
    private com.see.truetransact.uicomponent.CLabel lblPanNumber1;
    private com.see.truetransact.uicomponent.CLabel lblPenalInterest;
    private com.see.truetransact.uicomponent.CLabel lblPrincipalFrequency;
    private com.see.truetransact.uicomponent.CLabel lblRateOfInterest;
    private com.see.truetransact.uicomponent.CLabel lblReleaseDt;
    private com.see.truetransact.uicomponent.CLabel lblReleaseNo;
    private com.see.truetransact.uicomponent.CLabel lblReleaseNoVal;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblRepaymentFrequency;
    private com.see.truetransact.uicomponent.CLabel lblRepaymentPeriod;
    private com.see.truetransact.uicomponent.CLabel lblRequestedDt;
    private com.see.truetransact.uicomponent.CLabel lblSanctionAmount;
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
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblSubCategory;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CLabel lblToAmount1;
    private com.see.truetransact.uicomponent.CLabel lblTotMembers;
    private com.see.truetransact.uicomponent.CLabel lblTotaMembersVal;
    private com.see.truetransact.uicomponent.CLabel lblTotaMembersValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountPayable;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmountPayableVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmt;
    private com.see.truetransact.uicomponent.CLabel lblTotalAmtValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestPayable;
    private com.see.truetransact.uicomponent.CLabel lblTotalInterestPayableVal;
    private com.see.truetransact.uicomponent.CLabel lblTotalMembers;
    private com.see.truetransact.uicomponent.CLabel lblTotalMembers1;
    private com.see.truetransact.uicomponent.CLabel lblUnUsedAmount;
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
    private com.see.truetransact.uicomponent.CPanel panButton_AmtSlabWise;
    private com.see.truetransact.uicomponent.CPanel panButton_AmtSlabWise1;
    private com.see.truetransact.uicomponent.CPanel panClassDetails_Acc;
    private com.see.truetransact.uicomponent.CPanel panClassDetails_Details;
    private com.see.truetransact.uicomponent.CPanel panClassMembers;
    private com.see.truetransact.uicomponent.CPanel panClassificationDetails;
    private com.see.truetransact.uicomponent.CPanel panDocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panInsideDocumentDetails;
    private com.see.truetransact.uicomponent.CPanel panInterestDetails;
    private com.see.truetransact.uicomponent.CPanel panMemberDetails;
    private com.see.truetransact.uicomponent.CPanel panMinDepositPeriod3;
    private com.see.truetransact.uicomponent.CPanel panProd_CD;
    private com.see.truetransact.uicomponent.CPanel panReleaseDetails;
    private com.see.truetransact.uicomponent.CPanel panSanction;
    private com.see.truetransact.uicomponent.CPanel panSanctionCustDet;
    private com.see.truetransact.uicomponent.CPanel panSanctionCustomerDet;
    private com.see.truetransact.uicomponent.CPanel panSanctionDetails;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CPanel panTable_ASW;
    private com.see.truetransact.uicomponent.CPanel panTable_ASW1;
    private com.see.truetransact.uicomponent.CPanel panTermLoan;
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
    private com.see.truetransact.uicomponent.CSeparator sptBorroewrProfile1;
    private javax.swing.JSeparator sptCancel;
    private com.see.truetransact.uicomponent.CSeparator sptClassDetails;
    private com.see.truetransact.uicomponent.CSeparator sptClassification_vertical;
    private javax.swing.JSeparator sptException;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ASW;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_ASW1;
    private com.see.truetransact.uicomponent.CScrollPane srpTable_Document;
    private com.see.truetransact.uicomponent.CTabbedPane tabLimitAmount;
    private com.see.truetransact.uicomponent.CTable tblAmtSlabWise;
    private com.see.truetransact.uicomponent.CTable tblDocumentTable;
    private com.see.truetransact.uicomponent.CTable tblMemberDetails;
    private javax.swing.JToolBar tbrTermLoan;
    private com.see.truetransact.uicomponent.CDateField tdRequestedDt;
    private com.see.truetransact.uicomponent.CDateField tdtDueDate;
    private com.see.truetransact.uicomponent.CDateField tdtDueDt;
    private com.see.truetransact.uicomponent.CDateField tdtReleaseDt;
    private com.see.truetransact.uicomponent.CTextField txtAmount;
    private com.see.truetransact.uicomponent.CTextField txtAmountReleased;
    private com.see.truetransact.uicomponent.CTextField txtAmountRequested;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtEditTermLoanNo;
    private com.see.truetransact.uicomponent.CTextField txtKCCAcNo;
    private com.see.truetransact.uicomponent.CTextField txtNCLSanctionNo;
    private com.see.truetransact.uicomponent.CTextField txtNoOFInst;
    private com.see.truetransact.uicomponent.CTextField txtNoOfMembers;
    private com.see.truetransact.uicomponent.CTextField txtOperativeAcNo;
    private com.see.truetransact.uicomponent.CTextField txtPenalInterest;
    private com.see.truetransact.uicomponent.CTextField txtRateOfInterest;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtRepaymentPeriod;
    private com.see.truetransact.uicomponent.CTextField txtSanctionAmount;
    private com.see.truetransact.uicomponent.CTextField txtSlabNoOfMembers;
    private com.see.truetransact.uicomponent.CTextField txtTotMembers;
    private com.see.truetransact.uicomponent.CTextField txtUnUsedAmount;
    // End of variables declaration//GEN-END:variables
}
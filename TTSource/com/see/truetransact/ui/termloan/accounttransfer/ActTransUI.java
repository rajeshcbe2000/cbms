/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * ActTransUI.java
 *
 * Created on December 23, 2004, 4:18 PM
 */

package com.see.truetransact.ui.termloan.accounttransfer;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.ui.common.viewall.ViewAll;

import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
/**
 *
 * @author  152713
 */
public class ActTransUI extends CPanel implements java.util.Observer, UIMandatoryField{
    private ActTransOB observableActTrans;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.accounttransfer.ActTransRB", ProxyParameters.LANGUAGE);
//    PowerOfAttorneyRB resourceBundle = new PowerOfAttorneyRB();
    
    String lblStatus = "";
    String viewType = "";
    String TITLE = "Power of Attorney";
    private final static Logger log = Logger.getLogger(ActTransUI.class);
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String REJECT = "REJECT";
    private final        String EXCEPTION = "EXCEPTION";
    
    private boolean updateModePoA = false;
    
    int modePoA       = -1;
    
    private HashMap mandatoryMap;
    
    /** Creates new form PowerOfAttorneyUI */
    public ActTransUI(String strModule) { 
        initComponents();
        settlementUI(strModule);
    }
    public ActTransUI() { 
        initComponents();
        String strModule = "a";
        settlementUI(strModule);
      
    }
    
    private void settlementUI(String strModule){
        setFieldNames();
        internationalize();
        setMaxLength();
        setObservable(strModule);
        ClientUtil.enableDisable(this, false);
//        ClientUtil.enableDisable(panPowerAttorney_Part10, true);
        allEnableDisable();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPowerAttorney_Part5);
//        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPowerAttorney_Part2);
        //        setHelpMessage();
    }
    
    public ActTransOB getActTransOB(){
        return this.observableActTrans;
    }
    
    private void allEnableDisable(){
        setAllPoAEnableDisable(false);
        setPoANewOnlyEnable();
    }
    
    private void setObservable(String strModule){
        observableActTrans = new ActTransOB(strModule);
        observableActTrans.addObserver(this);
    }
    
    public boolean  checkmandatoryRemarks(){
        String str=CommonUtil.convertObjToStr(new MandatoryCheck().checkMandatory(getClass().getName(),panPowerAttorney_Part5));
        if(str.length()>0){
            ClientUtil.displayAlert(str);
            return true;
        }
        return false;
    }
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboPoACust", new Boolean(true));
        mandatoryMap.put("txtPoANo", new Boolean(true));
        mandatoryMap.put("cboAddrType_PoA", new Boolean(true));
        mandatoryMap.put("txtPoaHolderName", new Boolean(true));
        mandatoryMap.put("txtStreet_PowerAttroney", new Boolean(true));
        mandatoryMap.put("txtArea_PowerAttroney", new Boolean(true));
        mandatoryMap.put("cboCity_PowerAttroney", new Boolean(true));
        mandatoryMap.put("cboState_PowerAttroney", new Boolean(true));
        mandatoryMap.put("cboCountry_PowerAttroney", new Boolean(true));
        mandatoryMap.put("txtPin_PowerAttroney", new Boolean(true));
        mandatoryMap.put("txtPhone_PowerAttroney", new Boolean(true));
        mandatoryMap.put("tdtPeriodFrom_PowerAttroney", new Boolean(true));
        mandatoryMap.put("tdtPeriodTo_PowerAttroney", new Boolean(false));  //true changed as false by Rajesh.
        mandatoryMap.put("txtRemark_PowerAttroney", new Boolean(true));
        mandatoryMap.put("txtCustID_PoA", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void setHelpMessage() {
//        final ActTransRB objActTransRB = new ActTransMRB();
//        txtPoANo.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPoANo"));
//        cboAddrType_PoA.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAddrType_PoA"));
//        txtPoaHolderName.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPoaHolderName"));
//        cboPoACust.setHelpMessage(lblMsg, objMandatoryRB.getString("cboPoACust"));
//        txtStreet_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet_PowerAttroney"));
//        txtArea_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea_PowerAttroney"));
//        cboCity_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity_PowerAttroney"));
//        cboState_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState_PowerAttroney"));
//        cboCountry_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry_PowerAttroney"));
//        txtPin_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPin_PowerAttroney"));
//        txtPhone_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPhone_PowerAttroney"));
//        tdtPeriodFrom_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPeriodFrom_PowerAttroney"));
//        tdtPeriodTo_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtPeriodTo_PowerAttroney"));
//        txtRemark_PowerAttroney.setHelpMessage(lblMsg, objMandatoryRB.getString("txtRemark_PowerAttroney"));
    }
    
    private void setFieldNames() {
        panPowerAttorney.setName("panPowerAttorney");
        panPowerAttorney_Part1.setName("panPowerAttorney_Part1");
        lblBankName.setName("lblBankName");
        lblBranchName.setName("lblBranchName");
        lblRefNo.setName("lblRefNo");
        lblAmt.setName("lblAmt");
        lblSecDepRec.setName("lblSecDepRec");
        lblPoDdNo.setName("lblPoDdNo");
        lblPoDdDate.setName("lblPoDdDate");
        lblPoDdAmt.setName("lblPoDdAmt");
        lblRemarks.setName("lblRemarks");
        cboBankName.setName("cboBankName");
        cboBranchName.setName("cboBranchName");
    }
    
    private void internationalize() {
//        lblCustID_PoA.setText(resourceBundle.getString("lblCustID_PoA"));
//        lblPoANo.setText(resourceBundle.getString("lblPoANo"));
//        lblAddrType_PoA.setText(resourceBundle.getString("lblAddrType_PoA"));
//        lblPoaHolderName.setText(resourceBundle.getString("lblPoaHolderName"));
//        btnNew_PoA.setText(resourceBundle.getString("btnNew_PoA"));
//        lblPoACust.setText(resourceBundle.getString("lblPoACust"));
//        btnDelete_PoA.setText(resourceBundle.getString("btnDelete_PoA"));
//        btnSave_PoA.setText(resourceBundle.getString("btnSave_PoA"));
//        lblPin_PowerAttroney.setText(resourceBundle.getString("lblPin_PowerAttroney"));
//        lblRemark_PowerAttroney.setText(resourceBundle.getString("lblRemark_PowerAttroney"));
//        lblState_PowerAttroney.setText(resourceBundle.getString("lblState_PowerAttroney"));
//        lblPeriodFrom_PowerAttroney.setText(resourceBundle.getString("lblPeriodFrom_PowerAttroney"));
//        lblCity_PowerAttroney.setText(resourceBundle.getString("lblCity_PowerAttroney"));
//        lblArea_PowerAttroney.setText(resourceBundle.getString("lblArea_PowerAttroney"));
//        lblPeriodTo_PowerAttroney.setText(resourceBundle.getString("lblPeriodTo_PowerAttroney"));
//        lblCountry_PowerAttroney.setText(resourceBundle.getString("lblCountry_PowerAttroney"));
//        lblPhone_PowerAttroney.setText(resourceBundle.getString("lblPhone_PowerAttroney"));
//        lblStreet_PowerAttroney.setText(resourceBundle.getString("lblStreet_PowerAttroney"));
        lblBankName.setText(resourceBundle.getString("lblBankName"));
        lblBranchName.setText(resourceBundle.getString("lblBranchName"));
        lblRefNo.setText(resourceBundle.getString("lblRefNo"));
        lblAmt.setText(resourceBundle.getString("lblAmt"));
        lblSecDepRec.setText(resourceBundle.getString("lblSecDepRec"));
        lblPoDdNo.setText(resourceBundle.getString("lblPoDdNo"));
        lblPoDdDate.setText(resourceBundle.getString("lblPoDdDate"));
        lblPoDdAmt.setText(resourceBundle.getString("lblPoDdAmt"));
        lblRemarks.setText(resourceBundle.getString("lblRemarks"));
    }
    
    private void setMaxLength(){
//        txtStreet_PowerAttroney.setMaxLength(256);
//        txtArea_PowerAttroney.setMaxLength(128);
//        txtPin_PowerAttroney.setMaxLength(16);
//        txtPhone_PowerAttroney.setMaxLength(32);
//        txtPhone_PowerAttroney.setAllowNumber(true);
//        txtPin_PowerAttroney.setValidation(new PincodeValidation_IN());
//        txtPoaHolderName.setMaxLength(32);
        txtAmt.setValidation(new CurrencyValidation());
        txtPoDdAmt.setValidation(new CurrencyValidation());
    }
    
    public void update(java.util.Observable o, Object arg) {
   
            cboBankName.setSelectedItem(observableActTrans.getCboBankName());
            cboBranchName.setSelectedItem(observableActTrans.getCboBranchName());
//            txtActNo.setText(observableSettle.getTxtActNo());
            txtRefNo.setText(observableActTrans.getTxtRefNo());
            txtAmt.setText(observableActTrans.getTxtAmt());
            txtSecDepRec.setText(observableActTrans.getTxtSecDepRec());
            txtPoDdNo.setText(observableActTrans.getTxtPoDdNo());
            tdtPoDdDate.setDateValue(observableActTrans.getTdtPoDdDate());
            txtPoDdAmt.setText(observableActTrans.getTxtPoDdAmt());
            txtRemarks.setText(observableActTrans.getTxtRemarks());
        
    }
    
    public void updateOBFields() {
//        observablePoA.setTxtPoANo(txtPoANo.getText());
//        observablePoA.setTxtCustID_PoA(txtCustID_PoA.getText());
//        observablePoA.setTxtPoaHolderName(txtPoaHolderName.getText());
//        observablePoA.setCboAddrType_PoA(CommonUtil.convertObjToStr(cboAddrType_PoA.getSelectedItem()));
//        observablePoA.setCboPoACust(CommonUtil.convertObjToStr(cboPoACust.getSelectedItem()));
//        observablePoA.setTxtStreet_PowerAttroney(txtStreet_PowerAttroney.getText());
//        observablePoA.setTxtArea_PowerAttroney(txtArea_PowerAttroney.getText());
//        observablePoA.setCboCity_PowerAttroney(CommonUtil.convertObjToStr(cboCity_PowerAttroney.getSelectedItem()));
//        observablePoA.setCboState_PowerAttroney(CommonUtil.convertObjToStr(cboState_PowerAttroney.getSelectedItem()));
//        observablePoA.setCboCountry_PowerAttroney(CommonUtil.convertObjToStr(cboCountry_PowerAttroney.getSelectedItem()));
//        observablePoA.setTxtPin_PowerAttroney(txtPin_PowerAttroney.getText());
//        observablePoA.setTxtPhone_PowerAttroney(txtPhone_PowerAttroney.getText());
//        observablePoA.setTdtPeriodFrom_PowerAttroney(tdtPeriodFrom_PowerAttroney.getDateValue());
//        observablePoA.setTdtPeriodTo_PowerAttroney(tdtPeriodTo_PowerAttroney.getDateValue());
//        observablePoA.setTxtRemark_PowerAttroney(txtRemark_PowerAttroney.getText());
//        observableSettle.setCboBankName(CommonUtil.convertObjToStr(cboBankName.getSelectedItem()));
//        observableSettle.setCboBranchName(CommonUtil.convertObjToStr(cboBranchName.getSelectedItem()));
//        observableSettle.setTxtActNo(txtActNo.getText());
//        observableSettle.setTxtFromChqNo(txtFromChqNo.getText());
//        observableSettle.setTxtToChqNo(txtToChqNo.getText());
//        observableSettle.setTxtQty(txtQty.getText());
//        observableSettle.setTdtChqDate(tdtChqDate.getDateValue());
//        observableSettle.setTxtChqAmt(txtChqAmt.getText());
//        observableSettle.setTdtClearingDt(tdtClearingDt.getDateValue());
//        observableSettle.setCboBounReason(CommonUtil.convertObjToStr(cboBounReason.getSelectedItem()));
//        observableSettle.setTxtRemarks(txtRemarks.getText());
//        observableSettle.setRdoReturnChq_No(rdoReturnChq_No.isSelected());
//        observableSettle.setRdoReturnChq_Yes(rdoReturnChq_Yes.isSelected());
//        observableSettle.setRdoActype_Sb(rdoActype_Sb.isSelected());
//        observableSettle.setRdoActype_Ca(rdoActype_Ca.isSelected());
//        observableSettle.setRdoActype_Od(rdoActype_Od.isSelected());
//        observableSettle.setRdoChqBounce_Yes(rdoChqBounce_Yes.isSelected());
//        observableSettle.setRdoChqBounce_No(rdoChqBounce_No.isSelected());
//        observableSettle.setRdoSetMode_Ecs(rdoSetMode_Ecs.isSelected());
//        observableSettle.setRdoSetMode_Othrs(rdoSetMode_Othrs.isSelected());
//        observableSettle.setRdoSetMode_Pdc(rdoSetMode_Pdc.isSelected());
        observableActTrans.setCboBankName(CommonUtil.convertObjToStr(cboBankName.getSelectedItem()));
        observableActTrans.setCboBranchName(CommonUtil.convertObjToStr(cboBranchName.getSelectedItem()));
        observableActTrans.setTxtRefNo(txtRefNo.getText());
        observableActTrans.setTxtAmt(txtAmt.getText());
        observableActTrans.setTxtSecDepRec(txtSecDepRec.getText());
        observableActTrans.setTxtPoDdNo(txtPoDdNo.getText());
        observableActTrans.setTxtPoDdAmt(txtPoDdAmt.getText());
        observableActTrans.setTxtRemarks(txtRemarks.getText()); 
        observableActTrans.setTdtPoDdDate(tdtPoDdDate.getDateValue());
    }
    
//    public void updateOBFieldsBank(){
//        observableSettle.setCboBankName(CommonUtil.convertObjToStr(cboBankName.getSelectedItem()));
//        observableSettle.setCboBranchName(CommonUtil.convertObjToStr(cboBranchName.getSelectedItem()));
//        observableSettle.setTxtActNo(txtActNo.getText());
//        observableSettle.setRdoActype_Sb(rdoActype_Sb.isSelected());
//        observableSettle.setRdoActype_Ca(rdoActype_Ca.isSelected());
//        observableSettle.setRdoActype_Od(rdoActype_Od.isSelected());
//        observableSettle.setRdoChqBounce_Yes(rdoChqBounce_Yes.isSelected());
//        observableSettle.setRdoChqBounce_No(rdoChqBounce_No.isSelected());
//        observableSettle.setRdoSetMode_Ecs(rdoSetMode_Ecs.isSelected());
//        observableSettle.setRdoSetMode_Othrs(rdoSetMode_Othrs.isSelected());
//        observableSettle.setRdoSetMode_Pdc(rdoSetMode_Pdc.isSelected());
//    }
//    private void removeRadioButtons() {
//        removeActTypRadioBtns();
//        removeSetModeRadioBtns();
//        removeChqBounceRadioBtns();
//        removeReturnChqRadioBtns();
//    }
//    private void addRadioButtons(){
//        addActTypRadioBtns();
//        addSetModeRadioBtns();
//        addChqBounceRadioBtns();
//        addReturnChqRadioBtns();
//    }
    
//    private void removeReturnChqRadioBtns(){
//        rdoReturnChq_Yes.remove(rdoReturnChq_Yes);
//        rdoReturnChq_No.remove(rdoReturnChq_No);
//    }
//    private void removeActTypRadioBtns(){
//        rdoActype_Sb.remove(rdoActype_Sb);
//        rdoActype_Ca.remove(rdoActype_Ca);
//        rdoActype_Od.remove(rdoActype_Od);
//    }
//    private void removeSetModeRadioBtns(){
//        rdoSetMode_Ecs.remove(rdoSetMode_Ecs);
//        rdoSetMode_Othrs.remove(rdoSetMode_Othrs);
//        rdoSetMode_Pdc.remove(rdoSetMode_Pdc);
//    }
//    private void removeChqBounceRadioBtns(){
//        rdoChqBounce_Yes.remove(rdoChqBounce_Yes);
//        rdoChqBounce_No.remove(rdoChqBounce_No);
//    }
//    private void addReturnChqRadioBtns(){
//        rdoReturnChq = new CButtonGroup();
//        rdoReturnChq.add(rdoReturnChq_Yes);
//        rdoReturnChq.add(rdoReturnChq_No);
//    }
//    private void addActTypRadioBtns(){
//        rdoActTyp = new CButtonGroup();
//        rdoActTyp.add(rdoActype_Sb);
//        rdoActTyp.add(rdoActype_Ca);
//        rdoActTyp.add(rdoActype_Od);
//        
//    }
//    private void addChqBounceRadioBtns(){
//        rdoChqBounce = new CButtonGroup();
//        rdoChqBounce.add(rdoChqBounce_Yes);
//        rdoChqBounce.add(rdoChqBounce_No);
//    }
//    private void addSetModeRadioBtns(){
//        rdoSetMode = new CButtonGroup();
//        rdoSetMode.add(rdoSetMode_Ecs);
//        rdoSetMode.add(rdoSetMode_Othrs);
//        rdoSetMode.add(rdoSetMode_Pdc);
//    }
    private void initComponentData(){
        cboBankName.setModel(observableActTrans.getCbmBankName());
//        cboBranchName.setModel(observableSettle.getCbmBranchName());
//        cboBounReason.setModel(observableSettle.getCbmBounReason());
        cboBranchName.setModel(new ComboBoxModel());
//        cboCountry_PowerAttroney.setModel(observablePoA.getCbmCountry_PowerAttroney());
//        cboPoACust.setModel(observablePoA.getCbmPoACust());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        rdoActTyp = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoSetMode = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoChqBounce = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoReturnChq = new com.see.truetransact.uicomponent.CButtonGroup();
        panPowerAttorney = new com.see.truetransact.uicomponent.CPanel();
        panPowerAttorney_Part1 = new com.see.truetransact.uicomponent.CPanel();
        lblPoDdNo = new com.see.truetransact.uicomponent.CLabel();
        txtPoDdNo = new com.see.truetransact.uicomponent.CTextField();
        txtPoDdAmt = new com.see.truetransact.uicomponent.CTextField();
        lblPoDdDate = new com.see.truetransact.uicomponent.CLabel();
        tdtPoDdDate = new com.see.truetransact.uicomponent.CDateField();
        lblPoDdAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panAllAccountDetails = new com.see.truetransact.uicomponent.CPanel();
        panPowerAttorney_Part5 = new com.see.truetransact.uicomponent.CPanel();
        lblBankName = new com.see.truetransact.uicomponent.CLabel();
        lblRefNo = new com.see.truetransact.uicomponent.CLabel();
        txtRefNo = new com.see.truetransact.uicomponent.CTextField();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        cboBankName = new com.see.truetransact.uicomponent.CComboBox();
        cboBranchName = new com.see.truetransact.uicomponent.CComboBox();
        lblAmt = new com.see.truetransact.uicomponent.CLabel();
        txtAmt = new com.see.truetransact.uicomponent.CTextField();
        panOperations3 = new com.see.truetransact.uicomponent.CPanel();
        lblSecDepRec = new com.see.truetransact.uicomponent.CLabel();
        srpTxtAreaParticulars = new com.see.truetransact.uicomponent.CScrollPane();
        txtSecDepRec = new com.see.truetransact.uicomponent.CTextArea();

        setLayout(new java.awt.GridBagLayout());

        panPowerAttorney.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney.setBorder(new javax.swing.border.TitledBorder("Accont Transfer Details"));
        panPowerAttorney.setMinimumSize(new java.awt.Dimension(850, 500));
        panPowerAttorney.setPreferredSize(new java.awt.Dimension(850, 500));
        panPowerAttorney_Part1.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part1.setBorder(new javax.swing.border.TitledBorder("Balance Settlement Details"));
        panPowerAttorney_Part1.setMaximumSize(new java.awt.Dimension(290, 130));
        panPowerAttorney_Part1.setMinimumSize(new java.awt.Dimension(290, 130));
        panPowerAttorney_Part1.setPreferredSize(new java.awt.Dimension(350, 110));
        lblPoDdNo.setText("PO/DD No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblPoDdNo, gridBagConstraints);

        txtPoDdNo.setAllowAll(true);
        txtPoDdNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPoDdNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtPoDdNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPoDdNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(txtPoDdNo, gridBagConstraints);

        txtPoDdAmt.setAllowAll(true);
        txtPoDdAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPoDdAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(txtPoDdAmt, gridBagConstraints);

        lblPoDdDate.setText("Date");
        lblPoDdDate.setMaximumSize(new java.awt.Dimension(10, 18));
        lblPoDdDate.setMinimumSize(new java.awt.Dimension(10, 18));
        lblPoDdDate.setPreferredSize(new java.awt.Dimension(30, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblPoDdDate, gridBagConstraints);

        tdtPoDdDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtPoDdDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtPoDdDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPoDdDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(tdtPoDdDate, gridBagConstraints);

        lblPoDdAmt.setText("Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblPoDdAmt, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblRemarks, gridBagConstraints);

        txtRemarks.setAllowAll(true);
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(txtRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(30, 76, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPowerAttorney.add(panPowerAttorney_Part1, gridBagConstraints);

        panAllAccountDetails.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part5.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part5.setMaximumSize(new java.awt.Dimension(350, 110));
        panPowerAttorney_Part5.setMinimumSize(new java.awt.Dimension(350, 110));
        panPowerAttorney_Part5.setPreferredSize(new java.awt.Dimension(350, 110));
        lblBankName.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 37, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPowerAttorney_Part5.add(lblBankName, gridBagConstraints);

        lblRefNo.setText("Taken over Ref No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(lblRefNo, gridBagConstraints);

        txtRefNo.setAllowAll(true);
        txtRefNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtRefNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPowerAttorney_Part5.add(txtRefNo, gridBagConstraints);

        lblBranchName.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(lblBranchName, gridBagConstraints);

        cboBankName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboBankName.setMinimumSize(new java.awt.Dimension(100, 21));
        cboBankName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboBankNameItemStateChanged(evt);
            }
        });
        cboBankName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboBankNameActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPowerAttorney_Part5.add(cboBankName, gridBagConstraints);

        cboBranchName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboBranchName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPowerAttorney_Part5.add(cboBranchName, gridBagConstraints);

        lblAmt.setText("Taken over Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(lblAmt, gridBagConstraints);

        txtAmt.setAllowAll(true);
        txtAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(1, 4, 1, 1);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPowerAttorney_Part5.add(txtAmt, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panAllAccountDetails.add(panPowerAttorney_Part5, gridBagConstraints);

        panOperations3.setLayout(new java.awt.GridBagLayout());

        panOperations3.setMinimumSize(new java.awt.Dimension(480, 110));
        panOperations3.setPreferredSize(new java.awt.Dimension(480, 110));
        lblSecDepRec.setText("Security documents recieved");
        lblSecDepRec.setMaximumSize(new java.awt.Dimension(133, 18));
        lblSecDepRec.setMinimumSize(new java.awt.Dimension(133, 18));
        lblSecDepRec.setPreferredSize(new java.awt.Dimension(133, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 44, 1);
        panOperations3.add(lblSecDepRec, gridBagConstraints);

        srpTxtAreaParticulars.setMinimumSize(new java.awt.Dimension(300, 95));
        srpTxtAreaParticulars.setPreferredSize(new java.awt.Dimension(300, 95));
        txtSecDepRec.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        txtSecDepRec.setLineWrap(true);
        txtSecDepRec.setMinimumSize(new java.awt.Dimension(50, 30));
        txtSecDepRec.setPreferredSize(new java.awt.Dimension(50, 30));
        txtSecDepRec.setAutoscrolls(false);
        srpTxtAreaParticulars.setViewportView(txtSecDepRec);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 6, 0);
        panOperations3.add(srpTxtAreaParticulars, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 40, 0, 0);
        panAllAccountDetails.add(panOperations3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 45, 0, 0);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panPowerAttorney.add(panAllAccountDetails, gridBagConstraints);

        add(panPowerAttorney, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents
    public void populateData() {
        observableActTrans.doAction(2);
    }
    private void txtPoDdNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPoDdNoFocusLost
        // TODO add your handling code here:
      
    }//GEN-LAST:event_txtPoDdNoFocusLost

    private void cboBankNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboBankNameItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBankNameItemStateChanged

    private void cboBankNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBankNameActionPerformed
        // TODO add your handling code here:
        observableActTrans.getBranchData("");
        cboBranchName.setModel(observableActTrans.getCbmBranchName());
    }//GEN-LAST:event_cboBankNameActionPerformed
public  void setActTransferEnableDisable(boolean flag){
     ClientUtil.enableDisable(panPowerAttorney_Part5,flag);
        ClientUtil.enableDisable(panPowerAttorney_Part1,flag);
}
    public void resetFrom(){
    cboBankName.setSelectedItem("");
    cboBranchName.setSelectedItem("");
    txtRefNo.setText("");
    txtAmt.setText("");
    txtSecDepRec.setText("");
    txtPoDdNo.setText("");
    tdtPoDdDate.setDateValue("");
    txtPoDdAmt.setText("");
    txtRemarks.setText("");
}    private void btnCustID_SelectActionPerformed() {
        callView("CUSTOMER ID");
    }
    
    public void callView(String currField) {
        viewType = currField;
        // If Customer Id is selected OR JointAccnt New is clciked, show the popup Screen of Customer Table
        if ((currField.equals("CUSTOMER ID"))){
            HashMap viewMap = new HashMap();
            viewMap.put("MAPNAME", "getCustomers");
            HashMap whereMap = new HashMap();
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, "", viewMap).show();
            whereMap = null;
        }
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        
        if (viewType != null) {
//            if(viewType.equals("CUSTOMER ID")){
//                observableSettle.setTxtCustID_PoA(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
//                //txtCustID_PoA.setText(observablePoA.getTxtCustID_PoA());
//                final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
//                observableSettle.setPoACustAddr(CustID);
//                observableSettle.setPoACustName(CustID);
//                observableSettle.setPoACustPhone(CustID);
//                observableSettle.ttNotifyObservers();
//                setAllPoAEnableDisable(true);
//                setPoANewSaveOnlyEnable();
//                txtCustID_PoA.setEditable(true);
            }
        }  
    private void tdtPoDdDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPoDdDateFocusLost
        // TODO add your handling code here:
        tdtPeriodFrom_PowerAttroneyFocusLost();
    }//GEN-LAST:event_tdtPoDdDateFocusLost
    
    private void tdtPeriodFrom_PowerAttroneyFocusLost(){
//        ClientUtil.validateLTDate(tdtPeriodFrom_PowerAttroney);
        // To check whether this From date is less than this details To date
//        ClientUtil.validateFromDate(tdtPeriodFrom_PowerAttroney, tdtPeriodTo_PowerAttroney.getDateValue());
    }
    
    public void setLblStatus(String lblStatus){
        this.lblStatus = lblStatus;
    }
    
    public String getLblStatus(){
        return lblStatus;
    }
    
    
    
    public void setCboPoACustModel(){
//        cboPoACust.setModel(observablePoA.getCbmPoACust());
    }
//    public boolean checkCustIDExistInJointAcctAndPoA(String CustID){
////        return observablePoA.checkCustIDExistInJointAcctAndPoA(CustID);
//    }
    
    public void resetPoAForm(){
//        observableSettle.resetPoAForm();
//        observableSettle.ttNotifyObservers();
    }
    
    public void resetAllFieldsPoA(){
//        observableSettle.resetAllFieldsInPoA();
//        observableSettle.ttNotifyObservers();
    }
    
    public void resetPoACustID(String Cust_ID){
//        observableSettle.resetPoACustID(Cust_ID);
//        observableSettle.ttNotifyObservers();
    }
    
    public void clearCboPoACust_ID(){
//        observableSettle.clearCboPoACust_ID();
    }
    
    public void setbtnCustEnableDisable(boolean val){
//        btnCustID_Select.setEnabled(val);
    }
    
    public void setAllPoAEnableDisable(boolean val){
//        txtPoANo.setEditable(false);
////        txtPoANo.setEnabled(val);
//        cboAddrType_PoA.setEnabled(false);
//        txtCustID_PoA.setEditable(false);
//        txtCustID_PoA.setEnabled(val);
//        tdtPeriodFrom_PowerAttroney.setEnabled(val);
//        tdtPeriodTo_PowerAttroney.setEnabled(val);
//        cboPoACust.setEnabled(val);
//        txtArea_PowerAttroney.setEditable(false);
//        txtArea_PowerAttroney.setEnabled(val);
//        txtPhone_PowerAttroney.setEditable(false);
//        txtPhone_PowerAttroney.setEnabled(val);
//        txtPin_PowerAttroney.setEditable(false);
//        txtPin_PowerAttroney.setEnabled(val);
//        txtPoaHolderName.setEditable(false);
//        txtPoaHolderName.setEnabled(val);
//        txtRemark_PowerAttroney.setEnabled(val);
//        cboState_PowerAttroney.setEnabled(false);
//        txtStreet_PowerAttroney.setEditable(false);
//        txtStreet_PowerAttroney.setEnabled(val);
//        cboCity_PowerAttroney.setEnabled(false);
//        cboCountry_PowerAttroney.setEnabled(false);
//        setbtnCustEnableDisable(val);
//        txtFromChqNo.setEnabled(val);
//        txtToChqNo.setEnabled(val);
//        txtQty.setEnabled(val);
//        tdtChqDate.setEnabled(val);
//        txtChqAmt.setEnabled(val);
//        tdtClearingDt.setEnabled(val);
//        cboBounReason.setEnabled(val);
//        txtRemarks.setEnabled(val);
//        observableSettle.ttNotifyObservers();
    }
    
    //To enable and disable the buttons in PoA
    public void setPoAToolBtnsEnableDisable(boolean val){
//        btnNew_PoA.setEnabled(val);
//        btnSave_PoA.setEnabled(val);
//        btnDelete_PoA.setEnabled(val);
    }
    
    public void setPoANewOnlyEnable(){
//        btnNew_PoA.setEnabled(true);
//        btnSave_PoA.setEnabled(false);
//        btnDelete_PoA.setEnabled(false);
    }
    
    public void setPoANewSaveOnlyEnable(){
//        btnNew_PoA.setEnabled(true);
//        btnSave_PoA.setEnabled(true);
//        btnDelete_PoA.setEnabled(false);
    }
    
    public void ttNotifyObservers(){
        observableActTrans.ttNotifyObservers();
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** returns the PoA CTable Row count
     */
//    public int getTblRowCount(){
////        return tblPowerAttroney.getRowCount();
//    }
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        ActTransUI sett = new ActTransUI("TL");
        frm.getContentPane().add(sett);
        sett.show();
        frm.setSize(600, 500);
        frm.show();
    }
    
    /**
     * Getter for property updateModePoA.
     * @return Value of property updateModePoA.
     */
    public boolean isUpdateModePoA() {
        return updateModePoA;
    }
    
    /**
     * Setter for property updateModePoA.
     * @param updateModePoA New value of property updateModePoA.
     */
    public  void setUpdateModePoA(boolean updateModePoA) {
        this.updateModePoA = updateModePoA;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CComboBox cboBankName;
    private com.see.truetransact.uicomponent.CComboBox cboBranchName;
    private com.see.truetransact.uicomponent.CLabel lblAmt;
    private com.see.truetransact.uicomponent.CLabel lblBankName;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblPoDdAmt;
    private com.see.truetransact.uicomponent.CLabel lblPoDdDate;
    private com.see.truetransact.uicomponent.CLabel lblPoDdNo;
    private com.see.truetransact.uicomponent.CLabel lblRefNo;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblSecDepRec;
    private com.see.truetransact.uicomponent.CPanel panAllAccountDetails;
    private com.see.truetransact.uicomponent.CPanel panOperations3;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part1;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part5;
    private com.see.truetransact.uicomponent.CButtonGroup rdoActTyp;
    private com.see.truetransact.uicomponent.CButtonGroup rdoChqBounce;
    private com.see.truetransact.uicomponent.CButtonGroup rdoReturnChq;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSetMode;
    private com.see.truetransact.uicomponent.CScrollPane srpTxtAreaParticulars;
    private com.see.truetransact.uicomponent.CDateField tdtPoDdDate;
    private com.see.truetransact.uicomponent.CTextField txtAmt;
    private com.see.truetransact.uicomponent.CTextField txtPoDdAmt;
    private com.see.truetransact.uicomponent.CTextField txtPoDdNo;
    private com.see.truetransact.uicomponent.CTextField txtRefNo;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextArea txtSecDepRec;
    // End of variables declaration//GEN-END:variables
    
}

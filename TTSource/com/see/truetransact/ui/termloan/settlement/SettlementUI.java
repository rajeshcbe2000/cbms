/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * SettlementUI.java
 *
 * Created on December 23, 2004, 4:18 PM
 */

package com.see.truetransact.ui.termloan.settlement;

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
import com.see.truetransact.uivalidation.NumericValidation;

import java.util.HashMap;
import java.util.ArrayList;
import org.apache.log4j.Logger;
/**
 *
 * @author  152713
 */
public class SettlementUI extends CPanel implements java.util.Observer, UIMandatoryField{
    private SettlementOB observableSettle;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.termloan.settlement.SettlementRB", ProxyParameters.LANGUAGE);
//    PowerOfAttorneyRB resourceBundle = new PowerOfAttorneyRB();
    
    String lblStatus = "";
    String viewType = "";
    String TITLE = "Power of Attorney";
    private final static Logger log = Logger.getLogger(SettlementUI.class);
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String REJECT = "REJECT";
    private final        String EXCEPTION = "EXCEPTION";
    
    private boolean updateModePoA = false;
    
    int modePoA       = -1;
    
    private HashMap mandatoryMap;
    
    /** Creates new form PowerOfAttorneyUI */
    public SettlementUI(String strModule) { 
        initComponents();
        settlementUI(strModule);
    }
    public SettlementUI() { 
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
        ClientUtil.enableDisable(panPowerAttorney_Part10, true);
        allEnableDisable();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPowerAttorney_Part1);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPowerAttorney_Part2);
        //        setHelpMessage();
         btnNew_PoA.setEnabled(false);
         btnSave_PoA2.setEnabled(false);
         panPowerAttorney_Part8.setVisible(false);
    }
    
    public SettlementOB getSettlementOB(){
        return this.observableSettle;
    }
    
    private void allEnableDisable(){
        setAllPoAEnableDisable(false);
        setPoANewOnlyEnable();
    }
    
    private void setObservable(String strModule){
        observableSettle = new SettlementOB(strModule);
        observableSettle.addObserver(this);
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
        final SettlementMRB objSettleRB = new SettlementMRB();
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
//        lblCustID_PoA.setName("lblCustID_PoA");
//        txtCustID_PoA.setName("txtCustID_PoA");
//        btnCustID_Select.setName("btnCustID_Select");
//        cboAddrType_PoA.setName("cboAddrType_PoA");
//        txtPoANo.setName("txtPoANo");
//        btnDelete_PoA.setName("btnDelete_PoA");
//        btnNew_PoA.setName("btnNew_PoA");
//        btnSave_PoA.setName("btnSave_PoA");
//        cboPoACust.setName("cboPoACust");
//        lblPoACust.setName("lblPoACust");
//        lblPoaHolderName.setName("lblPoaHolderName");
//        txtPoaHolderName.setName("txtPoaHolderName");
//        cboCity_PowerAttroney.setName("cboCity_PowerAttroney");
//        cboCountry_PowerAttroney.setName("cboCountry_PowerAttroney");
//        cboState_PowerAttroney.setName("cboState_PowerAttroney");
//        lblArea_PowerAttroney.setName("lblArea_PowerAttroney");
//        lblCity_PowerAttroney.setName("lblCity_PowerAttroney");
//        lblCountry_PowerAttroney.setName("lblCountry_PowerAttroney");
//        lblPeriodFrom_PowerAttroney.setName("lblPeriodFrom_PowerAttroney");
//        lblPeriodTo_PowerAttroney.setName("lblPeriodTo_PowerAttroney");
//        lblPhone_PowerAttroney.setName("lblPhone_PowerAttroney");
//        lblPin_PowerAttroney.setName("lblPin_PowerAttroney");
//        lblRemark_PowerAttroney.setName("lblRemark_PowerAttroney");
//        lblState_PowerAttroney.setName("lblState_PowerAttroney");
//        lblStreet_PowerAttroney.setName("lblStreet_PowerAttroney");
        panPowerAttorney.setName("panPowerAttorney");
        panPowerAttorney_Part1.setName("panPowerAttorney_Part1");
        panPowerAttorney_Part2.setName("panPowerAttorney_Part2");
        panPowerAttorney_Part3.setName("panPowerAttorney_Part3");
        panPowerAttroney_Table.setName("panPowerAttroney_Table");
//        sptPowerAttroney.setName("sptPowerAttroney");
        srpPowerAttroney.setName("srpPowerAttroney");
        tblPowerAttroney.setName("tblPowerAttroney");
//        tdtPeriodFrom_PowerAttroney.setName("tdtPeriodFrom_PowerAttroney");
//        tdtPeriodTo_PowerAttroney.setName("tdtPeriodTo_PowerAttroney");
//        txtArea_PowerAttroney.setName("txtArea_PowerAttroney");
//        txtPhone_PowerAttroney.setName("txtPhone_PowerAttroney");
//        txtPin_PowerAttroney.setName("txtPin_PowerAttroney");
//        txtRemark_PowerAttroney.setName("txtRemark_PowerAttroney");
//        txtStreet_PowerAttroney.setName("txtStreet_PowerAttroney");
        lblBankName.setName("lblBankName");
        lblBranchName.setName("lblBranchName");
        lblAccType.setName("lblAccType");
        lblActNo.setName("lblActNo");
        lblSetMode.setName("lblSetMode");
        lblFromChqNo.setName("lblFromChqNo");
        lblToChqNo.setName("lblToChqNo");
        lblQty.setName("lblQty");
        lblChqDate.setName("lblChqDate");
        lblChqAmt.setName("lblChqAmt");
        lblClearingDt.setName("lblClearingDt");
        lblChqBounce.setName("lblChqBounce");
        lblBounReason.setName("lblBounReason");
        lblRemarks.setName("lblRemarks");
        cboBankName.setName("cboBankName");
        cboBranchName.setName("cboBranchName");
        txtActNo.setName("txtActNo");
        txtFromChqNo.setName("txtFromChqNo");
        txtToChqNo.setName("txtToChqNo");
        txtQty.setName("txtQty");
        tdtChqDate.setName("tdtChqDate");
        txtChqAmt.setName("txtChqAmt");
        tdtClearingDt.setName("tdtClearingDt");
        cboBounReason.setName("cboBounReason");
        txtRemarks.setName("txtRemarks");
        rdoActype_Sb.setName("rdoActype_Sb");
        rdoActype_Ca.setName("rdoActype_Ca");
        rdoActype_Od.setName("rdoActype_Od");
        rdoSetMode_Ecs.setName("rdoSetMode_Ecs");
        rdoSetMode_Pdc.setName("rdoSetMode_Pdc");
        rdoSetMode_Othrs.setName("rdoSetMode_Othrs");
        rdoChqBounce_Yes.setName("rdoChqBounce_Yes");
        rdoChqBounce_No.setName("rdoChqBounce_No");
        rdoReturnChq_No.setName("rdoReturnChq_No");
        rdoReturnChq_Yes.setName("rdoReturnChq_Yes");
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
        lblAccType.setText(resourceBundle.getString("lblAccType"));
        lblActNo.setText(resourceBundle.getString("lblActNo"));
        lblSetMode.setText(resourceBundle.getString("lblSetMode"));
        lblFromChqNo.setText(resourceBundle.getString("lblFromChqNo"));
        lblToChqNo.setText(resourceBundle.getString("lblToChqNo"));
        lblQty.setText(resourceBundle.getString("lblQty"));
        lblChqDate.setText(resourceBundle.getString("lblChqDate"));
        lblChqAmt.setText(resourceBundle.getString("lblChqAmt"));
        lblClearingDt.setText(resourceBundle.getString("lblClearingDt"));
        lblChqBounce.setText(resourceBundle.getString("lblChqBounce"));
        lblBounReason.setText(resourceBundle.getString("lblBounReason"));
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
        txtChqAmt.setValidation(new CurrencyValidation());
        txtFromChqNo.setValidation(new NumericValidation());
    }
    
    public void update(java.util.Observable o, Object arg) {
        removeRadioButtons();
//        txtPoaHolderName.setText(observablePoA.getTxtPoaHolderName());
//        txtCustID_PoA.setText(observablePoA.getTxtCustID_PoA());
//        cboAddrType_PoA.setSelectedItem(observablePoA.getCboAddrType_PoA());
//        txtPoANo.setText(observablePoA.getTxtPoANo());
//        txtStreet_PowerAttroney.setText(observablePoA.getTxtStreet_PowerAttroney());
//        txtArea_PowerAttroney.setText(observablePoA.getTxtArea_PowerAttroney());
//        cboCity_PowerAttroney.setSelectedItem(observablePoA.getCboCity_PowerAttroney());
//        cboState_PowerAttroney.setSelectedItem(observablePoA.getCboState_PowerAttroney());
//        cboCountry_PowerAttroney.setSelectedItem(observablePoA.getCboCountry_PowerAttroney());
//        txtPin_PowerAttroney.setText(observablePoA.getTxtPin_PowerAttroney());
//        txtPhone_PowerAttroney.setText(observablePoA.getTxtPhone_PowerAttroney());
//        tdtPeriodFrom_PowerAttroney.setDateValue(observablePoA.getTdtPeriodFrom_PowerAttroney());
//        tdtPeriodTo_PowerAttroney.setDateValue(observablePoA.getTdtPeriodTo_PowerAttroney());
//        txtRemark_PowerAttroney.setText(observablePoA.getTxtRemark_PowerAttroney());
//        if(!observableSettle.isIsEditMode())
            tblPowerAttroney.setModel(observableSettle.getTblPoA());
//        cboPoACust.setSelectedItem(observablePoA.getCboPoACust());
            cboBankName.setSelectedItem(observableSettle.getCboBankName());
            cboBranchName.setSelectedItem(observableSettle.getCboBranchName());
            txtActNo.setText(observableSettle.getTxtActNo());
            txtFromChqNo.setText(observableSettle.getTxtFromChqNo());
            txtToChqNo.setText(observableSettle.getTxtToChqNo());
            txtQty.setText(observableSettle.getTxtQty());
            tdtChqDate.setDateValue(observableSettle.getTdtChqDate());
            txtChqAmt.setText(observableSettle.getTxtChqAmt());
            tdtClearingDt.setDateValue(observableSettle.getTdtClearingDt());
            cboBounReason.setSelectedItem(observableSettle.getCboBounReason());
            txtRemarks.setText(observableSettle.getTxtRemarks());
            rdoActype_Sb.setSelected(observableSettle.isRdoActype_Sb());
            rdoActype_Ca.setSelected(observableSettle.isRdoActype_Ca());
            rdoActype_Od.setSelected(observableSettle.isRdoActype_Od());
            rdoSetMode_Ecs.setSelected(observableSettle.isRdoSetMode_Ecs());
            rdoSetMode_Pdc.setSelected(observableSettle.isRdoSetMode_Pdc());
            rdoSetMode_Othrs.setSelected(observableSettle.isRdoSetMode_Othrs());
            rdoChqBounce_Yes.setSelected(observableSettle.isRdoChqBounce_Yes());
            rdoChqBounce_No.setSelected(observableSettle.isRdoChqBounce_No());
            rdoReturnChq_Yes.setSelected(observableSettle.isRdoReturnChq_Yes());
            rdoReturnChq_No.setSelected(observableSettle.isRdoReturnChq_No());
            addRadioButtons();
        
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
        observableSettle.setTxtFromChqNo(txtFromChqNo.getText());
        observableSettle.setTxtToChqNo(txtToChqNo.getText());
        observableSettle.setTxtQty(txtQty.getText());
        observableSettle.setTdtChqDate(tdtChqDate.getDateValue());
        observableSettle.setTxtChqAmt(txtChqAmt.getText());
        observableSettle.setTdtClearingDt(tdtClearingDt.getDateValue());
        observableSettle.setCboBounReason(CommonUtil.convertObjToStr(cboBounReason.getSelectedItem()));
        observableSettle.setTxtRemarks(txtRemarks.getText());
        observableSettle.setRdoReturnChq_No(rdoReturnChq_No.isSelected());
        observableSettle.setRdoReturnChq_Yes(rdoReturnChq_Yes.isSelected());
        observableSettle.setRdoChqBounce_Yes(rdoChqBounce_Yes.isSelected());
        observableSettle.setRdoChqBounce_No(rdoChqBounce_No.isSelected());
        observableSettle.setRdoActype_Sb(rdoActype_Sb.isSelected());
        observableSettle.setRdoActype_Ca(rdoActype_Ca.isSelected());
        observableSettle.setRdoActype_Od(rdoActype_Od.isSelected());
        observableSettle.setRdoChqBounce_Yes(rdoChqBounce_Yes.isSelected());
        observableSettle.setRdoChqBounce_No(rdoChqBounce_No.isSelected());
        observableSettle.setRdoSetMode_Ecs(rdoSetMode_Ecs.isSelected());
        observableSettle.setRdoSetMode_Othrs(rdoSetMode_Othrs.isSelected());
        observableSettle.setRdoSetMode_Pdc(rdoSetMode_Pdc.isSelected());
    }
    
    public void updateOBFieldsBank(){
        observableSettle.setCboBankName(CommonUtil.convertObjToStr(cboBankName.getSelectedItem()));
        observableSettle.setCboBranchName(CommonUtil.convertObjToStr(cboBranchName.getSelectedItem()));
        observableSettle.setTxtActNo(txtActNo.getText());
//        observableSettle.setRdoActype_Sb(rdoActype_Sb.isSelected());
//        observableSettle.setRdoActype_Ca(rdoActype_Ca.isSelected());
//        observableSettle.setRdoActype_Od(rdoActype_Od.isSelected());
//        observableSettle.setRdoChqBounce_Yes(rdoChqBounce_Yes.isSelected());
//        observableSettle.setRdoChqBounce_No(rdoChqBounce_No.isSelected());
//        observableSettle.setRdoSetMode_Ecs(rdoSetMode_Ecs.isSelected());
//        observableSettle.setRdoSetMode_Othrs(rdoSetMode_Othrs.isSelected());
//        observableSettle.setRdoSetMode_Pdc(rdoSetMode_Pdc.isSelected());
    }
    private void removeRadioButtons() {
        removeActTypRadioBtns();
        removeSetModeRadioBtns();
        removeChqBounceRadioBtns();
        removeReturnChqRadioBtns();
    }
    private void addRadioButtons(){
        addActTypRadioBtns();
        addSetModeRadioBtns();
        addChqBounceRadioBtns();
        addReturnChqRadioBtns();
    }
    
    private void removeReturnChqRadioBtns(){
        rdoReturnChq_Yes.remove(rdoReturnChq_Yes);
        rdoReturnChq_No.remove(rdoReturnChq_No);
    }
    private void removeActTypRadioBtns(){
        rdoActype_Sb.remove(rdoActype_Sb);
        rdoActype_Ca.remove(rdoActype_Ca);
        rdoActype_Od.remove(rdoActype_Od);
    }
    private void removeSetModeRadioBtns(){
        rdoSetMode_Ecs.remove(rdoSetMode_Ecs);
        rdoSetMode_Othrs.remove(rdoSetMode_Othrs);
        rdoSetMode_Pdc.remove(rdoSetMode_Pdc);
    }
    private void removeChqBounceRadioBtns(){
        rdoChqBounce_Yes.remove(rdoChqBounce_Yes);
        rdoChqBounce_No.remove(rdoChqBounce_No);
    }
    private void addReturnChqRadioBtns(){
        rdoReturnChq = new CButtonGroup();
        rdoReturnChq.add(rdoReturnChq_Yes);
        rdoReturnChq.add(rdoReturnChq_No);
    }
    private void addActTypRadioBtns(){
        rdoActTyp = new CButtonGroup();
        rdoActTyp.add(rdoActype_Sb);
        rdoActTyp.add(rdoActype_Ca);
        rdoActTyp.add(rdoActype_Od);
        
    }
    private void addChqBounceRadioBtns(){
        rdoChqBounce = new CButtonGroup();
        rdoChqBounce.add(rdoChqBounce_Yes);
        rdoChqBounce.add(rdoChqBounce_No);
    }
    private void addSetModeRadioBtns(){
        rdoSetMode = new CButtonGroup();
        rdoSetMode.add(rdoSetMode_Ecs);
        rdoSetMode.add(rdoSetMode_Othrs);
        rdoSetMode.add(rdoSetMode_Pdc);
    }
    private void initComponentData(){
        cboBankName.setModel(observableSettle.getCbmBankName());
//        cboBranchName.setModel(observableSettle.getCbmBranchName());
        cboBounReason.setModel(observableSettle.getCbmBounReason());
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
        lblFromChqNo = new com.see.truetransact.uicomponent.CLabel();
        txtFromChqNo = new com.see.truetransact.uicomponent.CTextField();
        txtChqAmt = new com.see.truetransact.uicomponent.CTextField();
        lblToChqNo = new com.see.truetransact.uicomponent.CLabel();
        txtToChqNo = new com.see.truetransact.uicomponent.CTextField();
        lblChqDate = new com.see.truetransact.uicomponent.CLabel();
        tdtChqDate = new com.see.truetransact.uicomponent.CDateField();
        lblClearingDt = new com.see.truetransact.uicomponent.CLabel();
        tdtClearingDt = new com.see.truetransact.uicomponent.CDateField();
        lblQty = new com.see.truetransact.uicomponent.CLabel();
        txtQty = new com.see.truetransact.uicomponent.CTextField();
        lblChqAmt = new com.see.truetransact.uicomponent.CLabel();
        lblRemarks = new com.see.truetransact.uicomponent.CLabel();
        txtRemarks = new com.see.truetransact.uicomponent.CTextField();
        panPowerAttorney_Part2 = new com.see.truetransact.uicomponent.CPanel();
        lblBounReason = new com.see.truetransact.uicomponent.CLabel();
        cboBounReason = new com.see.truetransact.uicomponent.CComboBox();
        panPowerAttorney_Part4 = new com.see.truetransact.uicomponent.CPanel();
        lblChqBounce = new com.see.truetransact.uicomponent.CLabel();
        rdoChqBounce_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoChqBounce_No = new com.see.truetransact.uicomponent.CRadioButton();
        panPowerAttorney_Part3 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_PoA = new com.see.truetransact.uicomponent.CButton();
        btnSave_PoA = new com.see.truetransact.uicomponent.CButton();
        btnDelete_PoA = new com.see.truetransact.uicomponent.CButton();
        panPowerAttroney_Table = new com.see.truetransact.uicomponent.CPanel();
        srpPowerAttroney = new com.see.truetransact.uicomponent.CScrollPane();
        tblPowerAttroney = new com.see.truetransact.uicomponent.CTable();
        panBorrowDetails = new com.see.truetransact.uicomponent.CPanel();
        panPowerAttorney_Part5 = new com.see.truetransact.uicomponent.CPanel();
        lblBankName = new com.see.truetransact.uicomponent.CLabel();
        lblAccType = new com.see.truetransact.uicomponent.CLabel();
        lblActNo = new com.see.truetransact.uicomponent.CLabel();
        txtActNo = new com.see.truetransact.uicomponent.CTextField();
        panPowerAttorney_Part6 = new com.see.truetransact.uicomponent.CPanel();
        rdoSetMode_Ecs = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSetMode_Pdc = new com.see.truetransact.uicomponent.CRadioButton();
        rdoSetMode_Othrs = new com.see.truetransact.uicomponent.CRadioButton();
        panPowerAttorney_Part7 = new com.see.truetransact.uicomponent.CPanel();
        rdoActype_Sb = new com.see.truetransact.uicomponent.CRadioButton();
        rdoActype_Ca = new com.see.truetransact.uicomponent.CRadioButton();
        rdoActype_Od = new com.see.truetransact.uicomponent.CRadioButton();
        lblSetMode = new com.see.truetransact.uicomponent.CLabel();
        lblBranchName = new com.see.truetransact.uicomponent.CLabel();
        cboBankName = new com.see.truetransact.uicomponent.CComboBox();
        cboBranchName = new com.see.truetransact.uicomponent.CComboBox();
        panPowerAttorney_Part8 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_PoA1 = new com.see.truetransact.uicomponent.CButton();
        btnSave_PoA1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete_PoA1 = new com.see.truetransact.uicomponent.CButton();
        btnDelete_PoA2 = new com.see.truetransact.uicomponent.CButton();
        panPowerAttorney_Part9 = new com.see.truetransact.uicomponent.CPanel();
        panPowerAttorney_Part10 = new com.see.truetransact.uicomponent.CPanel();
        lblReturnChq = new com.see.truetransact.uicomponent.CLabel();
        rdoReturnChq_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoReturnChq_No = new com.see.truetransact.uicomponent.CRadioButton();
        btnSave_PoA2 = new com.see.truetransact.uicomponent.CButton();

        setLayout(new java.awt.GridBagLayout());

        panPowerAttorney.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney.setMinimumSize(new java.awt.Dimension(870, 550));
        panPowerAttorney.setPreferredSize(new java.awt.Dimension(800, 500));
        panPowerAttorney_Part1.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part1.setBorder(new javax.swing.border.EtchedBorder());
        panPowerAttorney_Part1.setMaximumSize(new java.awt.Dimension(235, 230));
        panPowerAttorney_Part1.setMinimumSize(new java.awt.Dimension(255, 230));
        panPowerAttorney_Part1.setPreferredSize(new java.awt.Dimension(290, 190));
        lblFromChqNo.setText("From Chq No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblFromChqNo, gridBagConstraints);

        txtFromChqNo.setAllowAll(true);
        txtFromChqNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtFromChqNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromChqNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromChqNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(txtFromChqNo, gridBagConstraints);

        txtChqAmt.setAllowAll(true);
        txtChqAmt.setMaximumSize(new java.awt.Dimension(100, 21));
        txtChqAmt.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(txtChqAmt, gridBagConstraints);

        lblToChqNo.setText("To Chq No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblToChqNo, gridBagConstraints);

        txtToChqNo.setAllowAll(true);
        txtToChqNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtToChqNo.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToChqNo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToChqNoFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(txtToChqNo, gridBagConstraints);

        lblChqDate.setText("      Chq Date");
        lblChqDate.setPreferredSize(new java.awt.Dimension(76, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblChqDate, gridBagConstraints);

        tdtChqDate.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtChqDate.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtChqDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtChqDateFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(tdtChqDate, gridBagConstraints);

        lblClearingDt.setText("Clearing/Pres Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblClearingDt, gridBagConstraints);

        tdtClearingDt.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtClearingDt.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtClearingDt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtClearingDtFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(tdtClearingDt, gridBagConstraints);

        lblQty.setText("Qty");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblQty, gridBagConstraints);

        txtQty.setAllowAll(true);
        txtQty.setMaximumSize(new java.awt.Dimension(100, 21));
        txtQty.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(txtQty, gridBagConstraints);

        lblChqAmt.setText("Chq Amt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblChqAmt, gridBagConstraints);

        lblRemarks.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part1.add(lblRemarks, gridBagConstraints);

        txtRemarks.setAllowAll(true);
        txtRemarks.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panPowerAttorney_Part1.add(txtRemarks, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part1, gridBagConstraints);

        panPowerAttorney_Part2.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part2.setBorder(new javax.swing.border.EtchedBorder());
        panPowerAttorney_Part2.setMaximumSize(new java.awt.Dimension(255, 100));
        panPowerAttorney_Part2.setMinimumSize(new java.awt.Dimension(255, 100));
        panPowerAttorney_Part2.setPreferredSize(new java.awt.Dimension(290, 60));
        lblBounReason.setText("Bounce Reason");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 65, 1, 1);
        panPowerAttorney_Part2.add(lblBounReason, gridBagConstraints);

        cboBounReason.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboBounReason.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part2.add(cboBounReason, gridBagConstraints);

        panPowerAttorney_Part4.setLayout(new java.awt.GridBagLayout());

        lblChqBounce.setText("Chq Bounce");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part4.add(lblChqBounce, gridBagConstraints);

        rdoChqBounce_Yes.setText("Yes");
        rdoChqBounce_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChqBounce_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part4.add(rdoChqBounce_Yes, gridBagConstraints);

        rdoChqBounce_No.setText("No");
        rdoChqBounce_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoChqBounce_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part4.add(rdoChqBounce_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 77, 1, 1);
        panPowerAttorney_Part2.add(panPowerAttorney_Part4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part2, gridBagConstraints);

        panPowerAttorney_Part3.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part3.setBorder(new javax.swing.border.EtchedBorder());
        btnNew_PoA.setText("New");
        btnNew_PoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_PoAActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part3.add(btnNew_PoA, gridBagConstraints);

        btnSave_PoA.setText("Save");
        btnSave_PoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_PoAActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part3.add(btnSave_PoA, gridBagConstraints);

        btnDelete_PoA.setText("Delete");
        btnDelete_PoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_PoAActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part3.add(btnDelete_PoA, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part3, gridBagConstraints);

        panPowerAttroney_Table.setLayout(new java.awt.GridBagLayout());

        panPowerAttroney_Table.setMinimumSize(new java.awt.Dimension(375, 250));
        panPowerAttroney_Table.setPreferredSize(new java.awt.Dimension(375, 250));
        srpPowerAttroney.setMinimumSize(new java.awt.Dimension(360, 250));
        srpPowerAttroney.setPreferredSize(new java.awt.Dimension(360, 250));
        tblPowerAttroney.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No", "Chq No", "Chq Date", "Chq Amt", "Status"
            }
        ));
        tblPowerAttroney.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblPowerAttroneyMousePressed(evt);
            }
        });

        srpPowerAttroney.setViewportView(tblPowerAttroney);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttroney_Table.add(srpPowerAttroney, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney.add(panPowerAttroney_Table, gridBagConstraints);

        panBorrowDetails.setLayout(new java.awt.GridBagLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.insets = new java.awt.Insets(3, 3, 3, 3);
        panPowerAttorney.add(panBorrowDetails, gridBagConstraints);

        panPowerAttorney_Part5.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part5.setBorder(new javax.swing.border.EtchedBorder());
        panPowerAttorney_Part5.setMaximumSize(new java.awt.Dimension(235, 230));
        panPowerAttorney_Part5.setMinimumSize(new java.awt.Dimension(255, 230));
        panPowerAttorney_Part5.setPreferredSize(new java.awt.Dimension(290, 170));
        lblBankName.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(lblBankName, gridBagConstraints);

        lblAccType.setText("Acc Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(lblAccType, gridBagConstraints);

        lblActNo.setText("A/c No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(lblActNo, gridBagConstraints);

        txtActNo.setAllowAll(true);
        txtActNo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtActNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(txtActNo, gridBagConstraints);

        panPowerAttorney_Part6.setLayout(new java.awt.GridBagLayout());

        rdoSetMode_Ecs.setText("ECS");
        rdoSetMode_Ecs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSetMode_EcsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part6.add(rdoSetMode_Ecs, gridBagConstraints);

        rdoSetMode_Pdc.setText("PDC");
        rdoSetMode_Pdc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSetMode_PdcActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part6.add(rdoSetMode_Pdc, gridBagConstraints);

        rdoSetMode_Othrs.setText("Others");
        rdoSetMode_Othrs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoSetMode_OthrsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part6.add(rdoSetMode_Othrs, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(panPowerAttorney_Part6, gridBagConstraints);

        panPowerAttorney_Part7.setLayout(new java.awt.GridBagLayout());

        rdoActype_Sb.setText("S/B");
        rdoActype_Sb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoActype_SbActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part7.add(rdoActype_Sb, gridBagConstraints);

        rdoActype_Ca.setText("CA");
        rdoActype_Ca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoActype_CaActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part7.add(rdoActype_Ca, gridBagConstraints);

        rdoActype_Od.setText("OD/CC");
        rdoActype_Od.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoActype_OdActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part7.add(rdoActype_Od, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(panPowerAttorney_Part7, gridBagConstraints);

        lblSetMode.setText("Settlement Mode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(lblSetMode, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(cboBankName, gridBagConstraints);

        cboBranchName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboBranchName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part5.add(cboBranchName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panPowerAttorney.add(panPowerAttorney_Part5, gridBagConstraints);

        panPowerAttorney_Part8.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part8.setBorder(new javax.swing.border.EtchedBorder());
        btnNew_PoA1.setText("New");
        btnNew_PoA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNew_PoA1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part8.add(btnNew_PoA1, gridBagConstraints);

        btnSave_PoA1.setText("Save");
        btnSave_PoA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_PoA1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part8.add(btnSave_PoA1, gridBagConstraints);

        btnDelete_PoA1.setText("Close");
        btnDelete_PoA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_PoA1ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part8.add(btnDelete_PoA1, gridBagConstraints);

        btnDelete_PoA2.setText("Edit");
        btnDelete_PoA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelete_PoA2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part8.add(btnDelete_PoA2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part8, gridBagConstraints);

        panPowerAttorney_Part9.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part9.setBorder(new javax.swing.border.EtchedBorder());
        panPowerAttorney_Part9.setPreferredSize(new java.awt.Dimension(310, 39));
        panPowerAttorney_Part10.setLayout(new java.awt.GridBagLayout());

        lblReturnChq.setText("Return Un used Chques");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part10.add(lblReturnChq, gridBagConstraints);

        rdoReturnChq_Yes.setText("Yes");
        rdoReturnChq_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReturnChq_YesActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part10.add(rdoReturnChq_Yes, gridBagConstraints);

        rdoReturnChq_No.setText("No");
        rdoReturnChq_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoReturnChq_NoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part10.add(rdoReturnChq_No, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panPowerAttorney_Part9.add(panPowerAttorney_Part10, gridBagConstraints);

        btnSave_PoA2.setText("Save");
        btnSave_PoA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSave_PoA2ActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part9.add(btnSave_PoA2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part9, gridBagConstraints);

        add(panPowerAttorney, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    private void rdoReturnChq_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReturnChq_NoActionPerformed
        // TODO add your handling code here:
        btnSave_PoA2.setEnabled(false);
    }//GEN-LAST:event_rdoReturnChq_NoActionPerformed

    private void btnSave_PoA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_PoA2ActionPerformed
        // TODO add your handling code here:
//        int[] rows = tblPowerAttroney.getSelectedRows();
//        int rowcnt = tblPowerAttroney.getRowCount();
//        int j = tblPowerAttroney.getSelectedRow();
//        for (int i=0; i<rowcnt; i++) {
////            txtAmount.setText(CommonUtil.convertObjToStr(tblData.getValueAt(j,7)));
////            txtfavouring.setText(CommonUtil.convertObjToStr(tblData.getValueAt(j,6)));
//            String val = CommonUtil.convertObjToStr(tblPowerAttroney.getValueAt(tblPowerAttroney.getSelectedRow(),0));
//            HashMap dataMap = new HashMap();
//            
//        }
//        String val = CommonUtil.convertObjToStr(tblPowerAttroney.getValueAt(tblPowerAttroney.getSelectedRow(),0));
//        System.out.println("$$$$$$$$$$$$$"+observableSettle.getSelected());
//        ArrayList newData = new ArrayList();
//        newData = observableSettle.getSelected();
//        StringBuffer chqNos = new StringBuffer();
//        HashMap paramMap = new HashMap();
//        for(int i=0; i < newData.size(); i++){
//            HashMap passMap = new HashMap();
//            passMap = (HashMap) newData.get(i);
//            System.out.println("$$$$$$$$$$$passMap$$"+passMap);
//            chqNos.append("'"+CommonUtil.convertObjToStr(passMap.get("CHQNO"))+"'");
//            if((newData.size() > 1) && (newData.size()-1 != i))
//                        chqNos.append(",");
//            
//        }
//        
//        System.out.println("$$$$$$$$$$$$$"+newData);
//        paramMap.put("FROM_CHQ", chqNos);
//        paramMap.put("CHQ_STATUS", "RETURNED");
//         paramMap.put("RETURN_CHQ", "Y");
//        ClientUtil.execute("updateChqRetStatus", paramMap);
//        paramMap = null;
//        newData = null;
//        chqNos = null;
        observableSettle.executeRetChqSave();
    }//GEN-LAST:event_btnSave_PoA2ActionPerformed

    private void btnDelete_PoA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_PoA2ActionPerformed
        // TODO add your handling code here:
//        populateData();
        observableSettle.setIsEditMode(true);
//        observableSettle.set_tblData(tblPowerAttroney);
        observableSettle.doAction(3 );
//        observableSettle.populateTable(tblPowerAttroney);
        
    }//GEN-LAST:event_btnDelete_PoA2ActionPerformed

    private void rdoReturnChq_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoReturnChq_YesActionPerformed
        // TODO add your handling code here:
//         populateData();
        observableSettle.setIsRetChq(true);
        observableSettle.doAction(3 );
        btnSave_PoA2.setEnabled(true);
    }//GEN-LAST:event_rdoReturnChq_YesActionPerformed

    private void btnSave_PoA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_PoA1ActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        updateOBFieldsBank();
        if(observableSettle.isIsEditMode())
            observableSettle.doAction(2);
        else
            observableSettle.doAction(1);
        observableSettle.resetPoAForm();
        observableSettle.resetSetBnkForm();
        observableSettle.resetAllFieldsInPoA();
        tblPowerAttroney.setModel(observableSettle.getTblPoA());
        observableSettle.setIsEditMode(true);
//        observableSettle.ttNotifyObservers();
        
    }//GEN-LAST:event_btnSave_PoA1ActionPerformed
    public void populateData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "getSettlementDetails");
        whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
//        whereMap.put("PROD_ID", ((ComboBoxModel)cboProductId.getModel()).getKeyForSelected());
//                whereMap.put("FROM_DT", DateUtil.getDateMMDDYYYY(tdtFromDate.getDateValue()));
//                whereMap.put("TO_DT", DateUtil.getDateMMDDYYYY(tdtToDate.getDateValue()));
//               if(flag == false)
//                {
//                  whereMap.put("AUTHORIZECHECK","");   
//                }
////        System.out.println("#### where map : "+whereMap);
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            log.info("populateData...");
            ArrayList heading = observableSettle.populateData(viewMap, tblPowerAttroney);
        } catch( Exception e ) {
            System.err.println( "Exception " + e.toString() + "Caught" );
            e.printStackTrace();
        }
//        flag = false;
    }
    private void txtFromChqNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromChqNoFocusLost
        // TODO add your handling code here:
        if(rdoSetMode_Pdc.isSelected()){
         if(!(CommonUtil.convertObjToStr(txtFromChqNo.getText()).equals("")) &&
            !(CommonUtil.convertObjToStr(txtToChqNo.getText()).equals(""))){
            int frmChq = CommonUtil.convertObjToInt(txtFromChqNo.getText());
            int toChq = CommonUtil.convertObjToInt(txtToChqNo.getText());
            int qty = (-(frmChq - toChq))+1;
            txtQty.setText(String.valueOf(qty));
         }
        }
    }//GEN-LAST:event_txtFromChqNoFocusLost

    private void txtToChqNoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToChqNoFocusLost
        // TODO add your handling code here:
        if(rdoSetMode_Pdc.isSelected()){
        if(!(CommonUtil.convertObjToStr(txtFromChqNo.getText()).equals("")) &&
            !(CommonUtil.convertObjToStr(txtToChqNo.getText()).equals(""))){
            int frmChq = CommonUtil.convertObjToInt(txtFromChqNo.getText());
            int toChq = CommonUtil.convertObjToInt(txtToChqNo.getText());
            int qty = (-(frmChq - toChq))+1;
            txtQty.setText(String.valueOf(qty));
        }
        }
    }//GEN-LAST:event_txtToChqNoFocusLost

    private void rdoChqBounce_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChqBounce_NoActionPerformed
        // TODO add your handling code here:
//        rdoChqBounce_Yes.setSelected(false);
        cboBounReason.setEnabled(false);
    }//GEN-LAST:event_rdoChqBounce_NoActionPerformed

    private void rdoChqBounce_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoChqBounce_YesActionPerformed
        // TODO add your handling code here:
//        rdoChqBounce_No.setSelected(false);
        cboBounReason.setEnabled(true);
        
    }//GEN-LAST:event_rdoChqBounce_YesActionPerformed

    private void rdoSetMode_OthrsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSetMode_OthrsActionPerformed
        // TODO add your handling code here:
//          rdoSetMode_Ecs.setSelected(false);
//          rdoSetMode_Pdc.setSelected(false);
          ClientUtil.enableDisable(panPowerAttorney_Part1,false);
          btnNew_PoA.setEnabled(false);
    }//GEN-LAST:event_rdoSetMode_OthrsActionPerformed

    private void rdoSetMode_PdcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSetMode_PdcActionPerformed
        // TODO add your handling code here:
//        rdoSetMode_Othrs.setSelected(false);
//        rdoSetMode_Ecs.setSelected(false);
//        ClientUtil.enableDisable(panPowerAttorney_Part1,true);
//        ClientUtil.enableDisable(panPowerAttorney_Part4,true);
        btnNew_PoA.setEnabled(true);
        internationalize();
        setEnableEcs(true);
    }//GEN-LAST:event_rdoSetMode_PdcActionPerformed

    private void rdoSetMode_EcsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoSetMode_EcsActionPerformed
        // TODO add your handling code here:
//        rdoSetMode_Pdc.setSelected(false);
//        rdoSetMode_Othrs.setSelected(false);
        ClientUtil.enableDisable(panPowerAttorney_Part1,false);
        btnNew_PoA.setEnabled(true);
        setLableEcs();
        setEnableEcs(false);
    }//GEN-LAST:event_rdoSetMode_EcsActionPerformed

    private void rdoActype_OdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoActype_OdActionPerformed
        // TODO add your handling code here:
//         rdoActype_Sb.setSelected(false);
//         rdoActype_Ca.setSelected(false);
    }//GEN-LAST:event_rdoActype_OdActionPerformed

    private void rdoActype_CaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoActype_CaActionPerformed
        // TODO add your handling code here:
//        rdoActype_Sb.setSelected(false);
//        rdoActype_Od.setSelected(false);
    }//GEN-LAST:event_rdoActype_CaActionPerformed

    private void rdoActype_SbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoActype_SbActionPerformed
        // TODO add your handling code here:
//        rdoActype_Ca.setSelected(false);
//        rdoActype_Od.setSelected(false);
    }//GEN-LAST:event_rdoActype_SbActionPerformed

    private void cboBankNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboBankNameItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cboBankNameItemStateChanged

    private void cboBankNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboBankNameActionPerformed
        // TODO add your handling code here:
        observableSettle.getBranchData("");
        cboBranchName.setModel(observableSettle.getCbmBranchName());
    }//GEN-LAST:event_cboBankNameActionPerformed

    private void btnNew_PoA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_PoA1ActionPerformed
        // TODO add your handling code here:
          if(observableSettle.getStrAccNumber().length()>0)
          {
        ClientUtil.enableDisable(panPowerAttorney_Part5,true);
        observableSettle.setIsEditMode(false);
          }else{
              ClientUtil.displayAlert("Choose Account Number From Sanction Details");
              return;
          }
//        ClientUtil.enableDisable(panPowerAttorney_Part1,true);
    }//GEN-LAST:event_btnNew_PoA1ActionPerformed

    private void btnDelete_PoA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_PoA1ActionPerformed
        // TODO add your handling code here:
//        cifClosingAlert();
//        observableSettle.setIsEditMode(false);
    }//GEN-LAST:event_btnDelete_PoA1ActionPerformed
    public void setLableEcs(){
        lblFromChqNo.setText("ECS No");
        lblQty.setText("No of Installments");
        lblChqDate.setText("ECS From Dt");
        lblChqAmt.setText("ECS Amt");
    }
    
    public void setEnableEcs(boolean val){
        lblToChqNo.setVisible(val);
        txtToChqNo.setVisible(val); 
    }
    private void btnCustID_SelectActionPerformed() {
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
            if(viewType.equals("CUSTOMER ID")){
                observableSettle.setTxtCustID_PoA(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                //txtCustID_PoA.setText(observablePoA.getTxtCustID_PoA());
                final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
                observableSettle.setPoACustAddr(CustID);
                observableSettle.setPoACustName(CustID);
                observableSettle.setPoACustPhone(CustID);
                observableSettle.ttNotifyObservers();
                setAllPoAEnableDisable(true);
                setPoANewSaveOnlyEnable();
//                txtCustID_PoA.setEditable(true);
            }
        }
    }
    private void tdtClearingDtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtClearingDtFocusLost
        // TODO add your handling code here:
        tdtPeriodTo_PowerAttroneyFocusLost();
    }//GEN-LAST:event_tdtClearingDtFocusLost
    private void tdtPeriodTo_PowerAttroneyFocusLost(){
        // To check whether this To date is greater than this details From date
//        ClientUtil.validateToDate(tdtPeriodTo_PowerAttroney, tdtPeriodFrom_PowerAttroney.getDateValue());
    }
    private void tdtChqDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtChqDateFocusLost
        // TODO add your handling code here:
        tdtPeriodFrom_PowerAttroneyFocusLost();
    }//GEN-LAST:event_tdtChqDateFocusLost
    
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
    
    public void setViewType(String viewType){
        this.viewType = viewType;
    }
    private void tblPowerAttroneyMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPowerAttroneyMousePressed
        // TODO add your handling code here:
        powerAttroneyTableMousePressed();
//        cboBounReason.setEnabled(false);
//        tdtClearingDt.setEnabled(false);
//        txtFromChqNo.setEnabled(false);
//        txtToChqNo.setEnabled(false);
//        txtQty.setEnabled(false);
//        tdtClearingDt.setEnabled(true);
//        txtRemarks.setEnabled(false);
        if(observableSettle.isIsEditMode())
            ClientUtil.enableDisable(panPowerAttorney_Part2, true);
        
    }//GEN-LAST:event_tblPowerAttroneyMousePressed
    private void powerAttroneyTableMousePressed(){
        // Actions have to be taken when a record of PoA details is selected
        if (tblPowerAttroney.getSelectedRow() >= 0){
//            if(!observableSettle.isIsEditMode()){
            observableSettle.setIsTableClicked(true);
            updateOBFields();
            observableSettle.populatePoATable(tblPowerAttroney.getSelectedRow());
//            observableSettle.populateTable(tblPowerAttroney);
            String val = CommonUtil.convertObjToStr(tblPowerAttroney.getValueAt(tblPowerAttroney.getSelectedRow(),0));
            boolean value = true;
            if(val.equalsIgnoreCase("false")){
                value = true;
            }else{
                value = false;
            }
//            if(observableSettle.isIsRetChq()){
//                tblPowerAttroney.setValueAt(new Boolean(value),tblPowerAttroney.getSelectedRow(),0);
//            }
            if(rdoReturnChq_Yes.isSelected()){
                observableSettle.setSelectAll(new Boolean(value),tblPowerAttroney.getSelectedRow());
            }
            if ((getLblStatus().equals(ClientConstants.ACTION_STATUS[3]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[7]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[8]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[10])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) || (viewType.equals(ClientConstants.ACTION_STATUS[3]))){
                setAllPoAEnableDisable(false);
                setPoAToolBtnsEnableDisable(false);
            }else{
                updateModePoA = true;
                modePoA = tblPowerAttroney.getSelectedRow();
                setPoAToolBtnsEnableDisable(true);
                setAllPoAEnableDisable(true);
            }
        
//        }else{
//            observableSettle.populatePoATable(tblPowerAttroney.getSelectedRow());
//             updateModePoA = true;
////                modePoA = tblPowerAttroney.getSelectedRow();
//                setPoAToolBtnsEnableDisable(true);
//                setAllPoAEnableDisable(true);
//        }
    }else{
            setAllPoAEnableDisable(false);
            setPoAToolBtnsEnableDisable(false);
        }
    }
    private void btnDelete_PoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelete_PoAActionPerformed
        // TODO add your handling code here:
        btnDeletePoAActionPerformed();
    }//GEN-LAST:event_btnDelete_PoAActionPerformed
    private void btnDeletePoAActionPerformed(){
        observableSettle.deletePoATable(modePoA);
        setPoAToolBtnsEnableDisable(false);
        setPoANewOnlyEnable();
        observableSettle.resetPoAForm();
        observableSettle.ttNotifyObservers();
        modePoA = -1;
        updateModePoA = false;
    }
    private void btnSave_PoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_PoAActionPerformed
        // TODO add your handling code here:
        savePoAactionPerformed();
    }//GEN-LAST:event_btnSave_PoAActionPerformed
    private void savePoAactionPerformed(){
//        String strOnBehalfOf = CommonUtil.convertObjToStr(((ComboBoxModel)cboPoACust.getModel()).getKeyForSelected());
//        if(!updateModePoA)
//        if (!observablePoA.isThisCustomerOnBehalfofOther(txtCustID_PoA.getText(), strOnBehalfOf)){
//            return;
//        }
//        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panPowerAttorney_Part1);
//        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        
//        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panPowerAttorney_Part2);
//        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
//        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0){
//            displayAlert(mandatoryMessage1+mandatoryMessage2);
//        }else{
//        if(!observableSettle.isIsEditMode()){
            setAllPoAEnableDisable(false);
            setPoAToolBtnsEnableDisable(false);
            btnNew_PoA.setEnabled(true);
            updateOBFields();
            updateOBFieldsBank();
            if (observableSettle.addPoATab(modePoA,updateModePoA) == 1){
                setAllPoAEnableDisable(true);
                btnSave_PoA.setEnabled(true);
            }else{
                observableSettle.resetPoAForm();
                btnSave_PoA.setEnabled(false);
            }
            updateModePoA=false;
            observableSettle.ttNotifyObservers();
//        }
//        }else{
//            
//        }
    }
    private void btnNew_PoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_PoAActionPerformed
        // TODO add your handling code here:
        newPoAactionPerformed();
        resetSetDetails();
        observableSettle.setIsEditMode(false);
        ClientUtil.enableDisable(panPowerAttorney_Part1,true);
        ClientUtil.enableDisable(panPowerAttorney_Part4,false);
        tdtClearingDt.setEnabled(false);
//        panPowerAttorney_Part4
        cboBounReason.setEnabled(false);
        ClientUtil.enableDisable(panPowerAttorney_Part9,true);
        btnSave_PoA2.setEnabled(false);
    }//GEN-LAST:event_btnNew_PoAActionPerformed
    private void newPoAactionPerformed(){
//        updateOBFields();
        setAllPoAEnableDisable(true);
//        txtCustID_PoA.setEditable(true);
//        observableSettle.resetPoAForm();
//        observableSettle.ttNotifyObservers();
        modePoA = -1;
        updateModePoA = false;
        setPoANewSaveOnlyEnable();
    }
    
    public void setCboPoACustModel(){
//        cboPoACust.setModel(observablePoA.getCbmPoACust());
    }
//    public boolean checkCustIDExistInJointAcctAndPoA(String CustID){
////        return observablePoA.checkCustIDExistInJointAcctAndPoA(CustID);
//    }
    
    public void resetPoAForm(){
        observableSettle.resetPoAForm();
        observableSettle.ttNotifyObservers();
    }
    
    public void resetAllFieldsPoA(){
        observableSettle.resetAllFieldsInPoA();
        observableSettle.ttNotifyObservers();
    }
    
    public void resetPoACustID(String Cust_ID){
        observableSettle.resetPoACustID(Cust_ID);
        observableSettle.ttNotifyObservers();
    }
    
    public void clearCboPoACust_ID(){
        observableSettle.clearCboPoACust_ID();
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
        txtFromChqNo.setEnabled(val);
        txtToChqNo.setEnabled(val);
        txtQty.setEnabled(val);
        tdtChqDate.setEnabled(val);
        txtChqAmt.setEnabled(val);
        tdtClearingDt.setEnabled(val);
        cboBounReason.setEnabled(val);
        txtRemarks.setEnabled(val);
        observableSettle.ttNotifyObservers();
    }
    public void resetSetDetails(){
            txtFromChqNo.setText("");
            txtToChqNo.setText("");
            txtQty.setText("");
            tdtChqDate.setDateValue("");
            txtChqAmt.setText("");
            tdtClearingDt.setDateValue("");
            cboBounReason.setSelectedItem("");
            txtRemarks.setText("");
    }
    //To enable and disable the buttons in PoA
    public void setPoAToolBtnsEnableDisable(boolean val){
        btnNew_PoA.setEnabled(val);
        btnSave_PoA.setEnabled(val);
        btnDelete_PoA.setEnabled(val);
    }
    
    public void setPoANewOnlyEnable(){
        btnNew_PoA.setEnabled(true);
        btnSave_PoA.setEnabled(false);
        btnDelete_PoA.setEnabled(false);
    }
    
    public void setPoANewSaveOnlyEnable(){
        btnNew_PoA.setEnabled(true);
        btnSave_PoA.setEnabled(true);
        btnDelete_PoA.setEnabled(false);
    }
    
    public void ttNotifyObservers(){
        observableSettle.ttNotifyObservers();
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    
    /** returns the PoA CTable Row count
     */
    public int getTblRowCount(){
        return tblPowerAttroney.getRowCount();
    }
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame frm = new javax.swing.JFrame();
        SettlementUI sett = new SettlementUI("TL");
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
    private com.see.truetransact.uicomponent.CButton btnDelete_PoA;
    private com.see.truetransact.uicomponent.CButton btnDelete_PoA1;
    private com.see.truetransact.uicomponent.CButton btnDelete_PoA2;
    private com.see.truetransact.uicomponent.CButton btnNew_PoA;
    private com.see.truetransact.uicomponent.CButton btnNew_PoA1;
    private com.see.truetransact.uicomponent.CButton btnSave_PoA;
    private com.see.truetransact.uicomponent.CButton btnSave_PoA1;
    private com.see.truetransact.uicomponent.CButton btnSave_PoA2;
    private com.see.truetransact.uicomponent.CComboBox cboBankName;
    private com.see.truetransact.uicomponent.CComboBox cboBounReason;
    private com.see.truetransact.uicomponent.CComboBox cboBranchName;
    private com.see.truetransact.uicomponent.CLabel lblAccType;
    private com.see.truetransact.uicomponent.CLabel lblActNo;
    private com.see.truetransact.uicomponent.CLabel lblBankName;
    private com.see.truetransact.uicomponent.CLabel lblBounReason;
    private com.see.truetransact.uicomponent.CLabel lblBranchName;
    private com.see.truetransact.uicomponent.CLabel lblChqAmt;
    private com.see.truetransact.uicomponent.CLabel lblChqBounce;
    private com.see.truetransact.uicomponent.CLabel lblChqDate;
    private com.see.truetransact.uicomponent.CLabel lblClearingDt;
    private com.see.truetransact.uicomponent.CLabel lblFromChqNo;
    private com.see.truetransact.uicomponent.CLabel lblQty;
    private com.see.truetransact.uicomponent.CLabel lblRemarks;
    private com.see.truetransact.uicomponent.CLabel lblReturnChq;
    private com.see.truetransact.uicomponent.CLabel lblSetMode;
    private com.see.truetransact.uicomponent.CLabel lblToChqNo;
    private com.see.truetransact.uicomponent.CPanel panBorrowDetails;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part1;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part10;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part2;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part3;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part4;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part5;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part6;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part7;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part8;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part9;
    private com.see.truetransact.uicomponent.CPanel panPowerAttroney_Table;
    private com.see.truetransact.uicomponent.CButtonGroup rdoActTyp;
    private com.see.truetransact.uicomponent.CRadioButton rdoActype_Ca;
    private com.see.truetransact.uicomponent.CRadioButton rdoActype_Od;
    private com.see.truetransact.uicomponent.CRadioButton rdoActype_Sb;
    private com.see.truetransact.uicomponent.CButtonGroup rdoChqBounce;
    private com.see.truetransact.uicomponent.CRadioButton rdoChqBounce_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoChqBounce_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoReturnChq;
    private com.see.truetransact.uicomponent.CRadioButton rdoReturnChq_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoReturnChq_Yes;
    private com.see.truetransact.uicomponent.CButtonGroup rdoSetMode;
    private com.see.truetransact.uicomponent.CRadioButton rdoSetMode_Ecs;
    private com.see.truetransact.uicomponent.CRadioButton rdoSetMode_Othrs;
    private com.see.truetransact.uicomponent.CRadioButton rdoSetMode_Pdc;
    private com.see.truetransact.uicomponent.CScrollPane srpPowerAttroney;
    private com.see.truetransact.uicomponent.CTable tblPowerAttroney;
    private com.see.truetransact.uicomponent.CDateField tdtChqDate;
    private com.see.truetransact.uicomponent.CDateField tdtClearingDt;
    private com.see.truetransact.uicomponent.CTextField txtActNo;
    private com.see.truetransact.uicomponent.CTextField txtChqAmt;
    private com.see.truetransact.uicomponent.CTextField txtFromChqNo;
    private com.see.truetransact.uicomponent.CTextField txtQty;
    private com.see.truetransact.uicomponent.CTextField txtRemarks;
    private com.see.truetransact.uicomponent.CTextField txtToChqNo;
    // End of variables declaration//GEN-END:variables
    
}

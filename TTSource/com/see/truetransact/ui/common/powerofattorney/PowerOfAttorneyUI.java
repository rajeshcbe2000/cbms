/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * PowerOfAttorneyUI.java
 *
 * Created on December 23, 2004, 4:18 PM
 */

package com.see.truetransact.ui.common.powerofattorney;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.ui.common.viewall.ViewAll;

import java.util.HashMap;

import org.apache.log4j.Logger;
/**
 *
 * @author  152713
 */
public class PowerOfAttorneyUI extends CPanel implements java.util.Observer,UIMandatoryField{
    private PowerOfAttorneyOB observablePoA;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.powerofattorney.PowerOfAttorneyRB", ProxyParameters.LANGUAGE);
//    PowerOfAttorneyRB resourceBundle = new PowerOfAttorneyRB();
    
    String lblStatus = "";
    String viewType = "";
    String TITLE = "Power of Attorney";
    private final static Logger log = Logger.getLogger(PowerOfAttorneyUI.class);
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String REJECT = "REJECT";
    private final        String EXCEPTION = "EXCEPTION";
    
    private boolean updateModePoA = false;
    
    int modePoA       = -1;
    
    private HashMap mandatoryMap;
    
    /** Creates new form PowerOfAttorneyUI */
    public PowerOfAttorneyUI(String strModule) {
        initComponents();
        powerOfAttorneyUI(strModule);
    }
    
    private void powerOfAttorneyUI(String strModule){
        setFieldNames();
        internationalize();
        setMaxLength();
        setObservable(strModule);
        ClientUtil.enableDisable(this, false);
        allEnableDisable();
        initComponentData();
        setMandatoryHashMap();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPowerAttorney_Part1);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panPowerAttorney_Part2);
        //        setHelpMessage();
    }
    
    public PowerOfAttorneyOB getPowerOfAttorneyOB(){
         return this.observablePoA;
    }
    
    private void allEnableDisable(){
        setAllPoAEnableDisable(false);
        setPoANewOnlyEnable();
    }
    
    private void setObservable(String strModule){
        observablePoA = new PowerOfAttorneyOB(strModule);
        observablePoA.addObserver(this);
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
        final PowerOfAttorneyMRB objMandatoryRB = new PowerOfAttorneyMRB();
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
        lblCustID_PoA.setName("lblCustID_PoA");
        txtCustID_PoA.setName("txtCustID_PoA");
        btnCustID_Select.setName("btnCustID_Select");
        cboAddrType_PoA.setName("cboAddrType_PoA");
        txtPoANo.setName("txtPoANo");
        btnDelete_PoA.setName("btnDelete_PoA");
        btnNew_PoA.setName("btnNew_PoA");
        btnSave_PoA.setName("btnSave_PoA");
        cboPoACust.setName("cboPoACust");
        lblPoACust.setName("lblPoACust");
        lblPoaHolderName.setName("lblPoaHolderName");
        txtPoaHolderName.setName("txtPoaHolderName");
        cboCity_PowerAttroney.setName("cboCity_PowerAttroney");
        cboCountry_PowerAttroney.setName("cboCountry_PowerAttroney");
        cboState_PowerAttroney.setName("cboState_PowerAttroney");
        lblArea_PowerAttroney.setName("lblArea_PowerAttroney");
        lblCity_PowerAttroney.setName("lblCity_PowerAttroney");
        lblCountry_PowerAttroney.setName("lblCountry_PowerAttroney");
        lblPeriodFrom_PowerAttroney.setName("lblPeriodFrom_PowerAttroney");
        lblPeriodTo_PowerAttroney.setName("lblPeriodTo_PowerAttroney");
        lblPhone_PowerAttroney.setName("lblPhone_PowerAttroney");
        lblPin_PowerAttroney.setName("lblPin_PowerAttroney");
        lblRemark_PowerAttroney.setName("lblRemark_PowerAttroney");
        lblState_PowerAttroney.setName("lblState_PowerAttroney");
        lblStreet_PowerAttroney.setName("lblStreet_PowerAttroney");
        panPowerAttorney.setName("panPowerAttorney");
        panPowerAttorney_Part1.setName("panPowerAttorney_Part1");
        panPowerAttorney_Part2.setName("panPowerAttorney_Part2");
        panPowerAttorney_Part3.setName("panPowerAttorney_Part3");
        panPowerAttroney_Table.setName("panPowerAttroney_Table");
        sptPowerAttroney.setName("sptPowerAttroney");
        srpPowerAttroney.setName("srpPowerAttroney");
        tblPowerAttroney.setName("tblPowerAttroney");
        tdtPeriodFrom_PowerAttroney.setName("tdtPeriodFrom_PowerAttroney");
        tdtPeriodTo_PowerAttroney.setName("tdtPeriodTo_PowerAttroney");
        txtArea_PowerAttroney.setName("txtArea_PowerAttroney");
        txtPhone_PowerAttroney.setName("txtPhone_PowerAttroney");
        txtPin_PowerAttroney.setName("txtPin_PowerAttroney");
        txtRemark_PowerAttroney.setName("txtRemark_PowerAttroney");
        txtStreet_PowerAttroney.setName("txtStreet_PowerAttroney");
    }
    
    private void internationalize() {
        lblCustID_PoA.setText(resourceBundle.getString("lblCustID_PoA"));
        lblPoANo.setText(resourceBundle.getString("lblPoANo"));
        lblAddrType_PoA.setText(resourceBundle.getString("lblAddrType_PoA"));
        lblPoaHolderName.setText(resourceBundle.getString("lblPoaHolderName"));
        btnNew_PoA.setText(resourceBundle.getString("btnNew_PoA"));
        lblPoACust.setText(resourceBundle.getString("lblPoACust"));
        btnDelete_PoA.setText(resourceBundle.getString("btnDelete_PoA"));
        btnSave_PoA.setText(resourceBundle.getString("btnSave_PoA"));
        lblPin_PowerAttroney.setText(resourceBundle.getString("lblPin_PowerAttroney"));
        lblRemark_PowerAttroney.setText(resourceBundle.getString("lblRemark_PowerAttroney"));
        lblState_PowerAttroney.setText(resourceBundle.getString("lblState_PowerAttroney"));
        lblPeriodFrom_PowerAttroney.setText(resourceBundle.getString("lblPeriodFrom_PowerAttroney"));
        lblCity_PowerAttroney.setText(resourceBundle.getString("lblCity_PowerAttroney"));
        lblArea_PowerAttroney.setText(resourceBundle.getString("lblArea_PowerAttroney"));
        lblPeriodTo_PowerAttroney.setText(resourceBundle.getString("lblPeriodTo_PowerAttroney"));
        lblCountry_PowerAttroney.setText(resourceBundle.getString("lblCountry_PowerAttroney"));
        lblPhone_PowerAttroney.setText(resourceBundle.getString("lblPhone_PowerAttroney"));
        lblStreet_PowerAttroney.setText(resourceBundle.getString("lblStreet_PowerAttroney"));
    }
    
    private void setMaxLength(){
        txtStreet_PowerAttroney.setMaxLength(256);
        txtArea_PowerAttroney.setMaxLength(128);
        txtPin_PowerAttroney.setMaxLength(16);
        txtPhone_PowerAttroney.setMaxLength(32);
        txtPhone_PowerAttroney.setAllowNumber(true);
        txtPin_PowerAttroney.setValidation(new PincodeValidation_IN());
        txtPoaHolderName.setMaxLength(32);
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtPoaHolderName.setText(observablePoA.getTxtPoaHolderName());
        txtCustID_PoA.setText(observablePoA.getTxtCustID_PoA());
        cboAddrType_PoA.setSelectedItem(observablePoA.getCboAddrType_PoA());
        txtPoANo.setText(observablePoA.getTxtPoANo());
        txtStreet_PowerAttroney.setText(observablePoA.getTxtStreet_PowerAttroney());
        txtArea_PowerAttroney.setText(observablePoA.getTxtArea_PowerAttroney());
        cboCity_PowerAttroney.setSelectedItem(observablePoA.getCboCity_PowerAttroney());
        cboState_PowerAttroney.setSelectedItem(observablePoA.getCboState_PowerAttroney());
        cboCountry_PowerAttroney.setSelectedItem(observablePoA.getCboCountry_PowerAttroney());
        txtPin_PowerAttroney.setText(observablePoA.getTxtPin_PowerAttroney());
        txtPhone_PowerAttroney.setText(observablePoA.getTxtPhone_PowerAttroney());
        tdtPeriodFrom_PowerAttroney.setDateValue(observablePoA.getTdtPeriodFrom_PowerAttroney());
        tdtPeriodTo_PowerAttroney.setDateValue(observablePoA.getTdtPeriodTo_PowerAttroney());
        txtRemark_PowerAttroney.setText(observablePoA.getTxtRemark_PowerAttroney());
        tblPowerAttroney.setModel(observablePoA.getTblPoA());
        cboPoACust.setSelectedItem(observablePoA.getCboPoACust());
    }
    
    public void updateOBFields() {
        observablePoA.setTxtPoANo(txtPoANo.getText());
        observablePoA.setTxtCustID_PoA(txtCustID_PoA.getText());
        observablePoA.setTxtPoaHolderName(txtPoaHolderName.getText());
        observablePoA.setCboAddrType_PoA(CommonUtil.convertObjToStr(cboAddrType_PoA.getSelectedItem()));
        observablePoA.setCboPoACust(CommonUtil.convertObjToStr(cboPoACust.getSelectedItem()));
        observablePoA.setTxtStreet_PowerAttroney(txtStreet_PowerAttroney.getText());
        observablePoA.setTxtArea_PowerAttroney(txtArea_PowerAttroney.getText());
        observablePoA.setCboCity_PowerAttroney(CommonUtil.convertObjToStr(cboCity_PowerAttroney.getSelectedItem()));
        observablePoA.setCboState_PowerAttroney(CommonUtil.convertObjToStr(cboState_PowerAttroney.getSelectedItem()));
        observablePoA.setCboCountry_PowerAttroney(CommonUtil.convertObjToStr(cboCountry_PowerAttroney.getSelectedItem()));
        observablePoA.setTxtPin_PowerAttroney(txtPin_PowerAttroney.getText());
        observablePoA.setTxtPhone_PowerAttroney(txtPhone_PowerAttroney.getText());
        observablePoA.setTdtPeriodFrom_PowerAttroney(tdtPeriodFrom_PowerAttroney.getDateValue());
        observablePoA.setTdtPeriodTo_PowerAttroney(tdtPeriodTo_PowerAttroney.getDateValue());
        observablePoA.setTxtRemark_PowerAttroney(txtRemark_PowerAttroney.getText());
    }
    
    private void initComponentData(){
        cboAddrType_PoA.setModel(observablePoA.getCbmAddrType_PoA());
        cboCity_PowerAttroney.setModel(observablePoA.getCbmCity_PowerAttroney());
        cboState_PowerAttroney.setModel(observablePoA.getCbmState_PowerAttroney());
        cboCountry_PowerAttroney.setModel(observablePoA.getCbmCountry_PowerAttroney());
        cboPoACust.setModel(observablePoA.getCbmPoACust());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panPowerAttorney = new com.see.truetransact.uicomponent.CPanel();
        panPowerAttorney_Part1 = new com.see.truetransact.uicomponent.CPanel();
        lblPoaHolderName = new com.see.truetransact.uicomponent.CLabel();
        txtPoaHolderName = new com.see.truetransact.uicomponent.CTextField();
        lblStreet_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        txtStreet_PowerAttroney = new com.see.truetransact.uicomponent.CTextField();
        lblArea_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        txtArea_PowerAttroney = new com.see.truetransact.uicomponent.CTextField();
        lblCity_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        cboCity_PowerAttroney = new com.see.truetransact.uicomponent.CComboBox();
        lblPoACust = new com.see.truetransact.uicomponent.CLabel();
        cboPoACust = new com.see.truetransact.uicomponent.CComboBox();
        lblPoANo = new com.see.truetransact.uicomponent.CLabel();
        txtPoANo = new com.see.truetransact.uicomponent.CTextField();
        lblAddrType_PoA = new com.see.truetransact.uicomponent.CLabel();
        cboAddrType_PoA = new com.see.truetransact.uicomponent.CComboBox();
        lblCustID_PoA = new com.see.truetransact.uicomponent.CLabel();
        txtCustID_PoA = new com.see.truetransact.uicomponent.CTextField();
        btnCustID_Select = new com.see.truetransact.uicomponent.CButton();
        sptPowerAttroney = new com.see.truetransact.uicomponent.CSeparator();
        panPowerAttorney_Part2 = new com.see.truetransact.uicomponent.CPanel();
        lblCountry_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        cboCountry_PowerAttroney = new com.see.truetransact.uicomponent.CComboBox();
        lblPin_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        txtPin_PowerAttroney = new com.see.truetransact.uicomponent.CTextField();
        lblPhone_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        txtPhone_PowerAttroney = new com.see.truetransact.uicomponent.CTextField();
        tdtPeriodFrom_PowerAttroney = new com.see.truetransact.uicomponent.CDateField();
        lblPeriodTo_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        tdtPeriodTo_PowerAttroney = new com.see.truetransact.uicomponent.CDateField();
        lblRemark_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        txtRemark_PowerAttroney = new com.see.truetransact.uicomponent.CTextField();
        lblState_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        cboState_PowerAttroney = new com.see.truetransact.uicomponent.CComboBox();
        lblPeriodFrom_PowerAttroney = new com.see.truetransact.uicomponent.CLabel();
        panPowerAttorney_Part3 = new com.see.truetransact.uicomponent.CPanel();
        btnNew_PoA = new com.see.truetransact.uicomponent.CButton();
        btnSave_PoA = new com.see.truetransact.uicomponent.CButton();
        btnDelete_PoA = new com.see.truetransact.uicomponent.CButton();
        panPowerAttroney_Table = new com.see.truetransact.uicomponent.CPanel();
        srpPowerAttroney = new com.see.truetransact.uicomponent.CScrollPane();
        tblPowerAttroney = new com.see.truetransact.uicomponent.CTable();
        panBorrowDetails = new com.see.truetransact.uicomponent.CPanel();

        setLayout(new java.awt.GridBagLayout());

        panPowerAttorney.setMinimumSize(new java.awt.Dimension(890, 550));
        panPowerAttorney.setPreferredSize(new java.awt.Dimension(900, 550));
        panPowerAttorney.setLayout(new java.awt.GridBagLayout());

        panPowerAttorney_Part1.setMaximumSize(new java.awt.Dimension(300, 230));
        panPowerAttorney_Part1.setMinimumSize(new java.awt.Dimension(265, 230));
        panPowerAttorney_Part1.setPreferredSize(new java.awt.Dimension(265, 230));
        panPowerAttorney_Part1.setLayout(new java.awt.GridBagLayout());

        lblPoaHolderName.setText("PoA Holder's Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblPoaHolderName, gridBagConstraints);

        txtPoaHolderName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(txtPoaHolderName, gridBagConstraints);

        lblStreet_PowerAttroney.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblStreet_PowerAttroney, gridBagConstraints);

        txtStreet_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(txtStreet_PowerAttroney, gridBagConstraints);

        lblArea_PowerAttroney.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblArea_PowerAttroney, gridBagConstraints);

        txtArea_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(txtArea_PowerAttroney, gridBagConstraints);

        lblCity_PowerAttroney.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblCity_PowerAttroney, gridBagConstraints);

        cboCity_PowerAttroney.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCity_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(cboCity_PowerAttroney, gridBagConstraints);

        lblPoACust.setText("Customer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblPoACust, gridBagConstraints);

        cboPoACust.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboPoACust.setMinimumSize(new java.awt.Dimension(100, 21));
        cboPoACust.setPopupWidth(200);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(cboPoACust, gridBagConstraints);

        lblPoANo.setText("PoA Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblPoANo, gridBagConstraints);

        txtPoANo.setMaximumSize(new java.awt.Dimension(100, 21));
        txtPoANo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(txtPoANo, gridBagConstraints);

        lblAddrType_PoA.setText("Address Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblAddrType_PoA, gridBagConstraints);

        cboAddrType_PoA.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboAddrType_PoA.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(cboAddrType_PoA, gridBagConstraints);

        lblCustID_PoA.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(lblCustID_PoA, gridBagConstraints);

        txtCustID_PoA.setMaximumSize(new java.awt.Dimension(100, 21));
        txtCustID_PoA.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustID_PoA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustID_PoAActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part1.add(txtCustID_PoA, gridBagConstraints);

        btnCustID_Select.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif"))); // NOI18N
        btnCustID_Select.setMaximumSize(new java.awt.Dimension(25, 21));
        btnCustID_Select.setMinimumSize(new java.awt.Dimension(25, 21));
        btnCustID_Select.setPreferredSize(new java.awt.Dimension(25, 21));
        btnCustID_Select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustID_SelectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        panPowerAttorney_Part1.add(btnCustID_Select, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part1, gridBagConstraints);

        sptPowerAttroney.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney.add(sptPowerAttroney, gridBagConstraints);

        panPowerAttorney_Part2.setMaximumSize(new java.awt.Dimension(240, 205));
        panPowerAttorney_Part2.setMinimumSize(new java.awt.Dimension(240, 205));
        panPowerAttorney_Part2.setPreferredSize(new java.awt.Dimension(240, 205));
        panPowerAttorney_Part2.setLayout(new java.awt.GridBagLayout());

        lblCountry_PowerAttroney.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(lblCountry_PowerAttroney, gridBagConstraints);

        cboCountry_PowerAttroney.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCountry_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(cboCountry_PowerAttroney, gridBagConstraints);

        lblPin_PowerAttroney.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(lblPin_PowerAttroney, gridBagConstraints);

        txtPin_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(txtPin_PowerAttroney, gridBagConstraints);

        lblPhone_PowerAttroney.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(lblPhone_PowerAttroney, gridBagConstraints);

        txtPhone_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(txtPhone_PowerAttroney, gridBagConstraints);

        tdtPeriodFrom_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtPeriodFrom_PowerAttroney.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtPeriodFrom_PowerAttroney.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPeriodFrom_PowerAttroneyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(tdtPeriodFrom_PowerAttroney, gridBagConstraints);

        lblPeriodTo_PowerAttroney.setText("Period To");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(lblPeriodTo_PowerAttroney, gridBagConstraints);

        tdtPeriodTo_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtPeriodTo_PowerAttroney.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtPeriodTo_PowerAttroney.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtPeriodTo_PowerAttroneyFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(tdtPeriodTo_PowerAttroney, gridBagConstraints);

        lblRemark_PowerAttroney.setText("Remarks");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(lblRemark_PowerAttroney, gridBagConstraints);

        txtRemark_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(txtRemark_PowerAttroney, gridBagConstraints);

        lblState_PowerAttroney.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(lblState_PowerAttroney, gridBagConstraints);

        cboState_PowerAttroney.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboState_PowerAttroney.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(cboState_PowerAttroney, gridBagConstraints);

        lblPeriodFrom_PowerAttroney.setText("Period From");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney_Part2.add(lblPeriodFrom_PowerAttroney, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part2, gridBagConstraints);

        panPowerAttorney_Part3.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 4, 4);
        panPowerAttorney.add(panPowerAttorney_Part3, gridBagConstraints);

        panPowerAttroney_Table.setMinimumSize(new java.awt.Dimension(350, 250));
        panPowerAttroney_Table.setPreferredSize(new java.awt.Dimension(350, 250));
        panPowerAttroney_Table.setLayout(new java.awt.GridBagLayout());

        srpPowerAttroney.setMinimumSize(new java.awt.Dimension(350, 250));
        srpPowerAttroney.setPreferredSize(new java.awt.Dimension(350, 250));

        tblPowerAttroney.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Sl No.", "PoA Holder", "Period From", "Period To"
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
        gridBagConstraints.gridx = 3;
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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panPowerAttorney, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txtCustID_PoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustID_PoAActionPerformed
        String txtCustId = txtCustID_PoA.getText();                 // TODO add your handling code here:
        HashMap txtCustIdPoa =new HashMap();
        txtCustIdPoa.put("CUSTOMER ID",txtCustId);
        viewType = "CUSTOMER ID";
        fillData(txtCustIdPoa);
        
    }//GEN-LAST:event_txtCustID_PoAActionPerformed

    private void btnCustID_SelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustID_SelectActionPerformed
        // TODO add your handling code here:
        btnCustID_SelectActionPerformed();
    }//GEN-LAST:event_btnCustID_SelectActionPerformed
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
                observablePoA.setTxtCustID_PoA(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
                //txtCustID_PoA.setText(observablePoA.getTxtCustID_PoA());
                final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
                observablePoA.setPoACustAddr(CustID);
                observablePoA.setPoACustName(CustID);
                observablePoA.setPoACustPhone(CustID);
                observablePoA.ttNotifyObservers();
                setAllPoAEnableDisable(true);
                setPoANewSaveOnlyEnable();
                txtCustID_PoA.setEditable(true);
            }
        }
    }
    private void tdtPeriodTo_PowerAttroneyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPeriodTo_PowerAttroneyFocusLost
        // TODO add your handling code here:
        tdtPeriodTo_PowerAttroneyFocusLost();
    }//GEN-LAST:event_tdtPeriodTo_PowerAttroneyFocusLost
    private void tdtPeriodTo_PowerAttroneyFocusLost(){
        // To check whether this To date is greater than this details From date
        ClientUtil.validateToDate(tdtPeriodTo_PowerAttroney, tdtPeriodFrom_PowerAttroney.getDateValue());
    }
    private void tdtPeriodFrom_PowerAttroneyFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtPeriodFrom_PowerAttroneyFocusLost
        // TODO add your handling code here:
        tdtPeriodFrom_PowerAttroneyFocusLost();
    }//GEN-LAST:event_tdtPeriodFrom_PowerAttroneyFocusLost
    
    private void tdtPeriodFrom_PowerAttroneyFocusLost(){
//        ClientUtil.validateLTDate(tdtPeriodFrom_PowerAttroney);
        // To check whether this From date is less than this details To date
        ClientUtil.validateFromDate(tdtPeriodFrom_PowerAttroney, tdtPeriodTo_PowerAttroney.getDateValue());
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
    }//GEN-LAST:event_tblPowerAttroneyMousePressed
    private void powerAttroneyTableMousePressed(){
        // Actions have to be taken when a record of PoA details is selected
        if (tblPowerAttroney.getSelectedRow() >= 0){
            updateOBFields();
            observablePoA.populatePoATable(tblPowerAttroney.getSelectedRow());
            if ((getLblStatus().equals(ClientConstants.ACTION_STATUS[3]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[7]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[8]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[10])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) || (viewType.equals(ClientConstants.ACTION_STATUS[3]))){
                setAllPoAEnableDisable(false);
                setPoAToolBtnsEnableDisable(false);
            }else{
                updateModePoA = true;
                modePoA = tblPowerAttroney.getSelectedRow();
                setPoAToolBtnsEnableDisable(true);
                setAllPoAEnableDisable(true);
            }
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
        observablePoA.deletePoATable(modePoA);
        setPoAToolBtnsEnableDisable(false);
        setPoANewOnlyEnable();
        observablePoA.resetPoAForm();
        observablePoA.ttNotifyObservers();
        modePoA = -1;
        updateModePoA = false;
    }
    private void btnSave_PoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSave_PoAActionPerformed
        // TODO add your handling code here:
        savePoAactionPerformed();
    }//GEN-LAST:event_btnSave_PoAActionPerformed
    private void savePoAactionPerformed(){
        String strOnBehalfOf = CommonUtil.convertObjToStr(((ComboBoxModel)cboPoACust.getModel()).getKeyForSelected());
        if(!updateModePoA)
        if (!observablePoA.isThisCustomerOnBehalfofOther(txtCustID_PoA.getText(), strOnBehalfOf)){
            return;
        }
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panPowerAttorney_Part1);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        
        final String mandatoryMessage2 = new MandatoryCheck().checkMandatory(getClass().getName(), panPowerAttorney_Part2);
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0){
            displayAlert(mandatoryMessage1+mandatoryMessage2);
        }else{
            setAllPoAEnableDisable(false);
            setPoAToolBtnsEnableDisable(false);
            btnNew_PoA.setEnabled(true);
            updateOBFields();
            if (observablePoA.addPoATab(modePoA,updateModePoA) == 1){
                setAllPoAEnableDisable(true);
                btnSave_PoA.setEnabled(true);
            }else{
                observablePoA.resetPoAForm();
                btnSave_PoA.setEnabled(false);
            }
            updateModePoA=false;
            observablePoA.ttNotifyObservers();
        }
    }
    private void btnNew_PoAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNew_PoAActionPerformed
        // TODO add your handling code here:
        newPoAactionPerformed();
    }//GEN-LAST:event_btnNew_PoAActionPerformed
    private void newPoAactionPerformed(){
        updateOBFields();
        setAllPoAEnableDisable(true);
        txtCustID_PoA.setEditable(true);
        observablePoA.resetPoAForm();
        observablePoA.ttNotifyObservers();
        modePoA = -1;
        updateModePoA = false;
        setPoANewSaveOnlyEnable();
    }
    
    public void setCboPoACustModel(){
        cboPoACust.setModel(observablePoA.getCbmPoACust());
    }
    public boolean checkCustIDExistInJointAcctAndPoA(String CustID){
        return observablePoA.checkCustIDExistInJointAcctAndPoA(CustID);
    }
    
    public void resetPoAForm(){
        observablePoA.resetPoAForm();
        observablePoA.ttNotifyObservers();
    }
    
    public void resetAllFieldsPoA(){
        observablePoA.resetAllFieldsInPoA();
        observablePoA.ttNotifyObservers();
    }
    
    public void resetPoACustID(String Cust_ID){
        observablePoA.resetPoACustID(Cust_ID);
        observablePoA.ttNotifyObservers();
    }
    
    public void clearCboPoACust_ID(){
        observablePoA.clearCboPoACust_ID();
    }
    
    public void setbtnCustEnableDisable(boolean val){
        btnCustID_Select.setEnabled(val);
    }
    
    public void setAllPoAEnableDisable(boolean val){
        txtPoANo.setEditable(false);
//        txtPoANo.setEnabled(val);
        cboAddrType_PoA.setEnabled(false);
        txtCustID_PoA.setEditable(false);
        txtCustID_PoA.setEnabled(val);
        tdtPeriodFrom_PowerAttroney.setEnabled(val);
        tdtPeriodTo_PowerAttroney.setEnabled(val);
        cboPoACust.setEnabled(val);
        txtArea_PowerAttroney.setEditable(false);
        txtArea_PowerAttroney.setEnabled(val);
        txtPhone_PowerAttroney.setEditable(false);
        txtPhone_PowerAttroney.setEnabled(val);
        txtPin_PowerAttroney.setEditable(false);
        txtPin_PowerAttroney.setEnabled(val);
        txtPoaHolderName.setEditable(false);
        txtPoaHolderName.setEnabled(val);
        txtRemark_PowerAttroney.setEnabled(val);
        cboState_PowerAttroney.setEnabled(false);
        txtStreet_PowerAttroney.setEditable(false);
        txtStreet_PowerAttroney.setEnabled(val);
        cboCity_PowerAttroney.setEnabled(false);
        cboCountry_PowerAttroney.setEnabled(false);
        setbtnCustEnableDisable(val);
//        observablePoA.ttNotifyObservers();
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
        observablePoA.ttNotifyObservers();
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
        PowerOfAttorneyUI poa = new PowerOfAttorneyUI("TL");
        frm.getContentPane().add(poa);
        poa.show();
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
    private com.see.truetransact.uicomponent.CButton btnCustID_Select;
    private com.see.truetransact.uicomponent.CButton btnDelete_PoA;
    private com.see.truetransact.uicomponent.CButton btnNew_PoA;
    private com.see.truetransact.uicomponent.CButton btnSave_PoA;
    private com.see.truetransact.uicomponent.CComboBox cboAddrType_PoA;
    private com.see.truetransact.uicomponent.CComboBox cboCity_PowerAttroney;
    private com.see.truetransact.uicomponent.CComboBox cboCountry_PowerAttroney;
    private com.see.truetransact.uicomponent.CComboBox cboPoACust;
    private com.see.truetransact.uicomponent.CComboBox cboState_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblAddrType_PoA;
    private com.see.truetransact.uicomponent.CLabel lblArea_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblCity_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblCountry_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblCustID_PoA;
    private com.see.truetransact.uicomponent.CLabel lblPeriodFrom_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblPeriodTo_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblPhone_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblPin_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblPoACust;
    private com.see.truetransact.uicomponent.CLabel lblPoANo;
    private com.see.truetransact.uicomponent.CLabel lblPoaHolderName;
    private com.see.truetransact.uicomponent.CLabel lblRemark_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblState_PowerAttroney;
    private com.see.truetransact.uicomponent.CLabel lblStreet_PowerAttroney;
    private com.see.truetransact.uicomponent.CPanel panBorrowDetails;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part1;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part2;
    private com.see.truetransact.uicomponent.CPanel panPowerAttorney_Part3;
    private com.see.truetransact.uicomponent.CPanel panPowerAttroney_Table;
    private com.see.truetransact.uicomponent.CSeparator sptPowerAttroney;
    private com.see.truetransact.uicomponent.CScrollPane srpPowerAttroney;
    private com.see.truetransact.uicomponent.CTable tblPowerAttroney;
    private com.see.truetransact.uicomponent.CDateField tdtPeriodFrom_PowerAttroney;
    private com.see.truetransact.uicomponent.CDateField tdtPeriodTo_PowerAttroney;
    private com.see.truetransact.uicomponent.CTextField txtArea_PowerAttroney;
    private com.see.truetransact.uicomponent.CTextField txtCustID_PoA;
    private com.see.truetransact.uicomponent.CTextField txtPhone_PowerAttroney;
    private com.see.truetransact.uicomponent.CTextField txtPin_PowerAttroney;
    private com.see.truetransact.uicomponent.CTextField txtPoANo;
    private com.see.truetransact.uicomponent.CTextField txtPoaHolderName;
    private com.see.truetransact.uicomponent.CTextField txtRemark_PowerAttroney;
    private com.see.truetransact.uicomponent.CTextField txtStreet_PowerAttroney;
    // End of variables declaration//GEN-END:variables
    
}

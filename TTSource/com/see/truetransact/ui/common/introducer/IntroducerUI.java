/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * IntroducerUI.java
 *
 * Created on December 27, 2004, 12:12 PM
 */

package com.see.truetransact.ui.common.introducer;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.TrueTransactMain;

import java.util.Observer;
import java.util.Observable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.List;

//import java.util.*;

/**
 *
 * @author  152721
 */
public class IntroducerUI extends CPanel implements java.util.Observer, UIMandatoryField{
    private HashMap mandatoryMap;
    IntroducerOB observable;
    ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.introducer.IntroducerRB", ProxyParameters.LANGUAGE);
     //private IntroducerRB resourceBundle = new IntroducerRB();
    String screen = "";
    
    /** Creates new form IntroducerUI */
    public IntroducerUI(String screen) {
        initComponents();
        initSetUp(screen);
    }
    
    private void initSetUp(String screen){
        setFieldNames();
        setMandatoryHashMap();
        setObservable();
        internationalize();
        initComponentData();
        setIntroPanel(false);
        setPanInVisible();
        setMaxLenths();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panSelfCustomer);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panIdentity);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panOthers);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panDocDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(),panBank);
        
        this.screen = screen;
    }
    
     /*
      * Creates the instance of OB
      */
    private void setObservable() {
        observable = new IntroducerOB();
        observable.addObserver(this);
    }
    
    public IntroducerOB getIntroducerOB() {
        return observable;
    }
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        cboState.setName("boState");
        btnAcctNo.setName("btnAcctNo");
        cboCity.setName("cboCity");
        cboCountry.setName("cboCountry");
        cboDocTypeDD.setName("cboDocTypeDD");
        cboIdentityTypeID.setName("cboIdentityTypeID");
//        lblAcctHd.setName("lblAcctHd");
//        lblAcctNo.setName("lblAcctNo");
        lblAcctNoOB.setName("lblAcctNoOB");
        lblAcctValue.setName("lblAcctValue");
        lblArea.setName("lblArea");
        lblBankOB.setName("lblBankOB");
        lblBranch.setName("lblBranch");
        lblBranchCode.setName("lblBranchCode");
        lblBranchCodeValue.setName("lblBranchCodeValue");
        lblBranchOB.setName("lblBranchOB");
        lblProdId.setName("lblProdId");
        lblBranchValue.setName("lblBranchValue");
        lblCity.setName("lblCity");
        lblCountry.setName("lblCountry");
        lblAcctNo.setName("lblAcctNo");
//        lblAcctNoValue.setName("lblAcctNoValue");
        lblDesig.setName("lblDesig");
        lblDocID.setName("lblDocID");
        lblDocNoDD.setName("lblDocNoDD");
        lblDocTypeDD.setName("lblDocTypeDD");
        lblExpiryDateDD.setName("lblExpiryDateDD");
        lblIdentityTypeID.setName("lblIdentityTypeID");
        lblIntroName.setName("lblIntroName");
        lblIntroducerType.setName("lblIntroducerType");
        lblIntroducerTypeValue.setName("lblIntroducerTypeValue");
        lblIssuedByDD.setName("lblIssuedByDD");
        lblIssuedByID.setName("lblIssuedByID");
        lblIssuedDateDD.setName("lblIssuedDateDD");
        lblName.setName("lblName");
        lblNameOB.setName("lblNameOB");
        lblNameValue.setName("lblNameValue");
        lblPhone.setName("lblPhone");
        lblPinCode.setName("lblPinCode");
        lblState.setName("lblState");
        lblStreet.setName("lblStreet");
        panBank.setName("panBank");
        panDocDetails.setName("panDocDetails");
        panIdentity.setName("panIdentity");
        panIntroducer.setName("panIntroducer");
        panIntroducerDetails.setName("panIntroducerDetails");
        panIntroducerType.setName("panIntroducerType");
        panOthers.setName("panOthers");
        panSelfCustomer.setName("panSelfCustomer");
        panphone.setName("panphone");
        sepSepOTP5.setName("sepSepOTP5");
        tdtExpiryDateDD.setName("tdtExpiryDateDD");
        tdtIssuedDateDD.setName("tdtIssuedDateDD");
        txtACode.setName("txtACode");
        txtAcctNo.setName("txtAcctNo");
        txtAcctNoOB.setName("txtAcctNoOB");
        txtArea.setName("txtArea");
        txtBankOB.setName("txtBankOB");
        txtBranchOB.setName("txtBranchOB");
        txtProdId.setName("txtProdId");
        txtDesig.setName("txtDesig");
        txtDocID.setName("txtDocID");
        txtDocNoDD.setName("txtDocNoDD");
        txtIntroName.setName("txtIntroName");
        txtIssuedAuthID.setName("txtIssuedAuthID");
        txtIssuedByDD.setName("txtIssuedByDD");
        txtNameOB.setName("txtNameOB");
        txtPhone.setName("txtPhone");
        txtPinCode.setName("txtPinCode");
        txtStreet.setName("txtStreet");
        txtActNum.setName("txtActNum");
    }
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
     private void internationalize() {
         //modified by rishad 09/aug/2018 all label value taking from properties file
        java.util.Locale currentLocale = null;
        currentLocale = new java.util.Locale(TrueTransactMain.language, TrueTransactMain.country);
        resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.introducer.IntroducerRB", currentLocale);
        lblIdentityTypeID.setText(resourceBundle.getString("lblIdentityTypeID"));
        //        ((javax.swing.border.TitledBorder)panIntroducerDetails.getBorder()).setTitle(resourceBundle.getString("panIntroducerDetails"));
        lblIntroducerTypeValue.setText(resourceBundle.getString("lblIntroducerTypeValue"));
        lblIntroName.setText(resourceBundle.getString("lblIntroName"));
        lblStreet.setText(resourceBundle.getString("lblStreet"));
        lblCity.setText(resourceBundle.getString("lblCity"));
        lblBranchCodeValue.setText(resourceBundle.getString("lblBranchCodeValue"));
        lblDocTypeDD.setText(resourceBundle.getString("lblDocTypeDD"));
        lblIssuedByDD.setText(resourceBundle.getString("lblIssuedByDD"));
        lblDocID.setText(resourceBundle.getString("lblDocID"));
        ((javax.swing.border.TitledBorder) panBank.getBorder()).setTitle(resourceBundle.getString("panBank"));
        lblAcctNoOB.setText(resourceBundle.getString("lblAcctNoOB"));
        lblIssuedByID.setText(resourceBundle.getString("lblIssuedByID"));
        ((javax.swing.border.TitledBorder)panOthers.getBorder()).setTitle(resourceBundle.getString("panOthers"));  
        lblExpiryDateDD.setText(resourceBundle.getString("lblExpiryDateDD"));
        lblArea.setText(resourceBundle.getString("lblArea"));
        lblIssuedDateDD.setText(resourceBundle.getString("lblIssuedDateDD"));
        lblPinCode.setText(resourceBundle.getString("lblPinCode"));
        ((javax.swing.border.TitledBorder) panDocDetails.getBorder()).setTitle(resourceBundle.getString("panDocDetails"));
        lblDocNoDD.setText(resourceBundle.getString("lblDocNoDD"));
        lblDesig.setText(resourceBundle.getString("lblDesig"));
        lblAcctValue.setText(resourceBundle.getString("lblAcctValue"));
        lblCountry.setText(resourceBundle.getString("lblCountry"));
        lblBranchValue.setText(resourceBundle.getString("lblBranchValue"));
        lblBranchOB.setText(resourceBundle.getString("lblBranchOB"));
        lblProdId.setText(resourceBundle.getString("lblProdId"));
        lblPhone.setText(resourceBundle.getString("lblPhone"));
        lblIntroducerType.setText(resourceBundle.getString("lblIntroducerType"));
        lblNameOB.setText(resourceBundle.getString("lblNameOB"));
        lblState.setText(resourceBundle.getString("lblState"));
        lblAcctNo.setText(resourceBundle.getString("lblAcctNo"));
        ((javax.swing.border.TitledBorder) panIdentity.getBorder()).setTitle(resourceBundle.getString("panIdentity"));
        lblBankOB.setText(resourceBundle.getString("lblBankOB"));
        lblAcctNo.setText(resourceBundle.getString("lblAcctNo"));
        lblActNum.setText(resourceBundle.getString("lblActNum"));
        lblName.setText(resourceBundle.getString("lblName"));
        ((javax.swing.border.TitledBorder) panSelfCustomer.getBorder()).setTitle(resourceBundle.getString("panSelfCustomer"));
        lblBranchCode.setText(resourceBundle.getString("lblBranchCode"));
        lblBranch.setText(resourceBundle.getString("lblBranch"));
        lblNameValue.setText(resourceBundle.getString("lblNameValue"));   
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        txtAcctNo.setText(observable.getTxtAcctNo());
        txtBankOB.setText(observable.getTxtBankOB());
        txtBranchOB.setText(observable.getTxtBranchOB());
        txtProdId.setText(observable.getTxtProdId());
        txtAcctNoOB.setText(observable.getTxtAcctNoOB());
        txtNameOB.setText(observable.getTxtNameOB());
        cboDocTypeDD.setSelectedItem(observable.getCboDocTypeDD());
        txtDocNoDD.setText(observable.getTxtDocNoDD());
        txtIssuedByDD.setText(observable.getTxtIssuedByDD());
        tdtIssuedDateDD.setDateValue(observable.getTdtIssuedDateDD());
        tdtExpiryDateDD.setDateValue(observable.getTdtExpiryDateDD());
        cboIdentityTypeID.setSelectedItem(observable.getCboIdentityTypeID());
        txtIssuedAuthID.setText(observable.getTxtIssuedAuthID());
        txtDocID.setText(observable.getTxtDocID());
        txtIntroName.setText(observable.getTxtIntroName());
        txtDesig.setText(observable.getTxtDesig());
        txtACode.setText(observable.getTxtACode());
        txtPhone.setText(observable.getTxtPhone());
        txtStreet.setText(observable.getTxtStreet());
        txtArea.setText(observable.getTxtArea());
        cboCountry.setSelectedItem(observable.getCboCountry());
        cboCity.setSelectedItem(observable.getCboCity());
        cboState.setSelectedItem(observable.getCboState());
        txtPinCode.setText(observable.getTxtPinCode());
        txtActNum.setText(observable.getTxtActNum());
        
//       observable.getSelfCustomerIntroDetails(observable.getTxtAcctNo()); 
        txtAcctNo.setText(observable.getTxtAcctNo());
//       lblAcctNoValue.setText(observable.getlblAcctNoValue();
        lblNameValue.setText(observable.getLblNameValue());
        lblAcctValue.setText(observable.getLblAcctValue());
        lblBranchCodeValue.setText(observable.getLblBranchCodeValue());
        lblBranchValue.setText(observable.getLblBranchValue());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtAcctNo(txtAcctNo.getText());
        observable.setTxtBankOB(txtBankOB.getText());
        observable.setTxtBranchOB(txtBranchOB.getText());
        observable.setTxtProdId(txtProdId.getText());
        observable.setTxtAcctNoOB(txtAcctNoOB.getText());
        observable.setTxtNameOB(txtNameOB.getText());
        observable.setCboDocTypeDD((String) cboDocTypeDD.getSelectedItem());
        observable.setTxtDocNoDD(txtDocNoDD.getText());
        observable.setTxtIssuedByDD(txtIssuedByDD.getText());
        observable.setTdtIssuedDateDD(tdtIssuedDateDD.getDateValue());
        observable.setTdtExpiryDateDD(tdtExpiryDateDD.getDateValue());
        observable.setCboIdentityTypeID((String) cboIdentityTypeID.getSelectedItem());
        observable.setTxtIssuedAuthID(txtIssuedAuthID.getText());
        observable.setTxtDocID(txtDocID.getText());
        observable.setTxtIntroName(txtIntroName.getText());
        observable.setTxtDesig(txtDesig.getText());
        observable.setTxtACode(txtACode.getText());
        observable.setTxtPhone(txtPhone.getText());
        observable.setTxtStreet(txtStreet.getText());
        observable.setTxtArea(txtArea.getText());
        observable.setCboCountry((String) cboCountry.getSelectedItem());
        observable.setCboCity((String) cboCity.getSelectedItem());
        observable.setCboState((String)cboState.getSelectedItem());
        observable.setTxtPinCode(txtPinCode.getText());
        observable.setTxtActNum(txtActNum.getText());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtAcctNo", new Boolean(true));
        mandatoryMap.put("txtBankOB", new Boolean(true));
        mandatoryMap.put("txtBranchOB", new Boolean(true));
        mandatoryMap.put("txtAcctNoOB", new Boolean(true));
        mandatoryMap.put("txtNameOB", new Boolean(true));
        mandatoryMap.put("cboDocTypeDD", new Boolean(true));
        mandatoryMap.put("txtDocNoDD", new Boolean(true));
        mandatoryMap.put("txtIssuedByDD", new Boolean(true));
        mandatoryMap.put("tdtIssuedDateDD", new Boolean(true));
        mandatoryMap.put("tdtExpiryDateDD", new Boolean(true));
        mandatoryMap.put("cboIdentityTypeID", new Boolean(true));
        mandatoryMap.put("txtIssuedAuthID", new Boolean(true));
        mandatoryMap.put("txtDocID", new Boolean(true));
        mandatoryMap.put("txtIntroName", new Boolean(true));
        mandatoryMap.put("txtDesig", new Boolean(false));
        mandatoryMap.put("txtACode", new Boolean(true));
        mandatoryMap.put("txtPhone", new Boolean(true));
        mandatoryMap.put("txtStreet", new Boolean(true));
        mandatoryMap.put("txtArea", new Boolean(true));
        mandatoryMap.put("cboCountry", new Boolean(true));
        mandatoryMap.put("cboState", new Boolean(true));
        mandatoryMap.put("cboCity", new Boolean(true));
        mandatoryMap.put("txtPinCode", new Boolean(true));
        mandatoryMap.put("txtActNum", new Boolean(false));
    }
    
    /** To do the Mandatory Check...*/
    public String mandatoryCheck(String panSelected){
        String mandatoryMessage = "";
        if(panSelected.equalsIgnoreCase("SELF_CUSTOMER")){
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panSelfCustomer);
            
        }else if(panSelected.equalsIgnoreCase("IDENTITY")){
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panIdentity);
            
        } else if(panSelected.equalsIgnoreCase("OTHERS")){
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panOthers);
            
        } else if(panSelected.equalsIgnoreCase("DOC_DETAILS")){
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panDocDetails);
            
        } else if(panSelected.equalsIgnoreCase("OTHER_BANK")){
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(),panBank);
        }
        
        return mandatoryMessage;
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    private void setMaxLenths() {
        txtIssuedByDD.setMaxLength(64);
        txtDocNoDD.setMaxLength(32);
        txtDocNoDD.setAllowAll(true);
        txtBankOB.setMaxLength(64);
        txtBranchOB.setMaxLength(64);
        txtProdId.setMaxLength(64);
        txtAcctNoOB.setMaxLength(32);
        txtAcctNoOB.setAllowAll(true);
        txtNameOB.setMaxLength(64);
        
        txtDocID.setMaxLength(32);
        txtDocID.setAllowAll(true);
        txtIssuedAuthID.setMaxLength(128);
        txtIssuedAuthID.setAllowAll(true);
        txtAcctNo.setMaxLength(10);
        
        txtIntroName.setMaxLength(64);
        txtDesig.setMaxLength(64);
        txtStreet.setMaxLength(256);
        txtArea.setMaxLength(128);
        txtACode.setMaxLength(8);
        txtACode.setValidation(new NumericValidation());
        txtPhone.setMaxLength(16);
        txtPhone.setValidation(new NumericValidation());
        txtPinCode.setValidation(new PincodeValidation_IN());
        txtBranchOB.setAllowNumber(true);
        txtActNum.setMaxLength(16);
        txtActNum.setAllowAll(true);
    }
    
    
    private void initComponentData() {
        cboCity.setModel(observable.getCbmCity());
        cboCountry.setModel(observable.getCbmCountry());
        cboState.setModel(observable.getCbmState());
        
        cboDocTypeDD.setModel(observable.getCbmDocTypeDD());
        cboIdentityTypeID.setModel(observable.getCbmIdentityTypeID());
    }
    
    /* Make all the introiduction panel as invisible */
    public void setPanInVisible(){
        panSelfCustomer.setVisible(false);
        panBank.setVisible(false);
        panIdentity.setVisible(false);
        panDocDetails.setVisible(false);
        panOthers.setVisible(false);
    }
    
    public void setIntroPanel(boolean value){
        panIntroducerType.setVisible(value);
        panIntroducerDetails.setVisible(value);
    }
    
    /**
     * To Ste the Particular Panel Visible, depending on the Value of IntroduceType...
     */
    public void setPanVisible(String panSelected){
        if(panSelected.equalsIgnoreCase("SELF_CUSTOMER")){
            panSelfCustomer.setVisible(true);
        }else if(panSelected.equalsIgnoreCase("IDENTITY")){
            panIdentity.setVisible(true);
        } else if(panSelected.equalsIgnoreCase("OTHERS")){
            panOthers.setVisible(true);
        } else if(panSelected.equalsIgnoreCase("DOC_DETAILS")){
            panDocDetails.setVisible(true);
        } else if(panSelected.equalsIgnoreCase("OTHER_BANK")){
            panBank.setVisible(true);
        } else{
            setPanInVisible();
        }
        
        
        if(getActionType()!=0){
            ClientUtil.enableDisable(this, true);
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panIntroducer = new com.see.truetransact.uicomponent.CPanel();
        panIntroducerType = new com.see.truetransact.uicomponent.CPanel();
        lblIntroducerType = new com.see.truetransact.uicomponent.CLabel();
        lblIntroducerTypeValue = new com.see.truetransact.uicomponent.CLabel();
        panIntroducerDetails = new com.see.truetransact.uicomponent.CPanel();
        panSelfCustomer = new com.see.truetransact.uicomponent.CPanel();
        lblAcctNo = new com.see.truetransact.uicomponent.CLabel();
        txtAcctNo = new com.see.truetransact.uicomponent.CTextField();
        btnAcctNo = new com.see.truetransact.uicomponent.CButton();
        lblNameValue = new com.see.truetransact.uicomponent.CLabel();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblAcctValue = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblBranchCodeValue = new com.see.truetransact.uicomponent.CLabel();
        lblBranch = new com.see.truetransact.uicomponent.CLabel();
        lblBranchValue = new com.see.truetransact.uicomponent.CLabel();
        lblActNum = new com.see.truetransact.uicomponent.CLabel();
        btnActNum = new com.see.truetransact.uicomponent.CButton();
        txtActNum = new com.see.truetransact.uicomponent.CTextField();
        panBank = new com.see.truetransact.uicomponent.CPanel();
        lblBankOB = new com.see.truetransact.uicomponent.CLabel();
        txtBankOB = new com.see.truetransact.uicomponent.CTextField();
        lblBranchOB = new com.see.truetransact.uicomponent.CLabel();
        txtBranchOB = new com.see.truetransact.uicomponent.CTextField();
        lblAcctNoOB = new com.see.truetransact.uicomponent.CLabel();
        txtAcctNoOB = new com.see.truetransact.uicomponent.CTextField();
        lblNameOB = new com.see.truetransact.uicomponent.CLabel();
        txtNameOB = new com.see.truetransact.uicomponent.CTextField();
        txtProdId = new com.see.truetransact.uicomponent.CTextField();
        lblProdId = new com.see.truetransact.uicomponent.CLabel();
        panDocDetails = new com.see.truetransact.uicomponent.CPanel();
        lblDocTypeDD = new com.see.truetransact.uicomponent.CLabel();
        cboDocTypeDD = new com.see.truetransact.uicomponent.CComboBox();
        lblDocNoDD = new com.see.truetransact.uicomponent.CLabel();
        txtDocNoDD = new com.see.truetransact.uicomponent.CTextField();
        lblIssuedByDD = new com.see.truetransact.uicomponent.CLabel();
        txtIssuedByDD = new com.see.truetransact.uicomponent.CTextField();
        lblIssuedDateDD = new com.see.truetransact.uicomponent.CLabel();
        tdtIssuedDateDD = new com.see.truetransact.uicomponent.CDateField();
        lblExpiryDateDD = new com.see.truetransact.uicomponent.CLabel();
        tdtExpiryDateDD = new com.see.truetransact.uicomponent.CDateField();
        panIdentity = new com.see.truetransact.uicomponent.CPanel();
        lblIdentityTypeID = new com.see.truetransact.uicomponent.CLabel();
        cboIdentityTypeID = new com.see.truetransact.uicomponent.CComboBox();
        lblIssuedByID = new com.see.truetransact.uicomponent.CLabel();
        txtIssuedAuthID = new com.see.truetransact.uicomponent.CTextField();
        lblDocID = new com.see.truetransact.uicomponent.CLabel();
        txtDocID = new com.see.truetransact.uicomponent.CTextField();
        panOthers = new com.see.truetransact.uicomponent.CPanel();
        lblIntroName = new com.see.truetransact.uicomponent.CLabel();
        txtIntroName = new com.see.truetransact.uicomponent.CTextField();
        lblDesig = new com.see.truetransact.uicomponent.CLabel();
        txtDesig = new com.see.truetransact.uicomponent.CTextField();
        lblPhone = new com.see.truetransact.uicomponent.CLabel();
        panphone = new com.see.truetransact.uicomponent.CPanel();
        txtACode = new com.see.truetransact.uicomponent.CTextField();
        txtPhone = new com.see.truetransact.uicomponent.CTextField();
        sepSepOTP5 = new com.see.truetransact.uicomponent.CSeparator();
        lblStreet = new com.see.truetransact.uicomponent.CLabel();
        txtStreet = new com.see.truetransact.uicomponent.CTextField();
        lblArea = new com.see.truetransact.uicomponent.CLabel();
        txtArea = new com.see.truetransact.uicomponent.CTextField();
        lblCountry = new com.see.truetransact.uicomponent.CLabel();
        cboCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblState = new com.see.truetransact.uicomponent.CLabel();
        cboState = new com.see.truetransact.uicomponent.CComboBox();
        lblCity = new com.see.truetransact.uicomponent.CLabel();
        cboCity = new com.see.truetransact.uicomponent.CComboBox();
        lblPinCode = new com.see.truetransact.uicomponent.CLabel();
        txtPinCode = new com.see.truetransact.uicomponent.CTextField();

        setLayout(new java.awt.GridBagLayout());

        panIntroducer.setLayout(new java.awt.GridBagLayout());

        panIntroducerType.setLayout(new java.awt.GridBagLayout());

        lblIntroducerType.setText("Type of Introduction:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntroducerType.add(lblIntroducerType, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIntroducerType.add(lblIntroducerTypeValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panIntroducer.add(panIntroducerType, gridBagConstraints);

        panIntroducerDetails.setLayout(new java.awt.GridBagLayout());

        panSelfCustomer.setLayout(new java.awt.GridBagLayout());

        panSelfCustomer.setBorder(new javax.swing.border.TitledBorder("Self or Existing Customer"));
        lblAcctNo.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSelfCustomer.add(lblAcctNo, gridBagConstraints);

        txtAcctNo.setEditable(false);
        txtAcctNo.setMaxLength(10);
        txtAcctNo.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSelfCustomer.add(txtAcctNo, gridBagConstraints);

        btnAcctNo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnAcctNo.setMaximumSize(new java.awt.Dimension(21, 21));
        btnAcctNo.setMinimumSize(new java.awt.Dimension(21, 21));
        btnAcctNo.setPreferredSize(new java.awt.Dimension(21, 21));
        btnAcctNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcctNoActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSelfCustomer.add(btnAcctNo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panSelfCustomer.add(lblNameValue, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        panSelfCustomer.add(lblName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSelfCustomer.add(lblAcctValue, gridBagConstraints);

        lblBranchCode.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        panSelfCustomer.add(lblBranchCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSelfCustomer.add(lblBranchCodeValue, gridBagConstraints);

        lblBranch.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        panSelfCustomer.add(lblBranch, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSelfCustomer.add(lblBranchValue, gridBagConstraints);

        lblActNum.setText("Act Number");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSelfCustomer.add(lblActNum, gridBagConstraints);

        btnActNum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnActNum.setMaximumSize(new java.awt.Dimension(21, 21));
        btnActNum.setMinimumSize(new java.awt.Dimension(21, 21));
        btnActNum.setPreferredSize(new java.awt.Dimension(21, 21));
        btnActNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActNumActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 10);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panSelfCustomer.add(btnActNum, gridBagConstraints);

        txtActNum.setMaxLength(10);
        txtActNum.setMinimumSize(new java.awt.Dimension(100, 21));
        txtActNum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtActNumActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panSelfCustomer.add(txtActNum, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panIntroducerDetails.add(panSelfCustomer, gridBagConstraints);

        panBank.setLayout(new java.awt.GridBagLayout());

        panBank.setBorder(new javax.swing.border.TitledBorder("Other Bank"));
        lblBankOB.setText("Bank Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBank.add(lblBankOB, gridBagConstraints);

        txtBankOB.setMaxLength(128);
        txtBankOB.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panBank.add(txtBankOB, gridBagConstraints);

        lblBranchOB.setText("Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBank.add(lblBranchOB, gridBagConstraints);

        txtBranchOB.setMaxLength(128);
        txtBranchOB.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panBank.add(txtBranchOB, gridBagConstraints);

        lblAcctNoOB.setText("Account No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBank.add(lblAcctNoOB, gridBagConstraints);

        txtAcctNoOB.setMaxLength(10);
        txtAcctNoOB.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panBank.add(txtAcctNoOB, gridBagConstraints);

        lblNameOB.setText("Branch Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBank.add(lblNameOB, gridBagConstraints);

        txtNameOB.setMaxLength(384);
        txtNameOB.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBank.add(txtNameOB, gridBagConstraints);

        txtProdId.setMaxLength(128);
        txtProdId.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panBank.add(txtProdId, gridBagConstraints);

        lblProdId.setText("Product ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panBank.add(lblProdId, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panIntroducerDetails.add(panBank, gridBagConstraints);

        panDocDetails.setLayout(new java.awt.GridBagLayout());

        panDocDetails.setBorder(new javax.swing.border.TitledBorder("Document Details"));
        lblDocTypeDD.setText("Document Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocDetails.add(lblDocTypeDD, gridBagConstraints);

        cboDocTypeDD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panDocDetails.add(cboDocTypeDD, gridBagConstraints);

        lblDocNoDD.setText("Document No.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocDetails.add(lblDocNoDD, gridBagConstraints);

        txtDocNoDD.setMaxLength(32);
        txtDocNoDD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 14);
        panDocDetails.add(txtDocNoDD, gridBagConstraints);

        lblIssuedByDD.setText("Issued By");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panDocDetails.add(lblIssuedByDD, gridBagConstraints);

        txtIssuedByDD.setMaxLength(128);
        txtIssuedByDD.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 4, 4, 4);
        panDocDetails.add(txtIssuedByDD, gridBagConstraints);

        lblIssuedDateDD.setText("Issue Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocDetails.add(lblIssuedDateDD, gridBagConstraints);

        tdtIssuedDateDD.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtIssuedDateDD.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtIssuedDateDD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtIssuedDateDDFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocDetails.add(tdtIssuedDateDD, gridBagConstraints);

        lblExpiryDateDD.setText("Expiry Date");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocDetails.add(lblExpiryDateDD, gridBagConstraints);

        tdtExpiryDateDD.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtExpiryDateDD.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtExpiryDateDD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtExpiryDateDDFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panDocDetails.add(tdtExpiryDateDD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panIntroducerDetails.add(panDocDetails, gridBagConstraints);

        panIdentity.setLayout(new java.awt.GridBagLayout());

        panIdentity.setBorder(new javax.swing.border.TitledBorder("Identity Info."));
        lblIdentityTypeID.setText("Identity Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIdentity.add(lblIdentityTypeID, gridBagConstraints);

        cboIdentityTypeID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIdentity.add(cboIdentityTypeID, gridBagConstraints);

        lblIssuedByID.setText("Issuing Authority");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIdentity.add(lblIssuedByID, gridBagConstraints);

        txtIssuedAuthID.setMaxLength(128);
        txtIssuedAuthID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIdentity.add(txtIssuedAuthID, gridBagConstraints);

        lblDocID.setText("Document ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIdentity.add(lblDocID, gridBagConstraints);

        txtDocID.setMaxLength(32);
        txtDocID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panIdentity.add(txtDocID, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panIntroducerDetails.add(panIdentity, gridBagConstraints);

        panOthers.setLayout(new java.awt.GridBagLayout());

        panOthers.setBorder(new javax.swing.border.TitledBorder("Others"));
        lblIntroName.setText("Introducer's Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panOthers.add(lblIntroName, gridBagConstraints);

        txtIntroName.setMaxLength(64);
        txtIntroName.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panOthers.add(txtIntroName, gridBagConstraints);

        lblDesig.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(lblDesig, gridBagConstraints);

        txtDesig.setMaxLength(64);
        txtDesig.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(txtDesig, gridBagConstraints);

        lblPhone.setText("Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(lblPhone, gridBagConstraints);

        panphone.setLayout(new java.awt.GridBagLayout());

        txtACode.setPreferredSize(new java.awt.Dimension(50, 21));
        txtACode.setMaxLength(5);
        txtACode.setMinimumSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        panphone.add(txtACode, gridBagConstraints);

        txtPhone.setMaxLength(12);
        txtPhone.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        panphone.add(txtPhone, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(panphone, gridBagConstraints);

        sepSepOTP5.setBorder(new javax.swing.border.EtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        panOthers.add(sepSepOTP5, gridBagConstraints);

        lblStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panOthers.add(lblStreet, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        panOthers.add(txtStreet, gridBagConstraints);

        lblArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(lblArea, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(txtArea, gridBagConstraints);

        lblCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(lblCountry, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(cboCountry, gridBagConstraints);

        lblState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(lblState, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(cboState, gridBagConstraints);

        lblCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(lblCity, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(cboCity, gridBagConstraints);

        lblPinCode.setText("Pin Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(lblPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        panOthers.add(txtPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panIntroducerDetails.add(panOthers, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panIntroducer.add(panIntroducerDetails, gridBagConstraints);

        add(panIntroducer, new java.awt.GridBagConstraints());

    }//GEN-END:initComponents

    private void txtActNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActNumActionPerformed
        // TODO add your handling code here:
        String act_num=(String)txtActNum.getText();
        HashMap whereMap = new HashMap();
        whereMap.put("ACT_NUM",act_num);
        HashMap where = new HashMap();
        whereMap.put("CUST_TYPE","");
        whereMap.put("CONSTITUTION",screen);
        if(screen.equalsIgnoreCase("INDIVIDUAL"))
            whereMap.put("IND","");
        else
            whereMap.put("COR","") ;
        List lst = ClientUtil.executeQuery("OperativeAcct.getCustDataForCustomerIntro", whereMap);
        if (lst.size()>0) {
            where =(HashMap)lst.get(0);
            String CUSTOMER_ID=CommonUtil.convertObjToStr(where.get("CUSTOMER ID"));
            observable.setTxtActNum(act_num);
            observable.getSelfCustomerIntroDetails(CUSTOMER_ID);
            observable.ttNotifyObservers();
            txtAcctNo.setText(CUSTOMER_ID);
            txtActNum.setText(act_num);
        }
        else{
            ClientUtil.displayAlert("Invalid Number");
            txtAcctNo.setText("");
            txtActNum.setText("");
        }
        
    }//GEN-LAST:event_txtActNumActionPerformed

    private void btnActNumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActNumActionPerformed
        // TODO add your handling code here:
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("CUST_TYPE","");
        whereMap.put("CONSTITUTION",screen);
        if(screen.equalsIgnoreCase("INDIVIDUAL"))
            whereMap.put("IND","");
        else
            whereMap.put("COR","") ;
        viewMap.put(CommonConstants.MAP_WHERE,whereMap);
        //        viewMap.put(CommonConstants.MAP_NAME, "OperativeAcct.getCustData");
        viewMap.put(CommonConstants.MAP_NAME, "OperativeAcct.getCustDataForCustomer");
        new ViewAll(this, "Introducer",viewMap).show();
    }//GEN-LAST:event_btnActNumActionPerformed

    private void tdtExpiryDateDDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtExpiryDateDDFocusLost
        // TODO add your handling code here:
        ClientUtil.validateToDate(tdtExpiryDateDD,ClientUtil.getCurrentDateinDDMMYYYY());
    }//GEN-LAST:event_tdtExpiryDateDDFocusLost

    private void tdtIssuedDateDDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtIssuedDateDDFocusLost
        // TODO add your handling code here:
         ClientUtil.validateLTDate(tdtIssuedDateDD);
    }//GEN-LAST:event_tdtIssuedDateDDFocusLost
    
    private void btnAcctNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcctNoActionPerformed
        // TODO add your handling code here:
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("CUST_TYPE","");
        whereMap.put("CONSTITUTION",screen);
        if(screen.equalsIgnoreCase("INDIVIDUAL"))
            whereMap.put("IND","");
        else
            whereMap.put("COR","") ;
        viewMap.put(CommonConstants.MAP_WHERE,whereMap);
        viewMap.put(CommonConstants.MAP_NAME, "OperativeAcct.getCustDataForCustomer");
        new ViewAll(this, "Introducer",viewMap).show();
    }//GEN-LAST:event_btnAcctNoActionPerformed
    public void fillData(Object obj) {
        HashMap hash = (HashMap) obj;
        final String CUSTOMER_ID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
        final String ACT_NUM = CommonUtil.convertObjToStr(hash.get("ACT NUM"));
        observable.getSelfCustomerIntroDetails(CUSTOMER_ID);
        observable.ttNotifyObservers(); 
        txtActNum.setText(ACT_NUM);
        txtAcctNo.setText(CUSTOMER_ID);
        
     
    }
    
    /**
     * To set the Type of Introducer at the top-left corner of the Screen...
     */
    public void setIntroducerType(String type){
        lblIntroducerTypeValue.setText(type);
        observable.setIntroType(type);
    }
    /**
     * To reset the Data in the Introducer Screen...
     */
    public void resetIntroducerData(){
        observable.resetLabels();
        observable.resetForm();
        observable.ttNotifyObservers();
        lblIntroducerTypeValue.setText("");
    }
    /**
     * To set the Introducer panel as enable and/or disable...
     */
    public void enableIntroducerData(boolean value){
        ClientUtil.enableDisable(panIntroducer, value);
    }
    
    public void enableDisableBtn(boolean flag){
        btnAcctNo.setEnabled(flag);
        btnActNum.setEnabled(flag);
    }
    
    int actionType = -1;
    public void setActionType(int actionType){
        this.actionType = actionType;
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    //
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame jf = new javax.swing.JFrame();
        
        IntroducerUI bui = new IntroducerUI("");
        jf.setSize(300,525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAcctNo;
    private com.see.truetransact.uicomponent.CButton btnActNum;
    private com.see.truetransact.uicomponent.CComboBox cboCity;
    private com.see.truetransact.uicomponent.CComboBox cboCountry;
    private com.see.truetransact.uicomponent.CComboBox cboDocTypeDD;
    private com.see.truetransact.uicomponent.CComboBox cboIdentityTypeID;
    private com.see.truetransact.uicomponent.CComboBox cboState;
    private com.see.truetransact.uicomponent.CLabel lblAcctNo;
    private com.see.truetransact.uicomponent.CLabel lblAcctNoOB;
    private com.see.truetransact.uicomponent.CLabel lblAcctValue;
    private com.see.truetransact.uicomponent.CLabel lblActNum;
    private com.see.truetransact.uicomponent.CLabel lblArea;
    private com.see.truetransact.uicomponent.CLabel lblBankOB;
    private com.see.truetransact.uicomponent.CLabel lblBranch;
    private com.see.truetransact.uicomponent.CLabel lblBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblBranchCodeValue;
    private com.see.truetransact.uicomponent.CLabel lblBranchOB;
    private com.see.truetransact.uicomponent.CLabel lblBranchValue;
    private com.see.truetransact.uicomponent.CLabel lblCity;
    private com.see.truetransact.uicomponent.CLabel lblCountry;
    private com.see.truetransact.uicomponent.CLabel lblDesig;
    private com.see.truetransact.uicomponent.CLabel lblDocID;
    private com.see.truetransact.uicomponent.CLabel lblDocNoDD;
    private com.see.truetransact.uicomponent.CLabel lblDocTypeDD;
    private com.see.truetransact.uicomponent.CLabel lblExpiryDateDD;
    private com.see.truetransact.uicomponent.CLabel lblIdentityTypeID;
    private com.see.truetransact.uicomponent.CLabel lblIntroName;
    private com.see.truetransact.uicomponent.CLabel lblIntroducerType;
    private com.see.truetransact.uicomponent.CLabel lblIntroducerTypeValue;
    private com.see.truetransact.uicomponent.CLabel lblIssuedByDD;
    private com.see.truetransact.uicomponent.CLabel lblIssuedByID;
    private com.see.truetransact.uicomponent.CLabel lblIssuedDateDD;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblNameOB;
    private com.see.truetransact.uicomponent.CLabel lblNameValue;
    private com.see.truetransact.uicomponent.CLabel lblPhone;
    private com.see.truetransact.uicomponent.CLabel lblPinCode;
    private com.see.truetransact.uicomponent.CLabel lblProdId;
    private com.see.truetransact.uicomponent.CLabel lblState;
    private com.see.truetransact.uicomponent.CLabel lblStreet;
    private com.see.truetransact.uicomponent.CPanel panBank;
    private com.see.truetransact.uicomponent.CPanel panDocDetails;
    private com.see.truetransact.uicomponent.CPanel panIdentity;
    private com.see.truetransact.uicomponent.CPanel panIntroducer;
    private com.see.truetransact.uicomponent.CPanel panIntroducerDetails;
    private com.see.truetransact.uicomponent.CPanel panIntroducerType;
    private com.see.truetransact.uicomponent.CPanel panOthers;
    private com.see.truetransact.uicomponent.CPanel panSelfCustomer;
    private com.see.truetransact.uicomponent.CPanel panphone;
    private com.see.truetransact.uicomponent.CSeparator sepSepOTP5;
    private com.see.truetransact.uicomponent.CDateField tdtExpiryDateDD;
    private com.see.truetransact.uicomponent.CDateField tdtIssuedDateDD;
    private com.see.truetransact.uicomponent.CTextField txtACode;
    private com.see.truetransact.uicomponent.CTextField txtAcctNo;
    private com.see.truetransact.uicomponent.CTextField txtAcctNoOB;
    private com.see.truetransact.uicomponent.CTextField txtActNum;
    private com.see.truetransact.uicomponent.CTextField txtArea;
    private com.see.truetransact.uicomponent.CTextField txtBankOB;
    private com.see.truetransact.uicomponent.CTextField txtBranchOB;
    private com.see.truetransact.uicomponent.CTextField txtDesig;
    private com.see.truetransact.uicomponent.CTextField txtDocID;
    private com.see.truetransact.uicomponent.CTextField txtDocNoDD;
    private com.see.truetransact.uicomponent.CTextField txtIntroName;
    private com.see.truetransact.uicomponent.CTextField txtIssuedAuthID;
    private com.see.truetransact.uicomponent.CTextField txtIssuedByDD;
    private com.see.truetransact.uicomponent.CTextField txtNameOB;
    private com.see.truetransact.uicomponent.CTextField txtPhone;
    private com.see.truetransact.uicomponent.CTextField txtPinCode;
    private com.see.truetransact.uicomponent.CTextField txtProdId;
    private com.see.truetransact.uicomponent.CTextField txtStreet;
    // End of variables declaration//GEN-END:variables
    
}

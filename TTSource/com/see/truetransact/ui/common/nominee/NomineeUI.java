/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * NomineeUI.java
 *
 * Created on December 23, 2004, 2:14 PM
 */

package com.see.truetransact.ui.common.nominee;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.uicomponent.CObservable;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uivalidation.PercentageValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;

import javax.swing.ListSelectionModel;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  152721
 */
public class NomineeUI extends CPanel implements java.util.Observer, UIMandatoryField{
    private HashMap mandatoryMap;
    private String mainCustomerId ;
    private StringBuffer customerList ;
//    private NomineeRB resourceBundle = new NomineeRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.nominee.NomineeRB", ProxyParameters.LANGUAGE);
    NomineeOB observable;
    int updateValue = -1;
    int rowNo= -1;
    double share = 0;
    boolean maxAllowed = false;
    final int DEFAULT_NOMINEE = 1;
    int maxNominee = DEFAULT_NOMINEE;
    
    String screen = "";
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String REJECT = "REJECT";
    private final        String EXCEPTION = "EXCEPTION";
    String viewType = "";
    String lblStatus = "";
   
    
    /** Creates new form NomineeUI */
    public NomineeUI(String screen) {
        initComponents();
        initSetUp(screen, false);
    }
    
    /** Creates new form NomineeUI */
    public NomineeUI(String screen, boolean max) {
        initComponents();
        initSetUp(screen, max);
    }
    
    private void initSetUp(String screen, boolean max){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        
        initComponentData();
        
        ClientUtil.enableDisable(panNomineeDetails, false);
        ClientUtil.enableDisable(panGuardianDetails, false);
        enableDisableNominee_SaveDelete();
        
        txtTotalShareNO.setText(String.valueOf(0));
        txtTotalShareNO.setEditable(false);
        txtMinNominees.setEnabled(false);
        //__ To Add the Max Nominee Allowed in the Screen...
        //        lblMaxNominee.setVisible(max);
        //        txtMaxNominee.setVisible(max);
        txtMaxNominee.setAllowAll(true);
        maxAllowed = max;
        
        lblMinNominees.setVisible(false);
        txtMinNominees.setVisible(false);
        txtMinNominees.setText("0");
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panNomineeDetails);
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panGuardianDetails);
        
        setHelpMessage();
        
        this.screen = screen;
        setMaxLenths();
    }
    /*
     * Creates the instance of OB
     */
    private void setObservable() {
        try {
            observable = NomineeOB.getInstance();
            observable.addObserver(this);
        } catch (Exception ex) {
            Logger.getLogger(NomineeUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public NomineeOB getNomineeOB() {
        return observable;
    }
    
    /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        btnAddNO.setName("btnAddNO");
        btnNewNO.setName("btnNewNO");
        btnRemoveNO.setName("btnRemoveNO");
        cboGCity.setName("cboGCity");
        cboGCountry.setName("cboGCountry");
        cboGState.setName("cboGState");
        cboNCity.setName("cboNCity");
        cboNCountry.setName("cboNCountry");
        cboNState.setName("cboNState");
        cboNomineeRelationNO.setName("cboNomineeRelationNO");
        cboNomineeStatus.setName("cboNomineeStatus");
        cboRelationNO.setName("cboRelationNO");
        tdtMinorDOBNO.setName("tdtMinorDOBNO");
        lblGArea.setName("lblGArea");
        lblGCity.setName("lblGCity");
        lblGCountry.setName("lblGCountry");
        lblGPinCode.setName("lblGPinCode");
        lblGState.setName("lblGState");
        lblGStreet.setName("lblGStreet");
        lblGuardianNameNO.setName("lblGuardianNameNO");
        lblGuardianPhoneNO.setName("lblGuardianPhoneNO");
        lblMinNominees.setName("lblMinNominees");
        lblMinorDOBNO.setName("lblMinorDOBNO");
        lblNArea.setName("lblNArea");
        lblNCity.setName("lblNCity");
        lblNCountry.setName("lblNCountry");
        lblNPinCode.setName("lblNPinCode");
        lblNState.setName("lblNState");
        lblNStreet.setName("lblNStreet");
        lblNomineeNameNO.setName("lblNomineeNameNO");
        lblNomineePhoneNO.setName("lblNomineePhoneNO");
        lblNomineeRelationNO.setName("lblNomineeRelationNO");
        lblNomineeShareNO.setName("lblNomineeShareNO");
        lblNomineeStatusNO.setName("lblNomineeStatusNO");
        lblRelationNO.setName("lblRelationNO");
        lblTotalShareNO.setName("lblTotalShareNO");
        panGuardAddr.setName("panGuardAddr");
        panGuardData.setName("panGuardData");
        panGuardianDetails.setName("panGuardianDetails");
        panNominee.setName("panNominee");
        panNomineeAddr.setName("panNomineeAddr");
        panNomineeDetails.setName("panNomineeDetails");
        panNomineeInfo.setName("panNomineeInfo");
        panNomineeList.setName("panNomineeList");
        panNomineedata.setName("panNomineedata");
        panShareAddEditRemoveNO.setName("panShareAddEditRemoveNO");
        phoneGPanelNO.setName("phoneGPanelNO");
        phoneNPanelNO.setName("phoneNPanelNO");
        sepSepNOP4.setName("sepSepNOP4");
        sptGuard.setName("sptGuard");
        sptNominee.setName("sptNominee");
        srpListNomineeNO.setName("srpListNomineeNO");
        tblListNominee.setName("tblListNominee");
        txtGArea.setName("txtGArea");
        txtGPinCode.setName("txtGPinCode");
        txtGStreet.setName("txtGStreet");
        txtGuardianACodeNO.setName("txtGuardianACodeNO");
        txtGuardianNameNO.setName("txtGuardianNameNO");
        txtGuardianPhoneNO.setName("txtGuardianPhoneNO");
        txtMinNominees.setName("txtMinNominees");
        txtNArea.setName("txtNArea");
        txtNPinCode.setName("txtNPinCode");
        txtNStreet.setName("txtNStreet");
        txtNomineeACodeNO.setName("txtNomineeACodeNO");
        txtNomineeNameNO.setName("txtNomineeNameNO");
        txtNomineePhoneNO.setName("txtNomineePhoneNO");
        txtNomineeShareNO.setName("txtNomineeShareNO");
        txtTotalShareNO.setName("txtTotalShareNO");
        
        lblMaxNominee.setName("lblMaxNominee");
        txtMaxNominee.setName("txtMaxNominee");
        
        btnCust.setName("btnCust");
        lblCustNo.setName("lblCustNo");
        lblCustomer.setName("lblCustomer");
    }
    
    
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        lblNState.setText(resourceBundle.getString("lblNState"));
        lblNStreet.setText(resourceBundle.getString("lblNStreet"));
        lblNPinCode.setText(resourceBundle.getString("lblNPinCode"));
        lblNomineeShareNO.setText(resourceBundle.getString("lblNomineeShareNO"));
        ((javax.swing.border.TitledBorder)panNomineeDetails.getBorder()).setTitle(resourceBundle.getString("panNomineeDetails"));
        lblGStreet.setText(resourceBundle.getString("lblGStreet"));
        lblGCity.setText(resourceBundle.getString("lblGCity"));
        lblTotalShareNO.setText(resourceBundle.getString("lblTotalShareNO"));
        lblMinorDOBNO.setText(resourceBundle.getString("lblMinorDOBNO"));
        btnAddNO.setText(resourceBundle.getString("btnAddNO"));
        lblNArea.setText(resourceBundle.getString("lblNArea"));
        lblGState.setText(resourceBundle.getString("lblGState"));
        lblGuardianPhoneNO.setText(resourceBundle.getString("lblGuardianPhoneNO"));
        lblGCountry.setText(resourceBundle.getString("lblGCountry"));
        lblNCountry.setText(resourceBundle.getString("lblNCountry"));
        btnNewNO.setText(resourceBundle.getString("btnNewNO"));
        lblGArea.setText(resourceBundle.getString("lblGArea"));
        ((javax.swing.border.TitledBorder)panNomineeList.getBorder()).setTitle(resourceBundle.getString("panNomineeList"));
        lblRelationNO.setText(resourceBundle.getString("lblRelationNO"));
        lblGuardianNameNO.setText(resourceBundle.getString("lblGuardianNameNO"));
        lblGPinCode.setText(resourceBundle.getString("lblGPinCode"));
        btnRemoveNO.setText(resourceBundle.getString("btnRemoveNO"));
        lblNomineeStatusNO.setText(resourceBundle.getString("lblNomineeStatusNO"));
        lblNomineeRelationNO.setText(resourceBundle.getString("lblNomineeRelationNO"));
        lblNomineeNameNO.setText(resourceBundle.getString("lblNomineeNameNO"));
        lblMinNominees.setText(resourceBundle.getString("lblMinNominees"));
        ((javax.swing.border.TitledBorder)panGuardianDetails.getBorder()).setTitle(resourceBundle.getString("panGuardianDetails"));
        lblNCity.setText(resourceBundle.getString("lblNCity"));
        lblNomineePhoneNO.setText(resourceBundle.getString("lblNomineePhoneNO"));
        
        lblMaxNominee.setText(resourceBundle.getString("lblMaxNominee"));
        lblCustNo.setText(resourceBundle.getString("lblCustNo"));
        lblCustomer.setText(resourceBundle.getString("lblCustomer"));
    }
    
/* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        if (observable.getTxtMaxNominees() == 0) {
            txtMaxNominee.setText(CommonUtil.convertObjToStr(DEFAULT_NOMINEE));
        } else {
            txtMaxNominee.setText(CommonUtil.convertObjToStr(observable.getTxtMaxNominees()));//added By Chithra on 17-01-14
        }
        txtNomineeNameNO.setText(observable.getTxtNomineeNameNO());
        txtNomineeACodeNO.setText(observable.getTxtNomineeACodeNO());
        txtNomineePhoneNO.setText(observable.getTxtNomineePhoneNO());
        txtNomineeShareNO.setText(observable.getTxtNomineeShareNO());
        //        txtMinNominees.setText(observable.getTxtMinNominees());
        txtNStreet.setText(observable.getTxtNStreet());
        txtNArea.setText(observable.getTxtNArea());
        cboNCountry.setSelectedItem(observable.getCboNCountry());
        cboNState.setSelectedItem(observable.getCboNState());
        cboNCity.setSelectedItem(observable.getCboNCity());
        txtNPinCode.setText(observable.getTxtNPinCode());
        cboRelationNO.setSelectedItem(observable.getCboRelationNO());
        txtGuardianNameNO.setText(observable.getTxtGuardianNameNO());
        txtGuardianACodeNO.setText(observable.getTxtGuardianACodeNO());
        txtGuardianPhoneNO.setText(observable.getTxtGuardianPhoneNO());
        txtGStreet.setText(observable.getTxtGStreet());
        txtGArea.setText(observable.getTxtGArea());
        cboGCountry.setSelectedItem(observable.getCboGCountry());
        cboGState.setSelectedItem(observable.getCboGState());
        cboGCity.setSelectedItem(observable.getCboGCity());
        txtGPinCode.setText(observable.getTxtGPinCode());
        lblCustNo.setText(observable.getLblCustNo());
        tdtMinorDOBNO.setDateValue(observable.getTdtMinorDOBNO());
        cboNomineeRelationNO.setSelectedItem(observable.getCboNomineeRelationNO());
        /**
         * To Set the Table Model for the Nominee Table...
         */
        tblListNominee.setModel(observable.getTblNominee());
        txtTotalShareNO.setText(String.valueOf(observable.nomineeShare()));
        cboNomineeStatus.setSelectedItem(observable.getCboNomineeStatus());
    }
    
    public void disableMaxAndMinNom(){
        txtMaxNominee.setEnabled(false);
        txtMinNominees.setEnabled(false);
    }
    
    private void updateGuardian() {
        tdtMinorDOBNO.setDateValue(observable.getTdtMinorDOBNO());
        cboRelationNO.setSelectedItem(observable.getCboRelationNO());
        txtGuardianNameNO.setText(observable.getTxtGuardianNameNO());
        txtGuardianACodeNO.setText(observable.getTxtGuardianACodeNO());
        txtGuardianPhoneNO.setText(observable.getTxtGuardianPhoneNO());
        txtGStreet.setText(observable.getTxtGStreet());
        txtGArea.setText(observable.getTxtGArea());
        cboGCountry.setSelectedItem(observable.getCboGCountry());
        cboGState.setSelectedItem(observable.getCboGState());
        cboGCity.setSelectedItem(observable.getCboGCity());
        txtGPinCode.setText(observable.getTxtGPinCode());
    }
    
    
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setTxtMaxNominees(CommonUtil.convertObjToInt(txtMaxNominee.getText()));
        observable.setTxtNomineeNameNO(txtNomineeNameNO.getText());
        observable.setCboNomineeStatus((String) cboNomineeStatus.getSelectedItem());
        observable.setTxtNomineeACodeNO(txtNomineeACodeNO.getText());
        observable.setTxtNomineePhoneNO(txtNomineePhoneNO.getText());
        observable.setTxtNomineeShareNO(txtNomineeShareNO.getText());
        observable.setTxtMinNominees(txtMinNominees.getText());
        observable.setTxtNStreet(txtNStreet.getText());
        observable.setTxtNArea(txtNArea.getText());
        observable.setCboNCountry((String) cboNCountry.getSelectedItem());
        observable.setCboNState((String) cboNState.getSelectedItem());
        observable.setCboNCity((String) cboNCity.getSelectedItem());
        observable.setTxtNPinCode(txtNPinCode.getText());
        observable.setCboRelationNO((String) cboRelationNO.getSelectedItem());
        observable.setTxtGuardianNameNO(txtGuardianNameNO.getText());
        observable.setTxtGuardianACodeNO(txtGuardianACodeNO.getText());
        observable.setTxtGuardianPhoneNO(txtGuardianPhoneNO.getText());
        observable.setTxtGStreet(txtGStreet.getText());
        observable.setTxtGArea(txtGArea.getText());
        observable.setCboGCountry((String) cboGCountry.getSelectedItem());
        observable.setCboGState((String) cboGState.getSelectedItem());
        observable.setCboGCity((String) cboGCity.getSelectedItem());
        observable.setTxtGPinCode(txtGPinCode.getText());
        //        observable.setTxtTotalShareNO(txtTotalShareNO.getText());
        
        observable.setLblCustNo(lblCustNo.getText());
        
        observable.setTdtMinorDOBNO(tdtMinorDOBNO.getDateValue());
        observable.setCboNomineeRelationNO((String) cboNomineeRelationNO.getSelectedItem());
        observable.setTblNominee((com.see.truetransact.clientutil.EnhancedTableModel)tblListNominee.getModel());
    }
    
    
/* Auto Generated Method - setMandatoryHashMap()
 
ADD: implements com.see.truetransact.uimandatory.UIMandatoryField
 
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("cboNomineeStatus", new Boolean(true));
        mandatoryMap.put("txtNomineeNameNO", new Boolean(true));
        mandatoryMap.put("cboNomineeRelationNO", new Boolean(true));
        mandatoryMap.put("txtNomineeACodeNO", new Boolean(true));
        mandatoryMap.put("txtNomineePhoneNO", new Boolean(true));
        mandatoryMap.put("txtNomineeShareNO", new Boolean(true));
        mandatoryMap.put("txtMinNominees", new Boolean(true));
        mandatoryMap.put("txtNStreet", new Boolean(true));
        mandatoryMap.put("txtNArea", new Boolean(true));
        mandatoryMap.put("cboNCountry", new Boolean(true));
        mandatoryMap.put("cboNState", new Boolean(true));
        mandatoryMap.put("cboNCity", new Boolean(true));
        mandatoryMap.put("txtNPinCode", new Boolean(true));
        mandatoryMap.put("tdtMinorDOBNO", new Boolean(true));
        mandatoryMap.put("cboRelationNO", new Boolean(true));
        mandatoryMap.put("txtGuardianNameNO", new Boolean(true));
        mandatoryMap.put("txtGuardianACodeNO", new Boolean(true));
        mandatoryMap.put("txtGuardianPhoneNO", new Boolean(true));
        mandatoryMap.put("txtGStreet", new Boolean(true));
        mandatoryMap.put("txtGArea", new Boolean(true));
        mandatoryMap.put("cboGCountry", new Boolean(true));
        mandatoryMap.put("cboGState", new Boolean(true));
        mandatoryMap.put("cboGCity", new Boolean(true));
        mandatoryMap.put("txtGPinCode", new Boolean(true));
        mandatoryMap.put("txtTotalShareNO", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    // To fill the Data into the Combo Boxes...
    // it invokes the Combo Box model defined in OB class...
    private void initComponentData() {
        cboGCity.setModel(observable.getCbmGCity());
        cboGCountry.setModel(observable.getCbmGCountry());
        cboGState.setModel(observable.getCbmGState());
        
        cboNCity.setModel(observable.getCbmNCity());
        cboNCountry.setModel(observable.getCbmNCountry());
        cboNState.setModel(observable.getCbmNState());
        
        cboNomineeRelationNO.setModel(observable.getCbmNomineeRelationNO());
        cboRelationNO.setModel(observable.getCbmRelationNO());
        cboNomineeStatus.setModel(observable.getCbmNomineeStatus());
        tblListNominee.setModel(observable.getTblNominee());
    }
    
    private void setMaxLenths() {
        txtNomineeNameNO.setMaxLength(64);
        txtNStreet.setMaxLength(256);
        txtNArea.setMaxLength(128);
        txtNomineeACodeNO.setMaxLength(8);
        txtNomineeACodeNO.setValidation(new NumericValidation());
        txtNomineePhoneNO.setMaxLength(10); //__ 16
        txtNomineePhoneNO.setValidation(new NumericValidation());
        //        txtNomineeShareNO.setMaxLength(10);
        //        txtNomineeShareNO.setValidation(new NumericValidation());
        txtNomineeShareNO.setValidation(new PercentageValidation());
        txtNPinCode.setValidation(new PincodeValidation_IN());
        
        txtGuardianNameNO.setMaxLength(64);
        txtGStreet.setMaxLength(256);
        txtGArea.setMaxLength(128);
        txtGuardianACodeNO.setMaxLength(8);
        txtGuardianACodeNO.setValidation(new NumericValidation());
        txtGuardianPhoneNO.setMaxLength(10); //__ 16
        txtGuardianPhoneNO.setValidation(new NumericValidation());
        txtGPinCode.setValidation(new PincodeValidation_IN());
    }
    
    public void setHelpMessage() {
        //        NomineeMRB objMandatoryRB = new NomineeMRB();
        //        cboGCountry.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGCountry"));
        //        tdtMinorDOBNO.setHelpMessage(lblMsg, objMandatoryRB.getString("tdtMinorDOBNO"));
        //        txtNomineeNameNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeNameNO"));
        //        txtGuardianPhoneNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianPhoneNO"));
        //        txtGPinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGPinCode"));
        //        txtGArea.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGArea"));
        //        txtTotalShareNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTotalShareNO"));
        //        txtNomineeACodeNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeACodeNO"));
        //        txtNStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNStreet"));
        //        cboNomineeRelationNO.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNomineeRelationNO"));
        //        cboGState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGState"));
        //        txtNArea.setHelpMessage(lblMsg, objMandatoryRB.getString("cboBranctxtNAreahCode"));
        //        txtGuardianACodeNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGuardianACodeNO"));
        //        cboNState.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNState"));
        //        txtMinNominees.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMinNominees"));
        //
        //        cboRelationNO.setHelpMessage(lblMsg, objMandatoryRB.getString("cboRelationNO"));
        //        txtGStreet.setHelpMessage(lblMsg, objMandatoryRB.getString("txtGStreet"));
        //        txtNomineePhoneNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineePhoneNO"));
        //        txtNPinCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNPinCode"));
        //        rdoStatus_MinorNO.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoStatus_MinorNO"));
        //        txtNomineeShareNO.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNomineeShareNO"));
        //        cboNCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboNCity"));
        //        cboGCity.setHelpMessage(lblMsg, objMandatoryRB.getString("cboGCity"));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panNominee = new com.see.truetransact.uicomponent.CPanel();
        panNomineeInfo = new com.see.truetransact.uicomponent.CPanel();
        panNomineeDetails = new com.see.truetransact.uicomponent.CPanel();
        panNomineedata = new com.see.truetransact.uicomponent.CPanel();
        lblNomineeNameNO = new com.see.truetransact.uicomponent.CLabel();
        txtNomineeNameNO = new com.see.truetransact.uicomponent.CTextField();
        lblNomineeRelationNO = new com.see.truetransact.uicomponent.CLabel();
        cboNomineeRelationNO = new com.see.truetransact.uicomponent.CComboBox();
        lblNomineePhoneNO = new com.see.truetransact.uicomponent.CLabel();
        phoneNPanelNO = new com.see.truetransact.uicomponent.CPanel();
        txtNomineeACodeNO = new com.see.truetransact.uicomponent.CTextField();
        txtNomineePhoneNO = new com.see.truetransact.uicomponent.CTextField();
        lblNomineeStatusNO = new com.see.truetransact.uicomponent.CLabel();
        lblNomineeShareNO = new com.see.truetransact.uicomponent.CLabel();
        txtNomineeShareNO = new com.see.truetransact.uicomponent.CTextField();
        lblMinNominees = new com.see.truetransact.uicomponent.CLabel();
        txtMinNominees = new com.see.truetransact.uicomponent.CTextField();
        lblMaxNominee = new com.see.truetransact.uicomponent.CLabel();
        txtMaxNominee = new com.see.truetransact.uicomponent.CTextField();
        btnCust = new com.see.truetransact.uicomponent.CButton();
        lblCustNo = new com.see.truetransact.uicomponent.CLabel();
        lblCustomer = new com.see.truetransact.uicomponent.CLabel();
        cboNomineeStatus = new com.see.truetransact.uicomponent.CComboBox();
        sptNominee = new com.see.truetransact.uicomponent.CSeparator();
        panNomineeAddr = new com.see.truetransact.uicomponent.CPanel();
        lblNStreet = new com.see.truetransact.uicomponent.CLabel();
        txtNStreet = new com.see.truetransact.uicomponent.CTextField();
        lblNArea = new com.see.truetransact.uicomponent.CLabel();
        txtNArea = new com.see.truetransact.uicomponent.CTextField();
        lblNCountry = new com.see.truetransact.uicomponent.CLabel();
        cboNCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblNState = new com.see.truetransact.uicomponent.CLabel();
        cboNState = new com.see.truetransact.uicomponent.CComboBox();
        lblNCity = new com.see.truetransact.uicomponent.CLabel();
        cboNCity = new com.see.truetransact.uicomponent.CComboBox();
        lblNPinCode = new com.see.truetransact.uicomponent.CLabel();
        txtNPinCode = new com.see.truetransact.uicomponent.CTextField();
        panGuardianDetails = new com.see.truetransact.uicomponent.CPanel();
        panGuardData = new com.see.truetransact.uicomponent.CPanel();
        lblMinorDOBNO = new com.see.truetransact.uicomponent.CLabel();
        tdtMinorDOBNO = new com.see.truetransact.uicomponent.CDateField();
        lblRelationNO = new com.see.truetransact.uicomponent.CLabel();
        cboRelationNO = new com.see.truetransact.uicomponent.CComboBox();
        lblGuardianNameNO = new com.see.truetransact.uicomponent.CLabel();
        txtGuardianNameNO = new com.see.truetransact.uicomponent.CTextField();
        lblGuardianPhoneNO = new com.see.truetransact.uicomponent.CLabel();
        phoneGPanelNO = new com.see.truetransact.uicomponent.CPanel();
        txtGuardianACodeNO = new com.see.truetransact.uicomponent.CTextField();
        txtGuardianPhoneNO = new com.see.truetransact.uicomponent.CTextField();
        sptGuard = new com.see.truetransact.uicomponent.CSeparator();
        panGuardAddr = new com.see.truetransact.uicomponent.CPanel();
        lblGStreet = new com.see.truetransact.uicomponent.CLabel();
        txtGStreet = new com.see.truetransact.uicomponent.CTextField();
        lblGArea = new com.see.truetransact.uicomponent.CLabel();
        txtGArea = new com.see.truetransact.uicomponent.CTextField();
        lblGCountry = new com.see.truetransact.uicomponent.CLabel();
        cboGCountry = new com.see.truetransact.uicomponent.CComboBox();
        lblGState = new com.see.truetransact.uicomponent.CLabel();
        cboGState = new com.see.truetransact.uicomponent.CComboBox();
        lblGCity = new com.see.truetransact.uicomponent.CLabel();
        cboGCity = new com.see.truetransact.uicomponent.CComboBox();
        lblGPinCode = new com.see.truetransact.uicomponent.CLabel();
        txtGPinCode = new com.see.truetransact.uicomponent.CTextField();
        panNomineeList = new com.see.truetransact.uicomponent.CPanel();
        srpListNomineeNO = new com.see.truetransact.uicomponent.CScrollPane();
        tblListNominee = new com.see.truetransact.uicomponent.CTable();
        panShareAddEditRemoveNO = new com.see.truetransact.uicomponent.CPanel();
        lblTotalShareNO = new com.see.truetransact.uicomponent.CLabel();
        sepSepNOP4 = new com.see.truetransact.uicomponent.CSeparator();
        btnAddNO = new com.see.truetransact.uicomponent.CButton();
        btnRemoveNO = new com.see.truetransact.uicomponent.CButton();
        btnNewNO = new com.see.truetransact.uicomponent.CButton();
        txtTotalShareNO = new com.see.truetransact.uicomponent.CTextField();

        panNominee.setLayout(new java.awt.GridBagLayout());

        panNomineeInfo.setLayout(new java.awt.GridBagLayout());

        panNomineeDetails.setLayout(new java.awt.GridBagLayout());

        panNomineeDetails.setBorder(new javax.swing.border.TitledBorder("Nominee Details"));
        panNomineedata.setLayout(new java.awt.GridBagLayout());

        lblNomineeNameNO.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblNomineeNameNO, gridBagConstraints);

        txtNomineeNameNO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(txtNomineeNameNO, gridBagConstraints);

        lblNomineeRelationNO.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblNomineeRelationNO, gridBagConstraints);

        cboNomineeRelationNO.setMinimumSize(new java.awt.Dimension(100, 21));
        cboNomineeRelationNO.setPopupWidth(150);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(cboNomineeRelationNO, gridBagConstraints);

        lblNomineePhoneNO.setText("Phone No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblNomineePhoneNO, gridBagConstraints);

        phoneNPanelNO.setLayout(new java.awt.GridBagLayout());

        txtNomineeACodeNO.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNomineeACodeNO.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        phoneNPanelNO.add(txtNomineeACodeNO, gridBagConstraints);

        txtNomineePhoneNO.setMaxLength(12);
        txtNomineePhoneNO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        phoneNPanelNO.add(txtNomineePhoneNO, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNomineedata.add(phoneNPanelNO, gridBagConstraints);

        lblNomineeStatusNO.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblNomineeStatusNO, gridBagConstraints);

        lblNomineeShareNO.setText("Share (%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblNomineeShareNO, gridBagConstraints);

        txtNomineeShareNO.setMinimumSize(new java.awt.Dimension(50, 21));
        txtNomineeShareNO.setPreferredSize(new java.awt.Dimension(50, 21));
        txtNomineeShareNO.setValidation(new com.see.truetransact.uivalidation.NumericValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(txtNomineeShareNO, gridBagConstraints);

        lblMinNominees.setText("Min Nominees Reqd.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblMinNominees, gridBagConstraints);

        txtMinNominees.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMinNominees.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(txtMinNominees, gridBagConstraints);

        lblMaxNominee.setText("Max Nominees Allowed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblMaxNominee, gridBagConstraints);

        txtMaxNominee.setMinimumSize(new java.awt.Dimension(50, 21));
        txtMaxNominee.setPreferredSize(new java.awt.Dimension(50, 21));
        txtMaxNominee.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtMaxNomineeFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(txtMaxNominee, gridBagConstraints);

        btnCust.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCust.setToolTipText("Customer Data");
        btnCust.setMinimumSize(new java.awt.Dimension(25, 25));
        btnCust.setPreferredSize(new java.awt.Dimension(25, 25));
        btnCust.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panNomineedata.add(btnCust, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblCustNo, gridBagConstraints);

        lblCustomer.setText("Customer ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(lblCustomer, gridBagConstraints);

        cboNomineeStatus.setMinimumSize(new java.awt.Dimension(100, 21));
        cboNomineeStatus.setPopupWidth(125);
        cboNomineeStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNomineeStatusActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineedata.add(cboNomineeStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeDetails.add(panNomineedata, gridBagConstraints);

        sptNominee.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeDetails.add(sptNominee, gridBagConstraints);

        panNomineeAddr.setLayout(new java.awt.GridBagLayout());

        lblNStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(lblNStreet, gridBagConstraints);

        txtNStreet.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(txtNStreet, gridBagConstraints);

        lblNArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(lblNArea, gridBagConstraints);

        txtNArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(txtNArea, gridBagConstraints);

        lblNCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(lblNCountry, gridBagConstraints);

        cboNCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(cboNCountry, gridBagConstraints);

        lblNState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(lblNState, gridBagConstraints);

        cboNState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(cboNState, gridBagConstraints);

        lblNCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(lblNCity, gridBagConstraints);

        cboNCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(cboNCity, gridBagConstraints);

        lblNPinCode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(lblNPinCode, gridBagConstraints);

        txtNPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeAddr.add(txtNPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panNomineeDetails.add(panNomineeAddr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        panNomineeInfo.add(panNomineeDetails, gridBagConstraints);

        panGuardianDetails.setLayout(new java.awt.GridBagLayout());

        panGuardianDetails.setBorder(new javax.swing.border.TitledBorder("In case of Minor"));
        panGuardData.setLayout(new java.awt.GridBagLayout());

        lblMinorDOBNO.setText("Nominee DOB");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblMinorDOBNO, gridBagConstraints);

        tdtMinorDOBNO.setMinimumSize(new java.awt.Dimension(100, 21));
        tdtMinorDOBNO.setPreferredSize(new java.awt.Dimension(100, 21));
        tdtMinorDOBNO.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tdtMinorDOBNOFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(tdtMinorDOBNO, gridBagConstraints);

        lblRelationNO.setText("Relationship");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblRelationNO, gridBagConstraints);

        cboRelationNO.setEditable(true);
        cboRelationNO.setMinimumSize(new java.awt.Dimension(100, 21));
        cboRelationNO.setPopupWidth(125);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(cboRelationNO, gridBagConstraints);

        lblGuardianNameNO.setText("Gurdian's Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblGuardianNameNO, gridBagConstraints);

        txtGuardianNameNO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(txtGuardianNameNO, gridBagConstraints);

        lblGuardianPhoneNO.setText("Phone No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardData.add(lblGuardianPhoneNO, gridBagConstraints);

        phoneGPanelNO.setLayout(new java.awt.GridBagLayout());

        txtGuardianACodeNO.setMinimumSize(new java.awt.Dimension(50, 21));
        txtGuardianACodeNO.setPreferredSize(new java.awt.Dimension(50, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 2);
        phoneGPanelNO.add(txtGuardianACodeNO, gridBagConstraints);

        txtGuardianPhoneNO.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        phoneGPanelNO.add(txtGuardianPhoneNO, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panGuardData.add(phoneGPanelNO, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardianDetails.add(panGuardData, gridBagConstraints);

        sptGuard.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardianDetails.add(sptGuard, gridBagConstraints);

        panGuardAddr.setLayout(new java.awt.GridBagLayout());

        lblGStreet.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGStreet, gridBagConstraints);

        txtGStreet.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(txtGStreet, gridBagConstraints);

        lblGArea.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGArea, gridBagConstraints);

        txtGArea.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(txtGArea, gridBagConstraints);

        lblGCountry.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGCountry, gridBagConstraints);

        cboGCountry.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(cboGCountry, gridBagConstraints);

        lblGState.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGState, gridBagConstraints);

        cboGState.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(cboGState, gridBagConstraints);

        lblGCity.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGCity, gridBagConstraints);

        cboGCity.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(cboGCity, gridBagConstraints);

        lblGPinCode.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(lblGPinCode, gridBagConstraints);

        txtGPinCode.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardAddr.add(txtGPinCode, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panGuardianDetails.add(panGuardAddr, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.5;
        panNomineeInfo.add(panGuardianDetails, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.7;
        gridBagConstraints.weighty = 1.0;
        panNominee.add(panNomineeInfo, gridBagConstraints);

        panNomineeList.setLayout(new java.awt.GridBagLayout());

        panNomineeList.setBorder(new javax.swing.border.TitledBorder("List of Nominees"));
        tblListNominee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "", ""
            }
        ));
        tblListNominee.setPreferredScrollableViewportSize(new java.awt.Dimension(150, 64));
        tblListNominee.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblListNominee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblListNomineeMousePressed(evt);
            }
        });

        srpListNomineeNO.setViewportView(tblListNominee);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.9;
        panNomineeList.add(srpListNomineeNO, gridBagConstraints);

        panShareAddEditRemoveNO.setLayout(new java.awt.GridBagLayout());

        lblTotalShareNO.setText("Total Share (%)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareAddEditRemoveNO.add(lblTotalShareNO, gridBagConstraints);

        sepSepNOP4.setBorder(new javax.swing.border.EtchedBorder());
        sepSepNOP4.setMinimumSize(new java.awt.Dimension(5, 5));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareAddEditRemoveNO.add(sepSepNOP4, gridBagConstraints);

        btnAddNO.setText("Save");
        btnAddNO.setMaximumSize(new java.awt.Dimension(70, 30));
        btnAddNO.setMinimumSize(new java.awt.Dimension(70, 26));
        btnAddNO.setPreferredSize(new java.awt.Dimension(70, 28));
        btnAddNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNOActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareAddEditRemoveNO.add(btnAddNO, gridBagConstraints);

        btnRemoveNO.setText("Delete");
        btnRemoveNO.setMaximumSize(new java.awt.Dimension(70, 30));
        btnRemoveNO.setPreferredSize(new java.awt.Dimension(70, 28));
        btnRemoveNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveNOActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareAddEditRemoveNO.add(btnRemoveNO, gridBagConstraints);

        btnNewNO.setText("New");
        btnNewNO.setMaximumSize(new java.awt.Dimension(70, 30));
        btnNewNO.setMinimumSize(new java.awt.Dimension(70, 26));
        btnNewNO.setPreferredSize(new java.awt.Dimension(70, 28));
        btnNewNO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewNOActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panShareAddEditRemoveNO.add(btnNewNO, gridBagConstraints);

        txtTotalShareNO.setMinimumSize(new java.awt.Dimension(100, 21));
        panShareAddEditRemoveNO.add(txtTotalShareNO, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.1;
        panNomineeList.add(panShareAddEditRemoveNO, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        gridBagConstraints.weighty = 1.0;
        panNominee.add(panNomineeList, gridBagConstraints);

        add(panNominee);

    }//GEN-END:initComponents

    private void txtMaxNomineeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMaxNomineeFocusLost
        // TODO add your handling code here:
        if(screen.equals("TD"))
            setMaxNominee(CommonUtil.convertObjToInt(txtMaxNominee.getText()));
    }//GEN-LAST:event_txtMaxNomineeFocusLost

    private void cboNomineeStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNomineeStatusActionPerformed
        String oldValue = observable.getCboNomineeStatus();
        updateOBFields();
//        System.out.println("A4cboNomineeStatus:" + cboNomineeStatus.getSelectedItem());
//        System.out.println("A4observable.cboNomineeStatus:" + observable.getCboNomineeStatus());
//        System.out.println("A4observable.getCbmNomineeStatus().getKeyForSelected():" + observable.getCbmNomineeStatus().getKeyForSelected());
        
        //--- If value selected, then continue
        if(cboNomineeStatus.getSelectedIndex()>0){
            //--- If major is selected, disable the Guardian's Details
            if(CommonUtil.convertObjToStr(observable.getCbmNomineeStatus().getKeyForSelected()).equals("0")){
                    observable.resetGuardian();
                    ClientUtil.enableDisable(panGuardianDetails, false);
                    updateGuardian();
            //--- Else If Major NOT selected, and if the relation is Father, Mother or spouse, then reset nominee
            } else if((cboNomineeRelationNO.getSelectedItem().equals("Father") || cboNomineeRelationNO.getSelectedItem().equals("Mother") || cboNomineeRelationNO.getSelectedItem().equals("Spouse")) && !(CommonUtil.convertObjToStr(observable.getCbmNomineeStatus().getKeyForSelected()).equals("0"))) {
                observable.setCboNomineeStatus("");
                cboNomineeStatus.setSelectedItem(observable.getCboNomineeStatus());
            } else {
            //--- else enable guardian's details
                 ClientUtil.enableDisable(panGuardianDetails, true);
            }
        } else {
            observable.setCboNomineeStatus(oldValue);
            cboNomineeStatus.setSelectedItem(observable.getCboNomineeStatus());
        }
    }//GEN-LAST:event_cboNomineeStatusActionPerformed

    private void tdtMinorDOBNOFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tdtMinorDOBNOFocusLost
        ClientUtil.validateMinorAndMinor_Under_Wards(tdtMinorDOBNO, CommonUtil.convertObjToStr(observable.getCbmNomineeStatus().getKeyForSelected()));
    }//GEN-LAST:event_tdtMinorDOBNOFocusLost

   
//    private void rdoStatus_MajorNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStatus_MajorNOActionPerformed
//    }//GEN-LAST:event_rdoStatus_MajorNOActionPerformed
//    private void rdoStatus_MinorNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoStatus_MinorNOActionPerformed
//    }//GEN-LAST:event_rdoStatus_MinorNOActionPerformed


    
    private void btnCustActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustActionPerformed
        // TODO add your handling code here:
        if(getMainCustomerId() == null || getMainCustomerId().trim().equals("")){
            displayAlert("Main Customer is not selected.");
        }else{
            popUp();
        }
    }//GEN-LAST:event_btnCustActionPerformed
    private void popUp(){
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        viewMap.put(CommonConstants.MAP_NAME, "Nominee.getCustData");
        whereMap.put("CUST_ID", getMainCustomerId());
        //--- To pass the Joint Account holders (JK)
        if(getCustomerList() != null && getCustomerList().length()>0){
            whereMap.put("CUSTOMER_ID", getCustomerList());
        }
        System.out.println("getCustomerList : " + getCustomerList());
        whereMap.put("BRANCH_ID", TrueTransactMain.BRANCH_ID);
        
        System.out.println("getMainCustomerId : " + getMainCustomerId());
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        whereMap = null ;
        new ViewAll(this, resourceBundle.getString("title"), viewMap).show();
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("hash:" + hash);
        txtNomineeNameNO.setText(CommonUtil.convertObjToStr(hash.get("NAME")));
        lblCustNo.setText(CommonUtil.convertObjToStr(hash.get("CUSTOMER ID")));
        cboNomineeStatus.setSelectedItem(CommonUtil.convertObjToStr(hash.get("STATUS")));
        cboNomineeStatus.setEnabled(false);            // This line added by Rajesh.
        cboNomineeStatusActionPerformed(null);         // This line added by Rajesh.
        final HashMap dataMap = observable.getCustAddr(hash);
        //        System.out.println("dataMap: " + dataMap);
        //        if(dataMap.containsKey("STREET")){
        txtNomineeACodeNO.setText(CommonUtil.convertObjToStr(dataMap.get("AREA_CODE")));
        txtNomineePhoneNO.setText(CommonUtil.convertObjToStr(dataMap.get("PHONE_NUMBER")));
        txtNStreet.setText(CommonUtil.convertObjToStr(dataMap.get("STREET")));
        txtNArea.setText(CommonUtil.convertObjToStr(dataMap.get("AREA")));
        cboNCountry.setSelectedItem(((ComboBoxModel) cboNCountry.getModel()).getDataForKey(CommonUtil.convertObjToStr(dataMap.get("COUNTRY_CODE"))));
        cboNState.setSelectedItem(((ComboBoxModel) cboNState.getModel()).getDataForKey(CommonUtil.convertObjToStr(dataMap.get("STATE"))));
        cboNCity.setSelectedItem(((ComboBoxModel) cboNCity.getModel()).getDataForKey(CommonUtil.convertObjToStr(dataMap.get("CITY"))));
        txtNPinCode.setText(CommonUtil.convertObjToStr(dataMap.get("PIN_CODE")));
       
        //--- set Guardian details if nominee is minor
        if(!CommonUtil.convertObjToStr(hash.get("STATUS")).equals("Major")){
            final HashMap guardianDetails = observable.getGuardianDetails(hash);
            //--- set nominee status to WARDS, if CareOf in Customer is Court Administrator
            if(CommonUtil.convertObjToStr(guardianDetails.get("CARE_OF")).equals("COURT")){
                cboNomineeStatus.setSelectedItem(((ComboBoxModel) cboNomineeStatus.getModel()).getDataForKey("21"));
            }
            tdtMinorDOBNO.setDateValue(CommonUtil.convertObjToStr(guardianDetails.get("DOB")));
            cboRelationNO.setSelectedItem(((ComboBoxModel) cboRelationNO.getModel()).getDataForKey(CommonUtil.convertObjToStr(guardianDetails.get("RELATIONSHIP"))));
            txtGuardianNameNO.setText(CommonUtil.convertObjToStr(guardianDetails.get("GUARDIAN_NAME")));
            txtGuardianACodeNO.setText(CommonUtil.convertObjToStr(guardianDetails.get("AREA_CODE")));
            txtGuardianPhoneNO.setText(CommonUtil.convertObjToStr(guardianDetails.get("PH_NO")));
            txtGStreet.setText(CommonUtil.convertObjToStr(guardianDetails.get("STREET")));
            txtGArea.setText(CommonUtil.convertObjToStr(guardianDetails.get("AREA")));
            cboGCountry.setSelectedItem(((ComboBoxModel) cboGCountry.getModel()).getDataForKey(CommonUtil.convertObjToStr(guardianDetails.get("COUNTRY_CODE"))));
            cboGState.setSelectedItem(((ComboBoxModel) cboGState.getModel()).getDataForKey(CommonUtil.convertObjToStr(guardianDetails.get("STATE"))));
            cboGCity.setSelectedItem(((ComboBoxModel) cboGCity.getModel()).getDataForKey(CommonUtil.convertObjToStr(guardianDetails.get("CITY"))));
            txtGPinCode.setText(CommonUtil.convertObjToStr(guardianDetails.get("PIN_CODE")));

        }
        //            System.out.println("STATUS: " + CommonUtil.convertObjToStr(dataMap.get("STATUS")));
        
//        if(CommonUtil.convertObjToStr(dataMap.get("STATUS")).equalsIgnoreCase("MINOR")){
//            setMajorMinorData(true);
//        }else{
//            setMajorMinorData(false);
//        }
        //        }
    }
    
    public String getLblStatus(){
        return lblStatus;
    }
    
    public void setViewType(String viewType){
        this.viewType = viewType;
    }
     public void setAuthInstEnableDisable(boolean val){
         ClientUtil.enableDisable(panNomineeDetails, val);
         ClientUtil.enableDisable(panGuardianDetails, val);
         ClientUtil.enableDisable( panNomineeAddr,val);
         ClientUtil.enableDisable(panGuardData, val);
     }
     
     
    private void tblListNomineeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblListNomineeMousePressed
        // TODO add your handling code here:
        updateOBFields();
//        observable.resetStatus();
        int selectedRow = tblListNominee.getSelectedRow();
        /**
         * To Populate the Data of the Row Selected...
         */
        observable.populateNomineeTab(selectedRow);

        //__ Incase of Authorization and Delete...
         if ((getLblStatus().equals(ClientConstants.ACTION_STATUS[3]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[7]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[8]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[10])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) || (viewType.equals(ClientConstants.ACTION_STATUS[3]))){
             ClientUtil.enableDisable(panNomineeDetails, false);
             ClientUtil.enableDisable(panGuardianDetails, false);
             ClientUtil.enableDisable( panNomineeAddr, false);
             ClientUtil.enableDisable(panGuardData, false);
             btnCust.setEnabled(false);
         }else{
             ClientUtil.enableDisable(panNomineeDetails, true);
             ClientUtil.enableDisable(panGuardianDetails, true);
             enableDisableNominee_New();
             btnCust.setEnabled(true);
             txtNomineeNameNO.setEnabled(false);
             
             txtMinNominees.setEnabled(false);
         }
        observable.ttNotifyObservers();
        updateValue = 1;
        rowNo = selectedRow;
        /**
         * To get the Share of the Nominee Selected...
         */
        share = observable.getSharedata(selectedRow);
        disableMaxAndMinNom();
        if(getActionType() == ClientConstants.ACTIONTYPE_VIEW ||
        getActionType() == ClientConstants.ACTIONTYPE_VIEW_MODE ||
        getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ||
        getActionType() == ClientConstants.ACTIONTYPE_REJECT ||
        getActionType() == ClientConstants.ACTIONTYPE_DELETE){
            ClientUtil.enableDisable(panNomineeDetails, false);
            ClientUtil.enableDisable(panGuardianDetails,false);
            btnNewNO.setEnabled(false);
            btnAddNO.setEnabled(false);
            btnRemoveNO.setEnabled(false);
            btnCust.setEnabled(false);
        }else{
            ClientUtil.enableDisable(panNomineeDetails, true);
            btnNewNO.setEnabled(true);
            btnAddNO.setEnabled(true);
            btnRemoveNO.setEnabled(true);
        }
         if(tblListNominee.getRowCount()>0 && getActionType() != ClientConstants.ACTIONTYPE_NEW){
            setMaxNominee(CommonUtil.convertObjToInt(tblListNominee.getRowCount()));
        }
//        txtMaxNominee.setEnabled(false);
        
    }//GEN-LAST:event_tblListNomineeMousePressed
            
    private void btnRemoveNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveNOActionPerformed
        // TODO add your handling code here:
        updateOBFields();
        observable.deleteNomineeTab(rowNo);
        /**
         * Reset all the fields and Disable them...
         */
        observable.resetNomineeFields();
        rowNo = -1;
        observable.ttNotifyObservers();
        enableDisableNominee_SaveDelete();
        ClientUtil.enableDisable(panNomineeDetails, false);
        ClientUtil.enableDisable(panGuardianDetails, false);
        txtTotalShareNO.setText(CommonUtil.convertObjToStr(String.valueOf(observable.nomineeShare())));
    }//GEN-LAST:event_btnRemoveNOActionPerformed
    
    private void btnAddNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNOActionPerformed
        // TODO add your handling code here
        updateOBFields();
        /**
         * If the Nominee is a minor, Enter the Details of the guardian(s)..
         */
        String mandatoryMessage = "";
        StringBuffer str = new StringBuffer();
//        if(rdoStatus_MinorNO.isSelected()==false && rdoStatus_MajorNO.isSelected()==false){
//            str.append(resourceBundle.getString("StatusWarning") + "\n");
//        }
        
        mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panNomineeDetails);
        if(mandatoryMessage.length() > 0){
            str.append(mandatoryMessage + "\n");
        }
        
//        if(!cboNomineeStatus.getSelectedItem().equals(CommonUtil.convertObjToStr("Major"))){
         if(!CommonUtil.convertObjToStr(observable.getCbmNomineeStatus().getKeyForSelected()).equals("0")){
            mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panGuardianDetails);
        }
        /**
         * To Check if all the Mandatory fields are entered...
         */
        if(mandatoryMessage.length() > 0 ){
            displayAlert(mandatoryMessage);
        }
        else{
            if(txtTotalShareNO != null){
                double activeShare = 0;
                if(updateValue!=1){
                    activeShare = CommonUtil.convertObjToDouble(txtNomineeShareNO.getText()).doubleValue()+ observable.nomineeShare();
                }else{
                    activeShare = observable.nomineeShare() + CommonUtil.convertObjToDouble(txtNomineeShareNO.getText()).doubleValue() - share;
                }
                /**
                 * The Total share of the Nominee(s) cannot exceed 100%...
                 */
                if( activeShare > 100){
                    mandatoryMessage = resourceBundle.getString("MoreWarning");
                }
            }
            if(mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            } else {
                observable.addNomineeData(updateValue, rowNo);
                observable.resetNomineeFields();
                //            ClientUtil.clearAll(panNomineeDetails);
                //            ClientUtil.clearAll(panGuardianDetails);
                System.out.println(" getCboNomineeRelationNO" +  observable.getCboNomineeRelationNO());
                enableDisableNominee_SaveDelete();
                observable.ttNotifyObservers();
                btnCust.setEnabled(false);
                ClientUtil.enableDisable(panNomineeDetails, false);
                ClientUtil.enableDisable(panGuardianDetails, false);
            }
        }
    }//GEN-LAST:event_btnAddNOActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    
    /**
     * To Validate the Data at the Time of Final Save...
     */
    public String validateData(){
        int rowCount;
        StringBuffer strBAlert = new StringBuffer();
        rowCount = observable.getRowCount();
        
        if((observable.getRowCount() > 0) && (CommonUtil.convertObjToDouble(txtTotalShareNO.getText()).doubleValue() < 100)){
            strBAlert.append(resourceBundle.getString("LessWarning") +"\n");
        }
        //        }
        
        //        if(maxAllowed){
        //__ If no value is entered for the MaxNominee, Set is value as the Default Value
//        if(txtMaxNominee.getText() == null || txtMaxNominee.getText().equals("")||txtMaxNominee.getText().equals("0")){
//             txtMaxNominee.setText(String.valueOf(DEFAULT_NOMINEE)); 
//             maxNominee= CommonUtil.convertObjToInt(txtMaxNominee.getText());
//        }
          if (observable.getTxtMaxNominees() == 0) {
//             txtMaxNominee.setText(String.valueOf(DEFAULT_NOMINEE)); 
//             maxNominee= CommonUtil.convertObjToInt(txtMaxNominee.getText());
            maxNominee = CommonUtil.convertObjToInt(DEFAULT_NOMINEE);
        } else {
            maxNominee = observable.getTxtMaxNominees();

        }
        if(observable.getTxtMaxNominees() !=0){
            if(rowCount > maxNominee){
                strBAlert.append(resourceBundle.getString("MoreRowWarning") +"\n");
            }
        }
        //        }
        
        return  strBAlert.toString();
    }
    
    private void btnNewNOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewNOActionPerformed
        // TODO add your handling code here:
        updateValue = -1;
        rowNo = 0;
        double shareSum = observable.nomineeShare();
        /**
         * If the total share of all the nominee(s) equals 100%,
         * cannot enter a new nominee...
         */
        int rowCount = observable.getRowCount();
        if(shareSum >= 100){
            displayAlert(resourceBundle.getString("MoreWarning"));
        }else if(rowCount >= maxNominee){
            displayAlert(resourceBundle.getString("MoreRowWarning"));
        }else{
            if(CommonUtil.convertObjToStr(txtMaxNominee.getText()).equalsIgnoreCase("")){
                setMaxNominee(DEFAULT_NOMINEE);
            }
            newButtonAction();
             observable.getCbmNomineeStatus().setKeyForSelected("0");
        }
    }//GEN-LAST:event_btnNewNOActionPerformed
    private void newButtonAction(){
        enableDisableNominee_New();
        ClientUtil.enableDisable(panNomineeDetails, true);
        btnCust.setEnabled(true);
        txtMinNominees.setEnabled(false);
       
        observable.resetNomineeFields();
        observable.ttNotifyObservers();
        if(screen.equals("TD")||screen.equals("OA")||screen.equals("TL")){//modified by chithra on 6-05-14
            txtMaxNominee.setEnabled(true);
            
        }else{
            txtMaxNominee.setEnabled(false);
        }
        //added by chithra on 6-05-14
        if(CommonUtil.convertObjToStr(txtMaxNominee.getText()).equalsIgnoreCase("")){
            setMaxNominee(DEFAULT_NOMINEE);
        }
      
        if(txtMaxNominee.getText().equalsIgnoreCase("1")){
           txtNomineeShareNO.setText("100"); 
           txtNomineeShareNO.setEditable(false);
           if(screen.equals("TD")||screen.equals("OA")||screen.equals("TL")){//modified by chithra on 6-05-14
            txtNomineeShareNO.setEditable(true);
           }
        }
    }
    
    /**
     * To Enable the Buttons when New Is pressed...
     */
    public void enableDisableNominee_New(){
        btnNewNO.setEnabled(true);
        btnAddNO.setEnabled(true);
        btnRemoveNO.setEnabled(true);
    }
    
    /**
     * To Disable the Buttons at the time of Save and/Or delete...
     */
    public void enableDisableNominee_SaveDelete(){
        btnNewNO.setEnabled(true);
        btnAddNO.setEnabled(false);
        btnRemoveNO.setEnabled(false);
        
        btnCust.setEnabled(false);
    }
    
    /**
     * To Enable and Disable the Buttons
     */
    public void setBtnEnableDisable(boolean val){
        btnNewNO.setEnabled(val);
        btnAddNO.setEnabled(val);
        btnRemoveNO.setEnabled(val);
    }
    
    /** returns the Nominee CTable Row count
     */
    public int getTblRowCount(){
        return tblListNominee.getRowCount();
    }
    
    /**
     * To reset the Data in the Table...
     */
    public void resetTable(){
        observable.resetNomineeTab();
    }
    
    /**
     * To reset the Data in the Table...
     */
    public void resetNomineeData(){
        observable.resetNomineeFields();
        observable.resetLists();
        enableDisableNominee_SaveDelete();
        txtMinNominees.setText("");
        txtTotalShareNO.setText("");
    }
    
    /**
     * To get the Maximum of the Deleted Nominee ID
     */
    public void callMaxDel(String actNum){
        observable.maxDelNominee(actNum);
    }
    
    /**
     * To set the Minimum No of Nominee(s)
     */
    public void setMinNominee(String nomNum){
//        txtMinNominees.setText(nomNum);
        this.maxNominee = CommonUtil.convertObjToInt(nomNum);
        txtMaxNominee.setText(nomNum);
    }
    
    /**
     * To get the Action Type for the Table data...
     */
    int actionType = -1;
    public void setActionType(int actionType){
        this.actionType = actionType;
    }
    
    public int getActionType(){
        return this.actionType;
    }
    
    /**
     * To Reset the Fields when Main New is Pressed...
     */
    public void resetNomineeTab(){
        ClientUtil.clearAll(panNomineeDetails);
        ClientUtil.clearAll(panGuardianDetails);
        ClientUtil.enableDisable(panNomineeDetails, false);
        ClientUtil.enableDisable(panGuardianDetails, false);
        enableDisableNominee_SaveDelete();
    }
    
    public void disableNewButton(boolean value){
        btnNewNO.setEnabled(value);
    }
    
    
    /**
     * Getter for property maxNominee.
     */
    public int getMaxNominee() {
        return maxNominee;
    }
    
    /**
     * Setter for property maxNominee.
     */
    public void setMaxNominee(int maxNominee) {
        this.maxNominee = maxNominee;
        txtMaxNominee.setText(String.valueOf(maxNominee));
    }
    
    /**
     * Getter for property mainCustomerId.
     * @return Value of property mainCustomerId.
     */
    public java.lang.String getMainCustomerId() {
        return mainCustomerId;
    }
    
    /**
     * Setter for property mainCustomerId.
     * @param mainCustomerId New value of property mainCustomerId.
     */
    public void setMainCustomerId(java.lang.String mainCustomerId) {
        this.mainCustomerId = mainCustomerId;
    }
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        javax.swing.JFrame jf = new javax.swing.JFrame();
        NomineeUI bui = new NomineeUI("",false);
        bui.setMainCustomerId("C0001089");
        jf.setSize(300,525);
        jf.getContentPane().add(bui);
        jf.show();
        bui.show();
    }
    
    /**
     * Getter for property customerList.
     * @return Value of property customerList.
     */
    public java.lang.StringBuffer getCustomerList() {
        return customerList;
    }    
    
    /**
     * Setter for property customerList.
     * @param customerList New value of property customerList.
     */
    public void setCustomerList(java.lang.StringBuffer customerList) {
        this.customerList = customerList;
    }    
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAddNO;
    private com.see.truetransact.uicomponent.CButton btnCust;
    private com.see.truetransact.uicomponent.CButton btnNewNO;
    private com.see.truetransact.uicomponent.CButton btnRemoveNO;
    private com.see.truetransact.uicomponent.CComboBox cboGCity;
    private com.see.truetransact.uicomponent.CComboBox cboGCountry;
    private com.see.truetransact.uicomponent.CComboBox cboGState;
    private com.see.truetransact.uicomponent.CComboBox cboNCity;
    private com.see.truetransact.uicomponent.CComboBox cboNCountry;
    private com.see.truetransact.uicomponent.CComboBox cboNState;
    private com.see.truetransact.uicomponent.CComboBox cboNomineeRelationNO;
    private com.see.truetransact.uicomponent.CComboBox cboNomineeStatus;
    private com.see.truetransact.uicomponent.CComboBox cboRelationNO;
    private com.see.truetransact.uicomponent.CLabel lblCustNo;
    private com.see.truetransact.uicomponent.CLabel lblCustomer;
    private com.see.truetransact.uicomponent.CLabel lblGArea;
    private com.see.truetransact.uicomponent.CLabel lblGCity;
    private com.see.truetransact.uicomponent.CLabel lblGCountry;
    private com.see.truetransact.uicomponent.CLabel lblGPinCode;
    private com.see.truetransact.uicomponent.CLabel lblGState;
    private com.see.truetransact.uicomponent.CLabel lblGStreet;
    private com.see.truetransact.uicomponent.CLabel lblGuardianNameNO;
    private com.see.truetransact.uicomponent.CLabel lblGuardianPhoneNO;
    private com.see.truetransact.uicomponent.CLabel lblMaxNominee;
    private com.see.truetransact.uicomponent.CLabel lblMinNominees;
    private com.see.truetransact.uicomponent.CLabel lblMinorDOBNO;
    private com.see.truetransact.uicomponent.CLabel lblNArea;
    private com.see.truetransact.uicomponent.CLabel lblNCity;
    private com.see.truetransact.uicomponent.CLabel lblNCountry;
    private com.see.truetransact.uicomponent.CLabel lblNPinCode;
    private com.see.truetransact.uicomponent.CLabel lblNState;
    private com.see.truetransact.uicomponent.CLabel lblNStreet;
    private com.see.truetransact.uicomponent.CLabel lblNomineeNameNO;
    private com.see.truetransact.uicomponent.CLabel lblNomineePhoneNO;
    private com.see.truetransact.uicomponent.CLabel lblNomineeRelationNO;
    private com.see.truetransact.uicomponent.CLabel lblNomineeShareNO;
    private com.see.truetransact.uicomponent.CLabel lblNomineeStatusNO;
    private com.see.truetransact.uicomponent.CLabel lblRelationNO;
    private com.see.truetransact.uicomponent.CLabel lblTotalShareNO;
    private com.see.truetransact.uicomponent.CPanel panGuardAddr;
    private com.see.truetransact.uicomponent.CPanel panGuardData;
    private com.see.truetransact.uicomponent.CPanel panGuardianDetails;
    private com.see.truetransact.uicomponent.CPanel panNominee;
    private com.see.truetransact.uicomponent.CPanel panNomineeAddr;
    private com.see.truetransact.uicomponent.CPanel panNomineeDetails;
    private com.see.truetransact.uicomponent.CPanel panNomineeInfo;
    private com.see.truetransact.uicomponent.CPanel panNomineeList;
    private com.see.truetransact.uicomponent.CPanel panNomineedata;
    private com.see.truetransact.uicomponent.CPanel panShareAddEditRemoveNO;
    private com.see.truetransact.uicomponent.CPanel phoneGPanelNO;
    private com.see.truetransact.uicomponent.CPanel phoneNPanelNO;
    private com.see.truetransact.uicomponent.CSeparator sepSepNOP4;
    private com.see.truetransact.uicomponent.CSeparator sptGuard;
    private com.see.truetransact.uicomponent.CSeparator sptNominee;
    private com.see.truetransact.uicomponent.CScrollPane srpListNomineeNO;
    private com.see.truetransact.uicomponent.CTable tblListNominee;
    private com.see.truetransact.uicomponent.CDateField tdtMinorDOBNO;
    private com.see.truetransact.uicomponent.CTextField txtGArea;
    private com.see.truetransact.uicomponent.CTextField txtGPinCode;
    private com.see.truetransact.uicomponent.CTextField txtGStreet;
    private com.see.truetransact.uicomponent.CTextField txtGuardianACodeNO;
    private com.see.truetransact.uicomponent.CTextField txtGuardianNameNO;
    private com.see.truetransact.uicomponent.CTextField txtGuardianPhoneNO;
    private com.see.truetransact.uicomponent.CTextField txtMaxNominee;
    private com.see.truetransact.uicomponent.CTextField txtMinNominees;
    private com.see.truetransact.uicomponent.CTextField txtNArea;
    private com.see.truetransact.uicomponent.CTextField txtNPinCode;
    private com.see.truetransact.uicomponent.CTextField txtNStreet;
    private com.see.truetransact.uicomponent.CTextField txtNomineeACodeNO;
    private com.see.truetransact.uicomponent.CTextField txtNomineeNameNO;
    private com.see.truetransact.uicomponent.CTextField txtNomineePhoneNO;
    private com.see.truetransact.uicomponent.CTextField txtNomineeShareNO;
    private com.see.truetransact.uicomponent.CTextField txtTotalShareNO;
    // End of variables declaration//GEN-END:variables
    
}

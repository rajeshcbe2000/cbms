/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AuthorizedSignatoryUI.java
 *
 * Created on December 22, 2004, 4:27 PM
 */

package com.see.truetransact.ui.common.authorizedsignatory;

import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.uicomponent.CPanel;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.EmailValidation;
import com.see.truetransact.uivalidation.PincodeValidation_IN;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.ui.common.viewall.ViewAll;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
/**
 *
 * @author  152713
 */
public class AuthorizedSignatoryUI extends CPanel implements java.util.Observer,UIMandatoryField{
    private AuthorizedSignatoryOB observableAuthSign;
    public int no_Authorized;   /*bala*/
    private AuthorizedSignatoryInstructionOB observableInstruction;
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.common.authorizedsignatory.AuthorizedSignatoryRB", ProxyParameters.LANGUAGE);
//    AuthorizedSignatoryRB resourceBundle = new AuthorizedSignatoryRB();
    
    String lblStatus = "";
    String viewType = "";
    String TITLE = "Authorized Signatory";
    private final static Logger log = Logger.getLogger(AuthorizedSignatoryUI.class);
    private final        String AUTHORIZE = "AUTHORIZE";
    private final        String REJECT = "REJECT";
    private final        String EXCEPTION = "EXCEPTION";
    
    private boolean updateModeAuthorize = false;
    private boolean updateModeAuthorizeInst = false;
    
    int modeAuthorize = -1;
    int result = -1;
    int authorizeInstSelectedRow = -1;
    
    private HashMap mandatoryMap;
    private ArrayList acctLevelCustomerList;
    
    /** Creates new form AuthorizedSignatoryUI */
    public AuthorizedSignatoryUI(String strModule){
        initComponents();
        authorizedSignatoryUI(strModule);
    }
    
    private void authorizedSignatoryUI(String strModule){
        setFieldNames();
        internationalize();
        setMaxLength();
        setObservable(strModule);
        ClientUtil.enableDisable(this, false);
        allEnableDisable();
        initComponentData();
        setMandatoryHashMap();
        acctLevelCustomerList = new ArrayList();
        new MandatoryCheck().putMandatoryMarks(getClass().getName(), panAuthorizedSignatory_Name1);
    }
    
    public void addAcctLevelCustomer(String strCustID){
        acctLevelCustomerList.add(strCustID);
    }
    
    public boolean removeAcctLevelCustomer(String strCustID){
        return acctLevelCustomerList.remove(strCustID);
    }
    
    public void clearAcctLevelCustomers(){
        acctLevelCustomerList = null;
        acctLevelCustomerList = new ArrayList();
    }
    
    public AuthorizedSignatoryOB getAuthorizedSignatoryOB(){
        return this.observableAuthSign;
    }
    
    public AuthorizedSignatoryInstructionOB getAuthorizedSignatoryInstructionOB(){
        return this.observableInstruction;
    }
    
    private void allEnableDisable(){
        setbtnCustEnableDisable(false);
        setAllAuthEnableDisable(false);
        setAuthTabBtnEnableDisable(false);
        setAuthInstAllBtnsEnableDisable(false);
        setAllAuthInstEnableDisable(false);
    }
    
    private void setObservable(String strModule){
        observableAuthSign = new AuthorizedSignatoryOB(strModule);
        observableAuthSign.addObserver(this);
        observableInstruction = new AuthorizedSignatoryInstructionOB(strModule);
        observableInstruction.addObserver(this);
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtNumberAuthSignatory", new Boolean(true));
        mandatoryMap.put("txtCustomerID", new Boolean(true));
        mandatoryMap.put("txtLimits", new Boolean(true));
        mandatoryMap.put("txtName_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtDesig_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("cboAddrCommunication_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtStreet_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtArea_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("cboCity_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("cboState_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("cboCountry_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtPin_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtHomePhone_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtBusinessPhone_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtHomeFax_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtBusinessFax_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtPager_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtMobile_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtEmailId_AuthorizedSignatory", new Boolean(true));
        mandatoryMap.put("txtInstruction", new Boolean(true));
        mandatoryMap.put("txtFromAmount", new Boolean(true));
        mandatoryMap.put("txtToAmount", new Boolean(true));
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void setHelpMessage() {
        final AuthorizedSignatoryMRB objMandatoryRB = new AuthorizedSignatoryMRB();
//        txtNumberAuthSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtNumberAuthSignatory"));
//        txtCustomerID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCustomerID"));
//        txtLimits.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLimits"));
//        txtName_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtName_AuthorizedSignatory"));
//        txtDesig_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDesig_AuthorizedSignatory"));
//        cboAddrCommunication_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboAddrCommunication_AuthorizedSignatory"));
//        txtStreet_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtStreet_AuthorizedSignatory"));
//        txtArea_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtArea_AuthorizedSignatory"));
//        cboCity_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCity_AuthorizedSignatory"));
//        cboState_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboState_AuthorizedSignatory"));
//        cboCountry_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("cboCountry_AuthorizedSignatory"));
//        txtPin_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPin_AuthorizedSignatory"));
//        txtHomePhone_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHomePhone_AuthorizedSignatory"));
//        txtBusinessPhone_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBusinessPhone_AuthorizedSignatory"));
//        txtHomeFax_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtHomeFax_AuthorizedSignatory"));
//        txtBusinessFax_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtBusinessFax_AuthorizedSignatory"));
//        txtPager_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtPager_AuthorizedSignatory"));
//        txtMobile_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtMobile_AuthorizedSignatory"));
//        txtEmailId_AuthorizedSignatory.setHelpMessage(lblMsg, objMandatoryRB.getString("txtEmailId_AuthorizedSignatory"));
    }
    
    private void setFieldNames() {
        btnAuthorizedDelete.setName("btnAuthorizedDelete");
        btnAuthorizedNew.setName("btnAuthorizedNew");
        btnAuthorizedSave.setName("btnAuthorizedSave");
        btnCustomerID.setName("btnCustomerID");
        cboAddrCommunication_AuthorizedSignatory.setName("cboAddrCommunication_AuthorizedSignatory");
        cboCity_AuthorizedSignatory.setName("cboCity_AuthorizedSignatory");
        cboCountry_AuthorizedSignatory.setName("cboCountry_AuthorizedSignatory");
        cboState_AuthorizedSignatory.setName("cboState_AuthorizedSignatory");
        lblArea_AuthorizedSignatory.setName("lblArea_AuthorizedSignatory");
        lblBusinessFax_AuthorizedSignatory.setName("lblBusinessFax_AuthorizedSignatory");
        lblBusinessPhone_AuthorizedSignatory.setName("lblBusinessPhone_AuthorizedSignatory");
        lblCity_AuthorizedSignatory.setName("lblCity_AuthorizedSignatory");
        lblCountry_AuthorizedSignatory.setName("lblCountry_AuthorizedSignatory");
        lblCustomerID.setName("lblCustomerID");
        txtCustomerID.setName("txtCustomerID");
        lblDesig_AuthorizedSignatory.setName("lblDesig_AuthorizedSignatory");
        lblEmailId_AuthorizedSignatory.setName("lblEmailId_AuthorizedSignatory");
        lblHomeFax_AuthorizedSignatory.setName("lblHomeFax_AuthorizedSignatory");
        lblHomePhone_AuthorizedSignatory.setName("lblHomePhone_AuthorizedSignatory");
        lblLimits.setName("lblLimits");
        lblMobile_AuthorizedSignatory.setName("lblMobile_AuthorizedSignatory");
        lblName_AuthorizedSignatory.setName("lblName_AuthorizedSignatory");
        lblNumberAuthSignatory.setName("lblNumberAuthSignatory");
        lblPager_AuthorizedSignatory.setName("lblPager_AuthorizedSignatory");
        lblPin_AuthorizedSignatory.setName("lblPin_AuthorizedSignatory");
        lblState_AuthorizedSignatory.setName("lblState_AuthorizedSignatory");
        lblStreet_AuthorizedSignatory.setName("lblStreet_AuthorizedSignatory");
        panAuthorizedSignatory.setName("panAuthorizedSignatory");
        panAuthorizedSignatory_Name.setName("panAuthorizedSignatory_Name");
        panAuthorizedSignatory_Name1.setName("panAuthorizedSignatory_Name1");
        panAuthorizedSignatory_Name2.setName("panAuthorizedSignatory_Name2");
        panAuthorizedTable.setName("panAuthorizedTable");
        panAuthorizedToolBtns.setName("panAuthorizedToolBtns");
        panAuthorizedsignatory_Number.setName("panAuthorizedsignatory_Number");
        sptAuthorizedSignatory_Hori.setName("sptAuthorizedSignatory_Hori");
        sptAuthorizedSignatory_Vert.setName("sptAuthorizedSignatory_Vert");
        srpAuthorizedTable.setName("srpAuthorizedTable");
        tblAuthorizedTable.setName("tblAuthorizedTable");
        txtArea_AuthorizedSignatory.setName("txtArea_AuthorizedSignatory");
        txtBusinessFax_AuthorizedSignatory.setName("txtBusinessFax_AuthorizedSignatory");
        txtBusinessPhone_AuthorizedSignatory.setName("txtBusinessPhone_AuthorizedSignatory");
        txtDesig_AuthorizedSignatory.setName("txtDesig_AuthorizedSignatory");
        txtEmailId_AuthorizedSignatory.setName("txtEmailId_AuthorizedSignatory");
        txtHomeFax_AuthorizedSignatory.setName("txtHomeFax_AuthorizedSignatory");
        txtHomePhone_AuthorizedSignatory.setName("txtHomePhone_AuthorizedSignatory");
        txtLimits.setName("txtLimits");
        txtMobile_AuthorizedSignatory.setName("txtMobile_AuthorizedSignatory");
        txtName_AuthorizedSignatory.setName("txtName_AuthorizedSignatory");
        txtNumberAuthSignatory.setName("txtNumberAuthSignatory");
        txtPager_AuthorizedSignatory.setName("txtPager_AuthorizedSignatory");
        txtPin_AuthorizedSignatory.setName("txtPin_AuthorizedSignatory");
        txtStreet_AuthorizedSignatory.setName("txtStreet_AuthorizedSignatory");
        ((javax.swing.border.TitledBorder)panAuthorizedSignatory.getBorder()).setTitle(resourceBundle.getString("panAuthorizedSignatory"));
        ((javax.swing.border.TitledBorder)panInstruction.getBorder()).setTitle(resourceBundle.getString("panInstruction"));
        lblInstruction.setName("lblInstruction");
        lblFromAmount.setName("lblFromAmount");
        lblToAmount.setName("lblToAmount");
        txtInstruction.setName("txtInstruction");
        txtFromAmount.setName("txtFromAmount");
        txtToAmount.setName("txtToAmount");
        panInstructionToolBtns.setName("panInstructionToolBtns");
        btnInstructionDelete.setName("btnInstructionDelete");
        btnInstructionNew.setName("btnInstructionNew");
        btnInstructionSave.setName("btnInstructionSave");
        panInstructionTable.setName("panInstructionTable");
        panInstructionFields.setName("panInstructionFields");
        panInstruction.setName("panInstruction");
        srpInstructionTable.setName("srpInstructionTable");
        tblInstructionTable.setName("tblInstructionTable");
    }
    
    private void internationalize() {
        btnAuthorizedDelete.setText(resourceBundle.getString("btnAuthorizedDelete"));
        btnAuthorizedNew.setText(resourceBundle.getString("btnAuthorizedNew"));
        btnAuthorizedSave.setText(resourceBundle.getString("btnAuthorizedSave"));
        btnCustomerID.setText(resourceBundle.getString("btnCustomerID"));
        btnInstructionDelete.setText(resourceBundle.getString("btnInstructionDelete"));
        btnInstructionNew.setText(resourceBundle.getString("btnInstructionNew"));
        btnInstructionSave.setText(resourceBundle.getString("btnInstructionSave"));
        lblInstruction.setText(resourceBundle.getString("lblInstruction"));
        lblFromAmount.setText(resourceBundle.getString("lblFromAmount"));
        lblToAmount.setText(resourceBundle.getString("lblToAmount"));
        lblArea_AuthorizedSignatory.setText(resourceBundle.getString("lblArea_AuthorizedSignatory"));
        lblBusinessFax_AuthorizedSignatory.setText(resourceBundle.getString("lblBusinessFax_AuthorizedSignatory"));
        lblBusinessPhone_AuthorizedSignatory.setText(resourceBundle.getString("lblBusinessPhone_AuthorizedSignatory"));
        lblCity_AuthorizedSignatory.setText(resourceBundle.getString("lblCity_AuthorizedSignatory"));
        lblCountry_AuthorizedSignatory.setText(resourceBundle.getString("lblCountry_AuthorizedSignatory"));
        lblCustomerID.setText(resourceBundle.getString("lblCustomerID"));
        lblDesig_AuthorizedSignatory.setText(resourceBundle.getString("lblDesig_AuthorizedSignatory"));
        lblEmailId_AuthorizedSignatory.setText(resourceBundle.getString("lblEmailId_AuthorizedSignatory"));
        lblHomeFax_AuthorizedSignatory.setText(resourceBundle.getString("lblHomeFax_AuthorizedSignatory"));
        lblHomePhone_AuthorizedSignatory.setText(resourceBundle.getString("lblHomePhone_AuthorizedSignatory"));
        lblLimits.setText(resourceBundle.getString("lblLimits"));
        lblMobile_AuthorizedSignatory.setText(resourceBundle.getString("lblMobile_AuthorizedSignatory"));
        lblName_AuthorizedSignatory.setText(resourceBundle.getString("lblName_AuthorizedSignatory"));
        lblNumberAuthSignatory.setText(resourceBundle.getString("lblNumberAuthSignatory"));
        lblPager_AuthorizedSignatory.setText(resourceBundle.getString("lblPager_AuthorizedSignatory"));
        lblPin_AuthorizedSignatory.setText(resourceBundle.getString("lblPin_AuthorizedSignatory"));
        lblState_AuthorizedSignatory.setText(resourceBundle.getString("lblState_AuthorizedSignatory"));
        lblStreet_AuthorizedSignatory.setText(resourceBundle.getString("lblStreet_AuthorizedSignatory"));
    }
    
    private void setMaxLength(){
        txtPin_AuthorizedSignatory.setMaxLength(16);
        txtPin_AuthorizedSignatory.setValidation(new PincodeValidation_IN());
        txtNumberAuthSignatory.setMaxLength(1);
        txtNumberAuthSignatory.setValidation(new NumericValidation(1, 0));
        txtLimits.setMaxLength(16);
        txtLimits.setValidation(new CurrencyValidation(14,2));
        txtDesig_AuthorizedSignatory.setMaxLength(32);
        txtHomePhone_AuthorizedSignatory.setMaxLength(32);
        txtHomePhone_AuthorizedSignatory.setAllowNumber(true);
        txtHomeFax_AuthorizedSignatory.setMaxLength(32);
        txtPager_AuthorizedSignatory.setMaxLength(32);
        txtEmailId_AuthorizedSignatory.setMaxLength(32);
        txtEmailId_AuthorizedSignatory.setValidation(new EmailValidation());
        txtBusinessFax_AuthorizedSignatory.setMaxLength(32);
        txtBusinessPhone_AuthorizedSignatory.setMaxLength(32);
        txtBusinessPhone_AuthorizedSignatory.setAllowNumber(true);
        txtMobile_AuthorizedSignatory.setMaxLength(32);
        txtFromAmount.setMaxLength(16);
        txtFromAmount.setValidation(new CurrencyValidation(14,2));
        txtToAmount.setMaxLength(16);
        txtToAmount.setValidation(new CurrencyValidation(14,2));
        txtInstruction.setMaxLength(256);
    }
    
    public void update(java.util.Observable o, Object arg) {
        txtNumberAuthSignatory.setText(observableAuthSign.getTxtNumberAuthSignatory());
        txtCustomerID.setText(observableAuthSign.getTxtCustomerID());
        txtLimits.setText(observableAuthSign.getTxtLimits());
        txtName_AuthorizedSignatory.setText(observableAuthSign.getTxtName_AuthorizedSignatory());
        txtDesig_AuthorizedSignatory.setText(observableAuthSign.getTxtDesig_AuthorizedSignatory());
        cboAddrCommunication_AuthorizedSignatory.setSelectedItem(observableAuthSign.getCboAddrCommunication_AuthorizedSignatory());
        txtStreet_AuthorizedSignatory.setText(observableAuthSign.getTxtStreet_AuthorizedSignatory());
        txtArea_AuthorizedSignatory.setText(observableAuthSign.getTxtArea_AuthorizedSignatory());
        cboCity_AuthorizedSignatory.setSelectedItem(observableAuthSign.getCboCity_AuthorizedSignatory());
        cboState_AuthorizedSignatory.setSelectedItem(observableAuthSign.getCboState_AuthorizedSignatory());
        cboCountry_AuthorizedSignatory.setSelectedItem(observableAuthSign.getCboCountry_AuthorizedSignatory());
        txtPin_AuthorizedSignatory.setText(observableAuthSign.getTxtPin_AuthorizedSignatory());
        txtHomePhone_AuthorizedSignatory.setText(observableAuthSign.getTxtHomePhone_AuthorizedSignatory());
        txtBusinessPhone_AuthorizedSignatory.setText(observableAuthSign.getTxtBusinessPhone_AuthorizedSignatory());
        txtHomeFax_AuthorizedSignatory.setText(observableAuthSign.getTxtHomeFax_AuthorizedSignatory());
        txtBusinessFax_AuthorizedSignatory.setText(observableAuthSign.getTxtBusinessFax_AuthorizedSignatory());
        txtPager_AuthorizedSignatory.setText(observableAuthSign.getTxtPager_AuthorizedSignatory());
        txtMobile_AuthorizedSignatory.setText(observableAuthSign.getTxtMobile_AuthorizedSignatory());
        txtEmailId_AuthorizedSignatory.setText(observableAuthSign.getTxtEmailId_AuthorizedSignatory());
        tblAuthorizedTable.setModel(observableAuthSign.getTblAuthorized());
        txtFromAmount.setText(observableInstruction.getTxtFromAmount());
        txtToAmount.setText(observableInstruction.getTxtToAmount());
        txtInstruction.setText(observableInstruction.getTxtInstruction());
        tblInstructionTable.setModel(observableInstruction.getTblInstructionTable());
    }
    
    public void updateOBFields(){
        observableAuthSign.setTxtNumberAuthSignatory(txtNumberAuthSignatory.getText());
        observableAuthSign.setTxtCustomerID(txtCustomerID.getText());
        observableAuthSign.setTxtLimits(txtLimits.getText());
        observableAuthSign.setTxtName_AuthorizedSignatory(txtName_AuthorizedSignatory.getText());
        observableAuthSign.setTxtDesig_AuthorizedSignatory(txtDesig_AuthorizedSignatory.getText());
        observableAuthSign.setCboAddrCommunication_AuthorizedSignatory((String) cboAddrCommunication_AuthorizedSignatory.getSelectedItem());
        observableAuthSign.setTxtStreet_AuthorizedSignatory(txtStreet_AuthorizedSignatory.getText());
        observableAuthSign.setTxtArea_AuthorizedSignatory(txtArea_AuthorizedSignatory.getText());
        observableAuthSign.setCboCity_AuthorizedSignatory((String) cboCity_AuthorizedSignatory.getSelectedItem());
        observableAuthSign.setCboState_AuthorizedSignatory((String) cboState_AuthorizedSignatory.getSelectedItem());
        observableAuthSign.setCboCountry_AuthorizedSignatory((String) cboCountry_AuthorizedSignatory.getSelectedItem());
        observableAuthSign.setTxtPin_AuthorizedSignatory(txtPin_AuthorizedSignatory.getText());
        observableAuthSign.setTxtHomePhone_AuthorizedSignatory(txtHomePhone_AuthorizedSignatory.getText());
        observableAuthSign.setTxtBusinessPhone_AuthorizedSignatory(txtBusinessPhone_AuthorizedSignatory.getText());
        observableAuthSign.setTxtHomeFax_AuthorizedSignatory(txtHomeFax_AuthorizedSignatory.getText());
        observableAuthSign.setTxtBusinessFax_AuthorizedSignatory(txtBusinessFax_AuthorizedSignatory.getText());
        observableAuthSign.setTxtPager_AuthorizedSignatory(txtPager_AuthorizedSignatory.getText());
        observableAuthSign.setTxtMobile_AuthorizedSignatory(txtMobile_AuthorizedSignatory.getText());
        observableAuthSign.setTxtEmailId_AuthorizedSignatory(txtEmailId_AuthorizedSignatory.getText());
        observableInstruction.setTxtFromAmount(txtFromAmount.getText());
        observableInstruction.setTxtInstruction(txtInstruction.getText());
        observableInstruction.setTxtToAmount(txtToAmount.getText());
    }
    
    private void initComponentData(){
        cboAddrCommunication_AuthorizedSignatory.setModel(observableAuthSign.getCbmAddrCommunication_AuthorizedSignatory());
        cboCity_AuthorizedSignatory.setModel(observableAuthSign.getCbmCity_AuthorizedSignatory());
        cboState_AuthorizedSignatory.setModel(observableAuthSign.getCbmState_AuthorizedSignatory());
        cboCountry_AuthorizedSignatory.setModel(observableAuthSign.getCbmCountry_AuthorizedSignatory());
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        panAuthorizedSignatory = new com.see.truetransact.uicomponent.CPanel();
        panAuthorizedsignatory_Number = new com.see.truetransact.uicomponent.CPanel();
        lblNumberAuthSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtNumberAuthSignatory = new com.see.truetransact.uicomponent.CTextField();
        panAuthorizedSignatory_Name = new com.see.truetransact.uicomponent.CPanel();
        panAuthorizedSignatory_Name1 = new com.see.truetransact.uicomponent.CPanel();
        lblCustomerID = new com.see.truetransact.uicomponent.CLabel();
        txtCustomerID = new com.see.truetransact.uicomponent.CTextField();
        btnCustomerID = new com.see.truetransact.uicomponent.CButton();
        lblLimits = new com.see.truetransact.uicomponent.CLabel();
        txtLimits = new com.see.truetransact.uicomponent.CTextField();
        lblName_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtName_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblDesig_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtDesig_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblAddrCommunication_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        cboAddrCommunication_AuthorizedSignatory = new com.see.truetransact.uicomponent.CComboBox();
        lblStreet_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtStreet_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblArea_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtArea_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblCity_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        cboCity_AuthorizedSignatory = new com.see.truetransact.uicomponent.CComboBox();
        lblState_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        cboState_AuthorizedSignatory = new com.see.truetransact.uicomponent.CComboBox();
        lblCountry_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        cboCountry_AuthorizedSignatory = new com.see.truetransact.uicomponent.CComboBox();
        sptAuthorizedSignatory_Vert = new com.see.truetransact.uicomponent.CSeparator();
        panAuthorizedSignatory_Name2 = new com.see.truetransact.uicomponent.CPanel();
        lblPin_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtPin_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblHomePhone_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtHomePhone_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblBusinessPhone_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtBusinessPhone_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblHomeFax_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtHomeFax_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblBusinessFax_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtBusinessFax_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblPager_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtPager_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblMobile_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtMobile_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblEmailId_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtEmailId_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        lblAreaCode_AuthorizedSignatory = new com.see.truetransact.uicomponent.CLabel();
        txtAreaCode_AuthorizedSignatory = new com.see.truetransact.uicomponent.CTextField();
        panAuthorizedToolBtns = new com.see.truetransact.uicomponent.CPanel();
        btnAuthorizedNew = new com.see.truetransact.uicomponent.CButton();
        btnAuthorizedSave = new com.see.truetransact.uicomponent.CButton();
        btnAuthorizedDelete = new com.see.truetransact.uicomponent.CButton();
        panAuthorizedTable = new com.see.truetransact.uicomponent.CPanel();
        srpAuthorizedTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblAuthorizedTable = new com.see.truetransact.uicomponent.CTable();
        sptAuthorizedSignatory_Hori = new com.see.truetransact.uicomponent.CSeparator();
        panInstruction = new com.see.truetransact.uicomponent.CPanel();
        panInstructionFields = new com.see.truetransact.uicomponent.CPanel();
        lblFromAmount = new com.see.truetransact.uicomponent.CLabel();
        txtFromAmount = new com.see.truetransact.uicomponent.CTextField();
        lblToAmount = new com.see.truetransact.uicomponent.CLabel();
        txtToAmount = new com.see.truetransact.uicomponent.CTextField();
        lblInstruction = new com.see.truetransact.uicomponent.CLabel();
        txtInstruction = new com.see.truetransact.uicomponent.CTextField();
        panInstructionToolBtns = new com.see.truetransact.uicomponent.CPanel();
        btnInstructionNew = new com.see.truetransact.uicomponent.CButton();
        btnInstructionSave = new com.see.truetransact.uicomponent.CButton();
        btnInstructionDelete = new com.see.truetransact.uicomponent.CButton();
        panInstructionTable = new com.see.truetransact.uicomponent.CPanel();
        srpInstructionTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblInstructionTable = new com.see.truetransact.uicomponent.CTable();

        setLayout(new java.awt.GridBagLayout());

        setMinimumSize(new java.awt.Dimension(830, 600));
        setPreferredSize(new java.awt.Dimension(830, 600));
        panAuthorizedSignatory.setLayout(new java.awt.GridBagLayout());

        panAuthorizedSignatory.setBorder(new javax.swing.border.TitledBorder("Authorized Signatory"));
        panAuthorizedSignatory.setMinimumSize(new java.awt.Dimension(840, 375));
        panAuthorizedSignatory.setPreferredSize(new java.awt.Dimension(840, 375));
        panAuthorizedsignatory_Number.setLayout(new java.awt.GridBagLayout());

        lblNumberAuthSignatory.setText("Number of Authorized Signatory");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 4);
        panAuthorizedsignatory_Number.add(lblNumberAuthSignatory, gridBagConstraints);

        txtNumberAuthSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        txtNumberAuthSignatory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumberAuthSignatoryActionPerformed(evt);
            }
        });
        txtNumberAuthSignatory.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumberAuthSignatoryFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorizedsignatory_Number.add(txtNumberAuthSignatory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 110, 0, 4);
        panAuthorizedSignatory.add(panAuthorizedsignatory_Number, gridBagConstraints);

        panAuthorizedSignatory_Name.setLayout(new java.awt.GridBagLayout());

        panAuthorizedSignatory_Name.setMinimumSize(new java.awt.Dimension(500, 265));
        panAuthorizedSignatory_Name.setPreferredSize(new java.awt.Dimension(500, 265));
        panAuthorizedSignatory_Name1.setLayout(new java.awt.GridBagLayout());

        panAuthorizedSignatory_Name1.setMinimumSize(new java.awt.Dimension(280, 255));
        panAuthorizedSignatory_Name1.setPreferredSize(new java.awt.Dimension(280, 255));
        lblCustomerID.setText("Customer Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblCustomerID, gridBagConstraints);

        txtCustomerID.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustomerIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(txtCustomerID, gridBagConstraints);

        btnCustomerID.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SELECT.gif")));
        btnCustomerID.setToolTipText("Save");
        btnCustomerID.setPreferredSize(new java.awt.Dimension(21, 21));
        btnCustomerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomerIDActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAuthorizedSignatory_Name1.add(btnCustomerID, gridBagConstraints);

        lblLimits.setText("Limits");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblLimits, gridBagConstraints);

        txtLimits.setMinimumSize(new java.awt.Dimension(100, 21));
        txtLimits.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtLimitsFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(txtLimits, gridBagConstraints);

        lblName_AuthorizedSignatory.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblName_AuthorizedSignatory, gridBagConstraints);

        txtName_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(txtName_AuthorizedSignatory, gridBagConstraints);

        lblDesig_AuthorizedSignatory.setText("Designation");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblDesig_AuthorizedSignatory, gridBagConstraints);

        txtDesig_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(txtDesig_AuthorizedSignatory, gridBagConstraints);

        lblAddrCommunication_AuthorizedSignatory.setText("Communication Address");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblAddrCommunication_AuthorizedSignatory, gridBagConstraints);

        cboAddrCommunication_AuthorizedSignatory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboAddrCommunication_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(cboAddrCommunication_AuthorizedSignatory, gridBagConstraints);

        lblStreet_AuthorizedSignatory.setText("Street");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblStreet_AuthorizedSignatory, gridBagConstraints);

        txtStreet_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(txtStreet_AuthorizedSignatory, gridBagConstraints);

        lblArea_AuthorizedSignatory.setText("Area");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblArea_AuthorizedSignatory, gridBagConstraints);

        txtArea_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(txtArea_AuthorizedSignatory, gridBagConstraints);

        lblCity_AuthorizedSignatory.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblCity_AuthorizedSignatory, gridBagConstraints);

        cboCity_AuthorizedSignatory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCity_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(cboCity_AuthorizedSignatory, gridBagConstraints);

        lblState_AuthorizedSignatory.setText("State");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblState_AuthorizedSignatory, gridBagConstraints);

        cboState_AuthorizedSignatory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(cboState_AuthorizedSignatory, gridBagConstraints);

        lblCountry_AuthorizedSignatory.setText("Country");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(lblCountry_AuthorizedSignatory, gridBagConstraints);

        cboCountry_AuthorizedSignatory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "---Select---" }));
        cboCountry_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name1.add(cboCountry_AuthorizedSignatory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorizedSignatory_Name.add(panAuthorizedSignatory_Name1, gridBagConstraints);

        sptAuthorizedSignatory_Vert.setOrientation(javax.swing.SwingConstants.VERTICAL);
        sptAuthorizedSignatory_Vert.setMinimumSize(new java.awt.Dimension(2, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorizedSignatory_Name.add(sptAuthorizedSignatory_Vert, gridBagConstraints);

        panAuthorizedSignatory_Name2.setLayout(new java.awt.GridBagLayout());

        panAuthorizedSignatory_Name2.setMinimumSize(new java.awt.Dimension(202, 235));
        panAuthorizedSignatory_Name2.setPreferredSize(new java.awt.Dimension(202, 261));
        lblPin_AuthorizedSignatory.setText("Pincode");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblPin_AuthorizedSignatory, gridBagConstraints);

        txtPin_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtPin_AuthorizedSignatory, gridBagConstraints);

        lblHomePhone_AuthorizedSignatory.setText("Home Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblHomePhone_AuthorizedSignatory, gridBagConstraints);

        txtHomePhone_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtHomePhone_AuthorizedSignatory, gridBagConstraints);

        lblBusinessPhone_AuthorizedSignatory.setText("Business Phone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblBusinessPhone_AuthorizedSignatory, gridBagConstraints);

        txtBusinessPhone_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtBusinessPhone_AuthorizedSignatory, gridBagConstraints);

        lblHomeFax_AuthorizedSignatory.setText("Home Fax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblHomeFax_AuthorizedSignatory, gridBagConstraints);

        txtHomeFax_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtHomeFax_AuthorizedSignatory, gridBagConstraints);

        lblBusinessFax_AuthorizedSignatory.setText("Business Fax");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblBusinessFax_AuthorizedSignatory, gridBagConstraints);

        txtBusinessFax_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtBusinessFax_AuthorizedSignatory, gridBagConstraints);

        lblPager_AuthorizedSignatory.setText("Pager");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblPager_AuthorizedSignatory, gridBagConstraints);

        txtPager_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtPager_AuthorizedSignatory, gridBagConstraints);

        lblMobile_AuthorizedSignatory.setText("Mobile");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblMobile_AuthorizedSignatory, gridBagConstraints);

        txtMobile_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtMobile_AuthorizedSignatory, gridBagConstraints);

        lblEmailId_AuthorizedSignatory.setText("Email Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblEmailId_AuthorizedSignatory, gridBagConstraints);

        txtEmailId_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtEmailId_AuthorizedSignatory, gridBagConstraints);

        lblAreaCode_AuthorizedSignatory.setText("Area Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(lblAreaCode_AuthorizedSignatory, gridBagConstraints);

        txtAreaCode_AuthorizedSignatory.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAuthorizedSignatory_Name2.add(txtAreaCode_AuthorizedSignatory, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorizedSignatory_Name.add(panAuthorizedSignatory_Name2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAuthorizedSignatory.add(panAuthorizedSignatory_Name, gridBagConstraints);

        panAuthorizedToolBtns.setLayout(new java.awt.GridBagLayout());

        btnAuthorizedNew.setText("New");
        btnAuthorizedNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizedNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorizedToolBtns.add(btnAuthorizedNew, gridBagConstraints);

        btnAuthorizedSave.setText("Save");
        btnAuthorizedSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizedSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorizedToolBtns.add(btnAuthorizedSave, gridBagConstraints);

        btnAuthorizedDelete.setText("Delete");
        btnAuthorizedDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizedDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAuthorizedToolBtns.add(btnAuthorizedDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 190, 0, 4);
        panAuthorizedSignatory.add(panAuthorizedToolBtns, gridBagConstraints);

        panAuthorizedTable.setLayout(new java.awt.GridBagLayout());

        panAuthorizedTable.setMinimumSize(new java.awt.Dimension(300, 325));
        panAuthorizedTable.setPreferredSize(new java.awt.Dimension(300, 325));
        srpAuthorizedTable.setMinimumSize(new java.awt.Dimension(295, 325));
        srpAuthorizedTable.setPreferredSize(new java.awt.Dimension(295, 325));
        tblAuthorizedTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AS No.", "Customer ID", "N", "Limits"
            }
        ));
        tblAuthorizedTable.setMinimumSize(new java.awt.Dimension(200, 0));
        tblAuthorizedTable.setPreferredScrollableViewportSize(new java.awt.Dimension(200, 140));
        tblAuthorizedTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblAuthorizedTableMousePressed(evt);
            }
        });

        srpAuthorizedTable.setViewportView(tblAuthorizedTable);

        panAuthorizedTable.add(srpAuthorizedTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 4;
        panAuthorizedSignatory.add(panAuthorizedTable, gridBagConstraints);

        sptAuthorizedSignatory_Hori.setMinimumSize(new java.awt.Dimension(0, 2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        panAuthorizedSignatory.add(sptAuthorizedSignatory_Hori, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panAuthorizedSignatory, gridBagConstraints);

        panInstruction.setLayout(new java.awt.GridBagLayout());

        panInstruction.setBorder(new javax.swing.border.TitledBorder("Instruction"));
        panInstruction.setMinimumSize(new java.awt.Dimension(840, 180));
        panInstruction.setPreferredSize(new java.awt.Dimension(840, 180));
        panInstructionFields.setLayout(new java.awt.GridBagLayout());

        panInstructionFields.setMinimumSize(new java.awt.Dimension(225, 128));
        panInstructionFields.setPreferredSize(new java.awt.Dimension(225, 128));
        lblFromAmount.setText("From Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 4, 4);
        panInstructionFields.add(lblFromAmount, gridBagConstraints);

        txtFromAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtFromAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFromAmountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructionFields.add(txtFromAmount, gridBagConstraints);

        lblToAmount.setText("To Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 4, 4);
        panInstructionFields.add(lblToAmount, gridBagConstraints);

        txtToAmount.setMinimumSize(new java.awt.Dimension(100, 21));
        txtToAmount.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtToAmountFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructionFields.add(txtToAmount, gridBagConstraints);

        lblInstruction.setText("Instruction");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 12, 4, 4);
        panInstructionFields.add(lblInstruction, gridBagConstraints);

        txtInstruction.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructionFields.add(txtInstruction, gridBagConstraints);

        panInstructionToolBtns.setLayout(new java.awt.GridBagLayout());

        btnInstructionNew.setText("New");
        btnInstructionNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstructionNewActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructionToolBtns.add(btnInstructionNew, gridBagConstraints);

        btnInstructionSave.setText("Save");
        btnInstructionSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstructionSaveActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructionToolBtns.add(btnInstructionSave, gridBagConstraints);

        btnInstructionDelete.setText("Delete");
        btnInstructionDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstructionDeleteActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panInstructionToolBtns.add(btnInstructionDelete, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 0);
        panInstructionFields.add(panInstructionToolBtns, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        panInstruction.add(panInstructionFields, gridBagConstraints);

        panInstructionTable.setLayout(new java.awt.GridBagLayout());

        panInstructionTable.setMinimumSize(new java.awt.Dimension(400, 128));
        panInstructionTable.setPreferredSize(new java.awt.Dimension(400, 128));
        srpInstructionTable.setMinimumSize(new java.awt.Dimension(400, 128));
        srpInstructionTable.setPreferredSize(new java.awt.Dimension(400, 128));
        tblInstructionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tblInstructionTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblInstructionTableMousePressed(evt);
            }
        });

        srpInstructionTable.setViewportView(tblInstructionTable);

        panInstructionTable.add(srpInstructionTable, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 4);
        panInstruction.add(panInstructionTable, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panInstruction, gridBagConstraints);

    }//GEN-END:initComponents

    private void txtNumberAuthSignatoryFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumberAuthSignatoryFocusLost
        // TODO add your handling code here:
        no_Authorized = Integer.parseInt(txtNumberAuthSignatory.getText());
    }//GEN-LAST:event_txtNumberAuthSignatoryFocusLost

    private void txtNumberAuthSignatoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumberAuthSignatoryActionPerformed

        // TODO add your handling code here:
       // no_Authorized = Integer.parseInt(txtNumberAuthSignatory.getText());
        
    }//GEN-LAST:event_txtNumberAuthSignatoryActionPerformed

    private void txtCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustomerIDActionPerformed
        String txtCustId = txtCustomerID.getText();             // TODO add your handling code here:
        HashMap txtCust =new HashMap();
        txtCust.put("CUSTOMER ID", txtCustId);
        viewType = "Author_Cust_Id";
	fillData(txtCust);
        
    }//GEN-LAST:event_txtCustomerIDActionPerformed
    public int getAuthorizedSignatoryRowCount(){
        return tblAuthorizedTable.getRowCount();
    }
    private void txtLimitsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLimitsFocusLost
        // TODO add your handling code here:
        txtLimitsFocusLost();
    }//GEN-LAST:event_txtLimitsFocusLost
    private void txtLimitsFocusLost() {
        // TODO add your handling code here:
        if (CommonUtil.convertObjToInt(txtLimits.getText()) == 0){
            txtLimits.setText("");
        }
    }
    private void tblInstructionTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInstructionTableMousePressed
        // TODO add your handling code here:
        tblInstructionTableMousePressed();
    }//GEN-LAST:event_tblInstructionTableMousePressed
    private void tblInstructionTableMousePressed() {
        if (tblInstructionTable.getSelectedRow() >= 0){
            updateOBFields();
            observableInstruction.populateAuthorizeInstTab(tblInstructionTable.getSelectedRow());
            if ((getLblStatus().equals(ClientConstants.ACTION_STATUS[3]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[7]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[8]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[10])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) || (viewType.equals(ClientConstants.ACTION_STATUS[3]))){
                setAllAuthInstEnableDisable(false);
                setAuthInstAllBtnsEnableDisable(false);
            }else{
                setAllAuthInstEnableDisable(true);
                setAuthInstAllBtnsEnableDisable(true);
                updateModeAuthorizeInst = true;
                authorizeInstSelectedRow = tblInstructionTable.getSelectedRow();
            }
            observableInstruction.ttNotifyObservers();
        }
    }
    private void txtToAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtToAmountFocusLost
        // TODO add your handling code here:
        txtToAmountFocusLost();
    }//GEN-LAST:event_txtToAmountFocusLost
    private void txtToAmountFocusLost() {
        updateOBFromToAmountOB();
        if (txtFromAmount.getText().length() > 0){
            if ((CommonUtil.convertObjToInt(txtToAmount.getText()) == 0) || CommonUtil.convertObjToInt(txtFromAmount.getText()) > CommonUtil.convertObjToInt(txtToAmount.getText())){
                observableInstruction.setTxtToAmount("");
            }
        }else{
            if (CommonUtil.convertObjToInt(txtToAmount.getText()) == 0){
                observableInstruction.setTxtToAmount("");
            }
        }
        updateFromToAmount();
    }
    private void txtFromAmountFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFromAmountFocusLost
        // TODO add your handling code here:
        txtFromAmountFocusLost();
    }//GEN-LAST:event_txtFromAmountFocusLost
    private void txtFromAmountFocusLost() {
        updateOBFromToAmountOB();
        if (txtToAmount.getText().length() > 0){
            if ((CommonUtil.convertObjToInt(txtFromAmount.getText()) == 0) || CommonUtil.convertObjToInt(txtFromAmount.getText()) > CommonUtil.convertObjToInt(txtToAmount.getText())){
                observableInstruction.setTxtFromAmount("");
            }
        }else{
            if (CommonUtil.convertObjToInt(txtFromAmount.getText()) == 0){
                observableInstruction.setTxtFromAmount("");
            }
        }
        updateFromToAmount();
    }
    private void updateFromToAmount(){
        txtFromAmount.setText(observableInstruction.getTxtFromAmount());
        txtToAmount.setText(observableInstruction.getTxtToAmount());
    }
    private void updateOBFromToAmountOB(){
        observableInstruction.setTxtFromAmount(txtFromAmount.getText());
        observableInstruction.setTxtToAmount(txtToAmount.getText());
    }
    private void btnInstructionDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstructionDeleteActionPerformed
        // TODO add your handling code here:
        btnInstructionDeleteActionPerformed();
    }//GEN-LAST:event_btnInstructionDeleteActionPerformed
    private void btnInstructionDeleteActionPerformed() {
        updateOBFields();
        observableInstruction.deleteAuthorizedInstTab(authorizeInstSelectedRow);
        observableInstruction.resetInstructionFields();
        setAllAuthInstEnableDisable(false);
        setAuthInstOnlyNewBtnEnable();
        observableInstruction.ttNotifyObservers();
        authorizeInstSelectedRow = -1;
        updateModeAuthorizeInst = false;
    }
    private void btnInstructionSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstructionSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panInstructionFields);
        /* mandatoryMessage length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        if (mandatoryMessage.length() > 0){
            displayAlert(mandatoryMessage);
        }else{
            btnInstructionSaveActionPerformed();
        }
    }//GEN-LAST:event_btnInstructionSaveActionPerformed
    private void btnInstructionSaveActionPerformed() {
        result=0;
        updateOBFields();
        result = observableInstruction.addAuthorizedInstTab(authorizeInstSelectedRow, updateModeAuthorizeInst);
        //ClientUtil.enableDisable(panAuthorizedSignatory_Name,false);
        if (result == 1){
            setAllAuthInstEnableDisable(true);
        }else{
            observableInstruction.resetInstructionFields();
            setAllAuthInstEnableDisable(false);
            setAuthInstOnlyNewBtnEnable();
        }
        setUpdateModeAuthorizeInst(false);
        observableInstruction.ttNotifyObservers();
    }
    private void btnInstructionNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstructionNewActionPerformed
        // TODO add your handling code here:
        btnInstructionNewActionPerformed();
    }//GEN-LAST:event_btnInstructionNewActionPerformed
    private void btnInstructionNewActionPerformed() {
        updateOBFields();
        observableInstruction.resetInstructionFields();
        observableInstruction.ttNotifyObservers();
        setAllAuthInstEnableDisable(true);
        setAuthInstOnlyDeleteBtnDisable();
        authorizeInstSelectedRow = -1;
        updateModeAuthorizeInst = false;
    }    
    private void btnAuthorizedDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizedDeleteActionPerformed
        // TODO add your handling code here:
        btnAuthorizedDelete();
    }//GEN-LAST:event_btnAuthorizedDeleteActionPerformed
    private void btnAuthorizedDelete(){
        updateOBFields();
        observableAuthSign.deleteAuthorizedTab(modeAuthorize);
        observableAuthSign.resetAuthorizedForm();
        setAuthEnableDisable(false);
        setAuthOnlyNewBtnEnable();
        observableAuthSign.ttNotifyObservers();
        modeAuthorize = -1;
        updateModeAuthorize = false;
    }
    private void btnAuthorizedSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizedSaveActionPerformed
        // TODO add your handling code here:
        final String mandatoryMessage1 = new MandatoryCheck().checkMandatory(getClass().getName(), panAuthorizedSignatory_Name1);
        /* mandatoryMessage1 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        String mandatoryMessage2 = "";
        if (CommonUtil.convertObjToInt(txtLimits.getText()) <= 0){
            mandatoryMessage2 = resourceBundle.getString("limitLessThanOrEqualToZeroWarning");
        }
        /* mandatoryMessage2 length will be greater than 0 if the mandatory conditions are not satisfied and so the alert should be displayed*/
        String mandatoryMessage3 = "";
        if (getAcctLevelCustomerList().contains(txtCustomerID.getText())){
            mandatoryMessage3 = resourceBundle.getString("acctLevelCustWarning");
        }
        if (mandatoryMessage1.length() > 0 || mandatoryMessage2.length() > 0 || mandatoryMessage3.length() > 0){
            displayAlert(mandatoryMessage1 + mandatoryMessage2 + "\n" + mandatoryMessage3);
        }else{
            btnauthorizedSaveActionPerformed();
        }
    }//GEN-LAST:event_btnAuthorizedSaveActionPerformed
    private void btnauthorizedSaveActionPerformed(){
        result=0;
        updateOBFields();
        result = observableAuthSign.addAuthorizedTab(modeAuthorize, updateModeAuthorize);
        //ClientUtil.enableDisable(panAuthorizedSignatory_Name,false);
        if (result == 1){
            setAllAuthEnableDisable(true);
            btnCustomerID.setEnabled(false);
        }else{
            observableAuthSign.resetAuthorizedForm();
            setAuthEnableDisable(false);
            setAuthOnlyNewBtnEnable();
            btnCustomerID.setEnabled(false);
        }
        setUpdateModeAuthorize(false);
        observableAuthSign.ttNotifyObservers();
    }
    
    private void displayAlert(String message){
        final CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
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
    private void tblAuthorizedTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAuthorizedTableMousePressed
        // TODO add your handling code here:
        authorizedMousePressed();
    }//GEN-LAST:event_tblAuthorizedTableMousePressed
    private void authorizedMousePressed(){
        if (tblAuthorizedTable.getSelectedRow() >= 0){
            updateOBFields();
            observableAuthSign.populateAuthorizeTab(tblAuthorizedTable.getSelectedRow());
            if ((getLblStatus().equals(ClientConstants.ACTION_STATUS[3]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[7]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[8]) || getLblStatus().equals(ClientConstants.ACTION_STATUS[10])) || (viewType.equals(AUTHORIZE) || viewType.equals(EXCEPTION) || viewType.equals(REJECT)) || (viewType.equals(ClientConstants.ACTION_STATUS[3]))){
               
                {
                setAllAuthEnableDisable(false);
                setAuthTabBtnEnableDisable(false);
                btnCustomerID.setEnabled(false);
               
               
                }
            }else{
                setAllAuthEnableDisable(true);
                setAuthTabBtnEnableDisable(true);
                btnCustomerID.setEnabled(false);
                updateModeAuthorize = true;
                modeAuthorize = tblAuthorizedTable.getSelectedRow();
                
            }
            observableAuthSign.ttNotifyObservers();
        }
    }
    private void btnAuthorizedNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizedNewActionPerformed
        // TODO add your handling code here:
        int rowcount=tblAuthorizedTable.getRowCount();
        
        System.out.println("rowcount"+rowcount+"no_Authorized"+no_Authorized);
        if(rowcount<no_Authorized)
            btnauthorizedNewActionPerformed();
        else
            ClientUtil.showMessageWindow(no_Authorized+" persons added already");
    }//GEN-LAST:event_btnAuthorizedNewActionPerformed
    private void btnauthorizedNewActionPerformed(){
        updateOBFields();
        ClientUtil.enableDisable(panAuthorizedTable, true);
        setAllAuthEnableDisable(true);
        setAuthOnlyNewSaveBtnsEnable();
        txtCustomerID.setEditable(true);
        btnCustomerID.setEnabled(true);
        observableAuthSign.resetAuthorizedForm();
        observableAuthSign.ttNotifyObservers();
        modeAuthorize = -1;
        updateModeAuthorize = false;
    }
    private void btnCustomerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomerIDActionPerformed
        // TODO add your handling code here:
        observableAuthSign.setTxtNumberAuthSignatory(txtNumberAuthSignatory.getText());
        callView("Author_Cust_Id");
    }//GEN-LAST:event_btnCustomerIDActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception{
        javax.swing.JFrame frm = new javax.swing.JFrame();
        AuthorizedSignatoryUI authsign = new AuthorizedSignatoryUI("TL");
        frm.getContentPane().add(authsign);
        authsign.show();
        frm.setSize(600, 500);
        frm.show();
    }
    
    public void callView(String currField) {
        viewType = currField;
        if (currField == "Author_Cust_Id"){
            HashMap viewMap = new HashMap();
            StringBuffer presentCust = new StringBuffer();
            int jntAccntTablRow = tblAuthorizedTable.getRowCount();
            if(tblAuthorizedTable.getRowCount()!=0) {
                for(int i =0, sizeJointAcctAll = tblAuthorizedTable.getRowCount();i<sizeJointAcctAll;i++){
                    if(i==0 || i==sizeJointAcctAll){
                        presentCust.append("'" + CommonUtil.convertObjToStr(tblAuthorizedTable.getValueAt(i, 1)) + "'");
                    } else{
                        presentCust.append(",'" + CommonUtil.convertObjToStr(tblAuthorizedTable.getValueAt(i, 1)) + "'");
                    }
                }
            }
            int acctLevelCustomerListSize = acctLevelCustomerList.size();
            int presentCustLength = presentCust.length();
            for (int i = acctLevelCustomerListSize - 1,j = 0;i >= 0;--i,++j){
                if (presentCustLength == 0 || j == acctLevelCustomerListSize){
                    presentCust.append("'" + CommonUtil.convertObjToStr(acctLevelCustomerList.get(j)) + "'");
                }else{
                    presentCust.append(",'" + CommonUtil.convertObjToStr(acctLevelCustomerList.get(j)) + "'");
                }
            }
            viewMap.put("MAPNAME", "getSelectAccInfoTOList");
            HashMap whereMap = new HashMap();
            whereMap.put("AUTHSIGNATORYCHECK","CHECK");
            whereMap.put("OMIT_BRANCH_ID","Y");
            whereMap.put("CUSTOMER_ID", presentCust);
            whereMap.put("SELECTED_BRANCH_ID", com.see.truetransact.ui.TrueTransactMain.BRANCH_ID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            new ViewAll(this, TITLE, viewMap).show();
            presentCust = null;
        }
    }
    // To display the All the Cust Id's which r having status as
    // created or updated, in a table...
    private void popUp(String field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field.equals("Author_Cust_Id")){
            viewMap.put("MAPNAME", "getCustomers");
        }
        new ViewAll(this, TITLE, viewMap).show();
    }
    
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
         
        if (viewType != null) {
            if (viewType.equals("Author_Cust_Id")){
                final String CustID = CommonUtil.convertObjToStr(hash.get("CUSTOMER ID"));
                observableAuthSign.setTxtCustomerID(CustID);
                observableAuthSign.setAuthCustAddr(CustID);
                observableAuthSign.setAuthCustName(CustID);
                observableAuthSign.setAuthCustPhone(CustID);
                observableAuthSign.ttNotifyObservers();
                setAllAuthEnableDisable(true);
                setAuthOnlyNewSaveBtnsEnable();
                txtCustomerID.setEditable(true);
            }
        }
        observableAuthSign.ttNotifyObservers();
    }
    
    public String isHavingProperNoOfRecords(String strWarning){
        if (((CommonUtil.convertObjToInt(txtNumberAuthSignatory.getText())) != (tblAuthorizedTable.getRowCount())) || (txtNumberAuthSignatory.getText().length() < 1)){
            strWarning = strWarning + "\n" + resourceBundle.getString("existenceAuthSignTableWarning") + txtNumberAuthSignatory.getText() + " record(s)!!!";
        }
        return strWarning;
    }
    
    public void resetAuthForm(){
        observableAuthSign.resetAuthorizedForm();
        observableAuthSign.ttNotifyObservers();
    }
    
    public void resetAllFieldsInAuthTab(){
        clearAcctLevelCustomers();
        observableAuthSign.resetAllFieldsInAuthTab();
        resetAllFieldsInAuthInstTab();
        observableAuthSign.ttNotifyObservers();
    }
    
    public void resetAuthInstForm(){
        observableInstruction.resetInstructionFields();
        observableInstruction.ttNotifyObservers();
    }
    
    public void resetAllFieldsInAuthInstTab(){
        observableInstruction.resetAllInstruction();
        observableInstruction.ttNotifyObservers();
    }
    
    public void setAllAuthEnableDisable(boolean val){
        txtNumberAuthSignatory.setEnabled(val);
        setbtnCustEnableDisable(false);
        setAuthEnableDisable(val);
        observableAuthSign.ttNotifyObservers();
    }
    
    //To enable or disable text fields and comboboxes of Authorized Status
    public void setAuthEnableDisable(boolean val){
        txtCustomerID.setEditable(false);
        txtCustomerID.setEnabled(val);
        txtLimits.setEnabled(val);
        txtAreaCode_AuthorizedSignatory.setEditable(false);
        txtAreaCode_AuthorizedSignatory.setEnabled(val);
        txtArea_AuthorizedSignatory.setEditable(false);
        txtArea_AuthorizedSignatory.setEnabled(val);
        txtName_AuthorizedSignatory.setEditable(false);
        txtName_AuthorizedSignatory.setEnabled(val);
        txtPin_AuthorizedSignatory.setEditable(false);
        txtPin_AuthorizedSignatory.setEnabled(val);
        cboState_AuthorizedSignatory.setEnabled(false);
        txtStreet_AuthorizedSignatory.setEditable(false);
        txtStreet_AuthorizedSignatory.setEnabled(val);
        cboAddrCommunication_AuthorizedSignatory.setEnabled(false);
        cboCity_AuthorizedSignatory.setEnabled(false);
        cboCountry_AuthorizedSignatory.setEnabled(false);
        txtDesig_AuthorizedSignatory.setEditable(false);
        txtDesig_AuthorizedSignatory.setEnabled(val);
        txtBusinessFax_AuthorizedSignatory.setEditable(false);
        txtBusinessFax_AuthorizedSignatory.setEnabled(val);
        txtBusinessPhone_AuthorizedSignatory.setEditable(false);
        txtBusinessPhone_AuthorizedSignatory.setEnabled(val);
        txtEmailId_AuthorizedSignatory.setEditable(false);
        txtEmailId_AuthorizedSignatory.setEnabled(val);
        txtHomeFax_AuthorizedSignatory.setEditable(false);
        txtHomeFax_AuthorizedSignatory.setEnabled(val);
        txtHomePhone_AuthorizedSignatory.setEditable(false);
        txtHomePhone_AuthorizedSignatory.setEnabled(val);
        txtMobile_AuthorizedSignatory.setEditable(false);
        txtMobile_AuthorizedSignatory.setEnabled(val);
        txtPager_AuthorizedSignatory.setEditable(false);
        txtPager_AuthorizedSignatory.setEnabled(val);
        btnCustomerID.setEnabled(val);
       
    }
    
    // To enable and disable the buttons in Authorized Signatory
    public void setAuthTabBtnEnableDisable(boolean val){
        btnAuthorizedDelete.setEnabled(val);
        btnAuthorizedNew.setEnabled(val);
        btnAuthorizedSave.setEnabled(val);
    }
    
    public void setAuthOnlyNewBtnEnable(){
        btnAuthorizedDelete.setEnabled(false);
        btnAuthorizedNew.setEnabled(true);
        btnAuthorizedSave.setEnabled(false);
    }
    
    public void setAuthOnlyNewSaveBtnsEnable(){
        btnAuthorizedDelete.setEnabled(false);
        btnAuthorizedNew.setEnabled(true);
        btnAuthorizedSave.setEnabled(true);
    }
    
    public void setbtnCustEnableDisable(boolean val){
        btnCustomerID.setEnabled(val);
    }
    
    public void setAuthInstOnlyNewBtnEnable(){
        btnInstructionDelete.setEnabled(false);
        btnInstructionNew.setEnabled(true);
        btnInstructionSave.setEnabled(false);
    }
    
    public void setAuthInstOnlyDeleteBtnDisable(){
        btnInstructionDelete.setEnabled(false);
        btnInstructionNew.setEnabled(true);
        btnInstructionSave.setEnabled(true);
    }
    
    public void setAuthInstAllBtnsEnableDisable(boolean val){
        btnInstructionDelete.setEnabled(val);
        btnInstructionNew.setEnabled(val);
        btnInstructionSave.setEnabled(val);
    }
    
    public void setAllAuthInstEnableDisable(boolean val){
        ClientUtil.enableDisable(panInstructionFields, val);
    }
    
    /** returns the Authorized CTable Row count
     */
    public int getTblRowCount(){
        return tblAuthorizedTable.getRowCount();
    }
    
    public void resetDisableNoOfAuthSign(boolean val){
        txtNumberAuthSignatory.setText("");
        txtNumberAuthSignatory.setEnabled(val);
        observableAuthSign.ttNotifyObservers();
    }
    
    /**
     * Getter for property updateModeAuthorize.
     * @return Value of property updateModeAuthorize.
     */
    public boolean isUpdateModeAuthorize() {
        return updateModeAuthorize;
    }
    
    /**
     * Setter for property updateModeAuthorize.
     * @param updateModeAuthorize New value of property updateModeAuthorize.
     */
    public void setUpdateModeAuthorize(boolean updateModeAuthorize) {
        this.updateModeAuthorize = updateModeAuthorize;
    }
    
    /**
     * Getter for property updateModeAuthorizeInst.
     * @return Value of property updateModeAuthorizeInst.
     */
    public boolean isUpdateModeAuthorizeInst() {
        return updateModeAuthorizeInst;
    }
    
    /**
     * Setter for property updateModeAuthorizeInst.
     * @param updateModeAuthorizeInst New value of property updateModeAuthorizeInst.
     */
    private void setUpdateModeAuthorizeInst(boolean updateModeAuthorizeInst) {
        this.updateModeAuthorizeInst = updateModeAuthorizeInst;
    }
    
    /**
     * Getter for property acctLevelCustomerList.
     * @return Value of property acctLevelCustomerList.
     */
    public java.util.ArrayList getAcctLevelCustomerList() {
        return acctLevelCustomerList;
    }
    
    /**
     * Setter for property acctLevelCustomerList.
     * @param acctLevelCustomerList New value of property acctLevelCustomerList.
     */
    public void setAcctLevelCustomerList(java.util.ArrayList acctLevelCustomerList) {
        this.acctLevelCustomerList = acctLevelCustomerList;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorizedDelete;
    private com.see.truetransact.uicomponent.CButton btnAuthorizedNew;
    private com.see.truetransact.uicomponent.CButton btnAuthorizedSave;
    private com.see.truetransact.uicomponent.CButton btnCustomerID;
    private com.see.truetransact.uicomponent.CButton btnInstructionDelete;
    private com.see.truetransact.uicomponent.CButton btnInstructionNew;
    private com.see.truetransact.uicomponent.CButton btnInstructionSave;
    private com.see.truetransact.uicomponent.CComboBox cboAddrCommunication_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CComboBox cboCity_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CComboBox cboCountry_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CComboBox cboState_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblAddrCommunication_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblAreaCode_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblArea_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblBusinessFax_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblBusinessPhone_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblCity_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblCountry_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblCustomerID;
    private com.see.truetransact.uicomponent.CLabel lblDesig_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblEmailId_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblFromAmount;
    private com.see.truetransact.uicomponent.CLabel lblHomeFax_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblHomePhone_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblInstruction;
    private com.see.truetransact.uicomponent.CLabel lblLimits;
    private com.see.truetransact.uicomponent.CLabel lblMobile_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblName_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblNumberAuthSignatory;
    private com.see.truetransact.uicomponent.CLabel lblPager_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblPin_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblState_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblStreet_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CLabel lblToAmount;
    private com.see.truetransact.uicomponent.CPanel panAuthorizedSignatory;
    private com.see.truetransact.uicomponent.CPanel panAuthorizedSignatory_Name;
    private com.see.truetransact.uicomponent.CPanel panAuthorizedSignatory_Name1;
    private com.see.truetransact.uicomponent.CPanel panAuthorizedSignatory_Name2;
    private com.see.truetransact.uicomponent.CPanel panAuthorizedTable;
    private com.see.truetransact.uicomponent.CPanel panAuthorizedToolBtns;
    private com.see.truetransact.uicomponent.CPanel panAuthorizedsignatory_Number;
    private com.see.truetransact.uicomponent.CPanel panInstruction;
    private com.see.truetransact.uicomponent.CPanel panInstructionFields;
    private com.see.truetransact.uicomponent.CPanel panInstructionTable;
    private com.see.truetransact.uicomponent.CPanel panInstructionToolBtns;
    private com.see.truetransact.uicomponent.CSeparator sptAuthorizedSignatory_Hori;
    private com.see.truetransact.uicomponent.CSeparator sptAuthorizedSignatory_Vert;
    private com.see.truetransact.uicomponent.CScrollPane srpAuthorizedTable;
    private com.see.truetransact.uicomponent.CScrollPane srpInstructionTable;
    private com.see.truetransact.uicomponent.CTable tblAuthorizedTable;
    private com.see.truetransact.uicomponent.CTable tblInstructionTable;
    private com.see.truetransact.uicomponent.CTextField txtAreaCode_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtArea_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtBusinessFax_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtBusinessPhone_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtCustomerID;
    private com.see.truetransact.uicomponent.CTextField txtDesig_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtEmailId_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtFromAmount;
    private com.see.truetransact.uicomponent.CTextField txtHomeFax_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtHomePhone_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtInstruction;
    private com.see.truetransact.uicomponent.CTextField txtLimits;
    private com.see.truetransact.uicomponent.CTextField txtMobile_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtName_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtNumberAuthSignatory;
    private com.see.truetransact.uicomponent.CTextField txtPager_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtPin_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtStreet_AuthorizedSignatory;
    private com.see.truetransact.uicomponent.CTextField txtToAmount;
    // End of variables declaration//GEN-END:variables
    
}

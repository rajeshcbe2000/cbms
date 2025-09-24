/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.
 * 
 *
 * ParameterUI.java
 *
 * Created on March 12, 2004, 5:49 PM
 */
package com.see.truetransact.ui.clearing;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Observer;
import java.util.Observable;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.uicomponent.CButtonGroup;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.uivalidation.NumericValidation;
import com.see.truetransact.uivalidation.CurrencyValidation;
import java.util.ResourceBundle;
import com.see.truetransact.clientproxy.ProxyParameters;

import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import java.util.Date;
/**
 *
 * @author  Prasath.T
 *modified by Ashok Vijayakumar
 */
public class ParameterUI extends com.see.truetransact.uicomponent.CInternalFrame implements java.util.Observer,UIMandatoryField {
    
    // Variables declaration - do not modify
    private ParameterOB observable;
    private HashMap mandatoryMap;
    private ResourceBundle resourceBundle = ResourceBundle.getBundle("com.see.truetransact.ui.clearing.ParameterRB", ProxyParameters.LANGUAGE);
    ParameterMRB objMandatoryRB;
    final int EDIT=0, DELETE=1,CLEA_HD=2,CLEA_SUSP_HD=3,OCRC_HD=4,INCR_HD=5, AUTHORIZE=6, VIEW =7;
    int viewType=-1;
    int DUPLICATION = 2;
    final int NO = 1,NOCHANGE=2;
    final String BRANCHID = TrueTransactMain.BRANCH_ID;
    private Date currDt = null;
    // End of variables declaration
    
    /** Creates new form ParameterUI */
    public ParameterUI() {
        initComponents();
        initStartUp();
    }
    /* methods invoked at the time of new form */
    private void initStartUp() {
        currDt = ClientUtil.getCurrentDate();
        setFieldNames();
        internationalize();
        setObservable();
        initComponentData();
        setMaximumLength();
        ClientUtil.enableDisable(this, false);
        setButtonEnableDisable();
        
        setMandatoryHashMap();
        setHelpMessage();
        observable.resetForm();
        clearBankParams();
    }
    
    /** Setting Observable to this InternalFrame */
    private void setObservable(){
        /* Implementing Singleton pattern */
        observable = ParameterOB.getInstance();
        observable.addObserver(this);
    }
    
    /* To enable or disable the main New Save Delete buttons  */
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
        btnView.setEnabled(!btnView.isEnabled());
    }
    // To set the maximun length of fields in the UI
    private void  setMaximumLength(){
        txtClearingHD.setMaxLength(32);
        //        txtOCReturnCharges.setMaxLength(16);
        txtOCReturnCharges.setValidation(new CurrencyValidation(14, 2));
        //        txtICReturnCharges.setMaxLength(16);
        txtICReturnCharges.setValidation(new CurrencyValidation(14, 2));
         txtLotSize.setValidation(new NumericValidation(5, 0));
        //txtLotSize.setValidation(new CurrencyValidation(14, 2));
        txtClearingSuspenseHD.setMaxLength(32);
        txtOCReturnChargesHD.setMaxLength(32);
        txtICReturnChargesHD.setMaxLength(32);
        txtServiceBranchCode.setMaxLength(32);
        //        txtValueofHighValueCheque.setMaxLength(16);
        txtValueofHighValueCheque.setValidation(new CurrencyValidation(14, 2));
        txtClearingFreq.setValidation(new NumericValidation(5, 0));
    }
    
    /** To set the txtValueofHighValueCheque enable or disable accoring to selection rdoHighValue_Yes */
    private void setTxtEnableDisable(boolean flag){
        txtValueofHighValueCheque.setEnabled(flag);
        txtValueofHighValueCheque.setEditable(flag);
    }
    
    /** To populate the BankRelated BankParmeters According to the ClearingType selected */
    public void populateBankParameters(String clearingType){
        HashMap resultMap = observable.getResultMap(clearingType);
        if(resultMap != null){
            txtClearingHD.setText(CommonUtil.convertObjToStr(resultMap.get("CLEARING_HD")));
            txtClearingSuspenseHD.setText(CommonUtil.convertObjToStr(resultMap.get("CLEARING_SUSPENSE_HD")));
            txtOCReturnCharges.setText(CommonUtil.convertObjToStr(resultMap.get("OUTWARD_RETURN_CHRG")));
            txtOCReturnChargesHD.setText(CommonUtil.convertObjToStr(resultMap.get("OUTWARD_RETURN_HD")));
            txtICReturnCharges.setText(CommonUtil.convertObjToStr(resultMap.get("INWARD_RETURN_CHRG")));
            txtICReturnChargesHD.setText(CommonUtil.convertObjToStr(resultMap.get("INWARD_RETURN_HD")));
        }
    }
    
    /* To remove the radio buttons */
    private void removeRadioButtons(){
        rdoHighValue.remove(rdoHighValue_Yes);
        rdoHighValue.remove(rdoHighValue_No);
    }
    
    /* To add the radio buttons */
    private void addRadioButtons(){
        rdoHighValue = new CButtonGroup();
        rdoHighValue.add(rdoHighValue_No);
        rdoHighValue.add(rdoHighValue_Yes);
    }
    
    /* Auto Generated Method - update()
   This method called by Observable. It updates the UI with
   Observable's data. If needed add/Remove RadioButtons
   method need to be added.*/
    public void update(Observable observed, Object arg) {
        removeRadioButtons();
        txtLotSize.setText(observable.getTxtLotSize());
        lblStatus.setText(observable.getLblStatus());
        cboClearingType.setSelectedItem(observable.getCboClearingType());
        rdoHighValue_Yes.setSelected(observable.getRdoHighValue_Yes());
        rdoHighValue_No.setSelected(observable.getRdoHighValue_No());
        txtServiceBranchCode.setText(observable.getTxtServiceBranchCode());
        txtValueofHighValueCheque.setText(observable.getTxtValueofHighValueCheque());
        txtClearingFreq.setText(observable.getTxtClearingFreq());
        cboClearingFreq.setSelectedItem(observable.getCboClearingFreq());
        addRadioButtons();
    }
    
   /* Auto Generated Method - setFieldNames()
   This method assigns name for all the components.
   Other functions are working based on this name. */
    private void setFieldNames() {
        panClearingHD.setName("panClearingHD");
        panClearingSuspenseHD.setName("panClearingSuspenseHD");
        panHighValueApplicability.setName("panHighValueApplicability");
        panICReturnChargesHD.setName("panICReturnChargesHD");
        panOCReturnChargesHD.setName("panOCReturnChargesHD");
        panParameter.setName("panParameter");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        
        btnAuthorize.setName("btnAuthorize");
        btnException.setName("btnException");
        btnReject.setName("btnReject");
        
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        lbSpace2.setName("lbSpace2");
        lblClearingHD.setName("lblClearingHD");
        lblClearingSuspenseHD.setName("lblClearingSuspenseHD");
        lblClearingType.setName("lblClearingType");
        lblHighValueApplicability.setName("lblHighValueApplicability");
        lblICReturnCharges.setName("lblICReturnCharges");
        lblICReturnChargesHD.setName("lblICReturnChargesHD");
        lblLotSize.setName("lblLotSize");
        lblMsg.setName("lblMsg");
        lblOCReturnCharges.setName("lblOCReturnCharges");
        lblOCReturnChargesHD.setName("lblOCReturnChargesHD");
        lblServiceBranchCode.setName("lblServiceBranchCode");
        lblSpace1.setName("lblSpace1");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblValueofHighValueCheque.setName("lblValueofHighValueCheque");
        mbrParameter.setName("mbrParameter");
        panStatus.setName("panStatus");
        rdoHighValue_No.setName("rdoHighValue_No");
        rdoHighValue_Yes.setName("rdoHighValue_Yes");
        txtClearingHD.setName("txtClearingHD");
        txtClearingSuspenseHD.setName("txtClearingSuspenseHD");
        txtICReturnCharges.setName("txtICReturnCharges");
        txtICReturnChargesHD.setName("txtICReturnChargesHD");
        txtLotSize.setName("txtLotSize");
        txtOCReturnCharges.setName("txtOCReturnCharges");
        txtOCReturnChargesHD.setName("txtOCReturnChargesHD");
        txtServiceBranchCode.setName("txtServiceBranchCode");
        txtValueofHighValueCheque.setName("txtValueofHighValueCheque");
        cboClearingType.setName("cboClearingType");
        panFrequency.setName("panFrequency");
        lblClearingFrequency.setName("lblClearingFrequency");
        txtClearingFreq.setName("txtClearingFreq");
        cboClearingFreq.setName("cboClearingFreq");
    }
/* Auto Generated Method - updateOBFields()
   This method called by Save option of UI.
   It updates the OB with UI data.*/
    public void updateOBFields() {
        observable.setModule(getModule());
        observable.setScreen(getScreen());
        observable.setTxtLotSize(txtLotSize.getText());
        observable.setRdoHighValue_Yes(rdoHighValue_Yes.isSelected());
        observable.setRdoHighValue_No(rdoHighValue_No.isSelected());
        observable.setCboClearingType((String)cboClearingType.getSelectedItem());
        observable.setTxtServiceBranchCode(txtServiceBranchCode.getText());
        observable.setTxtValueofHighValueCheque(txtValueofHighValueCheque.getText());
        observable.setCboClearingFreq((String)cboClearingFreq.getSelectedItem());
        observable.setTxtClearingFreq(txtClearingFreq.getText());
    }
    
        /* Auto Generated Method - setHelpMessage()
   This method shows tooltip help for all the input fields
   available in the UI. It needs the Mandatory Resource Bundle
   object. Help display Label name should be lblMsg. */
    public void setHelpMessage() {
        objMandatoryRB = new ParameterMRB();
        txtOCReturnCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOCReturnCharges"));
        txtICReturnCharges.setHelpMessage(lblMsg, objMandatoryRB.getString("txtICReturnCharges"));
        txtLotSize.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLotSize"));
        rdoHighValue_Yes.setHelpMessage(lblMsg, objMandatoryRB.getString("rdoHighValue_Yes"));
        cboClearingType.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClearingType"));
        txtClearingHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingHD"));
        txtServiceBranchCode.setHelpMessage(lblMsg, objMandatoryRB.getString("txtServiceBranchCode"));
        txtValueofHighValueCheque.setHelpMessage(lblMsg, objMandatoryRB.getString("txtValueofHighValueCheque"));
        txtClearingSuspenseHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingSuspenseHD"));
        txtOCReturnChargesHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtOCReturnChargesHD"));
        txtICReturnChargesHD.setHelpMessage(lblMsg, objMandatoryRB.getString("txtICReturnChargesHD"));
        txtClearingFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingFreq"));
        cboClearingFreq.setHelpMessage(lblMsg, objMandatoryRB.getString("cboClearingFreq"));
    }
    
    
/* Auto Generated Method - internationalize()
   This method used to assign display texts from
   the Resource Bundle File. */
    private void internationalize() {
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblClearingHD.setText(resourceBundle.getString("lblClearingHD"));
        rdoHighValue_Yes.setText(resourceBundle.getString("rdoHighValue_Yes"));
        lblOCReturnChargesHD.setText(resourceBundle.getString("lblOCReturnChargesHD"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblValueofHighValueCheque.setText(resourceBundle.getString("lblValueofHighValueCheque"));
        lblClearingType.setText(resourceBundle.getString("lblClearingType"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblClearingSuspenseHD.setText(resourceBundle.getString("lblClearingSuspenseHD"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        lbSpace2.setText(resourceBundle.getString("lbSpace2"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        lblHighValueApplicability.setText(resourceBundle.getString("lblHighValueApplicability"));
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        lblOCReturnCharges.setText(resourceBundle.getString("lblOCReturnCharges"));
        lblICReturnCharges.setText(resourceBundle.getString("lblICReturnCharges"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblServiceBranchCode.setText(resourceBundle.getString("lblServiceBranchCode"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        rdoHighValue_No.setText(resourceBundle.getString("rdoHighValue_No"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblLotSize.setText(resourceBundle.getString("lblLotSize"));
        lblICReturnChargesHD.setText(resourceBundle.getString("lblICReturnChargesHD"));
        lblClearingFrequency.setText(resourceBundle.getString("lblClearingFrequency"));
        
        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
        btnException.setText(resourceBundle.getString("btnException"));
        btnReject.setText(resourceBundle.getString("btnReject"));
    }
    
/* Auto Generated Method - setMandatoryHashMap()
   This method list out all the Input Fields available in the UI.
   It needs a class level HashMap variable mandatoryMap. */
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtOCReturnCharges", new Boolean(true));
        mandatoryMap.put("txtICReturnCharges", new Boolean(true));
        mandatoryMap.put("txtLotSize", new Boolean(true));
        mandatoryMap.put("rdoHighValue_Yes", new Boolean(true));
        mandatoryMap.put("cboClearingType", new Boolean(true));
        mandatoryMap.put("txtClearingHD", new Boolean(true));
        mandatoryMap.put("txtServiceBranchCode", new Boolean(true));
        mandatoryMap.put("txtValueofHighValueCheque", new Boolean(false));
        mandatoryMap.put("txtClearingSuspenseHD", new Boolean(true));
        mandatoryMap.put("txtOCReturnChargesHD", new Boolean(true));
        mandatoryMap.put("txtICReturnChargesHD", new Boolean(true));
    }
    
/* Auto Generated Method - getMandatoryHashMap()
   Getter method for setMandatoryHashMap().*/
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    /** To populate Comboboxes */
    private void initComponentData() {
        cboClearingType.setModel(observable.getCbmClearingType());
        cboClearingFreq.setModel(observable.getCbmClearingFreq());
    }
    
    /** To display a popUp window for viewing existing data */
    private void popUp(int field) {
        if( field == CLEA_HD || field == CLEA_SUSP_HD || field == OCRC_HD || field == INCR_HD ) {
            lblStatus.setText(ClientConstants.ACTION_STATUS[observable.getActionType()]);
        }else {
            lblStatus.setText(ClientConstants.ACTION_STATUS[0]);
        }
        viewType = field;
        final HashMap viewMap = new HashMap();
        if(field==EDIT || field==DELETE || field==VIEW){//Edit=0 and Delete=1
            viewMap.put(CommonConstants.MAP_NAME, "viewParameter");
            
            HashMap whereMap = new HashMap();
            whereMap.put("BRANCH_ID", BRANCHID);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            
            //mapped statement: viewParameter---> result map should be a Hashmap...
        }else {
            viewMap.put(CommonConstants.MAP_NAME, "getSelectAcctHeadTOList");
        }
        System.out.println("View Map: " + viewMap);
        new ViewAll(this, viewMap).show();
    }
    
    /** Called by the Popup window created thru popUp method
     * @param param
     */
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        final String accountHead=(String)hash.get("AC_HD_ID");
        if (viewType != -1) {
            /* In the Edit or Delete Mode */
            if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW) {
                hash.put("CLEARING_TYPE", hash.get("CLEARING_TYPE"));
                hash.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
                
                observable.populateData(hash);
                populateBankParameters(CommonUtil.convertObjToStr(hash.get("CLEARING_TYPE")));
                observable.setStatus();
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    ClientUtil.enableDisable(this, true);
                    setCboClearingTypeEnableDisable(false);
                } else if(observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW){
                    ClientUtil.enableDisable(this, false);
                }
                if(viewType==AUTHORIZE){
                    ClientUtil.enableDisable(this,false);
                }
                setButtonEnableDisable();
            }else if (viewType==CLEA_HD) {
                txtClearingHD.setText(accountHead);
            }else if (viewType==CLEA_SUSP_HD) {
                txtClearingSuspenseHD.setText(accountHead);
            }else if (viewType==OCRC_HD) {
                txtOCReturnChargesHD.setText(accountHead);
            }else if (viewType==INCR_HD) {
                txtICReturnChargesHD.setText(accountHead);
            }
        }
        setModified(true);
    }
    
    // action performed when save button is pressed
    private void savePerformed() {
        updateOBFields();
        observable.doAction();
        observable.setResultStatus();
        setModified(false);
        if(observable.getResult()!=ClientConstants.ACTIONTYPE_FAILED){
            ClientUtil.enableDisable(this, false);
            setButtonEnableDisable();
            observable.resetForm();
            clearBankParams();
        }
        
    }
    
    /** Clearing BankRelated TextFields */
    private void clearBankParams(){
        txtClearingHD.setText("");
        txtClearingSuspenseHD.setText("");
        txtOCReturnCharges.setText("");
        txtOCReturnChargesHD.setText("");
        txtICReturnCharges.setText("");
        txtICReturnChargesHD.setText("");
    }
    
    /* To set enable disable the CboClearingType */
    private void setCboClearingTypeEnableDisable(boolean flag) {
        cboClearingType.setEnabled(flag);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdoHighValue = new com.see.truetransact.uicomponent.CButtonGroup();
        rdoCompleteDay = new com.see.truetransact.uicomponent.CButtonGroup();
        panParameter = new com.see.truetransact.uicomponent.CPanel();
        lblClearingHD = new com.see.truetransact.uicomponent.CLabel();
        lblOCReturnCharges = new com.see.truetransact.uicomponent.CLabel();
        lblICReturnCharges = new com.see.truetransact.uicomponent.CLabel();
        lblLotSize = new com.see.truetransact.uicomponent.CLabel();
        lblHighValueApplicability = new com.see.truetransact.uicomponent.CLabel();
        txtOCReturnCharges = new com.see.truetransact.uicomponent.CTextField();
        txtICReturnCharges = new com.see.truetransact.uicomponent.CTextField();
        txtLotSize = new com.see.truetransact.uicomponent.CTextField();
        panHighValueApplicability = new com.see.truetransact.uicomponent.CPanel();
        rdoHighValue_Yes = new com.see.truetransact.uicomponent.CRadioButton();
        rdoHighValue_No = new com.see.truetransact.uicomponent.CRadioButton();
        lblClearingType = new com.see.truetransact.uicomponent.CLabel();
        panClearingHD = new com.see.truetransact.uicomponent.CPanel();
        txtClearingHD = new com.see.truetransact.uicomponent.CTextField();
        lblClearingSuspenseHD = new com.see.truetransact.uicomponent.CLabel();
        lblOCReturnChargesHD = new com.see.truetransact.uicomponent.CLabel();
        lblICReturnChargesHD = new com.see.truetransact.uicomponent.CLabel();
        lblServiceBranchCode = new com.see.truetransact.uicomponent.CLabel();
        lblValueofHighValueCheque = new com.see.truetransact.uicomponent.CLabel();
        txtServiceBranchCode = new com.see.truetransact.uicomponent.CTextField();
        txtValueofHighValueCheque = new com.see.truetransact.uicomponent.CTextField();
        panClearingSuspenseHD = new com.see.truetransact.uicomponent.CPanel();
        txtClearingSuspenseHD = new com.see.truetransact.uicomponent.CTextField();
        panOCReturnChargesHD = new com.see.truetransact.uicomponent.CPanel();
        txtOCReturnChargesHD = new com.see.truetransact.uicomponent.CTextField();
        panICReturnChargesHD = new com.see.truetransact.uicomponent.CPanel();
        txtICReturnChargesHD = new com.see.truetransact.uicomponent.CTextField();
        cboClearingType = new com.see.truetransact.uicomponent.CComboBox();
        lblClearingFrequency = new com.see.truetransact.uicomponent.CLabel();
        panFrequency = new com.see.truetransact.uicomponent.CPanel();
        txtClearingFreq = new com.see.truetransact.uicomponent.CTextField();
        cboClearingFreq = new com.see.truetransact.uicomponent.CComboBox();
        tbrParameter = new com.see.truetransact.uicomponent.CToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lbSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lbSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace23 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace24 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace25 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace26 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        mbrParameter = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptView = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Parameter");
        getContentPane().setLayout(new java.awt.BorderLayout(10, 10));

        panParameter.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panParameter.setLayout(new java.awt.GridBagLayout());

        lblClearingHD.setText("Clearing Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblClearingHD, gridBagConstraints);

        lblOCReturnCharges.setText("Outward Clearing Return Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblOCReturnCharges, gridBagConstraints);

        lblICReturnCharges.setText("Inward Clearing Return Charges");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblICReturnCharges, gridBagConstraints);

        lblLotSize.setText("Lot Size MICR Clearing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblLotSize, gridBagConstraints);

        lblHighValueApplicability.setText("High Value Applicability");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblHighValueApplicability, gridBagConstraints);

        txtOCReturnCharges.setEditable(false);
        txtOCReturnCharges.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtOCReturnCharges, gridBagConstraints);

        txtICReturnCharges.setEditable(false);
        txtICReturnCharges.setValidation(new CurrencyValidation());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtICReturnCharges, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtLotSize, gridBagConstraints);

        panHighValueApplicability.setLayout(new java.awt.GridBagLayout());

        rdoHighValue.add(rdoHighValue_Yes);
        rdoHighValue_Yes.setText("Yes");
        rdoHighValue_Yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHighValue_YesActionPerformed(evt);
            }
        });
        panHighValueApplicability.add(rdoHighValue_Yes, new java.awt.GridBagConstraints());

        rdoHighValue.add(rdoHighValue_No);
        rdoHighValue_No.setText("No");
        rdoHighValue_No.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoHighValue_NoActionPerformed(evt);
            }
        });
        panHighValueApplicability.add(rdoHighValue_No, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panParameter.add(panHighValueApplicability, gridBagConstraints);

        lblClearingType.setText("Clearing Type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblClearingType, gridBagConstraints);

        panClearingHD.setLayout(new java.awt.GridBagLayout());

        txtClearingHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingHD.add(txtClearingHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panClearingHD, gridBagConstraints);

        lblClearingSuspenseHD.setText("Clearing Suspense Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblClearingSuspenseHD, gridBagConstraints);

        lblOCReturnChargesHD.setText("Outward Clearing Return Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblOCReturnChargesHD, gridBagConstraints);

        lblICReturnChargesHD.setText("Inward Clearing Return Charges Head");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblICReturnChargesHD, gridBagConstraints);

        lblServiceBranchCode.setText("Service Branch Code");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblServiceBranchCode, gridBagConstraints);

        lblValueofHighValueCheque.setText("Value of High Value Cheque");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(lblValueofHighValueCheque, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtServiceBranchCode, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(txtValueofHighValueCheque, gridBagConstraints);

        panClearingSuspenseHD.setLayout(new java.awt.GridBagLayout());

        txtClearingSuspenseHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panClearingSuspenseHD.add(txtClearingSuspenseHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panClearingSuspenseHD, gridBagConstraints);

        panOCReturnChargesHD.setLayout(new java.awt.GridBagLayout());

        txtOCReturnChargesHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panOCReturnChargesHD.add(txtOCReturnChargesHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panOCReturnChargesHD, gridBagConstraints);

        panICReturnChargesHD.setLayout(new java.awt.GridBagLayout());

        txtICReturnChargesHD.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panICReturnChargesHD.add(txtICReturnChargesHD, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        panParameter.add(panICReturnChargesHD, gridBagConstraints);

        cboClearingType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboClearingTypeItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panParameter.add(cboClearingType, gridBagConstraints);

        lblClearingFrequency.setText("Clearing Frequency");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        panParameter.add(lblClearingFrequency, gridBagConstraints);

        txtClearingFreq.setPreferredSize(new java.awt.Dimension(50, 21));
        txtClearingFreq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClearingFreqFocusLost(evt);
            }
        });
        panFrequency.add(txtClearingFreq);

        cboClearingFreq.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cboClearingFreqFocusLost(evt);
            }
        });
        panFrequency.add(cboClearingFreq);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panParameter.add(panFrequency, gridBagConstraints);

        getContentPane().add(panParameter, java.awt.BorderLayout.CENTER);

        btnView.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_Summary.gif"))); // NOI18N
        btnView.setToolTipText("Enquiry");
        btnView.setMinimumSize(new java.awt.Dimension(21, 21));
        btnView.setPreferredSize(new java.awt.Dimension(21, 21));
        btnView.setEnabled(false);
        btnView.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewActionPerformed(evt);
            }
        });
        tbrParameter.add(btnView);

        lbSpace3.setText("     ");
        tbrParameter.add(lbSpace3);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrParameter.add(btnNew);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace21);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrParameter.add(btnEdit);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace22);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrParameter.add(btnDelete);

        lbSpace2.setText("     ");
        tbrParameter.add(lbSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrParameter.add(btnSave);

        lblSpace23.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace23.setText("     ");
        lblSpace23.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace23.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace23);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrParameter.add(btnCancel);

        lblSpace3.setText("     ");
        tbrParameter.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrParameter.add(btnAuthorize);

        lblSpace24.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace24.setText("     ");
        lblSpace24.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace24.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace24);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrParameter.add(btnException);

        lblSpace25.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace25.setText("     ");
        lblSpace25.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace25.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace25);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrParameter.add(btnReject);

        lblSpace5.setText("     ");
        tbrParameter.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        tbrParameter.add(btnPrint);

        lblSpace26.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace26.setText("     ");
        lblSpace26.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace26.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrParameter.add(lblSpace26);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrParameter.add(btnClose);

        getContentPane().add(tbrParameter, java.awt.BorderLayout.NORTH);

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
        mnuProcess.add(sptView);

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

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrParameter.add(mnuProcess);

        setJMenuBar(mbrParameter);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnViewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_VIEW);
        observable.setStatus();
        lblStatus.setText(observable.getLblStatus());
        popUp(VIEW);
        btnCheck();
    }//GEN-LAST:event_btnViewActionPerformed
    
    private void cboClearingFreqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cboClearingFreqFocusLost
        // TODO add your handling code here:
//        if(txtClearingFreq.getText().length()!=0 && observable.getCbmClearingFreq().getKeyForSelected()!=null){
//            ClientUtil.validPeriodMaxLength(txtClearingFreq, CommonUtil.convertObjToStr(observable.getCbmClearingFreq().getKeyForSelected()));
//        }
    }//GEN-LAST:event_cboClearingFreqFocusLost
    private String periodLengthValidation(){
        ParameterMRB objMandatoryRB = new ParameterMRB();
        StringBuffer stbAlert = new StringBuffer();
        String strPeriodType = CommonUtil.convertObjToStr(((ComboBoxModel) cboClearingFreq.getModel()).getKeyForSelected());
        if (strPeriodType.length() > 0 && (!ClientUtil.validPeriodMaxLength(txtClearingFreq, strPeriodType))){
            stbAlert.append("\n");
            stbAlert.append(objMandatoryRB.getString(txtClearingFreq.getName()));
        }
        return stbAlert.toString();
    }
    private void txtClearingFreqFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClearingFreqFocusLost
        // TODO add your handling code here:
        periodLengthValidation();
    }//GEN-LAST:event_txtClearingFreqFocusLost
    
    private void rdoHighValue_YesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHighValue_YesActionPerformed
        // TODO add your handling code here:
        setTxtEnableDisable(true);
    }//GEN-LAST:event_rdoHighValue_YesActionPerformed
    
    private void rdoHighValue_NoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoHighValue_NoActionPerformed
        // TODO add your handling code here:
        txtValueofHighValueCheque.setText("");
        setTxtEnableDisable(false);
    }//GEN-LAST:event_rdoHighValue_NoActionPerformed
    
    private void cboClearingTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboClearingTypeItemStateChanged
        // TODO add your handling code here:
        if (observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ){
            DUPLICATION = observable.checkDuplication(CommonUtil.convertObjToStr(cboClearingType.getSelectedItem()));
            if ((DUPLICATION == NO ||DUPLICATION == NOCHANGE)){
                populateBankParameters(CommonUtil.convertObjToStr(cboClearingType.getSelectedItem()));
            }else{
                clearBankParams();
            }
        }
        
    }//GEN-LAST:event_cboClearingTypeItemStateChanged
    
    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EXCEPTION);
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed
    
    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_REJECT);
        authorizeStatus(CommonConstants.STATUS_REJECTED);
    }//GEN-LAST:event_btnRejectActionPerformed
    
    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_AUTHORIZE);
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
    }//GEN-LAST:event_btnAuthorizeActionPerformed
    /** Does the neceessary Operation for Authorization of Selected Row in AuthorizeStatusUI Screen */
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE){
            viewType = AUTHORIZE;
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getClearingParamAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeClearingParam");
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            whereMap = null;
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            //            observable.setStatus();
            btnCancel.setEnabled(true);
            btnSave.setEnabled(false);
        } else if (viewType == AUTHORIZE){
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("CLEARING_TYPE", CommonUtil.convertObjToStr(cboClearingType.getSelectedItem()));
            singleAuthorizeMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            singleAuthorizeMap.put("AUTHORIZE_DT", currDt.clone());
            ClientUtil.execute("authorizeClearingParam", singleAuthorizeMap);
            if (authorizeStatus.equals(CommonConstants.STATUS_AUTHORIZED)){
                observable.setResult(ClientConstants.ACTIONTYPE_AUTHORIZE);
            }else if (authorizeStatus.equals(CommonConstants.STATUS_REJECTED)){
                observable.setResult(ClientConstants.ACTIONTYPE_REJECT);
            }else if (authorizeStatus.equals(CommonConstants.STATUS_EXCEPTION)){
                observable.setResult(ClientConstants.ACTIONTYPE_EXCEPTION);
            }
            observable.setResultStatus();
            viewType = -1;
            clearScreen();
        }
    }
    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // Add your handling code here:
        btnCloseActionPerformed(evt);
    }//GEN-LAST:event_mitCloseActionPerformed
    
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
    
    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // Add your handling code here:
        cifClosingAlert();
    }//GEN-LAST:event_btnCloseActionPerformed
    
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);
        observable.setStatus();
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        clearScreen();
    }//GEN-LAST:event_btnCancelActionPerformed
    private void clearScreen(){
        observable.resetForm();
        clearBankParams();
        ClientUtil.enableDisable(this,false);
        setButtonEnableDisable();
        setModified(false);
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panParameter);
        StringBuffer message = new StringBuffer(mandatoryMessage);
        if(rdoHighValue_Yes.isSelected()){
            if(txtValueofHighValueCheque.getText().length() == 0 || txtValueofHighValueCheque.getText().equals("")){
                message.append(objMandatoryRB.getString("txtValueofHighValueCheque"));
            }
        }
        message.append(periodLengthValidation());
        if (message.length() > 0){
            displayAlert(message.toString());
        }else{
            savePerformed();
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
    }//GEN-LAST:event_btnSaveActionPerformed
    /* To display an alert message if any of the mandatory fields is not inputed */
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.setModal(true);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        ClientUtil.enableDisable(this,false);
        
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);
        popUp(EDIT);
        if(observable.getRdoHighValue_Yes()== true) {
            setTxtEnableDisable(true);
        }else if(observable.getRdoHighValue_No()== true) {
            setTxtEnableDisable(false);
        }
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
    
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();
        clearBankParams();
        ClientUtil.enableDisable(this,true);
        // In the following code true is changed as false by Rajesh
        setTxtEnableDisable(false);
        setDefaultValWhenNewBtnPressed();
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);
        setButtonEnableDisable();
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        observable.setStatus();
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    private void setDefaultValWhenNewBtnPressed(){
        rdoHighValue_No.setSelected(true);
        observable.setRdoHighValue_No(true);
    }
    
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnView;
    private com.see.truetransact.uicomponent.CComboBox cboClearingFreq;
    private com.see.truetransact.uicomponent.CComboBox cboClearingType;
    private com.see.truetransact.uicomponent.CLabel lbSpace2;
    private com.see.truetransact.uicomponent.CLabel lbSpace3;
    private com.see.truetransact.uicomponent.CLabel lblClearingFrequency;
    private com.see.truetransact.uicomponent.CLabel lblClearingHD;
    private com.see.truetransact.uicomponent.CLabel lblClearingSuspenseHD;
    private com.see.truetransact.uicomponent.CLabel lblClearingType;
    private com.see.truetransact.uicomponent.CLabel lblHighValueApplicability;
    private com.see.truetransact.uicomponent.CLabel lblICReturnCharges;
    private com.see.truetransact.uicomponent.CLabel lblICReturnChargesHD;
    private com.see.truetransact.uicomponent.CLabel lblLotSize;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblOCReturnCharges;
    private com.see.truetransact.uicomponent.CLabel lblOCReturnChargesHD;
    private com.see.truetransact.uicomponent.CLabel lblServiceBranchCode;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace23;
    private com.see.truetransact.uicomponent.CLabel lblSpace24;
    private com.see.truetransact.uicomponent.CLabel lblSpace25;
    private com.see.truetransact.uicomponent.CLabel lblSpace26;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblValueofHighValueCheque;
    private com.see.truetransact.uicomponent.CMenuBar mbrParameter;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panClearingHD;
    private com.see.truetransact.uicomponent.CPanel panClearingSuspenseHD;
    private com.see.truetransact.uicomponent.CPanel panFrequency;
    private com.see.truetransact.uicomponent.CPanel panHighValueApplicability;
    private com.see.truetransact.uicomponent.CPanel panICReturnChargesHD;
    private com.see.truetransact.uicomponent.CPanel panOCReturnChargesHD;
    private com.see.truetransact.uicomponent.CPanel panParameter;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private com.see.truetransact.uicomponent.CButtonGroup rdoCompleteDay;
    private com.see.truetransact.uicomponent.CButtonGroup rdoHighValue;
    private com.see.truetransact.uicomponent.CRadioButton rdoHighValue_No;
    private com.see.truetransact.uicomponent.CRadioButton rdoHighValue_Yes;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptView;
    private com.see.truetransact.uicomponent.CToolBar tbrParameter;
    private com.see.truetransact.uicomponent.CTextField txtClearingFreq;
    private com.see.truetransact.uicomponent.CTextField txtClearingHD;
    private com.see.truetransact.uicomponent.CTextField txtClearingSuspenseHD;
    private com.see.truetransact.uicomponent.CTextField txtICReturnCharges;
    private com.see.truetransact.uicomponent.CTextField txtICReturnChargesHD;
    private com.see.truetransact.uicomponent.CTextField txtLotSize;
    private com.see.truetransact.uicomponent.CTextField txtOCReturnCharges;
    private com.see.truetransact.uicomponent.CTextField txtOCReturnChargesHD;
    private com.see.truetransact.uicomponent.CTextField txtServiceBranchCode;
    private com.see.truetransact.uicomponent.CTextField txtValueofHighValueCheque;
    // End of variables declaration//GEN-END:variables
    
}

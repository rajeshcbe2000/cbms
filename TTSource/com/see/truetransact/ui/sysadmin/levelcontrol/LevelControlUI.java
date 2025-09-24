/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * LevelControlUI.java
 *
 * Created on March 2, 2004, 11:44 AM
 */

package com.see.truetransact.ui.sysadmin.levelcontrol;

import  com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientproxy.ProxyParameters;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.Date;
import org.apache.log4j.Logger;
/**
 *
 * @author  rahul
 *maodified by Ashok Vijayakumar
 *@modified : Sunil
 *      Added Edit Locking - 07-07-2005
 */
public class LevelControlUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {
    private HashMap mandatoryMap;
    LevelControlOB observable;
    
    final int EDIT=0, DELETE=1,AUTHORIZE = 2,VIEW=4;
    int viewType=-1;
    boolean isFilled = false;
    //Logger
    private final static Logger log = Logger.getLogger(LevelControlUI.class);
    private Date currDt = null;
    //    private LevelControlRB resourceBundle = new LevelControlRB();
    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.sysadmin.levelcontrol.LevelControlRB", ProxyParameters.LANGUAGE);
    /** Creates new form LevelControlUI */
    public LevelControlUI() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        initSetup();
    }
    
    private void initSetup(){
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        initComponentData();
        setMaxLenths();// To set the Numeric Validation and the Maximum length of the Text fields...
         new MandatoryCheck().putMandatoryMarks(getClass().getName(),panLevelControl);
        ClientUtil.enableDisable(this, false);// Disables all when the screen appears for the 1st time
        setButtonEnableDisable();// Enables/Disables the necessary buttons and menu items...
        observable.resetForm();// To reset all the fields in UI...
        observable.resetStatus();// To reset the Satus in the UI...
        setHelpMessage();
    }
     
    // Creates The Instance of InwardClearingOB
    private void setObservable() {
        observable = LevelControlOB.getInstance();
        observable.addObserver(this);
    }
    
    private void setFieldNames() {
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnSave.setName("btnSave");
        lblCash.setName("lblCash");
        lblClearing.setName("lblClearing");
        lblCredit.setName("lblCredit");
        lblDebit.setName("lblDebit");
        lblDescription.setName("lblDescription");
        lblLevelID.setName("lblLevelID");
        lblMsg.setName("lblMsg");
        lblName.setName("lblName");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblStatus.setName("lblStatus");
        lblTransfer.setName("lblTransfer");
        mbrMain.setName("mbrMain");
        panData.setName("panData");
        panLevel.setName("panLevel");
        panLevelControl.setName("panLevelControl");
        panStatus.setName("panStatus");
        txtCashCredit.setName("txtCashCredit");
        txtCashDebit.setName("txtCashDebit");
        txtClearingCredit.setName("txtClearingCredit");
        txtClearingDebit.setName("txtClearingDebit");
        txtDescription.setName("txtDescription");
        txtLevelID.setName("txtLevelID");
        txtTransferCredit.setName("txtTransferCredit");
        txtTransferDebit.setName("txtTransferDebit");
        
        lblSingleWindow.setName("lblSingleWindow");
        chkSingleWindow.setName("chkSingleWindow");
        cboName.setName("cboName");
    }
     private void initComponentData() {
        cboName.setModel(observable.getCbmName());
    }
    
    private void internationalize() {
        btnDelete.setText(resourceBundle.getString("btnDelete"));
        btnClose.setText(resourceBundle.getString("btnClose"));
        lblName.setText(resourceBundle.getString("lblName"));
        lblCredit.setText(resourceBundle.getString("lblCredit"));
        btnEdit.setText(resourceBundle.getString("btnEdit"));
        lblMsg.setText(resourceBundle.getString("lblMsg"));
        btnNew.setText(resourceBundle.getString("btnNew"));
        lblDebit.setText(resourceBundle.getString("lblDebit"));
        ((javax.swing.border.TitledBorder)panData.getBorder()).setTitle(resourceBundle.getString("panData"));
        lblSpace2.setText(resourceBundle.getString("lblSpace2"));
        btnSave.setText(resourceBundle.getString("btnSave"));
        lblTransfer.setText(resourceBundle.getString("lblTransfer"));
        btnCancel.setText(resourceBundle.getString("btnCancel"));
        lblSpace3.setText(resourceBundle.getString("lblSpace3"));
        lblStatus.setText(resourceBundle.getString("lblStatus"));
        lblDescription.setText(resourceBundle.getString("lblDescription"));
        lblLevelID.setText(resourceBundle.getString("lblLevelID"));
        lblSpace1.setText(resourceBundle.getString("lblSpace1"));
        btnPrint.setText(resourceBundle.getString("btnPrint"));
        lblClearing.setText(resourceBundle.getString("lblClearing"));
        lblCash.setText(resourceBundle.getString("lblCash"));    
        lblSingleWindow.setText(resourceBundle.getString("lblSingleWindow"));
    }
    
    public void setMandatoryHashMap() {
        mandatoryMap = new HashMap();
        mandatoryMap.put("txtLevelID", new Boolean(true));
        mandatoryMap.put("txtName", new Boolean(true));
        mandatoryMap.put("txtDescription", new Boolean(true));
        mandatoryMap.put("txtCashCredit", new Boolean(true));
        mandatoryMap.put("txtCashDebit", new Boolean(true));
        mandatoryMap.put("txtTransferCredit", new Boolean(true));
        mandatoryMap.put("txtTransferDebit", new Boolean(true));
        mandatoryMap.put("txtClearingCredit", new Boolean(true));
        mandatoryMap.put("txtClearingDebit", new Boolean(true));
        mandatoryMap.put("chkSingleWindow", new Boolean(true));
        mandatoryMap.put("cboName", new Boolean(true));
        
    }
    
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }
    
    public void update(Observable observed, Object arg) {
        txtLevelID.setText(observable.getTxtLevelID());
        cboName.setSelectedItem(observable.getCboName());
        txtDescription.setText(observable.getTxtDescription());
        txtCashCredit.setText(observable.getTxtCashCredit());
        txtCashDebit.setText(observable.getTxtCashDebit());
        txtTransferCredit.setText(observable.getTxtTransferCredit());
        txtTransferDebit.setText(observable.getTxtTransferDebit());
        txtClearingCredit.setText(observable.getTxtClearingCredit());
        txtClearingDebit.setText(observable.getTxtClearingDebit());
        chkSingleWindow.setSelected(observable.getChkSingleWindow());
        
        //To set the Status...
        lblStatus.setText(observable.getLblStatus());
    }
    
    public void updateOBFields() {
        observable.setTxtLevelID(txtLevelID.getText());
        observable.setCboName((String)(((ComboBoxModel)(cboName).getModel())).getKeyForSelected());
        observable.setTxtDescription(txtDescription.getText());
        observable.setTxtCashCredit(txtCashCredit.getText());
        observable.setTxtCashDebit(txtCashDebit.getText());
        observable.setTxtTransferCredit(txtTransferCredit.getText());
        observable.setTxtTransferDebit(txtTransferDebit.getText());
        observable.setTxtClearingCredit(txtClearingCredit.getText());
        observable.setTxtClearingDebit(txtClearingDebit.getText());
        
        observable.setChkSingleWindow(chkSingleWindow.isSelected());
    }
    
    public void setHelpMessage() {
        LevelControlMRB objMandatoryRB = new LevelControlMRB();
        txtLevelID.setHelpMessage(lblMsg, objMandatoryRB.getString("txtLevelID"));
        txtDescription.setHelpMessage(lblMsg, objMandatoryRB.getString("txtDescription"));
        txtCashCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashCredit"));
        txtCashDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtCashDebit"));
        txtTransferCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransferCredit"));
        txtTransferDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtTransferDebit"));
        txtClearingCredit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingCredit"));
        txtClearingDebit.setHelpMessage(lblMsg, objMandatoryRB.getString("txtClearingDebit"));
        chkSingleWindow.setHelpMessage(lblMsg, objMandatoryRB.getString("chkSingleWindow"));
    }
    
    //To set the length of the Text Fields and Numeric Validation...
    private void setMaxLenths() {
        txtLevelID.setMaxLength(16);
        txtDescription.setMaxLength(256);
        
        txtCashCredit.setMaxLength(16);
        
        txtCashDebit.setMaxLength(16);
        
        txtTransferCredit.setMaxLength(16);
        
        txtTransferDebit.setMaxLength(16);
        
        txtClearingCredit.setMaxLength(16);
        
        txtClearingDebit.setMaxLength(16);
        
    }
    
    // To set The Value of the Buttons Depending on the Value or Condition...
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
    
    /** Method used to do Required operation when user clicks btnAuthorize,btnReject or btnReject **/
    public void authorizeStatus(String authorizeStatus) {
        if(viewType != AUTHORIZE && !isFilled){
            viewType = AUTHORIZE;
            //__ To Save the data in the Internal Frame...
            setModified(true);
            
            HashMap mapParam = new HashMap();
            
            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);
            
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
            mapParam.put(CommonConstants.MAP_NAME, "getLevelMasterAuthorizeList");
            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeLevelMaster");
            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);
            
            //__ If there's no data to be Authorized, call Cancel action...
            if(!isModified()){
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
            
        } else{
            HashMap singleAuthorizeMap = new HashMap();
            singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
            singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            singleAuthorizeMap.put("LEVEL ID", txtLevelID.getText());
            singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, currDt);
            
            ClientUtil.execute("authorizeLevelMaster", singleAuthorizeMap);
            viewType = -1;
            isFilled = false;
            
            btnSave.setEnabled(true);
            btnCancelActionPerformed(null);
        }
    }
    
    /** This will show the alertwindow **/
    private int showAlertWindow(String alertMsg) throws Exception {
        int optionSelected = 1;
        String[] options = {resourceBundle.getString("cDialogOK")};
        optionSelected = COptionPane.showOptionDialog(null,alertMsg, CommonConstants.INFORMATIONTITLE,
        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
        null, options, options[0]);
        return optionSelected;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panLevelControl = new com.see.truetransact.uicomponent.CPanel();
        panLevel = new com.see.truetransact.uicomponent.CPanel();
        lblLevelID = new com.see.truetransact.uicomponent.CLabel();
        txtLevelID = new com.see.truetransact.uicomponent.CTextField();
        lblName = new com.see.truetransact.uicomponent.CLabel();
        lblDescription = new com.see.truetransact.uicomponent.CLabel();
        txtDescription = new com.see.truetransact.uicomponent.CTextField();
        lblSingleWindow = new com.see.truetransact.uicomponent.CLabel();
        chkSingleWindow = new com.see.truetransact.uicomponent.CCheckBox();
        cboName = new com.see.truetransact.uicomponent.CComboBox();
        panData = new com.see.truetransact.uicomponent.CPanel();
        lblCredit = new com.see.truetransact.uicomponent.CLabel();
        lblDebit = new com.see.truetransact.uicomponent.CLabel();
        lblCash = new com.see.truetransact.uicomponent.CLabel();
        txtCashCredit = new com.see.truetransact.uicomponent.CTextField();
        txtCashDebit = new com.see.truetransact.uicomponent.CTextField();
        lblTransfer = new com.see.truetransact.uicomponent.CLabel();
        txtTransferCredit = new com.see.truetransact.uicomponent.CTextField();
        txtTransferDebit = new com.see.truetransact.uicomponent.CTextField();
        lblClearing = new com.see.truetransact.uicomponent.CLabel();
        txtClearingCredit = new com.see.truetransact.uicomponent.CTextField();
        txtClearingDebit = new com.see.truetransact.uicomponent.CTextField();
        tbrHead = new javax.swing.JToolBar();
        btnView = new com.see.truetransact.uicomponent.CButton();
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnNew = new com.see.truetransact.uicomponent.CButton();
        lblSpace17 = new com.see.truetransact.uicomponent.CLabel();
        btnEdit = new com.see.truetransact.uicomponent.CButton();
        lblSpace18 = new com.see.truetransact.uicomponent.CLabel();
        btnDelete = new com.see.truetransact.uicomponent.CButton();
        lblSpace2 = new com.see.truetransact.uicomponent.CLabel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        lblSpace19 = new com.see.truetransact.uicomponent.CLabel();
        btnCancel = new com.see.truetransact.uicomponent.CButton();
        lblSpace3 = new com.see.truetransact.uicomponent.CLabel();
        btnAuthorize = new com.see.truetransact.uicomponent.CButton();
        lblSpace20 = new com.see.truetransact.uicomponent.CLabel();
        btnException = new com.see.truetransact.uicomponent.CButton();
        lblSpace21 = new com.see.truetransact.uicomponent.CLabel();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        lblSpace5 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace22 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
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
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        panLevelControl.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panLevelControl.setLayout(new java.awt.GridBagLayout());

        panLevel.setLayout(new java.awt.GridBagLayout());

        lblLevelID.setText("Level ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblLevelID, gridBagConstraints);

        txtLevelID.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(txtLevelID, gridBagConstraints);

        lblName.setText("Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblName, gridBagConstraints);

        lblDescription.setText("Description");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblDescription, gridBagConstraints);

        txtDescription.setMinimumSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(txtDescription, gridBagConstraints);

        lblSingleWindow.setText("Single Window");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevel.add(lblSingleWindow, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 0, 4, 4);
        panLevel.add(chkSingleWindow, gridBagConstraints);

        cboName.setMinimumSize(new java.awt.Dimension(100, 21));
        cboName.setPopupWidth(225);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panLevel.add(cboName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panLevelControl.add(panLevel, gridBagConstraints);

        panData.setBorder(javax.swing.BorderFactory.createTitledBorder("Maximum Limit"));
        panData.setLayout(new java.awt.GridBagLayout());

        lblCredit.setText("Credit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCredit, gridBagConstraints);

        lblDebit.setText("Debit");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblDebit, gridBagConstraints);

        lblCash.setText("Cash");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblCash, gridBagConstraints);

        txtCashCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCashCredit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtCashCredit, gridBagConstraints);

        txtCashDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtCashDebit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtCashDebit, gridBagConstraints);

        lblTransfer.setText("Transfer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblTransfer, gridBagConstraints);

        txtTransferCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransferCredit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtTransferCredit, gridBagConstraints);

        txtTransferDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtTransferDebit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtTransferDebit, gridBagConstraints);

        lblClearing.setText("Clearing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(lblClearing, gridBagConstraints);

        txtClearingCredit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtClearingCredit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtClearingCredit, gridBagConstraints);

        txtClearingDebit.setMinimumSize(new java.awt.Dimension(100, 21));
        txtClearingDebit.setValidation(new CurrencyValidation(14,2));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panData.add(txtClearingDebit, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 8, 4, 4);
        panLevelControl.add(panData, gridBagConstraints);

        getContentPane().add(panLevelControl, java.awt.BorderLayout.CENTER);

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
        tbrHead.add(btnView);

        lblSpace4.setText("     ");
        tbrHead.add(lblSpace4);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrHead.add(btnNew);

        lblSpace17.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace17.setText("     ");
        lblSpace17.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace17.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace17);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrHead.add(btnEdit);

        lblSpace18.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace18.setText("     ");
        lblSpace18.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace18.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace18);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrHead.add(btnDelete);

        lblSpace2.setText("     ");
        tbrHead.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrHead.add(btnSave);

        lblSpace19.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace19.setText("     ");
        lblSpace19.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace19.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace19);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrHead.add(btnCancel);

        lblSpace3.setText("     ");
        tbrHead.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrHead.add(btnAuthorize);

        lblSpace20.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace20.setText("     ");
        lblSpace20.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace20.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace20);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrHead.add(btnException);

        lblSpace21.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace21.setText("     ");
        lblSpace21.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace21.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace21);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrHead.add(btnReject);

        lblSpace5.setText("     ");
        tbrHead.add(lblSpace5);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrHead.add(btnPrint);

        lblSpace22.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace22.setText("     ");
        lblSpace22.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace22.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrHead.add(lblSpace22);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrHead.add(btnClose);

        getContentPane().add(tbrHead, java.awt.BorderLayout.NORTH);

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

        mitClose.setText("Close");
        mitClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCloseActionPerformed(evt);
            }
        });
        mnuProcess.add(mitClose);

        mbrMain.add(mnuProcess);

        setJMenuBar(mbrMain);

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
        super.removeEditLock(txtLevelID.getText());
        observable.resetForm();                 // Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, false);  // Disables the panel...
        setButtonEnableDisable();               // Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 // To set the Value of lblStatus...
        
        viewType = -1;
        isFilled = false;
        
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnCancelActionPerformed
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // Add your handling code here:
        try{
            updateOBFields();
            //To display an alert if the mandatory fields are not properly inputted, else proceed with normal operation
            final String mandatoryMessage = new MandatoryCheck().checkMandatory(getClass().getName(), panLevelControl);
            if( observable.getActionType() != ClientConstants.ACTIONTYPE_DELETE && mandatoryMessage.length() > 0 ){
                displayAlert(mandatoryMessage);
            }
            else{
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_NEW ||
                observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT){
                    /** Checking if the amount entered in txtCashCredtit, txtCashDebit, txtTransferDebit, txtTransferCredit,txtClearingCredit,txtClearingDebit
                     *already exists, if so giving an alert message */
                    //                    if(observable.dataExists()){
                    //                        showAlertWindow("dataExistsMsg");
                    //                        return;
                    //                    }
                    HashMap resultMap = observable.dataExists();
                    if(resultMap.containsKey("LEVEL_ID")){
                       StringBuffer str = new StringBuffer();
                       str.append(resourceBundle.getString("dataExistsMsg") + "\n\n");
                       str.append(resourceBundle.getString("lblLevelID") +  ": " + CommonUtil.convertObjToStr(resultMap.get("LEVEL_ID")) + "\n");
                       str.append(resourceBundle.getString("lblName") +  ": " + CommonUtil.convertObjToStr(resultMap.get("LEVEL_NAME")) + "\n");
                       str.append(resourceBundle.getString("lblDescription") + ": " + CommonUtil.convertObjToStr(resultMap.get("LEVEL_DESC")));
                       showAlertWindow(str.toString());
                        return;
                    }
                }
                observable.doAction();                // To perform the necessary operation depending on the Action type...
                if(observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT)
                    super.removeEditLock(txtLevelID.getText());
                observable.resetForm();               // Reset the fields in the UI to null...
                ClientUtil.enableDisable(this, false);// Disables the panel...
                setButtonEnableDisable();             // Enables or Disables the buttons and menu Items depending on their previous state...
                observable.setResultStatus();         // To Reset the Value of lblStatus...
            }
        }catch(Exception e){
            System.out.println("Exception in btnCancelActionPerformed()");
        }
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnException.setEnabled(true);
        //__ Make the Screen Closable..
        setModified(false);
    }//GEN-LAST:event_btnSaveActionPerformed
    private void displayAlert(String message){
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // Add your handling code here:
        observable.resetForm();         // Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE);//Sets the Action Type to be performed...
        popUp(DELETE);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnDeleteActionPerformed
    
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // Add your handling code here:
        observable.resetForm();     // Reset the fields in the UI to null...
        observable.setActionType(ClientConstants.ACTIONTYPE_EDIT);//Sets the Action Type to be performed...
        popUp(EDIT);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
    }//GEN-LAST:event_btnEditActionPerformed
   
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

    
    // To display the All the Product Id's which r having status as
    // created or updated, in a table...
    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        if(field==EDIT || field==DELETE || field==VIEW){  
            //Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("LEVEL ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_NAME, "viewLevelControl");
        }
        new ViewAll(this, viewMap).show();
    }
    
    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        if (viewType==EDIT || viewType==DELETE || viewType==AUTHORIZE || viewType==VIEW) {
            isFilled = true;
            final String LEVELID = CommonUtil.convertObjToStr(hash.get("LEVEL ID"));
            hash.put(CommonConstants.MAP_WHERE, LEVELID);
            System.out.println("Hash: " + hash);
            
            observable.populateData(hash);// Called to display the Data in the UI fields...
            
            // To set the Value of Level Id...
            //            txtLevelID.setText(CommonUtil.convertObjToStr(hash.get("LEVEL_ID")));
            //            observable.setTxtLevelID(LEVELID);
            
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType==AUTHORIZE || observable.getActionType() == ClientConstants.ACTIONTYPE_VIEW) {
                ClientUtil.enableDisable(this, false);// Disables the panel...
            }else{
                ClientUtil.enableDisable(this, true);// Enables the panel...
                txtLevelID.setEditable(false);
            }
            setButtonEnableDisable();// Enables or Disables the buttons and menu Items depending on their previous state...
             if(viewType == AUTHORIZE){
             btnAuthorize.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_AUTHORIZE ? true : false);
             btnReject.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_REJECT ? true : false);
             btnException.setEnabled(observable.getActionType() == ClientConstants.ACTIONTYPE_EXCEPTION ? true : false);
            }
            observable.setStatus();// To set the Value of lblStatus...
        }
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }
    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // Add your handling code here:
        observable.resetForm();          //__ Reset the fields in the UI to null...
        ClientUtil.enableDisable(this, true); //__ Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);  //__ Sets the Action Type to be performed...
        setButtonEnableDisable();          //__ Enables or Disables the buttons and menu Items depending on their previous state...
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
        btnException.setEnabled(false);
        txtLevelID.setEditable(false);     //__ To make this textBox non editable...
        observable.setStatus();            //__ To set the Value of lblStatus...
        
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }//GEN-LAST:event_btnNewActionPerformed
    
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
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new LevelControlUI().show();
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
    private com.see.truetransact.uicomponent.CComboBox cboName;
    private com.see.truetransact.uicomponent.CCheckBox chkSingleWindow;
    private com.see.truetransact.uicomponent.CLabel lblCash;
    private com.see.truetransact.uicomponent.CLabel lblClearing;
    private com.see.truetransact.uicomponent.CLabel lblCredit;
    private com.see.truetransact.uicomponent.CLabel lblDebit;
    private com.see.truetransact.uicomponent.CLabel lblDescription;
    private com.see.truetransact.uicomponent.CLabel lblLevelID;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblName;
    private com.see.truetransact.uicomponent.CLabel lblSingleWindow;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace17;
    private com.see.truetransact.uicomponent.CLabel lblSpace18;
    private com.see.truetransact.uicomponent.CLabel lblSpace19;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace20;
    private com.see.truetransact.uicomponent.CLabel lblSpace21;
    private com.see.truetransact.uicomponent.CLabel lblSpace22;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace5;
    private com.see.truetransact.uicomponent.CLabel lblStatus;
    private com.see.truetransact.uicomponent.CLabel lblTransfer;
    private com.see.truetransact.uicomponent.CMenuBar mbrMain;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panData;
    private com.see.truetransact.uicomponent.CPanel panLevel;
    private com.see.truetransact.uicomponent.CPanel panLevelControl;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptDelete;
    private javax.swing.JToolBar tbrHead;
    private com.see.truetransact.uicomponent.CTextField txtCashCredit;
    private com.see.truetransact.uicomponent.CTextField txtCashDebit;
    private com.see.truetransact.uicomponent.CTextField txtClearingCredit;
    private com.see.truetransact.uicomponent.CTextField txtClearingDebit;
    private com.see.truetransact.uicomponent.CTextField txtDescription;
    private com.see.truetransact.uicomponent.CTextField txtLevelID;
    private com.see.truetransact.uicomponent.CTextField txtTransferCredit;
    private com.see.truetransact.uicomponent.CTextField txtTransferDebit;
    // End of variables declaration//GEN-END:variables
    
}

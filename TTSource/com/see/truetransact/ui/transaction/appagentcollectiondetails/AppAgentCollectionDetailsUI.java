/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition. 
 * 
 * AppAgentCollectionDetailsUI.java
 *
 * Created on February 2, 2005, 12:20 PM
 */
package com.see.truetransact.ui.transaction.appagentcollectiondetails;

import com.see.truetransact.ui.transaction.appagentcollectiondetails.AppAgentCollectionDetailsOB;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uimandatory.UIMandatoryField;
import com.see.truetransact.uimandatory.MandatoryCheck;
import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.CMandatoryDialog;
import com.see.truetransact.ui.common.viewall.ViewAll;
import com.see.truetransact.ui.common.authorizestatus.AuthorizeStatusUI;
import com.see.truetransact.ui.TrueTransactMain;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.EnhancedTableModel;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.uicomponent.COptionPane;

import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.uivalidation.CurrencyValidation;
import com.see.truetransact.uivalidation.NumericValidation;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Observable;
import java.util.List;
import java.util.Date;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author 152721
 */
public class AppAgentCollectionDetailsUI extends CInternalFrame implements java.util.Observer, UIMandatoryField {

    HashMap mandatoryMap;
    AppAgentCollectionDetailsOB observable;
    //    AgentRB resourceBundle = new AgentRB();
    //    java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle("com.see.truetransact.ui.transaction.AgentCommisionDisbursalRB", ProxyParameters.LANGUAGE);
    java.util.ResourceBundle resourceBundle, objMandatoryRB;
    final int EDIT = 0, DELETE = 1, AUTHORIZE = 2, AGENTID = 3, ACCNO = 4, DEPOSITNO = 5;
    int viewType = -1;
    boolean cbo = false;
    private Date curr_dt = null;
    int updateTab = -1;
    private TableModelListener tableModelListener;

    /**
     * Creates new form AgentUI
     */
    public AppAgentCollectionDetailsUI(String are) {
        initComponents();
        initSetup();
        btnAdd.setEnabled(false);
        btnCancelSyncedEntry.setEnabled(false);
    }

    public AppAgentCollectionDetailsUI() {
        initComponents();
        initSetup();
        btnAdd.setEnabled(false);

    }

    private void initSetup() {
        setFieldNames();
        internationalize();
        setMandatoryHashMap();
        setObservable();
        setMaxLenths();
        initComponentsData();
        //        lblCommision.setE(false);
        ClientUtil.enableDisable(this, false); //__ Disables all when the screen appears for the 1st time
        setButtonEnableDisable();
        //__ Enables/Disables the necessary buttons and menu items...
        observable.resetForm();
        observable.resetStatus();              //__ to reset the status...
//        btnAuthorize.setVisible(false);
        btnSave.setVisible(true);
        btnEdit.setVisible(false);
        btnPrint.setVisible(false);
        btnDelete.setVisible(false);
        observable.ttNotifyObservers();
        curr_dt = ClientUtil.getCurrentDate();
        tblAgentCommission.getModel().addTableModelListener(tableModelListener);
        btnAdd.setEnabled(false);
        cboAgentId.setEnabled(false);
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnCancel.setEnabled(true);
        lblBatchId.setVisible(false);
        lblBatchIdValue.setVisible(false);
        btnCancelSyncedEntry.setEnabled(false);
    }

    private void setObservable() {
        observable = AppAgentCollectionDetailsOB.getInstance();
        observable.addObserver(this);
    }

    /*
     * Auto Generated Method - setFieldNames() This method assigns name for all
     * the components. Other functions are working based on this name.
     */
    private void setFieldNames() {
        //        btnAgentID.setName("btnAgentID");
        btnAuthorize.setName("btnAuthorize");
        btnCancel.setName("btnCancel");
        btnClose.setName("btnClose");
        btnDelete.setName("btnDelete");
        btnEdit.setName("btnEdit");
        btnException.setName("btnException");
        btnNew.setName("btnNew");
        btnPrint.setName("btnPrint");
        btnReject.setName("btnReject");
        btnSave.setName("btnSave");
        lblSpace1.setName("lblSpace1");
        lblSpace2.setName("lblSpace2");
        lblSpace3.setName("lblSpace3");
        lblSpace4.setName("lblSpace4");
        lblStatus1.setName("lblStatus1");;
        lblAgentName.setName("lblAgentName");
        lblAgentID.setName("lblAgentID");
        lblNameForAgent.setName("lblNameForAgent");
        cboAgentId.setName("cboAgentId");
        panAgentData.setName("panAgentData");

    }

    /*
     * Auto Generated Method - internationalize() This method used to assign
     * display texts from the Resource Bundle File.
     */
    private void internationalize() {
    }

    public void initComponentsData() {
        cboAgentId.setModel(observable.getCbmAgentId());
        //  cboProdtype.setModel(observable.getCbmProdtype());
    }

    /*
     * Auto Generated Method - update() This method called by Observable. It
     * updates the UI with Observable's data. If needed add/Remove RadioButtons
     * method need to be added.
     */
    public void update(Observable observed, Object arg) {
        cboAgentId.setSelectedItem(observable.getCboAgentId());
    }

    /*
     * Auto Generated Method - updateOBFields() This method called by Save
     * option of UI. It updates the OB with UI data.
     */
    public void updateOBFields() {
        observable.setScreen(getScreen());
        observable.setModule(getModule());
        observable.setSelectedBranchID(getSelectedBranchID());
        observable.setAgentId(((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected().toString());
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
        mandatoryMap.put("txtCommision", new Boolean(true));
        mandatoryMap.put("txtCommisionDuringThePeriod", new Boolean(true));
        mandatoryMap.put("txCommisionForThePeriod", new Boolean(true));
        mandatoryMap.put("cboAgentId", new Boolean(true));
    }

    /*
     * Auto Generated Method - getMandatoryHashMap() Getter method for
     * setMandatoryHashMap().
     */
    public HashMap getMandatoryHashMap() {
        return mandatoryMap;
    }

    /*
     * Auto Generated Method - setHelpMessage() This method shows tooltip help
     * for all the input fields available in the UI. It needs the Mandatory
     * Resource Bundle object. Help display Label name should be lblMsg.
     */
    public void setHelpMessage() {
    }

    private void setMaxLenths() {
    }

    // To set The Value of the Buttons Depending on the Value or Condition...
    private void setButtonEnableDisable() {
        btnNew.setEnabled(!btnNew.isEnabled());
        btnClose.setEnabled(!btnClose.isEnabled());
        btnEdit.setEnabled(!btnEdit.isEnabled());
        btnDelete.setEnabled(!btnDelete.isEnabled());
        mitNew.setEnabled(btnNew.isEnabled());
        mitEdit.setEnabled(btnEdit.isEnabled());
        mitDelete.setEnabled(btnDelete.isEnabled());

        btnSave.setEnabled(!btnSave.isEnabled());
        btnCancel.setEnabled(!btnNew.isEnabled());
        mitSave.setEnabled(btnSave.isEnabled());
        mitCancel.setEnabled(btnCancel.isEnabled());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        chkTranstypeGroup = new com.see.truetransact.uicomponent.CButtonGroup();
        panAgent = new com.see.truetransact.uicomponent.CPanel();
        panAgentData = new com.see.truetransact.uicomponent.CPanel();
        lblAgentName = new com.see.truetransact.uicomponent.CLabel();
        cboAgentId = new com.see.truetransact.uicomponent.CComboBox();
        lblAgentID = new com.see.truetransact.uicomponent.CLabel();
        lblNameForAgent = new com.see.truetransact.uicomponent.CLabel();
        btnAdd = new com.see.truetransact.uicomponent.CButton();
        btnCancelSyncedEntry = new com.see.truetransact.uicomponent.CButton();
        panAgentDisplay = new com.see.truetransact.uicomponent.CPanel();
        srpPrintPan = new com.see.truetransact.uicomponent.CScrollPane();
        tblAgentCommission = new com.see.truetransact.uicomponent.CTable();
        panAgentData2 = new com.see.truetransact.uicomponent.CPanel();
        lblTotalDebitAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCreditAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCreditAmount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDebitAmountValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDebitCount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalDebitCountValue = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCreditCount = new com.see.truetransact.uicomponent.CLabel();
        lblTotalCreditCountValue = new com.see.truetransact.uicomponent.CLabel();
        panAgentData1 = new com.see.truetransact.uicomponent.CPanel();
        chkVerifyAll = new com.see.truetransact.uicomponent.CCheckBox();
        lblBatchId = new com.see.truetransact.uicomponent.CLabel();
        lblBatchIdValue = new com.see.truetransact.uicomponent.CLabel();
        panStatus = new com.see.truetransact.uicomponent.CPanel();
        lblSpace1 = new com.see.truetransact.uicomponent.CLabel();
        lblStatus1 = new com.see.truetransact.uicomponent.CLabel();
        lblMsg = new com.see.truetransact.uicomponent.CLabel();
        tbrLoantProduct = new javax.swing.JToolBar();
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
        lblSpace4 = new com.see.truetransact.uicomponent.CLabel();
        btnPrint = new com.see.truetransact.uicomponent.CButton();
        lblSpace75 = new com.see.truetransact.uicomponent.CLabel();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        mbrAgent = new com.see.truetransact.uicomponent.CMenuBar();
        mnuProcess = new javax.swing.JMenu();
        mitNew = new javax.swing.JMenuItem();
        mitEdit = new javax.swing.JMenuItem();
        mitDelete = new javax.swing.JMenuItem();
        sptProcess = new javax.swing.JSeparator();
        mitSave = new javax.swing.JMenuItem();
        mitCancel = new javax.swing.JMenuItem();
        sptCancel = new javax.swing.JSeparator();
        mitPrint = new javax.swing.JMenuItem();
        sptPrint = new javax.swing.JSeparator();
        mitClose = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setMinimumSize(new java.awt.Dimension(700, 620));
        setPreferredSize(new java.awt.Dimension(700, 620));

        panAgent.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panAgent.setMinimumSize(new java.awt.Dimension(412, 500));
        panAgent.setPreferredSize(new java.awt.Dimension(412, 500));
        panAgent.setLayout(new java.awt.GridBagLayout());

        panAgentData.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAgentData.setMinimumSize(new java.awt.Dimension(550, 85));
        panAgentData.setPreferredSize(new java.awt.Dimension(550, 100));
        panAgentData.setLayout(new java.awt.GridBagLayout());

        lblAgentName.setText("Agent Name");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData.add(lblAgentName, gridBagConstraints);

        cboAgentId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboAgentId.setPopupWidth(150);
        cboAgentId.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboAgentIdItemStateChanged(evt);
            }
        });
        cboAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData.add(cboAgentId, gridBagConstraints);

        lblAgentID.setText("Agent ID");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData.add(lblAgentID, gridBagConstraints);

        lblNameForAgent.setMaximumSize(new java.awt.Dimension(100, 21));
        lblNameForAgent.setMinimumSize(new java.awt.Dimension(100, 21));
        lblNameForAgent.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData.add(lblNameForAgent, gridBagConstraints);

        btnAdd.setText("Load Data");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData.add(btnAdd, gridBagConstraints);

        btnCancelSyncedEntry.setText("Cancel Synced Entry");
        btnCancelSyncedEntry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelSyncedEntryActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData.add(btnCancelSyncedEntry, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 15, 20);
        panAgent.add(panAgentData, gridBagConstraints);

        panAgentDisplay.setMinimumSize(new java.awt.Dimension(675, 300));
        panAgentDisplay.setPreferredSize(new java.awt.Dimension(675, 300));
        panAgentDisplay.setLayout(new java.awt.GridBagLayout());

        srpPrintPan.setMinimumSize(new java.awt.Dimension(800, 350));
        srpPrintPan.setPreferredSize(new java.awt.Dimension(650, 280));

        tblAgentCommission.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Select", "CUSTOMER ID", "CUSTOMER NAME", "ACT NUM", "TRANS TYPE", "PROD TYPE", "AMOUNT", "AGENT CUST ID", "APP ID", "APP CREATED DT", "VALUE DT"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, false, false, false, false, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAgentCommission.setMinimumSize(new java.awt.Dimension(165, 10000));
        tblAgentCommission.setPreferredSize(new java.awt.Dimension(600, 10000));
        tblAgentCommission.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAgentCommissionMouseClicked(evt);
            }
        });
        srpPrintPan.setViewportView(tblAgentCommission);

        panAgentDisplay.add(srpPrintPan, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panAgent.add(panAgentDisplay, gridBagConstraints);

        panAgentData2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAgentData2.setMinimumSize(new java.awt.Dimension(550, 60));
        panAgentData2.setPreferredSize(new java.awt.Dimension(550, 150));
        panAgentData2.setLayout(new java.awt.GridBagLayout());

        lblTotalDebitAmount.setForeground(new java.awt.Color(255, 0, 51));
        lblTotalDebitAmount.setText("Total Debit Amount");
        lblTotalDebitAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalDebitAmount, gridBagConstraints);

        lblTotalCreditAmountValue.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalCreditAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalCreditAmountValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTotalCreditAmountValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalCreditAmountValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalCreditAmountValue, gridBagConstraints);

        lblTotalCreditAmount.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalCreditAmount.setText("Total Credit Amount");
        lblTotalCreditAmount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalCreditAmount, gridBagConstraints);

        lblTotalDebitAmountValue.setForeground(new java.awt.Color(255, 0, 51));
        lblTotalDebitAmountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalDebitAmountValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTotalDebitAmountValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalDebitAmountValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalDebitAmountValue, gridBagConstraints);

        lblTotalDebitCount.setForeground(new java.awt.Color(255, 0, 51));
        lblTotalDebitCount.setText("Total Debit Count");
        lblTotalDebitCount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalDebitCount, gridBagConstraints);

        lblTotalDebitCountValue.setForeground(new java.awt.Color(255, 0, 51));
        lblTotalDebitCountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalDebitCountValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTotalDebitCountValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalDebitCountValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalDebitCountValue, gridBagConstraints);

        lblTotalCreditCount.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalCreditCount.setText("Total Credit Count");
        lblTotalCreditCount.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalCreditCount, gridBagConstraints);

        lblTotalCreditCountValue.setForeground(new java.awt.Color(0, 153, 0));
        lblTotalCreditCountValue.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        lblTotalCreditCountValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblTotalCreditCountValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblTotalCreditCountValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData2.add(lblTotalCreditCountValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panAgent.add(panAgentData2, gridBagConstraints);

        panAgentData1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panAgentData1.setMinimumSize(new java.awt.Dimension(550, 25));
        panAgentData1.setPreferredSize(new java.awt.Dimension(550, 100));
        panAgentData1.setLayout(new java.awt.GridBagLayout());

        chkVerifyAll.setText("Verify All");
        chkVerifyAll.setMaximumSize(new java.awt.Dimension(300, 20));
        chkVerifyAll.setMinimumSize(new java.awt.Dimension(300, 18));
        chkVerifyAll.setPreferredSize(new java.awt.Dimension(300, 18));
        chkVerifyAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkVerifyAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 1);
        panAgentData1.add(chkVerifyAll, gridBagConstraints);

        lblBatchId.setText("Batch Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData1.add(lblBatchId, gridBagConstraints);

        lblBatchIdValue.setMaximumSize(new java.awt.Dimension(100, 21));
        lblBatchIdValue.setMinimumSize(new java.awt.Dimension(100, 21));
        lblBatchIdValue.setPreferredSize(new java.awt.Dimension(100, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        panAgentData1.add(lblBatchIdValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(15, 20, 15, 20);
        panAgent.add(panAgentData1, gridBagConstraints);

        getContentPane().add(panAgent, java.awt.BorderLayout.CENTER);

        panStatus.setLayout(new java.awt.GridBagLayout());

        lblSpace1.setText(" Status :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblSpace1, gridBagConstraints);

        lblStatus1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        lblStatus1.setText("                      ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblStatus1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panStatus.add(lblMsg, gridBagConstraints);

        getContentPane().add(panStatus, java.awt.BorderLayout.SOUTH);

        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_NEW.gif"))); // NOI18N
        btnNew.setToolTipText("New");
        btnNew.setEnabled(false);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnNew);

        lblSpace70.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace70.setText("     ");
        lblSpace70.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace70.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace70);

        btnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EDIT.gif"))); // NOI18N
        btnEdit.setToolTipText("Edit");
        btnEdit.setEnabled(false);
        btnEdit.setOpaque(false);
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnEdit);

        lblSpace71.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace71.setText("     ");
        lblSpace71.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace71.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace71);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_DELETE.gif"))); // NOI18N
        btnDelete.setToolTipText("Delete");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnDelete);

        lblSpace2.setText("     ");
        tbrLoantProduct.add(lblSpace2);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_SAVE.gif"))); // NOI18N
        btnSave.setToolTipText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnSave);

        lblSpace72.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace72.setText("     ");
        lblSpace72.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace72.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace72);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCancel.setToolTipText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnCancel);

        lblSpace3.setText("     ");
        tbrLoantProduct.add(lblSpace3);

        btnAuthorize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnAuthorize.setToolTipText("Authorize");
        btnAuthorize.setEnabled(false);
        btnAuthorize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuthorizeActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnAuthorize);

        lblSpace73.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace73.setText("     ");
        lblSpace73.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace73.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace73);

        btnException.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_EXCEPTION.gif"))); // NOI18N
        btnException.setToolTipText("Exception");
        btnException.setEnabled(false);
        btnException.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExceptionActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnException);

        lblSpace74.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace74.setText("     ");
        lblSpace74.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace74.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace74);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setToolTipText("Reject");
        btnReject.setEnabled(false);
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnReject);

        lblSpace4.setText("     ");
        tbrLoantProduct.add(lblSpace4);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_PRINT.gif"))); // NOI18N
        btnPrint.setToolTipText("Print");
        btnPrint.setEnabled(false);
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnPrint);

        lblSpace75.setForeground(new java.awt.Color(240, 240, 240));
        lblSpace75.setText("     ");
        lblSpace75.setMaximumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setMinimumSize(new java.awt.Dimension(3, 18));
        lblSpace75.setPreferredSize(new java.awt.Dimension(3, 18));
        tbrLoantProduct.add(lblSpace75);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setToolTipText("Close");
        btnClose.setEnabled(false);
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        tbrLoantProduct.add(btnClose);

        getContentPane().add(tbrLoantProduct, java.awt.BorderLayout.NORTH);

        mnuProcess.setText("Process");

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
        mitEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitEditActionPerformed(evt);
            }
        });
        mnuProcess.add(mitEdit);

        mitDelete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        mitDelete.setMnemonic('D');
        mitDelete.setText("Delete");
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
        mitSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitSaveActionPerformed(evt);
            }
        });
        mnuProcess.add(mitSave);

        mitCancel.setMnemonic('C');
        mitCancel.setText("Cancel");
        mitCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitCancelActionPerformed(evt);
            }
        });
        mnuProcess.add(mitCancel);
        mnuProcess.add(sptCancel);

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

        mbrAgent.add(mnuProcess);

        setJMenuBar(mbrAgent);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        // TODO add your handling code here:
        cbo = true;
        observable.resetForm();              // to Reset all the Fields and Status in UI...
        btnAdd.setEnabled(true);
        setButtonEnableDisable();             // Enables/Disables the necessary buttons and menu items...
        ClientUtil.enableDisable(this, true); // Enables the panel...
        observable.setActionType(ClientConstants.ACTIONTYPE_NEW);   //Sets the Action Type to be performed...
        observable.setStatus();
        btnAdd.setEnabled(true);
        cboAgentId.setEnabled(true);
        setModified(true);
        btnAuthorize.setEnabled(false);
        btnReject.setEnabled(false);
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        //                observable.setActionType(ClientConstants.ACTIONTYPE_EDIT); //__ Sets the Action Type to be performed...
        //        popUp(EDIT);
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        //        observable.setActionType(ClientConstants.ACTIONTYPE_DELETE); //__ Sets the Action Type to be performed...
        //        popUp(DELETE);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private boolean isSelectClosedAccounts(){
        boolean selectClose = false;
        for (int i = 0; i < tblAgentCommission.getRowCount(); i++) {
                if ((Boolean) tblAgentCommission.getValueAt(i, 0) && tblAgentCommission.getValueAt(i, 11).equals("CLOSED")) {
                    selectClose = true;
                    break;
                }
            }
        return selectClose;
    }
    
    
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        //        // TODO add your handling code here:  
        boolean countSelect = false;
        if (tblAgentCommission.getRowCount() > 0) {
            for (int i = 0; i < tblAgentCommission.getRowCount(); i++) {
                if ((Boolean) tblAgentCommission.getValueAt(i, 0)) {
                    countSelect = true;
                }
            }
            if (!countSelect) {
                ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                countSelect = false;
                return;
            } else if(isSelectClosedAccounts()){
                ClientUtil.showMessageWindow("Selected closed accounts !!! ");
                return;
            } else {
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Are you sure want to generate transaction ?", CommonConstants.WARNINGTITLE,
                        COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
                System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    int count = tblAgentCommission.getRowCount();
                    if (count > 0) {
                        HashMap dataMap = new HashMap();
                        observable.doAllAction(dataMap);
                    }
                    if (observable.getResult() != ClientConstants.ACTIONTYPE_FAILED) {
                        lblStatus1.setText("POSTED");
                        if (observable.getProxyReturnMap() != null && observable.getProxyReturnMap().size() > 0) {
//                            if (observable.getProxyReturnMap().containsKey("BATCH_ID")) {
//                                ClientUtil.showMessageWindow("Batch ID : " + CommonUtil.convertObjToStr(observable.getProxyReturnMap().get("BATCH_ID")));
//                            }
                        }
                        btnCancelActionPerformed(null);
                    }
                } else {
                    
                }
            }
        } else {
            ClientUtil.showMessageWindow("Please Calculate Commision!!");
            return;
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:

        if (viewType == EDIT) {
            //            super.removeEditLock(txtAgentID.getText());
        }

        observable.resetForm();                 //__ Reset the fields in the UI to null...
        updateTable();
        ClientUtil.enableDisable(this, false);  //__ Disables the panel...
        if (!btnSave.isEnabled()) {
            btnSave.setEnabled(true);
        }
        setButtonEnableDisable();               //__ Enables or Disables the buttons and menu Items depending on their previous state...
        observable.setActionType(ClientConstants.ACTIONTYPE_CANCEL);//Sets the Action Type to be performed...
        observable.setStatus();                 //__ To set the Value of lblStatus..

        viewType = -1;

        //__ Make the Screen Closable..
        setModified(false);
        btnClose.setEnabled(true);
        lblNameForAgent.setText("");
//        txtTdsCommission.setText("");
        btnNew.setEnabled(true);
        btnAdd.setEnabled(false);
        cboAgentId.setEnabled(false);
        lblTotalDebitCountValue.setText("");
        lblTotalDebitAmountValue.setText("");
        lblTotalCreditCountValue.setText("");
        lblTotalCreditAmountValue.setText("");
        chkVerifyAll.setSelected(false);
        lblBatchIdValue.setText("");
        btnAuthorize.setEnabled(true);
        btnReject.setEnabled(true);
        btnCancelSyncedEntry.setEnabled(false);
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnAuthorizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuthorizeActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_AUTHORIZED);
        btnReject.setEnabled(false);
        btnCancel.setEnabled(true);
        btnCancelSyncedEntry.setEnabled(false);
    }//GEN-LAST:event_btnAuthorizeActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_REJECTED);
        btnAuthorize.setEnabled(false);
         btnCancel.setEnabled(true);
        btnCancelSyncedEntry.setEnabled(false);
    }//GEN-LAST:event_btnRejectActionPerformed

    private void btnExceptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExceptionActionPerformed
        // TODO add your handling code here:
        authorizeStatus(CommonConstants.STATUS_EXCEPTION);
    }//GEN-LAST:event_btnExceptionActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        // TODO add your handling code here:
        cifClosingAlert();
        //        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed
    private void displayTransDetail(HashMap proxyResultMap) {
        System.out.println("@#$@@$@@@$ proxyResultMap : " + proxyResultMap);
        int yesNo = 0;
        String[] options = {"Yes", "No"};
        yesNo = COptionPane.showOptionDialog(null, "Do you want to print?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                null, options, options[0]);
        if (yesNo == 0) {
            TTIntegration ttIntgration = null;
            HashMap paramMap = null;
            String toTransId = "";
            String fromTransId = "";
            String toCashId = "";
            String fromCashId = "";
            if (proxyResultMap.containsKey("TRANSFER") && proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                    fromTransId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
                }
                paramMap = new HashMap();
                paramMap.put("TransId", fromTransId);
//                paramMap.put("ToTransId", toTransId);
                paramMap.put("TransDt", curr_dt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                System.out.println("#$#$$ paramMap : " + paramMap);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("ReceiptPayment");
            }
            if (proxyResultMap.containsKey("CASH") && proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                if (proxyResultMap.containsKey("SINGLE_TANS_ID")) {
                    fromCashId = CommonUtil.convertObjToStr(proxyResultMap.get("SINGLE_TANS_ID"));
                }
                paramMap = new HashMap();
                paramMap.put("TransId", fromCashId);
//              paramMap.put("ToTransId", toCashId);
                paramMap.put("TransDt", curr_dt);
                paramMap.put("BranchId", ProxyParameters.BRANCH_ID);
                System.out.println("#$#$$ paramMap : " + paramMap);
                ttIntgration.setParam(paramMap);
                ttIntgration.integrationForPrint("CashPayment");
            }
        }
    }
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if (cboAgentId.getSelectedItem() != null && !cboAgentId.getSelectedItem().equals("")) {
            setAgentCommisionTable();
            btnCancelSyncedEntry.setEnabled(true);
        } else {
            ClientUtil.showMessageWindow("Please select AgentID!!");
            return;
        }
    }//GEN-LAST:event_btnAddActionPerformed

       
    private void setColour() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (CommonUtil.convertObjToStr(table.getValueAt(row, 11)).equals("CLOSED")) { 
                    setForeground(Color.RED);
                }
                else {
                    setForeground(Color.BLACK);
                }
                this.setOpaque(true);
                return this;
            }
        };
        tblAgentCommission.setDefaultRenderer(Object.class, renderer);
    }
    
    private void setAgentCommisionTable() {
        // btnReject.setVisible(true);
        HashMap whereMap = new HashMap();
        HashMap shareTypeMap = new HashMap();
        HashMap agentLeaveMap = new HashMap();
        agentLeaveMap.put("userId", CommonUtil.convertObjToStr(observable.getCbmAgentId().getKeyForSelected()));
        agentLeaveMap.put("currDt", curr_dt.clone());
//        List list = ClientUtil.executeQuery("getAgentLeaveDetails", agentLeaveMap);
//        if (list != null && list.size() > 0) {
//            agentLeaveMap = (HashMap) list.get(0);
//            String leaveTakingId = CommonUtil.convertObjToStr(agentLeaveMap.get("L_AGENT_ID"));
//            String collectingId = CommonUtil.convertObjToStr(lblNameForAgent.getText());
//            System.out.println("leaveTakingId : " + leaveTakingId + "collectingId : " + collectingId);
//            whereMap.put("LEAVE_AGENT_ID", "'" + leaveTakingId + "'" + "," + "'" + collectingId + "'");
//            System.out.println("whereMap : " + whereMap);
//        } else {
            whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(observable.getCbmAgentId().getKeyForSelected()));
//        }
        whereMap.put("CURR_DT", curr_dt.clone());
        whereMap.put("NEW_MODE","NEW_MODE");
//        whereMap.put("VAT_PER", CommonUtil.convertObjToDouble(txtTdsCommission.getText()));           
        shareTypeMap.put(CommonConstants.MAP_NAME, "getAppAgentCollectionDetailsList");
        shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
        try {
            System.out.println("shareTypeMap=======" + shareTypeMap);
            if (observable.populateData(shareTypeMap, tblAgentCommission)) {
                tblAgentCommission.setModel(observable.getTbmAgentCommission());
                tblAgentCommission.editCellAt(0, 0);
                tblAgentCommission.setEditingColumn(0);
                setColour();
//                lblTotaCreditAmountValue.setText(CommonUtil.convertObjToStr(observable.getTotalCreditAmount()));
//                lblTotalDebitAmountValue.setText(CommonUtil.convertObjToStr(observable.getTotalDebitAmount()));
            } else {
                ClientUtil.showMessageWindow("No Records Found!!");
                observable.resetForm();
                updateTable();
                return;
            }
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }

    private void updateTable() {
        tblAgentCommission.setModel(observable.getTbmAgentCommission());
    }

    private Date setProperDtFormat(Date dt) {
        Date tempDt = (Date) curr_dt.clone();
        if (dt != null) {
            tempDt.setDate(dt.getDate());
            tempDt.setMonth(dt.getMonth());
            tempDt.setYear(dt.getYear());
            return tempDt;
        }
        return null;
    }
    private void cboAgentIdItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboAgentIdItemStateChanged
        //        observable.agentName();
        //        lblNameForAgent.setText; // TODO add your handling code here:
    }//GEN-LAST:event_cboAgentIdItemStateChanged

    private void cboAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboAgentIdActionPerformed
        String prodId = "";
        String agentId = "";
        if (cboAgentId.getSelectedItem() != null && !cboAgentId.getSelectedItem().equals("")) {
            agentId = ((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected().toString();
            System.out.println("agentId^$^$^$^^^$^" + agentId);
            lblNameForAgent.setText(CommonUtil.convertObjToStr(((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected()));
            observable.setLblNameForAgent(CommonUtil.convertObjToStr(((ComboBoxModel) cboAgentId.getModel()).getKeyForSelected()));
            observable.setCboAgentId(agentId);
        } else {
            lblNameForAgent.setText("");
            observable.setLblNameForAgent("");
        }
    }//GEN-LAST:event_cboAgentIdActionPerformed
    public void authorizeStatus(String authorizeStatus) {
        if (viewType != AUTHORIZE) {
            viewType = AUTHORIZE;
            lblBatchId.setVisible(true);
            lblBatchIdValue.setVisible(true);
            //__ To Save the data in the Internal Frame...
            setModified(true);
            HashMap mapParam = new HashMap();
            mapParam.put(CommonConstants.MAP_NAME, "getAppAgentCollectionAuthorizeList");

            HashMap whereMap = new HashMap();
            whereMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
            whereMap.put("TRANS_DT", curr_dt.clone());
            //            whereMap.put(CommonConstants.BRANCH_ID, TrueTransactMain.BRANCH_ID);
            whereMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
            whereMap.put(CommonConstants.AUTHORIZESTATUS, authorizeStatus);

//            whereMap.put("BRANCHID",TrueTransactMain.BRANCH_ID);
            mapParam.put(CommonConstants.MAP_WHERE, whereMap);
//            mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeAgentData");

            whereMap = null;

            AuthorizeStatusUI authorizeUI = new AuthorizeStatusUI(this, mapParam);
            authorizeUI.show();
            btnSave.setEnabled(false);

            //__ If there's no data to be Authorized, call Cancel action...
            if (!isModified()) {
                setButtonEnableDisable();
                btnCancelActionPerformed(null);
            }
        } else if (viewType == AUTHORIZE) {
            try{
                HashMap singleAuthorizeMap = new HashMap();
                singleAuthorizeMap.put(CommonConstants.STATUS, authorizeStatus);
                singleAuthorizeMap.put(CommonConstants.USER_ID, TrueTransactMain.USER_ID);
                //            singleAuthorizeMap.put("AGENT ID",txtAgentID.getText());
                //            singleAuthorizeMap.put(CommonConstants.BRANCH_ID,TrueTransactMain.BRANCH_ID);
                singleAuthorizeMap.put(CommonConstants.BRANCH_ID, getSelectedBranchID());
                singleAuthorizeMap.put(CommonConstants.AUTHORIZEDT, curr_dt.clone());
                singleAuthorizeMap.put("BATCH_ID",lblBatchIdValue.getText());
    //            HashMap dataMap = new HashMap();
                singleAuthorizeMap.put("AUTHORIZE","AUTHORIZE");
                observable.doAllActionPerform(singleAuthorizeMap);

                System.out.println("singleAuthorizeMap: " + singleAuthorizeMap);

                ClientUtil.execute("authorizeAgentData", singleAuthorizeMap);
                viewType = -1;
                btnSave.setEnabled(true);
                btnCancelActionPerformed(null);
            }catch(Exception e){
                
            }
        }
    }

    private void displayAlert(String message) {
        CMandatoryDialog cmd = new CMandatoryDialog();
        cmd.setMessage(message);
        cmd.show();
    }

    private void popUp(int field) {
        final HashMap viewMap = new HashMap();
        viewType = field;
        HashMap whereMap = new HashMap();
        HashMap operativeMap = new HashMap();
        HashMap depositMap = new HashMap();
        whereMap.put("BRANCHID", getSelectedBranchID());
        if (field == EDIT || field == DELETE || field == AUTHORIZE) { //Edit=0 and Delete=1
            ArrayList lst = new ArrayList();
            lst.add("AGENT ID");
            viewMap.put(ClientConstants.RECORD_KEY_COL, lst);
            lst = null;
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "viewAgentData");
            new ViewAll(this, viewMap, false).show();
        } else if (field == AGENTID) {
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            viewMap.put(CommonConstants.MAP_NAME, "getAgentDetails");
            new ViewAll(this, viewMap, true).show();
        } else if (field == ACCNO) {
            viewMap.put(CommonConstants.MAP_WHERE, operativeMap);
            viewMap.put(CommonConstants.MAP_NAME, "getOperativeDetails");
            new ViewAll(this, viewMap, true).show();
            System.out.println("getOperativeDetails :" + operativeMap);
        } else if (field == DEPOSITNO) {
            viewMap.put(CommonConstants.MAP_WHERE, depositMap);
            System.out.println("depositMap : " + depositMap);
            viewMap.put(CommonConstants.MAP_NAME, "getDepositIdForCustomer");
            new ViewAll(this, viewMap, true).show();
        }
    }

    public Date getProperDateFormat(Object obj) {
        Date currDate = null;
        if (obj != null && obj.toString().length() > 0) {
            Date tempDt = DateUtil.getDateMMDDYYYY(CommonUtil.convertObjToStr(obj));
            currDate = (Date) curr_dt.clone();
            currDate.setDate(tempDt.getDate());
            currDate.setMonth(tempDt.getMonth());
            currDate.setYear(tempDt.getYear());
        }
        return currDate;
    }

    // this method is called automatically from ViewAll...
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        HashMap OAprodDescMap = new HashMap();
        HashMap depProdDescMap = new HashMap();
        System.out.println("hash:  " + hash);
        if (viewType == EDIT || viewType == DELETE || viewType == AUTHORIZE) {
            HashMap whereMap = new HashMap();
            HashMap shareTypeMap = new HashMap();
            HashMap agentLeaveMap = new HashMap();
            agentLeaveMap.put("userId", hash.get("AGENT_CUST_ID"));
            agentLeaveMap.put("currDt", curr_dt.clone());
            List list = ClientUtil.executeQuery("getAgentLeaveDetails", agentLeaveMap);
            if (list != null && list.size() > 0) {
                agentLeaveMap = (HashMap) list.get(0);
                String leaveTakingId = CommonUtil.convertObjToStr(agentLeaveMap.get("L_AGENT_ID"));
                String collectingId = CommonUtil.convertObjToStr(agentLeaveMap.get("C_AGENT_ID"));
                System.out.println("leaveTakingId : " + leaveTakingId + "collectingId : " + collectingId);
                whereMap.put("LEAVE_AGENT_ID", "'" + leaveTakingId + "'" + "," + "'" + collectingId + "'");
                System.out.println("whereMap : " + whereMap);
            } else {
                whereMap.put("AGENT_ID", hash.get("AGENT_CUST_ID"));
            }
            whereMap.put("CURR_DT", curr_dt.clone());
            whereMap.put("AUTHORIZE","AUTHORIZE");
            whereMap.put("BATCH_ID",hash.get("BATCH_ID"));
    //        whereMap.put("VAT_PER", CommonUtil.convertObjToDouble(txtTdsCommission.getText()));           
            shareTypeMap.put(CommonConstants.MAP_NAME, "getAppAgentCollectionDetailsList");
            shareTypeMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                System.out.println("shareTypeMap=======" + shareTypeMap);
                if (observable.populateData(shareTypeMap, tblAgentCommission)) {
                    lblBatchIdValue.setText(CommonUtil.convertObjToStr(hash.get("BATCH_ID")));
                    tblAgentCommission.setModel(observable.getTbmAgentCommission());
                    chkVerifyAll.setSelected(true);
                    chkVerifyAllActionPerformed(null);
                    String agentCustId = CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(tblAgentCommission.getRowCount()-1, 7));
                    if(agentCustId != null && agentCustId.length()>0){
//                        setCboAgentCustId(objCustomerTO.getAgentCustId());
                        HashMap agentCustMap = new HashMap();
                        agentCustMap.put("AGENT_CUST_ID",agentCustId);
                        List lst = ClientUtil.executeQuery("getSelectAgentCustomerName", agentCustMap);
                        if(lst != null && lst.size()>0){
                            agentCustMap = (HashMap)lst.get(0);
                            observable.setCboAgentId(CommonUtil.convertObjToStr(agentCustMap.get("VALUE")));
                            lblNameForAgent.setText(agentCustId);
                            observable.setLblNameForAgent(CommonUtil.convertObjToStr(agentCustId));
                        }
                    }
                }
            }catch(Exception e){
                
            }
//            hash.put("AGENTID", hash.get("AGENT_CUST_ID"));
//            hash.put("BRANCHID", getSelectedBranchID());
//            observable.populateData(hash);     //__ Called to display the Data in the UI fields...
            //__ To Set the Value of the Agent Name...
            //__ If the Action type is Delete...
            if (observable.getActionType() == ClientConstants.ACTIONTYPE_DELETE || viewType == AUTHORIZE) {
                ClientUtil.enableDisable(this, false);     //__ Disables the panel...

                //__ If the Action Type is Edit...
            } else if (observable.getActionType() == ClientConstants.ACTIONTYPE_EDIT) {
                ClientUtil.enableDisable(this, true);     //__ Anable the panel...
            }

            //            btnAgentID.setEnabled(false);
            observable.setStatus();             //__ To set the Value of lblStatus...
            setButtonEnableDisable();           //__ Enables or Disables the buttons and menu Items depending on their previous state...

        } else if (viewType == AGENTID) {
            //__ To Set the Data Regarding the Customer in the Screen...
        } else if (viewType == ACCNO) {
            //            txtCommisionCreditedTo.setText(CommonUtil.convertObjToStr(hash.get("OA_ACT_NUM")));
            //            OAprodDescMap.put("PROD_DESC", hash.get("PROD_ID"));
            List lst = ClientUtil.executeQuery("getOAProdDescription", hash);
            OAprodDescMap = (HashMap) lst.get(0);
        } else if (viewType == DEPOSITNO) {
            List lst = ClientUtil.executeQuery("getDepositProdDescription", hash);
            depProdDescMap = (HashMap) lst.get(0);
        }
        //__ To Save the data in the Internal Frame...
        setModified(true);
    }

    private void mitCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCloseActionPerformed
        // TODO add your handling code here:
        btnCloseActionPerformed(null);
    }//GEN-LAST:event_mitCloseActionPerformed

    private void mitPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitPrintActionPerformed
        // TODO add your handling code here:
        btnPrintActionPerformed(null);
    }//GEN-LAST:event_mitPrintActionPerformed

    private void mitCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitCancelActionPerformed
        // TODO add your handling code here:
        btnCancelActionPerformed(null);
    }//GEN-LAST:event_mitCancelActionPerformed

    private void mitSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitSaveActionPerformed
        // TODO add your handling code here:
        btnSaveActionPerformed(null);
    }//GEN-LAST:event_mitSaveActionPerformed

    private void mitDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitDeleteActionPerformed
        // TODO add your handling code here:
        btnDeleteActionPerformed(null);
    }//GEN-LAST:event_mitDeleteActionPerformed

    private void mitEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitEditActionPerformed
        // TODO add your handling code here:
        btnEditActionPerformed(null);
    }//GEN-LAST:event_mitEditActionPerformed

    private void mitNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitNewActionPerformed
        // TODO add your handling code here:
        btnNewActionPerformed(null);
    }//GEN-LAST:event_mitNewActionPerformed

    /**
     * Exit the Application
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm

private void tblAgentCommissionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAgentCommissionMouseClicked
// TODO add your handling code here:
    if (evt != null) {
        if (tblAgentCommission.getRowCount() > 0) {
            double totCreditNonSelectedAmount = 0;
            int totalCreditNonSelectedCount = 0;
            double totDebitNonSelectedAmount = 0;
            int totalDebitNonSelectedCount = 0;
            String st = CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(tblAgentCommission.getSelectedRow(), 0));
            if (st.equals("true")) {
                tblAgentCommission.setValueAt(new Boolean(false), tblAgentCommission.getSelectedRow(), 0);
            } else {
                tblAgentCommission.setValueAt(new Boolean(true), tblAgentCommission.getSelectedRow(), 0);
            }
            double totDRAmt = 0;
            double totCRAmt = 0;
            double totDRCount = 0;
            double totCRCount = 0;
            for (int i = 0; i < tblAgentCommission.getRowCount(); i++) {
                st = CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 0));
                if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("CREDIT")) {
                    totCRAmt += CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                    totCRCount = totCRCount + 1;
                }
                if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("DEBIT")) {
                    totDRAmt += CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                    totDRCount = totDRCount + 1;
                }
            }
            System.out.println("totDRAmt : "+totDRAmt+"totDRCount : "+totDRCount+"totCRAmt : "+totCRAmt+"totCRCount : "+totCRCount);
            for (int i = 0; i < tblAgentCommission.getRowCount(); i++) {
                st = CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 0));
                if (st.equals("false")) {
                    if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("CREDIT")) {
                        totCreditNonSelectedAmount += CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                        totalCreditNonSelectedCount = totalCreditNonSelectedCount + 1;
                    }
                    if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("DEBIT")) {
                        totDebitNonSelectedAmount += CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                        totalDebitNonSelectedCount = totalDebitNonSelectedCount + 1;
                    }
                }
            }
            System.out.println("totCreditNonSelectedAmount : "+totCreditNonSelectedAmount+"totalDebitNonSelectedCount : "+totalDebitNonSelectedCount);
            lblTotalCreditCountValue.setText(CommonUtil.convertObjToStr(totCRCount - totalCreditNonSelectedCount));
            lblTotalCreditAmountValue.setText(CommonUtil.convertObjToStr(totCRAmt - totCreditNonSelectedAmount));
            lblTotalDebitCountValue.setText(CommonUtil.convertObjToStr(totDRCount - totalDebitNonSelectedCount));
            lblTotalDebitAmountValue.setText(CommonUtil.convertObjToStr(totDRAmt - totDebitNonSelectedAmount));
        }
    }

}//GEN-LAST:event_tblAgentCommissionMouseClicked

    private void chkVerifyAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkVerifyAllActionPerformed
        // TODO add your handling code here:
        boolean flag;
        if (chkVerifyAll.isSelected() == true) {
            flag = true;
        } else {
            flag = false;
        }
        double totCreditAmount = 0;
        double totDebitAmount = 0;
        int totalDebitCount = 0;
        int totalCreditCount = 0;
        for (int i = 0; i < tblAgentCommission.getRowCount(); i++) {
            tblAgentCommission.setValueAt(new Boolean(flag), i, 0);
            if (flag) {
                if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("CREDIT")) {
                    totCreditAmount += CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                    lblTotalCreditAmountValue.setText(CommonUtil.convertObjToStr(totCreditAmount));
                    totalCreditCount += 1;
                    lblTotalCreditCountValue.setText(CommonUtil.convertObjToStr(totalCreditCount));
                }
                if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("DEBIT")) {
                    totDebitAmount += CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                    lblTotalDebitAmountValue.setText(CommonUtil.convertObjToStr(totDebitAmount));
                    totalDebitCount += 1;
                    lblTotalDebitCountValue.setText(CommonUtil.convertObjToStr(totalDebitCount));
                }
            } else {
                if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("CREDIT")) {
                    totCreditAmount -= CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                    lblTotalCreditAmountValue.setText(CommonUtil.convertObjToStr(0));
                    lblTotalCreditCountValue.setText(CommonUtil.convertObjToStr(0));
                }
                if (CommonUtil.convertObjToStr(tblAgentCommission.getValueAt(i, 4)).equals("DEBIT")) {
                    totDebitAmount -= CommonUtil.convertObjToDouble(tblAgentCommission.getValueAt(i, 6));
                    lblTotalDebitAmountValue.setText(CommonUtil.convertObjToStr(0));
                    lblTotalDebitCountValue.setText(CommonUtil.convertObjToStr(0));
                }
            }
        }
    }//GEN-LAST:event_chkVerifyAllActionPerformed

    private void btnCancelSyncedEntryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelSyncedEntryActionPerformed
        // TODO add your handling code here:
        boolean countSelect = false;
        if (tblAgentCommission.getRowCount() > 0) {
            for (int i = 0; i < tblAgentCommission.getRowCount(); i++) {
                if ((Boolean) tblAgentCommission.getValueAt(i, 0)) {
                    countSelect = true;
                }
            }
            if (!countSelect) {
                ClientUtil.showMessageWindow(" NO Rows Selected !!! ");
                countSelect = false;
                return;
            } else {
                int yesNo = 0;
                String[] options = {"Yes", "No"};
                yesNo = COptionPane.showOptionDialog(null, "Are you sure want to Cancel synced entry ?", CommonConstants.WARNINGTITLE,
                COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,null, options, options[0]);
//                System.out.println("#$#$$ yesNo : " + yesNo);
                if (yesNo == 0) {
                    int count = tblAgentCommission.getRowCount();
                    if (count > 0) {
                        observable.setCancelSyncedEntry();
                        btnCancelActionPerformed(null);
                    }
                }
            }
        }else{
            ClientUtil.showMessageWindow(" Please select atleast one entry !!! ");
            return;
        }
    }//GEN-LAST:event_btnCancelSyncedEntryActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new AppAgentCollectionDetailsUI().show();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnAdd;
    private com.see.truetransact.uicomponent.CButton btnAuthorize;
    private com.see.truetransact.uicomponent.CButton btnCancel;
    private com.see.truetransact.uicomponent.CButton btnCancelSyncedEntry;
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnDelete;
    private com.see.truetransact.uicomponent.CButton btnEdit;
    private com.see.truetransact.uicomponent.CButton btnException;
    private com.see.truetransact.uicomponent.CButton btnNew;
    private com.see.truetransact.uicomponent.CButton btnPrint;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CComboBox cboAgentId;
    private com.see.truetransact.uicomponent.CButtonGroup chkTranstypeGroup;
    private com.see.truetransact.uicomponent.CCheckBox chkVerifyAll;
    private com.see.truetransact.uicomponent.CLabel lblAgentID;
    private com.see.truetransact.uicomponent.CLabel lblAgentName;
    private com.see.truetransact.uicomponent.CLabel lblBatchId;
    private com.see.truetransact.uicomponent.CLabel lblBatchIdValue;
    private com.see.truetransact.uicomponent.CLabel lblMsg;
    private com.see.truetransact.uicomponent.CLabel lblNameForAgent;
    private com.see.truetransact.uicomponent.CLabel lblSpace1;
    private com.see.truetransact.uicomponent.CLabel lblSpace2;
    private com.see.truetransact.uicomponent.CLabel lblSpace3;
    private com.see.truetransact.uicomponent.CLabel lblSpace4;
    private com.see.truetransact.uicomponent.CLabel lblSpace70;
    private com.see.truetransact.uicomponent.CLabel lblSpace71;
    private com.see.truetransact.uicomponent.CLabel lblSpace72;
    private com.see.truetransact.uicomponent.CLabel lblSpace73;
    private com.see.truetransact.uicomponent.CLabel lblSpace74;
    private com.see.truetransact.uicomponent.CLabel lblSpace75;
    private com.see.truetransact.uicomponent.CLabel lblStatus1;
    private com.see.truetransact.uicomponent.CLabel lblTotalCreditAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalCreditAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalCreditCount;
    private com.see.truetransact.uicomponent.CLabel lblTotalCreditCountValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalDebitAmount;
    private com.see.truetransact.uicomponent.CLabel lblTotalDebitAmountValue;
    private com.see.truetransact.uicomponent.CLabel lblTotalDebitCount;
    private com.see.truetransact.uicomponent.CLabel lblTotalDebitCountValue;
    private com.see.truetransact.uicomponent.CMenuBar mbrAgent;
    private javax.swing.JMenuItem mitCancel;
    private javax.swing.JMenuItem mitClose;
    private javax.swing.JMenuItem mitDelete;
    private javax.swing.JMenuItem mitEdit;
    private javax.swing.JMenuItem mitNew;
    private javax.swing.JMenuItem mitPrint;
    private javax.swing.JMenuItem mitSave;
    private javax.swing.JMenu mnuProcess;
    private com.see.truetransact.uicomponent.CPanel panAgent;
    private com.see.truetransact.uicomponent.CPanel panAgentData;
    private com.see.truetransact.uicomponent.CPanel panAgentData1;
    private com.see.truetransact.uicomponent.CPanel panAgentData2;
    private com.see.truetransact.uicomponent.CPanel panAgentDisplay;
    private com.see.truetransact.uicomponent.CPanel panStatus;
    private javax.swing.JSeparator sptCancel;
    private javax.swing.JSeparator sptPrint;
    private javax.swing.JSeparator sptProcess;
    private com.see.truetransact.uicomponent.CScrollPane srpPrintPan;
    private com.see.truetransact.uicomponent.CTable tblAgentCommission;
    private javax.swing.JToolBar tbrLoantProduct;
    // End of variables declaration//GEN-END:variables
}

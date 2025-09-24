/*
 * Copyright 2003-2020 FINCuro Solutions Pvt Ltd. All rights reserved.
 *
 * This software and its components are the property of FINCuro Solutions Pvt Limited and its affiliates, through authorship and acquisition.  
 * 
 *
 * Authorize.java
 *
 * Created on March 3, 2004, 1:46 PM
 */
package com.see.truetransact.ui.deposit.depositfrommobileapp;

import com.see.truetransact.ui.termloan.kcc.multiplerenewal.*;
import com.see.truetransact.ui.termloan.arbitration.*;
import com.see.truetransact.ui.termloan.notices.*;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
//import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.see.truetransact.commonutil.CommonConstants;
import com.see.truetransact.commonutil.CommonUtil;
import com.see.truetransact.commonutil.DateUtil;
import com.see.truetransact.clientutil.EnhancedComboBoxModel;
import com.see.truetransact.uicomponent.CInternalFrame;
import com.see.truetransact.uicomponent.COptionPane;
import com.see.truetransact.clientutil.ComboBoxModel;
import com.see.truetransact.clientutil.ClientUtil;
import com.see.truetransact.clientutil.ClientConstants;
import com.see.truetransact.clientutil.ttrintegration.TTIntegration;
import com.see.truetransact.clientproxy.ProxyParameters;
import com.see.truetransact.clientutil.*;
import com.see.truetransact.ui.common.viewall.ViewAll;

import com.see.truetransact.ui.common.viewall.TableDialogUI;
import com.see.truetransact.ui.TrueTransactMain;
import java.awt.*;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * @author balachandar
 */
public class DepositAutoCreationUI extends com.see.truetransact.uicomponent.CInternalFrame implements Observer, ListSelectionListener {
//    private final AuthorizeRB resourceBundle = new AuthorizeRB();

    private DepositAutoCreationOB observable;
    HashMap paramMap = null;
    CInternalFrame parent = null;
    javax.swing.JList lstSearch;
    java.util.ArrayList arrLst = new java.util.ArrayList();
    Date currDt = null;
    TTIntegration ttIntegration = null;
    int previousRow = -1;
    HashMap accountNumberMap = null;
    HashMap guarantorMemberMap = null;
    HashMap accountChargeMap = null;
    HashMap guarantorChargeMap = null;
    String bankName = "";
    boolean generateNotice = false;
    private String viewType = "";
    int FROMACTNO = 1, TOACTNO = 2;
    private final static Logger log = Logger.getLogger(KCCMultipleRenewalUI.class);
    boolean isEdit = false;
    ArrayList dueColourList = new ArrayList();
    ArrayList suretyColourList = new ArrayList();
    ArrayList riskFundLimitList =  new ArrayList();
    ArrayList deathMarkedCustomerList = new ArrayList();
    HashMap shareDetailsMap = new HashMap();

    /**
     * Creates new form AuthorizeUI
     */
    public DepositAutoCreationUI() {
        setupInit();
        setupScreen();       
        cboCustAgentId.setModel(observable.getCbmCustAgentId());
        cboShareAgentId.setModel(observable.getCbmShareAgentId());
        cboDepositAgentId.setModel(observable.getCbmDepositAgentId());
        cboSBAgentId.setModel(observable.getCbmSBAgentId());
    }

    /**
     * Creates new form AuthorizeUI
     */
    public DepositAutoCreationUI(CInternalFrame parent, HashMap paramMap) {
        this.parent = parent;
        this.paramMap = paramMap;
        setupInit();
        setupScreen();
        cboCustAgentId.setModel(observable.getCbmCustAgentId());
        cboShareAgentId.setModel(observable.getCbmShareAgentId());
        cboDepositAgentId.setModel(observable.getCbmDepositAgentId());
        cboSBAgentId.setModel(observable.getCbmSBAgentId());
    }

    private void setupInit() {
        currDt = ClientUtil.getCurrentDate();
        initComponents();
        internationalize();
        setObservable();
        bankName = ((String) TrueTransactMain.BANKINFO.get("BANK_NAME")).toUpperCase();       
    }

    private void setupScreen() {
//        setModal(true);

        /*
         * Calculate the screen size
         */
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        /*
         * Center frame on the screen
         */
        Dimension frameSize = this.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    private void setObservable() {
        try {
            observable = new DepositAutoCreationOB();
            observable.addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateData(HashMap mapID) {
        try {
            log.info("populateData...");
            ArrayList heading = observable.populateData(mapID, tblCustData);
            if (heading != null && heading.size() > 0) {
                EnhancedComboBoxModel cboModel = new EnhancedComboBoxModel(heading);
//                cboSearchCol.setModel(cboModel);
            }
        } catch (Exception e) {
            System.err.println("Exception " + e.toString() + "Caught");
            e.printStackTrace();
        }
    }

//    public void show() {
//        if (observable.isAvailable()) {
//            super.show();
//        } else {
//            if (parent != null) parent.setModified(false);
//            ClientUtil.noDataAlert();
//        }
//    }
//    public void setVisible(boolean visible) {
//        if (observable.isAvailable()) {
//            super.setVisible(visible);
//        }
//    }
    /**
     * Bring up and populate the temporary project detail screen.
     */
    private void whenTableRowSelected() {
        int rowIndexSelected = tblCustData.getSelectedRow();
        if (previousRow != -1) {
            if (((Boolean) tblCustData.getValueAt(previousRow, 0)).booleanValue()) {
                //    int guarantorRowIndexSelected = tblGuarantorData.getSelectedRow();
                if (accountNumberMap == null) {
                    accountNumberMap = new HashMap();
                }
                if (guarantorMemberMap == null) {
                    guarantorMemberMap = new HashMap();
                }
                if (previousRow != -1 && previousRow != rowIndexSelected) {
                    // isSelectedRowTicked(tblGuarantorData);
                }
            } else {
                // observable.setSelectAll(tblGuarantorData, new Boolean(false));
            }
        }
        //Changed By Suresh
        // String prodType = String.valueOf(cboProdType.getSelectedItem());

        previousRow = rowIndexSelected;
        calTotalAmount();
    }

    private void whenGuarantorTableRowSelected() {
        int rowIndexSelected = tblCustData.getSelectedRow();
        if (!((Boolean) tblCustData.getValueAt(rowIndexSelected, 0)).booleanValue()) {
//            if (isSelectedRowTicked(tblGuarantorData)) {
//                ClientUtil.displayAlert("Loanee Record not selected...");
//                observable.setSelectAll(tblGuarantorData, new Boolean(false));
//            }
        }
    }

    private boolean isSelectedRowTicked(com.see.truetransact.uicomponent.CTable table) {
        boolean selected = false;
        for (int i = 0, j = table.getRowCount(); i < j; i++) {
            selected = ((Boolean) table.getValueAt(i, 0)).booleanValue();
            if (selected) {
                //                if (isGuarantor) {
                //                    guarantorMemberMap.put(table.getValueAt(i, 3),null);
                //                } else {
                //                    accountNumberMap.put(table.getValueAt(i, 1),null);
                //                }
                break;
            }
        }
        return selected;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        rdbArbit = new com.see.truetransact.uicomponent.CButtonGroup();
        cTabbedPane1 = new com.see.truetransact.uicomponent.CTabbedPane();
        panKCCREnewal = new com.see.truetransact.uicomponent.CPanel();
        cLabel1 = new com.see.truetransact.uicomponent.CLabel();
        lblSelectedRecords = new com.see.truetransact.uicomponent.CLabel();
        panSearchCondition = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearch1 = new com.see.truetransact.uicomponent.CPanel();
        panArbit = new com.see.truetransact.uicomponent.CPanel();
        lblAgentId = new com.see.truetransact.uicomponent.CLabel();
        cboCustAgentId = new com.see.truetransact.uicomponent.CComboBox();
        lblCustAgentName = new com.see.truetransact.uicomponent.CLabel();
        btnProcess = new com.see.truetransact.uicomponent.CButton();
        panTable = new com.see.truetransact.uicomponent.CPanel();
        chkSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblCustData = new com.see.truetransact.uicomponent.CTable();
        lblToDate1 = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblNoOfRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch = new com.see.truetransact.uicomponent.CPanel();
        btnSave = new com.see.truetransact.uicomponent.CButton();
        btnClose = new com.see.truetransact.uicomponent.CButton();
        btnCustCreationClear = new com.see.truetransact.uicomponent.CButton();
        btnReject = new com.see.truetransact.uicomponent.CButton();
        sptLine = new com.see.truetransact.uicomponent.CSeparator();
        panRiskFund = new com.see.truetransact.uicomponent.CPanel();
        panRiskFundTable = new com.see.truetransact.uicomponent.CPanel();
        chkRiskFundSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srcRiskFundTable = new com.see.truetransact.uicomponent.CScrollPane();
        tblShareData = new com.see.truetransact.uicomponent.CTable();
        lblRiskFundAccts = new com.see.truetransact.uicomponent.CLabel();
        lblRiskFundNoOfRecords = new com.see.truetransact.uicomponent.CLabel();
        lblShareRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panRiskFundTotDetails = new com.see.truetransact.uicomponent.CPanel();
        cLabel2 = new com.see.truetransact.uicomponent.CLabel();
        lblShareSelectedRecordsVAl = new com.see.truetransact.uicomponent.CLabel();
        cLabel4 = new com.see.truetransact.uicomponent.CLabel();
        lblShareTotalAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch1 = new com.see.truetransact.uicomponent.CPanel();
        btnShareProcess = new com.see.truetransact.uicomponent.CButton();
        btnShareClose = new com.see.truetransact.uicomponent.CButton();
        btnShareClear = new com.see.truetransact.uicomponent.CButton();
        panMultiSearch4 = new com.see.truetransact.uicomponent.CPanel();
        panArbit3 = new com.see.truetransact.uicomponent.CPanel();
        lblProdType3 = new com.see.truetransact.uicomponent.CLabel();
        cboShareAgentId = new com.see.truetransact.uicomponent.CComboBox();
        lblShareAgentName = new com.see.truetransact.uicomponent.CLabel();
        btnShowMobDataShare = new com.see.truetransact.uicomponent.CButton();
        cSeparator1 = new com.see.truetransact.uicomponent.CSeparator();
        panDeposit = new com.see.truetransact.uicomponent.CPanel();
        panRiskFundTable1 = new com.see.truetransact.uicomponent.CPanel();
        chkDepositSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srpDepositData = new com.see.truetransact.uicomponent.CScrollPane();
        tblDepositData = new com.see.truetransact.uicomponent.CTable();
        lblRiskFundAccts1 = new com.see.truetransact.uicomponent.CLabel();
        lblRiskFundNoOfRecords1 = new com.see.truetransact.uicomponent.CLabel();
        lblDepositRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panRiskFundTotDetails1 = new com.see.truetransact.uicomponent.CPanel();
        cLabel5 = new com.see.truetransact.uicomponent.CLabel();
        lblDepositSelectedRecords = new com.see.truetransact.uicomponent.CLabel();
        cLabel6 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalRiskFundVal1 = new com.see.truetransact.uicomponent.CLabel();
        lblDepositTotalAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panSearch2 = new com.see.truetransact.uicomponent.CPanel();
        btnDepositProcess = new com.see.truetransact.uicomponent.CButton();
        btnDepositClose = new com.see.truetransact.uicomponent.CButton();
        btnDepositClear = new com.see.truetransact.uicomponent.CButton();
        panMultiSearch5 = new com.see.truetransact.uicomponent.CPanel();
        panArbit4 = new com.see.truetransact.uicomponent.CPanel();
        lblProdType4 = new com.see.truetransact.uicomponent.CLabel();
        cboDepositAgentId = new com.see.truetransact.uicomponent.CComboBox();
        lblDepositAgentName = new com.see.truetransact.uicomponent.CLabel();
        btnShowMobDataDeposit = new com.see.truetransact.uicomponent.CButton();
        cSeparator2 = new com.see.truetransact.uicomponent.CSeparator();
        panSB = new com.see.truetransact.uicomponent.CPanel();
        panMultiSearchSB = new com.see.truetransact.uicomponent.CPanel();
        panAgentSB = new com.see.truetransact.uicomponent.CPanel();
        lblProdType5 = new com.see.truetransact.uicomponent.CLabel();
        cboSBAgentId = new com.see.truetransact.uicomponent.CComboBox();
        lblSBAgentName = new com.see.truetransact.uicomponent.CLabel();
        btnShowMobDataSB = new com.see.truetransact.uicomponent.CButton();
        cSeparator3 = new com.see.truetransact.uicomponent.CSeparator();
        panAgentSBData = new com.see.truetransact.uicomponent.CPanel();
        chkSBSelectAll = new com.see.truetransact.uicomponent.CCheckBox();
        srpSBData = new com.see.truetransact.uicomponent.CScrollPane();
        tblSBData = new com.see.truetransact.uicomponent.CTable();
        lblSBAccts = new com.see.truetransact.uicomponent.CLabel();
        lblSBRecords = new com.see.truetransact.uicomponent.CLabel();
        lblSBRecordsVal = new com.see.truetransact.uicomponent.CLabel();
        panSBTotDetails = new com.see.truetransact.uicomponent.CPanel();
        cLabel7 = new com.see.truetransact.uicomponent.CLabel();
        lblSBSelectedRecords = new com.see.truetransact.uicomponent.CLabel();
        cLabel8 = new com.see.truetransact.uicomponent.CLabel();
        lblTotalSBVal = new com.see.truetransact.uicomponent.CLabel();
        lblSBTotalAmtVal = new com.see.truetransact.uicomponent.CLabel();
        panSBButtons = new com.see.truetransact.uicomponent.CPanel();
        btnSBProcess = new com.see.truetransact.uicomponent.CButton();
        btnSBClose = new com.see.truetransact.uicomponent.CButton();
        btnSBClear = new com.see.truetransact.uicomponent.CButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Deposit Auto Cteration");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(800, 630));

        panKCCREnewal.setMinimumSize(new java.awt.Dimension(800, 35));
        panKCCREnewal.setPreferredSize(new java.awt.Dimension(800, 35));

        cLabel1.setText("Selected Records:");

        panSearchCondition.setMinimumSize(new java.awt.Dimension(574, 150));
        panSearchCondition.setPreferredSize(new java.awt.Dimension(574, 150));

        panMultiSearch1.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panMultiSearch1.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setMinimumSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setPreferredSize(new java.awt.Dimension(450, 150));
        panMultiSearch1.setLayout(new java.awt.GridBagLayout());

        panArbit.setMinimumSize(new java.awt.Dimension(400, 65));
        panArbit.setPreferredSize(new java.awt.Dimension(400, 65));
        panArbit.setLayout(new java.awt.GridBagLayout());

        lblAgentId.setText("Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 71, 0, 0);
        panArbit.add(lblAgentId, gridBagConstraints);

        cboCustAgentId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboCustAgentId.setPopupWidth(160);
        cboCustAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCustAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 16, 0);
        panArbit.add(cboCustAgentId, gridBagConstraints);

        lblCustAgentName.setForeground(new java.awt.Color(0, 0, 204));
        lblCustAgentName.setText("Name");
        lblCustAgentName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 259;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 18, 0, 65);
        panArbit.add(lblCustAgentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 247;
        gridBagConstraints.ipady = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(21, 16, 0, 16);
        panMultiSearch1.add(panArbit, gridBagConstraints);

        btnProcess.setText("Show ");
        btnProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 255, 8, 0);
        panMultiSearch1.add(btnProcess, gridBagConstraints);

        javax.swing.GroupLayout panSearchConditionLayout = new javax.swing.GroupLayout(panSearchCondition);
        panSearchCondition.setLayout(panSearchConditionLayout);
        panSearchConditionLayout.setHorizontalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addComponent(panMultiSearch1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panSearchConditionLayout.setVerticalGroup(
            panSearchConditionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSearchConditionLayout.createSequentialGroup()
                .addComponent(panMultiSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panTable.setMinimumSize(new java.awt.Dimension(600, 350));
        panTable.setPreferredSize(new java.awt.Dimension(600, 350));
        panTable.setLayout(new java.awt.GridBagLayout());

        chkSelectAll.setText("Select All");
        chkSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panTable.add(chkSelectAll, gridBagConstraints);

        srcTable.setViewport(srcTable.getRowHeader());

        tblCustData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblCustData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblCustData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblCustData.setPreferredScrollableViewportSize(new java.awt.Dimension(804, 296));
        tblCustData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCustDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblCustDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblCustDataMouseReleased(evt);
            }
        });
        tblCustData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblCustDataMouseMoved(evt);
            }
        });
        srcTable.setViewportView(tblCustData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panTable.add(srcTable, gridBagConstraints);

        lblToDate1.setText("Customer List");
        lblToDate1.setMaximumSize(new java.awt.Dimension(230, 85));
        lblToDate1.setMinimumSize(new java.awt.Dimension(186, 18));
        lblToDate1.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panTable.add(lblToDate1, gridBagConstraints);

        lblNoOfRecords.setText("No. of Records Found : ");
        lblNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecords, gridBagConstraints);

        lblNoOfRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblNoOfRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblNoOfRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panTable.add(lblNoOfRecordsVal, gridBagConstraints);

        panSearch.setLayout(new java.awt.GridBagLayout());

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnSave.setText("Process");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnSave, gridBagConstraints);

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch.add(btnClose, gridBagConstraints);

        btnCustCreationClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnCustCreationClear.setText("Clear");
        btnCustCreationClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustCreationClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch.add(btnCustCreationClear, gridBagConstraints);

        btnReject.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_REJECT.gif"))); // NOI18N
        btnReject.setText("Reject");
        btnReject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRejectActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        panSearch.add(btnReject, gridBagConstraints);

        sptLine.setMinimumSize(new java.awt.Dimension(2, 2));
        sptLine.setPreferredSize(new java.awt.Dimension(2, 2));

        javax.swing.GroupLayout panKCCREnewalLayout = new javax.swing.GroupLayout(panKCCREnewal);
        panKCCREnewal.setLayout(panKCCREnewalLayout);
        panKCCREnewalLayout.setHorizontalGroup(
            panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panKCCREnewalLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panKCCREnewalLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panKCCREnewalLayout.createSequentialGroup()
                        .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(panSearchCondition, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)
                        .addComponent(panTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 731, Short.MAX_VALUE)))
                .addGap(37, 37, 37))
            .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panKCCREnewalLayout.createSequentialGroup()
                    .addGap(21, 21, 21)
                    .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(48, Short.MAX_VALUE)))
        );
        panKCCREnewalLayout.setVerticalGroup(
            panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panKCCREnewalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panSearchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panTable, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(panSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
            .addGroup(panKCCREnewalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panKCCREnewalLayout.createSequentialGroup()
                    .addGap(141, 141, 141)
                    .addComponent(sptLine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(425, Short.MAX_VALUE)))
        );

        cTabbedPane1.addTab("Customer Creation", panKCCREnewal);

        panRiskFundTable.setMinimumSize(new java.awt.Dimension(600, 350));
        panRiskFundTable.setPreferredSize(new java.awt.Dimension(600, 350));
        panRiskFundTable.setLayout(new java.awt.GridBagLayout());

        chkRiskFundSelectAll.setText("Select All");
        chkRiskFundSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkRiskFundSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRiskFundTable.add(chkRiskFundSelectAll, gridBagConstraints);

        srcRiskFundTable.setViewport(srcTable.getRowHeader());

        tblShareData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblShareData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblShareData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblShareData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblShareData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblShareDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblShareDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblShareDataMouseReleased(evt);
            }
        });
        tblShareData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblShareDataMouseMoved(evt);
            }
        });
        srcRiskFundTable.setViewportView(tblShareData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRiskFundTable.add(srcRiskFundTable, gridBagConstraints);

        lblRiskFundAccts.setText("Customer List");
        lblRiskFundAccts.setMaximumSize(new java.awt.Dimension(230, 85));
        lblRiskFundAccts.setMinimumSize(new java.awt.Dimension(186, 18));
        lblRiskFundAccts.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panRiskFundTable.add(lblRiskFundAccts, gridBagConstraints);

        lblRiskFundNoOfRecords.setText("No. of Records Found : ");
        lblRiskFundNoOfRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblRiskFundNoOfRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblRiskFundNoOfRecords.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRiskFundTable.add(lblRiskFundNoOfRecords, gridBagConstraints);

        lblShareRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblShareRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblShareRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRiskFundTable.add(lblShareRecordsVal, gridBagConstraints);

        panRiskFundTotDetails.setLayout(new java.awt.GridBagLayout());

        cLabel2.setText("Selected Records");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 0);
        panRiskFundTotDetails.add(cLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 81;
        gridBagConstraints.ipady = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 30, 10, 0);
        panRiskFundTotDetails.add(lblShareSelectedRecordsVAl, gridBagConstraints);

        cLabel4.setText("Total Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 123, 10, 0);
        panRiskFundTotDetails.add(cLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 119;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 18, 0, 0);
        panRiskFundTotDetails.add(lblShareTotalAmtVal, gridBagConstraints);

        panSearch1.setLayout(new java.awt.GridBagLayout());

        btnShareProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnShareProcess.setText("Process");
        btnShareProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch1.add(btnShareProcess, gridBagConstraints);

        btnShareClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnShareClose.setText("Close");
        btnShareClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch1.add(btnShareClose, gridBagConstraints);

        btnShareClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnShareClear.setText("Clear");
        btnShareClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShareClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch1.add(btnShareClear, gridBagConstraints);

        panMultiSearch4.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panMultiSearch4.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearch4.setMinimumSize(new java.awt.Dimension(450, 150));
        panMultiSearch4.setPreferredSize(new java.awt.Dimension(450, 150));
        panMultiSearch4.setLayout(new java.awt.GridBagLayout());

        panArbit3.setMinimumSize(new java.awt.Dimension(400, 65));
        panArbit3.setPreferredSize(new java.awt.Dimension(400, 65));
        panArbit3.setLayout(new java.awt.GridBagLayout());

        lblProdType3.setText("Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 71, 0, 0);
        panArbit3.add(lblProdType3, gridBagConstraints);

        cboShareAgentId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboShareAgentId.setPopupWidth(160);
        cboShareAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboShareAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 16, 0);
        panArbit3.add(cboShareAgentId, gridBagConstraints);

        lblShareAgentName.setForeground(new java.awt.Color(0, 0, 204));
        lblShareAgentName.setText("Name");
        lblShareAgentName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 259;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 18, 0, 65);
        panArbit3.add(lblShareAgentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 247;
        gridBagConstraints.ipady = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 36, 0, 36);
        panMultiSearch4.add(panArbit3, gridBagConstraints);

        btnShowMobDataShare.setText("Show ");
        btnShowMobDataShare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowMobDataShareActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 275, 0, 0);
        panMultiSearch4.add(btnShowMobDataShare, gridBagConstraints);

        javax.swing.GroupLayout panRiskFundLayout = new javax.swing.GroupLayout(panRiskFund);
        panRiskFund.setLayout(panRiskFundLayout);
        panRiskFundLayout.setHorizontalGroup(
            panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRiskFundLayout.createSequentialGroup()
                .addGroup(panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panRiskFundLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, 823, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panRiskFundLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panRiskFundTable, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                            .addComponent(panRiskFundTotDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panRiskFundLayout.createSequentialGroup()
                .addGroup(panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panRiskFundLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(cSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panRiskFundLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(panMultiSearch4, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(96, 96, 96))
        );
        panRiskFundLayout.setVerticalGroup(
            panRiskFundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRiskFundLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panMultiSearch4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panRiskFundTable, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panRiskFundTotDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panSearch1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        cTabbedPane1.addTab("Share Opening", panRiskFund);

        panRiskFundTable1.setMinimumSize(new java.awt.Dimension(600, 350));
        panRiskFundTable1.setPreferredSize(new java.awt.Dimension(600, 350));
        panRiskFundTable1.setLayout(new java.awt.GridBagLayout());

        chkDepositSelectAll.setText("Select All");
        chkDepositSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDepositSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panRiskFundTable1.add(chkDepositSelectAll, gridBagConstraints);

        srpDepositData.setViewport(srcTable.getRowHeader());

        tblDepositData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblDepositData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblDepositData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblDepositData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblDepositData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDepositDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblDepositDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblDepositDataMouseReleased(evt);
            }
        });
        tblDepositData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblDepositDataMouseMoved(evt);
            }
        });
        srpDepositData.setViewportView(tblDepositData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panRiskFundTable1.add(srpDepositData, gridBagConstraints);

        lblRiskFundAccts1.setText("Loan Account Holders List");
        lblRiskFundAccts1.setMaximumSize(new java.awt.Dimension(230, 85));
        lblRiskFundAccts1.setMinimumSize(new java.awt.Dimension(186, 18));
        lblRiskFundAccts1.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panRiskFundTable1.add(lblRiskFundAccts1, gridBagConstraints);

        lblRiskFundNoOfRecords1.setText("No. of Records Found : ");
        lblRiskFundNoOfRecords1.setMaximumSize(new java.awt.Dimension(140, 18));
        lblRiskFundNoOfRecords1.setMinimumSize(new java.awt.Dimension(140, 18));
        lblRiskFundNoOfRecords1.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRiskFundTable1.add(lblRiskFundNoOfRecords1, gridBagConstraints);

        lblDepositRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblDepositRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblDepositRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panRiskFundTable1.add(lblDepositRecordsVal, gridBagConstraints);

        cLabel5.setText("Selected Records");

        cLabel6.setText("Total Amount");

        javax.swing.GroupLayout panRiskFundTotDetails1Layout = new javax.swing.GroupLayout(panRiskFundTotDetails1);
        panRiskFundTotDetails1.setLayout(panRiskFundTotDetails1Layout);
        panRiskFundTotDetails1Layout.setHorizontalGroup(
            panRiskFundTotDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRiskFundTotDetails1Layout.createSequentialGroup()
                .addComponent(lblTotalRiskFundVal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(lblDepositSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(122, 122, 122)
                .addComponent(cLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(lblDepositTotalAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panRiskFundTotDetails1Layout.setVerticalGroup(
            panRiskFundTotDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTotalRiskFundVal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(cLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(lblDepositSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(panRiskFundTotDetails1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblDepositTotalAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panSearch2.setLayout(new java.awt.GridBagLayout());

        btnDepositProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnDepositProcess.setText("Process");
        btnDepositProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch2.add(btnDepositProcess, gridBagConstraints);

        btnDepositClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnDepositClose.setText("Close");
        btnDepositClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSearch2.add(btnDepositClose, gridBagConstraints);

        btnDepositClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnDepositClear.setText("Clear");
        btnDepositClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSearch2.add(btnDepositClear, gridBagConstraints);

        panMultiSearch5.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panMultiSearch5.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearch5.setMinimumSize(new java.awt.Dimension(450, 150));
        panMultiSearch5.setPreferredSize(new java.awt.Dimension(450, 150));
        panMultiSearch5.setLayout(new java.awt.GridBagLayout());

        panArbit4.setMinimumSize(new java.awt.Dimension(400, 65));
        panArbit4.setPreferredSize(new java.awt.Dimension(400, 65));
        panArbit4.setLayout(new java.awt.GridBagLayout());

        lblProdType4.setText("Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 71, 0, 0);
        panArbit4.add(lblProdType4, gridBagConstraints);

        cboDepositAgentId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboDepositAgentId.setPopupWidth(160);
        cboDepositAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboDepositAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 16, 0);
        panArbit4.add(cboDepositAgentId, gridBagConstraints);

        lblDepositAgentName.setForeground(new java.awt.Color(0, 0, 204));
        lblDepositAgentName.setText("Name");
        lblDepositAgentName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 259;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 18, 0, 65);
        panArbit4.add(lblDepositAgentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 247;
        gridBagConstraints.ipady = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 36, 0, 36);
        panMultiSearch5.add(panArbit4, gridBagConstraints);

        btnShowMobDataDeposit.setText("Show ");
        btnShowMobDataDeposit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowMobDataDepositActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 275, 0, 0);
        panMultiSearch5.add(btnShowMobDataDeposit, gridBagConstraints);

        javax.swing.GroupLayout panDepositLayout = new javax.swing.GroupLayout(panDeposit);
        panDeposit.setLayout(panDepositLayout);
        panDepositLayout.setHorizontalGroup(
            panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDepositLayout.createSequentialGroup()
                .addGroup(panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panDepositLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panDepositLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panRiskFundTotDetails1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panMultiSearch5, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
            .addGroup(panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panDepositLayout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(panRiskFundTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(18, Short.MAX_VALUE)))
        );
        panDepositLayout.setVerticalGroup(
            panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDepositLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panMultiSearch5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 364, Short.MAX_VALUE)
                .addComponent(panRiskFundTotDetails1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(panSearch2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panDepositLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panDepositLayout.createSequentialGroup()
                    .addGap(150, 150, 150)
                    .addComponent(panRiskFundTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(96, Short.MAX_VALUE)))
        );

        cTabbedPane1.addTab("Deposit Opening", panDeposit);

        panMultiSearchSB.setBorder(javax.swing.BorderFactory.createTitledBorder("Agent Details"));
        panMultiSearchSB.setMaximumSize(new java.awt.Dimension(450, 150));
        panMultiSearchSB.setMinimumSize(new java.awt.Dimension(450, 150));
        panMultiSearchSB.setPreferredSize(new java.awt.Dimension(450, 150));
        panMultiSearchSB.setLayout(new java.awt.GridBagLayout());

        panAgentSB.setMinimumSize(new java.awt.Dimension(400, 65));
        panAgentSB.setPreferredSize(new java.awt.Dimension(400, 65));
        panAgentSB.setLayout(new java.awt.GridBagLayout());

        lblProdType5.setText("Agent Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 71, 0, 0);
        panAgentSB.add(lblProdType5, gridBagConstraints);

        cboSBAgentId.setMinimumSize(new java.awt.Dimension(100, 21));
        cboSBAgentId.setPopupWidth(160);
        cboSBAgentId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSBAgentIdActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 42;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 16, 0);
        panAgentSB.add(cboSBAgentId, gridBagConstraints);

        lblSBAgentName.setForeground(new java.awt.Color(0, 0, 204));
        lblSBAgentName.setText("Name");
        lblSBAgentName.setFont(new java.awt.Font("MS Sans Serif", 1, 13)); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 259;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 18, 0, 65);
        panAgentSB.add(lblSBAgentName, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 247;
        gridBagConstraints.ipady = -17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 36, 0, 36);
        panMultiSearchSB.add(panAgentSB, gridBagConstraints);

        btnShowMobDataSB.setText("Show ");
        btnShowMobDataSB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowMobDataSBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 275, 0, 0);
        panMultiSearchSB.add(btnShowMobDataSB, gridBagConstraints);

        panAgentSBData.setMinimumSize(new java.awt.Dimension(600, 350));
        panAgentSBData.setPreferredSize(new java.awt.Dimension(600, 350));
        panAgentSBData.setLayout(new java.awt.GridBagLayout());

        chkSBSelectAll.setText("Select All");
        chkSBSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSBSelectAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        panAgentSBData.add(chkSBSelectAll, gridBagConstraints);

        srpSBData.setViewport(srcTable.getRowHeader());

        tblSBData.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSBData.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSBData.setMinimumSize(new java.awt.Dimension(350, 80));
        tblSBData.setPreferredScrollableViewportSize(new java.awt.Dimension(450000, 400000));
        tblSBData.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSBDataMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblSBDataMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tblSBDataMouseReleased(evt);
            }
        });
        tblSBData.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                tblSBDataMouseMoved(evt);
            }
        });
        srpSBData.setViewportView(tblSBData);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panAgentSBData.add(srpSBData, gridBagConstraints);

        lblSBAccts.setText("Loan Account Holders List");
        lblSBAccts.setMaximumSize(new java.awt.Dimension(230, 85));
        lblSBAccts.setMinimumSize(new java.awt.Dimension(186, 18));
        lblSBAccts.setPreferredSize(new java.awt.Dimension(186, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 4);
        panAgentSBData.add(lblSBAccts, gridBagConstraints);

        lblSBRecords.setText("No. of Records Found : ");
        lblSBRecords.setMaximumSize(new java.awt.Dimension(140, 18));
        lblSBRecords.setMinimumSize(new java.awt.Dimension(140, 18));
        lblSBRecords.setPreferredSize(new java.awt.Dimension(140, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAgentSBData.add(lblSBRecords, gridBagConstraints);

        lblSBRecordsVal.setMaximumSize(new java.awt.Dimension(230, 85));
        lblSBRecordsVal.setMinimumSize(new java.awt.Dimension(80, 18));
        lblSBRecordsVal.setPreferredSize(new java.awt.Dimension(80, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 2, 4);
        panAgentSBData.add(lblSBRecordsVal, gridBagConstraints);

        cLabel7.setText("Selected Records");

        cLabel8.setText("Total Amount");

        javax.swing.GroupLayout panSBTotDetailsLayout = new javax.swing.GroupLayout(panSBTotDetails);
        panSBTotDetails.setLayout(panSBTotDetailsLayout);
        panSBTotDetailsLayout.setHorizontalGroup(
            panSBTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSBTotDetailsLayout.createSequentialGroup()
                .addGroup(panSBTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalSBVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panSBTotDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(lblSBSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(lblSBTotalAmtVal, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        panSBTotDetailsLayout.setVerticalGroup(
            panSBTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSBTotDetailsLayout.createSequentialGroup()
                .addGroup(panSBTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSBTotDetailsLayout.createSequentialGroup()
                        .addComponent(lblTotalSBVal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblSBSelectedRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSBTotDetailsLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panSBTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSBTotDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblSBTotalAmtVal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        panSBButtons.setLayout(new java.awt.GridBagLayout());

        btnSBProcess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_AUTHORIZE.gif"))); // NOI18N
        btnSBProcess.setText("Process");
        btnSBProcess.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSBProcessActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSBButtons.add(btnSBProcess, gridBagConstraints);

        btnSBClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CLOSE.gif"))); // NOI18N
        btnSBClose.setText("Close");
        btnSBClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSBCloseActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panSBButtons.add(btnSBClose, gridBagConstraints);

        btnSBClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/see/truetransact/ui/images/TT_CANCEL.gif"))); // NOI18N
        btnSBClear.setText("Clear");
        btnSBClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSBClearActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
        panSBButtons.add(btnSBClear, gridBagConstraints);

        javax.swing.GroupLayout panSBLayout = new javax.swing.GroupLayout(panSB);
        panSB.setLayout(panSBLayout);
        panSBLayout.setHorizontalGroup(
            panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSBLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panSBLayout.createSequentialGroup()
                        .addComponent(panMultiSearchSB, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(28, 28, 28))
                    .addGroup(panSBLayout.createSequentialGroup()
                        .addGroup(panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(panSBTotDetails, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panSBLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(panAgentSBData, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(19, Short.MAX_VALUE))))
            .addGroup(panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panSBLayout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(cSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(19, 19, 19)))
            .addGroup(panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panSBLayout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(panSBButtons, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE)))
        );
        panSBLayout.setVerticalGroup(
            panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSBLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panMultiSearchSB, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(panAgentSBData, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panSBTotDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
            .addGroup(panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panSBLayout.createSequentialGroup()
                    .addGap(133, 133, 133)
                    .addComponent(cSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(433, Short.MAX_VALUE)))
            .addGroup(panSBLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSBLayout.createSequentialGroup()
                    .addContainerGap(520, Short.MAX_VALUE)
                    .addComponent(panSBButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        cTabbedPane1.addTab("SB Opening", panSB);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(cTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 774, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(cTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setVisibleForAuctionNotice(boolean val) {
//        lblAuctionDate.setVisible(val);
//        tdtAuctionDate.setVisible(val);
    }

    private void setVisibleForDemandNotice(boolean val) {
//        lblOverDueDate.setVisible(val);
//        tdtOverDueDate.setVisible(val);
//        chkFulldue.setVisible(val);
//        lblNoOfInstallments.setVisible(val);
//   
    }

    private void btnCustCreationClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustCreationClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(panKCCREnewal);
        isEdit = false;
        btnSave.setEnabled(true);
        lblNoOfRecordsVal.setText("");
        lblSelectedRecords.setText("");
    }//GEN-LAST:event_btnCustCreationClearActionPerformed

    private void tblCustDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustDataMouseReleased
        // TODO add your handling code here:
        if (/*
                 * (evt.getClickCount() == 2) &&
                 */(evt.getModifiers() == 16)) {
            whenTableRowSelected();
            setSelectedRecord();
        }
    }//GEN-LAST:event_tblCustDataMouseReleased
    public void fillData(Object param) {
        final HashMap hash = (HashMap) param;
        System.out.println("Hash: " + hash);
        if (viewType.equalsIgnoreCase("RENEWAL_ID")) {
            if (hash.containsKey("RENEWAL_ID") && hash.get("RENEWAL_ID") != null) {
                observable.setRiskFundProdId(CommonUtil.convertObjToStr(hash.get("PROD_ID")));
                observable.setRiskFundProdDesc(CommonUtil.convertObjToStr(hash.get("PROD_DESC")));
            }
        }
    }

    private void popUp(String field) {
        final HashMap viewMap = new HashMap();
        if (field.equalsIgnoreCase("RENEWAL_ID")) {
            HashMap checkMap = new HashMap();
            checkMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            checkMap.put("CURR_DT", currDt);
            viewMap.put(CommonConstants.MAP_WHERE, checkMap);
            viewMap.put(CommonConstants.MAP_NAME, "getRenewedAccountsForRiskFundPocessing");
        }
        new ViewAll(this, viewMap).show();
    }

    private void tblCustDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustDataMouseMoved

    }//GEN-LAST:event_tblCustDataMouseMoved

    private void tblCustDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustDataMousePressed
//        if ((evt.getClickCount() == 2) && (evt.getModifiers() == 16)) {
//            HashMap whereMap = new HashMap();
//            whereMap.put("ACT_NUM", tblData.getValueAt(tblData.getSelectedRow(), 1));
//
//            TableDialogUI tableData = new TableDialogUI("getNoticeChargeDetails", whereMap);
//            tableData.setTitle("Notice Sent Details for " + tblData.getValueAt(tblData.getSelectedRow(), 1));
//            tableData.setPreferredSize(new Dimension(750, 450));
//            tableData.show();
//        }       
    }//GEN-LAST:event_tblCustDataMousePressed

    private void chkSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectAllActionPerformed
        // Add your handling code here:
        observable.setSelectAll(tblCustData, new Boolean(chkSelectAll.isSelected()));
        for (int i = 0; i < tblCustData.getRowCount(); i++) {
           
            if (suretyColourList.contains(String.valueOf(i))) {
                //tblData.setValueAt(false, i, 0);
            }
             if (deathMarkedCustomerList.contains(String.valueOf(i))) {
                tblCustData.setValueAt(false, i, 0);
            }
        }
        setSelectedRecord();
        calTotalAmount();
    }//GEN-LAST:event_chkSelectAllActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        if(tblCustData.getRowCount() > 0){
           observable.removeRowsFromGuarantorTable(tblCustData);
        }
        //  observable.removeRowsFromGuarantorTable(tblGuarantorData);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    //Added By Suresh
    private void setSelectedRecord() {
        int count = 0;
        if (tblCustData.getRowCount() > 0) {
            for (int i = 0, j = tblCustData.getRowCount(); i < j; i++) {
                if (((Boolean) tblCustData.getValueAt(i, 0)).booleanValue()) {
                    count += 1;
                }
            }
        }
        //   lblSelectedRecordVal.setText(String.valueOf(count));
        calculateTotalApplCharges();
    }

    private void calculateTotalApplCharges() {
//        double totalChargeAmount = (CommonUtil.convertObjToDouble(txtNoticeCharge.getText())
//                + CommonUtil.convertObjToDouble(txtPostageCharge.getText()));
//        lblTotalChargeAmountVal.setText(String.valueOf(totalChargeAmount * CommonUtil.convertObjToDouble(lblSelectedRecordVal.getText())));
    }

    public void populateEditData() {
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        whereMap.put("CURR_DATE", getProperDate(currDt));
        viewMap.put(CommonConstants.MAP_WHERE, whereMap);
        viewMap.put(CommonConstants.MAP_NAME, "getAwardViewDetails");
        new ViewAll(this, viewMap).show();

    }

    public void populateData() {       
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        if (isOK) {
            viewMap.put(CommonConstants.MAP_NAME, "getMobileAppDataForCustomerCreation");          
            whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(observable.getCbmCustAgentId().getKeyForSelected()));
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateData(viewMap, tblCustData);
                //setColorLists();
                //setColour();
                lblNoOfRecordsVal.setText(String.valueOf(tblCustData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }

    public void populateRiskFundData(List chargeList) {
        String behavesLike = "";
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        System.out.println("chkng");
        if (isOK) {
            viewMap.put(CommonConstants.MAP_NAME, "getAllAccountsByKCCRenewalId");
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            whereMap.put("CHARGE_LIST", chargeList);
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateRiskFundData(viewMap, tblShareData);
                setColorListsForRiskFundProcess();
                setColourForRiskFundProcess();
                lblShareRecordsVal.setText(String.valueOf(tblShareData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
     private void setColourForRiskFundProcess() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (riskFundLimitList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblShareData.setDefaultRenderer(Object.class, renderer);
    }

    private void setColour() {
        /*
         * Set a cellrenderer to this table in order format the date
         */
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (deathMarkedCustomerList.contains(String.valueOf(row))) {
                    setForeground(Color.RED);
                } else if (suretyColourList.contains(String.valueOf(row))) {
                    setForeground(Color.BLUE);
                } else {
                    setForeground(Color.BLACK);
                }
                // Set oquae
                this.setOpaque(true);
                return this;
            }
        };
        tblCustData.setDefaultRenderer(Object.class, renderer);
    }

//     private String validateAccNo(){
//        String from = txtFromAccountno.getText();
//        String to = txtTOAccountno.getText();
//        String message = "";
//        if(!(from.equalsIgnoreCase("")|| to.equalsIgnoreCase(""))){
//            if(from.compareTo(to) > 0){
////                message = resourceBundle.getString("ACCOUNTWARNING");
//            }
//        }
//        if(((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("TL") || ((ComboBoxModel)cboProdType.getModel()).getKeyForSelected().toString().equals("AD"))
//        {
//            HashMap hash=new HashMap();
//         hash.put(PROD_ID,((ComboBoxModel)cboProdId.getModel()).getKeyForSelected());
//         hash.put("ACT_NUM",txtFromAccount.getText());
//         if(txtTOAccountno !=null && (! txtTOAccountno.getText().equals(""))) {
//             hash.put("ACT_NUM",txtTOAccountno.getText());
//         }
//         hash.put("SELECTED_BRANCH",ProxyParameters.BRANCH_ID);
//         List actlst=ClientUtil.executeQuery("getActNotCLOSEDTL",hash);
//         if(actlst != null &&  actlst.size()>0 ){
//         } else{
//             ClientUtil.displayAlert("Enter the correct Number");
//             txtFromAccountno.setText("");
//             txtTOAccountno.setText("");
//             return message;
//         }
//        }
//        return message;
//    }
    public void generateNotice() {
        //        updateOBFields();
    }

    private void setColorLists() {
        suretyColourList = new ArrayList();
        dueColourList = new ArrayList();
        deathMarkedCustomerList = new ArrayList();
        if (tblCustData.getRowCount() > 0) {
            for (int i = 0; i < tblCustData.getRowCount(); i++) {
                double limit = CommonUtil.convertObjToDouble(tblCustData.getValueAt(i, 5).toString()).doubleValue();
                double suretyAmt = CommonUtil.convertObjToDouble(tblCustData.getValueAt(i, 7).toString()).doubleValue();
                if (suretyAmt < limit) {
                    suretyColourList.add(String.valueOf(i));
                }
              
                String acctNum = CommonUtil.convertObjToStr(tblCustData.getValueAt(i, 1).toString());
                HashMap actMap =  new HashMap();
                actMap.put("ACCOUNTNO",acctNum);
                List deathLsit = ClientUtil.executeQuery("getDeathMarkedCustomerAD", actMap);
                if(deathLsit.size() > 0){
                    deathMarkedCustomerList.add(String.valueOf(i));
                }
            }
        }
    }
    
    private void setColorListsForRiskFundProcess() {
        riskFundLimitList = new ArrayList();
        if (tblCustData.getRowCount() > 0) {
            for (int i = 0; i < tblShareData.getRowCount(); i++) {
                double availableBal = CommonUtil.convertObjToDouble(tblShareData.getValueAt(i, 5).toString()).doubleValue();
                double totalAmount = CommonUtil.convertObjToDouble(tblShareData.getValueAt(i, 10).toString()).doubleValue();
                if (availableBal < totalAmount) {
                    riskFundLimitList.add(String.valueOf(i));
                }
            }
        }
    }

    public String guarantorGetSelected() {
//        Boolean bln;
//        ArrayList arrRow;
//        HashMap selectedMap;
        //        ArrayList selectedList = new ArrayList();
        String selected = "";
        Object obj[] = guarantorMemberMap.keySet().toArray();
        for (int i = 0, j = obj.length; i < j; i++) {
            selected += "'" + obj[i];
            selected += "',";
        }
        // If no guarantor selected also records should be selected from other than guarantor.
        selected = selected.equals("") ? "'aa'" : selected.substring(0, selected.length() - 1);
        System.out.println("#$#$ guaranter selected : " + selected);
        return selected;
    }

    private void createCboNoticeType() {
//      
    }

    private Date getProperDate(Date sourceDate) {
        Date targetDate = (Date) currDt.clone();
        targetDate.setDate(sourceDate.getDate());
        targetDate.setMonth(sourceDate.getMonth());
        targetDate.setYear(sourceDate.getYear());
        return targetDate;
    }

    private ComboBoxModel getListModel() {
        ComboBoxModel listData = new ComboBoxModel();
        return listData;
    }

    public void valueChanged(javax.swing.event.ListSelectionEvent e) {
        // Add your handling code here://GEN-FIRST:event_btnCancelActionPerformed
//        if ( < 0) {
//            COptionPane.showMessageDialog(null,
//                resourceBundle.getString("SelectRow"), 
//                resourceBundle.getString("SelectRowHeading"),
//                COptionPane.OK_OPTION + COptionPane.INFORMATION_MESSAGE);
//        } 
//        if (parent != null) parent.setModified(false);
//        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void updateDBStatus(String status) {
        //observable.updateStatus(paramMap, status);
        HashMap screenParamMap = new HashMap();
        screenParamMap.put(CommonConstants.AUTHORIZEDATA, observable.getSelected());
        screenParamMap.put(CommonConstants.AUTHORIZESTATUS, status);

//        observable.insertCharges(paramMap);
        ClientUtil.showMessageWindow(observable.getResult());
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[4])) {
            observable.removeRowsFromGuarantorTable(tblCustData);
            lblNoOfRecordsVal.setText("");
            //observable.removeRowsFromGuarantorTable(tblGuarantorData);
        }

    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        HashMap resultMap = new HashMap();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
                ArrayList custDataList = new ArrayList();
                try {
                    if (tblCustData.getRowCount() > 0) {
                        for (int i = 0; i < tblCustData.getRowCount(); i++) {
                            HashMap custDataMap = new HashMap();
                            if (((Boolean) tblCustData.getValueAt(i, 0)).booleanValue()) {
                                for (int j = 1; j < tblCustData.getColumnCount(); j++) {
                                    custDataMap.put("MOB_DEP_ID", tblCustData.getValueAt(i, 1));
                                    custDataMap.put("CUST_NAME", tblCustData.getValueAt(i, 2));
                                    custDataMap.put("DOB", tblCustData.getValueAt(i, 3));
                                    custDataMap.put("GENDER", tblCustData.getValueAt(i, 4));
                                    custDataMap.put("MARITAL_STATUS", tblCustData.getValueAt(i, 5));
                                    custDataMap.put("HOUSE_NAME", tblCustData.getValueAt(i, 6));
                                    custDataMap.put("PLACE", tblCustData.getValueAt(i, 7));
                                    custDataMap.put("CITY", tblCustData.getValueAt(i, 8));
                                    custDataMap.put("PINCODE", tblCustData.getValueAt(i, 9));
                                    custDataMap.put("MOBILE_NO", tblCustData.getValueAt(i, 10));
                                    custDataMap.put("IDENTITY_PROOF", tblCustData.getValueAt(i, 11));
                                    custDataMap.put("UNIQUE_ID", tblCustData.getValueAt(i, 12));
                                    custDataMap.put("SHARE_TYPE", tblCustData.getValueAt(i, 13));
                                    custDataMap.put("SHARE_NO", tblCustData.getValueAt(i, 14));
                                }
                                custDataList.add(custDataMap);
                            }
                        }
                    }

                    HashMap custCreationMap = new HashMap();
                    custCreationMap.put("CUSTOMER_CREATION_LIST", custDataList);
                    HashMap statusMap = observable.createCustomerProcess(custCreationMap);
                    observable.setCustCreationResultMap(statusMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        resultMap = observable.getCustCreationResultMap();
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            if (resultMap != null && resultMap.containsKey("CUST_CREATE_STATUS") && resultMap.get("CUST_CREATE_STATUS") != null && resultMap.get("CUST_CREATE_STATUS").equals("SUCCESS")) {
                ClientUtil.showMessageWindow("Customer creation Completed . Customer Id : " + resultMap.get("FROM") + "  To  " + resultMap.get("TO"));
            }
            observable.removeRowsFromGuarantorTable(tblCustData);
            ClientUtil.clearAll(this);
            isEdit = false;
            btnSave.setEnabled(true);
            btnCustCreationClearActionPerformed(null);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    
    
    
    
    private void btnProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessActionPerformed
        // TODO add your handling code here:
        if (cboCustAgentId.getSelectedIndex() == 0) {
            ClientUtil.displayAlert("Select an Agent !!");
        } else {
            lblNoOfRecordsVal.setText("");
            isEdit = false;
            btnSave.setEnabled(true);
            populateData();
            if (tblCustData.getRowCount() > 0) {
                tblCustData.getColumnModel().getColumn(1).setPreferredWidth(90);
                tblCustData.getColumnModel().getColumn(7).setPreferredWidth(100);
                tblCustData.getColumnModel().getColumn(8).setPreferredWidth(100);
            }
        }
    }//GEN-LAST:event_btnProcessActionPerformed

    public void calTotalRenewedAmount() {
        if (tblShareData.getRowCount() > 0) {
            int count = tblShareData.getRowCount();
            double selectedCount = 0.0;
            double riskFund = 0.0;
            double totalTax = 0.0;
            for (int i = 0; i < count; i++) {
                if ((Boolean) tblShareData.getValueAt(i, 0)) {
                    selectedCount++;
                    riskFund += CommonUtil.convertObjToDouble(tblShareData.getValueAt(i, 6));
                    totalTax += CommonUtil.convertObjToDouble(tblShareData.getValueAt(i, 7)) + CommonUtil.convertObjToDouble(tblShareData.getValueAt(i, 8)) + CommonUtil.convertObjToDouble(tblShareData.getValueAt(i, 9));
                }
            }
            lblShareSelectedRecordsVAl.setText(CommonUtil.convertObjToStr(selectedCount));
            lblShareTotalAmtVal.setText(CommonUtil.convertObjToStr(riskFund));
        }
    }

    private void chkRiskFundSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkRiskFundSelectAllActionPerformed
        // TODO add your handling code here:
        observable.setSelectAll(tblShareData, new Boolean(chkRiskFundSelectAll.isSelected()));
        
        for (int i = 0; i < tblShareData.getRowCount(); i++) {           
            if (riskFundLimitList.contains(String.valueOf(i))) {
                tblShareData.setValueAt(false, i, 0);
            }
        }
        calTotalRenewedAmount();
        
    }//GEN-LAST:event_chkRiskFundSelectAllActionPerformed

    private void tblShareDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareDataMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblShareDataMousePressed

    private void tblShareDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareDataMouseReleased
        // TODO add your handling code here:
        calTotalRenewedAmount();
    }//GEN-LAST:event_tblShareDataMouseReleased

    private void tblShareDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareDataMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblShareDataMouseMoved

    private void btnShareProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareProcessActionPerformed
        // TODO add your handling code here:      
       HashMap resultMap = new HashMap();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
                ArrayList shareDataList = new ArrayList();
                try {
                    if (tblShareData.getRowCount() > 0) {
                        for (int i = 0; i < tblShareData.getRowCount(); i++) {
                            HashMap shareDataMap = new HashMap();
                            if (((Boolean) tblShareData.getValueAt(i, 0)).booleanValue()) {
                                for (int j = 1; j < tblShareData.getColumnCount(); j++) {
                                    shareDataMap.put("MOB_DEP_ID", tblShareData.getValueAt(i, 1));
                                    shareDataMap.put("MOB_DEPDETAIL_ID", tblShareData.getValueAt(i, 2));
                                    shareDataMap.put("CUST_ID", tblShareData.getValueAt(i, 3));
                                    shareDataMap.put("FNAME", tblShareData.getValueAt(i, 4));
                                    shareDataMap.put("SHARE_TYPE", tblShareData.getValueAt(i, 5));
                                    shareDataMap.put("FACE_VALUE", tblShareData.getValueAt(i, 6));
                                    shareDataMap.put("SHARE_AMOUNT", tblShareData.getValueAt(i, 7));
                                    shareDataMap.put("NO_OF_SHARES", tblShareData.getValueAt(i, 8));
                                    shareDataMap.put("TOTAL_AMOUNT", tblShareData.getValueAt(i, 9));
                                }
                                shareDataList.add(shareDataMap);
                            }
                        }
                    }

                    HashMap shareOpeningMap = new HashMap();
                    shareOpeningMap.put("SHARE_OPENING_LIST", shareDataList);
                    HashMap statusMap = observable.shareOpeningProcess(shareOpeningMap);
                    observable.setShareOpeningResultMap(statusMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        resultMap = observable.getShareOpeningResultMap();
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            if (resultMap != null && resultMap.containsKey("SHARE_OPENING_STATUS") && resultMap.get("SHARE_OPENING_STATUS") != null && resultMap.get("SHARE_OPENING_STATUS").equals("SUCCESS")) {
                ClientUtil.showMessageWindow("Share opened for customers . Customer Id : " + resultMap.get("FROM") + "  To  " + resultMap.get("TO"));
            }
            observable.removeRowsFromGuarantorTable(tblShareData);
            ClientUtil.clearAll(this);
            isEdit = false;
            btnSave.setEnabled(true);
            btnShareClearActionPerformed(null);
        }
    }//GEN-LAST:event_btnShareProcessActionPerformed

    private void btnShareCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareCloseActionPerformed
        // TODO add your handling code here:
        observable.removeRowsFromRiskFundTable(tblShareData);
        dispose();
    }//GEN-LAST:event_btnShareCloseActionPerformed

    private void btnShareClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShareClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(panRiskFund);
        lblShareRecordsVal.setText("");
        lblShareSelectedRecordsVAl.setText("");
        lblShareTotalAmtVal.setText("");       
        observable.setRiskFundProdId("");
        observable.setRiskFundProdDesc("");
        observable.setRiskFundAcctHead("");        
    }//GEN-LAST:event_btnShareClearActionPerformed

    private void tblCustDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCustDataMouseClicked
        // TODO add your handling code here:
        if (tblCustData.getRowCount() > 0) {
            if (suretyColourList.contains(String.valueOf(tblCustData.getSelectedRow()))) {
                //tblData.setValueAt(new Boolean(false), tblData.getSelectedRow(), 0);
            }
            if (deathMarkedCustomerList.contains(String.valueOf(tblCustData.getSelectedRow()))) {
                tblCustData.setValueAt(new Boolean(false), tblCustData.getSelectedRow(), 0);
            }
          
        }
        calTotalAmount();
    }//GEN-LAST:event_tblCustDataMouseClicked

    private void tblShareDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblShareDataMouseClicked
        // TODO add your handling code here:
          if (tblShareData.getRowCount() > 0) {
            if (riskFundLimitList.contains(String.valueOf(tblShareData.getSelectedRow()))) {
                tblShareData.setValueAt(new Boolean(false), tblShareData.getSelectedRow(), 0);
            }            
        }
          calTotalRenewedAmount();
    }//GEN-LAST:event_tblShareDataMouseClicked

    private void chkDepositSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDepositSelectAllActionPerformed
        // TODO add your handling code here:
       observable.setSelectAll(tblDepositData, new Boolean(chkDepositSelectAll.isSelected()));
       
        calculateDepositAmountVal();
    }//GEN-LAST:event_chkDepositSelectAllActionPerformed

    private void calculateDepositAmountVal() {
        double total = 0.0;
        int count = 0;
        if (tblDepositData.getRowCount() > 0) {
            for (int i = 0; i < tblDepositData.getRowCount(); i++) {
                if (tblDepositData.getValueAt(i, 0).equals(true)) {
                    total += CommonUtil.convertObjToDouble(tblDepositData.getValueAt(i, 8));
                    count++;
                }
            }
        }
        lblDepositSelectedRecords.setText(String.valueOf(count));
        lblDepositTotalAmtVal.setText(String.valueOf(total));
    }
    
    private void tblDepositDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositDataMouseClicked
        // TODO add your handling code here:
        calculateDepositAmountVal();
    }//GEN-LAST:event_tblDepositDataMouseClicked

    private void tblDepositDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositDataMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDepositDataMousePressed

    private void tblDepositDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositDataMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDepositDataMouseReleased

    private void tblDepositDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDepositDataMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDepositDataMouseMoved

    private void btnDepositProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositProcessActionPerformed
        // TODO add your handling code here:
           HashMap resultMap = new HashMap();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
                ArrayList depositDataList = new ArrayList();
                try {
                    if (tblDepositData.getRowCount() > 0) {
                        for (int i = 0; i < tblDepositData.getRowCount(); i++) {
                            HashMap depositDataMap = new HashMap();
                            if (((Boolean) tblDepositData.getValueAt(i, 0)).booleanValue()) {
                                for (int j = 1; j < tblDepositData.getColumnCount(); j++) {                               
                                    depositDataMap.put("MOB_DEP_ID", tblDepositData.getValueAt(i, 1));
                                    depositDataMap.put("CUST_ID", tblDepositData.getValueAt(i, 2));
                                    depositDataMap.put("FNAME", tblDepositData.getValueAt(i, 3));
                                    depositDataMap.put("SHARE_NO", tblDepositData.getValueAt(i, 4));
                                    depositDataMap.put("PROD_ID", tblDepositData.getValueAt(i, 5));
                                    depositDataMap.put("PRODUCT", tblDepositData.getValueAt(i, 6));
                                    depositDataMap.put("BEHAVES_LIKE", tblDepositData.getValueAt(i, 7));
                                    depositDataMap.put("DEPOSIT_AMOUNT", tblDepositData.getValueAt(i, 8));
                                    depositDataMap.put("TOTAL_AMOUNT", tblDepositData.getValueAt(i, 8));
                                    depositDataMap.put("DEPOSIT_PERIOD", tblDepositData.getValueAt(i, 9));
                                    depositDataMap.put("DEPOSIT_PERIOD_TYPE", tblDepositData.getValueAt(i, 10));                                    
                                    depositDataMap.put("ROI", tblDepositData.getValueAt(i, 11));
                                    depositDataMap.put("DEPOSIT_DT", tblDepositData.getValueAt(i, 12));
                                    depositDataMap.put("MATURITY_DT", tblDepositData.getValueAt(i, 13));
                                    depositDataMap.put("MATURITY_AMOUNT", tblDepositData.getValueAt(i, 14));
                                    depositDataMap.put("TOTAL_INT", tblDepositData.getValueAt(i, 15));
                                    depositDataMap.put("MOBILE_NO", tblDepositData.getValueAt(i, 16));
                                    depositDataMap.put("NOMINEE_NAME", tblDepositData.getValueAt(i, 17));
                                    depositDataMap.put("RELATIONSHIP", tblDepositData.getValueAt(i, 18));
                                    depositDataMap.put("REMARKS", tblDepositData.getValueAt(i, 19));
                                    
                                }
                                depositDataList.add(depositDataMap);
                            }
                        }
                    }
                    HashMap depositOpeningMap = new HashMap();
                    depositOpeningMap.put("DEPOSIT_OPENING_LIST", depositDataList);
                    HashMap statusMap = observable.depositOpeningProcess(depositOpeningMap);
                    System.out.println("deposit opening statusMap :: " + statusMap);
                    observable.setDepositOpeningResultMap(statusMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        resultMap = observable.getDepositOpeningResultMap();
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            if (resultMap != null && resultMap.containsKey("DEPOSIT_OPENING_STATUS") && resultMap.get("DEPOSIT_OPENING_STATUS") != null && resultMap.get("DEPOSIT_OPENING_STATUS").equals("SUCCESS")) {
                ClientUtil.showMessageWindow("Deposit opening completed successfully !!!");
            }
            observable.removeRowsFromGuarantorTable(tblDepositData);
            ClientUtil.clearAll(this);
            isEdit = false;
            btnSave.setEnabled(true);
            btnCustCreationClearActionPerformed(null);
        }
    }//GEN-LAST:event_btnDepositProcessActionPerformed

    private void btnDepositCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositCloseActionPerformed
        // TODO add your handling code here:
        observable.removeRowsFromRiskFundTable(tblShareData);
        dispose();
    }//GEN-LAST:event_btnDepositCloseActionPerformed

    private void btnDepositClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositClearActionPerformed
        // TODO add your handling code here:
             // TODO add your handling code here:
        ClientUtil.clearAll(panDeposit);
        lblDepositRecordsVal.setText("");
        lblShareSelectedRecordsVAl.setText("");
        lblShareTotalAmtVal.setText("");
        lblDepositTotalAmtVal.setText("");
        lblDepositSelectedRecords.setText("");
       
        observable.setRiskFundProdId("");
        observable.setRiskFundProdDesc("");
        observable.setRiskFundAcctHead("");
    }//GEN-LAST:event_btnDepositClearActionPerformed

    private void cboCustAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCustAgentIdActionPerformed
        // TODO add your handling code here:
          if (cboCustAgentId.getSelectedIndex() > 0) {
//            HashMap agentMap = new HashMap();
//            agentMap.put("AGENT_ID", CommonUtil.convertObjToStr(cboCustAgentId.getSelectedItem()));
//            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
//            agentMap = null;
//            if (lst != null && lst.size() > 0) {
//                agentMap = (HashMap) lst.get(0);
//                lblCustAgentName.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
//            }
              lblCustAgentName.setText(CommonUtil.convertObjToStr(observable.getCbmCustAgentId().getKeyForSelected()));
        } else {
            lblCustAgentName.setText("                                ");            
        }
    }//GEN-LAST:event_cboCustAgentIdActionPerformed

    private void cboShareAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboShareAgentIdActionPerformed
        if (cboShareAgentId.getSelectedIndex() > 0) {
//            HashMap agentMap = new HashMap();
//            agentMap.put("AGENT_ID", CommonUtil.convertObjToStr(cboShareAgentId.getSelectedItem()));
//            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
//            agentMap = null;
//            if (lst != null && lst.size() > 0) {
//                agentMap = (HashMap) lst.get(0);
//                lblShareAgentName.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
//            }
            lblShareAgentName.setText(CommonUtil.convertObjToStr(observable.getCbmShareAgentId().getKeyForSelected()));
        } else {
            lblShareAgentName.setText("                                ");            
        }
    }//GEN-LAST:event_cboShareAgentIdActionPerformed

    public void populateShareData() {       
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        if (isOK) {
            viewMap.put(CommonConstants.MAP_NAME, "getMobileAppDataForShareOpening");          
            whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(observable.getCbmShareAgentId().getKeyForSelected()));
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateShareData(viewMap, tblShareData);
                //setColorLists();
                //setColour();
                lblShareRecordsVal.setText(String.valueOf(tblShareData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    public void populateDepositData() {       
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        if (isOK) {
            viewMap.put(CommonConstants.MAP_NAME, "getMobileAppDataForDepositOpening");          
            whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(observable.getCbmDepositAgentId().getKeyForSelected()));
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateDepositData(viewMap, tblDepositData);
                lblDepositRecordsVal.setText(String.valueOf(tblDepositData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
    
    
    
    private void btnShowMobDataShareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMobDataShareActionPerformed
         if (cboShareAgentId.getSelectedIndex() == 0) {
            ClientUtil.displayAlert("Select an Agent !!");
        } else {
            //Get All share details
            List lst = ClientUtil.executeQuery("getAllShareFeeDetails", null);
            if(lst != null && lst.size() > 0){
                for(int i=0; i<lst.size(); i++){
                    HashMap shareMap = (HashMap)lst.get(i);
                    HashMap eachShareMap = new  HashMap();
                    //System.out.println("shareMap :: " + shareMap);
                    eachShareMap.put("FACE_VALUE", shareMap.get("FACE_VALUE"));
                    eachShareMap.put("ADMISSION_FEE",shareMap.get("ADMISSION_FEE"));
                    eachShareMap.put("APPLICATION_FEE",shareMap.get("ADMISSION_FEE"));
                    eachShareMap.put("RATIFICATION_REQUIRED",shareMap.get("RATIFICATION_REQUIRED"));
                    //System.out.println("eachShareMap :: " + eachShareMap);
                    shareDetailsMap.put(shareMap.get("SHARE_TYPE"),eachShareMap);
                }
            }
             //System.out.println("allSahreMap :: " + shareDetailsMap);
            observable.setShareDetailsMap(shareDetailsMap);
            lblShareRecordsVal.setText("");
            isEdit = false;
            btnShareProcess.setEnabled(true);
            populateShareData();
            if(tblShareData.getRowCount() > 0){
             tblShareData.getColumnModel().getColumn(1).setPreferredWidth(90);
            }
            //tblShareData.getColumnModel().getColumn(7).setPreferredWidth(100);
            //tblShareData.getColumnModel().getColumn(8).setPreferredWidth(100);
        }
    }//GEN-LAST:event_btnShowMobDataShareActionPerformed

    private void cboDepositAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboDepositAgentIdActionPerformed
        // TODO add your handling code here:
        if (cboDepositAgentId.getSelectedIndex() > 0) {
//            HashMap agentMap = new HashMap();
//            agentMap.put("AGENT_ID", CommonUtil.convertObjToStr(cboDepositAgentId.getSelectedItem()));
//            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
//            agentMap = null;
//            if (lst != null && lst.size() > 0) {
//                agentMap = (HashMap) lst.get(0);
//                lblDepositAgentName.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
//            }
            lblDepositAgentName.setText(CommonUtil.convertObjToStr(observable.getCbmDepositAgentId().getKeyForSelected()));
        } else {
            lblDepositAgentName.setText("                                ");
        }
    }//GEN-LAST:event_cboDepositAgentIdActionPerformed

    private void btnShowMobDataDepositActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMobDataDepositActionPerformed
        if (cboDepositAgentId.getSelectedIndex() == 0) {
            ClientUtil.displayAlert("Select an Agent !!");
        } else {
            lblDepositRecordsVal.setText("");
            isEdit = false;
            btnSave.setEnabled(true);
            populateDepositData();
            tblDepositData.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblDepositData.getColumnModel().getColumn(6).setPreferredWidth(150);
        }
    }//GEN-LAST:event_btnShowMobDataDepositActionPerformed

    private void cboSBAgentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSBAgentIdActionPerformed
        // TODO add your handling code here:
         if (cboSBAgentId.getSelectedIndex() > 0) {
//            HashMap agentMap = new HashMap();
//            agentMap.put("AGENT_ID", CommonUtil.convertObjToStr(cboSBAgentId.getSelectedItem()));
//            List lst = ClientUtil.executeQuery("getAgentDetailsName", agentMap);
//            agentMap = null;
//            if (lst != null && lst.size() > 0) {
//                agentMap = (HashMap) lst.get(0);
//                lblSBAgentName.setText(CommonUtil.convertObjToStr(agentMap.get("AGENT_NAME")));
//            }
            lblSBAgentName.setText(CommonUtil.convertObjToStr(observable.getCbmSBAgentId().getKeyForSelected()));
        } else {
            lblSBAgentName.setText("                                ");
        }
    }//GEN-LAST:event_cboSBAgentIdActionPerformed

    private void btnShowMobDataSBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowMobDataSBActionPerformed
        // TODO add your handling code here:
          if (cboSBAgentId.getSelectedIndex() == 0) {
            ClientUtil.displayAlert("Select an Agent !!");
        } else {
            lblSBRecordsVal.setText("");
            isEdit = false;
            btnSave.setEnabled(true);
            populateSBData();
            tblSBData.getColumnModel().getColumn(1).setPreferredWidth(90);
            tblSBData.getColumnModel().getColumn(6).setPreferredWidth(200);
        }
    }//GEN-LAST:event_btnShowMobDataSBActionPerformed

    
    public void populateSBData() {       
        HashMap viewMap = new HashMap();
        HashMap whereMap = new HashMap();
        boolean isOK = true;
        if (isOK) {
            viewMap.put(CommonConstants.MAP_NAME, "getMobileAppDataForSBOpening");          
            whereMap.put("AGENT_ID", CommonUtil.convertObjToStr(observable.getCbmSBAgentId().getKeyForSelected()));
            whereMap.put("BRANCH_CODE", ProxyParameters.BRANCH_ID);
            whereMap.put("CURR_DATE", getProperDate(currDt));
            viewMap.put(CommonConstants.MAP_WHERE, whereMap);
            try {
                ArrayList heading = observable.populateSBData(viewMap, tblSBData);
                lblSBRecordsVal.setText(String.valueOf(tblSBData.getRowCount()));
                heading = null;
            } catch (Exception e) {
                System.err.println("Exception " + e.toString() + "Caught");
                e.printStackTrace();
            }
        }
        viewMap = null;
        whereMap = null;
    }
    
 
    private void chkSBSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSBSelectAllActionPerformed
        // TODO add your handling code here:        
        if (tblSBData.getRowCount() > 0) {
            if (chkSBSelectAll.isSelected()) {
                for (int i = 0; i < tblSBData.getRowCount(); i++) {
                    tblSBData.setValueAt(true, i, 0);
                }
            } else {
                for (int i = 0; i < tblSBData.getRowCount(); i++) {
                    tblSBData.setValueAt(false, i, 0);
                }
            }
        }
        calculateSBAmountVal();
    }//GEN-LAST:event_chkSBSelectAllActionPerformed

    private void calculateSBAmountVal(){
        double total = 0.0;
        int count = 0;
        if(tblSBData.getRowCount() > 0){
            for(int i=0; i<tblSBData.getRowCount(); i++){
                if(tblSBData.getValueAt(i, 0).equals(true)){
                    total += CommonUtil.convertObjToDouble(tblSBData.getValueAt(i, 8));
                    count ++;
                }
            }
        }
        lblSBSelectedRecords.setText(String.valueOf(count));
        lblSBTotalAmtVal.setText(String.valueOf(total));
    }
    
    
    private void tblSBDataMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSBDataMouseClicked
        // TODO add your handling code here:
        calculateSBAmountVal();
    }//GEN-LAST:event_tblSBDataMouseClicked

    private void tblSBDataMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSBDataMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSBDataMousePressed

    private void tblSBDataMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSBDataMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSBDataMouseReleased

    private void tblSBDataMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSBDataMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_tblSBDataMouseMoved

    private void btnSBProcessActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSBProcessActionPerformed
        // TODO add your handling code here:
        
             HashMap resultMap = new HashMap();
        CommonUtil comm = new CommonUtil();
        final JDialog loading = comm.addProgressBar();
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException /**
             * Execute some operation
             */
            {
                ArrayList SBDataList = new ArrayList();
                try {
                    if (tblSBData.getRowCount() > 0) {
                        for (int i = 0; i < tblSBData.getRowCount(); i++) {
                            HashMap SBDataMap = new HashMap();
                            if (((Boolean) tblSBData.getValueAt(i, 0)).booleanValue()) {
                                for (int j = 1; j < tblSBData.getColumnCount(); j++) {                               
                                    SBDataMap.put("MOB_DEP_ID", tblSBData.getValueAt(i, 1));
                                    SBDataMap.put("CUST_ID", tblSBData.getValueAt(i, 2));
                                    SBDataMap.put("FNAME", tblSBData.getValueAt(i, 3));
                                    SBDataMap.put("SHARE_NO", tblSBData.getValueAt(i, 4));
                                    SBDataMap.put("PROD_ID", tblSBData.getValueAt(i, 5));
                                    SBDataMap.put("PRODUCT", tblSBData.getValueAt(i, 6));
                                    SBDataMap.put("BEHAVES_LIKE", tblSBData.getValueAt(i, 7));
                                    SBDataMap.put("AMOUNT", tblSBData.getValueAt(i, 8));
                                    SBDataMap.put("TOTAL_AMOUNT", tblSBData.getValueAt(i, 8));                                   
                                    SBDataMap.put("DEPOSIT_DT", tblSBData.getValueAt(i, 9));
                                    SBDataMap.put("MOBILE_NO", tblSBData.getValueAt(i, 10));
                                    SBDataMap.put("NOMINEE_NAME", tblSBData.getValueAt(i, 11));                                   
                                    SBDataMap.put("RELATIONSHIP", tblSBData.getValueAt(i, 12));
                                    SBDataMap.put("REMARKS", tblSBData.getValueAt(i, 13));
                                }
                                SBDataList.add(SBDataMap);
                            }
                        }
                    }
                    HashMap SBOpeningMap = new HashMap();
                    SBOpeningMap.put("SB_OPENING_LIST", SBDataList);
                    HashMap statusMap = observable.SBOpeningProcess(SBOpeningMap);
                    System.out.println("SB opening statusMap :: " + statusMap);
                    observable.setSBOpeningResultMap(statusMap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                loading.dispose();
            }
        };
        worker.execute();
        loading.show();
        try {
            worker.get();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        resultMap = observable.getSBOpeningResultMap();
        if (observable.getResult().equals(ClientConstants.RESULT_STATUS[2])) {
            if (resultMap != null && resultMap.containsKey("SB_OPENING_STATUS") && resultMap.get("SB_OPENING_STATUS") != null && resultMap.get("SB_OPENING_STATUS").equals("SUCCESS")) {
                ClientUtil.showMessageWindow("SB opening completed successfully !!!");
            }
            observable.removeRowsFromGuarantorTable(tblSBData);
            ClientUtil.clearAll(this);
            isEdit = false;
            btnSave.setEnabled(true);
            btnCustCreationClearActionPerformed(null);
        }
        
        
    }//GEN-LAST:event_btnSBProcessActionPerformed

    private void btnSBCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSBCloseActionPerformed
        // TODO add your handling code here:
        observable.removeRowsFromRiskFundTable(tblSBData);
        dispose();
    }//GEN-LAST:event_btnSBCloseActionPerformed

    private void btnSBClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSBClearActionPerformed
        // TODO add your handling code here:
        ClientUtil.clearAll(panSB);
        lblSBRecordsVal.setText("");
        lblSBSelectedRecords.setText("");
        lblSBRecordsVal.setText("");
        lblSBTotalAmtVal.setText("");
       
        observable.setRiskFundProdId("");
        observable.setRiskFundProdDesc("");
        observable.setRiskFundAcctHead("");
    }//GEN-LAST:event_btnSBClearActionPerformed

    private void btnRejectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRejectActionPerformed
        // TODO add your handling code here:
        if(tblCustData.getRowCount() > 0){
             int yesNo = 0;
            String[] options = {"Yes", "No"};
            yesNo = COptionPane.showOptionDialog(null, "Are you sure you want to reject selected rows?", CommonConstants.WARNINGTITLE,
                    COptionPane.OK_OPTION, COptionPane.WARNING_MESSAGE,
                    null, options, options[0]);
            if (yesNo == 0) {
                  for(int i=0; i<tblCustData.getRowCount(); i++){
                if(tblCustData.getValueAt(i, 0).equals(true)){
                    HashMap whereMap = new HashMap();
                    whereMap.put("MOB_DEP_ID",CommonUtil.convertObjToStr(tblCustData.getValueAt(i, 1)));
                    ClientUtil.execute("updateRecteStatusForCustomer", whereMap);
                }
            }
            btnCustCreationClearActionPerformed(null);
            }           
        }
    }//GEN-LAST:event_btnRejectActionPerformed

    private void internationalize() {
//        lblSearch.setText(resourceBundle.getString("lblSearch"));
//        btnSearch.setText(resourceBundle.getString("btnSearch"));
//        chkCase.setText(resourceBundle.getString("chkCase"));
//        btnAuthorize.setText(resourceBundle.getString("btnAuthorize"));
//        btnCancel.setText(resourceBundle.getString("btnCancel"));
    }

    public void calTotalAmount() {
        if (tblCustData.getRowCount() > 0) {
            int count = tblCustData.getRowCount();
            double selectedCount = 0.0;
            for (int i = 0; i < count; i++) {
                if ((Boolean) tblCustData.getValueAt(i, 0)) {
                    selectedCount++;
                }
            }
            lblSelectedRecords.setText(CommonUtil.convertObjToStr(selectedCount));
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        HashMap mapParam = new HashMap();

        HashMap whereMap = new HashMap();
        whereMap.put("USER_ID", "sysadmin1");
        //getSelectOperativeAcctProductAuthorizeTOList
        whereMap.put("INITIATED_BRANCH", ProxyParameters.BRANCH_ID);
        whereMap.put("OUTWARD_DT", ClientUtil.getCurrentDate());
        mapParam.put(CommonConstants.MAP_NAME, "getSelectOutwardClearingRealizeTOList");
        mapParam.put(CommonConstants.MAP_WHERE, whereMap);

        mapParam.put(CommonConstants.UPDATE_MAP_NAME, "authorizeOperativeAcctProduct");

//        AuthorizeUI authorizeUI = new AuthorizeUI(mapParam);
//        authorizeUI.setAuthorize(true);
//        authorizeUI.setException(false);
//        authorizeUI.setReject(false);
//        authorizeUI.setRealize(true);
//        authorizeUI.show();
    }

    public void update(Observable o, Object arg) {
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.see.truetransact.uicomponent.CButton btnClose;
    private com.see.truetransact.uicomponent.CButton btnCustCreationClear;
    private com.see.truetransact.uicomponent.CButton btnDepositClear;
    private com.see.truetransact.uicomponent.CButton btnDepositClose;
    private com.see.truetransact.uicomponent.CButton btnDepositProcess;
    private com.see.truetransact.uicomponent.CButton btnProcess;
    private com.see.truetransact.uicomponent.CButton btnReject;
    private com.see.truetransact.uicomponent.CButton btnSBClear;
    private com.see.truetransact.uicomponent.CButton btnSBClose;
    private com.see.truetransact.uicomponent.CButton btnSBProcess;
    private com.see.truetransact.uicomponent.CButton btnSave;
    private com.see.truetransact.uicomponent.CButton btnShareClear;
    private com.see.truetransact.uicomponent.CButton btnShareClose;
    private com.see.truetransact.uicomponent.CButton btnShareProcess;
    private com.see.truetransact.uicomponent.CButton btnShowMobDataDeposit;
    private com.see.truetransact.uicomponent.CButton btnShowMobDataSB;
    private com.see.truetransact.uicomponent.CButton btnShowMobDataShare;
    private com.see.truetransact.uicomponent.CLabel cLabel1;
    private com.see.truetransact.uicomponent.CLabel cLabel2;
    private com.see.truetransact.uicomponent.CLabel cLabel4;
    private com.see.truetransact.uicomponent.CLabel cLabel5;
    private com.see.truetransact.uicomponent.CLabel cLabel6;
    private com.see.truetransact.uicomponent.CLabel cLabel7;
    private com.see.truetransact.uicomponent.CLabel cLabel8;
    private com.see.truetransact.uicomponent.CSeparator cSeparator1;
    private com.see.truetransact.uicomponent.CSeparator cSeparator2;
    private com.see.truetransact.uicomponent.CSeparator cSeparator3;
    private com.see.truetransact.uicomponent.CTabbedPane cTabbedPane1;
    private com.see.truetransact.uicomponent.CComboBox cboCustAgentId;
    private com.see.truetransact.uicomponent.CComboBox cboDepositAgentId;
    private com.see.truetransact.uicomponent.CComboBox cboSBAgentId;
    private com.see.truetransact.uicomponent.CComboBox cboShareAgentId;
    private com.see.truetransact.uicomponent.CCheckBox chkDepositSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkRiskFundSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSBSelectAll;
    private com.see.truetransact.uicomponent.CCheckBox chkSelectAll;
    private com.see.truetransact.uicomponent.CLabel lblAgentId;
    private com.see.truetransact.uicomponent.CLabel lblCustAgentName;
    private com.see.truetransact.uicomponent.CLabel lblDepositAgentName;
    private com.see.truetransact.uicomponent.CLabel lblDepositRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblDepositSelectedRecords;
    private com.see.truetransact.uicomponent.CLabel lblDepositTotalAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblNoOfRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblProdType3;
    private com.see.truetransact.uicomponent.CLabel lblProdType4;
    private com.see.truetransact.uicomponent.CLabel lblProdType5;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundAccts;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundAccts1;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundNoOfRecords;
    private com.see.truetransact.uicomponent.CLabel lblRiskFundNoOfRecords1;
    private com.see.truetransact.uicomponent.CLabel lblSBAccts;
    private com.see.truetransact.uicomponent.CLabel lblSBAgentName;
    private com.see.truetransact.uicomponent.CLabel lblSBRecords;
    private com.see.truetransact.uicomponent.CLabel lblSBRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblSBSelectedRecords;
    private com.see.truetransact.uicomponent.CLabel lblSBTotalAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblSelectedRecords;
    private com.see.truetransact.uicomponent.CLabel lblShareAgentName;
    private com.see.truetransact.uicomponent.CLabel lblShareRecordsVal;
    private com.see.truetransact.uicomponent.CLabel lblShareSelectedRecordsVAl;
    private com.see.truetransact.uicomponent.CLabel lblShareTotalAmtVal;
    private com.see.truetransact.uicomponent.CLabel lblToDate1;
    private com.see.truetransact.uicomponent.CLabel lblTotalRiskFundVal1;
    private com.see.truetransact.uicomponent.CLabel lblTotalSBVal;
    private com.see.truetransact.uicomponent.CPanel panAgentSB;
    private com.see.truetransact.uicomponent.CPanel panAgentSBData;
    private com.see.truetransact.uicomponent.CPanel panArbit;
    private com.see.truetransact.uicomponent.CPanel panArbit3;
    private com.see.truetransact.uicomponent.CPanel panArbit4;
    private com.see.truetransact.uicomponent.CPanel panDeposit;
    private com.see.truetransact.uicomponent.CPanel panKCCREnewal;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch1;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch4;
    private com.see.truetransact.uicomponent.CPanel panMultiSearch5;
    private com.see.truetransact.uicomponent.CPanel panMultiSearchSB;
    private com.see.truetransact.uicomponent.CPanel panRiskFund;
    private com.see.truetransact.uicomponent.CPanel panRiskFundTable;
    private com.see.truetransact.uicomponent.CPanel panRiskFundTable1;
    private com.see.truetransact.uicomponent.CPanel panRiskFundTotDetails;
    private com.see.truetransact.uicomponent.CPanel panRiskFundTotDetails1;
    private com.see.truetransact.uicomponent.CPanel panSB;
    private com.see.truetransact.uicomponent.CPanel panSBButtons;
    private com.see.truetransact.uicomponent.CPanel panSBTotDetails;
    private com.see.truetransact.uicomponent.CPanel panSearch;
    private com.see.truetransact.uicomponent.CPanel panSearch1;
    private com.see.truetransact.uicomponent.CPanel panSearch2;
    private com.see.truetransact.uicomponent.CPanel panSearchCondition;
    private com.see.truetransact.uicomponent.CPanel panTable;
    private com.see.truetransact.uicomponent.CButtonGroup rdbArbit;
    private com.see.truetransact.uicomponent.CSeparator sptLine;
    private com.see.truetransact.uicomponent.CScrollPane srcRiskFundTable;
    private com.see.truetransact.uicomponent.CScrollPane srcTable;
    private com.see.truetransact.uicomponent.CScrollPane srpDepositData;
    private com.see.truetransact.uicomponent.CScrollPane srpSBData;
    private com.see.truetransact.uicomponent.CTable tblCustData;
    private com.see.truetransact.uicomponent.CTable tblDepositData;
    private com.see.truetransact.uicomponent.CTable tblSBData;
    private com.see.truetransact.uicomponent.CTable tblShareData;
    // End of variables declaration//GEN-END:variables
}
